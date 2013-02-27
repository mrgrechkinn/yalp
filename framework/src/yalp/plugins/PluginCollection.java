package yalp.plugins;

import yalp.Logger;
import yalp.Yalp;
import yalp.YalpPlugin;
import yalp.classloading.ApplicationClasses;
import yalp.classloading.ApplicationClassloader;
import yalp.data.binding.RootParamNode;
import yalp.db.Model;
import yalp.exceptions.UnexpectedException;
import yalp.mvc.Http;
import yalp.mvc.Router;
import yalp.mvc.results.Result;
import yalp.templates.BaseTemplate;
import yalp.templates.Template;
import yalp.test.BaseTest;
import yalp.test.TestEngine;
import yalp.vfs.VirtualFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

/**
 * Class handling all plugins used by Yalp.
 *
 * Loading/reloading/enabling/disabling is handled here.
 *
 * This class also exposes many YalpPlugin-methods which
 * when called, the method is executed on all enabled plugins.
 *
 * Since all the enabled-plugins-iteration is done here,
 * the code elsewhere is cleaner.
 */
public class PluginCollection {

    /**
     * Property holding the name of the yalp.plugins-resource-name.
     * Can be modified in unittest to supply modifies plugin-list
     */
    protected String yalp_plugins_resourceName = "yalp.plugins";

    protected Object lock = new Object();
    /**
     * List that holds all loaded plugins, enabled or disabled
     */
    protected List<YalpPlugin> allPlugins = new ArrayList<YalpPlugin>();

    /**
     * Readonly copy of allPlugins - updated each time allPlugins is updated.
     * Using this cached copy so we don't have to create it all the time..
     */
    protected List<YalpPlugin> allPlugins_readOnlyCopy = createReadonlyCopy(allPlugins);

    /**
     * List of all enabled plugins
     */
    protected List<YalpPlugin> enabledPlugins = new ArrayList<YalpPlugin>();

    /**
     * Readonly copy of enabledPlugins - updated each time enabledPlugins is updated.
     * Using this cached copy so we don't have to create it all the time
     */
    protected List<YalpPlugin> enabledPlugins_readOnlyCopy = createReadonlyCopy(enabledPlugins);


    /**
     * Using readonly list to crash if someone tries to modify the copy.
     * @param list
     * @return
     */
    protected List<YalpPlugin> createReadonlyCopy( List<YalpPlugin> list ){
        return Collections.unmodifiableList( new ArrayList<YalpPlugin>( list ));
    }


    private static class LoadingPluginInfo implements Comparable<LoadingPluginInfo> {
        public final String name;
        public final int index;
        public final URL url;

        private LoadingPluginInfo(String name, int index, URL url) {
            this.name = name;
            this.index = index;
            this.url = url;
        }

        @Override
        public String toString() {
            return "LoadingPluginInfo{" +
                    "name='" + name + '\'' +
                    ", index=" + index +
                    ", url=" + url +
                    '}';
        }

        public int compareTo(LoadingPluginInfo o) {
            int res = index < o.index ? -1 : (index == o.index ? 0 : 1);
            if (res != 0) {
                return res;
            }

            // index is equal in both plugins.
            // sort on name to get consistent order
            return name.compareTo(o.name);
        }
    }
    /**
     * Enable found plugins
     */
    public void loadPlugins() {
        Logger.trace("Loading plugins");
        // Yalp plugins
        Enumeration<URL> urls = null;
        try {
            urls = Yalp.classloader.getResources( yalp_plugins_resourceName);
        } catch (Exception e) {
            Logger.error("Error loading yalp.plugins", e);
            return ;
        }

        // First we build one big list of all plugins to load, then we sort it based
        // on index before we load the classes.
        // This must be done to make sure the enhancing is happening
        // when loading plugins using other classes that must be enhanced.
        List<LoadingPluginInfo> pluginsToLoad = new ArrayList<LoadingPluginInfo>();
        while (urls != null && urls.hasMoreElements()) {
            URL url = urls.nextElement();
            Logger.trace("Found one plugins descriptor, %s", url);
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "utf-8"));
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.trim().length() == 0) {
                        continue;
                    }
                    String[] lineParts = line.split(":");
                    LoadingPluginInfo info = new LoadingPluginInfo(lineParts[1].trim(), Integer.parseInt(lineParts[0]), url);
                    pluginsToLoad.add(info);
                }
            } catch (Exception e) {
                Logger.error("Error interpreting %s", url );
            }

        }

        // sort it
        Collections.sort(pluginsToLoad);

        for ( LoadingPluginInfo info : pluginsToLoad) {
            Logger.trace("Loading plugin %s", info.name);
            try {
                YalpPlugin plugin = (YalpPlugin) Yalp.classloader.loadClass(info.name).newInstance();
                plugin.index = info.index;
                if( addPlugin(plugin) ){
                    Logger.trace("Loaded plugin %s", plugin);
                }else{
                    Logger.warn("Did not load plugin %s. Already loaded", plugin);
                }
            } catch (Exception ex) {
                Logger.error(ex, "Error loading plugin %s", info.toString());
            }
        }
        //now we must call onLoad for all plugins - and we must detect if a plugin
        //disables another plugin the old way, by removing it from Yalp.plugins.
        for( YalpPlugin plugin : getEnabledPlugins()){

            //is this plugin still enabled?
            if( isEnabled(plugin)){
                initializePlugin(plugin);
            }
        }

        //must update Yalp.plugins-list one last time
        updateYalpPluginsList();

    }

    /**
     * Reloads all loaded plugins that is application-supplied.
     */
    public void reloadApplicationPlugins() throws Exception{
        Set<YalpPlugin> reloadedPlugins = new HashSet<YalpPlugin>();

        for (YalpPlugin plugin : getAllPlugins()) {

            //Is this plugin an application-supplied-plugin?
            if (isLoadedByApplicationClassloader(plugin)) {
                //This plugin is application-supplied - Must reload it
                String pluginClassName = plugin.getClass().getName();
                Class pluginClazz = Yalp.classloader.loadClass( pluginClassName);

                //first looking for constructors the old way
                Constructor<?>[] constructors = pluginClazz.getConstructors();

                if( constructors.length == 0){
                    //no constructors in plugin
                    //using getDeclaredConstructors() instead of getConstructors() to make it work for plugins without constructor
                    constructors = pluginClazz.getDeclaredConstructors();
                }

                YalpPlugin newPlugin = (YalpPlugin) constructors[0].newInstance();
                newPlugin.index = plugin.index;
                //replace this plugin
                replacePlugin(plugin, newPlugin);
                reloadedPlugins.add(newPlugin);
            }
        }

        //now we must call onLoad for all reloaded plugins
        for( YalpPlugin plugin : reloadedPlugins ){
            initializePlugin( plugin );
        }

        updateYalpPluginsList();

    }

    protected boolean isLoadedByApplicationClassloader(YalpPlugin plugin) {
        return plugin.getClass().getClassLoader().getClass().equals(ApplicationClassloader.class);
    }


    /**
     * Calls plugin.onLoad but detects if plugin removes other plugins from Yalp.plugins-list to detect
     * if plugins disables a plugin the old hacked way..
     * @param plugin
     */
    @SuppressWarnings({"deprecation"})
    protected void initializePlugin(YalpPlugin plugin) {
        Logger.trace("Initializing plugin " + plugin);
        //we're ready to call onLoad for this plugin.
        //must create a unique Yalp.plugins-list for this onLoad-method-call so
        //we can detect if some plugins are removed/disabled
        Yalp.plugins = new ArrayList<YalpPlugin>( getEnabledPlugins() );
        plugin.onLoad();
        //check for missing/removed plugins
        for( YalpPlugin enabledPlugin : getEnabledPlugins()){
            if( !Yalp.plugins.contains( enabledPlugin)) {
                Logger.info("Detected that plugin '" + plugin + "' disabled the plugin '" + enabledPlugin + "' the old way - should use Yalp.disablePlugin()");
                //this enabled plugin was disabled.
                //must disable it in pluginCollection
                disablePlugin( enabledPlugin);
            }
        }
    }


    /**
     * Adds one plugin and enables it
     * @param plugin
     * @return true if plugin was new and was added
     */
    protected boolean addPlugin( YalpPlugin plugin ){
        synchronized( lock ){
            if( !allPlugins.contains(plugin) ){
                allPlugins.add( plugin );
                Collections.sort(allPlugins);
                allPlugins_readOnlyCopy = createReadonlyCopy( allPlugins);
                enablePlugin(plugin);
                return true;
            }
        }
        return false;
    }

    protected void replacePlugin( YalpPlugin oldPlugin, YalpPlugin newPlugin){
        synchronized( lock ){
            if( allPlugins.remove( oldPlugin )){
                allPlugins.add( newPlugin);
                Collections.sort( allPlugins);
                allPlugins_readOnlyCopy = createReadonlyCopy( allPlugins);
            }

            if( enabledPlugins.remove( oldPlugin )){
                enabledPlugins.add(newPlugin);
                Collections.sort( enabledPlugins);
                enabledPlugins_readOnlyCopy = createReadonlyCopy( enabledPlugins);
            }

        }
    }

    /**
     * Enable plugin.
     *
     * @param plugin
     * @return true if plugin exists and was enabled now
     */
    public boolean enablePlugin( YalpPlugin plugin ){
        synchronized( lock ){
            if( allPlugins.contains( plugin )){
                //the plugin exists
                if( !enabledPlugins.contains( plugin )){
                    //plugin not currently enabled
                    enabledPlugins.add( plugin );
                    Collections.sort( enabledPlugins);
                    enabledPlugins_readOnlyCopy = createReadonlyCopy( enabledPlugins);
                    updateYalpPluginsList();
                    Logger.trace("Plugin " + plugin + " enabled");
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * enable plugin of specified type
     * @return true if plugin was enabled
     */
    public boolean enablePlugin( Class<? extends YalpPlugin> pluginClazz ){
        return enablePlugin(getPluginInstance(pluginClazz));
    }


    /**
     * Returns the first instance of a loaded plugin of specified type
     * @param pluginClazz
     * @return
     */
    public YalpPlugin getPluginInstance( Class<? extends YalpPlugin> pluginClazz){
        synchronized( lock ){
            for( YalpPlugin p : getAllPlugins()){
                if (pluginClazz.isInstance(p)) {
                    return p;
                }
            }
        }
        return null;
    }


    /**
     * disable plugin
     * @param plugin
     * @return true if plugin was enabled and now is disabled
     */
    public boolean disablePlugin( YalpPlugin plugin ){
        synchronized( lock ){
            //try to disable it?
            if( enabledPlugins.remove( plugin ) ){
                //plugin was removed
                enabledPlugins_readOnlyCopy = createReadonlyCopy( enabledPlugins);
                updateYalpPluginsList();
                Logger.trace("Plugin " + plugin + " disabled");
                return true;
            }
        }
        return false;
    }

    /**
     * disable plugin of specified type
     * @return true if plugin was enabled and now is disabled
     */
    public boolean disablePlugin( Class<? extends YalpPlugin> pluginClazz ){
        return disablePlugin( getPluginInstance( pluginClazz));
    }



    /**
     * Must update Yalp.plugins-list to be backward compatible
     */
    @SuppressWarnings({"deprecation"})
    public void updateYalpPluginsList(){
        Yalp.plugins = Collections.unmodifiableList( getEnabledPlugins() );
    }

    /**
     * Returns new readonly list of all enabled plugins
     * @return
     */
    public List<YalpPlugin> getEnabledPlugins(){
        return enabledPlugins_readOnlyCopy;
    }
    
    /**
     * Returns readonly view of all enabled plugins in reversed order
     * @return
     */
    public Collection<YalpPlugin> getReversedEnabledPlugins() {
        return new AbstractCollection<YalpPlugin>() {
			
		    @Override public Iterator<YalpPlugin> iterator() {
		    	final ListIterator<YalpPlugin> enabledPluginsListIt = enabledPlugins.listIterator(size() - 1);
		        return new Iterator<YalpPlugin>() {

					@Override
					public boolean hasNext() {
						return enabledPluginsListIt.hasPrevious();
					}

					@Override
					public YalpPlugin next() {
						return enabledPluginsListIt.previous();
					}

					@Override
					public void remove() {
						enabledPluginsListIt.remove();
					}};
		      }

		      @Override public int size() {
		        return enabledPlugins.size();
		      }			
			
			
		};
    }

    /**
     * Returns new readonly list of all plugins
     * @return
     */
    public List<YalpPlugin> getAllPlugins(){
        return allPlugins_readOnlyCopy;
    }


    /**
     *
     * @param plugin
     * @return true if plugin is enabled
     */
    public boolean isEnabled( YalpPlugin plugin){
        return getEnabledPlugins().contains( plugin );
    }

    public boolean compileSources() {
        for( YalpPlugin plugin : getEnabledPlugins() ){
            if(plugin.compileSources()) {
                return true;
            }
        }
        return false;
    }

    public boolean detectClassesChange() {
        for(YalpPlugin plugin : getEnabledPlugins()){
            if(plugin.detectClassesChange()) {
                return true;
            }
        }
        return false;
    }

    public void invocationFinally(){
        for( YalpPlugin plugin : getEnabledPlugins() ){
            plugin.invocationFinally();
        }
    }

    public void beforeInvocation(){
        for( YalpPlugin plugin : getEnabledPlugins() ){
            plugin.beforeInvocation();
        }
    }

    public void afterInvocation(){
        for( YalpPlugin plugin : getEnabledPlugins() ){
            plugin.afterInvocation();
        }
    }

    public void onInvocationSuccess(){
        for( YalpPlugin plugin : getEnabledPlugins() ){
            plugin.onInvocationSuccess();
        }
    }

    public void onInvocationException(Throwable e) {
        for (YalpPlugin plugin : getEnabledPlugins()) {
            try {
                plugin.onInvocationException(e);
            } catch (Throwable ex) {
                //nop
            }
        }
    }

    public void beforeDetectingChanges(){
        for( YalpPlugin plugin : getEnabledPlugins() ){
            plugin.beforeDetectingChanges();
        }
    }

    public void detectChange(){
        for( YalpPlugin plugin : getEnabledPlugins() ){
            plugin.detectChange();
        }
    }

    public void onApplicationReady(){
        for( YalpPlugin plugin : getEnabledPlugins() ){
            plugin.onApplicationReady();
        }
    }

    public void onConfigurationRead(){
        for( YalpPlugin plugin : getEnabledPlugins() ){
            plugin.onConfigurationRead();
        }
    }

    public void onApplicationStart(){
        for (YalpPlugin plugin : getEnabledPlugins()) {
            plugin.onApplicationStart();
        }
    }

    public void afterApplicationStart(){
        for( YalpPlugin plugin : getEnabledPlugins() ){
            plugin.afterApplicationStart();
        }
    }

    public void onApplicationStop(){
        for( YalpPlugin plugin : getReversedEnabledPlugins() ){
            plugin.onApplicationStop();
        }
    }

    public void onEvent(String message, Object context){
        for( YalpPlugin plugin : getEnabledPlugins() ){
            plugin.onEvent(message, context);
        }
    }

    public void enhance(ApplicationClasses.ApplicationClass applicationClass){
        for (YalpPlugin plugin : getEnabledPlugins()) {
            try {
                long start = System.currentTimeMillis();
                plugin.enhance(applicationClass);
                if (Logger.isTraceEnabled()) {
                    Logger.trace("%sms to apply %s to %s", System.currentTimeMillis() - start, plugin, applicationClass.name);
                }
            } catch (Exception e) {
                throw new UnexpectedException("While applying " + plugin + " on " + applicationClass.name, e);
            }
        }
    }

    @Deprecated
    public List<ApplicationClasses.ApplicationClass> onClassesChange(List<ApplicationClasses.ApplicationClass> modified){
        List<ApplicationClasses.ApplicationClass> modifiedWithDependencies = new ArrayList<ApplicationClasses.ApplicationClass>();
        for( YalpPlugin plugin : getEnabledPlugins() ){
            modifiedWithDependencies.addAll( plugin.onClassesChange(modified) );
        }
        return modifiedWithDependencies;
    }

    @Deprecated
    public void compileAll(List<ApplicationClasses.ApplicationClass> classes){
        for( YalpPlugin plugin : getEnabledPlugins() ){
            plugin.compileAll(classes);
        }
    }

    public Object bind(RootParamNode rootParamNode, String name, Class<?> clazz, Type type, Annotation[] annotations){
        for (YalpPlugin plugin : getEnabledPlugins()) {
            Object result = plugin.bind(rootParamNode, name, clazz, type, annotations);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    public Object bindBean(RootParamNode rootParamNode, String name, Object bean){
        for (YalpPlugin plugin : getEnabledPlugins()) {
            Object result = plugin.bindBean(rootParamNode, name, bean);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    public Map<String, Object> unBind(Object src, String name){
        for (YalpPlugin plugin : getEnabledPlugins()) {
            Map<String, Object> r = plugin.unBind(src, name);
            if (r != null) {
                return r;
            }
        }
        return null;
    }

    public Object willBeValidated(Object value){
        for (YalpPlugin plugin : getEnabledPlugins()) {
            Object newValue = plugin.willBeValidated(value);
            if (newValue != null) {
                return newValue;
            }
        }
        return value;
    }

    public Model.Factory modelFactory(Class<? extends Model> modelClass){
        for(YalpPlugin plugin : getEnabledPlugins()) {
            Model.Factory factory = plugin.modelFactory(modelClass);
            if(factory != null) {
                return factory;
            }
        }
        return null;
    }

    public String getMessage(String locale, Object key, Object... args){
        for (YalpPlugin plugin : getEnabledPlugins()) {
            String message = plugin.getMessage(locale, key, args);
            if(message != null) {
                return message;
            }
        }
        return null;
    }

    public void beforeActionInvocation(Method actionMethod){
        for (YalpPlugin plugin : getEnabledPlugins()) {
            plugin.beforeActionInvocation(actionMethod);
        }
    }

    public void onActionInvocationResult(Result result){
        for (YalpPlugin plugin : getEnabledPlugins()) {
            plugin.onActionInvocationResult(result);
        }
    }

    public void afterActionInvocation(){
        for (YalpPlugin plugin : getEnabledPlugins()) {
            plugin.afterActionInvocation();
        }
    }

    public void routeRequest(Http.Request request){
        for (YalpPlugin plugin : getEnabledPlugins()) {
            plugin.routeRequest(request);
        }
    }

    public void onRequestRouting(Router.Route route){
        for (YalpPlugin plugin : getEnabledPlugins()) {
            plugin.onRequestRouting(route);
        }
    }

    public void onRoutesLoaded(){
        for (YalpPlugin plugin : getEnabledPlugins()) {
            plugin.onRoutesLoaded();
        }
    }

    public boolean rawInvocation(Http.Request request, Http.Response response)throws Exception{
        for (YalpPlugin plugin : getEnabledPlugins()) {
            if (plugin.rawInvocation(request, response)) {
                //raw = true;
                return true;
            }
        }
        return false;
    }


    public boolean serveStatic(VirtualFile file, Http.Request request, Http.Response response){
        for (YalpPlugin plugin : getEnabledPlugins()) {
            if (plugin.serveStatic(file, request, response)) {
                //raw = true;
                return true;
            }
        }
        return false;
    }

    public List<String> addTemplateExtensions(){
        List<String> list = new ArrayList<String>();
        for (YalpPlugin plugin : getEnabledPlugins()) {
            list.addAll(plugin.addTemplateExtensions());
        }
        return list;
    }

    public String overrideTemplateSource(BaseTemplate template, String source){
        for(YalpPlugin plugin : getEnabledPlugins()) {
            String newSource = plugin.overrideTemplateSource(template, source);
            if(newSource != null) {
                source = newSource;
            }
        }
        return source;
    }

    public Template loadTemplate(VirtualFile file){
        for(YalpPlugin plugin : getEnabledPlugins() ) {
            Template pluginProvided = plugin.loadTemplate(file);
            if(pluginProvided != null) {
                return pluginProvided;
            }
        }
        return null;
    }

    public void afterFixtureLoad(){
        for (YalpPlugin plugin : getEnabledPlugins()) {
            plugin.afterFixtureLoad();
        }
    }

    public TestEngine.TestResults runTest(Class<BaseTest> clazz){
        for (YalpPlugin plugin : getEnabledPlugins()) {
            TestEngine.TestResults pluginTestResults = plugin.runTest(clazz);
            if (pluginTestResults != null) {
                return pluginTestResults;
            }
        }
        return null;
    }

    public Collection<Class> getUnitTests() {
        Set<Class> allPluginTests = new HashSet<Class>();
        for (YalpPlugin plugin : getEnabledPlugins()) {
            Collection<Class> unitTests = plugin.getUnitTests();
            if(unitTests != null) {
                allPluginTests.addAll(unitTests);
            }
        }
        
        return allPluginTests;
    }
    
    public Collection<Class> getFunctionalTests() {
        Set<Class> allPluginTests = new HashSet<Class>();
        for (YalpPlugin plugin : getEnabledPlugins()) {
            Collection<Class> funcTests = plugin.getFunctionalTests();
            if(funcTests != null) {
                allPluginTests.addAll(funcTests);
            }
        }
        
        return allPluginTests;
    }
}

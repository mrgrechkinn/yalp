package yalp.plugins;

import yalp.Logger;
import yalp.Yalp;
import yalp.YalpPlugin;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Plugin that reads list of plugins to disable from application.conf
 *
 *
 * To disable plugins, specify it like this in application.conf:
 *
 * plugins.disable=full-plugin-class-name
 * plugins.disable.0=full-plugin-class-name
 * plugins.disable.1=full-plugin-class-name
 * plugins.disable.whatever=full-plugin-class-name
 *
 */
public class ConfigurablePluginDisablingPlugin extends YalpPlugin {

    /**
     * List holding all disabled plugins.
     * when reloading config, we have to enable hem again, in case,
     * they are no longer listed in the "disable plugins"-section
     */
    protected final static Set<String> previousDisabledPlugins = new HashSet<String>();

    @Override
    public void onConfigurationRead() {
        Logger.trace("Looking for plugins to disable");


        Set<String> disabledPlugins = new HashSet<String>();

        for( Map.Entry<Object, Object> e : Yalp.configuration.entrySet()){
            String key = (String)e.getKey();
            if( key.equals("plugins.disable") || key.startsWith("plugins.disable.")){
                String pluginClassName = (String)e.getValue();
                //try to find this class..
                Class<? extends YalpPlugin> clazz = resolveClass(pluginClassName);
                if( clazz != null ){

                    YalpPlugin pluginInstance = Yalp.pluginCollection.getPluginInstance( clazz );

                    if( pluginInstance != null ){

                        //try to disable it
                        //must remember that we have tries to disabled this plugin
                        disabledPlugins.add( pluginClassName );

                        if( Yalp.pluginCollection.disablePlugin( clazz)){
                            Logger.info("Plugin disabled: " + clazz);

                        }else{
                            Logger.warn("Could not disable Plugin: " + clazz + ". Already disabled?");
                        }
                    }else{
                        Logger.error("Cannot disable plugin " + clazz + ". No loaded plugin of that type");
                    }
                }
            }
        }

        //must look for plugins disabled the last time but not this time.. This can happen
        //when reloading config with changes in disable-list...

        for( String pluginClassName : previousDisabledPlugins ){
            if( !disabledPlugins.contains( pluginClassName)){
                Logger.info("Enabling plugin " + pluginClassName + " since it is now longer listed in plugins.disable section in config");
                Class<? extends YalpPlugin> clazz = resolveClass(pluginClassName);
                if( clazz != null ){
                    //try to disable it
                    if( Yalp.pluginCollection.enablePlugin( clazz)){
                        Logger.info("Plugin reenabled: " + clazz);
                    }else{
                        Logger.warn("Could not reenable Plugin: " + clazz);
                    }
                }
            }
        }

        //remember the plugins we disabled this time until the next time..
        previousDisabledPlugins.clear();
        previousDisabledPlugins.addAll(disabledPlugins);
        
    }

    @SuppressWarnings("unchecked")
    private Class<YalpPlugin> resolveClass(String pluginClassName) {
        try{
            return (Class<YalpPlugin>)getClass().getClassLoader().loadClass(pluginClassName);
        }catch(Exception e){
            Logger.error("Could not disable plugin " + pluginClassName, e);
        }
        return null;
    }
}

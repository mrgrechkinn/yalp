package yalp.plugins;

import java.util.Arrays;
import java.util.Collection;
import org.junit.Test;
import yalp.CorePlugin;
import yalp.Yalp;
import yalp.YalpBuilder;
import yalp.YalpPlugin;
import yalp.classloading.ApplicationClasses;
import yalp.data.parsing.TempFilePlugin;
import yalp.data.validation.ValidationPlugin;
import yalp.db.DBPlugin;
import yalp.db.Evolutions;
import yalp.db.jpa.JPAPlugin;
import yalp.i18n.MessagesPlugin;
import yalp.jobs.JobsPlugin;
import yalp.libs.WS;
import yalp.test.TestEngine;
import yalp.test.UnitTest;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Created by IntelliJ IDEA.
 * User: mortenkjetland
 * Date: 3/3/11
 * Time: 12:14 AM
 * To change this template use File | Settings | File Templates.
 */
public class PluginCollectionTest {

    @Test
    public void verifyLoading() {
        new YalpBuilder().build();
        PluginCollection pc = new PluginCollection();
        pc.loadPlugins();

        //the following plugin-list should match the list in the file 'yalp.plugins'
        assertThat(pc.getEnabledPlugins()).containsExactly(
                pc.getPluginInstance(CorePlugin.class),
                pc.getPluginInstance(TempFilePlugin.class),
                pc.getPluginInstance(ValidationPlugin.class),
                pc.getPluginInstance(DBPlugin.class),
                pc.getPluginInstance(JPAPlugin.class),
                pc.getPluginInstance(Evolutions.class),
                pc.getPluginInstance(MessagesPlugin.class),
                pc.getPluginInstance(WS.class),
                pc.getPluginInstance(JobsPlugin.class),
                pc.getPluginInstance(ConfigurablePluginDisablingPlugin.class));
    }

    @Test
    public void verifyLoadingFromFilesWithBlankLines() throws Exception {
        //verify that only application specific plugins gets reloaded
        new YalpBuilder().build();

        //create custom PluginCollection that fakes that TestPlugin is application plugin
        PluginCollection pc = new PluginCollection(){
            @Override
            protected boolean isLoadedByApplicationClassloader(YalpPlugin plugin) {
                //return true only if This is our TestPlugin
                return plugin.getClass().equals( TestPlugin.class);
            }
        };
        //make sure we load custom yalp.plugins-file
        pc.yalp_plugins_resourceName = "yalp/plugins/custom-yalp-with-blank-lines.plugins";

        pc.loadPlugins();

        YalpPlugin corePlugin_first_instance = pc.getPluginInstance(CorePlugin.class);
        YalpPlugin testPlugin_first_instance = pc.getPluginInstance(TestPlugin.class);

        assertThat(pc.getAllPlugins()).containsExactly(
                corePlugin_first_instance,
                testPlugin_first_instance);

    }

    @Test
    public void verifyReloading() throws Exception{
        //verify that only application specific plugins gets reloaded
        new YalpBuilder().build();


        //create custom PluginCollection that fakes that TestPlugin is application plugin
        PluginCollection pc = new PluginCollection(){
            @Override
            protected boolean isLoadedByApplicationClassloader(YalpPlugin plugin) {
                //return true only if This is our TestPlugin
                return plugin.getClass().equals( TestPlugin.class);
            }
        };
        //make sure we load custom yalp.plugins-file
        pc.yalp_plugins_resourceName = "yalp/plugins/custom-yalp.plugins";

        pc.loadPlugins();

        YalpPlugin corePlugin_first_instance = pc.getPluginInstance(CorePlugin.class);
        YalpPlugin testPlugin_first_instance = pc.getPluginInstance(TestPlugin.class);

        //the following plugin-list should match the list in the file 'yalp.plugins'
        assertThat(pc.getEnabledPlugins()).containsExactly(
                corePlugin_first_instance,
                testPlugin_first_instance);
        assertThat(pc.getAllPlugins()).containsExactly(
                corePlugin_first_instance,
                testPlugin_first_instance);

        pc.reloadApplicationPlugins();

        YalpPlugin testPlugin_second_instance = pc.getPluginInstance(TestPlugin.class);

        assertThat(pc.getPluginInstance(CorePlugin.class)).isEqualTo( corePlugin_first_instance);
        assertThat(testPlugin_second_instance).isNotEqualTo( testPlugin_first_instance);

    }

    @SuppressWarnings({"deprecation"})
    @Test
    public void verifyUpdateYalpPluginsList(){
        new YalpBuilder().build();

        assertThat(Yalp.plugins).isEmpty();

        PluginCollection pc = new PluginCollection();
        pc.loadPlugins();

        assertThat(Yalp.plugins).containsExactly( pc.getEnabledPlugins().toArray());


    }

    @SuppressWarnings({"deprecation"})
    @Test
    public void verifyThatDisabelingPluginsTheOldWayStillWorks(){
        PluginCollection pc = new PluginCollection();


        YalpPlugin legacyPlugin = new LegacyPlugin();

        pc.addPlugin( legacyPlugin );
        pc.addPlugin( new TestPlugin() );

        pc.initializePlugin( legacyPlugin );

        assertThat( pc.getEnabledPlugins() ).containsExactly(legacyPlugin);

        //make sure Yalp.plugins-list is still correct
        assertThat(Yalp.plugins).isEqualTo( pc.getEnabledPlugins() );

    }

    @Test
    public void verifyThatPluginsCanAddUnitTests() {
        PluginCollection pc = new PluginCollection();
        Yalp.pluginCollection = pc;

        assertThat(TestEngine.allUnitTests()).isEmpty();
        assertThat(TestEngine.allFunctionalTests()).isEmpty();

        PluginWithTests p1 = new PluginWithTests();
        PluginWithTests2 p2 = new PluginWithTests2();
        pc.addPlugin(p1);
        pc.addPlugin(p2);

        pc.initializePlugin(p1);
        pc.initializePlugin(p2);

        assertThat(TestEngine.allUnitTests()).contains(PluginUnit.class, PluginUnit2.class);
        assertThat(TestEngine.allFunctionalTests()).contains(PluginFunc.class, PluginFunc2.class);
    }
}


class LegacyPlugin extends YalpPlugin {

    @SuppressWarnings({"deprecation"})
    @Override
    public void onLoad() {
        //find TestPlugin in Yalp.plugins-list and remove it to disable it
        YalpPlugin pluginToRemove = null;
        for( YalpPlugin pp : Yalp.plugins){
            if( pp.getClass().equals( TestPlugin.class)){
                pluginToRemove = pp;
                break;
            }
        }
        Yalp.plugins.remove( pluginToRemove);
    }

}

class PluginWithTests extends YalpPlugin {

    @Override
    public Collection<Class> getUnitTests() {
        return Arrays.asList(new Class[]{PluginUnit.class});
    }

    @Override
    public Collection<Class> getFunctionalTests() {
        return Arrays.asList(new Class[]{PluginFunc.class});
    }
}

class PluginWithTests2 extends YalpPlugin {

    @Override
    public Collection<Class> getUnitTests() {
        return Arrays.asList(new Class[]{PluginUnit2.class});
    }

    @Override
    public Collection<Class> getFunctionalTests() {
        return Arrays.asList(new Class[]{PluginFunc2.class});
    }
}

class PluginUnit {}
class PluginUnit2 {}
class PluginFunc {}
class PluginFunc2 {}

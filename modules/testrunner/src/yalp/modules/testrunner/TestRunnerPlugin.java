package yalp.modules.testrunner;

import java.io.File;

import yalp.Yalp;
import yalp.YalpPlugin;
import yalp.mvc.Router;
import yalp.vfs.VirtualFile;

public class TestRunnerPlugin extends YalpPlugin {

    @Override
    public void onLoad() {
        VirtualFile appRoot = VirtualFile.open(Yalp.applicationPath);
        Yalp.javaPath.add(appRoot.child("test"));
        for (VirtualFile module : Yalp.modules.values()) {
            File modulePath = module.getRealFile();
            if (!modulePath.getAbsolutePath().startsWith(Yalp.frameworkPath.getAbsolutePath()) && !Yalp.javaPath.contains(module.child("test"))) {
                Yalp.javaPath.add(module.child("test"));
            }
        }
    }

    @Override
    public void onRoutesLoaded() {
        Router.addRoute("GET", "/@tests", "TestRunner.index");
        Router.addRoute("GET", "/@tests.list", "TestRunner.list");
        Router.addRoute("GET", "/@tests/{<.*>test}", "TestRunner.run");
        Router.addRoute("POST", "/@tests/{<.*>test}", "TestRunner.saveResult");
        Router.addRoute("GET", "/@tests/emails", "TestRunner.mockEmail");
        Router.addRoute("GET", "/@tests/cache", "TestRunner.cacheEntry");
    }

    @Override
    public void onApplicationReady() {
        String protocol = "http";
        String port = "9000";
        if(Yalp.configuration.getProperty("https.port") != null) {
            port = Yalp.configuration.getProperty("https.port");
            protocol = "https";
        } else if(Yalp.configuration.getProperty("http.port") != null) {
          port = Yalp.configuration.getProperty("http.port");
        }
        System.out.println("~");
        System.out.println("~ Go to "+protocol+"://localhost:" + port + "/@tests to run the tests");
        System.out.println("~");
    }
    
}

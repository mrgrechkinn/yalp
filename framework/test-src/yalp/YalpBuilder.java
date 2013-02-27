package yalp;


import yalp.classloading.ApplicationClasses;
import yalp.classloading.ApplicationClassloader;
import yalp.vfs.VirtualFile;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Properties;

/**
 * Builder-pattern-builder for Yalp-class..
 *
 * It's kind of odd since Yalp only uses statics,
 * But it basically inits the needed properties for Yalp-object to work in unittests
 */
public class YalpBuilder {

    public Properties configuration = new Properties();

    public YalpBuilder withConfiguration(Properties config){
        this.configuration = config;
        return this;
    }


    @SuppressWarnings({"deprecation"})
    public void build(){
        
        Yalp.configuration = configuration;
        Yalp.classes = new ApplicationClasses();
        Yalp.javaPath = new ArrayList<VirtualFile>();
        Yalp.applicationPath = new File(".");
        Yalp.classloader = new ApplicationClassloader();
        Yalp.plugins = Collections.unmodifiableList( new ArrayList<YalpPlugin>());

    }
}

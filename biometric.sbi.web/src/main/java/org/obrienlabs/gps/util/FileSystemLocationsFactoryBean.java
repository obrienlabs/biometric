package org.obrienlabs.gps.util;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.env.Environment;
import org.springframework.core.env.StandardEnvironment;

/**
 *  FactoryBean implementation that loads the list of search locations and provides
 *  an interface for loading configuration files in sequence. 
 */
public class FileSystemLocationsFactoryBean implements FactoryBean<FileSystemLocations>, InitializingBean {

    private String[] settings = null;
    private FileSystemLocations fileSystemLocations = null;
    private Environment env = new StandardEnvironment();

    @Override
    public FileSystemLocations getObject() throws Exception {
        return fileSystemLocations;
    }

    @Override
    public Class<?> getObjectType() {
        return FileSystemLocations.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        fileSystemLocations = new FileSystemLocationsImpl();
        for(int i=0; i<settings.length; i++) {
           fileSystemLocations.setLocation(env.resolveRequiredPlaceholders(settings[i]));
        }
    }
    
    public FileSystemLocations getfileSystemLocations() {
        return fileSystemLocations;
    }

    public void setfileSystemLocations(FileSystemLocations fileSystemLocations) {
        this.fileSystemLocations = fileSystemLocations;
    }

    public void setSettings(String[] settings) {
        this.settings = settings;
    }

    public void setEnv(Environment env) {
        this.env = env;
    }
}

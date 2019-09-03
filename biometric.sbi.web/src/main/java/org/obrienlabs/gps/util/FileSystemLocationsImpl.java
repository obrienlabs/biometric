package org.obrienlabs.gps.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;


public class FileSystemLocationsImpl implements FileSystemLocations {

    private static final Logger log = Logger.getLogger(FileSystemLocationsImpl.class);
    
    private List<String> locations = new ArrayList<String>();

    /**
     * Get the object pointed to by location.
     * @param location - String
     * @return Resource or null if not found
     */
    @Override
    public Resource resolveResource(String location) {
    		        log.trace("Loading " + location);
        
        String[] locationSplit = location.split(":");
        if(locationSplit.length != 2) {
            log.warn("Invalid S3 location format: " + location);
            return null;
        }
        FileSystemResource resource = new FileSystemResource(location);
        try {
            if(!resource.exists()) {
                log.debug("File does not exist: " + location);
                return null;
            }
        } catch(Exception e) {
            log.warn("Error accessing file " + location + ": " + e.getMessage());
            return null;
        }
        return resource;
    }

    @Override
    public String getLocation(int index) {
        return locations.get(index);
    }
    
    @Override
    public List<String> getLocations() {
        return locations;
    }

    @Override
    public void setLocations(List<String> locations) {
        this.locations = locations;
    }
    
    @Override
    public void setLocation(String location) {
        if(null == locations) {
            locations = new ArrayList<String>();
        }
        locations.add(location);
    }
}

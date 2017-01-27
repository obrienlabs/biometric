package com.obrienlabs.gps.util;

import java.util.List;

import org.springframework.core.io.Resource;


/**
 *  Holds the list of search locations and provides
 *  an interface for loading resources from these locations. 
 */
public interface FileSystemLocations {

    static final String FILE_PATH_DELIMITER = "/"; // always unix format for S3
    static final String FILE_PREFIX = "file://"; // dont take out the root last / slash
    
    /**
     * Get the object pointed to by location.
     * @param location - String
     * @return AmazonS3resource or null if not found
     */
    Resource resolveResource(String location);
    
    /** return the location string at index */
    String getLocation(int index);
    
    List<String> getLocations();
    
    void setLocations(List<String> locations);
    
    void setLocation(String location);
}

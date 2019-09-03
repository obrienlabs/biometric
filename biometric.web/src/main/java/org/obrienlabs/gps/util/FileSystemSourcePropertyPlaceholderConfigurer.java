package org.obrienlabs.gps.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;


public class FileSystemSourcePropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer implements ResourceLoaderAware {
	
	private FileSystemLocations fileSystemLocations;
    private String filePath;
    private ResourceLoader resourceLoader;
    
    private String[] nonS3Locations;
    
    private String propertiesName;
    
    @Override
	protected void loadProperties(Properties props) throws IOException {
        loadResources();
		super.loadProperties(props);
	}

    /**
     * Process S3 pes.properties settings in order after non-s3 local properties are loaded.
     * @throws IOException
     */
	private void loadResources() throws IOException {
		List<Resource> resources = new ArrayList<Resource>();
		/*for(String resourceLoc : nonLocations) {
			if(resourceLoader instanceof ResourcePatternResolver) {
				resources.addAll( Arrays.asList(((ResourcePatternResolver) resourceLoader).getResources(resourceLoc)));
			} else {
				resources.add(resourceLoader.getResource(resourceLoc));
			}
		}*/
		
        for(String location : fileSystemLocations.getLocations()) {
        	Resource resource = new FileSystemResource(
                new StringBuffer(location.substring(FileSystemLocations.FILE_PREFIX.length()))
                .append(FileSystemLocations.FILE_PATH_DELIMITER).append(propertiesName).toString());
            if(resource != null) {
                resources.add( resource );
             }
        }
		super.setLocations(resources.toArray(new Resource[0]));
	}

	@Override 
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	public void setNonS3Locations(String[] nonS3Locations) {
		this.nonS3Locations = nonS3Locations;
	}
	

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public FileSystemLocations getFileSystemLocations() {
        return fileSystemLocations;
    }

    public void setFileSystemLocations(FileSystemLocations fileSystemLocations) {
        this.fileSystemLocations = fileSystemLocations;
    }

    public void setPropertiesName(String propertiesName) {
        this.propertiesName = propertiesName;
    }
    
}

package org.obrienlabs.gps.integration;

import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.context.ServletContextAware;

import com.mangofactory.swagger.annotations.ApiIgnore;
import com.mangofactory.swagger.configuration.SpringSwaggerConfig;
import com.mangofactory.swagger.models.dto.ApiInfo;
import com.mangofactory.swagger.paths.RelativeSwaggerPathProvider;
import com.mangofactory.swagger.plugin.EnableSwagger;
import com.mangofactory.swagger.plugin.SwaggerSpringMvcPlugin;

import springfox.documentation.spring.web.plugins.Docket;


@EnableSwagger
public class SwaggerConfig implements ServletContextAware {

	   private static final String PRODUCT_TITLE = "obrienlabs";
	    
	    private static final String API_TITLE = PRODUCT_TITLE + " REST API";
	    
	    private static final String API_DESCRIPTION = "This API describes the REST interface to interact with the " + PRODUCT_TITLE;

	    private static final String API_TERMS_OF_SERVICE_URL = "";

	    private static final String API_CONTACT = "";

	    private static final String API_LICENSE = "";

	    private static final String API_LICENSE_URL = "";
	    
	    /*@Bean
	    public Docket apiOP {
	    	//return new Docket(DocumentationType)
	    }*/
	    
	    @Autowired
	    private SpringSwaggerConfig springSwaggerConfig;

	    private ServletContext servletContext;

	    private static final String ENV_DOC_DEV = "obrienlabs.doc.dev";
	    private static final String NON_VERSIONED_REG_EX = "(?:(?!v1|v2|v3|v4|v5).)*";

	    @PostConstruct
	    public void init()
	    {
	        if (hasDocDev())
	        {
	            springSwaggerConfig.defaultExcludeAnnotations().remove(ApiIgnore.class);
	        }
	    }

	    private boolean hasDocDev()
	    {
	        Properties systemProperties = System.getProperties();
	        return systemProperties.containsKey(ENV_DOC_DEV) && systemProperties.getProperty(ENV_DOC_DEV).equals("true");
	    }

	    
	    @Bean
	    public SwaggerSpringMvcPlugin v3Implementation(){
	        return create("v3");
	    }
	    
	    @Bean
	    public SwaggerSpringMvcPlugin v2Implementation(){
	        return create("v2");
	    }

	    @Bean
	    public SwaggerSpringMvcPlugin allImplementation(){
	        SwaggerSpringMvcPlugin lPlugin = create()
	                							.swaggerGroup("all")
	                							.apiInfo(apiInfo("all"));
	        
	        if (!hasDocDev())
	        {
	        	lPlugin.excludeAnnotations(ApiIgnore.class);
	        }

	        return lPlugin;
	    }

	    @Bean
	    public SwaggerSpringMvcPlugin otherImplementation(){
	        if (hasDocDev()) {
	            return create()
	                    .includePatterns(NON_VERSIONED_REG_EX)
	                    .swaggerGroup("other")
	                    .apiInfo(apiInfo("other"));
	        }
	        else
	        {
	            // Need to return an actual bean here... going to return one that does nothing.
	            return create().enable(false);
	        }
	    }

	    private SwaggerSpringMvcPlugin create(String aInVersion)
	    {
	        return create()
	                //.includePatterns(".*" + aInVersion + ".*")
	        		.includePatterns("/*")
	                .swaggerGroup(aInVersion)
	                .apiInfo(apiInfo(aInVersion));
	    }

	    private SwaggerSpringMvcPlugin create()
	    {
	        RelativeSwaggerPathProvider relativeSwaggerPathProvider = new RelativeSwaggerPathProvider(servletContext);
	        relativeSwaggerPathProvider.setApiResourcePrefix("rest");//"api");
	        return new SwaggerSpringMvcPlugin(springSwaggerConfig)
	                .pathProvider(relativeSwaggerPathProvider);
	    }

	    private ApiInfo apiInfo(String aInGroup) {
	        ApiInfo apiInfo = new ApiInfo(
	                API_TITLE +" - " + aInGroup,
	                API_DESCRIPTION,
	                API_TERMS_OF_SERVICE_URL,
	                API_CONTACT,
	                API_LICENSE,
	                API_LICENSE_URL
	        );
	        return apiInfo;
	    }


	    @Override
	    public void setServletContext(ServletContext aInServletContext)
	    {
	        this.servletContext = aInServletContext;
	    }

}

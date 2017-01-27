package com.obrienlabs.gps.business;

import java.util.List;

import javax.inject.Inject;
//import javax.ejb.EJB;
////import javax.inject.Inject;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.Consumes;
//import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;

//import weblogic.rmi.extensions.PortableRemoteObject;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.obrienlabs.gps.business.entity.Record;
import com.obrienlabs.gps.business.service.ApplicationServiceLocal;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiResponses;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiOperation;





// Specifies the path to the RESTful service
@Path("/health")
////@Produces("text/plain")
//@Consumes("application/json")
//@RestController
//@Service("daoFacade")
//@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Api(value="health", produces="application/html", description="Health check")
@RequestMapping("/health")
@Component
public class RestHealthServiceImpl extends Application {
	// https://jersey.java.net/documentation/latest/spring.html
	// http://stackoverflow.com/questions/3027834/inject-a-ejb-into-jax-rs-restfull-service
	// not supported until 2.0 https://java.net/jira/browse/JERSEY-517
	//@EJB(name="ejb/ApplicationService") // not for JAX-RS
	// 20160606: spring injection requires jersey-spring3 from org.glassfish.jersey.ext with spring 4 exclusions in the pom
	@Inject
	//@Autowired
	@Qualifier("daoFacade")
    private ApplicationServiceLocal applicationServiceLocal;// = new ApplicationService();	
	
	
	@GET
	@Path("/health")
	@Produces(MediaType.TEXT_HTML)
	//@RequestMapping("/test")
	@ApiOperation(value="health check", notes="health check for auto scaling")
	@ApiResponses (value= {
			@ApiResponse(code=200, message="OK - success"),
			@ApiResponse(code=400, message="Bad Request"),
			@ApiResponse(code=401, message="Unauthorized"),
			@ApiResponse(code=403, message="Forbidden"),
			@ApiResponse(code=404, message="NotFound"),
			@ApiResponse(code=409, message="Conflict"),
			@ApiResponse(code=500, message="Internal Server Error")
	})
	public String getHealth() {
		return applicationServiceLocal.health().toString();
	}

	
}


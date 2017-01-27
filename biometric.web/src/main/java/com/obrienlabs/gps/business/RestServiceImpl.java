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
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;


// Specifies the path to the RESTful service
@Path("/read")
////@Produces("text/plain")
//@Consumes("application/json")
//@RestController
//@Service("daoFacade")
//@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@RequestMapping("/read")
@Api(value="read", produces="application/html", description="read queries")
@Component
public class RestServiceImpl extends Application {
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
	@Path("/test")
	@Produces(MediaType.TEXT_HTML)
	//@RequestMapping("/test")
	@ApiOperation(value="read test", notes="read test")
	@ApiResponses (value= {
			@ApiResponse(code=200, message="OK - success"),
			@ApiResponse(code=400, message="Bad Request"),
			@ApiResponse(code=401, message="Unauthorized"),
			@ApiResponse(code=403, message="Forbidden"),
			@ApiResponse(code=404, message="NotFound"),
			@ApiResponse(code=409, message="Conflict"),
			@ApiResponse(code=500, message="Internal Server Error")
	})
	public String getTest() {
		return "testing: " + applicationServiceLocal;
	}

	@GET
	@Path("/geohash/{lat}/{lon}")
	@Produces(MediaType.TEXT_PLAIN)
	public String getGeoHash(@PathParam("lat")String lat, @PathParam("lon")String lon)  {		
		return getApplicationService().geohash(Double.parseDouble(lat), Double.parseDouble(lon));
	}
	
	@GET
	@Path("/geohashcount/{prefix}")
	@Produces(MediaType.TEXT_PLAIN)
	public String getGeoHashCount(@PathParam("prefix")String prefix) {
		return getApplicationService().geoHashCountFor(prefix).toString();
	}
	
	@GET
	@Path("/csv/records/userid")
	@Produces(MediaType.TEXT_PLAIN)
	public String getUserIds() {
		StringBuilder builder = new StringBuilder();
		List<Long> records = getApplicationService().getUserIds();
		for (Long r : records) {
			builder.append(r);
			builder.append(",");
			// builder.append(r.getVersion());
			builder.append("\n");
		}
		return builder.toString();
	}
		
	// /rest/read/ident/201312060
   @GET
   @Path("/csv/record/{identifier}")  
   //@Produces(MediaType.TEXT_PLAIN)
   @Produces(MediaType.APPLICATION_XML)
   public String getRecordCSV(@PathParam("identifier")String identifier) {	   
      return "<state>" + Integer.toString(read(identifier).size()) + "</state>";
   }

   @GET
   @Path("/csv/records/{identifier}")  
   @Produces(MediaType.TEXT_PLAIN)
   public String getRecordsCSV(@PathParam("identifier")String identifier) {
	   StringBuilder builder = new StringBuilder();
	   List<Record> records = read(identifier);
	   for(Record r : records) {
		   builder.append(r.getId());
		   builder.append(",");
		   builder.append(r.getVersion());
		   builder.append("\n");
	   }
      return builder.toString();
   }

   @GET
   @Path("/xml/records/{identifier}")  
   @Produces(MediaType.APPLICATION_XML)
   public List<Record> getRecordsXML(@PathParam("identifier")String identifier) {	   
	   return read(identifier);
   }

   // 20160606 working with jackson
   @GET
   @Path("/json/records/{id}")  
   //@Consumes(MediaType.TEXT_PLAIN)
   @Produces(MediaType.APPLICATION_JSON)
   public List<Record> getRecordsJSON(@PathParam("id")String identifier) {	   
	   return read(identifier);
   }
   
   // working with jackson
   @GET
   @Path("/json/latest/{id}")  
   //@Consumes(MediaType.TEXT_PLAIN)
   @Produces(MediaType.APPLICATION_JSON)
   public Record getLatestJSON(@PathParam("id")String identifier) {	 //  requires XmlRootElement on Record  
	   return getApplicationService().latest(identifier);
   }

   
   @GET
   @Path("/xml/latest/{id}")  
   //@Consumes(MediaType.TEXT_PLAIN)
   @Produces(MediaType.APPLICATION_XML)
   public Record getLatestXML(@PathParam("id")String identifier) {	   
	   return getApplicationService().latest(identifier);
   }
   /*@GET
   @Path("/records/{identifier}")  
   //@Consumes(MediaType.TEXT_PLAIN)
   @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
   public List<Record> getRecordsJSON_or_XML(@PathParam("identifier")String identifier) {	   
	   return read(identifier);
   }*/
   // TODO: not working
   @GET
   @Path("/html/records/{identifier}")  
   @Produces(MediaType.TEXT_HTML)
   public List<Record> getRecordsHTML(@PathParam("identifier")String identifier) {	   
	   return read(identifier);
   }
   //ok
   @GET
   @Path("/xml/record/{identifier}")  
   @Produces(MediaType.APPLICATION_XML)
   public String getRecordXML(@PathParam("identifier")String identifier) {	   
      return Integer.toString(read(identifier).size());
   }

   @GET
   @Path("/json/record/{identifier}")  
   @Produces(MediaType.APPLICATION_JSON)
   public String getRecordJSON(@PathParam("identifier")String identifier) {	   
      return Integer.toString(read(identifier).size());
   }
   
   //@PUT//
   //@Pa
   // working
   private List<Record> read(String user) {
	   return getApplicationService().read(user);
   }

   private ApplicationServiceLocal getApplicationService() {
	   /*if(null == applicationServiceLocal) {
		   ApplicationContext applicationContext = null;
		   try {
			   applicationContext = new ClassPathXmlApplicationContext(
				   "classpath:spring_old.xml");
		   applicationServiceLocal = (ApplicationServiceLocal) applicationContext.getBean("daoFacade");
		   } catch (Exception e) {
			   e.printStackTrace();
			   
		   }
		   
	   }*/
	   // JNDI lookup as workaround until JAX-RS 2.0 implements CDI
	   /*if(null == applicationServiceLocal) {
		   try {
			Object object = new InitialContext()
				.lookup("java:global/com.obrienlabs.gps.LoggerEAR/com.obrienlabs.gps.LoggerEJB/ApplicationService");
			//applicationServiceLocal = (ApplicationServiceLocal)PortableRemoteObject
			//	.narrow(object, ApplicationServiceLocal.class);
		   } catch (NamingException ne) {
			   ne.printStackTrace();
		   }
	   }*/
	   return applicationServiceLocal;
   }
}


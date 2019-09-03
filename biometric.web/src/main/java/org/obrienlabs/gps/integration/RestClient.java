package org.obrienlabs.gps.integration;

import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.web.client.RestTemplate;

import org.obrienlabs.gps.business.entity.Record;

public class RestClient {
	
    private static final String URL_CREATE_RECORD =
            "http://biometric.elasticbeanstalk.com/FrontController?action=latest&u=201505165";
	public void post() {
        GenericXmlApplicationContext ctx = new GenericXmlApplicationContext();
        ctx.load("classpath:META-INF/spring-rest-client.xml");
        ctx.refresh();
        
        //Record record = null;
        RestTemplate restTemplate = ctx.getBean("restTemplate", RestTemplate.class);
        long time = System.currentTimeMillis();
        Record record = restTemplate.getForObject(URL_CREATE_RECORD, Record.class);
        long end = System.currentTimeMillis() - time;
        System.out.println(end + "ms Record: " + record.getSendSeq());
        ctx.close();

	}

	public static void main(String[] args) {

		RestClient client = new RestClient();
		client.post();

	}

}

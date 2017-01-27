package com.obrienlabs.gps.integration;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.SslConfigurator;

import com.obrienlabs.gps.business.entity.Record;
import com.obrienlabs.gps.business.entity.User;

public class JAXRSClient {

    final static char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
        '9', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k', 'm', 'n', 'p',
        'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };
    /*final static int[] geo_x = { 0, 1, 2, 3, 4, 5, 6, 7, 8,
        9, b, c, d, e, 'f', 'g', 'h', 'j', 'k', 'm', 'n', 'p',
        'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };*/
  
    //bcfguvyz
    //89destwx
    //2367kmqr
    //0145hjnp
    
	public void run() {
		//Long id = 1L;
		// import cert with
		// sudo keytool -import -alias nutridat_server -file /Users/michaelobrien/Dropbox/Nutridat/nutridat_domain_cert/20150119_nutridat_server_cer.cer -keystore /Library/Java/JavaVirtualMachines/jdk1.8.0_65.jdk/Contents/Home/jre/lib/security/tomcat8
		SslConfigurator sslConfig = SslConfigurator.newInstance()
		        .trustStoreFile("/Library/Java/JavaVirtualMachines/jdk1.8.0_65.jdk/Contents/Home/jre/lib/security/tomcat8")
		        .trustStorePassword("changeit")
		        .keyStoreFile("/Library/Java/JavaVirtualMachines/jdk1.8.0_65.jdk/Contents/Home/jre/lib/security/tomcat8")
		        .keyPassword("changeit");
		SSLContext sslContext = sslConfig.createSSLContext();
		// fix java.security.cert.CertificateException: No subject alternative names present
		HostnameVerifier verifier = new HostnameVerifier() {
		    public boolean verify(String hostname, SSLSession sslSession) {
		        return true; // TODO: security breach 
		    }};
		
		Client client = ClientBuilder.newBuilder().sslContext(sslContext).hostnameVerifier(verifier).build();
		//Client client = ClientBuilder.newClient();
		//WebTarget rootTarget = client.target("http://138.120.149.110:8080/biometric/rest/rea");
		//WebTarget rootTarget = client.target("http://127.0.0.1:8080/biometric/rest/read");
		WebTarget rootTarget = client.target("https://obrienlabs.elasticbeanstalk.com:443/rest/read");
		//WebTarget rootTarget = client.target("http://192.168.0.55:8080/biometric/rest/read");
		//WebTarget latestTarget = rootTarget.path("json/latest");
		WebTarget latestTarget = rootTarget.path("geohashcount");//json/record");
		
		try {
			WebTarget finalTarget = null;
			//Record record = null;
			String prefix = "f241b";//rnekx3";// t // 11 * 5 = 55 bit
			String record = null;
			//for(long id=1; id<201607010; id++) {
			for(int id=0;id<digits.length;id++) {
				try { Thread.sleep(1); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); }
				finalTarget = latestTarget.path(prefix + String.valueOf(digits[id]));
				long before = System.currentTimeMillis();
				record = finalTarget.request().get(String.class);//Long.class);//Record.class);
				long after = System.currentTimeMillis();
				System.out.println(new StringBuffer(prefix + String.valueOf(digits[id])).append(":").append(record).append(" : ").append(String.valueOf(after - before)).toString());
			}
		} catch (Exception e)  {
			e.printStackTrace();
		} finally {
			client.close();
		}
	}
	
	
	public static void main(String[] args) {
		JAXRSClient client = new JAXRSClient();
		client.run();
	}

}

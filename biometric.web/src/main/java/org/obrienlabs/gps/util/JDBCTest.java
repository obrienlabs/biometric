package org.obrienlabs.gps.util;

import java.sql.Connection;
import java.sql.DriverManager;

// http://wiki.obrienlabs.cloud/display/DEV/Databases#Databases-LocalSSHTunnelinOSX

public class JDBCTest {
	public String testConnection(String username, String password, String useSSL) {
		Connection connection;
		String ret = null;
		//String url = "jdbc:mysql://host.docker.internal:3406/biometric?useSSL=";
		//String url = "jdbc:mysql://host.docker.internal:3406/biometric?useSSL=";
		String url = "jdbc:mysql://127.0.0.1:3406/biometric?useSSL=";
		try {
		      Class.forName("com.mysql.jdbc.Driver");
		      System.out.println("testing: " + username);
		            connection = DriverManager.getConnection(url + useSSL, username, password);
		            ret = connection.getMetaData().getUserName();  
		            System.out.println(ret);
		} catch ( Exception e ){ e.printStackTrace(); }	
		return ret;
	}

	public static void main(String[] args) {
		JDBCTest test = new JDBCTest();
		// useSSL=false to avoid Mon Sep 23 20:47:29 EDT 2019 WARN: Establishing SSL connection without server's identity verification is not recommended. According to MySQL 5.5.45+, 5.6.26+ and 5.7.6+ requirements SSL connection must be established by default if explicit option isn't set. For compliance with existing applications not using SSL the verifyServerCertificate property is set to 'false'. You need either to explicitly disable SSL by setting useSSL=false, or set useSSL=true and provide truststore for server certificate verification.
		test.testConnection("o*s", "O*", "true");	
	}
}

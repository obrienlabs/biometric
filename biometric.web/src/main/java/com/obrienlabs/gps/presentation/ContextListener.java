package com.obrienlabs.gps.presentation;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;

//@WebListener
public class ContextListener {//extends ServletContextListener {

	public ContextListener(ServletContext source, String name, Object value) {
		//super(source, name, value);
		// TODO Auto-generated constructor stub
	}

	private static final long serialVersionUID = 6042193078108636817L;


    /*@Override
    public void contextInitialized(ServletContextEvent event) {
        System.out.println("The application started");
    }
     
    @Override
    public void contextDestroyed(ServletContextEvent event) {
        System.out.println("The application stopped");
    }*/
}

/*******************************************************************************
 * Contributors:
 *     16/02/2011 2.3  Michael O'Brien 
 *          - Initial API and implementation platform to be used for 
 *             distributed EE application research, development and architecture
 ******************************************************************************/ 
  
package com.obrienlabs.gps.business;

import java.util.ArrayList;
import java.util.List;

//import javax.annotation.PostConstruct;
//import javax.ejb.EJB;
//import javax.faces.bean.ManagedBean;
//import javax.faces.bean.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
//import javax.ejb.EJB;
//import javax.faces.bean.RequestScoped;
//import javax.faces.bean.ViewScoped;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transaction;



/**
 * This class is part of a distributed application framework used to simulate and research
 * concurrency, analytics, management, performance and exception handling.
 * The focus is on utilizing JPA 2.0 as the persistence layer for scenarios involving
 * multicore, multithreaded and multiuser distributed memory L1 persistence applications.
 * The secondary focus is on exercising Java EE6 API to access the results of this distributed application.
 * 
 * @see http://obrienscience.blogspot.com
 * @author Michael O'Brien
 */

//@ManagedBean(name="monitorManagedBean", eager=true) 
////@SessionScoped
//@RequestScoped
public class MonitorManagedBean {
	String gps;
	String status;
	
	String query;
	Boolean isConnected = true;
	
	

	public Boolean getIsConnected() {
		return isConnected;
	}

	public void setIsConnected(Boolean isConnected) {
		this.isConnected = isConnected;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getGps() {
		return gps;
	}

	public void setGps(String gps) {
		this.gps = gps;
	}
	
}

package com.obrienlabs.gps.integration;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;

import com.obrienlabs.gps.business.entity.Device;
import com.obrienlabs.gps.business.entity.SensorTransient;
import com.obrienlabs.gps.business.entity.Session;
import com.obrienlabs.gps.business.entity.Reading;
import com.obrienlabs.gps.business.entity.Sensor;
import com.obrienlabs.gps.business.entity.Record;
import com.obrienlabs.gps.business.entity.User;


public class Populate {
	// Application managed EMF and EM
	private EntityManagerFactory emf = null;
	private EntityManager entityManager = null;
	private Session session = null;
	// Reference the database specific persistence unit in persistence.xml
	public static final String PU_NAME_CREATE = "copy-to";
	private long sentSeq = 1;
	private long recvSeq = 1;

	/**
	 * Create the EMF and EM and start a transaction (out of container context)
	 * 
	 * @param puName
	 */
	private void initialize(String puName) {
		try {
			// Initialize an application managed JPA emf and em via META-INF
			emf = Persistence.createEntityManagerFactory(puName);
			entityManager = emf.createEntityManager();
			if (null == entityManager) {
				throw new IllegalArgumentException("EM is null - check DB is running");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Close the application managed EM and EMF
	 */
	public void finalize() {
		// close JPA
		try {
			if (null != getEntityManager()) {
				getEntityManager().close();
				// getEmf().close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private EntityTransaction getTransaction() {
		EntityTransaction transaction = getEntityManager().getTransaction();
		if (null == transaction) {
			throw new IllegalArgumentException(
					"transaction is null - check DB is running");
		}
		return transaction;
	}

	private void queryCriteriaTestPM() {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Record> query = cb
				.createQuery(Record.class);
		Root<Record> target = query.from(Record.class);
		query.where(cb.equal(target.get("id"), "1"));
		/*
		 * cb.and( cb.equal(descriptor.get("name"), "name1"),
		 * cb.equal(descriptor.get("entityPkid"), -1L),
		 * cb.equal(descriptor.get("descriptorType"),
		 * DescriptorType.DOMAIN.toString())));
		 */
		List<Record> result = getEntityManager().createQuery(query)
				.getResultList();
		System.out.println(result);
	}

	
    public Record latest(String user) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
	    CriteriaQuery<Record> query = cb.createQuery(Record.class);
	    Root<Record> target = query.from(Record.class);
	    // workaround for http://stackoverflow.com/questions/16348354/how-do-i-write-a-max-query-with-a-where-clause-in-jpa-2-0
	    SingularAttribute<? super Record, Long> anAttribute = entityManager.getMetamodel()
	    		.entity(Record.class).getSingularAttribute("tsStop", Long.class);
	    query.orderBy(
	    		cb.desc(target.get(anAttribute)));
	    //query.where(
	    	//	cb.equal(target.get("userId"), user));
	    TypedQuery<Record> typedQuery = entityManager.createQuery(query);
	    typedQuery.setMaxResults(1);
	    // see http://bugs.eclipse.org/303205
	    Record result = (Record)typedQuery.getSingleResult();
	    //List<Record> list = new ArrayList<>();
	    //list.add(result);
	    System.out.println("latest: " + user + ": " + result);
		return result;//.get(0);   	
    }
    
	private Map<String, Sensor> createMetadataSensors(EntityManager em) {
		// create standard types
		Map<String, Sensor> types = new HashMap<>();
		
		// create dynamic types
		Sensor hrSensor = new SensorTransient();
		hrSensor.setLabel("HEART_RATE");//Sensor.SENSORS_BUILTIN.HEART_RATE.toString());
		hrSensor.setType(Sensor.NUMERIC_TYPES.INT.toString());
		types.put(hrSensor.getLabel(), hrSensor);
		// no need to set reverse relationship to reading
		for(Sensor type : types.values()) {
			em.persist(type);
		}
		return types;
	}
	
	private Map<String, Sensor> createMetadata(EntityManager em) {
		int counter = 0;
		
		// register users
		User user = new User();
		user.setFirstAccess(System.currentTimeMillis());
		user.setName("michael");
		
		
		
		// register new types // associate with user
		Map<String, Sensor> readingTypeMap = createMetadataSensors(em);
		// create a user session
		session = new Session();
			
		// create readings for user session
		//Map<Sensor, Reading> readings = new HashMap<>();
		//session.setReadingsByType(readings);
		Reading hrReading = new Reading();
		hrReading.setSensor(readingTypeMap.get("HEART_RATE"));
		hrReading.setValue((new Integer(60 + counter++)).toString());
		hrReading.setTimestampRec(System.currentTimeMillis());
		hrReading.setSequenceNumberRec(recvSeq++);
		hrReading.setSequenceNumberSent(sentSeq++);
		session.addReading(hrReading);
		hrReading.setSession(session);
		
		// set bidirectional mappings
		session.setUser(user);
		user.addSession(session);
		
		Device device = new Device();
		device.setName("iphone5s");
		device.setIp("127.0.0.1");
		device.setDeviceId(1L);
		device.setUser(user);
		user.addDevice(device);

		// save
		for(Reading reading : session.getReadings()) {
			getEntityManager().persist(reading);
				
		
		
		
		}
		getEntityManager().persist(user);
		getEntityManager().persist(session);
		//}
	
		return readingTypeMap;
	}
	
	// registration
	
	private void processInsertPM() {
		// Insert schema and classes into the database
		List<Record> records = new ArrayList<>();
		Map<String, Sensor> readingTypeMap = null;
		try {
			// Cache objects
			getTransaction().begin();
			for(int i=0;i<8;i++) {
				Record r = new Record();
				r.setUserId(1L);
				records.add(r);
				getEntityManager().persist(r);
			}
			
			readingTypeMap = createMetadata(getEntityManager());
			// Store objects
			getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// add one more item in the list to see if the owner gets updated 
		// http://openjpa.208410.n2.nabble.com/Unecessary-database-update-td7586121.html
		try {
			getTransaction().begin();
			
			// user
			User userIphone = new User();
			User userIpod = new User();
			userIphone.setFirstAccess(System.currentTimeMillis());
			userIpod.setFirstAccess(System.currentTimeMillis());
			userIpod.setName("Ipod");
			userIphone.setName("Iphone");
			
			Session iphoneSession = new Session();
			Session ipodSession = new Session();
			iphoneSession.setUser(userIphone);
			ipodSession.setUser(userIphone);
			List<Reading> readings = new ArrayList<>();
			ipodSession.setReadings(readings);
			
			Sensor hrSensor = new SensorTransient();
			hrSensor.setLabel("Mio Alpha");
			hrSensor.setSerial("101");
			hrSensor.setType("HRM");//Sensor.SENSORS_BUILTIN);
			hrSensor.setUnit(Sensor.NUMERIC_TYPES.INT.toString());
			
			Reading hrReading = new Reading();
			readings.add(hrReading);
			hrReading.setSensor(readingTypeMap.get("HEART_RATE"));
			hrReading.setValue((new Integer(61)).toString());
			hrReading.setTimestampRec(System.currentTimeMillis());
			hrReading.setSequenceNumberRec(recvSeq++);
			hrReading.setSequenceNumberSent(sentSeq++);

			hrReading.setSession(ipodSession);
			//session1.getReadings().add(hrReading);
			//session.getReadings().add(hrReading);

			getEntityManager().persist(hrSensor);
			
			getEntityManager().persist(hrSensor);
			getEntityManager().persist(hrReading);

			getEntityManager().persist(userIphone);
			getEntityManager().persist(userIpod);
			getEntityManager().persist(iphoneSession);
			getEntityManager().persist(ipodSession);
			getTransaction().commit();
		} catch (Exception e ) {
			e.printStackTrace();
		}
	}

	public EntityManagerFactory getEmf() {
		return emf;
	}

	public void setEmf(EntityManagerFactory emf) {
		this.emf = emf;
	}

	public EntityManager getEntityManager() {
		if (null == entityManager) {
			throw new IllegalArgumentException(
					"EM is null - check DB is running");
		}
		return entityManager;
	}

	public void setEntifyManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public static void doQuery() {
		Populate aClient = new Populate();
		aClient.initialize(PU_NAME_CREATE);
		// aClient.processInsert();
		// aClient.processInsertMT();
		aClient.processInsertPM();
		// aClient.queryJPQLTest();
		// aClient.queryCriteriaTest();
		// aClient.queryCriteriaTestMT();
		//aClient.queryCriteriaTestPM();
		aClient.latest("201403");
		aClient.finalize();
		System.exit(0);
	}

	public static void testInitialContext() {
		        Hashtable hashtable = new Hashtable();
		        hashtable.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
		        hashtable.put(Context.PROVIDER_URL, "t3://localhost:17001");
		        hashtable.put(Context.SECURITY_PRINCIPAL, "weblogic");
		        hashtable.put(Context.SECURITY_CREDENTIALS, "ciralogic3");

		        try {
		            Context context = new InitialContext(hashtable);
		            System.out.println(context);
		            System.out.println(context.lookup("javax.jms.QueueConnectionFactory"));
		        } catch (NamingException e) {
		            e.printStackTrace();
		        }

	}
	
	public static void main(String[] args) {
		doQuery();
		//testInitialContext();

	}

}


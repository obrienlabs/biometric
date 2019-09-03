package org.obrienlabs.gps.integration;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;

import org.obrienlabs.gps.business.entity.Device;
import org.obrienlabs.gps.business.entity.SensorTransient;
import org.obrienlabs.gps.business.entity.Session;
import org.obrienlabs.gps.business.entity.Reading;
import org.obrienlabs.gps.business.entity.Sensor;
import org.obrienlabs.gps.business.entity.Record;
import org.obrienlabs.gps.business.entity.User;


public class Reconfigure {
	// Application managed EMF and EM
	private EntityManagerFactory emf1 = null;
	private EntityManager entityManager1 = null;
	private Session session1 = null;
	private EntityManagerFactory emf2 = null;
	private EntityManager entityManager2 = null;
	private Session session2 = null;
	// Reference the database specific persistence unit in persistence.xml - biometric.root
	public static final String PU_NAME_CREATE_1 = "from";
	public static final String PU_NAME_CREATE_2 = "copy_to";//"to";
	private long sentSeq = 1;
	private long recvSeq = 1;
	private User aUser;


	private void persistORM(Record aRecord) {
		// create object tree
		//User aUser = new User();
		//aUser.setId(2);
		
	}
	
	private void reconfigure() {

		
		
		// read in batches
		BigInteger countFrom = null;
		BigInteger countTo = null;
		try {
			Query query = entityManager1.createNativeQuery("select count(1) from gps_record");// + " order by r.tsStop");
			countFrom = (BigInteger)query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		System.out.println("records 1: " + countFrom.longValue());

		List<Long> currentPKList = new ArrayList<>();
				
		// get ids of records
		List<BigInteger> pks = null;
		try {
			Query query = entityManager1.createNativeQuery("select IDENT_ID from gps_record r order by r.IDENT_ID desc");// + " order by r.tsStop");
			pks = (List<BigInteger>)query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		
		Set<Record> recordsToFlush = new HashSet<>();
		
		Record aRecord = null;
		boolean uncommittedChanges = true; // force transaction begin
		EntityTransaction transactionTo = entityManager2.getTransaction();
		transactionTo.begin();
		
		User aUser = new User();
		aUser.setId(1L);
		aUser.setName("michael");
		entityManager2.persist(aUser);
		
		Device aDevice = new Device();
		aDevice.setIp("127.0.0.1");
		aDevice.setUser(aUser);
		aUser.addDevice(aDevice);
		entityManager2.persist(aDevice);
		
		int partition = 0;
		for(BigInteger id : pks) {
			try {
				// commits occur on reads
				Query query = entityManager1.createQuery("select object(r) from Record r where r.id=" + id.longValue());// + " order by r.tsStop");
				aRecord = (Record)query.getSingleResult();
				if(null != aRecord) {
					entityManager1.detach(aRecord);
					// clear record from both sides
					currentPKList.add(aRecord.getId());

					// clear PK when moving between databases to avoid unknown behavior
					//aRecord.setId(null);
					
					//entityManager2.merge(aRecord);
					persistORM(aRecord);
					recordsToFlush.add(aRecord);
					partition++;
				}
				if(partition > 39) {
					System.out.println("Committing: " + partition);
					entityManager2.flush();
					transactionTo.commit();
					partition = 0;
					uncommittedChanges = false;
					
					for(Long pk : currentPKList) {
						emf1.getCache().evict(Record.class, pk);
					}
					for(Record recordToFlush : recordsToFlush) {
						emf2.getCache().evict(Record.class, recordToFlush.getId());
						// use case 2  - reduced heap
						entityManager2.detach(recordToFlush);
						recordToFlush.setId(null);
					}
					recordsToFlush = new HashSet<>();
				} else {
					if(!uncommittedChanges) {
						transactionTo.begin();
					}
					uncommittedChanges = true;
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
			}
			
		}
		if(uncommittedChanges) {
			entityManager2.flush();
			transactionTo.commit();
		}
		// partition
		// save
		try {
			Query query = entityManager2.createNativeQuery("select count(1) from gps_record");																			// " order by r.tsStop");
			countTo = (BigInteger) query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		System.out.println("records 2: " + countTo.longValue());

	}
	

	/**
	 * Create the EMF and EM and start a transaction (out of container context)
	 * 
	 * @param puName
	 */
	private void initialize() {
		try {
			// Initialize an application managed JPA emf and em via META-INF
			emf1 = Persistence.createEntityManagerFactory(PU_NAME_CREATE_1);
			emf2 = Persistence.createEntityManagerFactory(PU_NAME_CREATE_2);
			entityManager1 = emf1.createEntityManager();
			if (null == entityManager1) {
				throw new IllegalArgumentException("EM1 is null - check DB is running");
			}
			entityManager2 = emf2.createEntityManager();
			if (null == entityManager2) {
				throw new IllegalArgumentException("EM2 is null - check DB is running");
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
			if (null != getEntityManager1()) {
				getEntityManager1().close();
				// getEmf().close();
			}
			if (null != getEntityManager2()) {
				getEntityManager2().close();
				// getEmf().close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private EntityTransaction getTransaction(EntityManager em) {
		EntityTransaction transaction = em.getTransaction();
		if (null == transaction) {
			throw new IllegalArgumentException(
					"transaction is null - check DB is running");
		}
		return transaction;
	}

	public EntityManagerFactory getEmf1() {
		return emf1;
	}

	public void setEmf1(EntityManagerFactory emf) {
		this.emf1 = emf;
	}

	public EntityManagerFactory getEmf2() {
		return emf2;
	}

	public void setEmf2(EntityManagerFactory emf) {
		this.emf2 = emf;
	}
	
	public EntityManager getEntityManager1() {
		if (null == entityManager1) {
			throw new IllegalArgumentException(
					"EM1 is null - check DB is running");
		}
		return entityManager1;
	}

	public void setEntifyManager1(EntityManager entityManager) {
		this.entityManager1 = entityManager;
	}

	public EntityManager getEntityManager2() {
		if (null == entityManager2) {
			throw new IllegalArgumentException(
					"EM2 is null - check DB is running");
		}
		return entityManager2;
	}

	public void setEntifyManager2(EntityManager entityManager) {
		this.entityManager2 = entityManager;
	}
	public static void doQuery() {
		Reconfigure aClient = new Reconfigure();
		aClient.initialize();
		aClient.reconfigure();		
		// aClient.processInsert();
		// aClient.processInsertMT();
//		aClient.processInsertPM();
		// aClient.queryJPQLTest();
		// aClient.queryCriteriaTest();
		// aClient.queryCriteriaTestMT();
		//aClient.queryCriteriaTestPM();
//		aClient.latest("201403");
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

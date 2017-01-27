package com.obrienlabs.gps.integration;

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
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;

import com.obrienlabs.gps.business.integration.GeoHash;
import com.obrienlabs.gps.business.entity.SensorTransient;
import com.obrienlabs.gps.business.entity.Session;
import com.obrienlabs.gps.business.entity.Reading;
import com.obrienlabs.gps.business.entity.Sensor;
import com.obrienlabs.gps.business.entity.Record;
import com.obrienlabs.gps.business.entity.User;


public class Transfer {
	// Application managed EMF and EM
	private EntityManagerFactory emf1 = null;
	private EntityManager entityManager1 = null;
	private Session session1 = null;
	private EntityManagerFactory emf2 = null;
	private EntityManager entityManager2 = null;
	private Session session2 = null;
	// Reference the database specific persistence unit in persistence.xml
	public static final String PU_NAME_CREATE_1 = "copy-from";
	public static final String PU_NAME_CREATE_2 = "copy-to";
	private long sentSeq = 1;
	private long recvSeq = 1;
	
	public static User michaelUser = new User();
	private final Map<Class<?>, Object> objects = new HashMap<>();

	
	/*private void copy1_brute_force() {
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

				
		// get ids of records
		List<BigInteger> pks = null;
		try {
			Query query = entityManager1.createNativeQuery("select IDENT_ID from gps_record r order by r.IDENT_ID desc");// + " order by r.tsStop");
			pks = (List<BigInteger>)query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		
		Record aRecord = null;
		boolean uncommittedChanges = true; // force transaction begin
		EntityTransaction transactionTo = entityManager2.getTransaction();
		transactionTo.begin();
		// do an initial query
		//try {
		//	Query query = entityManager2.createQuery("select object(r) from Record r where r.id=1" );																			// " order by r.tsStop");
		//	query.getSingleResult();
		//} catch (Exception e) {
		//	e.printStackTrace();
		//} finally {
		//}
		
		int partition = 0;
		for(BigInteger id : pks) {
			try {
				// commits occur on reads
				Query query = entityManager1.createQuery("select object(r) from Record r where r.id=" + id.longValue());// + " order by r.tsStop");
				aRecord = (Record)query.getSingleResult();
				if(null != aRecord) {
					//entityManager2.flush();
					entityManager1.detach(aRecord);
					// clear PK when moving between databases
					aRecord.setId(null);
					entityManager2.merge(aRecord);
					//entityManager2.persist(aRecord);
					partition++;
				}
				if(partition > 39) {
					System.out.println("Committing: " + partition);
					entityManager2.flush();
					transactionTo.commit();
					partition = 0;
					uncommittedChanges = false;
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
	}*/
	/*
	private void copy2() {
		// read in batches
		BigInteger countFrom = null;
		Long countTo = null;
		try {
			Query query = entityManager1.createNativeQuery("select count(1) from gps_record");// + " order by r.tsStop");
			//countFrom = (BigInteger)query.getSingleResult(); // hibernate
			countFrom = BigInteger.valueOf(((Long)query.getSingleResult()).longValue()); // eclipselink
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		System.out.println("records 1: " + countFrom.longValue());

		List<Long> currentPKList = new ArrayList<>();
				
		// get ids of records
		List<Long> pks = null;
		try {
			Query query = entityManager1.createNativeQuery("select IDENT_ID from gps_record r order by r.IDENT_ID desc");// + " order by r.tsStop");
			pks = (List<Long>)query.getResultList();
			//pks = BigInteger.valueOf(((Long)query.getSingleResult()).longValue()); // eclipselink
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		
		Set<Record> recordsToFlush = new HashSet<>();
		
		Record aRecord = null;
		Record aRecordTo = null;
		boolean uncommittedChanges = true; // force transaction begin
		EntityTransaction transactionTo = entityManager2.getTransaction();
		transactionTo.begin();
		
		int partition = 0;
		int skipCounter = 0;
		for(Long id : pks) {
			try {
				
				// commits occur on reads
				Query query = entityManager1.createQuery("select object(r) from Record r where r.id=" + id.longValue());// + " order by r.tsStop");
				aRecord = (Record)query.getSingleResult();
				if(null != aRecord) {

				// check destination first
					boolean found = false;
				try {
				Query queryTo = entityManager2.createQuery("select object(r) from Record r where r.tsStop=" + aRecord.getTsStop() + " and r.tsStart=" + aRecord.getTsStart());// + " order by r.tsStop");
				aRecordTo = (Record)queryTo.getSingleResult();
				found = true;
				} catch (NoResultException  nre ) {
				aRecordTo = null;
				found  = false;
				} catch (NonUniqueResultException nure) {
					aRecordTo = null;
					found = true;
				} finally {
					System.out.print("s");
					if(skipCounter < 100) {
						skipCounter++;
					} else {
						skipCounter = 0;
						System.out.println(" : " + id);
					}

				}
				if(!found) {

					//entityManager2.flush();
					entityManager1.detach(aRecord);
					// clear record from both sides
					currentPKList.add(aRecord.getId());
					//emf1.getCache().evict(Record.class, aRecord.getId());

					// clear PK when moving between databases to avoid unknown behavior
					aRecord.setId(null);
					
					//entityManager2.merge(aRecord);
					entityManager2.persist(aRecord);
					recordsToFlush.add(aRecord);
					partition++;
				//}
				if(partition > 39) {
					System.out.println("Committing: " + partition + " for " + id);
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
				//} else {
				}
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
			countTo = (Long) query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		System.out.println("records 2: " + countTo.longValue());

	}*/
	
	private void copy3() {
		// read in batches
		BigInteger countFrom = null;
		BigInteger countTo = null;
		long before = 0;
		long after = 0;
		//String whereClause = " where r.userId like '20160501%' ";
		String whereClause = " where r.userId = 201606095 ";  
		System.out.println(whereClause);
		try {
			Query query = entityManager1.createNativeQuery("select count(1) from gps_record r " + whereClause +  " ");// + " order by r.tsStart");
			countFrom = (BigInteger)query.getSingleResult(); // hibernate
			//countFrom = BigInteger.valueOf(((Long)query.getSingleResult()).longValue()); // eclipselink
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		System.out.println("records 1: " + countFrom.longValue());

		List<Long> currentPKList = new ArrayList<>();
				
		// get ids of records
		List<BigInteger> pks = null;
		try {
			Query query = entityManager1.createNativeQuery("select IDENT_ID from gps_record r "  + whereClause +  " order by r.IDENT_ID asc");// + " order by r.tsStop");
			pks = (List<BigInteger>)query.getResultList();
			//pks = BigInteger.valueOf(((Long)query.getSingleResult()).longValue()); // eclipselink
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		
		// get list of records from target
		/*List<Long> targetpks = null;
		try {
			Query query = entityManager1.createNativeQuery("select IDENT_ID from gps_record r order by r.IDENT_ID desc");// + " order by r.tsStop");
			pks = (List<Long>)query.getResultList();
			//pks = BigInteger.valueOf(((Long)query.getSingleResult()).longValue()); // eclipselink
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}*/
		
		// get diff
		
		
		Set<Record> recordsToFlush = new HashSet<>();
		
		Record aRecord = null;
		Record aRecordTo = null;
		boolean uncommittedChanges = true; // force transaction begin
		EntityTransaction transactionTo = entityManager2.getTransaction();
		transactionTo.begin();
		
		int partition = 0;
		int skipCounter = 0;
		int count = 0;
		for(BigInteger id : pks) {
			try {
				
				// commits occur on reads
				Query query = entityManager1.createQuery("select object(r) from Record r where r.id=" + id.longValue());// + " order by r.tsStop");
				aRecord = (Record)query.getSingleResult();
				if(null != aRecord) {

					// check destination first (id may be used - check tsStop
					boolean found = false;
					/*try {
						//Query queryTo = entityManager2.createQuery("select object(r) from Record r where r.tsStop=" + aRecord.getTsStop() + " and r.tsStart=" + aRecord.getTsStart());// + " order by r.tsStop");
						Query queryTo = entityManager2.createQuery("select object(r) from Record r where r.tsStop=" + aRecord.getTsStop()); //tsStop=" + aRecord.getTsStop() + " and r.tsStart=" + aRecord.getTsStart());// + " order by r.tsStop");
						aRecordTo = (Record)queryTo.getSingleResult();
						found = true;
						System.out.print("1");
					} catch (NoResultException  nre ) {
						System.out.print("n");
						aRecordTo = null;
						found  = false;
					} catch (NonUniqueResultException nure) {
						aRecordTo = null;
						found = true;
						System.out.print("2");
					} finally {
						if(found) {
							if(skipCounter < 100) {
								skipCounter++;
							} else {
								skipCounter = 0;
								System.out.println(" : " + id);
							}
						}
					}*/
					found=false;
					aRecordTo = null;
					if(!found) {
						//entityManager2.flush();
						entityManager1.detach(aRecord);
						// clear record from both sides
						currentPKList.add(aRecord.getId());
						//emf1.getCache().evict(Record.class, aRecord.getId());
						// clear PK when moving between databases to avoid unknown behavior
						//aRecord.setId(null);
						entityManager2.merge(aRecord); // hibernate
						//entityManager2.persist(aRecord); // eclipselink
						recordsToFlush.add(aRecord);
						partition++;
						count++;
						//}
						if(partition > 39) {
							before = after;
							after = System.currentTimeMillis();
							System.out.println("Committing: " + partition + " for " + id + " left: " + (pks.size() - count) + " t:" + ((after-before)/1000));
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
							entityManager2.close();
							entityManager2 = this.getEmf2().createEntityManager();
						} else {
							if(!uncommittedChanges) {
								transactionTo = entityManager2.getTransaction();
								transactionTo.begin();
							}
							uncommittedChanges = true;
						}
					//} else {
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
			}
		}
		if(uncommittedChanges) {
			System.out.println("Last em flush");
			entityManager2.flush();
			transactionTo.commit();
		}
		// partition
		// save
		try {
			Query query = entityManager2.createNativeQuery("select count(1) from gps_record");																			// " order by r.tsStop");
			countTo = (BigInteger) query.getSingleResult();
			System.out.println("records 2: " + countTo.longValue());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}
	
	private void fixGeoHash() {
		// read in batches
		BigInteger countFrom = null;
		Long countTo = null;
		try {
			Query query = entityManager1.createNativeQuery("select count(1) from gps_record r where r.geohash is null ");// + " order by r.tsStop");
			countFrom = (BigInteger)query.getSingleResult(); // hibernate
			//countFrom = BigInteger.valueOf(((Long)query.getSingleResult()).longValue()); // eclipselink
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		System.out.println("records 1: " + countFrom.longValue());

		//List<Long> currentPKList = new ArrayList<>();
				
		// get ids of records
		List<BigInteger> pks = null;
		try {
			Query query = entityManager1.createNativeQuery("select IDENT_ID from gps_record r where r.geohash is null order by r.IDENT_ID asc");// + " order by r.tsStop");
			pks = (List<BigInteger>)query.getResultList();
			//pks = BigInteger.valueOf(((Long)query.getSingleResult()).longValue()); // eclipselink
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		
		//Set<Record> recordsToFlush = new HashSet<>();
		
		Record aRecord = null;
		boolean uncommittedChanges = true; // force transaction begin
		EntityTransaction transactionTo = entityManager1.getTransaction();
		transactionTo.begin();
		GeoHash geoHash = new GeoHash();
		
		int partition = 0;
		long iterations = 0;
		long count = 0;
		for(BigInteger id : pks) {
			try {
				// commits occur on reads
				Query query = entityManager1.createQuery("select object(r) from Record r where r.id=" + id.longValue());// + " order by r.tsStop");
				aRecord = (Record)query.getSingleResult();
				if(null != aRecord) {
					iterations++;
					if((aRecord.getGeohash() == null || aRecord.getGeohash().length() < 1)
							&& ((aRecord.getLattitude() != null && aRecord.getLongitude() != null))) {
						aRecord.setGeohash(geoHash.encode(aRecord.getLattitude().doubleValue(), 
								aRecord.getLongitude().doubleValue()));
						System.out.print(aRecord.getGeohash().substring(aRecord.getGeohash().length()-1));
						entityManager1.persist(aRecord);
						partition++;
						count++;
					} else {
						if(iterations > 39) {
							iterations = 0;
							System.out.println();
						}
						System.out.print(".");
					}
					
					//entityManager2.flush();
					//entityManager1.detach(aRecord);
					// clear record from both sides
					//currentPKList.add(aRecord.getId());
					//emf1.getCache().evict(Record.class, aRecord.getId());

					// clear PK when moving between databases to avoid unknown behavior
					//aRecord.setId(null);
					
					//entityManager2.merge(aRecord);
					//entityManager2.persist(aRecord);
					//recordsToFlush.add(aRecord);
					//partition++;
				}
				if(partition > 39) {
					//System.out.println("Committing: " + partition);
					System.out.println(" left: " + (pks.size() - count));
					entityManager1.flush();
					transactionTo.commit();
					partition = 0;
					uncommittedChanges = false;
					
					/*for(Long pk : currentPKList) {
						emf1.getCache().evict(Record.class, pk);
					}*/
					/*for(Record recordToFlush : recordsToFlush) {
						emf1.getCache().evict(Record.class, recordToFlush.getId());
						// use case 2  - reduced heap
						entityManager1.detach(recordToFlush);
						recordToFlush.setId(null);
					}*/
					//recordsToFlush = new HashSet<>();
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
			entityManager1.flush();
			transactionTo.commit();
		}
		// partition
		// save
		try {
			Query query = entityManager1.createNativeQuery("select count(1) from gps_record");																			// " order by r.tsStop");
			countTo = (Long) query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		System.out.println("records 1: " + countTo.longValue());

	}
	
	private void normalizeRecord(Record aRecord) {
		// extract User
		// hardcode
		
		// extract Device
		String provider = aRecord.getProvider();
		
		// extract sensors
		
		// extract session
		// extract readings
		// extract geohash
		
		
	}
	
	private void normalize() {
		BigInteger countTo = null;
		// read in batches
		BigInteger countFrom = null;
		try {
			Query query = entityManager2.createNativeQuery("select count(1) from gps_record");// + " order by r.tsStop");
			countFrom = (BigInteger)query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		System.out.println("records: " + countFrom.longValue());

		List<Long> currentPKList = new ArrayList<>();
				
		// get ids of records
		List<BigInteger> pks = null;
		try {
			Query query = entityManager2.createNativeQuery("select IDENT_ID from gps_record r order by r.IDENT_ID desc");// + " order by r.tsStop");
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
		
		int partition = 0;
		for(BigInteger id : pks) {
			try {
				// commits occur on reads
				//Query query = entityManager2.createQuery("select object(r) from Record r where r.id=" + id.longValue());// + " order by r.tsStop");
				//aRecord = (Record)query.getSingleResult();
				aRecord = entityManager2.find(Record.class, id);
				if(null != aRecord) {
					//entityManager2.flush();
					//entityManager1.detach(aRecord);
					// clear record from both sides
					currentPKList.add(aRecord.getId());
					//emf1.getCache().evict(Record.class, aRecord.getId());

					// clear PK when moving between databases to avoid unknown behavior
					//aRecord.setId(null);
					
					//recordsToFlush.add(aRecord);
					partition++;
				}
				if(partition > 39) {
					System.out.println("Committing: " + partition);
					entityManager2.flush();
					transactionTo.commit();
					partition = 0;
					uncommittedChanges = false;
					
					//for(Long pk : currentPKList) {
					//	emf1.getCache().evict(Record.class, pk);
					//}
					/*for(Record recordToFlush : recordsToFlush) {
						emf2.getCache().evict(Record.class, recordToFlush.getId());
						// use case 2  - reduced heap
						entityManager2.detach(recordToFlush);
						recordToFlush.setId(null);
					}*/
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

/*	private void queryCriteriaTestPM() {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Record> query = cb
				.createQuery(Record.class);
		Root<Record> target = query.from(Record.class);
		query.where(cb.equal(target.get("id"), "1"));
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
    */
	private Map<String, Sensor> createMetadataSensors(EntityManager em) {
		// create standard types
		Map<String, Sensor> types = new HashMap<>();
		
		// create dynamic types
		Sensor hrSensor = new Sensor();
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
		user.setName("Iphone5s");
		
		
		
		// register new types // associate with user
		Map<String, Sensor> readingTypeMap = createMetadataSensors(em);
		// create a user session
		Session session = new Session();
			
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

		// save
		for(Reading reading : session.getReadings()) {
			em.persist(reading);
		}
		em.persist(user);
		em.persist(session);
		//}
	
		return readingTypeMap;
	}
	
	// registration
	
	private void prePopulate() {
		EntityManager em = entityManager2;
		// Insert schema and classes into the database
		List<Record> records = new ArrayList<>();
		Map<String, Sensor> readingTypeMap = null;
		try {
			// Cache objects
			getTransaction(em).begin();
			for(int i=0;i<10;i++) {
				Record r = new Record();
				r.setUserId(1L);
				records.add(r);
				em.persist(r);
			}
			
			readingTypeMap = createMetadata(em);
			// Store objects
			getTransaction(em).commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// add one more item in the list to see if the owner gets updated 
		// http://openjpa.208410.n2.nabble.com/Unecessary-database-update-td7586121.html
		try {
			getTransaction(em).begin();
			
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
			
			em.persist(hrSensor);
			em.persist(hrReading);

			em.persist(userIphone);
			em.persist(userIpod);
			em.persist(iphoneSession);
			em.persist(ipodSession);
			getTransaction(em).commit();
		} catch (Exception e ) {
			e.printStackTrace();
		}
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
		Transfer aClient = new Transfer();
		aClient.initialize();

		aClient.copy3();
		//aClient.fixGeoHash();
		//aClient.prePopulate();
		//aClient.normalize();
		aClient.finalize();
		System.exit(0);
	}

	public static void main(String[] args) {
		doQuery();
	}
}

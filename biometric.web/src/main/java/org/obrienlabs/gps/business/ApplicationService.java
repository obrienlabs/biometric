package org.obrienlabs.gps.business;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownServiceException;
import java.util.ArrayList;
import java.util.List;



import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;









import javax.inject.Inject;
//import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;
import javax.servlet.ServletOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
//import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import org.obrienlabs.gps.business.entity.Device;
import org.obrienlabs.gps.business.entity.Record;
import org.obrienlabs.gps.business.entity.User;


//@TransactionConfiguration( transactionManager = "transactionManager", defaultRollback = true)
@Service("daoFacade")
@Repository(value="daoFacade")
//@Transactional // will get a javax.persistence.TransactionRequiredException: No transactional EntityManager available without it
public class ApplicationService implements ApplicationServiceLocal {
	
	//private @Value("${server}") String server; // aws only
	private Log log = LogFactory.getLog(ApplicationService.class);
	
	@PersistenceContext(name="${os.environment.persistencecontext.applicationservice.name}")//(unitName="entityManagerFactory", type=PersistenceContextType.TRANSACTION)
	private EntityManager entityManager;
	
    private Map<Long,Record> userRecordMap = new ConcurrentHashMap<>();
    private Long currentUser = 0L;
    public static final Record fakeRecord;
    static {
    	fakeRecord = new Record();
    	fakeRecord.setHeartRate1(0);
    	fakeRecord.setHeartRate2(0);
    	fakeRecord.setLongitude(0.0);
    	fakeRecord.setLattitude(0.0);
    	fakeRecord.setSendSeq(100L);
    }
    
    private Record getLatestRecordPrivate(String user) {
    	Record record = null;
        // check cache
        if(currentUser > 0) {
        	record = userRecordMap.get(Long.valueOf(user));
        	if(null != record) { 
        	    log.info("latest from cache");//: {} {}", user, record);
        	}
        }
        return record;
    }
	
    @Override
    public Boolean health() {
    	Boolean health = true;
    	// TODO: check database
    	return health;
    }
    
	@Override
	public String persist(Object aRecord) {
		String result = "OK";
		try {
			entityManager.persist(aRecord);
			log.info("persisted: " + aRecord);
		} catch (Exception e) {
			e.printStackTrace();
			result = e.getMessage();
		}
		return result;		
	}
	
	@Override
	@Transactional
	public String persist(Record aRecord) {
		return persist(aRecord, false, false);
	}
	
	private static final GeoHash _geohash = new GeoHash();
	
	@Transactional(propagation = Propagation.NEVER)
	public String geohash(double lat, double lon) {
		return _geohash.encode(lat, lon);
	}
	
	@Override
	// org.springframework.transaction.CannotCreateTransactionException: Could not open JPA EntityManager for transaction; nested exception is java.lang.NullPointerException
    @Transactional
	public String persist(Record aRecord, boolean persistORM, boolean persistNoSQL) {
    	String status = "OK";
		try {
			entityManager.merge(aRecord);
			System.out.println("Commit:CMP: " + aRecord);
			// cache value
			userRecordMap.put(aRecord.getUserId(), aRecord);
			currentUser = aRecord.getUserId();
			
			if(persistORM) {
				persistORM(aRecord);
			}
			if(persistNoSQL) {
				persistNoSQL(aRecord);
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = e.getMessage();
		} finally {			
		}
		return status;
	}  
    
	private void persistORM(Record aRecord) {
		// create object tree
//		User aUser = new User();
	}
	
	private void persistNoSQL(Record aRecord) {
		
	}
		
    public List<Long> getSessionIds(Long userId) {
    	return getUserIds();
    }
    
    
    @Override
    public List<Long> getUserIds() {
    	List<Long> aList = new ArrayList<>();
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
	    CriteriaQuery<Long> query = cb.createQuery(Long.class);
	    Root<Record> target = query.from(Record.class);
	    // workaround for http://stackoverflow.com/questions/16348354/how-do-i-write-a-max-query-with-a-where-clause-in-jpa-2-0
	    SingularAttribute<? super Record, Long> anAttribute = entityManager.getMetamodel()
	    		.entity(Record.class).getSingularAttribute("userId", Long.class);
	    query.select(target.get(anAttribute));
	    query.orderBy(
	    		cb.desc(target.get(anAttribute)));
	    query.distinct(true);
	    TypedQuery<Long> typedQuery = entityManager.createQuery(query);
	    // see http://bugs.eclipse.org/303205
	    try {
	    	aList = typedQuery.getResultList();
	    } catch (NoResultException nre) {
	    	nre.printStackTrace();
	    }
	    System.out.println("list: " + aList);
		return aList;
    }

    @Override
    public String activeId() {
        // check local cache first
        //String activeId = null;
        if(currentUser > 0) {
        	return currentUser.toString();
        }
    	Long aLong = null;
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
	    CriteriaQuery<Long> query = cb.createQuery(Long.class);
	    Root<Record> target = query.from(Record.class);
	    // workaround for http://stackoverflow.com/questions/16348354/how-do-i-write-a-max-query-with-a-where-clause-in-jpa-2-0
	    SingularAttribute<? super Record, Long> userIdAttribute = entityManager.getMetamodel()
	    		.entity(Record.class).getSingularAttribute("userId", Long.class);
	    SingularAttribute<? super Record, Long> tsStopAttribute = entityManager.getMetamodel()
	    		.entity(Record.class).getSingularAttribute("tsStop", Long.class);
	    query.select(target.get(userIdAttribute));
	    query.orderBy(
	    		cb.desc(target.get(tsStopAttribute))); // deprecated in mysql 5.7+
	    //query.distinct(true); // fix for mysql 5.7
	    TypedQuery<Long> typedQuery = entityManager.createQuery(query);
		typedQuery.setMaxResults(1);
	    // see http://bugs.eclipse.org/303205
	    try {
	    	aLong = typedQuery.getSingleResult();
	    } catch (NoResultException nre) {
	    	nre.printStackTrace();
	    }
	    System.out.println("id: " + aLong);
		return String.valueOf(aLong.longValue());    	
    }
    
    private void latestPrivate(String user, CriteriaQuery<Record> query, CriteriaBuilder cb, Root<Record> target) {
    	// special case null or 0 means any user id
    	if(null != user && !user.equalsIgnoreCase("0")) {
    		query.where(
    				cb.equal(target.get("userId"), user));
    	}
    }
    
    private TypedQuery<Record> getTypedCriteriaQuery(EntityManager entityManager, String user) {
 		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Record> query = cb.createQuery(Record.class);
		Root<Record> target = query.from(Record.class);
		// workaround for http://stackoverflow.com/questions/16348354/how-do-i-write-a-max-query-with-a-where-clause-in-jpa-2-0
		SingularAttribute<? super Record, Long> anAttribute = entityManager.getMetamodel()
    		.entity(Record.class).getSingularAttribute("tsStop", Long.class);
		query.orderBy(
    		cb.desc(target.get(anAttribute)));
		latestPrivate(user, query, cb, target);
		TypedQuery<Record> typedQuery = entityManager.createQuery(query);
		return typedQuery;	
    }
    
    @Override
    public Record latest(String user) {
    	Record result = getLatestRecordPrivate(user);
    	if(null == result) {
    		try {
    			TypedQuery<Record> typedQuery = getTypedCriteriaQuery(entityManager, user);
    			typedQuery.setMaxResults(1);
    			// see http://bugs.eclipse.org/303205
    			try {
    				result = (Record)typedQuery.getSingleResult();
    			} catch (NoResultException nre) {
    				System.out.println("No result for " + user);
    			} finally {	    	   				
    			}
    		} finally {
    		}
    		System.out.println("latest: " + user + ": " + result);
    		//System.out.println(server);
    	}
		return result;    	   	
    }

    @Override
    public Record latest() {
    	return latest(null);
    }

    @Override
    /**
     * Criteria query for the following JPQL
     * select object(r) from Record r where r.userId=USER order by r.tsStop
     */
	public List<Record> read(String user) {
    	List<Record> result = null;
    	try {
    		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    		CriteriaQuery<Record> query = cb.createQuery(Record.class);
    		Root<Record> target = query.from(Record.class);
    		// workaround for http://stackoverflow.com/questions/16348354/how-do-i-write-a-max-query-with-a-where-clause-in-jpa-2-0
    		SingularAttribute<? super Record, Long> anAttribute = entityManager.getMetamodel()
	    		.entity(Record.class).getSingularAttribute("tsStop", Long.class);
    		query.orderBy(
	    		cb.asc(target.get(anAttribute)));
    		latestPrivate(user, query, cb, target);
    		result = entityManager.createQuery(query).getResultList();
    		System.out.println("user: " + user + ": " + result.size());
    	} catch (Exception e) {
    		e.printStackTrace();
    	} finally {
    	}
    	return result;
	}
	
	@Override
	public String captureImage(ServletOutputStream out, String fullURL) throws Exception {
		//String imageName = null;
		StringBuffer buffer = new StringBuffer();
		
		//ServletOutputStream ps = null;
		/** 
		 * Local Variables
		 * Note: Since there are separate calls to this function for each
		 * thread, these are thread-safe, but don't put generic properties here
		 */			
		/** this stream is used to get the BufferedInputStream below */
		InputStream abstractInputStream = null;
		/** stream to read from the FTP server */
		BufferedInputStream aBufferedInputStream = null;
		/** stream to file system */
		//String filenamePath = getFilename(site, urlAppend, postfix, subdir);
		//FileOutputStream aFileWriter = new FileOutputStream(filenamePath);
		//System.out.println("_Writing to: " + filenamePath);
		/** connection based on the aURL */
		HttpURLConnection aURLConnection = null;
		/** URL object that we can pass to the URLConnection abstract factory */
		URL 	aURL = null;
		/** create a date formatter for time tracking - not thread-safe */
		//SimpleDateFormat aFileStamp = new SimpleDateFormat("yyMMdd_kkmm");		
		long byteCount;			
		// mark the actual bytes read into the buffer, and write only those bytes
		int bytesRead;
		try {
			/*
			 * Clear output content buffer, leave header and status codes
			 * throws IllegalStateException
			 */
			aURL  = new URL(fullURL);
							
			/*
			 * get a connection based on the URL
			 * throws IOException
			 */
			aURLConnection = (HttpURLConnection)aURL.openConnection();
			// fake the agent
			HttpURLConnection.setDefaultAllowUserInteraction(true);
			aURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; MSIE 7.0; Windows NT 6.0; en-US");
			//Map<String, List<String>> map = aURLConnection.getRequestProperties();
	
			/*
			 * get the abstract InputStream from the URLConnection
			 * throws IOException, UnknownServiceException
			 */				
			abstractInputStream = aURLConnection.getInputStream();
			aBufferedInputStream = new BufferedInputStream(abstractInputStream);
			// signed byte counter for file sizes up to 2^63 = 4GB * 2GB
			byteCount = 0;
				
			//System.out.println("Downloading from: " + fullURL);
			/*
			 * buffer the input
			 * Note: the implementation of OutputStream.write(,,)
			 * may not allow the buffer size to affect download speed
			 * Also: the byte array is preinitialized to 0-bytes
			 * Range is -128 to 127
			 */			
			byte[] b = new byte[1024];//INPUT_BUFFER_SIZE];
			
			/*
			 * Read a specific amount of bytes from the input stream at a time
			 * and redirect the buffer to the servlet output stream.
			 * A -1 will signify an EOF on the input.
			 * Start writing to the buffer at position 0
			 * throws IOException - if an I/O error occurs.
			 */
			while ((bytesRead = aBufferedInputStream.read(
			        b, 				// name of buffer
			        0, 				// start of buffer to start reading into
			        b.length		// save actual bytes read, not default max buffer size
			    )) >= 0) {
				/**
				 * We will use the write() function of the abstract superclass
				 * OutputStream not the print() function which is used for html
				 * output and appends cr/lf chars.
				 * Only write out the actual bytes read starting at offset 0
				 * throws IOException 
				 * - if an I/O error occurs. 
				 * In particular, an IOException is thrown if the output stream is closed.
				 * IE: The client closing the browser will invoke the exception
				 * [Connection reset by peer: socket write error]
				 * IOException: Software caused connection abort: socket write error
				 * 
				 * If b is null, a NullPointerException is thrown.
				 * 
				 * Note: The default implementation of write(,,) writes one byte a time
				 * consequently performance may be unaffected by array size
				 */
				// keep track of total bytes read from array
				byteCount += bytesRead;
				// write to file
				//buffer.append(b);
				
				/*char[] c = new char[1024];//INPUT_BUFFER_SIZE];
				for(int i=0;i<bytesRead; i++) {
					c[i] = (char)b[i];
				}*/
				out.write(b, 0, bytesRead);//b.length);
				
				//aFileWriter.write(b, 0, bytesRead);//b.length);
			} // while
			//System.out.println("HTML capture/processing complete: bytes: " + byteCount);
//			imageName = urlAppend;
			// we successfully streamed the file					
			aBufferedInputStream.close();
			// now write the file if it is valid
/*			if(byteCount > 6144) {
				service.setCurrentImage(filenamePath);
			}
*/				
/*			if(null != aFileWriter) {
			    aFileWriter.flush();
			    aFileWriter.close();
			}*/
		} catch (IllegalStateException e) {
			//e.printStackTrace();
			System.out.println(e.getMessage());
			throw e;							
		} catch (UnknownServiceException e) {
			// testcase: remove ftp prefix from the URL
			//e.printStackTrace();
			System.out.println(e.getMessage());
			throw e;							
		} catch (MalformedURLException e) {
			// testcase: remove ftp prefix from the URL
			//e.printStackTrace();
			System.out.println(e.getMessage());
			throw e;			
		} catch (IOException e) {
			// 403 testcase: add text after ftp://
			//e.printStackTrace();
			System.out.println(e.getMessage());
			throw e;							
		} catch (Exception e) {
			//e.printStackTrace();
			System.out.println(e.getMessage());
			//throw e;
		/*
		 * Finalization block executed by all code paths
		 */
		} finally {
			// close input stream
			if(aBufferedInputStream != null) {
				aBufferedInputStream.close();
			} // if
			
			// close file stream
/*			if(aFileWriter != null) {
				//aFileWriter.flush(); 
				aFileWriter.close();
			}*/
			// dereference objects
		} // finally
		
		// only return a non-null image name if the image is invalid
		return "";//buffer.toString();//imageName;
	}

	@Override
	public List<Object> nativeQuery(String queryString) {
    	//EntityManager entityManager = openEntityManager();
		List<Object> list = new ArrayList<>();
		try {
			Query query = entityManager.createNativeQuery(queryString);
			list = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//closeEntityManager(entityManager);
		}
		return list;
	}	
	
	@Override
	public String registerUser(User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String registerDevice(Device device) {
		// TODO Auto-generated method stub
		return null;
	}

    @Override
    public Long geoHashCountFor(String substring) {
    	return readGeoHashSubtreeCount(substring);   	
    }

    /**
     * Criteria query for the following JPQL
     * select count(1) from biometric.gps_record r where r.geohash like 'g%';
     */
	/*public List<Record> readGeoHashSubtreex(String substring) {
    	List<Record> result = null;
    	try {
    		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    		CriteriaQuery<Record> query = cb.createQuery(Record.class);
    		Root<Record> target = query.from(Record.class);
    		// workaround for http://stackoverflow.com/questions/16348354/how-do-i-write-a-max-query-with-a-where-clause-in-jpa-2-0
    		SingularAttribute<? super Record, Long> anAttribute = entityManager.getMetamodel()
	    		.entity(Record.class).getSingularAttribute("tsStop", Long.class);
    		query.orderBy(
	    		cb.asc(target.get(anAttribute)));
    		//latestPrivate(user, query, cb, target);
    	   	if(null != substring) {
        		query.where(
        				cb.like(target.get("geohash"), substring + "%"));
        	}
    		result = entityManager.createQuery(query).getResultList();
    		System.out.println("geohash: " + substring + ": " + result.size());
    	} catch (Exception e) {
    		e.printStackTrace();
    	} finally {
    	}
    	return result;
	}*/
	
	public Long readGeoHashSubtreeCount(String substring) {
    	long count = 0;
    	try {
    		TypedQuery<Long> query = entityManager.createQuery(
    				"select count(r) from Record r where r.geohash like '" + substring +"%'", Long.class);
    		count = query.getSingleResult();
    		//result = entityManager.createQuery(query).getResultList();
    		System.out.println("geohashCount: " + substring + ": " + count);
    	} catch (Exception e) {
    		e.printStackTrace();
    	} finally {
    	}
    	return count;
	}

	@Override
	public String geoHashHotspotCount() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String geoHashHotspotHeart() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String goeHashHotspotSpeed() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String geoHashHotspotHeight() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String geoHashHotspotForce() {
		// TODO Auto-generated method stub
		return null;
	}

}

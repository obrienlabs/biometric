package org.obrienlabs.gps.business.service;

import java.io.OutputStream;
import java.util.List;


//import javax.ejb.Local;
//mport javax.servlet.ServletOutputStream;

import org.obrienlabs.gps.business.entity.Device;
import org.obrienlabs.gps.business.entity.Record;
import org.obrienlabs.gps.business.entity.User;

//@Local
public interface ApplicationServiceLocal {
	String registerUser(User user);
	String registerDevice(Device device);
	
	String persist(Object record);	
	String persist(Record record);
	String persist(Record record, boolean persistORM, boolean persistNoSQL);
	List<Record> read(String user);
	String activeId();
	Record latest(String user);
	Record latest();
	String captureImage(OutputStream out, String fullURL) throws Exception;
	List<Object> nativeQuery(String query);
	List<Long> getSessionIds(Long userId);
	@Deprecated
	List<Long> getUserIds();
	
	String geohash(double lat, double lon);
	Long geoHashCountFor(String substring);
	String geoHashHotspotCount();
	String geoHashHotspotHeart();
	String goeHashHotspotSpeed();
	String geoHashHotspotHeight();
	String geoHashHotspotForce();
	
	Boolean health();
}

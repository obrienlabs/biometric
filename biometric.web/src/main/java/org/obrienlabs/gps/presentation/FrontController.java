/*******************************************************************************
 * Contributors:
 *     16/02/2011 2.3  Michael O'Brien 
 *          - Initial API and implementation platform to be used for 
 *             distributed EE application research, development and architecture
 ******************************************************************************/ 
  
package org.obrienlabs.gps.presentation;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import org.obrienlabs.gps.business.ApplicationService;
import org.obrienlabs.gps.business.ApplicationServiceLocal;
import org.obrienlabs.gps.business.entity.Device;
import org.obrienlabs.gps.business.entity.Reading;
import org.obrienlabs.gps.business.entity.Record;
import org.obrienlabs.gps.business.entity.Sensor;
import org.obrienlabs.gps.business.entity.SensorTransient;
//import javax.servlet.http.HttpSession;
import org.obrienlabs.gps.business.entity.Session;
import org.obrienlabs.gps.business.entity.User;
import org.obrienlabs.gps.util.JDBCTest;


/**
 * <pattern>FrontController</pattern><br>
 * This class is the controller end of an active client first principles ajaxclient.jsp.
 * Normally a standard JSF .xhtml and @ManagedBean presentation bean would be used.
 * It is part of a distributed application framework used to simulate and research
 * concurrency, analytics, management, performance and exception handling.
 * The focus is on utilizing JPA 2.0 as the persistence layer for scenarios involving
 * multicore, multithreaded and multiuser distributed memory L1 persistence applications.
 * The secondary focus is on exercising Java EE6 API to access the results of this distributed application.
 * 
 * @see http://obrienscience.blogspot.com
 * @author Michael O'Brien
 */
//http://developer.android.com/guide/topics/sensors/sensors_overview.html
//@WebListener
public class FrontController extends HttpServlet {
    private static final long serialVersionUID = -312633509671504746L;
    
     @Autowired
    @Qualifier("daoFacade")
    private ApplicationServiceLocal service;// = new ApplicationService();
     
    
    
  	// Application managed EMF and EM
	private Session session = null;
	// Reference the database specific persistence unit in persistence.xml
	public static final String PU_NAME_CREATE = "to";
	private long sentSeq = 1;
	private long recvSeq = 1;
    
    private static AtomicLong nextSessionId = new AtomicLong(1);
    //private Map<Long, AtomicLong> lastTimestampMap = new ConcurrentHashMap<>();
    //private Map<Long, AtomicLong> nextReadingSequenceIdMap = new ConcurrentHashMap<>();
    private static AtomicLong nextReadingSequenceId = new AtomicLong(1);

    public static final String EMPTY_STRING = "";
    public static final String FRONT_CONTROLLER_ACTION = "action";
    public static final String FRONT_CONTROLLER_ACTION_DEMO = "demo";
    public static final String FRONT_CONTROLLER_ACTION_GET_STATISTIC = "getStatistic";    
    public static final String FRONT_CONTROLLER_ACTION_SET_GPS = "setGps";    
    public static final String FRONT_CONTROLLER_ACTION_GET_GPS = "getGps";    
    public static final String FRONT_CONTROLLER_ACTION_GET_KML = "kml";
    public static final String FRONT_CONTROLLER_ACTION_GET_CSV = "csv";
    public static final String FRONT_CONTROLLER_ACTION_DDL = "ddl";
    public static final String FRONT_CONTROLLER_ACTION_DIFF = "diff";
    public static final String FRONT_CONTROLLER_ACTION_LATEST = "latest";   

    
    public static final Record fakeRecord;
    static {
    	fakeRecord = new Record();
    	fakeRecord.setHeartRate1(0);
    	fakeRecord.setHeartRate2(0);
    	fakeRecord.setLongitude(0.0);
    	fakeRecord.setLattitude(0.0);
    	fakeRecord.setSendSeq(100L);
    }
    
    public FrontController() {
        super();
    }

    private void processSession() {
    }
    
	private void processGpsPrivate(HttpServletRequest request,
			HttpServletResponse response, PrintWriter out, boolean persist) {
		// determine if this is a new session
		processSession();

		String user = request.getParameter("u");

		StringBuffer xmlBuffer = new StringBuffer();
		xmlBuffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");

		if (persist) {
			String lg = request.getParameter("lg");
			String lt = request.getParameter("lt");
			String ac = request.getParameter("ac");
			String al = request.getParameter("al");
			String ts = request.getParameter("ts");
			String pressure = request.getParameter("p");
			String temp = request.getParameter("te");
			String bearing = request.getParameter("be");
			String speed = request.getParameter("s");
			String provider = request.getParameter("pr");
			String heartRate1 = request.getParameter("hr1");
			String heartRate2 = request.getParameter("hr2");
			String heartRateDevice1 = request.getParameter("hrd1");
			String heartRateDevice2 = request.getParameter("hrd2");

			String gravityX = request.getParameter("grx");
			String gravityY = request.getParameter("gry");
			String gravityZ = request.getParameter("grz");
			String accelerometerX = request.getParameter("arx");
			String accelerometerY = request.getParameter("ary");
			String accelerometerZ = request.getParameter("arz");
			String gyroscopeX = request.getParameter("gsx");
			String gyroscopeY = request.getParameter("gsy");
			String gyroscopeZ = request.getParameter("gsz");
			String light = request.getParameter("li");
			String linearAccelerationX = request.getParameter("lax");
			String linearAccelerationY = request.getParameter("lay");
			String linearAccelerationZ = request.getParameter("laz");
			String proximity = request.getParameter("px");
			String humidity = request.getParameter("hu");
			String rotationVectorX = request.getParameter("rvx");
			String rotationVectorY = request.getParameter("rvy");
			String rotationVectorZ = request.getParameter("rvz");

			String magFieldX = request.getParameter("mfx");
			String magFieldY = request.getParameter("mfy");
			String magFieldZ = request.getParameter("mfz");

			String sendSeq = request.getParameter("up");

			Record aRecord = new Record();
			if (null != ts && !ts.isEmpty()) {
				aRecord.setTsStart(new Long(ts));
			}
			aRecord.setTsStop(System.currentTimeMillis());
			if (null != lt && !lt.isEmpty()) {
				aRecord.setLattitude(new Double(lt));
			}
			if (null != lg && !lg.isEmpty()) {
				aRecord.setLongitude(new Double(lg));
			}

			aRecord.setUserId(new Long(user));
			if (null != ac && !ac.isEmpty()) {
				aRecord.setAccuracy(new Double(ac));
			}
			if (null != al && !al.isEmpty()) {
				aRecord.setAltitude(new Double(al));
			}
			if (null != pressure && !pressure.isEmpty()) {
				aRecord.setPressure(new Double(pressure));
			}
			if (null != temp && !temp.isEmpty()) {
				aRecord.setTemp(new Double(temp));
			}
			if (null != bearing && !bearing.isEmpty()) {
				aRecord.setBearing(new Integer(bearing));
			}
			if (null != speed && !speed.isEmpty()) {
				aRecord.setSpeed(new Double(speed));
			}
			if (null != provider && !provider.isEmpty()) {
				aRecord.setProvider(provider);
			}
			if (null != heartRate1 && !heartRate1.isEmpty()) {
				aRecord.setHeartRate1(new Integer(heartRate1));
			}
			if (null != heartRate2 && !heartRate2.isEmpty()) {
				aRecord.setHeartRate2(new Integer(heartRate2));
			}
			if (null != heartRateDevice1 && !heartRateDevice1.isEmpty()) {
				aRecord.setHeartRateDevice1(heartRateDevice1);
			}
			if (null != heartRateDevice2 && !heartRateDevice2.isEmpty()) {
				aRecord.setHeartRateDevice2(heartRateDevice2);
			}

			if (null != gravityX && !gravityX.isEmpty()) {
				aRecord.setGravityX(gravityX);
			}
			if (null != gravityY && !gravityY.isEmpty()) {
				aRecord.setGravityY(gravityY);
			}
			if (null != gravityZ && !gravityZ.isEmpty()) {
				aRecord.setGravityZ(gravityZ);
			}
			if (null != accelerometerX && !accelerometerX.isEmpty()) {
				aRecord.setAccelerometerX(accelerometerX);
			}
			if (null != accelerometerY && !accelerometerY.isEmpty()) {
				aRecord.setAccelerometerY(accelerometerY);
			}
			if (null != accelerometerZ && !accelerometerZ.isEmpty()) {
				aRecord.setAccelerometerZ(accelerometerZ);
			}
			if (null != gyroscopeX && !gyroscopeX.isEmpty()) {
				aRecord.setGyroscopeX(gyroscopeX);
			}
			if (null != gyroscopeY && !gyroscopeY.isEmpty()) {
				aRecord.setGyroscopeY(gyroscopeY);
			}
			if (null != gyroscopeZ && !gyroscopeZ.isEmpty()) {
				aRecord.setGyroscopeZ(gyroscopeZ);
			}
			if (null != light && !light.isEmpty()) {
				aRecord.setLight(light);
			}
			if (null != linearAccelerationX && !linearAccelerationX.isEmpty()) {
				aRecord.setLinearAccelerationX(linearAccelerationX);
			}
			if (null != linearAccelerationY && !linearAccelerationY.isEmpty()) {
				aRecord.setLinearAccelerationY(linearAccelerationY);
			}
			if (null != linearAccelerationZ && !linearAccelerationZ.isEmpty()) {
				aRecord.setLinearAccelerationZ(linearAccelerationZ);
			}
			if (null != proximity && !proximity.isEmpty()) {
				aRecord.setProximity(proximity);
			}
			if (null != humidity && !humidity.isEmpty()) {
				aRecord.setHumidity(humidity);
			}
			if (null != rotationVectorX && !rotationVectorX.isEmpty()) {
				aRecord.setRotationVectorX(rotationVectorX);
			}
			if (null != rotationVectorY && !rotationVectorY.isEmpty()) {
				aRecord.setRotationVectorY(rotationVectorY);
			}
			if (null != rotationVectorZ && !rotationVectorZ.isEmpty()) {
				aRecord.setRotationVectorZ(rotationVectorZ);
			}
			if (null != magFieldX && !magFieldX.isEmpty()) {
				aRecord.setTeslaX(magFieldX);
			}
			if (null != magFieldY && !magFieldY.isEmpty()) {
				aRecord.setTeslaY(magFieldY);
			}
			if (null != magFieldZ && !magFieldZ.isEmpty()) {
				aRecord.setTeslaZ(magFieldZ);
			}
			if (null != sendSeq && !sendSeq.isEmpty()) {
				aRecord.setSendSeq(new Long(sendSeq));
			}

			aRecord.setRecvSeq(nextReadingSequenceId.addAndGet(1));

			String geohash = service.geohash(new Double(lt), new Double(lg));
			//System.out.println(geohash);
			aRecord.setGeohash(geohash);
			String status = service.persist(aRecord);
			out.println(status + ":" + geohash + ":" + aRecord);// xmlBuffer.toString());
			
			normalize(aRecord);
		} else {
			List<Record> records = readTx(user);
			xmlBuffer.append("<user>" + user + "</user>");
			for (Record record : records) {
				xmlBuffer.append("<record>" + record.toString() + "</record>");
			}
			out.println(xmlBuffer.toString());
		}

	}

	private void normalize(Record aRecord) {
		// get readings for all sensor values
		// 
	}
	
    private String getGPSDateFormat(long milliseconds) {
    	StringBuilder buffer = new StringBuilder();
        //DateFormat df = new SimpleDateFormat("YYYY-MM-dd");
        //DateFormat tf = new SimpleDateFormat("HH:mm:ss");
        final Calendar cal = Calendar.getInstance();
    	cal.setTimeInMillis(milliseconds);
    	// here's how to get the minutes
    	//final int minutes = cal.get(Calendar.MINUTE);
    	int year = cal.get(Calendar.YEAR);
    	int month = cal.get(Calendar.MONTH) + 1;
    	int day = cal.get(Calendar.DAY_OF_MONTH);
    	
    	buffer.append(year);
    	buffer.append("-");
    	if(month < 10) buffer.append("0");
    	buffer.append(month);
    	buffer.append("-");
    	if(day < 10) buffer.append("0");
    	buffer.append(day +"T");
    	String second =""+ (milliseconds / 1000) % 60;
    	String minute =""+ (milliseconds / (1000 * 60)) % 60;
    	String hour = ""+(milliseconds / (1000 * 60 * 60)) % 24;
    	if(second.length() == 1) second = "0" + second;
    	if(minute.length() == 1) minute = "0" + minute;
    	if(hour.length() == 1) hour = "0" + hour;
    	buffer.append(hour+":"+minute+":"+second + "Z");
    	
        // 			<when>2012-12-03T21:38:01Z</when>
        //Date date = new Date(record.getTsStart());
        //String dateFormatted = df.format(date) + "T" + tf.format(date) + "Z";
        //xmlBuffer.append(record.getTsStart())
        //dateFormatted	"2013-06-174T22:29:04Z" (id=32)
        return buffer.toString();
	
    }
	
    private void processCSV(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
        String user = request.getParameter("u");        
        StringBuffer xmlBuffer = new StringBuffer();
        List<Record> records = readTx(user);	
        for(Record record : records) {
            xmlBuffer.append(record.getId()).append(",");
            xmlBuffer.append(record.getUserId()).append(",");
            xmlBuffer.append(record.getSendSeq()).append(",");
            xmlBuffer.append(record.getRecvSeq()).append(",");
            
            xmlBuffer.append(record.getLongitude()).append(",");
            xmlBuffer.append(record.getLattitude()).append(",");
            xmlBuffer.append(record.getTsStart()).append(",");
            xmlBuffer.append(record.getTsStop()).append(",");
            xmlBuffer.append(record.getAccuracy()).append(",");
            xmlBuffer.append(record.getBearing()).append(",");
            xmlBuffer.append(record.getAltitude()).append(",");
            xmlBuffer.append(record.getTemp()).append(",");
            xmlBuffer.append(record.getPressure()).append(",");
            xmlBuffer.append(record.getTeslaX()).append(",");
            xmlBuffer.append(record.getTeslaY()).append(",");
            xmlBuffer.append(record.getTeslaZ()).append(",");
            xmlBuffer.append(record.getSpeed()).append(",");
            xmlBuffer.append(record.getProvider()).append(",");
            xmlBuffer.append(record.getHeartRateDevice1()).append(",");
            xmlBuffer.append(record.getHeartRateDevice2()).append(",");
            xmlBuffer.append(record.getHeartRate1()).append(",");
            xmlBuffer.append(record.getHeartRate2()).append(",");
            xmlBuffer.append(record.getGravityX()).append(",");
            xmlBuffer.append(record.getGravityY()).append(",");
            xmlBuffer.append(record.getGravityZ()).append(",");
            xmlBuffer.append(record.getAccelerometerX()).append(",");
            xmlBuffer.append(record.getAccelerometerY()).append(",");
            xmlBuffer.append(record.getAccelerometerZ()).append(",");
            xmlBuffer.append(record.getGyroscopeX()).append(",");
            xmlBuffer.append(record.getGyroscopeY()).append(",");
            xmlBuffer.append(record.getGyroscopeZ()).append(",");
            xmlBuffer.append(record.getLight()).append(",");
            xmlBuffer.append(record.getLinearAccelerationX()).append(",");
            xmlBuffer.append(record.getLinearAccelerationY()).append(",");
            xmlBuffer.append(record.getLinearAccelerationZ()).append(",");
            xmlBuffer.append(record.getProximity()).append(",");
            xmlBuffer.append(record.getHumidity()).append(",");
            xmlBuffer.append(record.getRotationVectorX()).append(",");
            xmlBuffer.append(record.getRotationVectorY()).append(",");
            xmlBuffer.append(record.getRotationVectorZ()).append("\r\n");
        }
        out.println(xmlBuffer.toString()); 
    }

    private Record getLatestRecordPrivate(String user) {
    	Record record = service.latest(user);//null;
        // send fake record;
        if(record == null ) {
        	record = fakeRecord;
        }	
        return record;
    }
    
    private void processLatest(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
        String user = request.getParameter("u");        
        StringBuffer xmlBuffer = new StringBuffer();
        Record record = getLatestRecordPrivate(user);
        
        	xmlBuffer.append("{ \"tsStop\" : ").append(record.getTsStop()).append(",");
        	xmlBuffer.append(" \"tsStart\" : ").append(record.getTsStart()).append(",");
        	xmlBuffer.append(" \"sendSeq\" : ").append(record.getSendSeq()).append(",");
        	xmlBuffer.append(" \"recvSeq\" : ").append(record.getRecvSeq()).append(",");
        	xmlBuffer.append(" \"heartRate1\" : ").append(record.getHeartRate1()).append(",");
        	xmlBuffer.append(" \"heartRate2\" : ").append(record.getHeartRate2()).append(",");
        	xmlBuffer.append(" \"longitude\" : ").append(record.getLongitude()).append(",");
        	xmlBuffer.append(" \"lattitude\" : ").append(record.getLattitude()).append("");
        	xmlBuffer.append("}");
        out.println(xmlBuffer.toString()); 
    }

    private void processActiveId(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
        String user = request.getParameter("u");        
        StringBuffer xmlBuffer = new StringBuffer();
        // check local cache first
        String activeId = service.activeId();//null;
        xmlBuffer.append("{ \"id\" : ").append(activeId).append("}");
        out.println(xmlBuffer.toString()); 
    }

    
    private void processKML(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
        String user = request.getParameter("u");        
        StringBuffer xmlBuffer = new StringBuffer();
        List<Record> records = readTx(user);
        if(records.size() < 1) {
        	xmlBuffer.append("<size>0</size>");
        } else {
        xmlBuffer.append("        <kml xmlns=\"http://www.opengis.net/kml/2.2\" xmlns:gx=\"http://www.google.com/kml/ext/2.2\" xmlns:kml=\"http://www.opengis.net/kml/2.2\" xmlns:atom=\"http://www.w3.org/2005/Atom\">\n");
        xmlBuffer.append("        <Document>\n");
        xmlBuffer.append("        	<name>FrontController.kml</name>\n");
        xmlBuffer.append("        	<StyleMap id=\"multiTrack\">\n");
        xmlBuffer.append("        		<Pair>\n");
        xmlBuffer.append("        			<key>normal</key>\n");
        xmlBuffer.append("        			<styleUrl>#multiTrack_n</styleUrl>\n");
        xmlBuffer.append("        		</Pair>\n");
        xmlBuffer.append("        		<Pair>\n");
        xmlBuffer.append("        			<key>highlight</key>\n");
        xmlBuffer.append("        			<styleUrl>#multiTrack_h</styleUrl>\n");
        xmlBuffer.append("        		</Pair>\n");
        xmlBuffer.append("        	</StyleMap>\n");
        xmlBuffer.append("        	<Style id=\"multiTrack_h\">\n");
        xmlBuffer.append("        		<IconStyle>\n");
        xmlBuffer.append("        			<scale>1.2</scale>\n");
        xmlBuffer.append("        			<Icon>\n");
        xmlBuffer.append("        				<href>http://earth.google.com/images/kml-icons/track-directional/track-0.png</href>\n");
        xmlBuffer.append("        			</Icon>\n");
        xmlBuffer.append("</IconStyle>\n");
        xmlBuffer.append("        		<LineStyle>\n");
        xmlBuffer.append("        			<color>99ffac59</color>\n");
        xmlBuffer.append("        			<width>8</width>\n");
        xmlBuffer.append("        		</LineStyle>\n");
        xmlBuffer.append("        	</Style>\n");
        xmlBuffer.append("        	<Style id=\"multiTrack_n\">\n");
        xmlBuffer.append("        		<IconStyle>\n");
        xmlBuffer.append("        			<Icon>\n");
        xmlBuffer.append("        				<href>http://earth.google.com/images/kml-icons/track-directional/track-0.png</href>\n");
        xmlBuffer.append("        			</Icon>\n");
        xmlBuffer.append("        		</IconStyle>\n");
        xmlBuffer.append("        		<LineStyle>\n");
        xmlBuffer.append("        			<color>99ffac59</color>\n");
        xmlBuffer.append("        			<width>6</width>\n");
        xmlBuffer.append("        		</LineStyle>\n");
        xmlBuffer.append("        	</Style>\n");
        xmlBuffer.append("        	<Placemark>\n");
        xmlBuffer.append("        		<name>FrontController</name>\n");
        xmlBuffer.append("        		<LookAt>\n");
        xmlBuffer.append("        			<gx:TimeSpan>\n");
        xmlBuffer.append("        				<begin>");
        xmlBuffer.append(this.getGPSDateFormat(Long.valueOf(records.get(0).getTsStop())));//.getTsStart())));
        xmlBuffer.append("</begin>\n");
        xmlBuffer.append("        				<end>");
        xmlBuffer.append(this.getGPSDateFormat(Long.valueOf(records.get(records.size() - 1).getTsStop())));//.getTsStart())));
        xmlBuffer.append("</end>\n");
        xmlBuffer.append("        			</gx:TimeSpan>\n");
        xmlBuffer.append("        			<longitude>-75.94066170441245</longitude>\n");
        xmlBuffer.append("        			<latitude>45.34373422857725</latitude>\n");
        xmlBuffer.append("        			<altitude>0</altitude>\n");
        xmlBuffer.append("        			<heading>17.18805462791894</heading>\n");
        xmlBuffer.append("        			<tilt>12.71054082497606</tilt>\n");
        xmlBuffer.append("        			<range>117.8841883659822</range>\n");
        xmlBuffer.append("        			<gx:altitudeMode>relativeToSeaFloor</gx:altitudeMode>\n");
        xmlBuffer.append("        		</LookAt>\n");
        xmlBuffer.append("        		<styleUrl>#multiTrack</styleUrl>\n");
        xmlBuffer.append("		<gx:Track>\n");
        
        for(Record record : records) {
            long timestamp = Long.valueOf(record.getTsStop());//.getTsStart());
            if(timestamp > 0) { // don't display <when>1970-00-01T00:00:00Z</when>
            	xmlBuffer.append("<when>");
            	xmlBuffer.append(this.getGPSDateFormat(timestamp));
            	xmlBuffer.append("</when>\n");
            }
        }
        for(Record record : records) {
        	Double longitude = record.getLongitude();
        	Double lattitude = record.getLattitude();
        	Double altitude = record.getAltitude();
        	if(!(longitude == 0 && lattitude == 0 && altitude == 0)) { // sorry 0 alt at 0,0)
        		xmlBuffer.append("<gx:coord>\n");
        		xmlBuffer.append(longitude);
        		xmlBuffer.append(" ");
        		xmlBuffer.append(lattitude);
        		xmlBuffer.append(" ");
        		xmlBuffer.append(altitude);
        		xmlBuffer.append("</gx:coord>\n");
        	}
        }
        
        xmlBuffer.append("		</gx:Track>\n");
        xmlBuffer.append("	</Placemark>\n");
        xmlBuffer.append("</Document>\n");
        xmlBuffer.append("</kml>\n");
        }
        System.out.println(xmlBuffer.toString());
        out.println(xmlBuffer.toString());   
        
    }
    
    private void processGetGps(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
    	processGpsPrivate(request, response, out, false);
    }
    private void processSetGps(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
    	processGpsPrivate(request, response, out, true);
    }   

    private List<Record> readTx(String user) {
		List<Record> list = null;
		try {
			list = service.read(user);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
		return list;
	}    

    private void processDemoCommand(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
        String cell = request.getParameter("cell");
        int cellNumber = -1;
        if(null != cell) {
            cellNumber = Integer.parseInt(cell);
        }
        
        StringBuffer xmlBuffer = new StringBuffer();
        long number = System.nanoTime();
        xmlBuffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        xmlBuffer.append("<stat>");
        switch (cellNumber) {
        case 0:
            long random = Math.round(Math.random() * 1000);
        	try {
        		//xmlBuffer.append(service.captureImage("http://weather.gc.ca/data/lightning_images/NAT.png"));
        	} catch (Exception e) {
        		e.printStackTrace(out);
        	}
            xmlBuffer.append(random);
            break;
        case 1:
            xmlBuffer.append(number);
            break;
        case 2:
        	//xmlBuffer.append(new JDBCTest().testConnection("o*s", "Or*d", "true"));
        	break;
        default:
            xmlBuffer.append(number);
            break;
        }
        xmlBuffer.append("</stat>");
        out.println(xmlBuffer.toString());        
        //StringBuffer outBuffer = new StringBuffer("Thread: ");        
        System.out.println("_xml: " + xmlBuffer.toString());
    }
    
    //http://127.0.0.1:7001/opendata/FrontController?action=image
    // https://java-trialanzh.java.us1.oraclecloudapps.com/opendata/FrontController?action=image
    private void processImage(HttpServletRequest request, HttpServletResponse response, ServletOutputStream out) {
        StringBuffer xmlBuffer = new StringBuffer();
        try {
        		service.captureImage(out, "http://weather.gc.ca/data/lightning_images/NAT.png");
        		//service.captureImage(out, "https://www.google.ca/images/google_favicon_128.png");
        		
        	} catch (Exception e) {
        		try {
        		out.print(e.getLocalizedMessage());
        		} catch (IOException ioe) {
        			
        		}
        	}
        //out.println(xmlBuffer.toString());        
        //StringBuffer outBuffer = new StringBuffer("Thread: ");        
        //System.out.println("_xml: " + xmlBuffer.toString());
    }    
    private void processAction(HttpServletRequest aRequest, HttpServletResponse aResponse) {
        PrintWriter out = null;
        try {
                //HttpSession aSession = aRequest.getSession(true);
                String action = aRequest.getParameter(FRONT_CONTROLLER_ACTION);
                if(null == action) {
                    action = FRONT_CONTROLLER_ACTION_DEMO;
                }
                // Process requests
                if(action.equalsIgnoreCase(FRONT_CONTROLLER_ACTION_DEMO)) {
                    aResponse.setContentType("text/xml");
                    Writer writer = new BufferedWriter(new OutputStreamWriter(aResponse.getOutputStream(),"UTF-8"));
                    out = new PrintWriter(writer, true);
                    processDemoCommand(aRequest, aResponse, out);
                }
                if(action.equalsIgnoreCase("latest")) {
                    aResponse.setContentType("application/json");
                    Writer writer = new BufferedWriter(new OutputStreamWriter(aResponse.getOutputStream(),"UTF-8"));
                    out = new PrintWriter(writer, true);
                    processLatest(aRequest, aResponse, out);
                }
                if(action.equalsIgnoreCase(FRONT_CONTROLLER_ACTION_SET_GPS)) {
                    aResponse.setContentType("text/xml");
                    Writer writer = new BufferedWriter(new OutputStreamWriter(aResponse.getOutputStream(),"UTF-8"));
                    out = new PrintWriter(writer, true);
                    processSetGps(aRequest, aResponse, out);
                }
                if(action.equalsIgnoreCase(FRONT_CONTROLLER_ACTION_GET_GPS)) {
                    aResponse.setContentType("text/xml");
                    Writer writer = new BufferedWriter(new OutputStreamWriter(aResponse.getOutputStream(),"UTF-8"));
                    out = new PrintWriter(writer, true);
                    processGetGps(aRequest, aResponse, out);
                }
                if(action.equalsIgnoreCase(FRONT_CONTROLLER_ACTION_GET_KML)) {
                    aResponse.setContentType("text/xml");
                    Writer writer = new BufferedWriter(new OutputStreamWriter(aResponse.getOutputStream(),"UTF-8"));
                    out = new PrintWriter(writer, true);
                    processKML(aRequest, aResponse, out);
                }
                if(action.equalsIgnoreCase(FRONT_CONTROLLER_ACTION_GET_CSV)) {
                    aResponse.setContentType("text/csv");
                    Writer writer = new BufferedWriter(new OutputStreamWriter(aResponse.getOutputStream(),"UTF-8"));
                    out = new PrintWriter(writer, true);
                    processCSV(aRequest, aResponse, out);
                }                
                if(action.equalsIgnoreCase(FRONT_CONTROLLER_ACTION_DDL)) {
                    aResponse.setContentType("text/html");
                    Writer writer = new BufferedWriter(new OutputStreamWriter(aResponse.getOutputStream(),"UTF-8"));
                    out = new PrintWriter(writer, true);
                    //processDDL(aRequest, aResponse, out);
                }
                if(action.equalsIgnoreCase("image")) {
                    aResponse.setContentType("image/png");
                    //Writer writer = new BufferedWriter(new OutputStreamWriter(aResponse.getOutputStream(),"UTF-8"));
                    //out = new PrintWriter(writer, true);
                    processImage(aRequest, aResponse, aResponse.getOutputStream());
                }
                if(action.equalsIgnoreCase("diff")) {
                    aResponse.setContentType("text/csv");
                    Writer writer = new BufferedWriter(new OutputStreamWriter(aResponse.getOutputStream(),"UTF-8"));
                    out = new PrintWriter(writer, true);
                    processDiff(aRequest, aResponse, out);
                }            
                  
                if(action.equalsIgnoreCase("activeid")) {
                    aResponse.setContentType("application/json");
                    Writer writer = new BufferedWriter(new OutputStreamWriter(aResponse.getOutputStream(),"UTF-8"));
                    out = new PrintWriter(writer, true);
                    processActiveId(aRequest, aResponse, out);
                }                                   
        } catch (Exception e) {
            	e.printStackTrace();
        }
    }
    
    private void processDiff(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
        String user = request.getParameter("u");        
        StringBuffer xmlBuffer = new StringBuffer();
        List<Record> records = readTx(user);	
        for(Record record : records) {
            xmlBuffer.append(record.getId()).append(",");
            xmlBuffer.append(record.getUserId()).append(",");
            xmlBuffer.append(record.getSendSeq()).append(",");
            xmlBuffer.append(record.getRecvSeq()).append(",");
            
            xmlBuffer.append(record.getLongitude()).append(",");
            xmlBuffer.append(record.getLattitude()).append(",");
            xmlBuffer.append(record.getTsStart()).append(",");
            xmlBuffer.append(record.getTsStop()).append(",");
            xmlBuffer.append(record.getAccuracy()).append(",");
            xmlBuffer.append(record.getBearing()).append(",");
            xmlBuffer.append(record.getAltitude()).append(",");
            xmlBuffer.append(record.getTemp()).append(",");
            xmlBuffer.append(record.getPressure()).append(",");
            xmlBuffer.append(record.getTeslaX()).append(",");
            xmlBuffer.append(record.getTeslaY()).append(",");
            xmlBuffer.append(record.getTeslaZ()).append(",");
            xmlBuffer.append(record.getSpeed()).append(",");
            xmlBuffer.append(record.getProvider()).append(",");
            xmlBuffer.append(record.getHeartRateDevice1()).append(",");
            xmlBuffer.append(record.getHeartRateDevice2()).append(",");
            xmlBuffer.append(record.getHeartRate1()).append(",");
            xmlBuffer.append(record.getHeartRate2()).append(",");
            xmlBuffer.append(record.getGravityX()).append(",");
            xmlBuffer.append(record.getGravityY()).append(",");
            xmlBuffer.append(record.getGravityZ()).append(",");
            xmlBuffer.append(record.getAccelerometerX()).append(",");
            xmlBuffer.append(record.getAccelerometerY()).append(",");
            xmlBuffer.append(record.getAccelerometerZ()).append(",");
            xmlBuffer.append(record.getGyroscopeX()).append(",");
            xmlBuffer.append(record.getGyroscopeY()).append(",");
            xmlBuffer.append(record.getGyroscopeZ()).append(",");
            xmlBuffer.append(record.getLight()).append(",");
            xmlBuffer.append(record.getLinearAccelerationX()).append(",");
            xmlBuffer.append(record.getLinearAccelerationY()).append(",");
            xmlBuffer.append(record.getLinearAccelerationZ()).append(",");
            xmlBuffer.append(record.getProximity()).append(",");
            xmlBuffer.append(record.getHumidity()).append(",");
            xmlBuffer.append(record.getRotationVectorX()).append(",");
            xmlBuffer.append(record.getRotationVectorY()).append(",");
            xmlBuffer.append(record.getRotationVectorZ()).append("\r\n");
        }
        out.println(xmlBuffer.toString()); 
    }

	@Override
    public void init(ServletConfig config) throws ServletException {
		super.init(config);
		// enable autowiring in servlets
		SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,
			      config.getServletContext());
	}

	@Override
    public ServletConfig getServletConfig() {
		return null;
	}

	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processAction(request, response);
	}

	@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processAction(request, response);
	}

	@Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	@Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	@Override
    protected void doHead(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}
	
	private void processDDL(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
		// aClient.processInsert();
		// aClient.processInsertMT();
		processInsertPM();
		// aClient.queryJPQLTest();
		// aClient.queryCriteriaTest();
		// aClient.queryCriteriaTestMT();
		//aClient.queryCriteriaTestPM();
		out.println(service.latest("201403"));
		
		//System.exit(0);
	}

	private Map<String, Sensor> createMetadataSensors() {
		// create standard types
		Map<String, Sensor> types = new HashMap<>();
		
		// create dynamic types
		Sensor hrSensor = new SensorTransient();
		hrSensor.setLabel("HEART_RATE");//Sensor.SENSORS_BUILTIN.HEART_RATE.toString());
		hrSensor.setType(Sensor.NUMERIC_TYPES.INT.toString());
		types.put(hrSensor.getLabel(), hrSensor);
		// no need to set reverse relationship to reading
		for(Sensor type : types.values()) {
			service.persist(type);
		}
		return types;
	}
	
	private Map<String, Sensor> createMetadata() {
		int counter = 0;
		
		// register users
		User user = new User();
		user.setFirstAccess(System.currentTimeMillis());
		user.setName("michael");
		// register new types // associate with user
		Map<String, Sensor> readingTypeMap = createMetadataSensors();
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
			service.persist(reading);
		}
		service.persist(user);
		service.persist(session);
		return readingTypeMap;
	}
	
	private void processInsertPM() {
		// Insert schema and classes into the database
		List<Record> records = new ArrayList<>();
		Map<String, Sensor> readingTypeMap = null;
		try {
			// Cache objects
			//getTransaction().begin();
			for(int i=0;i<10;i++) {
				Record r = new Record();
				r.setUserId(1L);
				records.add(r);
				service.persist(r);
			}
			
			readingTypeMap = createMetadata();
			// Store objects
			//getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// add one more item in the list to see if the owner gets updated 
		// http://openjpa.208410.n2.nabble.com/Unecessary-database-update-td7586121.html
		try {
			//getTransaction().begin();
			
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

			service.persist(hrSensor);
			
			service.persist(hrSensor);
			service.persist(hrReading);

			service.persist(userIphone);
			service.persist(userIpod);
			service.persist(iphoneSession);
			service.persist(ipodSession);
		} catch (Exception e ) {
			e.printStackTrace();
		}
	}
	
}

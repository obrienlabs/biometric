package com.obrienlabs.gps.business.entity;

import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

@MappedSuperclass
public class DataObject {

    @Version
    private Long version;
/*
    public static final Map<String, String> urlToColumnMap;
    public static final Map<String, String> columnToUrlMap;
    public static final char[] URL_PARAMS = {
    
	"lg", // longitude
	"lt", // latitude
	"ac", // accuracy
	"al", // altitude
	"ts", // start time)
	"p");
	"te");
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
    */
    //@Id
   // private Long tenantID;
	public Long getVersion() {
		return version;
	}

	
}

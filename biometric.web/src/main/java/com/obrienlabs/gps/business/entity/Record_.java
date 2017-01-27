package com.obrienlabs.gps.business.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2016-06-12T13:04:08.112-0400")
@StaticMetamodel(Record.class)
public class Record_ {
	public static volatile SingularAttribute<Record, Long> userId;
	public static volatile SingularAttribute<Record, String> geohash;
	public static volatile SingularAttribute<Record, Long> recvSeq;
	public static volatile SingularAttribute<Record, Long> sendSeq;
	public static volatile SingularAttribute<Record, Double> longitude;
	public static volatile SingularAttribute<Record, Double> lattitude;
	public static volatile SingularAttribute<Record, Long> tsStart;
	public static volatile SingularAttribute<Record, Long> tsStop;
	public static volatile SingularAttribute<Record, Double> accuracy;
	public static volatile SingularAttribute<Record, Integer> bearing;
	public static volatile SingularAttribute<Record, Double> altitude;
	public static volatile SingularAttribute<Record, Double> temp;
	public static volatile SingularAttribute<Record, Double> pressure;
	public static volatile SingularAttribute<Record, String> teslaX;
	public static volatile SingularAttribute<Record, String> teslaY;
	public static volatile SingularAttribute<Record, String> teslaZ;
	public static volatile SingularAttribute<Record, Double> speed;
	public static volatile SingularAttribute<Record, String> provider;
	public static volatile SingularAttribute<Record, String> heartRateDevice1;
	public static volatile SingularAttribute<Record, String> heartRateDevice2;
	public static volatile SingularAttribute<Record, Integer> heartRate1;
	public static volatile SingularAttribute<Record, Integer> heartRate2;
	public static volatile SingularAttribute<Record, String> gravityX;
	public static volatile SingularAttribute<Record, String> gravityY;
	public static volatile SingularAttribute<Record, String> gravityZ;
	public static volatile SingularAttribute<Record, String> accelerometerX;
	public static volatile SingularAttribute<Record, String> accelerometerY;
	public static volatile SingularAttribute<Record, String> accelerometerZ;
	public static volatile SingularAttribute<Record, String> gyroscopeX;
	public static volatile SingularAttribute<Record, String> gyroscopeY;
	public static volatile SingularAttribute<Record, String> gyroscopeZ;
	public static volatile SingularAttribute<Record, String> light;
	public static volatile SingularAttribute<Record, String> linearAccelerationX;
	public static volatile SingularAttribute<Record, String> linearAccelerationY;
	public static volatile SingularAttribute<Record, String> linearAccelerationZ;
	public static volatile SingularAttribute<Record, String> proximity;
	public static volatile SingularAttribute<Record, String> humidity;
	public static volatile SingularAttribute<Record, String> rotationVectorX;
	public static volatile SingularAttribute<Record, String> rotationVectorY;
	public static volatile SingularAttribute<Record, String> rotationVectorZ;
}

package org.obrienlabs.gps.business.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2017-02-05T20:29:54.042-0500")
@StaticMetamodel(Sensor.class)
public class Sensor_ {
	public static volatile SingularAttribute<Sensor, Long> id;
	public static volatile SingularAttribute<Sensor, Long> sensorId;
	public static volatile SingularAttribute<Sensor, Boolean> manual;
	public static volatile SingularAttribute<Sensor, String> label;
	public static volatile SingularAttribute<Sensor, String> type;
	public static volatile SingularAttribute<Sensor, String> unit;
	public static volatile SingularAttribute<Sensor, String> serial;
	public static volatile SingularAttribute<Sensor, Boolean> builtIn;
	public static volatile SingularAttribute<Sensor, Boolean> dynamic;
}

package org.obrienlabs.gps.business.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2017-02-05T20:29:54.040-0500")
@StaticMetamodel(Reading.class)
public class Reading_ {
	public static volatile SingularAttribute<Reading, Long> id;
	public static volatile SingularAttribute<Reading, Sensor> sensor;
	public static volatile SingularAttribute<Reading, Session> session;
	public static volatile SingularAttribute<Reading, String> value;
	public static volatile SingularAttribute<Reading, Long> timestampSent;
	public static volatile SingularAttribute<Reading, Long> timestampRec;
	public static volatile SingularAttribute<Reading, Long> sequenceNumberSent;
	public static volatile SingularAttribute<Reading, Long> sequenceNumberRec;
}

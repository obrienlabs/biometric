package org.obrienlabs.gps.business.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2017-02-05T20:29:54.039-0500")
@StaticMetamodel(Device.class)
public class Device_ {
	public static volatile SingularAttribute<Device, Long> id;
	public static volatile SingularAttribute<Device, Long> deviceId;
	public static volatile SingularAttribute<Device, String> ip;
	public static volatile SingularAttribute<Device, String> name;
	public static volatile SingularAttribute<Device, Long> firstAccess;
	public static volatile SingularAttribute<Device, Long> lastAccess;
	public static volatile SingularAttribute<Device, User> user;
}

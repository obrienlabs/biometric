package org.obrienlabs.gps.business.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2017-02-05T20:29:54.045-0500")
@StaticMetamodel(User.class)
public class User_ {
	public static volatile SingularAttribute<User, Long> id;
	public static volatile SingularAttribute<User, Long> userId;
	public static volatile SingularAttribute<User, String> name;
	public static volatile SingularAttribute<User, Long> firstAccess;
	public static volatile SingularAttribute<User, Long> lastAccess;
	public static volatile SetAttribute<User, Session> sessions;
	public static volatile SetAttribute<User, Device> devices;
}

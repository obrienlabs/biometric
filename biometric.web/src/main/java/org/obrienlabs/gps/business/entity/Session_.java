package org.obrienlabs.gps.business.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2017-02-05T20:29:54.045-0500")
@StaticMetamodel(Session.class)
public class Session_ {
	public static volatile SingularAttribute<Session, Long> id;
	public static volatile SingularAttribute<Session, Long> sessionId;
	public static volatile SingularAttribute<Session, User> user;
	public static volatile ListAttribute<Session, Reading> readings;
}

package org.obrienlabs.gps.business.entity;


import java.util.HashSet;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


//import org.eclipse.persistence.annotations.Multitenant;
//import org.eclipse.persistence.annotations.MultitenantType;
//import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;

@Entity
@Table(name="DEVICE")
//@IdClass(IdClassRecordPK.class)
//@DiscriminatorColumn(name="DTYPE", discriminatorType=DiscriminatorType.STRING, length=1)
@Access(value = AccessType.FIELD)
//@Multitenant(MultitenantType.SINGLE_TABLE)
//@TenantDiscriminatorColumn(name = "TENANT", contextProperty = "multi-tenant.id") // not part of the PK
// http://wiki.eclipse.org/EclipseLink/Development/Indigo/Multi-Tenancy
//@NoSql
@XmlType
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Device extends DataObject {
	
    @Id
    @Column(name="IDENT_ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long deviceId;
    
    private String ip;
    
    private String name;
    private Long firstAccess;
    private Long lastAccess;
    
    @ManyToOne
    private User user;
    
    /*@OneToMany(mappedBy="user", cascade=CascadeType.ALL)
    private Set<Session> sessions;
    
    public void addSession(Session session) {
    	if(null == sessions) {
    		sessions = new HashSet<>();
    	}
    	sessions.add(session);
    }
    
    public Set<Session> getSessions() {
		return sessions;
	}

	public void setSessions(Set<Session> sessions) {
		this.sessions = sessions;
	}*/

	public Long getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getFirstAccess() {
		return firstAccess;
	}

	public void setFirstAccess(Long firstAccess) {
		this.firstAccess = firstAccess;
	}

	public Long getLastAccess() {
		return lastAccess;
	}

	public void setLastAccess(Long lastAccess) {
		this.lastAccess = lastAccess;
	}

	@Override
    public String toString() {
    	StringBuffer buffer = new StringBuffer();
    	buffer.append(this.getClass().getSimpleName());
    	buffer.append("(");
    	buffer.append(this.getId());
    	buffer.append(")");
    	return buffer.toString();
    }

}

package com.obrienlabs.gps.business.entity;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

//import org.eclipse.persistence.annotations.Multitenant;
//import org.eclipse.persistence.annotations.MultitenantType;
//import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;

@Entity
@Table(name="SESSION")
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
public class Session extends DataObject {

    @Id
    @Column(name="IDENT_ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    @XmlElement
    private Long id;

    @XmlElement 
    private Long sessionId;
    
    @ManyToOne
    private User user;
    
    // FK
    @OneToMany(mappedBy="session", cascade=CascadeType.ALL)
    // Join table
    //@OneToMany(cascade=CascadeType.ALL)
    /*@JoinTable(name="READING_SESSION_JOIN",
		joinColumns=@JoinColumn(name="SESSION_ID"),
		inverseJoinColumns=@JoinColumn(name="READING_ID"))*/
    private List<Reading> readings;
    

    /*@ManyToMany
    @JoinTable(name="DATA_EXT",
    	joinColumns=@JoinColumn(name="D_ID"),
    	inverseJoinColumns=@JoinColumn(name="E_ID"))
    @MapKeyColumn(name="id")
    private Map<Sensor, Reading> readingsOfType;
    
    public void addReadingOfType(Reading reading, Sensor readingType) {
    	if(null == readingsOfType) {
    		readingsOfType = new HashMap<>();
    	}
    	readingsOfType.put(readingType, reading);
    }
    
	public Map<Sensor, Reading> getReadingsByType() {
		return readingsOfType;
	}

	public void setReadingsByType(Map<Sensor, Reading> readingsByType) {
		this.readingsOfType = readingsByType;
	}*/

	public Long getId() {
		return id;
	}

	public Long getSessionId() {
		return sessionId;
	}

	public void setSessionId(Long sessionId) {
		this.sessionId = sessionId;
	}

	public void setId(Long id) {
		this.id = id;
	}

    public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<Reading> getReadings() {
		return readings;
	}

	public void addReading(Reading reading) {
		if(null == readings) {
			readings = new ArrayList<>();
		}
		readings.add(reading);
	}
	
	public void setReadings(List<Reading> readings) {
		this.readings = readings;
	}

	@Override
    public String toString() {
    	StringBuffer buffer = new StringBuffer();
    	buffer.append(this.getClass().getSimpleName());
    	buffer.append("(");
    	buffer.append(this.getId());

    	buffer.append(",");
    	buffer.append(this.getVersion());
    	buffer.append(")");
    	return buffer.toString();
    }

}

package com.obrienlabs.gps.business.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
@Table(name = "GPS_USER")
// @IdClass(IdClassRecordPK.class)
// @DiscriminatorColumn(name="DTYPE",
// discriminatorType=DiscriminatorType.STRING, length=1)
@Access(value = AccessType.FIELD)
// @Multitenant(MultitenantType.SINGLE_TABLE)
// @TenantDiscriminatorColumn(name = "TENANT", contextProperty =
// "multi-tenant.id") // not part of the PK
// http://wiki.eclipse.org/EclipseLink/Development/Indigo/Multi-Tenancy
// @NoSql
@XmlType
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class User extends DataObject {

	@Id
	@Column(name = "IDENT_ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private Long userId;

	private String name;
	private Long firstAccess;
	private Long lastAccess;

	/*@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	@JoinTable(name = "VIG_INGRED_INGRED", joinColumns = {
			@JoinColumn(name = "C_LOCALE_ID", referencedColumnName = "LOCALE_ID"),
			@JoinColumn(name = "C_RELEASE_ID", referencedColumnName = "RELEASE_ID"),
			@JoinColumn(name = "C_INGRED_CLASS_ID", referencedColumnName = "INGRED_CLASS_ID"),
			@JoinColumn(name = "C_INGRED_SEQ_ID", referencedColumnName = "INGRED_SEQ_ID") }, inverseJoinColumns = {
			@JoinColumn(name = "O_LOCALE_ID", referencedColumnName = "LOCALE_ID"),
			@JoinColumn(name = "O_RELEASE_ID", referencedColumnName = "RELEASE_ID"),
			@JoinColumn(name = "O_CLASS_ID", referencedColumnName = "INGRED_CLASS_ID"),
			@JoinColumn(name = "O_SEQ_ID", referencedColumnName = "INGRED_SEQ_ID") })
	private Set<GeoHash> geohashes;// = new ArrayList<>();*/


	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private Set<Session> sessions;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private Set<Device> devices;
	
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public void addSession(Session session) {
		if (null == sessions) {
			sessions = new HashSet<>();
		}
		sessions.add(session);
	}

	public Set<Session> getSessions() {
		return sessions;
	}

	public void setSessions(Set<Session> sessions) {
		this.sessions = sessions;
	}

	public void addDevice(Device device) {
		if (null == devices) {
			devices = new HashSet<>();
		}
		devices.add(device);
	}

	public Set<Device> getDevices() {
		return devices;
	}

	public void setDevices(Set<Device> devices) {
		this.devices = devices;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

package com.obrienlabs.gps.business.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name="READING")
@XmlRootElement
public class Reading extends DataObject {


    @Id
    @Column(name="IDENT_ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    
    
	@ManyToOne
    private Sensor sensor;
	
	@ManyToOne
	private Session session;
    
    @Basic
    private String value;
    
    @Basic
    @Column(name="tsSent")
    private Long timestampSent;
    
    @Basic
    @Column(name="tsRec")
    private Long timestampRec;
    @Basic
    @Column(name="seqNumSent")
    private Long sequenceNumberSent;
    @Basic
    @Column(name="seqNumRec")
    private Long sequenceNumberRec;

	
    
	public long getTimestampSent() {
		return timestampSent;
	}

	public void setTimestampSent(long timestampSent) {
		this.timestampSent = timestampSent;
	}

	public long getTimestampRec() {
		return timestampRec;
	}

	public void setTimestampRec(long timestampRec) {
		this.timestampRec = timestampRec;
	}

	public long getSequenceNumberSent() {
		return sequenceNumberSent;
	}

	public void setSequenceNumberSent(long sequenceNumberSent) {
		this.sequenceNumberSent = sequenceNumberSent;
	}

	public long getSequenceNumberRec() {
		return sequenceNumberRec;
	}

	public void setSequenceNumberRec(long sequenceNumberRec) {
		this.sequenceNumberRec = sequenceNumberRec;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Sensor getSensor() {
		return sensor;
	}

	public void setSensor(Sensor sensor) {
		this.sensor = sensor;
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

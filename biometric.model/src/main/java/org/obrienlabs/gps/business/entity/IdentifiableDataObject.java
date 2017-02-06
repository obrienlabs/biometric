package org.obrienlabs.gps.business.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.xml.bind.annotation.XmlElement;

@MappedSuperclass
public class IdentifiableDataObject extends DataObject {

    @Id
    @Column(name="IDENT_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)//.AUTO)
    //@GeneratedValue(strategy = GenerationType.AUTO)
        //@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="rec_id_seq")
    //@SequenceGenerator(name="rec_id_seq", sequenceName="rec_id_seq", allocationSize=50)
    //@GeneratedValue(strategy = GenerationType.AUTO)
    
    @XmlElement
    private Long id;
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
    

}

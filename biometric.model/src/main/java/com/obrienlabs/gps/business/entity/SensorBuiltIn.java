package com.obrienlabs.gps.business.entity;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
//@Table(name="SENSOR_BUILT_IN")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorValue("B")
public class SensorBuiltIn extends Sensor {
	
}

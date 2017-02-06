package org.obrienlabs.gps.business.entity;

import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

//@MappedSuperclass
@Entity
@Table(name="SENSOR")
@DiscriminatorColumn(name="DTYPE", discriminatorType=DiscriminatorType.STRING, length=1)
@Access(value=AccessType.FIELD)
@XmlRootElement
public class Sensor extends DataObject {

	// DESIGN: treat acceleration x,y,z as 3 sensors
	//@Transient
	public enum SENSORS_BUILTIN {
		//HEART_RATE,
		ACCELEROMETER_X,
		ACCELEROMETER_Y,
		ACCELEROMETER_Z,
		AMBIENT_TEMPERATURE,
		GRAVITY_X,
		GRAVITY_Y,
		GRAVITY_Z,
		GYROSCOPE_X,
		GYROSCOPE_Y,
		GYROSCOPE_Z,
		LIGHT,
		LINEAR_ACCELERATION_X,
		LINEAR_ACCELERATION_Y,
		LINEAR_ACCELERATION_Z,
		MAGNETIC_FIELD_X,
		MAGNETIC_FIELD_Y,
		MAGNETIC_FIELD_Z,
		ORIENTATION_X,
		ORIENTATION_Y,
		ORIENTATION_Z,
		PRESSURE,
		PROXIMITY,
		RELATIVE_HUMIDITY,
		ROTATION_VECTOR_X,
		ROTATION_VECTOR_Y,
		ROTATION_VECTOR_Z,
		TEMPERATURE
		
		
	
	};
	
	public enum NUMERIC_TYPES {
		FLOAT,
		DOUBLE,
		INT,
		STRING
	}
	
    
    @Id
    @Column(name="IDENT_ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long sensorId;
    
    private Boolean manual;
    
    private String label;
    
    private String type;
    
    private String unit;
    
    private String serial;
    
    /*@OneToMany(mappedBy="sensor", cascade=CascadeType.ALL)
    private List<Reading> readings;
    
    public List<Reading> getReadings() {
		return readings;
	}

	public void setReadings(List<Reading> readings) {
		this.readings = readings;
	}*/

    
	public Long getSensorId() {
		return sensorId;
	}

	public void setSensorId(Long sensorId) {
		this.sensorId = sensorId;
	}

	public String getSerial() {
		return serial;
	}

	public Boolean getManual() {
		return manual;
	}

	public void setManual(Boolean manual) {
		this.manual = manual;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	private Boolean builtIn;
    
    private Boolean dynamic;
    
	public Boolean getDynamic() {
		return dynamic;
	}

	public void setDynamic(Boolean dynamic) {
		this.dynamic = dynamic;
	}

	public Boolean getBuiltIn() {
		return builtIn;
	}

	public void setBuiltIn(Boolean builtIn) {
		this.builtIn = builtIn;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

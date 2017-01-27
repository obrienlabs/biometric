package com.obrienlabs.gps.business.entity;


import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
//import javax.persistence.Index;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
//import javax.persistence.UniqueConstraint;

//import org.hibernate.annotations.Index;

//import org.eclipse.persistence.annotations.Multitenant;
//import org.eclipse.persistence.annotations.MultitenantType;
//import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;

@Entity
@Table(name="gps_record")//,indexes={@Index(name="GPS_RECORD_INDX0", columnList="userId")})
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
public class Record extends IdentifiableDataObject {
    
    //@Id
   // private Long tenantID;
    


    @XmlElement
    @Column(name="userId", nullable=false)
    private Long userId;
    
    private String geohash;
    
    // sequence # of the record on the receiving end
    @Column(name="RECV_SEQ")
    @XmlElement
    private Long recvSeq;

    // sequence # of the record on the sending end
    @Column(name="SEND_SEQ")
    @XmlElement
    private Long sendSeq;

    // override NUMBER(19,4) default
    @Column(name="LONGITUDE",columnDefinition="DECIMAL(19,10)")
    @XmlElement 
    private Double longitude;
    
    @Column(name="LATITUDE", columnDefinition="DECIMAL(19,10)")
    @XmlElement 
    private Double lattitude;
    @XmlElement 
    private Long tsStart;
    @XmlElement 
    private Long  tsStop;
    
    @Column(name="ACCURACY", columnDefinition="DECIMAL(19,10)") // derby DOUBLE
    @XmlElement 
    private Double accuracy;
    @XmlElement 
    private Integer bearing;
    @Column(name="ALTITUDE", columnDefinition="DECIMAL(19,10)",nullable=true)
    @XmlElement 
    private Double altitude;
    @XmlElement 
    private Double temp;
    @Column(name="PRES")
    @XmlElement 
    private Double pressure;
    @XmlElement 
    private String teslaX;
    @XmlElement 
    private String teslaY;
    @XmlElement 
    private String teslaZ;
    @XmlElement 
    private Double speed;
    @XmlElement 
    private String provider;
    @Column(name="HRDEV1")
    @XmlElement 
    private String heartRateDevice1;
    @Column(name="HRDEV2")
    @XmlElement 
    private String heartRateDevice2;
    @Column(name="HEART1")
    @XmlElement 
    private Integer heartRate1;
    @Column(name="HEART2")
    @XmlElement 
    private Integer heartRate2;
    @Column(name="GRAVX")
    @XmlElement 
    private String gravityX;
    @Column(name="GRAVY")
    @XmlElement 
    private String gravityY;
    @Column(name="GRAVZ")
    @XmlElement 
    private String gravityZ;
    @Column(name="ACCELX")
    @XmlElement 
    private String accelerometerX;
    @Column(name="ACCELY")
    @XmlElement 
    private String accelerometerY;
    @Column(name="ACCELZ")
    @XmlElement 
    private String accelerometerZ;
    @Column(name="GYROX")
    @XmlElement 
    private String gyroscopeX;
    @Column(name="GYROY")
    @XmlElement 
    private String gyroscopeY;
    @Column(name="GYROZ")
    @XmlElement 
    private String gyroscopeZ;
    // 20160117: BIOM-N
    @Column(name="LIGHT")
    @XmlElement 
    private String light;
    @Column(name="LINACCX")
    @XmlElement 
    private String linearAccelerationX;
    @Column(name="LINACCY")
    @XmlElement 
    private String linearAccelerationY;
    @Column(name="LINACCZ")
    @XmlElement 
    private String linearAccelerationZ;
    @Column(name="PROX")
    @XmlElement 
    private String proximity;
    @XmlElement 
    private String humidity;
    @Column(name="ROTVECX")
    @XmlElement 
    private String rotationVectorX;
    @Column(name="ROTVECY")
    @XmlElement 
    private String rotationVectorY;
    @Column(name="ROTVECZ")
    @XmlElement 
    private String rotationVectorZ;

    
	public String getHeartRateDevice1() {
		return heartRateDevice1;
	}
	public void setHeartRateDevice1(String heartRateDevice1) {
		this.heartRateDevice1 = heartRateDevice1;
	}
	public String getHeartRateDevice2() {
		return heartRateDevice2;
	}
	public void setHeartRateDevice2(String heartRateDevice2) {
		this.heartRateDevice2 = heartRateDevice2;
	}
	public Integer getHeartRate1() {
		return heartRate1;
	}
	public void setHeartRate1(Integer heartRate1) {
		this.heartRate1 = heartRate1;
	}
	public Integer getHeartRate2() {
		return heartRate2;
	}
	public void setHeartRate2(Integer heartRate2) {
		this.heartRate2 = heartRate2;
	}
	public String getTeslaX() {
		return teslaX;
	}
	public void setTeslaX(String teslaX) {
		this.teslaX = teslaX;
	}
	public String getTeslaY() {
		return teslaY;
	}
	public void setTeslaY(String teslaY) {
		this.teslaY = teslaY;
	}
	public String getTeslaZ() {
		return teslaZ;
	}
	public void setTeslaZ(String teslaZ) {
		this.teslaZ = teslaZ;
	}
	public String getGravityX() {
		return gravityX;
	}
	public void setGravityX(String gravityX) {
		this.gravityX = gravityX;
	}
	public String getGravityY() {
		return gravityY;
	}
	public void setGravityY(String gravityY) {
		this.gravityY = gravityY;
	}
	public String getGravityZ() {
		return gravityZ;
	}
	public void setGravityZ(String gravityZ) {
		this.gravityZ = gravityZ;
	}
	public String getAccelerometerX() {
		return accelerometerX;
	}
	public void setAccelerometerX(String accelerometerX) {
		this.accelerometerX = accelerometerX;
	}
	public String getAccelerometerY() {
		return accelerometerY;
	}
	public void setAccelerometerY(String accelerometerY) {
		this.accelerometerY = accelerometerY;
	}
	public String getAccelerometerZ() {
		return accelerometerZ;
	}
	public void setAccelerometerZ(String accelerometerZ) {
		this.accelerometerZ = accelerometerZ;
	}
	public String getGyroscopeX() {
		return gyroscopeX;
	}
	public void setGyroscopeX(String gyroscopeX) {
		this.gyroscopeX = gyroscopeX;
	}
	public String getGyroscopeY() {
		return gyroscopeY;
	}
	public void setGyroscopeY(String gyroscopeY) {
		this.gyroscopeY = gyroscopeY;
	}
	public String getGyroscopeZ() {
		return gyroscopeZ;
	}
	public void setGyroscopeZ(String gyroscopeZ) {
		this.gyroscopeZ = gyroscopeZ;
	}
	public String getLinearAccelerationX() {
		return linearAccelerationX;
	}
	public void setLinearAccelerationX(String linearAccelerationX) {
		this.linearAccelerationX = linearAccelerationX;
	}
	public String getLinearAccelerationY() {
		return linearAccelerationY;
	}
	public void setLinearAccelerationY(String linearAccelerationY) {
		this.linearAccelerationY = linearAccelerationY;
	}
	public String getLinearAccelerationZ() {
		return linearAccelerationZ;
	}
	public void setLinearAccelerationZ(String linearAccelerationZ) {
		this.linearAccelerationZ = linearAccelerationZ;
	}
	public String getRotationVectorX() {
		return rotationVectorX;
	}
	public void setRotationVectorX(String rotationVectorX) {
		this.rotationVectorX = rotationVectorX;
	}
	public String getRotationVectorY() {
		return rotationVectorY;
	}
	public void setRotationVectorY(String rotationVectorY) {
		this.rotationVectorY = rotationVectorY;
	}
	public String getRotationVectorZ() {
		return rotationVectorZ;
	}
	public void setRotationVectorZ(String rotationVectorZ) {
		this.rotationVectorZ = rotationVectorZ;
	}
	public String getLight() {
		return light;
	}
	public void setLight(String light) {
		this.light = light;
	}

	public String getProximity() {
		return proximity;
	}
	public void setProximity(String proximity) {
		this.proximity = proximity;
	}
	public String getHumidity() {
		return humidity;
	}
	public void setHumidity(String humidity) {
		this.humidity = humidity;
	}

	public Double getSpeed() {
		return speed;
	}
	public void setSpeed(Double speed) {
		this.speed = speed;
	}
	public String getProvider() {
		return provider;
	}
	public void setProvider(String provider) {
		this.provider = provider;
	}

	public Long getTsStart() {
		return tsStart;
	}
	public void setTsStart(Long tsStart) {
		this.tsStart = tsStart;
	}
	public Long getTsStop() {
		return tsStop;
	}
	public void setTsStop(Long tsStop) {
		this.tsStop = tsStop;
	}
	public Integer getBearing() {
		return bearing;
	}
	public void setBearing(Integer bearing) {
		this.bearing = bearing;
	}
	public Double getAltitude() {
		return altitude;
	}
	public void setAltitude(Double altitude) {
		this.altitude = altitude;
	}
	public Double getTemp() {
		return temp;
	}
	public void setTemp(Double temp) {
		this.temp = temp;
	}
	public Double getPressure() {
		return pressure;
	}
	public void setPressure(Double pressure) {
		this.pressure = pressure;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public Double getLattitude() {
		return lattitude;
	}
	public void setLattitude(Double lattitude) {
		this.lattitude = lattitude;
	}
	public Double getAccuracy() {
		return accuracy;
	}
	public void setAccuracy(Double accuracy) {
		this.accuracy = accuracy;
	}

    
    public Long getRecvSeq() {
		return recvSeq;
	}
	public void setRecvSeq(Long recvSeq) {
		this.recvSeq = recvSeq;
	}
	public Long getSendSeq() {
		return sendSeq;
	}
	public void setSendSeq(Long sendSeq) {
		this.sendSeq = sendSeq;
	}

	
	public String getGeohash() {
		return geohash;
	}
	public void setGeohash(String geohash) {
		this.geohash = geohash;
	}
	
	@Override
    public String toString() {
    	StringBuffer buffer = new StringBuffer();
    	buffer.append(this.getClass().getSimpleName());
    	buffer.append("(");
    	buffer.append(this.getId());
    	buffer.append(",");
    	buffer.append(this.getUserId());
    	buffer.append(",");
    	buffer.append(this.getSendSeq());
    	buffer.append(",");
    	buffer.append(this.getRecvSeq());
    	buffer.append(",");
    	buffer.append(this.getHeartRate1());
    	buffer.append(",");
    	buffer.append(this.getHeartRate2());
    	buffer.append(",");
    	buffer.append(this.getLattitude());
    	buffer.append(",");
    	buffer.append(this.getLongitude());
    	buffer.append(",");
    	buffer.append(this.getBearing());
    	buffer.append(",");
    	buffer.append(this.getAltitude());
    	buffer.append(",");
    	buffer.append(this.getTsStart());
    	buffer.append(",");
    	buffer.append(this.getTsStop());
    	buffer.append(",");
    	buffer.append(this.getPressure());
    	buffer.append(")");
    	return buffer.toString();
    }

}


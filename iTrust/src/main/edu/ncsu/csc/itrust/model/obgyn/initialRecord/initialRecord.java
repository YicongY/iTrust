package edu.ncsu.csc.itrust.model.obgyn.initialRecord;

import javax.faces.bean.ManagedBean;
import java.sql.Date;
/**
 * @author Yicong
 *OB Record data object
 */
@ManagedBean(name="initialRecord")
public class initialRecord {
	private long obRecordID;
	private int yearOfConception;
	private float numberOfHoursInLabor;
	private String numberOfWeeksPregnant;
	private float weightGainDuringPregnancy;
	private String DeliveryType;
	private int pregnancyType;
	private Date LMP;
	private Date EDD;
	private long MID;
	private Date recordDate;
	public Date getRecordDate() {
		return recordDate;
	}
	public void setRecordDate(Date recordDate) {
		this.recordDate = recordDate;
	}
	public long getObRecordID() {
		return obRecordID;
	}
	public void setObRecordID(long obRecordID) {
		this.obRecordID = obRecordID;
	}
	public int getYearOfConception() {
		return yearOfConception;
	}
	public void setYearOfConception(int yearOfConception) {
		this.yearOfConception = yearOfConception;
	}
	public float getNumberOfHoursInLabor() {
		return numberOfHoursInLabor;
	}
	public void setNumberOfHoursInLabor(float numberOfHoursInLabor) {
		this.numberOfHoursInLabor = numberOfHoursInLabor;
	}
	public String getNumberOfWeeksPregnant() {
		return numberOfWeeksPregnant;
	}
	public void setNumberOfWeeksPregnant(String numberOfWeeksPregnant) {
		this.numberOfWeeksPregnant = numberOfWeeksPregnant;
	}
	public float getWeightGainDuringPregnancy() {
		return weightGainDuringPregnancy;
	}
	public void setWeightGainDuringPregnancy(float weightGainDuringPregnancy) {
		this.weightGainDuringPregnancy = weightGainDuringPregnancy;
	}
	public String getDeliveryType() {
		return DeliveryType;
	}
	public void setDeliveryType(String deliveryType) {
		DeliveryType = deliveryType;
	}
	public int getPregnancyType() {
		return pregnancyType;
	}
	public void setPregnancyType(int pregnancyType) {
		this.pregnancyType = pregnancyType;
	}
	public Date getLMP() {
		return LMP;
	}
	public void setLMP(Date lMP) {
		LMP = lMP;
	}
	public Date getEDD() {
		return EDD;
	}
	public void setEDD(Date eDD) {
		EDD = eDD;
	}
	public long getMID() {
		return MID;
	}
	public void setMID(long mID) {
		MID = mID;
	}

}
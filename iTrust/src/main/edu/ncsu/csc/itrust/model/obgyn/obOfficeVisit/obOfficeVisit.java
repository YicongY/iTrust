package edu.ncsu.csc.itrust.model.obgyn.obOfficeVisit;

import java.time.LocalDate;

import javax.faces.bean.ManagedBean;
/**
 * @author Shu
 *OB Office Visit data object
 */
@ManagedBean(name="obOfficeVisit")
public class obOfficeVisit {
    private long visiteId;
    private long patientMID;
    private String locationID;
    private int apptTypeID;
    private LocalDate currentDate;
    private String numOfWeeks;
    private float weight;
    private String bloodPressure;
    private int fetalHeartRate;
    private boolean isMultiplePregnancy;
    private int numOfMultiplePregnancy;
    private boolean isLowLyingPlacentaObserved;

    public long getVisiteId() {
        return visiteId;
    }

    public long getPatientMID() {
        return patientMID;
    }

    public String getLocationID() {
        return locationID;
    }

    public int getApptTypeID() {
        return apptTypeID;
    }

    public LocalDate getCurrentDate() {
        return currentDate;
    }

    public String getNumOfWeeks() {
        return numOfWeeks;
    }

    public float getWeight() {
        return weight;
    }

    public String getBloodPressure() {
        return bloodPressure;
    }

    public int getFetalHeartRate() {
        return fetalHeartRate;
    }

    public boolean isMultiplePregnancy() {
        return isMultiplePregnancy;
    }

    public int getNumOfMultiplePregnancy() {
        return numOfMultiplePregnancy;
    }

    public boolean isLowLyingPlacentaObserved() {
        return isLowLyingPlacentaObserved;
    }

    public void setVisiteId(long visiteId) {
        this.visiteId = visiteId;
    }

    public void setPatientMID(long patientMID) {
        this.patientMID = patientMID;
    }

    public void setLocationID(String locationID) {
        this.locationID = locationID;
    }

    public void setApptTypeID(int apptTypeID) {
        this.apptTypeID = apptTypeID;
    }

    public void setCurrentDate(LocalDate currentDate) {
        this.currentDate = currentDate;
    }

    public void setNumOfWeeks(String numOfWeeks) {
        this.numOfWeeks = numOfWeeks;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public void setBloodPressure(String bloodPressure) {
        this.bloodPressure = bloodPressure;
    }

    public void setFetalHeartRate(int fetalHeartRate) {
        this.fetalHeartRate = fetalHeartRate;
    }

    public void setMultiplePregnancy(boolean multiplePregnancy) {
        isMultiplePregnancy = multiplePregnancy;
    }

    public void setNumOfMultiplePregnancy(int numOfMultiplePregnancy) {
        this.numOfMultiplePregnancy = numOfMultiplePregnancy;
    }

    public void setLowLyingPlacentaObserved(boolean lowLyingPlacentaObserved) {
        isLowLyingPlacentaObserved = lowLyingPlacentaObserved;
    }
}

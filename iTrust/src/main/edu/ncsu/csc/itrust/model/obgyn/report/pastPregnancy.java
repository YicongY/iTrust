package edu.ncsu.csc.itrust.model.obgyn.report;

/**
 *@author Shu
 */
public class pastPregnancy {
    int pregnancyTerm;
    int conceptionYear;
    String deliveryType;
    int numberOfHoursInLabor;
    int weightGain;
    int pregnancyType;
    long patientMID;

    public long getPatientMID() {
        return patientMID;
    }

    public void setPatientMID(long patientMID) {
        this.patientMID = patientMID;
    }

    public int getPregnancyTerm() {
        return pregnancyTerm;
    }

    public int getConceptionYear() {
        return conceptionYear;
    }

    public String getDeliveryType() {
        return deliveryType;
    }

    public int getNumberOfHoursInLabor() {
        return numberOfHoursInLabor;
    }


    public void setNumberOfHoursInLabor(int numberOfHoursInLabor) {
        this.numberOfHoursInLabor = numberOfHoursInLabor;
    }

    public int getWeightGain() {
        return weightGain;
    }

    public void setWeightGain(int weightGain) {
        this.weightGain = weightGain;
    }

    public int getPregnancyType() {
        return pregnancyType;
    }

    public void setPregnancyType(int pregnancyType) {
        this.pregnancyType = pregnancyType;
    }

    public void setPregnancyTerm(int pregnancyTerm) {
        this.pregnancyTerm = pregnancyTerm;
    }

    public void setConceptionYear(int conceptionYear) {
        this.conceptionYear = conceptionYear;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }
}
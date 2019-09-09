package edu.ncsu.csc.itrust.model.obgyn.childBirthVisit;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.old.beans.ApptBean;

import java.sql.Time;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

/**
 * @ClassName:    ChildBirthVisit
 * @Description:  ChildBirthVisit class, including private fields and getter and setter for each fields
 * @Author:       Xiaocong Yu
 **/
public class ChildBirthVisit {
    private Long childBirthVisitID;
    private Long patientMID;
    private String preferredDeliveryType;
    private String visitType;
    private String deliveryType;
    private Date deliveryDate;
    private Time deliveryTime;
    private int numberOfGirlBabies;
    private int numberOfBoyBabies;
    private int dosage_pitocin;
    private int dosage_nitrousOxide;
    private int dosage_pethidine;
    private int dosage_epiduralAnaesthesia;
    private int dosage_magnesiumSulfate;
    private int dosage_rhImmuneGlobulin;
    private String comment;

    public ChildBirthVisit(){}
    public Long getChildBirthVisitID() {
        return childBirthVisitID;
    }
    public void setChildBirthVisitID(Long childBirthVisitID) {
        this.childBirthVisitID = childBirthVisitID;
    }
    public Long getPatientMID() {
        return patientMID;
    }
    public void setPatientMID(Long patientMID) {
        this.patientMID = patientMID;
    }
    public String getPreferredDeliveryType() {
        return preferredDeliveryType;
    }
    public void setPreferredDeliveryType(String preferredDeliveryType) {
        this.preferredDeliveryType = preferredDeliveryType;
    }
    public String getVisitType() {
        return visitType;
    }
    public void setVisitType(String visitType) {
        this.visitType = visitType;
    }
    public String getDeliveryType() {
        return deliveryType;
    }
    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }
    public Date getDeliveryDate() {
        return deliveryDate;
    }
    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }
    public Time getDeliveryTime() {
        return deliveryTime;
    }
    public void setDeliveryTime(Time deliveryTime) {
        this.deliveryTime = deliveryTime;
    }
    public int getNumberOfGirlBabies() {
        return numberOfGirlBabies;
    }
    public void setNumberOfGirlBabies(int numberOfGirlBabies) {
        this.numberOfGirlBabies = numberOfGirlBabies;
    }
    public int getNumberOfBoyBabies() {
        return numberOfBoyBabies;
    }
    public void setNumberOfBoyBabies(int numberOfBoyBabies) {
        this.numberOfBoyBabies = numberOfBoyBabies;
    }
    public int getDosage_pitocin() {
        return dosage_pitocin;
    }
    public void setDosage_pitocin(int dosage_pitocin) {
        this.dosage_pitocin = dosage_pitocin;
    }
    public int getDosage_nitrousOxide() {
        return dosage_nitrousOxide;
    }
    public int getDosage_pethidine() {
        return dosage_pethidine;
    }
    public void setDosage_pethidine(int dosage_pethidine) {
        this.dosage_pethidine = dosage_pethidine;
    }
    public int getDosage_epiduralAnaesthesia() {
        return dosage_epiduralAnaesthesia;
    }
    public void setDosage_epiduralAnaesthesia(int dosage_epiduralAnaesthesia) {
        this.dosage_epiduralAnaesthesia = dosage_epiduralAnaesthesia;
    }
    public int getDosage_magnesiumSulfate() {
        return dosage_magnesiumSulfate;
    }
    public void setDosage_magnesiumSulfate(int dosage_magnesiumSulfate) {
        this.dosage_magnesiumSulfate = dosage_magnesiumSulfate;
    }
    public int getDosage_rhImmuneGlobulin() {
        return dosage_rhImmuneGlobulin;
    }
    public void setDosage_rhImmuneGlobulin(int dosage_rhImmuneGlobulin) {
        this.dosage_rhImmuneGlobulin = dosage_rhImmuneGlobulin;
    }
    public void setDosage_nitrousOxide(int dosage_nitrousOxide) {
        this.dosage_nitrousOxide = dosage_nitrousOxide;
    }
    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
}


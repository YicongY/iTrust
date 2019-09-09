package edu.ncsu.csc.itrust.model.obgyn.report;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *@author Shu
 */
public class report {
    String bloodType;
    LocalDate EDD;
    long patientMID;
    List<pastPregnancy> pastPregnancyList;
    List<obVisitInfo> obVisitInfoList;

    public String getBloodType() {
        return bloodType;
    }

    public LocalDate getEDD() {
        return EDD;
    }

    public long getPatientMID() {
        return patientMID;
    }

    public void setPatientMID(long patientMID) {
        this.patientMID = patientMID;
    }


    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public void setEDD(LocalDate EDD) {
        this.EDD = EDD;
    }

    public List<pastPregnancy> getPastPregnancyList() {
        return pastPregnancyList;
    }

    public List<obVisitInfo> getObVisitInfoList() {
        return obVisitInfoList;
    }

    public void setPastPregnancyList(List<pastPregnancy> pastPregnancyList) {
        this.pastPregnancyList = pastPregnancyList;
    }

    public void setObVisitInfoList(List<obVisitInfo> obVisitInfoList) {
        this.obVisitInfoList = obVisitInfoList;
    }
}

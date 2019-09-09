package edu.ncsu.csc.itrust.model.obgyn.report;

import java.time.LocalDate;

/**
 *@author Shu
 */
public class obVisitInfo {
    boolean RHFlag;
    boolean highBloodPressure;
    boolean advancedMaternalAge;
    boolean lowLyingPlacenta;
    boolean abnormalFetalHeartRate;
    boolean isMultiplePregnancy;
    boolean atypicalWeightChange;
    boolean hyperemesisGravidarum;
    boolean hypothyroidism;
    boolean diabetes;
    boolean autoimmuneDisorders;
    boolean cancers;
    boolean STDs;
    boolean penicillin;
    boolean sulfaDrugs;
    boolean tetracycline;
    boolean codeine;
    boolean NSAIDs;
    long patientMID;
    String numOfWeeksPregnant;
    float weight;
    String bloodPressure;
    int fetalHeartRate;
    boolean isMultiple;
    int numOfMultiples;
    boolean isLowLyingPlacentaObserved;
    boolean complications;
    boolean preExistingConditions;
    boolean drugAllergies;

    public String getNumOfWeeksPregnant() {
        return numOfWeeksPregnant;
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

    public boolean isMultiple() {
        return isMultiple;
    }

    public int getNumOfMultiples() {
        return numOfMultiples;
    }

    public boolean isLowLyingPlacentaObserved() {
        return isLowLyingPlacentaObserved;
    }

    public boolean isComplications() {
        return complications;
    }

    public boolean isRHFlag() {
        return RHFlag;
    }

    public boolean isHighBloodPressure() {
        return highBloodPressure;
    }

    public boolean isAdvancedMaternalAge() {
        return advancedMaternalAge;
    }

    public boolean isLowLyingPlacenta() {
        return lowLyingPlacenta;
    }

    public boolean isAbnormalFetalHeartRate() {
        return abnormalFetalHeartRate;
    }

    public boolean isMultiplePregnancy() {
        return isMultiplePregnancy;
    }

    public boolean isAtypicalWeightChange() {
        return atypicalWeightChange;
    }

    public boolean isHyperemesisGravidarum() {
        return hyperemesisGravidarum;
    }

    public void setHyperemesisGravidarum(boolean hyperemesisGravidarum) {
        this.hyperemesisGravidarum = hyperemesisGravidarum;
    }

    public boolean isHypothyroidism() {
        return hypothyroidism;
    }

    public void setHypothyroidism(boolean hypothyroidism) {
        this.hypothyroidism = hypothyroidism;
    }

    public boolean isDiabetes() {
        return diabetes;
    }

    public void setDiabetes(boolean diabetes) {
        this.diabetes = diabetes;
    }

    public boolean isAutoimmuneDisorders() {
        return autoimmuneDisorders;
    }

    public void setAutoimmuneDisorders(boolean autoimmuneDisorders) {
        this.autoimmuneDisorders = autoimmuneDisorders;
    }

    public boolean isCancers() {
        return cancers;
    }

    public void setCancers(boolean cancers) {
        this.cancers = cancers;
    }

    public boolean isSTDs() {
        return STDs;
    }

    public void setSTDs(boolean STDs) {
        this.STDs = STDs;
    }

    public boolean isPenicillin() {
        return penicillin;
    }

    public void setPenicillin(boolean penicillin) {
        this.penicillin = penicillin;
    }

    public boolean isSulfaDrugs() {
        return sulfaDrugs;
    }

    public void setSulfaDrugs(boolean sulfaDrugs) {
        this.sulfaDrugs = sulfaDrugs;
    }

    public boolean isTetracycline() {
        return tetracycline;
    }

    public void setTetracycline(boolean tetracycline) {
        this.tetracycline = tetracycline;
    }

    public boolean isCodeine() {
        return codeine;
    }

    public void setCodeine(boolean codeine) {
        this.codeine = codeine;
    }

    public boolean isNSAIDs() {
        return NSAIDs;
    }

    public void setNSAIDs(boolean NSAIDs) {
        this.NSAIDs = NSAIDs;
    }

    public void setRHFlag(String bloodType) {
        if (bloodType.charAt(bloodType.length() - 1) == '-') {
            this.RHFlag = true;
        } else {
            this.RHFlag = false;
        }
    }

    public void setHighBloodPressure(String bloodPressure) {
        String[] splited = bloodPressure.split("/");
        if (Integer.parseInt(splited[0]) >= 140 || Integer.parseInt(splited[1]) >= 90) {
            this.highBloodPressure = true;
        } else {
            this.highBloodPressure = false;
        }
    }

    public void setAdvancedMaternalAge(LocalDate dateOfBirth) {
        if (LocalDate.now().getYear() - dateOfBirth.getYear() >= 35) {
            this.advancedMaternalAge = true;
        } else {
            this.advancedMaternalAge = false;
        }
    }

    public void setLowLyingPlacenta(boolean lowLyingPlacenta) {
        this.lowLyingPlacenta = lowLyingPlacenta;
    }

    public void setAbnormalFetalHeartRate(int fetalHeartRate) {
        if (fetalHeartRate < 120 || fetalHeartRate > 160) {
            this.abnormalFetalHeartRate = true;
        } else {
            this.abnormalFetalHeartRate = false;
        }
    }

    public void setMultiplePregnancy(boolean multiplePregnancy) {
        isMultiplePregnancy = multiplePregnancy;
    }

    public void setAtypicalWeightChange(float startWeight, float endWeight) {
        float weightGain = endWeight - startWeight;
        if (weightGain < 15 || weightGain > 35) {
            this.atypicalWeightChange = true;
        } else {
            this.atypicalWeightChange = false;
        }
    }

    public long getPatientMID() {
        return patientMID;
    }

    public void setPatientMID(long patientMID) {
        this.patientMID = patientMID;
    }

    public void setNumOfWeeksPregnant(String numOfWeeksPregnant) {
        this.numOfWeeksPregnant = numOfWeeksPregnant;
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

    public void setMultiple(boolean multiple) {
        isMultiple = multiple;
    }

    public void setNumOfMultiples(int numOfMultiples) {
        this.numOfMultiples = numOfMultiples;
    }

    public void setLowLyingPlacentaObserved(boolean lowLyingPlacentaObserved) {
        this.isLowLyingPlacentaObserved = lowLyingPlacentaObserved;
    }

    public void setComplications() {
        boolean RHFlag = this.isRHFlag();
        boolean highBloodPressure = this.isHighBloodPressure();
        boolean advancedMaternalAge = this.isAdvancedMaternalAge();
        boolean lowLyingPlacenta = this.isLowLyingPlacentaObserved();
        boolean abnormalFetalHeartRate = this.isAbnormalFetalHeartRate();
        boolean atypicalWeightChange = this.isAtypicalWeightChange();
        boolean hyperemesisGravidarum = this.isHyperemesisGravidarum();
        boolean hypothyroidism = this.isHypothyroidism();
        boolean diabetes = this.isDiabetes();
        boolean autoimmuneDisorders = this.isAutoimmuneDisorders();
        boolean cancers = this.isCancers();
        boolean STDs = this.isSTDs();
        boolean penicillin = this.isPenicillin();
        boolean sulfaDrugs = this.isSulfaDrugs();
        boolean tetracycline = this.isTetracycline();
        boolean codeine = this.isCodeine();
        boolean NSAIDs = this.isNSAIDs();
        boolean isLowLyingPlacentaObserved = this.isLowLyingPlacentaObserved();
        boolean complications = RHFlag || highBloodPressure || advancedMaternalAge ||
                                lowLyingPlacenta || abnormalFetalHeartRate || atypicalWeightChange ||
                                hyperemesisGravidarum || hypothyroidism || diabetes || autoimmuneDisorders ||
                                cancers || STDs || penicillin || sulfaDrugs || tetracycline || codeine ||
                                NSAIDs || isLowLyingPlacentaObserved;
        this.complications = complications;
    }

    public boolean getComplications() {
        return this.complications;
    }

    public void setPreExistingConditions() {
        boolean diabetes = this.isDiabetes();
        boolean autoimmuneDisorders = this.isAutoimmuneDisorders();
        boolean cancers = this.isCancers();
        boolean STDs = this.isSTDs();
        this.preExistingConditions = diabetes || autoimmuneDisorders || cancers || STDs;
    }

    public boolean getPreExistingConditions() {
        return this.preExistingConditions;
    }

    public void setDrugAllergies() {
        boolean penicillin = this.isPenicillin();
        boolean sulfaDrugs = this.isSulfaDrugs();
        boolean tetracycline = this.isTetracycline();
        boolean codeine = this.isCodeine();
        boolean NSAIDs = this.isNSAIDs();
        this.drugAllergies = penicillin || sulfaDrugs || tetracycline || codeine || NSAIDs;
    }

    public boolean getDrugAllergies() {
        return this.drugAllergies;
    }
}
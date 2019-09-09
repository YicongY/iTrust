package edu.ncsu.csc.itrust.model.ratings;

import java.math.BigInteger;

public class Rating {
    private Float punctuality;
    private Float attitude;
    private Float skillfulness;
    private Float knowledge;
    private Float efficiency;
    private Integer appt_id;
    private Long patient_id;
    private Long doctor_id;
    private String comment;

    public Float getAttitude() {
        return attitude;
    }

    public void setAttitude(Float attitude) {
        this.attitude = attitude;
    }

    public Float getSkillfulness() {
        return skillfulness;
    }

    public void setSkillfulness(Float skillfulness) {
        this.skillfulness = skillfulness;
    }

    public Float getKnowledge() {
        return knowledge;
    }

    public void setKnowledge(Float knowledge) {
        this.knowledge = knowledge;
    }

    public Float getEfficiency() {
        return efficiency;
    }

    public void setEfficiency(Float efficiency) {
        this.efficiency = efficiency;
    }

    public Integer getAppt_id() {
        return appt_id;
    }

    public void setAppt_id(Integer appt_id) {
        this.appt_id = appt_id;
    }

    public Long getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(Long patient_id) {
        this.patient_id = patient_id;
    }

    public Long getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(Long doctor_id) {
        this.doctor_id = doctor_id;
    }

    public Float getPunctuality() {
        return punctuality;
    }

    public void setPunctuality(Float punctuality) {
        this.punctuality = punctuality;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }
}

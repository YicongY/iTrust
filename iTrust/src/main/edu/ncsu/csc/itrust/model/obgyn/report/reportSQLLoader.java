package edu.ncsu.csc.itrust.model.obgyn.report;

import edu.ncsu.csc.itrust.model.SQLLoader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *@author Shu
 */
public class reportSQLLoader implements SQLLoader<report> {
    @Override
    public List<report> loadList(ResultSet rs) throws SQLException {
        return null;
    }

    @Override
    public PreparedStatement loadParameters(Connection conn, PreparedStatement ps, report insertObject, boolean newInstance) throws SQLException {
        return null;
    }

    @Override
    public report loadSingle(ResultSet rs) throws SQLException {
        return null;
    }

    public report loadSingleReport(ResultSet rs) throws SQLException {
        report newReport = new report();
        newReport.setBloodType(rs.getString("bloodType"));
        newReport.setEDD(rs.getDate("EDD").toLocalDate());
        newReport.setPatientMID(rs.getLong("patientMID"));
        return newReport;
    }

    public List<report> loadReportList(ResultSet rs) throws SQLException {
        List<report> list = new ArrayList<>();
        while (rs.next()) {
            list.add(loadSingleReport(rs));
        }
        return list;
    }

    public pastPregnancy loadSinglePastPregnancy(ResultSet rs) throws SQLException {
        pastPregnancy pp = new pastPregnancy();
        pp.setPregnancyTerm(rs.getInt("pregnancyTerm"));
        pp.setConceptionYear(rs.getInt("conceptionYear"));
        pp.setDeliveryType(rs.getString("deliveryType"));
        return pp;
    }

    public List<pastPregnancy> loadPastPregnancyList(ResultSet rs) throws SQLException {
        List<pastPregnancy> list = new ArrayList<>();
        while (rs.next()) {
            list.add(loadSinglePastPregnancy(rs));
        }
        return list;
    }

    public obVisitInfo loadSingleObVisitInfo(ResultSet rs) throws SQLException {
        obVisitInfo obv = new obVisitInfo();
        obv.setRHFlag(rs.getString("bloodType"));
        obv.setHighBloodPressure(rs.getString("bloodPressure"));
        obv.setAdvancedMaternalAge(rs.getDate("dateOfBirth").toLocalDate());
        obv.setLowLyingPlacenta(rs.getBoolean("lowLyingPlacenta"));
        obv.setAbnormalFetalHeartRate(rs.getInt("fetalHeartRate"));
        obv.setMultiplePregnancy(rs.getBoolean("multiplePregnancy"));
        obv.setAtypicalWeightChange(rs.getFloat("startWeight"), rs.getFloat("endWeight"));
        obv.setHypothyroidism(rs.getBoolean("hyperemesisGravidarum"));
        obv.setHyperemesisGravidarum(rs.getBoolean("hypothyroidism"));
        obv.setDiabetes(rs.getBoolean("diabetes"));
        obv.setAutoimmuneDisorders(rs.getBoolean("autoimmuneDisorders"));
        obv.setCancers(rs.getBoolean("cancers"));
        obv.setSTDs(rs.getBoolean("STDs"));
        obv.setPenicillin(rs.getBoolean("penicillin"));
        obv.setSulfaDrugs(rs.getBoolean("sulfaDrugs"));
        obv.setTetracycline(rs.getBoolean("tetracycline"));
        obv.setCodeine(rs.getBoolean("codeine"));
        obv.setNSAIDs(rs.getBoolean("NSAIDs"));
        obv.setNumOfWeeksPregnant(rs.getString("numOfWeeksPregnant"));
        obv.setWeight(rs.getFloat("weight"));
        obv.setBloodPressure(rs.getString("bloodPressure"));
        obv.setFetalHeartRate(rs.getInt("fetalHeartRate"));
        obv.setMultiple(rs.getBoolean("multiplePregnancy"));
        obv.setNumOfMultiples(rs.getInt("numOfMultiples"));
        obv.setLowLyingPlacenta(rs.getBoolean("lowLyingPlacenta"));
        obv.setComplications();
        obv.setPreExistingConditions();
        obv.setDrugAllergies();
        return obv;
    }


    public List<obVisitInfo> loadPObVisitInfoList(ResultSet rs) throws SQLException {
        List<obVisitInfo> list = new ArrayList<>();
        while (rs.next()) {
            list.add(loadSingleObVisitInfo(rs));
        }
        return list;
    }
}

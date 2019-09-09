package edu.ncsu.csc.itrust.model.obgyn.obOfficeVisit;

import com.mysql.jdbc.Statement;
import edu.ncsu.csc.itrust.model.SQLLoader;
import edu.ncsu.csc.itrust.model.obgyn.initialRecord.initialRecord;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class obOfficeVisitSQLLoader implements SQLLoader<obOfficeVisit> {
    @Override
    public List<obOfficeVisit> loadList(ResultSet rs) throws SQLException {
        ArrayList<obOfficeVisit> list = new ArrayList<obOfficeVisit>();
        while (rs.next()) {
            list.add(loadSingle(rs));
        }
        return list;
    }

    @Override
    public obOfficeVisit loadSingle(ResultSet rs) throws SQLException {
        obOfficeVisit retOBVisit = new obOfficeVisit();

        retOBVisit.setApptTypeID(rs.getInt("apptTypeId"));
        retOBVisit.setBloodPressure(rs.getString("bloodPressure"));
        retOBVisit.setCurrentDate(rs.getDate("currentDate").toLocalDate());
        retOBVisit.setFetalHeartRate(rs.getInt("fetalHeartRate"));
        retOBVisit.setLocationID(rs.getString("locationID"));
        retOBVisit.setLowLyingPlacentaObserved(rs.getBoolean("isLowLyingPlacentaObserved"));
        retOBVisit.setMultiplePregnancy(rs.getBoolean("isMultiplePregnancy"));
        retOBVisit.setNumOfWeeks(rs.getString("numOfWeeks"));
        retOBVisit.setPatientMID(rs.getLong("patientMID"));
        retOBVisit.setVisiteId(rs.getLong("visitId"));
        retOBVisit.setWeight(rs.getFloat("weight"));
        retOBVisit.setNumOfMultiplePregnancy(rs.getInt("numOfMultiplePregnancy"));

        return retOBVisit;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public PreparedStatement loadParameters(Connection conn, PreparedStatement ps, obOfficeVisit obv, boolean newInstance) throws SQLException  {
        String stmt = "";
        if (newInstance) {
            stmt = "INSERT INTO OBOfficeVisit (patientMID, locationID, apptTypeID, currentDate, numOfWeeks, weight, bloodPressure, fetalHeartRate, isMultiplePregnancy, numOfMultiplePregnancy, isLowLyingPlacentaObserved)"
                    + "VALUES(?,?,?,?,?,?,?,?,?,?,?);";
        } else {
            long id = obv.getVisiteId();
            stmt = "UPDATE OBOfficeVisit SET patientMID=?, "
                    + "locationID=?, "
                    + "apptTypeID=?, "
                    + "currentDate=?, "
                    + "numOfWeeks=?, "
                    + "weight=?, "
                    + "bloodPressure=?, "
                    + "fetalHeartRate=?, "
                    + "isMultiplePregnancy=?, "
                    + "numOfMultiplePregnancy=?, "
                    + "isLowLyingPlacentaObserved=? "
                    + "WHERE visitID=" + id + ";";
        }

        ps = conn.prepareStatement(stmt, Statement.RETURN_GENERATED_KEYS);
        ps.setLong(1, obv.getPatientMID());
        ps.setString(2, obv.getLocationID());
        ps.setInt(3, obv.getApptTypeID());
        ps.setDate(4, java.sql.Date.valueOf(obv.getCurrentDate()));
        ps.setString(5, obv.getNumOfWeeks());
        ps.setFloat(6, obv.getWeight());
        ps.setString(7, obv.getBloodPressure());
        ps.setInt(8, obv.getFetalHeartRate());
        ps.setBoolean(9, obv.isMultiplePregnancy());
        ps.setInt(10, obv.getNumOfMultiplePregnancy());
        ps.setBoolean(11, obv.isLowLyingPlacentaObserved());
        return ps;

    }
}


package edu.ncsu.csc.itrust.model.obgyn.report;

import edu.ncsu.csc.itrust.DBUtil;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.ValidationFormat;
import edu.ncsu.csc.itrust.model.old.validate.ApptBeanValidator;

import javax.annotation.Resource;
import javax.faces.bean.ManagedBean;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *@author ziyu
 */
@ManagedBean
public class reportMySQL implements Serializable, reportData {
    private static final long serialVersionUID = 1389654351629546245L;
    @Resource(name = "jdbc/itrust2")
    private reportSQLLoader loader;
    private DataSource ds;
    private reportValidator validator;
    private ApptBeanValidator AppValidator = new ApptBeanValidator();

    /**
     * Default Constructor
     *
     * @throws DBException
     */
    public reportMySQL() throws DBException {
        loader = new reportSQLLoader();
        try {
            Context ctx = new InitialContext();
            this.ds = ((DataSource) (((Context) ctx.lookup("java:comp/env"))).lookup("jdbc/itrust"));
        } catch (NamingException e) {
            throw new DBException(new SQLException("Context Lookup Naming Exception: " + e.getMessage()));
        }
        validator = new reportValidator(this.ds);
    }

    /**
     * Constructor for testing.
     *
     * @param ds
     */
    public reportMySQL(DataSource ds) throws DBException{
        loader = new reportSQLLoader();
        this.ds = ds;
        validator = new reportValidator(this.ds);
    }

    /**
     * {@inheritDoc}
     */
    public List<report> getAll() throws DBException {
        return null;
    }

    /**
     * Get a report record for a patient.
     * A patient will have only one report.
     *
     * {@inheritDoc}
     */
    public report getByID(long patientMID) throws DBException {
        Connection conn = null;
        PreparedStatement pstring = null;
        ResultSet results = null;

        if (ValidationFormat.MID.getRegex().matcher(Long.toString(patientMID)).matches()) {
            try {
                conn = ds.getConnection();

                // For EDD and blood type
                pstring = conn.prepareStatement(
                        "SELECT p.MID AS patientMID, EDD, BloodType AS bloodType "
                                + "FROM initobrecord AS init, patients AS p "
                                + "WHERE init.MID = p.MID AND p.MID = ? "
                                + "AND init.obrecordID = (SELECT MAX(obrecordID) FROM initobrecord WHERE MID = ?);"
                );
                pstring.setLong(1, patientMID);
                pstring.setLong(2, patientMID);
                results = pstring.executeQuery();

                List<report> resultsList= loader.loadReportList(results);
                if (resultsList.size() == 0) {
                    return null;
                } else {
                    report resultReport = resultsList.get(0);
                    results.close();
                    pstring.close();

                    // For all past pregnancies
                    pstring = conn.prepareStatement(
                            "SELECT pregnancyterm AS pregnancyTerm, yearofconception AS conceptionYear, deliverytype AS deliveryType "
                                    + "FROM pregnancyRecord WHERE patientMID = ?;"
                    );
                    pstring.setLong(1, patientMID);
                    results = pstring.executeQuery();
                    List<pastPregnancy> pastPregList = loader.loadPastPregnancyList(results);
                    resultReport.setPastPregnancyList(pastPregList);
                    results.close();
                    pstring.close();

                    // For OB office visit info
                    pstring = conn.prepareStatement(
                            "SELECT DISTINCT OBOfficeVisit.visitID,"
                                    + "BloodType AS bloodType, "
                                    + "numOfWeeks AS numOfWeeksPregnant, "
                                    + "weight, bloodPressure, DateOfBirth AS dateOfBirth, isLowLyingPlacentaObserved AS lowLyingPlacenta, "
                                    + "fetalHeartRate, isMultiplePregnancy AS multiplePregnancy, numOfMultiplePregnancy AS numOfMultiples, "
                                    + "(SELECT weight FROM OBOfficeVisit WHERE visitID = (SELECT MIN(visitID) FROM OBOfficeVisit WHERE patientMID = ?)) AS startWeight, "
                                    + "(SELECT weight FROM OBOfficeVisit WHERE visitID = (SELECT MAX(visitID) FROM OBOfficeVisit WHERE patientMID = ?)) AS endWeight, "
                                    + "(SELECT CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END FROM diagnosis WHERE obVisitID IN (SELECT visitID FROM OBOfficeVisit WHERE patientMID = ?) and icdCode = 'O21') AS hyperemesisGravidarum, "
                                    + "(SELECT CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END FROM diagnosis WHERE obVisitID IN (SELECT visitID FROM OBOfficeVisit WHERE patientMID = ?) and icdCode = 'E03') AS hypothyroidism, "
                                    + "(SELECT CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END FROM diagnosis WHERE obVisitID IN (SELECT visitID FROM OBOfficeVisit WHERE patientMID = ?) and icdCode = 'E11') AS diabetes, "
                                    + "(SELECT CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END FROM diagnosis WHERE obVisitID IN (SELECT visitID FROM OBOfficeVisit WHERE patientMID = ?) and icdCode = 'M35') AS autoimmuneDisorders, "
                                    + "(SELECT CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END FROM diagnosis WHERE obVisitID IN (SELECT visitID FROM OBOfficeVisit WHERE patientMID = ?) and icdCode = 'Z11') AS STDs, "
                                    + "(SELECT CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END FROM diagnosis WHERE obVisitID IN (SELECT visitID FROM OBOfficeVisit WHERE patientMID = ?) and icdCode = 'B484') AS penicillin, "
                                    + "(SELECT CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END FROM diagnosis WHERE obVisitID IN (SELECT visitID FROM OBOfficeVisit WHERE patientMID = ?) and icdCode = 'Z88-1') AS sulfaDrugs, "
                                    + "(SELECT CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END FROM diagnosis WHERE obVisitID IN (SELECT visitID FROM OBOfficeVisit WHERE patientMID = ?) and icdCode = 'T36') AS tetracycline, "
                                    + "(SELECT CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END FROM diagnosis WHERE obVisitID IN (SELECT visitID FROM OBOfficeVisit WHERE patientMID = ?) and icdCode = 'Z88-2') AS codeine, "
                                    + "(SELECT CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END FROM diagnosis WHERE obVisitID IN (SELECT visitID FROM OBOfficeVisit WHERE patientMID = ?) and icdCode = 'Z79') AS NSAIDs, "
                                    + "(SELECT CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END FROM diagnosis WHERE obVisitID IN (SELECT visitID FROM OBOfficeVisit WHERE patientMID = ?) and icdCode = 'C44') AS cancers "
                                    + "FROM patients, OBOfficeVisit, diagnosis "
                                    + "WHERE patients.MID = OBOfficeVisit.patientMID AND diagnosis.obVisitID = OBOfficeVisit.visitID AND OBOfficeVisit.patientMID = ?;"
                    );
                    pstring.setLong(1, patientMID);
                    pstring.setLong(2, patientMID);
                    pstring.setLong(3, patientMID);
                    pstring.setLong(4, patientMID);
                    pstring.setLong(5, patientMID);
                    pstring.setLong(6, patientMID);
                    pstring.setLong(7, patientMID);
                    pstring.setLong(8, patientMID);
                    pstring.setLong(9, patientMID);
                    pstring.setLong(10, patientMID);
                    pstring.setLong(11, patientMID);
                    pstring.setLong(12, patientMID);
                    pstring.setLong(13, patientMID);
                    pstring.setLong(14, patientMID);
                    results = pstring.executeQuery();
                    List<obVisitInfo> obVisitList = loader.loadPObVisitInfoList(results);
                    resultReport.setObVisitInfoList(obVisitList);

                    return resultReport;
                }

            } catch (SQLException e) {
                throw new DBException(e);
            } finally {
                try {
                    if (results != null) {
                        results.close();
                    }
                } catch (SQLException e) {
                    throw new DBException(e);
                } finally {
                    DBUtil.closeConnection(conn, pstring);
                }
            }
        } else {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    public boolean add(report r) throws DBException {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    public boolean update(report r) throws DBException {
        return false;
    }

    //shu's code start
    /**
     * Function for testing the OBeligibility
     * @param patientID
     * @return
     * @throws FormValidationException
     * @throws DBException
     */
    public List<Boolean> getOBeligibilityForPatient(long patientID) throws FormValidationException, DBException {
        Connection conn = null;
        PreparedStatement pstring = null;
        ResultSet results = null;
        if (ValidationFormat.NPMID.getRegex().matcher(Long.toString(patientID)).matches()) {
            try {
                conn = ds.getConnection();
                pstring = conn.prepareStatement("SELECT OBEligibility FROM patients WHERE MID=?");

                pstring.setLong(1, patientID);
                results = pstring.executeQuery();   // a Result set type

                ArrayList<Boolean> eligibilityList = new ArrayList<Boolean>();
                while (results.next()) {
                    eligibilityList.add(results.getBoolean("OBEligibility"));
                }

                return eligibilityList;

            } catch (SQLException e) {
                throw new DBException(e);

            } finally {
                try {
                    if (results != null) {
                        results.close();
                    }
                } catch (SQLException e) {
                    throw new DBException(e);
                } finally {
                    DBUtil.closeConnection(conn, pstring);
                }
            }
        } else {
            return null;
        }
    }

    //shu's code end


    // check if the patient has record
    public boolean hasRecord(long patientMID) throws DBException {
        Connection conn = null;
        PreparedStatement pstring = null;
        ResultSet results = null;
        if (ValidationFormat.NPMID.getRegex().matcher(Long.toString(patientMID)).matches()) {
            try {
                conn = ds.getConnection();
                pstring = conn.prepareStatement(
                        "(SELECT CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END AS recordCount FROM initOBrecord WHERE MID = ?);"
                );

                pstring.setLong(1, patientMID);
                results = pstring.executeQuery();   // a Result set type

                boolean hasRecordFlag = false;
                while (results.next()) {
                    hasRecordFlag = results.getBoolean("recordCount");
                }

                return hasRecordFlag;

            } catch (SQLException e) {
                throw new DBException(e);

            } finally {
                try {
                    if (results != null) {
                        results.close();
                    }
                } catch (SQLException e) {
                    throw new DBException(e);
                } finally {
                    DBUtil.closeConnection(conn, pstring);
                }
            }
        } else {
            return false;
        }

    }

    public boolean insertPastPregnancyRecord(pastPregnancy newPastPregnancy) throws DBException {
        Connection conn = null;
        PreparedStatement pstring = null;
        boolean insertSucceed = false;
        try {
            conn = ds.getConnection();
            pstring = conn.prepareStatement("INSERT INTO pregnancyRecord (pregnancyterm, deliverytype, yearofconception, numberofhoursinlabor, weightgainduringpregnancy, pregnancytype, patientMID) VALUES(?,?,?,?,?,?,?)");
            pstring.setInt(1, newPastPregnancy.getPregnancyTerm());
            pstring.setString(2, newPastPregnancy.getDeliveryType());
            pstring.setInt(3, newPastPregnancy.getConceptionYear());
            pstring.setInt(4, newPastPregnancy.getNumberOfHoursInLabor());
            pstring.setInt(5, newPastPregnancy.getWeightGain());
            pstring.setInt(6, newPastPregnancy.getPregnancyType());
            pstring.setLong(7, newPastPregnancy.getPatientMID());
            int updated = pstring.executeUpdate();
            if (updated == 1) {
                insertSucceed = true;
            }
        } catch (SQLException e) {
            throw new DBException(e);
        } finally {
            DBUtil.closeConnection(conn, pstring);
        }
        return insertSucceed;
    }
}
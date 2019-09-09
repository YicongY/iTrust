package edu.ncsu.csc.itrust.controller.obgyn.report;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.ValidationFormat;
import edu.ncsu.csc.itrust.model.obgyn.report.pastPregnancy;
import edu.ncsu.csc.itrust.model.obgyn.report.report;
import edu.ncsu.csc.itrust.model.obgyn.report.reportMySQL;
import javax.sql.DataSource;
import edu.ncsu.csc.itrust.logger.TransactionLogger;
import edu.ncsu.csc.itrust.model.old.enums.TransactionType;

import java.util.Collections;
import java.util.List;

public class reportController {

    reportMySQL reportData ;

    private long loggedInMID;

    public reportController(long loggedInMID) throws DBException {
        super();
        reportData = new reportMySQL();
        this.loggedInMID = loggedInMID;
    }

    public reportController(DataSource ds) throws DBException{
        super();
        reportData  = new reportMySQL(ds);
    }

    public void setSQL(reportMySQL newSQL){
        reportData  = newSQL;
    }

    public report getPatientReportByMID(String patientMID) throws DBException{
        List<Long> res = Collections.emptyList();
        long MID = -1L;
        boolean success = true;
        report newReport = new report();
        MID = Long.parseLong(patientMID);
        try {
            newReport = reportData.getByID(MID);
        } catch (DBException e) {
            success = false;
            e.printStackTrace();
        }

        if (success){
            TransactionLogger.getInstance().logTransaction(TransactionType.VIEW_LABOR_DELIVERY_REPORT, loggedInMID, Long.parseLong(patientMID),"");
        }
        return newReport;
    }

    //dec 5 shu's code
    public boolean isNotElegibleOrHasNoReport(String patientMID) throws DBException{
        List<Long> res = Collections.emptyList();
        long MID = -1L;
        boolean success = true;
        report newReport = new report();
        MID = Long.parseLong(patientMID);
        boolean isElegible = false;
        boolean hasRecord = false;
        try {
            isElegible = reportData.getOBeligibilityForPatient(MID).get(0);
            hasRecord = reportData.hasRecord(MID);
        } catch (DBException e) {
            success = false;
            e.printStackTrace();
        } catch (FormValidationException e) {
            e.printStackTrace();
        }

        return !isElegible || !hasRecord;
    }

    public boolean insertPastPregnancyRecord(pastPregnancy newPregnancy) throws DBException{
        boolean insertSucceed = true;
        reportData.insertPastPregnancyRecord(newPregnancy);
        return insertSucceed;
    }
}

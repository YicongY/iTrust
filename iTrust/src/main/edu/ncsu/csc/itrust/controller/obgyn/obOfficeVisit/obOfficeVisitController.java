package edu.ncsu.csc.itrust.controller.obgyn.obOfficeVisit;


import edu.ncsu.csc.itrust.controller.iTrustController;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.logger.TransactionLogger;
import edu.ncsu.csc.itrust.model.ValidationFormat;
import edu.ncsu.csc.itrust.model.obgyn.obOfficeVisit.obOfficeVisit;
import edu.ncsu.csc.itrust.model.obgyn.obOfficeVisit.obOfficeVisitMySQL;

import edu.ncsu.csc.itrust.model.old.enums.TransactionType;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.sql.DataSource;

import org.jboss.netty.channel.SucceededChannelFuture;

import java.util.Collections;
import java.util.List;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * @author Yicong
 * Controller for  OB OV record
 */

@ManagedBean(name = "ob_office_visit_Controller")
@SessionScoped
public class obOfficeVisitController extends iTrustController {

    obOfficeVisitMySQL ovData;

    private long loggedInMID;

    public obOfficeVisitController(long loggedInMID) throws DBException {
        super();
        ovData = new obOfficeVisitMySQL();
        this.loggedInMID = loggedInMID;
    }

    public obOfficeVisitController(DataSource ds) {
        super();
        ovData = new obOfficeVisitMySQL(ds);
    }

    public void setSQL(obOfficeVisitMySQL newSQL) {
        ovData = newSQL;
    }

    /*
     * Return a list of record for a patient
     * @param id
     * @return list of initialRecord
     */
    public List<obOfficeVisit> getOvRecordsByDate(String patientID) throws FormValidationException, DBException{
        List<obOfficeVisit> ret = Collections.emptyList();
        long mid = -1;
        if ((patientID != null) && ValidationFormat.NPMID.getRegex().matcher(patientID).matches()) {
            mid = Long.parseLong(patientID);
            try {
                ret = ovData.getOBOfficeVisit(mid);
            } catch (DBException e) {
                e.printStackTrace();

            }
        }
        //TransactionLogger.getInstance().logTransaction(TransactionType.VIEW_INITIAL_OBSTETRICS_RECORD, loggedInMID, Long.valueOf(patientID), "EDD");
    
        return ret;
    }

    /*
     * Add office visit record for a patient
     * @param id
     * @return list of initialRecord
     */
    public List<String> add(obOfficeVisit obv) throws DBException{
        List<String> res = Collections.emptyList();
        long recordId = -1;
		try {
            recordId = ovData.addReturnGeneratedId(obv);
		} catch (DBException e) {
			res = e.getErrorList();
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res.size() == 0) {

			TransactionLogger.getInstance().logTransaction(TransactionType.CREATE_OBSTETRICS_OV, loggedInMID, Long.valueOf(obv.getPatientMID()), String.valueOf(recordId));
		}
		return res;
    }

    /*
     * update office visit record for a patient
     * @param id
     * @return list of initialRecord
     */
    public List<String> update(obOfficeVisit obv) throws DBException{
        List<String> res = Collections.emptyList();
        long recordId = obv.getVisiteId();
		try {
            ovData.update(obv);
		} catch (DBException e) {
			res = e.getErrorList();
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res.size() == 0) {

			TransactionLogger.getInstance().logTransaction(TransactionType.EDIT_OBSTETRICS_OV, loggedInMID, Long.valueOf(obv.getPatientMID()), String.valueOf(recordId));
		}
		return res;
    }
    /*
     * delte office visit record for a patient
     * @param id
     * @return list of initialRecord
     */
    public boolean delete(obOfficeVisit obv) throws DBException{
        boolean success = true;
        long recordId = obv.getVisiteId();
		try {
            ovData.delete(recordId);
		} catch (DBException e) {
            success = false;
			e.printStackTrace();
		} catch (Exception e) {
            success = false;
			e.printStackTrace();
		}
		if (success) {

			TransactionLogger.getInstance().logTransaction(TransactionType.EDIT_OBSTETRICS_OV, loggedInMID, Long.valueOf(obv.getPatientMID()), String.valueOf(recordId));
		}
		return success;
    }

    public obOfficeVisit getOBOfficeVisitByVisitID(long visitID){
        obOfficeVisit ret = new obOfficeVisit();

        try {
            ret = ovData.getOBOfficeVisitByVisitID(visitID);
        } catch (DBException e) {
            e.printStackTrace();
        }
        TransactionLogger.getInstance().logTransaction(TransactionType.VIEW_OBSTETRICS_OV, loggedInMID, ret.getPatientMID(), String.valueOf(visitID));

        return ret;
    }

}
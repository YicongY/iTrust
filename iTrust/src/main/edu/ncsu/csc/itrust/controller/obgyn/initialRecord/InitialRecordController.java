package edu.ncsu.csc.itrust.controller.obgyn.initialRecord;


import edu.ncsu.csc.itrust.controller.iTrustController;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.logger.TransactionLogger;
import edu.ncsu.csc.itrust.model.ValidationFormat;
import edu.ncsu.csc.itrust.model.obgyn.initialRecord.initialRecord;
import edu.ncsu.csc.itrust.model.obgyn.initialRecord.initialRecordMySQL;

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
 * Controller for initializing an OB record 
 */

@ManagedBean(name = "initial_Record_Controller")
@SessionScoped
public class InitialRecordController extends iTrustController {
	
    initialRecordMySQL initialRecordData ;

	private long loggedInMID;

    public InitialRecordController(long loggedInMID) throws DBException{
        super();
        initialRecordData = new initialRecordMySQL();
        this.loggedInMID = loggedInMID;
    }

    public InitialRecordController(DataSource ds){
        super();
        initialRecordData  = new initialRecordMySQL(ds);
    }

    public void setSQL(initialRecordMySQL newSQL){
    	initialRecordData  = newSQL;
    }
    
    
    
    /* 
	 * Add a single record for a patient
	 * @param initialRecord
	 * @return List<String> error message for frontend
	 */
    public List<String> add(initialRecord initialRecord) throws DBException{
    	List<String> res = Collections.emptyList();
    	Date EDD = null;
    	Boolean succeed = false;
		try {
			succeed = initialRecordData.add(initialRecord);
		} catch (DBException e) {
			res = e.getErrorList();
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
//			printFacesMessage(FacesMessage.SEVERITY_ERROR, "Unable to add record",
//					"Unable to add record", null);
		}
		if (res.size() == 0) {
//			printFacesMessage(FacesMessage.SEVERITY_INFO, "record create successfully",
//					"Unable to add record", null);
			EDD = initialRecord.getEDD();
			DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy"); 
			TransactionLogger.getInstance().logTransaction(TransactionType.CREATE_INITIAL_OBSTETRICS_RECORD, loggedInMID, Long.valueOf(initialRecord.getMID()), dateFormat.format(EDD));
			//logTransaction(TransactionType.CREATE_INITIAL_OBSTETRICS_RECORD, "EDD");
		}
		return res;

    }
    /* 
	 * Return a list of record for a patient
	 * @param id
	 * @return list of initialRecord
	 */
    public List<initialRecord> getInitialRecordsByDate(String patientID) throws FormValidationException, DBException{
    	List<initialRecord> ret = Collections.emptyList();
		long mid = -1;
		if ((patientID != null) && ValidationFormat.NPMID.getRegex().matcher(patientID).matches()) {
			mid = Long.parseLong(patientID);
			try {
				ret = initialRecordData.getOBrecordForPatient(mid);
			} catch (DBException e) {
				e.printStackTrace();
//				printFacesMessage(FacesMessage.SEVERITY_ERROR, "Unable to Retrieve Record",
//						"Unable to Retrieve Record", null);
			}
		}
		TransactionLogger.getInstance().logTransaction(TransactionType.VIEW_INITIAL_OBSTETRICS_RECORD, loggedInMID, Long.valueOf(patientID), "EDD");
		//logTransaction(TransactionType.VIEW_INITIAL_OBSTETRICS_RECORD, "EDD");
		
		return ret;
    }
    /* 
	 * Update Obeligibility for  patient
	 * @param id
	 * @return initialRecord
	 */
    public Boolean updateOBeligibility(String patientID, Boolean Eligibility) throws FormValidationException, DBException{
    	long mid = -1;
    	Boolean success = false;
		if ((patientID != null) && ValidationFormat.NPMID.getRegex().matcher(patientID).matches()) {
			mid = Long.parseLong(patientID);
			try {
				success = initialRecordData.updateOBeligibility(mid, Eligibility);
			} catch (DBException e) {
				printFacesMessage(FacesMessage.SEVERITY_ERROR, "Unable to Retrieve Record",
						"Unable to Retrieve Record", null);
			}
		}
		return success;
    }
    
    public void edit(initialRecord initialRecord){
        // no need to implement
    }

    public void remove(long initialRecordId){
        // no need to implement
    }



    public long getLoggedInMID() {
		return loggedInMID;
	}

	public void setLoggedInMID(long loggedInMID) {
		this.loggedInMID = loggedInMID;
	}

}

package edu.ncsu.csc.itrust.controller.obgyn.obOfficeVisit;


import edu.ncsu.csc.itrust.controller.iTrustController;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.logger.TransactionLogger;
import edu.ncsu.csc.itrust.model.ValidationFormat;
import edu.ncsu.csc.itrust.model.obgyn.ultrasoundRecord.ultrasoundRecord;
import edu.ncsu.csc.itrust.model.obgyn.ultrasoundRecord.ultrasoundRecordMySQL;

import edu.ncsu.csc.itrust.model.old.enums.TransactionType;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

import java.util.Collections;

/**
 * @author Yicong
 * Controller for  OB OV record
 */

@ManagedBean(name = "ultrasound_Controller")
@SessionScoped
public class ultraSoundController extends iTrustController {

    ultrasoundRecordMySQL ovData;

    private long loggedInMID;

    public ultraSoundController(long loggedInMID) throws DBException {
        super();
        ovData = new ultrasoundRecordMySQL();
        this.loggedInMID = loggedInMID;
    }

    public ultraSoundController(DataSource ds) {
        super();
        ovData = new ultrasoundRecordMySQL(ds);
    }

    public void setSQL(ultrasoundRecordMySQL newSQL) {
        ovData = newSQL;
    }

    
    /*
     * Add office visit record for a patient
     * @param id
     * @return list of initialRecord
     */
    public List<String> add( ultrasoundRecord ulr) throws DBException{
        List<String> res = Collections.emptyList();
        long recordId = -1;
		try {
            recordId = ovData.addReturnGeneratedId(ulr);
		} catch (DBException e) {
			res = e.getErrorList();
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res.size() == 0) {

			TransactionLogger.getInstance().logTransaction(TransactionType.EDIT_ULTRASOUND, loggedInMID, Long.valueOf(ulr.getPatientMID()), String.valueOf(recordId));
		}
		return res;
    }
}
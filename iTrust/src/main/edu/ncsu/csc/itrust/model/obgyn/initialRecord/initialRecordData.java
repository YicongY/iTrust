package edu.ncsu.csc.itrust.model.obgyn.initialRecord;

import java.util.List;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.DataBean;


public interface initialRecordData extends DataBean<initialRecord>{
	public List<initialRecord> getOBrecordForPatient(long patientID) throws FormValidationException, DBException;
	
	
	public Boolean updateOBeligibility(long patientID, Boolean Eligibility) throws FormValidationException, DBException;
	
}
package edu.ncsu.csc.itrust.model.obgyn.obOfficeVisit;

import java.util.List;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.DataBean;

public interface obOfficeVisitData extends DataBean<obOfficeVisit>{
    List<obOfficeVisit> getOBOfficeVisit(long patientID) throws FormValidationException, DBException;
}

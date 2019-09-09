package edu.ncsu.csc.itrust.model.obgyn.childBirthVisit;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.DataBean;
import edu.ncsu.csc.itrust.model.old.beans.ApptBean;
import org.apache.xpath.operations.Bool;

import java.util.List;

/**
 * @ClassName:    ChildBirthVisitData
 * @Description:  Interface for ChildBirthVisitData, defining extra functionality in addition to DataBean interface for uc96
 * @Author:       Xiaocong Yu
 **/
public interface ChildBirthVisitData extends DataBean<ChildBirthVisit> {
    List<ChildBirthVisit> getChildBirthVistForPatient(Long patientID) throws DBException;
}

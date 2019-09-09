package edu.ncsu.csc.itrust.model.obgyn.obOfficeVisit;

import edu.ncsu.csc.itrust.exception.ErrorList;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.POJOValidator;
import edu.ncsu.csc.itrust.model.ValidationFormat;

import javax.sql.DataSource;
import java.time.LocalDate;

/**
 * @author Shu
 */
public class obOfficeVisitValidator extends POJOValidator<obOfficeVisit> {
    /**
     * Default constructor for obOfficeVisitRecord.
     */
    private DataSource ds;
    public obOfficeVisitValidator(DataSource ds) {

        this.ds = ds;
    }

    public obOfficeVisitValidator() { }



    /**
     * Used to Validate an office visit object. If the validation does not
     * succeed, a {@link FormValidationException} is thrown. only performs
     * checks on the values stored in the object (e.g. Patient MID) Does NOT
     * validate the format of the other attributes that are NOT
     * stored in the object itself
     *
     * @param obj
     *
     */


    @Override
    public void validate(obOfficeVisit obj) throws FormValidationException {
        ErrorList errorList = new ErrorList();

        Long visitedID = obj.getVisiteId();
        LocalDate currDate = LocalDate.now();
        errorList.addIfNotNull(checkFormat("Visit ID", Long.toString(visitedID), ValidationFormat.visitedID, false));

        LocalDate patientRecordDate = obj.getCurrentDate();
        int currYear = currDate.getYear();

        if (patientRecordDate.isAfter(currDate)) {
            errorList.addIfNotNull("Record date cannot be later than today");
        }

        if (obj.getWeight() != 0) {
            errorList.addIfNotNull(checkFormat("Patient Weight", Float.toString(obj.getWeight()), ValidationFormat.WEIGHT, true));
        }

        if (obj.getNumOfWeeks() != "") {
            errorList.addIfNotNull(checkFormat("Number of weeks in preg", obj.getNumOfWeeks(), ValidationFormat.WEEKS_PREGNANT, true));
        }

        if (obj.getBloodPressure() != "") {
            errorList.addIfNotNull(checkFormat("Blood Pressure", obj.getBloodPressure(), ValidationFormat.BLOOD_PRESSURE_OV, true));
        }

        if (obj.getFetalHeartRate() != 0) {
            errorList.addIfNotNull(checkFormat("Fetal Heart Rate", Integer.toString(obj.getFetalHeartRate()), ValidationFormat.FHR, true));
        }

        if (obj.isMultiplePregnancy()) {
            errorList.addIfNotNull(checkFormat("is Multiple Pregnancy not zero", Integer.toString(obj.getNumOfMultiplePregnancy()), ValidationFormat.NUMBEROF_PREGNANCY, true));
        }

        if (obj.isMultiplePregnancy() == false) {
            errorList.addIfNotNull(checkFormat("is Multiple Pregnancy zero", Integer.toString(obj.getNumOfMultiplePregnancy()), ValidationFormat.NUMBEROF_PREGNANCY_NOT_MULTIPLE, true));
        }

        if (errorList.hasErrors()) {
            throw new FormValidationException(errorList);
        }
    }

}

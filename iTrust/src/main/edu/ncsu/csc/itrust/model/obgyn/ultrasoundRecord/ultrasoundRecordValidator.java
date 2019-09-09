package edu.ncsu.csc.itrust.model.obgyn.ultrasoundRecord;

import edu.ncsu.csc.itrust.exception.ErrorList;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.POJOValidator;
import edu.ncsu.csc.itrust.model.ValidationFormat;

import javax.sql.DataSource;

/**
 * @author Ziyu
 */
public class ultrasoundRecordValidator extends POJOValidator<ultrasoundRecord>{
    private DataSource ds;

    /**
     * Default constructor for OfficeVisitValidator.
     */
    public ultrasoundRecordValidator() {}

    /**
     * Constructor for the testing purpose.
     */
    public ultrasoundRecordValidator(DataSource ds) {
        this.ds = ds;
    }

    /**
     * Used to Validate an ultrasound record object. If the validation does not
     * succeed, a {@link FormValidationException} is thrown. only performs
     * checks on the values stored in the object (e.g. Patient MID) Does NOT
     * validate the format of the other attributes that are NOT
     * stored in the object itself
     *
     * @param obj   the Obrecord to be validated
     */
    @Override
    public void validate(ultrasoundRecord obj) throws FormValidationException {

        ErrorList errorList = new ErrorList();

        Long obOfficeVisitID = obj.getObOfficeVisitID();
        errorList.addIfNotNull(checkFormat(
                "Visit ID", Long.toString(obOfficeVisitID),
                ValidationFormat.visitedID, false)
        );

        Long patientMID = obj.getPatientMID();
        errorList.addIfNotNull(checkFormat(
                "Patient MID", Long.toString(patientMID),
                ValidationFormat.NPMID, false)
        );

        errorList.addIfNotNull(checkFormat(
                "Location ID", obj.getLocationID(),
                ValidationFormat.HOSPITAL_ID, false)
        );

        if (obj.getCrownRumpLength() < 0) {
            errorList.addIfNotNull("Crown Rump Length: Should be positive");
        }

        if (obj.getBiparietalDiameter() < 0) {
            errorList.addIfNotNull("Biparietal Diameter: Should be positive");
        }

        if (obj.getHeadCircumference() < 0) {
            errorList.addIfNotNull("Head Circumference: Should be positive");
        }

        if (obj.getFemurLength() < 0) {
            errorList.addIfNotNull("Femur Length: Should be positive");
        }

        if (obj.getOccipitofrontalDiameter() < 0) {
            errorList.addIfNotNull("Occipitofrontal Diameter: Should be positive");
        }

        if (obj.getAbdominalCircumference() < 0) {
            errorList.addIfNotNull("Abdominal Circumference: Should be positive");
        }

        if (obj.getHumerusLength() < 0) {
            errorList.addIfNotNull("Humerus Length: Should be positive");
        }

        if (obj.getEstimatedFetalWeight() < 0) {
            errorList.addIfNotNull("Estimated Fetal Weight: Should be positive");
        }

        if (errorList.hasErrors()) {
            throw new FormValidationException(errorList);
        }
    }

}

package edu.ncsu.csc.itrust.unit.model.obgyn.ultrasoundRecord;

import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.obgyn.ultrasoundRecord.ultrasoundRecord;
import edu.ncsu.csc.itrust.model.obgyn.ultrasoundRecord.ultrasoundRecordValidator;
import org.junit.Before;
import org.junit.Test;

import java.awt.image.BufferedImage;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * @author Ziyu
 */
public class ultrasoundRecordValidatorTest {
    ultrasoundRecordValidator validator;
    ultrasoundRecord record;

    @Before
    public void setUp() {
        validator = new ultrasoundRecordValidator();

        // Init a valid ultrasound record
        record = new ultrasoundRecord();
        record.setRecordID(1L);
        record.setObOfficeVisitID(2L);
        record.setPatientMID(3L);
        record.setLocationID("1");
        record.setCrownRumpLength(3.5f);
        record.setBiparietalDiameter(10.0f);
        record.setHeadCircumference(10.0f);
        record.setFemurLength(10.0f);
        record.setOccipitofrontalDiameter(10.0f);
        record.setAbdominalCircumference(10.0f);
        record.setHumerusLength(10.0f);
        record.setEstimatedFetalWeight(10.0f);
        record.setImageFormat("png");

        BufferedImage img = new BufferedImage(
                100, 100,
                BufferedImage.TYPE_3BYTE_BGR
        );
        record.setImageStream(record.imageToInputStream(img));
//        record.setImage(img);
    }

    @Test
    public void testValidUltrasoundRecord() {
        try {
            validator.validate(record);
        } catch (FormValidationException f) {
            fail("Valid ultrasound record wasn't validated. Error: " + f.getMessage());
        }
    }

    @Test
    public void testInvalidVisitID() {
        record.setObOfficeVisitID(-1L);
        tryValidateWithInvalidField(record, "Visit ID");
    }

    @Test
    public void testInvalidPatientMID() {
        record.setPatientMID(-1L);
        tryValidateWithInvalidField(record, "Patient MID");
    }

    @Test
    public void testInvalidLocationID() {
        record.setLocationID("-1");
        tryValidateWithInvalidField(record, "Location ID");
    }

    @Test
    public void testInvalidCrownRumpLength() {
        record.setCrownRumpLength(-3.0f);
        tryValidateWithInvalidField(record, "Crown Rump Length");
    }

    @Test
    public void testInvalidBiparietalDiameter() {
        record.setBiparietalDiameter(-3.0f);
        tryValidateWithInvalidField(record, "Biparietal Diameter");
    }

    @Test
    public void testInvalidHeadCircumference() {
        record.setHeadCircumference(-3.0f);
        tryValidateWithInvalidField(record, "Head Circumference");
    }

    @Test
    public void testInvalidFemurLength() {
        record.setFemurLength(-3.0f);
        tryValidateWithInvalidField(record, "Femur Length");
    }

    @Test
    public void testInvalidOccipitofrontalDiameter() {
        record.setOccipitofrontalDiameter(-3.0f);
        tryValidateWithInvalidField(record, "Occipitofrontal Diameter");
    }

    @Test
    public void testInvalidAbdominalCircumference() {
        record.setAbdominalCircumference(-3.0f);
        tryValidateWithInvalidField(record, "Abdominal Circumference");
    }

    @Test
    public void testInvalidHumerusLength() {
        record.setHumerusLength(-3.0f);
        tryValidateWithInvalidField(record, "Humerus Length");
    }

    @Test
    public void testInvalidEstimatedFetalWeight() {
        record.setEstimatedFetalWeight(-3.0f);
        tryValidateWithInvalidField(record, "Estimated Fetal Weight");
    }

    /**
     * Invokes the validator.validate() method on the given ultrasound record,
     * Assert.fail()ing if the validator does not throw an exception.
     *
     * @param record The ultrasoundRecord object with one invalid field
     * @param nameOfInvalidField Name of the field which is invalid
     */
    private void tryValidateWithInvalidField(ultrasoundRecord record, String nameOfInvalidField) {
        try {
            validator.validate(record);
            fail("Validator should catch invalid field " + nameOfInvalidField);
        } catch (FormValidationException e) {
            assertThat(e.getMessage().toLowerCase(), containsString(nameOfInvalidField.toLowerCase()));
        }
    }
}

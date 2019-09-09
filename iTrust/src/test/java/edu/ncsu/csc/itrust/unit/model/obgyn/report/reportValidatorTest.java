package edu.ncsu.csc.itrust.unit.model.obgyn.report;

import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.obgyn.report.report;
import edu.ncsu.csc.itrust.model.obgyn.report.reportValidator;
import org.junit.Test;

import javax.sql.DataSource;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;


/**
 * @author Shu
 *
 */
public class reportValidatorTest {
    reportValidator validator = new reportValidator();
    report rp = getValidMID();
    private DataSource ds;

    @Test
    public void testInvalidMID() {
        try {
            validator.validate(rp.getPatientMID());
        } catch (FormValidationException f) {
            fail("Valid office visit wasn't validated. Error: " + f.getMessage());
        }

        rp.setPatientMID(-1L);
        tryValidateWithInvalidField(rp, "patient MID");
    }

    public report getValidMID() {
        report rp = new report();
        rp.setPatientMID(1L);
        return rp;
    }

    private void tryValidateWithInvalidField(report rp, String nameOfInvalidField) {
        try {
            validator.validate(rp.getPatientMID());
            fail("Validator should catch invalid field " + nameOfInvalidField);
        } catch (FormValidationException e) {
            // Exception should be thrown
            assertThat(e.getMessage().toLowerCase(), containsString(nameOfInvalidField.toLowerCase()));
        }
    }
}
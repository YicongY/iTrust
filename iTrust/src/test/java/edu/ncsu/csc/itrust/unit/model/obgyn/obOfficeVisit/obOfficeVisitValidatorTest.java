package edu.ncsu.csc.itrust.unit.model.obgyn.obOfficeVisit;

import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.obgyn.obOfficeVisit.obOfficeVisit;
import edu.ncsu.csc.itrust.model.obgyn.obOfficeVisit.obOfficeVisitValidator;
import org.junit.Test;

import java.sql.Date;
import java.time.LocalDate;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * @author Shu
 *
 */
public class obOfficeVisitValidatorTest {
    obOfficeVisitValidator validator = new obOfficeVisitValidator();

    @Test
    public void testInvalidAppDate() {
        obOfficeVisit visit = getValidVisit();
        validator = new obOfficeVisitValidator();
        try {
            validator.validate(visit);
        } catch(FormValidationException f) {
            fail("Valid office visit wasn't validated. Error: " + f.getMessage());
        }

        String str2 = "2020-10-10";
        LocalDate invalidDate = LocalDate.parse(str2);
        visit.setCurrentDate(invalidDate);
        tryValidateWithInvalidField(visit, "record date");
    }

    @Test
    public void testInvalidNumOfWeek() {
        obOfficeVisit visit = getValidVisit();
        validator = new obOfficeVisitValidator();
        try {
            validator.validate(visit);
        } catch(FormValidationException f) {
            fail("Valid office visit wasn't validated. Error: " + f.getMessage());
        }

        String invalidNumOfWeek = "43-7";
        visit.setNumOfWeeks(invalidNumOfWeek);
        tryValidateWithInvalidField(visit, "number of weeks");
    }

    @Test
    public void testInvalidWeight() {
        obOfficeVisit visit = getValidVisit();
        validator = new obOfficeVisitValidator();
        try {
            validator.validate(visit);
        } catch(FormValidationException f) {
            fail("Valid office visit wasn't validated. Error: " + f.getMessage());
        }

        float invalidWeight = 1000000.5f;
        visit.setWeight(invalidWeight);
        tryValidateWithInvalidField(visit, "weight");
    }

    @Test
    public void testInvalidBloodPressure() {
        obOfficeVisit visit = getValidVisit();
        validator = new obOfficeVisitValidator();
        try {
            validator.validate(visit);
        } catch(FormValidationException f) {
            fail("Valid office visit wasn't validated. Error: " + f.getMessage());
        }

        String invalidBloodPressure = "-199/-999";
        visit.setBloodPressure(invalidBloodPressure);
        tryValidateWithInvalidField(visit, "blood pressure");
    }

    @Test
    public void testInvalidFetalHeartRate() {
        obOfficeVisit visit = getValidVisit();
        validator = new obOfficeVisitValidator();
        try {
            validator.validate(visit);
        } catch(FormValidationException f) {
            fail("Valid office visit wasn't validated. Error: " + f.getMessage());
        }

        int invalidFHR = -1;
        visit.setFetalHeartRate(invalidFHR);
        tryValidateWithInvalidField(visit, "heart rate");
    }

    @Test
    public void testInvalidMultiplePregnancy1() {
        obOfficeVisit visit = getValidVisit();
        validator = new obOfficeVisitValidator();
        try {
            validator.validate(visit);
        } catch(FormValidationException f) {
            fail("Valid office visit wasn't validated. Error: " + f.getMessage());
        }

        visit.setMultiplePregnancy(false);
        tryValidateWithInvalidField(visit, "is Multiple Pregnancy zero");
    }


    @Test
    public void testInvalidMultiplePregnancy2() {
        obOfficeVisit visit = getValidVisit();
        validator = new obOfficeVisitValidator();
        try {
            validator.validate(visit);
        } catch(FormValidationException f) {
            fail("Valid office visit wasn't validated. Error: " + f.getMessage());
        }

        visit.setNumOfMultiplePregnancy(0);
        tryValidateWithInvalidField(visit, "is Multiple Pregnancy not zero");
    }

    public obOfficeVisit getValidVisit() {
        obOfficeVisit cur = new obOfficeVisit();
        cur.setVisiteId(2L);
        cur.setPatientMID(3L);
        cur.setLocationID("1");
        cur.setApptTypeID(1);
        LocalDate date = LocalDate.now();
        cur.setCurrentDate(date);
        cur.setNumOfWeeks("12-5");
        cur.setBloodPressure("120/120");
        cur.setFetalHeartRate(130);
        cur.setMultiplePregnancy(true);
        cur.setNumOfMultiplePregnancy(2);
        cur.setLowLyingPlacentaObserved(true);
        return cur;
    }


    private void tryValidateWithInvalidField(obOfficeVisit visit, String nameOfInvalidField) {
        try {
            validator.validate(visit);
            fail("Validator should catch invalid field " + nameOfInvalidField);
        } catch (FormValidationException e) {
            // Exception should be thrown
            assertThat(e.getMessage().toLowerCase(), containsString(nameOfInvalidField.toLowerCase()));
        }
    }
}

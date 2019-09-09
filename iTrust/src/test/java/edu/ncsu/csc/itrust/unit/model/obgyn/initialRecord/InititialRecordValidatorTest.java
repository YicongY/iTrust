package edu.ncsu.csc.itrust.unit.model.obgyn.initialRecord;

import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.obgyn.initialRecord.initialRecord;
import edu.ncsu.csc.itrust.model.obgyn.initialRecord.initialRecordValidator;
import org.junit.Test;

import java.sql.Date;
import java.time.LocalDate;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * @author Ziyu & Shu
 *
 */

public class InititialRecordValidatorTest {
    initialRecord rc;
    initialRecordValidator validator;

    @Test
    public void testInvalidDate() {
        rc = getValidInitialRecord();

        String str2 = "2020-10-10";
        Date date2 = Date.valueOf(str2);
        rc.setRecordDate(date2);
        tryValidateWithInvalidField(rc, "Record date");
    }

    @Test
    public void testInvalidYear() {
        rc = getValidInitialRecord();
        rc.setYearOfConception(2019);
        tryValidateWithInvalidField(rc, "year");
    }

    @Test
    public void testInvalidDeliveryType() {
        rc = getValidInitialRecord();

        rc.setDeliveryType("foo");
        tryValidateWithInvalidField(rc, "Delivery type");
    }

    @Test
    public void testInvalidLMPDate() {
        rc = getValidInitialRecord();

        String lmp1 = "2020-10-30";
        java.sql.Date dateLmp1 = java.sql.Date.valueOf(lmp1);
        rc.setLMP(dateLmp1);

//        System.out.println("==> in test");
//        System.out.println(rc.getLMP());
//        System.out.println(rc.getLMP().toLocalDate().isAfter(LocalDate.now()));
//        System.out.println("==> test end");

        tryValidateWithInvalidField(rc, "LMP date");
    }

    @Test
    public void testInvalidPatientMID() {
        rc = getValidInitialRecord();
        rc.setMID(10000000000L);
        tryValidateWithInvalidField(rc, "Patient MID");
    }

    @Test
    public void testInvalidWeightGain() {
        rc = getValidInitialRecord();
        rc.setWeightGainDuringPregnancy(10000);
        tryValidateWithInvalidField(rc, "Weight Gain");
    }

    @Test
    public void testInvalidHoursInLabor() {
        rc = getValidInitialRecord();
        rc.setNumberOfHoursInLabor(10000);
        tryValidateWithInvalidField(rc, "hours in labor");
    }

    @Test
    public void testInvalidPregnancyType() {
        rc = getValidInitialRecord();
        rc.setPregnancyType(100);
        tryValidateWithInvalidField(rc, "Pregnancy Type");
    }

    private void tryValidateWithInvalidField(initialRecord ir, String nameOfInvalidField) {
        try {
            validator.validate(ir);
            fail("Validator should catch invalid field " + nameOfInvalidField);
        } catch (FormValidationException e) {
            // Exception should be thrown
            assertThat(e.getMessage().toLowerCase(), containsString(nameOfInvalidField.toLowerCase()));
        }
    }

    private initialRecord getValidInitialRecord() {
        initialRecord rc = new initialRecord();
        rc.setDeliveryType("vaginal delivery");
        String lmp = "2018-10-29";
        java.sql.Date dateLmp = java.sql.Date.valueOf(lmp);
        String edd = "2019-08-05";
        java.sql.Date dateEdd = java.sql.Date.valueOf(edd);
        rc.setEDD(dateEdd);
        rc.setLMP(dateLmp);
        rc.setMID(3L);
        rc.setNumberOfHoursInLabor(100);
        rc.setNumberOfWeeksPregnant("10-2");
        rc.setObRecordID(1L);
        rc.setPregnancyType(0);
        rc.setWeightGainDuringPregnancy(100);
        rc.setYearOfConception(2018);
        LocalDate date = LocalDate.now();
        rc.setRecordDate(java.sql.Date.valueOf(date));
        validator = new initialRecordValidator();
        try {
            validator.validate(rc);
        } catch (FormValidationException f) {
            fail("Valid initial record wasn't validated. Error: " + f.getMessage());
        }
        return rc;
    }
}

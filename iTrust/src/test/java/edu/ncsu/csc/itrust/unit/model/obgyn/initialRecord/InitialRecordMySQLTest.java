package edu.ncsu.csc.itrust.unit.model.obgyn.initialRecord;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.obgyn.initialRecord.initialRecord;
import edu.ncsu.csc.itrust.model.obgyn.initialRecord.initialRecordMySQL;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import javax.sql.DataSource;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * @author Ziyu & Shu
 *
 */

public class InitialRecordMySQLTest extends TestCase {
    private DataSource ds;
    private initialRecordMySQL irsql;
    private initialRecord ir;
    private TestDataGenerator gen;

    @Mock
    private DataSource mockDataSource;

    @Before
    public void setUp() throws Exception {
        ds = ConverterDAO.getDataSource();
        irsql = new initialRecordMySQL(ds);
        gen = new TestDataGenerator();
        gen.clearAllTables();
        gen.uc93();
        mockDataSource = Mockito.mock(DataSource.class);
    }

    @After
    public void tearDown() throws FileNotFoundException, SQLException, IOException {
        gen.clearAllTables();
    }

    @Test
    public void testGetOBrecordForPatient() throws FormValidationException, DBException {
        List<initialRecord> list1 = irsql.getOBrecordForPatient(1010L);
        assertEquals(0, list1.size());
        List<initialRecord> list2 = irsql.getOBrecordForPatient(1234L);
        assertEquals(1, list2.size());
        initialRecord rc = list2.get(0);
        assertEquals(rc.getRecordDate(), java.sql.Date.valueOf(LocalDate.now()));
        assertEquals(rc.getYearOfConception(), 2017);
        assertEquals(rc.getNumberOfWeeksPregnant(), "28-1");
        assertEquals(rc.getNumberOfHoursInLabor(), 10.1f);
        assertEquals(rc.getWeightGainDuringPregnancy(), 30.5f);
        assertEquals(rc.getDeliveryType(),"vaginal delivery");
        assertEquals(rc.getPregnancyType(), 1);
        assertEquals(rc.getLMP(), Date.valueOf("2017-1-1"));
        assertEquals(rc.getEDD(), Date.valueOf("2017-10-10"));
    }

    @Test
    public void testAdd() throws FormValidationException, DBException, IOException, SQLException {
        initialRecord rc = new initialRecord();
        rc.setDeliveryType("vaginal delivery");
        String lmp = "2018-10-29";
        java.sql.Date dateLmp = java.sql.Date.valueOf(lmp);
        String edd = "2019-08-05";
        java.sql.Date dateEdd = java.sql.Date.valueOf(edd);
        rc.setMID(80060L);
        rc.setEDD(dateEdd);
        rc.setLMP(dateLmp);
        rc.setNumberOfHoursInLabor(100);
        rc.setNumberOfWeeksPregnant("10-2");
        rc.setObRecordID(1L);
        rc.setPregnancyType(0);
        rc.setWeightGainDuringPregnancy(100);
        rc.setYearOfConception(2018);
        LocalDate localD = LocalDate.now();
        java.sql.Date date = Date.valueOf(localD);
        rc.setRecordDate(date);
        irsql.add(rc); // add a valid record
        List<initialRecord> list2 = irsql.getOBrecordForPatient(80060L);
        Assert.assertTrue(list2.size() == 1);
        Assert.assertEquals(list2.get(0).getDeliveryType(), "vaginal delivery");
        Date date1 = Date.valueOf(LocalDate.now());
        Assert.assertTrue(list2.get(0).getRecordDate().compareTo(date1) == 0);
        Assert.assertTrue(list2.get(0).getYearOfConception() == 2018);
        Assert.assertEquals(list2.get(0).getNumberOfWeeksPregnant(), "10-2");
        Assert.assertTrue(list2.get(0).getNumberOfHoursInLabor() == 100);
        Assert.assertTrue(list2.get(0).getWeightGainDuringPregnancy() == 100);
        Assert.assertTrue(list2.get(0).getPregnancyType() == 0);
        String lmp1 = "2018-10-29";
        Date dateLmp1 = Date.valueOf(lmp1);
        Assert.assertTrue(list2.get(0).getLMP().compareTo(dateLmp1) == 0);
        String edd1 = "2019-08-05";
        Date dateEdd1 = Date.valueOf(edd1);
        Assert.assertTrue(list2.get(0).getEDD().compareTo(dateEdd1) == 0);
    }


    @Test
    public void testGetOBeligibilityForPatient() throws FormValidationException, DBException {

        // Generate a test data

        try {
            gen.uc93();
        } catch (Exception e) {
            fail("Couldn't set up test data");
        }

        // Validate the retrieved test data
        long patientID = 800;

        List<Boolean> all;
        try {
            all = irsql.getOBeligibilityForPatient(patientID);

            if (all == null) {
                fail("Get eligibility by an existing MID should not return null");
            }

            if (all.size() != 1) {
                fail("Get eligibility by an existing MID should not return more than one row");
            }

            Boolean expectedEligibility = true;
            Boolean retrievedEligibility = all.get(0);
            Assert.assertEquals(expectedEligibility, retrievedEligibility);

        } catch (DBException e) {
            fail("DBException should not be thrown");
        }
    }

    @Test
    public void testUpdateOBeligibility() throws FormValidationException, DBException {

        // Generate a test data

        try {
            gen.uc93();
        } catch (SQLException | IOException e1) {
            fail("Couldn't set up test data");
            e1.printStackTrace();
        }

        // Update the generated data (just a single row)

        long patientID = 800;
        Boolean newEligibility = false;
        boolean success = false;

        try {
            success = irsql.updateOBeligibility(patientID, newEligibility);
        } catch (DBException e) {
            fail("Shouldn't throw exception when updating OB eligibility: " + e.getMessage());
        }

        if (!success) {
            fail("updateOBeligibility should return true when updating is successful");
        }

        // Validate the row has been correctly updated

        List<Boolean> allUpdated;
        try {
            allUpdated = irsql.getOBeligibilityForPatient(patientID);

            if (allUpdated == null) {
                fail("Get eligibility method should not return null");
            }

            if (allUpdated.size() != 1) {
                fail("Update method should not increase the number of patients");
            }

            Boolean updatedEligibility = allUpdated.get(0);
            Assert.assertEquals(newEligibility, updatedEligibility);

        } catch (DBException e) {
            fail("DBException should not be thrown");
        }
    }
}

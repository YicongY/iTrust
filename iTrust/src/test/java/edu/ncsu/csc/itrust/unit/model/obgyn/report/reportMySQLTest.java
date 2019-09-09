package edu.ncsu.csc.itrust.unit.model.obgyn.report;

import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.obgyn.report.obVisitInfo;
import edu.ncsu.csc.itrust.model.obgyn.report.pastPregnancy;
import edu.ncsu.csc.itrust.model.obgyn.report.report;
import edu.ncsu.csc.itrust.model.obgyn.report.reportMySQL;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.sql.DataSource;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author ziyu
 */
public class reportMySQLTest {
    private DataSource ds;
    private reportMySQL rsql;
    private report report;
    private TestDataGenerator gen;

    @Before
    public void setUp() throws Exception {
        ds = ConverterDAO.getDataSource();
        rsql = new reportMySQL(ds);
        report = new report();
        gen = new TestDataGenerator();
        gen.clearAllTables();
        gen.uc95();
    }

    @After
    public void tearDown() throws FileNotFoundException, SQLException, IOException {
        gen.clearAllTables();
    }

    @Test
    public void testGetByID() throws Exception {
        long patientMID = 5678;
        report reportActual = rsql.getByID(patientMID);

        // Test MID, EDD and blood type
        Assert.assertTrue(reportActual.getPatientMID() == patientMID);
        Assert.assertTrue(
                reportActual.getEDD().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")).equals("2019-02-01")
        );
        Assert.assertTrue(reportActual.getBloodType().equals("AB+"));

        // Test for past pregnancy
        List<pastPregnancy> pregnancyList = reportActual.getPastPregnancyList();
        pastPregnancy pregnancy = pregnancyList.get(0);
        Assert.assertTrue(pregnancy.getPregnancyTerm() == 10);
        Assert.assertTrue(pregnancy.getConceptionYear() == 2016);
        Assert.assertTrue(pregnancy.getDeliveryType().equals("vaginal"));

        // Test for OB office visit info (visitID = 1)
        List<obVisitInfo> obVisitInfoList = reportActual.getObVisitInfoList();
        obVisitInfo visit = obVisitInfoList.get(0);
        Assert.assertFalse(visit.isRHFlag());
        Assert.assertTrue(visit.isHighBloodPressure());
        Assert.assertTrue(visit.isAdvancedMaternalAge());
        Assert.assertFalse(visit.isLowLyingPlacenta());
        Assert.assertTrue(visit.isAbnormalFetalHeartRate());
        Assert.assertTrue(visit.isMultiplePregnancy());
        Assert.assertTrue(visit.isAtypicalWeightChange());
        Assert.assertTrue(visit.isHypothyroidism());
        Assert.assertTrue(visit.isHyperemesisGravidarum());
        Assert.assertTrue(visit.isDiabetes());
        Assert.assertFalse(visit.isAutoimmuneDisorders());

        // has cancer
        Assert.assertTrue(visit.isCancers());

        Assert.assertFalse(visit.isSTDs());
        Assert.assertFalse(visit.isPenicillin());
        Assert.assertFalse(visit.isSulfaDrugs());
        Assert.assertFalse(visit.isTetracycline());
        Assert.assertFalse(visit.isCodeine());
        Assert.assertFalse(visit.isNSAIDs());
        Assert.assertTrue(visit.getNumOfWeeksPregnant().equals("10-5"));
        Assert.assertTrue(visit.getWeight() == 73.5);
        Assert.assertTrue(visit.getBloodPressure().equals("120/120"));
        Assert.assertTrue(visit.getFetalHeartRate() == 6);
        Assert.assertTrue(visit.isMultiple());
        Assert.assertTrue(visit.getNumOfMultiples() == 2);
        Assert.assertFalse(visit.isLowLyingPlacenta());
        Assert.assertTrue(visit.isComplications());
    }

    @Test
    public void testGetOBeligibilityForPatient() throws Exception {
        // Generate a test data
        gen.uc93();

        // Validate the retrieved test data
        long patientMID = 800;

        List<Boolean> all;
        all = rsql.getOBeligibilityForPatient(patientMID);
        Assert.assertTrue(all.size() == 1);
        Assert.assertTrue(all.get(0));
    }

    @Test
    public void testHasRecord() throws Exception {
        long patientMID = 6666;
        boolean hasRecordFlag = rsql.hasRecord(patientMID);
        Assert.assertFalse(hasRecordFlag);
    }
}

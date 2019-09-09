package edu.ncsu.csc.itrust.unit.controller.obgyn.initialRecord;

import edu.ncsu.csc.itrust.controller.obgyn.report.reportController;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.ConverterDAO;
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
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class reportControllerTest {
    private static final String DEFAULT_PATIENT_MID = "1";
    private static final String DEFAULT_INVALID_PATIENT_MID = "-1";

    private DataSource ds;
    private TestDataGenerator gen;
    private reportController rpC;

    @Before
    public void setUp() throws Exception {
        ds = ConverterDAO.getDataSource();
        gen = new TestDataGenerator();
        gen.clearAllTables();
        gen.uc95();
        rpC = new reportController(ds);
    }

    @After
    public void tearDown() throws FileNotFoundException, SQLException, IOException {
        gen.clearAllTables();
    }

    @Test
    public void TestGetByInvalidMID () {
        try {
            Assert.assertTrue(rpC.getPatientReportByMID(DEFAULT_INVALID_PATIENT_MID) == null);
        } catch (DBException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void TestGetByValidMID() {
        try {
            report rp = rpC.getPatientReportByMID("5678");
            Assert.assertTrue(rp != null);
        } catch (DBException e) {
            e.printStackTrace();
        }
    }
}

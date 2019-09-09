package edu.ncsu.csc.itrust.unit.controller.obgyn.childBirthVisit;

import edu.ncsu.csc.itrust.controller.obgyn.childBirthVisit.ChildBirthVisitController;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.obgyn.childBirthVisit.ChildBirthVisit;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.sql.Date;
import java.sql.Time;
import java.util.List;

import javax.sql.DataSource;

public class ChildBirthVisitControllerTest extends TestCase {
    private DataSource ds;
    private TestDataGenerator gen;
    private ChildBirthVisitController cbvController;
    private ChildBirthVisit cbv;
    private static final String testmid = "801";

    @Before
    protected void setUp() throws Exception {
        ds = ConverterDAO.getDataSource();
        gen = new TestDataGenerator();
        gen.clearAllTables();
        gen.uc96();


        cbvController = new ChildBirthVisitController(ds,
                TestDAOFactory.getTestInstance());

        cbv = new ChildBirthVisit();
        cbv.setPatientMID(801L);
        cbv.setPreferredDeliveryType("vaginal delivery");
        cbv.setDeliveryType("vaginal delivery");
        cbv.setVisitType("appointment");
        cbv.setDeliveryDate(Date.valueOf( "2018-01-31" ));
        cbv.setDeliveryTime(new Time(1, 0, 0));
        cbv.setNumberOfBoyBabies(1);
        cbv.setNumberOfGirlBabies(0);
        cbv.setDosage_pitocin(1);
        cbv.setDosage_nitrousOxide(1);
        cbv.setDosage_pethidine(1);
        cbv.setDosage_epiduralAnaesthesia(1);
        cbv.setDosage_magnesiumSulfate(1);
        cbv.setDosage_rhImmuneGlobulin(1);
        cbv.setComment("Test");
    }

    @After
    protected void tearDown() throws Exception {
        super.tearDown();
        gen.clearAllTables();
    }

    @Test
    public void testLoggedInMID() {
        cbvController.setLoggedInMID(100L);
        assertEquals(100L, cbvController.getLoggedInMID());
    }

    @Test
    public void testAddChildBirthVisit() throws Exception{
        cbvController.addChildBirthVisit(cbv);
        assertTrue(true);
    }

    @Test
    public void testGetChildBirthVisiteByDate() throws Exception{
        List<ChildBirthVisit> retList = cbvController.getChildBirthVisitByDate(testmid);
        assertEquals(1, retList.size());
    }

    @Test
    public void testGetChildBirthVisiteByDateWithNullID() throws DBException {
        try {
            cbvController.getChildBirthVisitByDate(null);
            Assert.fail("Should not get any childBirthVisit with null pointer!");
        } catch (FormValidationException e) {
            assertTrue(e.getMessage().contains("Invalid patientID"));
        }
    }

    @Test
    public void testGetChildBirthVisiteByDateWithInvalidID() throws DBException {
        try {
            cbvController.getChildBirthVisitByDate("10000000000");
            Assert.fail("Should not get any childBirthVisit with null pointer!");
        } catch (FormValidationException e) {
            assertTrue(e.getMessage().contains("Invalid patientID"));
        }
    }

    @Test
    public void testUpdateChildBirthVisit() throws Exception{
        cbv.setComment("Test");
        cbv.setChildBirthVisitID(1L);
        cbvController.updateChildBirthVisit(cbv);
        assertTrue(true);
    }

    @Test
    public void testCheckExistingAppointment() throws Exception{
        String retType = cbvController.checkExistingAppointment(testmid);
        assertTrue(retType.equals("vaginal delivery"));
    }
}

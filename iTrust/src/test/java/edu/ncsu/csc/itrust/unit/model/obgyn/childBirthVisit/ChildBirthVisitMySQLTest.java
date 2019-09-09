package edu.ncsu.csc.itrust.unit.model.obgyn.childBirthVisit;

import com.sun.jna.platform.win32.WinNT;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.obgyn.childBirthVisit.ChildBirthVisit;
import edu.ncsu.csc.itrust.model.obgyn.childBirthVisit.ChildBirthVisitMySQL;
import edu.ncsu.csc.itrust.model.obgyn.childBirthVisit.ChildBirthVisitMySQLLoader;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import junit.framework.TestCase;
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javax.naming.Context;
import javax.sql.DataSource;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotEquals;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * @ClassName:    ChildBirthVisitMySQLTest
 * @Description:  uc96 Model Test
 * @Author:       Xiaocong Yu
 **/
public class ChildBirthVisitMySQLTest extends TestCase {
    private DataSource ds;
    private ChildBirthVisitMySQL cbvsql;
    private TestDataGenerator gen;

    private static Long DEFAULTED_MID = 801L;
    private static Long DEFAULTED_CBV_ID = 100L;
    private static Long DEFAULTED_INVALID_MID = 1000L;
    private static Long DEFAULTED_DOCTOR_MID = 900000000L;
    @Rule public ExpectedException thrown= ExpectedException.none();

    @Before
    protected void setUp() throws Exception {
        super.setUp();
        MockitoAnnotations.initMocks(this);
        ds = ConverterDAO.getDataSource();
        cbvsql = new ChildBirthVisitMySQL(ds);
        gen = new TestDataGenerator();
        gen.clearAllTables();
        gen.uc96();
    }

    @After
    protected void tearDown() throws Exception {
//        super.tearDown();
//        gen.clearAllTables();
    }

    /**
     * helper function for generating a ChildBirthVisit Object with test data in it
     * @return
     */
    static private ChildBirthVisit beanGenerator(){
        ChildBirthVisit tmp_cbv= new ChildBirthVisit();
        java.util.Date curr_date = new java.util.Date();
        Date today = new Date(curr_date.getTime());
        tmp_cbv.setDeliveryDate(today);
        tmp_cbv.setDeliveryTime(Time.valueOf("20:00:00"));
        tmp_cbv.setComment("test cmmt");
        tmp_cbv.setPatientMID(DEFAULTED_MID);
        tmp_cbv.setDeliveryType("vaginal delivery");
        tmp_cbv.setPreferredDeliveryType("vaginal delivery");
        tmp_cbv.setVisitType("appointment");
        tmp_cbv.setDosage_epiduralAnaesthesia(1);
        tmp_cbv.setDosage_magnesiumSulfate(1);
        tmp_cbv.setDosage_pethidine(1);
        tmp_cbv.setDosage_nitrousOxide(1);
        tmp_cbv.setDosage_pitocin(1);
        tmp_cbv.setDosage_rhImmuneGlobulin(1);
        tmp_cbv.setNumberOfBoyBabies(1);
        tmp_cbv.setNumberOfGirlBabies(1);
        return tmp_cbv;
    }



    /**
     * test for getChildBirthVisitForPatient() function
     * @throws Exception
     */
    @Test
    public void testGetChildBirthVisitForPatient() throws Exception {
        // for valid MID
        List<ChildBirthVisit> list_withValidMID = cbvsql.getChildBirthVistForPatient(DEFAULTED_MID);
        assertEquals(1, list_withValidMID.size());

        // for valid MID each object should be not-null
        for (ChildBirthVisit tmp : list_withValidMID) {
            assertNotNull(tmp);
        }

        //DEFAULTED_INVALID_MID
        List<ChildBirthVisit> list_withInvalidMID = cbvsql.getChildBirthVistForPatient(900000000L);
        assertNull(list_withInvalidMID);
    }

    /**
     *      normal insertion and exception test
     *      test data: delivery time: currentYear-currentMon-currentDate 20:00:00 by appointment,
     *      preferredType = vaginal delivery, 1 boy and 1 girl
     * @throws FormValidationException
     * @throws IOException
     * @throws SQLException
     * @throws DBException
     */
    @Test
    public void testAdd() throws FormValidationException, IOException, SQLException, DBException {
        ChildBirthVisit tmp_cbv = beanGenerator();
        assertEquals(true, cbvsql.add(tmp_cbv));
        ChildBirthVisit inserted_cbv = cbvsql.getChildBirthVistForPatient(DEFAULTED_MID).get(0);
        assertEquals(tmp_cbv.getDeliveryType(), inserted_cbv.getDeliveryType());
        assertEquals(true, tmp_cbv.getDeliveryDate().toLocalDate().isEqual(inserted_cbv.getDeliveryDate().toLocalDate()));
        assertEquals(tmp_cbv.getDeliveryTime(), inserted_cbv.getDeliveryTime());
        assertEquals(tmp_cbv.getPreferredDeliveryType(), inserted_cbv.getPreferredDeliveryType());
        assertEquals(tmp_cbv.getVisitType(), inserted_cbv.getVisitType());
        assertEquals(tmp_cbv.getNumberOfBoyBabies(), inserted_cbv.getNumberOfBoyBabies());
        assertEquals(tmp_cbv.getNumberOfGirlBabies(), inserted_cbv.getNumberOfGirlBabies());
    }

    /**
     * test for addReturnGeneratedId() method
     * @throws DBException
     * @throws SQLException
     * @throws IOException
     * @throws FormValidationException
     */
    @Test(expected = DBException.class)
    public void testAddReturnGenerateId()throws DBException, SQLException, IOException, FormValidationException {
        ChildBirthVisit tmp_cbv = beanGenerator();
        // expected generated id returned
        assertEquals(101L, (long) cbvsql.addReturnGeneratedId(tmp_cbv));

        // add for the second time, expected exception occurred
        cbvsql.addReturnGeneratedId(tmp_cbv);

    }

    /**
     * test for update() method
     * @throws DBException
     * @throws FormValidationException
     * @throws SQLException
     * @throws IOException
     */
    @Test
    public void testUpdate()throws DBException, FormValidationException, SQLException, IOException {
        ChildBirthVisit tmp_cbv= cbvsql.getChildBirthVistForPatient(DEFAULTED_MID).get(0);

        tmp_cbv.setDeliveryType("vaginal delivery vacuum assist");
        tmp_cbv.setVisitType("emergency");
        tmp_cbv.setPreferredDeliveryType("");

        // 1 row affcted
        assertEquals(true, cbvsql.update(tmp_cbv));

        // same cbv update, executeUpdate() should return 0 for no sql update, then update() shall return false
        assertEquals(true, cbvsql.update(tmp_cbv));

    }

    /**
     * constructor test
     * @throws DBException
     */
    @Test
    public void testConstructor() throws DBException {
        boolean thrown = false;
        try {
            ChildBirthVisitMySQL test_sql = new ChildBirthVisitMySQL();
        } catch(DBException e) {
            thrown = true;
        }
        assertTrue(thrown);
    }

//    /**
//     * test getById() method (optional for this uc)
//     * @throws DBException
//     */
//    public void testGetById() throws DBException {
//        ChildBirthVisit cbv_current = cbvsql.getByID(DEFAULTED_CBV_ID);
//        assertTrue(cbv_current.getPatientMID() == 100L);
//        assertTrue(cbv_current.getComment().equals("test cmmt"));
//        assertTrue(cbv_current.getVisitType().equals("appointment"));
//        assertTrue(cbv_current.getPreferredDeliveryType().equals("vaginal delivery"));
//        assertTrue(cbv_current.getDeliveryType().equals("vaginal delivery"));
//        assertTrue(cbv_current.getNumberOfBoyBabies() == 1);
//        assertTrue(cbv_current.getNumberOfGirlBabies() == 1);
//        assertTrue(cbv_current.getDosage_rhImmuneGlobulin() == 1);
//        assertTrue(cbv_current.getDosage_pitocin() == 1);
//        assertTrue(cbv_current.getDosage_pethidine() == 1);
//        assertTrue(cbv_current.getDosage_nitrousOxide() == 1);
//        assertTrue(cbv_current.getDosage_epiduralAnaesthesia() == 1);
//        assertTrue(cbv_current.getDosage_magnesiumSulfate() == 1);
//    }
}
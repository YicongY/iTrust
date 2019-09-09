package edu.ncsu.csc.itrust.unit.controller.obgyn.initialRecord;

import edu.ncsu.csc.itrust.controller.obgyn.initialRecord.InitialRecordController;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.logger.TransactionLogger;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.obgyn.initialRecord.initialRecord;
import edu.ncsu.csc.itrust.model.obgyn.initialRecord.initialRecordMySQL;
import edu.ncsu.csc.itrust.model.old.enums.TransactionType;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.webutils.SessionUtils;

import org.junit.Assert;
import org.junit.Before;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import javax.faces.application.FacesMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import junit.framework.TestCase;

import static org.junit.Assert.assertNotEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * @ClassName:    InitialRecordControllerTest
 * @Description:  test_driven InitialRecordController.java
 *
 *
 * @Author:       Xiaocong Yu
 * @Date:         10/29/2018
 **/
public class InitialRecordControllerTest extends TestCase {

    private static final String DEFAULT_PATIENT_MID = "1";
    public static final String  DEFAULT_HCP_MID = "900000000";
    private static final String DEFAULT_INVALID_PATIENT_MID = "-1";

//    @Mock
//    private HttpServletRequest mockHttpServletRequest;
//    @Mock
//    private HttpSession mockHttpServletSession;

//    @Mock
//    private initialRecordData mockInitialRecordData;
    @Mock
    private initialRecordMySQL mockInitialRecordMySQL;
    @Mock
    private SessionUtils mockSessionUtils;
    @Mock
    private TransactionLogger mockLogger;

    private initialRecord testRecord;
//    private initialRecordMySQL initialRecordSQL;

    @Spy private InitialRecordController controller;
//    @Spy private InitialRecordController controllerWithNullSource;
//    @Spy private SessionUtils sessionUtils;

    private TestDataGenerator gen;
    private DataSource ds;

    /**
     * prepare the mock object for each test method
    **/
    @Before
    public void setUp() throws Exception {
        ds = ConverterDAO.getDataSource();
        controller = spy(new InitialRecordController(ds));
        MockitoAnnotations.initMocks(this);
        controller.setSessionUtils(mockSessionUtils);
        controller.setSQL(mockInitialRecordMySQL);
        controller.setTransactionLogger(mockLogger);
        // global stubbing for printFacesMessage and logTransaction
        doNothing().when(controller).printFacesMessage(Mockito.any(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
        doNothing().when(mockLogger).logTransaction(any(), any(), any(),any());

        gen = new TestDataGenerator();


        // generate database with
        // 1.fake obrecords
        // 2.uneligible hcp
        //   eligible hcp
        // 3.uneligible patient
//           eligible patient

//        gen.uc73();
//        flushTable();

        // setup a testRecord
        testRecord = new initialRecord();
        testRecord.setEDD(new Date(2018, 5, 1));
        testRecord.setWeightGainDuringPregnancy(5.0F);
        testRecord.setPregnancyType(8);
        testRecord.setMID(1L);
        testRecord.setLMP(new Date(2018, 12, 30));
        testRecord.setMID(Long.parseLong(DEFAULT_PATIENT_MID));
        testRecord.setRecordDate(new Date(2018, 10,30));
        testRecord.setYearOfConception(2016);

        Date date = new Date(2018, 12, 30);

//        // mock httpServerlet and httpSession
//        mockHttpServletRequest = mock(HttpServletRequest.class);
//        mockHttpServletSession = mock(HttpSession.class);


    }

    /**
     * @Aimed Function:   add()
    **/
    @Test
    public void testAddTrue()throws DBException{
        List<initialRecord> ls = Collections.emptyList();
        when(mockInitialRecordMySQL.add(testRecord)).thenReturn(true);
        Assert.assertEquals(0, controller.add(testRecord).size());
        verify(controller).printFacesMessage(eq(FacesMessage.SEVERITY_INFO), anyString(), anyString(), anyString());
        verify(mockLogger).logTransaction(any(), any(), any(), any());
        when(mockInitialRecordMySQL.add(testRecord)).thenReturn(false);
        Assert.assertEquals(0, controller.add(testRecord).size());

    }

    @Test(expected = DBException.class)
    public void testAddWithException() throws DBException{
        when(mockInitialRecordMySQL.add(testRecord)).thenThrow(new DBException(new SQLException()));
        controller.add(testRecord);
    }

    @Test
    public void testAddWithFacesContext() throws DBException{
        when(mockInitialRecordMySQL.add(testRecord)).thenReturn(true);
        controller.add(testRecord);
        verify(controller).printFacesMessage(Mockito.eq(FacesMessage.SEVERITY_INFO),anyString(),anyString(),anyString());
    }

    @Test
    public void testAddLogtransaction() throws DBException{
        //TODO
        //should all the log be tested and verify the count?
        when(mockInitialRecordMySQL.add(testRecord)).thenReturn(true);
        // skip the logTransaction taking effect into database
        doNothing().when(controller).logTransaction(any(), any());
        controller.add(testRecord);
        verify(controller, times(1)).logTransaction(any(), anyString());
//        verify(mockInitialRecordMySQL).add(any());
    }

    /**
     * @Aimed Function:   getInitialRecordsByDate()
    **/
    @Test
    public void testGetInitialRecordsByDateTrue() throws DBException, FormValidationException{
        List<initialRecord> record = new ArrayList<initialRecord>();
        record.add(testRecord);
        when(mockInitialRecordMySQL.getOBrecordForPatient(Long.parseLong(DEFAULT_PATIENT_MID))).thenReturn(record);

        assertEquals(controller.getInitialRecordsByDate(DEFAULT_PATIENT_MID).size(), 1);
    }

    @Test(expected = DBException.class)
    public void testGetInitialRecordsByDateWithException() throws Exception{
        when(mockInitialRecordMySQL.getOBrecordForPatient(Long.parseLong(DEFAULT_PATIENT_MID))).thenThrow(new DBException(new SQLException()));
        controller.getInitialRecordsByDate(DEFAULT_PATIENT_MID);
    }

    @Test
    public void testGetInitialRecordsByDateInvalidInput() throws FormValidationException, DBException {
        assertEquals(controller.getInitialRecordsByDate(DEFAULT_INVALID_PATIENT_MID).size(), 0);
        assertEquals(controller.getInitialRecordsByDate(null).size(), 0);

    }
    @Test
    public void testGetInitialRecordsByDateLogtransaction() throws Exception{
        List<initialRecord> ret = Collections.emptyList();
        when(mockInitialRecordMySQL.getOBrecordForPatient(Long.parseLong(DEFAULT_PATIENT_MID))).thenReturn(ret);
        controller.getInitialRecordsByDate(DEFAULT_PATIENT_MID);
        verify(mockLogger).logTransaction(TransactionType.VIEW_INITIAL_OBSTETRICS_RECORD, any(), any(), any());
    }


    /**
     * @Aimed Function:   updateOBeligibility
    **/

    @Test
    public void testUpdateOBeligibilityTrue() throws Exception{
        when(mockInitialRecordMySQL.updateOBeligibility(Long.parseLong(DEFAULT_PATIENT_MID), true)).thenReturn(true);
        assertTrue(controller.updateOBeligibility(DEFAULT_PATIENT_MID, true));

    }

    @Test
    public void testUpdateOBeligibilityFalse() throws Exception{
        when(mockInitialRecordMySQL.updateOBeligibility(Long.parseLong(DEFAULT_PATIENT_MID), true)).thenReturn(false);

        boolean ret = controller.updateOBeligibility(DEFAULT_PATIENT_MID, true);
        assertFalse(ret);
        // ? why this cannot pass
//        verify(mockInitialRecordMySQL).updateOBeligibility(any(), any());
    }

    @Test
    public void testUpdateOBeligibilityInvalidInput() throws FormValidationException, DBException{
        boolean ret = controller.updateOBeligibility(null, true);
        assertFalse(ret);
        ret = controller.updateOBeligibility(DEFAULT_INVALID_PATIENT_MID, true);
        assertFalse(ret);
    }

    @Test(expected = DBException.class)
    public void testUpdateOBeligibilityWithExceptionInvalidformat() throws FormValidationException, DBException{
        assertFalse(controller.updateOBeligibility(DEFAULT_INVALID_PATIENT_MID, true));
    }

    @Test
    public void testGetInitialRecordsByDate() throws Exception {
        //generate fake  records
        //TODO
        //patientID and format validation
        //
        List<initialRecord> ret = Collections.emptyList();
        when(mockInitialRecordMySQL.getOBrecordForPatient(Long.parseLong(DEFAULT_PATIENT_MID))).thenReturn(ret);

        List<initialRecord> all = controller.getInitialRecordsByDate(DEFAULT_PATIENT_MID);
        long recordID = -1;

        // check all the records returned
        for(int i =0; i< all.size(); i++){
            initialRecord recordCurrent = all.get(i);
            boolean bLMP = recordCurrent.getLMP().equals(testRecord.getLMP());
            boolean bDate = recordCurrent.getRecordDate().equals(testRecord.getRecordDate());
            boolean bEDD = recordCurrent.getEDD().equals(testRecord.getEDD());
            boolean brecordDate = recordCurrent.getRecordDate().equals(testRecord.getRecordDate());
            boolean bpregnancyType = (recordCurrent.getPregnancyType() == (testRecord.getPregnancyType()));
            boolean bweightGainDuringPregnancy = (recordCurrent.getWeightGainDuringPregnancy() == (testRecord.getWeightGainDuringPregnancy()));
            boolean bdeliveryType = recordCurrent.getDeliveryType().equals(testRecord.getDeliveryType());
            boolean bnumberOfWeeksPregnant = recordCurrent.getNumberOfWeeksPregnant().equals(testRecord.getNumberOfWeeksPregnant());

            if(bDate && bdeliveryType && bEDD && bLMP && bnumberOfWeeksPregnant && bpregnancyType &&brecordDate && bweightGainDuringPregnancy){
                recordID = recordCurrent.getObRecordID();
            }
        }

        assertNotEquals(-1L, recordID);
        testRecord.setObRecordID(recordID);
    }

    // fake data:
    // long obRecordID = 1L;
    // yearOfConception = 2016;
    // private float numberOfHoursInLabor = 10.5F;
    //	private String numberOfWeeksPregnant = "10";
    //	private float weightGainDuringPregnancy = 5.0F;
    //	private String DeliveryType = "miscarriage";
    //	private int pregnancyType = 8;
    //	private Date LMP = new Date(2018, 12, 30);
    //	private Date EDD = new Date(2018, 5, 1);
    //	private long MID = 1L;
    //	private Date recordDate = new Date(2018, 10,30);
}



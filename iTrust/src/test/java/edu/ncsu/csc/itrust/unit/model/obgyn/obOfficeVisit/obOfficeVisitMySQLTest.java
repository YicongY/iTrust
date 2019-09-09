package edu.ncsu.csc.itrust.unit.model.obgyn.obOfficeVisit;


import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.obgyn.obOfficeVisit.obOfficeVisit;
import edu.ncsu.csc.itrust.model.obgyn.obOfficeVisit.obOfficeVisitMySQL;
import edu.ncsu.csc.itrust.model.officeVisit.OfficeVisit;
import edu.ncsu.csc.itrust.model.old.beans.ApptTypeBean;
import edu.ncsu.csc.itrust.model.old.beans.loaders.ApptTypeBeanLoader;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import junit.framework.TestCase;
import net.sf.cglib.core.Local;
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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.time.LocalDate;
import edu.ncsu.csc.itrust.model.old.beans.ApptBean;

/**
 * @author Shu
 *
 */
public class obOfficeVisitMySQLTest extends TestCase {
    //yicong's code start
    private DataSource ds;
    private  obOfficeVisitMySQL ovsql;
    private obOfficeVisit visit;
    private TestDataGenerator gen;


    @Mock
    private DataSource mockDataSource;
    @Mock
    private Connection mockConnection;
    @Mock
    private PreparedStatement mockPreparedStatement;
    @Mock
    private ResultSet mockResultSet;
    private obOfficeVisitMySQL mockIrsql;

    @Before
    public void setUp() throws Exception {
        ds = ConverterDAO.getDataSource();
        ovsql = new obOfficeVisitMySQL(ds);
        gen = new TestDataGenerator();
        gen.clearAllTables();
        gen.standardData();
        gen.uc94();
        visit = new obOfficeVisit();

        mockDataSource = Mockito.mock(DataSource.class);
        mockIrsql = new obOfficeVisitMySQL(mockDataSource);

        mockConnection = Mockito.mock(Connection.class);
        mockResultSet = Mockito.mock(ResultSet.class);
        mockPreparedStatement = Mockito.mock(PreparedStatement.class);
    }


//    @After
//    public void tearDown() throws FileNotFoundException, SQLException, IOException {
//        gen.clearAllTables();
//    }




    //
    //shu's code start
    public void testGetVisitsRecord() throws Exception {
        List<obOfficeVisit> list101 = ovsql.getOBOfficeVisit(101L);
        List<obOfficeVisit> list = ovsql.getOBOfficeVisit(1234);

        assertEquals(0, list101.size());
        assertEquals(1, list.size());
        obOfficeVisit ov = list.get(0);
        Assert.assertTrue(ov.getPatientMID() == 1234L);
        Assert.assertTrue(ov.getLocationID().equals("1"));
        Assert.assertTrue(ov.getApptTypeID() == 1);
        String str = "2018-11-30";
        LocalDate date = LocalDate.parse(str);
        Assert.assertEquals(ov.getCurrentDate(), date);
        Assert.assertEquals(ov.getNumOfWeeks(), "10-5");
        Assert.assertTrue(ov.getWeight() == 70.1f);
        Assert.assertEquals(ov.getBloodPressure(), "120/120");
        Assert.assertEquals(ov.getFetalHeartRate(), 6);
        Assert.assertEquals(ov.getNumOfMultiplePregnancy(), 2);
        Assert.assertEquals(ov.isMultiplePregnancy(), true);
        Assert.assertEquals(ov.isLowLyingPlacentaObserved(), true);
    }

    public void testGetOBOfficeVisitByVisitID() throws Exception {
        obOfficeVisit ov = ovsql.getOBOfficeVisitByVisitID(7L);
        Assert.assertTrue(ov.getPatientMID() == 1234L);
        Assert.assertTrue(ov.getLocationID().equals("1"));
        Assert.assertTrue(ov.getApptTypeID() == 1);
        String str = "2018-11-30";
        LocalDate date = LocalDate.parse(str);
        Assert.assertEquals(ov.getCurrentDate(), date);
        Assert.assertEquals(ov.getNumOfWeeks(), "10-5");
        Assert.assertTrue(ov.getWeight() == 70.1f);
        Assert.assertEquals(ov.getBloodPressure(), "120/120");
        Assert.assertEquals(ov.getFetalHeartRate(), 6);
        Assert.assertEquals(ov.getNumOfMultiplePregnancy(), 2);
        Assert.assertEquals(ov.isMultiplePregnancy(), true);
        Assert.assertEquals(ov.isLowLyingPlacentaObserved(), true);
    }

    public void testDeleteVisitRecord() throws Exception {
        List<obOfficeVisit> listBeforeDelete = ovsql.getOBOfficeVisit(1234L);
        Assert.assertTrue(listBeforeDelete.size() == 1);
        long visitID = listBeforeDelete.get(0).getVisiteId();
        ovsql.delete(visitID);
        List<obOfficeVisit> list1234L = ovsql.getOBOfficeVisit(1234L);
        Assert.assertTrue(list1234L.size() == 0);
    }

    public void testUpdateVisitRecord() throws Exception {
        List<obOfficeVisit> listBeforeUpdate = ovsql.getOBOfficeVisit(1234L);
        obOfficeVisit cur = listBeforeUpdate.get(0);
        cur.setWeight(80.1f);
        cur.setCurrentDate(LocalDate.now());
        ovsql.update(cur);
        List<obOfficeVisit> listAfterUpdate = ovsql.getOBOfficeVisit(1234L);
        obOfficeVisit newVisit = listAfterUpdate.get(0);
        Assert.assertTrue(newVisit.getWeight() == 80.1f);
    }

    public void testAddVisitRecord() throws Exception {
        obOfficeVisit cur = new obOfficeVisit();
        cur.setPatientMID(1234L);
        cur.setLocationID("2");
        cur.setApptTypeID(1);
        LocalDate date = LocalDate.now();
        cur.setCurrentDate(date);
        cur.setNumOfWeeks("10-5");
        cur.setBloodPressure("121/121");
        cur.setFetalHeartRate(6);
        cur.setMultiplePregnancy(true);
        cur.setNumOfMultiplePregnancy(2);
        cur.setLowLyingPlacentaObserved(true);
        cur.setWeight(80.1f);
        ovsql.add(cur);
        List<obOfficeVisit> listAfterAdd = ovsql.getOBOfficeVisit(1234L);
        Assert.assertTrue(listAfterAdd.size() == 2);
        obOfficeVisit ov = listAfterAdd.get(0);
        Assert.assertTrue(ov.getPatientMID() == 1234L);
        Assert.assertTrue(ov.getLocationID().equals("2"));
        Assert.assertTrue(ov.getApptTypeID() == 1);
        Assert.assertEquals(ov.getCurrentDate(), LocalDate.now());
        Assert.assertEquals(ov.getNumOfWeeks(), "10-5");
        Assert.assertTrue(ov.getWeight() == 80.1f);
        Assert.assertEquals(ov.getBloodPressure(), "121/121");
        Assert.assertEquals(ov.getFetalHeartRate(), 6);
        Assert.assertEquals(ov.getNumOfMultiplePregnancy(), 2);
        Assert.assertEquals(ov.isMultiplePregnancy(), true);
        Assert.assertEquals(ov.isLowLyingPlacentaObserved(), true);
    }
}

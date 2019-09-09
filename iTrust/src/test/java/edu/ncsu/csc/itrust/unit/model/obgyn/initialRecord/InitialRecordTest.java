package edu.ncsu.csc.itrust.unit.model.obgyn.initialRecord;

import edu.ncsu.csc.itrust.model.obgyn.initialRecord.initialRecord;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Date;

/**
 * @author Ziyu & Shu
 *
 */
public class InitialRecordTest {
    initialRecord record;

    @Before
    public void setUp() throws Exception {
        record = new initialRecord();
    }


    @Test
    public void testGetObRecordID() {
        record.setObRecordID(1L);
        Assert.assertTrue(record.getObRecordID() == 1L);
    }

    @Test
    public void testSetObRecordID() {
        record.setObRecordID(1L);
        Assert.assertTrue(record.getObRecordID() == 1L);
    }

    @Test
    public void testSetYearOfConception() {
        record.setYearOfConception(1);
        Assert.assertEquals(1, record.getYearOfConception());
    }

    @Test
    public void testGetYearOfConception() {
        record.setYearOfConception(1);
        Assert.assertEquals(1, record.getYearOfConception());
    }


    @Test
    public void testGetNumberOfHoursInLabor() {
        record.setNumberOfHoursInLabor(10);
        Assert.assertTrue(record.getNumberOfHoursInLabor() == 10);
    }

    @Test
    public void testSetNumberOfHoursInLabor() {
        record.setNumberOfHoursInLabor(10);
        Assert.assertTrue(record.getNumberOfHoursInLabor() == 10);
    }


    @Test
    public void testGetNumberOfWeeksPregnant() {
        record.setNumberOfWeeksPregnant("10-2");
        Assert.assertEquals(record.getNumberOfWeeksPregnant(), "10-2");
    }

    @Test
    public void testSetNumberOfWeeksInLabor() {
        record.setNumberOfWeeksPregnant("10-2");
        Assert.assertEquals(record.getNumberOfWeeksPregnant(), "10-2");
    }

    @Test
    public void testGetWeightGainDuringPregnancy() {
        record.setWeightGainDuringPregnancy(10);
        Assert.assertTrue(record.getWeightGainDuringPregnancy() == 10);
    }

    @Test
    public void testSetWeightGainDuringPregnancy() {
        record.setWeightGainDuringPregnancy(10);
        Assert.assertTrue(record.getWeightGainDuringPregnancy() == 10);
    }

    @Test
    public void testGetDeliveryType() {
        record.setDeliveryType("miscarriage");
        Assert.assertEquals("miscarriage", record.getDeliveryType());
    }

    @Test
    public void testSetDeliveryType() {
        record.setDeliveryType("miscarriage");
        Assert.assertEquals("miscarriage", record.getDeliveryType());
    }

    @Test
    public void testGetPregnancyType() {
        record.setPregnancyType(1);
        Assert.assertTrue(record.getPregnancyType() == 1);
    }

    @Test
    public void testSetPregnancyType() {
        record.setPregnancyType(1);
        Assert.assertTrue(record.getPregnancyType() == 1);
    }

    @Test
    public void testGetLMP() {
        String str = "2018-10-28";
        Date date = Date.valueOf(str);
        record.setLMP(date);
        Assert.assertEquals(date, record.getLMP());
    }

    @Test
    public void testSetLMP() {
        String str = "2018-10-28";
        Date date = Date.valueOf(str);
        record.setLMP(date);
        Assert.assertEquals(date, record.getLMP());
    }

    @Test
    public void testGetEDD() {
        String str = "2018-10-28";
        Date date = Date.valueOf(str);
        record.setEDD(date);
        Assert.assertEquals(date, record.getEDD());
    }

    @Test
    public void testSetEDD() {
        String str = "2018-10-28";
        Date date = Date.valueOf(str);
        record.setEDD(date);
        Assert.assertEquals(date, record.getEDD());
    }

    @Test
    public void testGetMID() {
        record.setMID(1L);
        Assert.assertTrue(record.getMID() == 1L);
    }

    @Test
    public void testSetMID() {
        record.setMID(1L);
        Assert.assertTrue(record.getMID() == 1L);
    }

    @Test
    public void testGetRecordDate() {
        String str = "2018-10-28";
        Date date = Date.valueOf(str);
        record.setRecordDate(date);
        Assert.assertEquals(date, record.getRecordDate());
    }

    @Test
    public void testSetRecordDate() {
        String str = "2018-10-28";
        Date date = Date.valueOf(str);
        record.setRecordDate(date);
        Assert.assertEquals(date, record.getRecordDate());
    }

}

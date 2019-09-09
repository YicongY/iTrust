package edu.ncsu.csc.itrust.unit.model.obgyn.report;

import edu.ncsu.csc.itrust.model.obgyn.report.pastPregnancy;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Shu
 *
 */
public class pastPregnancyTest {
    pastPregnancy pp;

    @Before
    public void setUp() {
        pp = new pastPregnancy();
    }

    @Test
    public void obVisitInfoTest() {
        pp.setPregnancyTerm(20);
        Assert.assertEquals(pp.getPregnancyTerm(), 20);
    }

    @Test
    public void conceptionYearTest() {
        pp.setConceptionYear(2017);
        Assert.assertEquals(pp.getConceptionYear(), 2017);
    }

    @Test
    public void deliveryTypeTest() {
        pp.setDeliveryType("vagina delivery");
        Assert.assertEquals(pp.getDeliveryType(), "vagina delivery");
    }

    @Test
    public void patientMIDTest() {
        pp.setPatientMID(1L);
        Assert.assertEquals(pp.getPatientMID(), 1L);
    }

}

package edu.ncsu.csc.itrust.unit.model.obgyn.obOfficeVisit;

//import com.sun.source.tree.AssertTree;
import edu.ncsu.csc.itrust.model.obgyn.obOfficeVisit.obOfficeVisit;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;


/**
 * @author Shu
 *
 */
public class obOfficeVisitTest {
    obOfficeVisit visit;

    @Before
    public void setUp() throws Exception {
        visit = new obOfficeVisit();
    }

    @Test
    public void testVisitID() {
        visit.setVisiteId(1L);
        Assert.assertTrue(visit.getVisiteId() == 1L);
    }

    @Test
    public void testPatientMID() {
        visit.setPatientMID(1L);
        Assert.assertTrue(visit.getPatientMID() == 1L);
    }

    @Test
    public void testLocationID() {
        visit.setLocationID("1");
        Assert.assertTrue(visit.getLocationID().equals("1"));
    }

    @Test
    public void testAppTypeID() {
        visit.setApptTypeID(1);
        Assert.assertTrue(visit.getApptTypeID() == 1);
    }

    @Test
    public void testCurrentDate() {
        String str = "2018-11-29";
        LocalDate date = LocalDate.parse(str);
        visit.setCurrentDate(date);
        Assert.assertEquals(date, visit.getCurrentDate());
    }

    @Test
    public void testNumOfWeeks() {
        visit.setNumOfWeeks("20-5");
        Assert.assertTrue(visit.getNumOfWeeks().equals("20-5"));
    }

    @Test
    public void testWeight() {
        visit.setWeight(70);
        Assert.assertTrue(visit.getWeight() == 70);
    }

    @Test
    public void testBloodPressure() {
        visit.setBloodPressure("120");
        Assert.assertTrue(visit.getBloodPressure().equals("120"));
    }

    @Test
    public void testFetalHeartRate() {
        visit.setFetalHeartRate(130);
        Assert.assertTrue(visit.getFetalHeartRate() == 130);
    }

    @Test
    public void testMultiplePregnancy() {
        visit.setMultiplePregnancy(true);
        Assert.assertTrue(visit.isMultiplePregnancy() == true);
    }

    @Test
    public void testNumOfPregnancy() {
        visit.setNumOfMultiplePregnancy(2);
        Assert.assertTrue(visit.getNumOfMultiplePregnancy() == 2);
    }

    @Test
    public void testLowLyingPlacement() {
        visit.setLowLyingPlacentaObserved(true);
        Assert.assertTrue(visit.isLowLyingPlacentaObserved() == true);
    }
}
package edu.ncsu.csc.itrust.unit.model.obgyn.report;

import edu.ncsu.csc.itrust.model.obgyn.report.obVisitInfo;
import edu.ncsu.csc.itrust.model.obgyn.report.pastPregnancy;
import edu.ncsu.csc.itrust.model.obgyn.report.report;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Shu
 *
 */
public class reportTest {
    report rp;

    @Before
    public void setUp() {
        rp = new report();
    }

    @Test
    public void bloodTypeTest() {
        rp.setBloodType("RH-");
        Assert.assertEquals(rp.getBloodType(), "RH-");
    }

    @Test
    public void patientMIDTest() {
        rp.setPatientMID(1L);
        Assert.assertEquals(rp.getPatientMID(), 1L);
    }

    @Test
    public void EDDTest() {
        rp.setEDD(LocalDate.parse("2018-12-25"));
        LocalDate date = LocalDate.parse("2018-12-25");
        Assert.assertEquals(date, rp.getEDD());
    }

    @Test
    public void pastPregnancyListTest() {
        List<pastPregnancy> list = new ArrayList<>();
        pastPregnancy p1 = new pastPregnancy();
        pastPregnancy p2 = new pastPregnancy();
        list.add(p1);
        list.add(p2);
        rp.setPastPregnancyList(list);
        Assert.assertTrue(rp.getPastPregnancyList().size() == 2);
    }

    @Test
    public void obVisitInfoListTest() {
        List<obVisitInfo> list = new ArrayList<>();
        obVisitInfo ov1 = new obVisitInfo();
        obVisitInfo ov2 = new obVisitInfo();
        list.add(ov1);
        list.add(ov2);
        rp.setObVisitInfoList(list);
        Assert.assertTrue(rp.getObVisitInfoList().size() == 2);
    }
}

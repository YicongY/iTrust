package edu.ncsu.csc.itrust.unit.model.obgyn.childBirthVisit;
import edu.ncsu.csc.itrust.model.obgyn.childBirthVisit.ChildBirthVisit;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

import java.sql.Date;
import java.sql.Time;

/**
 * @ClassName:    ChildBirthVisitTest
 * @Description:  uc96 Model Test
 * @Author:       Xiaocong Yu
 **/
public class ChildBirthVisitTest extends TestCase{
    
    private ChildBirthVisit cbv;

    @Before
    protected void setUp() throws Exception {
        super.setUp();
        cbv = new ChildBirthVisit();
    }

    /**
     * test for setters and getters
     */
    @Test
    public void testSet(){
        cbv.setChildBirthVisitID(1L);
        cbv.setPatientMID(800L);
        cbv.setDeliveryDate(Date.valueOf("2018-11-26"));
        cbv.setDeliveryTime(Time.valueOf("12:00:00"));
        cbv.setDeliveryType("vaginal delivery");
        cbv.setPreferredDeliveryType("vaginal delivery");
        cbv.setVisitType("emergency");
        cbv.setNumberOfBoyBabies(1);
        cbv.setNumberOfGirlBabies(1);
        cbv.setDosage_epiduralAnaesthesia(1);
        cbv.setDosage_magnesiumSulfate(1);
        cbv.setDosage_pethidine(1);
        cbv.setDosage_nitrousOxide(1);
        cbv.setDosage_pitocin(1);
        cbv.setDosage_rhImmuneGlobulin(1);
        cbv.setComment("fake cmmt");

        assertEquals(1L, (long)cbv.getChildBirthVisitID());
        assertEquals(800L, (long) cbv.getPatientMID());
        assertEquals(Date.valueOf("2018-11-26"), cbv.getDeliveryDate());
        assertEquals(Time.valueOf("12:00:00"), cbv.getDeliveryTime());
        assertEquals("vaginal delivery", cbv.getDeliveryType());
        assertEquals("vaginal delivery", cbv.getPreferredDeliveryType());
        assertEquals("emergency", cbv.getVisitType());
        assertEquals(1, cbv.getNumberOfBoyBabies());
        assertEquals(1, cbv.getNumberOfGirlBabies());
        assertEquals(1, cbv.getDosage_epiduralAnaesthesia());
        assertEquals(1, cbv.getDosage_magnesiumSulfate());
        assertEquals(1, cbv.getDosage_pethidine());
        assertEquals(1, cbv.getDosage_nitrousOxide());
        assertEquals(1, cbv.getDosage_pitocin());
        assertEquals(1, cbv.getDosage_rhImmuneGlobulin());
        assertEquals("fake cmmt", cbv.getComment());

    }

    
}
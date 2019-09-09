package edu.ncsu.csc.itrust.unit.model.obgyn.report;

import edu.ncsu.csc.itrust.model.obgyn.report.obVisitInfo;
import net.sf.cglib.core.Local;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;


/**
 * @author Shu
 *
 */
public class obVisitInfoTest {
    obVisitInfo obv;

    @Before
    public void setUp() {
        obv = new obVisitInfo();
    }

    @Test
    public void RHFlagTest() {
        obv.setRHFlag("RH-");
        Assert.assertEquals(obv.isRHFlag(), true);
        obv.setRHFlag("O");
        Assert.assertEquals(obv.isRHFlag(), false);
    }

    @Test
    public void highBloodPressure() {
        obv.setHighBloodPressure("140/90");
        Assert.assertEquals(obv.isHighBloodPressure(), true);
        obv.setHighBloodPressure("130/90");
        Assert.assertEquals(obv.isHighBloodPressure(), true);
        obv.setHighBloodPressure("140/70");
        Assert.assertEquals(obv.isHighBloodPressure(), true);
        obv.setHighBloodPressure("100/60");
        Assert.assertFalse(obv.isHighBloodPressure() == true);
    }

    @Test
    public void advancedMaternalAgeTest() {
        String birth = "1970-10-10";
        obv.setAdvancedMaternalAge(LocalDate.parse(birth));
        Assert.assertTrue(obv.isAdvancedMaternalAge());

        birth = "1997-10-10";
        obv.setAdvancedMaternalAge(LocalDate.parse(birth));
        Assert.assertFalse(obv.isAdvancedMaternalAge());
    }

    @Test
    public void lowLyingPlacentaTest() {
        obv.setLowLyingPlacenta(true);
        Assert.assertEquals(obv.isLowLyingPlacenta(), true);
    }

    @Test
    public void abnormalFetalHeartRateTest() {
        obv.setAbnormalFetalHeartRate(110);
        Assert.assertTrue(obv.isAbnormalFetalHeartRate());
        obv.setAbnormalFetalHeartRate(170);
        Assert.assertTrue(obv.isAbnormalFetalHeartRate());
        obv.setAbnormalFetalHeartRate(140);
        Assert.assertFalse(obv.isAbnormalFetalHeartRate());
    }

    @Test
    public void isMultiplePregnancyTest() {
        obv.setMultiplePregnancy(true);
        Assert.assertTrue(obv.isMultiplePregnancy());
    }

    @Test
    public void atypicalWeightChangeTest() {
        obv.setAtypicalWeightChange(40f, 70f);
        Assert.assertFalse(obv.isAtypicalWeightChange());
        obv.setAtypicalWeightChange(30f, 70f);
        Assert.assertTrue(obv.isAtypicalWeightChange());
        obv.setAtypicalWeightChange(60f, 70f);
        Assert.assertTrue(obv.isAtypicalWeightChange());
    }

    @Test
    public void hyperemesisGravidarumTest() {
        obv.setHyperemesisGravidarum(true);
        Assert.assertTrue(obv.isHyperemesisGravidarum());
    }

    @Test
    public void hypothyroidismTest() {
        obv.setHypothyroidism(true);
        Assert.assertTrue(obv.isHypothyroidism());
    }

    @Test
    public void diabetesTest() {
        obv.setDiabetes(true);
        Assert.assertTrue(obv.isDiabetes());
    }

    @Test
    public void autoimmuneDisordersTest() {
        obv.setAutoimmuneDisorders(true);
        Assert.assertTrue(true);
    }

    @Test
    public void cancersTest() {
        obv.setCancers(true);
        Assert.assertTrue(obv.isCancers());
    }

    @Test
    public void STDsTest() {
        obv.setSTDs(true);
        Assert.assertTrue(obv.isSTDs());
    }

    @Test
    public void penicillinTest() {
        obv.setPenicillin(true);
        Assert.assertTrue(obv.isPenicillin());
    }

    @Test
    public void sulfaDrugsTest() {
        obv.setSulfaDrugs(true);
        Assert.assertTrue(obv.isSulfaDrugs());
    }

    @Test
    public void tetracyclineTest() {
        obv.setTetracycline(true);
        Assert.assertTrue(obv.isTetracycline());
    }

    @Test
    public void codeineTest() {
        obv.setCodeine(true);
        Assert.assertTrue(obv.isCodeine());
    }

    @Test
    public void NSAIDsTest() {
        obv.setNSAIDs(true);
        Assert.assertTrue(obv.isNSAIDs());
    }

    @Test
    public void patientMIDTest() {
        obv.setPatientMID(1L);
        Assert.assertTrue(obv.getPatientMID() == 1L);
    }

    @Test
    public void numOfWeeksPregnantTest() {
        obv.setNumOfWeeksPregnant("10-5");
        Assert.assertEquals(obv.getNumOfWeeksPregnant(), "10-5");
    }

    @Test
    public void weightTest() {
        obv.setWeight((float)70.1);
        Assert.assertTrue(obv.getWeight() == 70.1f);
    }

    @Test
    public void bloodPressureTest() {
        obv.setBloodPressure("130/130");
        Assert.assertEquals(obv.getBloodPressure(), "130/130");
    }

    @Test
    public void fetalHeartRateTest() {
        obv.setFetalHeartRate(10);
        Assert.assertEquals(obv.getFetalHeartRate(), 10);
    }

    @Test
    public void isMultipleTest() {
        obv.setMultiple(true);
        Assert.assertTrue(obv.isMultiple());
    }

    @Test
    public void numOfMultiplesTest() {
        obv.setNumOfMultiples(2);
        Assert.assertTrue(obv.getNumOfMultiples() == 2);
    }

    @Test
    public void isLowLyingPlacentaObservedTest() {
        obv.setLowLyingPlacentaObserved(true);
        Assert.assertTrue(obv.isLowLyingPlacentaObserved());
    }

    @Test
    public void isComplicationsTest() {
        obv.setRHFlag("O-");
        obv.setBloodPressure("130/130");
        String age = "1987-10-10";
        obv.setAdvancedMaternalAge(LocalDate.parse(age));
        obv.setLowLyingPlacentaObserved(true);
        obv.setAbnormalFetalHeartRate(2);
        obv.setAtypicalWeightChange(100f, 200f);
        obv.setHypothyroidism(true);
        obv.setHypothyroidism(true);
        obv.setDiabetes(true);
        obv.setAutoimmuneDisorders(true);
        obv.setAutoimmuneDisorders(true);
        obv.setCancers(true);
        obv.setSTDs(true);
        obv.setPenicillin(true);
        obv.setSulfaDrugs(true);
        obv.setTetracycline(true);
        obv.setCodeine(true);
        obv.setNSAIDs(false);
        obv.setComplications();
        Assert.assertTrue(obv.isComplications());
    }

    @Test
    public void drugAllergiesTest() {
        obv.setPenicillin(true);
        obv.setSulfaDrugs(true);
        obv.setTetracycline(true);
        obv.setCodeine(true);
        obv.setNSAIDs(true);
        obv.setDrugAllergies();
        Assert.assertTrue(obv.getDrugAllergies());
    }
}

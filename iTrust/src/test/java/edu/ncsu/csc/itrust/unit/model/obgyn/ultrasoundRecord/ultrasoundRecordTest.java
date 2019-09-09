package edu.ncsu.csc.itrust.unit.model.obgyn.ultrasoundRecord;

import edu.ncsu.csc.itrust.model.obgyn.ultrasoundRecord.ultrasoundRecord;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.awt.image.BufferedImage;
import java.io.InputStream;

public class ultrasoundRecordTest {
    ultrasoundRecord ur;

    @Before
    public void setUp() throws Exception {
        ur = new ultrasoundRecord();
    }

    @Test
    public void testRecordID() {
        ur.setRecordID(1L);
        Assert.assertTrue(ur.getRecordID() == 1L);
    }

    @Test
    public void testObOfficeVisitID() {
        ur.setObOfficeVisitID(1L);
        Assert.assertTrue(ur.getObOfficeVisitID() == 1L);
    }

    @Test
    public void testPatientMID() {
        ur.setPatientMID(1L);
        Assert.assertTrue(ur.getPatientMID() == 1L);
    }

    @Test
    public void testLocationID() {
        ur.setLocationID("1");
        Assert.assertTrue(ur.getLocationID().equals("1"));
    }

    @Test
    public void testCrownRumpLength() {
        ur.setCrownRumpLength(3.5f);
        Assert.assertTrue(ur.getCrownRumpLength() == 3.5f);
    }

    @Test
    public void testBiparietalDiameter() {
        ur.setBiparietalDiameter(10.0f);
        Assert.assertTrue(ur.getBiparietalDiameter() == 10.0f);
    }

    @Test
    public void testHeadCircumference() {
        ur.setHeadCircumference(10.0f);
        Assert.assertTrue(ur.getHeadCircumference() == 10.0f);
    }

    @Test
    public void testFemurLength() {
        ur.setFemurLength(10.0f);
        Assert.assertTrue(ur.getFemurLength() == 10.0f);
    }

    @Test
    public void testOccipitofrontalDiameter() {
        ur.setOccipitofrontalDiameter(10.0f);
        Assert.assertTrue(ur.getOccipitofrontalDiameter() == 10.0f);
    }

    @Test
    public void testAbdominalCircumference() {
        ur.setAbdominalCircumference(10.0f);
        Assert.assertTrue(ur.getAbdominalCircumference() == 10.0f);
    }

    @Test
    public void testHumerusLength() {
        ur.setHumerusLength(10.0f);
        Assert.assertTrue(ur.getHumerusLength() == 10.0f);
    }

    @Test
    public void testEstimatedFetalWeight() {
        ur.setEstimatedFetalWeight(10.0f);
        Assert.assertTrue(ur.getEstimatedFetalWeight() == 10.0f);
    }

    @Test
    public void testImageFormat() {
        ur.setImageFormat("jpg");
        Assert.assertTrue(ur.getImageFormat().equals("jpg"));
    }

    @Test
    public void testImage() throws Exception {
        // Create a fake buffered image and convert it to input stream
        BufferedImage imgExpected = new BufferedImage(
                100, 100,
                BufferedImage.TYPE_3BYTE_BGR
        );
        InputStream streamExpected = ur.imageToInputStream(imgExpected);
        ur.setImageStream(streamExpected);

        // Get the actual image
        InputStream streamActual = ur.getImageStream();
        Assert.assertTrue(streamExpected.toString().equals(streamActual.toString()));
    }
}

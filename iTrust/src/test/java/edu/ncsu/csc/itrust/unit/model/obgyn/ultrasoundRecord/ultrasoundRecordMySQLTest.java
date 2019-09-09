package edu.ncsu.csc.itrust.unit.model.obgyn.ultrasoundRecord;

import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.obgyn.ultrasoundRecord.ultrasoundRecord;
import edu.ncsu.csc.itrust.model.obgyn.ultrasoundRecord.ultrasoundRecordMySQL;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;
import junit.framework.TestCase;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.sql.DataSource;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

/**
 * @author ziyu
 */
public class ultrasoundRecordMySQLTest extends TestCase {
    private DataSource ds;
    private ultrasoundRecordMySQL ursql;
    private ultrasoundRecord record;
    private TestDataGenerator gen;

    @Before
    public void setUp() throws Exception {
        ds = ConverterDAO.getDataSource();
        ursql = new ultrasoundRecordMySQL(ds);
        record = new ultrasoundRecord();
        gen = new TestDataGenerator();
        gen.clearAllTables();
        gen.uc94();
    }

    @After
    public void tearDown() throws FileNotFoundException, SQLException, IOException {
        gen.clearAllTables();
    }

    @Test
    public void testGetUltrasoundRecordForPatient() throws Exception {
        // Load the image same as the one inserted into DB
        String hexImage = "89504E470D0A1A0A0000000D494844520000001000000010080200000090916836000000017352474200AECE1CE90000000467414D410000B18F0BFC6105000000097048597300000EC300000EC301C76FA8640000001E49444154384F6350DAE843126220493550F1A80662426C349406472801006AC91F1040F796BD0000000049454E44AE426082";
        if (hexImage.length() % 2 == 1)
            hexImage += "0";
        char[] charArr= hexImage.toCharArray();
        byte[] byteArray = Hex.decodeHex(charArr);
        InputStream streamExpected = new ByteArrayInputStream(byteArray);

        // Get the data from DB
        List<ultrasoundRecord> recordList = ursql.getUltrasoundRecordForPatient(1234);
        Assert.assertEquals(1, recordList.size());

        // Test the data
        ultrasoundRecord ur = recordList.get(0);

        Assert.assertTrue(ur.getPatientMID() == 1234L);
//        Assert.assertTrue(ur.getLocationID().equals("1"));
        Assert.assertTrue(ur.getCrownRumpLength() == 3.0f);
        Assert.assertTrue(ur.getBiparietalDiameter() == 3.0f);
        Assert.assertTrue(ur.getHeadCircumference() == 3.0f);
        Assert.assertTrue(ur.getFemurLength() == 3.0f);
        Assert.assertTrue(ur.getOccipitofrontalDiameter() == 3.0f);
        Assert.assertTrue(ur.getAbdominalCircumference() == 3.0f);
        Assert.assertTrue(ur.getHumerusLength() == 3.0f);
        Assert.assertTrue(ur.getEstimatedFetalWeight() == 3.0f);
        Assert.assertTrue(ur.getImageFormat().equals("png"));

        InputStream streamActual = ur.getImageStream();

        byte[] arrExpected = IOUtils.toByteArray(streamExpected);
        byte[] arrActual = IOUtils.toByteArray(streamActual);
        Assert.assertTrue(Arrays.equals(arrExpected, arrActual));
    }

    @Test
    public void testAdd() throws Exception {
        ultrasoundRecord ur = new ultrasoundRecord();
        ur.setPatientMID(1234L);
        ur.setLocationID("1");
        ur.setCrownRumpLength(10.0f);
        ur.setBiparietalDiameter(10.0f);
        ur.setHeadCircumference(10.0f);
        ur.setFemurLength(10.0f);
        ur.setOccipitofrontalDiameter(10.0f);
        ur.setAbdominalCircumference(10.0f);
        ur.setHumerusLength(10.0f);
        ur.setEstimatedFetalWeight(10.0f);
        ur.setImageFormat("png");

        BufferedImage imgExpected = new BufferedImage(
                100, 100,
                BufferedImage.TYPE_3BYTE_BGR
        );
        InputStream streamExpected = ur.imageToInputStream(imgExpected);
        ur.setImageStream(streamExpected);

        boolean success = ursql.add(ur);
        Assert.assertTrue(success);

        List<ultrasoundRecord> listAfterAdd = ursql.getUltrasoundRecordForPatient(1234L);
        ultrasoundRecord urAdded = listAfterAdd.get(0);

        Assert.assertTrue(ur.getPatientMID() == 1234L);
        Assert.assertTrue(ur.getLocationID().equals("1"));
        Assert.assertTrue(ur.getCrownRumpLength() == 10.0f);
        Assert.assertTrue(ur.getBiparietalDiameter() == 10.0f);
        Assert.assertTrue(ur.getHeadCircumference() == 10.0f);
        Assert.assertTrue(ur.getFemurLength() == 10.0f);
        Assert.assertTrue(ur.getOccipitofrontalDiameter() == 10.0f);
        Assert.assertTrue(ur.getAbdominalCircumference() == 10.0f);
        Assert.assertTrue(ur.getHumerusLength() == 10.0f);
        Assert.assertTrue(ur.getEstimatedFetalWeight() == 10.0f);
        Assert.assertTrue(ur.getImageFormat().equals("png"));

        InputStream streamActual = ur.getImageStream();
        Assert.assertTrue(streamExpected.toString().equals(streamActual.toString()));

//        byte[] arrExpected = IOUtils.toByteArray(streamExpected);
//        byte[] arrActual = IOUtils.toByteArray(streamActual);
//        Assert.assertTrue(Arrays.equals(arrExpected, arrActual));

//        BufferedImage imgAcutal = ur.getImage();
//        byte[] arrExpected = ((DataBufferByte) imgExpected.getData().getDataBuffer()).getData();
//        byte[] arrActual = ((DataBufferByte) imgAcutal.getData().getDataBuffer()).getData();
//        Assert.assertTrue(Arrays.equals(arrExpected, arrActual));
    }

}

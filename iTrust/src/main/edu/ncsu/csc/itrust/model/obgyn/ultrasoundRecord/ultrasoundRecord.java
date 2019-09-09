package edu.ncsu.csc.itrust.model.obgyn.ultrasoundRecord;

import javax.faces.bean.ManagedBean;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 *@author Ziyu
 */
@ManagedBean(name="ultrasoundRecord")
public class ultrasoundRecord {
    private long recordID;
    private long obOfficeVisitID;
    private long patientMID;
    private String locationID;

    private float crownRumpLength;
    private float biparietalDiameter;
    private float headCircumference;
    private float femurLength;
    private float occipitofrontalDiameter;
    private float abdominalCircumference;
    private float humerusLength;
    private float estimatedFetalWeight;

//    private BufferedImage image;
    private InputStream imageStream;
    private String imageFormat;

    public void setImageFormat(String imageFormat) {
        this.imageFormat = imageFormat;
    }

    public String getImageFormat() {
        return this.imageFormat;
    }

//    public void setImage(BufferedImage imgBuff) {
//        this.image = imgBuff;
//    }

    public void setImageStream(InputStream imageStream) {
        this.imageStream = imageStream;
    }

    public InputStream imageToInputStream(BufferedImage imgBuff) {
        String imgFormat = "png";
        if (this.getImageFormat() != null) {
            imgFormat = this.getImageFormat();
        }

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            ImageIO.write(imgBuff, imgFormat, os);
        } catch (Exception e) { }

        return new ByteArrayInputStream(os.toByteArray());
    }

    public BufferedImage inputStreamToBufferedImage(InputStream stream) {
        try {
            return ImageIO.read(stream);
        } catch (Exception e) { }
        return null;
    }

//    public BufferedImage getImage() {
//        return this.image;
//    }

    public InputStream getImageStream() {
        return this.imageStream;
    }

    public long getRecordID() {
        return recordID;
    }

    public long getObOfficeVisitID() {
        return obOfficeVisitID;
    }

    public long getPatientMID() {
        return patientMID;
    }

    public String getLocationID() {
        return locationID;
    }

    public float getCrownRumpLength() {
        return crownRumpLength;
    }

    public float getBiparietalDiameter() {
        return biparietalDiameter;
    }

    public float getHeadCircumference() {
        return headCircumference;
    }

    public float getFemurLength() {
        return femurLength;
    }

    public float getOccipitofrontalDiameter() {
        return occipitofrontalDiameter;
    }

    public float getAbdominalCircumference() {
        return abdominalCircumference;
    }

    public float getHumerusLength() {
        return humerusLength;
    }

    public float getEstimatedFetalWeight() {
        return estimatedFetalWeight;
    }

    public void setRecordID(long recordID) {
        this.recordID = recordID;
    }

    public void setObOfficeVisitID(long obOfficeVisitID) {
        this.obOfficeVisitID = obOfficeVisitID;
    }

    public void setPatientMID(long patientMID) {
        this.patientMID = patientMID;
    }

    public void setLocationID(String locationID) {
        this.locationID = locationID;
    }

    public void setCrownRumpLength(float crownRumpLength) {
        this.crownRumpLength = crownRumpLength;
    }

    public void setBiparietalDiameter(float biparietalDiameter) {
        this.biparietalDiameter = biparietalDiameter;
    }

    public void setHeadCircumference(float headCircumference) {
        this.headCircumference = headCircumference;
    }

    public void setFemurLength(float femurLength) {
        this.femurLength = femurLength;
    }

    public void setOccipitofrontalDiameter(float occipitofrontalDiameter) {
        this.occipitofrontalDiameter = occipitofrontalDiameter;
    }

    public void setAbdominalCircumference(float abdominalCircumference) {
        this.abdominalCircumference = abdominalCircumference;
    }

    public void setHumerusLength(float humerusLength) {
        this.humerusLength = humerusLength;
    }

    public void setEstimatedFetalWeight(float estimatedFetalWeight) {
        this.estimatedFetalWeight = estimatedFetalWeight;
    }
}

package edu.ncsu.csc.itrust.model.obgyn.ultrasoundRecord;

import com.mysql.jdbc.Statement;
import edu.ncsu.csc.itrust.model.SQLLoader;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ultrasoundRecordSQLLoader implements SQLLoader<ultrasoundRecord>{
    /**
     * {@inheritDoc}
     */
    @Override
    public List<ultrasoundRecord> loadList(ResultSet rs) throws SQLException {
        ArrayList<ultrasoundRecord> list = new ArrayList<ultrasoundRecord>();
        while (rs.next()) {
            list.add(loadSingle(rs));
        }
        return list;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ultrasoundRecord loadSingle(ResultSet rs) throws SQLException {
        ultrasoundRecord retRecord = new ultrasoundRecord();

        retRecord.setRecordID(rs.getLong("recordID"));
        //retRecord.setRecordID(rs.getLong("obOfficeVisitID"));
        retRecord.setPatientMID(rs.getLong("patientMID"));
        //retRecord.setLocationID(rs.getString("locationID"));
        retRecord.setCrownRumpLength(rs.getFloat("crownRumpLength"));
        retRecord.setBiparietalDiameter(rs.getFloat("biparietalDiameter"));
        retRecord.setHeadCircumference(rs.getFloat("headCircumference"));
        retRecord.setFemurLength(rs.getFloat("femurLength"));
        retRecord.setOccipitofrontalDiameter(rs.getFloat("occipitofrontalDiameter"));
        retRecord.setAbdominalCircumference(rs.getFloat("abdominalCircumference"));
        retRecord.setHumerusLength(rs.getFloat("humerusLength"));
        retRecord.setEstimatedFetalWeight(rs.getFloat("estimatedFetalWeight"));

        retRecord.setImageFormat(rs.getString("imageFormat"));

        InputStream stream = rs.getBinaryStream("image");
        retRecord.setImageStream(stream);
//        BufferedImage image = retRecord.inputStreamToBufferedImage(stream);
//        retRecord.setImage(image);

        return retRecord;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PreparedStatement loadParameters(Connection conn, PreparedStatement ps, ultrasoundRecord ur, boolean newInstance)
            throws SQLException {
        String stmt = "";
        if (newInstance) {
            stmt = "INSERT INTO UltrasoundRecord (obOfficeVisitID,patientMID, locationID,"
                    + "crownRumpLength, biparietalDiameter, headCircumference, femurLength,"
                    + "occipitofrontalDiameter, abdominalCircumference,"
                    + "humerusLength, estimatedFetalWeight, image, imageFormat)"
                    + "VALUES ((SELECT MAX(visitID) FROM OBOfficeVisit WHERE OBOfficeVisit.patientMID = ?),"
                    + " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?);";
        }
        ps = conn.prepareStatement(stmt, Statement.RETURN_GENERATED_KEYS);
        ps.setLong(1, ur.getPatientMID());
        ps.setLong(2, ur.getPatientMID());
        ps.setString(3, ur.getLocationID());
        ps.setFloat(4, ur.getCrownRumpLength());
        ps.setFloat(5, ur.getBiparietalDiameter());
        ps.setFloat(6, ur.getHeadCircumference());
        ps.setFloat(7, ur.getFemurLength());
        ps.setFloat(8, ur.getOccipitofrontalDiameter());
        ps.setFloat(9, ur.getAbdominalCircumference());
        ps.setFloat(10, ur.getHumerusLength());
        ps.setFloat(11, ur.getEstimatedFetalWeight());

        // Convert Image to InputStream and insert to DB
//        BufferedImage image = ur.getImage();
//        InputStream stream = ur.imageToInputStream(image);

        InputStream stream = ur.getImageStream();
        ps.setBinaryStream(12, stream);
        ps.setString(13, ur.getImageFormat());

        return ps;
    }
}

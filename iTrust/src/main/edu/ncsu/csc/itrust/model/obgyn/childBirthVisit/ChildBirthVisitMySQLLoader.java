package edu.ncsu.csc.itrust.model.obgyn.childBirthVisit;

import edu.ncsu.csc.itrust.model.SQLLoader;
import edu.ncsu.csc.itrust.model.obgyn.childBirthVisit.ChildBirthVisit;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class ChildBirthVisitMySQLLoader implements SQLLoader<ChildBirthVisit> {
    /**
     * {@inheritDoc}
     */
    @Override
    public List<ChildBirthVisit> loadList(ResultSet rs) throws SQLException {
        ArrayList<ChildBirthVisit> list = new ArrayList<ChildBirthVisit>();
        while (rs.next()) {
            list.add(loadSingle(rs));
        }
        return list;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public ChildBirthVisit loadSingle(ResultSet rs) throws SQLException {
        ChildBirthVisit retCBVisit = new ChildBirthVisit();
        retCBVisit.setChildBirthVisitID(rs.getLong("childBirthVisitID"));
        retCBVisit.setPatientMID(rs.getLong("MID"));
        retCBVisit.setPreferredDeliveryType(rs.getString("preferredDeliveryType"));
        retCBVisit.setVisitType(rs.getString("visitType"));
        retCBVisit.setDeliveryType(rs.getString("deliveryType"));
        retCBVisit.setDeliveryDate(rs.getDate("deliveryDate"));
        retCBVisit.setDeliveryTime(rs.getTime("deliveryDuration"));
        retCBVisit.setNumberOfGirlBabies(rs.getInt("numberOfGirlBabies"));
        retCBVisit.setNumberOfBoyBabies(rs.getInt("numberOfBoyBabies"));
        retCBVisit.setDosage_pitocin(rs.getInt("pitocin"));
        retCBVisit.setDosage_nitrousOxide(rs.getInt("nitrousOxide"));
        retCBVisit.setDosage_pethidine(rs.getInt("pethidine"));
        retCBVisit.setDosage_epiduralAnaesthesia(rs.getInt("epiduralAnaesthesia"));
        retCBVisit.setDosage_magnesiumSulfate(rs.getInt("magnesiumSulfate"));
        retCBVisit.setDosage_rhImmuneGlobulin(rs.getInt("rhImmuneGlobulin"));
        retCBVisit.setComment(rs.getString("comment"));

        return retCBVisit;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public PreparedStatement loadParameters(Connection conn, PreparedStatement ps, ChildBirthVisit cbv, boolean newInstance) throws SQLException  {
        String stmt = "";
        if (newInstance) {

            stmt = "INSERT INTO OBChildBirthVisit (" +
                    "MID," +
                    " preferredDeliveryType," +
                    " visitType," +
                    " deliveryDate," +
                    " deliveryDuration," +
                    " deliveryType," +
                    " numberOfGirlBabies," +
                    " numberOfBoyBabies," +
                    " pitocin," +
                    " nitrousOxide," +
                    " pethidine," +
                    " epiduralAnaesthesia," +
                    " magnesiumSulfate," +
                    " rhImmuneGlobulin," +
                    " comment)" + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
        }else{
            long id = cbv.getChildBirthVisitID();
            stmt = "UPDATE OBChildBirthVisit SET MID=?, "
                    + "preferredDeliveryType=?,"
                    + "visitType=?,"
                    + "deliveryDate=?,"
                    + "deliveryDuration=?,"
                    + "deliveryType=?,"
                    + "numberOfGirlBabies=?,"
                    + "numberOfBoyBabies=?,"
                    + "pitocin=?,"
                    + "nitrousOxide=?,"
                    + "pethidine=?,"
                    + "epiduralAnaesthesia=?,"
                    + "magnesiumSulfate=?,"
                    + "rhImmuneGlobulin=?,"
                    + "comment=? "
                    + "WHERE childBirthVisitID=" + id +";";
        }

        ps = conn.prepareStatement(stmt, com.mysql.jdbc.Statement.RETURN_GENERATED_KEYS);
        ps.setLong(1, cbv.getPatientMID());
//        ps.setLong(2, cbv.getChildBirthVisitID());
        ps.setString(2, cbv.getPreferredDeliveryType());
        ps.setString(3, cbv.getVisitType());
        ps.setDate(4, cbv.getDeliveryDate());
        ps.setTime(5, cbv.getDeliveryTime());
        ps.setString(6, cbv.getDeliveryType());
        ps.setInt(7, cbv.getNumberOfGirlBabies());
        ps.setInt(8, cbv.getNumberOfBoyBabies());
        ps.setInt(9, cbv.getDosage_pitocin());
        ps.setInt(10, cbv.getDosage_nitrousOxide());
        ps.setInt(11, cbv.getDosage_pethidine());
        ps.setInt(12, cbv.getDosage_epiduralAnaesthesia());
        ps.setInt(13, cbv.getDosage_magnesiumSulfate());
        ps.setInt(14, cbv.getDosage_rhImmuneGlobulin());
        ps.setString(15, cbv.getComment());
        return ps;

    }

}

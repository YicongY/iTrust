package edu.ncsu.csc.itrust.model.ratings;

import edu.ncsu.csc.itrust.model.SQLLoader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RatingSQLLoader implements SQLLoader<Rating> {
    /**
     * {@inheritDoc}
     */
    @Override
    public List<Rating> loadList(ResultSet rs) throws SQLException {
        List<Rating> retList = new ArrayList<Rating>();
        while(rs.next()){
            retList.add(loadSingle(rs));
        }
        return retList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Rating loadSingle(ResultSet rs) throws SQLException {
        Rating retRating = new Rating();
        retRating.setAppt_id(rs.getInt("appt_id"));
        retRating.setAttitude(rs.getFloat("attitude"));
        retRating.setDoctor_id(rs.getLong("doctor_id"));
        retRating.setEfficiency(rs.getFloat("efficiency"));
        retRating.setKnowledge(rs.getFloat("knowledge"));
        retRating.setPunctuality(rs.getFloat("punctuality"));
        retRating.setPatient_id(rs.getLong("patient_id"));
        retRating.setSkillfulness(rs.getFloat("skillfulness"));
        retRating.setComment(rs.getString("comment"));
        return retRating;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PreparedStatement loadParameters(Connection conn, PreparedStatement ps, Rating newRating, boolean newInstance) throws SQLException {
        String stmt = "";
        if (newInstance) {
            stmt = "INSERT INTO ratings (" +
                    " doctor_id," +
                    " patient_id," +
                    " punctuality," +
                    " attitude," +
                    " skillfulness," +
                    " knowledge," +
                    " efficiency," +
                    " comment," +
                    " appt_id)" + "VALUES(?,?,?,?,?,?,?,?,?);";
        }else{
            long id = newRating.getAppt_id();
            stmt = "UPDATE ratings SET "
                    + "doctor_id=?, "
                    + "patient_id=?,"
                    + "punctuality=?,"
                    + "attitude=?,"
                    + "skillfulness=?,"
                    + "knowledge=?,"
                    + "efficiency=?, "
                    + "comment=? "
                    + "WHERE appt_id=" + id +";";
        }
        ps = conn.prepareStatement(stmt, com.mysql.jdbc.Statement.RETURN_GENERATED_KEYS);

        ps.setLong(1, newRating.getDoctor_id());
        ps.setLong(2, newRating.getPatient_id());
        ps.setFloat(3, newRating.getPunctuality());
        ps.setFloat(4, newRating.getAttitude());
        ps.setFloat(5, newRating.getSkillfulness());
        ps.setFloat(6, newRating.getKnowledge());
        ps.setFloat(7, newRating.getEfficiency());
        ps.setString(8, newRating.getComment());
        if (newInstance) ps.setInt(9, newRating.getAppt_id());
        return ps;

    }
}

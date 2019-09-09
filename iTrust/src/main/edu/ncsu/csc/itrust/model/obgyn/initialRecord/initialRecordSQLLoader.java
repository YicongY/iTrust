package edu.ncsu.csc.itrust.model.obgyn.initialRecord;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.Statement;

import edu.ncsu.csc.itrust.model.SQLLoader;


public class initialRecordSQLLoader implements SQLLoader<initialRecord>{
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<initialRecord> loadList(ResultSet rs) throws SQLException {
		ArrayList<initialRecord> list = new ArrayList<initialRecord>();
		while (rs.next()) {
			list.add(loadSingle(rs));
		}
		return list;

	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public initialRecord loadSingle(ResultSet rs) throws SQLException {
		initialRecord retOBrecord = new initialRecord();
		retOBrecord.setRecordDate(rs.getDate("recorddate"));
		retOBrecord.setEDD(rs.getDate("EDD"));
		retOBrecord.setDeliveryType(rs.getString("deliverytype"));
		retOBrecord.setLMP(rs.getDate("LMP"));
		retOBrecord.setNumberOfHoursInLabor(rs.getFloat("numberofhoursinlabor"));
		retOBrecord.setNumberOfWeeksPregnant(rs.getString("numberofweekspregnant"));
		retOBrecord.setMID(rs.getInt("MID"));
		retOBrecord.setWeightGainDuringPregnancy(rs.getFloat("weightgainduringpregnancy"));
		retOBrecord.setPregnancyType(rs.getInt("pregnancytype"));
		retOBrecord.setObRecordID(rs.getInt("obrecordID"));
		retOBrecord.setYearOfConception(rs.getInt("yearofconception"));
		
		return retOBrecord;
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PreparedStatement loadParameters(Connection conn, PreparedStatement ps, initialRecord obr, boolean newInstance) throws SQLException  {
		String stmt = "";
		if (newInstance) {
			stmt = "INSERT INTO initobrecord (MID, yearofconception, numberofweekspregnant, numberofhoursinlabor, weightgainduringpregnancy, deliverytype, pregnancytype, LMP, EDD, recorddate)" 
					+ "VALUES(?,?,?,?,?,?,?,?,?,?);";
		}
		
		ps = conn.prepareStatement(stmt, Statement.RETURN_GENERATED_KEYS);
		ps.setLong(1, obr.getMID());
		ps.setInt(2, obr.getYearOfConception());
		ps.setString(3, obr.getNumberOfWeeksPregnant());
		ps.setFloat(4, obr.getNumberOfHoursInLabor());
		ps.setFloat(5, obr.getWeightGainDuringPregnancy());
		ps.setString(6, obr.getDeliveryType());
		ps.setInt(7, obr.getPregnancyType());
		ps.setDate(8, obr.getLMP());
		ps.setDate(9, obr.getEDD());
		ps.setDate(10, obr.getRecordDate());
		return ps;
		
	}
}
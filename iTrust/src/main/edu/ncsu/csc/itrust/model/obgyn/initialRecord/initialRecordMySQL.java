/**
 * 
 */
package edu.ncsu.csc.itrust.model.obgyn.initialRecord;

import edu.ncsu.csc.itrust.DBUtil;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.ValidationFormat;

import javax.annotation.Resource;
import javax.faces.bean.ManagedBean;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Yicong
 *MySQL interface between controller and database
 */

@ManagedBean
public class initialRecordMySQL implements Serializable, initialRecordData {

	private static final long serialVersionUID = 1L;
	@Resource(name = "jdbc/itrust2")
	private initialRecordSQLLoader obrLoader;
	private DataSource ds;
	private initialRecordValidator validator;
	
	/**
	 * Default Constructor 
	 * @throws DBException
	 */
	public initialRecordMySQL() throws DBException {
		obrLoader = new initialRecordSQLLoader();
		try {
			Context ctx = new InitialContext();
			this.ds = ((DataSource) (((Context) ctx.lookup("java:comp/env"))).lookup("jdbc/itrust"));
		} catch (NamingException e) {
			throw new DBException(new SQLException("Context Lookup Naming Exception: " + e.getMessage()));
		}
		validator = new initialRecordValidator(this.ds);
	}
	
	/**
	 * Constructor for testing.
	 * 
	 * @param ds
	 */
	public initialRecordMySQL(DataSource ds) {
		obrLoader = new initialRecordSQLLoader();
		this.ds = ds;
		validator = new initialRecordValidator(this.ds);
	}
	/**
	 * return a list of OBrecord in order of descending date for a Patient 
	 * @param patientID
	 * @return List<initialRecord>
	 */
	@Override
	public List<initialRecord> getOBrecordForPatient(long patientID) throws FormValidationException, DBException {
		Connection conn = null;
		PreparedStatement pstring = null;
		ResultSet results = null;
		if (ValidationFormat.NPMID.getRegex().matcher(Long.toString(patientID)).matches()) {
			try {
				conn = ds.getConnection();
				pstring = conn.prepareStatement("SELECT * FROM initobrecord WHERE MID=? ORDER BY recorddate DESC");

				pstring.setLong(1, patientID);

				results = pstring.executeQuery();

				final List<initialRecord> recordlist = obrLoader.loadList(results);
				return recordlist;
			} catch (SQLException e) {
				throw new DBException(e);
			} finally {
				try {
					if (results != null) {
						results.close();
					}
				} catch (SQLException e) {
					throw new DBException(e);
				} finally {
					DBUtil.closeConnection(conn, pstring);
				}
			}
		} else {
			return null;
		}

	}
	
	/**
	 * Function for testing the OBeligibility 
	 * @param patientID
	 * @return
	 * @throws FormValidationException
	 * @throws DBException
	 */
	public List<Boolean> getOBeligibilityForPatient(long patientID) throws FormValidationException, DBException {
		Connection conn = null;
		PreparedStatement pstring = null;
		ResultSet results = null;
		if (ValidationFormat.NPMID.getRegex().matcher(Long.toString(patientID)).matches()) {
			try {
				conn = ds.getConnection();
				pstring = conn.prepareStatement("SELECT OBEligibility FROM patients WHERE MID=?");

				pstring.setLong(1, patientID);
				results = pstring.executeQuery();   // a Result set type

				ArrayList<Boolean> eligibilityList = new ArrayList<Boolean>();
				while (results.next()) {
					eligibilityList.add(results.getBoolean("OBEligibility"));
				}

				return eligibilityList;

			} catch (SQLException e) {
				throw new DBException(e);

			} finally {
				try {
					if (results != null) {
						results.close();
					}
				} catch (SQLException e) {
					throw new DBException(e);
				} finally {
					DBUtil.closeConnection(conn, pstring);
				}
			}
		} else {
			return null;
		}

	}

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	
	public boolean add(initialRecord obr) throws DBException {
		return addReturnGeneratedId(obr) >= 0;
	}
	
	/**
	 * Validate an OB record first and add it into the database if it is valid, otherwise return error to frontend
	 * @param obr
	 * @return
	 * @throws DBException
	 */
	public long addReturnGeneratedId(initialRecord obr) throws DBException {
		Connection conn = null;
		PreparedStatement pstring = null;
		try {
			validator.validate(obr);
		} catch (FormValidationException e1) {
		    DBException eDB = new DBException(new SQLException(e1.getMessage()));
		    eDB.setErrorList(e1.getErrorList());
			throw eDB;
		}
		long generatedId = -1;
		try {
			conn = ds.getConnection();
			pstring = obrLoader.loadParameters(conn, pstring, obr, true);
			int results = pstring.executeUpdate();
			if (results != 0) {
				ResultSet generatedKeys = pstring.getGeneratedKeys();
				if(generatedKeys.next()) {
					generatedId = generatedKeys.getLong(1);
				}
			}
		} catch (SQLException e) {
			throw new DBException(e);
		} finally {
			DBUtil.closeConnection(conn, pstring);
		}
		return generatedId;
	}
	
	/* 
	 * Update the OBeligibility for an patient
	 * @return Boolean
	 */
	public Boolean updateOBeligibility(long patientID, Boolean Eligibility) throws FormValidationException, DBException{
		boolean success = false;
		Connection conn = null;
		PreparedStatement pstring = null;
		if (ValidationFormat.NPMID.getRegex().matcher(Long.toString(patientID)).matches()) {
			try {
				int results;
				conn = ds.getConnection();
				pstring = conn.prepareStatement("UPDATE patients SET OBEligibility = ? WHERE MID = ?;");
				pstring.setBoolean(1, Eligibility);
				pstring.setLong(2, patientID);

				pstring.executeUpdate();
				results = pstring.executeUpdate();
				success = (results > 0);
				
			} catch (SQLException e) {
				throw new DBException(e);
			} finally {
				DBUtil.closeConnection(conn, pstring);
			}
		} else {
			return success;
		}
		return success;
	}
	public boolean update(initialRecord updateObj) throws DBException, FormValidationException{
		return false;
	}
	
	public List<initialRecord> getAll() throws DBException{
		return null;
	}
	
	/* 
	 * Return a single record for a patient
	 * @param id
	 * @return initialRecord
	 */

	public initialRecord getByID(long id) throws DBException{
		Connection conn = null;
		PreparedStatement pstring = null;
		ResultSet results = null;
		List<initialRecord> obList = null;
		initialRecord ret = null;
		if (ValidationFormat.NPMID.getRegex().matcher(Long.toString(id)).matches()) {
			try {
				conn = ds.getConnection();
				pstring = conn.prepareStatement("SELECT * FROM initobrecord WHERE MID=?");
				pstring.setLong(1, id);

				results = pstring.executeQuery();
				obList = obrLoader.loadList(results);
				if (obList.size() > 0) {
					ret = obList.get(0);
				}
				return ret;
			} catch (SQLException e) {
				throw new DBException(e);
			} finally {
				try {
					if (results != null) {
						results.close();
					}
				} catch (SQLException e) {
					throw new DBException(e);
				} finally {
					DBUtil.closeConnection(conn, pstring);
				}
			}
		} else {
			return null;
		}

	}
}
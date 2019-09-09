package edu.ncsu.csc.itrust.model.obgyn.ultrasoundRecord;

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
import java.util.List;

/**
 * @author Ziyu
 */
@ManagedBean
public class ultrasoundRecordMySQL implements Serializable, ultrasoundRecordData {
    @Resource(name = "jdbc/itrust2")
    private static final long serialVersionUID = 1L;
    private ultrasoundRecordSQLLoader urLoader;
    private DataSource ds;
    private ultrasoundRecordValidator validator;

    /**
     * Default constructor for OfficeVisitMySQL.
     *
     * @throws DBException if there is a context lookup naming exception
     */
    public ultrasoundRecordMySQL() throws DBException {
        urLoader = new ultrasoundRecordSQLLoader();
        try {
            Context ctx = new InitialContext();
            this.ds = ((DataSource) (((Context) ctx.lookup("java:comp/env"))).lookup("jdbc/itrust"));
        } catch (NamingException e) {
            throw new DBException(new SQLException("Context Lookup Naming Exception: " + e.getMessage()));
        }
        validator = new ultrasoundRecordValidator(this.ds);
    }

    /**
     * Constructor for testing.
     *
     * @param ds
     */
    public ultrasoundRecordMySQL(DataSource ds) {
        urLoader = new ultrasoundRecordSQLLoader();
        this.ds = ds;
        validator = new ultrasoundRecordValidator(this.ds);
    }

    /**
     * {@inheritDoc}
     */
    public List<ultrasoundRecord> getAll() throws DBException {
        return null;
    }

    /**
     * Get a single ultrasound record for a patient.
     * Since a patient may have multiple ultrasound records, use method
     * getUltrasoundRecordForPatient instead.
     *
     * {@inheritDoc}
     */
    public ultrasoundRecord getByID(long id) throws DBException {
        return null;
    }

    public List<ultrasoundRecord> getUltrasoundRecordForPatient(long patientMID) throws DBException {
        Connection conn = null;
        PreparedStatement pstring = null;
        ResultSet results = null;
        if (ValidationFormat.NPMID.getRegex().matcher(Long.toString(patientMID)).matches()) {
            try {
                conn = ds.getConnection();
                pstring = conn.prepareStatement(
                        "SELECT * FROM UltrasoundRecord WHERE patientMID=? ORDER BY recordID DESC"
                );

                pstring.setLong(1, patientMID);

                results = pstring.executeQuery();

                final List<ultrasoundRecord> recordlist = urLoader.loadList(results);
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
     * {@inheritDoc}
     */
    public boolean add(ultrasoundRecord ur) throws DBException {
        return addReturnGeneratedId(ur) >= 0;
    }

    public long addReturnGeneratedId(ultrasoundRecord ur) throws DBException {
        Connection conn = null;
        PreparedStatement pstring = null;
        try {
            validator.validate(ur);
        } catch (FormValidationException e1) {
            DBException eDB = new DBException(new SQLException(e1.getMessage()));
            eDB.setErrorList(e1.getErrorList());
            throw eDB;
        }
        long generatedId = -1;
        try {
            conn = ds.getConnection();
            pstring = urLoader.loadParameters(conn, pstring, ur, true);
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


    /**
     * Update an ultrasound record.
     * This method won't be called because an ultrasound record is not
     * supposed to be updated.
     *
     * {@inheritDoc}
     */
    public boolean update(ultrasoundRecord ur) throws DBException {
        return false;
    }

}

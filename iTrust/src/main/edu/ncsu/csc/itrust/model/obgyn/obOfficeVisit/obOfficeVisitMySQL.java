package edu.ncsu.csc.itrust.model.obgyn.obOfficeVisit;

import java.io.Serializable;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.annotation.Resource;
import javax.faces.bean.ManagedBean;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;


import edu.ncsu.csc.itrust.action.AddApptAction;
import edu.ncsu.csc.itrust.action.ApptAction;
import edu.ncsu.csc.itrust.model.obgyn.initialRecord.initialRecord;
import edu.ncsu.csc.itrust.model.old.beans.ApptTypeBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.beans.ApptBean;
import edu.ncsu.csc.itrust.DBUtil;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.ValidationFormat;
import edu.ncsu.csc.itrust.model.old.beans.ApptBean;
import edu.ncsu.csc.itrust.model.old.dao.mysql.ApptDAO;
import edu.ncsu.csc.itrust.model.old.validate.ApptBeanValidator;
import jdk.nashorn.internal.runtime.ParserException;
import org.apache.commons.lang3.tuple.Pair;

import java.text.DateFormat;
import java.util.Date;

/**
 * @author Shu
 *MySQL interface between controller and database
 */
@ManagedBean
public class obOfficeVisitMySQL implements Serializable , obOfficeVisitData{

    private static final long serialVersionUID = 1389654351629546245L;
    @Resource(name = "jdbc/itrust2")
    private obOfficeVisitSQLLoader loader;
    private DataSource ds;
    private obOfficeVisitValidator validator;
    private ApptBeanValidator AppValidator = new ApptBeanValidator();

    /**
     * Default Constructor
     * @throws DBException
     */

    public obOfficeVisitMySQL() throws DBException {
        loader = new obOfficeVisitSQLLoader();
        try {
            Context ctx = new InitialContext();
            this.ds = ((DataSource) (((Context) ctx.lookup("java:comp/env"))).lookup("jdbc/itrust"));
        } catch (NamingException e) {
            throw new DBException(new SQLException("Context Lookup Naming Exception: " + e.getMessage()));
        }
        validator = new obOfficeVisitValidator(this.ds);
    }

    public obOfficeVisitMySQL(DataSource ds) {
        loader = new obOfficeVisitSQLLoader();
        this.ds = ds;
        validator = new obOfficeVisitValidator(this.ds);
    }



    /**
     *
     * @param patientID
     * @return list of ob office visit of a patient
     * @throws FormValidationException
     * @throws DBException
     */
    public List<obOfficeVisit> getOBOfficeVisit(long patientID) throws FormValidationException, DBException {
        Connection conn = null;
        PreparedStatement pstring = null;
        ResultSet results = null;
        if (ValidationFormat.MID.getRegex().matcher(Long.toString(patientID)).matches()) {
            try {
                conn = ds.getConnection();
                pstring = conn.prepareStatement("SELECT * FROM OBOfficeVisit WHERE patientMID=? ORDER BY currentDate DESC");
                pstring.setLong(1, patientID);
                results = pstring.executeQuery();
                final List<obOfficeVisit> recordlist = loader.loadList(results);
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
        } else { return null; }
    }

    /**
     *
     * @param visitID
     * @return one specific OB office visit
     * @throws DBException
     */
    public obOfficeVisit getOBOfficeVisitByVisitID(long visitID) throws DBException {
        Connection conn = null;
        PreparedStatement pstring = null;
        ResultSet results = null;
        if (ValidationFormat.visitedID.getRegex().matcher(Long.toString(visitID)).matches()) {
            try {
                conn = ds.getConnection();
                pstring = conn.prepareStatement("SELECT * FROM OBOfficeVisit WHERE visitID =?");
                pstring.setLong(1, visitID);
                results = pstring.executeQuery();
                results.next();
                final obOfficeVisit obv = loader.loadSingle(results);
                return obv;
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
        } else { return null; }
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean add(obOfficeVisit obv) throws DBException {
        return addReturnGeneratedId(obv) >= 0;
    }

    /**
     * Validate an OB record first and add it into the database if it is valid, otherwise return error to frontend
     * @param obv
     * @return
     * @throws DBException
     */
    public long addReturnGeneratedId(obOfficeVisit obv) throws DBException {
        Connection conn = null;
        PreparedStatement pstring = null;
        try {
            validator.validate(obv);
        } catch (FormValidationException e1) {
            DBException eDB = new DBException(new SQLException(e1.getMessage()));
            eDB.setErrorList(e1.getErrorList());
            throw eDB;
        }
        long generatedId = -1;
        try {
            conn = ds.getConnection();
            pstring = loader.loadParameters(conn, pstring, obv, true);
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
     *
     * @param obv
     * @return an updated OB office visit
     * @throws DBException
     */
    public boolean update(obOfficeVisit obv) throws DBException {
        boolean success = false;
        Connection conn = null;
        PreparedStatement pstring = null;
        try {
            validator.validate(obv);
        } catch (FormValidationException e1) {
            DBException eDB = new DBException(new SQLException(e1.getMessage()));
            eDB.setErrorList(e1.getErrorList());
            throw eDB;
        }
        try {
            conn = ds.getConnection();
            pstring = loader.loadParameters(conn, pstring, obv, false);
            int results = pstring.executeUpdate();

            if (results > 0) { success = true; }
        } catch (SQLException e) {
            throw new DBException(e);
        } finally {
            DBUtil.closeConnection(conn, pstring);
        }
        return success;
    }

    /**
     *
     * @param visitedId
     * @return if delete was success "true" or "false"
     * @throws DBException
     */
    public boolean delete(long visitedId) throws DBException {
        boolean success = false;
        if (ValidationFormat.visitedID.getRegex().matcher(Long.toString(visitedId)).matches()) {
            Connection conn = null;
            PreparedStatement pstring = null;
            try {
                int results;
                conn = ds.getConnection();
                pstring = conn.prepareStatement("DELETE FROM OBOfficeVisit WHERE visitID =?");
                pstring.setLong(1, visitedId);
                results = pstring.executeUpdate();
                success = (results > 0);
                return success;
            } catch (SQLException e) {
                throw new DBException(e);
            } finally {
                DBUtil.closeConnection(conn, pstring);
            }
        } else { return success; }
    }

    public obOfficeVisit getByID(long id) throws DBException{ return null;}

    public List<obOfficeVisit> getAll() throws DBException{ return null; }


}

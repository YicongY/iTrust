package edu.ncsu.csc.itrust.model.obgyn.childBirthVisit;

import edu.ncsu.csc.itrust.DBUtil;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.old.beans.ApptBean;
import edu.ncsu.csc.itrust.model.old.beans.loaders.ApptBeanLoader;
import edu.ncsu.csc.itrust.model.old.validate.ValidationFormat;

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
 * @ClassName:    ChildBirthVisitMySQL
 * @Description:  Intermediate layer for Controller to manipulate database or get results from database ordered by date
 * @Author:       Xiaocong Yu
 **/
@ManagedBean
public class ChildBirthVisitMySQL implements Serializable, ChildBirthVisitData {

    // used in serialUIDAdder.java
    private static final long SerialVersionUID = 1999L;
    @Resource(name = "jdbc/itrust2")
    private ChildBirthVisitMySQLLoader cbv_loader;
    private ChildBirthVisitValidator cbv_validator;
    private DataSource ds;

    /**
     * Normal Constructor
     * @throws DBException
     */
    public ChildBirthVisitMySQL() throws DBException{
        cbv_loader = new ChildBirthVisitMySQLLoader();
        try{
            Context ctx = new InitialContext();
            this.ds = (DataSource) (((Context) ctx.lookup("java:comp/env"))).lookup("jdbc/itrust");
        }catch(NamingException e){
            throw new DBException(new SQLException("Context Lookup Naming Exception: " + e.getMessage()));
        }
        cbv_validator = new ChildBirthVisitValidator(this.ds);
    }

    /**
     * Constructor for testing
     * @param ds
     */
    public ChildBirthVisitMySQL(DataSource ds){
        cbv_loader = new ChildBirthVisitMySQLLoader();
        this.ds = ds;
        cbv_validator = new ChildBirthVisitValidator(this.ds);
    }

    /**
     * getChildBirthVisit by patient's MID
     * @param patientID
     * @return
     * @throws DBException
     */
    @Override
    public List<ChildBirthVisit> getChildBirthVistForPatient(Long patientID) throws DBException {
        Connection conn = null;
        PreparedStatement pstring = null;
        ResultSet results = null;

        if(ValidationFormat.NPMID.getRegex().matcher(Long.toString(patientID)).matches()){
            try {
                conn = ds.getConnection();
                pstring = conn.prepareStatement("SELECT * FROM obchildbirthvisit WHERE MID=? ORDER BY deliveryDate DESC");
                pstring.setLong(1, patientID);
                results = pstring.executeQuery();
                // invoke SQLException here since iteration on the ResultSet
                final List<ChildBirthVisit> recordList = cbv_loader.loadList(results);
                return recordList;
            }catch(SQLException e){
                throw new DBException(e);
            }finally {
                try{
                    if(results != null){
                        results.close();
                    }
                }catch(SQLException e){
                    throw new DBException(e);
                }finally{
                    DBUtil.closeConnection(conn, pstring);
                }
            }
        }else{
            return null;
        }
    }

    /**
     * add a new ChildBirthVisit record and will return whether the add operation is carried out successfully
     * @param addObj
     * @return
     * @throws FormValidationException
     * @throws DBException
     */
    @Override
    public boolean add(ChildBirthVisit addObj) throws FormValidationException, DBException {
        return addReturnGeneratedId(addObj) > 0;
    }

    /**
     * return a Long generatedID associated with added ChildBirthVisit record in the table "obchildbirthvisit"
     * @param cbv
     * @return
     * @throws FormValidationException
     * @throws DBException
     */
    public Long addReturnGeneratedId(ChildBirthVisit cbv) throws FormValidationException, DBException{

        Long generatedId;
        Connection conn = null;
        PreparedStatement pstring = null;
        try{
            cbv_validator.validate(cbv);
            generatedId = Long.valueOf(-1);
            conn = ds.getConnection();
            pstring = cbv_loader.loadParameters(conn, pstring, cbv, true);
            int results = pstring.executeUpdate();
            if(results != 0){
                ResultSet generatedKeys = pstring.getGeneratedKeys();
                if(generatedKeys.next()){
                    generatedId = generatedKeys.getLong(1);
                }
            }
        } catch(SQLException e){
            throw new DBException(e);
        }finally{
            DBUtil.closeConnection(conn, pstring);
        }
        return generatedId;
    }



    /**
     * update ChildBirthVisit
     * @param updateObj
     * @return
     * @throws DBException
     * @throws FormValidationException
     */
    @Override
    public boolean update(ChildBirthVisit updateObj) throws DBException, FormValidationException {
        boolean retval = false;
        Connection conn = null;
        PreparedStatement pstring= null;
        cbv_validator.validate(updateObj);
        int results;

        try{
            conn = ds.getConnection();
            pstring= cbv_loader.loadParameters(conn, pstring, updateObj, false);
            results = pstring.executeUpdate();
            retval = (results > 0);
        }catch(SQLException e){
            throw new DBException(e);
        }finally{
            DBUtil.closeConnection(conn, pstring);
        }
        return retval;
    }

    /**
     * Not needed for current usecase
     * Get all ChildBirthRecord records
     * @return
     * @throws DBException
     */
    @Override
    public List<ChildBirthVisit> getAll() throws DBException {
        return null;
    }

    @Override
    public ChildBirthVisit getByID(long id) throws DBException {
        return null;
    }
}

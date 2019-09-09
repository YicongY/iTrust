package edu.ncsu.csc.itrust.model.ratings;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;

import javax.annotation.Resource;
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
 * @ClassName:    RatingMySQL
 * @Description:  Intermediate layer for Controller to manipulate database on Rating Object
 * @Author:       Qishan Zhu
 * @Date:         12/04/2018
 **/

public class RatingMySQL implements Serializable, RatingData {
    @Resource(name = "jdbc/itrust2")
    private static final long serialVersionUID = 188L;

    private RatingSQLLoader ratingLoader;
    private DataSource ds;
    private RatingValidator validator;

    /**
     * Default Constructor for RatingMySQL
     *
     * @throws DBException if there is a context lookup naming exception
     */
    public RatingMySQL() throws DBException {
        ratingLoader = new RatingSQLLoader();
        try {
            Context ctx = new InitialContext();
            this.ds = ((DataSource) (((Context) ctx.lookup("java:comp/env"))).lookup("jdbc/itrust"));
        } catch (NamingException e) {
            throw new DBException(new SQLException("Context Lookup Naming Exception: " + e.getMessage()));
        }
        validator = new RatingValidator(this.ds);
    }

    /**
     * Constructor for testing
     **/
    public RatingMySQL(DataSource ds) {
        ratingLoader = new RatingSQLLoader();
        this.ds = ds;
        validator = new RatingValidator(this.ds);
    }

    /**
     * Get a list of Rating by a given doctor_id
     *
     * @param doctor_id A mid for doctor
     * @return List<Rating> all Ratings for this specified doctor
     * @throws DBException if error occurred in inserting office visit
     */
    @Override
    public List<Rating> getRatingsForDoctor(Long doctor_id) throws DBException {

        try (Connection conn = ds.getConnection()) {
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM ratings WHERE doctor_id=" + doctor_id);
            ResultSet resultSet = statement.executeQuery();
            List<Rating> result_list = ratingLoader.loadList(resultSet);
            return result_list;
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Rating> getAll() throws DBException {
        try (Connection conn = ds.getConnection()) {
             PreparedStatement statement = conn.prepareStatement("SELECT * FROM ratings");
             ResultSet resultSet = statement.executeQuery();
             List<Rating> result_list = ratingLoader.loadList(resultSet);
             return result_list;
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Rating getByID(long id) throws DBException {
        try (Connection conn = ds.getConnection()){
             PreparedStatement statement = conn.prepareStatement("SELECT * FROM ratings WHERE appt_id=" + id);
             ResultSet resultSet = statement.executeQuery();
                List<Rating> result_list = ratingLoader.loadList(resultSet);
                if (result_list.size() > 0) {
                    return result_list.get(0);
                } else {
                    return null;
            }
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean add(Rating addObj) throws FormValidationException, DBException {
        validator.validate(addObj);

        try (Connection conn = ds.getConnection()){
            PreparedStatement statement = ratingLoader.loadParameters(conn, null, addObj, true);
            int rowcount = statement.executeUpdate();
            return rowcount == 1;
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean update(Rating updateObj) throws DBException, FormValidationException {
        validator.validate(updateObj);

        try (Connection conn = ds.getConnection()){
            PreparedStatement statement = ratingLoader.loadParameters(conn, null, updateObj, false);
            int rowcount = statement.executeUpdate();
            return rowcount == 1;
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }
}

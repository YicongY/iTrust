package edu.ncsu.csc.itrust.controller.ratings;

import edu.ncsu.csc.itrust.controller.iTrustController;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.logger.TransactionLogger;
import edu.ncsu.csc.itrust.model.ValidationFormat;
import edu.ncsu.csc.itrust.model.ratings.Rating;
import edu.ncsu.csc.itrust.model.ratings.RatingMySQL;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.PatientDAO;
import edu.ncsu.csc.itrust.model.old.enums.TransactionType;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RatingController extends iTrustController{
    /**
     * Model to access Rating in MySQL
     */
    private RatingMySQL ratingData;

    /**
     * MID of the person logging in this session
     */
    private long loggedInMID;

    /**
     * Constructor for ratingController with loggedInMid initialized
     */
    public RatingController(long loggedInMID) throws DBException {
        super();
        ratingData = new RatingMySQL();
        this.loggedInMID = loggedInMID;
    }

    /**
     * Constructor for RatingController with ratingMySql initialized
     * @parameter:  ds: DataSource for initializing a ratingRecord
     */
    public RatingController(DataSource ds){
        super();
        ratingData = new RatingMySQL(ds);
    }

    /**
     * Set the ratingData as the input SQL
     * @parameter:  ds: DataSource for initializing a ratingRecord
     */
    public void setRatingSQL(RatingMySQL newSQL) {
        this.ratingData = newSQL;
    }

    /**
     * Get the loggedInMID
     */
    public long getLoggedInMID() {
        return loggedInMID;
    }

    /**
     * Set the loggedInMID with input
     */
    public void setLoggedInMID(long loggedInMID) {
        this.loggedInMID = loggedInMID;
    }

    /**
     * Set the ratingData as the input SQL
     * @parameter newRating
     *              input Rating object to be added
     * @throws DBException
     *             if error occurred in adding Rating
     *         FormValidationException
     *             if error occurred in adding Rating
     */
    public void addRating(Rating newRating) throws DBException, FormValidationException {
        DAOFactory factory = DAOFactory.getProductionInstance();
        PatientDAO patientDAO = factory.getPatientDAO();
        boolean success = ratingData.add(newRating);
        // add logging to database
        if (success) {
            long appt_id = newRating.getAppt_id();
            TransactionLogger.getInstance().logTransaction(TransactionType.ADD_APPOINTMENT_RATING, loggedInMID, appt_id, "" + newRating.getDoctor_id());
        }
    }


    /**
     * Get Rating of an appointment id
     * @parameter  apptID
     *              an appointment id
     * @return A Rating of teh appontment
     * @throws DBException
     *             if error occurred in getting rating
     *         FormValidationException
     *             if apptID is invalid
     */
    public Rating getRating(String apptID) throws DBException, FormValidationException {
        if (apptID != null) {
            try {
                long appt_id = Long.parseLong(apptID);
                Rating rating = ratingData.getByID(appt_id);
                // add logging to database
                TransactionLogger.getInstance().logTransaction(TransactionType.VIEW_APPOINTMENT_RATING, loggedInMID, appt_id, "" + rating.getDoctor_id());
                return rating;
            } catch (NumberFormatException e) {
                throw new FormValidationException("Invalid appointment id");
            }
        } else {
            return null;
        }
    }


    /**
     * Get overall rating of a doctor
     * @parameter  doctorID
     *              a MID for the doctor
     * @return A map of the doctor's overall rating
     * @throws DBException
     *             if error occurred in getting rating
     *         FormValidationException
     *             if any doctorID is invalid
     */
    public Map<String, Float> getOverallRating(String doctorID) throws DBException, FormValidationException {
        if (doctorID != null) {
            if (!ValidationFormat.MID.getRegex().matcher(doctorID).matches()) {
                throw new FormValidationException("Invalid patientID");
            } else {
                try {
                    long mid = Long.parseLong(doctorID);
                    List<Rating> ratings = ratingData.getRatingsForDoctor(mid);
                    float[] total = {0, 0, 0, 0, 0};
                    int ratingCount = ratings.size();
                    for (Rating rating: ratings) {
                        total[0] += rating.getPunctuality();
                        total[1] += rating.getAttitude();
                        total[2] += rating.getSkillfulness();
                        total[3] += rating.getKnowledge();
                        total[4] += rating.getEfficiency();
                    }
                    Map<String, Float> results = new HashMap<>();
                    results.put("punctuality", total[0]/ratingCount);
                    results.put("attitude", total[1]/ratingCount);
                    results.put("skillfulness", total[2]/ratingCount);
                    results.put("knowledge", total[3]/ratingCount);
                    results.put("efficiency", total[4]/ratingCount);
                    // add logging to database
                    TransactionLogger.getInstance().logTransaction(TransactionType.VIEW_APPOINTMENT_RATING, loggedInMID, mid, "");
                    return results;
                } catch (NumberFormatException e) {
                    throw new FormValidationException("Invalid patientID");
                }
            }
        } else {
            return null;
        }
    }

    public int getRatingPeopleNumber(String doctorID) throws DBException, FormValidationException {
        if (doctorID != null) {
            if (!ValidationFormat.MID.getRegex().matcher(doctorID).matches()) {
                throw new FormValidationException("Invalid patientID");
            } else {
                try {
                    long mid = Long.parseLong(doctorID);
                    List<Rating> ratings = ratingData.getRatingsForDoctor(mid);
                    return ratings.size();
                } catch (NumberFormatException e) {
                    throw new FormValidationException("Invalid patientID");
                }
            }
        } else {
            return 0;
        }
    }

}

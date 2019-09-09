package edu.ncsu.csc.itrust.model.ratings;

import java.util.List;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.DataBean;

public interface RatingData extends DataBean<Rating> {
    /**
     * Get a list of Rating by a given doctor_id
     *
     * @param doctor_id
     * 			A mid for doctor
     * @return List<Rating> all Ratings for this specified doctor
     * @throws DBException if error occurred in inserting office visit
     *
     */
    public List<Rating> getRatingsForDoctor(Long doctor_id) throws DBException;
}

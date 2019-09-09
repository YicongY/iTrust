package edu.ncsu.csc.itrust.model.ratings;

import edu.ncsu.csc.itrust.exception.ErrorList;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.POJOValidator;

import javax.sql.DataSource;

public class RatingValidator extends POJOValidator<Rating> {
    private DataSource ds;

    /**
     * Default constructor for RatingValidator.
     */
    public RatingValidator(DataSource ds) {
        this.ds = ds;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validate(Rating obj) throws FormValidationException {
        ErrorList errorList = new ErrorList();
        if (obj == null) {
            errorList.addIfNotNull("Cannot add office visit information: invalid patient MID");
        }

        // check punctuality
        if (obj.getPunctuality() <= 0) {
            errorList.addIfNotNull("Punctuality can not be smaller than or equal to 0.");
        }
        if (obj.getPunctuality() > 5) {
            errorList.addIfNotNull("Punctuality can not be greater than 5.");
        }

        // check attitude
        if (obj.getAttitude() <= 0) {
            errorList.addIfNotNull("Attitude can not be smaller than or equal to 0.");
        }
        if (obj.getAttitude() > 5) {
            errorList.addIfNotNull("Attitude can not be greater than 5.");
        }

        // check skillfulness
        if (obj.getSkillfulness() <= 0) {
            errorList.addIfNotNull("Skillfulness can not be smaller than or equal to 0.");
        }
        if (obj.getSkillfulness() > 5) {
            errorList.addIfNotNull("Skillfulness can not be greater than 5.");
        }

        // check Knowledge
        if (obj.getKnowledge() <= 0) {
            errorList.addIfNotNull("Knowledge can not be smaller than or equal to 0.");
        }
        if (obj.getKnowledge() > 5) {
            errorList.addIfNotNull("Knowledge can not be greater than 5.");
        }

        // check Efficiency
        if (obj.getEfficiency() <= 0) {
            errorList.addIfNotNull("Efficiency can not be smaller than or equal to 0.");
        }
        if (obj.getEfficiency() > 5) {
            errorList.addIfNotNull("Efficiency can not be greater than 5.");
        }

        if (errorList.hasErrors()) {
            throw new FormValidationException(errorList);
        }

    }
}

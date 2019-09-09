package edu.ncsu.csc.itrust.model.obgyn.report;

import edu.ncsu.csc.itrust.exception.ErrorList;
import edu.ncsu.csc.itrust.exception.FormValidationException;

import javax.sql.DataSource;


/**
 *@author Shu
 */
public class reportValidator  {
        /**
         * Default constructor for reportValidator.
         */
        private DataSource ds;
        public reportValidator(DataSource ds) {
            this.ds = ds;
        }

        public reportValidator() { }

    public void validate(long patientMID) throws FormValidationException {
        ErrorList errorList = new ErrorList();
        if (patientMID < 0) {
            errorList.addIfNotNull("Invalid Patient MID");
        }

        if (errorList.hasErrors()) {
            throw new FormValidationException(errorList);
        }
    }
}

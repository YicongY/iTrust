package edu.ncsu.csc.itrust.model.old.validate;

import edu.ncsu.csc.itrust.exception.ErrorList;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.old.beans.ApptBean;

import java.sql.Timestamp;

public class ApptBeanValidator extends BeanValidator<ApptBean>{

	@Override
	public void validate(ApptBean bean) throws FormValidationException {
		ErrorList errorList = new ErrorList();
		if(bean.getComment() == null)
			return;
//		errorList.addIfNotNull(checkFormat("Appointment Comment", bean.getComment(), ValidationFormat.APPT_COMMENT, true));
		if(bean.getDate().before(new Timestamp(System.currentTimeMillis()))) {
			errorList.addIfNotNull("The scheduled date of this Appointment ("+bean.getDate()+") has already passed.");
		}
		if (errorList.hasErrors())
			throw new FormValidationException(errorList);
	}

}

package edu.ncsu.csc.itrust.model.obgyn.initialRecord;
import java.time.LocalDate;



import java.sql.Date;
import java.text.SimpleDateFormat; 
import java.text.DateFormat;  
import javax.sql.DataSource;
import java.util.Calendar; 


import edu.ncsu.csc.itrust.exception.ErrorList;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.POJOValidator;
import edu.ncsu.csc.itrust.model.ValidationFormat;

public class initialRecordValidator extends POJOValidator<initialRecord> {
	
	private DataSource ds;

	/**
	 * Default constructor for InitialRecord. 
	 */
	public	initialRecordValidator(DataSource ds) {
		this.ds = ds;
	}

	/**
	 * Default constructor for Testing 
	 */
	public initialRecordValidator() {}

	/**
	 * Used to Validate an initial Record object. If the validation does not
	 * succeed, a {@link FormValidationException} is thrown. only performs
	 * checks on the values stored in the object (e.g. Patient MID) Does NOT
	 * validate the format of the other attributes that are NOT
	 * stored in the object itself
	 * 
	 * @param obj
	 *            the Obrecord to be validated
	 */
	@Override
	public void validate(initialRecord obj) throws FormValidationException {
		ErrorList errorList = new ErrorList();
		
		Long patientMID = obj.getMID();
		LocalDate currDate = LocalDate.now(); 
		errorList.addIfNotNull(checkFormat("Patient MID", Long.toString(patientMID), ValidationFormat.NPMID, false));
		
		Date patientRecordDate = obj.getRecordDate();
		//LocalDate patientdate = patientRecordDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		int currYear = currDate.getYear();
		
		if (patientRecordDate.toLocalDate().isAfter(currDate)) {
			errorList.addIfNotNull("Record date cannot be later than today");
		}
		if (!patientRecordDate.toLocalDate().isEqual(currDate)) {
			errorList.addIfNotNull("Record date is not equal to today's date");
		}
		
		int conceptionYear = obj.getYearOfConception();
		if (conceptionYear != 0){
			errorList.addIfNotNull(checkFormat("Year of Conception", Integer.toString(conceptionYear), ValidationFormat.YEAR, true));
		}
		
		if (conceptionYear > currYear) {
			errorList.addIfNotNull("Cannot add record because year of conception is later than this year");
			throw new FormValidationException(errorList);
		}
		
		float hoursInLabor = obj.getNumberOfHoursInLabor();
		String weeksInPregnancy = obj.getNumberOfWeeksPregnant();
		float weightGain = obj.getWeightGainDuringPregnancy();
		if (weightGain != 0.0){
			errorList.addIfNotNull(checkFormat("Weight Gain", Float.toString(weightGain), ValidationFormat.Weight, false));
		}
		if (hoursInLabor != 0.0){
			errorList.addIfNotNull(checkFormat("Hours In Labor", Float.toString(hoursInLabor), ValidationFormat.HOURS_LABOR, false));
		}
		
		if (!weeksInPregnancy.equals("")){
			errorList.addIfNotNull(checkFormat("Weeks of Pregnancy", weeksInPregnancy, ValidationFormat.WEEKS_PREGNANT, false));
		}
		
		String deliverytype = obj.getDeliveryType();
		String[] deliveryformat = {"vaginal delivery", "vaginal delivery vacuum assist", "vaginal delivery forceps assist", "caesarean section", "miscarriage"};
		boolean found = false;
		if (!deliverytype.equals("")){
			for(String type : deliveryformat) {
				  found = deliverytype.equals(type);
				  if (found) break;
				}
				if (found == false){
					errorList.addIfNotNull("Delivery type has to be in vaginal delivery, vaginal delivery vacuum assist, vaginal delivery forceps assist, caesarean section, miscarriage ");
				}
				
				int pregnancyType = obj.getPregnancyType();
				if (pregnancyType != 0 ){
					errorList.addIfNotNull(checkFormat("Pregnancy type", Integer.toString(pregnancyType), ValidationFormat.PREGNANCYTYPE, false));
				}
		}
		

		
		Date EDDDate = obj.getEDD();
		Date LMPDate = obj.getLMP();
		
		if (LMPDate != null && LMPDate.toLocalDate().isAfter(currDate)) {
			errorList.addIfNotNull("LMP date cannot be later than today ");
		}
		
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy"); 
//		DateFormat sqldateFormat = new SimpleDateFormat("yyyy-MM-dd");
//		
//		Calendar calendar = Calendar.getInstance();
//		calendar.setTime(LMPDate);
//		calendar.add(Calendar.DATE, 280);
//		Date correctEDD = java.sql.Date.valueOf(sqldateFormat.format(calendar.getTime()));
//		if (EDDDate != null && correctEDD != EDDDate){
//			errorList.addIfNotNull("EDD is not calculated or entered correctly");
//		}
		//check EDD and LMP date
		if (EDDDate != null && (EDDDate.toLocalDate().isBefore(currDate) || EDDDate.toLocalDate().isEqual(currDate))){
			errorList.addIfNotNull("EDD cannot be equal or earlier than today");
		}
		if (EDDDate != null){
			errorList.addIfNotNull(checkFormat("EDD", dateFormat.format(EDDDate), ValidationFormat.DATE, false));
		}
		if (LMPDate != null){
			errorList.addIfNotNull(checkFormat("LMP", dateFormat.format(LMPDate), ValidationFormat.DATE, false));
		}
		
		if (errorList.hasErrors()) {
			throw new FormValidationException(errorList);
		}
	}
}

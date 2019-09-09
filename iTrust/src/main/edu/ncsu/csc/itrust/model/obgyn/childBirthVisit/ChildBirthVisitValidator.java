package edu.ncsu.csc.itrust.model.obgyn.childBirthVisit;

import edu.ncsu.csc.itrust.exception.ErrorList;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.ValidationFormat;
import edu.ncsu.csc.itrust.model.POJOValidator;


import javax.sql.DataSource;
import java.sql.Date;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


public class ChildBirthVisitValidator extends POJOValidator<ChildBirthVisit>{
    private DataSource ds;
    public static HashSet<String> deliveryTypes = new HashSet<String>(Arrays.asList(new String[]{"", "vaginal delivery", "vaginal delivery vacuum assist", "vaginal delivery forceps assist", "caesarean section", "miscarriage"}));
    public static HashSet<String> visitTypes = new HashSet<String>(Arrays.asList(new String[]{"appointment", "emergency"}));


    /**
     * Default constructor for ChildBirthVisitValidator.
     */
    public	ChildBirthVisitValidator(DataSource ds) {
        this.ds = ds;
    }

    /**
     * Default constructor for Testing
     */
    public ChildBirthVisitValidator() {
        // TODO no need
        String[] deliveryformat = {"vaginal delivery", "vaginal delivery vacuum assist", "vaginal delivery forceps assist", "caesarean section", "miscarriage"};
        deliveryTypes = new HashSet<String>(Arrays.asList(deliveryformat));

    }

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
    public void validate(ChildBirthVisit obj) throws FormValidationException {
        ErrorList errorList = new ErrorList();

//        Long patientMID = obj.getPatientMID();
//        Long childBirthVisitID = obj.getChildBirthVisitID();
//        errorList.addIfNotNull(checkFormat("Patient MID", Long.toString(patientMID), ValidationFormat.NPMID, false));
////        errorList.addIfNotNull(checkFormat("ChildBirth Visit ID", Long.toString(childBirthVisitID), ValidationFormat.NPMID, false));

        String visitType = obj.getVisitType();
        String preferredDeliverType = obj.getPreferredDeliveryType();

        if (visitType == null || visitType.equals("")) {
            errorList.addIfNotNull("Visit Type cannot be empty");
        } else if (visitType.equals("emergency") && !(preferredDeliverType == null || preferredDeliverType.equals(""))) {
            errorList.addIfNotNull("Emergency Visit Type shouldn't have a preferred delivery type");
        } else if (visitType.equals("appointment") && (preferredDeliverType == null || preferredDeliverType.equals(""))) {
            errorList.addIfNotNull("Appointment Visit Type must have a preferred delivery type");
        } else {
            errorList.addIfNotNull(" ");
        }
        errorList.addIfNotNull(" ");


        int dosage_pitocin = obj.getDosage_pitocin();
        int dosage_nitrousOxide = obj.getDosage_nitrousOxide();
        int dosage_pethidine = obj.getDosage_pethidine();
        int dosage_epiduralAnaesthesia = obj.getDosage_epiduralAnaesthesia();
        int dosage_magnesiumSulfate = obj.getDosage_magnesiumSulfate();
        int dosage_rhImmuneGlobulin = obj.getDosage_rhImmuneGlobulin();
        errorList.addIfNotNull(checkFormat("Dosage Pitocin", Integer.toString(dosage_pitocin), ValidationFormat.DRUG_DOSAGE, false));
        errorList.addIfNotNull(checkFormat("Dosage NitrousOxide", Integer.toString(dosage_nitrousOxide), ValidationFormat.DRUG_DOSAGE, false));
        errorList.addIfNotNull(checkFormat("Dosage Pethidine", Integer.toString(dosage_pethidine), ValidationFormat.DRUG_DOSAGE, false));
        errorList.addIfNotNull(checkFormat("Dosage EpiduralAnaesthesia", Integer.toString(dosage_epiduralAnaesthesia), ValidationFormat.DRUG_DOSAGE, false));
        errorList.addIfNotNull(checkFormat("Dosage MagnesiumSulfate", Integer.toString(dosage_magnesiumSulfate), ValidationFormat.DRUG_DOSAGE, false));
        errorList.addIfNotNull(checkFormat("Dosage RhImmuneGlobulin", Integer.toString(dosage_rhImmuneGlobulin), ValidationFormat.DRUG_DOSAGE, false));


        Date patientDeliveryDate = obj.getDeliveryDate();
        //LocalDate patientdate = patientDeliveryDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        LocalDate currDate = LocalDate.now();
        //int currYear = currDate.getYear();


        // TODO check the validation
        if (patientDeliveryDate.toLocalDate().isAfter(currDate)) {
            errorList.addIfNotNull("Delivery date cannot be later than today");
        }


        Time deliveryTime = obj.getDeliveryTime();
        int deliveryHour =deliveryTime.getHours();
        int deliveryMinute = deliveryTime.getMinutes();
        int deliverySeconds = deliveryTime.getSeconds();
        if (deliveryHour < 0 || deliveryHour > 23) {
            errorList.addIfNotNull("Delivery Time Error in Hour");
        } else if (deliveryMinute < 0 || deliveryMinute > 59) {
            errorList.addIfNotNull("Delivery Time Error in Minute");
        } else if (deliverySeconds < 0 || deliverySeconds > 59) {
            errorList.addIfNotNull("Delivery Time Error in Second");
        } else {
            errorList.addIfNotNull(" ");
        }


        String deliveryType = obj.getDeliveryType();
        if (!ifDeliveryTypeMatched(deliveryType)){
            errorList.addIfNotNull("Delivery type has to be in vaginal delivery, vaginal delivery vacuum assist, vaginal delivery forceps assist, caesarean section, miscarriage ");
        } else {
            errorList.addIfNotNull(" ");
        }

        int numberOfGirlBabies = obj.getNumberOfGirlBabies();
        int numberOfBoyBabies = obj.getNumberOfBoyBabies();
        errorList.addIfNotNull(checkFormat("Number of Girl Baby", Integer.toString(numberOfGirlBabies), ValidationFormat.BABY_NUMBER, false));
        errorList.addIfNotNull(checkFormat("Number of Boy Baby", Integer.toString(numberOfBoyBabies), ValidationFormat.BABY_NUMBER, false));


//        float hoursInLabor = obj.getNumberOfHoursInLabor();
//        String weeksInPregnancy = obj.getNumberOfWeeksPregnant();
//        float weightGain = obj.getWeightGainDuringPregnancy();
//        if (weightGain != 0.0){
//            errorList.addIfNotNull(checkFormat("Weight Gain", Float.toString(weightGain), ValidationFormat.Weight, false));
//        }
//        if (hoursInLabor != 0.0){
//            errorList.addIfNotNull(checkFormat("Hours In Labor", Float.toString(hoursInLabor), ValidationFormat.HOURS_LABOR, false));
//        }
//
//        if (!weeksInPregnancy.equals("")){
//            errorList.addIfNotNull(checkFormat("Weeks of Pregnancy", weeksInPregnancy, ValidationFormat.WEEKS_PREGNANT, false));
//        }

        if (errorList.hasErrors()) {
            throw new FormValidationException(errorList);
        }
    }


    public static boolean ifDeliveryTypeMatched(String deliveryType) {
        return deliveryTypes.contains(deliveryType);
    }

    public static boolean ifVisitTypeMatched(String visitType) {
        return visitTypes.contains(visitType);
    }
}

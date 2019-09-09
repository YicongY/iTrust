package edu.ncsu.csc.itrust.controller.obgyn.childBirthVisit;

import com.sun.jna.platform.win32.Sspi;
import edu.ncsu.csc.itrust.action.ViewMyApptsAction;
import edu.ncsu.csc.itrust.controller.iTrustController;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.logger.TransactionLogger;
import edu.ncsu.csc.itrust.model.ValidationFormat;
import edu.ncsu.csc.itrust.model.old.beans.ApptBean;
import edu.ncsu.csc.itrust.model.obgyn.childBirthVisit.ChildBirthVisit;
import edu.ncsu.csc.itrust.model.obgyn.childBirthVisit.ChildBirthVisitMySQL;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.PatientDAO;
import edu.ncsu.csc.itrust.model.old.enums.TransactionType;
import org.jboss.netty.handler.codec.replay.UnreplayableOperationException;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @author Qishan Zhu
 * Controller for child birth visit record
 */
public class ChildBirthVisitController extends iTrustController {
    /**
     * Model to access ChildBirthVisit in MySQL
     */
    private ChildBirthVisitMySQL childBirthVisitData;

    /**
     * Factory to access DAO object, particularly, appointment object
     */
    private DAOFactory factory;

    /**
     * MID of the person logging in this session
     */
    private long loggedInMID;


    /**
     * Constructor for ChildBirthVisitController with loggedInMid initialized
     */
    public ChildBirthVisitController(long loggedInMID) throws DBException {
        super();
        childBirthVisitData = new ChildBirthVisitMySQL();
        factory = DAOFactory.getProductionInstance();
        this.loggedInMID = loggedInMID;
    }

    /**
     * Constructor for ChildBirthVisitController with childBirthRecordMySql initialized
     * @parameter:  ds: DataSource for initializing a childBirthVisitRecord
     */
    public ChildBirthVisitController(DataSource ds, DAOFactory factory){
        super();
        childBirthVisitData  = new ChildBirthVisitMySQL(ds);
        this.factory = factory;
    }

    /**
     * Set the childBirthVisitData as the input SQL
     * @parameter:  ds: DataSource for initializing a childBirthVisitRecord
     */
    public void setChildBirthVisitSQL(ChildBirthVisitMySQL newSQL){
        this.childBirthVisitData = newSQL;
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
     * Set the childBirthVisitData as the input SQL
     * @parameter newChildBirthVisit
     *              input ChildBirthVisit object to be added
     * @throws DBException
     *             if error occurred in adding ChildBirthVisit
     *         FormValidationException
     *             if error occurred in adding ChildBirthVisit
     */
    public void addChildBirthVisit(ChildBirthVisit newChildBirthVisit) throws DBException, FormValidationException {
        DAOFactory factory = DAOFactory.getProductionInstance();
        PatientDAO patientDAO = factory.getPatientDAO();
        boolean success = childBirthVisitData.add(newChildBirthVisit);

        // add logging to database
        if (success) {
            Date EDD = newChildBirthVisit.getDeliveryDate();
            DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            TransactionLogger.getInstance().logTransaction(TransactionType.CREATE_CHILDBIRTH_VISIT, loggedInMID, loggedInMID, dateFormat.format(EDD));
        }
    }

    /**
     * Get a list of childBirthVisit sorted by date (lastest first)
     * @parameter  patientID
     *              a MID for the patient
     * @return A list of childBirthVisit sorted by date
     * @throws DBException
     *             if error occurred in getting ChildBirthVisit
     *         FormValidationException
     *             if any patientID is invalid
     */
    public List<ChildBirthVisit> getChildBirthVisitByDate(String patientID) throws DBException, FormValidationException{
        if (patientID == null || !ValidationFormat.MID.getRegex().matcher(patientID).matches()) {
            throw new FormValidationException("Invalid patientID");
        }
        long mid = Long.parseLong(patientID);
        List<ChildBirthVisit> cbvList  = childBirthVisitData.getChildBirthVistForPatient(mid);
        TransactionLogger.getInstance().logTransaction(TransactionType.VIEW_CHILDBIRTH_VISIT, loggedInMID, loggedInMID, "EDD");

        return cbvList;
    }

    /**
     * Update an existing childBirthVisit
     * @parameter  updatedChildBirthVisit
     *              an updated ChildBirthVisit
     * @throws DBException
     *             if error occurred in updating ChildBirthVisit
     *         FormValidationException
     *             if any patientID is invalid
     */
    public void updateChildBirthVisit(ChildBirthVisit updatedChildBirthVisit) throws DBException, FormValidationException {
        childBirthVisitData.update(updatedChildBirthVisit);
        TransactionLogger.getInstance().logTransaction(TransactionType.EDIT_CHILDBIRTH_VISIT, loggedInMID, loggedInMID, "EDD");
    }

    /**
     * Check whether there is an existing appointment for this patient id for today's visit
     * @parameter  patientID: a MID for the patient
     * @return Null
     *              if no any appointment
     *         DeliveryType as a string
     *              if there is a lastest Obstetrics_ChildBirth appointment
     * @throws DBException
     *             if error occurred in checking existing appointments
     *         FormValidationException
     *             if any patientID is invalid
     */
    public String checkExistingAppointment(String patientID) throws DBException, FormValidationException{
        if (patientID == null || !ValidationFormat.MID.getRegex().matcher(patientID).matches()) {
            throw new FormValidationException("Invalid patientID");
        }

        long mid = Long.parseLong(patientID);
        ViewMyApptsAction apptsAction = new ViewMyApptsAction(factory, mid);
        List<ApptBean> appts_list;
        try{
            appts_list = apptsAction.getAllMyAppointments();
        } catch (SQLException exception) {
            throw new DBException(exception);
        }

        // apptType_id.type == 8
        Timestamp minDeliveryDate = new Timestamp(System.currentTimeMillis() -  (long)(3e+9));
        for (int i = appts_list.size() - 1; i >= 0; i--) {
            ApptBean appt = appts_list.get(i);
            Timestamp date = appt.getDate();
            // The appointment is too old
            if (date.before(minDeliveryDate)) {
                break;
            }
            String apptType = appt.getApptType();
            if (apptType.equals("Obstetrics_ChildBirth")) {
                String comment = appt.getComment();
                String[] comments = comment.split("\\$");
                String deliveryType = comments[comments.length - 1];
                return deliveryType;
            }
        }

        return null;
    }
}

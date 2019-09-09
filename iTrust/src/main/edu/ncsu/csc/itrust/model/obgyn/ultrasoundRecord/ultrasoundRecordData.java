package edu.ncsu.csc.itrust.model.obgyn.ultrasoundRecord;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.DataBean;

import java.util.List;

/**
 * @author Ziyu
 */
public interface ultrasoundRecordData extends DataBean<ultrasoundRecord> {

    /**
     * Retrieves the patient's ultrasound records.
     *
     * @param patientMID
     * @return a list of ultrasound records of this patient
     */
    public List<ultrasoundRecord> getUltrasoundRecordForPatient(long patientMID) throws DBException;

    /**
     * Add ultrasound record to the database and return the generated recordID.
     *
     * @param ur    Ultrasound record to add to the database
     * @return recordID generated from the database insertion, -1 if nothing was generated
     * @throws DBException if error occurred in inserting ultrasound record
     */
    public long addReturnGeneratedId(ultrasoundRecord ur) throws DBException;
}

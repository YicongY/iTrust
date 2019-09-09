package edu.ncsu.csc.itrust.exception;
import java.sql.SQLException;
import java.util.List;
import java.util.Collections;

/**
 * The reasoning behind this wrapper exception is security. When an SQL Exception gets thrown all the way back
 * to the JSP, we begin to reveal details about our database (even knowing that it's MySQL is bad!) So, we
 * make a wrapper exception with a vague description, but we also keep track of the SQL Exception for
 * debugging and testing purposes.
 * Return Error List to front end
 * 
 *  
 * 
 */
public class DBException extends ITrustException {
	private static final long serialVersionUID = -6554118510590118376L;
	private SQLException sqlException = null;
	private List<String> errorList = Collections.emptyList();;

	public DBException(SQLException e) {
		super("A database exception has occurred. Please see the log in the console for stacktrace" + e.toString());
		this.sqlException = e;
	}
	
	public void setErrorList(List<String> errorList) {
		this.errorList = errorList;
	}
	public List<String> getErrorList() {
		return errorList;
	}
	/**
	 * @return The SQL Exception that was responsible for this error.
	 */
	public SQLException getSQLException() {
		return sqlException;
	}
	
	public String getErrorString() {
		return sqlException.toString();
	}
	@Override
	public String getExtendedMessage() {
		if (sqlException != null)
			return sqlException.getMessage();
		else
			return super.getExtendedMessage();
	}
}

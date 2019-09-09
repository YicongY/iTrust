<%@taglib prefix="itrust" uri="/WEB-INF/tags.tld"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="edu.ncsu.csc.itrust.model.old.dao.DAOFactory"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.action.EditPatientAction"%>
<%@page import="edu.ncsu.csc.itrust.BeanBuilder"%>
<%@page import="edu.ncsu.csc.itrust.model.old.enums.Ethnicity"%>
<%@page import="edu.ncsu.csc.itrust.model.old.enums.BloodType"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>
<%@page import="edu.ncsu.csc.itrust.model.old.enums.Gender"%>
<%@page import="java.time.LocalDate"%>
<%@page import="java.time.ZoneId"%>
<%@page import="java.sql.Date"%>
<%@page import="java.text.SimpleDateFormat"%> 
<%@page import="java.text.DateFormat"%>
<%@page import="edu.ncsu.csc.itrust.controller.obgyn.initialRecord.InitialRecordController"%>
<%@page import="java.lang.Long"%>
<%@page import="java.lang.Boolean"%>	
<%@page import="java.util.ArrayList"%>
<%@page import="java.lang.Integer"%>
<%@page import="edu.ncsu.csc.itrust.model.obgyn.initialRecord.initialRecord"%>
<%@page import="java.util.List"%>
<%@include file="/global.jsp"%>

<%
    pageTitle = "iTrust - OB Record Detail";
%>

<%@include file="/header.jsp"%>
<h2>Obstetrics(OB) Record Detail</h2>
</br>
<%
	// retrieve OB record data
	initialRecord record = null;
	List<initialRecord> list = (List<initialRecord>)session.getAttribute("OBRecords");
	int recordIdx = Integer.valueOf(request.getParameter("recordIdx"));
	if (list != null && list.size() > 0) {
		if (recordIdx >= 0 && recordIdx < list.size()) {
			record = list.get(recordIdx);
		}
	}
	if (record == null) {
		return;
	}
	// create date format
	// DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
	//DateFormat inputFormat = new SimpleDateFormat("yyyy-mm-dd);
	//Date inputDate = inputFormat.parse()
	String pid = (String) session.getAttribute("pid");
	//create controller
	InitialRecordController inic = new InitialRecordController(loggedInMID.longValue());
	//Log view OB record action
	//inic.addOBRecordViewLog(pid, dateFormat.format(record.getEDD()));
%>
<table width=50%>
    <tr>
        <td><p><b>Patient Name: <%=(String) session.getAttribute("first_name") + " "
                                    + (String) session.getAttribute("last_name")%></b></p></td>
        <td><p><b>Patient ID: <%=pid%></b></p></td>
    </tr>
</table>

<table class='fTable' width=50%><tr><th width=50%>Field</th><th width=50%>Value</th></tr>
    <tr>
        <td>Date</td>
        <td><%=record.getRecordDate() == null ? "N/A" : record.getRecordDate()%></td> 
    </tr>
    <tr>
        <td>LMP</td>
        <td><%=record.getLMP() == null ? "N/A" : record.getLMP()%></td>
    </tr>
    <tr>
        <td>EDD</td>
        <td><%=record.getEDD() == null ? "N/A" : record.getEDD()%></td>
    </tr>
    <tr>
        <td>Year of conception</td>
        <td><%=record.getYearOfConception()%></td>
    </tr>
    <tr>
        <td>Weeks of pregnant</td>
        <td><%=record.getNumberOfWeeksPregnant()%></td>
    </tr>
    <tr>
        <td>Hours in labor</td>
        <td><%=record.getNumberOfHoursInLabor()%></td>
    </tr>
    <tr>
        <td>Weight Gain (lbs)</td>
        <td><%=record.getWeightGainDuringPregnancy()%></td>
    </tr>
    <tr>
        <td>Delivery type</td>
        <td><%=record.getDeliveryType()%></td>
    </tr>
    <tr>
        <td>Number of baby</td>
        <td><%=record.getPregnancyType()%></td>
    </tr>
</table>
<%@include file="/footer.jsp"%>
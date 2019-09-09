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
<%@page import="edu.ncsu.csc.itrust.model.old.beans.PersonnelBean"%>
<%@page import="edu.ncsu.csc.itrust.action.EditPersonnelAction"%>
<%@page import="edu.ncsu.csc.itrust.BeanBuilder"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>
<%@page import="edu.ncsu.csc.itrust.model.old.enums.TransactionType"%>
<%@page import="edu.ncsu.csc.itrust.model.old.enums.Role"%>
<%@page import="java.time.format.DateTimeFormatter"%>
<%@page import="edu.ncsu.csc.itrust.model.obgyn.initialRecord.initialRecord"%>
<%@page import="edu.ncsu.csc.itrust.controller.obgyn.obOfficeVisit.obOfficeVisitController"%>
<%@page import="edu.ncsu.csc.itrust.controller.obgyn.initialRecord.InitialRecordController"%>
<%@page import="java.lang.Long"%>
<%@page import="java.lang.Boolean"%>
<%@page import="java.util.ArrayList"%>
<%@page import="edu.ncsu.csc.itrust.logger.TransactionLogger"%>
<%@page import="edu.ncsu.csc.itrust.model.obgyn.obOfficeVisit.obOfficeVisit"%>
<%@page import="java.util.List"%>

<%@include file="/global.jsp"%>



<%@include file="/header.jsp"%>

<title> OB Office Visit</title>

<%
     // retrieve OB record data
    String pidString = (String) session.getAttribute("pid");
    String first_name = (String) session.getAttribute("first_name");
    String last_name = (String) session.getAttribute("last_name");
    obOfficeVisitController ovControll = new obOfficeVisitController(loggedInMID.longValue());
    
    String record_idx_string = request.getParameter("RECORD_INDEX");

    if (record_idx_string == null || record_idx_string.equals("") || record_idx_string.length() < 1 ) {
        response.sendRedirect("iTrust/auth/hcp/viewObOfficeVisit.jsp"); 
        return;      
    }


    
    
    
    
    //List<obOfficeVisit> records = (List<obOfficeVisit>) session.getAttribute("VisitRecords");
    //obOfficeVisit record = records.get(record_idx);
    //Update view

    obOfficeVisit record = ovControll.getOBOfficeVisitByVisitID(Long.valueOf(record_idx_string));
    //log view 
    TransactionLogger.getInstance().logTransaction(TransactionType.VIEW_OBSTETRICS_OV, loggedInMID, record.getPatientMID(), Long.toString(record.getVisiteId()));
    // create date formatter
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    String action = (String) request.getAttribute("action");

    if (action != null && action.equals("BACK")){
        response.sendRedirect("/iTrust/auth/hcp/viewObOfficeVisit.jsp");
        return;
    }
%>

<h2>iTrust - View OB Visit Record Detail</h2>
</br>

<table width=75%>
    <tr>
        <td><b>Patient Name: <%=first_name + " " + last_name%></b></td>
        <td align="right"><b>Patient ID: <%=pidString%></b></td>
    </tr>
</table>

<table class='fTable' width=75%>
    <tr>
        <th width=60%>Visit Information</th>
        <th width=60%></th>
    </tr>
    <tr>
        <td>Date:</td>
        <td><%=record.getCurrentDate().format(dateFormatter)%></td>
    </tr>
    <tr>
        <td>Number of Weeks Pregnant: </td>
        <td><%=record.getNumOfWeeks()%></td>
    </tr>
    <tr>
        <td>Weight: </td>
        <td><%=record.getWeight()%></td>
    </tr>
    <tr>
        <td>Blood Pressure: </td>
        <td><%=record.getBloodPressure()%></td>
    </tr>
    <tr>
        <td>Fetal heart rate: </td>
        <td><%=record.getFetalHeartRate()%></td>
    </tr>
    <tr>
        <td>Number of Baby: </td>
        <td><%=record.getNumOfMultiplePregnancy()%></td>
    </tr>
    <tr>
        <td>low lying placenta observed: </td>
        <td><%=record.isLowLyingPlacentaObserved()%></td>
    </tr>
</table>
<form id="back" action="viewObOfficeVisit.jsp" method="post">
    <div>
        <input type="submit" name="action" style="font-size: 16pt; font-weight: bold;" value="BACK">
    </div>
</form>

<%@include file="/footer.jsp"%>
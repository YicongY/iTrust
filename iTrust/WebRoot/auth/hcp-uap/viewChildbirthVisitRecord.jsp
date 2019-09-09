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
<%@page import="edu.ncsu.csc.itrust.controller.obgyn.initialRecord.InitialRecordController"%>
<%@page import="java.lang.Long"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.lang.Boolean"%>
<%@page import="java.util.ArrayList"%>
<%@page import="edu.ncsu.csc.itrust.model.obgyn.initialRecord.initialRecord"%>
<%@page import="java.util.List"%>
<%@page import="edu.ncsu.csc.itrust.model.obgyn.childBirthVisit.ChildBirthVisit"%>
<%@page import="edu.ncsu.csc.itrust.controller.obgyn.childBirthVisit.ChildBirthVisitController"%>
<%@page import="java.util.Collections"%>

<%@include file="/global.jsp"%>

<%
    pageTitle = "iTrust - View Childbirth Visit Record";
%>

<%@include file="/header.jsp"%>

<h2>View Childbirth Visit Record</h2>
</br>

<%
    /* Require a Patient ID first */
    String pidString = (String) session.getAttribute("pid");
    String first_name = (String) session.getAttribute("first_name");
    String last_name = (String) session.getAttribute("last_name");
    String eligibility = (String) session.getAttribute("eligibility");
    if (pidString == null || pidString.equals("") || pidString.length() < 1) {
        out.println("pidstring is null");
        response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=hcp-uap/viewChildbirthVisitRecord.jsp");
        return;
    }
    String eligibility_string = "false";
    if (eligibility != null && eligibility.equals("true")) {
        eligibility_string = "true";
    }
    // create date formatter
    SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy");
    // when form is triggered
    String action_name = request.getParameter("action");
    if (action_name != null) {
        if (action_name.equals("Update")) {
            // get new eligibility
            String new_eligibility = request.getParameter("eligibility");
            if (!new_eligibility.equals(eligibility_string)) {
                // if new eligibility not equals old eligibility, then update
                InitialRecordController inic = new InitialRecordController(loggedInMID.longValue());
                boolean succeed = inic.updateOBeligibility(pidString, Boolean.valueOf(new_eligibility));
                if (succeed) {
                    session.setAttribute("eligibility", new_eligibility);
                    eligibility = new_eligibility;
                    eligibility_string = new_eligibility;
%>
<p style="color:green;">Eligibility Successfully Updated</p>
<%
                } else {
%>
<p style="color:red;">Failed updating Eligibility</p>
<%
                }
            } else {
%>
<p style="color:red;">Nothing to update...</p>
<%
            }
        } else if (action_name.equals("Add Visit Record")) {
            if (eligibility.equals("true")) {
                response.sendRedirect("editChildbirthVisitRecord.jsp?forward=hcp-uap/viewChildbirthVisitRecord.jsp&UID_PATIENTID=" + pidString);
            } else {
%>
<p style="color:red;">Only patients with eligibility of "true" can have new childbirth visit</p>
<%
            }
        }
    }


    /* Require a Personnel ID first */
    String personnelID= "" + loggedInMID.longValue();
    /* A bad personnel ID gets you exiled to the exception handler */
    EditPersonnelAction personnelEditor = new EditPersonnelAction(prodDAO,loggedInMID.longValue(), personnelID);
    long pid  = personnelEditor.getPid();
    PersonnelBean personnelForm = prodDAO.getPersonnelDAO().getPersonnel(pid);
    String speciality = personnelForm.getSpecialty();

    /* If the patient id doesn't check out, then kick 'em out to the exception handler */
    EditPatientAction action = new EditPatientAction(prodDAO, loggedInMID.longValue(), pidString);

    // fetch records with given pid
    ChildBirthVisitController cbvc = new ChildBirthVisitController(loggedInMID.longValue());
    List<ChildBirthVisit> records = null;
    try {
        records = cbvc.getChildBirthVisitByDate(pidString);
        session.setAttribute("childbirthVisitRecords", records);
    } catch (Exception e) {
%>
<p style="color:red;">Unable to fetch records..</p>
<%
    }
    int num_record = (records != null) ? records.size() : 0;

    // logging "DEMOGRAPHICS_VIEW" action
    PatientBean p = action.getPatient();
    loggingAction.logEvent(TransactionType.DEMOGRAPHICS_VIEW, loggedInMID.longValue(), p.getMID(), "");
%>
<table width=80%>
    <tr>
        <td><p><b>Patient Name: <%=first_name + " " + last_name%></b></p></td>
        <td><p><b>Patient ID: <%=pidString%></b></p></td>
        <%
            if (speciality != null && speciality.equals("OB/GYN")) {
        %>
        <td align="right">
            <form id="changeEligibility" action="viewChildbirthVisitRecord.jsp" method="post">
                <b>Eligibility: </b>
                <select name="eligibility">
                    <option value="false" <%=eligibility_string.equals("false") ? "selected" : ""%>>false</option>
                    <option value="true" <%=eligibility_string.equals("true") ? "selected" : ""%>>true</option>
                </select>
                <%--<input type="submit" name="action" style="height: 22px; align: center; font-size: 8pt; font-weight: bold;" value="Update">--%>
                <button type="submit" name="action" style="height: 22px; font-size: 8pt; font-weight: bold;" value="Update">Update</button>
            </form>
        </td>
        <%
            } else {
        %>
        <td align="right"><p align="right"><b>Eligibility: <%=eligibility_string%></b></p></td>
        <%
            }
        %>
    </tr>
</table>
<%
    if (num_record > 0) {
%>
<table class='fTable' width=80%><tr><th width=50%>Date</th><th width=20%>Action</th></tr>
    <%
        if (speciality != null && speciality.equals("OB/GYN")) {
            for (int i = 0; i < num_record; i++) {
                ChildBirthVisit record = records.get(i);
    %>
    <tr>
        <td><%=dateFormatter.format(record.getDeliveryDate())%></td>
        <td><input type='button' style='width:100px;' onclick="parent.location.href='editChildbirthVisitRecord.jsp?RECORD_INDEX=<%=i%>';" value="view/edit"></td>
    </tr>
    <%
            }
        } else {
            for (int i = 0; i < num_record; i++) {
                ChildBirthVisit record = records.get(i);
    %>
    <tr>
        <td><%=dateFormatter.format(record.getDeliveryDate())%></td>
        <td><input type='button' style='width:100px;' onclick="parent.location.href='detailChildbirthVisitRecord.jsp?RECORD_INDEX=<%=i%>';" value="view"></td>
    </tr>
    <%
            }
        }
    %>
</table>
<%
    } else {
%>
<p>No childbirth hospital visit record found</p>
<%
    }
%>
<br><br>
<form id="addNewRecord" action="viewChildbirthVisitRecord.jsp" method="post">
    <div align="center">
        <input type="submit" name="action" style="font-size: 16pt; font-weight: bold;" value="Add Visit Record">
    </div>
</form>


<%@include file="/footer.jsp"%>
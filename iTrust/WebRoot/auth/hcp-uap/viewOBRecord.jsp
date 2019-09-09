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
<%@page import="java.lang.Boolean"%>	
<%@page import="java.util.ArrayList"%>
<%@page import="edu.ncsu.csc.itrust.model.obgyn.initialRecord.initialRecord"%>
<%@page import="java.util.List"%>

<%@include file="/global.jsp"%>

<%
    pageTitle = "iTrust - View OB Record";
%>

<%@include file="/header.jsp"%>

<h2>Obstetrics(OB) Record</h2>
</br>

<%
    /* Require a Patient ID first */
    String pidString = (String) session.getAttribute("pid");
    String first_name = (String) session.getAttribute("first_name");
    String last_name = (String) session.getAttribute("last_name");
    String eligibility = (String) session.getAttribute("eligibility");
    if (pidString == null || pidString.equals("") || pidString.length() < 1) {
        out.println("pidstring is null");
        response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=hcp-uap/viewOBRecord.jsp&showEligibility=true");
        return;
    }
    String eligibility_string = "false";
    if (eligibility != null && eligibility.equals("true")) {
        eligibility_string = "true";
    }
    // form is triggered
    InitialRecordController inic = new InitialRecordController(loggedInMID.longValue());
    String action_name = request.getParameter("action");
    if (action_name != null) {
        if (action_name.equals("Update")) {
            // update eligibility
         
            String new_eligibility = request.getParameter("eligibility");
            if (new_eligibility == null || new_eligibility.equals(eligibility_string)) {
%>
<p style="color:red;">Nothing to update...</p>
<%
            } else {
                boolean succeed = inic.updateOBeligibility(pidString, Boolean.valueOf(new_eligibility));
                if (succeed) {
                    session.setAttribute("eligibility", new_eligibility);
                    eligibility_string = new_eligibility;
%>
<p style="color:green;">Eligibility Successfully Updated</p>
<%
                } else {
%>
<p style="color:red;">Failed updating Eligibility</p>
<%
                }
            }
        } else if (action_name.equals("Add New Record")) {
            if (eligibility.equals("true")) {
                response.sendRedirect("addOBRecord.jsp?forward=hcp-uap/viewOBRecord.jsp&UID_PATIENTID=" + pidString);
            } else {
            %>
                <p style="color:red;">Only patients with eligibility of 'Yes' can have new records</p>
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
    List<initialRecord> list = new ArrayList<initialRecord>();
    list = inic.getInitialRecordsByDate(pidString);
	int num_record = list.size();
	session.setAttribute("OBRecords", list);
	
    /* Now take care of updating information */
    boolean formIsFilled = request.getParameter("formIsFilled") != null && request.getParameter("formIsFilled").equals("true");
    PatientBean p;
    if (formIsFilled) {
        p = new BeanBuilder<PatientBean>().build(request.getParameterMap(), new PatientBean());
        try {
            action.updateInformation(p);
            loggingAction.logEvent(TransactionType.DEMOGRAPHICS_EDIT, loggedInMID.longValue(), p.getMID(), "");
%>
    <br />
    <div align=center>
        <span class="iTrustMessage">Information Successfully Updated</span>
    </div>
    <br />
<%
        } catch (FormValidationException e) {
%>
    <br />
    <div align=center>
        <span class="iTrustError"><%=StringEscapeUtils.escapeHtml(e.getMessage()) %></span>
    </div>
    <br />
<%
        }
    } else {
        p = action.getPatient();
        loggingAction.logEvent(TransactionType.DEMOGRAPHICS_VIEW, loggedInMID.longValue(), p.getMID(), "");
    }
    if (speciality != null && speciality.equals("OB/GYN")) {
%>
    <form id="changeEligibility" action="viewOBRecord.jsp" method="post">
        <table width=80%>
            <tr>
                <td><p><b>Patient Name: <%=first_name + " " + last_name%></b></p></td>
                <td><p><b>Patient ID: <%=pidString%></b></p></td>
                <td><p align="right"><b>Eligibility: </b></p></td>
                <td><select name="eligibility">
                    <option value="false" <%=eligibility_string.equals("false") ? "selected" : ""%>>false</option>
                    <option value="true" <%=eligibility_string.equals("true") ? "selected" : ""%>>true</option>
                </select></td>
                <td><input type="submit" name="action" style="font-size: 8pt; font-weight: bold;" value="Update"></td>
            </tr>
        </table>
    </form>
<%
    } else {
%>
    <table width=80%>
        <tr>
            <td><p><b>Patient Name: <%=first_name + " " + last_name%></b></p></td>
            <td><p><b>Patient ID: <%=pidString%></b></p></td>
            <td><p align="right"><b>Eligibility: <%=eligibility_string%></b></p></td>
        </tr>
    </table>
<%
    }
    if (num_record > 0) {
        %>
            <table class='fTable' width=80%><tr><th width=50%>Date</th><th width=20%>Action</th></tr>
        <%
        for (int i = 0; i < num_record; i++) {
        	initialRecord record = list.get(i);
            %>
            <tr>
                <td><%=record.getRecordDate()%></td>
                <td><input type='button' style='width:100px;' onclick="parent.location.href='detailOBRecord.jsp?recordIdx=<%=i%>';" value="view"></td>
            </tr>
            <%
        }
            %></table><%
    } else {
        %><p>No record found</p><%
    }
    if (speciality != null && speciality.equals("OB/GYN")) {
        %>
        <br><br>
        <form id="addNewRecord" action="viewOBRecord.jsp" method="post">
            <div align="center">
                <input type="submit" name="action" style="font-size: 16pt; font-weight: bold;" value="Add New Record">
            </div>
        </form>
        <%
    } else {
        %>
        <div align="left">
            <br>
            <p><b>Only doctor with speciality OB/GYN can add new record</b></p>
        </div>
        <%
    }
%>


<%@include file="/footer.jsp"%>






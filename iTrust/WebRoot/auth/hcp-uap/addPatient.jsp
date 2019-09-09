<%@taglib prefix="itrust" uri="/WEB-INF/tags.tld"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="edu.ncsu.csc.itrust.action.AddPatientAction"%>
<%@page import="edu.ncsu.csc.itrust.BeanBuilder"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>
<%@page import="edu.ncsu.csc.itrust.controller.obgyn.report.reportController"%>
<%@page import="edu.ncsu.csc.itrust.model.obgyn.report.pastPregnancy"%>
<%@page import="edu.ncsu.csc.itrust.exception.DBException"%>
<%@page import="java.lang.Long"%>
<%@page import="java.lang.Boolean"%>


<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - Add Patient";
%>

<%@include file="/header.jsp" %>

<%

String pidString = request.getParameter("patientMID");
long patientMID = 0;
if (pidString != null) {
    System.out.println("Pid not null!");
	patientMID = Long.valueOf(pidString);
}

	System.out.println("Action: " + request.getParameter("action"));

	if (request.getParameter("action") != null && request.getParameter("action").equals("Add Record")) {
		System.out.println("Enter add pregnancy record");
		reportController rc = new reportController(loggedInMID.longValue());
		pastPregnancy newPastPregnancy = new pastPregnancy();
		newPastPregnancy.setPregnancyTerm(Integer.parseInt(request.getParameter("pregnancyterm")));
		newPastPregnancy.setDeliveryType(request.getParameter("deliverytype"));
		newPastPregnancy.setConceptionYear(Integer.parseInt(request.getParameter("yearofconception")));
		newPastPregnancy.setNumberOfHoursInLabor(Integer.parseInt(request.getParameter("numberofhour")));
		newPastPregnancy.setWeightGain(Integer.parseInt(request.getParameter("weightgain")));
		newPastPregnancy.setPatientMID(Long.parseLong(request.getParameter("patientMID")));
		boolean succeed = true;

		try {
			System.out.println("Ready for Controller's function");
			rc.insertPastPregnancyRecord(newPastPregnancy);
		} catch (DBException e) {
			System.out.println("=============Hit Past Pregnancy DB Exception============");
			succeed = false;
		}

		if (succeed) { %>
<p style="color:green;">Past Pregnancy Record Successfully Added, redirect in 3 seconds...</p>
<script type="text/javascript">
    function sleep (time) {
        return new Promise((resolve) => setTimeout(resolve, time));
    }

    // sleep for 3 seconds
    sleep(3000).then(() => {
        // redirect to ob record page
        // window.location.replace("viewChildbirthVisitRecord.jsp");
    })
</script>
<%}
}

boolean formIsFilled = request.getParameter("formIsFilled") != null && request.getParameter("formIsFilled").equals("true");
if (formIsFilled) {
	//This page is not actually a "page", it just adds a user and forwards.
	PatientBean p = new BeanBuilder<PatientBean>().build(request.getParameterMap(), new PatientBean());
	try{
		boolean isDependent = false;
		long representativeId = -1L;
		if(request.getParameter("isDependent") != null && request.getParameter("isDependent").equals("on")){
			isDependent = true;
		}
		
		if(request.getParameter("repId") != "" && isDependent){
			representativeId = Long.valueOf(request.getParameter("repId"));
		}else if(isDependent && request.getParameter("repId") == ""){
			throw new FormValidationException("Representative MID must be filled if the patient is marked as a dependent.");
		}
		long newMID = 1L; 
		if(isDependent){
			newMID = new AddPatientAction(prodDAO, loggedInMID.longValue()).addDependentPatient(p, representativeId, loggedInMID.longValue());
		}else{
			newMID = new AddPatientAction(prodDAO, loggedInMID.longValue()).addPatient(p, loggedInMID.longValue());
		}
		session.setAttribute("pid", Long.toString(newMID));
		String fullname;
		String password;
		password = p.getPassword();
		fullname = p.getFullName();

		String[] deliveryTypes = {"", "vaginal delivery", "vaginal delivery vacuum assist", "vaginal delivery forceps assist", "caesarean section", "miscarriage"};
		String deliveryType = "";


%>
	<div align=center>
		<span class="iTrustMessage">New patient <%= StringEscapeUtils.escapeHtml("" + (fullname)) %> successfully added!</span>
		<br />
		<table class="fTable">
			<tr>
				<th colspan=2>New Patient Information</th>
			</tr>
			<tr>
				<td class="subHeaderVertical">MID:</td>
				<td><%= StringEscapeUtils.escapeHtml("" + (newMID)) %></td>
			</tr>
			<tr>
				<td class="subHeaderVertical">Temporary Password:</td>
				<td><%= StringEscapeUtils.escapeHtml("" + (password)) %></td>
			</tr>
		</table>
		<br />Please get this information to <b><%= StringEscapeUtils.escapeHtml("" + (fullname)) %></b>! 
		<p>
			<a href = "/iTrust/auth/hcp-uap/editPatient.jsp">Continue to patient information.</a>
		</p>
	</div>


	<div align=center>
		<form id="insertPregnancyRecord" action="addPatient.jsp" method="post">
			<table class='fTable' width=80% BORDERCOLOR=white>
				<tr>
					<th width=30%>Information</th>
					<th width=35%></th>
					<th width=35% style="background-color: white"></th>
				</tr>
				<tr>
					<td>Pregnancy Term (Weeks):</td>
					<td><input name="pregnancyterm" type="text" placeholder="20"></td>
				</tr>
				<tr>
					<td>Delivery Types:</td>
					<td>
						<select name="deliverytype">
							<%
								for (String t: deliveryTypes) {
							%>
									<option value="<%=t%>" <%=t.equals(deliveryType) ? "selected" : ""%>><%=t.equals("") ? "select one" : t%></option>
							<%
								}
							%>
						</select>
					</td>
				</tr>
				<tr>
					<td>Year of Conception:</td>
					<td><input name="yearofconception" type="text" placeholder="2018"></td>
				</tr>
				<tr>
					<td>Number of Hours in Labor:</td>
					<td><input name="numberofhour" type="text" placeholder="5(hours)"></td>
				</tr>
				<tr>
					<td>Weight Gain During Pregnancy:</td>
					<td><input name="weightgain" type="text" placeholder="10(kg)"></td>
				</tr>
				<tr>
					<td>Patient MID:</td>
					<td><input name="patientMID" type="text" value=<%=patientMID%>></td>
				</tr>

			</table>

			<div align="center">
				<input type="submit" name="action" style="font-size: 16pt; font-weight: bold;" value="Add Record">
			</div>
		</form>


	</div>
<%
	} catch(FormValidationException e){
%>
	<div align=center>
		<span class="iTrustError"><%=StringEscapeUtils.escapeHtml(e.getMessage()) %></span>
	</div>
<%
	}
}
%>

<div align=center>
<form action="addPatient.jsp" method="post">
	<input type="hidden" name="formIsFilled" value="true"> <br />
<br />
<div style="width: 50%; text-align:left;">Please enter in the name of the new
patient, with a valid email address. If the user does not have an email
address, use the hospital's email address, [insert pre-defined email],
to recover the password.</div>
<br />
<br />
<table class="fTable">
	<tr>
		<th colspan=2>Patient Information</th>
	</tr>
	<tr>
		<td class="subHeaderVertical">First name:</td>
		<td><input type="text" name="firstName"></td>
	</tr>
	<tr>
		<td class="subHeaderVertical">Last Name:</td>
		<td><input type="text" name="lastName">
	</tr>
	<tr>
		<td class="subHeaderVertical">Email:</td>
		<td><input type="text" name="email"></td>
	</tr>
</table>
<br />
<input type="checkbox" id="isDependent" name="isDependent"><i>Make this patient a dependent</i><br>
<i>Representative ID</i><input type="text" id="repId" name="repId"><br>
<input type="submit" style="font-size: 16pt; font-weight: bold;" value="Add patient">
</form>
<br />
</div>
<%@include file="/footer.jsp" %>

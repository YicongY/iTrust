<%@page import="java.util.zip.DataFormatException"%>
<%@page import="com.mysql.fabric.xmlrpc.base.Data"%>
<%@page import="java.util.Locale"%>
<%@page import="edu.ncsu.csc.itrust.model.obgyn.initialRecord.initialRecord"%>
<%@include file="/global.jsp" %>
<%@page import="java.util.List" %>
<%@page import="edu.ncsu.csc.itrust.action.SearchUsersAction" %>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.PatientBean" %>
<%@page import="java.time.LocalDate"%>
<%@page import="java.time.ZoneId"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.lang.Integer"%>
<%@page import="java.lang.Float"%>
<%@page import="java.util.Date"%>
<%@page import="java.lang.Long"%>
<%@page import="edu.ncsu.csc.itrust.exception.DBException"%>
<%@page import="edu.ncsu.csc.itrust.controller.obgyn.initialRecord.InitialRecordController"%>
<%@page import="java.util.Collections"%>


<%
    pageTitle = "iTrust - Add new OB record";
%>

<%@include file="/header.jsp"%>

<h2>Create Obstetrics(OB) Record</h2>
<br />
<%!
	public void removeAttributes(HttpSession session) {
	    session.removeAttribute("date");
		session.removeAttribute("lmp");
		session.removeAttribute("edd");
		session.removeAttribute("conceptionYear");
		session.removeAttribute("weeksOfPregnant");
		session.removeAttribute("laborHour");
		session.removeAttribute("weightGain");
		session.removeAttribute("delivery");
		session.removeAttribute("numBaby");
	}
%>
<%
    String pidString = (String) session.getAttribute("pid");
    String first_name = (String) session.getAttribute("first_name");
    String last_name = (String) session.getAttribute("last_name");
	
    // if pid is null, return
    if (pidString == null || pidString.equals("") || pidString.length() < 1) {
        out.println("pidstring is null");
        response.sendRedirect("hcp-uap/viewOBRecord.jsp&showEligibility=true");
        return;
    }
	// create date format
	DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    // get newRecord data
    initialRecord newRecord = (initialRecord) session.getAttribute("newRecord");
	Date today = new Date();
    if (newRecord == null) {
        removeAttributes(session);
        newRecord = new initialRecord();
        newRecord.setMID(Long.parseLong(pidString));
        newRecord.setRecordDate(new java.sql.Date(today.getTime()));
		// get previous record if possible
		List<initialRecord> list = (List<initialRecord>)session.getAttribute("OBRecords");
		if (list != null && list.size() > 0) {
			initialRecord prevRecord = list.get(list.size() - 1);
			newRecord.setYearOfConception(prevRecord.getYearOfConception());
			newRecord.setNumberOfWeeksPregnant(prevRecord.getNumberOfWeeksPregnant());
			newRecord.setNumberOfHoursInLabor(prevRecord.getNumberOfHoursInLabor());
			newRecord.setWeightGainDuringPregnancy(prevRecord.getWeightGainDuringPregnancy());
			newRecord.setDeliveryType(prevRecord.getDeliveryType());
			newRecord.setPregnancyType(prevRecord.getPregnancyType());
		}
		// set session attributes
		session.setAttribute("date", dateFormat.format(today));
		Date lmp = (newRecord.getLMP() != null) ? new Date(newRecord.getLMP().getTime()) : null;
		session.setAttribute("lmp", lmp == null ? "" : dateFormat.format(lmp));
		Date edd = (newRecord.getEDD() != null) ? new Date(newRecord.getEDD().getTime()) : null;
		session.setAttribute("edd", edd == null ? "" : dateFormat.format(edd));
		session.setAttribute("conceptionYear", newRecord.getYearOfConception());
		if (newRecord.getNumberOfWeeksPregnant() == null || newRecord.getNumberOfWeeksPregnant().equals("null")) {
			session.setAttribute("weeksOfPregnant", "");
		} else {
			session.setAttribute("weeksOfPregnant", newRecord.getNumberOfWeeksPregnant());
		}
		session.setAttribute("laborHour", newRecord.getNumberOfHoursInLabor());
		session.setAttribute("weightGain", newRecord.getWeightGainDuringPregnancy());
		String delivery = (newRecord.getDeliveryType() != null) ? newRecord.getDeliveryType() : "";
		session.setAttribute("delivery", delivery.equals("null") ? "" : delivery);
		session.setAttribute("numBaby", newRecord.getPregnancyType());
	}

	// check if "create new record" form is submitted
    String action = request.getParameter("action");
    if (action != null && action.equals("Add Record")) {
    	boolean isValid = true;
    	InitialRecordController inic = new InitialRecordController(loggedInMID.longValue());
    	// check recordDate
    	try {
			java.sql.Date recordDate = new java.sql.Date(dateFormat.parse(request.getParameter("date")).getTime());
	    	newRecord.setRecordDate(recordDate);
    	} catch (Exception e) {
    		isValid = false;
    		%><p style="color:red;">Invalid date</p><%
    	}
    	// check LMP
    	try {
	    	java.sql.Date lmp = new java.sql.Date(dateFormat.parse(request.getParameter("lmp")).getTime());
			newRecord.setLMP(lmp);
    	} catch (Exception e) {
    		isValid = false;
    		%><p style="color:red;">Invalid LMP</p><%
    	}
    	// check EDD
    	try {
    		java.sql.Date edd = new java.sql.Date(dateFormat.parse(request.getParameter("edd")).getTime());
			newRecord.setEDD(edd);
    	} catch (Exception e) {
    		isValid = false;
    		%><p style="color:red;">Invalid EDD</p><%
    	}
    	// check year of conception
    	try {
    		int conceptionYear = Integer.parseInt(request.getParameter("conceptionYear"));
			newRecord.setYearOfConception(conceptionYear);
    	} catch (Exception e) {
    		isValid = false;
    		%><p style="color:red;">Invalid Year of conception</p><%
    	}
    	// check number of weeks pregnant;
		newRecord.setNumberOfWeeksPregnant(request.getParameter("weeksOfPregnant"));
    	// check hours of labor
    	try {
    		float laborHour = Float.parseFloat(request.getParameter("laborHour"));
			newRecord.setNumberOfHoursInLabor(laborHour);
    	} catch (Exception e) {
    		isValid = false;
    		%><p style="color:red;">Invalid hours in labor</p><%
    	}
    	// check weight gain
    	try {
    		float weightGain = Float.parseFloat(request.getParameter("weightGain"));
			newRecord.setWeightGainDuringPregnancy(weightGain);
    	} catch (Exception e) {
    		isValid = false;
    		%><p style="color:red;">Invalid weight gain</p><%
    	}
    	// check delivery method
		newRecord.setDeliveryType(request.getParameter("delivery"));
		// check pregnant type
		try {
	    	int numBaby = Integer.parseInt(request.getParameter("numBaby"));
	    	newRecord.setPregnancyType(numBaby);
		} catch (Exception e) {
		    isValid = false;
			%><p style="color:red;">Invalid number of baby</p><%
		}
		session.setAttribute("date", request.getParameter("date"));
		session.setAttribute("lmp", request.getParameter("lmp"));
		session.setAttribute("edd", request.getParameter("edd"));
		session.setAttribute("conceptionYear", request.getParameter("conceptionYear"));
		session.setAttribute("weeksOfPregnant", request.getParameter("weeksOfPregnant"));
		session.setAttribute("laborHour", request.getParameter("laborHour"));
		session.setAttribute("weightGain", request.getParameter("weightGain"));
		session.setAttribute("delivery", request.getParameter("delivery"));
		session.setAttribute("numBaby", request.getParameter("numBaby"));
		// check if all inputs are valid
    	if (isValid) {
			List<String> errorList = Collections.emptyList();
    		// upload record
    		Boolean succeed = false;
    		try {
        		errorList = inic.add(newRecord);
        		if (errorList.size() == 0){
        			succeed = true;
        		}
        	} catch (DBException e) {
        		succeed = false;
        	}
    		if (succeed) {
    		    removeAttributes(session);
    	        %>
    	            <p style="color:green;">OB Record Successfully Added, redirect in 3 seconds...</p>
    	            <script type="text/javascript">
    	                function sleep (time) {
    	                    return new Promise((resolve) => setTimeout(resolve, time));
    	                }
    	                // sleep for 3 seconds
    	                sleep(3000).then(() => {
    	                    // redirect to ob record page
    	                    window.location.replace("viewOBRecord.jsp");
    	                })
    	            </script>
    	        <%
    	    } else {
    	    	if (errorList.size() > 0){
    	    		for (int i = 0; i < errorList.size(); i++) {
    	            	String error = errorList.get(i);
    	            	%><p style="color:red;"><%=error%></p><%
    	    		}  	
    	    	}
    	    }
    	}
    }

%>


<table width=60%>
    <tr>
        <td><p><b>Patient Name: <%=first_name + " " + last_name%></b></p></td>
        <td><p><b>Patient ID: <%=pidString%></b></p></td>
    </tr>
</table>

<script type='text/javascript'>
    function update_edd() {
        var intervalID = window.setInterval(function() {
        	var datepicker = document.getElementById("datepicker");
        	if (datepicker.offsetParent === null) {
            	var lmp = new Date($("#lmp").val());
            	var date = new Date($("#date").val());
            	if (!isNaN(lmp.getTime()) && !isNaN(date.getTime())) {
                	var edd = new Date();
                	var num_days = Math.floor((date.getTime() - lmp.getTime())/86400000);
                	var day = num_days%7;
                	var week = Math.floor(num_days/7);
                	edd.setDate(lmp.getDate() + 280);
                    var options = { month: '2-digit', day: '2-digit', year: 'numeric'};
                    $("#edd").val(edd.toLocaleDateString("en-US", options));
                	$("#elapsed").text(week + " week(s) and " + day + " day(s) since LMP");
                	// $("#weeksOfPregnant").text(week + "-" + day);
            	} else {
                	$("#elapsed").text("0 week(s) and 0 day(s) since LMP");
            	}
                window.clearInterval(intervalID);
        	}
		}, 300);
    }
</script>
	
<form id="createNewRecord" action="addOBRecord.jsp" method="post">
	<%--<input name="weeksOfPregnant" id="weeksOfPregnant" type="hidden" value="">--%>
    <table class='fTable' width=60%>
        <tr><th width=40%>Basic Information</th><th width=60%></th></tr>
        <tr>
            <td>Date:</td>
            <td>
				<div>
					<input id="date" name="date" onchange="update_edd()" placeholder="mm/dd/yyyy" value="<%=session.getAttribute("date")%>" type="text">
					<input type="button" value="Select Date" onclick="displayDatePicker('date');">
				</div>
			</td>
        </tr>
        <tr>
            <td>LMP:</td>
            <td>
				<div>
					<input id="lmp" onchange="update_edd()" name="lmp" placeholder="mm/dd/yyyy" value="<%=session.getAttribute("lmp")%>" type="text">
					<input type="button" value="Select Date" onclick="displayDatePicker('lmp');update_edd();">
				</div>
			</td>
        </tr>
        <tr>
            <td>EDD:</td>
			<td>
				<div>
					<input id="edd" name="edd" placeholder="yyyy-mm-dd" value="<%=session.getAttribute("edd")%>" type="text">
					<input type="button" value="Select Date" onclick="displayDatePicker('edd');">
				</div>
			</td>
        </tr>
        <tr align="center">
            <td colspan="2"><p id="elapsed">0 week(s) and 0 day(s) since LMP</p></td>
        </tr>
    </table>
    <br /><br/>
    <table class='fTable' width=60%>
        <tr><th width=50%>Prior Pregnancy</th><th width=20%></th></tr>
        <tr>
            <td>Year of conception:</td>
            <td><input name="conceptionYear" value="<%=session.getAttribute("conceptionYear")%>" type="text"></td>
        </tr>
        <tr>
            <td>Weeks of pregnant:</td>
			<td><input name="weeksOfPregnant" placeholder="ww-dd" value="<%=session.getAttribute("weeksOfPregnant")%>" type="text"></td>
        </tr>
        <tr>
            <td>Hours in labor:</td>
            <td><input name="laborHour" value="<%=session.getAttribute("laborHour")%>" type="text"></td>
        </tr>
        <tr>
            <td>Weight gain (lbs):</td>
            <td><input name="weightGain" value="<%=session.getAttribute("weightGain")%>" type="text"></td>
        </tr>
        <tr>
            <td>Delivery type:</td>
            <td><select name="delivery">
            <%
           	String type = (String) session.getAttribute("delivery");
          	String[] types = {"", "vaginal delivery", "vaginal delivery vacuum assist", "vaginal delivery forceps assist", "caesarean section", "miscarriage"};
           	for (String t: types) {
			%><option value="<%=t%>" <%=t.equals(type) ? "selected" : ""%>><%=t.equals("") ? "select one" : t%></option><%
       		}
           	%>
            </select></td>
        </tr>
        <tr>
            <td>Number of baby:</td>
            <td><input name="numBaby" value="<%=session.getAttribute("numBaby")%>" type="text"></td>
        </tr>
    </table>
    <br/><br/>
    <div align="center">
        <input type="submit" name="action" style="font-size: 16pt; font-weight: bold;" value="Add Record">
    </div>
</form>
<%@include file="/footer.jsp"%>
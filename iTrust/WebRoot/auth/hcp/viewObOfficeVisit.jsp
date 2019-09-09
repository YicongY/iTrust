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
<%@page import="edu.ncsu.csc.itrust.controller.obgyn.report.reportController"%>
<%@page import="java.lang.Long"%>
<%@page import="java.lang.Boolean"%>
<%@page import="java.lang.Math"%>
<%@page import="java.util.ArrayList"%>
<%@page import="edu.ncsu.csc.itrust.logger.TransactionLogger"%>
<%@page import="edu.ncsu.csc.itrust.model.obgyn.obOfficeVisit.obOfficeVisit"%>
<%@page import="edu.ncsu.csc.itrust.model.obgyn.report.report"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Date"%>

<%@include file="/global.jsp"%>


<title> OB Office Visit</title>
<%
    pageTitle = "iTrust - View OB Visit Record";
%>

<%@include file="/header.jsp"%>

<h2 id= "test_id2">Obstetrics(OB) Office Visit</h2>

</br>

<%
        /* Require a Patient ID first */
    String pidString = (String) session.getAttribute("pid");
    String first_name = (String) session.getAttribute("first_name");
    String last_name = (String) session.getAttribute("last_name");
    String eligibility = (String) session.getAttribute("eligibility");

    String delete_idx = (String) request.getParameter("DELETE_INDEX"); 
    int delete_id = 0;


    

    InitialRecordController inic = new InitialRecordController(loggedInMID.longValue());
    reportController reportcon = new reportController(loggedInMID.longValue());
    if (pidString == null || pidString.equals("") || pidString.length() < 1) {
        out.println("pidstring is null");
        response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=hcp/viewObOfficeVisit.jsp&showEligibility=true");
        return;
    }
    //E3 NOT OB PATIENT
    if (eligibility != null && eligibility.equals("false")){
        %>
        <p style="color:red;">The patient you searched is not eligible for obstetric care.Please update eligbility and initial a record first.</p>
         <script type="text/javascript">
            function sleep (time) {
                return new Promise((resolve) => setTimeout(resolve, time));
            }
            // sleep for 2 seconds
            sleep(3000).then(() => {
                window.location.replace("/iTrust/auth/getPatientID.jsp?forward=hcp/viewObOfficeVisit.jsp&showEligibility=true");
            })
        </script>
        <%
         return;
    }
    //Check whether the patient has initial a record
    List<initialRecord> inirecords = new ArrayList<initialRecord>();

    try {
        inirecords = inic.getInitialRecordsByDate(pidString);
        
    } catch (Exception e) {
        %>
            <p style="color:red;">Unable to fetch ob initial records..</p>
        <%
    }

    if (inirecords.size() == 0){
        %>
        <p style="color:red;">The patient you searched hasn't initialized an OB record yet. Please initial a record first.</p>
         <script type="text/javascript">
             function sleep (time) {
                 return new Promise((resolve) => setTimeout(resolve, time));
             }
            sleep(3000).then(() => {
                window.location.replace("/iTrust/auth/getPatientID.jsp?forward=hcp/viewObOfficeVisit.jsp&showEligibility=true");
            })
        </script>
        <%
         return;
    }
    

    initialRecord inirecord = inirecords.get(0);
    session.setAttribute("obrecord", inirecord);

    // check RH flag
    PatientBean p;
    EditPatientAction action = new EditPatientAction(prodDAO,
			loggedInMID.longValue(), pidString);
    p = action.getPatient();
    String bloodtype = p.getBloodType().toString();
    boolean rhflag = false;
    if (bloodtype.charAt(bloodtype.length() - 1) == '-') {
            rhflag = true;
    } else {
            rhflag = false;
    }
    //check number of pregrnancy 
    java.sql.Date lmp = inirecord.getLMP();
    if (lmp != null){
        java.sql.Date today = new java.sql.Date(System.currentTimeMillis());
        int weekspass = (int) (Math.floor((today.getTime() - lmp.getTime())/86400000) / 7);
        int days = (int) (Math.floor((today.getTime() - lmp.getTime())/86400000) % 7);
        String prenancypass = String.valueOf(weekspass) + "-" + String.valueOf(days);

        if (weekspass > 28 && rhflag){
            %>
            <p style="color:red;">Current weeks of Pregnancy :<%=weekspass%>. The patient should take a RH immunue globulin shot if they have not already</p>
            <%
        }
        session.setAttribute("weekspass", prenancypass);
        session.setAttribute("weekspass_num", weekspass);
    }
    
    //set eligibility string
    String eligibility_string = "false";
    if (eligibility != null && eligibility.equals("true")){
        eligibility_string = "true";
    }
    // create date formatter
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    // form is triggered
    obOfficeVisitController ovControll = new obOfficeVisitController(loggedInMID.longValue());
    
    String action_name = request.getParameter("action");
    if (action_name != null) {
       if (action_name.equals("Add Visit Record")){
           if (eligibility.equals("true")) {
                response.sendRedirect("addOBOfficeVisit.jsp?forward=hcp/viewObOfficeVisit.jsp&UID_PATIENTID=" + pidString);
                return;
            } else {
                %>
                    <p style="color:red;">Only patients with eligibility of "true" can have new childbirth visit</p>
                <%
            }
        }
        else if (action_name.equals("Regular Add Visit Record")){
            response.sendRedirect("iTrust/auth/getPatientID.jsp?forward=/iTrust/auth/hcp-uap/viewOfficeVisit.xhtml");
            return;
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

    // fetch records with given pid
    List<obOfficeVisit> records = new ArrayList<obOfficeVisit>();
    
    try {
        records = ovControll.getOvRecordsByDate(pidString);
        session.setAttribute("VisitRecords", records);
    } catch (Exception e) {
        %>
            <p style="color:red;">Unable to fetch records..</p>
        <%
    }
    //delete the record first
    if(delete_idx != null){
        delete_id = Integer.parseInt(delete_idx);
        obOfficeVisit del_record = records.get(delete_id);
        try{
            ovControll.delete(del_record);
        } catch (Exception e){

        }  
    }
    //refetch the record from db
    try {
        records = ovControll.getOvRecordsByDate(pidString);
        session.setAttribute("VisitRecords", records);
    } catch (Exception e) {
        %>
            <p style="color:red;">Unable to fetch records..</p>
        <%
    }

    int num_record = (records != null) ? records.size() : 0;

    /*Display record*/
%>
<table width=80%>
    <tr>
        <td><p><b>Patient Name: <%=first_name + " " + last_name%></b></p></td>
        <td><p><b>Patient ID: <%=pidString%></b></p></td>
        <td align="right"><p align="right"><b>Eligibility: <%=eligibility_string%></b></p></td>
    </tr>
</table>
<%
    if (num_record > 0) {
        if (speciality != null && speciality.equals("OB/GYN")) {
            %>
            <table class='fTable' width=80%><tr><th width=50%>Date</th><th width=%>View/Edit</th><th width=%>Delete</th></tr>
            <%
            for (int i = 0; i < num_record; i++) {
                obOfficeVisit record = records.get(i);
    %>
    <tr>
        <td><%=record.getCurrentDate().format(dateFormatter)%></td>
        <td><input type='button' style='width:100px;' onclick="parent.location.href='addOBOfficeVisit.jsp?RECORD_INDEX=<%=record.getVisiteId()%>';" value="view/edit"></td>
        <td><input type='button' style='width:100px;' onclick="parent.location.href='viewObOfficeVisit.jsp?DELETE_INDEX=<%=i%>';" value="delete"></td>
    </tr>
    <%
            }
%>
</table>
<br><br>
<form id="addNewRecord" action="viewObOfficeVisit.jsp" method="post">
    <div align="center">
        <input type="submit" name="action" style="font-size: 16pt; font-weight: bold;" value="Add Visit Record">
    </div>
</form>
<%
        }   
        else{
%>
<table class='fTable' width=80%><tr><th width=50%>Date</th><th width=20%>Action</th></tr>
    <%   
            for (int i = 0; i < num_record; i++) {
                obOfficeVisit record = records.get(i);
    %>
    <tr>
        <td><%=record.getCurrentDate().format(dateFormatter)%></td>
        <td><input type='button' style='width:100px;' onclick="parent.location.href='detailOBOfficeVisit.jsp?RECORD_INDEX=<%=record.getVisiteId()%>';" value="view"></td>
    </tr>
    <%
            }
    %>
</table>
<br><br>
<form id="addNewRecord" action="viewObOfficeVisit.jsp" method="post">
    <div align="center">
        <input type="submit" name="action" style="font-size: 16pt; font-weight: bold;" value="Regular Add Visit Record">
    </div>
</form>
    <div align="left">
        <br>
        <p><b>Only doctor with speciality OB/GYN can add new visit or edit existed visit. Click the bottom will direct you to regular office visit.</b></p>
    </div>
<%
        }
    } else {
        if (speciality != null && speciality.equals("OB/GYN")) {
    %>
        <p>No office visit record found</p>
        <br><br>
        <form id="addNewRecord" action="viewObOfficeVisit.jsp" method="post">
            <div align="center">
                <input type="submit" name="action" style="font-size: 16pt; font-weight: bold;" value="Add Visit Record">
            </div>
        </form>
    <%
        } else {
            %>
            <br><br>
            <form id="addNewRecord" action="/iTrust/auth/hcp-uap/viewOfficeVisit.xhtml" method="post">
            <div align="center">
                <input type="submit" name="action" style="font-size: 16pt; font-weight: bold;" value="Regular Add Visit Record">
            </div>
            </form>
            <div align="left">
                <br>
            <p><b>Only doctor with speciality OB/GYN can add new visit or edit existed visit. Click the bottom will direct you to regular office visit.</b></p>
            </div>    
            <%
        }
    }
    %>


    <%@include file="/footer.jsp"%>

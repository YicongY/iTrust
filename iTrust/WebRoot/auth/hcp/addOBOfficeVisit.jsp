<%@page import="java.util.zip.DataFormatException"%>
<%@page import="com.mysql.fabric.xmlrpc.base.Data"%>
<%@page import="java.util.Locale"%>
<%@page import="edu.ncsu.csc.itrust.model.obgyn.obOfficeVisit.obOfficeVisit"%>
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
<%@page import="java.time.format.DateTimeFormatter"%>
<%@page import="edu.ncsu.csc.itrust.exception.DBException"%>
<%@page import="edu.ncsu.csc.itrust.controller.obgyn.initialRecord.InitialRecordController"%>
<%@page import="edu.ncsu.csc.itrust.controller.obgyn.obOfficeVisit.obOfficeVisitController"%>
<%@page import="java.util.Collections"%>
<%@page import="edu.ncsu.csc.itrust.model.old.dao.mysql.ApptDAO"%>
<%@page import="edu.ncsu.csc.itrust.model.old.validate.ApptBeanValidator"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.ApptTypeBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.dao.DAOFactory"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.ApptBean"%>
<%@page import="edu.ncsu.csc.itrust.logger.TransactionLogger"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.action.EditPatientAction"%>
<%@page import="jdk.nashorn.internal.runtime.ParserException"%>
<%@page import="org.apache.commons.lang3.tuple.Pair"%>
<%@page import="edu.ncsu.csc.itrust.action.AddApptAction"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.Arrays"%>
<%@page import="edu.ncsu.csc.itrust.exception.DBException"%>
<%@page import="java.sql.Timestamp"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>
<%@page import="java.text.ParseException"%>
<%@page import="java.sql.SQLException"%>





<%@include file="/header.jsp"%>
<%!
  public boolean autoAddApp(int weeks, long hcpID, long pid, String apptType, String comment) throws DBException{
        boolean success = false;
        DAOFactory prodDAO = DAOFactory.getProductionInstance();
        AddApptAction apptAction = new AddApptAction(prodDAO,hcpID);

        ApptBean appt = new ApptBean();
        appt.setHcp(hcpID);
        appt.setPatient(pid);
        appt.setComment(comment);
        appt.setApptType(apptType);

        Calendar originalDate = Calendar.getInstance();

        List<Integer> hourList = new ArrayList<>(Arrays.asList(9,10,11,12,13,14,15,16));
        List<Integer> weekdayList = new ArrayList<>(Arrays.asList(2,3,4,5,6));
        List<Pair<Integer,Integer>> combList = new ArrayList<>();
        for (Integer weekday: weekdayList){
            for (Integer hour: hourList){
                combList.add(Pair.of(weekday,hour));

            }
        }
        Calendar nextdate = (Calendar) originalDate.clone();
        if (weeks >= 0 && weeks <= 13){
            //every month appointment
            nextdate.add(Calendar.MONTH,1);
            success= generateApp(nextdate,appt, combList,weekdayList,hourList, apptAction, hcpID);
            System.out.print(success);
        }
        else if (weeks >= 14 && weeks <= 28){
            //every two weeks appointment
            nextdate.add(Calendar.WEEK_OF_MONTH,2);
            success= generateApp(nextdate,appt, combList,weekdayList,hourList, apptAction, hcpID);
            System.out.print(success);

        }

        else if (weeks >= 29 && weeks <= 40){
            //every two weeks appointment
            nextdate.add(Calendar.WEEK_OF_MONTH,1);
            success= generateApp(nextdate,appt, combList,weekdayList,hourList, apptAction, hcpID);
            System.out.print(success);

        }

        else if (weeks >= 41){
            //every two weeks appointment
            nextdate.add(Calendar.DAY_OF_WEEK,2);
            success= generateApp(nextdate,appt, combList,weekdayList,hourList, apptAction, hcpID);
            System.out.print(success);

        }

        return success;
    }


    public boolean generateApp(Calendar nextdate,ApptBean appt, List<Pair<Integer,Integer>> combList, List<Integer> weekdayList, List<Integer> hourList, AddApptAction controller, long hcpID) throws DBException{
        ApptBeanValidator AppValidator = new ApptBeanValidator();
        int dayOfWeek = nextdate.get(Calendar.DAY_OF_WEEK);
        int hour = nextdate.get(Calendar.HOUR_OF_DAY);
        Calendar originalDate = Calendar.getInstance();

        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");



        //initialize the time and date if they are not in the working hours or date
        if (!weekdayList.contains(dayOfWeek)){
            dayOfWeek = 2;
        }
        if (!hourList.contains(hour)){
            hour = 9;
        }

        nextdate.set(Calendar.DAY_OF_WEEK, dayOfWeek);
        nextdate.set(Calendar.HOUR_OF_DAY, hour);
        nextdate.set(Calendar.MINUTE, 0);
        nextdate.set(Calendar.SECOND, 0);

        if (nextdate.getTime().before(originalDate.getTime())){
            nextdate.add(Calendar.WEEK_OF_MONTH, 1);
        }


        appt = getTimestamp(nextdate, appt);

        boolean success = false;
        String headerMessage;

        boolean ignoreConflicts = false;
        List combListCopy = new ArrayList(combList);

        Calendar cal = Calendar.getInstance();
        try{
            AppValidator.validate(appt);
        }
        catch (FormValidationException e1){
            DBException eDB = new DBException(new SQLException(e1.getMessage()));
            eDB.setErrorList(e1.getErrorList());
            throw eDB;
        }
        // while loop begin
        try{
            int listLength;

            List<ApptBean> apptList = null;
            apptList = controller.getConflictsForAppt(hcpID, appt);
            listLength = apptList.size();

            while (listLength > 0){
                //if the doctor has no available time in this week, start the date from nextweek
                if (combList.size() == 0){
                   java.sql.Timestamp curTime = appt.getDate();
                   cal.setTime(curTime);
                   cal.add(Calendar.WEEK_OF_MONTH, 1);
                   combList = new ArrayList(combListCopy);
                }

                for (ApptBean app : apptList) {
                    java.sql.Timestamp badTime = app.getDate();
                    cal.setTime(badTime);
                    combList.remove(Pair.of(cal.get(Calendar.DAY_OF_WEEK), cal.get(Calendar.HOUR_OF_DAY)));

                    cal.set(Calendar.HOUR_OF_DAY, combList.get(0).getValue());
                    cal.set(Calendar.DAY_OF_WEEK, combList.get(0).getKey());
                    appt = getTimestamp(cal, appt);
                    apptList = controller.getConflictsForAppt(hcpID, appt);
                    listLength = apptList.size();
                }

            }
        }
        catch (SQLException e) {
            success = false;
            throw new DBException(e);

        }

        //add the appt to schedule
        try {
            System.out.println("Add appt");
            headerMessage = controller.addAppt(appt, ignoreConflicts);
            if (headerMessage.startsWith("Success")) {
                System.out.println("Success");
                success = true;
            }
        }
        catch (FormValidationException e){
            System.out.println("Exception :" + e);
        }
        catch (SQLException e){
            throw new DBException(e);
        }
        return success;
    }


    public ApptBean getTimestamp(Calendar cal, ApptBean appt) throws ParserException {
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String newdateString = dateformat.format(cal.getTime());
        try {

            Date newdateDate = (Date) dateformat.parse(newdateString);
            java.sql.Timestamp timestamp = new Timestamp(newdateDate.getTime());
            appt.setDate(timestamp);

        } catch (ParseException e) {
            System.out.println("Exception :" + e);

        }
        return appt;
    }
%>

<title> OB Office Visit</title>


<%
        String pidString = (String) session.getAttribute("pid");
        String first_name = (String) session.getAttribute("first_name");
        String last_name = (String) session.getAttribute("last_name");
        String record_idx = (String) request.getParameter("RECORD_INDEX");
        InitialRecordController inic = new InitialRecordController(loggedInMID.longValue());
        if (pidString == null || pidString.equals("") || pidString.length() < 1) {
            out.println("pidstring is null");
            response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=hcp/viewObOfficeVisit.jsp");
            return;
        }
        //check is editing and creating new record
        String title = "Edit OB Office Visit Record";
        boolean is_create = false;
        if (record_idx == null || record_idx.equals("") || record_idx.length() < 1 ) {
            is_create = true;
            title = "Create OB Office Visit Record";
        }
        LocalDate today = LocalDate.now();  
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        //auto cal
        
        int weekspass_num = (int) session.getAttribute("weekspass_num");
        String app_date = "";

        app_date = today.format(dateFormatter);
        //default 
        String weeks = (String) session.getAttribute("weekspass");
        String record_date = "";
        float weight = 0;
        String bloodp = "0/0";
        int heart_rate = 0;
        int numBaby = 1;
        String placenta = "false";
        boolean is_placenta = false;
        //date/time formatters
        obOfficeVisitController ovControll = new obOfficeVisitController(loggedInMID.longValue());
        
        String number_fetus = "1";
        request.setAttribute("session_fetus", number_fetus);
        record_date = today.format(dateFormatter);
%>  
<h2><%=title%></h2>
</br>
<%
        String action = request.getParameter("action");

        List<String> errorList = Collections.emptyList();
        
        boolean is_valid = true;
        boolean valid_ultra = true;
        if (action != null && (action.equals("Add") || action.equals("UPDATE"))){
            obOfficeVisit newRecord = new obOfficeVisit();
            if (action.equals("UPDATE")){
                newRecord.setVisiteId((Long) session.getAttribute("VisiteId"));
            }
            newRecord.setPatientMID(Long.valueOf(pidString));
           
            //check record date
            try {
                record_date = request.getParameter("record_date");
                LocalDate localDate = LocalDate.parse(record_date,dateFormatter);
                newRecord.setCurrentDate(localDate);
            } catch (Exception e) {
                is_valid = false;
                %><p style="color:red;">Invalid record_date</p><%
                
            }
            //check number of weeks no errry yet
            weeks = request.getParameter("weeksOfPregnant");
            newRecord.setNumOfWeeks(weeks);
            //check weight in pound
            try {
    		    weight = Float.parseFloat(request.getParameter("weight"));
			    newRecord.setWeight(weight);
    	    }catch (Exception e) {
    		    is_valid = false;
    		    %><p style="color:red;">Invalid weight</p><%
    	    }
            //check blood pressure no error yet
            bloodp = request.getParameter("blood_pressure");
            newRecord.setBloodPressure(bloodp);
            //check heart rate
            try {
	    	    heart_rate = Integer.parseInt(request.getParameter("heart_rate"));
	    	    newRecord.setFetalHeartRate(heart_rate);
		    } catch (Exception e) {
		        is_valid = false;
			    %><p style="color:red;">Invalid heart rate</p><%
		    }
            //check number of pregnancy
            try {
	    	    numBaby = Integer.parseInt(request.getParameter("numBaby"));
	    	    newRecord.setNumOfMultiplePregnancy(numBaby);
                if (numBaby > 1){
                    newRecord.setMultiplePregnancy(true);
                }
                else{
                    newRecord.setMultiplePregnancy(false);
                }
		    } catch (Exception e) {
		        is_valid = false;
			    %><p style="color:red;">Invalid pregnancy type</p><%
		    }
            //check low placenta no error yet
	    	placenta = request.getParameter("placenta");
            if (placenta.equals("true")){
                is_placenta = true;
                }
            else{
                is_placenta = false;
                }
            newRecord.setLowLyingPlacentaObserved(is_placenta);

	    	if (placenta == "true"){
                newRecord.setLowLyingPlacentaObserved(true);
            }
            else{
                newRecord.setLowLyingPlacentaObserved(false);
            }
            //if is valid ,start to upload
            if (is_valid) {
                
                
                boolean succeed = true;
                //try create or update with the controller
                try{
                    if (is_create == true){
                        errorList = ovControll.add(newRecord);
                    }
                    else{
                        errorList = ovControll.update(newRecord);
                    }
                } catch (DBException e){
                    is_valid = false;
                    succeed = false;
                }
                if (errorList.size() != 0){
                    is_valid = false;
                    succeed = false;
                }
                //check whether has erro
                if (succeed) {
                    if (is_create == true) {
                        //auto add calendar
                        String auto_delivery_type = "$";
                        if (weekspass_num >= 42){
                            if (request.getParameter("delivery") != null ){
                                auto_delivery_type = auto_delivery_type + request.getParameter("delivery");
                            } 
                            auto_delivery_type = auto_delivery_type + "vaginal delivery";
                            autoAddApp(weekspass_num, loggedInMID.longValue(), Long.valueOf(pidString), "Obstetrics_ChildBirth", auto_delivery_type);
                        }else{
                            autoAddApp(weekspass_num, loggedInMID.longValue(), Long.valueOf(pidString), "Obstetrics", auto_delivery_type);
                        }


                        %>
                <p id ="test_id" style="color:green;">OB Office Visit Record Successfully Add, Next Office Visit Appointment is generated. Please Visit Your Appointment Page.</p>
<script type="text/javascript">
    function sleep (time) {
        return new Promise((resolve) => setTimeout(resolve, time));
    }
    // sleep for 3 seconds
    sleep(1000).then(() => {
        // open new windows for baby patients creation
        console.log("hello");
        var my_url = "http://localhost:8080/iTrust/auth/hcp-uap/ultraSoundPop.jsp"
        var new_window = window.open(my_url,'_blank');
        if (window.focus) {
            new_window.focus();
        }
       
    })
</script>
                <%
                    } else{
                %>
                <p style="color:green;">OB Office Visit Record Successfully Updated, redirect in 1 seconds...</p>
                    
                <%
                    response.sendRedirect("/iTrust/auth/hcp/detailOBOfficeVisit.jsp?RECORD_INDEX=" + newRecord.getVisiteId());
                    return;
                    }

                 }
                 else{
                     if (errorList.size() > 0){
    	    		    for (int i = 0; i < errorList.size(); i++) {
    	            	    String error = errorList.get(i);
    	            	    %><p style="color:red;"><%=error%></p><%
    	    		    }
                     }
                    else{
                        %>
        <p style="color:red;">Not succeed,but no error </p>
        <%
                    }  	
                }
            }
        } else if (is_valid == false || action == null){
            if(is_create != true) {
                
                //List<obOfficeVisit> records = (List<obOfficeVisit>) session.getAttribute("VisitRecords");
                //obOfficeVisit record = records.get(Integer.parseInt(record_idx));
                obOfficeVisit record = ovControll.getOBOfficeVisitByVisitID(Long.valueOf(record_idx));
                session.setAttribute("VisiteId", record.getVisiteId());
                TransactionLogger.getInstance().logTransaction(TransactionType.VIEW_OBSTETRICS_OV, loggedInMID, record.getPatientMID(), String.valueOf(record.getVisiteId()));
                weeks = record.getNumOfWeeks();
                record_date = record.getCurrentDate().format(dateFormatter);
                weight = record.getWeight();
                bloodp = record.getBloodPressure();
                heart_rate = record.getFetalHeartRate();
                numBaby = record.getNumOfMultiplePregnancy();
                is_placenta = record.isLowLyingPlacentaObserved();
                if (is_placenta){
                    placenta = "true";
                }
                else{
                    placenta = "false";
                }

            }

        }
        //set number_fetus
        if (action != null && action.equals("BACK")){
        response.sendRedirect("/iTrust/auth/hcp/viewObOfficeVisit.jsp");
        return;
    }   
        //set number_fetus
        if (action != null && action.equals("Add ChildBirth Visit Appointment")){
            session.setAttribute("pid", pidString);
            response.sendRedirect("/iTrust/auth/hcp/scheduleAppt.jsp?TYPES=" + request.getParameter("delivery"));    
            return;
    }
        
        
      
%>
</br>

</br>
<table width=80%>
    <tr>
        <td><b>Patient Name: <%=first_name + " " + last_name%></b></td>
        <td><b>Patient ID: <%=pidString%></b></td>
    </tr>
</table>

<form id="createNewRecord" action="addOBOfficeVisit.jsp<%=is_create ? "" : ("?RECORD_INDEX=" + record_idx)%>" method="post">
    <table class='fTable' width=80% BORDERCOLOR=white>
        <tr>
            <th width=50%>Visit information</th>
            <th width=50%></th>
        </tr>
         <tr>
            <td>Date:</td>
            <td>
                <div>
                    <input type="text" name="record_date" value="<%=record_date%>">
                    <input type="button" value="Select Date" onclick="displayDatePicker('record_date');">
                </div>
            </td>
            
        </tr>
        <tr>
            <td>Number of Weeks Pregnant Genereated:</td>
            <td><input name="weeksOfPregnant" id="numofweeks" value="<%=weeks%>" type="text"></td>
        </tr>
        <tr>
            <td>Weight:</td>
            <td><input name="weight" id="weight" value="<%=weight%>" type="text"></td>
        </tr>
        <tr>
            <td>Blood Pressure:</td>
            <td><input name="blood_pressure" id="bloodp" value="<%=bloodp%>" type="text"></td>
        </tr>
        <tr>
            <td>Fetal Heart Rate:</td>
            <td><input name="heart_rate" id="fhr" value="<%=heart_rate%>" type="text"></td>
        </tr>
        <tr>
            <td>Number of Baby:</td>
            <td><input name="numBaby" id="numB" value="<%=numBaby%>" type="text"></td>
        </tr>
        <tr>
            <td>Low Lying Placenta Observed:</td>
            <td><select name="placenta">
        <%
        //String type = (String) session.getAttribute("delivery");
        String[] placenta_string = {"false","true"};
        for (String t: placenta_string) {
        %><option value="<%=t%>"> <%=t.equals("") ? "select one" : t %> </option><%
        }
        %>
        </select></td>
        </tr>
    </table>
    
    </br>
    <div class="row">
        <div class="col-md-6">
            <input type="submit" name="action" style="font-size: 16pt; font-weight: bold;" value="<%=is_create ? "Add" : "UPDATE"%>">
        </div>
        <div class="col-md-6">
        <input type="submit" name="action" style="font-size: 16pt; font-weight: bold;" value="BACK">
        </div>
    </div>

</form>
<% if (weekspass_num >= 37) {
    %>
    </br>
    <p style="color:green;">Your Weeks of Pregnancy Is More Than 37 Weeks, You Can Schedule Your ChildBirth Visit Through The Link</p>
    <form id="createcal" action="addOBOfficeVisit.jsp<%=is_create ? "" : ("?RECORD_INDEX=" + record_idx)%>" method="post">
    <tr>
        <td>Delivery type:</td>
        <td><select name="delivery">
        <%
        //String type = (String) session.getAttribute("delivery");
        String[] types = {"vaginal delivery", "vaginal delivery vacuum assist", "vaginal delivery forceps assist", "caesarean section", "miscarriage"};
        for (String t: types) {
        %><option value="<%=t%>"> <%=t.equals("") ? "select one" : t %> </option><%
        }
        %>
        </select></td>
    </tr>
    </br></br>
    <div align="left">
        <input type="submit" name="action" style="font-size: 14pt; font-weight: bold;" value="Add ChildBirth Visit Appointment">
    </div>
    </form>
    <%

}
%>

    
<%@include file="/footer.jsp"%>

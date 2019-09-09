<%@taglib prefix="itrust" uri="/WEB-INF/tags.tld"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="edu.ncsu.csc.itrust.model.old.dao.DAOFactory"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.action.EditPatientAction"%>
<%@page import="edu.ncsu.csc.itrust.BeanBuilder"%>
<%@page import="edu.ncsu.csc.itrust.model.old.enums.Ethnicity"%>
<%@page import="edu.ncsu.csc.itrust.model.old.enums.BloodType"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>
<%@page import="edu.ncsu.csc.itrust.exception.DBException"%>
<%@page import="edu.ncsu.csc.itrust.model.old.enums.Gender"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.PersonnelBean"%>
<%@page import="edu.ncsu.csc.itrust.action.EditPersonnelAction"%>
<%@page import="edu.ncsu.csc.itrust.BeanBuilder"%>
<%@page import="edu.ncsu.csc.itrust.model.old.enums.TransactionType"%>
<%@page import="edu.ncsu.csc.itrust.model.old.enums.Role"%>
<%@page import="edu.ncsu.csc.itrust.controller.obgyn.initialRecord.InitialRecordController"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.lang.Long"%>
<%@page import="java.lang.Boolean"%>
<%@page import="java.util.ArrayList"%>
<%@page import="edu.ncsu.csc.itrust.model.obgyn.initialRecord.initialRecord"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Collections"%>
<%@page import="edu.ncsu.csc.itrust.model.obgyn.childBirthVisit.ChildBirthVisit"%>
<%@page import="edu.ncsu.csc.itrust.controller.obgyn.childBirthVisit.ChildBirthVisitController"%>

<%@include file="/global.jsp"%>

<%
    pageTitle = "iTrust - View Childbirth Visit Record";
%>

<%@include file="/header.jsp"%>

<%
    String pidString = (String) session.getAttribute("pid");
    String first_name = (String) session.getAttribute("first_name");
    String last_name = (String) session.getAttribute("last_name");
    String record_idx = (String) request.getParameter("RECORD_INDEX");

    if (pidString == null || pidString.equals("") || pidString.length() < 1) {
        out.println("pidstring is null");
        response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=hcp-uap/viewChildbirthVisitRecord.jsp");
        return;
    }
    String title = "Edit Childbirth Hospital Visit Record";
    boolean is_create = false;
    if (record_idx == null || record_idx.equals("") || record_idx.length() < 1 ) {
        is_create = true;
        title = "Create Childbirth Hospital Visit Record";
    }
    // data source
    String[] visit_types = {"", "appointment", "emergency"};
    String[] delivery_types = {"", "vaginal delivery", "vaginal delivery vacuum assist", "vaginal delivery forceps assist", "caesarean section", "miscarriage"};
    String[] drug_types = {"Pitocin", "Nitrous oxide", "Pethidine", "Epidural anaesthesia", "Magnesium sulfate", "RH immune globulin"};
    // default values
    String visit_type = "";
    String delivery_type_preferred = "";
    String [] drug_dosages = new String[6];
    String delivery_date = "";
    String delivery_time = "";
    String delivery_type_actual = "";
    String num_boys = "0";
    String num_girls = "0";
    String comment = "";
    // date/time formatters
    SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy");
    SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm");
%>
<h2><%=title%></h2>
</br>
<%
    String action = request.getParameter("action");
    String[] type_errors = new String[13];
    java.util.Arrays.fill(type_errors, "");
    if (action != null && (action.equals("Add Record") || action.equals("Save"))) {
        ChildBirthVisit newVisit = new ChildBirthVisit();
        if (action.equals("Save")) {
            newVisit.setChildBirthVisitID((Long) session.getAttribute("childBirthVisitID"));
        }
        newVisit.setPatientMID(Long.valueOf(pidString));
        boolean is_valid = true;
        // check visit type
        if (java.util.Arrays.stream(visit_types).anyMatch(request.getParameter("visit_type")::equals)) {
            visit_type = request.getParameter("visit_type");
            newVisit.setVisitType(visit_type);
        } else {
            is_valid = false;
            type_errors[0] = "Invalid visit type";
        }
        // check preferred delivery type
        if (java.util.Arrays.stream(delivery_types).anyMatch(request.getParameter("delivery_preferred")::equals)) {
            delivery_type_preferred = request.getParameter("delivery_preferred");
            newVisit.setPreferredDeliveryType(delivery_type_preferred);
        } else {
            is_valid = false;
            type_errors[1] = "Invalid delivery type";
        }
        // check drugs
        for (int i = 0; i < drug_types.length; ++i) {
            try {
                drug_dosages[i] = request.getParameter(drug_types[i]);
                if (Integer.parseInt(drug_dosages[i]) < 0) {
                    type_errors[i + 2] = "Invalid dosage";
                    is_valid = false;
                }
            } catch (Exception e) {
                type_errors[i + 2] = "Invalid dosage";
                is_valid = false;
            }
        }
        // check delivery date
        try {
            delivery_date = request.getParameter("delivery_date");
            newVisit.setDeliveryDate(new java.sql.Date(dateFormatter.parse(delivery_date).getTime()));
        } catch (Exception e) {
            type_errors[8] = "Invalid delivery date";
            is_valid = false;
        }
        // check delivery time
        try {
            delivery_time = request.getParameter("delivery_time");
            newVisit.setDeliveryTime(new java.sql.Time(timeFormatter.parse(delivery_time).getTime()));
        } catch (Exception e) {
            type_errors[9] = "Invalid delivery time";
            is_valid = false;
        };
        // check actual delivery type
        if (java.util.Arrays.stream(delivery_types).anyMatch(request.getParameter("delivery_actual")::equals)) {
            delivery_type_actual = request.getParameter("delivery_actual");
            newVisit.setDeliveryType(delivery_type_actual);
        } else {
            type_errors[10] = "Invalid delivery type";
            is_valid = false;
        }
        // check number of boys
        try{
            num_boys = request.getParameter("boys");
            newVisit.setNumberOfBoyBabies(Integer.parseInt(num_boys));
        } catch (Exception e) {
            type_errors[11] = "Invalid number of babies";
            is_valid = false;
        }
        // check number of girls
        try {
            num_girls = request.getParameter("girls");
            newVisit.setNumberOfGirlBabies(Integer.parseInt(num_girls));
        } catch (Exception e) {
            type_errors[12] = "Invalid number of babies";
            is_valid = false;
        }
        // check comment
        comment = request.getParameter("comment");
        // if information is valid, upload...
        if (is_valid) {
            System.out.println("Is Valid");
            ChildBirthVisitController cbvc = new ChildBirthVisitController(loggedInMID.longValue());
            // set drug dosages
            newVisit.setDosage_pitocin(Integer.parseInt(drug_dosages[0]));
            newVisit.setDosage_nitrousOxide(Integer.parseInt(drug_dosages[1]));
            newVisit.setDosage_pethidine(Integer.parseInt(drug_dosages[2]));
            newVisit.setDosage_epiduralAnaesthesia(Integer.parseInt(drug_dosages[3]));
            newVisit.setDosage_magnesiumSulfate(Integer.parseInt(drug_dosages[4]));
            newVisit.setDosage_rhImmuneGlobulin(Integer.parseInt(drug_dosages[5]));
            newVisit.setComment(comment);
            // start upload
            boolean succeed = true;
            try {
                if (is_create) {
                    System.out.println("addChildBirthVisit");
                    cbvc.addChildBirthVisit(newVisit);
                } else {
                    System.out.println("updateChildBirthVisit");
                    cbvc.updateChildBirthVisit(newVisit);
                }
            } catch (DBException e) {
                System.out.println("DB Exception");
                succeed = false;
            } catch (FormValidationException e) {
                System.out.println("FormValidationException");
                succeed = false;
                List<String> errorList = e.getErrorList();
                for (int i = 0; i < errorList.size(); i++) {
                    type_errors[i] = errorList.get(i);
                }
            }
            if (succeed) {
                if (is_create == true) {
            %>
<p style="color:green;">Childbirth Visit Record Successfully Added, redirect in 3 seconds...</p>
<script type="text/javascript">
    function sleep (time) {
        return new Promise((resolve) => setTimeout(resolve, time));
    }

    // sleep for 3 seconds
    sleep(3000).then(() => {
        // open new windows for baby patients creation
        console.log("hello");
        var num_boys = parseInt(document.getElementById("boys").value);
        console.log(num_boys);
        var num_girls = parseInt(document.getElementById("girls").value);
        var i = 0;
        for (i = 0; i < num_boys + num_girls; i++) {
            var new_window = window.open("http://localhost:8080/iTrust/auth/hcp-uap/addPatient.jsp?patientMID=" + <%=Long.valueOf(pidString)%>,'_blank');
            if (window.focus) {
                new_window.focus();
            }
        }
        // redirect to ob record page
        window.location.replace("viewChildbirthVisitRecord.jsp");
    })
</script>
<%
                } else {
%>
<p style="color:green;">Childbirth Visit Record Successfully Updated, redirect in 3 seconds...</p>
<script type="text/javascript">
    function sleep (time) {
        return new Promise((resolve) => setTimeout(resolve, time));
    }

    // sleep for 3 seconds
    sleep(3000).then(() => {
        // redirect to ob record page
        window.location.replace("viewChildbirthVisitRecord.jsp");
    })
</script>
<%
                }
            } else {
%>
<p style="color:red;">Error occured..</p>
<%
            }
        }
    } else {
        if (is_create == true) {
            ChildBirthVisitController cbvc = new ChildBirthVisitController(loggedInMID.longValue());
            try {
                String temp = cbvc.checkExistingAppointment(pidString);
                if (temp != null) {
                    visit_type = "appointment";
                    delivery_type_preferred = temp;
                } else {
                    visit_type = "emergency";
                    delivery_type_preferred = "";
                }
            } catch (Exception e) {
%>
<p style="color:red;">Unable to retrive appointment data...</p>
<%
                visit_type = "";
                delivery_type_preferred = "";
            }
            java.util.Arrays.fill(drug_dosages, "0");
            delivery_date = dateFormatter.format(new java.util.Date());
            delivery_time = timeFormatter.format(new java.util.Date());
            delivery_type_actual = "";
            num_boys = "0";
            num_girls = "0";
            comment = "";
        } else {
            List<ChildBirthVisit> records = (List<ChildBirthVisit>) session.getAttribute("childbirthVisitRecords");
            ChildBirthVisit record = records.get(Integer.parseInt(record_idx));
            session.setAttribute("childBirthVisitID", record.getChildBirthVisitID());
            visit_type = record.getVisitType();
            delivery_type_preferred = record.getPreferredDeliveryType();
            drug_dosages[0] = Integer.toString(record.getDosage_pitocin());
            drug_dosages[1] = Integer.toString(record.getDosage_nitrousOxide());
            drug_dosages[2] = Integer.toString(record.getDosage_pethidine());
            drug_dosages[3] = Integer.toString(record.getDosage_epiduralAnaesthesia());
            drug_dosages[4] = Integer.toString(record.getDosage_magnesiumSulfate());
            drug_dosages[5] = Integer.toString(record.getDosage_rhImmuneGlobulin());
            delivery_date = dateFormatter.format(record.getDeliveryDate());
            delivery_time = timeFormatter.format(record.getDeliveryTime());
            delivery_type_actual = record.getDeliveryType();
            num_boys = Integer.toString(record.getNumberOfBoyBabies());
            num_girls = Integer.toString(record.getNumberOfGirlBabies());
            comment = record.getComment();
        }
    }
%>
<table width=52%>
    <tr>
        <td><b>Patient Name: <%=first_name + " " + last_name%></b></td>
        <td align="right"><b>Patient ID: <%=pidString%></b></td>
    </tr>
</table>

<form id="createNewRecord" action="editChildbirthVisitRecord.jsp<%=is_create ? "" : ("?RECORD_INDEX=" + record_idx)%>" method="post">
    <table class='fTable' width=80% BORDERCOLOR=white>
        <tr>
            <th width=30%>Basic Information</th>
            <th width=35%></th>
            <th width=35% style="background-color: white"></th>
        </tr>
        <tr>
            <td>Visit type:</td>
            <td><select name="visit_type">
                <%
                    for (String t: visit_types) {
                %>
                <option value="<%=t%>" <%=t.equals(visit_type) ? "selected" : ""%>><%=t.equals("") ? "select one" : t%></option>
                <%
                    }
                %>
            </select></td>
            <td style="background-color: white"><p style="color:red;"><%=type_errors[0]%></p></td>
        </tr>
        <tr>
            <td>Preferred delivery type:</td>
            <td><select name="delivery_preferred">
                <%
                    for (String t: delivery_types) {
                %>
                <option value="<%=t%>" <%=t.equals(delivery_type_preferred) ? "selected" : ""%>><%=t.equals("") ? "select one" : t%></option>
                <%
                    }
                %>
            </select></td>
            <td style="background-color: white"><p style="color:red;"><%=type_errors[1]%></p></td>
        </tr>
    </table>
    </br></br>
    <table class='fTable' width=80% BORDERCOLOR=white>
        <tr>
            <th width=30%>Drug Information</th>
            <th width=35%></th>
            <th width=35% style="background-color: white"></th>
        </tr>
        <%
        for (int i = 0; i < drug_types.length; ++i) {
        %>
        <tr>
            <td><%=drug_types[i]%>:</td>
            <td><input name="<%=drug_types[i]%>" value=<%=drug_dosages[i]%> type="text">&nbsp;&nbsp;mg</td>
            <td style="background-color: white"><p style="color:red;"><%=type_errors[i + 2]%></p></td>
        </tr>
        <%
        }
        %>
    </table>
    <br/><br/>
    <table class='fTable' width=80% BORDERCOLOR=white>
        <tr>
            <th width=25%>Additional Information</th>
            <th width=40%></th>
            <th width=35% style="background-color: white"></th>
        </tr>
        <tr>
            <td>Delivery Date:</td>
            <td>
                <div>
                    <input type="text" name="delivery_date" value="<%=delivery_date%>">
                    <input type="button" value="Select Date" onclick="displayDatePicker('delivery_date');">
                </div>
            </td>
            <td style="background-color: white"><p style="color:red;"><%=type_errors[8]%></p></td>
        </tr>
        <tr>
            <td>Delivery time:</td>
            <td><input type="time" id="delivery_time" name="delivery_time" value="<%=delivery_time%>"></td>
            <td style="background-color: white"><p style="color:red;"><%=type_errors[9]%></p></td>
        </tr>
        <tr>
            <td>Actual delivery type:</td>
            <td><select name="delivery_actual">
                <%
                    for (String t: delivery_types) {
                %><option value="<%=t%>" <%=t.equals(delivery_type_actual) ? "selected" : ""%>><%=t.equals("") ? "select one" : t%></option><%
                }
            %>
            </select></td>
            <td style="background-color: white"><p style="color:red;"><%=type_errors[10]%></p></td>
        </tr>
        <tr>
            <td>Number of boys:</td>
            <td><input name="boys" id="boys" value="<%=num_boys%>" type="text"></td>
            <td style="background-color: white"><p style="color:red;"><%=type_errors[11]%></p></td>
        </tr>
        <tr>
            <td>Number of girls:</td>
            <td><input name="girls" id="girls" value="<%=num_girls%>" type="text"></td>
            <td style="background-color: white"><p style="color:red;"><%=type_errors[12]%></p></td>
        </tr>
    </table>
    </br></br>
    <p>Comment:</p>
    <textarea name="comment" cols="100" rows="10"><%=comment%></textarea>
    </br></br>
    <div align="center">
        <input type="submit" name="action" style="font-size: 16pt; font-weight: bold;" value="<%=is_create ? "Add Record" : "Save"%>">
    </div>
</form>

<%@include file="/footer.jsp"%>
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
<%@page import="edu.ncsu.csc.itrust.controller.obgyn.obOfficeVisit.ultraSoundController"%>
<%@page import="edu.ncsu.csc.itrust.model.obgyn.ultrasoundRecord.ultrasoundRecord"%>
<%@page import="edu.ncsu.csc.itrust.controller.obgyn.obOfficeVisit.obOfficeVisitController"%>
<%@page import="java.util.Collections"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.action.EditPatientAction"%>

<%@include file="/header.jsp"%>


<%

    String action = request.getParameter("action");
    String number_fetus = "1";
    boolean valid_ultra = true;

  if (action != null && action.equals("CREATE")){
            
            //set futus number 
            int real_num_fetus = 1;
            number_fetus = request.getParameter("number_fetus");
            try{
            real_num_fetus = Integer.parseInt(number_fetus);
            
            }catch (Exception e){
                valid_ultra = false;
                %><p style="color:red;">Invalid Number of Fetus. Range: 1-10</p><%
            }
        
            if(real_num_fetus <= 0 || real_num_fetus >=10 ){
                valid_ultra = false;
                %><p style="color:red;">Invalid Number of Fetus. Range: 1-10</p><%
            }

            if (valid_ultra){
                %>
                <script type="text/javascript">
    function sleep (time) {
        return new Promise((resolve) => setTimeout(resolve, time));
    }
    // sleep for 3 seconds
    sleep(1000).then(() => {
        console.log("hello");
        var js_num = parseInt(document.getElementById("number_fetus").value);
        console.log(js_num);
        var i = 0;
        for (i = 0; i < js_num; i++) {
            var new_window = window.open("http://localhost:8080/iTrust/auth/hcp/addUltraSound.jsp",'_blank');
            if (window.focus) {
                new_window.focus();
            }
        }
        // close itself
        window.close();
    })
</script>
                <%
                
                
            }
            
        }
    else if (action != null && action.equals("CLOSE")){
        %>
        <script type="text/javascript">
        window.close();
        </script>
        <%
    }
%>

<title> UltraSound Record POP</title>

<form id="createNewRecord" action="ultraSoundPop.jsp" method="post">

<table class='fTable' width=80% BORDERCOLOR=white>
        <tr>
            <th width=50%>Ultrasound Record Request</th>
            <th width=50%></th>
        </tr>
        <tr>
            <td>Number of Fetus:</td>
            <td>
                <div>
                    <input type="text" id ="number_fetus" name="number_fetus" value="<%=number_fetus%>">
                </div>
            </td>
            
        </tr>
    </table>

    </br></br>
    <div class="row">
        <div class="col-md-6">
            <input type="submit" name="action" style="font-size: 16pt; font-weight: bold;" value="CREATE">
        </div>
        <div class="col-md-6">
        <input type="submit" name="action" style="font-size: 16pt; font-weight: bold;" value="CLOSE">
        </div>
    </div>

</form>
<%@include file="/footer.jsp"%>
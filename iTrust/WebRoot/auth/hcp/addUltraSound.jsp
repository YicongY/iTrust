<%@page import="java.util.zip.DataFormatException"%>
<%@page import="com.mysql.fabric.xmlrpc.base.Data"%>
<%@page import="java.util.Locale"%>
<%@page import="edu.ncsu.csc.itrust.model.obgyn.obOfficeVisit.obOfficeVisit"%>
<%@include file="/global.jsp"%>
<%@page import="java.util.List"%>
<%@page import="edu.ncsu.csc.itrust.action.SearchUsersAction"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.PatientBean"%>
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
<%@page import="java.awt.image.BufferedImage"%>
<%@page import="java.io.File"%>
<%@page import="javax.servlet.http.Part"%>
<%@page import="java.io.InputStream"%>
<%@page import="java.util.Iterator"%>
<%@page import="org.apache.commons.fileupload.servlet.ServletFileUpload"%>
<%@page import="org.apache.commons.fileupload.DefaultFileItemFactory"%>
<%@page import="org.apache.commons.fileupload.FileItemFactory"%>
<%@page import="org.apache.commons.fileupload.FileItem"%>


<%@include file="/header.jsp"%>




<title> UltraSound Record</title>

<%

    String pidString = (String) session.getAttribute("pid");
    String first_name = (String) session.getAttribute("first_name");
    String last_name = (String) session.getAttribute("last_name");

    //default value
    String c_l = "0.0";
    String b_d = "0.0";
    String h_c = "0.0";
    String f_l = "0.0";
    String o_d = "0.0";
    String a_c = "0.0";
    String h_l = "0.0";
    String e_w = "0.0";
    //date/time formatters
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy"); 

    String action = (String) request.getParameter("action");
    boolean is_valid = true;
  

    ultrasoundRecord record = new ultrasoundRecord();
    if (session.getAttribute("record") != null){
        record = (ultrasoundRecord) session.getAttribute("record");
    }
    record.setLocationID("1");
    if (action != null && action.equals("ADD")){
        
        record.setPatientMID(Long.valueOf(pidString));
        //check  c_l
        try {
            Float c_l_f = Float.parseFloat(request.getParameter("c_l"));
            record.setCrownRumpLength(c_l_f);
        }catch (Exception e) {
            is_valid = false;
            %><p style="color:red;">Invalid Crown Rump Length</p><%
        }
        //check  b_d
        try {
            Float b_d_f = Float.parseFloat(request.getParameter("b_d"));
            record.setBiparietalDiameter(b_d_f);
        }catch (Exception e) {
            is_valid = false;
            %><p style="color:red;">Invalid Biparieta Diameter</p><%
        }

        //check  h_c
        try {
           Float h_c_f = Float.parseFloat(request.getParameter("h_c"));
            record.setHeadCircumference(h_c_f);
        }catch (Exception e) {
            is_valid = false;
            %><p style="color:red;">Invalid Head Circumference</p><%
        }
        //check  f_l
        try {
            Float f_l_f = Float.parseFloat(request.getParameter("f_l"));
            record.setFemurLength(f_l_f);
        }catch (Exception e) {
            is_valid = false;
            %><p style="color:red;">Invalid Femur Length</p><%
        }
        //check  o_d
        try {
           Float o_d_f = Float.parseFloat(request.getParameter("o_d"));
            record.setOccipitofrontalDiameter(o_d_f);
        }catch (Exception e) {
            is_valid = false;
            %><p style="color:red;">Invalid Occipitofrontal Diameter</p><%
        }
        //check  a_c
        try {
           Float a_c_f = Float.parseFloat(request.getParameter("a_c"));
            record.setAbdominalCircumference(a_c_f);
        }catch (Exception e) {
            is_valid = false;
            %><p style="color:red;">Invalid Abdominal Circumference</p><%
        }
        //check  h_l
        try {
           Float h_l_f = Float.parseFloat(request.getParameter("h_l"));
            record.setHumerusLength(h_l_f);
        }catch (Exception e) {
            is_valid = false;
            %><p style="color:red;">Invalid Humerus Length</p><%
        }
        //check  e_w
        try {
           Float e_w_f = Float.parseFloat(request.getParameter("e_w"));
            record.setEstimatedFetalWeight(e_w_f);
        }catch (Exception e) {
            is_valid = false;
            %><p style="color:red;">Invalid EstimatedFetal Weight</p><%
        }
        List<String> errorList = Collections.emptyList();

            if (is_valid) {
            
                ultraSoundController ulControl = new ultraSoundController(loggedInMID.longValue());
                boolean succeed = true;
                //UPLOAD PHOTO
            
                
                
                try{
                    errorList = ulControl.add(record);
                    
                } catch (DBException e){
                    is_valid =false;
                    succeed = false;
                }
                if (errorList.size() != 0){
                    is_valid = false;
                    succeed = false;
                }

                if (succeed) {
                    %>
                    <p style="color:green;">UltraSound Record Successfully Add, close in 1 seconds...</p>
                <script type="text/javascript">
                function sleep (time) {
                    return new Promise((resolve) => setTimeout(resolve, time));
                }
                // sleep for 3 seconds
                sleep(1000).then(() => {

                        window.close();
                })
                </script>
                <%
                }else{
                    %>
                        <p style="color:red;">Errors Happened..</p>
                        <%
                }
         }
        
    }
    boolean isMultipart = ServletFileUpload.isMultipartContent(request);
    if(isMultipart){
        Boolean fatal = false;
        InputStream fileStream = null;
        FileItemFactory factory = new DefaultFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        List<FileItem> items = upload.parseRequest(request);
        Iterator iter = items.iterator();
        %>
            <p style="color:green;">Saving file...</p>
        <%
        
        while (iter.hasNext()) {
            FileItem item = (FileItem) iter.next();
            fileStream = items.get(0).getInputStream();
        }

        if(fileStream!=null){
            record.setImageStream(fileStream);
            session.setAttribute("record",record);
            %>
            <p style="color:green;">File Set Successfully.Please continue to fill the form below</p>
            <%
        }

        if(fileStream==null){
            %>
            <p style="color:red;">File was not found.</p>
            <%
        }

        if (fatal){
            %>
            <p style="color:red;">File upload was unsuccessful</p>
            <%
        }

    }
    
%>

<form method="post" action="addUltraSound.jsp" enctype="multipart/form-data">
            <table border="0">
                
                <tr>
                    <td>UltraSound Photo: </td>
                    <td><input type="file" name="photo" size="50"/></td>
                </tr>
                <tr>
                    <td colspan="2">
                        <input type="submit" value="SAVE" id="SAVE" name="SAVE">
                    </td>
                </tr>
            </table>
</form>


<table width=80%>
    <tr>
        <td><b>Patient Name: <%=first_name + " " + last_name%></b></td>
        <td><b>Patient ID: <%=pidString%></b></td>
    </tr>
</table>

<form id="createultra_record" action="addUltraSound.jsp" method="post">
    <table class='fTable' width=80% BORDERCOLOR=white>
        <tr>
            <th width=50%>Record information</th>
            <th width=50%></th>
        </tr>
         <tr>
            <td>Crown Rump Length:</td>
            <td>
                <div>
                    <input type="text" name="c_l" id="c_l" value="<%=c_l%>">
                </div>
            </td>
            
        </tr>
        <tr>
            <td>Biparietal Diameter:</td>
            <td><input name="b_d" id="b_d" value="<%=b_d%>" type="text"></td>
        </tr>
        <tr>
            <td>Head Circumference:</td>
            <td><input name="h_c" id="h_c" value="<%=h_c%>" type="text"></td>
        </tr>
        <tr>
            <td>Femur Length:</td>
            <td><input name="f_l" id="f_l" value="<%=f_l%>" type="text"></td>
        </tr>
        <tr>
            <td>Occipitofrontal Diameter:</td>
            <td><input name="o_d" id="o_d" value="<%=o_d%>" type="text"></td>
        </tr>
        <tr>
            <td>Abdominal Circumference:</td>
            <td><input name="a_c" id="a_c" value="<%=a_c%>" type="text"></td>
        </tr>
        <tr>
            <td>Humerus length:</td>
            <td><input name="h_l" id="h_l" value="<%=h_l%>" type="text"></td>
        </tr>
        <tr>
            <td>Estimated Fetal Weight:</td>
            <td><input name="e_w" id="e_w" value="<%=e_w%>" type="text"></td>
        </tr>
    </table>
    

    </br></br>
    <div align="center">
        <input type="submit" id="submit" name="action" style="font-size: 16pt; font-weight: bold;" value="ADD">
    </div>
    
</form>
<%@include file="/footer.jsp"%>
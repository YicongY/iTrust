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
<%@page import="java.time.LocalDate"%>
<%@page import="java.time.ZoneId"%>
<%@page import="java.sql.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
<%@page import="edu.ncsu.csc.itrust.controller.obgyn.initialRecord.InitialRecordController"%>
<%@page import="java.lang.Long"%>
<%@page import="java.lang.Boolean"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.lang.Integer"%>
<%@page import="edu.ncsu.csc.itrust.model.obgyn.initialRecord.initialRecord"%>
<%@page import="java.util.List"%>
<%@page import="edu.ncsu.csc.itrust.model.obgyn.childBirthVisit.ChildBirthVisit"%>

<%@include file="/global.jsp"%>

<%
    pageTitle = "iTrust - Childbirth Visit Record Detail";
%>
<%@include file="/header.jsp"%>
<%
    // retrieve OB record data
    String pidString = (String) session.getAttribute("pid");
    String first_name = (String) session.getAttribute("first_name");
    String last_name = (String) session.getAttribute("last_name");
    int record_idx = -1;
    try {
        record_idx = Integer.valueOf(request.getParameter("RECORD_INDEX"));
    } catch (Exception e) {
        response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=hcp-uap/viewChildbirthVisitRecord.jsp");
    }
    List<ChildBirthVisit> records = (List<ChildBirthVisit>) session.getAttribute("childbirthVisitRecords");
    ChildBirthVisit record = records.get(record_idx);
    // create date format
    SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy");
    SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mm a");
    // InitialRecordController inic = new InitialRecordController(loggedInMID.longValue());
    //Log view OB record action
    //inic.addOBRecordViewLog(pid, dateFormat.format(record.getEDD()));
    //TODO
%>
<h2>Childbrith Visit Record Detail</h2>
</br>
<table width=50%>
    <tr>
        <td><b>Patient Name: <%=first_name + " " + last_name%></b></td>
        <td align="right"><b>Patient ID: <%=pidString%></b></td>
    </tr>
</table>

<table class='fTable' width=50%>
    <tr>
        <th width=50%>Basic Information</th>
        <th width=50%></th>
    </tr>
    <tr>
        <td>Visit type:</td>
        <td><%=record.getVisitType()%></td>
    </tr>
    <tr>
        <td>Preferred delivery type:</td>
        <td><%=record.getPreferredDeliveryType() == null || record.getPreferredDeliveryType().equals("") ? "N/A" : record.getPreferredDeliveryType()%></td>
    </tr>
</table>
</br></br>
<table class='fTable' width=50%>
    <tr>
        <th width=50%>Drug Information</th>
        <th width=50%></th>
    </tr>
    <tr>
        <td>Pitocin:</td>
        <td align="left"><%=record.getDosage_pitocin()%>&nbsp;&nbsp;mg</tdalign>
    </tr>
    <tr>
        <td>Nitrous oxide:</td>
        <td align="left"><%=record.getDosage_nitrousOxide()%>&nbsp;&nbsp;mg</td>
    </tr>
    <tr>
        <td>Pethidine:</td>
        <td align="left"><%=record.getDosage_pethidine()%>&nbsp;&nbsp;mg</td>
    </tr>
    <tr>
        <td>Epidural anaesthesia:</td>
        <td align="left"><%=record.getDosage_epiduralAnaesthesia()%>&nbsp;&nbsp;mg</td>
    </tr>
    <tr>
        <td>Magnesium sulfate:</td>
        <td align="left"><%=record.getDosage_magnesiumSulfate()%>&nbsp;&nbsp;mg</td>
    </tr>
    <tr>
        <td>RH immune globulin:</td>
        <td align="left"><%=record.getDosage_rhImmuneGlobulin()%>&nbsp;&nbsp;mg</td>
    </tr>
</table>
<br/><br/>
<table class='fTable' width=50%>
    <tr>
        <th width=50%>Additional Information</th>
        <th width=50%></th>
    </tr>
    <tr>
        <td>Delivery Date:</td>
        <td><%=dateFormatter.format(record.getDeliveryDate())%></td>
    </tr>
    <tr>
        <td>Delivery time:</td>
        <td><%=timeFormatter.format(record.getDeliveryTime())%></td>
    </tr>
    <tr>
        <td>Actual delivery type:</td>
        <td><%=record.getDeliveryType()%></td>
    </tr>
    <tr>
        <td>Number of boys:</td>
        <td><%=record.getNumberOfBoyBabies()%></td>
    </tr>
    <tr>
        <td>Number of girls:</td>
        <td><%=record.getNumberOfGirlBabies()%></td>
    </tr>
</table>
</br></br>
<p>Comment:</p>
<textarea cols="100" rows="10" readonly><%=record.getComment()%></textarea>
<%@include file="/footer.jsp"%>
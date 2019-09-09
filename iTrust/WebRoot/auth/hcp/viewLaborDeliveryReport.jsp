<%--
  Created by IntelliJ IDEA.
  User: vincent.k.z
  Date: 12/3/18
  Time: 1:14 PM
  To change this template use File | Settings | File Templates.
--%>
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
<%@page import="edu.ncsu.csc.itrust.controller.obgyn.report.reportController"%>
<%@page import="java.lang.Long"%>
<%@page import="java.lang.Boolean"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.time.LocalDate"%>
<%@page import="java.time.format.DateTimeFormatter"%>
<%@page import="edu.ncsu.csc.itrust.model.obgyn.initialRecord.initialRecord"%>
<%@page import="edu.ncsu.csc.itrust.model.obgyn.report.report"%>
<%@page import="edu.ncsu.csc.itrust.model.obgyn.report.obVisitInfo"%>
<%@page import="edu.ncsu.csc.itrust.model.obgyn.report.pastPregnancy"%>
<%@page import="java.util.List"%>

<%@include file="/global.jsp"%>

<%
    pageTitle = "iTrust - View OB Record";
%>

<%@include file="/header.jsp"%>

<h2>Labor and Delivery Report</h2>
</br>

<%
    /* Require a Patient ID first */


    String pidString = (String) session.getAttribute("pid");
    String first_name = (String) session.getAttribute("first_name");
    String last_name = (String) session.getAttribute("last_name");
    String eligibility = (String) session.getAttribute("eligibility");
    if (pidString == null || pidString.equals("") || pidString.length() < 1) {
        out.println("pidstring is null");
        response.sendRedirect("/iTrust/auth/getPatientID.jsp?forward=hcp/viewLaborDeliveryReport.jsp");
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

            boolean succeed = false;
            succeed = inic.updateOBeligibility(pidString, Boolean.valueOf(new_eligibility));

            if (succeed) {
                session.setAttribute("eligibility", new_eligibility);
                eligibility = new_eligibility;
    %>
    <p style="color:green;">Eligibility Successfully Updated</p>
    <%
    } else {
    %>
    <p style="color:red;">Failed updating Eligibility</p>
    <%
        }
    } else if (action_name.equals("Add New Record")) {
        if (eligibility.equals("true")) {
            response.sendRedirect("addOBRecord.jsp?forward=hcp-uap/viewLaborDeliveryReport.jsp&UID_PATIENTID=" + pidString);
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

    //control the page status: "main", "pastPregRecord", "obVisits"
    String pageStatus = request.getParameter("pageStatus") == null ? "Main Page" : (request.getParameter("pageStatus"));

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
<form id="changeEligibility" action="viewLaborDeliveryReport.jsp" method="post">
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
<%
    }
    if (true) {
        reportController rc = new reportController(loggedInMID.longValue());
        report r = rc.getPatientReportByMID(pidString);
        if (r == null) System.out.print("not null");

        if (r == null || rc.isNotElegibleOrHasNoReport(pidString)) { %>
          <p>Selected patient does not have an obstetrics record
          </p>

        <%
            return;
        }

//      //section 1: unique information
        String bloodType = r.getBloodType();
        String EDD = r.getEDD().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

//      //section 2: list of information for each past pregnancy
        List<pastPregnancy> pregList = r.getPastPregnancyList();

//      //section 3: Obstetrics Office Visit Information, most recent visit first
        List<obVisitInfo> obVisitList = r.getObVisitInfoList();

        if (pageStatus.equals("Main Page")){
            %>
            <table class="fTable" width="100%">
                <tbody><tr>
                    <th width="30%">Patient Name: <%=first_name + " " + last_name%></th>
                    <th width="20%">Patient ID: <%=pidString%></th>
                    <th width="20%">Blood Type: <%=bloodType%></th>
                    <th width="30%">Estimated Delivery Data: <%=EDD%></th>
                </tr>
                </tbody>
            </table>
            <br/>

            <!-- Example row of columns -->
            <div class="row">
            <div class="col-md-6">
            <h3>Past Pregnancies</h3><br/>
            <p>See information for each past pregnancy records including Pregnancy term, Delivery type, Conception year.</p><br/>
            <p>
                <%--<a class="btn btn-secondary" href="#" role="button" style="color:white;background-color:#808080;">View details</a>--%>
                <form action="viewLaborDeliveryReport.jsp" method="post">
                    <input type="submit" value="View Pregant Records" name="pageStatus">
                </form>
            </p>
            </div>
            <div class="col-md-6">
            <h3>Obstetrics Office Visits</h3><br/>
            <p>See Obstetrics Office Visit Information including Weeks pregnant, Weight, Blood pressure, Fetal heart rate (FHR), pregnancy times, low lying placenta and Complications. </p><br/>
            <p>
                <%--<a class="btn btn-primary" href="#" role="button" style="color:white;background-color:#808080;">View details</a>--%>
                <form action="viewLaborDeliveryReport.jsp" method="post">
                    <input type="submit" value="View Obstetrics Visits" name="pageStatus">
                </form>
            </p>

            </div>
            </div>

            <hr>
        <% } else if (pageStatus.equals("View Pregant Records")) {
                int index = 1;
                for (pastPregnancy pgRecord : pregList) {
            %>
                    <div class="panel panel-default">
                        <!-- Default panel contents -->
                        <div class="panel-heading">Record <%=index%></div>

                        <!-- Table -->
                        <table>
                            <tr>
                                <th>Item</th>
                                <th>Details</th>
                            </tr>
                            <tr>
                                <td>pregnancyTerm</td>
                                <td><%=pgRecord.getPregnancyTerm()%></td>
                            </tr>
                            <tr>
                                <td>conceptionYear</td>
                                <td><%=pgRecord.getConceptionYear()%></td>
                            </tr>
                            <tr>
                                <td>deliveryType</td>
                                <td><%=pgRecord.getDeliveryType()%></td>
                            </tr>

                        </table>
                    </div>
                    <br/>

            <%
                    index ++;
                }
            %>

            <form action="viewLaborDeliveryReport.jsp" method="post">
                <input type="submit" value="Main Page" name="pageStatus">
            </form>
<% } else if (pageStatus.equals("View Obstetrics Visits")) {
            int index = 1;
            for (obVisitInfo obvisit : obVisitList) {
            %>

            <div class="panel panel-default">
                <!-- Default panel contents -->
                <div class="panel-heading">Record <%=index%></div>

                <!-- Table -->
                <table class="table">
                    <tr>
                        <th>Item</th>
                        <th>Details</th>
                    </tr>
                    <tr>
                        <td>Weeks of Pregnant</td>
                        <td><%=obvisit.getNumOfWeeksPregnant()%></td>
                    </tr>
                    <tr>
                        <td>Weight(kg)</td>
                        <td><%=obvisit.getWeight()%></td>
                    </tr>
                    <tr>
                        <td>blood Pressure(mmHg)</td>
                        <td><%=obvisit.getBloodPressure()%></td>
                    </tr>
                    <tr>
                        <td>fetal Heart Rate(/s)</td>
                        <td><%=obvisit.getFetalHeartRate()%></td>
                    </tr>
                    <tr>
                        <td>Baby number</td>
                        <td><%=obvisit.getNumOfMultiples()%></td>
                    </tr>
                    <tr>
                        <td>Low Lying Placenta</td>
                        <td><%=obvisit.isLowLyingPlacentaObserved()%></td>
                    </tr>
                    <tr>
                        <td style="vertical-align:middle;">Complications</td>
                        <% if (obvisit.getComplications())  { //has complications%>
                        <td>
                            <% if (obvisit.isRHFlag()) { // RHFlag %>
                                RH - flag <br>
                            <% } %>
                            <% if (obvisit.isHighBloodPressure()) { // RHFlag %>
                                High Blood Pressure <br>
                            <% } %>
                            <% if (obvisit.isAdvancedMaternalAge()) { // RHFlag %>
                                Advanced Maternal Age <br>
                            <% } %>
                            <% if (obvisit.isLowLyingPlacenta()) { // RHFlag %>
                                Low Lying Placenta <br>
                            <% } %>
                            <% if (obvisit.isAbnormalFetalHeartRate()) { // RHFlag %>
                                Abnormal Fetal Heart Rate <br>
                            <% } %>
                            <% if (obvisit.isMultiplePregnancy()) { // RHFlag %>
                                Multiple Pregnancy <br>
                            <% } %>
                            <% if (obvisit.isAtypicalWeightChange()) { // RHFlag %>
                                Atypical Weight Change <br>
                            <% } %>
                            <% if (obvisit.isHyperemesisGravidarum()) { // RHFlag %>
                                Hyperemesis Gravidarum <br>
                            <% } %>
                            <% if (obvisit.isHypothyroidism()) { // RHFlag %>
                                Hypothyroidism <br>
                            <% } %>
                        </td>

                        <%} else { %>
                            <td>None</td>
                        <% } %>
                    </tr>
                    <tr>
                        <td style="vertical-align:middle;">Pre Existing Conditions</td>
                        <% if (obvisit.getPreExistingConditions())  { //has pre existing conditions%>
                        <td>
                            <% if (obvisit.isDiabetes()) { // RHFlag %>
                                Diabetes <br>
                            <% } %>
                            <% if (obvisit.isAutoimmuneDisorders()) { // RHFlag %>
                                Autoimmune Disorders <br>
                            <% } %>
                            <% if (obvisit.isCancers()) { // RHFlag %>
                                Cancers <br>
                            <% } %>
                            <% if (obvisit.isSTDs()) { // RHFlag %>
                                STDs <br>
                            <% } %>
                        </td>

                        <%} else { %>
                        <td>None</td>
                        <% } %>
                    </tr>
                    <tr>
                        <td style="vertical-align:middle;">Drug Allergies</td>
                        <% if (obvisit.getDrugAllergies())  { //has pre existing conditions%>
                        <td>
                            <% if (obvisit.isSulfaDrugs()) { // RHFlag %>
                                Sulfa drugs<br>
                            <% } %>
                            <% if (obvisit.isPenicillin()) { // RHFlag %>
                                Penicillin<br>
                            <% } %>
                            <% if (obvisit.isTetracycline()) { // RHFlag %>
                                Tetracycline<br>
                            <% } %>
                            <% if (obvisit.isCodeine()) { // RHFlag %>
                                Codeine<br>
                            <% } %>
                            <% if (obvisit.isNSAIDs()) { // RHFlag %>
                                NSAIDs<br>
                            <% } %>
                        </td>
                        <%} else { %>
                        <td>None</td>
                        <% } %>
                    </tr>
                </table>
            </div>
            <br/>

            <%
                index ++;
            }
            %>
            <form action="viewLaborDeliveryReport.jsp" method="post">
                <input type="submit" value="Main Page" name="pageStatus">
            </form>
        <% }

    } else {
        %><p>No record found</p><%
    }
%>


<%@include file="/footer.jsp"%>

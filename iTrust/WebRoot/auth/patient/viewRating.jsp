<%@taglib prefix="itrust" uri="/WEB-INF/tags.tld"%>
<%@page errorPage ="/auth/exceptionHandler.jsp"%>

<%@page import="edu.ncsu.csc.itrust.model.old.dao.DAOFactory"%>

<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>
<%@page import="edu.ncsu.csc.itrust.exception.DBException"%>

<%@page import="edu.ncsu.csc.itrust.controller.ratings.RatingController"%>
<%@page import="edu.ncsu.csc.itrust.model.ratings.Rating"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewMyApptsAction"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.util.List"%>
<%@page import="java.lang.Math"%>
<%@page import="java.lang.Integer"%>
<%@page import="java.lang.Float"%>

<link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.5.0/css/all.css" integrity="sha384-B4dIYHKNBt8Bc12p+WXckhzcICo0wtJAoU8YZTY5qE0Id1GSseTk6S+L3BlXeVIU" crossorigin="anonymous">
<link href="/iTrust/css/uc807.css" type="text/css" rel="stylesheet">
<%@include file="/global.jsp"%>

<%
    /**
     * viewRating.jsp for display of details for a existed Rating which is clicked by the user in the viewMyAppts.jsp
     */

    pageTitle = "iTrust - Rate Doctor";
    String[] rating_metrics = new String[]{"punctuality", "attitude" ,"skillfulness"
            ,"knowledge","efficiency"};
%>

<%@include file="/header.jsp"%>

<%
    // get some static information from the prior pages
    String doctor_name = request.getParameter("dname");
    doctor_name = (doctor_name == null || doctor_name == "") ? "Unknown" : doctor_name;
    String appt_date = request.getParameter("aptdate");
    appt_date = (appt_date == null || appt_date == "") ? "Unknown" : appt_date;
    String appt_id = request.getParameter("aptid");
    appt_id = (appt_id == null || appt_id == "") ? "" : appt_id;
    String comment = request.getParameter("comment");
    comment = (comment == null) ? "" : comment;

    int aptid = Integer.parseInt(appt_id);
    int punctuality = 0;
    int attitude = 0;
    int skillfulness = 0;
    int knowledge = 0;
    int efficiency = 0;


//    init of controller
    RatingController rc;
    Rating newRating;
    rc = new RatingController(loggedInMID.longValue());
    Rating currentRating = null;
    boolean succeed = true;
    try{
        currentRating = rc.getRating(appt_id);
    }catch(DBException e){
        succeed =false;
//        response.sendRedirect("viewMyAppts.jsp");
    }catch(FormValidationException e){
        succeed = false;
//        response.sendRedirect("viewMyAppts.jsp");
    }

    if(!succeed){
%>

<p style="color:red;">Error occured..redirect will be take place in 1s</p>
<script type="text/javascript">

    // error handling, if get() is not working correctly, it will redirect to viweMyAppt.jsp in 1 second
    function sleep (time) {
        return new Promise((resolve) => setTimeout(resolve, time));
    }
    // sleep for 3 seconds
    sleep(1000).then(() => {
        // redirect to ob record page
        window.location.replace("viewMyAppts.jsp");
    })
</script>


<%

    }

    // if success
    else{
        punctuality = currentRating.getPunctuality().intValue();
        attitude = currentRating.getAttitude().intValue();
        skillfulness = currentRating.getSkillfulness().intValue();
        knowledge = currentRating.getKnowledge().intValue();
        efficiency = currentRating.getEfficiency().intValue();
        comment = currentRating.getComment();
        Integer[] five_rating = new Integer[5];
        five_rating[0] = new Integer(punctuality);
        five_rating[1] =new Integer(attitude);
        five_rating[2] =new Integer(skillfulness);
        five_rating[3] =new Integer(knowledge);
        five_rating[4] =new Integer(efficiency);

%>

<div class="center-block">
    <form id="createRatingReview" class="center-block">


        <table class="fTable" width="70%" BORDERCOLOR="white">
            <tr>
                <th width="45%">item</th>
                <th width="45%">input field</th>
                <th width="5%" hidden>errors list </th>
            </tr>
            <tr>
                <td>Doctor Name: </td>
                <td><span><%=doctor_name%></span></td>
                <td hidden></td>
            </tr>
            <tr>
                <td>Date of the visit: </td>
                <td><span><%=appt_date%></span></td>
                <td hidden></td>
            </tr>

            <tr >
                <td>Comment: </td>
                <td><input name="comment" type="text" placeholder="<%=comment%>" disabled></td>
                <td hidden></td>
            </tr>

        </table>

        <br />
            <%
            for(int i =0; i< 5; i++){
                String type = rating_metrics[i];

            %>

        <fieldset class="rating" class="center-block">
            <legend><%=("rate on " + type)%></legend>

            <%
                // select the right input in the fieldset according to the value retrieved from controller
            for(int j = 0; j< 5; j++){

                    if( (5 - j) == five_rating[i].intValue()){
                        // checked
            %>
                        <input type="radio" id="<%=(type + Integer.toString(j))%>" name="<%=type%>" value="<%=(5-j)%>" checked disabled/><label for="<%=(type + Integer.toString(j))%>"></label>
            <%
                    }
                    else{
                        // not checked
            %>
                        <input type="radio" id="<%=(type + Integer.toString(j))%>" name="<%=type%>" value="<%=(5-j)%>" disabled/><label for="<%=(type + Integer.toString(j))%>"></label>
            <%
                    }

            }
            %>

        </fieldset>

            <%
        }
%>

        <a class="iTrustNavlink" href="viewMyAppts.jsp">Back To My Appointments</a>
</div>

<%
    }
%>

<%@include file="/footer.jsp"%>
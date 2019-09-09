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


<link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.5.0/css/all.css" integrity="sha384-B4dIYHKNBt8Bc12p+WXckhzcICo0wtJAoU8YZTY5qE0Id1GSseTk6S+L3BlXeVIU" crossorigin="anonymous">
<link href="/iTrust/css/uc807.css" type="text/css" rel="stylesheet">
<%@include file="/global.jsp"%>

<%
    /**
     * addRating pages for add a Rating for completed appointment
     * @Author: Xiaocong Yu
     */

    pageTitle = "iTrust - Rate Doctor";
%>

<%@include file="/header.jsp"%>

<%
    String[] rating_metrics = new String[]{"punctuality", "attitude" ,"skillfulness"
        ,"knowledge","efficiency"};
    String[] type_errors = new String[5];
%>

<%
    // get display messages from request
    String appt_id =  request.getParameter("aptid");
    String doctor_id = request.getParameter("did");
    String doctor_name = request.getParameter("dname");
    String appt_date =  request.getParameter("aptdate");
    String comment = request.getParameter("comment");

    doctor_name = (doctor_name == null || doctor_name == "") ? "Unknown" : doctor_name;
    appt_date = (appt_date == null || appt_date == "") ? "Unknown" : appt_date;
    comment = (comment == null) ? "" : comment;

    String action = request.getParameter("action");

    // get from viewMyAppt hyperlink
    Integer a_id = (appt_id == null )? null : Integer.parseInt(appt_id);
    Long d_id = (doctor_id == null)? null : Long.parseLong(doctor_id);

    // controller init
    ViewMyApptsAction appt_controller = new ViewMyApptsAction(prodDAO, loggedInMID.longValue());
    RatingController rc;
    Rating newRating;
    boolean isValid = true;

    // branch for receiving addRating request
    if(action != null && action.equals("Add Record")){
        rc = new RatingController(loggedInMID.longValue());
        newRating = new Rating();

        Float metric_punctuality = Float.parseFloat(request.getParameter("punctuality"));
        Float metric_attitude = Float.parseFloat(request.getParameter("attitude"));
        Float metric_skillfulness = Float.parseFloat(request.getParameter("skillfulness"));
        Float metric_knowledge = Float.parseFloat(request.getParameter("knowledge"));
        Float metric_efficiency = Float.parseFloat(request.getParameter("efficiency"));

        // session attribute
        doctor_name = (session.getAttribute("dname") == null) ? doctor_name : ((String) session.getAttribute("dname"));
        appt_date = (session.getAttribute("aptdate") == null) ? appt_date : ((String) session.getAttribute("aptdate"));
        d_id = (session.getAttribute("did") == null) ? d_id : ((Long) session.getAttribute("did"));
        a_id = (session.getAttribute("aptid") == null) ? a_id : ((Integer) session.getAttribute("aptid"));

        newRating.setComment(comment);
        newRating.setPunctuality(metric_punctuality);
        newRating.setAttitude(metric_attitude);
        newRating.setSkillfulness(metric_skillfulness);
        newRating.setKnowledge(metric_knowledge);
        newRating.setEfficiency(metric_efficiency);
        newRating.setPatient_id(loggedInMID);
        newRating.setDoctor_id(d_id);
        newRating.setAppt_id(a_id);

        boolean succeed = true;

        try{
            rc.addRating(newRating);
            appt_controller.updateIsRated(a_id);
            System.out.println("success in add rating and update isRated");

        }catch(DBException e){
            succeed = false;
            System.out.println("failed here DBEX");
            e.printStackTrace();
        }catch(FormValidationException e){
            succeed = false;
            System.out.println("failed here FVEX");
            List<String> errorList =e.getErrorList();
            for (int i = 0; i < errorList.size(); i++) {
                type_errors[i] = errorList.get(i);
            }

        }
        if( succeed ){


%>

<p id="test_id" style="color:green;">Childbirth Visit Record Successfully Updated, redirect in 3 seconds...</p>
<script type="text/javascript">
    function sleep (time) {
        return new Promise((resolve) => setTimeout(resolve, time));
    }

    // sleep for 3 seconds
    sleep(3000).then(() => {
        // redirect to ob record page
        console.log("hit");
        window.location.replace("viewRating.jsp?aptid=" + "<%=a_id%>" + "&dname=" + "<%=doctor_name%>" + "&aptdate=" + "<%=appt_date%>");
    })

</script>


<%
//        removeSession();

        }else{

%>

<p style="color:red;">Error occured..</p>

<%
        }

    }

    // if action != addRating, then just display the page for rating
    else {
        // session setting
        session.setAttribute("aptid", a_id);
        session.setAttribute("did", d_id);
        session.setAttribute("aptdate", appt_date);
        session.setAttribute("dname", doctor_name);
//        ==DEBUG==
//        System.out.println("action not equal to add rating");
    }
%>

<div id="status" class="center-block">
</div>

<div class="center-block">
    <form id="createRatingReview" action="addRating.jsp" class="center-block" method="post">

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
                <td>
                    <textarea name="comment" cols="70" rows="10" placeholder="any comment will be helpful to other patients"><%=comment%></textarea>
                </td>
                <td hidden></td>
            </tr>

        </table>


        <br />


<%
        for(String type: rating_metrics){
%>

        <fieldset class="rating">
            <legend><%=("Rating The " + type)%></legend>
            <input type="radio" id="<%=("input1_"+ type)%>" name="<%=type %>" value="5" /><label for="<%=("input1_"+ type)%>" title="Rocks!"></label>
            <input type="radio" id="<%=("input2_"+ type)%>" name="<%=type %>" value="4" /><label for="<%=("input2_"+ type)%>" title="Pretty good"></label>
            <input type="radio" id="<%=("input3_"+ type)%>" name="<%=type %>" value="3" /><label for="<%=("input3_"+ type)%>" title="Meh"></label>
            <input type="radio" id="<%=("input4_"+ type)%>" name="<%=type %>" value="2" /><label for="<%=("input4_"+ type)%>" title="Kinda bad"></label>
            <input type="radio" id="<%=("input5_"+ type)%>" name="<%=type %>" value="1" /><label for="<%=("input5_"+ type)%>" title="Sucks big time"></label>
        </fieldset>

            <%
        };


%>
        <div class="clearfix"></div>
        <br />
        <input type="submit" class="center-block clearfix" name = "action" value="Add Record">
</div>

<%@include file="/footer.jsp"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="java.util.List"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.sql.Timestamp"%>
<%@page import="edu.ncsu.csc.itrust.action.EditApptTypeAction"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewMyApptsAction"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.ApptBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.dao.mysql.ApptTypeDAO"%>
<%@page import="edu.ncsu.csc.itrust.model.old.dao.DAOFactory"%>
<%@page import="java.util.Calendar"%>

<%@include file="/global.jsp" %>

<%
pageTitle = "iTrust - View My Appointments";
%>

<%@include file="/header.jsp" %>

<div align=center>
	<h2>My Appointments</h2>
<%
	loggingAction.logEvent(TransactionType.APPOINTMENT_ALL_VIEW, loggedInMID.longValue(), 0, "");
	
	ViewMyApptsAction action = new ViewMyApptsAction(prodDAO, loggedInMID.longValue());
	EditApptTypeAction types = new EditApptTypeAction(prodDAO, loggedInMID.longValue());
	ApptTypeDAO apptTypeDAO = prodDAO.getApptTypeDAO();
	List<ApptBean> appts = action.getMyAppointments();
	session.setAttribute("appts", appts);
	if (appts.size() > 0) {
%>


	<table class="fancyTable">
		<tr>
			<%// TODO fancy table add some background color for apptment %>
			<th>Doctor Name</th>
			<th>Appointment Type</th>
			<th>Appointment Date/Time</th>
			<th>Duration</th>
            <th>Comment</th>
			<th>Rating Record</th>
            <th>Add to Google Calendar</th>
		</tr>
<%		 
		

		List<ApptBean>conflicts = action.getAllConflicts(loggedInMID.longValue());

		int index = 0;
		for(ApptBean a : appts) { 
			String comment = "";
			if(a.getComment() == null)
				comment = "No Comment";
			else
				comment = "<a href='viewAppt.jsp?apt=" + a.getApptID() + "'>Read Comment</a>";
				
			Date d = new Date(a.getDate().getTime());
			DateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm a");

			//auto google
			Calendar cal = Calendar.getInstance();
			cal.setTime(d);
			cal.add(Calendar.HOUR_OF_DAY, 6);
			String years = Integer.toString(cal.get(Calendar.YEAR));
			String hours = Integer.toString(cal.get(Calendar.HOUR_OF_DAY));
			if (hours.length() == 1){
				hours = "0" + hours;
			}
			String minutes = Integer.toString(cal.get(Calendar.MINUTE));
			if (minutes.length() == 1){
				minutes = "0" + minutes;
			}
			//month start from 0
			String month = Integer.toString(cal.get(Calendar.MONTH) + 1);
			if (month.length() == 1){
				month = "0" + month;
			}
			String days = Integer.toString(cal.get(Calendar.DATE));
			if (days.length() == 1){
				days = "0" + days;
			}
			cal.add(Calendar.HOUR_OF_DAY, 1);
			String end_years = Integer.toString(cal.get(Calendar.YEAR));
			String end_hours = Integer.toString(cal.get(Calendar.HOUR_OF_DAY));
			if (end_hours.length() == 1){
				end_hours  = "0" + end_hours;
			}
			String end_minutes = Integer.toString(cal.get(Calendar.MINUTE));
			if (end_minutes.length() == 1){
				end_minutes = "0" + end_minutes;
			}
			String end_month = Integer.toString(cal.get(Calendar.MONTH) + 1);
			if (end_month.length() == 1){
				end_month = "0" + end_month;
			}
			String end_days = Integer.toString(cal.get(Calendar.DATE));
			if (end_days.length() == 1){
				end_days = "0" + end_days;
			}
			String dis_appt_type = a.getApptType();
			//end google

			String row = "";
			if(conflicts.contains(a))
				row = "<tr style='font-weight: bold;'";
			else
				row = "<tr";

			%>
			<%=row+" "+((index%2 == 1)?"class=\"alt\"":"")+">"%>
				<td><%= StringEscapeUtils.escapeHtml("" + ( action.getName(a.getHcp()) )) %></td>
				<td><%= StringEscapeUtils.escapeHtml("" + ( a.getApptType() )) %></td>
				<td><%= StringEscapeUtils.escapeHtml("" + ( format.format(d) )) %></td>
 				<td><%= StringEscapeUtils.escapeHtml("" + ( apptTypeDAO.getApptType(a.getApptType()).getDuration()+" minutes" )) %></td>
				<td><%= comment %></td>


        <%

            //TODO  get the appointment info on :  appt_date, doctor_name, doctor_id, MID(auto), isRated(controller modify required)


			boolean isRated = a.getIsRated();
			String isRatedLink = "";
            Date today = new Date(System.currentTimeMillis());

            if(isRated){
                isRatedLink = "<a href='viewRating.jsp?aptid=" + a.getApptID() + "&dname=" + action.getName(a.getHcp()) + "&aptdate=" + format.format(d) +  "'>" + "Read Rating" + "</a>" ;
            }else{
                // state: the visit hasn't occurred
                if(today.compareTo(d) < 0){
                    isRatedLink = "Incomplete Visit";
                }

                // state: rate now
                else{isRatedLink = "<a href='addRating.jsp?aptid=" + a.getApptID() + "&dname=" + action.getName(a.getHcp()) + "&did=" + a.getHcp() + "&aptdate=" + format.format(d) +  "'>Rate Now!" + "</a>";}

            }

        %>
                <td><%=isRatedLink%></td>>
                <td><input type='button' style='width:100px;' onclick="parent.location.href='http://www.google.com/calendar/event?action=TEMPLATE&dates=<%=years%><%=month%><%=days%>T<%=hours%><%=minutes%>00Z%2F<%=end_years%><%=end_month%><%=end_days%>T<%=end_hours%><%=end_minutes%>00Z&text=<%=dis_appt_type%>&location=iTrust&details=';" value="Add"></td>


        </tr>


	<%		index ++; %>
	<%	} %>
	</table>
<%	} else { %>
	<div>
		<i>You have no Appointments</i>
	</div>
<%	} %>	
	<br />
</div>

<%@include file="/footer.jsp" %>

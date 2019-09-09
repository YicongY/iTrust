<%@page import="java.util.Date"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.ReviewsBean"%>
<%@page import="org.jfree.ui.Align"%>
<%@page import="edu.ncsu.csc.itrust.action.ReviewsAction"%>
<%@page import="edu.ncsu.csc.itrust.action.ZipCodeAction"%>
<%@page errorPage="/auth/exceptionHandler.jsp" %>
<%@page import="java.net.URLEncoder" %>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Map"%>
<%@page import="java.lang.Double"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="edu.ncsu.csc.itrust.model.old.dao.DAOFactory"%>
<%@page import="edu.ncsu.csc.itrust.action.FindExpertAction"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.HospitalBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.beans.PersonnelBean"%>
<%@page import="edu.ncsu.csc.itrust.model.old.dao.mysql.PatientDAO"%>
<%@page import="edu.ncsu.csc.itrust.model.ratings.Rating"%>
<%@page import="edu.ncsu.csc.itrust.controller.ratings.RatingController"%>
<%@page import="java.util.HashMap"%>

<%@include file="/global.jsp" %>

<% pageTitle = "iTrust - Reviews Page"; %>

<%@include file="/header.jsp" %>



<%
	DecimalFormat df = new DecimalFormat(".##");
	String mid = request.getParameter("expertID");
	String rating = null;
	long expertID = -1;
	if(mid != null)
	{
		expertID = Long.parseLong(mid);
		session.setAttribute("expertID", mid);
		loggingAction.logEvent(TransactionType.VIEW_REVIEWS, loggedInMID, expertID, "");
		response.sendRedirect("/iTrust/auth/patient/reviewsPage.jsp");
		return;
	}
	if(session.getAttribute("expertID") != null){
		try {
			expertID = Long.parseLong((String)session.getAttribute("expertID"));
		} catch (NumberFormatException e){
			%> <h1>User does not exist!</h1> <%
			return;
		}
	}

	ReviewsAction reviewsAction = new ReviewsAction(prodDAO, loggedInMID.longValue());
		String reviewTitle = request.getParameter("title");
		String reviewRating = request.getParameter("rating");
		String description = request.getParameter("description");

		if(reviewTitle != null && reviewRating != null && description != null)
		{
			loggingAction.logEvent(TransactionType.SUBMIT_REVIEW, loggedInMID, expertID, "");
			ReviewsBean review = new ReviewsBean();
			review.setDescriptiveReview(description);
			review.setRating(Integer.parseInt(reviewRating));
			review.setTitle(reviewTitle);
			review.setMID(loggedInMID.longValue());
			review.setPID(expertID);
			review.setDateOfReview(new Date());

			reviewsAction.addReview(review);

		}

		RatingController rc  = new RatingController(loggedInMID.longValue());
		Map<String, Float> results = rc.getOverallRating(Long.toString(expertID));
//		double punctuality = results.get("punctuality");
//		double attitude = results.get("attitude");
//		double skillfulness = results.get("skillfulness");
//		double knowledge = results.get("knowledge");
//		double efficiency = results.get("efficiency");
//
//		double punctuality_ratio = punctuality / 5;
//		double attitude_ratio = attitude / 5;
//		double skillfulness_ratio = skillfulness / 5;
//		double knowledge_ratio = knowledge / 5;
//		double efficiency_ratio = efficiency / 5;

	double punctuality = results.get("punctuality");
	double attitude = results.get("attitude");
	double skillfulness = results.get("skillfulness");
	double knowledge = results.get("knowledge");
	double efficiency = results.get("efficiency");

	punctuality = punctuality == punctuality ? punctuality : 0; //check for NAN (NAN == NAN => false)
	attitude = attitude == attitude ? attitude : 0;
	skillfulness = skillfulness == skillfulness ? skillfulness : 0;
	knowledge = knowledge == knowledge ? knowledge : 0;
	efficiency = efficiency == efficiency ? efficiency : 0;

	double punctuality_ratio = punctuality / 5;
	double attitude_ratio = attitude / 5;
	double skillfulness_ratio = skillfulness / 5;
	double knowledge_ratio = knowledge / 5;
	double efficiency_ratio = efficiency / 5;



	int ratingSize = rc.getRatingPeopleNumber(Long.toString(expertID));


	if(expertID != -1)
	{
		List<ReviewsBean> reviews = reviewsAction.getReviews(expertID);
		PersonnelBean physician = reviewsAction.getPhysician(expertID);
		%>

		<!DOCTYPE HTML>
		<html>
		<head>
			<meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
			<title>Radar chart</title>
			<link href="/iTrust/css.jsp" type="text/css" rel="stylesheet" />
			<link href="/iTrust/css/bootstrap.min.css" rel="stylesheet" />
			<link href="/iTrust/css/dashboard.css" rel="stylesheet" />
			<link href="/iTrust/css/datepicker.css" type="text/css" rel="stylesheet" />
			<link href="/iTrust/css/starrating.css" type="text/css" rel="stylesheet" />
			<link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.5.0/css/all.css" integrity="sha384-B4dIYHKNBt8Bc12p+WXckhzcICo0wtJAoU8YZTY5qE0Id1GSseTk6S+L3BlXeVIU" crossorigin="anonymous">
			<script src="/iTrust/js/DatePicker.js" type="text/javascript"></script>
			<script src="/iTrust/js/jquery-1.8.3.js" type="text/javascript"></script>
			<script src="/iTrust/js/SwipeableElem.js" type="text/javascript"></script>
			<script src="/iTrust/js/slidyRabbit.js" type="text/javascript"></script>
			<script src="http://d3js.org/d3.v3.min.js"></script>
			<script src="/iTrust/js/radarChart.js" type="text/javascript" ></script>
		</head>
		<body>

		<h1>Reviews for <%=physician.getFullName()%></h1>
		<br>

		<div id="body">
			<div id="chart" style="text-align:center;"></div>
			<div class="container mt-5">
				<table class="table table-striped">
					<%--<thead>--%>
					<%--<tr>--%>
						<%--<th>4K Television</th>--%>
						<%--<th>Rating</th>--%>
					<%--</tr>--%>
					<%--</thead>--%>
					<tbody>
					<tr class="punctuality">
						<td>Punctuality</td>
						<td>
							<div class="stars-outer">
								<div class="stars-inner"></div>
							</div>
							<span class="number-rating"><%=ratingSize == 0 ? "N/A" : Double.toString(punctuality)%></span>
						</td>
					</tr>
					<tr class="attitude">
						<td>Attitude</td>
						<td>
							<div class="stars-outer">
								<div class="stars-inner"></div>
							</div>
							<span class="number-rating"><%=ratingSize == 0 ? "N/A" : Double.toString(attitude)%></span>
						</td>
					</tr>
					<tr class="skillfulness">
						<td>Skillfulness</td>
						<td>
							<div class="stars-outer">
								<div class="stars-inner"></div>
							</div>
							<span class="number-rating"><%=ratingSize == 0 ? "N/A" : Double.toString(skillfulness)%></span>
						</td>
					</tr>
					<tr class="knowledge">
						<td>Knowledge</td>
						<td>
							<div class="stars-outer">
								<div class="stars-inner"></div>
							</div>
							<span class="number-rating"><%=ratingSize == 0 ? "N/A" : Double.toString(knowledge)%></span>
						</td>
					</tr>
					<tr class="efficiency">
						<td>Efficiency</td>
						<td>
							<div class="stars-outer">
								<div class="stars-inner"></div>
							</div>
							<span class="number-rating"><%=ratingSize == 0 ? "N/A" : Double.toString(efficiency)%></span>
						</td>
					</tr>
					</tbody>
				</table>
			</div>
		</div>


		<script>
            var w = 500,
                h = 500;

            var colorscale = d3.scale.category10();

            //Legend titles
            var LegendOptions = ['Polarity'];

            //Data
            var d = [
                [
                    {axis:"Punctuality",value:<%=punctuality_ratio%>},
                    {axis:"Attitude",value:<%=attitude_ratio%>},
                    {axis:"Skillfulness",value:<%=skillfulness_ratio%>},
                    {axis:"Knowledge",value:<%=knowledge_ratio%>},
                    {axis:"Patience",value:<%=efficiency_ratio%>}
                ]
            ];

            //Options for the Radar chart, other than default
            var mycfg = {
                w: w,
                h: h,
                maxValue: 1,
                levels: 10,
                ExtraWidthX: 300
            }

            //Call function to draw the Radar chart
            //Will expect that data is in %'s
            RadarChart.draw("#chart", d, mycfg);

            ////////////////////////////////////////////
            /////////// Initiate legend ////////////////
            ////////////////////////////////////////////

            var svg = d3.select('#body')
                .selectAll('svg')
                .append('svg')
                .attr("width", w+300)
                .attr("height", h)

            //Create the title for the legend
            var text = svg.append("text")
                .attr("class", "title")
                .attr('transform', 'translate(90,0)')
                .attr("x", w - 70)
                .attr("y", 10)
                .attr("font-size", "15px")
                .attr("fill", "#404040")
                .text("The scores normalized from 0 to 100%");

            //Initiate Legend
            var legend = svg.append("g")
                .attr("class", "legend")
                .attr("height", 100)
                .attr("width", 200)
                .attr('transform', 'translate(90,20)')
            ;
            //Create colour squares
            legend.selectAll('rect')
                .data(LegendOptions)
                .enter()
                .append("rect")
                .attr("x", w - 65)
                .attr("y", function(d, i){ return i * 20;})
                .attr("width", 10)
                .attr("height", 10)
                .style("fill", function(d, i){ return colorscale(i);})
            ;
            //Create text next to squares
            legend.selectAll('text')
                .data(LegendOptions)
                .enter()
                .append("text")
                .attr("x", w - 52)
                .attr("y", function(d, i){ return i * 20 + 9;})
                .attr("font-size", "14px")
                .attr("fill", "#737373")
                .text(function(d) { return d; })
            ;


            // Initial Ratings
            var ratings = {
                punctuality: <%=df.format(punctuality)%>,
                attitude: <%=df.format(attitude)%>,
                skillfulness: <%=df.format(skillfulness)%>,
                knowledge: <%=df.format(knowledge)%>,
                efficiency: <%=df.format(efficiency)%>
            };

            // Total Stars
            var starsTotal = 5;
            // Run getRatings when DOM loads
            //document.addEventListener('DOMContentLoaded', getRatings);

            // Get ratings
            var i = 0;
            let getRatings = () => {
                for (var rating in ratings) {
                    // Get percentage
                    var starPercentage = (ratings[rating] / starsTotal) * 100;

                    // Round to nearest 10

                    var starPercentageRounded = Math.round(starPercentage / 10) * 10 + "%";

                    // Set width of stars-inner to percentage
                    var starClassName = "." + rating + " .stars-inner";
                    var numberClassName = "." + rating + " .number-rating";
                    document.querySelector(starClassName).style.width = starPercentageRounded;

                    console.log("rating size: " + <%=ratingSize%>);
                    var numberText = <%=ratingSize%> == 0 ? "N/A" : (ratings[rating] + "");
                    // var numberText = 1;
                    // Add number rating
                    document.querySelector(numberClassName).innerHTML = numberText;
                    i++;
                }
            }

            getRatings();
		</script>


		<%
		for(ReviewsBean reviewBean : reviews )
		{ %>
			<div class="grey-border-container">
				<p> <b><%= reviewBean.getTitle()%> </b> <span style="margin-right:10px"></span>

					<%
					for(int i = 0 ; i < 5 ; i++)
					{
						if(i < reviewBean.getRating())
						{
							%> <span class="glyphicon glyphicon-star" style="color:red;"></span><%
						}
						else
						{
							%> <span class="glyphicon glyphicon-star-empty"></span><%
						}

					}


					%>
				    </p>
				<p><%= reviewBean.getDescriptiveReview() %> </p>
				<p><%= reviewBean.getDateOfReview()%></p>
			</div>

	  <%}


	  		if(reviewsAction.isAbleToRate(expertID))
	  		{
	  %>
	  	<a href="#addModal" role="button" class="btn btn-primary" data-toggle="modal">Add a Review</a>


				<div id="addModal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="addReview" aria-hidden="true">
					<div class="modal-dialog">
					<div class="modal-content">

						<div class="modal-header" >
						<button type="button" class="close" data-dismiss="modal" aria-hidden="true" >x</button>
						<h3 class="modal-title" id="addReview">Add a Review</h3>
					 </div>
					 <div class="modal-body">
					 	<form class="form-horizontal" role="form" method="post" id="mainForm" name="mainForm">
					 	<div class="form-group">
					 	<p>
					 		<b>Title: </b> <input class="form-control" type="text" width="1" name="title">
					 	</p>
					 	 </div>
					 	<br>
					 	<div class="form-group">
					 	<b>Rating (out of 5): </b>
						<select class="form-control" name="rating">
						<option value="1">1</option>
						<option value= "2">2</option>
						<option value="3">3</option>
						<option value="4">4</option>
						<option value="5">5</option>
						</select>
						</div>
						<br>
						<br>
						<div class="form-group">
						<p>
							<b>Describe your experience: </b> <textarea style="margin-top:5px;width:100%;" rows="4" cols="80" name="description" class="form-control"></textarea>
						</p>
						</div>
					 <div class="modal-footer">
    				</div>
    				</form>

  					</div>
  					</div>
  					</div>
				</div>
				<%
				}
				 %>
		<% }
	else
	{
	%> <h1>User does not exist!</h1> <%
	}
 %>




	<%@include file="/footer.jsp"%>

	</body>
	</html>
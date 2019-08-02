<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<style>
.totalHour {
	color: #CC0000;
	font-size: 22px;
	font-weight: 700;
}
</style>

<header class="page-header">
	<!-- <h2>Personal Identification</h2> -->

	<div class="left-wrapper pull-left">
		<ol class="breadcrumbs">
			<li><a href="/pos/"> &nbsp;<i class="fa fa-home"></i></a></li>
			<li><span>Reports &nbsp;</span></li>
			<li><span>Payroll Report</span></li>
		</ol>
	</div>
</header>


<!-- start: page -->

<div class="row">

	<div class="col-lg-12">

		<!-- message -->
		<div id="msg">
			<c:if test="${!empty mCode}">
				<div
					class="alert
						<c:choose>
						<c:when test="${mCode == '0000'}"> <!-- false -->
						alert-danger 
						</c:when>
						<c:when test="${mCode == '1111'}"> <!-- true -->
						alert-success
						</c:when>
						<c:otherwise>
						alert-primary
						</c:otherwise>
						</c:choose>
						">

					<button class="close" aria-hidden="true" data-dismiss="alert"
						type="button">&times;</button>
					<strong>${message}</strong>
				</div>
			</c:if>
		</div>

		<form class="form-horizontal form-bordered" action="viewreport" target="_blank"
			method="post" autocomplete="off">

			<%-- <div class="form-group col-md-12">
				<input type="hidden" name="${_csrf.parameterName}"
					value="${_csrf.token}" /> <input type="hidden"
					id="encEffortTrackingId" name="encEffortTrackingId"
					value="${taskTrackingInfo.encEffortTrackingId}" />
			</div> --%>

			<!-- start: page -->
			<section class="panel panel-featured panel-featured-primary">
				<header class="panel-heading">
					<h2 class="panel-title">Payroll Report</h2>
				</header>
				<div class="panel-body">
					<br />
					
					
					<input type="hidden" name="reportCode" value="401">
				
				<div class="col-sm-12 form-group">
								<div class="col-sm-6 form-group">
									<label class="col-md-4 col-sm-3 control-label">Year <span class="required">*</span> </label>
									<div class="col-md-8">
										<input autofocus type="text" class="form-control" required
											name="year" id="year" placeholder="yyyy"
											value="${salaryProcessModel.year}" maxlength="4"
											onkeypress="return isNumberKey(event)" />

										<div id="erryear"></div>

									</div>
								</div>

								<div class="col-sm-6 form-group">
									<label class="col-md-4 col-sm-3 control-label">Month <span class="required">*</span> </label>
									<div class="col-md-8">
										<select data-plugin-selectTwo class="form-control"
											name="month" id="month" required
											onkeypress="goToNext(event,'search')">
											<option value="">Please Select One</option>
											<option
												<c:if test="${salaryProcessModel.month == '01'}"> selected </c:if>
												value="01">January</option>
											<option
												<c:if test="${salaryProcessModel.month == '02'}"> selected </c:if>
												value="02">February</option>
											<option
												<c:if test="${salaryProcessModel.month == '03'}"> selected </c:if>
												value="03">March</option>
											<option
												<c:if test="${salaryProcessModel.month == '04'}"> selected </c:if>
												value="04">April</option>
											<option
												<c:if test="${salaryProcessModel.month == '05'}"> selected </c:if>
												value="05">May</option>
											<option
												<c:if test="${salaryProcessModel.month == '06'}"> selected </c:if>
												value="06">June</option>
											<option
												<c:if test="${salaryProcessModel.month == '07'}"> selected </c:if>
												value="07">July</option>
											<option
												<c:if test="${salaryProcessModel.month == '08'}"> selected </c:if>
												value="08">August</option>
											<option
												<c:if test="${salaryProcessModel.month == '09'}"> selected </c:if>
												value="09">September</option>
											<option
												<c:if test="${salaryProcessModel.month == '10'}"> selected </c:if>
												value="10">October</option>
											<option
												<c:if test="${salaryProcessModel.month == '11'}"> selected </c:if>
												value="11">November</option>
											<option
												<c:if test="${salaryProcessModel.month == '12'}"> selected </c:if>
												value="12">December</option>
										</select>
									</div>
								</div>
							</div>
				
				
					
				</div>

				<footer class="panel-footer">
					<div class="row">
						<div class="col-sm-offset-5">
							<button class="btn btn-primary" type="submit">View</button>
						</div>

					</div>
				</footer>

			</section>

		</form>
		<!-- end: page -->

	</div>
</div>
<script>

function isNumberKey(evt) {
    var charCode = (evt.which) ? evt.which : event.keyCode;
    var errid = "err"+evt.target.id;
    var msg = '<label class="error" for="'+evt.target.id+'">Please enter a valid number.</label>';
    if (charCode != 46 && charCode > 31 && (charCode < 48 || charCode > 57)) {
    	$("#"+errid).html(msg);
        return false;
    } else {
    	$("#"+errid).html('');
        return true;
    }      
}


</script>

<script
	src="//ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
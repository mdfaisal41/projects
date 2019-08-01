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
			<li><span>Point of Sale &nbsp;</span></li>
			<li><span>Order Management &nbsp;</span></li>
		</ol>
	</div>
</header>


<!-- start: page -->

<div class="row">

	<div class="col-lg-12">

		<!-- message -->

		<div id="msg"></div>


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


		<form class="form-horizontal form-bordered" id="form"
			autocomplete="off" action="/pos/pointOfSale/saveOrder" method="post">

			<%-- <div class="form-group col-md-12">
				<input type="hidden" name="${_csrf.parameterName}"
					value="${_csrf.token}" /> <input type="hidden"
					id="encEffortTrackingId" name="encEffortTrackingId"
					value="${taskTrackingInfo.encEffortTrackingId}" />
			</div> --%>

			<!-- start: page -->
			<section class="panel panel-featured panel-featured-primary">
				<header class="panel-heading">
					<h2 class="panel-title">Order Management</h2>
				</header>
				<div class="panel-body">
					<br />

					<div class="form-group col-md-12">
						<div class="col-sm-6 form-group">
							<label class="control-label col-md-5 col-sm-5 col-xs-12">Item
								Name</label>
							<div class="col-md-7 col-sm-7 col-xs-12">
								<select data-plugin-selectTwo class="form-control" id="itemId"
									name="itemId" required>
									<option value="">Select One</option>
									<c:forEach items="${itemList}" var="list">
										<option value="${list.itemId}">${list.itemName}</option>
									</c:forEach>
								</select>
							</div>
						</div>

						<div class="col-sm-6 form-group">
							<label class="col-md-5 control-label" for="nationalityId">Quantity</label>
							<div class="col-md-6">
								<input type="text" required class="form-control mandatory"
									onblur="orderAddList()" name="quantity" id="quantity"
									onkeydown="return isNumberKey(event)"
									value="${pointOfSale.quantity}">

								<div id="errquantity"></div>

							</div>

						</div>

					</div>


					<div class="form-group col-md-12">

						<div class="col-sm-6 form-group">
							<label class="control-label col-md-5 col-sm-5 col-xs-12"
								for="tableNo">Table No</label>
							<div class="col-md-7 col-sm-7 col-xs-12">
								<input type="text" required class="form-control mandatory"
									name="tableNo" id="tableNo"
									onkeydown="return isNumberKey(event)"
									value="${pointOfSale.tableNo}">

								<div id="errtableNo"></div>

							</div>
						</div>

					</div>


				</div>

				<%-- <c:if test="${pointOfSale.encOrderId != null}">
					<p style="font-size: 30px; color: red; text-align: center;">Receive
						Amount : ${pointOfSale.totalPrice}</p>
				</c:if> --%>



				<div class="panel-body" id="">
					<input type="hidden" id="sl" value="${orderSl}"> <br />
					<div style="overflow: scroll; max-width: 100%;">
						<input type='hidden' id='orderIndex' value="${orderInfoListIndex}" />
						<table
							class="table table-bordered table-striped table-condensed mb-none"
							id="dataTable">
							<thead>
								<tr>
									<th>Serial No</th>
									<th>Item Name</th>
									<th>Item Price</th>
									<th>Quantity</th>
									<th>Sub Total</th>
									<th>Action</th>
									<c:if test="${!empty orderInfoList}">
										<th>Edit</th>
									</c:if>
								</tr>
							</thead>
							<tbody id="orderInfoList">
								<c:if test="${!empty orderInfoList}">
									<%
										int j = 0;
									%>
									<c:forEach items="${orderInfoList}" var="list">
										<tr style="cursor: pointer">
											<td id="sl">${orderSl}</td>
											<td style="border-style: inset;">${list.itemName}<input
												type='hidden' id='itemId_${list.itemId}'
												value='${list.itemId}'
												name='orderModelList[<% out.print(j);%>].itemId'> <input
												type='hidden' id='encOrderId_${list.encOrderId}'
												value='${list.encOrderId}'
												name='orderModelList[<% out.print(j);%>].encOrderId'>
											</td>
											<td style="border-style: inset;">${list.itemPrice}<input
												type='hidden' id='itemPrice_${list.itemPrice}'
												value='${list.itemPrice}'
												name='orderModelList[<% out.print(j);%>].itemPrice'>
											</td>
											<td style="border-style: inset;">${list.quantity}<input
												type='hidden' id='quantity_${list.quantity}'
												value='${list.quantity}'
												name='orderModelList[<% out.print(j);%>].quantity'>
											</td>
											<td style="border-style: inset;">${list.subTotal}<input
												type='hidden' id='subTotal_${list.subTotal}'
												value='${list.subTotal}'
												name='orderModelList[<% out.print(j);%>].subTotal'>
											</td>
											<td style="border-style: inset;"><c:if
													test="${!empty list.encOrderId }">
													<a href='#' onclick='removetr(this)'><button
															class="btn btn-xs btn-primary" type="button">&times;
														</button></a>
												</c:if></td>
											<td style="border-style: inset;"><c:if
													test="${!empty list.encOrderId }">
													<a href='#' onclick='removetr(this)'><button
															type="button" class="btn btn-primary"
															onclick="getOrderEditList('${list.encOrderId}')">
															<i class="fa fa-edit"></i>
														</button></a>
												</c:if></td>
										</tr>
										<%
											j = j + 1;
										%>
									</c:forEach>
								</c:if>

								<c:if test="${! empty orderEditListNotFound}">
									<tr id="orderEditListNotFound">
										<td colspan="8"><p>${orderEditListNotFound}</p></td>
									</tr>
								</c:if>
							</tbody>

						</table>
						<p id="totalPrice" style="font-size: 20px"></p>
					</div>
				</div>


				<footer class="panel-footer">
					<div class="row">
						<div class="col-sm-offset-5">
							<button class="btn btn-primary" type="submit">Submit</button>
							<a href="/pos/pointOfSale/orderManagement"><button
									class="btn btn-default" type="button" role="button">Clear</button></a>
							<%-- <c:if test="${pointOfSale.encOrderId != null}">
								<button class="btn btn-primary" type="button"
									onclick="orderFinalizeReport('${pointOfSale.encOrderId}')">Print</button>
							</c:if> --%>
							<a href="/pos/pointOfSale/orderReprint"><button
									class="btn btn-primary" type="button">Re-Print</button></a>
						</div>

					</div>
				</footer>

			</section>

		</form>
		<!-- end: page -->

	</div>
</div>


<div class="row">
	<div class="col-lg-12">
		<form class="form-horizontal form-bordered" id="form"
			autocomplete="off" action="/pos/pointOfSale/saveOrder" method="post">

			<!-- start: page -->
			<section class="panel panel-featured panel-featured-primary">
				<header class="panel-heading">
					<h2 class="panel-title">Pending Order List</h2>
				</header>

				<div class="panel-body">
					<div class="row">
						<div class="table-responsive">
							<table id="datatable-default"
								class="table table-striped table-condensed table-hover mb-none">
								<thead>
									<tr>
										<th>#</th>
										<th>Order ID</th>
										<th>Table No</th>
										<th>Time</th>
										<th>Taken By</th>
										<th>View</th>
										<th>Edit</th>
										<th>Token</th>
										<th>Receipt</th>
										<th>Cancel</th>
									</tr>
								</thead>
								<tbody>
									<%
										int i = 1;
									%>
									<c:if test="${!empty pendingOrderList}">

										<c:forEach items="${pendingOrderList}" var="list">
											<tr style="cursor: pointer">
												<td>
													<%
														out.print(i);
																i++;
													%>
												</td>
												<td>${list.orderId}</td>
												<td>${list.tableNo}</td>
												<td>${list.updateDate}</td>
												<td>${list.updateBy}</td>

												<td><button
														onclick="getPendingOrderInfoList('${list.encOrderId}')"
														class="btn btn-mg btn-primary modal-with-move-anim"
														href="#modalOrderInfoList" type="button">
														<i class="fa fa-eye"></i>
													</button></td>
												<td>
												<c:if test="${list.finalizedYn == 'N'}">
														<button type="button" class="btn btn-primary"
															onclick="getOrderEditList('${list.encOrderId}')">
															<i class="fa fa-edit"></i>
														</button>
													</c:if>
												</td>
												<td>
												<c:if test="${list.finalizedYn == 'N'}">
														<button type="button" class="btn btn-primary"
															onclick="orderFinalizeReport('${pointOfSale.encOrderId}')">
															<i class="fa fa-print"></i>
														</button>
													</c:if>
												</td>
												<td>
												<c:choose>
														<c:when test="${list.finalizedYn == 'N'}">
															<button onclick="getOrderTotalAmount('${list.encOrderId}')"
															class="btn btn-mg btn-success modal-with-move-anim" 
															href="#modalMoneyReceiptInfo"
															type="button">Finalize</button>
														</c:when>
														<c:otherwise>
															<button
																onclick="getOrderTotalAmount('${list.encOrderId}')"
																class="btn btn-mg btn-success modal-with-move-anim"
																href="#modalMoneyReceiptInfo" type="button">
																<i class="fa fa-print"></i>
															</button>
														</c:otherwise>
													</c:choose>

												</td>
												<td><button class="btn btn-xs btn-primary"
														onclick="cancelOrder('${list.encOrderId}')" type="button">&times;</button></td>
											</tr>
										</c:forEach>
									</c:if>
									<c:if test="${! empty pendingOrderListNotFound}">
										<tr>
											<td colspan="10"><p>${pendingOrderListNotFound}</p></td>
										</tr>
									</c:if>
								</tbody>
							</table>
						</div>
					</div>
				</div>


				<footer class="panel-footer">
					<div class="row"></div>
				</footer>

			</section>

		</form>
		<!-- end: page -->

	</div>
</div>

<!----------------------------------------------------- Start Registration Id Modal ----------------------------------------------------------------------->


<div id="modalMoneyReceiptInfo"
	class="zoom-anim-dialog modal-block modal-block-sm mfp-hide">
	<!-- class="zoom-anim-dialog modal-block modal-block-sm mfp-hide"> -->
	<!-- class="mb-xs mt-xs mr-xs modal-sizes btn btn-default"  -->
	<form class="" method="post" autocomplete="off"
			action="/pos/pointOfSale/saveOrderFinalize" id="form"
			onkeypress="return event.keyCode != 13;">
	<section class="panel">
		<header class="panel-heading">
			<button class="close modal-dismiss" type="button"
				id="btnModalEmpListClose">
				<span aria-hidden="true">&times;</span> <span class="sr-only">Close</span>
			</button>
			<h2 class="panel-title">Customer Payment</h2>
		</header>

		<div class="panel-body">
			<!-- <div class="col-md-12"> -->

			<div class="form-group col-md-12">
				<label class="col-md-6 control-label" for="personId">
					Payable Amount<span class="required"> * </span>
				</label>
				<div class="col-md-6">
					<!-- <p id="orderTotalAmount"></p> -->
					<input type="text" required class="form-control" readonly
						name="orderTotalAmount" id="orderTotalAmount" required
						value="${pointOfSale.orderTotalAmount}" />
				</div>
			</div>
			<div class="form-group col-md-12">
				<label class="col-md-6 control-label" for="receivedAmount">Received
					Amount</label>
				<div class="col-md-6">
					<input type="text" class="form-control mandatory"
						name="receivedAmount" id="receivedAmount" required
						value=""
						onkeypress="return isNumberKey(event)" onkeyup="getChangeAmount()" />
					<div id="errorderPrice"></div>
				</div>
			</div>
			<!-- </div> -->
			<!-- <div class="col-md-12"> -->
			<div class="form-group col-md-12">
				<label class="col-md-6 control-label" for="personId">Change
					Amount <span class="required"> * </span>
				</label>
				<div class="col-md-6">
					<input type="text" required class="form-control" readonly
						name="changeAmount" id="changeAmount"
						value=""
						onkeypress="return isNumberKey(event)" />

					<div id="errdiscountAmount"></div>
				</div>
			</div>
			<div class="form-group col-md-12">
				<label class="col-md-6 control-label"> Cash Pay Amount <span
					class="required"> * </span>
				</label>
				<div class="col-md-6">
					<input type="text" required class="form-control mandatory"
						name="cashPayAmount" id="cashPayAmount" required
						value="" onkeyup="getCardPayAmount()"
						onkeypress="return isNumberKey(event)" />

					<div id="errdiscountAmount"></div>
				</div>
			</div>
			<div class="form-group col-md-12">
				<label class="col-md-6 control-label"> Card Pay Amount <span
					class="required"> * </span>
				</label>
				<div class="col-md-6">
					<input type="text" required class="form-control mandatory"
						name="cardPayAmount" id="cardPayAmount" required
						value="" onkeyup="getCashPayAmount()"
						onkeypress="return isNumberKey(event)" />

					<div id="errdiscountAmount"></div>
				</div>
			</div>

			<div class="form-group col-md-12">
				<label class="col-md-6 control-label"> Discount Amount <span
					class="required"> * </span>
				</label>
				<div class="col-md-6">
					<input type="text" required class="form-control mandatory"
						name="discountAmount" id="discountAmount" required
						value="" onkeyup="getDiscountAmount()"
						onkeypress="return isNumberKey(event)" />

					<div id="errdiscountAmount"></div>
				</div>
			</div>
			<div class="form-group col-md-12">
				<label class="col-md-6 control-label" for="personId"> Net
					Payable <span class="required"> * </span>
				</label>
				<div class="col-md-6">
					<!-- <p id="orderTotalAmount"></p> -->
					<input type="text" required class="form-control" readonly
						name="netPayableAmount" id="netPayableAmount"
						value="${pointOfSale.netPayableAmount}" />
				</div>
			</div>
			<!-- </div> -->
		</div>
		<footer class="panel-footer">
			<div class="row">
				<div class="col-sm-offset-4">
					<button class="btn btn-primary" type="submit">Submit</button>
					<a href="/pos/pointOfSale/orderManagement"><button
							class="btn btn-default" type="button" role="button">Cancel</button></a>
				</div>
			</div>
		</footer>
	</section>
	</form>
</div>

<!-------------------------------- Ending Modal --------------------------------------------->


<!-------------------------------- start modal makers country ------------------------------------------>

<div id="modalOrderInfoList"
	class="zoom-anim-dialog modal-block modal-block-lg mfp-hide">
	<!-- class="modal-block modal-block-sm mfp-hide"> -->

	<section class="panel">
		<header class="panel-heading">
			<button class="close modal-dismiss" type="button"
				id="makersCountryListClose">
				<span aria-hidden="true">&times;</span> <span class="sr-only">Close</span>
			</button>
			<h2 class="panel-title">Pending Order Info List</h2>
		</header>

		<div class="panel-body">

			<div class="row">
				<div class="col-md-12">
					<div style="overflow: scroll; max-height: 300px;">

						<table
							class="table table-striped table-condensed table-hover mb-none ">
							<thead>
								<tr>
									<th>#</th>
									<th>Item Name</th>
									<th>Quantity</th>
									<th>Item Price</th>
									<th>Sub Total</th>
								</tr>
							</thead>
							<tbody id="pendingOrderInfoList" style="border-style: inset;">
							</tbody>

						</table>
					</div>
				</div>
			</div>
		</div>
	</section>
</div>


<!-------------------------------- end modal makers country ------------------------------------------>


<%-- <input type="hidden" name="updateDate" id="updateDate"
	value="${inventory.updateDate}"> --%>

<script>

function getCardPayAmount () {
	var cashPayAmount = $("#cashPayAmount").val();
	var cardPayAmount = $("#cardPayAmount").val();
	var orderTotalAmount = $("#orderTotalAmount").val();
	
	if (cashPayAmount != '' && cashPayAmount > 0) {
		var newCardPayAmount = orderTotalAmount - cashPayAmount;
		$("#cardPayAmount").val(newCardPayAmount);
	} else {
		$("#cardPayAmount").val('0');
	}
}

function getCashPayAmount () {
	var cardPayAmount = $("#cardPayAmount").val();
	var cashPayAmount = $("#cashPayAmount").val();
	var orderTotalAmount = $("#orderTotalAmount").val();
	
	if (cardPayAmount != '' && cardPayAmount > 0) {
		var newCashPayAmount = orderTotalAmount - cardPayAmount;
		//alert(newCardPayAmount);
		$("#cashPayAmount").val(newCashPayAmount);
		
	} else {
		$("#cashPayAmount").val('0');
	}
}



function getChangeAmount () {
	var receivedAmount = $("#receivedAmount").val();
	//var changeAmount = $("#changeAmount").val();
	var orderTotalAmount = $("#orderTotalAmount").val();
		if (receivedAmount != '' && receivedAmount > 0) {
			var changeAmount = receivedAmount - orderTotalAmount;
			$("#changeAmount").val(changeAmount);
		} else {
			$("#changeAmount").val('0');
		}
}

function getDiscountAmount () {
	var discountAmount = $("#discountAmount").val();
	var orderTotalAmount = $("#orderTotalAmount").val();
		if (discountAmount != '' && discountAmount > 0) {
			var newOrderTotalAmount = orderTotalAmount - discountAmount;
			//alert(newOrderTotalAmount);
			$("#netPayableAmount").val(newOrderTotalAmount);
		} else {
			//alert(orderTotalAmount);
			$("#netPayableAmount").val(orderTotalAmount);
		}
}

function getOrderTotalAmount(encOrderId) {
	var link = "/pos/pointOfSale/getOrderTotalAmount";

	$
			.ajax({
				type : "POST",
				url : link,
				data : "encOrderId=" + encodeURIComponent(encOrderId),
				async : true,

				success : function(data) {
					//alert(data.orderTotalAmount);
					$("#orderTotalAmount").val(data.orderTotalAmount);
					$("#netPayableAmount").val(data.orderTotalAmount);
				}
			});
};


function getOrderEditList(encOrderId) {
	//alert(encStudentId);
	var url = "/pos/pointOfSale/getOrderEditList?encOrderId="
			+ encodeURIComponent(encOrderId);
	//alert(url);
	window.location = url;
}

/* function getOrderEditList(encOrderId) {

	if (!encOrderId == '') {

		var link = "/pos/pointOfSale/getOrderEditList";

		$.ajax({
			type : "POST",
			url : link,
			data : "encOrderId=" + encodeURIComponent(encOrderId),
			async : true,

			success : function(data) {
				 //alert(data);
				$("#orderInfoList").html(data);
			},

			error : function(data) {
				alert('Error!!!')
			}
		});
	}
};
 */

function cancelOrder(encOrderId) {
	//alert(encOrderId);
	var conf = confirm("Do You Want To Cancel This Order ? ");
	
	if (conf) {
		var url = "/pos/pointOfSale/cancelOrder?encOrderId=" + encodeURIComponent(encOrderId);
		//alert(url);
		window.location = url;
	}
}

function removeTaggedVehicle(encTagId) {
	var conf = confirm("Do you want to untag this vehicle ? ");

	if (conf) {
		var java_base_url = "<%out.print(session.getAttribute("sess_baseurl"));%>";
		var link = java_base_url+"/v/registration/removeTaggedVehicle?encTagId=" + encodeURIComponent(encTagId);
		window.location = link;
	}
};


function getPendingOrderInfoList(encOrderId) {

	if (!encOrderId == '') {

		var link = "/pos/pointOfSale/getPendingOrderInfoList";

		$.ajax({
			type : "POST",
			url : link,
			data : "encOrderId=" + encodeURIComponent(encOrderId),
			async : true,

			success : function(data) {
				 //alert(data.orderTotalAmount)
				$("#pendingOrderInfoList").html(data);
				//$("#orderTotalAmount").val(data.orderTotalAmount);
			},

			error : function(data) {
				alert('Error!!!')
			}
		});
	}
};

function isNumberKey(evt) {
    var charCode = (evt.which) ? evt.which : event.keyCode;
    var errid = "err"+evt.target.id;
    var msg = '<label class="error" for="'+evt.target.id+'">Please enter a valid number.</label>';
    if (charCode != 46 && charCode > 31 && (charCode < 48 || charCode > 57) && (charCode < 96 || charCode > 105)) {
    	$("#"+errid).html(msg);
        return false;
    } else {
    	$("#"+errid).html('');
        return true;
    }      
}

  function orderFinalizeReport(encOrderId) {
	var url = "/pos/reports/cashReceipt?encOrderId=" + encodeURIComponent(encOrderId) + "&reportCode=" + "101";
	window.open(url, '_blank');

};  



	/* function orderFinalizeReport(encOrderId) {
		alert('hello');
		var link = "/pos/reports/viewreport";
		$.ajax({
			type : "POST",
			url : link,
			data : "encOrderId=" + encodeURIComponent(encOrderId)
					+ "&reportCode=" + "101",
			async : true,
			beforeSend : function(xhr) {
				// here it is
				//xhr.setRequestHeader(header, token);
			},
			success : function(data) {
				window.open(url, '_blank');
			},
			error : function(data) {
				alert('Error!!!');
			}
		});
	}; */

<%-- 	function printLearner(encServiceRequestNo) {
		var java_base_url = "<%out.print(session.getAttribute("sess_baseurl"));%>";
		var url = java_base_url + "/v/drivingLicense/printLearner?encServiceRequestNo=" + encodeURIComponent(encServiceRequestNo);
		window.open(url, '_blank');

	} --%>

	function orderAddList() {
		var itemId = $("#itemId").val();
		var quantity = $("#quantity").val();
		var trIndex = $("#orderIndex").val();
		var content = "";
		var myElem = document.getElementById("encItemId_" + itemId);
		var totalPrice = "";
		//alert(quantity)

		var sl = $("#sl").val();
		if (sl == '') {
			sl = 0;
		}
		sl = parseInt(sl) + 1;

		if (itemId == '') {
			content = "<div class='alert alert-danger'>"
					+ "<button class='close' aria-hidden='true' data-dismiss='alert' "
			+ "type='button'>&times;</button>"
					+ "<strong>Select Item Name First !</strong>" + "</div>";

			$("#msg").html(content);
			//$("#itemId").focus();
		} else if (quantity == '') {
			content = "<div class='alert alert-danger'>"
					+ "<button class='close' aria-hidden='true' data-dismiss='alert' "
			+ "type='button'>&times;</button>"
					+ "<strong>Select Quantity !</strong>" + "</div>";

			$("#msg").html(content);
			//$("#quantity").focus();
		} else {
			
			
			/* if (trIndex > 10) {
				content = "<div class='alert alert-danger'>"
					+ "<button class='close' aria-hidden='true' data-dismiss='alert' "
			+ "type='button'>&times;</button>"
					+ "<strong>Maximum 10 Orders Can Be Generated In One Receipt !!</strong>" + "</div>"

			$("#msg").html(content);
			} else { */

			if (trIndex == '') {
				var trIndexResult = '0';
			} else {
				var trIndexResult = parseInt(trIndex) + 1;
			}

			var link = "/pos/pointOfSale/getOrderInfoSList";
			$
					.ajax({
						type : "POST",
						url : link,
						data : "itemId=" + itemId + "&quantity=" + quantity,

						async : true,

						success : function(data) {
							$("#orderInfoListNotFound").remove();
							if (data.mCode == '1111') {
								//alert(data.orderPrice);
								var subTotal = data.itemPrice * quantity;
								//alert(subTotal);
								var content = "<tr>"
										+ "<td style='border-style: inset;'>"
										+ sl
										+ "</td>"
										+ "<td style='border-style: inset;'><input type='hidden' id='itemId_" + itemId+ "'" + " name='orderModelList["+ trIndexResult+ "].itemId' value='"+ itemId+ "' />"
										+ data.itemName
										+ "</td>"
										+ "</td>"
										+ "<td style='border-style: inset;'><input type='hidden' id='itemPrice_" + data.itemPrice+ "'" + " name='orderModelList["+ trIndexResult+ "].itemPrice' value='"+ data.itemPrice+ "' />"
										+ data.itemPrice
										+ "</td>"
										+ "<td style='border-style: inset;'><input type='hidden' id='quantity_"+ quantity+ "'"+ " name='orderModelList["+ trIndexResult+ "].quantity' value='"+ quantity+ "'/>"
										+ quantity
										
										+ "<td style='border-style: inset;'><input type='hidden' id='subTotal_" + subTotal+ "'" + " name='orderModelList["+ trIndexResult+ "].subTotal' value='"+ subTotal+ "' />"
										+ subTotal
										+ "</td>"
										+ "<td style='border-style: inset;'><a href='#' onclick='removetr(this)'><button class='btn btn-xs btn-primary' type='button'>&times;</button></a></td>"
										+ "</tr>";
								// alert(content);

								$("#msg").html('');
								$("#orderInfoList").append(content);
								$("#orderIndex").val(trIndexResult);
								//$("#receiptId").val('');
								$("#sl").val(sl);

								totalPrice = data.orderPrice;
								//alert(totalPrice);
								$("#totalPrice").val(totalPrice);

							} else {
								content = "<div class='alert alert-danger'>"
										+ "<button class='close' aria-hidden='true' data-dismiss='alert' "
										+ "type='button'>&times;</button>"
										+ "<strong>" + data.message
										+ "</strong>" + "</div>"

								$("#msg").html(content);
							}
						},
						error : function(data) {
							alert('Error!!!')
						}
					//}
					});
			$("#orderIndex").val(trIndexResult);
		}

	}

	function removetr(e) {
		var whichtr = $(e).closest("tr");
		whichtr.remove();
	};

	function receiptAddList() {

		var receiptId = $("#receiptId").val();
		var trIndex = $("#receiptIndex").val();
		var content = "";
		var myElem = document.getElementById("encReceiptId_" + receiptId);

		if (receiptId != '') {
			if (!isNaN(receiptId)) {

				if (trIndex == '') {
					var trIndexResult = '0';
				} else {
					var trIndexResult = parseInt(trIndex) + 1;
				}

				if (myElem == null) {

					var link = "/brtais/vdis/registration/getReceiptInfoSList";
					$
							.ajax({
								type : "POST",
								url : link,
								data : "receiptId=" + receiptId,

								async : true,

								success : function(data) {
									$("#receiptInfoListNotFound").remove();
									if (data.mCode == '1111') {
										// alert("abcd");
										var content = "<tr>"
												+ "<td style='border-style: inset;'><input type='hidden' id='purpose_"
											+ data.receiptId
											+ "'"
											+ " name='receiptList["
											+ trIndexResult
											+ "].purpose' value='"
											+ data.purpose
											+ "' />"
												+ data.purpose
												+ "</td>"
												+ "<td style='border-style: inset;'><input type='hidden' id='encReceiptId_"
											+ data.receiptId
											+ "'"
											+ " name='receiptList["
											+ trIndexResult
											+ "].encReceiptId' value='"
											+ data.encReceiptId
											+ "'/>"
												+ " <input type='hidden' id='receiptId_"
											+ data.receiptId
											+ "'"
											+ " name='receiptList["
											+ trIndexResult
											+ "].receiptId' value='"
											+ data.receiptId
											+ "'/>"
												+ data.receiptId
												+ "</td>"

												+ "<td style='border-style: inset;'><input type='hidden' id='amountWithoutVat_"+data.receiptId+"'"
											+" name='receiptList["+trIndexResult+"].amountWithoutVat' value='"+data.amountWithoutVat+"'/>"

												+ data.amountWithoutVat
												+ "</td>"
												+ "<td style='border-style: inset;'><input type='hidden' id='vatAmount_"
											+ data.receiptId
											+ "'"
											+ " name='receiptList["
											+ trIndexResult
											+ "].vatAmount' value='"
											+ data.vatAmount
											+ "'/>"
												+ data.vatAmount
												+ "</td>"
												+ "<td style='border-style: inset;'><input type='hidden' id='totalAmount_"
											+ data.receiptId
											+ "'"
											+ " name='receiptList["
											+ trIndexResult
											+ "].totalAmount' value='"
											+ data.totalAmount
											+ "'/>"
												+ data.totalAmount
												+ "</td>"/* + "<td>" */
												+ "<td style='border-style: inset;'><a href='#' onclick='removetr(this)'><button class='btn btn-xs btn-primary' type='button'>&times;</button></a></td>"
												+ "</tr>";
										// alert(content);

										$("#msg").html('');
										$("#receiptInfoList").append(content);
										$("#receiptIndex").val(trIndexResult);
										$("#receiptId").val('');

									} else {
										content = "<div class='alert alert-danger'>"
												+ "<button class='close' aria-hidden='true' data-dismiss='alert' "
											+ "type='button'>&times;</button>"
												+ "<strong>"
												+ data.message
												+ "</strong>" + "</div>"

										$("#msg").html(content);
									}
								},
								error : function(data) {
									alert('Error!!!')
								}

							});
					$("#receiptIndex").val(trIndexResult);
				} else {
					content = "<div class='alert alert-danger'>"
							+ "<button class='close' aria-hidden='true' data-dismiss='alert' "
						+ "type='button'>&times;</button>"
							+ "<strong>Select another Receipt ID... </strong>"
							+ "</div>"

					$("#msg").html(content);
					$("#receiptId").val('');
					$("#receiptId").focus();
				}

			} else {
				content = "<div class='alert alert-danger'>"
						+ "<button class='close' aria-hidden='true' data-dismiss='alert' "
					+ "type='button'>&times;</button>"
						+ "<strong>Invalid Reciept Id... </strong>" + "</div>"

				$("#msg").html(content);
				$("#receiptId").val('');
				$("#receiptId").focus();
			}

		}
	}

	/* function isNumberKey(evt) {
	 var charCode = (evt.which) ? evt.which : event.keyCode;
	 var errid = "err"+evt.target.id;
	 var msg = '<label class="error" for="'+evt.target.id+'">Please enter a valid number.</label>';
	 if (charCode != 46 && charCode > 31 && (charCode < 48 || charCode > 57) && (charCode < 96 || charCode > 105)) {
	 $("#"+errid).html(msg);
	 return false;
	 } else {
	 $("#"+errid).html('');
	 return true;
	 }      
	 } */

	function totalEffortCalc() {
		var min = parseFloat(0);
		var totalHour = parseInt(0);
		var totalMin = parseInt(0);
		var strH = '';
		var strM = '';

		$('input[id^="totalHour_"]').each(function() {
			console.log($(this).val());
			var hm = $(this).val().split(':');
			totalHour = totalHour + parseInt(hm[0]);
			totalMin = totalMin + parseInt(hm[1]);
		});

		if (totalMin > 59) {
			min = totalMin / 60;
			totalHour = totalHour
					+ Math.round(parseInt(min.toString().split('.')[0]));
			totalMin = Math.round(parseFloat('0.'
					+ min.toString().split('.')[1]) * 60);
		}

		if (totalHour.toString().length < 1) {
			strH = '00';
		} else if (totalHour.toString().length == 1) {
			strH = '0' + totalHour.toString();
		} else {
			strH = totalHour.toString();
		}
		if (totalMin.toString().length < 1) {
			strM = '00';
		} else if (totalMin.toString().length == 1) {
			strM = '0' + totalMin.toString();
		} else {
			strM = totalMin.toString();
		}
		$("#totalefforthour").html(strH + ':' + strM + ' Hour');
	}

	function newData() {

		$("#newdatarow").show();
		/* $("#projectId").val('');
		$("#taskTypeId").val(''); */
		$("#taskDesc").val('');
		//$("#startTime").val('');
		//$("#endTime").val('');
		$("#totalHour_").val('00:00');
		$("#noOfDefect").val('');
		$("#defectDesc").val('');

	}

	function cancelnewdata() {

		$("#newdatarow").hide();
		//$("#taskDate").val('');
		//$("#projectId").val('');
		//$("#taskTypeId").val('');
		$("#taskDesc").val('');
		//$("#startTime").val('');
		//$("#endTime").val('');
		$("#totalHour_").val('00:00');
		$("#noOfDefect").val('');
		$("#defectDesc").val('');
	}

	function getTimeDiff() {
		var taskDate = $("#taskDate").val();
		var startTime = $("#startTime").val();
		var endTime = $("#endTime").val();

		if (taskDate != '' && startTime != '' && endTime != '') {

			var token = $("meta[name='_csrf']").attr("content");
			var header = $("meta[name='_csrf_header']").attr("content");

			var link = "/cbms/effortTracking/getTimeDifferent";

			var totalhour = parseFloat(0);

			$('input[id^="totalHour_"]').each(function() {
				totalhour = totalhour + parseFloat($(this).val());
			});

			//alert(totalhour);

			$.ajax({
				type : "POST",
				url : link,
				data : "taskDate=" + encodeURIComponent(taskDate)
						+ "&startTime=" + startTime + "&endTime=" + endTime,
				async : true,
				beforeSend : function(xhr) {
					// here it is
					xhr.setRequestHeader(header, token);
				},
				success : function(data) {
					var listTotalHour = parseFloat(0);
					if (data.totalHour != '') {
						listTotalHour = parseFloat(data.totalHour);
					}

					$("#totalHour_").val(data.totalHour);
					/*
					totalhour = totalhour + listTotalHour;
					//alert(totalhour);
					var h = 'hour';
					if (totalhour > 1) {
						h = 'hours';
					}
					$("#totalefforthour").html(totalhour.toFixed(2) + ' ' + h);
					 */
					totalEffortCalc();
				},
				error : function(data) {
					alert('Error!!!');
				}
			});
		}
	}

	function getTimeDiffEdit(effortId) {
		var taskDate = $("#taskDate").val();
		var startTime = $("#startTime_" + effortId).val();
		var endTime = $("#endTime_" + effortId).val();

		if (taskDate != '' && startTime != '' && endTime != '') {

			var token = $("meta[name='_csrf']").attr("content");
			var header = $("meta[name='_csrf_header']").attr("content");

			var link = "/cbms/effortTracking/getTimeDifferent";

			var totalhour = parseFloat(0);

			$('input[id^="totalHour_"]').each(function() {
				totalhour = totalhour + parseFloat($(this).val());
			});

			//alert(totalhour);

			$.ajax({
				type : "POST",
				url : link,
				data : "taskDate=" + encodeURIComponent(taskDate)
						+ "&startTime=" + startTime + "&endTime=" + endTime,
				async : true,
				beforeSend : function(xhr) {
					// here it is
					xhr.setRequestHeader(header, token);
				},
				success : function(data) {
					var listTotalHour = parseFloat(0);
					if (data.totalHour != '') {
						listTotalHour = parseFloat(data.totalHour);
					}
					$("#totalHour_" + effortId).val(data.totalHour);
					/*
					totalhour = totalhour + listTotalHour;
					//alert(totalhour);
					var h = 'hour';
					if (totalhour > 1) {
						h = 'hours';
					}
					$("#totalefforthour").html(totalhour.toFixed(2) + ' ' + h);
					 */
					totalEffortCalc();
				},
				error : function(data) {
					alert('Error!!!');
				}
			});
		}
	}

	function saveNewData() {
		//alert('hello');
		var itemId = $("#itemId").val();
		var productId = $("#productId").val();
		var quantity = $("#quantity").val();
		var unitId = $("#unitId").val();
		//var price = $("#price").val();
		//var employeeId = $("#employeeId").val();
		var msg = "";

		//alert(productId);
		/*alert(productId);
		alert(quantity);
		alert(unitId);
		alert(price);
		alert(employeeId); */

		if (itemId == '') {
			alert("Please Select Item !!!");
		} else if (productId == '') {
			alert("Please Select Product !!!");
		} else if (quantity == '') {
			alert("Please Select Quantity !!!");
		} else if (unitId == '') {
			alert("Please Select Unit !!!");
		} else {
			//alert('HELLO');
			//var link = "/cbms/effortTracking/saveEffortTrackingInfo";
			var link = "/pos/pointOfSale/saveItemConfig";
			//alert(link);
			$
					.ajax({
						type : "POST",
						url : link,

						data : "itemId=" + itemId + "&productId=" + productId
								+ "&quantity=" + quantity + "&unitId=" + unitId,

						async : true,
						/* beforeSend : function(xhr) {
						// here it is
						xhr.setRequestHeader(header, token);
						},  */
						success : function(data) {
							//alert(data.mCode);
							if (data.mCode == '1111') {
								//cancelnewdata();
								//$("#updateDate").val(data.updateDate)
								getItemConfigList();

								msg = '<div class="alert alert-success">'
										+ '<button class="close" aria-hidden="true" data-dismiss="alert"'
						+'type="button">x</button>'
										+ '<strong>' + data.message
										+ '</strong></div>';
								$("#effortSearchButton").click();

							} else {

								msg = '<div class="alert alert-danger">'
										+ '<button class="close" aria-hidden="true" data-dismiss="alert"'
				+'type="button">x</button>'
										+ '<strong>' + data.message
										+ '</strong></div>';

							}
							//alert(msg);
							$("#msg").html(msg);
						},

						error : function(data) {
							alert('Error!!!')
						}
					});
		}
	};

	function getItemConfigList() {

		var itemId = $("#itemId").val();
		//var updateDate = $("#updateDate").val();

		//alert(inventoryTypeId);
		//alert(updateDate);
		if (itemId == '') {
			alert("Item Empty!!!")
		} else {

			var link = "/pos/pointOfSale/getItemConfigList";
			//alert(link);
			$.ajax({
				type : "POST",
				url : link,
				data : "itemId=" + itemId,
				async : true,
				success : function(data) {
					$("#itemConfigList").html(data);
					//totalPriceCalc();
					$("#itemConfigDownList").show();
				},

				error : function(data) {
					alert('Error!!!')
				}
			});
		}
	};

	function totalPriceCalc() {
		var inventoryTypeId = $("#inventoryTypeId").val();
		var updateDate = $("#updateDate").val();

		//alert(inventoryTypeId);
		//alert(updateDate);

		var link = "/pos/inventory/getPriceSum";
		//alert(link);
		$.ajax({
			type : "POST",
			url : link,
			data : "inventoryTypeId=" + inventoryTypeId + "&updateDate="
					+ updateDate,
			async : true,
			success : function(data) {
				//alert(data.priceSum);
				$("#priceSum").html(data.priceSum);
				//totalPriceCalc();
				//$("#inventoryDownList").show();
			},

			error : function(data) {
				alert('Error!!!')
			}
		});

	}

	/* function saveNewData() {
		//alert('hello');
		var inventoryTypeId = $("#inventoryTypeId").val();
		var productId = $("#productId").val();
		var quantity = $("#quantity").val();
		var unitId = $("#unitId").val();
		var price = $("#price").val();
		var employeeId = $("#employeeId").val();
		var msg = "";

		 alert(inventoryTypeId);
		alert(productId);
		alert(quantity);
		alert(unitId);
		alert(price);
		alert(employeeId);
		
		if (inventoryTypeId == '') {
			alert("Please Select Inventory Type !!!");
		} else {
			alert('HELLO');
			//var link = "/cbms/effortTracking/saveEffortTrackingInfo";
			var link = "/pos/inventory/saveStoreProduct";
			alert(link);
			$.ajax({type : "POST",
						url : link,

						
						data : "inventoryTypeId=" + inventoryTypeId 
						+ "&productId=" + productId 
						+ "&quantity=" + quantity 
						+ "&unitId=" + unitId
						+ "&price=" + price 
						+ "&employeeId=" + employeeId,

						async : true,

						success : function(data) {
							alert(data.mCode);
							if (data.mCode == '1111') {
								//cancelnewdata();
								//getEffortList();
								alert('HELLO');
								msg = '<div class="alert alert-success">'
										+ '<button class="close" aria-hidden="true" data-dismiss="alert"'
							+'type="button">x</button>'
										+ '<strong>' + data.message
										+ '</strong></div>';
								$("#effortSearchButton").click();

							} else {

								msg = '<div class="alert alert-danger">'
										+ '<button class="close" aria-hidden="true" data-dismiss="alert"'
					+'type="button">x</button>'
										+ '<strong>' + data.message
										+ '</strong></div>';

							}
							alert(msg);
							$("#msg").html(msg);
						},

						error : function(data) {
							alert('Error!!!')
						}
					});
		}
	} */

	function editOldData(itmId) {
		alert(itmId);
		var productId = $("#productId_" + itmId).show();
		productId.select2();
		$("#dataProductId_" + itmId).hide();

		var quantity = $("#quantity_" + itmId).show();
		//taskTypeId.select2();
		$("#dataQuantity_" + itmId).hide();

		var unitId = $("#unitId_" + itmId).show();
		unitId.select2();
		$("#dataUnitId_" + itmId).hide();

		/* $("#taskDesc_" + effortId).show();
		$("#dataTaskDesc_" + effortId).hide();

		var startTime = $("#startTime_" + effortId).show();
		startTime.timepicker();
		$("#dataStartTime_" + effortId).hide();

		var endTime = $("#endTime_" + effortId).show();
		endTime.timepicker();
		$("#dataEndTime_" + effortId).hide(); 

		$("#totalHour_" + effortId).show();
		$("#dataTotalHour_" + effortId).hide();

		$("#noOfDefect_" + effortId).show();
		$("#dataNoOfDefect_" + effortId).hide();

		$("#defectDesc_" + effortId).show();
		$("#dataDefectDesc_" + effortId).hide();*/

		$("#update_" + itmId).show();
		$("#clear_" + itmId).show();
		$("#edit_" + itmId).hide();
	};

	function cancelEditOldData(effortId) {

		$("#projectId_" + effortId).hide();
		$("#s2id_projectId_" + effortId).hide();
		$("#dataProjectId_" + effortId).show();

		$("#taskTypeId_" + effortId).hide();
		$("#s2id_taskTypeId_" + effortId).hide();
		$("#dataTaskTypeId_" + effortId).show();

		$("#taskDesc_" + effortId).hide();
		$("#dataTaskDesc_" + effortId).show();

		$("#startTime_" + effortId).hide();
		$("#dataStartTime_" + effortId).show();

		$("#endTime_" + effortId).hide();
		$("#dataEndTime_" + effortId).show();

		$("#totalHour_" + effortId).hide();
		$("#dataTotalHour_" + effortId).show();

		$("#noOfDefect_" + effortId).hide();
		$("#dataNoOfDefect_" + effortId).show();

		$("#defectDesc_" + effortId).hide();
		$("#dataDefectDesc_" + effortId).show();

		$("#update_" + effortId).hide();
		$("#clear_" + effortId).hide();
		$("#edit_" + effortId).show();
	};
	b

	function saveUpdateData(effortId) {

		var taskDate = $("#taskDate").val();
		var encEffortTrackingId = $("#encEffortTrackingId_" + effortId).val();
		var projectId = $("#projectId_" + effortId).val();
		var taskTypeId = $("#taskTypeId_" + effortId).val();
		var taskDesc = $("#taskDesc_" + effortId).val();
		var startTime = $("#startTime_" + effortId).val();
		var endTime = $("#endTime_" + effortId).val();
		var noOfDefect = $("#noOfDefect_" + effortId).val();
		var defectDesc = $("#defectDesc_" + effortId).val();
		var msg = "";

		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");

		if (projectId == '') {
			alert("Please Select Project !!!");
		} else {
			var link = "/cbms/effortTracking/saveEffortTrackingInfo";

			//alert(link);

			$
					.ajax({
						type : "POST",
						url : link,
						data : "encEffortTrackingId="
								+ encodeURIComponent(encEffortTrackingId)
								+ "&projectId=" + projectId + "&taskDate="
								+ encodeURIComponent(taskDate) + "&taskTypeId="
								+ taskTypeId + "&taskDesc="
								+ encodeURIComponent(taskDesc) + "&startTime="
								+ startTime + "&endTime=" + endTime
								+ "&noOfDefect=" + noOfDefect + "&defectDesc="
								+ encodeURIComponent(defectDesc),

						async : true,
						beforeSend : function(xhr) {
							// here it is
							xhr.setRequestHeader(header, token);
						},
						success : function(data) {

							if (data.messageCode == '1111') {
								cancelnewdata();
								getEffortList();

								msg = '<div class="alert alert-success">'
										+ '<button class="close" aria-hidden="true" data-dismiss="alert"'
							+'type="button">x</button>'
										+ '<strong>' + data.message
										+ '</strong></div>';
								$("#effortSearchButton").click();

							} else {

								msg = '<div class="alert alert-danger">'
										+ '<button class="close" aria-hidden="true" data-dismiss="alert"'
					+'type="button">x</button>'
										+ '<strong>' + data.message
										+ '</strong></div>';

							}
							//alert(msg);
							$("#msg").html(msg);
						},

						error : function(data) {
							alert('Error!!!')
						}
					});
		}
	}
</script>
<script
	src="//ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
<!-- end: page -->
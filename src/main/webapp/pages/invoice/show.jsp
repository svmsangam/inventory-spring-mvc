<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@include file="/pages/parts/header.jsp" %>
<%@include file="/pages/parts/sidebar.jsp" %>

<input type="hidden" value="${pageContext.request.contextPath}" id="page">
<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
    <section class="content invoice">

        <c:if test="${not empty message}">
            <div class="alert alert-success alert-dismissable">
                <a href="#" class="close" data-dismiss="alert" aria-label="close">x</a>
                <strong>${message}</strong>
            </div>
        </c:if>

        <c:if test="${not empty error}">
            <div class="alert alert-danger alert-dismissable">
                <a href="#" class="close" data-dismiss="alert" aria-label="close">x</a>
                <strong>${error}</strong>
            </div>
        </c:if>


        <div id="contentPDF">
            <!-- title row -->
            <div class="row">
                <div class="col-xs-12">
                    <h2 class="page-header">
                        Invoice <span id="inv">#${invoice.invoiceNo}</span>
                        <c:if test="${invoice.fiscalYearInfo ne null}"><span style="margin-left: 20%;">Fiscal year : &nbsp;${invoice.fiscalYearInfo.title}</span></c:if>
                        <small class="pull-right">Date: <fmt:formatDate pattern="MMM dd, yyyy"
                                                                        value="${invoice.invoiceDate}"/></small>
                    </h2>
                </div>
                <!-- /.col -->
            </div>
            <!-- info row -->
            <div class="row invoice-info">
                <div class="col-sm-4 invoice-col">
                    From
                    <address>
                        <strong>${invoice.storeInfoDTO.name}</strong><br>
                        ${invoice.storeInfoDTO.cityName},${invoice.storeInfoDTO.stateName},${invoice.storeInfoDTO.countryName}<br>
                        ${invoice.storeInfoDTO.street}<br>
                        Phone: ${invoice.storeInfoDTO.contact},${invoice.storeInfoDTO.mobileNumber}<br>
                        Pan No.:${invoice.storeInfoDTO.panNumber}<br>
                        Email: ${invoice.storeInfoDTO.email}
                    </address>
                </div>
                <!-- /.col -->
                <div class="col-sm-4 invoice-col">
                    To
                    <address>
                        <c:if test="${invoice.orderInfo.clientInfo.companyName eq null or empty invoice.orderInfo.clientInfo.companyName}">
                            <strong>${invoice.orderInfo.clientInfo.name}</strong><br>
                        </c:if>

                        <c:if test="${invoice.orderInfo.clientInfo.companyName ne null and not empty invoice.orderInfo.clientInfo.companyName}">
                            <strong>${invoice.orderInfo.clientInfo.companyName}</strong><br>
                        </c:if>

                        <c:if test="${invoice.orderInfo.clientInfo.cityInfoDTO.cityName ne null and not empty invoice.orderInfo.clientInfo.cityInfoDTO.cityName}">
                            ${invoice.orderInfo.clientInfo.cityInfoDTO.cityName},
                        </c:if>

                        <c:if test="${invoice.orderInfo.clientInfo.cityInfoDTO.stateName ne null and not empty invoice.orderInfo.clientInfo.cityInfoDTO.stateName}">
                            ${invoice.orderInfo.clientInfo.cityInfoDTO.stateName},
                        </c:if>

                        <c:if test="${invoice.orderInfo.clientInfo.cityInfoDTO.countryName ne null and not empty invoice.orderInfo.clientInfo.cityInfoDTO.countryName}">
                            ${invoice.orderInfo.clientInfo.cityInfoDTO.countryName}<br>
                        </c:if>

                        <c:if test="${invoice.orderInfo.clientInfo.street ne null and not empty invoice.orderInfo.clientInfo.street}">
                            ${invoice.orderInfo.clientInfo.street}<br>
                        </c:if>

                        <c:if test="${invoice.orderInfo.clientInfo.pan ne null and not empty invoice.orderInfo.clientInfo.pan}">
                            Pan Number : ${invoice.orderInfo.clientInfo.pan}<br>
                        </c:if>


                        Phone:<c:if test="${invoice.orderInfo.clientInfo.contact ne null and not empty invoice.orderInfo.clientInfo.contact}">${invoice.orderInfo.clientInfo.contact}</c:if>
                        <c:if test="${invoice.orderInfo.clientInfo.mobileNumber ne null and not empty invoice.orderInfo.clientInfo.mobileNumber}">,${invoice.orderInfo.clientInfo.mobileNumber}</c:if><br>
                        Email: <c:if
                            test="${invoice.orderInfo.clientInfo.email ne null and not empty invoice.orderInfo.clientInfo.email}">${invoice.orderInfo.clientInfo.email}</c:if>

                    </address>
                </div>
                <!-- /.col -->
                <div class="col-sm-4 invoice-col no-print">
                    <%--<b>Invoice #${invoice.invoiceNo}</b><br>
                    <br>--%>
                    <b>Order ID:</b> <a
                        href="${pageContext.request.contextPath}/order/sale/${invoice.orderInfo.orderId}">#${invoice.orderInfo.orderNo}</a><br>
                    <c:if test="${invoice.receivableAmount gt 0}">
                        <b>Payment Due:</b> <fmt:formatNumber type="number" maxFractionDigits="3" groupingUsed="true"
                                                              value="${invoice.receivableAmount}"/><br>
                    </c:if>

                    <b>Account:</b> <a
                        href="${pageContext.request.contextPath}/paymentinfo/add?invoiceId=${invoice.invoiceId}">${invoice.orderInfo.clientInfo.accountNo}</a>
                </div>
                <!-- /.col -->
            </div>
            <!-- /.row -->

            <!-- Table row -->
            <div class="col-md-12">
                <div class="table-responsive">
                    <table class="table table-striped table-bordered">
                        <thead>
                        <tr>
                            <th>SN</th>
                            <th>Item</th>
                            <th>Quantity</th>
                            <th>Rate</th>
                            <th>Discount(%)</th>
                            <th>Subtotal</th>
                        </tr>
                        </thead>
                        <tbody>

                        <c:forEach items="${orderItemList}" var="orderItem" varStatus="i">
                            <tr>
                                <td>${i.index + 1}</td>
                                <td>${orderItem.itemInfoDTO.productInfo.name}-${orderItem.itemInfoDTO.tagInfo.name}</td>
                                <td>${orderItem.quantity} &nbsp; ${orderItem.itemInfoDTO.productInfo.unitInfo.code}</td>
                                <td><fmt:formatNumber type="number" maxFractionDigits="3" groupingUsed="true"
                                                      value="${orderItem.rate}"/></td>
                                <td><fmt:formatNumber type="number" maxFractionDigits="3" groupingUsed="true"
                                                      value="${orderItem.discount}"/></td>
                                <td><fmt:formatNumber type="number" maxFractionDigits="3" groupingUsed="true"
                                                      value="${orderItem.amount}"/></td>
                            </tr>
                        </c:forEach>

                        </tbody>
                    </table>
                </div>
                <!-- /.col -->
            </div>
            <!-- /.row -->

            <div class="row">

                <c:if test="${invoice.description ne null and '' ne invoice.description}">
                    <!-- accepted payments column -->
                    <div class="col-xs-6 no-print">
                        <p class="lead">Remark:</p>

                        <p class="text-muted well well-sm no-shadow" style="margin-top: 10px;">
                                ${invoice.description}
                        </p>
                    </div>
                </c:if>

                <!-- /.col -->
                <div class="col-lg-3 pull-right">
                    <%--<p class="lead">Amount Due 2/22/2014</p>--%>

                    <div class="table-responsive">
                        <table class="table">
                            <tr>
                                <th style="width:50%">Net Total:</th>
                                <td>Rs.<fmt:formatNumber type="number" maxFractionDigits="3" groupingUsed="true"
                                                         value="${invoice.orderInfo.totalAmount}"/></td>
                            </tr>
                            <tr>
                                <th>Tax (<fmt:formatNumber type="number" maxFractionDigits="3" groupingUsed="true"
                                                           value="${invoice.orderInfo.tax}"/>%)
                                </th>
                                <td>Rs.<fmt:formatNumber type="number" maxFractionDigits="3" groupingUsed="true"
                                                         value="${invoice.orderInfo.totalAmount * invoice.orderInfo.tax /100}"/></td>
                            </tr>
                            <tr>
                                <th>Grand Total:</th>
                                <td>Rs.<fmt:formatNumber type="number" maxFractionDigits="3" groupingUsed="true"
                                                         value="${invoice.totalAmount}"/></td>
                            </tr>
                            <tr>
                                <td>Entered By :</td>
                                <td>${invoice.createdByName}</td>
                            </tr>
                        </table>
                    </div>
                </div>
                <!-- /.col -->
            </div>
            <!-- /.row -->

            <div class="row">
                <c:choose>
                    <c:when test="${invoice.canceled eq true}">
                        <div class="col-xs-6 no-print">
                            <p class="lead">Cancel Reason:</p>

                            <p class="text-muted well well-sm no-shadow" style="margin-top: 10px;">
                                    ${invoice.cancelNote}
                            </p>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="col-xs-6 no-print margin"><label class="label label-success">Active</label></div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
        <!-- this row will not appear when printing -->
        <div class="row no-print" id="editor">
            <div class="col-xs-12">
                <a href="${pageContext.request.contextPath}/invoice/print?invoiceId=${invoice.invoiceId}"
                   class="btn btn-default" target="_blank"><i class="fa fa-print"></i> Print</a>

                <c:if test="${invoice.canceled eq false}">

                    <a href="${pageContext.request.contextPath}/paymentinfo/add?invoiceId=${invoice.invoiceId}"
                       class="btn btn-success pull-right"><i class="fa fa-credit-card"></i> Proceed To Payment </a>

                </c:if>

                <a href="${pageContext.request.contextPath}/invoice/pdf?invoiceId=${invoice.invoiceId}"
                   class="btn btn-primary pull-left" target="_blank" style="margin-right: 5px;">
                    <i class="fa fa-file-pdf-o"></i> Generate PDF
                </a>

                <c:choose>
                    <c:when test="${invoice.canceled eq true}"><label class="btn btn-danger">canceled</label> </c:when>
                    <c:otherwise>
                        <button class="btn btn-warning btn-sm" data-toggle="modal" data-target="#invoiceCancel">cancel
                        </button>
                    </c:otherwise>
                </c:choose>

                <a href="${pageContext.request.contextPath}/invoice/xls?invoiceId=${invoice.invoiceId}"
                   class="btn btn-primary pull-left" target="_blank" style="margin-right: 5px;">
                    <i class="fa fa-file-excel-o"></i> Generate Excel
                </a>
            </div>
        </div>

        <c:if test="${fn:length(paymentList) gt 0}">
            <div class="row">
                <div class="col-md-12">
                    <div class="row">
                        <div class="col-md-12"><h4>Payment Details</h4></div>
                    </div>
                    <div class="row">
                        <div class="col-md-12">
                            <table class="table table-bordered table-hover table-striped">
                                <thead>
                                <tr>
                                    <th>Date</th>
                                    <th>Amount</th>
                                    <th>Method</th>
                                    <th>Remarks</th>
                                    <th>Cheque Date</th>
                                    <th>Exp-Withdrawable Date</th>
                                    <th>Bank Name</th>
                                    <th>Bank Account</th>
                                    <th>Action</th>
                                </tr>
                                </thead>

                                <tbody>
                                <c:forEach items="${paymentList}" var="paymentInfo">

                                    <c:if test="${paymentInfo.receivedPayment ne null}">
                                        <tr>
                                            <td><fmt:formatDate pattern="MMM dd, yyyy"
                                                                value="${paymentInfo.paymentDate}"/></td>
                                            <td><fmt:formatNumber type="number" maxFractionDigits="3"
                                                                  groupingUsed="true"
                                                                  value="${paymentInfo.receivedPayment.amount}"/></td>
                                            <td>${paymentInfo.receivedPayment.paymentMethod}</td>
                                            <td>${paymentInfo.remark}</td>
                                            <td><fmt:formatDate pattern="MMM dd, yyyy"
                                                                value="${paymentInfo.receivedPayment.chequeDate}"/></td>

                                            <td><fmt:formatDate pattern="MMM dd, yyyy"
                                                                value="${paymentInfo.receivedPayment.commitedDateOfCheque}"/></td>
                                            <td>${paymentInfo.receivedPayment.bankOfCheque}</td>
                                            <td>${paymentInfo.receivedPayment.bankAccountNumber}</td>
                                            <th>
                                                <c:if test="${paymentInfo.receivedPayment.paymentMethod eq 'CHEQUE'}">
                                                    <c:choose>
                                                        <c:when test="${paymentInfo.receivedPayment.status eq 'INACTIVE'}"><a
                                                                class="btn btn-sm btn-danger"
                                                                href="${pageContext.request.contextPath}/paymentinfo/chuque/collect?paymentId=${paymentInfo.paymentInfoId}">Is
                                                            Collected ?</a></c:when>
                                                        <c:otherwise><label
                                                                class="label label-success">collected</label> </c:otherwise>
                                                    </c:choose>
                                                </c:if>
                                            </th>
                                        </tr>
                                    </c:if>

                                    <c:if test="${paymentInfo.refundPayment ne null}">
                                        <tr>
                                            <td><fmt:formatDate pattern="MMM dd, yyyy"
                                                                value="${paymentInfo.paymentDate}"/></td>
                                            <td><fmt:formatNumber type="number" maxFractionDigits="3"
                                                                  groupingUsed="true"
                                                                  value="${paymentInfo.refundPayment.amount}"/></td>
                                            <td>${paymentInfo.refundPayment.paymentMethod}</td>
                                            <td>${paymentInfo.remark}</td>

                                        </tr>
                                    </c:if>
                                </c:forEach>

                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </c:if>

        <c:if test="${fn:length(logger) gt 0}">
            <div class="row" style="margin-top: 10px;">

                <div class="col-md-12">
                    <div class="row">
                        <div class="col-md-6">
                            <h4 class="text-center">Printed Logs</h4>
                            <table class="table">
                                <thead>
                                <tr>
                                    <th>S.N.</th>
                                    <th>Username</th>
                                    <th>Log</th>
                                    <th>Date</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach varStatus="i" items="${logger}" var="log">
                                    <tr>
                                        <td>${i.index + 1}</td>
                                        <td>${log.username}</td>
                                        <td>${log.log}</td>
                                        <td><fmt:formatDate type="both" dateStyle="medium" timeStyle="medium"
                                                            value="${log.date}"/></td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </c:if>
    </section>
    <!-- /.content -->
    <div class="clearfix"></div>

    <!-- Modal -->
    <div class="modal fade" id="invoiceCancel" role="dialog">
        <div class="modal-dialog">

            <!-- Modal content-->
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                    <h4 class="modal-title">Cancel invoice</h4>
                </div>
                <form action="${pageContext.request.contextPath}/invoice/cancel" method="post">
                    <div class="modal-body">

                        <input type="hidden" name="invoiceId" value="${invoice.invoiceId}">
                        <input type="hidden" name="version" value="${invoice.version}">
                        <div class="form-group">
                            <label for="description">Reason:</label><br/>
                            <textarea class="form-control" placeholder="write something.." name="note"
                                      id="description" rows="3" cols="30" required></textarea>
                        </div>

                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default pull-left" data-dismiss="modal">Close</button>
                        <button type="submit" class="btn btn-success pull-right">Save changes</button>
                    </div>
                </form>
            </div>

        </div>
    </div>
    <!-- model end -->

</div>
<%@include file="/pages/parts/footer.jsp" %>
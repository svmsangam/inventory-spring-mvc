<%--
  Created by IntelliJ IDEA.
  User: dhiraj
  Date: 10/20/17
  Time: 1:38 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@include file="/pages/parts/header.jsp" %>
<%@include file="/pages/parts/sidebar.jsp" %>

<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">

    <section class="content">
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

        <div class="row">
            <div class="col-xs-12">
                <div class="box box-info">
                    <div class="box-header">
                        <h3 class="box-title">Ledger Filter of Account &nbsp; ${accountNo} &nbsp; ${clientName}</h3>

                        <div class="dropdown pull-right">
                            <button class="btn btn-primary dropdown-toggle" type="button" data-toggle="dropdown">Download Report
                                <span class="caret"></span></button>
                            <ul class="dropdown-menu">
                                <li><a href="${pageContext.request.contextPath}/report/ledger/filter/pdf?clientId=${term.clientId}&from=<fmt:formatDate pattern="MM/dd/yyyy" value="${term.from}"/>&to=<fmt:formatDate pattern="MM/dd/yyyy" value="${term.to}"/>">PDF</a></li>
                                <li><a href="${pageContext.request.contextPath}/report/ledger/filter/xls?clientId=${term.clientId}&from=<fmt:formatDate pattern="MM/dd/yyyy" value="${term.from}"/>&to=<fmt:formatDate pattern="MM/dd/yyyy" value="${term.to}"/>">XLS</a></li>
                            </ul>
                        </div>

                    </div>
                    <!-- /.box-header -->
                    <div class="box-body">
                        <form action="${pageContext.request.contextPath}/ledger/filter" method="GET" modelAttribute="terms">

                            <div class="row well well-sm">

                                <div class="col-md-4">
                                    <div class="form-group">
                                        <label>Client Name</label>
                                        <select class="choose1 form-control" name="clientId">

                                        </select>
                                        <p class="form-error"></p>
                                    </div>
                                </div>

                                <div class="col-md-3">
                                    <div class="form-group">
                                        <label>From Date:</label>
                                        <div class='input-group date'>
                                            <div class="input-group-addon">
                                                <i class="fa fa-calendar"></i>
                                            </div>
                                            <input type="text" class="form-control datepicker"
                                                   onkeypress="return false;" onkeyup="return false;"
                                                    value="<fmt:formatDate pattern="MM/dd/yyyy" value="${term.from}"/>"
                                                   name="from" placeholder="Delivery Date"/>
                                        </div>
                                        <p class="form-error"></p>
                                    </div>
                                </div>

                                <div class="col-md-3">
                                    <div class="form-group">
                                        <label>To Date:</label>
                                        <div class='input-group date'>
                                            <div class="input-group-addon">
                                                <i class="fa fa-calendar"></i>
                                            </div>
                                            <input type="text" class="form-control datepicker"
                                                   onkeypress="return false;" onkeyup="return false;"
                                                   name="to" placeholder="Delivery Date" value="<fmt:formatDate pattern="MM/dd/yyyy" value="${term.to}"/>"/>
                                        </div>
                                        <p class="form-error"></p>
                                    </div>
                                </div>
                                <div class="margin">
                                    <button type="submit" class="btn btn-primary btn-flat btn-lg">filter</button>
                                </div>

                            </div>
                        </form>

                        <table class="table table-bordered table-hover table-striped">
                            <thead>
                            <tr>
                                <th>Date</th>
                                <th>AccountNo</th>
                                <th>Amount</th>
                                <th>DR/CR</th>
                                <th>Ledger Entry</th>
                            </tr>
                            </thead>
                            <tbody class="border-bottom">
                            <c:forEach var="ledger" items="${ledgerList}" varStatus="counter">
                                <tr>

                                    <td><fmt:formatDate pattern="MMM dd, yyyy" value="${ledger.date}"/></td>
                                    <td>${ledger.accountInfo.acountNumber}</td>
                                    <td><fmt:formatNumber type="number" maxFractionDigits="3" groupingUsed="true" value="${ledger.amount}"/></td>
                                    <td>${ledger.accountEntryType}</td>
                                    <td>${ledger.ledgerEntryType}</td>
                                </tr>
                            </c:forEach>
                            </tbody>
                            <tfoot>
                            <tr >
                                <th colspan="4">Debit Amount &nbsp; (<fmt:formatDate pattern="MMM dd, yyyy" value="${term.from}"/> &nbsp; to &nbsp; <fmt:formatDate pattern="MMM dd, yyyy" value="${term.to}"/>)</th>
                                <td colspan="4">Rs <fmt:formatNumber type="number" maxFractionDigits="3" groupingUsed="true" value="${totalFilterDr}"/></td>
                            </tr>
                            <tr>
                                <th colspan="4">Credit Amount &nbsp; (<fmt:formatDate pattern="MMM dd, yyyy" value="${term.from}"/> &nbsp; to &nbsp; <fmt:formatDate pattern="MMM dd, yyyy" value="${term.to}"/>)</th>
                                <td colspan="4">Rs <fmt:formatNumber type="number" maxFractionDigits="3" groupingUsed="true" value="${totalFilterCr}"/></td>
                            </tr>
                            <tr>
                                <th colspan="4">Balance &nbsp; (<fmt:formatDate pattern="MMM dd, yyyy" value="${term.from}"/> &nbsp; to &nbsp; <fmt:formatDate pattern="MMM dd, yyyy" value="${term.to}"/>)</th>
                                <td colspan="4">Rs <fmt:formatNumber type="number" maxFractionDigits="3" groupingUsed="true" value="${totalFilterCr - totalFilterDr}"/></td>
                            </tr>
                            <tr>
                                <th colspan="4" >Total Balance</th>
                                <th colspan="4" >Rs <fmt:formatNumber type="number" maxFractionDigits="3" groupingUsed="true" value="${totalCr - totalDr}"/></th>
                            </tr>
                            </tfoot>
                        </table>
                    </div>


                    <c:if test="${fn:length(pagelist) gt 1}">

                        <div class="col-xs-12">
                            <nav class="pull-right">
                                <ul class="pagination">

                                    <c:if test="${currentpage > 1}">
                                        <li class="page-item">

                                            <a href="${pageContext.request.contextPath}/ledger/filter?pageNo=${currentpage-1}&clientId=${term.clientId}&from=<fmt:formatDate pattern="MM/dd/yyyy" value="${term.from}"/>&to=<fmt:formatDate pattern="MM/dd/yyyy" value="${term.to}"/>"
                                               class="page-link">Prev</a>
                                        </li>
                                    </c:if>

                                    <c:forEach var="pagelist" items="${pagelist}">
                                        <c:choose>
                                            <c:when test="${pagelist == currentpage}">

                                                <li class="page-item active">
                                                  <span class="page-link">
                                                    ${pagelist}
                                                    <span class="sr-only">(current)</span>
                                                  </span>
                                                </li>

                                            </c:when>
                                            <c:otherwise>

                                                <li class="page-item"><a class="page-link"
                                                                         href="${pageContext.request.contextPath}/ledger/filter?pageNo=${pagelist}&clientId=${term.clientId}&from=<fmt:formatDate pattern="MM/dd/yyyy" value="${term.from}"/>&to=<fmt:formatDate pattern="MM/dd/yyyy" value="${term.to}"/>">${pagelist}</a>
                                                </li>

                                            </c:otherwise>

                                        </c:choose>
                                    </c:forEach>

                                    <c:if test="${currentpage + 1 <= lastpage}">
                                        <li class="page-item">
                                            <a class="page-link"
                                               href="${pageContext.request.contextPath}/ledger/filter?pageNo=${currentpage+1}&clientId=${term.clientId}&from=<fmt:formatDate pattern="MM/dd/yyyy" value="${term.from}"/>&to=<fmt:formatDate pattern="MM/dd/yyyy" value="${term.to}"/>">Next</a>
                                        </li>
                                    </c:if>
                                </ul>
                            </nav>
                        </div>

                    </c:if>

                </div>
            </div>
        </div>
    </section>
</div>
<%@include file="/pages/parts/footer.jsp" %>


<script>
    $(document).ready(function () {

        $(".choose1").select2({
            ajax: {
                url: '${pageContext.request.contextPath}/client/search',
                dataType: 'json',
                headers: {'X-CSRF-TOKEN': $('meta[name="csrf-token"]').attr('content')},
                delay: 250,
                type: 'GET',
                data: function (params) {
                    return {
                        term: params.term, // search term
                        /* page: params.page*/
                    };
                },
                processResults: function (data, params) {
                    params.page = params.page || 1;
                    var arr = []
                    $.each(data.detail, function (index, value) {

                        if (value.companyName === null) {

                            arr.push({
                                id: value.clientId,
                                text: value.name + ' - ' + value.mobileNumber
                            })
                        } else {
                            arr.push({
                                id: value.clientId,
                                text: value.companyName + ' - ' + value.mobileNumber
                            })
                        }
                    })


                    return {
                        results: arr/*,
                         pagination: {
                         more: (params.page * 1) < 2
                         }*/
                    };
                },
                cache: true
            },
            escapeMarkup: function (markup) {
                return markup;
            },
            minimumInputLength: 1,
            placeholder: "Search Customer by Name & Mobile No"
        });
    });
</script>


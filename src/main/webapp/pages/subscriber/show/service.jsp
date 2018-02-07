<%--
  Created by IntelliJ IDEA.
  User: dhiraj
  Date: 2/7/18
  Time: 2:27 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="row">
    <div class="col-md-12">
        <table class="table table-bordered table-hover table-striped">
            <thead>
            <tr>
                <th>SN</th>
                <th>Title</th>
                <th>Total Store</th>
                <th>Validity</th>
                <th>Rate</th>
                <th>Selected</th>
                <th>Status</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="subscriberService" items="${subscriberServiceList}" varStatus="i">
                <tr>
                    <td>${i.index + 1}</td>
                    <td>${subscriberService.serviceInfo.title}</td>
                    <td>${subscriberService.serviceInfo.totalStore}</td>
                    <td>${subscriberService.serviceInfo.expireDays} days</td>
                    <td>${subscriberService.serviceInfo.rate}</td>
                    <td>
                        <c:if test="${subscriberService.selected eq true}"><i class="fa fa-check-square"></i></c:if>
                    </td>
                    <td>
                        <c:if test="${subscriberService.status eq 'ACTIVE'}"><span class="label label-success">Active</span></c:if>
                        <c:if test="${subscriberService.status ne 'ACTIVE'}"><span class="label label-danger">Deactive</span></c:if>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>

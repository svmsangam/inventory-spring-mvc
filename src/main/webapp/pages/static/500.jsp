<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="/pages/parts/header.jsp" %>
<%@include file="/pages/parts/sidebar.jsp" %>

<div class="content-wrapper">

<section class="content">

    <div class="error-page">
        <h2 class="headline text-red">500</h2>

        <div class="error-content">
            <h3><i class="fa fa-warning text-red"></i> Oops! Something went wrong.</h3>

            <p>
                We will work on fixing that right away.
                Meanwhile, you may <a href="">return to dashboard</a> or try using the search form.
            </p>

            <form class="search-form">
                <div class="input-group">
                    <input type="text" name="search" class="form-control" placeholder="Search">

                    <div class="input-group-btn">
                        <button type="submit" name="submit" class="btn btn-danger btn-flat"><i class="fa fa-search"></i>
                        </button>
                    </div>
                </div>
                <!-- /.input-group -->
            </form>
        </div>
    </div>
    <!-- /.error-page -->

</section>
<!-- /.content -->
</div>
<%@include file="/pages/parts/footer.jsp" %>

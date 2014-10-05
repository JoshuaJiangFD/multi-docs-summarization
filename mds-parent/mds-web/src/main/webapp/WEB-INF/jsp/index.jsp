<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>MDS</title>
<!-- for mobile device -->
<meta content="width=device-width, initial-scale=1.0" name="viewport" />
<meta content="" name="description" />
<meta content="" name="author" />
<!-- BEGIN GLOBAL MANDATORY STYLES -->
<link href="includes/bootstrap/css/bootstrap.min.css" rel="stylesheet"
	type="text/css" />
<link href="css/main.css" rel="stylesheet" type="text/css" />
<!-- END GLOBAL MANDATORY STYLES -->
</head>
<body>
	<!-- BEGIN HEADER -->
	<div class="header navbar navbar-inverse navbar-fixed-top">
		<div class="navbar-inner">
			<div class="navbar-header">
				<button type="button" class="navbar-toggle" data-toggle="collapse"
					data-target=".navbar-collapse">
					<span class="sr-only">Toggle navigation</span> <span
						class="icon-bar"></span> <span class="icon-bar"></span> <span
						class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href="#">MDS</a>
			</div>
			<div class="collapse navbar-collapse">
				<ul class="nav navbar-nav">
					<li class=""><a href="#">Search</a></li>
					<li><a href="#about">Index</a></li>
				</ul>
			</div>
		</div>
	</div>
	<!-- END HEADER -->

	<!-- BEGIN CONTAINER -->
	<div class="container">
		<form class="searchform" action="<c:url value="/search" />"  method="get">
			<input type="text" name="q">
			<!--
			-->
			<button type="submit" class="btn btn-primary" type="submit">搜索</button>
		</form>
	</div>
	<!-- END CONTAINER -->

	<!-- BEGIN FOOTER -->
	<footer class="footer">
	<p>&copy; Company 2014</p>
	</footer>
	<!-- END FOOTER -->
	<!-- BEGIN JAVASCRIPTS(Load javascripts at bottom, this will reduce page load time) -->
	<!-- BEGIN CORE PLUGINS -->
	<script src="includes/jquery/jquery-2.1.1.min.js"></script>
	<script src="includes/bootstrap/js/bootstrap.min.js"></script>
	<!-- END CORE PLUGINS -->

	<!-- BEGIN PAGE LEVEL PLUGINS -->
	<!-- END PAGE LEVEL PLUGINS -->
</body>
</html>
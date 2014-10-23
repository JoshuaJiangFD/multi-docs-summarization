<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Query Result</title>
<!-- for mobile device -->
<meta content="width=device-width, initial-scale=1.0" name="viewport" />
<meta content="" name="description" />
<meta content="" name="author" />

<!-- BEGIN GLOBAL MANDATORY STYLES -->
<link rel="stylesheet" href="includes/bootstrap/css/bootstrap.css"
	rel="stylesheet" type="text/css" />

<link rel="stylesheet" href="includes/jqwidgets/styles/jqx.base.css" type="text/css" />
	
<link  rel="stylesheet" href="includes/jqueryui/jquery-ui.css" type="text/css">
<link  rel="stylesheet" href="includes/jqueryui/jquery-ui.structure.css" type="text/css">

<link rel="stylesheet" href="css/reset.css"  type="text/css" />
<!-- END GLOBAL MANDATORY STYLES -->

<!-- BEGIN PAGE LEVEL STYLES -->
<link href="css/query.css" rel="stylesheet" type="text/css" />
<!-- END PAGE LEVEL STYLES -->
<style type="text/css">
html, body {
	height: 100%;
}

.container {
	width: 90%;
	margin: 0px auto;
}

.footer {
	background-color: #080808;
	width: 100%;
	height: 20px;
}

.mainContent {
	padding-top: 50px;
	padding-left: 5px;
	padding-right: 5px;
	height: 100%;
	padding-bottom: 5px;
}

#progressbar{
	width: 300px;
    value: false;
	z-index:1;
}

#sumBtn{
	font-size: 12px;
}
</style>
</head>
<body>
	<header class="navbar navbar-inverse navbar-fixed-top"
		role="navigation">
	<div class="container">
		<div class="navbar-header">
			<button class="navbar-toggle" data-target=".navbar-collapse"
				data-toggle="collapse" type="button">
				<span class="sr-only">Toggle navigation</span> <span
					class="icon-bar"></span> <span class="icon-bar"></span> <span
					class="icon-bar"></span>
			</button>
			<a class="navbar-brand" href="#">MDS</a>
		</div>
		<div class="navbar-collapse collapse">
			<form class="navbar-form navbar-right" role="form">
				<div class="form-group">
					<input class="form-control" type="text" placeholder="keyword..">
				</div>
				<button class="btn btn-success" type="submit">Search</button>
			</form>
		</div>
	</div>
	</header>

	<div class="mainContent">
		<div id="mainSplitter">
			<div>
				<div style="border: none;" id="feedExpander">
					<div class="jqx-hideborder">Clusters</div>
					<div class="jqx-hideborder jqx-hidescrollbars">
						<div class="jqx-hideborder" id='jqxTree'>
						</div>
					</div>
				</div>
			</div>
			<div>
				<div id="contentSplitter">
					<div class="feed-item-list-container" id="feedUpperPanel">
						<div class="jqx-hideborder" id="feedListExpander">
							<div class="jqx-hideborder" id="feedHeader"></div>
							<div class="jqx-hideborder jqx-hidescrollbars">
								<div class="jqx-hideborder" id="feedListContainer"></div>
							</div>
						</div>
					</div>
					<div id="feedContentArea">
						<div class="jqx-hideborder" id="feedContentExpander">
							<div class="jqx-hideborder" id="feedItemHeader">
								<button id="sumBtn" type="button" class="btn btn-primary">Summarize</button>
							</div>
							<div class="jqx-hideborder jqx-hidescrollbars">
								<div class="jqx-hideborder" id="feedItemContent"></div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	<!--  <footer class="footer">
		<p class="text-muted">Place sticky footer content here.</p>
	</footer>-->
	</div>
	
	<!-- BEGIN JAVASCRIPTS(Load javascripts at bottom, this will reduce page load time) -->
	
	<!-- BEGIN CORE PLUGINS -->
	<script src="includes/jquery/jquery-2.1.1.min.js"></script>

	<script src="includes/bootstrap/js/bootstrap.js"></script>
	
	<script src="includes/jqueryui/jquery-ui.js"></script>

	<script type="text/javascript" src="includes/jqwidgets/jqxcore.js"></script>
	<script type="text/javascript" src="includes/jqwidgets/jqxtree.js"></script>
	<script type="text/javascript" src="includes/jqwidgets/jqxsplitter.js"></script>
	<script type="text/javascript" src="includes/jqwidgets/jqxbuttons.js"></script>
	<script type="text/javascript" src="includes/jqwidgets/jqxpanel.js"></script>
	<script type="text/javascript" src="includes/jqwidgets/jqxlistbox.js"></script>
	<script type="text/javascript" src="includes/jqwidgets/jqxexpander.js"></script>
	<script type="text/javascript" src="includes/jqwidgets/jqxscrollbar.js"></script>
	<script type="text/javascript" src="includes/jqwidgets/jqxdata.js"></script>
	<!-- END CORE PLUGINS -->
	
	<!-- BEGIN PAGE LEVEL SCRIPTS -->
	<script type="text/javascript">
		var queryterm='${queryterm}'
	</script>
	<script type="text/javascript" src="js/query.js"></script>
	<!-- END PAGE LEVEL SCRIPTS -->
</body>
</html>
/**
 * the js file for query page
 */
$(document).ready(function() {
	var rss = (function($) {
		var createWidgets = function() {
			$('#mainSplitter').jqxSplitter({
				width : '100%',
				height : '100%',
				panels : [ {
					size : 200,
					min : 100
				}, {
					min : 200,
					size : '100%'
				} ]
			});
			$('#contentSplitter').jqxSplitter({
				width : '100%',
				height : '100%',
				orientation : 'horizontal',
				panels : [ {
					size : 400,
					min : 100,
					collapsible : false
				}, {
					min : 100,
					collapsible : true
				} ]
			});
			$("#feedExpander").jqxExpander({
				toggleMode : 'none',
				showArrow : false,
				width : "100%",
				height : "100%",
				initContent : function() {
					$('#jqxTree').jqxTree({
						height : '100%',
						width : '100%'
					});
				}
			});
			$("#feedContentExpander").jqxExpander({
				toggleMode : 'none',
				showArrow : false,
				width : "100%",
				height : "100%",
				initContent : function() {
					$('#feedItemContent').jqxPanel({
						width : '100%',
						height : '100%'
					});
				}
			});
			$("#feedListExpander").jqxExpander({
				toggleMode : 'none',
				showArrow : false,
				width : "100%",
				height : "100%",
				initContent : function() {
					$('#feedListContainer').jqxListBox({
						width : '100%',
						height : '100%'
					});
				}
			});
		};
		var initQuery = function(queryterm) {
			var urlstr = "/cluster/" + queryterm;
			$.ajax({
				url : urlstr,
				headers : {
					Accept : "application/json; charset=utf-8",
				},
			success : function(response) { 
				var clusters=response.clusters;
				var docs=response.docs;
				$('#jqxTree').jqxTree({
					source:clusters,
					height : '100%',
					width : '100%'
				});
			}
			});
		}
		return {
			init : function() {
				createWidgets();
				if (queryterm)
					initQuery(queryterm);
			}
		}
	}(jQuery));
	rss.init();
});
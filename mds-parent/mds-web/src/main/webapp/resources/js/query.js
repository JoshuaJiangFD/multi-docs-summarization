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
        var addEventListeners = function () {
            $('#jqxTree').on('select', function (event) {
                var item = $('#jqxTree').jqxTree('getItem', event.args.element);
                getDocs(item.label);
            });
        };
       var getDocs=function(clusterLabel){
    	   var currentDocs=[];
    	   var docIds;
    	   $(pageData.clusters).each(function(index){
    		   if(clusterLabel==this.label){
    			   docIds=this.ids
    		   }
    		});
    	   if(docIds){
    	  	   $(pageData.docs).each(function(index){
    	  		   if($.inArray(this.id, docIds)!=-1){
    	  			 currentDocs.push(this);
    	  		   }
        		});  
    	   }
    	   $('#feedListContainer').jqxListBox('source', currentDocs);
       }  
		var initQuery = function(queryterm) {
			var urlstr = "rest/cluster/" + queryterm;
			$.ajax({
				url : urlstr,
				headers : {
					Accept : "application/json; charset=utf-8",
				},
				contentType:'application/json; charset=UTF-8',
			success : function(response) { 
				pageData.clusters=response.clusters;
				pageData.docs=response.docs;
				$('#jqxTree').jqxTree({
					source:pageData.clusters,
					height : '100%',
					width : '100%'
				});
			}
			});
		}
		var pageData={
				clusters:[],
				docs:{},
		}
		return {
			init : function() {
				createWidgets();
				addEventListeners();
				if (queryterm)
					initQuery(queryterm);
			}
		}
	}(jQuery));
	rss.init();
});
/**
 * the js file for query page
 */
$(document).ready(function() {
	var $dialog=$('<div id="progressbar"></div>');
	
	$(document).ajaxStart(function(){
		$dialog.dialog({  
	        open: function(event, ui) { $(".ui-dialog-titlebar-close").hide(); },
	        modal: true,
	        width:800
	    });
	}).ajaxStop(function(){
		$dialog.dialog( "close");
	});
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
            $('#sumBtn').on('click', function (event) {
            	if(!pageData.currentDocIds||pageData.currentDocIds.length==0){
            		return;
            	}
            	var urlstr = "rest/sum";
            	var data={
            			docIds:pageData.currentDocIds
            	};
            	$.ajax({
            		url : urlstr,
            		type: 'post',    
            		cache: false,
            		dataType:'json',
            		success : function(response) {
              			$('#feedItemContent').jqxPanel('prepend', '<div style="padding: 1px;"><span>' + 
              					response + '</span></div>');
            		}
            	});
            });
        };
       var getDocs=function(clusterLabel){
    	   var label=clusterLabel.replace(/\[\d+\]/i,'');
    	   var currentDocs=[];
    	   var docIds;
    	   $(pageData.clusters).each(function(index){
    		   if(label==this.label){
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
    	   pageData.currentDocIds=docIds;
    	   renderDocs(currentDocs);
       }  
       var renderDocs=function(docs){
    	   var listBox=$('#feedListContainer');
    	   var source={
    		localdata:docs,
    		datatype:"array"
    	   };
    	   var dataAdapter = new $.jqx.dataAdapter(source);
    	   listBox.jqxListBox({ selectedIndex: 0,  source: docs, displayMember: "id", valueMember: "title", itemHeight: 70, height: '100%', width: '100%',
               renderer: function (index, label, value) {
                   var datarecord = this.source[index];
                   var title = '<a href="'+datarecord.url+'">'+datarecord.title+'</a>';
                   var table = '<table style="min-width: 130px;"><tr><td>' + title + '</td></tr><tr><td>' + datarecord.content + '</td></tr></table>';
                   return table;
               }
           });
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
						pageData.clusters = response.clusters;
						pageData.docs = response.docs;
						headers = [];
						$(pageData.clusters).each(
										function(index) {
											headers.push('['+ this.ids.length+ ']'+ this.label);
										});
						$('#jqxTree').jqxTree({
							source : headers,
							height : '100%',
							width : '100%'
						});
						$('#jqx-hideborder').text("Clusters for <b>"+ queryterm+ '</b>');
					}
				});
		}
		var pageData={
				clusters:[],
				docs:{},
				currentDocIds:[]
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
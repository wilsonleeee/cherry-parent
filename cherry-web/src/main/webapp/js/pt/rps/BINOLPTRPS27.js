function BINOLPTRPS27(){
	this.needUnlock = true;
};

BINOLPTRPS27.prototype={
		"search" : function() {
			var url = $("#search_Url").attr("href");
			var params= $("#mainForm").serialize();
			params = params + "&" +getRangeParams();
			if(params != null && params != "") {
				url = url + "?" + params;
			}
			// 显示结果一览
			$("#resultInfo").removeClass("hide");
			// 表格设置
			var depart = $("input[name='depart']");
			var totalBussinessTypeSize = $('#totalBussinessTypeSize').val();
			//动态生成列
			var aoColumns = [  { "sName": "no", "bSortable": false}, //基础数据
				                { "sName": "StatisticDate"},
				                { "sName": "weekday"},
								{ "sName": "departName"},
								{ "sName": "departType"}];
			for(var i=0;i<totalBussinessTypeSize;i++){
				aoColumns.push({ "sName": "type"+i, "bSortable": false});
			}
			var tableSetting = {
					 // 表格ID
					 tableId : '#dataTable',
					 // 数据URL
					 url : url,
					 bFilter:false,
					 inedex : 0,
					 // 表格默认排序
					 aaSorting : [[ 1, "desc" ]],
					fixedColumns: 0,
					// 横向滚动条出现的临界宽度
					sScrollX : "100%",
					fnDrawCallback: function() {
						//将不为0的数据标记为红色
						$('#dataTable').find('span').each(function(){
							var val = $.trim($(this).html());
							if(val == '' || val == 0){
								$(this).html('-');
							}else if(!isNaN(val)){
								$(this).html(Number(val));
								if(val > 0){
									$(this).attr("class","highlight");
								}
							}
						});
					}
			};
			tableSetting.aoColumns = aoColumns;
			// 调用获取表格函数
			getTable(tableSetting);			
	    },
	    "locationPosition" : function(){
	    	var treeObj = $.fn.zTree.getZTreeObj("departTree");
	    	//刷新树
	    	treeObj.refresh();
	    	var name = $('#locationPosition').val();
	    	if(name == null || name == ''){
	    		return;
	    	}
	    	//模糊查询节点
	    	var nodes = treeObj.getNodesByParamFuzzy("name", name, null);
	    	//设置选中状态
	    	for(var i = 0; i<nodes.length; i++){
	    		if(!nodes[i].isParent){
	    			treeObj.selectNode(nodes[i],true);
	    		}
	    	}
	    },
	    "exportExcel" : function(url){
	    	 if($(".dataTables_empty:visible").length==0){
				var that = this;
	            var params= $("#mainForm").serialize();
	            // $("#mainForm").serialize()中已经有csrftoken值了
//	            params = params + "&csrftoken=" +parentTokenVal();
	            params = params + "&" +getRangeParams();
	            url = url + "?" +params;
	            //锁住父页面
	            that.needUnlock=false;
	            window.open(url,"_self");
	            //开启父页面
	            that.needUnlock=true;
	    	 }
	    },
	    "popParameter":function(url){
	    	if($("#popParameter").length == 0) {
	    		$("body").append('<div style="display:none" id="popParameter"></div>');
	    	} else {
	    		$("#popParameter").empty();
	    	}
	    	var initCallback = function(msg){
		    	var dialogSetting = {
		    			dialogInit: "#popParameter",
		    			text: msg,
		    			width: 	800,
		    			height: 600,
		    			title: 	$("#dialogTitle").html(),
		    			confirm: $("#dialogConfirm").html(),
		    			cancel: $("#dialogCancel").html(),
		    			confirmEvent: function(){
		    				var parameterType = $('#parameter_type').val();
		    				var params= $("#parameterForm").serialize() + "&csrftoken=" + getTokenVal();
		    				var url = $('#setParameter_url').attr("href");
		    				if(parameterType == 'BT'){
		    					cherryAjaxRequest({
			    					url : url,
			    					param : params,
			    					formId : 'mainForm',
			    					callback:function(msg){
			    						ajaxSubmitLeftMenu($('#hiddenbasePath').val() +'/pt/BINOLPTRPS27_init.action'+'?currentMenuID=BINOLPTRPS27&parentMenuID=PTRPS');			
			    					}
			    		    	});
		    				}else if(parameterType == 'DP'){
		    					var zTree = $.fn.zTree.getZTreeObj("departTree");
		    					if(zTree != null){
				        	        var nodes = zTree.getCheckedNodes(true);
				        	        //获取选中的节点
				        	        var checkedNode = "";
				        	        var checkedParentNode = "";
				        	        for (var i = 0; i < nodes.length; i++) {
				        	    	    if(!nodes[i].getCheckStatus().half && !nodes[i].isParent){
				        	    	    	checkedNode +=  nodes[i].id+",";
				        	    	    }
				        	    	    if(nodes[i].isParent){
				        	    	    	checkedParentNode +=  nodes[i].id+",";
				        	    	    }
				        	        }        
				        			url = url + "?dpParameter = " + checkedNode+"&dtParameter="+checkedParentNode;
			    				}
		    					cherryAjaxRequest({
			    					url : url,
			    					param : params,
			    					formId : 'mainForm',
			    					callback:function(msg){
			    						ajaxSubmitLeftMenu($('#hiddenbasePath').val() +'/pt/BINOLPTRPS27_init.action'+'?currentMenuID=BINOLPTRPS27&parentMenuID=PTRPS');			
			    					}
			    		    	});
		    				}else if(parameterType == 'ST'){
		    					cherryAjaxRequest({
			    					url : url,
			    					param : params,
			    					formId : 'mainForm',
			    					callback:function(msg){
			    						ajaxSubmitLeftMenu($('#hiddenbasePath').val() +'/pt/BINOLPTRPS27_init.action'+'?currentMenuID=BINOLPTRPS27&parentMenuID=PTRPS');			
			    					}
			    		    	});
		    				}
		    			},
		    			cancelEvent: function(){
		    				removeDialog("#popParameter");
		    			}
		    	};
		    	openDialog(dialogSetting);
		    	
	    	}
	    	cherryAjaxRequest({
	    		url: url,
	    		param: null,
	    		callback: initCallback
	    	});
	    }
};

var BINOLPTRPS27 = new BINOLPTRPS27();
$(document).ready(function() {
   // BINOLPTRPS27.search();
});

function selectSubSys(object,url,type) {
	$('#actionResultDisplay').html("");
	$('#parameterContent').html($('#loading').html());
	$(object).siblings().removeClass("on");
	$(object).addClass("on");
	$('#parameterTitle').html($(object).html());
	if(type != 'dp'){
		cherryAjaxRequest({
			url: url,
			param: null,
			callback: function(msg){
				$('#parameterContent').html(msg);
			}
		});
	}
	if(type == 'dp'){
		cherryAjaxRequest({
			url: url,
			param: null,
			callback: function(msg){
				$('#parameterContent').html(msg);
				//生成tree
	    		cherryAjaxRequest({
	    			url: $('#getDepartTree_Url').attr("href"),
	    			param: null,
	    			callback: function(msg){
	    				var setting = {
	    						check: {
	    							enable: true,
	    							chkboxType: { "Y": "", "N": "" }
	    						},
	    						data:{
	    							key:{
	    								name:"name",
	    								children:"nodes"
	    							}
	    						}
	    				};
	    				var $departTree = $('#departTree');
	    				//初始化树
	    				treeObj = $.fn.zTree.init($departTree,setting,eval('('+msg+')'));
	    				//初始选择的节点
	    				var depart = $("input[name='depart']");
	    				for(var i = 0; i < depart.length; i++){
	    					var value = $.trim($(depart[i]).val());
	    					var node = treeObj.getNodesByParam("id", value, null);
	    					if(node != null && !node[0].isParent){
	    						treeObj.checkNode(node[0], true, true);
	    					}
	    				}	
	    			}
	    		});
	    		
			}
		});		
	}
}

function BINOLMBRPT12() {};
BINOLMBRPT12.prototype = {
		// 选择购买产品弹出框
	    "openPrtDialog" :function(targetId, mainType, prtId) {
	    	$("#errorMessage").empty();
	    	// 表单验证
			if(!$("#mainForm").valid()) {
				return false;
			}
			var params = "";
	    	params += "mainType=" + mainType;
	    	params += "&" + $("#startDate").serialize();
	    	params += "&" + $("#endDate").serialize();
	    	var nameKey;
	    	if (mainType == "2") {
				if ($("#mainPrtId").length > 0) {
					params += "&" + $("#mainPrtId").serialize();
				}
				if ($("#mainCateId").length > 0) {
					params += "&" + $("#mainCateId").serialize();
				}
				nameKey = 'jointPrtName';
			} else {
				nameKey = 'mainPrtName';
			}
	    	var option = {
	    			targetId : targetId,
	    			dialogId : 'rptProductDialog',
	    			popTableId : 'rpt_prt_dataTable',
	    			mode : 2,
	    			params: params,
	    			getHtmlFun:function(info){
	    		       	 var html = '<tr><td><span class="list_normal">';
	   	       			html += '<span class="text" style="line-height:19px;">' + info.prtName +'</span>';
	   	       			html += '<span class="close" style="margin: 4px 0 0 6px;" onclick="binolmbrpt12.removePrt(this);return false;"><span class="ui-icon ui-icon-close"></span></span>';
	   	       			html += '<input type="hidden" name="prtVendorId" value="' + info.productVendorId + '"/>';
	   	       			html += '<input type="hidden" id="' +  prtId + '" name="' + prtId + '" value="' + info.productVendorId + '"/>';
	   	       			html += '<input type="hidden" name="' + nameKey + '" value="' + info.prtName + '"/>';
	   	       			html += '</span></td></tr>';
	   	       			return html;
	   			   	},
				   	click : function() {
				   		if (mainType == "1") {
				   			$('#jointPrt').empty();
				   		}
				   	}
	    	}
	    	var $dialog = $('#rptProductDialog');
	    	var url = $("#rptInitPrtDialogUrl").attr("href");
	    	if($dialog.length == 0){
	    		$.ajax({
	    	        url: url, 
	    	        type: 'post',
	    	        success: function(msg){
	    				$("body").append(msg);
	    				binolmbrpt12.popRptProductDialog(option);
	    			}			
	    		});
	    	} else {
	    		binolmbrpt12.popRptProductDialog(option);
	    	}
		},
		"popRptProductDialog" : function(option) {
			var index = 90;
			if(popDataTable_global.rptPrtDialogHtml == ""){
				popDataTable_global.rptPrtDialogHtml = $("#rptProductDialog").html();
			}
			var dialogBody = popDataTable_global.rptPrtDialogHtml;
			// 目标区内容加载到缓存区
			exchangeHtml(option);
			var tableSetting = {
					 // 表格ID
					 tableId : '#rpt_prt_dataTable',
					 // 一页显示页数
					 iDisplayLength:10,
					 // 数据URL
					 url : $("#rptPrtSearchUrl").text() + "?" + option.params,
					 // 表格列属性设置
					 aoColumns : [  { "sName": "checkbox","bSortable": false}, 	// 0
									{ "sName": "unitCode","bSortable": false},                     // 1
									{ "sName": "barCode","bSortable": false},                      // 2
									{ "sName": "nameTotal","bSortable": false},                    // 4
									{ "sName": "primaryCategoryBig","bSortable": false},           // 5
									{ "sName": "quantity","bSortable": false}         // 6
									], 
					// 表格默认排序
					aaSorting : [[ 5, "desc" ]],
					index:index,
					colVisFlag: false,
					// 横向滚动条出现的临界宽度
					//sScrollX : "100%",
					// pop窗口弹出后回调函数
					fnDrawCallback:function(){
							initPopInput(option);
							bindPopInput(option);
					}
			};
			// 调用获取表格函数
			getTable(tableSetting);
			
			var dialogSetting = {
					width:'900',
					title:"产品选择", 
					close: function(event, ui) {
						closeCherryDialog(option.dialogId,dialogBody);				
						oTableArr[index]= null;
					}
			};
			// 设置Dialog共通属性
			setDialogSetting($("#rptProductDialog"),dialogSetting,option);
		},
		// 删除分类
		"removePrt":function(_this){
			// 分类块
			var $obj = $(_this).parent().parent().parent();
			if ($obj.parent().attr("id") == "mainPrt") {
				$("#jointPrt").empty();
			}
			$obj.remove();
		},
		
		
		
		
		// 选择购买产品弹出框
	    "openCateDialog" :function(targetId, mainType, cateId) {
	    	$("#errorMessage").empty();
	    	// 表单验证
			if(!$("#mainForm").valid()) {
				return false;
			}
			var params = "";
	    	params += "mainType=" + mainType;
	    	params += "&" + $("#startDate").serialize();
	    	params += "&" + $("#endDate").serialize();
	    	var nameKey;
	    	if (mainType == "2") {
				if ($("#mainPrtId").length > 0) {
					params += "&" + $("#mainPrtId").serialize();
				}
				if ($("#mainCateId").length > 0) {
					params += "&" + $("#mainCateId").serialize();
				}
				nameKey = 'jointCateName';
			} else {
				nameKey = 'mainCateName';
			}
	    	var option = {
	    			targetId : targetId,
	    			dialogId : 'rptCateDialog',
	    			popTableId : 'rpt_prtCate_Table',
	    			mode : 2,
	    			params: params,
	    			getHtmlFun:function(info){
	    				var html = '<tr><td><span class="list_normal">';
	   	       			html += '<span class="text" style="line-height:19px;">' + info.cateName +'</span>';
	   	       			html += '<span class="close" style="margin: 4px 0 0 6px;" onclick="binolmbrpt12.removePrt(this);return false;"><span class="ui-icon ui-icon-close"></span></span>';
	   	       			html += '<input type="hidden" name="cateValId" value="' + info.cateValId + '"/>';
	   	       			html += '<input type="hidden" id="' +  cateId + '" name="' + cateId + '" value="' + info.cateValId + '"/>';
	   	       			html += '<input type="hidden" name="' + nameKey + '" value="' + info.cateName + '"/>';
	   	       			html += '</span></td></tr>';
	   	       			return html;
	   			   	},
				   	click : function() {
				   		if (mainType == "1") {
				   			$('#jointPrt').empty();
				   		}
				   	}
	    	}
	    	var $dialog = $('#rptCateDialog');
	    	var url = $("#rptInitCateDialogUrl").attr("href");
	    	if($dialog.length == 0){
	    		$.ajax({
	    	        url: url, 
	    	        type: 'post',
	    	        success: function(msg){
	    				$("body").append(msg);
	    				binolmbrpt12.popRptCateDialog(option);
	    			}			
	    		});
	    	} else {
	    		binolmbrpt12.popRptCateDialog(option);
	    	}
		},
		"popRptCateDialog" : function(option) {
			var index = 89;
			if(popDataTable_global.rptCateDialogHtml == ""){
				popDataTable_global.rptCateDialogHtml = $("#rptCateDialog").html();
			}
			var dialogBody = popDataTable_global.rptCateDialogHtml;
			// 目标区内容加载到缓存区
			exchangeHtml(option);
			var tableSetting = {
					 // 表格ID
					 tableId : '#rpt_prtCate_Table',
					 // 一页显示页数
					 iDisplayLength:10,
					 // 数据URL
					 url : $("#rptCateSearchUrl").text() + "?" + option.params,
					 // 表格列属性设置
					 aoColumns : [  { "sName": "checkbox","bSortable": false}, 	// 0
									{ "sName": "primaryCategoryBig","bSortable": false},                     // 1
									{ "sName": "cateCode","bSortable": false},                      // 2
									{ "sName": "quantity","bSortable": false}         // 6
									], 
					// 表格默认排序
					aaSorting : [[ 3, "desc" ]],
					index:index,
					colVisFlag: false,
					// 横向滚动条出现的临界宽度
					//sScrollX : "100%",
					// pop窗口弹出后回调函数
					fnDrawCallback:function(){
							initPopInput(option);
							bindPopInput(option);
					}
			};
			// 调用获取表格函数
			getTable(tableSetting);
			
			var dialogSetting = {
					width:'900',
					title:"产品大类选择", 
					close: function(event, ui) {
						closeCherryDialog(option.dialogId,dialogBody);				
						oTableArr[index]= null;
					}
			};
			// 设置Dialog共通属性
			setDialogSetting($("#rptCateDialog"),dialogSetting,option);
		},
		"openJointPrtCateDialog" : function() {
			if ($("#mainCateId").length > 0) {
				binolmbrpt12.openCateDialog('jointPrt','2','jointCateId');
			} else {
				binolmbrpt12.openPrtDialog('jointPrt','2','jointPrtId');
			}
		},
		"searchList" : function() {
			// 表单验证
			if(!$("#mainForm").valid()) {
				return false;
			}
			var url = $("#rptSearchMemUrl").attr("href");
			var params = $("#mainForm").serialize();
			if(params != null && params != "") {
				url = url + "?" + params;
			}
			$("#resultList").show();
			// 表格设置
			var tableSetting = {
					 // 表格ID
					 tableId: '#resultDataTable',
					 // 数据URL
					 url: url,
					 // 表格默认排序
					 aaSorting: [[ 8, "desc" ]],
					 // 表格列属性设置
					 aoColumns: [
					             { "sName": "name", "sWidth": "25%"},
					             { "sName": "memCode", "sWidth": "10%"},
					             { "sName": "mobilePhone", "sWidth": "10%"},
					             { "sName": "birthDay", "sWidth": "10%"},
					             { "sName": "levelName", "sWidth": "10%"},
					             { "sName": "totalPoint", "sWidth": "10%"},
					             { "sName": "departCode", "sWidth": "10%"},
					             { "sName": "departName", "sWidth": "15%"},
					             { "sName": "jointDate", "sWidth": "10%"}
					             ],
					// 横向滚动条出现的临界宽度
					aiExclude :[0, 1],
					sScrollX: "100%"
			};
			// 调用获取表格函数
			getTable(tableSetting);
		},
		
		"exportFile" : function(exportType) {
			if($("#resultDataTable").find(".dataTables_empty:visible").length==0) {
				var url = $("#rptExportMemUrl").attr("href");
				if("Excel" == exportType) {
					url += "?exportType=" + exportType;
					$("#mainForm").attr("action", url);
					$("#mainForm").submit();
				} else {
					var params = $("#mainForm").serialize();
					params += "&exportType=" + exportType;
					exportReport({
		        		exportUrl:url,
		        		exportParam:params            	
		        	});
				}
		    }
			return false;
		}
}

var binolmbrpt12 =  new BINOLMBRPT12();

$(function(){
	$('#startDate').cherryDate({
		beforeShow: function(input){
			var value = $('#endDate').val();
			return [value,'maxDate'];
		}
	});
	$('#endDate').cherryDate({
		beforeShow: function(input){
			var value = $('#startDate').val();
			return [value,'minDate'];
		}
	});
	
	// 表单验证初期化
	cherryValidate({
		formId: 'mainForm',
		rules: {
			startDate: {required: true, dateValid: true},
			endDate: {required: true, dateValid: true}
		}
	});
});
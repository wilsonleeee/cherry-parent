var BINOLCTRPT05_GLOBAL = function () {

};

BINOLCTRPT05_GLOBAL.prototype = {
	"getError" : function(obj,errorMsg){
		var $parent = $(obj).parent();
		$parent.removeClass('error');
		$parent.find("#errorText").remove();
		$parent.addClass("error");
		$parent.append('<span class="ui-icon icon-error tooltip-trigger-error" id="errorText"></span>');
		$parent.find('#errorText').attr("title",'error|'+errorMsg);
		$parent.find('#errorText').cluetip({
	    	splitTitle: '|',
		    width: 150,
		    cluezIndex: 20000,
		    tracking: true,
		    cluetipClass: 'error', 
		    arrows: true, 
		    dropShadow: false,
		    hoverIntent: false,
		    sticky: false,
		    mouseOutClose: true,
		    closePosition: 'title',
		    closeText: '<span class="ui-icon ui-icon-close"></span>'
		});
	},
	//查询条件验证
	"validateSearch" : function(){
		//清除错误信息
		$('#selectDiv').find('span').removeClass('error')
		$('#selectDiv').find("#errorText").remove();
		var $form = $('#mainForm');
		var queryType = $('#queryType').val();
		if(queryType == '2'){
			var startDays = $('#startDays').val();
			var endDays = $('#endDays').val();
			//var reg = /^[0-9]*[1-9][0-9]*$/;
			var reg = /^[0-9]*$/;
			if(startDays == ''){
				this.getError('#startDays', $('#errorMsg1').text());
				return false;
			}
			if(!reg.test(startDays)){
				this.getError('#startDays', $('#errorMsg2').text());
				return false;
			}
			if(endDays != ''){
				if(!reg.test(endDays)){
					this.getError('#endDays', $('#errorMsg2').text());
					return false;
				}
				if(parseInt(endDays) < parseInt(startDays)){
					this.getError('#endDays', $('#errorMsg3').text());
					return false;
				}
			}
		}else{
			cherryValidate({//form表单验证
				formId: "mainForm",		
				rules: {
					startTime:{required:true,dateValid: true},
					endTime:{dateValid: true}
				}		
			});
			if(!$form.valid()){
				return false;
			}
		}
		return true;
	},	
	// 效果统计查询
	"search" : function() {
		var $form = $('#mainForm');
		$form.find(":input").each(function(){
			$(this).val($.trim(this.value));
		});
		if(!this.validateSearch()){
			return false;
		}
		//清除提示信息
		$('#actionResultDisplay').empty();
		$('#searchResult').show();
		//表格列属性
		var aoColumnsArr = [
		                    {"sName" : "RowNumber", "sWidth": "3%", "bSortable": false},
		                    {"sName" : "regionName", "bVisible" : false,  "sWidth" : "10%"},
		                    {"sName" : "departName", "bVisible" : false, "sWidth" : "15%"},
		                    {"sName" : "counterName", "sWidth" : "15%"},
		                    {"sName" : "sendNumber", "sWidth" : "5%"},
		                    {"sName" : "saleNumber", "sWidth" : "5%"},
		                    {"sName" : "rate", "sWidth" : "5%"},
		                    {"sName" : "billQuantity", "sWidth" : "5%"},
		                    {"sName" : "amount", "sWidth" : "5%"},
		                    {"sName" : "quantity", "sWidth" : "5%"},
		                    {"sName" : "act", "sWidth" : "15%","bSortable" : false,"sClass":"center"}
		                    ];
		
		var url = $("#searchUrl").attr("href");
		var params = $form.serialize() + "&csrftoken=" + parentTokenVal()+"&"+getRangeParams();
		url = url + "?" + params;
		// 表格设置
		var tableSetting = {
			// 表格ID
			tableId : "#dataTable",
			// 数据URL
			url : url,
			// 排序列
			aaSorting : [ [ 1, "desc" ] ],
			// 表格列属性设置
			aoColumns : aoColumnsArr,
			// 不可设置显示或隐藏的列
			aiExclude : [0,3,9 ],
			// 横向滚动条出现的临界宽度
			sScrollX : "100%",
			//表格索引
			index : 0,
			// 固定列数
			//fixedColumns : 4,
			fnDrawCallback : function() {
				$('#dataTable').find("a.description").cluetip({
					splitTitle: '|',
				    width: 300,
				    height: 'auto',
				    cluetipClass: 'default', 
				    cursor: 'pointer',
				    showTitle: false
				});
			},
			callbackFun : function(msg) {
				var $msg = $("<div></div>").html(msg);
		 		var $totalInfo = $("#analysisTotalInfo",$msg);
		 		if($totalInfo.length > 0){
		 			$("#analysisTotalInfoSpan").html($totalInfo.html());
		 		}else{
		 			$("#analysisTotalInfoSpan").empty();
			 	}
			}
		};
		// 调用获取表格函数
		getTable(tableSetting);
	},
	"changeQueryType" : function(){
		var index = $("#queryType").val();
		$("span[id*='selectDiv_']").hide();
		$("#selectDiv_"+index).show();
	},
	"popJoinDetailDialog":function(url){
    	if($("#popJoinDetailDialog").length == 0) {
    		$("body").append('<div style="display:none" id="popJoinDetailDialog"></div>');
    	} else {
    		$("#popJoinDetailDialog").empty();
    	}
    	var initCallback = function(msg){
	    	var dialogSetting = {
	    			dialogInit: "#popJoinDetailDialog",
	    			text: msg,
	    			width: 	800,
	    			height: 420,
	    			title: 	$("#joinDialogTitle").text(),
	    			confirm: $("#confirmBtn").text(),
	    			confirmEvent: function(){
	    				removeDialog("#popJoinDetailDialog");
	    				oTableArr[1]=null;
	    			},
	    			closeEvent: function(){
	    				removeDialog("#popJoinDetailDialog");
	    				oTableArr[1]=null;
	    			}
	    	};
	    	openDialog(dialogSetting);
	    	$("a.description").cluetip({
				splitTitle: '|',
			    width: '300',
			    height: 'auto',
			    cluetipClass: 'default',
			    cursor:'pointer',
			    arrows: false, 
			    dropShadow: false
			});
	    	BINOLCTRPT05.searchJoinDetail();
	    	
    	}
    	cherryAjaxRequest({
    		url: url,
    		param: null,
    		callback: initCallback
    	});
    },
    // 参与会员明细查询
	"searchJoinDetail" : function() {
		//清除提示信息
		$('#actionResultDisplay').empty();
		var $form = $('#mainForm');
		//表格列属性
		var aoColumnsArr = [
		                    {"sName" : "RowNumber", "sWidth": "3%", "bSortable": false},
		                    {"sName" : "memCode",  "sWidth" : "15%"},
		                    {"sName" : "memberName",  "sWidth" : "15%"},
		                    {"sName" : "mobilephone",  "sWidth" : "15%"},
		                    {"sName" : "billQuantity", "sWidth" : "15%"},
		                    {"sName" : "amount", "sWidth" : "15%"},
		                    {"sName" : "quantity", "sWidth" : "15%"}
		                    ];
		
		var url = $("#searchJoinDetailUrl").attr("href");
		
		var params = $form.serialize() + "&csrftoken=" + parentTokenVal()+"&organizationId="+$("#organizationId").val();
		url = url + "?" + params;
		// 表格设置
		var tableSetting = {
			// 表格ID
			tableId : "#joinDetailTable",
			// 数据URL
			url : url,
			// 排序列
			aaSorting : [ [ 1, "desc" ] ],
			colVisFlag: false,
			// 表格列属性设置
			aoColumns : aoColumnsArr,
			// 横向滚动条出现的临界宽度
			sScrollX : "100%",
			// 默认显示行数
			iDisplayLength : 10,
			//表格索引
			index : 1,
			fnDrawCallback : function() {

			}
		};
		// 调用获取表格函数
		getTable(tableSetting);
	},
	"popSendDetailDialog":function(url){
    	if($("#popSendDetailDialog").length == 0) {
    		$("body").append('<div style="display:none" id="popSendDetailDialog"></div>');
    	} else {
    		$("#popSendDetailDialog").empty();
    	}
    	var initCallback = function(msg){
	    	var dialogSetting = {
	    			dialogInit: "#popSendDetailDialog",
	    			text: msg,
	    			width: 	800,
	    			height: 420,
	    			title: 	$("#sendDialogTitle").text(),
	    			confirm: $("#confirmBtn").text(),
	    			confirmEvent: function(){
	    				removeDialog("#popSendDetailDialog");
	    				oTableArr[2]=null;
	    			},
	    			closeEvent: function(){
	    				removeDialog("#popSendDetailDialog");
	    				oTableArr[2]=null;
	    			}
	    	};
	    	openDialog(dialogSetting);
	    	BINOLCTRPT05.searchSendDetail();
	    	
    	}
    	cherryAjaxRequest({
    		url: url,
    		param: null,
    		callback: initCallback
    	});
    },
    // 参与会员明细查询
	"searchSendDetail" : function() {
		//清除提示信息
		$('#actionResultDisplay').empty();
		var $form = $('#mainForm');
		//表格列属性
		var aoColumnsArr = [
		                    {"sName" : "RowNumber", "sWidth": "3%", "bSortable": false},
		                    {"sName" : "memCode",  "sWidth" : "15%"},
		                    {"sName" : "memName",  "sWidth" : "15%"},
		                    {"sName" : "mobilephone",  "sWidth" : "15%"},
		                    {"sName" : "message", "sWidth" : "15%"},
		                    {"sName" : "sendTime", "sWidth" : "15%"},
		                    {"sName" : "couponCode", "sWidth" : "15%"}
		                    ];
		
		var url = $("#searchSendDetailUrl").attr("href");
		var params = $form.serialize() + "&csrftoken=" + parentTokenVal()+"&organizationId="+$("#organizationId").val();
		url = url + "?" + params;
		// 表格设置
		var tableSetting = {
			// 表格ID
			tableId : "#sendDetailTable",
			// 数据URL
			url : url,
			// 排序列
			aaSorting : [ [ 1, "desc" ] ],
			colVisFlag: false,
			// 表格列属性设置
			aoColumns : aoColumnsArr,
			// 横向滚动条出现的临界宽度
			sScrollX : "100%",
			// 默认显示行数
			iDisplayLength : 10,
			//表格索引
			index : 2,
			fnDrawCallback : function(msg) {
				$("a.description").cluetip({
					splitTitle: '|',
				    width: '300',
				    height: 'auto',
				    cluetipClass: 'default',
				    cursor:'pointer',
				    arrows: false, 
				    dropShadow: false
				});
			}
		};
		// 调用获取表格函数
		getTable(tableSetting);
	},
	//会员销售明细窗
	"popSaleDetailDialog":function(thisObj){
		var url = thisObj;
    	if($("#popSaleDetailDialog").length == 0) {
    		$("body").append('<div style="display:none" id="popSaleDetailDialog"></div>');
    	} else {
    		$("#popSaleDetailDialog").empty();
    	}
    	var initCallback = function(msg){
	    	var dialogSetting = {
	    			dialogInit: "#popSaleDetailDialog",
	    			text: msg,
	    			width: 	900,
	    			height: 420,
	    			resizable: true,
	    			title: 	$("#saleDialogTitle").text(),
	    			confirm: $("#confirmBtn").text(),
	    			confirmEvent: function(){
	    				removeDialog("#popSaleDetailDialog");
	    				oTableArr[3]=null;
	    			},
	    			closeEvent: function(){
	    				removeDialog("#popSaleDetailDialog");
	    				oTableArr[3]=null;
	    			}
	    	};
	    	openDialog(dialogSetting);
	    	$("a.description").cluetip({
				splitTitle: '|',
			    width: '300',
			    height: 'auto',
			    cluetipClass: 'default',
			    cursor:'pointer',
			    arrows: false, 
			    dropShadow: false
			});
	    	BINOLCTRPT05.searchSaleDetail();
	    	//统计信息
	    	$("#saleTotalInfoSpan").find("#joinTotalNumber").html($(thisObj).parent().parent().find("#joinNumber").html());
	    	$("#saleTotalInfoSpan").find("#saleAmountTotal").html($(thisObj).parent().parent().find("#saleAmount").html());
	    	$("#saleTotalInfoSpan").find("#saleQuantityTotal").html($(thisObj).parent().parent().find("#saleQuantity").html());
    	}
    	cherryAjaxRequest({
    		url: url,
    		param: null,
    		callback: initCallback
    	});
    },
    //会员销售明细查询
	"searchSaleDetail" : function() {
		//清除提示信息
		$('#actionResultDisplay').empty();
		var $form = $('#mainForm');
		//表格列属性
		var aoColumnsArr = [
		                    {"sName" : "RowNumber", "sWidth": "3%", "bSortable": false},
		                    {"sName" : "memCode",  "sWidth" : "15%"},
		                    {"sName" : "memberName",  "sWidth" : "15%"},
		                    {"sName" : "mobilephone",  "sWidth" : "15%"},
		                    {"sName" : "billCode", "sWidth" : "20%"},
		                    {"sName" : "amount", "sWidth" : "15%"},
		                    {"sName" : "quantity", "sWidth" : "15%"},
		                    {"sName" : "saleDate", "sWidth" : "15%"}     
		                    ];
		
		var url = $("#searchSaleDetailUrl").attr("href");
		
		var params = $form.serialize() + "&csrftoken=" + parentTokenVal()+"&organizationId="+$("#organizationId").val();
		url = url + "?" + params;
		// 表格设置
		var tableSetting = {
			// 表格ID
			tableId : "#saleDetailTable",
			// 数据URL
			url : url,
			// 排序列
			aaSorting : [ [ 1, "desc" ] ],
			colVisFlag: false,
			// 表格列属性设置
			aoColumns : aoColumnsArr,
			// 横向滚动条出现的临界宽度
			sScrollX : "100%",
			// 默认显示行数
			iDisplayLength : 10,
			//表格索引
			index : 3,
			fnDrawCallback : function() {

			}
		};
		// 调用获取表格函数
		getTable(tableSetting);
	},
	"exportExcel" : function(url,exportFormat){
		//清除提示信息
		$('#actionResultDisplay').empty();
		var params= $("#mainForm").serialize();
        params = params + "&" +getSerializeToken()+ "&exportFormat=" +exportFormat + "&"+getRangeParams();
        //明细导出时
        if(exportFormat == '3' || exportFormat == '4' || exportFormat == '5' || exportFormat == '6'){
        	if($('#joinTotal').text() == '0'){
        		return false;
        	}
        	var organizationId = $('#organizationId').val();
        	if(organizationId != undefined){
        		organizationId = $.trim(organizationId);
        		if(organizationId != null && organizationId !=''){
        			params = params + "&organizationId=" + organizationId;
        		}else{
        			params = params + "&organizationId=0";
        		}
        	}
        }
		//无数据不导出
		if($(".dataTables_empty:visible").length==0){
			if(exportFormat == '1' || exportFormat == '3' || exportFormat == '5'){
				var that = this;
				that.needUnlock=false;
		        url = url + "?" +params;
		        document.location.href = url;
		        that.needUnlock=true;
			}else{
				exportReport({
					exportUrl: url,
					exportParam: params
				});
			}
		}
	}
}

var BINOLCTRPT05 = new BINOLCTRPT05_GLOBAL();

window.onbeforeunload = function(){
	if (window.opener) {
		if(BINOLCTRPT05.needUnlock){
			// 解除父页面锁定
			window.opener.unlockParentWindow();
		}
	}
};

$(document).ready(function() {
	if (window.opener) {
		window.opener.lockParentWindow();
	}
	BINOLCTRPT05.changeQueryType();
	BINOLCTRPT05.search();
	$("#startTime").cherryDate({
		minDate : $("#sendTime").val(),
		beforeShow: function(input){
			var value = $("#endTime").val();
			return [value, "maxDate"];
		}
	});
	$("#endTime").cherryDate({
		beforeShow: function(input){
			var value = $("#selectDiv_1").find("input[name='startTime']").val();
			return [value, "minDate"];
		}
	});
	BINOLCTRPT05.needUnlock = true;
});

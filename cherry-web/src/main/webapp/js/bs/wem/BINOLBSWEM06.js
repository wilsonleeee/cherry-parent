function BINOLBSWEM06() {};

BINOLBSWEM06.prototype = {	
	/**
	 * 清理执行结果信息
	 */
	"clearActionHtml" : function() {
		$("#errorMessage").empty();
		$("#actionResultDisplay").empty();
		$("#errorMessageTemp").hide();
	},
	"profitRebateReset" : function(url){
		BINOLBSWEM06.clearActionHtml();
		if($('#dataTable').find(":checkbox[checked]").length<1){
			$("#errorMessageTemp").show();
			return false;
		}
		var saleRecordCodeList = new Array();
		$($('#dataTable').find(":checkbox[checked]")).each(function(){
			var saleRecordCode = $(this).parent().find("[name='saleRecordCodeP']").val();
			var obj = {"saleRecordCode":saleRecordCode};
			saleRecordCodeList.push(obj);
		});
		saleRecordCodeList = JSON.stringify(saleRecordCodeList);
		var params = "saleRecordCodeList="+saleRecordCodeList;
		var title = $('#title').text();
		var text = $('#text').html();
		var callback = function() {
			if(oTableArr[0] != null)oTableArr[0].fnDraw();
		};
		var dialogSetting = {
			dialogInit: "#dialogInit",
			text:	text,
			width: 	500,
			height: 300,
			title: 	title,
			confirm: $("#dialogConfirm").text(),
			cancel: $("#dialogCancel").text(),
			confirmEvent: function(){BINOLBSWEM06.profitRebateReset1(url, params, callback);},
			cancelEvent: function(){removeDialog("#dialogInit");}
		};
		openDialog(dialogSetting);
	},
	// 重新分摊处理
	"profitRebateReset1" : function(url, param, delCallback) {
		var callback = function(msg) {
			$("#dialogInit").html(msg);
			removeDialog("#dialogInit");
			if(typeof(delCallback) == "function") {
				if($("#actionResultDiv").find("ul").find("li").html()==null){
					$("#actionResultDiv").find("ul").html($("#showError").html());
				}
				delCallback();
			}
		};
		cherryAjaxRequest({
			url: url,
			param: param,
			callback: callback
		});
	},
	"search" : function(){
		BINOLBSWEM06.clearActionHtml();
		// 查询参数序列化
		var params= $("#mainForm1").serialize();
		var url = $("#saleSearch_url").val();
		if(params != null && params != "") {
			url = url + "?" + params;
		}
		// 显示结果一览
		$("#section").show();
		// 表格设置
		var tableSetting = {
			// 表格ID
			tableId : '#dataTable',
			// 数据URL
			url : url,
			aaSorting : [[ 5, "desc" ]],
			// 表格列属性设置
			aoColumns : [
		             	{ "sName": "no","sWidth": "5%","bSortable": false},
		             	{ "sName": "saleRecordCode","sWidth": "10%","bVisible": false},
			            { "sName": "billCodePre","sWidth": "10%","bVisible": false},
			            { "sName": "billCode","sWidth": "10%"},
			            { "sName": "saleType","sWidth": "5%"},
			            { "sName": "saleTime","sWidth": "10%"},
			            { "sName": "employeeCode","sWidth": "10%"},
			            { "sName": "saleCount","sWidth": "10%"},
			            { "sName": "amount","sWidth": "5%"},
			            { "sName": "employeeName","sWidth": "10%"},
			            { "sName": "saleProfit","sWidth": "10%"},
						{ "sName": "channel","sClass":"center","sWidth": "5%"}
					],	
			// 不可设置显示或隐藏的列
			aiExclude :[0, 3],
			// 横向滚动条出现的临界宽度
			sScrollX : "100%"
		};
		// 调用获取表格函数
		getTable(tableSetting);
	},
	"checkSelectAll": function(checkbox){
		BINOLBSWEM06.clearActionHtml();
		$('#dataTable').find(":checkbox").prop("checked", $(checkbox).prop("checked"));
	},
	"checkSelect": function(checkbox){
		BINOLBSWEM06.clearActionHtml();
		if($(checkbox).prop("checked")) {
	        $(checkbox).prop("checked",true);
	    } else {
	        $(checkbox).prop("checked",false);
	    }
		if($('#dataTable').find(":checkbox[checked]").length==$('#dataTable >tbody >tr').length){
			$("#allSelect").prop("checked", true);
		}else {
			$("#allSelect").prop("checked", false);
		}
	}
};

var BINOLBSWEM06 =  new BINOLBSWEM06();
$(function(){
	BINOLBSWEM06.search();
});
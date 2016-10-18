var BINOLSTJCS11_global = function(){};

BINOLSTJCS11_global.prototype = {
	"delError": function(){
		$("actionResultDisplay").empty();
		$("#errorSpan2").empty();
		$("#errorDiv2").hide();
		$('#checkAll').prop("checked",false);
	},
	"search" : function(){
		BINOLSTJCS11.delError();
		var url = "BINOLSTJCS11_search";
		var params = $('#mainForm').serialize();
		url = url + "?" + params;
		var tableSetting = {
			tableId : '#dataTable',
			url : url,
			aaSorting : [[2, "desc"]],
			aoColumns : [{ "sName": "checkbox", "sWidth": "5%","bSortable": false,"sClass":"center"},
			             { "sName": "No", "sWidth": "5%","bSortable": false},
			             { "sName": "batchCode", "sWidth": "10%"},
			             { "sName": "departCode", "sWidth": "10%"},
			             { "sName": "departName", "sWidth": "10%"},
			             { "sName": "startTime", "sWidth": "15%"},
			             { "sName": "endTime", "sWidth": "15%"},
			             { "sName": "author", "sWidth": "10%"},
			             { "sName": "comments", "sWidth": "15%"},
			             { "sName": "validFlag", "sWidth": "5%","sClass":"center"}],
			// 横向滚动条出现的临界宽度
			sScrollX : "100%",
			// 不可设置显示或隐藏的列	
			aiExclude :[0,1,2],
			// 固定列数
			fixedColumns : 0,
		 };
		 // 调用获取表格函数
		 getTable(tableSetting);
		 $("#section").show();
	},
	// 选择记录
	"checkRecord": function (object, id) {
		var $id = $(id);
		if($(object).attr('id') == "checkAll") {
			if(object.checked) {
				$id.find(':checkbox').prop("checked",true);
			} else {
				$id.find(':checkbox').prop("checked",false);
			}
		} else {
			if($id.find(':checkbox:not([checked])').length == 0) {
				$('#checkAll').prop("checked",true);
			} else {
				$('#checkAll').prop("checked",false);
			}
		}
	},
	"disable": function(validFlag, url){
		BINOLSTJCS11.delError();
		if($("#dataTable input:checked").length == 0) {
			$("#errorSpan2").html($("#errorMsg5").html());
			$("#errorDiv2").show();
			return;
		}
		var params =  $('#dataTable :input[checked]').serialize()
						+ "&validFlag=" + validFlag 
						+ "&csrftoken=" + $("#csrftoken").val();
		cherryAjaxRequest({
			url: url,
			param: params,
			callback: function(msg){
				if(oTableArr[0] != null)oTableArr[0].fnDraw();
			}
		});
	}
}; 

var BINOLSTJCS11 = new BINOLSTJCS11_global();

$(document).ready(function(){
	var counterOption = {
			elementId: "departName",
			showNum: "20",
			selected: "name"
	}
	counterBinding(counterOption);
	BINOLSTJCS11.search();
});
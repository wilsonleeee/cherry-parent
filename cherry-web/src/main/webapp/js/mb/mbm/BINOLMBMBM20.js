function BINOLMBMBM20() {};

BINOLMBMBM20.prototype = {
	"searchMemWarnInfo" : function() {
		if(!$('#memWarnInfoForm').valid()) {
			return false;
		}
		var url = $("#memWarnInfoUrl").attr("href");
		url += "?" + getSerializeToken();
		var params= $("#memWarnInfoForm").serialize();
		if(params != null && params != "") {
			url = url + "&" + params;
		}
		$("#memWarnInfoDataTableDiv").show();
		// 表格设置
		var tableSetting = {
				 // 表格ID
				 tableId : '#memWarnInfoDataTable',
				 // 数据URL
				 url : url,
				 // 表格默认排序
				 aaSorting : [[ 5, "desc" ]],
				 // 表格列属性设置
				 aoColumns : [   { "sName": "id","sWidth": "3%","bSortable": false},
					             { "sName": "TradeType","sWidth": "10%"},
					             { "sName": "TradeNoIF","sWidth": "20%","bSortable": false},
					             { "sName": "ErrType","sWidth": "10%"},
					             { "sName": "ErrInfo","sWidth": "40%","bSortable": false},
					             { "sName": "PutTime","sWidth": "17%"}],
				// 横向滚动条出现的临界宽度
				sScrollX : "100%"
				//bAutoWidth : true,
		};
		// 调用获取表格函数
		getTable(tableSetting);
    },
    "showDetail":function(obj){
		var $this = $(obj);
		var dialogSetting = {
			dialogInit: "#dialogInit",
			width: 	430,
			height: 300,
			title: 	$("#showDetailTitle").text(),
			confirm: $("#dialogClose").text(),
			confirmEvent: function(){
				removeDialog("#dialogInit");
			}
		};
		openDialog(dialogSetting);
		$("#dialogContent").html($this.attr("rel"));
		$("#dialogInit").html($("#dialogDetail").html());
	},
	// 删除错误日志
	"delete" : function(url) {
		if($('#memWarnInfoDataTable :input[checked]').length == 0) {
			$("#errorMessage").html($("#errorMessageTemp").html());
			return false;
		}
		var param = $('#memWarnInfoDataTable :input[checked]').serialize();
		var callback = function() {
			if(oTableArr[0] != null)oTableArr[0].fnDraw();
		};
		binolmbmbm20_deleteConfirm(url, param, callback);
	}
};

var binolmbmbm20 =  new BINOLMBMBM20();

//删除确认
function binolmbmbm20_deleteConfirm(url, param, callback) {
	
	var title = $('#deleteTitle').text();
	var text = $('#deleteMessage').html();
	var dialogSetting = {
		dialogInit: "#dialogInit",
		text: text,
		width: 	500,
		height: 300,
		title: 	title,
		confirm: $("#dialogConfirm").text(),
		cancel: $("#dialogCancel").text(),
		confirmEvent: function(){binolmbmbm20_deleteHandle(url, param, callback);},
		cancelEvent: function(){removeDialog("#dialogInit");}
	};
	openDialog(dialogSetting);
}

// 删除处理
function binolmbmbm20_deleteHandle(url, param, delCallback) {
	var callback = function(msg) {
		$("#dialogInit").html(msg);
		if($("#errorMessageDiv").length > 0) {
			$("#errorMessageDiv").find("span").html($("#errorMessageDiv").next().text());
			$("#dialogInit").dialog( "option", {
				buttons: [{
					text: $("#dialogClose").text(),
				    click: function(){removeDialog("#dialogInit");}
				}]
			});
		} else {
			removeDialog("#dialogInit");
			if(typeof(delCallback) == "function") {
				delCallback();
			}
		}
	};
	cherryAjaxRequest({
		url: url,
		param: param,
		callback: callback
	});
}

// 选择记录
function binolmbmbm20_checkRecord(object, id) {
	$("#errorMessage").empty();
	var $id = $(id);
	if($(object).attr('id') == "checkAll") {
		if(object.checked) {
			$id.find(':checkbox').prop("checked",true);
		} else {
			$id.find(':checkbox').prop("checked",false);
		}
	} else {
		if($id.find(':checkbox:not([checked])').length == 0) {
			$id.parent().prev().find('#checkAll').prop("checked",true);
		} else {
			$id.parent().prev().find('#checkAll').prop("checked",false);
		}
	}
}

$(document).ready(function() {
	$('#putTimeStart').cherryDate({
		beforeShow: function(input){
			var value = $('#putTimeEnd').val();
			return [value,'maxDate'];
		}
	});
	$('#putTimeEnd').cherryDate({
		beforeShow: function(input){
			var value = $('#putTimeStart').val();
			return [value,'minDate'];
		}
	});
	// 表单验证初期化
	cherryValidate({
		formId: 'memWarnInfoForm',
		rules: {
			putTimeStart: {dateValid: true},
			putTimeEnd: {dateValid: true}
		}
	});
	binolmbmbm20.searchMemWarnInfo();
});
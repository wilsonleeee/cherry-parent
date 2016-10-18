
function BINOLMOWAT06() {};

BINOLMOWAT06.prototype = {
	// MQ消息错误日志查询
	"search" : function(){
		var url = $("#search_url").val();
		$('#mainForm :input').each(function(){
			$(this).val($.trim(this.value));
		});
		// 查询参数序列化
		var params= $("#mainForm").serialize();
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
			// 表格默认排序
			aaSorting : [[ 6, "desc" ]],
			// 表格列属性设置
			aoColumns : [{ "sName": "id","sWidth": "3%","bSortable": false},
			             { "sName": "TradeType","sWidth": "5%"},
			             { "sName": "TradeNoIF","sWidth": "17%","bSortable": false},
			             { "sName": "ErrType","sWidth": "7%"},
			             { "sName": "MessageBody","sWidth": "20%","bSortable": false},
			             { "sName": "ErrInfo","sWidth": "33%","bSortable": false},
			             { "sName": "PutTime","sWidth": "15%"}],
			// 横向滚动条出现的临界宽度
			sScrollX : "100%"
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
		if($('#dataTable :input[checked]').length == 0) {
			$("#errorMessage").html($("#errorMessageTemp").html());
			return false;
		}
		var param = $('#dataTable :input[checked]').serialize() + "&csrftoken=" + getTokenVal();
		var callback = function() {
			if(oTableArr[0] != null)oTableArr[0].fnDraw();
		};
		mowat06_deleteConfirm(url, param, callback);
	}
};

var binolmowat06 =  new BINOLMOWAT06();

//删除确认
function mowat06_deleteConfirm(url, param, callback) {
	
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
		confirmEvent: function(){mowat06_deleteHandle(url, param, callback);},
		cancelEvent: function(){removeDialog("#dialogInit");}
	};
	openDialog(dialogSetting);
}

// 删除处理
function mowat06_deleteHandle(url, param, delCallback) {
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
function mowat06_checkRecord(object, id) {
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
	// MQ消息错误日志查询
	binolmowat06.search();
});


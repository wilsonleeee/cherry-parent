
function BINOLPTUNQ02() {};

BINOLPTUNQ02.prototype = {	
		
	// 查询唯一码生成记录
	"search" : function(){
		
		// 表单验证
		if (!$('#mainForm').valid()) {
			return false;
		}
		var url = $("#searchUrlID").val();
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
			aaSorting : [[ 2, "asc" ]],
			// 表格列属性设置
			aoColumns : [
			        { "sName": "number"},
			        { "sName": "unitCode"},
			        { "sName": "barCode"},
					{ "sName": "nameTotal"},
					{ "sName": "pointUniqueCode"},
					{ "sName": "relUniqueCode"},
					{ "sName": "boxCode"},
					{ "sName": "createTime"},
					{ "sName": "primaryCategoryBig"},
					{ "sName": "primaryCategorySmall"},
					{ "sName": "activationStatus"},
					{ "sName": "useStatus"}],
			// 不可设置显示或隐藏的列
			aiExclude :[0, 1],
			// 横向滚动条出现的临界宽度
			sScrollX : "100%",
			index : 1
		};
	
		// 调用获取表格函数
		getTable(tableSetting);
	},
	
	// 查询唯一码初始化查询
	"initSearch" : function(){
		
		// 表单验证
		if (!$('#mainForm').valid()) {
			return false;
		}
		var url = $("#initSearchUrlID").val();
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
			aaSorting : [[ 2, "asc" ]],
			// 表格列属性设置
			aoColumns : [
			        { "sName": "number"},
			        { "sName": "unitCode"},
			        { "sName": "barCode"},
					{ "sName": "nameTotal"},
					{ "sName": "pointUniqueCode"},
					{ "sName": "relUniqueCode"},
					{ "sName": "boxCode"},
					{ "sName": "createTime"},
					{ "sName": "primaryCategoryBig"},
					{ "sName": "primaryCategorySmall"},
					{ "sName": "activationStatus"},
					{ "sName": "useStatus"}],
			// 不可设置显示或隐藏的列
			aiExclude :[0, 1],
			// 横向滚动条出现的临界宽度
			sScrollX : "100%",
			index : 1
		};
	
		// 调用获取表格函数
		getTable(tableSetting);
	},
	
	// 查询验证
    "searchCheck": function() {
    	
    	var that = this;
		var url = $("#searchCheckUrlID").val();
        var param = $('#mainForm').serialize();
		var callback = function(msg) {
			var msgJson = eval("("+msg+")");
			if(msgJson.errorCode == "1"){
				// 校验通过，执行查询显示数据
				BINOLPTUNQ02.search();
				// 通过校验时，移除相关没有通过校验的格式效果
				$('#errorText').remove();
				$("#pointUniqueCodeSrh").removeClass();
			}else{
				return false;
			}
		
		};
		cherryAjaxRequest({
			url: url,
			param: param,
			callback: callback
		});
    }

}
var BINOLPTUNQ02 =  new BINOLPTUNQ02();

$(document).ready(function() {
	
	// 初始化查询
	BINOLPTUNQ02.initSearch();
	
	
});
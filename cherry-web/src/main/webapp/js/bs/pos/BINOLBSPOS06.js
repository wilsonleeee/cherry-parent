
function BINOLBSPOS06() {};

BINOLBSPOS06.prototype = {
	// 岗位查询
	"search" : function(){
		var url = $("#posCategoryUrl").attr("href");
		var params= $("#posCategoryCherryForm").serialize();
		if(params != null && params != "") {
			url = url + "?" + params;
		}
		// 显示结果一览
		$("#posCategoryInfoId").removeClass("hide");
		// 表格设置
		var tableSetting = {
				 // 表格ID
				 tableId : '#dataTable',
				 // 数据URL
				 url : url,
				 // 表格默认排序
				 aaSorting : [[ 1, "asc" ]],
				 // 表格列属性设置
				 aoColumns : [  { "sName": "checkbox", "sWidth": "5%", "bSortable": false },
								{ "sName": "CategoryCode", "sWidth": "30%" },
								{ "sName": "CategoryName", "sWidth": "40%" },
								{ "sName": "Grade", "sWidth": "20%" },
								{ "sName": "ValidFlag", "sWidth": "5%", "bSortable": false }],
				// 不可设置显示或隐藏的列
				aiExclude :[],
				// 横向滚动条出现的临界宽度
				sScrollX : "100%",
				// 固定列数
				fixedColumns : 2
		};
		// 调用获取表格函数
		getTable(tableSetting);
	},
	// 岗位启用停用处理
	"editValidFlag" : function(flag, url) {
		var param = "";
		if($('#dataTable_Cloned :input[checked]').length == 0) {
			$("#errorMessage").html($("#errorMessageTemp").html());
			return false;
		}
		param = $('#dataTable_Cloned :input[checked]').nextAll().serialize();
		var callback = function() {
			if(oTableArr[0] != null)oTableArr[0].fnDraw();
		};
		bscom03_deleteConfirm(flag, url, param, callback);
	}
};

var binolbspos06 =  new BINOLBSPOS06();



$(document).ready(function() {
	// 岗位查询
	binolbspos06.search();
});


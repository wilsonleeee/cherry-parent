/*
 * 全局变量定义
 */
var binOLPTRPS02_global = {};

// 消息区分(true:显示消息，false:删除消息)
binOLPTRPS02_global.msgKbn = false;
// 前一次的业务类型
binOLPTRPS02_global.tradeTypePre = "";

var BINOLPTRPS02 = function () {
};
BINOLPTRPS02.prototype = {
		PTRPS02_dialogBody : "",

		"openProPopup":function(_this){
			var proPopParam = {
					thisObj : _this,
					  index : 1,
				  checkType : "radio",
				      modal : false,
				  autoClose : [],
				 dialogBody : BINOLPTRPS02.PTRPS02_dialogBody
			};
			popDataTableOfPrtInfo(proPopParam);
		}
};
var BINOLPTRPS02 = new BINOLPTRPS02();

$(document).ready(function() {
	// 产品popup初始化
	BINOLPTRPS02.PTRPS02_dialogBody = $('#productDialog').html();
	productBinding({elementId:"nameTotal",showNum:20});
	$("#productName").keyup(function(event){
		  $("#productVendorId").val("");
		  $("#productName").val("");
	});
	
	$("#productName").focus(function(){
		BINOLPTRPS02.openProPopup(this);
	});
//	// 表格列选中
//	$('thead th').live('click',function(){
//		$("th.sorting").removeClass('sorting');
//		$(this).addClass('sorting');
//	});
	$("#result_table table").attr("id", "dataTable");
	$("#result_list").append($("#result_table").html());
	$("#result_table table").removeAttr("id");
	// 部门名称组合框
	$('#organizationId').combobox({inputId: '#depart_text', btnId: '#depart_btn'});
	// 仓库名称组合框
	$('#inventSel').combobox({inputId: '#invent_text', btnId: '#invent_btn'});
	// 设置部门名称输入框的focus事件
	setInputVal('#depart_text', $("#RPS02_select").text());
	// 设置仓库名称输入框的focus事件
	setInputVal('#invent_text', $("#RPS02_select").text());
	cherryValidate({			
		formId: "mainForm",		
		rules: {
			startDate: {dateValid:true},	// 开始日期
			endDate: {dateValid:true}	// 结束日期
		}		
	});
} );

//用户查询
function search(){
	var $form = $('#mainForm');
	if (!$form.valid()) {
		return false;
	};
	
	var aoColumnsArr = [	{ "sName": "no","sWidth": "1%","bSortable": false}, 			// 0
							{ "sName": "deliverId","sWidth": "15%"},						// 1
							{ "sName": "departCode","sWidth": "10%"},						// 2							
							{ "sName": "departCodeReceive","sWidth": "10%"},				// 3
							{ "sName": "totalQuantity","sWidth": "8%","sClass":"alignRight"},// 4
							{ "sName": "totalAmount","sWidth": "12%","sClass":"alignRight"},// 5
							{ "sName": "deliverDate","bVisible": false,"sWidth": "10%"},						// 6
							{ "sName": "verifiedFlag","sWidth": "5%"}];					// 7
	var aiExcludeArr = [0,1];	
	var url = $("#searchUrl").attr("href");
	 // 查询参数序列化
	var params= $form.find("div.column").find(":input").serialize();
	 params = params + "&csrftoken=" +$("#csrftoken").val();
	 params = params + "&" +getRangeParams();
	 url = url + "?" + params;
	 // 显示结果一览
	 $("#section").show();
	 // 表格设置
	 var tableSetting = {
			 // 表格ID
			 tableId : '#dataTable',
			 // 数据URL
			 url : url,
			 // 排序列
			 aaSorting : [[1, "desc"]],
			 // 表格列属性设置
			 aoColumns : aoColumnsArr,
			// 不可设置显示或隐藏的列	
			aiExclude : aiExcludeArr,
			// 横向滚动条出现的临界宽度
			sScrollX : "100%",
			// 固定列数
			fixedColumns : 2,
			callbackFun : function (msg){
	 		var $msg = $("<div></div>").html(msg);
	 		var $headInfo = $("#headInfo",$msg);
	 		if($headInfo.length > 0){
	 			$("#headInfo").html($headInfo.html());
	 		}else{
	 			$("#headInfo").empty();
		 	}
	 	}
	 };
	 // 调用获取表格函数
	 getTable(tableSetting);
}

function selectProduct () {
	var value = $("#dataTableBody").find(":input[checked]").val();
	if(value != undefined && null != value){
		var selectedObj = window.JSON2.parse(value);
		$("#productName").val(selectedObj.nameTotal);
		$("#productVendorId").val(selectedObj.productVendorId);
	}
	closeCherryDialog('productDialog',BINOLPTRPS02.PTRPS02_dialogBody);	
	oTableArr[1]= null;	
}
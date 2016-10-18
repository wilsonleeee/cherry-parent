var BINOLPTRPS01 = function () {
    
};

BINOLPTRPS01.prototype = {

//	PTRPS01_dialogBody : "",

//	"openProPopup":function(_this){
//		var proPopParam = {
//				thisObj : _this,
//				  index : 1,
//			  checkType : "radio",
//			      modal : false,
//			  autoClose : [],
//			 dialogBody : BINOLPTRPS01.PTRPS01_dialogBody
//		};
//		popDataTableOfPrtInfo(proPopParam);
//	},

//用户查询
"search":function(){
	if (!$('#mainForm').valid()) {
		return false;
	};
	$("#mainForm").find(":input").each(function() {
		$(this).val($.trim(this.value));
	});
	 var url = $("#searchUrl").attr("href");
	 // 查询参数序列化
	 var params= $("#mainForm").find("div.column").find(":input").serialize();
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
			 // 表格默认排序
			 aaSorting : [[ 1, "desc" ]],
			 // 表格列属性设置
			 aoColumns : [	{ "sName": "no","sWidth": "1%","bSortable": false}, 	// 0
							{ "sName": "stockTakingId","sWidth": "15%"},			// 1
							{ "sName": "departCode","sWidth": "15%"},				// 2
							{ "sName": "inventoryName","bVisible": false,"sWidth": "15%"},			// 3
							{ "sName": "summQuantity","bVisible": false,"sWidth": "8%","sClass":"alignRight"},				// 4
							{ "sName": "summAmount","sWidth": "8%","sClass":"alignRight"},				// 5
							{ "sName": "takingType","sWidth": "10%"},				// 6
							{ "sName": "date","sWidth": "10%"},			            // 7
							{ "sName": "verifiedFlag","bVisible": false,"sWidth": "5%"},				// 8
							{ "sName": "employeeName","bVisible": false,"sWidth": "10%"}],			// 9
							
			// 不可设置显示或隐藏的列	
			aiExclude :[0, 1],
			// 横向滚动条出现的临界宽度
			sScrollX : "100%",
			// 滚动体的宽度
			sScrollXInner:"",
			// 固定列数
			fixedColumns : 2,
			// html转json前回调函数
			callbackFun: function(msg){
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
};
var BINOLPTRPS01 = new BINOLPTRPS01();

$(document).ready(function() {
	// 产品选择绑定
	productBinding({elementId:"productName",showNum:20});
	
//	// 产品popup初始化
//	BINOLPTRPS01.PTRPS01_dialogBody = $('#productDialog').html();
	
//	$("#productName").keyup(function(event){
//		  $("#prtVendorId").val("");
//		  $("#productName").val("");
//	});
	
//	$("#productName").focus(function(){
//		BINOLPTRPS01.openProPopup(this);
//	});
	
//	// 表格列选中
//	$('thead th').live('click',function(){
//		$("th.sorting").removeClass('sorting');
//		$(this).addClass('sorting');
//	});
	cherryValidate({			
		formId: "mainForm",		
		rules: {
			startDate: {dateValid:true},	// 开始日期
			endDate: {dateValid:true}	// 结束日期
		}		
	});
	//BINOLPTRPS01.search();
} );

//function selectProduct () {		
//	var value = $("#dataTableBody").find(":input[checked]").val();
//	if(value != undefined && null != value){
//		var vals = value.split("_");
//		$("#productName").val(vals[2]);
//		$("#prtVendorId").val(vals[4]);
//	}
//	closeCherryDialog('productDialog',BINOLPTRPS01.PTRPS01_dialogBody);	
//	oTableArr[1]= null;	
//}
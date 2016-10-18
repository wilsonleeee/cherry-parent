var BINOLPTRPS06 = function () {
};
BINOLPTRPS06.prototype = {
	PTRPS06_dialogBody : "",

	"openProPopup":function(_this){
		var proPopParam = {
				thisObj : _this,
				  index : 1,
			  checkType : "radio",
			      modal : false,
			  autoClose : [],
			 dialogBody : BINOLPTRPS06.PTRPS06_dialogBody
		};
		popDataTableOfPrtInfo(proPopParam);
	}
};
var BINOLPTRPS06 = new BINOLPTRPS06();

$(function(){
	// 产品popup初始化
	BINOLPTRPS06.PTRPS06_dialogBody = $('#productDialog').html();
	productBinding({elementId:"nameTotal",showNum:20});
	$("#productName").keyup(function(event){
		  $("#productVendorId").val("");
		  $("#productName").val("");
	});
	
	$("#productName").focus(function(){
		BINOLPTRPS06.openProPopup(this);
	});
	
	$('#orgId').combobox({inputId: '#org_text', btnId: '#org_btn'});
	setInputVal('#org_text', $("#selectAll").text());
	// 表单验证配置
	cherryValidate({						
		formId: 'mainForm',
		rules: {
			startDate: {dateValid: true},
			endDate: {dateValid: true}
		}
	});
});
// 查询
function search(url){
	var $form = $("#mainForm");
	// 表单验证
	if(!$form.valid()){
		return;
	}
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
			 aoColumns : [	{ "sName": "no","sWidth": "5%","bSortable": false}, 		// 0
							{ "sName": "proAllocationId","sWidth": "20%"},				// 1
							{ "sName": "bin_OrganizationIDIn","sWidth": "10%"},			// 2
							{ "sName": "bin_OrganizationIDOut","sWidth": "10%"},		// 3
							{ "sName": "totalQuantity","sWidth": "8%","sClass":"alignRight"},	// 4
							{ "sName": "totalAmount","sWidth": "8%","sClass":"alignRight"},		// 5
							{ "sName": "allocationDate","bVisible": false,"sWidth": "17%"},		// 6
							{ "sName": "verifiedFlag","sWidth": "10%","sClass":"center"},		// 7
							{ "sName": "employeeName","bVisible": false,"sWidth": "12%"}],		// 8		
			// 不可设置显示或隐藏的列	
			aiExclude :[0, 1],
			// 横向滚动条出现的临界宽度
			sScrollX : "100%",
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

function selectProduct () {		
	var value = $("#dataTableBody").find(":input[checked]").val();
	if(value != undefined && null != value){
		var selectedObj = window.JSON2.parse(value);
		$("#productName").val(selectedObj.nameTotal);
		$("#productVendorId").val(selectedObj.productVendorId);
	}
	closeCherryDialog('productDialog',BINOLPTRPS06.PTRPS06_dialogBody);	
	oTableArr[1]= null;	
}
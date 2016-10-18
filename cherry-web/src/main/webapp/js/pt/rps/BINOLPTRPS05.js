var BINOLPTRPS05 = function () {
};
BINOLPTRPS05.prototype = {
	PTRPS05_dialogBody : "",

	"openProPopup":function(_this){
		var proPopParam = {
				thisObj : _this,
				  index : 1,
			  checkType : "radio",
			      modal : false,
			  autoClose : [],
			 dialogBody : BINOLPTRPS05.PTRPS05_dialogBody
		};
		popDataTableOfPrtInfo(proPopParam);
	},
	
	/**
	 * Excel导出
	 */
	"exportExcel" : function() {
		if($(".dataTables_empty:visible").length==0) {
			if(!$('#mainForm').valid()) {
				return false;
			}
			var params = getSearchParams();
			var callback = function(msg) {
				var url = $("#exportUrl").attr("href");
				url = url + "?" + params;
			    window.open(url,"_self");
			}
			// 导出数据条数限制
		    exportExcelReport({
	    		url: $("#exporChecktUrl").attr("href"),
	    		param: params,
	    		callback: callback
	    	});
		}
	},
	
	/**
	 * CSV导出
	 */
	"exportCsv" : function() {
		if($(".dataTables_empty:visible").length==0) {
			if (!$('#mainForm').valid()) {
		        return false;
		    };
		    var params = getSearchParams();
			exportReport({
				exportUrl:$("#exportCsvUrl").attr("href"),
				exportParam:params
			});
		}
	}
};
var BINOLPTRPS05 = new BINOLPTRPS05();

$(function(){
	// 产品popup初始化
	BINOLPTRPS05.PTRPS05_dialogBody = $('#productDialog').html();
	productBinding({elementId:"nameTotal",showNum:20});
	$("#productName").keyup(function(event){
		  $("#productVendorId").val("");
		  $("#productName").val("");
	});
	
	$("#productName").focus(function(){
		BINOLPTRPS05.openProPopup(this);
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


/**
 * 查询参数序列化
 * @returns
 */
function getSearchParams() {
	var $form = $("#mainForm");
	var params= $form.find("div.column").find(":input").serialize();
	params = params + "&csrftoken=" +$("#csrftoken").val();
	params = params + "&" +getRangeParams();
	return params;
}
// 查询
function search(url){
	var $form = $("#mainForm");
	// 表单验证
	if(!$form.valid()){
		return;
	}
	 // 查询参数序列化
	 var params= getSearchParams();
	 url = url + "?" + params;
	 var allocationType = $("#mainForm").find("#allocationType").val();
	 // 设置列清空
	 $("#colSettingBtn").empty();
	 $("#searchResultDiv").empty();
	 var table = '<table id="dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%"><thead><tr>';
	 table += '<th>'+ $("#RPS05_num").html() +'</th>';
	 table += '<th>'+$("#RPS05_allocationNo").html() +'<span class="ui-icon ui-icon-document"></span></th>'
	 if(allocationType == '1') {
		 table += '<th>'+$("#RPS05_sendOrg").html() +'</th>';
		 table += '<th>'+$("#RPS05_receiveOrg").html() +'</th>';
	 } else if(allocationType == '2') {
		 table += '<th>'+$("#RPS05_receiveOrg").html() +'</th>';
		 table += '<th>'+$("#RPS05_sendOrg").html() +'</th>';
	 }
	 table += '<th>'+$("#RPS05_totalQuantity").html() +'</th>';
	 table += '<th>'+$("#RPS05_totalAmount").html() +'</th>';
	 if(allocationType == '1') {
		 table += '<th>'+$("#RPS05_dateOut").html() +'</th>';
	 } else if(allocationType == '2') {
		 table += '<th>'+$("#RPS05_dateIn").html() +'</th>';
	 }
	 table += '<th>'+$("#RPS05_verifiedFlag").html() +'</th>';
	 table += '<th>'+$("#RPS05_employeeName").html() +'</th>';
	 table += '</tr></thead></table>';
	 // 设置列按钮
	 $("#colSettingBtn").html($("#colSettingBtnDiv").html());
	 $("#searchResultDiv").html(table);
	 
	 oTableArr = new Array(null,null);
	 fixedColArr = new Array(null,null);
	 var aoColumns = [];
	 aoColumns.push({ "sName": "no","sWidth": "5%","bSortable": false});
	 aoColumns.push({ "sName": "proAllocationId","sWidth": "20%"});
	 if(allocationType == '1') {
		 aoColumns.push({ "sName": "bin_OrganizationIDOut","sWidth": "10%"});
		 aoColumns.push({ "sName": "bin_OrganizationIDIn","sWidth": "10%"});
	 } else if(allocationType == '2') {
		 aoColumns.push({ "sName": "bin_OrganizationIDIn","sWidth": "10%"});
		 aoColumns.push({ "sName": "bin_OrganizationIDOut","sWidth": "10%"});
	 }
	 aoColumns.push({ "sName": "totalQuantity","sWidth": "8%","sClass":"alignRight"});
	 aoColumns.push({ "sName": "totalAmount","sWidth": "8%","sClass":"alignRight"});
	 aoColumns.push({ "sName": "allocationDate","bVisible": false,"sWidth": "17%"});
	 aoColumns.push({ "sName": "verifiedFlag","sWidth": "10%","sClass":"center"});
	 aoColumns.push({ "sName": "employeeName","bVisible": false,"sWidth": "12%"});
	 // 表格设置
	 var tableSetting = {
			 // 表格ID
			 tableId : '#dataTable',
			 // 数据URL
			 url : url,
			 // 排序列
			 aaSorting : [[1, "desc"]],
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
	 // 列数据
	 tableSetting.aoColumns = aoColumns;
	 // 显示结果一览
	 $("#section").show();
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
	closeCherryDialog('productDialog',BINOLPTRPS05.PTRPS05_dialogBody);	
	oTableArr[1]= null;	
}
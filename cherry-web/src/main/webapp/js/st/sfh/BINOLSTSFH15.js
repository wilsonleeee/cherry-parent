var BINOLSTSFH15_GLOBAL = function() {

};

BINOLSTSFH15_GLOBAL.prototype = {
	// 用户查询
	"search" : function() {
		var $form = $('#mainForm');
		$form.find(":input").each(function() {
			$(this).val($.trim(this.value));
		});
		if(!$form.valid()){
			return false;
		}
		//表格列属性
		var aoColumnsArr = [
		                    {"sName" : "checkbox", "sWidth": "3%", "bSortable": false},
		                    {"sName" : "saleOrderNo", "sWidth" : "5%"},
		                    {"sName" : "importBatch", "sWidth" : "5%", "bVisible": false},
		                    {"sName" : "customerName", "sWidth" : "5%"},
		                    {"sName" : "contactPerson", "sWidth" : "5%", "bVisible": false},
		                    {"sName" : "deliverAddress", "sWidth" : "10%", "bVisible": false, "bSortable" : false},
		                    {"sName" : "customerType", "sWidth" : "5%", "bVisible": false},
		                    {"sName" : "billType", "sWidth" : "5%", "bVisible": false},
		                    {"sName" : "saleOrganization", "sWidth" : "5%"},
		                    {"sName" : "saleEmployee", "sWidth" : "5%"},
		                    {"sName" : "totalQuantity","sClass":"alignRight", "sWidth" : "5%"},
		                    {"sName" : "originalAmount","sClass":"alignRight", "sWidth" : "5%"},
		                    {"sName" : "discountRate","sClass":"alignRight", "sWidth" : "5%"},
		                    {"sName" : "discountAmount","sClass":"alignRight", "sWidth" : "5%"},
		                    {"sName" : "payAmount","sClass":"alignRight", "sWidth" : "5%"},
		                    {"sName" : "expectFinishDate", "sWidth" : "5%"},
		                    {"sName" : "saleDate", "sWidth" : "5%"},
		                    //{"sName" : "saleTime", "sWidth" : "5%"},
		                    {"sName" : "billTicketTime", "sWidth" : "5%", "bVisible" : false},
		                    {"sName" : "employeeName", "sWidth" : "5%", "bVisible" : false},
		                    {"sName" : "billState", "sWidth" : "5%"}
		                    //{"sName" : "printStatus", "sWidth" : "5%"}
		                    ];
		
		var url = $("#searchUrl").attr("href");
		var params= $("#mainForm").find("div.column").find(":input").serialize();
		params = params + "&" +getSerializeToken();
		params = params + "&" +getRangeParams();
		url = url + "?" + params;
		 
		$("#saleOrdersSection").show();
		// 表格设置
		var tableSetting = {
			// datatable 对象索引
			index : 1,
			// 表格ID
			tableId : "#dataTable",
			// 数据URL
			url : url,
			// 排序列
			aaSorting : [ [ 16, "desc" ] ],
			// 表格列属性设置
			aoColumns : aoColumnsArr,
			// 不可设置显示或隐藏的列
			aiExclude : [ 0 , 1 , 16 , 19 ],
			// 横向滚动条出现的临界宽度
			sScrollX : "100%",
			// 固定列数
			fixedColumns : 2,
			fnDrawCallback : function() {
				cleanPrintBill();
				getPrintTip("a.printed");
			},
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
	},
	
	"exportExcel" : function(url){
		//无数据不导出
		if($(".dataTables_empty:visible").length==0){
			$("#mainForm").find(":input").each(function() {
				$(this).val($.trim(this.value));
			});
			// 查询参数序列化
			var params= $("#mainForm").find("div.column").find(":input").serialize();
            params = params + "&" +getSerializeToken() + "&" +getRangeParams();
            url = url + "?" + params;
            window.open(url,"_self");
		}
	},
	
	"cgAfterSelectFun":function(info){
		$('#customerOrganizationId').val(info.orgId);
		$('#customerOrganization').val(info.name);
	},
	
	"bpAfterSelectFun":function(info){
		$('#customerOrganizationId').val(info.partnerId);
	},
	
	"changeCustomerType":function(){
		//取消文本框的自动完成。
		$('#customerOrganization').unautocomplete();
		$("#customerOrganization").val("");
		$("#customerOrganizationId").val("");
		var customerType = $("#customerType").val();
		if(customerType != ""){
			$("#pCustomerName").show();
			if(customerType == "1"){
				var option = {
						elementId:"customerOrganization",
						targetId:"customerOrganizationId",
						targetDetail:true,
						afterSelectFun:BINOLSTSFH15.cgAfterSelectFun,
						showNum:20
				};
				organizationBinding(option);
			}else if(customerType == "2"){
				var option = {
						elementId:"customerOrganization",
						targetId:"customerOrganizationId",
						targetDetail:true,
						afterSelectFun:BINOLSTSFH15.bpAfterSelectFun,
						showNum:20
				};
				bussinessPartnerBinding(option);
			}
		}else{
			$("#pCustomerName").hide();
		}
	}
	
};

var BINOLSTSFH15 = new BINOLSTSFH15_GLOBAL();

$(document).ready(function(){
//	var option1 = {
//			elementId:"customerOrganization",
//			targetId:"customerOrganizationId",
//			showNum:20
//	};
//	organizationBinding(option1);
	var option2 = {
			elementId:"organization",
			targetId:"organizationId",
			showNum:20
	};
	organizationBinding(option2);
	
	$("#startDate").cherryDate({
		beforeShow: function(input){
			var value = $("#endDate").val();
			return [value, "maxDate"];
		}
	});
	$("#endDate").cherryDate({
		beforeShow: function(input){
			var value = $("#startDate").val();
			return [value, "minDate"];
		}
	});
	
	// 表单验证初始化
	cherryValidate({
		formId: 'mainForm',
		rules: {
			startDate: {dateValid: true},
			endDate: {dateValid: true}
		}
	});
	
});


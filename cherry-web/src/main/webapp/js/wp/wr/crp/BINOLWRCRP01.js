function BINOLWRCRP01_global(){
	
};

BINOLWRCRP01_global.prototype = {
		
		/**
		 * 查询
		 */
		"search" : function() {
			 if (!$('#mainForm').valid()) {
				return false;
			 };
			 $("#mainForm").find(":input").each(function() {
				$(this).val($.trim(this.value));
			 });
			 var url = $("#searchUrl").attr("href");
			 // 查询参数序列化
			 var params= $("#mainForm").serialize();
			 url = url + "?" + params;
			 // 显示结果一览
			 $("#section").show();
			 // 表格设置
			 var tableSetting = {
					 // datatable 对象索引
					 index : 1,
					 // 表格ID
					 tableId : '#dataTable',
					 // 表格默认排序
					 aaSorting : [[ 7, "desc" ]],
					 // 数据URL
					 url : url,
					 // 表格列属性设置			 
					 aoColumns :    [{ "sName": "no","bSortable": false},
					                 { "sName": "couponCode"},
					                 { "sName": "subType","sClass": "center"},
					                 { "sName": "customerName","sClass": "center"},
					                 { "sName": "mobilePhone","sClass": "center"},
					                 { "sName": "birthDay","sClass": "center"},	
									 { "sName": "gender", "sClass" : "center"},
									 { "sName": "campaignOrderDate","sClass": "center"},
					                 { "sName": "bookDate","sClass": "center"},
					                 { "sName": "finishTime","sClass": "center"},
					                 { "sName": "optPerson","sClass": "center"},
					                 { "sName": "state","sClass": "center"}
					                 ],
					                     
					 // 不可设置显示或隐藏的列	
				   	aiExclude :[0, 1],
					// 横向滚动条出现的临界宽度
					sScrollX : "100%",
					// 滚动体的宽度
					sScrollXInner:"",
					callbackFun : function (){
			 		}
			 };
			 // 调用获取表格函数
			 getTable(tableSetting);
		}
}

var BINOLWRCRP01 = new BINOLWRCRP01_global();

$(document).ready(function() {
	// 表单验证配置
    cherryValidate({
        formId: 'mainForm',
        rules: {
        	campaignOrderDateStart: {dateValid:true},
        	campaignOrderDateEnd: {dateValid:true},
        	bookDateStart: {dateValid:true},
        	bookDateEnd: {dateValid:true}
        }
    });
    
	$("#employeeId").change(function(){
		if($(this).val() != '') {
			$("#employeeName").val($(this).find("option:selected").text());
		} else {
			$("#employeeName").val('');
		}
	});
	
})
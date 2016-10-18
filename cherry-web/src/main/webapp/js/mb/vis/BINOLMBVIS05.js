function BINOLMBVIS05() {};

BINOLMBVIS05.prototype = {
		"search":function() {
			var url = $("#searchUrl").attr("href");
			var params = $("#queryForm").serialize();
			url = url + "?" + params;

			$("#resultDiv").show();
			// 表格设置
			tableSetting = {
				index : 1,
				// 表格ID
				tableId : '#resultTable',
				// 数据URL
				url : url,
				// 排序列
				aaSorting : [[0, "asc"]],
				// 表格列属性设置
				aoColumns : [	
								{ "sName": "visitTypeName", "sWidth": "8%"},
		                    	{ "sName": "birthDay", "sWidth": "8%"},
		                    	{ "sName": "memName", "sWidth": "8%"},
		                    	{ "sName": "memCode", "sWidth": "8%"},
		                    	{ "sName": "memMobile", "sWidth": "8%"},
		                    	{ "sName": "skinType", "sWidth": "8%"},
		                    	{ "sName": "operate", "sWidth": "22%", "bSortable": false }
	 						],
				sScrollX : "100%",
				fnDrawCallback: function() {
					$("#resultDiv").find(":checkbox").click(function(){
						var $id = $("#resultTable");
						if($(this).attr('id') == "checkAll") {
							if(this.checked) {
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
					});
				}
			};
			// 调用获取表格函数
			getTable(tableSetting);
		}
};


var BINOLMBVIS05 =  new BINOLMBVIS05();

$(document).ready(function() {
employeeBinding({elementId:"employeeCode",showNum:20,selected:"code",privilegeFlag:"1"});
	
	$("#visitType").change(function(){
		if($(this).val() != '') {
			$("#visitTypeName").val($(this).find("option:selected").text());
		} else {
			$("#visitTypeName").val('');
		}
	});
	
	$("#searchButton").click(function(){
		BINOLMBVIS05.search();
		return false;
	});
});

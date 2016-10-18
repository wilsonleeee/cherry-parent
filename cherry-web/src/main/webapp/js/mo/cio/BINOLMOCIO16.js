var BINOLMOCIO16 = function () {};

BINOLMOCIO16.prototype = {
	/*
	 * 导入批次查询
	 */
	"search": function(){
		if (!$('#mainForm').valid()) {
			return false;
		};
		$("#mainForm").find(":input").each(function() {
			$(this).val($.trim(this.value));
		});
		var url = $("#search_Url").attr("href");
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
				// 数据URL
				url : url,
				// 表格默认排序
				aaSorting : [[ 2, "desc" ]],
				// 表格列属性设置
				aoColumns : [  { "sName": "number", "bSortable": false},
				               { "sName": "importBatchCode" },
				               { "sName": "importDate"},
				               { "sName": "employeeCode"},
				               { "sName": "isPublish","sClass": "center"},
				               { "sName": "comments" }
							],
				// 横向滚动条出现的临界宽度
				sScrollX : "100%",
		        fnDrawCallback:function(){
		        	$("a.description").cluetip({
						splitTitle: '|',
					    width: '300',
					    height: 'auto',
					    cluetipClass: 'default',
					    cursor:'pointer',
					    arrows: false, 
					    dropShadow: false
					});
		        }
		 };
		 // 调用获取表格函数
		 getTable(tableSetting);
	}

};

var BINOLMOCIO16 = new BINOLMOCIO16();

$(function(){
	// 表单验证配置
    cherryValidate({
        formId: 'mainForm',
        rules: {
        	importStartDate: {dateValid:true},    // 开始日期
        	importEndDate: {dateValid:true}   // 结束日期
    	}
    });
    
    BINOLMOCIO16.search();

});
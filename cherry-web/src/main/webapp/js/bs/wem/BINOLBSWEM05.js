var BINOLBSWEM05_GLOBAL = function() {

};

BINOLBSWEM05_GLOBAL.prototype = {
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
		                    {"sName":"employeeCode", "sWidth":"10%", "bVisible":false},
		                    {"sName":"employeeName", "sWidth":"10%"},
		                    {"sName":"saleTime", "sWidth":"20%"},
		                    {"sName":"levelType", "sWidth":"10%", "bSortable":false},
		                    {"sName":"billCode", "sWidth":"20%"},
		                    {"sName":"saleEmployeeCode", "sWidth":"10%", "bVisible":false},
		                    {"sName":"saleEmployeeName", "sWidth":"10%"},
		                    {"sName":"saleType", "sWidth":"10%"},
		                    {"sName":"incomeAmount", "sWidth":"10%"},
		                    {"sName":"saleAmount", "sWidth":"10%"},
		                    {"sName":"quantity", "sWidth":"10%"}
		                    ];
		
		var url = $("#searchUrl").attr("href");
		var params= $("#mainForm").serialize();
		if(params != null && params != "") {
			url = url + "?" + params;
		}
		 
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
			aaSorting : [ [ 2, "desc" ] ],
			// 表格列属性设置
			aoColumns : aoColumnsArr,
			// 不可设置显示或隐藏的列
			aiExclude : [ 1 , 2 , 7 , 8 ],
			// 横向滚动条出现的临界宽度
			sScrollX : "100%",
			// 固定列数
			fixedColumns : 2,
			fnDrawCallback : function() {
				
			}
		};
		// 调用获取表格函数
		getTable(tableSetting);
	},
	
	"exportExcel" : function(url){
		//无数据不导出
		if($(".dataTables_empty:visible").length==0){
			if (!$('#mainForm').valid()) {
		        return false;
		    };
		    var params= $("#mainForm").serialize();
			if(params != null && params != "") {
				url = url + "?" + params;
			}
            window.open(url,"_self");
		}
	}
	
};

var BINOLBSWEM05 = new BINOLBSWEM05_GLOBAL();

$(document).ready(function(){
	// 销售人员
	var option = {
			elementId:"employeeCode",
			showNum:20,
			selected:"code"
	};
	employeeBinding(option);
	
	// 收益人
	var option1 = {
			elementId:"commissionEmployeeCode",
			showNum:20,
			selected:"code"
	};
	employeeBinding(option1);
	
	$('#startDate').cherryDate({
	    beforeShow: function(input){
	        var value = $('#endDate').val();
	        return [value,'maxDate'];
	    }
	});
	$('#endDate').cherryDate({
	    beforeShow: function(input){
	        var value = $('#startDate').val();
	        return [value,'minDate'];
	    }
	});
	cherryValidate({			
		formId: "mainForm",		
		rules: {
			startDate: {dateValid:true},	// 开始日期
			endDate: {dateValid:true}	// 结束日期
		}		
	});
	
});

/**
 * 控制文本框与销售日期区间交互
 * @param _this
 */
function ignoreCondition(_this){
	var $this = $(_this);
	if($.trim($this.val()) == ""){
		// 单据输入框为空时，日期显示
		$("#startDate").prop("disabled",false);
		$("#endDate").prop("disabled",false);
		var startDate = $("#startDate").val();
		var endDate = $("#endDate").val();
		if(startDate == ""){
			$("#startDate").val($("#defStartDate").val());
		}
		if(endDate == ""){
			$("#endDate").val($("#defEndDate").val());
		}
		$("#COVERDIV_AJAX").remove(); //清除覆盖的DIV块
	}else{
		// 单据输入框不为空时，日期隐藏
		var datecover=$("#dateCover");  //需要覆盖的内容块的ID
		requestStart(datecover);		//内容块覆盖一块DIV来实现不允许输入日期
		$("#startDate").prop("disabled",true);
		$("#endDate").prop("disabled",true);
		var startDate = $("#startDate").val();
		var endDate = $("#endDate").val();
		if(startDate != ""){
			$("#defStartDate").val(startDate);
			$("#startDate").val("");
		}
		if(endDate != ""){
			$("#defEndDate").val(endDate);
			$("#endDate").val("");
		}
	}
};

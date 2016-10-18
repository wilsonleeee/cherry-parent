function BINOLBSWEM07() {};

BINOLBSWEM07.prototype = {	
		
	/**
	 * 清理执行结果信息
	 */
	"clearActionHtml" : function() {
		$("#errorMessage").empty();
		$("#actionResultDisplay").empty();
	},
	
	/**
	 * 查询
	 */
	"search" : function(url){
		BINOLBSWEM07.clearActionHtml();
		// 查询参数序列化
		var params= $("#mainForm").serialize();
		if(params != null && params != "") {
			url = url + "?" + params;
		}
		// 显示结果一览
		$("#section").show();
		// 表格设置
		var tableSetting = {
			// 表格ID
			tableId : '#dataTable',
			// 数据URL
			url : url,
			aaSorting : [[ 1, "asc" ]],
			// 表格列属性设置
			aoColumns : [
		             	{ "sName": "no","sWidth": "5%","bSortable":false},
		             	{ "sName": "collectionAccount","sWidth": "10%"},// 收款帐户
			            { "sName": "accountName","sWidth": "10%"},      // 收款户名
			            { "sName": "amount","sWidth": "10%"},           // 转账金额
			            { "sName": "comments","sWidth": "10%","bSortable":false},// 备注
			            { "sName": "mainBank","sWidth": "10%"},   // 收款银行
			            { "sName": "subBank","sWidth": "10%"},      // 开户行
			            { "sName": "province","sWidth": "10%"},         // 收款省份
			            { "sName": "cityCounty","sWidth": "10%"},       // 收款市县
			            { "sName": "commissionMobile","sWidth": "10%"},          // 收益人手机
			            { "sName": "commissionName","sWidth": "10%"},         // 收益人姓名
			            { "sName": "commissionCounter","sWidth": "10%"}        //收益人店铺
					],	
			// 不可设置显示或隐藏的列
			aiExclude :[0, 1],
			// 横向滚动条出现的临界宽度
			sScrollX : "100%"
		};
		// 调用获取表格函数
		getTable(tableSetting);
	},
	
	/**
	 * 导出销售记录查询结果
	 */
	"exportExcel" : function(url){
		//无数据不导出
		if($(".dataTables_empty:visible").length == 0){
		    if (!$('#mainForm').valid()) {
		        return false;
		    };
		    var params = getSearchParams();
		    var callback = function(msg) {
			    url = url + "?" + params;
			    window.open(url,"_self");
		    }
		    exportExcelReport({
	    		url: $("#exporChecktUrl").attr("href"),
	    		param: params,
	    		callback: callback
	    	});
		}
	},
	
	// 导出信息
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
	},
};

var BINOLBSWEM07 =  new BINOLBSWEM07();

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
	
});


/**
 * 查询参数序列化
 * @returns
 */
function getSearchParams(){
	var $form = $("#mainForm");
	var params= $form.find("#wem07SearchId").find(":input").serialize(); 
	params = params + "&" +getRangeParams();
	params = params + "&csrftoken=" +$("#csrftoken").val();
	return params;
};

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



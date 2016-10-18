$(function(){
	// 产品选择绑定
	productBinding({elementId:"nameTotal",showNum:20});
	// 表单验证配置
	cherryValidate({						
		formId: 'mainForm',
		rules: {
			startDate: {dateValid: true},
			endDate: {dateValid: true}
		}
	});
//	search($("#search_url").val());
	// 输入框trim处理
	$(":text").bind('focusout',function(){ 
		var $this = $(this);$this.val($.trim($this.val()));});
});

// 查询
function search(url){
	var $form = $("#mainForm");
	// 表单验证
	if(!$form.valid()){
		return;
	}
	// 显示查询结果
	$("#section").show();
	// 查询参数序列化
	 var params= getSearchParams();
	 url = url + "?" + params;
	 // 表格设置
	 var tableSetting = {
			 // 表格ID
			 tableId : '#dataTable',
			 // 数据URL
			 url : url,
			// 排序
			 aaSorting:[[1, "desc"]],
			 // 表格列属性设置
			 aoColumns :   [{ "sName": "no","bSortable":false}, 							// 0
							{ "sName": "proIOId"},											// 1
							{ "sName": "departCode"},										// 2
							{ "sName": "tradeType"},										// 3
							{ "sName": "stockType"},										// 4
							{ "sName": "totalQuantity","sClass":"alignRight"},				// 5
							{ "sName": "totalAmount","sClass":"alignRight"},				// 6
							{ "sName": "time","sClass":"alignRight"},												// 7
							{ "sName": "verifiedFlag","bVisible": false,"sClass":"center"}, // 8
						    { "sName": "employeeName","bVisible": false}],					// 9			
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

/**
 * 查询参数序列化
 * @returns
 */
function getSearchParams() {
	var $form = $("#mainForm");
	var params= $form.serialize();
	params = params + "&" +getRangeParams();
	return params;
}

/**
* 一览excel导出
*/
function ptrps09_exportExcel() {
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
	    exportExcelReport({
    		url: $("#exporChecktUrl").attr("href"),
    		param: params,
    		callback: callback
    	});
	}
}
	
/**
 * 一览Csv导出
 */
function ptrps09_exportCsv() {
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

//设置隐藏的业务类型当前值
function setValue(_this){
	var value = $(_this).val();
	$("#defTradeType").val(value);
}
//忽略条件
function ignoreCondition(_this){
	// 单据号
	var tradeNo = $.trim($("#tradeNo").val());
	// 关联单号
	var relevanceNo = $.trim($("#relevanceNo").val());
	
	if(tradeNo == "" && relevanceNo == ""){
		// ===========还原现场==============//
		// 业务类型
		var tradeType = $("#defTradeType").val();
		$("#tradeType option").each(function(){
			if($(this).val() == tradeType){
				$(this).prop("selected",true);
			}
		});
		// 单据输入框为空时，日期显示
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
		var $this = $(_this);
		var id = $this.prop("id");
		
		if(id == "tradeNo" && $this.val() != ""){
			$("#relevanceNo").val("");
		}
		if(id == "relevanceNo" && $this.val() != ""){
			$("#tradeNo").val("");
		}	
		// ===========保存现场===============//
		// 选中全部
		$("#tradeType option").first().prop("selected",true);
		// 单据输入框不为空时，日期隐藏
		var datecover=$("#dateCover");  //需要覆盖的内容块的ID
		requestStart(datecover);		//内容块覆盖一块DIV来实现不允许输入日期
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
}
function BINOLPTRPS42() {
	
};
BINOLPTRPS42.prototype={
		/* 
		 * 多选
		 * Inputs:  Object:obj	选中的对象
		*/
		"checkSelect":function(obj){
			$("#actionResultDisplay").empty();
			var $this = $(obj);
			var checkedFlag = $this.prop("checked");
			if(checkedFlag){
				var subAllNum = $("#dataTable_Cloned").find("input[name='prePayBillMainId']").length;
				var subCheckedNum = $("#dataTable_Cloned").find("input[name='prePayBillMainId']:checked").length;
				$(".FixedColumns_Cloned #prePayBillMainId").prop("checked",subAllNum==subCheckedNum? checkedFlag:false);
			}else{
				$(".FixedColumns_Cloned #prePayBillMainId").prop("checked",checkedFlag);
			}
		},
		"checkAll":function(obj){
			$("#actionResultDisplay").empty();
			var $this = $(obj);
			$("#dataTable_Cloned").find("input[name='prePayBillMainId']").prop("checked",$this.prop("checked"));
		}
		
};

var binolptrps42 =  new BINOLPTRPS42();

$(document).ready(function(){
	 $('.tabs').tabs();

	window.onbeforeunload = function(){
		if (window.opener) {
			window.opener.unlockParentWindow();
		}
	};

	$(document).ready(function() {
		initDetailTable();
		if (window.opener) {
	       window.opener.lockParentWindow();
	    }
	} );

});

/**
 * 查询参数序列化
 * @returns
 */
function getSearchParams(){
	var $form = $("#mainForm");
	var params= $form.find("#rps42SearchId").find(":input").serialize(); 
	params = params + "&" +getRangeParams();
	params = params + "&csrftoken=" +$("#csrftoken").val();
	return params;
}

/**
 * 导出销售记录查询结果
 */
function binOLPTRSP42_exportExcel(url){
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
}

/**
 * 查询
 * @param url
 */
function binOLPTRSP42_search(url){
	var url1 = url?url:$("#search").html();
	var $form = $("#mainForm");

	// 表单验证
	if(!$form.valid()){
		return;
	}

	var params = getSearchParams();

    url1 = url1 + "?" + params;// + paramsBak;

    // 显示结果一览
	 $("#section").show();
	 // 表格设置
	 var tableSetting = {
			 // 表格ID
			 tableId : '#dataTable',
			 // 数据URL
			 url : url1,
			// 表格默认排序
			 aaSorting : [[ 2, "desc" ]],
			 // 表格列属性设置
			 aoColumns : [{ "sName": "prePayBillMainId","sWidth": "1%","bSortable":false}, 	// 0
							{ "sName": "telephone","sWidth": "16%"},						// 1
							//{ "sName": "departCode","bVisible": false,"sWidth": "20%"},	// 2
							{ "sName": "prePayNo","sWidth": "20%"},					// 3
							{ "sName": "departCode","sWidth": "9%"},
							{ "sName": "prePayDate","sWidth": "9%"},					// 4
							{ "sName": "transactionType","sWidth": "9%"},
							{ "sName": "prePayAmount","sWidth": "8%"},						// 5
							{ "sName": "buyQuantity","sWidth": "10%"},//6
							{ "sName": "leftQuantity","sWidth": "8%"},	// 7
							{ "sName": "pickupTimes","sWidth": "8%"},// 8
							{ "sName": "pickupDate","sWidth": "8%"}	// 9							
							],
			// 不可设置显示或隐藏的列	
			aiExclude :[0, 1],
			// 横向滚动条出现的临界宽度
			sScrollX : "100%",
			// 固定列数
			fixedColumns : 2,
			
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
};


/**
 * 控制文本框与预付日期区间交互
 * @param _this
 */
function ignoreCondition(_this){
	var $this = $(_this);
	if($.trim($this.val()) == ""){
		// 单据输入框为空时，日期显示
		$("#prePayStartDate").prop("disabled",false);
		$("#prePayEndDate").prop("disabled",false);
		var startDate = $("#prePayStartDate").val();
		var endDate = $("#prePayEndDate").val();
		if(startDate == ""){
			$("#prePayStartDate").val($("#defStartDate").val());
		}
		if(endDate == ""){
			$("#prePayEndDate").val($("#defEndDate").val());
		}
		$("#COVERDIV_AJAX").remove(); //清除覆盖的DIV块
	}else{
		// 单据输入框不为空时，日期隐藏
		var datecover=$("#dateCover");  //需要覆盖的内容块的ID
		requestStart(datecover);		//内容块覆盖一块DIV来实现不允许输入日期
		$("#prePayStartDate").prop("disabled",true);
		$("#prePayEndDate").prop("disabled",true);
		var startDate = $("#prePayStartDate").val();
		var endDate = $("#prePayEndDate").val();
		if(startDate != ""){
			$("#defStartDate").val(startDate);
			$("#prePayStartDate").val("");
		}
		if(endDate != ""){
			$("#defEndDate").val(endDate);
			$("#prePayEndDate").val("");
		}
	}
};
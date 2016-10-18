/*
 * 全局变量定义
 */
var binOLTSSFH02_global = {};

// 消息区分(true:显示消息，false:删除消息)
binOLTSSFH02_global.msgKbn = false;
$(document).ready(function() {
//	// 表格列选中
//	$('thead th').live('click',function(){
//		$("th.sorting").removeClass('sorting');
//		$(this).addClass('sorting');
//	});
	$("#result_table table").attr("id", "dataTable");
	$("#result_list").append($("#result_table").html());
	$("#result_table table").removeAttr("id");
	productBinding({elementId:"nameTotal",showNum:20});
	// 部门名称组合框
	$('#organizationId').combobox({inputId: '#depart_text', btnId: '#depart_btn'});
	// 仓库名称组合框
	$('#inventSel').combobox({inputId: '#invent_text', btnId: '#invent_btn'});
	cherryValidate({			
		formId: "mainForm",		
		rules: {
			startDate: {dateValid:true},	// 开始日期
			endDate: {dateValid:true}	// 结束日期
		}		
	});
} );

//用户查询
function search(){
	var $form = $('#mainForm');
	$form.find(":input").each(function() {
		$(this).val($.trim(this.value));
	});
	if (!$form.valid()) {
		return false;
	};
	
	var aoColumnsArr = [	{ "sName": "checkbox","bSortable": false,"sWidth": "2%"},			//0
	                    	{ "sName": "orderNoIF","sWidth": "1%"},			                    //1
							{ "sName": "orderNo","bVisible": false},							//2							
							{ "sName": "relevanceNo","bVisible": false},						//3
							{ "sName": "binOrganizationID"},	               					//4
							{ "sName": "suggestedQuantity"},									//5
							{ "sName": "applyQuantity","bVisible": false},	
							{ "sName": "totalQuantity"},										//6
							{ "sName": "totalAmount","bVisible": false},						//7
							{ "sName": "tradeStatus"},											//8
							{ "sName": "verifiedFlag"},											//9
							{ "sName": "binLogisticInfoID","bVisible": false},					//10
							{ "sName": "comments","bVisible": false},							//11
	                        { "sName": "tradeDateTime"},								//12
							{ "sName": "binEmployeeID","bVisible": false},						//13
							{ "sName": "binEmployeeIDAudit","bVisible": false },				//14
							{ "sName": "printStatus","bVisible": false}];						//15
	var aiExcludeArr = [0,1];
	var url = $("#searchUrl").attr("href");
	// 查询参数序列化
	var params= getSearchParams();
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
			 // 排序列
			 aaSorting : [[13, "desc"]],
			 // 表格列属性设置			 
			 aoColumns : aoColumnsArr,
			 // 不可设置显示或隐藏的列	
			 aiExclude :[0, 1],
			 // 横向滚动条出现的临界宽度
			 sScrollX : "100%",
			 // 固定列数
			 fixedColumns : 2,
			callbackFun : function (msg){
	 		var $msg = $("<div></div>").html(msg);
	 		var $headInfo = $("#headInfo",$msg);
	 		if($headInfo.length > 0){
	 			$("#headInfo").html($headInfo.html());
	 		}else{
	 			$("#headInfo").empty();
		 	}
	 	},
 		fnDrawCallback:function(){cleanPrintBill();getPrintTip("a.printed");}
	 };
	 // 调用获取表格函数
	 getTable(tableSetting);
}
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
}

/***
 * 导出excel
 * @param url
 * @returns {Boolean}
 */
function exportExcel(url) {
	$("#mainForm").find(":input").each(function() {
		$(this).val($.trim(this.value));
	});
	//无数据不导出
	if($(".dataTables_empty:visible").length == 0){
	    if (!$('#mainForm').valid()) {
	        return false;
	    };
	    // 查询参数序列化
		var params= getSearchParams();
	    url = url + "?" + params;
	    window.open(url,"_self");
	}
}
/**
 * 查询参数序列化
 * @returns
 */
function getSearchParams() {
	// 查询参数序列化
	var params= $("#mainForm").find("div.column").find(":input").serialize();
	params = params + "&csrftoken=" +$("#csrftoken").val();
	params = params + "&" +getRangeParams();
    //选中导出的单据，不选导出全部
    $.each($("#dataTable_Cloned").find("[id=checkbill]:checked"), function(n){
        params += "&checkedBillIdArr="+$(this).val();
    });
	return params;
}
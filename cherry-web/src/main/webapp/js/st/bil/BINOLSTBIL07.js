function BINOLSTBIL07(){};
BINOLSTBIL07.prototype={
		// 查询
		"search":function(){
			var $form = $("#mainForm");
			// 表单验证
			if(!$form.valid()){
				return;
			}
			$form.find(":input").each(function() {
				$(this).val($.trim(this.value));
			});
			 // 查询参数序列化
			 var url = $("#searchUrl").attr("href");
			 var params= $form.find("div.column").find(":input").serialize();
			 params = params + "&csrftoken=" +$("#csrftoken").val();
			 params = params + "&" +getRangeParams();
			 url = url + "?" + params;
			 // 显示结果一览
			 $("#section").show();
			 // 表格设置
			 var tableSetting = {
					 // 表格ID
					 tableId : '#dataTable',
					 // 数据URL
					 url : url,
					 index: 1,
					 // 排序列
					 aaSorting : [[1, "desc"]],
					 // 表格列属性设置
					 aoColumns : [	{ "sName": "checkbox","bSortable": false,"sWidth": "2%"}, 			// 0 		
									{ "sName": "billNo"},			// 1
									{ "sName": "departCode"},							// 2
									{ "sName": "fromDepot"},							// 3
									{ "sName": "fromLogicInventory"},					// 4
									{ "sName": "toLogicInventory"},						// 5
									{ "sName": "totalQuantity","sClass":"alignRight"},	// 6
									{ "sName": "totalAmount","sClass":"alignRight"},	// 7
									{ "sName": "operateDate"},							// 8
									{ "sName": "verifiedFlag","sClass":"center"},		// 9
									{ "sName": "comments","bVisible": false},			// 10
									{ "sName": "printStatus","bVisible": false}],		// 11
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
		},
		/**
		 * 一览excel导出
		 */
		"exportExcel" : function(url){
			$("#mainForm").find(":input").each(function() {
				$(this).val($.trim(this.value));
			});
			//无数据不导出
			if($(".dataTables_empty:visible").length == 0){
			    if (!$('#mainForm').valid()) {
			        return false;
			    };
			    // 查询参数序列化
			    var params= $("#mainForm").find("div.column").find(":input").serialize();
				params = params + "&csrftoken=" +$("#csrftoken").val();
				params = params + "&" +getRangeParams();
			    url = url + "?" + params;
			    window.open(url,"_self");
			}
		}
};
var BINOLSTBIL07 = new BINOLSTBIL07();
$(document).ready(function() {
productBinding({elementId:"nameTotal",showNum:20});
cherryValidate({			
	formId: "mainForm",		
	rules: {
		startDate: {dateValid:true},	// 开始日期
		endDate: {dateValid:true}	// 结束日期
	}		
});
});
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
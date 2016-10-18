var BINOLWSMNG07 = function () {
    
};

BINOLWSMNG07.prototype = {

	/**
	 * 删除掉画面头部的提示信息框
	 * @return
	 */
	"clearActionMsg":function() {
		$("#errorMessage #actionResultDiv").remove();
	},
		
	/**
	 * 查询一览
	 */
	"search":function(){
		binOLWSMNG07.clearActionMsg();
		if (!$('#mainForm').valid()) {
			return false;
		};
		$("#mainForm").find(":input").each(function() {
			$(this).val($.trim(this.value));
		});
		 var url = $("#searchUrl").attr("href");
		 // 查询参数序列化
		 var params= $("#mainForm").find("div.column").find(":input").serialize();
		 params = params + "&csrftoken=" +$("#csrftoken").val();
		 params = params + "&params="+$("#params").val();
		 url = url + "?" + params;
		 // 显示结果一览
		 $("#section").show();
		 // 表格设置
		 var tableSetting = {
				 // 表格ID
				 tableId : '#dataTable',
				 // 数据URL
				 url : url,
				 index:1,
				 // 表格默认排序
				 aaSorting : [[ 1, "desc" ]],
				 // 表格列属性设置
				 aoColumns :  [	{ "sName": "no","sWidth": "1%","bSortable": false}, // 0
								{ "sName": "stockTakingNo"},			// 1
								{ "sName": "totalCheckQuantity","sClass":"alignRight"},	// 5
								{ "sName": "totalQuantity","sClass":"alignRight"},		// 6
								{ "sName": "totalAmount","sClass":"alignRight"},		// 7
								{ "sName": "stocktakeType","sClass":"alignRight"},	// 8
								{ "sName": "tradeDateTime","sWidth": "10%"},	// 9
								{ "sName": "verifiedFlag"},						// 10
								{ "sName": "employeeName"}	// 11
							],

				// 不可设置显示或隐藏的列	
				aiExclude :[0, 1],
				// 横向滚动条出现的临界宽度
				sScrollX : "100%",
				// 滚动体的宽度
				sScrollXInner:"",
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
			}
		};
		// 调用获取表格函数
		getTable(tableSetting);
	},

	/**
	 * 弹出新增自由盘点/商品全盘窗口
	 */
	"addBillInit":function(obj){
		binOLWSMNG07.clearActionMsg();
		var checkURL = $("#checkAddBillInitUrl").attr("href");
		var checkParam = "?&"+"csrftoken=" + getTokenVal();
		var callback = function(msg){
			var json = eval('('+msg+')');
			if(json.AuditBillCount == 0){
				var url = $("#addBillInitUrl").attr("href");
				var param = "?&brandInfoId="+$("#brandInfoId").val();
				param += (param ? "&" : "") + "csrftoken=" + getTokenVal();
				if($(obj).attr("id")=="btnAddAllCR"){
					param += "&addType=all";
				}else if($(obj).attr("id")=="btnAddGivenCR"){
					param += "&addType=given";
				}
				url = url+param;
				openWinByUrl(url);
			}else{
				$("#errorMessage").html('<div id="actionResultDiv" class="actionError"><ul class="errorMessage"><li><span>'+$("#errmsg_EST00038").val()+'</span></li></ul></div>');
			}
		};
		cherryAjaxRequest({
			url:checkURL,
			param:checkParam,
			callback:callback
		});
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
			params = params + "&tradeType=CR&csrftoken=" +$("#csrftoken").val();
			params = params + "&params="+$("#params").val();
		    url = url + "?" + params;
		    window.open(url,"_self");
		}
	}
	
};
var binOLWSMNG07 = new BINOLWSMNG07();

$(document).ready(function() {
	cherryValidate({
		formId: "mainForm",
		rules: {
			startDate: {dateValid:true},	// 开始日期
			endDate: {dateValid:true}	// 结束日期
		}
	});
	binOLWSMNG07.search();
} );
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
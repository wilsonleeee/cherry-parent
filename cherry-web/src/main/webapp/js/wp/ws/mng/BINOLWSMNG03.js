var BINOLWSMNG03 = function () {
    
};

BINOLWSMNG03.prototype = {

	/**
	 * 查询一览
	 */
	"search":function(){
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
				 aoColumns : [
								{ "sName": "no","sWidth": "1%","bSortable": false},
								{ "sName": "billNo","sWidth": "20%"},
								{ "sName": "tradeDate","sWidth": "10%"},
							    { "sName": "departNameReceive","bVisible": false,"sWidth": "10%"},
								{ "sName": "totalQuantity","sClass":"alignRight","sWidth": "10%"},
								{ "sName": "totalAmount","sClass":"alignRight","sWidth": "10%"},
								{ "sName": "verifiedFlag","sWidth": "15%"},
								{ "sName": "tradeStatus","sWidth": "15%"},
								{ "sName": "employeeName","sWidth": "20%"}],
	
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
	 * 弹出新增窗口
	 */
	"addBillInit":function(){
		var url = $("#addBillInitUrl").attr("href");
		var param = "?&brandInfoId="+$("#brandInfoId").val();
		param += (param ? "&" : "") + "csrftoken=" + getTokenVal();
		url = url+param;
		openWinByUrl(url);
	},
	
	/**
	 * 调出部门弹出框
	 * @return
	 */
	"openOutDepartBox":function(thisObj) {
		BINOLSTIOS06.clearActionMsg();
		if ($('#inOrganizationID').val() == null || $('#inOrganizationID').val() == "") {
			$('#errorDiv2 #errorSpan2').html($('#errmsgEST00013').val());
			$('#errorDiv2').show();
			return false;
		}
		if ($('#inDepotID').val() == null || $('#inDepotID').val() == "") {
			$('#errorDiv2 #errorSpan2').html($('#errmsgEST00006').val());
			$('#errorDiv2').show();
			return false;
		}
		BINOLSTIOS06.changeOddEvenColor();
		// 取得所有部门类型
		var departType = "";
		for ( var i = 0; i < $("#departTypePop option").length; i++) {
			var departTypeValue = $("#departTypePop option:eq(" + i + ")").val();
			// 排除4柜台
			if (departTypeValue != "4") {
				departType += "&departType=" + departTypeValue;
			}
		}
		var param = "checkType=radio&businessType=1&testTypeFlg=1&levelFlg=0&organizationId=" + $("#inOrganizationID").val() + departType;
		var callback = function() {
			var $selected = $('#departDataTable').find(':input[checked]').parents("tr");
			if ($selected.length > 0) {
				var departId = $selected.find("input@[name='organizationId']").val();
				var departCode = $selected.find("td:eq(1)").text();
				var departName = $selected.find("td:eq(2)").text();
				$("#outOrganizationID").val(departId);
				$("#outOrgName").html("(" + departCode + ")" + departName);
			}
		};
		popDataTableOfDepart(thisObj, param, callback);
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
			params = params + "&params="+$("#params").val();
		    //选中导出的单据，不选导出全部
		    $.each($("#dataTable_Cloned").find("[id=checkbill]:checked"), function(n){
		        params += "&checkedBillIdArr="+$(this).val();
		    });
		    url = url + "?" + params;
		    window.open(url,"_self");
		}
	},
	
	/**
	 * 查询参数序列化
	 * @returns
	 */
	"getSearchParams":function(){
		var $form = $("#mainForm");
		var params= $form.find(":input").serialize(); 
		var prtVendorIdLength = $("input[name='prtVendorId']").length + $("input[name='prmVendorId']").length;
		params = params + "&prtVendorIdLength=" +prtVendorIdLength;
		params = params + "&params="+$("#params").val();
		params = params + "&csrftoken=" +$("#csrftoken").val();
		return params;
	},
	
	// 导出信息
	"exportCsv" : function() {
		if($(".dataTables_empty:visible").length==0) {
			if (!$('#mainForm').valid()) {
		        return false;
		    };
		    var params = binOLWSMNG03.getSearchParams();
			exportReport({
				exportUrl:$("#exportCsvUrl").attr("href"),
				exportParam:params
			});
		}
	}
};
var binOLWSMNG03 = new BINOLWSMNG03();

$(document).ready(function() {
	cherryValidate({
		formId: "mainForm",
		rules: {
			startDate: {dateValid:true},	// 开始日期
			endDate: {dateValid:true}	// 结束日期
		}
	});
	binOLWSMNG03.search();
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
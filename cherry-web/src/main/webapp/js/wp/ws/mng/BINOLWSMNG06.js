var BINOLWSMNG06 = function () {
    
};

BINOLWSMNG06.prototype = {

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
		binOLWSMNG06.clearActionMsg();
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
								{ "sName": "stockTakingNo","sWidth": "20%"},
								{ "sName": "tradeDateTime","sWidth": "10%"},
								{ "sName": "realQuantity","sClass":"alignRight","sWidth": "10%"},
								{ "sName": "summQuantity","sClass":"alignRight","sWidth": "10%"},
								{ "sName": "summAmount","sClass":"alignRight","sWidth": "10%"},
								{ "sName": "takingType","sClass":"alignRight","sWidth": "10%"},
								{ "sName": "verifiedFlag","sWidth": "15%"},
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
	 * 弹出新增自由盘点/商品全盘窗口
	 */
	"addBillInit":function(obj){
		binOLWSMNG06.clearActionMsg();
		var checkURL = $("#checkAddBillInitUrl").attr("href");
		var checkParam = "?&"+"csrftoken=" + getTokenVal();
		var callback = function(msg){
			var json = eval('('+msg+')');
			if(json.AuditBillCount == 0){
				var url = $("#addBillInitUrl").attr("href");
				var param = "?&brandInfoId="+$("#brandInfoId").val();
				param += (param ? "&" : "") + "csrftoken=" + getTokenVal();
				if($(obj).attr("id")=="btnAddAllCA"){
					param += "&addType=all";
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
		    var params = binOLWSMNG06.getSearchParams();
			exportReport({
				exportUrl:$("#exportCsvUrl").attr("href"),
				exportParam:params
			});
		}
	}
};
var binOLWSMNG06 = new BINOLWSMNG06();

$(document).ready(function() {
	cherryValidate({
		formId: "mainForm",
		rules: {
			startDate: {dateValid:true},	// 开始日期
			endDate: {dateValid:true}	// 结束日期
		}
	});
	binOLWSMNG06.search();
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
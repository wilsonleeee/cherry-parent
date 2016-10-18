/*
 * 全局变量定义
 */
var binOLPTRPS03_global = {};

// 消息区分(true:显示消息，false:删除消息)
binOLPTRPS03_global.msgKbn = false;
// 前一次的业务类型
binOLPTRPS03_global.tradeTypePre = "";

var BINOLPTRPS03 = function () {};
BINOLPTRPS03.prototype = {
	
	"openPrmPopup":function(_this){
	
		// 产品弹出框属性设置
		var option = {
			targetId: "promotion_ID",//目标区ID
			checkType : "radio",// 选择框类型
			prmCate :'CXLP', // 产品类别
			mode : 2, // 模式
			brandInfoId : $("#BIN_BrandInfoID").val(),// 品牌ID
			getHtmlFun:function(info){// 目标区追加数据行function
				var html = '<tr><td><span class="list_normal">';
				html += '<span class="text" style="line-height:19px;">' + info.nameTotal + '</span>';
				html += '<span class="close" style="margin: -1px 0 0 6px;" onclick="BINOLPTRPS03.delPrmLabel(this);return false;"><span class="ui-icon ui-icon-close"></span></span>';
	 			html += '<input type="hidden" name="prmVendorId" value="' + info.proId + '"/>';
	 			html += '</span></td></tr>';
				return html;
			}
		};
		// 调用产品弹出框共通
		popAjaxPrtDialog(option);
	},
	/**
	 * 删除显示标签
	 * @param obj
	 * @return
	 */
	"delPrmLabel":function(obj){
		$(obj).parent().parent().parent().remove();
	}
};
var BINOLPTRPS03 = new BINOLPTRPS03();

$(document).ready(function() {
	$("#result_table table").attr("id", "dataTable");
	$("#result_list").append($("#result_table").html());
	$("#result_table table").removeAttr("id");
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
	if (!$('#mainForm').valid()) {
		return false;
	};
	var url = $("#searchUrl").attr("href");
	// 查询参数序列化
	 var params= $("#mainForm").serialize();
	 params = params + "&csrftoken=" +$("#csrftoken").val();
	 params = params + "&" + getRangeParams();
	 url = url + "?" + params;
	 // 显示结果一览
	 $("#section").show();
	 // 表格设置
	 var tableSetting = {
			 // 表格ID
			 tableId : '#dataTable',
			 // 数据URL
			 url : url,
			 // 排序列
			 aaSorting : [[1, "desc"]],
			 // 表格列属性设置
			 aoColumns : [	
	                    	{ "sName": "checkbox","bSortable": false,"sWidth": "2%"}, 	// 0
							{ "sName": "deliverRecNoSort"},						// 1
							{ "sName": "sendDepartCode"},
							{ "sName": "receiveDepartCode"},				// 4
							{ "sName": "inventoryName","bVisible" : false},// 5
							{ "sName": "totalQuantity","sClass":"alignRight"},						// 6
							{ "sName": "totalAmount","sClass":"alignRight","bVisible" : false},						// 7
							{ "sName": "receiveDate"},						// 8
							{ "sName": "employeeName"},
							{ "sName": "printStatus","sClass":"center"}],
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
		 		$("#allSelect").attr("checked", false);
		 		if (!binOLPTRPS03_global.msgKbn) {
		 			$("#actionResultDisplay").empty();
		 		}
		 		binOLPTRPS03_global.msgKbn = false;
		 	},
	 		fnDrawCallback:function(){
		 		cleanPrintBill();
		 		getPrintTip("a.printed");
		 	}
	 };
	 // 调用获取表格函数
	 getTable(tableSetting);
}

/* 
 * 设置全选按钮状态
 * 
 * Inputs:  Object:obj	选中的对象
 * 
 * 
 */
function checkAllSelect(obj){
	changeAllSelect(obj, "#dataTable_wrapper", "#allSelect", "#dataTable_Cloned");
}

/* 
 * 处理选中
 * 
 * Inputs:  String:url		Ajax请求地址
 * 			String:tableId 	表格ID
 * 			String:childId  子节点ID
 * 
 * 
 */
function doAjaxChecked(url, tableId, childId){
	var $selTableId = $(tableId).children(childId);
	$checkId = $selTableId.find(":checkbox[checked]");
	if ($checkId.length == 0) {
		return false;
	}
	var param = $("#mainForm").serialize();
	var addParam = [];
	$checkId.each(function(){
		var m = [];
		$(this).parents("tr").find(':input').each(function(){
			if(this.value != '') {
				m.push('"'+encodeURIComponent(this.name)+'":"'+encodeURIComponent(this.value.replace(/\\/g,'\\\\').replace(/"/g,'\\"'))+'"');
			}
		});
		addParam.push('{' + m.toString() + '}');
	});
	param += "&deliverInfo=[" + addParam.toString() + "]";
	var callback = function(msg) {
		var searchUrl = $("#searchUrl").text();
		binOLSSPRM27_global.msgKbn = true;
		// 刷新表格
		search(searchUrl);
	};
	cherryAjaxRequest({
		url: url,
		param: param,
		callback: callback,
		formId: "mainForm",
		coverId: "#div_main"
	});
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
		$("#COVERDIV_AJAX").remove(); 
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

/**
 * 发货部门弹出框
 * @param thisObj
 * */
function openDepartBox(thisObj){	 
	//取得所有部门类型
 	var param = "checkType=radio&privilegeFlg=1&businessType=1";
	var callback = function() {
		var $selected = $('#departDataTable').find(':input[checked]').parents("tr");
		if($selected.length > 0) {
			var departId = $selected.find("input@[name='organizationId']").val();
			var departCode = $selected.find("td:eq(1)").text();
			var departName = $selected.find("td:eq(2)").text();			
			var departNameDeliver = "("+departCode+")"+departName;
			var html = '<tr><td><span class="list_normal">';
			html += '<span class="text" style="line-height:19px;">' + departNameDeliver + '</span>';
			html += '<span class="close" style="margin: -1px 0 0 6px;" onclick="BINOLPTRPS03.delPrmLabel(this);return false;"><span class="ui-icon ui-icon-close"></span></span>';
 			html += '<input type="hidden" name="organizationIDReceive" value="' + departId + '"/>';
 			html += '</span></td></tr>';
 			$("#outOrganization_ID").html("");
 			$("#outOrganization_ID").append(html);
		}else{
			$("#outOrganization_ID").html("");
		}
	};
	popDataTableOfDepart(thisObj,param,callback);
}

/*
 * 全局变量定义
 */
var binOLSSPRM43_global = {};

// 消息区分(true:显示消息，false:删除消息)
binOLSSPRM43_global.msgKbn = false;
// 前一次的业务类型
binOLSSPRM43_global.tradeTypePre = "";

var BINOLSSPRM43 = function () {};
BINOLSSPRM43.prototype = {
//	SSPRM43_dialogBody : "",
	
	"openPrmPopup":function(_this){
//		var prmPopParam = {
//				thisObj : _this,
//				index : 1,
//				checkType : "radio",
//				modal : true,
//				autoClose : [],
//				dialogBody : BINOLSSPRM43.SSPRM43_dialogBody
//		};
//		popDataTableOfPrmInfo(prmPopParam);
	
		// 促销品弹出框属性设置
		var option = {
			targetId: "promotion_ID",//目标区ID
			checkType : "radio",// 选择框类型
			prmCate :'CXLP', // 促销品类别
			mode : 2, // 模式
			brandInfoId : $("#BIN_BrandInfoID").val(),// 品牌ID
			popValidFlag : 2 ,// 促销品产品有效区分,参数可选(0[无效]、1[有效]、2[全部])
			getHtmlFun:function(info){// 目标区追加数据行function
				var html = '<tr><td><span class="list_normal">';
				html += '<span class="text" style="line-height:19px;">' + info.nameTotal + '</span>';
				html += '<span class="close" style="margin: -1px 0 0 6px;" onclick="BINOLSSPRM43.delPrmLabel(this);return false;"><span class="ui-icon ui-icon-close"></span></span>';
	 			html += '<input type="hidden" name="prmVendorId" value="' + info.proId + '"/>';
	 			html += '</span></td></tr>';
				return html;
			}
		};
		// 调用促销品弹出框共通
		popAjaxPrmDialog(option);
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
var BINOLSSPRM43 = new BINOLSSPRM43();

$(document).ready(function() {
//	// 促销品popup初始化
//	BINOLSSPRM43.SSPRM43_dialogBody = $('#promotionDialog').html();
	
//	$("#promotionProductName").keyup(function(event){
//		  $("#promotionProductVendorId").val("");
//		  $("#promotionProductName").val("");
//	});
	
//	$("#promotionProductName").focus(function(){
//		BINOLSSPRM43.openPrmPopup(this);
//	});
	
//	// 表格列选中
//	$('thead th').live('click',function(){
//		$("th.sorting").removeClass('sorting');
//		$(this).addClass('sorting');
//	});
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
	//search();
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
							{ "sName": "deliverDate"},						// 8
							{ "sName": "employeeName"},				// 9
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
		 		if (!binOLSSPRM43_global.msgKbn) {
		 			$("#actionResultDisplay").empty();
		 		}
		 		binOLSSPRM43_global.msgKbn = false;
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
		binOLSSPRM43_global.msgKbn = true;
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

//function selectPromotion () {
//	var value = $("#prm_dataTableBody").find(":input[checked]").val();
//	if(value != undefined && null != value){
//		var selectedObj = window.JSON2.parse(value);
//		$("#promotionProductName").val(selectedObj.nameTotal);
//		$("#prmVendorId").val(selectedObj.promotionProductVendorId);
//	}else{
//		$("#promotionProductName").val("");
//		$("#prmVendorId").val("");
//	}
//	closeCherryDialog('promotionDialog',BINOLSSPRM43.SSPRM43_dialogBody);	
//	oTableArr[1]= null;	
//}
function ignoreCondition(_this){
	// 收货单据号
	var deliverRecNo = $.trim($("#deliverRecNo").val());
	// 关联单号
	var relevanceNo = $.trim($("#relevanceNo").val());
	if(deliverRecNo == "" && relevanceNo == ""){
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
		var $this = $(_this);
		var id = $this.prop("id");
		
		if(id == "deliverRecNo" && $this.val() != ""){
			$("#relevanceNo").val("");
		}
		if(id == "relevanceNo" && $this.val() != ""){
			$("#deliverRecNo").val("");
		}	
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
//			$("#outOrganizationId").val(departId);
//			$("#departNameDeliver").text("("+departCode+")"+departName);
//			chooseDepart();
			
			var departNameDeliver = "("+departCode+")"+departName;
			var html = '<tr><td><span class="list_normal">';
			html += '<span class="text" style="line-height:19px;">' + departNameDeliver + '</span>';
			html += '<span class="close" style="margin: -1px 0 0 6px;" onclick="BINOLSSPRM43.delPrmLabel(this);return false;"><span class="ui-icon ui-icon-close"></span></span>';
 			html += '<input type="hidden" name="outOrganizationId" value="' + departId + '"/>';
 			html += '</span></td></tr>';
 			$("#outOrganization_ID").html("");
 			$("#outOrganization_ID").append(html);
		}else{
//			$("#outOrganizationId").val("");
//			$("#departNameDeliver").text("");
			$("#outOrganization_ID").html("");
		}
	};
	popDataTableOfDepart(thisObj,param,callback);
}
///**
// * 更改了部门
// * @param thisObj
// */
//function chooseDepart(thisObj){	
//	$("#databody > tr[id!='dataRow0']").remove();
//}
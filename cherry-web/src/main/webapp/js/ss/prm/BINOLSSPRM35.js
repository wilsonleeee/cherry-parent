// ===促销品对话框 开始=== //
//var SSPRM35_dialogBody ="";
//function openPrmPopup(_this){
//	var prmPopParam = {
//			thisObj : _this,
//			  index : 1,
//		  checkType : "radio",
//		      modal : true,
//		  autoClose : [],
//		 dialogBody : SSPRM35_dialogBody
//	};
//	popDataTableOfPrmInfo(prmPopParam);
//}
//function selectPromotion () {		
//	var value = $("#prm_dataTableBody").find(":input[checked]").val();
//	if(value != undefined && null != value){
//		var selectedObj = window.JSON2.parse(value);
//		$("#nameTotal").val(selectedObj.nameTotal);
//		$("#prmVendorId").val(selectedObj.promotionProductVendorId);
//	}else{
//		$("#nameTotal").val("");
//		$("#prmVendorId").val("");
//	}
//	closeCherryDialog('promotionDialog',SSPRM35_dialogBody);	
//	oTableArr[1]= null;	
//}
// ===促销品对话框 结束=== //

var BINOLSSPRM35 = function () {};
BINOLSSPRM35.prototype = {
	"openPrmPopup":function(_this){
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
				html += '<span class="text" style="line-height:19px;" id="nameTotal">' + info.nameTotal + '</span>';
				html += '<span class="close" style="margin: -1px 0 0 6px;" onclick="BINOLSSPRM35.delPrmLabel(this);return false;"><span class="ui-icon ui-icon-close"></span></span>';
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
var BINOLSSPRM35 = new BINOLSSPRM35();

$(function(){
//	// 促销品popup初始化
//	SSPRM35_dialogBody = $('#promotionDialog').html();
//	$("#nameTotal").focus(function(){
//		openPrmPopup(this);
//	});
	// 表单验证配置
	cherryValidate({						
		formId: 'mainForm',
		rules: {
			startDate: {dateValid: true},
			endDate: {dateValid: true}
		}
	});
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
	 var params = getSearchParams();
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
			 aoColumns : [{ "sName": "no","bSortable":false}, 								// 0
							{ "sName": "proStockIOId"},										// 1
							{ "sName": "departCode"},										// 2
							{ "sName": "tradeType"},										// 3
							{ "sName": "stockType"},										// 4
							{ "sName": "totalQuantity","sClass":"alignRight"},				// 5
							{ "sName": "totalAmount","sClass":"alignRight","bVisible": false},// 6
							{ "sName": "stockInOutDate"},									// 7
							{ "sName": "verifiedFlag","sClass":"center","bVisible": false},	// 8
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
function ssprm35_exportExcel() {
	if($(".dataTables_empty:visible").length==0) {
		if(!$('#mainForm').valid()) {
			return false;
		}
		var params = getSearchParams();
		// 用于显示促销产品名称的查询条件
		params += $("#nameTotal").text() ? ("&nameTotal="+$("#nameTotal").text()) : "";
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
function ssprm35_exportCsv() {
	if($(".dataTables_empty:visible").length==0) {
		if (!$('#mainForm').valid()) {
	        return false;
	    };
	    var params = getSearchParams();
	    // 用于显示促销产品名称的查询条件
	    params += $("#nameTotal").text() ? ("&nameTotal="+$("#nameTotal").text()) : "";
		exportReport({
			exportUrl:$("#exportCsvUrl").attr("href"),
			exportParam:params
		});
	}
}

// 设置隐藏的业务类型当前值
function setValue(_this){
	var value = $(_this).val();
	$("#defTradeType").val(value);
}
//忽略条件
function ignoreCondition(_this){
	// 单据号
	var tradeNo = $.trim($("#tradeNo").val());
	// 关联单号
	var relevantNo = $.trim($("#relevantNo").val());
	
	if(tradeNo == "" && relevantNo == ""){
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
			$("#relevantNo").val("");
		}
		if(id == "relevantNo" && $this.val() != ""){
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
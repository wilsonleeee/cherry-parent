var BINOLSSPRM29 = function () {};
BINOLSSPRM29.prototype = {
//	SSPRM29_dialogBody : "",
	
	"openPrmPopup":function(_this){
//		var prmPopParam = {
//				thisObj : _this,
//				index : 1,
//				checkType : "radio",
//				modal : true,
//				autoClose : [],
//				dialogBody : BINOLSSPRM29.SSPRM29_dialogBody
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
				html += '<span class="close" style="margin: -1px 0 0 6px;" onclick="BINOLSSPRM29.delPrmLabel(this);return false;"><span class="ui-icon ui-icon-close"></span></span>';
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
var BINOLSSPRM29 = new BINOLSSPRM29();

$(function(){
//	// 促销品popup初始化
//	BINOLSSPRM29.SSPRM29_dialogBody = $('#promotionDialog').html();
	
//	$("#promotionProductName").keyup(function(event){
//		  $("#promotionProductVendorId").val("");
//		  $("#promotionProductName").val("");
//	});
	
//	$("#promotionProductName").focus(function(){
//		BINOLSSPRM29.openPrmPopup(this);
//	});
	
//	$('#orgId').combobox({inputId: '#org_text', btnId: '#org_btn'});
//	setInputVal('#org_text', $("#selectAll").text());
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
	//search("BINOLSSPRM29_search");
});
// 查询
function search(url){
	
	var $form = $("#mainForm");
	// 表单验证
	if(!$form.valid()){
		return;
	}
	// 显示结果一览
	$("#section").show();
	 // 查询参数序列化
	 var params= $form.find("div.column").find(":input").serialize();
	 
	 params = params + "&csrftoken=" +$("#csrftoken").val();
	 params = params + "&" +getRangeParams();
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
			 aoColumns : [	{ "sName": "checkbox","bSortable": false,"sWidth": "2%"}, 			// 0
							{ "sName": "proAllocationId"},					// 1
							{ "sName": "tradeType"},  						// 2
							{ "sName": "sendDepartCode"},						// 3
							{ "sName": "receiveDepartCode"},					// 4
							{ "sName": "totalQuantity","sClass":"alignRight"},						// 5
							{ "sName": "totalAmount","sClass":"alignRight","bVisible": false},		// 6
							{ "sName": "allocationDate"},					// 7
							{ "sName": "verifiedFlag","sClass":"center"},	// 8
							{ "sName": "employeeName"},	// 9	
							{ "sName": "printStatus","bVisible": false}],//10
							
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
//	closeCherryDialog('promotionDialog',BINOLSSPRM29.SSPRM29_dialogBody);	
//	oTableArr[1]= null;	
//}
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
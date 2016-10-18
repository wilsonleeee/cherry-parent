/*
 * 全局变量定义
 */
var binOLSSPRM57_global = {};

// 消息区分(true:显示消息，false:删除消息)
binOLSSPRM57_global.msgKbn = false;

var BINOLSSPRM57 = function () {};
BINOLSSPRM57.prototype = {
//	SSPRM57_dialogBody : "",
		
	"openPrmPopup":function(_this){
//		var prmPopParam = {
//			thisObj : _this,
//			index : 11,
//			checkType : "radio",
//			modal : true,
//			autoClose : [],
//			dialogBody : BINOLSSPRM57.SSPRM57_dialogBody
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
				html += '<span class="close" style="margin: -1px 0 0 6px;" onclick="BINOLSSPRM57.delPrmLabel(this);return false;"><span class="ui-icon ui-icon-close"></span></span>';
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
	},
	
	//用户查询
	"search":function(){
		if (!$('#mainForm').valid()) {
			return false;
		};
		
		var aoColumnsArr = [	{ "sName": "no","sWidth": "1%","bSortable": false}, 			// 0
								{ "sName": "deliverRecNo","sWidth": "15%"},						// 1
								{ "sName": "departName","sWidth": "10%"},						// 2							
								{ "sName": "departNameReceive","sWidth": "10%"},				// 3
								{ "sName": "totalQuantity","sWidth": "8%","sClass":"alignRight"},// 4
								{ "sName": "totalAmount","sWidth": "12%","sClass":"alignRight"},// 5
								{ "sName": "deliverDate","sWidth": "10%"}];						// 6
		var aiExcludeArr = [0,1];	
		var url = $("#searchUrl").attr("href");
		 // 查询参数序列化
		var params= $("#mainForm").find("div.column").find(":input").serialize();
		 params = params + "&csrftoken=" +$("#csrftoken").val();
		 params = params + "&" +getRangeParams();
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
				 aaSorting : [[1, "desc"]],
				 // 表格列属性设置
				 aoColumns : aoColumnsArr,
				// 不可设置显示或隐藏的列	
				aiExclude : aiExcludeArr,
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
			 		if (!binOLSSPRM57_global.msgKbn) {
			 			$("#actionResultDisplay").empty();
			 		}
			 		binOLSSPRM57_global.msgKbn = false;
			 	}
		 };
		 // 调用获取表格函数
		 getTable(tableSetting);
	}
};
var BINOLSSPRM57 = new BINOLSSPRM57();

$(document).ready(function() {
//	// 促销品popup初始化
//	BINOLSSPRM57.SSPRM57_dialogBody = $('#promotionDialog').html();

//	$("#promotionProductName").keyup(function(event){
//		  $("#promotionProductVendorId").val("");
//		  $("#promotionProductName").val("");
//	});
	
//	$("#promotionProductName").focus(function(){
//		BINOLSSPRM57.openPrmPopup(this);
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
} );



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
//	closeCherryDialog('promotionDialog',BINOLSSPRM57.SSPRM57_dialogBody);	
//	oTableArr[11]= null;
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
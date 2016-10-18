var BINOLSSPRM33 = function () {};
BINOLSSPRM33.prototype = {
//	SSPRM33_dialogBody : "",
	
	"openPrmPopup":function(_this){
//		var prmPopParam = {
//				thisObj : _this,
//				index : 1,
//				checkType : "radio",
//				modal : true,
//				autoClose : [],
//				dialogBody : BINOLSSPRM33.SSPRM33_dialogBody
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
				html += '<span class="close" style="margin: -1px 0 0 6px;" onclick="BINOLSSPRM33.delPrmLabel(this);return false;"><span class="ui-icon ui-icon-close"></span></span>';
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
	/**
	 * 收货部门弹出框
	 * @param thisObj
	 * */
	"openDepartBox":function (thisObj){	 
	 	//取得所有部门类型
	 	var param = "checkType=radio&privilegeFlg=1&businessType=1";
		var callback = function() {
			var $selected = $('#departDataTable').find(':input[checked]').parents("tr");
			if($selected.length > 0) {
				var departId = $selected.find("input@[name='organizationId']").val();
				var departCode = $selected.find("td:eq(1)").text();
				var departName = $selected.find("td:eq(2)").text();
				var departNameReceive = "("+departCode+")"+departName;
				var html = '<tr><td><span class="list_normal">';
				html += '<span class="text" style="line-height:19px;">' + departNameReceive + '</span>';
				html += '<span class="close" style="margin: -1px 0 0 6px;" onclick="BINOLSSPRM33.delPrmLabel(this);return false;"><span class="ui-icon ui-icon-close"></span></span>';
	 			html += '<input type="hidden" name="inOrganizationId" value="' + departId + '"/>';
	 			html += '</span></td></tr>';
	 			$("#inOrganization_ID").html("");
	 			$("#inOrganization_ID").append(html);
			}else{
				$("#inOrganization_ID").html("");
			}
		};
		popDataTableOfDepart(thisObj,param,callback);
	}
};
var BINOLSSPRM33 = new BINOLSSPRM33();

$(document).ready(function() {
//	// 促销品popup初始化
//	BINOLSSPRM33.SSPRM33_dialogBody = $('#promotionDialog').html();
	
//	$("#promotionProductName").keyup(function(event){
//		  $("#prmVendorId").val("");
//		  $("#promotionProductName").val("");
//	});
	
//	$("#promotionProductName").focus(function(){
//		BINOLSSPRM33.openPrmPopup(this);
//	});
	
//	// 表格列选中
//	$('thead th').live('click',function(){
//		$("th.sorting").removeClass('sorting');
//		$(this).addClass('sorting');
//	});
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
	 var params= $("#mainForm").find("div.column").find(":input").serialize();
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
			 // 表格默认排序
			 aaSorting : [[ 1, "desc" ]],
			 // 表格列属性设置
			 aoColumns : [	{ "sName": "checkbox","bSortable": false,"sWidth": "2%"}, 			// 0
							{ "sName": "deliverRecNoSort"},					// 1
							{ "sName": "sendDepartCode"},					// 2
							{ "sName": "receiveDepartCode"},				// 3
							{ "sName": "inventoryName","bVisible": false},	// 4
							{ "sName": "totalQuantity","sClass":"alignRight"},				// 5
							{ "sName": "totalAmount","sClass":"alignRight","bVisible": false},				// 6
							{ "sName": "deliverDate"},						// 9
							{ "sName": "employeeName"},					// 10
							{ "sName": "printStatus","bVisible": false}],			// 11
							
			// 不可设置显示或隐藏的列	
			aiExclude :[0, 1],
			// 横向滚动条出现的临界宽度
			sScrollX : "100%",
			// 滚动体的宽度
			sScrollXInner:"",
			// 固定列数
			fixedColumns : 2,
			callbackFun: function(msg){
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
//	closeCherryDialog('promotionDialog',BINOLSSPRM33.SSPRM33_dialogBody);	
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
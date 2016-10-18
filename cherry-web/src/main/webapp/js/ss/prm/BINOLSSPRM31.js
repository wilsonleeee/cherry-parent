// ===促销品对话框 开始=== //
//var SSPRM31_dialogBody ="";

//function openPrmPopup(_this){
//	var prmPopParam = {
//			thisObj : _this,
//			  index : 1,
//		  checkType : "radio",
//		      modal : true,
//		  autoClose : [],
//		 dialogBody : SSPRM31_dialogBody
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
//	closeCherryDialog('promotionDialog',SSPRM31_dialogBody);	
//	oTableArr[1]= null;	
//}
// ===促销品对话框 结束=== //

var BINOLSSPRM31 = function () {};
BINOLSSPRM31.prototype = {
	"openPrmPopup":function(_this){
		// 促销品产品有效区分
		var popValidFlag = $('input[name="validFlag"]:checked').val();
		// 促销品弹出框属性设置
		var option = {
			targetId: "promotion_ID",//目标区ID
			checkType : "radio",// 选择框类型
			prmCate :'CXLP', // 促销品类别
			mode : 2, // 模式
			brandInfoId : $("#BIN_BrandInfoID").val(),// 品牌ID
			popValidFlag : popValidFlag,// 促销品产品有效区分,根据画面所传参数决定,(0[无效]、1[有效]
			getHtmlFun:function(info){// 目标区追加数据行function
				var html = '<tr><td><span class="list_normal">';
				html += '<span class="text" style="line-height:19px;">' + info.nameTotal + '</span>';
				html += '<span class="close" style="margin: -1px 0 0 6px;" onclick="BINOLSSPRM31.delPrmLabel(this);return false;"><span class="ui-icon ui-icon-close"></span></span>';
	 			html += '<input type="hidden" name="prmVendorId" value="' + info.proId + '"/>';
	 			html += '<input type="hidden" name="nameTotal" value="' + info.nameTotal + '"/>';
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
var BINOLSSPRM31 = new BINOLSSPRM31();

$(function(){
//	// 促销品popup初始化
//	SSPRM31_dialogBody = $('#promotionDialog').html();
//	$("#nameTotal").focus(function(){
//		openPrmPopup(this);
//	});
	
	// 表单验证配置
	cherryValidate({						
		formId: 'mainForm',
		rules: {
			startDate: {required: true,dateValid: true},
			endDate: {required: true,dateValid: true}
		}
	});
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
	 params = getSearchParams();
	 url = url + "?" + params;
	 // 表格设置
	 var tableSetting = {
			 // 表格ID
			 tableId : '#dataTable',
			 // 数据URL
			 url : url,
			 // 表格列属性设置
			 aoColumns : [{ "sName": "no","sWidth": "7%","bSortable":false}, 			// 0
							{ "sName": "nameTotal","sWidth": "15%"},					// 1
							{ "sName": "unitCode","sWidth": "15%"},						// 2
							{ "sName": "barCode","sWidth": "15%"},						// 3
							{ "sName": "startQuantity","sWidth": "12%","sClass":"alignRight"},// 4
							{ "sName": "inQuantity","sWidth": "12%","sClass":"alignRight"},	// 5
							{ "sName": "outQuantity","sWidth": "12%","sClass":"alignRight"},// 6
							{ "sName": "endQuantity","sWidth": "12%","sClass":"alignRight"}],// 7			
			// 不可设置显示或隐藏的列	
			aiExclude :[0, 1, 2],
			// 横向滚动条出现的临界宽度
			sScrollX : "100%",
			// 固定列数
			fixedColumns : 2,
			callbackFun : function (msg){
	 			var $msg = $("<div></div>").html(msg);
	 			var $headInfo = $("#headInfo",$msg);
	 			$("#headInfo").html($headInfo.html());
	 	}

	 };
	 // 调用获取表格函数
	 getTable(tableSetting);
}
//查询参数序列化
function getSearchParams(){
	var $form = $("#mainForm");
	var params= $form.find("div.column").find(":input").serialize(); 
	params = params + "&csrftoken=" +$("#csrftoken").val();
	params = params + "&" +getRangeParams();
	return params;
}
// 详细画面
function getDetail(_this){
	var url = $(_this).attr("href");
	url += "&csrftoken=" +$("#csrftoken").val() + "&" +getRangeParams();
	popup(url,{width:"1200"});
}
// xls导出
function exportExcel(url){
	if($(".dataTables_empty:visible").length==0){
		if (!$('#mainForm').valid()) {
	        return false;
	    };
	    var params = getSearchParams();
	    url = url + "?" + params;
	    window.open(url,"_self");
    }
}
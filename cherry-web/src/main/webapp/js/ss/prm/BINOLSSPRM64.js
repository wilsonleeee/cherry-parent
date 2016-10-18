function BINOLSSPRM64(){};
BINOLSSPRM64.prototype={
		// 查询
		"search":function(){
			var $form = $("#mainForm");
			// 表单验证
			if(!$form.valid()){
				return;
			}
			 // 查询参数序列化
			 var url = $("#searchUrl").attr("href");
			 var params= $form.find("div.column").find(":input").serialize();
			 params = params + "&csrftoken=" +$("#csrftoken").val();
			 params = params + "&" +getRangeParams();
			 url = url + "?" + params;
			 // 显示结果一览
			 $("#section").show();
			 // 表格设置
			 var tableSetting = {
					 // 表格ID
					 tableId : '#dataTable',
					 // 数据URL
					 url : url,
					 index : 1,
					 // 排序列
					 aaSorting : [[1, "desc"]],
					 // 表格列属性设置
					 aoColumns : [	{ "sName": "checkbox","bSortable": false,"sWidth": "2%"}, 			// 0 					// 0
									{ "sName": "billNoIF"},			// 1
									{ "sName": "importBatch"},
									{ "sName": "departCode"},							// 2
									{ "sName": "depotCode"},							// 3
									{ "sName": "totalQuantity","sClass":"alignRight"},	// 4
									{ "sName": "totalAmount","sClass":"alignRight"},	// 5
									{ "sName": "inDepotDate"},							// 6
									{ "sName": "verifiedFlag","sClass":"center"},		// 7
									{ "sName": "tradeStatus"},
									{ "sName": "comments","bVisible": false},			// 8
									{ "sName": "printStatus","bVisible": false}],		// 9
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
		},
		
		/**
		 * 弹出促销品框
		 */
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
					html += '<span class="text" style="line-height:19px;">' + info.nameTotal + '</span>';
					html += '<span class="close" style="margin: -1px 0 0 6px;" onclick="BINOLSSPRM64.delPrmLabel(this);return false;"><span class="ui-icon ui-icon-close"></span></span>';
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
var BINOLSSPRM64 = new BINOLSSPRM64();
$(document).ready(function() {
//productBinding({elementId:"nameTotal",showNum:20});
cherryValidate({			
	formId: "mainForm",		
	rules: {
		startDate: {dateValid:true},	// 开始日期
		endDate: {dateValid:true}	// 结束日期
	}		
});
});
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
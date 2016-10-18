var BINOLSSPRM25 = function () {};
BINOLSSPRM25.prototype = {
//	SSPRM25_dialogBody : "",
	
	"openPrmPopup":function(_this){
//		var prmPopParam = {
//				thisObj : _this,
//				index : 1,
//				checkType : "radio",
//				modal : true,
//				autoClose : [],
//				dialogBody : BINOLSSPRM25.SSPRM25_dialogBody
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
				html += '<span class="close" style="margin: -1px 0 0 6px;" onclick="BINOLSSPRM25.delPrmLabel(this);return false;"><span class="ui-icon ui-icon-close"></span></span>';
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
	 * 修改大分类下拉框
	 * @param data
	 * @return
	 */
	 "choosePrimaryCategory":function(){
		var organizationid = $('#organizationId').val();
		var primaryCategory =$('#largeCategory').val();
		//更改了部门后，取得该部门所拥有的仓库
		var url = $('#s_getMCategoryAjax').html()+"?csrftoken="+$('#csrftoken').val();
		$.ajax({
			url:url,
			data:{'organizationid':organizationid,'largeCategory':primaryCategory},
			success:function(data) {
			BINOLSSPRM25.changeMAndSCategory(data)
			}
		});
	},

	/**
	 * 修改大分类下拉框后，中，小下拉框联动
	 * @param data
	 * @return
	 */
	"changeMAndSCategory":function(data){
		var member = eval("("+data+")");    //包数据解析为json 格式  
		$("#middleCategory option:not(:first)").remove();
		$.each(member, function(i){
			$("#middleCategory").append("<option value='"+ member[i].SecondryCategoryCode+"'>"+escapeHTMLHandle(member[i].SecondCategoryName)+"</option>"); 
		});
		$("#smallCategory option:not(:first)").remove();
	},
	/**
	 * 修改中分类下拉框
	 * @param data
	 * @return
	 */
	"chooseSecondCategory":function(){
		var organizationid = $('#organizationId').val();
		var primary =$('#largeCategory').val();
		var second =$('#middleCategory').val();
		//更改了部门后，取得该部门所拥有的仓库
		var url = $('#s_getSCategoryAjax').html()+"?csrftoken="+$('#csrftoken').val();	
		$.ajax({
			url:url,
			data:{'organizationid':organizationid,'largeCategory':primary,'middleCategory':second},
			success:function(data) {
			BINOLSSPRM25.changeSCategory(data)
			}
		});
	},
	/**
	 * 修改中分类下拉框后，小下拉框联动
	 * @param data
	 * @return
	 */
	"changeSCategory":function (data){
		var member = eval("("+data+")");    //包数据解析为json 格式  
		$("#smallCategory option:not(:first)").remove();
		$.each(member, function(i){
			$("#smallCategory").append("<option value='"+ member[i].SmallCategoryCode+"'>"+escapeHTMLHandle(member[i].SmallCategoryName)+"</option>"); 
		});	
	}
	
};
var BINOLSSPRM25 = new BINOLSSPRM25();

$(document).ready(function() {
//	// 促销品popup初始化
//	BINOLSSPRM25.SSPRM25_dialogBody = $('#promotionDialog').html();
	
//	$("#promotionProductName").keyup(function(event){
//		  $("#prmVendorId").val("");
//		  $("#promotionProductName").val("");
//	});
	
//	$("#promotionProductName").focus(function(){
//		BINOLSSPRM25.openPrmPopup(this);
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
	//search();
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
			 aoColumns : [	{ "sName": "checkbox","bSortable": false,"sWidth": "2%"}, 	// 0
							{ "sName": "stockTakingNo"},			// 1
							{ "sName": "departName"},				// 2
							{ "sName": "inventoryName","bVisible" : false},			// 3
							{ "sName": "sumrealQuantity","sClass":"alignRight"},				// 5
							{ "sName": "summQuantity","sClass":"alignRight"},				// 6
							{ "sName": "summAmount","sClass":"alignRight","bVisible" : false},				// 7
							{ "sName": "takingType"},				// 8
							{ "sName": "tradeDateTime"},			// 9
							//{ "sName": "verifiedFlag","sWidth": "5%", "bVisible" : false},				// 10
							{ "sName": "employeeName"},			// 11
							{ "sName": "printStatus","bVisible": false}],				// 12	
							
			// 不可设置显示或隐藏的列	
			aiExclude :[0, 1],
			// 横向滚动条出现的临界宽度
			sScrollX : "100%",
			// 滚动体的宽度
			sScrollXInner:"",
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
//	closeCherryDialog('promotionDialog',BINOLSSPRM25.SSPRM25_dialogBody);	
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
//xls导出
function exportExcel(url,flag){
	//没有数据Excel不导出
	 if($(".dataTables_empty:visible").length==0){
			if (!$('#mainForm').valid()) {
		        return false;
		    };
		    // 查询参数序列化
			 var params= $("#mainForm").find("div.column").find(":input").serialize();
			 params = params + "&csrftoken=" +$("#csrftoken").val();
			 params = params + "&" + getRangeParams();
			 if(flag == '0') {
				 // 一览导出
				 url = url + "?" + params;
				 window.open(url,"_self");
			 } else if(flag == '1'){
				 // 一览明细导出（先判断有无明细数据）
				 var callback = function(msg) {
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
}

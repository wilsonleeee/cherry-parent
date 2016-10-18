function BINOLPTRPS33() {
	
};

BINOLPTRPS33.prototype={		
		// 选择活动弹出框
		"popCampaignList": function(url) {
			var callback = function(tableId) {
				var $checkedRadio = $("#"+tableId).find(":input[checked]");
				if($checkedRadio.length > 0) {
					// 活动类型（0：会员活动，1：促销活动）
					if($checkedRadio.next().val() == ""){
						$("#campaignMode").val("0");
					}else {
						$("#campaignMode").val($checkedRadio.next().val());
					}
					$("#campaignCode").val($checkedRadio.val());
					var html = $checkedRadio.parent().next().text();
					//用于导出活动名称的查询条件
					$("#campaignName").val(html);
					html += '<span class="close" style="margin: 0 10px 2px 5px;" onclick="binolptrps33.delCampaignHtml(this);"><span class="ui-icon ui-icon-close" style="background-position: -80px -129px;"></span></span>';
					$("#campaignDiv").html(html);
				}
			}
			var value = $("#campaignCode").val();
			popDataTableOfCampaignList(url, null, value, callback);
		},
		// 删除选择的活动
		"delCampaignHtml": function(object) {
			$("#campaignMode").val("");
			$("#campaignCode").val("");
			$("#campaignName").val("");
			$("#campaignDiv").empty();
		},
		// 导出信息
		"exportCsv" : function() {
			if($(".dataTables_empty:visible").length==0) {
				if (!$('#mainForm').valid()) {
			        return false;
			    };
			    var params = getSearchParams();
				exportReport({
					exportUrl:$("#exportCsvUrl").attr("href"),
					exportParam:params
				});
			}
		},
		/**
		 * 弹出普通商品选择DIV
		 * @param flag
		 */
		"openProDialog" :function(flag) {
	    	var proTable = "proTable";
	    	var proShowDiv = "proShowDiv";
	    	var prtVendorId = "salePrtVendorId";
	    	var proPrmName = "saleProPrmName";
	    	if(flag == '1') {
				proTable = "joinProTable";
				proShowDiv = "joinProShowDiv";
				prtVendorId = "joinPrtVendorId";
				proPrmName = "joinProPrmName";
	    	}
	    	
	    	// 剔除销售商品与连带商品彼此已选择的商品 start 
	    	var salePrtVendorIdArr = new Array();
	    	$("input[name='salePrtVendorId']").each(function(){
	    		salePrtVendorIdArr.push($(this).val()); 
	    	})
	    	var joinPrtVendorIdArr = new Array();
	    	$("input[name='joinPrtVendorId']").each(function(){
	    		joinPrtVendorIdArr.push($(this).val()); 
	    	})
	    	
	    	var prtVendorIDArr = new Array();
	    	if(flag == '1') {
	    		prtVendorIDArr = salePrtVendorIdArr;
	    	}else {
	    		prtVendorIDArr = joinPrtVendorIdArr;
	    	}
	    	
	    	// 剔除销售商品与连带商品彼此已选择的商品 end 
	    	
			var option = {
	         	   	targetId: proShowDiv,
	 	           	checkType : "checkbox",
	 	           	mode : 2,
	 	           	brandInfoId : $("#brandInfoId").val(),
	 	            popValidFlag : 2, // 有效区别（2为全部即包括有效及无效 ）,
	 	            ignorePrtPrmVendorID : prtVendorIDArr, // 剔除销售商品与连带商品彼此已选择的商品
	 		       	getHtmlFun:function(info){
	     		       	var html = '<tr>'; 
	     				html += '<td  class="hide">' + '<input type="hidden" name="prtVendorId" value="' + info.proId + '"/><input type="hidden" name="salePrtVendorId" value="' + info.proId + '_N"/></td>';
	     				html += '<td style="width:25%;">' + info.unitCode + '</td>';
	     				html += '<td style="width:25%;">' + info.barCode + '</td>';
	     				html += '<td style="width:35%;">' + info.nameTotal + '</td>';
	     				html += '<td class="center" style="width:15%;"><a href="javascript:void(0);" onClick="binolptrps33.deleteHtml(this);return false;">'+$("#deleteButton").text()+'</a></td>';
	     				html += '</tr>';
	     				
	     				return html;
				   	},
				   	click : function() {
				   		if($("#"+proShowDiv).find("tr").length > 0) {
				   			$("#"+proTable).show();
				   			if(flag != '1'){
				   				$("#joinPrmProductTR1").show();
				   			}
				   		} else {
				   			$("#"+proTable).hide();
				   			// 销售商品为空时，不显示连带销售
				   			if(flag != '1'){
				   				binolptrps13.removeJointPrtPrm();
				   			}
				   		}
				   	}
	 	    };
	 		popAjaxPrtDialog(option);
		},		
		/**
		 * 弹出促销商品选择DIV
		 * @param flag
		 */
		"openPrmDialog" :function(flag) {
			var proTable = "proTable";
			var proShowDiv = "proShowDiv";
			var prtVendorId = "salePrtVendorId";
			var proPrmName = "saleProPrmName";
			if(flag == '1') {
				proTable = "joinProTable";
				proShowDiv = "joinProShowDiv";
				prtVendorId = "joinPrtVendorId";
				proPrmName = "joinProPrmName";
			}
			
	    	// 剔除销售商品与连带商品彼此已选择的商品 start 
	    	var salePrtVendorIdArr = new Array();
	    	$("input[name='salePrtVendorId']").each(function(){
	    		salePrtVendorIdArr.push($(this).val()); 
	    	})
	    	var joinPrtVendorIdArr = new Array();
	    	$("input[name='joinPrtVendorId']").each(function(){
	    		joinPrtVendorIdArr.push($(this).val()); 
	    	})
	    	
	    	var prmVendorIDArr = new Array();
	    	if(flag == '1') {
	    		prmVendorIDArr = salePrtVendorIdArr;
	    	}else {
	    		prmVendorIDArr = joinPrtVendorIdArr;
	    	}
	    	// 剔除销售商品与连带商品彼此已选择的商品 end 
			
			var option = {
					targetId: proShowDiv,//目标区ID
					checkType : "checkbox",// 选择框类型
					prmCate :'CXLP', // 促销品类别
					mode : 2, // 模式
					brandInfoId : $("#brandInfoId").val(),// 品牌ID
					popValidFlag : 2, // 有效区别（2为全部即包括有效及无效 ）
					ignorePrtPrmVendorID : prmVendorIDArr, // 剔除销售商品与连带商品彼此已选择的商品
					getHtmlFun:function(info){
						var html = '<tr>'; 
						html += '<td class="hide">' + '<input type="hidden" name="prmVendorId" value="' + info.proId + '"/><input type="hidden" name="salePrtVendorId" value="' + info.proId + '_P"/></td>';
						html += '<td style="width:25%;">' + info.unitCode + '</td>';
						html += '<td style="width:25%;">' + info.barCode + '</td>';
						html += '<td style="width:35%;">' + info.nameTotal + '</td>';
						html += '<td class="center" style="width:15%;"><a href="javascript:void(0);" onClick="binolptrps33.deleteHtml(this);return false;">'+$("#deleteButton").text()+'</a></td>';
						html += '</tr>';
						return html;
					},
				   	click : function() {
				   		if($("#"+proShowDiv).find("tr").length > 0) {
				   			$("#"+proTable).show();
				   			if(flag != '1'){
				   				$("#joinPrmProductTR1").show();
				   			}
				   		} else {
				   			$("#"+proTable).hide();
				   			// 销售商品为空时，不显示连带销售
				   			if(flag != '1'){
				   				binolptrps13.removeJointPrtPrm();
				   			}
				   		}
				   	}
			};
			// 调用促销品弹出框共通
			popAjaxPrmDialog(option);
		},

		/**
		 * Remove商品
		 * @param _this
		 */
		"deleteHtml": function(_this) {
			var $tbody = $(_this).parent().parent().parent();
			$(_this).parent().parent().remove();
			if($tbody.find('tr').length == 0) {
				$tbody.parent().hide();
			}
			// 销售商品为空时，不显示连带销售
			/*if($tbody.attr("id") == "proShowDiv"){
				if($tbody.html().length == 0){
					binolptrps13.removeJointPrtPrm();
				}
			}*/
			
		}
		
};

var binolptrps33 =  new BINOLPTRPS33();

$(document).ready(function(){
	
	salesStaffBinding({elementId:"employeeCode",selected : "code",showNum:20});
	$("#billCode").focus();
	// 表单验证配置
	cherryValidate({						
		formId: 'mainForm',
		rules: {
			startDate: {dateValid: true},
			endDate: {dateValid: true},
			payStartDate: {dateValid: true},//下单付款开始时间
			payEndDate: {dateValid: true}//下单付款结束时间
		}
	});
	
});

/**
 * 查询参数序列化
 * @returns
 */
function getSearchParams(){
	var $form = $("#mainForm");
	var params= $form.find("#rps33SearchId").find(":input").serialize(); 
	var prtVendorIdLength = $("input[name='prtVendorId']").length + $("input[name='prmVendorId']").length;
	params = params + "&prtVendorIdLength=" +prtVendorIdLength;
	params = params + "&" +getRangeParams();
	params = params + "&csrftoken=" +$("#csrftoken").val();
	return params;
}

/**
 * 查询
 * @param url
 */
function binOLPTRSP33_search(url){
	var prtVendorIdArr = new Array();

	var proShowDivLength = $("#proShowDiv >tr").length;
	if(proShowDivLength>0){
		$.each($("#proShowDiv >tr"),function(){
			var salePrtVendorId = $(this).find("[name='salePrtVendorId']").val();
			if(salePrtVendorId!=undefined && null!=salePrtVendorId){
				var obj = {"salePrtVendorId":salePrtVendorId}
				prtVendorIdArr.push(obj);
			}
		});
	}
	var prtVendorIdList = "";
	if(null!=prtVendorIdArr){
		prtVendorIdList = JSON.stringify(prtVendorIdArr);
	}

	var url1 = url?url:$("#search").html();
	var $form = $("#mainForm");
	// 表单验证
	if(!$form.valid()){
		return;
	}
	var params = getSearchParams();
	// 用于保存商品详细查询的条件 
	
	// 去除params备份中的csrftoken
	var paramsBak = params.substring(0,params.indexOf("csrftoken")-1);
	// 将params备份中的&全部替换成sp_a_
	paramsBak = paramsBak.replace(/&/g,"sp_a_"); // 
	// 将params备份中的=全部替换成sp_eq_
	paramsBak = paramsBak.replace(/=/g,"sp_eq_");
	
	paramsBak = "&paramsBak=" + paramsBak;
	
    url1 = url1 + "?" + params + paramsBak;
    if(prtVendorIdList!=""){
    	url1+="&prtVendorIdList="+prtVendorIdList;
    }

    // 显示结果一览
	 $("#section").show();
	 // 表格设置
	 var tableSetting = {
			 // 表格ID
			 tableId : '#dataTable',
			 // 数据URL
			 url : url1,
			// 表格默认排序
			 aaSorting : [[ 10, "desc" ]],
			 // 表格列属性设置
			 aoColumns : [{ "sName": "esOrderMainID","sWidth": "1%","bSortable":false},
							{ "sName": "billCode","sWidth": "16%"},
							{ "sName": "shopName","sWidth": "20%"},
							{ "sName": "employeeCode","sWidth": "9%"},
							{ "sName": "memberCode","sWidth": "9%"},
							{ "sName": "saleType","sWidth": "8%"},
							{ "sName": "consumerType","bVisible": false,"sWidth": "10%"},
							{ "sName": "ticketType","bVisible": false,"sWidth": "8%"},
							{ "sName": "quantity","sClass":"alignRight","sWidth": "1%"},
							{ "sName": "amount","sClass":"alignRight","sWidth": "10%"},
							{ "sName": "expressCost","sClass":"alignRight","sWidth": "10%"},
							{ "sName": "billCreateTime","sWidth": "15%"},	
							{ "sName": "billState","bVisible": false,"sWidth": "15%"},
							{ "sName": "preSale","bVisible": false,"sWidth": "10%"}
							],
			// 不可设置显示或隐藏的列	
			aiExclude :[0, 1],
			// 横向滚动条出现的临界宽度
			sScrollX : "100%",
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
			}
	 };
	 // 调用获取表格函数
	 getTable(tableSetting);
};

/**
 * 控制文本框与销售日期区间交互
 * @param _this
 */
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
};

/**
 * 导出销售记录查询结果
 */
function binOLPTRSP33_exportExcel(url){
	//无数据不导出
	if($(".dataTables_empty:visible").length == 0){
	    if (!$('#mainForm').valid()) {
	        return false;
	    };
	    var params = getSearchParams();
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
var BINOLSSPRM45 = function() {

};

BINOLSSPRM45.prototype = {

	// 查询
	"search" : function() {
		if (!$('#mainForm').valid()) {
			return false;
		};
		$("#mainForm").find(":input").each(function() {
			$(this).val($.trim(this.value));
		});
		var url = $("#searchUrl").attr("href");
		var selPrmVendorIdArr = new Array();
    	$("input[name='prmVendorId']").each(function(){
    		selPrmVendorIdArr.push($(this).val()); 
    	})
		// 查询参数序列化
		var params = $("#mainForm").find("#prm45searchId").find(":input").serialize();
		params = params + "&csrftoken=" + $("#csrftoken").val();
		params = params + "&selPrmVendorIdArr=" + JSON.stringify(selPrmVendorIdArr);
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
			index : 1,
			// 表格默认排序
			aaSorting : [ [ 1, "desc" ] ],
			// 表格列属性设置
			aoColumns : [ {"sName" : "region"}, 
			              {"sName" : "city"},// 0
			              {"sName" : "Type","sClass" : "alignRight","bVisible": false},
			              {"sName" : "employeeName"}, 
			              {"sName" : "stockTakingId"}, // 1
			              {"sName" : "employeeName"}, 
			              {"sName" : "stockTakingDate"}, 
			              {"sName" : "takingType","bVisible": false},// 7
			              {"sName" : "Comments","bVisible": false}, 
			              {"sName" : "Quantity"},// 3
			              {"sName" : "realQuantity","sClass" : "alignRight"}, 
			              {"sName" : "summQuantity","sClass" : "alignRight"}, 
			              {"sName" : "OverQuantity","sClass" : "alignRight"},
			              {"sName" : "ShortQuantity","sClass" : "alignRight"}], // 11

			// 不可设置显示或隐藏的列
			aiExclude : [ 0, 1 ],
			// 横向滚动条出现的临界宽度
			sScrollX : "100%",
			// 滚动体的宽度
			sScrollXInner : "",
			// 固定列数
			fixedColumns : 3,
			// html转json前回调函数
			callbackFun : function(msg) {
				var $msg = $("<div></div>").html(msg);
				var $headInfo = $("#headInfo", $msg);
				if ($headInfo.length > 0) {
					$("#headInfo").html($headInfo.html());
				} else {
					$("#headInfo").empty();
				}
			},
			fnDrawCallback : function() {
				cleanPrintBill();
				getPrintTip("a.printed");
			}
		};
		// 调用获取表格函数
		getTable(tableSetting);
	},
	/*
	 * 导出Excel
	 */
	"exportExcel" : function(urls) {
		if (window.opener) {
			window.opener.unlockParentWindow();
		};
		// 无数据不导出
		if ($(".dataTables_empty:visible").length == 0) {
			if (!$('#mainForm').valid()) {
				return false;
			}
			;
			var url = urls;
			var selPrmVendorIdArr = new Array();
	    	$("input[name='prmVendorId']").each(function(){
	    		selPrmVendorIdArr.push($(this).val()); 
	    	})
			// 查询参数序列化
			var params = $("#mainForm").find("#prm45searchId").find(":input").serialize();
			params = params + "&csrftoken=" + $("#csrftoken").val();
			params = params + "&selPrmVendorIdArr=" + JSON.stringify(selPrmVendorIdArr);
			params = params + "&" + getRangeParams();
			url = url + "?" + params;
			window.open(url, "_self");
		}
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
			BINOLSSPRM45.changeMAndSCategory(data)
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
			BINOLSSPRM45.changeSCategory(data)
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
	},
	
	"openPrmPopup":function(_this) {
		var proTable = "joinProTable";
		var proShowDiv = "joinProShowDiv";
		var prtVendorId = "joinPrtVendorId";
		var proPrmName = "joinProPrmName";
		
		var joinPrtVendorIdArr = new Array();
    	$("input[name='joinPrtVendorId']").each(function(){
    		joinPrtVendorIdArr.push($(this).val()); 
    	})
    	var prmVendorIDArr = new Array();
    	prmVendorIDArr = joinPrtVendorIdArr;
    	
		var option = {
			targetId: "promotion_ID",//目标区ID
			checkType : "checkbox",// 选择框类型
			prmCate :'CXLP', // 促销品类别
			mode : 2, // 模式
			brandInfoId : $("#BIN_BrandInfoID").val(),// 品牌ID
			popValidFlag : 2,// 促销品产品有效区分,参数可选(0[无效]、1[有效]、2[全部])
			ignorePrtPrmVendorID : prmVendorIDArr,
			getHtmlFun:function(info) {// 目标区追加数据行function
				var html = '<tr>';
				html += '<td class="hide">' + '<input type="hidden" name="prmVendorId" value="' + info.proId + '"/><input type="hidden" name="'+prtVendorId+'" value="' + info.proId + '_P"/><input type="hidden" name="prtType" value="P"/><input type="hidden" name="'+proPrmName+'" value="' + info.unitCode + '_spt_n' + info.barCode + '_spt_n' + info.nameTotal + '"/></td>';
				html += '<td style="width:25%;">' + info.unitCode + '</td>';
				html += '<td style="width:25%;">' + info.barCode + '</td>';
				html += '<td style="width:35%;">' + info.nameTotal + '</td>';
				html += '<td class="center" style="width:15%;"><a href="javascript:void(0);" onClick="BINOLSSPRM45.deleteHtml(this);return false;">'+$("#deleteButton").text()+'</a></td>';
				html += '</tr>';
				return html;
			},
			click : function() {
				if($("#promotion_ID").find("tr").length > 0) {
					$("#proPrmTr").show();
				}
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
	"deleteHtml":function(_this){
		var $tbody = $(_this).parent().parent().parent();
		$(_this).parent().parent().remove();
		if($tbody.find('tr').length == 0) {
			$("#proPrmTr").hide();
		}
	}
};
var BINOLSSPRM45 = new BINOLSSPRM45();

$(document).ready(function() {
	// 选择绑定
	productBinding({
		elementId : "promotionName",
		showNum : 20,
		proType : "prm",
		targetId:"prmVendorId"
	});
	cherryValidate({
		formId : "mainForm",
		rules : {
			startDate : {
				dateValid : true
			}, // 开始日期
			endDate : {
				dateValid : true
			}
		// 结束日期
		}
	});
});

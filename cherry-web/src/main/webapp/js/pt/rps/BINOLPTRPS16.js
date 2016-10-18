var BINOLPTRPS16 = function() {

};

BINOLPTRPS16.prototype = {
		
	// 对象JSON化
	"toJSON" : function(obj) {
		var JSON = [];
		var propValArr = [];
		$(obj).find(':input').each(
				function() {
					$this = $(this);
					if (($this.attr("type") == "radio" && this.checked)
							|| $this.attr("type") != "radio") {
						if ($.trim($this.val()) != '') {
							var name = $this.attr("name");
							if (name.indexOf("_") != -1) {
								name = name.split("_")[0];
							}
							
							if(name == 'propValId'){
								propValArr.push('"'+encodeURIComponent($.trim($this.val())
										.replace(/\\/g, '\\\\').replace(
												/"/g, '\\"')) + '"');
							} 
							JSON.push('"'
									+ encodeURIComponent(name)
									+ '":"'
									+ encodeURIComponent($.trim($this.val())
											.replace(/\\/g, '\\\\').replace(
													/"/g, '\\"')) + '"');
							
						}
					}
				});
		JSON.push('"propValArr":[' + propValArr.toString()+']');
		return "{" + JSON.toString() + "}";
	},
	// 对象JSON数组化
	"toJSONArr" : function($obj) {
		var that = this;
		var JSONArr = [];
		$obj.each(function() {
			JSONArr.push(that.toJSON(this));
		});
		return "[" + JSONArr.toString() + "]";
	},

	// 查询
	"search" : function() {
		if (!$('#mainForm').valid()) {
			return false;
		}
		;
		$("#mainForm").find(":input").each(function() {
			$(this).val($.trim(this.value));
		});
		var url = $("#searchUrl").attr("href");
		//选中的产品
		var selPrtVendorIdArr = new Array();
		$("input[name='prtVendorId']").each(function(){
			selPrtVendorIdArr.push($(this).val());
		})
		// 查询参数序列化
		var params = $("#mainForm").find("#rps16searchId").find(":input").serialize();
		params = params + "&csrftoken=" + $("#csrftoken").val();
		params += "&selPrtVendorIdArr=" + JSON.stringify(selPrtVendorIdArr);
		params = params + "&" + getRangeParams();
		//产品分类
		params += "&cateInfo=" + this.toJSONArr($("#cateInfo").find(".detail").children().children());
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
			              {"sName" : "Type","sClass":"center"},
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
		// 无数据不导出
		if ($(".dataTables_empty:visible").length == 0) {
			if (!$('#mainForm').valid()) {
				return false;
			}
			;
			var url = urls;
			//选中的产品
			var selPrtVendorIdArr = new Array();
			$("input[name='prtVendorId']").each(function(){
				selPrtVendorIdArr.push($(this).val());
			})
			// 查询参数序列化
			var params = $("#mainForm").find("#rps16searchId").find(":input").serialize();
			params = params + "&csrftoken=" + $("#csrftoken").val();
			params += "&selPrtVendorIdArr=" + JSON.stringify(selPrtVendorIdArr);
			params = params + "&" + getRangeParams();
			//产品分类
			params += "&cateInfo=" + this.toJSONArr($("#cateInfo").find(".detail").children().children());
			url = url + "?" + params;
			window.open(url, "_self");
		}
	},
	//产品弹出框
	"openProPopup" : function(_this){
    	var proTable = "proTable";
    	var proShowDiv = "proShowDiv";
    	var prtVendorId = "salePrtVendorId";
    	var proPrmName = "saleProPrmName";
    	
    	// 剔除销售商品与连带商品彼此已选择的商品 start 
    	var salePrtVendorIdArr = new Array();
    	$("input[name='salePrtVendorId']").each(function(){
    		salePrtVendorIdArr.push($(this).val()); 
    	})
    	var prtVendorIDArr = new Array();
    	prtVendorIDArr = salePrtVendorIdArr;    	
    	
		var option = {
     	   	targetId: "production_ID",
           	checkType : "checkbox",
           	mode : 2,
           	brandInfoId : $("#brandInfoId").val(),
            popValidFlag : 2, // 有效区别（2为全部即包括有效及无效 ）
            ignorePrtPrmVendorID : prtVendorIDArr,
	       	getHtmlFun:function(info){
 		       	var html = '<tr>'; 
 				html += '<td class="hide">' + '<input type="hidden" name="prtVendorId" value="' + info.proId + '"/><input type="hidden" name="'+prtVendorId+'" value="' + info.proId + '_N"/><input type="hidden" name="prtType" value="N"/><input type="hidden" name="'+proPrmName+'" value="' + info.unitCode + '_spt_n' + info.barCode + '_spt_n' + info.nameTotal + '"/></td>';
 				html += '<td style="width:25%;">' + info.unitCode + '</td>';
 				html += '<td style="width:25%;">' + info.barCode + '</td>';
 				html += '<td style="width:35%;">' + info.nameTotal + '</td>';
 				html += '<td class="center" style="width:15%;"><a href="javascript:void(0);" onClick="BINOLPTRPS16.deleteHtml(this);return false;">'+$("#deleteButton").text()+'</a></td>';
 				html += '</tr>';
 				return html;
		   	},
		   	click : function() {
		   		if($("#production_ID").find("tr").length > 0) {
					$("#productionTr").show();
				}
		   	}
 	    };
		popAjaxPrtDialog(option);
	},		
	
	"deleteHtml":function(_this) {
		var $tbody = $(_this).parent().parent().parent();
		$(_this).parent().parent().remove();
		if($tbody.find('tr').length == 0) {
			$("#productionTr").hide();
		}
	}
};
var BINOLPTRPS16 = new BINOLPTRPS16();

$(document).ready(function() {
	// 产品选择绑定
	productBinding({
		elementId : "productName",
		showNum : 20
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

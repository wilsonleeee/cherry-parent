
function BINOLPTRPS15() {
	
};

BINOLPTRPS15.prototype = {
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

							if (name == 'propValId') {
								propValArr.push('"'+ encodeURIComponent($.trim($this.val()).replace(/\\/g, '\\\\').replace(/"/g, '\\"')) + '"');
							}
							JSON.push('"'+ encodeURIComponent(name)+ '":"'+ 
							encodeURIComponent($.trim($this.val()).replace(/\\/g, '\\\\').replace(/"/g, '\\"')) + '"');

						}
					}
				});
		JSON.push('"propValArr":[' + propValArr.toString() + ']');
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
	"searchSaleCount" : function() {
		var url = $("#saleCountUrl").attr("href");
		var params= $("#mainForm").find("td.columns").find(":input").serialize();
		//选中的产品
		var selPrtVendorIdArr = new Array();
		$("input[name='prtVendorId']").each(function(){
			selPrtVendorIdArr.push($(this).val());
		});
		params += "&selPrtVendorIdArr=" + JSON.stringify(selPrtVendorIdArr);
		// 产品分类
		params += "&cateInfo=" + this.toJSONArr($("#cateInfo").find(".detail").children().children());
		params = params + "&csrftoken=" +$("#csrftoken").val();
		params = params + "&" +getRangeParams();
		url = url + "?" + params;
		var countModel = $("#mainForm").find("input[name='countModel']:checked").val();
//		var saleTimeStart = $("#saleTimeStart").val();
//		var saleTimeEnd = $("#saleTimeEnd").val();
		var fiscalMonthFlag = $("#mainForm").find("input[name='fiscalMonthFlag']").val();
		$("#countResultDiv").empty();
		var table = '<table id="CountResultDataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%"><thead><tr>';
		table += '<th>'+ $("#no").html() +'</th>';
		if(countModel == '0' || countModel == '1') {
			table += '<th>'+ $("#binolptrps15_departName").html() +'</th>';
			table += '<th>'+ $("#binolptrps15_busniessPrincipal").html() +'</th>';
		}
		if(countModel == '0' || countModel == '2') {
			table += '<th>'+ $("#binolptrps15_unitCode").html() +'</th>';
			table += '<th>'+ $("#binolptrps15_barCode").html() +'</th>';
			table += '<th>'+ $("#binolptrps15_nameTotal").html() +'</th>';
			table += '<th>'+ $("#binolptrps15_saleType").html() +'</th>';
		}
		table += '<th>'+ $("#binolptrps15_quantity").html() +'</th>';
		table += '<th>'+ $("#binolptrps15_amount").html() +'</th>';
		if(fiscalMonthFlag == '1') {
			table += '<th><strong>'+ $("#binolptrps15_monthQuantity").html() +'</strong></th>';
			table += '<th><strong>'+ $("#binolptrps15_monthAmount").html() +'</strong></th>';
		} else if(fiscalMonthFlag == '2') {
			table += '<th><strong>'+ $("#binolptrps15_yearQuantity").html() +'</strong></th>';
			table += '<th><strong>'+ $("#binolptrps15_yearAmount").html() +'</strong></th>';
		}
		table += '</tr></thead></table>';
		$("#countResultDiv").html(table);
		
		oTableArr = new Array(null,null);
		fixedColArr = new Array(null,null);
		var aoColumns = [];
		aoColumns.push({"sName": "number", "sWidth": "5%", "bSortable": false});
		if(countModel == '0') {
			if(fiscalMonthFlag == '1') {
				aoColumns.push({ "sName": "departName", "sWidth": "15%" });
				aoColumns.push({ "sName": "busniessPrincipal", "sWidth": "15%" });
				aoColumns.push({ "sName": "unitCode", "sWidth": "10%" });
				aoColumns.push({ "sName": "barCode", "sWidth": "10%" });
				aoColumns.push({ "sName": "nameTotal", "sWidth": "10%" });
				aoColumns.push({ "sName": "saleType", "sWidth": "10%" });
				aoColumns.push({ "sName": "quantity", "sWidth": "10%" });
				aoColumns.push({ "sName": "amount", "sWidth": "10%" });
				aoColumns.push({ "sName": "monthQuantity", "sWidth": "10%"});
				aoColumns.push({ "sName": "monthAmount", "sWidth": "10%"});
			} else if(fiscalMonthFlag == '2') {
				aoColumns.push({ "sName": "departName", "sWidth": "15%" });
				aoColumns.push({ "sName": "busniessPrincipal", "sWidth": "15%" });
				aoColumns.push({ "sName": "unitCode", "sWidth": "10%" });
				aoColumns.push({ "sName": "barCode", "sWidth": "10%" });
				aoColumns.push({ "sName": "nameTotal", "sWidth": "10%" });
				aoColumns.push({ "sName": "saleType", "sWidth": "10%" });
				aoColumns.push({ "sName": "quantity", "sWidth": "10%" });
				aoColumns.push({ "sName": "amount", "sWidth": "10%" });
				aoColumns.push({ "sName": "yearQuantity", "sWidth": "10%"});
				aoColumns.push({ "sName": "yearAmount", "sWidth": "10%"});
			} else {
				aoColumns.push({ "sName": "departName", "sWidth": "20%" });
				aoColumns.push({ "sName": "busniessPrincipal", "sWidth": "15%" });
				aoColumns.push({ "sName": "unitCode", "sWidth": "15%" });
				aoColumns.push({ "sName": "barCode", "sWidth": "15%" });
				aoColumns.push({ "sName": "nameTotal", "sWidth": "15%" });
				aoColumns.push({ "sName": "saleType", "sWidth": "10%" });
				aoColumns.push({ "sName": "quantity", "sWidth": "10%" });
				aoColumns.push({ "sName": "amount", "sWidth": "10%" });
			}
		}
		if(countModel == '1') {
			if(fiscalMonthFlag == '1') {
				aoColumns.push({ "sName": "departName", "sWidth": "25%" });
				aoColumns.push({ "sName": "busniessPrincipal", "sWidth": "15%" });
				aoColumns.push({ "sName": "quantity", "sWidth": "15%" });
				aoColumns.push({ "sName": "amount", "sWidth": "15%" });
				aoColumns.push({ "sName": "monthQuantity", "sWidth": "20%"});
				aoColumns.push({ "sName": "monthAmount", "sWidth": "20%"});
			} else if(fiscalMonthFlag == '2') {
				aoColumns.push({ "sName": "departName", "sWidth": "25%" });
				aoColumns.push({ "sName": "busniessPrincipal", "sWidth": "15%" });
				aoColumns.push({ "sName": "quantity", "sWidth": "15%" });
				aoColumns.push({ "sName": "amount", "sWidth": "15%" });
				aoColumns.push({ "sName": "yearQuantity", "sWidth": "20%"});
				aoColumns.push({ "sName": "yearAmount", "sWidth": "20%"});
			} else {
				aoColumns.push({ "sName": "departName", "sWidth": "35%" });
				aoColumns.push({ "sName": "busniessPrincipal", "sWidth": "15%" });
				aoColumns.push({ "sName": "quantity", "sWidth": "30%" });
				aoColumns.push({ "sName": "amount", "sWidth": "30%" });
			}
		}
		if(countModel == '2') {
			if(fiscalMonthFlag == '1') {
				aoColumns.push({ "sName": "unitCode", "sWidth": "10%" });
				aoColumns.push({ "sName": "barCode", "sWidth": "10%" });
				aoColumns.push({ "sName": "nameTotal", "sWidth": "15%" });
				aoColumns.push({ "sName": "saleType", "sWidth": "10%" });
				aoColumns.push({ "sName": "quantity", "sWidth": "10%" });
				aoColumns.push({ "sName": "amount", "sWidth": "10%" });
				aoColumns.push({ "sName": "monthQuantity", "sWidth": "15%"});
				aoColumns.push({ "sName": "monthAmount", "sWidth": "15%"});
			} else if(fiscalMonthFlag == '2') {
				aoColumns.push({ "sName": "unitCode", "sWidth": "10%" });
				aoColumns.push({ "sName": "barCode", "sWidth": "10%" });
				aoColumns.push({ "sName": "nameTotal", "sWidth": "15%" });
				aoColumns.push({ "sName": "saleType", "sWidth": "10%" });
				aoColumns.push({ "sName": "quantity", "sWidth": "10%" });
				aoColumns.push({ "sName": "amount", "sWidth": "10%" });
				aoColumns.push({ "sName": "yearQuantity", "sWidth": "15%"});
				aoColumns.push({ "sName": "yearAmount", "sWidth": "15%"});
			} else {
				aoColumns.push({ "sName": "unitCode", "sWidth": "15%" });
				aoColumns.push({ "sName": "barCode", "sWidth": "15%" });
				aoColumns.push({ "sName": "nameTotal", "sWidth": "20%" });
				aoColumns.push({ "sName": "saleType", "sWidth": "15%" });
				aoColumns.push({ "sName": "quantity", "sWidth": "15%" });
				aoColumns.push({ "sName": "amount", "sWidth": "15%" });
			}
		}
		var aaSorting = []
		if(countModel == '0') {
			aaSorting.push([8, "desc"])
		}
		if(countModel == '1') {
			aaSorting.push([4, "desc"])
		}
		if(countModel == '2') {
			aaSorting.push([6, "desc"])
		}
		// 表格设置
		tableSetting = {
			 // 表格ID
			 tableId : '#CountResultDataTable',
			 // 数据URL
			 url : url,
			 // 横向滚动条出现的临界宽度
			 sScrollX : "100%",
			 callbackFun: function(msg) {
			 	var $msg = $("<div></div>").html(msg);
			 	var $saleCountInfoDiv = $("#saleCountInfoDiv",$msg);
			 	var $fiscalMonthInfoDiv = $("#fiscalMonthInfoDiv",$msg);
		 		if($saleCountInfoDiv.length > 0){
		 			$("#saleCountInfoDiv").html($saleCountInfoDiv.html());
		 		}else{
		 			$("#saleCountInfoDiv").empty();
		 		}
		 		if($fiscalMonthInfoDiv.length > 0){
		 			$("#fiscalMonthInfoDiv").html($fiscalMonthInfoDiv.html());
		 		}else{
		 			$("#fiscalMonthInfoDiv").empty();
		 		}
			}
		};
		tableSetting.aoColumns = aoColumns;
		tableSetting.aaSorting = aaSorting;
		$("#section").show();
		// 调用获取表格函数
		getTable(tableSetting);
    },
    "exportExcel" : function() {
        if($(".dataTables_empty:visible").length==0) {
        	//alert(oTableArr[0].fnSettings().sAjaxSource)
    		var url = $("#exportUrl").attr("href");
    		var params= $("#mainForm").find("td.columns").find(":input").serialize();
    		//选中的产品
    		var selPrtVendorIdArr = new Array();
    		$("input[name='prtVendorId']").each(function(){
    			selPrtVendorIdArr.push($(this).val());
    		});
    		params += "&selPrtVendorIdArr=" + JSON.stringify(selPrtVendorIdArr);
    		// 产品分类
    		params += "&cateInfo=" + this.toJSONArr($("#cateInfo").find(".detail").children().children());
    		params = params + "&csrftoken=" +$("#csrftoken").val();
    		params = params + "&" +getRangeParams();
            url = url + "?" + params;
            document.location.href = url;
        }
    },
    /**
     * 取得起止日期是否处于【同一财务月】标记
     */
    "getFiscalFlag" : function() {
    	var fiscalFlagUrl = $("#fiscalFlagUrl").attr("href");
    	$('#mainForm :input').each(function(){
			$(this).val($.trim(this.value));
		});
		// 查询参数序列化
		var params= $("#mainForm").serialize();
    	var callback = function(msg){
			$("#fiscalMonthFlag").val(msg);
		};
		cherryAjaxRequest({
			url : fiscalFlagUrl,
			param : params,
			callback : callback
		});
    },
    
	/**
	 * 产品弹出框
	 */
	"openProPopup" : function(_this){
    	
		var option = {
     	   	targetId: "production_ID",
           	checkType : "checkbox",
           	mode : 2,
           	brandInfoId : $("#brandInfoId").val(),
           	optionalValidFlag : 1, // 自选有效区分
            popValidFlag : 2, // 有效区别（2为全部即包括有效及无效 ）
	       	getHtmlFun:function(info){
 		       	var html = '<tr>'; 
 				html += '<td class="hide">' + '<input type="hidden" name="prtVendorId" value="' + info.proId + '"/></td>';
 				html += '<td style="width:25%;">' + info.unitCode + '</td>';
 				html += '<td style="width:25%;">' + info.barCode + '</td>';
 				html += '<td style="width:35%;">' + info.nameTotal + '</td>';
 				html += '<td class="center" style="width:15%;"><a href="javascript:void(0);" onClick="binolptrps15.deleteHtml(this);return false;">'+$("#deleteButton").text()+'</a></td>';
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


var binolptrps15 =  new BINOLPTRPS15();

$(function(){
	//先进行判断起止日期是否处于同一财务月 
	binolptrps15.getFiscalFlag();
//	productBinding({elementId:"productName",showNum:20});
});
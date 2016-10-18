function BINOLMBMBM05() {};

BINOLMBMBM05.prototype = {
	"searchSaleRecord" : function() {
		var $saleCherryForm = $('#saleCherryForm');
		if(!$saleCherryForm.valid()) {
			return false;
		}
		var url = $("#searchSaleRecordUrl").attr("href");
		var displayFlag = $("#tabSelect").find(".ui-tabs-selected").attr("id");
		url += "?" + getSerializeToken();
		url += "&" + $saleCherryForm.serialize();
		url += '&displayFlag=' + displayFlag;
		if ($("#memberClubId").length > 0) {
			url += "&" + $("#memberClubId").serialize();
		}
		$("#memSaleDataDiv").empty();
		var sysConfigShowUniqueCode = $("#sysConfigShowUniqueCode").val();
		var table = '<table id="memSaleDataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table '+(displayFlag == '0' ? 'memEventDataTable' : '')+'" width="100%"><thead><tr>';
		if(displayFlag == '0') {
			table += '<th>'+ $("#binolmbmbm05_saleMemCode").html() +'</th>';
			table += '<th>'+ $("#binolmbmbm05_billCode").html() +'</th>';
			table += '<th>'+ $("#binolmbmbm05_saleType").html() +'</th>';
			if($("#binolmbmbm05_departCode").length > 0) {
				table += '<th>'+ $("#binolmbmbm05_departCode").html() +'</th>';
			}
			table += '<th>'+ $("#binolmbmbm05_saleTime").html() +'</th>';
			table += '<th>'+ $("#binolmbmbm05_quantity").html() +'</th>';
			table += '<th>'+ $("#binolmbmbm05_amount").html() +'</th>';
			table += '<th>'+ $("#binolmbmbm05_billState").html() +'</th>';
		} else {
			table += '<th>'+ $("#binolmbmbm05_saleMemCode").html() +'</th>';
			table += '<th>'+ $("#binolmbmbm05_proName").html() +'</th>';
			table += '<th>'+ $("#binolmbmbm05_unitCode").html() +'</th>';
			table += '<th>'+ $("#binolmbmbm05_barCode").html() +'</th>';
			table += '<th>'+ $("#binolmbmbm05_saleType").html() +'</th>';
			table += '<th>'+ $("#binolmbmbm05_detailSaleType").html() +'</th>';
			table += '<th>'+ $("#binolmbmbm05_quantity").html() +'</th>';
			table += '<th>'+ $("#binolmbmbm05_pricePay").html() +'</th>';
			if($("#binolmbmbm05_departCode").length > 0) {
				table += '<th>'+ $("#binolmbmbm05_departCode").html() +'</th>';
			}
			table += '<th>'+ $("#binolmbmbm05_saleEmployee").html() +'</th>';
			table += '<th>'+ $("#binolmbmbm05_saleTime").html() +'</th>';
			table += '<th>'+ $("#binolmbmbm05_campaignName").html() +'</th>';
			table += '<th>'+ $("#binolmbmbm05_saleExt").html() +'</th>';
			if(sysConfigShowUniqueCode == "1"){
				table += '<th>'+ $("#binolmbmbm05_UniqueCode").html() +'</th>';
			}
			table += '<th>'+ $("#binolmbmbm05_billCode").html() +'</th>';
		}
		table += '</tr></thead></table>';
		$("#memSaleDataDiv").html(table);
		
		oTableArr = new Array(null,null);
		fixedColArr = new Array(null,null);
		var aoColumns = [];
		var aaSorting = [];
		if(displayFlag == '0') {
			aoColumns.push({ "sName": "memberCode", "sWidth": "10%", "bSortable": false });
			aoColumns.push({ "sName": "billCode", "sWidth": "25%" });
			aoColumns.push({ "sName": "saleType", "sWidth": "10%" });
			if($("#binolmbmbm05_departCode").length > 0) {
				aoColumns.push({ "sName": "departCode", "sWidth": "20%" });
			}
			aoColumns.push({ "sName": "saleTime", "sWidth": "15%" });
			aoColumns.push({ "sName": "quantity", "sWidth": "10%" });
			aoColumns.push({ "sName": "amount", "sWidth": "10%" });
			aoColumns.push({ "sName": "billState", "sWidth": "10%" });
			
			if($("#binolmbmbm05_departCode").length > 0) {
				aaSorting.push([4, "desc"]);
			} else {
				aaSorting.push([3, "desc"]);
			}
		} else {
			aoColumns.push({ "sName": "memberCode", "sWidth": "8%", "bSortable": false });
			aoColumns.push({ "sName": "prtName", "sWidth": "8%" });
			aoColumns.push({ "sName": "unitCode", "sWidth": "8%" });
			aoColumns.push({ "sName": "barCode", "sWidth": "8%" });
			aoColumns.push({ "sName": "saleType", "sWidth": "8%" });
			aoColumns.push({ "sName": "detailSaleType", "sWidth": "8%" });
			aoColumns.push({ "sName": "quantity", "sWidth": "8%" });
			aoColumns.push({ "sName": "pricePay", "sWidth": "8%" });
			if($("#binolmbmbm05_departCode").length > 0) {
				aoColumns.push({ "sName": "departCode", "sWidth": "13%" });
			}
			aoColumns.push({ "sName": "employeeCode", "sWidth": "13%" });
			aoColumns.push({ "sName": "saleTime", "sWidth": "10%" });
			aoColumns.push({ "sName": "activityName", "sWidth": "10%", "bSortable": false });
			aoColumns.push({ "sName": "saleExt", "sWidth": "10%", "bSortable": false });
			if(sysConfigShowUniqueCode == "1"){
				aoColumns.push({ "sName": "uniqueCode", "sWidth": "10%", "bSortable": false });
			}
			aoColumns.push({ "sName": "billCode", "sWidth": "13%", "bSortable": false });
			
			if($("#binolmbmbm05_departCode").length > 0) {
				aaSorting.push([10, "desc"]);
			} else {
				aaSorting.push([9, "desc"]);
			}
		}
		
		// 表格设置
		var tableSetting = {
				// 表格ID
				tableId : '#memSaleDataTable',
				// 数据URL
				url : url,
				// 横向滚动条出现的临界宽度
				sScrollX : "100%",
				bAutoWidth : true,
				fnDrawCallback : function() {
					if(displayFlag == '0') {
						$("#memSaleDataTable").find('tr').click(function() {
							binolmbmbm10.searchDetail(this);
						});
					}
				},
				callbackFun: function(msg) {
			 		var $msg = $("<div></div>").html(msg);
			 		var $headInfo = $("#saleCountInfo",$msg);
			 		if($headInfo.length > 0) {
			 			$("#saleCountInfo").html($headInfo.html());
			 		} else {
			 			$("#saleCountInfo").empty();
			 		}
				}
		};
		tableSetting.aoColumns = aoColumns;
		tableSetting.aaSorting = aaSorting;
		// 调用获取表格函数
		getTable(tableSetting);
//			$(window).bind('resize', function () {
//				if(oTableArr[0]) {
//					oTableArr[0].fnAdjustColumnSizing();
//				}
//			});
    },
    "changeTab" : function(thisObj) {
    	
    	$(thisObj).siblings().removeClass('ui-tabs-selected');
    	$(thisObj).addClass('ui-tabs-selected');
    	binolmbmbm05.searchSaleRecord();
    },
    "exportExcel" : function() {
        if($(".dataTables_empty:visible").length==0) {
        	//alert(oTableArr[0].fnSettings().sAjaxSource)
    		var url = $("#exportUrl").attr("href");
    		url += "?" + getSerializeToken();
    		url += "&" + $("#saleCherryForm").serialize();
            document.location.href = url;
        }
    }
};

var binolmbmbm05 =  new BINOLMBMBM05();

$(function() {
	$('#saleTimeStart').cherryDate({
		beforeShow: function(input){
			var value = $('#saleTimeEnd').val();
			return [value,'maxDate'];
		}
	});
	$('#saleTimeEnd').cherryDate({
		beforeShow: function(input){
			var value = $('#saleTimeStart').val();
			return [value,'minDate'];
		}
	});
	// 表单验证初期化
	cherryValidate({
		formId: 'saleCherryForm',
		rules: {
			saleTimeStart: {dateValid: true},
			saleTimeEnd: {dateValid: true}
		}
	});
	$('#expandConditionSale').click(function(){
		if($(this).children('.ui-icon').is('.ui-icon-triangle-1-n')) {
			$(this).children('.ui-icon').removeClass('ui-icon-triangle-1-n').addClass('ui-icon-triangle-1-s');
			$('#saleConditionDiv').show();
		} else {
			$(this).children('.ui-icon').removeClass('ui-icon-triangle-1-s').addClass('ui-icon-triangle-1-n');
			$('#saleConditionDiv').hide();
		}
	});
	productBinding({elementId:"nameTotal",showNum:20});
	binolmbmbm05.searchSaleRecord();
});


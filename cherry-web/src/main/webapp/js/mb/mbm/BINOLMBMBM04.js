function BINOLMBMBM04() {};

BINOLMBMBM04.prototype = {
    "searchPointDetail" : function() {
    	var $pointDetailForm = $('#pointDetailForm');
    	if(!$pointDetailForm.valid()) {
			return false;
		}
		var url = $("#searchPointInfoUrl").attr("href");
		var displayFlag = $("#tabSelect").find(".ui-tabs-selected").attr("id");
		url += "?" + getSerializeToken();
		url += "&" + $pointDetailForm.serialize();
		url += '&displayFlag=' + displayFlag;
		if ($("#memberClubId").length > 0) {
			url += "&" + $("#memberClubId").serialize();
		}
		$("#pointDetailDataDiv").empty();
		var table = '<table id="pointDetailDataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table '+(displayFlag == '0' ? 'memEventDataTable' : '')+'" width="100%"><thead><tr>';
		if(displayFlag == '0') {
			table += '<th>'+ $("#binolmbmbm04_memberCode").html() +'</th>';
			table += '<th>'+ $("#binolmbmbm04_billCode").html() +'</th>';
			table += '<th>'+ $("#binolmbmbm04_billType").html() +'</th>';
			if($("#binolmbmbm04_departCode").length > 0) {
				table += '<th>'+ $("#binolmbmbm04_departCode").html() +'</th>';
			}
			table += '<th>'+ $("#binolmbmbm04_changeDate").html() +'</th>';
			table += '<th>'+ $("#binolmbmbm04_amount").html() +'</th>';
			table += '<th>'+ $("#binolmbmbm04_quantity").html() +'</th>';
			table += '<th>'+ $("#binolmbmbm04_point").html() +'</th>';
			table += '<th>'+ $("#binolmbmbm04_hasRelevantSRCode").html() +'</th>';
		} else {
			table += '<th>'+ $("#binolmbmbm04_memberCode").html() +'</th>';
			table += '<th>'+ $("#binolmbmbm04_proName").html() +'</th>';
			table += '<th>'+ $("#binolmbmbm04_unitCode").html() +'</th>';
			table += '<th>'+ $("#binolmbmbm04_barCode").html() +'</th>';
			table += '<th>'+ $("#binolmbmbm04_saleType").html() +'</th>';
			table += '<th>'+ $("#binolmbmbm04_proPrice").html() +'</th>';
			table += '<th>'+ $("#binolmbmbm04_proQuantity").html() +'</th>';
			table += '<th>'+ $("#binolmbmbm04_proPoint").html() +'</th>';
			table += '<th>'+ $("#binolmbmbm04_pointType").html() +'</th>';
			if($("#binolmbmbm04_departCode").length > 0) {
				table += '<th>'+ $("#binolmbmbm04_departCode").html() +'</th>';
			}
			table += '<th>'+ $("#binolmbmbm04_changeDate").html() +'</th>';
			table += '<th>'+ $("#binolmbmbm04_combCampaignName").html() +'</th>';
			table += '<th>'+ $("#binolmbmbm04_mainCampaignName").html() +'</th>';
			table += '<th>'+ $("#binolmbmbm04_subCampaignName").html() +'</th>';
			table += '<th>'+ $("#binolmbmbm04_reason").html() +'</th>';
			table += '<th>'+ $("#binolmbmbm04_billCode").html() +'</th>';
			table += '<th>'+ $("#binolmbmbm04_relevantSRCode").html() +'</th>';
			table += '<th>'+ $("#binolmbmbm04_relevantSRTime").html() +'</th>';
		}
		table += '</tr></thead></table>';
		$("#pointDetailDataDiv").html(table);
		
		oTableArr = new Array(null,null);
		fixedColArr = new Array(null,null);
		var aoColumns = [];
		var aaSorting = [];
		if(displayFlag == '0') {
			aoColumns.push({ "sName": "memCode", "sWidth": "10%", "bSortable": false });
			aoColumns.push({ "sName": "billCode", "sWidth": "15%" });
			aoColumns.push({ "sName": "billType", "sWidth": "10%" });
			if($("#binolmbmbm04_departCode").length > 0) {
				aoColumns.push({ "sName": "departCode", "sWidth": "10%" });
			}
			aoColumns.push({ "sName": "changeDate", "sWidth": "15%" });
			aoColumns.push({ "sName": "amount", "sWidth": "10%" });
			aoColumns.push({ "sName": "quantity", "sWidth": "10%" });
			aoColumns.push({ "sName": "point", "sWidth": "10%" });
			aoColumns.push({ "sName": "hasRelevantSRCode", "sWidth": "10%", "bSortable": false });
			
			if($("#binolmbmbm04_departCode").length > 0) {
				aaSorting.push([4, "desc"]);
			} else {
				aaSorting.push([3, "desc"]);
			}
		} else {
			aoColumns.push({ "sName": "memCode", "sWidth": "5%", "bSortable": false });
			aoColumns.push({ "sName": "proName", "sWidth": "5%" });
			aoColumns.push({ "sName": "unitCode", "sWidth": "5%" });
			aoColumns.push({ "sName": "barCode", "sWidth": "5%" });
			aoColumns.push({ "sName": "saleType", "sWidth": "5%" });
			aoColumns.push({ "sName": "price", "sWidth": "5%" });
			aoColumns.push({ "sName": "quantity", "sWidth": "5%" });
			aoColumns.push({ "sName": "point", "sWidth": "5%" });
			aoColumns.push({ "sName": "pointType", "sWidth": "5%" });
			if($("#binolmbmbm04_departCode").length > 0) {
				aoColumns.push({ "sName": "departCode", "sWidth": "10%" });
			}
			aoColumns.push({ "sName": "changeDate", "sWidth": "10%" });
			aoColumns.push({ "sName": "combCampaignName", "sWidth": "5%", "bSortable": false });
			aoColumns.push({ "sName": "mainCampaignName", "sWidth": "5%", "bSortable": false });
			aoColumns.push({ "sName": "subCampaignName", "sWidth": "5%", "bSortable": false });
			aoColumns.push({ "sName": "reason", "sWidth": "10%", "bSortable": false });
			aoColumns.push({ "sName": "billCode", "sWidth": "10%", "bSortable": false });
			aoColumns.push({ "sName": "srCode", "sWidth": "10%", "bSortable": false });
			aoColumns.push({ "sName": "srTime", "sWidth": "10%", "bSortable": false });
			
			if($("#binolmbmbm04_departCode").length > 0) {
				aaSorting.push([10, "desc"]);
			} else {
				aaSorting.push([9, "desc"]);
			}
		}
		
		// 表格设置
		var tableSetting = {
				// 表格ID
				tableId : '#pointDetailDataTable',
				// 数据URL
				url : url,
				// 横向滚动条出现的临界宽度
				sScrollX : "100%",
				bAutoWidth : true,
				fnDrawCallback : function() {
					if(displayFlag == '0') {
						$("#pointDetailDataTable").find('tr').click(function() {
							binolmbmbm10.searchDetail(this);
						});
					}
				}
		};
		tableSetting.aoColumns = aoColumns;
		tableSetting.aaSorting = aaSorting;
		// 调用获取表格函数
		getTable(tableSetting);
//		$(window).bind('resize', function () {
//			if(oTableArr[1]) {
//				oTableArr[1].fnAdjustColumnSizing();
//			}
//		});
    },
    "changeTab" : function(thisObj) {
    	
    	$(thisObj).siblings().removeClass('ui-tabs-selected');
    	$(thisObj).addClass('ui-tabs-selected');
    	binolmbmbm04.searchPointDetail();
    },
    "exportExcel" : function() {
        if($(".dataTables_empty:visible").length==0) {
        	//alert(oTableArr[0].fnSettings().sAjaxSource)
    		var url = $("#exportUrl").attr("href");
    		url += "?" + getSerializeToken();
    		url += "&" + $("#pointDetailForm").serialize();
            document.location.href = url;
        }
    },
    "changeClub" : function() {
    	binolmbmbm04.searchMemPoint();
    	binolmbmbm04.searchPointDetail();
    },
    "searchMemPoint" : function() {
    	var pointUrl = $("#searchMemPointUrl").attr("href");
    	var param = $("#memberInfoId").serialize() + '&' + $("#memberClubId").serialize();
		cherryAjaxRequest({
			url: pointUrl,
			param: param,
			callback: function(msg) {
				$(".point-info").remove();
				$("#pointDiv").append(msg);
			}
		});
    }
};

var binolmbmbm04 =  new BINOLMBMBM04();

$(function() {
	$('#changeDateStart').cherryDate({
		beforeShow: function(input){
			var value = $('#changeDateEnd').val();
			return [value,'maxDate'];
		}
	});
	$('#changeDateEnd').cherryDate({
		beforeShow: function(input){
			var value = $('#changeDateStart').val();
			return [value,'minDate'];
		}
	});
	// 表单验证初期化
	cherryValidate({
		formId: 'pointDetailForm',
		rules: {
			changeDateStart: {dateValid: true},
			changeDateEnd: {dateValid: true},
			memPointStart: {numberValid: true},
			memPointEnd: {numberValid: true}
		}
	});
	$('#expandConditionPoint').click(function(){
		if($(this).children('.ui-icon').is('.ui-icon-triangle-1-n')) {
			$(this).children('.ui-icon').removeClass('ui-icon-triangle-1-n').addClass('ui-icon-triangle-1-s');
			$('#pointConditionDiv').show();
		} else {
			$(this).children('.ui-icon').removeClass('ui-icon-triangle-1-s').addClass('ui-icon-triangle-1-n');
			$('#pointConditionDiv').hide();
		}
	});
	productBinding({elementId:"prtNamePoint",showNum:20,targetId:"prtVendorIdPoint"});
	counterBinding({elementId:"departCode",showNum:20,selected:"code"});
	binolmbmbm04.searchPointDetail();
});


function BINOLBSCNT10() {};

BINOLBSCNT10.prototype = {
	"searchDetail" : function() {

		var displayFlag = $("#tabSelect").find(".ui-tabs-selected").attr("id");
		var url = '';
		if(displayFlag == '0'){
			url = $("#searchPointCounterDetail1").attr("href");
		}else{
			url = $("#searchCounterPointLimitDetail2").attr("href");
		}


		$("#tabsPanel").empty();

		var table = '';
		table = '<table id="dataTable_pointCounterDetail" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">';
		table += '<thead>';
		table += '<tr>';
		if(displayFlag == '0'){
			table += '<th style="text-align: center">' + $("#createTime").html() + '</th>';
			table += '<th style="text-align: center">' + $("#modifyType").html() + '</th>';
			table += '<th style="text-align: center">' + $("#modifyer").html() + '</th>';
			table += '<th style="text-align: center">' + $("#startDate").html() + '</th>';
			table += '<th style="text-align: center">' + $("#endDate").html() + '</th>';
			table += '<th style="text-align: center">' + $("#comment").html() + '</th>';
			table += '</tr>';
			table += '</thead>';
			table += '</table>';

			$("#tabsPanel").html($('#tabsPanel1').html());
			$("#table_div1").html(table);

		}else{
			table += '<th style="text-align: center">' + $("#number").html() + '</th>';
			table += '<th style="text-align: center">' + $("#tradeType").html() + '</th>';
			table += '<th style="text-align: center">' + $("#billNo").html() + '</th>';
			table += '<th style="text-align: center">' + $("#tradeTime").html() + '</th>';
			table += '<th style="text-align: center">' + $("#totalAmount").html() + '</th>';
			table += '<th style="text-align: center">' + $("#limitAmount").html() + '</th>';
			table += '<th style="text-align: center">' + $("#memberCard").html() + '</th>';
			table += '<th style="text-align: center">' + $("#comment").html() + '</th>';
			table += '</tr>';
			table += '</thead>';
			table += '</table>';

			$("#tabsPanel").html($('#tabsPanel2').html());
			$("#table_div2").html(table);
		}

		var tableSetting = {};

		oTableArr = new Array(null,null);
		fixedColArr = new Array(null,null);
		var aoColumns = [];
		var aaSorting = [];

		if(displayFlag == '0') {

			aoColumns.push({ "sName": "CreateTime","sClass":"center"});
			aoColumns.push({ "sName": "ModifyType","bSortable": false,"sClass":"center"});
			aoColumns.push({ "sName": "EmployeeName","sClass":"center"});
			aoColumns.push({ "sName": "StartDate","sClass":"center"});
			aoColumns.push({ "sName": "EndDate","sClass":"center"});
			aoColumns.push({ "sName": "Comment","bSortable": false});

			aaSorting.push([0, "desc"]);
		}else{
			aoColumns.push({ "sName": "No","sClass":"center"});
			aoColumns.push({ "sName": "TradeType","bSortable": false,"sClass":"center"});
			aoColumns.push({ "sName": "BillNo","sClass":"center"});
			aoColumns.push({ "sName": "TradeTime","sClass":"center"});
			aoColumns.push({ "sName": "Amount","sClass":"center"});
			aoColumns.push({ "sName": "PointChange","sClass":"center"});
			aoColumns.push({ "sName": "MemberCode","sClass":"center"});
			aoColumns.push({ "sName": "Comment","bSortable": false});

			aaSorting.push([3, "desc"]);
		}

		var binolmbmbm10 =  new BINOLMBMBM10();

		// 表格设置
		var tableSetting = {
			// 表格ID
			tableId : '#dataTable_pointCounterDetail',
			// 数据URL
			url : url,
			// 横向滚动条出现的临界宽度
			sScrollX : "100%",
			bAutoWidth : true,
			fnDrawCallback : function() {
				if(displayFlag == '1') {

					$("#dataTable_pointCounterDetail").find('tr td span[class="saleTypeClass"]').parent().parent().click(function() {
						binolmbmbm10.searchDetail(this);
					});
				}
			}
		};
		tableSetting.aoColumns = aoColumns;
		tableSetting.aaSorting = aaSorting;

		// 调用获取表格函数
		getTable(tableSetting);
	},
	"changeTab" : function(thisObj) {

		$(thisObj).siblings().removeClass('ui-tabs-selected');
		$(thisObj).addClass('ui-tabs-selected');
		binolbscnt10.searchDetail();
	},
	"exportExcel" : function() {
		if($(".dataTables_empty:visible").length==0) {
			//alert(oTableArr[0].fnSettings().sAjaxSource)
			var url = $("#downUrl").attr("href");
			url += "&" + getSerializeToken();
			//url += "&" + $("#pointDetailForm").serialize();
			//url += "&counterInfoId=${counterPointPlanInfo.BIN_CounterInfoID}";
			document.location.href = url;
		}
	}
};

var binolbscnt10 =  new BINOLBSCNT10();

$(function() {

	binolbscnt10.searchDetail();
});


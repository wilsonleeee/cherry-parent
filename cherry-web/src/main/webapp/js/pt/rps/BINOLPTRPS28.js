
function BINOLPTRPS28() {
	
};

BINOLPTRPS28.prototype = {
		
	"searchSaleTargetRpt" : function() {
		var url = $("#saleTargetRptUrl").attr("href");
		var params= $("#mainForm").find("div.column").find(":input").serialize();
		params = params + "&csrftoken=" +$("#csrftoken").val();
		params = params + "&" +getRangeParams();
		url = url + "?" + params;
		
		var countModel = $("#mainForm").find("input[name='countModel']:checked").val();
		
		$("#saleTargetRptDiv").empty();
		var table = '<table id="saleTargetRptDataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%"><thead><tr>';
		
		if(countModel == '0') {
			table += '<th>'+ $("#binolptrps28_departCode").html() +'</th>';
			table += '<th>'+ $("#binolptrps28_departName").html() +'</th>';
			table += '<th>'+ $("#binolptrps28_busniessPrincipal").html() +'</th>';
			table += '<th>'+ $("#binolptrps28_provinceName").html() +'</th>';
			table += '<th>'+ $("#binolptrps28_cityName").html() +'</th>';
			table += '<th>'+ $("#binolptrps28_channelName").html() +'</th>';
		}
		if(countModel == '1') {
			table += '<th>'+ $("#binolptrps28_regionName").html() +'</th>';
		}
		table += '<th>'+ $("#binolptrps28_amount").html() +'</th>';
		table += '<th>'+ $("#binolptrps28_targetMoney").html() +'</th>';
		table += '<th>'+ $("#binolptrps28_percent").html() +'</th>';
		table += '<th>'+ $("#binolptrps28_datePercent").html() +'</th>';
		table += '<th>'+ $("#binolptrps28_lastMoney").html() +'</th>';
		table += '<th>'+ $("#binolptrps28_predict").html() +'</th>';
		
		$("#saleTargetRptDiv").html(table);
		
		oTableArr = new Array(null,null);
		fixedColArr = new Array(null,null);
		var aoColumns = [];
		if(countModel == '0') {
			aoColumns.push({ "sName": "departCode", "sWidth": "10%" });
			aoColumns.push({ "sName": "departName", "sWidth": "10%" });
			aoColumns.push({ "sName": "busniessPrincipal", "sWidth": "10%" });
			aoColumns.push({ "sName": "provinceName", "sWidth": "10%" });
			aoColumns.push({ "sName": "cityName", "sWidth": "10%" });
			aoColumns.push({ "sName": "channelName", "sWidth": "10%" });
			
			aoColumns.push({ "sName": "amount", "sWidth": "8%" });
			aoColumns.push({ "sName": "targetMoney", "sWidth": "8%" });
			aoColumns.push({ "sName": "percent", "sWidth": "8%", "bSortable": false });
			aoColumns.push({ "sName": "datePercent", "sWidth": "8%", "bSortable": false });
			aoColumns.push({ "sName": "lastMoney", "sWidth": "8%" });
			aoColumns.push({ "sName": "predict", "sWidth": "10%", "bSortable": false });
		}
		if(countModel == '1') {
			aoColumns.push({ "sName": "departName", "sWidth": "16%" });
			
			aoColumns.push({ "sName": "amount", "sWidth": "14%" });
			aoColumns.push({ "sName": "targetMoney", "sWidth": "14%" });
			aoColumns.push({ "sName": "percent", "sWidth": "14%", "bSortable": false });
			aoColumns.push({ "sName": "datePercent", "sWidth": "14%", "bSortable": false });
			aoColumns.push({ "sName": "lastMoney", "sWidth": "14%" });
			aoColumns.push({ "sName": "predict", "sWidth": "14%", "bSortable": false });
		}
		
		var aaSorting = []
		if(countModel == '0') {
			aaSorting.push([5, "desc"])
		}
		if(countModel == '1') {
			aaSorting.push([1, "desc"])
		}
		
		// 表格设置
		tableSetting = {
			 // 表格ID
			 tableId : '#saleTargetRptDataTable',
			 // 数据URL
			 url : url,
			 // 横向滚动条出现的临界宽度
			 sScrollX : "100%",
			 callbackFun: function(msg) {
			 	
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
    		var params= $("#mainForm").find("div.column").find(":input").serialize();
    		params = params + "&csrftoken=" +$("#csrftoken").val();
    		params = params + "&" +getRangeParams();
            url = url + "?" + params;
            document.location.href = url;
        }
    }
};

var binolptrps28 =  new BINOLPTRPS28();
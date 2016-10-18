var BINOLWYWYL01_GLOBAL = function() {
	
};

BINOLWYWYL01_GLOBAL.prototype = {
	
	"search":function(){
		var $form = $('#mainForm');
		//表格列属性
		var aoColumnsArr = [
		                    {"sName" : "rowNumber", "sWidth": "5%", "bSortable": false},
		                    {"sName" : "memberName", "sWidth" : "10%"},
		                    {"sName" : "mobilePhone", "sWidth" : "10%"},
		                    {"sName" : "birthday", "sWidth" : "10%"},
		                    {"sName" : "gender", "sWidth" : "5%", "bSortable": false},
		                    {"sName" : "activityType", "sWidth" : "10%"},
		                    {"sName" : "applyGetDate", "sWidth" : "10%"},
		                    {"sName" : "city", "sWidth" : "10%"},
		                    {"sName" : "state", "sWidth" : "10%"},
		                    {"sName" : "act", "sWidth" : "10%", "bSortable": false}
		                    ];
		var url = $("#searchUrl").attr("href");
		var params = $form.serialize() + "&" + getSerializeToken();
		url = url + "?" + params;
		
		// 表格设置
		var tableSetting = {
			// datatable 对象索引
			index : 1,
			// 表格ID
			tableId : "#mainTable",
			// 数据URL
			url : url,
			// 排序列
			aaSorting : [ [ 1, "desc" ] ],
			// 表格列属性设置
			aoColumns : aoColumnsArr,
			// 不可设置显示或隐藏的列
			aiExclude : [ 0 , 1 ],
			// 横向滚动条出现的临界宽度
			sScrollX : "100%",
			// 固定列数
			fixedColumns : 2,
			// 默认显示行数
			iDisplayLength : 20,
			fnDrawCallback : function() {
			}
		};
		// 调用获取表格函数
		getTable(tableSetting);
	},
	
	// 生日框初始化处理
	"birthDayInit":function(){
		for(var i = 1; i <= 12; i++) {
			$("#birthDayMonthQ").append('<option value="'+i+'">'+i+'</option>');
		}
		$("#birthDayMonthQ").change(function(){
			var $date = $("#birthDayDateQ");
			var month = $(this).val();
			var options = '<option value="">'+$date.find('option').first().html()+'</option>';
			if(month == "") {
				$date.html(options);
				return;
			}
			var i = 1;
			var max = 0;
			if(month == '2') {
				max = 29;
			} else if(month == '4' || month == '6' || month == '9' || month == '11') {
				max = 30;
			} else {
				max = 31;
			}
			for(i = 1; i <= max; i++) {
				options += '<option value="'+i+'">'+i+'</option>';
			}
			$date.html(options);
		});
	},
	
	"subCampaignBinding":function(options){
		var csrftoken = '';
		if($("#csrftokenCode").length > 0) {
			csrftoken = $("#csrftokenCode").serialize();
			if(!csrftoken) {
				csrftoken = $('#csrftokenCode',window.opener.document).serialize();
			}
		} else {
			csrftoken = $('#csrftoken').serialize();
			if(!csrftoken) {
				csrftoken = $('#csrftoken',window.opener.document).serialize();
			}
		}
		var keycode = [13,37,38,39,40,16,17,9,27,123,18,20,93,91,144];
		var flag = false;
		var strPath = window.document.location.pathname;
		var postPath = strPath.substring(0, strPath.substr(1).indexOf('/') + 1);
		var url = postPath+"/wy/BINOLWYWYL01_getSubCampaignList.action"+"?"+csrftoken;
		var targetId = options.targetId ? options.targetId : "subCampaignCode";
		$('#'+options.elementId).autocompleteCherry(url,{
			extraParams:{
				subCampInfoStr: function() { return $('#'+options.elementId).val();},
				//默认是最多显示50条
				number:options.showNum ? options.showNum : 50,
				//活动状态（默认为全部不包含草稿）
				state:options.state ? options.state : -999
			},
			loadingClass: "ac_loading",
			minChars:1,
		    matchContains:1,
		    matchContains: true,
		    scroll: false,
		    cacheLength: 0,
		    width:300,
		    max:options.showNum ? options.showNum : 50,
			formatItem: function(row, i, max) {
				return escapeHTMLHandle(row[0])+" "+"["+escapeHTMLHandle(row[1])+"]";
			}
		}).result(function(event, data, formatted){
			if(options.targetDetail == null || options.targetDetail == undefined){
				$("#"+targetId).val(data[2]);
				$('#'+options.elementId).val(data[1]);
				$('#'+options.elementId).data("subCampaignName",$("#"+targetId).val());
				$('#'+options.elementId).data("change",true);
			}else if(options.targetDetail == true){
				var obj = {};
				obj.elementId = options.elementId;
				obj.subCampaignId = data[2];
				obj.subCampaignCode = data[0];
				obj.subCampaignName = data[1];
				//选择后执行方法
				if($.isFunction(options.afterSelectFun)){
					options.afterSelectFun.call(this,obj);
				}
			}
		}).bind("keydown",function(event){
			for( var i in keycode){
				if(event.keyCode == keycode[i]){
					flag = true;
				}
			}
			if(flag){
				if($('#'+options.elementId).val() == $('#'+options.elementId).data("subCampaignName")){
					$('#'+options.elementId).data("change",true);
					$('#'+options.elementId).data("flag",false);
				}else if($('#'+options.elementId).val() != $('#'+options.elementId).data("subCampaignName")){
					$('#'+options.elementId).data("change",false);
					$('#'+options.elementId).data("flag",false);
				}else{
					$('#'+options.elementId).data("change",false);
					$('#'+options.elementId).data("flag",true);
				}
				flag = false;
			}else{
				if((!$('#'+options.elementId).data("flag"))&&(!$('#'+options.elementId).data("change"))&&($('#'+options.elementId).val() != $('#'+options.elementId).data("subCampaignName"))){
					$("#"+targetId).val("");
				}else if($('#'+options.elementId).val() == $('#'+options.elementId).data("subCampaignName")){
					$('#'+options.elementId).data("change",true);
					$('#'+options.elementId).data("flag",true);
				}else if($('#'+options.elementId).val() != $('#'+options.elementId).data("subCampaignName")){
					$('#'+options.elementId).data("change",false);
					$('#'+options.elementId).data("flag",true);
				}else{
					$('#'+options.elementId).data("change",false);
					$('#'+options.elementId).data("flag",true);
				}
			}
		}).bind("change",function(){
			if(!$('#'+options.elementId).data("change")&&($('#'+options.elementId).data("flag"))){
				$("#"+targetId).val("");
			}else{
				$('#'+options.elementId).data("change",false);
			}
		}).data("flag",true);
	}
	
};

var BINOLWYWYL01 = new BINOLWYWYL01_GLOBAL();

$(document).ready(function(){
	// 初始化月日选择框
	BINOLWYWYL01.birthDayInit();
	// 默认条件为女
	$("#gender option:[value='2']").attr("selected",true);
	// 初始化日历控件
	$("#applyGetDate").cherryDate({
		beforeShow: function(input){
			return ["minValue", "maxValue"];
		}
	});
	// 绑定活动名称
	BINOLWYWYL01.subCampaignBinding({
		elementId:"subCampaignName",
		showNum:20,
		targetId:"campaignId"
	});
});

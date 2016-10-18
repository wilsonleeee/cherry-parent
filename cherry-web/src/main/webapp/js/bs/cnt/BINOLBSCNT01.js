
function BINOLBSCNT01() {};

BINOLBSCNT01.prototype = {	
	// 柜台查询
	"search" : function(){
		var maintainCoutSynergy=$("#maintainCoutSynergy").val();
		var url = $("#search_url").val();
		$('#mainForm :input').each(function(){
			$(this).val($.trim(this.value));
		});
		// 查询参数序列化
		var params= $("#mainForm").serialize();
		if(params != null && params != "") {
			url = url + "?" + params;
		}
		// 显示结果一览
		$("#section").show();
		// 表格设置
		var tableSetting = {
			// 表格ID
			tableId : '#dataTable',
			// 数据URL
			url : url,
			// 表格列属性设置
			aoColumns : [{ "sName": "checkbox","bSortable": false},
					{ "sName": "counterCode"},
					{ "sName": "counterNameIF"},
					{ "sName": "employeeName"},
					{ "sName": "brandNameChinese","bVisible": false},
					{ "sName": "region"},
					{ "sName": "province"},
					{ "sName": "city"},
					{ "sName": "operateMode","bVisible": false},//运营模式
					{ "sName": "belongFaction","bVisible": false},//所属系统
					{ "sName": "invoiceCompany","bVisible": false},//开票单位
					{ "sName": "channelName"},
					{ "sName": "resellerName","bVisible": false},
					{ "sName": "resellerDepartName"},
					{ "sName": "status","bVisible": false},
					{ "sName": "posFlag","bVisible": false},
					{ "sName": "employeeNum","bVisible": false},
					{ "sName": "counterAddress","bVisible": false,"bSortable": false},
					{ "sName": "busniessPrincipal","bVisible": false},//业务负责人
					{ "sName": "equipmentCode","bVisible": false},//银联设备号
					{ "sName": "managingType2","bVisible": false},//柜台类型
					{ "sName": "validFlag","bSortable": false,"sClass":"center"}],	
			// 不可设置显示或隐藏的列
			aiExclude :[0, 1],
			// 横向滚动条出现的临界宽度
			sScrollX : "100%",
			// 固定列数
			fixedColumns : 2
		};
		if(maintainCoutSynergy=="true"){
			tableSetting.aoColumns.splice(14,0,{ "sName": "counterSynergyFlag","sWidth": "15%","bVisible" : false});//柜台协同区分
		}
		// 调用获取表格函数
		getTable(tableSetting);
	},
	// 根据品牌ID筛选下拉列表
	"changeBrandInfo" : function(object,text) {
		// 清空省信息
		$('#provinceId').val("");
		$("#provinceText").text(text);
		$('#provinceTemp').empty();
		// 清空城市信息
		$('#cityId').val("");
		$("#cityText").text(text);
		$('#cityTemp ul').empty();
		// 清空渠道信息
		$('#channelId').empty();
		$('#channelId').append('<option value="">'+text+'</option>');
		if($(object).val() != "") {
			var callback = function(msg){
				if(msg) {
					// 默认地区
					var defaultTitle = $('#defaultTitle').text();
					// 全部
					var defaultText = $('#defaultText').text();
					var jsons = eval('('+msg+')');
					if(jsons.reginList.length > 0) {
						var html = '<div class="clearfix"><span class="label">'+defaultTitle+'</span><ul class="u2"><li onclick="bscom03_getNextRegin(this, \''+text+'\', 1);return false;">'+defaultText+'</li></ul></div>';
						for(var i in jsons.reginList) {
							html += '<div class="clearfix"><span class="label">'+escapeHTMLHandle(jsons.reginList[i].reginName)+'</span><ul class="u2">';
							for(var j in jsons.reginList[i].provinceList) {
								html += '<li id="'+jsons.reginList[i].provinceList[j].provinceId+'" onclick="bscom03_getNextRegin(this, \''+text+'\', 1);">'+escapeHTMLHandle(jsons.reginList[i].provinceList[j].provinceName)+'</li>';
							}
							html += '</ul></div>';
						}
						$('#provinceTemp').html(html);
					}
					if(jsons.channelList.length > 0) {
						for(var i in jsons.channelList) {
							$('#channelId').append('<option value="'+jsons.channelList[i].channelId+'">'+escapeHTMLHandle(jsons.channelList[i].channelName)+'</option>');
						}
					}
				}
			};
			cherryAjaxRequest({
				url: $('#filter_url').val(),
				param: $(object).serialize(),
				callback: callback
			});
		}
	},
	// 柜台启用停用处理
	"editValidFlag" : function(flag, url) {
		var param = "";
		if($('#dataTable_Cloned :input[checked]').length == 0) {
			$("#errorMessage").html($("#errorMessageTemp").html());
			return false;
		}
		param = $('#dataTable_Cloned :input[checked]').nextAll().serialize();
		var callback = function() {
			if(oTableArr[0] != null)oTableArr[0].fnDraw();
		};
		bscom03_deleteConfirm(flag, url, param, callback);
	},
	
	/* 
     * 导出Excel
     */
	"exportExcel" : function(){
		//无数据不导出
        if($(".dataTables_empty:visible").length==0){
		    if (!$('#mainForm').valid()) {
                return false;
            };
            var url = $("#downUrl").attr("href");
            var params= $("#mainForm").serialize();
            params = params + "&csrftoken=" +$("#csrftoken").val();
            url = url + "?" + params;
            window.open(url,"_self");
        }
    },
    
    // 在柜台主管名称的text框上绑定下拉框选项
	"counterBASBinding":function(options){
		var csrftoken = $('#csrftoken').serialize();
		if(!csrftoken) {
			csrftoken = $('#csrftoken',window.opener.document).serialize();
		}
		var strPath = window.document.location.pathname;
		var postPath = strPath.substring(0, strPath.substr(1).indexOf('/') + 1);
		var url = postPath + options.testUrl+csrftoken;
		$('#'+options.elementId).autocompleteCherry(url,{
			extraParams:{
				counterBAS: function() { return $('#'+options.elementId).val();},
				//默认是最多显示50条
				number:options.showNum ? options.showNum : 50,
				//默认是选中柜台主管名称
				selected:options.selected ? options.selected : "name",
				brandInfoId:function() { return $('#brandInfoId').val();}
			},
			loadingClass: "ac_loading",
			minChars:1,
		    matchContains:1,
		    matchContains: true,
		    scroll: false,
		    cacheLength: 0,
		    width:200,
		    max:options.showNum ? options.showNum : 50,
			formatItem: function(row, i, max) {
				if(typeof options.selected == "undefined" || options.selected=="name"){
					return escapeHTMLHandle(row[1])+" "+ (row[0] ? "【"+escapeHTMLHandle(row[0])+"】" : "");
				}else{
					return escapeHTMLHandle(row[1])+" "+ (row[0] ? "【"+escapeHTMLHandle(row[0])+"】" : "");
				}
			}
		});
	}
	
};

var binolbscnt01 =  new BINOLBSCNT01();



$(document).ready(function() {
	// 柜台查询
	binolbscnt01.search();
	
	// 省份选择
	$("#province").click(function(){
		// 显示省份列表DIV
		bscom03_showRegin(this,"provinceTemp");
	});
	// 城市选择
	$("#city").click(function(){
		// 显示城市列表DIV
		bscom03_showRegin(this,"cityTemp");
	});
	//柜台主管选择
	binolbscnt01.counterBASBinding({
		elementId:"counterBAS",
		testUrl:"/basis/BINOLBSCNT01_getCounterBAS.action"+"?",
		showNum:20
	});
});


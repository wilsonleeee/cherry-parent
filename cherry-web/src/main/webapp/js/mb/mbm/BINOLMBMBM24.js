var BINOLMBMBM24 = function () {
};
BINOLMBMBM24.prototype = {
		"search" : function() {
			var url = $("#search_Url").attr("href");
			var params= $("#mainForm").serialize();
			if(params != null && params != "") {
				url = url + "?" + params;
			}
			// 显示结果一览
			$("#importInfo").removeClass("hide");
			// 表格设置
			var tableSetting = {
					 // 表格ID
					 tableId : '#importDataTable',
					 // 数据URL
					 url : url,
					 // 表格默认排序
					 aaSorting : [[ 1, "desc" ]],
					 // 表格列属性设置
					 aoColumns : [  { "sName": "number", "sWidth": "3%", "bSortable": false},
					                { "sName": "serialNo", "sWidth": "15%" },
					                { "sName": "importName", "sWidth": "15%" },
									{ "sName": "importTime", "sWidth": "20%" },
									{ "sName": "employeeName", "sWidth": "15%" },
									{ "sName": "importReason", "sWidth": "30%", "bSortable": false}],
					// 横向滚动条出现的临界宽度
					sScrollX : "100%",
					fnDrawCallback: function() {
						$("a.description").cluetip({
							splitTitle: '|',
						    width: '350',
						    height: 'auto',
						    cluetipClass: 'default',
						    cursor:'pointer',
						    arrows: false, 
						    dropShadow: false
						});
					}
			};
			// 调用获取表格函数
			getTable(tableSetting);
	    },
	    // 在导入名称的text框上绑定下拉框选项
		"importNameBinding":function(options){
			var csrftoken = $('#csrftoken').serialize();
			if(!csrftoken) {
				csrftoken = $('#csrftoken',window.opener.document).serialize();
			}
			var strPath = window.document.location.pathname;
			var postPath = strPath.substring(0, strPath.substr(1).indexOf('/') + 1);
			var url = postPath + options.testUrl+csrftoken;
			$('#'+options.elementId).autocompleteCherry(url,{
				extraParams:{
					importNameStr: function() { return $('#'+options.elementId).val();},
					//默认是最多显示50条
					number:options.showNum ? options.showNum : 50,
					//默认是选中柜台名称
					selected:options.selected ? options.selected : "name",
					brandInfoId:function() { return $('#brandInfoId').val();}
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
					if(typeof options.selected == "undefined" || options.selected=="name"){
						return escapeHTMLHandle(row[1])+" "+ (row[0] ? "【"+escapeHTMLHandle(row[0])+"】" : "");
					}else{
						return escapeHTMLHandle(row[1])+" "+ (row[0] ? "【"+escapeHTMLHandle(row[0])+"】" : "");
					}
				}
			});
		}
};
var BINOLMBMBM24 = new BINOLMBMBM24();
$(document).ready(function() {
	BINOLMBMBM24.search();
	cherryValidate({			
		formId: "mainForm",		
		rules: {
			startDate: {dateValid:true},	// 开始日期
			startDate: {dateValid:true}	// 结束日期
		}		
	});
	BINOLMBMBM24.importNameBinding({
		elementId:"importName",
		testUrl:"/mb/BINOLMBMBM24_getImportName.action"+"?",
		showNum:20
	});
});
//（父页面刷新）
function search () {
	BINOLMBMBM24.search();
}
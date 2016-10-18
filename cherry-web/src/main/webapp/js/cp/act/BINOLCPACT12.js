
function BINOLCPACT12() {
	
};

BINOLCPACT12.prototype = {
		"search" : function() {
			var $form = $('#mainForm');
			var url = $("#searchUrl").attr("href");
			// 表单验证
			if(!$form.valid()){
				return;
			}
			$("#mainForm").find(":input").each(function() {
				$(this).val($.trim(this.value));
			});
			var param = BINOLCPACT12.getSearchParams();
			url += "?" + param;
			// 显示结果一览
			$("#section").show();
			 // 表格设置
			var tableSetting = {
					 index : 0,
					 // 表格ID
					 tableId : '#dataTable',
					 // 数据URL
					 url : url,
					 // 表格默认排序
					 aaSorting : [[ 1, "asc" ]],
					 // 表格列属性设置
					 aoColumns : [  { "sName": "no.","bSortable":false},
									{ "sName": "campaign"},
									{ "sName": "subcamp"},
									{ "sName": "counter"},
									{ "sName": "totalQuantity","sClass": "alignRight"},
									{ "sName": "currentQuantity","sClass": "alignRight"},
									{ "sName": "safeQuantity","sClass": "alignRight"},
									{ "sName": "operation"}
								],       
									
					// 不可设置显示或隐藏的列	
					aiExclude :[0,1,2,3],
					// 横向滚动条出现的临界宽度
					sScrollX : "100%"
			 };
			 // 调用获取表格函数
			 getTable(tableSetting);
		},
		
		/**
	     * 查询参数序列化
	     * @returns
	     */
	    "getSearchParams" : function() {
	    	// 查询参数序列化
	    	var params= $("#mainForm").find("div.column").find(":input").serialize();
	    	params += (params ? "&":"") + "csrftoken=" +$("#csrftoken").val();
	    	params = params + "&" + getRangeParams();
	    	return params;
	    },
		
		// 在活动名称的text框上绑定下拉框选项
		"campNameBinding":function(options){
			var csrftoken = $('#csrftoken').serialize();
			if(!csrftoken) {
				csrftoken = $('#csrftoken',window.opener.document).serialize();
			}
			var strPath = window.document.location.pathname;
			var postPath = strPath.substring(0, strPath.substr(1).indexOf('/') + 1);
			var url = postPath + options.testUrl+csrftoken;
			$('#'+options.elementId).autocompleteCherry(url,{
				extraParams:{
					campInfoStr: function() { return $('#'+options.elementId).val();},
					subCampInfoStr: function() { return $('#'+options.elementId).val();},
					campaignCode:function() {return options.campaignCodeKey ? $('#'+options.campaignCodeKey).val() : "";},
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
						return escapeHTMLHandle(row[0])+" "+ (row[1] ? "【"+escapeHTMLHandle(row[1])+"】" : "");
					}else{
						return escapeHTMLHandle(row[0])+" "+ (row[1] ? "【"+escapeHTMLHandle(row[1])+"】" : "");
					}
				}
			});
		}
};

var BINOLCPACT12 =  new BINOLCPACT12();
//初始化
$(document).ready(function() {
	BINOLCPACT12.search();
	// 产品选择绑定
	productBinding({elementId:"nameTotal",showNum:20});
	// 绑定活动TEXT框
	BINOLCPACT12.campNameBinding({
		elementId:"campaignCode",
		testUrl:"/cp/BINOLCPACT12_getCampName.action"+"?",
		showNum:20
	});
	BINOLCPACT12.campNameBinding({
		elementId:"subCampCode",
		testUrl:"/cp/BINOLCPACT12_getSubCampName.action"+"?",
		showNum:20,
		campaignCodeKey:"campaignCode"
	});
});

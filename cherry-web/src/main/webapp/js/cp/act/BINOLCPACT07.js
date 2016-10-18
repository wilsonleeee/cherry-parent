
function BINOLCPACT07() {
	
};

BINOLCPACT07.prototype = {
		"search" : function() {
			var url = $("#searchUrl").html();
			var $form = $("#mainForm");
			// 表单验证
			if(!$form.valid()){
				return;
			}	
			var token = getSerializeToken();
			var params= $form.serialize();
			params += "&" + token;
			url = url + "?" + params;
			// 显示结果一览
			$("#section").show();
			 // 表格设置
			var tableSetting = {
					 // 表格ID
					 tableId : '#dataTable',
					 // 数据URL
					 url : url,
					 // 表格默认排序
					 aaSorting : [[ 2, "asc" ]],
					 // 表格列属性设置
					 aoColumns : [  { "sName": "no.","bSortable":false},//1
					                { "sName": "billNoIF"},//2
									{ "sName": "memberName"},//3
									{ "sName": "memberPhone","bVisible": false},
									{ "sName": "testType","bVisible": false},
									{ "sName": "getCounter"},//4
									{ "sName": "couponCode"},//5
									{ "sName": "getTime"},//6
									{ "sName": "getQuantity","sClass":"alignRight"},//7
									{ "sName": "getAmount","sClass":"alignRight"},//8
									{ "sName": "giftDrawType","bVisible": false},//9
									{ "sName": "activity","bVisible": false},//10
									{ "sName": "employee"},//11
									{ "sName": "comments","bVisible": false}//12
								],       
									
					// 不可设置显示或隐藏的列	
					aiExclude :[0,1,2],
					// 横向滚动条出现的临界宽度
					sScrollX : "100%",
					// 固定列数
					fixedColumns : 3
			 };
			 // 调用获取表格函数
			 getTable(tableSetting);
		},
		// 在活动名称的text框上绑定下拉框选项
		"activityBinding":function(options){
			var csrftoken = $('#csrftoken').serialize();
			if(!csrftoken) {
				csrftoken = $('#csrftoken',window.opener.document).serialize();
			}
			var strPath = window.document.location.pathname;
			var postPath = strPath.substring(0, strPath.substr(1).indexOf('/') + 1);
			var url = postPath + options.testUrl+csrftoken;
			var $input = $('#'+options.elementId);
			var $next = $input.next();
			$('#'+options.elementId).autocompleteCherry(url,{
				extraParams:{
					activityCode: function() { return $input.val();},
					//默认是最多显示50条
					number:options.showNum ? options.showNum : 50,
					//默认是选中活动名称
					selected:options.selected ? options.selected : "code",
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
					if(typeof options.selected == "undefined" || options.selected=="code"){
						return escapeHTMLHandle(row[0])+" "+"【"+escapeHTMLHandle(row[1])+"】";
					}else{
						return escapeHTMLHandle(row[1])+" "+"【"+escapeHTMLHandle(row[0])+"】";
					}
				}
			}).result(function(event, data, formatted){
				$next.val(data[1]);
			}).bind("change",function(){
				// 清除input框后hidden值
				$next.val("");
			}).bind("focusout",function(){
				if($next.val() == ""){
					$input.val("");
				}
			});
		},
		//打开领用单据详细
		"openDetail" : function(obj) {
			openWin(obj);
		},
		/**
		 * 根据ID查询礼品领用详细信息
		 */
		"getPrtDetail" : function(obj) {
			var $this = $(obj);
			var url = $this.attr("href");
			var dialogSetting = {
				dialogInit : "#popPrtTable",
				width : 830,
				height : 550,
				resizable : true,
				zIndex: 99999,  
				title : $("#dialogTitle").text(),
				confirm : $("#dialogClose").text(),
				confirmEvent : function() {
					removeDialog("#popPrtTable");
				}
			};
			openDialog(dialogSetting);
			var callback = function(msg) {
				$("#popPrtTable").html(msg);
			};
			cherryAjaxRequest( {
				url : url,
				callback : callback
			});
		},
		/**
		 * 导出查询结果
		 */
		"exportExcel" : function() {
			var $form = $("#mainForm");
			//无数据不导出
			if($(".dataTables_empty:visible").length == 0){
			    if (!$form.valid()) {
			        return false;
			    };
			    var url = $("#exportUrl").html();
				var token = getSerializeToken();
				var params= $form.serialize();
				params += "&" + token;
				url = url + "?" + params;
			    window.open(url,"_self");
			}
		},
		/**
		 * 领用结果Excel导出
		 */
		"giftDrawExport":function(url) {
			//无数据不导出
	        if($(".dataTables_empty:visible").length==0){
	    	    if (!$('#mainForm').valid()) {
	                return false;
	            }
	    	    url += "?" + getSerializeToken();
	    		url += "&" + $("#mainForm").serialize();
	    		document.location.href = url;
	        }
		}
};

var BINOLCPACT07 =  new BINOLCPACT07();
//初始化
$(document).ready(function() {
	// 日期验证
	cherryValidate({
		formId : 'mainForm',
		rules : {
			startDate : {
				dateValid : true
			},
			endDate : {
				dateValid : true
			}
		}
	});
	// 查询
//	BINOLCPACT07.search();
	// 主题活动选择
	BINOLCPACT07.activityBinding({
		elementId : "activityName",
		testUrl : "/cp/BINOLCPACT07_getActivity.action" + "?",
		showNum : 20,
		selected:"name"
	});
	// 柜台选择
	counterBinding({
		elementId : "counterCode",
		showNum : 20,
		selected : "code"
	});
});

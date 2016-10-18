
function BINOLCPACT10() {
	
};

BINOLCPACT10.prototype = {
		"search" : function(url) {
			var $form = $('#mainForm');
			// 表单验证
			if(!$form.valid()){
				return;
			}
			var $text = $form.find(':text');
			$text.each(function(){
				$(this).val($.trim($(this).val()));
			});
			url += "?" + getSerializeToken();
			url += "&" + $form.serialize();
			// 显示结果一览
			$("#section").show();
			 // 表格设置
			var tableSetting = {
					 // 表格ID
					 tableId : '#dataTable',
					 // 数据URL
					 url : url,
					 // 表格默认排序
					 aaSorting : [[ 1, "asc" ]],
					 // 表格列属性设置
					 aoColumns : [  { "sName": "no.","bSortable":false},//1
					                { "sName": "billCode"},//2
									{ "sName": "memNameCode"},//3
									{ "sName": "mobilePhone"},
									{ "sName": "testType"},//3
									{ "sName": "departName"},//4
									{ "sName": "quantity","sClass":"alignRight"},//7
									{ "sName": "amount","sClass":"alignRight"},//8
									{ "sName": "saleTime"}
								],       
									
					// 不可设置显示或隐藏的列	
					aiExclude :[0,1],
					// 横向滚动条出现的临界宽度
					sScrollX : "100%"
			 };
			 // 调用获取表格函数
			 getTable(tableSetting);
		},
		/**
		 * 根据ID查询兑换礼品信息
		 */
		"getPrtDetail" : function(obj) {
			var $this = $(obj);
			var url = $this.attr("href");
			var dialogSetting = {
				dialogInit : "#popPrtTable",
				width : 860,
				height : 500,
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
		 * 兑换活动Excel导出
		 */
	       "exportExcel":function (url){
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

var BINOLCPACT10 =  new BINOLCPACT10();
//初始化
$(document).ready(function() {
	counterBinding({
		elementId:"departCode",
		showNum:20,
		selected :"code"
	});
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
});

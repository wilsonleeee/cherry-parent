function BINOLPTRPS31_1_global() {	
	this.needUnlock = true;
};

BINOLPTRPS31_1_global.prototype = {
		"search" : function() {
			$("#actionResultDisplay").empty();
			var $form = $('#memberBuyForm');
			if (!$form.valid()) {
				return false;
			};
			$form.find(":input").each(function() {
				$(this).val($.trim(this.value));
			});
			var url = $("#searchUrl").attr("href");
			var params = $form.serialize();
			if(params != null && params != "") {
				url = url + "?" + params;
			}
			url = url + "&csrftoken="+parentTokenVal();
			
			$("#memberBuyInfo").show();
			// 表格设置
			tableSetting = {
				 // 表格ID
				 tableId : '#memberBuyDataTable',
				 // 数据URL
				 url : url,
				 // 排序列
				 aaSorting : [[5, "desc"]],
				 // 表格列属性设置
				 aoColumns : [	
		                    	{ "sName": "no", "sWidth": "1%","bSortable": false},
		                    	{ "sName": "billCode"},
		                    	{ "sName": "baName"},
		                    	{ "sName": "memberName"},
		                    	{ "sName": "mobilePhone"},
		                    	{ "sName": "memberBuyDate"},
		                    	{ "sName": "unitCode"},
		                    	{ "sName": "barCode"},
		                    	{ "sName": "productName"},
		                    	{ "sName": "memberBuyQuantity","sClass":"alignRight"},
		                    	{ "sName": "memberBuyAmount","sClass":"alignRight"}
							],
				 sScrollX : "100%",
				// 不可设置显示或隐藏的列	
				aiExclude :[0,1,2,3]
			};
			
			// 调用获取表格函数
			getTable(tableSetting);
		},
		/**
	     * 导出数据
	     */
		"exportExcel" : function(url,exportFormat){
			$("#actionResultDisplay").empty();
			var that = this;
			var $form = $('#memberBuyForm');
			if (!$form.valid()) {
				return false;
			};
			$form.find(":input").each(function() {
				$(this).val($.trim(this.value));
			});
			//无数据不导出
			if($(".dataTables_empty:visible").length==0){
				var params= $form.serialize();
				params = params + "&exportFormat=" + exportFormat + "&csrftoken="
					+ parentTokenVal();
				
//	            if(exportFormat == "0"){
            	var callback = function(msg) {
        		    url = url + "?" + params;
        		    //锁住父页面
    	            that.needUnlock=false;
    	            document.location.href = url;
    	            //开启父页面
    	            that.needUnlock=true;
        	    }
            	
        	    exportExcelReport({
            		url: $("#exporCheckUrl").attr("href"),
            		param: params,
            		callback: callback
            	});
//	            }else {
//					exportReport({
//						exportUrl : url,
//						exportParam : params
//					});
//	            }
			}
		}
};

var BINOLPTRPS31_1 = new BINOLPTRPS31_1_global();

$(function(){
	if (window.opener) {
		window.opener.lockParentWindow();
	}
});
window.onbeforeunload = function(){
	if (BINOLPTRPS31_1.needUnlock ) {
		if (window.opener) {
			window.opener.unlockParentWindow();
		}
	}
};
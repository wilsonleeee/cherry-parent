var BINOLSSPRM67 = function () {
	this.needUnlock = true;
};

BINOLSSPRM67.prototype={
		"search" : function() {
			if(!$("#mainForm").valid()){
				return;
			}
			$("#mainForm").find(":input").each(function(){
				$(this).val($.trim(this.value));
			});
			var url = $("#search_Url").attr("href");
			var params= $("#mainForm").serialize();
			if(params != null && params != "") {
				url = url + "?" + params;
			}
			url = url + "&csrftoken="+parentTokenVal();
			// 显示结果一览
			$("#prmInDepotExcelInfo").removeClass("hide");
			// 表格设置
			var tableSetting = {
					 // 表格ID
					 tableId : '#prmInDepotExcelInfoDataTable',
					 // 数据URL
					 url : url,
					 // 表格默认排序
					 aaSorting : [[ 1, "desc" ]],
					 // 表格列属性设置
					 aoColumns : [  { "sName": "number", "sWidth": "5%", "bSortable": false},
					                { "sName": "billNo", "sWidth": "15%" },
					                { "sName": "departName", "sWidth": "20%" },
					                { "sName": "inventoryName", "sWidth": "20%", "bVisible": false },
					                { "sName": "logicInventoryName", "sWidth": "20%", "bVisible": false },
					                { "sName": "totalQuantity", "sWidth": "5%" },
									{ "sName": "totalAmount", "sWidth": "5%" },
									{ "sName": "inDepotDate", "sWidth": "5%" },
									{ "sName": "importResult", "sWidth": "5%","sClass":"center" },
									{ "sName": "tradeStatus", "sWidth": "5%","sClass":"center" }],
					// 横向滚动条出现的临界宽度
					sScrollX : "100%",
					aiExclude :[0, 1,2]
			};
			// 调用获取表格函数
			getTable(tableSetting);
	    },
	    "popDetail":function(url){
	    	if($("#popDetail").length == 0) {
	    		$("body").append('<div style="display:none" id="popDetail"></div>');
	    	} else {
	    		$("#popDetail").empty();
	    	}
	    	var initCallback = function(msg){
		    	var dialogSetting = {
		    			dialogInit: "#popDetail",
		    			text: msg,
		    			width:800,
		    			minWidth:600,
		    			height:450,
		    			minHeight:450,
		    			maxHeight:450,
		    			resizable: true,
		    			title: 	$("#dialogTitle").html(),
		    			confirm: $("#dialogConfirm").html(),
		    			confirmEvent: function(){
		    				removeDialog("#popDetail");
		    			}
		    	};
		    	openDialog(dialogSetting);
		    	$("a.description").cluetip({
					splitTitle: '|',
				    width: '300',
				    height: 'auto',
				    cluetipClass: 'default',
				    cursor:'pointer',
				    arrows: false, 
				    dropShadow: false
				});
		    	
	    	}
	    	cherryAjaxRequest({
	    		url: url,
	    		param: null,
	    		callback: initCallback
	    	});
	    },
		"exportExcel" : function(url){
			//无数据不导出
			if($(".dataTables_empty:visible").length==0){
				var that = this;
	            var params= $("#mainForm").serialize();
	            params = params + "&csrftoken=" +parentTokenVal();
	            url = url + "?" +params;
	            //锁住父页面
	            that.needUnlock=false;
	            document.location.href = url;
	            //开启父页面
	            that.needUnlock=true;
			}
    }
};



var BINOLSSPRM67 = new BINOLSSPRM67();

$(document).ready(function() {
	if (window.opener) {
		window.opener.lockParentWindow();
	}
	cherryValidate({			
		formId: "mainForm",		
		rules: {
			inDepotStartTime: {dateValid:true},	// 开始日期
			inDepotEndTime: {dateValid:true}	// 结束日期
		}		
	});
});
window.onbeforeunload = function(){
	if (BINOLSSPRM67.needUnlock ) {
		if (window.opener) {
			window.opener.unlockParentWindow();
		}
	}
};
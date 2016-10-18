function BINOLSTIOS08(){
	this.needUnlock = true;
};

BINOLSTIOS08.prototype={
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
			$("#productInDepotExcelInfo").removeClass("hide");
			// 表格设置
			var tableSetting = {
					 // 表格ID
					 tableId : '#productInDepotExcelInfoDataTable',
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
	    "popu" : function(url){
	    	$('#detailData').html($('#processing').html());
	    	$that = this;
            var params= $("#mainForm").serialize();
            cherryAjaxRequest({
				url:url,
				param:params,
				formId:'mainForm',
				callback:function(msg){
					$('#detailData').html(msg);
					$that.getCluetip();
				}
			});
            $that.getDialog();
	    },
	    "getDialog" : function(){
	    	var dialogSetting = {
	    			bgiframe: true,
	    			width:800,
	    			minWidth:600,
	    			height:450,
	    			minHeight:450,
	    			maxHeight:450,
	    			zIndex: 1,
	    			modal: true, 
	    			resizable: true,
	    			title:$('#popuTitle').html(),
	    			buttons: [
	  						{
	  							text: $('#popuOK').text(),
	  						    click: function(){ $('#detailData').dialog( "destroy" );}
	  						}],
	    			close: function() { $('#detailData').dialog( "destroy" ); }
    		};
    		$('#detailData').dialog(dialogSetting);
	    },
	    "getCluetip" : function(){
	    	$("a.description").cluetip({
				splitTitle: '|',
			    width: '300',
			    height: 'auto',
			    cluetipClass: 'default',
			    cursor:'pointer',
			    arrows: false, 
			    dropShadow: false}
	    	);
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

var BINOLSTIOS08 = new BINOLSTIOS08();
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
	if (BINOLSTIOS08.needUnlock ) {
		if (window.opener) {
			window.opener.unlockParentWindow();
		}
	}
};

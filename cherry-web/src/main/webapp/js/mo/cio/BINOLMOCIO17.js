function BINOLMOCIO17(){
	this.needUnlock = true;
};

BINOLMOCIO17.prototype={
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
			$("#cntMsgImportInfo").removeClass("hide");
			// 表格设置
			var tableSetting = {
					 // 表格ID
					 tableId : '#cntMsgImportInfoDataTable',
					 // 数据URL
					 url : url,
					 // 表格默认排序
					 aaSorting : [[ 1, "desc" ]],
					 // 表格列属性设置
					 aoColumns : [  { "sName": "number", "bSortable": false},
					                { "sName": "messageTitle" },
					                { "sName": "messageBody" },
					                { "sName": "startValidDate" },
					                { "sName": "endValidDate" },
									{ "sName": "importDate", "bVisible": false },
									{ "sName": "importResult", "sClass":"center" },
									{ "sName": "publishStatus", "sClass":"center" }],
					// 横向滚动条出现的临界宽度
					sScrollX : "100%",
					aiExclude :[0, 1, 2]
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
		"exportExcel" : function(){
			var url = $("#exportURL").attr("href");
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

var BINOLMOCIO17 = new BINOLMOCIO17();
$(document).ready(function() {
	if (window.opener) {
		window.opener.lockParentWindow();
	}
	
	// 表单验证配置
    cherryValidate({
        formId: 'mainForm',
        rules: {
        	startValidDateBegin: {dateValid:true},    // 开始生效日期的开始日期
        	startValidDateFinish: {dateValid:true},  // 开始生效日期的结束日期
        	endValidDateBegin: {dateValid:true},    // 结束生效日期的开始日期
        	endValidDateFinish: {dateValid:true}   // 结束生效日期的结束日期
    	}
    });
	
	BINOLMOCIO17.search();
});
window.onbeforeunload = function(){
	if (BINOLMOCIO17.needUnlock ) {
		if (window.opener) {
			window.opener.unlockParentWindow();
		}
	}
};

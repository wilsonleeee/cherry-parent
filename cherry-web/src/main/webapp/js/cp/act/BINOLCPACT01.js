
function BINOLCPACT01() {
	
};

BINOLCPACT01.prototype = {
		
	//用户查询
	"search" : function (){
		var $form = $('#mainForm');
		if (!$form.valid()) {
			return false;
		};
		$("#mainForm").find(":input").each(function() {
			$(this).val($.trim(this.value));
		});
		var mainCampType=$('#mainCampType').val();
		//默认为兑礼活动
		//BINOLCPACT01.campTypeChange();
		var url = $("#MainListUrl").attr("href");
		 // 查询参数序列化
		var params= $form.serialize();
			params = params + "&csrftoken=" +$("#csrftoken").val();
			url = url + "?" + params;
		var aoColumn = null;
		var aaSorting = null;
		var index = 0;
		if($('#searchMode_1').is(":checked")){
			index= 1;
			aaSorting = [[1, "desc"]];
			aoColumn= [	
			           	{ "sName": "no","bSortable": false}, 			// 0
			           	{ "sName": "campId"},							// 1
			           	{ "sName": "campaignCode"},						// 2						
	                  	{ "sName": "campType"},							// 3
	                  	{ "sName": "campState"}, 						// 4
	                  	{ "sName": "startDate"}, 						// 5
	                  	{ "sName": "endDate"}, 							// 6
	                  	{ "sName": "updateTime","bVisible": false},     // 7
	                  	{ "sName": "employeeName"},     					// 8
	                  	{ "sName": "validFlag","sClass":"center","bVisible": false},       // 9
	                  	{ "sName": "operator","bSortable": false}		// 10
		              ];
		}else{
			index= 2;
			aaSorting = [[2, "desc"]];
			aoColumn= [	
	           			{ "sName": "no","bSortable": false}, 			// 0
			           	{ "sName": "subCampName"},						// 1
			           	{ "sName": "subCampaignCode"},					// 2
			           	{ "sName": "campName"},  						// 3
						{ "sName": "campType","bVisible": false},		// 4
						{ "sName": "subcampType"},						// 5
						{ "sName": "subCampState"},  					// 6
						{ "sName": "obtainStartDate"}, 				    // 7
				    	{ "sName": "obtainEndDate"}, 				    // 8
				    	{ "sName": "validFlag","sClass":"center","bVisible": false},       // 9
						{ "sName": "operator","bSortable": false}		// 10
				      ];
		}
		var tableSetting = {
					// datatable 对象索引
					index : index,
					// 表格ID
					tableId : '#dataTable_'+index,
					// 数据URL
					url : url,
					// 排序列
					aaSorting : aaSorting,
					// 表格列属性设置			 
					aoColumns :aoColumn,
                    // 不可设置显示或隐藏的列	
          			aiExclude :[0,1],
          			// 横向滚动条出现的临界宽度
        			sScrollX : "100%",
        			// 固定列数
        			fixedColumns : 2
			 };
		//DataTable结果显示
		$("#section").show();
		// 调用获取表格函数
		getTable(tableSetting);
	},
	//活动查询切换
	"changeMode":function(url,_this){
		oTableArr[$(_this).val()]= null;
		cherryAjaxRequest({
			url: url,
			param: $(_this).serialize(),
			callback: function(msg) {
				$("#div_main").html(msg);
			}
		});
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
	},
	//活动类型切换
	"campTypeChange":function(){
		var $this = $('#mainCampType');
		var $subcampType = $(':input[name="subcampType"]');
		$subcampType.hide();
		$subcampType.prop("disabled", true);
		var $select = $('#subcampType_' + $this.val());
		$select.show();
		if($this.val() == ''){
			$select.prop("disabled", true);
		}else{
			$select.prop("disabled", false);
		}
	},
	/**
	 * 停用会员活动弹出框
	 * @return
	 */
	"stopCampDialog":function (campaignId,subCampaignId,validFlag){
		if(validFlag == '1'){
			BINOLCPACT01.stopCampaign(campaignId,subCampaignId,validFlag);
		}else{
			var dialogId = "stop_act_dialog";
			var $dialog = $('#' + dialogId);
			$dialog.dialog({ 
				//默认不打开弹出框
				autoOpen: false,  
				//弹出框宽度
				width: 350, 
				//弹出框高度
				height: 250, 
				//弹出框标题
				title:$("#dialogTitle").text(), 
				//弹出框索引
				zIndex: 1,  
				modal: true, 
				resizable:false,
				//弹出框按钮
				buttons: [{
					text:$("#dialogConfirm").text(),//确认按钮
					click: function() {
						//点击确认后执行活动停用
						BINOLCPACT01.stopCampaign(campaignId,subCampaignId,0);
						$dialog.dialog("close");
					}
				},
				{	
					text:$("#dialogCancel").text(),//取消按钮
					click: function() {
						$dialog.dialog("close");
					}
				}],
				//关闭按钮
				close: function() {
					closeCherryDialog(dialogId);
				}
			});
			$dialog.dialog("open");
		}
	},
	/**
	 * 停用会员活动
	 */
	"stopCampaign":function (campaignId,subCampaignId,validFlag){
		if(isEmpty(subCampaignId)){//主题活动停用
			var url = $('#stopCampaignUrl').html();
			var param = "campaignId="+campaignId;
		}else{//活动停用
			var url = $('#stopSubCampaignUrl').html();
			var param = "subCampaignId="+subCampaignId;
		}
		param += "&validFlag=" + validFlag;
		cherryAjaxRequest({
			url:url,
			param:param,
			callback: function(data) {
				$("#stop_act_dialog").dialog("close");
				//刷新表格数据
				BINOLCPACT01.search();
			}
		});
	}
};

var BINOLCPACT01 =  new BINOLCPACT01();
//初始化
$(document).ready(function() {
	BINOLCPACT01.search();
	BINOLCPACT01.campNameBinding({
		elementId:"campCode",
		testUrl:"/cp/BINOLCPACT01_getCampName.action"+"?",
		showNum:20
	});
	BINOLCPACT01.campNameBinding({
		elementId:"subcampCode",
		testUrl:"/cp/BINOLCPACT01_getSubCampName.action"+"?",
		showNum:20
	});
});
//查询（父页面刷新）
function search () {
	BINOLCPACT01.search();
}

function BINOLSTSFH13(){
	this.needUnlock = true;
};

BINOLSTSFH13.prototype={
		
		/**
		 * 发、收货部门弹出框
		 */
		"popDepartBox" : function(thisObj,flag) {
			//取得所有部门类型
		 	var param = "checkType=radio&privilegeFlg=1&businessType=1";
			var callback = function() {
				var $selected = $('#departDataTable').find(':input[checked]').parents("tr");
				if($selected.length > 0) {
					var departId = $selected.find("input@[name='organizationId']").val();
					var departCode = $selected.find("td:eq(1)").text();
					var departName = $selected.find("td:eq(2)").text();
					var departNameReceive = "("+departCode+")"+departName;
					var html = '<tr><td><span class="list_normal">';
					html += '<span class="text" style="line-height:19px;">' + departNameReceive + '</span>';
					html += '<span class="close" style="margin: -1px 0 0 6px;" onclick="BINOLSTSFH13.delPrmLabel(this);return false;"><span class="ui-icon ui-icon-close"></span></span>';
		 			if(flag=='out'){
		 				html += '<input type="hidden" name="organizationId" value="' + departId + '"/>';
		 				html += '</span></td></tr>';
			 			$("#organization_ID").html("");
			 			$("#organization_ID").append(html);
		 			} else if(flag == 'in') {
		 				html += '<input type="hidden" name="organizationIdReceive" value="' + departId + '"/>';
		 				html += '</span></td></tr>';
			 			$("#inOrganization_ID").html("");
			 			$("#inOrganization_ID").append(html);
		 			}
		 			
				}else{
					if(flag=='out'){
						$("#organization_ID").val("");
					} else if(flag == 'in') {
						$("#inOrganization_ID").val("");
					}
				}
			};
			popDataTableOfDepart(thisObj,param,callback);
		},
		
		/**
		 * 删除显示标签
		 * @param obj
		 * @return
		 */
		"delPrmLabel":function(obj){
			$(obj).parent().parent().parent().remove();
		},
		
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
			$("#prtDeliverExcelInfo").removeClass("hide");
			// 表格设置
			var tableSetting = {
					 // 表格ID
					 tableId : '#prtDeliverExcelInfoDataTable',
					 // 数据URL
					 url : url,
					 // 表格默认排序
					 aaSorting : [[ 1, "desc" ]],
					 // 表格列属性设置
					 aoColumns : [  { "sName": "number", "bSortable": false},
					                { "sName": "billNo"},
					                { "sName": "relevanceNo", "bVisible": false},
					                { "sName": "departNameFrom" },
					                { "sName": "inventoryName", "bVisible": false },
					                { "sName": "logicInventoryName", "bVisible": false },
					                { "sName": "departNameTo" },
					                { "sName": "totalQuantity" },
									{ "sName": "totalAmount" },
									{ "sName": "importDate" },
									{ "sName": "importResult", "sClass":"center" },
									{ "sName": "tradeStatus", "sClass":"center" }],
					// 横向滚动条出现的临界宽度
					sScrollX : "100%",
					aiExclude :[0, 1]
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

var BINOLSTSFH13 = new BINOLSTSFH13();
$(document).ready(function() {
	if (window.opener) {
		window.opener.lockParentWindow();
	}
	cherryValidate({			
		formId: "mainForm",		
		rules: {
			importDateStart: {dateValid:true},	// 开始日期
			importDateEnd: {dateValid:true}	// 结束日期
		}		
	});
});
window.onbeforeunload = function(){
	if (BINOLSTSFH13.needUnlock ) {
		if (window.opener) {
			window.opener.unlockParentWindow();
		}
	}
};

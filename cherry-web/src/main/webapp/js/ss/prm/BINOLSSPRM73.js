function BINOLSSPRM73() {
	
};

BINOLSSPRM73.prototype = {
		
		"searchPointInfo" : function() {
			if(!$('#mainForm').valid()) {
				return false;
			}
			var url = $("#couponRuleUrl").attr("href");
			var params= $("#mainForm").serialize();
			if(params != null && params != "") {
				url = url + "?" + params;
			}
			// 显示结果一览
			$("#couponRule").removeClass("hide");
			// 表格设置
			var tableSetting = {
					 // 表格ID
					 tableId : '#couponRuleDataTable',
					 // 数据URL
					 url : url,
					 // 表格默认排序
					 aaSorting : [[ 3, "desc" ]],
					 // 表格列属性设置
					 aoColumns : [  { "sName": "number", "sWidth": "3%", "bSortable": false },
					                { "sName": "ruleName", "sWidth": "20%" },
									{ "sName": "ruleCode", "sWidth": "15%" },
									{ "sName": "sendStartTime", "sWidth": "10%" },
									{ "sName": "sendEndTime", "sWidth": "10%" },
									{ "sName": "status", "sWidth": "10%" },
									{ "sName": "validFlag", "sWidth": "10%" },
									{ "sName": "operate", "sWidth": "5%", "bSortable": false }],
					// 横向滚动条出现的临界宽度
					sScrollX : "100%"
			};
			// 调用获取表格函数
			getTable(tableSetting);
	    },
	    /**
		 * 弹出新增菜单分组对话框
		 */
		"popBatchDialog" : function(obj) {
			var param = $(obj).parent().parent().find("input[name='ruleCode']").serialize();
			var dialogSetting = {
					dialogInit: "#couponDialogInit",
					width: 	650,
					height: 370,
					title: 	"批量生成优惠券",
					confirm: null,
					cancel: null
				};
				openDialog(dialogSetting);
				var initUrl = $("#batchInitUrl").attr("href");
				var callback = function(msg) {
					$("#couponDialogInit").html(msg);
					$("#confirmBtn").bind("click", function(){// 批量生成优惠券
						binolssprm73.couponBatch($("#couponBatchUrl").attr("href"),param + "&" + $("#batchForm").serialize());
			    	});
				};
				cherryAjaxRequest({
					url: initUrl,
					param: param,
					callback: callback
				});
		},
	    "couponBatch" : function(url, param) {
	    	if(!$('#batchForm').valid()) {
				return false;
			}
	    	// 禁用确定按钮
	    	$("#confirmBtn").attr("disabled", "disabled");
	    	$("#loading").show();
	    	cherryAjaxRequest({
	     		url: url,
	     		param: param,
	     		isDialog: true,
	     		resultId: "#saveActionResult",
	    		bodyId: "#couponDialogInit",
	    		callback: function() {
	    			// 激活确定按钮
	    			$("#confirmBtn").removeAttr("disabled");
	    			$("#loading").hide();
	    		}
	     	});
	    },
	    "checkCouponRule" : function(url) {
	    	var $p = $("#check_coupon_dialog").find('p.message');
	    	var $message = $p.find('span');
	    	var $loading = $p.find('img');
	    	$loading.hide();
	    	var $message2 = $('#errorMessage');
	    	var option = { 
	    			autoOpen: false,  
	    			width: 350, 
	    			height: 250, 
	    			title:"提示", 
	    			zIndex: 1,  
	    			modal: true,
	    			resizable:false,
	    			buttons: [{
	    				text:"确定",
	    				click: function(_this) {
	    					$message2.html('');
	    					$loading.show();
	    					cherryAjaxRequest({
	    						url:url,
	    						callback:function(data){
	    							var json=JSON.parse(data);
	    							var html = '';
	    							var msg = json.ERRORMSG;
	    							if(msg == undefined || msg == null || msg == ''){
	    								msg = $('#checkMsg_' + json.ERRORCODE).val();
	    							}
	    							if(json.ERRORCODE != '0'){
	    								html = showMsg(msg + '[' + json.ERRORCODE + ']',1);
	    							}else{
	    								html = showMsg(msg,json.ERRORCODE);	
	    							}
	    							$message2.html(html);
	    							oTableArr[0].fnDraw();
	    							closeCherryDialog("check_coupon_dialog");
	    						}
	    					});
	    				}
	    			},
	    			{	text:"取消",
	    				click: function() {
	    					closeCherryDialog("check_coupon_dialog");
	    				}
	    			}],
	    			close: function() {
	    				closeCherryDialog("check_coupon_dialog");
	    			}
	    		};
	    	$message.text("您确定要审核通过该活动吗？");
	    	$("#check_coupon_dialog").dialog(option);
	    	$("#check_coupon_dialog").dialog("open");
	    },
	    "couponUpload" : function (){
			var $parentDiv;
			var $couponUpExcel;
			var couponUpExcelId;
			var upMode;
			var $couponPathExcel;
			
			$parentDiv = $("#sendCondition");
			$couponUpExcel = $("#couponUpExcel");
			couponUpExcelId = 'couponUpExcel';
			upMode = $parentDiv.find('input[name="upMode"]:checked').val();
			$couponPathExcel = $('#couponPathExcel');
			var batchMode =$("#batchMode_3").val();
			var ruleCode =$("#ruleCode_3").val();
			var url = $("#importCouponUrl").attr("href");
	    	// AJAX登陆图片
	    	var $ajaxLoading = $parentDiv.find("#couponloading");
	    	// 错误信息提示区
	    	var $errorMessage = $('#actionResultDisplay');
	    	// 清空错误信息
			$errorMessage.hide();
			$errorMessage.empty();
	    	if($couponUpExcel.val()==''){
	    		$couponPathExcel.val('');
				$("#errorDiv").show();
				$("#errorSpan").text("请选择文件");
	    		return false;
	    	}
	    	$ajaxLoading.ajaxStart(function(){$(this).show();});
	    	$.ajaxFileUpload({
		        url: url,
		        secureuri:false,
		        data:{'csrftoken':parentTokenVal(),
		        	'upMode': upMode,
		        	'batchMode':batchMode,
		        	'ruleCode':ruleCode,
		        	'conditionType':3},
		        fileElementId:couponUpExcelId,
		        dataType: 'html',
		        success: function (msg){
		        	$ajaxLoading.ajaxComplete(function(){$(this).hide();});
		        	$errorMessage.html(msg);
		        	if($errorMessage.find("#actionResultDiv").length > 0){
		        		$errorMessage.show();
		        	}
		        }
	        });
		},
		"changeBatchMode":function(obj){
			var batchMode=$(obj).val();
			if(batchMode == 0){
				$("#sendCondition tbody tr:eq(2)").hide();
				$("#batchCount").removeAttr("disabled");
			}else if(batchMode == 1){
				$("#sendCondition tbody tr:eq(2)").show();
				$("#batchCount").attr("disabled","disabled");
			}
		},
		"couponDialog" : function(conditionType) {
			if($("#memberDialogDiv").length == 0) {
				$("body").append('<div style="display:none" id="memberDialogDiv"></div>');
			} else {
				$("#memberDialogDiv").empty();
			}
			var searchInitCallback = function(msg) {
				$("#memberDialogDiv").html(msg);
				var dialogSetting = {
					dialogInit: "#memberDialogDiv",
					text: msg,
					width: 	600,
					height: 350,
					title: 	"会员一览",
					cancel: "关闭",
					cancelEvent: function(){removeDialog("#memberDialogDiv");}
				};
				openDialog(dialogSetting);
				
				if(oTableArr[191]) {
					oTableArr[191] = null;
				}
				var searchUrl = $("#memberDialogUrl").attr("href")+"?"+getSerializeToken()
					+ "&ruleCode=" + $('#ruleCode_3').val() + "&conditionType=" + conditionType;
				var tableSetting = {
						 // 表格ID
						 tableId : '#memberDialogDataTable',
						 // 一页显示页数
						 iDisplayLength:5,
						 // 数据URL
						 url : searchUrl,
						 // 表格默认排序
						 aaSorting : [[ 1, "asc" ]],
						 // 表格列属性设置
						 aoColumns : [  { "sName": "No", "sWidth": "5%", "bSortable": false}, 	
										{ "sName": "memCode", "sWidth": "45%"},
										{ "sName": "mobile", "sWidth": "50%"}],               
						index:191,
						colVisFlag:false
				 };
				// 调用获取表格函数
				getTable(tableSetting);
			};
			cherryAjaxRequest({
				url: $("#memberDialogInitUrl").attr("href"),
				param: null,
				callback: searchInitCallback
			});
		}
};

function reSearch() {
	oTableArr[0].fnDraw();
}
/* 
 * 关闭Dialog
 * 
 * 
 * 
 */
function dialogClose() {
	removeDialog("#couponDialogInit");
}

var binolssprm73 =  new BINOLSSPRM73();

$(function(){
	$('#sendStartTime').cherryDate({
		beforeShow: function(input){
			var value = $('#sendEndTime').val();
			return [value,'maxDate'];
		}
	});
	$('#sendEndTime').cherryDate({
		beforeShow: function(input){
			var value = $('#sendStartTime').val();
			return [value,'minDate'];
		}
	});
	// 表单验证初期化
	cherryValidate({
		formId: 'mainForm',
		rules: {
			sendStartTime: {dateValid: true},
			sendEndTime: {dateValid: true}
		}
	});
});
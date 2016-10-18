function BINOLSSPRM75() {
	
};

BINOLSSPRM75.prototype = {
		
		"search" : function() {
			if(!$('#mainForm').valid()) {
				return false;
			}
			var url = $("#couponUrl").attr("href");
			var params= $("#mainForm").serialize();
			if(params != null && params != "") {
				url = url + "?" + params;
			}
			
//			var searchStatus=$("#status").val();
			//如果设置查询的是已使用的
//			if(searchStatus=="OK"){
//				// 显示结果一览
//				$("#coupon").addClass("hide");
//				$("#couponUsed").removeClass("hide");
//				
//
//				// 表格设置
//				var tableSetting = {
//						 // 表格ID
//						 tableId : "#couponUsedDataTable",
//						 // 数据URL
//						 url : url,
//						 index: 2015,
//						 // 表格默认排序
//						 aaSorting : [[ 0, "desc" ]],
//						 // 表格列属性设置
//						 aoColumns : [  { "sName": "couponNo", "sWidth": "15%"},
//						                { "sName": "couponStatus", "sWidth": "5%","bSortable": false},
//										{ "sName": "couponType", "sWidth": "10%" },
//										{ "sName": "ruleCode", "sWidth": "5%" },
//										{ "sName": "couponCode", "sWidth": "15%"},
//										{ "sName": "memberCode", "sWidth": "15%" },
//										{ "sName": "memberName", "sWidth": "5%" },
//										{ "sName": "userBP", "sWidth": "5%" ,"bSortable": false},
//										{ "sName": "useTime", "sWidth": "5%" },
//										{ "sName": "relationBill", "sWidth": "5%" },
//										{ "sName": "billCode", "sWidth": "5%" },
//										{ "sName": "countName", "sWidth": "5%" },
//										{ "sName": "operator", "sWidth": "5%","bSortable": false}],
////						// 不可设置显示或隐藏的列	
////						aiExclude :[0,2,5,8],
//						// 横向滚动条出现的临界宽度
//						sScrollX : "100%"
//				};
//				// 调用获取表格函数
//				getTable(tableSetting);
//			}else{
				// 显示结果一览
				//$("#couponUsed").addClass("hide");
				$("#coupon").removeClass("hide");
				
				// 表格设置
				var tableSetting = {
						 // 表格ID
						 tableId : "#couponDataTable",
						 // 数据URL
						 url : url,
						 index: 2016,
						 // 表格默认排序
						 aaSorting : [[ 0, "desc" ]],
						 // 表格列属性设置
						 aoColumns : [  { "sName": "couponNo", "sWidth": "10%"},    //0
						                { "sName": "couponType", "sWidth": "5%" },  //1
										{ "sName": "ruleCode", "sWidth": "10%", "bVisible" : false},   //2
										{ "sName": "couponCode", "sWidth": "5%", "bVisible" : false},  //3
										{ "sName": "bpCode", "sWidth": "5%","bSortable": false},  //4
										{ "sName": "memberCode", "sWidth": "5%" },  //5
										{ "sName": "memberMobile", "sWidth": "5%" }, //6										
										{ "sName": "startTime", "sWidth": "10%", "bVisible" : false},  //7
										{ "sName": "endTime", "sWidth": "10%" },  //8
										{ "sName": "createTime", "sWidth": "15%" },  //9
										{ "sName": "relationBill", "sWidth": "5%","bSortable": false},  //10
										{ "sName": "departCode", "sWidth": "5%","bSortable": false},  //11
										{ "sName": "departName", "sWidth": "5%","bSortable": false},  //12
										{ "sName": "status", "sWidth": "5%","bSortable": false},		//13					
										{ "sName": "validFlag", "sWidth": "5%","bSortable": false,"sClass":"center"},  //14
										{ "sName": "operate", "sWidth": "20%","bSortable": false}],  //15
						// 不可设置显示或隐藏的列	
						aiExclude :[0,1,4],
						// 横向滚动条出现的临界宽度
						sScrollX : "100%"
				};
				// 调用获取表格函数
				getTable(tableSetting);
//			}
			
	    },		
};



/* 
 * 导出Excel
 */
 function exportExcel(){
	
	 var $p = $("#export_dialog").find('p.message');
	 var $message = $p.find('span');
	 
	 var check_url = $("#checkExportUrl").attr("href");
	 var check_params= $("#mainForm").serialize();
	 check_params = check_params + "&csrftoken=" +$("#csrftoken").val();
	 check_url = check_url + "?" + check_params;
	 
    cherryAjaxRequest({
 		url:check_url,
 		callback:callBack
 	});
    
    function callBack(data){
    	if(data == 'true'){
			//操作成功
    	    if($(".dataTables_empty:visible").length==0){
    		    if (!$('#mainForm').valid()) {
    	            return false;
    	        };
    	        var url = $("#downUrl").attr("href");
    	        var params= $("#mainForm").serialize();
    	        params = params + "&csrftoken=" +$("#csrftoken").val();
    	        url = url + "?" + params;
    	        window.open(url,"_self");
    	    }else{
    	    	//操作失败
    			var option = { 
    					autoOpen: false,  
    					width: 350, 
    					height: 250, 
    					title:"提示",
    					zIndex: 1,  
    					modal: true,
    					resizable:false,
    					buttons: [
    					{	text:"确定",
    						click: function() {
    							closeCherryDialog("export_dialog");
    						}
    					}],
    					close: function() {
    						closeCherryDialog("export_dialog");
    					}
    				};
    			$message.text("对不起，当前没有数据可以导出，请点击确定按钮退出此次操作");
    			$("#export_dialog").dialog(option);
    			$("#export_dialog").dialog("open");
    			
    			return false;
    	    }
		}else{
			//操作失败
			var option = { 
					autoOpen: false,  
					width: 350, 
					height: 250, 
					title:"提示",
					zIndex: 1,  
					modal: true,
					resizable:false,
					buttons: [
					{	text:"确定",
						click: function() {
							closeCherryDialog("export_dialog");
						}
					}],
					close: function() {
						closeCherryDialog("export_dialog");
					}
				};
			$message.text("对不起，数据量最大不能超过200,000条，请点击确定按钮退出此次操作");
			$("#export_dialog").dialog(option);
			$("#export_dialog").dialog("open");
			
			return false;
		}
    }
	 
}
 
/**
 * 停用促销活动
 * @return
 */
function stopCoupon(couponNo){
	var $p = $("#stop_coupon_dialog").find('p.message');
	var $message = $p.find('span');
	var $loading = $p.find('img');
	$loading.hide();
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
				click: function() {
					$loading.show();
					cancleCoupon(couponNo);
				}
			},
			{	text:"取消",
				click: function() {
					closeCherryDialog("stop_coupon_dialog");
				}
			}],
			close: function() {
				closeCherryDialog("stop_coupon_dialog");
			}
		};
	$message.text("您确定要取消此优惠券吗？");
	$("#stop_coupon_dialog").dialog(option);
	$("#stop_coupon_dialog").dialog("open");
}

function cancleCoupon(couponNo){
	
	var url = $("#stopUrl").attr("href");
	var param = "couponNo="+couponNo;
	cherryAjaxRequest({
		url:url,
		param:param,
		callback:callBack
	});
	
	function callBack(data){
		oTableArr[2016].fnDraw();
		closeCherryDialog("stop_coupon_dialog");	
	}
}

/**
 * 短信重发
 */
function sendMsg(couponNo,memberMobile){
	var $p = $("#send_msg_dialog").find('p.message');
	var $message = $p.find('span');
	var $loading = $p.find('img');
	$loading.hide();
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
				click: function() {
					$loading.show();
					activeSendMsg(couponNo,memberMobile);
				}
			},
			{	text:"取消",
				click: function() {
					closeCherryDialog("send_msg_dialog");
				}
			}],
			close: function() {
				closeCherryDialog("send_msg_dialog");
			}
		};
	$message.text("您确定要重发信息吗？");
	$("#send_msg_dialog").dialog(option);
	$("#send_msg_dialog").dialog("open"); 
	
}

function activeSendMsg(couponNo,memberMobile){
	
	var url = $("#sendUrl").attr("href");
	
	var param = "couponNo="+couponNo+"&mobile="+memberMobile;
	
	cherryAjaxRequest({
		url:url,
		param:param,
		callback:callBack
	});
	
	function callBack(data){	
		closeCherryDialog("send_msg_dialog");
		
		$r = $("#send_result_dialog").find('p.message');
		var $message = $r.find('span');
		var option = { 
				autoOpen: false,  
				width: 250, 
				height: 150, 
				title:"提示",
				zIndex: 2,  
				modal: true,
				resizable:false,
				close: function() {
					closeCherryDialog('send_result_dialog');
				}
			};
		
		if(data == 'true'){
			//操作成功
			$message.text("信息发送成功");
		}else{
			//操作失败
			$message.text("信息发送失败");
		}
		$("#send_result_dialog").dialog(option);
		$("#send_result_dialog").dialog("open"); 
	}
}
var binolssprm75 =  new BINOLSSPRM75();

$(function(){
	$('#startTime').cherryDate({
		beforeShow: function(input){
			var value = $('#endTime').val();
			return [value,'maxDate'];
		}
	});
	$('#endTime').cherryDate({
		beforeShow: function(input){
			var value = $('#startTime').val();
			return [value,'minDate'];
		}
	});
	// 表单验证初期化
	cherryValidate({
		formId: 'mainForm',
		rules: {
			startTime: {dateValid: true},
			endTime: {dateValid: true}
		}
	});
});
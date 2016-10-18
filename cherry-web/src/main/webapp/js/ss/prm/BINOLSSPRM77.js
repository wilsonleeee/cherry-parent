function BINOLSSPRM77() {
	
};

BINOLSSPRM77.prototype = {
		
		"search" : function() {
			if(!$('#mainForm').valid()) {
				return false;
			}
			var url = $("#couponUrl").attr("href");
			var params= $("#mainForm").serialize();
			if(params != null && params != "") {
				url = url + "?" + params;
			}
			

			$("#coupon").removeClass("hide");
			
			// 表格设置
			var tableSetting = {
					 // 表格ID
					 tableId : "#couponDataTable",
					 // 数据URL
					 url : url,
					 index: 20160803,
					 // 表格默认排序
					 aaSorting : [[ 0, "desc" ]],
					 // 表格列属性设置
					 aoColumns : [  { "sName": "couponType", "sWidth": "5%" },  
									{ "sName": "ruleCode", "sWidth": "5%", "bVisible" : false},   
									{ "sName": "ruleName", "sWidth": "10%"}, 
									{ "sName": "couponNo", "sWidth": "8%"}, 
									{ "sName": "status", "sWidth": "5%" },  
									{ "sName": "createTime", "sWidth": "10%" }, 										
									{ "sName": "endTime", "sWidth": "10%" },  
									{ "sName": "finishTime", "sWidth": "10%" },  
									{ "sName": "relationNoA", "sWidth": "8%"}, 
									{ "sName": "relationNoB", "sWidth": "8%"},
									{ "sName": "operate", "sWidth": "10%","bSortable": false}], 
					// 不可设置显示或隐藏的列	
					aiExclude :[0,2,3,4],
					// 横向滚动条出现的临界宽度
					sScrollX : "100%"
			};
			// 调用获取表格函数
			getTable(tableSetting);
			
	    }		
};

var binolssprm77 =  new BINOLSSPRM77();

/**
 * 短信重发
 */
function sendMsg(couponNo){
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
					activeSendMsg(couponNo);
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

function activeSendMsg(couponNo){
	
	var url = $("#sendUrl").attr("href");
	var brandCode = $("#brandCode").val();
	var mobile = $("#memPhone").val();
	var memberCode = $("#memCount").val();
	var bpCode = $("#bpCode").val();
	
	var param = "brandCode="+brandCode+"&couponNo="+couponNo+"&mobile="+mobile+"&memberCode="+memberCode+"&bpCode="+bpCode;
	
	cherryAjaxRequest({
		url:url,
		param:param,
		callback:callBack
	});
	
	function callBack(data){
		oTableArr[20160803].fnDraw();
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

$(function(){
	document.title = '优惠券查询';
});
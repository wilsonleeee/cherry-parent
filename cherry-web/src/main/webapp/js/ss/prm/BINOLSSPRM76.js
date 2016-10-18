function BINOLSSPRM76() {
	
};

BINOLSSPRM76.prototype = {
		
		"search" : function() {
			if(!$('#mainForm').valid()) {
				return false;
			}
			var url = $("#couponUrl").attr("href");
			var params= $("#mainForm").serialize();
			if(params != null && params != "") {
				url = url + "?" + params;
			}

			// 显示结果一览			
			$("#couponUsed").removeClass("hide");		

			// 表格设置
			var tableSetting = {
					 // 表格ID
					 tableId : "#couponUsedDataTable",
					 // 数据URL
					 url : url,
					 index: 2015,
					 // 表格默认排序
					 aaSorting : [[ 10, "desc" ]],
					 // 表格列属性设置
					 aoColumns : [  { "sName": "couponNo", "sWidth": "10%"},
									{ "sName": "couponType", "sWidth": "10%","sClass":"center"},
									{ "sName": "relationBill", "sWidth": "10%" },
									{ "sName": "billCode", "sWidth": "10%" },
					                { "sName": "counterCode", "sWidth": "5%" ,"sClass":"center"},
					                { "sName": "counterName", "sWidth": "15%" ,"sClass":"center"},
									{ "sName": "memberName", "sWidth": "5%","bSortable": false,"sClass":"center", "bVisible" : false},
									{ "sName": "userMemCode", "sWidth": "5%","bSortable": false},
									{ "sName": "userMobile", "sWidth": "5%","bSortable": false},
									{ "sName": "bpCode", "sWidth": "5%","bSortable": false, "bVisible" : false},
									{ "sName": "userBP", "sWidth": "5%","bSortable": false, "bVisible" : false},
									{ "sName": "useTime", "sWidth": "5%" },	
									{ "sName": "status", "sWidth": "5%" },	
									{ "sName": "rulecode", "sWidth": "5%", "bVisible" : false},
									{ "sName": "couponCode", "sWidth": "15%", "bVisible" : false},						
									{ "sName": "operator", "sWidth": "5%","bSortable": false}],
					// 不可设置显示或隐藏的列	
					aiExclude :[0,3,5,7,11],
					// 横向滚动条出现的临界宽度
					sScrollX : "100%"
			};
			// 调用获取表格函数
			getTable(tableSetting);
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

var binolssprm76 =  new BINOLSSPRM76();

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
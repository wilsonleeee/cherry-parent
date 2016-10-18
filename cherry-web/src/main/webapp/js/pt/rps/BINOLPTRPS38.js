$(function(){
	if($('#productId').length > 0){
		// 产品选择绑定
		productBinding({elementId:"nameTotal",showNum:20,targetId:'productId',validFlag:'validFlag'});
	}else{
		// 产品选择绑定
		productBinding({elementId:"nameTotal",showNum:20,validFlag:'validFlag'});
	}
	
});
// 查询
function search(url){
	var $form = $("#mainForm");
	// 表单验证
	if(!$form.valid()){
		return;
	}
	var aoColumns = null;
	var $groupType = $('#groupType');
	if($groupType.val() == '1'){
		aoColumns = [{ "sName": "no","bSortable":false}, 			// 0
		             { "sName": "nameTotal"},					// 1
		             { "sName": "unitCode"},						// 2
		             { "sName": "barCode"},						// 3
//		             { "sName": "originalBrand","sClass":"center"},	 // 3.1
		             { "sName": "moduleCode","sClass":"center"},	 // 3.2
		             { "sName": "price","sClass":"alignRight"},
		             { "sName": "quantity","sClass":"alignRight"}];// 4
	}else{
		aoColumns = [{ "sName": "no","bSortable":false}, 			// 0
		 			{ "sName": "nameTotal"},					// 1
		 			{ "sName": "unitCode"},						// 2
		 			{ "sName": "barCode"},						// 3
//		 			{ "sName": "originalBrand","sClass":"center"},	 // 3.1
		 			{ "sName": "moduleCode","sClass":"center"},	 // 3.2
		 			{ "sName": "price","sClass":"alignRight"},
		 			{ "sName": "quantity","sClass":"alignRight"}];// 4
	}
	// 显示查询结果
	$("#section").show();
	// 查询参数序列化
	 var params= getSearchParams();
	 url = url + "?" + params;
	 // 表格设置
	 var tableSetting = {
			 // 表格ID
			 tableId : '#dataTable',
			 // 数据URL
			 url : url,
			 // 表格列属性设置
			 aoColumns : aoColumns,	
			// 不可设置显示或隐藏的列	
			aiExclude :[0, 1, 2],
			// 横向滚动条出现的临界宽度
			sScrollX : "100%",
			// 固定列数
			fixedColumns : 2,
			callbackFun : function (msg){
				var $msg = $("<div></div>").html(msg);
	 		}
	 };
	 // 调用获取表格函数
	 getTable(tableSetting);
}		
//详细画面
function getDetail(_this){
	var url = $(_this).attr("href");
	url += "&csrftoken=" +$("#csrftoken").val() + "&" +getRangeParams();
	popup(url,{width:"1200"});
}
//查询参数序列化
function getSearchParams(){
	var $form = $("#mainForm");
	var params= $form.find("div.column").find(":input").serialize(); 
	params = params + "&csrftoken=" +$("#csrftoken").val();
	params = params + "&" +getRangeParams();
	return params;
}

/**
 * 一览概要导出
 * @param url
 */
function exportSummaryExcel(url) {
	if($(".dataTables_empty:visible").length==0){
		if (!$('#mainForm').valid()) {
	        return false;
		};
		
		var params = getSearchParams();
		url = url + "?" + params;
		window.open(url,"_self");
	}
}


function setRPS38Params(id){
	var $hideDiv = $(id);
	var $hiddens = $hideDiv.children(":input");
	var params = $hideDiv.find("div").find("input[name='params']").val();
	var JSON = [];
	$hiddens.each(function(){
		var $this = $(this);
		JSON.push('"'+$this.prop("name")+'":"'+ $this.val() +'"');
	});
	if(params != null && params != undefined && params != ""){
		if(JSON.length > 0){
			params = params.substring(0,params.length-1);
			params += "," + JSON.toString() + "}";
		}
	}else{
		params = "{" + JSON.toString() + "}";
	}
	$("#e_params").val(params);
}

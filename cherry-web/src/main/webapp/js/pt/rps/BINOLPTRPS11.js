$(function(){
	if($('#productId').length > 0){
		// 产品选择绑定
		productBinding({elementId:"nameTotal",showNum:20,targetId:'productId',validFlag:'validFlag'});
	}else{
		// 产品选择绑定
		productBinding({elementId:"nameTotal",showNum:20,validFlag:'validFlag'});
	}
	
	// 表单验证配置
	cherryValidate({						
		formId: 'mainForm',
		rules: {
			startDate: {required: true,dateValid: true},
			endDate: {required: true,dateValid: true}
		}
	});
});

//对象JSON化
function toJSON(obj) {
	var JSON = [];
	var propValArr = [];
	$(obj).find(':input').each(
			function() {
				$this = $(this);
				if (($this.attr("type") == "radio" && this.checked)
						|| $this.attr("type") != "radio") {
					if ($.trim($this.val()) != '') {
						var name = $this.attr("name");
						if (name.indexOf("_") != -1) {
							name = name.split("_")[0];
						}
						
						if(name == 'propValId'){
							propValArr.push('"'+encodeURIComponent($.trim($this.val())
									.replace(/\\/g, '\\\\').replace(
											/"/g, '\\"')) + '"');
						} 
						JSON.push('"'
								+ encodeURIComponent(name)
								+ '":"'
								+ encodeURIComponent($.trim($this.val())
										.replace(/\\/g, '\\\\').replace(
												/"/g, '\\"')) + '"');
						
					}
				}
			});
	JSON.push('"propValArr":[' + propValArr.toString()+']');
	return "{" + JSON.toString() + "}";
}

// 对象JSON数组化
function toJSONArr($obj) {
	var JSONArr = [];
	$obj.each(function() {
		JSONArr.push(toJSON(this));
	});
	return "[" + JSONArr.toString() + "]";
}

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
		             { "sName": "originalBrand","sClass":"center"},	 // 3.1
		             { "sName": "moduleCode","sClass":"center"},	 // 3.2
		             { "sName": "saleUnit","sClass":"center"},	 // 3.3
		             { "sName": "spec","sClass":"center"},	 // 3.4
		             { "sName": "price","sClass":"alignRight"},
		             { "sName": "startQuantity","sClass":"alignRight"},// 4
		             { "sName": "startAmount","sClass":"alignRight"},
		             { "sName": "inQuantity","sClass":"alignRight"},	// 5
		             { "sName": "outQuantity","sClass":"alignRight"},// 6
		             { "sName": "endQuantity","sClass":"alignRight"},
		             { "sName": "endAmount","sClass":"alignRight"}];
	}else{
		aoColumns = [{ "sName": "no","bSortable":false}, 			// 0
		 			{ "sName": "nameTotal"},					// 1
		 			{ "sName": "unitCode"},						// 2
		 			{ "sName": "originalBrand","sClass":"center"},	 // 3.1
		 			{ "sName": "moduleCode","sClass":"center"},	 // 3.2
		 			{ "sName": "saleUnit","sClass":"center"},	 // 3.3
		            { "sName": "spec","sClass":"center"},	 // 3.4
		 			{ "sName": "price","sClass":"alignRight"},
		 			{ "sName": "startQuantity","sClass":"alignRight"},// 4
		            { "sName": "startAmount","sClass":"alignRight"},
		 			{ "sName": "inQuantity","sClass":"alignRight"},	// 5
		 			{ "sName": "outQuantity","sClass":"alignRight"},// 6
		 			{ "sName": "endQuantity","sClass":"alignRight"},
		 			{ "sName": "endAmount","sClass":"alignRight"}];
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
				var $headInfo = $("#headInfo",$msg);
				$("#headInfo").html($headInfo.html());
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
	var params= $("#mainForm").find("#rps11searchId").find(":input").serialize();
	params = params + "&csrftoken=" +$("#csrftoken").val();
	params = params + "&" +getRangeParams();
	//产品分类
	params += "&cateInfo=" + toJSONArr($("#cateInfo").find(".detail").children().children());
	return params;
}

//xls导出
function exportExcel(url){
	if($(".dataTables_empty:visible").length==0){
		if (!$('#mainForm').valid()) {
	        return false;
	    };
		var callback = function(msg) {
			var params = getSearchParams();
			url = url + "?" + params;
			window.open(url,"_self");
    	}
    	exportExcelReport({
    		url: $("#exporChecktUrl").attr("href"),
    		param: getSearchParams(),
    		callback: callback
    	});
		
    }
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

//导出CSV
function ptrps11_exportCsv() {
	if($(".dataTables_empty:visible").length==0) {
		if (!$('#mainForm').valid()) {
	        return false;
	    };
	    var params = getSearchParams();
		exportReport({
			exportUrl:$("#exportCsvUrl").attr("href"),
			exportParam:params
		});
	}
}
$(document).ready(function() {
//	// 表格列选中
//	$('thead th').live('click',function(){
//		$("th.sorting").removeClass('sorting');
//		$(this).addClass('sorting');
//	});
	plscf06_searchCode();
} );


// 查询code值管理表一览
function plscf06_searchCode() {
	var url = $("#codeListUrl").attr("href");
	var params= $("#searchCodeForm").serialize();
	if(params != null && params != "") {
		url = url + "?" + params;
	}
	// 显示结果一览
	$("#codeSection").removeClass("hide");
	// 表格设置
	var tableSetting = {
			 // 表格ID
			 tableId : '#dateTable',
			 // 数据URL
			 url : url,
			 // 表格默认排序
			 aaSorting : [[ 0, "asc" ]],
			 // 表格列属性设置
			 aoColumns : [
			                { "sName": "u_codeType", "sWidth": "14%" },
			                { "sName": "u_codeName", "sWidth": "20%"},
							{ "sName": "u_keyDescription", "sWidth": "15%"},
							{ "sName": "u_value1Description", "sWidth": "20%"},
							{ "sName": "u_value2Description", "sWidth": "15%"},
							{ "sName": "u_value3Description", "sWidth": "15%"}],
			// 横向滚动条出现的临界宽度
			sScrollX : "100%"
	};
	// 调用获取表格函数
	getTable(tableSetting);
}

//根据组织查询品牌List
function plscf06_searchBrand(url,object,defaultSelect) {
	
	var $select = $('#brandInfoId');
	$select.empty();
//	$select.append('<option value="">'+defaultSelect+'</option>');
	var param = $(object).serialize();
	param += (param ? "&":"") + "csrftoken=" + getTokenVal();
	var callback = function(msg) {
		if(msg) {
			var json = eval('('+msg+')');
			for(var i in json) {
				$select.append('<option value="'+json[i].brandCode+'">'+json[i].brandName+'</option>');
			}
		}
	};
	cherryAjaxRequest({
		url: url,
		param: param,
		callback: callback
	});
}

// 刷新系统Code值
function plscf06_refreshCodes(obj) {
	cherryAjaxRequest({
		url: $(obj).attr("href"),
		argDataType : 'json',
		callback: function(msg) {
			if(window.JSON && window.JSON.parse) {
				var msgJson = window.JSON.parse(msg);
//				alert(msgJson['result']);
				refreshCodeDialog(msgJson['result']);
			}
		}
	});
}

/**
*刷新code值提示信息
*@param message:提示内容
*
*/
function refreshCodeDialog(message){
	$("#refreshCodeMeg").find("p").html('<span>'+message+'</span>');
	var dialogSetting = {
			dialogInit: "#dialogInit",
			text: $("#refreshCodeMeg").html(),
			width: 	500,
			height: 300,
			title: 	$("#refreshCodeTitle").text(),
			confirm: $("#dialogConfirm").text(),
			confirmEvent: function(){removeDialog("#dialogInit");},
			closeEvent: function(){removeDialog("#dialogInit");}
		};
		openDialog(dialogSetting);
}
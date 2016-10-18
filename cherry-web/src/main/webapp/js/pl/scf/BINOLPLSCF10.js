/*
 * 全局变量定义
 */
var plscf10_global = {};
//是否需要解锁
plscf10_global.needUnlock = true;

$(document).ready(function() {
	if (window.opener) {
       window.opener.lockParentWindow();
    }
});

window.onbeforeunload = function(){
	if (plscf10_global.needUnlock) {
		if(window.opener){
			window.opener.unlockParentWindow();
		}
	}
};

function doClose(){
	window.close();
}



$(document).ready(function() {
//	// 表格列选中
//	$('thead th').live('click',function(){
//		$("th.sorting").removeClass('sorting');
//		$(this).addClass('sorting');
//	});
	plscf10_searchCode();
} );


// 查询code值管理表一览
function plscf10_searchCode() {
	var url = $("#coderListUrl").attr("href");
	var parentToken = parentTokenVal();
	$("#parentCsrftoken").val(parentToken);
	
	var params= $("#searchCoderForm").serialize();
	if(params != null && params != "") {
		url = url + "?" + params;
	}
	// 显示结果一览
	$("#coderSection").removeClass("hide");
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
			                { "sName": "codeType", "sWidth": "14%" },
			                { "sName": "codeKey", "sWidth": "14%"},
							{ "sName": "value1", "sWidth": "15%"},
							{ "sName": "value2", "sWidth": "15%"},
							{ "sName": "value3", "sWidth": "14%"},
							{ "sName": "Grade", "sWidth": "14%"},
							{ "sName": "CodeOrder", "sWidth": "14%"}],
							
			// 横向滚动条出现的临界宽度
			sScrollX : "100%"
	};
	// 调用获取表格函数
	getTable(tableSetting);
}

//返回
function plscf10_doBack(){
	var tokenVal = parentTokenVal();
	$("#parentCsrftoken1").val(tokenVal);
	plscf10_global.needUnlock = false;
	$("#toDetailForm").submit();
}

//code值添加初始画面
function plscf10_addCoder(){
	var tokenVal = parentTokenVal();
	$("#parentCsrftoken2").val(tokenVal);
	plscf10_global.needUnlock = false;
	$("#addCoderForm").submit();
}

//code值详细画面
function plscf10_coderDetail(codeId){
	var tokenVal = parentTokenVal();
	$("#parentCsrftoken3").val(tokenVal);	
	$("#coderID").val(codeId);	
	plscf10_global.needUnlock = false;
	$("#coderDetailForm").submit();
}


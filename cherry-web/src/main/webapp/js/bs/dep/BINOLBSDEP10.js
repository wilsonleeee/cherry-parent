
$(function(){

	bsdep10_searchBrandInfo();
});

// 查询品牌一览
function bsdep10_searchBrandInfo() {
	var url = $("#brandInfoListUrl").attr("href");
	$('#searchBrandForm :input').each(function(){
		$(this).val($.trim(this.value));
	});
	var params= $("#searchBrandForm").serialize();
	if(params != null && params != "") {
		url = url + "?" + params;
	}
	// 显示结果一览
	$("#BrandSection").removeClass("hide");
	// 表格设置
	var tableSetting = {
			 // 表格ID
			 tableId : '#dataTable',
			 // 数据URL
			 url : url,
			 // 表格默认排序
			 aaSorting : [[ 1, "asc" ]],
			 // 表格列属性设置
			 aoColumns : [  { "sName": "number", "sWidth": "5%", "bSortable": false },
			                { "sName": "BrandCode", "sWidth": "10%" },
							{ "sName": "BrandNameChinese", "sWidth": "20%" },
							{ "sName": "BrandNameForeign", "sWidth": "20%" },
							{ "sName": "BrandNameShort", "sWidth": "15%", "bVisible": false },
							{ "sName": "BrandNameForeignShort", "sWidth": "15%", "bVisible": false },
							{ "sName": "ValidFlag", "sWidth": "5%", "sClass":"center" },
							{ "sName": "Operate", "sWidth": "10%", "bSortable": false,"sClass": "center" }],
			// 不可设置显示或隐藏的列
			aiExclude :[0, 1],
			// 横向滚动条出现的临界宽度
			sScrollX : "100%",
			// 固定列数
			fixedColumns : 2
	};
	// 调用获取表格函数
	getTable(tableSetting);
}

// 添加品牌画面初期表示
function bsdep10_addBrandInit(url) {
	var param = $('#csrftoken').serialize();
	url = url + '?' + param;
	popup(url);
}

//// 删除品牌确认画面
//function bsdep10_deleteBrandInit(object) {
//	var brandName = $(object).parent().parent().find('td:first-child').next().next().text();
//	var param = $(object).parent().find(':input').serialize();
//	var dialogSetting = {
//		dialogInit: "#dialogInit",
//		text: $("#deleteBrandText").html(),
//		width: 	500,
//		height: 300,
//		title: 	$("#deleteBrandTitle").text()+':'+brandName,
//		confirm: $("#dialogConfirm").text(),
//		cancel: $("#dialogCancel").text(),
//		confirmEvent: function(){bsdep10_deleteBrand($(object).attr("href"), param);},
//		cancelEvent: function(){removeDialog("#dialogInit");}
//	};
//	openDialog(dialogSetting);
//	
//}
//
//// 删除品牌处理
//function bsdep10_deleteBrand(url,param) {
//	var callback = function(msg) {
//		$("#dialogInit").html(msg);
//		if($("#errorMessageDiv").length > 0) {
//			$("#errorMessageDiv").find("span").html($("#errorMessageDiv").next().text());
//			$("#dialogInit").dialog( "option", {
//				buttons: [{
//					text: $("#dialogClose").text(),
//				    click: function(){removeDialog("#dialogInit");if(oTableArr[0] != null)oTableArr[0].fnDraw(); }
//				}]
//			});
//		} else {
//			removeDialog("#dialogInit");
//			if(oTableArr[0] != null)oTableArr[0].fnDraw();
//		}
//	};
//	cherryAjaxRequest({
//		url: url,
//		param: param,
//		callback: callback
//	});
//}

//品牌启用停用处理
function bsdep10_editValidFlag(flag, url) {
	
	var param = "";
	if($('#dataTable_Cloned :input[checked]').length == 0) {
		$("#errorMessage").html($("#errorMessageTemp").html());
		return false;
	}
	param = $('#dataTable_Cloned :input[checked]').nextAll().serialize();
	var callback = function() {
		if(oTableArr[0] != null)oTableArr[0].fnDraw();
	};
	bscom03_deleteConfirm(flag, url, param, callback);
}
/*
 * 全局变量定义
 */
var popDataTable_global = {};

// datatable 索引
popDataTable_global.index = 0;
//上传文件Dialog
popDataTable_global.fileUpDialogHtml = "";
// 厂商信息Dialog
popDataTable_global.factoryDialogHtml = "";
// 部门信息Dialog
popDataTable_global.departDialogHtml = "";
// 考核问卷信息Dialog
popDataTable_global.checkPaperDialogHtml = "";
// 柜台信息Dialog
popDataTable_global.counterDialogHtml = "";
// 产品分类信息Dialog
popDataTable_global.prtCateDialogHtml = "";
// 促销品分类信息Dialog
popDataTable_global.prmCateDialogHtml = "";
//产品信息Dialog
popDataTable_global.prtDialogHtml = "";
//产品信息DialogOne
popDataTable_global.prtDialogOneHtml = "";
// 促销品信息Dialog
popDataTable_global.prmDialogHtml = "";
//共通Dialog
popDataTable_global.commDialogHtml = "";
//会员信息Dialog
popDataTable_global.memDialogHtml = "";
// 沟通信息模板Dialog
popDataTable_global.msgTemplateDialogHtml = "";
//对象批次Dialog
popDataTable_global.objBatchDialogHtml = "";
popDataTable_global.rptPrtDialogHtml = "";
popDataTable_global.rptCateDialogHtml = "";

function popDataTableOfPrmInfo (prmPopParam){
	$('#promotionCateTitle').find('li').removeClass('ui-tabs-selected');
	$('#promotionCateTitle').find('li').first().addClass('ui-tabs-selected');
	var promotionCateCode = $('#promotionCateTitle').find('li.ui-tabs-selected').attr("id");
//	if(prmPopParam.index != null){
//		oTableArr[prmPopParam.index]= null;	
//	}else{
//		oTableArr[0]= null;
//	}
	// 操作对象
	var thisObj = prmPopParam.thisObj;
	var url = popDataTableSetUrl(prmPopParam,"prmSearchUrl");
	var joinChar = "";
	if (url.lastIndexOf("?") == -1) {
		joinChar = "?";
	} else if (url.lastIndexOf("?") < url.length - 1) {
		joinChar = "&";
	}
	url += joinChar + "promotionCateCD="+promotionCateCode;
	
	// 弹出后是否锁定底部页面
	if (prmPopParam.modal!=null){
		var modal  = prmPopParam.modal;
	}else{
		var modal = true;
	}
	
	if (prmPopParam.dialogBody!=null){
		var dialogBody  = prmPopParam.dialogBody;
	}else{
		var dialogBody = null;
	}
	
	popDataTableModalSet(modal,prmPopParam,"prmSearchUrl");
	var tableSetting = {
			 // 一页显示页数
			 iDisplayLength:10,
			 // 表格ID
			 tableId : '#prm_dataTable',
			 // 数据URL
			 url : url,
			 // 表格列属性设置
			 aoColumns : [  { "sName": "checkbox","sWidth": "5%","bSortable": false}, 	// 0
							{ "sName": "unitCode","sWidth": "15%"},                     // 1
							{ "sName": "barCode","sWidth": "15%"},                      // 2
							{ "sName": "nameTotal","sWidth": "25%"},                    // 3
							{ "sName": "primaryCategory","sWidth": "15%"},              // 4
							{ "sName": "secondryCategory","sWidth": "15%"},             // 5
							{ "sName": "smallCategory","sWidth": "15%"},                // 6
							{ "sName": "standardCost","sWidth": "15%"}],                   // 7
			index:prmPopParam.index,
			colVisFlag: true,
			// 横向滚动条出现的临界宽度
			sScrollX : "100%",
			// pop窗口弹出后回调函数
			fnDrawCallback:prmPopParam.callback
	 };
	
	// 调用获取表格函数
	getTable(tableSetting);

	var dialogSetting = {
			modal: modal,
			width:860, 
			height:'auto', 
			zIndex: 9999, 
			resizable:false,
			title:$("#PopPrmTitle").text(), 
			close: function(event, ui) {
				if (dialogBody!=null){
					closeCherryDialog('promotionDialog',dialogBody);				
					oTableArr[prmPopParam.index]= null;
				}else{
					$(this).dialog( "destroy" );
				}
				if (!modal && prmPopParam.autoClose!=null){
					$("body").unbind('click');
				}
			}
	};
//	if(thisObj){
//		var opos = $(thisObj).offset();
//		oleft = parseInt(opos.left,10) ;
//		otop = parseInt(opos.top, 10) + 25 - $(document).scrollTop();
//		dialogSetting.position = [ oleft , otop ];
//	}
	$('#promotionDialog').dialog(dialogSetting);
}

function promotionCateFilter(thisObj, tableIndex) {
	
	$(thisObj).siblings().removeClass('ui-tabs-selected');
	$(thisObj).addClass('ui-tabs-selected');
	
	var index;
	if (tableIndex!=null && tableIndex!=""){
		index = tableIndex;
	}else{
		index = popDataTable_global.index;
	}
	if (index==null){
		index = 0;
	}
	
	var promotionCateCode = $(thisObj).attr("id");
	var oSettings = oTableArr[index].fnSettings();
	var url = oSettings.sAjaxSource;
	if(url.indexOf("promotionCateCD") > -1) {
		var arrays = url.split("promotionCateCD");
		url = arrays[0] + "promotionCateCD="+promotionCateCode;
	} else {
		var joinChar = "";
		if (url.lastIndexOf("?") == -1) {
			joinChar = "?";
		} else if (url.lastIndexOf("?") < url.length - 1) {
			joinChar = "&";
		}
		url += joinChar + "promotionCateCD="+promotionCateCode;
	}
	oSettings.sAjaxSource = url;
	// 刷新表格数据
	oTableArr[index].fnDraw();
}

function popDataTableOfPrtInfo(prtPopParam){
	if(prtPopParam.index != null){
		oTableArr[prtPopParam.index]= null;	
	}else{
		oTableArr[0]= null;
	}
	// 操作对象
	var thisObj = prtPopParam.thisObj;
	var url = popDataTableSetUrl(prtPopParam,"prtSearchUrl");
	
	// 弹出后是否锁定底部页面
	if (prtPopParam.modal!=null){
		var modal  = prtPopParam.modal;
	}else{
		var modal = true;
	}
	
	if (prtPopParam.dialogBody!=null){
		var dialogBody  = prtPopParam.dialogBody;
	}else{
		var dialogBody = null;
	}
	popDataTableModalSet(modal,prtPopParam,"prtSearchUrl");
	var tableSetting = {
			 // 一页显示页数
			 iDisplayLength:10,
			 // 表格ID
			 tableId : '#prt_dataTable',
			 // 数据URL
			 url : url,
			 // 表格列属性设置
			 aoColumns : [  { "sName": "checkbox","sWidth": "5%","bSortable": false}, 	// 0
							{ "sName": "unitCode","sWidth": "15%"},                     // 1
							{ "sName": "barCode","sWidth": "15%"},                      // 2
							{ "sName": "nameTotal","sWidth": "25%"},                    // 3
							{ "sName": "primaryCategoryBig","sWidth": "10%"},           // 4
							{ "sName": "primaryCategorySmall","sWidth": "10%"},         // 5
							{ "sName": "salePrice","sWidth": "10%"},
							{ "sName": "standardCost","sWidth": "10%"}],                   // 6
			index:prtPopParam.index,
			colVisFlag: true,
			// 横向滚动条出现的临界宽度
			sScrollX : "100%",
			// pop窗口弹出后回调函数
			fnDrawCallback:prtPopParam.callback
	 };
	
	// 调用获取表格函数
	getTable(tableSetting);
	var dialogSetting = {modal: modal,width:800, height:'auto', zIndex: 9999, resizable:false,title:$("#PopProTitle").text(), 
			close: function(event, ui) {
		if (dialogBody!=null){
			closeCherryDialog('productDialog',dialogBody);				
			oTableArr[prtPopParam.index]= null;			// 其中数组中的0要根据自己的datatable序号而定，如果页面只存在一个datatable则为0	
		}else{
			$(this).dialog( "destroy" );
		}
		if (!modal && prtPopParam.autoClose!=null){
			$("body").unbind('click');
		}
	}};
//	if(thisObj){
//		var opos = $(thisObj).offset();
//		oleft = parseInt(opos.left,10) ;
//		otop = parseInt(opos.top, 10) + 25 - $(document).scrollTop();
//		dialogSetting.position = [ oleft , otop ];
//	}
	$('#productDialog').dialog(dialogSetting);
}


function popDataTableOfCounter (thisObj,param,callback){
	if(popDataTable_global.counterDialogHtml == ""){
		popDataTable_global.counterDialogHtml = $("#counterDialog").html();
	}
	var dialogBody = popDataTable_global.counterDialogHtml;
	var csrftoken = $('#csrftoken').serialize();
	if(!csrftoken) {
		csrftoken = $('#csrftoken',window.opener.document).serialize();
	}
	var url = $('#getCounterListUrl').text()+'?' + csrftoken + (param?('&'+param):'');
	var tableSetting = {
			 // 一页显示页数
			 iDisplayLength:10,
			 // 表格ID
			 tableId : '#counter_dataTable',
			 // 不自动计算列宽度 
			 bAutoWidth: false,
			 // 数据URL
			 url : url,
			 // 表格列属性设置
			 aoColumns : [  { "sName": "checkbox","sWidth": "10%","bSortable": false}, 	// 0
							{ "sName": "CounterCode","sWidth": "45%"},                     // 1
							{ "sName": "CounterNameIF","sWidth": "45%","bSortable": false}],    // 2
			 index: 9,
			 colVisFlag: false
	 };
	
	// 调用获取表格函数
	getTable(tableSetting);

	var dialogSetting = {
		bgiframe: true,
		width:600, 
		height:'auto',
		minWidth:600,
		zIndex: 9999,
		modal: true, 
		title:$("#PopcounterTitle").text(),
		close: function(event, ui) { 
			closeCherryDialog("counterDialog",dialogBody);				
			oTableArr[9]= null;
		}
	};
	var option = {};
	option.click = callback;
	setDialogSetting($('#counterDialog'),dialogSetting,option);
}

function popDataTableOfFactory (thisObj, params){
	if ("" == popDataTable_global.factoryDialogHtml) {
		// 临时存放厂商信息页面内容
		popDataTable_global.factoryDialogHtml = $("#factoryDialog").html();
	} else {
		$("#factoryDialog").html(popDataTable_global.factoryDialogHtml);
	}
	var url = $('#popFactoryUrl').text();
	var param = params || ("csrftoken=" + $('#csrftoken').val());
	cherryAjaxRequest({
		url: url,
		param: param,
		callback: function(msg) {
			$("#factory_dataTable tbody").html(msg);
		}
	});
	var dialogSetting = {
		width:600, 
		height:'auto',
		minWidth:600,
		//minHeight:320,
		zIndex: 9999,
		title:$("#PopvendorTitle").text(),
		close: function(event, ui) { $(this).dialog( "destroy" ); }
	};
//	if(thisObj) {
//		var opos = $(thisObj).offset();
//		var oleft = parseInt(opos.left,10) ;
//		var otop = parseInt(opos.top, 10) + 25 - $(document).scrollTop();
//		dialogSetting.position = [ oleft , otop ];
//	}
	$('#factoryDialog').dialog(dialogSetting);
}

function tableFilter(obj, params) {
	var url = $('#popFactoryUrl').text();
	var param = params || ("csrftoken=" + $('#csrftoken').val());
	param += "&" + $(obj).serialize();
	cherryAjaxRequest({
		url: url,
		param: param,
		callback: function(msg) {
			$("#factory_dataTable tbody").html(msg);
		}
	});
}

function popDataTableOfDepartInfo(option){
	var param, callback, click;
	var index=9;
	var mode=1;
	option.dialogId = 'departDialogInit';
	option.popTableId = 'departDataTableInit';
	var $dialog = $("#" + option.dialogId);
	if (!isEmpty(option.mode)){mode = option.mode;}
	//后台传递的参数
	if(!isEmpty(option.param)){
		param = option.param;
	}
	if(!isEmpty(option.checkType)){
		param += "&checkType=" + option.checkType;
	}
	//点击画面上的确定按钮所做的操作
	if(!isEmpty(option.click)){
		click = option.click;
	}
	// 弹出后是否锁定底部页面
	if (!isEmpty(option.modal)){
		var modal  = option.modal;
	}else{
		var modal = true;
	}
	//弹出框大小是否可变
	if(!isEmpty(option.resizable)){
		var resizable = option.resizable;
	}else{
		var resizable = false;
	}
	var dialogBody = $("#departDialogInit").html();
	oTableArr[index] = null;
	var url = $('#popDepartUrlInit').text();
	var csrftoken = $('#csrftoken').serialize();
	if(!csrftoken) {
		csrftoken = $('#csrftoken',window.opener.document).serialize();
	}
	url += "?" + csrftoken + (param?('&'+param):'');
	var validFlag = $("#departDialogInit").find(":input[name='validFlag9']").filter(":checked").val();
	url += "&param=" + validFlag;
	// 目标区内容加载到缓存区
	exchangeHtml(option);
	var tableSetting = {
			 // 一页显示页数
			 iDisplayLength:10,
			 // 表格ID
			 tableId : '#'+option.popTableId,
			 // 数据URL
			 url : url,
			 // 排序
			 aaSorting:[[4, "asc"]],
			 // 表格列属性设置
			 aoColumns : [  { "sName": "organizationId","sWidth": "10%","bSortable": false}, 	// 0
							{ "sName": "departCode","sWidth": "35%"},                     	// 1
							{ "sName": "departName","sWidth": "35%"},
							{ "sName": "type","sWidth": "20%"},
							{ "sName": "departGrade","bVisible": false}],    // 2
			index : index,
			colVisFlag: false,
			// pop窗口弹出后回调函数
			fnDrawCallback:function(){
				initPopInput(option);
				bindPopInput(option);
			}
	 };
	
	// 调用获取表格函数
	getTable(tableSetting);

	var dialogSetting = {
		modal:modal,
		bgiframe: true,
		width:600, 
		height:"auto",
		minWidth:600,
		zIndex: 9999,
		resizable: resizable,
		title:$("#PopdepartTitleInit").text(),
		close: function(event, ui) {
			if (mode==2){
				closeCherryDialog(option.dialogId,dialogBody);				
				oTableArr[index]= null;
			}else{
				$(this).dialog( "destroy" );
				$("#"+option.dialogId).remove();
			}
			if (!modal && option.autoClose!=null){
				$("body").unbind('click');
			} 
		}
	};
	
	// 设置Dialog共通属性
	setDialogSetting($dialog,dialogSetting,option);
	// 弹出框全选CHECKBOX绑定事件
	checkAllBind(option);
}

function popDataTableOfDepart (thisObj, param, callback){
	var dialogBody = $("#departDialog").html();
	if(oTableArr[19]){
		oTableArr[19]= null;
	}
	var url = $('#popDepartUrl').text();
	var csrftoken = $('#csrftoken').serialize();
	if(!csrftoken) {
		csrftoken = $('#csrftoken',window.opener.document).serialize();
	}
	url += "?" + csrftoken + (param?('&'+param):'');
	var validFlag = $("#departDialog").find(":input[name='validFlag19']").filter(":checked").val();
	url += "&param=" + validFlag;
	//判断停用部门是否可以查询
	if(param.indexOf("&valid=1")>-1){
		$("#departDialog").find("#spanRadio").hide();
	}
	var tableSetting = {
			 // 一页显示页数
			 iDisplayLength:10,
			 // 表格ID
			 tableId : '#departDataTable',
			 // 数据URL
			 url : url,
			 // 排序
			 aaSorting:[[4, "asc"]],
			 // 表格列属性设置
			 aoColumns : [  { "sName": "organizationId","sWidth": "10%","bSortable": false}, 	// 0
							{ "sName": "departCode","sWidth": "35%"},                     	// 1
							{ "sName": "departName","sWidth": "35%"},
							{ "sName": "type","sWidth": "20%"},
							{ "sName": "departGrade","bVisible": false}],    // 2
			index : 19,
			colVisFlag: false
	 };
	
	// 调用获取表格函数
	getTable(tableSetting);

	var dialogSetting = {
		bgiframe: true,
		width:600, 
		height:"auto",
		minWidth:600,
		zIndex: 9999,
		modal: true,
		resizable: false,
		title:$("#PopdepartTitle").text(),
		close: function(event, ui) {
			$('#departDialog_errorDisplay').empty();
			closeCherryDialog("departDialog",dialogBody);
		}
	};
//	if(thisObj) {
//		var opos = $(thisObj).offset();
//		var oleft = parseInt(opos.left,10) ;
//		var otop = parseInt(opos.top, 10) + 25 - $(document).scrollTop();
//		dialogSetting.position = [ oleft , otop ];
//	}
	$('#departDialog').dialog(dialogSetting);
	
	$('#departConfirm').unbind('click');
	$('#departConfirm').click(function(){
		var $select = $('#departDataTable').find(':input[checked]');
//		if($select.length == 0) {
//			$('#departDialog_errorDisplay').html($('#departDialog_error').html());
//			return;
//		} else {
			if(typeof(callback) == "function") {
				callback();
			}
			$('#departDialog_errorDisplay').empty();
			closeCherryDialog("departDialog",dialogBody);
//		}
	});
}
/**
 * 部门有效性切换
 * 
 * @param thisObj
 * @param index
 * @return
 */
function changeValidParam(thisObj,index) {
	if(index == null || index == undefined){
		index = 0;
	}
	var $this = $(thisObj);
	var paramVal = $this.val();
	var oSettings = oTableArr[index].fnSettings();
	var url = oSettings.sAjaxSource;
	url = replaceParamVal(url, "param1", paramVal, true);
	oSettings.sAjaxSource = url;
	// 刷新表格数据
	oTableArr[index].fnDraw();
}
/**
 * 部门有效性切换
 * 
 * @param thisObj
 * @param index
 * @return
 */
function changeValid(thisObj,index) {
	if(index == null || index == undefined){
		index = 0;
    }
	var $this = $(thisObj);
	var paramVal = $this.val();
	var oSettings = oTableArr[index].fnSettings();
	var url = oSettings.sAjaxSource;
	url = replaceParamVal(url, "param", paramVal, true);
	oSettings.sAjaxSource = url;
	// 刷新表格数据
	oTableArr[index].fnDraw();
}

function emptyResult() {
	$("#fileUpMsg").hide();
	$("#fileUpMsg").empty();
}

function uploadSuccess(rst, msg) {
	if ("OK" == rst) {
		$("#fileUpMsg").attr("class", "actionSuccess");
	} else {
		$("#fileUpMsg").attr("class", "actionError");
	}
	$("#fileUpMsg").html(msg);
	$("#fileUpMsg").show();
}
function popUploadFile(thisObj) {
	var dialogSetting = {
			bgiframe: true,
			width:450, 
			height:'auto',
			minWidth:350,
			//minHeight:280,
			zIndex: 9999,
			modal: true, 
			title: $("#UploadImage").text(),
			resizable : false,
			close: function(event, ui) { $(this).dialog( "destroy" ); }
		};
//		if(thisObj) {
//			var opos = $(thisObj).offset();
//			var oleft = parseInt(opos.left,10) ;
//			var otop = parseInt(opos.top, 10) + 25 - $(document).scrollTop();
//			dialogSetting.position = [ oleft , otop ];
//		}
		if ("" == popDataTable_global.fileUpDialogHtml) {
			// 临时存放上传图片页面内容
			popDataTable_global.fileUpDialogHtml = $("#fileUpDialog").html();
		} else {
			$("#fileUpDialog").html(popDataTable_global.fileUpDialogHtml);
		}
		$('#fileUpDialog').dialog(dialogSetting);
}

function fileUpDialogClose() {
	$('#fileUpDialog').dialog( "destroy" );
}


function uploadFile(options, pageKbn) {
	$("#fileUpMsg").hide();
	if($('#fileUp').val()==''){
		$("#filePath").val("");
		return false;
	}
	var url = "";
	var successFun = function(){};
	// 共通的上传文件画面
	if (!pageKbn) {
		// 清除结果信息
		emptyResult();
		url = $("#uploadImageUrl").text() + "?" + getParentToken();
		successFun = function (data, status)
		{	
			// 显示结果信息
			uploadSuccess(data.result, data.msg);
		};
	}
	var uploadOpts = jQuery.extend({
		url					: url,		// AJAX请求地址
		secureuri			: false,
		loadImgId			: "#loading",	// 等待图片ID
		isLoading			: true,	// 需要加载等待图片
		fileElementId		: "fileUp",
		dataType			: "json",
		success				: successFun,
		error				: function (data, status, e){
									alert(e);
								}
	}, options);
	if ("" == $("#" + uploadOpts.fileElementId).val()) {
		return false;
	}
	if (uploadOpts.isLoading) {
		$(uploadOpts.loadImgId)
		.ajaxStart(function(){
			$(this).show();
		})
		.ajaxComplete(function(){
			$(this).hide();
		});
	}
	$.ajaxFileUpload
	(
		{
			url				: uploadOpts.url,
			secureuri		: uploadOpts.secureuri,
			fileElementId	: uploadOpts.fileElementId,
			dataType		: uploadOpts.dataType,
			success			: uploadOpts.success,
			error			: uploadOpts.error
		}
	);
	return false;
}

function popDataTableOfEmployee (thisObj, params){
	var url = $('#popEmployeeUrl').text();
	var param = params || ("csrftoken=" + $('#csrftoken').val());
	url += "?" + param;
	var tableSetting = {
			 // 一页显示页数
			 iDisplayLength:10,
			 // 表格ID
			 tableId : '#employee_dataTable',
			 // 数据URL
			 url : url,
			 // 表格列属性设置
			 aoColumns : [  { "sName": "employeeId","sWidth": "10%","bSortable": false}, 	  // 0
							{ "sName": "employeeCode","sWidth": "10%"},                       // 1
							{ "sName": "employeeName","sWidth": "20%","bSortable": false},    // 2
							{ "sName": "departName","sWidth": "30%","bSortable": false},    // 2
							{ "sName": "positionName","sWidth": "30%","bSortable": false}],    // 2
			colVisFlag: false
	 };
	
	// 调用获取表格函数
	getTable(tableSetting);

	var dialogSetting = {
		bgiframe: true,
		width:600, 
		height:'auto',
		minWidth:600,
		//minHeight:320,
		zIndex: 9999,
		modal: true, 
		title:$("#EmployeeTitle").text(),
		close: function(event, ui) { $(this).dialog( "destroy" ); }
	};
//	if(thisObj) {
//		var opos = $(thisObj).offset();
//		var oleft = parseInt(opos.left,10) ;
//		var otop = parseInt(opos.top, 10) + 25 - $(document).scrollTop();
//		dialogSetting.position = [ oleft , otop ];
//	}
	$('#employeeDialog').dialog(dialogSetting);
}

/**
 * 弹出用户弹出框
 * @param url
 * @param param
 * @param value
 * @param callback
 */
function popDataTableOfUserList(url, param, value, callback) {
	if($("#searchUserDialog").length == 0) {
		$("body").append('<div style="display:none" id="searchUserDialog"></div>');
	} else {
		$("#searchUserDialog").empty();
	}
	var searchInitCallback = function(msg) {
		$("#searchUserDialog").html(msg);
		var dialogSetting = {
			dialogInit: "#searchUserDialog",
			text: msg,
			width: 	600,
			height: 300,
			title: 	$("#searchUserDialog").find("#searchUserTitle").text(),
			confirm: $("#searchUserDialog").find("#dialogConfirm").text(),
			cancel: $("#searchUserDialog").find("#dialogCancel").text(),
			confirmEvent: function(){
				if(typeof(callback) == "function") {
					callback("searchUserDataTable");
				}
				removeDialog("#searchUserDialog");
			},
			cancelEvent: function(){removeDialog("#searchUserDialog");}
		};
		openDialog(dialogSetting);
		
		if(oTableArr[31]) {
			oTableArr[31] = null;
		}
		var searchUrl = $("#searchUserListUrl").attr("href")+"?"+getSerializeToken();
		if(param) {
			searchUrl += "&" + param;
		}
		var tableSetting = {
				 // 表格ID
				 tableId : '#searchUserDataTable',
				 // 一页显示页数
				 iDisplayLength : 5,
				 // 数据URL
				 url : searchUrl,
				 // 表格默认排序
				 aaSorting : [[ 0, "asc" ]],
				 // 表格列属性设置
				 aoColumns : [  { "sName": "userId", "bSortable": false}, 	
								{ "sName": "code"},                      
								{ "sName": "name"}
							],
				index : 31,
				colVisFlag : false,
				fnDrawCallback : function() {
					// 显示上次选中的项
					if(value) {
						$('#searchUserDataTable').find(':input').each(function() {
							if($(this).val() == value) {
								$(this).prop('checked', true);
								return false;
							}
						});
					}
				}
		 };
		// 调用获取表格函数
		getTable(tableSetting);
	};
	cherryAjaxRequest({
		url: url,
		param: null,
		callback: searchInitCallback
	});
}

/**
 * 弹出所有部门弹出框
 * @param url
 * @param param
 * @param value
 * @param callback
 */
function popDataTableOfAllDepartList(url, param, value, callback) {
	if($("#searchDepartDialog").length == 0) {
		$("body").append('<div style="display:none" id="searchDepartDialog"></div>');
	} else {
		$("#searchDepartDialog").empty();
	}
	var searchInitCallback = function(msg) {
		$("#searchDepartDialog").html(msg);
		var dialogSetting = {
			dialogInit: "#searchDepartDialog",
			text: msg,
			width: 	600,
			height: 300,
			title: 	$("#searchDepartDialog").find("#searchDepartTitle").text(),
			confirm: $("#searchDepartDialog").find("#dialogConfirm").text(),
			cancel: $("#searchDepartDialog").find("#dialogCancel").text(),
			confirmEvent: function(){
				if(typeof(callback) == "function") {
					callback("searchDepartDataTable");
				}
				removeDialog("#searchDepartDialog");
			},
			cancelEvent: function(){removeDialog("#searchDepartDialog");}
		};
		openDialog(dialogSetting);
		
		if(oTableArr[32]) {
			oTableArr[32] = null;
		}
		var searchUrl = $("#searchDepartListUrl").attr("href")+"?"+getSerializeToken();
		if(param) {
			searchUrl += "&" + param;
		}
		var tableSetting = {
				 // 表格ID
				 tableId : '#searchDepartDataTable',
				 // 一页显示页数
				 iDisplayLength : 5,
				 // 数据URL
				 url : searchUrl,
				 // 表格默认排序
				 aaSorting : [[ 0, "asc" ]],
				 // 表格列属性设置
				 aoColumns : [  { "sName": "departId", "bSortable": false}, 	
								{ "sName": "code"},                      
								{ "sName": "name"}
							],
				index : 32,
				colVisFlag : false,
				fnDrawCallback : function() {
					// 显示上次选中的项
					if(value) {
						$('#searchDepartDataTable').find(':input').each(function() {
							if($(this).val() == value) {
								$(this).prop('checked', true);
								return false;
							}
						});
					}
				}
		 };
		// 调用获取表格函数
		getTable(tableSetting);
	};
	cherryAjaxRequest({
		url: url,
		param: null,
		callback: searchInitCallback
	});
}

/**
 * 弹出促销品,产品url设置
 * @param popParamObj
 * @param urlType
 * @return
 */
function popDataTableSetUrl(popParamObj,urlType){
	if (urlType =="prmSearchUrl"){
		var url = $('#prmSearchUrl').html();
	}else if (urlType =="prtSearchUrl"){
		var url = $('#prtSearchUrl').html();
	}
	
	// csrftoken
	if (popParamObj.csrftoken!=null){
		url = url + "?"+popParamObj.csrftoken;
	}else{
		var csrftoken = $('#csrftoken').serialize();
		if(!csrftoken) {
			csrftoken = $('#csrftoken',window.opener.document).serialize();
		}
		url = url + "?"+csrftoken;
	}
	// 可否BOM产品区分
	if (popParamObj.isBOM!=null){
		url = url + "&isBOM="+popParamObj.isBOM;
	}
	// 品牌ID
	if (popParamObj.brandInfoId!=null){
		url = url + "&brandInfoId="+popParamObj.brandInfoId;
	}
	// 部门ID
	if (popParamObj.organizationID!=null){
		url = url + "&organizationID="+popParamObj.organizationID;
	}
	// 价格种类
	if (popParamObj.priceType!=null){
		url = url + "&priceType="+popParamObj.priceType;
	}
	// 促销品过期Flag
	if (popParamObj.prmExpiredFlag!=null){
		url = url + "&prmExpiredFlag="+popParamObj.prmExpiredFlag;
	}
	// datatable索引
	if (popParamObj.index==null){
		popParamObj.index = 0;
	}
	
	popDataTable_global.index = popParamObj.index;
	// 单复选类型
	if (popParamObj.checkType!=null){
		url = url + "&checkType="+popParamObj.checkType;
	}else{
		url = url + "&checkType=checkbox";
	}
	// 套装折扣和积分兑礼单复选类型
	if (popParamObj.radioFlag!=null){
		url = url + "&radioFlag="+popParamObj.radioFlag;
	}
	
	return url;
}

function popDataTableModalSet(popParamObj,modal,type){
	if (type=="prmSearchUrl"){
		var dialogId = "promotionDialog";
	}else if (type=="prtSearchUrl"){
		var dialogId = "productDialog";
	}else if (type=="cateSearchUrl"){
		var dialogId = "cateDialog";
	}
	
	if (!modal){
		if (prtPopParam.autoClose!=null){
			// 例外数组,数组之内的控件不会被自动关闭
			var exceptionArr = popParamObj.autoClose;
			var $div = $('#'+dialogId);
			var $body = $("body");
			var firstFlag = true;
			$body.unbind('click');
			// 隐藏弹出的DIV
			$body.bind('click',function(event){
				if (!firstFlag){
					var skipFlag = false;
					var parents = $(event.target).parents();
					for (var i=0;i<exceptionArr.length;i++){
						if ($(event.target).attr('id') == exceptionArr[i]){
							skipFlag = true;
							break;
						}
					}
					
					if (!skipFlag){
						for (var i=0;i<parents.length;i++){
							var parent = parents[i];
							if ($(parent).attr('id') == dialogId){
								skipFlag = true;
								break;
							}
						}
					}

					if (!skipFlag){
						if (dialogBody!=null){
							closeCherryDialog(dialogId,dialogBody);				
							oTableArr[dataTableIndex]= null;
						}else{
							$('#'+dialogId).dialog( "destroy" );
						}
						$body.unbind('click');
					}
				}
				firstFlag = false;			

			});
		}
	}
	
}

function popDataTableOfCheckPaper (thisObj, param, callback){
	if ("" == popDataTable_global.checkPaperDialogHtml) {
		// 临时存放部门信息页面内容
		popDataTable_global.checkPaperDialogHtml = $("#checkPaperDialog").html();
	} else {
		$("#checkPaperDialog").html(popDataTable_global.checkPaperDialogHtml);
	}
	oTableArr[9] = null;
	var csrftoken = $('#csrftoken').serialize();
	if(!csrftoken) {
		csrftoken = $('#csrftoken',window.opener.document).serialize();
	}
	var url = $('#popCheckPaperUrl').text()+'?' + csrftoken + (param?('&'+param):'');
	var tableSetting = {
			 // 一页显示页数
			 iDisplayLength:10,
			 // 表格ID
			 tableId : '#checkPaper_dataTable',
			 // 不自动计算列宽度 
			 bAutoWidth: false,
			 // 数据URL
			 url : url,
			 // 表格列属性设置
			 aoColumns : [  { "sName": "radio","sWidth": "10%","bSortable": false}, 	// 0
							{ "sName": "paperName","sWidth": "45%"},                     // 1
							{ "sName": "paperStatus","sWidth": "45%"}],    // 2
			index : 9,
			colVisFlag: false
	 };
	
	// 调用获取表格函数
	getTable(tableSetting);

	var dialogSetting = {
		bgiframe: true,
		width:600, 
		height:'auto',
		minWidth:600,
		//minHeight:320,
		zIndex: 9999,
		modal: true, 
		title:$("#PoppaperTitle").text(),
		close: function(event, ui) { removeDialog("#checkPaperDialog"); }
	};
//	if(thisObj) {
//		var opos = $(thisObj).offset();
//		var oleft = parseInt(opos.left,10) ;
//		var otop = parseInt(opos.top, 10) + 25 - $(document).scrollTop();
//		dialogSetting.position = [ oleft , otop ];
//	}
	$('#checkPaperDialog').dialog(dialogSetting);
	
	$('#checkPaperConfirm').unbind('click');
	$('#checkPaperConfirm').click(function(){
		if(typeof(callback) == "function") {
			callback();
		}
		removeDialog("#checkPaperDialog");
	});
}
	
function popDataTableOfPaper (thisObj, param, callback){
	if ("" == popDataTable_global.checkPaperDialogHtml) {
	// 临时存放部门信息页面内容
	popDataTable_global.checkPaperDialogHtml = $("#checkPaperDialog").html();
	} else {
		$("#checkPaperDialog").html(popDataTable_global.checkPaperDialogHtml);
	}
	oTableArr[9] = null;
	var csrftoken = $('#csrftoken').serialize();
	if(!csrftoken) {
		csrftoken = $('#csrftoken',window.opener.document).serialize();
	}
	var url = $('#popPaperUrl').text()+'?' + csrftoken + (param?('&'+param):'');
	var tableSetting = {
			 // 一页显示页数
		 iDisplayLength:10,
		 // 表格ID
		 tableId : '#checkPaper_dataTable',
		 // 不自动计算列宽度 
		 bAutoWidth: false,
		 // 数据URL
		 url : url,
		 // 表格列属性设置
		 aoColumns : [  { "sName": "radio","sWidth": "10%","bSortable": false}, 	// 0
						{ "sName": "paperName","sWidth": "45%"},                     // 1
						{ "sName": "paperStatus","sWidth": "45%"}],    // 2
			index : 9,
			colVisFlag: false
	 };
	
	// 调用获取表格函数
	getTable(tableSetting);
	
	var dialogSetting = {
		bgiframe: true,
		width:600, 
		height:'auto',
		minWidth:600,
		//minHeight:320,
		zIndex: 9999,
		modal: true, 
		title:$("#AnswerpaperTitle").text(),
	close: function(event, ui) { removeDialog("#checkPaperDialog"); }
	};
//	if(thisObj) {
//		var opos = $(thisObj).offset();
//		var oleft = parseInt(opos.left,10) ;
//		var otop = parseInt(opos.top, 10) + 25 - $(document).scrollTop();
//		dialogSetting.position = [ oleft , otop ];
//	}
	$('#checkPaperDialog').dialog(dialogSetting);
	
	$('#checkPaperConfirm').unbind('click');
	$('#checkPaperConfirm').click(function(){
	if(typeof(callback) == "function") {
		callback();
	}
	removeDialog("#checkPaperDialog");
	});
}
/**
 * 	URL参数值替换
 *
 * @param url
 * @param paramName 需替换的参数名
 * @param paramVal 替换后的参数值
 * @param addFlg url中不存在此参数时，是否追加此参数
 * @return 替换后的URL
 */
function replaceParamVal(url,paramName,paramVal,addFlg) {
    var nUrl = "";
    var param = paramName+'='+paramVal;
    if(isEmpty(addFlg)){
    	addFlg = false;
    }
    if(addFlg && url.indexOf(paramName)==-1){
        if (url.indexOf('?')==-1) {
            nUrl = url + '?' + param;
        } else {
            nUrl = url + '&' + param;
        }
    } else {
        var re=eval('/('+ paramName+'=)([^&]*)/gi');
        nUrl = url.replace(re,param);
    }
   return nUrl;
}
/**
 * 取得被选中的checkbox中的数据信息，如果能被json解析则返回解析后的数据，否则返回弹出框中被点击的一行信息
 * 
 * @param $input 被点击的checkbox对象
 * 
 * */
function getInputInfo(input,popTableId){
	var obj = {};
	try{
		//如果是部门弹出框
		if("departDataTableInit" == popTableId){
			var $row = $(input).parent().parent();
			var $td = $row.children("td");
			//部门ID
			var departId = $(input).val();
			obj.departId = departId;
			//部门编码
			var departCode = $td.eq(1).find("span").text().escapeHTML();
			obj.departCode = departCode;
			//部门名称
			var departName = $td.eq(2).find("span").text().escapeHTML();
			obj.departName = departName;
			//部门类型
			var departType = $td.eq(3).find("span").text().escapeHTML();
			obj.departType = departType;
		}else{
			obj = window.JSON2.parse($(input).val());
		}
	}catch(e){
		
	}finally{
		return obj;
	}
}

/**
 * 根据弹出框类型取得pop框input记录ID
 * 
 * @param input
 * @param popTableId:弹出框dataTableId
 * @return id
 */
function getPopInputId(input,popTableId){
	var id = "";
	var obj = getInputInfo(input,popTableId);
	if(popTableId == 'prt_dataTable' || popTableId == 'rpt_prt_dataTable'){
		id = obj.productVendorId;
	}else if(popTableId == 'prm_dataTable'){
		id = obj.promotionProductVendorId;
	}else if(popTableId == 'prtCate_Table' || popTableId == 'rpt_prtCate_Table'){
		id = obj.cateValId;
	}else if(popTableId == 'prmCate_Table'){
		id = obj.cateCode;
	}else if(popTableId == 'departDataTableInit'){
		id = obj.departId;
	}else if(popTableId == 'mem_dataTable'){
		id = obj.memberInfoId;
	}else if(popTableId=='objBatch_dataTable'){
		id=obj.searchCode;
	} else if(popTableId == 'prtOne_dataTable' ){
		id = obj.barCode;
	} else if(popTableId == 'prm_dataTableOne'){
		id = obj.barCode;
	}
	return id;
}

/**
 * 取得目标区记录行数据ID
 * 
 * @param tr 目标区记录行
 * @param popTableId:弹出框dataTableId
 * @return id
 */
function getTargetInputId(tr,popTableId){
	var id = "";
	var $tr = $(tr);
	if(popTableId == 'prt_dataTable' || popTableId == 'rpt_prt_dataTable'){
		id = $tr.find(":hidden[name='prtVendorId']").val();
	}else if(popTableId == 'prm_dataTable' || popTableId == 'prm_dataTableOne'){
		id = $tr.find(":hidden[name='prmVendorId']").val();
	}else if(popTableId == 'prtCate_Table' || popTableId == 'rpt_prtCate_Table'){
		id = $tr.find(":hidden[name='cateValId']").val();
	}else if(popTableId == 'prmCate_Table'){
		id = $tr.find(":hidden[name='cateCode']").val();
	}else if(popTableId == 'departDataTableInit'){
		id = $tr.find(":hidden[name='departId']").val();
	}else if(popTableId == 'mem_dataTable'){
		id = $tr.find(":hidden[name='memberIdArr']").val();
	}else if(popTableId == 'prtOne_dataTable'){
		id = $tr.find(":hidden[name='prtBarCode']").val();
	}
	return id;
}
/**
 * 目标区内容加载到缓存区
 * 
 * @param option
 * @param flag
 * @return
 */
function exchangeHtml(option,flag){
	if(!option.bindFlag){
		// 目标区
		var $target = $("#" + option.targetId);
		if(isEmpty(option.targetId)){
			$target = $(option.target);
		}
		// 弹出框目标缓存区
		var $temp = $("#" + option.dialogId + "_temp");
		var flag_1 = 0;
		if(option.freeCount == 1){
			var $maxCount = $("#maxCount");
			var maxCount = $maxCount.val();
			if(maxCount == null || maxCount == "")
				flag_1 = 0;
			else if($temp.children().length> maxCount)
				flag_1 = 1;
		}
		if(flag){
			if(flag_1){
				var $p = $("#send_checkinfo_dialog").find('p.message');
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
					buttons: [
						{	text:"确定",
							click: function() {
								closeCherryDialog("send_checkinfo_dialog");
							}
						}],
					close: function() {
						closeCherryDialog("send_checkinfo_dialog");
					}
				};
				$message.text("单次盘点产品数不能超过"+$maxCount.val()+"行");
				$("#send_checkinfo_dialog").dialog(option);
				$("#send_checkinfo_dialog").dialog("open");
			}else{
				// 缓存区到目标区
				$target.empty();
				$temp.children().clone(true).appendTo($target);
			}
		}else{
			// 备份目标区到缓存区
			$temp.empty();
			if(!isEmpty(option.infoHtml)){
				$temp.append(option.infoHtml);
			}else{
				$target.children().clone(true).appendTo($temp);
			}			
		}
	}
}


/**
 * 全选绑定事件
 * 
 * @param src
 * @param target
 * @param getHtmlFun
 * @param type
 * @return
 */
function checkAllBind(option){
	// 弹出框DATATABLE
	var $popTable = $("#" + option.popTableId);
	// 目标区
	var $target = $("#" + option.targetId);
	if(isEmpty(option.targetId)){
		$target = $(option.target);
	}
	// 弹出框目标缓存区
	var $tableTemp = $("#" + option.dialogId + "_temp");
	var $temp = $tableTemp;
	if(option.bindFlag){
		$temp = $target;
	}
	// 全选CHECKBOX
	var $checkAll = $popTable.find("th").find(":checkbox");
	if($checkAll.length > 0){
		$checkAll.unbind("click");
		$checkAll.click(function(){
			// 取得弹出框内的CHECKBOX对象
			var $popInputs = $popTable.find("tbody").find(":input");
			var $uncheck = $popInputs.not(":checked");
			var $check = $popInputs.filter(":checked");
			if (this.checked) {
				// 全部选中
				$uncheck.prop("checked", true);
				$uncheck.each(function(){
					var info = getInputInfo(this,option.popTableId);
					var html = '';
					if($.isFunction(option.getHtmlFun)){
						html = option.getHtmlFun.call(this,info);
					}else{
						console.error("getHtmlFun is not a function or String!");
						return;
					}
					// 追加
					$temp.append(html);
				});
			} else {
				// 全部不选中
				$check.prop("checked", false);
				$check.each(function(){
					var id = getPopInputId(this, option.popTableId);					
					// 清除去选内容
					$temp.children().each(function() {
						var targetId = getTargetInputId(this,option.popTableId);
						if (id == targetId) {
							$(this).remove();
							return false;
						}
					});
				});
			}
		});
	}
}
/**
 * 初始化全选CHECKBOX
 * 
 * @param option:弹出框区
 * @return
 */
function initCheckAll(option){
	var $table = $("#" + option.popTableId);
	// 初始化全选CHECKBOX
	var $checkAll = $table.find("th").find(":checkbox");
	if($checkAll.length > 0){
		// 未选中的input
		var $unChecks = $table.find("tbody").find(":input").filter(":not(:checked)");
		if($unChecks.length == 0){
			$checkAll.attr("checked", true);
		}else{
			$checkAll.attr("checked", false);
		}
	}
}

function popDataTableOfCateInfo(catePopParam){
	if(catePopParam.index != null){
		oTableArr[catePopParam.index]= null;	
	}else{
		oTableArr[0]= null;
	}
	// 操作对象
	var thisObj = catePopParam.thisObj;
	var url = $("#cateSearchUrl").html();
	url = url + "?"+ catePopParam.csrftoken;
	if(catePopParam.teminalFlag != null){
		url = url + "&teminalFlag="+catePopParam.teminalFlag;
	}else{
		url = url + "&teminalFlag=1";
	}
	if(catePopParam.checkType != null){
		url = url + "&checkType="+catePopParam.checkType;
	}else{
		url = url + "&checkType=checkbox";
	}
	// datatable索引
	if (catePopParam.index==null){
		catePopParam.index = 0;
	}
	
	popDataTable_global.index = catePopParam.index;
	// 弹出后是否锁定底部页面
	if (catePopParam.modal!=null){
		var modal  = catePopParam.modal;
	}else{
		var modal = true;
	}
	
	if (catePopParam.dialogBody!=null){
		var dialogBody  = catePopParam.dialogBody;
	}else{
		var dialogBody = null;
	}
	popDataTableModalSet(modal,catePopParam,"cateSearchUrl");
	var tableSetting = {
			 // 一页显示页数
			 iDisplayLength:5,
			 // 表格ID
			 tableId : '#cate_dataTable',
			 // 数据URL
			 url : url,
			 // 表格列属性设置
			 aoColumns : [  { "sName": "checkbox","sWidth": "5%","bSortable": false}, 	// 0
							{ "sName": "cateCode","sWidth": "10%"},                     // 1
							{ "sName": "cateType","sWidth": "10%"},                     // 1
							{ "sName": "cateName","sWidth": "15%"}],                     // 2
			index:catePopParam.index,
			colVisFlag: true,
			// 横向滚动条出现的临界宽度
			sScrollX : "100%",
			// pop窗口弹出后回调函数
			fnDrawCallback:catePopParam.callback
	 };
	
	// 调用获取表格函数
	getTable(tableSetting);
	var dialogSetting = {modal: modal,width:800, height:345, zIndex: 9999, resizable:false, title:'分类信息', 
			close: function(event, ui) {
		if (dialogBody!=null){
			closeCherryDialog('cateDialog',dialogBody);				
			oTableArr[catePopParam.index]= null;			// 其中数组中的0要根据自己的datatable序号而定，如果页面只存在一个datatable则为0	
		}else{
			$(this).dialog( "destroy" );
		}
		if (!modal && catePopParam.autoClose!=null){
			$("body").unbind('click');
		}
	}};
	if(thisObj){
		var opos = $(thisObj).offset();
		oleft = parseInt(opos.left,10) ;
		otop = parseInt(opos.top, 10) + 25 - $(document).scrollTop();
		dialogSetting.position = [ oleft , otop ];
	}
	$('#cateDialog').dialog(dialogSetting);
}

/**
 * 弹出框初始化：选中已选对象[产品/促销品/产品分类/促销品分类]
 * 
 * @param option:弹出框区
 */
function initPopInput(option){
	// 弹出框DATATABLE
	var $popTable = $("#" + option.popTableId);
	// 目标区
	var $target = $("#" + option.targetId);
	if(isEmpty(option.targetId)){
		$target = $(option.target);
	}
	// 弹出框目标缓存区
	var $tableTemp = $("#" + option.dialogId + "_temp");
	// 取得弹出框内的input对象
	var $popInputs = $popTable.find("tbody").find(":input");
	var $temp = $tableTemp;
	if(option.bindFlag){
		$temp = $target;
	}
	
	$popInputs.each(function(){
		var $input = $(this);
		var id = getPopInputId($input,option.popTableId);
		$temp.children().each(function(){
			var tempId = getTargetInputId(this,option.popTableId);
			if(tempId == id){
				$input.prop("checked",true);
				return false;
			}
		});
	});
	// 初始化全选CHECKBOX
	initCheckAll(option);
}


/**
 * 弹出框input事件绑定
 * 
 * @param option:
 * 
 */
function bindPopInput(option) {
	// 弹出框datatable
	var $popTable = $("#" + option.popTableId);
	// 目标区
	var $target = $("#" + option.targetId);
	if(isEmpty(option.targetId)){
		$target = $(option.target);
	}
	// 弹出框目标缓存区
	var $tableTemp = $("#" + option.dialogId + "_temp");
	var $temp = $tableTemp;
	if(option.bindFlag){
		$temp = $target;
	}
	// 取得弹出框内的CHECKBOX对象
	var $popInputs = $popTable.find("tbody").find(":input");
	$popInputs.unbind("click");
	$popInputs.click(function() {
		var $input = $(this);
		var info = getInputInfo($input,option.popTableId);
		var id = getPopInputId($input, option.popTableId);
		// CHECKBOX
		if($input.is(":checkbox")){
			if ($input.is(":checked")) {
				var html = '';
				if($.isFunction(option.getHtmlFun)){
					html = option.getHtmlFun.call(this,info);
				}else{
//					console.error("getHtmlFun is not a function or String!");
					return;
				}
				$temp.append(html);
			} else {
				// 清除去选内容
				$temp.children().each(function() {
					var targetId = getTargetInputId(this,option.popTableId);
					if (id == targetId) {
						$(this).remove();
						return false;
					}
				});
			}
		}else{
			// RADIO
			var $trTemp = $temp.children();
			var targetId = getTargetInputId($trTemp,option.popTableId);
			if (id == targetId) {
				return;
			}else{
				var html = '';
				if($.isFunction(option.getHtmlFun)){
					html = option.getHtmlFun.call(this,info);
				}else{
//					console.error("getHtmlFun is not a function or String!");
					return;
				}
				$trTemp.remove();
				$temp.append(html);
			}
		}
		// 初始化全选CHECKBOX
		initCheckAll(option);
	});
}
/**
 * 设置Dialog共通属性
 * @param $dialog
 * @param dialogSetting
 * @param option
 * @return
 */
function setDialogSetting($dialog,dialogSetting, option){
	
	// setting dialog max-height
	var height = 420;
	$dialog.css("max-height",height+"px");
	if(isEmpty(dialogSetting.buttons) || dialogSetting.buttons.length == 0){
		dialogSetting.buttons = [{
			text: $("#global_page_ok").text(),
			click: function() { 
				if(!option.bindFlag){
					if($.isFunction(option.exchangeHtml)){
						option.exchangeHtml(option);
					}else{
						exchangeHtml(option,true);
					}
				}
				if($.isFunction(option.click)) {
					option.click();
				}
				$(this).dialog("close");
			}
		}];
	}
	dialogSetting.open= function() {
		$(this).bind("keypress.ui-dialog", function(event) {
			if (event.keyCode == $.ui.keyCode.ENTER) {
				if(!option.bindFlag){
					exchangeHtml(option,true);
				}
				if($.isFunction(option.click)) {
					option.click();
				}
				
				if(options.isPosCloud == 0){
					$(this).dialog("close");
				}
				
			}
		});
	};
	dialogSetting.modal = true;
	dialogSetting.zIndex = 9999;
	dialogSetting.maxHeight = height + 80;
	var $win= $(window);
	// 弹出框位置居中计算
	dialogSetting.position = [($win.outerWidth() -dialogSetting.width)/2,($win.outerHeight() - 440)/2];
	dialogSetting.resizable = true;
	$dialog.dialog(dialogSetting);
}
/**
 * 设置Dialog共通属性
 * @param $dialog
 * @param dialogSetting
 * @param option
 * @return
 */
function setDialogSettingNzdm($dialog,dialogSetting, option){
	
	// setting dialog max-height
	var height = 420;
	$dialog.css("max-height",height+"px");
	if(isEmpty(dialogSetting.buttons) || dialogSetting.buttons.length == 0){
		dialogSetting.buttons = [{
			text: $("#global_page_addOrder").text(),
			click: function() { 
				if(!option.bindFlag){
					if($.isFunction(option.exchangeHtml)){
						option.exchangeHtml(option);
					}else{
						exchangeHtml(option,true);	
					}
				}
				if($.isFunction(option.click)) {
					option.click();
				}
				$(this).dialog("close");
			}
		},
		{
			text: $("#global_page_close").text(),
			click: function() { 
				$(this).dialog("close");
			}
		}];
	}
	dialogSetting.open= function() {
		$(this).bind("keypress.ui-dialog", function(event) {
			if (event.keyCode == $.ui.keyCode.ENTER) {
				if(!option.bindFlag){
					exchangeHtml(option,true);
				}
				if($.isFunction(option.click)) {
					option.click();
				}
				
				if(options.isPosCloud == 0){
					$(this).dialog("close");
				}
				
			}
		});
	};
	dialogSetting.modal = true;
	dialogSetting.zIndex = 9999;
	dialogSetting.maxHeight = height + 80;
	var $win= $(window);
	// 弹出框位置居中计算
	dialogSetting.position = [($win.outerWidth() -dialogSetting.width)/2,($win.outerHeight() - 440)/2];
	dialogSetting.resizable = true;
	$dialog.dialog(dialogSetting);
}
/**
 * 弹出框tab切换
 * 
 * @param thisObj
 * @param index
 * @return
 */
function changeTab(thisObj,index,paramName) {
	if(isEmpty(index)){
		index = 0;
    }
	if(!paramName) {
		paramName = "param";
	}
	var $this = $(thisObj);
	$this.siblings().removeClass('ui-tabs-selected');
	$this.addClass('ui-tabs-selected');
	
	var paramVal = $this.prop("id");
	var oSettings = oTableArr[index].fnSettings();
	var url = oSettings.sAjaxSource;
	url = replaceParamVal(url, paramName, paramVal, true);
	oSettings.sAjaxSource = url;
	// 刷新表格数据
	oTableArr[index].fnDraw();
}

/**
 * 产品分类弹出框
 * 
 * @param option
 * @return
 */
function popPrtCateDialog (option){
	var index = 20;
	var mode = 1;
	option.dialogId = 'prtCategoryDialog';
	option.popTableId = 'prtCate_Table';
	var $dialog = $('#' + option.dialogId);
	if (!isEmpty(option.mode)){mode = option.mode;}
	if(popDataTable_global.prtCateDialogHtml == ""){
		popDataTable_global.prtCateDialogHtml = $dialog.html();
	}
	var dialogBody = popDataTable_global.prtCateDialogHtml;
	var url = $("#prtCateSearchUrl").text();
	url += "?" + getSerializeToken();
	var param = $dialog.find(':radio[name="radio'+index+'"]').filter(":checked").val();
	if(!isEmpty(param)){
		url += "&param="+param;
	}
	if(!isEmpty(option.propId)){
		url += "&param1=" + option.propId;
	} else {
		url += "&param1=" + $dialog.find("li.ui-tabs-selected").attr("id");
	}
	if(!isEmpty(option.cateInfo)){
		url += "&param2=" + option.cateInfo;
	}
	if(!isEmpty(option.checkType)){
		url += "&checkType=" + option.checkType;
	}
	if(!isEmpty(option.teminalFlag)){
		url += "&teminalFlag=" + option.teminalFlag;
	}
	// 目标区内容加载到缓存区
	exchangeHtml(option);
	var tableSetting = {
			 // 表格ID
			 tableId : '#' + option.popTableId,
			 // 一页显示页数
			 iDisplayLength:10,
			 // 数据URL
			 url : url,
			 // 表格列属性设置
			 aoColumns : [  { "sName": "checkbox","bSortable": false}, 	// 0
							{ "sName": "cateVal"},                      // 1
							{ "sName": "cateValName"}],                 // 2
			index:index,
			colVisFlag: false,
			// 横向滚动条出现的临界宽度
			//sScrollX : "100%",
			// pop窗口弹出后回调函数
			fnDrawCallback:function(){
					initPopInput(option);
					bindPopInput(option);
			}
	 };
	// 调用获取表格函数
	getTable(tableSetting);
	var dialogSetting = {
			width:'600', 
			title:$("#PopPrtCateTitle").text(), 
			close: function(event, ui) {
				if (mode==2){
					closeCherryDialog(option.dialogId,dialogBody);				
					oTableArr[index]= null;
				}else{
					$(this).dialog( "destroy" );
				}
			}
	};
	// 设置Dialog共通属性
	setDialogSetting($dialog,dialogSetting,option);
	// 弹出框全选CHECKBOX绑定事件
	checkAllBind(option);
}
/**
 * 促销品分类弹出框
 * 
 * @param option
 * @return
 */
function popPrmCateDialog (option){
	var index = 21;
	var mode = 1;
	option.dialogId = 'prmCategoryDialog';
	option.popTableId = 'prmCate_Table';
	var $dialog = $('#' + option.dialogId);
	if (!isEmpty(option.mode)){mode = option.mode;}
	if(popDataTable_global.prmCateDialogHtml == ""){
		popDataTable_global.prmCateDialogHtml = $dialog.html();
	}
	var dialogBody = popDataTable_global.prmCateDialogHtml;
	$('#prmCate_Title').find('li').removeClass('ui-tabs-selected');
	$('#prmCate_Title').find('li').first().addClass('ui-tabs-selected');
	
	
	var url = $("#prmCateSearchUrl").text();
	url += "?" + getSerializeToken();
	if(!isEmpty(option.checkType)){
		url += "&checkType="+option.checkType;
	}
	if(!isEmpty(option.brandInfoId)){
		url += "&brandInfoId=" + option.brandInfoId;
	}
	if(!isEmpty(option.param)){
		url += "&param="+ option.param;
	}else{
		var param = $('#prmCate_Title').find('li.ui-tabs-selected').attr("id");
		url += "&param="+ param;
	}
	// 目标区内容加载到缓存区
	exchangeHtml(option);
	var tableSetting = {
			 // 表格ID
			 tableId : '#' + option.popTableId,
			 // 一页显示页数
			 iDisplayLength:10,
			 // 数据URL
			 url : url,
			 // 表格列属性设置
			 aoColumns : [  { "sName": "checkbox","bSortable": false}, 	// 0
							{ "sName": "cateCode"},                      // 1
							{ "sName": "cateName"}],                 // 2
			index:index,
			colVisFlag: false,
			// 横向滚动条出现的临界宽度
			//sScrollX : "100%",
			// pop窗口弹出后回调函数
			fnDrawCallback:function(){
					initPopInput(option);
					bindPopInput(option);
			}
	 };
	// 调用获取表格函数
	getTable(tableSetting);
	var dialogSetting = {
			width:'600', 
			title:$("#PopPrmCateTitle").text(), 
			close: function(event, ui) {
				if (mode==2){
					closeCherryDialog(option.dialogId,dialogBody);				
					oTableArr[index]= null;
				}else{
					$(this).dialog( "destroy" );
				}
			}
	};
	// 设置Dialog共通属性
	setDialogSetting($dialog,dialogSetting,option);
	// 弹出框全选CHECKBOX绑定事件
	checkAllBind(option);
}
/**
 * 产品信息弹出框
 * 
 * @param option
 * 				String:options.popValidFlag 有效区分,参数可选(0[无效]、1[有效]、2[全部])，默认为1
 * 				Array:options.ignorePrtPrmVendorID 剔除产品，参数可选，默认为空
 * 				String options.ignoreSoluId 产品方案ID，剔除产品方案中的产品，参数可选，默认为空
 * 				String options.isPosCloud 是否小店云模式（小店云显示barcode），参数可选(0[否]、1[是])，默认为否
 * 				String options.ignorePrtFunId 产品功能开启时间主表ID，剔除产品功能开启时间明细表中的产品，参数可选，默认为空。
 * 				String options.showOrderPrice 是否显示采购价（目前思乐得的大仓入库有用），参数可选（0：否；1：是），默认为否
 * 注意：ignoreSoluId与ignorePrtFunId只能二选一，不能同时使用

 * @return
 */
function popProductDialog(option){
	var index = 22;
	var mode = 1;
	option.dialogId = 'productDialog';
	option.popTableId = 'prt_dataTable';
	var $dialog = $("#" + option.dialogId);
	if (!isEmpty(option.mode)){
		mode = option.mode;
	}
	if(popDataTable_global.prtDialogHtml == ""){
		popDataTable_global.prtDialogHtml = $dialog.html();
	}
	var dialogBody = popDataTable_global.prtDialogHtml;
	// 设置URL
	var url = $('#prtSearchUrl').html();
	url += "?" + getSerializeToken();
	if(!isEmpty(option.checkType)){
		url += "&checkType=" + option.checkType;
	}
	if(!isEmpty(option.brandInfoId)){
		url += "&brandInfoId=" + option.brandInfoId;
	}
	if(!isEmpty(option.isBOM)){
		url += "&isBOM=" + option.isBOM;
	}
	if(!isEmpty(option.isExchanged)){
		url += "&isExchanged=" + option.isExchanged;
	}
	// 有效区分
	if(!isEmpty(option.popValidFlag)){
		url += "&popValidFlag=" + option.popValidFlag;
	} else {
		url += "&popValidFlag=1";
	} 
	// 需要剔除的产品
	if(!isEmpty(option.ignorePrtPrmVendorID)){
		url += "&ignorePrtPrmVendorID=" + option.ignorePrtPrmVendorID;
	}
	// 需要剔除产品方案中的产品
	if(!isEmpty(option.ignoreSoluId)){
		url += "&ignoreSoluId=" + option.ignoreSoluId;
	}

	// 需要剔除产品功能开启时间中的产品
	if(!isEmpty(option.ignorePrtFunId)){
		url += "&ignorePrtFunId=" + option.ignorePrtFunId;
	}
	// 是否小店云模式
	if(!isEmpty(option.ignoreSoluId)){
		url += "&isPosCloud=" + option.isPosCloud;
	}
	// 是否显示产品采购价格
	if(!isEmpty(option.showOrderPrice)) {
		url += "&showOrderPrice=" + option.showOrderPrice;
	}
	// 产品品牌ID（智能促销时使用）
	if(!isEmpty(option.originalBrand)) {
		url += "&param=" + option.originalBrand;
	}
	// 目标区内容加载到缓存区
	exchangeHtml(option);
	var tableSetting = {
			 // 表格ID
			 tableId : '#' + option.popTableId,
			 // 一页显示页数
			 iDisplayLength:10,
			 // 数据URL
			 url : url,
			 // 表格列属性设置
			 aoColumns : [  { "sName": "checkbox","bSortable": false}, 	// 0
							{ "sName": "unitCode"},                     // 1
							{ "sName": "barCode"},                      // 2
							{ "sName": "originalBrand"},                // 3
							{ "sName": "nameTotal"},                    // 4
							{ "sName": "primaryCategoryBig"},           // 5
				 			{ "sName": "primaryCategoryMedium"},         // 6
							{ "sName": "primaryCategorySmall"},         // 6
							{ "sName": "salePrice"},					// 7
							{ "sName": "memPrice"},						// 8
							{ "sName": "standardCost"},					// 9
							{ "sName": "orderPrice"},					// 10
							
							{ "sName": "platinumPrice","bVisible" : false},					// 11
							{ "sName": "tagPrice","bVisible" : false},	 					// 12
							{ "sName": "validFlag" ,"bVisible" : false}
							],                
			index:index,
			colVisFlag: false,
			// 横向滚动条出现的临界宽度
			//sScrollX : "100%",
			// pop窗口弹出后回调函数
			fnDrawCallback:function(){
					initPopInput(option);
					bindPopInput(option);
			}

	 };
	// 剔除产品方案中的产品时，隐藏barcode列

	if(!isEmpty(option.ignoreSoluId) || !isEmpty(option.ignorePrtFunId)){
		tableSetting.aoColumns.splice(2,1,{ "sName": "barCode","bVisible" : false});
	}
	if(!isEmpty(option.ignoreSoluId)){
		if(option.isPosCloud ==0){
			tableSetting.aoColumns.splice(2,1,{ "sName": "barCode","bVisible" : false}); // 隐藏barcode
		}else if(option.isPosCloud==1){
			tableSetting.aoColumns[9]= { "sName": "standardCost","bVisible" : false};
		}
	}
	// 
	if(!isEmpty(option.showOrderPrice)) {
		if(option.showOrderPrice == 0){
			tableSetting.aoColumns.splice(10,1,{ "sName": "orderPrice","bVisible" : false}); // 隐藏orderPrice
		}
	} else {
		tableSetting.aoColumns.splice(10,1,{ "sName": "orderPrice","bVisible" : false}); // 隐藏orderPrice
	}
	
	if(!isEmpty(option.optionalValidFlag)){
		tableSetting.aoColumns.splice(13,1,{ "sName": "validFlag" }); // 
	}
	
	
	
	// 调用获取表格函数
	getTable(tableSetting);
	
	var dialogSetting = {
			width:'980',
			title:$("#PopProTitle").text(), 
			close: function(event, ui) {
				if (mode==2){
					closeCherryDialog(option.dialogId,dialogBody);				
					oTableArr[index]= null;
				}else{
					$(this).dialog( "destroy" );
				}
			}
	};
	// 设置Dialog共通属性
	setDialogSetting($dialog,dialogSetting,option);
	// 弹出框全选CHECKBOX绑定事件
	checkAllBind(option);
	var comboTree;
	//在这里请求数据就可以了
	$.ajax({
		url: "/Cherry/common/BINOLCM02_initTreeCategory?"+getSerializeToken(),
		type: "post",
		dataType: "json",
		success: function (data) {
			//sourceData = eval("(" + data + ")");
			comboTree = $('#justAnInputBox').comboTree({
				source: data,
				isMultiple: true
			});
		},
		error: function (data) {
			comboTree = $('#justAnInputBox').comboTree({
				isMultiple: true
			});
		}
	});



}

/**
 * 产品信息弹出框(薇诺娜，无厂商编码，同一产品条码以最新的记录为准)
 * 
 * @param option
 * 				String:options.popValidFlag 有效区分,参数可选(0[无效]、1[有效]、2[全部])，默认为1
 * 				Array:options.ignorePrtPrmVendorID 剔除产品，参数可选，默认为空
 * 				String options.ignoreSoluId 产品方案ID，剔除产品方案中的产品，参数可选，默认为空
 * 				String options.isPosCloud 是否小店云模式（小店云显示barcode），参数可选(0[否]、1[是])，默认为否
 * 				String options.ignorePrtFunId 产品功能开启时间主表ID，剔除产品功能开启时间明细表中的产品，参数可选，默认为空。
 * 				String options.showOrderPrice 是否显示采购价（目前思乐得的大仓入库有用），参数可选（0：否；1：是），默认为否
 * 注意：ignoreSoluId与ignorePrtFunId只能二选一，不能同时使用

 * @return
 */
function popProductDialogOne(option){
	var index = 220;
	var mode = 1;
	option.dialogId = 'productDialogOne';
	option.popTableId = 'prtOne_dataTable';
	var $dialog = $("#" + option.dialogId);
	if (!isEmpty(option.mode)){
		mode = option.mode;
	}
	if(popDataTable_global.prtDialogOneHtml == ""){
		popDataTable_global.prtDialogOneHtml = $dialog.html();
	}
	var dialogBody = popDataTable_global.prtDialogOneHtml;
	// 设置URL
	var url = $('#prtSearchOneUrl').html();
	url += "?" + getSerializeToken();
	if(!isEmpty(option.checkType)){
		url += "&checkType=" + option.checkType;
	}
	if(!isEmpty(option.brandInfoId)){
		url += "&brandInfoId=" + option.brandInfoId;
	}
	if(!isEmpty(option.isBOM)){
		url += "&isBOM=" + option.isBOM;
	}
	if(!isEmpty(option.isExchanged)){
		url += "&isExchanged=" + option.isExchanged;
	}
	// 有效区分
	if(!isEmpty(option.popValidFlag)){
		url += "&popValidFlag=" + option.popValidFlag;
	} else {
		url += "&popValidFlag=1";
	} 
	// 需要剔除的产品
	if(!isEmpty(option.ignorePrtPrmVendorID)){
		url += "&ignorePrtPrmVendorID=" + option.ignorePrtPrmVendorID;
	}
	// 需要剔除产品方案中的产品
	if(!isEmpty(option.ignoreSoluId)){
		url += "&ignoreSoluId=" + option.ignoreSoluId;
	}
	
	// 需要剔除产品功能开启时间中的产品
	if(!isEmpty(option.ignorePrtFunId)){
		url += "&ignorePrtFunId=" + option.ignorePrtFunId;
	}
	// 是否小店云模式
	if(!isEmpty(option.ignoreSoluId)){
		url += "&isPosCloud=" + option.isPosCloud;
	}
	// 是否显示产品采购价格
	if(!isEmpty(option.showOrderPrice)) {
		url += "&showOrderPrice=" + option.showOrderPrice;
	}
	// 产品品牌ID（智能促销时使用）
	if(!isEmpty(option.originalBrand)) {
		url += "&param=" + option.originalBrand;
	}
	// 目标区内容加载到缓存区
	exchangeHtml(option);
	var tableSetting = {
			// 表格ID
			tableId : '#' + option.popTableId,
			// 一页显示页数
			iDisplayLength:10,
			// 数据URL
			url : url,
			// 表格列属性设置
			aoColumns : [  { "sName": "checkbox","bSortable": false}, 	// 0
			               { "sName": "barCode"},                      // 2
			               { "sName": "originalBrand"},                // 3
			               { "sName": "nameTotal"},                    // 4
			               { "sName": "primaryCategoryBig"},           // 5
			               { "sName": "primaryCategorySmall"},         // 6
			               { "sName": "salePrice"},					// 7
			               { "sName": "memPrice"},						// 8
			               { "sName": "standardCost"},					// 9
			               { "sName": "orderPrice"},					// 10
			               
			               { "sName": "platinumPrice","bVisible" : false},					// 11
			               { "sName": "tagPrice","bVisible" : false},	 					// 12
			               { "sName": "validFlag" ,"bVisible" : false}
			               ],                
			               index:index,
			               colVisFlag: false,
			               // 横向滚动条出现的临界宽度
			               //sScrollX : "100%",
			               // pop窗口弹出后回调函数
			               fnDrawCallback:function(){
			            	   initPopInput(option);
			            	   bindPopInput(option);
			               }
	};
	// 剔除产品方案中的产品时，隐藏barcode列
	
	if(!isEmpty(option.ignoreSoluId) || !isEmpty(option.ignorePrtFunId)){
		tableSetting.aoColumns.splice(1,1,{ "sName": "barCode","bVisible" : false});
	}
	if(!isEmpty(option.ignoreSoluId)){
		if(option.isPosCloud ==0){
			tableSetting.aoColumns.splice(1,1,{ "sName": "barCode","bVisible" : false}); // 隐藏barcode
		}else if(option.isPosCloud==1){
			tableSetting.aoColumns[9]= { "sName": "standardCost","bVisible" : false};
		}
	}
	// 
	if(!isEmpty(option.showOrderPrice)) {
		if(option.showOrderPrice == 0){
			tableSetting.aoColumns.splice(9,1,{ "sName": "orderPrice","bVisible" : false}); // 隐藏orderPrice
		}
	} else {
		tableSetting.aoColumns.splice(9,1,{ "sName": "orderPrice","bVisible" : false}); // 隐藏orderPrice
	}
	
	/*if(!isEmpty(option.optionalValidFlag)){
		tableSetting.aoColumns.splice(12,1,{ "sName": "validFlag" }); // 
	}*/
		
	// 调用获取表格函数
	getTable(tableSetting);
	
	var dialogSetting = {
			width:'980',
			title:$("#PopProTitle").text(), 
			close: function(event, ui) {
				if (mode==2){
					closeCherryDialog(option.dialogId,dialogBody);				
					oTableArr[index]= null;
				}else{
					$(this).dialog( "destroy" );
				}
			}
	};
	// 设置Dialog共通属性
	setDialogSetting($dialog,dialogSetting,option);
	// 弹出框全选CHECKBOX绑定事件
	checkAllBind(option);
}

/**
 * 产品信息弹出框(浓妆淡抹订货商城专用)
 * 
 * @param option
 * 				String:options.popValidFlag 有效区分,参数可选(0[无效]、1[有效]、2[全部])，默认为1
 * 				Array:options.ignorePrtPrmVendorID 剔除产品，参数可选，默认为空
 * 				String options.ignoreSoluId 产品方案ID，剔除产品方案中的产品，参数可选，默认为空
 * 				String options.isPosCloud 是否小店云模式（小店云显示barcode），参数可选(0[否]、1[是])，默认为否
 * 				String options.ignorePrtFunId 产品功能开启时间主表ID，剔除产品功能开启时间明细表中的产品，参数可选，默认为空。
 * 				String options.showOrderPrice 是否显示采购价（目前思乐得的大仓入库有用），参数可选（0：否；1：是），默认为否
 * 注意：ignoreSoluId与ignorePrtFunId只能二选一，不能同时使用

 * @return
 */
function popProductDialogTwo(option){
	var index = 25;
	var mode = 1;
	option.dialogId = 'productDialog';
	option.popTableId = 'prt_dataTable';
	var $dialog = $("#" + option.dialogId);
	if (!isEmpty(option.mode)){
		mode = option.mode;
	}
	if(popDataTable_global.prtDialogHtml == ""){
		popDataTable_global.prtDialogHtml = $dialog.html();
	}
	var dialogBody = popDataTable_global.prtDialogHtml;
	// 设置URL
	var url = $('#prtSearchUrl').html();
	url += "?" + getSerializeToken();
	if(!isEmpty(option.checkType)){
		url += "&checkType=" + option.checkType;
	}
	if(!isEmpty(option.brandInfoId)){
		url += "&brandInfoId=" + option.brandInfoId;
	}
	if(!isEmpty(option.isBOM)){
		url += "&isBOM=" + option.isBOM;
	}
	if(!isEmpty(option.isExchanged)){
		url += "&isExchanged=" + option.isExchanged;
	}
	// 有效区分
	if(!isEmpty(option.popValidFlag)){
		url += "&popValidFlag=" + option.popValidFlag;
	} else {
		url += "&popValidFlag=1";
	} 
	// 需要剔除的产品
	if(!isEmpty(option.ignorePrtPrmVendorID)){
		url += "&ignorePrtPrmVendorID=" + option.ignorePrtPrmVendorID;
	}
	// 需要剔除产品方案中的产品
	if(!isEmpty(option.ignoreSoluId)){
		url += "&ignoreSoluId=" + option.ignoreSoluId;
	}

	// 需要剔除产品功能开启时间中的产品
	if(!isEmpty(option.ignorePrtFunId)){
		url += "&ignorePrtFunId=" + option.ignorePrtFunId;
	}
	// 是否小店云模式
	if(!isEmpty(option.ignoreSoluId)){
		url += "&isPosCloud=" + option.isPosCloud;
	}
	// 是否显示产品采购价格
	if(!isEmpty(option.showOrderPrice)) {
		url += "&showOrderPrice=" + option.showOrderPrice;
	}
	// 产品品牌ID（智能促销时使用）
	if(!isEmpty(option.originalBrand)) {
		url += "&param=" + option.originalBrand;
	}
	// 目标区内容加载到缓存区
	exchangeHtml(option);
	var tableSetting = {
			 // 表格ID
			 tableId : '#' + option.popTableId,
			 // 一页显示页数
			 iDisplayLength:10,
			 // 数据URL
			 url : url,
			 // 表格列属性设置
			 aoColumns : [  { "sName": "checkbox","bSortable": false}, 	// 0
							{ "sName": "unitCode"},                     // 1
							{ "sName": "barCode"},                      // 2
							{ "sName": "originalBrand"},                // 3
							{ "sName": "nameTotal"}, 
							{ "sName": "salePrice","bSortable": false}, // 4
							{ "sName": "primaryCategoryBig"},           // 5
							{ "sName": "primaryCategorySmall"},         // 6
							{ "sName": "distributionPrice"}],  				// 7
/*							{ "sName": "memPrice"},						// 8
							{ "sName": "standardCost"},					// 9
							{ "sName": "orderPrice"},					// 10
							
							{ "sName": "platinumPrice","bVisible" : false},					// 11
							{ "sName": "tagPrice","bVisible" : false},	 					// 12
							{ "sName": "validFlag" ,"bVisible" : false}*/
							              
			index:index,
			colVisFlag: false,
			// 横向滚动条出现的临界宽度
			//sScrollX : "100%",
			// pop窗口弹出后回调函数
			fnDrawCallback:function(){
					initPopInput(option);
					bindPopInput(option);
			}
	 };
	// 剔除产品方案中的产品时，隐藏barcode列

	if(!isEmpty(option.ignoreSoluId) || !isEmpty(option.ignorePrtFunId)){
		tableSetting.aoColumns.splice(2,1,{ "sName": "barCode","bVisible" : false});
	}
	if(!isEmpty(option.ignoreSoluId)){
		if(option.isPosCloud ==0){
			tableSetting.aoColumns.splice(2,1,{ "sName": "barCode","bVisible" : false}); // 隐藏barcode
		}else if(option.isPosCloud==1){
			tableSetting.aoColumns[9]= { "sName": "standardCost","bVisible" : false};
		}
	}
//	// 
//	if(!isEmpty(option.showOrderPrice)) {
//		if(option.showOrderPrice == 0){
//			tableSetting.aoColumns.splice(10,1,{ "sName": "orderPrice","bVisible" : false}); // 隐藏orderPrice
//		}
//	} else {
//		tableSetting.aoColumns.splice(10,1,{ "sName": "orderPrice","bVisible" : false}); // 隐藏orderPrice
//	}
//	
//	if(!isEmpty(option.optionalValidFlag)){
//		tableSetting.aoColumns.splice(13,1,{ "sName": "validFlag" }); // 
//	}
//	
//	
	
	// 调用获取表格函数
	getTable(tableSetting);
	
	var dialogSetting = {
			width:'980',
			title:$("#PopProTitle").text(), 
			close: function(event, ui) {
				if (mode==2){
					closeCherryDialog(option.dialogId,dialogBody);				
					oTableArr[index]= null;
				}else{
					$(this).dialog( "destroy" );
				}
			}
	};
	// 设置Dialog共通属性
	setDialogSettingNzdm($dialog,dialogSetting,option);
	// 弹出框全选CHECKBOX绑定事件
	checkAllBind(option);
}



/**
 * 促销品信息弹出框
 * 
 * @param option
 * 				String:options.popValidFlag 有效区分,参数可选(0[无效]、1[有效]、2[全部])，默认为1
 * 				Array:options.ignorePrtPrmVendorID 剔除促销品，参数可选，默认为空
 * @return
 */
function popPromotionDialog(option){
	var index = 23;
	var mode = 1;
	option.dialogId = 'promotionDialog';
	option.popTableId = 'prm_dataTable';
	var $dialog = $("#" + option.dialogId);
	if (!isEmpty(option.mode)){
		mode = option.mode;
	}
	if(popDataTable_global.prmDialogHtml == ""){
		popDataTable_global.prmDialogHtml = $dialog.html();
	}
	var dialogBody = popDataTable_global.prmDialogHtml;
	var $prmCate = $('#promotionCateTitle').find('li.ui-tabs-selected');
	// 设置URL
	var url = $('#prmSearchUrl').html();
	url += "?" + getSerializeToken();
	if(!isEmpty(option.checkType)){
		url += "&checkType=" + option.checkType;
	}
	if(!isEmpty(option.brandInfoId)){
		url += "&brandInfoId=" + option.brandInfoId;
	}
	if(!isEmpty(option.isStock)){
		url += "&isStock=" + option.isStock;
	}
	if(!isEmpty(option.prmCate)){
		url += "&param=" + option.prmCate;
	}else if($prmCate.length > 0){
		url += "&param=" + $prmCate.attr("id");
	}
	if(!isEmpty(option.isExchanged)){
		url += "&isExchanged=" + option.isExchanged;
	}
	if(!isEmpty(option.popValidFlag)){
		url += "&popValidFlag=" + option.popValidFlag;
	} else {
		url += "&popValidFlag=1";
	}	
	// 需要剔除的促销品
	if(!isEmpty(option.ignorePrtPrmVendorID)){
		url += "&ignorePrtPrmVendorID=" + option.ignorePrtPrmVendorID;
	}
	// 目标区内容加载到缓存区
	exchangeHtml(option);
	var tableSetting = {
			 // 一页显示页数
			 iDisplayLength:10,
			 // 表格ID
			 tableId : '#'+option.popTableId,
			 // 数据URL
			 url : url,
			 // 表格列属性设置
			 aoColumns : [  { "sName": "checkbox","bSortable": false}, 	// 0
							{ "sName": "unitCode"},                     // 1
							{ "sName": "barCode"},                      // 2
							{ "sName": "nameTotal"},                    // 3
							{ "sName": "primaryCategory"},              // 4
							{ "sName": "secondryCategory"},             // 5
							{ "sName": "smallCategory"},                // 6
							{ "sName": "standardCost"}],                // 7
			index:index,
			colVisFlag: false,
			// 横向滚动条出现的临界宽度
			//sScrollX : "100%",
			// pop窗口弹出后回调函数
			fnDrawCallback:function(){
				// 弹出框初始化
				initPopInput(option);
				// 弹出框input绑定事件
				bindPopInput(option);
			}
	 };
	// 调用获取表格函数
	getTable(tableSetting);

	var dialogSetting = {
			width:860,
			title:$("#PopPrmTitle").text(), 
			close: function(event, ui) {
				if (mode==2){
					closeCherryDialog(option.dialogId,dialogBody);				
					oTableArr[index]= null;
				}else{
					$(this).dialog( "destroy" );
				}
			}
	};
	// 设置Dialog共通属性
	setDialogSetting($dialog,dialogSetting,option);
	// 弹出框全选CHECKBOX绑定事件
	checkAllBind(option);
}
/**
 * 促销品信息弹出框(薇诺娜，合并)
 * 
 * @param option
 * 				String:options.popValidFlag 有效区分,参数可选(0[无效]、1[有效]、2[全部])，默认为1
 * 				Array:options.ignorePrtPrmVendorID 剔除促销品，参数可选，默认为空
 * @return
 */
function popPromotionDialogOne(option){
	var index = 230;
	var mode = 1;
	option.dialogId = 'promotionDialogOne';
	option.popTableId = 'prm_dataTableOne';
	var $dialog = $("#" + option.dialogId);
	if (!isEmpty(option.mode)){
		mode = option.mode;
	}
	if(popDataTable_global.prmDialogHtml == ""){
		popDataTable_global.prmDialogHtml = $dialog.html();
	}
	var dialogBody = popDataTable_global.prmDialogHtml;
	var $prmCate = $('#promotionCateTitleOne').find('li.ui-tabs-selected');
	// 设置URL
	var url = $('#prmSearchOneUrl').html();
	url += "?" + getSerializeToken();
	if(!isEmpty(option.checkType)){
		url += "&checkType=" + option.checkType;
	}
	if(!isEmpty(option.brandInfoId)){
		url += "&brandInfoId=" + option.brandInfoId;
	}
	if(!isEmpty(option.isStock)){
		url += "&isStock=" + option.isStock;
	}
	if(!isEmpty(option.prmCate)){
		url += "&param=" + option.prmCate;
	}else if($prmCate.length > 0){
		url += "&param=" + $prmCate.attr("id");
	}
	if(!isEmpty(option.isExchanged)){
		url += "&isExchanged=" + option.isExchanged;
	}
	if(!isEmpty(option.popValidFlag)){
		url += "&popValidFlag=" + option.popValidFlag;
	} else {
		url += "&popValidFlag=1";
	}	
	// 需要剔除的促销品
	if(!isEmpty(option.ignorePrtPrmVendorID)){
		url += "&ignorePrtPrmVendorID=" + option.ignorePrtPrmVendorID;
	}
	// 目标区内容加载到缓存区
	exchangeHtml(option);
	var tableSetting = {
			// 一页显示页数
			iDisplayLength:10,
			// 表格ID
			tableId : '#'+option.popTableId,
			// 数据URL
			url : url,
			// 表格列属性设置
			aoColumns : [  { "sName": "checkbox","bSortable": false}, 	// 0
			               { "sName": "barCode"},                      // 2
			               { "sName": "nameTotal"},                    // 3
			               { "sName": "primaryCategory"},              // 4
			               { "sName": "secondryCategory"},             // 5
			               { "sName": "smallCategory"},                // 6
			               { "sName": "standardCost"}],                // 7
			               index:index,
			               colVisFlag: false,
			               // 横向滚动条出现的临界宽度
			               //sScrollX : "100%",
			               // pop窗口弹出后回调函数
			               fnDrawCallback:function(){
			            	   // 弹出框初始化
			            	   initPopInput(option);
			            	   // 弹出框input绑定事件
			            	   bindPopInput(option);
			               }
	};
	// 调用获取表格函数
	getTable(tableSetting);
	
	var dialogSetting = {
			width:860,
			title:$("#PopPrmTitle").text(), 
			close: function(event, ui) {
				if (mode==2){
					closeCherryDialog(option.dialogId,dialogBody);				
					oTableArr[index]= null;
				}else{
					$(this).dialog( "destroy" );
				}
			}
	};
	// 设置Dialog共通属性
	setDialogSetting($dialog,dialogSetting,option);
	// 弹出框全选CHECKBOX绑定事件
	checkAllBind(option);
}
/**
 * 会员信息弹出框
 * 
 * @param option
 * @return
 */
function popMemberDialog(option){
	var index = 33;
	var mode = 1;
	option.dialogId = 'memberDialog';
	option.popTableId = 'mem_dataTable';
	var $dialog = $("#" + option.dialogId);
	if (!isEmpty(option.mode)){
		mode = option.mode;
	}
	if(popDataTable_global.memDialogHtml == ""){
		popDataTable_global.memDialogHtml = $dialog.html();
	}
	var dialogBody = popDataTable_global.memDialogHtml;
	// 设置URL
	var url = $('#memSearchUrl').html();
	url += "?" + getSerializeToken();
	if(!isEmpty(option.checkType)){
		url += "&checkType=" + option.checkType;
	}
	if(!isEmpty(option.brandInfoId)){
		url += "&brandInfoId=" + option.brandInfoId;
	}
	// 目标区内容加载到缓存区
	exchangeHtml(option);
	var tableSetting = {
			 // 表格ID
			 tableId : '#' + option.popTableId,
			 // 一页显示页数
			 iDisplayLength:10,
			 // 数据URL
			 url : url,
			 // 表格列属性设置
			 aoColumns : [  { "sName": "checkbox","bSortable": false}, 	// 0
							{ "sName": "memCode"},                     // 1
							{ "sName": "memName"},                      // 2
							{ "sName": "mobilePhone"},                    // 3
							{ "sName": "departName"}],          // 4
			index:index,
			colVisFlag: false,
			// 横向滚动条出现的临界宽度
			//sScrollX : "100%",
			// pop窗口弹出后回调函数
			fnDrawCallback:function(){
					initPopInput(option);
					bindPopInput(option);
			}
	 };
	// 调用获取表格函数
	getTable(tableSetting);
	
	var dialogSetting = {
			width:'650',
			title:$("#PopMemTitle").text(), 
			close: function(event, ui) {
				if (mode==2){
					closeCherryDialog(option.dialogId,dialogBody);				
					oTableArr[index]= null;
				}else{
					$(this).dialog( "destroy" );
				}
			}
	};
	// 设置Dialog共通属性
	setDialogSetting($dialog,dialogSetting,option);
	// 弹出框全选CHECKBOX绑定事件
	checkAllBind(option);
}
/**
 * 主活动信息弹出框
 * 
 * @param option
 * @return
 */
function actGrpDialog(option){
	 var index = 24;
		var mode = 1;      
		option.dialogId = 'actGrpDialog';
		option.popTableId = 'actGrpTable';
		
		
	var $dialog = $("#" + option.dialogId);
	if (!isEmpty(option.mode)){
		mode = option.mode;
	}
	if(isEmpty(popDataTable_global.actGropDialogHtml)){
		popDataTable_global.actGropDialogHtml = $dialog.html();
	}
	var dialogBody = popDataTable_global.actGropDialogHtml;
	// 设置URL
	var url = $('#actGropSearchUrl').html();
	url += "?" + getSerializeToken();
	if(!isEmpty(option.checkType)){
		url += "&checkType=" + option.checkType;
	}
	if(!isEmpty(option.brandInfoId)){
		url += "&brandInfoId=" + option.brandInfoId;
	}
	var tableSetting = {
			 // 表格ID
			 tableId : '#' + option.popTableId,
			 // 一页显示页数
			 iDisplayLength:10,
			 // 数据URL
			 url : url,
			// 表格列属性设置
			 aoColumns : [  { "sName": "checkbox","sWidth":"2%","bSortable": false}, 	// 0
							{ "sName": "GroupCode","sWidth":"15%"},                     // 1
							{ "sName": "GroupName","sWidth":"15%"},                      // 2 
							{ "sName": "ActivityType","sWidth":"10%"},
							{ "sName": "ActivityBeginDate","sWidth":"15%"},                      // 2 
							{ "sName": "ActivityEndDate","sWidth":"15%"},                      // 2 
							{ "sName": "Operator","sWidth":"10%","bSortable": false}],

			index:index,
			colVisFlag: false,
			// 横向滚动条出现的临界宽度
//			sScrollX : "100%",
			// pop窗口弹出后回调函数
			fnDrawCallback:function(){
				// 取得弹出框内的CHECKBOX对象
				var $popInputs = $('#' + option.popTableId).find("tbody").find(":input");
				var $popInput = $('#' + option.popTableId).find("thead").find(":input");
				$popInput.attr("checked", false);
				$popInputs.unbind("click");
				$popInputs.click(function() {
					// 初始化全选CHECKBOX
					initCheckAll(option);
				});
			}
	 };
	// 调用获取表格函数
	getTable(tableSetting);
	var dialogSetting = {
			width:'800',
			title:$("#global_page_actTitle").text(), 
			close: function(event, ui) {
				if (mode==2){
					closeCherryDialog(option.dialogId,dialogBody);				
					oTableArr[index]= null;
				}else{
					$(this).dialog( "destroy" );
				}
			},
			buttons:[
			         {
			        	 text: $("#global_page_delete").text(),
			        	 click: function() { 
			        		 	if($.isFunction(option.click)) {
			        		 		if(!option.click()){
			        		 			$(this).dialog("close");
			        		 		}
			 					}			 					
			        	 }
			         },
			         {
			        	 text: $("#global_page_cancle").text(),
			        	 click: function() {
			        		 $(this).dialog("close");
			        		 $('#errorDiv').hide();
			        	 }}]
	};
	
	// 设置Dialog共通属性
	setDialogSetting($dialog,dialogSetting,option);
	// 弹出框全选CHECKBOX绑定事件
	checkAllBind(option);
}
/**
 * 对象批次信息弹出框
 * 
 * @param option
 * @return
 */
function popObjBatchDialog(option){
	var index = 43;
	var mode = 1;
	option.dialogId = 'objBatchDialog';
	option.popTableId = 'objBatch_dataTable';
	var $dialog = $("#" + option.dialogId);
	if (!isEmpty(option.mode)){
		mode = option.mode;
	}
	if(popDataTable_global.objBatchDialogHtml == ""){
		popDataTable_global.objBatchDialogHtml = $dialog.html();
	}
	var dialogBody = popDataTable_global.objBatchDialogHtml;
	// 设置URL
	var url = $('#objBatchUrl').html();
	url += "?" + getSerializeToken();
	if(!isEmpty(option.checkType)){
		url += "&checkType=" + option.checkType;
	}
	if(!isEmpty(option.brandInfoId)){
		url += "&brandInfoId=" + option.brandInfoId;
	}
	var tableSetting = {
			 // 一页显示页数
			 iDisplayLength:5,
			 // 表格ID
			 tableId : '#'+option.popTableId,
			 // 数据URL
			 url : url,
			 // 表格列属性设置
			 aoColumns : [  { "sName": "checkbox","bSortable": false}, 	// 0
							{ "sName": "searchCode"},                   // 1
							{ "sName": "recordName"},                   // 2
							{ "sName": "comments"}],                // 3
			index:index,
			colVisFlag: false,
			// 横向滚动条出现的临界宽度
			//sScrollX : "100%",
			// pop窗口弹出后回调函数
			fnDrawCallback:function(){
				if(typeof(option.callBack) == "function") {
					option.callBack();
				}
			}
	 };
	// 调用获取表格函数
	getTable(tableSetting);

	var dialogSetting = {
			width:650,
			
			title:$("#PopObjBatchTitle").text(), 
			close: function(event, ui) {
				if (mode==2){
					closeCherryDialog(option.dialogId,dialogBody);				
					oTableArr[index]= null;
				}else{
					$(this).dialog( "destroy" );
				}
			}
	};
	option.bindFlag = true;
	// 设置Dialog共通属性
	setDialogSetting($dialog,dialogSetting,option);
}

/**
 * 活动会员导入弹出框
 * 
 * @param option
 * @return
 */
function popMemImportDialog(option){
	var $dialog = $("#" + option.dialogId);
	var dialogSetting = {
			height:300,
			width:600,
			modal:true,
			title:$("#PopMemImportTitle").text(), 
			close: function(event, ui) {
				$dialog.dialog('destroy');
				$dialog.remove();
			},
			buttons:[{text: $("#global_page_ok").text(),
			        	 click: function() {
			        		 if(option.click){
			        			 option.click();
			        		 }else{
			        			 var $parentSearchCode = $('.SEARCHCODE').filter(':visible').find(':input[name="searchCode"]');
				        		 var $dialogSearchCode = $dialog.find(':input[name="searchCode"]');
				        		 $parentSearchCode.val($dialogSearchCode.val());
			        		 }
			        		 $(this).dialog("close");
			             }
		         	}
			]
	};
	$dialog.dialog(dialogSetting);
}

/**
 * 活动Coupon导入弹出框
 * 
 * @param option
 * @return
 */
function popCouponDialog(option){
	var $dialog = $("#" + option.dialogId);
	var $parents = $('.FORM_CONTEXT').filter(':visible');
	var $parentCampaignCode = $parents.find(':input[name="campaignCode"]');
	var $dialogCampaignCode = $dialog.find(':input[name="campaignCode"]');
	var $parentObtainToDate = $parents.find(':input[name="obtainToDate"]');
	var $dialogObtainToDate = $dialog.find(':input[name="obtainToDate"]');
	$dialogCampaignCode.val($parentCampaignCode.val());
	$dialogObtainToDate.val($parentObtainToDate.val());
	var dialogSetting = {
			height:280,
			width:580,
			modal:true,
			title:$("#PopMemImportTitle").text(), 
			close: function(event, ui) {
				$dialog.dialog('destroy');
				$dialog.remove();
			},
			buttons:[{text: $("#global_page_ok").text(),
			        	 click: function() {
			        		 var $parentBatchCode = $('.BATCHCODE').filter(':visible').find(':input[name="batchCode"]');
			        		 var $dialogBatchCode = $dialog.find(':input[name="batchCode"]');
			        		 $parentBatchCode.val($dialogBatchCode.val());
			        		 $(this).dialog("close");
			             }
		         	}
			]
	};
	$dialog.dialog(dialogSetting);
}


/**
 * 共通弹出框
 * 
 * @param option
 * @return
 */
function popDataTableOfComm (option){
	if ("" == popDataTable_global.commDialogHtml) {
		// 临时存放规则页面内容
		popDataTable_global.commDialogHtml = $(option.dialogId).html();
	} else {
		$(option.dialogId).html(popDataTable_global.commDialogHtml);
	}
	oTableArr[option.index] = null;
	var csrftoken = $('#csrftoken').serialize();
	if(!csrftoken) {
		csrftoken = $('#csrftoken',window.opener.document).serialize();
	}
	var url = option.url + '?' + csrftoken + (option.param?('&'+option.param):'');
	var tableSetting = {
			 // 一页显示页数
			 iDisplayLength:10,
			 // 表格ID
			 tableId : option.tableId,
			 // 不自动计算列宽度 
			 bAutoWidth: false,
			 // 数据URL
			 url : url,
			 // 表格列属性设置
			 aoColumns : option.aoColumns,
			 index: option.index,
			 colVisFlag: false
	 };
	
	// 调用获取表格函数
	getTable(tableSetting);

	var dialogSetting = {
		bgiframe: true,
		width:600, 
		height:'auto',
		minWidth:600,
		zIndex: 9999,
		modal: true, 
		title: option.title,
		close: function(event, ui) { 
			$(option.dialogId).dialog( "destroy" ); 
			$(option.dialogId).empty(); }
	};
	$(option.dialogId).dialog(dialogSetting);
	var callback = option.callback;
	if (callback) {
		$(option.confirmBtn).unbind('click');
		$(option.confirmBtn).click(function(){
			if(typeof(callback) == "function") {
				callback();
			}
			$(option.dialogId).dialog( "destroy" ); 
			$(option.dialogId).empty();
		});
	}
}

// 会员共通弹出框
function popDataTableOfMemList(url, param) {
	
	if($("#memListDialog").length == 0) {
		$("body").append('<div style="display:none" id="memListDialog"></div>');
	} else {
		$("#memListDialog").empty();
	}
	var callback = function(msg) {
		$("#memListDialog").html(msg);
		var dialogSetting = {
			dialogInit: "#memListDialog",
			text: msg,
			width: 	800,
			height: 430,
			title: 	$("#memListDialog").find("#dialogTitle").text(),
			confirm: $("#memListDialog").find("#dialogClose").text(),
			confirmEvent: function(){removeDialog("#memListDialog");}
		};
		openDialog(dialogSetting);
		
		if(oTableArr[50]) {
			oTableArr[50] = null;
		}
		var searchMemListUrl = $("#searchMemListUrl").attr("href")+"?"+param;
		var tableSetting = {
				 // 表格ID
				 tableId : '#memListDataTable',
				 // 一页显示页数
				 iDisplayLength:10,
				 // 数据URL
				 url : searchMemListUrl,
				 // 表格默认排序
				 aaSorting : [[ 3, "asc" ]],
				 // 表格列属性设置
				 aoColumns : [  { "sName": "number", "sWidth": "5%", "bSortable": false}, 	
								{ "sName": "memName", "sWidth": "20%"},                      
								{ "sName": "memCode", "sWidth": "20%"},
								{ "sName": "joinDate", "sWidth": "20%"},
								{ "sName": "levelName", "sWidth": "15%"},
								{ "sName": "mobilePhone", "sWidth": "20%"}],               
				index: 50,
				colVisFlag:false
		 };
		// 调用获取表格函数
		getTable(tableSetting);
	};
	cherryAjaxRequest({
		url: url,
		param: null,
		callback: callback
	});
}

// 会员销售共通弹出框
function popDataTableOfMemSaleList(url, param) {
	
	if($("#memSaleListDialog").length == 0) {
		$("body").append('<div style="display:none" id="memSaleListDialog"></div>');
	} else {
		$("#memSaleListDialog").empty();
	}
	var callback = function(msg) {
		$("#memSaleListDialog").html(msg);
		var dialogSetting = {
			dialogInit: "#memSaleListDialog",
			text: msg,
			width: 	800,
			height: 430,
			title: 	$("#memSaleListDialog").find("#dialogTitle").text(),
			confirm: $("#memSaleListDialog").find("#dialogClose").text(),
			confirmEvent: function(){removeDialog("#memSaleListDialog");}
		};
		openDialog(dialogSetting);
		
		if(oTableArr[51]) {
			oTableArr[51] = null;
		}
		var searchMemSaleListUrl = $("#searchMemSaleListUrl").attr("href")+"?"+param;
		var tableSetting = {
				 // 表格ID
				 tableId : '#memSaleListDataTable',
				 // 一页显示页数
				 iDisplayLength:10,
				 // 数据URL
				 url : searchMemSaleListUrl,
				 // 表格默认排序
				 aaSorting : [[ 2, "asc" ]],
				 // 表格列属性设置
				 aoColumns : [  { "sName": "number", "sWidth": "5%", "bSortable": false},      
								{ "sName": "billCode", "sWidth": "35%"},
								{ "sName": "eventDate", "sWidth": "20%"},
								{ "sName": "counterCode", "sWidth": "20%"},
								{ "sName": "amount", "sWidth": "10%"},
								{ "sName": "quantity", "sWidth": "10%"}],               
				index: 51,
				colVisFlag:false
		 };
		// 调用获取表格函数
		getTable(tableSetting);
	};
	cherryAjaxRequest({
		url: url,
		param: null,
		callback: callback
	});
}
 /* 沟通信息模板弹出框
 * 
 * @param option
 * @return
 */
function popMsgTemplateDialog(option){
	var index = 25;
	var mode = 1;
	option.dialogId = 'msgTemplateDialog';
	option.popTableId = 'msgTemplateDataTable';
	var $dialog = $("#" + option.dialogId);
	if (!isEmpty(option.mode)){
		mode = option.mode;
	}
	if(popDataTable_global.msgTemplateDialogHtml == ""){
		popDataTable_global.msgTemplateDialogHtml = $dialog.html();
	}
	var dialogBody = popDataTable_global.msgTemplateDialogHtml;
	// 设置URL
	var url = $('#msgTemplateSearchUrl').html();
	url += "?" + getSerializeToken();
	if(!isEmpty(option.messageType)){
		url += "&messageType=" + option.messageType;
	}
	if(!isEmpty(option.brandInfoId)){
		url += "&brandInfoId=" + option.brandInfoId;
	}
	// 目标区内容加载到缓存区
	exchangeHtml(option);
	var tableSetting = {
			 // 表格ID
			 tableId : '#' + option.popTableId,
			 // 一页显示页数
			 iDisplayLength:10,
			 // 数据URL
			 url : url,
			 // 表格列属性设置
			 aoColumns : [  {"sName" : "RowNumber", "sWidth": "5%", "bSortable": false},
			                {"sName" : "templateCode", "bVisible" : false, "sWidth" : "5%"},
			                {"sName" : "templateName", "sWidth" : "15%"},
		                    {"sName" : "templateUse", "sWidth" : "5%"},
		                    {"sName" : "contents", "bSortable" : false},
		                    {"sName" : "customerType", "sWidth" : "5%"},
		                    {"sName" : "act", "sWidth" : "10%", "bSortable" : false} 
		                    ],
			index:index,
			colVisFlag: false,
			// pop窗口弹出后回调函数
			fnDrawCallback:function(){
				
			}
	 };
	// 调用获取表格函数
	getTable(tableSetting);
	var dialogSetting = {
			width:'800',
			title:$("#PopMsgTemplateTitle").text(), 
			close: function(event, ui) {
				if (mode==2){
					closeCherryDialog(option.dialogId,dialogBody);				
					oTableArr[index]= null;
				}else{
					$(this).dialog( "destroy" );
				}
			}
	};
	// 设置Dialog共通属性
	setDialogSetting($dialog,dialogSetting,option);
}

function popDataTableOfCounterList(url, param, value, callback) {
	if($("#searchCounterDialog").length == 0) {
		$("body").append('<div style="display:none" id="searchCounterDialog"></div>');
	} else {
		$("#searchCounterDialog").empty();
	}
	var searchInitCallback = function(msg) {
		$("#searchCounterDialog").html(msg);
		var dialogSetting = {
			dialogInit: "#searchCounterDialog",
			text: msg,
			width: 	600,
			height: 350,
			title: 	$("#searchCounterDialog").find("#searchCounterTitle").text(),
			confirm: $("#searchCounterDialog").find("#dialogConfirm").text(),
			cancel: $("#searchCounterDialog").find("#dialogCancel").text(),
			confirmEvent: function(){
				if(typeof(callback) == "function") {
					callback("searchCounterDataTable");
				}
				removeDialog("#searchCounterDialog");
			},
			cancelEvent: function(){removeDialog("#searchCounterDialog");}
		};
		openDialog(dialogSetting);
		
		if(oTableArr[40]) {
			oTableArr[40] = null;
		}
		var searchUrl = $("#searchCounterListUrl").attr("href")+"?"+getSerializeToken();
		if(param) {
			searchUrl += "&" + param;
		}
		var tableSetting = {
				 // 表格ID
				 tableId : '#searchCounterDataTable',
				 // 一页显示页数
				 iDisplayLength:5,
				 // 数据URL
				 url : searchUrl,
				 // 表格默认排序
				 aaSorting : [[ 0, "asc" ]],
				 // 表格列属性设置
				 aoColumns : [  { "sName": "BIN_OrganizationID", "sWidth": "5%", "bSortable": false}, 	
								{ "sName": "CounterCode", "sWidth": "45%"},                      
								{ "sName": "CounterNameIF", "sWidth": "50%"}],               
				index:40,
				colVisFlag:false,
				fnDrawCallback : function() {
					if(value) {
						$('#searchCounterDataTable').find(':input').each(function() {
							if($(this).val() == value) {
								$(this).prop('checked', true);
								return false;
							}
						});
					}
				}
		 };
		// 调用获取表格函数
		getTable(tableSetting);
	};
	cherryAjaxRequest({
		url: url,
		param: null,
		callback: searchInitCallback
	});
}

function popDataTableOfEmployeeList(url, param, value, callback) {
	if($("#searchEmployeeDialog").length == 0) {
		$("body").append('<div style="display:none" id="searchEmployeeDialog"></div>');
	} else {
		$("#searchEmployeeDialog").empty();
	}
	var searchInitCallback = function(msg) {
		$("#searchEmployeeDialog").html(msg);
		var dialogSetting = {
			dialogInit: "#searchEmployeeDialog",
			text: msg,
			width: 	600,
			height: 350,
			title: 	$("#searchEmployeeDialog").find("#searchEmployeeTitle").text(),
			confirm: $("#searchEmployeeDialog").find("#dialogConfirm").text(),
			cancel: $("#searchEmployeeDialog").find("#dialogCancel").text(),
			confirmEvent: function(){
				if(typeof(callback) == "function") {
					callback("searchEmployeeDataTable");
				}
				removeDialog("#searchEmployeeDialog");
			},
			cancelEvent: function(){removeDialog("#searchEmployeeDialog");}
		};
		openDialog(dialogSetting);
		
		if(oTableArr[30]) {
			oTableArr[30] = null;
		}
		var searchUrl = $("#searchEmployeeListUrl").attr("href")+"?"+getSerializeToken();
		if(param) {
			searchUrl += "&" + param;
		}
		var tableSetting = {
				 // 表格ID
				 tableId : '#searchEmployeeDataTable',
				 // 一页显示页数
				 iDisplayLength:5,
				 // 数据URL
				 url : searchUrl,
				 // 表格默认排序
				 aaSorting : [[ 0, "asc" ]],
				 // 表格列属性设置
				 aoColumns : [  { "sName": "employeeId", "sWidth": "5%", "bSortable": false}, 	
								{ "sName": "employeeCode", "sWidth": "25%"},                      
								{ "sName": "employeeName", "sWidth": "30%"},
								{ "sName": "counterName", "sWidth": "40%"}],               
				index:30,
				colVisFlag:false,
				fnDrawCallback : function() {
					if(value) {
						$('#searchEmployeeDataTable').find(':input').each(function() {
							if($(this).val() == value) {
								$(this).prop('checked', true);
								return false;
							}
						});
					}
				}
		 };
		// 调用获取表格函数
		getTable(tableSetting);
	};
	cherryAjaxRequest({
		url: url,
		param: null,
		callback: searchInitCallback
	});
}

// 发卡柜台弹出框共通
function popRegionDialog(url, param, value, callback) {
	if($("#regionTreeDialog").length == 0) {
		$("body").append('<div style="display:none" id="regionTreeDialog"></div>');
	} else {
		$("#regionTreeDialog").empty();
	}
	var searchInitCallback = function(msg) {
		// 发卡柜台弹出框
		$("#regionTreeDialog").html(msg);
		var treeObj = null;
		var dialogSetting = {
			dialogInit: "#regionTreeDialog",
			text: msg,
			width: 	400,
			height: 560,
			title: 	$("#regionDialogTitle").text(),
			confirm: $("#regionDialogConfirm").text(),
			cancel: $("#regionDialogCancel").text(),
			confirmEvent: function(){
				if(typeof(callback) == "function") {
					var selExclusiveFlagObj = {};
					selExclusiveFlagObj.selExclusiveFlag=$(":input[name=selExclusiveFlag]:checked").val();
					selExclusiveFlagObj.selExclusiveFlagText=$(":input[name=selExclusiveFlag]:checked").next().text();
					var popCouValidFlagObj = {};
					popCouValidFlagObj.popCouValidFlag=$(":input[name=popCouValidFlag]").val();
					popCouValidFlagObj.popCouValidFlagText=$(":input[name=popCouValidFlag]").find("option:selected").text();
					var selMode = $(":input[name=selMode]").val();
					var channelRegionObj = {};
					if(selMode == "2") {
						channelRegionObj.channelRegionId=$("#channelRegionDiv").find(":input[name=channelRegionId]").val();
						channelRegionObj.channelRegionText=$("#channelRegionDiv").find(":input[name=channelRegionId]").find("option:selected").text();
					}
					callback(treeObj, selMode, selExclusiveFlagObj, popCouValidFlagObj, channelRegionObj);
				}
				removeDialog("#regionTreeDialog");
			},
			cancelEvent: function(){removeDialog("#regionTreeDialog");}
		};
		openDialog(dialogSetting);
		if(value) {
			if(value.selMode) {
				$(":input[name=selMode]").val(value.selMode);
			}
			if(value.exclusiveFlag) {
				$(":input[name=selExclusiveFlag]").each(function(){
					if(this.value == value.exclusiveFlag) {
						this.checked = true;
						return false;
					}
				});
			}
			if(value.couValidFlag) {
				$(":input[name=popCouValidFlag]").val(value.couValidFlag);
			}
		}
		
		$("#regionTreeDialog").find("#selMode").change(function() {
			$("#channelRegionDiv").hide();
			$('#regionTreeDialog').find("#regionTree").html($("#regionDialogHandle").text());
			var selMode = this.value;
			var searchUrl = $("#popRegionDialogUrl").attr("href");
			var searchParam = $(this).serialize();
			var popCouValidFlag = $("#regionTreeDialog").find("#popCouValidFlag").serialize();
			searchParam += '&' + popCouValidFlag;
			if(param) {
				searchParam += '&' + param;
			}
			var regionTreeInitCallback = function(msg) {
				if(msg) {
					if(selMode == "1" || selMode == "3" || selMode == "4" || selMode == "5") {
						var setting = {
							check: {
								enable: true
							},
							data:{
								key:{
									name:"name",
									children:"nodes"
								}
							}
						};
						var $regionTree = $('#regionTreeDialog').find("#regionTree");
						treeObj = $.fn.zTree.init($regionTree,setting,eval('('+msg+')'));
						if(value) {
							if(value.regionInfo && value.regionInfo.length > 0) {
								for(var i = 0; i < value.regionInfo.length; i++) {
									var node = treeObj.getNodeByParam("id", value.regionInfo[i], null);
									if(node != null) {
										treeObj.checkNode(node, true, true);
									}
								}
							}
							value = "";
						}
					} else if(selMode == "2") {
						var regionIdInfo = eval('('+msg+')');
						if(regionIdInfo && regionIdInfo.length > 0) {
							var regionSelect = '<select name="channelRegionId" id="channelRegionId"><option value="">'+$("#select_default").html()+'</option>';
							for(var i = 0; i < regionIdInfo.length; i++) {
								regionSelect += '<option value="'+regionIdInfo[i].regionId+'">'+regionIdInfo[i].regionName+'</option>';
							}
							regionSelect += '</select>';
							$('#channelRegionDiv').find("span").html(regionSelect);
							$("#channelRegionDiv").show();
							if(value.channelRegionId) {
								$('#channelRegionDiv').find(":input[name=channelRegionId]").val(value.channelRegionId);
							}
							
							$('#channelRegionDiv').find("#channelRegionId").change(function() {
								var searchChannelUrl = $("#popChannelDialogUrl").attr("href");
								var searchChannelParam = $(this).serialize();
								searchChannelParam += '&' + popCouValidFlag;
								if(param) {
									searchChannelParam += '&' + param;
								}
								var channelTreeInitCallback = function(msg) {
									if(msg) {
										var setting = {
											check: {
												enable: true
											},
											data:{
												key:{
													name:"name",
													children:"nodes"
												}
											}
										};
										var $regionTree = $('#regionTreeDialog').find("#regionTree");
										treeObj = $.fn.zTree.init($regionTree,setting,eval('('+msg+')'));
										if(value) {
											if(value.regionInfo && value.regionInfo.length > 0) {
												for(var i = 0; i < value.regionInfo.length; i++) {
													var node = treeObj.getNodeByParam("id", value.regionInfo[i], null);
													if(node != null) {
														treeObj.checkNode(node, true, true);
													}
												}
											}
											value = "";
										}
									} else {
										$('#regionTreeDialog').find("#regionTree").empty();
									}
								}
								cherryAjaxRequest({
									url: searchChannelUrl,
									param: searchChannelParam,
									callback: channelTreeInitCallback
								});
							});
							$('#channelRegionDiv').find("#channelRegionId").trigger("change");
						} else {
							$('#regionTreeDialog').find("#regionTree").empty();
						}
					}
				} else {
					$('#regionTreeDialog').find("#regionTree").empty();
				}
			};
			cherryAjaxRequest({
				url: searchUrl,
				param: searchParam,
				callback: regionTreeInitCallback
			});
		});
		
		$("#locationRegionButtion").click(function() {
			if(treeObj == null) {
				return false;
			}
			if($(":input[name=locationPosition]").val()) {
				var locationUrl = $("#locationRegionUrl").attr("href");
				var locationParam = $(":input[name=locationPosition]").serialize();
				var popCouValidFlag = $("#regionTreeDialog").find("#popCouValidFlag").serialize();
				locationParam += '&' + popCouValidFlag;
				if(param) {
					locationParam += '&' + param;
				}
				var locationCallback = function(msg){
					var node = treeObj.getNodeByParam("id", msg, null);
					treeObj.expandNode(node,true,false);
					treeObj.selectNode(node);
				}
				cherryAjaxRequest({
					url: locationUrl,
					param: locationParam,
					callback: locationCallback
				});
			}
			return false;
		});
		
		counterBinding({elementId:"locationPosition",showNum:20,selected:"code",privilegeFlag:1});
		
		$("#regionTreeDialog").find("#popCouValidFlag").change(function() {
			$("#regionTreeDialog").find("#selMode").trigger("change");
		});
		
		$("#regionTreeDialog").find("#selMode").trigger("change");
	};
	cherryAjaxRequest({
		url: url,
		param: param,
		callback: searchInitCallback
	});
}

//单独渠道弹出框
function popChannelDialog(url,value, callback){
	if($("#channelTreeDialog").length == 0) {
		$("body").append('<div style="display:none" id="channelTreeDialog"></div>');
	} else {
		$("#channelTreeDialog").empty();
	}
	
	var searchInitCallback = function(msg){
		// 渠道弹出框
		$("#channelTreeDialog").html(msg);
		var treeObj = null;
		var dialogSetting = {
			dialogInit: "#channelTreeDialog",
			text: msg,
			width: 	400,
			height: 560,
			title: 	$("#channelDialogTitle").text(),
			confirm: $("#channelDialogConfirm").text(),
			cancel: $("#channelDialogCancel").text(),
			confirmEvent: function(){
				if(typeof(callback) == "function"){
					callback(treeObj);
				}
				removeDialog("#channelTreeDialog");
			},
			cancelEvent: function(){
				removeDialog("#channelTreeDialog");
			}
		};
		openDialog(dialogSetting);
		
		var searchUrl = $("#popChannelDialogUrl").attr("href");
		var regionTreeInitCallback = function(msg){
			if(msg){
				var setting = {
						check: {
							enable: true
						},
						data:{
							key:{
								name:"name",
								children:"nodes"
							}
						}
					};
				var $regionTree = $('#channelTreeDialog').find("#channelTree");
				treeObj = $.fn.zTree.init($regionTree,setting,eval('('+msg+')'));
				if(value) {
					if(value.regionInfo && value.regionInfo.length > 0) {
						for(var i = 0; i < value.regionInfo.length; i++) {
							var node = treeObj.getNodeByParam("id", value.regionInfo[i], null);
							if(node != null) {
								treeObj.checkNode(node, true, true);
							}
						}
					}
					value = "";
				}
			}else {
				$('#channelTreeDialog').find("#channelTree").empty();
			}
		};
		cherryAjaxRequest({
			url: searchUrl,
			callback: regionTreeInitCallback
		});
		
		$("#locationRegionButtion").click(function() {
			if(treeObj == null) {
				return false;
			}
			if($(":input[name=locationPosition]").val()) {
				var locationUrl = $("#locationRegionUrl").attr("href");
				var locationParam = $(":input[name=locationPosition]").serialize();
				var locationCallback = function(msg){
					var node = treeObj.getNodeByParam("id", msg, null);
					treeObj.expandNode(node,true,false);
					treeObj.selectNode(node);
				}
				cherryAjaxRequest({
					url: locationUrl,
					param: locationParam,
					callback: locationCallback
				});
			}
			return false;
		});
		
		counterBinding({elementId:"locationPosition",showNum:20,selected:"code",privilegeFlag:1});		
	};	
	cherryAjaxRequest({
		url: url,
		callback: searchInitCallback
	});
}

// 会员活动选择
function popDataTableOfCampaignList(url, param, value, callback, valueArr) {
	if($("#searchCampaignDialog").length == 0) {
		$("body").append('<div style="display:none" id="searchCampaignDialog"></div>');
	} else {
		$("#searchCampaignDialog").empty();
	}
	var searchInitCallback = function(msg) {
		$("#searchCampaignDialog").html(msg);
		var dialogSetting = {
			dialogInit: "#searchCampaignDialog",
			text: msg,
			width: 	600,
			height: 400,
			title: 	$("#searchCampaignDialog").find("#searchCampaignTitle").text(),
			confirm: $("#searchCampaignDialog").find("#dialogConfirm").text(),
			cancel: $("#searchCampaignDialog").find("#dialogCancel").text(),
			confirmEvent: function(){
				if(typeof(callback) == "function") {
					callback("searchCampaignDataTable");
				}
				removeDialog("#searchCampaignDialog");
			},
			cancelEvent: function(){removeDialog("#searchCampaignDialog");}
		};
		openDialog(dialogSetting);
		
		if(oTableArr[70]) {
			oTableArr[70] = null;
		}
		var searchUrl = $("#searchCampaignListUrl").attr("href")+"?"+getSerializeToken();
		if(param) {
			searchUrl += "&" + param;
		}
		var tableSetting = {
				 // 表格ID
				 tableId : '#searchCampaignDataTable',
				 // 一页显示页数
				 iDisplayLength:5,
				 // 数据URL
				 url : searchUrl,
				 // 表格默认排序
				 aaSorting : [[ 2, "desc" ]],
				 // 表格列属性设置
				 aoColumns : [  { "sName": "campaignCode", "sWidth": "5%", "bSortable": false}, 	
								{ "sName": "campaignName", "sWidth": "40%"},   
								{ "sName": "campaignCode", "sWidth": "40%"},   
								{ "sName": "campaignType", "sWidth": "15%"}],               
				index:70,
				colVisFlag:false,
				fnDrawCallback : function() {
					if (valueArr) {
						for (var j in valueArr) {
							$('#searchCampaignDataTable').find(':input').each(function() {
								if($(this).val() == valueArr[j]) {
									$(this).prop('checked', true);
								}
							});
						}
					} else if(value) {
						$('#searchCampaignDataTable').find(':input').each(function() {
							if($(this).val() == value) {
								$(this).prop('checked', true);
								return false;
							}
						});
					}
					// 全选
					var $ckAll = $('#camp_checkAll');
					if ($ckAll.length == 1) {
						$ckAll.prop("checked",false);
						var $ck = $("#searchCampaignDataBody").find("input[name='campaignCode']");
						if ($ck.length > 0) {
							$ckAll.bind('click', function() {
								if ($(this).is(":checked")) {
									$ck.prop("checked",true);
								} else {
									$ck.prop("checked",false);
								}
							});
							$ck.bind('click', function() {
								if ($("#searchCampaignDataBody").find("input[name='campaignCode']:not(:checked)").length == 0) {
									$ckAll.prop("checked",true);
								} else {
									$ckAll.prop("checked",false);
								}
							});
						}
					}
				}
		 };
		// 调用获取表格函数
		getTable(tableSetting);
	};
	var p = null;
	if (param && param.indexOf("param2") >= 0) {
		p = param;
	}
	cherryAjaxRequest({
		url: url,
		param: p,
		callback: searchInitCallback
	});
}

// 销售记录
function popDataTableOfSaleRecordList(url, param, value, callback) {
	if($("#searchSaleRecordDialog").length == 0) {
		$("body").append('<div style="display:none" id="searchSaleRecordDialog"></div>');
	} else {
		$("#searchSaleRecordDialog").empty();
	}
	var searchInitCallback = function(msg) {
		$("#searchSaleRecordDialog").html(msg);
		var dialogSetting = {
			dialogInit: "#searchSaleRecordDialog",
			text: msg,
			width: 	700,
			height: 380,
			title: 	$("#searchSaleRecordDialog").find("#searchSaleRecordTitle").text(),
			confirm: $("#searchSaleRecordDialog").find("#dialogConfirm").text(),
			cancel: $("#searchSaleRecordDialog").find("#dialogCancel").text(),
			confirmEvent: function(){
				if(typeof(callback) == "function") {
					callback("searchSaleRecordDialog");
				}
				removeDialog("#searchSaleRecordDialog");
			},
			cancelEvent: function(){removeDialog("#searchSaleRecordDialog");}
		};
		openDialog(dialogSetting);
		
		if(oTableArr[71]) {
			oTableArr[71] = null;
		}
		var searchUrl = $("#searchSaleRecordListUrl").attr("href")+"?"+getSerializeToken();
		if(param) {
			searchUrl += "&" + param;
		}
		var tableSetting = {
				 // 表格ID
				 tableId : '#searchSaleRecordDataTable',
				 // 一页显示页数
				 iDisplayLength:5,
				 // 数据URL
				 url : searchUrl,
				 // 表格默认排序
				 aaSorting : [[ 4, "desc" ]],
				 // 表格列属性设置
				 aoColumns : [  { "sName": "billCode", "sWidth": "5%", "bSortable": false}, 	
								{ "sName": "billCode", "sWidth": "25%"},   
								{ "sName": "saleType", "sWidth": "10%"},   
								{ "sName": "departCode", "sWidth": "15%"},
								{ "sName": "saleTime", "sWidth": "15%"},
								{ "sName": "quantity", "sWidth": "15%"},
								{ "sName": "amount", "sWidth": "15%"}],               
				index:71,
				colVisFlag:false,
				// 横向滚动条出现的临界宽度
				sScrollX : "100%",
				fnDrawCallback : function() {
					if(value) {
						$('#searchSaleRecordDataTable').find(':input').each(function() {
							if($(this).val() == value) {
								$(this).prop('checked', true);
								return false;
							}
						});
					}
				}
		 };
		// 调用获取表格函数
		getTable(tableSetting);
	};
	cherryAjaxRequest({
		url: url,
		param: null,
		callback: searchInitCallback
	});
}

//经销商信息
function popDataTableOfResellerList(url, param, value, callback) {
	if($("#searchResellerDialog").length == 0) {
		$("body").append('<div style="display:none" id="searchResellerDialog"></div>');
	} else {
		$("#searchResellerDialog").empty();
	}
	var searchInitCallback = function(msg) {
		$("#searchResellerDialog").html(msg);
		var dialogSetting = {
			dialogInit: "#searchResellerDialog",
			text: msg,
			bgiframe: true,
			width:600, 
			height:"auto",
			minWidth:600,
			zIndex: 9999,
			modal: true,
			resizable: false,
			title: 	$("#searchResellerDialog").find("#dialogTitle").text(),
			confirm: $("#searchResellerDialog").find("#dialogConfirm").text(),
			cancel: $("#searchResellerDialog").find("#dialogCancel").text(),
			confirmEvent: function(){
				if(typeof(callback) == "function") {
					callback("searchResellerDialog");
				}
				removeDialog("#searchResellerDialog");
			},
			cancelEvent: function(){removeDialog("#searchResellerDialog");}
		};
		openDialog(dialogSetting);
		
		if(oTableArr[72]) {
			oTableArr[72] = null;
		}
		var searchUrl = $("#searchResellerListUrl").attr("href")+"?"+getSerializeToken();
		if(param) {
			searchUrl += "&" + param;
		}
		var tableSetting = {
				 // 表格ID
				 tableId : '#searchResellerDataTable',
				 // 一页显示页数
				 iDisplayLength:5,
				 // 数据URL
				 url : searchUrl,
				 // 表格默认排序
				 aaSorting : [[ 1, "desc" ]],
				 // 表格列属性设置
				 aoColumns : [  { "sName": "", "sWidth": "1%", "bSortable": false},
					            { "sName": "resellerCode"},
								{ "sName": "resellerName"},
								{ "sName": "levelCode"},
								{ "sName": "type"}
					         ],               
				index:72,
				colVisFlag:false,
				// 横向滚动条出现的临界宽度
				sScrollX : "100%",
				fnDrawCallback : function() {
					if(value) {
						$('#searchResellerDataTable').find(':input').each(function() {
							if($(this).val() == value) {
								$(this).prop('checked', true);
								return false;
							}
						});
					}
				}
		 };
		// 调用获取表格函数
		getTable(tableSetting);
	};
	cherryAjaxRequest({
		url: url,
		param: null,
		callback: searchInitCallback
	});
}

/**
 * 信息弹出框
 * 
 * @param option
 * @return
 */
function popDialog(option){
	var mode = 1;
	var $dialog = $("#" + option.dialogId);
	if(!option.width){
		option.width = '450';
	}
	var dialogSetting = {
			width:option.width,
			title:option.title, 
			close: function(event, ui) {
				$(this).dialog( "destroy" );
			}
	};
	// 设置Dialog共通属性
	setDialogSetting($dialog,dialogSetting,option);
}

//活动对象为搜索结果的活动
function popCampObjList(url, param, value, callback) {
	if($("#popCampObjListDialog").length == 0) {
		$("body").append('<div style="display:none" id="popCampObjListDialog"></div>');
	} else {
		$("#popCampObjListDialog").empty();
	}
	var searchInitCallback = function(msg) {
		$("#popCampObjListDialog").html(msg);
		var dialogSetting = {
			dialogInit: "#popCampObjListDialog",
			text: msg,
			bgiframe: true,
			width:600,
			height:"auto",
			minWidth:600,
			zIndex: 9999,
			modal: true,
			resizable: false,
			title: 	$("#popCampObjListDialog").find("#dialogTitle").text(),
			confirm: $("#popCampObjListDialog").find("#dialogConfirm").text(),
			cancel: $("#popCampObjListDialog").find("#dialogCancel").text(),
			confirmEvent: function(){
				if(typeof(callback) == "function") {
					callback("popCampObjListDialog");
				}
				removeDialog("#popCampObjListDialog");
			},
			cancelEvent: function(){removeDialog("#popCampObjListDialog");}
		};
		openDialog(dialogSetting);

		if(oTableArr[100]) {
			oTableArr[100] = null;
		}
		var searchUrl = $("#searchCampObjListUrl").attr("href")+"?"+getSerializeToken();
		if(param) {
			searchUrl += "&" + param;
		}
		var tableSetting = {
			// 表格ID
			tableId : '#searchCampObjDataTable',
			// 一页显示页数
			iDisplayLength:5,
			// 数据URL
			url : searchUrl,
			// 表格默认排序
			aaSorting : [[ 1, "desc" ]],
			// 表格列属性设置
			aoColumns : [  { "sName": "", "sWidth": "1%", "bSortable": false},
				{ "sName": "SubCampaignCode"},
				{ "sName": "SubCampaignName"}
			],
			index:100,
			colVisFlag:false,
			// 横向滚动条出现的临界宽度
			sScrollX : "100%",
			fnDrawCallback : function() {
				if(value) {
					$('#searchCampObjDataTable').find(':input').each(function() {
						if($(this).val() == value) {
							$(this).prop('checked', true);
							return false;
						}
					});
				}
			}
		};
		// 调用获取表格函数
		getTable(tableSetting);
	};
	cherryAjaxRequest({
		url: url,
		param: null,
		callback: searchInitCallback
	});
}

function popDataTableOfCampaignList2(url, param, value, callback, valueArr){
	if($("#searchCampaignDialog").length == 0) {
		$("body").append('<div style="display:none" id="searchCampaignDialog"></div>');
	} else {
		$("#searchCampaignDialog").empty();
	}
	var searchInitCallback = function(msg) {
		$("#searchCampaignDialog").html(msg);
		var dialogSetting = {
			dialogInit: "#searchCampaignDialog",
			text: msg,
			width: 	600,
			height: 400,
			title: 	$("#searchCampaignDialog").find("#searchCampaignTitle").text(),
			confirm: $("#searchCampaignDialog").find("#dialogConfirm").text(),
			cancel: $("#searchCampaignDialog").find("#dialogCancel").text(),
			confirmEvent: function(){
				if(typeof(callback) == "function") {
					callback("searchCampaignDataTable");
				}
				removeDialog("#searchCampaignDialog");
			},
			cancelEvent: function(){removeDialog("#searchCampaignDialog");}
		};
		openDialog(dialogSetting);

		if(oTableArr[77]) {
			oTableArr[77] = null;
		}
		var searchUrl = $("#searchCampaignListUrl").attr("href")+"?"+getSerializeToken();
		if(param) {
			searchUrl += "&" + param;
		}
		var tableSetting = {
			// 表格ID
			tableId : '#searchCampaignDataTable',
			// 一页显示页数
			iDisplayLength:5,
			// 数据URL
			url : searchUrl,
			// 表格默认排序
			aaSorting : [[ 2, "desc" ]],
			// 表格列属性设置
			aoColumns : [  { "sName": "campaignCode", "sWidth": "5%", "bSortable": false},
				{ "sName": "campaignName", "sWidth": "40%"},
				{ "sName": "campaignCode", "sWidth": "40%"},
				{ "sName": "campaignType", "sWidth": "15%"}],
			index:77,
			colVisFlag:false,
			fnDrawCallback : function() {
				if (valueArr) {
					for (var j in valueArr) {
						$('#searchCampaignDataTable').find(':input').each(function() {
							if($(this).val() == valueArr[j]) {
								$(this).prop('checked', true);
							}
						});
					}
				} else if(value) {
					$('#searchCampaignDataTable').find(':input').each(function() {
						if($(this).val() == value) {
							$(this).prop('checked', true);
							return false;
						}
					});
				}
				// 全选
				var $ckAll = $('#camp_checkAll');
				if ($ckAll.length == 1) {
					$ckAll.prop("checked",false);
					var $ck = $("#searchCampaignDataBody").find("input[name='campaignCode']");
					if ($ck.length > 0) {
						$ckAll.bind('click', function() {
							if ($(this).is(":checked")) {
								$ck.prop("checked",true);
							} else {
								$ck.prop("checked",false);
							}
						});
						$ck.bind('click', function() {
							if ($("#searchCampaignDataBody").find("input[name='campaignCode']:not(:checked)").length == 0) {
								$ckAll.prop("checked",true);
							} else {
								$ckAll.prop("checked",false);
							}
						});
					}
				}
			}
		};
		// 调用获取表格函数
		getTable(tableSetting);
	};
	var p = null;
	if (param && param.indexOf("param2") >= 0) {
		p = param;
	}
	cherryAjaxRequest({
		url: url,
		param: p,
		callback: searchInitCallback
	});
}
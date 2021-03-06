/*
 * 全局变量定义
 */
var binOLPTJCS45_global = {};

// 消息区分(true:显示消息，false:删除消息)
binOLPTJCS45_global.msgKbn = false;
$(document).ready(function() {
//	// 表格列选中
//	$('thead th').live('click',function(){
//		$("th.sorting").removeClass('sorting');
//		$(this).addClass('sorting');
//	});
	$("#result_table table").attr("id", "dataTable");
	$("#result_list").append($("#result_table").html());
	$("#result_table table").removeAttr("id");
//	// 部门名称组合框
//	$('#organizationId').combobox({inputId: '#depart_text', btnId: '#depart_btn'});
//	// 仓库名称组合框
//	$('#inventSel').combobox({inputId: '#invent_text', btnId: '#invent_btn'});
//	cherryValidate({			
//		formId: "mainForm",		
//		rules: {
//			startDate: {dateValid:true},	// 开始日期
//			endDate: {dateValid:true}	// 结束日期
//		}		
//	});
	search();
} );

//用户查询
function search(){
	var $form = $('#mainForm');
	if (!$form.valid()) {
		return false;
	};
	
	var aoColumnsArr = [	{ "sName": "checkbox", "sWidth": "1%", "bSortable": false}, // 0
	                    	{ "sName": "no","sWidth": "1%","bSortable": false},			// 1
	                    	{ "sName": "solutionName","sWidth": "20%"},	                // 2							
	                    	{ "sName": "solutionCode","sWidth": "20%"},	                // 3							
							{ "sName": "comments","sWidth": "20%"},	                // 4							
							{ "sName": "startDate","sWidth": "15%"},						// 5
							{ "sName": "endDate","sWidth": "15%"},						// 6
	                        { "sName": "validFlag","sWidth": "4%","sClass": "center"}]; // 7
	var aiExcludeArr = [0,1];	
	var url = $("#searchUrl").attr("href");
	 // 查询参数序列化
	var params= $form.serialize();
	 params = params + "&csrftoken=" +$("#csrftoken").val();
	 url = url + "?" + params;
	 // 显示结果一览
	 $("#section").show();
	 // 表格设置
	 var tableSetting = {
			 // datatable 对象索引
			 index : 1,
			 // 表格ID
			 tableId : '#dataTable',
			 // 数据URL
			 url : url,
			 // 排序列
			 aaSorting : [[2, "desc"]],
			 // 表格列属性设置			 
			 aoColumns : aoColumnsArr,
			 // 不可设置显示或隐藏的列	
		   	aiExclude :[0, 1, 2],
			// 横向滚动条出现的临界宽度
			sScrollX : "100%",
			// 固定列数
			fixedColumns : 3,
			callbackFun : function (){		 		
		 		if (!binOLPTJCS45_global.msgKbn) {
		 			$("#actionResultDisplay").empty();
		 		}
		 		binOLPTJCS45_global.msgKbn = false;
		 	}
	 };
	 // 调用获取表格函数
	 getTable(tableSetting);
}

function checkSelectAll(checkbox){
	$("#errorMessage").empty();
	$('#dataTable_wrapper #dataTable_Cloned').find(":checkbox").prop("checked", $(checkbox).prop("checked"));
	if ($(checkbox).prop("checked") && $("input@[id=validFlag][checked]").length>0){
	}
};

function checkSelect(checkbox){
	$("#errorMessage").empty();
	if($(checkbox).prop("checked")) {
        if($("#dataTable_Cloned input@[id=validFlag]").length == $("#dataTable_Cloned input@[id=validFlag][checked]").length) {
            $("input@[id=allSelect]").prop("checked",true);
        }
    } else {
        $("input@[id=allSelect]").prop("checked",false);
    }
};

/*
 * 确认画面
 */
function confirmInit(object,confirmname){
	if(!this.validCheckBox()){
        return false;
    };
//	var param = $(object).parent().find(':input').serialize();
	var param = "brandInfoId="+$("#brandInfoId").val();
	param += "&"+$("#dataTable_Cloned").find(":checkbox[checked]").nextAll("#solutionIdArr").serialize();
	var strText = '';
	var strTitle = '';
	if (confirmname == 'disable') {
		strText="#disableText";
		strTitle="#disableTitle";
		// 停用
		param += "&validFlag=0";
		
	}else if(confirmname == 'enable'){
		strText="#enableText";
		strTitle="#enableTitle";
		// 启用
		param += "&validFlag=1";
		
	}
	var dialogSetting = {
		dialogInit: "#dialogInit",
		text:  $(strText).html(),
		width: 	500,
		height: 300,
		title: 	$(strTitle).text(),
		confirm: $("#dialogConfirm").text(),
		cancel: $("#dialogCancel").text(),
		confirmEvent: function(){confirmProcess($(object).attr("href"), param);},
		cancelEvent: function(){removeDialog("#dialogInit");}
	};
	openDialog(dialogSetting);
};

function confirmProcess(url,param) {
	var callback = function(msg) {
		$("#dialogInit").html(msg);
		if($("#errorMessageDiv").length > 0) {
			$("#errorMessageDiv").find("span").html($("#errorMessageDiv").next().text());
			$("#dialogInit").dialog( "option", {
				buttons: [{
					text: $("#dialogClose").text(),
				    click: function(){removeDialog("#dialogInit");if(oTableArr[1] != null)oTableArr[1].fnDraw(); }
				}]
			});
		} else {
			removeDialog("#dialogInit");
			if(oTableArr[1] != null)oTableArr[1].fnDraw();
		}
	};
	cherryAjaxRequest({
		url: url,
		param: param,
		callback: callback,
		formId: '#mainForm'
	});
};

/*
 * 验证勾选，显示隐藏错误提示
 */
function validCheckBox(){
	var flag = true;
	var checksize = $("input@[id=validFlag][checked]").length;
	if (checksize == 0){
	    //没有勾选
		$("#errorMessage").html($("#errorMessageTemp").html());
        $('#errorMessage').show();
        flag = false;
        }
	return flag;
};

/*
 * 关闭Dialog
 */
function dialogClose(){
	this.doClose('#dialogInit');
	if (binOLPTJCS45_global.refresh) {
		this.search();
		binOLPTJCS45_global.refresh = false;
	}
};

/**
 * 产品实时下发确认DIV
 * 
 * 
 * */
function issuedInit(){

	var _this = this;
	var text = "";
	var title = "";
//	text = '<p class="message"><span>'+$('#confirIsEnable').text();
	text = '<p class="message"><span>' + $("#issCntPrtMsg1").text() + '</span></p>';
//	title = $('#enableValTitle').text();
	title = $("#issCntPrt").text();
	var dialogSetting = {
			dialogInit: "#dialogIssuedInitDIV",
			text: text,
			width: 	300,
			height: 200,
			title: 	title,
			confirm: $("#dialogConfirmIss").text(),
			cancel: $("#dialogCancelIss").text(),
			confirmEvent: function(){
				issuedPrt();
//				removeDialog("#dialogIssuedInitDIV");
			},
			cancelEvent: function(){removeDialog("#dialogIssuedInitDIV");}
	};
	openDialog(dialogSetting);
}
function issuedPrt(){
	var url=$("#issuedPrtId").html();
	
	var param ="brandInfoId=" + $("#brandInfoId").val();
	param += "&csrftoken=" +$("#csrftoken").val();
	
	// 清空按钮
	$("#dialogIssuedInitDIV").dialog( "option", {
		buttons: []
	});
	$("#dialogIssuedInitDIV").html('').append($("#issLaunchingMsg").text());
	
//	$("#dialogIssuedInitDIV").html('').append("处理中...");
	cherryAjaxRequest({
		url: url,
		param: param,
		callback: function(msg) {
			var map = eval("(" + msg + ")");
			if(map.result == "0"){
//				$("#dialogIssuedInitDIV").empty();
				// 显示结果信息
				var ht = $("#operateSuccessId").clone();
				$("#dialogIssuedInitDIV").html('').append(ht);
			}else{
//				$("#dialogIssuedInitDIV").empty();
				// 显示结果信息
				var ht= $("#operateFaildId").clone();
				$("#dialogIssuedInitDIV").html('').append(ht);
				
			}
			$("#dialogIssuedInitDIV").dialog( "option", {
				buttons: [{
					text: $("#dialogConfirm").text(),
				    click: function(){removeDialog("#dialogIssuedInitDIV");}
				}]
			});
//			removeDialog("#dialogIssuedInitDIV");
		}
	});
}
/**
 * @author user3
 */
$(document).ready(function(){
	$("#paperName").focus();
	cherryValidate({						
		formId: 'mainForm',
		rules: {
			startDate: {dateValid: true},
			endDate: {dateValid: true}
		}
	});
	binOLMOCIO09_search($("#search").html());
	$("#addPaper").click(function(){
		var href = $('#addInit').html()+"?csrftoken=" + getTokenVal();
		windOpen(href);
		return false;
	});
});

function binOLMOCIO09_search(url,cleanMessageDiv){
	var url1= url?url:$("#search").html();
	setBtnsdisable();
	$("input[name='allSelect']").prop("checked",false);
	if(cleanMessageDiv == undefined){
		$("#actionResultDisplay").html("");
	}
	var $form = $("#mainForm");
	// 表单验证
	if(!$form.valid()){
		return;
	}	 
	 var params= $form.serialize();
	 url1 = url1 + "?" + params;
	 // 表格设置
	 var tableSetting = {
			 // 表格ID
			 tableId : '#dataTable',
			 // 数据URL
			 url : url1,
			 // 表格列属性设置
			// 表格默认排序
			 aaSorting : [[ 2, "asc" ]],
			 aoColumns : [{ "sName": "checkbox", "sWidth": "1%", "bSortable": false},
			              	{ "sName": "no.","sWidth": "5%","bSortable":false}, 			 // 0
							{ "sName": "paperName","sWidth": "25%"},					     // 1
							{ "sName": "paperRight","sWidth": "5%","sClass": "center"},	                     // 2
							{ "sName": "paperStatus","sWidth": "5%","sClass": "center"},                       // 3
							{ "sName": "startDate","sWidth": "5%","sClass": "center"},					     // 4
							{ "sName": "endDate","sWidth": "5%","sClass": "center"},	                         // 5
							{ "sName": "publisher","bVisible": false,"sWidth": "5%"},        // 6
							{ "sName": "publishTime","bVisible": false,"sWidth": "5%","sClass": "center"},      // 7
							{ "sName": "issuedStatus","sWidth": "5%","bSortable":false}],     // 8
			// 不可设置显示或隐藏的列	
			aiExclude :[0, 1,2],
			// 横向滚动条出现的临界宽度
			sScrollX : "100%",
			// 固定列数
			fixedColumns : 3
	 
	 };
	 // 调用获取表格函数
	 getTable(tableSetting);
};

function windOpen(url){
	var options = {
			fullscreen:"no",                    
			name : "",							// 弹出窗口的文件名
			height: 680,						// 窗口高度
			width: 720,						    // 窗口宽度
			top: 0,								// 窗口距离屏幕上方的象素值
			left: 0,							// 窗口距离屏幕左侧的象素值
			toolbar: "no",						// 是否显示工具栏，yes为显示
			menubar: "no",						// 是否显示菜单栏，yes为显示
			scrollbars: "yes",					// 是否显示滚动栏，yes为显示
			resizable: "no",					// 是否允许改变窗口大小，yes为允许
			location: "no",						// 是否显示地址栏，yes为允许
			status: "no",						// 是否显示状态栏内的信息（通常是文件已经打开），yes为允许
			center: "yes",						// 页面是否居中显示
			childModel: 1						// 弹出子页面的模式(1:只弹出一个子页面，2:可以弹出多个子页面，3:在一个窗口里切换页面)
		};
	popup(url,options);
}

function checkSelectAll(checkbox){
	$("#errorMessage").empty();
	$("#actionResultDisplay").empty();
	if($(checkbox).prop("checked")==true)
	{
		$("#dataTable_Cloned").find("input[name='validFlag']").prop("checked",true);
	}else
	{
		$("#dataTable_Cloned").find("input[name='validFlag']").prop("checked",false);
	}
	controlButton();
};

function checkSelect(checkbox){
	$("#errorMessage").empty();
	$("#actionResultDisplay").empty();
	var checked = "input[name='validFlag']:checked";
	if($(checked).length == $("#dataTable_Cloned").find("input[name='validFlag']").length){
		$("#dataTable_Cloned").prev().find("#checkAll").prop("checked",true);
	}else{
		$("#dataTable_Cloned").prev().find("#checkAll").prop("checked",false);
	}
	controlButton();
}

/**
 * 停用,编辑,复制三个按钮的有效与无效状态之间的转换实现函数
 * @author zgl
 * 
 */
function controlButton()
{
	var dom = $("#dataTable_Cloned").find("input[name='validFlag']:checked");
	if(dom.length == 0){
		setBtnsdisable();
	}else{
		if(dom.length == 1)
		{
			var val = dom[0].value.split("*");
			if(val[0] !="1")
			{
				$("#disableBtn").unbind("click"); 
				$("#disableBtn").click(function(){
					$("#errorMessage").empty();
					$("#actionResultDisplay").empty();
					var url = $("#disableCheckPaper").html();
					var param = "paperId="+dom[0].id+"&issuedStatus="+val[1]+"&csrftoken=" + getTokenVal();
					endableOrDisableConfirm("disable", url, param);
					return false;
					}
				);
			}else{
				$("#disableBtn").click(function(){
					$("#errorMessage").empty();
					$("#actionResultDisplay").empty();
					$("#errorMessage").html($("#errorMessageTemp2").html());
					return false;
				});
			}
			if(val[0] !="2")
			{
				$("#enableBtn").unbind("click"); 
				$("#enableBtn").click(function(){
					$("#errorMessage").empty();
					$("#actionResultDisplay").empty();
						var url = $("#enableCheckPaper").html();
						var param = "paperId="+dom[0].id+"&issuedStatus="+val[1]+"&csrftoken=" + getTokenVal();
						endableOrDisableConfirm("enable", url, param);
						return false;
					}
				);
			}else{
				$("#enableBtn").click(function(){
					$("#errorMessage").empty();
					$("#actionResultDisplay").empty();
					$("#errorMessage").html($("#errorMessageTemp2").html());
					return false;
				});
			}
		}else
		{
			$("#disableBtn").unbind("click");
			$("#disableBtn").click(function(){
				$("#errorMessage").empty();
				$("#actionResultDisplay").empty();
				$("#errorMessage").html($("#errorMessageTemp3").html());
				return false;
			});
			$("#enableBtn").unbind("click");
			$("#enableBtn").click(function(){
				$("#errorMessage").empty();
				$("#actionResultDisplay").empty();
				$("#errorMessage").html($("#errorMessageTemp3").html());
				return false;
			});
		}
		var paperIdArray = [];
		$("#deleteBtn").unbind("click"); 
		$("#deleteBtn").click(function(){
			$("#errorMessage").empty();
			$("#actionResultDisplay").empty();
				var url = $("#deleteCheckPaper").html();
				var paperIdStr = JSON2.stringify(paperIdArray);
				var param = "paperIdStr="+paperIdStr+"&csrftoken=" + getTokenVal();
				endableOrDisableConfirm("delete", url, param);
				return false;
			}
		);
		for(var i = 0;i < dom.length; i++)
		{
			var val = dom[i].value.split("*");
			if(val[0] == "2" || val[1] == "1")
			{
				$("#deleteBtn").unbind("click");  
				$("#deleteBtn").click(function(){
					$("#errorMessage").empty();
					$("#actionResultDisplay").empty();
					$("#errorMessage").html($("#errorMessageTemp2").html());
						return false;
				} 
						);
				break;
			}else
			{
				var o = {
						paperId:dom[i].id
				};
				paperIdArray.push(o);
			}
		}
	}
}

function endableOrDisableConfirm(flag, url, param) {
	if(flag == 'enable') {
		var title = $('#enableTitle').text();
		var text = $("#enableWarn").html();
	} else if(flag == 'disable'){
		var title = $('#disableTitle').text();
		var text = $('#disableWarn').html();
	}else{
		var title = $('#deleteTitle').text();
		var text = $('#deleteWarn').html();
	}
	var dialogSetting = {
		dialogInit: "#dialogInit",
		text: text,
		width: 	500,
		height: 300,
		title: 	title,
		confirm: $("#dialogConfirm").text(),
		cancel: $("#dialogCancel").text(),
		confirmEvent: function(){endableOrDisable(url, param);removeDialog("#dialogInit");},
		cancelEvent: function(){removeDialog("#dialogInit");}
	};
	openDialog(dialogSetting);
}

function endableOrDisable(url,param){
	cherryAjaxRequest({
		url: url,
		param: param,
		callback:function(msg) {
			if (msg.indexOf('actionMessage"') > -1) {
				binOLMOCIO09_search($("#search").html(),"a");
				setBtnsdisable();
			}
	}
	});
}

function setBtnsdisable(){
	$("#paperOption a").each(function(){
		$(this).unbind("click");
		$(this).click(function(){
			$("#errorMessage").empty();
			$("#actionResultDisplay").empty();
			$("#errorMessage").html($("#errorMessageTemp1").html());
			return false;
		});
	});
}

/**
 * 编辑考核问卷
 * 
 * */
function cio09EditCheckPaper(obj){
	var paperId = obj.id;
	var editHref = $("#editInit").html()+"?paperId="+paperId+"&csrftoken=" + getTokenVal();
	$("#errorMessage").empty();
	$("#actionResultDisplay").empty();
	windOpen(editHref);
	return false;
}

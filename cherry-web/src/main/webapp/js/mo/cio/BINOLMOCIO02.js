/**
 * @author zgl
 */

var BINOLMOCIO03_Tree = null;

$(document).ready(function(){
	$("#paperName").focus();
	cherryValidate({						
		formId: 'mainForm',
		rules: {
			startDate: {dateValid: true},
			endDate: {dateValid: true}
		}
	});
//	$("#disableBtn,#copyBtn").click(function()
//			{
//				return false;
//			}
//	);
	$("#addPaper").click(function(){
		var href = $('#addInit').html()+"?csrftoken=" + $('#csrftoken').val();
		windOpen(href);
		return false;
	});
	
	binOLMOCIO02_search($('#searchUrl').html());
});

/**
 * 问卷操作，问卷停用，问卷启用，问卷删除
 * 
 * 
 * */
function paperOption(checkedPaper,optType){
	var param = "checkedPaperIdArr="+checkedPaper + "&csrftoken=" + $('#csrftoken').val();
	if(optType == "1" || optType == "2"){
		var url = $("#paperDisableOrEnable").html();
	}else{
		var url = $("#paperDelete").html();
	}
	var opt = {
			url:url,
			param:param,
			callback : function(msg) {
				if (msg.indexOf('actionMessage"') > -1) {
					binOLMOCIO02_search($("#search").html(),"a");
					//disabPapOptBtn();
				}
			}
	};
	cherryAjaxRequest(opt);
}
/**
 * @author menghao
 * @param optType:操作类型（1：停用，2：启用）
 * 		  thisObj
 */
function popOptPaperDialog(optType,thisObj) {
	$("#errorMessage").empty();
	$("#actionResultDisplay").empty();
	var _this = thisObj;
	var _id = $(_this).parent().find("input").attr("id");
	var _value = $(_this).parent().find("input").val();
	var _thisArr = _value.split("*");
	var paperStatus = _thisArr[0];//问卷当前状态（1：停用，2：启用）
	var issuedStatus = _thisArr[1];//是否已发布（0：否，1：是）
	var paperType = _thisArr[2];//问卷类型（0：普通问卷，1：会员问卷，2：商场问卷）
	var checkedArray = [];
	if(optType == "1" || optType == "2"){//停用与启用的情况
		var obj = {
				paperId:_id,
				isIssued:issuedStatus,
				paperStatus:optType   //1：停用，2：启用
		};
		checkedArray.push(obj);
	}else{//删除的情况
		var obj = {
				paperId:_id
		};
		checkedArray.push(obj);
	}
	var checkedPaperIdArr = JSON2.stringify(checkedArray);
	if(optType=="2") {
		var title = $('#enableTitle').text();
		var text = '<p class="message"><span>'+BINOLMOCIO02_js_i18n.confirIsEnable;
	} else if(optType=="1"){
		var title = $('#disableTitle').text();
		var text = '<p class="message"><span>'+BINOLMOCIO02_js_i18n.confirIsDisable;
	}else{
		var title = $('#deleteTitle').text();
		var text = '<p class="message"><span>'+BINOLMOCIO02_js_i18n.confirIsDelete;
	}
	var dialogSetting = {
		dialogInit: "#dialogInit",
		text: text,
		width: 	500,
		height: 300,
		title: 	title,
		confirm: $("#dialogConfirm").text(),
		cancel: $("#dialogCancel").text(),
		confirmEvent: function(){paperOption(checkedPaperIdArr,optType);removeDialog("#dialogInit");},
		cancelEvent: function(){removeDialog("#dialogInit");}
	};
	openDialog(dialogSetting);
	
}

//function setBtnsdisable(){
//	$("#disableBtn").unbind("click"); 
//	$("#copyBtn").unbind("click"); 
//}

/**
 * 弹出问卷详细
 * @param obj
 */
function popupWind(obj){
	windOpen($(obj).attr("href") + "&csrftoken=" + getTokenVal());
}

function windOpen(url,option){
	var options;
	if(!option){
		options = {
				fullscreen:"no",                    
				name : "",							// 弹出窗口的文件名
				height: 680,						// 窗口高度
				width: 800,						    // 窗口宽度
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
	}else{
		options = {
				fullscreen: option.fullscreen? option.fullscreen:"no",                    
				name : option.name? option.name:"",								// 弹出窗口的文件名
				height: option.height? option.height:680,						// 窗口高度
				width: option.width? option.width:800,						    // 窗口宽度
				top: option.top? option.top:0,									// 窗口距离屏幕上方的象素值
				left: option.left? option.left:0,								// 窗口距离屏幕左侧的象素值
				toolbar: option.toolbar? option.toolbar:"no",					// 是否显示工具栏，yes为显示
				menubar: option.menubar? option.menubar:"no",					// 是否显示菜单栏，yes为显示
				scrollbars: option.scrollbars? option.scrollbars:"yes",			// 是否显示滚动栏，yes为显示
				resizable: option.resizable? option.resizable:"no",				// 是否允许改变窗口大小，yes为允许
				location: option.location? option.location:"no",				// 是否显示地址栏，yes为允许
				status: option.status? option.status:"no",						// 是否显示状态栏内的信息（通常是文件已经打开），yes为允许
				center: option.center? option.center:"yes",						// 页面是否居中显示
				childModel: option.childModel? option.childModel:1				// 弹出子页面的模式(1:只弹出一个子页面，2:可以弹出多个子页面，3:在一个窗口里切换页面)
			};
	}
	
	popup(url,options);
}

/**
 * 查询
 * @author zgl
 * 
 */
function binOLMOCIO02_search(url,cleanMessageDiv){
	var url1 = url?url:$("#search").html();
	//disabPapOptBtn();
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
			 aoColumns : [//{ "sName": "checkbox", "sWidth": "1%", "bSortable": false},
			              	{ "sName": "no.","sWidth": "5%","bSortable":false,"sClass": "center"}, 			 // 0
							{ "sName": "paperName","sWidth": "25%"},					     // 1
							{ "sName": "paperType","sWidth": "5%","sClass": "center"},	                     // 2
							{ "sName": "startTime","sWidth": "5%","sClass": "center"},
							{ "sName": "endTime","sWidth": "5%","sClass": "center"},// 4
							{ "sName": "publishTime","sWidth": "5%","sClass": "center"},	                         // 5
							{ "sName": "publisher","sWidth": "5%"},
							{ "sName": "paperStatus","sWidth": "5%","sClass": "center"},
							{ "sName": "operate","sWidth": "25%","bSortable":false}
							],       // 6
							
			// 不可设置显示或隐藏的列	
			aiExclude :[0,1,2],
			// 横向滚动条出现的临界宽度
			sScrollX : "100%",
			// 固定列数
			fixedColumns : 3
	 };
	 // 调用获取表格函数
	 getTable(tableSetting);
};

/**
 * 问卷操作诸按钮置无效
 * 
 * 
 */
//function disabPapOptBtn(){
//	$("#paperOption a").each(function(){
//		$(this).unbind("click");
//		$(this).click(function(){
//			$("#errorMessage").empty();
//			$("#actionResultDisplay").empty();
//			$("#errorMessage").html($("#errorMessageTemp1").html());
//			return false;
//		});
//	});
//}

/**
 * 查看发布
 */
function cio02PopRegion(obj){
	var dialogSetting = {
			dialogInit: "#region",
			width: 450,
			height: "auto",
			minHeight:550,
			title: 	$("#doRegion").text(),
			confirm: $("#dialogClose").text(),
			confirmEvent: function(){removeDialog("#region");}
		};
	openDialog(dialogSetting);
	var demoHtml = [];
	demoHtml.push('<div class="jquery_tree">');
	demoHtml.push('<div class="box2-header clearfix">');
	demoHtml.push('<strong class="left active"><span class="ui-icon icon-flag2"></span>'+$("#publisCounter").text()+'</strong>');
	demoHtml.push('<span class="right"><input id="position_left_0" class="text locationPosition ac_input" type="text" style="width: 160px;" name="locationPosition" autocomplete="off"><a class="search" onclick="cio03LocationCutPosition(this);return false;"><span class="ui-icon icon-position"></span><span class="button-text">定位</s:text></span></a></span>');
	demoHtml.push('</div>');
	demoHtml.push('<div class="jquery_tree treebox_left ztree" id = "treeDemo" style="overflow: auto;background:#FCFCFC;min-height:400px;max-height:700px;height:auto">');
	demoHtml.push('</div>');
	demoHtml.push('</div>');
	$("#region").html(demoHtml.join(''));
	
	counterBinding({
		elementId:"position_left_0",
		showNum:20,
		selected:"codeName"
	});
	
	$("#position_left_0").bind("keydown",function(event){
		if(event.keyCode==13){
			cio03LocationCutPosition(this,"keydown");
        }
	});
	loadTree(obj);
}
/**
 * 加载树
 * @param obj
 */
function loadTree(obj){
	
	var treeSetting = {
			data: {
				key:{
					children: "nodes",
					name:"name"
				},
				simpleData:{
					idKey: "id"
				}
			}
	};
	var url = $("#getPaperIssum").html();
	var param = "paperId="+obj.id; //+ "&csrftoken=" + getTokenVal();
	var callback = function(msg){
		if(msg.indexOf('id="actionResultDiv"') > -1){
			removeDialog("#region");
		}else{
			var nodes = window.JSON2.parse(msg);
			BINOLMOCIO03_Tree = $.fn.zTree.init($("#treeDemo"), treeSetting, nodes);
		}
	};
	cherryAjaxRequest(
			{
				url:url,
				param:param,
				callback:callback
			}
	);
	
}

function cio03LocationCutPosition(obj,flag){
	if(typeof flag == "undefined"){
		var $input = $(obj).prev();
	}else{
		var $input = $(obj);
	}
	var inputNode = BINOLMOCIO03_Tree.getNodeByParam("name",$input.val());
	BINOLMOCIO03_Tree.expandNode(inputNode,true,false);
	BINOLMOCIO03_Tree.selectNode(inputNode);
}

/**
 * 问卷下发
 * 
 * */
function cio02IssuedPaper(obj){
	var _this = obj;
	var _thisIdArr = _this.id.split("*");
	var paperId = _thisIdArr[0];
	var isIssued = _thisIdArr[1];
	var paperType = _thisIdArr[2];
	var issuedHref = $("#issuedInit").html()+"?paperId="+paperId + "&csrftoken=" + getTokenVal()+"&isIssued="+isIssued;
	
	$("#errorMessage").empty();
	$("#actionResultDisplay").empty();
	if(paperType == "1"){
		var url = $("#isExistSomePaper").html();
		var param = "paperId="+paperId;
		var callback = function(msg){
			var msgObj = eval('('+msg+')');
			if((msgObj.length == 1 && msgObj[0].paperId != paperId) || msgObj.length > 1){
				$("#errorMessage").html($("#existMember").html());
			}else{
				windOpen(issuedHref,{
					height: 700,
					width: 700
				});
				return false;
			}
		};
		cherryAjaxRequest({url:url,
			param:param,
			callback:callback
			});
	}else{
		windOpen(issuedHref,{
			height: 700,
			width: 700
		});
		return false;
	}
}

/**
 * 问卷编辑或者复制
 * @param optType:
 * 				0：编辑
 * 				1：复制
 * 
 * */
function cio02EditOrCopyPaper(editFlag,obj){
	var _this = obj;
	var paperId = $(_this).parent().find("input").attr("id");
	var editOrCopyHref = $("#editOrCopyInit").html() + "?paperId=" + paperId
			+ "&editFlag=" + editFlag + "&csrftoken=" + getTokenVal();
	$("#errorMessage").empty();
	$("#actionResultDisplay").empty();
	windOpen(editOrCopyHref);
	return false;
}

/**
 * 问卷过滤
 * @return
 */
function prmActDateFilter (filterType,thisObj){
	
	$('#ui-tabs').find('li').removeClass('ui-tabs-selected');
	$(thisObj).addClass('ui-tabs-selected');
	// 数据过滤
	oTableArr[0].fnFilter(filterType);

	$('#th_operator').show();
}
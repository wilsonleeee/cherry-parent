function BINOLMOCIO07(){};

BINOLMOCIO07.prototype={

"tree" : null,

/**
 * 单位类型过滤
 * 
 */
"typeDateFilter" : function(type,thisObj) {
	this.clearActionHtml();
	$('#ui-tabs').find('li').removeClass('ui-tabs-selected');
	$(thisObj).addClass('ui-tabs-selected');
	// 数据过滤
	oTableArr[1].fnFilter(type);
	// 页面上的单位类型设置为当前模式的单位类型
	$("#mainForm #type").val(type);
},

//用户查询
"search":function(msg){
	this.clearActionHtml();
	if(typeof msg != "undefined"){
		$("#actionResultDisplay").html(msg);
		$("#actionResultDisplay").show();
	}else{
		$("#actionResultDisplay").empty();
	}
	var $form = $('#mainForm');
	if (!$form.valid()) {
		return false;
	};

	var url = $("#searchUrl").attr("href");
	 // 查询参数序列化[包含了token值]
	var params= $form.serialize();
	 url = url + "?" + params + "&" + getRangeParams();
	 // 显示结果一览
	 $("#section").show();
	 // 表格设置
	 var tableSetting = {
			 index : 1,
			 // 表格ID
			 tableId : '#dataTable',
			 // 表格默认排序
			 aaSorting : [[ 2, "desc" ]],
			 // 数据URL
			 url : url,
			 // 表格列属性设置			 
			 aoColumns :    [{ "sName": "no","bSortable": false},			// 0
			                 { "sName": "different","bVisible": false},		// 1
							 { "sName": "parameter"},						// 2
							 { "sName": "type","bVisible": false},			// 3
							 { "sName": "targetDate","sClass": "center"},	// 4
							 { "sName": "targetType","bVisible": false},	// 5
							 { "sName": "activity","bVisible": false},		// 6
							 { "sName": "targetQuantity","sClass": "alignRight"},// 8
							 { "sName": "targetMoney","sClass": "alignRight"},	// 7
							 { "sName": "synchroFlag","sClass": "center"},// 9
							 { "sName": "source","sClass": "center","bVisible": false},// 10
							 { "sName": "targetSetTime","sClass": "center","bVisible": false},// 11
							 { "sName": "parameterID","bSortable":false}// 12
							 ],  
			 // 不可设置显示或隐藏的列	
		   	aiExclude :[0, 2],
			// 横向滚动条出现的临界宽度
			sScrollX : "100%",
			callbackFun : function (){
	        $('#allSelect').prop("checked", false);
	 		}
	 };
	 // 调用获取表格函数
	 getTable(tableSetting);
},

"exportExcel" : function(url) {
	this.clearActionHtml();
	//无数据不导出
	if($(".dataTables_empty:visible").length==0){
		if (!$('#mainForm').valid()) {
	        return false;
	    };
		var params= $("#mainForm").serialize();
		params = params  + "&" + getRangeParams();
		
    	var callback = function(msg) {
		    url = url + "?" + params;
		    window.open(url,"_self");
	    }
    	// 先校验查询到的目标设定数据
	    exportExcelReport({
    		url: $("#exporCheckUrl").attr("href"),
    		param: params,
    		callback: callback
    	});
	}
},

/**
 * 清理执行结果信息 
 */
"clearActionHtml" : function() {
	$("#errorDiv2 #errorSpan2").html("");
	$("#errorDiv2").hide();
	$("#errorMessage").empty();
	$("#actionResultDisplay").empty();
},


/**
 * 弹出编辑dialog
 * 
 * 
 */
"showEditDialog":function (object) {
	this.clearActionHtml();
    var _$this = $(object);
    // 查询页面上的目标日期（或月或日）
    var targetDateSearch = '';
    if($("[name='targetDateType']:checked").val()=="month") {
    	targetDateSearch = $("#mainForm #targetMonth").val();
	} else if($("[name='targetDateType']:checked").val()=="day") {
		targetDateSearch = $("#mainForm #targetDay").val();
	}
    var targetDate = _$this.nextAll("div").find("#saleTargetDateArr").val()? _$this.nextAll("div").find("#saleTargetDateArr").val():targetDateSearch;
    var targetType = _$this.nextAll("div").find("#targetType").val() ? _$this.nextAll("div").find("#targetType").val() : $("#mainForm #targetType").val();
    
    var name = _$this.nextAll("div").find("#nameArr").val();
    var activityCode = _$this.nextAll("div").find("#activityCode").val();
    var activityName = _$this.nextAll("div").find("#activityName").val();
    
    var param = 'type=' + $("#mainForm #type").val();
	param += "&"+_$this.nextAll("div").find("#parameterArr").serialize();
	param += "&"+_$this.nextAll("div").find("#differentArr").serialize();
	param += "&"+_$this.nextAll("div").find("#nameArr").serialize();
	param += "&targetDateType="+_$this.nextAll("div").find("#targetDateTypeArr").val();

	$("#editSaleTarget").dialog( {
		resizable : false,
		modal : true,
		title: 	$("#setTitle").text(),
		height : 350,
		width : 400,
		zIndex: 30,  
		buttons: [
		    {
		    	text: $("#dialogConfirm").text(),
		    	click: function(){BINOLMOCIO07.mocioconfirm($("#setTarget").attr("href"),param);}
		    },
		    {
		    	text: $("#dialogCancel").text(),
		    	click: function(){closeCherryDialog('editSaleTarget',dialogBody);}
		    }],
		    close: function(){closeCherryDialog('editSaleTarget',dialogBody);}
	});
	$("#editTargetMoney").val("");
	$("#editTargetQuantity").val("");
	$("#parameterName").val(name).prop("disabled","disabled");
	$("#editSaleTarget #editTargetDate").val(targetDate).prop("disabled","disabled");
	$("#editSaleTarget #targetType").val(targetType);
	$("#editSaleTarget #campaignCode").val(activityCode);
	$("#editSaleTarget #campaignName").val(activityName);
	
	if(activityName && activityName != '') {
		var $campaignDiv = $("#editSaleTarget #campaignDiv");
		var html = activityName;
		// 用于插入销售目标设定表的活动名称
		$("#editSaleTarget #campaignName").val(html);
		html += '<span class="close" style="margin: 0 10px 2px 5px;" onclick="BINOLMOCIO07.delCampaignHtml(this);"><span class="ui-icon ui-icon-close" style="background-position: -80px -129px;"></span></span>';
		$campaignDiv.html(html);
	}
	cherryValidate({
		formId: 'cherryEdit',	
		rules: {
		editBrandInfoId:	{required: true},
		editTargetMoney: {floatValid: [9,2]},
		editTargetQuantity: {digits:true},
		targetType : {required:true}
		}	
	});
},

/**
 * 修改销售目标确认
 */
"mocioconfirm":function(url,param){
	if(!$('#cherryEdit').valid()) {
		return false;
	}
	var callback = function(msg){
		closeCherryDialog('editSaleTarget',dialogBody);
		BINOLMOCIO07.search(msg);
	};
	param += "&" + $("#cherryEdit").serialize();
	param += "&editTargetDate="+$("#editTargetDate").val();
	// cherryAjaxRequest共通方法中已经包含了此参数
//	param += "&csrftoken=" +$("#csrftoken").val();
	cherryAjaxRequest({
		url: url,
		param: param,
		formId: '#mainForm',
		callback: callback
	});	
},
/**
 * 下发弹出框
 */
"popDownDiglog":function(){
	this.clearActionHtml();
	var title = $('#makeSureTitle').val();
	var text = '<p class="message"><span>'+$("#makeSureContext").val()+'</span></p>';
	var that = this;
	var dialogSetting = {
			dialogInit:"#dialogInit",
			text:text,
			width:500,
			height:300,
			title:title,
			confirm: $("#dialogConfirm").text(),
			cancel: $("#dialogCancel").text(),
			confirmEvent: function(){that.down();},
			cancelEvent: function(){removeDialog("#dialogInit");}
		};
	openDialog(dialogSetting);
},

/**
 * 下发
 */
"down":function(){
	var callback = function(msg){
		$("#actionResultDisplay").html(msg);
		$("#actionResultDisplay").show();
		removeDialog("#dialogInit");
		BINOLMOCIO07.search(msg);
	};
	url=$("#down").attr("href");
	param=$("#brandInfoId").serialize();
	param += "&type="+$("#type").val();
	param += "&parameter="+$("#parameter").val();
	param += "&targetDate="+$("#targetDate").val();
	
	if($("[name='targetDateType']:checked").val()=="month") {
		param += "&"+$("#targetMonth").serialize();
	} else if($("[name='targetDateType']:checked").val()=="day") {
		param += "&"+$("#targetDay").serialize();
	}
	
	// 目标只下发产品类型的销售目标
	param += "&targetType="+$("#targetType").val();
	param += "&campaignCode="+$("#campaignCode").val();
	param += "&campaignName="+$("#campaignName").val();
	// cherryAjaxRequest共通方法中已经有此参数
//	param += "&csrftoken=" +$("#csrftoken").val();
	param += "&" + getRangeParams();
	cherryAjaxRequest({
		url : url,
		param : param,
		callback : callback
	});	
},

"checkSelectAll":function(checkbox){
	this.clearActionHtml();
	$('#dataTable').find(":checkbox").prop("checked", $(checkbox).prop("checked"));
	if ($(checkbox).prop("checked") && $("input@[id=validFlag][checked]").length>0){
	}
},

"checkSelect":function (checkbox){
	this.clearActionHtml();
	if($(checkbox).prop("checked")) {
        if($("#dataTable input@[id=validFlag]").length == $("#dataTable input@[id=validFlag][checked]").length) {
            $("input@[id=allSelect]").prop("checked",true);
        }
    } else {
        $("input@[id=allSelect]").prop("checked",false);
    }
},

/*
 * 验证勾选，显示隐藏错误提示
 */
"validCheckBox":function(){
	var flag = true;
	var checksize = $("input@[id=validFlag][checked]").length;
	if (checksize == 0){
	    //没有勾选
        $('#errorDiv2 #errorSpan2').html($('#errmsg1').val());
        $('#errorDiv2').show();
        flag = false;
	}else{
		if($('#errorDiv2 #errorSpan2').text()==$('#errmsg1').val()){
			$('#errorDiv2').hide();
		}
	}
	return flag;
},

/**
 * 新增销售目标确认
 */
"addconfirm":function(url){
	if(!$('#cherrytargetadd').valid()) {
		return false;
	}
	var callback = function(msg){
		closeCherryDialog('pop_target_body',addBody);
		BINOLMOCIO07.search(msg);
	};
	var checked = [];
	var nodes = this.tree.getCheckedNodes();
	for(var i in nodes){
		if(nodes[i].nodes == undefined){
			var o={
					parameterArr:nodes[i].parameterArr,
					differentArr:nodes[i].differentArr,
					nameArr:nodes[i].nameArr,
					categoryCodeArr:nodes[i].categoryCodeArr
			};
			checked.push(o);
		}
	}	
	var param=$("#addBrandInfoId").serialize();
	param += "&"+$("#addtype").serialize();
//	param += "&"+$("#addTargetMonth").serialize();
//	param += "&"+$("#addTargetDay").serialize();
	param += "&"+$("#addTargetMoney").serialize();
	param += "&"+$("#addTargetQuantity").serialize();
	param += "&"+$("#cherrytargetadd #targetType").serialize();
	param += "&"+$("#cherrytargetadd #campaignCode").serialize();
	param += "&"+$("#cherrytargetadd #campaignName").serialize();
	param += "&addpop="+JSON2.stringify(checked);
	
	if($("[name='addTargetDateType']:checked").val()=="month") {
		param += "&"+$("#addTargetMonth").serialize() + "&targetDateType=1";
	} else if($("[name='addTargetDateType']:checked").val()=="day") {
		param += "&"+$("#addTargetDay").serialize() + "&targetDateType=2";
	}
	// cherryAjaxRequest共通方法中已经有此参数
//	param += "&csrftoken=" +$("#csrftoken").val();
	cherryAjaxRequest({
		url: url,
		param: param,
		callback: callback
	});	
},

/**
 * 弹出新增销售目标弹出框
 */
"popSetSaleTargetDialog":function(){
	this.clearActionHtml();
	// 页面上的单位类型已经隐藏，改用tab标签来切换
	var formType = $("#mainForm #type").val();
	var formDate = $("#mainForm #targetDate").val();
	var formTargetType = $("#mainForm #targetType").find("option:selected").val();
	
	var that = this;
	var dialogSetting = {
			bgiframe: true,
			width:500, 
			height:700,
			zIndex: 9999,
			modal: true, 
			resizable: false,
			title:$("#addSaleTargetTitle").text(),
			buttons : [ 
			    {
			    	text: $("#dialogConfirm").text(),
			    	click : function(){BINOLMOCIO07.addconfirm($("#setTarget").attr("href"));}
			    },
			    {	
			    	text: $("#dialogCancel").text(),
			    	click:function(){
			    		closeCherryDialog('pop_target_body',addBody);
			    		}
			    }],
			close:function(){closeCherryDialog('pop_target_body',addBody);}
		};
	$('#pop_target_body').dialog(dialogSetting);
	
	$("#pop_target_body #addTargetDate").val(formDate);
	$("#pop_target_body #addtype").val(formType);
	$("#pop_target_body #targetType").val(formTargetType);
	
	BINOLMOCIO07.getTreeNodes();
	
	cherryValidate({
		formId: 'cherrytargetadd',	
		rules: {
			addBrandInfoId:{required: true},
			addTargetMonth: {dateYYYYmm:true},
			addTargetDay:{dateYYYYmmDD:true},
			addTargetMoney: {floatValid: [9,2]},
			addTargetQuantity: {digits:true}
		}
	});
	
	// 切换目标日期类型（month:月目标；day:日目标）
	$("[name='addTargetDateType']").live('click',function(){
		if($("[name='addTargetDateType']:checked").val()=="month"){
			// 月目标
			$("#addTargetDay").prop("disabled",true);
			$("#addTargetMonth").prop("disabled",false);
			
			$("#addTargetDay").parent().find("#errorText").remove();
            $("#addTargetDay").parent().removeClass("error");
            
			$("#addTargetMonth").rules("add", {dateYYYYmm:true});
			$("#addTargetDay").rules("remove", "dateYYYYmmDD");
			// 重新获取树，需要显示已经设置的目标
			BINOLMOCIO07.getTreeNodes();
		}else if($("[name='addTargetDateType']:checked").val()=="day"){
			// 日目标
			$("#addTargetDay").prop("disabled",false);
			$("#addTargetMonth").prop("disabled",true);
			
			$("#addTargetMonth").parent().find("#errorText").remove();
            $("#addTargetMonth").parent().removeClass("error");
            
            $("#addTargetDay").rules("add", {dateYYYYmmDD:true});
			$("#addTargetMonth").rules("remove", "dateYYYYmm");
			// 重新获取树，需要显示已经设置的目标
			BINOLMOCIO07.getTreeNodes();
		}
    });
},

/**
 * 取得树结点
 */
"getTreeNodes":function(){
	$("#position_left_0").val("");
	var that = this;
	var url = $("#getTreeNodes").html();
	var param =$("#addBrandInfoId").serialize();
	param += "&" + $("#addtype").serialize() + "&targetType="
			+ $("#pop_target_body #targetType").find("option:selected").val();
	
	if($("[name='addTargetDateType']:checked").val()=="month") {
		param += "&" + $("#addTargetMonth").serialize() + "&targetDateType=1";
	} else if($("[name='addTargetDateType']:checked").val()=="day") {
		param += "&" + $("#addTargetDay").serialize() + "&targetDateType=2";
	}
	var callback = function(msg){
			that.loadTree(msg);
		};

	cherryAjaxRequest( {
		url : url,
		param : param,
		callback : callback
	});

	if ($("#addtype option:selected").val() == '1') {
		departBinding({
			elementId : "position_left_0",
			showNum : 20,
			selected : "name",
			privilegeFlag : true
		});
	} else if ($("#addtype option:selected").val() == '2') {
		departBinding({
			elementId : "position_left_0",
			showNum : 20,
			selected : "name",
			includeCounter : true,
			privilegeFlag : true
		});
	} else if ($("#addtype option:selected").val() == '3') {
		employeeBinding({
			elementId : "position_left_0",
			showNum : 20,
			selected : "name"
		});
	}
	
},

/**
 * 定位
 */
"locationPosition":function(obj,flag){
	if(typeof flag == "undefined"){
		var $input = $(obj).prev();
	}else{
		var $input = $(obj);
	}
	var inputNodes = this.tree.getNodesByParamFuzzy("name",")"+$input.val());
	this.tree.expandNode(inputNodes[0],true,false);
	this.tree.selectNode(inputNodes[0]);
},

/**
 * 加载树
 */
"loadTree" : function(nodes) {
	var treeNodes = eval("(" + nodes + ")");
	
	/**
	 * 将已经下发的销售目标单位显示为绿色
	 * @param treeId
	 * @param treeNode
	 * @returns
	 */
	var setFontCss = function(treeId, treeNode) {
		return treeNode.synchroFlag == '1' ? {color:"#009900"} : {};
	};
	var treeSetting = {
		checkable : true,
		showLine : true,
		fontCss : setFontCss
	};
	this.tree = null;
	this.tree = $("#treeDemo").zTree(treeSetting, treeNodes);
},

/**
 * 弹出导入销售目标选择框
 */
"popImportSaleTargetDialog" : function() {
	this.clearActionHtml();
	var dialogId="popImport_target_main";
	var dialogBody="popImport_target_body";
	var $dialog = $("#"+dialogId);
	var that = this;
	$dialog.dialog({
			bgiframe: true,
			width:850, 
			height:450,
			zIndex: 9999,
			modal: true, 
			resizable: false,
			title:$("#ImportSaleTargetTitle").text(),
			buttons : [ 
			    {	
			    	text: $("#dialogClose").text(),
			    	click:function(){
			    			that.deleteActionMsg();
			    			closeCherryDialog(dialogId);
			    			that.search();
			    		}
			    }],
			close:function(){
					that.deleteActionMsg();
					closeCherryDialog(dialogId);
					that.search();
				}
	});
	$dialog.dialog("open");
},
/**
 * 清除Action信息
 */
"deleteActionMsg" : function() {
	//清理错误信息提示区
	$("#errorMessageImport").empty();
	//清理导入错误明细区
	$("#errorTargets").empty();
	$("#errorTargetsShow").hide();
},
/**
 * ajax文件导入
 */
"ajaxFileUpload" : function(url) {
	$("#errorTargetsShow").hide();
	// AJAX登陆图片
	$ajaxLoading = $("#loading");
	// 错误信息提示区
	$errorMessage = $('#errorMessageImport');
	// 清空错误信息
	$errorMessage.empty();
	// 若未选择文件报错
	if ($('#upExcel').val() == '') {
		$("#pathExcel").val("");
		var errHtml = this.getErrHtml($('#errmsg2').val());
		$errorMessage.html(errHtml);
		return false;
	}
	$ajaxLoading.ajaxStart(function() {
		$(this).show();
		$("#upload").attr("disabled","disabled");
		// 禁用关闭按钮
		$("button[role='button']").attr("disabled","disabled");
		// 隐藏关闭按钮
		$(".ui-dialog-titlebar-close", $("#popImport_target_main").parent()).hide();
	});
	$ajaxLoading.ajaxComplete(function() {
		$(this).hide();
		$("#upload").removeAttr("disabled");
		// 使用关闭按钮
		$("button[role='button']").removeAttr("disabled");
		// 隐藏关闭按钮
		$(".ui-dialog-titlebar-close", $("#popImport_target_main").parent()).show();
	});
	$.ajaxFileUpload({
		url : url,
		secureuri : false,
		data : {
			'csrftoken' : $('#csrftoken').val(),
			'importBrandInfoId' : $('#importBrandInfoId').val()
		},
		fileElementId : 'upExcel',
		dataType : 'html',
		success : function(data) {
			$("#errorTargets").html("");
			if (data.indexOf("actionMessage") > -1) {// 完全成功
				$errorMessage.html(data);
			} else if (data.indexOf("actionError") > -1
					&& data.indexOf("odd") <= -1) {// 文件有误
				$errorMessage.html(data);
			} else if ($errorMessage.find(".actionSuccess").length > 0) {
				$errorMessage.html(data);
			} else {// 导入文件内容有误并显示出明细
				$("#errorTargetsShow").show();
				$("#hiddenTable").html(data);
				$errorMessage.html($("#hiddenTable #errorDiv").html());
				$("#errorTargets").html($("#hiddenTable tbody").html());
				$("#hiddenTable").html("");
			}
		}
	});
},
/**
 * 取得上传文件错误信息HTML
 */
"getErrHtml" : function (text){
	var errHtml = '<div class="actionError"><ul><li><span>';
	errHtml += text;
	errHtml += '</span></li></ul></div>';
	return errHtml;
},

//选择活动弹出框
"popCampaignList": function(url,obj) {
	var $campaignName = obj ? $(obj).parent().find("#campaignName") : $("#campaignName");
	var $campaignDiv = obj ? $(obj).parent().find("#campaignDiv") : $("#campaignDiv");
	var $campaignCode = obj ? $(obj).parent().find("#campaignCode") : $("#campaignCode");
	var callback = function(tableId) {
		var $checkedRadio = $("#"+tableId).find(":input[checked]");
		if($checkedRadio.length > 0) {
			$campaignCode.val($checkedRadio.val());
			var html = $checkedRadio.parent().next().text();
			// 用于插入销售目标设定表的活动名称
			$campaignName.val(html);
			html += '<span class="close" style="margin: 0 10px 2px 5px;" onclick="BINOLMOCIO07.delCampaignHtml(this);"><span class="ui-icon ui-icon-close" style="background-position: -80px -129px;"></span></span>';
			$campaignDiv.html(html);
		}
	}
	var value = $campaignCode.val();
	var params = "param=1&param2=CXHD";
	popDataTableOfCampaignList(url, params, value, callback);
},
// 删除选择的活动
"delCampaignHtml": function(obj) {
	var $input = $(obj).parent().parent();
	var $campaignName = $input.find("#campaignName");
	var $campaignDiv = $input.find("#campaignDiv");
	var $campaignCode = $input.find("#campaignCode");
	$campaignCode.val("");
	$campaignName.val("");
	$campaignDiv.empty();
}

};


var BINOLMOCIO07 = new BINOLMOCIO07();
$(document).ready(function(){
	
	$("[name='targetDateType']").live('click',function(){
		if($("[name='targetDateType']:checked").val()=="month"){
			$("#targetDay").prop("disabled",true);
			$("#targetMonth").prop("disabled",false);
			
			$("#targetDay").parent().find("#errorText").remove();
            $("#targetDay").parent().removeClass("error");
            // 查询参数无需限制
//			$("#targetMonth").rules("add", {dateYYYYmm:true});
//			$("#targetDay").rules("remove", "dateYYYYmmDD");
			
			BINOLMOCIO07.search();
		}else if($("[name='targetDateType']:checked").val()=="day"){
			$("#targetDay").prop("disabled",false);
			$("#targetMonth").prop("disabled",true);
			
			$("#targetMonth").parent().find("#errorText").remove();
            $("#targetMonth").parent().removeClass("error");
            // 查询参数无需限制
//          $("#targetDay").rules("add", {dateYYYYmmDD:true});
//			$("#targetMonth").rules("remove", "dateYYYYmm");
			
			BINOLMOCIO07.search();
		}
    });
	
	BINOLMOCIO07.search();
	$("#editButton").click(function() {
		showChoiceDialog();
	});
	$("#position_left_0").bind("keydown",function(event){
		if(event.keyCode==13){
			BINOLMOCIO07.locationPosition(this,"keydown");
        }
	});
	dialogBody = $('#editSaleTarget').html();
	addBody = $('#pop_target_body').html();
});

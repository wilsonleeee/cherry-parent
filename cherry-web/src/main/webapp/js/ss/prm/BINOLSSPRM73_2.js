function BINOLSSPRM7302() {
	
};

/**
 * 全局变量定义
 */
var binOLSSPRM7302_global = {};
// 发送树Tmp
binOLSSPRM7302_global.sendTreeTmp;
// 使用树Tmp
binOLSSPRM7302_global.useTreeTmp;

BINOLSSPRM7302.prototype = {
	/* 是否刷新一览画面 */
	"doRefresh" : false,
	
	"changeSpan":function(prefix, obj, clear) {
		var $othes = $("[id^='" + prefix + "']");
		$othes.hide();
		if (1 == clear) {
			$othes.find(':input').val('');
		}
		$('#' + prefix + $(obj).val()).show();
	},
	"changeZk":function(prefix, obj) {
		var $parent = $(obj).closest('div.rule_content');
		var $othes = $parent.find("[id^='" + prefix + "']");
		$othes.hide();
		$othes.find(':input').val('');
		if ($(obj).val()==1){
			$parent.find(".proContent").empty();
		}
		$parent.find('#' + prefix + $(obj).val()).show();
	},
	"changeCouponType":function(obj) {
		var value = $(obj).val();
		if(value == '9'){
			$(".sendCondType").show();
			$('#coupon-content').html("");
			$(".useCondType").hide();
		}else{
			maxContentNoCopy=maxContentNoCopy+1;
			$(".sendCondType").hide();
			$('#coupon-content').html($('#coupon-content' + value).html());
			$('#coupon-content').find('input[name=contentNo]').val(maxContentNoCopy);
			$(".useCondButton").show();
			$(".useCondButtonAll").hide();
			$('#useCondTypeCheckbox').prop("checked", false);
		}
		if (value == '5') {
			$('#coupon-content').find('#proContent').attr('id', 'proContent1');
		} else if (value == '2') {
			$('#coupon-content').find('#contentProduct').attr('id', 'contentProduct1');
		} else if(code == '3'){
			$('#coupon-content').find('#contentCampaign').attr('id', 'contentCampaign1');
		}
	},
	"changeFullFlag":function(obj){
		var value = $(obj).val();
		var $change = $(obj).next();
		if(value=='1'){
			$change.addClass('hide');
		}else{
			$change.removeClass('hide')
		}
	},
	"showErrorMsg":function(msg){
		binolssprm7302.showMessageDialog({
			message:msg,
			type:"MESSAGE"
		});
	},
	"showMessageDialog":function (dialogSetting){
		if(dialogSetting.type == "MESSAGE"){
			$("#messageContent").show();
			$("#successContent").hide();
			$("#messageContentSpan").text(dialogSetting.message);
		}else{
			$("#messageContent").hide();
			$("#successContent").show();
			$("#successContentSpan").text(dialogSetting.message);
		}
		var $dialog = $('#messageDialogDiv');
		$dialog.dialog({
			//默认不打开弹出框
			autoOpen: false,
			//弹出框宽度
			width: 400,
			//弹出框高度
			height: 220,
			//弹出框标题
			title:"错误提示框",
			//弹出框索引
			zIndex: 99,
			modal: true,
			resizable:false,
			//关闭按钮
			close: function() {
				closeCherryDialog("messageDialogDiv");
				if(typeof dialogSetting.focusEvent == "function") {
					dialogSetting.focusEvent();
				}
			}
		});
		$dialog.dialog("open");
		// 给确认按钮绑定事件
		$("#btnMessageConfirm").bind("click", function(){
			closeCherryDialog("messageDialogDiv");
			//binolssprm7302.messageConfirm(dialogSetting.focusEvent);
		});
	},
	//"messageConfirm":function (focusEvent){
	//	closeCherryDialog("messageDialogDiv");
		//if(typeof focusEvent == "function") {
		//	focusEvent();
		//}
	//},
	/*
	 * 校验柜台相关逻辑
	 * (1)按渠道指定柜台情况必须选择柜台，否则给予弹框提示（按渠道指定柜台情况下必须选择柜台）
	 * (2)渠道选择情况必须选择渠道，否则给予提示（渠道选择情况下必须选择渠道）
	 */
	"validCounter":function(conditionType){
		var cnt_select;
		var cnt_select_obj;
		if(conditionType == 1){
			cnt_select=$("#cnt_s_w_select").val();
			cnt_select_obj=$("#cnt_s_w_select");
		}else{
			cnt_select=$("#cnt_u_w_select").val();
			cnt_select_obj=$("#cnt_u_w_select");
		}
		if(cnt_select == "2"){//按渠道指定柜台情况
			//获取树的选中状态
			if(conditionType == 1){
				var nodes = binOLSSPRM7302_global.sendTreeTmp.getCheckedNodes(true);
				if(nodes == 0){//没有选中的情况，打开错误提示框
					cnt_select_obj.parents("div").show();
					binolssprm7302.showErrorMsg("购买门店中按渠道指定柜台情况下必须选择柜台");
					return false;
				}else{
					return true;
				}
			}else{
				var nodes = binOLSSPRM7302_global.useTreeTmp.getCheckedNodes(true);
				if(nodes == 0){//没有选中的情况，打开错误提示框
					cnt_select_obj.parents("div").show();
					binolssprm7302.showErrorMsg("购买门店中按渠道指定柜台情况下必须选择柜台");
					return false;
				}else{
					return true;
				}
			}
		}else if(cnt_select == "3"){//渠道选择
			if(conditionType == 1){
				var selectLength=$("#cnt_s_w_commonDiv li input:checked").length;
				if(selectLength == 0){
					cnt_select_obj.parents("div").show();
					binolssprm7302.showErrorMsg("购买门店中按渠道选择情况下必须选择渠道");
					return false;
				}else{
					return true;
				}
			}else{
				var selectLength=$("#cnt_u_w_commonDiv li input:checked").length;
				if(selectLength == 0){
					cnt_select_obj.parents("div").show();
					binolssprm7302.showErrorMsg("购买门店中按渠道选择情况下必须选择渠道");
					return false;
				}else{
					return true;
				}
			}
		}else{
			return true;
		}
	},
	/*
	 校验会员
	 (1)会员等级至少选择一个（会员等级情况下必须选择对应的会员等级）
	 */
	"validMember":function(conditionType){
		var mem_s_w_select=$("#mem_s_w_select").val();
		var mem_s_w_select_obj=$("#mem_s_w_select");
		if(mem_s_w_select == "2"){//会员等级
			var selectLength=$(".memberLevel li input:checked").length;
			if(selectLength == 0){
				mem_s_w_select_obj.parents("div").show();
				binolssprm7302.showErrorMsg("购买会员中按会员等级情况下必须选择对应的会员等级");
				return false;
			}else{
				return true;
			}
		}else{
			return true;
		}
	},
	/*
	 *校验产品
	 * （1）产品分类检验至少选择一个（产品分类情况下必须选择对应的产品等级）
	 */
	"validProduct":function(conditionType){
		var pro_select;
		var pro_select_obj;
		if(conditionType == 1){
			pro_select=$("#prt_s_w_productKbn").val();
			pro_select_obj=$("#prt_s_w_productKbn");
		}else{
			pro_select=$("#prt_u_w_productKbn").val();
			pro_select_obj=$("#prt_u_w_productKbn");
		}
		if(pro_select == "1"){//产品分类选择的情况
			//获取树的选中状态
			if(conditionType == 1){
				var nodes = $("#prt_s_w_typeDIV li input:checked").length;
				if(nodes == 0){//没有选中的情况，打开错误提示框
					pro_select_obj.parents("div").show();
					binolssprm7302.showErrorMsg("购买产品中按分类指定产品情况下必须选择对应的产品分类");
					return false;
				}else{
					return true;
				}
			}else{
				var nodes = $("#prt_u_w_typeDIV li input:checked").length;
				if(nodes == 0){//没有选中的情况，打开错误提示框
					pro_select_obj.parents("div").show();
					binolssprm7302.showErrorMsg("购买产品中按分类指定产品情况下必须选择对应的产品分类");
					return false;
				}else{
					return true;
				}
			}
		}else{
			return true;
		}
	},

	/*
	 *保存优惠券规则
	 */
	"saveRule" : function (){
		if(!binolssprm7302.validCounter(1) || !binolssprm7302.validMember() || !binolssprm7302.validProduct(1)){
			return false;
		}
		var zkFlag=0;
		$(".zkCoupon:visible").each(function(){
			var zkValue=$(this).val();
			//if (zkValue2==undefined || zkValue2=="" || zkValue2==null){
			//	if (!$("#zkValue").val()){
			//		binolssprm7302.showErrorMsg("折扣券折扣率不能为空");
			//		return false;
			//	}
			//}
			if (zkValue==undefined || zkValue=="" || zkValue==null){
					binolssprm7302.showErrorMsg("折扣券折扣率不能为空");
					zkFlag=1;
			}
		});

		if(zkFlag == 1){
			return false;
		}

		//if ($('#couponFlag option:selected') .val()=='5'){
		//	var zkValue=$("#zkValue").val();
		//	var zkValue2=$("#zkValue2").val();
		//	if (zkValue2==undefined || zkValue2=="" || zkValue2==null){
		//		if (!$("#zkValue").val()){
		//			binolssprm7302.showErrorMsg("折扣券折扣率不能为空");
		//			return false;
		//		}
		//	}
		//	if (zkValue==undefined || zkValue=="" || zkValue==null){
		//		if (!$("#zkValue2").val()){
		//			binolssprm7302.showErrorMsg("折扣券折扣率不能为空");
		//			return false;
		//		}
		//	}
		//}
		// 参数序列化
		var param = null;
		// 基本信息
		$("#baseInfo").find(":input").each(function() {
			if ($(this).attr("name") && !$(this).is(":disabled")) {
				$(this).val($.trim(this.value));
				if (null == param) {
					param = $(this).serialize();
				} else {
					param += "&" + $(this).serialize();
				}
			}
		});
		param += '&' + $('#couponFlag').serialize();
		var arr = [];
		$('#contentDiv').find(".rule_content").each(function() {
			arr.push(binolssprm7302.convertContent2(this));
		});
		param += "&couponRule.content=["
			+ arr.toString() + "]";
		param += "&couponRule.sendCond="
			+ binolssprm7302.convertCondition('#sendCondition');
		param += "&couponRule.sendCondPrt="
			+ this.convertPrt("1","0","1");

		param += "&couponRule.sendCondCnt="
			+ binolssprm7302.getCounterResult();

		param += "&couponRule.useCond="
			+ binolssprm7302.getUseCondAll();
		if($("#useCondTypeCheckbox").is(":checked")){
			param += "&couponRule.isSameFlag=1";
		}else{
			param += "&couponRule.isSameFlag=0";
		}
		cherryAjaxRequest({
			url: $("#saveUrl").attr("href"),
			param: param,
			coverId: "#pageBrandButton",
			callback: function(msg) {
				binolssprm7302.doRefresh = true;
			}
		});
	},
	"convertParams" : function(id) {
		var arr = [];
		$(id).find(":input").each(function() {
			var name = $(this).attr("name");
			if (!name || $(this).is(":disabled") ||
					($(this).is(':radio') || $(this).is(':checkbox')) && !$(this).is(':checked')) {
				return true;
			}
			var rst = '"'+name+'":"'+
			encodeURIComponent($.trim($(this).val().replace(/\\/g,'\\\\').replace(/"/g,'\\"')))+'"';
			arr.push(rst);
		});
		return "{" + arr.toString() + "}";
	},

	"convertParamsUseType" : function(id) {
		var arr = [];
		$(id).find(":input").each(function() {
			var name = $(this).attr("name");
			if (!name || $(this).is(":disabled") ||
				($(this).is(':radio') || $(this).is(':checkbox')) && !$(this).is(':checked')) {
				return true;
			}
			var rst = '"'+name+'":"'+
				encodeURIComponent($.trim($(this).val().replace(/\\/g,'\\\\').replace(/"/g,'\\"')))+'"';
			arr.push(rst);
		});
		var useTimeType ='"useTimeType":'+'"'+$('#useTimeType option:selected').val()+'"';
		arr.push(useTimeType);
		return "{" + arr.toString() + "}";
	},
	"convertContent" : function(id) {
		var arr = [];
		$(id).find(":input").not($(":input", ".z-tbody")).each(function() {
			var name = $(this).attr("name");
			if (!name || $(this).is(":disabled") ||
					($(this).is(':radio') || $(this).is(':checkbox')) && !$(this).is(':checked')) {
				return true;
			}
			var rst = '"'+name+'":"'+
			encodeURIComponent($.trim($(this).val().replace(/\\/g,'\\\\').replace(/"/g,'\\"')))+'"';
			arr.push(rst);
		});
		var $ztbody = $(id).find('.z-tbody');
		if ($ztbody.length > 0) {
			var p = [];
			$ztbody.find('tr').each(function() {
				p.push(binolssprm7302.convertParams(this));
			});
			arr.push('"zList":' + '[' + p.toString() + ']');
		}
		return "{" + arr.toString() + "}";
	},
	"convertContent2" : function(id) {
		var arr = [];
		$(id).find(":input").not($(":input", ".z-tbody")).each(function() {
			var name = $(this).attr("name");
			if (!name || $(this).is(":disabled") ||
					($(this).is(':radio') || $(this).is(':checkbox')) && !$(this).is(':checked')) {
				return true;
			}
			if(name!='useCondJson'){
				var rst = '"'+name+'":"'+
					encodeURIComponent($.trim($(this).val().replace(/\\/g,'\\\\').replace(/"/g,'\\"')))+'"';
				arr.push(rst);
			}

		});
		var $ztbody = $(id).find('.z-tbody');
		if ($ztbody.length > 0) {
			var p = [];
			$ztbody.find('li').each(function() {
				p.push(binolssprm7302.convertParams(this));
			});
			arr.push('"zList":' + '[' + p.toString() + ']');
		}
		return "{" + arr.toString() + "}";
	},
	"convertCondition" : function(id) {
		var $proShowDiv;
		var $proTypeShowDiv;
		var $camp_tbody_w;
		var $camp_tbody_b;
		var $cnt_tbody_w;
		if (id == '#sendCondition') {
			$proShowDiv = $('#proShowDiv');
			$proTypeShowDiv = $('#proTypeShowDiv');
			$camp_tbody_w = $('#rul_s_w_commonDiv');
			$camp_tbody_b = $('#rul_s_b_div');
			$cnt_tbody_w = $('#cnt_s_w_commonDiv');
		}
		var arr = [];
		$(id).find(":input.z-param").each(function() {
			var name = $(this).attr("name");
			if (!name || $(this).is(":disabled") ||
					($(this).is(':radio') || $(this).is(':checkbox')) && !$(this).is(':checked')) {
				return true;
			}
			var rst = '"'+name+'":"'+
			encodeURIComponent($.trim($(this).val().replace(/\\/g,'\\\\').replace(/"/g,'\\"')))+'"';
			arr.push(rst);

		});
		var p = [];
		$(id).find(':input[name="memLevel"]:checked').each(function(){
			p.push($(this).next().val());
		});
		if (p.length > 0) {
			arr.push('"memLevel_w":' + '"' + p.toString() + '"');
		}
		/* 使用产品明细表,sendCond useCond注释
		var prtArr = [];
		$proShowDiv.find('tr').each(function() {
			prtArr.push(binolssprm7302.convertParams(this));
		});
		if (prtArr.length > 0) {
			arr.push('"proList":[' + prtArr.toString() + ']');
		}
		var typeArr = [];
		$proTypeShowDiv.find('tr').each(function() {
			typeArr.push(binolssprm7302.convertParams(this));
		});
		if (typeArr.length > 0) {
			arr.push('"proTypeList":[' + typeArr.toString() + ']');
		}
		*/
		var campArr_w = [];
		$camp_tbody_w.find('li').each(function() {
			campArr_w.push(binolssprm7302.convertParam_rule(this));
		});
		if (campArr_w.length > 0) {
			arr.push('"campList_w":[' + campArr_w.toString() + ']');
		}

		var campArr_b = [];
		$camp_tbody_b.find('li').each(function() {
			campArr_b.push(binolssprm7302.convertParam_rule(this));
		});
		if (campArr_b.length > 0) {
			arr.push('"campList_b":[' + campArr_b.toString() + ']');
		}

		if($("#cnt_s_w_select").val() == 3){
			var cntArr_w = [];
			$cnt_tbody_w.find('li').each(function() {
				if($(this).find("input:checkbox").is(":checked")){
					cntArr_w.push(binolssprm7302.convertParam_cnt(this));
				}
			});
			if (cntArr_w.length > 0) {
				arr.push('"cntChannelList_w":[' + cntArr_w.toString() + ']');
			}
		}else if($("#cnt_s_w_select").val() == 1){
			if($cnt_tbody_w.find('li').length > 0){
				arr.push('"cntListCheckedFlag_w": "1"');
			}else{
				arr.push('"cntListCheckedFlag_w": "0"');
			}
		}

		if($("#cnt_s_b_div").find('li').length > 0){
			arr.push('"cntListCheckedFlag_b": "1"');
		}else{
			arr.push('"cntListCheckedFlag_b": "0"');
		}

		if($("#sendMemberWhite").find("[name='memberKbn_w']").val() == 1){
			if($("#sendMemberWhite").find(".memberInput").find('li').length > 0){
			}else{
				arr.push('"memListCheckedFlag_w": "0"');
			}
		}

		if($("#sendMemberBlackDiv").find(".memberInput").find('li').length > 0){
			arr.push('"memListCheckedFlag_b": "1"');
		}else{
			arr.push('"memListCheckedFlag_b": "0"');
		}
		arr.push('"counterKbn_b": "1"');
		arr.push('"memberKbn_b": "1"');
		return "{" + arr.toString() + "}";
	},
	"convertParam_rule":function(obj){
		var $this=$(obj);
		var map={
			"campaignCode":$this.find("input[name='campaignCode']").val(),
			"campaignMode":$this.find("input[name='campaignMode']").val(),
			"campaignName":$this.find("input[name='campaignName']").val()
		};
		return JSON2.stringify(map);
	},
	"convertParam_cnt":function(obj){
		var $this=$(obj);
		var map={
			"id":$this.find("input[name='id']").val(),
			"name":$this.find("input[name='name']").val(),
		};
		return JSON2.stringify(map);
	},
	"counterUpload" : function (conditionType){
		var $parentDiv;
		var $counterUpExcel;
		var counterUpExcelId;
		var upMode;
		var $counterPathExcel;
		if (conditionType == '1') {
			$parentDiv = $("#sendCondition");
			$counterUpExcel = $("#counterUpExcel");
			counterUpExcelId = 'counterUpExcel';
			upMode = $parentDiv.find('input[name="upMode"]:checked').val();
			$counterPathExcel = $('#counterPathExcel');
		} else {
			$parentDiv = $("#useCondition");
			$counterUpExcel = $("#counterUpExcelUse");
			counterUpExcelId = 'counterUpExcelUse';
			upMode = $parentDiv.find('input[name="upModeUse"]:checked').val();
			$counterPathExcel = $('#useCounterPathExcel');
		}
		var url = $("#importCounterUrl").attr("href");
    	// AJAX登陆图片
    	var $ajaxLoading = $parentDiv.find("#counterloading");
    	// 错误信息提示区
    	var $errorMessage = $('#actionResultDisplay');
    	// 清空错误信息
		$errorMessage.hide();
		$errorMessage.empty();
    	if($counterUpExcel.val()==''){
    		$counterPathExcel.val('');
			$("#errorDiv").show();
			$("#errorSpan").text("请选择文件");
    		return false;
    	}
    	$ajaxLoading.ajaxStart(function(){$(this).show();});
    	$.ajaxFileUpload({
	        url: url,
	        secureuri:false,
	        data:{'csrftoken':parentTokenVal(),
	        	'couponRule.ruleCode':$('#ruleCode').val(),
	        	'couponRule.brandInfoId':$('#brandInfoId').val(),
	        	'upMode': upMode,
	        	'conditionType': conditionType},
	        fileElementId:counterUpExcelId,
	        dataType: 'html',
	        success: function (msg){
	        	$ajaxLoading.ajaxComplete(function(){$(this).hide();});
	        	$errorMessage.html(msg);
	        	if($errorMessage.find("#actionResultDiv").length > 0){
	        		$errorMessage.show();
	        	}
	        }
        });
	},
	"counterDialog" : function(conditionType) {
		if($("#counterDialogDiv").length == 0) {
			$("body").append('<div style="display:none" id="counterDialogDiv"></div>');
		} else {
			$("#counterDialogDiv").empty();
		}
		var searchInitCallback = function(msg) {
			$("#counterDialogDiv").html(msg);
			var dialogSetting = {
				dialogInit: "#counterDialogDiv",
				text: msg,
				width: 	600,
				height: 350,
				title: 	"柜台一览",
				cancel: "关闭",
				cancelEvent: function(){removeDialog("#counterDialogDiv");}
			};
			openDialog(dialogSetting);

			if(oTableArr[190]) {
				oTableArr[190] = null;
			}
			var searchUrl = $("#counterDialogUrl").attr("href")+"?"+getSerializeToken()
				+ "&ruleCode=" + $('#ruleCode').val() + "&conditionType=" + conditionType;
			var tableSetting = {
					 // 表格ID
					 tableId : '#counterDialogDataTable',
					 // 一页显示页数
					 iDisplayLength:5,
					 // 数据URL
					 url : searchUrl,
					 // 表格默认排序
					 aaSorting : [[ 1, "asc" ]],
					 // 表格列属性设置
					 aoColumns : [  { "sName": "No", "sWidth": "5%", "bSortable": false},
									{ "sName": "counterCode", "sWidth": "35%"},
									{ "sName": "counterName", "sWidth": "60%"}],
					index:190,
					colVisFlag:false
			 };
			// 调用获取表格函数
			getTable(tableSetting);
		};
		cherryAjaxRequest({
			url: $("#counterDialogInitUrl").attr("href"),
			param: null,
			callback: searchInitCallback
		});
	},
	// 选择购买产品弹出框
    "openProDialog" :function(obj) {
    	var $parent= $(obj).closest('div.rule_content');
    	var proShowDiv= $parent.find('#contentProduct').attr('id');

    	var proTable = "proTable";
    	var prtVendorId = "prtVendId";
		var option = {
         	   	targetId: proShowDiv,
 	           	checkType : "checkbox",
 	           	mode : 2,
 	            popValidFlag: 2,
 	           	brandInfoId : $("#brandInfoId").val(),
 		       	getHtmlFun:function(info){
     		       	var html = '<tr>';
     				html += '<td class="hide"><input type="hidden" name="prtVendorId" value="' + info.proId + '"/><input type="hidden" name="'+prtVendorId+'" value="' + info.proId + '"/></td>';
     				html += '<td style="width:25%;">' + info.unitCode + '</td>';
     				html += '<td style="width:25%;">' + info.barCode + '</td>';
     				html += '<td style="width:25%;">' + info.nameTotal + '</td>';
     				html += '<td style="width:10%;"><input type="text" class="number" name="proNum" value="1" maxlength="2" /></td>';
     				html += '<td class="center" style="width:15%;"><a href="javascript:void(0);" onClick="binolssprm7302.deleteHtml(this);return false;">删除</a></td>';
     				html += '</tr>';
     				return html;
			   	},
			   	click : function() {
			   		if($("#"+proShowDiv).find("tr").length > 0) {
			   			$parent.find("#"+proTable).show();
			   		} else {
			   			$parent.find("#"+proTable).hide();
			   		}
			   	}
 	    };
 		popAjaxPrtDialog(option);
	},
	// 选择购买产品弹出框
    "openOneProDialog" :function(obj) {
    	var $parent = $(obj).closest('div.rule_content');
    	var proShowDiv = $parent.find('.z-tbody').attr('id');
		var option = {
         	   	targetId: proShowDiv,
 	           	mode : 2,
 	            popValidFlag: 2,
 	            checkType : "radio",
 	           	brandInfoId : $("#brandInfoId").val(),
 		       	getHtmlFun:function(info){
     				var html = '<tr><td><span class="list_normal">';
    				html += '<span class="text" style="line-height:19px;">' + info.nameTotal + '(' + info.barCode + ')</span>';
           			html += '<span class="close" style="margin: 0 1px 2px 5px;" onclick="binolssprm7302.deleteHtml2(this);return false;"><span class="ui-icon ui-icon-close"></span></span>';
           			html += '<input type="hidden" name="prtVendorId" value="' + info.proId + '"/>';
           			html += '<input type="hidden" name="prtVendId" value="' + info.proId + '"/>';
           			html += '</span></td></tr>';
     				return html;
			   	}
 	    };
 		popAjaxPrtDialog(option);
	},
// 选择购买产品弹出框
	"openOneProDialog2" :function(obj) {
		var $parent = $(obj).closest('div.rule_content');
		var proShowDiv = $parent.find('.z-tbody').attr('id');
		var option = {
			targetId: proShowDiv,
			mode : 2,
			popValidFlag: 2,
			checkType : "radio",
			brandInfoId : $("#brandInfoId").val(),
			getHtmlFun:function(info){
				var html2 = '<li>';
				html2 +=  '<input type="checkbox" checked="checked" name="discountTicket" onclick="binolssprm7302.removeLi(this);" value="' + info.proId  +  '"/>';
				html2 += '<input type="hidden" name="prtVendorId" value="' + info.proId + '"/><input type="hidden" name="prtVendId" value="' + info.proId + '"/>';
				html2 += info.unitCode;
				html2 += '&nbsp;' + info.barCode;
				html2 += '&nbsp;' + info.nameTotal;
				html2 += '</li>';
				return html2;
			}
		};
		popAjaxPrtDialog(option);
	},

	// 选择购买产品类别弹出框
	"openProTypeDialog" :function(conditionType) {
		var $parent;
		var proTypeShowDiv;
		if (conditionType == '1') {
    		$parent = $("#sendCondition");
    		proTypeShowDiv = "proTypeShowDiv";
    	}  else {
    		$parent = $("#useCondition");
    		proTypeShowDiv = "useProTypeShowDiv";
    	}
    	var proTypeTable = "proTypeTable";
    	var cateValId = "cateId";
		var option = {
         	   	targetId: proTypeShowDiv,
 	           	checkType : "checkbox",
 	           	mode : 2,
 	           	brandInfoId : $("#brandInfoId").val(),
 		       	getHtmlFun:function(info){
     		       	var html = '<tr>';
     				html += '<td class="hide"><input type="hidden" name="cateValId" value="' + info.cateValId + '"/><input type="hidden" name="'+cateValId+'" value="' + info.cateValId + '"/></td>';
     				html += '<td style="width:25%;">' + info.cateVal + '</td>';
     				html += '<td style="width:40%;">' + info.cateValName + '</td>';
     				html += '<td style="width:10%;"><input type="text" class="number" name="cateNum" value="1" maxlength="2" /></td>';
     				html += '<td class="center" style="width:15%;"><a href="javascript:void(0);" onClick="binolssprm7302.deleteHtml(this);return false;">删除</a></td>';
     				html += '</tr>';
     				return html;
			   	},
			   	click : function(){
			   		if($("#"+proTypeShowDiv).find("tr").length > 0) {
			   			$parent.find("#"+proTypeTable).show();
			   		} else {
			   			$parent.find("#"+proTypeTable).hide();
			   		}
			   	}
 	    };
		popAjaxPrtCateDialog(option);
	},
	// 删除购买产品或者购买产品类别信息
	"deleteHtml": function(_this) {
		var $tbody = $(_this).closest('tbody');
		$(_this).closest('tr').remove();
		if($tbody.find('tr').length == 0) {
			$tbody.parent().hide();
		}
	},
	"deleteHtml2": function(_this) {
		var $tbody = $(_this).closest('tbody');
		$(_this).closest('tr').remove();
	},
	// 选择活动弹出框
	"popCampaignList": function(conditionType, obj) {
		var url = $("#searchCampaignInitUrl").attr('href');
		var valueArr = [];
		var $parent;
		$parent = $(obj).closest('div.rule_content');

		var $tbody = $parent.find(".contentCampaign");
		$parent.find('input[name="campaignCode"]').each(function() {
			valueArr.push($(this).val());
		});
		var callback = function(tableId) {
			$("#"+tableId).find(":input[checked]").each(function() {
				var campaignCode = $(this).val();
				if (valueArr.length > 0) {
					var isExist = false;
					for(var i in valueArr) {
						if (valueArr[i] == campaignCode) {
							isExist = true;
							break;
						}
					}
					if (isExist) {
						return true;
					}
				}
				var html = '<li>';
				html += '<input type="checkbox" name="campListWhiteChoose" checked="checked"/>';
				html += '<input type="hidden" name="campaignCode" value="'+campaignCode+'"/>';
				html += '<input type="hidden" name="campaignMode" value="'+$(this).next().val()+'"/>';
				html += '<input type="hidden" name="campaignName" value="'+$(this).parent().next().text()+'"/>';
				html += campaignCode+'&nbsp;'+$(this).parent().next().text();
				html += '</li>';
       			$tbody.append(html);
			});
		}
		popDataTableOfCampaignList(url, 'checkType=checkbox', null, callback, valueArr);
	},
	// 删除选择的活动
	"delCampaignHtml": function(object) {
		var $tbody = $(object).closest('tbody');
		$(object).closest('tr').remove();
	},
	"memberUpload" : function (conditionType){
		var $parentDiv;
		var $memberUpExcel;
		var memberUpExcelId;
		var upMode;
		if (conditionType == '1') {
			$parentDiv = $("#sendCondition");
			$memberUpExcel = $("#memberUpExcel");
			memberUpExcelId = 'memberUpExcel';
			upMode = $parentDiv.find('input[name="memberUpMode"]:checked').val();
		}
		var url = $("#importMemberUrl").attr("href");
    	// AJAX登陆图片
    	var $ajaxLoading = $parentDiv.find("#memberloading");
    	// 错误信息提示区
    	var $errorMessage = $('#actionResultDisplay');
    	// 清空错误信息
		$errorMessage.hide();
		$errorMessage.empty();
    	if($memberUpExcel.val()==''){
    		$parentDiv.find('#memberPathExcel').val('');
			$("#errorDiv").show();
			$("#errorSpan").text("请选择文件");
    		return false;
    	}
    	$ajaxLoading.ajaxStart(function(){$(this).show();});
    	$.ajaxFileUpload({
	        url: url,
	        secureuri:false,
	        data:{'csrftoken':parentTokenVal(),
	        	'couponRule.ruleCode':$('#ruleCode').val(),
	        	'couponRule.brandInfoId':$('#brandInfoId').val(),
	        	'upMode': upMode,
	        	'conditionType': conditionType},
	        fileElementId:memberUpExcelId,
	        dataType: 'html',
	        success: function (msg){
	        	$ajaxLoading.ajaxComplete(function(){$(this).hide();});
	        	$errorMessage.html(msg);
	        	if($errorMessage.find("#actionResultDiv").length > 0){
	        		$errorMessage.show();
	        	}
	        }
        });
	},
	"memberDialog" : function(conditionType) {
		if($("#memberDialogDiv").length == 0) {
			$("body").append('<div style="display:none" id="memberDialogDiv"></div>');
		} else {
			$("#memberDialogDiv").empty();
		}
		var searchInitCallback = function(msg) {
			$("#memberDialogDiv").html(msg);
			var dialogSetting = {
				dialogInit: "#memberDialogDiv",
				text: msg,
				width: 	600,
				height: 350,
				title: 	"会员一览",
				cancel: "关闭",
				cancelEvent: function(){removeDialog("#memberDialogDiv");}
			};
			openDialog(dialogSetting);

			if(oTableArr[191]) {
				oTableArr[191] = null;
			}
			var searchUrl = $("#memberDialogUrl").attr("href")+"?"+getSerializeToken()
				+ "&ruleCode=" + $('#ruleCode').val() + "&conditionType=" + conditionType;
			var tableSetting = {
					 // 表格ID
					 tableId : '#memberDialogDataTable',
					 // 一页显示页数
					 iDisplayLength:5,
					 // 数据URL
					 url : searchUrl,
					 // 表格默认排序
					 aaSorting : [[ 1, "asc" ]],
					 // 表格列属性设置
					 aoColumns : [  { "sName": "No", "sWidth": "5%", "bSortable": false},
									{ "sName": "memCode", "sWidth": "45%"},
									{ "sName": "mobile", "sWidth": "50%"}],
					index:191,
					colVisFlag:false
			 };
			// 调用获取表格函数
			getTable(tableSetting);
		};
		cherryAjaxRequest({
			url: $("#memberDialogInitUrl").attr("href"),
			param: null,
			callback: searchInitCallback
		});
	},
	"searchChannel" : function(url,type){
		var $cntwhiteChannel;
		if('0' == type){//0为发送门槛渠道选择
			$cntwhiteChannel = $("#cntwhiteChannel_send");
		}
		else{//使用门槛渠道选择
			$cntwhiteChannel = $("#cntwhiteChannel_use");
		}

		// 区域树初始默认选中值
		var value = {};
		var regionInfo = [];
		var memCounterId = $cntwhiteChannel.val();

		if(memCounterId) {
			var memCounterIds = memCounterId.split(",");
			for(var i = 0; i < memCounterIds.length; i++) {
				regionInfo.push("D"+memCounterIds[i]);
			}
		}
		value.regionInfo = regionInfo;
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
				if('0' == type){//0为发送门槛渠道选择
					binOLSSPRM7302_global.sendTreeTmp = $.fn.zTree.init($('#cnt_s_w_div_tree'),setting,eval('('+msg+')'));
					//$("#cnt_s_w_commonDiv").hide();
					$('#cnt_s_w_treeDiv').show();
				}else{//使用门槛渠道选择
					binOLSSPRM7302_global.useTreeTmp = $.fn.zTree.init($('#cnt_u_w_div_tree'),setting,eval('('+msg+')'));
					//$("#cnt_u_w_commonDiv").hide();
					$('#cnt_u_w_treeDiv').show();
				}
				if(value) {

					if(value.regionInfo && value.regionInfo.length > 0) {
						for(var i = 0; i < value.regionInfo.length; i++) {
							if('0' == type){
								var node = binOLSSPRM7302_global.sendTreeTmp.getNodeByParam("id", value.regionInfo[i], null);
								if(node != null) {
									binOLSSPRM7302_global.sendTreeTmp.checkNode(node, true, true);
								}
							}else{
								var node = binOLSSPRM7302_global.useTreeTmp.getNodeByParam("id", value.regionInfo[i], null);
								if(node != null) {
									binOLSSPRM7302_global.useTreeTmp.checkNode(node, true, true);
								}
							}
						}
					}
					value = "";
				}
			}else {
				$('#channelTreeDialog').find("#channelTree").empty();
			}

			if(type == 0){
				if($("#cnt_s_w_select").val() != 2){
					$("#cnt_s_w_treeDiv").hide();
				}else{
					$("#cnt_s_w_treeDiv").show();
				}
			}else{
				if($("#cnt_u_w_select").val() != 2){
					$("#cnt_u_w_treeDiv").hide();
				}else{
					$("#cnt_u_w_treeDiv").show();
				}
			}

		};
		cherryAjaxRequest({
			url: searchUrl,
			callback: regionTreeInitCallback
		});
	},
	"searchChannelWay":function(type){
		var url=$("#seachChannelWay").attr("href");
		cherryAjaxRequest({
			url: url,
			callback: function(msg){
				if(msg == null || msg == "" || msg == undefined || msg == "ERROR"){
					return;
				}else{
					var param_map = eval("("+msg+")");
					var channelList=param_map.channelList;
					var html="";
					for (var i=0;i<channelList.length;i++){
						html += '<li>';
						html += '<input type="checkbox"value="'+channelList[i].id+'" />'+channelList[i].name;
						html += '<input name="id" class="hide" value="'+channelList[i].id+'"/>';
						html += '<input name="name" class="hide" value="'+channelList[i].name+'"/>';
						html += '</li>';
					}
					console.log(html);
					if(type == 0){
						$("#cnt_s_w_commonDiv").empty();
						$("#cnt_s_w_commonDiv").append(html);
						$("#cnt_s_w_commonDiv").show();
					}else{
						$("#cnt_u_w_commonDiv").empty();
						$("#cnt_u_w_commonDiv").append(html);
						$("#cnt_u_w_commonDiv").show();
					}
				}
			}
		});
	},
	// pop券包
	"popPackageDialog":function(_this){
		var dialogId = '#popPackageDialog';
		if($(dialogId).length == 0) {
    		$('body').append('<div style="display:none" id="popPackageDialog"></div>');
    	} else {
    		$(dialogId).empty();
    	}
		var option = {
				dialogInit:dialogId,
				text:$("#packageDialog").html(),
				title:"添加券",
				confirm:"确定",
    			confirmEvent: function(){
    				binolssprm7302.addCoupon(dialogId);
    				removeDialog(dialogId);
    			},
    			cancel:"取消",
    			cancelEvent: function(){
    				removeDialog(dialogId);
    			}
			};
		openDialog(option);
	},
	"addCoupon":function(dialogId) {
		var tempId = '#couponsTempId';
		if($(tempId).length == 0) {
    		$('body').append('<div style="display:none" id="couponsTempId"></div>');
    	} else {
    		$(tempId).empty();
    	}
		var $content = $('#coupon-content');
		//var proContentStart = $content.find('[id^="proContent"]').length + 1;
		////产品div序号起始值
		//var contentProductStart = $content.find('[id^="contentProduct"]').length + 1;
		////活动div序号起始值
		//var contentCampaignStart = $content.find('[id^="contentCampaign"]').length + 1;
		$(dialogId).find(':input:checked').each(function(){
			var code = $(this).val();
			var coupNum;
			try {
				coupNum = parseInt($(this).parent().parent().find("input[name='coupNum']").val());
			} catch (e) {

			}
			if (!coupNum) {
				coupNum = 1;
			}
			for (var i = 0; i < coupNum; i++) {
				maxContentNoCopy+=1;
				if($("#useCondTypeCheckbox").prop("checked")){
					//$(".useCondButton").show();
					$(tempId).append($('#coupon-content' + code).html());
					$(".useCondButton").hide();
				}else{
					$(tempId).append($('#coupon-content' + code).html());
					$(".useCondButton").show();
				}
				if (code == '5') {
					$(tempId).find('#proContent').attr('id', 'proContent' + maxContentNoCopy);
				} else if (code == '2') {
					$(tempId).find('#contentProduct').attr('id', 'contentProduct' + maxContentNoCopy);
				} else if(code == '3'){
					$(tempId).find('#contentCampaign').attr('id', 'contentCampaign' + maxContentNoCopy);
				}
				$(tempId).find('input[name=contentNo]:last').val(maxContentNoCopy);
			}

		});
		//$(tempId).find('#contentNoFinal').val(maxContentNoCopy);
		$(tempId).find("span.icon_del").show();
		//$(tempId).find(".useCondButton").show();
		$(".useCondType").show();
		$content.append($(tempId).html());
		//$content.find('#contentNoFinal').val(maxContentNoCopy);
		//alert($(".contentNoToAdd").find('#contentNoFinal').val("u"));
	},
	"delCoupon":function(obj) {
		$(obj).parent().remove();
	},
	//柜台导入页面弹出框
	"popCounterLoadDialog":function(conditionType,filterType,contentNo){
		var dialogId = 'counterDivDialog';
		var $dialog = $("#" + dialogId);
		if($dialog.length == 0) {
			$("body").append('<div style="display:none" id="'+dialogId+'"></div>');
		} else {
			$dialog.empty();
		}
		var title;
		if(filterType==1){
			title = '导入柜台';
		}else if(filterType==2){
			title = '导入排除柜台';
		}
		var url = $("#popCounterUpload").attr("href");
		var param="ruleCode="+$('#ruleCode').val()+"&filterType="+filterType+"&conditionType="+conditionType+"&ExecLoadType=1"+"&contentNo="+contentNo;
		cherryAjaxRequest({
			url: url,
			param:param,
			callback: function(msg){
				$dialog.html(msg);
				// 弹出验证框
				var dialogSetting = {
					dialogInit: "#" + dialogId,
					text: msg,
					width: 	800,
					height: 400,

					title: title,
					confirm:"确定",
					closeEvent: function(){
						if(conditionType == 1){
							if(filterType == 1){
								binolssprm7302.changeCounterSpan(conditionType,filterType,$("#cnt_s_w_select"),0,true);
							}else{
								binolssprm7302.changeCounterSpan(conditionType,filterType,null,0,true);
							}
						}else{
							if(filterType == 1){
								binolssprm7302.changeCounterSpan(conditionType,filterType,$("#cnt_u_w_select"),0,true);
							}else{
								binolssprm7302.changeCounterSpan(conditionType,filterType,null,0,true);
							}
						}
						removeDialog("#" + dialogId);
					},
					confirmEvent:function(){
						if(conditionType == 1){
							if(filterType == 1){
								binolssprm7302.changeCounterSpan(conditionType,filterType,$("#cnt_s_w_select"),1,true);
							}else{
								binolssprm7302.changeCounterSpan(conditionType,filterType,null,1,true);
							}
						}else{
							if(filterType == 1){
								binolssprm7302.changeCounterSpan(conditionType,filterType,$("#cnt_u_w_select"),1,true);
							}else{
								binolssprm7302.changeCounterSpan(conditionType,filterType,null,1,true);
							}
						}
						removeDialog("#" + dialogId);
					}
				};
				openDialog(dialogSetting);
				$(".ui-dialog-titlebar-close.ui-corner-all").hide();
			}
		});
	},
	//产品导入页面弹出框
	"popProductLoadDialog":function(conditionType,filterType,contentNo){
		//alert(111);
		var dialogId = 'productDivDialog';
		var $dialog = $("#" + dialogId);
		if($dialog.length == 0) {
			$("body").append('<div style="display:none" id="'+dialogId+'"></div>');
		} else {
			$dialog.empty();
		}
		var title;
		if(filterType==1){
			title = '产品导入';
		}else if(filterType==2){
			title = '导入排除产品';
		}
		var url = $("#popUploadUrl").attr("href");
		var param = 'ruleCode=' + $("#ruleCode").val();
		param += '&conditionType=' + conditionType;
		param += '&filterType=' + filterType;
		param += '&execLoadType=2';

		var contentNoVal = (conditionType == '1' ? 0 : contentNo);
		param += '&contentNo=' + contentNoVal;
		//alert(param);
		cherryAjaxRequest({
			url: url,
			param:param,
			callback: function(msg){
				//alert(msg);
				$dialog.html(msg);
				// 弹出验证框
				var dialogSetting = {
					dialogInit: "#" + dialogId,
					text: msg,
					width: 	800,
					height: 400,

					title: title,
					confirm:'确定',
					confirmEvent:function(){
						removeDialog("#" + dialogId);
						binolssprm7302.writeImpPrt(conditionType,filterType,contentNoVal);
					},
					closeEvent: function(){
						removeDialog("#" + dialogId);
						binolssprm7302.writeImpPrt(conditionType,filterType,contentNoVal);

					}
				};
				openDialog(dialogSetting);
				$(".ui-dialog-titlebar-close.ui-corner-all").hide();
			}
		});
	},
	//会员导入页面弹出框
	"popMemberLoadDialog":function(conditionType,filterType,contentNo,obj){
		var dialogId = 'memberDivDialog';
		var $dialog = $("#" + dialogId);
		if($dialog.length == 0) {
			$("body").append('<div style="display:none" id="'+dialogId+'"></div>');
		} else {
			$dialog.empty();
		}
		var title;
		var $parent;
		if(filterType==1){
			title = '会员导入';
			$parent = $(obj).parents(".memberWriteDiv");
		}else if(filterType==2){
			title = '导入排除会员';
			$parent = $(obj).parents(".memberBlackDiv");
		}
		contentNo==null?0:contentNo;
		var url = $("#popUploadUrl").attr("href");
		var param = 'conditionType='+conditionType+'&filterType='+filterType+'&contentNo='+contentNo+'&execLoadType=3';
		cherryAjaxRequest({
			url: url,
			param:param,
			callback: function(msg){
				$dialog.html(msg);
				// 弹出验证框
				var dialogSetting = {
					dialogInit: "#" + dialogId,
					text: msg,
					width: 	800,
					height: 400,

					title: title,
					confirm:"确定",
					confirmEvent:function(){
						removeDialog('#'+dialogId);
						binolssprm7302.getExeclUploadMemberList(conditionType,filterType,$parent.find(".memberInput"),contentNo);
					},
					closeEvent: function(){
						removeDialog('#'+dialogId);
						binolssprm7302.getExeclUploadMemberList(conditionType,filterType,$parent.find(".memberInput"),contentNo);
					}
				};
				openDialog(dialogSetting);
				$(".ui-dialog-titlebar-close.ui-corner-all").hide();
			}
		});
	},
	"changeCounterSpan" : function (conditionType,filterType,obj,isShow,pageLoad){
		if (!pageLoad) {

			binolssprm7302.switchAlert_counter('柜台选项切换',conditionType,filterType,obj,isShow);
			/*
			 var $oldKbn = $("#" + obj).parent().parent().find(".oldKbn");
			 var r = confirm("切换选项后,原选项设置内容将被清除");
			 if (r==true){
			 $oldKbn.val($("#" + obj).val());
			 } else {
			 $("#" + obj).val($oldKbn.val()  );
			 return false;
			 }
			 */
		} else {
			binolssprm7302.changeCntSpanDiv(conditionType,filterType,obj,isShow);
		}
	},
	"changeCntSpanDiv":function(conditionType,filterType,obj,isShow){
		var selectValue;
		if(obj){
			selectValue=$(obj).val();
		}
		if(filterType == 1){
			if(conditionType == 1){
				//默认对两个DIV进行隐藏
				$("#cnt_s_w_commonDiv").hide();
				$("#cnt_s_w_treeDiv").hide();
				$("#cnt_s_w_inputButton").hide();
				//白名单的情况
				$("#cnt_s_w_inputButton").hide();
			}else{
				//默认对两个DIV进行隐藏
				$("#cnt_u_w_commonDiv").hide();
				$("#cnt_u_w_treeDiv").hide();
				$("#cnt_u_w_inputButton").hide();
				//白名单的情况
				$("#cnt_u_w_inputButton").hide();
			}
			if(selectValue == 0){//请选择状态
				if(conditionType == 1){
					$("#cnt_s_w_commonDiv").empty();
				}else{
					$("#cnt_u_w_commonDiv").empty();
				}
			}else if(selectValue == 1){//导入门店
				if(isShow == 0){
					if(conditionType == 1){
						$("#cnt_s_w_commonDiv").empty();
						$("#cnt_s_w_commonDiv").show();
						$("#cnt_s_w_inputButton").show();
					}else{
						$("#cnt_u_w_commonDiv").empty();
						$("#cnt_u_w_commonDiv").show();
						$("#cnt_u_w_inputButton").show();
					}
				}else{
					var param="ruleCode="+$('#ruleCode').val()+"&filterType="+filterType+"&conditionType="+conditionType;
					if(conditionType == 1){
						//调用Ajax动态取用导入的白名单柜台
						$("#cnt_s_w_commonDiv").empty();
						param += "&contentNo=0";
					}else{
						$("#cnt_u_w_commonDiv").empty();
						param += "&contentNo="+$("#useContionDiv input[name='contentNo']").val();
					}
					cherryAjaxRequest({
						url: $("#getCNTListUrl").attr("href"),
						param: param,
						callback: function(msg) {
							if(msg == null || msg == "" || msg == undefined || msg == "ERROR"){
								return;
							}else{
									var counterList = eval("("+msg+")");
									var html="";
									for (var i=0;i<counterList.length;i++){
										html += '<li>';
										if(conditionType == 1){
											//html += '<input type="checkbox"value="'+counterList[i].organizationID+'"  onclick="binolssprm7302.cnt_s_removeLi(this,1,1);" checked="checked" />'+counterList[i].counterCode+'  '+counterList[i].counterName;
											html += counterList[i].counterCode+'  '+counterList[i].counterName;
										}else{
											//html += '<input type="checkbox"value="'+counterList[i].organizationID+'"  onclick="binolssprm7302.cnt_s_removeLi(this,1,2);" checked="checked" />'+counterList[i].counterCode+'  '+counterList[i].counterName;
											html += counterList[i].counterCode+'  '+counterList[i].counterName;
										}
										html += '<input name="organizationID" class="hide" value="'+counterList[i].organizationID+'"/>';
										html += '<input name="counterCode" class="hide" value="'+counterList[i].counterCode+'"/>';
										html += '<input name="counterName" class="hide" value="'+counterList[i].counterName+'"/>';
										html += '</li>';
									}
								if(conditionType == 1){
									$("#cnt_s_w_commonDiv").append(html);
									$("#cnt_s_w_commonDiv").show();
									$("#cnt_s_w_inputButton").show();
								}else{
									$("#cnt_u_w_commonDiv").append(html);
									$("#cnt_u_w_commonDiv").show();
									$("#cnt_u_w_inputButton").show();
								}
							}
						}
					});
				}

			}else if(selectValue == 2){//按渠道指定柜台
				if(conditionType == 1){
					//去除树的勾选状态,并显示
					binOLSSPRM7302_global.sendTreeTmp.checkAllNodes(false);
					$("#cnt_s_w_treeDiv").show();
				}else{
					//去除树的勾选状态,并显示
					binOLSSPRM7302_global.useTreeTmp.checkAllNodes(false);
					$("#cnt_u_w_treeDiv").show();
				}
			}else if(selectValue == 3){//渠道选择
				if(conditionType == 1){
					binolssprm7302.searchChannelWay(0);
				}else{
					binolssprm7302.searchChannelWay(1);
				}
			}
		}else{
			//调用Ajax动态取用导入的白名单柜台
			var param="ruleCode="+$('#ruleCode').val()+"&filterType="+filterType+"&conditionType="+conditionType;
			if(conditionType == 1){
				param += "&contentNo=0";
				$("#cnt_s_b_div").empty();
			}else{
				param += "&contentNo="+$("#useContionDiv input[name='contentNo']").val();
				$("#cnt_u_b_div").empty();
			}
			cherryAjaxRequest({
				url: $("#getCNTListUrl").attr("href"),
				param: param,
				callback: function(msg) {
					if(msg == null || msg == "" || msg == undefined || msg == "ERROR"){
						return;
					}else{
						var counterList = eval("("+msg+")");
						var html="";
						for (var i=0;i<counterList.length;i++){
							html += '<li>';
							if(conditionType == 1){
								//html += '<input type="checkbox"value="'+counterList[i].organizationID+'"  onclick="binolssprm7302.cnt_s_removeLi(this,2,1);" checked="checked" />'+counterList[i].counterCode+'  '+counterList[i].counterName;
								html += counterList[i].counterCode+'  '+counterList[i].counterName;
							}else{
								//html += '<input type="checkbox"value="'+counterList[i].organizationID+'"  onclick="binolssprm7302.cnt_s_removeLi(this,2,2);" checked="checked" />'+counterList[i].counterCode+'  '+counterList[i].counterName;
								html += counterList[i].counterCode+'  '+counterList[i].counterName;
							}
							html += '<input name="organizationID" class="hide" value="'+counterList[i].organizationID+'"/>';
							html += '<input name="counterCode" class="hide" value="'+counterList[i].counterCode+'"/>';
							html += '<input name="counterName" class="hide" value="'+counterList[i].counterName+'"/>';
							html += '</li>';
						}
						if(conditionType == 1){
							$("#cnt_s_b_div").append(html);
						}else{
							$("#cnt_u_b_div").append(html);
						}
					}
				}
			});
		}
	},
	"cnt_s_b_allCheck":function(obj,conditionType){
		if ($(obj).is(":checked")) {
			if(conditionType == 1){
				$("#cnt_s_b_div input:checkbox").attr("checked","checked");
			}else{
				//TODO
			}
		}else{
			if(conditionType == 1){
				$("#cnt_s_b_div input:checkbox").attr("checked",false);
			}else{
				//TODO
			}
		}

	},
	"cnt_s_removeLi":function(obj,filterType,conditionType){
		//非选中状态下先删除后台导入的柜台再删除页面上的li节点
		var organizationID="";
		if($(obj).val()){
			organizationID=$(obj).val();
		}
		var ruleCode="";
		if($('#ruleCode').val()){
			ruleCode=$('#ruleCode').val();
		}
		if(!$(obj).is(':checked')){
			var param="filterType="+filterType+"&conditionType="+conditionType+"&organizationID="+organizationID+"&ruleCode="+ruleCode;
			if(conditionType == 1){
				param += "&contentNo=0";
			}else{
				param += "&contentNo="+$("#useContionDiv input[name='contentNo']").val();
			}
			cherryAjaxRequest({
				url: $("#delCNTUrl").attr("href"),
				param: param,
				callback: function(msg) {
					if(msg == null || msg == "" || msg == undefined || msg == "ERROR"){
						$(obj).attr("checked","checked");
						return;
					}else{
						if(msg > 0){
							$(obj).parents("li").remove();
						}else{
							$(obj).attr("checked","checked");
							return;
						}
					}
				}
			});
		}
	},

	/**
	 * 弹出产品框
	 * @param conditionType
	 * 1为发券门槛 2为使用门槛 3为券内容弹出产品框
	 * @param obj
     */
	"openProDialog2" :function(conditionType,obj) {
		var $parent;
		var proShowDiv;
		if (conditionType == '1') {
			//$parent = $("#sendCondition");
			proShowDiv = "prt_s_w_selPrtDIV";
		} else if (conditionType == '2'){
			//$parent = $("#useCondition");
			proShowDiv = "prt_u_w_selPrtDIV";
		} else if(conditionType== '3'){
			$parent = $(obj).closest('div.rule_content');
			proShowDiv = $parent.find('.contentProduct').attr('id');
		}
		//var proTable = "proTable";
		var prtVendorId = "prtVendId";
		var option = {
			targetId: proShowDiv,
			checkType : "checkbox",
			mode : 2,
			popValidFlag: 2,
			brandInfoId : $("#brandInfoId").val(),
			getHtmlFun:function(info){
				var html2 = '<li>';
				html2 +=  '<input type="checkbox" checked="checked" name="selPrtBlackChoose" onclick="binolssprm7302.removeLi(this);" value="' + info.proId  +  '"/>';
				html2 += '<input type="hidden" name="prtVendorId" value="' + info.proId + '"/><input type="hidden" name="'+prtVendorId+'" value="' + info.proId + '"/>';

				html2 += '<input type="hidden" name="prtObjId" value="' + info.proId + '"/>';
				html2 += '<input type="hidden" name="unitCode" value="' + info.unitCode + '"/>';
				html2 += '<input type="hidden" name="barCode" value="' + info.barCode + '"/>';
				html2 += '<input type="hidden" name="nameTotal" value="' + info.nameTotal + '"/>';

				html2 += info.unitCode;
				html2 += '&nbsp;' + info.barCode;
				html2 += '&nbsp;' + info.nameTotal;
				html2 += '&nbsp;&nbsp;<input type="text" style="width: 20px;" name="proNum" value="1" />';
				html2 += '</li>';
				return html2;
			},
			click : function() {
				if($("#"+proShowDiv).find("li").find("input[name='proNum']").length > 0) {
					$("#"+proShowDiv).find("li").find("input[name='proNum']").each(function(index){
						$(this).on('keyup',function(){
							var value = $(this).val();
							if((/^(\+|-)?\d+$/.test( value ))&&value>0){
							}else{
								//alert("数量中请输入正整数！");
								$(this).val(1);
								//return false;
							}
						})
					})
				}

				//alert("选择产品后的操作");
				//if($("#"+proShowDiv).find("tr").length > 0) {
				//	$parent.find("#"+proTable).show();
				//} else {
				//	$parent.find("#"+proTable).hide();
				//}

				//  onkeyup="this.value=this.value.replace(/\D/g,'')" onblur="this.value=this.value.replace(/\D/g,'')"


			}
		};
		popAjaxPrtDialog(option);
	},
	// 选择购买产品类别弹出框
	"openProTypeDialog2" :function(conditionType) {
		var proTypeShowDiv;
		var contentDiv;
		if (conditionType == '1') {
			proTypeShowDiv = "prt_s_w_typeDIV";
			contentDiv = 'prt_s_w_div';
		}  else if (conditionType == '2'){
			proTypeShowDiv = "prt_u_w_typeDIV";
			contentDiv = 'prt_u_w_div';
		}
		//var proTypeTable = "proTypeTable";
		var cateValId = "cateId";
		var option = {
			targetId: proTypeShowDiv,
			checkType : "checkbox",
			mode : 2,
			brandInfoId : $("#brandInfoId").val(),
			getHtmlFun:function(info){
				var html2 = '<li>';

				html2 +=  '<input type="checkbox" checked="checked" name="prtTypeBlackChoose" onclick="binolssprm7302.removeLi(this);" value="' + info.cateValId  +  '"/>';
				html2 += '<input type="hidden" name="cateValId" value="' + info.cateValId + '"/><input type="hidden" name="'+cateValId+'" value="' + info.cateValId + '"/>';

				html2 += '<input type="hidden" name="prtObjId" value="' + info.cateValId + '"/>';
				html2 += '<input type="hidden" name="cateVal" value="' + info.cateVal + '"/>';
				html2 += '<input type="hidden" name="cateValName" value="' + info.cateValName + '"/>';

				html2 += info.cateVal;
				html2 += ' ' + info.cateValName;
				html2 += '</li>';

				return html2;
			},
			click : function(){
				//$('#'+ contentDiv).find('#'+proTypeShowDiv).show();
				//$('#'+ contentDiv).children().not('#'+proTypeShowDiv).hide();
				//alert("选择分类后的操作");
				//$barCodes.not(":first").remove();
				//if($("#"+proTypeShowDiv).find("tr").length > 0) {
				//	$parent.find("#"+proTypeTable).show();
				//} else {
				//	$parent.find("#"+proTypeTable).hide();
				//}
			}
		};
		popAjaxPrtCateDialog(option);
	},
	// 回写导入产品数据
	"writeImpPrt" : function (conditionType,filterType,contentNo) {
		// 读取电子券产品明细表该RuleCode下的产品List
		var $ruleCode = $("#ruleCode");

		var url = $("#readCouponPrtDetailUrl").attr("href");
		//TODO
		var param = 'ruleCode=' + $ruleCode.val();
		param += '&conditionType=' + conditionType;
		param += '&filterType=' + filterType;
		param += '&contentNo=' + (conditionType == '1' ? '0' : contentNo);
		param += '&prtObjType=1';

		cherryAjaxRequest({
			url: url,
			param:param,
			callback: function(msg){
				//alert(msg);
				if (msg) {
					var map = JSON.parse(msg);
					var list = map.couponProductDetail;
					if(list.length != '0'){
						var html2="";
						$.each(list, function (i, item) {

							// 写到指定内容框
							html2 += '<li>';
							//html2 +=  '<input type="checkbox" checked="checked" name="selPrtBlackChoose" onclick="binolssprm7302.removeLi(this);binolssprm7302.delPrtLi('+conditionType+','+filterType+',this);"' +' value="' + item.prtObjId  +  '"/>';
							html2 += '<input type="hidden" name="prtVendorId" value="' + item.prtObjId + '"/><input type="hidden" name="prtVendId" value="' + item.prtObjId + '"/>';
							html2 += '<input type="hidden" name="proNum" value="' + item.proNum + '"/>';
							html2 += item.unitCode;
							html2 += ' ' + item.barCode;
							html2 += ' ' + item.nameTotal;
							if (filterType == '1') {
								html2 += '&nbsp;&nbsp;<input type="text" style="width: 20px;" name="prtNumLab" value="' + item.prtObjNum + '" disabled />';
							}

							html2 += '</li>';

						});

						var showImpPrtDIV = "";

						// 发送门槛
						if (conditionType == '1') {
							showImpPrtDIV = (filterType == '1') ? "prt_s_w_impPrtDIV" : "prt_s_b_impPrtDIV";
						}
						// 使用门槛
						else if(conditionType == '2'){
							showImpPrtDIV = (filterType == '1') ? "prt_u_w_impPrtDIV" : "prt_u_b_impPrtDIV";
						}

						$("#"+ showImpPrtDIV).html(html2);

						/*
						// 绑定产品数量事件
						if($("#"+showImpPrtDIV).find("li").find("input[name='prtNum']").length > 0) {
							$("#"+showImpPrtDIV).find("li").find("input[name='prtNum']").each(function(index){
								$(this).on('keyup',function(){
									var value = $(this).val();
									if((/^(\+|-)?\d+$/.test( value ))&&value>0){
									}else{
										//alert("数量中请输入正整数！");
										$(this).val(1);
										//return false;
									}
								})
							})
						}
						*/

					}
				}
			}
		});

	},
	"changePrtSpanDiv" : function (conditionType,filterType,obj){
		var prt_s_w_ShowDiv;
		var contentDiv;
		var btnSpan;
		var btnSpanItem;

		// 发送门槛
		if (conditionType == '1') {
			if (filterType == '1') {
				// 白名单
				contentDiv = 'prt_s_w_div'; // 产品发送门槛白名单DIV
				btnSpan = "prt_s_w_BtnSpan";
			}else {
				contentDiv = 'prt_s_b_div';
			}
		}
		// 使用门槛
		else if (conditionType == '2') {
			if (filterType == '1') {
				// 白名单
				contentDiv = 'prt_u_w_div'; // 产品发送门槛白名单DIV
				btnSpan = "prt_u_w_BtnSpan";
			}else {
				contentDiv = 'prt_u_b_div';
			}
		} else {

		}

		var productKbn = $("#"+obj).val();
		//alert(productKbn);
		if (productKbn == '0') {
			// 请选择
			prt_s_w_ShowDiv = (conditionType == '1') ? "prt_s_w_allDIV" : "prt_u_w_allDIV";
			this.switchPrtContentDiv(contentDiv,prt_s_w_ShowDiv);

			// 隐藏所有的按钮
			this.switchPrtBtnSpan(btnSpan);
		}
		else if (productKbn == '1') {
			// 分类
			prt_s_w_ShowDiv = (conditionType == '1') ? "prt_s_w_typeDIV" : "prt_u_w_typeDIV";
			this.switchPrtContentDiv(contentDiv,prt_s_w_ShowDiv);

			// 隐藏除分类选择之外的按钮
			btnSpanItem = (conditionType == '1') ? "prt_s_w_selTypeBtn" : "prt_u_w_selTypeBtn";
			this.switchPrtBtnSpan(btnSpan, btnSpanItem);

			//this.openProTypeDialog2(conditionType);
		} else if (productKbn == '2') {
			// 选择产品
			prt_s_w_ShowDiv = (conditionType == '1') ? "prt_s_w_selPrtDIV" : "prt_u_w_selPrtDIV";
			this.switchPrtContentDiv(contentDiv,prt_s_w_ShowDiv);

			// 隐藏除产品选择之外的按钮
			btnSpanItem = (conditionType == '1') ? "prt_s_w_selPrtBtn" : "prt_u_w_selPrtBtn";
			this.switchPrtBtnSpan(btnSpan, btnSpanItem);

			//this.openProDialog2(conditionType);
		} else if (productKbn == '3') {
			// 导入产品
			// 弹出导入画面
			prt_s_w_ShowDiv = (conditionType == '1') ? "prt_s_w_impPrtDIV" : "prt_u_w_impPrtDIV";
			this.switchPrtContentDiv(contentDiv,prt_s_w_ShowDiv);

			// 隐藏除产品导入之外的按钮
			btnSpanItem = (conditionType == '1') ? "prt_s_w_impPrtBtn" : "prt_u_w_impPrtBtn";
			this.switchPrtBtnSpan(btnSpan, btnSpanItem);

			//this.popProductLoadDialog(conditionType,filterType);
		}
		else {
			// 请选择
			prt_s_w_ShowDiv = (conditionType == '1') ? "prt_s_w_allDIV" : "prt_u_w_allDIV";
			this.switchPrtContentDiv(contentDiv,prt_s_w_ShowDiv);

			// 隐藏所有的按钮
			this.switchPrtBtnSpan(btnSpan);
		}
	},
	/**
	 * 切换选项时,设置提示信息
	 * @param dialogId
	 * @param titleVal
	 * @param textVal
	 * @param obj
	 */
	"switchAlert" : function (titleVal,conditionType,filterType,obj) {
		var $oldKbn = $("#" + obj).parent().parent().find(".oldKbn");

		var dialogId = 'switchBtnDialog';
		var option = {
			width:400,
			height:200,
			dialogInit:"#" + dialogId,
			text:'<p class="message"  ><span>'+'切换选项后,原选项设置内容将被清除.',
			title:titleVal,
			confirm:"确定",
			confirmEvent: function(){
				//binolssprm7302.saveUseContent(obj);
				$oldKbn.val($("#" + obj).val());
				binolssprm7302.changePrtSpanDiv(conditionType,filterType,obj);
				removeDialog("#" + dialogId);
			},
			cancel:"取消",
			cancelEvent: function(){
				$("#" + obj).val($oldKbn.val());
				removeDialog("#" + dialogId);
			}
		};
		openDialog(option);
		$(".ui-dialog-titlebar-close.ui-corner-all").hide();
	},
	"switchAlert_counter" : function (titleVal,conditionType,filterType,obj,isShow) {
		var $oldKbn = $(obj).parent().parent().find(".oldKbn");

		var dialogId = 'switchBtnDialog';
		var option = {
			width:400,
			height:200,
			dialogInit:"#" + dialogId,
			text:'<p class="message"  ><span>'+'切换选项后,原选项设置内容将被清除.',
			title:titleVal,
			confirm:"确定",
			confirmEvent: function(){
				//binolssprm7302.saveUseContent(obj);
				$oldKbn.val($(obj).val());
				binolssprm7302.changeCntSpanDiv(conditionType,filterType,obj,isShow);
				removeDialog("#" + dialogId);
			},
			cancel:"取消",
			cancelEvent: function(){
				$(obj).val($oldKbn.val());
				removeDialog("#" + dialogId);
			}
		};
		openDialog(option);
		$(".ui-dialog-titlebar-close.ui-corner-all").hide();
	},
	/**
	 * 产品类型内容框显示内容
	 * @param conditionType
	 * @param filterType
	 * @param obj
	 * @returns {boolean}
     */
	"changePrtSpan" : function (conditionType,filterType,obj,pageLoad){

		if (!pageLoad) {

			this.switchAlert('产品选项切换',conditionType,filterType,obj);
/*
			var $oldKbn = $("#" + obj).parent().parent().find(".oldKbn");
			var r = confirm("切换选项后,原选项设置内容将被清除");
			if (r==true){
				$oldKbn.val($("#" + obj).val());
			} else {
				$("#" + obj).val($oldKbn.val()  );
				return false;
			}
			*/
		} else {
			binolssprm7302.changePrtSpanDiv(conditionType,filterType,obj);
		}

		//alert(obj);

	},
	// 切换 产品框
	"switchPrtContentDiv" : function(contentDiv,prtShowDiv){

		// 显示产品内容框
		$('#' + contentDiv).find('#' + prtShowDiv).show(); // 显示当前内容框
		$('#' + contentDiv).children().not('#' + prtShowDiv).hide(); // 隐藏其他内容框
		$('#' + contentDiv).children().not('#' + prtShowDiv).empty(); // 清除其他内容框内容
	},
	// 切换 产品相关按钮
	"switchPrtBtnSpan" : function (btnSpan,btnSpanItem) {
		// 隐藏除当前产品选择项之外的按钮
		if (btnSpanItem) {
			$('#' + btnSpan).find('#' + btnSpanItem).show();
			$('#' + btnSpan).children().not('#' + btnSpanItem).hide();
		}
		// 隐藏所有按钮
		else {
			$('#' + btnSpan).children().hide();
		}
	},
	// 门槛条件/黑白名单过滤类型/contentNo
	"delPrtForConditionTypeFilterTypeContentNo" : function () {

		var $ruleCode = $("#ruleCode");

		var url = $("#delCouponPrtDetailForConditionTypeFilterTypeContentNoUrlUrl").attr("href");
		//TODO
		var param = 'ruleCode=' + $ruleCode.val();
		param += '&conditionType=' + conditionType;
		param += '&filterType=' + filterType;
		param += '&contentNo=' + (conditionType == '1' ? '0' : contentNo);
		param += '&prtObjType=1';

		cherryAjaxRequest({
			url: url,
			param:param,
			callback: function(msg){
				// 删除指定节点

			}
		});
	},
	// key-value
	"setKeyVal": function (key,val) {
		var rst = '"'+key+'":"'+
			encodeURIComponent(val.replace(/\\/g,'\\\\').replace(/"/g,'\\"'))+'"';
		return rst;
	} ,
	// 取得指定DIV里面的产品/分类List
	"getPrtParamList" : function (prtShowDiv) {
		var prtArr = [];
		$("#" + prtShowDiv).find('li').each(function() {
			prtArr.push(binolssprm7302.convertParams(this));
		});
		return prtArr;
	},
	// 保存产品
	"convertPrt" : function(conditionType,contentNo,filterType) {

		// 产品参数
		var prtParamArr =[];
		var prt_s_wArr =[];
		var prt_s_bArr = [];
		// return "{" + arr.toString() + "}";
		// param += "&couponRule.content=[" + arr.toString() + "]";

		var productKbn = $("#prt_s_w_productKbn").val();
		var prtShowDiv;

			//prtParamArr.push(binolssprm7302.setKeyVal("conditionType","1"));
			//prtParamArr.push(binolssprm7302.setKeyVal("filterType","1"));


		if (conditionType == '1') {
			// 白名单

			// prtParam += "&couponRule.content=[" + arr.toString() + "]";

			if (productKbn == '0') {
				prtShowDiv = "prt_s_w_allDIV";
			}
			else if (productKbn == '1') {
				// 分类
				prtShowDiv = "prt_s_w_typeDIV";

			} else if (productKbn == '2') {
				// 选择产品
				prtShowDiv = "prt_s_w_selPrtDIV";

			} else if (productKbn == '3') {
				// 导入产品
				// 弹出导入画面
				prtShowDiv = "prt_s_w_impPrtDIV";

			}
			else {
				return true;
			}

			prt_s_wArr.push(this.setKeyVal("productKbn",productKbn));
			var prt_s_w_prtShowDiv_Arr = this.getPrtParamList(prtShowDiv);
			if (prt_s_w_prtShowDiv_Arr.length > 0) {
				prt_s_wArr.push('"prtList":[' + prt_s_w_prtShowDiv_Arr.toString() + ']');
			}
			prtParamArr.push('"prt_s_wParam":{' + prt_s_wArr.toString() + '}'); // 发送门槛产品白名单

			// 黑名单
			prt_s_bArr.push(this.setKeyVal("productKbn","3"));
			var prt_s_b_impPrtDIV_Arr = this.getPrtParamList("prt_s_b_impPrtDIV");

			if (prt_s_b_impPrtDIV_Arr.length > 0) {
				prt_s_bArr.push('"prtList":[' + prt_s_b_impPrtDIV_Arr.toString() + ']');
			}
			prtParamArr.push('"prt_s_bParam":{' + prt_s_bArr.toString() + "}"); // 发送门槛产品黑名单

		}

		return "{" + prtParamArr.toString() + "}";
	},
	//切换发券对象span
	"changeMemberSpanDiv":function(conditionType,contentNo,filterType,obj){
		var type = $(obj).val();
		var $parent = $(obj).parents(".memberWriteDiv");
		//隐藏div
		$parent.find(".memberInput").addClass("hide");
		$parent.find(".memberLevel").addClass("hide");
		$parent.find(".changeSpan").addClass("hide");
		if(type==1){//如果type为1,则是导入会员
			$parent.find(".changeSpan").removeClass("hide");
			$parent.find(".memberInput").empty();
			$parent.find(".memberInput").removeClass("hide");
			//binolssprm7302.getExeclUploadMemberList(conditionType,'1',$parent.find(".memberInput"),contentNo);
		}else if (type==2){//如果type为2,则是会员等级
			if($parent.find(".memberLevel")){
				binolssprm7302.getMemLevel(conditionType,$parent.find(".memberLevel"),contentNo);
			}
			$parent.find(".memberLevel").removeClass("hide");
		}else {//如果type为0和3,则是默认和非会员

		}
	},
	//获取会员等级列表
	"getMemLevel":function(conditionType,obj,contentNo){
		var ruleCode = $("#ruleCode").val();
		var url = $("#getMemLevelListUrl").attr("href");
		var param = 'ruleCode='+ruleCode+'&conditionType='+conditionType+'&contentNo='+contentNo;
		cherryAjaxRequest({
			url:url,
			param:param,
			//dataType:'json',
			callback:function(msg){
				var list = JSON.parse(msg);
				if(list.length>0){
					var resultStr = '';
					for(var i=0;i < list.length; i++){
						resultStr += '<li>';
						resultStr +=  '<input type="checkbox" name="memLevel"/>';
						resultStr +='<input type="hidden" name="level" value="'+list[i].levelId+'"/>';
						resultStr +='<span>'+list[i].levelName+'</span>'+'<br/>';
						resultStr += '</li>';
					}
					$(obj).empty();
					$(obj).append(resultStr);
				}
			}
		});
	},
	//获取导入会员名单
	"getExeclUploadMemberList":function(conditionType,filterType,obj,contentNo){
		var ruleCode = $("#ruleCode").val();
		var url = $("#getExeclUploadMemberListUrl").attr("href");
		var param = 'ruleCode='+ruleCode+'&conditionType='+conditionType+'&filterType='+filterType+'&contentNo='+contentNo;
		cherryAjaxRequest({
			url:url,
			param:param,
			//dateType:'json',
			callback:function(msg){
				var list = JSON.parse(msg);
				if(list.length>0){
					var resultStr = '';
					for(var i=0; i < list.length;i++){
						resultStr += '<li>';
						//resultStr +='<input type="checkbox" checked="checked" onclick="binolssprm7302.delImpCouponMemberDetail('+conditionType+','+filterType+','+contentNo+',this);return false;"/>';
						resultStr +='<input type="hidden" name="mobile" value="'+list[i].mobile+'"/>';
						resultStr +='<span>'+list[i].mobile+'&nbsp;'+list[i].memberCode+'</span>';
						resultStr += '</li>';
					}
					$(obj).html(resultStr);
				}
			}
		});
	},
	"popCampDialog":function(conditionType,filterType,obj){
		var $parent = $(obj).parents(".campDiv").find(".campList");
		var url ;
		var valueArr = [];//campListWhiteChoose
		$parent.find('input:checked').each(function(){
			valueArr.push($(this).next('input').val());
		});
		var callback = function(tableId) {
			$parent.removeClass("hide");
			$("#"+tableId).find(":input[checked]").each(function() {
				var campaignCode = $(this).val();
				if (valueArr.length > 0) {
					var isExist = false;
					for(var i in valueArr) {
						if (valueArr[i] == campaignCode) {
							isExist = true;
							break;
						}
					}
					if (isExist) {
						return true;
					}
				}

				var campaignMode = $(this).next().val();
				var campaignName = $(this).parent().next().text();
				var html = '<li>';
				html += '<input type="checkbox" name="campListWhiteChoose" checked="checked"  onclick="binolssprm7302.removeLi(this);" />';
				html += '<input type="hidden" name="campaignCode" value="'+campaignCode+'"/>';
				html += '<input type="hidden" name="campaignMode" value="'+campaignMode+'"/>'
				html += '<input type="hidden" name="campaignName" value="'+campaignName+'"/>';
				html += campaignCode+'&nbsp;'+campaignName;
				html += '&nbsp;';
				if (campaignMode == '0') {
					html += '会员活动';
				} else if (campaignMode == '1') {
					html += '促销活动';
				} else if (campaignMode == '2')  {
					html += '电子券活动';
				}

				html += '</li>';
				$parent.append(html);
			});
			$("#"+tableId).find("input[name='campaignCode']:not(:checked)").each(function() {
				var campaignCode = $(this).val();
				$parent.find('input:checked').each(function(){
					if($(this).next('input').val()==campaignCode){
						binolssprm7302.removeLi(this);
					}
				});
			});
		}
		if(filterType==1){
			 url = $("#searchCampaignInitUrl").attr('href');
			 popDataTableOfCampaignList(url, 'checkType=checkbox', null, callback, valueArr);
		}else{
			 var ruleCode = $("#ruleCode").val();
			 url = $("#searchCampaignInitUrl2").attr('href');
			 popDataTableOfCampaignList2(url, 'checkType=checkbox&conditionType='+conditionType+'&ruleCode='+ruleCode, null, callback, valueArr);
		}
	},
	"removeLi" : function (obj) {
		$(obj).parents("li").remove();
	},
	"delPrtLi" : function(conditionType,filterType,obj) {
		var $ruleCode = $("#ruleCode");

		var url = $("#delImpCouponPrtDetailForNodeUrl").attr("href");
		var param = 'ruleCode=' + $ruleCode.val();
		param += '&conditionType=' + conditionType;
		param += '&filterType=' + filterType;
		param += '&contentNo=' + (conditionType == '1' ? '0' : $("#useContionDiv input[name='contentNo']").val());
		param += '&prtObjType=1';
		param += '&prtObjId=' + $(obj).parent().find("input[name='prtVendorId']").val();


		cherryAjaxRequest({
			url: url,
			param: param,
			callback: function (msg) {
				//alert(msg);
				if (msg) {
					var map = JSON.parse(msg);
				}
			}
		});
	},


	"tabDiv":function(obj){
		if($(obj).parents(".box4-content.clearfix").children("div:last").is(":visible")){
			//$(obj).parents("div").find()attr("class","ui-icon icon-arrow-crm");
			$(obj).parents(".box4-content.clearfix").children("div:last").hide("slow");
		}else{
			//$(obj).parents("div").attr("class","ui-icon ui-icon-triangle-1-s")
			$(obj).parents(".box4-content.clearfix").children("div:last").show("slow");
		}
	},
	"getCounterResult":function(){
			//获取发券白名单柜台
			var cnt_s_w_counter=new Array();
			if($("#cnt_s_w_select").val() == 1){
				$("#cnt_s_w_commonDiv li").each(function(){
					//if($(this).find("input").is(":checked")){
						cnt_s_w_counter.push($(this).find("input").val().toString());
					//}
				});

			}else if($("#cnt_s_w_select").val() == 2){//渠道指定柜台
				var nodes = binOLSSPRM7302_global.sendTreeTmp.getCheckedNodes(true);
				for(var i=0;i<nodes.length;i++){
					if(nodes[i].level == "1"){
						cnt_s_w_counter.push(nodes[i].id.replace("D",'').toString());
					}
				}
			}
			//获取发券黑名单柜台
			var cnt_s_b_counter=new Array();
			$("#cnt_s_b_div li").each(function(){
				//if($(this).find("input").is(":checked")){
					cnt_s_b_counter.push($(this).find("input").val().toString());
				//}
			});
			//黑名单内容TODO
			var map={
				"counterKbn_w": $("#cnt_s_w_select").val() ,
				"counterKbn_b": 1 ,
				"counterList_w":cnt_s_w_counter,
				"counterList_b":cnt_s_b_counter
			}

		return JSON2.stringify(map);
	},
	/**
	 * 使用门槛-确定后处理导入数据的问题
	 * @param contentNo
	 *
     */
	"confirmUseCond" : function(contentNo) {

		// 删除IsTemp=2的,并把IsTemp=3的改为2
		var $ruleCode = $("#ruleCode");

		var url = $("#confirmUseCondUrlID").attr("href");

		var param = 'ruleCode=' + $ruleCode.val();
		param += '&contentNo=' + $("#useContionDiv input[name='contentNo']").val();

		cherryAjaxRequest({
			url: url,
			param:param,
			callback: function(msg){
				//alert(msg);
				if (msg) {
					var map = JSON.parse(msg);
					console.log(map);
				}
			}
		});

	},

	/**
	 * 使用门槛-取消后处理导入数据的问题
	 * @param contentNo
	 * cancelUseCondUrlID
     */
	"cancelUseCond" : function(contentNo) {
		// 删除IsTemp=3
		var $ruleCode = $("#ruleCode");

		var url = $("#cancelUseCondUrlID").attr("href");

		var param = 'ruleCode=' + $ruleCode.val();
		param += '&contentNo=' + $("#useContionDiv input[name='contentNo']").val();

		cherryAjaxRequest({
			url: url,
			param:param,
			callback: function(msg){
				//alert(msg);
				if (msg) {
					var map = JSON.parse(msg);
					console.log(map);
				}
			}
		});

	},

	// pop使用门槛
	"popUseContionDialog":function(obj,type){
		var contentNo;
		if(type == 1){
			contentNo=$(obj).parents(".box2-content.clearfix.rule_content").find("input[name='contentNo']").val();
		}else{
			contentNo=$(obj).parents(".sendCondType").find("input[name='contentNo']").val();
		}
		var dialogId = '#popUseContionDialog';
		if($(dialogId).length == 0) {
			$('body').append('<div style="display:none" id="popUseContionDialog"></div>');
		} else {
			$(dialogId).empty();
		}
		var option = {
			width:1100,
			height:500,
			dialogInit:dialogId,
			//text:"使用门槛",
			title:"使用门槛",
			confirm:"确定",
			confirmEvent: function(){
				if($("#useTimeType").val() == 0){
					if(!$("#useStartTime").val() || !$("#useEndTime").val()){
						$("#useTimeType").parents("div").show();
						binolssprm7302.showErrorMsg("使用时间中指定时间不能为空");
						return false;
					}
					if ($('#sendStartTime').val()>$("#useStartTime").val()){
						$("#useTimeType").parents("div").show();
						binolssprm7302.showErrorMsg("券的指定日期开始时间不能早于发券时间");
						return false;
					}

				}else{
					if(!$("#afterDays").val() || !$("#validity").val()){
						$("#useTimeType").parents("div").show();
						binolssprm7302.showErrorMsg("参考发券时间中后几天与有效期不能为空");
						return false;
					}
					if ($("#validity").val()<1){
						$("#useTimeType").parents("div").show();
						binolssprm7302.showErrorMsg("使用时间有效期必须大于0");
						return false;
					}
				}
				if (!$('#useForm').valid()) {
					$("#useTimeType").parents("div").show();
					return false;
				};
				if(!binolssprm7302.validCounter(2) || !binolssprm7302.validProduct(2)){
					return false;
				}
				if (!binolssprm7302.validUseMemeberCondition()) {
					return false;
				}

				// 使用门槛-确定后处理导入数据的问题
				binolssprm7302.confirmUseCond(contentNo);

				binolssprm7302.saveUseContent(obj);
				removeDialog(dialogId);
			},
			cancel:"关闭",
			cancelEvent: function(){

				// 使用门槛-取消后处理导入数据的问题
				binolssprm7302.cancelUseCond(contentNo);
				removeDialog(dialogId);
			}
		};
		openDialog(option);
		var params="contentNo="+contentNo;
		params +="&ruleCode="+$('#ruleCode').val();
		params += "&useCondJson=" + $(obj).parent().find("input[name='useCondJson']").val();
		var initUseContentUrl=$("#initUseContentUrl").attr("href");
		cherryAjaxRequest({
			url: initUseContentUrl,
			param: params,
			callback: function(data) {
				$(dialogId).html(data);
			}
		});
	},
	"saveUseContent":function(obj){
		if (!$('#useForm').valid()) {
			return false;
		};
		//所有Kbn值处理
		var map = new Object();
		var length=$("#useContionDiv").find(":input.z-param").length;
		$("#useContionDiv").find(":input.z-param").each(function(index) {
			var name = $(this).attr("name");
			if (!name || $(this).is(":disabled") ||
				($(this).is(':radio') || $(this).is(':checkbox')) && !$(this).is(':checked')) {
				return true;
			}
			map[name]=encodeURIComponent($.trim($(this).val().replace(/\\/g,'\\\\').replace(/"/g,'\\"')));
		});
		map.contentNo=$("#useContionDiv input[name='contentNo']").val();
		//柜台处理
		binolssprm7302.useContionHandle_cnt(map);
		//产品处理
		binolssprm7302.useContionHandle_prt(map);
		//活动处理
		binolssprm7302.useContionHandle_cam(map);
		//金额处理
		binolssprm7302.getAmountCondition(map);
		//使用对象
		binolssprm7302.getUseMemeberCondition(map);
		//使用时间处理
		var useTimeJson=binolssprm7302.convertParamsUseType("#useTimeSpan" + $("#useTimeType").val());
		if(useTimeJson){
			map.useTimeJson=eval("("+useTimeJson+")");
		}
		$(obj).parent().find("input[name='useCondJson']").attr("value",JSON2.stringify(map));
	},

	"validUseMemeberCondition":function(){
		var mem_s_w_select_obj=$("#member_u_w_Kbn");
		if ($('#member_u_w_Kbn option:selected').val()==2){
			var p="";
			$("#mem_u_w_commonDiv").find(':input[name="memLevel"]:checked').each(function(){
				p=p+$(this).next().val()+",";
			});
			if (p.length == 0){
				mem_s_w_select_obj.parents("div").show();
				binolssprm7302.showErrorMsg("使用门槛使用对象按会员等级情况下必须选择对应的会员等级");
				return false;
			}else{
				return true;
			}
		}else{
			return true;
		}
	},
	//使用门槛中的使用对象
	"getUseMemeberCondition":function(map){
		//var arr = new Array();
		var memLevel_w="";
		var mem_s_w_select_obj=$("#member_u_w_Kbn");
		if ($('#member_u_w_Kbn option:selected').val()==2){
			var p="";
			$("#mem_u_w_commonDiv").find(':input[name="memLevel"]:checked').each(function(){
				p=p+$(this).next().val()+",";
				//p.push($(this).next().val());
			});
			if (p.length > 0) {
				p=p.substring(0,p.length-1);
				memLevel_w=p;
				//arr.push('"' + p.toString() + '"');
			}
		}

		map.memberKbn_w=$('#member_u_w_Kbn option:selected').val();
		map.memLevel_w=memLevel_w;
	},
	//转化金额
	"getAmountCondition":function(map){
		map.amountCondition=$("#amountCondition").val();
		map.useMinAmount=$("#useMinAmount").val();
	},
	"useContionHandle_cnt":function(map){
		//获取用券白名单柜台
		var cnt_u_w_counter=new Array();
		if($("#cnt_u_w_select").val() == 2){//按照渠道指定柜台
			var nodes = binOLSSPRM7302_global.useTreeTmp.getCheckedNodes(true);
			for(var i=0;i<nodes.length;i++){
				if(nodes[i].level == "1"){
					var counter=new Object();
					counter.counterCode=nodes[i].counterCode;
					counter.organizationID=nodes[i].id.replace("D",'').toString();
					counter.counterName=nodes[i].name;
					cnt_u_w_counter.push(counter);
				}
			}
		}else if($("#cnt_u_w_select").val() == 3){//渠道选择
			$("#cnt_u_w_commonDiv li").each(function(){
				if($(this).find("input").is(":checked")){
					var counter=new Object();
					counter.id=$(this).find("input[name='id']").val();
					counter.name=$(this).find("input[name='name']").val();
					cnt_u_w_counter.push(counter);
				}
			});
		}
		if($("#cnt_u_w_select").val() == 3){
			map.cntChannelList_w=cnt_u_w_counter;
		}else{
			if($("#cnt_u_w_select").val() == 1){
				if($("#cnt_u_w_commonDiv").find('li').length > 0){
					map.cntListCheckedFlag_w="1";
				}else{
					map.cntListCheckedFlag_w="0";
				}
			}else{
				map.counterList_w=cnt_u_w_counter;
			}
		}


		//获取用券黑名单柜台
		if($("#cnt_u_b_div").find('li').length > 0){
			map.cntListCheckedFlag_b="1";
		}else{
			map.cntListCheckedFlag_b="0";
		}

	},
	"useContionHandle_prt":function(map){
		//获取用券产品白名单
		var prt_u_w=new Array();
		var prt_u_w_productKbn=$("#prt_u_w_productKbn").val();
		if(prt_u_w_productKbn == 0){//请选择情况
			return;
		}else if(prt_u_w_productKbn == 1){//分类选择情况
			$("#prt_u_w_typeDIV li").each(function(){
				if($(this).find("input[type='checkbox']").is(":checked")){
					var prt_type=new Object();
					prt_type.cateValId=$(this).find("input[name='cateValId']").val();
					prt_type.cateId=$(this).find("input[name='cateId']").val();
					prt_type.prtObjId=$(this).find("input[name='prtObjId']").val();
					prt_type.cateVal=$(this).find("input[name='cateVal']").val();
					prt_type.cateValName=$(this).find("input[name='cateValName']").val();
					prt_u_w.push(prt_type);
				}
			});
		}else if(prt_u_w_productKbn == 2 || prt_u_w_productKbn == 3){//产品选择和导入的情况
			$("#prt_u_w_selPrtDIV li").each(function(){
				if($(this).find("input[type='checkbox']").is(":checked")){
					var prt_w=new Object();
					prt_w.prtVendorId=$(this).find("input[name='prtVendorId']").val();
					prt_w.prtVendId=$(this).find("input[name='prtVendId']").val();
					prt_w.prtObjId=$(this).find("input[name='prtObjId']").val();
					prt_w.unitCode=$(this).find("input[name='unitCode']").val();
					prt_w.barCode=$(this).find("input[name='barCode']").val();
					prt_w.nameTotal=$(this).find("input[name='nameTotal']").val();
					prt_w.proNum=$(this).find("input[name='proNum']").val();
					prt_u_w.push(prt_w);
				}
			});
		}
		map.prtList_w=prt_u_w;
		//获取用券产品黑名单
		var prt_u_b=new Array();
		$("#prt_u_b_impPrtDIV li").each(function(){
			if($(this).find("input[type='checkbox']").is(":checked")){
				var prt_b=new Object();
				prt_b.prtObjId=$(this).find("input[name='prtObjId']").val();
				prt_b.unitCode=$(this).find("input[name='unitCode']").val();
				prt_b.barCode=$(this).find("input[name='barCode']").val();
				prt_b.nameTotal=$(this).find("input[name='nameTotal']").val();
				prt_b.proNum=$(this).find("input[name='proNum']").val();
				prt_u_b.push(prt_b);
			}
		});
		map.prtList_b=prt_u_b;
	},
	"useContionHandle_cam":function(map){
		var cam_u_w=new Array();
		//活动用券白名单
		$("#rul_u_w_commonDiv li").each(function(){
			if($(this).find("input[type='checkbox']").is(":checked")){
				var cam_w=new Object();
				cam_w.campaignCode=$(this).find("input[name='campaignCode']").val();
				cam_w.campaignMode=$(this).find("input[name='campaignMode']").val();
				cam_w.campaignName=$(this).find("input[name='campaignName']").val();
				cam_u_w.push(cam_w);
			}
		});
		map.campList_w=cam_u_w;

		var cam_u_b=new Array();
		//活动用券黑名单
		$("#rul_u_b_div li").each(function(){
			if($(this).find("input[type='checkbox']").is(":checked")){
				var cam_b=new Object();
				cam_b.campaignCode=$(this).find("input[name='campaignCode']").val();
				cam_b.campaignMode=$(this).find("input[name='campaignMode']").val();
				cam_b.campaignName=$(this).find("input[name='campaignName']").val();
				cam_u_b.push(cam_b);
			}
		});
		map.campList_b=cam_u_b;
	},
	"getUseCondAll":function(){//获取使用门槛
		var UseCondArr=new Array();
		if($("#couponFlag").val() == 9){//券包情况
			//同一门槛
			if($("#useCondTypeCheckbox").is(":checked") ){
				if($("#useCondTypeCheckbox").parents("div").find("input[name='useCondJson']").val()){
					var UseCond=eval("("+$("#useCondTypeCheckbox").parents("div").find("input[name='useCondJson']").val()+")");
					UseCondArr.push(UseCond);
				}
			}else{
				$("#coupon-content .box2-content.clearfix.rule_content").each(function(){
					if($(this).find("input[name='useCondJson']").val()){
						var useCond=eval("("+$(this).find("input[name='useCondJson']").val()+")");
						UseCondArr.push(useCond);
					}
				});
			}
		}else{
			$("#coupon-content .box2-content.clearfix.rule_content").each(function(){
				if($(this).find("input[name='useCondJson']").val()){
					var useCond=eval("("+$(this).find("input[name='useCondJson']").val()+")");
					UseCondArr.push(useCond);
				}
			});
		}
		return JSON2.stringify(UseCondArr);
	},
	"delImpCouponMemberDetail":function(conditionType,filterType,contentNo,obj){
		var url = $("#delImpCouponMemberDetail").attr("href");
		var ruleCode = $("#ruleCode").val();
		var mobile = $(obj).next().val();
		var param = "ruleCode="+ruleCode+"&conditionType="+conditionType+"&filterType="+filterType+"&contentNo="+contentNo+"&mobile="+mobile;
		cherryAjaxRequest({
			url:url,
			param:param,
			callback:function(msg){
				if(msg=='1'||msg==1){
					binolssprm7302.removeLi(obj);
				}
			}
		});
	},
	"changeMemberSpan":function(conditionType,contentNo,filterType,obj,pageFlag){
		if(!pageFlag){
			binolssprm7302.alertMemberDialog(conditionType,contentNo,filterType,obj);
		}else{
			binolssprm7302.changeMemberSpanDiv(conditionType,contentNo,filterType,obj);
		}
	},
	"alertMemberDialog":function(conditionType,contentNo,filterType,obj){
		var $oldKbn = $(obj).parent().find('.oldKbn');
		var dialogId = 'switchBtnDialog';
		var option = {
			width:400,
			height:200,
			dialogInit:"#" + dialogId,
			text:'<p class="message"  ><span>'+'切换选项后,原选项设置内容将被清除.',
			title:"会员切换",
			confirm:"确定",
			confirmEvent: function(){
				$oldKbn.val($(obj).val());
				binolssprm7302.changeMemberSpanDiv(conditionType,contentNo,filterType,obj);
				removeDialog("#" + dialogId);
			},
			cancel:"取消",
			cancelEvent: function(){
				$(obj).val($oldKbn.val());
				removeDialog("#" + dialogId);
			}
		};
		openDialog(option);
		$(".ui-dialog-titlebar-close.ui-corner-all").hide();
	},
	"contentDivShow":function(obj){
		var $nextDiv = $(obj).next();
		if($nextDiv.is(":visible")){
			$nextDiv.hide("slow");
		}else{
			$nextDiv.show("slow");
		}
	}
};

window.onbeforeunload = function(){
	if (window.opener) {
		if (binolssprm7302.doRefresh) {
			// 刷新父页面
			window.opener.reSearch();
		}
	}
};

var binolssprm7302 =  new BINOLSSPRM7302();
var maxContentNo=0;
var maxContentNoCopy=0;
$(function(){
		binolssprm7302.searchChannel('/Cherry/ss/BINOLSSPRM73_channel.action',0);
	$( "#accordion" ).accordion();
	$(".ui-accordion-content").css("height","");
	//$(".date").attr("style", "font-size:11px;")
	$('#sendStartTime').cherryDate({
		beforeShow: function(input){
			var value = $('#sendEndTime').val();
			return [value,'maxDate'];
		}
	});
	$('#sendEndTime').cherryDate({
		beforeShow: function(input){
			var value = $('#sendStartTime').val();
			return [value,'minDate'];
		}
	});

/*	$('#useStartTime').cherryDate({
		beforeShow: function(input){
			var value = $('#useEndTime').val();
			return [value,'maxDate'];
		}
	});
	$('#useEndTime').cherryDate({
		beforeShow: function(input){
			var value = $('#useStartTime').val();
			return [value,'minDate'];
		}
	});*/

	// TODO 加载产品发送门槛的产品数据
	binolssprm7302.changePrtSpan('1','1','prt_s_w_productKbn',1111);
	// 显示发送门槛产品黑名单导入产品内容框
	if ($("#prt_s_b_productKbn").val() == '3') {
		$("#prt_s_b_impPrtDIV").show();
	}

	// 表单验证初期化
	cherryValidate({
		formId: 'mainForm',
		rules: {
			"couponRule.ruleName": {required: true},
			"couponRule.sendStartTime": {required: true, dateValid: true},
			"couponRule.sendEndTime": {required: true, dateValid: true},
			//useStartTime: {dateValid: true},
			//useEndTime: {dateValid: true},
			//afterDays: {number:true, min:0},
			//validity: {number:true, min:1},
			//"couponRule.sumQuantity": {number:true, min:1},
			//"couponRule.limitQuantity": {number:true, min:1},
			"couponRule.quantity": {required: true,number:true, min:1}
			//faceValue: {required: true, floatValid:[6,2]}
		}
	});
/*	if($("#cnt_s_w_select").val() == 2){
		binolssprm7302.searchChannel('/Cherry/ss/BINOLSSPRM73_channel.action',0,1);
	}*/
	if($("#cnt_s_w_select").val() != 2){
		$("#cnt_s_w_commonDiv").show();
	}else{
		$("#cnt_s_w_commonDiv").hide();
	}


	$("#useCondTypeCheckbox").click(function(){
		if($("#useCondTypeCheckbox").prop("checked")){
			$(".useCondButton").hide();
			$(".useCondButtonAll").show();
		}else{
			$(".useCondButton").show();
			$(".useCondButtonAll").hide();
		}
		//alert(this.checked?"勾上了":"取消了勾选");
	});

	if ($('#couponFlag option:selected') .val()!='9'){
		$(".sendCondType").hide();
	}else{
		$(".sendCondType").show();
		$(".useCondType").show();
		if($("#useCondTypeCheckbox").prop("checked")){
			$(".useCondButtonAll").show();
		}else{
			$(".useCondButton").show();
		}
	}
	maxContentNo=parseInt($('#maxContentNo').val());
	maxContentNoCopy=maxContentNo;
});
function BINOLSSPRM7302() {
	
};

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
		$parent.find('#' + prefix + $(obj).val()).show();
	},
	"changeCouponType":function(obj) {
		var value = $(obj).val();
		$('#coupon-content').html($('#coupon-content' + value).html());
		if (value == '5') {
			$('#coupon-content').find('#proContent').attr('id', 'proContent1');
		} else if (value == '2') {
			$('#coupon-content').find('#conProShowDiv').attr('id', 'conProShowDiv1');
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
	/*
	 *保存优惠券规则
	 */
	"saveRule" : function (){
		if (!$('#mainForm').valid()) {
			return false;
		};
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
		param += "&couponRule.useTimeJson=" 
			+ binolssprm7302.convertParams("#useTimeSpan" + $("#useTimeType").val());
		var arr = [];
		$('#contentDiv').find(".rule_content").each(function() {
			arr.push(binolssprm7302.convertContent(this));
		});
		param += "&couponRule.content=[" 
			+ arr.toString() + "]";
		param += "&couponRule.sendCond=" 
			+ binolssprm7302.convertCondition('#sendCondition');
		param += "&couponRule.useCond=" 
			+ binolssprm7302.convertCondition('#useCondition');
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
	"convertCondition" : function(id) {
		var $proShowDiv;
		var $proTypeShowDiv;
		var $camp_tbody;
		if (id == '#sendCondition') {
			$proShowDiv = $('#proShowDiv');
			$proTypeShowDiv = $('#proTypeShowDiv');
			$camp_tbody = $('#sendCondition').find('#camp_tbody');
		} else {
			$proShowDiv = $('#useProShowDiv');
			$proTypeShowDiv = $('#useProTypeShowDiv');
			$camp_tbody = $('#useCondition').find('#camp_tbody');
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
			p.push($(this).val());
		});
		if (p.length > 0) {
			arr.push('"memLevel":' + '"' + p.toString() + '"');
		}
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
		var campArr = [];
		$camp_tbody.find('tr').each(function() {
			campArr.push(binolssprm7302.convertParams(this));
		});
		if (campArr.length > 0) {
			arr.push('"campList":[' + campArr.toString() + ']');
		}
		return "{" + arr.toString() + "}";
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
    "openProDialog" :function(conditionType, obj) {
    	var $parent;
    	var proShowDiv;
    	if (conditionType == '1') {
    		$parent = $("#sendCondition");
    		proShowDiv = "proShowDiv";
    	} else if (conditionType == '2'){
    		$parent = $("#useCondition");
    		proShowDiv = "useProShowDiv";
    	} else {
    		$parent = $(obj).closest('div.rule_content');
    		proShowDiv = $parent.find('.z-tbody').attr('id');
    	}
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
		if (conditionType == '1') {
    		$parent = $("#sendCondition");
    	}  else if (conditionType == '2') {
    		$parent = $("#useCondition");
    	} else {
    		$parent = $(obj).closest('div.rule_content');
    	}
		var $tbody = $parent.find("#camp_tbody");
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
				var html = '<tr style="float:left;margin-left:10px;"><td><span class="list_normal">';
				html += '<span class="text" style="line-height:19px;">' + $(this).parent().next().text() + '</span>';
       			html += '<span class="close" style="margin: 0 1px 2px 5px;" onclick="binolssprm7302.delCampaignHtml(this);return false;"><span class="ui-icon ui-icon-close"></span></span>';
       			html += '<input type="hidden" name="campaignMode" value="' + $(this).next().val() + '"/>';
       			html += '<input type="hidden" name="campaignCode" value="' + campaignCode + '"/>';
       			html += '</span></td></tr>';
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
		var $channelId;
		var $memCounterId;
		var $channelNameDiv;
		if('0' == type){//0为发送门槛渠道选择
			$channelId = $("#sendChannelId");
			$memCounterId = $("#sendMemCounterId");
			$channelNameDiv = $("#sendThresholdDiv");
		}
		else{//使用门槛渠道选择
			$channelId = $("#useChannelId");
			$memCounterId = $("#useMemCounterId");
			$channelNameDiv = $("#useThresholdDiv");
		}
		
		//回调函数
		var callback = function(treeObj){
			if(treeObj == null) {
				return;
			}
			var nodes = treeObj.getCheckedNodes(true);
			if(nodes.length > 0){
				var memCounterId = "";
				var channelId = "";
				
				// 节点ID值
				var nodeId = "";
				// 节点ID去掉第一个字母后的值
				var subNodeId = "";
				var count = 0;
				var regionNameDiv = "";
				
				for(var i = 0; i < nodes.length; i++){
					var curNode = nodes[i];
					var parentNode = curNode.getParentNode();
					// 父节点不存在或者父节点为半选状态，而且当前节点没有子节点或者当前节点为全选状态，那么把该节点保存下来
					if((parentNode == null || parentNode.check_Child_State == 1) 
							&& (curNode.check_Child_State == 2 || curNode.check_Child_State == -1)){
						nodeId = curNode.id;
						subNodeId = nodeId.substring(1, nodeId.length);
						//
						if(nodeId.indexOf("Q") > -1) {// 渠道节点
							if(channelId == "") {
								channelId = subNodeId;
							} else {
								channelId += "," + subNodeId;
							}
						}else{
							if(memCounterId == "") {// 柜台节点
								memCounterId = subNodeId;
							} else {
								memCounterId += "," + subNodeId;
							}
						}
						count++;
						if(count <= 3) {
							if(count == 1) {
								regionNameDiv += curNode.name;
							} else {
								regionNameDiv += "," + curNode.name;
							}
						}
					}
				}
				if(count > 3) {
					regionNameDiv += "...";
				}
				$memCounterId.val(memCounterId);
				$channelId.val(channelId);
				regionNameDiv += '<span class="close" style="margin: 0 10px 2px 5px;" onclick="binolssprm7302.delChannelHtml('+type+');return false"><span class="ui-icon ui-icon-close" style="background-position: -80px -129px;"></span></span>';
				$channelNameDiv.html(regionNameDiv);
			}
			else{
				//无返回节点，删除
			}
		}
		
		// 区域树初始默认选中值
		var value = {};
		var regionInfo = [];
		var memCounterId = $memCounterId.val();
		var channelId = $channelId.val();
		if(channelId) {
			var channelIds = channelId.split(",");
			for(var i = 0; i < channelIds.length; i++) {
				regionInfo.push("Q"+channelIds[i]);
			}
		}
		if(memCounterId) {
			var memCounterIds = memCounterId.split(",");
			for(var i = 0; i < memCounterIds.length; i++) {
				regionInfo.push("D"+memCounterIds[i]);
			}
		}
		value.regionInfo = regionInfo;
		popChannelDialog(url, value, callback);
	},
	"delChannelHtml":function(type){
		var $channelId;
		var $memCounterId;
		var $channelNameDiv;
		if('0' == type){//0为发送门槛渠道选择
			$channelId = $("#sendChannelId");
			$memCounterId = $("#sendMemCounterId");
			$channelNameDiv = $("#sendThresholdDiv");
		}
		else{//使用门槛渠道选择
			$channelId = $("#useChannelId");
			$memCounterId = $("#useMemCounterId");
			$channelNameDiv = $("#useThresholdDiv");
		}
		$channelId.val("");
		$memCounterId.val("");
		$channelNameDiv.html("");
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
		var proContentStart = $content.find('[id^="proContent"]').length + 1;
		var conProShowDivStart = $content.find('[id^="conProShowDiv"]').length + 1;
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
				$(tempId).append($('#coupon-content' + code).html());
				if (code == '5') {
					$(tempId).find('#proContent').attr('id', 'proContent' + proContentStart);
					proContentStart++;
				} else if (code == '2') {
					$(tempId).find('#conProShowDiv').attr('id', 'conProShowDiv' + conProShowDivStart);
					conProShowDivStart++;
				}
			}
		});
		$(tempId).find("span.icon_del").show();
		$content.append($(tempId).html());
	},
	"delCoupon":function(obj) {
		$(obj).parent().remove();
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
$(function(){
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
	$('#useStartTime').cherryDate({
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
	});
	// 表单验证初期化
	cherryValidate({
		formId: 'mainForm',
		rules: {
			"couponRule.ruleName": {required: true},
			sendStartTime: {required: true, dateValid: true},
			sendEndTime: {required: true, dateValid: true},
			useStartTime: {dateValid: true},
			useEndTime: {dateValid: true},
			afterDays: {number:true, min:0},
			validity: {number:true, min:1},
			"couponRule.sumQuantity": {number:true, min:1},
			"couponRule.limitQuantity": {number:true, min:1},
			"couponRule.quantity": {required: true,number:true, min:1},
			faceValue: {required: true, floatValid:[6,2]}
		}
	});
});
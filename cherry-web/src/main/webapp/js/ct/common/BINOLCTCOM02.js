var BINOLCTCOM02_GLOBAL = function () {

};

BINOLCTCOM02_GLOBAL.prototype = {
	"getError" : function(obj,errorMsg){
		var $parent = $(obj).parent();
		$parent.removeClass('error');
		$parent.find("#errorText").remove();
		$parent.addClass("error");
		$parent.append('<span class="ui-icon icon-error tooltip-trigger-error" id="errorText"></span>');
		$parent.find('#errorText').attr("title",'error|'+errorMsg);
		$parent.find('#errorText').cluetip({
	    	splitTitle: '|',
		    width: 150,
		    cluezIndex: 20000,
		    tracking: true,
		    cluetipClass: 'error', 
		    arrows: true, 
		    dropShadow: false,
		    hoverIntent: false,
		    sticky: false,
		    mouseOutClose: true,
		    closePosition: 'title',
		    closeText: '<span class="ui-icon ui-icon-close"></span>'
		});
	},
	// 发送测试信息
	"setReceivedCodeInit" :function(obj){
		var msgType = $(obj).parents("#msgInfoBox").find("#messageType").val();
		var msgContents = $(obj).parents("#msgInfoBox").find("#contents").val();
		var smsChannel=$(obj).parents("#msgInfoBox").find("#smsChannel").val();
		if(msgContents!=""){
			var dialogSetting = {
				dialogInit: "#resDialogInit",
				width: 500,
				height: 220,
				title: $("#resDialogTitle").text()
			};
			openDialog(dialogSetting);
			var url = $(obj).attr("href");
			var param = "messageType=" + msgType + "&smsChannel=" +smsChannel + "&contents=" + encodeURI(msgContents);
			var callback = function(msg) {
				$("#resDialogInit").html(msg);
			};
			cherryAjaxRequest({
				url: url,
				param: param,
				callback: callback
			});
		}else{
			// 当没有沟通内容时
			var $parent = $(obj).parents("#msgInfoBox").find("#contents").parent();
			$parent.removeClass('error');
			$parent.find("#errorText").remove();
			$parent.addClass("error");
			$parent.append('<span class="ui-icon icon-error tooltip-trigger-error" id="errorText"></span>');
			$parent.find('#errorText').attr("title",'error|发送测试信息前请先编辑沟通内容！');
			$parent.find('#errorText').cluetip({
		    	splitTitle: '|',
			    width: 150,
			    cluezIndex: 20000,
			    tracking: true,
			    cluetipClass: 'error', 
			    arrows: true, 
			    dropShadow: false,
			    hoverIntent: false,
			    sticky: false,
			    mouseOutClose: true,
			    closePosition: 'title',
			    closeText: '<span class="ui-icon ui-icon-close"></span>'
			});
		}
	},
		
	//沟通时间为循环沟通时，更改频率选择框内容的处理
	"frequencyChange" :function(obj){
		var $box = $(obj).parents(".COMMINFO");
		if($box.find("#frequencyCode").val() == "1"){
			$box.find("#dayview").hide();
			$box.find("#forAttribute").hide();
		}else if($box.find("#frequencyCode").val() == "2"){
			$box.find("#dayValue").val("1");
			$box.find("#dayview").show();
			$box.find("#forAttribute").show();
			$box.find("#dayview").prop("class","clearfix");
		}else{
			$box.find("#dayview").show();
			$box.find("#forAttribute").show();
			$box.find("#dayview").prop("class","clearfix");
			if($box.find("#forAttribute").val() == "A" || $box.find("#forAttribute").val() == "E"){
				$box.find("#dayValue").val("01-01");
			}else{
				$box.find("#dayValue").val("1");
			}
		}
	},
	
	"forAttributeChange" :function(obj){
		var $box = $(obj).parents(".COMMINFO");
		if($box.find("#forAttribute").val() == "A" || $box.find("#forAttribute").val() == "E"){
			if($box.find("#frequencyCode").val() == "3"){
				$box.find("#dayValue").val("01-01");
			}else{
				$box.find("#dayValue").val("1");
			}			
		}else{
			$box.find("#dayValue").val("1");
		}
	},
	
	"conditionChange" :function(obj){
		var $box = $(obj).parents(".COMMINFO");
		if($box.find("#conditionCode").val() == "1"){
			$box.find("#filllabel").hide();
			$box.find("#paramview").hide();
		}else if($box.find("#conditionCode").val() == "4"){
			$box.find("#filllabel").hide();
			$box.find("#paramview").hide();
		}else{
			$box.find("#filllabel").show();
			$box.find("#paramview").show();
		}
	},
	
	//沟通为参考条件执行时，更改参考条件时所做的处理
	"referTypeChange" :function(obj){
		var $box = $(obj).parents(".COMMINFO");
		if($box.find("#referTypeCode").val() == "5" || $box.find("#referTypeCode").val() == "6" || $box.find("#referTypeCode").val() == "7" || 
				$box.find("#referTypeCode").val() == "9" || $box.find("#referTypeCode").val() == "10" || $box.find("#referTypeCode").val() == "12" ){
			$box.find("#dateBegin").hide();
			$box.find("#referbef").hide();
			$box.find("#referafter").show();
			$box.find("#beginDateDiv").show();
			$box.find("#endDateDiv").show();
			$box.find("#referAttribute").val("2"); 
			$box.find("#runType").removeAttr("checked");
		}else if($box.find("#referTypeCode").val() == "11" || $box.find("#referTypeCode").val() == "14"){
			$box.find("#dateBegin").show();
			$box.find("#referbef").show();
			$box.find("#referafter").hide();
			$box.find("#beginDateDiv").show();
			$box.find("#endDateDiv").show();
			$box.find("#referAttribute").val("1"); 
			$box.find("#runType").attr("checked","true");
		}else if( $box.find("#referTypeCode").val() == "13"){
			$box.find("#dateBegin").show();
			$box.find("#referbef").show();
			$box.find("#referafter").show();
			$box.find("#beginDateDiv").show();
			$box.find("#endDateDiv").show();
			$box.find("#referAttribute").val("1"); 
			$box.find("#runType").attr("checked","true");
		}else{
			$box.find("#dateBegin").hide();
			$box.find("#referbef").show();
			$box.find("#referafter").show();
			if($box.find("#referTypeCode").val() == "1" || $box.find("#referTypeCode").val() == "2" || 
					$box.find("#referTypeCode").val() == "3" || $box.find("#referTypeCode").val() == "4" ){
				$box.find("#beginDateDiv").hide();
				$box.find("#endDateDiv").hide();
				$box.find("#runType").removeAttr("checked");
			}else{
				$box.find("#beginDateDiv").show();
				$box.find("#endDateDiv").show();
				if($box.find("#referTypeCode").val() == "8"){
					$box.find("#runType").attr("checked","true");
				}else{
					$box.find("#runType").removeAttr("checked");
				}
			}
		}
	},
	
	//增加沟通
	"addNewComm" :function(_this){
		//新增菜单前获取最后一个菜单的标识值加1作为新增菜单的标识值
		var $prevCommBox = $("#commNameList").find("li").last().find("#commBoxId");
		var commBoxValue = parseInt($prevCommBox.val()) + 1;
				
		//获取菜单对象和沟通页面对象
		var $commNameLi = $("#commNameList");
		var $commList = $("#commList");
		var $box = $(_this).parents(".COMMBOX");
		var $commInfo = $box.find(".COMMINFO");
		
		//在增加沟通时先隐藏原有的所有页面内容
		$commInfo.hide();
		//移除沟通名称菜单上的样式
		$commNameLi.find("li").removeClass("on MENUTEXT");
		//增加新的沟通名称菜单
		$commNameLi.append($("#addNewLi").html());
		//增加新的沟通页面内容
		$commList.append($("#addNewComm").html());
		
		//增加菜单后重新获取最新的菜单并设置标识值
		var $lastCommId = $("#commNameList").find("li").last();
		$lastCommId.find("#commBoxId").attr("value",commBoxValue);
		//给新增加菜单赋一个默认名称
		$lastCommId.find("span").html("第"+commBoxValue+"次沟通");
		
		//获取最新增加的沟通页面
		var $lastBox = $(_this).parents(".COMMBOX").find(".COMMINFO").last();
		//给新页面块增加一个用于标识的id属性
		$lastBox.attr("id","commBox_"+commBoxValue);
		$lastBox.find("#communicationCode").attr("value",commBoxValue);
		//给新增加的沟通赋一个默认名称
		$lastBox.find("#communicationName").attr("value","第"+commBoxValue+"次沟通");
		//给日历控件赋一个唯一标识ID
		$lastBox.find("input[id^='sendDate_']").attr("id","sendDate_"+commBoxValue);
		$lastBox.find("input[id^='runBeginTime_']").attr("id","runBeginTime_"+commBoxValue);
		$lastBox.find("input[id^='runEndTime_']").attr("id","runEndTime_"+commBoxValue);
		$lastBox.find("input[id^='sendBeginTime_']").attr("id","sendBeginTime_"+commBoxValue);
		$lastBox.find("input[id^='sendEndTime_']").attr("id","sendEndTime_"+commBoxValue);
		$lastBox.find("input[id^='runBegin_']").attr("id","runBegin_"+commBoxValue);
		$lastBox.find("input[id^='runEnd_']").attr("id","runEnd_"+commBoxValue);
		$lastBox.find("input[id^='sendTime_']").attr("id","sendTime_"+commBoxValue);
		$lastBox.find("input[id^='timeValue_']").attr("id","timeValue_"+commBoxValue);
		$lastBox.find("input[id^='tValue_']").attr("id","tValue_"+commBoxValue);
		
		//移除初始日历控件样式
		//（由于页面初始化时新增的模板页面还未生成，所以新增页面内容中的日历控件失效，需要移除后重新初始化）
		$lastBox.find("#fixedTime").find("button").remove();
		$lastBox.find("#referTime").find("button").remove();
		$lastBox.find("#loopRun").find("button").remove();
		$lastBox.find("#conditionTrigger").find("button").remove();
		$lastBox.find("#eventTrigger").find("button").remove();
		$("#sendDate_"+commBoxValue).removeProp("class");
		$("#runBeginTime_"+commBoxValue).removeProp("class");
		$("#runEndTime_"+commBoxValue).removeProp("class");
		$("#sendBeginTime_"+commBoxValue).removeProp("class");
		$("#sendEndTime_"+commBoxValue).removeProp("class");
		$("#runBegin_"+commBoxValue).removeProp("class");
		$("#runEnd_"+commBoxValue).removeProp("class");
		$("#sendTime_"+commBoxValue).removeProp("class");
		$("#timeValue_"+commBoxValue).removeProp("class");
		$("#tValue_"+commBoxValue).removeProp("class");
		//日历控件重新初始化
		$("#sendDate_"+commBoxValue).cherryDate({
			beforeShow: function(input){
				var value = $("#nowDate").val();
				return [value, "minDate"];
			}
		});
		$("#runBeginTime_"+commBoxValue).cherryDate({
			beforeShow: function(input){
				var minValue = $("#nowDate").val();
				var maxValue = $(input).parents("tr").find("input[name='runEndTime']").val();
				return [minValue, maxValue];
			}
		});
		$("#runEndTime_"+commBoxValue).cherryDate({
			beforeShow: function(input){
				var value = $(input).parents("tr").find("input[name='runBeginTime']").val();
				if(value==""){
					value = $("#nowDate").val();
				}
				return [value, "minDate"];
			}
		});
		$("#sendBeginTime_"+commBoxValue).cherryDate({
			beforeShow: function(input){
				var minValue = $("#nowDate").val();
				var maxValue = $(input).parents("tr").find("input[name='sendEndTime']").val();
				return [minValue, maxValue];
			}
		});
		$("#sendEndTime_"+commBoxValue).cherryDate({
			beforeShow: function(input){
				var value = $(input).parents("tr").find("input[name='sendBeginTime']").val();
				if(value==""){
					value = $("#nowDate").val();
				}
				return [value, "minDate"];
			}
		});
		$("#runBegin_"+commBoxValue).cherryDate({
			beforeShow: function(input){
				var minValue = $("#nowDate").val();
				var maxValue = $(input).parents("tr").find("input[name='runEnd']").val();
				return [minValue, maxValue];
			}
		});
		$("#runEnd_"+commBoxValue).cherryDate({
			beforeShow: function(input){
				var value = $(input).parents("tr").find("input[name='runBegin']").val();
				if(value==""){
					value = $("#nowDate").val();
				}
				return [value, "minDate"];
			}
		});
		$("#sendTime_"+commBoxValue).timepicker({
			timeOnlyTitle: $("#timeOnlyTitle").val(),
			currentText: $("#currentText").val(),
			closeText: $("#closeText").val(),
			timeText: $("#timeText").val(),
			hourText: $("#hourText").val(),
			minuteText: $("#minuteText").val(),
			hourMax: 22
		});
		$("#timeValue_"+commBoxValue).timepicker({
			timeOnlyTitle: $("#timeOnlyTitle").val(),
			currentText: $("#currentText").val(),
			closeText: $("#closeText").val(),
			timeText: $("#timeText").val(),
			hourText: $("#hourText").val(),
			minuteText: $("#minuteText").val(),
			hourMax: 22
		});
		$("#tValue_"+commBoxValue).timepicker({
			timeOnlyTitle: $("#timeOnlyTitle").val(),
			currentText: $("#currentText").val(),
			closeText: $("#closeText").val(),
			timeText: $("#timeText").val(),
			hourText: $("#hourText").val(),
			minuteText: $("#minuteText").val(),
			hourMax: 22
		});
		//重新给日历控件增加样式
		$("#sendDate_"+commBoxValue).prop("class","text");
		$("#runBeginTime_"+commBoxValue).prop("class","text");
		$("#runEndTime_"+commBoxValue).prop("class","text");
		$("#sendBeginTime_"+commBoxValue).prop("class","text");
		$("#sendEndTime_"+commBoxValue).prop("class","text");
		$("#runBegin_"+commBoxValue).prop("class","text");
		$("#runEnd_"+commBoxValue).prop("class","text");
		$("#sendTime_"+commBoxValue).prop("class","text");
		$("#timeValue_"+commBoxValue).prop("class","text");
		$("#tValue_"+commBoxValue).prop("class","text");
		
		// 增加沟通时判断沟通次数，如果沟通次数达到2次以上则显示删除按钮
		if($("#commNameList").find("li").size()<=1){
			$("#commDeleteButton").hide();
		}else{
			$("#commDeleteButton").show();
		}
	},
	
	//增加沟通信息页面内容
	"addNewMessage" :function(_this){
		var $box = $(_this).parents(".COMMOBJECT");
		var $tbody = $box.find("#messageListTable tbody");
		$tbody.append($("#addNewMessageinfo tbody").html());
		
		// 判断每批沟通对象对应的沟通内容条数，达到2条以上则显示删除按钮
		if($tbody.find("tr").size()<=1){
			$tbody.find("#msgDeleteButton").hide();
		}else{
			$tbody.find("#msgDeleteButton").show();
		}
	},
	
	//增加沟通对象页面内容
	"addNewCommObject" :function(_this){
		var $box = $(_this).parents(".COMMINFO");
		var $div = $box.find("#commObjectList");
		$div.append($("#addNewCommObjInfo").html());
		
		// 判断每次沟通对应的沟通对象批次，达到2批以上则显示删除按钮
		if($box.find(".COMMOBJECT").size()<=1){
			$box.find("#objDeleteButton").hide();
		}else{
			$box.find("#objDeleteButton").show();
		}
	},
	
	//移除新增加的页面内容
	"viewRemove" :function(obj){
		var $msgList = $(obj).parents(".COMMOBJECT").find("#messageListTable tbody");
		var $objList = $(obj).parents(".COMMINFO");
		
		// 移除页面
		var trNode = obj.parentNode.parentNode;
		$(trNode).remove();
		
		// 删除新增加的沟通信息内容时如果少于2条则隐藏删除按钮，确保每批沟通对象至少存在一条沟通信息
		if($msgList.find("tr").size()<=1){
			$msgList.find("#msgDeleteButton").hide();
		}else{
			$msgList.find("#msgDeleteButton").show();
		}
		
		// 删除新增加的沟通对象时如果少于2批对象则隐藏删除按钮，确保每次沟通至少有一批沟通对象
		if($objList.find(".COMMOBJECT").size()<=1){
			$objList.find("#objDeleteButton").hide();
		}else{
			$objList.find("#objDeleteButton").show();
		}
		
	},
	
	// 沟通名称文本框值更改事件
	"textChange" :function(obj){
		var text=$(obj).val();
		var $box = $(obj).parents(".COMMBOX");
		var $menu = $box.find(".on.MENUTEXT").find("span");
		if($.trim(text) == "" || text == null){
			$(obj).val("[NULL]");
			text = "[NULL]";
		}
		$menu.html(text);
	},
	
	// 沟通时间选择框值更改事件
	"selectChange" :function(obj){
		var $box = $(obj).parents(".COMMINFO");
		//根据选择的时间类型隐藏和显示页面内容
		if($box.find("#timeType").val() == "1"){
			$box.find("#fixedTime").show();
			$box.find("#referTime").hide();
			$box.find("#loopRun").hide();
			$box.find("#conditionTrigger").hide();
			$box.find("#eventTrigger").hide();
			$box.find("#runType").removeAttr("checked");
			$box.find('[name="birthFlag"]').parent().parent().hide();
		}else if($box.find("#timeType").val() == "2"){
			$box.find("#fixedTime").hide();
			$box.find("#referTime").show();
			$box.find("#loopRun").hide();
			$box.find("#conditionTrigger").hide();
			$box.find("#eventTrigger").hide();
			if($box.find("#referTypeCode").val() == "5" || $box.find("#referTypeCode").val() == "6" || $box.find("#referTypeCode").val() == "7" || 
					$box.find("#referTypeCode").val() == "9" || $box.find("#referTypeCode").val() == "10" || $box.find("#referTypeCode").val() == "12" ||
					$box.find("#referTypeCode").val() == "14"){
				$box.find("#referbef").hide();
				$box.find("#beginDateDiv").show();
				$box.find("#endDateDiv").show();
				$box.find("#referAttribute").val("2"); 
				$box.find("#runType").removeAttr("checked");
			}else if($box.find("#referTypeCode").val() == "13"){
				$box.find("#referafter").show();
				$box.find("#beginDateDiv").show();
				$box.find("#endDateDiv").show();
				$box.find("#referAttribute").val("1"); 
				$box.find("#runType").removeAttr("checked");
			}else{
				$box.find("#referbef").show();
				if($box.find("#referTypeCode").val() == "1" || $box.find("#referTypeCode").val() == "2" || 
						$box.find("#referTypeCode").val() == "3" || $box.find("#referTypeCode").val() == "4" ){
					$box.find("#beginDateDiv").hide();
					$box.find("#endDateDiv").hide();
					$box.find("#runType").removeAttr("checked");
				}else{
					$box.find("#beginDateDiv").show();
					$box.find("#endDateDiv").show();
					if($box.find("#referTypeCode").val() == "8" || $box.find("#referTypeCode").val() == "11"){
						$box.find("#runType").attr("checked","true");
					}else{
						$box.find("#runType").removeAttr("checked");
					}
				}
			}
			$box.find('[name="birthFlag"]').parent().parent().hide();
		}else if($box.find("#timeType").val() == "3"){
			$box.find("#fixedTime").hide();
			$box.find("#referTime").hide();
			$box.find("#loopRun").show();
			$box.find("#conditionTrigger").hide();
			$box.find("#eventTrigger").hide();
			$box.find("#runType").attr("checked","true");
			$box.find('[name="birthFlag"]').parent().parent().show();
		}else if($box.find("#timeType").val() == "4"){
			$box.find("#fixedTime").hide();
			$box.find("#referTime").hide();
			$box.find("#loopRun").hide();
			$box.find("#conditionTrigger").show();
			$box.find("#eventTrigger").hide();
			$box.find("#runType").removeAttr("checked");
			$box.find('[name="birthFlag"]').parent().parent().hide();
		}else if($box.find("#timeType").val() == "5"){
			$box.find("#fixedTime").hide();
			$box.find("#referTime").hide();
			$box.find("#loopRun").hide();
			$box.find("#conditionTrigger").hide();
			$box.find("#eventTrigger").show();
			$box.find("#runType").removeAttr("checked");
			$box.find('[name="birthFlag"]').parent().parent().hide();
		}
	},
	
	// 沟通名称菜单选择事件
	"selectCommbox" :function(obj){
		// 获取所选沟通名称菜单对象和对应的标识值
		var commId = $(obj).find("#commBoxId").val();
		var $commNameLi = $("#commNameList");
		
		if(commId != null) {
			// 移除全部沟通名称菜单的样式
			$commNameLi.find("li").removeClass("on MENUTEXT");
			// 给所选沟通名称菜单添加样式
			$(obj).prop("class","on MENUTEXT");
			
			// 隐藏所有被选菜单对应页面之外的其它页面，显示所选菜单对应的沟通页面
			$("#commBox_"+commId).siblings().hide();
			$("#commBox_"+commId).show();
		}
		var timeType=$('#commBox_'+commId+' #timeType [selected]').val();
		if(timeType == 1){
			BINOLCTCOM02.checkSendType(commId);
		}
	},
	
	// 删除沟通
	"deleteComm" :function(obj){
		// 找到被选择的菜单
		var $box = $(obj).parents(".COMMBOX");
		var $menu = $box.find(".on.MENUTEXT");
		var $next = $menu.next();
		var $prev = $menu.prev();
		// 获取待删除菜单的标识号和下一个菜单的标识号
		var commId = $menu.find("#commBoxId").val();
		var nextId = $next.find("#commBoxId").val();
		
		// 删除菜单以及菜单对应的沟通页面
		$("#commBox_"+commId).remove();
		$menu.remove();
		
		// 删除菜单后将其上一个菜单或者下一个菜单显示为被选中状态
		// 如果删除的沟通是最后一条，则删除后显示前一个沟通页面，否则显示后一个沟通页面
		if(nextId == null){
			// 获取最新被选菜单的标识号
			var newId = $prev.find("#commBoxId").val();
			// 改变最新被选菜单的样式
			$prev.prop("class","on MENUTEXT");
			// 显示选择菜单对应的页面
			$("#commBox_"+newId).siblings().hide();
			$("#commBox_"+newId).show();
		}else{
			// 获取最新被选菜单的标识号
			var newId = $next.find("#commBoxId").val();
			// 改变最新被选菜单的样式
			$next.prop("class","on MENUTEXT");
			// 显示选择菜单对应的页面
			$("#commBox_"+newId).siblings().hide();
			$("#commBox_"+newId).show();
		}
		
		// 删除沟通时判断是否删除到了最后一条，如果是则隐藏删除按钮
		// 说明：每个沟通计划至少要求设置一次沟通，所以当删除到最后一条沟通时隐藏删除按钮
		if($("#commNameList").find("li").size()<=1){
			$("#commDeleteButton").hide();
		}else{
			$("#commDeleteButton").show();
		}
	},
	
	// 沟通对象编辑与选择页面初始化
	"setCommObjInit" :function(obj){
		// 移除页面上可能存在的所有被选中样式
		$(".OBJINFOON").removeClass("OBJINFOON");
		// 弹出页面前给所选的页面块增加样式用于区分被选中的页面范围
		$(obj).parents("#commObjinfo").prop("class","OBJINFOON");
		var dialogSetting = {
			dialogInit: "#dialogInit",
			width: 1000,
			height: 660,
			title: $("#objDialogTitle").text(),
			confirm: $("#dialogConfirm").text(),
			cancel: $("#dialogCancel").text(),
			confirmEvent: function(){BINOLCTCOM02.searchObjRequest($(obj));},
			cancelEvent: function(){removeDialog("#dialogInit");oTableArr[0]=null;oTableArr[1]=null;},
			closeEvent:function(){removeDialog("#dialogInit");oTableArr[0]=null;oTableArr[1]=null;}
		};
		openDialog(dialogSetting);
		// 添加选项卡
		$("#dialogInit").html($("#searchRecordTabs").html());
		$("#dialogInit").find('.ui-tabs').tabs();
		$("#dialogInit").find("#tabs-1").html($("#table_sProcessing").text());
		
		var userId = $("#userId").val();
		var privilegeFlag = $("#privilegeFlag").val();
		if(privilegeFlag == null || privilegeFlag == ""){
			privilegeFlag = "0";
		}
		// 获取全部条件信息
		var conditionInfo = $(obj).parents("#commObjinfo").find("#conditionInfo").val();
		// 获取需要固定的条件信息
		var disableCondition = $(obj).parents("#commObjinfo").find("#disableCondition").val();
		var setObjUrl = $(obj).attr("href");
		var param = "reqContent=" + conditionInfo + "&disableCondition=" + disableCondition + "&userId=" + userId + "&privilegeFlag=" + privilegeFlag;
		var searchName = $(obj).parents("#commObjinfo").find("#recordName").val();
		var searchCode = $(obj).parents("#commObjinfo").find("#recordCode").val();
		var recordType = $(obj).parents("#commObjinfo").find("#recordType").val();
		var callback = function(msg) {
			//初始化搜索条件选项卡
			$("#dialogInit").find("a@[href='#tabs-1']").click(function() {
				$("#dialogInit").find("#tabs-1").html(msg);
				// 向沟通对象选择页面增加记录名称输入框
				$("#dialogInit").find("#tabs-1").prepend($("#addObjName").html());
				if(recordType == '1'){
					// 如果已经选择了沟通对象并且为搜索条件时再次进入时将所选沟通对象记录的名称赋给新增加的输入框
					$("#dialogInit").find("#tabs-1").find("#searchName").val(searchName);
				}
				$("#dialogInit").dialog( "option", {
    				buttons: [
						{
							text: $("#dialogConfirm").text(),
						    click: function(){BINOLCTCOM02.searchObjRequest($(obj)); }
						},	
						 {
							text: $("#dialogCancel").text(),
							click: function(){removeDialog("#dialogInit");oTableArr[0]=null;oTableArr[1]=null; }
						}],
    			});
		    });
			// 初始化搜索结果选项卡
			$("#dialogInit").find("a@[href='#tabs-2']").click(function() {
				$("#dialogInit").find("#tabs-2").find("#searchReCordDataTableTem").attr("id","searchReCordDataTable");
				$("#dialogInit").find("#tabs-2").find("#searchRecordFormTem").attr("id","searchRecordForm");
				//初始化时若为搜索结果或是Excel导入的将SearchCode带入到搜索编号输入框
				if(recordType == '2'){
					$("#dialogInit #tabs-2").find("input[name='searchCode']").val(searchCode);
				}
				BINOLCTCOM02.searchSearchReCord();
				$("#dialogInit").dialog( "option", {
    				buttons: [{
    					text: $("#dialogCancel").text(),
    				    click: function(){removeDialog("#dialogInit");oTableArr[0]=null;oTableArr[1]=null; }
    				}],
    			});
		    });
			// 初始化Excel导入选项卡
			$("#dialogInit").find("a@[href='#tabs-3']").click(function() {
				//等待数据加载画面
				$("#dialogInit").find("#tabs-3").html($("#table_sProcessing").text());
				var url = "/Cherry/common/BINOLCM02_initMemImportDialog";
				cherryAjaxRequest({
			        url: url, 
			        param: null,
			        callback: function(msg){
			        	$("#dialogInit #tabs-3").html(msg);
			        	$("#dialogInit #tabs-3 #memImportDialog").show();
			        	//在信息提示下添加查看导入明细按钮
			        	if($("#showMessageInfo").length > 0){
			        		$("#showMessageInfo").after($("#importDetailViewBtn").html());
			        	}else{
			        		$("#actionMessage_import").after($("#importDetailViewBtn").html());
			        	}
					}			
				});
				$("#dialogInit").dialog( "option", {
    				buttons: [
						{
							text: $("#dialogConfirm").text(),
						    click: function(){BINOLCTCOM02.chooseCommObj(2,null);oTableArr[0]=null;oTableArr[1]=null; }
						},	
						 {
							text: $("#dialogCancel").text(),
							click: function(){removeDialog("#dialogInit");oTableArr[0]=null;oTableArr[1]=null; }
						}],
    			});
			});
			// 如果已经选择了沟通对象并且为搜索条件时再次进入时将所选沟通对象记录的名称赋给新增加的输入框
			if(recordType == '1'){
				$("#dialogInit").find("a@[href='#tabs-1']").click();
			}else if(recordType == '2'){
				$("#dialogInit").find("a@[href='#tabs-2']").click();
			}else{
				$("#dialogInit").find("a@[href='#tabs-1']").click();
			}
		};
		cherryAjaxRequest({
			url: setObjUrl,
			param: param,
			callback: callback
		});
	},
	
	//沟通对象搜索结果记录查询
	"searchSearchReCord" : function() {
		var $form = $('#searchRecordForm');
		//表格列属性
		var aoColumnsArr = [
		                    {"sName" : "RowNumber", "sWidth": "3%", "bSortable": false},
		                    {"sName" : "searchCode", "sWidth" : "15%"},
		                    {"sName" : "recordName", "sWidth" : "15%"},
		                    {"sName" : "customerType", "sWidth" : "15%"},
		                    {"sName" : "recordCount","sWidth" : "15%"},
		                    {"sName" : "act", "sWidth" : "10%", "bSortable" : false}
		                    ];
		var url = $("#searchSearchReCordUrl").attr("href");
		var params = getSerializeToken()+"&"+$form.serialize();
		url = url + "?" + params;
		// 表格设置
		var tableSetting = {
			// 表格ID
			tableId : '#searchReCordDataTable',
			// 数据URL
			url : url,
			// 排序列
			aaSorting : [ [ 1, "desc" ] ],
			// 表格列属性设置
			aoColumns : aoColumnsArr,
			// 横向滚动条出现的临界宽度
			sScrollX : "100%",
			// 默认显示行数
			iDisplayLength : 10,
			index : 1
		};
		// 调用获取表格函数
		getTable(tableSetting);
	},
	//沟通对象明细弹出框
	"showCustomerInit" :function(thisObj,type,index){
		var recordCode = "";
		var recordType = "";
		var customerType = "";
		if(type == 1){
			//搜索结果
			recordCode = $("#tbSearchCode_"+index).val();
			recordType = $("#tbRecordType_"+index).val();
			customerType = $("#tbCustomerType_"+index).val();
		}else if(type == 2){
			//Excel导入
			var $obj = $("#memImportDialog");
			recordCode = $.trim($obj.find("[name='searchCode']").val());
			customerType = $.trim($obj.find("[name='customerType']").val());
			recordType = 2;
		}
		var dialogSetting = {
			dialogInit: "#customerDialogInit",
			width: 800,
			height: 450,
			title: $("#customerDialogTitle").text()
		};
		openDialog(dialogSetting);
		var url = $(thisObj).attr("href");
		var param = "recordCode=" + recordCode;
			param = param + "&recordType=" + recordType;
			param = param + "&customerType=" + customerType;
		var callback = function(msg) {
			$("#customerDialogInit").html(msg);
		};
		cherryAjaxRequest({
			url: url,
			param: param,
			callback: callback
		});
	},
	//选择沟通对象
	"chooseCommObj" : function(type,index){
		var searchCode = "";
		var recordName = "";
		var customerType = "";
		var recordType = "";
		var fromType = "";
		var customerCount = "";
		var conditionDisplay = "";
		if(type == 1){
			//搜索结果
			searchCode = $("#tbSearchCode_"+index).val();
			recordName = $("#tbRecordName_"+index).val();
			customerType = $("#tbCustomerType_"+index).val();
			recordType = $("#tbRecordType_"+index).val();
			fromType = $("#tbFromType_"+index).val();
			customerCount = $("#tbRecordCount_"+index).val();
			conditionDisplay = $("#searchResult").html();
		}else if(type == 2){
			//Excel导入
			$obj = $("#memImportDialog");
			searchCode = $.trim($obj.find("[name='searchCode']").val());
			recordName = $.trim($obj.find("[name='recordName']").val());
			customerType = $.trim($obj.find("[name='customerType']").val());
			recordType = 2;
			fromType = 3;
			customerCount = $.trim($obj.find("[name='customerCount']").val());
			conditionDisplay = $("#excelImport").html();
			if(recordName == ""){
				this.getError($obj.find("[name='recordName']"), $("#importNameNull").text());
				return false;
			}
			if(searchCode == ""){
				this.getError($obj.find("[name='recordName']"),  $("#importCodeNull").text());
				return false;
			}
		}
		$(".OBJINFOON").find("#recordCode").val(searchCode);
		$(".OBJINFOON").find("#recordName").val(recordName);
		$(".OBJINFOON").find("#customerType").val(customerType);
		$(".OBJINFOON").find("#recordType").val(recordType);
		$(".OBJINFOON").find("#fromType").val(fromType);
		$(".OBJINFOON").find("#recordCount").val(customerCount);
		//关闭沟通对象选择框
		removeDialog("#dialogInit");
		oTableArr[1]=null;
		$(".OBJINFOON").find("#recordName").attr("title","条件|"+conditionDisplay);
		// 重新初始化提示框
		$(".OBJINFOON").find("#recordName").cluetip({width: '500',splitTitle:'|',height: 'auto',cluetipClass: 'default',cursor: 'pointer',showTitle: false});
		$(".OBJINFOON").find("#conditionDisplay").val(conditionDisplay);
	},
	
	// 沟通内容编辑与选择页面初始化
	"setMessageInit" :function(obj){
		// 移除页面上可能存在的所有被选中样式
		$(".MSGINFOON").removeClass("MSGINFOON");
		// 弹出页面前给所选的页面块增加样式用于区分被选中的页面范围
		$(obj).parents("#msgInfoBox").prop("class","MSGINFOON");
		var dialogSetting = {
			dialogInit: "#dialogInit",
			width: 1000,
			height: 650,
			title: $("#msgDialogTitle").text()
		};
		openDialog(dialogSetting);
		
		var messageType = $(obj).parents("#msgInfoBox").find("#messageType").val();
		var msgContents = $(obj).parents("#msgInfoBox").find("#contents").val();
		var setMessageUrl = $(obj).attr("href");
		var param = "messageType=" + messageType + "&msgContents=" + encodeURI(msgContents);
		var callback = function(msg) {
			$("#dialogInit").html(msg);
		};
		cherryAjaxRequest({
			url: setMessageUrl,
			param: param,
			callback: callback
		});
	},
	

	"searchObjRequest" : function(obj){
		var userId = $("#userId").val();
		var privilegeFlag = $("#privilegeFlag").val();
		var searchValue = $("#dialogInit").find(":input").serializeForm2Json();
		var searchName = $("#dialogInit").find("#searchName").val();
		var commBox=$("#commNameList .MENUTEXT.on #commBoxId").val();
		var timeType=$('#'+'commBox_'+commBox).find('select[name="timeType"]').val();
		var birthFlag=$('#'+'commBox_'+commBox).find('input[name="birthFlag"]').is(':checked');
		if(privilegeFlag == null || privilegeFlag == ""){
			privilegeFlag = "0";
		}
		//判断礼品领用的情况
		if("3" ==  timeType && birthFlag == true){
			var $birthDayMode=$('#memSearchRequestDiv div:first select[name="birthDayMode"]');
			var birthDayMode=$birthDayMode.val();
			if(birthDayMode < 0){
				BINOLCTCOM02.addErrorMessage($birthDayMode.parent(), "您勾选了生日礼沟通，必须选择生日");
				return false;
			}
		}
		// 判断用户是否输入了搜索记录名称
		if(searchName != undefined && null != searchName && searchName != ""){
			// 获取系统当前时间作为搜索记录编号
			var d = new Date();
			var vYear = d.getFullYear();
			var vMon = d.getMonth() + 1;
			var vDay = d.getDate();
			var h = d.getHours();
			var m = d.getMinutes();
			var se = d.getSeconds();
			var ms = d.getMilliseconds();
			var recordCode = "EC"+vYear+(vMon<10 ? "0" + vMon : vMon)+(vDay<10 ? "0"+ vDay : vDay)+(h<10 ? "0" + h : h)
						+(m<10 ? "0" + m : m)+(se<10 ? "0" + se : se)+(ms<100 ? "0" + ms : ms);
			
			// 给选择的沟通对象输入框赋值
			$(".OBJINFOON").find("#recordCode").val(recordCode);
			$(".OBJINFOON").find("#recordName").val(searchName);
			$(".OBJINFOON").find("#customerType").val("1");
			$(".OBJINFOON").find("#recordType").val("1");
			$(".OBJINFOON").find("#fromType").val("1");
			// 获取符合条件的客户数量
			var getCustomerCountUrl = $("#getCustomerCountUrl").attr("href");
			var params = "searchCode=" + recordCode + "&userId=" + userId + "&privilegeFlag=" + privilegeFlag + "&recordType=1&customerType=1&conditionInfo=" + searchValue;
			cherryAjaxRequest({
				url: getCustomerCountUrl,
				param: params,
				callback: function(data) {
					if(data != undefined && data != null && data != ""){
						// 保存搜索到的符合条件的会员数量
						$(".OBJINFOON").find("#recordCount").val(data);
						// 判断搜索条件值是否存在，若存在则保存条件相关信息，否则直接关闭窗口
						if(searchValue != undefined && null != searchValue && searchValue != ""){
							// 将条件信息保存到页面输入框中用于传递到下一个页面
							$(".OBJINFOON").find("#conditionInfo").val(searchValue);
							// 获取返回条件说明的Action地址
							var getInfoUrl = $("#conditionDisplayUrl").text();
							var param = "reqContent=" + searchValue;
							var callback = function(msg) {
								// 将条件说明保存到页面隐藏输入框中用于传递到下一个页面
								$(".OBJINFOON").find("#conditionDisplay").val(msg);
								// 将条件说明赋给提示框
								$(".OBJINFOON").find("#recordName").attr("title","条件|" + msg);
								// 重新初始化提示框
								$(".OBJINFOON").find("#recordName").cluetip({width: '500',splitTitle:'|',height: 'auto',cluetipClass: 'default',cursor: 'pointer',showTitle: false});
								
								// 关闭弹出窗口
								removeDialog("#dialogInit");
								// 移除临时样式，将弹出框内的表对象清空
								$(".OBJINFOON").removeClass("OBJINFOON");
								// 清空表对象
								oTableArr[0]=null;
								oTableArr[1]=null;
							};
							cherryAjaxRequest({
								url: getInfoUrl,
								param: param,
								callback: callback
							});
						}else{
							// 关闭弹出窗口
							removeDialog("#dialogInit");
							// 移除临时样式，将弹出框内的表对象清空
							$(".OBJINFOON").removeClass("OBJINFOON");
							// 清空表对象
							oTableArr[0]=null;
							oTableArr[1]=null;
						}
					}
				}
			});
		}else{
			// 当用户没有输入搜索记录名称时给出错误提示信息
			var $parent = $("#dialogInit").find("#searchName").parent();
			$parent.removeClass('error');
			$parent.find("#errorText").remove();
			$parent.addClass("error");
			$parent.append('<span class="ui-icon icon-error tooltip-trigger-error" id="errorText"></span>');
			$parent.find('#errorText').attr("title",'error|请输入名称');
			$parent.find('#errorText').cluetip({
		    	splitTitle: '|',
			    width: 150,
			    cluezIndex: 20000,
			    tracking: true,
			    cluetipClass: 'error', 
			    arrows: true, 
			    dropShadow: false,
			    hoverIntent: false,
			    sticky: false,
			    mouseOutClose: true,
			    closePosition: 'title',
			    closeText: '<span class="ui-icon ui-icon-close"></span>'
			});
		}
		
	},
	
	// 上一步、下一步
	"toNext" :function(url){
		// 获取页面的Json格式结果
		this.getCommResult();
		
		// 参数序列化
		var params= $("#infoSetForm").serialize();
		cherryAjaxRequest({
			url: url,
			param: params,
			callback: function(msg) {
				if(msg.indexOf('id="fieldErrorDiv"') == -1) {
					$("#div_main").html(msg);
				}
			}
		});
	},
	
	// 将沟通设置页面的设置结果转换成Json格式
	"getCommResult" :function(){
		var commresult = "";
		// 获取沟通集合
		var $infoBoxs = $("#commList").find("div[id^='commBox_']");
		// 遍历每次沟通
		$infoBoxs.each(function(i){
			var infojson = [];
			var objresult = "";
			var tempjson = "";
			// 将沟通明细信息转换成Json格式
			infojson = Obj2JSON($(this).find("#commDetailinfo"));
			if(infojson.length > 0){
				tempjson = infojson + ',';
			}
			// 获取每次沟通的沟通对象集合			
			var $objBoxs = $(this).find(".COMMOBJECT");
			// 遍历每次沟通的沟通对象
			$objBoxs.each(function(j){
				var objjson = [];
				var tjson = "";
				// 将每次沟通的沟通对象转换成Json格式
				objjson = Obj2JSON($(this).find("#commObjinfo"));
				if(objjson.length > 0){
					tjson = objjson + ',';
				}
				// 获取沟通对象的信息发送集合
				var $msgBoxs = $(this).find("#messageListTable");
				// 将每个沟通对象的信息集合转换成Json格式
				tjson += '"msgList":' + Obj2JSONArr($msgBoxs.find("tr").not(":first"));
				
				if(j > 0){
					objresult += ",";
				}
				objresult += "{" + tjson + "}";
			});
			// 增加沟通对象集合边界
			objresult = "[" + objresult + "]";
			// 将沟通对象集合拼接给每次沟通
			tempjson += '"objList":' + objresult;
			
			if(i > 0){
				commresult += ",";
			}
			commresult += "{" + tempjson + "}";
		});
		// 增加沟通集合边界
		commresult = "[" + commresult + "]";
		$("#commResultInfo").val(commresult);
	},
	//限制页面进行修改操作，放开了发送测试短信
	"restrictionModification":function(id){
						var commBox=$('#commBox_'+id+'' );
							commBox.find('#communicationName').attr("readonly","true").attr("class","text disabled");
							commBox.find('#sendDate_1').attr("readonly","true").attr("class","text disabled");
							commBox.find('#timeType option').each(function(i){
								if(!$(this).attr("selected")){
									$(this).attr("disabled","true");
								}
							});
							commBox.find('#sendDate_'+id).unbind();
							commBox.find('#sendDate_'+id).attr("readonly","true");
							commBox.find('#sendDate_'+id).parent().find('button').attr("disabled","true");
							commBox.find('#sendTime_'+id).attr("readonly","true").attr("class","text disabled").unbind();
							commBox.find('#commDetailinfo td:eq(1) a').attr("onclick","").attr("style","background:#ddd");
							commBox.find('#runType').attr("disabled","true");
							commBox.find('#recordName').attr("onclick","");
							commBox.find('#commObjectList .COMMOBJECT').each(function(i){
								$(this).find('#commObjinfo td:eq(1) a').attr("onclick","").attr("style","background:#ddd").attr("href","javascript:;");
							});
							commBox.find('#smsChannel option').each(function(i){
								if(!$(this).attr("selected")){
									$(this).attr("disabled","true");
								}
							});
							commBox.find('#commObjinfo').parent().parent().find('div:eq(1) a').attr("onclick","").attr("style","background:#ddd");
							commBox.find('table:visible #linkContents').removeAttr("onclick");
							commBox.find('#messageListTable td:eq(3) a').attr("readonly","true");
							commBox.find('.editable tbody ').each(function(i){
								$(this).find('#msgDeleteButton').hide();
								$(this).find('td:eq(2) #linkContents').attr("href","javascript:;");
								$(this).find('tr').each(function(){
								$(this).find('td:eq(3) a:first').attr("onclick","").attr("style","background:#ddd").attr("href","javascript:;");
								});
							});
							commBox.find('#objDeleteButton').hide();
//							$('#ol_steps').parent().find('div:eq(0) .right a').hide();
			},
	//检查发送状态
	"checkSendType":function(id){
		var timeType=$('#commBox_'+id+' #timeType [selected="selected"]').val();
		var url=$('#getSendTypeUrl').attr('href');
		var planCode=$('#planCode').val();
		var phaseNum=$('#commBox_'+id).find('#communicationCode').val();
		var params="planCode="+planCode+"&phaseNum="+phaseNum;
		if(timeType == 1){
			cherryAjaxRequest({
				url: url,
				param: params,
				callback: function(data) {
					if(data == 0){
						BINOLCTCOM02.restrictionModification(id);
					}
				}
			});
		}
	},
	"addErrorMessage":function($parent,errorMessage){
		$parent.addClass("error");
		$parent.append('<span class="ui-icon icon-error tooltip-trigger-error" id="errorText"></span>');
		$parent.find('#errorText').attr("title",'error|'+errorMessage);
		$parent.find('#errorText').cluetip({
	    	splitTitle: '|',
		    width: 150,
		    cluezIndex: 20000,
		    tracking: true,
		    cluetipClass: 'error', 
		    arrows: true, 
		    dropShadow: false,
		    hoverIntent: false,
		    sticky: false,
		    mouseOutClose: true,
		    closePosition: 'title',
		    closeText: '<span class="ui-icon ui-icon-close"></span>'
		});
	}
};

var BINOLCTCOM02 = new BINOLCTCOM02_GLOBAL();

$(document).ready(function() {
	// 日历控件初始化
	$("input[id^='sendDate_']").cherryDate({
		beforeShow: function(input){
			var value = $("#nowDate").val();
			return [value, "minDate"];
		}
	});
	$("input[id^='runBeginTime_']").cherryDate({
		beforeShow: function(input){
			var minValue = $("#nowDate").val();
			var maxValue = $(input).parents("tr").find("input[name='runEndTime']").val();
			return [minValue, maxValue];
		}
	});
	$("input[id^='runEndTime_']").cherryDate({
		beforeShow: function(input){
			var value = $(input).parents("tr").find("input[name='runBeginTime']").val();
			if(value==""){
				value = $("#nowDate").val();
			}
			return [value, "minDate"];
		}
	});
	$("input[id^='sendBeginTime_']").cherryDate({
		beforeShow: function(input){
			var minValue = $("#nowDate").val();
			var maxValue = $(input).parents("tr").find("input[name='sendEndTime']").val();
			return [minValue, maxValue];
		}
	});
	$("input[id^='sendEndTime_']").cherryDate({
		beforeShow: function(input){
			var value = $(input).parents("tr").find("input[name='sendBeginTime']").val();
			if(value==""){
				value = $("#nowDate").val();
			}
			return [value, "minDate"];
		}
	});
	$("input[id^='runBegin_']").cherryDate({
		beforeShow: function(input){
			var minValue = $("#nowDate").val();
			var maxValue = $(input).parents("tr").find("input[name='runEnd']").val();
			return [minValue, maxValue];
		}
	});
	$("input[id^='runEnd_']").cherryDate({
		beforeShow: function(input){
			var value = $(input).parents("tr").find("input[name='runBegin']").val();
			if(value==""){
				value = $("#nowDate").val();
			}
			return [value, "minDate"];
		}
	});
	$("input[id^='sendTime_']").timepicker({
		timeOnlyTitle: $("#timeOnlyTitle").val(),
		currentText: $("#currentText").val(),
		closeText: $("#closeText").val(),
		timeText: $("#timeText").val(),
		hourText: $("#hourText").val(),
		minuteText: $("#minuteText").val(),
		hourMax: 22
	});
	$("input[id^='timeValue_']").timepicker({
		timeOnlyTitle: $("#timeOnlyTitle").val(),
		currentText: $("#currentText").val(),
		closeText: $("#closeText").val(),
		timeText: $("#timeText").val(),
		hourText: $("#hourText").val(),
		minuteText: $("#minuteText").val(),
		hourMax: 22
	});
	$("input[id^='tValue_']").timepicker({
		timeOnlyTitle: $("#timeOnlyTitle").val(),
		currentText: $("#currentText").val(),
		closeText: $("#closeText").val(),
		timeText: $("#timeText").val(),
		hourText: $("#hourText").val(),
		minuteText: $("#minuteText").val(),
		hourMax: 22
	});
	
	// 沟通删除按钮初始化 
	if($("#commNameList").find("li").size()<=1){
		$("#commDeleteButton").hide();
	}else{
		$("#commDeleteButton").show();
	}
	// 初始化时移除从上一页面带入的错误信息提示DIV
	$("#cluetip-waitimage").remove();
	$("#cluetip").remove();
	// 页面加载时隐藏除第一个页面块外的其它内容
	var $commInfo = $("#commList").find(".COMMINFO");
	$commInfo.not(":first").hide();
	
	$commInfo.find(".description").cluetip({width: '500',splitTitle:'|',height: 'auto',cluetipClass: 'default',cursor: 'pointer',showTitle: false});
	
	// 页面加载时移除除第一个沟通名称菜单外的其它菜单的被选择样式
	var $commLi = $("#commNameList").find("li");
	$commLi.not(":first").removeClass("on MENUTEXT");
	BINOLCTCOM02.checkSendType(1);
});

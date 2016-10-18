var BINOLCTPLN02_GLOBAL = function () {

};

BINOLCTPLN02_GLOBAL.prototype = {
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
		
	//增加沟通信息页面内容
	"addNewMessage" :function(_this){
		var $box = $(_this).parents("#setInfoBox");
		var $tbody = $box.find("#messageListTable tbody");
		var eventType =  $('#eventType').val();
		$tbody.append($("#addNewMessageinfo tbody").html());
		
		$("#cluetip-waitimage").remove();
		$("#cluetip").remove();
		$("#messageListTable").find("#linkRecordName").cluetip({width: '500',splitTitle:'|',height: 'auto',cluetipClass: 'default',cursor: 'pointer',showTitle: false});
		$("#messageListTable").find("#lblRecordName").cluetip({width: '500',splitTitle:'|',height: 'auto',cluetipClass: 'default',cursor: 'pointer',showTitle: false});
		$("#messageListTable").find("#linkContents").cluetip({width: '500',splitTitle:'|',height: 'auto',cluetipClass: 'default',cursor: 'pointer',showTitle: false});
		// 判断每批沟通对象对应的沟通内容条数，达到2条以上则显示删除按钮
		if($tbody.find("tr").size()<=1){
			$tbody.find("#msgDeleteButton").hide();
		}else{
			$tbody.find("#msgDeleteButton").show();
		}
		if(eventType == "1" || eventType == "2" || eventType == "3" || eventType == "4" || eventType == "14" || eventType == "15"){
			$("#messageListTable").find("#activityCode").attr("disabled",false);
		}else{
			$("#messageListTable").find("#activityCode").attr("disabled",true);
		}
	},
	
	//移除新增加的页面内容
	"viewRemove" :function(obj){
		var $msgList = $(obj).parents("#setInfoBox").find("#messageListTable tbody");
		
		// 移除页面
		var trNode = obj.parentNode.parentNode;
		$(trNode).remove();
		
		// 删除新增加的沟通信息内容时如果少于2条则隐藏删除按钮，确保每批沟通对象至少存在一条沟通信息
		if($msgList.find("tr").size()<=1){
			$msgList.find("#msgDeleteButton").hide();
		}else{
			$msgList.find("#msgDeleteButton").show();
		}
		
	},
	
	// 发送测试信息
	"setReceivedCodeInit" :function(obj){
		var msgType = $(obj).parents("#msgInfoBox").find("#messageType").val();
		var smsChannel = $(obj).parents("#msgInfoBox").find("#smsChannel").val();
		var msgContents = $(obj).parents("#msgInfoBox").find("#contents").val();
		if(msgContents!=""){
			var dialogSetting = {
				dialogInit: "#resDialogInit",
				width: 500,
				height: 220,
				title: $("#resDialogTitle").text()
			};
			openDialog(dialogSetting);
			var url = $(obj).attr("href");
			var param = "messageType=" + msgType +"&smsChannel="+smsChannel + "&contents=" + encodeURI(msgContents);
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
	
	// 沟通對象编辑与选择页面初始化
	"setCommObjInit" :function(obj){
		// 移除页面上可能存在的所有被选中样式
		$(".MSGINFOON").removeClass("MSGINFOON");
		// 弹出页面前给所选的页面块增加样式用于区分被选中的页面范围
		$(obj).parents("#msgInfoBox").prop("class","MSGINFOON");
		var dialogSetting = {
			dialogInit: "#objDialogInit",
			width: 1000,
			height: 660,
			title: $("#objDialogTitle").text(),
			closeEvent:function(){removeDialog("#objDialogInit");oTableArr[0]=null;oTableArr[1]=null;}
		};
		openDialog(dialogSetting);
		// 添加选项卡
		$("#objDialogInit").html($("#searchRecordTabs").html());
		$("#objDialogInit").find('.ui-tabs').tabs();
		$("#objDialogInit").find("#tabs-1").html($("#table_sProcessing").text());
		// 获取全部条件信息
		var conditionInfo = $(obj).parents("#msgInfoBox").find("#conditionInfo").val();
		var setObjUrl = $(obj).attr("href");
		var param = "reqContent=" + conditionInfo;
		var searchName = $(obj).parents("#msgInfoBox").find("#recordName").val();
		var searchCode = $(obj).parents("#msgInfoBox").find("#searchCode").val();
		var recordType = $(obj).parents("#msgInfoBox").find("#recordType").val();
		var callback = function(msg) {
			//初始化搜索条件选项卡
			$("#objDialogInit").find("a@[href='#tabs-1']").click(function() {
				$("#objDialogInit").find("#tabs-1").html(msg);
				// 向沟通对象选择页面增加记录名称输入框
				$("#objDialogInit").find("#tabs-1").prepend($("#addObjName").html());
				if(recordType == '1'){
					// 如果已经选择了沟通对象并且为搜索条件时再次进入时将所选沟通对象记录的名称赋给新增加的输入框
					$("#objDialogInit").find("#tabs-1").find("#searchName").val(searchName);
				}
				$("#objDialogInit").dialog( "option", {
    				buttons: [
						{
							text: $("#dialogConfirm").text(),
						    click: function(){BINOLCTPLN02.searchObjRequest($(obj)); }
						},	
						 {
							text: $("#dialogCancel").text(),
							click: function(){removeDialog("#objDialogInit");oTableArr[0]=null;oTableArr[1]=null; }
						}],
    			});
		    });
			// 初始化搜索结果选项卡
			$("#objDialogInit").find("a@[href='#tabs-2']").click(function() {
				$("#objDialogInit").find("#tabs-2").find("#searchReCordDataTableTem").attr("id","searchReCordDataTable");
				$("#objDialogInit").find("#tabs-2").find("#searchRecordFormTem").attr("id","searchRecordForm");
				//初始化时若为搜索结果或是Excel导入的将SearchCode带入到搜索编号输入框
				if(recordType == '2'){
					$("#objDialogInit #tabs-2").find("input[name='searchCode']").val(searchCode);
				}
				BINOLCTPLN02.searchSearchReCord();
				$("#objDialogInit").dialog( "option", {
    				buttons: [{
    					text: $("#dialogCancel").text(),
    				    click: function(){removeDialog("#objDialogInit");oTableArr[0]=null;oTableArr[1]=null; }
    				}],
    			});
		    });
			// 初始化Excel导入选项卡
			$("#objDialogInit").find("a@[href='#tabs-3']").click(function() {
				//等待数据加载画面
				$("#objDialogInit").find("#tabs-3").html($("#table_sProcessing").text());
				var url = "/Cherry/common/BINOLCM02_initMemImportDialog";
				cherryAjaxRequest({
			        url: url, 
			        param: null,
			        callback: function(msg){
			        	$("#objDialogInit #tabs-3").html(msg);
			        	$("#objDialogInit #tabs-3 #memImportDialog").show();
			        	//在信息提示下添加查看导入明细按钮
			        	if($("#showMessageInfo").length > 0){
			        		$("#showMessageInfo").after($("#importDetailViewBtn").html());
			        	}else{
			        		$("#actionMessage_import").after($("#importDetailViewBtn").html());
			        	}
					}
				});
				$("#objDialogInit").dialog( "option", {
    				buttons: [
						{
							text: $("#dialogConfirm").text(),
						    click: function(){BINOLCTPLN02.chooseCommObj(2,null);oTableArr[0]=null;oTableArr[1]=null; }
						},	
						 {
							text: $("#dialogCancel").text(),
							click: function(){removeDialog("#objDialogInit");oTableArr[0]=null;oTableArr[1]=null; }
						}],
    			});
			});
			// 如果已经选择了沟通对象并且为搜索条件时再次进入时将所选沟通对象记录的名称赋给新增加的输入框
			if(recordType == '1'){
				$("#objDialogInit").find("a@[href='#tabs-1']").click();
			}else if(recordType == '2'){
				$("#objDialogInit").find("a@[href='#tabs-2']").click();
			}else{
				$("#objDialogInit").find("a@[href='#tabs-1']").click();
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
	
	"searchObjRequest" : function(obj){
		var searchValue = $("#objDialogInit").find(":input").serializeForm2Json();
		var searchName = $("#objDialogInit").find("#searchName").val();
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
			var dtime = "EC"+vYear+(vMon<10 ? "0" + vMon : vMon)+(vDay<10 ? "0"+ vDay : vDay)+(h<10 ? "0" + h : h)
						+(m<10 ? "0" + m : m)+(se<10 ? "0" + se : se)+(ms<100 ? "0" + ms : ms);
			
			// 给选择的沟通对象输入框赋值
			$(".MSGINFOON").find("#searchCode").val(dtime);
			$(".MSGINFOON").find("#recordName").val(searchName);
			$(".MSGINFOON").find("#customerType").val("1");
			$(".MSGINFOON").find("#recordCount").val("");
			$(".MSGINFOON").find("#recordType").val("1");
			$(".MSGINFOON").find("#fromType").val("1");
			$(".MSGINFOON").find("#linkRecordName").html(searchName);
			$(".MSGINFOON").find("#lblRecordName").html(searchName);
			// 判断搜索条件值是否存在，若存在则保存条件相关信息，否则直接关闭窗口
			if(searchValue != undefined && null != searchValue && searchValue != ""){
				// 将条件信息保存到页面输入框中
				$(".MSGINFOON").find("#conditionInfo").val(searchValue);
				// 获取返回条件说明的Action地址
				var getInfoUrl = $("#conditionDisplayUrl").attr("href");
				var param = "reqContent=" + searchValue;
				var callback = function(msg) {
					// 将条件说明保存到页面隐藏输入框中用于传递到下一个页面
					$(".MSGINFOON").find("#comments").val(msg);
					// 将条件说明赋给提示框
					$(".MSGINFOON").find("#linkRecordName").attr("title",$("#conditionBoxTitle").val() + "|" + msg);
					$(".MSGINFOON").find("#lblRecordName").attr("title",$("#conditionBoxTitle").val() + "|" + msg);
					// 重新初始化提示框
					$(".MSGINFOON").find("#linkRecordName").cluetip({width: '500',splitTitle:'|',height: 'auto',cluetipClass: 'default',cursor: 'pointer',showTitle: false});
					$(".MSGINFOON").find("#lblRecordName").cluetip({width: '500',splitTitle:'|',height: 'auto',cluetipClass: 'default',cursor: 'pointer',showTitle: false});
					// 关闭弹出窗口
					removeDialog("#objDialogInit");
					// 移除临时样式，将弹出框内的表对象清空
					$(".MSGINFOON").removeClass("MSGINFOON");
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
				removeDialog("#objDialogInit");
				// 移除临时样式，将弹出框内的表对象清空
				$(".MSGINFOON").removeClass("MSGINFOON");
				// 清空表对象
				oTableArr[0]=null;
				oTableArr[1]=null;
			}
		}else{
			// 当用户没有输入搜索记录名称时给出错误提示信息
			var $parent = $("#objDialogInit").find("#searchName").parent();
			$parent.removeClass('error');
			$parent.find("#errorText").remove();
			$parent.addClass("error");
			$parent.append('<span class="ui-icon icon-error tooltip-trigger-error" id="errorText"></span>');
			$parent.find('#errorText').attr("title",'error|' + $("#expNameEmpty").val());
			$parent.find('#errorText').cluetip({
		    	splitTitle: '|',
			    width: 150,
			    cluezIndex: 10000,
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
	
	//选择沟通对象
	"chooseCommObj" : function(type,index){
		if(type == 1){
			//搜索结果
			var searchCode = $("#tbSearchCode_"+index).val();
			var recordName = $("#tbRecordName_"+index).val();
			var customerType = $("#tbCustomerType_"+index).val();
			var recordCount = $("#tbRecordCount_"+index).val();
			var recordType = $("#tbRecordType_"+index).val();
			var fromType = $("#tbFromType_"+index).val();
			var conditionDisplay = $("#searchResult").html();
		}else if(type == 2){
			//Excel导入
			$obj = $("#memImportDialog");
			var searchCode = $.trim($obj.find("[name='searchCode']").val());
			var recordName = $.trim($obj.find("[name='recordName']").val());
			var customerType = $.trim($obj.find("[name='customerType']").val());
			var recordCount = "";
			var recordType = 2;
			var fromType = 3;
			var conditionDisplay = $("#excelImport").html();
			if(recordName == ""){
				this.getError($obj.find("[name='recordName']"), $("#importNameNull").text());
				return false;
			}
			if(searchCode == ""){
				this.getError($obj.find("[name='recordName']"),  $("#importCodeNull").text());
				return false;
			}
		}
		$(".MSGINFOON").find("#searchCode").val(searchCode);
		$(".MSGINFOON").find("#recordName").val(recordName);
		$(".MSGINFOON").find("#customerType").val(customerType);
		$(".MSGINFOON").find("#recordCount").val(recordCount);
		$(".MSGINFOON").find("#recordType").val(recordType);
		$(".MSGINFOON").find("#conditionInfo").val("");
		$(".MSGINFOON").find("#comments").val(conditionDisplay);
		$(".MSGINFOON").find("#fromType").val(fromType);
		$(".MSGINFOON").find("#linkRecordName").html(recordName);
		$(".MSGINFOON").find("#lblRecordName").html(recordName);
		//关闭沟通对象选择框
		removeDialog("#objDialogInit");
		oTableArr[1]=null;
		$(".MSGINFOON").find("#linkRecordName").attr("title",$("#conditionBoxTitle").val() + "|" + conditionDisplay);
		$(".MSGINFOON").find("#lblRecordName").attr("title",$("#conditionBoxTitle").val() + "|" + conditionDisplay);
		// 重新初始化提示框
		$(".MSGINFOON").find("#linkRecordName").cluetip({width: '500',splitTitle:'|',height: 'auto',cluetipClass: 'default',cursor: 'pointer',showTitle: false});
		$(".MSGINFOON").find("#lblRecordName").cluetip({width: '500',splitTitle:'|',height: 'auto',cluetipClass: 'default',cursor: 'pointer',showTitle: false});
	},
	
	//沟通对象明细弹出框
	"showCustomerInit" :function(thisObj,type,index){
		if(type == 1){
			//搜索结果
			var recordCode = $("#tbSearchCode_"+index).val();
			var recordType = $("#tbRecordType_"+index).val();
			var customerType = $("#tbCustomerType_"+index).val();
		}else if(type == 2){
			//Excel导入
			var $obj = $("#memImportDialog");
			var recordCode = $.trim($obj.find("[name='searchCode']").val());
			var customerType = $.trim($obj.find("[name='customerType']").val());
			var recordType = 2;
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
		var templateUse = "";
		var eventType =  $('#eventType').val();
		var messageType = $(obj).parents("#msgInfoBox").find("#messageType").val();
		var smsChannel = $(obj).parents("#msgInfoBox").find("#smsChannel").val();
		var msgContents = $(obj).parents("#msgInfoBox").find("#contents").val();
		var setMessageUrl = $(obj).attr("href");
		// 根据事件类型确定模板变量
		if(eventType=="1"){
			templateUse = "YYCG";
		}else if(eventType=="2"){
			templateUse = "HDFH";
		}else if(eventType=="3"){
			templateUse = "GTSH";
		}else if(eventType=="4"){
			templateUse = "YYQX";
		}else if(eventType=="5"){
			templateUse = "HYRH";
		}else if(eventType=="6"){
			templateUse = "HYZG";
		}else if(eventType=="71" || eventType=="72" || eventType=="73" || eventType=="74" 
			|| eventType=="75" || eventType=="76" || eventType=="77" || eventType=="78"){
			templateUse = "JFBH";
		}else if(eventType=="8"){
			templateUse = "ZLBG";
		}else if(eventType=="11"){
			templateUse = "WXRZ";
		}else if(eventType=="12"){
			templateUse = "HYSJ";
		}else if(eventType=="13"){
			templateUse = "MMCX";
		}else if(eventType=="14"){
			templateUse = "YZXX";
		}else if(eventType=="15"){
			templateUse = "GTSH";
		}else if(eventType=="90"){
			templateUse = "YHMM";
		}
		
		var param = "templateUse=" + templateUse + "&messageType=" + messageType +"&smsChannel=" + smsChannel
			+ "&msgContents=" + encodeURI(msgContents);
		var callback = function(msg) {
			$("#dialogInit").html(msg);
		};
		cherryAjaxRequest({
			url: setMessageUrl,
			param: param,
			callback: callback
		});
	},
	
	"infoEdit" :function(obj){
		var $this = $(obj);
		var eventType = $('#eventType').val();
		$("#setInfoEdit").hide();
		$("#setInfoSave").show();
		$("#addMsgBox").show();
		$("#messageListTable").find("#editButton").show();
		if($("#messageListTable tbody").find("tr").size()<=1){
			$("#messageListTable").find("#msgDeleteButton").hide();
		}else{
			$("#messageListTable").find("#msgDeleteButton").show();
		}
		$("#messageListTable").find("#linkRecordName").show();
		$("#messageListTable").find("#lblRecordName").hide();
		$("#messageListTable").find("#linkContents").show();
		$("#messageListTable").find("#lblContents").hide();
		$("#messageListTable").find("#messageType").attr("disabled",false);
		$("#messageListTable").find("#smsChannel").attr("disabled",false);
		$("#frequencyCode").attr("disabled",false);
		$("#sendBeginTime").attr("disabled",false);
		$("#sendEndTime").attr("disabled",false);
		if(eventType == "1" || eventType == "2" || eventType == "3" || eventType == "4" || eventType == "14" || eventType == "15"){
			$("#messageListTable").find("#activityCode").attr("disabled",false);
		}else{
			$("#messageListTable").find("#activityCode").attr("disabled",true);
		}
		$("#cluetip-waitimage").remove();
		$("#cluetip").remove();
		$("#messageListTable").find("#linkRecordName").cluetip({width: '500',splitTitle:'|',height: 'auto',cluetipClass: 'default',cursor: 'pointer',showTitle: false});
		$("#messageListTable").find("#lblRecordName").cluetip({width: '500',splitTitle:'|',height: 'auto',cluetipClass: 'default',cursor: 'pointer',showTitle: false});
		$("#messageListTable").find("#linkContents").cluetip({width: '500',splitTitle:'|',height: 'auto',cluetipClass: 'default',cursor: 'pointer',showTitle: false});
		if(eventType == "90"){
			$("#messageListTable").find("#linkRecordName").html("");
			$("#messageListTable").find("#lblRecordName").html("");
		}
	},
	
	"editCancel" :function(obj){
		BINOLCTPLN02.doChange();
	},
	
	"doChange" :function(){
		var url = $('#doChangeUrl').html();
		var eventType = $('#eventType').val();
		var param = "eventType=" + eventType;
		if(eventType=="77"){
			$('#lblExplanation1').show();
			$('#lblExplanation2').hide();
		}else if(eventType=="78"){
			$('#lblExplanation1').hide();
			$('#lblExplanation2').show();
		}else{
			$('#lblExplanation1').hide();
			$('#lblExplanation2').hide();
		}
		cherryAjaxRequest({
			url:url,
			param:param,
			callback: function(msg) {
				// 刷新数据
				$("#delaySetBox").html(msg);
				// 时间控件初始化
				$("#sendBeginTime").timepicker({
					timeOnlyTitle: $("#timeOnlyTitle").val(),
					currentText: $("#currentText").val(),
					closeText: $("#closeText").val(),
					timeText: $("#timeText").val(),
					hourText: $("#hourText").val(),
					minuteText: $("#minuteText").val()
				});
				$("#sendEndTime").timepicker({
					timeOnlyTitle: $("#timeOnlyTitle").val(),
					currentText: $("#currentText").val(),
					closeText: $("#closeText").val(),
					timeText: $("#timeText").val(),
					hourText: $("#hourText").val(),
					minuteText: $("#minuteText").val()
				});
				// 初始化时移除从上一页面带入的错误信息提示DIV
				$("#cluetip-waitimage").remove();
				$("#cluetip").remove();
				$("#messageListTable").find("#linkRecordName").cluetip({width: '500',splitTitle:'|',height: 'auto',cluetipClass: 'default',cursor: 'pointer',showTitle: false});
				$("#messageListTable").find("#lblRecordName").cluetip({width: '500',splitTitle:'|',height: 'auto',cluetipClass: 'default',cursor: 'pointer',showTitle: false});
				$("#messageListTable").find("#lblContents").cluetip({width: '500',splitTitle:'|',height: 'auto',cluetipClass: 'default',cursor: 'pointer',showTitle: false});
				$("#setInfoSave").hide();
				$("#setInfoEdit").show();
				$("#addMsgBox").hide();
				$("#messageListTable").find("#editButton").hide();
				$("#messageListTable").find("#msgDeleteButton").hide();
				$("#messageListTable").find("#messageType").attr("disabled",true);
				$("#messageListTable").find("#smsChannel").attr("disabled",true);
				$("#messageListTable").find("#activityCode").attr("disabled",true);
				$("#messageListTable").find("#linkRecordName").hide();
				$("#messageListTable").find("#lblRecordName").show();
				$("#messageListTable").find("#linkContents").hide();
				$("#messageListTable").find("#lblContents").show();
				$("#frequencyCode").attr("disabled",true);
				$("#sendBeginTime").attr("disabled",true);
				$("#sendEndTime").attr("disabled",true);
				if($("#contents").val()==""){
					$("#stopEventSetButton").hide();
				}else{
					$("#stopEventSetButton").show();
				}
				if(eventType == "90"){
					$("#messageListTable").find("#linkRecordName").html("");
					$("#messageListTable").find("#lblRecordName").html("");
				}
			}
		});
	},
	
	"frequencyChange":function (){
		if($("#frequencyCode").val() == "1"){
			$("#sendTimeRange").show();
		}else{
			$("#sendTimeRange").hide();
		}
	},
	
	/**
	 * 停用事件设置弹出框
	 * @return
	 */
	"stopEventSetDialog":function (url){
		var dialogId = "stop_eventSet_dialog";
		var $dialog = $('#' + dialogId);
		$dialog.dialog({ 
			//默认不打开弹出框
			autoOpen: false,  
			//弹出框宽度
			width: 400, 
			//弹出框高度
			height: 250, 
			//弹出框标题
			title:$("#stopDialogTitle").text(), 
			//弹出框索引
			zIndex: 1,  
			modal: true, 
			resizable:false,
			//弹出框按钮
			buttons: [{
				text:$("#dialogConfirm").text(),//确认按钮
				click: function() {
					//点击确认后执行停用
					BINOLCTPLN02.doStop(url);
					$dialog.dialog("close");
				}
			},
			{	
				text:$("#dialogCancel").text(),//取消按钮
				click: function() {
					$dialog.dialog("close");
				}
			}],
			//关闭按钮
			close: function() {
				closeCherryDialog(dialogId);
			}
		});
		$dialog.dialog("open");
	},
	
	"doStop" :function(url){
		// 获取事件类型参数
		var eventType = $('#eventType').val();
		// 增加参数
		var param = "eventType=" + eventType;
		cherryAjaxRequest({
			url: url,
			param: param,
			callback: function(msg) {
				$("#stop_eventSet_dialog").dialog("close");
				//刷新数据
				BINOLCTPLN02.doChange();
			},
			coverId: "#eventManage"
		});
		
	},
	
	/**
	 * 保存事件设置弹出框
	 * @return
	 */
	"saveEventSetDialog":function (url){
		var dialogId = "save_eventSet_dialog";
		var $dialog = $('#' + dialogId);
		$dialog.dialog({ 
			//默认不打开弹出框
			autoOpen: false,  
			//弹出框宽度
			width: 400, 
			//弹出框高度
			height: 250, 
			//弹出框标题
			title:$("#saveDialogTitle").text(), 
			//弹出框索引
			zIndex: 1,  
			modal: true, 
			resizable:false,
			//弹出框按钮
			buttons: [{
				text:$("#dialogConfirm").text(),//确认按钮
				click: function() {
					//点击确认后执行保存
					BINOLCTPLN02.doSave(url);
					$dialog.dialog("close");
				}
			},
			{	
				text:$("#dialogCancel").text(),//取消按钮
				click: function() {
					$dialog.dialog("close");
				}
			}],
			//关闭按钮
			close: function() {
				closeCherryDialog(dialogId);
			}
		});
		$dialog.dialog("open");
	},
	
	"doSave" :function(url){
		var $msgBoxs = $("#messageListTable tbody");
		// 获取页面的Json格式结果
		var eventSetInfo = Obj2JSONArr($msgBoxs.find("tr"));
		// 获取事件类型参数
		var eventType = $('#eventType').val();
		// 获取事件延时频率
		var frequencyCode = $('#frequencyCode').val();
		// 获取事件允许发送信息开始时间
		var sendBeginTime = $('#sendBeginTime').val();
		// 获取事件允许发送信息截止时间
		var sendEndTime = $('#sendEndTime').val();
		// 增加参数
		var param = "eventType=" + eventType + "&frequencyCode=" + frequencyCode 
			+ "&sendBeginTime=" + sendBeginTime + "&sendEndTime=" + sendEndTime + "&eventSetInfo=" + encodeURI(eventSetInfo);
		cherryAjaxRequest({
			url: url,
			param: param,
			callback: function(msg) {
				$("#save_eventSet_dialog").dialog("close");
				//刷新数据
				BINOLCTPLN02.doChange();
			},
			coverId: "#pageButton"
		});
		
	}
	
}

var BINOLCTPLN02 = new BINOLCTPLN02_GLOBAL();

$(document).ready(function() {
	$("#messageListTable").find("#messageType").attr("disabled",true);
	$("#messageListTable").find("#smsChannel").attr("disabled",true);
	$("#messageListTable").find("#activityCode").attr("disabled",true);
	$("#messageListTable").find("#linkRecordName").hide();
	$("#messageListTable").find("#lblRecordName").show();
	$("#messageListTable").find("#linkContents").hide();
	$("#messageListTable").find("#lblContents").show();
	BINOLCTPLN02.doChange();
});








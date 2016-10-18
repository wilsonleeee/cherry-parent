var BINOLMOCIO01 = function () {};

BINOLMOCIO01.prototype = {
	"BINOLMOCIO01_Tree":null,
	"messageDatailHtml":null,
	
	/*
	 * 用户查询
	 */
	"search": function(){
		if (!$('#mainForm').valid()) {
			return false;
		};
		$("#mainForm").find(":input").each(function() {
			$(this).val($.trim(this.value));
		});
		 var url = $("#searchUrl").attr("href");
		 // 查询参数序列化
		 var params= $("#mainForm").serialize();
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
				 // 表格默认排序
				 aaSorting : [[ 6, "desc" ]],
				 // 表格列属性设置
				 aoColumns : [	{ "sName": "no","bSortable": false},
								{ "sName": "messageTitle"},
								{ "sName": "messageBody"},
								{ "sName": "importBatch"},
								{ "sName": "startValidDate","sClass": "center"},
								{ "sName": "endValidDate","sClass": "center"},
								{ "sName": "publishDate","sClass": "center"},
								{ "sName": "status","sClass": "center"},
								{ "sName": "operate","bSortable": false}
							],			
								
				// 不可设置显示或隐藏的列	
				aiExclude :[0, 1, 2],
				// 横向滚动条出现的临界宽度
				sScrollX : "100%",
				// 滚动体的宽度
				sScrollXInner:"",
				// 固定列数
				fixedColumns : 3,
		        fnDrawCallback:function(){
			 		$("#allSelect").prop("checked",false);
//		        	$("#deleteBtn").addClass("ui-state-disabled");
		        }
		 };
		 // 调用获取表格函数
		 getTable(tableSetting);
	},
	/**
	 * 柜台消息数据过滤
	 */
	"cntMsgDateFilter" : function(filterType,thisObj){
		
		$('#ui-tabs').find('li').removeClass('ui-tabs-selected');
		$(thisObj).addClass('ui-tabs-selected');
		// 数据过滤
		oTableArr[1].fnFilter(filterType);

		$('#th_operator').show();
	},
	
	/*
	 * 保存消息
	 */
	"saveMsg":function(url){
		if(!$("#msgForm").valid()){
			return;
		}
		$('#msgForm :input').each(function(){
			$(this).val($.trim(this.value));
		});
		var param = $("#msgForm").serialize() + "&csrftoken=" + getTokenVal();
		var callback = function(msg) {
			if(msg.indexOf('id="actionResultDiv"') != -1 || msg.indexOf('id="fieldErrorDiv"') != -1) {
			} else {
				$("#dialogInit").html(msg);
				// 保存成功的场合
				if($("#dialogInit").find("#successDiv").length != 0) {
					$("#dialogInit").dialog("destroy");
	                var dialogSetting = {
						dialogInit: "#dialogInit",
						text: msg,
						width: 500,
						height: 300,
						title: $("#saveSuccessTitle").text(),
						confirm: $("#dialogClose").text(),
						confirmEvent: function(){removeDialog("#dialogInit");if(oTableArr[1] != null)oTableArr[1].fnDraw();},
                        closeEvent: function(){removeDialog("#dialogInit");if(oTableArr[1] != null)oTableArr[1].fnDraw();}
					};
					openDialog(dialogSetting);
				}// 保存失败的场合
				else if($("#dialogInit").find("#errorMessageDiv").length != 0){
	                $("#errorMessageDiv").find("span").html($("#errorMessageDiv").next().text());
	                $("#dialogInit").dialog( "option", {
	                    buttons: [{
	                        text: $("#dialogClose").text(),
	                        click: function(){removeDialog("#dialogInit");if(oTableArr[1] != null)oTableArr[1].fnDraw(); }
	                    }],
						closeEvent: function(){removeDialog("#dialogInit");if(oTableArr[1] != null)oTableArr[1].fnDraw();}
	                });
				}
			}
		};
		cherryAjaxRequest({
			url: url,
			param: param,
			callback: callback,
			formId: '#msgForm'
		});
	},
	
	/*
	 * 新增柜台消息初始化pop窗口
	 */
	"addMsgInit":function(object){
		var dialogSetting = {
			dialogInit: "#dialogInit",
			width: 	480,
			height: 300,
			title: 	$("#addMsgTitle").text(),
			confirm: $("#dialogConfirm").text(),
			cancel: $("#dialogCancel").text(),
			confirmEvent: function(){BINOLMOCIO01.saveMsg($("#addMsgUrl").attr("href"));},
			cancelEvent: function(){removeDialog("#dialogInit");}
		};
		openDialog(dialogSetting);
		var addUrl = $(object).attr("href");
		var callback = function(msg) {
			$("#dialogInit").html(msg);
		};
		cherryAjaxRequest({
			url: addUrl,
			param: null,
			callback: callback,
			formId: '#mainForm'
		});
	},
		
	/*
	 * 编辑柜台消息初始化pop窗口
	 */
	"editMsgInit":function(object){
		var dialogSetting = {
			dialogInit: "#dialogInit",	
			width: 	500,
			height: 300,
			title: 	$("#updateMsgTitle").text(),
			confirm: $("#dialogConfirm").text(),
			cancel: $("#dialogCancel").text(),
			confirmEvent: function(){BINOLMOCIO01.saveMsg($("#updateMsgUrl").attr("href"));},
			cancelEvent: function(){removeDialog("#dialogInit");}
		};
		openDialog(dialogSetting);
		var updateUrl = $(object).attr("href");
		var param = $(object).parent().find(':input[name=counterMessageId]').serialize() + "&csrftoken=" + getTokenVal();
		var callback = function(msg) {
			$("#dialogInit").html(msg);
		};
		cherryAjaxRequest({
			url: updateUrl,
			param: param,
			callback: callback,
			formId: '#mainForm'
		});
	},
	
	/*
	 * 删除柜台消息确认初始化pop窗口
	 */
	"delMsgInit":function(obj){
		var $this = $(obj);
		var dialogSetting = {
			dialogInit: "#dialogInit",
			text:  $("#deleteMsgText").html(),
			width: 	500,
			height: 300,
			title: 	$("#deleteMsgTitle").text(),
			confirm: $("#dialogConfirm").text(),
			cancel: $("#dialogCancel").text(),
			confirmEvent: function(){BINOLMOCIO01.delMsg($this.attr("href"));},
			cancelEvent: function(){removeDialog("#dialogInit");}
		};
		openDialog(dialogSetting);
	},
	
	/*
	 * 删除柜台消息 
	 */
	"delMsg":function(url){
		var param = "csrftoken=" + getTokenVal();
		var callback = function(msg) {
			if(msg.indexOf('id="actionResultDiv"') != -1 || msg.indexOf('id="fieldErrorDiv"') != -1) {
			} else {
				$("#dialogInit").html(msg);
				// 删除成功的场合
				if($("#dialogInit").find("#successDiv").length != 0) {
					$("#dialogInit").dialog("destroy");
	                var dialogSetting = {
						dialogInit: "#dialogInit",
						text: msg,
						width: 500,
						height: 300,
						title: $("#saveSuccessTitle").text(),
						confirm: $("#dialogClose").text(),
						confirmEvent: function(){removeDialog("#dialogInit");if(oTableArr[1] != null)oTableArr[1].fnDraw();},
                        closeEvent: function(){removeDialog("#dialogInit");if(oTableArr[1] != null)oTableArr[1].fnDraw();}
					};
					openDialog(dialogSetting);
				}// 删除失败的场合
				else if($("#dialogInit").find("#errorMessageDiv").length != 0){
	                $("#errorMessageDiv").find("span").html($("#errorMessageDiv").next().text());
	                $("#dialogInit").dialog( "option", {
	                    buttons: [{
	                        text: $("#dialogClose").text(),
	                        click: function(){removeDialog("#dialogInit");if(oTableArr[1] != null)oTableArr[1].fnDraw(); }
	                    }],
						closeEvent: function(){removeDialog("#dialogInit");if(oTableArr[1] != null)oTableArr[1].fnDraw();}
	                });
				}
			}
		};
		cherryAjaxRequest({
			url: url,
			param: param,
			callback: callback
		});
	},
	
	/**
	 * 取得下发区域,并加载树
	 * 
	 * 
	 * */
	"getMessageRegion":function(obj){
		var that = this;
		var dialogSetting = {
				dialogInit: "#region",
				width: 	450,
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
		demoHtml.push('<span><select name="selMode" id="selMode">');
		demoHtml.push('<option value="1">'+$("#selMode1").text()+'</option>');
		demoHtml.push('<option value="2">'+$("#selMode2").text()+'</option>');
		demoHtml.push('<option value="3">'+$("#selMode3").text()+'</option>');
		demoHtml.push('</select></span>');
		demoHtml.push('<span class="right"><input id="position_left_0" class="text locationPosition ac_input" type="text" style="width: 160px;" name="locationPosition" autocomplete="off"><a class="search" onclick="BINOLMOCIO01.locationCutPosition(this);return false;"><span class="ui-icon icon-position"></span><span class="button-text">定位</s:text></span></a></span>');
		demoHtml.push('</div>');
		demoHtml.push('<div id="channelRegionDiv" class="hide" style="padding: 5px 5px 5px 8px;border-bottom: 1px dashed #D6D6D6;"><strong>'+$("#channelRegion").text()+'</strong>');
		demoHtml.push('<span></span></div>');
		demoHtml.push('<div class="jquery_tree treebox_left ztree" id = "treeDemo" style="overflow: auto;background:#FCFCFC;min-height:400px;max-height:700px;height:auto">');
		demoHtml.push('</div>');
		demoHtml.push('<div id="dataTable_processing" class="hide dataTables_processing"  style="text-algin:left;"></div>');
		demoHtml.push('</div>');
		$("#region").html(demoHtml.join(''));
		
		counterBinding({
			elementId:"position_left_0",
			showNum:20,
			selected:"codeName"
		});
		
		$("#selMode").change(function(){
			// 显示进度条
			$("#region").find("#dataTable_processing").removeClass("hide");
			var param = "privilegeFlag=1&"+ $("#selMode").serialize() + "&csrftoken=" + getTokenVal();
			that.getNodes(obj,param);
		});
		$("#selMode").trigger("change");
		
		$("#position_left_0").bind("keydown",function(event){
			if(event.keyCode==13){
				that.locationCutPosition(this,"keydown");
	        }
		});
	},
	
	/**
	 * 加载渠道柜台树
	 */
	"loadChannelRegion" : function(obj,msg) {
		var that = this;
		var currentMessageId = $(obj).parent().find(":input[name=currentMessageId]").val();
		var channelRegionId = $("#channelRegionId").val();
		// 取得大区信息List
		var regionIdInfo = eval('('+msg+')');
		if(regionIdInfo && regionIdInfo.length > 0) {
			var regionSelect = '<select name="channelRegionId" id="channelRegionId"><option value="">'+$("#select_default").html()+'</option>';
			for(var i = 0; i < regionIdInfo.length; i++) {
				regionSelect += '<option value="'+regionIdInfo[i].regionId+'">'+regionIdInfo[i].regionName+'</option>';
			}
			regionSelect += '</select>';
			$('#channelRegionDiv').find("span").html(regionSelect);
			// 显示大区选择框
			$("#channelRegionDiv").removeClass("hide");
			if(channelRegionId != undefined && channelRegionId != ''){
				$('#channelRegionDiv').find(":input[name=channelRegionId]").val(channelRegionId);
			}
			// 选择框的change事件（加载指定大区的渠道柜台树）
			$('#channelRegionDiv').find("#channelRegionId").change(function() {
				var channelUrl = $("#getMessageChannel_url").attr("href");
				var channelParam = "privilegeFlag=1&currentMessageId="
								+ currentMessageId + "&"
								+ $("#channelRegionId").serialize()
								+ "&csrftoken=" + getTokenVal();
				// 显示进度条
				$("#region").find("#dataTable_processing").removeClass("hide");
				var channelCallback = function (msg) {
					var nodes = window.JSON2.parse(msg);
					// 加载指定大区的渠道柜台树
					that.loadTree(nodes);
				};
				cherryAjaxRequest({
					url: channelUrl,
					param: channelParam,
					callback: channelCallback
				});
			});
			//初始执行一次change事件
			$('#channelRegionDiv').find("#channelRegionId").trigger("change");
		}
	},
	
	/**
	 * AJAX加载树
	 * 
	 * */
	"getNodes":function(obj,param){
		var that = this;
		var selMode = $("#selMode").val();
		var url = obj.href;
		var callback = function(msg){
			if(msg.indexOf('id="actionResultDiv"') > -1){
				removeDialog("#region");
			}else{
				if(selMode == '1' || selMode == '3'){
					$('#channelRegionDiv').find("span").html("");
					$("#channelRegionDiv").addClass("hide");
					var nodes = window.JSON2.parse(msg);
					that.loadTree(nodes);
				}else if(selMode == '2'){
					that.loadChannelRegion(obj,msg);
				}
			}
		};
		cherryAjaxRequest(
				{
					url:url,
					param:param,
					callback:callback
				}
		);
	},
	
	/**
	 * 根据节点加载树
	 * */
	"loadTree" : function(nodes){
		$("#position_left_0").val("");
		var that = this;
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
		that.BINOLMOCIO01_Tree = null;
		that.BINOLMOCIO01_Tree = $.fn.zTree.init($("#treeDemo"), treeSetting, nodes);
		// 隐藏进度条
		$("#region").find("#dataTable_processing").addClass("hide");
	},
	
	/**
	 * 树节点定位
	 * 
	 * */
	"locationCutPosition":function(obj,flag){
		if(typeof flag == "undefined"){
			var $input = $(obj).prev();
		}else{
			var $input = $(obj);
		}
		if(null == this.BINOLMOCIO01_Tree){
			return false;
		}
		var inputNode = this.BINOLMOCIO01_Tree.getNodeByParam("name",$input.val());
		this.BINOLMOCIO01_Tree.expandNode(inputNode,true,false);
		this.BINOLMOCIO01_Tree.selectNode(inputNode);
	},
	
	/**
	 * 弹出消息详细
	 * 
	 * */
	"showDetail":function(obj){
		var $this = $(obj);
		var that = this;
		var dialogSetting = {
				dialogInit: "#messageDatail",
				width: 	430,
				height: 300,
				title: 	$("#detailTitle").text(),
				confirm: $("#dialogClose").text(),
				confirmEvent: function(){
					removeDialog("#messageDatail");
				}
			};
		that.messageDatailHtml = $("#detail").html();
		openDialog(dialogSetting);
		$("#messageDatail").html(that.messageDatailHtml);
		var messageDatail = $this.prop("name");
		var messageTitle = $this.prop("id");
		$("#messageDatail").find("#messageTitle").html(messageTitle.escapeHTML());
		$("#messageDatail").find("#messageBody").html(messageDatail.escapeHTML());
	},
	

	/**
	 * 弹出停用、启用确认框
	 */
	"disableOrEnableDialog" : function(obj, flag) {
		var $this = $(obj);
		var textShow = "";
		var titleShow = "";
		if (flag == "0") {
			textShow = $("#disableMsgText").html();
			titleShow = $("#disableMsgTitle").text();
		} else if (flag == "1") {
			textShow = $("#enableMsgText").html();
			titleShow = $("#enableMsgTitle").text();
		}
		var dialogSetting = {
			dialogInit : "#dialogInit",
			text : textShow,
			width : 500,
			height : 300,
			title : titleShow,
			confirm : $("#dialogConfirm").text(),
			cancel : $("#dialogCancel").text(),
			confirmEvent : function() {
				BINOLMOCIO01.disableOrEnable($this.attr("href"), flag);
			},
			cancelEvent : function() {
				removeDialog("#dialogInit");
			}
		};
		openDialog(dialogSetting);
	},
	
	/**
	 * 停用（flag=0）、启用（flag=1）柜台消息
	 */
	"disableOrEnable" : function(url, flag) {
		var that = this;
		var param = "status=" + flag + "&csrftoken=" + getTokenVal();
		var opt = {
			url : url,
			param : param,
			callback : function(msg) {
				if(msg.indexOf('id="actionResultDiv"') != -1 || msg.indexOf('id="fieldErrorDiv"') != -1) {
					// 出现指定异常的场合
					$("#dialogInit").html(msg);
//					var textShow = $("#actionResultDiv").find("span").html();
//					$("#actionResultDiv").removeClass().html('<p class="message"><span>'+textShow+'</span></p>');
	                $("#dialogInit").dialog( "option", {
	                    buttons: [{
	                        text: $("#dialogClose").text(),
	                        click: function(){removeDialog("#dialogInit");if(oTableArr[1] != null)oTableArr[1].fnDraw(); }
	                    }],
						closeEvent: function(){removeDialog("#dialogInit");if(oTableArr[1] != null)oTableArr[1].fnDraw();}
	                });
				} else {
					$("#dialogInit").html(msg);
					// 成功的场合
					if($("#dialogInit").find("#successDiv").length != 0) {
						$("#dialogInit").dialog("destroy");
		                var dialogSetting = {
							dialogInit: "#dialogInit",
							text: msg,
							width: 500,
							height: 300,
							title: $("#saveSuccessTitle").text(),
							confirm: $("#dialogClose").text(),
							confirmEvent: function(){removeDialog("#dialogInit");if(oTableArr[1] != null)oTableArr[1].fnDraw();},
	                        closeEvent: function(){removeDialog("#dialogInit");if(oTableArr[1] != null)oTableArr[1].fnDraw();}
						};
						openDialog(dialogSetting);
					}
					
				}
			}
		};
		cherryAjaxRequest(opt);
	}
};

var BINOLMOCIO01 = new BINOLMOCIO01();

$(function(){
	// 表单验证配置
    cherryValidate({
        formId: 'mainForm',
        rules: {
        	startDate: {dateValid:true},    // 开始日期
        	endDate: {dateValid:true}   // 结束日期
    	}
    });
        
	BINOLMOCIO01.search();
	
//	// 表格列选中
//    $('thead th').live('click',function(){
//        $("th.sorting").removeClass('sorting');
//        $(this).addClass('sorting');
//    });
	
//	$("#deleteBtn").live('click',function(){
////		if(BINOLMOCIO01.isEnable("#deleteBtn")){
//			BINOLMOCIO01.delMsgInit();
////		}
//	});
});
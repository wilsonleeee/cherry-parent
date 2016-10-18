var BINOLMOPMC01 = function(){
	
};

BINOLMOPMC01.prototype = {
		
		/**
		 * 查询
		 */
		"search" : function() {
			this.clearActionHtml();
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
					 // 表格默认排序
					 aaSorting : [[ 4, "desc" ]],
					 // 数据URL
					 url : url,
					 // 表格列属性设置			 
					 aoColumns :    [{ "sName": "no","bSortable": false},			// 0
					                 { "sName": "menuGrpName"},						// 1
					                 { "sName": "startDate","sClass": "center"},						// 2
					                 { "sName": "endDate","sClass": "center"},						// 3
					                 { "sName": "publishDate","sClass": "center"},						// 4
					                 { "sName": "machineType","sClass": "center"},						// 5
									 { "sName": "operation", "sClass" : "center", "sWidth": "10%","bSortable": false}// 6
					                 ],
					                     
					 // 不可设置显示或隐藏的列	
				   	aiExclude :[0, 1],
					// 横向滚动条出现的临界宽度
					sScrollX : "100%",
					// 滚动体的宽度
					sScrollXInner:"",
					// 固定列数
					fixedColumns : 2,
					callbackFun : function (){
			 		}
			 };
			 // 调用获取表格函数
			 getTable(tableSetting);
		},
		
		/**
		 * 菜单组数据过滤
		 */
		"menuGrpDateFilter" : function(filterType,thisObj){
			$('#ui-tabs').find('li').removeClass('ui-tabs-selected');
			$(thisObj).addClass('ui-tabs-selected');
			// 数据过滤
			oTableArr[1].fnFilter(filterType);

			$('#th_operator').show();
		},
		
		/**
		 * 弹出新增菜单分组对话框
		 */
		"popAddMenuGrp" : function(object) {
			var that = this;
			that.clearActionHtml();
			var dialogSetting = {
					dialogInit: "#dialogInit",
					width: 	350,
					height: 200,
					title: 	$("#addMenuGrpTitle").text(),
					confirm: $("#dialogConfirm").text(),
					cancel: $("#dialogCancel").text(),
					confirmEvent: function(){// 保存新增的菜单分组
			    			that.saveMenuGrp($("#addMenuGrpUrl").attr("href"));
			    		},
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
		/**
		 * 保存菜单分组信息
		 */
		"saveMenuGrp" : function(url) {
			if(!$('#operateForm').valid()) {
				return false;
			}
			$('#operateForm :input').each(function(){
				$(this).val($.trim(this.value));
			});
			var param = $("#operateForm").serialize() + "&csrftoken=" + getTokenVal();
			var callback = function(msg){
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
					} else if($("#dialogInit").find("#errorMessageDiv").length != 0) {
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
				formId: '#operateForm',
				callback: callback
			});	
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
		 * 弹出编辑对话框
		 */
		"popEditDialog" : function(object) {
			var that = this;
			that.clearActionHtml();
			var dialogSetting = {
					dialogInit: "#dialogInit",	
					width: 	350,
					height: 200,
					title: 	$("#updateMenuGrpTitle").text(),
					confirm: $("#dialogConfirm").text(),
					cancel: $("#dialogCancel").text(),
					confirmEvent: function(){// 编辑新增的菜单分组
				    		that.saveMenuGrp($("#updateMenuGrpUrl").attr("href"));
				    	},
					cancelEvent: function(){removeDialog("#dialogInit");}
				};
			openDialog(dialogSetting);
			var updateInitUrl = $(object).attr("href");
			var param = "csrftoken=" + getTokenVal();
			var callback = function(msg) {
				$("#dialogInit").html(msg);
			};
			cherryAjaxRequest({
				url: updateInitUrl,
				param: param,
				callback: callback,
				formId: '#mainForm'
			});
		},
		
		/**
		 * 弹出复制对话框
		 */
//		"popCopyDialog" : function(object) {
//			var that = this;
//			that.clearActionHtml();
//			var dialogSetting = {
//					dialogInit: "#dialogInit",	
//					width: 	350,
//					height: 200,
//					title: 	$("#copyMenuGrpTitle").text(),
//					confirm: $("#dialogConfirm").text(),
//					cancel: $("#dialogCancel").text(),
//					confirmEvent: function(){// 复制菜单组及其菜单配置
//				    		that.saveMenuGrp($("#copyMenuGrpAndConfigUrl").attr("href"));
//				    	},
//					cancelEvent: function(){removeDialog("#dialogInit");}
//				};
//			openDialog(dialogSetting);
//			var initUrl = $(object).attr("href");
//			var param = "csrftoken=" + getTokenVal();
//			var callback = function(msg) {
//				$("#dialogInit").html(msg);
//			};
//			cherryAjaxRequest({
//				url: initUrl,
//				param: param,
//				callback: callback,
//				formId: '#mainForm'
//			});
//		},
		
		/**
		 * 弹出删除确认页面
		 */
		"popDeleteDialog" : function(obj) {
			var that = this;
			that.clearActionHtml();
			var param = $(obj).parent().find(':input').serialize()+"&csrftoken=" + getTokenVal();
			var dialogSetting = {
				dialogInit: "#dialogInit",
				text: $("#dialogContent").html(),
				width: 	500,
				height: 300,
				title: 	$('#deleteMenuGrpTitle').text(),
				confirm: $("#dialogConfirm").text(),
				cancel: $("#dialogCancel").text(),
				confirmEvent: function(){
					that.deleteMenuGrp($(obj).attr("href"),param);
					removeDialog("#dialogInit");
				},
				cancelEvent: function(){
					removeDialog("#dialogInit");
				}
			};
			openDialog(dialogSetting);
		},
		
		/**
		 * 删除菜单组
		 */
		"deleteMenuGrp" : function(url,param) {
			var that = this;
			var callback = function(msg){
				if(msg.indexOf('id="actionResultDiv"') > -1){
						if(oTableArr[1] != null)oTableArr[1].fnDraw();
				}
			};
			cherryAjaxRequest({
				url: url,
				param: param,
				callback: callback
			});
		},
		
		"popViewPublish" : function(obj) {
			var that = this;
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
			demoHtml.push('<span><select name="selMode" id="selMode">');
			demoHtml.push('<option value="1">'+$("#selMode1").text()+'</option>');
			demoHtml.push('<option value="2">'+$("#selMode2").text()+'</option>');
			demoHtml.push('<option value="3">'+$("#selMode3").text()+'</option>');
			demoHtml.push('</select></span>');
			demoHtml.push('<span class="right"><input id="position_left_0" class="text locationPosition ac_input" type="text" style="width: 160px;" name="locationPosition" autocomplete="off"><a class="search" onclick="BINOLMOPMC01.locationPosition(this);return false;"><span class="ui-icon icon-position"></span><span class="button-text">定位</s:text></span></a></span>');
			demoHtml.push('</div>');
			demoHtml.push('<div id="channelRegionDiv" class="hide" style="padding: 5px 5px 5px 8px;border-bottom: 1px dashed #D6D6D6;"><strong>'+$("#channelRegion").text()+'</strong>');
			demoHtml.push('<span></span></div>');
			demoHtml.push('<div class="jquery_tree treebox_left ztree" id="treeDemo" style="overflow: auto;background:#FCFCFC;min-height:400px;max-height:700px;height:auto">');
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
				var param = $("#selMode").serialize();
				BINOLMOPMC01.getNodes(obj,param);
			});
			$("#selMode").trigger("change");
			
			$("#position_left_0").bind("keydown",function(event){
				if(event.keyCode==13){
					BINOLMOPMC01.locationPosition(this,"keydown");
		        }
			});
			
		},
		
		"getNodes" : function(obj,param) {
			var that  = this;
			var url = $(obj).attr("href");
			var selMode = $("#selMode").val();
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
			param += "&privilegeFlag=1&csrftoken=" + getTokenVal();
			var callback = function(msg){
				if(msg.indexOf('id="actionResultDiv"') > -1){
					removeDialog("#region");
				}else{
					if(selMode == '1' || selMode == '3'){
						$('#channelRegionDiv').find("span").html("");
						$("#channelRegionDiv").addClass("hide");
						var nodes = window.JSON2.parse(msg);
						that.loadTree(nodes);
					} else if(selMode == '2') {
						// 渠道树
						that.loadChannelRegion(obj, msg);
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
		 * 加载渠道柜台树
		 */
		"loadChannelRegion" : function(obj,msg) {
			var that  = this;
			var currentMenuGrpID = $(obj).parent().find(":input[name=currentMenuGrpID]").val();
			var channelRegionId = $("#channelRegionId").val();
			// 取得大区信息LIST
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
					var channelUrl = $("#getPublishChannel_url").attr("href");
					var channelParam = "privilegeFlag=1&currentMenuGrpID="
									+ currentMenuGrpID + "&"
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
		 * 加载树
		 */
		"loadTree" : function(nodes) {
			$("#position_left_0").val("");
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
			BINOLMOPMC01_tree = null;
			BINOLMOPMC01_tree = $.fn.zTree.init($("#treeDemo"), treeSetting, nodes);
			// 隐藏进度条
			$("#region").find("#dataTable_processing").addClass("hide");
		},
		
		"locationPosition" : function(obj,flag) {
			if(typeof flag == "undefined"){
				// 点击定位按钮
				var $input = $(obj).prev();
			}else{
				// 直接ENTER键
				var $input = $(obj);
			}
			var inputNode = BINOLMOPMC01_tree.getNodeByParam("name",$input.val());
			BINOLMOPMC01_tree.expandNode(inputNode,true,false);
			BINOLMOPMC01_tree.selectNode(inputNode);
		}
		
};

var BINOLMOPMC01 =  new BINOLMOPMC01();

var BINOLMOPMC01_tree;

$(document).ready(function() {
	// 表单验证配置
    cherryValidate({
        formId: 'mainForm',
        rules: {
        	startDate: {dateValid:true},    // 开始日期
        	endDate: {dateValid:true}   // 结束日期
    	}
    });
    
	BINOLMOPMC01.search();
})
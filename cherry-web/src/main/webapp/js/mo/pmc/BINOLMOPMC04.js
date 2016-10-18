var BINOLMOPMC04_global = function () {
    
};

BINOLMOPMC04_global.prototype = {
		
		"tree" : null,
		
		/**
		 * 读取树的数据
		 * @param id:
		 * 			读取树时需要显示的节点id,
		 * 			若无此参数则不显示(目前只有在新建菜单后的刷新用到)
		 * 
		 */
		"posMenu":function(id){
			var that = this;			
			that.clearActionMsg();
			var url = $("#searchUrl").attr("href");
			var param = $("#mainForm").serialize();
			var callback = function(msg){
				if(msg=="null"){
					$("#createMenuBrand").show();
					$("#addPosMenu").hide();
					$("#menuBrandTree").html("");
				}else{
					$("#createMenuBrand").hide();
					$("#addPosMenu").show();
					var nodesObj = eval("(" + msg + ")");
					that.loadTree(nodesObj,id);
				}
			};
			cherryAjaxRequest( {
				url : url,
				param : param,
				callback : callback
			});
			
		},
		
		/**
		 * 创建树
		 */
		"createMenu" : function(){
			var that = this;			
			var url = $("#create_url").attr("href");
			var param = $("#mainForm").serialize();
			var callback = function(msg){
				if(msg=="null"){
					$("#createMenuBrand").show();
					$("#addPosMenu").hide();
				}else{
					//调用加载树事件
					$("#createMenuBrand").hide();
					$("#addPosMenu").show();
					var nodesObj = eval("(" + msg + ")");
					that.loadTree(nodesObj);
				}
			};
			cherryAjaxRequest( {
				url : url,
				param : param,
				callback : callback
			});
		},
		
		/**
		 * 弹出新增菜单页面
		 * @param url:页面请求URL
		 */
		"popAddMenu" : function(url) {
			var param = $("#mainForm").serialize();
			url = url + '?' + param;
			popup(url);
		},
		
		/**
		 * 树定位
		 */
		"locateMenu" : function(obj,flag) {
			var that = this;
			if(typeof flag == "undefined"){
				var $input = $(obj).prev();
			}else{
				var $input = $(obj);
			}
			// 精确匹配;getNodesByParamFuzzy模糊匹配
			var inputNode = this.tree.getNodeByParam("name",$input.val());
			this.tree.expandNode(inputNode,true,false);
			this.tree.selectNode(inputNode);
			// 显示出定位的树节点的详细信息
			// 清理处理信息
			that.clearActionMsg();
			// 加载点击菜单详细
			that.menuDetailInit(inputNode.posMenuBrandID);
		},
		
		/**
		 * 加载树
		 * @param nodesObj:树节点数据
		 * @param id:加载树时初始需要显示的节点
		 */
		"loadTree":function(nodesObj,id){
			$("#locationPosition").val("");
			var that = this;
			// 无效的菜单以灰色显示
			var setFontCss = function(treeId, treeNode) {
				return treeNode.validFlag == '0' ? {color:"#CCCCCC"} : {};
			};
			var treeSetting = {
				checkable : true,
				showLine : true,
				fontCss : setFontCss,
				treeNodeKey : "id",               //在isSimpleData格式下，当前节点id属性  
			    treeNodeParentKey : "pId", 		  //在isSimpleData格式下，当前节点的父节点id属性  
				checkType : {
					"Y" : "",//勾选不会影响父、子节点
					"N" : ""//取消勾选不会影响父、子节点
				},//绑定点击树节点事件 显示节点数据
				
				callback: {
					click : function(event, treeId, treeNode){
						// 清理处理信息
						that.clearActionMsg();
						// 加载点击菜单详细
						that.menuDetailInit(treeNode.posMenuBrandID);
						return true;
					},
					
					//绑定点击选择框事件
					change : function(event, treeId, treeNode) {
						// 清除处理信息
						BINOLMOPMC04.clearActionMsg();
						var url = $("#editMenuShow_url").attr("href");
						/**
						 * 更新品牌菜单数据时终端specials的当前菜单的所有特殊配置清空，
						 * 统一以brand菜单为准,故更改菜单状态时需再加入菜单编号条件
						 * */
						// 品牌菜单id为查询条件
						var param = 'posMenuBrandID=' + treeNode.posMenuBrandID
							+ '&menuStatus=' + treeNode.menuStatus 
							+ '&posMenuID=' + treeNode.id
							+ '&menuCode=' + treeNode.menuCode;
						param += (param ? "&" : "") + "csrftoken=" + getTokenVal();
						//重新加载树
						var callback = function(msg){
							if(msg=="null"){
								$("#createMenuBrand").show();
								$("#addPosMenu").hide();
							}else{
								if(treeNode.menuStatus=="SHOW"){
									treeNode.menuStatus="HIDE";
								}else{
									treeNode.menuStatus="SHOW";
								}
							}
						};
						
						var dialogSetting = {
								dialogInit: "#dialogInit",
								text: $("#editShowMessage").html(),
								width: 	500,
								height: 300,
								title: 	$("#editShowTitle").text(),
								confirm: $("#dialogConfirm").text(),
								cancel: $("#dialogCancel").text(),
								confirmEvent: function(){BINOLMOPMC04.editShowHandle(url, param, callback);},
								cancelEvent: function(){BINOLMOPMC04.undoChange(treeNode,that.tree);}
							};
						openDialog(dialogSetting);
					}

				}
			};
			that.tree = null;
			that.tree = $("#menuBrandTree").zTree(treeSetting, nodesObj);
			var selectNodes = that.tree.getNodesByParam("posMenuBrandID" ,id);
			if(selectNodes.length > 0){
				that.tree.selectNode(selectNodes[0]);
				// 显示详细信息
				that.menuDetailInit(id);
			}
			
		},
		
		/**
		 * 撤消更改菜单显示状态
		 */
		"undoChange" : function(treeNode,treeObj) {
			removeDialog("#dialogInit");
			// 恢复原来的选中状态
			if(treeNode.menuStatus=="SHOW"){
				treeNode.checked="true";
			} else if(treeNode.menuStatus=="HIDE"){
				treeNode.checked="";
			}
			treeObj.updateNode(treeNode);
		},
		
		/**
		 * 更新菜单显示/隐藏状态处理
		 */
		"editShowHandle" : function(url,param,handleCallback) {
			var callback = function(msg) {
				$("#dialogInit").html(msg);
				if($("#errorMessageDiv").length > 0) {
					$("#errorMessageDiv").find("span").html($("#errorMessageDiv").next().text());
					$("#dialogInit").dialog( "option", {
						buttons: [{
							text: $("#dialogClose").text(),
						    click: function(){removeDialog("#dialogInit");}
						}]
					});
				} else {
					removeDialog("#dialogInit");
					if(typeof(handleCallback) == "function") {
						handleCallback();
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
		 * 选定指定树节点并显示其详细信息
		 * @param id : posMenuBrandID(品牌菜单管理ID)
		 */
		"selectNode" : function (id) {
			var that = this;
			var treeObj = that.tree;
			// 精确查找指定id的树节点展开
			var selectNodes = treeObj.getNodesByParam("posMenuBrandID" ,id);
			if(selectNodes.length > 0){
				treeObj.selectNode(selectNodes[0]);
				// 显示详细信息
				that.menuDetailInit(id);
			}
		},
		
		/**
		 * 初始化菜单详细
		 * @param value : 节点的品牌菜单ID
		 */
		"menuDetailInit" : function(value){
			if(value){
				var param = "posMenuBrandID=" + value;
				param += (param ? "&" : "") + "csrftoken=" + getTokenVal();
				var callback = function(msg) {
					$("#posMenuDetail").html(msg);
				};
				cherryAjaxRequest({
					url : $("#menuDetailInitUrl").attr("href"),
					param : param,
					callback : callback
				});
			}
		},
		
		/**
		 * 显示菜单链接地址详细
		 */
		"showMenuLinkInfo":function(obj){
			var $this = $(obj);
			var dialogSetting = {
				dialogInit: "#dialogInit",
				width: 	430,
				height: 300,
				title: 	$("#showMenuLinkTitle").text(),
				confirm: $("#dialogClose").text(),
				confirmEvent: function(){
					removeDialog("#dialogInit");
				}
			};
			openDialog(dialogSetting);
			$("#dialogContent").html($this.attr("rel"));
			$("#dialogInit").html($("#menuLinkDetail").html());
		},
		
		"popEditConfigDialog" : function(obj) {
			var that = this;
			var url = $("#updateConfigInitUrl").attr("href");
			that.clearActionMsg();
			if($("#editConfigDialog").length == 0) {
	    		$("body").append('<div style="display:none" id="editConfigDialog"></div>');
	    	} else {
	    		$("#editConfigDialog").empty();
	    	}

			var initCallback = function(msg) {
				var dialogSetting = {
	    			dialogInit: "#editConfigDialog",
	    			text: msg,
	    			width: 	400,
	    			height: 250,
	    			title: 	$("#modifyConfigTitle").html(),
	    			confirm: $("#dialogConfirm").html(),
	    			cancel: $("#dialogCancel").html(), 
	    			confirmEvent: function(){
	    				var param = $('#editConfigForm').serialize();
						param += "&csrftoken=" + getTokenVal();
			    		// 保存编辑
			    		that.updConfigHandle($("#updateConfigValueUrl").attr("href"),param);
	    			},
	    			cancelEvent: function(){
	    				// 关闭Dialog
	    				removeDialog("#editConfigDialog");
	    			}
				};
				openDialog(dialogSetting);
			}
			
			cherryAjaxRequest({
	    		url: url,
	    		param: "csrftoken=" + getTokenVal(),
	    		callback: initCallback
	    	});
		},
		
		"updConfigHandle" : function(url,param) {
			if(!$('#editConfigForm').valid()) {
				return false;
			}
			var callback = function(msg) {
				$("#editConfigDialog").html(msg);
				if($("#errorMessageDiv").length > 0) {
					$("#errorMessageDiv").find("span").html($("#errorMessageDiv").next().text());
					$("#dialogInit").dialog( "option", {
						buttons: [{
							text: $("#dialogClose").text(),
						    click: function(){removeDialog("#editConfigDialog");}
						}]
					});
				} else {
					removeDialog("#editConfigDialog");
				}
			};
			cherryAjaxRequest({
				url: url,
				param: param,
				callback: callback
			});
		},
		
		/**
		 * 编辑菜单
		 */
		"popEditMenu" : function(obj) {
			var that = this;
			var url = $("#editInit_url").attr("href");
			that.clearActionMsg();
			if($("#editPosMenuDialog").length == 0) {
	    		$("body").append('<div style="display:none" id="editPosMenuDialog"></div>');
	    	} else {
	    		$("#editPosMenuDialog").empty();
	    	}

			var initCallback = function(msg) {
				var dialogSetting = {
	    			dialogInit: "#editPosMenuDialog",
	    			text: msg,
	    			width: 	450,
	    			height: 400,
	    			title: 	$("#editDialogTitle").html(),
	    			confirm: $("#dialogConfirm").html(),
	    			cancel: $("#dialogCancel").html(), 
	    			confirmEvent: function(){
	    				var param = $('#editMenuForm').serialize();
						param += "&csrftoken=" + getTokenVal();
			    		// 保存编辑
			    		that.saveEdit($("#saveEditUrl").attr("href"),param);
	    			},
	    			cancelEvent: function(){
	    				// 关闭Dialog
	    				removeDialog("#editPosMenuDialog");
	    			}
				};
				openDialog(dialogSetting);
			}
			
			cherryAjaxRequest({
	    		url: url,
	    		param: "csrftoken=" + getTokenVal(),
	    		callback: initCallback
	    	});
		    
		},
		
		/**
		 * 保存对菜单信息的编辑
		 */
		"saveEdit" : function(url,param) {
			var that = this;
			if(!$('#editMenuForm').valid()) {
				return false;
			}
			// 用于刷新树节点
			var posMenuBrandID = $("#posMenuBrandID").val();
			var brandMenuName = '('+$("#menuCode").val()+')'+$("#brandMenuNameCN").val();
			var callback = function(msg){
				// 关闭Dialog
				removeDialog("#editPosMenuDialog");
				// 刷新详细页面
				that.menuDetailInit(posMenuBrandID);
				// 刷新树节点名称[不再刷新整棵树]
				var selectNode = that.tree.getNodesByParam("posMenuBrandID" ,posMenuBrandID);
				selectNode[0].name = brandMenuName;
				that.tree.updateNode(selectNode[0]);
			};
			
			cherryAjaxRequest({
				url: url,
				param: param,
				formId: '#editMenuForm',
				callback: callback
			});	
			
		},
		
		/**
		 * 启用、停用菜单【暂时废弃】
		 */
		"editValidFlag" : function(flag,url) {
			var that = this;
			// 清除处理信息
			that.clearActionMsg();
			var param = "";
			var posMenuBrandID = $('#posMenuBrandID').val();
			if(flag == 'enable') {
				param = 'validFlag=1';
				var title = $('#enableTitle').text();
				var text = $('#enableMessage').html();
			} else {
				param = 'validFlag=0';
				var title = $('#disableTitle').text();
				var text = $('#disableMessage').html();
			}
			param += (param ? "&" : "") + "csrftoken=" + getTokenVal();
			
			var callback = function() {
				// 停用、启用成功后刷新当前页面及树节点
				if($('#treeLayoutDiv').length > 0) {
					// 清理处理信息
					that.clearActionMsg();
					that.menuDetailInit(posMenuBrandID);
					// 取得当前节点数据集合
					var selectNode = that.tree.getNodesByParam("posMenuBrandID" ,posMenuBrandID);
					if(flag == 'enable') {
						selectNode[0].validFlag = "1";
					} else {
						selectNode[0].validFlag = "0";
					}
					// 刷新树
					that.tree.refresh();
					that.tree.selectNode(selectNode[0]);
				}
			};
			var dialogSetting = {
					dialogInit: "#dialogInit",
					text: text,
					width: 	500,
					height: 300,
					title: 	title,
					confirm: $("#dialogConfirm").text(),
					cancel: $("#dialogCancel").text(),
					confirmEvent: function(){that.disOrEnableHandle(url, param, callback);},
					cancelEvent: function(){removeDialog("#dialogInit");}
				};
			openDialog(dialogSetting);
		},
		
		/**
		 * 停用、启用处理请求
		 */
		"disOrEnableHandle" : function(url,param,handleCallback) {
			var callback = function(msg) {
				$("#dialogInit").html(msg);
				if($("#errorMessageDiv").length > 0) {
					$("#errorMessageDiv").find("span").html($("#errorMessageDiv").next().text());
					$("#dialogInit").dialog( "option", {
						buttons: [{
							text: $("#dialogClose").text(),
						    click: function(){removeDialog("#dialogInit");}
						}]
					});
				} else {
					removeDialog("#dialogInit");
					if(typeof(handleCallback) == "function") {
						handleCallback();
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
		 * 清理处理信息
		 */
		"clearActionMsg" : function() {
			// 清理处理信息
			$("#errorDiv2 #errorSpan2").html("");
			$("#errorDiv2").hide();
			$("#errorMessage").empty();
			$("#actionResultDisplay").empty();
		},
		
		/**
		 * 在菜单的text框上绑定下拉框选项
		 * @param Object:options
		 * 				String:options.elementId 元素ID,要绑定下拉框的元素ID
		 * 				String:options.showNum 参数可选，下拉最大的显示个数，默认是50
		 * 				String:options.selected 参数可选，希望被选中的部分，"name":表示要选中菜单名称；默认是"name"
		 * @param options
		 */
		"menuBinding" : function(options){
			var csrftoken = $('#csrftoken').serialize();
			if(!csrftoken) {
				csrftoken = $('#csrftoken',window.opener.document).serialize();
			}
			var strPath = window.document.location.pathname;
			var postPath = strPath.substring(0, strPath.substr(1).indexOf('/') + 1);
			var url = postPath+"/mo/BINOLMOPMC04_getPosMenuInfo.action"+"?"+csrftoken;
			$('#'+options.elementId).autocompleteCherry(url,{
				extraParams:{
					menuInfoStr: function() { return $('#'+options.elementId).val();},
					//默认是最多显示50条
					number:options.showNum? options.showNum : 50,
					machineType : function() { return $('#machineType').val();}
				},
				loadingClass: "ac_loading",
				minChars:1,
			    matchContains:1,
			    matchContains: true,
			    scroll: false,
			    cacheLength: 0,
			    width:230,
			    max:options.showNum ? options.showNum : 50,
				formatItem: function(row, i, max) {
					return escapeHTMLHandle(row[0]);
				}
			});
		}
};

var BINOLMOPMC04 = new BINOLMOPMC04_global();

/**
 * 新增菜单后刷新父页面
 * 注：在修改此方法时须考虑到对BINOLMOPMC04_3.js的影响
 */
function freshTree() {
	// 父页面处于显示状态的节点id
	var posMenuBrandID = $("#posMenuBrandID").val();
	// 重新同步树信息
	BINOLMOPMC04.posMenu(posMenuBrandID);
}

/**
 * 全局变量定义
 */
//var BINOLMOPMC04_global = {};

// 刷新区分
//BINOLMOPMC04_global.refresh = false;

$(function(){
	
	// 画面布局初期化处理
	BINOLMOPMC04.posMenu();
	
	counterBinding({elementId:"counterCodeName",showNum:20,selected:"name"});
	
	BINOLMOPMC04.menuBinding({
		elementId:"locationPosition",
		showNum:20
	});
	
	$("#locationPosition").bind("keydown",function(event){
		if(event.keyCode==13){
			BINOLMOPMC04.locateMenu(this,"keydown");
        }
	});

});
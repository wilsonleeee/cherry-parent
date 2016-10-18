var BINOLMOPMC02 = function(){
	
};

BINOLMOPMC02.prototype = {
		
		"popMenuGrpConfigTree" : function() {
			var that = this;
			var url = $("#getMenuTreeUrl").attr("href");
			var param = $("#treeForm").serialize();
			param += (param ? "&" : "") + "csrftoken=" + getTokenVal();
			var callback = function(msg) {
				var nodesObj = eval("(" + msg + ")");
				that.loadTree(nodesObj);
				$("#dataTable_processing").addClass("hide");
			};
			cherryAjaxRequest( {
				url : url,
				param : param,
				callback : callback,
				coverId: "#div_main"
			});
		},
		
		"loadTree" : function(nodesObj) {
			var that = this;
			// 特殊配置的菜单以红色显示
			var setFontCss = function(treeId, treeNode) {
				return treeNode.diffentFlag == '1' ? {color:"red"} : {};
			};
			var treeSetting = {
					checkable : true,
					showLine : true,
					fontCss : setFontCss,
					open : true,
					treeNodeKey : "id",               // 在isSimpleData格式下，当前节点id属性
				    treeNodeParentKey : "pId", 		  // 在isSimpleData格式下，当前节点的父节点id属性
					checkType : {
						"Y" : "",
						"N" : ""
					},
					
					callback : {
						change : function(event, treeId, treeNode) {
							treeNode.diffentFlag = treeNode.diffentFlag == '1' ? '0' : '1';
							// 因为改变字体样式需要刷新树，无法直接取得此次配置改动的菜单，故引入changeFlag参数来标记
							treeNode.changeFlag = treeNode.changeFlag == '0' ? '1' : '0';
							BINOLMOPMC02_tree.updateNode(treeNode);
							// 树更新才能调用加载样式的方法
							BINOLMOPMC02_tree.refresh();
						}
					}
			};
			BINOLMOPMC02_tree = null;
			BINOLMOPMC02_tree = $("#treeDemo").zTree(treeSetting, nodesObj);
		},
		
		"locationPosition" : function(obj,flag) {
			if(typeof flag == "undefined"){
				// 点击定位按钮
				var $input = $(obj).prev();
			}else{
				// 直接ENTER键
				var $input = $(obj);
			}
			var inputNode = BINOLMOPMC02_tree.getNodeByParam("name",$input.val());
			BINOLMOPMC02_tree.expandNode(inputNode,true,false);
			BINOLMOPMC02_tree.selectNode(inputNode);
		},
		
		"save" : function() {
			// 清空消息
			$('#actionResultDisplay').html('');
			// 相对于品牌菜单有差分的节点
			var diffentNodesArray = new Array();
			// 查看所有节点的改变状态
			var changeNodes = BINOLMOPMC02_tree.getNodesByParam("changeFlag", "1", null);
			for(var i=0; i<changeNodes.length; i++){
				var nodeObj = {
						posMenuID : changeNodes[i].id,
						menuStatus : changeNodes[i].menuStatus == 'SHOW' ? 'HIDE' : 'SHOW'
					};
				diffentNodesArray.push(nodeObj);
			}		
			var url = $("#saveMenuTreeUrl").attr("href");
			var param = $("#treeForm").serialize() + 
				"&diffentNodesArray="+JSON2.stringify(diffentNodesArray) + "&csrftoken=" + getTokenVal();
			// 显示进度条
			$("#dataTable_processing").removeClass("hide");
			var callback = function(msg) {
				// 显示进度条
				$("#dataTable_processing").addClass("hide");
				if(window.opener.oTableArr[1] != null)window.opener.oTableArr[1].fnDraw();
			};
			cherryAjaxRequest( {
				url : url,
				param : param,
				callback : callback,
				coverId: "#div_main"
			});
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
			var url = postPath+"/mo/BINOLMOPMC02_getPosMenuInfo.action"+"?"+csrftoken;
			$('#'+options.elementId).autocompleteCherry(url,{
				extraParams:{
					menuInfoStr: function() { return $('#'+options.elementId).val();},
					//默认是最多显示50条
					number:options.showNum? options.showNum : 50
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

var BINOLMOPMC02 = new BINOLMOPMC02();

var BINOLMOPMC02_tree;

window.onbeforeunload = function(){
    if (window.opener) {
        window.opener.unlockParentWindow();
    }
};

$(function(){
	if (window.opener) {
        window.opener.lockParentWindow();
    }
	// 画面布局初期化处理
	BINOLMOPMC02.popMenuGrpConfigTree();
	
	BINOLMOPMC02.menuBinding({
		elementId:"position_left_0",
		showNum:20
	});
	
	$("#position_left_0").bind("keydown",function(event){
		if(event.keyCode==13){
			BINOLMOPMC02.locationPosition(this,"keydown");
        }
	});
});
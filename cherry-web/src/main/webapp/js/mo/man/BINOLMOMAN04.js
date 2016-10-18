/**
 * @author zgl
 */

var leftRootNodes;
var rightAboveRootNodes;
var treeNodes_right_underside;
var tree_left;
var tree_right_above;
var tree_right_underside;

$(document).ready(function() {
	$('#expandCondition').click(function(){
		if($(this).children('.ui-icon').is('.ui-icon-triangle-1-n')) {
			$(this).children('.ui-icon').removeClass('ui-icon-triangle-1-n').addClass('ui-icon-triangle-1-s');
			$('#moreCondition').show('slow');
		} else {
			$(this).children('.ui-icon').removeClass('ui-icon-triangle-1-s').addClass('ui-icon-triangle-1-n');
			$('#moreCondition').hide('slow');
		}
	});
	$("#treeDemo_right_underside").hide();
	$("#leftToRight").bind("click",function(){
		dispaly_confirm1();
	});
	$("#rightToLeft").bind("click",function(){
		dispaly_confirm2();
	});
	$("#right_above_tree").css("background", "#E8E8C8");
	$("#right_underside_tree").css("background", "#FFFFFF");
	changeMachineType($("#getTreeRootNodesByAjax").html());
});
var hide = true;
/**
 * 右侧树的显示与隐藏控制
 * 
 * 
 */
function showDiv() {
	if (hide == true) {
		$("#right_underside_tree").css("background", "#E8E8C8");
		$("#right_above_tree").css("background", "#FFFFFF");
		$("#treeDemo_right_above").hide(300);
		$("#treeDemo_right_underside").show(300);
		hide = false;
	} else {
		$("#right_above_tree").css("background", "#E8E8C8");
		$("#right_underside_tree").css("background", "#FFFFFF");
		$("#treeDemo_right_underside").hide(300);
		$("#treeDemo_right_above").show(300);
		hide = true;
	}
}

/**
 * 重新加载
 * @param url
 * @param cleanMessageDiv
 */
function changeMachineType(url,cleanMessageDiv) {
	
	var callback = function(msg) {
		var rootNodes = msg.split("*"); 
		
		/**
		 * 将处于连线状态的柜台显示为绿色
		 * @param treeId
		 * @param treeNode
		 * @returns
		 */
		var setFontCss = function(treeId, treeNode) {
			return treeNode.connectFlag == '1' ? {color:"#009900"} : {};
		};
		
		// 左树加载
		var treeNodes_left = eval('(' + rootNodes[0] + ')');
		/**
		 * 左树设置（待升级树）
		 */
		var left_zTreeSetting = {
			checkable : true,
			showLine : true,
			fontCss : setFontCss,
			expandSpeed : "",
			async : true,
			asyncUrl : left_getAsyncUrl,
			asyncParamOther : [ "csrftoken", $('#csrftoken').val() ],
			asyncParam : [ "regionType", "machineType", "id", "brandInfoId","checked" ],
			callback : {
				asyncSuccess : treeRefresh
			}
		};
		tree_left = $("#treeDemo_left")
				.zTree(left_zTreeSetting, treeNodes_left);
		
		// 右上树加载
		var treeNodes_right_above = eval('(' + rootNodes[1] + ')');
		/**
		 * 右上树设置
		 */
		var right_above_zTreeSetting = {
			checkable : true,
			showLine : true,
			fontCss : setFontCss,
			expandSpeed : "",
			async : true,
			asyncUrl : right_getAsyncUrl,
			asyncParamOther : [ "csrftoken", $('#csrftoken').val() ],
			asyncParam : [ "regionType", "machineType", "updateStatus", "id",
					"brandInfoId","checked" ],
			callback : {
				asyncSuccess : treeRefresh
			}
		};
		tree_right_above = $("#treeDemo_right_above").zTree(right_above_zTreeSetting,
				treeNodes_right_above);
		
		// 右下树加载
		var treeNodes_right_underside = eval('(' + rootNodes[2] + ')');
		/**
		 * 右下树设置
		 */
		var right_underside_zTreeSetting = {
			checkable : true,
			showLine : true,
			fontCss : setFontCss,
			expandSpeed : "",
			async : true,
			asyncUrl : right_getAsyncUrl,
			asyncParamOther : [ "csrftoken", $('#csrftoken').val() ],
			asyncParam : [ "regionType", "machineType", "updateStatus", "id",
					"brandInfoId","checked" ],
			callback : {
				asyncSuccess : treeRefresh
			}
		};
		tree_right_underside = $("#treeDemo_right_underside").zTree(
				right_underside_zTreeSetting, treeNodes_right_underside);
	};
	var param = "brandInfoId=" + $("#brandInfoId").val() + "&machineType="
			+ $("#machineType").val() + "&csrftoken=" + $('#csrftoken').val();
	cherryAjaxRequest( {
		url : url,
		param : param,
		callback : callback
	});
	if(cleanMessageDiv == undefined){
		$("#actionResultDisplay").html("");
	}
}

/**
 * 取得通过ajax获得已经设置了升级的树子节点的URL
 * @param treeNode
 * @returns
 */
function right_getAsyncUrl(treeNode) {
	var url = $("#getTreeNodesByAjax").html();
	return url;
}

/**
 * 获取未升级的柜台的所在区域的下级区域URL
 * @param treeNode
 * @returns
 */
function left_getAsyncUrl(treeNode) {
	var url = $("#getSubRegionNoUpdateStatus").html();
	return url;
}

/**
 * 刷新树
 * @param event
 * @param treeId
 * @param msg
 */
function treeRefresh(event, treeId, msg) {
	if (treeId == "treeDemo_left") {
		tree_left.refresh();
	} else if (treeId == "treeDemo_right_above") {
		tree_right_above.refresh();
	} else {
		tree_right_underside.refresh();
	}
}

/**
 * 将节点从左边树移动到右上的树
 * 
 * 
 */
function seletToOfficial() {
	$("#dataTable_processing").show();
	var updateStatus = "2";
	fromNoneToUpdateStatus(tree_left, updateStatus);
}
/**
 * 将节点从右上的树移动到左边的树
 * 
 * 
 */
function officialToSelect() {
	$("#dataTable_processing").show();
	var newUpdateStatus = "1";
	fromUpdateStatusToNone(tree_right_above);
}
/**
 * 将节点从左边树移动到右下的树
 * 
 * 
 */
function selectToTest() {
	$("#dataTable_processing").show();
	var updateStatus = "3";
	fromNoneToUpdateStatus(tree_left, updateStatus);
}
/**
 * 将节点从右下的树移动到左边的树
 * 
 * 
 */
function testToSelect() {
	$("#dataTable_processing").show();
	var newUpdateStatus = "1";
	fromUpdateStatusToNone(tree_right_underside);
}

/**
 * 深层递归克隆节点(仅id,name数据)
 * 
 * @param node
 * @return
 */
function cloneNodeDeep(jsonObj) {

	var buf;
	if (jsonObj instanceof Array) {
		buf = [];
		var i = jsonObj.length;
		while (i--) {
			buf[i] = cloneNodeDeep(jsonObj[i]);
		}
		return buf;
	} else if (jsonObj instanceof Object) {
		buf = {};
		buf.id = jsonObj.id;
		buf.name = jsonObj.name;
		buf.isParent = jsonObj.isParent;
		buf.machineType = jsonObj.machineType;
		buf.regionType = jsonObj.regionType;
		buf.updateStatus = jsonObj.updateStatus;
		buf.nodes = cloneNodeDeep(jsonObj.nodes);
		return buf;
	} else {
		return jsonObj;
	}
}

/**
 * 根据id查询节点
 * 
 * @param nodes
 * @param id
 * @return
 */
function searchNodeById(nodes, id) {
	if (!nodes || !id)
		return null;
	// 遍历所有节点
	for ( var i = 0; i < nodes.length; i++) {
		// 定义一个节点
		var node = nodes[i];
		if (node.id === id) {
			return node;
		}
		var tmp = searchNodeById(node.nodes, id);
		if (tmp)
			return tmp;
	}
	return null;
}

function cloneNode(node, childNodes) {
	var newNode = {};
	if (childNodes != null) {
		// 取得父节点
		var parentNode = node.parentNode;
		newNode.id = parentNode.id;
		newNode.name = parentNode.name;
		// 设置子节点
		newNode.nodes = childNodes;
	} else {
		newNode.id = node.id;
		newNode.name = node.name;
	}
	return newNode;
}
/**
 * 复制节点
 * 
 * @param node
 *            需要复制的节点
 * @return newNode 返回复制的节点
 * 
 */
function copyNode(node) {
	var newNode = {};
	// 判断是否有子节点
	if (node.isParent != false) {
		newNode.id = node.id;
		newNode.name = node.name;
		newNode.isParent = node.isParent;
		newNode.updateStatus = node.updateStatus;
		// 如果要复制的节点node有子节点但是还未展开
		if (node.nodes == null) {
			newNode.machineType = node.machineType;
			newNode.regionType = node.regionType;
		}
	} else {
		newNode.id = node.id;
		newNode.name = node.name;
		newNode.updateStatus = node.updateStatus;
	}
	return newNode;
}
/**
 * 添加选中的节点
 * 
 * @param targetTree
 *            目标树
 * @param checkNodes
 *            要添加的节点对象
 * @return targetTree 返回添加过节点的目标树
 * 
 */
function addCheckedNodes(targetTree, checkNodes) {
	var length = checkNodes.length;
	// 添加节点
	for ( var i = 0; i < length; i++) {
		var targetNodes = targetTree.getNodes();
		var nodeParent = checkNodes[i].parentNode;
		if (nodeParent == null) {
			var sameNode = searchNodeById(targetNodes, checkNodes[i].id);
			if (sameNode == null) {
				var newNode = copyNode(checkNodes[i]);
				targetTree.addNodes(null, [ newNode ]);
			}
		} else {
			var sameParentNode = searchNodeById(targetNodes, nodeParent.id);
			if (sameParentNode != null) {
				targetTree = getSubNodesByAjax(targetTree, sameParentNode);
				// targetTree.expandNode(sameParentNode,true,false);
				// targetTree.refresh();
				var sameNode = searchNodeById(sameParentNode.nodes,
						checkNodes[i].id);
				if (sameNode == null) {
					var newNode = copyNode(checkNodes[i]);
					targetTree.addNodes(sameParentNode, [ newNode ]);
					targetTree.expandNode(sameParentNode, true, false);
					// targetTree.refresh();
				}
			}
		}
	}
	return targetTree;
}

/**
 * 比较两棵树,然后删除其中一棵上在另外一棵树上已经存在的节点
 * 
 * @param tree1
 *            要删除节点的树
 * @param tree2
 *            作为比较的树
 * @return tree1 返回删除后的树
 * 
 */
function removecheckedNodes(tree1, tree2) {
	var length = tree2.transformToArray(tree2.getNodes()).length;
	var targetTreeNodes = tree2.transformToArray(tree2.getNodes());
	for ( var i = length - 1; i >= 0; i--) {
		var sourceTreeNodes = tree1.getNodes();
		var sameNode = searchNodeById(sourceTreeNodes, targetTreeNodes[i].id);
		if (sameNode != null
				&& (sameNode.nodes == null || sameNode.nodes.length == 0)) {
			tree1.removeNode(sameNode);
			// sameNode.parentNode.isParent = true;
			// sourceTree.refresh();
		}
	}
	return tree1;
}

/**
 * 移动选中的节点
 * 
 * 
 */
function moveCheckedNodes(sourceTree, targetTree, checkNodes) {
	// 向目标树中添加选中的节点
	var new_target_tree = addCheckedNodes(targetTree, checkNodes);
	// 删除源树节点
	var new_source_tree = removecheckedNodes(sourceTree, new_target_tree);
	// 取消源树的节点选中状态
	new_source_tree.checkAllNodes(false);
	// 刷新树节点
	new_source_tree.refresh();
	new_target_tree.refresh();
}

/**
 * 通过AJAX获得子节点
 * 
 * 
 * 
 */
function getSubNodesByAjax(tree, node) {
	var url = $("#getTreeNodesByAjax").html();
	var param = "id=" + node.id + "&machineType=" + node.machineType
			+ "&regionType=" + node.regionType + "&updateStatus="
			+ node.updateStatus + "&csrftoken=" + $('#csrftoken').val();
	callback = function(msg) {
		var nodes = eval('(' + msg + ')');
		tree.addNodes(node, nodes);
		tree.expandNode(node, true, false);
	};
	$.ajax( {
		type : "POST",
		url : url,
		data : param,
		success : function(msg) {
			var nodes = eval('(' + msg + ')');
			tree.addNodes(node, nodes);
			tree.expandNode(node, true, false);
		}
	});
	return tree;
}

/**
 * 更新柜台升级状态
 * 
 * @param newUpdateStatus
 *            新的柜台状态
 * @param tree
 *            要更新柜台升级状态的树
 * 
 * 
 */
function updateCounterUpdateStatus(tree, newUpdateStatus) {
	var checkNodes = tree.getCheckedNodes();
	var length = checkNodes.length;
	var checkNodesArray = new Array();
	for ( var i = 0; i < length; i++) {
		if (checkNodes[i].isParent == true && checkNodes[i].nodes == null) {
			var id = checkNodes[i].id.split("_");
			var obj = {
				id : id[1],
				updateStatus : checkNodes[i].updateStatus,
				machineType : checkNodes[i].machineType,
				newUpdateStatus : newUpdateStatus
			};
			checkNodesArray[checkNodesArray.length] = obj;
		} else if (checkNodes[i].isParent == false
				|| checkNodes[i].isParent == null) {
			var obj = {
				id : checkNodes[i].id,
				machineType : checkNodes[i].machineType,
				newUpdateStatus : newUpdateStatus
			};
			checkNodesArray[checkNodesArray.length] = obj;
		}
	}
	var param = JSON2.stringify(checkNodesArray);
	param = "brandInfoId=" + $("#brandInfoId").val() + "&machineType="
			+ $("#machineType").val() + "&checkNodesArray=" + param
			+ "&csrftoken=" + $('#csrftoken').val();
	var url = $("#updateCounterUpdateStatus").html();
	$.ajax( {
			type : "POST",
			url : url,
			data : param,
			success : function(msg) {
				var rootNodes = msg.split("*");
				var treeNodes_left = eval('(' + rootNodes[0] + ')');
				tree_left = $("#treeDemo_left").zTree(zTreeSetting,
						treeNodes_left);
				var treeNodes_right_above = eval('(' + rootNodes[1] + ')');
				tree_right_above = $("#treeDemo_right_above").zTree(
						zTreeSetting, treeNodes_right_above);
				var treeNodes_right_underside = eval('(' + rootNodes[2] + ')');
				tree_right_underside = $("#treeDemo_right_underside")
						.zTree(zTreeSetting, treeNodes_right_underside);

			}
		});
}

/**
 * 从设置了升级状态装换成为未设置升级状态
 * 
 * 
 * 
 */
function fromUpdateStatusToNone(tree) {
	$("#leftToRight").unbind("click");
	$("#rightToLeft").unbind("click");
	var checkNodes = tree.getCheckedNodes();
	var length = checkNodes.length;
	var checkNodesArray = new Array();
	for ( var i = 0; i < length; i++) {
		if (checkNodes[i].isParent == true && checkNodes[i].nodes == null) {
			var obj = {
				id : checkNodes[i].id,
				updateStatus : checkNodes[i].updateStatus,
				machineType : checkNodes[i].machineType,
				brandInfoId : checkNodes[i].brandInfoId
			};
			checkNodesArray[checkNodesArray.length] = obj;
		} else if (checkNodes[i].isParent == false
				|| checkNodes[i].isParent == null) {
			var obj = {
				id : checkNodes[i].id,
				machineType : checkNodes[i].machineType,
				updateStatus : checkNodes[i].updateStatus,
				brandInfoId : checkNodes[i].brandInfoId
			};
			checkNodesArray[checkNodesArray.length] = obj;
		}
	}
	var param = JSON2.stringify(checkNodesArray);
	param = "brandInfoId=" + $("#brandInfoId").val() + "&machineType="
			+ $("#machineType").val() + "&checkNodesArray=" + param
			+ "&csrftoken=" + $('#csrftoken').val();
	var url = $("#fromUpdateStatusToNone").html();
	var obj={
			url:url,
			param:param,
			callback:function(msg) {
				$("#dataTable_processing").hide();
				changeMachineType($("#getTreeRootNodesByAjax").html(), "a");
				$("#leftToRight").bind("click",function(){
					dispaly_confirm1();
				});
				$("#rightToLeft").bind("click",function(){
					dispaly_confirm2();
				});
			}
	};
	cherryAjaxRequest(obj);
}

/**
 * 柜台升级
 * 
 * 
 */
function fromNoneToUpdateStatus(tree, updateStatus) {
	$("#leftToRight").unbind("click");
	$("#rightToLeft").unbind("click");
	var checkNodes = tree.getCheckedNodes();
	var length = checkNodes.length;
	var checkNodesArray = new Array();
	for ( var i = 0; i < length; i++) {
		if (checkNodes[i].isParent == true && checkNodes[i].nodes == null) {
			var obj = {
				id : checkNodes[i].id,
				updateStatus : updateStatus
			};
			checkNodesArray[checkNodesArray.length] = obj;
		} else if (checkNodes[i].isParent == false
				|| checkNodes[i].isParent == null) {
			var obj = {
				id : checkNodes[i].id,
				updateStatus : updateStatus
			};
			checkNodesArray[checkNodesArray.length] = obj;
		}
	}
	var param = JSON2.stringify(checkNodesArray);
	param = "brandInfoId=" + $("#brandInfoId").val() + "&machineType="
			+ $("#machineType").val() + "&checkNodesArray=" + param
			+ "&csrftoken=" + $('#csrftoken').val();
	var url = $("#fromNoneToUpdateStatus").html();
	var obj={
			url:url,
			param:param,
			callback:function(msg) {
				$("#dataTable_processing").hide();
				changeMachineType($("#getTreeRootNodesByAjax").html(), "a");
				$("#leftToRight").bind("click",function(){
					dispaly_confirm1();
				});
				$("#rightToLeft").bind("click",function(){
					dispaly_confirm2();
				});
			}
	};
	cherryAjaxRequest(obj);
}
/**
 * 左树节点移到右树
 */
function dispaly_confirm1() {
	var checkNodes = tree_left.getCheckedNodes();
	var length = checkNodes.length;
	if (length > 0) {
		sureMoveConfirm(1);
	} else {
		pleaseSelectConfirm();
	}
}

/**
 * 右树节点移到左树
 */
function dispaly_confirm2() {
	var checkNodes1 = tree_right_above.getCheckedNodes();
	var length1 = checkNodes1.length;
	var checkNodes2 = tree_right_underside.getCheckedNodes();
	var length2 = checkNodes2.length;
	if (length1 > 0 || length2 > 0) {
		sureMoveConfirm(2);
	} else {
		pleaseSelectConfirm();
	}
}

/**
 * 移动下属柜台选择框
 * @param param=1(左边移到右边),param=2(右边移到左边)
 */
function sureMoveConfirm(param) {
	var title = $('#sureTitle').text();
	var text = $("#sureMessage").html();
	var dialogSetting = {
		dialogInit: "#dialogInit",
		text: text,
		width: 	300,
		height: 200,
		title: 	title,
		confirm: $("#dialogConfirm").text(),
		cancel: $("#dialogCancel").text(),
		confirmEvent: function(){
		if(param == 1){
			if($("#treeDemo_right_underside").is(":hidden")){
				seletToOfficial();
			}else if($("#treeDemo_right_above").is(":hidden")){
				selectToTest();
			}else{
				;
			}
		}else if(param == 2){
			if($("#treeDemo_right_underside").is(":hidden")){
				officialToSelect();
			}else if($("#treeDemo_right_above").is(":hidden")){
				testToSelect();
			}else{
				;
			}
		}else{
			;
		};
		removeDialog("#dialogInit");},
		cancelEvent: function(){removeDialog("#dialogInit");}
	};
	openDialog(dialogSetting);
}

/**
 * 请选择要移动的柜台提示框
 */
function pleaseSelectConfirm() {
	var title = $('#tipTitle').text();
	var text = $('#messageWarn').html();
	var dialogSetting = {
		dialogInit: "#dialogInit",
		text: text,
		width: 	300,
		height: 200,
		title: 	title,
		confirm: $("#dialogConfirm").text(),
		confirmEvent: function(){removeDialog("#dialogInit");}
	};
	openDialog(dialogSetting);
}
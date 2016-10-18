// 画面布局全局变量
var plrla02_layout = null;
// 岗位树对象
var positionTree;
// 树节点数据
var treeNodes;

// 树设置
var treeSetting = {
	isSimpleData : false, // false时需要传json数据，=true时需要传array数据
	treeNodeKey : "id", // 在isSimpleData格式下，当前节点id属性
	treeNodeParentKey : "pId", // 在isSimpleData格式下，当前节点的父节点id属性
	showLine : true, // 是否显示节点间的连线
	checkable : false, // 每个节点上是否显示 CheckBox
	showIcon : false, // 是否显示图标
	callback : {
		click : function(event, treeId, treeNode) {
			if (treeNode.pId == -1) {// 品牌节点
				$("#roleAssignInit").empty();
				return;
			}
			var param = "positionCategoryId=" + treeNode.id + "&brandInfoId="
					+ treeNode.pId;
			param += (param ? "&" : "") + "csrftoken=" + getTokenVal();
			roleAssign($("#roleAssignUrl").attr("href"), param);
			return true;
		}
	}
};

$(document).ready(function() {
	// 画面布局初期化处理
	plrla02_layout = $('#treeLayoutDiv').layout({
		spacing_open : 2, // 边框的间隙
		spacing_closed : 5, // 关闭时边框的间隙
		resizerTip : "", // 鼠标移到边框时，提示语
		togglerTip_open : "", // pane打开时，当鼠标移动到边框上按钮上，显示的提示语
		togglerTip_closed : "", // pane关闭时，当鼠标移动到边框上按钮上，显示的提示语
		fxName : "none", // 打开关闭的动画效果
		togglerLength_open : 0, // pane打开时，边框按钮的长度
		togglerLength_closed : 0, // pane关闭时，边框按钮的长度
		west__minSize : 120, // 可改变的最小长度
		west__maxSize : 360
	// 可改变的最大长度
	});

	// 岗位树显示
	loadTree();

});

// 取得岗位树节点信息
function loadTree() {
	var url = $("#positionCtgLoadTreeUrl").attr("href");
	var callback = function(msg) {
		treeNodes = eval("(" + msg + ")");
		positionTree = $("#positionCtgTree").zTree(treeSetting, treeNodes);
	};
	cherryAjaxRequest({
		url : url,
		param : null,
		callback : callback
	});
}

// 查询岗位类别角色一览
function roleAssign(url, param) {
	var callback = function(msg) {
		$("#roleAssignInit").html(msg);
		if (plrla02_layout != null) {
			var height = $("#roleAssignInit").height();
			var _height = $("#roleAssignInit").parent().height();
			if (height > _height) {
				var thisHeight = $("#treeLayoutDiv").height();
				$("#treeLayoutDiv").css({
					height : (thisHeight + height - _height + 20)
				});
				plrla02_layout.resizeAll();
			}
		}
	};
	cherryAjaxRequest({
		url : url,
		param : param,
		callback : callback
	});
}

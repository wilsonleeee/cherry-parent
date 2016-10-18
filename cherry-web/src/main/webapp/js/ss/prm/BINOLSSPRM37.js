
var grpLocationPageList = new Array();

$(document).ready(function() {
	if ($('#proRewardContent >ul').find('> li').length>1){
		$('#proRewardContent .close').show();
	}
	
});

function disabledNodes(treeObj){
	var nodes =treeObj.getNodes();
	var leafNodes=[];
	getLeafNodes(nodes,leafNodes);
	for (var i=0;i < leafNodes.length; i++) {
		treeObj.setChkDisabled(leafNodes[i], true);
	}
}
function initLocationBox (thisObj,type){
	binOLSSPRM13_global.thisClickObj= $(thisObj);
	// 取得该活动地点所在的索引
	var index = getTimeLocationIndex();
	if ($('#location_dialog_'+index).html() == null){
		var htmlTmpArr = new Array();
		if (type=="edit"){
			getDialogHTML(htmlTmpArr,index);
		}else if(type=="editable"){
			getDialogHTML(htmlTmpArr,index,true);
		}else{
			getDialogHTMLForDetail(htmlTmpArr,index);
		}
		$('#div_main').append(htmlTmpArr.join(''));
	}
	$('#location_dialog_'+index).dialog({ autoOpen: false,  width: 900, height: 'auto', title:BINOLSSPRM13_js_i18n.activeLocation, zIndex: 1,  modal: true,resizable:false, dialogClass: 'dialog-yellow',
		buttons: [{
			text:BINOLSSPRM13_js_i18n.ok,
			click: function() {
				locationOK(index);
				$(this).dialog("close");
			}
		},
		{	
			text:BINOLSSPRM13_js_i18n.cancle,
			click: function() {
				$(this).dialog("close");
			}
		}],
		close: function() {
			$('#treeLeft_'+index).empty();
			$('#treeRight_'+index).empty();
			closeCherryDialog('location_dialog_'+index);
		}
	});
	
	$("#location_dialog_"+index).dialog("open");
	
	locationPositionBinding(index);
	//
	var treeLeft = binOLSSPRM13_global.leftTreeArr[index];
	var treeRight = binOLSSPRM13_global.rightTreeArr[index];
	var setting = binOLSSPRM13_global.zTreeSetting;
	if (treeLeft ==null && treeRight ==null){
		var cloneLeftTreeList = clone(grpLocationPageList[index].leftTreeList);
		var cloneRightTreeList = clone(grpLocationPageList[index].rightTreeList);
//		binOLSSPRM13_global.leftTreeTmp = $('#treeLeft_'+index).zTree(setting,cloneLeftTreeList);
		binOLSSPRM13_global.leftTreeTmp = $.fn.zTree.init($('#treeLeft_'+index),setting,cloneLeftTreeList);
//		binOLSSPRM13_global.rightTreeTmp = $('#treeRight_'+index).zTree(setting,cloneRightTreeList);
		binOLSSPRM13_global.rightTreeTmp = $.fn.zTree.init($('#treeRight_'+index),setting,cloneRightTreeList);
//		if(type=="editable"){
//			disabledNodes(binOLSSPRM13_global.rightTreeTmp);
//		}
		$('#sel_location_type_'+index).val(grpLocationPageList[index].actLocationType);
		binOLSSPRM13_global.locationType = grpLocationPageList[index].actLocationType;
		binOLSSPRM13_global.locationTypeArr[index] = binOLSSPRM13_global.locationType;
	}else{
		if (treeLeft!=null){
			var cloneLeftNodes = cloneNodeDeep(treeLeft.getNodes());
//			binOLSSPRM13_global.leftTreeTmp = $('#treeLeft_'+index).zTree(setting,cloneLeftNodes);
			binOLSSPRM13_global.leftTreeTmp = $.fn.zTree.init($('#treeLeft_'+index),setting,cloneLeftNodes);
		}
		if (treeRight!=null){
			var cloneRightNodes = cloneNodeDeep(treeRight.getNodes());
//			binOLSSPRM13_global.rightTreeTmp = $('#treeRight_'+index).zTree(setting,cloneRightNodes);
			binOLSSPRM13_global.rightTreeTmp = $.fn.zTree.init($('#treeRight_'+index),setting,cloneRightNodes);
//			if(type=="editable"){
//				disabledNodes(binOLSSPRM13_global.rightTreeTmp);
//			}
		}
	}
	//
	var locationType = binOLSSPRM13_global.locationTypeArr[index];
	var $locationType = $('#sel_location_type_'+index);
	var $searchBtn = $locationType.next();
	var $cntFile = $searchBtn.next();
	// 初始化select
	$locationType.val(locationType);
	if(locationType == 5){
		$searchBtn.hide();
		$cntFile.hide();
		if($("#departType").val() != '1' && type!="detail"){
			// 不是品牌总部人员，删除全部柜台节点
			var nodes = binOLSSPRM13_global.rightTreeTmp.getNodes();
			for (var i=0; i < nodes.length; i++) {
				binOLSSPRM13_global.rightTreeTmp.removeNode(nodes[i]);
			}
		}
	}else if(locationType == 6){
		$searchBtn.hide();
		$cntFile.show();
	}else{
		if(type=="edit"){
			$searchBtn.show();
		}
		$cntFile.hide();
	}
}
/**
 * 追加活动地点
 * @returns
 */
function updActLocation(index){
	var url = $("#addActLocationURL").text();
	var leafNodes = [];
	var tree = binOLSSPRM13_global.rightTreeArr[index];
	if(!isEmpty(tree)){
		var nodes = tree.getNodes();
		var $times = $('#activeTimeInfo').children();
		getLeafNodes(nodes, leafNodes);
		var params = "&timeLocationJSON=[";
		for (var i=0;i<leafNodes.length;i++){
			var node = leafNodes[i];
			if(i != 0){
				params += ",";
			}
			params += '"' + node.id + '"';
		}
		params += "]";
		var timeInfo = Obj2JSONArr($times,false,true);
		params += "&timeJSON=" + timeInfo;
		cherryAjaxRequest({
			url:url,
			param:params,
			callback:function(msg){
				$("#actionResultDisplay").html(msg);
			}
		});
	}
}
/**
 * 取得有效的节点
 * @param nodes
 * @returns {Array}
 */
function getNewNodes(nodes){
	var newNodes = [];
	// 全部叶子节点
	var leafNodes=[];
	getLeafNodes(nodes,leafNodes);
	for (var i=0;i<leafNodes.length;i++){
		if (!leafNodes[i].chkDisabled){
			newNodes.push ('{"basePropValue":"' + leafNodes[i].id + '"}');
		}
	}
	return newNodes;
}
var ptjcs14_tree = function(){};
//当前点击节点，（用于选中节点）
var ptjcs14_currnetClick = null;
// 当前点击节点,初始化勾选状态
var ptjcs14_currnetClickCheckedOld = null;

function clickEvent	(event, treeId, treeNode, clickFlag){
	// 当前点击节点
	ptjcs14_currnetClick = treeNode;
	ptjcs14_currnetClickCheckedOld = ptjcs14_currnetClick.checked;
	
	// 清空placeJson,saveJson
	$("#placeJson_0").val('');
	$("#saveJson_0").val('');
	
	// 清空消息
	$("#msgDIV").empty();
	// 清除方案错误提示信息
	$('#productPriceSolutionID').parent().removeClass('error');
	$('#productPriceSolutionID').parent().find('#errorText').remove();
	
	//alert(JSON.stringify(treeNode));
	var deep = treeNode.deep;
	var cntArr = new Array();
	if(deep == "3"){
		//alert(treeNode.name);
		//alert(treeNode.nodes.length);
		
		for(i = 0; i < treeNode.nodes.length ; i++){
			//cntArr += treeNode.children.CounterCode + ",";
			//if(treeNode.nodes[i].checked){
				cntArr.push(treeNode.nodes[i].id);
			//}
		}
		//alert("deep3 " + cntArr);
	}else if(deep == "1" || deep == "2"){
		cntArr = getChildren(cntArr,treeNode);
		//alert("deep1or2 " + cntArr);
	} else if(deep == "4"){
		//alert("我就是柜台节点了！");
		cntArr.push(treeNode.id);
	}
	// 柜台数组集合
	$("#cntArr").val(cntArr);
	
	// 隐藏方案生效区间
	JCS14.disSoluDate('');
	
	// 显示点击对象信息
	disObj(treeNode);
	
	return false;
}

/**
 * check前操作
 */
function zTreeBeforeCheck(treeId, treeNode) {
    return false;
};

/**
 * 显示点击对象信息
 */
function disObj(treeNode){
	var $shwoMainInfo = $("#shwoMainInfo");
	
	// 显示对象信息DIV
	$shwoMainInfo.removeClass('hide');
	if(!$shwoMainInfo.hasClass('show')){
		$shwoMainInfo.addClass('show');
	}
	// 对象名称
	var $objName = $("#objName");
	$objName.text(treeNode.name);
	
	// 对象类型
	var $objType = $("#objType");
	$objType.val(treeNode.deep);
	var $objTypeDesc = $("#objTypeDesc");
	var deep = treeNode.deep;
	if(deep == "1"){
		$objTypeDesc.text("大区");
	} else if (deep == "2"){
		$objTypeDesc.text("省份");
	} else if (deep == "3"){
		$objTypeDesc.text("城市");
	} else if (deep == "4"){
		$objTypeDesc.text("柜台");
	}
	
	// 对象Id
	var $objId = $("#objId");
	$objId.val(treeNode.id);
	
	// 查询柜台对应方案相关信息
	getCntConfig(treeNode);
}

/**
 * 查询柜台对应方案相关信息
 */
function getCntConfig(treeNode){
	
	var deep = treeNode.deep;
	
	if(deep == "4") {
		var param = $("#mainForm").serialize();
		var parentToken = getParentToken();
		param += "&" + parentToken;
		
		cherryAjaxRequest({
			url: $("#getPrtPriceSoluUrlId").val(),
			param: param,
			callback: function(data){
				if(data != 'null'){
					var map = eval("(" + data + ")");
					$('#productPriceSolutionID').val(map.BIN_ProductPriceSolutionID);	
//					$("#productPriceSolutionID option").eq(map.BIN_ProductPriceSolutionID).prop("selected",true);
					// 显示方案生效日期区间
					JCS14.disSoluDate(map.BIN_ProductPriceSolutionID);
					$("#clearSaveId").hide();
				} else {
					$('#productPriceSolutionID').val('');
//					$("#productPriceSolutionID option").eq('').prop("selected",true);
					$("#clearSaveId").show();
				}
			},
			coverId:"#div_main",	
			loadFlg:true
		
		});
	} else{
		$("#productPriceSolutionID option").eq('').prop("selected",true);
		$("#clearSaveId").show();
	}
	
}

/*
* 取得某一点下的所有子节点
* itemArr是一个数组 返回结果数组     treeNode是选中的节点
*/
function getChildren(itemArr,treeNode){
	 if (treeNode.isParent){
			for(var i = 0; i < treeNode.nodes.length; i++ ){
				var obj = treeNode.nodes[i];
				if(obj.deep == "4"){
					itemArr.push(obj.id);
				}
				itemArr = getChildren(itemArr,treeNode.nodes[i]);
			}
	}
	 return itemArr;
}	 
ptjcs14_tree.prototype = {
	"trees" : [],
	// 树配置项
	"setting":{
		check : {
			enable:true,
			chkboxType: { "Y": "ps", "N": "ps" }
		},
		data : {
			key:{
				name : "name",
				children : "nodes"
			}
		},
		view : {	
			showLine : true
		},
		callback: {
			onClick: clickEvent,
			beforeCheck: zTreeBeforeCheck
		}
	},
	"getPosition" : function(index,value){
		if(isEmpty(value)){
			value = $('#locationPositiontTxt_' + index).val();
		}
		var $tree = this.trees[index];
		if(!isEmpty($tree)){
			var inputNodes = $tree.getNodesByParam("name",value);
			$tree.expandNode(inputNodes[0],true,false);
			$tree.selectNode(inputNodes[0]);
		}
	},
	"initTrees" : function(places){
		var that = this;
		if(!isEmpty(places)){
			var places = eval(places);
			for(var i=0; i<places.length; i++){
				var place = places[i];
				if(!isEmpty(place)){
					that.loadTree(place,i,false);
				}
			}
		}
	},
	"setCntNode" : function (){
		
		// 遍历saveJson选中
		var $dataSaveJson = $("#dbSaveJson");
		
		var saveJsonDate = $dataSaveJson.val();
		var list = JSON.parse(saveJsonDate);
		var treeObj = $.fn.zTree.getZTreeObj("tree_0"); 
		
//		var filter =  function (node,param) {
//			alert("param: " + param);
//		    return (node.deep == "4" && node.id == param);
//		};
		
		$.each(list, function(i,item){
			var inputNodes = treeObj.getNodesByParam("id",item);
//			alert(item +"-   -"+JSON.stringify(inputNodes));
			if(!isEmpty(inputNodes) && inputNodes.length != 0){
				treeObj.checkNode(inputNodes[0], true, true);
			}
//			var treeObj = $.fn.zTree.getZTreeObj("tree_0"); 
//			var node = treeObj.getNodesByFilter(filter, true,null,11111); // 仅查找一个节点  v3.2版本才有
//			alert(JSON.stringify(item));
		});
	},
	// 加载树节点
	"loadTree" : function(nodes,index,checkAll) {
		var that = this; 
		var treeNodes = null;
		//绑定区域、柜台、渠道
		that.bindingInput(index);
		if(typeof(nodes) != "object"){
			treeNodes = eval("(" + nodes + ")");
		}else{
			treeNodes = nodes;
			checkedFlag = 1;
		}
		that.trees[index] = $.fn.zTree.init($("#tree_" + index),that.setting,treeNodes);
		
		that.setCntNode();
		
//		if(checkAll){// 全选
//			that.trees[index].checkAllNodes(true);
//		}
	},
	//下拉框绑定区域、柜台、渠道
	"bindingInput": function(index){
		var $selectVal = $("#locationType_"+index).val();
		var $locationType=$("#locationId_"+index);
		$("#locationPositiontTxt_"+index).val("");
		if($selectVal=='2'||$selectVal=='4'||$selectVal=='5'){//柜台绑定
			$locationType.show();
			counterBinding({
				elementId:"locationPositiontTxt_"+index,
				showNum:20
			});
		}else if($selectVal=='1'){//区域绑定
			$locationType.show();
			regionBinding({
				elementId:"locationPositiontTxt_"+index,
				showNum:20
			});
		}else if($selectVal=='3'){//渠道绑定
			$locationType.show();
			channelBinding({
				elementId:"locationPositiontTxt_"+index,
				showNum:20
			});
		}else if($selectVal=='0'||$selectVal==""){
			$locationType.hide();
		}
	},
	// 查询数节点
	"searchNodes" : function($select,index){
		var that = this;
		if($select.val() == '0'){// 全部柜台
			var nodes = [{id:"all",name:$select.find("option[value='0']").text()}];
			that.loadTree(nodes, index, true);
		}else{
			var url = '/Cherry/pt/BINOLPTJCS14_getTree';
			var param = 'loadingCnt=1';
			param += "&locationType=" + $select.val();
			param += "&departProductConfigID=" + $("#departProductConfigID").val();
			cherryAjaxRequest( {
				url : url,
				param : param,
				callback : function(nodes) {
					that.loadTree(nodes,index,false);
				},
				coverId:"#div_main",	
				loadFlg:true
			});
		}
	},
    /**
     * 增加节点
     */
    "addNodes" : function(treeObj,newNodes,checked){
    	var oldNodes = treeObj.getNodes();
    	var tempNodes = [];
		for (var i=0;i < newNodes.length; i++) {
			var isContain = false;
			for(var j=0;j < oldNodes.length; j++){
				if(oldNodes[j].id == newNodes[i].id){
					isContain = true;
					break;
				}
			}
			if(!isContain){
				newNodes[i].checked = checked;
				tempNodes.push(newNodes[i]);
			}
		}
    	if(tempNodes.length > 0){
    		treeObj.addNodes(null, tempNodes);
    	}
    },
    /**
     * 取得树节点
     * 
     * @param index
     * @param checked
	 * @return
     */
    "getTreeNodes":function (tree,checked){
		var nodes = [];
		if(!isEmpty(tree)){
			if(checked){
				// 半选以及全选节点
				nodes = tree.getCheckedNodes(true);
			}else{
				// 全部节点
				nodes = tree.getNodes();
			}
		}
		return nodes;
    },
    /**
	 * 取得选中节点【排除全选节点的子节点】
	 * 
	 * @param nodes
	 * @return
	 */
	"getCheckedNodes" : function (nodes){
		var checkNodes = [];
		if(!isEmpty(nodes)){
			// 取得全选节点(不包含全选的子节点)
			for(var i=0; i< nodes.length; i++){
				var node = nodes[i];
				// 半选
				if(node.getCheckStatus().half){
					var obj = {};
					obj.id = node.id;
					obj.half = true;
					obj.name = node.name;
					obj.level = node.level;
					checkNodes.push(obj);
				}else{
					var pNode = node.getParentNode();
					// 父节点为null或半选
					if(isEmpty(pNode) || pNode.getCheckStatus().half){
						var obj = {};
						obj.id = node.id;
						obj.half = false;
						obj.name = node.name;
						obj.level = node.level;
						checkNodes.push(obj);
					}
				}
			}
		}
		return checkNodes;
	},
	/**
	 * 取得叶子节点
	 * 
	 * @param nodes
	 * @return
	 */
	"getLeafNodes" : function(nodes){
		var leafNodes = [];
		if(!isEmpty(nodes)){
			// 取得全选的叶子节点
			for(var i=0; i< nodes.length; i++){
				var node = nodes[i];
				// 非父节点,即叶子节点
				if(!node.isParent){
					leafNodes.push(node.id);
				}
			}
		}
		return leafNodes;
	}
};
var ptjcs14Tree = new ptjcs14_tree();
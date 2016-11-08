var cherryTree = function(){};
cherryTree.prototype = {
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
		// 初始化节点半选属性
		var halfCheckNodes = that.trees[index].getNodesByParam("halfCheck",true,null);
		if(undefined != halfCheckNodes && null != halfCheckNodes && halfCheckNodes.length > 0){
			for(var i=0; i < halfCheckNodes.length; i++){
				halfCheckNodes[i].halfCheck = false;
			}
		}
		if(checkAll){// 全选
			that.trees[index].checkAllNodes(true);
		}
	},
	//下拉框绑定区域、柜台、渠道
	"bindingInput": function(index){
		var $selectVal = $("#locationType_"+index).val();
		var $locationType=$("#locationId_"+index);
		$("#locationPositiontTxt_"+index).val("");
		if($selectVal=='2'||$selectVal=='4'||$selectVal=='5'||$selectVal=='8'){//柜台绑定
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
		}else if($selectVal=='7'){//所属系统绑定
			$locationType.show();
//			channelBinding({
//				elementId:"locationPositiontTxt_"+index,
//				showNum:20
//			});
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
			var url = '/Cherry/cp/BINOLCPPOI01_getTree';
			var param = 'loadingCnt=1';
			param += "&locationType=" + $select.val();
			cherryAjaxRequest( {
				url : url,
				param : param,
				callback : function(nodes) {
					that.loadTree(nodes,index,false);
				}
			});
		}
	},
	// 改变地点类型
	"changeType" : function(_this,index){
		var that = this;
		var $this = $(_this);
		// 清空树内容
		that.trees[index] = null;
		$("#tree_" + index).empty();
		if($this.val() == '5'){// 导入柜台
			$("#importDiv_" + index).show();
			$("#locationId_"+index).hide();
		}else{
			$("#importDiv_" + index).hide();
			that.searchNodes($this, index);
		}
	},
	// 展示会员信息
	"showMebList":function(json,index){
		var $resultDiv = $("#conInfoDiv_" + index);
		var $memCount = $('#memCount_' + index);
		$("#searchCode_"+index).val(json['searchCode']);
		$memCount.text(json['size']);
		$resultDiv.show();
	},
	 // AJAX文件上传
    "ajaxFileUpload" : function (index,type){
    	var that = this;
    	// 导入柜台URL
		var url = '';
		var params = {};
		params.csrftoken = parentTokenVal();
		params.brandInfoId = $("#brandInfoId").val();
		if(isEmpty(type) || type == 0){
			url = '/Cherry/cp/BINOLCPPOI01_importCounter';
		}else if(type == 1){
			url = '/Cherry/cp/BINOLCPPOI01_importMember';
			params.recordName = $.trim($('#campList').find('li.on').first().text());
//			params.searchCode = $("#searchCode_" + index).val();
		}
		// 子活动内容DIV
    	var $context = $("#campContext_"+index);
    	var $upExcel = $('#upExcel_' + index);
    	var $loadingImg = $("#loadingImg_" + index);
    	var $errorDiv = $('#errorDiv');
    	var $errorSpan = $('#errorSpan');
    	$errorMessage = $('#actionResultDisplay');
    	// 清空错误信息
    	$errorDiv.hide();
    	$errorMessage.hide();
    	if($upExcel.val()==''){
    		$('#pathExcel_'+index).val("");
    		$errorSpan.text($("#errMsg_1").text());
    		$errorDiv.show();
    		return false;
    	}
    	$loadingImg.ajaxStart(function(){$(this).show();});
    	$loadingImg.ajaxComplete(function(){$(this).hide();});
    	$.ajaxFileUpload({
	        url: url,
	        secureuri:false,
	        data:params,
	        fileElementId:'upExcel_' + index,
	        dataType: 'html',
	        success: function (msg){
	        	if($("<div>"+msg+"<div>").find("#actionResultDiv").length > 0){
	        		$errorMessage.html(msg);
	        		$errorMessage.show();
	        	}else if (window.JSON && window.JSON.parse) {
					var msgJson = window.JSON.parse(msg);
					if(msgJson['errorMsg'] != undefined){
						$errorSpan.text(msgJson['errorMsg']);
						$errorDiv.show();
					}else{
						$errorDiv.hide();
					}
					if(isEmpty(type) || type == 0){
						var counterNodes = msgJson['trueCounterList'];
						if(isEmpty(that.trees[index])){
							// 柜台节点加载到树
							that.loadTree(counterNodes,index,true);
						}else{
							that.addNodes(that.trees[index], counterNodes, true);
						}
					}else if(type == 1){
						// 展示活动对象信息
						that.showMebList(msgJson,index);
					}
	        	}
	        }
        });
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
				newNodes[i].iconSkin = "newAdd";
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
				//if(node.getCheckStatus().half){
					var obj = {};
					obj.id = node.id;
					obj.half = node.getCheckStatus().half;
					obj.isParent = node.isParent;
					obj.name = node.name;
					obj.level = node.level;
					checkNodes.push(obj);
				//}else{
				//	var pNode = node.getParentNode();
				//	// 父节点为null或半选
				//	if(isEmpty(pNode) || pNode.getCheckStatus().half){
				//		var obj = {};
				//		obj.id = node.id;
				//		obj.half = false;
				//		obj.name = node.name;
				//		obj.level = node.level;
				//		checkNodes.push(obj);
				//	}
				//}
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
	},
	"selectAll" : function(_this,index){
		var $tree = this.trees[index];
		var $this = $(_this);
		if(!isEmpty($tree)){
			$tree.checkAllNodes($this.is(":checked"));
		}
	}
};
var CHERRYTREE = new cherryTree();
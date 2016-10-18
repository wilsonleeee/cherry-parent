/**
 * 全局变量定义
 */
var binOLPTJCS17_global = {};
// 当前点击对象
binOLPTJCS17_global.thisClickObj = {};
// ul HTML 
binOLPTJCS17_global.ulHTML = '<ul class = "items">\n';
// li HTML
binOLPTJCS17_global.liHTML = '<li class = "item">\n';
// 促销奖励
binOLPTJCS17_global.rewardType ="rewardProduct";
// 添加分配组弹出框内容
binOLPTJCS17_global.dialogHTML = '';
// z-Tree 设置
//binOLPTJCS17_global.zTreeSetting ={checkable : true,showLine : true,expandSpeed : "",callback:{beforeExpand: expendTreeNodesByAjax}};
binOLPTJCS17_global.zTreeSetting = {
		check: {
			enable: true
		},
		data:{
			key:{
				name:"name",
				children:"nodes"
			}
		},
		callback:{beforeExpand: expendTreeNodesByAjax}
};
// 左边树数组
binOLPTJCS17_global.leftTreeArr =new Array (null);
// 右边树数组
binOLPTJCS17_global.rightTreeArr = new Array (null);
// 方案分配地点类型数组
binOLPTJCS17_global.locationTypeArr = new Array(null);
// 左边树Tmp
binOLPTJCS17_global.leftTreeTmp;
// 右边树Tmp
binOLPTJCS17_global.rightTreeTmp;
// 时间地点提交数据
binOLPTJCS17_global.timeLocationDataArr = new Array ({});
// 日历起始日期
binOLPTJCS17_global.calStartDate="";
// 假日
binOLPTJCS17_global.holidays="";
// 分配地点区分
binOLPTJCS17_global.locationType = "";
// 分配地点关联已选择分配地点Flag
binOLPTJCS17_global.locationFlg = false;
// 目标节点
binOLPTJCS17_global.targetNode=null;
// 定位名称
binOLPTJCS17_global.positionName ="";
//是否需要解锁
binOLPTJCS17_global.needUnlock = true;
//画面显示类型
binOLPTJCS17_global.showType = "";

function changeSolu(_this){
	
	// 清空提示消息
	cleanMsgDIV();
	
	var that = this;
	var $this = $(_this);
//	alert("soluId: " + $this.val());
	if($this.val() == ''){
		//$("#soluMsg").hide();
	} else{
		// 读取方案对应的placeType、saveJson
		var url = $('#getDPConfigDetailBySoluUrl').html();
		var param = $("#mainForm").serialize();
		cherryAjaxRequest({
			url:url,
			param:param,
			callback: function(data){
				if('null' != data){
					if (data.indexOf('fieldErrorDiv')<0){
						
						var map = JSON.parse(data);
						$('#sel_location_type_0').val(map.PlaceType);	
						$('#placeTypeOld').val(map.PlaceType);	
						// 显示树
						changeLocationType($('#sel_location_type_0'));
						
					}
					
				} else {
					var isPosCloudVal = $("#isPosCloudId").val();
//					alert(isPosCloudVal);
					if(isPosCloudVal == 1){
//						alert('111');
						changeLocationType($("#sel_location_type_0"));
					}else{
						
						var index = 0; 
						var setting  = binOLPTJCS17_global.zTreeSetting;
						// 清空左树
						binOLPTJCS17_global.leftTreeTmp = $.fn.zTree.init($('#treeLeft_'+index),setting,null);
						// 清空右树
						binOLPTJCS17_global.rightTreeTmp = $.fn.zTree.init($('#treeRight_'+index),setting,null);
						
						// 方案没有配置(此时地点是请选择，所以清空左右树)
						$('#sel_location_type_0').val(0);	
						$('#placeTypeOld').val(0);
						
					}
					
				}
			},
			coverId:"#div_main",	
			loadFlg:true
		});
		
		
	}
}


/**
 * 分配地点选择全部柜台时，查询按钮不显示
 * @param obj
 * @return
 */
function changeLocationType (obj){
	// 清空提示消息
	cleanMsgDIV();
	locationPositionBinding(0);
//	alert(11);
	// 取得索引值
//	var index = getTimeLocationIndex(); 
	var index = 0; 
	// 情况错误信息
	var $message = $('#actionResultDisplay_' + index);
	var $errorDiv = $('#errorDiv_' + index);
	$errorDiv.hide();
	$message.hide();
	$(obj).next().next().hide();
	if($(obj).val() == 5 || $(obj).val() == 6){
		// 清空右边的树
		var setting = binOLPTJCS17_global.zTreeSetting;
		binOLPTJCS17_global.rightTreeTmp = $.fn.zTree.init($('#treeRight_'+index),setting,null);
		binOLPTJCS17_global.leftTreeTmp = $.fn.zTree.init($('#treeLeft_'+index),setting,null);
		if($(obj).val() == 6){
			$(obj).next().next().show();
			this.getLocationData();
		}
		if($(obj).val() == 5){
//			binOLPTJCS17_global.rightTreeTmp.addNodes(null, [{id:"all",name:binOLPTJCS17_js_i18n.allCounter}]);
			binOLPTJCS17_global.rightTreeTmp.addNodes(null, [{id:"all",name:"全部柜台"}]);
		}
	}else{
		this.getLocationData();
	}
}

/**
 * 清空提示消息
 */
function cleanMsgDIV(){
	// 清空消息
	$("#msgDIV").empty();
}

/**
 * 取得分配地点数据
 * @return
 */
function getLocationData (){
//	var index = getTimeLocationIndex(); 
	var index = 0;
	
	var url = $('#locationSearchUrl').html();
	// 分配地点类型
	var locationType = $('#sel_location_type_'+index).val();
	// 如果用户重新搜索分配地点,则不用再关联之前保存的分配地点
	binOLPTJCS17_global.locationFlg = true;
	binOLPTJCS17_global.locationType = locationType;
	binOLPTJCS17_global.locationTypeArr[index] = binOLPTJCS17_global.locationType;
	var param = $("#mainForm").serialize();
	param += "&placeType="+locationType;
	param += "&cntOwnOpt="+$("#cntOwnOpt").val(); // 门店自设
	cherryAjaxRequest({
		url:url,
		param:param,
		callback:addLocationTreeData,
		coverId:"#div_main",	
		loadFlg:true
		
		//,formId:"#prmActSearchform"
	});
	// 清空右边的树
//	var setting = binOLPTJCS17_global.zTreeSetting;
//	binOLPTJCS17_global.rightTreeTmp = $.fn.zTree.init($('#treeRight_'+index),setting,null);
	
	/**
	 * 加载树
	 * @param data
	 * @return
	 */
	function addLocationTreeData(data){
		var setting  = binOLPTJCS17_global.zTreeSetting;
		// 取得该分配地点所在的索引
//		var index = getTimeLocationIndex(); 
		var index = 0; 
//		binOLPTJCS17_global.leftTreeTmp = $('#treeLeft_'+index).zTree (setting,eval('('+data+')'));
		
		var rootNodes = data.split("*|"); 
		// 左树
		binOLPTJCS17_global.leftTreeTmp = $.fn.zTree.init($('#treeLeft_'+index),setting,eval('('+rootNodes[0]+')'));
		// 右树
		binOLPTJCS17_global.rightTreeTmp = $.fn.zTree.init($('#treeRight_'+index),setting,eval('('+rootNodes[1]+')'));
		
		if($("#isPosCloudId").val() == 1){
			var rightTree = $.fn.zTree.getZTreeObj("treeRight_0");
			var leftTree = $.fn.zTree.getZTreeObj("treeLeft_0");
			rightTree.expandAll(true);
			leftTree.expandAll(true);
		}
	}
	/**
	 * 加载树
	 * @param data
	 * @return
	 */
	function addLocationTreeDataOld(data){
		var setting  = binOLPTJCS17_global.zTreeSetting;
		// 取得该分配地点所在的索引
//		var index = getTimeLocationIndex(); 
		var index = 0; 
//		binOLPTJCS17_global.leftTreeTmp = $('#treeLeft_'+index).zTree (setting,eval('('+data+')'));
		binOLPTJCS17_global.leftTreeTmp = $.fn.zTree.init($('#treeLeft_'+index),setting,eval('('+data+')'));
	}
}

/**
 * 保存方案分配地点
 */
function addSave(){
	
	var index = 0;
	var locationArr = new Array();
	var locationType = $("#sel_location_type_0").val();
	
	if($("#errorDiv_0").find("#impErrorSpan").text() && locationType == 6){
//		alert('非空字符');
		return false;
	}
	
	// 清空提示消息
	cleanMsgDIV();
	
	var rightTree = $.fn.zTree.getZTreeObj("treeRight_0");
	var nodes = rightTree.getNodes();
	
	// 分配地点类型
	if(locationType == 5){
		binOLPTJCS17_global.locationType = locationType;
		binOLPTJCS17_global.locationTypeArr[index] = binOLPTJCS17_global.locationType;
		locationArr = addLocationData (nodes,locationArr,locationType);
	}else if(locationType == 6){
		binOLPTJCS17_global.locationType = locationType;
		binOLPTJCS17_global.locationTypeArr[index] = binOLPTJCS17_global.locationType;
		locationArr = addLocationData (nodes,locationArr,locationType);
	}else{
		locationArr = addLocationData (nodes,locationArr,locationType);
	}
//	alert(locationArr);
//	if(locationArr.length == 0){
//		return false;
//	}
	
	var param = $("#mainForm").serialize();
	
	param += "&locationArr="+locationArr + "&placeType=" + $("#sel_location_type_0").val();
	
	var parentToken = getParentToken();
	param += "&" + parentToken;
//	alert(param);
	cherryAjaxRequest({
		url: $("#addDepartSoluUrl").html(),
		param: param,
		callback: function(data){
			if(data.indexOf('fieldErrorDiv') < 0){
				var map = JSON.parse(data);
				if(map.result != '0'){
					var ht = $("#actionSuccessId").clone();
					$("#msgDIV").html('').append(ht);
				} else {
					var ht= $("#actionFaildId").clone();
					$("#msgDIV").html('').append(ht);
				}
				
			}
		},
		coverId : "#div_main"
	});
}

/**
* 添加地点数据
* @param nodes
* @param arr
* @return
*/
function addLocationData(nodes,arr,locationType){
	if (!nodes) return arr; 
//	locationType = binOLPTJCS17_global.locationTypeArr[index];
	for (var i=0;i<nodes.length;i++){
		var node = nodes[i];
		var childNodes = node.nodes;
		if (childNodes ==null){
			if (locationType == "1" || locationType == "5"){
//				arr.push ("city_"+node.id);
				arr.push (node.id);
			}else if (locationType == "3"){
//				arr.push ("channel_"+node.id);
				arr.push (node.id);
			}else if (locationType == "2" || locationType == "4" || locationType == "6" || locationType == "7"){
//				arr.push ("counter_"+node.id);
				arr.push ('"'+node.id+'"');
			}
		}else if (childNodes.length == 1 && childNodes[0].name =="artificialCounter"){
			if (locationType == "2"){
//				arr.push ("city_"+node.id);
//				arr.push (node.id);
				arr.push ('"'+node.id+'"');
			}else if (locationType == "4"){
//				arr.push ("channel_"+node.id);
//				arr.push (node.id);
				arr.push ('"'+node.id+'"');
			}
		}else {
			arr = addLocationData(childNodes,arr,locationType);
		}
	}
	return arr;
}

/**
 * 取得选中的地点数据
 * @param nodes
 * @param arr
 * @return
 */
function addCheckLocationData(nodes,arr,locationType){
	if (!nodes) return arr; 
//	locationType = binOLPTJCS17_global.locationTypeArr[index];
	for (var i=0;i<nodes.length;i++){
		var node = nodes[i];
		var childNodes = node.nodes;
		if(node.checked){
			if (childNodes ==null){
				if (locationType == "1" || locationType == "5"){
//				arr.push ("city_"+node.id);
					arr.push (node.id);
				}else if (locationType == "3"){
//				arr.push ("channel_"+node.id);
					arr.push (node.id);
				}else if (locationType == "2" || locationType == "4" || locationType == "6" || locationType == "7"){
//				arr.push ("counter_"+node.id);
//					arr.push ('"'+node.id+'"');
					arr.push (node.id);
				}
			}else if (childNodes.length == 1 && childNodes[0].name =="artificialCounter"){
				if (locationType == "2"){
//				arr.push ("city_"+node.id);
//				arr.push (node.id);
//					arr.push ('"'+node.id+'"');
					arr.push (node.id);
				}else if (locationType == "4"){
//				arr.push ("channel_"+node.id);
//				arr.push (node.id);
//					arr.push ('"'+node.id+'"');
					arr.push (node.id);
				}
			}else {
				arr = addCheckLocationData(childNodes,arr,locationType);
			}
			
		}
	}
	return arr;
}

/**
 * 左边树到右边树
 * @return
 */
function left2RightOld (){
//	var index = getTimeLocationIndex(); 
	moveCheckNodes(binOLPTJCS17_global.leftTreeTmp,binOLPTJCS17_global.rightTreeTmp);	
}

/**
 * 请选择要移动的柜台提示框
 */
function pleaseSelectConfirm() {
	var title = $('#tipTitle').text();
	var text = $('#pleaseSelect').html();
	var dialogSetting = {
		dialogInit: "#dialogPlaceToPlaceTipDIV",
		text: text,
		width: 	300,
		height: 200,
		title: 	title,
		confirm: $("#dialogConfirm").text(),
		confirmEvent: function(){removeDialog("#dialogPlaceToPlaceTipDIV");}
	};
	openDialog(dialogSetting);
}
/**
 * 左边树到右边树
 * @return
 */
function left2Right (){
	
	var locationArr = new Array();
	var locationType = $("#sel_location_type_0").val();
	
	var leftTree = $.fn.zTree.getZTreeObj("treeLeft_0");
	
	// 左树转移到右树时，没有选中的节点则还能移动
	var checkNodes = leftTree.getCheckedNodes();
	var length = checkNodes.length;
	if (length == 0) {
		pleaseSelectConfirm();
		return;
	}
	
	// 根据地点类型取得转移的地点集合
	var nodes = leftTree.getNodes();
	locationArr = addCheckLocationData(nodes,locationArr,locationType);
	
	// 检查分配地点是否已被其他方案分配过
	chkAllocationPlaceConflict('left2right',locationArr, locationType);
}

/**
 * 检查分配地点是否已被其他方案分配过
 * chkFlag 检查途径（树转移:left2right、导入：import）
 */

function chkAllocationPlaceConflict(chkFlag,locationArr,locationType){
	
	var url = $("#chkExistCntUrl").html();
	
	var param = $("#mainForm").serialize();
	param += "&locationArr="+locationArr + "&placeType=" + locationType;
	
	var callback = function(msg) {
		// 启用时，若存在有效产品编码条码时，则openDialog,否则直接调用_this.editValidFlag（）方法
		if(msg.indexOf("result0") != -1){
			var dialogSetting = {
					dialogInit: "#dialogLeftToRightDIV",
					bgiframe: true,
					width:  700,
					height: 350,
					resizable : true,
					text: msg,
					title: 	$("#tipTitle").text(),
					/*
					confirm: $("#dialogConfirmgoOn").text(),
					confirmEvent: function(){
						if(chkFlag == 'left2right'){
							moveCheckNodes(binOLPTJCS17_global.leftTreeTmp,binOLPTJCS17_global.rightTreeTmp);	
						}else if(chkFlag == 'import'){
							// 
//							alert("导入");
						}
						removeDialog("#dialogLeftToRightDIV");
					},
					cancel: $("#dialogCancel").text(),
					cancelEvent: function(){
						if(chkFlag == 'import'){
							binOLPTJCS17_global.rightTreeTmp = $.fn.zTree.init($('#treeRight_0'),binOLPTJCS17_global.zTreeSetting,null);
						}
						removeDialog("#dialogLeftToRightDIV");
					}
					*/
			};
			openDialog(dialogSetting);
			// 柜台产品分配地点冲突是否覆盖 0:否 1:是
			if(msg.indexOf("placeConflict_0") != -1){
				$("#dialogLeftToRightDIV").dialog( "option", {
					buttons: [{
						text: $("#dialogConfirm").text(),
					    click: function(){removeDialog("#dialogLeftToRightDIV");}
					}]
				});
			}else if (msg.indexOf("placeConflict_1") != -1){
				$("#dialogLeftToRightDIV").dialog( "option", {
					buttons: [{
								text: $("#dialogConfirmgoOn").text(),
							    click: function(){
									if(chkFlag == 'left2right'){
										moveCheckNodes(binOLPTJCS17_global.leftTreeTmp,binOLPTJCS17_global.rightTreeTmp);	
									}else if(chkFlag == 'import'){
										// 
		//								alert("导入");
									}
									removeDialog("#dialogLeftToRightDIV");
								}
							},
							{
								text: $("#dialogCancel").text(),
							    click: function(){
									if(chkFlag == 'import'){
										binOLPTJCS17_global.rightTreeTmp = $.fn.zTree.init($('#treeRight_0'),binOLPTJCS17_global.zTreeSetting,null);
									}
									removeDialog("#dialogLeftToRightDIV");
								}
							}]
				});	
			}
			
		}else if(msg.indexOf("result1") != -1){
			alert("导入验证失败");
		}else{
			if(chkFlag == 'left2right'){
				moveCheckNodes(binOLPTJCS17_global.leftTreeTmp,binOLPTJCS17_global.rightTreeTmp);	
			}else if(chkFlag == 'import'){
				// 
//				alert("导入");
			}
		}
		
	};
	
	cherryAjaxRequest({
		url: url,
		param: param,
		callback: callback,
		coverId:"#div_main",	
		loadFlg:true
	});
}

/**
 * 右边树到左边树
 * @return
 */
function right2Left (){
//	var index = getTimeLocationIndex(); 
	moveCheckNodes(binOLPTJCS17_global.rightTreeTmp,binOLPTJCS17_global.leftTreeTmp);	
}

/**
 * 移动选中的节点
 * @param srcTree
 * @param targetTree
 * @return
 */
function moveCheckNodes(srcTree,targetTree){
	if (srcTree!=null && targetTree !=null){
		var checkNodes = srcTree.getCheckedNodes();
		var groupArr = checkedNodesGroupBy (checkNodes);
		for (var i =0;i<groupArr.length;i++){
			var group = groupArr[i];
			addCheckedNodes (group.nodes,srcTree,targetTree);
		}
		//addCheckedNodes (checkNodes,srcTree,targetTree);
	}
}


/**
 * 添加选中的节点
 * @param checkedNodes
 * @return
 */
function addCheckedNodes (checkedNodes,srcTree,targetTree){
	var srcParentNodeArr = new Array();
	// 转移flag,true的情况下可以进行直接移动
	var moveFlag =false;
//	var index = getTimeLocationIndex(); 
	var index = 0; 
	if (checkedNodes[0].name == 'artificialCounter'){
		var srcParentNode = checkedNodes[0].getParentNode();
		srcParentNodeArr.push(srcParentNode);
//		var treeNode = searchNodeById(targetTree.getNodes(),srcParentNode.id);
		var treeNode = targetTree.getNodeByParam("id", srcParentNode.id, null);
		if (treeNode!=null){
			// 需要先加载源树节点
			var locationType = binOLPTJCS17_global.locationTypeArr[index];
			if (locationType == "2"){
				var param = "cityID="+treeNode.id;
			}else if (locationType == "4"){
				var param = "channelID="+treeNode.id;
			}
			
			// 取得分配地点所在的分组索引
//			var index = getTimeLocationIndex(); 
			var index = 0; 
			// 取得树对象
			if (srcParentNode.tId.indexOf('treeLeft')>=0){
				var tree= binOLPTJCS17_global.leftTreeTmp;
				param = param + "&treeType=leftTree";
			}else {
				var tree= binOLPTJCS17_global.rightTreeTmp;
				param = param + "&treeType=rightTree";
			}
			if ((!binOLPTJCS17_global.locationFlg) && $('#activeID')!=null && $('#activeID').val()!=undefined){
				param = param + "&activeID="+$('#activeID').val();
			}
			if ($('#activitySetBy')!=null && $('#activitySetBy').val()!=undefined){
				param = param + "&activitySetBy="+$('#activitySetBy').val();
			}
			if ($('#showType')!=null && $('#showType').val()!=undefined){
				param = param + "&showType="+$('#showType').val();
			}
//			var index = getTimeLocationIndex();
			var index = 0;
			param = param + "&grpID="+(parseInt(index)+1);

			tree.updateNode(treeNode);
			url = $('#getCounterDetailByAjaxUrl').html();
			cherryAjaxRequest({
				url:url,
				param:param,
				callback:expendTreeNodesCallBack,
				formId:"#prmActSearchform",
				reloadFlg:true
			});
			
			
		}else{
			moveFlag = true;
		}
	}else{
		moveFlag = true;
	}
	
	if (moveFlag){
		var newNodes = [];
		for (var i=0;i<checkedNodes.length;i++){
			var newNode = cloneNode (checkedNodes[i],null);
			newNodes.push(newNode);
		}
		
		addTargetTreeNode (targetTree,targetTree.getNodes(),checkedNodes[0],newNodes);
		targetTree.refresh();
		srcTree.checkAllNodes(false);
		for (var i=0;i<checkedNodes.length;i++){
			// 删除节点
			removeNodes(srcTree,checkedNodes[i]);
		}
	}

	function expendTreeNodesCallBack (data){
		var nodes = eval('('+data+')');
		var prNode;
		var cloneNodes = [];
		for (var i=0;i<nodes.length;i++){
			var node = nodes[i];
			for (var j = 0;j<srcParentNodeArr.length;j++){
				var srcPrNode = srcParentNodeArr[j];
				if (locationType == "2" && node.cityID == srcPrNode.id ){
					prNode = srcPrNode;
					break;
				}else if (locationType == "4" && node.channelID == srcPrNode.id){
					prNode = srcPrNode;
					break;
				}
			}
			node.parentNode = prNode;
			var newNode = cloneNode (node,null);
			cloneNodes.push(newNode);
		}
		
		addTargetTreeNode (targetTree,targetTree.getNodes(),nodes[0],cloneNodes);
		
		// 删除节点
		removeNodes(tree,prNode);
	}
}



function expendTreeNodesCallBack (data){
	var nodes = eval('('+data+')');
	var prNode;
	var cloneNodes = [];
	for (var i=0;i<nodes.length;i++){
		var node = nodes[i];
		for (var j = 0;j<srcParentNodeArr.length;j++){
			var srcPrNode = srcParentNodeArr[j];
			if (locationType == "2" && node.cityID == srcPrNode.id ){
				prNode = srcPrNode;
				break;
			}else if (locationType == "4" && node.channelID == srcPrNode.id){
				prNode = srcPrNode;
				break;
			}
		}
		node.parentNode = prNode;
		var newNode = cloneNode (node,null);
		cloneNodes.push(newNode);
	}
	
	addTargetTreeNode (targetTree,targetTree.getNodes(),nodes[0],cloneNodes);
	
	// 删除节点
	removeNodes(tree,prNode);
}

/**
* 选中节点分组
* @return
*/
function checkedNodesGroupBy (checkedNodes){
var groupArr = [];
for (var i=0;i<checkedNodes.length;i++){
	var checkedNode = checkedNodes[i];
	if (checkedNode.nodes != null){
		continue;
	}
	
	var flag = true;
	
	for (var j=0;j<groupArr.length;j++){
		var group = groupArr[j];
		var compareId = "";
		if (checkedNode.getParentNode() == null){
			compareId = checkedNode.id;
		}else{
			compareId = checkedNode.getParentNode().id;
		}
		if (group.id == compareId){
			var checkedNodeArr = group.nodes;
			checkedNodeArr.push(checkedNode);
			flag = false;
			break;
		}
	}
	
	if (flag){
		var group = {};
		if (checkedNode.getParentNode() != null){
			group.id = checkedNode.getParentNode().id;
		}else{
			group.id = checkedNode.id;
		}
		
		var checkedNodeArr = [];
		checkedNodeArr.push(checkedNode);
		group.nodes = checkedNodeArr;
		groupArr.push(group);
	}
}

return groupArr;
}

/**
* 根据id查询节点
* @param nodes
* @param id
* @return
*/
function searchNodeById (nodes,id){
if (!nodes || !id) return null;
// 遍历所有节点
for (var i=0;i<nodes.length;i++){
	// 定义一个节点
	var node = nodes[i];
	if (node.id === id){
		return node;
	}
	var tmp = searchNodeById(node.nodes, id);
	if (tmp) return tmp;
}
return null;
}

/**
* 添加目标树的节点
* @param targetTree
* @param targetNodes
* @param node
* @param newNode
* @return
*/
function addTargetTreeNode(targetTree,targetNodes,node,newNode){
 var srcParentNode = null;
 var targetNewNode = null;
 try{
	 srcParentNode = node.getParentNode() ;
 }catch(e){
	 srcParentNode = node.parentNode;
 }finally{
	 if (srcParentNode ==null){
		 targetNewNode = targetTree.addNodes(srcParentNode,newNode,true);
//		  var newTreeNode = searchNodeById(targetTree.getNodes(),newNode[0].id);
//		  var artificialNode = searchNodeById(newTreeNode.nodes,'artificialCounter');
//		  if (artificialNode==null){
//			  // 展开节点
//			  targetTree.expandNode(newTreeNode,true,true);
//		  }
		  
	  }else{
//		  var targetNode = searchNodeById(targetNodes,srcParentNode.id);
		  var targetNode = targetTree.getNodeByParam("id",srcParentNode.id, null);
		  // 如果目标节点没有源节点的父节点id
		  if (targetNode==null){
			  var cloneNewNode = cloneNode(node,newNode);
			  addTargetTreeNode (targetTree,targetNodes,srcParentNode,[cloneNewNode]);
		  }else{
			  // 目标节点下面有AJAX柜台需要加载
			  if (targetNode.nodes!=null && targetNode.nodes.length == 1 && targetNode.nodes[0].name=="artificialCounter"){
				  getCounterByAjax(targetNode);
			  }
			  targetNewNode = targetTree.addNodes(targetNode,newNode,true);
//			  var newTreeNode = searchNodeById(targetTree.getNodes(),newNode[0].id);
//			  var artificialNode = searchNodeById(newTreeNode.nodes,'artificialCounter');
//			  if (artificialNode==null){
//				  // 展开节点
//				  targetTree.expandNode(newTreeNode,true,true);
//			  }
		  }
	  }
	 targetTree.setChkDisabled(targetNewNode, true);
 }
}

/**
* 复制节点(仅节点id,name数据)
* @param node
* @param childNode
* @return
*/
function cloneNode (node,childNodes){
var newNode = {};
if (childNodes!=null){
	// 取得父节点
	var parentNode = node.getParentNode();
	newNode.id = parentNode.id;
	newNode.name = parentNode.name;
	// 设置子节点
	newNode.nodes = childNodes;
}else{
	newNode.id = node.id;
	newNode.name = node.name;
}
return newNode;
}

/**
* 深层递归克隆节点(仅id,name数据)
* @param node
* @return
*/
function cloneNodeDeep(jsonObj){

var  buf;   
if  (jsonObj  instanceof  Array) {   
    buf = [];   
    var  i = jsonObj.length;   
    while  (i--) {   
        buf[i] = cloneNodeDeep(jsonObj[i]);   
    }   
    return  buf;   
}else   if  (jsonObj  instanceof  Object){   
    buf = {};   
    buf.id = jsonObj.id;
    buf.name = jsonObj.name;
    buf.nodes = cloneNodeDeep(jsonObj.nodes);
    return  buf;   
}else {   
    return  jsonObj;   
} 
}


/**
* 删除节点
* @param tree
* @param node
* @return
*/
function removeNodes (tree,node){
 var parentNode = node.getParentNode();
 if (parentNode==null || parentNode.nodes.length >1){
	tree.removeNode(node);
 }else{
	// 如果父节点下未删除之前只有一个子节点,则将父节点也删除
	removeNodes(tree,parentNode);
 }
}



/**
* 展开节点并通过ajax查询柜台
* @param treeId
* @param treeNode
* @return
*/
function expendTreeNodesByAjax (treeId, treeNode){
if (treeNode.nodes!=null){
	var length = treeNode.nodes.length;
	if (length==0){
		return false;
	}else if (length==1){
		var node = treeNode.nodes[0];
//		alert(JSON.stringify(node));
		if (node.name=="artificialCounter"){
			getCounterByAjax(treeNode);
			return false;
		}else{
			return true;
		}
	}
}else {
	return false;
}
}

/**
* 通过Ajax取得柜台
* @param treeNode
* @return
*/
function getCounterByAjax(treeNode){
	if (typeof treeNode.nodes == "undefined"){
		return;
	}
	var node = treeNode.nodes[0];
	//var index = getTimeLocationIndex(); 
	var index = 0; 
	var locationType = binOLPTJCS17_global.locationTypeArr[index];
//	alert(locationType);
	if (locationType == "2"){
		var param = "cityID="+treeNode.id;
	}else if (locationType == "4"){
		var param = "channelID="+treeNode.id;
	}
	
	var name = treeNode.name;
	// 取得分配地点所在的分组索引
	//var index = getTimeLocationIndex(); 
	var index = 0; 
	// 取得树对象
	if (treeNode.tId.indexOf('treeLeft')>=0){
		var tree= binOLPTJCS17_global.leftTreeTmp;
		param = param + "&treeType=leftTree";
	}else {
		var tree= binOLPTJCS17_global.rightTreeTmp;
		param = param + "&treeType=rightTree";
	}
	
	if ((!binOLPTJCS17_global.locationFlg) && $('#activeID')!=null && $('#activeID').val()!=undefined){
		param = param + "&activeID="+$('#activeID').val();
	}
	if ($('#activitySetBy')!=null && $('#activitySetBy').val()!=undefined){
		param = param + "&activitySetBy="+$('#activitySetBy').val();
	}
	if ($('#showType')!=null && $('#showType').val()!=undefined){
		param = param + "&showType="+$('#showType').val();
	}
	//var index = getTimeLocationIndex();
	var index = 0;
	param = param + "&grpID="+(parseInt(index)+1);
	if (treeNode.name.indexOf("loading...")<0){
		treeNode.name +="(loading...)";
	}
	tree.updateNode(treeNode);
	
	url = $('#getCounterDetailByAjaxUrl').html();
	var reloadFlg;
	if (binOLPTJCS17_global.positionName !=""){
		reloadFlg:false;
	}else{
		reloadFlg:true;
	}
	cherryAjaxRequest({
		url:url,
		param:param,
		callback:expendTreeNodesCallBack,
		formId:"#prmActSearchform",
		reloadFlg:reloadFlg
	});
	
	function expendTreeNodesCallBack(data){
		var dataArr = eval('('+data+')');
		if (binOLPTJCS17_global.positionName !=""){
			var skipFlag = false;
			for (var i=0;i<dataArr.length;i++){
				var dataObj = dataArr[i];
				if (dataObj.name == binOLPTJCS17_global.positionName){
					skipFlag = true;
					break;
				}
			}
			var selectNode = tree.getNodeByParam("name",binOLPTJCS17_global.positionName+"(loading...)");
			if (!skipFlag && selectNode==null){
				treeNode.name = name;
				tree.updateNode(treeNode);
				binOLPTJCS17_global.positionName = "";
				return ;
			}
		}
		tree.removeNode (node);
		if(treeNode.checked){
			var length = dataArr.length;
			for(var i = 0 ; i < length ; i++){
				dataArr[i].checked = true;
			}
		}
		tree.addNodes(treeNode,dataArr);
		tree.refresh();
		tree.expandNode(treeNode, true, false);
		treeNode.name = name;
		tree.updateNode(treeNode);
		if (binOLPTJCS17_global.positionName !=""){
			var selnode = tree.getNodeByParam("name",binOLPTJCS17_global.positionName);
			tree.selectNode(selnode);
			binOLPTJCS17_global.positionName = "";
		}
	}

}

/**
 * 分配地点定位绑定
 * @param index
 * @return
 */
function locationPositionBindingOld(index){
	var url = $('#indSearchPrmLocationUrl').html()+"?csrftoken=" +$("#csrftoken").val();
	$('#location_dialog_'+index+' .locationPosition').autocompleteCherry(url,{
			extraParams:{
			searchPrmLocation: function() { 
				var obj = $(document.activeElement);
				return obj.val();
			},
			locationType:function() { return binOLPTJCS17_global.locationTypeArr[index];}
		},
		minChars:1,
	    matchContains:1,
	    matchContains: true,
	    scroll: false,
	    cacheLength: 0,
	    max:50,
	    width:200,
	    loadingClass: "ac_loading",
		formatItem: function(row, i, max) {
			if (row[2] == "counter"){
				return escapeHTMLHandle(row[1])+" "+"["+escapeHTMLHandle(row[0])+"]";
			}else if (row[2] == "channel"){
				return "["+escapeHTMLHandle(row[0])+"]";
			}else if (row[2] == "region"){
				return "["+escapeHTMLHandle(row[0])+"]";
			}
		}
	});
}

/**
 * 分配地点定位绑定
 * @param index
 * @return
 */
function locationPositionBinding(index){
	var url = '';
	var extraParams = {};
	var $selectVal = $("#sel_location_type_0").val();
//	alert($selectVal);
	if($selectVal=='2'||$selectVal=='4'||$selectVal=='6'){//柜台绑定
		url = "/common/BINOLCM21_getCounterInfo.action";
		extraParams = {
			counterInfoStr: function() { 
				var obj = $(document.activeElement);
				return obj.val();
			},
			number: 20,
			//默认是选中柜台名称
			selected: "name",
			brandInfoId:function() { return $('#brandInfoId').val();},
			privilegeFlag: 0
		};
	}else if($selectVal=='1'){//区域绑定
		url = "/common/BINOLCM21_getRegionInfo.action";
		extraParams = {
			regionInfoStr: function() { 
				var obj = $(document.activeElement);
				return obj.val();
			},
			//默认是最多显示50条
			number: 20,
			//默认是选中区名称
			selected: "name"
		};
	}else if($selectVal=='3'){//渠道绑定
		url = "/common/BINOLCM21_getChannelInfo.action";
		extraParams = {
			channelInfoStr: function() { 
				var obj = $(document.activeElement);
				return obj.val();
			},
			//默认是最多显示50条
			number: 20,
			// 默认是选中渠道名称
			selected: "name"
		};
	}
	
	var strPath = window.document.location.pathname;
	var postPath = strPath.substring(0, strPath.substr(1).indexOf('/') + 1);
	url = postPath + url + "?csrftoken=" +$("#csrftoken").val();
//	alert(url);
	$('#location_dialog_'+index+' .locationPosition').autocompleteCherry(url,{
		extraParams: extraParams,
		
		minChars:1,
	    matchContains:1,
	    matchContains: true,
	    scroll: false,
	    cacheLength: 0,
	    max:50,
	    width:200,
	    loadingClass: "ac_loading",
		formatItem: function(row, i, max) {
//			alert(row);
			if($selectVal=='2'||$selectVal=='4'||$selectVal=='6'){//柜台绑定
					return escapeHTMLHandle(row[1])+" "+"["+escapeHTMLHandle(row[0])+"]";
			}else if($selectVal=='1'){//区域绑定
					return escapeHTMLHandle(row[0])+" "+ (row[1] ? "["+escapeHTMLHandle(row[1])+"]" : "");
			}else if($selectVal=='3'){//渠道绑定
					return escapeHTMLHandle(row[0]);
			}
			
		}
	});
}

/**
 * 方案分配地点定位
 * @param treeType
 * @return
 */
function locationPosition(treeType){
//	var index = getTimeLocationIndex(); 
	var index = 0 
	if (treeType == "leftTree"){
		var tree= binOLPTJCS17_global.leftTreeTmp;
	}else if (treeType == "rightTree"){
		var tree= binOLPTJCS17_global.rightTreeTmp;
	}
	
	if (treeType == "leftTree"){
		var positionName = $('#position_left_'+index).val();
	}else{
		var positionName = $('#position_right_'+index).val();
	}
	
	if (positionName==""){
		return;
	}
	binOLPTJCS17_global.positionName = positionName;

	if (binOLPTJCS17_global.locationType == "1" || binOLPTJCS17_global.locationType == "3"){
		var node = tree.getNodeByParam("name",positionName);
		tree.selectNode(node);
	}else if (binOLPTJCS17_global.locationType == "2" || binOLPTJCS17_global.locationType == "4" || binOLPTJCS17_global.locationType == "6"){
		var node = tree.getNodeByParam("name",positionName);
		if (node!=null){
			var childNodes = node.nodes;
			if (childNodes!=null && childNodes.length==1 && childNodes[0].name == 'artificialCounter'){
				getCounterByAjax(node);
			}else{
				tree.selectNode(node);
				binOLPTJCS17_global.positionName = "";
			}

		}else{
			var url = $('#getCounterParentUrl').html();
			var param = "counterName="+positionName;
			cherryAjaxRequest({
				url:url,
				param:param,
				callback:loadParentNode,
				formId:"#prmActSearchform"
			});
		}
	}
	
	function loadParentNode (data){
		var resultData = eval('('+data+')');
		if (resultData!=null){
			if (binOLPTJCS17_global.locationType == "2"){
				var node = tree.getNodeByParam("id",resultData.cityID);
			}else if (binOLPTJCS17_global.locationType == "4"){
				var node = tree.getNodeByParam("id",resultData.channelID);
			}
			if (node!=null){
				if (node.nodes.length>0 && node.nodes[0].name != 'artificialCounter'){
					var treeNode = tree.getNodeByParam("name",positionName);
					if (treeNode!=null){
						// 该节点已经被读取
						tree.selectNode(treeNode);
					}else{
						// 取消选中
						tree.cancelSelectedNode();
					}

				}else{
					getCounterByAjax(node);
				}
				
			}
		}
	}
	
	/**
	 * textarea字符长度截取
	 * @param o
	 * @return
	 */
	function isMaxLen(o){  
		 var nMaxLen=o.getAttribute? parseInt(o.getAttribute("maxlength")):"";  
		 if(o.getAttribute && o.value.length>nMaxLen){  
			 o.value=o.value.substring(0,nMaxLen);  
		 }  
	}
}

/**
 * 取得叶子节点
 * @param nodes
 * @param leafNodes
 * @returns
 */
function getLeafNodes(nodes,leafNodes){
	for (var i=0;i<nodes.length;i++){
		var node = nodes[i];
		if(isEmpty(node.nodes)){
			leafNodes.push(node);
		}else{
			getLeafNodes(node.nodes,leafNodes);
		}
	}
	return leafNodes;
}



var BINOLPTJCS17 = function(){};
BINOLPTJCS17.prototype={
		/**
		 * 产品实时下发确认DIV
		 * 
		 * 
		 * */
		"issuedInit":function()
		{
			// 清空提示消息
			cleanMsgDIV();
			var _this = this;
			var text = "";
			var title = "";
//			text = '<p class="message"><span>'+$('#confirIsEnable').text();
			text = '<p class="message"><span>' + $("#issCntPrtMsg1").text() + '</span></p>';
//			title = $('#enableValTitle').text();
			title = $("#issCntPrt").text();
			var dialogSetting = {
					dialogInit: "#dialogIssuedInitDIV",
					text: text,
					width: 	300,
					height: 200,
					title: 	title,
					confirm: $("#dialogConfirmIss").text(),
					cancel: $("#dialogCancelIss").text(),
					confirmEvent: function(){
						_this.issuedPrt();
//						removeDialog("#dialogIssuedInitDIV");
					},
					cancelEvent: function(){removeDialog("#dialogIssuedInitDIV");}
			};
			openDialog(dialogSetting);
		},
		"issuedPrt":function(){
			var url=$("#issuedPrtId").html();
			
			var param ="brandInfoId=" + $("#brandInfoId").val();
			param += "&csrftoken=" +$("#csrftoken").val();
			
			// 清空按钮
			$("#dialogIssuedInitDIV").dialog( "option", {
				buttons: []
			});
			$("#dialogIssuedInitDIV").html('').append($("#issLaunchingMsg").text());
			
//			$("#dialogIssuedInitDIV").html('').append("处理中...");
			cherryAjaxRequest({
				url: url,
				param: param,
				callback: function(msg) {
					var map = eval("(" + msg + ")");
					if(map.result == "0"){
//						$("#dialogIssuedInitDIV").empty();
						// 显示结果信息
						var ht = $("#operateSuccessId").clone();
						$("#dialogIssuedInitDIV").html('').append(ht);
					}else{
//						$("#dialogIssuedInitDIV").empty();
						// 显示结果信息
						var ht= $("#operateFaildId").clone();
						$("#dialogIssuedInitDIV").html('').append(ht);
						
					}
					$("#dialogIssuedInitDIV").dialog( "option", {
						buttons: [{
							text: $("#dialogConfirm").text(),
						    click: function(){removeDialog("#dialogIssuedInitDIV");}
						}]
					});
//					removeDialog("#dialogIssuedInitDIV");
				}
			});
		},
		// 改变方案
		"changeSolu" : function(_this){
			
			var that = this;
			var $this = $(_this);
			
			if($(_this).val() == ''){
//				$("#soluMsg").hide();
			} else{
				var list = JSON.parse($("#prtPriceSolutionListId").val());
				$.each(list, function(i,item){
//				alert(JSON.stringify(item));
					if($(_this).val() == item.solutionID){
//						if(item.isSoluDetailFlag == 'Y'){
//							$("#soluMsg").show();
//						}else{
//							$("#soluMsg").hide();
//						}
						
						// 生效开始日期
						if(item.startDate){
							$("#soluStartDate").val(item.startDate);
						}
						// 生效结束日期
						if(item.endDate){
							$("#soluEndDate").val(item.endDate);
						}
												
						$("#solutionName").val(item.solutionName);
						
					}
				});
				
			}
		},
		
		// 取得错误信息HTML
		"getErrHtml" : function (text){
			var errHtml = '<div class="actionError"><ul><li><span>';
			errHtml += text;
			errHtml += '</span></li></ul></div>';
			return errHtml;
		},
	    // 清空错误信息
	    "deleteActionMsg" : function (){
	    	$('#errorMessage').empty();
	    	$('#errorDiv #errorSpan').html("");
	    	$('#successDiv #successSpan').html("");
			$('#errorDiv').hide();
			$('#successDiv').hide();
	    },
	    "formValid" : function(){
	    	var flag = true;
	    	var $form = $("#mainForm");
	    	// 表单验证
	    	flag = $form.valid();
	    	//批次号验证
	    	if($('#isChecked').attr("checked") == undefined ){
	    		var importBatchCode = $('#importBatchCode').val();
	    		var reg = /^[A-Za-z0-9]+$/i;
	    		var errorMsg = '';
	    		if(importBatchCode == undefined || importBatchCode == ''){
	    			errorMsg = $('#errmsg2').val();
	    		}else if(importBatchCode.length > 25){
	    			errorMsg = $('#errmsg3').val();
	    		}else if(!reg.test(importBatchCode)){
	    			errorMsg = $('#errmsg4').val();
	    		}
	    		if(errorMsg != ''){
	    			var $obj = $("#importBatchCodeSpan");
	    			$obj.removeClass('error');
	    			$obj.find("#errorText").remove();
	    			$obj.addClass("error");
	    			$obj.append('<span class="ui-icon icon-error tooltip-trigger-error" id="errorText"></span>');
	    			$obj.find('#errorText').attr("title",'error|'+errorMsg);
	    			$obj.find('#errorText').cluetip({
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
	    			flag = false;
	    		}
	    	}
			return flag;
	    },
	    // AJAX文件上传
	    "ajaxCntFileUpload" : function (){
	    	var index = 0;
	    	var $that=this;
	    	var url = $("#importCounterUrl").text();
//	    	$that.deleteActionMsg();
	    	// AJAX登陆图片
	    	$ajaxLoading = $("#loading");
	    	// 错误信息提示区
	    	$errorMessage = $('#errorMessage');
	    	
	    	// 信息提示区
	    	var $message = $('#actionResultDisplay_' + index);
	    	// 错误信息提示区
	    	var $errorDiv = $('#errorDiv_' + index);
	    	$errorDiv.hide();
	    	$message.hide();
	    	
	    	$("#locationType").val(6);
	    	if($('#cntFile_'+index).val()==''){
	    		$errorDiv.find("#errorSpan").text(BINOLSSPRM13_js_i18n.empty_err);
	    		$errorDiv.show();
	    		return false;
	    	}
	    	
	    	$ajaxLoading.ajaxStart(function(){$(this).show();});
	    	$ajaxLoading.ajaxComplete(function(){$(this).hide();});
	    	
	    	// 导入按钮
	    	var $excelBtn = $("#upload");
	    	// 禁用导入按钮
	    	$excelBtn.prop("disabled",true);
	    	$excelBtn.addClass("ui-state-disabled");
	    	
	    	$.ajaxFileUpload({
	    		url: url,
	    		secureuri:false,
	    		data:{'csrftoken':$("#csrftoken").val(),'brandInfoId':$('#brandInfoId').val()},
	    		fileElementId: 'cntFile_'+index,
	    		dataType: 'html',
	    		success: function (msg){
//		        	alert(msg);
	    			//释放按钮
	    			$excelBtn.removeAttr("disabled",false);
	    			$excelBtn.removeClass("ui-state-disabled");
	    			
		        	$message.html(msg);
		        	if($message.find("#actionResultDiv").length > 0){
		        		$message.show();
		        	}else if (window.JSON && window.JSON.parse) {
						var msgJson = window.JSON.parse(msg);
						var counterNodes = msgJson['trueCounterList'];
						if(msgJson['errorMsg'] != undefined){
							$errorDiv.find("#impErrorSpan").text(msgJson['errorMsg']);
							$errorDiv.show();
//							alert("error");
						}else{
							$errorDiv.hide();
						}
						
////						alert("rightTree:  " + JSON.stringify(binOLPTJCS17_global.rightTreeTmp));
//						if(isEmpty(binOLPTJCS17_global.rightTreeTmp) || binOLPTJCS17_global.rightTreeTmp.getNodes().length == 0){
//							alert(1 + '   ' + JSON.stringify(counterNodes));
//							// 柜台节点加载到树
////							binOLPTJCS17_global.rightTreeTmp = $.fn.zTree.init($('#treeRight_'+index),binOLPTJCS17_global.zTreeSetting,null);
////							binOLPTJCS17_global.rightTreeTmp = $.fn.zTree.init($('#treeRight_'+index),binOLPTJCS17_global.zTreeSetting,counterNodes);
//						}else{
//							alert(2 + '   ' + JSON.stringify(counterNodes));
////							binOLPTJCS17_global.rightTreeTmp = $.fn.zTree.init($('#treeRight_'+index),binOLPTJCS17_global.zTreeSetting,null);
////							that.addNodes(binOLPTJCS17_global.rightTreeTmp, counterNodes, false);
//						}
						binOLPTJCS17_global.rightTreeTmp = $.fn.zTree.init($('#treeRight_'+index),binOLPTJCS17_global.zTreeSetting,counterNodes);
						
						
						// 检查导入的柜台地点是否已被其他方案分配过 chkFlag 检查途径（树转移:left2right、导入：import）
						var rightTree = $.fn.zTree.getZTreeObj("treeRight_0");
						var nodes = rightTree.getNodes();
						if(!isEmpty(nodes) && nodes.length != 0){
							var locationArr = new Array();
							
				    		for (var i=0;i<nodes.length;i++){
				    			var node = nodes[i];
				    			locationArr.push (node.id);
				    		}
				    		if(locationArr.length != 0){
				    			chkAllocationPlaceConflict('import', locationArr, 6);
				    		}
						}
						
		        	}
	    		}
	    	});
	    },
	    "addNodes" : function(treeObj,newNodes,repFlag){
	    	var oldNodes = treeObj.getNodes();
	    	var tempNodes = [];
	    	if(!repFlag){
	    		for (var i=0;i < newNodes.length; i++) {
	    			var isContain = false;
	    			for(var j=0;j < oldNodes.length; j++){
	    				if(oldNodes[j].id == newNodes[i].id){
	    					isContain = true;
	    					break;
	    				}
	    			}
	    			if(!isContain){
	    				newNodes[i].iconSkin = "newAdd";
	    				tempNodes.push(newNodes[i]);
	    			}
	    		}
	    	}else{
	    		tempNodes = newNodes;
	    	}
	    	if(tempNodes.length > 0){
	    		treeObj.addNodes(null, tempNodes);
	    	}
	    },
	    // AJAX文件上传
	    "ajaxFileUpload" : function (url){
	    	var $that=this;
	    	$that.deleteActionMsg();
	    	// AJAX登陆图片
	    	$ajaxLoading = $("#loading");
	    	// 错误信息提示区
	    	$errorMessage = $('#errorMessage');
	    	// 清空错误信息
	    	$errorMessage.empty();
	    	var $form = $("#mainForm");
//	    	$('#importBatchCode').val($.trim($('#importBatchCode').val()));
//	    	$('#comments').val($.trim($('#comments').val()));
	    	//表单验证
//	    	if(!$that.formValid()){
//	    		return false;
//	    	}
			if(!$('#mainForm').valid()) {
				return false;
			}
	    	if($('#upExcel').val()==''){
	    		var errHtml = $that.getErrHtml($('#errmsg1').val());
	    		$errorMessage.html(errHtml);
	    		$("#pathExcel").val("");
	    		return false;
	    	}
	    	$ajaxLoading.ajaxStart(function(){$(this).show();});
	    	$ajaxLoading.ajaxComplete(function(){$(this).hide();});;
	    	// 导入按钮
	    	var $excelBtn = $("#upload");
	    	// 禁用导入按钮
	    	$excelBtn.prop("disabled",true);
	    	$excelBtn.addClass("ui-state-disabled");
	    	$.ajaxFileUpload({
		        url: url,
		        secureuri:false,
		        data:{
		        		'csrftoken':$('#csrftoken').val(),
		        		'brandInfoId':$('#brandInfoId').val(),
		        		'startTime':$("#soluStartDate").val(),
		        		'endTime':$("#soluEndDate").val(),
		        		'solutionName':$("#solutionName").val(),
		        		'productPriceSolutionID':$("#productPriceSolutionID").val(),
		        	},
		        fileElementId:'upExcel',
		        dataType: 'html',
		        success: function (msg){
//		        	alert(msg);
		        	//释放按钮
		        	$excelBtn.removeAttr("disabled",false);
					$excelBtn.removeClass("ui-state-disabled");
		        	$('#errorMessage').html(msg);
		        	var errorCount = $('#fFailed').html();
		        	if( errorCount != null && errorCount !="" && errorCount != 0){
		        		$('#errorSpan').html($('#failedResult').html());
		        		$('#errorDiv').show();
		        	}if(errorCount == 0){
		        		$('#successSpan').html($('#successResult').html());
		        		$('#successDiv').show();
		        	}
//		        	if(window.opener.oTableArr[0] != null)window.opener.oTableArr[0].fnDraw();
		        }
	        });
	    },
	    "close":function(){
	    	window.close();
		},
		"isChecked":function(thisObj){
			if(thisObj.checked){
				$('#importBatchCodeSpan').hide();
			}else{
				$('#importBatchCodeSpan').show();
			}
		},
		
	    "changeBrand":function(){
	    	var url = $("#getDownloadPathByAjaxURL").attr("href");
	    	var params = "brandInfoId="+$("#brandInfoId").val();
			var callback = function(msg){
				var jsons = eval("("+msg+")");
				var downloadURL = $("#downloadURL").attr("href");
				downloadURL = downloadURL.substring(0,downloadURL.indexOf("/download"))+jsons.downloadURL;
				$("#downloadURL").attr("href",downloadURL);
			};
			
			cherryAjaxRequest({
				url:url,
				reloadFlg:true,
				param:params,
				callback:callback
			});
	    }
};

var ptjcs17 = new BINOLPTJCS17();
$(document).ready(function() {
	// 地点定位
//	locationPositionBinding(0);
	changeSolu("#productPriceSolutionID");
});

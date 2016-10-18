window.onbeforeunload = function(){	
	
//	// 清除session中的操作品牌id的值
//	var url = $('#delCurrentBrandInfoIDUrl').html();
//	cherryAjaxRequest({
//		url:url
//	});
	if (binOLSSPRM13_global.needUnlock) {
		if (window.opener) {		
			window.opener.unlockParentWindow();	
		}
	}
};			

/**
 * 全局变量定义
 */
var binOLSSPRM13_global = {};
// 当前点击对象
binOLSSPRM13_global.thisClickObj = {};
// ul HTML 
binOLSSPRM13_global.ulHTML = '<ul class = "items">\n';
// li HTML
binOLSSPRM13_global.liHTML = '<li class = "item">\n';
// 促销奖励
binOLSSPRM13_global.rewardType ="rewardProduct";
// 添加活动组弹出框内容
binOLSSPRM13_global.dialogHTML = '';
// z-Tree 设置
//binOLSSPRM13_global.zTreeSetting ={checkable : true,showLine : true,expandSpeed : "",callback:{beforeExpand: expendTreeNodesByAjax}};
binOLSSPRM13_global.zTreeSetting = {
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
binOLSSPRM13_global.leftTreeArr =new Array (null);
// 右边树数组
binOLSSPRM13_global.rightTreeArr = new Array (null);
// 促销活动地点类型数组
binOLSSPRM13_global.locationTypeArr = new Array(null);
// 左边树Tmp
binOLSSPRM13_global.leftTreeTmp;
// 右边树Tmp
binOLSSPRM13_global.rightTreeTmp;
// 时间地点提交数据
binOLSSPRM13_global.timeLocationDataArr = new Array ({});
// 日历起始日期
binOLSSPRM13_global.calStartDate="";
// 假日
binOLSSPRM13_global.holidays="";
// 活动地点区分
binOLSSPRM13_global.locationType = "";
// 活动地点关联已选择活动地点Flag
binOLSSPRM13_global.locationFlg = false;
// 目标节点
binOLSSPRM13_global.targetNode=null;
// 定位名称
binOLSSPRM13_global.positionName ="";
//是否需要解锁
binOLSSPRM13_global.needUnlock = true;
//画面显示类型
binOLSSPRM13_global.showType = "";

/**
 * 促销活动新增(BINOLSSPRM13)页面初期处理
 */

$(document).ready(function() {
	

	if (window.opener){
		// popup层覆盖
		window.opener.lockParentWindow();
	}


	initDetailTable();

	//IE圆角修复 & IE表单交互
	if((navigator.userAgent.indexOf('MSIE') >= 0) && (navigator.userAgent.indexOf('Opera') < 0))
	{	
		$(":text,textarea").focus(function(){
			$(this).addClass('input-focus');
		});
		$(":text,textarea").blur(function(){
			$(this).removeClass('input-focus');
		});
		
	}
	
	// 初期条件位置选中当前条件
	$('#con_this .radio').attr('checked',true);
	
//	// 选择产品
//	$('#selectProducts').click(function(){
//		selectPruducts();
//	});
	
	// 点击逻辑运算符
	$('#logicOperator li').click(function(){
		var val = $(this).html();
		selectLogicOperator(val);
	});
	
	$('#box2-content span.add').click(function(){
		var opos = $(this).offset();
		var oleft = parseInt(opos.left, 10);
		var otop = parseInt(opos.top + $(this).outerHeight(), 10);
		$('.ui-option-add-2').css({'left': oleft + "px", 'top': otop }).show().click(function(){
			$(this).hide();
		});
	});
	
	// 确定编辑逻辑条件box
	$('#logicBoxOK').click(function(){
		editLogicBoxOK();
	});
	
	// 取消编辑逻辑条件box
	$('#logicBoxCancle').click(function(){
		$('#editLogicBox').hide();
	});
	
	// 关闭条件Box
	$('#boxCancle').click(function(){
		$('#conditionBox').hide();
	});
	
	// 设定金额值
	$('#options-1').click(function(){
		setAmountValue();
	});
	
	// 显示金额范围
	$('#options-2').click(function(){
		openAmountScope($(this));
	});
	
	// 设定金额范围
	$('#amountScopeOK').click(function(){
		setAmountScope();
	});
	
	// 取消金额范围
	$('#amountScopeCancle').click(function(){
		$('#amountBox').hide();
		$('#amountScope').hide();
	});
	
	// 关闭促销奖励box
	$('#rewardOK').click(function(){
		closeRewardBox();
	});
	
	// 促销奖励box取消
	$('#rewardCancle').click(function(){
		$('#rewardBox').hide();
	});
	
	// 减少奖励消费金额
	$('#rewardOptions-1').click(function(){
		rewardAmount('decrease');
	});
	
	// 增加奖励消费金额
	$('#rewardOptions-2').click(function(){
		rewardAmount('increase');
	});
	
	// 添加活动组
	$('#addActGrp').click(function(){
		if("" == binOLSSPRM13_global.dialogHTML){
			binOLSSPRM13_global.dialogHTML = $('#prm_act_grp_dialog_Main').html();
		}else{
			$('#prm_act_grp_dialog_Main').html(binOLSSPRM13_global.dialogHTML);
		}
		
		$('#prm_act_grp_dialog .endTime').cherryDate({
			holidayObj: binOLSSPRM13_global.holidays,
			minDate:binOLSSPRM13_global.calStartDate,
			// 结束时间大于起始时间
			beforeShow: function(input){										
				var value = $(input).parent().parent().prev().find(".startTime").val();	
				return [value,'minDate'];									
			}
		});
		
		$('#prm_act_grp_dialog .startTime').cherryDate({
			holidayObj: binOLSSPRM13_global.holidays,
			// 日历的最小时间	
			minDate:binOLSSPRM13_global.calStartDate
		});
		$('#prm_act_grp_dialog').dialog({ autoOpen: false,  width: 350, height: 250, title:BINOLSSPRM13_js_i18n.addPrmActiveGrp, zIndex: 1,  modal: true, resizable:false,
			
			buttons: [{
				text:BINOLSSPRM13_js_i18n.ok,
				click: function() {
					addActiveGroup();
				}
			},
			{	
				text:BINOLSSPRM13_js_i18n.cancle,
				click: function() {
					$("#prm_act_grp_dialog").dialog("destroy");
					 $("#prm_act_grp_dialog").remove();
				}
			}],
			close: function() {
				$("#prm_act_grp_dialog").dialog("destroy");
				 $("#prm_act_grp_dialog").remove();
			}
		});
		$("#prm_act_grp_dialog").dialog("open");
	});
	
	// 设置box中的选中取消
	$('#conditionBox li,#rewardBox li,#editLogicBox li').click(function(event){
		conditionLiClick(event);
	});
	
	
	// 品牌选择框onchange事件
	$('#brandInfoId').change(function(){
		var url = $('#BINOLSSPRM13Url').html()+"?brandInfoId="+$('#brandInfoId').val()+"&"+getParentToken('prmActiveform')+"&initFlag=1";
		binOLSSPRM13_global.needUnlock = false;
		window.location.href = url;
	});

	
	
	cherryValidate(
	{
		formId:'prmActiveform',
		rules:
		{
			prmActGrp:{required: true},
			//productCount:{required: true,digits:true},
			//prmPresentCount:{required: true,digits:true},
			//amountVal:{required: true,digits:true},
			prmActiveName:{required: true,maxlength:20},
			startTime:{required: true,dateValid:true},
			endTime:{required: true,dateValid:true},
			quantity:{maxlength:8},
			exPoint:{maxlength:8},
			allExPoint:{maxlength:8}
			//prmRewardAmount: {required: true,digits:true}
		}
	});	
	// 设置活动类型
	PRM13.setActivityType();
});

/**
 * 日历初始化事件绑定
 * @return
 */
function calEventBind(){
	$('#condition_body .endTime').cherryDate({
		holidayObj: binOLSSPRM13_global.holidays,
		// 结束时间大于起始时间
		beforeShow: function(input){										
			var value = $(input).parent().parent().prev().find(".startTime").val();	
			return [value,'minDate'];									
		}	
	});
	
	
	$('#condition_body .startTime').cherryDate({
		holidayObj: binOLSSPRM13_global.holidays,
		beforeShow: function(input){										
			var value = $(input).parent().parent().next().find(".endTime").val();	
			return [value,'maxDate'];									
		}		
	});
}

/**
 * 日历选中事件
 * @param dateText
 * @param inst
 * @return
 */
function calSelect(dateText, inst){
	var $input = inst.input;

	// 选择了开始时间
//	if ($input.attr('class').indexOf('startTime')>0){
//		$endTime = $($input.parents('.time_box').first().find('.endTime')[0]);
//		if ($endTime.val()==""){
//			$endTime.val(dateText);
//		}
//	}
//	
//	if ($input.attr('class').indexOf('endTime')>0){
//		$startTime = $($input.parents('.time_box').first().find('.startTime')[0]);
//		if ($startTime.val()==""){
//			$startTime.val(dateText);
//		}
//	}
	
	// 选择了开始时间
	if ($input.attr('name') == 'startTime'){
		var startDateStrArr = $input.val().split("-");
		var endDate =  new Date(parseInt(startDateStrArr[0],10),parseInt(startDateStrArr[1],10),parseInt(startDateStrArr[2],10));
		var endDateStr = changeDateToString(endDate);
		$input.parents(".time_box").first().find(':input[name="endTime"]').val(endDateStr);
	}
}


/**
 * 添加时间地点
 * @param thisObj
 * @return
 */
function addTimeLocation(){
	var $last_box = $('#condition_body .time_location_box').last();
	var htmlTmpArr = new Array();
	getTimeLocationHTML(htmlTmpArr);
	$last_box.after(htmlTmpArr.join(''));
	//日历初始化事件绑定
	calEventBind();
	$('#condition_body .remove_time_location').show();
	// 在地点数组中添加对象
	binOLSSPRM13_global.leftTreeArr.push(null);
	binOLSSPRM13_global.rightTreeArr.push(null);
	binOLSSPRM13_global.locationTypeArr.push(null);
	// 添加时间地点提交数据对象
	var timeLocationData = {};
	binOLSSPRM13_global.timeLocationDataArr.push(timeLocationData);
	
}


/**
 * 删除时间地点
 * @param thisObj
 * @return
 */
function removeTimeLocation(thisObj){
	var index = $('#condition_body .time_location_box').length - $($(thisObj).parents('.time_location_box')[0]).nextAll('.time_location_box').length;
	binOLSSPRM13_global.timeLocationDataArr = arr_del(binOLSSPRM13_global.timeLocationDataArr,index);
	
	binOLSSPRM13_global.locationTypeArr = arr_del(binOLSSPRM13_global.locationTypeArr,index);
	binOLSSPRM13_global.leftTreeArr = arr_del(binOLSSPRM13_global.leftTreeArr,index);
	binOLSSPRM13_global.rightTreeArr = arr_del(binOLSSPRM13_global.rightTreeArr,index);
	var $this_box = $(thisObj).parents('.time_location_box').first();
	$this_box.remove();
	if ($('#condition_body .remove_time_location').length==1){
		$('#condition_body .remove_time_location').hide();
	}
	
	
}

/**
 * 添加活动时间
 * @return
 */
function addActiveTime(thisObj){
	var $div_end = $(thisObj);
	var htmlTmpArr = new Array();
	getTimeHTML(htmlTmpArr);
	$div_end.append (htmlTmpArr.join(''));	
	//日历初始化事件绑定
	calEventBind(); 
	PRM13.initTime();
	$div_end.find('.close').each(function(){
		$(this).show();
	});
}

/**
 * 删除活动时间
 * @param thisObj
 * @return
 */
function removeTime(thisObj){
	var $div_end = $($(thisObj).parents('.start').first().find('.end')[0]);
	// 删除本行时间
	$(thisObj).parents('.clearfix').first().remove();
	var close_arr = $div_end.find('.close');
	if (close_arr.length==1){
		close_arr.each(function(){
			$(this).hide();
		});
	}
}

/**
 * 取得时间地点HTML
 * @param htmlTmpArr
 * @return
 */
function getTimeLocationHTML(htmlTmpArr){
	htmlTmpArr.push('<div class="box2 box2-active time_location_box">');
	htmlTmpArr.push('<div class="box2-header clearfix">');
	htmlTmpArr.push('<strong class="left active"><span class="ui-icon icon-clock"></span>'+BINOLSSPRM13_js_i18n.activeTime+'<span class="highlight">*</span></strong><strong class="left location" onclick="openLocation(this);"><span class="ui-icon icon-flag"></span>'+BINOLSSPRM13_js_i18n.activeLocation+'<span class="highlight">*</span></strong>');
	htmlTmpArr.push('<a style="margin-left: 8em; margin-top: 0.2em;" class="add left" onclick ="addTimeLocation();"><span class="ui-icon icon-add"></span><span class="button-text">'+BINOLSSPRM13_js_i18n.timeLocatin+'</span></a>');
	htmlTmpArr.push('<a style="display:none; margin-top: 0.2em;" class="add left remove_time_location" onclick="removeTimeLocation(this);"><span class="ui-icon icon-delete"></span><span class="button-text">'+BINOLSSPRM13_js_i18n.removeLocatin+'</span></a></div>');
	htmlTmpArr.push('<div class="box2-content start clearfix" style="padding:.5em 1.5em 0 1.5em; _padding:0 1.5em;">');
	htmlTmpArr.push('<div class="clearfix"><a class="add right" onclick ="addActiveTime(this);"><span class="ui-icon icon-add"></span><span class="button-text">'+BINOLSSPRM13_js_i18n.activeTime+'</span></a></div>');
	htmlTmpArr.push('<div class="end">');
	getTimeHTML(htmlTmpArr);
	htmlTmpArr.push('</div></div></div>');
}


/**
 * 取得时间HTML
 * @param htmlTmpArr
 * @return
 */
function getTimeHTML(htmlTmpArr){
	htmlTmpArr.push('<div class="clearfix time_box">');
	htmlTmpArr.push('<p class="column"><span><label>'+BINOLSSPRM13_js_i18n.startTime+'</label><input type="text" class="date startTime" name="startTime"/> <label><input class="date time" name="startTime2" type="text" value="00:00:00"/></label></span></p>');
	htmlTmpArr.push('<p class="column"><span><label>'+BINOLSSPRM13_js_i18n.endTime+'</label><input type="text" class="date endTime" name="endTime"/> <label><input class="date time" name="endTime2" type="text" value="23:59:59"/></label></span></p>');
	htmlTmpArr.push('<span class="close left mt1 hide" id="removeTime" style="display:none"><span class="ui-icon ui-icon-close" onclick="removeTime(this);"></span></span></div>');
}


/**
 * 保存促销规则
 * @return
 */
function savePrmActiveRule (url,templateFlag){
	// 设定奖励金额类型(增加还是减少)
//	if ($('#proRewardContent').find('.reward-amount-decrease').length >0){
//		$('#amountType').val('-1');
//	}else if ($('#proRewardContent').find('.reward-amount-increase').length >0){
//		$('#amountType').val('1');
//	}
	// 是否作为模版保存
	$('#templateFlag').val(templateFlag);
	if (!$('#prmActiveform').valid()){
		return ;
	}
   
	// 详细,编辑的情况下,如果没有打开地点box,需要做一遍树的初期化来取得数据
	if ($('#activeID').length !=0 && $('#activeID').val()!=undefined){
		var timeLocationBox_count = $('#prmActiveform').find('.time_location_box').length;
		for (var i=0;i<timeLocationBox_count;i++){
			// 如果没有加载右边树
			if (binOLSSPRM13_global.rightTreeArr[i]==null){
				if (grpLocationPageList[i]!=null){
					binOLSSPRM13_global.locationType = grpLocationPageList[i].actLocationType;
					binOLSSPRM13_global.locationTypeArr[i] = binOLSSPRM13_global.locationType;
					//locationOK(i,grpLocationPageList[i].rightTreeList);
					var locationData = {};
					var locationArr =[];
					locationArr = addLocationData (grpLocationPageList[i].rightTreeList,locationArr,i);
					// 
					if($("#departType").val() == '1' || binOLSSPRM13_global.locationTypeArr[i] != '5'){
						// 设定选中的促销活动地点
						locationData.locationDataList = locationArr;
						locationData.locationType = binOLSSPRM13_global.locationTypeArr[i];
						binOLSSPRM13_global.timeLocationDataArr[i].locationData = locationData;
					}
				}
			}
		}
	}
	var ruleJson = parseRuleBody2RuleJson($($('#rule-detail-content').find(' >ul')[0]));
	PRM13.getResultInfo();
	getTimeLocationData();
	loadCounterNodes(url,ruleJson);
	

}

/**
 * 取得活动地点数据
 * @return
 */
function getLocationData (){
	var index = getTimeLocationIndex(); 
	var url = $('#locationSearchUrl').html();
	// 活动地点类型
	var locationType = $('#sel_location_type_'+index).val();
	// 如果用户重新搜索活动地点,则不用再关联之前保存的活动地点
	binOLSSPRM13_global.locationFlg = true;
	binOLSSPRM13_global.locationType = locationType;
	binOLSSPRM13_global.locationTypeArr[index] = binOLSSPRM13_global.locationType;
	var param ="locationType="+locationType;
	cherryAjaxRequest({
		url:url,
		param:param,
		callback:addLocationTreeData
	});
	// 清空右边的树
	var setting = binOLSSPRM13_global.zTreeSetting;
//	binOLSSPRM13_global.rightTreeTmp = $('#treeRight_'+index).zTree (setting);
	binOLSSPRM13_global.rightTreeTmp = $.fn.zTree.init($('#treeRight_'+index),setting,null);
	
	/**
	 * 加载树
	 * @param data
	 * @return
	 */
	function addLocationTreeData(data){
		var setting  = binOLSSPRM13_global.zTreeSetting;
		// 取得该活动地点所在的索引
		var index = getTimeLocationIndex(); 
//		binOLSSPRM13_global.leftTreeTmp = $('#treeLeft_'+index).zTree (setting,eval('('+data+')'));
		binOLSSPRM13_global.leftTreeTmp = $.fn.zTree.init($('#treeLeft_'+index),setting,eval('('+data+')'));
	}
}


/**
 * 打开活动地点
 * @return
 */
function openLocation(thisObj){
	binOLSSPRM13_global.thisClickObj= $(thisObj);
	// 取得该活动地点所在的索引
	var index = getTimeLocationIndex();
	if ($('#location_dialog_'+index).html() == null){
		var htmlTmpArr = new Array();
		getDialogHTML(htmlTmpArr,index);
		$('#div_main').append(htmlTmpArr.join(''));
		
		
	}
	// 地点定位
	locationPositionBinding(index);
	$('#location_dialog_'+index).dialog({ autoOpen: false,  width: 900, height: 'auto', title:BINOLSSPRM13_js_i18n.activeLocation, zIndex: 1,  modal: true, dialogClass: 'dialog-yellow',resizable:false,
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
	
	// 初始化弹出框
	var treeLeft = binOLSSPRM13_global.leftTreeArr[index];
	var treeRight = binOLSSPRM13_global.rightTreeArr[index];
	var setting = binOLSSPRM13_global.zTreeSetting;
	// 初始化treeLeft
	if (treeLeft!=null){
		var cloneLeftNodes = cloneNodeDeep(treeLeft.getNodes());
		binOLSSPRM13_global.leftTreeTmp = $.fn.zTree.init($('#treeLeft_'+index), setting, cloneLeftNodes);
	}
	// 初始化treeRight
	if (treeRight!=null){
		var cloneRightNodes = cloneNodeDeep(treeRight.getNodes());
		binOLSSPRM13_global.rightTreeTmp = $.fn.zTree.init($('#treeRight_'+index), setting, cloneRightNodes);
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
	}else if(locationType == 6){
		$searchBtn.hide();
		$cntFile.show();
	}else{
		$searchBtn.show();
		$cntFile.hide();
	}
}


/**
 * 左边树到右边树
 * @return
 */
function left2Right (){
//	var index = getTimeLocationIndex(); 
	moveCheckNodes(binOLSSPRM13_global.leftTreeTmp,binOLSSPRM13_global.rightTreeTmp);	
}

/**
 * 右边树到左边树
 * @return
 */
function right2Left (){
//	var index = getTimeLocationIndex(); 
	moveCheckNodes(binOLSSPRM13_global.rightTreeTmp,binOLSSPRM13_global.leftTreeTmp);	
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
	var index = getTimeLocationIndex(); 
	if (checkedNodes[0].name == 'artificialCounter'){
		var srcParentNode = checkedNodes[0].getParentNode();
		srcParentNodeArr.push(srcParentNode);
//		var treeNode = searchNodeById(targetTree.getNodes(),srcParentNode.id);
		var treeNode = targetTree.getNodeByParam("id", srcParentNode.id, null);
		if (treeNode!=null){
			// 需要先加载源树节点
			var locationType = binOLSSPRM13_global.locationTypeArr[index];
			if (locationType == "2"){
				var param = "cityID="+treeNode.id;
			}else if (locationType == "4"){
				var param = "channelID="+treeNode.id;
			}else if (locationType == "8"){
				var param = "factionID="+treeNode.id;
			}
			
			// 取得活动地点所在的分组索引
			var index = getTimeLocationIndex(); 
			// 取得树对象
			if (srcParentNode.tId.indexOf('treeLeft')>=0){
				var tree= binOLSSPRM13_global.leftTreeTmp;
				param = param + "&treeType=leftTree";
			}else {
				var tree= binOLSSPRM13_global.rightTreeTmp;
				param = param + "&treeType=rightTree";
			}
			if ((!binOLSSPRM13_global.locationFlg) && $('#activeID')!=null && $('#activeID').val()!=undefined){
				param = param + "&activeID="+$('#activeID').val();
			}
			if ($('#activitySetBy')!=null && $('#activitySetBy').val()!=undefined){
				param = param + "&activitySetBy="+$('#activitySetBy').val();
			}
			if ($('#showType')!=null && $('#showType').val()!=undefined){
				param = param + "&showType="+$('#showType').val();
			}
			var index = getTimeLocationIndex();
			param = param + "&grpID="+(parseInt(index)+1);

			tree.updateNode(treeNode);
			url = $('#getCounterDetailByAjaxUrl').html();
			cherryAjaxRequest({
				url:url,
				param:param,
				callback:expendTreeNodesCallBack,
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
	/**
	 * 修改：完成树节点移动后不需要展开被移动的结点
	 * 对应问题票：WITPOSQA-4634
	 * 
	 * @author zhanggl
	 * 
	 * @date 2012-2-14
	 * 
	 * */
	 var srcParentNode = null;
	 var targetNewNode = null;
	 try{
		 srcParentNode = node.getParentNode() ;
	 }catch(e){
		 srcParentNode = node.parentNode;
	 }finally{
		 if (srcParentNode ==null){
			 targetNewNode = targetTree.addNodes(srcParentNode,newNode,true);
//			  var newTreeNode = searchNodeById(targetTree.getNodes(),newNode[0].id);
//			  var artificialNode = searchNodeById(newTreeNode.nodes,'artificialCounter');
//			  if (artificialNode==null){
//				  // 展开节点
//				  targetTree.expandNode(newTreeNode,true,true);
//			  }
			  
		  }else{
//			  var targetNode = searchNodeById(targetNodes,srcParentNode.id);
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
//				  var newTreeNode = searchNodeById(targetTree.getNodes(),newNode[0].id);
//				  var artificialNode = searchNodeById(newTreeNode.nodes,'artificialCounter');
//				  if (artificialNode==null){
//					  // 展开节点
//					  targetTree.expandNode(newTreeNode,true,true);
//				  }
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
	var index = getTimeLocationIndex(); 
	var locationType = binOLSSPRM13_global.locationTypeArr[index];
	if (locationType == "2"){
		var param = "cityID="+treeNode.id;
	}else if (locationType == "4"){
		var param = "channelID="+treeNode.id;
	}else if (locationType == "8"){
		var param = "factionID="+treeNode.id;
	}
	
	var name = treeNode.name;
	// 取得活动地点所在的分组索引
	var index = getTimeLocationIndex(); 
	// 取得树对象
	if (treeNode.tId.indexOf('treeLeft')>=0){
		var tree= binOLSSPRM13_global.leftTreeTmp;
		param = param + "&treeType=leftTree";
	}else {
		var tree= binOLSSPRM13_global.rightTreeTmp;
		param = param + "&treeType=rightTree";
	}
	
	if ((!binOLSSPRM13_global.locationFlg) && $('#activeID')!=null && $('#activeID').val()!=undefined){
		param = param + "&activeID="+$('#activeID').val();
	}
	if ($('#activitySetBy')!=null && $('#activitySetBy').val()!=undefined){
		param = param + "&activitySetBy="+$('#activitySetBy').val();
	}
	if ($('#showType')!=null && $('#showType').val()!=undefined){
		param = param + "&showType="+$('#showType').val();
	}
	var index = getTimeLocationIndex();
	param = param + "&grpID="+(parseInt(index)+1);
	if (treeNode.name.indexOf("loading...")<0){
		treeNode.name +="(loading...)";
	}
	tree.updateNode(treeNode);
	
	url = $('#getCounterDetailByAjaxUrl').html();
	var reloadFlg;
	if (binOLSSPRM13_global.positionName !=""){
		reloadFlg:false;
	}else{
		reloadFlg:true;
	}
	cherryAjaxRequest({
		url:url,
		param:param,
		callback:expendTreeNodesCallBack,
		reloadFlg:reloadFlg
	});
	
	function expendTreeNodesCallBack(data){
		var dataArr = eval('('+data+')');
		if (binOLSSPRM13_global.positionName !=""){
			var skipFlag = false;
			for (var i=0;i<dataArr.length;i++){
				var dataObj = dataArr[i];
				if (dataObj.name == binOLSSPRM13_global.positionName){
					skipFlag = true;
					break;
				}
			}
			var selectNode = tree.getNodeByParam("name",binOLSSPRM13_global.positionName+"(loading...)");
			if (!skipFlag && selectNode==null){
				treeNode.name = name;
				tree.updateNode(treeNode);
				binOLSSPRM13_global.positionName = "";
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
		if (binOLSSPRM13_global.positionName !=""){
			var selnode = tree.getNodeByParam("name",binOLSSPRM13_global.positionName);
			tree.selectNode(selnode);
			binOLSSPRM13_global.positionName = "";
		}
	}
	
}


/**
 * 添加地点数据
 * @param nodes
 * @param arr
 * @return
 */
function addLocationData(nodes,arr,index){
	if (!nodes) return arr; 
	locationType = binOLSSPRM13_global.locationTypeArr[index];
	for (var i=0;i<nodes.length;i++){
		var node = nodes[i];
		var childNodes = node.nodes;
		if (childNodes ==null){
			if (locationType == "1" || locationType == "5"){
				arr.push ("city_"+node.id);
			}else if (locationType == "3"){
				arr.push ("channel_"+node.id);
			}else if (locationType == "7"){
				arr.push ("faction_"+node.id);
			}else if (locationType == "2" || locationType == "4" || locationType == "6" || locationType == "8"){
				arr.push ("counter_"+node.id);
			}
		}else if (childNodes.length == 1 && childNodes[0].name =="artificialCounter"){
			if (locationType == "2"){
				arr.push ("city_"+node.id);
			}else if (locationType == "4"){
				arr.push ("channel_"+node.id);
			}else if (locationType == "8"){
				arr.push ("faction_"+node.id);
			}
		}else {
			arr = addLocationData(childNodes,arr,index);
		}
	}
	return arr;
}

/**
 * 活动地点确定
 * @return
 */
function locationOK(index,treeNodes){
	var locationArr = new Array();
	var nodes = treeNodes;
	if (treeNodes==null){
		// 将tmptree的数据转入正式确定的dataTree中
		binOLSSPRM13_global.leftTreeArr[index] = binOLSSPRM13_global.leftTreeTmp;
		binOLSSPRM13_global.rightTreeArr[index] = binOLSSPRM13_global.rightTreeTmp;
		binOLSSPRM13_global.leftTreeTmp = null;
		binOLSSPRM13_global.rightTreeTmp = null;
		var rightTree = binOLSSPRM13_global.rightTreeArr[index];
		if (typeof (rightTree) != 'undefined' && rightTree != null){
			nodes = rightTree.getNodes();	
		}
	}
	// 活动地点类型
	var locationType = $('#sel_location_type_'+index).val();
	if(locationType == 5){
		binOLSSPRM13_global.locationType = locationType;
		binOLSSPRM13_global.locationTypeArr[index] = binOLSSPRM13_global.locationType;
		locationArr = addLocationData (nodes,locationArr,index);
	}else if(locationType == 6){
		binOLSSPRM13_global.locationType = locationType;
		binOLSSPRM13_global.locationTypeArr[index] = binOLSSPRM13_global.locationType;
		locationArr = addLocationData (nodes,locationArr,index);
	}else{
		locationArr = addLocationData (nodes,locationArr,index);
	}
	var locationData = {};
	// 设定选中的促销活动地点
	locationData.locationDataList = locationArr;
	locationData.locationType = binOLSSPRM13_global.locationTypeArr[index];
	binOLSSPRM13_global.timeLocationDataArr[index].locationData = locationData;
	// 活动类型为，区域，渠道时
	if((locationType == 1 || locationType == 3) && nodes != undefined && nodes != null && nodes.length > 0){
		// 叶子节点
		var leafNodes = [];
		getLeafNodes(nodes, leafNodes);
		var url = '/Cherry/ss/BINOLSSPRM13/BINOLSSPRM13_queryCounterCount';
		var params = "locationType=" + locationType;
		params += "&timeLocationJSON=["
		for (var i=0;i<leafNodes.length;i++){
			var node = leafNodes[i];
			if(i != 0){
				params += ",";
			}
			params += node.id;
		}
		params += "]";
		cherryAjaxRequest({
			url:url,
			param:params,
			callback: function (msg){
				$("#actionResultDisplay").html(msg);
			}
		});
	}
}

/**
 * 添加活动组
 * @return
 */
function addActiveGroup (){
	var url = $('#addPrmActiveGrpUrl').html();
	var groupName = $('#groupName').val();
	param = $("#prm_act_grp_dialog").find(':input:visible').serialize();
	cherryAjaxRequest({
		url:url,
		param:param,
		callback:callBack
	});
	
	
	function callBack(data){
		var htmlTmp = '<option value="'+data +'">'+groupName+'</option>';
		$('#prmActGrp').append(htmlTmp);
		if (data.indexOf('fieldErrorDiv')<0){
			$("#prm_act_grp_dialog").dialog("destroy");
			$("#prm_act_grp_dialog").remove();
			$('#prmActGrp').val(data);
			var url = $('#changeGroupUrl').html();
			var param = "prmActGrp=" + data;
			cherryAjaxRequest({
				url:url,
				param:param,
				callback: function (msg){
					// 修改活动奖励
					getGropInfo(msg);
					// 设置子活动类型
					PRM13.setActivityType();
				}
			});
		}
	}
}
/**
 * 活动组change事件
 * @return
 */
function groupChange(obj){
	var url = $('#changeGroupUrl').html();
	param = "prmActGrp=" + $(obj).val();
	cherryAjaxRequest({
		url:url,
		param:param,
		callback:function(msg){getGropInfo(msg);}
	});
}
/**
 * 
 */
function getGropInfo(data){
	$('#groupInfo').html(data);
	// 改变奖励box
	var $data = $(data);
	var prmGrpType = $data.find(":input[name='prmGrpType']").val();
	var $rewardsBody = $("#rewards_body");	
	$rewardsBody.children().hide();
	$("#"+prmGrpType).show();
	// 设置活动类型
	PRM13.setActivityType(prmGrpType);
	// 清空礼品list
	
//	$rewardsBody.children(":hidden").find("tbody").each(function(){
//		var $this = $(this);
//		$this.empty();
//		$this.parent().next().find(":input").val("");
//		PRM13.getSumInfo($this.prop("id"));
//	});
}
/**
 * 取得时间地点提交数据
 * @return
 */
function getTimeLocationData(){
	// 取得时间地点box数组
	var $time_location_box_arr = $('#condition_body .time_location_box');
	for (var i=0;i<$time_location_box_arr.length;i++){
		var $time_location_box = $($time_location_box_arr[i]);
		var time_box_arr = $time_location_box.find('.time_box');
		var timeArr = new Array();
		for (var j=0;j<time_box_arr.length;j++){
			$time_box = $(time_box_arr[j]);
			var timeJson = {};
			// 开始时间
			timeJson.startTime = $($time_box.find('.startTime')[0]).val();
			// 开始时间
			timeJson.startTime2 = $($time_box.find('.time')[0]).val();
			// 结束时间
			timeJson.endTime = $($time_box.find('.endTime')[0]).val();
			// 结束时间
			timeJson.endTime2 = $($time_box.find('.time')[1]).val();
			timeArr.push(timeJson);
		}
		binOLSSPRM13_global.timeLocationDataArr[i].timeDataList = timeArr;
		// 时间地点条件分组序号
		binOLSSPRM13_global.timeLocationDataArr[i].conditionGrpId = i+1;
	}
	
	$('#timeLocationJSON').val(JSON2.stringify(binOLSSPRM13_global.timeLocationDataArr));
}

/**
 * 取得活动地点弹出框HTML
 * @param htmlTmpArr
 * @param index
 * @return
 */
function getDialogHTML(htmlTmpArr,index,flag){
	htmlTmpArr.push('<div class="dialog2 clearfix" style="display:none;" id="location_dialog_'+index+'">');
	// message
	htmlTmpArr.push('<div id="actionDisplay_'+index+'"><div style="display:none;" class="actionError" id="errorDiv_'+index+'"><ul><li><span id="errorSpan"></span></li></ul></div>'+
        	'<div style="display:none;" class="actionSuccess" id="successDiv_'+index+'"><ul><li><span id="successSpan"></span></li></ul></div></div><div id="actionResultDisplay_'+index+'"></div>');
	
	//htmlTmpArr.push('<div class="highlight">'+ BINOLSSPRM13_js_i18n.tip1 +'</div>');
	htmlTmpArr.push('<div><p style="margin: 5px;"><label>'+BINOLSSPRM13_js_i18n.locationType+'</label><select id="sel_location_type_'+index+'" style="width:150px;" onchange="changeLocationType(this);return false;"');
	if(flag){
		htmlTmpArr.push(' disabled="true"');
	}
	htmlTmpArr.push('><option value="0">'+BINOLSSPRM13_js_i18n.actLocationType+'</option><option value="1">'+BINOLSSPRM13_js_i18n.byRegin+'</option><option value="2">'+BINOLSSPRM13_js_i18n.byReginAndCounter+'</option><option value="3">'+BINOLSSPRM13_js_i18n.byChannel+'</option><option value="4">'+BINOLSSPRM13_js_i18n.byChannelAndCounter+'</option>');
	if($("#departType").val() == '1'){
		htmlTmpArr.push('<option value="5">'+BINOLSSPRM13_js_i18n.allCounter+'</option>');
	}
	htmlTmpArr.push('<option value="6">'+BINOLSSPRM13_js_i18n.byImport+'</option>');
	htmlTmpArr.push('<option value="7">'+BINOLSSPRM13_js_i18n.byFaction+'</option>');
	htmlTmpArr.push('<option value="8">'+BINOLSSPRM13_js_i18n.byFactionCnt+'</option></select><a style="display:none;"></a>');
//	if(flag){
//		htmlTmpArr.push(' style="display:none;" ');
//	}
//	htmlTmpArr.push('onclick ="getLocationData();return false;"><span class="ui-icon icon-search"></span><span class="button-text">'+BINOLSSPRM13_js_i18n.global_page_search+'</span></a>');
	// 柜台导入
	htmlTmpArr.push('<span style="display:none; margin-left:10px;"><span class="highlight">※</span>'+BINOLSSPRM13_js_i18n.tip2+' <a style="color:#3366FF;" href="/Cherry/download/柜台信息模板.xls">'+BINOLSSPRM13_js_i18n.tip3+'</a>');
	htmlTmpArr.push('<input class="input_text" type="text" id="pathExcel_' + index + '" name="pathExcel" style="height:20px;"/><input type="button" value="'+BINOLSSPRM13_js_i18n.browse+'"/>');
	htmlTmpArr.push('<input class="input_file" style="height:auto;margin-left:-314px;" type="file" name="upExcel" id="cntFile_' + index + '" size="40" onchange="pathExcel_' +index +'.value=this.value;return false;" />');
	htmlTmpArr.push('<input style="margin-left:10px;" type="button" id="upload" onclick="PRM13.ajaxFileUpload(\''+index+'\','+flag+');return false;" value="'+BINOLSSPRM13_js_i18n.upload+'"><img height="15px" style="display:none;" src="/Cherry/css/cherry/img/loading.gif" id="loading"></span></p></div>');
	htmlTmpArr.push('<div class="ztree"><div style="width: 45%;" class="left"><div class="jquery_tree"><div class="box2-header clearfix"> <strong class="left active"><span class="ui-icon icon-flag"></span>'+BINOLSSPRM13_js_i18n.notPrmLocation+'</strong><span style="margin-left:5px;"><input type="checkbox" id="selectAll_left_0" onchange="selectAll(this,0);"/><label for="selectAll_left_0">全选</label></span><span class="right"><input name ="locationPosition" type="text" style="width: 140px;" class="text locationPosition" id="position_left_'+index+'"><a onclick="locationPosition(\'leftTree\');return false;" class="search"><span class="ui-icon icon-position"></span><span class="button-text">'+BINOLSSPRM13_js_i18n.position+'</span></a></span></div><div id="treeLeft_'+index+'" class="jquery_tree treebox tree" style="overflow:auto;"></div></div></div>');
	htmlTmpArr.push('<div style="width: 9%; text-align: center; padding-top: 200px;" class="left"> <a class="mover" onclick ="left2Right();"><span class="ui-icon icon-mover"></span></a><br><br><br><a class="movel" onclick="right2Left();"><span class="ui-icon icon-movel"></span></a> </div>');
	htmlTmpArr.push('<div style="width: 45%;" class="right"><div class="jquery_tree"><div class="box2-header clearfix"> <strong class="left active"><span class="ui-icon icon-flag2"></span>'+BINOLSSPRM13_js_i18n.prmLocation+'</strong><span style="margin-left:5px;"><input type="checkbox" id="selectAll_right_0" onchange="selectAll(this,1);"/><label for="selectAll_right_0">全选</label></span><span class="right"><input name ="locationPosition" type="text" style="width: 140px;" class="text locationPosition" id="position_right_'+index+'"><a onclick="locationPosition(\'rightTree\');return false;" class="search"><span class="ui-icon icon-position"></span><span class="button-text">'+BINOLSSPRM13_js_i18n.position+'</span></a></span></div><div id="treeRight_'+index+'" class="jquery_tree treebox tree" style="overflow:auto;"></div></div></div>');
	htmlTmpArr.push('</div></div>');
}

/**
 * 取得活动地点弹出框HTML(详细画面用)
 * @param htmlTmpArr
 * @param index
 * @return
 */
function getDialogHTMLForDetail(htmlTmpArr,index){
	htmlTmpArr.push('<div class="dialog2 clearfix" style="display:none" id="location_dialog_'+index+'">');
	// message
	htmlTmpArr.push('<div id="actionDisplay_'+index+'"><div style="display:none;" class="actionError" id="errorDiv_'+index+'"><ul><li><span id="errorSpan"></span></li></ul></div>'+
        	'<div style="display:none;" class="actionSuccess" id="successDiv_'+index+'"><ul><li><span id="successSpan"></span></li></ul></div></div><div id="actionResultDisplay_'+index+'"></div>');
	//htmlTmpArr.push('<div class="highlight">'+ BINOLSSPRM13_js_i18n.tip1 +'</div>');
	htmlTmpArr.push('<div><p style="margin: 5px;"><label>'+BINOLSSPRM13_js_i18n.locationType+'</label><select id="sel_location_type_'+index+'" disabled="true" style="width:150px;"><option value="1">'+BINOLSSPRM13_js_i18n.byRegin+'</option><option value="2">'+BINOLSSPRM13_js_i18n.byReginAndCounter+'</option><option value="3">'+BINOLSSPRM13_js_i18n.byChannel+'</option><option value="4">'+BINOLSSPRM13_js_i18n.byChannelAndCounter+'</option><option value="5">'+BINOLSSPRM13_js_i18n.allCounter+'</option><option value="6">'+BINOLSSPRM13_js_i18n.byImport+'</option>');
	htmlTmpArr.push('<option value="7">'+BINOLSSPRM13_js_i18n.byFaction+'</option>');
	htmlTmpArr.push('<option value="8">'+BINOLSSPRM13_js_i18n.byFactionCnt+'</option>');
	htmlTmpArr.push('</select></p></div>');
	htmlTmpArr.push('<div class="ztree"><div style="width: 45%;" class="left"><div class="jquery_tree"><div class="box2-header clearfix"> <strong class="left active"><span class="ui-icon icon-flag"></span>'+BINOLSSPRM13_js_i18n.notPrmLocation+'</strong> <span class="right"><input name ="locationPosition" type="text" style="width: 140px;" class="text locationPosition" id="position_left_'+index+'"><a onclick="locationPosition(\'leftTree\');return false;" class="search"><span class="ui-icon icon-position"></span><span class="button-text">'+BINOLSSPRM13_js_i18n.position+'</span></a></span></div><div id="treeLeft_'+index+'" class="jquery_tree treebox tree" style="overflow:auto;"></div></div></div>');
	//htmlTmpArr.push('<div style="width: 9%; text-align: center; padding-top: 200px;" class="left"> <a class="mover" onclick ="left2Right();"><span class="ui-icon icon-mover"></span></a><br><br><br><a class="movel" onclick="right2Left();"><span class="ui-icon icon-movel"></span></a> </div>');
	htmlTmpArr.push('<div style="width: 45%;" class="right"><div class="jquery_tree"><div class="box2-header clearfix"> <strong class="left active"><span class="ui-icon icon-flag2"></span>'+BINOLSSPRM13_js_i18n.prmLocation+'</strong><span class="right"><input name ="locationPosition" type="text" style="width: 140px;" class="text locationPosition" id="position_right_'+index+'"><a onclick="locationPosition(\'rightTree\');return false;" class="search"><span class="ui-icon icon-position"></span><span class="button-text">'+BINOLSSPRM13_js_i18n.position+'</span></a></span></div><div id="treeRight_'+index+'" class="jquery_tree treebox tree" style="overflow:auto;"></div></div></div>');
	htmlTmpArr.push('</div></div>');
}

/**
 * 取得点击对象所在时间地点box的索引
 * @return
 */
function getTimeLocationIndex(){
	// 取得点击的对象
	var $clickObj =  binOLSSPRM13_global.thisClickObj;
	var index = $('#condition_body .time_location_box').length - $($clickObj.parents('.time_location_box')[0]).nextAll('.time_location_box').length;
	return index-1;
}

function arr_del(arr,d){
    return arr.slice(0,d-1).concat(arr.slice(d));
}

/**
 * 活动地点定位绑定
 * @param index
 * @return
 */
function locationPositionBinding(index){
	var url = $('#indSearchPrmLocationUrl').html()+"?"+getParentToken();
	$('#location_dialog_'+index+' .locationPosition').autocompleteCherry(url,{
			extraParams:{
			searchPrmLocation: function() { 
				var obj = $(document.activeElement);
				return obj.val();
			},
			locationType:function() { return binOLSSPRM13_global.locationTypeArr[index];}
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
 * 活动地点选择全部柜台时，查询按钮不显示
 * @param obj
 * @return
 */
function changeLocationType (obj){
	// 取得索引值
	var index = getTimeLocationIndex(); 
	// 情况错误信息
	var $message = $('#actionResultDisplay_' + index);
	var $errorDiv = $('#errorDiv_' + index);
	$errorDiv.hide();
	$message.hide();
	$(obj).next().next().hide();
	if($(obj).val() == 5 || $(obj).val() == 6){
		// 清空右边的树
		var setting = binOLSSPRM13_global.zTreeSetting;
		binOLSSPRM13_global.rightTreeTmp = $.fn.zTree.init($('#treeRight_'+index),setting,null);
		binOLSSPRM13_global.leftTreeTmp = $.fn.zTree.init($('#treeLeft_'+index),setting,null);
		if($(obj).val() == 6){
			$(obj).next().next().show();
		}
		if($(obj).val() == 5){
			binOLSSPRM13_global.rightTreeTmp.addNodes(null, [{id:"all",name:BINOLSSPRM13_js_i18n.allCounter}]);
		}
	}else{
		this.getLocationData();
	}
}

/**
 * 活动地点定位
 * @param treeType
 * @return
 */
function locationPosition(treeType){
	var index = getTimeLocationIndex(); 
	if (treeType == "leftTree"){
		var tree= binOLSSPRM13_global.leftTreeTmp;
	}else if (treeType == "rightTree"){
		var tree= binOLSSPRM13_global.rightTreeTmp;
	}
	
	if (treeType == "leftTree"){
		var positionName = $('#position_left_'+index).val();
	}else{
		var positionName = $('#position_right_'+index).val();
	}
	
	if (positionName==""){
		return;
	}
	binOLSSPRM13_global.positionName = positionName;

	if (binOLSSPRM13_global.locationType == "1" || binOLSSPRM13_global.locationType == "3"){
		var node = tree.getNodeByParam("name",positionName);
		tree.selectNode(node);
	}else if (binOLSSPRM13_global.locationType == "2" || binOLSSPRM13_global.locationType == "4" || binOLSSPRM13_global.locationType == "6"){
		var node = tree.getNodeByParam("name",positionName);
		if (node!=null){
			var childNodes = node.nodes;
			if (childNodes!=null && childNodes.length==1 && childNodes[0].name == 'artificialCounter'){
				getCounterByAjax(node);
			}else{
				tree.selectNode(node);
				binOLSSPRM13_global.positionName = "";
			}

		}else{
			var url = $('#getCounterParentUrl').html();
			var param = "counterName="+positionName;
			cherryAjaxRequest({
				url:url,
				param:param,
				callback:loadParentNode
			});
		}
	}
	
	function loadParentNode (data){
		var resultData = eval('('+data+')');
		if (resultData!=null){
			if (binOLSSPRM13_global.locationType == "2"){
				var node = tree.getNodeByParam("id",resultData.cityID);
			}else if (binOLSSPRM13_global.locationType == "4"){
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

function showSetReserve(obj){
	if($(obj).val() == "DHHD"){
		$("#choiceReserve").show();
		setTimeDiv($("#needReserve").val());
	}else{
		$("#choiceReserve").hide();
		$("#prm_act_grp_dialog").height(150);
		$("#prm_act_grp_dialog").find("#grpSetTimeDiv").hide();
	}
}
/**
 *  添加活动组时，若活动组类型为DHHD，显示时间设置框
 *  @param obj
 *  @return
 */
function showSetTime(obj){
	setTimeDiv($(obj).val());
}

/**
 *  设置活动组时间显示框
 *  @param obj
 *  @return
 */
function setTimeDiv(flag){
	if(flag == 1){
		$("#prm_act_grp_dialog").height(300);
		$("#prm_act_grp_dialog").find("#grpSetTimeDiv").show();
	}else{
		$("#prm_act_grp_dialog").height(200);
		$("#prm_act_grp_dialog").find("#grpSetTimeDiv").hide();
	}
}

/**
 * 点击了选择框后，来控制全选框
 * @return
 */
function changechkbox(obj){	

	var chked = $(obj).prop("checked");
	if(!chked){
		$('#prt_checkAll').prop("checked",false);
		return false;
	}
	var flag = true;
	$("#dataTableBody :checkbox").each(function(i){
		if(i>=0){
				if($(this).prop("checked")!=true){
					flag=false;
				}	
			}		
		});
	if(flag){
		$('#prt_checkAll').prop("checked",true);
	}
}

/**
 * 设置按钮有效与无效
 * 
 * 
 */
function disabOptBtn(){
	var flag = $("#flag").val();
	if(flag == "1"){
		return false;
	}
	return true;
}
/**
 * 点击编辑按钮，修改主活动名称
 * @returngrpName
 */
function editActGropItem(_this,val){
	// 当前操作按钮
	var $this = $(_this);
	var id = $this.prop("id");
	var btn1 = "btn_1";
	var btn2 = "btn_2";
	var btn3 = "btn_3";
	// 操作按钮所在单元格
	var $this_td = $this.parent();
	var $tr = $this_td.parent();
	// 组名称
	var $td = $tr.find("td").eq(2);
	var $spans = $td.find("span");
	var $text = $spans.eq(0);
	var $input = $('<input maxlength="50" class="text" name="grpName" value="' +$text.text()+ '"/>');
	
	// 领用开始结束日期可否编辑（如果开始日期小于等于业务日期，则不能修改）
	var $dateEditFlag = $spans.eq(1);
	var bolDateEditFlag = false;
	if($dateEditFlag.text() > 0){
		// 大于0表示业务日期不在领用开始结束日期区间
		bolDateEditFlag = true;
	}
	
	// 领用开始时间
	var $tdBD = $tr.find("td").eq(4);
	var $spansBD = $tdBD.find("span");
	var $textBD = $spansBD.eq(0);
	var $inputBD = $('<input maxlength="10" type="text" class="date startTime" style="width:80px;" name="activityBeginDate" value="' +$textBD.text()+ '"/>');
	// 领用结束时间
	var $tdED = $tr.find("td").eq(5);
	var $spansED = $tdED.find("span");
	var $textED = $spansED.eq(0);
	var $inputED = $('<input maxlength="10" type="text" class="date endTime" style="width:80px;" name="activityEndDate" value="' +$textED.text()+ '"/>');
	
	// 编辑按钮
	var $editBtn = $this_td.find("a[id^='"+btn1+"']");
	// 保存按钮
	var $saveBtn = $this_td.find("a[id^='"+btn2+"']");
	// 取消按钮
	var $cancelBtn = $this_td.find("a[id^='"+btn3+"']");
	// edit
	if(id.indexOf(btn1) > -1){
		// 组活动名称
		$text.hide();
		$td.append($input);
		
		if(bolDateEditFlag){
			// 领用开始时间
			$textBD.hide();
			$tdBD.append($inputBD);
			// 绑定日控件
			$tr.find(".startTime").cherryDate({
				holidayObj: binOLSSPRM13_global.holidays,
				// 日历的最小时间	
				minDate:binOLSSPRM13_global.calStartDate,
				//onSelectEvent:calSelect
				// 结束时间大于开始时间
				beforeShow: function(input){										
					var value = $(input).parent().parent().find(".endTime").val();
					return [value,'maxDate'];									
				}
			});
		}
		
		// 领用结束时间
		$textED.hide();
		$tdED.append($inputED);
		// 绑定日控件
		$tr.find(".endTime").cherryDate({
			holidayObj: binOLSSPRM13_global.holidays,
//				minDate:binOLSSPRM13_global.calStartDate,
			// 结束时间大于起始时间
			beforeShow: function(input){										
				var value = $(input).parent().parent().find(".startTime").val();
				return [value,'minDate'];									
			}	
		});
		
		$editBtn.hide();
		$saveBtn.show();
		$cancelBtn.show();
	}else if(id.indexOf(btn2) > -1){
		
		var groupName = $.trim($td.find("input").val());
		var bd = bolDateEditFlag ? $.trim($tdBD.find("input").val()) : $textBD.text();
		var ed = $.trim($tdED.find("input").val());
		
		if(groupName==""){
			$('#errorSpan').html($('#editerrmessage').val());
			$('#errorDiv').show();
			return true;
		} else if (bd=="" && bolDateEditFlag){
			$('#errorSpan').html($('#editerrmessage').val());
			$('#errorDiv').show();
			return true;
		} else if(ed==""){
			$('#errorSpan').html($('#editerrmessage').val());
			$('#errorDiv').show();
			return true;
		} else{
			
			var url = $('#actGropEditUrl').text();
			var param =getSerializeToken() + "&" +"groupName="+ groupName + "&activityBeginDate=" + bd + "&activityEndDate=" + ed;
			param += "&" + "prmActGrp="+val;
			// ajax保存
			cherryAjaxRequest({
				url:url,
				param:param,
				callback:function(){
					// 刷新表格数据
					oTableArr[24].fnDraw();
					$('#errorDiv').hide();
					// 修改主活动下拉框内容
					$('#prmActGrp').find('option[value="'+val+'"]').text(groupName);
				}
			});
		}
	
	}else if(id.indexOf(btn3) > -1){
		// 组活动名称
		$text.show();
		$td.find(":input").remove();
		if(bolDateEditFlag){
			// 领用开始时间
			$textBD.show();
			$tdBD.find(":input").remove();
		}
		// 领用结束时间
		$textED.show();
		$tdED.find(":input").remove();
		
		$editBtn.show();
		$saveBtn.hide();
		$cancelBtn.hide();
	}
}
//编辑活动组
function eidtPrmActGrop(){
    // 主活动弹出框属性设置
	var option = {
       checkType : "checkbox",// 选择框类型
       brandInfoId : $("#brandInfoId").val(),// 品牌ID
       click:function(){//点击删除按钮之后的处理
   			var $checkedInputs = $('#' + option.popTableId).find("tbody").find(':input[name="prmActGrps"]').filter(":checked");
   			if($checkedInputs.length == 0){
   				$('#errorSpan').html($('#errmsg').val());
   				$('#errorDiv').show();
   				return true;
   			}else{
   				var urls = $('#actGropDeleteUrl').text();
   				
   				var param = getSerializeToken() + "&" + $("#brandInfoId").serialize();
       			param += "&" + $checkedInputs.serialize();
       			cherryAjaxRequest({
        			url:urls,
        			param:param,
        			callback:function(){
        				$('#prmActGrp').find("option").each(function(){
        					var $option = $(this);
        					$checkedInputs.each(function(){
        						if($(this).val() == $option.val()){
        							$option.remove();
        							return;
        						}
        					});
        				});
        				// 取活动组ID查询info信息
        				var urlGrop = $('#changeGroupUrl').html();
        				cherryAjaxRequest({
							url:urlGrop,
							param:$('#prmActGrp').serialize(),
							callback: function (msg){
								// 修改活动奖励
								getGropInfo(msg);
								// 设置子活动类型
								PRM13.setActivityType();
							}
						});
        			}
        		});
   			}
		}
    };
	$('#errorDiv').hide();
     // 调用产品弹出框共通
    actGrpAjaxPrtDialog(option);
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

function selectAll(_this,flag){
	var $tree = null;
	if(flag == 1){
		$tree = binOLSSPRM13_global.rightTreeTmp;
	}else{
		$tree = binOLSSPRM13_global.leftTreeTmp;
	}
	var $this = $(_this);
	if(!isEmpty($tree)){
		$tree.checkAllNodes($this.is(":checked"));
	}
}

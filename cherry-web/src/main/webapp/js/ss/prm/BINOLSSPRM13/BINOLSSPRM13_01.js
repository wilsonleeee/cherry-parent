/**
 * 条件box li控件点击事件处理
 * @param event
 * @return
 */
function conditionLiClick (event){
	var $e_obj = $(event.target);
	if ($e_obj.hasClass('radio')){
		$e_obj= $e_obj.parent();
	}
	// 取得父节点div层
	var $e_parent_obj = $e_obj.parent().parent();
	// 取得li的id
	var $e_obj_id = $e_obj.attr('id');
	// 取得li的父节点div的id
	var $e_obj_parent_id = $e_parent_obj.attr('id');
	var li_class = $e_obj.attr('class');
	// 如果含有选中样式则取消,反之则选中
	if (typeof (li_class) =="undefined" || li_class.indexOf('on')<0){
		// 去除其他选中的样式
		$e_parent_obj.find('li').removeClass('on');
		// 添加当前点击li的样式
		$e_obj.addClass('on');
		// 记录选中的值
		if ($e_obj_parent_id == "ruleConditionBox"){
			//binOLSSPRM13_global.ruleCondition = $e_obj_id;
		}else if ($e_obj_parent_id == "logicConditionBox"){
			//binOLSSPRM13_global.logicCondition = $e_obj_id;
		}else if ($e_obj_parent_id == "positionBox"){
			// 去除radio选中状态
			$e_parent_obj.find('.radio').attr('checked',false);
			$e_obj.find('.radio').attr('checked',true);
			//binOLSSPRM13_global.conditionPosition = $e_obj_id;
		}else if ($e_obj_parent_id == "rewardInBox"){
			binOLSSPRM13_global.rewardType = $e_obj_id;
		}
	}else{
		/*有以下情况不能取消选中
		 * ①促销奖励box
		 * ②条件位置box
		 * ③编辑逻辑条件box
		 * ④位置条件时上一层或者下一层时,不能取消逻辑条件的选中
		 */
		if ($e_obj_parent_id != "rewardInBox" && $e_obj_parent_id != "positionBox" && $e_obj_parent_id != "editLogicInBox" && 
				!($e_obj_parent_id == "logicConditionBox" && ($('#con_up').attr('class').indexOf('on')>=0 || $('#con_down').attr('class').indexOf('on')>=0 ))){
			$e_obj.removeClass('on');
		}
	}
	
	// 当选择上一层或者下一层条件时，自动选中所需要的条件
	if ($e_obj_id == "con_up" || $e_obj_id == "con_down"){
		if ($('#logicConditionBox').find('.on').length == 0){
			$('#logicCon_or').addClass('on');
		}
	}
}

/**
 * 点击确定关闭条件Box
 * @param type
 * @return
 */
function closeConditionBox (){
	var clickObj = binOLSSPRM13_global.thisClickObj;
	var ruleCondition = null;
	var logicCondition = null;
	var conditionPosition = null;
	// 设定规则条件
	if ($('#ruleCon_amount').attr('class') == 'on'){
		// 购买金额
		ruleCondition = "ruleCon_amount";
	}else if ($('#ruleCon_product').attr('class') == 'on'){
		// 购买产品
		ruleCondition = "ruleCon_product";
	}
	
	// 设定逻辑条件
	if ($('#logicCon_or').attr('class') == 'on'){
		// or 条件
		logicCondition = "logicCon_or";
	}else if ($('#logicCon_and').attr('class') == 'on'){
		// and 条件
		logicCondition = "logicCon_and";
	}
	
	// 设定条件位置
	if ($('#con_this').attr('class').indexOf('on')>=0){
		// 当前条件
		conditionPosition = "con_this";
	}else if ($('#con_up').attr('class').indexOf('on')>=0){
		// 上一层条件
		conditionPosition = "con_up";
	}else if ($('#con_down').attr('class').indexOf('on')>=0){
		// 下一层条件
		conditionPosition = "con_down";
	}

	var htmlTmpArr = new Array();
	
	// 定义一个需要插入的目标节点;
	var appendToObj = null;
	// 定义结束标记数组
	var endTagArr = new Array();
	
	// 如果没有选择条件，直接关闭box直接返回
	if (logicCondition=="" && ruleCondition ==""){
		$('#conditionBox').hide();
		return;
	}
	
	// 添加当前层条件
	if (conditionPosition=="con_this"){
		var beforeFlg =false;
		if (logicCondition !=null){
			// 如果选择逻辑条件,则插入对象为父对象的ul节点
			appendToObj = clickObj.parents('ul').eq(0);
			// 先插入一个li节点
			htmlTmpArr.push(binOLSSPRM13_global.liHTML);
			endTagArr.push('</li>');
			// 取得逻辑条件HTML
			getRuleConditionHtml(htmlTmpArr,logicCondition);
			// 再插入一个ul节点
			htmlTmpArr.push(binOLSSPRM13_global.ulHTML);
			endTagArr.push('</ul>');
		}else{
			// 如果没有选择了逻辑条件,则插入对象为父对象的li节点
			var li_obj = clickObj.parents('li').first();
			
			// 查看li对象下面是否还包含ul对象
			if (li_obj.find('ul').length==0){
				// 如果没有ul节点,需要添加一个ul节点
				htmlTmpArr.push(binOLSSPRM13_global.ulHTML);
				endTagArr.push('</ul>');
				appendToObj = li_obj;
			}else{
				// 该规则条件下面没有子条件
				if (li_obj.find('ul').length == 1){
					// 如果有ul节点,则插入目标节点为子节点的ul节点
					appendToObj = $(li_obj.find('ul')[0]);
				}else if (li_obj.find('ul').length >1){
					var length = $($(li_obj).find('ul')[0]).find('> li').length;
					// 该规则条件下面含有子条件
					appendToObj = $($($(li_obj).find('ul')[0]).find('> li')[length-1]);
					beforeFlg = true;
				}
			}
		}
		
		// 如果选择了购买产品
		if (ruleCondition == "ruleCon_product"){
			htmlTmpArr.push(binOLSSPRM13_global.liHTML);
			getProductHtml(htmlTmpArr);
			endTagArr.push('</li>');
		}
		// 如果选择了消费金额
		if (ruleCondition=="ruleCon_amount"){
			htmlTmpArr.push(binOLSSPRM13_global.liHTML);
			getAmountHtml(htmlTmpArr);
			endTagArr.push('</li>');
		}
		if (beforeFlg){
			appendToObj.before(htmlTmpArr.join('')+endTagArr.reverse().join(''));
		}else{
			appendToObj.append(htmlTmpArr.join('')+endTagArr.reverse().join(''));
		}
		
	}else if (conditionPosition=="con_down"){
		// 添加下一层条件
		// 当逻辑条件下面含有规则条件时
		if (clickObj.parents('li').first().find('ul').length>0){
			appendToObj = $(clickObj.parents('li').first().find('ul')[0]);
			// 添加li节点
			htmlTmpArr.push(binOLSSPRM13_global.liHTML);
			// 添加逻辑条件选择菜单
			getRuleConditionHtml(htmlTmpArr,logicCondition);
			endTagArr.push('</li>');
		}else{
			// 当逻辑条件下面没有规则条件时
			appendToObj = clickObj.parents('li').first();
			// 添加ul节点
			htmlTmpArr.push(binOLSSPRM13_global.ulHTML);
			// 添加li节点
			htmlTmpArr.push(binOLSSPRM13_global.liHTML);
			// 添加逻辑条件选择菜单
			getRuleConditionHtml(htmlTmpArr,logicCondition);
			endTagArr.push('</li>');
			endTagArr.push('</ul>');
		}
		// 如果选择了购买产品
		if (ruleCondition == "ruleCon_product"){
			htmlTmpArr.push(binOLSSPRM13_global.ulHTML);
			htmlTmpArr.push(binOLSSPRM13_global.liHTML);
			getProductHtml(htmlTmpArr);
			endTagArr.push('</li>');
			endTagArr.push('</ul>');
		}
		// 如果选择了消费金额
		if (ruleCondition=="ruleCon_amount"){
			htmlTmpArr.push(binOLSSPRM13_global.ulHTML);
			htmlTmpArr.push(binOLSSPRM13_global.liHTML);
			getAmountHtml(htmlTmpArr);
			endTagArr.push('</li>');
			endTagArr.push('</ul>');
		}
		appendToObj.append(htmlTmpArr.join('')+endTagArr.reverse().join(''));
	}else if (conditionPosition=="con_up"){
		// 添加上一层条件
		appendToObj = clickObj.parents('li').first();
		// 添加一个li节点
		htmlTmpArr.push(binOLSSPRM13_global.liHTML);
		endTagArr.push('</li>');
		// 添加逻辑条件
		getRuleConditionHtml(htmlTmpArr,logicCondition);
		// 再添加一个ul节点
		htmlTmpArr.push(binOLSSPRM13_global.ulHTML);
		endTagArr.push('</ul>');
		// 如果选择了购买产品
		if (ruleCondition == "ruleCon_product"){
			htmlTmpArr.push(binOLSSPRM13_global.liHTML);
			getProductHtml(htmlTmpArr);
			htmlTmpArr.push('</li>');
		}
		// 如果选择了消费金额
		if (ruleCondition=="ruleCon_amount"){
			htmlTmpArr.push(binOLSSPRM13_global.liHTML);
			getAmountHtml(htmlTmpArr);
			htmlTmpArr.push('</li>');
		}
		htmlTmpArr.push(binOLSSPRM13_global.liHTML);
		htmlTmpArr.push(clickObj.parents('li').first().html());
		htmlTmpArr.push('</li>');

		// HTML 替换
		appendToObj.before(htmlTmpArr.join('')+endTagArr.reverse().join(''));
		clickObj.parents('li').first().remove();
	}

	htmlTmpArr = null;
	
	
	if ($('#rule-detail-content >ul').find(' >li').length>1){
		// 当有多个同层的条件时，需要将删除按钮一并显示
		$('#rule-detail-content .delete-big').show();
	}else if ($('#rule-detail-content >ul').find('li').length>1){
		// 只有一个同层条件时，最外层的条件的删除按钮不予显示
		$('#rule-detail-content .delete-big').show();
		$('#rule-detail-content .delete-big').first().hide();
	}
	
	$('#conditionBox').hide();
	
}

/**
 * 打开条件box
 * @param type
 * @return
 */
function openConditionBox (thisObj){
	// 将点击对象保存
	binOLSSPRM13_global.thisClickObj = $(thisObj);
	// 取得按钮位置
	var opos = $(thisObj).offset();
	var oleft = parseInt(opos.left, 10);
	var otop = parseInt(opos.top + $(thisObj).outerHeight(), 10);
	if((oleft + $('#conditionBox').width()) > $(document).width())
	{
		oleft = oleft + $(thisObj).width() - $('#conditionBox').width();	
	}
	divHide('#conditionBox');
	$('#conditionBox').css({'left': oleft + "px", 'top': otop }).show();
}

/**
 * 打开产品查询box
 * @param thisObj
 * @return
 */
//function openProductSearchBox (thisObj){
//	var prtPopParam ={};
//	prtPopParam.thisObj = thisObj;
//	prtPopParam.index= 1;
//	prtPopParam.csrftoken = getParentToken('prmActiveform');
//	popDataTableOfPrtInfo(prtPopParam);
//	binOLSSPRM13_global.thisClickObj = $(thisObj);
//}

/**
 * 选择产品
 * @return
 */
function selectProduct (_this){
	// 如果没有数据，则不进行选择
	if ($('#prt_dataTable').find('tr').length == 0){
		return ;
	}
	var $this = $(_this);
	// 取得当前的ul节点
	var $this_ul = $this.parents('ul').eq(0);
	$this_ul.html("");
	// 取得选择的产品
	var $selectedProArr = $('#productDialog_temp').find('tr');
	$selectedProArr.each(function(i){
		var $tr = $(this); 
		var $td = $tr.find("td");
		var htmlTmpArr = new Array();
		var prtVendorId = $tr.find(":input[name='prtVendorId']").val();
		// 产品数量
//		var quantity = $tr.find(":input[name='quantity']").val();
		// 产品全称
		var selectedProName = $td.eq(3).text();
		htmlTmpArr.push(binOLSSPRM13_global.liHTML);
		// 添加选择产品按钮
		getProductHtml(htmlTmpArr,false);
		// 添加产品名,数量
		getProductHtml2(htmlTmpArr,selectedProName,prtVendorId);
		// 添加关闭小按钮
		getSmallCloseHTML(htmlTmpArr);
		htmlTmpArr.push('</li>');
		// 插入产品到ul节点
		$this_ul.append (htmlTmpArr.join(''));
	});
}

/**
 * 打开逻辑关系运算符
 * @param thisObj
 * @return
 */
function openCompareOperator(thisObj){
	//取得当前按钮的位置
	var opos = $(thisObj).offset();
	var oleft = parseInt(opos.left, 10);
	var otop = parseInt(opos.top + $(thisObj).outerHeight(), 10);
	divHide('#logicOperator');         
	// 设置显示关闭
	$('#logicOperator').css({'left': oleft + "px", 'top': otop }).show();
	binOLSSPRM13_global.thisClickObj = $(thisObj);
}

/**
 * 选择逻辑运算符
 * @return
 */
function selectLogicOperator(val){
	// 取得点击的控件对象
	var $thisClickObj = binOLSSPRM13_global.thisClickObj;
	// 设置值
	$($thisClickObj.find('.button-text')[0]).html(val);
	$('#logicOperator').hide();
	
}

/**
 * 删除单个条件
 * @param thisObj
 * @return
 */
function removeSingleCondition(thisObj){
	var removeObj = $(thisObj).parent();
	removeObj.remove();
	// 当相应的div上只有一个元素时，不进行删除
	if ($('#proRewardContent').find('.close').length == 1){
		$('#proRewardContent .close').hide();
	}
}

/**
 * 删除条件组
 * @param thisObj
 * @return
 */
function removeGroupCondition(thisObj){
	// 如果只有一个逻辑条件
	$(thisObj).parents('li').eq(0).remove();
	
	if ($('#rule-detail-content').find('.delete-big').length == 1){
		// 则不进行删除
		$('#rule-detail-content .delete-big').hide();
	}
}

/**
 * 显示金额设置box
 * @param thisObj
 * @return
 */
function openAmountBox (thisObj){
	var opos = $(thisObj).offset();
	var oleft = parseInt(opos.left, 10);
	var otop = parseInt(opos.top + $(thisObj).outerHeight(), 10);
	divHide('#amountBox'); 
	$('#amountBox').css({'left': oleft + "px", 'top': otop }).show();
	binOLSSPRM13_global.thisClickObj = $(thisObj);
}

/**
 * 设定金额值
 * @return
 */
function setAmountValue(){
	// 获取金额选择按钮
	var a_obj = binOLSSPRM13_global.thisClickObj;
	var li_obj = binOLSSPRM13_global.thisClickObj.parents('li').first();
	var htmlTmpArr = new Array();
	// 如果已经有金额范围设定,则进行替换
	if (li_obj.find('.amount-scope-start').length>0){
		// 清空金额范围设定
		li_obj.empty();
		// 添加消费金额选择按钮
		getAmountHtml(htmlTmpArr,false);
		// 添加比较运算,金额值输入框
		getAmountHtml2(htmlTmpArr);
		// 添加关闭小按钮
		getSmallCloseHTML(htmlTmpArr);
		li_obj.append(htmlTmpArr.join(''));
	}
	
	// 如果没有金额值设定，则生成金额值设定HTML
	if (li_obj.find('.amount-val').length==0){
		// 添加比较运算,金额值输入框
		getAmountHtml2(htmlTmpArr);
		// 将HTML插入在金额选择和关闭按钮中间
		a_obj.after(htmlTmpArr.join(''));
	}

	$('#amountBox').hide();
	$('#amountScope').hide();
}

/**
 * 打开金额范围
 * @param thisObj
 * @return
 */
function openAmountScope(thisObj){
	var opos = $(thisObj).offset();
	var oleft = parseInt(opos.left + $(thisObj).outerWidth(), 10);
	var otop = parseInt(opos.top, 10);
	divHide('#amountScope');
	$('#amountScope').css({'left': oleft + "px", 'top': otop }).show();
}

/**
 * 设定金额范围
 * @return
 */
function setAmountScope(){
	// 当输入框中的值为空时,直接返回
	if (($('#scopeStart').val()=="") || ($('#scopeEnd').val()=="")){
		return;
	}
	
	// 获取金额选择按钮
//	var a_obj = binOLSSPRM13_global.thisClickObj;
	var li_obj = binOLSSPRM13_global.thisClickObj.parents('li').first();
	var htmlTmpArr = new Array();
	// 清空金额值设定
	li_obj.empty();
	// 添加消费金额选择按钮
	getAmountHtml(htmlTmpArr,false);
	// 取得金额的范围HTML
	getAmountHtml3(htmlTmpArr,$('#scopeStart').val(),$('#scopeEnd').val());
	// 添加关闭小按钮
	getSmallCloseHTML(htmlTmpArr);
	li_obj.append(htmlTmpArr.join(''));
	// 关闭金额设置box
	$('#amountBox').hide();
	$('#amountScope').hide();
}

/**
 * 打开促销奖励Box
 * @return
 */
function openRewardBox(thisObj){
	var opos = $(thisObj).offset();
	var oleft = parseInt(opos.left, 10);
	var otop = parseInt(opos.top + $(thisObj).outerHeight(), 10);
	divHide('#rewardBox');
	$('#rewardBox').css({'left': oleft + "px", 'top': otop }).show();
	binOLSSPRM13_global.thisClickObj = $(thisObj);
}

/**
 * 关闭促销奖励Box
 * @return
 */
function closeRewardBox(){
	//var $li_obj = binOLSSPRM13_global.thisClickObj.parents('li').first();
	var $ul_obj = binOLSSPRM13_global.thisClickObj.parents('ul').first();
	if ($('#rewardAmount').hasClass('on')){
		binOLSSPRM13_global.rewardType = "rewardAmount";
	}else if ($('#rewardProduct').hasClass('on')){
		binOLSSPRM13_global.rewardType = "rewardProduct";
	}
	
	var htmlTmpArr = new Array();
	// 如果选择促销奖励
	if (binOLSSPRM13_global.rewardType =="rewardProduct"){
		htmlTmpArr.push(binOLSSPRM13_global.liHTML);
		getPromotionProHTML(htmlTmpArr);
		getAddPromotionRewardHTML(htmlTmpArr);
		getSmallCloseHTML(htmlTmpArr);
		htmlTmpArr.push('</li>');
		$ul_obj.append(htmlTmpArr.join(''));
	}else if (binOLSSPRM13_global.rewardType =="rewardAmount"){
		// 如果选择促销金额奖励
		if ($ul_obj.find('.promotion-amount').length!=0){
			$('#rewardBox').hide();
			return;
		}else {
			var $first_li = $($ul_obj.find('li')[0]);
			htmlTmpArr.push(binOLSSPRM13_global.liHTML);
			getPromotionAmountHTML(htmlTmpArr);
			getAddPromotionRewardHTML(htmlTmpArr);
			getSmallCloseHTML(htmlTmpArr);
			htmlTmpArr.push('</li>');
			// 将金额奖励放在第一行
			$first_li.before(htmlTmpArr.join(''));
		}
	}
	$('#rewardBox').hide();
	$('#proRewardContent .close').show();
}

/**
 * 打开促销产品查询box
 * @param thisObj
 * @return
 */
function openPromotionSearchBox (thisObj){
	var prmPopParam ={};
	prmPopParam.thisObj = thisObj;
	prmPopParam.csrftoken = getParentToken('prmActiveform');
	popDataTableOfPrmInfo(prmPopParam);
	binOLSSPRM13_global.thisClickObj = $(thisObj);
}

/**
 * 选择促销品
 * @return
 */
function selectPromotion () {
	if ($('#prm_dataTableBody').find('tr').length == 0){
		// 如果没有数据，则不进行选择
		return ;
	}
	var prmCate = $('#promotionCateTitle .ui-tabs-selected').attr("id");
	var prmCateName = $('#promotionCateTitle .ui-tabs-selected').attr("type");
	var selectedPrmArr = $('#prm_dataTableBody input:checked');
	
	var $ul_obj = binOLSSPRM13_global.thisClickObj.parents('ul').first();
	
	var $li_obj = binOLSSPRM13_global.thisClickObj.parents('li').eq(0);
	for (var i=0;i<selectedPrmArr.length;i++){
		var selectedValue = $(selectedPrmArr[i]).val();
		var selectedObj = window.JSON2.parse(selectedValue);
		// 促销品条码
		var selectedPrmBarCode = selectedObj.barCode;
		// 促销品厂商编码
		var selectedPrmUnitCode = selectedObj.unitCode;
		// 促销品名称
		var selectedPrmName = selectedObj.nameTotal;
		var htmlTmpArr = new Array();
		var prmPresentUnitCodeArr = $ul_obj.find('.prmPresentUnitCode');
		var prmPresentBarCodeArr = $ul_obj.find('.prmPresentBarCode');
		var skipFlag = false;
		for (var j=0;j<prmPresentUnitCodeArr.length;j++){
			if (selectedPrmBarCode == $(prmPresentBarCodeArr[j]).val() && selectedPrmUnitCode == $(prmPresentUnitCodeArr[j]).val()){
				skipFlag  =true;
				break;
			}
		}
		if (skipFlag){
			continue;
		}
		if (i==0){
			// 将促销产品信息添加到促销产品按钮后面
			// 判断是否已经有促销产品的信息
			if ($li_obj.find('.prmPresentUnitCode').length>0){
				// 清空li节点的内容
				$li_obj.empty();
				htmlTmpArr.push(binOLSSPRM13_global.liHTML);
				// 添加产品按钮
				getPromotionProHTML(htmlTmpArr);
				// 添加促销产品名,产品数量
				getPromotionProHTML2(htmlTmpArr,selectedPrmUnitCode,selectedPrmBarCode,selectedPrmName,prmCate,prmCateName);
				// 小添加按钮
				getAddPromotionRewardHTML(htmlTmpArr);
				if (selectedPrmArr.length>1 || $ul_obj.find('> li').length>1){
				    // 小关闭按钮
				    getSmallCloseHTML(htmlTmpArr);
				}else{
					getSmallCloseHTML(htmlTmpArr,'hide');
				}

			    $li_obj.append (htmlTmpArr.join(''));
			}
			// 添加促销产品名,产品数量
			getPromotionProHTML2(htmlTmpArr,selectedPrmUnitCode,selectedPrmBarCode,selectedPrmName,prmCate,prmCateName);
		    binOLSSPRM13_global.thisClickObj.after(htmlTmpArr.join(''));
		}else {
			htmlTmpArr.push(binOLSSPRM13_global.liHTML);
			// 添加产品按钮
			getPromotionProHTML(htmlTmpArr);
			// 添加促销产品名,产品数量
			getPromotionProHTML2(htmlTmpArr,selectedPrmUnitCode,selectedPrmBarCode,selectedPrmName,prmCate,prmCateName);
			// 小添加按钮
			getAddPromotionRewardHTML(htmlTmpArr);
			
		    // 小关闭按钮
		    getSmallCloseHTML(htmlTmpArr);
		    $ul_obj.append(htmlTmpArr.join(''));
		}
	}
	
	if ($ul_obj.find('> li').length>1){
		$('#proRewardContent .close').show();
	}
	$('#promotionDialog').dialog('destroy');
}


/**
 * 打开编辑逻辑条件box
 * @param thisObj
 * @return
 */
function openEditLogicBox(thisObj){
	var $span_obj = $(thisObj).parent();
	$('#editLogicInBox li').removeClass('on');
	if ($span_obj.attr('class').indexOf('logic-and')>0){
		$('#editLogicBox_and').addClass('on');
	}else if ($span_obj.attr('class').indexOf('logic-or')>0){
		$('#editLogicBox_or').addClass('on');
	}
	var opos = $(thisObj).offset();
	var oleft = parseInt(opos.left, 10);
	var otop = parseInt(opos.top + $(thisObj).outerHeight(), 10);
	divHide('#editLogicBox');
	$('#editLogicBox').css({'width':'250px','left': oleft + "px", 'top': otop }).show();
	binOLSSPRM13_global.thisClickObj = $(thisObj);
}

/**
 * 编辑逻辑条件box确定
 * @return
 */
function editLogicBoxOK(){
	var $span_obj = binOLSSPRM13_global.thisClickObj.parent();
	$span_obj.empty();
	var htmlTmpArr = new Array();
	if ($('#editLogicBox_or').attr('class') == 'on'){
		$span_obj.removeClass('logic-and');
		$span_obj.addClass('logic-or');
		htmlTmpArr.push(BINOLSSPRM13_js_i18n.conditionOr);
		getEditLogicConditionHTML(htmlTmpArr);
		$span_obj.html(htmlTmpArr.join(''));
	}else if ($('#editLogicBox_and').attr('class') == 'on'){
		$span_obj.removeClass('logic-or');
		$span_obj.addClass('logic-and');
		htmlTmpArr.push(BINOLSSPRM13_js_i18n.conditionAnd);
		getEditLogicConditionHTML(htmlTmpArr);
		$span_obj.html(htmlTmpArr.join(''));
	}
	
	$('#editLogicBox').hide();
}

/**
 * 打开奖励促销金额box
 * @return
 */
function openRewardAmountBox(thisObj){
	var opos = $(thisObj).offset();
	var oleft = parseInt(opos.left, 10);
	var otop = parseInt(opos.top + $(thisObj).outerHeight(), 10);
	divHide('#rewardAmountBox');  
	$('#rewardAmountBox').css({'left': oleft + "px", 'top': otop }).show();
	binOLSSPRM13_global.thisClickObj = $(thisObj);
}

/**
 * 选择增加或者减少金额
 * @return
 */
function rewardAmount(type){
	var $li_obj = binOLSSPRM13_global.thisClickObj.parents('li').first();
	var $ul_obj = binOLSSPRM13_global.thisClickObj.parents('ul').first();
	var htmlTmpArr = new Array();
	// 减少金额
	if (type == 'decrease'){
		// 如果画面上不存在减少金额
		if ($li_obj.find('.reward-amount-decrease').length ==0){
			$li_obj.empty();
			getPromotionAmountHTML(htmlTmpArr);
			getPromotionAmountHTML2(htmlTmpArr);
			// 小添加按钮
			getAddPromotionRewardHTML(htmlTmpArr);
			
		    // 小关闭按钮
		    getSmallCloseHTML(htmlTmpArr,'hide');
		    $li_obj.append(htmlTmpArr.join(''));
		}
	}else if (type == 'increase'){
		// 增加金额(加价购)
		// 如果画面上不存在增加金额
		if ($li_obj.find('.reward-amount-increase').length ==0){
			$li_obj.empty();
			getPromotionAmountHTML(htmlTmpArr);
			getPromotionAmountHTML3(htmlTmpArr);
			// 小添加按钮
			getAddPromotionRewardHTML(htmlTmpArr);
		    // 小关闭按钮
		    getSmallCloseHTML(htmlTmpArr);
		    $li_obj.append(htmlTmpArr.join(''));
		    if ($ul_obj.find(' .rewardPrm').length == 0){
		    	var htmlTmpArr2 = new Array();
		    	htmlTmpArr2.push(binOLSSPRM13_global.liHTML);
				// 添加促销产品按钮
				getPromotionProHTML(htmlTmpArr2);
				// 小添加按钮
				getAddPromotionRewardHTML(htmlTmpArr2);
			    // 小关闭按钮
			    getSmallCloseHTML(htmlTmpArr2);
			    $ul_obj.append(htmlTmpArr2.join(''));
		    }
		}
	}
	
	if ($ul_obj.find('> li').length>1){
		$('#proRewardContent .close').show();
	}
	// 隐藏奖励金额box
	$('#rewardAmountBox').hide();

}

/**
 * 取得购买产品HTML
 * @param htmlTmpArr
 * @return
 */
function getProductHtml(htmlTmpArr,closeFlag){
	// 详细画面
	if (binOLSSPRM13_global.showType=="detail"){
		htmlTmpArr.push('<strong>'+BINOLSSPRM13_js_i18n.buyProducts+'</strong>');
	}else{
		// 购买产品选择按钮
		htmlTmpArr.push ('<a class="ui-select ui-select-2" onclick="PRM13.openProDialog(this);" style="margin-left:0px;"><span class="button-text">'+ BINOLSSPRM13_js_i18n.buyProducts+'</span><span class="ui-icon ui-icon-triangle-1-s"></span></a>');
	}
	
	if (closeFlag==null){
	    // 小关闭按钮
	    getSmallCloseHTML(htmlTmpArr);
	}
}

/**
 * 取得购买产品HTML2(数量,产品条件)
 * @param htmlTmpArr
 * @param nameTotal
 * @return
 */
function getProductHtml2 (htmlTmpArr,nameTotal,prtVendorId,text,count){
	// 产品名称
	htmlTmpArr.push('\n<span class="highlight">');
	htmlTmpArr.push(nameTotal);
	htmlTmpArr.push('</span>');

	// 产品数量标题
	htmlTmpArr.push('<label>'+BINOLSSPRM13_js_i18n.prmProductCount+'</label>');
	// 比较条件
	getCompareOperatorHTML(htmlTmpArr,text);
	if (count==null){
		count = "1";
	}
	// 详细画面
	if (binOLSSPRM13_global.showType=="detail"){
		htmlTmpArr.push('<span class="highlight2">'+count+'</span>');
	}else{
		// 产品数量输入框
		htmlTmpArr.push ('<input type="text" style="width: 30px;" value="'+count+'" class = "product-count number" name="productCount"/>');
	}
	// 添加hidden值,用来记录产品信息
	htmlTmpArr.push ('<input type="hidden" name="prtVendorId" value="'+prtVendorId+'"/>');
	htmlTmpArr.push ('<input type="hidden" name="nameTotal" value="'+nameTotal+'"/>');
}

/**
 * 取得购买金额HTML(消费金额按钮)
 * @param htmlTmpArr
 * @return
 */
function getAmountHtml(htmlTmpArr,closeFlag){
	// 详细画面
	if (binOLSSPRM13_global.showType=="detail"){
		htmlTmpArr.push('<strong>'+BINOLSSPRM13_js_i18n.expenseAmount+'</strong>');
	}else{
		// 消费金额选择按钮
	    htmlTmpArr.push('<a class="ui-select ui-select-2 amount-select" onclick="openAmountBox(this);"><span class="button-text">'+BINOLSSPRM13_js_i18n.expenseAmount+'</span><span class="ui-icon ui-icon-triangle-1-s"></span></a> \n');
	}
    
	if (closeFlag == null){
	    // 小关闭按钮
	    getSmallCloseHTML(htmlTmpArr);
	}
}

/**
 * 取得比较运算HTML(大于小于..)
 * @param htmlTmpArr
 * @return
 */
function getCompareOperatorHTML(htmlTmpArr,operateText){
	if (operateText==null){
		operateText = BINOLSSPRM13_js_i18n.moreThen;
	}
	if (binOLSSPRM13_global.showType=="detail"){
		htmlTmpArr.push('<label>'+ operateText +'</label>');
	}else{
		htmlTmpArr.push('<a class="ui-select ui-select-3" onclick="openCompareOperator(this);"><span class="button-text">'+operateText+'</span><span class="ui-icon ui-icon-triangle-1-s"></span></a> \n');
	}
	
}

/**
 * 取得购买金额HTML2(金额值)
 * @param htmlTmpArr
 * @return
 */
function getAmountHtml2(htmlTmpArr,operateText,amountValue){
	// 取得比较运算HTNL
	getCompareOperatorHTML(htmlTmpArr,operateText);
	if (amountValue==null){
		amountValue = "100";
	}
	if (binOLSSPRM13_global.showType=="detail"){
		htmlTmpArr.push('<span class="highlight2">'+amountValue +'</span>');
	}else{
		// 金额输入框
		htmlTmpArr.push('<input type="text" style="width: 30px;" value="'+ amountValue +'" class = "amount-val number" name="amountVal"/> \n');
	}


	// 金额单位
	htmlTmpArr.push('<span class="highlight">'+BINOLSSPRM13_js_i18n.yuan+'</span> \n');
}


/**
 * 取得购买金额HTML3(金额范围)
 * @param htmlTmpArr
 * @param scopeStart
 * @param scopeEnd
 * @return
 */
function getAmountHtml3(htmlTmpArr,scopeStart,scopeEnd){
	// 金额起始范围
	htmlTmpArr.push('<span class = "highlight amount-scope-start" >');
	htmlTmpArr.push(scopeStart);
	htmlTmpArr.push('</span>');
	htmlTmpArr.push('<span class = "highlight">'+BINOLSSPRM13_js_i18n.yuan + '</span>');
	htmlTmpArr.push(' - ');
	// 金额结束范围
	htmlTmpArr.push('<span class = "highlight amount-scope-end">');
	htmlTmpArr.push(scopeEnd);
	htmlTmpArr.push('</span>');
	htmlTmpArr.push('<span class = "highlight">'+BINOLSSPRM13_js_i18n.yuan + '</span>');
}

/**
 * 取得小关闭按钮HTML
 * @param htmlTmpArr
 * @return
 */
function getSmallCloseHTML(htmlTmpArr,type){
	if (binOLSSPRM13_global.showType=="detail"){
		return;
	}
	if (type!=null && type=="hide"){
		htmlTmpArr.push('<span class="close" onclick="removeSingleCondition(this);" style="display:none"><span class="ui-icon ui-icon-close"></span></span>');
	}else{
		htmlTmpArr.push('<span class="close" onclick="removeSingleCondition(this);" ><span class="ui-icon ui-icon-close"></span></span>');
	}
	
}

/**
 * 取得大添加关闭按钮HTML
 * @param htmlTmpArr
 * @return
 */
function getBigAddCloseHTML(htmlTmpArr){
	htmlTmpArr.push('</span><span class="right"><a class="add-big" onclick="openConditionBox(this);"><span class="ui-icon icon-add-big"></span></a><a class="delete-big" onclick="removeGroupCondition(this);"><span class="ui-icon icon-delete-big"></span></a></span></div>');
}
/**
 * 取得逻辑条件HTML
 * @param htmlTmpArr
 * @return
 */
function getRuleConditionHtml(htmlTmpArr,logicCondition){
	htmlTmpArr.push('<div class="item-header clearfix"><span class="item-title left ');
	if (logicCondition =='logicCon_or'){
		htmlTmpArr.push ('logic-or">');
		htmlTmpArr.push (BINOLSSPRM13_js_i18n.conditionOr);
		// 逻辑条件编辑按钮
		getEditLogicConditionHTML(htmlTmpArr);
	}else if (logicCondition =='logicCon_and'){
		htmlTmpArr.push ('logic-and">');
		htmlTmpArr.push ( BINOLSSPRM13_js_i18n.conditionAnd);
		// 逻辑条件编辑按钮
		getEditLogicConditionHTML(htmlTmpArr);
	}
	if (binOLSSPRM13_global.showType!="detail"){
		getBigAddCloseHTML(htmlTmpArr);
	}else{
		htmlTmpArr.push('</span></div>');
	}

}

/**
 * 取得编辑逻辑条件HTML
 * @param htmlTmpArr
 * @return
 */
function getEditLogicConditionHTML(htmlTmpArr){
	if (binOLSSPRM13_global.showType!="detail"){
		htmlTmpArr.push ('<span class="ui-icon icon-ttl-section-info-edit" onclick ="openEditLogicBox(this);"></span>');
	}
}


/**
 * 取得促销产品HTML(促销产品选择)
 * @param htmlTmpArr
 * @return
 */
function getPromotionProHTML (htmlTmpArr){
	htmlTmpArr.push ('<a class="ui-select ui-select-2 rewardPrm" onclick="openPromotionSearchBox(this);return false;"><span class="button-text promotion-product">'+BINOLSSPRM13_js_i18n.presents+'</span><span class="ui-icon ui-icon-triangle-1-s"></span></a>');
}

/**
 * 取得添加促销奖励按钮
 * @param htmlTmpArr
 * @return
 */
function getAddPromotionRewardHTML (htmlTmpArr){
	htmlTmpArr.push('<span class="add reward" onclick="openRewardBox(this);return false;"><span class="ui-icon ui-icon-plus"></span></span>');
}

/**
 * 取得促销产品HTML2(数量,促销品信息)
 * @param htmlTmpArr
 * @return
 */
function getPromotionProHTML2 (htmlTmpArr,selectedPrmUnitCode,selectedPrmBarCode,selectedPrmName,prmCate,prmCateName){
	htmlTmpArr.push('\n<span class="highlight promotionName">');
	htmlTmpArr.push(selectedPrmName);
	htmlTmpArr.push('</span>');
	htmlTmpArr.push('<label>'+prmCateName+'</label>');
	htmlTmpArr.push('<label>'+BINOLSSPRM13_js_i18n.prmProductCount+'</label>');
	htmlTmpArr.push('<input type="text" name="prmPresentCount" value="1" style="width:30px;" class="number prmPresentCount" maxlength="4" >');
	// 添加hidden值,用来记录促销品信息
	htmlTmpArr.push ('<input type="hidden" class = "prmPresentUnitCode"  value="'+ selectedPrmUnitCode +'" name="prmPresentUnitCode" />');
	htmlTmpArr.push ('<input type="hidden" class = "prmPresentBarCode"  value="'+ selectedPrmBarCode +'" name="prmPresentBarCode" />');
	htmlTmpArr.push ('<input type="hidden" class = "prmCate"  value="'+ prmCate +'" name="prmCate" />');
}

/**
 * 取得促销购买金额HTML
 * @param htmlTmpArr
 * @return
 */
function getPromotionAmountHTML (htmlTmpArr){
	htmlTmpArr.push ('<a class="ui-select ui-select-2" onclick="openRewardAmountBox(this);return false;"><span class="button-text promotion-amount">'+BINOLSSPRM13_js_i18n.amountRegards+'</span><span class="ui-icon ui-icon-triangle-1-s"></span></a>');
}

/**
 * 取得促销购买金额HTML(减少金额)
 * @param htmlTmpArr
 * @return
 */
function getPromotionAmountHTML2(htmlTmpArr){
	htmlTmpArr.push ('<span class="highlight reward-amount-decrease"> '+BINOLSSPRM13_js_i18n.amountDecrease+'</span>&nbsp; <input type="text" style="width: 30px;" name= "prmRewardAmount" value="100" class="number prmRewardAmount" maxlength="4" />&nbsp;<span class="highlight"> '+BINOLSSPRM13_js_i18n.yuan+'</span>');
}

/**
 * 取得促销购买金额HTML(增加金额)
 * @param htmlTmpArr
 * @return
 */
function getPromotionAmountHTML3(htmlTmpArr){
	htmlTmpArr.push ('<span class="highlight reward-amount-increase"> '+BINOLSSPRM13_js_i18n.amountIncrease+'</span>&nbsp; <input type="text" style="width: 30px;" name="prmRewardAmount" value="100" class="number prmRewardAmount" maxlength="4" />&nbsp;<span class="highlight"> '+BINOLSSPRM13_js_i18n.yuan+'</span>');
}

/**
 * 隐藏弹出div
 * @param divId
 * @return
 */
function divHide (divId){
	
	$('#conditionBox').hide();
	$('#logicOperator').hide();
	if (divId!="#amountScope"){
		$('#amountBox').hide();  
	}
	$('#amountScope').hide();
	$('#rewardBox').hide();
	$('#editLogicBox').hide();
	$('#rewardAmountBox').hide();
//	var $div = $(divId);
	var $body = $("body");
	var firstFlag = true;
	$body.unbind('click');
	// 隐藏弹出的DIV
	$body.bind('click',function(event){
		if(!firstFlag) {
			var skipFlag = false;
			var parents = $(event.target).parents();
			for (var i=0;i<parents.length;i++){
				var parent = parents[i];
				if ('#'+$(parent).attr('id') == divId){
					skipFlag = true;
					break;
				}
			}
			
			if (!skipFlag){
				$('#conditionBox').hide();
				$('#logicOperator').hide(); 
				$('#amountBox').hide();    
				$('#amountScope').hide();
				$('#rewardBox').hide();
				$('#editLogicBox').hide();
				$('#rewardAmountBox').hide();
				$body.unbind('click');
			}
		}

		firstFlag = false;
	});
}

/**
 * 将规则体页面转换成规则对象
 * @param $ruleBody
 * @return
 */
function parseRuleBody2RuleJson($ruleBodyObj){
	/**
	 * 规则体对象
	 * ruleBodyJson
	 * 		Array arr[conditionObj]:条件对象数组
	 */
	
	/**
	 * 条件对象
	 * conditionObj
	 * 		String conditionVal:条件值(logicCon_or:满足任意条件,logicCon_and:满足全部条件)
	 */
	
	/**
	 * 金额对象
	 * amountObj
	 * 		String amountType:金额类型(0:金额值,1:金额范围)
	 * 		String operatorVal:比较值(0:小于,1:小于等于,2:等于,3:不等于,4:大于,5:大于等于)
	 * 		String amount:金额值(type为0时设定)
	 * 		String startAmount:起始金额(type为1时设定)
	 * 		String endAmount:结束金额(type为1时设定)
	 */
	
	/**
	 * 产品对象
	 * prtObj
	 * 		String prtName:产品名称
	 * 		String countOperatorVal:数量比较值(0:小于,1:小于等于,2:等于,3:不等于,4:大于,5:大于等于)
	 * 		String count:产品数量
	 * 		String prtUnitCode:产品unitCode
	 * 		String prtBarCode:产品条码
	 */
	 var buf;
	 if ($ruleBodyObj.hasClass('items')){
        buf = [];   
        var  i = $ruleBodyObj.find(' >li').length; 
        while  (i--) {   
            buf[i] = parseRuleBody2RuleJson($($ruleBodyObj.find(' >li')[i]));   
        }   
        return  buf;  
	 }else if ($ruleBodyObj.hasClass('item')){
		buf = {};   
		if ($ruleBodyObj.find(' >.item-header')!=null && $ruleBodyObj.find(' >.item-header').length>0){
			// 该对象是条件对象
			var $conditionBody = $($ruleBodyObj.find(' >.item-header >.item-title')[0]);
			buf.itemType = 'conditionObj';
			if ($conditionBody.hasClass('logic-or')){
				// 设定条件值:or
				buf.conditionVal = "logicCon_or";
			}else if ($conditionBody.hasClass('logic-and')){
				// 设定条件值:and
				buf.conditionVal = "logicCon_and";
			}
		}else{
			if ($ruleBodyObj.find(' >a')!=null && $($ruleBodyObj.find(' >a')[0]).hasClass('amount-select')){
				var amountBody = $($ruleBodyObj.find(' >a'));
				// 该对象是金额对象
				buf.itemType = 'amountObj';
				if (amountBody.hasClass('ui-select-3')){
					// 金额类型：金额值
					buf.amountType = '0';
					// 比较值
					var oparatorBody= $($ruleBodyObj.find(' >.ui-select-3')[0]);
					var text = $(oparatorBody.find('.button-text')[0]).html();
					if (text == BINOLSSPRM13_js_i18n.lessThan){
						buf.operatorVal = '0';
					}else if (text == BINOLSSPRM13_js_i18n.lessThanOrEquel){
						buf.operatorVal = '1';
					}else if (text == BINOLSSPRM13_js_i18n.equel){
						buf.operatorVal = '2';
					}else if (text == BINOLSSPRM13_js_i18n.notEquel){
						buf.operatorVal = '3';
					}else if (text == BINOLSSPRM13_js_i18n.moreThenOrEquel){
						buf.operatorVal = '4';
					}else if (text == BINOLSSPRM13_js_i18n.moreThen){
						buf.operatorVal = '5';
					}
					// 金额值
					buf.amount = $($ruleBodyObj.find(' >.amount-val')[0]).val();
				}else{
					// 金额类型：金额范围
					buf.amountType = '1';
					buf.startAmount = $($ruleBodyObj.find(' >.amount-scope-start')[0]).html();
					buf.endAmount = $($ruleBodyObj.find(' >.amount-scope-end')[0]).html();
				}
				
			}else{
				// 该对象购买产品
				buf.itemType = 'prtObj';
				// 比较值
				var oparatorBody= $($ruleBodyObj.find(' >.ui-select-3')[0]);
				var text = $(oparatorBody.find('.button-text')[0]).html();
				if (text == BINOLSSPRM13_js_i18n.lessThan){
					buf.countOperatorVal = '0';
				}else if (text == BINOLSSPRM13_js_i18n.lessThanOrEquel){
					buf.countOperatorVal = '1';
				}else if (text == BINOLSSPRM13_js_i18n.equel){
					buf.countOperatorVal = '2';
				}else if (text == BINOLSSPRM13_js_i18n.notEquel){
					buf.countOperatorVal = '3';
				}else if (text == BINOLSSPRM13_js_i18n.moreThenOrEquel){
					buf.countOperatorVal = '4';
				}else if (text == BINOLSSPRM13_js_i18n.moreThen){
					buf.countOperatorVal = '5';
				}
				// 产品名
				buf.prtName = $ruleBodyObj.children(':input[name="nameTotal"]').val();
				buf.prtVendorId = $ruleBodyObj.children(':input[name="prtVendorId"]').val();
				buf.count = $ruleBodyObj.children(':input[name="productCount"]').val();
			}
		}
		if ($ruleBodyObj.find(' >ul').length>0){
			buf.arr = parseRuleBody2RuleJson($($ruleBodyObj.find(' >ul')[0]));
		}
        return  buf; 
	 }else{
		return $ruleBodyObj;
	 }
}

function getRuleHtml(ruleJsonObj){
	var ruleBody = parseRuleJson2RuleBody(ruleJsonObj);
	
	var ruleBodyHtml = JSON2.stringify(ruleBody);
	ruleBodyHtml = ruleBodyHtml.replace(/("htmlVal":")|(",)|("htmlArr":)|(,)|(\\n)/g,"").replace(/\[/g,"<ul class=\"items\">").replace(/\]/g,"</ul>").replace(/\{/g,"<li class=\"item\">").replace(/\}/g,"</li>").replace(/\\\"/g,"\"").replace(/\\u000a/g,"\\n");
	ruleBodyHtml = ruleBodyHtml.replace(/\"\<\/li\>/g,"</li>");
	$('#rule-detail-content').html(ruleBodyHtml);
	
	if ($('#rule-detail-content >ul').find(' >li').length>1){
		// 当有多个同层的条件时，需要将删除按钮一并显示
		$('#rule-detail-content .delete-big').show();
	}else if ($('#rule-detail-content >ul').find('li').length>1){
		// 只有一个同层条件时，最外层的条件的删除按钮不予显示
		$('#rule-detail-content .delete-big').show();
		$('#rule-detail-content .delete-big').first().hide();
	}else{
		$('#rule-detail-content .delete-big').first().hide();
	}
}

/**
 * 将规则体对象转换成规则页面
 * @return
 */
function parseRuleJson2RuleBody(ruleJsonObj){
	var  buf;   
    if  (ruleJsonObj  instanceof  Array) {   
        buf = [];   
        var  i = ruleJsonObj.length;   
        while  (i--) {   
        	buf[i] = parseRuleJson2RuleBody(ruleJsonObj[i]);   
        }   
        return  buf;   
    }else   if  (ruleJsonObj  instanceof  Object){   
        buf = {};   
        var htmlTmpArr = new Array();
        if (ruleJsonObj.itemType=='conditionObj'){
        	getRuleConditionHtml(htmlTmpArr,ruleJsonObj.conditionVal);
        	buf.htmlVal = htmlTmpArr.join('');	
        }else if (ruleJsonObj.itemType == 'amountObj'){
        	// 添加消费金额按钮
        	getAmountHtml(htmlTmpArr,true);
        	var text = null;
        	if (ruleJsonObj.operatorVal == "0"){
        		text = BINOLSSPRM13_js_i18n.lessThan;
        	}else if (ruleJsonObj.operatorVal == "1"){
        		text = BINOLSSPRM13_js_i18n.lessThanOrEquel;
        	}else if (ruleJsonObj.operatorVal == "2"){
        		text = BINOLSSPRM13_js_i18n.equel;
        	}else if (ruleJsonObj.operatorVal == "3"){
        		text = BINOLSSPRM13_js_i18n.notEquel;
        	}else if (ruleJsonObj.operatorVal == "4"){
        		text = BINOLSSPRM13_js_i18n.moreThenOrEquel;
        	}else if (ruleJsonObj.operatorVal == "5"){
        		text = BINOLSSPRM13_js_i18n.moreThen;
        	}
        	if (ruleJsonObj.amountType=="0"){
        		getAmountHtml2(htmlTmpArr,text,ruleJsonObj.amount);
        	}else if (ruleJsonObj.amountType=="1"){
        		getAmountHtml3(htmlTmpArr,ruleJsonObj.startAmount,ruleJsonObj.endAmount);
        	}
        	
    	    // 小关闭按钮
    	    getSmallCloseHTML(htmlTmpArr);
    	    
        	buf.htmlVal = htmlTmpArr.join('');
        }else if (ruleJsonObj.itemType == 'prtObj'){
        	getProductHtml(htmlTmpArr,true);
        	var text = null;
        	if (ruleJsonObj.countOperatorVal == "0"){
        		text = BINOLSSPRM13_js_i18n.lessThan;
        	}else if (ruleJsonObj.countOperatorVal == "1"){
        		text = BINOLSSPRM13_js_i18n.lessThanOrEquel;
        	}else if (ruleJsonObj.countOperatorVal == "2"){
        		text = BINOLSSPRM13_js_i18n.equel;
        	}else if (ruleJsonObj.countOperatorVal == "3"){
        		text = BINOLSSPRM13_js_i18n.notEquel;
        	}else if (ruleJsonObj.countOperatorVal == "4"){
        		text = BINOLSSPRM13_js_i18n.moreThenOrEquel;
        	}else if (ruleJsonObj.countOperatorVal == "5"){
        		text = BINOLSSPRM13_js_i18n.moreThen;
        	}
        	getProductHtml2 (htmlTmpArr,ruleJsonObj.prtName,ruleJsonObj.prtVendorId,text,ruleJsonObj.count);
    	    // 小关闭按钮
    	    getSmallCloseHTML(htmlTmpArr);
    	    buf.htmlVal = htmlTmpArr.join('');
        }
        if (ruleJsonObj.arr!=null && ruleJsonObj.arr.length>0){
        	buf.htmlArr = parseRuleJson2RuleBody(ruleJsonObj.arr);
        }
        return  buf;   
    }else {   
        return  ruleJsonObj;   
    } 
}

/**
 * 将规则体对象转换成DRL语句
 * @param ruleJsonObj
 * @return
 */
function parseRuleJson2RuleDrl(ruleJsonObj){
	var  buf;
	if  (ruleJsonObj  instanceof  Array) {
		buf = [];   
        var  i = ruleJsonObj.length;   
        while  (i--) {   
        	buf[i] = parseRuleJson2RuleDrl(ruleJsonObj[i]);   
        }  
        return buf;
	}else if (ruleJsonObj  instanceof  Object){
		buf="";
        if (ruleJsonObj.arr!=null && ruleJsonObj.arr.length>0){
        	var arr =  parseRuleJson2RuleDrl(ruleJsonObj.arr);
            if (ruleJsonObj.conditionVal!=null){
            	if (ruleJsonObj.conditionVal=="logicCon_or"){
            		buf +=arr.join(' || ');
            	}else if (ruleJsonObj.conditionVal=="logicCon_and"){
            		buf +=arr.join(' && ');
            	}           	
            }else{
            	buf +=arr.join('&&');
            }

            if (arr.length>1){
            	buf="("+buf+")";
            }
        }else{
        	var drlTmpArr = new Array();
        	if (ruleJsonObj.itemType == 'amountObj'){
        		//drlTmpArr.push('(');
        		//drlTmpArr.push('SaleInfoBean (');
        		if (ruleJsonObj.amountType=="0"){
        			// 消费金额key
        			drlTmpArr.push('amount');
        			// 比较运算符
                	if (ruleJsonObj.operatorVal == "0"){
                		drlTmpArr.push('<');
                	}else if (ruleJsonObj.operatorVal == "1"){
                		drlTmpArr.push('<=');
                	}else if (ruleJsonObj.operatorVal == "2"){
                		drlTmpArr.push('==');
                	}else if (ruleJsonObj.operatorVal == "3"){
                		drlTmpArr.push('!=');
                	}else if (ruleJsonObj.operatorVal == "4"){
                		drlTmpArr.push('>=');
                	}else if (ruleJsonObj.operatorVal == "5"){
                		drlTmpArr.push('>');
                	}
                	// 消费金额数值
                	drlTmpArr.push(ruleJsonObj.amount);
        		}else if (ruleJsonObj.amountType=="1"){
        			// 消费金额key
        			drlTmpArr.push('amount');
        			// 比较运算符
        			drlTmpArr.push('>=');
        			drlTmpArr.push(ruleJsonObj.startAmount);
        			drlTmpArr.push(' && ');
        			drlTmpArr.push('amount');
        			drlTmpArr.push('<=');
        			drlTmpArr.push(ruleJsonObj.endAmount);
        		}
        		//drlTmpArr.push('))');
        	}else if (ruleJsonObj.itemType == 'prtObj'){
//        		drlTmpArr.push('(');
//        		drlTmpArr.push('List(size>0) from collect');
//        		drlTmpArr.push('(');
//        		drlTmpArr.push('ProductBean (');
        		drlTmpArr.push('eval(RuleUtil.compareListKey($s.getSaleRecordDetailList(),"'+ ruleJsonObj.prtUnitCode +'",'+ruleJsonObj.count+','+ruleJsonObj.countOperatorVal+"))");
        		// 购买产品unitcode key
//        		drlTmpArr.push('prtUnitCode');
//        		drlTmpArr.push('== "');
//        		// 购买产品unitcode
//        		drlTmpArr.push(ruleJsonObj.prtUnitCode + '"');
//        		drlTmpArr.push(' && ');
//        		// 购买产品数量key
//        		drlTmpArr.push('prtCount');
//    			// 比较运算符
//            	if (ruleJsonObj.countOperatorVal == "0"){
//            		drlTmpArr.push('<');
//            	}else if (ruleJsonObj.countOperatorVal == "1"){
//            		drlTmpArr.push('<=');
//            	}else if (ruleJsonObj.countOperatorVal == "2"){
//            		drlTmpArr.push('==');
//            	}else if (ruleJsonObj.countOperatorVal == "3"){
//            		drlTmpArr.push('!=');
//            	}else if (ruleJsonObj.countOperatorVal == "4"){
//            		drlTmpArr.push('>=');
//            	}else if (ruleJsonObj.countOperatorVal == "5"){
//            		drlTmpArr.push('>');
//            	}
//            	// 购买产品数量
//            	drlTmpArr.push(ruleJsonObj.count);
//            	drlTmpArr.push(') from $s.productBean ))');
        	}
        	buf += drlTmpArr.join('');
        }
		return  buf;   
	}else{
		return  ruleJsonObj;   
	}
}

/**
 * 将时间地点条件数据转换成DRL
 * @return
 */
function parseTimeLocation2Drl(){
	
	/**
	 * 时间地点数据
	 * timeLocationData
	 * 		String conditionGrpId 模块组ID
	 * 		Array locationData 地点数据
	 * 		Array timeDataList 时间数据数组 
	 */
	
	/**
	 * 地点数据
	 * locationData
	 * 		List<String> locationDataList 地点ID(查询类型_ID)
	 * 		String locationType 地点类型
	 */
	
	/**
	 * 时间数据
	 * timeData
	 * 		String startTime 起始时间
	 * 		String endTime 地点数据 
	 */
	
	// 取得时间地点条件数据
	var timeLocationDataArr = binOLSSPRM13_global.timeLocationDataArr;
	
	var drlTmpArr= [];
	drlTmpArr.push('(');
	// 根据时间地点模块循环
	for (var i=0;i<timeLocationDataArr.length;i++){
		var timeLocationData = timeLocationDataArr[i];
		drlTmpArr.push('(');
		// 取得时间数据数组
		var timeDataList = timeLocationData.timeDataList;
		var timeTmpArr = [];
		// 循环时间数据数组
		for (var j=0;j<timeDataList.length;j++){
			var timeData = timeDataList[j];
			timeTmpArr.push('(saleTime>="'+timeData.startTime+'" &&  saleTime<="'+(timeData.endTime==""?$('#defaultDate').html():timeData.endTime)+'")');
		}
		if (timeTmpArr.length==1){
			drlTmpArr.push(timeTmpArr.join(' || ') + ' && ');
		}else{
			drlTmpArr.push('('+timeTmpArr.join(' || ') +')'+ ' && ');
		}
		
		// 取得地点类型
		var locationType = timeLocationData.locationData.locationType;
		// 取得地点数据数组
		var locationDataList = timeLocationData.locationData.locationDataList;
		// 循环地点数据数组
//		var locationTmpArr = [];
		
		if (locationDataList.length!=1){
			drlTmpArr.push('( ');
		}
		
		for (var j=0;j<locationDataList.length;j++){
			
			if (locationType == '2' || locationType == '4'){
				drlTmpArr.push('counterCode == ');
			}else if (locationType == '1' || locationType == '5'){
				drlTmpArr.push('cityID == ');
			}else if (locationType == '3'){
				drlTmpArr.push('channelID == ');
			}
			
			// 取得地点ID
			drlTmpArr.push('"' + locationDataList[j].split('_')[1] + '"');	
			if (j!=locationDataList.length-1){
				drlTmpArr.push(' || ');
			}
		}
		if (locationDataList.length!=1){
			drlTmpArr.push(')');
		}
		drlTmpArr.push(')');
		if (i!=timeLocationDataArr.length-1){
			drlTmpArr.push(' || ');
		}
	}
	drlTmpArr.push(') \n');
	return drlTmpArr.join('');
}

/**
 * 将促销奖励数据转换成DRL
 * @return
 */
function parsePromotionReward2Drl(){
	// 奖励金额
	var rewardAmount = 0;
	var drlTmpArr= [];
	drlTmpArr.push('List commodityDTOList = new ArrayList(); \n');
	var prmUnitCodeArr = $('#proRewardContent .prmPresentUnitCode');
	var prmBarCodeArr = $('#proRewardContent .prmPresentBarCode');
	var prmCountArr = $('#proRewardContent .prmPresentCount');
	for (var i=0;i<prmUnitCodeArr.length;i++){
		
		drlTmpArr.push('CommodityDTO commodityDTO'+i+' = new CommodityDTO(); \n');
		drlTmpArr.push('commodityDTO'+i+'.setUnitCode("'+$(prmUnitCodeArr[i]).val()+'"); \n');
		drlTmpArr.push('commodityDTO'+i+'.setBarCode("'+$(prmBarCodeArr[i]).val()+'"); \n');
		drlTmpArr.push('commodityDTO'+i+'.setQuantity('+$(prmCountArr[i]).val()+'); \n');
		drlTmpArr.push('commodityDTOList.add(commodityDTO'+i+'); \n'); 		
	}
	rewardAmount = $('#proRewardContent .prmRewardAmount').val();
	if (typeof (rewardAmount) == 'undefined'){
		rewardAmount = 0;
	}
	// 是否为减少
	if ($('#proRewardContent').find('.reward-amount-decrease').length>0){
		rewardAmount = parseInt(rewardAmount) * (-1);
	}
	
	drlTmpArr.push('RuleUtil.setPromotionReward($s,'+rewardAmount+',commodityDTOList,drools.getRule().getName()); \n'); 		
	return drlTmpArr.join('');
}


/**
 * 生成DRL规则部分
 * @return
 */
function createDrlJson (ruleJsonObj){
	var drlTmpArr= [];
	var drlTimeLocation = parseTimeLocation2Drl();
	var drlBodyArr = parseRuleJson2RuleDrl(ruleJsonObj);
	var drlBodyStr = drlBodyArr.join(' && ');
	var drlThenStr = parsePromotionReward2Drl();
	drlTmpArr.push('salience 0 \n');
	// when 条件
	drlTmpArr.push('\t when \n');
	drlTmpArr.push('\t\t ');
	drlTmpArr.push('$s:SaleRecordDTO (');
	drlTmpArr.push(drlTimeLocation);
	drlTmpArr.push('\n \t\t ');
	if (drlBodyStr){
		drlTmpArr.push('&& ');
	}
	drlTmpArr.push(drlBodyStr);
	drlTmpArr.push('\n ');
	drlTmpArr.push(')');
	// then 条件
	drlTmpArr.push('\n then \n \t\t ');
	drlTmpArr.push(drlThenStr);
	
	return drlTmpArr.join('');
}

/**
 * 预读柜台信息,编辑或者复制阶段，有可能不展开柜台就进行保存，这个时候需要预读柜台节点来写入DRL文件
 * @return
 */
function loadCounterNodes (url,ruleJson){
	var urlLoad = $('#checkCounterNodesUrl').html();
	var param = "timeLocationJSON="+JSON2.stringify(binOLSSPRM13_global.timeLocationDataArr)+"&activeID="+$('#activeID').val();
	if (typeof ($('#showType'))!='undefined' && ($('#showType').val()=='edit' || $('#showType').val()=='copy')){
		param = param + "&"+$('#showType').val();
	}
	
	cherryAjaxRequest({
		url:urlLoad,
		param:param,
		formId:"prmActiveform",
		callback:callback
	});
	
	function callback(data){
		if(data.indexOf('id="actionResultDiv"') > -1 || data.indexOf('id="fieldErrorDiv"') > -1) {
			return;
		}
		binOLSSPRM13_global.timeLocationDataArr = eval('('+data+')');
		
		var ruleDrl = createDrlJson(ruleJson);
		
		$('#ruleHTML').val(JSON2.stringify(ruleJson));

		$('#ruleDrl').val(ruleDrl);
		// form序列化
		var param = $('#prmActiveform').children(":input").serialize();
		param += "&" + $("#baseInfo").find(":input").serialize();
		cherryAjaxRequest({
			url:url,
			param:param,
			formId:"prmActiveform",
			callback:callbackUpdate
		});
		
		function callbackUpdate(msg){
			  if(window.opener.oTableArr[0] != null)window.opener.oTableArr[0].fnDraw();
		}
	}
}

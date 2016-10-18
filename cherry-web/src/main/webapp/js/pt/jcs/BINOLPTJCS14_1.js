/**
 * 全局变量定义
 */
var ptjcs14_1_global = {};
//添加活动组弹出框内容
ptjcs14_1_global.dialogHTML = '';
// 当前点击节点，（用于选中节点）
var ptjcs14_1_currnetClick = null;
// 当前点击节点,初始化勾选状态
var ptjcs14_1_currnetClickCheckedOld = null;

//日历起始日期
ptjcs14_1_global.calStartDate="";
// 假日
ptjcs14_1_global.holidays="";

//页面关闭
window.onbeforeunload = function() {
	if (window.opener) {
		window.opener.unlockParentWindow();
		
		// 刷新父页面的产品价格方案sel
		window.opener.JCS14.getPrtPriceSoluList();
	}
}

// 页面初期化方法
$(document).ready(function() {
	if (window.opener) {
		window.opener.lockParentWindow();
	}
	
	// 隐藏定位框
	$("#locationId_0").hide();
	
	// 添加产品价格方案
	$('#addPrtPrcieSolu').click(function(){
		if("" == ptjcs14_1_global.dialogHTML){
			ptjcs14_1_global.dialogHTML = $('#prt_price_solu_dialog_Main').html();
		}else{
			$('#prt_price_solu_dialog_Main').html(ptjcs14_1_global.dialogHTML);
		}
		
		$('#prt_price_solu_dialog .endTime').cherryDate({
			holidayObj: ptjcs14_1_global.holidays,
			// 结束时间大于起始时间
			beforeShow: function(input){										
				var value = $(input).parents("table").find(".startTime").val();	
				return [value,'minDate'];									
			}
		});
		
		$('#prt_price_solu_dialog .startTime').cherryDate({
			holidayObj: ptjcs14_1_global.holidays,
			// 开始时间小于起始时间
			beforeShow: function(input){										
				var value = $(input).parents("table").find(".endTime").val();	
				return [value,'maxDate'];									
			}
		});
		
		$('#prt_price_solu_dialog').dialog(
			{ autoOpen: false,  width: 350, height: 230, title:$("#addMsgTitle").text(), zIndex: 1,  modal: true, resizable:false,
			
			buttons: [{
				text:$("#globalSave").text(),
				click: function() {
					addPrtPrcieSolu();
				}
			},
			{	
				text:$("#globalCancle").text(),
				click: function() {
					$("#prt_price_solu_dialog").dialog("destroy");
					 $("#prt_price_solu_dialog").remove();
				}
			}],
			close: function() {
				$("#prt_price_solu_dialog").dialog("destroy");
				 $("#prt_price_solu_dialog").remove();
			}
		});
		$("#prt_price_solu_dialog").dialog("open");
	});
	
	// 编辑产品价格方案
	$('#editPrtPrcieSolu').click(function(){
		if("" == ptjcs14_1_global.dialogHTML){
			ptjcs14_1_global.dialogHTML = $('#prt_price_solu_dialog_Main').html();
		}else{
			$('#prt_price_solu_dialog_Main').html(ptjcs14_1_global.dialogHTML);
		}
		
		// 将当前选中的方案赋给编辑DIV
//		$("#solutionName").val($("#productPriceSolutionID").find("option:selected").text());
//		var list = eval("(" + $("#prtPriceSolutionListId").val() + ")");
		var list = JSON.parse($("#prtPriceSolutionListId").val());
		$.each(list, function(i,item){
//			alert(JSON.stringify(item));
			if($("#productPriceSolutionID").val() == item.solutionID){
				// 方案名称
				if(item.solutionName){
					$("#solutionName").val(item.solutionName);
				}
				// 备注
				if(item.comments){
					$("#comments").val(item.comments);
				}
				// 生效开始日期
				if(item.startDate){
					$("#soluStartTime").val(item.startDate);
				}
				// 生效结束日期
				if(item.endDate){
					$("#soluEndTime").val(item.endDate);
				}
				
			}
		});
		
		
		$('#prt_price_solu_dialog .endTime').cherryDate({
			holidayObj: ptjcs14_1_global.holidays,
			// 结束时间大于起始时间
			beforeShow: function(input){										
				var value = $(input).parents("table").find(".startTime").val();	
				return [value,'minDate'];									
			}
		});
		
		$('#prt_price_solu_dialog .startTime').cherryDate({
			holidayObj: ptjcs14_1_global.holidays,
			// 开始时间小于起始时间
			beforeShow: function(input){										
				var value = $(input).parents("table").find(".endTime").val();	
				return [value,'maxDate'];									
			}
		});
		
		$('#prt_price_solu_dialog').dialog(
				{ autoOpen: false,  width: 350, height: 230, title:$("#editMsgTitle").text(), zIndex: 1,  modal: true, resizable:false,
					
					buttons: [{
						text:$("#globalSave").text(),
						click: function() {
							editPrtPrcieSolu();
						}
					},
					{	
						text:$("#globalCancle").text(),
						click: function() {
							$("#prt_price_solu_dialog").dialog("destroy");
							$("#prt_price_solu_dialog").remove();
						}
					}],
					close: function() {
						$("#prt_price_solu_dialog").dialog("destroy");
						$("#prt_price_solu_dialog").remove();
					}
				});
		$("#prt_price_solu_dialog").dialog("open");
	});
	
});

var ptjcs14_1 = function(){};

/**
 * 点击事件
 * @param event
 * @param treeId
 * @param treeNode
 * @param clickFlag
 */
function clickEvent	(event, treeId, treeNode, clickFlag){
	//alert(JSON.stringify(treeNode));
	// 当前点击节点
	ptjcs14_1_currnetClick = treeNode;
	ptjcs14_1_currnetClickCheckedOld = ptjcs14_1_currnetClick.checked;
	
	// 清空prtJson,prtSaveJson
	$("#prtJson_0").val('');
	$("#prtSaveJson_0").val('');
	
	// 清空消息
	$("#msgDIV").empty();
	
	var isPrt = treeNode.isPrt;
	if(isPrt){
		// 取得产品信息及产品方案价格（没有则取原品牌价格）
		
		$("#objName").text(treeNode.name);
		$("#productID").val(treeNode.path);
		
		var param = $("#mainForm").serialize();
		param += "&path="+ treeNode.path;
		var parentToken = getParentToken();
		param += "&" + parentToken;
		
		cherryAjaxRequest({
			url: $("#getPrtDetailInfoId").val(),
			param: param,
			callback: function(data){
				var map = eval("(" + data + ")");
				
				// 显示mainPanel
				var $mainPanel = $("#mainPanel");
				$mainPanel.removeClass('hide');
				if(!$mainPanel.hasClass('show')){
					$mainPanel.addClass('show');
				}
				
				// 根据价格来源，确定是否显示去除方案中产品的Button // 1：来自品牌价格， 2：来自产品方案
				if(map.priceFrom == "1"){
					$("#delBtnId").css('display','none'); 
				}else {
					$("#delBtnId").css('display','');
				}

				// 显示销售最高价最低价
				$("#minSalePriceDesc").text(map.proMap.minSalePrice);
				$("#minSalePrice").val(map.proMap.minSalePrice);
				$("#maxSalePriceDesc").text(map.proMap.maxSalePrice);
				$("#maxSalePrice").val(map.proMap.maxSalePrice);
//				alert("map.productPriceSolutionDetailID " + map.productPriceSolutionDetailID);
				if(map.productPriceSolutionDetailID){
					$("#productPriceSolutionDetailID").val(map.productPriceSolutionDetailID);
				} else {
					$("#productPriceSolutionDetailID").val('');
				}

				var $priceInfo = $("#priceInfo");
				// 清空价格div
				$priceInfo.empty();
				var html ='';
				
				// 显示价格
				$.each(map.priceList, function(i,prcieInfo){
					var len = map.priceList.length;
//					alert("len: "+ map.priceList.length);
					html += '<table class="detail" cellpadding="0" cellspacing="0">';
//					html += '<caption style="height:20px;">';
//					if(len > 1){
//						// <span class="delBtn <s:if test="%{compareDateFlag}"> hide </s:if>">
//						html += '<span class="delBtn ';
////						alert("compareDateFlag:  " +prcieInfo.compareDateFlag);
//						if(prcieInfo.compareDateFlag){
//							html += 'hide';
//						}
//						html += '"> ';
//						html += '<a class="delete right" href="javascript:void(0)" onclick="ptjcs14_1.delDivRow(this);return false;">';
//						html += '<span class="ui-icon icon-delete"></span><span class="button-text">'+$("#globalDelete").text()+'</span>'; //<%-- 删除按钮 --%>
//						html += '</a></span>';
//					}
//					html += '</caption>';
					html += '<tbody>';
					html += '<tr>';
					html += '<th>';
					html += '<label>'+$("#salePriceTxt").text()+'<span class="highlight"><span class="highlight">*</span></span></label>';
						
					if(prcieInfo.prtPriceId){
						html += '<input type="hidden" name="prtPriceId" value="'+prcieInfo.prtPriceId+'"/>';
					}
					html += '<input type="hidden" name="option" value="1"/>';
					html += '</th>'; // <%-- 销售价格 --%>
					html += '<td>';
					html += '<span>';
					if(prcieInfo.compareDateFlag){
						
						html += '<input type="text" name="salePrice" class="price" maxlength="17" value="'+ prcieInfo.salePrice + '" onkeyup="ptjcs14_1.setMemPrice(this);return false;" disabled="true"/> 元';
					}else{
						html += '<input type="text" name="salePrice" class="price" maxlength="17" value="' + prcieInfo.salePrice + '" onkeyup="ptjcs14_1.setMemPrice(this);return false;" /> 元';
					}
					html += '</span>';
					html += '</td>';
					html += '<th>';
					html += '<span style="background-color:#FFDDBB;border:1px solid red; padding:2px;">'+$("#memPriceTxt").text()+'</span>';
					html += '<span class="';
					if(prcieInfo.compareDateFlag){
						html += 'hide';
					}
					html += '"><span class="calculator" title="'+$("#calculatorPriceTxt").text()+'" onclick="ptjcs14_1.initRateDiv(this);"></span></span>';
					html += '</th>';
					html += '<td>';
					html += '<span>';
					if(prcieInfo.compareDateFlag){
						html += '<input type="text" name="memPrice" class="price" maxlength="17" value="'+prcieInfo.memPrice+'" disabled="true"/>'+$("#moneryUnitTxt").text();
					}else{
						html += '<input type="text" name="memPrice" class="price" maxlength="17" value="'+prcieInfo.memPrice+'"/>'+$("#moneryUnitTxt").text();
					}
					html += '</span>';
					html += '</td>';
					html += '</tr>';
					
//					html += '<tr>';
//					html += '<th><label>'+$("#startDateTxt").text()+'<span class="highlight"><span class="highlight">*</span></span></label></th>'; // <%-- 生效日期 --%>
//						
//					
//					html += '<td>';
//					html += '<span>';
//					if(prcieInfo.compareDateFlag){
//						html += '<input type="text" id="" name="priceStartDate" value="' + prcieInfo.startDate + '" style="width:80px;" disabled="true"/>';
//					}else {
//						html += '<input type="text" id="" name="priceStartDate" class="date" value="' + prcieInfo.startDate + '" style="width:80px;"/>';
//					}
//					
//					html += '</span>';
//					html += '</td>';
//					html += '<th><label>'+$("#endDateTxt").text()+'</label></th>'; // <%-- 失效日期 --%>
//					html += '<td>';
//					html += '<span>';
//					if(prcieInfo.compareDateFlag){
//						html += '<input type="text" id="" name="priceEndDate" value="';
//						if(prcieInfo.endDate){
//							html += prcieInfo.endDate;
//						}
//						html += '" style="width:80px;" disabled="true"/>';
//					}else{
//						html += '<input type="text" id="" name="priceEndDate" class="date" value="';
//						if(prcieInfo.endDate){
//							html += prcieInfo.endDate;
//						}
//						html += '" style="width:80px;"/>';
//					}
//					html += '</span>';
//					html += '</td>';
//					html += '</tr>';
					
					
					html += '</tbody>';
					html += '</table>';
					
				});
				
				$priceInfo.append(html);
				initSalePrice();

			},	
			coverId:"#div_main",
			loadFlg:true
		});
		
	} else {
//		alert("cate");
		// 取得分类下的产品ID
		// 显示mainPanel
		var $mainPanel = $("#mainPanel");
		$mainPanel.removeClass('show');
		if(!$mainPanel.hasClass('hide')){
			$mainPanel.addClass('hide');
		}
	}
	var prtArr = new Array();
}

/**
 * check前操作(禁止手动勾选)
 */
function zTreeBeforeCheck(treeId, treeNode) {
    return false;
};

function zTreeBeforeExpand(treeId, treeNode) {
	expandFlag = 2;
};

/**
 * 设置日期控制
 */
// 标准销售价格初期化
function initSalePrice() {
	$("#priceInfo")
			.find("table")
			.each(
					function() {
						$(this).find(".date").unbind("cherryDate");
						$(this)
								.find("input[name='priceStartDate'][class='date']")
								.cherryDate(
										{
											holidayObj : $("#dateHolidays")
													.val(),
											beforeShow : function(input) {
												var value = $(input)
														.parents("table")
														.find(
																"input[name='priceEndDate']")
														.val();
												return [ value, "maxDate" ];
											}
										});
						$(this)
								.find("input[name='priceEndDate'][class='date']")
								.cherryDate(
										{
											holidayObj : $("#dateHolidays")
													.val(),
											beforeShow : function(input) {
												var value = $(input)
														.parents("table")
														.find(
																"input[name='priceStartDate']")
														.val();
												return [ value, "minDate" ];
											}
										});
					});
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

/**
 * 显示编辑按钮
 */
function disEditBtn(){
	// 当前方案为空时，不显示显示按钮
	var $editPrtPrcieSoluId = $("#editPrtPrcieSoluId");
	if($("#productPriceSolutionID").val() == ''){
		$editPrtPrcieSoluId.removeClass('show');
		if(!$editPrtPrcieSoluId.hasClass('hide')){
			$editPrtPrcieSoluId.addClass('hide');
		}
	}else {
		$editPrtPrcieSoluId.removeClass('hide');
		if(!$editPrtPrcieSoluId.hasClass('show')){
			$editPrtPrcieSoluId.addClass('show');
		}
	}
}

/**
 * 添加产品方案
 * @return
 */
function addPrtPrcieSolu (){
	var url = $('#addPrtPriceSoluUrlId').val();
	var solutionName = $('#solutionName').val();
	param = $("#prt_price_solu_dialog").find(':input:visible').serialize();
	cherryAjaxRequest({
		url:url,
		param:param,
		callback: function(data){
			if (data.indexOf('fieldErrorDiv')<0){
				var map = JSON.parse(data);
				// 将新添加的方案放到select上
				var htmlTmp = '<option value="'+map.bin_ProductPriceSolutionID +'">'+solutionName+'</option>';
				$('#productPriceSolutionID').append(htmlTmp);
				// 存放最新的所有方案
				$("#prtPriceSolutionListId").val(JSON.stringify(map.prtPriceSolutionList));
			
				// 去除dialog
				$("#prt_price_solu_dialog").dialog("destroy");
				$("#prt_price_solu_dialog").remove();
				// 当前添加的方案设置为当前选中方案
				$('#productPriceSolutionID').val(map.bin_ProductPriceSolutionID);	
				// 显示方案树
				ptjcs14_1.changeSolu($('#productPriceSolutionID'),0);
				// 当前方案为空时，不显示显示按钮
				disEditBtn();
				
			}
		},	
		coverId:"#pageButton"
	});
}

/**
 * 修改产品方案
 * @return
 */
function editPrtPrcieSolu (){
	var url = $('#updPrtPriceSoluUrlId').val();
	var solutionName = $('#solutionName').val();
	var comments = $('#comments').val();
	var businessDateVal = $('#businessDateId').val();
	var soluEndTimeVal = $('#soluEndTime').val();
	param = $("#prt_price_solu_dialog").find(':input:visible').serialize();
	param += "&productPriceSolutionID=" + $("#productPriceSolutionID").val();
	cherryAjaxRequest({
		url:url,
		param:param,
		callback: function(data){
			var map = JSON.parse(data);
//			var htmlTmp = '<option value="'+data +'">'+solutionName+'</option>';
//			$('#productPriceSolutionID').append(htmlTmp);
			// 存放最新的所有方案
			$("#prtPriceSolutionListId").val(JSON.stringify(map.prtPriceSolutionList));
			
//			$("#productPriceSolutionID").find("option:selected").text = solutionName; 
//			alert($("#productPriceSolutionID option:selected").text());
			
			// 显示更新后的方案名称
			var bol = compareDate(soluEndTimeVal,businessDateVal);
			if(bol){
				$("#productPriceSolutionID option:selected").text(solutionName);
			} else {
				$("#productPriceSolutionID option:selected").text(solutionName+'(过期)');
			}
			
			// 显示方案生效区间
			disSoluDate("#productPriceSolutionID");
			
			if (data.indexOf('fieldErrorDiv')<0){
				$("#prt_price_solu_dialog").dialog("destroy");
				$("#prt_price_solu_dialog").remove();
			}
		}
	});
}

/**
 * 比较日期
 */
function compareDate(date1,date2){
	var d1=new Date(date1.replace("-", "/").replace("-", "/"));
	var d2=new Date(date2.replace("-", "/").replace("-", "/"));
	if(d1 >= d2){
		return true;
	}
	return false;
}

/**
 * 保存产品方案明细
 */
function addPrtSoluDetail(){
	
	// 清空消息
	$("#msgDIV").empty();

	// 保存后，将产品节点勾选
	var treeObj = $.fn.zTree.getZTreeObj("tree_0");
	treeObj.checkNode(ptjcs14_1_currnetClick, true, true);
	
//		param += "&priceJson=" + ptjcs14_1.toJSONArr($("#priceInfo").find("table"));
		//param += "&" + $("#shwoMainInfo").find(":input").serialize();
//		var param;
		var tree = ptjcs14_1.trees[0];
		if(!isEmpty(tree)){
//			// 取得选中节点 
			var nodes = ptjcs14_1.getTreeNodes(tree,true);
//			// 取得选中节点【排除全选节点的子节点】
			var checkedNodes = ptjcs14_1.getCheckedNodes(nodes);
			
//			// 取得叶子节点
			var leafNodes = ptjcs14_1.getLeafNodes(nodes);
			$("#prtJson_0").val(JSON.stringify(checkedNodes));
			$("#prtSaveJson_0").val(JSON.stringify(leafNodes));
//			//param += "&placeJson=" + $("#placeJson_0").val();
//			//param += "&saveJson=" + $("#saveJson_0").val();
//			//param += "&priceJson=" + $("#priceJson").val();
//			//alert($("#placeJson_0").val());
//			//alert($("#saveJson_0").val());
//			
		}
//		var priceMode = $("input[name='priceMode']:checked").val();
//		if(priceMode == "1"){
//			$("#priceJson").val(this.toJSONArr($("#priceInfo").find("table")));
//		} else {
//			$("#priceJson").val(this.toJSONArr($("#brandPriceInfo").find("table")));
//		}
		$("#priceJson").val(ptjcs14_1.toJSONArr($("#priceInfo").find("table")));
		var param = $("#mainForm").serialize();
		
		var parentToken = getParentToken();
		param += "&" + parentToken;
		
		cherryAjaxRequest({
			url: $("#addPrtPriceSoluDetailUrlId").val(),
			param: param,
			callback: function(msg){
				if(msg.indexOf('fieldErrorDiv') > -1){
					// 去除勾选的产品节点
					treeObj.checkNode(ptjcs14_1_currnetClick, ptjcs14_1_currnetClickCheckedOld, true);
					// 校验失败
				}else if(msg == '0'){
					// 显示结果信息
					var ht= $("#actionFaildId").clone();
					$("#msgDIV").html('').append(ht);
					
					// 去除勾选的产品节点
					treeObj.checkNode(ptjcs14_1_currnetClick, ptjcs14_1_currnetClickCheckedOld, true);
//					alert("保存失败");
				} else {
					// 去除校验失败的html
					$(".error").removeClass('error');
					$(".icon-error").remove();
					// 显示结果信息
					var ht = $("#actionSuccessId").clone();
					$("#msgDIV").html('').append(ht);
					
					// 显示撤消Button
					$("#delBtnId").css('display','');
					
					// 方案明细Id
					$("#productPriceSolutionDetailID").val(msg);
//					alert("保存成功");
					
					// 添加成功后，刷新初始化当前节点的勾选状态为true
					ptjcs14_1_currnetClickCheckedOld = true;
				}
			},
			coverId : "#pageButton"
		});
}

function doClose(){
	window.close();
}

/**
 * 去除当前方案中指定的产品
 */
function delPrtSoluDetail(){
	
	// 清空消息
	$("#msgDIV").empty();
	
	// 去除后，将产品节点勾选状态去除
	var treeObj = $.fn.zTree.getZTreeObj("tree_0");
	treeObj.checkNode(ptjcs14_1_currnetClick, false, true);
	
	var tree = ptjcs14_1.trees[0];
	if(!isEmpty(tree)){
		// 取得选中节点 
		var nodes = ptjcs14_1.getTreeNodes(tree,true);
		// 取得选中节点【排除全选节点的子节点】
		var checkedNodes = ptjcs14_1.getCheckedNodes(nodes);
		
		// 取得叶子节点
		var leafNodes = ptjcs14_1.getLeafNodes(nodes);
		
		$("#prtJson_0").val(JSON.stringify(checkedNodes));
		$("#prtSaveJson_0").val(JSON.stringify(leafNodes));
	}
	
	var param = $("#mainForm").serialize();
	
	var parentToken = getParentToken();
	param += "&" + parentToken;
	
	cherryAjaxRequest({
		url: $("#delPrtPriceSoluDetailUrlId").val(),
		param: param,
		callback: function(msg){
			if(msg == '0'){
				// 显示结果信息
				var ht= $("#actionFaildId").clone();
				$("#msgDIV").html('').append(ht);
				
				// 去除勾选的产品节点
				treeObj.checkNode(ptjcs14_1_currnetClick, ptjcs14_1_currnetClickCheckedOld, true);
//					alert("保存失败");
			} else {
				// 去除校验失败的html
				$(".error").removeClass('error');
				$(".icon-error").remove();
				
				// 显示结果信息
				var ht = $("#actionSuccessId").clone();
				$("#msgDIV").html('').append(ht);
				
				// 隐藏撤消Button
				$("#delBtnId").css('display','none'); 
				
				// 去除成功后，刷新初始化当前节点的勾选状态为false
				ptjcs14_1_currnetClickCheckedOld = false;
				
			}
		},
		coverId : "#pageButton",	
		loadFlg:true
	});
}

/**
 * 异步加载前
 */
function zTreeBeforeAsync(treeId, treeNode){
	var treeObj = $.fn.zTree.getZTreeObj("tree_0");
	var csrftoken = getParentToken();
	csrftoken = csrftoken.replace('csrftoken=','');
	treeObj.setting.async.otherParam = ["productPriceSolutionID",$("#productPriceSolutionID").val(),"csrftoken",csrftoken];
	return true;
}

/**
 * 查询节点异步加载次数
 */
var curAsyncCount = 0;
/**
 * 当前位置查询结果
 */
var curGetPostionValResultArr;

/**
 * 展开标记 1：定位 2：点击父节点
 */
var expandFlag;
/**
 * 异步加载成功后:去除强制半勾选属性，实现联动勾选 
 */
function zTreeOnAsyncSuccess(event, treeId, treeNode, msg){
//	alert(JSON.stringify(treeId));
//	alert(JSON.stringify(treeNode));
//	alert("isLastCate: "+treeNode.isLastCate);
	
	// 去除halfCheck强制半勾选属性，实现联动勾选 
	var rmHalfChk = function(treeNode){
		if(treeNode.isParent){
//			alert(treeNode.name);
			treeNode.halfCheck = false;
			if(treeNode.level != 0){
				rmHalfChk(treeNode.getParentNode());
			}
		}
	}
	
	// 当是类别最后一个节点时，去除halfCheck
	if(treeNode.isLastCate){
		rmHalfChk(treeNode);
	}
	
	if(expandFlag == 1){
		ptjcs14_1.getPosition(0,null,2);
	}
}

/**
 * // 显示方案生效区间
 */
function disSoluDate(_this){
	
	var soluEndTimeVal = '';
	
	var $soluDateId = $("#soluDateId");
	
	if($(_this).val() == ''){
		// 隐藏
		$soluDateId.removeClass('show');
		if(!$soluDateId.hasClass('hide')){
			$soluDateId.addClass('hide');
		}
	}else {
		// 显示
		$soluDateId.removeClass('hide');
		if(!$soluDateId.hasClass('show')){
			$soluDateId.addClass('show');
		}
		
		var list = JSON.parse($("#prtPriceSolutionListId").val());
		$.each(list, function(i,item){
//			alert(JSON.stringify(item));
			if($(_this).val() == item.solutionID){
				// 生效开始日期
				if(item.startDate){
					$("#soluStartDateId").text(item.startDate);
				}
				// 生效结束日期
				if(item.endDate){
					$("#soluEndDateId").text(item.endDate);
					soluEndTimeVal = item.endDate;
				}
			}
		});
		
		// 过期方案 时间区间显示红色
		var businessDateVal = $('#businessDateId').val();
		var bol = compareDate(soluEndTimeVal,businessDateVal);
		if(bol){
			$("#soluStartDateId").parent().removeClass('red');
			$("#soluStartDateId").parent().addClass('green');
			
			$("#soluEndDateId").parent().removeClass('red');
			$("#soluEndDateId").parent().addClass('green');
		}else{
			$("#soluStartDateId").parent().removeClass('green');
			$("#soluStartDateId").parent().addClass('red');
			
			$("#soluEndDateId").parent().removeClass('green');
			$("#soluEndDateId").parent().addClass('red');
			
		}
	}
}

ptjcs14_1.prototype = {
	// 改变方案
	"changeSolu" : function(_this,index){
		curAsyncCount = 0;
		
		var that = this;
		var $this = $(_this);
		// 清空树内容
		that.trees[index] = null;
		$("#tree_" + index).empty();
		// 清空价格div
		$("#priceInfo").empty();
		// 清空消息
		$("#msgDIV").empty();
		
		// 当前方案为空时，不显示显示按钮
		disEditBtn();
		
		// 隐藏mainPanel
		var $mainPanel = $("#mainPanel");
		$mainPanel.removeClass('show');
		if(!$mainPanel.hasClass('hide')){
			$mainPanel.addClass('hide');
		}
		
		if($this.val() == ''){// 
			// 隐藏定位框
			$("#locationId_0").hide();
		}else{
			// 显示定位框
			$("#locationId_0").show();
			// 显示树形 
			that.searchNodes($this, index);
		}
		
		// 显示方案生效区间
		disSoluDate(_this);
	},
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
			beforeAsync: zTreeBeforeAsync,
			onAsyncSuccess: zTreeOnAsyncSuccess,
			beforeCheck: zTreeBeforeCheck,
			beforeExpand :zTreeBeforeExpand
		},
		async: {
			enable: true,
			url:"/Cherry/pt/BINOLPTJCS14_getPrtTree",
			autoParam:["path","isLastCate"]
			
		}
	},
	/**
	 * opt 区分操作类型 1：画面点击 ，2：onaysnSucc函数调用 
	 */
	"getPosition" : function(index,value,opt){
//		if(isEmpty(value)){
//			value = $('#locationPositiontTxt_' + index).val();
//		}
//		var $tree = this.trees[index];
//		if(!isEmpty($tree)){
//			var inputNodes = $tree.getNodesByParam("name",$.trim(value));
//			$tree.expandNode(inputNodes[0],true,false);
//			$tree.selectNode(inputNodes[0]);
//		}
		
//		var val = $("#locationPositiontTxt_0").val();
//		alert(val);
//		return false;
		
		
		var $tree = this.trees[index];
		if(opt == 1){
			
			expandFlag = 1;
			
			// 查询value对应的各层级节点path
			curAsyncCount = 0;
			
			var unitCodeVal = $("#unitCode").val();
			
			// 查询框产品必须是存在的，因为需要用unitCode去查询。
			if(!unitCodeVal){
				return false;
			}
			
			var param = $("#mainForm").serialize();
			param += "&unitCode="+ unitCodeVal;
			var parentToken = getParentToken();
			param += "&" + parentToken;
			
			cherryAjaxRequest({
				url: $("#getPrtCateValId").val(),
				param: param,
				callback: function(data){
					
//					var curGetPostionValResult = "3\/126\/,3\/126\/127\/,3\/126\/127\/128\/";
//					curGetPostionValResultArr = curGetPostionValResult.split(",");
					curGetPostionValResultArr = data.split(",");
					
					var inputNodes = $tree.getNodesByParam("path",$.trim(curGetPostionValResultArr[curAsyncCount]));
					$tree.expandNode(inputNodes[0],true,false);
					$tree.reAsyncChildNodes(inputNodes[0], "refresh");
					curAsyncCount++;
				}
			});
			
		}else{
			var inputNodes = $tree.getNodesByParam("path",$.trim(curGetPostionValResultArr[curAsyncCount]));
			$tree.expandNode(inputNodes[0],true,false);
			
			if(curGetPostionValResultArr.length-1 == curAsyncCount){
//				$tree.selectNode(inputNodes[0]);
				curAsyncCount = 0;
			}else {
				curAsyncCount++;
			}

			// 选中产品
			var prtNodes = $tree.getNodesByParam("name",$("#locationPositiontTxt_0").val());
			if(!isEmpty(prtNodes) && prtNodes.length != 0){
				$tree.selectNode(prtNodes[0]);
			}
		}
		
//		var myDate = new Date();
//		alert(curAsyncCount +'  ' + myDate.getTime());
		
//		if(curGetPostionVal != value){
//			curGetPostionVal = value;
//			curAsyncCount = 3;
//		}else {
//			if(opt == 1){
//				// 查询value对应的各层级节点path
//				var $tree = this.trees[index];
//				var path1 = "3\/126\/";
//				var path2 = "3\/126\/127\/";
//				var path3 = "3\/126\/127\/128\/";
//				
//				
//				var arr = curGetPostionValResult.split(",");
//				var inputNodes = $tree.getNodesByParam("path",$.trim(arr[curAsyncCount]));
////				var inputNodes1 = $tree.getNodesByParam("path",$.trim(path1));
//				
//				
////				$tree.expandNode(inputNodes1[0],true,false);
////				var inputNodes2 = $tree.getNodesByParam("path",$.trim(path2));
////				$tree.expandNode(inputNodes2[0],true,false);
////				var inputNodes3 = $tree.getNodesByParam("path",$.trim(path3));
////				$tree.expandNode(inputNodes3[0],true,false);
////				$tree.selectNode(inputNodes3[0]);
//			}
//		}
		
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
		//that.bindingInput(index);
		if(typeof(nodes) != "object"){
			treeNodes = eval("(" + nodes + ")");
		}else{
			treeNodes = nodes;
			checkedFlag = 1;
		}
		that.trees[index] = $.fn.zTree.init($("#tree_" + index),that.setting,treeNodes);
		
//		var nodes = that.trees[index].getCheckedNodes(true);
//		if(!isEmpty(nodes)){
//			for(var i=0; i< nodes.length; i++){
//				var node = nodes[i];
//				node.halfCheck = false;
//				that.trees[index].updateNode(nodes[i]);
//			}
//		}
		
		if(checkAll){// 全选
			that.trees[index].checkAllNodes(true);
		}
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
		if($select.val() == '0'){ 
			var nodes = [{id:"all",name:$select.find("option[value='0']").text()}];
			that.loadTree(nodes, index, true);
		}else{
			var url = '/Cherry/pt/BINOLPTJCS14_getPrtTree';
			var param = "productPriceSolutionID=" + $select.val();
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
					obj.path = node.path;
					obj.half = true;
					obj.name = node.name;
					obj.level = node.level;
					checkNodes.push(obj);
				}else{
//					var pNode = node.getParentNode();
//					// 父节点为null或半选
//					if(isEmpty(pNode) || pNode.getCheckStatus().half){
//						var obj = {};
//						obj.path = node.path;
//						obj.half = false;
//						obj.name = node.name;
//						obj.level = node.level;
//						checkNodes.push(obj);
//					}
					var obj = {};
					obj.path = node.path;
					obj.half = false;
					obj.name = node.name;
					obj.level = node.level;
					checkNodes.push(obj);
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
					leafNodes.push(node.path);
				}
			}
		}
		return leafNodes;
	},
	// 设置会员价格
	"setMemPrice":function(_this){
		// 当前激活table
		var $table = $("#priceInfo").find(".activating");
		if(_this != null && _this != undefined){
			$table = $(_this).parents("table");
		}
		// 销售价格
		var $salePrice = $table.find("input[name='salePrice']");
		// 会员价格
		var $memPrice = $table.find("input[name='memPrice']");
		var salePrice = parseFloat($salePrice.val());
		// 会员折扣
		var $memRate = $("#memRate");
		var memRate = parseInt($memRate.val());
		if(isNaN(salePrice)){
			salePrice = 0;
		}
		if(isNaN(memRate)){
			memRate = 0;
		}
		var memPrice = salePrice*memRate/100;
		$memPrice.val(memPrice.toFixed("2"));
	},
	// 添加价格DIV
	"addPriceDivRow" : function() {
		// 添加价格DIV
		this.addDivRow('#priceInfo', '#priceRow');
//		var $salePrices = $("#priceInfo").find("input[name='salePrice']");
//		var size = $salePrices.length;
//		if(size >= 2){
//			var $last = $salePrices.eq(size-1);
//			var $perLast = $salePrices.eq(size-2);
//			$last.val($perLast.val());
//		}
		// 标准销售价格初期化
		initSalePrice();
	},
	// 添加DIV
	"addDivRow" : function(id, rowId) {
		$(id).append($(rowId).html());
	},
	// 删除DIV
	"delDivRow" : function(obj) {
		var $salePrices = $("#priceInfo").find("input[name='salePrice']");
		// 保证价格信息不为空
		if($salePrices.length > 1){
			var $table = $(obj).parents('table');
			// 产品销售价格
			var $prtPriceId = $table.find("input[name='prtPriceId']");
			if($prtPriceId.length > 0){
				$table.empty();
				$table.append('<input type="hidden" name="prtPriceId" value="' + $prtPriceId.val() + '"/>');
				$table.append('<input type="hidden" name="option" value="2" />');
			}else{
				$table.remove();
			}
		}
	},
	
	// 初始化会员折扣弹出框 
	"initRateDiv":function(_this){
		var $this = $(_this);
		// 会员折扣
		var $memRate = $("#memRate");
		var $rateDiv = $memRate.parent();
		// ============激活当前编辑table开始=================== //
		var $table =  $this.parents("table");
		var $priceInfoDiv = $("#priceInfo");
		$priceInfoDiv.find("table").removeClass("activating");
		$table.addClass("activating");
		// ============激活当前编辑table结束=================== //
		// 销售价格
		var $salePrice = $table.find("input[name='salePrice']");
		// 会员价格
		var $memPrice = $table.find("input[name='memPrice']");
		var salePrice = parseFloat($salePrice.val());
		var memPrice = parseFloat($memPrice.val());
		if(isNaN(salePrice)){salePrice = 0;}
		if(isNaN(memPrice)){memPrice = 0;}
		if(salePrice!=0){
			var memRate = parseInt(memPrice*100/salePrice);
			$memRate.val(memRate);
		}
		// 弹出会员折扣框
		$rateDiv.show();
		$memRate.focus();
		$memRate.trigger("select");
		// 设置弹出框位置
		var offset = $this.offset();
//		$("#rateDialog").offset($this.offset());
		//$rateDiv.offset($this.offset());
		//var ra = $rateDiv.offset();
//		$rateDiv.offset(function($this,oldoffset){
//	    	newPos=new Object();
//	        newPos.left=oldoffset.left+10;
//	        newPos.top=oldoffset.top+10;
//	        return newPos;
//		});
		$rateDiv.offset({"top":offset.top-$rateDiv.height()-2,"left":offset.left});
	},
	// 关闭弹出框
	"closeDialog":function(_this){
		var $rateDiv = $(_this).parent();
		$rateDiv.hide();
	},
	// 对象JSON化
	"toJSON" : function(obj) {
		var JSON = [];
		$(obj).find(':input').each(
				function() {
					$this = $(this);
					if (($this.attr("type") == "radio" && this.checked)
							|| $this.attr("type") != "radio") {
						if ($.trim($this.val()) != '') {
							var name = $this.attr("name");
							if (name.indexOf("_") != -1) {
								name = name.split("_")[0];
							}
							JSON.push('"'
									+ encodeURIComponent(name)
									+ '":"'
									+ encodeURIComponent($.trim($this.val())
											.replace(/\\/g, '\\\\').replace(
													/"/g, '\\"')) + '"');
						}
					}
				});
		return "{" + JSON.toString() + "}";
	},
	// 对象JSON数组化
	"toJSONArr" : function($obj) {
		var that = this;
		var JSONArr = [];
		$obj.each(function() {
			JSONArr.push(that.toJSON(this));
		});
		return "[" + JSONArr.toString() + "]";
	}
	
};
var ptjcs14_1 = new ptjcs14_1();
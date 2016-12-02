var ACT = function () {
};
ACT.prototype = {
		"boxNo": 0,
		// 增加模块
		"addItem":	function(src,allFlag){
			var $target = $("ul.sortable:visible");
			var $items = $target.find("li.border");
			if(isEmpty(allFlag) && $items.length == 1){
				$items.find("a.deleteClass").show();
			}
			var $clone = $(src).clone(true);
			// 取得最大活动序号
			var $maxNo = $("#MAX_DETAILNO");
			// 设置新增模块编号
			var $newNo = $clone.find(":input[name='SUBCAMP_DETAILNO']");
			var lastNo = parseInt($maxNo.val()) + 1;
			$newNo.val(lastNo);
			$maxNo.val(lastNo);
			$target.append($clone.html());
		},
		// 删除模块
		"removeItem": function(_this,allFlag){
			var $thisItem = $(_this).parents("li.border");
			var $items = $thisItem.parent().find("li.border");
			$thisItem.remove();
			if(isEmpty(allFlag) && $items.length == 2){
				$items.find("a.deleteClass").hide();
			}
		},
		// 对所有活动档次使用相同的条件
		"setAll":function(_this){
			var that = this;
			var $this = $(_this);
			var $subCamps = $("#campList").find("li");
			// 活动具体内容
			var $campContext = $("#campContext").find("div");
			if($subCamps.filter("li.cMark").length == 0){
				$subCamps.eq(0).addClass("cMark");
			}
			// 当前非选择的子活动标签
			var $target = $subCamps.not("li.cMark");
			if($this.is(":checked")){
				$target.addClass("on");
			}else{
				$target.removeClass("on");
			}
		},
		// 对所有活动档次使用相同的条件
		"setCamp":function(_this,index){
			var $this = $(_this);
			// 当前设置中的活动
			var $selectItem = $this.parent().find("li.on");
			if($this.hasClass("on")){
				return;
			}else{
				// 更新当前设置中的活动
				$selectItem.prop("class","");
				// on:选中样式，cMark:当前活动处在设置状态标记
				$this.addClass("on cMark");
			}
			// 初始化活动具体内容
			var $campContext = $("#campContext").children();
			$campContext.hide();
			$campContext.eq(index).show();
		},
		// 改变奖励方式
		"changeRewardType":function(_this,index){
			var $this = $(_this);
			var id = "#giftList_" + index;
			var $allReward = $(id);
			var $showDiv = $(id+"_" + $this.val());
			$allReward.children().hide();
			$showDiv.show();
			var $barCode = $('#barCode_' + index);
			if($barCode.length > 0){
				if($this.val() == '1'){
					$barCode.hide();
				}else{
					$barCode.show();
				}
			}
		},
		// 取得会员活动条件JOSN数组
		"getCampList":function(contextId){
			var campInfo = '"campList":';
			// 对所有活动使用相同配置CHECKBOX
			var $allChecked = $("#allChecked");
			// 会员活动条件内容集合对象
			var $campForms = $(contextId).find(".FORM_CONTEXT");
			if($allChecked.length == 1 && $allChecked.prop("checked")){
				var JSONArr = [];
				// 当前可视的活动内容
				var $showContext = $campForms.filter(":visible");
				// 当前隐藏的活动内容
				var $hideContexts = $campForms.filter(":hidden");
				// 当前可视的活动内容添加到json数组
				JSONArr.push("{"+Obj2JSON($showContext,false,true).toString() + "}");
				// 相同的内容
				var commJSON = Obj2JSON($showContext.find(".EN_SAME"),false,true);
				$hideContexts.each(function(i){
					// 不相同的内容
					var json = Obj2JSON($(this).find(".UN_SAME"),false,true);
					var jsonStr = json.toString();
					var commJsonStr = commJSON.toString();
					if(commJsonStr != ""){
						jsonStr += "," + commJsonStr;
					}
					JSONArr.push("{" + jsonStr + "}");
				});
				campInfo += "[" + JSONArr.toString() + "]";
			}else{
				campInfo += Obj2JSONArr($campForms,false,true);
			}
			return campInfo;
		},
		/**
		 * 展示礼品记录
		 */
		"getHtmlFun": function(info,prtType,prmCate,free){
			var name = "prmVendorId";
			var price=getNumber(info.standardCost);
			if(prtType == 'N'){
				name = "prtVendorId";
				price=getNumber(info.salePrice);
			}
			var priceText = price;
			
			if(!isEmpty(prmCate)){
				priceText = (price * -1);
				free = false;
			}
			if(!isEmpty(prmCate)){
				priceText = (price * -1);
			}else{
				if(free){
					price = 0;
					priceText = price;
				}
			}
			//格式化金额
			price = price.toFixed(2);
			priceText = priceText.toFixed(2);
			var html = '<tr><td class="hide">';
			if(!isEmpty(prmCate)){
				html = '<tr class="gray"><td class="hide">'; 
				html += '<input type="hidden" name="prmCate" value="' + prmCate + '"/>';
			}
			
			html += '<input type="hidden" name="'+name+'" value="' + info.proId + '"/>';
			html += '<input type="hidden" name="unitCode" value="' + info.unitCode + '"/>';
			html += '<input type="hidden" name="barCode" value="' + info.barCode + '"/></td>';
			html += '<td>' + info.unitCode + '</td>';
			html += '<td style="white-space:normal">' + info.nameTotal + '</td>';
			html += '<td>' + info.barCode + '</td>';
			if(free){
				html += '<td><input class="price" name="price" value="' + price + '"/></td>';
			}else{
				html += '<td>' + priceText +'<input type="hidden" name="price" value="' + price + '"/></td>';	
			}
			
			html += '<td><span><input class="number" name="quantity" value="1" style="text-align:right;"/></span></td>';
			html += '<td>';
			if($('#deliveryType_hide').find(':input').length > 0){
				html += $('#deliveryType_hide').html();
			}
			html += '</td>';
			html += '<td class="center"><a href="javascript:void(0);" onClick="$(this).parent().parent().remove();return false;">删除</a></td>';
			html += '</tr>';
			return html;
		},
		
		
		/*
		 * 活动阶段选择pop窗口
		 */
		"openCampTimeDialog":function(thisObj){
			// 当前操作的活动内容
			var $ul = $("ul.sortable:visible");
			// 当前可视的活动阶段
			var $allLis = $ul.children();
			// 当前操作的活动阶段数组
			var $lis = $ul.children(":visible");
			var $inputs = $('#PopTime_dataTable').find(':input');
			// 初始化 PopTimeDialog
			$inputs.prop("checked",false);
			$inputs.each(function(){
				var $input = $(this);
				$lis.each(function(){
					var $li = $(this);
					if($input.val() == $li.prop("id")) {
						// 当前阶段状态
						var status = $li.find(":input[name='status']").val();
						$input.prop("checked",true);
						if(status == '1'){
							$input.prop("disabled",true);
						}
						return;
					}
				});
			});
			var dialogSetting = {
				bgiframe: true,
				width:300, 
				height:200,
				zIndex: 90,
				modal: true, 
				title: $("#PopTimeTitle").text(),
				close: function(event, ui) {
					$('#PopTimeDialog').dialog("destroy");
				},
				buttons: [{
					text: $("#global_page_ok").text(),
					click: function() {
						$inputs.each(function(){
							var $input = $(this);
							var $li= $allLis.filter("#"+ $input.val());
							if($input.is(":checked")){
								$li.show();
							}else{
								$li.hide();
								$li.find('.date').val("");
							}
						});
						$(this).dialog("close");
					}
				}]
			};
			$('#PopTimeDialog').dialog(dialogSetting);
		},
		//移除活动阶段
		"removeCampTime":function (_this){
			var $thisItem = $(_this).parents("li.border");
			var $items = $thisItem.parent().find("li.border");
			$thisItem.hide();
			$thisItem.find('.date').val("");
		},
		// 活动对象类型
		"changeMebType":function(_this,index){
			var $this = $(_this);
			// 选择导入批次号
			var $linkSearchCode = $("#linkSearchCode_" + index);
			// 选择搜索条件
			var $linkMebSearch = $("#linkMebSearch_" + index);
			// 选择EXCEL文件
			var $linkExcel = $("#linkExcel_" + index);
			// 活动对象分组
			var $groupMeb= $("#groupMeb_" + index);
			// 查询活动对象
			var $searchMeb= $("#searchMeb_" + index);
			// 活动对象一览
			var $mebResult= $("#mebResult_div_" + index);
			//导入弹出按钮
			var $linkMebImport= $("#linkMebImport_" + index);
			// 预估提示
			var $yugu= $("#yugu_" + index);
			$mebResult.hide();
			$("#memberInfo_"+index).hide();
			// 清空对象信息
			$("#campMebInfo_" + index).val("");
			$("#searchCode_" + index).val("");
			$('#conInfoDiv_' + index).hide();
			$('#memCountShow_' + index).hide();
			$groupMeb.hide();
			if($this.val() == '0' || $this.val() == '5'|| $this.val() == '6'){// 全部会员,不限对象,非会员
				$linkSearchCode.hide();
				$linkMebSearch.hide();
				$linkMebImport.hide();
				$linkExcel.hide();
				$searchMeb.hide();
				if($this.val() == '0'){//全部会员，弹出框确认
					var url = "Cherry/cp/BINOLCPCOM02_getMemCount";
					var MemCount;
					$.ajax({
						url :url, 
	                    type:'post',
	                    dataType:'html',
	                    success:function(count){
	                    	MemCount = count;
	                    	$('#memCount_' + index).html(MemCount);
	                    	$('#yugu_' + index).show();
	                    	$('#memCountShow_' + index).show();
	                    }
					});	
				}
			}else if($this.val() == '1' || $this.val() == '2'){// 条件或结果
				$linkMebSearch.show();
				$searchMeb.show();
				$linkSearchCode.hide();
				$linkMebImport.hide();
				$linkExcel.hide();
				if($this.val() == '1'){
					$yugu.show();
				}else{
					$yugu.hide();
					$groupMeb.show();
				}
			}else if($this.val() == '3'){// excel导入
				$searchMeb.show();
				$linkSearchCode.hide();
				$linkMebSearch.hide();
				$linkMebImport.show();
				$yugu.hide();
			}else if($this.val() == '4'){// 外部接口导入
				$searchMeb.show();
				$linkSearchCode.show();
				$linkExcel.hide();
				$linkMebSearch.hide();
				$linkMebImport.hide();
				$yugu.hide();
			}
		},
		//活动对象Excel导入
		"popMemImport":function(){
			//弹出框属性设置
			var option = {
					searchCode : $('.SEARCHCODE').filter(':visible').find(':input[name="searchCode"]').val()
			};
			// 调用会员导入弹出框共通
			popAjaxMemImportDialog(option);
		},
		
		//活动对象Excel导入
		"popCouponImport":function(){
			//弹出框属性设置
			var option = {
					batchCode : $('.BATCHCODE').filter(':visible').find(':input[name="batchCode"]').val()
			};
			// 调用会员导入弹出框共通
			popAjaxCouponDialog(option);
		},
		// 活动对象查询
		"memSearch" : function(index, groupType) {
			var url = '/Cherry/cp/BINOLCPCOM03_memInfosearch';
			var searchCode= $("#searchCode_" + index).val();
			if(!isEmpty(searchCode)){
				var campMebType = $("#campMebType_"+index).val();
				var params= getSerializeToken();
				var brandInfoId=$("#brandInfoId").val();
				params +="&brandInfoId="+brandInfoId+"&searchCode="+searchCode+"&groupType="+groupType;
				url = url + "?" + params;
				// 显示结果一览
				$("#mebResult_div_"+index).show();
				if(campMebType == '2') {
					$("#mebResult_div_"+index).find("li").removeClass("ui-tabs-selected");
					$("#mebResult_div_"+index).find("li[id='"+groupType+"']").addClass("ui-tabs-selected");
					$("#memberInfo_" + index).hide();
					$("#memberInfoTab_"+index).show();
				} else {
					$("#memberInfoTab_"+index).hide();
					$("#memberInfo_" + index).show();
				}
				// 表格设置
				var tableSetting = {
						 index: (campMebType == '2' ? 200000:100000) + index,
						 // 表格ID
						 tableId : '#memberDataTable'+ (campMebType == '2' ? "Tab":"") + "_" + index,
						 // 数据URL
						 url : url,
						 iDisplayLength:5,
						 // 表格默认排序
						 aaSorting : [[ 1, "asc" ]],
						 // 表格列属性设置
						 aoColumns : [  
										{ "sName": "customerType","bSortable": false},
										{ "sName": "memCode"},
										{ "sName": "memName"},
										{ "sName": "mobilePhone"},
										{ "sName": "birthDay","bSortable": false},
										{ "sName": "joinDate","bSortable": false},
										{ "sName": "changablePoint","sClass":"alignRight","bSortable": false},
										{ "sName": "receiveMsgFlg","sClass":"center","bSortable": false}]
				};
				// 调用获取表格函数
				getTable(tableSetting);
			}
	    },
		// Coupon查询
		"couponSearch" : function(index) {
			var url = '/Cherry/cp/BINOLCPCOM03_couponsearch';
			var batchCode= $("#batchCode_" + index).val();
			var campaignCode=$("#campaignCode_" + index).val();
			if(!isEmpty(batchCode)){
				var params= getSerializeToken();
				var brandInfoId=$("#brandInfoId").val();
				params +="&brandInfoId="+brandInfoId+"&batchCode="+batchCode+"&campaignCode="+campaignCode;
				url = url + "?" + params;
				// 显示结果一览
				$("#couponInfo_"+index).show();
				// 表格设置
				var tableSetting = {
						 index: 200000 + index,
						 // 表格ID
						 tableId : '#couponDataTable_' + index,
						 // 数据URL
						 url : url,
						 iDisplayLength:5,
						 // 表格默认排序
						 aaSorting : [[ 2, "asc" ]],
						 // 表格列属性设置
						 aoColumns : [  
										{ "sName": "batchCode","bSortable": false},
										{ "sName": "couponCode"},
										{ "sName": "createTime","bSortable": false}]
				};
				// 调用获取表格函数
				getTable(tableSetting);
			}
	    },
	    /**
	     * 对象批次弹出框
	     * */
	    "popObjBatchSearch":function(index){
	    	//对象批次弹出框属性设置
			var option = {
				checkType : "radio",// 选择框类型
				mode : 2, // 模式
				brandInfoId : $("#brandInfoId").val(),// 品牌ID
				click:function(){ //设置批次号
					//批次信息
					var objBatchInfo=$("#objBatch_dataTableBody").find("input[name='objBatchInfo']:checked").val(); 
					$("#searchCode_"+index).val(objBatchInfo);
					var $resultDiv = $("#mebResult_div_" + index);
					var $labelTip = $resultDiv.find("label");
					var $batchMsg = $labelTip.last();
					$batchMsg.find("strong").text(objBatchInfo);
					$labelTip.hide();
					$batchMsg.show();
					$resultDiv.show();
				},
				callBack:function(){ // 初始化pop
					var searchCode = $("#searchCode_"+index).val();
					$("#objBatch_dataTableBody").find("tr").each(function(){
				    	var $tr = $(this);
				    	var $Td=$tr.find("input[name='objBatchInfo']");
				    	if($Td.val()==searchCode){
				    		$Td.prop("checked",true);
				    	}else{
				    		$Td.prop("checked",false);
				    	}
				    });
				}
			};
			// 调用对象批次弹出框共通
			popAjaxObjBatchDialog(option);
	    },
	    // 活动对象搜索条件弹出框
		"searchConDialog" :function(obj,index){
			var that = this;
			var url = '/Cherry/common/BINOLCM33_init';
			var params = "reqContent=" + $("#campMebInfo_"+index).val();
			var dialogSetting = {
				dialogInit: "#searchCondialogInit",
				width: 900,
				height: 550,
				title: $("#objDialogTitle").text(),
				confirm: $("#dialogConfirm").text(),
				cancel: $("#dialogCancel").text(),
				//确认按钮
				confirmEvent: function(){
					$('#conInfoDiv_' + index).hide();
					//将搜索条件弹出框信息json格式化
					var searchParam = getMemCommonSearchJson("searchCondialogInit");
					//var searchParam = $("#searchCondialogInit").find(":input").serializeForm2Json(false);
					if(searchParam != undefined && null != searchParam && searchParam != ""){
						$("#campMebInfo_"+index).val(searchParam);
						that.getConInfo(searchParam,index);
						// 关闭搜索条件弹出框
						removeDialog("#searchCondialogInit");
					}
					$('#mebResult_div_' + index).hide();
				},
				//关闭按钮
				cancelEvent: function(){removeDialog("#searchCondialogInit");}
			};
			openDialog(dialogSetting);
			cherryAjaxRequest({
				url: url,
				param : params,
				callback: function(msg) {
					$("#searchCondialogInit").html(msg);
				}
			});
		},
		// 取得活动对象数量
		"getMemCount":function(json,index){
			var url = '/Cherry/cp/BINOLCPCOM03_searchMemCount';
			var params = "campMebInfo=" + json;
			cherryAjaxRequest({
				url: url,
				param : params,
				callback: function(msg) {
					$("#memCount_"+index).text(msg);
					$('#memCountShow_' + index).show();
					$('#conInfoDiv_' + index).show();
				}
			});
		},
		// 取得查询条件
		"getConInfo":function(json,index){
			var that = this;
			var url = '/Cherry/common/BINOLCM33_conditionDisplay';
			var $json = $('#campMebInfo_' + index);
			var params = "reqContent=" + json;
			cherryAjaxRequest({
				url: url,
				param : params,
				callback: function(msg) {
					$("#searchCondition_"+index).text(msg);
					that.getSearchCode(json,index);
				}
			});
		},
		// 添加奖励组合框
		"addBox":function(boxType,index){
			var $src = $('#BOX_' + boxType).clone();
			var $tbody = $src.find('tbody');
			this.boxNo ++;
			$tbody.prop('id','boxBody_' + this.boxNo);
			var $target = $('#boxDiv_' + index);
			var $boxs = $target.children('div.GIFTBOX');
			if($boxs.length == 1){
				// 添加逻辑关系
				$target.append($('#LOGIC_BTN').html());
			}else if($boxs.length > 1){
				var html = $target.children('div.LOGICBTN').eq(0).html();
				$target.append('<div class="LOGICBTN">' + html + '</div>');
			}
			$target.append($src.html());
		},
		// 删除组合框
		"delBox":function(_this){
			var $thisBox =$(_this).parent().parent();
			var $boxs = $thisBox.parent().children('div.GIFTBOX');
			if($boxs.length > 1){
				var $prev = $thisBox.prev('div.LOGICBTN');
				// 删除逻辑关系
				if($prev.length != 0){
					$prev.remove();
				}else{
					$thisBox.next().remove();
				}
			}
			$thisBox.remove();
		},
		// 切换逻辑关系
		"changeLogicOpt": function(_this){
			var $this = $(_this);
			var $target = $this.parent().parent();
			var $logicOpt = $target.prev('div.PARAMS').find(':input[name="logicOpt"]');
			var $buttons = $target.children('.LOGICBTN').find('button');
			if($this.prop('class') == 'button_OR'){
				$buttons.prop('class','button_AND');
				$buttons.find('.text').text('AND');
				$logicOpt.val('AND');
			}else{
				$buttons.prop('class','button_OR');
				$buttons.find('.text').text('OR');
				$logicOpt.val('OR');
			}
		},
		//生成searchCode
		"getSearchCode":function(json,index){
			var that = this;
			var url = '/Cherry/cp/BINOLCPCOM03_queryMemSearchCode';
			var params = "campMebInfo=" + json;
			params += "&recordName=" + $.trim($('#campList').find('li.on').first().text());
			var campMebType= $("#campMebType_"+index).val();
			if(campMebType=='1'){//动态搜索条件
				params+="&recordType=1";
			}else if(campMebType=='2'){//当前搜索结果
				params+="&recordType=2&saveFlag=1";
			}
			cherryAjaxRequest({
				url: url,
				param : params,
				callback: function(msg) {
					$("#searchCode_"+index).val(msg);
					// 取得活动对象数量
					that.getMemCount(json,index);
				}
			});
		},
		"setDeliveryType":function(_this){
			var $this = $(_this);
			var val =  $(_this).val();
			var $td = $this.parent();
			var $deliveryType = $td.find(':hidden');
			var value = '';
			var $checkboxs = $td.find(':checkbox').filter(':checked');
			if($checkboxs.length > 0){
				$checkboxs.each(function(i){
					value = value + '_' + $(this).val();
				});
				$deliveryType.val(value);
			}else{
				$deliveryType.val('');
			}
			var $formDiv = $('#campContext').find('div.FORM_CONTEXT').filter(':visible');
			var $deliveryDiv = $formDiv.children('div[id^="DeliveryPrice_"]');
			var $giftList = $formDiv.next();
			// 直达配送
			if(val == '1'){
				if($this.is(":checked")){
					$deliveryDiv.show();
				}else{
					var num = $giftList.find(':checkbox[value="1"]').filter(':checked').length;
					if(num == 0){
						$deliveryDiv.hide();
						$deliveryDiv.find(':input').val('');
					}
				}
			}
		},
	    "extendOption" : {
			"getCommInfo" : function(id){
				var $box = $("#" + id);
				var m = Obj2JSON($box.find("#comParams"));
				// 取得会员活动条件JOSN数组
				var campList = ACT.getCampList("#campContext");
				
				var curIndex = 0;
				var $campList = $('#campList');
				if($campList.length > 0){
					$campList.children().each(function(i){
						if($(this).hasClass('cMark')){
							curIndex = i;
							return;
						}
					});
				}
				$("#extraInfo").val('{' + campList + ',' + '"curIndex":"' + curIndex +'"}');
				m.push(campList);
				return m.toString();
			},
			// 创建活动档次模块传值
			"BASE000053_INFO" : function(id) {
				return this.getCommInfo(id);
			},
			// 活动阶段模块传值
			"BASE000056_INFO" : function(id) {
				var $camps = $("#campContext").children();
				// 活动阶段设置
				$camps.each(function(i){
					var $this = $(this);
					var timeList = Obj2JSONArr($this.find("li"),false,true);
					$("#timeList_" + i).val(timeList);
				});
				return this.getCommInfo(id);
			},
			// 活动范围模块传值
			"BASE000059_INFO" : function(id) {
				var trees = CHERRYTREE.trees;
				// 取得选中节点
				for(var i = 0; i < trees.length; i++){
					if(!isEmpty(trees[i])){
						// 取得选中节点
						var nodes = CHERRYTREE.getTreeNodes(trees[i],true);
						// 取得选中节点【排除全选节点的子节点】
						var checkedNodes = CHERRYTREE.getCheckedNodes(nodes);
						// 取得叶子节点
						var leafNodes = CHERRYTREE.getLeafNodes(nodes);
						$("#placeJson_" + i).val(JSON.stringify(checkedNodes));
						$("#saveJson_" + i).val(JSON.stringify(leafNodes));
					}
				}
				return this.getCommInfo(id);
			},
			// 活动对象模块传值
			"BASE000060_INFO" : function(id) {
				var $camps = $("#campContext").find(".FORM_CONTEXT");
				// 设置活动编号
				$camps.each(function(i){
					// 对象类型
					var $campMebType = $("#campMebType_" + i);
					var $campMebInfo = $("#campMebInfo_" + i);
					var $mebCondition = $("#mebCondition_div_" + i);
					if($campMebType.val() == '0'){// 全部会员
						$campMebInfo.val('ALL_MEB');
					}else if($campMebType.val() == '5'){// 不限对象
						$campMebInfo.val('ALL');
					}else if($campMebType.val() == '6'){// 非会员
						$campMebInfo.val('NO_MEB');
					}
				});
				return this.getCommInfo(id);
			},
			// 活动奖励模块传值
			"BASE000061_INFO" : function(id) {
				var $camps = $("#campContext").children();
				$camps.each(function(i){
					var $camp = $(this);
					var sourceIdA = "#rewardInfo_AA_" + i;
					var sourceIdB = "#rewardInfo_BB_" + i;
					var sourceIdC = "#rewardInfo_CC_" + i;
					var targetIdA = "#rewardInfo_A_" + i;
					var targetIdB = "#rewardInfo_B_" + i;
					var targetIdC = "#rewardInfo_C_" + i;
					var rewardType = $("#rewardType_" + i).val();
					if(!isEmpty(rewardType)){
						sourceIdB += '_' + rewardType;
					}else{
						rewardType = '1';
					}
					$(targetIdC).val($(sourceIdC).val());
					$(targetIdB).val($(sourceIdB).val());
					if(rewardType === "1"){// 指定礼品
						var rewardInfo = "";
						if($('#groupFlag').val() == '1'){// 开启组合奖励模式
							var $boxesDiv = $camp.find('div.sortbox_content');
							var $paramsDiv = $boxesDiv.prev('.PARAMS');
							var $boxes = $boxesDiv.children('div.GIFTBOX');
							var $logicOptArr1 = $paramsDiv.find(':input[name="logicOptArr"]');
							$boxes.each(function(){
								var $box = $(this);
								var $logicOptArr2 = $box.children('.PARAMS').find(':input[name="logicOptArr"]');
								$logicOptArr2.val(Obj2JSONArr2($box.find('tbody').children()));
							});
							$logicOptArr1.val(Obj2JSONArr2($boxes.children('.PARAMS')));
							rewardInfo = '{'+Obj2JSON2($paramsDiv) +'}';
						}else{
							rewardInfo = Obj2JSONArr($("#prtBody_" + i).children(),false,true);
						}
						$(targetIdA).val(rewardInfo);
					}else if(rewardType === "2"){// 抵用券	
						$(targetIdA).val($(sourceIdA).val());
					}
				});				
				return this.getCommInfo(id);
			}
		},
		//展示会员搜索条件
		"expandMore":function(index){
			var $this=$('#expandCondition_'+index);
			if($this.children('.ui-icon').is('.ui-icon-triangle-1-n')) {
				$this.children('.ui-icon').removeClass('ui-icon-triangle-1-n').addClass('ui-icon-triangle-1-s');
				$('#moreCondition_'+index).show();
				$('#showCondition_'+index).hide();
				$('#hideCondition_'+index).show();
			} else {
				$this.children('.ui-icon').removeClass('ui-icon-triangle-1-s').addClass('ui-icon-triangle-1-n');
				$('#moreCondition_'+index).hide();
				$('#hideCondition_'+index).hide();
				$('#showCondition_'+index).show();
			}
		},
		/**
		 * 验证虚拟促销品barCode
		 */
		"vaildBarCode":function(_this,index){
			var url = '/Cherry/common/BINOLCM15_validBarCode';
			var params = $("#brandInfoId").serialize();
			params += '&' + $(_this).serialize();
			params += '&index=' + index;
			cherryAjaxRequest({
				url:url,
				param:params,
				isResultHandle:false,
				callback: function(msg) {
					// 打印错误信息
					ajaxFieldHandle(msg,_this);
					// 开启或禁用下一步和存草稿
					var $errors = $("#campContext").find(".error");
					var $nextBtn = $("#pageButton").find("button.save");
					if($errors.length > 0){
						$nextBtn.prop("disabled",true);
						$nextBtn.addClass("ui-state-disabled");
					}else{
						$nextBtn.removeAttr("disabled");
						$nextBtn.removeClass("ui-state-disabled");
					}
				}
			});
		},
		"extendRules" : {
			"BASE000053":{
				campName:{required : true,maxlength: 20},// 活动名称
				topLimit:{integerValid: true},//活动预约总数量上限
				times:{integerValid: true}//活动对象预约次数上限
			},
			"BASE000061":{
				quantity:{integerValid: true,maxlength: 10}
			}
		},
		"groupMeb": function(index){
			var searchCode= $("#searchCode_" + index).val();
			var campMebInfo = $("#campMebInfo_"+index).val();
			var campMebInfoJson = eval("("+campMebInfo+")");
			if(!searchCode){
				alert("请先选择活动对象");
				return;
			}
			if($("#groupMebdialogInit").length == 0) {
				$("body").append('<div style="display:none" id="groupMebdialogInit"></div>');
			} else {
				$("#groupMebdialogInit").empty();
			}
			var dialogSetting = {
				dialogInit: "#groupMebdialogInit",
				width: 500,
				height: 300,
				title: '活动对象分组',
				confirm: $("#dialogConfirm").text(),
				cancel: $("#dialogCancel").text(),
				//确认按钮
				confirmEvent: function(){
					var campObjGroupValue = $("#groupMebdialogInit").find(":input[name='campObjGroupValue']").val();
					if(campObjGroupValue && /^\d+$/.test(campObjGroupValue)) {

					} else {
						return;
					}
					var groupParams = $("#groupMebdialogInit").find(":input").serialize();
					campMebInfoJson.campObjGroupType=$("#groupMebdialogInit").find(":input[name='campObjGroupType']").val();
					campMebInfoJson.campObjGroupValue=$("#groupMebdialogInit").find(":input[name='campObjGroupValue']").val();
					$("#campMebInfo_"+index).val(JSON.stringify(campMebInfoJson));
					$("#groupMebdialogInit").html('处理中...');
					$("#groupMebdialogInit").dialog( "option", {
						buttons: [{
							text: "关闭",
							click: function(){removeDialog("#groupMebdialogInit");ACT.memSearch(index, "1")}
						}],
						closeEvent: function(){removeDialog("#groupMebdialogInit");}
					});
					var url = '/Cherry/cp/BINOLCPCOM02_campObjGroup';
					var params = getSerializeToken();
					var brandInfoId=$("#brandInfoId").val();
					params +="&brandInfoId="+brandInfoId+"&searchCode="+searchCode+"&"+groupParams;
					cherryAjaxRequest({
						url:url,
						param:params,
						isResultHandle:false,
						callback: function(msg) {
							var result = eval('('+msg+")");
							if(result.errorcode == '0') {
								$("#groupMebdialogInit").html('分组成功');
							} else {
								$("#groupMebdialogInit").html(result.errormsg);
							}
						}
					});
				},
				//关闭按钮
				cancelEvent: function(){removeDialog("#groupMebdialogInit");}
			};
			openDialog(dialogSetting);
			var content = '<table class="detail"><tbody>'
				+'<tr>'
				+'<th>进行沟通计划分组</th>'
				+'<td>'
				+'<span><select name="campObjGroupType" value="'+(campMebInfoJson.campObjGroupType?campMebInfoJson.campObjGroupType:'')+'"><option value="1" '+(campMebInfoJson.campObjGroupType==1?'selected':'')+'>按百分比</option><option value="2" '+(campMebInfoJson.campObjGroupType==2?'selected':'')+'>按人数</option></select></span>'
				+'<span><input name="campObjGroupValue" value="'+(campMebInfoJson.campObjGroupValue?campMebInfoJson.campObjGroupValue:'')+'" style="width:80px;"/></span><span><span id="campObjGroupUnit">%</span>&nbsp;&nbsp;参与沟通计划</span>'
				+'</td>'
				+'</tr>'
				+'<tr>'
				+'<th>不进行沟通机会分组</th>'
				+'<td><span>剩余人数不参与沟通计划</span></td>'
				+'</tr>'
				+'</tbody></table>';
			$("#groupMebdialogInit").html(content);
			$("#groupMebdialogInit").find(":input[name='campObjGroupType']").change(function(){
				$("#groupMebdialogInit").find(":input[name='campObjGroupValue']").val('');
				if(this.value == '1') {
					$("#groupMebdialogInit").find("#campObjGroupUnit").show();
				} else {
					$("#groupMebdialogInit").find("#campObjGroupUnit").hide();
				}
			});
		}
};
var ACT = new ACT();
CAMPAIGN_TEMPLATE.addOption(ACT.extendOption);
CAMPAIGN_TEMPLATE.addTempRules(ACT.extendRules);

$(function(){
	var $campContext = $('#campContext');
	ACT.boxNo = $campContext.find('div.GIFTBOX').length;
});

function BINOLSSPRM68_4(){};
BINOLSSPRM68_4.prototype={
		"upModelFinal":true,
		"index":1000000,
		// 添加组合框
		"addBox":function(type){
			var srcHtml = $('#logicBox_' + type).html();
			var $target = $('#ruleCondBoxInfo').children().last();
			var $boxs = $target.children('div.box2');
			if($boxs.length == 1){
				// 添加逻辑关系
				$target.append($('#logicBtn').html());
			}else if($boxs.length > 1){
				var html = $target.children('div.LOGICBTN').eq(0).html();
				$target.append('<div class="LOGICBTN">' + html + '</div>');
			}
			$target.append(srcHtml);
		},
		// 删除组合框
		"delBox":function(_this){
			var $target = $('#ruleCondBoxInfo').children().last();
			var $thisBox =$(_this).parent().parent();
			var $boxs = $target.children('div.box2');
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
		// 添加单元
		"addItem":function(_this,srcFlag){
			this.index ++;
			var $src = null;
			var $thisParent = $(_this).parent();
			if(srcFlag == 0){// 条件单元
				$src=$('#conditionItem');
			}else{
				$rewardType= $thisParent.parents('div.REWARDBOX').find(':input[name="rewardType"]');
				$src = $('#rewardItem_' + $rewardType.val());
			}
			$src.children('li').children('span.RANGEVAL').prop('id','rangeVal_' + this.index);
			var $target = $thisParent.next().find('ul');
			var srcHtml = $src.html();
			if($('#condType').val() == '1' && $rewardType.val() =='DPZK'){
				var $temp = $('<ul>' + srcHtml + '</ul>');
				$temp.find(':input[name="quantityType"]').parent().remove();
				srcHtml=$temp.html();
			}
			$target.append(srcHtml);
		},
		// 切换产品范围类型
		"changeRangeType":function(_this){
			var $rangeType = $(_this);
			var $rangeOpt = $rangeType.next();
			var $rangeVal = $rangeOpt.next();
			var $propHave = $rangeType.nextAll('.PROPHAVE');
			var $isAll = $rangeType.nextAll('.ISALL');
			var $propHaveCheckbox = $propHave.find(':checkbox');
			var $isAllCheckbox = $isAll.find(':checkbox');
			$rangeOpt.val('');
			$rangeVal.empty();
			$propHave.hide();
			$isAll.hide();
			$propHaveCheckbox.prop('checked',false);
			$isAllCheckbox.prop('checked',true);
			if($rangeType.val() == 'ALL'){
				$rangeOpt.prop('disabled',true);
				$propHave.show();
			}else if($rangeType.val() == 'ZD'){
				$rangeOpt.prop('disabled',true);
				$isAll.show();
			}else if($rangeType.val().indexOf('CODE') >= 0){
				$rangeOpt.prop('disabled',false);
				$rangeOpt.val('EQUAL');
				this.changeRangeOpt(_this);
			}else if($rangeType.val() == 'PRICERANGE'){
				$rangeOpt.prop('disabled',true);
				$rangeOpt.val('EQUAL');
				this.changeRangeOpt(_this);
			}else if($rangeType.val() == 'RANGE'){
				$rangeOpt.prop('disabled',true);
				$rangeOpt.val('EQUAL');
				this.changeRangeOpt(_this);
			}else{
				$rangeOpt.val('EQUAL');
				$rangeOpt.prop('disabled',true);
				this.changeRangeOpt(_this);
			}
		},
		// 切换产品范围操作关系
		"changeRangeOpt":function(_this){
			var $this = $(_this);
			var $li = $this.parent();
			var $rangeOpt =$li.find(':input[name="rangeOpt"]');
			var rangeType = $li.find(':input[name="rangeType"]').val();
			var $quantityType = $li.find(':input[name="quantityType"]');
			var $rangeVal = $rangeOpt.next();
			var $ranges = $rangeVal.next();
			var $add = $ranges.next();
			$rangeVal.empty();
			$ranges.empty();
			if(rangeType =='RANGE'){
				$add.show();
			}else{
				$add.hide();
			}
			if($rangeOpt.val() == 'EQUAL'){
				if(rangeType.indexOf('CODE') > 0){
					$rangeVal.html('<div class="tag"><input name="rangeVal" value="" class="text" style="width:344px;margin:0;"/></div>');
				}else if(rangeType == 'PRICERANGE'){
					$rangeVal.html($('#priceRangeVal').html());
				}else{
					this.openConDialog($rangeVal,rangeType);
				}
			}else if($rangeOpt.val() == 'LIKE'){
				$rangeVal.html($('#likeRangeVal').html());
			}
			if($this.val().indexOf('PRODUCT') < 0){
				$quantityType.parent().show();
			}else{
				$quantityType.parent().hide();
			}
		},
		"setRangeVal":function(_this){
			var $input = $(_this).parent().find(':input');
			$input.eq(2).val($input.eq(0).val()+'+'+$input.eq(1).val());
		},
		"popConDialog":function(_this){
			var $li = $(_this).parent();
			var $rangeType = $li.find(':input[name="rangeType"]');
			var rangeType = $rangeType.val();
			if(rangeType.indexOf('CODE') < 0){
				this.openConDialog(_this,rangeType);
			}
		},
		// 规则条件弹出框
		"openConDialog":function(target,type){
			var that = this;
			var $target = $(target);
			var option = {
         	   targetId: $target.prop('id'),
         	   brandInfoId : $('#brandInfoId').val(),
         	   isBind:0,
 	           checkType : "radio",
 	           mode : 2,
 		       getHtmlFun:function(info){	
 		    	   return that.getConHtml(info,type);
 	           },
 	          click:function(){}
 	        };
			if(type == 'BCLASS'){
				option.teminalFlag = 1;
				popAjaxPrtCateDialog(option);
			}else if(type == 'MCLASS'){
				option.teminalFlag = 3;
				popAjaxPrtCateDialog(option);
			}else if(type == 'LCLASS'){
				option.teminalFlag = 2;
				popAjaxPrtCateDialog(option);
			}else if(type == 'PRODUCT'){
				popAjaxPrtDialog(option);
			}else if(type == 'PRMPRODUCT'){
				option.prmCate = 'CXLP';
				popAjaxPrmDialog(option);
			}else if(type == 'PBCLASS'){
				option.param = '1';
				popAjaxPrmCateDialog(option);
			}else if(type == 'PMCLASS'){
				option.param = '2';
				popAjaxPrmCateDialog(option);
			}else if(type == 'PLCLASS'){
				option.param = '3';
				popAjaxPrmCateDialog(option);
			}
		},
		"getConHtml":function(info,type){
			var text = '';
			var val = '';
			var html = '<div class="tag">';
			if(type == 'BCLASS' || type == 'MCLASS' || type == 'LCLASS'){
				text = info.cateValName + ' (' + info.cateVal + ')';
				val = info.cateVal2;
				html +='<input type="hidden" name="cateValId" value="'+info.cateValId+'"/>';
			}else if(type == 'PRODUCT' || type == 'PRMPRODUCT'){
				text = info.nameTotal + ' (UC:' + info.unitCode + ',BC:' + info.barCode + ')';
				val = info.unitCode + '+' + info.barCode;
				var name = 'prtVendorId';
				if(type == 'PRMPRODUCT'){name = 'prmVendorId';}
				html +='<input type="hidden" name="'+name+'" value="'+info.proId+'"/>';
			}else if(type == 'PBCLASS' || type == 'PMCLASS' || type == 'PLCLASS'){
				text = info.cateName + ' (' + info.cateCode + ')';
				val = info.cateCode;
				html +='<input type="hidden" name="cateCode" value="'+val+'"/>';
			}
			html += '<span style="margin:0 5px;">' + text + '</span><input type="hidden" name="rangeText" value="' 
				+ text + '"/><input type="hidden" name="rangeVal" value="' + val +'"/></div>';
			return html;
		},
		"Obj2JSONArr":function(obj,filter) {
			var JSONArr = [];
			var that = this;
			$(obj).each(function() {
				JSONArr.push("{"+that.Obj2JSON(this,filter).toString() + "}");
			});
			return "[" + JSONArr.toString() + "]";
		},
		"Obj2JSON":function (obj,filter) {
			var JSON = [];
			var $inputs = $(obj).find(':input');
			if(!isEmpty(filter)){
				$inputs = $(obj).find(':input[class != "NOSUBMIT"]');
			}
			$inputs.each(function() {
				$this = $(this);
				var name = $this.attr("name");
				if(!isEmpty(name)){
					var value = $this.val();
					if($this.is(":checkbox")){
						if($this.is(":checked")){
							value = "1";
						}else{
							value = "0";
						}
					}
					JSON.push('"'+ name+ '":"'+ value + '"');
				}
			});
			return JSON;
		},
		// 添加奖励
		"addReward":function(type){
			var $src = $('#rewardVal_hide_' + type);
			var $target = $('#ruleReslutDiv');
			var $boxs = $target.children('div.REWARDBOX');
			if($boxs.length == 1){
				// 添加表格逻辑关系
				$target.append($('#logicBtn').html());
			}else if($boxs.length > 1){
				var html = $target.children('div.LOGICBTN').eq(0).html();
				$target.append('<div class="LOGICBTN">' + html + '</div>');
			}
			$target.append( $src.html());
		},
		// 删除奖励
		"delReward":function(_this){
			var $target = $('#ruleReslutDiv');
			var $thisBox =$(_this).parent().parent().parent();
			var $boxs = $target.children('div.REWARDBOX');
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
		// 添加奖励组合框
		"addRewardBox":function(type,_this){
			var $this = $(_this);
			var $target = $this.parent().parent().next();
			var srcHtml = $('#logicRewardBox_' + type).html();
			var $boxs = $target.children('div.box2');
			if($boxs.length == 1){
				// 添加组合框逻辑关系
				$target.append($('#logicBtn').html());
			}else if($boxs.length > 1){
				var html = $target.children('div.LOGICBTN').eq(0).html();
				$target.append('<div class="LOGICBTN">' + html + '</div>');
			}
			$target.append(srcHtml);
			var rewardType = $('#rewardType').val();
			if (rewardType == 'GIFT' || rewardType == 'DPZK' || rewardType == 'DPTJ'){
				$('#productImportRewardType').show();
			}else{
				$('#productImportRewardType').hide();
			}
		},
		// 删除奖励组合框
		"delRewardBox":function(_this){
			var $thisBox =$(_this).parent().parent();
			var $target = $thisBox.parent();
			var $boxs = $target.children('div.box2');
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
		// 改变条件类型
		"changeCondType":function(_this){
			var $this = $(_this);
			var $box = $('#ruleCondBoxInfo');
			var val = $this.val();
			$box.html($('#condBox_hide_' + val).html());
			if(val == '2'){
				$this.next().show();
			}else{
				$this.next().hide();
			}
			this.setRewardType(true);
		},
		// 改变奖励类型
		"changeRewardType":function(_this){
			var $this = $(_this);
			var rewardType = $this.val();
			var $condType = $('#condType');
			var $rewardHead = $('#rewardHead');
			var $boxBtn = $('#boxBtn');
			var $rewardBody = $this.parent().next();
			var headHtml = '';
			var boxBtnHtml = '';
			var bodyHtml = '';
			var $rewardHeadHide = $('#rewardHead_hide_' + rewardType);
			var $rewardBodyHide = $('#rewardValBody_hide_' + rewardType);
			if($rewardHeadHide.length > 0){
				headHtml = $rewardHeadHide.html();
			}
			if(rewardType !='JFDK' && rewardType !='SGGJ' && rewardType !='ZDYH' && rewardType !='ZDZK' 
				&& rewardType !='ZDMS' && rewardType !='TZZK' && rewardType !='YHTZ' && rewardType !='ZDXL'){
				boxBtnHtml = $('#rewardBtn_hide').html();
			}
			if(rewardType =='DPZK'){
				
			}
			if($rewardBodyHide.length > 0){
				bodyHtml = $rewardBodyHide.html();
			}
			$rewardHead.html(headHtml);
			$boxBtn.html(boxBtnHtml);
			$rewardBody.html(bodyHtml);
		},
		"setRewardType":function(changeBodyFlag){
			var condType = $('#condType').val();
			var changeTypeFlag = false;
			var $rewardType = $('#ruleReslutDiv').find('select[name="rewardType"]');
			var $options = $rewardType.find('option');
			var $opt_GIFT = $options.filter('option[value="GIFT"]');
			var $opt_ZDYH = $options.filter('option[value="ZDYH"]');
			var $opt_ZDZK = $options.filter('option[value="ZDZK"]');
			var $opt_DPZK = $options.filter('option[value="DPZK"]');
			var $opt_DPTJ = $options.filter('option[value="DPTJ"]');
			var $opt_DNZK = $options.filter('option[value="DNZK"]');
			var $opt_DPYH = $options.filter('option[value="DPYH"]');
			var $opt_TZZK = $options.filter('option[value="TZZK"]');
			var $opt_YHTZ = $options.filter('option[value="YHTZ"]');
			var $opt_JJHG = $options.filter('option[value="JJHG"]');
			var $opt_ZDXL = $options.filter('option[value="ZDXL"]');
			$options.show();
			if(condType == '0'){// 无条件
				$options.hide();
				$opt_GIFT.show();
				$opt_JJHG.show();
				if($rewardType.val() != 'GIFT' && $rewardType.val() != 'JJHG'){
					$rewardType.val($options.filter(':visible').first().val());
					changeTypeFlag = true;
				}
//			}else if(condType == '1'){// 整单条件
//				$options.hide();
//				$opt_GIFT.show();
//				$opt_DPTJ.show();
//				$opt_DPZK.show();
//				$opt_ZDZK.show();
//				$opt_ZDYH.show();
//				$opt_JJHG.show();
//				$opt_DNZK.show();
//				if($rewardType.val() != 'GIFT' && $rewardType.val() != 'ZDZK' && $rewardType.val() != 'ZDYH' 
//					&& $rewardType.val() != 'DPTJ' && $rewardType.val() != 'DPZK' 
//						&& $rewardType.val() != 'JJHG'&& $rewardType.val() != 'DNZK'){
//					$rewardType.val($options.filter(':visible').first().val());
//					changeTypeFlag = true;
//				}
			}else{// 非整单条件
				$options.show();
			}
			if(changeBodyFlag && changeTypeFlag){
				this.changeRewardType($rewardType);
			}
		},
		"nextBefore":function(){
			var that = this;
			// 排除指定产品范围
			var $exRangesJson = $('#exRangesJson');
			var $ranges = $exRangesJson.prev();
			var exRangesJson = that.Obj2JSONArr($ranges.find('.tag'));
			$exRangesJson.val(exRangesJson);
			// 规则条件处理
			var $condType = $('#condType');
			var $ruleCondJson = $('#ruleCondJson');
			var $ruleCondBoxInfo = $('#ruleCondBoxInfo').children();
			if($condType.val() == '2'){
				var $ruleConditionBox = $ruleCondBoxInfo.last();
				// 条件组合框
				var $ruleBoxs = $ruleConditionBox.children('.box2');
				$ruleBoxs.each(function(){
					var $this = $(this);
					var $logicObjArr = $this.children().eq(0).find('input[name="logicObjArr"]');
					var $items = $this.find('li');
					$items.each(function(){
						var $li = $(this);
						var $ranges = $li.find('.RANGES');
						var rangesJson = that.Obj2JSONArr($ranges.find('.tag'));
						$li.find(':input[name="ranges"]').val(rangesJson);
					});
					$logicObjArr.val(that.Obj2JSONArr($items,1));
				});
				//
				var $logicObjArr_box = $('#logicObjArr_box');
				$logicObjArr_box.val(that.Obj2JSONArr($ruleBoxs.find('.PARAMS')));
			}
			$ruleCondJson.val("{"+that.Obj2JSON($ruleCondBoxInfo.first()).toString() + "}");
			// 规则结果处理
			var $ruleResultJson = $('#ruleResultJson');
			var $ruleReslutDiv = $('#ruleReslutDiv');
			var $params = $ruleReslutDiv.prev('div.PARAMS');
			var $logicObjArr0 = $params.find('input[name="logicObjArr"]');
			// 奖励模块
			var $resultBoxs = $ruleReslutDiv.children('div.REWARDBOX');
			var rewardType = $('#rewardType').val();
			if($resultBoxs.length > 0){
				$resultBoxs.each(function(){
					var $this = $(this);
					// 组合框
					var $box2s = $this.find('div.box2');
					var $paramsDiv = $this.children('div.PARAMS');
					var $logicObjArr1 = $paramsDiv.find('input[name="logicObjArr"]');
					$box2s.each(function(){
						var $this = $(this);
						var $logicObjArr2 = $this.children('div.PARAMS').find('input[name="logicObjArr"]');
						var $items = $this.find('li');
						$items.each(function(){
							var $li = $(this);
							var $ranges = $li.find('.RANGES');
							var rangesJson = that.Obj2JSONArr($ranges.find('.tag'));
							$li.find(':input[name="ranges"]').val(rangesJson);
							if('DPZK' === rewardType){// 单品折扣
								var $minQuantity = $li.find('input[name="minQuantity"]');
								var $maxQuantity = $li.find('input[name="maxQuantity"]');
								if(!isEmpty($minQuantity.val()) || !isEmpty($maxQuantity.val())){
									var $quantityType = $li.find('select[name="quantityType"]');
									$quantityType.val('RANDOM');
								}
							}
						});
						// 过滤NOSUBMIT
						$logicObjArr2.val(that.Obj2JSONArr($items,1));
					});
					$logicObjArr1.val(that.Obj2JSONArr($box2s.children('div.PARAMS')));
					// 
					var $rewardBody = $paramsDiv.next();
					var $rewardVal2 = $rewardBody.find('input[name="rewardVal"]')
				    var $execFlag2 = $rewardBody.find('input[name="execFlag"]');
					var $rewardQty2 = $rewardBody.find('input[name="rewardQty"]');
					var $rewardMtp2 = $rewardBody.find('input[name="rewardMtp"]');
					var $bcjFlag = $rewardBody.find('#bcjFlag');
					if('SGGJ' === rewardType){// 修改价格
						var val1 = $rewardBody.find('input[name="val1"]').val();
						var val2 = $rewardBody.find('input[name="val2"]').val();
						$paramsDiv.append('<input type="hidden" name="val1" value="'+val1+'"/>')
						.append('<input type="hidden" name="val2" value="'+val2+'"/>');
					}else if('JFDK' === rewardType){// 积分抵扣
						var point = $rewardBody.find('input[name="point"]').val();
						var maxPoint = $rewardBody.find('input[name="maxPoint"]').val();
						$paramsDiv.append('<input type="hidden" name="point" value="'+point+'"/>');
						$paramsDiv.append('<input type="hidden" name="maxPoint" value="'+maxPoint+'"/>');
					}
				    if($rewardVal2.length > 0){
				    	var $rewardVal = $paramsDiv.find('input[name="rewardVal"]');
				    	if($rewardVal.length > 0){
				    		$rewardVal.val($rewardVal2.val());
				    	}else{
				    		$paramsDiv.append('<input type="hidden" name="rewardVal" value="'+$rewardVal2.val()+'"/>');
				    	}
				    }
				    if($bcjFlag.length > 0 && $bcjFlag.prop('checked')){
				    	$paramsDiv.append('<input type="hidden" name="bcj" value="1"/>');
					}
					if($execFlag2.length > 0){
						var $execFlag = $paramsDiv.find('input[name="execFlag"]');
						var val = '0';
						if($execFlag2.prop('checked')){
							val = '1';
						}
						if($execFlag.length > 0){
							$execFlag.val(val);
				    	}else{
				    		$paramsDiv.append('<input type="hidden" name="execFlag" value="'+val+'"/>');
				    	}
					}
					if($rewardQty2.length > 0){
				    	var $rewardQty = $paramsDiv.find('input[name="rewardQty"]');
				    	if($rewardQty.length > 0){
				    		$rewardQty.val($rewardQty2.val());
				    	}else{
				    		$paramsDiv.append('<input type="hidden" name="rewardQty" value="'+$rewardQty2.val()+'"/>');
				    	}
				    }
					if($rewardMtp2.length > 0){
				    	var $rewardMtp = $paramsDiv.find('input[name="rewardMtp"]');
				    	if($rewardMtp.length > 0){
				    		$rewardMtp.val($rewardMtp2.val());
				    	}else{
				    		$paramsDiv.append('<input type="hidden" name="rewardMtp" value="'+$rewardMtp2.val()+'"/>');
				    	}
				    }
				});
				$logicObjArr0.val(that.Obj2JSONArr($resultBoxs.children('div.PARAMS')));
			}
			$ruleResultJson.val("{"+that.Obj2JSON($params).toString() + "}");
			//alert($ruleResultJson.val());
			return true;
		},
		"showRewardQty":function(_this){
			var $this = $(_this);
			var $span = $this.parent().next();
			if($this.is(":checked")){
				$span.show();
			}else{
				$span.hide();
				$span.find(':input').val('');
			}
		},
		// pop产品范围
		"popRangeDialog":function(_this){
			var option = {dialogId:"popRangeDialog",title:"产品范围类型一览"};
			popDialog(option);
			this.bindPopInput(_this);
		},
		"bindPopInput": function(_this){
			var $target = $(_this).prev();
			var $popInputs = $("#popRangeBody").find(":input");
			$popInputs.prop("checked",false);
			$popInputs.unbind("click");
			$popInputs.click(function() {
				var $input = $(this);
				var info = window.JSON2.parse($input.val());
				if ($input.is(":checked")) {
					// 添加产品范围块
					var html = '<div class="tag">'
						+ '<input class="NOSUBMIT" type="hidden" name="key" value="' + info.id + '"/>'
						+ '<input class="NOSUBMIT" type="hidden" name="name" value="' + info.name + '"/>'
						+ '<div class="tag_l">'+ info.name + '</div>'
						+ '<div class="tag_r">';
					if('ALL' == info.id){
						html += '<input class="NOSUBMIT" type="hidden" name="val" value="ALL"/><input class="NOSUBMIT" type="hidden" name="valText" value="' + info.name + '"/>' + info.name;
					}else if('PRICERANGE' == info.id){
						html += '<input class="NOSUBMIT" type="text" class="number" name="val1" value=""/>- <input class="NOSUBMIT" type="text" class="number" name="val2" value=""/>' ;
					}else{
						html += '<a href="#" onclick="PRM68_4.popSetRangeDialog(this,\'' + info.id + '\');return false;">选择</a>';
					}
					html += '</div><span class="icon_del" onclick="$(this).parent().remove();return false;"></span></div>';
					$target.append(html);
				}
			});
		},
		"popSetRangeDialog":function(_this,type){
			var that = this;
			var $target = $(_this).parent();
			var option = {
         	   target: $target,
         	   brandInfoId : $('#brandInfoId').val(),
         	   isBind:0,
 	           checkType : "radio",
 	           mode : 2,
 		       getHtmlFun:function(info){	
 		    	   return that.getRangeHtml(info,type);
 	           },
 	          click:function(){}
 	        };
			if(type == 'BCLASS'){
				option.teminalFlag = 1;
				popAjaxPrtCateDialog(option);
			}else if(type == 'MCLASS'){
				option.teminalFlag = 3;
				popAjaxPrtCateDialog(option);
			}else if(type == 'LCLASS'){
				option.teminalFlag = 2;
				popAjaxPrtCateDialog(option);
			}else if(type == 'PRODUCT'){
				option.checkType = 'checkbox';
				option.className = type;
				option.exchangeHtml = function(opt){
					that.exchangeHtml(opt);
				};
				popAjaxPrtDialog(option);
			}else if(type == 'PRMPRODUCT'){
				option.prmCate = 'CXLP';
				option.className = type;
				option.checkType = 'checkbox';
				option.exchangeHtml = function(opt){
					that.exchangeHtml(opt);
				};
				popAjaxPrmDialog(option);
			}else if(type == 'PBCLASS'){
				option.param = '1';
				popAjaxPrmCateDialog(option);
			}else if(type == 'PMCLASS'){
				option.param = '2';
				popAjaxPrmCateDialog(option);
			}else if(type == 'PLCLASS'){
				option.param = '3';
				popAjaxPrmCateDialog(option);
			}
		},
		"exchangeHtml":function(opt){
			var $target = $(opt.target).parent();
			$target.addClass(opt.className);
			var $targetParent = $target.parent();
			// 弹出框目标缓存区
			var $temp = $("#" + opt.dialogId + "_temp");
			var $divs = $temp.children('div');
			if($divs.length > 1){
				for(var i=0; i < $divs.length - 1; i++){
					$target.clone(true).appendTo($targetParent);	
				}
			}
			var $newDiv = $targetParent.children('.' + opt.className);
			$newDiv.each(function(i){
				$newDiv.eq(i).children('.tag_r').html($divs.eq(i));
			});
			$targetParent.children('.' + opt.className).removeClass(opt.className);
		},
		"getRangeHtml":function(info,type){
			var html = '';
			if(type == 'BCLASS' || type == 'MCLASS' || type == 'LCLASS'){
				html = '<div><input type="hidden" name="val" value="' + info.cateVal2+ '"/>'
				+ '<input class="NOSUBMIT" type="hidden" name="valText" value="' + info.cateValName + '"/>' + info.cateValName + '</div>';
			}else if(type == 'PRODUCT' || type == 'GIFT'){
				var val = info.unitCode + '+' + info.barCode;
				html = '<div><input class="NOSUBMIT" type="hidden" name="val" value="' + val +'"/>' 
				+ '<input class="NOSUBMIT" type="hidden" name="valText" value="' + info.nameTotal + '"/>' + info.nameTotal + '</div>';
			}else if(type == 'PBCLASS' || type == 'PMCLASS' || type == 'PLCLASS'){
				html = '<div><input class="NOSUBMIT" type="hidden" name="val" value="' + info.cateCode+ '"/>'
				+ '<input class="NOSUBMIT" type="hidden" name="valText" value="' + info.cateName + '"/>' + info.cateName + '</div>';
			}
			return html;
		},
		"changeZDXL":function(id1,id2){
			var $this = $(id1);
			var $that =$(id2);
			$this.prop("disabled",false);
			$this.focus();
			$that.val('');
			$that.prop("disabled",true);
		},
	//产品弹出页面弹出框
	"popProductDialog":function(_this,type){
		var dialogId = 'popProductDialog';
		var $dialog = $("#" + dialogId);
		if($dialog.length == 0) {
			$("body").append('<div style="display:none" id="'+dialogId+'"></div>');
		} else {
			$dialog.empty();
		}
		var html;
		var execLoadType;
		var typeAmount=0;//用以加减行数量
		var shopAmount=0;//购物车总共的数量
		var awardAmount=0;//奖励的总共数量
		if(type=="award"){
			execLoadType =$('#rewardType').val();
			if(execLoadType=="GIFT") {
				html="赠送礼品产品导入";
			}else if(execLoadType=="DPZK") {
				html="单品折扣产品导入";
			}else if(execLoadType=="DPTJ") {
				html="单品特价产品导入";
			}
		}else if (type=="shoppingCart"){
			execLoadType="shoppingCart";
			html="非整单条件产品导入";
		}

		// 规则条件处理
		var $ruleCondBoxInfo = $(_this).parent().parent().find('.box2-content_AND');
		if ($ruleCondBoxInfo.length==0){
			$ruleCondBoxInfo = $(_this).parent().parent().find('.box2-content_OR');
		}
		var arr = [];
		var p=[];
		var total=[];
		$ruleCondBoxInfo.find('li').each(function(){
			$(this).find(':input').each(function(){
				var name = $(this).attr("name");
				if (!name || $(this).is(":disabled") ||
					($(this).is(':radio') || $(this).is(':checkbox')) && !$(this).is(':checked')) {
					return true;
				}
				if (name=="rangeVal"){
					var rst = '"'+name+'":"'+$(this).val()+'"';
					arr.push(rst);
				}
			});
			p.push("{" + arr.toString() + "}");
			typeAmount=typeAmount+1;
		});
		total.push('"ruleCondProduct":' +'['+p.toString()+']');
		var ruleCondProduct = "{" + total.toString() + "}";

		// 规则条件处理
		var $condType = $('#condType');
		var $ruleCondBoxInfo = $('#ruleCondBoxInfo').children();
		if($condType.val() == '2'){
			var $ruleConditionBox = $ruleCondBoxInfo.last();
			// 条件组合框
			var $ruleBoxs = $ruleConditionBox.children('.box2');
			$ruleBoxs.find('li').each(function(){
				shopAmount = shopAmount+1;
			});
		}
		// 规则结果处理
		var $ruleReslutDiv = $('#ruleReslutDiv');
		// 奖励模块
		var $resultBoxs = $ruleReslutDiv.children('div.REWARDBOX');
		if($resultBoxs.length > 0){
			$resultBoxs.each(function(){
				var $this = $(this);
				// 组合框
				var $box2s = $this.find('div.box2');
				$box2s.find('li').each(function(){
					awardAmount = awardAmount+1;
				});

			});
		}
		var productPageSize=0;
		if (execLoadType=="shoppingCart"){
			productPageSize = shopAmount-typeAmount;
		}else{
			productPageSize = awardAmount-typeAmount;
		}
		var url = '/Cherry/ss/BINOLSSPRM68_execlLoadInit';
		var param="execLoadType="+execLoadType+"&ruleCondProduct="+ruleCondProduct+"&productPageSizeALL="+productPageSize;
		PRM68_4.upModelFinal=true;
		cherryAjaxRequest({
			url: url,
			param:param,
			callback: function(msg){
				$dialog.html(msg);
				// 弹出验证框
				var dialogSetting = {
					dialogInit: "#" + dialogId,
					text: msg,
					width: 	800,
					height: 400,
					title: html,
					confirm:"确定",
					closeEvent: function(){
						removeDialog("#" + dialogId);
					},
					confirmEvent:function(){
						if (type=="shoppingCart"){ //非整单
							PRM68_4.popShopProductConfirm(_this);
						} else if(execLoadType == "GIFT") { // 赠品
							PRM68_4.popGiftConfirm(_this);
						} else if(execLoadType == "DPZK") { // 单品折扣
							PRM68_4.popDpzkfirm(_this);
						} else if(execLoadType == "DPTJ") { // 单品特价
							PRM68_4.popDptjConfirm(_this);
						}

						removeDialog("#" + dialogId);
					}
				};
				openDialog(dialogSetting);
				$(".ui-dialog-titlebar-close.ui-corner-all").hide();
			}
		});
	},
	// 赠品确认回调函数
	"popGiftConfirm":function(_this){
		var excelProductAward=$("#excelProductAward").val();
		var searchCode=$("#searchCode").val();
		if (searchCode!=""){
			if (excelProductAward!="") {
				var excelProductAwardList = eval("(" + excelProductAward + ")");
				var html = '';
				var upMode=PRM68_4.upModelFinal;
				var $thisParent = $(_this).parent();
				var $target = $thisParent.next().find('ul');
				for (var i = 0; i < excelProductAwardList.length; i++) {
					this.index++;
					var htmlCondition;
					htmlCondition = '<span id="rangeVal_' + this.index + '" onclick="PRM68_4.popConDialog(this);" class="RANGEVAL">';
					htmlCondition += '<div class="tag"><input type="hidden" name="prtVendorId" value="' + excelProductAwardList[i].prtVendorId + '"/>';
					htmlCondition += '<span style="margin:0 5px;">' + excelProductAwardList[i].productName + '(UC:' + excelProductAwardList[i].unitCode + ',BC:' + excelProductAwardList[i].barCode + ')</span>';
					htmlCondition += '<input type="hidden" value="' + excelProductAwardList[i].productName + '(UC:' + excelProductAwardList[i].unitCode + ',BC:' + excelProductAwardList[i].barCode + ')" name="rangeText">';
					htmlCondition += '<input type="hidden" value="' + excelProductAwardList[i].unitCode + '+' + excelProductAwardList[i].barCode + '" name="rangeVal">';
					htmlCondition += '</div></span>';
					htmlCondition += '<span id="ranges_1" class="RANGES" onclick=""></span>';
					htmlCondition += '<input name="ranges" value="" type="hidden">';

					html += '<li class="sortsubbox" style="margin-bottom:10px;">';
					html += '<select name="rangeType" style="margin:0px;width:95px;"  value="' + excelProductAwardList[i].rangeType + '" onchange="PRM68_4.changeRangeType(this);">' +
						'<option  value="ALL" >任意产品</option>';
					html += '<option  value="PRODUCT"  selected="selected">特定产品</option><option  value="RANGE">产品范围</option>';
					html += '<option  value="PRICERANGE">单价范围</option>';
					html += '<option  value="BCLASS">产品大类</option>';
					html += '<option  value="MCLASS">产品中类</option>';
					html += '<option  value="LCLASS">产品小类</option>';
					html += '<option  value="UNITCODE">厂商编码</option>';
					html += '<option  value="BARCODE">产品条码</option>';
					html += '<option  value="ZD">整单产品</option></select>';
					html += '<select name="rangeOpt" style="margin:0px;width:60px;" disabled="true" onchange="PRM68_4.changeRangeOpt(this);"> ' +
						'<option value="EQUAL">等于</option>' +
						'</select>';
					html += htmlCondition;
					html += '<input value="' + excelProductAwardList[i].productNum + '" name="quantity" style="margin-bottom:0px;" class="number">件';
					html += '<a href="#" class="right" onclick="$(this).parent().remove();return false;" role="button"><span class="ui-icon icon-delete-big">close</span></a>';
				}
				if (!upMode){
					$target.html("");
				}
				$target.append(html);
			}
		}
	},
	"popDpzkfirm":function(_this){
		var excelProductAward=$("#excelProductAward").val();
		var searchCode=$("#searchCode").val();
		if (searchCode!=""){
			if (excelProductAward!="") {
				var excelProductAwardList = eval("(" + excelProductAward + ")");
				var html = '';
				var $thisParent = $(_this).parent();
				var $target = $thisParent.next().find('ul');
				var upMode=PRM68_4.upModelFinal;
				for (var i = 0; i < excelProductAwardList.length; i++) {
					this.index++;
					var htmlCondition;

					htmlCondition = '<span id="rangeVal_' + this.index + '" onclick="PRM68_4.popConDialog(this);" class="RANGEVAL">';
					htmlCondition += '<div class="tag"><input type="hidden" name="prtVendorId" value="' + excelProductAwardList[i].prtVendorId + '"/>';
					htmlCondition += '<span style="margin:0 5px;">' + excelProductAwardList[i].productName + '(UC:' + excelProductAwardList[i].unitCode + ',BC:' + excelProductAwardList[i].barCode + ')</span>';
					htmlCondition += '<input type="hidden" value="' + excelProductAwardList[i].productName + '(UC:' + excelProductAwardList[i].unitCode + ',BC:' + excelProductAwardList[i].barCode + ')" name="rangeText">';
					htmlCondition += '<input type="hidden" value="' + excelProductAwardList[i].unitCode + '+' + excelProductAwardList[i].barCode + '" name="rangeVal">';
					htmlCondition += '</div></span>';
					htmlCondition += '<span id="ranges_1" class="RANGES" onclick=""></span>';
					htmlCondition += '<input name="ranges" value="" type="hidden">';

					html += '<li class="sortsubbox" style="margin-bottom:10px;">';
					html += '<select name="rangeType" style="margin:0px;width:95px;"  value="' + excelProductAwardList[i].rangeType + '" onchange="PRM68_4.changeRangeType(this);">' +
						'<option  value="ALL" >任意产品</option>';
					html += '<option  value="PRODUCT"  selected="selected">特定产品</option><option  value="RANGE">产品范围</option>';
					html += '<option  value="PRICERANGE">单价范围</option>';
					html += '<option  value="BCLASS">产品大类</option>';
					html += '<option  value="MCLASS">产品中类</option>';
					html += '<option  value="LCLASS">产品小类</option>';
					html += '<option  value="UNITCODE">厂商编码</option>';
					html += '<option  value="BARCODE">产品条码</option>';
					html += '<option  value="ZD">整单产品</option></select>';
					html += '<select name="rangeOpt" style="margin:0px;width:60px;" disabled="true" onchange="PRM68_4.changeRangeOpt(this);"> ' +
						'<option value="EQUAL">等于</option>' +
						'</select>';
					html += htmlCondition;
					html += '<span style="margin-left:5px;">折扣件数范围（';
					html += '<input class="number" value="' + excelProductAwardList[i].discountNumGtEq + '" name="minQuantity" style="margin:0px;">-';
					html += '<input class="number" value="' + excelProductAwardList[i].discountNumLtEq + '" name="maxQuantity" style="margin:0px;">）</span>';
					html += '<span style="display: none;">(<select name="quantityType" style="margin:0px;width:80px;">';
					html += '<option value="ALL">全部</option><option value="RANDOM">随机一件</option><option value="MIN">价格最低</option>';
					html += '<option value="MAX">价格最高</option></select>)</span>';
					html += '<input  value="' + excelProductAwardList[i].discountNum + '" name="rewardVal" style="margin-bottom:0px;" class="number">折';
					html += '<a href="#" class="right" onclick="$(this).parent().remove();return false;" role="button"><span class="ui-icon icon-delete-big">close</span></a>';
				}
			}
			if (!upMode){
				$target.html("");
			}
			$target.append(html);
			//$target.html("").append(html);
		}

	},
	// 单品特价确认回调函数
	"popDptjConfirm":function(_this){
		var excelProductAward=$("#excelProductAward").val();
		var searchCode=$("#searchCode").val();
		if (searchCode!=""){
			if (excelProductAward!="") {
				var excelProductAwardList = eval("(" + excelProductAward + ")");
				var html = '';
				var $thisParent = $(_this).parent();
				var $target = $thisParent.next().find('ul');
				var upMode=PRM68_4.upModelFinal;
				for (var i = 0; i < excelProductAwardList.length; i++) {
					this.index++;
					var htmlCondition;

					htmlCondition = '<span id="rangeVal_' + this.index + '" onclick="PRM68_4.popConDialog(this);" class="RANGEVAL">';
					htmlCondition += '<div class="tag"><input type="hidden" name="prtVendorId" value="' + excelProductAwardList[i].prtVendorId + '"/>';
					htmlCondition += '<span style="margin:0 5px;">' + excelProductAwardList[i].productName + '(UC:' + excelProductAwardList[i].unitCode + ',BC:' + excelProductAwardList[i].barCode + ')</span>';
					htmlCondition += '<input type="hidden" value="' + excelProductAwardList[i].productName + '(UC:' + excelProductAwardList[i].unitCode + ',BC:' + excelProductAwardList[i].barCode + ')" name="rangeText">';
					htmlCondition += '<input type="hidden" value="' + excelProductAwardList[i].unitCode + '+' + excelProductAwardList[i].barCode + '" name="rangeVal">';
					htmlCondition += '</div></span>';
					htmlCondition += '<span id="ranges_1" class="RANGES" onclick=""></span>';
					htmlCondition += '<input name="ranges" value="" type="hidden">';

					html += '<li class="sortsubbox" style="margin-bottom:10px;">';
					html += '<select name="rangeType" style="margin:0px;width:95px;"  value="' + excelProductAwardList[i].rangeType + '" onchange="PRM68_4.changeRangeType(this);">' +
						'<option  value="ALL" >任意产品</option>';
					html += '<option  value="PRODUCT"  selected="selected">特定产品</option><option  value="RANGE">产品范围</option>';
					html += '<option  value="PRICERANGE">单价范围</option>';
					html += '<option  value="BCLASS">产品大类</option>';
					html += '<option  value="MCLASS">产品中类</option>';
					html += '<option  value="LCLASS">产品小类</option>';
					html += '<option  value="UNITCODE">厂商编码</option>';
					html += '<option  value="BARCODE">产品条码</option>';
					html += '<option  value="ZD">整单产品</option></select>';
					html += '<select name="rangeOpt" style="margin:0px;width:60px;" disabled="true" onchange="PRM68_4.changeRangeOpt(this);"> ' +
						'<option value="EQUAL">等于</option>' +
						'</select>';
					html += htmlCondition;
					html += '件<input  value="' + excelProductAwardList[i].specialPrice + '" name="rewardVal" style="margin-bottom:0px;" class="number">元';
					html += '<a href="#" class="right" onclick="$(this).parent().remove();return false;" role="button"><span class="ui-icon icon-delete-big">close</span></a>';
				}
			}
			if (!upMode){
				$target.html("");
			}
			$target.append(html);
			//$target.html("").append(html);
		}
	},
	/*导入时只导入产品范围以及特定产品*/
	"popShopProductConfirm":function(_this){
		var excelProductShop=$("#excelProductShopping").val();
		var searchCode=$("#searchCode").val();
		if (searchCode!=""){
			if (excelProductShop!=""){
				var excelProductShopList=eval("("+excelProductShop+")");
				var $thisParent = $(_this).parent();
				var $target = $thisParent.next().find('ul');
				var upMode=PRM68_4.upModelFinal;
				var html='';
				for(var i=0;i<excelProductShopList.length;i++){
					this.index ++;
					var htmlCondition;
					htmlCondition ='<span id="rangeVal_'+this.index+'" onclick="PRM68_4.popConDialog(this);" class="RANGEVAL">';
					htmlCondition+='<div class="tag"><input type="hidden" name="prtVendorId" value="'+excelProductShopList[i].prtVendorId+'"/>';
					htmlCondition+='<span style="margin:0 5px;">'+excelProductShopList[i].productName+'(UC:'+excelProductShopList[i].unitCode +',BC:'+ excelProductShopList[i].barCode+')</span>';
					htmlCondition+='<input type="hidden" value="'+excelProductShopList[i].productName+'(UC:'+excelProductShopList[i].unitCode +',BC:'+ excelProductShopList[i].barCode+')" name="rangeText">';
					htmlCondition+='<input type="hidden" value="'+ excelProductShopList[i].unitCode + '+' + excelProductShopList[i].barCode +'" name="rangeVal">';
					htmlCondition+='</div></span>';
					htmlCondition+='<span id="ranges_1" class="RANGES" onclick=""></span>';
					htmlCondition+='<input name="ranges" value="" type="hidden">';

					html+= '<li class="sortsubbox" style="margin-bottom:10px;">';
					html+= '<select name="rangeType" style="margin:0px;width:95px;"  value="'+excelProductShopList[i].rangeType+'" onchange="PRM68_4.changeRangeType(this);">'+
						'<option  value="ALL" >任意产品</option>' ;
					html+='<option  value="PRODUCT"  selected="selected">特定产品</option><option  value="RANGE">产品范围</option>';
					html+='<option  value="PRICERANGE">单价范围</option>';
					html+='<option  value="BCLASS">产品大类</option>';
					html+='<option  value="MCLASS">产品中类</option>';
					html+='<option  value="LCLASS">产品小类</option>';
					html+='<option  value="UNITCODE">厂商编码</option>';
					html+='<option  value="BARCODE">产品条码</option>';
					html+='<option  value="ZD">整单产品</option></select>';
					html+= '<select name="rangeOpt" style="margin:0px;width:60px;" disabled="true" onchange="PRM68_4.changeRangeOpt(this);"> ' +
						'<option value="EQUAL">等于</option>' +
						'</select>';
					html+=htmlCondition;
					html+= '<select name="propName" style="margin:0px;width:60px;">';
					if ("QUANTITY"==excelProductShopList[i].propName){
						html+='<option value="QUANTITY" selected="selected">数量</option><option value="AMOUNT">金额</option></select>';
					}else if ("AMOUNT"==excelProductShopList[i].propName){
						html+='<option value="QUANTITY" >数量</option><option value="AMOUNT" selected="selected">金额</option></select>';
					}
					html+= '<select name="propOpt" style="margin:0px;width:80px;" >';
					if ("EQ"==excelProductShopList[i].propOpt){
						html+='<option  value="EQ"  selected="selected">等于</option><option value="NE">不等于</option><option value="GT">大于</option>';
						html+='<option value="GE">大于等于</option><option value="LT">小于</option><option value="LE">小于等于</option>';
					}else if ("NE"==excelProductShopList[i].propOpt){
						html+='<option  value="EQ"  >等于</option><option value="NE" selected="selected">不等于</option><option value="GT">大于</option>';
						html+='<option value="GE">大于等于</option><option value="LT">小于</option><option value="LE">小于等于</option>';
					}else if ("GT"==excelProductShopList[i].propOpt){
						html+='<option  value="EQ" >等于</option><option value="NE">不等于</option><option value="GT"  selected="selected">大于</option>';
						html+='<option value="GE">大于等于</option><option value="LT">小于</option><option value="LE">小于等于</option>';
					}else if ("GE"==excelProductShopList[i].propOpt){
						html+='<option  value="EQ"  >等于</option><option value="NE">不等于</option><option value="GT">大于</option>';
						html+='<option value="GE" selected="selected">大于等于</option><option value="LT">小于</option><option value="LE">小于等于</option>';
					}else if ("LT"==excelProductShopList[i].propOpt){
						html+='<option  value="EQ"  >等于</option><option value="NE">不等于</option><option value="GT">大于</option>';
						html+='<option value="GE">大于等于</option><option value="LT" selected="selected">小于</option><option value="LE">小于等于</option>';
					}else if ("LE"==excelProductShopList[i].propOpt){
						html+='<option  value="EQ" >等于</option><option value="NE">不等于</option><option value="GT">大于</option>';
						html+='<option value="GE">大于等于</option><option value="LT">小于</option><option value="LE"  selected="selected">小于等于</option>';
					}

					html+='<input type="text" value="'+excelProductShopList[i].propValue+'" name="propValue" style="margin:0px;width:70px;" class="text">';
					html+='<a href="#" class="right" onclick="$(this).parent().remove();return false;" role="button"><span class="ui-icon icon-delete-big">close</span></a>';
				}
			}
			if (!upMode){
				$target.html("");
			}
			$target.append(html);
			//$target.html("").append(html);
		}
	}
}
var PRM68_4 = new BINOLSSPRM68_4();
$(document).ready(function() {
//	PRM68_4.index = $('li.sortsubbox').length;
	PRM68_4.setRewardType();
});

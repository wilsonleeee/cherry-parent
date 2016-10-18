var PRM13 = function() {
};
PRM13.prototype = {
		"initTime":function(){
			$('#activeTimeInfo .time').timepicker({
				timeFormat: 'HH:mm:ss',
				showSecond: true,
				timeOnlyTitle: '设置时分秒',
				currentText: '当前时间',
				closeText: '关闭',
				hourMax: 23
			});
		},
		// 显示/隐藏奖励结果
		"showBox":function(_this,flag){
			var $this = $(_this);
			var id = $this.prop("id");
			var $box = $("#" + id + "0");
			if($this.prop("checked")){
				$box.show();
			}else{
				$box.hide();
			}
			var $obj = $("#addAmountFlag").parent().parent();
			// 加价购，套装折扣排斥
			if(flag){
				if($("#rewardsType_10").is(":visible") && $("#rewardsType_20").is(":visible")){
					$obj.hide();
				}else{
					$obj.show();
				}
			}
			$obj.find(":text").val("");
		},
		// 设置活动类型
		"setActivityType": function(mainType){
			if(isEmpty(mainType)){
				mainType = $("#groupInfo").find(":input[name='prmGrpType']").val();
				if(isEmpty(mainType)){
					mainType = 'CXHD';
				}
			}
			// 当前子活动类型
			var $activityType =$("#activityType_"+mainType);
			var $others = $activityType.siblings();
			$activityType.show();
			$activityType.prop("disabled",false);
			$others.hide();
			$others.prop("disabled",true);
		},
		// 套装折扣类型切换
		"changeBox":function(num,type){
			if(isEmpty(type)){
				type = 'tzzk';
			}
			var str = type + "_type_";
			var $showBox = $("#" + str + num);
			var $Boxs = $("div[id^='"+str+"']");
			$Boxs.hide();
			$showBox.show();
		},
		"deleteHtml":function (obj){
			var targetId = $(obj).parents("tbody").first().prop("id");
			$(obj).parents("tr").first().remove();
			this.getSumInfo(targetId);
		},
		// flag：不重置价格标记
		"getHtmlFun": function(info,type,groupType,flag,prmCate){
			var name = "prtVendorId";
			var price = null;
			var priceText = '0.00';
			var quantity = 1;
			var className = "";
			if(type == 'prm'){
				name = "prmVendorId";
				price = info.standardCost;
			}else{
				price = info.salePrice;
			}
			if(groupType == '1'){// 赠送
				price = '0.00';
			}else{
				price = getNumber(price);
				priceText = price;
				if('TZZK' == prmCate || 'DHCP' == prmCate){
					priceText = -1 * priceText;
					className = "gray";
				}
				price = getPrice(price);
				priceText = getPrice(priceText);
			}			
			if(!isEmpty(info.quantity)){
				quantity = info.quantity;
			}
			
			// 产品类型
			var saleType = "";
			if('pro' == type){
				saleType = 'N';
			}else if('prm' == type){
				saleType = 'P';
			}
			
			var html = '<tr class="'+className+'">'; 
			html += '<td class="hide"><input type="hidden" name="'+name+'" value="' + info.proId + '"/>';
			html += '<input type="hidden" name="saleType" value="'+saleType+'" />';
			html += '<input type="hidden" name="groupType" value="'+groupType+'" />';
			html += '<input type="hidden" name="unitCode" value="'+info.unitCode+'" />';
			html += '<input type="hidden" name="barCode" value="'+info.barCode+'" />';
			html += '<input type="hidden" name="oldPrice" value="'+price+'" /></td>';		
			html += '<td>' + info.unitCode + '</td>';
			html += '<td>' + info.barCode + '</td>';
			html += '<td>' + info.nameTotal + '</td>';
			if(!flag){
				html += '<td>'+priceText+'<input type="hidden" name="price" value="'+price+'"></td>';		
			}else{
				html += '<td><span><input onchange="PRM13.getSumInfo(this);" class="price" name="price" value="'+price+'"></span></td>';
			}
			html += '<td><span><input onchange="PRM13.getSumInfo(this);" class="number" name="quantity" value="'+quantity+'"></span></td>';
			html += '<td class="center"><a href="javascript:void(0);" onClick="PRM13.deleteHtml(this);return false;">'+BINOLSSPRM13_js_i18n.del+'</a></td>';
			html += '</tr>';
			return html;
		},
		// 取得活动规则产品INFO对象数组
		"getInfoHtml": function(_this){
			var that = this;
			var html = "";
			var $ul = $(_this).parent().parent();
			var $lis = $ul.find("li");
			$lis.each(function(){
				var $li = $(this);
				var info = {};
				if($li.find(":input[name='prtVendorId']").length > 0){
					info.nameTotal = $li.find(":input[name='nameTotal']").val();
					info.proId = $li.find(":input[name='prtVendorId']").val();
					info.quantity = $li.find(":input[name='quantity']").val();
					html += that.getHtmlFun(info, 'pro');
				}
			});
			return html;
		},
		/**
		 * 产品弹出框
		 * @param _this
		 * @param targetId
		 * @param flag：是否可修改单价
		 */
		"openProDialog" :function(_this,groupType,flag){
			var that = this;
			var targetId = "bomBody" + groupType;
			var option = {
             	   	targetId: targetId,
     	           	checkType : "checkbox",
     	           	mode : 2,
     	           	brandInfoId : $("#brandInfoId").val(),
     		       	getHtmlFun:function(info){
						return that.getHtmlFun(info, 'pro', groupType, flag);
				   	},
				   	click : function(){
				   		that.getSumInfo(targetId);
				   	}
     	        };
			// 手动初始化弹出框
			if(isEmpty(groupType)){
				option.targetId = '';
				option.click = function(){
					selectProduct(_this);
				};
				option.infoHtml = that.getInfoHtml(_this);
			}
			if(groupType=='3'){
				option.isExchanged = 1;
			}
     		popAjaxPrtDialog(option);
		},
		// 促销品弹出框
		"openPrmDialog" : function (groupType,prmCate){
			var that = this;
			var checkType = "checkbox";
			if(isEmpty(prmCate)){
				prmCate = 'CXLP';
			}else{
				checkType = 'radio';
			}
			var targetId = "bomBody" + groupType;
			var option = {
             	   	targetId: targetId,
     	           	checkType : checkType,
     	           	mode : 2,
     	           	brandInfoId : $("#brandInfoId").val(),
     	            prmCate : prmCate,
     		       	getHtmlFun:function(info){
						return that.getHtmlFun(info, 'prm',groupType,false,prmCate);
				   	},
				   	click : function(){
				   		that.getSumInfo(targetId);
				   	}
     	        };
			if(groupType=='3'){
				option.isExchanged = 1;
			}
			popAjaxPrmDialog(option);
		},
		// 活动奖励INFO
		"getResultInfo" : function(){
			var result = "";
			var $infoBoxs = $("#rewards_body").find("div[id^='rewardsType_']").filter(":visible");
			$infoBoxs.each(function(i){
				// Table
				var $table = $(this).find("div.box2-content").children(":visible").find("table");
				// sumInfo
				var $tableNext = $table.next();
				var json = [];
				var temp = "";
				// 套装折扣情况，全部折扣，产品折扣
				if($tableNext.length == 0){
					temp = Obj2JSON($table).toString();
				}else{
					json = Obj2JSON($tableNext);
					if(json.length > 0){
						temp = json + ',';
					}
					temp += '"list":' + Obj2JSONArr($table.find("tr").not(":first"));
				}
				if(i > 0){
					result += ",";
				}
				result += "{" + temp + "}";
			});
			result = "[" + result + "]";
			$("#resultInfo").val(result);
		},
		// 总数量计算
		"getSumInfo" : function(obj){
			var targetId = "";
			var count = 0;
			var money = 0;
			var oldMoney = 0;
			if(typeof(obj) == 'string'){
				targetId = obj;
			}else{
				targetId = $(obj).parents("tbody").first().prop("id");
			}
			var $target = $("#" + targetId);
			var $count = $("#" + targetId + "_count");
			var $money = $("#" + targetId + "_money");
			var $discountHide = $money.next();
			var $moneyText = $("#" + targetId + "_moneyText");
			var $oldMoney = $("#" + targetId + "_oldMoney");
			var $discount = $("#" + targetId + "_discount");
			$target.find("tr").each(function(){
				var $this = $(this);
				var quantity = getNumber($this.find(":input[name='quantity']").val());
				var oldPrice = getNumber($this.find(":input[name='oldPrice']").val());
				var price = getNumber($this.find(":input[name='price']").val());
				// 总数量计算
				count += quantity;
				if($this.prop("class") == 'gray'){
					oldMoney -= oldPrice * quantity;
					money -= price * quantity;
				}else{
					oldMoney += oldPrice * quantity;
					money += price * quantity;
				}
			});
			$count.text(count);
			$oldMoney.text(getPrice(oldMoney));
			$money.val(getPrice(money));
			$moneyText.text(getPrice(money));
			// 积分兑换
			if(targetId == 'bomBody3'){
				$discount.text(getPrice(oldMoney));
			}else{
				$discount.text(getPrice(oldMoney-money));
				$discountHide.val(getPrice(oldMoney-money));
			}
		},
		// 计算折扣价格
		"setPrice" :function(_this){
			var $this = $(_this);
			var id = $this.prop("id");
			var prexId = id.substring(0,id.indexOf("_"));
			var oldMoneyText = $.trim($("#" + prexId + "_oldMoney").text()).replace(',','');
			var oldMoney = getNumber(oldMoneyText);
			var discount = oldMoney - getNumber($this.val());
			$this.next().val(getPrice(discount));
			$("#" + prexId + "_discount").text(getPrice(discount));
		},
		 // AJAX文件上传
	    "ajaxFileUpload" : function (index,flag){
	    	var that = this;
			var url = $("#importCounterUrl").text();
	    	// AJAX登陆图片
	    	var $ajaxLoading = $("#loading");
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
	    	$.ajaxFileUpload({
		        url: url,
		        secureuri:false,
		        data:{'csrftoken':parentTokenVal(),'brandInfoId':$('#brandInfoId').val()},
		        fileElementId: 'cntFile_'+index,
		        dataType: 'html',
		        success: function (msg){
		        	$message.html(msg);
		        	if($message.find("#actionResultDiv").length > 0){
		        		$message.show();
		        	}else if (window.JSON && window.JSON.parse) {
						var msgJson = window.JSON.parse(msg);
						var counterNodes = msgJson['trueCounterList'];
						if(msgJson['errorMsg'] != undefined){
							$errorDiv.find("#errorSpan").text(msgJson['errorMsg']);
							$errorDiv.show();
						}else{
							$errorDiv.hide();
						}
						if(isEmpty(binOLSSPRM13_global.rightTreeTmp) || binOLSSPRM13_global.rightTreeTmp.getNodes().length == 0){
							// 柜台节点加载到树
							binOLSSPRM13_global.rightTreeTmp = $.fn.zTree.init($('#treeRight_'+index),binOLSSPRM13_global.zTreeSetting,counterNodes);
						}else{
							that.addNodes(binOLSSPRM13_global.rightTreeTmp, counterNodes, false);
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
	    }
};
var PRM13 = new PRM13();
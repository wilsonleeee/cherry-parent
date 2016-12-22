var binOLWSMNG07_02_global = {};
//是否需要解锁
binOLWSMNG07_02_global.needUnlock = true;

//工作流单据间跳转是否解锁
var OS_BILL_Jump_needUnlock = true;

window.onbeforeunload = function(){
	if(OS_BILL_Jump_needUnlock){
		if(binOLWSMNG07_02_global.needUnlock){
			if (window.opener) {
				window.opener.unlockParentWindow();
			}
		}
	}
};




var BINOLWSMNG07_02 = function () {
    
};

BINOLWSMNG07_02.prototype = {
		
	/**
	 * 删除掉画面头部的提示信息框
	 * @return
	 */
	"clearActionMsg":function() {
		$('#actionResultDisplay').html("");
		$('#errorDiv2').attr("style", 'display:none');
		$.each($('#databody >tr'), function(i) {
			if (i > 0) {
				$(this).removeClass('errTRbgColor');
			}
		});
	},
		
	"changeOddEvenColor":function() {
		$("#databody tr:odd").attr("class", "even");
		$("#databody tr:even").attr("class", "odd");
	},
	
	/**
	 * AJAX取得产品当前库存量
	 * 
	 * */
	"getPrtStock": function(productVendorID){
		var url = $("#s_getStockCount").html();
		var lenTR = $("#databody").find("tr");
		var params = getSerializeToken();
		params += "&inDepotID="+$("#depotId").val();
		params += "&inLogicDepotID="+$("#logicinventId").val();
		if(productVendorID == undefined){
			//查明细全部所有
			params += "&" + $("#databody").find("[name='productVendorIDArr']").serialize();
		}else{
			//查当前行的产品
			params += "&productVendorIDArr=" + productVendorID;
		}
		if(lenTR.length>0){
			var callback = function(msg){
				var json = eval('('+msg+')');
				for (var one in json){
					var prtVendorId = json[one].prtVendorId;
					var stock = json[one].stock;
					$("#databody").find("tr").each(function(){
						var $tr = $(this);
						if($tr.find("@[name='productVendorIDArr']").val()==prtVendorId){
							$tr.find("#nowCount").text(stock);
							$tr.find("#dataTDStock").text(stock);
							$tr.find("#bookCountArr").val(stock);
						}
					});
				}
			};
			cherryAjaxRequest({
				url:url,
				param:params,
				callback:callback
			});
		}
	},
	
	"changechkbox":function(obj){
		var chked = $(obj).prop("checked");
		if(!chked){
			$('#allSelect').prop("checked",false);
			return false;
		}
		var flag = true;
		$("#databody :checkbox").each(function(i){
			if($(this).prop("checked")!=true){
				flag=false;
			}
		});
		if(flag){
			$('#allSelect').prop("checked",true);
		}
	},
	
	/**
	 * 输入了盘点数量
	 * 
	 * @param thisObj
	 */
	"changeCount":function(thisObj) {
		var $this = $(thisObj);
		var $td_obj = $this.parent();
		var $tr_obj = $td_obj.parent();
		var price = Number($tr_obj.find(":input[name='priceArr']").val());
		var $amount = $tr_obj.find("#money");
		var count = $this.val();
		if(count == ""){
			//清空盘差
			$tr_obj.find("#dataTdGainStock").text("");
			$tr_obj.find('#checkQuantityArr').html("");
			$tr_obj.find('#gainCount').html("");
			$tr_obj.find('#gainCountArr').val("");
			$tr_obj.find('#htArr').val("0");
			$amount.html("0.00");
			binOLWSMNG07_02.calcTotal();
			return;
		}else if(isNaN(parseInt(count))){
			count = 0;
			$this.val("");
			$tr_obj.find('#htArr').val("2");
		} else {
			count = Math.abs(parseInt(count));
			$this.val(count);
			$tr_obj.find('#htArr').val("2");
			$td_obj.find('#hideQuantiyArr').html(count);
		}
		var amount = (count * price).toFixed(2);
		$amount.text(binOLWSMNG07_02.formateMoney(amount, 2));
		
		var gainQuantity = "";
		if($tr_obj.find('#bookCountArr').val() != "" && $tr_obj.find('#bookCountArr').val() != undefined){
			var tempCount = Number(count)-Number($tr_obj.find('#bookCountArr').val());
			$tr_obj.find("#dataTdGainStock").text(binOLWSMNG07_02.formateMoney(tempCount,0));
			$tr_obj.find('#checkQuantityArr').val(count);
			$tr_obj.find('#gainCount').html(tempCount);
			$tr_obj.find('#gainCountArr').val(tempCount);
		}
		
		binOLWSMNG07_02.calcTotal();
	},
	
	/*
	 * 计算总金额，总数量
	 */
	"calcTotal":function(){
		//计算总金额、总数量
		var rows = $("#databody").children();
		var totalQuantity = 0;
		var totalAmount = 0.00;
		if(rows.length > 0){
			rows.each(function(i){
				var quantity = Number($(this).find("#quantityArr").val());
				var amount = Number($(this).find("[name='priceArr']").val());
				totalQuantity += quantity;
				totalAmount += quantity * amount;
			});
		}
		$("#totalQuantity").html(binOLWSMNG07_02.formateMoney(totalQuantity,0));
		$("#totalAmount").html(binOLWSMNG07_02.formateMoney(totalAmount,2));
	},
	
	/*
	 * 用逗号分割金额、数量
	 */
	"formateMoney":function(money,num){
		money = parseFloat(money);
		money = String(money.toFixed(num));
		var re = /(-?\d+)(\d{3})/;
		while(re.test(money))
			money = money.replace(re,"$1,$2");
		return money;
	},
	
	/**
	 * 全选，全不选
	 */
	"selectAll":function() {
		if ($('#allSelect').prop("checked")) {
			$("#databody :checkbox").each(function() {
				if ($(this).val() != 0) {
					$(this).prop("checked", true);
				}

			});
		} else {
			$("#databody :checkbox").each(function() {
				$(this).removeAttr("checked");
			});
		}
	},
	
	/**
	 * 点击保存按钮
	 */
	"submitSave":function() {
		binOLWSMNG07_02.clearActionMsg();
		var checkFlag = binOLWSMNG07_02.checkData();
		var addType = $("#addType").val();
		if(!checkFlag){
			if (addType == "all") {
				if($('#errorDiv2 #errorSpan2').is(":visible") == false){
					$.each($('#databody >tr'), function(i){
						if($(this).find("#quantityArr").val()==""){
							$(this).find("#gainCountArr").val("0");
						}
					});
				}else{
					return;
				}
			}else if(addType != "all"){
				return;
			}
		}
		//删除没有选择产品的行
		$.each($('#databody >tr'), function(i){
			if($(this).find("#productVendorIDArr").val()==""){
				$(this).remove();
			}
		});
		var callback = function(msg){
			if(window.opener.oTableArr[1] != null)window.opener.oTableArr[1].fnDraw();
		};
		cherryAjaxRequest({
			url : $("#saveUrl").attr("href"),
			param : $('#mainForm').formSerialize(),
			callback : callback,
			coverId:"body"
		});
	},

	/**
	 * 点击申请按钮
	 */
	"submitSend":function() {
		binOLWSMNG07_02.clearActionMsg();
		// 校验数据
		var checkFlag = binOLWSMNG07_02.checkData();
		var addType = $("#addType").val();
		if(!checkFlag){
			// 全盘
			if (addType == "all") {
				if($('#errorDiv2 #errorSpan2').is(":visible") == false){
					var dialogSetting = {
							dialogInit: "#dialogInit",
							width: 	350,
							height: 180,
							title: 	$("#MNG07_dialog_title").html(),
							confirm: $("#MNG07_dialog_reset").html(),
							confirm2: $("#MNG07_dialog_nochange").html(),
							cancel: $("#MNG07_dialog_continue").html(),
							confirmEvent: function(){binOLWSMNG07_02.submit("reset");removeDialog("#dialogInit");},
							confirm2Event: function(){binOLWSMNG07_02.submit("nochange");removeDialog("#dialogInit");},
							cancelEvent: function(){removeDialog("#dialogInit");}
						};
						openDialog(dialogSetting);
						$("#dialogInit").html($("#errmsg_EST00024").val());
				}else{
					return;
				}
			}else if(addType != "all"){
				return;
			}
		}
		//删除没有选择产品的行
		if(addType == "all"){
			$.each($('#databody >tr'), function(i){
				if($(this).find("#productVendorIDArr").val()==""){
					$(this).remove();
				}
			});
		}
		if(checkFlag){
			binOLWSMNG07_02.submit("");
		}
	},
	
	"submit":function(type){
		if(type == "reset"){
			//没有输入数量的产品清零
			$.each($('#databody >tr'), function(i){
				if($(this).find("#quantityArr").val()==""){
					var gainCount = 0-parseInt($(this).find("#bookCountArr").val());
					$(this).find("#gainCountArr").val(String(gainCount));
					$(this).find("#checkQuantityArr").val('0');
					$(this).find("#htArr").val("1");
				}
			});
		}else if(type == "nochange"){
			//没有输入数量的产品库存不变，即盘差为0
			$.each($('#databody >tr'), function(i){
				if($(this).find("#quantityArr").val()==""){
					$(this).find("#gainCountArr").val("0");
					$(this).find("#htArr").val("0");
				}
			});
		}else{
			
		}
		var callback = function(msg){
			if(window.opener.oTableArr[1] != null)window.opener.oTableArr[1].fnDraw();
		};
		cherryAjaxRequest({
			url : $("#submitURL").attr("href"),
			param : $('#mainForm').formSerialize(),
			callback : callback,
			coverId:"body"
		});
	},
	
	/**
	 * 提交前对数据进行检查
	 * 
	 * @returns {Boolean}
	 */
	"checkData":function() {
		binOLWSMNG07_02.changeOddEvenColor();
		var operateType = $("#operateType").val();
		if(operateType == "newBill"){
			var tradeEmployeeID = $("#tradeEmployeeID").val();
			if(tradeEmployeeID == ""){
				$("#errorSpan2").html($("#errmsg_EST00039").val());
				$("#errorDiv2").show();
				return false;
			}
		}
		if ($('#organizationId').val() == null || $('#organizationId').val() == "") {
			$('#errorDiv2 #errorSpan2').html($('#errmsg_EST00013').val());
			$('#errorDiv2').show();
			return false;
		}
		if ($('#depotId').val() == null || $('#depotId').val() == "") {
			$('#errorDiv2 #errorSpan2').html($('#errmsg_EST00036').val());
			$('#errorDiv2').show();
			return false;
		}
		var flag = true;
		var count = 0;
		var addType = $("#addType").val();
		// 检查有无空行
		$.each($('#databody >tr'), function(i) {
			var $this = $(this);
			if($(this).find("#productVendorIDArr").val()!=""){
				count += 1;
				var quantityArr = $this.find(":input[id='quantityArr']").val();
				if (quantityArr == "") {
					flag = false;
					if(addType != "all"){
						// 全盘时允许不输入数量
						$(this).attr("class", "errTRbgColor");
					}
				} else {
					$(this).removeClass('errTRbgColor');
				}
			} else {
				// 产品厂商ID为空，即产品输入错误
				flag = false;
				$(this).attr("class", "errTRbgColor");
			}
		});
		if (!flag) {
			
			//指定盘点存在无效数据
			if(addType == "given"){
				$('#errorDiv2 #errorSpan2').html($('#errmsg_EST00047').val());
				$('#errorDiv2').show();
			} else if(addType != "all") {
				// 有无效的数据行，请补充完整或删除该行。
				$('#errorDiv2 #errorSpan2').html($('#errmsg_EST00008').val());
				$('#errorDiv2').show();
			} else {
				// 有空行存在(不会出现产品错误的情况)，全盘允许不输入。
			}
			return flag;
		}
		if (count == 0) {
			flag = false;
			$('#errorDiv2 #errorSpan2').html($('#errmsg_EST00022').val());
			$('#errorDiv2').show();
			return flag;
		}
		return flag;
	},
	
	/**
	 * 删除选中行
	 */
	"deleterow":function() {
		binOLWSMNG07_02.clearActionMsg();
		var $checkboxs = $("#databody").find(":checkbox:checked");
		$checkboxs.parent().parent().remove();
		binOLWSMNG07_02.changeOddEvenColor();
		$('#allSelect').prop("checked", false);
		binOLWSMNG07_02.calcTotal();
		var rowsCount = $("#databody").children().length;
		if(rowsCount == 0){
			binOLWSMNG07_02.addNewLine();
		}
	},
	
	/**
	 * 产品下拉输入框选中后执行方法
	 */
	"pbAfterSelectFun":function(info){
		$('#'+info.elementId).val(info.unitCode);
		//验证产品厂商ID是否重复
		var flag = true;
		$.each($('#databody >tr'), function(i){
			if($(this).find("[name='productVendorIDArr']").val() == info.prtVendorId){
				flag = false;
				$("#errorSpan2").html($("#errmsg_EST00035").val());
				$("#errorDiv2").show();
				return;
			}
		});
		if(flag){
			$("#errorDiv2").hide();
			
			var price = info.price;//销售价格
			var sysConfigUsePrice = $("#sysConfigUsePrice").val();
			if(sysConfigUsePrice == "MemPrice"){
				price = info.memPrice;//会员价格
			} else if(sysConfigUsePrice == "StandardCost") {
				price = info.standardCost;// 结算价
			}
			
			//设置隐藏值
			$('#'+info.elementId).parent().parent().find("[name='productVendorIDArr']").val(info.prtVendorId);
			$('#'+info.elementId).parent().parent().find("[name='priceArr']").val(price);
			$('#'+info.elementId).parent().parent().find("#unitCodeArr").val(info.unitCode);
			$('#'+info.elementId).parent().parent().find("#barCodeArr").val(info.barCode);
			
			//设置显示值
			$('#'+info.elementId).parent().parent().find("#spanUnitCode").html(info.unitCode);
			$('#'+info.elementId).parent().parent().find("#spanBarCode").html(info.barCode);
			$('#'+info.elementId).parent().parent().find("#spanProductName").html(info.productName);
			$('#'+info.elementId).parent().parent().find("#spanPrice").html(binOLWSMNG07_02.formateMoney(price,2));
			
			//取消该文本框的自动完成并隐藏。
			$('#'+info.elementId).unautocomplete();
			$('#'+info.elementId).hide();
			
			//跳到数量文本框
			var $quantityInputText =  $('#'+info.elementId).parent().parent().find("#quantityArr");
			
			$quantityInputText.select();
			
			//查询库存
			binOLWSMNG07_02.getPrtStock(info.prtVendorId);
		}
	},
	
	/**
	 * 添加新行
	 */
	"addNewLine":function(){
		binOLWSMNG07_02.clearActionMsg();
		
		// 设置全选状态
		$('#allSelect').prop("checked",false);
		
		//盘点允许添加多条空白行
		var addNewLineFlag = true;
		
		if(addNewLineFlag){
			var nextIndex = parseInt($("#rowNumber").val())+1;
			$("#rowNumber").val(nextIndex);
			var blindFlag = $("#blindFlag").val();
			var html = '<tr id="dataRow'+nextIndex+'">';
			html += '<td class="center"><input type="checkbox" id="chkbox" onclick="binOLWSMNG07_02.changechkbox(this);"/></td>';
			html += '<td><span id="spanUnitCode"></span><input type="text" id="unitCodeBinding_'+nextIndex+'" name="unitCodeBinding"  value=""/><input type="hidden" id="unitCodeArr"></td>';
			html += '<td><span id="spanBarCode"></span><input type="hidden" id="barCodeArr"></td>';
			html += '<td><span id="spanProductName"></span></td>';
		    html += '<td style="text-align:right;"><span id="spanPrice"/></span></td>';
			if(blindFlag == "0"){
				html += '<td id="nowCount" style="text-align:right;"></td>';
			}
			html += '<td class="alignRight"><input id="quantityArr" value="" cssClass="text-number" style="width:120px;text-align:right;" size="10" maxlength="4" onchange="binOLWSMNG07_02.changeCount(this)" onkeyup="binOLWSMNG07_02.changeCount(this)"  cssStyle="width:98%"/></td>';
			if(blindFlag == "0"){
				html += '<td id="dataTdGainStock" style="text-align:right;"></td>';
			}
			if(blindFlag == "0"){
				html += '<td id="money" class="center" style="text-align:right;">0.00</td>';
			}
			html += '<td><input  id="commentsArr" name="commentsArr" value="" type="text"maxlength="200" style="width:96%;"/></td>';
			html +='<td style="display:none">'
	        +'<input type="hidden" id="productVendorIDArr" name="productVendorIDArr" value=""/>'
	        +'<input type="hidden" name="checkQuantityArr" id="checkQuantityArr" value=""></input>'
	        +'<input type="hidden" name="bookCountArr" id="bookCountArr" value=""></input>'
	        +'<input type="hidden" name="gainCountArr" id="gainCountArr" value=""></input>'
	        +'<input type="hidden" name="htArr" id="htArr" value="0"></input>'
	        +'<input type="hidden" name="priceArr" id="priceArr" value=""></input></td></tr>';
			$("#databody").append(html);
	
			var unitCode = "unitCodeBinding_"+nextIndex;
			binOLWSMNG07_02.setProductBinding(unitCode);
	
			$("#unitCodeBinding_"+nextIndex).focus();
			
			binOLWSMNG07_02.bindInput();
			
			//改变奇偶行的样式
			binOLWSMNG07_02.changeOddEvenColor();
		}
	},

	"setProductBinding":function(id){
		cntProductBinding({counterCode:$("#counterCode").val(),elementId:id,showNum:20,targetDetail:true,afterSelectFun:binOLWSMNG07_02.pbAfterSelectFun});
	},

	"bindInput":function(){
		var tableName = "mainTable";
		$("#"+tableName+" #databody tr").each(function(i) {
			var trID = $(this).attr("id");
			var trParam = "#"+tableName +" #"+trID;
			binOLWSMNG07_02.bindInputTR(trParam);
		})
	},

	"bindInputTR":function(trParam){
		//按Enter键到下一个文本框，备注按Enter键到下一行或新增行。
		var $inp = $(trParam+" input:text");
		$inp.bind('keydown', function (e) {
			if($("#currentMenuID").val() == "BINOLWSMNG07_02"){
				var key = e.which;
				if (key == 13) {
					//回车键
					e.preventDefault();
		            
					//当前文本框为下拉框可见且输入有问题如重复，停止跳到下一个
					var nextFlag = true;
					if($(this).attr("name") == "unitCodeBinding" && $(this).is(":visible")){
						nextFlag = false;
					}
					if(nextFlag){
						var nxtIdx = $inp.index(this) + 1;
						var $nextInputText = $(trParam+" input:text:eq("+nxtIdx+")");
						if($nextInputText.length>0){
							//跳到下一个文本框
							$nextInputText.focus();
						}else{
							//跳到下一行第一个文本框，如果没有下一行，新增一行
							if($(trParam).next().length==0){
								binOLWSMNG07_02.addNewLine();
							}else{
								$(trParam).next().find("input:text:visible:eq(0)").focus();
							}
						}
					}
				}
			}
		});
	},
	
	"modifyForm":function(){
		$("#showToolbar").removeClass("hide");
		$("#showCheckbox").removeClass("hide");
		
		$.each($('#databody').find('tr'), function(n){
			$(this).find("#quantityArr").removeClass("hide");
			$(this).find("[name='commentsArr']").removeClass("hide");
			
			$(this).find("#hideQuantiyArr").addClass("hide");
			$(this).find("#hideReason").addClass("hide");
		});
		$("#btnSave").css("display","");
		$("#btnReturn").css("display","");
		$("#btn-icon-edit-big").css("display","none");
		return "";
	},
	
	/**
	 * 从编辑状态返回详细状态
	 */
	"back":function(){
		binOLWSMNG07_02_global.needUnlock=false;
		var tokenVal = $('#csrftoken',window.opener.document).val();
		$('#proStocktakeDetailUrl').find("input[name='csrftoken']").val(tokenVal);
		$('#proStocktakeDetailUrl').submit();
	},
	
	/**
	 * 商品全盘
	 */
	"takingRequest":function(){
		binOLWSMNG07_02.clearActionMsg();
		if ($('#organizationId').val() == null || $('#organizationId').val() == "") {
			$('#errorDiv2 #errorSpan2').html($('#errmsg_EST00013').val());
			$('#errorDiv2').show();
			return false;
		}
		if ($('#depotId').val() == null || $('#depotId').val() == "") {
			$('#errorDiv2 #errorSpan2').html($('#errmsg_EST00036').val());
			$('#errorDiv2').show();
			return false;
		}
		$("#btnTakingRequest").hide();
		$("#btnCancelStockTaking").show();
		$("#databody").empty();
		var callback = function(msg){
			var json = eval('('+msg+')');
			$("#rowNumber").val("0");
			for(var one in json){
				var price = json[one].SalePrice;//销售价格
				var sysConfigUsePrice = $("#sysConfigUsePrice").val();
				if(sysConfigUsePrice == "MemPrice"){
					price = json[one].MemPrice;//会员价格
				} else if(sysConfigUsePrice == "StandardCost") {
					price = json[one].StandardCost;// 结算价
				}
				
				var nextIndex = parseInt($("#rowNumber").val())+1;
				$("#rowNumber").val(nextIndex);
				var blindFlag = $("#blindFlag").val();
				var html = '<tr id="dataRow'+nextIndex+'">';
				html += '<td class="center">'+nextIndex+'</td>';
				html += '<td><span id="spanUnitCode">'+json[one].UnitCode+'</span></td>';
				html += '<td><span id="spanBarCode">'+json[one].BarCode+'</span></td>';
				html += '<td><span id="spanProductName">'+json[one].NameTotal+'</span></td>';
			    html += '<td style="text-align:right;"><span id="spanPrice"/>'+binOLWSMNG07_02.formateMoney(price,2)+'</span></td>';
				if(blindFlag == "0"){
					html += '<td id="nowCount" style="text-align:right;">'+json[one].StockQuantity+'</td>';
				}
				html += '<td class="alignRight"><input id="quantityArr" value="" cssClass="text-number" style="width:120px;text-align:right;" size="10" maxlength="4" onchange="binOLWSMNG07_02.changeCount(this)" onkeyup="binOLWSMNG07_02.changeCount(this)"  cssStyle="width:98%"/></td>';
				if(blindFlag == "0"){
					html += '<td id="dataTdGainStock" style="text-align:right;"></td>';
				}
				if(blindFlag == "0"){
					html += '<td id="money" class="center" style="text-align:right;">0.00</td>';
				}
				html += '<td><input  id="commentsArr" name="commentsArr" value="" type="text"maxlength="200" style="width:96%;"/></td>';
				html +='<td style="display:none">'
		        +'<input type="hidden" id="productVendorIDArr" name="productVendorIDArr" value="'+json[one].BIN_ProductVendorID+'"/>'
		        +'<input type="hidden" name="checkQuantityArr" id="checkQuantityArr" value=""></input>'
		        +'<input type="hidden" name="bookCountArr" id="bookCountArr" value="'+json[one].StockQuantity+'"></input>'
		        +'<input type="hidden" name="gainCountArr" id="gainCountArr" value=""></input>'
		        +'<input type="hidden" name="htArr" id="htArr" value="0"></input>'
		        +'<input type="hidden" name="priceArr" id="priceArr" value="'+price+'"></input></td></tr>';
				$("#databody").append(html);
			}
		};
		var url =$('#s_takingRequest').html();
		var param = "organizationId="+$('#organizationId').val()+"&depotId="+$('#depotId').val()+"&logicinventId="+$("#logicinventId").val();
		cherryAjaxRequest({
			url:url,
			param:param,
			callback:callback
		});
	},
	
	/**
	 * 商品指定盘点
	 */
	"givenRequest":function(){
		binOLWSMNG07_02.clearActionMsg();
		if ($('#organizationId').val() == null || $('#organizationId').val() == "") {
			$('#errorDiv2 #errorSpan2').html($('#errmsg_EST00013').val());
			$('#errorDiv2').show();
			return false;
		}
		if ($('#depotId').val() == null || $('#depotId').val() == "") {
			$('#errorDiv2 #errorSpan2').html($('#errmsg_EST00036').val());
			$('#errorDiv2').show();
			return false;
		}
		$("#btnGivenRequest").hide();
		$("#btnCancelStockTaking").show();
		$("#databody").empty();
		var callback = function(msg){
			var json = eval('('+msg+')');
			$("#rowNumber").val("0");
			for(var one in json){
				var price = json[one].SalePrice;//销售价格
				var sysConfigUsePrice = $("#sysConfigUsePrice").val();
				if(sysConfigUsePrice == "MemPrice"){
					price = json[one].MemPrice;//会员价格
				} else if(sysConfigUsePrice == "StandardCost") {
					price = json[one].StandardCost;// 结算价
				}
				
				var nextIndex = parseInt($("#rowNumber").val())+1;
				$("#rowNumber").val(nextIndex);
				var blindFlag = $("#blindFlag").val();
				var html = '<tr id="dataRow'+nextIndex+'">';
				html += '<td class="center">'+nextIndex+'</td>';
				html += '<td><span id="spanUnitCode">'+json[one].UnitCode+'</span></td>';
				html += '<td><span id="spanBarCode">'+json[one].BarCode+'</span></td>';
				html += '<td><span id="spanProductName">'+json[one].NameTotal+'</span></td>';
			    html += '<td style="text-align:right;"><span id="spanPrice"/>'+binOLWSMNG07_02.formateMoney(price,2)+'</span></td>';
				if(blindFlag == "0"){
					html += '<td id="nowCount" style="text-align:right;">'+json[one].StockQuantity+'</td>';
				}
				html += '<td class="alignRight"><input id="quantityArr" value="" cssClass="text-number" style="width:120px;text-align:right;" size="10" maxlength="4" onchange="binOLWSMNG07_02.changeCount(this)" onkeyup="binOLWSMNG07_02.changeCount(this)"  cssStyle="width:98%"/></td>';
				if(blindFlag == "0"){
					html += '<td id="dataTdGainStock" style="text-align:right;"></td>';
				}
				if(blindFlag == "0"){
					html += '<td id="money" class="center" style="text-align:right;">0.00</td>';
				}
				html += '<td><input  id="commentsArr" name="commentsArr" value="" type="text"maxlength="200" style="width:96%;"/></td>';
				html +='<td style="display:none">'
		        +'<input type="hidden" id="productVendorIDArr" name="productVendorIDArr" value="'+json[one].BIN_ProductVendorID+'"/>'
		        +'<input type="hidden" name="checkQuantityArr" id="checkQuantityArr" value=""></input>'
		        +'<input type="hidden" name="bookCountArr" id="bookCountArr" value="'+json[one].StockQuantity+'"></input>'
		        +'<input type="hidden" name="gainCountArr" id="gainCountArr" value=""></input>'
		        +'<input type="hidden" name="htArr" id="htArr" value="0"></input>'
		        +'<input type="hidden" name="priceArr" id="priceArr" value="'+price+'"></input></td></tr>';
				$("#databody").append(html);
			}
		};
		var url =$('#s_givenRequest').html();
		var param = "organizationId="+$('#organizationId').val()+"&depotId="+$('#depotId').val()+"&logicinventId="+$("#logicinventId").val();
		cherryAjaxRequest({
			url:url,
			param:param,
			callback:callback
		});
	},
	
	"cancelStockTaking":function(){
		var addType = $("#addType").val();
		$("#databody").empty();
		if(addType != "all"){
			$("#btnGivenRequest").show();
		}else{
			$("#btnTakingRequest").show();
		}
		$("#btnCancelStockTaking").hide();
	}
};
var binOLWSMNG07_02 = new BINOLWSMNG07_02();

$(document).ready(function() {
	if (window.opener) {
		window.opener.lockParentWindow();
	}
	var operateType = $("#operateType").val();
	var addType = $("#addType").val();
	if(operateType == "newBill"){
		if(addType != "all" && addType !="given"){
			binOLWSMNG07_02.addNewLine();
		}
		$("#showToolbar").show();
	}else if(operateType == "2"){
		if(addType != "all" && addType !="given"){
			binOLWSMNG07_02.bindInput();
		}else{
			$("#showToolbar").hide();
		}
	}
	
	if(operateType == "newBill" || operateType == "2"){
		if(refreshSessionTimerID == null){
			refreshSessionTimerID = setInterval("refreshCherrySession($('#s_refreshAjax').html())", 30*60*1000);
		}
	}
} );
$(window).load(function(){
	if($("#btnGivenRequest").length==1){
		binOLWSMNG07_02.givenRequest();
	}
});
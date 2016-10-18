var binOLWSMNG03_02_global = {};
//是否需要解锁
binOLWSMNG03_02_global.needUnlock = true;

//工作流单据间跳转是否解锁
var OS_BILL_Jump_needUnlock = true;

window.onbeforeunload = function(){
	if(OS_BILL_Jump_needUnlock){
		if(binOLWSMNG03_02_global.needUnlock){
			if (window.opener) {
				window.opener.unlockParentWindow();
			}
		}
	}
};




var BINOLWSMNG03_02 = function () {
    
};

BINOLWSMNG03_02.prototype = {
		
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
		params += "&inDepotID="+$("#inventoryInfoID").val();
		params += "&inLogicDepotID="+$("#logicInventoryInfoID").val();
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
						if($tr.find("@[name='prtVendorId']").val()==prtVendorId){
							$tr.find("td:eq(5)").text(stock);
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
	
	"getHtmlFun":function(info){
		var productVendorId = info.productVendorId;//产品厂商Id
		var unitCode = info.unitCode;//厂商编码
		var barCode = info.barCode;//产品条码
		var nameTotal = info.nameTotal;//产品名称
		var price = info.standardCost;//结算价格
		if(isEmpty(price)){
			price = parseFloat("0.0");
		}else{
			price = parseFloat(price);
		}
		price = price.toFixed(2);

		var quantity=info.quantity;//数量
		if(isEmpty(quantity)){
			quantity = "";
		}
		var amount = price * Number(quantity);//金额
		amount = amount.toFixed(2);
		var reason = info.reason;//备注
		if(quantity == 0){quantity = "";}
		if(isEmpty(reason)){reason = "";}
		var html = '<tr>';
		html += '<td class="center"><input type="checkbox" id="chkbox" onclick="binOLWSMNG03_02.changechkbox(this);"/></td>';
		html += '<td>' + unitCode +'<input type="hidden" id="unitCodeArr"  value="'+ unitCode + '"/></td>';
		html += '<td>' + barCode + '<input type="hidden" id="barCodeArr" value="'+  barCode + '"/></td>';
		html += '<td>' + nameTotal + '</td>';
		html += '<td style="text-align:right;" >'+ price + '</td>';
		html += '<td style="text-align:right;"></td>';
		html += '<td class="alignRight"><input value="' + quantity +'" name="quantityArr" id="quantityArr" style="width:120px;text-align:right;" size="10" maxlength="9" onchange="binOLWSMNG03_02.changeCount(this)"/></td>';
		html += '<td class="center" style="text-align:right;">' + amount + '</td>';
		html += '<td class="center"><input value="' + reason +'" name="commentsArr"  type="text" id="commentsArr" size="25" maxlength="200"  cssStyle="width:98%"/></td>';
		html +='<td style="display:none">'
			+'<input type="hidden" name="prtVendorId" value="'+ productVendorId + '"/>'
			+'<input type="hidden" id="productVendorIDArr" name="productVendorIDArr" value="'+productVendorId+'"/>'
			+'<input type="hidden" id="priceUnitArr" name="priceUnitArr" value="'+ price +'"/></td></tr>';
		return html;
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
	 * 输入了退库数量
	 * 
	 * @param thisObj
	 */
	"changeCount":function(thisObj) {
		var $this = $(thisObj);
		var $td_obj = $this.parent();
		var $tr_obj = $td_obj.parent();
		var price = Number($tr_obj.find(":input[name='priceUnitArr']").val());
		var $amount = $td_obj.next();
		var count = $this.val();
		if(isNaN(parseInt(count))){
			count = 0;
			$this.val("");
		} else {
			count = Math.abs(parseInt(count));
			$(thisObj).val(count);
		}
		var amount = (count * price).toFixed(2);
		$amount.text(binOLWSMNG03_02.formateMoney(amount, 2));
		
		binOLWSMNG03_02.calcTotal();
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
				var amount = Number($(this).find("[name='priceUnitArr']").val());
				totalQuantity += quantity;
				totalAmount += quantity * amount;
			});
		}
		$("#totalQuantity").html(binOLWSMNG03_02.formateMoney(totalQuantity,0));
		$("#totalAmount").html(binOLWSMNG03_02.formateMoney(totalAmount,2));
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
		binOLWSMNG03_02.clearActionMsg();
		if (!binOLWSMNG03_02.checkData()) {
			return;
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
		//判断是否允许负库存操作字段
		var nsOperation = $("#nsOperation").val();
		var a=true;
		if(nsOperation=='N'){
			$.each($('#databody >tr'), function(i){
				//输入数量
				var quantity=$(this).find("#quantityArr").val();
				//库存数量
				var stock=$(this).find("td:#nowCount").text();
				if(Number(quantity)>Number(stock)){
					//如果quantity>stock,不能提交
					a=false;
				}
			});
			if(a){
				$("#errorDiv2").hide();
				binOLWSMNG03_02.clearActionMsg();
				if (!binOLWSMNG03_02.checkData()) {
					return;
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
					url : $("#submitURL").attr("href"),
					param : $('#mainForm').formSerialize(),
					callback : callback,
					coverId:"body"
				});
			}else {
				$("#errorSpan2").html($("#errmsg_EST00042").val());
				$("#errorDiv2").show();
			}
		}else{
			binOLWSMNG03_02.clearActionMsg();
			if (!binOLWSMNG03_02.checkData()) {
				return;
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
				url : $("#submitURL").attr("href"),
				param : $('#mainForm').formSerialize(),
				callback : callback,
				coverId:"body"
			});
		}
	},
	/**
	 * 提交前对数据进行检查
	 * 
	 * @returns {Boolean}
	 */
	"checkData":function() {
		binOLWSMNG03_02.changeOddEvenColor();
		var flag = true;
		var count = 0;
		var operateType = $("#operateType").val();
		if(operateType == "newBill"){
			var tradeEmployeeID = $("#tradeEmployeeID").val();
			if(tradeEmployeeID == ""){
				$("#errorSpan2").html($("#errmsg_EST00039").val());
				$("#errorDiv2").show();
				return false;
			}
		}
		// 检查有无空行
		$.each($('#databody >tr'), function(i) {
			var $this = $(this);
			if($(this).find("#productVendorIDArr").val()!=""){
				count += 1;
				var quantityArr = $this.find(":input[name='quantityArr']").val();
				if (quantityArr == "" || quantityArr == "0") {
					flag = false;
					$(this).attr("class", "errTRbgColor");
				} else {
					$(this).removeClass('errTRbgColor');
				}
			}
		});

		if (!flag) {
			// 有空行存在
			$('#errorDiv2 #errorSpan2').html($('#errmsg_EST00008').val());
			$('#errorDiv2').show();
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
		binOLWSMNG03_02.clearActionMsg();
		var $checkboxs = $("#databody").find(":checkbox:checked");
		$checkboxs.parent().parent().remove();
		binOLWSMNG03_02.changeOddEvenColor();
		$('#allSelect').prop("checked", false);
		binOLWSMNG03_02.calcTotal();
		var rowsCount = $("#databody").children().length;
		if(rowsCount == 0){
			binOLWSMNG03_02.addNewLine();
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
			
			var price = info.price;//结算价格
			var sysConfigUsePrice = $("#sysConfigUsePrice").val();
			if(sysConfigUsePrice == "MemPrice"){
				price = info.memPrice;//会员价格
			} else if(sysConfigUsePrice == "StandardCost") {
				price = info.standardCost;// 结算价
			}
			
			//设置隐藏值
			$('#'+info.elementId).parent().parent().find("[name='prtVendorId']").val(info.prtVendorId);
			$('#'+info.elementId).parent().parent().find("[name='productVendorIDArr']").val(info.prtVendorId);
			$('#'+info.elementId).parent().parent().find("[name='referencePriceArr']").val(price);
			$('#'+info.elementId).parent().parent().find("#unitCodeArr").val(info.unitCode);
			$('#'+info.elementId).parent().parent().find("#barCodeArr").val(info.barCode);
			
			//设置显示值
			$('#'+info.elementId).parent().parent().find("#spanUnitCode").html(info.unitCode);
			$('#'+info.elementId).parent().parent().find("#spanBarCode").html(info.barCode);
			$('#'+info.elementId).parent().parent().find("#spanProductName").html(info.productName);
			$('#'+info.elementId).parent().parent().find("#spanPrice").html(binOLWSMNG03_02.formateMoney(price,2));
			$('#'+info.elementId).parent().parent().find("#priceUnitArr").val(price);
			
			//取消该文本框的自动完成并隐藏。
			$('#'+info.elementId).unautocomplete();
			$('#'+info.elementId).hide();
			
//			//跳到下一个文本框
//			var nxtIdx = $('#'+info.elementId).parent().parent().find("input:text").index($('#'+info.elementId)) + 1;
//			var $nextInputText =  $('#'+info.elementId).parent().parent().find("input:text:eq("+nxtIdx+")");
			
			//跳到数量文本框
			var $quantityInputText =  $('#'+info.elementId).parent().parent().find("#quantityArr");

			//选中一个产品后后默认发货数量1
			var quantity = $('#'+info.elementId).parent().parent().find("#quantityArr").val();
			if(quantity == ""){
				$('#'+info.elementId).parent().parent().find("#quantityArr").val("1");
			}
			//计算金额（防止先输入数量后输入产品）
			binOLWSMNG03_02.changeCount($quantityInputText);
			
			$quantityInputText.select();
			
			//查询库存
			binOLWSMNG03_02.getPrtStock(info.prtVendorId);
		}
	},
	
	/**
	 * 添加新行
	 */
	"addNewLine":function(){
		binOLWSMNG03_02.clearActionMsg();
		
		// 设置全选状态
		$('#allSelect').prop("checked",false);
		
		//已有空行不新增一行
		var addNewLineFlag = true;
		$.each($('#databody >tr'), function(i){
			if($(this).find("[name='unitCodeBinding']").is(":visible")){
				addNewLineFlag = false;
				$(this).find("[name='unitCodeBinding']").focus();
				return;
			}
		});
		
		if(addNewLineFlag){
			var nextIndex = parseInt($("#rowNumber").val())+1;
			$("#rowNumber").val(nextIndex);
			var html = '<tr id="dataRow'+nextIndex+'">';
			html += '<td class="center"><input type="checkbox" id="chkbox" onclick="binOLWSMNG03_02.changechkbox(this);"/></td>';
			html += '<td><span id="spanUnitCode"></span><input type="text" id="unitCodeBinding_'+nextIndex+'" name="unitCodeBinding"  value=""/><input type="hidden" id="unitCodeArr"></td>';
			html += '<td><span id="spanBarCode"></span><input type="hidden" id="barCodeArr"></td>';
			html += '<td><span id="spanProductName"></span></td>';
			html += '<td style="text-align:right;"><span id="spanPrice"/></span></td>';
			html += '<td id="nowCount" style="text-align:right;"></td>';
			html += '<td class="alignRight"><input id="quantityArr" name="quantityArr" value="" cssClass="text-number" style="width:120px;text-align:right;" size="10" maxlength="4" onchange="binOLWSMNG03_02.changeCount(this)" onkeyup="binOLWSMNG03_02.changeCount(this)"  cssStyle="width:98%"/></td>';
			html += '<td id="money" class="center" style="text-align:right;">0.00</td>';
			html += '<td><input  id="commentsArr" name="commentsArr" value="" type="text"maxlength="200" style="width:96%;"/></td>';
			html +='<td style="display:none">'
	        +'<input type="hidden" name="prtVendorId" value=""/>'
	        +'<input type="hidden" id="productVendorIDArr" name="productVendorIDArr" value=""/>'
			+'<input type="hidden" id="priceUnitArr" name="priceUnitArr" value=""/></td></tr>';
			$("#databody").append(html);
	
			var unitCode = "unitCodeBinding_"+nextIndex;
			binOLWSMNG03_02.setProductBinding(unitCode);
	
			$("#unitCodeBinding_"+nextIndex).focus();
			
			binOLWSMNG03_02.bindInput();
			
			//改变奇偶行的样式
			binOLWSMNG03_02.changeOddEvenColor();
		}
	},

	"setProductBinding":function(id){
		cntProductBinding({counterCode:$("#counterCode").val(),elementId:id,showNum:20,targetDetail:true,afterSelectFun:binOLWSMNG03_02.pbAfterSelectFun});
	},

	"bindInput":function(){
		var tableName = "mainTable";
		$("#"+tableName+" #databody tr").each(function(i) {
			var trID = $(this).attr("id");
			var trParam = "#"+tableName +" #"+trID;
			binOLWSMNG03_02.bindInputTR(trParam);
		})
	},

	"bindInputTR":function(trParam){
		//按Enter键到下一个文本框，备注按Enter键到下一行或新增行。
		var $inp = $(trParam+" input:text");
		$inp.bind('keydown', function (e) {
			if($("#currentMenuID").val() == "BINOLWSMNG03_02"){
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
								binOLWSMNG03_02.addNewLine();
							}else{
								$(trParam).next().find("input:text:visible:eq(0)").focus();
							}
						}
					}
				}
			}
		});
	},
	
	"back":function(){
		binOLWSMNG03_02_global.needUnlock=false;
		var tokenVal = $('#csrftoken',window.opener.document).val();
		$('#proReturnReqDetailUrl').find("input[name='csrftoken']").val(tokenVal);
		$('#proReturnReqDetailUrl').submit();
	},
	
	/**
	 * 开放修改单据
	 * @return
	 */
	"modifyForm":function(){
		$("#showBtnopenDepartBox").removeClass("hide");
		$("#showAcceptDepotCodeName").removeClass("hide");
		$("#showAcceptLogicInventoryName").removeClass("hide");
		$("#showToolbar").removeClass("hide");
		$("#showCheckbox").removeClass("hide");
		$("#showOrderTypeSelect").removeClass("hide");
		
		$("#hideAcceptDepotCodeName").addClass("hide");
		$("#hideAcceptLogicInventoryName").addClass("hide");
		$("#hideOrderType").addClass("hide");
		
		$.each($('#databody').find('tr'), function(n){
			$(this).find("#dataTd0").removeClass("hide");
			$(this).find("#dataTd6").removeClass("hide");
			$(this).find("[name='quantityArr']").removeClass("hide");
			$(this).find("[name='reasonArr']").removeClass("hide");
			$(this).find("[name='commentsArr']").removeClass("hide");
			
			$(this).find("#hideQuantiyArr").addClass("hide");
			$(this).find("#hideReason").addClass("hide");
		});
		$("#btnReturn").css("display","");
		$("#btn-icon-edit-big").css("display","none");
		$("#btnSave").css("display","");
	},
	
	"checkRRData":function(){
		var tradeEmployeeID = $("#tradeEmployeeID").val();
		if(tradeEmployeeID == ""){
			$("#errorSpan2").html($("#errmsg_EST00039").val());
			$("#errorDiv2").show();
			return false;
		}else{
			return true;
		}
	},
	
	"beforeDoActionFun":function(){
		if(binOLWSMNG03_02.checkRRData()){
			return "doaction";
		}else{
			return "";
		}
	}
};
var binOLWSMNG03_02 = new BINOLWSMNG03_02();

$(document).ready(function() {
	if (window.opener) {
		window.opener.lockParentWindow();
	}
	var operateType = $("#operateType").val();
	if(operateType == "newBill"){
		binOLWSMNG03_02.addNewLine();
	}else if(operateType == "2"){
		binOLWSMNG03_02.bindInput();
	}
} );
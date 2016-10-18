var binOLWSMNG04_02_global = {};
//是否需要解锁
binOLWSMNG04_02_global.needUnlock = true;

//工作流单据间跳转是否解锁
var OS_BILL_Jump_needUnlock = true;

window.onbeforeunload = function(){
	if(OS_BILL_Jump_needUnlock){
		if(binOLWSMNG04_02_global.needUnlock){
			if (window.opener) {
				window.opener.unlockParentWindow();
			}
		}
	}
};




var BINOLWSMNG04_02 = function () {
    
};

BINOLWSMNG04_02.prototype = {
		
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
	 * 调出部门弹出框
	 * 注：1、新后台的调出部门必须与调入部门属于同一上级；
	 * 	   2、云POS与终端一致，不用强制调入部门与调出部门同属一个上级部门。
	 * WITPOSQA-18626	
	 * @return
	 */
	"openOutDepartBox":function(thisObj) {
		binOLWSMNG04_02.clearActionMsg();
		if ($('#inOrganizationID').val() == null || $('#inOrganizationID').val() == "") {
			$('#errorDiv2 #errorSpan2').html($('#errmsgEST00013').val());
			$('#errorDiv2').show();
			return false;
		}
		if ($('#inDepotID').val() == null || $('#inDepotID').val() == "") {
			$('#errorDiv2 #errorSpan2').html($('#errmsg_EST00036').val());
			$('#errorDiv2').show();
			return false;
		}
		binOLWSMNG04_02.changeOddEvenColor();
		// 云POS只支持柜台间的调拨
		var departType = "";
		for ( var i = 0; i < $("#departTypePop option").length; i++) {
			var departTypeValue = $("#departTypePop option:eq(" + i + ")").val();
			// 只允许向柜台做调入申请
			if(departTypeValue == '4') {
				departType += "&departType=" + departTypeValue;
			}
		}
		var param = "checkType=radio&businessType=1&testTypeFlg=1&levelFlg=0&organizationId=" + $("#inOrganizationID").val() + departType;
		param += "&valid=1";
		// 用于判断是否是云POS做的业务
		param += "&cloudPosFlag=1";
		//云POS调入调出只能云POS柜台之间（并且没有POS机）
		param += "&isPosFlag=0";
		var callback = function() {
			var $selected = $('#departDataTable').find(':input[checked]').parents("tr");
			if ($selected.length > 0) {
				var departId = $selected.find("input@[name='organizationId']").val();
				var departCode = $selected.find("td:eq(1)").text();
				var departName = $selected.find("td:eq(2)").text();
				$("#outOrganizationID").val(departId);
				$("#outOrgName").html("(" + departCode + ")" + departName);
			}
		};
		popDataTableOfDepart(thisObj, param, callback);
	},
	
	/**
	 * 产品弹出框
	 * 
	 * */
	"openProPopup":function(){
		var that = this;
		binOLWSMNG04_02.clearActionMsg();
		if ($('#inOrganizationID').val() == null || $('#inOrganizationID').val() == "") {
			$('#errorDiv2 #errorSpan2').html($('#errmsg_EST00013').val());
			$('#errorDiv2').show();
			return false;
		}
		if ($('#inDepotID').val() == null || $('#inDepotID').val() == "") {
			$('#errorDiv2 #errorSpan2').html($('#errmsg_EST00036').val());
			$('#errorDiv2').show();
			return false;
		}
		if($('#outOrganizationID').val()==null || $('#outOrganizationID').val()==""){
			$('#errorDiv2 #errorSpan2').html($('#errmsg_EST00037').val());
			$('#errorDiv2').show();
			return false;
		}
		// 产品弹出框属性设置
		var option = {
			targetId: "databody",//目标区ID
			checkType : "checkbox",// 选择框类型
			mode : 2, // 模式
			brandInfoId : $("#brandInfoId").val(),// 品牌ID
			getHtmlFun:that.getHtmlFun,// 目标区追加数据行function
			click:function(){//点击确定按钮之后的处理
				var checkbox= $('#dataTableBody').find(':input').is(":checked");
				//改变奇偶行的样式
				binOLWSMNG04_02.changeOddEvenColor();
				// 设置全选状态
				var $checkboxs = $('#databody').find(':checkbox');
				var $unChecked = $('#databody').find(':checkbox:not(":checked")');
				if($unChecked.length == 0 && $checkboxs.length > 0){
					$('#allSelect').prop("checked",true);
				}else{
					$('#allSelect').prop("checked",false);
				}
				// AJAX取得产品当前库存量
				that.getPrtStock();
			}
		};
		// 调用产品弹出框共通
		popAjaxPrtDialog(option);
	},
	
	/**
	 * AJAX取得产品当前库存量
	 * 
	 * */
	"getPrtStock": function(productVendorID){
		var url = $("#s_getStockCount").html();
		var lenTR = $("#databody").find("tr");
		var params = getSerializeToken();
		params += "&inDepotID="+$("#inDepotID").val();
		params += "&inLogicDepotID="+$("#inLogicDepotID").val();
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
		html += '<td class="center"><input type="checkbox" id="chkbox" onclick="binOLWSMNG04_02.changechkbox(this);"/></td>';
		html += '<td>' + unitCode +'<input type="hidden" id="unitCodeArr"  value="'+ unitCode + '"/></td>';
		html += '<td>' + barCode + '<input type="hidden" id="barCodeArr" value="'+  barCode + '"/></td>';
		html += '<td>' + nameTotal + '</td>';
		html += '<td style="text-align:right;" >'+ price + '</td>';
		html += '<td style="text-align:right;"></td>';
		html += '<td class="alignRight"><input value="' + quantity +'" name="quantityArr" id="quantityArr" style="width:120px;text-align:right;" size="10" maxlength="9" onchange="binOLWSMNG04_02.changeCount(this)"/></td>';
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
	 * 输入了调拨数量
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
		$amount.text(binOLWSMNG04_02.formateMoney(amount, 2));
		
		binOLWSMNG04_02.calcTotal();
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
		$("#totalQuantity").html(binOLWSMNG04_02.formateMoney(totalQuantity,0));
		$("#totalAmount").html(binOLWSMNG04_02.formateMoney(totalAmount,2));
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
		binOLWSMNG04_02.clearActionMsg();
		if (!binOLWSMNG04_02.checkData()) {
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
		binOLWSMNG04_02.clearActionMsg();
		if (!binOLWSMNG04_02.checkData()) {
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
	},
	
	/**
	 * 提交前对数据进行检查
	 * 
	 * @returns {Boolean}
	 */
	"checkData":function() {
		binOLWSMNG04_02.changeOddEvenColor();
		var operateType = $("#operateType").val();
		if(operateType == "newBill"){
			var tradeEmployeeID = $("#tradeEmployeeID").val();
			if(tradeEmployeeID == ""){
				$("#errorSpan2").html($("#errmsg_EST00039").val());
				$("#errorDiv2").show();
				return false;
			}
		}
		if ($('#inOrganizationID').val() == null || $('#inOrganizationID').val() == "") {
			$('#errorDiv2 #errorSpan2').html($('#errmsg_EST00013').val());
			$('#errorDiv2').show();
			return false;
		}
		if ($('#inDepotID').val() == null || $('#inDepotID').val() == "") {
			$('#errorDiv2 #errorSpan2').html($('#errmsg_EST00036').val());
			$('#errorDiv2').show();
			return false;
		}
		if($('#outOrganizationID').val()==null || $('#outOrganizationID').val()==""){
			$('#errorDiv2 #errorSpan2').html($('#errmsg_EST00037').val());
			$('#errorDiv2').show();
			return false;
		}
		var flag = true;
		var count = 0;
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
		binOLWSMNG04_02.clearActionMsg();
		var $checkboxs = $("#databody").find(":checkbox:checked");
		$checkboxs.parent().parent().remove();
		binOLWSMNG04_02.changeOddEvenColor();
		$('#allSelect').prop("checked", false);
		binOLWSMNG04_02.calcTotal();
		var rowsCount = $("#databody").children().length;
		if(rowsCount == 0){
			binOLWSMNG04_02.addNewLine();
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
			$('#'+info.elementId).parent().parent().find("[name='prtVendorId']").val(info.prtVendorId);
			$('#'+info.elementId).parent().parent().find("[name='productVendorIDArr']").val(info.prtVendorId);
			$('#'+info.elementId).parent().parent().find("[name='referencePriceArr']").val(price);
			$('#'+info.elementId).parent().parent().find("#unitCodeArr").val(info.unitCode);
			$('#'+info.elementId).parent().parent().find("#barCodeArr").val(info.barCode);
			
			//设置显示值
			$('#'+info.elementId).parent().parent().find("#spanUnitCode").html(info.unitCode);
			$('#'+info.elementId).parent().parent().find("#spanBarCode").html(info.barCode);
			$('#'+info.elementId).parent().parent().find("#spanProductName").html(info.productName);
			$('#'+info.elementId).parent().parent().find("#spanPrice").html(binOLWSMNG04_02.formateMoney(price,2));
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
			binOLWSMNG04_02.changeCount($quantityInputText);
			
			$quantityInputText.select();
			
			//查询库存
			binOLWSMNG04_02.getPrtStock(info.prtVendorId);
		}
	},
	
	/**
	 * 添加新行
	 */
	"addNewLine":function(){
		binOLWSMNG04_02.clearActionMsg();
		
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
			html += '<td class="center"><input type="checkbox" id="chkbox" onclick="binOLWSMNG04_02.changechkbox(this);"/></td>';
			html += '<td><span id="spanUnitCode"></span><input type="text" id="unitCodeBinding_'+nextIndex+'" name="unitCodeBinding"  value=""/><input type="hidden" id="unitCodeArr"></td>';
			html += '<td><span id="spanBarCode"></span><input type="hidden" id="barCodeArr"></td>';
			html += '<td><span id="spanProductName"></span></td>';
			html += '<td style="text-align:right;"><span id="spanPrice"/></span></td>';
			html += '<td id="nowCount" style="text-align:right;"></td>';
			html += '<td class="alignRight"><input id="quantityArr" name="quantityArr" value="" cssClass="text-number" style="width:120px;text-align:right;" size="10" maxlength="4" onchange="binOLWSMNG04_02.changeCount(this)" onkeyup="binOLWSMNG04_02.changeCount(this)"  cssStyle="width:98%"/></td>';
			html += '<td id="money" class="center" style="text-align:right;">0.00</td>';
			html += '<td><input  id="commentsArr" name="commentsArr" value="" type="text"maxlength="200" style="width:96%;"/></td>';
			html +='<td style="display:none">'
	        +'<input type="hidden" name="prtVendorId" value=""/>'
	        +'<input type="hidden" id="productVendorIDArr" name="productVendorIDArr" value=""/>'
			+'<input type="hidden" id="priceUnitArr" name="priceUnitArr" value=""/></td></tr>';
			$("#databody").append(html);
	
			var unitCode = "unitCodeBinding_"+nextIndex;
			binOLWSMNG04_02.setProductBinding(unitCode);
	
			$("#unitCodeBinding_"+nextIndex).focus();
			
			binOLWSMNG04_02.bindInput();
			
			//改变奇偶行的样式
			binOLWSMNG04_02.changeOddEvenColor();
		}
	},

	"setProductBinding":function(id){
		cntProductBinding({counterCode:$("#counterCode").val(),elementId:id,showNum:20,targetDetail:true,afterSelectFun:binOLWSMNG04_02.pbAfterSelectFun});
	},

	"bindInput":function(){
		var tableName = "mainTable";
		$("#"+tableName+" #databody tr").each(function(i) {
			var trID = $(this).attr("id");
			var trParam = "#"+tableName +" #"+trID;
			binOLWSMNG04_02.bindInputTR(trParam);
		})
	},

	"bindInputTR":function(trParam){
		//按Enter键到下一个文本框，备注按Enter键到下一行或新增行。
		var $inp = $(trParam+" input:text");
		$inp.bind('keydown', function (e) {
			if($("#currentMenuID").val() == "BINOLWSMNG04_02"){
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
								binOLWSMNG04_02.addNewLine();
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
		binOLWSMNG04_02_global.needUnlock=false;
		var tokenVal = $('#csrftoken',window.opener.document).val();
		$('#productAllocationDetailUrl').find("input[name='csrftoken']").val(tokenVal);
		$('#productAllocationDetailUrl').submit();
	}
};
var binOLWSMNG04_02 = new BINOLWSMNG04_02();

$(document).ready(function() {
	if (window.opener) {
		window.opener.lockParentWindow();
	}
	var operateType = $("#operateType").val();
	if(operateType == "newBill"){
		binOLWSMNG04_02.addNewLine();
	}else if(operateType == "2"){
		binOLWSMNG04_02.bindInput();
	}
} );
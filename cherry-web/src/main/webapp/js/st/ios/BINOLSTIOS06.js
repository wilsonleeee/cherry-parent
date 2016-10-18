function BINOLSTIOS06(){};

BINOLSTIOS06.prototype={
	/**
	 * 调入部门弹出框
	 * @return
	 */
	"openInDepartBox":function(thisObj) {
		BINOLSTIOS06.clearActionMsg();
		BINOLSTIOS06.changeOddEvenColor();
		// 取得所有部门类型
		var departType = "";
		for ( var i = 0; i < $("#departTypePop option").length; i++) {
			var departTypeValue = $("#departTypePop option:eq(" + i + ")").val();
			// 排除4柜台
			if (departTypeValue != "4") {
				departType += "&departType=" + departTypeValue;
			}
		}
		var param = "checkType=radio&privilegeFlg=1&businessType=1" + departType;
		var callback = function() {
			var $selected = $('#departDataTable').find(':input[checked]').parents("tr");
			if ($selected.length > 0) {
				var departId = $selected.find("input@[name='organizationId']").val();
				var departCode = $selected.find("td:eq(1)").text();
				var departName = $selected.find("td:eq(2)").text();
				$("#inOrganizationID").val(departId);
				$("#inOrgName").text("(" + departCode + ")" + departName);
				BINOLSTIOS06.chooseDepart();
			}
		};
		popDataTableOfDepart(thisObj, param, callback);
	},

	/**
	 * 调出部门弹出框
	 * @return
	 */
	"openOutDepartBox":function(thisObj) {
		BINOLSTIOS06.clearActionMsg();
		if ($('#inOrganizationID').val() == null || $('#inOrganizationID').val() == "") {
			$('#errorDiv2 #errorSpan2').html($('#errmsgEST00013').val());
			$('#errorDiv2').show();
			return false;
		}
		if ($('#inDepotID').val() == null || $('#inDepotID').val() == "") {
			$('#errorDiv2 #errorSpan2').html($('#errmsgEST00006').val());
			$('#errorDiv2').show();
			return false;
		}
		BINOLSTIOS06.changeOddEvenColor();
		// 取得所有部门类型
		var departType = "";
		for ( var i = 0; i < $("#departTypePop option").length; i++) {
			var departTypeValue = $("#departTypePop option:eq(" + i + ")").val();
			// 排除4柜台
			if (departTypeValue != "4") {
				departType += "&departType=" + departTypeValue;
			}
		}
		var param = "checkType=radio&businessType=1&testTypeFlg=1&levelFlg=0&organizationId=" + $("#inOrganizationID").val() + departType;
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
	 * 更改了部门
	 * @param thisObj
	 */
	"chooseDepart":function(thisObj){
		BINOLSTIOS06.clearActionMsg();
		var organizationID = $('#inOrganizationID').val();
		var param ="organizationid="+organizationID;
		
		//更改了部门后，取得该部门所拥有的仓库
		var url = $('#s_getDepot').html()+"?csrftoken="+$('#csrftoken').val();	
		var callback = function(msg){
			BINOLSTIOS06.changeDepotDropDownList(msg);
		};
		cherryAjaxRequest({
			url:url,
			param:param,
			callback:callback
		});
		BINOLSTIOS06.clearDetailData(true);
		$("#outOrganizationID").val("");
		$("#outOrgName").html("&nbsp");
	},
	
	/**
	 * 修改仓库下拉框
	 * @param data
	 * @return
	 */
	"changeDepotDropDownList":function(data){
		//data为json格式的文本字符串	
		var member = eval("("+data+")");//包数据解析为json 格式  
		$("#inDepotID").children().each(function(i){
			$(this).remove();
		});
		$.each(member, function(i){
			$("#inDepotID").append("<option value='"+ member[i].BIN_DepotInfoID+"'>"+escapeHTMLHandle(member[i].DepotCodeName)+"</option>"); 
		});	
		if($("#inDepotId option").length>0){
			$("#inDepotId").get(0).selectedIndex = 0;
			if($("#inOrganizationID").val() != null && $("#inOrganizationID").val() !=""){
				$("#inDepotID").attr("disabled",false);
				$("#inLogicDepotID").attr("disabled",false);
				BINOLSTSFH01.clearActionMsg();
			}
		}
	},
	
	"clearDetailData":function(flag){
		$.each($('#databody >tr'), function(i){
			$(this).remove();
		});	
		$("#allSelect").prop("checked",false);
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
		
		var nextIndex = parseInt($("#rowNumber").val())+1;
		$("#rowNumber").val(nextIndex);
		var html = '<tr id="tr_'+nextIndex+'">';
		html += '<td class="center"><input type="checkbox" id="chkbox" onclick="BINOLSTIOS06.changechkbox(this);"/></td>';
		html += '<td id="dataTd1">' + unitCode +'<input type="hidden" id="unitCodeArr"  value="'+ unitCode + '"/></td>';
		html += '<td id="dataTd2">' + barCode + '<input type="hidden" id="barCodeArr" value="'+  barCode + '"/></td>';
		html += '<td id="dataTd3">' + nameTotal + '</td>';
		html += '<td id="dataTd4" style="text-align:right;" >'+ price + '</td>';
		html += '<td id="dataTd5" style="text-align:right;"></td>';
		html += '<td id="dataTd6" class="center"><input value="' + quantity +'" name="quantityArr" id="quantityArr" cssClass="text-number" style="width:120px;text-align:right;" size="10" maxlength="9" onchange="BINOLSTIOS06.changeCount(this)"  cssStyle="width:98%"/></td>';
		html += '<td id="dataTd7" class="center" style="text-align:right;">' + amount + '</td>';
		html += '<td id="dataTd8" class="center"><input value="' + reason +'" name="commentsArr"  type="text" id="commentsArr" size="25" maxlength="200"  cssStyle="width:98%"/></td>';
		html +='<td id="dataTd9" style="display:none">'
			+'<input type="hidden" name="prtVendorId" value="'+ productVendorId + '"/>'
			+'<input type="hidden" id="productVendorIDArr" name="productVendorIDArr" value="'+productVendorId+'"/>'
			+'<input type="hidden" id="priceUnitArr" name="priceUnitArr" value="'+ price +'"/></td></tr>';
		return html;
	},
	
	/**
	 * AJAX取得产品当前库存量
	 * @param obj:当前产品对象，无此参数则查询页面上的所有产品的库存
	 * 
	 * */
	"getPrtStock": function(obj){
		var url = $("#s_getStockCount").html();
		var lenTR = $("#databody").find("tr");
		var params = getSerializeToken();
		params += "&" + $("#inDepotID").serialize();
		params += "&" + $("#inLogicDepotID").serialize();
		if(obj) {
			params += "&" + $(obj).find("#productVendorIDArr").serialize();
		} else {
			params += "&" + $("#databody").find("#productVendorIDArr").serialize();
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
	
	/**
	 * 产品弹出框
	 * 
	 * */
	"openProPopup":function(){
		var that = this;
		BINOLSTIOS06.clearActionMsg();
		if($('#inOrganizationID').val()==null || $('#inOrganizationID').val()==""){
			$('#errorDiv2 #errorSpan2').html($('#noOutDepart').val());
			$('#errorDiv2').show();
			return false;
		}
		if($("#inDepotID option:selected").val()==""){
			$('#errorDiv2 #errorSpan2').html($('#noOutDepot').val());
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
				BINOLSTIOS06.changeOddEvenColor();
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
				BINOLSTIOS06.bindInput();
				//重新绑定下拉框
    			$("#databody tr [name='unitCodeBinding']").each(function(){
    				var unitCode = $(this).attr("id");
    				BINOLSTIOS06.setProductBinding(unitCode);
    			});
			}
		};
		// 调用产品弹出框共通
		popAjaxPrtDialog(option);
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
		$amount.text(amount);
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
		BINOLSTIOS06.clearActionMsg();
		if (!BINOLSTIOS06.checkData()) {
			return;
		}
		cherryAjaxRequest({
			url : $("#s_saveURL").html(),
			param : $('#mainForm').formSerialize(),
			callback : BINOLSTIOS06.submitSuccess
		});
	},

	/**
	 * 点击申请按钮
	 */
	"submitSend":function() {
		BINOLSTIOS06.clearActionMsg();
		if (!BINOLSTIOS06.checkData()) {
			return;
		}
		cherryAjaxRequest({
			url : $("#s_submitURL").html(),
			param : $('#mainForm').formSerialize(),
			callback : BINOLSTIOS06.submitSuccess
		});
	},

	/**
	 * 提交前对数据进行检查
	 * 
	 * @returns {Boolean}
	 */
	"checkData":function() {
		BINOLSTIOS06.changeOddEvenColor();
		if ($('#inOrganizationID').val() == null || $('#inOrganizationID').val() == "" || $("#outOrganizationID").val() == "" || $("#outOrganizationID").val() == null) {
			$('#errorDiv2 #errorSpan2').html($('#errmsgEST00013').val());
			$('#errorDiv2').show();
			return false;
		}
		if ($('#inDepotID').val() == null || $('#inDepotID').val() == "") {
			$('#errorDiv2 #errorSpan2').html($('#errmsgEST00006').val());
			$('#errorDiv2').show();
			return false;
		}
		if ($('#inLogicDepotID').val() == null || $('#inLogicDepotID').val() == "") {
			$('#errorDiv2 #errorSpan2').html($('#errmsgEST00025').val());
			$('#errorDiv2').show();
			return false;
		}
		var flag = true;
		var count = 0;
		// 检查有无空行
		$.each($('#databody >tr'), function(i) {
			count += 1;
			var $this = $(this);
			var quantityArr = $this.find(":input[name='quantityArr']").val();
			var productVendorIDArr = $this.find("#productVendorIDArr").val();
			
			if (quantityArr == "" || quantityArr == "0" || productVendorIDArr == "") {
				flag = false;
				$(this).attr("class", "errTRbgColor");
			} else {
				$(this).removeClass('errTRbgColor');
			}
		});

		if (!flag) {
			// 有空行存在
			$('#errorDiv2 #errorSpan2').html($('#errmsg1').val());
			$('#errorDiv2').show();
			return flag;
		}
		if (count == 0) {
			// 请输入明细行数据
			flag = false;
			$('#errorDiv2 #errorSpan2').html($('#errmsg2').val());
			$('#errorDiv2').show();
			return flag;
		}
		return flag;
	},

	/**
	 * 提交成功的回调函数
	 * 
	 * @param msg
	 */
	"submitSuccess":function(msg) {
		submitflag = true;
		if (msg.indexOf("actionMessage") > -1) {
			$("#databody > tr").remove();
			$("#outOrganizationID").val("");
			$("#outOrgName").html("");
		}
	},
	
	/**
	 * 删除选中行
	 */
	"deleterow":function() {
		BINOLSTIOS06.clearActionMsg();
		var $checkboxs = $("#databody").find(":checkbox:checked");
		$checkboxs.parent().parent().remove();
		BINOLSTIOS06.changeOddEvenColor();
		$('#allSelect').prop("checked", false);
	},
	
	/**
	 * 添加新行
	 */
	"addNewLine":function(){
		
		BINOLSTIOS06.clearActionMsg();
		if($('#inOrganizationID').val()==null || $('#inOrganizationID').val()==""){
			$('#errorDiv2 #errorSpan2').html($('#noOutDepart').val());
			$('#errorDiv2').show();
			return false;
		}
		if($("#inDepotID option:selected").val()==""){
			$('#errorDiv2 #errorSpan2').html($('#noOutDepot').val());
			$('#errorDiv2').show();
			return false;
		}
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
			//新增一行产品详细
			var html = '<tr id="tr_'+nextIndex+'">';
			html += '<td class="center"><input type="checkbox" id="chkbox" onclick="BINOLSTIOS06.changechkbox(this);"/></td>';
			//UnitCode
			html += '<td id="dataTd1"><span id="spanUnitCode"></span><input type="text" id="unitCodeBinding_'+nextIndex+'" name="unitCodeBinding"  value=""/></td>';
			//BarCode
			html += '<td id="dataTd2"><span id="spanBarCode"></span></td>';
			//名称
			html += ('<td id="dataTd3"><span id="spanProductName"></span></td>');
			html += '<td id="dataTd4" style="text-align:right;" ></td>';
			html += '<td id="dataTd5" style="text-align:right;"></td>';
			html += '<td id="dataTd6" class="center"><input value="" name="quantityArr" id="quantityArr" cssClass="text-number" style="width:120px;text-align:right;" size="10" maxlength="9" onchange="BINOLSTIOS06.changeCount(this)"  cssStyle="width:98%"/></td>';
			html += '<td id="dataTd7" class="center" style="text-align:right;"></td>';
			html += '<td id="dataTd8" class="center"><input value="" name="commentsArr"  type="text" id="commentsArr" size="25" maxlength="200"  cssStyle="width:98%"/></td>';
			html +='<td id="dataTd9" style="display:none">'
				+'<input type="hidden" name="prtVendorId" value=""/>'
				+'<input type="hidden" id="productVendorIDArr" name="productVendorIDArr" value=""/>'
				+'<input type="hidden" id="priceUnitArr" name="priceUnitArr" value=""/></td></tr>';
			
			$("#databody").append(html);
			
			var unitCode = "unitCodeBinding_"+nextIndex;
			BINOLSTIOS06.setProductBinding(unitCode);
			
			$("#unitCodeBinding_"+nextIndex).focus();
			
			BINOLSTIOS06.bindInput();
		}
	},
	
	"setProductBinding":function(id){
		productBinding({elementId:id,showNum:20,targetDetail:true,afterSelectFun:BINOLSTIOS06.pbAfterSelectFun});
	},
	
	/**
	 * 产品下拉输入框选中后执行方法
	 */
	"pbAfterSelectFun":function(info){
		BINOLSTIOS06.clearActionMsg();
		$('#'+info.elementId).val(info.unitCode);
		
		//验证产品厂商ID是否重复
		var flag = true;
		$.each($('#databody >tr'), function(i){
			if($(this).find("#productVendorIDArr").val() == info.prtVendorId){
				flag = false;
				$("#errorSpan2").html($("#errmsg_EST00035").val());
				$("#errorDiv2").show();
				return;
			}
		});
		if(flag){
			$("#errorDiv2").hide();
			// 下拉框共通中的销售价格为price,而弹出框中的销售价格为salePrice
			var price = info.standardCost;//结算价格
//			var sysConfigUsePrice = $("#sysConfigUsePrice").val();
//			if(sysConfigUsePrice == "MemPrice"){
//				price = info.memPrice;//会员价格
//			} else if("StandardCost" == sysConfigUsePrice) {
//				price = info.standardCost;//结算价格
//			}
			if(isEmpty(price)){
				price = parseFloat("0.0");
			}else{
				price = parseFloat(price);
			}
			price = price.toFixed(2);
			var $dataTd = $('#'+info.elementId).parent().parent();
			//设置隐藏值
			$dataTd.find("[name='prtVendorId']").val(info.prtVendorId);
			$dataTd.find("[name='productVendorIDArr']").val(info.prtVendorId);
			$dataTd.find("#priceUnitArr").val(price);
			
			//设置显示值
			$dataTd.find("#spanUnitCode").html(info.unitCode);
			$dataTd.find("#spanBarCode").html(info.barCode);
			$dataTd.find("#spanProductName").html(info.productName);
			
			$dataTd.find("#dataTd4").html(price);
			
			//取消该文本框的自动完成并隐藏。
			$('#'+info.elementId).unautocomplete();
			$('#'+info.elementId).hide();
			
			//查询库存
			BINOLSTIOS06.getPrtStock($dataTd);
			
			//跳到数量文本框
			var $quantityInputText =  $dataTd.find("#quantityArr");
			//选中一个产品后后默认数量1
			var quantity = $quantityInputText.val();
			if(quantity == ""){
				$quantityInputText.val("1");
			}
			//计算盘差（防止先输入数量后输入产品）
			BINOLSTIOS06.changeCount($quantityInputText);
			// 获取焦点
			$quantityInputText.select();
		}
	},
	
	
	/**
	 * 绑定输入框
	 */
	"bindInput":function(){
		var tableName = "mainTable";
		$("#"+tableName+" #databody tr").each(function(i) {
			var trID = $(this).attr("id");
			var trParam = "#"+tableName +" #"+trID;
			BINOLSTIOS06.bindInputTR(trParam);
		});
	},

	"bindInputTR":function(trParam){
		//按Enter键到下一个文本框，当前行最后一个文本框按Enter键到下一行或新增行。
		var $inp = $(trParam+" input:text");
		$inp.bind('keydown', function (e) {
			if($("#currentMenuID").val() == "BINOLSTIOS06"){
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
						if($(this).attr("name") == "unitCodeBinding"){
							//手动输入产品关键词，按Enter键选择产品后，跳到批次或者数量文本框
							if($("#BatchStockTakingTrue").prop("checked") == true){
								// 跳到批次文本框
								var $batchNoArrInputText =  $dataTd.find("#batchNoArr");
								$batchNoArrInputText.select();
							} else {
								var $quantityInputText =  $('#'+trParam).find("#quantityArr");
								$quantityInputText.select();
							}
						}else{
							var nxtIdx = $inp.index(this) + 1;
							var $nextInputText = $(trParam+" input:text:eq("+nxtIdx+")");
							if($nextInputText.length>0){
								//跳到下一个文本框
								$nextInputText.focus();
							}else{
								//跳到下一行第一个文本框，如果没有下一行，新增一行
								if($(trParam).next().length==0){
									BINOLSTIOS06.addNewLine();
								}else{
									$(trParam).next().find("input:text:visible:eq(0)").focus();
								}
							}
						}
					}
				}
			}
		});
	}
};

var BINOLSTIOS06 = new BINOLSTIOS06();

$(function(){
	
});
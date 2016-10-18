var binOLSTBIL14_global = {};
//是否需要解锁
binOLSTBIL14_global.needUnlock = true;

//工作流单据间跳转是否解锁
var OS_BILL_Jump_needUnlock = true;

window.onbeforeunload = function(){
	if(OS_BILL_Jump_needUnlock){
		if(binOLSTBIL14_global.needUnlock){
			if (window.opener) {
				window.opener.unlockParentWindow();
			}
		}
	}
};

$(document).ready(function() {
	$('.tabs').tabs();
	initDetailTable();
	if (window.opener) {
		window.opener.lockParentWindow();
	}
	$("#exportButton").prop("disabled",false);
} );

var BINOLSTBIL14 = function () {};
BINOLSTBIL14.prototype = {
	//产品弹出框
	"openProPopup":function(_this){
		// 产品弹出框属性设置
		var option = {
			targetId: "databody",//目标区ID
			checkType : "checkbox",// 选择框类型
			mode : 2, // 模式
			brandInfoId : $("#BIN_BrandInfoID").val(),// 品牌ID
			getHtmlFun:function(info){// 目标区追加数据行function
				var rowNumber = Math.abs($('#rowNumber').val())+1;
				$('#rowNumber').val(rowNumber);
				var index = $('#rowNumber').val();
				var html = '<tr id="dataRow'+index+'"><td id="dataTd0"><input id="chkbox" type="checkbox" value="'+index+'" onclick="BINOLSTBIL14.changechkbox(this)"/></td>';
				html += '<td id="dataTd1" style="white-space:nowrap"></td>';
				html += '<td id="dataTd2">'+info.unitCode+'</td>';
				html += '<td id="dataTd3">'+info.barCode+'</td>';
				html += '<td id="dataTd4">'+info.nameTotal+'</td>';
				var price = info.salePrice;//销售价格
				var sysConfigUsePrice = $("#sysConfigUsePrice").val();
				if(sysConfigUsePrice == "MemPrice"){
					price = info.memPrice;// 会员价格
				} else if(sysConfigUsePrice == "StandardCost") {
					price = info.standardCost;// 结算价
				}
				if(isEmpty(price)){
					price = parseFloat("0.0");
				}else{
					price = parseFloat(price);
				}
				html += '<td id="dataTd5" class="alignRight">'+BINOLSTBIL14.formateMoney(price,2)+'</td>';
				if($("#th_stockQuantity").is(":visible")){
					html += '<td id="dataTd6" class="alignRight"></td>';
				}
				html += '<td id="nowCount" class="alignRight"><input name="quantityArr" id="quantityArr" class="text-number" size="10" maxlength="9" onchange="BINOLSTBIL14.changeCount(this);"/></td>';
				html += '<td id="money" style="text-align:right;">0.00</td>';
				html += '<td id="dataTd9"><input type="text" name="reasonArr" size="25" maxlength="200" style="width:98%"/></td>';
				html += '<td style="display:none" id="dataTd10">';
				html += '<input type="hidden" id="priceUnitArr'+index+'" name="priceUnitArr" value="'+price+'"/>';
				html += '<input type="hidden" name="prtVendorId" value="'+info.proId+'"/>';
				html += '</td></tr>';
				return html;
			},
			click:function(){
				// 设置全选状态
				var $checkboxs = $('#databody').find(':checkbox');
				var $unChecked = $('#databody').find(':checkbox:not(":checked")');
				if($unChecked.length == 0 && $checkboxs.length > 0){
					$('#allSelect').prop("checked",true);
				}else{
					$('#allSelect').prop("checked",false);
				}
				//重置序号
				$.each($('#databody >tr'), function(i){	
					var index = $(this).attr("id").substr(7);
					$("#dataRow"+index+" td:eq(1)").html(i+1);
				});
				BINOLSTBIL14.changeOddEvenColor();
				BINOLSTBIL14.calcTotal();
				
				//ajax取得选中产品的库存数量
				var param ='inventoryInfoID='+$('#inventoryInfoID').val()+'&logicInventoryInfoID='+$("#logicInventoryInfoID").val();
				var len = $("#databody tr").length;
				if(len>0){
					for(var i=0;i<len;i++){
						var productVendorID = $($("#databody tr [name=prtVendorId]")[i]).val();
						var currentIndex = $($("#databody tr :checkbox")[i]).val();
						param += "&productVendorId="+productVendorID;
						param += "&currentIndex="+currentIndex;
					}
					cherryAjaxRequest({
						url:$("#s_getStockCount").html(),
						reloadFlg:true,
						param:param,
						callback:BINOLSTBIL14.getStockSuccess
					});
				}
			}
		};
		// 调用产品弹出框共通
		popAjaxPrtDialog(option);
	},
	
	/**
	 * 解析库存JSON
	 * @param msg
	 */
	"getStockSuccess":function(msg){
		var productStock = eval("("+msg+")"); //数据解析为json 格式  	
		$.each(productStock, function(i){
			$('#dataRow'+productStock[i].currentIndex +' > #dataTd6').html(productStock[i].Quantity);
		});
	},
		
	/**
	 * 退库接收方弹出框
	 * @return
	 */
	"openDepartBox":function(thisObj){
	 	//取得所有部门类型
	 	var departType = "";
	 	for(var i=0;i<$("#departTypePop option").length;i++){
	 		var departTypeValue = $("#departTypePop option:eq("+i+")").val();
	 		//排除4柜台
	 		if(departTypeValue != "4"){
	 			departType += "&departType="+departTypeValue;
	 		}
	 	}
		
	 	var param = "checkType=radio&privilegeFlg=1&businessType=1"+departType+"&testType="+$("#inTestType").val();
	 	
	 	var callback = function() {
			var $selected = $('#departDataTable').find(':input[checked]').parents("tr");
			if($selected.length > 0) {
				var departId = $selected.find("input@[name='organizationId']").val();
				var departCode = $selected.find("td:eq(1)").text().escapeHTML();
				var departName = $selected.find("td:eq(2)").text().escapeHTML();
				$("#organizationIDReceive").val(departId);
				$("#inOrgName").text("("+departCode.unEscapeHTML()+")"+departName.unEscapeHTML());
				
				BINOLSTBIL14.chooseDepart();
			}
		};
		popDataTableOfDepart(thisObj,param,callback);
	},
	
	/**
	 * 更改了部门
	 * @param thisObj
	 */
	"chooseDepart":function(thisObj){	
		BINOLSTBIL14.clearActionMsg();
		var organizationid = $('#organizationIDReceive').val();
		var param ="organizationid="+organizationid;
		
		//更改了部门后，取得该部门所拥有的仓库
		var csrftoken = $('#csrftoken').val();
		if(csrftoken == undefined){
			csrftoken = parentTokenVal();
		}
		var url = $('#urlgetdepotAjax').html()+"?csrftoken="+csrftoken;
		ajaxRequest(url,param,this.changeDepotDropDownList);
	},
	
	/**
	 * 修改仓库下拉框
	 * @param data
	 * @return
	 */
	"changeDepotDropDownList":function(data){	
		//data为json格式的文本字符串	
		var member = eval("("+data+")");    //包数据解析为json 格式  
		$("#inDepotInfoId").find("option").remove();
		$.each(member, function(i){
			$("#inDepotInfoId ").append("<option value='"+ member[i].BIN_DepotInfoID+"'>"+escapeHTMLHandle(member[i].DepotCodeName)+"</option>"); 
		});
		if($("#inDepotInfoId option").length>1){
			$("#inDepotInfoId").get(0).selectedIndex=0;
		}
		BINOLSTBIL14.setHideInventoryValue();
	},
	
	/**
	 * 设置隐藏的仓库ID
	 */
	"setHideInventoryValue":function(){
		$("#inventoryInfoIDReceive").val($("#inDepotInfoId").val());
		$("#logicInventoryInfoIDReceive").val($("#inLogicDepotsInfoId").val());
	},
	
	/**
	 * 删除掉画面头部的提示信息框
	 * @return
	 */
	"clearActionMsg":function(){
		$('#actionResultDisplay').html("");
		$('#errorDiv2').attr("style",'display:none');
	},
	
	/**
	 * 全选，全不选
	 */
	"selectAll":function(){
		if($('#allSelect').prop("checked")){
			$("#databody :checkbox").each(function(){
				if($(this).val()!=0){
					$(this).prop("checked",true);
					}
				});
			}
		else{
			$("#databody :checkbox").each(function(){
				$(this).prop("checked",false);});
		}
	},
	
	/**
	 * 点击了选择框后，来控制全选框
	 * @return
	 */
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
	 * 删除选中行
	 * @return
	 */
	"deleteRow":function(){
		this.clearActionMsg();
		$("#databody :checkbox").each(function(){
			if($(this).prop("checked")){
				$('#dataRow' + $(this).val()).remove();	
			}
		});
		//重置序号
		$.each($('#databody >tr'), function(i){	
			var index = $(this).attr("id").substr(7);
			$("#dataRow"+index+" td:eq(1)").html(i+1);
		});	
		this.changeOddEvenColor();
		$('#allSelect').prop("checked",false);
		BINOLSTBIL14.calcTotal();
	},
	
	"changeOddEvenColor":function(){
		$("#databody tr:odd").attr("class","even");
		$("#databody tr:even").attr("class","odd");
	},
	
	"changeOddEvenColor1":function(){
		$("#databody tr:odd").attr("class",function(){
			if($(this).attr("class")=="errTRbgColor"){
				
			}else{
				$(this).addClass("odd");
			}
			
		});
		$("#databody tr:even").attr("class",function(){
			if($(this).attr("class")=="errTRbgColor"){
				
			}else{
				$(this).addClass("even");
			}
		});
	},
	
	/**
	 * 提交前对数据进行检查
	 * @returns {Boolean}
	 */
	"checkData":function(){
		var flag = true;
		var count= 0;
		//检查有无空行
		$.each($('#databody >tr'), function(i){	
			count +=1;
			if($(this).find("#quantityArr").val()==""){
				flag = false;
				$(this).attr("class","errTRbgColor");
			}else{
				$(this).removeClass('errTRbgColor');
			}
		});	
		this.changeOddEvenColor1();
		if(!flag){
			//有空行存在		
			$('#errorDiv2 #errorSpan2').html($('#errmsg_EST00008').val());
			$('#errorDiv2').show();
			return flag;
		}
		if(count==0){
			flag = false;
			$('#errorDiv2 #errorSpan2').html($('#errmsg_EST00009').val());
			$('#errorDiv2').show();
			return flag;
		}
		return flag;
	},
	
	/**
	 * 输入了退库数量
	 * @param thisObj
	 */
	"changeCount":function(thisObj){
		var $tr_obj = $(thisObj).parent().parent();
		var trID = $tr_obj.attr('id');
		var index = trID.substr(7);
		var count = parseInt($(thisObj).val(),10);
		var price = $("#"+trID).find("[name='priceUnitArr']").val();
		if(isNaN(count)){
			$(thisObj).val("");
			$("#"+trID + " > #money").html("0.00");
			BINOLSTBIL14.calcTotal();
			return;
		}	
		$(thisObj).val(count);
		$("#"+trID + " > #money").html(this.formateMoney(count*price,2));
		
		BINOLSTBIL14.calcTotal();
	},
	
	/*
	 * 保存退库申请单
	 */
	"saveForm":function(){
		this.clearActionMsg();
		if(!this.checkData()){
			return false;
		}
		var params=$('#mainForm').formSerialize();
		var url = $("#saveUrl").attr("href");
		var callback = function(msg){
			if(window.opener.oTableArr[1] != null)window.opener.oTableArr[1].fnDraw();
		};
		cherryAjaxRequest({
			url:url,
			param:params,
			callback:callback
		});
	},
	
	/*
	 * 提交暂存的订单
	 */
	"submitForm":function(){
		this.clearActionMsg();
		if(!this.checkData()){
			return false;
		}
		
		var checkStockFlag = $("#checkStockFlag").val();
		// 为1库存允许为负，为0库存不允许为负
		if(checkStockFlag == "0"){
			if(this.hasWarningData()){
				$("#errorSpan2").html($("#errmsg_EST00044").val());
				$("#errorDiv2").show();
				return false;
			}
		}
		
		var params=$('#mainForm').formSerialize();
		var url = $("#submitUrl").attr("href");
		var callback = function(msg){
			if(window.opener.oTableArr[1] != null)window.opener.oTableArr[1].fnDraw();
		};
		cherryAjaxRequest({
			url:url,
			param:params,
			callback:callback
		});
	},
	
	/**
	 * 提交前对数据进行检查
	 * 检查操作数量是否大于库存
	 * @returns {Boolean}
	 */
	"hasWarningData":function(){
		var flag = false;
		//检查有无空行
		$.each($('#databody >tr'), function(i){	
			if(i>=0){
				if(Number($(this).find("#quantityArr").val())>Number($(this).find("#dataTd6").text())){
					flag = true;
					$(this).attr("class","errTRbgColor");				
				}else{
					$(this).removeClass('errTRbgColor');				
				}
			}
		});	
		return flag;
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
			
			$(this).find("#hideQuantiyArr").addClass("hide");
			$(this).find("#hideReason").addClass("hide");
		});
		$("#btnReturn").css("display","");
		$("#btn-icon-edit-big").css("display","none");
		$("#btnSave").css("display","");
		return "";
	},
	
	"back":function(){
		binOLSTBIL14_global.needUnlock=false;
		var tokenVal = $('#csrftoken',window.opener.document).val();
		$('#proReturnReqDetailUrl').find("input[name='csrftoken']").val(tokenVal);
		$('#proReturnReqDetailUrl').submit();
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
		$("#totalQuantity").html(this.formateMoney(totalQuantity,0));
		$("#totalAmount").html(this.formateMoney(totalAmount,2));
	}
};
var BINOLSTBIL14 = new BINOLSTBIL14();
var binOLSTBIL18_global = {};
//是否需要解锁
binOLSTBIL18_global.needUnlock = true;

//工作流单据间跳转是否解锁
var OS_BILL_Jump_needUnlock = true;

window.onbeforeunload = function(){
	if(OS_BILL_Jump_needUnlock){
		if(binOLSTBIL18_global.needUnlock){
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

var BINOLSTBIL18 = function () {};
BINOLSTBIL18.prototype = {
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
				var html = '<tr id="dataRow'+index+'"><td id="dataTd0"><input id="chkbox" type="checkbox" value="'+index+'" onclick="BINOLSTBIL18.changechkbox(this)"/></td>';
				html += '<td id="dataTd1" style="white-space:nowrap"></td>';
				html += '<td id="dataTd2">'+info.unitCode+'</td>';
				html += '<td id="dataTd3">'+info.barCode+'</td>';
				html += '<td id="dataTd4">'+info.nameTotal+'</td>';
				html += '<td id="dataTd5" class="alignRight">'+BINOLSTBIL18.formateMoney(info.standardCost,2)+'</td>';
				if($("#th_ProductQuantity").is(":visible")){
					html += '<td id="dataTdStock" class="alignRight"></td>';
				}
				var operateType = $("#operateType").val();
				if(operateType != "2"){
					var auditLGFlag = $("#auditLGFlag").val();
					if(operateType == "76" && auditLGFlag == "YES"){
						html += '<td id="dataTdApplyQuantity" class="alignRight"></td>';
					}
					html += '<td id="dataTd6" class="alignRight"></td>';
				}
				html += '<td id="nowCount" class="alignRight"><input name="quantityArr" id="quantityArr" class="text-number" size="10" maxlength="9" onchange="BINOLSTBIL18.changeCount(this);"/></td>';
				html += '<td id="money" style="text-align:right;">0.00</td>';
				html += '<td id="dataTd9"><input type="text" name="commentsArr" size="25" maxlength="200" style="width:98%"/></td>';
				html += '<td style="display:none" id="dataTd10">';
				html += '<input type="hidden" id="priceUnitArr'+index+'" name="priceUnitArr" value="'+info.standardCost+'"/>';
				html += '<input type="hidden" id="productVendorIDArr'+index+'" name="productVendorIDArr" value="'+info.proId+'"/>';
				html += '<input type="hidden" id="productVendorPackageIDArr'+index+'" name="productVendorPackageIDArr" value="0"/>';
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
				BINOLSTBIL18.changeOddEvenColor();
				BINOLSTBIL18.calcTotal();
				
				//ajax取得选中产品的库存数量
				var params = "&inventoryInfoIDOut="+$("#inventoryInfoIDOut").val()+"&logicInventoryInfoIDOut="+$("#logicInventoryInfoIDOut").val();
				var len = $("#databody tr").length;
				if(len>0){
					for(var i=0;i<len;i++){
						var productVendorID = $($("#databody tr [name=prtVendorId]")[i]).val();
						var currentIndex = $($("#databody tr :checkbox")[i]).val();
						params += "&prtVendorId="+productVendorID;
						params += "&currentIndex="+currentIndex;
					}
					cherryAjaxRequest({
						url:$("#s_getStockCount").html(),
						reloadFlg:true,
						param:params,
						callback:BINOLSTBIL18.getStockSuccess
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
		var product = eval("("+msg+")");
		$.each(product, function(i){
			//发货方库存
			$('#dataRow'+product[i].currentIndex +' > #dataTdStock').html(BINOLSTBIL18.formateMoney(product[i].ProductQuantity,0));
		});
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
				$(this).prop("checked",false);}
			);
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
		BINOLSTBIL18.calcTotal();
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
	 * 输入了调拨数量
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
			BINOLSTBIL18.calcTotal();
			return;
		}	
		$(thisObj).val(count);
		$("#"+trID + " > #money").html(this.formateMoney(count*price,2));
		
		BINOLSTBIL18.calcTotal();
	},
	
	/*
	 * 保存调拨申请单
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
	 * 提交单据
	 */
	"submitForm":function(){
		this.clearActionMsg();
		if(!this.checkData()){
			return false;
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
	 * 开放修改单据
	 * @return
	 */
	"modifyForm":function(){
		$("#showToolbar").removeClass("hide");
		$("#showCheckbox").removeClass("hide");
		$("#showDepotCodeNameIn").removeClass("hide");
		$("#showLogicInventoryCodeNameIn").removeClass("hide");
		
		$("#hideDepotCodeNameIn").addClass("hide");
		$("#hideLogicInventoryCodeNameIn").addClass("hide");
			
		$.each($('#databody').find('tr'), function(n){
			$(this).find("#dataTd0").removeClass("hide");
			$(this).find("#dataTd6").removeClass("hide");
			$(this).find("[name='quantityArr']").removeClass("hide");
			$(this).find("[name='commentsArr']").removeClass("hide");
			
			$(this).find("#hideQuantiyArr").addClass("hide");
			$(this).find("#hideReason").addClass("hide");
		});
		$("#btnSave").css("display","");
		$("#btnReturn").css("display","");
		$("#btn-icon-edit-big").css("display","none");
		$("#btn-icon-delete-big").css("display","none");
		return "";
	},
	
	"back":function(){
		binOLSTBIL18_global.needUnlock=false;
		var tokenVal = $('#csrftoken',window.opener.document).val();
		$('#productAllocationDetailUrl').find("input[name='csrftoken']").val(tokenVal);
		$('#productAllocationDetailUrl').submit();
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
	},
	
	//刷新显示库存
	"refreshStockCount":function(){
		this.clearActionMsg();
		
		var stockUrl = $('#s_getStockCount').html();
		var params = "&inventoryInfoIDOut="+$("#inventoryInfoIDOut").val()+"&logicInventoryInfoIDOut="+$("#logicInventoryInfoIDOut").val();
		$.each($('#databody >tr'), function(i){	
			if(i>=0){
				var index = $(this).attr("id").substr(7);
				var prtVendorId = $(this).find("[name=prtVendorId]").val();
				params += "&currentIndex="+index+"&prtVendorId="+prtVendorId;
			}
		});
		var callback = function(msg){
			var product = eval("("+msg+")");
			$.each(product, function(i){
				//发货方库存
				$('#dataRow'+product[i].currentIndex +' > #dataTdStock').html(BINOLSTBIL18.formateMoney(product[i].ProductQuantity,0));
			});
		};
		
		cherryAjaxRequest({
			url:stockUrl,
			reloadFlg:true,
			param:params,
			callback:callback
		});
	}
};
var BINOLSTBIL18 = new BINOLSTBIL18();
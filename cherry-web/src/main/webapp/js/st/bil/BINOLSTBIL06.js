//工作流单据间跳转是否解锁
var OS_BILL_Jump_needUnlock = true;

window.onbeforeunload = function(){
	if(OS_BILL_Jump_needUnlock){
		if (window.opener) {
			window.opener.unlockParentWindow();
		}
	}
};

$(document).ready(function() {
	initDetailTable();
	if (window.opener) {
       window.opener.lockParentWindow();
    }
    $("#exportButton").prop("disabled",false);
    $('.tabs').tabs();
} );

var BINOLSTBIL06 = function () {};
BINOLSTBIL06.prototype = {
//	"STBIL06_dialogBody":"",

	/**
	 * 报损部门弹出框
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
		
	 	var param = "checkType=radio&privilegeFlg=1&businessType=1"+departType;
		var callback = function() {
			var $selected = $('#departDataTable').find(':input[checked]').parents("tr");
			if($selected.length > 0) {
				var departId = $selected.find("input@[name='organizationId']").val();
				var departCode = $selected.find("td:eq(1)").text();
				var departName = $selected.find("td:eq(2)").text();
				$("#outOrganizationID").val(departId);
				$("#outOrgName").text("("+departCode+")"+departName);
				BINOLSTBIL06.chooseDepart();
			}
			
		};
		popDataTableOfDepart(thisObj,param,callback);
	},

	/**
	 * 更改了部门
	 * @param thisObj
	 */
	"chooseDepart":function(thisObj){	
		//this.clearActionMsg();
		var organizationid = $('#outOrganizationID').val();
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
		//$("#outDepotInfoId").find("option:not(:first)").remove();
		$("#outDepotInfoId").find("option").remove();
		$.each(member, function(i){
			$("#outDepotInfoId ").append("<option value='"+ member[i].BIN_DepotInfoID+"'>"+escapeHTMLHandle(member[i].DepotCodeName)+"</option>"); 
		});
		if($("#outDepotInfoId option").length>1){
			$("#outDepotInfoId").get(0).selectedIndex=1;
		}
	},
	
	//刷新显示库存
	"refreshStockCount":function(){
		if($("#outDepotInfoId").val() == null || $("#outDepotInfoId").val() == undefined){
			$('#errorDiv2 #errorSpan2').html($('#errmsg_EST00015').val());
			$('#errorDiv2').show();
			return false;
		}else{
			$('#errorDiv2').hide();
		}
		
		//修改发货仓库、逻辑仓库隐藏项
		$("#depotInfoIdAccept").val($("#outDepotInfoId").val());
		$("#logicDepotsInfoIdAccept").val($("#outLogicDepotsInfoId").val());
		
		var csrftoken = $('#csrftoken').val();
		if(csrftoken == undefined){
			csrftoken = parentTokenVal();
		}
		var stockUrl = $('#s_getStockCount').html()+"?csrftoken="+csrftoken;
		$.each($('#databody >tr'), function(i){	
			var index = $(this).attr("id").substr(7);
			var productVendorId = $(this).find("[name=productVendorIDArr]").val();
			var params = "currentIndex="+index+"&productVendorId="+productVendorId+"&depotInfoId="+$("#outDepotInfoId").val()+"&logicDepotsInfoId="+$("#outLogicDepotsInfoId").val();
				
			//重置详细的发货仓库ID，发货逻辑仓库ID
			$(this).find("[name=inventoryInfoIDAcceptArr]").val($("#outDepotInfoId").val());
			$(this).find("[name=logicInventoryInfoIDAcceptArr]").val($("#outLogicDepotsInfoId").val());
				
			var callback = function(msg){
				var member = eval("("+msg+")");
				if(member.length>0){
					$("#dataRow"+member[0].currentIndex+" td:eq(6)").html(member[0].Quantity);
				}
			};
			cherryAjaxRequest({
				url:stockUrl,
				param:params,
				callback:callback
			});
		});
	},
	
	/**
	 * 产品弹出框
	 * @return
	 */
	"openProPopup":function(_this){
		this.clearActionMsg();
		this.changeOddEvenColor();
//		var proPopParam = {
//				thisObj : _this,
//				  index : 0,
//			  checkType : "checkbox",
//			      modal : true,
//			  autoClose : [],
//			 dialogBody : BINOLSTBIL06.STBIL06_dialogBody,
//			 csrftoken  : "csrftoken="+window.opener.document.getElementById("csrftoken").value
//		};
//		popDataTableOfPrtInfo(proPopParam);
		
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
				var html = '<tr id="dataRow'+index+'"><td id="dataTd0"><input id="chkbox" name="chkbox" type="checkbox" value="'+index+'" onclick="BINOLSTBIL06.changechkbox(this)"/></td>';
				html += '<td id="dataTd1" style="white-space:nowrap"></td>';
				html += '<td id="dataTd2">'+info.unitCode+'</td>';
				html += '<td id="dataTd3">'+info.barCode+'</td>';
				html += '<td id="dataTd4">'+info.nameTotal+'</td>';
				html += '<td id="dataTd5" class="alignRight">'+BINOLSTBIL06.formateMoney(info.salePrice,2)+'</td>';
				html += '<td id="dataTd7" class="alignRight"><input name="quantityArr" id="quantityArr" class="text-number" size="10" maxlength="9" onchange="BINOLSTBIL06.changeCount(this);"/></td>';
				html += '<td id="money" style="text-align:right;">0.00</td>';
				html += '<td id="dataTd9"><input type="text" name="commentsArr" size="25" maxlength="200" style="width:99%"/></td>';
				html += '<td style="display:none" id="dataTd10">';
				html += '<input type="hidden" id="priceUnitArr'+index+'" name="priceUnitArr" value="'+info.salePrice+'"/>';
				html += '<input type="hidden" id="productVendorIDArr'+index+'" name="productVendorIDArr" value="'+info.proId+'"/>';
				html += '<input type="hidden" id="productVendorPackageIDArr'+index+'" name="productVendorPackageIDArr" value="0"/>';
				html += '<input type="hidden" id="inventoryInfoIDArr'+index+'" name="inventoryInfoIDArr" value="'+$("#depotInfoID").val()+'"/>';
				html += '<input type="hidden" id="logicInventoryInfoIDArr'+index+'" name="logicInventoryInfoIDArr" value="'+$("#logicInventoryInfoID").val()+'"/>';
				html += '<input type="hidden" id="storageLocationInfoIDArr'+index+'" name="storageLocationInfoIDArr" value="0"/>';
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
				BINOLSTBIL06.changeOddEvenColor();
			}
		};
		// 调用产品弹出框共通
		popAjaxPrtDialog(option);
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
					$(this).prop("checked",true);
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
	
//	/**
//	 * 新增一行
//	 * @return
//	 */
//	"addRow":function(){
//		this.clearActionMsg();
//		var rowNumber = Math.abs($('#rowNumber').val())+1;
//		var newid = 'dataRow'+rowNumber;
//		$('#dataRow0').clone().attr('id',newid).removeAttr("style").appendTo("#databody");	
//		$('#'+newid).find(':checkbox').val(rowNumber);
//		$('#'+newid).find('#productVendorIDArr').attr('id','productVendorIDArr'+rowNumber);
//		$('#'+newid).find('#inventoryInfoIDArr').attr('id','inventoryInfoIDArr'+rowNumber);
//		$('#'+newid).find('#logicInventoryInfoIDArr').attr('id','logicInventoryInfoIDArr'+rowNumber);
//		$('#'+newid).find('#storageLocationInfoIDArr').attr('id','storageLocationInfoIDArr'+rowNumber);
//		$('#'+newid).find('#priceUnitArr').attr('id','priceUnitArr'+rowNumber);
//		$('#'+newid).find('#inventoryInfoIDAcceptArr').attr('id','inventoryInfoIDAcceptArr'+rowNumber);
//		$('#rowNumber').val(rowNumber);
//		
//		$('#'+newid).find('#inventoryInfoIDArr'+rowNumber).val($("#inventoryInfoID").val());
//		$('#'+newid).find('#logicInventoryInfoIDArr'+rowNumber).val($("#logicInventoryInfoID").val());
//		$('#'+newid).find('#inventoryInfoIDAcceptArr'+rowNumber).val($("#outDepotInfoId").val());
//		$('#'+newid).find('#logicInventoryInfoIDAcceptArr'+rowNumber).val($("#outLogicDepotsInfoId").val());
//
//	},
	
	/**
	 * 删除选中行
	 * @return
	 */
	"deleteRow":function(){
		this.clearActionMsg();
		$("#databody :checkbox").each(function(){
			if($(this).prop("checked"))
				{
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
	},
	
	"changeOddEvenColor":function(){
		$("#databody tr:odd").attr("class","even");
		$("#databody tr:even").attr("class","odd");
	},
	
	"changeOddEvenColor1":function(){
		$("#databody tr:odd").attr("class",function(){
			if($(this).attr("class")=="errTRbgColor"){
				
			}else{
				$(this).addClass("even");
			}
			
		});
		$("#databody tr:even").attr("class",function(){
			if($(this).attr("class")=="errTRbgColor"){
				
			}else{
				$(this).addClass("odd");
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
			if($(this).find("#quantityArr").val()==""||$(this).find("#quantityArr").val()=="0"){
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
	 * 输入了订货数量
	 * @param thisObj
	 */
	"changeCount":function(thisObj){
		var $tr_obj = $(thisObj).parent().parent();
		var trID = $tr_obj.attr('id');
		var index = trID.substr(7);
		var count = Math.abs(parseInt($(thisObj).val(),10));
		var price = $("#"+trID + " > #dataTd5").html();
		if(isNaN(count)){
			$(thisObj).val("");
			$("#"+trID + " > #money").html("");
			return;
		}	
		$(thisObj).val(count);
		$("#"+trID + " > #money").html((count*price).toFixed(2));
	},
	
	/*
	 * 保存订单
	 */
	"saveOrder":function(){
		this.clearActionMsg();
		if(!this.checkData()){
			return false;
		}
		var params=$('#mainForm').formSerialize();
		var url = $("#saveUrl").attr("href");
		var callback = function(msg){
			if(oTableArr[1] != null)oTableArr[1].fnDraw();
		};
		cherryAjaxRequest({
			url:url,
			param:params,
			callback:callback
		});
	},
	
	/*
	 * 提交订单
	 */
	"submitOrder":function(){
		this.clearActionMsg();
		if(!this.checkData()){
			return false;
		}
		var params=$('#mainForm').formSerialize();
		var url = $("#submitUrl").attr("href");
		var callback = function(msg){
			if(oTableArr[1] != null)oTableArr[1].fnDraw();
		};
		cherryAjaxRequest({
			url:url,
			param:params,
			callback:callback
		});
	},
	
	/*
	 * 删除订单
	 */
	"deleteOrder":function(){
		this.clearActionMsg();
		var params=$('#mainForm').formSerialize();
		var url = $("#deleteUrl").attr("href");
		var callback = function(msg){
			if(oTableArr[1] != null)oTableArr[1].fnDraw();
		};
		cherryAjaxRequest({
			url:url,
			param:params,
			callback:callback
		});
	},
	
	/**
	 * 解析库存JSON
	 * @param msg
	 */
	"getStockSuccess":function(msg){
		var member = eval("("+msg+")"); //数据解析为json 格式  	
		$.each(member, function(i){
			$('#hasproductflagArr'+member[i].currentIndex).val(member[i].hasproductflag);
			$('#dataRow'+member[i].currentIndex +' > #dataTd6').html(member[i].Quantity);
		});
	},
	
	/**
	 * 刷新仓库ID、逻辑仓库ID
	 * @return
	 */
	"refreshDetailID":function(){
		var depotInfoID = $("#depotInfoID").val();
		var logicInventoryInfoID = $("#logicInventoryInfoID").val();
		$.each($('#databody >tr'), function(i){	
			$(this).find("[name=inventoryInfoIDArr]").val(depotInfoID);
			$(this).find("[name=logicInventoryInfoIDArr]").val(logicInventoryInfoID);
		});
	}
};
var BINOLSTBIL06 = new BINOLSTBIL06();

$(function(){
//	// 产品popup初始化
//	BINOLSTBIL06.STBIL06_dialogBody = $('#productDialog').html();
});

//function selectProduct () {
//	var selectedArr = $("#dataTableBody").find(":input[checked]");
//	
//	var length = selectedArr.length;
//	for(var i=0;i<length;i++){
//		BINOLSTBIL06.addRow();
//		var $selectedProduct = $(selectedArr[i]);
//		var selectedObj = window.JSON2.parse($selectedProduct.val()); 
//		var index = $('#rowNumber').val();
//		// 产品厂商编号
//		var unitCode = selectedObj.unitCode;
//		// 条码
//		var barCode = selectedObj.barCode;
//		// 厂商ID
//		var productVendorID = selectedObj.productVendorId;
//		
//		var trID = 'dataRow'+index;
//		//检查重复行
//		var sameFlag =false;
//		$.each($('#databody').find('tr'), function(n){
//			if($(this).find("input@[name='unitCodeArr']").val() == unitCode && $(this).find("input@[name='barCodeArr']").val() == barCode){
//				$(this).attr("class","errTRbgColor");
//				$('#errorSpan2').html($('#errmsg_EST00007').val());
//				$('#errorDiv2').show();
//				sameFlag =true;
//				//删除新增行
//				$('#databody').find('tr:last').remove();
//				return false;
//			}
//		});
//		
//		if(sameFlag){
//			continue;
//		}
//		$('#productVendorIDArr' + index).val(productVendorID);
//		
//		//序号
//		var curNo = $("#databody tr").length-1;
//		$('#'+trID).find('#dataTd1').html(curNo);
//
//		// 厂商编码
//		$('#'+trID).find('#dataTd2').html(unitCode);
//		
//		// 条码
//		$('#'+trID).find('#dataTd3').html(barCode);
//		
//		// 产品名称
//		$('#'+trID).find('#dataTd4').html(selectedObj.nameTotal);
//		
//		//价格
//		var price;
//		if(!selectedObj.salePrice){
//			price = "0.00";
//		}else{
//			price = selectedObj.salePrice;
//		}
//		$('#'+trID).find('#dataTd5').html(price);
//		$('#'+trID).find('#dataTd10').find('#priceUnitArr'+index).val(price);
//	}
//	closeCherryDialog('productDialog',BINOLSTBIL06.STBIL06_dialogBody);	
//	oTableArr[0]= null;	
//}
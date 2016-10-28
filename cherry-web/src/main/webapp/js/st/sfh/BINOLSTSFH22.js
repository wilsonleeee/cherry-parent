var BINOLSTSFH22 = function () {};

BINOLSTSFH22.prototype = {
	
	"STIOS01_dialogBody":"",
		
	/**
	  * 删除掉画面头部的提示信息框
	  * @return
      */
	"clearActionMsg":function(){
		$('#actionResultDisplay').html("");
		$('#errorDiv2').attr("style",'display:none');
		$('#errorMessage1').html("");
		$('#successMessage1').html("");
	},

	/**
	 * 入库部门弹出框
	 * @return
	 */
	"openDepartBox":function(thisObj){	 	
	 	//取得所有部门类型
	 	var departType = "";
	 	var counterInDepotFlag = $("#counterInDepotFlag").val();
	 	for(var i=0;i<$("#departTypePop option").length;i++){
	 		var departTypeValue = $("#departTypePop option:eq("+i+")").val();
	 		if(counterInDepotFlag == "1"){
	 			departType += "&departType="+departTypeValue;
	 		}else{
		 		//系统配置项不允许后台给柜台入库则排除4柜台
		 		if(departTypeValue != "4"){
		 			departType += "&departType="+departTypeValue;
		 		}
	 		}
	 	}
		
	 	//业务类型=5入库
	 	var param = "checkType=radio&privilegeFlg=1&businessType=5"+departType;
	 	param += "&valid=1";
		var callback = function() {
			var $selected = $('#departDataTable').find(':input[checked]').parents("tr");
			if($selected.length > 0) {
				var departId = $selected.find("input@[name='organizationId']").val();
				var departCode = $selected.find("td:eq(1)").text();
				var departName = $selected.find("td:eq(2)").text();
				$("#inOrganizationId").val(departId);
				$("#inOrgName").text("("+departCode+")"+departName);
				BINOLSTSFH22.chooseDepart();
			}
		};
		popDataTableOfDepart(thisObj,param,callback);
	},
	
	/**
	 * 更改了部门
	 * @param thisObj
	 */
	"chooseDepart":function(thisObj){	
		this.clearActionMsg();
		var organizationid = $('#inOrganizationId').val();
		var param ="organizationid="+organizationid;
		
		//更改了部门后，取得该部门所拥有的仓库
		var url = $('#urlgetdepotAjax').html()+"?csrftoken="+$('#csrftoken').val();	
		ajaxRequest(url,param,this.changeDepotDropDownList);
		//清空明细
		this.clearDetailData();
	},
	
	/**
	 * 修改仓库下拉框
	 * @param data
	 * @return
	 */
	"changeDepotDropDownList":function(data){	
		//data为json格式的文本字符串	
		var member = eval("("+data+")");    //包数据解析为json 格式  
		$("#depotInfoId").find("option:not(:first)").remove();
		$.each(member, function(i){
			$("#depotInfoId ").append("<option value='"+ member[i].BIN_DepotInfoID+"'>"+escapeHTMLHandle(member[i].DepotCodeName)+"</option>"); 
		});
		if($("#depotInfoId option").length>1){
			$("#depotInfoId").get(0).selectedIndex=1;
			if($("#inOrganizationId").val() != null && $("#inOrganizationId").val() !=""){
				$("#depotInfoId").attr("disabled",false);
				$("#logicDepotsInfoId").attr("disabled",false);
				BINOLSTSFH22.clearActionMsg();
			}
		}else{
			$("#depotInfoId").attr("disabled",true);
			$("#logicDepotsInfoId").attr("disabled",true);
			$('#errorDiv2 #errorSpan2').html($('#errmsg_EST00015').val());
			$('#errorDiv2').show();
		}
		BINOLSTSFH22.getLogicDepotInfo();
	},
	
	//取得对应的逻辑仓库
	"getLogicDepotInfo":function(){
		var url = $("#getLogicInfo").html();
		var param = "inOrganizationId="+$("#inOrganizationId").val();
		var callback = function(msg){
			if(msg == '[]'){
				$("#logicDepotsInfoId").html("");
			}else{
				var obj = eval('(' + msg + ')');
				var html = "";
				for(var index in obj){
					var logicInventoryInfoID = obj[index].BIN_LogicInventoryInfoID;
					var logicInventoryCodeName = obj[index].LogicInventoryCodeName;
					html=html+'<option value="'+logicInventoryInfoID+'">'+logicInventoryCodeName+'</option>';
				}
				$("#logicDepotsInfoId").html(html);
			}
		};
		cherryAjaxRequest({
			url:url,
			param:param,
			callback:callback
		});
	},
	
	"checkSelect":function(){
		if($('#inOrganizationId').val()==null || $('#inOrganizationId').val()==""){
			$('#errorDiv2 #errorSpan2').html($('#errmsg_EST00013').val());
			$('#errorDiv2').show();
			return false;
		}
		if($('#depotInfoId').val()==null || $('#depotInfoId').val()==""){
			$('#errorDiv2 #errorSpan2').html($('#errmsg_EST00006').val());
			$('#errorDiv2').show();
			return false;
		}
		return true;
	},
	/**
	 * 目标区追加数据行
	 * */
	"getHtmlFun":function(info,negativeFlag){
		var productVendorId=info.productVendorId;//产品厂商Id
		var unitCode = info.unitCode;//厂商编码
		var barCode = info.barCode;//产品条码
		var nameTotal = info.nameTotal;//产品名称
		var price = info.distributionPrice;//标准配送价
		var stockAmount = info.stockAmount;//标准配送价
		if(isEmpty(price)){
			price = parseFloat("0.0");
		}else{
			price = parseFloat(price);
		}
		
		price = price.toFixed(2);
		
//		var discountPrice = price*$("#batchPriceRate").val()/100;
//		discountPrice = discountPrice.toFixed(2);
		
		var quantity=info.quantity;//数量
		if(isEmpty(quantity)){
			quantity = "";
		}
		if(!isEmpty(quantity) && negativeFlag){
			quantity = Number(quantity)*(-1);
		}
		var amount = price * Number(quantity);//金额
		amount = amount.toFixed(2);
		var reason = escapeHTMLHandle(info.reason);//备注
		if(isEmpty(reason)){
			reason = "";
		}
//		var batchNo = escapeHTMLHandle(info.batchNo);
//		if(isEmpty(batchNo)){
//			batchNo = "";
//		}
		var nextIndex = parseInt($("#rowNumber").val())+1;
		$("#rowNumber").val(nextIndex);
		var html = '<tr id="dataRow'+nextIndex+'">';
		html += '<td class="center"><input type="checkbox" id="chkbox" onclick="BINOLSTSFH22.changechkbox(this);"/></td>';
		html += '<td><span id="spanUnitCode">' + unitCode + '</span></td>';
		html += '<td><span id="spanBarCode">'  + barCode + '</span></td>';
		html += '<td><span id="spanNameTotal">' + nameTotal + '</span></td>';
		html += '<td style="text-align:right;">'+ BINOLSTSFH22.formateMoney(price,2) + '</td>';
		html += '<td style="text-align:right;">' +stockAmount + '</td>';
		html += '<span id="spanCalPrice" class="hide"><span class="calculator" onclick="BINOLSTSFH22.initRateDiv(this);" title="'+$("#spanCalTitle").text()+'"></span></span></td>';
		html += '<td class="center"><input value="'+quantity+'" name="quantityArr" id="quantityArr" cssClass="text-number" style="width:120px;text-align:right;" size="10" maxlength="9" onchange="BINOLSTSFH22.calcuAmount(this);"/></td>';
		html += '<td class="center" style="text-align:right;">' + amount + '</td>';
		html += '<td class="center"><input value="'+reason+'" name="reasonArr"  id="reasonArr" size="25"  maxlength="200" cssStyle="width:98%"/></td>';
		html += '<td style="display:none">'
			 +'<input type="hidden" id="prtVendorId" name="prtVendorId" value="'+productVendorId+'"/>'
             +'<input type="hidden" id="productVendorIDArr" name="productVendorIDArr" value="'+productVendorId+'"/>'
             +'<input type="hidden" id="referencePriceArr" name="referencePriceArr" value="'+price+'"/>'
             +'<input type="hidden" id="stockAmount" name="stockAmount" value="'+stockAmount+'"/>'
             +'<input type="hidden" id="unitCode" name="unitCode" value="'+unitCode+'"/>'
 			 +'<input type="hidden" id="priceUnitArr" name="priceUnitArr" value="'+ price +'"/></td></tr>';
		return html;
	},
	/**
	 * 产品弹出框
	 * @return
	 */
	"openProPopup":function (){
		var that = this;
/*		if(!this.checkSelect()){
			return;
		}*/
		// 出库部门
		if($('#outOrganizationId').val()==null || $('#outOrganizationId').val()==""){
			$('#errorDiv2 #errorSpan2').html($('#configOutDepart').val());
			$('#errorDiv2').show();
			return false;
		}
		// 入库部门
		if($('#inOrganizationId').val()==null || $('#inOrganizationId').val()==""){
			$('#errorDiv2 #errorSpan2').html($('#noInDepart').val());
			$('#errorDiv2').show();
			return false;
		}
		// 入库仓库
		if($("#inDepotId option:selected").val()==""){
			$('#errorDiv2 #errorSpan2').html($('#noInDepot').val());
			$('#errorDiv2').show();
			return false;
		}
		this.clearActionMsg();

		// 点击产品选择时，判断是否手工输入过相关数据,未选择数据则删除相关行
		$.each($('#databody >tr'), function(i){
			var productVendorIDArr=$(this).find("#productVendorIDArr").val();
			if(undefined == productVendorIDArr || productVendorIDArr == ''){
                //移除空行数据
				$(this).remove();	
		   }

		});	
        // 产品弹出框属性设置
		var option = {
	 	    targetId: "databody",//目标区ID
	        checkType : "checkbox",// 选择框类型
	        mode : 2, // 模式
	        brandInfoId : $("#brandInfoId").val(),// 品牌ID
		    getHtmlFun:that.getHtmlFun,// 目标区追加数据行function
	     	click:function(){//点击确定按钮之后的处理
				// 改变奇偶行的样式
				BINOLSTSFH22.changeOddEvenColor();
				//计算总金额总数量
				BINOLSTSFH22.calcuTatol();
				// 设置全选状态
	     	    var $checkboxs = $('#databody').find(':checkbox');
	     	    var $unChecked = $('#databody').find(':checkbox:not(":checked")');
	     	    if($unChecked.length == 0 && $checkboxs.length > 0){
	     		   $('#allSelect').prop("checked",true);
	     	    }else{
	     		   $('#allSelect').prop("checked",false);
	     	    }
	     	    
	     	   BINOLSTSFH22.bindInput();
	     	    
			}	
	     };
		// 调用产品弹出框共通
		popAjaxPrtDialogTwo(option);
},


/**
 * 弹出建议订货天数对话框
 */
"openSuggestDay" : function(inOrganizationId){
	
//    var _$this = $(obj);
    var that = this;
	var callback = function() {
		if(oTableArr[0] != null)oTableArr[0].fnDraw();
	};
	
    $("#sugggestDayPop").dialog( {
		resizable : false,
		modal : true,
		title: "订货量设定",
		height : 240,
		width : 340,
		zIndex: 30,  
		buttons: [{
			text: "确认",
		    click: function(){
		    	  var sugggestDay =$("#orderDayNum").val();
		    	  if (isPositiveFloat(Number($.trim($("#orderDayNum").val()))) == false) {// 判断正整数
		  			$('#errorSpan4').html($('#errmsg9').val());
		  			$("#errorDiv4").show();
		  			return false;
		  		}
		    	  that.selectSuggestProduct(sugggestDay,inOrganizationId);
		    	}
		},
		{
			text: "取消",
		    click: function(){
		    		closeCherryDialog('sugggestDayPop',sugggestDayPopBody);
		    	}
		}],
		close: function(){
			closeCherryDialog('sugggestDayPop',sugggestDayPopBody);
		}
	});


	cherryValidate({
		formId: 'sugggestDayForm',	
		rules: {
			sugggestDay: {required: true,maxlength: 20},
		}	
	});
},

// 查询建议订货数据
"selectSuggestProduct" : function(sugggestDay,inOrganizationId){
	var _this = this;
	var url = document.getElementById("btnSelectSuggestPrductURL").innerHTML+ "?orderDayNum=" + sugggestDay+"&inOrganizationId=" + inOrganizationId;
//	var param = "&sugggestDay=" + sugggestDay;
//	param += "&inOrganizationId=" + inOrganizationId;
    var callback = function(msg) {	
    	var _this = this;
     	closeCherryDialog('sugggestDayPop',sugggestDayPopBody);
    	$("#databody").html("");
    	var data = eval('('+msg+')');
    	for(var i=0;i<data.length;i++){
    		
    		var nextIndex = parseInt($("#rowNumber").val())+1;
    		$("#rowNumber").val(nextIndex);
    		var html = '<tr id="dataRow'+nextIndex+'">';
			html += '<td class="center"><input type="checkbox" id="chkbox" onclick="BINOLSTSFH22.changechkbox(this);"/></td>';
			html += '<td><span id="spanUnitCode">'+data[i].unitCode+'</span></td>';
			html += '<td><span id="spanBarCode">'+data[i].barCode+'</span></td>';
			html += '<td><span id="spanNameTotal">'+data[i].nameTotal+'</span></td>';
			html += '<td style="text-align:right;"><span id="spanPrice"/>'+BINOLSTSFH22.formateMoney(data[i].distributionPrice,2) +'</span></td>';
			html += '<td style="text-align:right;"><span id="spanStockAmount"/>'+data[i].stockAmount+'</span></td>';
//			html += '<td style="text-align:right;"><span id="spanCalPrice" class="hide"><span class="calculator" onclick="BINOLSTSFH22.initRateDiv(this);" title="'+$("#spanCalTitle").text()+'"></span></span></td>';
			html += '<td class="center"><input value="'+data[i].sumQuantity+'" name="quantityArr" id="quantityArr" cssClass="text-number" style="width:120px;text-align:right;" size="10" maxlength="9" onchange="BINOLSTSFH22.calcuAmount(this);"/></td>';
			html += '<td id="tdAmount" class="center" style="text-align:right;">'+(data[i].sumQuantity*data[i].distributionPrice).toFixed(2)+'</td>';
			html += '<td class="center"><input value="" name="reasonArr" id="reasonArr" size="25" maxlength="200" cssStyle="width:98%"/></td>';
			html += '<td style="display:none">'
				 +'<input type="hidden" id="prtVendorId" name="prtVendorId" value="'+data[i].BIN_ProductVendorID+'"/>'
	             +'<input type="hidden" id="productVendorIDArr" name="productVendorIDArr" value="'+data[i].BIN_ProductVendorID+'"/>'
	             +'<input type="hidden" id="priceUnitArr" name="priceUnitArr" value="'+data[i].distributionPrice+'"/>'
	             +'<input type="hidden" id="stockAmount" name="stockAmount" value="'+data[i].stockAmount+'"/>'
	             +'<input type="hidden" id="unitCode" name="unitCode" value="'+data[i].unitCode+'"/>'
	             +'<input type="hidden" id="referencePriceArr" name="referencePriceArr" value=""/></td></tr>';
			$("#databody").append(html);
    		
    	}
    	
    	BINOLSTSFH22.calcuTatol();
    	BINOLSTSFH22.bindInput(); 
			//重新绑定下拉框
			$("#databody tr [name='unitCodeBinding']").each(function(){
				var unitCode = $(this).attr("id");
				BINOLSTSFH22.setProductBinding(unitCode);
			});
	};
	cherryAjaxRequest({
		url: url,
//		param: param,
		callback: callback
	});
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
			if(i>=0){
					if($(this).prop("checked")!=true){
						flag=false;
					}	
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
	"deleterow":function(){
		this.clearActionMsg();
		var checked = $("#databody").find(":checked");
		if(checked.length == 0){
			$("#errorMessage1").html($("#EBS00145").html());
			return false;
		}else{
		$("#databody :checkbox").each(function(){
			var $this = $(this);
			if($this.prop("checked")){			
				$this.parent().parent().remove();	
			}					
		});
		}
		this.changeOddEvenColor();
		$('#allSelect').prop("checked",false);
		this.calcuTatol();
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
	
	"clearDetailData":function(){
		$("#databody > tr[id!='dataRow0']").remove();
		$("#bussinessPartnerId").val("");
		$("#bussinessPartner").val("");
		$("#totalQuantity").html("0");
		$("#totalAmount").html("0.00");
		$("#priceRate").val("100.00");
		$("#batchPriceRate").val("100.00");
	},
	
	"chooseDepot":function(){
		this.clearDetailData();
	},
	
	/**
	 * 提交前对数据进行检查
	 * @returns {Boolean}
	 */
	"checkData":function(){
		var flag = true;
		var count= 0;
		BINOLSTSFH22.clearActionMsg();
		//检查有无空行
		$.each($('#databody >tr'), function(i){	
			if(i>=0){
				count +=1;
				if($(this).find("#quantityArr").val()=="" || $(this).find("#quantityArr").val()=="0"){
					flag = false;
					$(this).attr("class","errTRbgColor");				
				}else{
					$(this).removeClass('errTRbgColor');				
				}		
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
	    // 不允许负库存时，校验输入数量必须小于等于库存数，允许库存为负时，不做相关校验
		if($("#isAllowNegativeInventory").val()=="0"){
			//检查订货量是否小于库存数
			$.each($('#databody >tr'), function(i){	
				var a=parseInt($(this).find("#quantityArr").val());
				var b=parseInt($(this).find("#stockAmount").val());
				if (a > b) {
					flag = false;
					$(this).attr("class", "errTRbgColor");
				}

			});	
			if(!flag){
				//有订货量大于库存数的行		
				$('#errorDiv2 #errorSpan2').html($('#errmsg_EST00048').val());
				$('#errorDiv2').show();
			}
		}
		return flag;
	},

	/**
	 * 订货确认
	 * @returns {Boolean}
	 */
	"confirm":function(){
		var that = this;
		$("#actionResultDiv").hide();
		if($("#bussinessPartnerId").val() == "" && $("#bussinessPartner").val() !=""){
			$('#errorDiv2 #errorSpan2').html($('#partnerError').val());
			$('#errorDiv2').show();
			return;
		}
		if(!this.checkSelect()){
			return;
		}
		this.clearActionMsg();
		if(!this.checkData()){
			return;
		}
		var $form = $("#mainForm");
		// 表单验证
		if(!$form.valid()){
			return;
		}
		var param=$("#mainForm").formSerialize();
		var url=$("#urlSubmit").text();
		var callback=function(){
			that.clearDetailData();
		};
		cherryAjaxRequest({
			url:url,
			param:param,
			callback:callback
		});
	},

	/**
	 * 订货暂存
	 * @returns {Boolean}
	 */
	"save":function(){
		if(!BINOLSTSFH22.checkData()){
			return false;
		}
		BINOLSTSFH22.clearActionMsg();
		var url = document.getElementById("saveURL").innerHTML;
		var param = $("#mainForm").serialize();
		var callback = function(msg){
/*			if(msg.indexOf("actionMessage") > -1){
				BINOLSTSFH22.clearPage(true);
			}*/
		};
		cherryAjaxRequest({
			url:url,
			param:param,
			callback:callback
		});
	},
	/**
	 * 保存前对数据进行检查（允许空数量）
	 * @returns {Boolean}
	 */
	"checkSaveData":function(){
		BINOLSTSFH22.clearActionMsg();
		var flag = true;
		if(Number($("#inDepotId option:selected").val()) == 0){
			flag = false;
			$("#errorSpan2").html($("#errmsg4").val());
			$("#errorDiv2").show();
			return;
		}
		if($("#outOrganizationId").val() == ""){
			$('#errorDiv2 #errorSpan2').html($('#noOutDepart').val());
			$('#errorDiv2').show();
			return false;
		}
		BINOLSTSFH22.changeOddEvenColor1();

		return flag;
	},
	
	"btnSendClick":function(){
		if(!BINOLSTSFH22.checkData()){
			return false;
		}
		BINOLSTSFH22.clearActionMsg();
		var url = document.getElementById("submitURL").innerHTML;
		var param = $("#mainForm").serialize();
		var callback = function(msg){
             if(msg.indexOf("fieldErrorDiv")>-1){
            	 
             }else{
             	// 提交成功后，使暂存，提交按钮失效
    			 $("#save").hide(); 
    			 $("#sumbit").hide(); 
    			 $("#btnSendMsm").show(); 
    			 $("#spanBtnadd").hide(); 
    			 $("#spanBtdelete").hide();  
    			 $("#spanBtnaddP").hide();  
    			 $("#suggestProduct").hide();  
    			 $("#deliverAddress").attr("disabled", true); 
    			 $("#reasonAll").attr("disabled", true); 
    			 $("#orderStatus").html("已提交");
    				//提交后订货数量和备注不可修改
    			$.each($('#databody >tr'), function(i){	
    				if(i>=0){
    				 $(this).find("#quantityArr").attr("disabled", true); 
    				 $(this).find("#reasonArr").attr("disabled", true); 
    				}
    			});	
             }
				
		};
		cherryAjaxRequest({
			url:url,
			param:param,
			callback:callback
		});
	},
	
	"btnSendMsm":function(){
		var url = document.getElementById("btnSendMsmURL").innerHTML;
		var param = $("#mainForm").serialize();
		var callback = function(msg){
			var data = eval('('+msg+')');
			if(data.result=="0"){
				$("#btnSendMsm").hide(); 
				$("#orderStatus").html("款已付");
				BINOLSTSFH22.clearActionMsg(true);
				$("#successMessage").show();
				// 显示操作成功
			}else{
				BINOLSTSFH22.clearActionMsg(true);
				$("#btnSendMsm").hide(); 
				// 显示操作失败
				$("#errorMessage").show();
			}

	
		};
		cherryAjaxRequest({
			url:url,
			param:param,
			callback:callback
		});
	},
	
	"calcuAmount":function(obj){
		var $this = $(obj);
		var $thisTd = $this.parent();
		if(isNaN($this.val().toString())){
			$this.val("0");
			$thisTd.next().text("0.00");
		}else{
			var price = Number($this.parent().prev().prev().text());
			var quantity = Math.abs(parseInt(Number($this.val())));
			$this.val(quantity);
			var amount = price * Number(quantity);
			$thisTd.next().text(amount.toFixed(2));
		}
		BINOLSTSFH22.calcuTatol();
	},
	
	"calcuTatol":function(){
		var rows = $("#databody").children();
		var totalQuantity = 0;
		var totalAmount = 0.00;
		if(rows.length > 0){
			rows.each(function(i){
				var $tds = $(this).find("td");
				if(!isEmpty($tds.eq(6).find(":input").val())){
				totalQuantity += Number($tds.eq(6).find(":input").val());
				}
		        totalAmount +=Number($tds.eq(7).text());
			});
		}
		$("#totalQuantity").html(totalQuantity);
		$("#totalAmount").html(totalAmount.toFixed(2));
	},
	
	/**
	 * 取得产品入库单详单
	 * @return
	 */
	"getProInDepotDetail":function(negativeFlag,inDepotID){
		var that = this;
		var callback = function(msg) {
			var jsons = eval(msg);
			for(var i=0;i<jsons.length;i++){
				var html = that.getHtmlFun(jsons[i],negativeFlag);
				$("#databody").append(html);
			}
			//添加了新行，去除全选框的选中状态
			$('#allSelect').prop("checked",false);
			BINOLSTSFH22.changeOddEvenColor();
			//计算总金额总数量
			BINOLSTSFH22.calcuTatol();
			
			BINOLSTSFH22.bindInput();
		};
		//ajax取得选中的发货单详细
		var param ="inDepotID="+inDepotID+"&organizationId="+$("#inOrganizationId").val()+"&depotInfoId="+$("#depotInfoId").val()+"&logicDepotInfoId="+$("#logicDepotsInfoId").val();
		cherryAjaxRequest({
			url:$("#s_getInDepotDetail").html(),
			reloadFlg:true,
			param:param,
			callback:callback
		});
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
	
	 // 格式化折扣价格及重新计算金额
	"setPrice":function(obj){
		var $this = $(obj);
		var $tr_obj = $this.parent().parent();

		// 折扣价
		var $discountPrice = $tr_obj.find("input[name='priceArr']");
		var discountPrice = parseFloat($this.val());

		if(isNaN(discountPrice)){
			discountPrice = 0;
		}
		discountPrice = discountPrice.toFixed("2");
		$discountPrice.val(discountPrice);
		//数量
		var count = $tr_obj.find("#quantityArr").val();
		//画面显示的金额
		$tr_obj.find("#tdAmount").html(BINOLSTSFH22.formateMoney(count*discountPrice,2));
		BINOLSTSFH22.calcuTatol();
	},
	
	// 根据折扣率计算折扣价格（批量/单个）
	"setDiscountPrice":function(obj){
		var curRateIndex = $("#curRateIndex").val();
		var findTRParam = "tr:eq("+curRateIndex+")";
		var $priceRate = $("#priceRate");
		var priceRate = $priceRate.val();
		if(isNaN(priceRate) || priceRate == ""){
			priceRate = 100;
		}
		
		if(curRateIndex == ""){
			//批量计算折扣价格
			$("#batchPriceRate").val(parseFloat(priceRate).toFixed("2"));
			findTRParam = "tr";
		}
		
		$.each($('#databody').find(findTRParam), function(n){
			var $tr_obj = $(this);
			
			//原价
			var $costPrice = $tr_obj.find("input[name='referencePriceArr']");
			var costPrice = parseFloat($costPrice.val());
			if(isNaN(costPrice)){
				costPrice = 0;
			}

			// 折扣价
			var $discountPrice = $tr_obj.find("input[name='priceArr']");
			var discountPrice = parseFloat($discountPrice.val());
			if(isNaN(discountPrice)){
				discountPrice = 0;
			}
			discountPrice = costPrice*priceRate/100;
			$discountPrice.val(discountPrice.toFixed("2"));
			
			//数量
			var count = $tr_obj.find("#quantityArr").val();

			$tr_obj.find("#tdAmount").html(BINOLSTSFH22.formateMoney(count*discountPrice,2));
		});
		
		BINOLSTSFH22.calcuTatol();
	},
	
	// 关闭折扣率弹出框
	"closeDialog":function(_this){
		$("#curRateIndex").val("");
		var $rateDiv = $(_this).parent();
		$rateDiv.hide();
	},
	
	/**
	 * 产品下拉输入框选中后执行方法
	 */
	"pbAfterSelectFun":function(info){
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
			
			var price = info.price;//销售价格
			var sysConfigUsePrice = $("#sysConfigUsePrice").val();
			if(sysConfigUsePrice == "MemPrice"){
				price = info.memPrice;//会员价格
			} else if("StandardCost" == sysConfigUsePrice) {
				price = info.standardCost;//结算价格
			}
			
			//设置隐藏值
			$('#'+info.elementId).parent().parent().find("[name='prtVendorId']").val(info.prtVendorId);
			$('#'+info.elementId).parent().parent().find("[name='productVendorIDArr']").val(info.prtVendorId);
			
			//设置显示值
			$('#'+info.elementId).parent().parent().find("#spanUnitCode").html(info.unitCode);
			$('#'+info.elementId).parent().parent().find("#spanBarCode").html(info.barCode);
			$('#'+info.elementId).parent().parent().find("#spanProductName").html(info.productName);
			$('#'+info.elementId).parent().parent().find("#spanPrice").html(info.distributionPrice);
			$('#'+info.elementId).parent().parent().find("#spanStockAmount").html(info.stockAmount);
			var discountPrice = parseFloat(price)*$("#batchPriceRate").val()/100;
			discountPrice = discountPrice.toFixed(2);
			$('#'+info.elementId).parent().parent().find("#priceArr").val(discountPrice);
			$('#'+info.elementId).parent().parent().find("#referencePriceArr").val(price);
			$('#'+info.elementId).parent().parent().find("#priceUnitArr").val(info.distributionPrice);
			$('#'+info.elementId).parent().parent().find("#unitCode").val(info.unitCode);
			$('#'+info.elementId).parent().parent().find("#stockAmount").val(info.stockAmount);
			//取消该文本框的自动完成并隐藏。
			$('#'+info.elementId).unautocomplete();
			$('#'+info.elementId).hide();
						
			//跳到数量文本框
			//var nxtIdx = $('#'+info.elementId).parent().parent().find("input:text").index($('#'+info.elementId)) + 1;
			var $quantityInputText =  $('#'+info.elementId).parent().parent().find("#quantityArr");

			//选中一个产品后后默认数量1
			var quantity = $('#'+info.elementId).parent().parent().find("#quantityArr").val();
			if(quantity == ""){
				$('#'+info.elementId).parent().parent().find("#quantityArr").val("1");
			}
			//计算金额（防止先输入数量后输入产品）
			BINOLSTSFH22.calcuAmount($quantityInputText);
			
			$quantityInputText.select();
		}
	},
	
	//弹出商品图片模式
	"popAdd":function() {	
		var url=$("#add_url").html();
		url+="?"+getSerializeToken();
		
		var question = new Array();	
		$("#databody").find("tr").each(function(index) {
			var prtVendorId = $(this).find("[name='prtVendorId']").val();//产品厂商ID
			var quantityArr = $(this).find("[name='quantityArr']").val();//订货数量					
			var reasonArr = $(this).find("[name='reasonArr']").val();//产品备注
			var stockAmount = $(this).find("[name='stockAmount']").val();
			
			if (!isEmpty(prtVendorId)) {		
				var obj = {"prtVendorId":prtVendorId,"quantityArr":quantityArr,"reasonArr":reasonArr,"amount":stockAmount};
				question.push(obj);
			}					
		});
		
		var queStr = JSON2.stringify(question);
		url+="&queStr="+queStr;	
		
		popup(url);
	},
	
	/**
	 * 添加新行
	 */
	"addNewLine":function(){
		BINOLSTSFH22.clearActionMsg();
		if(Number($("#inDepotId option:selected").val()) == 0){
			flag = false;
			$("#errorSpan2").html($("#errmsg4").val());
			$("#errorDiv2").show();
			return;
		}
		if($("#outOrganizationId").val() == ""){
			$('#errorDiv2 #errorSpan2').html($('#configOutDepart').val());
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
			
			var html = '<tr id="dataRow'+nextIndex+'">';
			html += '<td class="center"><input type="checkbox" id="chkbox" onclick="BINOLSTSFH22.changechkbox(this);"/></td>';
			html += '<td><span id="spanUnitCode"></span><input type="text" id="unitCodeBinding_'+nextIndex+'" name="unitCodeBinding"  value=""/></td>';
			html += '<td><span id="spanBarCode"></span></td>';
			html += '<td><span id="spanProductName"></span></td>';
			html += '<td style="text-align:right;"><span id="spanPrice"/></span></td>';
			html += '<td style="text-align:right;"><span id="spanStockAmount"/></span></td>';
			html += '<span id="spanCalPrice" class="hide"><span class="calculator" onclick="BINOLSTSFH22.initRateDiv(this);" title="'+$("#spanCalTitle").text()+'"></span></span></td>';
			html += '<td class="center"><input value="" name="quantityArr" id="quantityArr" cssClass="text-number" style="width:120px;text-align:right;" size="10" maxlength="9" onchange="BINOLSTSFH22.calcuAmount(this);"/></td>';
			html += '<td id="tdAmount" class="center" style="text-align:right;">0.00</td>';
			html += '<td class="center"><input value="" name="reasonArr" id="reasonArr" size="25" maxlength="200" cssStyle="width:98%"/></td>';
			html += '<td style="display:none">'
				 +'<input type="hidden" id="prtVendorId" name="prtVendorId" value=""/>'
	             +'<input type="hidden" id="productVendorIDArr" name="productVendorIDArr" value=""/>'
	             +'<input type="hidden" id="priceUnitArr" name="priceUnitArr" value=""/>'
	             +'<input type="hidden" id="unitCode" name="unitCode" value=""/>'
	             +'<input type="hidden" id="stockAmount" name="stockAmount" value=""/>'
	             +'<input type="hidden" id="referencePriceArr" name="referencePriceArr" value=""/></td></tr>';
			$("#databody").append(html);
			
			var unitCode = "unitCodeBinding_"+nextIndex;
			BINOLSTSFH22.setProductBinding(unitCode);
			
			$("#unitCodeBinding_"+nextIndex).focus();
			
			BINOLSTSFH22.bindInput();
		}
	},

	"setProductBinding":function(id){
		nzdmProductBinding({elementId:id,showNum:20,targetDetail:true,afterSelectFun:BINOLSTSFH22.pbAfterSelectFun});
//		productBinding({elementId:id,showNum:20,targetDetail:true,afterSelectFun:BINOLSTSFH22.pbAfterSelectFun});
	},

	"bindInput":function(){
		var tableName = "mainTable";
		$("#"+tableName+" #databody tr").each(function(i) {
			var trID = $(this).attr("id");
			var trParam = "#"+tableName +" #"+trID;
			BINOLSTSFH22.bindInputTR(trParam);
		});
	},

	"bindInputTR":function(trParam){
		//按Enter键到下一个文本框，当前行最后一个文本框按Enter键到下一行或新增行。
		var $inp = $(trParam+" input:text");
		$inp.bind('keydown', function (e) {
			if($("#currentMenuID").val() == "BINOLSTSFH22" || $("#currentMenuID").val() == "BINOLSTSFH23_02"){
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
							//手动输入产品关键词，按Enter键选择产品后，跳到数量文本框
							var $quantityInputText =  $('#'+trParam).find("#quantityArr");
							$quantityInputText.select();
						}else{
							var nxtIdx = $inp.index(this) + 1;
							var $nextInputText = $(trParam+" input:text:eq("+nxtIdx+")");
							if($nextInputText.length>0){
								//跳到下一个文本框
								$nextInputText.focus();
								
							}else{
								//跳到下一行第一个文本框，如果没有下一行，新增一行
								if($(trParam).next().length==0){
									BINOLSTSFH22.addNewLine();
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

var BINOLSTSFH22 = new BINOLSTSFH22();
/**
 * 正整数判断
 * @param s
 * @returns {Boolean}
 */
function isPositiveFloat(s){
	var patrn=/^[0-9]*[1-9][0-9]*$/; 
	if (!patrn.exec(s)) return false;
	return true;
}


$(document).ready(function(){
	// 产品popup初始化
	if($("#inOrganizationId").val() != null && $("#inOrganizationId").val() !=""){
		$("#inDepotId").attr("disabled",false);
		$("#inLogicDepotId").attr("disabled",false);
	}
	// 页面初始化时，款已付按钮不可用
	 $("#btnSendMsm").hide(); 
//	// 日期控件
	$("#expectDeliverDate").cherryDate({
	    minDate : new Date()
	});
	
	// 日期校验和发货地址校验
	cherryValidate({
		formId: "mainForm",		
		rules: {
	        deliverAddress: {required: true,maxlength: 200},
			expectDeliverDate:{required: true}
	   }		
	});
	
	sugggestDayPopBody = $('#sugggestDayPop').html();
	BINOLSTSFH22.addNewLine();
});

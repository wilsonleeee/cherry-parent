var BINOLSTIOS12 = function () {};

BINOLSTIOS12.prototype = {
	
	/**
	  * 删除掉画面头部的提示信息框
	  * @return
      */
	"clearActionMsg":function(){
		$('#actionResultDisplay').html("");
		$('#errorDiv2').attr("style",'display:none');
	},

	/**
	 * 入库部门弹出框，此本程序【不提供选择入库部门功能】，固定只能给大仓做入库
	 * @return
	 */
	"openDepartBox":function(thisObj){	 	
	 	//取得所有部门类型
	 	var departType = "";
	 	// 品牌总部
	 	departType += "&departType=1";
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
				BINOLSTIOS12.chooseDepart();
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
				BINOLSTIOS12.clearActionMsg();
			}
		}else{
			$("#depotInfoId").attr("disabled",true);
			$("#logicDepotsInfoId").attr("disabled",true);
			$('#errorDiv2 #errorSpan2').html($('#errmsg_EST00015').val());
			$('#errorDiv2').show();
		}
		BINOLSTIOS12.getLogicDepotInfo();
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
		if($("#inOrganizationId").val() != "" && $("#depotInfoId option").length<=1){
			return false;
		}
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
		var price = info.orderPrice;// 采购价格
		if(isEmpty(price)){
			price = parseFloat("0.0");
		}else{
			price = parseFloat(price);
		}
		price = price.toFixed(2);
		
		var discountPrice = price*$("#batchPriceRate").val()/100;
		discountPrice = discountPrice.toFixed(2);
		
		var quantity=info.quantity;//数量
		if(isEmpty(quantity)){
			quantity = "";
		}
		if(!isEmpty(quantity) && negativeFlag){
			quantity = Number(quantity)*(-1);
		}
		var amount = discountPrice * Number(quantity);//金额
		amount = amount.toFixed(2);
		var reason = escapeHTMLHandle(info.reason);//备注
		if(isEmpty(reason)){
			reason = "";
		}
		var batchNo = escapeHTMLHandle(info.batchNo);
		if(isEmpty(batchNo)){
			batchNo = "";
		}
		var nextIndex = parseInt($("#rowNumber").val())+1;
		$("#rowNumber").val(nextIndex);
		var html = '<tr id="dataRow'+nextIndex+'">';
			html += '<td class="center"><input type="checkbox" id="chkbox" onclick="BINOLSTIOS12.changechkbox(this);"/></td>';
			html += '<td>' + unitCode + '</td>';
			html += '<td>' + barCode + '</td>';
			html += '<td>' + nameTotal + '</td>';
//			html += '<td><input name="batchNoArr" value="'+batchNo+'" id="batchNoArr" style="text-align:right;" cssClass="text-number" maxlength="20" cssStyle="width:98%"/></td>';
			html += '<td style="text-align:right;">' +BINOLSTIOS12.formateMoney(price,2) + '</td>';
			html += '<td style="text-align:right;"><input type="text" class="price" id="priceArr" name="priceArr" maxlength="17" value="'+ discountPrice + '" onchange="BINOLSTIOS12.setPrice(this);"/>';
			html += '<span id="spanCalPrice" class="hide"><span class="calculator" onclick="BINOLSTIOS12.initRateDiv(this);" title="'+$("#spanCalTitle").text()+'"></span></span></td>';
			html += '<td style="text-align:right;"><input value="'+quantity+'" name="quantityuArr"style="text-align:right;" id="quantityuArr" cssClass="text-number" size="10" maxlength="9" onchange="BINOLSTIOS12.calcuAmount(this);" cssStyle="width:98%"/></td>';
			html += '<td id="tdAmount" class="center" style="text-align:right;">' + BINOLSTIOS12.formateMoney(amount,2) + '</td>';
			html += '<td class="center"><input value="'+reason+'" name="reasonArr" size="25" maxlength="200" cssStyle="width:98%"/></td>';
			html += '<td style="display:none">'
				 +'<input type="hidden" name="prtVendorId" value="'+ productVendorId + '"/>'
	             +'<input type="hidden" id="productVendorIDArr" name="productVendorIDArr" value="'+productVendorId+'"/>'
	             +'<input type="hidden" id="referencePriceArr" name="referencePriceArr" value="'+price+'"/></td></tr>';
		return html;
	},
	/**
	 * 产品弹出框
	 * @return
	 */
	"openProPopup":function (){
		var that = this;
		if(!this.checkSelect()){
			return;
		}
		this.clearActionMsg();
        // 产品弹出框属性设置
		var option = {
	 	    targetId : "databody",//目标区ID
	        checkType : "checkbox",// 选择框类型
	        mode : 2, // 模式
	        showOrderPrice : 1, // 是否显示采购价格
	        brandInfoId : $("#brandInfoId").val(),// 品牌ID
		    getHtmlFun : that.getHtmlFun,// 目标区追加数据行function
	     	click : function(){//点击确定按钮之后的处理
				// 改变奇偶行的样式
				BINOLSTIOS12.changeOddEvenColor();
				//计算总金额总数量
				BINOLSTIOS12.calcuTatol();
				// 设置全选状态
	     	    var $checkboxs = $('#databody').find(':checkbox');
	     	    var $unChecked = $('#databody').find(':checkbox:not(":checked")');
	     	    if($unChecked.length == 0 && $checkboxs.length > 0){
	     		   $('#allSelect').prop("checked",true);
	     	    }else{
	     		   $('#allSelect').prop("checked",false);
	     	    }
	     	    
	     	   BINOLSTIOS12.bindInput();
	     	    
	     	   //重新绑定下拉框
	     	   $("#databody tr [name='unitCodeBinding']").each(function(){
	     		   var unitCode = $(this).attr("id");
	     		   BINOLSTIOS12.setProductBinding(unitCode);
	     	   });
			}	
	     };
	    // 调用产品弹出框共通
		popAjaxPrtDialog(option);
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
	"deleteRow":function(){
		this.clearActionMsg();
		var checked = $("#databody").find(":checked");
		if(checked.length == 0){
			return false;
		} else {
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
		//检查有无空行
		$.each($('#databody >tr'), function(i){	
			if(i>=0){
				count +=1;
				if($(this).find("#quantityuArr").val()=="" || $(this).find("#quantityuArr").val()=="0"){
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
		return flag;
	},

	/**
	 * 入库确认
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
	 * 入库暂存
	 * @returns {Boolean}
	 */
	"save":function(){
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
		var url=$("#urlSave").text();
		var callback=function(msg){
			that.clearDetailData();
			$("#actionResultDisplay").html(msg);
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
			$thisTd.parent().find("#tdAmount").text("0.00");
		}else{
			var price = Number($thisTd.parent().find("#priceArr").val());
			$this.val(parseInt(Number($this.val())));
			var amount = price * Number($this.val());
			//$thisTd.next().text(amount.toFixed(2));
			$thisTd.parent().find("#tdAmount").text(BINOLSTIOS12.formateMoney(amount,2));
		}
		BINOLSTIOS12.calcuTatol();
	},
	
	"calcuTatol":function(){
		var rows = $("#databody").children();
		var totalQuantity = 0;
		var totalAmount = 0.00;
		if(rows.length > 0){
			rows.each(function(i){
				var $tds = $(this).find("td");
				var $inputVal=$tds.find("#quantityuArr").val();
				if(isNaN($inputVal)){
					$inputVal=0;
				}
				totalQuantity += Number($inputVal);
				totalAmount += Number($inputVal) * parseFloat($tds.find("#priceArr").val());
			});
		}
		$("#totalQuantity").html(BINOLSTIOS12.formateMoney(totalQuantity,0));
		$("#totalAmount").html(BINOLSTIOS12.formateMoney(totalAmount,2));
	},
	
	/**
	 * 复制入库单
	 * @return
	 */
	"copyInDepot":function(){
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
		this.clearActionMsg();
		var callback = function() {
			var $selected = $('#inDepotDataTable').find(':input[checked]').parents("tr");
			//原单数量取反
			var negativeFlag= $('#checkboxType').is(":checked");
			if($selected.length>0){
				var inDepotID = $selected.find("[name=inDepotID]").val();
				//清空原先入库单
				BINOLSTIOS12.clearDetailData();
				BINOLSTIOS12.getProInDepotDetail(negativeFlag,inDepotID);
			}
		};
		var param ="organizationId="+$('#inOrganizationId').val()+"&depotInfoId="+$('#depotInfoId').val();
		popDataTableOfProInDepot(null,param,callback);
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
			BINOLSTIOS12.changeOddEvenColor();
			//计算总金额总数量
			BINOLSTIOS12.calcuTatol();
			
			BINOLSTIOS12.bindInput();
		};
		//ajax取得选中的发货单详细
		var param ="inDepotID="+inDepotID+"&organizationId="+$("#inOrganizationId").val()+"&depotInfoId="+$("#depotInfoId").val()+"&logicDepotInfoId="+$("#logicDepotsInfoId").val();
		cherryAjaxRequest({
			url : $("#s_getInDepotDetail").html(),
			reloadFlg : true,
			param : param,
			callback : callback
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
		var count = $tr_obj.find("#quantityuArr").val();
		//画面显示的金额
		$tr_obj.find("#tdAmount").html(BINOLSTIOS12.formateMoney(count*discountPrice,2));
		BINOLSTIOS12.calcuTatol();
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
			var count = $tr_obj.find("#quantityuArr").val();

			$tr_obj.find("#tdAmount").html(BINOLSTIOS12.formateMoney(count*discountPrice,2));
		});
		
		BINOLSTIOS12.calcuTatol();
	},
	
	// 初始化折扣率弹出框 
	"initRateDiv":function(_this,type){
		var $this = $(_this);
		// 折扣率
		var $priceRate = $("#priceRate");
		var $rateDiv = $priceRate.parent();
		
		if(type == "batch"){
			//批量处理折扣价格
			$("#curRateIndex").val("");
			var batchPriceRate = $("#batchPriceRate").val();
			if(batchPriceRate == ""){
				batchPriceRate = "100";
			}
			$priceRate.val(parseFloat(batchPriceRate).toFixed("2"));
		}else{
			//折扣率弹出框的行数
			var $tr_obj = $this.parent().parent().parent();
			var curRateIndex = $("#databody tr").index($tr_obj);
			$("#curRateIndex").val(curRateIndex);
			
			// 原价
			var $costPrice = $tr_obj.find("[name='referencePriceArr']");
			// 折扣价格
			var $discountPrice = $tr_obj.find("[name='priceArr']");
			var costPrice = parseFloat($costPrice.val()).toFixed("2");
			var discountPrice = parseFloat($discountPrice.val()).toFixed("2");
			if(isNaN(costPrice)){costPrice = 0;}
			if(isNaN(discountPrice)){discountPrice = 0;}
			if(costPrice!=0){
				var priceRate = discountPrice*100/costPrice;
				$priceRate.val(priceRate.toFixed("2"));
			}
		}
		
		// 弹出折扣率设置框
		$rateDiv.show();
		//$priceRate.focus();
		$priceRate.trigger("select");
		// 设置弹出框位置
		var offset = $this.offset();
		$rateDiv.offset({"top":offset.top-$rateDiv.height()-2,"left":offset.left});
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
			var price = info.orderPrice;// 采购价格
			
			//设置隐藏值
			$('#'+info.elementId).parent().parent().find("[name='prtVendorId']").val(info.prtVendorId);
			$('#'+info.elementId).parent().parent().find("[name='productVendorIDArr']").val(info.prtVendorId);
			
			//设置显示值
			$('#'+info.elementId).parent().parent().find("#spanUnitCode").html(info.unitCode);
			$('#'+info.elementId).parent().parent().find("#spanBarCode").html(info.barCode);
			$('#'+info.elementId).parent().parent().find("#spanProductName").html(info.productName);
			$('#'+info.elementId).parent().parent().find("#spanPrice").html(BINOLSTIOS12.formateMoney(price,2));
			var discountPrice = parseFloat(price)*$("#batchPriceRate").val()/100;
			discountPrice = discountPrice.toFixed(2);
			$('#'+info.elementId).parent().parent().find("#priceArr").val(discountPrice);
			$('#'+info.elementId).parent().parent().find("#referencePriceArr").val(price);
			
			//取消该文本框的自动完成并隐藏。
			$('#'+info.elementId).unautocomplete();
			$('#'+info.elementId).hide();
			
			//跳到数量文本框
			//var nxtIdx = $('#'+info.elementId).parent().parent().find("input:text").index($('#'+info.elementId)) + 1;
			var $quantityInputText =  $('#'+info.elementId).parent().parent().find("#quantityuArr");

			//选中一个产品后后默认数量1
			var quantity = $('#'+info.elementId).parent().parent().find("#quantityuArr").val();
			if(quantity == ""){
				$('#'+info.elementId).parent().parent().find("#quantityuArr").val("1");
			}
			//计算金额（防止先输入数量后输入产品）
			BINOLSTIOS12.calcuAmount($quantityInputText);
			
			$quantityInputText.select();
		}
	},
	
	/**
	 * 添加新行
	 */
	"addNewLine":function(){
		BINOLSTIOS12.clearActionMsg();
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
			html += '<td class="center"><input type="checkbox" id="chkbox" onclick="BINOLSTIOS12.changechkbox(this);"/></td>';
			html += '<td><span id="spanUnitCode"></span><input type="text" id="unitCodeBinding_'+nextIndex+'" name="unitCodeBinding"  value=""/></td>';
			html += '<td><span id="spanBarCode"></span></td>';
			html += '<td><span id="spanProductName"></span></td>';
//			html += '<td><input name="batchNoArr" value="" id="batchNoArr" style="text-align:right;" cssClass="text-number" maxlength="20" cssStyle="width:98%"/></td>';
			html += '<td style="text-align:right;"><span id="spanPrice"/></span></td>';
			html += '<td style="text-align:right;"><input type="text" class="price" id="priceArr" name="priceArr" maxlength="17" value="" onchange="BINOLSTIOS12.setPrice(this);"/>';
			html += '<span id="spanCalPrice" class="hide"><span class="calculator" onclick="BINOLSTIOS12.initRateDiv(this);" title="'+$("#spanCalTitle").text()+'"></span></span></td>';
			html += '<td style="text-align:right;"><input value="" name="quantityuArr"style="text-align:right;" id="quantityuArr" cssClass="text-number" size="10" maxlength="9" onchange="BINOLSTIOS12.calcuAmount(this);" cssStyle="width:98%"/></td>';
			html += '<td id="tdAmount" class="center" style="text-align:right;">0.00</td>';
			html += '<td class="center"><input value="" name="reasonArr" size="25" maxlength="200" cssStyle="width:98%"/></td>';
			html += '<td style="display:none">'
				 +'<input type="hidden" name="prtVendorId" value=""/>'
	             +'<input type="hidden" id="productVendorIDArr" name="productVendorIDArr" value=""/>'
	             +'<input type="hidden" id="referencePriceArr" name="referencePriceArr" value=""/></td></tr>';
			$("#databody").append(html);
			
			var unitCode = "unitCodeBinding_"+nextIndex;
			BINOLSTIOS12.setProductBinding(unitCode);
			
			$("#unitCodeBinding_"+nextIndex).focus();
			
			BINOLSTIOS12.bindInput();
		}
	},

	"setProductBinding":function(id){
		productBinding({elementId:id,showNum:20,targetDetail:true,afterSelectFun:BINOLSTIOS12.pbAfterSelectFun});
	},

	"bindInput":function(){
		var tableName = "mainTable";
		$("#"+tableName+" #databody tr").each(function(i) {
			var trID = $(this).attr("id");
			var trParam = "#"+tableName +" #"+trID;
			BINOLSTIOS12.bindInputTR(trParam);
		});
	},

	"bindInputTR":function(trParam){
		//按Enter键到下一个文本框，当前行最后一个文本框按Enter键到下一行或新增行。
		var $inp = $(trParam+" input:text");
		$inp.bind('keydown', function (e) {
			if($("#currentMenuID").val() == "BINOLSTIOS12"){
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
							var $quantityInputText =  $('#'+trParam).find("#quantityuArr");
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
									BINOLSTIOS12.addNewLine();
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

var BINOLSTIOS12 = new BINOLSTIOS12();

$(function(){
	if($("#actionResultDiv.actionError").is(":visible")){
		return;
	}
	if($("#inOrganizationId").val() != ""){
		BINOLSTIOS12.chooseDepart();
	}
	var option = {
			elementId:"bussinessPartner",
			showNum:20	
	};
	bussinessPartnerBinding(option);
	cherryValidate({			
		formId: "mainForm",		
		rules: {
			inDepotDate: {required: true,dateValid:true}	// 入库日期
		}		
	});
});
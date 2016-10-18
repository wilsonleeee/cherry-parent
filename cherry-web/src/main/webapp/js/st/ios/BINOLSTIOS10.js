function BINOLSTIOS10(){};

BINOLSTIOS10.prototype={
	"sortFlag":"",
	
	"submitflag":true,
	
	"getHtmlFun":function(info){
		var productVendorId = info.productVendorId;//产品厂商Id
		var unitCode = info.unitCode;//厂商编码
		var barCode = info.barCode;//产品条码
		var nameTotal = info.nameTotal;//产品名称
		var price = info.salePrice;// 默认使用销售价格
		var sysConfigUsePrice = $("#sysConfigUsePrice").val();
		if(sysConfigUsePrice == "MemPrice"){
			price = info.memPrice;//会员价格
		} else if(sysConfigUsePrice == "StandardCost") {
			price = info.standardCost;// 结算价
		}
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
		
		var html = '<tr id="dataRow_'+nextIndex+'">';
		html += '<td class="center"><input type="checkbox" id="chkbox" onclick="BINOLSTIOS10.changechkbox(this);"/></td>';
		html += '<td>' + unitCode +'<input type="hidden" id="unitCodeBinding_'+nextIndex+'" name="unitCodeBinding"  value=""/><input type="hidden" id="unitCodeArr"  value="'+ unitCode + '"/></td>';
		html += '<td>' + barCode + '<input type="hidden" id="barCodeArr" value="'+  barCode + '"/></td>';
		html += '<td>' + nameTotal + '</td>';
		html += '<td style="text-align:right;" >'+ price + '</td>';
		html += '<td id="nowCount" style="text-align:right;"></td>';
		html += '<td class="center"><input value="' + quantity +'" name="quantityArr" id="quantityArr" cssClass="text-number" style="width:120px;text-align:right;" size="10" maxlength="9" onchange="BINOLSTIOS10.calcAmount(this)" oninput="BINOLSTIOS10.calcAmount(this)" cssStyle="width:98%"/></td>';
		html += '<td class="center" style="text-align:right;">' + amount + '</td>';
		html += '<td class="center"><input value="' + reason +'" name="reasonArr"  type="text" id="reasonArr" size="25" maxlength="200"  cssStyle="width:98%"/></td>';
		html +='<td style="display:none">'
			+'<input type="hidden" id="prtVendorId" name="prtVendorId" value="'+ productVendorId + '"/>'
			+'<input type="hidden" id="priceArr" name="priceArr" value="'+ price +'"/></td></tr>';
		return html;
	},
	
	/**
	 * AJAX取得产品当前库存量
	 * 
	 * */
	"getPrtStock": function(){
		var url = $("#s_getStockCount").html();
		var lenTR = $("#databody").find("tr");
		var params = getSerializeToken();
		params += "&" + $("#outInventoryInfoID").serialize();
		params += "&" + $("#outLogicInventoryInfoID").serialize();
		params += "&" + $("#databody").find("#prtVendorId").serialize();
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
		if($("#spanBtnAdd").attr("class").indexOf("ui-state-disabled")>-1){
			return;
		}
		var that = this;
		BINOLSTIOS10.clearActionMsg();
		if($('#outOrganizationID').val()==null || $('#outOrganizationID').val()==""){
			$('#errorDiv2 #errorSpan2').html($('#noOutDepart').val());
			$('#errorDiv2').show();
			return false;
		}
		if($("#outInventoryInfoID option:selected").val()==""){
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
				BINOLSTIOS10.changeOddEvenColor();
//				// 拖动排序
//				$("#mainTable #databody").sortable({
//					update: function(event,ui){BINOLSTIOS10.changeOddEvenColor();}
//				});
				
				if($("#databody").find("tr").length>0){//禁掉生成发货单按钮
					$("#spanBtnAllRA").addClass("ui-state-disabled");
				}else{		//放开生成发货单按钮
					$("#spanBtnAllRA").removeClass("ui-state-disabled");
				}
				
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
				//计算总金额总数量
				BINOLSTIOS10.calcTotal();
				BINOLSTIOS10.bindInput();
			}
		};
		// 调用产品弹出框共通
		popAjaxPrtDialog(option);
		
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
	 * 计算总数量，总金额
	 * */
	"calcTotal":function(){
		var rows = $("#databody").children();
		var totalQuantity = 0;
		var totalAmount = 0.00;
		if(rows.length > 0){
			rows.each(function(i){
				var $tds = $(this).find("td");
				totalQuantity += Number($tds.eq(6).find(":input").val());
				totalAmount += Number($tds.eq(7).text());
			});
		}
		$("#totalQuantity").html(totalQuantity);
		$("#totalAmount").html(totalAmount.toFixed(2));
	},
	
	/**
	 * 退库方弹出框
	 * @return
	 */
	"openOutDepartBox":function(thisObj){
		BINOLSTIOS10.clearActionMsg();
		//取得所有部门类型
		var departType = "";
		for(var i=0;i<$("#departTypePop option").length;i++){
			var departTypeValue = $("#departTypePop option:eq("+i+")").val();
			departType += "&departType="+departTypeValue;
		}
		var param = "checkType=radio&privilegeFlg=1&businessType=1"+departType;
		param += "&valid=1";//不允许查询停用部门
		var callback = function() {
			var $selected = $('#departDataTable').find(':input[checked]').parents("tr");
			if($selected.length > 0) {
				var departId = $selected.find("input@[name='organizationId']").val();
				var departCode = $selected.find("td:eq(1)").text();
				var departName = $selected.find("td:eq(2)").text();
				$("#outOrganizationID").val(departId);
				$("#outOrgName").text("("+departCode+")"+departName);
				BINOLSTIOS10.chooseDepart();
			}
		};
		popDataTableOfDepart(thisObj,param,callback);
	},
	
	/**
	 * 更改了部门
	 * @param thisObj
	 */
	"chooseDepart":function(thisObj){
		BINOLSTIOS10.clearActionMsg();
		$('#mydetail').hide();
		$('#canceldetail').show();
		var organizationid = $('#outOrganizationID').val();
		var param ="organizationid="+organizationid;
		
		//更改了部门后，取得该部门所拥有的仓库
		var url = $('#urlgetdepotAjax').html()+"?csrftoken="+$('#csrftoken').val();	
		var callback = function(msg){
			BINOLSTIOS10.changeDepotDropDownList(msg);
		};
		cherryAjaxRequest({
			url:url,
			param:param,
			callback:callback
		});
		BINOLSTIOS10.clearDetailData(true);
	},
	
	/**
	 * 修改仓库下拉框
	 * @param data
	 * @return
	 */
	"changeDepotDropDownList":function(data){
		//data为json格式的文本字符串	
		var member = eval("("+data+")");//包数据解析为json 格式  
		$("#outInventoryInfoID").children().each(function(i){
			$(this).remove();
		});
		$.each(member, function(i){
			$("#outInventoryInfoID").append("<option value='"+ member[i].BIN_DepotInfoID+"'>"+escapeHTMLHandle(member[i].DepotCodeName)+"</option>"); 
		});	
		if($("#outInventoryInfoID option").length>0){
			$("#outInventoryInfoID").get(0).selectedIndex = 0;
			if($("#outOrganizationID").val() != null && $("#outOrganizationID").val() !=""){
				$("#outInventoryInfoID").attr("disabled",false);
				$("#outLogicInventoryInfoID").attr("disabled",false);
				BINOLSTIOS10.clearActionMsg();
			}
		}
		BINOLSTIOS10.getLogicDepotInfo();
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
	 * 接收退库方弹出框
	 * @return
	 */
	"openInDepartBox":function(thisObj){
		BINOLSTIOS10.clearActionMsg();
		BINOLSTIOS10.changeOddEvenColor();
		if($("#outOrganizationID").val() == ""){
			$("#errorSpan2").html($("#noOutDepart").val());
			$("#errorDiv2").show();
			return false;
		}else if($("#outInventoryInfoID option:selected").val() == "" || $("#outInventoryInfoID option:selected").val() == undefined){
			$("#errorSpan2").html($("#noOutDepot").val());
			$("#errorDiv2").show();
			return false;
		}
		
	 	//取得所有部门类型
	 	var departType = "";
	 	for(var i=0;i<$("#departTypePop option").length;i++){
	 		var departTypeValue = $("#departTypePop option:eq("+i+")").val();
	 		//排除4柜台
	 		if(departTypeValue != "4"){
	 			departType += "&departType="+departTypeValue;
	 		}
	 	}
		
		//var param = "checkType=radio&privilegeFlg=1&businessType=1"+departType+"&testType="+$("#inTestType").val();
		var param = "checkType=radio&businessType=60&depotId="+$("#outInventoryInfoID option:selected").val()+"&inOutFlag=OUT&departId="+$("#outOrganizationID").val();
		var callback = function() {
			var $selected = $('#_departDataTable').find(':input[checked]').parents("tr");
			var departId = $selected.find("input@[name='organizationId']").val();
			var departCode = $selected.find("td:eq(1)").text();
			var departName = $selected.find("td:eq(2)").text();
			$("#inOrgName").html("("+departCode+")"+departName);
			$("#inOrganizationID").val(departId);
		};
		//popDataTableOfDepart(thisObj,param,callback);
		popDepartTableByBusinessType(thisObj,param,callback);
	},
	
	/**
	 * 计算金额
	 * 
	 * */
	"calcAmount":function(obj){
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
		BINOLSTIOS10.calcTotal();
	},
	
	/**
	 * 删除选中行
	 */
	"deleterow":function(){
		if($("#databody #chkbox:checked").length == 0){
			return;
		}
		BINOLSTIOS10.clearActionMsg();
		$("#databody :checkbox").each(function(){
			var $this = $(this);
			if($this.prop("checked")){
				$this.parent().parent().remove();
			}
		});
		if($("#databody").find("tr").length == 0){
			$("#spanBtnAllRA").removeClass("ui-state-disabled");
		}
		$('#allSelect').prop("checked",false);
		BINOLSTIOS10.changeOddEvenColor();
		$('#allSelect').prop("checked",false);
		
		$("input[type=checkbox]").prop("checked",false);
		
		if($('#databody >tr').length == 0){
			BINOLSTIOS10.canceProductResult(true);
		}
		
		BINOLSTIOS10.calcTotal();
	},
	
	/**
	 * 保存单据
	 * 
	 */
	"save":function(){
		if(!BINOLSTIOS10.checkData()){
			return false;
		}
		var url = document.getElementById("saveURL").innerHTML;
		var param = $("#mainForm").serialize();
		var callback = function(msg){
			if(msg.indexOf("actionMessage") > -1){
				BINOLSTIOS10.clearPage(true);
			}
		};
		cherryAjaxRequest({
			url:url,
			param:param,
			callback:callback
		});
	},
	
	"btnSendClick":function(){
		if(!BINOLSTIOS10.checkData()){
			return false;
		}
		
		var checkStockFlag = $("#checkStockFlag").val();
		//为1库存允许为负，为0库存不允许为负
		if(checkStockFlag == "0"){
			if(BINOLSTIOS10.hasWarningData()){
				// 输入数量不能大于库存数量！
				$("#errorSpan2").html($("#errmsg_EST00042").val());
				$("#errorDiv2").show();
				return false;
			}
		}
		
		var url = document.getElementById("submitURL").innerHTML;
		var param = $("#mainForm").serialize();
		var callback = function(msg){
			if(msg.indexOf("actionMessage") > -1){
				BINOLSTIOS10.clearPage(true);
			}
		};
		cherryAjaxRequest({
			url:url,
			param:param,
			callback:callback
		});
	},
	
	/**
	 * 提交前对数据进行检查
	 * 检查发货数量是否大于库存
	 * @returns {Boolean}
	 */
	"hasWarningData":function(){
		var flag = false;
		//检查有无空行
		$.each($('#databody >tr'), function(i){	
			if(i>=0){
				if(Number($(this).find("#quantityArr").val())>Number($(this).find("#nowCount").text().replace(/,/g,""))){
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
	 * 提交前对数据进行检查
	 * @returns {Boolean}
	 */
	"checkData":function(){
		BINOLSTIOS10.clearActionMsg();
		var flag = true;
		if($("#outOrganizationID").val() == "" || $("#outOrganizationID").val() == "0"){
			$('#errorDiv2 #errorSpan2').html($('#noOutDepart').val());
			$('#errorDiv2').show();
			return false;
		}
		if(Number($("#outInventoryInfoID option:selected").val()) == 0){
			flag = false;
			$("#errorSpan2").html($("#errmsg4").val());
			$("#errorDiv2").show();
			return;
		}
		if($("#inOrganizationID").val() == ""){
			$('#errorDiv2 #errorSpan2').html($('#noInDepart').val());
			$('#errorDiv2').show();
			return false;
		}
		
		//检查有无空行
		var count = 0;
		$.each($('#databody >tr'), function(i){	
			if(i>=0){
				count +=1;
				if($(this).find("#quantityArr").val()=="" || $(this).find("#quantityArr").val()=="0" || $(this).find("#prtVendorId").val()==""){
					flag = false;
					$(this).attr("class","errTRbgColor");				
				}else{
					$(this).removeClass('errTRbgColor');				
				}		
			}
		});	
		BINOLSTIOS10.changeOddEvenColor1();
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
		if($('#databody >tr').length>1000){
			flag = false;
			//每次最多退1000条
			$('#errorDiv2 #errorSpan2').html($('#msg_maxDetail').val());
			$('#errorDiv2').show();
			return flag;
		}
		return flag;
	},
	
	/**
	 * 清理页面(保存或者提交成功后调用)
	 * @return
	 */
	"clearPage":function(flag){
		//$("#inOrgName").html(" ");
		//$("#inOrganizationId").val("");
		$.each($('#databody >tr'), function(i){
			if(i >= 0){
				$(this).remove();
			}
		});
		$("#reasonAll").val("");
		$("#totalQuantity").html("0");
		$("#totalAmount").html("0.00");
		$("#allSelect").prop("checked",false);
		BINOLSTIOS10.canceProductResult(flag);
	},
	
	"clearDetailData":function(flag){
		$.each($('#databody >tr'), function(i){
			if(i >= 0){
				$(this).remove();
			}
		});	
		//$("#outOrganizationID").val("");
		//$("#outOrgName").html("&nbsp");
		$("#totalQuantity").html("0");
		$("#totalAmount").html("0.00");
		$("#allSelect").prop("checked",false);
	},
	
	//取得对应的逻辑仓库
	"getLogicDepotInfo":function(){
		var url = $("#getLogicInfo").html();
		var param = "outOrganizationID="+$("#outOrganizationID").val();
		var callback = function(msg){
			if(msg == '[]'){
				$("#outLogicInventoryInfoID").html("");
			}else{
				var obj = eval('(' + msg + ')');
				var html = "";
				for(var index in obj){
					var logicInventoryInfoID = obj[index].BIN_LogicInventoryInfoID;
					var logicInventoryCodeName = obj[index].LogicInventoryCodeName;
					html=html+'<option value="'+logicInventoryInfoID+'">'+logicInventoryCodeName+'</option>';
				}
				$("#outLogicInventoryInfoID").html(html);
				BINOLSTIOS10.getInDepart();
			}
		};
		cherryAjaxRequest({
			url:url,
			param:param,
			callback:callback
		});
	},
	
	//取得对应的接收退库方
	"getInDepart":function(){
		if($("#outInventoryInfoID").val() == "" || $("#outInventoryInfoID").val() == undefined || $("#outInventoryInfoID").val() == null){
			return;
		}
		var url = $("#s_getInDepart").html();
		var param = "outInventoryInfoID="+$("#outInventoryInfoID").val();
		var callback = function(msg){
			if(msg == '[]'){
				$("#inOrganizationID").val("");
				$("#inOrgName").html("");
			}else{
				var obj = eval('(' + msg + ')');
				$("#inOrganizationID").val(obj.inOrganizationID);
				$("#inOrgName").html(obj.inDepartCodeName);
			}
		};
		cherryAjaxRequest({
			url:url,
			param:param,
			callback:callback
		});
	},
	
	/**
	 * 显示一个仓库下所有可退产品
	 */
	"showAllRA":function(){
		if($("#spanBtnAllRA").attr("class").indexOf("ui-state-disabled")>-1){
			return;
		}
		BINOLSTIOS10.clearDetailData(true);
		BINOLSTIOS10.clearActionMsg();
		if($('#outOrganizationID').val()==null || $('#outOrganizationID').val()==""){
			$('#errorDiv2 #errorSpan2').html($('#noOutDepart').val());
			$('#errorDiv2').show();
			return false;
		}
		if($("#outInventoryInfoID option:selected").val()==""){
			$('#errorDiv2 #errorSpan2').html($('#noOutDepot').val());
			$('#errorDiv2').show();
			return false;
		}
		$("#spanBtnCancelAllRA").show();
		$("#spanBtnAllRA").hide();
		$("#spanBtnAdd").addClass("ui-state-disabled");
		$("#addLineBtn").addClass("ui-state-disabled");
		//$("#spanBtnDelete").addClass("ui-state-disabled");

		var url = $("#s_getAllRAProduct").html();
		var params = getSerializeToken();
		params += "&" + $("#outInventoryInfoID").serialize();
		params += "&" + $("#outLogicInventoryInfoID").serialize();
		var sysConfigUsePrice = $("#sysConfigUsePrice").val();
		var callback = function(msg){
			if(msg == '[]'){
				
			}else{
				var jsons = eval(msg);
				for(var i=0;i<jsons.length;i++){
					var quantity = Number(jsons[i].Quantity);
					var price = jsons[i].SalePrice;//销售价格
					if(sysConfigUsePrice == "MemPrice"){
						price = jsons[i].MemPrice;//会员价格
					} else if(sysConfigUsePrice == "StandardCost") {
						price = jsons[i].StandardCost;// 结算价
					}
					price = parseFloat(price).toFixed(2);
					var amount = quantity * price;
					
					var nextIndex = parseInt($("#rowNumber").val())+1;
					$("#rowNumber").val(nextIndex);
					
					var html = '<tr id="dataRow_'+nextIndex+'">';
					html += '<td class="center"><input type="checkbox" id="chkbox" onclick="BINOLSTIOS10.changechkbox(this);"/></td>';
					html += '<td>' + jsons[i].UnitCode +'<input type="hidden" id="unitCodeBinding_'+nextIndex+'" name="unitCodeBinding"  value=""/><input type="hidden" id="unitCodeArr"  value="'+ jsons[i].UnitCode + '"/></td>';
					html += '<td>' + jsons[i].BarCode + '<input type="hidden" id="barCodeArr" value="'+  jsons[i].BarCode + '"/></td>';
					html += '<td>' + jsons[i].prodcutName + '</td>';
					html += '<td style="text-align:right;" >'+ price + '</td>';
					html += '<td id="nowCount" style="text-align:right;">'+ quantity +'</td>';
					html += '<td class="center"><input value="'+ quantity +'" name="quantityArr" id="quantityArr" cssClass="text-number" style="width:120px;text-align:right;" size="10" maxlength="9" onchange="BINOLSTIOS10.calcAmount(this)" oninput="BINOLSTIOS10.calcAmount(this)" cssStyle="width:98%"/></td>';
					html += '<td class="center" style="text-align:right;">'+ amount.toFixed(2); +'</td>';
					html += '<td class="center"><input value="" name="reasonArr" type="text" id="reasonArr" size="25" maxlength="200"  cssStyle="width:98%"/></td>';
					html +='<td style="display:none">'
						+'<input type="hidden" id="prtVendorId" name="prtVendorId" value="'+ jsons[i].BIN_ProductVendorID + '"/>'
						+'<input type="hidden" id="priceArr" name="priceArr" value="'+ price +'"/></td></tr>';
					$("#mainTable").append(html);
				}
				BINOLSTIOS10.calcTotal();
				BINOLSTIOS10.bindInput();	
			}
		};
		cherryAjaxRequest({
			url:url,
			param:params,
			callback:callback
		});
	},
	
	/**
	 * 取消显示全部产品
	 * @return
	 */
	"canceProductResult":function (flag){
		$('#spanBtnCancelAllRA').hide();
		$('#spanBtnAllRA').show();
		$("#spanBtnAdd").removeClass("ui-state-disabled");
		$("#addLineBtn").removeClass("ui-state-disabled");
		//$("#spanBtnDelete").removeClass("ui-state-disabled");
		$('#databody').empty();
		$("#totalQuantity").html("0");
		$("#totalAmount").html("0.00");
	},
	/**
	 * 添加新行
	 */
	"addNewLine":function(){
		if($("#addLineBtn").attr("class").indexOf("ui-state-disabled")>-1){
			return;
		}
		var that = this;
		BINOLSTIOS10.clearActionMsg();
		if($('#outOrganizationID').val()==null || $('#outOrganizationID').val()==""){
			$('#errorDiv2 #errorSpan2').html($('#noOutDepart').val());
			$('#errorDiv2').show();
			return false;
		}
		if($("#outInventoryInfoID option:selected").val()==""){
			$('#errorDiv2 #errorSpan2').html($('#noOutDepot').val());
			$('#errorDiv2').show();
			return false;
		}
				
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
			
				
			var html = '<tr id="dataRow_'+nextIndex+'">';
			html += '<td class="center"><input type="checkbox" id="chkbox" onclick="BINOLSTIOS10.changechkbox(this);"/></td>';
			html += '<td><span id="spanUnitCode"></span><input type="text" id="unitCodeBinding_'+nextIndex+'" name="unitCodeBinding"  value=""/><input type="hidden" id="unitCodeArr"  value=""/></td>';
			html += '<td><span id="spanBarCode"></span><input type="hidden" id="barCodeArr" value=""/></td>';
			html += '<td><span id="spanProductName"></span></td>';
			html += '<td style="text-align:right;" id="salePrice">'+ 0 + '</td>';
			html += '<td id="nowCount" style="text-align:right;">'+ 0.00 + '</td>';
			html += '<td class="center"><input value="" name="quantityArr" id="quantityArr" cssClass="text-number" style="width:120px;text-align:right;" size="10" maxlength="9" onchange="BINOLSTIOS10.calcAmount(this)" oninput="BINOLSTIOS10.calcAmount(this)" cssStyle="width:98%"/></td>';
			html += '<td class="center" style="text-align:right;">' + 0.00 + '</td>';
			html += '<td class="center"><input value="" name="reasonArr"  type="text" id="reasonArr" size="25" maxlength="200"  cssStyle="width:98%"/></td>';
			html +='<td style="display:none">'
				+'<input type="hidden" id="prtVendorId" name="prtVendorId" value=""/>'
				+'<input type="hidden" id="productVendorIDArr" name="productVendorIDArr" value=""/>'
				+'<input type="hidden" id="priceArr" name="priceArr" value=""/></td></tr>';
			
			$("#databody").append(html);
			
			var checkbox= $('#dataTableBody').find(':input').is(":checked");
			//改变奇偶行的样式
			BINOLSTIOS10.changeOddEvenColor();
			
			if($("#databody").find("tr").length>0){//禁掉退库全选
				$("#spanBtnAllRA").addClass("ui-state-disabled");
			}else{		//放开退库全选
				$("#spanBtnAllRA").removeClass("ui-state-disabled");
			}
			
			// 设置全选状态
			var $checkboxs = $('#databody').find(':checkbox');
			var $unChecked = $('#databody').find(':checkbox:not(":checked")');
			if($unChecked.length == 0 && $checkboxs.length > 0){
				$('#allSelect').prop("checked",true);
			}else{
				$('#allSelect').prop("checked",false);
			}
			
			var unitCode = "unitCodeBinding_"+nextIndex;
			BINOLSTIOS10.setProductBinding(unitCode);
			
			$("#unitCodeBinding_"+nextIndex).focus();
			
			BINOLSTIOS10.bindInput();			
		}
	},
	/**
	 * 绑定产品
	 */
	"setProductBinding":function(id){
		productBinding({elementId:id,showNum:20,targetDetail:true,targetId:'productVendorIDArr',afterSelectFun:BINOLSTIOS10.pbAfterSelectFun});
	},
	
	/**
	 * 产品下拉输入框选中后执行方法
	 */
	"pbAfterSelectFun":function(info){
		$("#actionResultDisplay").empty();
		$("#errorSpan").html("");
		$("#errorDiv").hide();
		$('#'+info.elementId).val(info.unitCode);
		//验证产品厂商ID是否重复
		var flag = true;
		$.each($('#databody >tr'), function(i){
			if($(this).find("#prtVendorId").val() == info.prtVendorId){
				flag = false;
				$("#errorSpan2").html($("#errmsg_EST00035").val());
				$("#errorDiv2").show();
				return;
			}
		});
		if(flag){
			$("#errorDiv2").hide();
			// 下拉框共通中的销售价格为price,而弹出框中的销售价格为salePrice
			var price = info.price;//销售价格
			if(isEmpty(price)){
				price = parseFloat("0.0");
			}else{
				price = parseFloat(price);
			}
			price = price.toFixed(2);

					
			//设置隐藏值
			$('#'+info.elementId).parent().parent().find("[name='prtVendorId']").val(info.prtVendorId);
			$('#'+info.elementId).parent().parent().find("[name='productVendorIDArr']").val(info.prtVendorId);
			$('#'+info.elementId).parent().parent().find("#unitCodeArr").val(info.unitCode);
			$('#'+info.elementId).parent().parent().find("#barCodeArr").val(info.barCode);
			$('#'+info.elementId).parent().parent().find("#priceArr").val(price);
			
			//设置显示值
			$('#'+info.elementId).parent().parent().find("#salePrice").html(info.price);
			$('#'+info.elementId).parent().parent().find("#spanUnitCode").html(info.unitCode);
			$('#'+info.elementId).parent().parent().find("#spanBarCode").html(info.barCode);
			$('#'+info.elementId).parent().parent().find("#spanProductName").html(info.productName);
			
			//取消该文本框的自动完成并隐藏。
			$('#'+info.elementId).unautocomplete();
			$('#'+info.elementId).hide();
			
			//查询库存
			BINOLSTIOS10.getPrtStock();
			
			//跳到数量文本框
			var $quantityInputText =  $('#'+info.elementId).parent().parent().find("#quantityArr");
			// 获取焦点
			$quantityInputText.select();
			//计算总金额总数量
			BINOLSTIOS10.calcTotal();
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
			BINOLSTIOS10.bindInputTR(trParam);
		});
	},
	
	"bindInputTR":function(trParam){		
		//按Enter键到下一个文本框，当前行最后一个文本框按Enter键到下一行或新增行。
		var $inp = $(trParam+" input:text");
		$inp.bind('keydown', function (e) {
			if($("#currentMenuID").val() == "BINOLSTIOS10"){				
				var key = e.which;
				if (key == 13) {
					//回车键
					e.preventDefault();
					//当前文本框为下拉框可见且输入有问题如重复，停止跳到下一个
					var nextFlag = true;
					if($(this).attr("name") == "unitCodeBinding"  && $(this).is(":visible")){
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
									BINOLSTIOS10.addNewLine();
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


var BINOLSTIOS10 = new BINOLSTIOS10();

/**
* 页面初期处理
*/
$(function(){
	if($("#outOrganizationID").val() != null && $("#outOrganizationID").val() !=""){
		$("#outInventoryInfoID").attr("disabled",false);
		$("#outLogicInventoryInfoID").attr("disabled",false);
	}
});



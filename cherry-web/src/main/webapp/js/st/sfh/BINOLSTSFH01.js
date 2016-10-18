function BINOLSTSFH01(){};

BINOLSTSFH01.prototype={
	"sortFlag":"",
	
	"submitflag":true,
	
	"getHtmlFun":function(info,negativeFlag){
		var productVendorId = info.productVendorId;//产品厂商Id
		var unitCode = info.unitCode;//厂商编码
		var barCode = info.barCode;//产品条码
		var nameTotal = info.nameTotal;//产品名称
		var price = info.salePrice;//销售价格
		if(isEmpty(price)){
			price = parseFloat("0.0");
		}else{
			price = parseFloat(price);
		}
		price = price.toFixed(2);
//		var curStock = info.curStock;//当前库存
//		if(isEmpty(curStock)){
//			curStock = 0;
//		}
		var quantity=info.quantity;//数量
		if(isEmpty(quantity)){
			quantity = "";
		}
		if(!isEmpty(quantity) && negativeFlag){
			quantity = Number(quantity)*(-1);
		}
		var amount = price * Number(quantity);//金额
		amount = amount.toFixed(2);
		var reason = info.reason;//备注
		if(quantity == 0){quantity = "";}
		if(isEmpty(reason)){reason = "";}
		var html = '<tr>';
		html += '<td class="center"><input type="checkbox" id="chkbox" onclick="BINOLSTSFH01.changechkbox(this);"/></td>';
		html += '<td>' + unitCode +'<input type="hidden" id="unitCodeArr"  value="'+ unitCode + '"/></td>';
		html += '<td>' + barCode + '<input type="hidden" id="barCodeArr" value="'+  barCode + '"/></td>';
		html += '<td>' + nameTotal + '</td>';
		html += '<td style="text-align:right;" >'+ price + '</td>';
		html += '<td style="text-align:right;"></td>';
		html += '<td class="center"><input value="' + quantity +'" name="quantityArr" id="quantityArr" cssClass="text-number" style="width:120px;text-align:right;" size="10" maxlength="9" onchange="BINOLSTSFH01.calcAmount(this)" oninput="BINOLSTSFH01.calcAmount(this)" cssStyle="width:98%"/></td>';
		html += '<td class="center" style="text-align:right;">' + amount + '</td>';
		html += '<td class="center"><input value="' + reason +'" name="reasonArr"  type="text" id="reasonArr" size="25" maxlength="200"  cssStyle="width:98%"/></td>';
		html +='<td style="display:none">'
			+'<input type="hidden" name="prtVendorId" value="'+ productVendorId + '"/>'
			+'<input type="hidden" id="productVendorIDArr" name="productVendorIDArr" value="'+productVendorId+'"/>'
			+'<input type="hidden" id="priceUnitArr" name="priceUnitArr" value="'+ price +'"/></td></tr>';
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
		params += "&" + $("#inDepotId").serialize();
		params += "&" + $("#inLogicDepotId").serialize();
		params += "&" + $("#databody").find("#productVendorIDArr").serialize();
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
		BINOLSTSFH01.clearActionMsg();
		if($('#inOrganizationId').val()==null || $('#inOrganizationId').val()==""){
			$('#errorDiv2 #errorSpan2').html($('#noOutDepart').val());
			$('#errorDiv2').show();
			return false;
		}
		if($("#inDepotId option:selected").val()==""){
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
				BINOLSTSFH01.changeOddEvenColor();
				// 拖动排序
				$("#mainTable #databody").sortable({
					update: function(event,ui){BINOLSTSFH01.changeOddEvenColor();}
				});
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
				BINOLSTSFH01.calcTotal();
			}
		};
		// 调用产品弹出框共通
		popAjaxPrtDialog(option);
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
					
					if (!isEmpty(prtVendorId)) {		
						var obj = {"prtVendorId":prtVendorId,"quantityArr":quantityArr,"reasonArr":reasonArr};
						question.push(obj);
					}					
			});
			
			var queStr = JSON2.stringify(question);
			url+="&queStr="+queStr;
			
		popup(url);
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
				totalAmount +=Number($tds.eq(7).text());
			});
		}
		$("#totalQuantity").html(totalQuantity);
		$("#totalAmount").html(totalAmount.toFixed(2));
	},
	
	/**
	 * 订货方弹出框
	 * @return
	 */
	"openInDepartBox":function(thisObj){
		BINOLSTSFH01.clearActionMsg();
		//取得所有部门类型
		var departType = "";
		for(var i=0;i<$("#departTypePop option").length;i++){
			var departTypeValue = $("#departTypePop option:eq("+i+")").val();
			departType += "&departType="+departTypeValue;
		}
		var param = "checkType=radio&privilegeFlg=1&businessType=1"+departType;
		var callback = function() {
			var $selected = $('#departDataTable').find(':input[checked]').parents("tr");
			if($selected.length > 0) {
				var departId = $selected.find("input@[name='organizationId']").val();
				var departCode = $selected.find("td:eq(1)").text();
				var departName = $selected.find("td:eq(2)").text();
				$("#inOrganizationId").val(departId);
				$("#inOrgName").text("("+departCode+")"+departName);
				BINOLSTSFH01.chooseDepart();
			}
		};
		popDataTableOfDepart(thisObj,param,callback);
	},
	
	/**
	 * 更改了部门
	 * @param thisObj
	 */
	"chooseDepart":function(thisObj){
		BINOLSTSFH01.clearActionMsg();
		$('#mydetail').hide();
		$('#canceldetail').show();
		var organizationid = $('#inOrganizationId').val();
		var param ="organizationid="+organizationid;
		
		//更改了部门后，取得该部门所拥有的仓库
		var url = $('#urlgetdepotAjax').html()+"?csrftoken="+$('#csrftoken').val();	
		var callback = function(msg){
			BINOLSTSFH01.changeDepotDropDownList(msg);
		};
		cherryAjaxRequest({
			url:url,
			param:param,
			callback:callback
		});
		BINOLSTSFH01.clearDetailData(true);
	},
	
	/**
	 * 修改仓库下拉框
	 * @param data
	 * @return
	 */
	"changeDepotDropDownList":function(data){
		//data为json格式的文本字符串	
		var member = eval("("+data+")");//包数据解析为json 格式  
		$("#inDepotId").children().each(function(i){
			$(this).remove();
		});
		$.each(member, function(i){
			$("#inDepotId").append("<option value='"+ member[i].BIN_DepotInfoID+"'>"+escapeHTMLHandle(member[i].DepotCodeName)+"</option>"); 
		});	
		if($("#inDepotId option").length>0){
			$("#inDepotId").get(0).selectedIndex = 0;
			if($("#inOrganizationId").val() != null && $("#inOrganizationId").val() !=""){
				$("#inDepotId").attr("disabled",false);
				$("#inLogicDepotId").attr("disabled",false);
				BINOLSTSFH01.clearActionMsg();
			}
		}
		BINOLSTSFH01.getLogicDepotInfo();
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
	 * 发货方弹出框
	 * @return
	 */
	"openOutDepartBox":function(thisObj){
		BINOLSTSFH01.clearActionMsg();
		BINOLSTSFH01.changeOddEvenColor();
		if($("#inOrganizationId").val() == ""){
			$("#errorSpan2").html($("#noInDepart").val());
			$("#errorDiv2").show();
			return false;
		}else if($("#inDepotId option:selected").val() == "" || $("#inDepotId option:selected").val() == undefined){
			$("#errorSpan2").html($("#noInDepot").val());
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
		var param = "checkType=radio&businessType=30&depotId="+$("#inDepotId option:selected").val()+"&inOutFlag=IN&departId="+$("#inOrganizationId").val();
		var callback = function() {
			var $selected = $('#_departDataTable').find(':input[checked]').parents("tr");
			var departId = $selected.find("input@[name='organizationId']").val();
			var departCode = $selected.find("td:eq(1)").text();
			var departName = $selected.find("td:eq(2)").text();
			$("#outOrgName").html("("+departCode+")"+departName);
			$("#outOrganizationId").val(departId);
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
		BINOLSTSFH01.calcTotal();
	},
	
	/**
	 * 删除选中行
	 */
	"deleterow":function(){
		BINOLSTSFH01.clearActionMsg();
		$("#databody :checkbox").each(function(){
			var $this = $(this);
			if($this.prop("checked")){
				$this.parent().parent().remove();
			}
		});
		$('#allSelect').prop("checked",false);
		BINOLSTSFH01.changeOddEvenColor();
		$('#allSelect').prop("checked",false);
		
		$("input[type=checkbox]").prop("checked",false);
		BINOLSTSFH01.calcTotal();
	},
	
	/**
	 * 保存订货单
	 * 
	 */
	"save":function(){
		if(!BINOLSTSFH01.checkSaveData()){
			return false;
		}
		var url = document.getElementById("saveURL").innerHTML;
		var param = $("#mainForm").serialize();
		var callback = function(msg){
			if(msg.indexOf("actionMessage") > -1){
				BINOLSTSFH01.clearPage(true);
			}
		};
		cherryAjaxRequest({
			url:url,
			param:param,
			callback:callback
		});
	},
	
	"btnSendClick":function(){
		if(!BINOLSTSFH01.checkData()){
			return false;
		}
		var url = document.getElementById("submitURL").innerHTML;
		var param = $("#mainForm").serialize();
		var callback = function(msg){
			if(msg.indexOf("actionMessage") > -1){
				BINOLSTSFH01.clearPage(true);
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
	 * @returns {Boolean}
	 */
	"checkData":function(){
		BINOLSTSFH01.clearActionMsg();
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
		
		//检查有无空行
		var count = 0;
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
		BINOLSTSFH01.changeOddEvenColor1();
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
	 * 保存前对数据进行检查（允许空数量）
	 * @returns {Boolean}
	 */
	"checkSaveData":function(){
		BINOLSTSFH01.clearActionMsg();
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
		BINOLSTSFH01.changeOddEvenColor1();

		return flag;
	},
	
	/**
	 * 清理订货页面(保存或者提交成功后调用)
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
	},
	
	"clearDetailData":function(flag){
		$.each($('#databody >tr'), function(i){
			if(i >= 0){
				$(this).remove();
			}
		});	
		$("#outOrganizationId").val("");
		$("#outOrgName").html("&nbsp");
		$("#totalQuantity").html("0");
		$("#totalAmount").html("0.00");
		$("#allSelect").prop("checked",false);
	},
	
	//取得对应的逻辑仓库
	"getLogicDepotInfo":function(){
		var url = $("#getLogicInfo").html();
		var param = "inOrganizationId="+$("#inOrganizationId").val();
		var callback = function(msg){
			if(msg == '[]'){
				$("#inLogicDepotId").html("");
			}else{
				var obj = eval('(' + msg + ')');
				var html = "";
				for(var index in obj){
					var logicInventoryInfoID = obj[index].BIN_LogicInventoryInfoID;
					var logicInventoryCodeName = obj[index].LogicInventoryCodeName;
					html=html+'<option value="'+logicInventoryInfoID+'">'+logicInventoryCodeName+'</option>';
				}
				$("#inLogicDepotId").html(html);
				BINOLSTSFH01.getOutDepart();
			}
		};
		cherryAjaxRequest({
			url:url,
			param:param,
			callback:callback
		});
	},
	
	//取得对应的发货方
	"getOutDepart":function(){
		if($("#inDepotId").val() == "" || $("#inDepotId").val() == undefined || $("#inDepotId").val() == null){
			return;
		}
		var url = $("#s_getOutDepart").html();
		var param = "inDepotId="+$("#inDepotId").val();
		var callback = function(msg){
			if(msg == '[]'){
				$("#outOrganizationId").val("");
				$("#outOrgName").html("");
			}else{
				var obj = eval('(' + msg + ')');
				$("#outOrganizationId").val(obj.outOrganizationId);
				$("#outOrgName").html(obj.outDepartCodeName);
			}
		};
		cherryAjaxRequest({
			url:url,
			param:param,
			callback:callback
		});
	},
	
	"getTableHtmlObj":function(){
		var htmlObjArr = [];
		var children = $("#databody").children();
		for(var i = 0 ; i < children.length ; i++){
			var child = children[i];
			var quantityArr = $(child).find("#quantityArr").val();
			if(isEmpty(quantityArr)){
				quantityArr="";
			}else{
			    quantityArr= Number($(child).find("#quantityArr").val());
			}
			var o = {
				_html : child.innerHTML.escapeHTML(),
				_style : child.style,
				_quantityArr : quantityArr,
				_blankBarArr : $(child).find("#barArr").val(),
				_blankUnitArr : $(child).find("#unitArr").val(),
				_unitCodeArr : $(child).find("#unitCodeArr").val(),
				_barCodeArr : $(child).find("#barCodeArr").val(),
				_quantArr : quantityArr,
				_reasonArr : $(child).find("#reasonArr").val()
			};
			htmlObjArr.push(o);
		}
		return htmlObjArr;
	},
	
	"sortTable":function(sortFlag){
		if(sortFlag){
			BINOLSTSFH01.sortFlag = sortFlag;
		}else{
			return false;
		}
		var htmlObjArr = BINOLSTSFH01.getTableHtmlObj();
		if(htmlObjArr.length > 0){
			htmlObjArr.sort(BINOLSTSFH01.sortObject);
			if($("#"+sortFlag).attr("class").indexOf("ui-icon-triangle-1-n") > -1){
				htmlObjArr.reverse();
				$("#"+sortFlag).attr("class","css_right ui-icon ui-icon-triangle-1-s");
			}else{
				$("#"+sortFlag).attr("class","css_right ui-icon ui-icon-triangle-1-n");
			}
			$("#databody").html("");
			for(var i = 0 ; i < htmlObjArr.length ; i++){
				var trHtml = "<tr></tr>";
				$('#databody').append(trHtml);
				$('#databody tr:last-child').append(htmlObjArr[i]._html.unEscapeHTML()).find("#quantityArr").val(htmlObjArr[i]._quantityArr);
				$('#databody tr:last-child').find("#reasonArr").val(htmlObjArr[i]._reasonArr);
			}
			BINOLSTSFH01.changeOddEvenColor();
			$("#blankTable #databody").find("tr").each(function (i){
				$(this).find("td").eq(0).html(i+1);
			});
		}
	},
	
	"sortObject":function(obj1,obj2){
		var sortFlag = BINOLSTSFH01.sortFlag;
		if(obj1[sortFlag] < obj2[sortFlag]){
			return -1;
		}else if(obj1[sortFlag] > obj2[sortFlag]){
			return 1;
		}else{
			return 0;
		}
	}
};


var BINOLSTSFH01 = new BINOLSTSFH01();

/**
* 页面初期处理
*/
$(function(){
	//BINOLSTSFH01.chooseDepart();
	if($("#inOrganizationId").val() != null && $("#inOrganizationId").val() !=""){
		$("#inDepotId").attr("disabled",false);
		$("#inLogicDepotId").attr("disabled",false);
	}
});
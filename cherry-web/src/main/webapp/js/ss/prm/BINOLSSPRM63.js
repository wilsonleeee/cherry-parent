var BINOLSSPRM63 = function () {};

BINOLSSPRM63.prototype = {
	
	"SSPRM63_dialogBody":"",
		
	/**
	  * 删除掉画面头部的提示信息框
	  * @return
	  */
	"clearActionMsg":function(){
		$('#actionResultDisplay').html("");
		$('#errorDiv2').attr("style",'display:none');
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
				BINOLSSPRM63.chooseDepart();
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
				BINOLSSPRM63.clearActionMsg();
			}
		}else{
			$("#depotInfoId").attr("disabled",true);
			$("#logicDepotsInfoId").attr("disabled",true);
			$('#errorDiv2 #errorSpan2').html($('#errmsg_EST00015').val());
			$('#errorDiv2').show();
		}
		BINOLSSPRM63.getLogicDepotInfo();
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
	 * 促销品弹出框
	 */
	"openProPopup":function(_this){
		var that = this;
		if(!this.checkSelect()){
			return;
		}
		this.clearActionMsg();
		var option = {
				targetId: "databody",
				checkType : "checkbox",
				prmCate :'CXLP',//促销礼品
				mode : 2,
				brandInfoId : $("#brandInfoId").val(),
				isStock : "1",//显示管理库存的促销品
				getHtmlFun:function(info){
					return that.getHtmlFun(info);
				},
				click:function (){
					// 设置全选状态
					var $checkboxs = $('#databody').find(':checkbox');
					var $unChecked = $('#databody').find(':checkbox:not(":checked")');
					if($unChecked.length == 0 && $checkboxs.length > 0){
						$('#checkAll').prop("checked",true);
					}else{
						$('#checkAll').prop("checked",false);
					}
					that.changeOddEvenColor();
				}
			};
		popAjaxPrmDialog(option);
	},

	"getHtmlFun" : function (info,negativeFlag){
		var promotionProductVendorId=info.promotionProductVendorId;//促销品厂商Id
		var unitCode = info.unitCode;//厂商编码
		var barCode = info.barCode;//产品条码
		var nameTotal = info.nameTotal;//促销品名称
		var standardCost = info.standardCost;//结算价格
		if(isEmpty(standardCost)){
			standardCost = parseFloat("0.0");
		}else{
			standardCost = parseFloat(standardCost);
		}
		standardCost = standardCost.toFixed(2);
		
		var quantity=info.quantity;//数量
		if(isEmpty(quantity)){
			quantity = "";
		}
		if(!isEmpty(quantity) && negativeFlag){
			quantity = Number(quantity)*(-1);
		}
		var amount = standardCost * Number(quantity);//金额
		amount = amount.toFixed(2);
		var reason = escapeHTMLHandle(info.reason);//备注
		if(isEmpty(reason)){
			reason = "";
		}
		var html = '<tr>';
			html += '<td class="center"><input type="checkbox" id="chkbox" onclick="BINOLSSPRM63.changechkbox(this);"/></td>';
			html += '<td>' + unitCode + '</td>';
			html += '<td>' + barCode + '</td>';
			html += '<td>' + nameTotal + '</td>';
			html += '<td class="hide"><input name="batchNoArr" id="batchNoArr" style="text-align:right;" cssClass="text-number" maxlength="20" cssStyle="width:98%"/></td>';
			html += '<td style="text-align:right;">' +standardCost + '</td>';
			html += '<td style="text-align:right;"><input value="'+quantity+'" name="quantityArr"style="text-align:right;" id="quantityArr" cssClass="text-number" size="10" maxlength="9" onchange="BINOLSSPRM63.calcuAmount(this);" cssStyle="width:98%"/></td>';
			html += '<td class="center" style="text-align:right;">' + amount + '</td>';
			html += '<td class="center"><input value="'+reason+'" name="reasonArr" size="25" maxlength="200" cssStyle="width:98%"/></td>';
			html +='<td style="display:none">'
				 +'<input type="hidden" name="prmVendorId" value="'+ promotionProductVendorId + '"/>'
				 +'<input type="hidden" id="prmVendorIDArr" name="prmtVendorIDArr" value="'+promotionProductVendorId+'"/>'
				 +'<input type="hidden" id="priceArr" name="priceArr" value="'+standardCost+'"/></td></tr>';
		return html;
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
			$thisTd.next().text("0.00");
		}else{
			var price = Number($thisTd.parent().find("#priceArr").val());
			$this.val(parseInt(Number($this.val())));
			var amount = price * Number($this.val());
			$thisTd.next().text(amount.toFixed(2));
		}
		this.calcuTatol();
	},
	
	"calcuTatol":function(){
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
				BINOLSSPRM63.clearDetailData();
				BINOLSSPRM63.getPrmInDepotDetail(negativeFlag,inDepotID);
			}
		};
		var param ="organizationId="+$('#inOrganizationId').val()+"&depotInfoId="+$('#depotInfoId').val();
		popDataTableOfPrmInDepot(null,param,callback);
	},
	
	/**
	 * 取得促销品入库单详单
	 * @return
	 */
	"getPrmInDepotDetail":function(negativeFlag,inDepotID){
		var that = this;
		var callback = function(msg) {
			var jsons = eval(msg);
			for(var i=0;i<jsons.length;i++){
				var html = that.getHtmlFun(jsons[i],negativeFlag);
				$("#databody").append(html);
			}
			//添加了新行，去除全选框的选中状态
			$('#allSelect').prop("checked",false);
			BINOLSSPRM63.changeOddEvenColor();
			//计算总金额总数量
			BINOLSSPRM63.calcuTatol();
		};
		//ajax取得选中的发货单详细
		var param ="inDepotID="+inDepotID+"&organizationId="+$("#inOrganizationId").val()+"&depotInfoId="+$("#depotInfoId").val()+"&logicDepotInfoId="+$("#logicDepotsInfoId").val();
		cherryAjaxRequest({
			url:$("#s_getInDepotDetail").html(),
			reloadFlg:true,
			param:param,
			callback:callback
		});
	}
};

var BINOLSSPRM63 = new BINOLSSPRM63();

$(function(){
	if($("#actionResultDiv.actionError").is(":visible")){
		return;
	}
	if($("#inOrganizationId").val() != ""){
		BINOLSSPRM63.chooseDepart();
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
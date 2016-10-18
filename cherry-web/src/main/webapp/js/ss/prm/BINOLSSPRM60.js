function BINOLSSPRM60(){};

BINOLSSPRM60.prototype={
		
	"readyPopDepart":function(obj){
		var that = this;
		//取得所有部门类型
		var departType = "";
		var CounterShiftFlag = $("#CounterShiftFlag").val();
		for(var i=0;i<$("#departTypePop option").length;i++){
			var departTypeValue = $("#departTypePop option:eq("+i+")").val();
			if(CounterShiftFlag == "false" && departTypeValue == "4"){
				//不支持终端移库时排除4柜台
				continue;
			}else{
				departType += "&departType="+departTypeValue;
			}
		}
		
		var param = "privilegeFlg=1&businessType=1"+departType;
		var callback = function() {
			var checkedRadio = $("#departDialogInit").find("input[name='organizationId']:checked");
			var departId = "";
			var departName = "";
			var departCode = "";
			if(checkedRadio){
				departId = checkedRadio.val();
				departCode = checkedRadio.parent().parent().children("td").eq(1).find("span").text().escapeHTML();
				departName = checkedRadio.parent().parent().children("td").eq(2).find("span").text().escapeHTML();
			}
			if(departId != undefined && null != departId){
				$("#departId").val(departId);
				$("#departName").html("("+departCode+")"+departName);
				that.getDepotInfo(departCode,obj);
				
				$("#actionResultDisplay").empty();
				$("#errorSpan").html("");
				$("#errorDiv").hide();
			}else{
				$("#errorSpan_pop").html($("#noDepartWarnig").html());
				$("#errorDiv_pop").show();
			}
		};
		var option={
			checkType:"radio",
			brandInfoId:$("#brandInfoId").val(),
			param:param,
			click:callback
		};
		popAjaxDepDialog(option);
	},
	
	//取得对应的仓库
	"getDepotInfo":function(val,obj){
		var url = $("#getDapotInfo").html();
		var param = "departId="+$("#departId").val();
		var callback = function(msg){
			if(msg == '[]'){
				$("#errorSpan").html($("#noDepotWarning").html());
				$("#errorDiv").show();
				$("#depotInfoId").html("");
				$("#shiftData").html("");
				$("#fromLogicInventoryInfoId").attr("disabled",true);
				$("#toLogicInventoryInfoId").attr("disabled",true);
				$("#depotInfoId").attr("disabled",true);
			}else{
				var obj = eval('(' + msg + ')');
				var html = "";
				for(var index in obj){
					var depotInfoId = obj[index].BIN_DepotInfoID;
					var depotNameCn = obj[index].DepotCodeName;
					html=html+'<option value="'+depotInfoId+'">'+depotNameCn+'</option>';
				}
				$("#depotInfoId").html(html);
				$("#fromLogicInventoryInfoId").attr("disabled",false);
				$("#toLogicInventoryInfoId").attr("disabled",false);
				$("#depotInfoId").attr("disabled",false);
				$("#shiftData").html("");
			}
			binOLSSPRM60.getLogicDepotInfo();
		};
		cherryAjaxRequest({
			url:url,
			param:param,
			callback:callback
		});
	},
	
	//取得对应的逻辑仓库
	"getLogicDepotInfo":function(){
		var url = $("#getLogicInfo").html();
		var param = "departId="+$("#departId").val();
		var callback = function(msg){
			if(msg == '[]'){
				$("#fromLogicInventoryInfoId").html("");
				$("#toLogicInventoryInfoId").html("");
			}else{
				var obj = eval('(' + msg + ')');
				var html = "";
				for(var index in obj){
					var logicInventoryInfoID = obj[index].BIN_LogicInventoryInfoID;
					var logicInventoryCodeName = obj[index].LogicInventoryCodeName;
					html=html+'<option value="'+logicInventoryInfoID+'">'+logicInventoryCodeName+'</option>';
				}
				$("#fromLogicInventoryInfoId").html(html);
				$("#toLogicInventoryInfoId").html(html);
			}
		};
		cherryAjaxRequest({
			url:url,
			param:param,
			callback:callback
		});
	},
	
	"openProPopup":function(_this){
		$("#actionResultDisplay").empty();
		$("#errorSpan").html("");
		$("#errorDiv").hide();
		if($("#departName").html() == ""){
			$("#errorSpan").html($("#noDepartWarnig").html());
			$("#errorDiv").show();
			return false;
		}else if($("#depotInfoId").html() == ""){
			$("#errorSpan").html($("#noDepotWarning").html());
			$("#errorDiv").show();
			return false;
		}else if(!$("#fromLogicInventoryInfoId").val()){
			$("#errorSpan").html($("#noLogiDepotWarning").html());
			$("#errorDiv").show();
			return false;
		}
		var that = this;
		var option = {
				targetId: "shiftData",
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
					var $checkboxs = $('#shiftData').find(':checkbox');
					var $unChecked = $('#shiftData').find(':checkbox:not(":checked")');
					if($unChecked.length == 0 && $checkboxs.length > 0){
						$('#checkAll').prop("checked",true);
					}else{
						$('#checkAll').prop("checked",false);
					}
					that.changeOddEvenColor();
					that.getStock();
				}
			};
		popAjaxPrmDialog(option);
	},
	
	"getHtmlFun" : function (info){
		var num = $("#number").val();
		$("#number").val(Number(num)+1);
		var html = '<tr id="dataRow'+num+'">';
		html += '<td style="text-align:center" id="dataTd0">';
		html += '<input type="checkbox" class="checkbox" id="checkbox'+num+'" value="'+num+'" onclick="binOLSSPRM60.checkSelect(this)" name="validFlag"/>';
		html += '<input name="priceArr" value="'+info.standardCost+'" class="hide"/>';
		html += '<input name="prmVendorId" value="'+info.promotionProductVendorId+'" class="hide">';
		html += '<input name="prmVendorIdArr" value="'+info.promotionProductVendorId+'" class="hide">';
		html += '<td id="dataTd2">'+info.unitCode+'</td>';
		html += '<td id="dataTd3">'+info.barCode+'</td>';
		html += '<td id="dataTd4">'+info.nameTotal+'</td>';
		html += '<td style="text-align:right" id="dataTd5"></td>';
		html += '<td id="dataTd6" style="width:100px">';
		html += '<input type="text" value="" maxlength="8" style="text-align:right;width:95%" class="text1" name="quantityArr" id="quantityArr" onchange="binOLSSPRM60.isInteger(this);"/></td>';
		html += '<td style="text-align:left" id="dataTd7"><input type="text" value="" style="width:95%" class="text1" name="commentsArr" id="commentsArr"/></td></tr>';
		return html;
	},
	
	"getStock":function(){
		var len = $("#shiftData tr").length;
		if(len>0){
			var param = "depotId="+$("#depotInfoId").val()+"&loginDepotId="+$("#fromLogicInventoryInfoId").val();
			for(var i=0;i<len;i++){
				var promotionProductVendorID = $($("#shiftData tr [name=prmVendorIdArr]")[i]).val();
				var currentIndex = $($("#shiftData tr :checkbox")[i]).val();
				param += "&promotionProductVendorID="+promotionProductVendorID;
				param += "&currentIndex="+currentIndex;
			}
			cherryAjaxRequest({
				url:$("#s_getStockCount").html(),
				reloadFlg:true,
				param:param,
				callback:binOLSSPRM60.getStockSuccess
			});
		}
	},
	
	"getStockSuccess":function(msg){	
		var member = eval("("+msg+")"); //数据解析为json 格式  	
		$.each(member, function(i){
			$('#dataRow'+member[i].currentIndex+" > #dataTd5").html(member[i].Quantity);
		});
	},
	
	"btnSaveClick":function(){
		var that = this;
		$("#actionResultDisplay").empty();
		$("#errorSpan").html("");
		$("#errorDiv").hide();
		if($("#departName").html() == ""){
			$("#errorSpan").html($("#noDepartWarnig").html());
			$("#errorDiv").show();
			return false;
		}else  if($("#depotInfoId").html() == ""){
			$("#errorSpan").html($("#noDepotWarning").html());
			$("#errorDiv").show();
			return false;
		}else if($("#shiftData").html().length == 0){
			$("#errorSpan").html($("#noProductWarning").html());
			$("#errorDiv").show();
			return false;
		}else if(!$("#fromLogicInventoryInfoId option:selected").val() || !$("#toLogicInventoryInfoId option:selected").val()){
			$("#errorSpan").html($("#noLogiDepotWarning").html());
			$("#errorDiv").show();
			return false;
		}
		if(!this.checkData()){
			$("#errorSpan").html($("#errmsg_EST00008").html());
			$("#errorDiv").show();
			return false;
		}
		var url = $("#save").html();
		var param = $("#mainForm").serialize();
		var callback = function(msg){
			if(msg.indexOf("actionMessage") > -1){
				that.clearPage();
			}
		};
		cherryAjaxRequest({
			url:url,
			param:param,
			callback:callback
		});
	},
	
	"clearPage":function(){
		$("#shiftData").html("");
		$("#comments").val("");
	},
	
	"checkSelectAll" : function(obj) {
		$("#actionResultDisplay").html("");
		$("#errorDiv #errorSpan").html("");
		$("#errorDiv").hide();
		var $this = $(obj);
		$("#shiftData").find("input[name=validFlag]").prop("checked",$this.prop("checked"));
	},

	"checkSelect" : function(obj) {
		$("#actionResultDisplay").html("");
		$("#errorDiv #errorSpan").html("");
		$("#errorDiv").hide();
		var $this = $(obj);
		if ($this.prop("checked")) {
			var num = $("#shiftData").find("input[name=validFlag]").length;
			if ($("#shiftData").find("input[name=validFlag]:checked").length == num) {
				$("input[name=allSelect]").prop("checked", true);
			}
		} else {
			$("input[name=allSelect]").prop("checked", false);
		}
	},
	
	"deleteLine":function(){
		var checked = $("#shiftData").find("input[name=validFlag]:checked");
		if(checked.length == 0){
			return false;
		}else{
			checked.each(function(){
				if(!$(this).is(":hidden")){
					$(this).parent().parent().remove();
				}
			});
			$("input[name=allSelect]").prop("checked", false);
			$("#shiftData tr:odd").attr("class","even");
			$("#shiftData tr:even").attr("class","odd");
		}
	},
	
	"isInteger":function(obj){
		var $this = $(obj);
		var patrn=/^0$|^[1-9]{1}[0-9]{0,7}$/; 
		if (!patrn.exec($this.val())){
			$this.val("");
		}
	},
	
	"changeOddEvenColor":function(){
		$("#shiftData tr:odd").attr("class","even");
		$("#shiftData tr:even").attr("class","odd");
	},
	
	"changeOddEvenColor1":function(){
		$("#shiftData tr:odd").attr("class",function(){
			if($(this).attr("class")=="errTRbgColor"){
				
			}else{
				$(this).addClass("odd");
			}
		});
		$("#shiftData tr:even").attr("class",function(){
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
		
		//检查有无空行
		$.each($('#shiftData >tr'), function(){
			if($(this).find("#quantityArr").val()=="" || $(this).find("#quantityArr").val()=="0"){
				flag = false;
				$(this).attr("class","errTRbgColor");
			}else{
				$(this).removeClass('errTRbgColor');
			}
		});
		return flag;
	},
	
	"changeLogic":function(obj){
		$('#shiftData').html('');
	}
};
var binOLSSPRM60 = new BINOLSSPRM60();
$(document).ready(function() {

});
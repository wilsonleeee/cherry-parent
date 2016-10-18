function BINOLSTIOS03(){};

BINOLSTIOS03.prototype={
		/**
		  * 删除掉画面头部的提示信息框
		  * @return
	      */
		"clearActionMsg":function(){
			$('#actionResultDisplay').html("");
			$('#errorDiv2').attr("style",'display:none');
		},

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
					$("#inOrganizationId").val(departId);
					$("#inOrgName").text("("+departCode+")"+departName);
					BINOLSTIOS03.chooseDepart();
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
			$("#actionResultDisplay").html("");
			$("#errorDiv #errorSpan").html("");
			$("#errorDiv").hide();
			//data为json格式的文本字符串	
			var member = eval("("+data+")");    //包数据解析为json 格式  
			$("#depotInfoId").html("");
			if(member.length==0)
			{	
				$('#errorSpan').html($('#errmsg_EST00015').val());
				$('#errorDiv').show();
			}
			else
			{
				$.each(member, function(i){
					$("#depotInfoId").append("<option value='"+ member[i].BIN_DepotInfoID+"'>"+escapeHTMLHandle(member[i].DepotCodeName)+"</option>"); 
				});
				if($("#depotInfoId option").length>=1){
					$("#depotInfoId").attr("disabled",false);
					$("#logicInventoryInfoId").attr("disabled",false);
					BINOLSTIOS03.clearActionMsg();
				}else{
					$("#depotInfoId").attr("disabled",true);
					$("#logicInventoryInfoId").attr("disabled",true);
					$('#errorDiv #errorSpan').html($('#errmsg_EST00015').val());
					$('#errorDiv').show();
				}
			}

		},		
		/**
		 * 产品弹出框
		 **/
	"openProPopup":function(_this){
	  if($("#departName").html() == ""){
			$("#errorSpan").html($("#noDepartWarnig").html());
			$("#errorDiv").show();
			return false;
		  }
	  else if($("#depotInfoId").val() == null || $("#depotInfoId").val() == ""){
			$("#errorSpan").html($("#noDepotWarning").html());
			$("#errorDiv").show();
			return false;
			}
		$("#errorSpan").html("");
		$("#errorDiv").hide();
		$("#actionResultDisplay").html("");
		this.changeOddEvenColor();
		var that = this;
		var option = {
         	   	targetId: "databody",
 	           	checkType : "checkbox",
 	           	mode : 2,
 	         //  bindFlag : true,
 	           	brandInfoId : $("#brandInfoId").val(),
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

			   	   that.getStock();
			   	}
 	        };
 		popAjaxPrtDialog(option);
	},
	
	"getHtmlFun" : function (info){
		var num = $("#number").val();
		$("#number").val(Number(num)+1);
		var html = '<tr id="dataRow_'+num+'">';
    	html += '<td style="text-align:center" id="dataTd1">';
    	html += '<input type="checkbox"  class="checkbox" id="checkbox_'+num+'" onclick="BINOLSTIOS03.checkSelect(this)" name="validFlag"/>';
    	html += '<input name="productVendorIdArr" value="'+info.productVendorId+'" class="hide">';
    	html += '<input name="priceArr" value="'+info.salePrice+'" class="hide">';
    	html += '<input name="prtVendorId" value="'+info.productVendorId+'" class="hide"></td>';
    	html += '<td id="dataTd2">'+info.unitCode+'</td>';
    	html += '<td id="dataTd3">'+info.barCode+'</td>';
    	html += '<td id="dataTd4">'+info.nameTotal+'</td>';
    	html += '<td style="text-align:right" id="dataTd5">'+0+'</td>';
    	html += '<td id="dataTd6"><input type="text" value="" style="text-align: right;width:90%;" class="text" name="quantityArr" maxlength="7" id="quantityArr" onchange="BINOLSTIOS03.isInteger(this);"/></td>';
    	html += '<td id="dataTd7"><input type="text" value="" style="width:95%;" maxlength="200" class="text" name="commentsArr"/></td></tr>';
    	return html;
	},
	
	"getStock":function(){
		var that = this;
		$("#databody").find("tr").each(function(){
			var html = "";
			var $tdDiv = $(this).find("td");
			var unitCode = $tdDiv.eq(1).text();
			var barCode = $tdDiv.eq(2).text();
			var url = $("#getPrtVenIdAndStock").attr("href");
			var param = $("#mainForm").serialize()+"&unitCode="+unitCode+"&barCode="+barCode;
			var callback = function(msg){
				if(msg == 'null'){
					var stock = 0;
				}else{
					var obj = eval('(' + msg + ')');
					var stock = obj.quantity;
				}
				$tdDiv.eq(4).html(stock);
				that.changeOddEvenColor1();
			};
			cherryAjaxRequest({
				url:url,
				param:param,
				callback:callback,
				reloadFlg:true
				});
			
		});
		
	},
	
	"changeOddEvenColor":function(){
		$("#databody").find("tr:odd").attr("class","even");
		$("#databody").find("tr:even").attr("class","odd");
	},
	
	"changeOddEvenColor1":function(){
		$.each($("#databody").find("tr:odd"),function(){
			if($(this).prop("class").indexOf("errTRbgColor")==-1){
				$(this).prop("class","even");
			}
		});
		$.each($("#databody").find("tr:even"),function(){
			if($(this).prop("class").indexOf("errTRbgColor")==-1){
				$(this).prop("class","odd");
			}
		});
	},
		
	"save":function(){
		$("#actionResultDisplay").html("");
		$("#errorDiv #errorSpan").html("");
		$("#errorDiv").hide();
		if($("#databody").html().length == 0){
			$('#errorDiv #errorSpan').html($("#noProductWarning").val());
			$("#errorDiv").show();
			return false;
		}else{
			if(!this.checkData()){
				$('#errorDiv #errorSpan').html($('#errmsg_EST00008').val());
				$("#errorDiv").show();
				return false;
			}
			var url = $("#save").attr("href");
			var param = $("#mainForm").serialize();
			var callback=function(msg){
				if((msg.indexOf('errorMessage') == -1) && (msg.indexOf('hidFieldErrorMsg') == -1)){
					$("#errorDiv #errorSpan").html("");
					$("#errorDiv").hide();
					$("#databody").html("");
				}
			};
			cherryAjaxRequest({
				url:url,
				param:param,
				callback:callback
				});
		}
	},
	
	"checkSelectAll" : function(obj) {
		$("#actionResultDisplay").html("");
		$("#errorDiv #errorSpan").html("");
		$("#errorDiv").hide();
		var $this = $(obj);
		$("#databody").find("input[name=validFlag]").prop("checked",
				$this.prop("checked"));
	},

	"checkSelect" : function(obj) {
		$("#actionResultDisplay").html("");
		$("#errorDiv #errorSpan").html("");
		$("#errorDiv").hide();
		var $this = $(obj);
		if ($this.prop("checked")) {
			var num = $("#databody").find("input[name=validFlag]").length;
			if ($("#databody").find("input[name=validFlag]:checked").length == num) {
				$("input[name=allSelect]").prop("checked", true);
			}
		} else {
			$("input[name=allSelect]").prop("checked", false);
		}
	},
	
	/**
	 * 提交前对数据进行检查
	 * @returns {Boolean}
	 */
	"checkData":function(){
		var flag = true;
		$.each($('#databody >tr'), function(){
				if($(this).find("#quantityArr").val()=="" || $(this).find("#quantityArr").val()=="0"){
					flag = false;
					$(this).attr("class","errTRbgColor");	
				}else{
					$(this).removeClass('errTRbgColor');				
				}	
		});	
		return flag;
	},

	
	"deleteLine":function(){
		this.clearActionMsg();
		var checked = $("#databody").find("input[name=validFlag]:checked");
		if(checked.length == 0){
			return false;
		}else{
			checked.each(function(){
				var id = $(this).attr("id");
				var idArr = id.split("_");
				var index = idArr[1];
				var trId = "dataRow_"+index;
				$("#"+trId).remove();
			});
		}
		$("input[name=allSelect]").prop("checked", false);
		BINOLSTIOS03.changeOddEvenColor();
	},
	
	"clearDetailData":function(){
		$("#databody").html("");
	},
	
	"isInteger":function(obj){
		var $this = $(obj);
		var patrn=/^0$|^[1-9]{1}[0-9]{0,7}$/; 
		if (!patrn.exec($this.val())){
			$this.val("");
		}
	}
};

var IOS03_dialogBody = "";
var BINOLSTIOS03 = new BINOLSTIOS03();
$(document).ready(function(){
	BINOLSTIOS03.chooseDepart();
	IOS03_dialogBody = $('#productDialog').html();
});
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<s:i18n name="i18n.message.message">
<script type="text/javascript">
// 打印日志tip
function getPrintTip(obj) {
	$('#dataTable').find(obj).cluetip({
	    width: '350',
		height: 'auto',
	    cluetipClass: 'default',
	    cursor:'pointer',
	    arrows: true, 
	    dropShadow: false});
}

//包装错误信息
function getErrorMessage(msg){
	var msgPrefix = '<div id="printErrorMessage"><div class="actionError"><ul><li><span>';
	var msgSuffix = '</span></li></ul></div></div>';
	return msgPrefix + msg + msgSuffix;
}
//清除打印单号
function cleanPrintBill(){
	var $checkboxs = $('#dataTable_wrapper #dataTable_Cloned').find(":checkbox:checked");
	$("#allSelect").prop("checked",false);
	$checkboxs.prop("checked",false);
	$("#print_param_hide").find(":input[name='billId']").remove();

}
//设置按钮为全选状态
function checkBillAll(_this){
	$("#printErrorMessage").remove();
	var isChecked = $(_this).prop("checked");
	var $checkboxs = $('#dataTable_wrapper #dataTable_Cloned').find(":checkbox");
	if(isChecked){
		$checkboxs.each(function(){
			var $that = $(this);
			if(!$that.prop("checked")){
				var billHtml = '<input id="'+$that.val()+'" type="hidden" name="billId" value="'+$that.val()+'"/>';
		        $("#print_param_hide").append(billHtml);
		        $that.prop("checked",true);
			}
		});
	}else{
		$checkboxs.each(function(){
			var $that = $(this);
			$("#print_param_hide").find("#"+$that.val()).remove();
	        $that.prop("checked",false);
		});
	}
}
//checkbox勾选
function checkBill(_this){
	var $this = $(_this);
	var value = $this.val();
	$("#printErrorMessage").remove();
	if($this.prop("checked")) {
		var billHtml = '<input id="'+value+'" type="hidden" name="billId" value="'+value+'"/>';
        $("#print_param_hide").append(billHtml);
        if($("#dataTable_Cloned input@[id=checkbill]").length == $("#dataTable_Cloned input@[id=checkbill][checked]").length) 
        {
            $("input@[id=allSelect]").prop("checked",true);
        }
    } else {
    	$("#print_param_hide").find("#"+value).remove();
    	$("input@[id=allSelect]").prop("checked",false);
    }
}
// 取得打印对象url
function getReportUrl(url){
	var url = "/Cherry/common/BINOLCM99_jasperPrint";
	var params = $("#print_param_hide").find(":input").serialize();
	url += "?" + getSerializeToken();
	if("" != params){
		url += "&" + params;
	}
	return url;
}
// 取得打印记录url
function getLogUrl(){
	var url = "/Cherry/common/BINOLCM99_writeLog";
	url += "?" + getSerializeToken();
	var $printDiv = $("#print_param_hide");
	var params = $printDiv.find(":input[name!='billId']").serialize();
	if("" != params){
		url += "&" + params;
	}
	return url;
}
/**
 * 打印或预览操作
 * @param code:打印：Print; 预览打印： VIEW(目前已经不再支持Print,即使参数为Print也是预览打印 )
 * @param errTarget:错误提示信息放置位置的 Target
 * @param type:业务类型，新增的参数，用于单据流程变化 后需要在同一张页面中打印生成的关联单
 *						（如订货单生成发货单后需要在订货单页面打印发货单）
 */
function openPrintApp(code,errTarget,type) {
	// 业务类型
	var typeTemp = (type == null ? '':type);
	if(errTarget != null){
		var $errTarget = $(errTarget);
		$errTarget.find("#printErrorMessage").remove();
		var $billId = $("#print_param_hide"+typeTemp).find(":input[name='billId']");
		if($billId.length == 0){
			var msg = '<s:text name="ECM00056"/>';
			$errTarget.prepend(getErrorMessage(msg));
			return;
		}
	}
	// 保存原始的CODE值
	var code_temp = code;
	/**
	* 打印按钮统一不再使用，批量打印实现【多张单据后台生成一个pdf，返回给浏览器】
	* 票号：WITPOSQA-14648
	*
	*/
	
	/* if(code == "PrintRet"){
		var applet = "";
		code = "PrinterApplet.class";
		var _app = navigator.appName;
		// 获取打印对象URL
		var report_url = getReportUrl();
		// 此时打印的是销售单对应的签收单
		if(code_temp == "PrintRet") {
			report_url += '&receiptFlag=1';
		}
		// 记录报表成功打印URL
		var log_url = getLogUrl();
		// 
		var archive = "jasperreports-applet-4.1.2.jar"
					+ ",commons-collections-2.1.1.jar"
					+ ",xstream-1.4.2.jar"
					+ ",xmlpull-1.1.3.1.jar"
					+ ",commons-logging-1.0.4.jar";

		if(_app == 'Netscape'){
			applet = '<EMBED CODEBASE = "/Cherry/applet" CODE = "' + code + '"'
			 	+ ' ARCHIVE = "' + archive + '" REPORT_URL = "'+report_url+'" LOG_URL = "'+log_url+'"'
			 	+ ' TYPE="application/x-java-applet;version=1.1.2" WIDTH = "0" HEIGHT = "0"'
			 	+ ' pluginspage="http://java.sun.com/products/plugin/1.1.2/plugin-install.html"></EMBED>'
//		}else if(_app == 'Microsoft Internet Explorer'){
//			applet = '<OBJECT CLASSID = "clsid:8AD9C840-044E-11D1-B3E9-00805F499D93" WIDTH = "0" HEIGHT = "0" '     
//				 + 'CODEBASE = "http://java.sun.com/products/plugin/1.1.2/jinstall-112-win32.cab#Version=1,1,2,0">'
//				 + '<PARAM NAME = "CODE" VALUE = "'+code+'">'
//				 + '<PARAM NAME = "ARCHIVE" VALUE = "'+archive+'">' 
//				 + '<PARAM NAME = "CODEBASE" VALUE = "/Cherry/applet">'
//				 + '<PARAM NAME = "type" VALUE="application/x-java-applet;version=1.2.2">'   
//				 + '<PARAM NAME = "scriptable" VALUE="false">' 
//		 		 + '<PARAM name="java_arguments" VALUE="-Xmx256m -Dsun.java2d.noddraw=true">'
//				 + '<PARAM NAME = "REPORT_URL" VALUE ="'+report_url+'"></OBJECT>'
//				 + '<PARAM NAME = "LOG_URL" VALUE ="'+log_url+'"></OBJECT>';
		}else { 
			applet = '<APPLET ID="JrPrt" CODE="'+code+'" CODEBASE = "/Cherry/applet" ARCHIVE = "'+archive+'" WIDTH = "0" HEIGHT = "0">'
				+ '<PARAM NAME = "type" VALUE="application/x-java-applet;version=1.2.2">'   
				+ '<PARAM NAME = "scriptable" VALUE="false">'
				+ '<PARAM NAME = "REPORT_URL" VALUE ="'+report_url+'"></APPLET>'
				+ '<PARAM NAME = "LOG_URL" VALUE ="'+log_url+'"></APPLET>';
		}
	    $("#print_div").html(applet); 
	}else{*/
		var url = '/Cherry/common/BINOLCM99_export?exportType=pdf&isView=1';
		var params = $("#print_param_hide"+typeTemp).find(":input").serialize();
		url += "&" + getSerializeToken();
		if("" != params){
			url += "&" + params;
		}
		
		// 此时预览的的是销售单对应的签收单
		if(code_temp == "ViewRet" || code_temp == "PrintRet") {
			url += '&receiptFlag=1';
		}
		popup(url,{center:"no",height: 650,width: 880});
		return false;
	//}
}

/**
 * 用于打印WebPos销售单据 
 * @param printType:打印类型：
 *			"0":销售直接后直接打印
 *			"1":补打小票
 *			"2":退货
 */
function printWebPosSaleBill(printType) {
	var url = '/Cherry/common/BINOLCM99_printWebPosSaleBill?exportType=pdf&isView=1&printType='+printType;
	var params = $("#print_param_hide").find(":input").serialize();
	url += "&" + getSerializeToken();
	if("" != params){
		url += "&" + params;
	}
	popup(url,{center:"no",height: 650,width: 880});
	return false;
}

/**
 * 用于打印WebPos产品吊牌
 */
 function printWebPosProductBill(obj,originalBrand) {
		var url = '/Cherry/common/BINOLCM99_printWebPosProductBill?exportType=pdf&isView=1';
		var params = $("#print_param_hide").find(":input").serialize();
		url += "&" + getSerializeToken();
		if("" != params){
			url += "&" + params;
		}
		url += "&printTagType=" + obj;
		if(obj == 2){
			url += "&originalBrand=" + originalBrand;
		}
		popup(url,{center:"no",height: 650,width: 880});
		return false;
	}

function printSearchProductWithoutParam(){
	var url = '/Cherry/common/BINOLCM99_printWebPosProductBill?exportType=pdf&isView=1&pageId=BINOLPTJCS44_1';
	var params = $("#searchPrintbody").find(":input").serialize();
	url += "&" + getSerializeToken();
	url += "&" + params;
	popup(url,{center:"no",height: 650,width: 880});
	return false;
}

/**
 * 导出产品二维码
 */
function printSearchProduct(prtUniqueCodeID){
	var url = '/Cherry/common/BINOLCM99_printProductUnq?exportType=pdf&isView=1&pageId=BINOLPTJCS44_1';
	var params = $("#searchPrintbody").find(":input").serialize();
	url += "&" + getSerializeToken();
	url += "&" + params;
	popup(url,{center:"no",height: 650,width: 880});
	return false;
}

</script>
<div id="print_div"></div>
</s:i18n>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<s:i18n name="i18n.wp.BINOLWPSAL13">
<div id="transactionDetailPageDiv" class="hide ui-dialog-content ui-widget-content" style="display: block; width: auto; min-height: 300px;">
    <div class="wpleft_header">
    <form id="transactionDetailForm" method="post" class="inline">
   	    <div class="header_box">
			<s:text name="wpsal13.searchDate"/>
			<span><s:textfield id="fromDate" name="fromDate" cssClass="date" cssStyle="width:80px;" value="%{nowDate}" onkeyup="BINOLWPSAL07.keyupStartDate(this);return false;"/></span>
			<s:text name="wpsal13.toDate" />
			<span><s:textfield id="toDate" name="toDate" cssClass="date" cssStyle="width:80px;" value="%{nowDate}" onkeyup="BINOLWPSAL07.keyupEndDate(this);return false;"/></span>
			<input class="hide" id="dgCardCode" name="cardCode" value="<s:property value='cardCode'/>"/>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<s:text name="wpsal13.billCode" />
			<input id="dgBillCode" name="billCode" type="text" class="date" style="width:250px;"/>
			<button id="btnSaleSearch" class="close" type="button" onclick="BINOLWPSAL13.transactionDetail();return false;">
	    		<span class="ui-icon icon-search"></span>
				<span class="button-text"><s:text name="wpsal13.search"/></span>
			</button>
        </div>
    </form>
    </div>
    <div id="searchTransactionDiv" class="wp_tablebox">
        <table width="100%" cellspacing="0" cellpadding="0" border="0" class="wp_table" id="transactionDetailTable">
        	 <thead>
	             <tr>
	             	<th><s:text name="wpsal13.date" /></th>
	               	<th><s:text name="wpsal13.type" /></th>
	           		<th><s:text name="wpsal13.amount" /></th>
	           		<th><s:text name="wpsal13.billCode" /></th>
	           		<th><s:text name="wpsal13.act"></s:text></th>
	             </tr>
             </thead>
        	<tbody id="searchTransactionbody"></tbody>
        </table>
   </div>
   </div>
</s:i18n>
<script>
$(document).ready(function(){
	// 表单验证初期化
	cherryValidate({
		formId: 'transactionDetailForm',
		rules: {
			fromDate: {dateValid: true},
			toDate: {dateValid: true}
		}
	});
	$("#fromDate").cherryDate({
		beforeShow: function(input){
			var value = $("#toDate").val();
			var minDate = BINOLWPSAL13.dateAdd('m', value, -1);
			return [changeDateToString(minDate), value];
		},
		onSelectEvent: function(input){
			// 查询按钮获得焦点
			$("#btnSaleSearch").focus();
		}
	});
	$("#toDate").cherryDate({
		beforeShow: function(input){
			var value = $("#fromDate").val();
			var maxDate = BINOLWPSAL13.dateAdd('m', value, 1);
			return [value, changeDateToString(maxDate)];
		},
		onSelectEvent: function(input){
			// 查询按钮获得焦点
			$("#btnSaleSearch").focus();
		}
	});
	// 绑定回车键
	$("#btnSaleSearch").find("input:text:visible")
	.bind("keydown", function (e) {
		var key = e.which;
		if (key == 13) {
			e.preventDefault();
			BINOLWPSAL13.transactionDetail();
		}
	});
})
</script>
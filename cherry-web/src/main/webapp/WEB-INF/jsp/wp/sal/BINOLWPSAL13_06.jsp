<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<s:i18n name="i18n.wp.BINOLWPSAL13">
<div class="hide">
	<input id="revokeBillCode" name="billCode" value="<s:property value='billCode'/>"/>
</div>
<div class="wp_tablebox">
	<table width="100%" cellspacing="0" cellpadding="0" border="0" class="wp_detail">
		<tr>
			<td width="50%"><s:text name="wpsal13.rechargeamount"></s:text></td>
			<td><s:property value="transactionDetailMap.amount"/></td>
		</tr>
		<tr>
			<td><s:text name="wpsal13.recharTime"></s:text></td>
			<td><s:property value="transactionDetailMap.transactionTime"/></td>
		</tr>
		<tr>
			<td><s:text name="wpsal13.verificationType"></s:text></td>
			<td>
				<span>
					<s:select list="verificationTypeList" listKey="CodeKey" listValue="Value" id="verificationType" name="verificationType" onchange="BINOLWPSAL13.verificationType();return false;"></s:select>
				</span>
			</td>
		</tr>
		<tr>
			<td>
				<s:text name="wpsal13.placePrint"></s:text>
					<span id="verificationCode1"><s:text name="wpsal13.password"></s:text></span>
					<span id="verificationCode2" class="hide"><s:text name="wpsal13.verification"></s:text></span>
			</td>
			<td><input id="verificationCode" name="verificationCode" class="text"/>
			<button id="getVerificationCodeButton" onclick="BINOLWPSAL13.getVerificationCode();return false;"><s:text name="wpsal13.getVerificationCode"/></button>
			</td>
		</tr>
	</table>
</div>
<div class="bottom_butbox clearfix">
  	<button id="btnConfirm" class="close" type="button" onclick="BINOLWPSAL13.revoke();return false;">
  		<span class="ui-icon icon-confirm"></span>
        <span class="button-text"><s:text name="wpsal13.confirm"/></span>
	</button>
	<button id="btnCancel" class="close" type="button" onclick="BINOLWPSAL13.cancel4();return false;">
		<span class="ui-icon icon-close"></span>
	    <span class="button-text"><s:text name="wpsal13.cancel"/></span>
	</button>
</div>
</s:i18n>
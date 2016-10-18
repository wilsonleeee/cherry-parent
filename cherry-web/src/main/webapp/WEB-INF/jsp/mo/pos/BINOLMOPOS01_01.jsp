<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<div id="sEcho"><s:property value="sEcho" /></div>
<div id="iTotalRecords"><s:property value="iTotalRecords" /></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords" /></div>
<s:i18n name="i18n.mo.BINOLMOPOS01">
<div id="aaData">
	<s:iterator value="storePayConfigList" status="status">
		<ul>
			<li>
				<input value=<s:property value="storePayCode"/> id="validFlag" onclick="BINOLMOPOS01.checkRecord(this,'#storePayConfigTableTbode');" type="checkbox">
			</li>
			<li><s:property value="storePayCode" /></li>
			<li><s:property value="storePayValue" /></li>
			<li>
				<div id="payType">
					<s:if test='payType=="1"'><s:text name="MOPOS01_special"/></s:if>
					<s:else><s:text name="MOPOS01_special2"/></s:else>
				</div>
			</li>
			<li>
				<div id="isEnable">
					<s:if test='isEnable=="Y"'><s:text name="MOPOS01_isEnable"></s:text></s:if>
					<s:else><s:text name="MOPOS01_noEnable"></s:text></s:else>
				</div>
			</li>
			
			<li>
				<div id="defaultPay">
					<s:if test='defaultPay=="Y"'><s:text name="MOPOS01_defaultPay1"></s:text></s:if>
					<s:else><s:text name="MOPOS01_defaultPay2"></s:text></s:else>
				</div>
			</li>
		</ul>
	</s:iterator>
</div>
</s:i18n>

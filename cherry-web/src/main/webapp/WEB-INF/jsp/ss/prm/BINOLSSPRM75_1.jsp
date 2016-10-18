<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/ss/prm/BINOLSSPRM75.js?V=20160728"></script>
<s:i18n name="i18n.ss.BINOLSSPRM75">
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<div id="aaData">
<s:iterator value="couponList" id="couponInfo">
	<s:url id="edit_url" action="BINOLSSPRM73_editInit" namespace="/ss">
		<s:param name="ruleCode">${couponInfo.ruleCode}</s:param>
	</s:url>
	<ul>
		<li><span><s:property value="#couponInfo.couponNo"/></span></li>
		<li>
			<span>		
				<s:property value="#application.CodeTable.getVal('1383',couponType)"/>
			</span>
		</li>
		<li><a href="${edit_url}" onclick="openWin(this);return false;"><s:property value="#couponInfo.ruleCode"/></a></li>
		<li><span><s:property value="#couponInfo.couponCode"/></span></li>
		<li>
			<span>
				<s:property value="#couponInfo.bpCode"/>
			</span>
		</li>
		<li>
			<span>
				<s:property value="#couponInfo.memCode"/>
			</span>
		</li>
		<li><span><s:property value="#couponInfo.memberMobile"/></span></li>
		
		<li><span><s:property value="#couponInfo.startTime"/></span></li>
		<li><span><s:property value="#couponInfo.endTime"/></span></li>
		<li><span><s:property value="#couponInfo.createTime"/></span></li>
		<li><span><s:property value="#couponInfo.relationBill"/></span></li>
		<li><span><s:property value="#couponInfo.departCode"/></span></li>
		<li><span><s:property value="#couponInfo.departName"/></span></li>
		<li>
			<span>
				<s:property value="#application.CodeTable.getVal('1384',status)"/>
			</span>
		</li>
		<li>
			<span>
				<s:if test="#couponInfo.validFlag ==1"><span class='ui-icon icon-valid'></span></s:if>
				<s:elseif test="#couponInfo.validFlag ==0"><span class='ui-icon icon-invalid'></span></s:elseif>
			</span>
		</li>
		<li>
		<s:if test="#couponInfo.status=='AR'">
			<s:url id="edit_url" action="BINOLSSPRM75_editInit" namespace="/ss">
				<s:param name="couponNo"><s:property value="#couponInfo.couponNo"/></s:param>
			</s:url>
			<a class="delete" href="${edit_url}" onclick="openWin(this);return false;">
				<span class="ui-icon icon-edit"></span><span class="button-text"><s:text name="Edit"/></span>
			</a>
			<s:url action="BINOLSSPRM75_stop" id="stop_Url" namespace="/ss"></s:url>
			<a href="${stop_Url}" id="stopUrl"></a>
			<a class="delete" onclick = "stopCoupon('<s:property value="#couponInfo.couponNo"/>');"><span class="ui-icon icon-delete"></span><span class="button-text"><s:text name="Stop"/></span></a>
			<a class="delete" onclick = "sendMsg('<s:property value="#couponInfo.couponNo"/>','<s:property value="#couponInfo.memberMobile"/>');"><span class="ui-icon icon-edit"></span><span class="button-text"><s:text name="SendMsg"/></span></a>
		</s:if>
		<s:elseif test="#couponInfo.status=='OK'">
			<s:url id="usedInfo" action="BINOLSSPRM75_usedInfoInit" namespace="/ss">
				<s:param name="couponNo"><s:property value="#couponInfo.couponNo"/></s:param>
			</s:url>
			<a class="delete" href="${usedInfo}" onclick="openWin(this);return false;">
				<span class="ui-icon icon-copy"></span><span class="button-text"><s:text name="Search"/></span>
			</a>
		</s:elseif>
		</li>
	</ul>
</s:iterator>
</div>
</s:i18n>

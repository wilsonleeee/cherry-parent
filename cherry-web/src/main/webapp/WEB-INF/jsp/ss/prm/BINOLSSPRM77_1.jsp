<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/ss/prm/BINOLSSPRM77.js?V=20160728"></script>
<s:i18n name="i18n.ss.BINOLSSPRM77">
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<div id="aaData">
<s:iterator value="couponList" id="couponInfo">
	<ul>
		<li>
			<span>
				<s:if test="#couponInfo.couponType==1 ">
					<s:text name="couponType1"></s:text>
				</s:if>
				<s:elseif test="#couponInfo.couponType==2 ">
					<s:text name="couponType2"></s:text>
				</s:elseif>
				<s:elseif test="#couponInfo.couponType==3 ">
					<s:text name="couponType3"></s:text>
				</s:elseif>
				<s:elseif test="#couponInfo.couponType==4 ">
					<s:text name="couponType4"></s:text>
				</s:elseif>
				<s:elseif test="#couponInfo.couponType==5 ">
					<s:text name="couponType5"></s:text>
				</s:elseif>
				<s:else><s:text name="notDefined"/></s:else>
			</span>
		</li>
		<li><span><s:property value="#couponInfo.ruleCode"/></span></li>
		<li><span><s:property value="#couponInfo.ruleName"/></span></li>
		<li><span><s:property value="#couponInfo.couponNo"/></span></li>
		<li>
			<span>
				<s:if test="#couponInfo.status=='AR'">
					<s:text name="NotUsed"/>
				</s:if>
				<s:elseif test="#couponInfo.status=='OK'"><s:text name="IsUsed"/></s:elseif>
				<s:elseif test="#couponInfo.status=='CA'"><s:text name="IsCancled"/></s:elseif>
				<s:elseif test="#couponInfo.status=='CK'"><s:text name="IsUsed2"/></s:elseif>
				<s:else><s:text name="notDefined"/></s:else>
			</span>
		</li>
		<li><span><s:property value="#couponInfo.createTime"/></span></li>
		<li><span><s:property value="#couponInfo.endTime"/></span></li>
		<li><span><s:property value="#couponInfo.finishTime"/></span></li>
		<li><span><s:property value="#couponInfo.relatedNoA"/></span></li>
		<li><span><s:property value="#couponInfo.relatedNoB"/></span></li>
		<li>
			<s:if test=" #couponInfo.endTime > #couponInfo.currentTime and #couponInfo.status=='AR' ">
				
				<a class="delete" onclick = "sendMsg('<s:property value="#couponInfo.couponNo"/>');"><span class="ui-icon icon-edit"></span><span class="button-text"><s:text name="SendMsg"/></span></a>
			</s:if>
			<s:else>&nbsp;</s:else>
		</li>
	</ul>
</s:iterator>
</div>
</s:i18n>
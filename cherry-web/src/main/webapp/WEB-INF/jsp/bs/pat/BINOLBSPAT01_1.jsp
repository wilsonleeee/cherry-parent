<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="cherry" uri="/cherry-tags"%> 
<%@ taglib prefix="s" uri="/struts-tags"%>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho" /></div>
<div id="iTotalRecords"><s:property value="iTotalRecords" /></div>
<div id="iTotalDisplayRecords"><s:property
	value="iTotalDisplayRecords" /></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.ss.BINOLBSPAT01_1">
	<div id="aaData"><s:iterator value="partnerList" id="partner">
	<s:url id="detailsUrl" action="BINOLBSPAT02_init">
			<%-- 单位Code --%>
			 <s:param name="partnerId">${partner.partnerId}</s:param>
		</s:url>
	
		<ul>
			<li><s:checkbox id="validFlag" name="validFlag" value="false"
				fieldValue="%{#paperId}" onclick="BINOLBSPAT01.checkSelect(this);" />
				<input type="hidden" id="partnerIdArr" name="partnerIdArr" value="<s:property value='partnerId' />"></input>
            </li>
			<%-- No. --%>
			<li><s:property value="RowNumber" /></li>
			<li>
			<%-- 编码  --%> <a href="${detailsUrl}" class="left"
				onclick="javascript:openWin(this);return false;"> 
			<s:property value="code" /></a></li>
			<li>
			<%-- 单位中文名称  --%> 
			 <s:property value="nameCn" /></li>
			<li>
			<%-- 单位外文名称 --%>
			<s:property value="nameEn" /></li>
			<li>
			<%-- 大区（区域） --%>
			<s:property value="region" /></li>
			<li>
			<%-- 省份--%>
			<s:property value="province" /></li>
			<li>
			<%-- 城市 --%>
			<s:property value="city" /></li>
			<li>
			<%-- 电话号码--%>
			<s:property value="phoneNumber" /></li>
			<li>
			<%-- 邮编 --%>
			<s:property value="postalCode" /></li>
			<li>
			<%-- 联系人 --%>
			<s:property value="contactPerson" /> </li>
			<li>
			<%-- 联系地址 --%>
			<s:property value="contactAddress" /> </li>
			<li>
			<%-- 办公地址 --%>
			<s:property value="address" /> </li>
			<li>
			<%-- 送货地址 --%>
			<s:property value="deliverAddress" /> </li>
			
			<%-- 有效区分 --%> 
			<li><s:if test='"1".equals(validFlag)'><span class='ui-icon icon-valid'></span>
			</s:if><s:else><span class='ui-icon icon-invalid'></span>
			</s:else></li>
			<s:url action="BINOLBSPATEDIT_init" id="updateInitUrl">
			 <s:param name="partnerId">${partner.partnerId}</s:param>
			</s:url>
			<li>
			<cherry:show domId="BINOLBSPAT0103">
			<a class="delete" href="${updateInitUrl }" onclick="openWin(this);return false;">
				<span class="ui-icon icon-edit"></span><span class="button-text"><s:text name="global.page.edit" /></span>
			</a>
			</cherry:show>
			</li>
		</ul>
	</s:iterator></div>
</s:i18n>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho" /></div>
<div id="iTotalRecords"><s:property value="iTotalRecords" /></div>
<div id="iTotalDisplayRecords"><s:property
	value="iTotalDisplayRecords" /></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.bs.BINOLBSRES01">
	<div id="aaData">
		<s:iterator value="resList" id="res">
			<s:url id="detailsUrl" action="BINOLBSRES02_init">
				<%-- 渠道名称   --%>
				<s:param name="resellerInfoId">${res.resellerInfoId}</s:param>
			</s:url>
			<ul>
				<li><s:checkbox id="validFlag" name="validFlag" value="false"
					fieldValue="%{#paperId}" onclick="BINOLBSRES01.checkSelect(this);" />
					<input type="hidden" id="resellerInfoIdArr" name="resellerInfoIdArr" value="<s:property value='resellerInfoId' />"></input>
	            </li>
				<li>
					<%--经销商名称 --%>
					<a href="${detailsUrl}" class="left" onclick="javascript:openWin(this);return false;">
						<s:property value="resellerCode"/>
					</a>
				</li>
				<li><span><s:property value="resellerName"/></span></li>
				<li><span><s:property value='#application.CodeTable.getVal("1314", #res.type)' /></span></li>	
				<li><span><s:property value='#application.CodeTable.getVal("1315", #res.levelCode)' /></span></li>				
				<li>
					<s:if test="%{parentResellerCode != null && parentResellerCode != ''}">
						（<s:property value="parentResellerCode"/>）
					</s:if>					
				</li>
				<li><s:property value="parentResellerName"/></li>
				<li><s:property value="regionName"/></li>				
				<li><s:property value="provinceName"/></li>
				<li><s:property value="cityName"/></li>			
				<li><s:property value="legalPerson"/></li>
				<li><s:property value="mobile"/></li>
				<li><s:property value="telePhone"/></li>
				<li>
					<s:if test='"1".equals(validFlag)'>
						<span class='ui-icon icon-valid'></span>
					</s:if><%-- 有效区分 --%> 
					<s:else>
						<span class='ui-icon icon-invalid'></span>
					</s:else>
				</li>														
			</ul>
		</s:iterator>
	</div>
</s:i18n>
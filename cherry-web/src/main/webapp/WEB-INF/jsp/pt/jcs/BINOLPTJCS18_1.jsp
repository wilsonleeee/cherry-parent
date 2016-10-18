<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho" /></div>
<div id="iTotalRecords"><s:property value="iTotalRecords" /></div>
<div id="iTotalDisplayRecords"><s:property
	value="iTotalDisplayRecords" /></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.pt.BINOLPTJCS18">
	<div id="aaData">
	<s:iterator value="solutionList" id="solution">
		<s:url id="detailsUrl" value="/pt/BINOLPTJCS19_init.action">
			<s:param name="productPriceSolutionID">${solution.productPriceSolutionID}</s:param>
			<s:param name="brandInfoId"><s:property value="brandInfoId"/></s:param>
		</s:url>
		<ul>
			<li><s:checkbox id="validFlag" name="validFlag" value="false" fieldValue="%{#paperId}" onclick="checkSelect(this);" />
				<input type="hidden" id="solutionIdArr" name="solutionIdArr" value="<s:property value='productPriceSolutionID' />"></input>
            </li>
			<%-- No. --%>
			<li><s:property value="RowNumber" /></li>
			<li><%-- 方案名称  --%> 
					<a href="${detailsUrl}" class="left" onclick="javascript:openWin(this);return false;"> 
				<%--
				<s:if test='"0".equals(isPosCloud)'>
					<a href="${detailsUrl}" class="left" onclick="javascript:openWin(this);return false;"> 
				</s:if>
				<s:elseif test='"1".equals(isPosCloud)'>
					<a href="${detailsUrl}" class="left" onclick="javascript:openWin(this,{width:window.screen.width-10,height:window.screen.height-35});return false;"> 
				</s:elseif>
				--%> 
				
				<s:property value="solutionName"/></a>
			</li>
			<li><span><s:property value="solutionCode" /></span></li><%-- 方案编码 --%>	
			<li><span><s:property value="comments"/></span></li><%-- 方案描述 --%>	
			<li><span><s:property value="startDate"/></span></li><%-- 方案生效日期 --%>	
			<li><span><s:property value="endDate"/></span></li><%-- 方案失效日期 --%>	
				
				
				
			<li><s:if test='"1".equals(validFlag)'>
				<span class='ui-icon icon-valid'></span>
			</s:if><%-- 有效区分 --%> <s:else>
				<span class='ui-icon icon-invalid'></span>
			</s:else></li>
		</ul>
	</s:iterator>
	</div>
</s:i18n>
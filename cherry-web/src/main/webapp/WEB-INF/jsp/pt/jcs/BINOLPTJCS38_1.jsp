<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho" /></div>
<div id="iTotalRecords"><s:property value="iTotalRecords" /></div>
<div id="iTotalDisplayRecords"><s:property
	value="iTotalDisplayRecords" /></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.pt.BINOLPTJCS38">
	<div id="aaData">
	<s:iterator value="prtFunList" id="prtFun">
		<s:url id="detailsUrl" value="/pt/BINOLPTJCS39_init.action">
			<s:param name="productFunctionID">${prtFun.productFunctionID}</s:param>
			<s:param name="brandInfoId"><s:property value="brandInfoId"/></s:param>
		</s:url>
		<ul>
			<li><s:checkbox id="validFlag" name="validFlag" value="false" fieldValue="%{#paperId}" onclick="checkSelect(this);" />
				<input type="hidden" id="prtFunIdArr" name="prtFunIdArr" value="<s:property value='productFunctionID' />"></input>
            </li>
			<%-- No. --%>
			<li><s:property value="RowNumber" /></li>
			<li> <%-- 产品功能开启时间名称 --%>
				<a href="${detailsUrl}" class="left" onclick="javascript:openWin(this);return false;"> 
					<s:property value="prtFunDateName"/> 
				</a>
			</li>	
			<li><%-- 产品类别  --%> 
				<span><s:property value='#application.CodeTable.getVal("1327",prtFunType)'/></span>
			</li>
			<li><span><s:property value="startDate"/></span></li><%-- 生效日期 --%>	
			<li><span><s:property value="endDate"/></span></li><%-- 失效日期 --%>	
				
				
				
			<li><s:if test='"1".equals(validFlag)'>
				<span class='ui-icon icon-valid'></span>
			</s:if><%-- 有效区分 --%> <s:else>
				<span class='ui-icon icon-invalid'></span>
			</s:else></li>
		</ul>
	</s:iterator>
	</div>
</s:i18n>
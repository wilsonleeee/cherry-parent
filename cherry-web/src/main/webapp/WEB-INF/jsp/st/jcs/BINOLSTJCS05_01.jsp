<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@page import="com.cherry.cm.core.CherryConstants"%>
<%String model = (String)request.getAttribute("configModel");%>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.st.BINOLSTJCS05">

<div id="aaData">
	<%
		if(model.equals(CherryConstants.DEPOTBUSINESS_REGION)){
	%>
		<s:iterator value="depotBusinessList" status="status">
		<ul>
			<%--选择 --%>
			<li> 
           <s:checkbox id="%{depotBusinessId}" name="validFlag" fieldValue="%{businessType}" onclick="binOLSTJCS05.checkSelect(this);"/>
        	</li>
			<%-- No. --%>
			<li>${RowNumber}</li>
			<%-- 出库仓库/区域--%>
			<li>
				<s:if test='null != outCode'>
					<span><s:property value='"("+outCode+")"+outName'/></span>
				</s:if>
				<s:else>
					<span><s:property value='outName'/></span>
				</s:else>
			</li>
			<%-- 入库仓库/区域  --%>
			<li>
				<s:if test='null != inCode'>
					<span><s:property value='"("+inCode+")"+inName'/></span>
				</s:if>
				<s:else>
					<span><s:property value='inName'/></span>
				</s:else>
			</li>
			<%-- 业务   --%>
			<li><span><s:property value='#application.CodeTable.getVal("1132", businessType)'/></span></li>
			<%-- 品牌  --%>
			<li><span><s:property value='brandName'/></span></li>
			<li>
				<cherry:show domId="BINOLSTJCS0502">
					 <a id="<s:property value='depotBusinessId'/>" class="delete" name="<s:property value='businessType'/>" onclick="binOLSTJCS05.popEditdialog(this);">
                		<span class="ui-icon icon-edit"></span>
                		<span class="button-text"><s:text name="JCS05_edit"/></span>
               	 	</a>
				</cherry:show>
			</li>
		</ul>
	</s:iterator>
	<%}else{%>
	<s:iterator value="depotBusinessList" status="status">
		<ul>
			<%-- No. --%>
			<li>${RowNumber}</li>
			<%-- 出库仓库/区域--%>
			<li><span><s:property value='"("+outCode+")"+outName'/></span></li>
			<%-- 入库仓库/区域  --%>
			<li><span><s:property value='"("+inCode+")"+inName'/></span></li>
			<%-- 业务   --%>
			<li><span><s:property value='#application.CodeTable.getVal("1132", businessType)'/></span></li>
			<%-- 品牌  --%>
			<li><span><s:property value='brandName'/></span></li>
		</ul>
	</s:iterator>
	<%} %>
	
</div>
</s:i18n>
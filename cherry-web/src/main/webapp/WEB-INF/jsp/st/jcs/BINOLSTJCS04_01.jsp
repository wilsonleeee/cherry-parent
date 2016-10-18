<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.st.BINOLSTJCS04">

<div id="aaData">
	<s:iterator value="ReaInvenDepartList" status="status">
		<ul>
			<%--选择 --%>
			<li> 
           <s:checkbox id="%{identityId+'*'+inventoryInfoId+'*'+organizationId+'*'+defaultFlag}" name="validFlag" fieldValue="%{comments}" onclick="binOLSTJCS04.checkSelect(this);"/>
        	</li>
			<%-- No. --%>
			<li>${RowNumber}</li>
			<%-- 仓库   --%>
			<li><span><s:property value='"("+inventoryCode+")"+inventoryName'/></span></li>
			<%-- 部门   --%>
			<li><span><s:property value='"("+departCode+")"+departName'/></span></li>
			<%-- 品牌--%>
			<li><span><s:property value='brandName'/></span></li>
			<%-- 默认仓库  --%>
			<li><span class="center">
				<s:if test="1==defaultFlag">
					<span ><s:property value='#application.CodeTable.getVal("1135",defaultFlag)'/></span>
				</s:if>
				<s:else>
					<span ><s:property value='#application.CodeTable.getVal("1135",defaultFlag)'/></span>
				</s:else>
				</span>
			</li>
			
			<%-- 备注  --%>
			<li><span><s:property value="comments"/></span></li>
			<%--操作 --%>
			<li>
			<cherry:show domId="BINOLSTJCS0402"> 
				<a id="<s:property value='identityId+"*"+inventoryInfoId+"*"+organizationId+"*"+defaultFlag+"*"+depotTestType+"*"+departTestType'/>" class="delete" onclick="binOLSTJCS04.popEditDialog(this);" value="<s:property value='comments'/>"> 
                	<span class="ui-icon icon-edit"></span>
                	<span class="button-text"><s:text name="JCS04_edit"/></span>
               	 </a>
            </cherry:show>
           </li>
		</ul>
	</s:iterator>
</div>
</s:i18n>
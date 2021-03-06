<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<link rel="stylesheet" href="/Cherry/css/cherry/combobox.css"
	type="text/css">
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.st.BINOLSTSFH10">
<div id="aaData">
	<s:iterator value="productParamList" status="status">
		<ul>
			<%-- No. --%>
			<li>${RowNumber}</li>
			<%-- 产品名称 --%>
			<li>
				<span style="font-size:13px"><s:property value='"("+unitCode+")"+productName'/></span>
			</li>
			<%-- 年月 --%>
			<li><span style="font-size:13px"><s:property value='year+month'/></span></li>
			<%-- 调整系数  --%>
			<li><span style="font-size:13px"><s:property value='adtCoefficient'/></span></li>
			<%-- 发布人 --%>
			<li>
			<span class="center">
				<a class="add" id="<s:property value='productOrderParameterId'/>_<s:property value='year+month'/>_<s:property value='adtCoefficient'/>" onclick="binOLSTSFH10.popEditDialog('prt',this);return false;">
                 	<span class="ui-icon icon-edit"></span>
                 	<span class="button-text" style="font-size:13px"><s:text name="SFH10_edit"></s:text></span>
             	</a>
            </span>
             </li>
		</ul>
	</s:iterator>
</div>
</s:i18n>
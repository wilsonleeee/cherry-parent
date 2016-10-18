<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<link rel="stylesheet" href="/Cherry/css/cherry/combobox.css" type="text/css">
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.st.BINOLSTSFH10">
<div id="aaData">
	<s:iterator value="globalParamList" status="status">
		<ul>
			<%-- No. --%>
			<li>${RowNumber}</li>
			<%-- 品牌名称 --%>
			<li><span style="font-size:13px"><s:property value='brandName'/></span></li>
			<%-- 月销售天数 --%>
			<li><span style="font-size:13px"><s:property value='saleDaysOfMonth'/></span></li>
			<!-- 建议发货数量 -->
			<li><span style="font-size:13px"><s:property value='daysOfProduct'/></span></li>
			<%-- 操作 --%>
			<li class="center">
			<span >
				<a class="add" onclick="binOLSTSFH10.popEditDialog('global',this);return false;" id="<s:property value='globalParameterId'/>_<s:property value='saleDaysOfMonth'/>_<s:property value='daysOfProduct'/>">
                    <span class="ui-icon icon-edit"></span>
                    <span class="button-text" style="font-size:13px"><s:text name="SFH10_edit"></s:text></span>
                </a>
            </span>
             </li>
		</ul>
	</s:iterator>
</div>
</s:i18n>
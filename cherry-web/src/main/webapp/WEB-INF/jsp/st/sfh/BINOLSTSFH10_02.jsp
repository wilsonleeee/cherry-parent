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
	<s:iterator value="counterParamList" status="status">
		<ul>
			<%-- No. --%>
			<li>${RowNumber}</li>
			<%-- 柜台名称 --%>
			<li>
				<span style="font-size:13px"><s:property value='"("+departCode+")"+departName'/></span>
			</li>
			<%-- 订货间隔 --%>
			<li><span style="font-size:13px"><s:property value='orderDays'/></span></li>
			<%-- 在途天数  --%>
			<li><span style="font-size:13px"><s:property value='intransitDays'/></span></li>
			<%-- 操作 --%>
			<li class="center">
			<span >
				<a class="add" onclick="binOLSTSFH10.popEditDialog('cut',this);return false;" id="<s:property value='counterOrderParameterId'/>_<s:property value='orderDays'/>_<s:property value='intransitDays'/>">
                  	<span class="ui-icon icon-edit"></span>
                  	<span class="button-text" style="font-size:13px"><s:text name="SFH10_edit"></s:text></span>
              	</a>
            </span>
             </li>
		</ul>
	</s:iterator>
</div>
</s:i18n>
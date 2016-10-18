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
	<s:iterator value="couPrtParamList" status="status">
		<ul>
			<%-- No. --%>
			<li>${RowNumber}</li>
			<%-- 柜台名称 --%>
			<li>
				<span style="font-size:13px;"><s:property value='"("+departCode+")"+departName'/></span>
			</li>
			<%-- 产品名称 --%>
			<li><span style="font-size:13px"><s:property value='"("+unitCode+")"+productName'/></span></li>
			<%-- 最低库存天数 --%>
			<li><span style="font-size:13px"><s:property value='lowestStockDays'/></span></li>
			<%-- 旬环比增长系数 --%>
			<li><span style="font-size:13px"><s:property value='growthFactor'/></span></li>
			<%-- 主推计划(调整系数) --%>
			<li><span style="font-size:13px"><s:property value='regulateFactor'/></span></li>
            <%-- 陈列数 --%>
            <li><span style="font-size:13px"><s:property value='exhibitQuantity'/></span></li>
			<%-- 操作 --%>
			<li class="center">
				<span> 
					<a class="add" onclick="binOLSTSFH10.popEditDialog('cutPrt',this);return false;"
						id="<s:property value='counterPrtOrParameterId'/>_<s:property value='lowestStockDays'/>_<s:property value='growthFactor'/>_<s:property value='regulateFactor'/>">
							<span class="ui-icon icon-edit"></span> <span class="button-text"
							style="font-size: 13px"><s:text name="SFH10_edit"></s:text></span>
					</a>
				</span>
			</li>
			</ul>
	</s:iterator>
</div>
</s:i18n>
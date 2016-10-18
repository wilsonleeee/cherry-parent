<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ page import="com.cherry.cm.core.CherryConstants"%>
<s:set id="DATE_PATTERN_24_HOURS"><%=CherryConstants.DATE_PATTERN_24_HOURS%></s:set>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho">
	<s:property value="sEcho" />
</div>
<div id="iTotalRecords">
	<s:property value="iTotalRecords" />
</div>
<div id="iTotalDisplayRecords">
	<s:property value="iTotalDisplayRecords" />
</div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.mo.BINOLMOMAN08">
	<div id="aaData">
		<s:iterator value="posConfigInfoList" id="machineinfo">
			<ul>
				<li><s:property value="RowNumber" /></li>
				<%-- No. --%>
				<li><s:property value="ConfigCode" /></li>
				<li><s:property value="ConfigNote" /></li>
				<li><s:property value="ConfigType" /></li>
				<li><s:property value="ConfigValue" /></li>
				<li><a id="51" class="delete"
					onclick="BINOLMOMAN08.MAN08EditPaper(<s:property value="BIN_PosConfigID"/>);return false;">
						<span class="ui-icon icon-edit"></span> <span class="button-text"><s:text
								name="MAN08_editPaper" /></span>
				</a></li>
			</ul>
		</s:iterator>
	</div>
</s:i18n>

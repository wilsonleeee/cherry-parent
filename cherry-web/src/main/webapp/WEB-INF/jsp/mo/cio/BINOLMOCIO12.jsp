<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>

<jsp:include page="/WEB-INF/jsp/mo/cio/BINOLMOCIO10.jsp" flush="true" />
<script type="text/javascript" src="/Cherry/js/mo/cio/BINOLMOCIO12.js"></script>
<s:i18n name="i18n.mo.BINOLMOCIO12">

<s:url id="editPaper_url" action="mo/cio/BINOLMOCIO12_saveCheckPaper"></s:url>
<span id="editPaper" style="display:none">${editPaper_url}</span>
<span id="paperId" style="display:none"></span>
<input type="hidden" value="<s:property value="gropStr" />" id="gropStr1" name="gropStr1"/>
<input type="hidden" value="<s:property value="paperStr" />" id="paperStr1" name="paperStr1"/>
<input type="hidden" value="<s:property value="queStr" />" id="queStr1" name="queStr1"/>

<script type="text/javascript">
$("#pageTitle").html('<s:text name="CIO12_title" />');
var groupStr = $("#gropStr1").val();
var groupArray = eval('(' +groupStr + ')');
var paperStr = $("#paperStr1").val();
var paperMap = eval('(' + paperStr + ')');
binOLMOCIO10.setGroupArray(groupArray);
var questionStr = $("#queStr1").val();
var questionArray = eval('(' + questionStr + ')');
binOLMOCIO10.setQuestionArray(questionArray);
binOLMOCIO10.setFlag("1");
binOLMOCIO10.showGroup();
$("#mainForm #brandInfoId").val(paperMap.brandName);
$("#mainForm #brandInfoId").prop("disabled",true);
$("#mainForm #paperName").val(paperMap.paperName);
$("#mainForm #maxPoint").val(paperMap.maxPoint);
$("#mainForm #startDate").val(paperMap.startDate);
$("#mainForm #endDate").val(paperMap.endDate);
$("#paperId").html(paperMap.paperId);
String.prototype.escapeHTML = function () {
	return this.replace(/&/g, '&amp;').replace(/>/g, '&gt;').replace(/</g, '&lt;').replace(/”/g, '&quot;');
};
</script>

</s:i18n>
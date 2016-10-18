<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ page import="com.cherry.cm.core.CherryConstants" %>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>


<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/bs/pos/BINOLBSPOS04.js"></script>
<script type="text/javascript">
/*
 * 全局变量定义
 */
var binolbspos03_global = {};
//是否需要解锁
binolbspos03_global.needUnlock = true;
window.onbeforeunload = function(){
	if (binolbspos03_global.needUnlock) {
		if (window.opener) {
			window.opener.unlockParentWindow();
		}
	}
};
function detailPosition() {
	binolbspos03_global.needUnlock=false;
	var tokenVal = $('#csrftoken',window.opener.document).val();
	$('#detailPosition').find("input[name='csrftoken']").val(tokenVal);
	$('#detailPosition').submit();
}
</script>



<s:set id="BRAND_INFO_ID_VALUE"><%=CherryConstants.BRAND_INFO_ID_VALUE %></s:set>



<s:i18n name="i18n.bs.BINOLBSPOS01">
<s:text name="select_default" id="select_default"></s:text>
<div class="main container clearfix">
<div class="panel ui-corner-all">
<div id="div_main">

<div class="panel-header">
  <h2><s:text name="editing"></s:text>：${positionInfo.positionName}</h2>
</div>

<div id="actionResultDisplay"></div>

<cherry:form onsubmit="savePosition();return false;" id="addPositionInfo" csrftoken="false">
<s:hidden name="positionId" value="%{positionInfo.positionId}"></s:hidden>
<s:hidden name="positionPath" value="%{positionInfo.positionPath}"></s:hidden>
<s:hidden name="higherPositionPath" value="%{positionInfo.higherPositionPath}"></s:hidden>
<s:hidden name="modifyTime" value="%{positionInfo.updateTime}"></s:hidden>
<s:hidden name="modifyCount" value="%{positionInfo.modifyCount}"></s:hidden>
<div class="panel-content clearfix">
  <div class="section">
    <div class="section-header">
    <strong><span class="ui-icon icon-ttl-section-info"></span><s:text name="base_title"></s:text></strong>
    </div>
    <div class="section-content">
      <table class="detail" cellpadding="0" cellspacing="0">
        <caption>
     		<s:text name="brandInfo"></s:text>：
     		<s:if test="%{positionInfo.brandInfoId == #BRAND_INFO_ID_VALUE}"><s:text name="PPL00006"></s:text></s:if>
		    <s:else>${positionInfo.brandNameChinese }</s:else>&nbsp;&nbsp;
		    <s:text name="organizationId"></s:text>：${positionInfo.departName}&nbsp;&nbsp;
		    <s:if test="%{higherPositionList != null && !higherPositionList.isEmpty()}">
     		<s:text name="directlyHigherPos"></s:text>：<s:select list="higherPositionList" name="path" listKey="path" listValue="positionName" value="%{positionInfo.higherPositionPath}"></s:select>
    		</s:if>
    	</caption>
        <tr>
          <th><s:text name="positionName"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
          <td><span><s:textfield name="positionName" cssClass="text" value="%{positionInfo.positionName}" maxlength="50"></s:textfield></span></td>
          <th><s:text name="categoryName"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
          <td><span><s:select list="positionCategoryList" name="positionCategoryId" listKey="positionCategoryId" listValue="ategoryName" headerKey="" headerValue="%{#select_default}" value="%{positionInfo.positionCategoryId}"></s:select></span></td>
        </tr>
        <tr>
          <th><s:text name="positionNameForeign"></s:text></th>
          <td><span><s:textfield name="positionNameForeign" cssClass="text" value="%{positionInfo.positionNameForeign}" maxlength="50"></s:textfield></span></td>
          <th><s:text name="isManager"></s:text></th>
          <td><span><s:select list='#application.CodeTable.getCodes("1038")' listKey="CodeKey" listValue="Value" name="isManager" headerKey="" headerValue="%{#select_default}" value="%{positionInfo.isManager}"></s:select></span></td> 
        </tr>
        <tr>
          <th><s:text name="positionDESC"></s:text></th>
          <td><span><s:textfield name="positionDESC" cssClass="text" value="%{positionInfo.positionDESC}" maxlength="200"></s:textfield></span></td>
          <th><s:text name="foundationDate"></s:text></th>
          <td><span><s:textfield name="foundationDate" cssClass="text" value="%{positionInfo.foundationDate}" cssStyle="width:80px;"></s:textfield></span></td>
        </tr>
        <tr>
          <th><s:text name="positionType"></s:text></th>
          <td><span><s:select list='#application.CodeTable.getCodes("1039")' listKey="CodeKey" listValue="Value" name="positionType" value="%{positionInfo.positionType}" headerKey="" headerValue="%{#select_default}" onchange="changePosType();"></s:select></span></td>
          <th id="resellerInfoIdTh" style="display: none;"><s:text name="resellerName"></s:text></th>
          <td id="resellerInfoIdTd" style="display: none;"><span><s:select list="resellerInfoList" listKey="resellerInfoId" listValue="resellerName" name="resellerInfoId" headerKey="" headerValue="%{#select_default}" value="%{positionInfo.resellerInfoId}"></s:select></span></td>
        </tr>
      </table>      
    </div>
  </div>   

<hr class="space" />
<div class="center">
  <s:url action="BINOLBSPOS03_update" id="addPositionUrl"></s:url>
  <a id="addPositionUrl" href="${addPositionUrl }" style="display: none;"></a>
  <button class="save" onclick="savePosition();return false;"><span class="ui-icon icon-save"></span><span class="button-text"><s:text name="save_button"></s:text></span></button>
  <s:if test='%{fromPage != null && fromPage != ""}'>
  <button class="back" type="button" onclick="detailPosition();return false;"><span class="ui-icon icon-back"></span><span class="button-text"><s:text name="global.page.back"/></span></button>
  </s:if>
  <button class="close" type="button" onclick="window.close();return false;"><span class="ui-icon icon-close"></span><span class="button-text"><s:text name="global.page.close"/></span></button>
</div>
 
</div>
</cherry:form>
<cherry:form action="BINOLBSPOS02_init" csrftoken="false" id="detailPosition">
  <input name="csrftoken" type="hidden"/>
  <s:hidden name="positionId" value="%{positionInfo.positionId}"></s:hidden>
</cherry:form> 


</div>
</div>
</div>  
</s:i18n>


















      
 
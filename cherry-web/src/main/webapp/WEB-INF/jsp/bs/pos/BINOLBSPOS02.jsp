<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ page import="com.cherry.cm.core.CherryConstants" %>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>



<script type="text/javascript">
$(function(){
	if (window.opener) {
       window.opener.lockParentWindow();
    }
});
/*
 * 全局变量定义
 */
var binolbspos02_global = {};
//是否需要解锁
binolbspos02_global.needUnlock = true;
window.onbeforeunload = function(){
	if (binolbspos02_global.needUnlock) {
		if (window.opener) {
			window.opener.unlockParentWindow();
		}
	}
};
function updatePosition() {
	binolbspos02_global.needUnlock=false;
	var tokenVal = $('#csrftoken',window.opener.document).val();
	$('#updatePosition').find("input[name='csrftoken']").val(tokenVal);
	$('#updatePosition').submit();
}
</script>



<s:set id="BRAND_INFO_ID_VALUE"><%=CherryConstants.BRAND_INFO_ID_VALUE %></s:set>



<s:i18n name="i18n.bs.BINOLBSPOS01">
<div class="main container clearfix">
<div class="panel ui-corner-all">
<div id="div_main">

<div class="panel-header">
  <h2>${positionInfo.positionName}</h2>
</div>

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
		    <s:if test='%{positionInfo.higherPositionName != null && positionInfo.higherPositionName != ""}'>
     		<s:text name="directlyHigherPos"></s:text>：${positionInfo.higherPositionName }
     		</s:if>
    	</caption>
        <tr>
          <th><s:text name="positionName"></s:text></th>
          <td><span>${positionInfo.positionName}</span></td>
          <th><s:text name="categoryName"></s:text></th>
          <td><span>${positionInfo.categoryName }</span></td>
        </tr>
        <tr>
          <th><s:text name="positionNameForeign"></s:text></th>
          <td><span>${positionInfo.positionNameForeign }</span></td>
          <th><s:text name="isManager"></s:text></th>
          <td><span><s:property value='#application.CodeTable.getVal("1038", positionInfo.isManager)' /></span></td> 
        </tr>
        <tr>
          <th><s:text name="positionDESC"></s:text></th>
          <td><span>${positionInfo.positionDESC}</span></td>
          <th><s:text name="foundationDate"></s:text></th>
          <td><span>${positionInfo.foundationDate}</span></td>
        </tr>
        <tr>
          <th><s:text name="positionType"></s:text></th>
          <td><span><s:property value='#application.CodeTable.getVal("1039", positionInfo.positionType)' /></span></td>
          <s:if test='positionInfo.positionType == "1" || positionInfo.positionType == "2"'>
          <th><s:text name="resellerName"></s:text></th>
          <td><span>${positionInfo.resellerName}</span></td>
          </s:if>
        </tr>
      </table>      
    </div>
  </div>   

<hr class="space" />
<cherry:form action="BINOLBSPOS03_init" csrftoken="false" id="updatePosition">
  <input name="csrftoken" type="hidden"/>
  <s:hidden name="positionId" value="%{positionInfo.positionId}"></s:hidden>
  <s:hidden name="fromPage" value="1"></s:hidden>
</cherry:form> 
<div class="center">
  <cherry:show domId="BSPOS0101UPDPO">
  <button class="save" onclick="updatePosition();return false;"><span class="ui-icon icon-edit-big"></span><span class="button-text"><s:text name="edit_button"></s:text></span></button> <%-- <button class="delete"><span class="ui-icon icon-delete-big2"></span><span class="button-text">停用</span></button>--%>
  </cherry:show>
  <button class="close" type="button" onclick="window.close();return false;"><span class="ui-icon icon-close"></span><span class="button-text"><s:text name="global.page.close"/></span></button>
</div>
 
</div>


</div>
</div>
</div>  
</s:i18n>


















      
 
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ page import="com.cherry.cm.core.CherryConstants" %>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>


<script type="text/javascript">
$(function(){
	if (window.opener) {
       window.opener.lockParentWindow();
    }
});
/*
 * 全局变量定义
 */
var binolbspos07_global = {};
//是否需要解锁
binolbspos07_global.needUnlock = true;
window.onbeforeunload = function(){
	if (binolbspos07_global.needUnlock) {
		if (window.opener) {
			window.opener.unlockParentWindow();
		}
	}
};
function updatePosCategory() {
	binolbspos07_global.needUnlock=false;
	var tokenVal = $('#csrftoken',window.opener.document).val();
	$('#updatePosCategory').find("input[name='csrftoken']").val(tokenVal);
	$('#updatePosCategory').submit();
}
</script>

<s:set id="BRAND_INFO_ID_VALUE"><%=CherryConstants.BRAND_INFO_ID_VALUE %></s:set>

<s:i18n name="i18n.bs.BINOLBSPOS06">
<div class="main container clearfix">
<div class="panel ui-corner-all">
<div id="div_main">

<div class="panel-header">
    <div class="clearfix"> 
    <span class="breadcrumb left"> <span class="ui-icon icon-breadcrumb"></span><s:text name="base_mng"></s:text>&nbsp;&gt;&nbsp;<s:text name="position_detail"></s:text> </span> 
    </div>
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
     		<s:if test="%{posCategoryInfo.brandInfoId == #BRAND_INFO_ID_VALUE}"><s:text name="PPL00006"></s:text></s:if>
		    <s:else><s:property value="posCategoryInfo.brandNameChinese"/></s:else>
    	</caption>
        <tr>
          <th><s:text name="categoryCode"></s:text></th>
          <td><span><s:property value="posCategoryInfo.categoryCode"/></span></td>
          <th><s:text name="categoryName"></s:text></th>
          <td><span><s:property value="posCategoryInfo.categoryName"/></span></td>
        </tr>
        <tr>
          <th><s:text name="posGrade"></s:text></th>
          <td><span><s:property value="posCategoryInfo.posGrade"/></span></td>
          <th><s:text name="categoryNameForeign"></s:text></th>
          <td><span><s:property value="posCategoryInfo.categoryNameForeign"/></span></td>
        </tr>
      </table>      
    </div>
  </div>   

<hr class="space" />
<cherry:form action="BINOLBSPOS08_init" csrftoken="false" id="updatePosCategory">
  <input name="csrftoken" type="hidden"/>
  <s:hidden name="positionCategoryId" value="%{posCategoryInfo.positionCategoryId}"></s:hidden>
</cherry:form> 
<div class="center">
  <cherry:show domId="BINOLBSPOS0604">
  <button class="save" onclick="updatePosCategory();return false;"><span class="ui-icon icon-edit-big"></span><span class="button-text"><s:text name="edit_button"></s:text></span></button> <%-- <button class="delete"><span class="ui-icon icon-delete-big2"></span><span class="button-text">停用</span></button>--%>
  </cherry:show>
  <button class="close" type="button" onclick="window.close();return false;"><span class="ui-icon icon-close"></span><span class="button-text"><s:text name="global.page.close"/></span></button>
</div>
</div>
</div>
</div>
</div>  
</s:i18n>
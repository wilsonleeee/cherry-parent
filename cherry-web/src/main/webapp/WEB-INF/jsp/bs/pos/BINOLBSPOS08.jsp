<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ page import="com.cherry.cm.core.CherryConstants" %>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>

<script type="text/javascript" src="/Cherry/js/bs/pos/BINOLBSPOS09.js"></script>
<script type="text/javascript">
/*
 * 全局变量定义
 */
var binolbspos08_global = {};
//是否需要解锁
binolbspos08_global.needUnlock = true;
window.onbeforeunload = function(){
	if (binolbspos08_global.needUnlock) {
		if (window.opener) {
			window.opener.unlockParentWindow();
		}
	}
};
function detailPosCategory() {
	binolbspos08_global.needUnlock=false;
	var tokenVal = $('#csrftoken',window.opener.document).val();
	$('#detailPosCategory').find("input[name='csrftoken']").val(tokenVal);
	$('#detailPosCategory').submit();
}
</script>


<s:set id="BRAND_INFO_ID_VALUE"><%=CherryConstants.BRAND_INFO_ID_VALUE %></s:set>


<s:i18n name="i18n.bs.BINOLBSPOS06">
<s:text name="select_default" id="select_default"></s:text>
<div class="main container clearfix">
<div class="panel ui-corner-all">
<div id="div_main">

<div class="panel-header">
    <div class="clearfix"> 
    <span class="breadcrumb left"> <span class="ui-icon icon-breadcrumb"></span><s:text name="base_mng"></s:text>&nbsp;&gt;&nbsp;<s:text name="position_edit"></s:text> </span> 
    </div>
  </div>

<div id="actionResultDisplay"></div>

<cherry:form onsubmit="savePosCategory();return false;" id="addPosCategoryInfo" csrftoken="false">
<s:hidden name="positionCategoryId" value="%{posCategoryInfo.positionCategoryId}"></s:hidden>
<s:hidden name="brandInfoId" value="%{posCategoryInfo.brandInfoId}"></s:hidden>
<s:hidden name="modifyTime" value="%{posCategoryInfo.updateTime}"></s:hidden>
<s:hidden name="modifyCount" value="%{posCategoryInfo.modifyCount}"></s:hidden>
<div class="panel-content clearfix">
  <div class="section" id="positionBasic">
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
          <th><s:text name="categoryName"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
          <td><span><s:textfield name="categoryName" cssClass="text" value="%{posCategoryInfo.categoryName}" maxlength="20"></s:textfield></span></td>
        </tr>
        <tr>
          <th><s:text name="posGrade"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
          <td><span><s:textfield name="posGrade" cssClass="text" value="%{posCategoryInfo.posGrade}"></s:textfield></span></td>
          <th><s:text name="categoryNameForeign"></s:text></th>
          <td><span><s:textfield name="categoryNameForeign" cssClass="text" value="%{posCategoryInfo.categoryNameForeign}" maxlength="20"></s:textfield></span></td>
        </tr>
      </table>      
    </div>
  </div> 

<hr class="space" />
<div class="center">
  <s:url action="BINOLBSPOS08_update" id="addPosCategoryUrl"></s:url>
  <a id="addPosCategoryUrl" href="${addPosCategoryUrl }" style="display: none;"></a>
  <button class="save" onclick="savePosCategory();return false;"><span class="ui-icon icon-save"></span><span class="button-text"><s:text name="save_button"></s:text></span></button>
  <button class="back" type="button" onclick="detailPosCategory();return false;"><span class="ui-icon icon-back"></span><span class="button-text"><s:text name="global.page.back"/></span></button>
  <button class="close" type="button" onclick="window.close();return false;"><span class="ui-icon icon-close"></span><span class="button-text"><s:text name="global.page.close"/></span></button>
</div>
 
</div>
</cherry:form>
<cherry:form action="BINOLBSPOS07_init" csrftoken="false" id="detailPosCategory">
  <input name="csrftoken" type="hidden"/>
  <s:hidden name="positionCategoryId" value="%{posCategoryInfo.positionCategoryId}"></s:hidden>
</cherry:form> 


</div>
</div>
</div>  
</s:i18n>


          
     
















      
 
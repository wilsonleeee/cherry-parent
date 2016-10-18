<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>

<script type="text/javascript" src="/Cherry/js/pl/plt/BINOLPLPLT03.js"></script>


<s:if test="privilegeTypeInfo != null">
<s:i18n name="i18n.pl.BINOLPLPLT01">
<s:text name="global.page.select" id="select_default"></s:text>
<div class="main container clearfix">
<div class="panel ui-corner-all">
<div id="div_main">

<div class="panel-header">
  <div class="clearfix"> 
    <span class="breadcrumb left"> <span class="ui-icon icon-breadcrumb"></span><s:text name="privilege_manage" />&nbsp;&gt;&nbsp;<s:text name="update_plt" /></span> 
  </div>
</div>

<div id="actionResultDisplay"></div>

<div class="panel-content clearfix">

  <div class="box3">
    <div><s:text name="privilegeConf"></s:text></div>
  </div>
 
  <form id="updatePltForm">
  <s:hidden name="privilegeTypeId"></s:hidden>
  <s:hidden name="category" value="%{privilegeTypeInfo.category}"></s:hidden>
  <s:hidden name="modifyTime" value="%{privilegeTypeInfo.updateTime}"></s:hidden>
  <s:hidden name="modifyCount" value="%{privilegeTypeInfo.modifyCount}"></s:hidden>
  <div class="section">
    <div class="section-content">
      <table class="detail" cellpadding="0" cellspacing="0">
    	<tr>
          <s:if test='%{privilegeTypeInfo.category == "0"}'>
          <th style="width: 15%"><s:text name="departType"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
          <td style="width: 85%" class="td_point"><span><s:select list='#application.CodeTable.getCodes("1000")' listKey="CodeKey" listValue="Value" name="departType" headerKey="" headerValue="%{select_default}" value="%{privilegeTypeInfo.departType}" cssStyle="width:180px;"></s:select></span></td>
          </s:if>
          <s:if test='%{privilegeTypeInfo.category == "1"}'>
          <th style="width: 15%"><s:text name="positionCategoryId"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
          <td style="width: 85%" class="td_point"><span><s:select list="posCategoryList" listKey="positionCategoryId" listValue="categoryName" name="positionCategoryId" headerKey="" headerValue="%{select_default}" value="%{privilegeTypeInfo.positionCategoryId}" cssStyle="width:180px;"></s:select></span></td>
          </s:if>
        </tr>
        <tr>
          <th style="width: 15%"><s:text name="businessType"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
          <td style="width: 85%" class="td_point"><span><s:select list='#application.CodeTable.getCodes("1048")' listKey="CodeKey" listValue="Value" name="businessType" headerKey="" headerValue="%{select_default}" value="%{privilegeTypeInfo.businessType}" cssStyle="width:180px;"></s:select></span></td>
        </tr>
        <tr>
          <th style="width: 15%"><s:text name="operationType"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
          <td style="width: 85%"><span><s:select list='#application.CodeTable.getCodes("1049")' listKey="CodeKey" listValue="Value" name="operationType" headerKey="" headerValue="%{select_default}" value="%{privilegeTypeInfo.operationType}" cssStyle="width:180px;"></s:select></span></td>
        </tr>
        <tr>
          <th style="width: 15%"><s:text name="privilegeType"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
          <td style="width: 85%"><span><s:select list='#application.CodeTable.getCodes("1050")' listKey="CodeKey" listValue="Value" name="privilegeType" headerKey="" headerValue="%{select_default}" value="%{privilegeTypeInfo.privilegeType}" cssStyle="width:180px;"></s:select></span></td>
        </tr>
        <tr>
          <th style="width: 15%"><s:text name="exclusive_title"></s:text></th>
          <td style="width: 85%"><span><s:checkbox name="exclusive" fieldValue="1" value="%{privilegeTypeInfo.exclusive}"></s:checkbox><s:text name="exclusive"></s:text></span></td>
        </tr>
      </table>      
    </div>
  </div> 
  </form>
  
  


<hr class="space" />
<div class="center">
  <s:a action="BINOLPLPLT03_update" id="updatePltUrl" cssStyle="display: none;"></s:a>
  <button class="save" onclick="plplt03_savePlt();return false;"><span class="ui-icon icon-save"></span><span class="button-text"><s:text name="global.page.save"></s:text></span></button>
  <button class="close" type="button" onclick="window.close();return false;"><span class="ui-icon icon-close"></span><span class="button-text"><s:text name="global.page.close"/></span></button>
</div>
 
</div>

</div>
</div>
</div>  
</s:i18n>
</s:if>
<s:else>
<jsp:include page="/WEB-INF/jsp/common/actionResultBody.jsp" flush="true" />
</s:else>

<%-- 实时刷新数据权限URL --%>
<s:url id="refreshPrivilegeUrl" value="/common/BINOLCMPL04_init">
	<s:param name="isReOrgRelPl" value="0"></s:param>
</s:url>
<s:hidden value="%{#refreshPrivilegeUrl}" id="refreshPrivilegeUrl"></s:hidden>
          
     
















      
 
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>


<script type="text/javascript" src="/Cherry/js/bs/dep/BINOLBSDEP07.js"></script>


<s:if test="organization != null">
<s:i18n name="i18n.bs.BINOLBSDEP06">
<div class="main container clearfix">
<div class="panel ui-corner-all">
<div id="div_main">

<div class="panel-header">
  <div class="clearfix"> 
    <span class="breadcrumb left"> <span class="ui-icon icon-breadcrumb"></span><s:text name="bsdep_title"></s:text>&nbsp;&gt;&nbsp;<s:property value="organization.orgNameChinese"/><s:text name="editing"></s:text> </span>
  </div>
</div>

<div id="actionResultDisplay"></div>

<cherry:form id="addOrg" csrftoken="false" onsubmit="bsdep07_saveOrg();return false;">
<s:hidden name="organizationInfoId" value="%{organization.organizationInfoId}"></s:hidden>
<s:hidden name="modifyTime" value="%{organization.updateTime}"></s:hidden>
<s:hidden name="modifyCount" value="%{organization.modifyCount}"></s:hidden>
<div class="panel-content clearfix">

<div class="section">
  <div class="section-content">
    <table class="detail" cellpadding="0" cellspacing="0">
  	<tr>
        <th><s:text name="orgCode"></s:text></th>
        <td><span><s:property value="organization.orgCode"/></span></td>
        <th><s:text name="orgNameChinese"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
        <td><span><s:textfield name="orgNameChinese" cssClass="text" maxlength="50" value="%{organization.orgNameChinese}"></s:textfield></span></td>
      </tr>
      <tr>
        <th><s:text name="orgNameShort"></s:text></th>
        <td><span><s:textfield name="orgNameShort" cssClass="text" maxlength="20" value="%{organization.orgNameShort}"></s:textfield></span></td>
        <th><s:text name="orgNameForeign"></s:text></th>
        <td><span><s:textfield name="orgNameForeign" cssClass="text" maxlength="50" value="%{organization.orgNameForeign}"></s:textfield></span></td>
      </tr>
      <tr>
        <th><s:text name="orgNameForeignShort"></s:text></th>
        <td><span><s:textfield name="orgNameForeignShort" cssClass="text" maxlength="20" value="%{organization.orgNameForeignShort}"></s:textfield></span></td>
      </tr>
    </table>      
  </div>
</div> 

<hr class="space" />
<div class="center">
  <s:a action="BINOLBSDEP08_update" id="addOrgUrl" cssStyle="display: none;"></s:a>
  <button class="save" onclick="bsdep07_saveOrg();return false;"><span class="ui-icon icon-save"></span><span class="button-text"><s:text name="global.page.save"></s:text></span></button>
  <button class="close" type="button" onclick="window.close();return false;"><span class="ui-icon icon-close"></span><span class="button-text"><s:text name="global.page.close"/></span></button>
</div>
 
</div>
</cherry:form>

</div>
</div>
</div>  
</s:i18n>
</s:if>
<s:else>
<jsp:include page="/WEB-INF/jsp/common/actionResultBody.jsp" flush="true" />
</s:else>

          
     
















      
 
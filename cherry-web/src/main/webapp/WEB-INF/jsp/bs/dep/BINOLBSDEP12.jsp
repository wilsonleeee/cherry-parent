<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>

<script type="text/javascript" src="/Cherry/js/bs/dep/BINOLBSDEP11.js"></script>



<s:i18n name="i18n.bs.BINOLBSDEP10">
<div class="main container clearfix">
<div class="panel ui-corner-all">
<div id="div_main">

<div class="panel-header">
  <div class="clearfix"> 
    <span class="breadcrumb left"> <span class="ui-icon icon-breadcrumb"></span><s:text name="bsdep_title"></s:text>&nbsp;&gt;&nbsp;<s:property value="brandInfo.brandNameChinese"/><s:text name="editing"></s:text> </span>
  </div>
</div>

<div id="actionResultDisplay"></div>

<cherry:form id="addBrand" csrftoken="false" onsubmit="bsdep11_saveBrand();return false;">
<s:hidden name="brandInfoId" value="%{brandInfo.brandInfoId}"></s:hidden>
<s:hidden name="oldBrandCode" value="%{brandInfo.brandCode}"></s:hidden>
<s:hidden name="modifyTime" value="%{brandInfo.updateTime}"></s:hidden>
<s:hidden name="modifyCount" value="%{brandInfo.modifyCount}"></s:hidden>
<div class="panel-content clearfix">

<div class="section">
  <div class="section-content">
    <table class="detail" cellpadding="0" cellspacing="0">
  	  <tr>
        <th><s:text name="brandCode"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
        <td><span><s:property value="brandInfo.brandCode"/></span></td>
        <th><s:text name="brandNameChinese"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
        <td><span><s:textfield name="brandNameChinese" cssClass="text" maxlength="50" value="%{brandInfo.brandNameChinese}"></s:textfield></span></td>
      </tr>
      <tr>
        <th><s:text name="brandNameShort"></s:text></th>
        <td><span><s:textfield name="brandNameShort" cssClass="text" maxlength="20" value="%{brandInfo.brandNameShort}"></s:textfield></span></td>
        <th><s:text name="brandNameForeign"></s:text></th>
        <td><span><s:textfield name="brandNameForeign" cssClass="text" maxlength="50" value="%{brandInfo.brandNameForeign}"></s:textfield></span></td>
      </tr>
      <tr>
        <th><s:text name="brandNameForeignShort"></s:text></th>
        <td><span><s:textfield name="brandNameForeignShort" cssClass="text" maxlength="20" value="%{brandInfo.brandNameForeignShort}"></s:textfield></span></td>
      </tr>
    </table>      
  </div>
</div>

<hr class="space" />
<div class="center">
  <s:a action="BINOLBSDEP12_update" id="addBrandUrl" cssStyle="display: none;"></s:a>
  <button class="save" onclick="bsdep11_saveBrand();return false;"><span class="ui-icon icon-save"></span><span class="button-text"><s:text name="global.page.save"></s:text></span></button>
  <button class="close" type="button" onclick="window.close();return false;"><span class="ui-icon icon-close"></span><span class="button-text"><s:text name="global.page.close"/></span></button>
</div>
 
</div>
</cherry:form>

</div>
</div>
</div>  
</s:i18n>

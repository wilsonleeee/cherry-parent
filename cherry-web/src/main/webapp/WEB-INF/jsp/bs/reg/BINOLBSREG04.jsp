<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<script type="text/javascript" src="/Cherry/js/bs/reg/BINOLBSREG04.js"></script>

<s:i18n name="i18n.bs.BINOLBSREG04">
<s:text name="global.page.select" id="select_default"/>
<div class="main container clearfix">
<div class="panel ui-corner-all">
<div id="div_main">

<div class="panel-header">
    <div class="clearfix"> 
    <span class="breadcrumb left"> <span class="ui-icon icon-breadcrumb"></span><s:text name="bsreg04_mng"></s:text>&nbsp;&gt;&nbsp;<s:text name="bsreg04_title"></s:text> </span>
	</div>
</div>

<div id="actionResultDisplay"></div>

<cherry:form id="addRegionInfo" class="inline" csrftoken="false">
<s:hidden name="ignoreFlg" value="0"></s:hidden>
<div class="panel-content clearfix">

<div class="section" id="regionBasic">
  <div class="section-header">
	<strong><span class="ui-icon icon-ttl-section-info"></span><s:text name="bsreg04_base_title"></s:text></strong>
  </div>
  <div class="section-content">
  <table class="detail" cellpadding="0" cellspacing="0">
	<tr>
	  <th><s:text name="bsreg04_regionCode"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
	  <td><span><s:textfield name="regionCode" cssClass="text" maxlength="10"></s:textfield></span></td>
	  <th><s:text name="bsreg04_regionNameCh"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
	  <td><span><s:textfield name="regionNameChinese" cssClass="text" maxlength="50"></s:textfield></span></td>
	</tr>
	<tr>
	  <th><s:text name="bsreg04_brand"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
	  <td><span><s:select name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" onchange="binolbsreg04.changeBrandInfo(this);"></s:select></span></td>
	  <th><s:text name="bsreg04_regionNameEn"></s:text></th>
	  <td><span><s:textfield name="regionNameForeign" cssClass="text" maxlength="50"></s:textfield></span></td>
	</tr>
	<tr>
	  <th><s:text name="bsreg04_zipCode"></s:text></th>
	  <td><span><s:textfield name="zipCode" cssClass="text" maxlength="10"></s:textfield></span></td>
	<th><s:text name="bsreg04_helpCode"></s:text></th>
      <td><span><s:textfield name="helpCode" cssClass="text" maxlength="10"></s:textfield></span></td>
	</tr>
	<tr>
      <th><s:text name="bsreg04_teleCode"></s:text></th>
	  <td><span><s:textfield name="teleCode" cssClass="text" maxlength="5"></s:textfield></span></td>
	</tr>
  </table>
  </div>
</div>


<%--=================== 选择所管辖的省份 ===================== --%>
<div class="section" id="higheRegionDiv">
	<div class="section-header clearfix">
    	<a class="add left" href="#" onclick="bscom06_popProvince(this,$('#brandInfoId').serialize());return false;">
			<span class="ui-icon icon-add"></span><span class="button-text"><s:text name="bsreg04_province" /></span>
		</a>
    </div>
    <div class="section-content">
	<table border="0" cellpadding="0" cellspacing="0" width="100%" style="margin-bottom: 10px;">
	  <thead>
	    <tr>
	      <th width="30%"><s:text name="bsreg04_higherRegCode" /></th>
	      <th width="30%"><s:text name="bsreg04_higherRegName" /></th>
	      <th width="30%"><s:text name="bsreg04_higherRegType" /></th>
	      <th width="10%"><s:text name="bsreg04_operation" /></th>
	    </tr>
	  </thead>
	  <tbody>
	  </tbody>
	</table>
  </div>
</div>
  
<hr class="space" />
<div class="center">
  <s:url action="BINOLBSREG04_save" id="addRegionUrl"></s:url>
  <button class="save" onclick="binolbsreg04.addRegion('${addRegionUrl }');return false;"><span class="ui-icon icon-save"></span><span class="button-text"><s:text name="bsreg04_saveButton"></s:text></span></button>
  <button class="close" type="button" onclick="window.close();return false;"><span class="ui-icon icon-close"></span><span class="button-text"><s:text name="global.page.close"/></span></button>
</div>

</div>
</cherry:form>

</div>
</div>
</div>
</s:i18n>

<jsp:include page="/WEB-INF/jsp/bs/common/BINOLBSCOM05.jsp" flush="true" />























         
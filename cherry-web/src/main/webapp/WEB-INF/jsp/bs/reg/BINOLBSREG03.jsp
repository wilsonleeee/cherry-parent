<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<script type="text/javascript" src="/Cherry/js/bs/reg/BINOLBSREG03.js"></script>

<s:i18n name="i18n.bs.BINOLBSREG03">
<s:text name="global.page.select" id="select_default"/>
<div class="main container clearfix">
<div class="panel ui-corner-all">
<div id="div_main">

<div class="panel-header">
    <div class="clearfix"> 
    <span class="breadcrumb left"> <span class="ui-icon icon-breadcrumb"></span><s:text name="bsreg03_mng"></s:text>&nbsp;&gt;&nbsp;<s:text name="bsreg03_title"></s:text> </span>
	</div>
</div>

<div id="actionResultDisplay"></div>

<cherry:form id="updateRegionInfo" class="inline" csrftoken="false">
<s:hidden name="ignoreFlg" value="0"></s:hidden>
<s:hidden name="regionId" value="%{regionInfo.regionId}"></s:hidden>
<s:hidden name="oldRegionCode" value="%{regionInfo.regionCode}"></s:hidden>
<s:hidden name="brandInfoId" value="%{regionInfo.brandInfoId}"></s:hidden>
<s:hidden name="regionPath" value="%{regionInfo.regionPath}"></s:hidden>
<s:hidden name="oldHigherPath" value="%{regionInfo.higherPath}"></s:hidden>
<s:hidden name="modifyTime" value="%{regionInfo.updateTime}"></s:hidden>
<s:hidden name="modifyCount" value="%{regionInfo.modifyCount}"></s:hidden>
<div class="panel-content clearfix">

<div class="section" id="regionBasic">
  <div class="section-header">
	<strong><span class="ui-icon icon-ttl-section-info"></span><s:text name="bsreg03_base_title"></s:text></strong>
  </div>
  <div class="section-content">
  <table class="detail" cellpadding="0" cellspacing="0">
  <s:if test='regionInfo.regionType=="0"'>
	<tr>
	  <th><s:text name="bsreg03_regionCode"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
	  <td><span><s:textfield name="regionCode" cssClass="text" maxlength="10" value="%{regionInfo.regionCode}"></s:textfield></span></td>
	  <th><s:text name="bsreg03_regionNameCh"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
	  <td><span><s:textfield name="regionNameChinese" cssClass="text" maxlength="50" value="%{regionInfo.regionNameCh}"></s:textfield></span></td>
	</tr>
   </s:if> 
   <s:else>
   <tr>
	 <th><s:text name="bsreg03_regionCode"></s:text></th>
	 <td><span><s:property value="regionInfo.regionCode"/></span></td>
	 <s:if test='!"".equals(regionInfo.regionCode)'>
	 <s:hidden name="regionCode" value="%{regionInfo.regionCode}"></s:hidden>
	 </s:if>
	 <th><s:text name="bsreg03_regionNameCh"></s:text></th>
	 <td><span><s:property value="regionInfo.regionNameCh" /></span></td>
	  <s:hidden name="regionNameChinese" value="%{regionInfo.regionNameCh}"></s:hidden>
	</tr>
   </s:else>  
	<tr>
	  <th><s:text name="bsreg03_regionType"></s:text></th>
	  <td><span><s:property value='#application.CodeTable.getVal("1151", regionInfo.regionType)' /></span></td>
	  <s:hidden name="regionType" value="%{regionInfo.regionType}"></s:hidden>
	   <s:if test='regionInfo.regionType=="0"'>
	  <th><s:text name="bsreg03_regionNameEn"></s:text></th>
	  <td><span><s:textfield name="regionNameForeign" cssClass="text" maxlength="50" value="%{regionInfo.regionNameEn}"></s:textfield></span></td>
	  </s:if>
	  <s:else>
	  <th><s:text name="bsreg03_regionNameEn"></s:text></th>
	  <td><span><s:property value="regionInfo.regionNameEn"/></span></td>
	  </s:else>
	</tr>
	<tr>
	  <th><s:text name="bsreg03_brand"></s:text></th>
	  <td><span><s:property value="%{regionInfo.brandName}"/></span></td>
	  <s:if test='regionInfo.regionType=="0"'>
	  <th><s:text name="bsreg03_zipCode"></s:text></th>
	  <td><span><s:textfield name="zipCode" cssClass="text" maxlength="10" value="%{regionInfo.zipCode}"></s:textfield></span></td>
	  </s:if>
	  <s:else>
	  <th><s:text name="bsreg03_zipCode"></s:text></th>
	  <td><span><s:property value="regionInfo.zipCode"/></span></td>
	  </s:else>
	</tr>
	<tr>
	 <s:if test='regionInfo.regionType=="0"'>
	  <th><s:text name="bsreg03_helpCode"></s:text></th>
      <td><span><s:textfield name="helpCode" cssClass="text" maxlength="10" value="%{regionInfo.helpCode}"></s:textfield></span></td>
      <th><s:text name="bsreg03_teleCode"></s:text></th>
	  <td><span><s:textfield name="teleCode" cssClass="text" maxlength="5" value="%{regionInfo.teleCode}"></s:textfield></span></td>
	</s:if>
	<s:else>
	<th><s:text name="bsreg03_helpCode"></s:text></th>
    <td><span><s:property value="regionInfo.helpCode"/></span></td>
    <th><s:text name="bsreg03_teleCode"></s:text></th>
    <td><span><s:property value="regionInfo.teleCode"/></span></td>
	</s:else>
	</tr>
  </table>
  </div>
</div>
 <s:if test='regionInfo.regionType=="0"'>
<%--=================== 选择所管辖的省份 ===================== --%>
<div class="section" id="higheRegionDiv">
	<div class="section-header clearfix">
    	<a class="add left" href="#" onclick="bscom06_popProvince(this,$('#brandInfoId').serialize());return false;">
			<span class="ui-icon icon-add"></span><span class="button-text"><s:text name="bsreg03_province" /></span>
		</a>
    </div>
    <div class="section-content">
	<table border="0" cellpadding="0" cellspacing="0" width="100%" style="margin-bottom: 10px;">
	  <thead>
	    <tr>
	      <th width="30%"><s:text name="bsreg03_higherRegCode" /></th>
	      <th width="30%"><s:text name="bsreg03_higherRegName" /></th>
	      <th width="30%"><s:text name="bsreg03_higherRegType" /></th>
	      <th width="10%"><s:text name="bsreg03_operation" /></th>
	    </tr>
	  </thead>
	  <tbody>
	    	<s:iterator value="provinceList" status="status">
	    <s:if test='regionCode!=null'>
	    	<s:hidden name="oldProvincePath" value="%{path}"></s:hidden>
	  <tr>
	      <td><s:property value="regionCode"/></td>
	      <td><s:property value="regionName"/></td>
	      <td><s:property value='#application.CodeTable.getVal("1151", regionType)' /></td>
	      <td class="center">
	    	<s:hidden name="provincePath" value="%{path}"></s:hidden>
			<a class="delete" onclick="bscom06_removeRegion(this);return false;"><span class="ui-icon icon-delete"></span><span class="button-text"><s:text name="bsreg03_deleteButton" /></span></a>
		  </td>
	    </tr>
	    </s:if>
	    </s:iterator>
	  </tbody>
	</table>
  </div>
</div>
<jsp:include page="/WEB-INF/jsp/bs/common/BINOLBSCOM05.jsp" flush="true" />
</s:if>
<s:else>
<%--=================== 上级区域 ===================== --%>
<div class="section" id="higheRegionDiv">
	<div class="section-header clearfix">
    	<a class="add left" href="#" onclick="bscom06_popDataTableOfRegion(this,$('#brandInfoId').serialize()+'&'+$('#regionType').serialize());return false;">
			<span class="ui-icon icon-add"></span><span class="button-text"><s:text name="bsreg03_higherRegion" /></span>
		</a>
    </div>
    <div class="section-content">
	<table border="0" cellpadding="0" cellspacing="0" width="100%" style="margin-bottom: 10px;">
	  <thead>
	    <tr>
	      <th width="30%"><s:text name="bsreg03_higherRegCode" /></th>
	      <th width="30%"><s:text name="bsreg03_higherRegName" /></th>
	      <th width="30%"><s:text name="bsreg03_higherRegType" /></th>
	      <th width="10%"><s:text name="bsreg03_operation" /></th>
	    </tr>
	  </thead>
	  <tbody>
	  	<s:if test='%{regionInfo.higherPath != null && !"".equals(regionInfo.higherPath)}'>
	  	<tr>
	      <td><s:property value="regionInfo.higherRegionCode"/></td>
	      <td><s:property value="regionInfo.higherRegionName"/></td>
	      <td><s:property value='#application.CodeTable.getVal("1151", regionInfo.higherRegionType)' /></td>
	      <td class="center">
	    	<s:hidden name="higherPath" value="%{regionInfo.higherPath}"></s:hidden>
			<a class="delete" onclick="bscom06_removeRegion(this);return false;"><span class="ui-icon icon-delete"></span><span class="button-text"><s:text name="bsreg03_deleteButton" /></span></a>
		  </td>
	    </tr>
	    </s:if>
	  </tbody>
	</table>
  </div>
</div>
</s:else>
<hr class="space" />
<div class="center">
  <s:url action="BINOLBSREG03_update" id="updateRegionUrl"></s:url>
  <button class="save" onclick="binolbsreg03.updateRegion('${updateRegionUrl }');return false;"><span class="ui-icon icon-save"></span><span class="button-text"><s:text name="bsreg03_saveButton"></s:text></span></button>
  <button class="close" type="button" onclick="window.close();return false;"><span class="ui-icon icon-close"></span><span class="button-text"><s:text name="global.page.close"/></span></button>
</div>
<jsp:include page="/WEB-INF/jsp/bs/common/BINOLBSCOM04.jsp" flush="true" />
</div>
</cherry:form>

</div>
</div>
</div>
</s:i18n>


























         
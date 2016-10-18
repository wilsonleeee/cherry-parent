<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>



<s:i18n name="i18n.bs.BINOLBSREG02">
  <div class="section">
    <div class="section-header">
    <strong><span class="ui-icon icon-ttl-section-info"></span><s:text name="bsreg02_title"></s:text></strong>
    </div>
    
    <div class="section-content">
    	<table class="detail" cellpadding="0" cellspacing="0">
	        <tr>
		      <th><s:text name="bsreg02_regionCode"></s:text></th>
		      <td><span><s:property value="regionInfo.regionCode"/></span></td>
		      <th><s:text name="bsreg02_regionNameCh"></s:text></th>
			  <td><span><s:property value="regionInfo.regionNameCh" /></span></td>
		    </tr>
		    <tr>
		      <th><s:text name="bsreg02_regionType"></s:text></th>
		      <td><span><s:property value='#application.CodeTable.getVal("1151", regionInfo.regionType)' /></span></td>
		      <th><s:text name="bsreg02_regionNameEn"></s:text></th>
		      <td><span><s:property value="regionInfo.regionNameEn"/></span></td>
		    </tr>
		    <tr>
		      <th><s:text name="bsreg02_brand"></s:text></th>
		      <td><span><s:property value="regionInfo.brandName"/></span></td>
		      <th><s:text name="bsreg02_zipCode"></s:text></th>
		      <td><span><s:property value="regionInfo.zipCode"/></span></td>
		    </tr>
			<tr>
			  <th><s:text name="bsreg02_helpCode"></s:text></th>
		      <td><span><s:property value="regionInfo.helpCode"/></span></td>
		      <th><s:text name="bsreg02_teleCode"></s:text></th>
		      <td><span><s:property value="regionInfo.teleCode"/></span></td>
			</tr>
		</table>
    </div>
  </div>
  
  <hr class="space" />
  <div class="center">
  <s:if test="%{regionInfo.regionType == 1||regionInfo.regionType == 0}">
  	<cherry:show domId="BINOLBSREG0102">
  	<s:url action="BINOLBSREG03_init" id="updateRegionUrl">
	  <s:param name="regionId" value="%{regionInfo.regionId}"></s:param>
	</s:url>
	<a href="${updateRegionUrl }" id="updateRegionUrl" style="display: none;"></a>
	<button class="save" onclick="openWin('#updateRegionUrl');return false;">
	  <span class="ui-icon icon-edit-big"></span>
	  <span class="button-text"><s:text name="bsreg02_editButton"></s:text></span>
	</button>
	</cherry:show>
	</s:if>
	<s:url action="BINOLBSREG05_updRegValidFlag" id="updRegValidFlagUrl">
      <s:param name="regionId" value="%{regionInfo.regionId}"></s:param>
    </s:url>
    <s:hidden name="regionId" value="%{regionInfo.regionId}"/>
	<s:if test="%{regionInfo.validFlag == 1}">
   		<cherry:show domId="BINOLBSREG0104">
   		<button class="edit" onclick="bsreg01_editValidFlag('disable','${updRegValidFlagUrl }');return false;">
       		<span class="ui-icon icon-delete-big"></span>
       		<span class="button-text"><s:text name="global.page.disable"/></span>
       	</button>
       	</cherry:show>
   	</s:if>
   	<s:else>
   		<cherry:show domId="BINOLBSREG0103">
   		<button class="edit" onclick="bsreg01_editValidFlag('enable','${updRegValidFlagUrl }');return false;">
       		<span class="ui-icon icon-success"></span>
       		<span class="button-text"><s:text name="global.page.enable"/></span>
       	</button>
       	</cherry:show>
   	</s:else>
  </div>
</s:i18n>


    
 
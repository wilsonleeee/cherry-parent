<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  

<input type="text" class="text" onKeyup ="datatableFilter(this,70);" id="searchKey"/>
<a class="search" onclick="datatableFilter('#searchKey',70);return false;"><span class="ui-icon icon-search"></span><span class="button-text"><s:text name="global.page.searchfor"/></span></a>
<hr class="space" />
<s:if test='%{param2 == null || "".equals(param2)}'>
<div class="ui-tabs">

<ul class ="ui-tabs-nav clearfix">
  <li id="0" class="ui-tabs-selected" onclick="changeTab(this, 70, 'campaignMode');"><a><s:text name="global.page.campaignMode0"/></a></li>
  <li id="1" onclick="changeTab(this, 70, 'campaignMode');"><a><s:text name="global.page.campaignMode1"/></a></li>
</ul>
</s:if>
<div class="right" style="margin: -25px 15px 0 0;">
<input type="radio" value="1" name="campaignValid" onclick="changeValid(this,70);" checked="checked">
<span><s:text name="global.page.valid" /></span>
<input type="radio" value="0" name="campaignValid" onclick="changeValid(this,70);">
<span><s:text name="global.page.invalid" /></span>
</div>
<s:if test='%{param2 == null || "".equals(param2)}'>
<div class="ui-tabs-panel">
</s:if>
<table id="searchCampaignDataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
    <thead>
         <tr>
            <th>
            	<s:if test='%{checkType == "checkbox"}'>
               		<input type="checkbox" id="camp_checkAll"/>
              	</s:if>
            	<s:text name="global.page.Popselect"></s:text>
            </th>
            <th><s:text name="global.page.campaignName"></s:text></th>
            <th><s:text name="global.page.campaignCode"></s:text></th>
            <th><s:text name="global.page.campaignType"></s:text></th>
         </tr>
     </thead>
     <tbody id="searchCampaignDataBody">
     </tbody>
</table>
<s:if test='%{param2 == null || "".equals(param2)}'>
</div>
</div>
</s:if>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />

<div class="hide">
<div id="searchCampaignTitle"><s:text name="global.page.selectCampaign" /></div>
<div id="dialogConfirm"><s:text name="global.page.dialogConfirm" /></div>
<div id="dialogCancel"><s:text name="global.page.dialogCancel" /></div>
<s:url action="BINOLCM02_popCampaignDialog3" id="searchCampaignListUrl"></s:url>
<a href="${searchCampaignListUrl }" id="searchCampaignListUrl"></a>
</div>
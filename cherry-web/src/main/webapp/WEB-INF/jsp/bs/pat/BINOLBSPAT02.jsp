<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<script type="text/javascript" src="/Cherry/js/bs/pat/BINOLBSPAT02.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<s:i18n name="i18n.bs.BINOLBSPAT02">
<div class="main container clearfix">
<div class="panel ui-corner-all">
<div id="div_main">
<div class="panel-header">
<cherry:form id="toEditForm" action="BINOLBSPAT03_init" method="post" csrftoken="false">
        	<input type="hidden" name="partnerId" value='<s:property value="partnerId"/>'/>
        	<input type="hidden" name="csrftoken" id="parentCsrftoken"/>
</cherry:form>
<div class="clearfix"> 
    <span class="breadcrumb left"> <span class="ui-icon icon-breadcrumb"></span><s:text name="PAT02_title1" />&nbsp;&gt;&nbsp;<s:text name="PAT02_detail"></s:text></span> 
  </div>
</div>
<div class="panel-content clearfix">
<div class="section">
  <div class="section-header">
	<strong><span class="ui-icon icon-ttl-section-info"></span><s:text name="PAT02_detail"></s:text></strong>
  </div>
  <div class="section-content" id="detail">
	<table class="detail" cellpadding="0" cellspacing="0">
		<tr>
			<th><s:text name="PAT02_brandName"></s:text></th>
			<td><span><s:property value="partnerDetail.brandName" /></span></td>
			<th><s:text name="partnerCode"></s:text></th>
			<td><span><s:property value="partnerDetail.code" /></span></td>
		</tr>
		<tr>
			<th><s:text name="PAT02_region"></s:text></th>
			<td><span><s:property value="partnerDetail.region" /></span></td>
			<th><s:text name="partnerNameCn"></s:text></th>
			<td><span><s:property value="partnerDetail.nameCn" /></span></td>
		</tr>
		<tr>
			<th><s:text name="PAT02_province"></s:text></th>
			<td><span><s:property value="partnerDetail.province" /></span></td>
			<th><s:text name="partnerNameEn"></s:text></th>
			<td><span><s:property value="partnerDetail.nameEn" /></span></td>
		</tr>
		<tr>
			<th><s:text name="PAT02_city"></s:text></th>
			<td><span><s:property value="partnerDetail.city" /></span></td>
			<th><s:text name="partnerPhoneNumber"></s:text></th>
			<td><span><s:property value="partnerDetail.phoneNumber" /></span></td>
		</tr>
		<tr>
			<th><s:text name="PAT02_county"></s:text></th>
			<td><span><s:property value="partnerDetail.county" /></span></td>
			<th></th>
			<td></td>
		</tr>
		<tr>
			<!-- 联系人 -->
			<th><s:text name="PAT02_contactPerson"></s:text></th>
			<td><span><s:property value="partnerDetail.contactPerson"/></span></td>
			<th><s:text name="partnerPostalCode"></s:text></th>
			<td><span><s:property value="partnerDetail.postalCode" /></span></td>
		</tr>
		<tr>
			<!-- 联系地址 -->
			<th><s:text name="PAT02_contactAddress"></s:text></th>
			<td colspan="3"><span><s:property value="partnerDetail.contactAddress"/></span></td>
		</tr>
		<tr>
			<!-- 送货地址 -->
			<th><s:text name="PAT02_deliverAddress"></s:text></th>
			<td colspan="3"><span><s:property value="partnerDetail.deliverAddress"/></span></td>
		</tr>
		<tr>
			<!-- 办公地址 -->
			<th><s:text name="partnerAddress"></s:text></th>
			<td colspan="3"><span><s:property value="partnerDetail.address"/></span></td>
		</tr>
	</table>    
  </div>
</div> 
<div class="center">
<button class="close" type="button" onclick="window.close();return false;"><span class="ui-icon icon-close"></span><span class="button-text"><s:text name="global.page.close"/></span></button>
</div>
</div>
</div>
</div>
</div>
</s:i18n> 
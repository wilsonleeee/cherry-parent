<!-- 编辑往来单位 -->
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<script type="text/javascript" src="/Cherry/js/bs/common/BINOLBSCOM03.js"></script>
<script type="text/javascript" src="/Cherry/js/bs/pat/BINOLBSPAT02_1.js"></script>
<s:i18n name="i18n.bs.BINOLBSPAT02">
<s:text name="global.page.select" id="select_default"/>
<div class="main container clearfix">
<div class="panel ui-corner-all">
<div id="div_main">
	<div class="panel-header">
	  <div class="clearfix"> 
	    <span class="breadcrumb left"> <span class="ui-icon icon-breadcrumb"></span><s:text name="PAT02_title1"></s:text>&nbsp;&gt;&nbsp;<s:text name="PAT02_edit"></s:text> </span>
	  </div>
	</div>
	<div id="actionResultDisplay"></div>
	
	<cherry:form id="update" csrftoken="false" onsubmit="savepartner();return false;">
	<input type="hidden" id="partnerId" name="partnerId" value="<s:property value='partnerId' />"></input>
	<div class="panel-content clearfix">
		<div class="section">
		  <div class="section-content">
		    <table class="detail" cellpadding="0" cellspacing="0">
		  	  <tr>
		  	  	<th><s:text name="PAT02_brandName"></s:text></th>
				<td><span><s:property value="partnerDetail.brandName" /></span></td>
		        <th><s:text name="PAT02_Code"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
		        <td><span><s:textfield name="code" cssClass="text" value="%{partnerDetail.code}" maxlength="15"/></span></td>
		      </tr>
		      <tr>
		      	<th><s:text name="PAT02_province"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
				<td><span>
					<a id="province" class="ui-select" style="margin-left: 0px; font-size: 12px;">
					<span id="provinceText" class="button-text" style="min-width:100px;text-align:left;line-height: 1.4;">
					<s:if test='%{partnerDetail.province != null && !"".equals(partnerDetail.province)}'><s:property value="partnerDetail.province"/></s:if>
					<s:else><s:text name="global.page.select"/></s:else>
					</span>
					<span class="ui-icon ui-icon-triangle-1-s"></span>
					</a>
					<s:hidden name="regionId" id="regionId" value="%{partnerDetail.regionId}"/>
					<s:hidden name="provinceId" id="provinceId" value="%{partnerDetail.provinceId}"/>
				</span></td>
				<th><s:text name="PAT02_Name_CN"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
		        <td><span><s:textfield name="nameCn" cssClass="text" value="%{partnerDetail.nameCn}" maxlength="50"></s:textfield></span></td>
		      </tr>
		      <tr>
		      	<th><s:text name="PAT02_city"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
				<td><span>
					<a id="city" class="ui-select" style="margin-left: 0px; font-size: 12px;">
					<span id="cityText" class="button-text" style="min-width:100px;text-align:left;line-height: 1.4;">
					<s:if test='%{partnerDetail.city != null && !"".equals(partnerDetail.city)}'><s:property value="partnerDetail.city"/></s:if>
					<s:else><s:text name="global.page.select"/></s:else>
					</span>
					<span class="ui-icon ui-icon-triangle-1-s"></span>
					</a>
					<s:hidden name="cityId" id="cityId" value="%{partnerDetail.cityId}"/>
				</span></td>
				<th><s:text name="PAT02_Name_EN"></s:text></th>
				<td><span><s:textfield name="nameEn" cssClass="text" value="%{partnerDetail.nameEn}" maxlength="50"></s:textfield></span></td>
		      </tr>
		      <tr>
				<th><s:text name="PAT02_county"></s:text></th>
				<td><span>
					<a id="county" class="ui-select" style="margin-left: 0px; font-size: 12px;">
					<span id="countyText" class="button-text" style="min-width:100px;text-align:left;line-height: 1.4;">
					<s:if test='%{partnerDetail.county != null && !"".equals(partnerDetail.county)}'><s:property value="partnerDetail.county"/></s:if>
					<s:else><s:text name="global.page.select"/></s:else>
					</span>
					<span class="ui-icon ui-icon-triangle-1-s"></span>
					</a>
					<s:hidden name="countyId" id="countyId" value="%{partnerDetail.countyId}"/>
				</span></td>
				<th><s:text name="partnerPhoneNumber"></s:text></th>
				<td><span><s:textfield name="phoneNumber" cssClass="text" maxlength="20" cssStyle="width:180px" value="%{partnerDetail.phoneNumber}"></s:textfield></span></td>
			  </tr>
			  <tr>
			  	<!-- 联系人 -->
			  	<th><s:text name="PAT02_contactPerson"></s:text></th>
		        <td><span><s:textfield name="contactPerson" cssClass="text" maxlength="50" value="%{partnerDetail.contactPerson}"></s:textfield></span></td>
		        <th><s:text name="partnerPostalCode"></s:text></th>
				<td><span><s:textfield name="postalCode" cssClass="text" value="%{partnerDetail.postalCode}" maxlength="6"></s:textfield></span></td>
			  </tr>
			  <tr>
			  	<!-- 联系地址 -->
		        <th><s:text name="PAT02_contactAddress"></s:text></th>
		        <td colspan="3"><span><s:textfield name="contactAddress" cssClass="text" maxlength="200" cssStyle="width:900px" value="%{partnerDetail.contactAddress}"></s:textfield></span></td>
			  </tr>
			  <tr>
			  	<!-- 送货地址 -->
		        <th><s:text name="PAT02_deliverAddress"></s:text></th>
		        <td colspan="3"><span><s:textfield name="deliverAddress" cssClass="text" maxlength="200" cssStyle="width:900px" value="%{partnerDetail.deliverAddress}"></s:textfield></span></td>
			  </tr>
			  <tr>
			  	<!-- 办公地址 -->
		        <th><s:text name="partnerAddress"></s:text></th>
				<td colspan="3"><span><s:textfield name="address" cssStyle="width:900px" cssClass="text" maxlength="200" value="%{partnerDetail.address}"></s:textfield></span></td>
			  </tr>
		    </table>      
		  </div>
		</div>
		<hr class="space" />
		<div class="center">
		  <s:a action="BINOLBSPAT02_edit" id="editUrl" cssStyle="display: none;"></s:a>
		  <button class="save" onclick="savepartner();return false;"><span class="ui-icon icon-save"></span><span class="button-text"><s:text name="global.page.save"></s:text></span></button>
		  <button class="close" type="button" onclick="window.close();return false;"><span class="ui-icon icon-close"></span><span class="button-text"><s:text name="global.page.close"/></span></button>
		</div>
	</div>
	</cherry:form>
</div>
</div>
</div>

<%-- ================== 省市选择DIV START ======================= --%>
<div id="provinceTemp" class="ui-option hide" style="width:325px;">
	<div class="clearfix">
		<span class="label"><s:text name="global.page.range"></s:text></span>
		<ul class="u2"><li onclick="bscom03_getNextRegin(this, '${select_default }', 1);return false;"><s:text name="global.page.all"></s:text></li></ul>
	</div>
	<s:iterator value="reginList" id="reginMap">
    	<div class="clearfix"><span class="label"><s:property value="reginName"/></span>
    	<ul class="u2">
     		<s:iterator value="%{#reginMap.provinceList}">
         		<li id="<s:property value="%{#reginMap.reginId}" />_<s:property value="provinceId" />" onclick="bscom03_getNextRegin(this, '${select_default }', 1);">
         			<s:property value="provinceName"/>
         		</li>
      		</s:iterator>
      	</ul>
    	</div>
   	</s:iterator>
</div>
<div id="cityTemp" class="ui-option hide">
	<s:if test="%{cityList != null && !cityList.isEmpty()}">
	<ul class="u2">
		<li onclick="bscom03_getNextRegin(this, '${select_default }', 2);"><s:text name="global.page.all"></s:text></li>
		<s:iterator value="cityList">
			<li id="<s:property value="regionId" />" onclick="bscom03_getNextRegin(this, '${select_default }', 2);"><s:property value="regionName"/></li>
		</s:iterator>
	</ul>
	</s:if>
</div>
<div id="countyTemp" class="ui-option hide">
	<s:if test="%{countyList != null && !countyList.isEmpty()}">
	<ul class="u2">
		<li onclick="bscom03_getNextRegin(this, '${select_default }', 3);"><s:text name="global.page.all"></s:text></li>
		<s:iterator value="countyList">
			<li id="<s:property value="regionId" />" onclick="bscom03_getNextRegin(this, '${select_default }', 3);"><s:property value="regionName"/></li>
		</s:iterator>
	</ul>
	</s:if>
</div>
<%-- 下级区域查询URL --%>
<s:url id="querySubRegionUrl" value="/common/BINOLCM00_querySubRegion"></s:url>
<s:hidden name="querySubRegionUrl" value="%{querySubRegionUrl}"/>
<span id="defaultTitle" class="hide"><s:text name="global.page.range"></s:text></span>
<span id="defaultText" class="hide"><s:text name="global.page.all"></s:text></span>
<%-- ================== 省市选择DIV  END  ======================= --%>
  
</s:i18n>


          
     
















      
 
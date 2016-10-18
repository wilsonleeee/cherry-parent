<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<script type="text/javascript" src="/Cherry/js/bs/common/BINOLBSCOM03.js"></script>
<script type="text/javascript" src="/Cherry/js/bs/pat/BINOLBSPAT02.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<s:i18n name="i18n.bs.BINOLBSPAT02">
<s:text name="global.page.select" id="select_default"/>
<div class="main container clearfix">
<div class="panel ui-corner-all">
<div id="div_main">
<div class="panel-header">
  <div class="clearfix"> 
    <span class="breadcrumb left"> <span class="ui-icon icon-breadcrumb"></span><s:text name="PAT02_title1" />&nbsp;&gt;&nbsp;<s:text name="PAT02_add"></s:text></span> 
  </div>
</div>
<div id="actionResultDisplay"></div>
<div class="panel-content clearfix">
<div class="section">
  <div class="section-header">
	<strong><span class="ui-icon icon-ttl-section-info"></span><s:text name="PAT02_baseInfo"></s:text></strong>
  </div>
  <div class="section-content">
  <cherry:form id="add" csrftoken="false" onsubmit="save();return false;">
    <table class="detail" cellpadding="0" cellspacing="0">
  	  <tr>
  	  	<th><s:text name="PAT02_brandName"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
		<td><span><s:select name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" onchange="changeBrandInfo(this,'%{#select_default}')"></s:select></span></td>
  	   	<th><s:text name="PAT02_Code"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
        <td><span><s:textfield name="code" cssClass="text" maxlength="15"></s:textfield></span></td>
     </tr>
	 <tr>
	      <th><s:text name="PAT02_province"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
	      <td><span>
	        <a id="province" class="ui-select" style="margin-left: 0px; font-size: 12px;">
	              	<span id="provinceText" class="button-text choice"><s:text name="global.page.select"/></span>
	   		 		<span class="ui-icon ui-icon-triangle-1-s"></span>
	   		 	</a>
	   		 	<s:hidden name="regionId" id="regionId"/>
	   		 	<s:hidden name="provinceId" id="provinceId"/>
	      </span></td>
	      <th><s:text name="PAT02_Name_CN"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
          <td><span><s:textfield name="nameCn" cssClass="text" maxlength="50"></s:textfield></span></td>
      </tr>
	  <tr>
	  <th><s:text name="PAT02_city"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
		  <td><span>
		    <a id="city" class="ui-select" style="margin-left: 0px; font-size: 12px;">
	              	<span id="cityText" class="button-text choice"><s:text name="global.page.select"/></span>
	   		 		<span class="ui-icon ui-icon-triangle-1-s"></span>
	   		 	</a>
	   		 	<s:hidden name="cityId" id="cityId"/>
		  </span></td>
		  <th><s:text name="PAT02_Name_EN"></s:text></th>
          <td><span><s:textfield name="nameEn" cssClass="text" maxlength="50"></s:textfield></span></td>
	  </tr>
	  <tr>
		  <th><s:text name="PAT02_county"></s:text></th>
		  <td><span>
		    <a id="county" class="ui-select" style="margin-left: 0px; font-size: 12px;">
	              	<span id="countyText" class="button-text choice"><s:text name="global.page.select"/></span>
	   		 		<span class="ui-icon ui-icon-triangle-1-s"></span>
	   		 	</a>
	   		 	<s:hidden name="countyId" id="countyId"/>
		  </span></td>
		  <th><s:text name="PAT02_phoneNumber"></s:text></th>
          <td><span><s:textfield name="phoneNumber" cssClass="text" maxlength="20"></s:textfield></span></td>
	  </tr>
	  <tr>
	  	<!-- 联系人 -->
	  	<th><s:text name="PAT02_contactPerson"></s:text></th>
        <td><span><s:textfield name="contactPerson" cssClass="text" maxlength="50"></s:textfield></span></td>
        <th><s:text name="PAT02_postalCode"></s:text></th>
        <td><span><s:textfield name="postalCode" cssClass="text" maxlength="6"></s:textfield></span></td>
	  </tr>
	  <tr>
	  	<!-- 联系地址 -->
        <th><s:text name="PAT02_contactAddress"></s:text></th>
        <td colspan="3"><span><s:textfield name="contactAddress" cssClass="text" maxlength="200" cssStyle="width:900px"></s:textfield></span></td>
	  </tr>
	  <tr>
	  	<!-- 送货地址 -->
        <th><s:text name="PAT02_deliverAddress"></s:text></th>
        <td colspan="3"><span><s:textfield name="deliverAddress" cssClass="text" maxlength="200" cssStyle="width:900px"></s:textfield></span></td>
	  </tr>
	  <tr>
	  	<!-- 办公地址 -->
        <th><s:text name="partnerAddress"></s:text></th>
        <td colspan="3"><span><s:textfield name="address" cssClass="text" maxlength="200" cssStyle="width:900px"></s:textfield></span></td>
	  </tr>
    </table>    
 </cherry:form>  
  </div>
</div> 
<hr class="space" />
<div class="center">
  <s:a action="BINOLBSPAT02_save" id="PAT02_save" cssStyle="display: none;"></s:a>
  <button class="save" onclick="save();return false;"><span class="ui-icon icon-save"></span><span class="button-text"><s:text name="global.page.save"></s:text></span></button>
  <button class="close" type="button" onclick="window.close();return false;"><span class="ui-icon icon-close"></span><span class="button-text"><s:text name="global.page.close"/></span></button>
</div>
</div>
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
<div id="cityTemp" class="ui-option hide"></div>
<div id="countyTemp" class="ui-option hide"></div>
<%-- 下级区域查询URL --%>
<s:url id="querySubRegionUrl" value="/common/BINOLCM00_querySubRegion"></s:url>
<s:hidden name="querySubRegionUrl" value="%{querySubRegionUrl}"/>
<span id="defaultTitle" class="hide"><s:text name="global.page.range"></s:text></span>
<span id="defaultText" class="hide"><s:text name="global.page.all"></s:text></span>
<%-- ================== 省市选择DIV  END  ======================= --%>

<%-- 根据当前品牌ID筛选下拉列表URL --%>
<s:url id="filter_url" value="BINOLBSPAT02_filter"/>
<s:hidden name="filter_url" value="%{filter_url}"/>
</s:i18n> 
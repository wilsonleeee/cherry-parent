<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ page import="com.cherry.cm.core.CherryConstants" %>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>

<script type="text/javascript" src="/Cherry/js/bs/common/BINOLBSCOM03.js"></script>
<script type="text/javascript" src="/Cherry/js/bs/dep/BINOLBSDEP04.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-timepicker-addon.js"></script>
<link rel="stylesheet" href="/Cherry/css/cherry/cherry_timepicker.css" type="text/css">

<script type="text/javascript">
$(function(){
	$('#counterInfo').find('.divItem').first().append($('#regin-city-subCity-div').html());
	var provinceVal = $('#counterInfo').find(':input[name=province]').val();
	var cityVal = $('#counterInfo').find(':input[name=city]').val();
	var countyVal = $('#counterInfo').find(':input[name=county]').val();
	if(provinceVal) {
		var $province = $('#counterInfo').find('div.provinceDiv').find('li[id='+provinceVal+']');
		getCity($province[0],cityVal,countyVal);
	}
	$('#departAddress').find('.divItem').each(function(){
		$(this).append($('#regin-city-subCity-div').html());
		var provinceVal_addr = $(this).find(':input[name=province]').val();
		var cityVal_addr = $(this).find(':input[name=city]').val();
		if(provinceVal_addr) {
			var $province = $(this).find('div.provinceDiv').find('li[id='+provinceVal_addr+']');
			getCity($province[0],cityVal_addr);
		}
	});
	
});
/*
 * 全局变量定义
 */
var binolbsdep03_global = {};
//是否需要解锁
binolbsdep03_global.needUnlock = true;
window.onbeforeunload = function(){
	if (binolbsdep03_global.needUnlock) {
		if (window.opener) {
			window.opener.unlockParentWindow();
		}
	}
};
function detailOrganization() {
	binolbsdep03_global.needUnlock=false;
	var tokenVal = $('#csrftoken',window.opener.document).val();
	$('#detailOrganization').find("input[name='csrftoken']").val(tokenVal);
	$('#detailOrganization').submit();
}
</script>



<s:set id="BRAND_INFO_ID_VALUE"><%=CherryConstants.BRAND_INFO_ID_VALUE %></s:set>


<s:i18n name="i18n.bs.BINOLBSDEP01">
<div id="timeOnlyTitle" class="hide"><s:text name="global.timepicker.timeOnlyTitle"/></div>
<div id="currentText" class="hide"><s:text name="global.timepicker.currentText"/></div>
<div id="closeText" class="hide"><s:text name="global.timepicker.closeText"/></div>
<s:text name="select_default" id="select_default"></s:text>
<div class="main container clearfix">
<div class="panel ui-corner-all">
<div id="div_main">

<div class="panel-header">
    <div class="clearfix"> 
    <span class="breadcrumb left"> <span class="ui-icon icon-breadcrumb"></span><s:text name="base_mng"></s:text>&nbsp;&gt;&nbsp;<s:text name="editing"></s:text> </span>
	</div>
</div>

<div id="actionResultDisplay"></div>

<cherry:form onsubmit="saveWithValid();return false;" id="addOrganizationInfo" csrftoken="false">
<div class="panel-content clearfix">

<div class="section" id="departBasic">
<s:hidden name="organizationId" value="%{organizationInfo.organizationId}"></s:hidden>
<s:hidden name="brandInfoId" value="%{organizationInfo.brandInfoId}"></s:hidden>
<s:hidden name="higherDepartPath" value="%{organizationInfo.higherDepartPath}"></s:hidden>
<s:hidden name="departPath" value="%{organizationInfo.departPath}"></s:hidden>
<s:hidden name="modifyTime" value="%{organizationInfo.updateTime}"></s:hidden>
<s:hidden name="modifyCount" value="%{organizationInfo.modifyCount}"></s:hidden>
<s:iterator value="departAddressList" id="departAddress">
<s:hidden name="oldAddressInfoId" value="%{#departAddress.addressInfoId}"></s:hidden>
</s:iterator>
<s:iterator value="departContactList" id="departContact">
<s:hidden name="oldContactInfoId" value="%{#departContact.employeeId}"></s:hidden>
</s:iterator>
<div class="section-header">
<strong><span class="ui-icon icon-ttl-section-info"></span><s:text name="base_title"></s:text></strong>
</div>
<div class="section-content">
  <table class="detail" cellpadding="0" cellspacing="0">
    <tr>
	  <th><s:text name="departCode"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
	  <td><span><s:textfield name="departCode" cssClass="text" value="%{organizationInfo.departCode}" maxlength="15"></s:textfield></span></td>
	  <th><s:text name="testType"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
	  <td><span>
	    <s:if test="%{organizationInfo.testType != null && organizationInfo.testType == 0}"><s:text name="testType0"></s:text></s:if>
        <s:if test="%{organizationInfo.testType != null && organizationInfo.testType == 1}"><s:text name="testType1"></s:text></s:if>
	    <s:hidden name="testType" value="%{organizationInfo.testType}"></s:hidden>
	  </span></td>
	</tr>
	<tr>
	  <th><s:text name="type"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
	  <td><span><s:select name="type" list='#application.CodeTable.getCodesWithFilter("1000","0|1|4")' listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{#select_default}" value="%{organizationInfo.type}"></s:select></span></td>
	  <th><s:text name="departName"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
	  <td><span><s:textfield name="departName" cssClass="text" value="%{organizationInfo.departName}" maxlength="30"></s:textfield></span></td>
	</tr>
	<tr>
	  <th><s:text name="brandInfo"></s:text></th>
      <td><span>
        <s:if test="%{organizationInfo.brandInfoId == #BRAND_INFO_ID_VALUE}"><s:text name="PPL00006"></s:text></s:if>
      	<s:else><s:property value="organizationInfo.brandNameChinese"/></s:else>
      </span></td>
      <th><s:text name="departNameShort"></s:text></th>
	  <td><span><s:textfield name="departNameShort" cssClass="text" value="%{organizationInfo.departNameShort}" maxlength="20"></s:textfield></span></td>
	</tr>
	<tr>
	  <th><s:text name="provinceName"></s:text></th>
      <td><span>
        <a id="province" class="ui-select" style="margin-left: 0px; font-size: 12px;">
          	<span id="provinceText" class="button-text" style="min-width:100px;text-align:left;line-height: 1.4;">
          	<s:if test='%{organizationInfo.provinceName != null && !"".equals(organizationInfo.provinceName)}'><s:property value="organizationInfo.provinceName"/></s:if>
		    <s:else><s:text name="global.page.select"/></s:else>
          	</span>
	 		<span class="ui-icon ui-icon-triangle-1-s"></span>
	 	</a>
	 	<s:hidden name="regionId" id="regionId" value="%{organizationInfo.regionId}"/>
	 	<s:hidden name="provinceId" id="provinceId" value="%{organizationInfo.provinceId}"/>
      </span></td>
      <th><s:text name="nameForeign"></s:text></th>
	  <td><span><s:textfield name="nameForeign" cssClass="text" value="%{organizationInfo.nameForeign}" maxlength="30"></s:textfield></span></td>
	</tr>
	<tr>
	  <th><s:text name="cityName"></s:text></th>
	  <td><span>
	    <a id="city" class="ui-select" style="margin-left: 0px; font-size: 12px;">
          	<span id="cityText" class="button-text" style="min-width:100px;text-align:left;line-height: 1.4;">
          	<s:if test='%{organizationInfo.cityName != null && !"".equals(organizationInfo.cityName)}'><s:property value="organizationInfo.cityName"/></s:if>
		    <s:else><s:text name="global.page.select"/></s:else>
          	</span>
	 		<span class="ui-icon ui-icon-triangle-1-s"></span>
	 	</a>
	 	<s:hidden name="cityId" id="cityId" value="%{organizationInfo.cityId}"/>
	  </span></td>
	  <th><s:text name="nameShortForeign"></s:text></th>
	  <td><span><s:textfield name="nameShortForeign" cssClass="text" value="%{organizationInfo.nameShortForeign}" maxlength="20"></s:textfield></span></td>
	</tr>
	<tr>
	  <th><s:text name="countyName"></s:text></th>
	  <td><span>
	    <a id="county" class="ui-select" style="margin-left: 0px; font-size: 12px;">
          	<span id="countyText" class="button-text" style="min-width:100px;text-align:left;line-height: 1.4;">
          	<s:if test='%{organizationInfo.countyName != null && !"".equals(organizationInfo.countyName)}'><s:property value="organizationInfo.countyName"/></s:if>
		    <s:else><s:text name="global.page.select"/></s:else>
          	</span>
	 		<span class="ui-icon ui-icon-triangle-1-s"></span>
	 	</a>
	 	<s:hidden name="countyId" id="countyId" value="%{organizationInfo.countyId}"/>
	  </span></td>
	  <th><s:text name="status"></s:text></th>
	  <td><span><s:select list='#application.CodeTable.getCodes("1030")' listKey="CodeKey" listValue="Value" name="status" headerKey="" headerValue="%{#select_default}" value="%{organizationInfo.status}"></s:select></span></td>
	</tr>
	<tr>
	<%-- 到期日 --%>
	<th><s:text name="counter.expiringDate"></s:text></th>
	<%-- <td><span><s:textfield id="expiringDate" name="expiringDate" value="%{organizationInfo.expiringDate}" cssClass="date" cssStyle="width:150px;"/></span></td>--%>
	<td>
		<span><s:textfield id="expiringDateDate" name="expiringDateDate" cssClass="date" value="%{organizationInfo.expiringDateDate}"/></span>
		<span style='margin-left:10px;'><s:textfield id="expiringDateTime" name="expiringDateTime" cssClass="date" value="%{organizationInfo.expiringDateTime}" /></span>
	</td>
	<%-- 柜台协同区分 --%>
	<s:if test="maintainOrgSynergy">
		<th><s:text name="depart.orgSynergyFlag"></s:text></th>
		<td>
			<span>
				<s:select
					 list='#application.CodeTable.getCodes("1331")' listKey="CodeKey" listValue="Value" name="orgSynergyFlag" value="%{organizationInfo.orgSynergyFlag}">
				</s:select>
			</span>
		</td>
	</s:if>
	<s:else>					
		<th></th>
		<td></td>
	</s:else>
	</tr>	
  </table>
</div>

</div>

<%--=================== 上级部门 ===================== --%>
<div class="section" id="higheOrgDiv">
	<div class="section-header clearfix">
    	<a class="add left" href="#" onclick="bscom05_popDataTableOfHigherDepart(this,$('#brandInfoId').serialize()+'&'+$('#type').serialize());return false;">
			<span class="ui-icon icon-add"></span><span class="button-text"><s:text name="higherOrganization" /></span>
		</a>
    </div>
    <div class="section-content">
	<table border="0" cellpadding="0" cellspacing="0" width="100%" style="margin-bottom: 10px;">
	  <thead>
	    <tr>
	      <th width="30%"><s:text name="departCode" /></th>
	      <th width="30%"><s:text name="departName" /></th>
	      <th width="30%"><s:text name="type" /></th>
	      <th width="10%"><s:text name="operation" /></th>
	    </tr>
	  </thead>
	  <tbody>
	    <s:if test="%{organizationInfo.higherDepartPath != null}">
	    <tr>
  			<td><s:property value="organizationInfo.higherDepartCode"/></td>
  			<td><s:property value="organizationInfo.higherDepartName"/></td>
  			<td><s:property value='#application.CodeTable.getVal("1000", organizationInfo.higherType)' /></td>
  			<td class="center">
		    	<s:hidden name="path" value="%{organizationInfo.higherDepartPath}"></s:hidden>
				<a class="delete" onclick="bscom05_removeDepart(this);return false;"><span class="ui-icon icon-delete"></span><span class="button-text"><s:text name="delete_button" /></span></a>
			</td>
 			</tr>
 		</s:if>
	  </tbody>
	</table>
  </div>
</div>

<%--=================== 下级柜台 ===================== --%>
<div class="section" id="lowerCounterDiv">
  <div class="section-header clearfix">
   	<a class="add left" href="#" onclick="bscom05_popDataTableOfLowerCounter(this,$('#brandInfoId').serialize()+'&'+$('#departPath').serialize());return false;">
		<span class="ui-icon icon-add"></span><span class="button-text"><s:text name="depart.addLowerCounter" /></span>
	</a>
  </div>
  <div class="section-content">
	<table border="0" cellpadding="0" cellspacing="0" width="100%" style="margin-bottom: 10px;">
	  <thead>
	    <tr>
	      <th width="30%"><s:text name="counter.departCode" /></th>
	      <th width="30%"><s:text name="counter.departName" /></th>
	      <th width="30%"><s:text name="counter.departType" /></th>
	      <th width="10%"><s:text name="counter.operation" /></th>
	    </tr>
	  </thead>
	  <tbody>
	  </tbody>
	</table>
  </div>
</div>

<div class="section" id="departAddress">
    <div class="section-header clearfix">
    <a class="add left" onclick="addAddress(this);return false;"><span class="ui-icon icon-add"></span><span class="button-text"><s:text name="addAddress_button"></s:text></span></a>
    </div>
    <div class="section-content">
      <s:iterator value="departAddressList" id="departAddress" status="status">
      <div class="divItem">  
      <s:hidden name="addressInfoId" value="%{#departAddress.addressInfoId}"></s:hidden>
      <s:hidden name="province" value="%{#departAddress.province}"></s:hidden>
      <s:hidden name="city" value="%{#departAddress.city}"></s:hidden>
      <table class="detail" cellpadding="0" cellspacing="0">
        <caption>
        	<span class="left">
          	  <s:text name="addressType"></s:text>：<s:select list='#application.CodeTable.getCodes("1027")' listKey="CodeKey" listValue="Value" name="addressTypeId" value="%{#departAddress.addressTypeId}"></s:select>
          	  <input type="checkbox" name="defaultFlag" value="1" <s:if test="%{#departAddress.defaultFlag == 1}">checked</s:if> onclick="changeDefaulAdd(this);" /> <s:text name="defaultFlag"></s:text>
          	</span>
          	<a class="delete right" href="javascript:void(0)" onclick="removeAddress(this);"><span class="ui-icon icon-delete"></span><span class="button-text"><s:text name="delete_button"></s:text></span></a>
        </caption>
        <tr>
          <th><s:text name="addressLine1"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
          <td><span><s:textfield name="addressLine1" cssClass="text" value="%{#departAddress.addressLine1}" maxlength="100"></s:textfield></span></td>
          <th><s:text name="provinceName"></s:text></th>
          <td><span>
          <a class="ui-select ui-select-1" style="margin-left: 0px;" onclick="changeRegin(this,'provinceDiv');return false;">
            <span class="button-text provinceText" style="width:100px;text-align:left;"><s:text name="select_default"/></span>
  		 	<span class="ui-icon ui-icon-triangle-1-s"></span>
  		  </a>
          </span></td>
        </tr>
        <tr>
          <th><s:text name="addressLine2"></s:text></th>
          <td><span><s:textfield name="addressLine2" cssClass="text" value="%{#departAddress.addressLine2}" maxlength="100"></s:textfield></span></td>
          <th><s:text name="cityName"></s:text></th>
          <td><span>
          <a class="ui-select ui-select-2" style="margin-left: 0px;" onclick="changeRegin(this,'cityDiv');return false;">
            <span class="button-text cityText" style="width:100px;text-align:left;"><s:text name="select_default"/></span>
 		 	<span class="ui-icon ui-icon-triangle-1-s"></span>
 		  </a>
          </span></td>
        </tr>
        <tr>
          <th><s:text name="zipCode"></s:text></th>
          <td><span><s:textfield name="zipCode" cssClass="text" value="%{#departAddress.zipCode}" maxlength="10"></s:textfield></span></td>
          <%--<th><s:text name="locationGPS"></s:text></th>
          <td><span><s:textfield name="locationGPS" cssClass="text" value="%{#departAddress.locationGPS}"></s:textfield></span></td> --%>
        </tr>
      </table>
      </div>
      </s:iterator>
    </div>
</div>

<div class="section" id="departContact">
    <div class="section-header clearfix">
    <a class="add left" onclick="addContact(this);return false;"><span class="ui-icon icon-add"></span><span class="button-text"><s:text name="addContact_button"></s:text></span></a>
    </div>
    <div class="section-content">
      <s:iterator value="departContactList" id="departContact" status="status">
      <table class="detail" cellpadding="0" cellspacing="0">
        <caption>
        	<span class="left">
          	  <input type="checkbox" name="defaultFlag" value="1" <s:if test="%{#departContact.defaultFlag == 1}">checked</s:if> onclick="changeDefaulContact(this);" /> <s:text name="defaultFlag"></s:text>
          	</span>
          	<a class="delete right" href="" onclick="removeContact(this);return false;"><span class="ui-icon icon-delete"></span><span class="button-text"><s:text name="delete_button"></s:text></span></a>
        </caption>
        <tr>
          <th><s:text name="employeeNam"></s:text></th>
          <td>
          <s:hidden name="employeeId" value="%{#departContact.employeeId}"></s:hidden>
          <span id="employeeName" class="left" style="word-wrap:break-word;overflow:hidden;width:220px;"><s:property value="#departContact.employeeName"/></span>
          <a onclick="bscom05_popDataTableOfDepartEmp(this,$('#brandInfoId').serialize());return false;" class="popup right" href=""><span class="ui-icon icon-search"></span><s:text name="selectContact"></s:text></a>   
          </td>
          <th><s:text name="phone"></s:text></th>
          <td><span id="phone"><s:property value="#departContact.phone"/></span></td>
        </tr>
        <tr>
          <th><s:text name="mobilePhone"></s:text></th>
          <td><span id="mobilePhone"><s:property value="#departContact.mobilePhone"/></span></td>
          <th><s:text name="email"></s:text></th>
          <td><span id="email"><s:property value="#departContact.email"/></span></td>
        </tr>
      </table>
      </s:iterator>
    </div>
</div>

<hr class="space" />
<div class="center">
  <button class="save" onclick="saveWithValid();return false;"><span class="ui-icon icon-save"></span><span class="button-text"><s:text name="save_button"></s:text></span></button>
  <s:if test='%{fromPage != null && fromPage != ""}'>
  <button class="back" type="button" onclick="detailOrganization();return false;"><span class="ui-icon icon-back"></span><span class="button-text"><s:text name="global.page.back"/></span></button>
  </s:if>
  <button class="close" type="button" onclick="window.close();return false;"><span class="ui-icon icon-close"></span><span class="button-text"><s:text name="global.page.close"/></span></button>
</div>

</div>
</cherry:form>
<cherry:form action="BINOLBSDEP02_init" csrftoken="false" id="detailOrganization">
  <input name="csrftoken" type="hidden"/>
  <s:hidden name="organizationId" value="%{organizationInfo.organizationId}"></s:hidden>
</cherry:form> 


</div>
</div>
</div>

<div style="display: none">
<span id="selectInitText"><s:text name="select_default"/></span>
<s:url action="BINOLBSDEP03_update" id="addOrganizationUrl"></s:url>
<a id="addOrganizationUrl" href="${addOrganizationUrl }"></a>
</div>
<%-- ================== 部门地址DIV  START  ======================= --%>
<div id="departAddressTemp" style="display: none">
	<div class="divItem">  
	  <input name="province" type="hidden"/>
	  <input name="city" type="hidden"/>
	  <table class="detail" cellpadding="0" cellspacing="0">
        <caption>
        	<span class="left">
        	<s:text name="addressType"></s:text>：<s:select list='#application.CodeTable.getCodes("1027")' listKey="CodeKey" listValue="Value" name="addressTypeId"></s:select>
           	<input type="checkbox" name="defaultFlag" value="1" onclick="changeDefaulAdd(this);" /> <s:text name="defaultFlag"></s:text>
           	</span>
           	<a class="delete right" href="javascript:void(0)" onclick="removeAddress(this);"><span class="ui-icon icon-delete"></span><span class="button-text"><s:text name="delete_button"></s:text></span></a>
        </caption>
        <tr>
          <th><s:text name="addressLine1"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
          <td><span><s:textfield name="addressLine1" cssClass="text" maxlength="100"></s:textfield></span></td>
          <th><s:text name="provinceName"></s:text></th>
          <td><span>
          <a class="ui-select ui-select-1" style="margin-left: 0px;" onclick="changeRegin(this,'provinceDiv');return false;">
            <span class="button-text provinceText" style="width:100px;text-align:left;"><s:text name="select_default"/></span>
  		 	<span class="ui-icon ui-icon-triangle-1-s"></span>
  		  </a>
          </span></td>
        </tr>
        <tr>
          <th><s:text name="addressLine2"></s:text></th>
          <td><span><s:textfield name="addressLine2" cssClass="text" maxlength="100"></s:textfield></span></td>
          <th><s:text name="cityName"></s:text></th>
          <td><span>
          <a class="ui-select ui-select-2" style="margin-left: 0px;" onclick="changeRegin(this,'cityDiv');return false;">
            <span class="button-text cityText" style="width:100px;text-align:left;"><s:text name="select_default"/></span>
 		 	<span class="ui-icon ui-icon-triangle-1-s"></span>
 		  </a>
          </span></td>
        </tr>
        <tr>
          <th><s:text name="zipCode"></s:text></th>
          <td><span><s:textfield name="zipCode" cssClass="text" maxlength="10"></s:textfield></span></td>
          <%-- <th><s:text name="locationGPS"></s:text></th>
          <td><span><s:textfield name="locationGPS" cssClass="text"></s:textfield></span></td>--%>
        </tr>
      </table>
    </div>  
</div>
<%-- ================== 部门地址DIV  END  ======================= --%>

<%-- ================== 部门联系人DIV  START  ======================= --%>
<div id="departContactTemp" style="display: none">
	  <table class="detail" cellpadding="0" cellspacing="0">
        <caption>
          <span class="left">
           	<input type="checkbox" name="defaultFlag" value="1" onclick="changeDefaulContact(this);"></input> <s:text name="defaultFlag"></s:text>
          </span>
          <a class="delete right" href="" onclick="removeContact(this);return false;"><span class="ui-icon icon-delete"></span><span class="button-text"><s:text name="delete_button"></s:text></span></a>
        </caption>
        <tr>
          <th><s:text name="employeeNam"></s:text></th>
          <td>
          <s:hidden name="employeeId"></s:hidden>
          <span id="employeeName" class="left" style="word-wrap:break-word;overflow:hidden;width:220px;">&nbsp;</span>
          <a onclick="bscom05_popDataTableOfDepartEmp(this,$('#brandInfoId').serialize());return false;" class="popup right" href=""><span class="ui-icon icon-search"></span><s:text name="selectContact"></s:text></a>   
          </td>
          <th><s:text name="phone"></s:text></th>
          <td><span id="phone"></span></td>
        </tr>
        <tr>
          <th><s:text name="mobilePhone"></s:text></th>
          <td><span id="mobilePhone"></span></td>
          <th><s:text name="email"></s:text></th>
          <td><span id="email"></span></td>
        </tr>
      </table>
</div>
<%-- ================== 部门联系人DIV  END  ======================= --%>

<%-- ================== 省市选择DIV START ======================= --%>
<div id="regin-city-subCity-div" style="display: none;">
<div class="ui-option ui-option-1 provinceDiv" style="width:325px;display: none;">
	<div class="clearfix">
		<span class="label"><s:text name="global.page.range"></s:text></span>
		<ul class="u2"><li onclick="getCity(this);"><s:text name="global.page.all"></s:text></li></ul>
	</div>
	<s:iterator value="reginList" id="reginInfo">
   	<div class="clearfix"><span class="label"><s:property value="#reginInfo.reginName"/></span>
   	<ul class="u2">
   		<s:iterator value="#reginInfo.provinceList" id="provinceInfo">
       		<li id='<s:property value="#provinceInfo.provinceId"/>' onclick="getCity(this);">
       			<s:property value="#provinceInfo.provinceName"/>
       		</li>
    		</s:iterator>
     	</ul>
   	</div>
  	</s:iterator>
</div>
<div class="ui-option ui-option-2 cityDiv" style="display: none;"></div>
<div class="ui-option ui-option-2 countyDiv" style="display: none;"></div>
</div>
<%-- ================== 省市选择DIV  END  ======================= --%>
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

<jsp:include page="/WEB-INF/jsp/bs/common/BINOLBSCOM03.jsp" flush="true" />

<%-- 实时刷新数据权限URL --%>
<s:url id="refreshPrivilegeUrl" value="/common/BINOLCMPL04_init" />
<s:hidden value="%{#refreshPrivilegeUrl}" id="refreshPrivilegeUrl"></s:hidden>


























         
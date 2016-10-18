<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>

<script type="text/javascript" src="/Cherry/js/bs/common/BINOLBSCOM03.js"></script>
<script type="text/javascript" src="/Cherry/js/bs/dep/BINOLBSDEP04.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-timepicker-addon.js"></script>
<link rel="stylesheet" href="/Cherry/css/cherry/cherry_timepicker.css" type="text/css">
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>

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
    <span class="breadcrumb left"> <span class="ui-icon icon-breadcrumb"></span><s:text name="base_mng"></s:text>&nbsp;&gt;&nbsp;<s:text name="add_organization"></s:text> </span>
	</div>
</div>

<div id="actionResultDisplay"></div>

<cherry:form onsubmit="saveWithValid();return false;" id="addOrganizationInfo" csrftoken="false">
<div class="panel-content clearfix">

<div class="section" id="departBasic">
  <div class="section-header">
	<strong><span class="ui-icon icon-ttl-section-info"></span><s:text name="base_title"></s:text></strong>
  </div>
  <div class="section-content">
  <table class="detail" cellpadding="0" cellspacing="0">
	<tr>
	  <th><s:text name="departCode"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
	  <td><span><s:textfield name="departCode" cssClass="text" maxlength="15"></s:textfield></span></td>
	  <th><s:text name="testType"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
	  <td><span>
	    <select name="testType">
	      <option value="">${select_default }</option>
	      <option value="0"><s:text name="testType0"></s:text></option>
	      <option value="1"><s:text name="testType1"></s:text></option>
	    </select>
	  </span></td>
	</tr>
	<tr>
	  <th><s:text name="type"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
	  <td><span><s:select name="type" list='#application.CodeTable.getCodesWithFilter("1000","0|1|4")' listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{#select_default}"></s:select></span></td>
	  <th><s:text name="departName"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
	  <td><span><s:textfield name="departName" cssClass="text" maxlength="30"></s:textfield></span></td>
	</tr>
	<tr>
	  <th><s:text name="brandInfo"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
	  <td><span><s:select name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" onchange="bsdep04_changeBrandInfo(this, '%{#select_default}');"></s:select></span></td>
	  <th><s:text name="departNameShort"></s:text></th>
	  <td><span><s:textfield name="departNameShort" cssClass="text" maxlength="20"></s:textfield></span></td>
	</tr>
	<tr>
	  <th><s:text name="provinceName"></s:text></th>
      <td><span>
        <a id="province" class="ui-select" style="margin-left: 0px; font-size: 12px;">
          	<span id="provinceText" class="button-text" style="min-width:100px;text-align:left;"><s:text name="global.page.select"/></span>
	 		<span class="ui-icon ui-icon-triangle-1-s"></span>
	 	</a>
	 	<s:hidden name="regionId" id="regionId"/>
	 	<s:hidden name="provinceId" id="provinceId"/>
      </span></td>
      <th><s:text name="nameForeign"></s:text></th>
	  <td><span><s:textfield name="nameForeign" cssClass="text" maxlength="30"></s:textfield></span></td>
	</tr>
	<tr>
	  <th><s:text name="cityName"></s:text></th>
	  <td><span>
	    <a id="city" class="ui-select" style="margin-left: 0px; font-size: 12px;">
          	<span id="cityText" class="button-text" style="min-width:100px;text-align:left;"><s:text name="global.page.select"/></span>
	 		<span class="ui-icon ui-icon-triangle-1-s"></span>
	 	</a>
	 	<s:hidden name="cityId" id="cityId"/>
	  </span></td>
	  <th><s:text name="nameShortForeign"></s:text></th>
	  <td><span><s:textfield name="nameShortForeign" cssClass="text" maxlength="20"></s:textfield></span></td>
	</tr>
	<tr>
	  <th><s:text name="countyName"></s:text></th>
	  <td><span>
	    <a id="county" class="ui-select" style="margin-left: 0px; font-size: 12px;">
          	<span id="countyText" class="button-text" style="min-width:100px;text-align:left;"><s:text name="global.page.select"/></span>
	 		<span class="ui-icon ui-icon-triangle-1-s"></span>
	 	</a>
	 	<s:hidden name="countyId" id="countyId"/>
	  </span></td>
	  <th><s:text name="status"></s:text></th>
	  <td><span><s:select list='#application.CodeTable.getCodes("1030")' listKey="CodeKey" listValue="Value" name="status" headerKey="" headerValue="%{#select_default}"></s:select></span></td>
	</tr>
	<tr>
	  <th><s:text name="bindDepotTitle"></s:text></th>
      <td><span>
        <input type="radio" name="isCreateFlg" id="isCreateFlg2" value="1" checked="checked" onclick="bsdep04_changeDepotType(this);"/><label for="isCreateFlg2"><s:text name="selectHavedDepot"></s:text></label>
        <input type="radio" name="isCreateFlg" id="isCreateFlg1" value="0" onclick="bsdep04_changeDepotType(this);"/><label for="isCreateFlg1"><s:text name="creatDefaultDepot"></s:text></label>
      </span></td>
	  <th><s:text name="selectDepotTitle"></s:text></th>
	  <td><span>
	  	<s:select list="depotInfoList" headerKey="" headerValue="%{#select_default}" listKey="depotInfoId" listValue="depotNameCN" name="depotInfoId"></s:select>
	  </span></td>
	</tr>
	<tr>
		<%-- 到期日 --%>
		<th><s:text name="counter.expiringDate"></s:text></th>
		<%--<td><span><s:textfield id="expiringDate" name="expiringDate" cssClass="date" cssStyle="width:150px;"/></span></td> --%>
		<td>
			<span><s:textfield id="expiringDateDate" name="expiringDateDate" cssClass="date" /></span>
			<span style='margin-left:10px;'><s:textfield id="expiringDateTime" name="expiringDateTime" cssClass="date"/></span>
		</td>		
		<%-- 柜台协同区分 --%>
		<s:if test="maintainOrgSynergy">
			<th><s:text name="depart.orgSynergyFlag"></s:text></th>
			<td>
				<span>
					<s:select list='#application.CodeTable.getCodes("1331")' listKey="CodeKey" listValue="Value" name="orgSynergyFlag">
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
	  </tbody>
	</table>
  </div>
</div>


<%--=================== 下级柜台 ===================== --%>
<div class="section" id="lowerCounterDiv">
  <div class="section-header clearfix">
   	<a class="add left" href="#" onclick="bscom05_popDataTableOfLowerCounter(this,$('#brandInfoId').serialize());return false;">
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
  <div class="section-content"></div>
</div>

<div class="section" id="departContact">
  <div class="section-header clearfix">
    <a class="add left" onclick="addContact(this);return false;"><span class="ui-icon icon-add"></span><span class="button-text"><s:text name="addContact_button"></s:text></span></a>
  </div>
  <div class="section-content"></div>
</div>
  
<hr class="space" />
<div class="center">
  <button class="save" onclick="saveWithValid();return false;"><span class="ui-icon icon-save"></span><span class="button-text"><s:text name="save_button"></s:text></span></button>
  <button class="close" type="button" onclick="window.close();return false;"><span class="ui-icon icon-close"></span><span class="button-text"><s:text name="global.page.close"/></span></button>
</div>

</div>
</cherry:form>

</div>
</div>
</div>
<div style="display: none">
<span id="selectInitText"><s:text name="select_default"/></span>
<s:url action="BINOLBSDEP04_add" id="addOrganizationUrl"></s:url>
<a id="addOrganizationUrl" href="${addOrganizationUrl }"></a>
<s:url action="BINOLBSDEP04_filter" id="filterByBrandInfoUrl"></s:url>
<a id="filterByBrandInfoUrl" href="${filterByBrandInfoUrl }"></a>
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
           	<input type="checkbox" name="defaultFlag" value="1" onclick="changeDefaulAdd(this);"></input> <s:text name="defaultFlag"></s:text>
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
<div id="cityTemp" class="ui-option hide"></div>
<div id="countyTemp" class="ui-option hide"></div>
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





















         
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>	
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>

<script type="text/javascript">

$(function(){
	
	// 部门查询
	bsdep01_searchOrganization();
});

</script>
	
<s:i18n name="i18n.bs.BINOLBSDEP01">	
<s:text name="select_default" id="select_default"></s:text>
<%-- ================== 错误信息提示 START ======================= --%>
<div id="errorMessage"></div>  
<div style="display: none" id="errorMessageTemp">
<div class="actionError">
   <ul><li><span><s:text name="depart.errorMessage"/></span></li></ul>         
</div>
</div>
<%-- ================== 错误信息提示   END  ======================= --%>
<div class="panel-content clearfix">
<div class="box">
  <cherry:form id="organizationCherryForm" class="inline" onsubmit="bsdep01_searchOrganization();return false;">
    <div class="box-header"> <strong><span class="ui-icon icon-ttl-search"></span><s:text name="search_condition"></s:text></strong>
    <s:checkbox name="validFlag" fieldValue="1"></s:checkbox><s:text name="containDelDepart"></s:text></div>
    <div class="box-content clearfix">
      <div class="column" style="width:50%;">
        <p>
          <label><s:text name="departCode"></s:text></label>
          <s:textfield name="departCode" cssClass="text"></s:textfield>
        </p>
        <p>
          <label><s:text name="departName"></s:text></label>
          <s:textfield name="departName" cssClass="text"></s:textfield>
        </p>
      </div>
      <div class="column last" style="width:49%;">
        <!-- 
        <s:if test='%{brandInfoList != null && !brandInfoList.isEmpty()}'>
        <p>
          <label><s:text name="brandInfo"></s:text></label>
          <s:a action="BINOLBSDEP01_filter" cssStyle="display:none;" id="filterByBrandInfoUrl"></s:a>
          <s:select name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" headerKey="" headerValue="%{#select_default}" onchange="bsdep01_changeBrandInfo(this,'%{#select_default}');"></s:select>
        </p>
        </s:if> -->
        <p>
          <label><s:text name="type"></s:text></label>
          <s:select list='#application.CodeTable.getCodes("1000")' listKey="CodeKey" listValue="Value" name="type" headerKey="" headerValue="%{#select_default}"></s:select>
        </p>
        <p>
          <label><s:text name="status"></s:text></label>
          <s:select list='#application.CodeTable.getCodes("1030")' listKey="CodeKey" listValue="Value" name="status" headerKey="" headerValue="%{#select_default}"></s:select>
          <input type="hidden" value='<s:text name="maintainOrgSynergy"/>' id="maintainOrgSynergy">
        </p>
      </div>
    </div>
    <p class="clearfix">
      <cherry:show domId="BINOLBSDEP0105">
      <button class="right search" onclick="bsdep01_searchOrganization();return false;"><span class="ui-icon icon-search-big"></span><span class="button-text"><s:text name="search_button"></s:text></span></button>
      </cherry:show>
    </p>
  </cherry:form>
</div>
<div class="section hide" id="organizationInfoId">
  <div class="section-header"><strong><span class="ui-icon icon-ttl-section-search-result"></span><s:text name="search_result"></s:text></strong></div>
  <div class="section-content">
    <div class="toolbar clearfix">
      <span class="left">
     	<s:url action="BINOLBSDEP05_delete" id="delDepart"></s:url>
     	<%-- EXCEL导出URL --%>
		<s:url id="xls_url" action="BINOLBSDEP01_export" />
		 <cherry:show domId="BINOLBSDEP01EX">
     	<span style="margin-right: 10px;"> 
     	<a id="export" class="export" onclick="bsdep01_exportExcel('${xls_url}');return false;">
			<span class="ui-icon icon-export"></span> 
			<span class="button-text"><s:text name="global.page.exportExcel" /></span>
		</a>
		</span>
		</cherry:show>
        <cherry:show domId="BINOLBSDEP0102">
        <a href="" class="add" onclick="bsdep01_editValidFlag('enable','${delDepart}');return false;">
           <span class="ui-icon icon-enable"></span>
           <span class="button-text"><s:text name="global.page.enable"/></span>
        </a>
        </cherry:show>
        <cherry:show domId="BINOLBSDEP0103">
        <a href="" class="delete" onclick="bsdep01_editValidFlag('disable','${delDepart}');return false;">
           <span class="ui-icon icon-disable"></span>
           <span class="button-text"><s:text name="global.page.disable"/></span>
        </a>
        </cherry:show>
      </span>
   	  <span class="right">
  		<%-- 列设置按钮  --%>
  		<a href="#" class="setting">
  			<span class="button-text"><s:text name="setting"></s:text></span>
			<span class="ui-icon icon-setting"></span>
 		</a>
      </span>
    </div>
    <table cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%" id="dataTable">
      <thead>
        <tr>
          <%-- <th>选择</th>--%>
          <th><input type="checkbox" id="checkAll" onclick="bscom03_checkRecord(this,'#dataTable_Cloned');"/></th>
          <th><s:text name="departCode"></s:text><span class="ui-icon-document css_right ui-icon ui-icon-triangle-1-n"></span><span class="css_right ui-icon ui-icon-triangle-1-n"></span></th>
          <th><s:text name="departName"></s:text></th>
          <th><s:text name="type"></s:text></th>
          <th><s:text name="brandInfo"></s:text></th>
          <th><s:text name="status"></s:text></th>
          <th><s:text name="regionName"></s:text></th>
          <th><s:text name="provinceName"></s:text></th>
          <th><s:text name="cityName"></s:text></th>
          <th><s:text name="countyName"></s:text></th>
          <%-- 部门协同区分 --%>
          <s:if test="maintainOrgSynergy" ><th><s:text name="depart.orgSynergyFlag"/></th></s:if>
          <th><s:text name="validFlag"></s:text></th>
        </tr>
      </thead>
      <tbody>
      </tbody>
    </table>
  </div>
</div>
</div>
</s:i18n>

<div class="hide">
<s:url action="BINOLBSDEP01_list" id="organizationListUrl"></s:url>
<a href="${organizationListUrl }" id="organizationListUrl"></a>
</div>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />   
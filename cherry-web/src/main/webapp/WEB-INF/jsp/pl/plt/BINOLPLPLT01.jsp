<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%> 

<script type="text/javascript" src="/Cherry/js/pl/plt/BINOLPLPLT01.js"></script>


<s:i18n name="i18n.pl.BINOLPLPLT01">
<s:text name="global.page.select" id="select_default"></s:text>
	<div class="panel-header">
        <div class="clearfix"> 
        <span class="breadcrumb left">	    
			<jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
		</span> 
        <s:url action="BINOLPLPLT02_init" id="addPltInitUrl"></s:url>
        <span class="right"> 
   		<%-- 实时刷新数据权限URL --%>
		<s:url id="refreshPrivilegeUrl" value="/common/BINOLCMPL04_init">
			<s:param name="isReOrgRelPl" value="0"></s:param>
		</s:url>
    	<cherry:show domId="BINOLPLPLT0104">
    	<a class="add" href="" onclick="javascript:privilegeRefreshConfirm('${refreshPrivilegeUrl }');return false;">
    	<span class="ui-icon icon-refresh-s"></span><span class="button-text"><s:text name="global.page.privilegeTitle"></s:text></span>
    	</a>
    	</cherry:show>
        <cherry:show domId="BINOLPLPLT0101">
        <a class="add" onclick="plplt01_addPltInit('${addPltInitUrl }');return false;">
        <span class="ui-icon icon-add"></span><span class="button-text"><s:text name="add_plt" /></span>
        </a> 
        </cherry:show>
        </span> 
        </div>
	</div>
	<div class="panel-content">
        <div class="box">
		<cherry:form id="searchPltForm" class="inline" onsubmit="plplt01_searchPlt();return false;">
            <div class="box-header"> <strong><span class="ui-icon icon-ttl-search"></span><s:text name="global.page.condition" /></strong> </div>
            <div class="box-content clearfix">
              <div class="column" style="width:50%;">
                <p>
                  <label style="width:100px;"><s:text name="departType" /></label>
                  <s:select list='#application.CodeTable.getCodes("1000")' listKey="CodeKey" listValue="Value" name="departType" headerKey="" headerValue="%{select_default}"></s:select>
                </p>
                <p>
                  <label style="width:100px;"><s:text name="positionCategoryId" /></label>
                  <s:select list="posCategoryList" listKey="positionCategoryId" listValue="categoryName" name="positionCategoryId" headerKey="" headerValue="%{select_default}"></s:select>
                </p>
              </div>
              <div class="column last" style="width:49%;">
              	<p>
                  <label style="width:100px;"><s:text name="businessType" /></label>
                  <s:select list='#application.CodeTable.getCodes("1048")' listKey="CodeKey" listValue="Value" name="businessType" headerKey="" headerValue="%{select_default}"></s:select>
                </p>
              </div>
            </div>
            <p class="clearfix">
              <cherry:show domId="BINOLPLPLT0105">
              <button class="right search" onclick="plplt01_searchPlt();return false;">
         		<span class="ui-icon icon-search-big"></span><span class="button-text"><s:text name="global.page.search" /></span>
              </button>
              </cherry:show>
            </p>
		</cherry:form>
        </div>
        <div class="section hide" id="pltSection">
          <div class="section-header"><strong><span class="ui-icon icon-ttl-section-search-result"></span><s:text name="global.page.list" /></strong></div>
          <div class="ui-tabs">
          	<ul class ="ui-tabs-nav clearfix">
	            <li class="ui-tabs-selected" onclick="plplt01_pltFilter('allPl',this);"><a><s:text name="allPl"/></a></li>
	            <li onclick="plplt01_pltFilter('departPl',this);"><a><s:text name="departPl"/></a></li>
	            <li onclick="plplt01_pltFilter('positionPl',this);"><a><s:text name="positionPl"/></a></li>
          	</ul>
			<div class="ui-tabs-panel">
            	<table id ="dateTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
					<thead>
			        	<tr>
                  			<th><s:text name="category" /></th>
                  			<th><s:text name="departType" /></th>
                  			<th><s:text name="positionCategoryId" /></th>
                  			<th><s:text name="businessType" /></th>
                  			<th><s:text name="operationType" /></th>
                 		 	<th><s:text name="privilegeType" /></th>
                 		 	<th><s:text name="exclusive_title" /></th>
                 		 	<th class="center"><s:text name="operation_button" /></th>
						</tr>
			        </thead>
			        <tbody>
			        </tbody>
				</table>
          	</div>
		  </div>
        </div>
	</div>

<div class="hide" id="dialogInit"></div>
<div style="display: none;">
<s:a action="BINOLPLPLT01_list" id="pltListUrl"></s:a>
<div id="privilegeTitle"><s:text name="global.page.privilegeTitle" /></div>
<div id="privileMessage"><p class="message"><span><s:text name="global.page.privileMessage" /></span></p></div>
<div id="deletePltText"><p class="message"><span><s:text name="deletePltText" /></span></p></div>
<div id="deletePltTitle"><s:text name="deletePltTitle" /></div>
<div id="dialogConfirm"><s:text name="global.page.ok" /></div>
<div id="dialogCancel"><s:text name="global.page.cancle" /></div>
<div id="dialogClose"><s:text name="global.page.close" /></div>
</div>
</s:i18n>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- 实时刷新数据权限URL --%>
<s:url id="refreshPrivilegeUrl" value="/common/BINOLCMPL04_init">
	<s:param name="isReOrgRelPl" value="0"></s:param>
</s:url>
<s:hidden value="%{#refreshPrivilegeUrl}" id="refreshPrivilegeUrl"></s:hidden>

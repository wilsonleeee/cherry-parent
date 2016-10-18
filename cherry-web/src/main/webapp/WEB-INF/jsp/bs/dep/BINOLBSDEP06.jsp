<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%> 

<script type="text/javascript" src="/Cherry/js/bs/common/BINOLBSCOM03.js"></script>
<script type="text/javascript" src="/Cherry/js/bs/dep/BINOLBSDEP06.js"></script>


<s:i18n name="i18n.bs.BINOLBSDEP06">
	<div class="panel-header">
        <div class="clearfix"> 
      	<span class="breadcrumb left">	    
			<jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
		</span>
        <s:if test='%{isAddOrgButton != null && "1".equals(isAddOrgButton)}'>
        <s:url action="BINOLBSDEP07_init" id="addOrgInitUrl"></s:url>
        <span class="right"> <a class="add" onclick="bsdep06_addOrgInit('${addOrgInitUrl }');return false;">
        <span class="ui-icon icon-add"></span><span class="button-text"><s:text name="bsdep_addOrg" /></span>
        </a> </span> 
        </s:if>
        </div>
	</div>
	<%-- ================== 错误信息提示 START ======================= --%>
	<div id="errorMessage"></div>    
	<div style="display: none" id="errorMessageTemp">
	<div class="actionError">
	   <ul><li><span><s:text name="org.errorMessage"/></span></li></ul>         
	</div>
	</div>
	<%-- ================== 错误信息提示   END  ======================= --%>
	<div class="panel-content">
        <div class="box">
		<cherry:form id="searchOrgForm" class="inline" onsubmit="bsdep06_searchOrg();return false;">
            <div class="box-header"> <strong><span class="ui-icon icon-ttl-search"></span><s:text name="global.page.condition" /></strong> 
            <s:checkbox name="validFlag" fieldValue="1"></s:checkbox><s:text name="containDelOrg"></s:text></div>
            <div class="box-content clearfix">
              <div class="column" style="width:100%; border:none;">
                <p>
                  <label><s:text name="orgNameKw" /></label>
                  <s:textfield name="orgNameKw" cssClass="text"></s:textfield>
                </p>
              </div>
            </div>
            <p class="clearfix">
              <cherry:show domId="BINOLBSDEP0604">
              <button class="right search" onclick="bsdep06_searchOrg();return false;">
         		<span class="ui-icon icon-search-big"></span><span class="button-text"><s:text name="global.page.search" /></span>
              </button>
              </cherry:show>
            </p>
		</cherry:form>
        </div>
        <div class="section hide" id="OrgSection">
          <div class="section-header"><strong><span class="ui-icon icon-ttl-section-search-result"></span><s:text name="global.page.list" /></strong></div>
          <div class="section-content">
            <div class="toolbar clearfix">
           	  <span class="left">
           		<s:url action="BINOLBSDEP09_delete" id="delOrg"></s:url>
                <cherry:show domId="BINOLBSDEP0601">
                <a href="" class="add" onclick="bsdep06_editValidFlag('enable','${delOrg}');return false;">
		           <span class="ui-icon icon-enable"></span>
		           <span class="button-text"><s:text name="global.page.enable"/></span>
		        </a>
		        </cherry:show>
		        <cherry:show domId="BINOLBSDEP0602">
		        <a href="" class="delete" onclick="bsdep06_editValidFlag('disable','${delOrg}');return false;">
		           <span class="ui-icon icon-disable"></span>
		           <span class="button-text"><s:text name="global.page.disable"/></span>
		        </a>
		        </cherry:show>
              </span>
              <span class="right">
		  		<%-- 列设置按钮  --%>
		  		<a href="#" class="setting">
		  			<span class="button-text"><s:text name="global.page.colSetting"></s:text></span>
					<span class="ui-icon icon-setting"></span>
		 		</a>
		      </span>
            </div>
            <table cellpadding="0" cellspacing="0" border="0" class="jquery_table" id="dataTable" width="100%">
              <thead>
                <tr>
                  <th><input type="checkbox" id="checkAll" onclick="bscom03_checkRecord(this,'#dataTable_Cloned');"/></th>
                  <th><s:text name="orgCode" /></th>
                  <th><s:text name="orgNameChinese" /></th>
                  <th><s:text name="orgNameForeign" /></th>
                  <th><s:text name="orgNameShort" /></th>
                  <th><s:text name="orgNameForeignShort" /></th>
                  <th><s:text name="validFlag"></s:text></th>
                  <th class="center"><s:text name="operation_button" /></th>
                </tr>
              </thead>
              <tbody>
              </tbody>
            </table>
          </div>
        </div>
	</div>

<div class="hide" id="dialogInit"></div>
<div style="display: none;">
<s:a action="BINOLBSDEP06_list" id="orgListUrl"></s:a>
<div id="disableTitle"><s:text name="org.disableTitle" /></div>
<div id="enableTitle"><s:text name="org.enableTitle" /></div>
<div id="disableMessage"><p class="message"><span><s:text name="org.disableMessage" /></span></p></div>
<div id="enableMessage"><p class="message"><span><s:text name="org.enableMessage" /></span></p></div>
<div id="dialogConfirm"><s:text name="global.page.ok" /></div>
<div id="dialogCancel"><s:text name="global.page.cancle" /></div>
<div id="dialogClose"><s:text name="global.page.close" /></div>
</div>
</s:i18n>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />


<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>

<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/mb/vis/BINOLMBVIS05.js"></script>

<s:i18n name="i18n.mb.BINOLMBVIS05">
    <s:text name="global.page.select" id="select_default"/>
    <div class="panel-header">
	    <div class="clearfix"> 
	 	<span class="breadcrumb left">	    
			<jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
		</span>
	    </div>
    </div>
    <div class="panel-content">
      <div class="box">
        <cherry:form id="queryForm">
          <div class="box-header">
          	<strong><span class="ui-icon icon-ttl-search"></span><s:text name="global.page.condition"/></strong>
          </div>
          <div class="box-content clearfix">
            <div class="column" style="width:50%; height: auto;">
	              <p>
			        <label><s:text name="mbvis05_visitTypeCode" /></label>
			        <s:select name="visitType" list="visitCategoryList" listKey="visitTypeCode" listValue="visitTypeName" headerKey="" headerValue="%{#select_default}" cssStyle="width:150px"></s:select>
			        <s:hidden name="visitTypeName"></s:hidden>
			      </p>
            </div>
          </div>
          <p class="clearfix">
         	<button class="right search" id="searchButton">
         		<span class="ui-icon icon-search-big"></span><span class="button-text"><s:text name="global.page.search"/></span>
         	</button>
          </p>
        </cherry:form>
      </div>
      <div id="resultDiv" class="section hide">
        <div class="section-header">
        	<strong>
        		<span class="ui-icon icon-ttl-section-search-result"></span>
        		<s:text name="global.page.list"/>
        	</strong>
        </div>
        <div class="section-content">
          <table id="resultTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
            <thead>
              <tr>
                <th><s:text name="mbvis05_visitTypeCode"/></th>
                <th><s:text name="mbvis05_birthDay"/></th>
                <th><s:text name="mbvis05_memName"/></th>
                <th><s:text name="mbvis05_memCode"/></th>
                <th><s:text name="mbvis05_memMobile"/></th>
                <th><s:text name="mbvis05_skinType"/></th>
                <th><s:text name="mbvis05_operater"/></th>
              </tr>
            </thead>           
          </table>
        </div>
      </div>
    </div>
</s:i18n>

<div class="hide">
<s:a action="BINOLMBVIS05_search" id="searchUrl"></s:a>
</div>    

<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/popExportDialog.jsp" flush="true" />
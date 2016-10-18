<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%> 

<script type="text/javascript" src="/Cherry/js/pl/scf/BINOLPLSCF06.js"></script>


<s:i18n name="i18n.pl.BINOLPLSCF06">
<s:text name="global.page.select" id="select_default"></s:text>
	<div class="panel-header">
        <div class="clearfix"> 
      	<span class="breadcrumb left">	    
			<jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
		</span> 
        <s:url action="BINOLPLSCF06_addInit" id="add_url"></s:url>
        <s:url action="BINOLPLSCF06_refreshCodes" id="refresh_url"></s:url>
        <span class="right">
        <a href="${refresh_url }" class="add" id="refreshCodes" onclick="javascript:plscf06_refreshCodes(this);return false;">
        <span class="ui-icon icon-refresh-s"></span><span class="button-text"><s:text name="code_refresh" /></span>
        </a>	
        <a href="${add_url }" class="add" id="add" onclick="javascript:openWin(this);return false;">
        <span class="ui-icon icon-add"></span><span class="button-text"><s:text name="add_code" /></span>
        </a> </span> 
        </div>
	</div>
	<div class="panel-content">
        <div class="box">
		<cherry:form id="searchCodeForm" class="inline" onsubmit="plscf06_searchCode();return false;">
            <div class="box-header"> <strong><span class="ui-icon icon-ttl-search"></span><s:text name="global.page.condition" /></strong> </div>
            <div class="box-content clearfix">
              <div class="column" style="width: 49%; height: 50px;">
                <%-- Code类别--%>
                <p>
                  <label style="width: 65px;"><s:text name="codeType" /></label>
                  <s:textfield name="codeType" cssClass="text" maxlength="20" />
                </p>
              </div>
              <div class="column last" style="width:50%; height: 50px;">
                
           		<%-- Code类别名称--%>
                <p>
                  <label style="width:65px;"><s:text name="codeName" /></label>
                  <s:textfield name="codeName" cssClass="text" maxlength="20" />
                </p>
              </div> 
            </div>
            
            <p class="clearfix">
              <button class="right search" onclick="plscf06_searchCode();return false;">
         		<span class="ui-icon icon-search-big"></span><span class="button-text"><s:text name="global.page.search" /></span>
              </button>
            </p>
		</cherry:form>
        </div>
        <%--dataTabel area --%>
        <div class="section hide" id="codeSection">
          <div class="section-header"><strong><span class="ui-icon icon-ttl-section-search-result"></span><s:text name="global.page.list" /></strong></div>
          <div class="section-content">
            <table cellpadding="0" cellspacing="0" border="0" class="jquery_table" id="dateTable" width="100%">
              <thead>
                <tr>
                  <%-- <th><s:text name="brandInfo" /></th> --%>
                  <th><s:text name="codeType" /></th>
                  <th><s:text name="codeName" /></th>
                  <th><s:text name="keyDescription" /></th>
                  <th><s:text name="value1Description" /></th>
                  <th><s:text name="value2Description" /></th>
                  <th><s:text name="value3Description" /></th>
                </tr>
              </thead>
              <tbody>
              </tbody>
            </table>
          </div>
        </div>
	</div>
	
	<s:a action="BINOLPLSCF06_codeList" id="codeListUrl"></s:a>
	
<div class="hide" id="dialogInit"></div>
<div style="display: none;">
	<div id="refreshCodeTitle"><s:text name="code_refresh" /></div>
	<div id="refreshCodeMeg"><p class="message"></p></div>
	<div id="dialogConfirm"><s:text name="global.page.ok" /></div>
	<div id="dialogClose"><s:text name="global.page.close" /></div>
</div>	
</s:i18n>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />


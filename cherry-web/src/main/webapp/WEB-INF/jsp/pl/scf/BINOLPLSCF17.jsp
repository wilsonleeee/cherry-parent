<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/pl/scf/BINOLPLSCF17.js"></script>
<s:i18n name="i18n.pl.BINOLPLSCF17">
<s:url id="search_url" action="BINOLPLSCF17_search"/>
<s:url id="handValid_url" action="BINOLPLSCF17_handValid"/>
<s:url id="handRefresh_url" action="BINOLPLSCF17_handRefresh"/>
<div class="hide">
     <a id="searchUrl" href="${search_url}"></a>
     <a id="handValidUrl" href="${handValid_url}"></a>
</div>
<div class="panel-header">
    <div class="clearfix">
     <span class="breadcrumb left"> <span class="ui-icon icon-breadcrumb"></span><a href="#"><s:text name="SCF17.sysConfig"/></a> &gt; <a href="#"><s:text name="SCF17.handlerManage"/></a> </span> 
     <span class="right">
     <cherry:show domId="BINOLPLSCF1701">
        <a href="${handRefresh_url}" class="add" id="refreshCodes" onclick="javascript:BINOLPLSCF17.refreshHandler(this);return false;">
        <span class="ui-icon icon-refresh-s"></span><span class="button-text"><s:text name="SCF17.refresh"/></span>
        </a>
     </cherry:show>
     </span>
     </div>
 </div>
 <div id="actionResultDisplay"></div>
 <div class="panel-content">
 		 <div class="box">
          <cherry:form id="mainForm" class="inline">
         <div class="box-header">
            	<strong><span class="ui-icon icon-ttl-search"></span><s:text name="global.page.condition"/></strong>
            </div>
            <div class="box-content clearfix">
          <div class="column" style="width:50%;height:55px;">
            <p class="clearfix">
              <label><s:text name="SCF17.brandName"/></label>
          	  <s:select name="brandCode" list="brandInfoList" listKey="brandCode" listValue="brandName" id="brandSel"/>
            </p>
            </div>
            <div class="column last" style="width:49%;height:55px;">  
             <p class="date">
              		<label><s:text name="SCF17.handlerName"/></label>
              		<span><s:textfield name="handNameCN" cssClass = "text"></s:textfield></span>
             </p>
            </div>
            </div>
            <p class="clearfix">
	              	<button class="right search"  onclick="BINOLPLSCF17.search();return false;">
	              			<span class="ui-icon icon-search-big"></span>
	              			<span class="button-text"><s:text name="global.page.search"/></span>
	              	</button>
	            </p>
          </cherry:form>
        </div>
        
        <div id="section" class="section">
	          <div class="section-header">
	          <strong>
	          	<span class="ui-icon icon-ttl-section-search-result"></span>
	          	<s:text name="global.page.list"/>
	         </strong>
	         </div>
    <div class="section-content">
         <div class="toolbar clearfix">
          	<span class="left">
			<cherry:show domId="BINOLPLSCF1702">
               <a href="" class="add" onclick="javascript:BINOLPLSCF17.handlerValid('1');return false;">
                  <span class="ui-icon icon-enable"></span>
                  <span class="button-text"><s:text name="global.page.enable"/></span>
               </a>
             </cherry:show>
             <cherry:show domId="BINOLPLSCF1703">
               <a href="" class="delete" onclick="javascript:BINOLPLSCF17.handlerValid('0');return false;">
                  <span class="ui-icon icon-disable"></span>
                  <span class="button-text"><s:text name="global.page.disable"/></span>
               </a>
              </cherry:show>
           </span>
         </div>
		<table id="dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table2" width="100%">
   		<thead>
            <tr>
               <th><input type="checkbox" id="checkAll" onclick="BINOLPLSCF17.checkHandler(this, '#dataTableBody');"/></th>
               <th><s:text name="SCF17.handlerName"/></th>
               <th><s:text name="SCF17.description"/></th>
               <th><s:text name="SCF17.status"/></th>
            </tr>
           </thead>
        <tbody id="dataTableBody" >
        </tbody>
   		</table>
   		</div>
   		</div>
      </div>
       <div class="hide" id="dialogHandler"></div>
       <div class="hide" id="dialogRefresh"></div>
       <div class="hide">
       	<span id="cancelTxt"><s:text name="SCF17.cancel"/></span>
       	<span id="titleOnTxt"><s:text name="SCF17.titleOn"/></span>
       	<span id="warnOnTxt"><s:text name="SCF17.warnOn"/></span>
       	<span id="closeTxt"><s:text name="SCF17.close"/></span>
       	<span id="sureTxt"><s:text name="SCF17.sure"/></span>
       	<span id="configOnTxt"><s:text name="SCF17.configOn"/></span>
       	<span id="titleOffTxt"><s:text name="SCF17.titleOff"/></span>
       	<span id="warnOffTxt"><s:text name="SCF17.warnOff"/></span>
       	<span id="configOffTxt"><s:text name="SCF17.configOff"/></span>
       	<span id="refreshTxt"><s:text name="SCF17.refresh"/></span>
       	<span id="refreshNGTxt"><s:text name="SCF17.refreshNG"/></span>
       	<span id="refreshOKTxt"><s:text name="SCF17.refreshOK"/></span>
       </div>
</s:i18n>
 <%-- ================== dataTable共通导入 START ======================= --%>
  <jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
  <%-- ================== dataTable共通导入    END  ======================= --%>
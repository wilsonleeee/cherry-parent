<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/pl/upm/BINOLPLUPM04.js"></script>
<s:i18n name="i18n.pl.BINOLPLUPM04">
	 <%-- 密码安全配置信息查询URL --%>
     <s:url id="change_url" action="BINOLPLUPM04_change"/>
     <%-- 密码安全配置信息编辑URL --%>
     <s:url id="edit_url" action="BINOLPLUPM06_init"/>
     <%-- 密码安全配置信息添加URL --%>
     <s:url id="add_url" action="BINOLPLUPM05_init"/>
     <div class="hide">
     	<a id="changeUrl" href="${change_url}"></a>
     	<a id="editUrl" href="${edit_url}"></a>
     	<a id="addUrl" href="${add_url}"></a>
     	<div id="editDialogTitle"><s:text name="UPM04_editDialogTitle"/></div>
     	<div id="addDialogTitle"><s:text name="UPM04_addDialogTitle"/></div>
     	<div id="errMsgText1"><s:text name="EPL00014"/></div>
     	<div id="errMsgText2"><s:text name="EPL00015"/></div>
     	<div id="errMsgText3"><s:text name="EPL00016"/></div>
     	<div id="errMsgText4"><s:text name="EPL00017"/></div>
     </div>
      <div class="panel-header">
        <div class="clearfix"> 
		<span class="breadcrumb left">	    
			<jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
		</span> 
        </div>
      </div>
      <div class="panel-content">
        <div id="section" class="section">
          <div class="section-header">
          <strong>
          <span class="ui-icon icon-ttl-section-search-result"></span>
          <span>
         	<%-- 品牌名称 --%>
       		<s:text name="UPM04_brand"/>
          </span>
          </strong>
          <s:if test="null != brandInfoList">
          	<s:select id="brandSel" name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" onchange="pwConfByBrand()"/>
         </s:if>
         <s:else>
         	
         		<%-- 品牌名称 --%>
          		<s:property value="brandName"/>
         	
         	<%-- 品牌ID --%>
         	<input type="hidden" id="brandSel" name="brandInfoId" value="<s:property value='brandInfoId' />"/>
         </s:else>
        </div>
          <div class="section-content">
          <cherry:form id="mainForm" class="inline">
          	<div id="detailInfo">
             <jsp:include page="/WEB-INF/jsp/pl/upm/BINOLPLUPM04_1.jsp" flush="true" />
             </div>
          </cherry:form>
          </div>
        </div>
      </div>
      <div id="savePage"></div>
 </s:i18n>
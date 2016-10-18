<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ page import="com.cherry.cm.core.CherryConstants" %>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>


<script type="text/javascript" src="/Cherry/js/bs/pos/BINOLBSPOS09.js"></script>


<s:set id="BRAND_INFO_ID_VALUE"><%=CherryConstants.BRAND_INFO_ID_VALUE %></s:set>

<s:i18n name="i18n.bs.BINOLBSPOS06">
<s:text name="select_default" id="select_default"></s:text>
<s:text name="headquarters" id="headquarters"></s:text>
<div class="main container clearfix">
<div class="panel ui-corner-all">
<div id="div_main">

<div class="panel-header">
    <div class="clearfix"> 
    <span class="breadcrumb left"> <span class="ui-icon icon-breadcrumb"></span><s:text name="base_mng"></s:text>&nbsp;&gt;&nbsp;<s:text name="add_positionCategory"></s:text> </span> 
    </div>
  </div>

<div id="actionResultDisplay"></div>

<cherry:form onsubmit="savePosCategory();return false;" id="addPosCategoryInfo" csrftoken="false">
<div class="panel-content clearfix">
  <div class="section" id="positionBasic">
    <div class="section-header">
    <strong><span class="ui-icon icon-ttl-section-info"></span><s:text name="base_title"></s:text></strong>
    </div>
    <div class="section-content">
      <div class="table clearfix">
        <s:if test="%{brandInfoList != null && !brandInfoList.isEmpty()}">
        <div class="caption">
     		<s:text name="brandInfo"></s:text>：
            <s:select list="brandInfoList" listKey="brandInfoId" listValue="brandName" headerKey="%{#BRAND_INFO_ID_VALUE}" headerValue="%{#headquarters}" name="brandInfoId"></s:select>
    	</div>
    	</s:if>
        <div class="tr">
          <div class="th"><label><s:text name="categoryCode"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></label></div>
          <div class="td"><p><s:textfield name="categoryCode" cssClass="text" maxlength="2"></s:textfield></p></div>
          <div class="th"><label><s:text name="categoryName"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></label></div>
          <div class="td"><p><s:textfield name="categoryName" cssClass="text" maxlength="20"></s:textfield></p></div>
        </div>
        <div class="tr">
          <div class="th"><label><s:text name="posGrade"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></label></div>
          <div class="td"><p><s:textfield name="posGrade" cssClass="text"></s:textfield></p></div>
          <div class="th"><label><s:text name="categoryNameForeign"></s:text></label></div>
          <div class="td"><p><s:textfield name="categoryNameForeign" cssClass="text" maxlength="20"></s:textfield></p></div>
        </div>
      </div>      
    </div>
  </div> 

<hr class="space" />
<div class="center">
  <s:url action="BINOLBSPOS09_add" id="addPosCategoryUrl"></s:url>
  <a id="addPosCategoryUrl" href="${addPosCategoryUrl }" style="display: none;"></a>
  <button class="save" onclick="savePosCategory();return false;"><span class="ui-icon icon-save"></span><span class="button-text"><s:text name="save_button"></s:text></span></button>
  <button class="close" type="button" onclick="window.close();return false;"><span class="ui-icon icon-close"></span><span class="button-text"><s:text name="global.page.close"/></span></button>
</div>
 
</div>
</cherry:form>


</div>
</div>
</div>  
</s:i18n>


          
     
















      
 
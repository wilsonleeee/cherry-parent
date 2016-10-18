<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<script type="text/javascript" src="/Cherry/js/pt/jcs/BINOLPTJCS40.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<s:i18n name="i18n.pt.BINOLPTJCS38">
<s:text id="global.select" name="global.page.select"/>
<s:text name="CHA03_select" id="CHA03_select"/>
<div class="main container clearfix">
<div class="panel ui-corner-all">
<div id="div_main">
<div class="panel-header">
<cherry:form id="toDetailForm" action="BINOLPTJCS39_init" method="post" csrftoken="false">
       	<input type="hidden" name="productFunctionID" value='<s:property value="productFunctionID"/>'/>
       	<input type="hidden" name="csrftoken" id="parentCsrftoken"/>
</cherry:form>
  <div class="clearfix"> 
   <span class="breadcrumb left"> <span class="ui-icon icon-breadcrumb"></span><s:text name="prtFun_title1" />&nbsp;&gt;&nbsp;<s:text name="JCS40_detail"></s:text></span> 
    <%-- <span class="breadcrumb left"> <span class="ui-icon icon-breadcrumb"></span><s:text name="CHA03_title1" />&nbsp;&gt;&nbsp;<s:text name="CHA03_edit"></s:text></span>--%> 
  </div>
</div>
<div id="actionResultDisplay"></div>
<div class="panel-content clearfix">
<div class="section">
  <div class="section-header">
	<strong><span class="ui-icon icon-ttl-section-info"></span><s:text name="prtfun_baseInfo"></s:text></strong>
  </div>
  <div class="section-content">
  <cherry:form id="update" csrftoken="false" onsubmit="save();return false;">
    <input type="hidden" name="productFunctionID" value='<s:property value="productFunctionID"/>'/>
    <s:hidden name="prtFunUpdateTime" value="%{prtFunInfo.prtFunUpdateTime}"></s:hidden>
	<s:hidden name="prtFunModifyCount" value="%{prtFunInfo.prtFunModifyCount}"></s:hidden>
    <table class="detail" id="detailId" cellpadding="0" cellspacing="0">
  	  <tr>
         <th><s:text name="prtFunType"></s:text></th>
         <td><span><s:property value='#application.CodeTable.getVal("1327",prtFunInfo.prtFunType)'/></span></td>
        <th><s:text name="prtfun_startDate"></s:text></th>
	    <td><span><s:textfield name="startDate" cssClass="date startTime" value="%{prtFunInfo.startDateYMD}"></s:textfield></span></td>
      </tr>
      <tr>
        <th><s:text name="prtFunDateName"></s:text></th>
        <td><span><s:textfield name="prtFunDateName" cssClass="text" maxlength="50" value="%{prtFunInfo.prtFunDateName}"/></span></td>
      	
        <th><s:text name="prtfun_endDate"></s:text></th>
	    <td><span><s:textfield name="endDate" cssClass="date endTime" value="%{prtFunInfo.endDateYMD}"></s:textfield></span></td>
	  </tr>
    </table>    
 </cherry:form>  
  </div>
</div> 
 <div class="center clearfix" id="pageButton">
          <button class="save" onclick="save();return false;">
          <s:a action="BINOLPTJCS40_save" id="JCS40_save" cssStyle="display: none;"></s:a>
          <span class="ui-icon icon-save"></span>
          <span class="button-text"><s:text name="global.page.save"/></span>
          </button>
          <button id="back" class="back" type="button" onclick="doBack()">
           	<span class="ui-icon icon-back"></span>
           	<span class="button-text"><s:text name="global.page.back"/></span>
          </button>
          <button class="close" onclick="window.close();">
          	<span class="ui-icon icon-close"></span>
            <span class="button-text"><s:text name="global.page.close"/></span><%-- 关闭 --%>
          </button>
    </div>
</div>
</div>
</div>
</div>
</s:i18n> 
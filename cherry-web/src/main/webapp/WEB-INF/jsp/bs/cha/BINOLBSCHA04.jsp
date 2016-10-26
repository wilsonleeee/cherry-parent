<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<script type="text/javascript" src="/Cherry/js/bs/cha/BINOLBSCHA04.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<s:i18n name="i18n.bs.BINOLBSCHA04">
<s:text name="CHA04_select" id="CHA04_select"/>
<div class="main container clearfix">
<div class="panel ui-corner-all">
<div id="div_main">
<div class="panel-header">
  <div class="clearfix"> 
    <span class="breadcrumb left"> <span class="ui-icon icon-breadcrumb"></span><s:text name="CHA04_title1" />&nbsp;&gt;&nbsp;<s:text name="CHA04_add"></s:text></span> 
  </div>
</div>
<div id="actionResultDisplay"></div>
<div class="panel-content clearfix">
<div class="section">
  <div class="section-header">
	<strong><span class="ui-icon icon-ttl-section-info"></span><s:text name="CHA04_baseInfo"></s:text></strong>
  </div>
  <div class="section-content">
  <cherry:form id="add" csrftoken="false" onsubmit="save();return false;">
    <table class="detail" cellpadding="0" cellspacing="0">
  	  <tr>
  	  	<th><s:text name="CHA04_brand"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span><s:text name="CHA04_select" id="CHA04_select"/></th>
  	  	<td><span><s:select name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" cssStyle="width:100px;"></s:select></span></td>
        <th><s:text name="CHA04_channelName"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
        <td><span><s:textfield name="channelName" cssClass="text" maxlength="50"></s:textfield></span></td>
     </tr>
	 <tr> 
	 	<th><s:text name="CHA04_channelCode"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
        <td><span><s:textfield name="channelCode" cssClass="text" maxlength="50"></s:textfield></span></td>
        <th><s:text name="CHA04_status"></s:text></th>
        <td><span><s:select name="status" list='#application.CodeTable.getCodes("1121")' 
	                 	listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{CHA04_select}"/></span></td>
      </tr>
      <tr>
        <th><s:text name="CHA04_channelNameForeign"></s:text></th>
        <td><span><s:textfield name="channelNameForeign" cssClass="text" maxlength="50"></s:textfield></span></td>
     	<th><s:text name="CHA04_joinDate"></s:text></th>
	  	<td><span><s:textfield name="joinDate" cssClass="date" value="%{endDate}"></s:textfield></span></td>
	  </tr>
    </table>    
 </cherry:form>  
  </div>
</div> 
<hr class="space" />
<div class="center">
  <s:a action="BINOLBSCHA04_save" id="CHA04_save" cssStyle="display: none;"></s:a>
  <button class="save" onclick="save();return false;"><span class="ui-icon icon-save"></span><span class="button-text"><s:text name="global.page.save"></s:text></span></button>
  <button class="close" type="button" onclick="window.close();return false;"><span class="ui-icon icon-close"></span><span class="button-text"><s:text name="global.page.close"/></span></button>
</div>
</div>
</div>
</div>
</div>
</s:i18n> 
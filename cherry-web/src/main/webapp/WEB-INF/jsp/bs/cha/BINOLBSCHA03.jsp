<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<script type="text/javascript" src="/Cherry/js/bs/cha/BINOLBSCHA03.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<s:i18n name="i18n.bs.BINOLBSCHA03">
<s:text id="global.select" name="global.page.select"/>
<s:text name="CHA03_select" id="CHA03_select"/>
<div class="main container clearfix">
<div class="panel ui-corner-all">
<div id="div_main">
<div class="panel-header">
<cherry:form id="toDetailForm" action="BINOLBSCHA02_init" method="post" csrftoken="false">
       	<input type="hidden" name="channelId" value='<s:property value="channelId"/>'/>
       	<input type="hidden" name="csrftoken" id="parentCsrftoken"/>
</cherry:form>
  <div class="clearfix"> 
    <span class="breadcrumb left"> <span class="ui-icon icon-breadcrumb"></span><s:text name="CHA03_title1" />&nbsp;&gt;&nbsp;<s:text name="CHA03_edit"></s:text></span> 
  </div>
</div>
<div id="actionResultDisplay"></div>
<div class="panel-content clearfix">
<div class="section">
  <div class="section-header">
	<strong><span class="ui-icon icon-ttl-section-info"></span><s:text name="CHA03_baseInfo"></s:text></strong>
  </div>
  <div class="section-content">
  <cherry:form id="update" csrftoken="false" onsubmit="save();return false;">
    <input type="hidden" name="channelId" value='<s:property value="channelId"/>'/>
    <s:hidden name="modifyTime" value="%{channelDetail.modifyTime}"></s:hidden>
	<s:hidden name="modifyCount" value="%{channelDetail.modifyCount}"></s:hidden>
    <table class="detail" cellpadding="0" cellspacing="0">
  	  <tr>
  	    <th><s:text name="CHA03_channelCode"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
        <td><span><s:textfield name="channelCode" cssClass="text" value="%{channelDetail.channelCode}" maxlength="50"/></span></td>
        <th><s:text name="CHA03_channelName"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
        <td><span><s:textfield name="channelName" cssClass="text" value="%{channelDetail.channelName}" maxlength="50"/></span></td>
      </tr>
      <tr>
        <th><s:text name="CHA03_channelNameForeign"></s:text></th>
        <td><span><s:textfield name="channelNameForeign" cssClass="text" value="%{channelDetail.channelNameForeign}" maxlength="50"></s:textfield></span></td>  
        <th><s:text name="CHA03_status"></s:text></th>
        <td><span><s:select name="status" list='#application.CodeTable.getCodes("1121")' 
	                 	listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{global.select}" value="channelDetail.status"/></span></td>
      </tr>
      <tr>
	    <th><s:text name="CHA03_joinDate"></s:text></th>
		<td><span><s:textfield name="joinDate" cssClass="date" value="%{channelDetail.joinDate}"></s:textfield></span></td>
	  </tr>
    </table>    
 </cherry:form>  
  </div>
</div> 
 <div class="center clearfix" id="pageButton">
          <button class="save" onclick="save();return false;">
          <s:a action="BINOLBSCHA03_save" id="CHA03_save" cssStyle="display: none;"></s:a>
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
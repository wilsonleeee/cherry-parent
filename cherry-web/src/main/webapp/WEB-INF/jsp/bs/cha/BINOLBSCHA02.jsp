<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<script type="text/javascript" src="/Cherry/js/bs/cha/BINOLBSCHA02.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<s:i18n name="i18n.bs.BINOLBSCHA02">
<s:text name="CHA02_select" id="CHA02_select"/>
<div class="main container clearfix">
<div class="panel ui-corner-all">
<div id="div_main">
<div class="panel-header">
<cherry:form id="toEditForm" action="BINOLBSCHA03_init" method="post" csrftoken="false">
        	<input type="hidden" name="channelId" value='<s:property value="channelId"/>'/>
        	<input type="hidden" name="csrftoken" id="parentCsrftoken"/>
</cherry:form>
<div class="clearfix"> 
    <span class="breadcrumb left"> <span class="ui-icon icon-breadcrumb"></span><s:text name="CHA02_title1" />&nbsp;&gt;&nbsp;<s:text name="CHA02_detail"></s:text></span> 
  </div>
</div>
<div class="panel-content clearfix">
<div class="section">
  <div class="section-header">
	<strong><span class="ui-icon icon-ttl-section-info"></span><s:text name="CHA02_detail"></s:text></strong>
  </div>
  <div class="section-content" id="detail">
 <table class="detail" cellpadding="0" cellspacing="0">
  	  <tr>
  	  	  <th><s:text name="channelCode"></s:text></th>
          <td><span><s:property value="channelDetail.channelCode"/></span></td>
          <th><s:text name="channelName"></s:text></th>
          <td><span><s:property value="channelDetail.channelName"/></span></td>
      </tr>
      <tr>
      	  <th><s:text name="channelNameForeign"></s:text></th>
          <td><span><s:property value="channelDetail.channelNameForeign"/></span></td>
          <th><s:text name="status"></s:text></th>
          <td><span><s:property value='#application.CodeTable.getVal("1121",channelDetail.status)' /></span></td> 
      </tr>
      <tr>
      	  <th><s:text name="joinDate"></s:text></th>
          <td><span><s:property value="channelDetail.joinDate"/></span></td>
      </tr>
    </table>    
  </div>
</div> 
<div class="center">
<cherry:show domId="BSCHA02EDIT">
<button class="edit" onclick="edit();return false;">
<span class="ui-icon icon-edit-big"></span> <%-- 编辑按钮 --%>
<span class="button-text"><s:text name="global.page.edit"/></span>
</button>
</cherry:show>
<button class="close" type="button" onclick="window.close();return false;"><span class="ui-icon icon-close"></span><span class="button-text"><s:text name="global.page.close"/></span></button>
</div>
</div>
</div>
</div>
</div>
</s:i18n> 
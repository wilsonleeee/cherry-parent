<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<script type="text/javascript" src="/Cherry/js/pt/jcs/BINOLPTJCS41.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<s:i18n name="i18n.pt.BINOLPTJCS38">
<s:text name="CHA04_select" id="CHA04_select"/>
<div class="main container clearfix">
<div class="panel ui-corner-all">
<div id="div_main">
<div class="panel-header">
  <div class="clearfix"> 
    <span class="breadcrumb left"> <span class="ui-icon icon-breadcrumb"></span><s:text name="prtFun_title1" />&nbsp;&gt;&nbsp;<s:text name="JCS41_detail"></s:text></span>
  </div>
</div>
<div id="actionResultDisplay"></div>
<div class="panel-content clearfix">
<div class="section">
  <div class="section-header">
	<strong><span class="ui-icon icon-ttl-section-info"></span><s:text name="prtfun_baseInfo"></s:text></strong>
  </div>
  <div class="section-content">
  <cherry:form id="add" csrftoken="false" onsubmit="save();return false;">
    <table class="detail" id="detailId" cellpadding="0" cellspacing="0">
  	  <tr>
  	  	<th><s:text name="brandName"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
  	  	<td><span><s:select name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" cssStyle="width:100px;"></s:select></span></td>
        <th><s:text name="prtfun_startDate"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
        <td><span><s:textfield name="startDate" cssClass="date startTime" ></s:textfield></span></td>  
     </tr>
	 <tr> 
        <th><s:text name="prtFunType"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
        <td><span>
        <s:select name="prtFunType" id="prtFunType" list='#application.CodeTable.getCodes("1327")' 
         			listKey="CodeKey" listValue="Value" />
        </span></td>
        <th><s:text name="prtfun_endDate"></s:text></th>
        <td><span><s:textfield name="endDate" cssClass="date endTime" ></s:textfield></span></td>  
      </tr>
      <tr>
      <th><s:text name="prtFunDateName"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
      <td><span><s:textfield name="prtFunDateName" cssClass="text" maxlength="50"/></span></td>
      </tr>
    </table>    
 </cherry:form>  
  </div>
</div> 


<hr class="space" />
<div class="center">
  <s:a action="BINOLPTJCS41_save" id="JCS41_save" cssStyle="display: none;"></s:a>
  <button class="save" onclick="save();return false;"><span class="ui-icon icon-save"></span><span class="button-text"><s:text name="global.page.save"></s:text></span></button>
  <button class="close" type="button" onclick="window.close();return false;"><span class="ui-icon icon-close"></span><span class="button-text"><s:text name="global.page.close"/></span></button>
</div>
</div>
</div>
</div>
</div>
</s:i18n> 
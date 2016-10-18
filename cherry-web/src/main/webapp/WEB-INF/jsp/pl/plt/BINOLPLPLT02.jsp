<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>


<script type="text/javascript" src="/Cherry/js/pl/plt/BINOLPLPLT02.js"></script>



<s:i18n name="i18n.pl.BINOLPLPLT01">
<s:text name="global.page.select" id="select_default"></s:text>
<div class="main container clearfix">
<div class="panel ui-corner-all">
<div id="div_main">

<div class="panel-header">
  <div class="clearfix"> 
    <span class="breadcrumb left"> <span class="ui-icon icon-breadcrumb"></span><s:text name="privilege_manage" />&nbsp;&gt;&nbsp;<s:text name="add_plt" /></span> 
  </div>
</div>

<div id="actionResultDisplay"></div>

<div class="panel-content clearfix">
<div class="box3">
  <div><s:text name="privilegeConf"></s:text></div>
</div>
<div class="tabs" id="tabs"> 
  <ul>
	  <li><a href="#tabs-1"><s:text name="departPl"/></a></li>
	  <li><a href="#tabs-2"><s:text name="positionPl"/></a></li>
  </ul>
  <div id="tabs-1">
  <form id="addDepPltInfo">
  <s:hidden name="category" value="0"></s:hidden>
  <div class="section">
    <div class="section-content">
      <table class="detail" cellpadding="0" cellspacing="0">
    	<tr>
          <th style="width: 15%"><s:text name="departType"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
          <td style="width: 85%" class="td_point"><span><s:checkboxlist list='#application.CodeTable.getCodes("1000")' listKey="CodeKey" listValue="Value" name="departType"></s:checkboxlist></span></td>
        </tr>
        <tr>
          <th style="width: 15%"><s:text name="businessType"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
          <td style="width: 85%" class="td_point"><span><s:checkboxlist name="businessType" list='#application.CodeTable.getCodes("1048")' listKey="CodeKey" listValue="Value"></s:checkboxlist></span></td>
        </tr>
        <tr>
          <th style="width: 15%"><s:text name="operationType"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
          <td style="width: 85%"><span><s:checkboxlist name="operationType" list='#application.CodeTable.getCodes("1049")' listKey="CodeKey" listValue="Value"></s:checkboxlist></span></td>
        </tr>
        <tr>
          <th style="width: 15%"><s:text name="privilegeType"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
          <td style="width: 85%"><span><s:select list='#application.CodeTable.getCodes("1050")' listKey="CodeKey" listValue="Value" name="privilegeType" headerKey="" headerValue="%{select_default}" cssStyle="width:180px;"></s:select></span></td>
        </tr>
        <tr>
          <th style="width: 15%"><s:text name="exclusive_title"></s:text></th>
          <td style="width: 85%"><span><s:checkbox name="exclusive" fieldValue="1"></s:checkbox><s:text name="exclusive"></s:text></span></td>
        </tr>
      </table>      
    </div>
  </div> 
  </form>
  </div>
  <div id="tabs-2">
  <form id="addPosPltInfo">
  <s:hidden name="category" value="1"></s:hidden>
  <div class="section">
    <div class="section-content">
      <table class="detail" cellpadding="0" cellspacing="0">
    	<tr>
          <th style="width: 15%"><s:text name="positionCategoryId"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
          <td style="width: 85%" class="td_point"><span><s:checkboxlist list="posCategoryList" listKey="positionCategoryId" listValue="categoryName" name="positionCategoryId"></s:checkboxlist></span></td>
        </tr>
        <tr>
          <th style="width: 15%"><s:text name="businessType"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
          <td style="width: 85%" class="td_point"><span><s:checkboxlist name="businessType" list='#application.CodeTable.getCodes("1048")' listKey="CodeKey" listValue="Value"></s:checkboxlist></span></td>
        </tr>
        <tr>
          <th style="width: 15%"><s:text name="operationType"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
          <td style="width: 85%"><span><s:checkboxlist name="operationType" list='#application.CodeTable.getCodes("1049")' listKey="CodeKey" listValue="Value"></s:checkboxlist></span></td>
        </tr>
        <tr>
          <th style="width: 15%"><s:text name="privilegeType"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
          <td style="width: 85%"><span><s:select list='#application.CodeTable.getCodes("1050")' listKey="CodeKey" listValue="Value" name="privilegeType" headerKey="" headerValue="%{select_default}" cssStyle="width:180px;"></s:select></span></td>
        </tr>
        <tr>
          <th style="width: 15%"><s:text name="exclusive_title"></s:text></th>
          <td style="width: 85%"><span><s:checkbox name="exclusive" fieldValue="1"></s:checkbox><s:text name="exclusive"></s:text></span></td>
        </tr>
      </table>      
    </div>
  </div> 
  </form>
  </div>
</div>  

<hr class="space" />
<div class="center">
  <s:a action="BINOLPLPLT02_add" id="addPltUrl" cssStyle="display: none;"></s:a>
  <button class="save" onclick="plplt02_savePlt();return false;"><span class="ui-icon icon-save"></span><span class="button-text"><s:text name="global.page.save"></s:text></span></button>
  <button class="close" type="button" onclick="window.close();return false;"><span class="ui-icon icon-close"></span><span class="button-text"><s:text name="global.page.close"/></span></button>
</div>
 
</div>

</div>
</div>
</div>  
</s:i18n>

<%-- 实时刷新数据权限URL --%>
<s:url id="refreshPrivilegeUrl" value="/common/BINOLCMPL04_init">
	<s:param name="isReOrgRelPl" value="0"></s:param>
</s:url>
<s:hidden value="%{#refreshPrivilegeUrl}" id="refreshPrivilegeUrl"></s:hidden>


          
     
















      
 
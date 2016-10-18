<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/bs/common/BINOLBSCOM03.js"></script>
<script type="text/javascript" src="/Cherry/js/bs/pos/BINOLBSPOS06.js"></script>

<s:i18n name="i18n.bs.BINOLBSPOS06">
  <s:url id="add_url" action="BINOLBSPOS09_init"/>
  <s:text name="select_default" id="select_default"></s:text>
  <div class="panel-header">
    <div class="clearfix"> 
    <span class="breadcrumb left">	    
			<jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
	</span>
    <%-- 添加按钮 --%>
    <span class="right"> 
      <cherry:show domId="BINOLBSPOS0601">
	    <a href="${add_url}" class="add" onclick="javascript:openWin(this);return false;">
	      <span class="button-text"><s:text name="add_positionCategory"/></span><span class="ui-icon icon-add"></span>
	    </a>
      </cherry:show> 
    </span>
    </div>
  </div>
  
    <%-- ================== 错误信息提示 START ======================= --%>
    <div id="errorMessage"></div>    
    <div style="display: none" id="errorMessageTemp">
    <div class="actionError">
       <ul><li><span><s:text name="position.errorMessage"/></span></li></ul>         
    </div>
    </div>
    <%-- ================== 错误信息提示   END  ======================= --%>
  
  <div class="panel-content clearfix">

	<div class="box">
	  <cherry:form id="posCategoryCherryForm" class="inline" onsubmit="binolbspos06.search();return false;">
	    <div class="box-header"> <strong><span class="ui-icon icon-ttl-search"></span><s:text name="search_condition"></s:text></strong>
	      <input type="checkbox" name="validFlag" value="1"/><s:text name="position_invalid"/> </div>
	    <div class="box-content clearfix">
	      <div class="column" style="width:50%; height: auto;">
	      	<s:if test='%{brandInfoList != null && !brandInfoList.isEmpty()}'>
	        	<label><s:text name="brandInfo"></s:text></label>
	        	<s:select name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" headerKey="" headerValue="%{#select_default}"></s:select>
	      	</s:if>
	        <p>
	          <label><s:text name="categoryCode"></s:text></label>
	          <s:textfield name="categoryCode" cssClass="text"></s:textfield>
	        </p>
	        </div>
	       <div class="column last" style="width:49%; height: auto;">
	        <p>
	          <label><s:text name="categoryName"></s:text></label>
	          <s:textfield name="categoryName" cssClass="text"></s:textfield>
	        </p>
	      </div>
	    </div>
	    <p class="clearfix">
	      <cherry:show domId="BINOLBSPOS0605">
	      <button class="right search" onclick="binolbspos06.search();return false;"><span class="ui-icon icon-search-big"></span><span class="button-text"><s:text name="search_button"></s:text></span></button>
	      </cherry:show>
	    </p>
	  </cherry:form>
	</div>
	<div class="section hide" id="posCategoryInfoId">
	  <div class="section-header"><strong><span class="ui-icon icon-ttl-section-search-result"></span><s:text name="search_result"></s:text></strong></div>
	  <div class="section-content">
	    <div class="toolbar clearfix">
	      <span class="left">
	        <s:url action="BINOLBSPOS10_delete" id="delPosition"></s:url>
	        <cherry:show domId="BINOLBSPOS0602">
	        <a href="" class="add" onclick="binolbspos06.editValidFlag('enable','${delPosition}');return false;">
	           <span class="ui-icon icon-enable"></span>
	           <span class="button-text"><s:text name="global.page.enable"/></span>
	        </a>
	        </cherry:show>
	        <cherry:show domId="BINOLBSPOS0603">
	        <a href="" class="delete" onclick="binolbspos06.editValidFlag('disable','${delPosition}');return false;">
	           <span class="ui-icon icon-disable"></span>
	           <span class="button-text"><s:text name="global.page.disable"/></span>
	        </a>
	        </cherry:show>
	      </span>
	    </div>
	    <table cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%" id="dataTable">
	      <thead>
	        <tr>
	          <th><input type="checkbox" id="checkAll" onclick="bscom03_checkRecord(this,'#dataTable_Cloned');"/></th>
	          <th><s:text name="categoryCode"></s:text><span class="ui-icon-document css_right ui-icon ui-icon-triangle-1-n"></span><span class="css_right ui-icon ui-icon-triangle-1-n"></span></th>
	          <th><s:text name="categoryName"></s:text></th>
	          <th><s:text name="posGrade"></s:text></th>
	          <th><s:text name="validFlag"></s:text></th>
	        </tr>
	      </thead>
	      <tbody>
	      </tbody>
	    </table>
	  </div>
	</div>

  </div>
  
  <div style="display: none;">
	<div id="privilegeTitle"><s:text name="global.page.privilegeTitle" /></div>
	<div id="privileMessage"><p class="message"><span><s:text name="global.page.privileMessage" /></span></p></div>
	<div id="disableTitle"><s:text name="position.disableTitle" /></div>
	<div id="enableTitle"><s:text name="position.enableTitle" /></div>
	<div id="disableMessage"><p class="message"><span><s:text name="position.disableMessage" /></span></p></div>
	<div id="enableMessage"><p class="message"><span><s:text name="position.enableMessage" /></span></p></div>
	<div id="dialogConfirm"><s:text name="global.page.ok" /></div>
    <div id="dialogCancel"><s:text name="global.page.cancle" /></div>
    <div id="dialogClose"><s:text name="global.page.close" /></div>
  </div>
</s:i18n>  

<div class="hide">
<s:url action="BINOLBSPOS06_list" id="posCategoryUrl"></s:url>
<a href="${posCategoryUrl }" id="posCategoryUrl"></a>
</div>
<div class="hide" id="dialogInit"></div>  
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />























 

    

 
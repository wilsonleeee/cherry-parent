<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/bs/sam/BINOLBSSAM04.js"></script>
<script type="text/javascript" src="/Cherry/js/bs/common/BINOLBSCOM03.js"></script>
<%-- 销售与提成率维护 --%>
<div class="hide">
	<!-- 查询 -->
	<s:url id="s_dgSearch" value="/basis/BINOLBSSAM04_Search"></s:url>
	<a id="searchUrl" href="${s_dgSearch}"></a>
	<!-- 删除 -->
	<s:url id="s_dgDelete" value="/basis/BINOLBSSAM04_delete"></s:url>
	<a id="deleteUrl" href="${s_dgDelete}"></a>
	<!-- 添加初始化 -->
	<s:url id="s_dgAddInit" value="/basis/BINOLBSSAM04_addSalesBonusRateInit"></s:url>
</div>
<s:i18n name="i18n.bs.BINOLBSSAM04">
<cherry:form id="mainForm"  class="inline">
<s:text name="global.page.all" id="select_default"/>
    <div class="panel-header">
        <div class="clearfix"> 
			<span class="breadcrumb left">	    
				<jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
			</span>
			<span class="right"> 
				<a onclick="javascript:openWin(this);return false;" class="add"  href="${s_dgAddInit}">
					<span class="button-text"><s:text name="BSSAM04_add"></s:text></span>
					<span class="ui-icon icon-add"></span>
				</a>
			</span>
        </div>
    </div>
 <div id="errorMessage"></div>
 <div id="actionResultDisplay"></div>
<div class="panel-content clearfix">
<div class="box">
  
    <div class="box-header"> 
      <strong><span class="ui-icon icon-ttl-search"></span><s:text name="global.page.condition"/></strong>
    </div>
    <div class="box-content clearfix">
      <div class="column" style="width:50%; height: auto;">
     	<p>
          <label style="width:80px;"><s:text name="BSSAM04_bonusEmployeePosition"></s:text></label><s:text name=""></s:text>
            <span>
                  <s:select headerKey="" headerValue="%{global.select}" cssClass="text" cssStyle="width:185px;" id="bonusEmployeePosition" name="bonusEmployeePosition" list="positionCategoryList" listKey="BIN_PositionCategoryID" listValue="CategoryName" value="%{editSaleMap.bonusEmployeePosition}"></s:select>
			</span>
        </p>
        <p>
          <label style="width:80px;"><s:text name="BSSAM04_saleEmployeePosition"></s:text></label>
            <span>
                  <s:select headerKey="" headerValue="%{global.select}" cssClass="text" cssStyle="width:185px;" id="saleEmployeePosition" name="saleEmployeePosition" list="positionCategoryList" listKey="BIN_PositionCategoryID" listValue="CategoryName" value="%{editSaleMap.bonusEmployeePosition}"></s:select>
			</span>
        </p>
      </div>
      <div class="column last" style="width:49%; height: auto;">
        <p>
          <label><s:text name="BSSAM04_counterCode"></s:text></label>
            <span>
                  <input id="counterCode" name="counterCode" class="text">
			</span>
        </p>
      </div>
    </div>
    <p class="clearfix">
      <button class="right search" onclick="BINOLBSSAM04.search();return false;">
	      <span class="ui-icon icon-search-big"></span>
		      <span class="button-text">
		      <s:text name="global.page.search"></s:text>
	      </span>
      </button>
    </p>
</div>
<div class="section hide" id="section">
  <div class="section-header">
  	<strong>
	  	<span class="ui-icon icon-ttl-section-search-result"></span>
	  	<s:text name="global.page.list"></s:text>
  	</strong>
	<span class="right"> <%-- 设置列 --%>
			<a class="setting"> 
				<span class="ui-icon icon-setting"></span> 
				<span class="button-text"><s:text name="global.page.colSetting" /></span> 
			</a>
	</span>
  </div>

  <div class="section-content">
    <table cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%" id="saleListTable">
      <thead>
        <tr>
          <th><s:text name="BSSAM04_no"/></th>
          <th><s:text name="BSSAM04_bonusEmployeePosition"></s:text></th>
          <th><s:text name="BSSAM04_saleEmployeePosition"></s:text></th>
          <th><s:text name="BSSAM04_counterCode"></s:text></th>
          <th><s:text name="BSSAM04_beginAmount"></s:text></th>
          <th><s:text name="BSSAM04_endAmount"></s:text></th>
          <th><s:text name="BSSAM04_bonusRate"></s:text></th>
          <th><s:text name="BSSAM04_memo"></s:text></th>
          <th><s:text name="BSSAM04_act"></s:text></th>
        </tr>
      </thead>
      <tbody>
      </tbody>
    </table>
  </div>
</div>
</div>
 </cherry:form>
<div class="hide" id="messageDialogTitle"><s:text name="BSSAM04_message"></s:text></div>
<div id="messageDialogDiv" class="hide ui-dialog-content ui-widget-content" style="display: none; width: auto; min-height: 200px;">
	<p id="messageContent" class="message hide" style="margin:40px auto 30px;"><span id="messageContentSpan"></span></p>
	<p id="successContent" class="success hide" style="margin:40px auto 30px;"><span id="successContentSpan"></span></p>
	<p class="center">
		<button id="btnMessageConfirm" class="close" type="button">
    		<span class="ui-icon icon-confirm"></span>
            <span class="button-text"><s:text name="BSSAM04_confirm"/></span>
		</button>
	</p>
</div>
</s:i18n>  
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
 <script type="text/javascript">
	 	$(document).ready(function() {
	 		BINOLBSSAM04.search();
		});
    </script>
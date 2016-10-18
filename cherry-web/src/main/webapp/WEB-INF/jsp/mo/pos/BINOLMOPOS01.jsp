<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/mo/pos/BINOLMOPOS01.js"></script>
<script type="text/javascript" src="/Cherry/js/bs/common/BINOLBSCOM03.js"></script>
<%-- 云POS支付方式配置 --%>
<div class="hide">
	<!-- 查询 -->
	<s:url id="s_dgSearch" value="/mo/BINOLMOPOS01_search"></s:url>
	<a id="searchUrl" href="${s_dgSearch}"></a>
	<!-- 删除 -->
	<s:url id="s_dgDelete" value="/mo/BINOLMOPOS01_delStorePayConfig"></s:url>
	<a id="deleteUrl" href="${s_dgDelete}"></a>
	<!-- 添加初始化 -->
	<s:url id="s_dgAddInit" value="/mo/BINOLMOPOS01_addInit"></s:url>
	<a id="addInitUrl" href="${s_dgAddInit}"></a>
	<!-- 添加 -->
	<s:url id="s_dgAdd" value="/mo/BINOLMOPOS01_addStorePayConfig"></s:url>
	<a id="addUrl" href="${s_dgAdd}"></a>
	<!-- 修改 -->
	<s:url id="s_dgEdit" value="/mo/BINOLMOPOS01_editStorePayConfig"></s:url>
	<a id="editUrl" href="${s_dgEdit}"></a>
</div>
<s:i18n name="i18n.mo.BINOLMOPOS01">
<cherry:form id="mainForm"  class="inline">
<s:text name="global.page.all" id="select_default"/>
    <div class="panel-header">
        <div class="clearfix"> 
			<span class="breadcrumb left">	    
				<jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
			</span>
			<span class="right"> 
				<a onclick="BINOLMOPOS01.addInit();return false;" class="add">
					<span class="button-text"><s:text name="MOPOS01_add"></s:text></span>
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
          <label style="width:80px;"><s:text name="MOPOS01_storePayCode"></s:text></label>
            <span>
                  <s:textfield cssClass="text" name="storePayCode"></s:textfield>
			</span>
        </p>
      </div>
      <div class="column last" style="width:49%; height: auto;">
        <p>
          <label><s:text name="MOPOS01_storePayValue"></s:text></label>
            <span>
                  <s:textfield cssClass="text" name="storePayValue" ></s:textfield>
			</span>
        </p>
      </div>
    </div>
    <p class="clearfix">
      <button class="right search" onclick="BINOLMOPOS01.search();return false;">
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
  </div>

  <div class="section-content">
  	<div class="toolbar clearfix">
  	<span class="left">
  		<a href="/Cherry/mo/BINOLMOPOS01_editStorePayConfig.action" class="add" onclick="BINOLMOPOS01.editConfirm(this,'enable');return false;">
	        <span class="ui-icon icon-enable"></span>
	        <span class="button-text"><s:text name="MOPOS01_isEnable"></s:text></span>
	    </a>
        <a href="/Cherry/mo/BINOLMOPOS01_editStorePayConfig.action" class="delete" onclick="BINOLMOPOS01.editConfirm(this,'disable');return false;">
	        <span class="ui-icon icon-disable"></span>
	        <span class="button-text"><s:text name="MOPOS01_noEnable"></s:text></span>
	    </a>
        <a href="/Cherry/mo/BINOLMOPOS01_editStorePayConfig.action" class="delete" onclick="BINOLMOPOS01.editConfirm(this,'delete');return false;">
	        <span class="ui-icon icon-delete"></span>
	        <span class="button-text"><s:text name="MOPOS01_del"></s:text></span>
	    </a>
	    <a href="/Cherry/mo/BINOLMOPOS01_editStorePayConfig.action" class="delete" onclick="BINOLMOPOS01.editConfirm(this,'default');return false;">
	        <span class="ui-icon icon-enable"></span>
	        <span class="button-text"><s:text name="MOPOS01_defaultPay3"></s:text></span>
	    </a>
  	</span>
	<span class="right"> <%-- 设置列 --%>
			<a class="setting"> 
				<span class="ui-icon icon-setting"></span> 
				<span class="button-text"><s:text name="global.page.colSetting" /></span> 
			</a>
	</span>
  	</div>
    <table cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%" id="storePayConfigTable">
      <thead>
        <tr>
          <th>
          	<s:checkbox id="checkAllPay" name="validFlag" onclick="BINOLMOPOS01.checkRecord(this,'#storePayConfigTableTbode');"></s:checkbox>
          </th>
          <th><s:text name="MOPOS01_storePayCode"></s:text></th>
          <th><s:text name="MOPOS01_storePayValue"></s:text></th>
          <th><s:text name="MOPOS01_payType"></s:text></th>
          <th><s:text name="MOPOS01_status"></s:text></th>
          <th><s:text name="MOPOS01_defaultPay"></s:text></th>
          <%-- <th><s:text name="MOPOS01_act"></s:text></th> --%>
        </tr>
      </thead>
      <tbody id="storePayConfigTableTbode">
      </tbody>
    </table>
  </div>
</div>
</div>
 </cherry:form>
<div class="hide" id="dialogInit"></div>
<div class="hide" id="messageDialogTitle"><s:text name="MOPOS01_message"></s:text></div>
<div id="messageDialogDiv" class="hide ui-dialog-content ui-widget-content" style="display: none; width: auto; min-height: 200px;">
	<p id="messageContent" class="message hide" style="margin:40px auto 30px;"><span id="messageContentSpan"></span></p>
	<p id="successContent" class="success hide" style="margin:40px auto 30px;"><span id="successContentSpan"></span></p>
	<p class="center">
		<button id="btnMessageConfirm" class="close" type="button">
    		<span class="ui-icon icon-confirm"></span>
            <span class="button-text"><s:text name="MOPOS01_confirm"/></span>
		</button>
	</p>
</div>
</s:i18n>  
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />

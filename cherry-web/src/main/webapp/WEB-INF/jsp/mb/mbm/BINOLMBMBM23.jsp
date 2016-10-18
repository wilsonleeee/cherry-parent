<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/mb/mbm/BINOLMBMBM23.js"></script>

<s:i18n name="i18n.mb.BINOLMBMBM23"> 	
 	<div class="crm_content_header clearfix">
	  <span class="left">
	  <span class="icon_crmnav"></span>
	  <span><s:text name="binolmbmbm23_title" /></span>
	  </span>
	</div>
	<div id="actionResultDisplay"></div>
	<div class="panel-content clearfix">
	
	<div class="section">
        <div class="section-content">
	  
		  <div class="box">
		  <form id="smsSendDetailCherryForm" class="inline" onsubmit="binolmbmbm23.searchSmsSendDetailList();return false;" >
		  <s:hidden name="memberInfoId"></s:hidden>
		  <input type="hidden" id="parentCsrftoken" name="csrftoken"/>
		  <div class="box-header">
		  <strong><span class="ui-icon icon-ttl-search"></span><s:text name="global.page.condition"/></strong>
		  </div>
		  <div class="box-content clearfix">
		  <div class="column last" style="width:50%; height: auto;">
	        <p>
	          <label style="width:60px;"><s:text name="binolmbmbm23_sendTime"/></label>
	          <span><s:textfield name="sendTimeStart" cssClass="date"/></span>-<span><s:textfield name="sendTimeEnd" cssClass="date"/></span>
	        </p>
	      </div>
	      
		  </div>
		  <p class="clearfix">
	          <button class="right search" onclick="binolmbmbm23.searchSmsSendDetailList();return false;">
	          	<span class="ui-icon icon-search-big"></span>
	          	<span class="button-text"><s:text name="global.page.search"></s:text></span>
	          </button>
	      </p>
		  </form>
		  </div>
		  <div class="toolbar clearfix">
		  	  <span class="left">
		        <s:url action="BINOLCTCOM07_init" namespace="/ct" id="sendviewUrl">
		          <s:param name="customerSysId" value="memberInfoId"></s:param>
		          <s:param name="brandInfoId" value="brandInfoId"></s:param>
		          <s:param name="mobilePhone" value="mobilePhone"></s:param>
		          <s:param name="sourse">BINOLMBMBM23</s:param>
		        </s:url>
		        <a class="edit" href="${sendviewUrl}" onclick="binolmbmbm23.sendmsgtest(this);return false;">
		          <span class="ui-icon icon-copy"></span>
		          <span class="button-text"><s:text name="binolmbmbm23_sendMessage"></s:text></span>
		        </a>
		      </span>
		  </div>
	  	  <table cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%" id="smsSendDetailDataTable">
		      <thead>
		        <tr>
		          <th><s:text name="binolmbmbm23_message"></s:text></th>
		          <th><s:text name="binolmbmbm23_mobilephone"></s:text></th>
		          <th><s:text name="binolmbmbm23_planName"></s:text></th>
		          <th><s:text name="binolmbmbm23_couponCode"></s:text></th>
		          <th><s:text name="binolmbmbm23_sendTime"></s:text></th>  
		          <th><s:text name="binolmbmbm23_operate"></s:text></th>  
		        </tr>
		      </thead>
		      <tbody>
		      </tbody>
		  </table>
		  
	      
		</div>
	</div>
	
	
    
    </div>
    
<div style="display: none;">
	<div id="showDetailTitle"><s:text name="binolmbmbm23_message" /></div>
    <div id="dialogClose"><s:text name="global.page.close" /></div>
    <div id="sendDialogTitle"><s:text name="binolmbmbm23_sendMessage"></s:text></div>
    <div id="dialogConfirm"><s:text name="global.page.dialogConfirm" /></div>
	<div id="dialogCancel"><s:text name="global.page.dialogCancel" /></div>
	<div id="sendMsgInitDialogText"><s:text name="binolmbmbm23_sendMsgInitDialogText" /></div>
	<div id="sendMsgInitDialogTitle"><s:text name="binolmbmbm23_sendMsgInitDialogTitle" /></div>
</div>  
      
</s:i18n>   

<div class="hide">
	<s:url action="BINOLMBMBM23_search" id="searchSmsSendDetailUrl"></s:url>
	<a href="${searchSmsSendDetailUrl }" id="searchSmsSendDetailUrl"></a>
</div>
<div class="hide" id="dialogInit"></div>
<div id="div_main" class="hide"></div> 
<div id="dialogDetail" class="hide">
  <div class="box2-active">
	<div class="box2 box2-content ui-widget" >
		<div style="word-break:break-all;word-wrap:break-word;">
			<label style="display: block;margin-right: 0px;" id="dialogContent"></label>
		</div>
	</div>
  </div>  
</div>
<div class="hide" id="sendDialogInit"></div>
<div class="hide" id="templateDialogInit"></div>
  

<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>  
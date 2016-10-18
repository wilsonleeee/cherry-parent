<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/mb/mbm/BINOLMBMBM20.js"></script>

<s:i18n name="i18n.mb.BINOLMBMBM20"> 	
 	<div class="crm_content_header">
	  <span class="icon_crmnav"></span>
	  <span><s:text name="binolmbmbm20_memWarnInfo" /></span>
	</div>
	
	<%-- ================== 错误信息提示 START ======================= --%>
    <div id="actionResultDisplay"></div>
    <div id="errorMessage"></div>    
    <div style="display: none" id="errorMessageTemp">
    <div class="actionError">
       <ul><li><span><s:text name="binolmbmbm20_deleteError" /></span></li></ul>         
    </div>
    </div>
    <%-- ================== 错误信息提示   END  ======================= --%>
	<div class="panel-content clearfix">
	
	<div class="section">
        <div class="section-content">
        
		<div class="box">
		<cherry:form id="memWarnInfoForm" class="inline" onsubmit="binolmbmbm20.searchMemWarnInfo();return false;" csrftoken="false">
		  <s:hidden name="memberInfoId"></s:hidden>
		  <div class="box-header">
		  <strong><span class="ui-icon icon-ttl-search"></span><s:text name="global.page.condition"/></strong>
		  </div>
		  <div class="box-content clearfix">
		  <div class="column" style="width:50%; height: auto;">
	        <p>
	          <label><s:text name="binolmbmbm20_tradeType"/></label>
	          <s:textfield name="tradeType" cssClass="text"/>
	        </p>
	        <p>
	          <label><s:text name="binolmbmbm20_tradeNoIF"/></label>
	          <s:textfield name="tradeNoIF" cssClass="text"/>
	        </p>
	        <p>
	          <label><s:text name="binolmbmbm20_putTime"/></label>
	          <span><s:textfield name="putTimeStart" cssClass="date" cssStyle="width: 80px;"/>-<s:textfield name="putTimeEnd" cssClass="date" cssStyle="width: 80px;"/></span>
	        </p>
	      </div>
	      <div class="column last" style="width:49%; height: auto;">
	        <p>
	          <label><s:text name="binolmbmbm20_errType"/></label>
	          <select name="errType">
                <option value=""><s:text name="global.page.select" /></option>
                <option value="0"><s:text name="binolmbmbm20_errType0" /></option>
                <option value="1"><s:text name="binolmbmbm20_errType1" /></option>
              </select>
	        </p>
	        <p>
	          <label><s:text name="binolmbmbm20_errInfo"/></label>
	          <s:textfield name="errInfo" cssClass="text"/>
	        </p>
	      </div>
		  </div>
		  <p class="clearfix">
	          <button class="right search" onclick="binolmbmbm20.searchMemWarnInfo();return false;"><span class="ui-icon icon-search-big"></span><span class="button-text"><s:text name="global.page.search"></s:text></span></button>
	      </p>
		</cherry:form>
		</div>
		
		<div id="memWarnInfoDataTableDiv" class="hide">
		<div class="toolbar clearfix">
          <span class="left">
         	<s:url action="BINOLMBMBM20_delete" id="delmemWarnUrl"></s:url>
            <a class="delete" onclick="binolmbmbm20.delete('${delmemWarnUrl}');return false;">
               <span class="ui-icon icon-disable"></span>
               <span class="button-text"><s:text name="global.page.remove"/></span>
            </a>
          </span>
        </div>
		<table cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%" id="memWarnInfoDataTable">
	      <thead>
	        <tr>
	          <th><input type="checkbox" id="checkAll" onclick="binolmbmbm20_checkRecord(this,'#memWarnInfoDataTable');"/></th>
              <%-- 业务类型 --%>
              <th><s:text name="binolmbmbm20_tradeType" /></th>
              <%-- 单据号 --%>
              <th><s:text name="binolmbmbm20_tradeNoIF" /></th>
              <%-- 错误类型 --%>
              <th><s:text name="binolmbmbm20_errType" /></th>
              <%-- 错误信息 --%>
              <th><s:text name="binolmbmbm20_errInfo" /></th>
              <%-- 发生时间 --%>
              <th><s:text name="binolmbmbm20_putTime" /></th>
	        </tr>
	      </thead>
	      <tbody>
	      </tbody>
	    </table>
	    </div>
    
    	</div>
    </div>
    
    </div>


<div style="display: none;">
	<div id="showDetailTitle"><s:text name="binolmbmbm20_showDetailTitle" /></div>
	<div id="deleteTitle"><s:text name="binolmbmbm20_deleteTitle" /></div>
	<div id="deleteMessage"><p class="message"><span><s:text name="binolmbmbm20_deleteMessage" /></span></p></div>
	<div id="dialogConfirm"><s:text name="global.page.ok" /></div>
    <div id="dialogCancel"><s:text name="global.page.cancle" /></div>
    <div id="dialogClose"><s:text name="global.page.close" /></div>
</div>      
</s:i18n>   

<div class="hide">
	<s:url action="BINOLMBMBM20_search" id="memWarnInfoUrl"></s:url>
	<a href="${memWarnInfoUrl }" id="memWarnInfoUrl"></a>
</div>
<div class="hide" id="dialogInit"></div>
<div id="dialogDetail" class="hide">
  <div class="box2-active">
	<div class="box2 box2-content ui-widget" >
		<div style="word-break:break-all;word-wrap:break-word;">
			<label style="display: block;margin-right: 0px;" id="dialogContent"></label>
		</div>
	</div>
  </div>  
</div>

<div id="div_main" class="hide"></div>       		 

<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>  
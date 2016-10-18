<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/mo/wat/BINOLMOWAT06.js"></script>

<script type="text/javascript">
$(document).ready(function() {
	// 节日
	var holidays = '${holidays }';
	$('#putTimeStart').cherryDate({holidayObj: holidays});
	$('#putTimeEnd').cherryDate({
		holidayObj: holidays,
		beforeShow: function(input){
			var value = $('#putTimeStart').val();
			return [value,'minDate'];
		}
	});
});
</script>

<s:i18n name="i18n.mo.BINOLMOWAT06">
	<s:text name="global.page.select" id="select_default"/>
    <div class="panel-header">
	  <div class="clearfix"> 
	 	<span class="breadcrumb left"><span class="ui-icon icon-breadcrumb"></span><s:text name="binolmowat06_breadcrumb" />&nbsp;&gt;&nbsp;<s:text name="binolmowat06_title" /></span>
	  </div>
    </div>
    <%-- ================== 错误信息提示 START ======================= --%>
    <div id="actionResultDisplay"></div>
    <div id="errorMessage"></div>    
    <div style="display: none" id="errorMessageTemp">
    <div class="actionError">
       <ul><li><span><s:text name="binolmowat06_deleteError" /></span></li></ul>         
    </div>
    </div>
    <%-- ================== 错误信息提示   END  ======================= --%>
    <div class="panel-content">
      <div class="box">
        <cherry:form id="mainForm" method="post" class="inline" onsubmit="binolmowat06.search();return false;">
          <div class="box-header">
          	<strong><span class="ui-icon icon-ttl-search"></span><s:text name="global.page.condition"/></strong>
          </div>
          <div class="box-content clearfix">
            <div class="column" style="width:50%; height: auto;">
              <p>
              	<label><s:text name="binolmowat06_tradeType" /></label>
                <s:textfield name="tradeType" cssClass="text"/>
              </p>
              <p>
              	<label><s:text name="binolmowat06_tradeNoIF" /></label>
                <s:textfield name="tradeNoIF" cssClass="text"/>
              </p>
              <p>
               	<label><s:text name="binolmowat06_putTime" /></label>
               	<s:textfield name="putTimeStart" cssClass="date"/>-<s:textfield name="putTimeEnd" cssClass="date"/>
              </p>
            </div>
            <div class="column last" style="width:49%; height: auto;">
              <p>
		        <label><s:text name="binolmowat06_errType"/></label>
		        <select name="errType">
	              <option value=""><s:text name="global.page.select" /></option>
	              <option value="0"><s:text name="binolmowat06_errType0" /></option>
	              <option value="1"><s:text name="binolmowat06_errType1" /></option>
	            </select>
		      </p>
              <p>
               	<label><s:text name="binolmowat06_errInfo" /></label>
               	<s:textfield name="errInfo" cssClass="text"/>
              </p>
              <p>
              	<label><s:text name="binolmowat06_messageBody" /></label>
                <s:textfield name="messageBody" cssClass="text"/>
              </p>
            </div>
          </div>
          <p class="clearfix">
         	<button class="right search" onclick="binolmowat06.search();return false;">
         		<span class="ui-icon icon-search-big"></span><span class="button-text"><s:text name="global.page.search"/></span>
         	</button>
          </p>
        </cherry:form>
      </div>
      <div id="section" class="section hide">
        <div class="section-header">
        	<strong>
        		<span class="ui-icon icon-ttl-section-search-result"></span>
        		<s:text name="global.page.list"/>
        	</strong>
        </div>
        <div class="section-content">
          <div class="toolbar clearfix">
           	<span class="left">
           		<s:url action="BINOLMOWAT06_delete" id="delMQWarnUrl"></s:url>
                <cherry:show domId="BINOLMOWAT0601">
                <a class="delete" onclick="binolmowat06.delete('${delMQWarnUrl}');return false;">
                   <span class="ui-icon icon-disable"></span>
                   <span class="button-text"><s:text name="global.page.delete"/></span>
                </a>
                </cherry:show>
            </span>
          </div>
          <table id="dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
            <thead>
              <tr>              
                <th><input type="checkbox" id="checkAll" onclick="mowat06_checkRecord(this,'#dataTable');"/></th>
                <%-- 业务类型 --%>
                <th><s:text name="binolmowat06_tradeType" /></th>
                <%-- 单据号 --%>
                <th><s:text name="binolmowat06_tradeNoIF" /></th>
                <%-- 错误类型 --%>
                <th><s:text name="binolmowat06_errType"/></th>
                <%-- 消息体 --%>
                <th><s:text name="binolmowat06_messageBody" /></th>
                <%-- 错误信息 --%>
                <th><s:text name="binolmowat06_errInfo" /></th>
                <%-- 发生时间 --%>
                <th><s:text name="binolmowat06_putTime" /></th>
              </tr>
            </thead>           
          </table>
        </div>
      </div>
    </div> 
<div style="display: none;">
	<div id="showDetailTitle"><s:text name="binolmowat06_showDetailTitle" /></div>
	<div id="deleteTitle"><s:text name="binolmowat06_deleteTitle" /></div>
	<div id="deleteMessage"><p class="message"><span><s:text name="binolmowat06_deleteMessage" /></span></p></div>
	<div id="dialogConfirm"><s:text name="global.page.ok" /></div>
    <div id="dialogCancel"><s:text name="global.page.cancle" /></div>
    <div id="dialogClose"><s:text name="global.page.close" /></div>
</div>     
</s:i18n>

<%-- 员工查询URL --%>
<s:url id="search_url" value="BINOLMOWAT06_search"/>
<s:hidden name="search_url" value="%{search_url}"/>
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
<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>
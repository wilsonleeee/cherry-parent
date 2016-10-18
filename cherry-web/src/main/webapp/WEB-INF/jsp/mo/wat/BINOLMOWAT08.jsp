<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>

<s:if test='null != subType && !"".equals(subType)'>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
</s:if>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/mo/wat/BINOLMOWAT08.js"></script>

<script type="text/javascript">
$(document).ready(function() {
	// 节日
	var holidays = '${holidays }';
	$('#timeStart').cherryDate({
		holidayObj: holidays,
		beforeShow: function(input){
			var value = $('#timeEnd').val();
			return [value,'maxDate'];
		}
	});
	$('#timeEnd').cherryDate({
		holidayObj: holidays,
		beforeShow: function(input){
			var value = $('#timeStart').val();
			return [value,'minDate'];
		}
	});
	// MQ消息错误日志查询
	BINOLMOWAT08.search('${subType}');
});
</script>

<s:i18n name="i18n.mo.BINOLMOWAT08">
	<s:text name="global.page.select" id="select_default"/>
	<s:if test='null == subType || "".equals(subType)'>
	    <div class="panel-header">
		  <div class="clearfix"> 
		 	<span class="breadcrumb left"><span class="ui-icon icon-breadcrumb"></span><s:text name="MOWAT08_breadcrumb" />&nbsp;&gt;&nbsp;<s:text name="MOWAT08_title" /></span>
		  </div>
	    </div>
    </s:if>
    <%-- ================== 错误信息提示 START ======================= --%>
    <div id="actionResultDisplay"></div>
    <div id="errorMessage"></div>    
    <div style="display: none" id="errorMessageTemp">
    <div class="actionError">
       <ul><li><span><s:text name="MOWAT08_deleteError" /></span></li></ul>         
    </div>
    </div>
    <%-- ================== 错误信息提示   END  ======================= --%>
    <div class="panel-content">
      <div class="box">
      	<s:if test='null == subType || "".equals(subType)'>
        <cherry:form id="mainForm" class="inline">
          <div class="box-header">
          	<strong><span class="ui-icon icon-ttl-search"></span><s:text name="global.page.condition"/></strong>
          </div>
          <div class="box-content clearfix">
            <div class="column" style="width:50%; height: auto;">
	        	<p>
		           	<%-- 所属品牌 --%>
		           	<s:if test="%{brandInfoList.size()> 1}">
		               	<label><s:text name="MOWAT08_brand"></s:text></label>
		               	<s:text name="global.page.select" id="MOWAT08_select"/>
		               	<s:select name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" headerKey="" headerValue="%{#MOWAT08_select}" cssStyle="width:100px;"></s:select>
		           	</s:if><s:else>
		           		<label><s:text name="MOWAT08_brand"></s:text></label>
		               	<s:select name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" cssStyle="width:100px;"></s:select>      	
		           	</s:else>
	           	</p>
              <p>
              	<!-- 消息类型 -->
		        <label><s:text name="MOWAT08_subType"/></label>
		        <s:text name="global.page.all" id="MOWAT08_selectAll"/>
               	<s:select name="subType" list="#application.CodeTable.getCodes('1316')"
              		listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{MOWAT08_selectAll}"/>
		      </p>
            </div>
            <div class="column last" style="width:49%; height: auto;">
              <p>
              	<!-- 消息时间-->
               	<label><s:text name="MOWAT08_time" /></label>
               	<s:textfield name="timeStart" cssClass="date"/>-<s:textfield name="timeEnd" cssClass="date"/>
              </p>
              <p>
              	<!-- 错误码 -->
              	<label><s:text name="MOWAT08_errorCode" /></label>
                <s:textfield name="errorCode" cssClass="text"/>
              </p>
            </div>
          </div>
          <p class="clearfix">
         	<button class="right search" onclick="BINOLMOWAT08.search();return false;">
         		<span class="ui-icon icon-search-big"></span><span class="button-text"><s:text name="global.page.search"/></span>
         	</button>
          </p>
        </cherry:form>
     </s:if><s:else>
        <form id="mainForm">
        	<s:hidden name="brandInfoId"></s:hidden>
        	<s:hidden name="subType"></s:hidden>
        	<s:hidden name="code"></s:hidden>
        	<div class="box-header">
          	<strong><span class="ui-icon icon-ttl-search"></span><s:text name="global.page.condition"/></strong>
          </div>
          <div class="box-content clearfix">
            <div class="column" style="width:50%; height: auto;">
            	<p>
	              	<!-- 消息时间-->
	               	<label><s:text name="MOWAT08_time" /></label>
	               	<s:textfield name="timeStart" cssClass="date"/>-<s:textfield name="timeEnd" cssClass="date"/>
              	</p>
            </div>
            <div class="column last" style="width:49%; height: auto;">
              <p>
              	<!-- 错误码 -->
              	<label><s:text name="MOWAT08_errorCode" /></label>
                <s:textfield name="errorCode" cssClass="text"/>
              </p>
            </div>
          </div>
          <p class="clearfix">
         	<button class="right search" onclick="BINOLMOWAT08.search();return false;">
         		<span class="ui-icon icon-search-big"></span><span class="button-text"><s:text name="global.page.search"/></span>
         	</button>
          </p>
        </form>
     </s:else>
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
          </div>
          <table id="dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
            <thead>
              <tr>              
                <th><s:text name="MOWAT08_No" /></th>
                <%-- 消息类型 --%>
                <th><s:text name="MOWAT08_subType" /></th>
                <%-- 消息码 --%>
                <th><s:text name="MOWAT08_errorCode" /></th>
                <%-- 消息时间 --%>
                <th><s:text name="MOWAT08_time" /></th>
                <%-- 信息内容 --%>
                <th><s:text name="MOWAT08_content" /></th>
                <%-- 消息体 --%>
                <th><s:text name="MOWAT08_messageBody" /></th>
              </tr>
            </thead>           
          </table>
        </div>
      </div>
    </div> 
<div style="display: none;">
	<div id="showDetailTitle"><s:text name="MOWAT08_showDetailTitle" /></div>
	<div id="dialogConfirm"><s:text name="global.page.ok" /></div>
    <div id="dialogCancel"><s:text name="global.page.cancle" /></div>
    <div id="dialogClose"><s:text name="global.page.close" /></div>
</div>     
</s:i18n>

<%-- 终端消息反馈日志查询URL --%>
<s:url id="search_url" value="BINOLMOWAT08_search"/>
<s:hidden name="search_url" value="%{search_url}"/>
<div id="div_main"><div class="hide" id="dialogInit"></div></div>
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
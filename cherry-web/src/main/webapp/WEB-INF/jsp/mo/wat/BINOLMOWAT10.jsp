<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/mo/wat/BINOLMOWAT10.js"></script>

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

<s:i18n name="i18n.mo.BINOLMOWAT10">
	<s:text name="global.page.select" id="select_default"/>
    <div class="panel-header">
	  <div class="clearfix"> 
	 	<span class="breadcrumb left"><span class="ui-icon icon-breadcrumb"></span><s:text name="binolmowat10_breadcrumb" />&nbsp;&gt;&nbsp;<s:text name="binolmowat10_title" /></span>
	  </div>
    </div>
   
    <div class="panel-content">
      <div class="box">
        <cherry:form id="mainForm" method="post" class="inline" onsubmit="binolmowat10.search();return false;">
          <div class="box-header">
          	<strong><span class="ui-icon icon-ttl-search"></span><s:text name="global.page.condition"/></strong>
          </div>
          <div class="box-content clearfix">
            <div class="column" style="width:50%; height: auto;">
              <%-- 所属品牌 --%>
              <p>
		           	<s:if test="%{brandInfoList.size()> 1}">
		               	<label><s:text name="binolmowat10_brand"></s:text></label>
		               	<s:text name="global.page.select" id="MOWAT10_select"/>
		               	<s:select name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" headerKey="" headerValue="%{#MOWAT10_select}" cssStyle="width:100px;"></s:select>
		           	</s:if><s:else>
		           		<label><s:text name="binolmowat10_brand"></s:text></label>
		               	<s:select name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" cssStyle="width:100px;"></s:select>      	
		           	</s:else>
              </p>
              <%-- JobCode --%>
              <p>
              	<label><s:text name="binolmowat10_JobCode" /></label>
                <s:textfield name="jobCode" cssClass="text"/>
              </p>
            </div>
            <div class="column last" style="width:49%; height: auto;">
              <%-- 运行结果 --%>
              <p>
		        <label><s:text name="binolmowat10_result"/></label>
		        <select name="result">
	              <option value=""><s:text name="global.page.select" /></option>
	              <option value="S"><s:text name="binolmowat10_result0" /></option>
	              <option value="W"><s:text name="binolmowat10_result1" /></option>
	              <option value="E"><s:text name="binolmowat10_result2" /></option>
	            </select>
		      </p>
		      <%-- 运行时间 --%>
              <p>
               	<label><s:text name="binolmowat10_putTime" /></label>
               	<span><s:textfield name="putTimeStart" cssClass="date"/></span>
               	-
               	<span><s:textfield name="putTimeEnd" cssClass="date"/></span>
              </p>
            </div>
          </div>
          <p class="clearfix">
         	<button class="right search" onclick="binolmowat10.search();return false;">
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
          </div>
          <table id="dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
            <thead>
              <tr>  
                <%-- No. --%> 
                <th><s:text name="binolmowat10_num"/></th>   
                        
                <%-- 品牌 --%>
                <%-- <th><s:text name="binolmowat10_brand" /></th> --%>
                
                <%-- JobCode --%>
                <th><s:text name="binolmowat10_JobCode" /></th>
                <%-- 运行结果 --%>
                <th><s:text name="binolmowat10_result"/></th>
                <%-- 信息描述 --%>
                <th><s:text name="binolmowat10_comments" /></th>
                <%-- 开始时间--%>
                <th><s:text name="binolmowat10_startTime" /></th>
                <%-- 结束时间 --%>
                <th><s:text name="binolmowat10_endTime" /></th>
              </tr>
            </thead>           
          </table>
        </div>
      </div>
    </div> 
<div style="display: none;">
	<div id="showDetailTitle"><s:text name="binolmowat10_showComments" /></div>

    <div id="dialogClose"><s:text name="global.page.close" /></div>
</div>     
</s:i18n>

<%-- Job运行履历查询URL --%>
<s:url id="search_url" value="BINOLMOWAT10_search"/>
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
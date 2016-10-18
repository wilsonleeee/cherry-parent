<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/mo/wat/BINOLMOWAT11.js"></script>

<s:i18n name="i18n.mo.BINOLMOWAT11">
	<s:text name="global.page.select" id="select_default"/>
    <div class="panel-header">
	  <div class="clearfix"> 
	 	<span class="breadcrumb left"><span class="ui-icon icon-breadcrumb"></span><s:text name="binolmowat11_breadcrumb" />&nbsp;&gt;&nbsp;<s:text name="binolmowat11_title" /></span>
	  </div>
    </div>
   
    <div class="panel-content">
      <div class="box">
        <cherry:form id="mainForm" method="post" class="inline" onsubmit="binolmowat11.search();return false;">
          <div class="box-header">
          	<strong><span class="ui-icon icon-ttl-search"></span><s:text name="global.page.condition"/></strong>
          </div>
          <div class="box-content clearfix">
            <div class="column" style="width:50%; height: auto;">
              <%-- 所属品牌 --%>
              <p>
		           	<s:if test="%{brandInfoList.size()> 1}">
		               	<label><s:text name="binolmowat11_brand"></s:text></label>
		               	<s:text name="global.page.select" id="MOWAT10_select"/>
		               	<s:select name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" headerKey="" headerValue="%{#MOWAT10_select}" cssStyle="width:100px;"></s:select>
		           	</s:if><s:else>
		           		<label><s:text name="binolmowat11_brand"></s:text></label>
		               	<s:select name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" cssStyle="width:100px;"></s:select>      	
		           	</s:else>
              </p>
              <%-- unionIndex --%>
              <p>
		        <label><s:text name="binolmowat11_unionIndex"/></label>
		        <s:textfield name="unionIndex" cssClass="text"/>
		      </p>
		      <%-- unionIndex2 --%>
              <p>
		        <label><s:text name="binolmowat11_unionIndex2"/></label>
		        <s:textfield name="unionIndex2" cssClass="text"/>
		      </p>
            </div>
            <div class="column last" style="width:49%; height: auto;">
            <%-- JobCode --%>
              <p>
              	<label><s:text name="binolmowat11_JobCode" /></label>
                <s:textfield name="jobCode" cssClass="text"/>
              </p>
		      <%-- unionIndex1 --%>
              <p>
		        <label><s:text name="binolmowat11_unionIndex1"/></label>
		        <s:textfield name="unionIndex1" cssClass="text"/>
		      </p>
		      <%-- unionInde3 --%>
              <p>
		        <label><s:text name="binolmowat11_unionIndex3"/></label>
		        <s:textfield name="unionIndex3" cssClass="text"/>
		      </p>
            </div>
          </div>
          <p class="clearfix">
         	<button class="right search" onclick="binolmowat11.search();return false;">
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
                <th><s:text name="binolmowat11_num"/></th>   
                <%-- JobCode --%>
                <th><s:text name="binolmowat11_JobCode" /></th>
                <%-- unionIndex --%>
                <th><s:text name="binolmowat11_unionIndex"/></th>
                <%-- unionIndex1 --%>
                <th><s:text name="binolmowat11_unionIndex1" /></th>
                <%-- unionIndex2--%>
                <th><s:text name="binolmowat11_unionIndex2" /></th>
                <%-- unionIndex3 --%>
                <th><s:text name="binolmowat11_unionIndex3" /></th>
                <%-- 错误信息 --%>
                <th><s:text name="binolmowat11_errorMsg" /></th>
                <%-- 运行信息 --%>
                <th><s:text name="binolmowat11_comments" /></th>
              </tr>
            </thead>           
          </table>
        </div>
      </div>
    </div> 
<div style="display: none;">
	<div id="showDetailTitle"><s:text name="binolmowat11_showComments" /></div>

    <div id="dialogClose"><s:text name="global.page.close" /></div>
</div>     
</s:i18n>

<%-- Job失败履历查询URL --%>
<s:url id="search_url" value="BINOLMOWAT11_search"/>
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
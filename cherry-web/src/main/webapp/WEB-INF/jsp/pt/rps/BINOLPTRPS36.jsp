<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<link rel="stylesheet" href="/${CHERRY_CONTEXT_PATH}/css/cherry/combobox.css" type="text/css">
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/pt/rps/BINOLPTRPS36.js"></script>

<s:i18n name="i18n.pt.BINOLPTRPS36">
<div class="hide">
	<s:url action="BINOLPTRPS36_search" id="search_url"></s:url>
	<a href="${search_url }" id="searchUrl"></a>
	<s:url id="export_url" action="BINOLPTRPS36_export" ></s:url>
</div>
<div class="panel-header">
  <div class="clearfix"> 
	<span class="breadcrumb left">	    
	  <jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
	</span>  
  </div>
</div>
<div id="actionResultDisplay"></div>
<div class="panel-content">
	<cherry:form id="mainForm" class="inline">
		<div class="box">	
			<div class="box-header">
           		<strong>
           			<span class="ui-icon icon-ttl-search"></span><s:text name="global.page.condition"/>
           		</strong>
            </div>
            <div class="box-content clearfix">
               	<div class="column" style="width:50%;height:auto;">
               		<p>
                	<%-- 所属品牌 --%>
                	<label><s:text name="RPS36_brand"></s:text></label>
                	<s:text name="RPS36_select" id="RPS36_select"/>
                	<s:if test="%{brandInfoList.size()> 1}">
                    	<s:select onchange="BINOLPTRPS36.search();return false;" name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" headerKey="" headerValue="%{#RPS36_select}" cssStyle="width:100px;"></s:select>
                	</s:if><s:else>
                    	<s:select name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" cssStyle="width:100px;"></s:select>      	
                	</s:else>
                	</p>
                	<p><%-- 柜台  --%>
	                  <label><s:text name="RPS36_counter"/></label>
	                  <s:textfield id="counterName" name="counterName" cssClass="text"/>
				    </p>
		        </div>    
        		<div class="column last" style="width:49%;height:auto;">
        			<p><%-- 统计月度  --%>
	                  <label><s:text name="RPS36_month"/></label>
	                  <s:textfield id="month" name="month" cssClass="date" maxlength="6" value="%{month}"/>
	                  <span class="highlight">※</span>
	                  <span class="gray"><s:text name="RPS36_example"/></span>
				    </p>
	            </div>
            </div> 
         	<p class="clearfix">
         		<button class="right search" type="button" onclick="BINOLPTRPS36.search();return false;">
         			<span class="ui-icon icon-search-big"></span>
         			<span class="button-text"><s:text name="global.page.search"/></span>
         		</button>
        	</p>
       	</div> 
    </cherry:form>  
	<%-- ====================== 结果一览开始 ====================== --%>
	<div id="section" class="section hide">
		<div class="section-header">
			<strong> 
				<span class="ui-icon icon-ttl-section-search-result"></span> 
				<s:text name="global.page.list" />
		 	</strong>
		</div>
		<div class="section-content">
			<div class="toolbar clearfix">
				<cherry:show domId="BINOLPTRPS36EXP">
					<span style="margin-right:10px;">
				     	<a id="export" href="${export_url }" class="export" onclick="BINOLPTRPS36.exportExcel(this);return false;">
				          <span class="ui-icon icon-export"></span>
				          <span class="button-text"><s:text name="global.page.export"/></span>
				        </a>
				    </span>
			    </cherry:show>
				<span class="right"> 
					<%-- 设置列 --%> 
					<a class="setting"> 
						<span class="ui-icon icon-setting"></span> 
						<span class="button-text"><s:text name="global.page.colSetting" /></span>
					</a>
				</span>
			</div>
			<div id="searchResultDiv">
				<table class="jquery_table" style="width: 100%" id="dataTable">
					<thead>
						<tr>
							<th><s:text name="No." /></th>
							<th><s:text name="RPS36_counterCode" /></th>
							<th><s:text name="RPS36_counterName" /></th>
							<th><s:text name="RPS36_monthSaleAmount" /></th>
							<th><s:text name="RPS36_employeeNum" /></th>
							<th><s:text name="RPS36_numberOfDay" /></th>
							<th><s:text name="RPS36_monthPeopleEffect" /></th>
						</tr>
					</thead>
					<tbody></tbody>
				</table>
			</div>
		</div>
	</div>
	<%-- ====================== 结果一览结束 ====================== --%>
</div>
</s:i18n>
<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/popExportDialog.jsp" flush="true" />
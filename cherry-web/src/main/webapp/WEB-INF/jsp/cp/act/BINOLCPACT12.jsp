<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<link rel="stylesheet" href="/${CHERRY_CONTEXT_PATH}/css/cherry/combobox.css" type="text/css">
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/common/departBar.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/common/popDataTable.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/cp/act/BINOLCPACT12.js"></script>
<%-- 批量导入URL --%>
<s:url id="importInit_url" action="BINOLCPACT13_init"/>
<s:i18n name="i18n.cp.BINOLCPACT12">
<div class="panel-header">
	<div class="clearfix">
		<span class="breadcrumb left"> 
			<span class="ui-icon icon-breadcrumb"></span>
			<s:text name="ACT12_titleFirst"></s:text>&nbsp;&gt;&nbsp;<s:text
				name="ACT12_titleSecond"></s:text>
		</span>
		<span class="right"> 
	   	  <cherry:show domId="BINOLCPACT12IMP">
		    <a onclick="javascript:openWin(this);return false;" class="add"  href="${importInit_url}">
			 		<span class="button-text"><s:text name="ACT12_import"></s:text></span>
			 		<span class="ui-icon icon-add"></span>
		    </a>
		   </cherry:show>
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
               		<s:if test="%{brandInfoList!=null && brandInfoList.size()> 0}">
	               		<p>
	                		<%-- 所属品牌 --%>
	                    	<label style="width:100px;"><s:text name="ACT12_brand"></s:text></label>
	                    	<s:text name="ACT12_select" id="ACT12_select"/>
	                    	<s:select name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" headerKey="" headerValue="%{#ACT12_select}" cssStyle="width:100px;"></s:select>
	                	</p>
                	</s:if>
               		<p>
               			<!-- 主题活动代码 -->
	                	<label style="width:100px;"><s:text name="ACT12_mainCampaign" /></label>
	                    <span><input id="campaignCode"  class="text" name="campaignCode"></span>
	                </p>
	                <p>
	                	<%--活动代码--%>
		            	<label style="width:100px;"><s:text name="ACT12_subcampCode"/></label>
		           		<span><input id="subCampCode"  class="text" name="subCampCode"></span>
	                </p>
		        </div>    
        		<div class="column last" style="width:49%;height:auto;">
	                <p>
	                	<!-- 产品名称 -->
	                	<label style="width:100px;"><s:text name="ACT12_productName" /></label>
	                    <s:textfield name="nameTotal" cssClass="text"/>
	                  	<input type="hidden" id="prtVendorId" name="prtVendorId" value=""/>
	                </p>
	            </div>
	            <%-- ======================= 组织联动共通导入开始  ============================= --%>
				<s:action name="BINOLCM13_query" namespace="/common" executeResult="true">
					<s:param name="businessType">A</s:param>
					<s:param name="operationType">1</s:param>
					<s:param name="mode">dpat,area,chan</s:param>
				</s:action>
			<%-- ======================= 组织联动共通导入结束  ============================= --%>
            </div> 
         	<p class="clearfix">
         		<button class="right search" type="button" onclick="BINOLCPACT12.search();return false;">
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
				<span class="right"> 
					<%-- 设置列 --%>
					<a class="setting"> 
						<span class="ui-icon icon-setting"></span> 
						<span class="button-text"><s:text name="global.page.colSetting" /></span>
					</a>
				</span>
			</div>
			<div id="campaignStockDiv">
				<table class="jquery_table" style="width: 100%" id="dataTable">
					<thead>
						<tr>
							<th><s:text name="No." /></th>
							<th><s:text name="ACT12_mainCampaign" /></th>
							<th><s:text name="ACT12_subcamp" /></th>
							<th><s:text name="ACT12_counter" /></th>
							<th><s:text name="ACT12_totalQuantity" /></th>
							<th><s:text name="ACT12_currentQuantity" /></th>
							<th><s:text name="ACT12_safeQuantity" /></th>
							<th><s:text name="ACT12_operation" /></th>
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
<div style="display: none;">
<s:url action="BINOLCPACT12_search" id="search_url"></s:url>
<a id="searchUrl" href="${search_url}"></a>
</div>

<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>

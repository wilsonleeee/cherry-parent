<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<link rel="stylesheet" href="/Cherry/css/cherry/combobox.css" type="text/css">
<script type="text/javascript" src="/Cherry/js/common/departBar.js"></script>
<script type="text/javascript" src="/Cherry/js/pt/rps/BINOLPTRPS28.js"></script>

<s:i18n name="i18n.pt.BINOLPTRPS28">
<div class="panel-header">
  <div class="clearfix"> 
	<span class="breadcrumb left">	    
	  <jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
	</span>  
  </div>
</div>

<div class="panel-content">
	<div class="box">
		<cherry:form id="mainForm" class="inline" onsubmit="binolptrps28.searchSaleTargetRpt();return false;">
			<div class="box-header">
           		<strong>
           			<span class="ui-icon icon-ttl-search"></span><s:text name="global.page.condition"/>
           		</strong>
            </div>
            <div class="box-content clearfix">
               	<div class="column" style="width:50%;height:auto;">
	                <p>
	                  	<label><s:text name="binolptrps28_fiscalYearMonth"/></label>
	                  	<s:select list="yearList" name="fiscalYear" cssStyle="width:auto;"></s:select>
	                  	<s:select list="monthList" name="fiscalMonth" cssStyle="width:auto;"></s:select>
	                </p>
		        </div>    
        		<div class="column last" style="width:49%;height:auto;">
	               	<p>
	                  	<label><s:text name="binolptrps28_countModel"/></label>
	                  	<input type="radio" name="countModel" value="0" checked="checked"/><s:text name="binolptrps28_countModel0"/>
	                  	<input type="radio" name="countModel" value="1"/><s:text name="binolptrps28_countModel1"/>
	                </p>	         
	            </div>
	            <%-- ======================= 组织联动共通导入开始  ============================= --%>
           	  	<s:action name="BINOLCM13_query" namespace="/common" executeResult="true">
	           	  	<s:param name="showType">0</s:param>
	           	  	<s:param name="businessType">3</s:param>
	        		<s:param name="operationType">1</s:param>
	        		<s:param name="mode">dpat,area,chan</s:param>
           	  	</s:action>
           	  	<%-- ======================= 组织联动共通导入结束  ============================= --%>
            </div> 
         	<p class="clearfix">
         		<button class="right search" type="button" onclick="binolptrps28.searchSaleTargetRpt();return false;">
         			<span class="ui-icon icon-search-big"></span>
         			<span class="button-text"><s:text name="global.page.search"/></span>
         		</button>
        	</p> 
    	</cherry:form>  
	</div>
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
				<span style="margin-right:10px;">
			      <cherry:show domId="BINOLPTRPS28_01">
			     	<a id="export" class="export" onclick="binolptrps28.exportExcel();return false;">
			          <span class="ui-icon icon-export"></span>
			          <span class="button-text"><s:text name="global.page.export"/></span>
			        </a>
				  </cherry:show> 	
			    </span>
			</div>
			<div id="saleTargetRptDiv"></div>
		</div>
	</div>
	<%-- ====================== 结果一览结束 ====================== --%>
	<div class="hide">
		<div id="binolptrps28_departCode"><s:text name="binolptrps28_departCode" /></div>
		<div id="binolptrps28_departName"><s:text name="binolptrps28_departName" /></div>
		<div id="binolptrps28_busniessPrincipal"><s:text name="binolptrps28_busniessPrincipal" /></div>
		<div id="binolptrps28_provinceName"><s:text name="binolptrps28_provinceName" /></div>
		<div id="binolptrps28_cityName"><s:text name="binolptrps28_cityName" /></div>
		<div id="binolptrps28_channelName"><s:text name="binolptrps28_channelName" /></div>
		<div id="binolptrps28_regionName"><s:text name="binolptrps28_regionName" /></div>
		<div id="binolptrps28_amount"><s:text name="binolptrps28_amount" /></div>
		<div id="binolptrps28_targetMoney"><s:text name="binolptrps28_targetMoney" /></div>
		<div id="binolptrps28_percent"><s:text name="binolptrps28_percent" /></div>
		<div id="binolptrps28_datePercent"><s:text name="binolptrps28_datePercent" /></div>
		<div id="binolptrps28_lastMoney"><s:text name="binolptrps28_lastMoney" /></div>
		<div id="binolptrps28_predict"><s:text name="binolptrps28_predict" /></div>
	</div>
</div>
</s:i18n>
<div class="hide">
<s:url action="BINOLPTRPS28_search" id="saleTargetRptUrl"></s:url>
<a href="${saleTargetRptUrl }" id="saleTargetRptUrl"></a>
<s:url id="exportUrl" action="BINOLPTRPS28_export" ></s:url>
<a id="exportUrl" href="${exportUrl}"></a>
</div>
<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/cp/act/BINOLCPACT10.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript">
	//节日
	var holidays = '${holidays }';
	$('#startDate').cherryDate({
		holidayObj: holidays,
		beforeShow: function(input){
			var value = $('#endDate').val();
			return [value,'maxDate'];
		}
	});
	$('#endDate').cherryDate({
		holidayObj: holidays,
		beforeShow: function(input){
			var value = $('#startDate').val();
			return [value,'minDate'];
		}
	});
</script>
<%--礼品领用报表查询URL --%>
<s:url id="search_url" value="/cp/BINOLCPACT10_search"/>
<s:url id="Excel_url" value="BINOLCPACT10_export"/>
<s:text id="selectAll" name="global.page.all"/>
<s:i18n name="i18n.cp.BINOLCPACT10">
<div id="div_main">
	<div class="crm_content_header">
		<span class="icon_crmnav"></span> <span><s:text name="ACT10_campDetail" /> &gt; <s:text name="ACT10_exChangeDetail" /></span>
	</div>
	<div class="panel-content">
	<div class="section">
		<form id="mainForm"  method="post" class="inline" >
	    	<div class="box">
				<div class="box-header">
					<strong>
						<span class="ui-icon icon-ttl-search"></span>
						<s:text name="global.page.condition"/>
					</strong>
				</div>
	        <div class="box-content clearfix">
	        <div class="column" style="width:50%;height:auto;">
				<p class="clearfix">
		            <%--单据号--%>
		            <label><s:text name="ACT10_billCode"/></label>
		            <s:textfield id="billCode" name="billCode" cssClass="text"/>
	            </p>
	            <p class="clearfix">
		            <%--会员卡号--%>
		            <label><s:text name="ACT10_memberCode"/></label>
		            <s:textfield id="memberCode" name="memberCode" cssClass="text"/>
	            </p>
	            <p class="clearfix">
	            	<%--会员类型--%>
	            	<label><s:text name="ACT10_testType"/></label>
	      		 	<s:select name="testType" list='#application.CodeTable.getCodes("1256")' listKey="CodeKey" listValue="Value"
	               	headerKey="" headerValue="%{selectAll}" value="'1'"  cssStyle="width:185px;"/>
	            </p>
			</div>
			<div class="column last" style="width:49%;height:auto;">
				<p  class="date">
                	<%-- 日期范围 --%>
                  	<label><s:text name="ACT10_getDate"/></label>
                  	<span><s:textfield id="startDate" name="startDate" cssClass="date"/></span>
                  	- 
                  	<span><s:textfield id="endDate" name="endDate" cssClass="date"/></span>
                </p>
	            <p class="clearfix">
		            <%--兑换柜台--%>
		            <label><s:text name="ACT10_counter"/></label>
		            <s:textfield id="departCode" name="departCode" cssClass="text"/>
	            </p>
	             <p class="clearfix">
		            <%--兑换柜台--%>
		            <label><s:text name="ACT10_memberPhone"/></label>
		            <s:textfield id="mobilePhone" name="mobilePhone" cssClass="text"/>
	            </p>
	         	<span class="hide">
		            <s:textfield id="campaignId" name="campaignId" cssClass="text"/>
	            </span>
	 		 </div>
	         </div>
				<p class="clearfix">
					<%--查询--%>
				  	<button onclick="BINOLCPACT10.search('${search_url}');return false;" class="right search">
			  			<span class="ui-icon icon-search-big"></span>
			  			<span class="button-text"><s:text name="ACT10_search"/></span>
				  	</button>
				</p>
	        </div>
		</form>
	</div>
	<div id="section" class="section hide" >
		<div class="section-header" id="section-header">
			<%--查询结果一览 --%>
			<strong>
				<span class="ui-icon icon-ttl-section-search-result"></span>
				<s:text name="ACT10_resultList"/>
			</strong>
		</div>
		<div class="ui-tabs-panel">
			<div class="toolbar clearfix">
				<%-- 设置列 --%>
				<span class="right">
					<a class="setting"> 
						<span class="ui-icon icon-setting"></span> 
						<span class="button-text"><s:text name="global.page.colSetting" /></span> 
					</a>
				</span>
				<cherry:show domId="BINOLCPACT10EXP">
					<a id="exportPointInfo" class="export left" onclick="BINOLCPACT10.exportExcel('${Excel_url}');return false;">
				     <span class="ui-icon icon-export"></span>
				     <span class="button-text"><s:text name="ACT10_excelExport"/></span>
			 		</a>
			 	</cherry:show>
			</div>
		<div class="section" id="result_list">
		<table id="dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
			<thead>
				<tr>
					<th>NO.</th>
					<!--兑换单据 -->
					<th><s:text name="ACT10_billCode"/><span class="ui-icon ui-icon-document"></span></th>
					<%--兑换会员--%>
					<th><s:text name="ACT10_member"/></th>
					<%--会员手机--%>
					<th><s:text name="ACT10_memberPhone"/></th>
					<%--会员类型--%>
					<th><s:text name="ACT10_testType"/></th>
					<%--兑换柜台--%>
					<th><s:text name="ACT10_counter"/></th>
					<%--兑换数量--%>
					<th><s:text name="ACT10_sumQuantity"/></th>
					<!--兑换金额 -->
					<th><s:text name="ACT10_sumAmount"/></th>
					<%--兑换时间--%>
					<th><s:text name="ACT10_getTime"/></th>
				</tr>
			</thead>
		</table>
		</div>
		</div>
	</div>
</div>
<div id="popPrtTable" ></div>
<div class="hide">
    <div id="dialogClose"><s:text name="global.page.close" /></div>
	<div id="dialogTitle"><s:text name="ACT10_resultsDetail" /></div>
	<div id="dialogInitMessage">
	<img id="loading" src="/Cherry/css/cherry/img/loading.gif" height="15px">
	<s:text name="ACT10_messageInit" />
	</div>
</div>
</div>
</s:i18n>
<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>
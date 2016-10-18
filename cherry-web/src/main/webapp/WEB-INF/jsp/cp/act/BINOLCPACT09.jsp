<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/cp/act/BINOLCPACT07.js"></script>
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
<s:url id="search_url" value="/cp/BINOLCPACT09_search"/>
<s:url id="Excel_url" value="BINOLCPACT07_export"/>
<s:text id="selectAll" name="global.page.all"/>
<s:i18n name="i18n.cp.BINOLCPACT07">
<div id="div_main">
	<div class="crm_content_header">
		<span class="icon_crmnav"></span> <span><s:text
				name="ACT07_campaignDetail" /> &gt; <s:text
				name="ACT07_getResult" /></span>
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
		            <label><s:text name="ACT07_billNoIF"/></label>
		            <s:textfield id="billNoIF" name="billNoIF" cssClass="text"/>
	            </p>
	            <p class="clearfix">
		            <%--会员卡号--%>
		            <label><s:text name="ACT07_memberCode"/></label>
		            <s:textfield id="memberCode" name="memberCode" cssClass="text"/>
	            </p>
	            <p class="clearfix">
		            <%--会员手机号--%>
		            <label><s:text name="ACT07_memberPhone"/></label>
		            <s:textfield id="mobilePhone" name="mobilePhone" cssClass="text"/>
	            </p>
	            <p class="clearfix">
	            	<%--会员类型--%>
	            	<label><s:text name="ACT07_testType"/></label>
	      		 	<s:select name="testType" list='#application.CodeTable.getCodes("1256")' listKey="CodeKey" listValue="Value"
	               	headerKey="" headerValue="%{selectAll}" value="'1'"  cssStyle="width:185px;"/>
	            </p>
			</div>
			<div class="column last" style="width:49%;height:auto;">
				<p id="getDateCover" class="date">
                	<%-- 日期范围 --%>
                  	<label><s:text name="ACT07_getDate"/></label>
                  	<span><s:textfield id="startDate" name="startDate" cssClass="date"/></span>
                  	- 
                  	<span><s:textfield id="endDate" name="endDate" cssClass="date"/></span>
                </p>
	            <p class="clearfix">
		            <%--领用柜台--%>
		            <label><s:text name="ACT07_counter"/></label>
		            <s:textfield id="counterCode" name="counterCode" cssClass="text"/>
	            </p>
	            <p class="clearfix">
		            <%--coupon码--%>
		            <label><s:text name="ACT07_couponCode"/></label>
		            <s:textfield id="couponCode" name="couponCode" cssClass="text"/>
	            </p>
	         	<span class="hide">
		            <s:textfield id="activityCode" name="activityCode" cssClass="text"/>
	            </span>
	 		 </div>
	         </div>
				<p class="clearfix">
					<%--查询--%>
				  	<button class="right search" type="submit" onclick="BINOLCPACT07.search();return false;">
			  			<span class="ui-icon icon-search-big"></span>
			  			<span class="button-text"><s:text name="ACT07_search"/></span>
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
				<s:text name="ACT07_searchResult"/>
			</strong>
		</div>
		<div class="ui-tabs-panel">
			<div class="toolbar clearfix">
			<cherry:show domId="BINOLCPACT09EXP">
           		<span style="margin-right:10px;">
                   <a id="export" class="export" onclick="BINOLCPACT07.giftDrawExport('${Excel_url}');return false;">
                      <span class="ui-icon icon-export"></span>
                      <span class="button-text"><s:text name="global.page.exportExcel"/></span>
                   </a>
                </span>
            </cherry:show>
				<%-- 设置列 --%>
				<span class="right">
					<a class="setting"> 
						<span class="ui-icon icon-setting"></span> 
						<span class="button-text"><s:text name="global.page.colSetting" /></span> 
					</a>
				</span>
			</div>
		<div class="section" id="result_list">
		<table id="dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
			<thead>
				<tr>
					<th>NO.</th>
					<!-- 单据号 -->
					<th><s:text name="ACT07_billNoIFs"/><span class="ui-icon ui-icon-document"></span></th>
					<%--会员--%>
					<th><s:text name="ACT07_memberName"/></th>
					<%--会员手机--%>
					<th><s:text name="ACT07_memberPhone"/></th>
					<%--会员类型--%>
					<th><s:text name="ACT07_testType"/></th>
					<%--领用柜台--%>
					<th><s:text name="ACT07_counter"/></th>
					<!-- 赠券条码 -->
					<th><s:text name="ACT07_couponCode"/></th>
					<%--领用时间--%>
					<th><s:text name="ACT07_getTime"/></th>
					<%--领用数量--%>
					<th><s:text name="ACT07_getQuantity"/></th>
					<!-- 领用金额 -->
					<th><s:text name="ACT07_getAmount"/></th>
					<!-- 领用类型 -->
					<th><s:text name="ACT07_giftDrawType"/></th>
					<%--活动--%>
					<th><s:text name="ACT07_activity"/></th>
					<%--操作人员--%>
					<th><s:text name="ACT07_employee"/></th>
					<!-- 备注  -->
					<th><s:text name="ACT07_comment"/></th>
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
	<div id="dialogTitle"><s:text name="ACT07_getPrtDetail" /></div>
	<div id="dialogInitMessage">
	<img id="loading" src="/Cherry/css/cherry/img/loading.gif" height="15px">
	<s:text name="ACT07_messageInit" />
	</div>
</div>
</div>
</s:i18n>
<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>
<span id="searchUrl" style="display:none">${search_url}</span>
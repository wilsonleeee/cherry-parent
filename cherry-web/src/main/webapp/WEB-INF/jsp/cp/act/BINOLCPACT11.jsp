<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/cp/act/BINOLCPACT11.js"></script>
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
<%--履历查询URL --%>
<s:url id="search_url" value="/cp/BINOLCPACT11_search"/>
<s:text id="selectAll" name="global.page.all"/>
<s:i18n name="i18n.cp.BINOLCPACT11">
<div id="div_main">
	<div class="crm_content_header">
		<span class="icon_crmnav"></span> <span><s:text
				name="ACT11_activityDetail" /> &gt; <s:text
				name="ACT11_activityCode" /></span>
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
		            <label><s:text name="ACT11_billNo"/></label>
		            <s:textfield id="tradeNoIF" name="tradeNoIF" cssClass="text"/>
	            </p>
	            <p class="clearfix">
		            <%--批次号--%>
		            <label><s:text name="ACT11_batchNo"/></label>
		            <s:textfield id="batchNo" name="batchNo" cssClass="text"/>
	            </p>	            
<!-- 	            <p class="clearfix"> -->
<%-- 	             	活动柜台 --%>
<%-- 	                <label ><s:text name="ACT11_departName"/></label> --%>
<!-- 					<input id="departName"  class="text" name="departName"> -->
<!--             	</p> -->
	             <p class="clearfix">
	               <%--单据状态--%>
	               	<label ><s:text name="ACT11_state"/></label>
	               	<s:select name="state" list='#application.CodeTable.getCodes("1116")' listKey="CodeKey" listValue="Value"
	               	headerKey="" headerValue="%{selectAll}" value="''"  cssStyle="width:185px;"/>
            	</p>
			</div>
			<div class="column last" style="width:49%;height:auto;">
			   <p  class="date">
                	<%-- 日期范围 --%>
                  	<label><s:text name="ACT11_optTime"/></label>
                  	<span><s:textfield id="startDate" name="startDate" cssClass="date"/></span>
                  	- 
                  	<span><s:textfield id="endDate" name="endDate" cssClass="date"/></span>
                </p>
	            <p class="clearfix">
		            <%--会员卡号--%>
		            <label><s:text name="ACT11_memberCode"/></label>
		            <s:textfield id="memCode" name="memCode" cssClass="text"/>
	            </p>
	            <p class="clearfix">
		            <%--会员手机号--%>
		            <label><s:text name="ACT11_memberPhone"/></label>
		            <s:textfield id="mobilePhone" name="mobilePhone" cssClass="text"/>
	            </p>
	         	<span class="hide">
		            <s:textfield id="campCode" name="campCode" cssClass="text"/>
	            </span>
	 		 </div>
	         </div>
				<p class="clearfix">
					<%--查询--%>
				  	<button class="right search" type="submit" onclick="BINOLCPACT11.search('${search_url}');return false;">
			  			<span class="ui-icon icon-search-big"></span>
			  			<span class="button-text"><s:text name="ACT11_search"/></span>
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
				<s:text name="ACT11_searchResult"/>
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
			</div>
		<div class="section" id="result_list">
		<table id="dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
			<thead>
				<tr>
					<th>NO.</th>
					<!--活动单号 -->
					<th><s:text name="ACT11_billNo"/></th>
					<!--批次号码 -->
					<th><s:text name="ACT11_batchNo"/></th>
					<%--活动名称--%>
					<th><s:text name="ACT11_subActivityName"/></th>
					<%--会员名称--%>
					<th><s:text name="ACT11_memberName"/></th>
					<%--会员手机--%>
					<th><s:text name="ACT11_memberPhone"/></th>
					<%--活动柜台--%>
					<%--<th><s:text name="ACT11_departName"/></th>--%>
					<!--活动时间 -->
					<th><s:text name="ACT11_optTime"/></th>
					<%--单据状态--%>
					<th><s:text name="ACT11_state"/></th>
				</tr>
			</thead>
		</table>
		</div>
		</div>
	</div>
</div>
</div>
</s:i18n>
<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>
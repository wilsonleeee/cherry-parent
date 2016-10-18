<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<link rel="stylesheet" href="/Cherry/css/cherry/combobox.css" type="text/css">
<script type="text/javascript" src="/Cherry/js/common/departBar.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/common/popDataTable.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/bs/wem/BINOLBSWEM05.js"></script>
<s:i18n name="i18n.bs.BINOLBSWEM05">
	<s:text name="global.page.all" id="bswem05_select" />
	<s:url id="export_url" action="/BINOLBSWEM05_export"/>
	<div class="hide">
		<s:url id="search_url" value="/basis/BINOLBSWEM05_search"/>
		<a id="searchUrl" href="${search_url}"></a>
	</div>
	<div class="panel-header">
		<div class="clearfix"> 
			<span class="breadcrumb left">      
				<jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
			</span>
		</div>
	</div>
	<div id="actionResultDisplay"></div>
	<%-- ================== 错误信息提示 START ======================= --%>
	<div id="errorMessage"></div>
	<div style="display: none" id="errorMessageTemp">
		<div class="actionError">
		</div>
	</div>
	<%-- ================== 错误信息提示   END  ======================= --%>
	<div class="panel-content">
		<div class="box">
			<cherry:form id="mainForm" class="inline">
				<input type="hidden" id="currentUnitID" name="currentUnitID" value="BINOLBSWEM05"/>
				<div class="box-header">
				<strong>
	            	<span class="ui-icon icon-ttl-search"></span>
	            	<s:text name="bswem05.condition"/>
	            </strong>
				</div>
				            <div class="box-content clearfix">
				<div style="padding: 15px 0px 5px;">
		      	<table class="detail" cellpadding="0" cellspacing="0">
				  <tbody>
		            <tr>
		              <%--销售单据 --%>
		              <th><s:text name="bswem05.billCode" /></th>
		              <td>
		                <span><s:textfield name="billCode" cssClass="text" maxlength="35" onblur="ignoreCondition(this);return false;"/></span> 
		              </td>
		              <%--销售日期 --%>
		              <th><s:text name="bswem05.date" /></th>
		              <td id="dateCover">
		                <span><s:textfield id="startDate" name="startDate" cssClass="date"/>-<s:textfield id="endDate" name="endDate" cssClass="date"/></span>
		              </td>
		            </tr>
		            <tr>
		              <%-- 销售人员 --%>
		              <th><s:text name="bswem05.saleEmployeeCondition" /></th>
		              <td>
		                <span><input type="text" class="text" name="employeeCode" id="employeeCode"></input></span>
		              </td>
		              <%-- 会员卡号 --%>
		              <th><s:text name="bswem05.memCode" /></th>
		              <td>
		                <span><s:textfield name="memCode" cssClass="text"/></span>
		              </td>
		            </tr>
		            <tr>
		              <%-- 收益人 --%>
		              <th><s:text name="bswem05.incomePerson" /></th>
		              <td>
		                <span><s:textfield name="commissionEmployeeCode" cssClass="text" maxlength="30"/> </span>
		              </td>
		              <%-- 级别  --%>
		              <th><s:text name="bswem05.departLevel" /></th>
		              <td>
		                <s:select name="commissionEmployeeLevel" list="commissionEmployeeLevelList" listKey="codeKey" listValue="value1" headerKey="" headerValue='%{#bswem05_select}'></s:select>
		              </td>
		            </tr>
		          </tbody>
		        </table>
				</div>
			</div>
				<p class="clearfix">
					<button class="right search" onclick="BINOLBSWEM05.search();return false;">
						<span class="ui-icon icon-search-big"></span>
						<span class="button-text"><s:text name="bswem05.search"/></span>
					</button>
				</p>
			</cherry:form>
		</div>
		<div id="saleOrdersSection" class="section hide">
			<div class="section-header">
				<strong>
					<span class="ui-icon icon-ttl-section-search-result"></span>
					<s:text name="bswem05.resultlist"/>
				</strong>
			</div>
			<div class="section-content" id="result_list">
				<div class="toolbar clearfix">
					<a class="export left" onclick="BINOLBSWEM05.exportExcel(this);return false;"  href="${export_url}">
						<span class="ui-icon icon-export"></span>
						<span class="button-text"><s:text name="global.page.export"></s:text></span>
					</a>
		   			<span id="headInfo"></span>
					<span class="right">
						<a class="setting">
                            <span class="ui-icon icon-setting"></span>
            	            <span class="button-text">
	                            <s:text name="bswem05.colSetting"/>
                            </span>
						</a>
					</span>
				</div>
				<table id="dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
					<thead>
						<tr>
							<th><s:text name="bswem05.employeeCode"/></th>
							<th><s:text name="bswem05.employeeName"/></th>
							<th><s:text name="bswem05.saleTime"/></th>
							<th><s:text name="bswem05.levelType"/></th>
							<th><s:text name="bswem05.billCode"/></th>
							<th><s:text name="bswem05.saleEmployeeCode"/></th>
							<th><s:text name="bswem05.saleEmployeeName"/></th>
							<th><s:text name="bswem05.saleType"/></th>
							<th><s:text name="bswem05.incomeAmount"/></th>
							<th><s:text name="bswem05.saleAmount"/></th>
							<th><s:text name="bswem05.quantity"/></th>
						</tr>
					</thead>
				</table>
			</div>
		</div>
	</div>
</s:i18n>
<!-- 缓存默认的开始结束日期 -->
<input type="hidden"  id="defStartDate" value=''/>
<input type="hidden"  id="defEndDate"	value=''/> 
<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>




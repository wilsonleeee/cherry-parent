<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<link rel="stylesheet" href="/Cherry/css/cherry/combobox.css" type="text/css">
<script type="text/javascript" src="/Cherry/js/common/departBar.js"></script>
<script type="text/javascript" src="/Cherry/js/bs/wem/BINOLBSWEM07.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/common/popDataTable.js"></script>
<script>

	$('#startDate').cherryDate({
	    beforeShow: function(input){
	        var value = $('#endDate').val();
	        return [value,'maxDate'];
	    }
	});
	$('#endDate').cherryDate({
	    beforeShow: function(input){
	        var value = $('#startDate').val();
	        return [value,'minDate'];
	    }
	});
	cherryValidate({			
		formId: "mainForm",		
		rules: {
			startDate: {dateValid:true},	// 开始日期
			endDate: {dateValid:true}	// 结束日期
		}		
	});

</script>

<s:url id="search_url" value="/basis/BINOLBSWEM07_search"/>
<%-- EXCEL导出URL --%>
<s:url id="xls_url" value="/basis/BINOLBSWEM07_export"/>
<s:i18n name="i18n.bs.BINOLBSWEM07">
	<s:text name="global.page.all" id="WEM07_select" />
	<div class="panel-header">
		<%-- ###销售记录查询 --%>
		<div class="clearfix"> 
			<span class="breadcrumb left">	    
				<jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
			</span>  
		</div>
	</div>
	<input type="hidden" id="yes" value='<s:text name="WEM07_yes"/>'/>
	<input type="hidden" id="dialogTitle" value='<s:text name="WEM07_dialogTitle"/>'/>

	<%-- ================== 错误信息提示 START ======================= --%>
	<div id="errorMessage"></div>
	<%-- ================== 错误信息提示   END  ======================= --%>
	
<div class="panel-content">
	<div class="box">
		<cherry:form id="mainForm" class="inline">
			<div class="box-header">
           		<strong>
           			<span class="ui-icon icon-ttl-search"></span><s:text name="global.page.condition"/>
           		</strong>
            </div>
            <div class="box-content clearfix">
				<div style="padding: 15px 0px 5px;">
		      	<table class="detail" cellpadding="0" cellspacing="0" id="wem07SearchId">
				  <tbody>
		            <tr>
		              <%--销售单据 --%>
		              <th><s:text name="WEM07_billCode" /></th>
		              <td>
		                <span><s:textfield name="billCode" cssClass="text" maxlength="35" onblur="ignoreCondition(this);return false;"/></span> 
		              </td>
		              <%--销售日期 --%>
		              <th><s:text name="WEM07_date" /></th>
		              <td id="dateCover">
		                <span><s:textfield id="startDate" name="startDate" cssClass="date"/>-<s:textfield id="endDate" name="endDate" cssClass="date"/></span>
		              </td>
		            </tr>
		            <tr>
		              <%-- 销售人员 --%>
		              <th><s:text name="WEM07_saleEmployeeName" /></th>
		              <td>
		                <span><input type="text" class="text" name="employeeCode" id="employeeCode"></input></span>
		              </td>
		              <%-- 会员卡号 --%>
		              <th><s:text name="WEM07_memCode" /></th>
		              <td>
		                <span><s:textfield name="memCode" cssClass="text"/></span>
		              </td>
		            </tr>
		            <tr>
		              <%-- 收益人 --%>
		              <th><s:text name="WEM07_incomePerson" /></th>
		              <td>
		                <span><s:textfield name="commissionEmployeeCode" cssClass="text" maxlength="30"/> </span>
		              </td>
		              <%-- 级别  --%>
		              <th><s:text name="WEM07_departLevel" /></th>
		              <td>
		                <s:select name="commissionEmployeeLevel" list="commissionEmployeeLevelList" listKey="codeKey" listValue="value1" headerKey="" headerValue='%{#WEM07_select}'></s:select>
		              </td>
		            </tr>
		          </tbody>
		        </table>
				</div>
			</div> 
       	  	<p class="clearfix">
       			<%-- 查询 --%>
       			<button class="right search" type="button" onclick="BINOLBSWEM07.search('<s:property value="#search_url"/>')">
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
					<cherry:show domId="BINOLPTWEM07EXP">
						<a id="export" class="export" onclick="BINOLBSWEM07.exportExcel('${xls_url}');return false;">
							<span class="ui-icon icon-export"></span>
							<span class="button-text"><s:text name="global.page.exportExcel"/></span>
						</a>
						<a id="export" class="export" onclick="BINOLBSWEM07.exportCsv();return false;">
							<span class="ui-icon icon-export"></span>
							<span class="button-text"><s:text name="global.page.exportCsv"/></span>
						</a>
					</cherry:show>
				</span>
				<span id="headInfo" ></span>
				<%-- 设置列 --%>
				<span class="right"> 
					<a class="setting"> 
						<span class="ui-icon icon-setting"></span> 
						<span class="button-text"><s:text name="global.page.colSetting" /></span> 
					</a>
				</span>
			</div>
			<table id="dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
		  		<thead>
					<tr>
						<%-- No. --%>
						<th><s:text name="No." /></th>
						<%-- 收款帐户 --%>
						<th><s:text name="WEM07_collectionAccount" /><span class="ui-icon ui-icon-document"></span></th>
						<%-- 收款户名 --%>
						<th><s:text name="WEM07_accountName" /></th>
						<%-- 转账金额--%>
				        <th><s:text name="WEM07_amount" /></th>
						<%-- 备注   --%>
						<th><s:text name="WEM07_comments" /></th>
						<%-- 收款银行   --%>
						<th><s:text name="WEM07_mainBank" /></th>
						<%-- 开户行   --%>
						<th><s:text name="WEM07_subBank" /></th>
						<%-- 收款省份   --%>
						<th><s:text name="WEM07_province" /></th>
						<%-- 收款市县 --%>
						<th><s:text name="WEM07_cityCounty" /></th>
						<%-- 收益人手机 --%>
						<th><s:text name="WEM07_commissionMobile" /></th>
						<%-- 收益人姓名 --%>
						<th><s:text name="WEM07_commissionName" /></th>
						<%-- 收益人店铺--%>
						<th><s:text name="WEM07_commissionCounter" /></th>
					</tr>
	      		</thead>
			<tbody id="databody"></tbody>
		</table>
	</div>
	</div> 
	<div class="hide">
		<div id="deleteButton"><s:text name="global.page.delete" /></div><%-- 商品列表删除按钮 --%>
	</div>
	<%-- ====================== 结果一览结束 ====================== --%>
</div>
</s:i18n>
<!-- 缓存默认的开始结束日期 -->
<input type="hidden"  id="defStartDate" value=''/>
<input type="hidden"  id="defEndDate"	value=''/> 
<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>
<span id="search" style="display:none">${search_url}</span>
<jsp:include page="/WEB-INF/jsp/common/popExportDialog.jsp" flush="true" />
<div class="hide">
	<s:url id="exportCsvUrl" action="BINOLBSWEM07_exportCsv" ></s:url>
	<a id="exportCsvUrl" href="${exportCsvUrl}"></a>
	<s:url id="exporChecktUrl" action="BINOLBSWEM07_exportCheck" ></s:url>
	<a id="exporChecktUrl" href="${exporChecktUrl}"></a>
</div>
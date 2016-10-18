<%--柜台消息(excel导入)明细 --%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<style>
.box-header {
	padding: 0.2em 0;
}

table.detail {
	margin-bottom: 3px;
}
</style>
<s:i18n name="i18n.mo.BINOLMOCIO17">
	<div class="section">
		<div class="section-header">
			<strong><span class="ui-icon icon-ttl-section-info"></span>
				<%-- 基本信息 --%> 
				<s:text name="global.page.title" /> </strong>
		</div>
		<div class="section-content">
			<div>
				<div class="box-header"></div>
				<table class="detail">
					<tbody>
						<tr>
							<%-- 消息标题 --%>
							<th><s:text name="CIO17_messageTitle" /></th>
							<td><s:property value="counterMessageImportInfo.messageTitle" /></td>
							<%-- 生效日期 --%>
							<th><s:text name="CIO17_validDate" /></th>
							<td><s:property value="counterMessageImportInfo.startValidDate" /> ~ 
								<s:property value="counterMessageImportInfo.endValidDate" />
							</td>
						</tr>
						<tr>
							<%-- 消息内容 --%>
							<th><s:text name="CIO17_messageBody" /></th>
							<td colspan="3">
								<s:property value="counterMessageImportInfo.messageBody" />
							</td>
						</tr>
						<tr>
							<%-- 导入日期--%>
							<th><s:text name="CIO17_importDate" /></th>
							<td><s:property value="counterMessageImportInfo.importDate" /></td>
							<%-- 制单员工 --%>
							<th><s:text name="CIO17_employee" /></th>
							<td>
								<s:if test='counterMessageImportInfo.employeeCode != null && !"".equals(counterMessageImportInfo.employeeCode)'>
									（<s:property value="counterMessageImportInfo.employeeCode" />）
								</s:if>
								<s:property value="counterMessageImportInfo.employeeName" />
							</td>
						</tr>
						<tr>
							<%-- 导入结果 --%>
							<th><s:text name="CIO17_importResult" /></th>
							<td>
								<s:property value="#application.CodeTable.getVal('1250',counterMessageImportInfo.importResult)"/>
							</td>
							<%-- 导入理由 --%>
							<th><s:text name="CIO17_importReson" /></th>
							<td><s:property value='counterMessageImportInfo.comments' /></td>
						</tr>
					</tbody>
				</table>
				<div class="clearfix"></div>
			</div>
		</div>
	</div>
	<s:if test='null != counterMessageImportDetailList && counterMessageImportDetailList.size() > 0'>
	<div class="section">
		<div class="section-header">
			<strong><span
				class="ui-icon icon-ttl-section-list"></span> 
				<%-- 柜台消息（Excel导入）明细--%>
				<s:text name="CIO17_results_list" /> </strong>
		</div>
		<div class="section-content">
				<div id="dataTable" style="overflow-x: auto; overflow-y: hidden">
					<table id="detailDataTable1" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
						<thead>
							<tr>
								<%-- 编号 --%>
								<th class="center"><s:text name="CIO17_No" /></th>
								<%-- 柜台号 --%>
								<th class="center"><s:text name="CIO17_counterCode" /></th>
								<%-- 柜台名称 --%>
								<th class="center"><s:text name="CIO17_counterName" /></th>
								<%-- 备注--%>
								<th class="center"><s:text name="CIO17_errorMsg" /></th>
							</tr>
						</thead>
						<tbody id="databody">
							<s:iterator value="counterMessageImportDetailList" status="status">
								<tr id="dataRow<s:property value='#status.index+1'/>" class="<s:if test='#status.odd'>odd</s:if><s:else>even</s:else>">
									<td id="dataTd1"><s:property value="#status.index+1" /></td>
									<td id="dataTd2"><span><s:property value="counterCode" /></span></td>
									<td id="dataTd3"><span><s:property value="counterName" /></span></td>
									<td id="dataTd4">
										<span>
											<a class="description" style="cursor: pointer;"  title="<s:text name="CIO17_errorMsg" /> | <s:property value="errorMsg" />">
										        <s:if test="%{null != errorMsg && errorMsg.length() > 16}">
										          <s:property value="%{errorMsg.substring(0, 12)}" />...
										 		</s:if>
										 		<s:else>
										          <s:property value="errorMsg" />
										   		</s:else>
									        </a>
								        </span>
									</td>
								</tr>
							</s:iterator>
						</tbody>	
					</table>
				</div>
				<hr class = "space" />
		</div>
	</div>
	</s:if>
</s:i18n>
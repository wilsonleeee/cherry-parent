<%-- 产品关联维护 --%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/pt/jcs/BINOLPTJCS43.js"></script>
<s:i18n name="i18n.pt.BINOLPTJCS43">
	<div class="hide">
		<s:url id="search_url" action="BINOLPTJCS43_search" />
		<s:hidden name="search_url" value="%{search_url}" />
		<s:text id="selectAll" name="global.page.all" />
		<div id="confirmDialogText">
			<p class="message"><s:text name="JCS43_confirmDialogText"/></p>
		</div>
		<span id="dialogConfirm"><s:text name="global.page.ok" /></span>
		<span id="dialogCancel"><s:text name="global.page.cancle" /></span>
	</div>
	<div class="panel-header">
		<div class="clearfix">
			<span class="breadcrumb left">
				<jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
			</span>
			<span class="right">
				<cherry:show domId="BINOLPTJCS43CON">
					<s:url id="conjunction_url" action="BINOLPTJCS43_conjunction"/>
					<a href="${conjunction_url}" class="add" onclick="BINOLPTJCS43.associateInit(this);return false;">
	       		 		<span class="button-text"><s:text name="JCS43_conjunction"/></span>
	       		 		<span class="ui-icon icon-add"></span>
	       		 	</a>
       		 	</cherry:show>
			</span>
		</div>
	</div>
	<div id="dialogInit"></div>
	<div id="errorMessage"></div>
	<div id="actionResult"></div>
	<div id="actionResultDisplay"></div>
	<div class="panel-content">
		<div class="box">
			<cherry:form id="mainForm" class="inline" onsubmit="BINOLPTJCS43.search(); return false;">
				<input name="brandInfoId" id="brandInfoId" type="hidden" value='<s:property value="#session.userinfo.BIN_BrandInfoID"/>'></input>
				<div class="box-header">
					<strong> <span class="ui-icon icon-ttl-search"></span> <%-- 查询条件 --%>
						<s:text name="JCS43_condition" />
					</strong>
				</div>
				<div class="box-content clearfix">
					<div class="column" style="width: 50%;">
						<%-- 产品类型--%> 
						<p>
							<label style="width: 65px;"><s:text name="JCS43_mode" /></label>
							<s:select name="mode" list='#application.CodeTable.getCodes("1136")' listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{selectAll}" />
						</p>
						<%-- 产品全名--%>
						<p>
							<label style="width: 65px;"><s:text name="JCS43_nameTotal" /></label> 
							<s:textfield name="nameTotal" cssClass="text" maxlength="50" />
						</p>
					</div>
					<div class="column last" style="width: 49%;">
						<%-- 产品厂商编码 --%>
						<p>
							<label style="width: 65px;"><s:text name="JCS43_unitCode" /></label> 
							<s:textfield name="unitCode" cssClass="text" />
						</p>
						<%-- 产品条码--%>
						<p>
							<label style="width: 65px;"><s:text name="JCS43_barCode" /></label> 
							<s:textfield name="barCode" cssClass="text" />
						</p>
					</div>
				</div>
				<p class="clearfix">
					<button class="right search" type="submit" onclick="BINOLPTJCS43.search();return false;">
						<span class="ui-icon icon-search-big"></span>
						<span class="button-text"><s:text name="JCS43_search" /></span>
					</button>
				</p>
			</cherry:form>
		</div>
		<div id="section" class="section hide">
			<%-- 查询结果一览 --%>
			<div class="section-header">
				<strong>
					<span class="ui-icon icon-ttl-section-search-result"></span>
					<s:text name="global.page.list" />
				</strong>
			</div>
			<div class="section-content">
				<div class="toolbar clearfix">
					<span class="left">
						<cherry:show domId="BINOLPTJCS43DEL">
							<s:url action="BINOLPTJCS43_delGroups" id="delGroups"></s:url>
			                <a href="" class="add" onclick="BINOLPTJCS43.delGroups('${delGroups}');return false;">
			                   <span class="ui-icon icon-disable"></span>
			                   <span class="button-text"><s:text name="JCS43_delGroups"/></span>
			                </a>
						</cherry:show>
					</span>
				</div>
				<table id="dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
					<thead>
						<tr>
							<th><input type="checkbox" id="checkAll" onclick="BINOLPTJCS43.checkRecord(this,'#dataTable_Cloned');" /></th>
							<th><s:text name="JCS43_groupId" /></th>
							<th><s:text name="JCS43_nameTotal" /></th>
						</tr>
					</thead>
				</table>
			</div>
		</div>
	</div>
</s:i18n>
<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>

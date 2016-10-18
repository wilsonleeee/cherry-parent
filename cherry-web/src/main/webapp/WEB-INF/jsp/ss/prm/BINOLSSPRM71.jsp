<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/ss/prm/BINOLSSPRM71.js"></script>
<%-- 促销品查询URL --%>
<s:i18n name="i18n.ss.BINOLSSPRM71">
	<s:url id="search_url" action="BINOLSSPRM71_search" />
	<s:hidden name="search_url" value="%{search_url}" />
	<s:text id="selectAll" name="global.page.all" />
	<s:text id="dialogClose" name="global.page.close"/>
	<div class="hide">
		<div id="confirmDialogText">
			<p class="message"><s:text name="PRM71_confirmDialogText"/></p>
		</div>
		<span id="dialogConfirm"><s:text name="global.page.ok" /></span>
		<span id="dialogCancel"><s:text name="global.page.cancle" /></span>
	</div>
	<div class="panel-header">
		<div class="clearfix">
			<span class="breadcrumb left"> <jsp:include	page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" /></span>
			<span class="right">
				<cherry:show domId="BINOLSSPRM71CON">
					<s:url id="conjunction_url" action="BINOLSSPRM71_conjunction"/>
					<a href="${conjunction_url}" class="add" onclick="BINOLSSPRM71.associateInit(this);return false;">
	       		 		<span class="button-text"><s:text name="PRM71_conjunction"/></span>
	       		 		<span class="ui-icon icon-add"></span>
	       		 	</a>
       		 	</cherry:show>
			</span>
		</div>
	</div>
	<div id="errorMessage"></div>
	<div id="dialogInit"></div>
	<div id="actionResultDisplay"></div>
	<div id="actionResult"></div>
	<div class="panel-content">
		<div class="box">
			<cherry:form id="mainForm" class="inline" onsubmit="BINOLSSPRM71.search(); return false;">
				<input name="brandInfoId" id="brandInfoId" type="hidden" value='<s:property value="#session.userinfo.BIN_BrandInfoID"/>'></input>
				<div class="box-header">
					<strong>
						<span class="ui-icon icon-ttl-search"></span>
						<s:text name="global.page.condition" />
					</strong>
				</div>
				<div class="box-content clearfix">
					<div class="column" style="width: 49%;">
						<p>
							<label style="width: 80px;"><s:text name="PRM71_promCate" /></label>
							<%-- 促销品类型 --%>
							<s:select name="prmCate" list='#application.CodeTable.getCodes("1139")' listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{selectAll}" value="'CXLP'" />
						</p>
						<p>
							<%-- 促销品全名--%>
							<label style="width: 80px;"><s:text name="PRM71_nameTotal" /></label>
							<s:textfield name="nameTotal" cssClass="text" maxlength="50" />
						</p>
					</div>
					<div class="column last" style="width: 50%;">
						<p>
							<%-- 促销品厂商编码--%>
							<label style="width: 80px;"><s:text name="PRM71_unitCode" /></label>
							<s:textfield name="unitCode" cssClass="text" />
						</p>
						<p>
							<%-- 促销品条码--%>
							<label style="width: 80px;"><s:text name="PRM71_barCode" /></label>
							<s:textfield name="barCode" cssClass="text" />
						</p>
					</div>
				</div>
				<p class="clearfix">
					<%-- 促销品查询按钮 --%>
					<button class="right search" type="submit" onclick="BINOLSSPRM71.search();return false;">
						<span class="ui-icon icon-search-big"></span>
						<span class="button-text"><s:text name="global.page.search" /></span>
					</button>
				</p>
			</cherry:form>
		</div>
		<div id="conjunctionDiv"></div>
		<div id="section" class="section hide">
			<div class="section-header">
				<strong>
					<span class="ui-icon icon-ttl-section-search-result"></span>
					<s:text name="global.page.list" />
				</strong>
			</div>
			<div class="section-content">
				<div class="toolbar clearfix">
					<span class="left">
						<cherry:show domId="BINOLSSPRM71DEL">
							<s:url action="BINOLSSPRM71_delGroup" id="delGroup"></s:url>
			                <a href="" class="add" onclick="BINOLSSPRM71.delGroup('${delGroup}');return false;">
			                   <span class="ui-icon icon-disable"></span>
			                   <span class="button-text"><s:text name="PRM71_delGroup"/></span>
			                </a>
						</cherry:show>
					</span>
					<%-- <!-- 列设置 -->
					<span class="right">
						<a class="setting">
							<span class="ui-icon icon-setting"></span> 
							<span class="button-text"><s:text name="global.page.colSetting" /></span>
						</a>
					</span> --%>
				</div>
				<table id="dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
					<thead>
						<tr>
							<th><input type="checkbox" id="checkAll" onclick="BINOLSSPRM71.checkRecord(this,'#dataTable_Cloned');" /></th>
							<th><s:text name="PRM71_groupId" /></th>
							<th><s:text name="PRM71_nameTotal" /></th>
							<%-- <th><s:text name="PRM71_unitCode" /></th>
							<th><s:text name="PRM71_barCode" /></th>
							<th><s:text name="PRM71_promCate" /></th>
							<th><s:text name="PRM71_validFlag" /></th> --%>
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
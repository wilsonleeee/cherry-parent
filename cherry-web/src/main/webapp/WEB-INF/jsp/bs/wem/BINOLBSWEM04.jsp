<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/bs/wem/BINOLBSWEM04.js"></script>
<script type="text/javascript" src="/Cherry/js/bs/common/BINOLBSCOM03.js"></script>
<s:i18n name="i18n.bs.BINOLBSWEM04">
	<%-- 查询URL --%>
	<s:url id="search_url" action="BINOLBSWEM04_search" />
	<s:hidden name="search_url" value="%{search_url}" />
	<s:url id="addInit_Url" action="BINOLBSWEM04_add"/>
    <a id="addInit_Url" href="${addInit_Url}"></a>
	<s:text name="global.page.select" id="select_default" />
	<div class="panel-header">
		<div class="clearfix">
			<span class="breadcrumb left">
				<jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
			</span>
			<span class="right"> 
			<cherry:show domId="BINOLBSWEM04ADD">
	    	 	<a href="${addInit_Url}" class="add" onclick="javascript:openWin(this);return false;">
			 		<span class="button-text"><s:text name="WEM04_add"/></span>
			 		<span class="ui-icon icon-add"></span>
			 	</a>
		 	</cherry:show>
  		    </span> 
		</div>
	</div>
	<%-- ================== 错误信息提示 START ======================= --%>
	<div id="errorMessage"></div>
	<div style="display: none" id="errorMessageTemp">
		<div class="actionError">
			<ul>
				<li><span><s:text name="EBS00017" /></span></li>
			</ul>
		</div>
	</div>
	<%-- ================== 错误信息提示   END  ======================= --%>
	<div id="actionResultDisplay"></div>
	<div class="panel-content">
		<cherry:form id="mainForm">
			<s:hidden name="provinceId" id="provinceId" />
			<s:hidden name="cityId" id="cityId" />
			<div class="box">
				<div class="box-header">
					<strong> <span class="ui-icon icon-ttl-search"></span> <%-- 查询条件 --%>
						<s:text name="WEM04_condition" />
					</strong>
				</div>
				<div class="box-content clearfix">
					<div class="column" style="width: 50%;">
						<p>
							<label> <s:text name="WEM04_mobilePhone" />
							</label> <input name="agentMobile" class="text">
						</p>
						<p>
							<label><s:text name="WEM04_name" /></label>
							<input name="agentName" class="text">
						</p>
						<p>
							<label><s:text name="WEM04_superMobilePhone" /></label>
							<input name="superMobile" class="text">
						</p>
						<p>
							<label><s:text name="WEM04_departName" /></label>
							<input name="departName" class="text">
						</p>
					</div>
					<div class="column last" style="width: 49%;">
						<p>
							<!-- 部门类型 -->
							<label><s:text name="WEM04_level" /></label>
							<s:select name="agentLevel" list="agentLevelList" listKey="codeKey" listValue="value1" headerKey="" headerValue='%{#select_default}'></s:select>
						</p>
						<p style="margin: 0.2em 0 1em;">
							<%-- 所属省份 --%>
							<label><s:text name="WEM04_provinceName" /></label>
							<a id="province" class="ui-select" style="margin-left: 0px; font-size: 12px;">
							<span id="provinceText" class="button-text choice">
								<s:text name="global.page.select" /></span>
								<span class="ui-icon ui-icon-triangle-1-s"></span>
							</a>
						</p>
						<p style="margin: 0.4em 0 0.8em;">
							<%-- 所属城市 --%>
							<label><s:text name="WEM04_cityName" /></label>
								<a id="city" class="ui-select" style="margin-left: 0px; font-size: 12px;">
								<span id="cityText" class="button-text choice">
									<s:text name="global.page.select" />
								</span>
								<span class="ui-icon ui-icon-triangle-1-s"></span>
							</a>
						</p>
					</div>
				</div>
				<p class="clearfix">
					<%-- 查询 --%>
					<button class="right search" onclick="BINOLBSWEM04.search();return false;">
						<span class="ui-icon icon-search-big"></span>
						<span class="button-text"><s:text name="WEM04_search" /></span>
					</button>
				</p>
			</div>
		</cherry:form>
		<%-- 查询结果一览 --%>
		<div class="section hide" id="section">
			<div class="section-header">
				<strong> 
					<span class="ui-icon icon-ttl-section-search-result"></span>
					<s:text name="WEM04_results_list" />
				</strong>
			</div>
			<div class="section-content" id="result_list">
				<div class="toolbar clearfix">
					<span class="right">
						<a class="setting">
							<span class="ui-icon icon-setting"></span>
							<span class="button-text">
								<s:text name="WEM04_colSetting" />
							</span>
						</a>
					</span>
				</div>
				<div id="resultList">
					<table id="dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
						<thead>
							<tr>
								<th><s:text name="No" /></th>
								<th><s:text name="WEM04_mobilePhone" /></th>
								<th><s:text name="WEM04_name" /></th>
								<th><s:text name="WEM04_departName" /></th>
								<th><s:text name="WEM04_organization" /></th>
								<th><s:text name="WEM04_superMobilePhone" /></th>
								<th><s:text name="WEM04_provinceName" /></th>
								<th><s:text name="WEM04_cityName" /></th>
								<th><s:text name="WEM04_validFlag" /></th>
							</tr>
						</thead>
					</table>
				</div>
			</div>
		</div>
	</div>
	<div class="hide" id="dialogInit"></div>
	<div style="display: none;">
		<div id="dialogConfirm">
			<s:text name="global.page.ok" />
		</div>
		<div id="dialogCancel">
			<s:text name="global.page.cancle" />
		</div>
		<div id="dialogClose">
			<s:text name="global.page.close" />
		</div>
		<div id="selectSupreTitle">
			<s:text name="WEM04_selectSuperMobile" />
		</div>
		
	</div>
	<div id="provinceTemp" class="ui-option hide" style="width: 325px;">
		<div class="clearfix">
			<span class="label"><s:text name="global.page.range"></s:text></span>
			<ul class="u2">
				<li onclick="bscom03_getNextRegin(this, '${select_default }', 1);return false;">
					<s:text name="global.page.all"></s:text>
				</li>
			</ul>
		</div>
		<s:iterator value="reginList">
			<s:set name="provinceList" value="provinceList" />
			<div class="clearfix">
				<span class="label"><s:property value="reginName" /></span>
				<ul class="u2">
					<s:iterator value="#provinceList">
						<li id="<s:property value="provinceId" />" onclick="bscom03_getNextRegin(this, '${select_default }', 1);">
							<s:property value="provinceName" />
						</li>
					</s:iterator>
				</ul>
			</div>
		</s:iterator>
	</div>
	<div id="cityTemp" class="ui-option hide"></div>
	<%-- 下级区域查询URL --%>
	<s:url id="querySubRegionUrl" value="/common/BINOLCM08_querySubRegion"></s:url>
	<s:hidden name="querySubRegionUrl" value="%{querySubRegionUrl}" />
	<span id="defaultTitle" class="hide">
		<s:text name="global.page.range"></s:text>
	</span>
	<span id="defaultText" class="hide">
		<s:text name="global.page.all"></s:text>
	</span>
	<%-- ================== dataTable共通导入 START ======================= --%>
	<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
	<jsp:include page="/WEB-INF/jsp/common/popExportDialog.jsp" flush="true" />
	<%-- ================== dataTable共通导入    END  ======================= --%>
</s:i18n>
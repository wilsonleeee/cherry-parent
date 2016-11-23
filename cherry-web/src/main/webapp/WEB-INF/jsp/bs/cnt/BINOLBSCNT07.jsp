<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/bs/common/BINOLBSCOM03.js"></script>
<script type="text/javascript" src="/Cherry/js/bs/cnt/BINOLBSCNT07.js"></script>


<s:i18n name="i18n.bs.BINOLBSCNT07">
	<s:text name="global.page.select" id="select_default"/>
	<div class="panel-header">
		<div class="clearfix">
	 	 <span class="breadcrumb left">
			<jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
		 </span>

	    <span class="right">
			<%-- 导入积分计划柜台 --%>
			<a class="import" href="" onclick="return false;"><span class="ui-icon icon-import"></span><span class="button-text"><s:text name="CTN07.importLimitPlanCounter"></s:text></span></a>
			<%-- 导入经销商额度变更 --%>
			<a class="import" href="" onclick="return false;"><span class="ui-icon icon-import"></span><span class="button-text"><s:text name="CNT07.importLimitPointModified"></s:text></span></a>
	    </span>

		</div>
	</div>
	<%-- ================== 错误信息提示 START ======================= --%>
	<div id="actionResultDisplay"></div>
	<div id="errorMessage"></div>
	<div style="display: none" id="errorMessageTemp">
		<div class="actionError">
			<ul><li><span><s:text name="CNT07.errorMessage"/></span></li></ul>
		</div>
	</div>
	<%-- ================== 错误信息提示   END  ======================= --%>
	<div class="panel-content">
		<div class="box">
			<cherry:form id="mainForm" action="/basis/search" method="post" class="inline" onsubmit="binolbscnt01.search();return false;">

				<div class="box-content clearfix">
					<div class="column" style="width:50%; height: auto;">

					</div>
					<div class="column last" style="width:49%; height: auto;">

					</div>
				</div>
				<p class="clearfix">
					<%-- 积分计划柜台查询按钮 --%>
					<button class="right search" onclick="return false;">
						<span class="ui-icon icon-search-big"></span><span class="button-text"><s:text name="global.page.search"/></span>
					</button>
				</p>
			</cherry:form>
		</div>
		<div id="section" class="section hide">
			<div class="section-header">
				<strong>
					<span class="ui-icon icon-ttl-section-search-result"></span>
					<s:text name="global.page.list"/>
				</strong>
			</div>
			<div class="section-content">
				<div class="toolbar clearfix">
           	<span class="left">
				<!-- 积分计划导出 -->
			   <a id="export" class="export" onclick="return false;">
				   <span class="ui-icon icon-export"></span>
				   <span class="button-text"><s:text name="global.page.export"/></span>
			   </a>
				<!-- 停用 -->
				<a href="" class="add" onclick="return false;">
					<span class="ui-icon icon-enable"></span>
					<span class="button-text"><s:text name="global.page.enable"/></span>
				</a>
				<!-- 启用 -->
				<a href="" class="delete" onclick="return false;">
					<span class="ui-icon icon-disable"></span>
					<span class="button-text"><s:text name="global.page.disable"/></span>
				</a>

				<a href="" class="edit" onclick="return false;">
					<span class="ui-icon icon-edit"></span>
					<span class="button-text"><s:text name="CNT07.limitEdit"/></span>
				</a>

            </span>
           	<span class="right">
           		<%-- 列设置按钮  --%>
           		<a href="#" class="setting">
					<span class="button-text"><s:text name="global.page.colSetting"/></span>
					<span class="ui-icon icon-setting"></span>
				</a>
           	</span>
				</div>
				<table id="dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
					<thead>
					<tr>
						<th><input type="checkbox" id="checkAll" onclick="bscom03_checkRecord(this,'#dataTable_Cloned');"/></th>

					</tr>
					</thead>
				</table>
			</div>
		</div>
	</div>

	<div style="display: none;">
		<div id="privilegeTitle"><s:text name="global.page.privilegeTitle" /></div>
		<div id="privileMessage"><p class="message"><span><s:text name="global.page.privileMessage" /></span></p></div>
		<div id="disableTitle"><s:text name="CNT07.disableTitle" /></div>
		<div id="enableTitle"><s:text name="CNT07.enableTitle" /></div>
		<div id="disableMessage"><p class="message"><span><s:text name="CNT07.disableMessage" /></span></p></div>
		<div id="enableMessage"><p class="message"><span><s:text name="CNT07.enableMessage" /></span></p></div>
		<div id="dialogConfirm"><s:text name="global.page.ok" /></div>
		<div id="dialogCancel"><s:text name="global.page.cancle" /></div>
		<div id="dialogClose"><s:text name="global.page.close" /></div>
	</div>
</s:i18n>

<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>

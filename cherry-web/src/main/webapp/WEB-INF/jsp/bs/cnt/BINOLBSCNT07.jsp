<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/bs/common/BINOLBSCOM03.js"></script>
<script type="text/javascript" src="/Cherry/js/bs/cnt/BINOLBSCNT07.js?v=20161123"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/bs/cnt/BINOLBSCNT07.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/common/popDataTable.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>

<%-- 导入经销商额度变更URL --%>
<s:url id="importInit_url"  action="BINOLBSCNT08_init"/>

<s:i18n name="i18n.bs.BINOLBSCNT07">

	<%-- 停用/启用 URL --%>
	<s:url action="BINOLBSCNT07_enablePointPlan" id="enablePointPlan"></s:url>
	<s:url action="BINOLBSCNT07_disablePointPlan" id="disablePointPlan"></s:url>
	<s:url action="BINOLBSCNT07_pointChange" id="pointChange"></s:url>
	<s:url id="enableInit_url" value="/basis/BINOLBSCNT07_enableInit"/>
	<a id="enableInitUrl" href="${enableInit_url}"></a>

	<s:url id="disableInit_url" value="/basis/BINOLBSCNT07_disableInit"/>
	<a id="disableInitUrl" href="${disableInit_url}"></a>

	<s:url id="pointChangeInit_url" value="/basis/BINOLBSCNT07_pointChangeInit"/>
	<a id="pointChangeInitUrl" href="${pointChangeInit_url}"></a>
	<s:url id="search_url" value="BINOLBSCNT07_search"/>
	<s:hidden name="search_url" value="%{search_url}"/>
	<s:text name="global.page.select" id="select_default"/>
	<div class="panel-header">
		<div class="clearfix">
	 	 <span class="breadcrumb left">
			<jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
		 </span>

	    <span class="right">
			<%-- 导入积分计划柜台 --%>
			<s:url id="importLimitPlanCounter_url" action="BINOLBSCNT07_importLimitPlanCounter"/>
			<a class="import" href="${importLimitPlanCounter_url}" onclick="javascript:openWin(this);return false;"><span class="ui-icon icon-import"></span><span class="button-text"><s:text name="CTN07.importLimitPlanCounter"></s:text></span></a>
			<%-- 导入经销商额度变更 --%>
			<a  class="import"  href="${importInit_url}" onclick="javascript:openWin(this);return false;"><span class="ui-icon icon-import"></span><span class="button-text"><s:text name="CNT07.importLimitPointModified"></s:text></span></a>
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
	<div style="display: none" id="ECNT001"><!--只能选择一个用户进行该操作，请重新选择！ -->
		<div class="actionError">
			<ul><li><span><s:text name="CNT07.ECNT001"/></span></li></ul>
		</div>
	</div>
	<%-- ================== 错误信息提示   END  ======================= --%>
	<div class="panel-content">
		<div class="box">
			<cherry:form id="mainForm" action="/basis/search" method="post" class="inline" onsubmit="binolbscnt01.search();return false;">

				<div class="box-content clearfix">
					<div class="column" style="width:50%; height: auto;">
						<%-- 柜台名称 --%>
						<p>
							<label><s:text name="CNT07.counterNameIF"/></label>
							<s:textfield name="counterName" cssClass="text"/>
						</p>
						<%-- 柜台号 --%>
						<p>
							<label><s:text name="CNT07.counterCode"/></label>
							<s:textfield name="counterCode" cssClass="text"/>
						</p>
						<%-- 积分范围 --%>
						<p>
							<label><s:text name="CNT07.pointLimit"/></label>
							<s:textfield name="pointLimitBegin" cssClass="text"/>-<s:textfield name="pointLimitEnd" cssClass="text"/>
						</p>
					</div>
					<div class="column last" style="width:49%; height: auto;">
						<p>
							<%-- 积分计划 --%>
							<label><s:text name="CNT07.pointPlanStatus"/></label>
							<select name="pointPlanStatus">
								<option value=""><s:text name="CNT07.all"/></option>
								<option value="1" selected><s:text name="CNT07.yes"/></option>
								<option value="0"><s:text name="CNT07.no"/></option>
							</select>
						</p>
						<p>
							<%-- 柜台状态 --%>
							<label><s:text name="CNT07.counterStatus"/></label>
							<s:select list='#application.CodeTable.getCodes("1030")' listKey="CodeKey" listValue="Value" name="counterStatus" headerKey="" headerValue="%{#select_default}"></s:select>
						</p>
							<%-- 积分日期范围(存在交集就查询) --%>
						<p>
							<label><s:text name="CNT07.pointDate"/></label>
							<span><s:textfield name="pointDateBegin" cssClass="date"/></span> - <span><s:textfield name="pointDateEnd" cssClass="date"/></span>
						</p>

					</div>
				</div>
				<p class="clearfix">
					<%-- 积分计划柜台查询按钮 --%>
					<button class="right search" onclick="binolbscnt07.search();return false;">
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
				<!-- 启用 -->
				<a href="" class="add" onclick="binolbscnt07.operatePointPlanPop('enable','${enablePointPlan}');return false;">
					<span class="ui-icon icon-enable"></span>
					<span class="button-text"><s:text name="global.page.enable"/></span>
				</a>
				<!-- 停用 -->
				<a href="" class="delete" onclick="binolbscnt07.operatePointPlanPop('disable','${disablePointPlan}');return false;">
					<span class="ui-icon icon-disable"></span>
					<span class="button-text"><s:text name="global.page.disable"/></span>
				</a>

				<a href="" class="edit" onclick="binolbscnt07.operatePointPlanPop('pointChange','${pointChange}');return false;">
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
						<th style="text-align: center"><input type="checkbox" id="checkAll" onclick="bscom03_checkRecord(this,'#dataTable_Cloned');"/></th>
						<!-- 行号 -->
						<th style="text-align: center"><s:text name="CNT07.number"></s:text></th>
						<!-- 柜台编号 -->
						<th style="text-align: center"><s:text name="CNT07.counterCode"></s:text></th>
						<!-- 柜台名称 -->
						<th style="text-align: center"><s:text name="CNT07.counterName"></s:text></th>
						<!-- 积分计划 -->
						<th style="text-align: center"><s:text name="CNT07.pointPlan"></s:text></th>
						<!-- 说明 -->
						<th style="text-align: center"><s:text name="CNT07.explain"></s:text></th>
						<!-- 开始日期 -->
						<th style="text-align: center"><s:text name="CNT07.startDate"></s:text></th>
						<!-- 结束日期 -->
						<th style="text-align: center"><s:text name="CNT07.endDate"></s:text></th>
						<!-- 经销商额度 -->
						<th style="text-align: center"><s:text name="CNT07.currentPointLimit"></s:text></th>
						<!-- 修改者 -->
						<th style="text-align: center"><s:text name="CNT07.modifier"></s:text></th>
						<!-- 备注 -->
						<th style="text-align: center"><s:text name="CNT07.comment"></s:text></th>
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
		<div id="pointChangeTitle"><s:text name="CNT07.pointChangeTitle" /></div>
		<div id="disableMessage"><p class="message"><span><s:text name="CNT07.disableMessage" /></span></p></div>
		<div id="enableMessage"><p class="message"><span><s:text name="CNT07.enableMessage" /></span></p></div>
		<div id="dialogConfirm"><s:text name="global.page.ok" /></div>
		<div id="dialogCancel"><s:text name="global.page.cancle" /></div>
		<div id="dialogClose"><s:text name="global.page.close" /></div>
	</div>
	<div class="hide" id="dialogInit"></div>
<%--	<div class="hide" id="dialogDetail">
		<div style='text-align: center;'>
			开始时间
			<s:textfield id="startDate" name="startDate" cssClass="date" readOnly="readOnly"></s:textfield>
			<p class="message"><s:text name="CNT07.enableMessage" /></p>
		</div>
	</div>--%>
</s:i18n>

<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>
<script type="text/javascript">

	$('#pointDateBegin').cherryDate({
		beforeShow: function(input){
			var value = $('#pointDateEnd').val();
			return [value,'maxDate'];
		}
	});
	$('#pointDateEnd').cherryDate({
		beforeShow: function(input){
			var value = $('#pointDateBegin').val();
			return [value,'minDate'];
		}
	});


</script>

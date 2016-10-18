<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/common/departBar.js"></script>
<script type="text/javascript" src="/Cherry/js/mo/wat/BINOLMOWAT05_1.js"></script>
<s:i18n name="i18n.mo.BINOLMOWAT05">
    <div class="hide">
        <s:url id="detailList_url" value="/mo/BINOLMOWAT05_detailList"/>
        <a id="detailListUrl" href="${detailList_url}"></a>
        <s:url id="export" action="BINOLMOWAT05_export" ></s:url>
        <a id="downUrl" href="${export}"></a>
    </div>
	<div class="panel ui-corner-all">
		<div id="div_main">
			<div class="panel-header">
				<div class="clearfix"> 
					<span class="breadcrumb left">	    
						<span class="ui-icon icon-breadcrumb"></span>
						<s:text name="WAT05_native1" />&nbsp;&gt;&nbsp;
						<s:text name="WAT05_native2" />
					</span>
				</div>
			</div>
			<div id="errorMessage"></div>
				<div id="actionResultDisplay"></div>
				<div class="panel-content clearfix">
					<div class="box">
					<form id="detailForm">
						<input type="hidden" value='<s:property value="employeeId"/>'  name="employeeId" />
						<input type="hidden" value='<s:property value="brandInfoId"/>'  name="brandInfoId" />
						<input type="hidden" value='<s:property value="startAttendanceDate"/>'  name="startAttendanceDate" />
						<input type="hidden" value='<s:property value="endAttendanceDate"/>'  name="endAttendanceDate" />
					</form>
						<div class="box-header"> 
							<strong><span class="ui-icon icon-ttl-search"></span><s:text name="global.page.condition"/></strong>
						</div>
						<div class="box-content clearfix">
							<div class="column" style="width:50%; height: auto;">
								<p>
		                        	<%-- 员工姓名--%>
		                          <label style="width:80px"><s:text name="WAT05_employeeName"/>：</label>
		                          <span><s:property value="%{attendanceEmpMap.employeeCodeName}"/></span>
		                        </p>
		                        <p>
		                        	<%-- 岗位 --%>
		                            <label style="width:80px"><s:text name="WAT05_categoryName"/>：</label>
		                            <span><s:property value="%{attendanceEmpMap.categoryName}"/></span>
		                        </p>
							</div>
							<div class="column last" style="width:49%; height: auto;">
								<p>
									<%-- U盘序列号 --%>
									<label style="width:80px"><s:text name="WAT05_udiskSN"/>：</label>
									<span><s:property value="%{attendanceEmpMap.udiskSN}"/></span>
								</p>
								<p>
									<%-- 考勤日期--%>
									<label style="width:80px"><s:text name="WAT05_attendanceDate"/>：</label>
									<span><input id="startAttendanceDate" class="date" disabled="disabled" value='<s:property value="%{attendanceEmpMap.startAttendanceDate}"/>'/></span>
									- 
									<span><input id="endAttendanceDate" class="date" disabled="disabled" value='<s:property value="%{attendanceEmpMap.endAttendanceDate}"/>'/></span>
								</p>
							</div>
							<%-- ======================= 组织联动共通导入开始  ============================= --%>
		                    <s:action name="BINOLCM13_query" namespace="/common" executeResult="true">
		                        <s:param name="businessType">3</s:param>
		                        <s:param name="operationType">1</s:param>
		                        <s:param name="showType">0</s:param>
		                        <s:param name="mode">dpat,area</s:param>
		                    </s:action>
		                    <%-- ======================= 组织联动共通导入结束  ============================= --%>
						</div>
						<p class="clearfix">
 						<button class="right search" onclick="BINOLMOWAT05_1.search();return false;">
							<span class="ui-icon icon-search-big"></span>
							<span class="button-text">
								<s:text name="global.page.search"></s:text>
							</span>
							</button>
						</p>
					</div>
					<div class="section hide" id="detailListSection">
						<div class="section-header">
							<strong>
						  	<span class="ui-icon icon-ttl-section-search-result"></span>
						  	<s:text name="global.page.list"></s:text>
					  	</strong>
						</div>
 					<div class="section-content">
 						<div class="toolbar clearfix">
 							<cherry:show domId="BINOLMOWAT05EXP">
			                    <span class="left">
			                        <a id="export" class="export">
			                            <span class="ui-icon icon-export"></span>
			                            <span class="button-text"><s:text name="global.page.export"/></span>
			                        </a>
			                    </span>
			                 </cherry:show>
						  	<span class="right">
					  			<a class="setting">
					  				<span class="ui-icon icon-setting"></span>
				              		<span class="button-text"><s:text name="global.page.colSetting"/></span>
			              		</a>
			              	</span>
 						</div>
 						<table cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%" id="detailDataTable">
 							<thead>
						        <tr>
			                        <th><s:text name="WAT05_num"/></th><%-- No. --%>
			                        <th><s:text name="WAT05_region"/></th><%-- 大区  --%>
			                        <th><s:text name="WAT05_province"/></th><%-- 省份 --%>
			                        <th><s:text name="WAT05_city"/></th><%-- 城市 --%>
			                        <th><s:text name="WAT05_departName"/></th><%-- 部门  --%>
			                        <th><s:text name="WAT05_arriveTime"/></th><%-- 到柜时间 --%>
			                        <th><s:text name="WAT05_leaveTime"/></th><%-- 离柜时间--%>
			                        <th><s:text name="WAT05_stayMinutesSum"/></th><%-- 离柜时间--%>
			                    </tr>
							</thead>
							<tbody></tbody>
 						</table>
 					</div>
					</div>
				</div>
		</div>
	</div>
	<div class="hide">
		<div id="processing"><span class="dataTables_processing"></span></div>
	</div>
 </s:i18n>
<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>
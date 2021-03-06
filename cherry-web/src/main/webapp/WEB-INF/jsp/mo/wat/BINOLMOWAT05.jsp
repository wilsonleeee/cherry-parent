<%-- 考勤信息查询 --%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<link rel="stylesheet" href="/Cherry/css/cherry/combobox.css" type="text/css">
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/common/popDataTable.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<%-- <script type="text/javascript" src="/Cherry/js/common/departBar.js"></script> --%>
<script type="text/javascript" src="/Cherry/js/mo/wat/BINOLMOWAT05.js"></script>
<s:i18n name="i18n.mo.BINOLMOWAT05">
    <div class="hide">
        <s:url id="search_url" value="/mo/BINOLMOWAT05_search"/>
        <a id="searchUrl" href="${search_url}"></a>
        <s:url id="export" action="BINOLMOWAT05_export" ></s:url>
        <a id="downUrl" href="${export}"></a>
        <s:url id="queryPositionCategory" action="BINOLMOWAT05_queryPositionCategory"/>
    </div>
    <div class="panel-header">
        <%-- 考勤信息查询--%>
        <div class="clearfix">
	    <span class="breadcrumb left">	    
			<jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
		</span>
            <span class="right">
            </span>
        </div>
    </div>
    <div id="errorMessage"></div>
    <%-- ================== 错误信息提示 START ======================= --%>
    <div id="errorDiv2" class="actionError" style="display:none">
        <ul>
            <li><span id="errorSpan2"></span></li>
        </ul>         
    </div>
    <%-- ================== 错误信息提示   END  ======================= --%>
    <div class="panel-content">
        <div class="box">
            <cherry:form id="mainForm" class="inline" onsubmit="BINOLMOWAT05.search(); return false;">
                <div class="box-header">
                    <strong>
                        <span class="ui-icon icon-ttl-search"></span>
                        <%-- 查询条件 --%>
                        <s:text name="WAT05_condition"/>
                    </strong>
                    <input type="checkbox" name="testFlag" id="testFlag" value="1"/><s:text name="WAT05_testFlag"/>
                    <input type="checkbox" name="validFlag" id="validFlag" value="1"/><s:text name="WAT05_validFlag"/> 
                </div>
                <div class="box-content clearfix">
                    <div class="column" style="width:50%;">
                        <p>
                        <%-- 所属品牌 --%>
                            <s:if test='%{brandInfoList != null && !brandInfoList.isEmpty()}'>
                            <label><s:text name="WAT05_brandInfo"></s:text></label>
                            <s:text name="WAT05_select" id="WAT05_select"/>
                            <s:select name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" headerKey="" headerValue="%{WAT05_select}" onchange="BINOLMOWAT05.doPositionCategory(this, '%{queryPositionCategory}')"></s:select>
                            </s:if>
                        </p>
                        <p>
                        <%-- 员工姓名--%>
                          <label><s:text name="WAT05_employeeName"/></label>
                          <s:textfield name="employeeName" cssClass="text" maxlength="30"/>
                        </p>
                        <p>
                        <%-- 岗位 --%>
                            <label><s:text name="WAT05_categoryName"></s:text></label>
                            <s:text name="WAT05_selectAll" id="WAT05_selectAll"/>
                            <s:select name="positionCategoryId" list="positionCategoryList" listKey="positionCategoryId" listValue="categoryName" headerKey="" headerValue="%{WAT05_selectAll}"></s:select>
                        </p>
                    </div>
                    <div class="column last" style="width:49%;">
                        <p>
                        <%-- U盘序列号 --%>
                          <label><s:text name="WAT05_udiskSN"/></label>
                          <s:textfield name="udiskSN" cssClass="text" maxlength="100"/>
                        </p>
                        <p>
                        <%-- 考勤日期--%>
                            <label><s:text name="WAT05_attendanceDate"></s:text></label>
                            <span><s:textfield id="startAttendanceDate" name="startAttendanceDate" cssClass="date"/></span>
                            - 
                            <span><s:textfield id="endAttendanceDate" name="endAttendanceDate" cssClass="date"/></span>
                        </p>
                    </div>
                    <!-- 主页负责对巡柜数与巡柜次数进行统计，查看详细见详细画面 -->
                    <%-- ======================= 组织联动共通导入开始  ============================= --%>
                    <%-- <s:action name="BINOLCM13_query" namespace="/common" executeResult="true">
                        <s:param name="businessType">3</s:param>
                        <s:param name="operationType">1</s:param>
                        <s:param name="showType">0</s:param>
                        <s:param name="mode">dpat,area</s:param>
                    </s:action> --%>
                    <%-- ======================= 组织联动共通导入结束  ============================= --%>
                </div>
                <p class="clearfix">
                    <button class="right search" type="submit" onclick="BINOLMOWAT05.search();return false;">
                        <span class="ui-icon icon-search-big"></span>
                        <%-- 查询 --%>
                        <span class="button-text"><s:text name="WAT05_search"/></span>
                    </button>
                </p>
            </cherry:form>
        </div>
        <div id="section" class="section hide">
            <div class="section-header">
                <strong>
                    <span class="ui-icon icon-ttl-section-search-result"></span>
                    <%-- 查询结果一览 --%>
                    <s:text name="WAT05_results_list"/>
                </strong>
            </div>
            <div class="section-content">
                <div class="toolbar clearfix">
                	<cherry:show domId="BINOLMOWAT05EXP">
	                    <span class="left">
	                        <a id="exportDetail" class="export">
	                            <span class="ui-icon icon-export"></span>
	                            <span class="button-text"><s:text name="WAT05_exportDetail"/></span>
	                        </a>
	                        <a id="exportCount" class="export">
	                            <span class="ui-icon icon-export"></span>
	                            <span class="button-text"><s:text name="WAT05_exportCount"/></span>
	                        </a>
	                    </span>
	                </cherry:show>
                    <span class="right">
                        <a class="setting">
                            <span class="ui-icon icon-setting"></span>
                            <span class="button-text">
                                <%-- 设置列 --%>
                                <s:text name="WAT05_colSetting"/>
                            </span>
                        </a>
                    </span>
                </div>
                <table id="dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
                    <thead>
                    <tr>
                        <th><s:text name="WAT05_num"/></th><%-- No. --%>
                        <th><s:text name="WAT05_employeeName"/></th><%-- 员工姓名  --%>
                        <th><s:text name="WAT05_region"/></th><%-- 区域  --%>
                        <th><s:text name="WAT05_province"/></th><%-- 省份  --%>
                        <th><s:text name="WAT05_city"/></th><%-- 城市  --%>
                        <th><s:text name="WAT05_categoryName"/></th><%-- 岗位 --%>
                        <th><s:text name="WAT05_udiskSN"/></th><%-- U盘序列号 --%>
                        <th><s:text name="WAT05_arrCntCount"/></th><%-- 巡柜数 --%>
                        <th><s:text name="WAT05_arrCntSum"/></th><%-- 巡柜次数--%>
                        <th><s:text name="WAT05_stayMinutesSum"/></th><%-- 在柜工作分钟--%>
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
    <script type="text/javascript">
        // 节日
        var holidays = '${holidays }';
        $('#startAttendanceDate').cherryDate({
            holidayObj: holidays,
            beforeShow: function(input){
                var value = $('#endAttendanceDate').val();
                return [value,'maxDate'];
            }
        });
        $('#endAttendanceDate').cherryDate({
            holidayObj: holidays,
            beforeShow: function(input){
                var value = $('#startAttendanceDate').val();
                return [value,'minDate'];
            }
        });
    </script>
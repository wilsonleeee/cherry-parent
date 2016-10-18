<%-- 终端实时监控 --%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<link rel="stylesheet" href="/Cherry/css/cherry/combobox.css" type="text/css">
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/common/popDataTable.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/mo/wat/BINOLMOWAT01.js"></script>
<style>
label.column_label_left {
    width:165px;
    text-align: left;
}
input.text {
    width: 155px;
}
</style>
<s:i18n name="i18n.mo.BINOLMOWAT01">
    <div class="hide">
        <s:url id="search_url" value="/mo/BINOLMOWAT01_search"/>
        <a id="searchUrl" href="${search_url}"></a>
        <s:url id="export" action="BINOLMOWAT01_export" ></s:url>
        <a id="downUrl" href="${export}"></a>
    </div>
    <div class="panel-header">
        <%-- 终端实时监控--%>
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
            <cherry:form id="mainForm" class="inline" onsubmit="BINOLMOWAT01.search(); return false;">
            <%-- IE只有一个text框 ，按 Enter键跳转其他页面hack --%>
            <input class="hide"/>
                <div class="box-header">
<!--                    <strong>-->
<!--                        <span class="ui-icon icon-ttl-search"></span>-->
<!--                        <%-- 查询条件 --%>-->
<!--                        <s:text name="MAN01_condition"/>-->
<!--                    </strong>-->
                </div>
                <div class="box-content clearfix">
                    <div class="column" style="width:50%;min-width:405px;">
                        <p>
                        <%-- 所属品牌 --%>
                            <s:if test='%{brandInfoList != null && !brandInfoList.isEmpty()}'>
                            <label><s:text name="WAT01_brandInfo"></s:text></label>
                            <s:text name="WAT01_select" id="WAT01_select"/>
                            <s:select name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" headerKey="" cssStyle="width:100px;"></s:select>
                            </s:if>
                        </p>
                        <p>
                        <%-- 最后联络时差（分） --%>
                            <span>
                            <label class="column_label_left"><input id="searchType" name="searchType" class="radio" type="radio" value="minute" checked><s:text name="WAT01_dateDiff"></s:text></label>
                            <s:textfield name="dateDiff" cssClass="text" maxlength="9" value="60" onkeyup="value=value.replace(/[^\d]/g,'')"/>
                            </span>
                        </p>
                        <%--<p>--%>
                        <%-- 查询指定天数内未联网机器 --%>
                        <%--
                            <span>
                            <label><input id="searchType" name="searchType" class="radio" type="radio" value="day"><s:text name="WAT01_day"></s:text></label>
							<select id="nuberOfDays" name="nuberOfDays" style="width:120;" disabled>
                                <option value="1" selected>1天</option>
                                <option value="2">2天</option>
                                <option value="3">3天</option>
                                <option value="4">3天以上</option>
							</select>
                            </span>
                        </p>
                        --%>
                        <p>
                        <%-- 连接日期 --%>
                            <span>
                                <label class="column_label_left"><input id="searchType" name="searchType" class="radio" type="radio" value="date"><s:text name="WAT01_date"></s:text></label>
                                <span><s:textfield id="startDate" name="startDate" cssClass="date" disabled="true"/></span> - <span><s:textfield id="endDate" name="endDate" cssClass="date" disabled="true"/></span>
                            </span>
                        </p>
                    </div>
                    <div class="column last" style="width:39%;">
                        <p>
                            <%-- 机器编号 --%>
                            <label><s:text name="WAT01_machineCodeNew"/></label>
                            <s:textfield name="machineCode" cssClass="text" maxlength="30"/>
                        </p>
                        <p>
                            <%-- 柜台名 --%>
                            <label><s:text name="WAT01_counter"/></label>
                            <s:textfield name="counterCodeName" cssClass="text" maxlength="30"/>
                        </p>
                    </div>
                </div>
                <p class="clearfix">
                    <button class="right search" type="submit" onclick="BINOLMOWAT01.search();return false;">
                        <span class="ui-icon icon-refresh"></span>
                        <%-- 刷新 --%>
                        <span class="button-text"><s:text name="WAT01_refresh"/></span>
                    </button>
                </p>
            </cherry:form>
        </div>
        <div id="section" class="section hide">
            <div class="section-header">
                <strong>
                    <span class="ui-icon icon-ttl-section-search-result"></span>
                    <%-- 查询结果一览 --%>
                    <s:text name="WAT01_results_list"/>
                </strong>
            </div>
            <div class="section-content">
                <div class="toolbar clearfix">
                    <cherry:show domId="BINOLMOWAT01EXP">
                    <span class="left">
                        <a id="export" class="export"  onclick="BINOLMOWAT01.exportExcel();return false;">
                            <span class="ui-icon icon-export"></span>
                            <span class="button-text"><s:text name="global.page.export"/></span>
                        </a>
                    </span>
                    </cherry:show>
                    &nbsp;
                    <span id="headInfo" style=""></span>
                    <span class="right">
                        <a class="setting" id="setting1">
                            <span class="ui-icon icon-setting"></span>
                            <span class="button-text">
                                <%-- 设置列 --%>
                                <s:text name="WAT01_colSetting"/>
                            </span>
                        </a>
                        <a class="setting" id="setting2">
                            <span class="ui-icon icon-setting"></span>
                            <span class="button-text">
                                <%-- 设置列 --%>
                                <s:text name="WAT01_colSetting"/>
                            </span>
                        </a>
                    </span>
                </div>
                <table id="dataTable1" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
                    <thead>
                    <tr>
                        <th><s:text name="WAT01_num"/></th><%-- No. --%>
                        <th><s:text name="WAT01_machineCode"/></th><%-- 机器编号 --%>
                        <th><s:text name="WAT01_brandInfo"/></th><%-- 所属品牌  --%>
                        <th><s:text name="WAT01_machineType"/></th><%-- 机器类型  --%>
                        <th><s:text name="WAT01_softWareVersion"/></th><%-- 软件版本 --%>
                        <th><s:text name="WAT01_capacity"/></th><%-- 容量 --%>
                        <th><s:text name="WAT01_internetFlow"/></th><%-- 总流量 --%>
                        <th><s:text name="WAT01_internetTime"/></th><%-- 累积上网时间 --%>
                        <th><s:text name="WAT01_internetTimes"/></th><%-- 累积上网次数 --%>
                        <th><s:text name="WAT01_uploadLasttime"/></th><%-- 最后一次上传数据的时间 --%>
                        <th><s:text name="WAT01_syncLasttime"/></th><%-- 最后一次下载同步数据的时间 --%>
                        <th><s:text name="WAT01_phoneCode"/></th><%-- 通讯卡号 --%>
                        <th><s:text name="WAT01_provinceName"/></th><%-- 省份 --%>
                        <th><s:text name="WAT01_cityName"/></th><%-- 城市 --%>
                        <th><s:text name="WAT01_counterNameIF"/></th><%-- 柜台名 --%>
                        <th><s:text name="WAT01_iMSIcode"/></th><%-- IMSI号 --%>
                        <th><s:text name="WAT01_startTime"/></th><%-- 机器启用的时间 --%>
                        <th><s:text name="WAT01_lastStartTime"/></th><%-- 上一次开机时间 --%>
                        <th><s:text name="WAT01_lastConnTime"/></th><%-- 上一次连接时间 --%>
                        <th><s:text name="WAT01_connStatus"/></th><%-- 连接状态 --%>
                    </tr>
                    </thead>
                </table>
                <table id="dataTable2" cellpadding="0" cellspacing="0" border="0" class="jquery_table hide" width="100%">
                    <thead>
                    <tr>
                        <th><s:text name="WAT01_num"/></th><%-- No. --%>
                        <th><s:text name="WAT01_machineCode"/></th><%-- 机器编号 --%>
                        <th><s:text name="WAT01_brandInfo"/></th><%-- 所属品牌  --%>
                        <th><s:text name="WAT01_machineType"/></th><%-- 机器类型  --%>
                        <th><s:text name="WAT01_softWareVersion"/></th><%-- 软件版本 --%>
                        <th><s:text name="WAT01_capacity"/></th><%-- 容量 --%>
                        <th><s:text name="WAT01_internetFlow"/></th><%-- 总流量 --%>
                        <th><s:text name="WAT01_internetTime"/></th><%-- 累积上网时间 --%>
                        <th><s:text name="WAT01_internetTimes"/></th><%-- 累积上网次数 --%>
                        <th><s:text name="WAT01_uploadLasttime"/></th><%-- 最后一次上传数据的时间 --%>
                        <th><s:text name="WAT01_syncLasttime"/></th><%-- 最后一次下载同步数据的时间 --%>
                        <th><s:text name="WAT01_phoneCode"/></th><%-- 通讯卡号 --%>
                        <th><s:text name="WAT01_provinceName"/></th><%-- 省份 --%>
                        <th><s:text name="WAT01_cityName"/></th><%-- 城市 --%>
                        <th><s:text name="WAT01_counterNameIF"/></th><%-- 柜台名 --%>
                        <th><s:text name="WAT01_iMSIcode"/></th><%-- IMSI号 --%>
                        <th><s:text name="WAT01_startTime"/></th><%-- 机器启用的时间 --%>
                        <th><s:text name="WAT01_lastStartTime"/></th><%-- 上一次开机时间 --%>
                        <th><s:text name="WAT01_lastConnTime"/></th><%-- 上一次连接时间 --%>
                        <th><s:text name="WAT01_connStatus"/></th><%-- 连接状态 --%>
                        <th><s:text name="WAT01_connDays"/></th><%-- 连接天数 --%>
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
        $('#startDate').cherryDate({
            holidayObj: holidays,
            beforeShow: function(input){
                var value = $('#endDate').val();
                return [value,'maxDate'];
            }
        });
        $('#endDate').cherryDate({
            holidayObj: holidays,
            beforeShow: function(input){
                var value = $('#startDate').val();
                return [value,'minDate'];
            }
        });
    </script>
<%-- 异常盘点次数监控 --%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<link rel="stylesheet" href="/Cherry/css/cherry/combobox.css" type="text/css">
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/common/popDataTable.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/common/departBar.js"></script>
<script type="text/javascript" src="/Cherry/js/mo/wat/BINOLMOWAT04.js"></script>
<style>
.column label {
    width:120px;
}
input.text{
    width:145px;
}
</style>
<s:i18n name="i18n.mo.BINOLMOWAT04">
    <div class="hide">
        <s:url id="search_url" value="/mo/BINOLMOWAT04_search"/>
        <a id="searchUrl" href="${search_url}"></a>
        <s:url id="export" action="BINOLMOWAT04_export" ></s:url>
        <a id="downUrl" href="${export}"></a>
    </div>
    <div class="panel-header">
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
            <cherry:form id="mainForm" class="inline" onsubmit="BINOLMOWAT04.search(); return false;">
                <div class="box-header"></div>
                <div class="box-content clearfix">
                    <div class="column last" style="width:100%;">
                        <p>
                        <%-- 日期 --%>
                            <label style="width:auto"><s:text name="WAT04_date"/></label>
                            <span><s:textfield id="startDate" name="startDate" cssClass="date" /></span> - <span><s:textfield id="endDate" name="endDate" cssClass="date" /></span>
                            <span>
	                            <span class="highlight"><s:text name="WAT04_snow"/></span>
	                            <span class="gray"><s:text name="WAT04_notice"/></span>
                            </span>
                        </p>
                            <div style="margin:0;" class="box2 box2-active">
                                <div class="box2-header clearfix">
                                    <strong class="left active">
                                        <s:text name="WAT04_rule"></s:text>
                                    </strong>
                                </div>
                                <div style="padding:.5em .5em 0 .5em;" class="box2-content clearfix">
                                    <div id="divRule">
                                        <div id="divAmount" style="float:left;width:50%;">
                                            <p>
                                                <%-- 盘点次数>= --%>
                                                <span>
                                                <label><input checked type="radio" class="radio" id="cbAmount" name="cbAmount" ><s:text name="WAT04_maxLimit"></s:text></label>
                                                <s:textfield name="maxLimit" cssClass="text" maxlength="9"/>
                                                </span>
                                            </p>
                                        </div>
                                        <div id="divGainQuantity" style="float:left;width:50%;">
                                            <p>
                                                <%-- 盘差>= --%>
                                                <span>
                                                <label><input type="radio"  class="radio" id="cbGainQuantityGE" name="cbGainQuantity" ><s:text name="WAT04_gainQuantityGE"></s:text></label>
                                                <s:textfield name="gainQuantityGE" cssClass="text" maxlength="9" disabled="true"/>
                                                </span>
                                            </p>
                                            <%--<p>--%>
                                                <%-- 盘差<= --%>
                                            <%--
                                                <span>
                                                <label><input type="checkbox"  class="checkbox" id="cbGainQuantityLE" name="cbGainQuantity" ><s:text name="WAT04_gainQuantityLE"></s:text></label>
                                                <s:textfield name="gainQuantityLE" cssClass="text" maxlength="9" disabled="true"/>
                                                </span>
                                            </p>
                                            --%>
                                        </div>
                                    </div>
                                </div>
                            </div>
                    </div>
                    <%-- ======================= 组织联动共通导入开始  ============================= --%>
                    <s:action name="BINOLCM13_query" namespace="/common" executeResult="true">
                        <s:param name="businessType">3</s:param>
                        <s:param name="operationType">1</s:param>
                        <s:param name="showType">0</s:param>
                        <s:param name="mode">dpat,area,chan</s:param>
                    </s:action>
                    <%-- ======================= 组织联动共通导入结束  ============================= --%>
                </div>
                <p class="clearfix">
                    <button class="right search" type="submit" onclick="BINOLMOWAT04.search();return false;">
                        <span class="ui-icon icon-search-big"></span>
                        <%-- 查询 --%>
                        <span class="button-text"><s:text name="WAT04_search"/></span>
                    </button>
                </p>
            </cherry:form>
        </div>
        <div id="section" class="section hide">
            <div class="section-header">
                <strong>
                    <span class="ui-icon icon-ttl-section-search-result"></span>
                    <%-- 查询结果一览 --%>
                    <s:text name="WAT04_results_list"/>
                </strong>
            </div>
            <div class="section-content">
                <div class="toolbar clearfix">
                <cherry:show domId="BINOLMOWAT04EXP">
                    <span class="left">
                        <a id="export" class="export">
                            <span class="ui-icon icon-export"></span>
                            <span class="button-text"><s:text name="global.page.export"/></span>
                        </a>
                    </span>
                </cherry:show>
                    <span class="right">
                        <a class="setting" id="setting1">
                            <span class="ui-icon icon-setting"></span>
                            <span class="button-text">
                                <%-- 设置列 --%>
                                <s:text name="WAT04_colSetting"/>
                            </span>
                        </a>
                    </span>
                    <span class="right">
                        <a class="setting2" id="setting2">
                            <span class="ui-icon icon-setting"></span>
                            <span class="button-text">
                                <%-- 设置列 --%>
                                <s:text name="WAT04_colSetting"/>
                            </span>
                        </a>
                    </span>
                </div>
                <table id="dataTable1" cellpadding="0" cellspacing="0" border="0" class="jquery_table hide" width="100%">
                    <thead>
                    <tr>
                        <th><s:text name="WAT04_num"/></th><%-- No. --%>
                        <th><s:text name="WAT04_departCode"/></th><%-- 部门编号 --%>
                        <th><s:text name="WAT04_departName"/></th><%-- 部门名称  --%>
                        <th><s:text name="WAT04_count"/></th><%-- 盘点次数 --%>
                    </tr>
                    </thead>
                </table>
                <table id="dataTable2" cellpadding="0" cellspacing="0" border="0" class="jquery_table hide" width="100%">
                    <thead>
                    <tr>
                        <th><s:text name="WAT04_num"/></th><%-- No. --%>
                        <th><s:text name="WAT04_stockTakingNo"/></th><%-- 盘点单号 --%>
                        <th><s:text name="WAT04_departCode"/></th><%-- 部门编号 --%>
                        <th><s:text name="WAT04_departName"/></th><%-- 部门名称  --%>
                        <th><s:text name="WAT04_gainQuantity"/></th><%-- 盘点次数 --%>
                        <th><s:text name="WAT04_date"/></th><%-- 盘点日期 --%>
                        <th><s:text name="WAT04_employeeName"/></th><%-- 操作员 --%>
                    </tr>
                    </thead>
                </table>
            </div>
        </div>
    </div>
<div id="errmessage" style="display:none">
    <input type="hidden" id="errmsg_EMO00023" value='<s:text name="EMO00023"/>'/>
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
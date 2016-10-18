<%-- 会员异常数据监控 --%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<link rel="stylesheet" href="/Cherry/css/cherry/combobox.css" type="text/css">
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/common/popDataTable.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/common/departBar.js"></script>
<script type="text/javascript" src="/Cherry/js/mo/wat/BINOLMOWAT03.js"></script>
<style>
.column label {
    width:85px;
}
input.text{
    width:145px;
}
</style>
<s:i18n name="i18n.mo.BINOLMOWAT03">
    <div class="hide">
        <s:url id="search_url" value="/mo/BINOLMOWAT03_search"/>
        <a id="searchUrl" href="${search_url}"></a>
        <s:url id="export" action="BINOLMOWAT03_export" ></s:url>
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
            <cherry:form id="mainForm" class="inline" onsubmit="BINOLMOWAT03.search(); return false;">
                <div class="box-header"></div>
                <div class="box-content clearfix">
                    <div class="column last" style="width:100%;">
                        <p>
                        <%-- 日期 --%>
                            <label style="width:auto"><s:text name="WAT03_date"/></label>
                            <span><s:textfield id="startDate" name="startDate" cssClass="date" /></span> - <span><s:textfield id="endDate" name="endDate" cssClass="date" /></span>
                            <span>
	                            <span class="highlight"><s:text name="WAT03_snow"/></span>
	                            <span class="gray"><s:text name="WAT03_notice"/></span>
                            </span>
                        </p>
                            <div style="margin:0;" class="box2 box2-active">
                                <div class="box2-header clearfix">
                                    <strong class="left active">
                                        <s:text name="WAT03_rule"></s:text>
                                    </strong>
                                </div>
                                <div style="padding:.5em .5em 0 .5em;" class="box2-content clearfix">
                                    <div id="divRule">
                                        <div id="divAmount" style="float:left;width:55%;">
                                            <p>
                                                <%-- 购买次数>= --%>
                                                <span>
                                                <label><s:text name="WAT03_maxCount"></s:text></label>
                                                <s:textfield name="maxCount" cssClass="text" maxlength="9"/>
                                                </span>
                                            </p>
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
                    <button class="right search" type="submit" onclick="BINOLMOWAT03.search();return false;">
                        <span class="ui-icon icon-search-big"></span>
                        <%-- 查询 --%>
                        <span class="button-text"><s:text name="WAT03_search"/></span>
                    </button>
                </p>
            </cherry:form>
        </div>
        <div id="section" class="section hide">
            <div class="section-header">
                <strong>
                    <span class="ui-icon icon-ttl-section-search-result"></span>
                    <%-- 查询结果一览 --%>
                    <s:text name="WAT03_results_list"/>
                </strong>
            </div>
            <div class="section-content">
                <div class="toolbar clearfix">
                <cherry:show domId="BINOLMOWAT03EXP">
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
                            <span class="button-text">
                                <%-- 设置列 --%>
                                <s:text name="WAT03_colSetting"/>
                            </span>
                        </a>
                    </span>
                </div>
                <table id="dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
                    <thead>
                    <tr>
                        <th><s:text name="WAT03_num"/></th><%-- No. --%>
                        <th><s:text name="WAT03_memberCode"/></th><%-- 会员卡号 --%>
                        <th><s:text name="WAT03_memberName"/></th><%-- 会员姓名  --%>
                        <th><s:text name="WAT03_count"/></th><%-- 购买次数 --%>
                        <th><s:text name="WAT03_sumQuantity"/></th><%-- 总数量 --%>
                        <th><s:text name="WAT03_sumAmount"/></th><%-- 总金额 --%>
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
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<script type="text/javascript" src="/Cherry/js/common/ajaxfileupload.js"></script>
<script type="text/javascript" src="/Cherry/js/bs/cnt/BINOLBSCNT10.js"></script>
<script type="text/javascript" src="/Cherry/js/mb/mbm/BINOLMBMBM10.js"></script>

<s:i18n name="i18n.bs.BINOLBSCNT10">
    <s:text name="global.page.select" id="select_default"/>
    <div class="clearfix">
        <span class="icon_crmnav"></span>
        <span class="breadcrumb left"> <span class="ui-icon icon-breadcrumb"></span><s:text name="binolbscnt10_counterManageHeader" />&nbsp;&gt;&nbsp;<s:text name="binolbscnt10_pointPlanDetailHeader"></s:text></span>
    </div>
    <div class="panel-content clearfix">

        <div class="section">

            <div class="section-content">

                <div class="box hide" id="pointConditionDiv">
                    <form id="pointDetailForm" class="inline" onsubmit="binolbscnt10.searchDetail();return false;" >
                        <s:hidden name="counterInfoId"></s:hidden>

                    </form>
                </div>

                <div class="ui-tabs">

                    <div class="clearfix">
                        <ul class ="ui-tabs-nav left" id="tabSelect">
                            <li id="0" class="ui-tabs-selected" onclick="binolbscnt10.changeTab(this);"><a><s:text name="binolbscnt10_counterInfoMode"/></a></li>
                            <li id="1" onclick="binolbscnt10.changeTab(this);"><a><s:text name="binolbscnt10_limitDetailMode"/></a></li>
                        </ul>
                    </div>
                    <a href="BINOLBSCNT10_counterPointPlanDetail_search?counterInfoId=${counterPointPlanInfo.BIN_CounterInfoID}" id="searchPointCounterDetail1" ></a>
                    <a href="BINOLBSCNT10_counterPointLimitDetail_search?counterInfoId=${counterPointPlanInfo.BIN_CounterInfoID}" id="searchCounterPointLimitDetail2" ></a>
                    <div class="ui-tabs-panel" id="tabsPanel">

                    </div>

                    <div class="hide">
                        <!-- 修改时间 -->
                        <div id="createTime"><s:text name="binolbscnt10_updateTime"></s:text></div>
                        <!-- 修改类型 -->
                        <div id="modifyType"><s:text name="binolbscnt10_modifyType"></s:text></div>
                        <!-- 修改者 -->
                        <div id="modifyer"><s:text name="binolbscnt10_modifyer"></s:text></div>
                        <!-- 开始日期 -->
                        <div id="startDate"><s:text name="binolbscnt10_startDate"></s:text></div>
                        <!-- 结束日期 -->
                        <div id="endDate"><s:text name="binolbscnt10_endDate"></s:text></div>
                        <!-- 备注 -->
                        <div id="comment"><s:text name="binolbscnt10_comment"></s:text></div>

                        <!-- 行号 -->
                        <div id="number"><s:text name="binolbscnt10_number"></s:text></div>
                        <!-- 业务类型 -->
                        <div id="tradeType"><s:text name="binolbscnt10_tradeType"></s:text></div>
                        <!-- 单据号 -->
                        <div id="billNo"><s:text name="binolbscnt10_billNo"></s:text></div>
                        <!-- 单据日期 -->
                        <div id="tradeTime"><s:text name="binolbscnt10_tradeTime"></s:text></div>
                        <!-- 金额 -->
                        <div id="totalAmount"><s:text name="binolbscnt10_totalAmount"></s:text></div>
                        <!-- 经销商额度 -->
                        <div id="limitAmount"><s:text name="binolbscnt10_limitAmount"></s:text></div>
                        <!-- 会员卡号 -->
                        <div id="memberCard"><s:text name="binolbscnt10_memberCard"></s:text></div>
                    </div>

                    <!-- 积分计划柜台履历tab -->
                    <div class="hide" id="tabsPanel1">
                        <div class="section-header">
                            <strong>
                                <span class="ui-icon icon-ttl-section-info"></span><s:text name="binolbscnt10_baseInfo" />
                            </strong>



                        </div>
                        <div class="section-content" id="pointDiv">

                            <table class="detail point-info" style="margin-bottom: 3px;">
                                <tr>
                                    <th><s:text name="binolbscnt10_counterCode" /></th>
                                    <td><span><s:property value="counterPointPlanInfo.CounterCode"/></span></td>
                                    <th><s:text name="binolbscnt10_counterName" /></th>
                                    <td><span><s:property value="counterPointPlanInfo.counterName"/></span></td>
                                </tr>
                                <tr>
                                    <th><s:text name="binolbscnt10_pointPlan" /></th>
                                    <td>
                                        <span>
                                            <s:if test=' currentDate >= counterPointPlanInfo.StartDate && currentDate < counterPointPlanInfo.EndDate  '>
                                                <s:text name="binolbscnt10_using"/>
                                            </s:if>
                                            <s:else>
                                                <s:text name="binolbscnt10_unused"/>
                                            </s:else>
                                        </span>
                                    </td>
                                    <th><s:text name="binolbscnt10_startDate" /></th>
                                    <td><span><s:property value="counterPointPlanInfo.StartDate"/></span></td>
                                </tr>
                                <tr>
                                    <th><s:text name="binolbscnt10_lastModifyTime" /></th>
                                    <td><span><s:property value="counterPointPlanInfo.updateTime"/></span></td>
                                    <th><s:text name="binolbscnt10_endDate" /></th>
                                    <td><span><s:property value="counterPointPlanInfo.EndDate"/></span></td>
                                </tr>
                            </table>
                        </div>

                        <div class="section-header">
                            <strong>
                                <span class="ui-icon icon-ttl-section-info"></span><s:text name="binolbscnt10_historyInfo" />
                            </strong>



                        </div>
                        <a href="BINOLBSCNT10_counterPointPlanDetail_search?counterInfoId=${counterPointPlanInfo.BIN_CounterInfoID}" id="searchPointCounterDetail" ></a>
                        <div id="table_div1">
                            <%--<table id="dataTable_pointPlanCounterDetail" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
                                <thead>
                                <tr>
                                    <!-- 修改时间 -->
                                    <th style="text-align: center"><s:text name="binolbscnt10_updateTime"></s:text></th>
                                    <!-- 修改类型 -->
                                    <th style="text-align: center"><s:text name="binolbscnt10_modifyType"></s:text></th>
                                    <!-- 修改者 -->
                                    <th style="text-align: center"><s:text name="binolbscnt10_modifyer"></s:text></th>
                                    <!-- 开始日期 -->
                                    <th style="text-align: center"><s:text name="binolbscnt10_startDate"></s:text></th>
                                    <!-- 结束日期 -->
                                    <th style="text-align: center"><s:text name="binolbscnt10_endDate"></s:text></th>
                                    <!-- 备注 -->
                                    <th style="text-align: center"><s:text name="binolbscnt10_comment"></s:text></th>
                                </tr>
                                </thead>
                            </table>--%>
                        </div>
                    </div>

                    <!-- 积分计划柜台履历tab -->
                    <div class="hide" id="tabsPanel2">



                        <div class="section-header">
                            <strong>
                                <span class="ui-icon icon-ttl-section-info"></span><s:text name="binolbscnt10_pointRecordInfo" />
                            </strong>
	                        <span class="right">
                            <s:url id="export" action="BINOLBSCNT10_export" ></s:url>
                            <a id="downUrl" href="${export}?counterInfoId=${counterPointPlanInfo.BIN_CounterInfoID}"></a>
                            <a id="export" class="export" onclick="binolbscnt10.exportExcel();return false;">
                                <span class="ui-icon icon-export"></span>
                                <span class="button-text"><s:text name="global.page.export"/></span>
                            </a>
                            </span>
                        </div>
                        <a href="BINOLBSCNT10_counterPointLimitDetail_search?counterInfoId=${counterPointPlanInfo.BIN_CounterInfoID}" id="searchCounterPointLimitDetail" ></a>
                        <div id="table_div2">
                            <%--<table id="dataTable_counterPointLimitDetail" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
                                <thead>
                                <tr>
                                    <!-- 行号 -->
                                    <th style="text-align: center"><s:text name="binolbscnt10_number"></s:text></th>
                                    <!-- 业务类型 -->
                                    <th style="text-align: center"><s:text name="binolbscnt10_tradeType"></s:text></th>
                                    <!-- 单据号 -->
                                    <th style="text-align: center"><s:text name="binolbscnt10_billNo"></s:text></th>
                                    <!-- 单据日期 -->
                                    <th style="text-align: center"><s:text name="binolbscnt10_tradeTime"></s:text></th>
                                    <!-- 金额 -->
                                    <th style="text-align: center"><s:text name="binolbscnt10_totalAmount"></s:text></th>
                                    <!-- 经销商额度 -->
                                    <th style="text-align: center"><s:text name="binolbscnt10_limitAmount"></s:text></th>
                                    <!-- 会员卡号 -->
                                    <th style="text-align: center"><s:text name="binolbscnt10_memberCard"></s:text></th>
                                </tr>
                                </thead>
                            </table>--%>
                        </div>
                    </div>

                </div>

            </div>
        </div>

    </div>

</s:i18n>

<div class="hide">
    <s:url action="" id="searchMemPoint_Url"></s:url>
    <a href="${searchMemPoint_Url}" id="searchMemPointUrl"></a>
    <s:url action="" id="searchPointInfoUrl"></s:url>
    <a href="${searchPointInfoUrl }" id="searchPointInfoUrl"></a>
    <s:url id="exportUrl" action="BINOLBSCNT10_export" ></s:url>
    <a id="exportUrl" href="${exportUrl}"></a>
</div>

<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>
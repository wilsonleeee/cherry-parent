<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<script type="text/javascript" src="/Cherry/js/common/ajaxfileupload.js"></script>
<script type="text/javascript" src="/Cherry/js/bs/cnt/BINOLBSCNT09.js"></script>
<s:i18n name="i18n.bs.BINOLBSCNT09">
    <%-- 积分模板下载URL --%>
    <s:url id="downLoad_url" value="/download/积分计划柜台导入模板.xls"/>
    <%-- 积分批量导入URL --%>
    <s:url id="import_url" value="BINOLBSCNT08_importPointPlanCounter"/>
    <s:text name="global.page.select" id="select_default"/>
    <div class="panel ui-corner-all">
        <div id="div_main">
            <div class="panel-header">
                <div class="clearfix">
                    <span class="breadcrumb left"> <span class="ui-icon icon-breadcrumb"></span><s:text name="binolbscnt09_importPoint" />&nbsp;&gt;&nbsp;<s:text name="binolbscnt09_importExcel_title"></s:text></span>
                </div>
            </div>
                <%-- ================== 信息提示区 START ======================= --%>
 <%--           <div id="actionDisplay">
                <div id="errorDiv" class="actionError" style="display:none;">
                    <ul>
                        <li><span id="errorSpan"></span></li>
                    </ul>
                </div>
                <div id="successDiv" class="actionSuccess" style="display:none;">
                    <ul class="actionMessage">
                        <li><span id="successSpan"></span></li>
                    </ul>
                </div>
            </div>--%>
            <div id="errorMessage"></div>
                <%-- ================== 信息提示区   END  ======================= --%>
            <div class="panel-content clearfix">
                <div class="box-yew">
                    <div class="box-yew-header clearfix"><strong class="left"><span class="ui-icon icon-help-star-yellow"></span><s:text name="binolbscnt09_importExplanation" /></strong> <a id="expandCondition" style="margin-left: 25px; font-size: 12px;width:80px" class="ui-select right">
                        <span class="button-text choice" style="min-width: 50px;"><s:text name="binolbscnt09_moreExplanation"/></span>
                        <span class="ui-icon ui-icon-triangle-1-n"></span>
                    </a></div>
                    <div class="box-yew-content">
                        <div class="step-content">
                            <label  style="margin:1px 0 0 0px;">1</label>
                            <div class="step">
                                <s:text name="binolbscnt09_explanation1"/>
                            </div>
                            <div class="line"></div>
                        </div>
                        <div id="moreCondition" style="display: none;">
                            <div class="step-content">
                                <label  style="margin:1px 0 0 0px;">2</label>
                                <div class="step">
                                    <s:text name="binolbscnt09_explanation2"/>
                                </div>
                                <div class="line"></div>
                            </div>
                            <div class="step-content">
                                <label  style="margin:1px 0 0 0px;">3</label>
                                <div class="step">
                                    <s:text name="binolbscnt09_explanation3"/>
                                </div>
                                <div class="line"></div>
                            </div>
                            <div class="step-content">
                                <label  style="margin:1px 0 0 0px;">4</label>
                                <div class="step">
                                    <s:text name="binolbscnt09_explanation4"/>
                                </div>
                                <div class="line"></div>
                            </div>
                            <div class="step-content">
                                <label  style="margin:1px 0 0 0px;">5</label>
                                <div class="step">
                                    <s:text name="binolbscnt09_explanation5"/>
                                </div>
                                <div class="line"></div>
                            </div>
                            <div class="step-content">
                                <label  style="margin:1px 0 0 0px;">6</label>
                                <div class="step">
                                    <s:text name="binolbscnt09_explanation6"/>
                                </div>
                                <div class="line"></div>
                            </div>
                        </div>
                    </div>
                </div>
                <form id="importForm" >
                    <div class="box2 box2-active">
                        <div class="box2-header clearfix"><strong class="active left"><span class="ui-icon icon-ttl-section-info"></span><s:text name="binolbscnt09_importExcel_con" /></strong></div>
                        <div class="box2-content clearfix">
                            <table class="detail" cellpadding="0" cellspacing="0">
                                <tr>
                                    <th><s:text name="binolbscnt09_reason" /><span class="highlight">*</span></th>
                                    <td style="word-wrap: break-word; word-break: break-all" colspan="3">
                                        <span style="width:75%">
                                            <input  id="comment" style="height:45px;width:100%;" class="text" maxlength="100"  name="comment">
                                        </span>
                                    </td>
                                </tr>
                            </table>
                        </div>
                    </div>
                </form>

                <div class="box2 box2-active">
                    <div class="box2-header clearfix"><strong class="active left"><img id="excel" src="/Cherry/css/common/blueprint/images/excel.png">  <s:text name="binolbscnt09_importBatch" /></strong></div>
                    <div class="box2-content clearfix">
                        <div style="overflow-x:auto;overflow-y:hidden" id="table_scroll">
                            <div class="box4-content">
                                <div style="margin-top: 0px;" class="relation clearfix">
							<span class="left" style="margin:5px 0 0 5px;"> <span class="highlight">※</span>
								<s:text name="binolbscnt09_notice" /> <a href="${downLoad_url }"><s:text name="binolbscnt09_download" /></a>
							</span>
							<span style="margin-left:10px; display: inline;" class="left hide">
							    <input class="input_text" type="text" id="pathExcel" name="pathExcel"/>
								<input type="button" value="<s:text name="global.page.browse"/>"/>
								<input class="input_file" type="file" name="upExcel" id="upExcel" size="33" onchange="pathExcel.value=this.value;BINOLBSCNT09.deleteActionMsg();return false;" />
								<input type="button" value="<s:text name="binolbscnt09_importBatch"/>" onclick="BINOLBSCNT09.ajaxFileUpload('${import_url}');" id="upload" />
								<img id="loading" src="/Cherry/css/cherry/img/loading.gif" height="15px" style="display: none;">
							</span>
                                </div>
                            </div>
                            <hr class="space">
                        </div>
                    </div>
                </div>

                <div id="errorCountersShow" style="display:none;">
                    <div id="section" class="section">
                        <div class="section-header">
                            <strong>
                                <span class="ui-icon icon-ttl-section-search-result"></span>
                                <s:text name="CNT09_warm1"/><span class="highlight" id="showBasError"><s:text name="CNT09_warm2"/></span><s:text name="CNT09_warm3"/>
                            </strong>
                        </div>
                        <div class="section-content" style="overflow-x:auto;width:100%;">
                            <table id="dataTable" cellpadding="0" cellspacing="0" border="0" style="width:100%;">
                                <thead>
                                <tr>
                                <tr>
                                        <%-- 编号--%>
                                    <th><s:text name="counter.number"/></th>
                                        <%-- 柜台编码 --%>
                                    <th><s:text name="counter.counterCode"/></th>
                                        <%-- 柜台名称 --%>
                                    <th><s:text name="counter.counterName"/></th>
                                        <%-- 开始时间 --%>
                                    <th><s:text name="counter.startDate"/></th>
                                        <%-- 结束时间 --%>
                                    <th><s:text name="counter.endDate"/></th>
                                        <%-- 错误原因 --%>
                                    <th><s:text name="counter.error"/></th>
                                </tr>
                                </thead>
                                <tbody id="errorCounters">
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
                <div id="hiddenTable" class="">
                </div>
                <div class="center clearfix" id="closeButton">
                    <button onclick="BINOLBSCNT09.close();return false;" type="button" class="close" id="close">
                        <span class="ui-icon icon-close"></span>
                        <span class="button-text"><s:text name="binolbscnt09_close"/></span>
                    </button>
                </div>
            </div>
        </div>
    </div>
    <%-- 错误信息 --%>
    <div id="errMsg">
        <input type="hidden" id="errmsg1" value='<s:text name="EMO00010"/>'/><%-- 请选择上传文件。 --%>
        <input type="hidden" id="errmsg2" value='<s:text name="binolbscnt09_selectPointType"/>'/><%-- 请选择积分类型。 --%>
        <input type="hidden" id="errmsg3" value='<s:text name="binolbscnt09_selectImportType"/>'/><%-- 请选择导入方式。 --%>
        <input type="hidden" id="errmsg4" value='<s:text name="binolbscnt09_reasonNotNull"/>'/><%-- 请选择导入方式。 --%>
        <input type="hidden" id="exportTip1" value='<s:text name="binolbscnt09_exportTip1"/>'/><%-- exportTip1 --%>
        <input type="hidden" id="exportTip2" value='<s:text name="binolbscnt09_exportTip2"/>'/><%-- exportTip2 --%>
    </div>
</s:i18n>
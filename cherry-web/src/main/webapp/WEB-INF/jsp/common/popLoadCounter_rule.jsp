<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<script type="text/javascript" src="/Cherry/js/common/ajaxfileupload.js"></script>
<script type="text/javascript" src="/Cherry/js/ss/prm/BINOLSSPRM88_counter.js"></script>
<div id="counterLoadDialog">
    <!-- 隐藏条件div-->
    <div class="hide">
        <form class="hidden" id="conditionForm">
            <!-- filterType 黑名单/白名单 -->
            <input type="hidden" id="filterType" name="filterType" value="<s:property value='%{filterType}'/>"/>
            <!-- operateType EXECL类型 -->
            <input type="hidden" id="searchCode" name="searchCode" value="<s:property value='%{searchCode}'/>"/>
            <input type="hidden" id="operateType" name="operateType" value="1"/>
            <input type="hidden" id="counterList" name="counterList" value="<s:property value='%{counterList}'/>">
            <input type="hidden" id="operateFlag" name="operateFlag" value="<s:property value='%{operateFlag}'/>">
            <!-- 柜台白名单导入结果 -->
            <input type="hidden" id="excelCounter_w" value="<s:property value='%{excelCounter_w}'/>"/>
            <!-- 柜台黑名单导入结果 -->
            <input type="hidden" id="excelCounter_b" value="<s:property value='%{excelCounter_b}'/>"/>
        </form>
    </div>
    <!-- 错误提示DIV -->
    <div id="errorDiv" class="actionError" style="display:none;">
        <ul>
            <li><span id="errorSpan"></span></li>
        </ul>
    </div>
    <div  style="color:red;">不建议导入超过300个柜台</div>
    <span>请使用标准模版</span>
    <a href="/Cherry/download/柜台信息模板.xls" style="color:blue;">模版下载</a>
    <s:select name="upMode" list="#application.CodeTable.getCodes('1398')"
                              listKey="CodeKey" listValue="Value" value="1" id="counterUpMode"></s:select>
    <input class="input_text" type="text" id="counterPathExcel" name="counterPathExcel"/>
    <input type="button" value="<s:text name="global.page.browse"/>"/>
    <input class="input_file" type="file" name="upExcel" id="counterUpExcel" size="33" onchange="counterPathExcel.value=this.value;return false;" />
    <input type="button" value="批量导入" onclick="PRM88_counter.counterExeclLoad();return false;"/>
    <img id="counterLoading" src="/Cherry/css/cherry/img/loading.gif" height="15px" style="display:none;">
    <div id="actionResultDisplay"></div>
    <div id="counterResult"></div>
</div>
<div class="section hide" id="counterFail">
    <div class="section-header">
        <strong><span class="icon icon-ttl-section"></span><s:text name="global.page.unUpLoad"></s:text></strong>&nbsp;
        <span>
             <a class="export" onclick="PRM88_counter.export();return false;">
                 <span class="ui-icon icon-export"></span>
                 <span class="button-text"><s:text name="global.page.export"/></span>
             </a>
        </span>
    </div>
    <div class="section-content">
        <table cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%" id="counterFailDataTable">
            <thead>
                <tr>
                    <th>品牌*</th>
                    <th>柜台号*</th>
                    <th>柜台名称</th>
                    <th>备注</th>
                </tr>
            </thead>
            <tbody>
            </tbody>
        </table>
    </div>
</div>
<div class="dialog2 clearfix" style="display:none" id="check_coupon_dialog">
    <p class="clearfix message">
        <span></span>
        <img height="15px" class="hide" src="/Cherry/css/cherry/img/loading.gif"/>
    </p>
</div>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%--<s:url action="BINOLSSPRM73_counterExeclLoad" namespace="/ss" id="counterExeclLoadUrl" />
<a id ="counterExeclLoad" style="display:none" href="${counterExeclLoadUrl}"/>--%>
<s:url action="BINOLSSPRM88_counterFailSearch" namespace="/ss" id="counterFailSearchUrl" />
<a id ="counterFailSearch" style="display:none" href="${counterFailSearchUrl}"/>
<!-- execl导出URL -->
<s:url action="BINOLSSPRM88_export" namespace="/ss" id="exportExeclUrl" />
<a id ="exportExecl" style="display:none" href="${exportExeclUrl}"/>

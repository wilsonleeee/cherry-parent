<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<script type="text/javascript" src="/Cherry/js/common/ajaxfileupload.js"></script>
<script type="text/javascript" src="/Cherry/js/ss/prm/BINOLSSPRM88_productBlack.js"></script>
<div id="proBlackLoadDialog">
    <!-- 隐藏条件div-->
    <div class="hide">
        <form class="hidden" id="conditionForm">
            <!-- filterType 黑名单/白名单 -->
            <input type="hidden" id="filterType" name="filterType" value="<s:property value='%{filterType}'/>"/>
            <!-- operateType EXECL类型 -->
            <input type="hidden" id="operateType" name="operateType" value="2"/>
            <input type="hidden" id="operateFlag" name="operateFlag" value="<s:property value='%{operateFlag}'/>">
            <!-- 产品黑名单导入结果 -->
            <input type="hidden" id="excelProductExcept" value=""/>
        </form>
    </div>
    <!-- 错误提示DIV -->
    <div id="errorDiv" class="actionError" style="display:none;">
        <ul>
            <li><span id="errorSpan"></span></li>
        </ul>
    </div>
    <span>请使用标准模版</span>
    <a href="/Cherry/download/产品黑名单模版.xls" style="color:blue;">模版下载</a>
    <s:select name="upMode" list="#application.CodeTable.getCodes('1398')"
                              listKey="CodeKey" listValue="Value" value="1" id="counterUpMode"></s:select>
    <input class="input_text" type="text" id="proBlackPathExcel" name="proBlackPathExcel"/>
    <input type="button" value="<s:text name="global.page.browse"/>"/>
    <input class="input_file" type="file" name="upExcel" id="proBlackUpExcel" size="33" onchange="proBlackPathExcel.value=this.value;return false;" />
    <input type="button" value="批量导入" onclick="PRM88_proBlack.proBlackExeclLoad();return false;"/>
    <img id="proBlackLoading" src="/Cherry/css/cherry/img/loading.gif" height="15px" style="display:none;">
    <div id="actionResultDisplay"></div>
    <div id="proBlackResult"></div>
</div>
<div class="section hide" id="proBlackFail">
    <div class="section-header">
        <strong><span class="icon icon-ttl-section"></span><s:text name="global.page.unUpLoad"></s:text></strong>&nbsp;
        <span>
             <a class="export" onclick="PRM88_proBlack.export();return false;">
                 <span class="ui-icon icon-export"></span>
                 <span class="button-text"><s:text name="global.page.export"/></span>
             </a>
        </span>
    </div>
    <div class="section-content">
        <table cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%" id="proBlackFailDataTable">
            <thead>
                <tr>
                    <th>品牌*</th>
                    <th>产品编码*</th>
                    <th>产品条码</th>
                    <th>产品名称</th>
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
<s:url action="BINOLSSPRM88_proBlackFailSearch" namespace="/ss" id="proBlackFailSearchUrl" />
<a id ="proBlackFailSearch" style="display:none" href="${proBlackFailSearchUrl}"/>
<!-- execl导出URL -->
<s:url action="BINOLSSPRM88_export" namespace="/ss" id="exportExeclUrl" />
<a id ="exportExecl" style="display:none" href="${exportExeclUrl}"/>

<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<link rel="stylesheet" href="/Cherry/css/cherry/combobox.css" type="text/css">
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/common/popDataTable.js"></script>
<script type="text/javascript" src="/Cherry/js/mb/mbm/BINOLMBMBM31.js"></script>
<s:i18n name="i18n.mb.BINOLMBMBM31">
    <s:url id="search_url" value="/mb/BINOLMBMBM31_search"/>
    <s:url id="addRuleUrl" value="/mb/BINOLMBMBM31_add"/>
    <s:url id="deleteRuleUrl" value="/mb/BINOLMBMBM31_deleteRule"/>
    <div id="div_main">
    <div class="hide">
        <a id="searchUrl" href="${search_url}"></a>
        <a id="addRuleUrl" href="${addRuleUrl}"></a>
        <a id="deleteRuleUrl" href="${deleteRuleUrl}"></a>
        <div id="addRuleTitle"><s:text name="MBM31_meminfoCompleteRule"/></div>
        <div id="delConfirmMessage"><p class="message"><span><s:text name="MBM31_deleteConfirm"/></span></p></div>
        <div id="deleteRuleTitle"><s:text name="MBM31_deleteRuleTitle"/></div>
        <div id="dialogConfirm"><s:text name="MBM31_dialogConfirm"/></div>
        <div id="dialogCancel"><s:text name="MBM31_dialogCancel"/></div>
        <div id="popRuleDetailTable"></div>
        <div id="popRuleAddTable"></div>
        <div id="popRuleEditTable"></div>
        <div id="popRuleDeleteTable"></div>
    </div>
    <div class="panel-header">
        <div class="clearfix">
		<span class="breadcrumb left">
			<jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
		</span>
                <%-- 添加按钮 --%>
		<span class="right">
            <s:url action="BINOLMBMBM31_addInit" id="addRuleUrl"></s:url>
            <a class="add" href="${addRuleUrl }" onclick="BINOLMBMBM31.addRuleInfo(this);return false;"><span class="ui-icon icon-add"></span><span class="button-text"><s:text name="MBM31_addRuleButton"></s:text></span></a>
        </a>
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
    <div id="actionResultDisplay"></div>
    <div class="panel-content">
        <div class="box">
            <cherry:form id="mainForm" class="inline">
                <input type="hidden" id="currentUnitID" name="currentUnitID" value="BINOLMOCIO15"/>
                <div class="box-header">
                    <strong>
                        <span class="ui-icon icon-ttl-search"></span>
                            <%-- 查询条件 --%>
                        <s:text name="MBM31_condition"/>
                    </strong>
                </div>
                <div class="box-content clearfix">
                    <div class="column" style="width:49%;">
                        <p>
                        <p><%-- 名称 --%>
                            <label  style="width: 120px;"><s:text name="MBM31_informationRuleName" /></label>
                            <s:textfield name="ruleName" cssClass="text" maxlength="20"/>
                        </p>
                        </p>
                    </div>
                    <div class="column last" style="width:50%;">
                        <p><%-- 有效时间 --%>
                            <label><s:text name="MBM31_effectiveTime" /></label>
                            <span><s:textfield id="startTime" name="startTime" cssClass="date"/></span>
                            -
                            <span><s:textfield id="endTime" name="endTime" cssClass="date"/></span>
                        </p>
                    </div>
                </div>
                <p class="clearfix">
                        <%-- 查询 --%>
                    <button class="right search" onclick="BINOLMBMBM31.search();return false;">
                        <span class="ui-icon icon-search-big"></span>
                        <span class="button-text"><s:text name="MBM31_search"/></span>
                    </button>
                </p>
            </cherry:form>
        </div>
            <%-- ====================== 结果一览开始 ====================== --%>
        <div id="section" class="section hide">
            <div class="section-header">
                <strong>
                    <span class="ui-icon icon-ttl-section-search-result"></span>
                    <s:text name="global.page.list"/>
                </strong>
            </div>
            <div class="section-content">
                <table id="dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%" >
                    <thead>
                    <tr>
                        <th><s:text name="MBM31_no"/></th>
                        <th><s:text name="MBM31_ruleName"/></th><%-- 规则名称 --%>
                        <th><s:text name="MBM31_startDate"/></th><%-- 开始时间 --%>
                        <th><s:text name="MBM31_endDate"/></th><%-- 结束名称 --%>
                        <th><s:text name="MBM31_operate"/></th><!-- 操作 -->
                        <th><s:text name="MBM31_memo"/></th><%-- 备注 --%>
                    </tr>
                    </thead>
                </table>
            </div>
        </div>
    </div>
    <%-- ====================== 结果一览结束 ====================== --%>
    </div>
    <%-- ====================== 编辑竞争对手弹出框结束 ====================== --%>
    <div class="hide" id="dialogInit"></div>
    <%--错误信息提示 --%>
    <div id="errmessage" style="display:none">
        <div id="messageDialogDiv" class="hide ui-dialog-content ui-widget-content" style="display: none; width: auto; min-height: 200px;">
            <p id="messageContent" class="message hide" style="margin:40px auto 30px;"><span id="messageContentSpan"></span></p>
            <p id="successContent" class="success hide" style="margin:40px auto 30px;"><span id="successContentSpan"></span></p>
            <p class="center">
                <button id="btnMessageConfirm" class="close" type="button">
                    <span class="ui-icon icon-confirm"></span>
                    <span class="button-text">确定</span>
                </button>
            </p>
        </div>
    </div>
</s:i18n>
<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>
<div id="errmessage" style="display:none">
    <input type="hidden" id="errmsg1" value='<s:text name="EMO00031"/>'/>
</div>
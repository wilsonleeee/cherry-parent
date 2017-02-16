<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<script type="text/javascript" src="/Cherry/js/common/ajaxfileupload.js"></script>
<script type="text/javascript" src="/Cherry/js/ss/prm/BINOLSSPRM88_load.js"></script>
<div id="memberLoadDialog" class="loadDialog">
    <!-- 隐藏条件div-->
    <div class="hide">
        <form class="hidden" id="conditionForm">
            <!-- filterType 黑名单/白名单 -->
            <input type="hidden" id="filterType" name="filterType" value="<s:property value='%{filterType}'/>"/>
            <!-- operateType EXECL类型 -->
            <input type="hidden" id="operateType" name="operateType" value="3"/>
            <input type="hidden" id="searchCode" name="searchCode" value="<s:property value='%{searchCode}'/>"/>
        </form>
    </div>
    <!-- 错误提示DIV -->
    <div id="errorDiv" class="actionError" style="display:none;">
        <ul>
            <li><span id="errorSpan"></span></li>
        </ul>
    </div>
    <span>请使用标准模版</span>
    <a style="color:blue;" href="/Cherry/download/活动对象导入模版.xls">模版下载</a>
    <s:select name="upMode" list="#application.CodeTable.getCodes('1398')"
                              listKey="CodeKey" listValue="Value" value="1" id="counterUpMode"></s:select>
    <input class="input_text" type="text" id="memberPathExcel" name="memberPathExcel"/>
    <input type="button" value="<s:text name="global.page.browse"/>"/>
    <input class="input_file" type="file" name="upExcel" id="memberUpExcel" size="33" onchange="memberPathExcel.value=this.value;return false;" />
    <input type="button" value="批量导入" onclick="binolssprm88_load.memberExeclLoad();return false;"/>
    <s:if test="filterType==2">
        <input type="button" value="会员清空" onclick="binolssprm88_load.cleanSearchCode(this);return false;"/>
    </s:if>
    <img id="memberLoading" src="/Cherry/css/cherry/img/loading.gif" height="15px" style="display:none;">
    <div id="actionResultDisplay"></div>
</div>
<div class="section hide" id="memberFail">
    <div class="section-header">
        <strong><span class="icon icon-ttl-section"></span><s:text name="global.page.unUpLoad"></s:text></strong>&nbsp;
        <span>
             <a id="export" class="export" onclick="binolssprm88_load.export();return false;">
                 <span class="ui-icon icon-export"></span>
                 <span class="button-text"><s:text name="global.page.export"/></span>
             </a>
        </span>
    </div>
    <div class="section-content">
        <table cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%" id="memberFailDataTable">
            <thead>
                <tr>
                    <th>品牌*</th>
                    <th>会员卡号</th>
                    <th>会员手机号*</th>
                    <th>BP号</th>
                    <th>会员等级</th>
                    <th>会员姓名</th>
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
<s:url action="BINOLSSPRM88_memberExeclLoadAct" namespace="/ss" id="memberExeclLoadUrl" />
<a id ="memberExeclLoad" style="display:none" href="${memberExeclLoadUrl}"/>
<s:url action="BINOLSSPRM88_getFailUploadMemberList" namespace="/ss" id="memberFailSearchUrl" />
<a id ="memberFailSearch" style="display:none" href="${memberFailSearchUrl}"/>
<s:url action="BINOLSSPRM88_exportMemberExecl" namespace="/ss" id="exportExeclUrl" />
<a id ="exportExecl" style="display:none" href="${exportExeclUrl}"/>

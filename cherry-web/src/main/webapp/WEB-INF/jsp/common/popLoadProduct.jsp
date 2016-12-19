<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<script type="text/javascript" src="/Cherry/js/common/ajaxfileupload.js"></script>
<script type="text/javascript" src="/Cherry/js/ss/prm/BINOLSSPRM73_load.js"></script>
<div id="productLoadDialog">
    <!-- 隐藏条件div-->
    <div class="hide">
        <form class="hidden" id="conditionForm">
            <!-- conditionType 使用门槛/发送门槛 -->
            <input type="hidden" id="conditionType" name="failUploadDataDTO.conditionType" value="<s:property value='%{conditionType}'/>"/>
            <!-- filterType 黑名单/白名单 -->
            <input type="hidden" id="filterType" name="failUploadDataDTO.filterType" value="<s:property value='%{filterType}'/>"/>
            <!-- contentNo 子券No -->
            <input type="hidden" id="contentNo" name="failUploadDataDTO.contentNo" value="<s:property value='%{contentNo}'/>"/>
            <!-- operateType EXECL类型 -->
            <input type="hidden" id="operateType" name="failUploadDataDTO.operateType" value="2"/>
        </form>
    </div>
    <!-- 错误提示DIV -->
    <div id="errorDiv" class="actionError" style="display:none;">
        <ul>
            <li><span id="errorSpan"></span></li>
        </ul>
    </div>
    <span>请使用标准模版</span>
    <s:if test="filterType==2">
        <a style="color:blue;" href="/Cherry/download/电子券产品导入模版.xls">模版下载</a>
    </s:if>
    <s:elseif test="filterType==1">
        <a style="color:blue;" href="/Cherry/download/电子券产品导入模版(白名单).xls">模版下载</a>
    </s:elseif>
    <s:select name="upMode" list="#application.CodeTable.getCodes('1398')"
                              listKey="CodeKey" listValue="Value" value="1" id="productUpMode"></s:select>
    <input class="input_text" type="text" id="productPathExcel" name="productPathExcel"/>
    <input type="button" value="<s:text name="global.page.browse"/>"/>
    <input class="input_file" type="file" name="upExcel" id="productUpExcel" size="33" onchange="productPathExcel.value=this.value;return false;" />
    <input type="button" value="批量导入" onclick="binolssprm73_load.productExeclLoad();return false;"/>
    <img id="productLoading" src="/Cherry/css/cherry/img/loading.gif" height="15px" style="display:none;">
    <div id="actionResultDisplay"></div>
</div>
<s:if test="filterType==2">
    <div class="section hide" id="productFail">
        <div class="section-header">
            <strong><span class="icon icon-ttl-section"></span><s:text name="global.page.unUpLoad"></s:text></strong>&nbsp;
        <span>
             <a class="export" onclick="binolssprm73_load.export();return false;">
                 <span class="ui-icon icon-export"></span>
                 <span class="button-text"><s:text name="global.page.export"/></span>
             </a>
        </span>
        </div>
        <div class="section-content">
            <table cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%" id="productFailDataTable">
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
</s:if>
<s:elseif test="filterType==1">
    <div class="section hide" id="productFail">
        <div class="section-header">
            <strong><span class="icon icon-ttl-section"></span><s:text name="global.page.unUpLoad"></s:text></strong>&nbsp;
        <span>
             <a class="export" onclick="binolssprm73_load.export();return false;">
                 <span class="ui-icon icon-export"></span>
                 <span class="button-text"><s:text name="global.page.export"/></span>
             </a>
        </span>
        </div>
        <div class="section-content">
            <table cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%" id="productFailDataTable">
                <thead>
                <tr>
                    <th>品牌*</th>
                    <th>产品编码*</th>
                    <th>产品条码</th>
                    <th>产品名称</th>
                    <th>产品数量*</th>
                    <th>备注</th>
                </tr>
                </thead>
                <tbody>
                </tbody>
            </table>
        </div>
    </div>
</s:elseif>
<div class="dialog2 clearfix" style="display:none" id="check_product_dialog">
    <p class="clearfix message">
        <span></span>
        <img height="15px" class="hide" src="/Cherry/css/cherry/img/loading.gif"/>
    </p>
</div>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<s:url action="BINOLSSPRM73_productExeclLoad" namespace="/ss" id="productExeclLoadUrl" />
<a id ="productExeclLoad" style="display:none" href="${productExeclLoadUrl}"/>
<s:url action="BINOLSSPRM73_productFailSearch" namespace="/ss" id="productFailSearchUrl" />
<a id ="productFailSearch" style="display:none" href="${productFailSearchUrl}"/>

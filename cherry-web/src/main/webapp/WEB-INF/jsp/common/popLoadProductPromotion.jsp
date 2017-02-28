<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<script type="text/javascript" src="/Cherry/js/common/ajaxfileupload.js"></script>
<script type="text/javascript" src="/Cherry/js/ss/prm/BINOLSSPRM68_load.js"></script>
<div id="productLoadDialog">
    <!-- 隐藏条件div-->
    <div class="hide">
        <form class="hidden" id="conditionForm">
            <input type="hidden" id="execLoadType" name="execLoadType" value="<s:property value='%{execLoadType}'/>"/>
            <input type="hidden" id="searchCode" name="searchCode" value="<s:property value='%{searchCode}'/>"/>
            <!-- 产品购物车导入结果 -->
            <input type="hidden" id="excelProductShopping" name="excelProductShopping" value="<s:property value='%{excelProductShopping}'/>"/>
            <!-- 产品奖励导入结果 -->
            <input type="hidden" id="excelProductAward" name="excelProductAward" value="<s:property value='%{excelProductAward}'/>"/>
            <!-- 原产品界面该产品类型的总共数量 -->
            <input type="hidden" id="productPageSize" name="productPageSize" value="<s:property value='%{productPageSize}'/>"/>
        </form>
    </div>
    <!-- 错误提示DIV -->
    <div id="errorDiv" class="actionError" style="display:none;">
        <ul>
            <li><span id="errorSpan"></span></li>
        </ul>
    </div>
    <span>请使用标准模版</span>
    <s:if test="execLoadType=='shoppingCart'">
        <a style="color:blue;" href="/Cherry/download/非整单条件产品模板.xls">模版下载</a>
    </s:if>
    <s:elseif test="execLoadType=='GIFT'">
        <a style="color:blue;" href="/Cherry/download/赠送礼品产品模板.xls">模版下载</a>
    </s:elseif>
    <s:elseif test="execLoadType=='DPZK'">
        <a style="color:blue;" href="/Cherry/download/单品折扣产品导入模板.xls">模版下载</a>
    </s:elseif>
    <s:elseif test="execLoadType=='DPTJ'">
        <a style="color:blue;" href="/Cherry/download/单品特价导入模板.xls">模版下载</a>
    </s:elseif>
    <s:select name="upMode" list="#application.CodeTable.getCodes('1398')"
              listKey="CodeKey" listValue="Value" value="1" id="productUpMode"></s:select>
    <input class="input_text" type="text" id="productPathExcel" name="productPathExcel"/>
    <input type="button" value="<s:text name="global.page.browse"/>"/>
    <input class="input_file" type="file" name="upExcel" id="productUpExcel" size="33" onchange="productPathExcel.value=this.value;return false;" />
    <input type="button" value="批量导入" onclick="binolssprm68_load.productExeclLoad();return false;"/>
    <img id="memberLoading" src="/Cherry/css/cherry/img/loading.gif" height="15px" style="display:none;">
    <div id="actionResultDisplay"></div>
</div>


<div class="section hide" id="productFail">
    <div class="section-header">
        <strong><span class="icon icon-ttl-section"></span><s:text name="global.page.unUpLoad"></s:text></strong>&nbsp;
    <span>
         <a id="export" class="export" onclick="binolssprm68_load.export();return false;">
             <span class="ui-icon icon-export"></span>
             <span class="button-text"><s:text name="global.page.export"/></span>
         </a>
    </span>
    </div>
    <div class="section-content">
        <table cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%" id="productFailDataTable">
            <thead>
            <s:if test="execLoadType=='shoppingCart'">
                <tr>
                    <th>厂商编码*</th>
                    <th>产品条码*</th>
                    <th>产品名称</th>
                    <th>数量金额条件*</th>
                    <th>比较条件*</th>
                    <th>比较值*</th>
                    <th>备注</th>
                </tr>
            </s:if>
            <s:elseif test="execLoadType=='GIFT'">
                <tr>
                    <th>厂商编码*</th>
                    <th>产品条码*</th>
                    <th>产品名称</th>
                    <th>数量*</th>
                    <th>备注</th>
                </tr>
            </s:elseif>
            <s:elseif test="execLoadType=='DPZK'">
                <tr>
                    <th>厂商编码*</th>
                    <th>产品条码*</th>
                    <th>产品名称</th>
                    <th>折扣数量大于等于*</th>
                    <th>折扣数量小于等于*</th>
                    <th>折扣</th>
                    <th>备注</th>
                </tr>
            </s:elseif>
            <s:elseif test="execLoadType=='DPTJ'">
                <tr>
                    <th>厂商编码*</th>
                    <th>产品条码*</th>
                    <th>产品名称</th>
                    <th>特价(元)*</th>
                    <th>备注</th>
                </tr>
            </s:elseif>
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

<s:url action="BINOLSSPRM68_productExeclLoadPromotion" namespace="/ss" id="productExeclLoadUrl" />
<a id ="productExeclLoad" style="display:none" href="${productExeclLoadUrl}"/>
<s:url action="BINOLSSPRM68_getFailUploadProductList" namespace="/ss" id="productFailSearchUrl" />
<a id ="productFailSearch" style="display:none" href="${productFailSearchUrl}"/>
<s:url action="BINOLSSPRM68_export" namespace="/ss" id="exportExeclUrl" />
<a id ="exportExecl" style="display:none" href="${exportExeclUrl}"/>


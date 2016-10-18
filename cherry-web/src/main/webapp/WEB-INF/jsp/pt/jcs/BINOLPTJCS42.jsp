<%-- 产品信息二维码维护 --%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<link rel="stylesheet" href="/Cherry/css/cherry/combobox.css" type="text/css">
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/common/popDataTable.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/common/departBar.js"></script>
<script type="text/javascript" src="/Cherry/js/pt/jcs/BINOLPTJCS42.js"></script>
<s:i18n name="i18n.pt.BINOLPTJCS42">
    <div class="hide">
        <s:url id="search_url" value="/pt/BINOLPTJCS42_search"/>
        <a id="searchUrl" href="${search_url}"></a>
        <s:url id="export" action="BINOLPTJCS42_export" ></s:url>
        <a id="downUrl" href="${export}"></a>
        <s:url id="reGenerate" action="BINOLPTJCS42_reGenerate" ></s:url>
        <a id="reGenerateUrl" href="${reGenerate}"></a>
        <s:url id="getResellerList" action="BINOLPTJCS42_getResellerList" ></s:url>
        <a id="getResellerListUrl" href="${getResellerList}"></a>
        <s:text name="JCS42_selectAll" id="JCS42_selectAll"/>
    </div>
    <div class="panel-header">
        <%-- 产品信息二维码维护--%>
        <div class="clearfix">
	    <span class="breadcrumb left">	    
			<jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
		</span>
        <cherry:show domId="BINOLPTJCS4201">
            <a class="add right" id="resetAllQRCode" onclick="BINOLPTJCS42.reGenerate();return false;">
                <span class="ui-icon icon-add"></span><span class="button-text"><s:text name="JCS42_resetALLQRCode"/></span>
            </a>
        </cherry:show>
        </div>
    </div>
    <div id="actionResultDisplay"></div>
    <div id="errorMessage"></div>
    <%-- ================== 错误信息提示 START ======================= --%>
        <div id="errorDiv2" style="display:none">
	        <div class="actionError">
	            <ul>
	                <li><span id="errorSpan2"></span></li>
	            </ul>
	        </div>
        </div>
    <%-- ================== 错误信息提示   END  ======================= --%>
    <div class="panel-content">
        <div class="box">
            <cherry:form id="mainForm" class="inline" onsubmit="BINOLPTJCS42.search(); return false;">
                <div class="box-header">
                    <strong>
                        <span class="ui-icon icon-ttl-search"></span>
                        <%-- 查询条件 --%>
                        <s:text name="JCS42_condition"/>
                    </strong>
                    <input type="checkbox" name="validFlag" id="validFlag" value="1"/><s:text name="JCS42_includeDisabled"/>
                </div>
                <div class="box-content clearfix">
                    <div class="column" style="width:50%;">
                        <p>
                        <%-- 所属品牌 --%>
                            <s:if test='%{brandInfoList != null && !brandInfoList.isEmpty()}'>
                                <label><s:text name="JCS42_brandInfo"></s:text></label>
                                <s:text name="JCS42_select" id="JCS42_select"/>
                                <s:select id="brandInfoId" name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" headerKey="" cssStyle="width:100px;"></s:select>
                            </s:if>
                        </p>
                        <p>
                        <%-- 产品--%>
                            <label><s:text name="JCS42_productName"/></label>
                            <s:textfield name="productName" cssClass="text" maxlength="30"/>
                            <input type="hidden" id="prtVendorId" name="prtVendorId" value="" />
                        </p>
                    </div>
                    <div class="column last" style="width:49%;">
                        <p>
                        <%-- 经销商--%>
                            <label><s:text name="JCS42_resellerName"/></label>
                            <s:textfield name="resellerName" cssClass="text" maxlength="30"/>
                            <input type="hidden" id="resellerCode" name="resellerCode" value="" />
                        </p>
                    </div>
                </div>
                <p class="clearfix">
                    <button class="right search" type="submit" onclick="BINOLPTJCS42.search();return false;">
                        <span class="ui-icon icon-search-big"></span>
                        <%-- 查询 --%>
                        <span class="button-text"><s:text name="JCS42_search"/></span>
                    </button>
                </p>
            </cherry:form>
        </div>
        <div id="section" class="section hide">
            <div class="section-header">
                <strong>
                    <span class="ui-icon icon-ttl-section-search-result"></span>
                    <%-- 查询结果一览 --%>
                    <s:text name="JCS42_results_list"/>
                </strong>
            </div>
            <div class="section-content">
                <div class="toolbar clearfix">
                 <cherry:show domId="BINOLPTJCS42EXP">
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
                                <s:text name="JCS42_colSetting"/>
                            </span>
                        </a>
                    </span>
                </div>
                <table id="dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
                    <thead>
                    <tr>
                        <th><s:text name="JCS42_num"/></th><%-- No. --%>
                        <th><s:text name="JCS42_productName"/></th><%-- 产品名称  --%>
                        <th><s:text name="JCS42_unitCode"/></th><%-- 厂商编码  --%>
                        <th><s:text name="JCS42_barCode"/></th><%-- 产品条码  --%>
                        <th><s:text name="JCS42_resellerName"/></th><%-- 经销商  --%>
                        <th><s:text name="JCS42_qrCodeCiphertext"/></th><%-- 二维码 --%>
                        <th><s:text name="JCS42_wholeURL"/></th><%-- 完整URL --%>
                        <th><s:text name="JCS42_validFlag"/></th><%-- 有效区分 --%>
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
<%-- ================== 生成URL弹出窗口 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/pt/jcs/BINOLPTJCS42_2.jsp" flush="true" />
<%-- ==================  生成URL弹出窗口End ======================== --%>
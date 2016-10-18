<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%-- 产品收货一览 --%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<link rel="stylesheet" href="/Cherry/css/cherry/combobox.css" type="text/css">
<script type="text/javascript" src="/Cherry/js/common/departBar.js"></script>
<script type="text/javascript" src="/Cherry/js/st/sfh/BINOLSTSFH11.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<s:i18n name="i18n.st.BINOLSTSFH11">
    <div class="hide">
        <s:url id="search_url" value="/st/BINOLSTSFH11_search"/>
        <s:text name="SFH11_selectAll" id="SFH11_selectAll"/>
        <a id="searchUrl" href="${search_url}"></a>
        <s:text name="SFH11_select" id="defVal"/>
        <div id="SFH11_select">${defVal}</div>
    </div>
    <div class="panel-header">
        <%-- 产品收货查询 --%>
        <div class="clearfix">
        <span class="breadcrumb left">
            <jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
        </span>
        </div>
    </div>
    <div id="errorMessage"></div>
    <div id="actionResultDisplay"></div>
    <div class="panel-content">
        <div class="box">
            <cherry:form id="mainForm" class="inline" onsubmit="BINOLSTSFH11.search(); return false;">
                <input type="hidden" id="currentUnitID" name="currentUnitID" value="BINOLSTSFH11"/>
                <div class="box-header">
                    <strong>
                        <span class="ui-icon icon-ttl-search"></span>
                        <%-- 查询条件 --%>
                        <s:text name="SFH11_condition"/>
                    </strong>
                </div>
                <div class="box-content clearfix">
                    <div class="column" style="width:50%;height:65px;">
                    <p>
                        <%-- 发货单号 --%>
                        <label><s:text name="SFH11_deliverNo"/></label>
                        <s:textfield name="deliverNo" cssClass="text" maxlength="40" onblur="ignoreCondition(this);return false;"/>
                    </p>
                    <p>
                        <%-- 产品名称 --%>
                        <label><s:text name="SFH11_productName"/></label>
                        <s:textfield name="nameTotal" cssClass="text" maxlength="100"/>
                        <input type="hidden" id="prtVendorId" name="prtVendorId" value="" />
                    </p>
                    </div>
                    <div class="column last" style="width:49%;">
                    <p id="dateCover" class="date">
                        <%-- 发货日期 --%>
                        <label><s:text name="SFH11_deliverDate"/></label>
                        <span><s:textfield name="startDate" cssClass="date"/></span> - <span><s:textfield name="endDate" cssClass="date"/></span>
                    </p>
                    </div>
                    <%-- ======================= 组织联动共通导入开始  ============================= --%>
                    <s:action name="BINOLCM13_query" namespace="/common" executeResult="true">
                        <s:param name="businessType">1</s:param>
                        <s:param name="operationType">1</s:param>
                        <s:param name="mode">dpat,area</s:param>
                    </s:action>
                    <%-- ======================= 组织联动共通导入结束  ============================= --%>
                </div>
                <p class="clearfix">
                    <button class="right search" type="submit" onclick="BINOLSTSFH11.search();return false">
                        <span class="ui-icon icon-search-big"></span>
                        <%-- 查询 --%>
                        <span class="button-text"><s:text name="SFH11_search"/></span>
                    </button>
                </p>
            </cherry:form>
        </div>
        <div id="section" class="section hide">
            <div class="section-header">
                <strong>
                    <span class="ui-icon icon-ttl-section-search-result"></span>
                    <%-- 查询结果一览 --%>
                    <s:text name="SFH11_results_list"/>
                </strong>
            </div>
            <div class="section-content" id="result_list">
                <div class="toolbar clearfix">
                <span id="headInfo" style=""></span>
                <span class="right"><a class="setting"><span class="ui-icon icon-setting"></span>
                <span class="button-text">
                <%-- 设置列 --%>
                <s:text name="SFH11_colSetting"/>
                </span></a>
                </span>
                </div>
            </div>
        </div>
    </div>
    <div id="result_table" class="hide">
        <table cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
            <thead>
                <tr>
                    <th><s:text name="SFH11_num"/></th><%-- No. --%>
                    <th><s:text name="SFH11_deliverNo"/><span class="ui-icon ui-icon-document"></span></th><%-- 发货单号 --%>
                    <th><s:text name="SFH11_departNameDeliver"/></th><%-- 发货部门  --%>
                    <th><s:text name="SFH11_departNameReceive"/></th><%-- 收货部门 --%>
                    <th><s:text name="SFH11_totalQuantity"/></th><%-- 总数量 --%>
                    <th><s:text name="SFH11_totalAmount"/></th><%-- 总金额 --%>
                    <th><s:text name="SFH11_deliverDate"/></th><%-- 发货日期 --%>
                </tr>
            </thead>
        </table>
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
<input type="hidden"  id="defStartDate" value=''/>
<input type="hidden"  id="defEndDate"   value=''/>
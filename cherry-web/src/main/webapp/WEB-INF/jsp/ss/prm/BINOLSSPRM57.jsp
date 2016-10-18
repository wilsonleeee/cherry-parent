<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<link rel="stylesheet" href="/Cherry/css/cherry/combobox.css" type="text/css">
<script type="text/javascript" src="/Cherry/js/common/departBar.js"></script>
<script type="text/javascript" src="/Cherry/js/ss/prm/BINOLSSPRM57.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<s:i18n name="i18n.ss.BINOLSSPRM57">
    <s:url id="getDepartUrl" value="/common/BINOLCM00_queryDepart"/>
    <s:url id="getInventUrl" value="/common/BINOLCM00_queryInventory"/>
    <s:url id="search_url" value="/ss/BINOLSSPRM57_search"/>
    <s:url id="delete_url" action="BINOLSSPRM57_delete"/>
    <s:url id="send_url" action="BINOLSSPRM57_send"/>
    <s:text name="PRM57_selectAll" id="PRM57_selectAll"/>
    <div class="hide">
        <a id="searchUrl" href="${search_url}"></a>
        <s:text name="PRM57_select" id="defVal"/>
        <div id="PRM57_select">${defVal}</div>
    </div>
    <div class="panel-header">
        <%-- 发货单查询 --%>
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
            <cherry:form id="mainForm" class="inline" onsubmit="BINOLSSPRM57.search(); return false;">
                <input type="hidden" id="currentUnitID" name="currentUnitID" value="BINOLSSPRM57"/>
                <div class="box-header">
                    <strong>
                        <span class="ui-icon icon-ttl-search"></span>
                        <%-- 查询条件 --%>
                        <s:text name="PRM57_condition"/>
                    </strong>
                </div>
                <div class="box-content clearfix">
                    <div class="column" style="width:50%;height:65px;">
                    <p>
                        <%-- 发货单号 --%>
                        <label><s:text name="PRM57_deliverRecNo"/></label>
                        <s:textfield name="deliverRecNo" cssClass="text" maxlength="40" onblur="ignoreCondition(this);return false;"/>
                    </p>
                    <p>
                        <%-- 促销品名称 --%>
                        <label class="left"><s:text name="PRM57_promotionProductName"/></label>
                        <%--<s:hidden name="prmVendorId" id="prmVendorId"/>--%>
                        <%--<s:textfield name="promotionProductName" cssClass="text" maxlength="30"/>--%>
                        <input id="BIN_BrandInfoID" type="hidden" value='<s:property value="#session.userinfo.BIN_BrandInfoID"/>'></input>
                        <table class="all_clean left"><tbody id="promotion_ID"></tbody></table>
                        <a class="add" onclick="BINOLSSPRM57.openPrmPopup();">
                           <span class="ui-icon icon-search"></span>
                           <span class="button-text"><s:text name="global.page.Popselect"/></span>
                        </a>
                    </p>
                    </div>
                    <div class="column last" style="width:49%;">
                    <p id="dateCover" class="date">
                        <%-- 发货日期 --%>
                        <label><s:text name="PRM57_deliverDate"/></label>
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
                    <button class="right search" type="submit" onclick="BINOLSSPRM57.search();return false">
                        <span class="ui-icon icon-search-big"></span>
                        <%-- 查询 --%>
                        <span class="button-text"><s:text name="PRM57_search"/></span>
                    </button>
                </p>
            </cherry:form>
        </div>
        <div id="section" class="section hide">
            <div class="section-header">
                <strong>
                    <span class="ui-icon icon-ttl-section-search-result"></span>
                    <%-- 查询结果一览 --%>
                    <s:text name="PRM57_results_list"/>
                </strong>
            </div>
            <div class="section-content" id="result_list">
                <div class="toolbar clearfix">
                <span id="headInfo" style=""></span>
                <span class="right"><a class="setting"><span class="ui-icon icon-setting"></span>
                <span class="button-text">
                <%-- 设置列 --%>
                <s:text name="PRM57_colSetting"/>
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
                    <th><s:text name="PRM57_num"/></th><%-- No. --%>
                    <th><s:text name="PRM57_deliverRecNo"/><span class="ui-icon ui-icon-document"></span></th><%-- 发货单号 --%>
                    <th><s:text name="PRM57_departNameDeliver"/></th><%-- 发货部门  --%>
                    <th><s:text name="PRM57_departNameReceive"/></th><%-- 收货部门 --%>
                    <th><s:text name="PRM57_totalQuantity"/></th><%-- 总数量 --%>
                    <th><s:text name="PRM57_totalAmount"/></th><%-- 总金额 --%>
                    <th><s:text name="PRM57_deliverDate"/></th><%-- 发货日期 --%>
                </tr>
            </thead>
        </table>
    </div>
</s:i18n>
<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>
<%-- ============ 弹出dataTable 促销产品共通导入 START ================= --%>
<%--<jsp:include page="/WEB-INF/jsp/common/prmProductTable.jsp" flush="true" />--%>
<%-- ============ 弹出dataTable 促销产品共通导入 END =================== --%>
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
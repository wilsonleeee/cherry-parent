<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%-- 产品订货--%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%> 
<%@ page import="com.cherry.cm.core.CherryConstants" %>
<script type="text/javascript" src="/Cherry/js/st/sfh/BINOLSTSFH01.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<s:i18n name="i18n.st.BINOLSTSFH01">
<cherry:form id="mainForm" class="inline" onsubmit="return false;">
    <div class="hide">
        <s:text name="SFH01_Select" id="select"/>
        <s:url id="url_getdepotAjax" value="/st/BINOLSTSFH01_getDepot" />
        <span id ="urlgetdepotAjax" >${url_getdepotAjax}</span>
        <s:url id="getLogicInfo_url" value="/st/BINOLSTSFH01_getLogicInfo" />
        <span id ="getLogicInfo" >${getLogicInfo_url}</span>
        <s:url id="s_submitURL" value="/st/BINOLSTSFH01_submit" />
        <span id ="submitURL" >${s_submitURL}</span>
        <s:url id="s_saveURL" value="/st/BINOLSTSFH01_save" />
        <span id ="saveURL" >${s_saveURL}</span>
        <s:url id="add_url" value="/common/BINOLCM02_popPrtImageDialog" />
        <span id ="add_url" >${add_url}</span>
        <s:url id="url_getStockCount" value="/st/BINOLSTSFH01_getPrtVenIdAndStock" />
        <span id ="s_getStockCount" >${url_getStockCount}</span>
        <s:url id="url_getOutDepart" value="/st/BINOLSTSFH01_getOutDepart" />
        <span id ="s_getOutDepart" >${url_getOutDepart}</span>
        <s:select id="departTypePop" name="departTypePop" list='#application.CodeTable.getCodes("1000")' listKey="CodeKey" listValue="Value"/>
        <input type="hidden" id="brandInfoId" value='<s:property value="#session.userinfo.BIN_BrandInfoID"/>'>
    </div>
    <div class="panel-header">
        <div class="clearfix"> 
            <span class="breadcrumb left">      
                <jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
            </span>
        </div>
    </div>
    <div id="actionResultDisplay"></div>
    <%-- ================== 错误信息提示 START ======================= --%>
    <div id="errorDiv2" class="actionError" style="display:none">
        <ul>
            <li><span id="errorSpan2"></span></li>
        </ul>
    </div>
    <%-- ================== 错误信息提示   END  ======================= --%>
    <s:if test="hasActionErrors()">
        <div class="actionError" id="actionResultDiv">
            <s:actionerror/>
        </div>
        <div style="height:20px"></div>
    </s:if>
    <s:else>
        <div class="panel-content">
            <div class="section">
                <div class="section-header">
                    <strong>
                        <span class="ui-icon icon-ttl-section-info"></span><s:text name="SFH01_general"/>
                    </strong>
                </div>
            <div class="section-content">
                <table class="detail">
                    <tr>
                        <th><s:text name="SFH01_date"/></th>
                        <td><s:property value="date"/><input type="hidden" name="date" value='<s:property value="date"/>'></td>
                        <th><s:text name="SFH01_operator"/></th>
                        <td><s:property value="#session.userinfo.employeeName"/></td>
                    </tr>
                    <tr>
                        <th><s:text name="SFH01_inDepartment"/></th>
                        <td>
                            <input type="hidden" name="inOrganizationId" id="inOrganizationId" value='${initInfoMap.defaultDepartID}'>
                            <span id="inOrgName" class="left"><s:property value='initInfoMap.defaultDepartCodeName'/></span>
                            <%--<input type="hidden" id="inTestType" value="<s:property value='initInfoMap.defaultTestType'/>"></input>--%>
                            <a class="add right" onclick="BINOLSTSFH01.openInDepartBox(this);">
                                <span class="ui-icon icon-search"></span>
                                <span class="button-text"><s:text name="SFH01_select"/></span>
                            </a>
                        </td>
                        <th><s:text name="SFH01_outDepartment"/></th>
                        <td>
                            <input type="hidden" name="outOrganizationId" id="outOrganizationId" value='${initInfoMap.defaultOutDepartID}'>
                            <span id="outOrgName" class="left"><s:property value='initInfoMap.defaultOutDepartCodeName'/></span>
                            <a class="add right" onclick="BINOLSTSFH01.openOutDepartBox(this);">
                                <span class="ui-icon icon-search"></span>
                                <span class="button-text"><s:text name="SFH01_select"/></span>
                            </a>
                        </td>
                    </tr>
                    <tr>
                        <th><s:text name="SFH01_InDepot"/></th>
                        <td>
                            <select style="width:200px;" name="inDepotId" id="inDepotId" disabled="true" onchange="BINOLSTSFH01.clearDetailData();return false;">
                                <s:iterator value="inDepotList">
                                    <option value="<s:property value="BIN_DepotInfoID" />">
                                        <s:property value="DepotCodeName"/>
                                    </option>
                                </s:iterator>
                            </select>
                        </td>
                        <th><s:text name="SFH01_InLogicDepot"/></th>
                        <td>
                            <s:select name="inLogicDepotId" list="inLogicDepotList" listKey="BIN_LogicInventoryInfoID" listValue="LogicInventoryCodeName" disabled="true" cssStyle="width:200px;" onchange="BINOLSTSFH01.clearDetailData('flag');return false;"/>
                        </td>
                    </tr>
                    <tr>
                        <th><s:text name="SFH01_orderType"/></th>
                        <td>
                            <s:select name="orderType" list='#application.CodeTable.getCodes("1168")' listKey="CodeKey" listValue="Value" headerKey="" cssStyle="width:150px;"/>
                        </td>
                        <th></th>
                        <td></td>
                    </tr>
                    <tr>
                        <th><s:text name="SFH01_ReasonAll"/></th>
                        <td colspan=3>
                            <input class="text" type="text" name="reasonAll" id="reasonAll" maxlength="200" style="width:95%;"/>
                        </td>
                    </tr>
                </table>
                </div>
            </div>
        <div class="section">
            <div class="section-header"><strong><span class="ui-icon icon-ttl-section-list"></span><s:text name="SFH01_detail"/></strong></div>
            <div class="section-content">
            <div class="toolbar clearfix">
                <span class="left">
                    <a id="spanBtnadd" class="add" onclick="BINOLSTSFH01.openProPopup();"><span class="ui-icon icon-add"></span><span class="button-text"><s:text name="SFH01_add"/></span></a> 
                    <a id="spanBtdelete" class="delete" onclick="BINOLSTSFH01.deleterow();"><span class="ui-icon icon-delete"></span><span class="button-text"><s:text name="SFH01_delete"/></span></a>
                   <!-- <a id="spanBtnadd" class="add" onclick="BINOLSTSFH01.popAdd();"><span class="ui-icon icon-add"></span><span class="button-text"><s:text name="图片模式选择产品"/></span></a> -->
                    <span class="bg_yew" ><span style="line-height:25px;" class="highlight">*<s:text name="SFH01_drag"/></span></span>
                </span>
            </div>
            <div id="canceldetail">
                <div style="width:100%;overflow-x:scroll;">
                <table id="mainTable" cellpadding="0" cellspacing="0" border="0" width="100%" class="background_1">
                    <thead>
                       <tr>
                            <th class="tableheader" width="3%">
                                <input id="allSelect" type="checkbox"  class="checkbox" onclick="doSelectAll(this,'#databody');"/>
                                <s:text name="SFH01_allselect"/>
                            </th>
                            <th class="tableheader" width="15%" onclick="BINOLSTSFH01.sortTable('_unitCodeArr');">
                            <div class="DataTables_sort_wrapper"><s:text name="SFH01_code"/><span id="_unitCodeArr" class="css_right ui-icon ui-icon-carat-2-n-s"></span></div></th>
                            <th class="tableheader" width="15%" onclick="BINOLSTSFH01.sortTable('_barCodeArr');">
                            <div class="DataTables_sort_wrapper"><s:text name="SFH01_barcode"/><span id="_barCodeArr" class="css_right ui-icon ui-icon-carat-2-n-s"></span></div></th>
                            <th class="tableheader" width="18%"><s:text name="SFH01_proname"/></th>
                            <th class="tableheader" width="8%"><s:text name="SFH01_price"/></th>
                            <th class="tableheader" width="5%"><s:text name="SFH01_NowCount"/></th>
                            <th class="tableheader" width="10%" onclick="BINOLSTSFH01.sortTable('_quantArr');">
                            <div class="DataTables_sort_wrapper"><s:text name="SFH01_quantity"/><span id="_quantArr" class="css_right ui-icon ui-icon-carat-2-n-s"></span></div></th>
                            <th class="tableheader" width="10%"><div style="line-height:13px;"><s:text name="SFH01_money"/><s:text name="SFH01_moneyu"/></div></th>
                            <th class="tableheader" width="20%"><s:text name="SFH01_reason"/></th>
                            <th style="display:none">
                        </tr>
                    </thead>
                    <tbody id="databody">
                    </tbody>
                </table>
                </div>
            </div>
            <div id="mydetail" class="section">
            </div>
            <hr class="space" />
            <table cellpadding="0" cellspacing="0" width="30%" border="0" class="right editable">
                <tr>
                    <th rowspan="2" scope="row" class="center"><s:text name="global.page.total"/></th><%-- 合计 --%>
                    <td class="center"><s:text name="SFH01_totalQuantity"/></td><%-- 总数量 --%>
                    <td class="center"><s:text name="SFH01_totalAmount"/></td><%-- 总金额--%>
                </tr>
                <tr>
                    <td class="center">
                    <%-- 总数量 --%>
                    <span id="totalQuantity">0</span>
                    </td>
                    <td class="center">
                        <%-- 总金额--%>
                        <span id="totalAmount">0.00</span>
                    </td>
                </tr>
            </table>
            <hr class="space" />
            <div class="center clearfix">
                <button class="save" type="button" onclick="BINOLSTSFH01.save();return false;"><span class="ui-icon icon-save"></span><span class="button-text"><s:text name="SFH01_Save"/></span></button>
                <cherry:show domId="BINOLSTSFH0101">
                    <button class="confirm" type="button" onclick="BINOLSTSFH01.btnSendClick()"><span class="ui-icon icon-confirm"></span><span class="button-text"><s:text name="SFH01_OK"/></span></button>
                </cherry:show>
            </div>
          </div>
        </div>
      </div>
      </s:else>
      <div class="hide" id="dialogInit"></div>
<%-- ================== 弹出发货部门datatable -- 部门共通START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/popDepartTable.jsp" flush="true" />
<%-- ================== 弹出datatable -- 部门共通START ======================= --%>
<%-- ================== 弹出收货部门datatable -- 部门共通START ======================= --%>
<jsp:include page="/WEB-INF/jsp/st/common/popDepartTableBusinessType.jsp" flush="true" />
<%-- ================== 弹出datatable -- 部门共通START ======================= --%>
<%-- ================== 弹出datatable -- 促销品发货单共通START ======================= --%>
<jsp:include page="/WEB-INF/jsp/st/common/BINOLSTCM12.jsp" flush="true" />
<%-- ================== 弹出datatable -- 促销品发货单共通START ======================= --%>
</cherry:form>
<div id="errmessage" style="display:none">
    <input type="hidden" id="errmsg1" value='<s:text name="ESS00007"/>'/>
    <input type="hidden" id="errmsg2" value='<s:text name="ESS00008"/>'/>
    <input type="hidden" id="errmsg3" value='<s:text name="ESS00040"/>'/>
    <input type="hidden" id="errmsg4" value='<s:text name="ESS00042"/>'/>
    <input type="hidden" id="errmsgESS00024" value='<s:text name="ESS00024"/>'/>
    <input type="hidden" id="notOnlyOneWarning" value='<s:text name="SFH01_notOnlyOne"/>'/>
    <input type="hidden" id="noOutDepart" value='<s:text name="SFH01_noOutDepart"/>'/>
    <input type="hidden" id="noOutDepot" value='<s:text name="SFH01_noOutDepot"/>'/>
    <input type="hidden" id="noInDepart" value='<s:text name="SFH01_noInDepart"/>'/>
    <input type="hidden" id="noInDepot" value='<s:text name="SFH01_noInDepot"/>'/>
    <input type="hidden" id="noInDepart" value='<s:text name="SFH01_noInDepart"/>'/>
    <input type="hidden" id="errmsg_EST00008" value='<s:text name="EST00008"/>'/>
    <input type="hidden" id="errmsg_EST00009" value='<s:text name="EST00009"/>'/>
</div>
</s:i18n>
<%-- ================== dataTable共通导�?START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导�?   END  ======================= --%>
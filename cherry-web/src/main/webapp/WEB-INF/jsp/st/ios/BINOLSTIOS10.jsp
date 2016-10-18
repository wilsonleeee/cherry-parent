<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%-- 退库申请--%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%> 
<%@ page import="com.cherry.cm.core.CherryConstants" %>
<script type="text/javascript" src="/Cherry/js/st/ios/BINOLSTIOS10.js?v=20160615"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<s:i18n name="i18n.st.BINOLSTIOS10">
    <div class="hide">
        <s:text name="IOS10_Select" id="select"/>
        <s:url id="url_getdepotAjax" value="/st/BINOLSTIOS10_getDepot" />
        <span id ="urlgetdepotAjax" >${url_getdepotAjax}</span>
        <s:url id="getLogicInfo_url" value="/st/BINOLSTIOS10_getLogicInfo" />
        <span id ="getLogicInfo" >${getLogicInfo_url}</span>
        <s:url id="s_submitURL" value="/st/BINOLSTIOS10_submit" />
        <span id ="submitURL" >${s_submitURL}</span>
        <s:url id="s_saveURL" value="/st/BINOLSTIOS10_save" />
        <span id ="saveURL" >${s_saveURL}</span>
        <s:url id="url_getStockCount" value="/st/BINOLSTIOS10_getPrtVenIdAndStock" />
        <span id ="s_getStockCount" >${url_getStockCount}</span>
        <s:url id="url_getInDepart" value="/st/BINOLSTIOS10_getInDepart" />
        <span id ="s_getInDepart" >${url_getInDepart}</span>
        <s:url id="url_getAllRAProduct" value="/st/BINOLSTIOS10_getAllRAProduct" />
        <span id ="s_getAllRAProduct" >${url_getAllRAProduct}</span>
        <s:select id="departTypePop" name="departTypePop" list='#application.CodeTable.getCodes("1000")' listKey="CodeKey" listValue="Value"/>
        <input type="hidden" id="brandInfoId" value='<s:property value="#session.userinfo.BIN_BrandInfoID"/>'>
        <input type="hidden" id="sysConfigUsePrice" value='<s:property value='sysConfigUsePrice'/>'>
        <!-- 用于做负库存控制，由系统配置项控制是否允许负库存操作 -->
        <input type="hidden"  id="checkStockFlag" value='<s:property value='checkStockFlag'/>'>
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
<cherry:form id="mainForm" class="inline" onsubmit="return false;">
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
                        <span class="ui-icon icon-ttl-section-info"></span><s:text name="IOS10_general"/>
                    </strong>
                </div>
            <div class="section-content">
                <table class="detail">
                    <tr>
                        <th><s:text name="IOS10_date"/></th>
                        <td><s:date name="new java.util.Date()" format="yyyy-MM-dd" /></td>
                        <th><s:text name="IOS10_operator"/></th>
                        <td><s:property value="#session.userinfo.employeeName"/></td>
                    </tr>
                    <tr>
                        <th><s:text name="IOS10_outDepartment"/></th>
                        <td>
                            <input type="hidden" name="outOrganizationID" id="outOrganizationID" value='${initInfoMap.defaultOutDepartID}'>
                            <span id="outOrgName" class="left"><s:property value='initInfoMap.defaultOutDepartCodeName'/></span>
                            <a class="add right" onclick="BINOLSTIOS10.openOutDepartBox(this);">
                                <span class="ui-icon icon-search"></span>
                                <span class="button-text"><s:text name="IOS10_select"/></span>
                            </a>
                        </td>
                        <th><s:text name="IOS10_inDepartment"/></th>
                        <td>
                            <input type="hidden" name="inOrganizationID" id="inOrganizationID" value='${initInfoMap.defaultInDepartID}'>
                            <span id="inOrgName" class="left"><s:property value='initInfoMap.defaultInDepartCodeName'/></span>
                            <a class="add right" onclick="BINOLSTIOS10.openInDepartBox(this);">
                                <span class="ui-icon icon-search"></span>
                                <span class="button-text"><s:text name="IOS10_select"/></span>
                            </a>
                        </td>
                    </tr>
                    <tr>
                        <th><s:text name="IOS10_outDepot"/></th>
                        <td>
                            <select style="width:200px;" name="outInventoryInfoID" id="outInventoryInfoID" disabled="true" onchange="BINOLSTIOS10.clearDetailData();return false;">
                                <s:iterator value="outDepotList">
                                    <option value="<s:property value="BIN_DepotInfoID" />">
                                        <s:property value="DepotCodeName"/>
                                    </option>
                                </s:iterator>
                            </select>
                        </td>
                        <th><s:text name="IOS10_outLogicDepot"/></th>
                        <td>
                            <s:select name="outLogicInventoryInfoID" list="outLogicDepotList" listKey="BIN_LogicInventoryInfoID" listValue="LogicInventoryCodeName" disabled="true" cssStyle="width:200px;" onchange="BINOLSTIOS10.clearDetailData('flag');return false;"/>
                        </td>
                    </tr>
                    <tr>
                        <th><s:text name="IOS10_ReasonAll"/></th>
                        <td colspan=3>
                            <input class="text" type="text" id="reason" name="reason" maxlength="200" style="width:95%;"/>
                        </td>
                    </tr>
                </table>
                </div>
            </div>
        <div class="section">
            <div class="section-header"><strong><span class="ui-icon icon-ttl-section-list"></span><s:text name="IOS10_detail"/></strong></div>
            <div class="section-content">
            <div class="toolbar clearfix">
                <span class="left">
                	<a id="addLineBtn" class="add"  onclick="BINOLSTIOS10.addNewLine();return false;"><span class="ui-icon icon-add"></span><span class="button-text"><s:text name="IOS10_add"/></span></a> 
                    <a id="spanBtnAdd" class="add" onclick="BINOLSTIOS10.openProPopup();"><span class="ui-icon icon-add"></span><span class="button-text"><s:text name="IOS10_selectPrt"/></span></a> 
                    <a id="spanBtnDelete" class="delete" onclick="BINOLSTIOS10.deleterow();"><span class="ui-icon icon-delete"></span><span class="button-text"><s:text name="IOS10_delete"/></span></a>
                    <a id="spanBtnAllRA" class="add" onclick="BINOLSTIOS10.showAllRA();"><span class="ui-icon icon-add"></span><span class="button-text"><s:text name="IOS10_AllRA"/></span></a>
                    <a id="spanBtnCancelAllRA" class="delete" style="display: none;" onclick="BINOLSTIOS10.canceProductResult();"><span class="ui-icon icon-delete"></span><span class="button-text"><s:text name="IOS10_CancelAllRA"/></span></a>
                </span>
            </div>
            
            <input id="rowNumber" type="hidden" value="0">
            <div id="canceldetail">
                <div style="width:100%;overflow-x:scroll;">
                <table id="mainTable" cellpadding="0" cellspacing="0" border="0" width="100%" class="background_1">
                    <thead>
                       <tr>
                            <th class="tableheader" width="3%">
                                <input id="allSelect" type="checkbox"  class="checkbox" onclick="doSelectAll(this,'#databody');"/>
                                <s:text name="IOS10_allselect"/>
                            </th>
                            <th class="tableheader" width="15%">
                            <div class="DataTables_sort_wrapper"><s:text name="IOS10_code"/></div></th>
                            <th class="tableheader" width="15%">
                            <div class="DataTables_sort_wrapper"><s:text name="IOS10_barcode"/></div></th>
                            <th class="tableheader" width="18%"><s:text name="IOS10_proname"/></th>
                            <th class="tableheader" width="8%"><s:text name="IOS10_price"/></th>
                            <th class="tableheader" width="5%"><s:text name="IOS10_NowCount"/></th>
                            <th class="tableheader" width="10%">
                            <div class="DataTables_sort_wrapper"><s:text name="IOS10_quantity"/></div></th>
                            <th class="tableheader" width="10%"><div style="line-height:13px;"><s:text name="IOS10_money"/><s:text name="IOS10_moneyu"/></div></th>
                            <th class="tableheader" width="20%"><s:text name="IOS10_reason"/></th>
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
                    <td class="center"><s:text name="IOS10_totalQuantity"/></td><%-- 总数量 --%>
                    <td class="center"><s:text name="IOS10_totalAmount"/></td><%-- 总金额--%>
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
                <button class="save" type="button" onclick="BINOLSTIOS10.save();return false;"><span class="ui-icon icon-save"></span><span class="button-text"><s:text name="IOS10_Save"/></span></button>
                <cherry:show domId="BINOLSTIOS1001">
                    <button class="confirm" type="button" onclick="BINOLSTIOS10.btnSendClick()"><span class="ui-icon icon-confirm"></span><span class="button-text"><s:text name="IOS10_OK"/></span></button>
                </cherry:show>
            </div>
          </div>
        </div>
      </div>
      </s:else>
      <div class="hide" id="dialogInit"></div>
</cherry:form>
<%-- ================== 弹出退库部门datatable -- 部门共通START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/popDepartTable.jsp" flush="true" />
<%-- ================== 弹出退库部门datatable -- 部门共通START ======================= --%>
<%-- ================== 弹出接收退库部门datatable -- 部门共通START ======================= --%>
<jsp:include page="/WEB-INF/jsp/st/common/popDepartTableBusinessType.jsp" flush="true" />
<%-- ================== 弹出接收退库部门datatable -- 部门共通START ======================= --%>
<div id="errmessage" style="display:none">
    <input type="hidden" id="errmsg1" value='<s:text name="ESS00007"/>'/>
    <input type="hidden" id="errmsg2" value='<s:text name="ESS00008"/>'/>
    <input type="hidden" id="errmsg3" value='<s:text name="ESS00040"/>'/>
    <input type="hidden" id="errmsg4" value='<s:text name="ESS00042"/>'/>
    <input type="hidden" id="errmsgESS00024" value='<s:text name="ESS00024"/>'/>
    <input type="hidden" id="errmsg_EST00008" value='<s:text name="EST00008"/>'/>
    <input type="hidden" id="errmsg_EST00009" value='<s:text name="EST00009"/>'/>
    <input type="hidden" id="notOnlyOneWarning" value='<s:text name="IOS10_notOnlyOne"/>'/>
    <input type="hidden" id="noOutDepart" value='<s:text name="IOS10_noOutDepart"/>'/>
    <input type="hidden" id="noOutDepot" value='<s:text name="IOS10_noOutDepot"/>'/>
    <input type="hidden" id="noInDepart" value='<s:text name="IOS10_noInDepart"/>'/>
    <input type="hidden" id="noInDepot" value='<s:text name="IOS10_noInDepot"/>'/>
    <input type="hidden" id="noInDepart" value='<s:text name="IOS10_noInDepart"/>'/>
    <input type="hidden" id="msg_maxDetail" value='<s:text name="IOS10_maxDetail"/>'/>
    <input type="hidden" id="errmsg_EST00042" value='<s:text name="EST00042"/>'/><!-- 输入数量不能大于库存数量！ -->
    <input type="hidden" id="errmsg_EST00035" value='<s:text name="EST00035"/>'/>
</div>
</s:i18n>
<%-- ================== dataTable共通导入START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入   END  ======================= --%>
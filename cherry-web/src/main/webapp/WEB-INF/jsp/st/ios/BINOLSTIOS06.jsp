<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%> 
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="com.cherry.cm.core.CherryConstants" %>
<%@ page import="com.cherry.cm.cmbeans.UserInfo" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.*" %>
<script type="text/javascript" src="/Cherry/js/st/ios/BINOLSTIOS06.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script language="javascript">
</script>
<%
UserInfo userinfo = (UserInfo)request.getSession().getAttribute(CherryConstants.SESSION_USERINFO);
SimpleDateFormat dateFm = new SimpleDateFormat("yyyy-MM-dd"); //格式化当前系统日期
String dateTime = dateFm.format(new java.util.Date());
%>
<s:i18n name="i18n.st.BINOLSTIOS06">
<div class="hide">
    <s:url id="url_getDepot" value="/st/BINOLSTIOS06_getDepot" />
    <span id ="s_getDepot" style="display:none">${url_getDepot}</span>
    <s:url id="url_saveURL" value="/st/BINOLSTIOS06_save" />
    <span id ="s_saveURL" style="display:none">${url_saveURL}</span>
    <s:url id="url_submitURL" value="/st/BINOLSTIOS06_submit" />
    <span id ="s_submitURL" style="display:none">${url_submitURL}</span>
    <s:url id="url_getStockCount" value="/st/BINOLSTIOS06_getStockCount" />
    <span id ="s_getStockCount" >${url_getStockCount}</span>
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
<cherry:form id="mainForm" action="" class="inline">
<div id="errorDiv2" class="actionError" style="display:none">
    <ul>
        <li><span id="errorSpan2"></span></li>
    </ul>
</div>
<s:if test="hasActionErrors()">
    <div class="actionError" id="actionResultDiv">
        <s:actionerror/>
    </div>
    <div style="height:20px"></div>
</s:if>
<s:else>
    <div class="panel-content">
        <%-- ========概要部分============= --%> 
        <div class="section">
            <div class="section-header">
                <strong>
                    <span class="ui-icon icon-ttl-section-info"></span>
                    <s:text name="IOS06_general"/>
                </strong>
            </div>
            <div class="section-content">
            <table class="detail">
                <tr>
                    <th><s:text name="IOS06_date"/></th>
                    <td><%= dateTime %></td>
                    <th><s:text name="IOS06_operator"/></th>
                    <td><%=userinfo.getEmployeeName() %></td>
                </tr>
                <tr>
                    <th><s:text name="IOS06_inDepart"/></th>
                    <td>
                        <input type="hidden" name="inOrganizationID" id="inOrganizationID" value="<s:property value="initInfoMap.defaultDepartID"/>">
                        <label id="inOrgName" class="left"><s:property value="initInfoMap.defaultDepartCodeName"></s:property></label>
                        <a class="add right" onclick="BINOLSTIOS06.openInDepartBox(this);">
                            <span class="ui-icon icon-search"></span>
                            <span class="button-text"><s:text name="IOS06_lblSelect"/></span>
                        </a>
                    </td>
                    <th><s:text name="IOS06_inDepot"/></th>
                    <td>
                        <select style="width:200px;" name="inDepotID" id="inDepotID" onchange="BINOLSTIOS06.clearDetailData(true);">
                            <s:iterator value="inDepotList">
                                <option value="<s:property value="BIN_DepotInfoID" />">
                                    <s:property value="DepotCodeName"/>
                                </option>
                            </s:iterator>
                        </select>
                    </td>
                </tr>
                <tr>
                    <th><s:text name="IOS06_outDepart"/></th>
                    <td>
                        <input type="hidden" name="outOrganizationID" id="outOrganizationID">
                        <label id="outOrgName" class="left"></label>
                        <a class="add right" onclick="BINOLSTIOS06.openOutDepartBox(this);">
                            <span class="ui-icon icon-search"></span>
                            <span class="button-text"><s:text name="IOS06_lblSelect"/></span>
                        </a>
                    </td>
                    <s:if test='null != inLogicDepotList && inLogicDepotList.size()>0'>
                        <th><s:text name="IOS06_inLogicDepot"/></th>
                        <td>
                            <s:select name="inLogicDepotID" list="inLogicDepotList" listKey="BIN_LogicInventoryInfoID"
                                onchange="BINOLSTIOS06.clearDetailData(true);"
                                listValue="LogicInventoryCodeName" cssStyle="width:200px;" />
                        </td>
                    </s:if>
                    <s:else>
                        <th></th>
                        <td></td>
                    </s:else>
                </tr>
                <tr>
                    <th><s:text name="IOS06_reasonAll"/></th>
                    <td colspan=3>
                        <input class="text" type="text" name="reasonAll" id="reasonAll" maxlength="100" style="width:95%;"/>
                    </td>
                </tr>
            </table>
            </div>
        </div>
        <div id="mydetail" class="section">
            <div class="section-header">
                <strong><span class="ui-icon icon-ttl-section-list"></span>
                <s:text name="IOS06_lbldetail"/></strong>
            </div>
            <div class="section-content">
                <div class="toolbar clearfix">
                    <span class="left">
                        <input id="allSelect" type="checkbox"  class="checkbox" onclick="BINOLSTIOS06.selectAll()"/>
                        <s:text name="IOS06_btnAllSelect"/>
                        <a class="add" onclick="BINOLSTIOS06.openProPopup(this);">
                            <span class="ui-icon icon-add"></span>
                            <span class="button-text"><s:text name="IOS06_btnSelectProduct"/></span>
                        </a> 
                        <a class="delete" onclick="BINOLSTIOS06.deleterow();">
                            <span class="ui-icon icon-delete"></span>
                            <span class="button-text"><s:text name="IOS06_btnDelete"/></span>
                        </a>
                        <a class="add" onclick="BINOLSTIOS06.addNewLine();">
                            <span class="ui-icon icon-add"></span>
                            <span class="button-text"><s:text name="IOS06_btnAdd"/></span>
                        </a>
                    </span>
                </div>
                <div style="width:100%;overflow-x:scroll;">
                	<input type="hidden" id="rowNumber" value="0"/>
                    <table id="mainTable" cellpadding="0" cellspacing="0" border="0" width="100%">
                        <thead>
                            <tr>
                                <th class="tableheader" width="5%"><s:text name="IOS06_lblSelect"/></th>
                                <th class="tableheader" width="10%"><s:text name="IOS06_unitCode"/></th>
                                <th class="tableheader" width="10%"><s:text name="IOS06_barcode"/></th>
                                <th class="tableheader" width="20%"><s:text name="IOS06_prodcutName"/></th>
                                <th class="tableheader" width="8%"><s:text name="IOS06_price"/></th>
                                <th class="tableheader" width="8%"><s:text name="IOS06_stock"/></th>
                                <th class="tableheader" width="12%"><s:text name="IOS06_quantity"/></th>
                                <th class="tableheader" width="10%"><s:text name="IOS06_amount"/></th>
                                <th class="tableheader" width="20%"><s:text name="IOS06_reason"/></th>
                                <th style="display:none">
                            </tr>
                        </thead>
                        <tbody id="databody"></tbody>
                    </table>
                </div>
                <hr class="space" />
                <div class="center clearfix">
                    <button class="save" type="button" onclick="BINOLSTIOS06.submitSave()">
                        <span class="ui-icon icon-save"></span>
                        <span class="button-text"><s:text name="IOS06_btnSave"/></span>
                    </button>
                    <cherry:show domId="BINOLSTIOS0601">
                        <button class="confirm" type="button" onclick="BINOLSTIOS06.submitSend()">
                            <span class="ui-icon icon-confirm"></span>
                            <span class="button-text"><s:text name="IOS06_btnOK"/></span>
                        </button>
                    </cherry:show>
                </div>
            </div>
        </div>
    </div>
</s:else>
</cherry:form>
</s:i18n>
<div id="errmessage" style="display:none">
    <input type="hidden" id="errmsg1" value='<s:text name="EST00008"/>'/>
    <input type="hidden" id="errmsg2" value='<s:text name="EST00009"/>'/>
    <input type="hidden" id="errmsg3" value='<s:text name="EST00007"/>'/>
    <input type="hidden" id="errmsgEST00006" value='<s:text name="EST00006"/>'/>
    <input type="hidden" id="errmsgEST00013" value='<s:text name="EST00013"/>'/>
    <input type="hidden" id="errmsgEST00025" value='<s:text name="EST00025"/>'/>
	<input type="hidden" id="errmsg_EST00035" value='<s:text name="EST00035"/>'/>
</div>
<%-- ================== 弹出datatable -- 部门共通START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/popDepartTable.jsp" flush="true" />
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== 弹出datatable -- 部门共通START ======================= --%>
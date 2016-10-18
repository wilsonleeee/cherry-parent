<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%--入库 新增/修改/详细--%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<script type="text/javascript" src="/Cherry/js/wp/ws/mng/BINOLWSMNG02_02.js"></script>
<s:i18n name="i18n.wp.BINOLWSMNG02">
<div class="main container clearfix">
	<div class="hide">
        <input type="hidden" id="counterCode" value='<s:property value="counterCode"/>'/>
	    <input type="hidden" id="currentMenuID" name="currentMenuID" value="BINOLWSMNG02_02"/>
	    <input type="hidden" id="pageId" name="pageId" value="BINOLWSMNG02_02"/>
	    <s:url id="save_url" value="/ws/BINOLWSMNG02_save"/>
	    <s:url id="submit_url" value="/ws/BINOLWSMNG02_submit"/>
        <s:url id="url_getStockCount" value="/st/BINOLSTIOS06_getStockCount" />
        <span id ="s_getStockCount" >${url_getStockCount}</span>
	    <a id="saveUrl" href="${save_url}"></a>
	    <a id="submitURL" href="${submit_url}"></a>
	    <s:select id="departTypePop" name="departTypePop" list='#application.CodeTable.getCodes("1000")' listKey="CodeKey" listValue="Value"/>
	    <s:text name="MNG02_selectAll" id="MNG02_selectAll"/>
	    <s:text name="global.page.select" id="globalSelect"/>
        <!-- 系统配置项产品入库使用价格 -->
        <input type="hidden" id="sysConfigUsePrice" value='<s:property value='sysConfigUsePrice'/>'>
	</div>
	<s:url id="doaction_url" value="/ws/BINOLWSMNG02_doaction"/>
<div class="hide">
    <a id="osdoactionUrl" href="${doaction_url}"></a>
</div>
    <div id="div_main" class="panel ui-corner-all">
        <div class="panel-header">
            <div class="clearfix">
                <span class="breadcrumb left">
	            <span class="ui-icon icon-breadcrumb"></span><s:text name="MNG02_popTitle"/></span>
            </div>
        </div>
        <div class="panel-content">
            <div id="actionResultDisplay"></div>
            <%-- ================== 错误信息提示 START ======================= --%>
            <div id="errorDiv2" class="actionError" style="display:none">
                <ul>
                    <li><span id="errorSpan2"></span></li>
                </ul>
            </div>
            <%-- ================== 错误信息提示   END  ======================= --%>
            <%-- ========概要部分============= --%> 
            <form id="mainForm" method="post" class="inline">
            <div class="section">
                <div class="section-header">
                    <strong>
                        <span class="ui-icon icon-ttl-section-info"></span>
                        <s:text name="MNG02_general"/>
                    </strong>
                </div>
                <div class="section-content">
                    <table class="detail">
                        <tr>
                            <s:if test='operateType.equals("newBill")'>
	                            <th><s:text name="MNG02_date"/></th>
	                            <td><s:property value="applyDate"/></td>
	                            <th><s:text name="MNG02_BA"/></th>
	                            <td>
		                            <%-- <s:if test='null != counterBAList && counterBAList.size>0'>
                                        <s:select name="tradeEmployeeID" list='counterBAList' listKey="baInfoId" listValue="baName" headerKey="" headerValue="%{globalSelect}" />
                                    </s:if>
                                    <s:else>
                                        <s:property value="#session.userinfo.employeeName"/>
                                        <s:hidden name="tradeEmployeeID" value="%{#session.userinfo.BIN_EmployeeID}"></s:hidden>
                                    </s:else> --%>
                                    <s:select name="tradeEmployeeID" list='counterBAList' listKey="baInfoId" listValue="baName" headerKey="" headerValue="%{globalSelect}" />
	                            </td>
                            </s:if>
                            <s:else>
	                            <th><s:text name="MNG02_date"/></th>
	                            <td><s:property value="inDepotMainMap.InDepotDate"/></td>
	                            <th><s:text name="MNG02_BA"/></th>
	                            <td><s:property value="inDepotMainMap.EmployeeName"/></td>
                            </s:else>
                        </tr>
                    </table>
                </div>
            </div>
            <div id="mydetail" class="section">
	            <div class="section-header">
	                <strong><span class="ui-icon icon-ttl-section-list"></span>
	                <s:text name="MNG02_lbldetail"/></strong>
	            </div>
                <div class="section-content">
                    <s:if test='operateType.equals("newBill") || operateType.equals("2")'>
	                    <div class="toolbar clearfix">
	                        <span class="left">
	                            <input id="allSelect" type="checkbox"  class="checkbox" onclick="binOLWSMNG02_02.selectAll();"/>
	                            <s:text name="MNG02_btnAllSelect"/>
	                            <a class="add" onclick="binOLWSMNG02_02.addNewLine();">
	                                <span class="ui-icon icon-add"></span>
	                                <span class="button-text"><s:text name="MNG02_btnAdd"/></span>
	                            </a>
	                            <a class="delete" onclick="binOLWSMNG02_02.deleterow();">
	                                <span class="ui-icon icon-delete"></span>
	                                <span class="button-text"><s:text name="MNG02_btnDelete"/></span>
	                            </a>
	                        </span>
	                    </div>
                    </s:if>
                    <div style="width:100%;overflow-x:scroll;">
                        <table id="mainTable" cellpadding="0" cellspacing="0" border="0" width="100%">
                            <thead>
                                <tr>
                                    <s:if test='operateType.equals("newBill") || operateType.equals("2")'>
	                                   <th class="tableheader" width="5%"><s:text name="MNG02_lblSelect"/></th>
	                                </s:if>
	                                <s:else>
	                                   <th class="tableheader" width="5%"><s:text name="MNG02_no"/></th>
	                                </s:else>
	                                <th class="tableheader" width="10%"><s:text name="MNG02_unitCode"/></th>
	                                <th class="tableheader" width="10%"><s:text name="MNG02_barcode"/></th>
	                                <th class="tableheader" width="20%"><s:text name="MNG02_prodcutName"/></th>
	                                <th class="tableheader" width="8%"><s:text name="MNG02_price"/></th>
	                                <s:if test='operateType.equals("newBill") || operateType.equals("2")  || operateType.equals("134")'>
	                                   <th class="tableheader" width="8%"><s:text name="MNG02_stock"/></th>
	                                </s:if>
	                                <th class="tableheader" width="12%"><s:text name="MNG02_quantity"/></th>
	                                <th class="tableheader" width="10%"><s:text name="MNG02_amount"/></th>
	                                <th class="tableheader" width="20%"><s:text name="MNG02_reason"/></th>
	                                <th style="display:none">
                                </tr>
                            </thead>
                            <tbody id="databody">
                                <s:if test='!operateType.equals("newBill")'>
	                                <s:iterator value="inDepotDetailList" status="status">
	                                    <tr id="dataRow<s:property value='#status.index+1'/>" class="<s:if test='#status.odd'>odd</s:if><s:else>even</s:else>">
                                            <td id="dataTd1" class="center">
                                                <s:if test='operateType.equals("2")'>
	                                               <input id="chkbox" type="checkbox" onclick="binOLWSMNG02_02.changechkbox(this);">
                                                </s:if>
                                                <s:else>
                                                    <span><s:property value="#status.index+1"/></span>
                                                </s:else>
	                                        </td>
	                                        <td id="dataTd2"><span><s:property value="UnitCode"/></span></td>
	                                        <td id="dataTd3"><span><s:property value="BarCode"/></span></td>
	                                        <td id="dataTd4"><span><s:property value="ProductName"/></span></td>
	                                        <td id="dataTd5" class="alignRight">
	                                            <s:if test='null!=Price'>
	                                                <s:text name="format.price"><s:param value="Price"></s:param></s:text>
	                                            </s:if>
	                                            <s:else>&nbsp;</s:else>
	                                        </td>
	                                        <s:if test='operateType.equals("newBill") || operateType.equals("2") || operateType.equals("134")'>
	                                           <td id="dataTDStock" class="alignRight"><s:property value="StockQuantity"/></td>
	                                        </s:if>
	                                        <td id="dataTd6" class="alignRight">
	                                            <s:if test='null != Quantity'>
	                                               <s:if test='operateType.equals("2")'>
	                                                   <input type="text"  id="quantityArr" name="quantityArr" style="width:120px;text-align:right;" class="hide"  size="10" maxlength="4" value='<s:property value="Quantity"/>' onchange="binOLWSMNG02_02.changeCount(this);"/>
                                                        <div id="hideQuantiyArr">
                                                            <s:text name="format.number"><s:param value="Quantity"></s:param></s:text>
                                                        </div>
	                                               </s:if>
	                                               <s:else>
	                                                   <s:text name="format.number"><s:param value="Quantity"></s:param></s:text>
	                                               </s:else>	                                                
	                                            </s:if>
	                                            <s:else>&nbsp;</s:else>
	                                        </td>
	                                        <td id="money" class="alignRight">
	                                            <s:if test='null!=Price && null != Quantity'>
	                                                <s:text name="format.price"><s:param value="Price*Quantity"></s:param></s:text>
	                                            </s:if>
	                                            <s:else>&nbsp;</s:else>
	                                        </td>
	                                        <td id="dataTd9">
	                                            <s:if test='operateType.equals("2") '>
                                                    <s:textfield name="commentsArr" maxlength="200" value="%{Comments}" cssStyle="width:96%" cssClass="hide"></s:textfield>
                                                    <div id="hideReason">
                                                        <p style="margin-bottom:0;"><s:property value="Comments"/></p>
                                                    </div>
	                                            </s:if>
	                                            <s:else>
                                                    <p style="margin-bottom:0;"><s:property value="Comments"/></p>
	                                            </s:else>
	                                        </td>
	                                        <td style="display:none" id="dataTd10">
	                                            <input type="hidden" id="priceUnitArr<s:property value='#status.index+1'/>" name="priceUnitArr" value="<s:property value='Price'/>"/>
	                                            <input type="hidden"  id="productVendorIDArr" name="productVendorIDArr" value="<s:property value='BIN_ProductVendorID'/>"/>
	                                            <input type="hidden" name="prtVendorId" value="<s:property value='BIN_ProductVendorID'/>"/>
	                                        </td>
	                                    </tr>
	                                </s:iterator>
                                </s:if>
                            </tbody>
                        </table>
                    </div>
                    <hr class="space" />
                    <table cellpadding="0" cellspacing="0" width="30%" border="0" class="right editable">
                        <tr>
	                        <th rowspan="2" scope="row" class="center"><s:text name="global.page.total"/></th><%-- 合计 --%>
	                        <td class="center"><s:text name="MNG02_totalQuantity"/></td><%-- 总数量 --%>
	                        <td class="center"><s:text name="MNG02_totalAmount"/></td><%-- 总金额--%>
                        </tr>
                        <tr>
	                        <td class="center">
	                            <%-- 总数量 --%>
	                            <span id="totalQuantity">
		                            <s:if test='null!=inDepotMainMap.TotalQuantity'>
		                                <s:text name="format.number"><s:param value="inDepotMainMap.TotalQuantity"></s:param></s:text>
		                            </s:if>
		                            <s:else>0</s:else>
	                            </span>
	                        </td>
	                        <td class="center">
	                            <%-- 总金额--%>
	                            <span id="totalAmount">
		                            <s:if test='null!=inDepotMainMap.TotalAmount'>
		                                <s:text name="format.price"><s:param value="inDepotMainMap.TotalAmount"></s:param></s:text>
		                            </s:if>
		                            <s:else>0.00</s:else>
	                            </span>
	                        </td>
                        </tr>
                    </table>
                    <hr class="space" />
	                <div style="display:none">
	                    <input type="hidden"  id="brandInfoId"  value='<s:property value="#session.userinfo.BIN_BrandInfoID"/>'>
	                    <input type="hidden"  id="operateType"  name="operateType" value='<s:property value="operateType"/>'>
	                    <s:if test='operateType.equals("newBill")'>
	                        <input type="hidden" id="inOrganizationID" name="inOrganizationID" value='<s:property value="inOrganizationID"/>'/>
	                        <input type="hidden" id="inInventoryInfoID" name="inInventoryInfoID" value='<s:property value="inInventoryInfoID"/>'/>
	                        <input type="hidden" id="inLogicInventoryInfoID" name="inLogicInventoryInfoID" value='<s:property value="inLogicInventoryInfoID"/>'/>
	                        <input type="hidden" id="reason" name="reason"  value=""/>
	                    </s:if>
	                    <s:else>
	                        <input type="hidden" id="inOrganizationID" name="inOrganizationID" value='<s:property value="inDepotMainMap.BIN_OrganizationID"/>'/>
	                        <input type="hidden" id="inInventoryInfoID" name="inInventoryInfoID" value='<s:property value="inDepotMainMap.BIN_InventoryInfoID"/>'/>
	                        <input type="hidden" id="inLogicInventoryInfoID" name="inLogicInventoryInfoID" value='<s:property value="inDepotMainMap.BIN_LogicInventoryInfoID"/>'/>
	                        <input type="hidden" id="reason" name="reason"  value='<s:property value="inDepotMainMap.Comments"/>'/>
	                    </s:else>
                        <s:if test='operateType.equals("2")' >
	                        <input type="hidden" id="rowNumber" value="<s:property value='inDepotMainMap.size()'/>"/>
	                        <input type="hidden" id="inTestType" value="<s:property value='inDepotMainMap.TestType'/>">
	                        <input type="hidden" id="updateTime" name="updateTime" value="<s:property value="inDepotMainMap.UpdateTime"/>">
	                        <input type="hidden" id="modifyCount" name="modifyCount" value="<s:property value="inDepotMainMap.ModifyCount"/>">
	                        <input type="hidden" id="verifiedFlag" name="verifiedFlag" value="<s:property value="inDepotMainMap.VerifiedFlag"/>">
	                        <s:hidden id="productInDepotId" name="productInDepotId" value="%{#request.productInDepotId}"></s:hidden>
                        </s:if>
                        <s:else>
                            <input type="hidden" id="rowNumber" value="0"/>
                        </s:else>
                        <input type="hidden" id="entryID" name="entryID" value='<s:property value="inDepotMainMap.WorkFlowID"/>'/>
                        <input type="hidden" id="actionID" name="actionID"/>
                        <input type="hidden" id="entryid" name="entryid"/>
                        <input type="hidden" id="actionid" name="actionid"/>
	                </div>
                    <div class="center clearfix">
                        <s:if test='operateType.equals("newBill") || operateType.equals("2")'>
	                        <button class="save" type="button" onclick="binOLWSMNG02_02.submitSave();">
	                            <span class="ui-icon icon-save"></span>
	                            <span class="button-text"><s:text name="MNG02_btnSave"/></span>
	                        </button>
	                        <button class="confirm" type="button" onclick="binOLWSMNG02_02.submitSend();">
	                            <span class="ui-icon icon-confirm"></span>
	                            <span class="button-text"><s:text name="MNG02_btnOK"/></span>
	                        </button>
	                        <s:if test='operateType.equals("2")'>
                                <button id="btn-icon-edit-big" class="confirm" onclick="binOLWSMNG02_02.modifyForm();return false;">
                                    <span class="ui-icon icon-edit-big"></span>
                                    <%-- 修改 --%>
                                    <span class="button-text"><s:text name="os.edit"/></span>
                                </button>
                        </s:if>
                        </s:if>
                        <s:elseif test='@com.cherry.cm.core.CherryConstants@OPERATE_GR_CONFIRM.equals(operateType)'>
			                <jsp:include page="/WEB-INF/jsp/common/osActionButton2.inc.jsp" flush="true" />
			            </s:elseif>	            
                        <button id="btnReturn" class="close" onclick="binOLWSMNG02_02.back();return false;" style="display:none;"><span class="ui-icon icon-back"></span>
                            <%--返回 --%>
                            <span class="button-text"><s:text name="global.page.back"/></span>
                        </button>
                        <button class="close" type="button" onclick="window.close();">
                            <span class="ui-icon icon-close"></span>
                            <span class="button-text"><s:text name="global.page.close"/></span>
                        </button>
                    </div>
                </div>
            </div>
            </form>
    </div>
</div>
</div>
</s:i18n>
<form action="BINOLWSMNG02_initDetail" id="productInDepotDetailUrl" method="post">
    <input name="csrftoken" type="hidden"/>
    <s:hidden name="productInDepotId" value="%{#request.productInDepotId}"></s:hidden>
    <input name="tradeType" type="hidden" value='<s:property value="tradeType"/>'/>
</form>
<div id="errmessage" style="display:none">
    <input type="hidden" id="errmsg_EST00006" value='<s:text name="EST00006"/>'/>
    <input type="hidden" id="errmsg_EST00007" value='<s:text name="EST00007"/>'/>
    <input type="hidden" id="errmsg_EST00008" value='<s:text name="EST00008"/>'/>
    <input type="hidden" id="errmsg_EST00009" value='<s:text name="EST00009"/>'/>
    <input type="hidden" id="errmsg_EST00013" value='<s:text name="EST00013"/>'/>
    <input type="hidden" id="errmsg_EST00015" value='<s:text name="EST00015"/>'/>
    <input type="hidden" id="errmsg_EST00022" value='<s:text name="EST00022"/>'/>
    <input type="hidden" id="errmsg_EST00025" value='<s:text name="EST00025"/>'/>
    <input type="hidden" id="errmsg_EST00035" value='<s:text name="EST00035"/>'/>
    <input type="hidden" id="errmsg_EST00036" value='<s:text name="EST00036"/>'/>
    <input type="hidden" id="errmsg_EST00037" value='<s:text name="EST00037"/>'/>
    <input type="hidden" id="errmsg_EST00039" value='<s:text name="EST00039"/>'/>
</div>
<%-- ================== 弹出datatable -- 部门共通START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/popDepartTable.jsp" flush="true" />
<%-- ================== 弹出datatable -- 部门共通START ======================= --%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%--调入 新增/修改/详细--%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<script type="text/javascript" src="/Cherry/js/common/popOSDialog.js"></script>
<script type="text/javascript" src="/Cherry/js/st/bil/BINOLSTBIL18.js"></script>
<script type="text/javascript" src="/Cherry/js/wp/ws/mng/BINOLWSMNG04_02.js?V=20161018"></script>
<s:i18n name="i18n.wp.BINOLWSMNG04">
<div class="main container clearfix">
	<div class="hide">
        <input type="hidden" id="counterCode" value='<s:property value="counterCode"/>'/>
	    <input type="hidden" id="currentMenuID" name="currentMenuID" value="BINOLWSMNG04_02"/>
	    <input type="hidden" id="pageId" name="pageId" value="BINOLWSMNG04_02"/>
        <s:url id="doaction_url" value="/st/BINOLSTBIL18_doaction"/>
        <a id="osdoactionUrl" href="${doaction_url}"></a>
	    <s:url id="save_url" value="/ws/BINOLWSMNG04_save"/>
	    <s:url id="submit_url" value="/ws/BINOLWSMNG04_submit"/>
        <s:url id="url_getStockCount" value="/st/BINOLSTIOS06_getStockCount" />
        <span id ="s_getStockCount" >${url_getStockCount}</span>
	    <a id="saveUrl" href="${save_url}"></a>
	    <a id="submitURL" href="${submit_url}"></a>
	    <s:select id="departTypePop" name="departTypePop" list='#application.CodeTable.getCodes("1000")' listKey="CodeKey" listValue="Value"/>
	    <s:text name="MNG04_selectAll" id="MNG04_selectAll"/>
	    <s:text name="global.page.select" id="globalSelect"/>
	    <!-- 系统配置项产品入库使用价格 -->
        <input type="hidden" id="sysConfigUsePrice" value='<s:property value='sysConfigUsePrice'/>'>
        <!-- 云POS调入是否存在编辑操作 -->
        <input type="hidden" id="inputEditFlag" value='<s:property value='inputEditFlag'/>'>
	</div>
    <div id="div_main" class="panel ui-corner-all">
        <div class="panel-header">
            <div class="clearfix">
                <span class="breadcrumb left">
	            <span class="ui-icon icon-breadcrumb"></span><s:text name="MNG04_popTitle"/></span>
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
                        <s:text name="MNG04_general"/>
                    </strong>
                </div>
                <div class="section-content">
                    <table class="detail">
                        <tr>
                            <s:if test='operateType.equals("newBill")'>
	                            <th><s:text name="MNG04_date"/></th>
	                            <td><s:property value="applyDate"/></td>
	                            <th><s:text name="MNG04_BA"/></th>
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
	                            <th><s:text name="MNG04_date"/></th>
	                            <td><s:property value="productAllocationMainData.Date"/></td>
	                            <th><s:text name="MNG04_BA"/></th>
	                            <td><s:property value="productAllocationMainData.EmployeeName"/></td>
                            </s:else>
                        </tr>
                        <tr>
                            <s:if test='operateType.equals("newBill")'>
	                            <th><s:text name="MNG04_outDepart"/></th>
	                            <td>
	                                <label id="outOrgName" class="left"></label>
	                                <a class="add right" onclick="binOLWSMNG04_02.openOutDepartBox(this);">
	                                    <span class="ui-icon icon-search"></span>
	                                    <span class="button-text"><s:text name="MNG04_lblSelect"/></span>
	                                </a>
	                            </td>
	                            <th></th>
	                            <td></td>
                            </s:if>
                            <s:else>
                                <th><s:text name="MNG04_outDepart"/></th>
                                <td><s:property value="productAllocationMainData.DepartCodeNameOut"/></td>
                                <th></th>
                                <td></td>
                            </s:else>
                        </tr>
                    </table>
                </div>
            </div>
            <div id="mydetail" class="section">
	            <div class="section-header">
	                <strong><span class="ui-icon icon-ttl-section-list"></span>
	                <s:text name="MNG04_lbldetail"/></strong>
	            </div>
                <div class="section-content">
                    <s:if test='operateType.equals("newBill") || operateType.equals("2")'>
	                    <div class="toolbar clearfix">
	                        <span class="left">
	                            <input id="allSelect" type="checkbox"  class="checkbox" onclick="binOLWSMNG04_02.selectAll();"/>
	                            <s:text name="MNG04_btnAllSelect"/>
	                            <a class="add" onclick="binOLWSMNG04_02.addNewLine();">
	                                <span class="ui-icon icon-add"></span>
	                                <span class="button-text"><s:text name="MNG04_btnAdd"/></span>
	                            </a>
	                            <a class="delete" onclick="binOLWSMNG04_02.deleterow();">
	                                <span class="ui-icon icon-delete"></span>
	                                <span class="button-text"><s:text name="MNG04_btnDelete"/></span>
	                            </a>
	                        </span>
	                    </div>
                    </s:if>
                    <div style="width:100%;overflow-x:scroll;">
                        <table id="mainTable" cellpadding="0" cellspacing="0" border="0" width="100%">
                            <thead>
                                <tr>
                                    <s:if test='operateType.equals("newBill") || operateType.equals("2")'>
	                                   <th class="tableheader" width="5%"><s:text name="MNG04_lblSelect"/></th>
	                                </s:if>
	                                <s:else>
	                                   <th class="tableheader" width="5%"><s:text name="MNG04_no"/></th>
	                                </s:else>
	                                <th class="tableheader" width="10%"><s:text name="MNG04_unitCode"/></th>
	                                <th class="tableheader" width="10%"><s:text name="MNG04_barcode"/></th>
	                                <th class="tableheader" width="20%"><s:text name="MNG04_prodcutName"/></th>
	                                <th class="tableheader" width="8%"><s:text name="MNG04_price"/></th>
	                                <s:if test='operateType.equals("newBill") || operateType.equals("2")  || operateType.equals("70")  || operateType.equals("80")'>
	                                   <th class="tableheader" width="8%"><s:text name="MNG04_stock"/></th>
	                                </s:if>
	                                <s:if test='productAllocationMainData.ShowInQuantity.equals("YES")'>
                                        <th class="tableheader" width="8%"><s:text name="MNG04_applyQuantity"/></th>
                                        <th class="tableheader" width="8%"><s:text name="MNG04_applyAmount"/></th>
                                        <th class="tableheader" width="8%"><s:text name="MNG04_inQuantity"/></th>
                                        <th class="tableheader" width="8%"><s:text name="MNG04_inAmount"/></th>
	                                </s:if>
	                                <s:else>
                                        <th class="tableheader" width="12%"><s:text name="MNG04_quantity"/></th>
                                        <th class="tableheader" width="10%"><s:text name="MNG04_amount"/></th>
	                                </s:else>
	                                <th class="tableheader" width="20%"><s:text name="MNG04_reason"/></th>
	                                <th style="display:none">
                                </tr>
                            </thead>
                            <tbody id="databody">
                                <s:if test='!operateType.equals("newBill")'>
	                                <s:iterator value="productAllocationDetailData" status="status">
	                                    <tr id="dataRow<s:property value='#status.index+1'/>" class="<s:if test='#status.odd'>odd</s:if><s:else>even</s:else>">
                                            <td id="dataTd1" class="center">
                                                <s:if test='operateType.equals("2")'>
	                                               <input id="chkbox" type="checkbox" onclick="binOLWSMNG04_02.changechkbox(this);">
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
	                                        <s:if test='operateType.equals("newBill") || operateType.equals("2") || operateType.equals("70") || operateType.equals("80")'>
	                                           <td id="dataTDStock" class="alignRight"><s:property value="StockQuantity"/></td>
	                                        </s:if>
	                                        <td id="dataTd6" class="alignRight">
	                                            <s:if test='null != Quantity'>
	                                               <s:if test='operateType.equals("2")  || "70".equals(operateType) || "80".equals(operateType)'>
	                                                   <input type="text"  id="quantityArr" name="quantityArr" style="width:120px;text-align:right;" class="hide"  size="10" maxlength="4" value='<s:property value="Quantity"/>' onchange="binOLWSMNG04_02.changeCount(this);"/>
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
	                                        <s:if test='productAllocationMainData.ShowInQuantity.equals("YES")'>
	                                           <td class="alignRight">
	                                               <s:if test='null != InQuantity'>
	                                                   <s:text name="format.number"><s:param value="InQuantity"></s:param></s:text>
	                                               </s:if>
	                                               <s:else>0</s:else>
	                                           </td>
	                                           <td class="alignRight">
	                                               <s:if test='null!=Price && null != InQuantity'>
	                                                   <s:text name="format.price"><s:param value="Price*InQuantity"></s:param></s:text>
	                                               </s:if>
	                                               <s:else>0.00</s:else>
	                                           </td>
	                                        </s:if>
	                                        <td id="dataTd9">
	                                            <s:if test='operateType.equals("2")  || "70".equals(operateType) || "80".equals(operateType)'>
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
	                        <s:if test='productAllocationMainData.ShowInQuantity.equals("YES")'>
                                <td class="center"><s:text name="MNG04_applyTotalQuantity"/></td><%-- 申请总数量 --%>
                                <td class="center"><s:text name="MNG04_applyTotalAmount"/></td><%-- 申请总金额--%>
                                <td class="center"><s:text name="MNG04_inTotalQuantity"/></td><%-- 调入总数量 --%>
                                <td class="center"><s:text name="MNG04_inTotalAmount"/></td><%-- 调入总金额--%>
	                        </s:if>
	                        <s:else>
                                <td class="center"><s:text name="MNG04_totalQuantity"/></td><%-- 总数量 --%>
                                <td class="center"><s:text name="MNG04_totalAmount"/></td><%-- 总金额--%>
	                        </s:else>
                        </tr>
                        <tr>
                            <td class="center">
                                <%-- 申请总数量 --%>
                                <span id="totalQuantity">
                                    <s:if test='null!=productAllocationMainData.TotalQuantity'>
                                        <s:text name="format.number"><s:param value="productAllocationMainData.TotalQuantity"></s:param></s:text>
                                    </s:if>
                                    <s:else>0</s:else>
                                </span>
                            </td>
                            <td class="center">
                                <%-- 申请总金额--%>
                                <span id="totalAmount">
                                    <s:if test='null!=productAllocationMainData.TotalAmount'>
                                        <s:text name="format.price"><s:param value="productAllocationMainData.TotalAmount"></s:param></s:text>
                                    </s:if>
                                    <s:else>0.00</s:else>
                                </span>
                            </td>
                            <s:if test='productAllocationMainData.ShowInQuantity.equals("YES")'>
                                <td class="center">
                                    <%-- 调入总数量 --%>
                                    <s:if test='null!=productAllocationMainData.InTotalQuantity'>
                                        <s:text name="format.number"><s:param value="productAllocationMainData.InTotalQuantity"></s:param></s:text>
                                    </s:if>
                                    <s:else>0</s:else>
                                </td>
                                <td class="center">
                                    <%-- 调入总金额 --%>
                                    <s:if test='null!=productAllocationMainData.InTotalAmount'>
                                        <s:text name="format.price"><s:param value="productAllocationMainData.InTotalAmount"></s:param></s:text>
                                    </s:if>
                                    <s:else>0.00</s:else>
                                </td>
                            </s:if>
                        </tr>
                    </table>
                    <hr class="space" />
	                <div style="display:none">
	                    <input type="hidden"  id="brandInfoId"  value='<s:property value="#session.userinfo.BIN_BrandInfoID"/>'>
	                    <input type="hidden"  id="operateType"  name="operateType" value='<s:property value="operateType"/>'>
	                    <s:if test='operateType.equals("newBill")'>
	                        <input type="hidden" id="inOrganizationID" name="inOrganizationID" value='<s:property value="inOrganizationID"/>'/>
	                        <input type="hidden" id="inDepotID" name="inDepotID" value='<s:property value="inDepotID"/>'/>
	                        <input type="hidden" id="inLogicDepotID" name="inLogicDepotID" value='<s:property value="inLogicDepotID"/>'/>
	                        <input type="hidden" id="outOrganizationID" name="outOrganizationID">
	                    </s:if>
	                    <s:else>
	                        <input type="hidden" id="inOrganizationID" name="inOrganizationID" value='<s:property value="productAllocationMainData.BIN_OrganizationIDIn"/>'/>
	                        <input type="hidden" id="inDepotID" name="inDepotID" value='<s:property value="productAllocationMainData.BIN_InventoryInfoIDIn"/>'/>
	                        <input type="hidden" id="inLogicDepotID" name="inLogicDepotID" value='<s:property value="productAllocationMainData.BIN_LogicInventoryInfoIDIn"/>'/>
	                        <input type="hidden" id="outOrganizationID" name="outOrganizationID" value='<s:property value="productAllocationMainData.BIN_OrganizationIDOut"/>'/>
                            <input type="hidden" id="inventoryInfoIDIn" name="inventoryInfoIDIn" value='<s:property value="productAllocationMainData.BIN_InventoryInfoIDIn"/>'/>
                            <input type="hidden" id="logicInventoryInfoIDIn" name="logicInventoryInfoIDIn" value='<s:property value="productAllocationMainData.BIN_LogicInventoryInfoIDIn"/>'/>
	                    </s:else>
                        <s:if test='operateType.equals("2")'>
	                        <input type="hidden" id="rowNumber" value="<s:property value='productAllocationDetailData.size()'/>"/>
	                        <input type="hidden" id="inTestType" value="<s:property value='productAllocationDetailData.TestType'/>">
	                        <input type="hidden" id="updateTime" name="updateTime" value="<s:property value="productAllocationMainData.UpdateTime"/>">
	                        <input type="hidden" id="modifyCount" name="modifyCount" value="<s:property value="productAllocationMainData.ModifyCount"/>">
	                        <input type="hidden" id="verifiedFlag" name="verifiedFlag" value="<s:property value="productAllocationMainData.VerifiedFlag"/>">
	                        <s:hidden id="productAllocationID" name="productAllocationID" value="%{#request.productAllocationID}"></s:hidden>
                        </s:if>
                        <s:else>
                            <input type="hidden" id="rowNumber" value="0"/>
                        </s:else>
                        <input type="hidden" id="entryID" name="entryID" value='<s:property value="productAllocationMainData.WorkFlowID"/>'/>
                        <input type="hidden" id="actionID" name="actionID"/>
                        <input type="hidden" id="entryid" name="entryid"/>
                        <input type="hidden" id="actionid" name="actionid"/>
	                </div>
                    <div class="center clearfix">
                        <s:if test='operateType.equals("newBill") || operateType.equals("2")'>
	                        <button class="save" type="button" onclick="binOLWSMNG04_02.submitSave();">
	                            <span class="ui-icon icon-save"></span>
	                            <span class="button-text"><s:text name="MNG04_btnSave"/></span>
	                        </button>
	                        <button class="confirm" type="button" onclick="binOLWSMNG04_02.submitSend();">
	                            <span class="ui-icon icon-confirm"></span>
	                            <span class="button-text"><s:text name="MNG04_btnOK"/></span>
	                        </button>
	                        <s:if test='operateType.equals("2")'>
                                <button id="btn-icon-edit-big" class="confirm" onclick="BINOLSTBIL18.modifyForm();return false;">
                                    <span class="ui-icon icon-edit-big"></span>
                                    <%-- 修改 --%>
                                    <span class="button-text"><s:text name="os.edit"/></span>
                                </button>
                        </s:if>
                        </s:if>
                        <s:elseif test='@com.cherry.cm.core.CherryConstants@OPERATE_BG.equals(operateType)'>
                            <jsp:include page="/WEB-INF/jsp/common/osActionButton2.inc.jsp" flush="true" />
                        </s:elseif>
                        <button id="btnReturn" class="close" onclick="binOLWSMNG04_02.back();return false;" style="display:none;"><span class="ui-icon icon-back"></span>
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
<form action="BINOLWSMNG04_initDetail" id="productAllocationDetailUrl" method="post">
    <input name="csrftoken" type="hidden"/>
    <s:hidden name="productAllocationID" value="%{#request.productAllocationID}"></s:hidden>
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
<%-- ================== 工作流提示弹出窗口 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/popOSDialog.jsp" flush="true" />
<%-- ==================  工作流提示弹出窗口End ======================== --%>
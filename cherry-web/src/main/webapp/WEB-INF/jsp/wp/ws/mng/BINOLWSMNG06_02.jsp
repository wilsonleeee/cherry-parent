<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%--自由盘点 新增/修改/详细--%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<script type="text/javascript" src="/Cherry/js/common/popOSDialog.js"></script>
<script type="text/javascript" src="/Cherry/js/wp/ws/mng/BINOLWSMNG06_02.js?v=20170122"></script>
<s:i18n name="i18n.wp.BINOLWSMNG06">
<div class="main container clearfix">
	<div class="hide">
        <input type="hidden" id="counterCode" value='<s:property value="counterCode"/>'/>
	    <input type="hidden" id="currentMenuID" name="currentMenuID" value="BINOLWSMNG06_02"/>
	    <input type="hidden" id="pageId" name="pageId" value="BINOLWSMNG06_02"/>
	    <s:url id="save_url" value="/ws/BINOLWSMNG06_save"/>
	    <s:url id="submit_url" value="/ws/BINOLWSMNG06_submit"/>
        <s:url id="url_getStockCount" value="/st/BINOLSTIOS06_getStockCount" />
        <span id ="s_getStockCount" >${url_getStockCount}</span>
	    <a id="saveUrl" href="${save_url}"></a>
	    <a id="submitURL" href="${submit_url}"></a>
	    <s:url id="url_startStocktaking" value="/ws/BINOLWSMNG06_STOCKTAKING" />
        <span id ="s_startStocktaking" style="display:none">${url_startStocktaking}</span>
        <s:url id = "url_refreshAjax" value="/common/BINOLCM00_refreshSessionRequest"/>
        <span id = "s_refreshAjax" style="display:none">${url_refreshAjax}</span>
	    <s:select id="departTypePop" name="departTypePop" list='#application.CodeTable.getCodes("1000")' listKey="CodeKey" listValue="Value"/>
	    <s:text name="MNG06_selectAll" id="MNG06_selectAll"/>
	    <s:text name="global.page.select" id="globalSelect"/>
        <!-- 系统配置项产品盘点使用价格 -->
        <input type="hidden" id="sysConfigUsePrice" value='<s:property value='sysConfigUsePrice'/>'>
        <span id="MNG06_dialog_reset"><s:text name="MNG06_dialog_reset" /></span>
        <span id="MNG06_dialog_nochange"><s:text name="MNG06_dialog_nochange" /></span>
        <span id="MNG06_dialog_continue"><s:text name="MNG06_dialog_continue" /></span>
	</div>
    <div id="div_main" class="panel ui-corner-all">
        <div class="panel-header">
            <div class="clearfix">
                <span class="breadcrumb left">
	            <span class="ui-icon icon-breadcrumb"></span><s:text name="MNG06_popTitle"/></span>
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
                <%--防止有button的form在text框输入后按Enter键后自动submit --%>
                <button type="submit" onclick="return false;" class="hide"></button>
            <div class="section">
                <div class="section-header">
                    <strong>
                        <span class="ui-icon icon-ttl-section-info"></span>
                        <s:text name="MNG06_general"/>
                    </strong>
                </div>
                <div class="section-content">
                    <table class="detail">
                        <tr>
                            <s:if test='operateType.equals("newBill")'>
	                            <th><s:text name="MNG06_date"/></th>
	                            <td><s:property value="applyDate"/></td>
	                            <th><s:text name="MNG06_BA"/></th>
	                            <td>
                                    <s:if test='null != counterBAList && counterBAList.size>0'>
                                        <s:select name="tradeEmployeeID" list='counterBAList' listKey="baInfoId" listValue="baName" headerKey="" headerValue="%{globalSelect}" />
                                    </s:if>
                                    <s:else>
                                        <s:property value="#session.userinfo.employeeName"/>
                                        <s:hidden name="tradeEmployeeID" value="%{#session.userinfo.BIN_EmployeeID}"></s:hidden>
                                    </s:else>
	                            </td>
                            </s:if>
                            <s:else>
	                            <th><s:text name="MNG06_date"/></th>
	                            <td><s:property value="takingInfo.Date"/></td>
	                            <th><s:text name="MNG06_BA"/></th>
	                            <td><s:property value="takingInfo.EmployeeName"/></td>
                            </s:else>
                        </tr>
                    </table>
                </div>
            </div>
            <div id="mydetail" class="section">
	            <div class="section-header">
	                <strong><span class="ui-icon icon-ttl-section-list"></span>
	                <s:text name="MNG06_lbldetail"/></strong>
	            </div>
                <div class="section-content">
                    <s:if test='operateType.equals("newBill") || operateType.equals("2")'>
                        <div id="showToolbar" class="hide">
		                    <div class='toolbar clearfix'>
		                        <span class="left">
                                    <s:if test='addType.equals("all")'>
                                        <a id="btnStockTaking" class="add" onclick="binOLWSMNG06_02.stockTaking();">
                                            <span class="ui-icon icon-add"></span>
                                            <span class="button-text"><s:text name="MNG06_btnStockTaking"/></span>
                                        </a>
                                        <a id="btnCancelStockTaking" class="delete" style="display:none;" onclick="binOLWSMNG06_02.cancelStockTaking();">
                                            <span class="ui-icon icon-delete"></span>
                                            <span class="button-text"><s:text name="MNG06_btnCancelStockTaking"/></span>
                                        </a>
                                    </s:if>
                                    <s:else>
                                        <input id="allSelect" type="checkbox"  class="checkbox" onclick="binOLWSMNG06_02.selectAll();"/>
                                        <s:text name="MNG06_btnAllSelect"/>
                                        <a class="add" onclick="binOLWSMNG06_02.addNewLine();">
                                            <span class="ui-icon icon-add"></span>
                                            <span class="button-text"><s:text name="MNG06_btnAdd"/></span>
                                        </a>
                                        <a class="delete" onclick="binOLWSMNG06_02.deleterow();">
                                            <span class="ui-icon icon-delete"></span>
                                            <span class="button-text"><s:text name="MNG06_btnDelete"/></span>
                                        </a>
                                    </s:else>
		                        </span>
		                    </div>
	                    </div>
                    </s:if>
                    <div style="width:100%;overflow-x:scroll;">
                        <table id="mainTable" cellpadding="0" cellspacing="0" border="0" width="100%">
                            <thead>
                                <tr>
                                    <s:if test='operateType.equals("newBill") || operateType.equals("2")'>
                                        <s:if test='addType.equals("all")'>
                                            <th class="tableheader" width="5%"><s:text name="MNG06_no"/></th>
                                        </s:if>
                                        <s:else>
                                            <th class="tableheader" width="5%"><s:text name="MNG06_lblSelect"/></th>
                                        </s:else>
	                                </s:if>
	                                <s:else>
	                                   <th class="tableheader" width="5%"><s:text name="MNG06_no"/></th>
	                                </s:else>
	                                <th class="tableheader" width="10%"><s:text name="MNG06_unitCode"/></th>
	                                <th class="tableheader" width="10%"><s:text name="MNG06_barcode"/></th>
	                                <th class="tableheader" width="20%"><s:text name="MNG06_prodcutName"/></th>
	                                <th class="tableheader" width="8%"><s:text name="MNG06_price"/></th>
	                                <s:if test='"0".equals(blindFlag)'>
		                                <s:if test='operateType.equals("newBill") || operateType.equals("2")'>
		                                   <th class="tableheader" width="8%"><s:text name="MNG06_bookCount"/></th>
		                                </s:if>
	                                </s:if>
	                                <th class="tableheader" width="12%"><s:text name="MNG06_quantity"/></th>
                                    <s:if test='"0".equals(blindFlag)'>
                                        <th class="tableheader" width="10%"><s:text name="MNG06_gainQuantity"/></th>
                                    </s:if>
                                    <s:if test='"0".equals(blindFlag)'>
                                        <th class="tableheader" width="10%"><s:text name="MNG06_amount"/></th>
	                                </s:if>
	                                <th class="tableheader" width="20%"><s:text name="MNG06_reason"/></th>
	                                <th style="display:none"></th>
                                </tr>
                            </thead>
                            <tbody id="databody">
                                <s:if test='!operateType.equals("newBill")'>
	                                <s:iterator value="takingDetailList" status="status">
	                                    <tr id="dataRow<s:property value='#status.index+1'/>" class="<s:if test='#status.odd'>odd</s:if><s:else>even</s:else>">
                                            <td id="dataTd1" class="center">
                                                <s:if test='operateType.equals("2") && !addType.equals("all")'>
	                                               <input id="chkbox" type="checkbox" onclick="binOLWSMNG06_02.changechkbox(this);">
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
	                                        <s:if test='"0".equals(blindFlag)'>
		                                        <s:if test='operateType.equals("newBill") || operateType.equals("2")'>
		                                           <td id="dataTDStock" class="alignRight"><s:property value="StockQuantity"/></td>
		                                        </s:if>
	                                        </s:if>
	                                        <td id="dataTd6" class="alignRight">
	                                            <s:if test='null != RealQuantity'>
	                                               <s:if test='operateType.equals("2")'>
	                                                   <s:if test='"0".equals(HandleType)'><%--不做处理 --%>
                                                           <input type="text"  id="quantityArr" style="width:120px;text-align:right;" class="hide"  size="10" maxlength="4" value='' onchange="binOLWSMNG06_02.changeCount(this);"/>
                                                            <div id="hideQuantiyArr">&nbsp;</div>
	                                                   </s:if>
	                                                   <s:else>
	                                                       <input type="text"  id="quantityArr" style="width:120px;text-align:right;" class="hide"  size="10" maxlength="4" value='<s:property value="RealQuantity"/>' onchange="binOLWSMNG06_02.changeCount(this);"/>
	                                                        <div id="hideQuantiyArr">
	                                                            <s:text name="format.number"><s:param value="RealQuantity"></s:param></s:text>
	                                                        </div>
	                                                   </s:else>
	                                               </s:if>
	                                               <s:else>
	                                                   <s:text name="format.number"><s:param value="RealQuantity"></s:param></s:text>
	                                               </s:else>	                                                
	                                            </s:if>
	                                            <s:else>&nbsp;</s:else>
	                                        </td>
	                                        <s:if test='"0".equals(blindFlag)'>
		                                        <td id="dataTdGainStock" class="alignRight">
		                                        <s:if test='"0".equals(HandleType) && operateType.equals("2")'>
                                                    &nbsp;
		                                        </s:if>
		                                        <s:else>
                                                    <s:text name="format.number"><s:param value="GainQuantity"></s:param></s:text>
		                                        </s:else>
		                                        </td>
	                                        </s:if>
	                                        <s:if test='"0".equals(blindFlag)'>
		                                        <td id="money" class="alignRight">
		                                            <s:if test='null!=Price && null != RealQuantity'>
                                                         <s:if test='"0".equals(HandleType) && operateType.equals("2")'>
                                                         0.00
                                                         </s:if>
                                                         <s:else>
                                                            <s:text name="format.price"><s:param value="Price*RealQuantity"></s:param></s:text>
                                                         </s:else>
		                                            </s:if>
		                                            <s:else>&nbsp;</s:else>
		                                        </td>
	                                        </s:if>
	                                        <td id="dataTd9">
	                                            <s:if test='operateType.equals("2")'>
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
	                                             <%--
	                                            <input type="hidden" id="priceUnitArr<s:property value='#status.index+1'/>" name="priceUnitArr" value="<s:property value='Price'/>"/>
	                                            <input type="hidden"  id="productVendorIDArr" name="productVendorIDArr" value="<s:property value='BIN_ProductVendorID'/>"/>
	                                             --%>
	                                            <%--<input type="hidden" name="prtVendorId" value="<s:property value='BIN_ProductVendorID'/>"/>--%>
	                                            <input type="hidden" name="bookCountArr" id="bookCountArr" value="<s:property value='StockQuantity'/>"></input>
	                                            <input type="hidden" name="gainCountArr" id="gainCountArr" value="<s:property value='GainQuantity'/>"></input>
	                                            <input type="hidden" name="productVendorIDArr" id="productVendorIDArr" value="<s:property value='BIN_ProductVendorID'/>"></input>
	                                            <%--<input type="hidden" name="hasproductflagArr" id="hasproductflagArr" value="0"></input>--%>
	                                            <input type="hidden" name="priceArr" id="priceArr" value="<s:property value='Price'/>"></input>
	                                            <input type="hidden" name="htArr" id="htArr" value="<s:property value='HandleType'/>"></input>
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
	                        <td class="center"><s:text name="MNG06_totalQuantity"/></td><%-- 总数量 --%>
	                        <td class="center"><s:text name="MNG06_totalAmount"/></td><%-- 总金额--%>
                        </tr>
                        <tr>
	                        <td class="center">
	                            <%-- 实盘总数量 --%>
	                            <span id="totalQuantity">
		                            <s:if test='null!=takingInfo.SumrealQuantity'>
		                                <s:text name="format.number"><s:param value="takingInfo.SumrealQuantity"></s:param></s:text>
		                            </s:if>
		                            <s:else>0</s:else>
	                            </span>
	                        </td>
	                        <td class="center">
	                            <%-- 实盘总金额--%>
	                            <span id="totalAmount">
		                            <s:if test='null!=takingInfo.SumrealAmount'>
		                                <s:text name="format.price"><s:param value="takingInfo.SumrealAmount"></s:param></s:text>
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
	                    <input type="hidden" id=blindFlag name="blindFlag" value="<s:property value='blindFlag' />"/>
	                    <input type="hidden" id="BatchStockTakingFalse" name="isBatchStockTaking" value="false"/>
	                    <s:if test='operateType.equals("newBill")'>
	                        <input type="hidden" id="organizationId" name="organizationId" value='<s:property value="organizationId"/>'/>
	                        <input type="hidden" id="depotId" name="depotId" value='<s:property value="depotId"/>'/>
	                        <input type="hidden" id="logicinventId" name="logicinventId" value='<s:property value="logicinventId"/>'/>
	                    </s:if>
	                    <s:else>
	                        <input type="hidden" id="organizationId" name="organizationId" value='<s:property value="takingInfo.BIN_OrganizationID"/>'/>
	                        <input type="hidden" id="depotId" name="depotId" value='<s:property value="takingInfo.BIN_InventoryInfoID"/>'/>
	                        <input type="hidden" id="logicinventId" name="logicinventId" value='<s:property value="takingInfo.BIN_LogicInventoryInfoID"/>'/>
                        </s:else>
                        <s:if test='operateType.equals("2")'>
	                        <input type="hidden" id="rowNumber" value="<s:property value='takingDetailList.size()'/>"/>
	                        <input type="hidden" id="inTestType" value="<s:property value='takingDetailList.TestType'/>">
	                        <input type="hidden" id="updateTime" name="updateTime" value="<s:property value="takingInfo.UpdateTime"/>">
	                        <input type="hidden" id="modifyCount" name="modifyCount" value="<s:property value="takingInfo.ModifyCount"/>">
	                        <input type="hidden" id="verifiedFlag" name="verifiedFlag" value="<s:property value="takingInfo.VerifiedFlag"/>">
	                        <s:hidden id="stockTakingId" name="stockTakingId" value="%{#request.stockTakingId}"></s:hidden>
                        </s:if>
                        <s:else>
                            <input type="hidden" id="rowNumber" value="0"/>
                        </s:else>
                        <input type="hidden" id="entryID" name="entryID" value='<s:property value="takingInfo.WorkFlowID"/>'/>
                        <input type="hidden" id="actionID" name="actionID"/>
                        <input type="hidden" id="entryid" name="entryid"/>
                        <input type="hidden" id="actionid" name="actionid"/>
                        <input type="hidden" id="addType"  name="addType"  value='<s:property value="addType"/>'/>
	                </div>
                    <div class="center clearfix">
                        <s:if test='operateType.equals("newBill") || operateType.equals("2")'>
	                        <button type="button" class="save" onclick="binOLWSMNG06_02.submitSave();">
	                            <span class="ui-icon icon-save"></span>
	                            <span class="button-text"><s:text name="MNG06_btnSave"/></span>
	                        </button>
	                        <button type="button" class="confirm" onclick="binOLWSMNG06_02.submitSend();">
	                            <span class="ui-icon icon-confirm"></span>
	                            <span class="button-text"><s:text name="MNG06_btnOK"/></span>
	                        </button>
	                        <s:if test='operateType.equals("2")'>
                                <button type="button" id="btn-icon-edit-big" class="confirm" onclick="binOLWSMNG06_02.modifyForm();return false;">
                                    <span class="ui-icon icon-edit-big"></span>
                                    <%-- 修改 --%>
                                    <span class="button-text"><s:text name="os.edit"/></span>
                                </button>
                        </s:if>
                        </s:if>
                        <s:elseif test='@com.cherry.cm.core.CherryConstants@OPERATE_BG.equals(operateType)'>
                            <jsp:include page="/WEB-INF/jsp/common/osActionButton2.inc.jsp" flush="true" />
                        </s:elseif>
                        <button type="button" id="btnReturn" class="close" onclick="binOLWSMNG06_02.back();return false;" style="display:none;"><span class="ui-icon icon-back"></span>
                            <%--返回 --%>
                            <span class="button-text"><s:text name="global.page.back"/></span>
                        </button>
                        <button type="button"  class="close" onclick="window.close();">
                            <span class="ui-icon icon-close"></span>
                            <span class="button-text"><s:text name="global.page.close"/></span>
                        </button>
                    </div>
                </div>
            </div>
            <div class="hide" id="dialogInit"></div>
            </form>
            <s:if test='operateType.equals("newBill") || operateType.equals("2")'>
                <br/>
                <span class="highlight"><s:text name="MNG06_snow"/></span><s:text name="MNG06_saveTip"/>
            </s:if>
    </div>
</div>
</div>
</s:i18n>
<form action="BINOLWSMNG06_initDetail" id="proStocktakeDetailUrl" method="post">
    <input name="csrftoken" type="hidden"/>
    <s:hidden name="stockTakingId" value="%{#request.stockTakingId}"></s:hidden>
</form>
<div id="errmessage" style="display:none">
    <input type="hidden" id="errmsg_EST00006" value='<s:text name="EST00006"/>'/>
    <input type="hidden" id="errmsg_EST00007" value='<s:text name="EST00007"/>'/>
    <input type="hidden" id="errmsg_EST00008" value='<s:text name="EST00008"/>'/><!--有无效的数据行，请补充完整或删除该行。-->
    <input type="hidden" id="errmsg_EST00009" value='<s:text name="EST00009"/>'/>
    <input type="hidden" id="errmsg_EST00013" value='<s:text name="EST00013"/>'/>
    <input type="hidden" id="errmsg_EST00015" value='<s:text name="EST00015"/>'/>
    <input type="hidden" id="errmsg_EST00022" value='<s:text name="EST00022"/>'/>
    <input type="hidden" id="errmsg_EST00024" value='<s:text name="EST00024"/>'/>
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
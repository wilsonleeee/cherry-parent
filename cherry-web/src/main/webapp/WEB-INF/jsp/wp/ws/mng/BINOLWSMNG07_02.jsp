<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%--盘点申请  新增--%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<script type="text/javascript" src="/Cherry/js/common/popOSDialog.js"></script>
<script type="text/javascript" src="/Cherry/js/wp/ws/mng/BINOLWSMNG07_02.js"></script>
<s:i18n name="i18n.wp.BINOLWSMNG07">
<div class="main container clearfix">
	<div class="hide">
        <input type="hidden" id="counterCode" value='<s:property value="counterCode"/>'/>
	    <input type="hidden" id="currentMenuID" name="currentMenuID" value="BINOLWSMNG07_02"/>
	    <input type="hidden" id="pageId" name="pageId" value="BINOLWSMNG07_02"/>
	    <s:url id="save_url" value="/ws/BINOLWSMNG07_save"/>
	    <s:url id="submit_url" value="/ws/BINOLWSMNG07_submit"/>
        <s:url id="url_getStockCount" value="/st/BINOLSTIOS06_getStockCount" />
        <span id ="s_getStockCount" >${url_getStockCount}</span>
	    <a id="saveUrl" href="${save_url}"></a>
	    <a id="submitURL" href="${submit_url}"></a>
	    <s:url id="url_takingRequest" value="/ws/BINOLWSMNG07_takingRequest" />
        <span id ="s_takingRequest" style="display:none">${url_takingRequest}</span>
        <s:url id="url_givenRequest" value="/ws/BINOLWSMNG07_givenRequest" />
        <span id ="s_givenRequest" style="display:none">${url_givenRequest}</span>
        <s:url id = "url_refreshAjax" value="/common/BINOLCM00_refreshSessionRequest"/>
        <span id = "s_refreshAjax" style="display:none">${url_refreshAjax}</span>
	    <s:select id="departTypePop" name="departTypePop" list='#application.CodeTable.getCodes("1000")' listKey="CodeKey" listValue="Value"/>
	    <s:text name="MNG07_selectAll" id="MNG07_selectAll"/>
	    <s:text name="global.page.select" id="globalSelect"/>
        <!-- 系统配置项产品盘点使用价格 -->
        <input type="hidden" id="sysConfigUsePrice" value='<s:property value='sysConfigUsePrice'/>'>
        <span id="MNG07_dialog_title"><s:text name="MNG07_dialog_title" /></span>
        <span id="MNG07_dialog_reset"><s:text name="MNG07_dialog_reset" /></span>
        <span id="MNG07_dialog_nochange"><s:text name="MNG07_dialog_nochange" /></span>
        <span id="MNG07_dialog_continue"><s:text name="MNG07_dialog_continue" /></span>
	</div>
    <div id="div_main" class="panel ui-corner-all">
        <div class="panel-header">
            <div class="clearfix">
                <span class="breadcrumb left">
	            <span class="ui-icon icon-breadcrumb"></span><s:text name="MNG07_popTitle"/></span>
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
                        <s:text name="MNG07_general"/>
                    </strong>
                </div>
                <div class="section-content">
                    <table class="detail">
                        <tr>
                        	<!-- 新增盘点申请 -->
                            <s:if test='operateType.equals("newBill")'>
	                            <th><s:text name="MNG07_date"/></th>
	                            <td><s:property value="applyDate"/></td>
	                            <th><s:text name="MNG07_BA"/></th>
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
                            <!-- 修改/详细 -->
                            <s:else>
	                            <th><s:text name="MNG07_date"/></th>
	                            <td><s:property value="takingInfo.Date"/></td>
	                            <th><s:text name="MNG07_BA"/></th>
	                            <td><s:property value="takingInfo.EmployeeName"/></td>
	                            <!-- 下单员工 -->
	                            <input type="hidden" id="tradeEmployeeID" name="tradeEmployeeID" value='<s:property value="takingInfo.BIN_EmployeeID"/>'/>
                            </s:else>
                        </tr>
                    </table>
                </div>
            </div>
            <div id="mydetail" class="section">
	            <div class="section-header">
	                <strong><span class="ui-icon icon-ttl-section-list"></span>
	                <s:text name="MNG07_lbldetail"/></strong>
	            </div>
                <div class="section-content">
                	<!-- 盘点申请新增（newBill）/修改(2) -->
                    <s:if test='operateType.equals("newBill") || operateType.equals("2")'>
                        <div id="showToolbar" class="hide">
		                    <div class='toolbar clearfix'>
		                        <span class="left">
		                        	<!-- 全盘 -->
                                    <s:if test='addType.equals("all")'>
                                        <a id="btnTakingRequest" class="add" onclick="binOLWSMNG07_02.takingRequest();">
                                            <span class="ui-icon icon-add"></span>
                                            <span class="button-text"><s:text name="MNG07_btnTakingRequest"/></span>
                                        </a>
                                        <a id="btnCancelStockTaking" class="delete" style="display:none;" onclick="binOLWSMNG07_02.cancelStockTaking();">
                                            <span class="ui-icon icon-delete"></span>
                                            <span class="button-text"><s:text name="MNG07_btnCancelStockTaking"/></span>
                                        </a>
                                    </s:if>
                                    <!-- 指定盘点 -->
                                    <s:elseif test='addType.equals("given")'>
                                        <a id="btnGivenRequest" class="add" onclick="binOLWSMNG07_02.givenRequest();">
                                            <span class="ui-icon icon-add"></span>
                                            <span class="button-text"><s:text name="MNG07_btnGivenRequest"/></span>
                                        </a>
                                        <a id="btnCancelStockTaking" class="delete" style="display:none;" onclick="binOLWSMNG07_02.cancelStockTaking();">
                                            <span class="ui-icon icon-delete"></span>
                                            <span class="button-text"><s:text name="MNG07_btnCancelStockTaking"/></span>
                                        </a>
                                    </s:elseif>
                                    <!-- 商品盘点 -->
                                    <s:else>
                                        <input id="allSelect" type="checkbox"  class="checkbox" onclick="binOLWSMNG07_02.selectAll();"/>
                                        <s:text name="MNG07_btnAllSelect"/>
                                        <a class="add" onclick="binOLWSMNG07_02.addNewLine();">
                                            <span class="ui-icon icon-add"></span>
                                            <span class="button-text"><s:text name="MNG07_btnAdd"/></span>
                                        </a>
                                        <a class="delete" onclick="binOLWSMNG07_02.deleterow();">
                                            <span class="ui-icon icon-delete"></span>
                                            <span class="button-text"><s:text name="MNG07_btnDelete"/></span>
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
                                	<!-- 盘点申请新增（newBill）/修改(2) -->
                                    <s:if test='operateType.equals("newBill") || operateType.equals("2")'>
                                    	<!-- 全盘 -->
                                        <s:if test='addType.equals("all")'>
                                            <th class="tableheader" width="5%"><s:text name="MNG07_no"/></th>
                                        </s:if>
                                        <!-- 商品盘点  可选择明细进行删除操作 -->
                                        <s:else>
                                            <th class="tableheader" width="5%"><s:text name="MNG07_lblSelect"/></th>
                                        </s:else>
	                                </s:if>
	                                <!-- 盘点申请  详细 -->
	                                <s:else>
	                                   <th class="tableheader" width="5%"><s:text name="MNG07_no"/></th>
	                                </s:else>
	                                <th class="tableheader" width="10%"><s:text name="MNG07_unitCode"/></th>
	                                <th class="tableheader" width="10%"><s:text name="MNG07_barcode"/></th>
	                                <th class="tableheader" width="20%"><s:text name="MNG07_prodcutName"/></th>
	                                <th class="tableheader" width="8%"><s:text name="MNG07_price"/></th>
	                                <!--非盲盘 -->
	                                <s:if test='"0".equals(blindFlag)'>
	                                	<!-- 盘点申请新增（newBill）/修改(2) -->
		                                <s:if test='operateType.equals("newBill") || operateType.equals("2")'>
		                                   <th class="tableheader" width="8%"><s:text name="MNG07_bookCount"/></th>
		                                </s:if>
	                                </s:if>
	                                <th class="tableheader" width="12%"><s:text name="MNG07_quantity"/></th>
	                                <!--非盲盘 -->
                                    <s:if test='"0".equals(blindFlag)'>
                                        <th class="tableheader" width="10%"><s:text name="MNG07_gainQuantity"/></th>
                                    </s:if>
                                    <s:if test='"0".equals(blindFlag)'>
                                        <th class="tableheader" width="10%"><s:text name="MNG07_amount"/></th>
	                                </s:if>
	                                <th class="tableheader" width="20%"><s:text name="MNG07_reason"/></th>
	                                <th style="display:none"></th>
                                </tr>
                            </thead>
                            <tbody id="databody">
                            	<!-- 盘点申请  非新增（新增时初始化,没有明细） -->
                                <s:if test='!operateType.equals("newBill")'>
	                                <s:iterator value="takingReqDetailList" status="status">
	                                    <tr id="dataRow<s:property value='#status.index+1'/>" class="<s:if test='#status.odd'>odd</s:if><s:else>even</s:else>">
                                            <td id="dataTd1" class="center">
                                            	<!-- 可编辑且非全盘  可对明细做删除操作 -->
                                                <s:if test='operateType.equals("2") && !addType.equals("all")'>
	                                               <input id="chkbox" type="checkbox" onclick="binOLWSMNG07_02.changechkbox(this);">
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
	                                        <!--非盲盘 -->
	                                        <s:if test='"0".equals(blindFlag)'>
	                                        	<!-- 盘点申请新增（newBill，在此处没有用处，只是为了与列名的判断逻辑保持一致）/修改(2) -->
		                                        <s:if test='operateType.equals("newBill") || operateType.equals("2")'>
		                                        	<!-- 库存数量，做扣减冻结库存 -->
		                                           <td id="dataTDStock" class="alignRight"><s:property value="BookQuantity"/></td>
		                                        </s:if>
	                                        </s:if>
	                                        <td id="dataTd6" class="alignRight">
	                                            <s:if test='null != CheckQuantity'>
	                                               <s:if test='operateType.equals("2")'>
	                                                   <s:if test='"0".equals(HandleType)'><%--不做处理 --%>
                                                           <input type="text"  id="quantityArr" style="width:120px;text-align:right;" class="hide"  size="10" maxlength="4" value='' onchange="binOLWSMNG07_02.changeCount(this);"/>
                                                            <div id="hideQuantiyArr">&nbsp;</div>
	                                                   </s:if>
	                                                   <s:else>
	                                                       <input type="text"  id="quantityArr" style="width:120px;text-align:right;" class="hide"  size="10" maxlength="4" value='<s:property value="CheckQuantity"/>' onchange="binOLWSMNG07_02.changeCount(this);"/>
	                                                        <div id="hideQuantiyArr">
	                                                            <s:text name="format.number"><s:param value="CheckQuantity"></s:param></s:text>
	                                                        </div>
	                                                   </s:else>
	                                               </s:if>
	                                               <s:else>
	                                                   <s:text name="format.number"><s:param value="CheckQuantity"></s:param></s:text>
	                                               </s:else>	                                                
	                                            </s:if>
	                                            <s:else>&nbsp;</s:else>
	                                        </td>
	                                        <!--非盲盘 -->
	                                        <s:if test='"0".equals(blindFlag)'>
		                                        <td id="dataTdGainStock" class="alignRight">
		                                        <!-- 盘点申请  编辑 -->
		                                        <s:if test='"0".equals(HandleType) && operateType.equals("2")'>
                                                    &nbsp;
		                                        </s:if>
		                                        <s:else>
                                                    <s:text name="format.number"><s:param value="GainQuantity"></s:param></s:text>
		                                        </s:else>
		                                        </td>
	                                        </s:if>
	                                        <!--非盲盘 -->
	                                        <s:if test='"0".equals(blindFlag)'>
		                                        <td id="money" class="alignRight">
		                                            <s:if test='null!=Price && null != CheckQuantity'>
                                                         <s:if test='"0".equals(HandleType) && operateType.equals("2")'>
                                                         0.00
                                                         </s:if>
                                                         <s:else>
                                                            <s:text name="format.price"><s:param value="Price*CheckQuantity"></s:param></s:text>
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
	                                        	<input type="hidden" name="checkQuantityArr" id="checkQuantityArr" value="<s:property value='CheckQuantity'/>"></input>
	                                            <input type="hidden" name="bookCountArr" id="bookCountArr" value="<s:property value='BookQuantity'/>"></input>
	                                            <input type="hidden" name="gainCountArr" id="gainCountArr" value="<s:property value='GainQuantity'/>"></input>
	                                            <input type="hidden" name="productVendorIDArr" id="productVendorIDArr" value="<s:property value='BIN_ProductVendorID'/>"></input>
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
	                        <td class="center"><s:text name="MNG07_totalQuantity"/></td><%-- 总数量 --%>
	                        <td class="center"><s:text name="MNG07_totalAmount"/></td><%-- 总金额--%>
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
	                    <!-- 盘点申请  新增 -->
	                    <s:if test='operateType.equals("newBill")'>
	                        <input type="hidden" id="organizationId" name="organizationId" value='<s:property value="organizationId"/>'/>
	                        <input type="hidden" id="depotId" name="depotId" value='<s:property value="depotId"/>'/>
	                        <input type="hidden" id="logicinventId" name="logicinventId" value='<s:property value="logicinventId"/>'/>
	                    </s:if>
	                    <s:else>
	                        <input type="hidden" id="organizationId" name="organizationId" value='<s:property value="takingInfo.BIN_OrganizationID"/>'/>
	                        <input type="hidden" id="depotId" name="depotId" value='<s:property value="takingInfo.BIN_InventoryInfoID"/>'/>
	                        <input type="hidden" id="logicinventId" name="logicinventId" value='<s:property value="takingInfo.BIN_LogicInventoryInfoID"/>'/>
                        	<!-- 用于提交已经暂存的盘点申请单在开始工作流时写的操作日志的单据号 -->
                        	<input type="hidden" id="stockTakingNoIF" name="stockTakingNoIF" value="<s:property value="takingInfo.StockTakingNoIF"/>">
                        </s:else>
                        <!-- 盘点申请  编辑 -->
                        <s:if test='operateType.equals("2")'>
	                        <input type="hidden" id="rowNumber" value="<s:property value='takingReqDetailList.size()'/>"/>
	                        <input type="hidden" id="inTestType" value="<s:property value='takingReqDetailList.TestType'/>">
	                        <input type="hidden" id="updateTime" name="updateTime" value="<s:property value="takingInfo.UpdateTime"/>">
	                        <input type="hidden" id="modifyCount" name="modifyCount" value="<s:property value="takingInfo.ModifyCount"/>">
	                        <input type="hidden" id="verifiedFlag" name="verifiedFlag" value="<s:property value="takingInfo.VerifiedFlag"/>">
	                        <s:hidden id="proStocktakeRequestID" name="proStocktakeRequestID" value="%{#request.proStocktakeRequestID}"></s:hidden>
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
	                        <button type="button" class="save" onclick="binOLWSMNG07_02.submitSave();">
	                            <span class="ui-icon icon-save"></span>
	                            <span class="button-text"><s:text name="MNG07_btnSave"/></span>
	                        </button>
	                        <button type="button" class="confirm" onclick="binOLWSMNG07_02.submitSend();">
	                            <span class="ui-icon icon-confirm"></span>
	                            <span class="button-text"><s:text name="MNG07_btnOK"/></span>
	                        </button>
	                        <s:if test='operateType.equals("2")'>
                                <button type="button" id="btn-icon-edit-big" class="confirm" onclick="binOLWSMNG07_02.modifyForm();return false;">
                                    <span class="ui-icon icon-edit-big"></span>
                                    <%-- 修改 --%>
                                    <span class="button-text"><s:text name="os.edit"/></span>
                                </button>
                        	</s:if>
                        </s:if>
                        <button type="button" id="btnReturn" class="close" onclick="binOLWSMNG07_02.back();return false;" style="display:none;"><span class="ui-icon icon-back"></span>
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
                <!-- 新增盘点申请单时的提示信息 -->
                <span class="highlight"><s:text name="MNG07_snow"/></span><s:text name="MNG07_saveTip"/>
            </s:if>
    </div>
</div>
</div>
</s:i18n>
<form action="BINOLWSMNG07_initDetail" id="proStocktakeDetailUrl" method="post">
    <input name="csrftoken" type="hidden"/>
    <s:hidden name="proStocktakeRequestID" value="%{#request.proStocktakeRequestID}"></s:hidden>
</form>
<div id="errmessage" style="display:none">
    <input type="hidden" id="errmsg_EST00006" value='<s:text name="EST00006"/>'/>
    <input type="hidden" id="errmsg_EST00007" value='<s:text name="EST00007"/>'/>
    <input type="hidden" id="errmsg_EST00008" value='<s:text name="EST00008"/>'/>
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
    <input type="hidden" id="errmsg_EST00047" value='<s:text name="EST00047"/>'/>
</div>
<%-- ================== 弹出datatable -- 部门共通START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/popDepartTable.jsp" flush="true" />
<%-- ================== 弹出datatable -- 部门共通START ======================= --%>
<%-- ================== 工作流提示弹出窗口 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/popOSDialog.jsp" flush="true" />
<%-- ==================  工作流提示弹出窗口End ======================== --%>
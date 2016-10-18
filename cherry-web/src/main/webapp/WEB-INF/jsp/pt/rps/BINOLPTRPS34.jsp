<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<script type="text/javascript" src="/Cherry/js/pt/rps/BINOLPTRPS34.js"></script>
<style>
    .box-header {
        padding: 0.2em 0;
    }
    table.detail {
        margin-bottom: 3px;
    }
</style>
<s:set name="language" value="session.language" />
<s:i18n name="i18n.pt.BINOLPTRPS34">
<div class="main container clearfix">
    <div class="panel ui-corner-all">
        <div class="panel-header">
        <div class="clearfix">
            <span class="breadcrumb left"><span class="ui-icon icon-breadcrumb"></span><s:text name="RPS34_title"/>&nbsp;(<s:text name="RPS34_orderNo"/>:<s:property value="esOrderMain.orderNo"/>)</span>
        </div>
        </div>
        <div class="panel-content">
            <div class="section">
                <div class="section-header">
                <strong>
                <span class="ui-icon icon-ttl-section-info"></span>
                    <%-- 基本信息 --%>
                    <s:text name="global.page.title"/>
                </strong>
                </div>
                <div class="section-content">
                    <div class="box-header"></div>
                    <table class="detail" cellpadding="0" cellspacing="0">
                        <tr>
                            <%-- 单据号 --%>
                            <th><s:text name="RPS34_billCode"/></th>
                            <td><s:property value="esOrderMain.billCode"/></td>
                            <%-- 关联单据号 --%>
                            <%--
                            <th><s:text name="RPS34_relevanceBillCode"/></th>
                            <td><s:property value="esOrderMain.relevanceBillCode"/></td>
                            --%>
                            <%-- 原始单据号 --%>
                            <th><s:text name="RPS34_OriginalBillCode"/></th>
                            <td><s:property value="esOrderMain.originalBillCode"/></td>
                        </tr>
                        <tr>
                            <%-- 下单时间 --%>
                            <th><s:text name="RPS34_BillCreateTime"/></th>
                            <td>${esOrderMain.billCreateTime}</td>
                            <%-- 单据状态 --%>
                            <th><s:text name="RPS34_BillState"/></th>
                            <td><s:property value='#application.CodeTable.getVal("1310", esOrderMain.billState)'/></td>
                        </tr>
                        <tr>
                            <%-- 单据付款时间 --%>
                            <th><s:text name="RPS34_BillPayTime"/></th>
                            <td><s:property value="esOrderMain.billPayTime"/></td>
                            <%-- 是否预售单 --%>
                            <th><s:text name="RPS34_preSale"/></th>
                            <td><s:property value='#application.CodeTable.getVal("1344", esOrderMain.preSale)'/></td>                           
                        </tr>
                        <tr>
                            <%-- 交易类型 --%>
                            <th><s:text name="RPS34_saleType"/></th>
                            <td><s:property value='#application.CodeTable.getVal("1055", esOrderMain.businessType)'/></td>
                            <%-- 业务类型 --%>
                            <th><s:text name="RPS34_TicketType"/></th>
                            <td><s:property value='#application.CodeTable.getVal("1261", esOrderMain.ticketType)'/></td>
                        </tr>
                        <tr>
                            <%-- 单据类型 --%>
                            <th><s:text name="RPS34_BillType"/></th>
                            <td><s:property value='#application.CodeTable.getVal("1326", esOrderMain.billType)'/></td>
                            <%-- 单据关闭时间 --%>
                            <th><s:text name="RPS34_BillCloseTime"/></th>
                            <td><s:property value="esOrderMain.billCloseTime"/></td>                           
                        </tr>
                        <tr>
                            <%-- 销售部门 --%>
                            <th><s:text name="RPS34_ShopName"/></th>
                            <td>
                                <span><s:property value="esOrderMain.departCode"/></span>
                                <span class="gray"><s:property value="'('+esOrderMain.departName+')'"/></span>
                            </td>
                            <%-- 操作员 --%>
                            <th><s:text name="RPS34_employeeName"/></th>
                            <td>
                                <span><s:property value='esOrderMain.employeeCode'/></span>
                                <span class="gray"><s:property value='"("+esOrderMain.employeeName+")"'/></span>
                            </td>
                        </tr>
                        <tr>
                            <%-- 下单部门 --%>
                            <th><s:text name="RPS34_DepartDX"/></th>
                            <td>
                                <span><s:property value="esOrderMain.departCodeDX"/></span>
                                <span class="gray"><s:property value="'('+esOrderMain.departNameDX+')'"/></span>
                            </td>
                            <%-- 下单员 --%>
                            <th><s:text name="RPS34_EmployeeDX"/></th>
                            <td>
                                <span><s:property value='esOrderMain.employeeCodeDX'/></span>
                                <span class="gray"><s:property value='"("+esOrderMain.employeeNameDX+")"'/></span>
                            </td>
                        </tr>
                        <tr>
                            <%-- 会员卡号 --%>
                            <th><s:text name="RPS34_memCode"/></th>
                            <td>
                                <span><s:property value='esOrderMain.memberCode'/></span>
                                <%-- 会员姓名(有则显示出来) --%>
                                <s:if test="esOrderMain.memberName != null">
                                    <span class="gray"><s:property value='"("+esOrderMain.memberName+")"'/></span>
                                </s:if>
                            </td>
                            <%-- 会员昵称 --%>
                            <th><s:text name="RPS34_MemberNickname"/></th>
                            <td><s:property value="esOrderMain.memberNickname"/></td>
                        </tr>
                        <tr>
                            <%-- 买家姓名 --%>
                            <th><s:text name="RPS34_BuyerName"/></th>
                            <td><s:property value="esOrderMain.buyerName"/></td>
                            <%-- 买家手机号 --%>
                            <th><s:text name="RPS34_BuyerMobilePhone"/></th>
                            <td><s:property value="esOrderMain.buyerMobilePhone"/></td>
                        </tr>
                        <tr>
                            <%-- 收货人姓名 --%>
                            <th><s:text name="RPS34_ConsigneeName"/></th>
                            <td><s:property value="esOrderMain.consigneeName"/></td>
                            <%-- 收货人手机 --%>
                            <th><s:text name="RPS34_ConsigneeMobilePhone"/></th>
                            <td><s:property value="esOrderMain.consigneeMobilePhone"/></td>
                        </tr>
                        <tr>
                            <%-- 收货人省份 --%>
                            <th><s:text name="RPS34_ConsigneeProvince"/></th>
                            <td><s:property value="esOrderMain.consigneeProvince"/></td>
                            <%-- 收货人城市 --%>
                            <th><s:text name="RPS34_ConsigneeCity"/></th>
                            <td><s:property value="esOrderMain.consigneeCity"/></td>
                        </tr>
                        <tr>
                            <%-- 收货人地址 --%>
                            <th><s:text name="RPS34_ConsigneeAddress"/></th>
                            <td colspan="3"><s:property value="esOrderMain.consigneeAddress"/></td>
                        </tr>
                        <tr>
                            <%-- 快递公司代号 --%>
                            <th><s:text name="RPS34_ExpressCompanyCode"/></th>
                            <td><s:property value="esOrderMain.expressCompanyCode"/></td>
                            <%-- 快递单编号 --%>
                            <th><s:text name="RPS34_ExpressBillCode"/></th>
                            <td><s:property value="esOrderMain.expressBillCode"/></td>
                        </tr>
                        <tr>
                            <%-- 运费 --%>
                            <th ><s:text name="RPS34_ExpressCost"/></th>
                            <td colspan="3"><s:property value="esOrderMain.expressCost"/></td>
                        </tr>
                        <tr>
                            <%-- 总折扣率 --%>
                            <th><s:text name="RPS34_billDiscount"/></th>
                            <td><s:property value='esOrderMain.billDiscount'/></td>
                            <%-- 数据来源 --%>
                            <th><s:text name="RPS34_DataSource"/></th>
                            <td>
                                <s:set id="dataSourceCodeValue" name="dataSourceCodeValue" value="#application.CodeTable.getVal('1011', esOrderMain.dataSource) "></s:set>
                                <s:if test="null == #dataSourceCodeValue || #dataSourceCodeValue.equals('')">
                                    <s:property value='esOrderMain.dataSource'/>
                                </s:if>
                                <s:else>
                                    <s:property value='#dataSourceCodeValue'/>
                                </s:else>
                            </td>
                        </tr>
                        <tr>
                            <%-- 买家留言 --%>
                            <th><s:text name="RPS34_BuyerMessage"/></th>
                            <td colspan="3"><s:property value="esOrderMain.buyerMessage"/></td>
                        </tr>
                        <tr>
                            <%-- 卖家留言 --%>
                            <th><s:text name="RPS34_SellerMemo"/></th>
                            <td colspan="3"><s:property value="esOrderMain.sellerMemo"/></td>
                        </tr>
                        <tr>
                            <%-- 备注 --%>
                            <th><s:text name="RPS34_Comments"/></th>
                            <td colspan="3"><s:property value='esOrderMain.comments'/></td>
                        </tr>
                    </table>
                    <div class="clearfix"></div>
                </div>
            </div>
            <%-- 支付方式详细为空不显示，迭代显示存在的支付方式及对应的金额 --%>
            <s:if test="payDetail != null && payDetail.size() > 0">
                <div class="section">
                    <div class="section-header">
                        <strong>
                            <span class="ui-icon icon-money"></span>
                            <%-- 支付明细 --%>
                            <s:text name="RPS34_payment_details"/>
                        </strong>
                    </div>
                    <div class="section-content">
                        <div class="box2-content clearfix">
                            <s:iterator value="payDetail" status="status">
                                <s:set var="payTypeWidthPercent" value="25" />
                                <s:set var="showSerialNumber" value="false"/>
                                <s:if test='null != serialNumber && !serialNumber.equals("")'>
                                    <s:set var="payTypeWidthPercent" value="50" />
                                    <s:set var="showSerialNumber" value="true"/>
                                </s:if>
                                <span class="left" style="width:<s:property value="#payTypeWidthPercent"/>%;margin:3px 0;">
                                    <span class="ui-icon icon-arrow-crm"></span>
                                    <span class="bg_title"><s:property value='#application.CodeTable.getVal("1175", payTypeCode)'/></span>
                                    <span class="red"><s:text name="format.price"><s:param value="payAmount"/></s:text></span>
                                    <s:if test='#showSerialNumber == true'>
                                       <span style="display:inline-block;">(<s:text name="RPS34_SerialNumber"/>:<s:property value='serialNumber'/>)</span>
                                    </s:if>
                                </span>
                            </s:iterator>
                        </div>
                    </div>
                </div>
            </s:if>
            <div class="section">
                 <div class="section-header">
                    <strong>
                        <span class="ui-icon icon-ttl-section-search-result"></span>
                        <%-- 销售记录明细一览 --%>
                        <s:text name="RPS34_results_list"/>
                    </strong>
                </div>
                <div class="section-content">
                    <div id="table_scroll" style="overflow-x:auto;overflow-y:hidden">
                        <table cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
                            <thead>
                                <tr>
                                    <th class="center"><s:text name="RPS34_num"/></th><%-- 编号 --%>
                                    <th class="center"><s:text name="RPS34_productName"/></th><%-- 产品名称 --%>
                                    <th class="center"><s:text name="RPS34_unitCode"/></th><%-- 厂商编码 --%>                                    
                                    <th class="center"><s:text name="RPS34_barCode"/></th><%-- 产品条码 --%> 
                                    <%--<th class="center"><s:text name="RPS34_amountPortion"/></th>--%><%-- 分摊后金额 --%>
                                    <th class="center"><s:text name="RPS34_moduleCode"/></th><%-- 计量单位 --%>
                                    <th class="center"><s:text name="RPS34_pricePay"/></th><%-- 销售价 --%>
                                    <th class="center"><s:text name="RPS34_quantity"/></th><%-- 数量 --%>
                                    <th class="center"><s:text name="RPS34_discount"/></th><%-- 折扣率 --%>
                                    <th class="center"><s:text name="RPS34_amount"/></th><%-- 金额 --%>
                                    <th class="center"><s:text name="RPS34_saleType"/></th><%-- 销售类型--%>
                                    <th class="center"><s:text name="RPS34_in_activities"/></th><%-- 参与活动--%>
                                    <s:if test='sysConfigShowUniqueCode.equals("1")'>
                                        <th class="center"><s:text name="RPS34_UniqueCode"/></th><%-- 唯一码--%>
                                    </s:if>
                                    <th class="center" width="20%"><s:text name="RPS34_comment"/></th><%-- 备注 --%>
                                    <th style="display: none;" class="center" width="20%"><s:text name="RPS34_isExistsPos"/></th> <%--产品在新后台是否存在--%>
                                </tr>
                            </thead>
                            <tbody>
                                <s:iterator value="esOrderDetail" status="status">
                                <tr class="<s:if test='#status.odd'>odd</s:if><s:else>even</s:else>">
                                    <td><s:property value="#status.index+1"/></td>
                                    <s:if test="productName!=null">
                                    	<td><span><s:property value="productName"/></span></td><%-- 商品名称 --%>
                                    </s:if>
                                    <s:else>                                  
                                    	<td><%-- 电商商品标题 --%>
                                    	<s:if test="esProductTitleName!=null">
                                    	<font color="blue"><s:property value="esProductTitleName"/></font>
                                    	<font color="red">(产品对应关系异常)</font>
                                    	</s:if>
                                    	</td>
                                    </s:else>
                                    <td><span><s:property value="unitCode"/></span></td>                                                                                                                                                
                                    <td><span><s:property value="barCode"/></span></td>
                                    <%--<td class="alignRight"><span><s:property value="amountPortion"/></span></td>--%>
                                    <td class="center"><span><s:property value='#application.CodeTable.getVal("1190", moduleCode)'/></span></td>
                                    <td class="alignRight">
                                        <span>
	                                        <s:if test='null != pricePay'>
	                                            <s:text name="format.price"><s:param value="pricePay"/></s:text>
	                                        </s:if>
                                        </span>
                                    </td>
                                    <td class="alignRight">
                                        <s:if test='quantity > 0'>
                                            <s:text name="format.number">
                                                <s:param value="quantity"></s:param>
                                            </s:text>
                                        </s:if><s:else>
                                            <span>0</span>
                                        </s:else>
                                    </td>
                                    <td class="alignRight"><span><s:property value="discount"/></span></td>
                                    <td class="alignRight">
                                        <s:if test='"N".equals(saleType)'>
                                            <s:if test='quantity > 0'>
                                                <s:text name="format.price">
                                                    <s:param value="quantity*pricePay"></s:param>
                                                </s:text>
                                            </s:if>
                                            <s:else>
                                                <span>0.00</span>
                                            </s:else>
                                        </s:if>
                                        <s:elseif test='"P".equals(saleType)'>
                                            <s:if test='quantity != 0'>
                                                <s:text name="format.price">
                                                    <s:param value="pricePay"></s:param>
                                                </s:text>
                                            </s:if>
                                            <s:else>
                                               <span>0.00</span>
                                            </s:else>
                                        </s:elseif>
                                    </td>
                                    <td><span><s:property value='#application.CodeTable.getVal("1106", saleType)'/></span></td>
                                    <%-- 参与活动 --%>
                                    <td><s:property value="inActivityName"/></td>
                                    <s:if test='sysConfigShowUniqueCode.equals("1")'>
                                        <%-- 唯一码 --%>
                                        <td><s:property value="uniqueCode"/></td>
                                    </s:if>
                                    <td>
	                                    <p>
	                                    <span>
	                                    <s:property value="comment"/>
                                    	<s:if test="esProductTitleName!=null">
	                                    	<s:property value="esProductTitleName"/>
	                                    </s:if>
	                                    </span>
	                                    </p>
                                    </td>
                                    <%-- 产品是否存在--%>
                                    <td style="text-align: center;display: none;">
                                    <span><s:property value='#application.CodeTable.getVal("1344", isExistsPos)'/></span>                                   
                                    </td>
           
                                </tr>
                                </s:iterator>
                            </tbody>
                        </table>
                    </div>
                    <hr class="space" />
                    <table cellpadding="0" cellspacing="0" width="30%" border="0" class="right editable">
                        <tr>
                            <th rowspan="2" scope="row" class="center"><s:text name="global.page.total"/></th><%-- 合计 --%>
                            <td class="center"><s:text name="RPS34_quantity"/></td><%-- 总数量 --%>
                            <td class="center"><s:text name="RPS34_amount"/></td><%-- 总金额--%>
                        </tr>
                        <tr>
                            <td class="center">
                                <s:if test="esOrderMain.totalQuantity >= 0"><s:text name="format.number"><s:param value="esOrderMain.totalQuantity"></s:param></s:text></s:if>
                                <s:else><span class="highlight"><s:text name="format.number"><s:param value="esOrderMain.totalQuantity"></s:param></s:text></span></s:else>
                            </td>
                            <td class="center">
                                <s:if test="esOrderMain.payAmount >= 0"><s:text name="format.price"><s:param value="esOrderMain.payAmount"></s:param></s:text></s:if>
                                <s:else><span class="highlight"><s:text name="format.price"><s:param value="esOrderMain.payAmount"></s:param></s:text></span></s:else>
                            </td>
                        </tr>
                    </table>
                    <hr class="space" />
                    <div class="center">
                        <button class="close" onclick="window.close();"><span class="ui-icon icon-close"></span>
                            <%-- 关闭 --%>
                            <span class="button-text"><s:text name="global.page.close"/></span>
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</s:i18n>

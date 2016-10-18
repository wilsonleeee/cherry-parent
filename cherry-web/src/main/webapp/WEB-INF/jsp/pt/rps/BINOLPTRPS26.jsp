<%--发货单明细 --%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<script type="text/javascript" src="/Cherry/js/st/sfh/BINOLSTSFH05.js"></script>
<style>
    .box-header {
        padding: 0.2em 0;
    }
    table.detail {
         margin-bottom: 3px;
    }
</style>
<s:i18n name="i18n.st.BINOLSTSFH05">
<div class="main container clearfix">
<div id="div_main" class="panel ui-corner-all">
    <div class="panel-header">
        <div class="clearfix">
            <span class="breadcrumb left"><span class="ui-icon icon-breadcrumb"></span><s:text name="SFH05_title"/>&nbsp;(<s:text name="SFH05_num"/>:<s:property value="mainMap.DeliverNo"/>)</span>
        </div>
    </div>
      <div class="panel-content">
        <div class="section">
          <div class="section-header"><strong><span class="ui-icon icon-ttl-section-info"></span>
                <%-- 基本信息 --%>
                <s:text name="global.page.title"/>
            </strong>
          </div>
            <div class="section-content">
                <div>
                <div class="box-header"></div>
                <table class="detail">
                    <tbody>
                        <tr>
                            <%-- 发货单单号 --%>
                            <th><s:text name="SFH05_deliverNo"/></th>
                            <td><s:property value="mainMap.DeliverNoIF"/></td>
                            <%-- 发货单日期 --%>
                            <th><s:text name="SFH05_date"/></th>
                            <td><s:property value="mainMap.Date"/></td>
                        </tr>
                        <tr>
                            <%-- 关联单号 --%>
                            <th><s:text name="SFH05_relevantNo"/></th>
                            <td><s:property value="mainMap.RelevanceNo"/></td>
                            <%-- 处理状态 --%>
                            <th><s:text name="SFH05_tradeStatus"/></th>
                            <td><s:property value='#application.CodeTable.getVal("1141", mainMap.TradeStatus)'/></td>
                        </tr>
                        <tr>
                            <%-- 发货部门 --%>
                            <th><s:text name="SFH05_depart"/></th>
                            <td><s:property value="mainMap.DepartCodeName"/></td>
                            <%-- 操作员 --%>
                            <th><s:text name="SFH05_employeeName"/></th>
                            <td><s:property value="mainMap.EmployeeName"/></td>
                        </tr>
                        <tr>
                            <%--实体仓名称 --%>
                            <th><s:text name="SFH05_DepotCodeName"/></th>
                            <td><s:property value="mainMap.DepotCodeName"/></td>
                            <%-- 逻辑仓名称 --%>
                            <th><s:text name="SFH05_LogicInventoryName"/></th>
                            <td><s:property value="mainMap.LogicInventoryName"/></td>
                        </tr>
                        <tr>
                            <%-- 发货理由 --%>
                            <th><s:text name="SFH05_reason"/></th>
                            <td colspan=3><s:property value='mainMap.Comments'/></td>
                        </tr>
                    </tbody>
                </table>
                <table class="detail">
                    <tbody>
                        <tr>
                            <%-- 收货部门 --%>
                            <th><s:text name="SFH05_receiveDepart"/></th>
                            <td><s:property value="mainMap.ReceiveDepartCodeName"/></td>
                            <th></th>
                            <td></td>
                        </tr>
                        <tr>
                            <%--收货地址 --%>
                            <th><s:text name="SFH05_address"/></th>
                            <td colspan=3><s:property value="mainMap.ReceiveDepartAddress"/></td>
                        </tr>
                    </tbody>
                </table>
                <table class="detail" style="margin-bottom: 5px;">
                    <tbody>
                        <tr>
                            <%-- 审核状态 --%>
                            <th><s:text name="SFH05_verifiedFlag"/></th>
                            <td><s:property value='#application.CodeTable.getVal("1007", mainMap.VerifiedFlag)'/></td>
                            <%-- 审核者 --%>
                            <th><s:text name="SFH05_employeeNameAudit"/></th>
                            <td><s:property value="mainMap.EmployeeNameAudit"/></td>
                        </tr>
                    </tbody>
                </table>
                <div class="clearfix"></div>
            </div>
          </div>
        </div>
        <div class="section">
          <div class="section-header"><strong><span class="ui-icon icon-ttl-section-search-result"></span>
            <%-- 入出库明细一览 --%>
            <s:text name="SFH05_results_list"/>
            </strong>
          </div>
          <div class="section-content">
            <div id="table_scroll" style="overflow-x:auto;overflow-y:hidden">
            <table cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
              <thead>
                <tr>
                    <th class="center"><s:text name="SFH05_no"/></th><%-- 编号 --%>
                    <th class="center"><s:text name="SFH05_UnitCode"/></th><%-- 厂商编码 --%>
                    <th class="center"><s:text name="SFH05_ProductName"/></th><%-- 产品名称 --%>
                    <th class="center"><s:text name="SFH05_BarCode"/></th><%-- 产品条码 --%> 
                    <th class="center"><s:text name="SFH05_Price"/></th><%-- 单价 --%>
                    <th class="center"><s:text name="SFH05_Quantity"/></th><%-- 数量 --%>
                    <th class="center"><s:text name="SFH05_Amount"/></th><%-- 金额 --%>
                    <th class="center"><s:text name="SFH05_remark"/></th><%-- 备注 --%>
                </tr>
              </thead>
              <tbody id="databody">
                <s:iterator value="detailList" status="status">
                    <tr id="dataRow<s:property value='#status.index+1'/>" class="<s:if test='#status.odd'>odd</s:if><s:else>even</s:else>">
                        <td><s:property value="#status.index+1"/></td>
                        <td><span><s:property value="UnitCode"/></span></td>
                        <td><span><s:property value="ProductName"/></span></td>
                        <td><span><s:property value="BarCode"/></span></td>
                        <td class="alignRight"><span><s:text name="format.price"><s:param value="Price"></s:param></s:text></span></td>
                        <td class="alignRight"><span><s:text name="format.number"><s:param value="Quantity"></s:param></s:text></span></td>
                        <td class="alignRight"><span><s:text name="format.price"><s:param value="Price*Quantity"></s:param></s:text></span></td>
                        <td><p><s:property value="Comments"/></p></td>
                    </tr>
                </s:iterator>
              </tbody>
            </table>
            </div>
            <hr class="space" />
            <table cellpadding="0" cellspacing="0" width="30%" border="0" class="right editable">
              <tr>
                <th rowspan="2" scope="row" class="center"><s:text name="global.page.total"/></th><%-- 合计 --%>
                <td class="center"><s:text name="SFH05_totalQuantity"/></td><%-- 总数量 --%>
                <td class="center"><s:text name="SFH05_totalAmount"/></td><%-- 总金额--%>
              </tr>
              <tr>
                <td class="center">
                    <%-- 总数量 --%>
                    <span><s:text name="format.number"><s:param value="mainMap.TotalQuantity"></s:param></s:text></span>
                </td>
                <td class="center">
                    <%-- 总金额--%>
                    <span><s:text name="format.price"><s:param value="mainMap.TotalAmount"></s:param></s:text></span>
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
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<script type="text/javascript" src="/Cherry/js/pt/rps/BINOLPTRPS14.js"></script>
<style>
    .box-header {
        padding: 0.2em 0;
    }
    table.detail {
         margin-bottom: 3px;
    }
</style>
<s:set name="language" value="session.language" />
<s:i18n name="i18n.pt.BINOLPTRPS14">
<div class="main container clearfix">
	<div class="panel ui-corner-all">
        <div class="panel-header">
        <div class="clearfix">
            <span class="breadcrumb left"><span class="ui-icon icon-breadcrumb"></span><s:text name="RPS14_title"/>&nbsp;(<s:text name="RPS14_saleRecordCode"/>:<s:property value="getSaleRecordDetail.saleRecordCode"/>)</span>
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
          					<%-- 销售单据号 --%>
          					<th><s:text name="RPS14_billCode"/></th>
          					<td><s:property value="getSaleRecordDetail.billCode"/></td>
          					<%-- 销售日期 --%>
          					<th><s:text name="RPS14_saleTime"/></th>
          					<td>${getSaleRecordDetail.saleTime}</td>
          				</tr>
              		    <tr>
                            <%-- 前置单据号 --%>
                            <th><s:text name="RPS14_billCodePre"/></th>
                            <td><s:property value="getSaleRecordDetail.billCodePre"/></td>
                            <%-- 单据类型 --%>
                            <th><s:text name="RPS14_TicketType"/></th>
                            <td><s:property value='#application.CodeTable.getVal("1261", getSaleRecordDetail.ticketType)'/></td>
                        </tr>
          				<tr>
          					<%-- 业务类型 --%>
          					<th><s:text name="RPS14_businessType"/></th>
          					<td><s:property value='#application.CodeTable.getVal("1055", getSaleRecordDetail.businessType)'/></td>
                            <%-- 操作员 --%>
                            <th><s:text name="RPS14_employeeName"/></th>
                            <td>
                            	<span><s:property value='getEmployeeName.employeeCode'/></span>
                            	<span class="gray"><s:property value='"("+getEmployeeName.employeeName+")"'/></span>
                            </td>
          				</tr>
          				<tr>
          					<%-- 会员卡号 --%>
          					<th><s:text name="RPS14_memCode"/></th>
          					<td>
          						<span><s:property value='getSaleRecordDetail.memberCode'/></span>
          						<%-- 会员姓名(有则显示出来) --%>
          						<s:if test="getSaleRecordDetail.memberName != null">
          							<span class="gray"><s:property value='"("+getSaleRecordDetail.memberName+")"'/></span>
          						</s:if>
          					</td>
          					<%-- 总折扣率 --%>
          					<th><s:text name="RPS14_billDiscount"/></th>
          					<td><s:property value='getSaleRecordDetail.billDiscount'/></td>
          				</tr>
          				<tr>
          					<%-- 销售部门 --%>
                            <th><s:text name="RPS14_departName"/></th>
                            <td>
                            	<%-- 从会员销售一览画面跳转过来的需要控制权限 --%>
                            	<s:if test='%{fromFlag != null && fromFlag == "1"}'>
                            	<cherry:show domId="BINOLMBMBM10_29">
                            	<span><s:property value="getSaleRecordDetail.departCode"/></span>
                            	<span class="gray"><s:property value="'('+getSaleRecordDetail.departName+')'"/></span>
                            	</cherry:show>
                            	</s:if>
                            	<s:else>
                            	<span><s:property value="getSaleRecordDetail.departCode"/></span>
                            	<span class="gray"><s:property value="'('+getSaleRecordDetail.departName+')'"/></span>
                            	</s:else>
                            </td>
                            <%-- 备注 --%>
                            <th><s:text name="RPS14_comments"/></th>
                            <td colspan=3><s:property value='getSaleRecordDetail.comments'/></td>
          				</tr>
          				<tr>
          					<%-- 单据模式 --%>
	                        <th><s:text name="RPS14_billModel"/></th>
          					<s:if test="getSaleRecordDetail.billModel!=null && getSaleRecordDetail.billModel != '0'">
	                            <td><s:property value='#application.CodeTable.getVal("1326", getSaleRecordDetail.billModel)'/></td>
          					</s:if>
          					<s:else>
          						<td><s:text name="RPS14_storeSale"></s:text></td>
          					</s:else>
          					<%-- 原始数据来源 --%>
                            <th><s:text name="RPS14_dataSource"/></th>
                            <td><s:property value='getSaleRecordDetail.dataSource'/></td>
          				</tr>
          			</table>
                    <div class="clearfix"></div>
          		</div>
			</div>
			<%-- 支付方式详细为空不显示，迭代显示存在的支付方式及对应的金额 --%>
			<s:if test="getPayTypeDetail != null && getPayTypeDetail.size() > 0">
				<div class="section">
					<div class="section-header">
						<strong>
							<span class="ui-icon icon-money"></span>
							<%-- 支付明细 --%>
							<s:text name="RPS14_payment_details"/>
						</strong>
					</div>
					<div class="section-content">
						<div class="box2-content clearfix">
							<s:iterator value="getPayTypeDetail" status="status">
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
									   <span style="display:inline-block;">(<s:text name="RPS14_SerialNumber"/>:<s:property value='serialNumber'/>)</span>
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
            			<s:text name="RPS14_results_list"/>
            		</strong>
          		</div>
          		<div class="section-content">
            		<div id="table_scroll" style="overflow-x:auto;overflow-y:hidden">
            			<table cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
              				<thead>
                				<tr>
                  					<th class="center"><s:text name="RPS14_num"/></th>          <%-- 编号 --%>
                  					<th class="center"><s:text name="RPS14_unitCode"/></th>     <%-- 厂商编码 --%>
                  					<th class="center"><s:text name="RPS14_productName"/></th>  <%-- 产品名称 --%>
                  					<th class="center"><s:text name="RPS14_barCode"/></th>      <%-- 产品条码 --%> 
                  					<th class="center"><s:text name="RPS14_amountPortion"/></th><%-- 分摊后金额 --%>
                  					<th class="center"><s:text name="RPS14_moduleCode"/></th>  <%-- 计量单位 --%>
                  					<%--<th class="center"><s:text name="RPS14_price"/></th>         定价 --%>
                  					<th class="center"><s:text name="RPS14_pricePay"/></th>     <%-- 销售价 --%>
                  					<th class="center"><s:text name="RPS14_quantity"/></th>     <%-- 数量 --%>
                  					<th class="center"><s:text name="RPS14_discount"/></th>     <%-- 折扣率 --%>
                  					<th class="center"><s:text name="RPS14_amount"/></th>       <%-- 金额 --%>
                  					<th class="center"><s:text name="RPS14_saleType"/></th>     <%-- 销售类型--%>
                  					<th class="center"><s:text name="RPS14_in_activities"/></th><%-- 参与活动--%>
                                    <s:if test='sysConfigShowUniqueCode.equals("1")'>
                                        <th class="center"><s:text name="RPS14_UniqueCode"/></th><%-- 唯一码--%>
                                    </s:if>
                  					<th class="center" width="20%"><s:text name="RPS14_comment"/></th>      <%-- 备注 --%>
                				</tr>
              				</thead>
              				<tbody>
                				<s:iterator value="getSaleRecordProductDetail" status="status">
                				<tr class="<s:if test='#status.odd'>odd</s:if><s:else>even</s:else>">
                	  				<td><s:property value="#status.index+1"/></td>
	                  				<td><span><s:property value="unitCode"/></span></td>
	                  				<td><span><s:property value="productName"/></span></td>
	                  				<td><span><s:property value="barCode"/></span></td>
	                  				<td class="alignRight"><span><s:property value="amountPortion"/></span></td>
	                  				<td class="center"><span><s:property value='#application.CodeTable.getVal("1190", moduleCode)'/></span></td>
	                  				<%--
	                  				<td class="alignRight">
	                  				<s:if test='price > 0'>
	                  					<s:text name="format.price">
	                  						<s:param value="price"></s:param>
	                  					</s:text>
	                  				</s:if><s:else>
	                  					<span>0</span>
	                  				</s:else>
	                  				</td>
	                  				--%>
	                  				<td class="alignRight"><span><s:property value="pricePay"/></span></td>
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
	                  				<td><p><s:property value="comment"/></p></td>
	                			</tr>
                				</s:iterator>
              				</tbody>
            			</table>
           			</div>
            <hr class="space" />
            <table cellpadding="0" cellspacing="0" width="30%" border="0" class="right editable">
              <tr>
                <th rowspan="2" scope="row" class="center"><s:text name="global.page.total"/></th><%-- 合计 --%>
                <td class="center"><s:text name="RPS14_quantity"/></td><%-- 总数量 --%>
                <td class="center"><s:text name="RPS14_amount"/></td><%-- 总金额--%>
              </tr>
              <tr>
                <td class="center">
                	<s:if test="getSaleRecordDetail.totalQuantity >= 0"><s:text name="format.number"><s:param value="getSaleRecordDetail.totalQuantity"></s:param></s:text></s:if>
                	<s:else><span class="highlight"><s:text name="format.number"><s:param value="getSaleRecordDetail.totalQuantity"></s:param></s:text></span></s:else>
                </td>
             	<td class="center">
             		<s:if test="getSaleRecordDetail.amount >= 0"><s:text name="format.price"><s:param value="getSaleRecordDetail.amount"></s:param></s:text></s:if>
             		<s:else><span class="highlight"><s:text name="format.price"><s:param value="getSaleRecordDetail.amount"></s:param></s:text></span></s:else>
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

<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<s:i18n name="i18n.cp.BINOLCPACT10">
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
          					<%-- 兑换单据号 --%>
          					<th><s:text name="ACT10_billNo"/></th>
          					<td><s:property value="getSaleRecordDetail.billCode"/></td>
          					<%-- 兑换时间 --%>
          					<th><s:text name="ACT10_getTime"/></th>
          					<td>${getSaleRecordDetail.saleTime}</td>
          				</tr>
              		    <tr>
                            <%-- 前置单据号 --%>
                            <th><s:text name="ACT10_billCodePre"/></th>
                            <td><s:property value="getSaleRecordDetail.billCodePre"/></td>
                            <%-- 单据类型 --%>
                            <th><s:text name="ACT10_billType"/></th>
                            <td><s:property value='#application.CodeTable.getVal("1261", getSaleRecordDetail.ticketType)'/></td>
                        </tr>
          				<tr>
          					<%-- 业务类型 --%>
          					<th><s:text name="ACT10_bussType"/></th>
          					<td><s:property value='#application.CodeTable.getVal("1055", getSaleRecordDetail.businessType)'/></td>
                            <%-- 操作员 --%>
                            <th><s:text name="ACT10_employee"/></th>
                            <td>
                            	<span><s:property value='getEmployeeName.employeeCode'/></span>
                            	<span class="gray"><s:property value='"("+getEmployeeName.employeeName+")"'/></span>
                            </td>
          				</tr>
          				<tr>
          					<%-- 会员卡号 --%>
          					<th><s:text name="ACT10_memberCode"/></th>
          					<td>
          						<span><s:property value='getSaleRecordDetail.memberCode'/></span>
          						<%-- 会员姓名(有则显示出来) --%>
          						<s:if test="getSaleRecordDetail.memberName != null">
          							<span class="gray"><s:property value='"("+getSaleRecordDetail.memberName+")"'/></span>
          						</s:if>
          					</td>
          					<%-- 总折扣率 --%>
          					<th><s:text name="ACT10_discout"/></th>
          					<td><s:property value='getSaleRecordDetail.billDiscount'/></td>
          				</tr>
          				<tr>
          					<%--兑换柜台 --%>
                            <th><s:text name="ACT10_counter"/></th>
                            <td>
                            	<span><s:property value="getSaleRecordDetail.departCode"/></span>
                            	<span class="gray"><s:property value="'('+getSaleRecordDetail.departName+')'"/></span>
                            </td>
                            <%-- 备注 --%>
                            <th><s:text name="ACT10_comment"/></th>
                            <td colspan=3><s:property value='getSaleRecordDetail.comments'/></td>
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
							<s:text name="ACT10_payment_details"/>
						</strong>
					</div>
					<div class="section-content">
						<div class="box2-content clearfix">
							<s:iterator value="getPayTypeDetail" status="status">
								<span class="left" style="width:25%;margin:3px 0;">
									<span class="ui-icon icon-arrow-crm"></span>
									<span class="bg_title"><s:property value='#application.CodeTable.getVal("1175", payTypeCode)'/></span>
									<span class="red"><s:text name="format.price"><s:param value="payAmount"/></s:text></span>
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
            			<s:text name="ACT10_results_list"/>
            		</strong>
          		</div>
          		<div class="section-content">
            		<div id="table_scroll" style="overflow-x:auto;overflow-y:hidden">
            			<table cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
              				<thead>
                				<tr>
                  					<th class="center"><s:text name="ACT10_num"/></th>          <%-- 编号 --%>
                  					<th class="center"><s:text name="ACT10_unitCode"/></th>     <%-- 厂商编码 --%>
                  					<th class="center"><s:text name="ACT10_productName"/></th>  <%-- 产品名称 --%>
                  					<th class="center"><s:text name="ACT10_barCode"/></th>      <%-- 产品条码 --%> 
                  					<th class="center"><s:text name="ACT10_moduleCode"/></th>  <%-- 计量单位 --%>
                  					<th class="center"><s:text name="ACT10_pricePay"/></th>     <%-- 销售价 --%>
                  					<th class="center"><s:text name="ACT10_quantity"/></th>     <%-- 数量 --%>
                  					<th class="center"><s:text name="ACT10_amount"/></th>       <%-- 金额 --%>
                  					<th class="center"><s:text name="ACT10_saleType"/></th>     <%-- 销售类型--%>
                  					<th class="center"><s:text name="ACT10_in_activities"/></th><%-- 参与活动--%>
                  					<th class="center"><s:text name="ACT10_comment"/></th><%-- 备注 --%>
                				</tr>
              				</thead>
              				<tbody>
                				<s:iterator value="getSaleRecordProductDetail" status="status">
                				<tr class="<s:if test='#status.odd'>odd</s:if><s:else>even</s:else>">
                	  				<td><s:property value="#status.index+1"/></td>
	                  				<td><span><s:property value="unitCode"/></span></td>
	                  				<td><span><s:property value="productName"/></span></td>
	                  				<td><span><s:property value="barCode"/></span></td>
	                  				<td class="center"><span><s:property value='#application.CodeTable.getVal("1190", moduleCode)'/></span></td>
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
	                  				<td class="alignRight">
                                        <s:if test='"N".equals(saleType)'>
                                            <s:if test='quantity > 0'>
                                                <s:text name="format.price">
                                                    <s:param value="quantity*pricePay"></s:param>
                                                </s:text>
                                            </s:if>
                                            <s:else>
                                                <span>0</span>
                                            </s:else>
                                        </s:if>
                                        <s:elseif test='"P".equals(saleType)'>
                                            <s:text name="format.price">
                                                <s:param value="pricePay"></s:param>
                                            </s:text>
                                        </s:elseif>
	                  				</td>
	                  				<td><span><s:property value='#application.CodeTable.getVal("1106", saleType)'/></span></td>
	                  				<%-- 参与活动 --%>
	                  				<td><s:property value="inActivityName"/></td>
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
                <td class="center"><s:text name="ACT10_quantity"/></td><%-- 总数量 --%>
                <td class="center"><s:text name="ACT10_amount"/></td><%-- 总金额--%>
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
          </div>
        </div>
</s:i18n>

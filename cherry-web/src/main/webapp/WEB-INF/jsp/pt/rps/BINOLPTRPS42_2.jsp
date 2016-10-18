<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<script type="text/javascript" src="/Cherry/js/pt/rps/BINOLPTRPS42.js"></script>
<style>
    .box-header {
        padding: 0.2em 0;
    }
    table.detail {
         margin-bottom: 3px;
    }
</style>
<s:set name="language" value="session.language" />
<s:i18n name="i18n.pt.BINOLPTRPS42">
<div class="main container clearfix">
	<div class="panel ui-corner-all">
        <div class="panel-header">
        <div class="clearfix">
            <span class="breadcrumb left"><span class="ui-icon icon-breadcrumb"></span><s:text name="RPS42_title"/>&nbsp;(<s:text name="RPS14_saleRecordCode"/>:<s:property value="getSaleRecordDetail.saleRecordCode"/>)</span>
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
          					<%-- 预留手机号 --%>
          					<th><s:text name="RPS42_telephone"/></th>
          					<td><s:property value="preInfoMap.telephone"/></td>
          					<%-- 预付单编号 --%>
          					<th><s:text name="RPS42_prePayNo"/></th>
          					<td><s:property value="preInfoMap.prePayNo"/></td>
          				</tr>
              		    <tr>
                            <%-- 预付柜台 --%>
                            <th><s:text name="RPS42_departName"/></th>
                            <td>(<s:property value="preInfoMap.departCode"/>)<s:property value="preInfoMap.departName"/></td>
                            <%-- BA --%>
                            <th><s:text name="RPS42_BA"/></th>
                            <td>(<s:property value="preInfoMap.employeeCode"/>)<s:property value="preInfoMap.employeeName"/></td>
                        </tr>
          				<tr>
          					<%-- 预付时间 --%>
          					<th><s:text name="RPS42_prePayDate"/></th>
          					<td><s:property value="preInfoMap.prePayDate"/></td>
                            <%-- 下次提货时间 --%>
                            <th><s:text name="RPS42_pickUpDate"/></th>
                            <td><s:property value='preInfoMap.pickupDate'/></td>
          				</tr>
          				<tr>
          					<%-- 交易类型 --%>
          					<th><s:text name="RPS42_transactionType"/></th>
          					<td>
          					<s:if test="'NS'.equals(preInfoMap.transactionType)">			
							<s:text name="RPS42_NS"/>				
							</s:if> 
							<s:if test="'SR'.equals(preInfoMap.transactionType)">						
								<s:text name="RPS42_SR"/>	
				          	</s:if>	       					
          					</td>
                            <th></th>
                            <td></td>
          				</tr>
          			</table>
                    <div class="clearfix"></div>
          		</div>
			</div>

			<div class="tabs">
          		 <ul>	
		            <li><a href="#tabs-1"><s:text name="RPS42_prePayBillDetail"/></a></li><%-- 预付单明细--%>
		            <li><a href="#tabs-2"><s:text name="RPS42_pickUpDetail"/></a>
		            </li><%-- 提货明细 --%>
		   			 </ul>
          		<div class="section-content" >
            		<div id="tabs-1" style="overflow-x:auto;overflow-y:hidden">
            			<%-- 支付方式详细为空不显示，迭代显示存在的支付方式及对应的金额 --%>
					<s:if test="payTypeList != null && payTypeList.size() > 0">
						<div class="section" style="overflow-x:auto;overflow-y:hidden">
							<div class="section-header">
								<strong>
									<span class="ui-icon icon-money"></span>
									<%-- 支付明细 --%>
									<s:text name="RPS42_payment_details"/>
								</strong>
							</div>
							<div class="section-content">
								<div class="box2-content clearfix">
									<s:iterator value="payTypeList" status="status">
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
											   <span style="display:inline-block;">(<s:text name="RPS42_SerialNumber"/>:<s:property value='serialNumber'/>)</span>
											</s:if>
										</span>
									</s:iterator>
								</div>
							</div>
						</div>
					</s:if>
            			<table cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
              				<thead>
                				<tr>
                  					<th class="center"><s:text name="RPS42_num"/></th>          	<%-- 编号 --%>
                  					<th class="center"><s:text name="RPS42_unitCode"/></th>     	<%-- 厂商编码 --%>
                  					<th class="center"><s:text name="RPS42_barCode"/></th>  		<%-- 产品条码 --%>
                  					<th class="center"><s:text name="RPS42_productName"/></th>  	<%-- 产品名称 --%> 
                  					<th class="center"><s:text name="RPS42_price"/></th>			<%-- 单价 --%>
                  					<th class="center"><s:text name="RPS42_buyQuantity"/></th>  	<%-- 购买数量 --%>
                  					<th class="center"><s:text name="RPS42_deatilAmount"/></th> 	<%-- 金额 --%>
                  					<th class="center"><s:text name="RPS42_pickupQuantity"/></th>   <%-- 已提数量 --%>
                  					<th class="center"><s:text name="RPS42_leftQuantity"/></th>     <%-- 剩余数量 --%>                  				
                				</tr>
              				</thead>
              				<tbody>
                				<s:iterator value="preDetailInfoList" status="status">
                				<tr class="<s:if test='#status.odd'>odd</s:if><s:else>even</s:else>">
                	  				<td><s:property value="#status.index+1"/></td>
	                  				<td><span><s:property value="unitCode"/></span></td>
	                  				<td><span><s:property value="barCode"/></span></td>
	                  				<td><span><s:property value="productName"/></span></td>
	                  				<td class="alignRight">
	                  					<s:if test='price != 0'>
	                  						<s:text name="format.price">
	                  							<s:param value="price"></s:param>
	                  						</s:text>
	                  					</s:if><s:else>
	                  						<span>0</span>
	                  					</s:else>
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
	                  				<td class="alignRight">
	                  					<s:if test='deatilAmount != 0'>
	                  						<s:text name="format.price">
	                  							<s:param value="deatilAmount"></s:param>
	                  						</s:text>
	                  					</s:if><s:else>
	                  						<span>0</span>
	                  					</s:else>
	                  				</td>
	                  				<td class="alignRight">
	                  					<s:if test='pickupQuantity > 0'>
	                  						<s:text name="format.number">
	                  							<s:param value="pickupQuantity"></s:param>
	                  						</s:text>
	                  					</s:if><s:else>
	                  						<span>0</span>
	                  					</s:else>
	                  				</td>
	                  				<td class="alignRight">
	                  					<s:if test='leftQuantity > 0'>
	                  						<s:text name="format.number">
	                  							<s:param value="leftQuantity"></s:param>
	                  						</s:text>
	                  					</s:if><s:else>
	                  						<span>0</span>
	                  					</s:else>
	                  				</td>
	                			</tr>
                				</s:iterator>
              				</tbody>
            			</table>
            			<hr class="space" />
            			<table cellpadding="0" cellspacing="0" width="30%" border="0" class="right editable">
			              <tr>
			                <th rowspan="2" scope="row" class="center"><s:text name="global.page.total"/></th><%-- 合计 --%>
			                <td class="center"><s:text name="RPS42_buyQuantity"/></td><%-- 购买数量 --%>
			                <td class="center"><s:text name="RPS42_prePayAmount"/></td><%-- 预付金额--%>
			                <td class="center"><s:text name="RPS42_leftQuantity"/></td><%-- 剩余数量--%>
			              </tr>
			              <tr>
			                <td class="center">
			                	<s:if test="preInfoMap.buyQuantity >= 0"><s:text name="format.number"><s:param value="preInfoMap.buyQuantity"></s:param></s:text></s:if>
			                	<s:else><span class="highlight"><s:text name="format.number"><s:param value="preInfoMap.buyQuantity"></s:param></s:text></span></s:else>
			                </td>
			             	<td class="center">
			             		<s:if test="preInfoMap.prePayAmount >= 0"><s:text name="format.price"><s:param value="preInfoMap.prePayAmount"></s:param></s:text></s:if>
			             		<s:else><span class="highlight"><s:text name="format.price"><s:param value="preInfoMap.prePayAmount"></s:param></s:text></span></s:else>
			             	</td>
			             	<td class="center">
			             		<s:if test="preInfoMap.leftQuantity >= 0"><s:text name="format.number"><s:param value="preInfoMap.leftQuantity"></s:param></s:text></s:if>
			             		<s:else><span class="highlight"><s:text name="format.number"><s:param value="preInfoMap.leftQuantity"></s:param></s:text></span></s:else>
			             	</td>
			              </tr>
			            </table>
            	
           			</div>
           			<div id="tabs-2" style="overflow-x:auto;overflow-y:hidden">
		   		<table cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
              				<thead>
                				<tr>
                  					<th class="center"><s:text name="RPS42_num"/></th>          	<%-- 编号 --%>
                  					<th class="center"><s:text name="RPS42_unitCode"/></th>     	<%-- 厂商编码 --%>
                  					<th class="center"><s:text name="RPS42_barCode"/></th>  		<%-- 产品条码 --%>
                  					<th class="center"><s:text name="RPS42_productName"/></th>  	<%-- 产品名称 --%> 
                  					<th class="center"><s:text name="RPS42_pickupQuantity"/></th>   <%-- 已提数量 --%>         				
                				</tr>
              				</thead>
              				<tbody>
                				<s:iterator value="pickDetailInfoList" status="status">
                				<tr class="<s:if test='#status.odd'>odd</s:if><s:else>even</s:else>">
                	  				<td><s:property value="#status.index+1"/></td>
	                  				<td><span><s:property value="unitCode"/></span></td>
	                  				<td><span><s:property value="barCode"/></span></td>
	                  				<td><span><s:property value="productName"/></span></td>
	                  				<td class="alignRight">
	                  					<s:if test='quantity !=0'>
	                  						<s:text name="format.number">
	                  							<s:param value="quantity"></s:param>
	                  						</s:text>
	                  					</s:if><s:else>
	                  						<span>0</span>
	                  					</s:else>
	                  				</td>
	                  				
	                			</tr>
                				</s:iterator>
              				</tbody>
            			</table>
            			<hr class="space" />
		            	<table cellpadding="0" cellspacing="0" width="30%" border="0" class="right editable">
			              <tr>
			                <th rowspan="2" scope="row" class="center"><s:text name="global.page.total"/></th><%-- 合计 --%>
			                <td class="center"><s:text name="RPS42_pickupQuantity"/></td><%-- 已提数量 --%>
			              </tr>
			              <tr>
			              <td class="center">			         
		             		<s:if test="pickUpMap.pickupQuantity >= 0"><s:text name="format.number"><s:param value="pickUpMap.pickupQuantity"></s:param></s:text></s:if>
		             		<s:else><span class="highlight"><s:text name="format.number"><s:param value="pickUpMap.pickupQuantity"></s:param></s:text></span></s:else>
			             	</td>
			              </tr>
			            </table>
				</div>
           			
            
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

<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%-- 语言环境 --%>
<s:i18n name="i18n.pt.BINOLPTRPS13">
	  		<div class="section">
	            <div class="section-header">
	            	<strong>
	            		<span class="ui-icon icon-ttl-section-info"></span>
	            		<s:text name="RPS13_saleTotal"/><%-- 销售单据统计信息 --%>
	            	</strong>
	            </div>
	            <div class="section-content" id="shwoMainInfo">
	            	<%-- 符合查询条件的单据数: --%>
					<s:text name="RPS13_saleSum"/>
					<span class="<s:if test='sumInfoBySaleDateArea.sum < 0'>highlight</s:if><s:else>green </s:else>" style='margin-left:0px;margin-right:5px;'>
					 <strong> 
					 	<s:text name="format.number"><s:param value="sumInfoBySaleDateArea.sum"></s:param></s:text>  
					 </strong> 
	                </span>
	             	<%-- 销售总数量 --%>
	                <s:text name="RPS13_sumQuantity"/>
					<span class="<s:if test='sumInfoBySaleDateArea.sumQuantity < 0'>highlight</s:if><s:else>green </s:else>" style='margin-left:0px;margin-right:5px;'>
						<strong><s:text name="format.number"><s:param value="sumInfoBySaleDateArea.sumQuantity"></s:param></s:text></strong>
					</span>
					<%-- 销售总金额 --%>
	                <s:text name="RPS13_sumAmount"/>
				    <span class="<s:if test='sumInfoBySaleDateArea.sumAmount < 0'>highlight</s:if><s:else>green </s:else>" style='margin-left:0px;margin-right:5px;'>
				    	<strong><s:text name="format.price"><s:param value="sumInfoBySaleDateArea.sumAmount"></s:param></s:text></strong>
				    </span>
                </div>
            </div>
            <div scrollleft="0" scrolltop="0" style="height: 5px;">
            </div>
			<div class="section">
		          <div class="section-header">
			          <strong><span class="ui-icon icon-ttl-section-info"></span>
			          <s:text name="RPS13_saleDetailTotal" /><%-- 各商品统计信息 --%>
			          </strong>
		          </div>
		        <%--dataTabel area --%>
		          <div class="section-content">
		            <form method="post">
		
						<table  cellpadding="0" cellspacing="0" border="0"  style="width:100%;">
							<thead>
								<tr>
								   <th ><s:text name="RPS13_proPrmName" /></th>
								   <th ><s:text name="RPS13_proPrmUnitCode" /></th>
								   <th ><s:text name="RPS13_proPrmBarCode" /></th>
								   <th ><s:text name="RPS13_proPrmSumQuantity" /></th>
								   <th ><s:text name="RPS13_proPrmSumAmount" /></th>
								   <th ><s:text name="RPS13_quantityPercent" /></th>
								   <th ><s:text name="RPS13_amountPercent" /></th>
								</tr>
							</thead>
							<tbody>
								<s:iterator value="saleProPrmDetailList" id="saleProPrm">
									<tr>
										<%-- 商品名称 --%>
										<td><span class="left">${saleProPrm.nameTotal}</span></td>
										<%-- 编码 --%>
										<td><span class="left">${saleProPrm.unitCode}</span></td>
										<%-- 条码 --%>
										<td><span class="left">${saleProPrm.barCode}</span></td>
									
										<%-- 总数量 --%>
										<td>
											<s:if test="quantity >= 0"><span class="right"><s:text name="format.number"><s:param value="quantity"></s:param></s:text></span></s:if>
											<s:else><span class="highlight right"><s:text name="format.number"><s:param value="quantity"></s:param></s:text></span></s:else>
										</td>
										<%-- 总金额  --%>
										<td>
											<s:if test="amount >= 0"><span class="right"><s:text name="format.price"><s:param value="amount"></s:param></s:text></span></s:if>
											<s:else><span class="highlight right"><s:text name="format.price"><s:param value="amount"></s:param></s:text></span></s:else>
										</td>
										<%-- 商品总数量占比  --%>
										<td>
											<s:if test='%{"P".equals(#saleProPrm.type)}'><span class="right"> — </span></s:if>
											<s:else><span class="right">${saleProPrm.quantityPercent}</span></s:else>
										</td>
										<%-- 商品总金额占比  --%>
										<td>
											<s:if test='%{"P".equals(#saleProPrm.type)}'><span class="right"> — </span></s:if>
											<s:else><span class="right">${saleProPrm.amountPercent}</span></s:else>
										</td>
									</tr>
								</s:iterator>
							</tbody>
						</table>
		            </form>
		          </div>
            </div>

</s:i18n>
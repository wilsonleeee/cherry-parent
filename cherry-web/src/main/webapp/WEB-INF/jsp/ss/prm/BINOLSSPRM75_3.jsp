<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<s:i18n name="i18n.ss.BINOLSSPRM75">
<div class="main container clearfix">
	<div class="panel ui-corner-all">
		<div id="div_main">
			<cherry:form id="mainForm" method="post" class="inline" csrftoken="false">
				<div class="panel-header"> 
		        	<div class="clearfix">
        				<span class="breadcrumb left"><span class="ui-icon icon-breadcrumb"></span><s:text name="CouponSearch"/> &gt;
		        		<s:text name="SearchCoupon"/>
		        		</span> 
		        	</div>
		      	</div>
		      	<div class="panel-content">
		      		<div class="box4" id="baseInfo">
			      		<div class="box4-header clearfix">
							<strong class="left"><s:text name="BaseInfo"/></strong>
						</div>
						<div class="box4-content clearfix">
							<table class="detail">
								<tbody>
									<tr>
				                  		<th><s:text name="CouponNo"/><span class="red">*</span></th>
				                  		<td><span><s:property value='couponInfo.couponNo'/></span></td>
				                  		<th><s:text name="CouponType"/><span class="red">*</span></th>
				                   		<td><span>
				                   		<s:property value="#application.CodeTable.getVal('1383',couponInfo.couponType)"/>
				                   		</span></td>
									</tr>
									<tr>
				                  		<th><s:text name="CouponRule"/><span class="red">*</span></th>
				                  		<td><span><s:property value='couponInfo.ruleCode'/></span></td>
				                  		<th><s:text name="CouponCode"/><span class="red">*</span></th>
				                   		<td><span>
				                   		<s:property value='couponInfo.couponCode'/></span>
				                   		</td>
									</tr>
									<tr>
										<th><s:text name="RelationBill"/><span class="red">*</span></th>
										<td><span><s:property value='couponInfo.relationBill'/></span></td>
										<th><s:text name="BillCode"/><span class="red">*</span></th>
										<td><span><s:property value='couponInfo.billCode'/></span></td>
									</tr>
									<tr>
										<th><s:text name="TotalQuantity"/><span class="red">*</span></th>
										<td><span><s:property value="couponInfo.totalQuantity"/></span></td>
										<th><s:text name="OriginalAmount"/><span class="red">*</span></th>
										<td><span><s:property value='couponInfo.originalAmount'/></span></td>										
									</tr>
									<tr>
										<th><s:text name="DiscountAmount"/><span class="red">*</span></th>
										<td><span><s:property value="couponInfo.discountAmount"/></span></td>
										<th><s:text name="IntegralAmount"/><span class="red">*</span></th>
										<td><span><s:property value='couponInfo.integralAmount'/></span></td>										
									</tr>
									<tr>
										<th><s:text name="TotalAmount"/><span class="red">*</span></th>
										<td><span><s:property value="couponInfo.totalAmount"/></span></td>
										<th><s:text name="UseTime"/><span class="red">*</span></th>
										<td><span><s:property value='couponInfo.useTime'/></span></td>
									</tr>
									<tr>
										<th><s:text name="MemberCode"/><span class="red">*</span></th>
										<td><span><s:property value='couponInfo.userMemCode'/></span></td>
										<th><s:text name="MemberName"/><span class="red">*</span></th>
										<td><span><s:property value='couponInfo.memberName'/></span></td>										
									</tr>
									<tr>
										<th><s:text name="MemberMobile"/><span class="red">*</span></th>
										<td><span><s:property value='couponInfo.userMobile'/></span></td>
										<th><s:text name="MemberBPCode"/><span class="red">*</span></th>
										<td><span><s:property value='couponInfo.bpCode'/></span></td>											
									</tr>
									<tr>										
										<th><s:text name="UnitCode"/><span class="red">*</span></th>
										<td><span><s:property value='couponInfo.unitCode'/></span></td>
										<th><s:text name="BarCode"/><span class="red">*</span></th>
										<td><span><s:property value='couponInfo.barCode'/></span></td>
									</tr>
									<tr>
										<th><s:text name="CounterCode"/><span class="red">*</span></th>
										<td><span><s:property value='couponInfo.counterCode'/></span></td>
										<th><s:text name="CountName"/><span class="red">*</span></th>
										<td><span><s:property value='couponInfo.counterName'/></span></td>
									</tr>
								</tbody>
							</table>
						</div>    			
		      		</div>
		      	</div>
			</cherry:form>
			<div class="center clearfix" id="pageBrandButton" style="margin-top:50px;">
				<button class="close" onclick="window.close();">
				<span class="ui-icon icon-close"></span>
				<span class="button-text"><s:text name="global.page.close" /></span>
				</button>
			</div>
		</div>
	</div>
</div>
</s:i18n>
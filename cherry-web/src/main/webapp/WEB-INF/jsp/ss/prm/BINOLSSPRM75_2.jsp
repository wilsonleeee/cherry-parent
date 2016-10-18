<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/ss/prm/BINOLSSPRM75_2.js"></script>
<s:url id="save_url" action="BINOLSSPRM75_save"/>
<div class="hide">
	<a id="saveUrl" href="${save_url}"></a>
</div>
<s:i18n name="i18n.ss.BINOLSSPRM75">
<div class="main container clearfix">
	<div class="panel ui-corner-all">
		<div id="div_main">
			<cherry:form id="mainForm" method="post" class="inline" csrftoken="false">
				<div class="panel-header"> 
		        	<div class="clearfix">
        				<span class="breadcrumb left"><span class="ui-icon icon-breadcrumb"></span><s:text name="CouponSearch"/> &gt;
		        		<s:text name="CouponEdit"/>
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
				                  		<td><span><input id="couponNo" class="text" name ="couponNo" value="<s:property value='couponInfo.couponNo'/>" disabled="disabled"/></span></td>
				                  		<th><s:text name="CouponType"/><span class="red">*</span></th>
				                   		<td><span>
				                   		<input class="text" name ="couponInfo.couponType" maxlength="25" value="<s:property value="#application.CodeTable.getVal('1383',couponInfo.couponType)"/>" disabled="disabled"/></span>
				                   		</td>
									</tr>
									<tr>
				                  		<th><s:text name="CouponRule"/><span class="red">*</span></th>
				                  		<td><span><input class="text" name ="couponInfo.ruleCode" maxlength="40" value="<s:property value='couponInfo.ruleCode'/>" disabled="disabled"/></span></td>
				                  		<th><s:text name="CouponCode"/><span class="red">*</span></th>
				                   		<td><span>
				                   		<input class="text" name ="couponInfo.couponCode" maxlength="25" value="<s:property value='couponInfo.couponCode'/>" disabled="disabled"/></span>
				                   		</td>
									</tr>
									<tr>
										<th><s:text name="CouponTime"/><span class="red">*</span></th>
				                  		<td>
				                   		<span>
				                   		<input class="date" id="startTime" name="startTime" value="<s:property value='couponInfo.startTime'/>"/>
				                   		-				                
				                   		<input class="date" id="endTime" name="endTime" value="<s:property value='couponInfo.endTime'/>"/>
				                   		</span>
										</td>
										<th><s:text name="CouponStatus"/><span class="red">*</span></th>
										<td><span><input class="text" name ="couponInfo.status" maxlength="25" value="<s:property value="#application.CodeTable.getVal('1384',couponInfo.status)"/>" disabled="disabled"/></span></td>
									</tr>
								</tbody>
							</table>
						</div>    			
		      		</div>
		      	</div>
			</cherry:form>
			<div class="center clearfix" id="pageBrandButton" style="margin-top:50px;">
				<button class="save" type="submit" onclick="binolssprm75_2.saveCoupon()">
				<span class="ui-icon icon-save"></span>
				<span class="button-text"><s:text name="global.page.save" /></span>
				</button>
				<button class="close" onclick="window.close();">
				<span class="ui-icon icon-close"></span>
				<span class="button-text"><s:text name="global.page.close" /></span>
				</button>
			</div>
		</div>
	</div>
</div>
</s:i18n>
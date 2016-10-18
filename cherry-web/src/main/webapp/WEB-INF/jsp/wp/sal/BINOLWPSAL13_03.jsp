<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/wp/sal/BINOLWPSAL13.js"></script>
<s:i18n name="i18n.wp.BINOLWPSAL13">
<%-- <div class="hide">
<div id="cardType"><s:property value="card.CardType"/></div>

</div> --%>
<form id="rechargeForm" method="post" class="inline">
        <div class="wp_tablebox">
        <table width="100%" cellspacing="0" cellpadding="0" border="0" class="wp_table" id="table_all">
            <tbody>
                <tr>
                	<th><s:text name="wpsal13.cardCode"></s:text></th>
                  	<th><s:text name="wpsal13.currentAvailableAmount"></s:text></th>
                  	<%-- <th><s:text name="wpsal13.cardType"></s:text></th> --%>
               		<th><s:text name="wpsal13.RelationMemberCardCode"></s:text></th>
               		<th><s:text name="wpsal13.reserveMobilePhone"></s:text></th>
               		<th><s:text name="wpsal13.channel"></s:text></th>
               		<th><s:text name="wpsal13.theSender"></s:text></th>
               		<th width="140px"><s:text name="wpsal13.giveCardTime"></s:text></th>
               		<th width="200px"><s:text name="wpsal13.act"></s:text></th>
                </tr>
                <s:iterator value="cardList">
	                <!-- <tr onclick="BINOLWPSAL13.showDetailed(this);return false;" style="cursor:hand"> -->
	                <tr style="cursor:hand">
	                	<td id="dgCardCode"><s:property value="CardCode"/></td>
	                  	<td><s:property value="Amount"/></td>
	                  	<%-- <td><s:property value="CardType==1?'储值卡':'服务卡'"/></td> --%>
	                  	<td><s:property value="MemberCode"/></td>
	                  	<td><s:property value="MobilePhone"/></td>
	                  	<td><s:property value="CounterCode"/></td>
	                  	<td><s:property value="EmployeeCode"/></td>
	                  	<td><s:property value="GetTime"/></td>
	                  	<td>
		          			<button class="wp_search_s" type="button" style="float:left; margin:0 5px 0 0" onclick="BINOLWPSAL13.transactionDetailInit(this);return false;">
								<span class="icon_search"></span>
								<span class="wp_search_text"><s:text name="wpsal13.transactionDetail" /></span>
							</button>
		          			
		          			<s:if test="ServiceDetail != null">
			          			<button id="showDetailed" class="wp_search_s" type="button" style="float:left; margin:0 5px 0 0" onclick="BINOLWPSAL13.showDetailed(this);return false;">
									<span class="icon_search"></span>
									<span class="wp_search_text"><s:text name="wpsal13.searchService" /></span>
								</button>
			          			
			          			<button id="hideDetailed" class="wp_search_s" type="button" style="display:none;float:left; margin:0 5px 0 0" onclick="BINOLWPSAL13.hideDetailed(this);return false;">
									<span class="icon_search"></span>
									<span class="wp_search_text"><s:text name="wpsal13.hideService" /></span>
								</button>
		          			</s:if>
	          			</td>
	                </tr>
	                  	<s:if test="ServiceDetail != null">
	                  			<tr style="display: none;" class="detail" id="<s:property value='CardCode'/>">
		                  			<td colspan="8" class="detail box2-active">
		                  			<div class="detail_box">
		                  				<table id="serviceDetailTable" width="100%" cellspacing="0" cellpadding="0" border="0" class="wp_table">
		                  					<tr>
		                  						<th><s:text name="wpsal13.serviceType"></s:text></th>
		                  						<th><s:text name="wpsal13.availableServiceNumber"></s:text></th>
		                  						<th><s:text name="wpsal13.sumRechargeServiceNumber"></s:text></th>
		                  						<th><s:text name="wpsal13.latelyUseTime"></s:text></th>
		                  					</tr>
				                  			<s:iterator value="ServiceDetail">
			                  					<tr>
			                  						<td>
			                  							<s:iterator value="serverList">
			                  								<s:if test="CodeKey==ServiceType">
						                  						<s:property value="Value"/>
			                  								</s:if>
			                  							</s:iterator>
			                  						</td>
			                  						<td><s:property value="ServiceQuantity"/></td>
			                  						<td><s:property value="TotalQuantity"/></td>
			                  						<td><s:property value="LastUseTime"/></td>
			                  					</tr>
				                  			</s:iterator>
		                  				</table>
		                  				</div>
		                  			</td>
	                  			</tr>
	                  	</s:if>
                </s:iterator>
            </tbody>
        </table>
    </div>
    <div class="bottom_butbox clearfix">
    	<button id="btnConfirm" class="close" type="button" onclick="BINOLWPSAL13.confirm2();return false;">
    		<span class="ui-icon icon-close"></span>
            <span class="button-text"><s:text name="wpsal13.cancel"/></span>
		</button>
    </div>
</form>
</s:i18n>
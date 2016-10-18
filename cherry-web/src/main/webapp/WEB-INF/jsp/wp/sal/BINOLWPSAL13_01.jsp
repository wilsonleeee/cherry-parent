<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/wp/sal/BINOLWPSAL13.js"></script>
<s:i18n name="i18n.wp.BINOLWPSAL13">
<s:text name="global.page.select" id="select_default"></s:text>
<div class="hide">
<%-- <input class="hide" id="cardType" name="cardType" value="<s:property value='cardType'/>"/> --%>
	<%--开卡 --%>
	<s:url id="s_createCard" value="/wp/BINOLWPSAL13_createCard" />
	<a id="createCard" href="${s_createCard}"></a>
</div>
    <div class="wp_tablebox">
        <table width="100%" cellspacing="0" cellpadding="0" border="0" class="wp_table">
        <caption><s:text name="wpsal13.useCard"></s:text></caption>
            <tbody>
               <tr>
	               <td>
	               		<s:text name="wpsal13.cardCode"></s:text>:
	               		<span>
	               			<input class="hide" id="cardCode" name="cardCode" class="text ac_input" readonly="readonly"/>
	               			<span id="cardCodeSpan"></span>
	               		</span>
	               	</td>
	            	<td>
	               		<s:text name="wpsal13.currentAvailableAmount"></s:text>:
	               		<span>
	               			<span id="R_amountSpan"></span>
	               		</span>
	               	</td>
	             	<td>
	               		<s:text name="wpsal13.reserveMobilePhone"></s:text>:
	               		<span>
	               			<input class="text hide ac_input" style="width: 120px" id="mobilePhone2" name="mobilePhone2" readonly="readonly"/><span class="highlight" id="mobilePhone2cls">*</span>
	               			<span id="mobilePhone2Span"></span>
	               		</span>
	               </td>
               </tr>
            </tbody>
        </table>
    </div>
    

<form id="rechargeForm" method="post" class="inline">
    <div class="wp_tablebox">
        <table width="100%" cellspacing="0" cellpadding="0" border="0" class="wp_table">
        <caption><s:text name="wpsal13.rechargeService"></s:text></caption>
            <tbody>
            	<tr>
            		<td>
            			<s:text name="wpsal13.businessType" />
            			<s:select onchange="BINOLWPSAL13.businessTypeChange();return false;" id="businessType" list="#application.CodeTable.getCodes('1350')" listKey="CodeKey" listValue="Value"/>
            			<s:text name="wpsal13.transactionAmount" />
            			<s:select onchange="BINOLWPSAL13.radio(this);return false;" name="saleType" id="samount" list="#application.CodeTable.getCodes('1342')" listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{select_default}"/>
            			<span id="ywselect" class="inline">
	            				<span id="amountSpan"><span id="pAmount"><s:text name="wpsal13.buyAmount" /></span><span id="cAmount"><s:text name="wpsal13.rechargeamount" /></span>
	            					<input id="fwamount" onkeyup="BINOLWPSAL13.checkNumber(this);return false" class="text ac_input" name="fwamount"/>
	            				</span>
            			</span>
            		</td>
	             	<s:iterator value="rechargeDiscountList">
            		<td class="hide">
		                    <div class="hide"><input name="RechargeMinValue" id="RechargeMinValue" value='<s:property value="RechargeMinValue"/>'/></div>
		                    <div class="hide"><input name="GiftAmount" id="GiftAmount" value='<s:property value="GiftAmount"/>'/></div>
		                    <div class="hide"><input name="ServiceType" id="ServiceType" value='<s:property value="ServiceType"/>'/></div>
		                    <div class="hide"><input name="SubDiscountName" id="SubDiscountName" value='<s:property value="SubDiscountName"/>'/></div> 
		                    <div class="hide"><input name="RechargeMaxValue" id="RechargeMaxValue" value='<s:property value="RechargeMaxValue"/>'/></div>
		                    <div class="hide"><input name="DiscountType" id="DiscountType" value='<s:property value="DiscountType"/>'/></div>
		                    <div class="hide"><input name="ServiceQuantity" id="ServiceQuantity" value='<s:property value="ServiceQuantity"/>'/></div>
		                    <div class="hide"><input name="CardTypeD" id="CardTypeD" value='<s:property value="CardType"/>'/></div>
		            </td>
	            	</s:iterator>
            	</tr>
            </tbody>
        </table>
    </div>
    
     <div id="opencxk">
        <div class="wp_tablebox">
	        <table width="100%" cellspacing="0" cellpadding="0" border="0" class="wp_table" id="fwk">
	        <caption></caption>
	            <tbody>
	                <tr>
	                  	<th><s:text name="wpsal13.currentAvailableAmount"></s:text></th>
	                  	<th><s:text name="wpsal13.discountName"></s:text></th>
	               		<th><s:text name="wpsal13.giftAmount"></s:text></th>
	               		<th><s:text name="wpsal13.rechargeamount"></s:text></th>
	                </tr>
	                <tr>
	                  	<td id="cardCodeSpanTD"></td>
	                  	<td><span id="subDiscountName" ></span></td>
	                 	<td><input readonly="readonly"onkeyup="BINOLWPSAL13.checkNumber(this);return false"  class="text ac_input" id="giftAmount" style="width:100px;" name="giftAmount" /></td>
	                 	<td><input readonly="readonly"onkeyup="BINOLWPSAL13.checkNumber(this);return false"  class="text ac_input" id="amount" style="width:100px;" name="amount" /></td>
	                </tr>
	                <tr>
	                	<td><s:text name="wpsal13.comments"></s:text></td>
	                	<td colspan="3">
	                		<textarea name="memo" style="width:500px;"></textarea>
	                	</td>
	                </tr>
	            </tbody>
	        </table>
    	</div>
    </div>
    
    <div id="openfwk">
        <div class="wp_tablebox">
        <table width="100%" cellspacing="0" cellpadding="0" border="0" class="wp_table" id="tyk">
        <caption></caption>
            <tbody>
            <tr>
            	<th><s:text name="wpsal13.serviceType"></s:text></th>
            	<th><s:text name="wpsal13.remainingNumber"></s:text></th>
            	<th><s:text name="wpsal13.discountName"></s:text></th>
            	<th id="purchaseTH"><s:text name="wpsal13.purchaseNumber"></s:text></th>
            	<th id="purchaseTH2"><s:text name="wpsal13.frequency"></s:text></th>
            </tr>
            <s:iterator value="serverList" id="server">
            	<tr id='<s:property value="#server.CodeKey"/>'>
	                <td>
	               		<s:property value="#server.Value"/>
	               	</td>
	               	<td>
	               		<s:iterator value="consumptionCodeMap.ServiceDetail" id="c">
	                		<s:if test="#server.CodeKey == #c.ServiceType">
	               				<s:property value="#c.ServiceQuantity"/>
	                		</s:if>
	                	</s:iterator>
	                	<input name="serviceType" class="hide" value="<s:property value='#server.CodeKey'/>"/>
	               	</td>
	               	<td>
	               		<div id="subDiscountNameDiv"></div>
	               		<input disabled="disabled" class="text ac_input hide" name="subDiscountName"/>
	               	</td>
	               	<td>
	               		<div id="serviceQuantityDiv"></div>
	               		<input disabled="disabled" class="text ac_input" style="width:100px;" onkeyup="BINOLWPSAL13.checkNumber(this);return false" name="serviceQuantity"/>
	               	</td>
               	</tr>
            </s:iterator>
            	<tr>
            		<td><s:text name="wpsal13.comments"></s:text></td>
                	<td colspan="3">
                		<textarea name="memo" style="width:500px;"></textarea>
                	</td>
                </tr>
            </tbody>
        </table>
    </div>
    </div>
    <div class="bottom_butbox clearfix">
    	<button id="btnConfirm" class="close" type="button" onclick="BINOLWPSAL13.commit();return false;">
    		<span class="ui-icon icon-confirm"></span>
            <span class="button-text"><s:text name="wpsal13.confirm"/></span>
		</button>
		<button id="btnCrefirm" class="close" type="button" onclick="BINOLWPSAL13.createCard();return false;">
    		<span class="ui-icon icon-confirm"></span>
            <span class="button-text"><s:text name="wpsal13.confirm"/></span>
		</button>
		<button id="btnCancel" class="close" type="button" onclick="BINOLWPSAL13.cancel();return false;">
			<span class="ui-icon icon-close"></span>
            <span class="button-text"><s:text name="wpsal13.cancel"/></span>
		</button>
    </div>
</form>
</s:i18n>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/mb/svc/BINOLMBSVC03_1.js?v=20160721"></script>
<s:i18n name="i18n.mb.BINOLMBSVC03">
<s:text name="global.page.select" id="select_default"></s:text>
<div class="hide">
	<s:url id="s_savingCardRecharge" value="/mb/BINOLMBSVC03_savingCardRecharge" />
		<a id="savingCardRechargeUrl" href="${s_savingCardRecharge}"></a>
		<div id="result_dialog_success" class="hide"><s:text name="SVC03_result_success"></s:text> </div>
	<div id="result_dialog_error" class="hide"><s:text name="SVC03_result_error"></s:text> </div>
	<div id="result_dialog" class="hide">
	<div class="dialog2 clearfix" 
		id="result_dialog">
		<p class="clearfix">
		<p class="message">
			<span></span>
		</p>
	</div>
	</div>
</div>
    <div class="wp_tablebox">
        <table width="100%" cellspacing="0" cellpadding="0" border="0" class="wp_table">
        <caption><s:text name="SVC03_useCard"></s:text></caption>
            <tbody>
               <tr>
	               <td>
	               		<s:text name="SVC03_cardCode"></s:text>
	               		<span>
	               			<input class="hide" id="svc01_cardCode" name="cardCode" class="text ac_input" readonly="readonly" value='<s:property value="card.CardCode"/>'/>
	               			<span>${card.CardCode }</span>
	               		</span>
	               	</td>
	            	<td>
	               		<s:text name="SVC03_currentAvailableAmount"></s:text>:
	               		<span>
	               			<span>${card.Amount }</span>
	               		</span>
	               	</td>
	             	<td>
	               		<s:text name="SVC03_reserveMobilePhone"></s:text>:
	               		<span>
	               			<input class="text hide ac_input" style="width: 120px" id="mobilePhone" name="mobilePhone" readonly="readonly"/>
	               			<span>${card.MobilePhone }</span>
	               		</span>
	               </td>
               </tr>
            </tbody>
        </table>
    </div>
    

<form id="rechargeForm" method="post" class="inline">
    <div class="wp_tablebox">
        <table width="100%" cellspacing="0" cellpadding="0" border="0" class="wp_table">
        <caption><s:text name="SVC03_RechargeService"></s:text></caption>
            <tbody>
            	<tr>
            		<td>
            			<s:text name="SVC03_serviceMode"></s:text>
            			<span><s:select
									name="businessType" Cssstyle="width:186px;"
									list='#application.CodeTable.getCodes("1350")'
									listKey="CodeKey" listValue="Value" 
									headerValue="%{selectAll}" value="1" onchange="BINOLMBSVC03_1.businessTypeChange();return false;" cssclass="text"/>
						</span>
            			<s:text name="SVC03_transactionAmount" />
            			<span>
            			<s:select onchange="BINOLMBSVC03_1.radio(this)" name="saleType" id="samount" list="#application.CodeTable.getCodes('1342')" listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{select_default}"/>
            			</span>
            			<span>
	               			<s:text name="SVC03_countSelect"></s:text>
	               			<span>
	               			${card.CounterCode }
	               			</span>
	               			<input class="hide" id="counterName" name="counterName" class="text ac_input" readonly="readonly" value='<s:property value="card.CounterCode"/>'/>
               			</span>
            			<span id="ywselect" class="inline">
	            				<span id="amountSpan"><span id="pAmount"><s:text name="SVC03_payamount"></s:text></span><span id="cAmount"><s:text name="SVC03_Amount"></s:text></span>
	            					<input id="fwamount" class="text ac_input" name="fwamount" style="width:80px"/>
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
	                  	<th><s:text name="SVC03_currentAvailableAmount"></s:text></th>
	                  	<th><s:text name="SVC03_ruleName"></s:text></th>
	               		<th><s:text name="SVC03_giftAmount"></s:text></th>
	               		<th><s:text name="SVC03_Amount"></s:text></th>
	                </tr>
	                <tr>
	                  	<td><s:property value="card.Amount"/></td>
	                  	<td><span id="subDiscountName" ></span></td>
	                 	<td><span><input readonly="readonly" class="text ac_input" id="giftAmount" style="width:100px;" name="giftAmount" /></span></td>
	                 	<td><span><input readonly="readonly" class="text ac_input" id="amount" style="width:100px;" name="amount" /></span></td>
	                </tr>
	                <tr>
	                	<td><s:text name="SVC03_memo"></s:text></td>
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
            	<th><s:text name="SVC03_serviceType"></s:text></th>
            	<th><s:text name="SVC03_quantity"></s:text></th>
            	<th><s:text name="SVC03_ruleName"></s:text></th>
            	<th id="purchaseTH"><s:text name="SVC03_payCount"></s:text></th>
            	<th id="purchaseTH2"><s:text name="SVC03_giftCount"></s:text></th>
            </tr>
            <s:iterator value="serverList" id="server">
            	<tr id='<s:property value="#server.CodeKey"/>'>
	                <td>
	               		<s:property value="#server.Value"/>
	               	</td>
	               	<td>
	               		<s:iterator value="card.ServiceDetail" id="c">
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
	               		<input disabled="disabled" class="text ac_input" style="width:100px;"  name="serviceQuantity"/>
	               	</td>
               	</tr>
            </s:iterator>
            	<tr>
            		<td><s:text name="SVC03_memo"></s:text></td>
                	<td colspan="3">
                		<textarea name="memo" style="width:500px;"></textarea>
                	</td>
                </tr>
            </tbody>
        </table>
    </div>
    </div>
    <div class="center clearfix">
    	<button id="btnConfirm" class="close" type="button" onclick="BINOLMBSVC03_1.commit();return false;">
    		<span class="ui-icon icon-confirm"></span>
            <span class="button-text"><s:text name="SVC03_confirm"/></span>
		</button>
		<button id="btnCancel" class="close" type="button" onclick="BINOLMBSVC03_1.cancel();return false;">
			<span class="ui-icon icon-close"></span>
            <span class="button-text"><s:text name="SVC03_cancel"/></span>
		</button>
    </div>
</form>
</s:i18n>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/wp/common/LodopFuncs.js?V=20160913"></script>
<script type="text/javascript" src="/Cherry/js/wp/sal/BINOLWPSAL03.js?V=2017011632"></script>
<s:i18n name="i18n.wp.BINOLWPSAL03">

	<div class="hide">
		<s:url id="s_dgSaleUrl" value="/wp/BINOLWPSAL02_init" />
		<a id="dgSaleUrl" href="${s_dgSaleUrl}"></a>
		<s:url id="s_dgCollectUrl" value="/wp/BINOLWPSAL03_collect" />
		<a id="dgCollectUrl" href="${s_dgCollectUrl}"></a>
		<s:url id="s_dgPaymentInitUrl" value="/wp/BINOLWPSAL03_paymentInit" />
		<a id="dgPaymentInitUrl" href="${s_dgPaymentInitUrl}"></a>
		<s:url id="s_dgSetPaymentUrl" value="/wp/BINOLWPSAL03_setPayment" />
		<a id="dgSetPaymentUrl" href="${s_dgSetPaymentUrl}"></a>
		<s:url id="s_dgWebPaymentUrl" value="/wp/BINOLWPSAL03_webPayment" />
		<a id="dgWebPaymentUrl" href="${s_dgWebPaymentUrl}"></a>
		<s:url id="s_dgPayResultUrl" value="/wp/BINOLWPSAL12_init" />
		<a id="dgPayResultUrl" href="${s_dgPayResultUrl}"></a>
		<s:url id="s_dgHangBillUrl" value="/wp/BINOLWPSAL05_hangBill" />
		<a id="dgHangBillUrl" href="${s_dgHangBillUrl}"></a>
		<!-- 获取验证码地址 -->
		<s:url id="s_dgGetVerificationCode" value="/wp/BINOLWPSAL03_getVerificationCode" />
		<a id="dgGetVerificationCode" href="${s_dgGetVerificationCode}"></a>
		<!-- 查询服务次数 -->
		<s:url id="s_dgServerList" value="/wp/BINOLWPSAL03_getServerList" />
		<a id="dgServerList" href="${s_dgServerList}"></a>
	</div>
	<input type="hidden" id="collectTitleNS" value='<s:text name="wpsal03.collectTitleNS"/>'/>
	<input type="hidden" id="collectTitleSR" value='<s:text name="wpsal03.collectTitleSR"/>'/>
	<input type="hidden" id="receivableNS" value='<s:text name="wpsal03.receivableNS"/>'/>
	<input type="hidden" id="receivableSR" value='<s:text name="wpsal03.receivableSR"/>'/>
	<form id="collectForm" method="post" class="inline">
		<div id="collectPageDiv" class="hide ui-dialog-content ui-widget-content" style="display: block; width: auto; min-height: 200px;">
			<input type="hidden" id="NEW_CZK_PAY" name="NEW_CZK_PAY" value="<s:property value='NEW_CZK_PAY'/>"/>
			<input type="hidden" id="isIngnoreConfirm" name="isIngnoreConfirm" value="<s:property value='isIngnoreConfirm'/>"/>
			<input type="hidden" id="dgOpenedState" name="dgOpenedState" value="Y"/>
			<input type="hidden" id="collectPageType" name="collectPageType" value="<s:property value='collectPageType'/>"/>
			<input type="hidden" id="dgCounterCode" name="dgCounterCode" value="<s:property value='counterCode'/>"/>
			<input type="hidden" id="dgPointRatio" name="dgPointRatio" value="<s:property value='pointRatio'/>"/>
			<input type="hidden" id="orgAliPay" name="orgAliPay" value="<s:property value='aliPay'/>"/>
			<input type="hidden" id="orgWechatPay" name="orgWechatPay" value="<s:property value='wechatPay'/>"/>
			<input type="hidden" id="dgCardCode" name="dgCardCode" value="<s:property value='cardCode'/>"/>
			<div class="wpleft_header">
				<div class="header_box"><s:text name="wpsal03.baName" /><span class="top_detail2"><s:property value="baName"/></span> </div>
				<div class="header_box"><s:text name="wpsal03.memberName" /><span class="top_detail2"><s:property value="memberName"/></span> </div>
				<div class="numberbox"><span id="spanReceivableLable"><s:text name="wpsal03.receivableNS" /></span><span id="spanReceivable"><s:property value="totalAmount"/></span><input id="receivable" name="receivable" type="hidden" value="<s:property value='totalAmount'/>" /></div>
				<div class="numberbox"><span id="spanGiveChangeLable"><s:text name="wpsal03.giveChange" /></span><span id="spanGiveChange">0.00</span><input id="giveChange" name="giveChange" type="hidden" maxlength="10" value="0.00"/></div>
			</div>
			<div class="wp_tablebox">
				<table width="100%" cellspacing="0" cellpadding="0" border="0" class="wp_detail_s">
					<tbody id="payTbody">
					<s:if test='isCA!="Y"'>
						<tr id="trCash">
							<th><s:text name="wpsal03.cash" />：</th>
							<td><input id="cash" name="cash" type="text" title='<s:text name="wpsal03.cash"/>' class="wp_input1" maxlength="10" <s:if test='cash >= 0'>value="<s:property value='cash'/>"</s:if><s:else>value=""</s:else> onchange="BINOLWPSAL03.changeCashValue(this)" onkeyup="BINOLWPSAL03.keyUpChangeCashValue(this)"/></td>
						</tr>
					</s:if>
					<s:iterator value="paymentList">
						<s:if test='storePayCode=="CA" && isCA=="Y"'>
							<tr id="trCash" <s:if test='check=="Y"'>class="show"</s:if><s:else>class="hide"</s:else>>
								<th><s:text name="wpsal03.cash" />：</th>
								<td><input id="cash" name="cash" type="text" title='<s:text name="wpsal03.cash"/>' class="wp_input1" maxlength="10" <s:if test='cash >= 0 && check=="Y"'>value="<s:property value='cash'/>"</s:if><s:else>value=""</s:else> onchange="BINOLWPSAL03.changeCashValue(this)" onkeyup="BINOLWPSAL03.keyUpChangeCashValue(this)"/></td>
							</tr>
						</s:if><s:else>
						<tr id='<s:property value="storePayCode"/>' <s:if test='check=="Y"'>class="show"</s:if><s:else>class="hide"</s:else> >
							<th><s:property value="storePayValue"/>：</th>
							<td>
								<input name='<s:property value="storePayCode"/>' title='<s:property value="storePayValue"/>' type="text" class="wp_input1" maxlength="10"
									   <s:if test='check=="Y" && storePayAmount > 0'>value="<s:property value='storePayAmount'/>"</s:if>
									   <s:elseif test='check=="Y" && defaultPay=="Y" && payValue > 0'>id="defaultPay" value="<s:property value='payValue'/>"</s:elseif>
									   <s:else>value=""</s:else> onchange="BINOLWPSAL03.changeCardValue(this)" onkeyup="BINOLWPSAL03.keyUpChangeCardValue(this)"/>
								<s:if test='storePayCode=="PT"'><span class="hide" id="aliPaySrInfo"><s:text name="wpsal03.aliPaySrInfo" /></span></s:if>
								<s:elseif test='storePayCode=="WEPAY"'><span class="hide" id="wechatPaySrInfo"><s:text name="wpsal03.wechatPaySrInfo" /></span></s:elseif>
							</td>
						</tr>
					</s:else>
					</s:iterator>
					<s:iterator value="paymentList">
						<s:if test='storePayCode=="CZK"'>
							<tr id="serverPay" <s:if test='check=="Y" && NEW_CZK_PAY=="Y"'>class="show"</s:if><s:else>class="hide"</s:else> >
								<th><s:text name="wpsal03.serverPay" />：</th>
								<td>
									<button id="rListShow" onclick="BINOLWPSAL13.serverListShow();return false;">
										<span id="choice1"><s:text name="wpsal03.choice"/></span>
										<span id="choice2" class="hide"><s:text name="wpsal03.choice2"/></span>
									</button>
								</td>
							</tr>
						</s:if>
					</s:iterator>
						<%--	            <tr id="trCreditCard" <s:if test='null == creditCardPayment || "".equals(creditCardPayment)'>class="hide"</s:if>>
                                            <th><s:text name="wpsal03.creditCard" />：</th>
                                            <td><input id="creditCard" name="creditCard" type="text" class="wp_input1" maxlength="10" <s:if test='creditCard >= 0'>value="<s:property value='creditCard'/>"</s:if><s:else>value=""</s:else> onchange="BINOLWPSAL03.changeCardValue(this)" onkeyup="BINOLWPSAL03.keyUpChangeCardValue(this)"/></td>
                                        </tr>
                                        <tr id="trBankCard" <s:if test='null == bankCardPayment || "".equals(bankCardPayment)'>class="hide"</s:if>>
                                            <th><s:text name="wpsal03.bankCard" />：</th>
                                            <td><input id="bankCard" name="bankCard" type="text" class="wp_input1" maxlength="10" <s:if test='bankCard >= 0'>value="<s:property value='bankCard'/>"</s:if><s:else>value=""</s:else> onchange="BINOLWPSAL03.changeCardValue(this)" onkeyup="BINOLWPSAL03.keyUpChangeCardValue(this)"/></td>
                                        </tr>
                                        <tr id="trCashCard" <s:if test='null == cashCardPayment || "".equals(cashCardPayment)'>class="hide"</s:if>>
                                            <th><s:text name="wpsal03.cashCard" />：</th>
                                            <td>
                                                <input id="cashCard" name="cashCard" type="text" class="wp_input1" maxlength="10" <s:if test='cashCard >= 0'>value="<s:property value='cashCard'/>"</s:if><s:else>value=""</s:else> onchange="BINOLWPSAL03.changeCardValue(this)" onkeyup="BINOLWPSAL03.keyUpChangeCardValue(this)"/>
                                                <span class="hide" id="cashCardSrInfo"><s:text name="wpsal03.cashCardSrInfo" /></span>
                                            </td>
                                        </tr>
                                        <tr id="trAliPay" <s:if test='null == aliPayment || "".equals(aliPayment)'>class="hide"</s:if>>
                                            <th><s:text name="wpsal03.aliPay" />：</th>
                                            <td>
                                                <input id="aliPay" name="aliPay" type="text" class="wp_input1" maxlength="10" <s:if test='aliPay >= 0'>value="<s:property value='aliPay'/>"</s:if><s:else>value=""</s:else> onchange="BINOLWPSAL03.changeCardValue(this)" onkeyup="BINOLWPSAL03.keyUpChangeCardValue(this)"/>
                                                <span id="aliPaySrInfo"><s:text name="wpsal03.aliPaySrInfo" /></span>
                                            </td>
                                        </tr>
                                        <tr id="trWechatPay" <s:if test='null == wechatPayment || "".equals(wechatPayment)'>class="hide"</s:if>>
                                            <th><s:text name="wpsal03.wechatPay" />：</th>
                                            <td>
                                                <input id="wechatPay" name="wechatPay" type="text" class="wp_input1" maxlength="10" <s:if test='wechatPay >= 0'>value="<s:property value='wechatPay'/>"</s:if><s:else>value=""</s:else> onchange="BINOLWPSAL03.changeCardValue(this)" onkeyup="BINOLWPSAL03.keyUpChangeCardValue(this)"/>
                                                <span id="wechatPaySrInfo"><s:text name="wpsal03.wechatPaySrInfo" /></span>
                                            </td>
                                        </tr>
                                        <tr id="serverPay" <s:if test='null == cashCardPayment || "".equals(cashCardPayment)'>class="hide"</s:if>>
                                            <th><s:text name="wpsal03.serverPay" />：</th>
                                            <td>
                                                <button id="rListShow" onclick="BINOLWPSAL13.serverListShow();return false;">
                                                <span id="choice1"><s:text name="wpsal03.choice"/></span>
                                                <span id="choice2" class="hide"><s:text name="wpsal03.choice2"/></span>
                                                </button>
                                            </td>
                                        </tr> --%>
					<tr id="serverList" class="hide">
						<th><s:text name="wpsal03.serverType" />：</th>
						<td id="serverListShow">
							<table width="100%" cellspacing="0" cellpadding="0" border="0" class="wp_detail_s" id="serviceTb">
								<s:if test="consumptionCodeMap!=null">
									<s:iterator value="serverList">
										<s:iterator value="consumptionCodeMap.ServiceDetail">
											<s:if test="CodeKey==ServiceType">
												<tr id="<s:property value='CodeKey'/>">
													<th><s:property value="Value"/></th>
													<td>
														<input name="serviceQuantity" onkeyup="BINOLWPSAL03.R_ServiceQuantity(this);return false" value="" class="text"/>
														<s:text name="wpsal03.surplus"></s:text><span id="R_serviceQuantity"><s:property value="ServiceQuantity"/></span><s:text name="wpsal03.quantityService"></s:text>
													</td>
												</tr>
											</s:if>
										</s:iterator>
									</s:iterator>
								</s:if><s:elseif test="savingsList!=null">
								<s:iterator value="savingsList">
									<s:iterator value="serverList">
										<s:if test="CodeKey==ServiceType">
											<tr id="<s:property value='CodeKey'/>">
												<th><s:property value="Value"/></th>
												<td>
													<input name="serviceQuantity" onkeyup="BINOLWPSAL03.R_ServiceQuantity(this);return false" value="<s:property value='ServiceQuantity'/>" class="text"/>
													<s:text name="wpsal03.retreat"></s:text><span id="R_serviceQuantity"><s:property value="ServiceQuantity"/></span><s:text name="wpsal03.quantityService"></s:text>
												</td>
											</tr>
										</s:if>
									</s:iterator>
								</s:iterator>
							</s:elseif><s:else>
								<s:iterator value="serverList">
									<tr id="<s:property value='CodeKey'/>">
										<th><s:property value="Value"/></th>
										<td>
											<input name="serviceQuantity" value="" onkeyup="BINOLWPSAL03.R_ServiceQuantity(this);return false" class="text"/>
											<span class="hide"><s:text name="wpsal03.retreat"></s:text><span id="R_serviceQuantity"></span><s:text name="wpsal03.quantityService"></s:text></span>
										</td>
									</tr>
								</s:iterator>
							</s:else>
							</table>
						</td>
					</tr>
						<%-- <tr id="trPoint" class="hide">
                            <th><s:text name="wpsal03.point" />：</th>
                            <td><input id="pointValue" name="pointValue" type="text" class="wp_input1" maxlength="10" <s:if test='pointValue >= 0'>value="<s:property value='pointValue'/>"</s:if><s:else>value=""</s:else> onchange="BINOLWPSAL03.changePointValue(this)" onkeyup="BINOLWPSAL03.keyUpChangePointValue(this)"/>
                                <div class="numberbox"><s:text name="wpsal03.exchangeCash" /><span id="spanExchangeCash"></span><input id="exchangeCash" name="exchangeCash" type="hidden" class="text" maxlength="10"/></div>
                            </td>
                        </tr> --%>
					</tbody>
				</table>
			</div>
			<div class="wp_tablebox">
				<table width="100%" cellspacing="0" cellpadding="0" border="0" class="wp_detail_s">
					<tbody>
					<tr>
						<th style="width: 15%">
							<s:text name="wpsal03.comments" />
						</th>
						<td style="width: 85%">
							<textarea id="comments" name="comments" style="width: 95%; height:30px;" maxlength="95"></textarea>
						</td>
					</tr>
					</tbody>
				</table>
			</div>
			<div class="bottom_butbox clearfix">
				<button id="btnConfirm" class="close" type="button" onclick="BINOLWPSAL03.confirm();return false;">
					<span class="ui-icon icon-confirm"></span>
					<span class="button-text"><s:text name="wpsal03.confirm"/></span>
				</button>
				<button id="btnCancel" class="close" type="button" onclick="BINOLWPSAL03.cancel();return false;">
					<span class="ui-icon icon-close"></span>
					<span class="button-text"><s:text name="wpsal03.cancel"/></span>
				</button>
				<cherry:show domId="BINOLWPSAL02SPT">
					<button id="btnSetPayment" class="close" type="button" onclick="BINOLWPSAL03.setPayment();return false;">
						<span class="ui-icon icon-ttl-control"></span>
						<span class="button-text"><s:text name="wpsal03.setPayment"/></span>
					</button>
				</cherry:show>
			</div>
		</div>
	</form>
	<div id="setPaymentPageDiv" class="hide ui-dialog-content ui-widget-content" style="display: none; width: auto; min-height: 200px;">
		<div class="wp_tablebox">
			<div class="center">
				<s:iterator value="paymentList">
					<p>
						<input type="checkbox" name='<s:property value="storePayCode"/>' class="wp_checkbox" value='<s:property value="storePayCode"/>'>
						<span onclick="BINOLWPSAL03.checkBoxTextClick(this);"><s:property value="storePayValue"/></span>
					</p>
				</s:iterator>
					<%-- <p>
                        <input type="checkbox" id="ckCreditCard" class="wp_checkbox" value="CR">
                        <span id="ckCreditCardText" onclick="BINOLWPSAL03.checkBoxTextClick(this);"><s:text name="wpsal03.creditCard"/></span>
                    </p>
                    <p>
                        <input type="checkbox" id="ckBankCard" class="wp_checkbox" value="BC">
                        <span id="ckBankCardText" onclick="BINOLWPSAL03.checkBoxTextClick(this);"><s:text name="wpsal03.bankCard"/></span>
                    </p>
                    <p>
                        <input type="checkbox" id="ckCashCard" class="wp_checkbox" value="CZK">
                        <span id="ckCashCardText" onclick="BINOLWPSAL03.checkBoxTextClick(this);"><s:text name="wpsal03.cashCard"/></span>
                    </p>
                    <p>
                        <input type="checkbox" id="ckAliPay" class="wp_checkbox" value="AL">
                        <span id="ckAliPayText" onclick="BINOLWPSAL03.checkBoxTextClick(this);"><s:text name="wpsal03.aliPay"/></span>
                    </p>
                    <p>
                        <input type="checkbox" id="ckWechatPay" class="wp_checkbox" value="WT">
                        <span id="ckWechatPayText" onclick="BINOLWPSAL03.checkBoxTextClick(this);"><s:text name="wpsal03.wechatPay"/></span>
                    </p>
                    <p style="display: none;">
                        <input type="checkbox" id="ckPoints" class="wp_checkbox" value="EX">
                        <span id="ckPointsText" onclick="BINOLWPSAL03.checkBoxTextClick(this);"><s:text name="wpsal03.point"/></span>
                    </p> --%>
			</div>
		</div>
		<div class="bottom_butbox clearfix">
			<button id="btnSetPaymentConfirm" class="close" type="button" onclick="BINOLWPSAL03.setPaymentConfirm();return false;">
				<span class="ui-icon icon-confirm"></span>
				<span class="button-text"><s:text name="wpsal03.confirm"/></span>
			</button>
			<button id="btnSetPaymentCancel" class="close" type="button" onclick="BINOLWPSAL03.setPaymentCancel();return false;">
				<span class="ui-icon icon-close"></span>
				<span class="button-text"><s:text name="wpsal03.cancel"/></span>
			</button>
		</div>
	</div>
	<div id="webPaymentPageDiv" class="hide ui-dialog-content ui-widget-content" style="display: none; width: auto; line-height: 120px;">
		<div class="wp_tablebox">
			<div class="center">
				<input type="password" style="position: absolute; top: -999px"/>
				<p id="fg">
					<s:text name="wpsal03.inputWebPayCode" />
					<input id="webPayCode" name="webPayCode" type="text" class="wp_input1" style="width:260px;" maxlength="30" />
				</p>
				<table id="FWK" class="hide" width="100%" cellspacing="0" cellpadding="0" border="0" class="wp_table">
					<tr>
						<td>
							<s:text name="wpsal03.verificationType" />
						</td>
						<td><span>
					<s:select list='#application.CodeTable.getCodes("1349")' listKey="CodeKey" listValue="Value" id="verificationType" name="verificationType" onchange="BINOLWPSAL03.verificationType();return false;"></s:select>
					</span>
						</td>
					</tr>
					<tr>
						<td>
								<%-- <s:text name="wpsal03.printCardCode" /> --%>
							<span><s:text name="wpsal03.printCardCode" /></span>
						</td>
						<td><span>
					<input id="cardCode" name="cardCode" type="text" class="wp_input1" style="width:200px;" maxlength="30" />
					<button id="getVerificationCodeButton" onclick="BINOLWPSAL03.getVerificationCode();return false;"><s:text name="wpsal03.getVerificationCode"/></button>
					</span>
						</td>
					</tr>
					<tr>
						<td>
							<span class="hide" id="printVerificationCode"><s:text name="wpsal03.printVerificationCode" /></span>
							<span id="printPass"><s:text name="wpsal03.printPass" /></span>
							<span class="hide" id="pringPaymentCode"><s:text name="wpsal03.pringPaymentCode" /></span>
						</td>
						<td>
							<input id="verificationCode2" name="verificationCode" type="password" class="wp_input1" style="width:200px;" maxlength="30" />
							<input id="verificationCode" name="verificationCode" type="text" class="wp_input1" style="width:200px;" maxlength="30" />
						</td>
					</tr>
				</table>
			</div>
		</div>
		<div class="bottom_butbox clearfix">
			<button id="btnWebPaymentConfirm" class="close" type="button" onclick="BINOLWPSAL03.webPaymentConfirm();return false;">
				<span class="ui-icon icon-confirm"></span>
				<span class="button-text"><s:text name="wpsal03.confirm"/></span>
			</button>
			<button id="btnWebPaymentCancel" class="close" type="button" onclick="BINOLWPSAL03.webPaymentCancel();return false;">
				<span class="ui-icon icon-close"></span>
				<span class="button-text"><s:text name="wpsal03.cancel"/></span>
			</button>
		</div>
		<div class="hide" id="payResultDialogInit"></div>
		<div class="hide" id="getPayResultDialogTitle"><s:text name="wpsal03.payResult"/></div>

	</div>
	<%-- <div id="webPaymentPageDivByCZK" class="hide ui-dialog-content ui-widget-content" style="display: none; width: auto; line-height: 100px;">
        <div class="wp_tablebox">
            <div class="center">
                <p>
                    <s:text name="wpsal03.printCardCode" />
                    <input id="cardCode" name="cardCode" type="text" class="wp_input1" style="width:260px;" maxlength="30" /><br>
                    <s:text name="wpsal03.verificationCode" />
                    <input id="verificationCode" name="verificationCode" type="text" class="wp_input1" style="width:260px;" maxlength="30" />
                    <button onclick=""><s:text name="wpsal03.getVerificationCode"/></button>
                </p>
            </div>
        </div>
        <div class="bottom_butbox clearfix">
            <button id="btnWebPaymentConfirm" class="close" type="button" onclick="BINOLWPSAL03.webPaymentConfirm();return false;">
                <span class="ui-icon icon-confirm"></span>
                <span class="button-text"><s:text name="wpsal03.confirm"/></span>
            </button>
            <button id="btnWebPaymentCancel" class="close" type="button" onclick="BINOLWPSAL03.webPaymentCancel();return false;">
                <span class="ui-icon icon-close"></span>
                <span class="button-text"><s:text name="wpsal03.cancel"/></span>
            </button>
        </div>
        <div class="hide" id="payResultDialogInit"></div>
        <div class="hide" id="getPayResultDialogTitle"><s:text name="wpsal03.payResult"/></div>
    </div> --%>
</s:i18n>

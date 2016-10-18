<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/wp/sal/BINOLWPSAL02.js?_v=20161014"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/wp/sal/BINOLWPSAL03.js?_v=20160315"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/wp/sal/BINOLWPSAL13.js?_v=20160315"></script>
<s:i18n name="i18n.wp.BINOLWPSAL02">
	<div class="hide">
		<s:url id="s_dgPaymentSizeUrl" value="/wp/BINOLWPSAL03_getPaymentSize" />
		<a id="paymentSizeUrl" href="${s_dgPaymentSizeUrl}"></a>
		<s:url id="s_searchUrl" value="/wp/BINOLWPSAL02_search" />
		<a id="searchUrl" href="${s_searchUrl}"></a>
		<s:url id="s_collectUrl" value="/wp/BINOLWPSAL03_init" />
		<a id="collectUrl" href="${s_collectUrl}"></a>
		<s:url id="s_discountUrl" value="/wp/BINOLWPSAL04_init" />
		<a id="discountUrl" href="${s_discountUrl}"></a>
		<s:url id="s_hangBillsUrl" value="/wp/BINOLWPSAL05_init" />
		<a id="hangBillsUrl" href="${s_hangBillsUrl}"></a>
		<s:url id="s_getBillsUrl" value="/wp/BINOLWPSAL06_init" />
		<a id="getBillsUrl" href="${s_getBillsUrl}"></a>
		<s:url id="s_searchBillsUrl" value="/wp/BINOLWPSAL07_init" />
		<a id="searchBillsUrl" href="${s_searchBillsUrl}"></a>
		<s:url id="s_showMemberUrl" value="/wp/BINOLWPMBM01_init" />
		<a id="showMemberUrl" href="${s_showMemberUrl}"></a>
		<s:url id="s_promotionUrl" value="/wp/BINOLWPSAL08_init" />
		<a id="promotionUrl" href="${s_promotionUrl}"></a>
		<s:url id="s_getProProductUrl" value="/wp/BINOLWPSAL08_getProduct" />
		<a id="getProProductUrl" href="${s_getProProductUrl}"></a>
		<s:url id="s_returnsGoodsUrl" value="/wp/BINOLWPSAL02_returnsGoods" />
		<a id="returnsGoodsUrl" href="${s_returnsGoodsUrl}"></a>
		<s:url id="s_saleMainUrl" value="/wp/BINOLWPSAL02_init" />
		<a id="saleMainUrl" href="${s_saleMainUrl}"></a>
		<s:url id="s_checkCouponUrl" value="/wp/BINOLWPSAL10_init" />
		<a id="checkCouponUrl" href="${s_checkCouponUrl}"></a>
		<s:url id="s_getCouponProductUrl" value="/wp/BINOLWPSAL10_checkCoupon" />
		<a id="getCouponProductUrl" href="${s_getCouponProductUrl}"></a>
		<s:url id="s_addMemberUrl" value="/wp/BINOLWPSAL11_init" />
		<a id="addMemberUrl" href="${s_addMemberUrl}"></a>
		<!-- 产品活动 -->
		<s:url id="s_initLYHDUrl_0" value="/wp/BINOLWPSAL08_initLYHD_0" />
		<a id="initLYHDUrl_0" href="${s_initLYHDUrl_0}"></a>
		<s:url id="s_initLYHDUrl_1" value="/wp/BINOLWPSAL08_initLYHD_1" />
		<a id="initLYHDUrl_1" href="${s_initLYHDUrl_1}"></a>
		<s:url id="s_initLYHDUrl_4" value="/wp/BINOLWPSAL08_initLYHD_4" />
		<a id="initLYHDUrl_4" href="${s_initLYHDUrl_4}"></a>
		<s:url id="s_initLYHDUrl_3" value="/wp/BINOLWPSAL08_initLYHD_3" />
		<a id="initLYHDUrl_3" href="${s_initLYHDUrl_3}"></a>
		<!-- 智能促销 -->
		<s:url id="s_initMatchRule" value="/wp/BINOLWPSAL08_initMatchRule" />
		<a id="initMatchRule" href="${s_initMatchRule}"></a>
		<s:url id="s_getMatchRule" value="/wp/BINOLWPSAL08_getMatchRule" />
		<a id="getMatchRule" href="${s_getMatchRule}"></a>
		<s:url id="s_ruleError" value="/wp/BINOLWPSAL08_RuleError" />
		<a id="ruleErrorUrl" href="${s_ruleError}"></a>
		<!-- 充值 -->
		<s:url id="s_rechargeUrl" value="/wp/BINOLWPSAL13_recharge" />
		<a id="rechargeUrl" href="${s_rechargeUrl}"></a>
		<!-- 获取储值卡信息 -->
		<s:url id="s_consumption" value="/wp/BINOLWPSAL13_getConsumption" />
		<a id="consumption" href="${s_consumption}" ></a>
		<!-- 获取储值卡信息 -->
		<s:url id="s_consumptionCode" value="/wp/BINOLWPSAL13_getConsumptionCode" />
		<a id="consumptionCode" href="${s_consumptionCode}" ></a>
		<!-- 输入储值卡界面 -->
		<s:url id="s_initUrl" value="/wp/BINOLWPSAL13_init" />
		<a id="initUrl" href="${s_initUrl}"></a>
<%-- 		<!-- 获取储值卡详细信息 -->
		<s:url id="s_consumptionCodeDetailed" value="/wp/BINOLWPSAL13_getConsumptionCodeDetailed"></s:url>
		<a id="consumptionCodeDetailed" href="${s_consumptionCodeDetailed}" ></a>
		<!-- 输入储值卡卡号 -->
		<s:url id="s_CardCode" value="/wp/BINOLWPSAL13_getCardCode" />
		<a id="cardCodeUrl" href="${s_CardCode}" ></a> --%>
		<!-- 检查储值卡是否存在 -->
		<s:url id="s_checkCardCode" value="/wp/BINOLWPSAL13_checkCardCode" />
		<a id="checkCardCode" href="${s_checkCardCode}" ></a>
		<!-- 储值卡交易明细初始化 -->
		<s:url id="s_transactionDetailInit" value="/wp/BINOLWPSAL13_transactionDetailInit" />
		<a id="transactionDetailInit" href="${s_transactionDetailInit}" ></a>
		<!-- 储值卡交易明细 -->
		<s:url id="s_transactionDetail" value="/wp/BINOLWPSAL13_transactionDetail" />
		<a id="transactionDetail" href="${s_transactionDetail}" ></a>
		<!-- 撤销初始化界面 -->
		<s:url id="s_revokeInit" value="/wp/BINOLWPSAL13_revokeInit" />
		<a id="revokeInit" href="${s_revokeInit}" ></a>
		<!-- 充值撤销 -->
		<s:url id="s_revoke" value="/wp/BINOLWPSAL13_revoke" />
		<a id="revoke" href="${s_revoke}" ></a>
		<!-- 获取验证码地址 -->
		<s:url id="s_dgGetVerificationCode" value="/wp/BINOLWPSAL03_getVerificationCode" />
		<a id="dgGetVerificationCode" href="${s_dgGetVerificationCode}"></a>
		<!-- 储值卡与会员卡关联 -->
		<s:url id="s_dgRelation" value="/wp/BINOLWPSAL13_relation" />
		<a id="dgRelation" href="${s_dgRelation}"></a>
		<s:url id="s_salCollectUrl" value="/wp/BINOLWPSAL03_collect" />
		<a id="salCollectUrl" href="${s_salCollectUrl}"></a>
		<s:url id="s_saleUrl" value="/wp/BINOLWPSAL02_init" />
		<a id="saleUrl" href="${s_saleUrl}"></a>
		<s:url id="productSearchUrl" value="/common/BINOLCM02_popPrtDialog" />
		<a id="productUrl" href="${productSearchUrl}"></a>
	</div>
	<div id="div_main">
		<s:if test="hasActionErrors()">
			<jsp:include page="/WEB-INF/jsp/common/actionResultBody.jsp" flush="true"/>
		</s:if>
		<s:else>
			<input type="hidden" id="new_Czk_Pay" name="newCzkPay" value="<s:property value='newCzkPay'/>"/>
			<%-- <input type="hidden" id="czkType" name="czkType" value="<s:property value='czkType'/>"/> --%>
			<input type="hidden" id="promotionInfoTitle" value='<s:text name="wpsal02.promotionInfoTitle"/>'/>
			<input type="hidden" id="promotionDetailTitle" value='<s:text name="wpsal02.promotionDetailTitle"/>'/>
			<input type="hidden" id="promotionCodeTitle" value='<s:text name="wpsal02.promotionCodeTitle"/>'/>
			<input type="hidden" id="promotionNameTitle" value='<s:text name="wpsal02.promotionNameTitle"/>'/>
			<div id="messagePageDiv" class="crm_top clearfix">
				<div>
			        <span class="icon_order"></span>
			        <s:text name="wpsal02.counterName" />
			        <span id="txtCounterName" class="top_detail"><s:property value="counterName"/></span>
			    </div>
			    <div>
			    	<input type="hidden" id="saleTypeNS" value='<s:text name="wpsal02.saleTypeNS"/>'/>
			    	<input type="hidden" id="saleTypeSR" value='<s:text name="wpsal02.saleTypeSR"/>'/>
			        <span class="icon_state"></span>
					<s:text name="wpsal02.saleState" />
			        <span id="txtSaleType" class="top_detail">
			        	<s:text name="wpsal02.saleTypeNS" />
			       	</span>
			       	<span id="txtAddHistoryBill" class="top_detail" style="display:none">
			        	<s:text name="wpsal02.addHistoryBillText" />
			       	</span>
			    </div>
			    <div>
			        <span class="icon_order"></span>
			        <s:text name="wpsal02.billCode" />
			        <span id="spanBillCode" class="top_detail"><s:property value="billCode"/></span>
			    </div>
				<div>
			        <span class="icon_member"></span>
			       	<s:text name="wpsal02.memberName" />
			        <span id="txtMemberName" class="top_detail"><s:property value="memberInfo.memberName"/></span>
			    </div>
			</div>
			<div id="buttonPageDiv" class="wp_navbuttonbox">
				<cherry:show domId="BINOLWPSAL02COL">
					<button id="btnCollect" class="btn_top" type="button" disabled="disabled" onclick="BINOLWPSAL02.initMatchRule_CloudPos();return false;">
						<s:text name="wpsal02.collect"/>
					</button>
				</cherry:show>
				<cherry:show domId="BINOLWPSAL02DIS">
					<button id="btnDiscount" class="btn_top" type="button" disabled="disabled" onclick="BINOLWPSAL02.discount();return false;">
						<s:text name="wpsal02.discount"/>
					</button>
				</cherry:show>
				<cherry:show domId="BINOLWPSAL02HAG">
					<button id="btnHangBills" class="btn_top" type="button" onclick="BINOLWPSAL02.hangBills();return false;">
						<s:text name="wpsal02.hangBills"/>
					</button>
				</cherry:show>
				<cherry:show domId="BINOLWPSAL02SCH">
					<button id="btnSearchBills" class="btn_top" type="button" onclick="BINOLWPSAL02.searchBills();return false;">
						<s:text name="wpsal02.searchBills"/>
					</button>
				</cherry:show>
				<cherry:show domId="BINOLWPSAL02MEM">
					<button id="btnShowMember" class="btn_top" type="button" onclick="BINOLWPSAL02.showMember();return false;">
						<s:text name="wpsal02.showMember"/>
					</button>
				</cherry:show>
				<cherry:show domId="BINOLWPSAL02EMP">
					<button id="btnEmptyShoppingCart" class="btn_top" type="button" onclick="BINOLWPSAL02.emptyShoppingCart();return false;">
						<s:text name="wpsal02.emptyShoppingCart"/>
					</button>
				</cherry:show>
				<cherry:show domId="BINOLWPSAL02RET">
					<button id="btnReturnsGoods" class="btn_top" type="button" onclick="BINOLWPSAL02.returnsGoods();return false;">
						<s:text name="wpsal02.returnsGoods"/>
					</button>
				</cherry:show>
				<cherry:show domId="BINOLWPSAL02HIS">
					<button id="btnAddHistoryBill" class="btn_top" type="button" onclick="BINOLWPSAL02.addHistoryBill();return false;">
						<s:text name="wpsal02.addHistoryBill"/>
					</button>
				</cherry:show>
				<cherry:show domId="BINOLWPSAL02SCR">
					<button id="recharge" class="btn_top" type="button" onclick="BINOLWPSAL13.confirm();return false;">
						<s:text name="wpsal02.recharge"/>
					</button>
				</cherry:show>
			</div>
			<div id="mainPageDiv" class="wp_main">
				<input type="hidden" id="businessBeginDate" name="businessBeginDate" value="<s:property value='businessBeginDate'/>"/>
			    <input type="hidden" id="nowDate" name="nowDate" value="<s:property value='nowDate'/>"/>
			    <input type="hidden" id="showSaleRows" name="showSaleRows" value="<s:property value='showSaleRows'/>"/>
			    <input type="hidden" id="conditionAmount" name="conditionAmount" value="<s:property value='conditionAmount'/>"/>
			    <input type="hidden" id="useMemberPrice" name="useMemberPrice" value="<s:property value='useMemberPrice'/>"/>
			    <input type="hidden" id="firstBillPrice" name="firstBillPrice" value="<s:property value='firstBillPrice'/>"/>
			    <input type="hidden" id="showCollectAfterJoin" name="showCollectAfterJoin" value="<s:property value='showCollectAfterJoin'/>"/>
			    <input type="hidden" id="autoPrintBill" name="autoPrintBill" value="<s:property value='autoPrintBill'/>"/>
			    <input type="hidden" id="minDiscount" name="minDiscount" value="<s:property value='minDiscount'/>"/>
			    <input type="hidden" id="stockType" name="stockType" value="<s:property value='stockType'/>"/>
			    <input type="hidden" id="discountType" name="discountType" value="<s:property value='discountType'/>"/>
			    <input type="hidden" id="highPriceSal" name="highPriceSal" value="<s:property value='highPriceSal'/>"/>
			    <input type="hidden" id="isFirstBill" name="isFirstBill" value="N"/>
			    <input type="hidden" id="merge" name="merge" value="<s:property value='merge'/>"/>
			    <input type="hidden" id="isPlatinumPrice" name="isPlatinumPrice" value="<s:property value='isPlatinumPrice'/>"/>
			    <input type="hidden" id="smartPromotion" name="smartPromotion" value="<s:property value='smartPromotion'/>"/>
			    <input type="hidden" id="rechargeAndOpendCardButton" name="rechargeAndOpendCardButton" value="<s:property value='rechargeAndOpendCardButton'/>"/>
			    <input type="hidden" id="isDiscountFlag" name="isDiscountFlag" value="<s:property value='isDiscountFlag'/>"/>
			    <input type="hidden" id="baChooseModel" name="baChooseModel" value="<s:property value='baChooseModel'/>"/>
			    <input type="hidden" id="isCA" name="isCA" value="<s:property value='isCA'/>"/>
			    <input type="hidden" id="isMemberSaleFlag" name="isMemberSaleFlag" value="<s:property value='isMemberSaleFlag'/>"/>
			    <input type="hidden" id="printBrandType" name="printBrandType" value="<s:property value='printBrandType'/>"/>
			    <input type="hidden" id="stockSaleType" name="stockSaleType" value="<s:property value='stockSaleType'/>"/>
			    <input type="hidden" id="birthFlag" name="birthFlag" value="<s:property value='birthFlag'/>"/>
			    <input type="hidden" id="isBuyFlag" name="isBuyFlag" value="<s:property value='isBuyFlag'/>"/>
			    <input type="hidden" id="memCodeRule" name="memCodeRule" value="<s:property value='memCodeRule'/>"/>
			    <input type="hidden" id="mobilePhoneQ" name="mobilePhoneQ" value="<s:property value='mobilePhoneQ'/>"/>
			    <div id="leftPageDiv" class="wp_left">
			    <form id="mainForm" method="post" class="inline" >
			    	<input type="hidden" id="billClassify" name="billClassify" value=""/>
			    	<input type="hidden" id="saleType" name="saleType" value="NS"/>
			    	<input type="hidden" id="businessState" name="businessState" value="N"/>
					<input type="hidden" id="counterCode" name="counterCode" value="<s:property value='counterCode'/>"/>
					<input type="hidden" id="memberInfoId" name="memberInfoId" value="<s:property value='memberInfoId'/>"/>
					<input type="hidden" id="memberCode" name="memberCode" value="<s:property value='memberInfo.memberCode'/>"/>
					<input type="hidden" id="memberName" name="memberName" value="<s:property value='memberInfo.memberName'/>"/>
					<input type="hidden" id="memberLevel" name="memberLevel" value="<s:property value='memberInfo.memberLevel'/>"/>
					<input type="hidden" id="mobilePhone" name="mobilePhone" value="<s:property value='memberInfo.mobilePhone'/>"/>
					<input type="hidden" id="changablePoint" name="changablePoint" value="<s:property value='memberInfo.changablePoint'/>"/>
					<input type="hidden" id="counterPhone" name="counterPhone" value='<s:property value="counterPhone"/>'/>
					<input type="hidden" id="counterAddress" name="counterAddress" value='<s:property value="counterAddress"/>'/>
					<input type="hidden" id="originalAmount" name="originalAmount" value=""/>
					<input type="hidden" id="totalDiscountRate" name="totalDiscountRate" value=""/>
					<input type="hidden" id="roundingAmount" name="roundingAmount" value=""/>
			        <div class="wp_content">
			        	<div class="wp_leftbox">
			        		<s:text name="wpsal02.selectCheck" id="selectCheck"/>
			            	<div id="searchPageDiv" class="wpleft_header">
			               	   <div class="header_box">
			                       	<s:text name="wpsal02.baName" />
			                       	<s:if test='"2".equals(baChooseModel)'>
			                        	<span><s:select id="baCode" name="baCode" list="baList" listKey="baCode" listValue="baName" cssStyle="width:100px;" headerKey="" headerValue="%{selectCheck}" onclick="BINOLWPSAL02.clickBa();return false;" onchange="BINOLWPSAL02.changeBa();return false;" /></span>
			                    	</s:if>
			                    	<s:else>
				                    	<span>
					                    	<input type="hidden" name="baCode" id="baCode"></input>
		                        			<input type="text" id="baName" class="text" style="width:80px"></input>
				                    	</span>
			                    	</s:else>
			                    </div>
			               	    <div class="header_box">
									<s:text name="wpsal02.businessDate" />
			                        <span><s:textfield id="businessDate" name="businessDate" cssClass="date titleTools" disabled="disabled" value="%{nowDate}" onkeyup="BINOLWPSAL02.keyupBusinessDate(this);return false;"/></span>
			                    </div>
			               	    <div class="header_box">
									<s:text name="wpsal02.member" />
			                        <input type="text" id="searchStr" name="searchStr" class="text titleTools" maxlength="20"/>
			                        <button id="btnSearch" class="wp_search" type="button" onclick="BINOLWPSAL02.search();return false;"></button>
			                    </div>
			               	    <div class="numberbox">
			               	    	<label id="saleTypeTotalQuantitySr" class="hide" style="font-weight:bold;">
							        	<s:text name="wpsal02.saleTypeQuantitySR" />
							       	</label>
							       	<label id="saleTypeTotalQuantityNs" style="font-weight:bold;">
										<s:text name="wpsal02.totalQuantity" />
									</label>
			                        <span id="spanTotalQuantity" name="spanTotalQuantity"></span>
			                        <input type="hidden" id="totalQuantity" name="totalQuantity" value=""/>
			                    </div>
			               	    <div class="numberbox">
			               	    	<label id="saleTypeTotalAmountSr" class="hide" style="font-weight:bold;">
							        	<s:text name="wpsal02.saleTypeAmountSR" />
							       	</label>
							       	<label id="saleTypeTotalAmountNs" style="font-weight:bold;">
										<s:text name="wpsal02.totalAmount" />
									</label>
			                        <span id="spanTotalAmount" name="spanTotalAmount"></span>
			                        <input type="hidden" id="totalAmount" name="totalAmount" value=""/>
			                    </div>
			                </div>
			            	<div class="wp_tablebox_b">
			            		<div id="dataPageDiv">
			            			<input type="hidden" id="rowNumber" value="0"/>
			            			<input type="hidden" id="rowCode" value="0"/>
									<input type="hidden" id="billCode" name="billCode" value="<s:property value='billCode'/>"/>
									<input type="hidden" id="saleDetailList" name="saleDetailList" value=""/>
									<div class="hide" id="saleDetailList_promotion"></div>
									<input class="hide" id="saleDetailList_json" value=""></input>
									<input type="hidden" id="promotionList" name="promotionList" value=""/>
				                    <table id="mainTable" width="100%" cellspacing="0" cellpadding="0" border="0" class="wp_table">
				                    <thead>
				                      <tr>
				                      	<th width="3%"><s:text name="wpsal02.rowNumber"/></th>
				                        <th width="13%"><s:text name="wpsal02.unitCode"/></th>
				                        <th width="13%"><s:text name="wpsal02.barCode"/></th>
				                        <th width="16%"><s:text name="wpsal02.productName"/></th>
				                        <th width="6%"><s:text name="wpsal02.price"/></th>
				                        <s:if test="useMemberPrice=='Y'.toString()">
					                        <th width="6%"><s:text name="wpsal02.memberPrice"/></th>
				                        </s:if>
				                        <s:if test="isPlatinumPrice=='Y'.toString()">
					                        <th width="6%"><s:text name="wpsal02.platinumPrice"/></th>
				                        </s:if>
				                        <th width="12%">
				                        	<span id="saleTypeQuantity" class="hide">
									        	<s:text name="wpsal02.saleTypeSR" />
									       	</span>
				                        	<s:text name="wpsal02.quantity"/>
				                        </th>
				                        <th width="7%"><s:text name="wpsal02.discountRate"/></th>
				                        <th width="7%"><s:text name="wpsal02.discountPrice"/></th>
				                        <th width="7%">
				                        	<span id="saleTypeAmount" class="hide">
									        	<s:text name="wpsal02.saleTypeSR" />
									       	</span>
				                        	<s:text name="wpsal02.amount"/>
				                        </th>
				                        <th width="5%"><s:text name="wpsal02.act"/></th>
				                        <th style="display:none">
				                      </tr>
				                    </thead>
				                    <tbody id="databody">
				                    </tbody>
				                    </table>
			                    </div>
			                    <div id="memberPageDiv" class="wp_bottom2 hide" style="height:135px">
			                        <div id="memberContentDiv" class="wp_content_b">
			                            <div id="memberBoxDiv" class="wp_leftbox">
						                    <table id="memberInfoTab" width="100%" cellspacing="0" cellpadding="0" border="0" class="wp_detail">
						                    <tbody>
						                        <tr>
						                            <th><s:text name="wpsal02.memberCode" /></th>
						                            <td><span id="spanMemberCode"></span></td>
						                            <th><s:text name="wpsal02.memName" /></th>
						                            <td><span id="spanMemberName"></span></td>
						                            <th><s:text name="wpsal02.joinDate" /></th>
						                            <td><span id="spanJoinDate"></span></td>
						                            <td rowspan="2" id="newCzkPay" class="hide"><span>
						                            	<a href="javascript:void(0)" onclick="BINOLWPSAL02.getConsumptionCode()"><s:text name="wpsal02.consumptionCode"></s:text></a>
						                            </span></td>
						                        </tr>
						                        <tr>
						                        	<th><s:text name="wpsal02.totalPoint" /></th>
						                            <td><span id="spanTotalPoint"></span></td>
						                            <th><s:text name="wpsal02.changablePoint" /></th>
						                            <td><span id="spanChangablePoint"></span></td>
						                            <th><s:text name="wpsal02.lastSaleDate" /></th>
						                            <td><span id="spanLastSaleDate"></span></td>
						                        </tr>
						                    </tbody>
						                    </table>
					                  	</div>
					               </div>
					            </div>
			                </div>
			            </div>
			        </div>
			    </form>
			    </div>
			    <div class="wp_right">
				    <div id="rightPageDiv" class="wp_rightbox">
					</div>
			    </div>
			</div>
		</s:else>
		<div class="hide" id="disableText"><p class="message"><span>是否选择支付方式</span></p></div>
		<div class="hide" id="disableTitle">设置支付方式</div>
		<div class="hide" id="dialogInit"></div>
		<div class="hide" id="serverListDialogInit"></div>
		<div class="hide" id="revokeInitDialogInit"></div>
		<div class="hide" id="collectDialogTitle"><s:text name="wpsal02.collectTitleNS"/></div>
		<div class="hide" id="discountDialogTitle"><s:text name="wpsal02.discountTitle"/></div>
		<div class="hide" id="hangBillsDialogTitle"><s:text name="wpsal02.hangBillsTitle"/></div>
		<div class="hide" id="getBillsDialogTitle"><s:text name="wpsal02.getBillsTitle"/></div>
		<div class="hide" id="searchBillsDialogTitle"><s:text name="wpsal02.searchBillsTitle"/></div>
		<div class="hide" id="addMemberDialogTitle"><s:text name="wpsal02.addMemberDialogTitle"/></div>
		<div class="hide" id="openCardDialogTitle"><s:text name="wpsal02.openCardDialogTitle"/></div>
		<div class="hide" id="openCardDialogTitle"><s:text name="wpsal02.openCardDialogTitle"/></div>
		<div class="hide" id="checkCoupon_dialog"></div>
		<div class="hide" id="checkCouponDialogTitle"><s:text name="wpsal02.checkCouponTitle"/></div>
		<div class="hide" id="returnBill_dialog"></div>
		<div class="hide" id="returnBillDialogTitle"><s:text name="wpsal02.collectTitleSR"/></div>
		<div class="hide" id="initMatchRule_CloudPos"><s:text name="wpsal02.getMatchRule"/></div>
		<div class="hide" id="getLYHD"><s:text name="wpsal02.lyhd"/></div>
		<div class="hide" id="rechargeDialogTitle"><s:text name="wpsal02.recharge"/></div>
		<div class="hide" id="rechargeDialogTitle2"><s:text name="wpsal02.recharge2"/></div>
		<div class="hide" id="rechargeDialogTitle3"><s:text name="wpsal02.recharge3"/></div>
		<div class="hide" id="getConsumptionCode"><s:text name="wpsal02.consumptionCode"/></div>
		<div class="hide" id="getConsumptionCodeDetailed"><s:text name="wpsal02.consumptionCodeDetailed"/></div>
		<div class="hide" id="dialogConfirm"><s:text name="global.page.ok" /></div>
   		<div class="hide" id="dialogCancel"><s:text name="global.page.cancle" /></div>
   		<div class="hide" id="CZKMessage"><p class="message"><span><s:text name="wpsal02.relationMessage"/></span></p></div>
   		<div class="hide" id="CZKTitle"><s:text name="wpsal02.relationTitle"/></div>
	</div>
	<div class="hide" id="messageDialogTitle"><s:text name="wpsal02.messageDialogTitle"/></div>
	<div id="messageDialogDiv" class="hide ui-dialog-content ui-widget-content" style="display: none; width: auto; min-height: 200px;">
		<p id="messageContent" class="message hide" style="margin:40px auto 30px;"><span id="messageContentSpan"></span></p>
		<p id="successContent" class="success hide" style="margin:40px auto 30px;"><span id="successContentSpan"></span></p>
		<p class="center">
			<button id="btnMessageConfirm" class="close" type="button">
	    		<span class="ui-icon icon-confirm"></span>
	            <span class="button-text"><s:text name="wpsal02.confirm"/></span>
			</button>
		</p>
	</div>
	<input type="hidden" id="promotionInfo" >
	<div id="saleNumberDialogTitle" class="hide"><s:text name="数量"/></div>
	<div id="saleNumberDialogDiv" class="hide ui-dialog-content ui-widget-content" style="display: none; width: auto; min-height: 200px;">
		<p class="center"><span>请输入数量：<input type="text" id="saleNumber" class="text" onkeyup="BINOLWPSAL02.keyupSaleNumber(this);return false;"></input></span></p>
		<p class="center">
			<button id="btnSetNumberConfirm" class="close" type="button" onclick="BINOLWPSAL02.setNumberConfirm();return false;">
	    		<span class="ui-icon icon-confirm"></span>
	            <span class="button-text"><s:text name="wpsal02.confirm"/></span>
			</button>
		</p>
	</div>
	<!-- 打印小票内容 -->
	<div class="hide" id="salePrintForm">
		<table id="printTable1" border="0" width="100%" height="48" cellspacing="0" style="border:solid 0px black;border-collapse:collapse;">
			<tr>
				<td id="counterNamePrint" height="10" colspan="2" style="border:solid 0px black"><font size="2px">柜台名称：</font></td>
			</tr>
			<tr>
				<td id="saleDateTimePrint" width="60%" height="10" style="border:solid 0px black"><font size="2px">日期：</font></td>
			</tr>
			<tr>
				<td id="employeeNamePrint" width="40%" height="10" style="border:solid 0px black"><font size="2px">营业员：</font></td>
			</tr>
			<tr>
				<td id="billCodePrint" height="10" colspan="2" style="border:solid 0px black"><font size="2px">流水号：</font></td>
			</tr>
			<tr>
				<td id="memberCodePrint" height="10" colspan="2" style="border:solid 0px black"><font size="2px">会员号：</font></td>
			</tr>
			<tr>
				<td id="memberIntegralPrint" height="10" colspan="2" style="border:solid 0px black"><font size="2px">会员积分：</font></td>
			</tr>
		</table>
		<table id="detailTitle" border="0" width="100%" height="16" style="border:solid 0px black;border-collapse:collapse;">
			<tr>
				<!-- <td width="40%" height="10" class="left" style="border:solid 0px black"><font size="2px">商品编号</font></td> -->
				<td width="15%" height="10" class="center" style="border:solid 0px black"><font size="2px">数量</font></td>
				<td width="15%" height="10" class="center" style="border:solid 0px black"><font size="2px">单价</font></td>
				<td width="15%" height="10" class="center" style="border:solid 0px black"><font size="2px">折扣</font></td>
				<td width="15%" height="10" class="center" style="border:solid 0px black"><font size="2px">实收</font></td>
			</tr> 
		</table>
		<table id="detailPrint" border="0" width="100%" height="16" style="border:solid 0px black;border-collapse:collapse;">
		</table>
		<table id="printTable2">
			<tr>
				<td height="10" colspan="2" style="border:solid 0px black"><font size="2px">请您在离店前核对所购买的货物</font></td>
			</tr>
			<tr>
				<td height="10" colspan="2" style="border:solid 0px black"><font size="2px">凭本票七天内无条件退货！</font></td>
			</tr>
			<tr>
				<td height="10" colspan="2" style="border:solid 0px black"><font size="2px">十五天内无条件换货!</font></td>
			</tr>
			<tr>
				<td height="10" colspan="2" style="border:solid 0px black"><font size="2px" id="counterPhonePrint">服务电话：85155298</font></td>
			</tr>
		</table>
		<!-- 虚线 -->
		<hr style="BORDER-BOTTOM-STYLE: dotted; BORDER-LEFT-STYLE: dotted; BORDER-RIGHT-STYLE: dotted; BORDER-TOP-STYLE: dotted" color=#111111 size=1>
		<table border="0" width="100%" height="16" style="border:solid 0px black;border-collapse:collapse;">
			<tr>
				<td width="10%" height="10" class="left" style="border:solid 0px black"><font size="2px">合计</font></td>
				<td id="sumQuantityPrint" width="10%" height="10" class="center" style="border:solid 0px black"></td>
				<td id="sumAmountPrint" width="15%" height="10" class="center" style="border:solid 0px black"></td>
			</tr>
			<tr>
				<td width="10%" height="10" class="left" style="border:solid 0px black"><font size="2px"></font></td>
				<td width="10%" height="10" class="center" style="border:solid 0px black"></td>
				<td id="discountAmountPrint" width="15%" height="10" class="center" style="border:solid 0px black"></td>
			</tr>
			<tr>
				<td width="10%" height="10" class="left" style="border:solid 0px black"><font size="2px"></font></td>
				<td width="10%" height="10" class="center" style="border:solid 0px black"></td>
				<td id="originalAmountPrint" width="15%" height="10" class="center" style="border:solid 0px black"></td>
			</tr>
			</table>
		<hr style="BORDER-BOTTOM-STYLE: dotted; BORDER-LEFT-STYLE: dotted; BORDER-RIGHT-STYLE: dotted; BORDER-TOP-STYLE: dotted" color=#111111 size=1>
		<table id="printTable3" border="0" width="100%" height="16" style="border:solid 0px black;border-collapse:collapse;">
			<tr>
				<td width="40%" height="10" class="left" style="border:solid 0px black"><font size="2px">整单去零（元）：</font></td>
				<td id="roundingAmountPrint" width="48" height="10" class="center" style="border:solid 0px black"></td>
			</tr> 
			<!-- <tr>
				<td width="30%" height="10" class="left" style="border:solid 0px black"><font size="2px">现金：</font></td>
				<td width="48" height="10" class="center" style="border:solid 0px black"></td>
			</tr> -->
			<!-- <tr>
				<td width="30%" height="10" class="left" style="border:solid 0px black"><font size="2px">实付：</font></td>
				<td id="payAmountPrint" width="48" height="10" class="center" style="border:solid 0px black"></td>
			</tr> -->
			<tr id="payChangePrintTr">
				<td width="30%" height="10" class="left" style="border:solid 0px black"><font size="2px">找零：</font></td>
				<td id="payChangePrint" width="48" height="10" class="center" style="border:solid 0px black"></td>
			</tr>
			<tr>
				<td height="10" colspan="2" style="border:solid 0px black"><font size="2px">尊敬的顾客：未提供收银小票的收银员，均</font></td>
			</tr>
			<tr>
				<td height="10" colspan="2" style="border:solid 0px black"><font size="2px">视其盗窃行为，一经核实，您将获得本次消费</font></td>
			</tr>
			<tr>
				<td height="10" colspan="2" style="border:solid 0px black"><font size="2px">金额的5倍作为奖金，欢迎您再次光临！</font></td>
			</tr>
			<tr>
				<td height="10" colspan="2" style="border:solid 0px black"><font size="2px">投诉电话 4008-876-867 </font></td>
			</tr>
		</table>
		<!-- 虚线 -->
		<!-- <hr style="BORDER-BOTTOM-STYLE: dotted; BORDER-LEFT-STYLE: dotted; BORDER-RIGHT-STYLE: dotted; BORDER-TOP-STYLE: dotted" color=#111111 size=1>
		<table border="0" width="100%" height="10" style="border:solid 0px black;border-collapse:collapse;">
			<tr>
				<td width="40%" height="10" class="left" style="border:solid 0px black"><font size="2px">地址：</font></td>
				<td id="addressPrint" width="15%" height="10" class="center" style="border:solid 0px black"></td>
			</tr>
			<tr>
				<td width="40%" height="10" class="left" style="border:solid 0px black"><font size="2px">电话：</font></td>
				<td id="phonePrint" width="48" height="10" class="center" style="border:solid 0px black"></td>
			</tr> 
			<tr>
				<td width="30%" height="10" class="left" style="border:solid 0px black"><font size="2px">网址：</font></td>
				<td id="webSitePrint" width="48" height="10" class="center" style="border:solid 0px black"></td>
			</tr>
			<tr>
				<td width="30%" height="10" class="left" style="border:solid 0px black"><font size="2px">客户专线：</font></td>
				<td id="CustomerLinePrint" width="48" height="10" class="center" style="border:solid 0px black"></td>
			</tr>
		</table> -->
	</div>
	<div class="hide" id="salePrintForm_AVON">
		<div>
			<p style="text-align: center;font-size: 20px;font-weight: bold;">AVON<br> <span style="font-size:15px;">AVON</span></p>
			<p id="counterNamePrint_AVON" style="margin:0;"></p>
			<p style="text-align: center;font-size: 25px;font-weight: bold;margin:0;">-----销售-----</p>
		</div>
		<table id="printTable1" border="0" width="100%" height="48" cellspacing="0" style="border:solid 0px black;border-collapse:collapse;">
			<tr>
				<td id="employeeNamePrint_AVON" width="40%" height="10" style="border:solid 0px black"><font size="2px">营业员：</font></td>
			</tr>
			<tr>
				<td id="saleDateTimePrint_AVON" width="60%" height="10" style="border:solid 0px black"><font size="2px">打印日期：</font></td>
			</tr>
			<tr>
				<td id="memberCodePrint_AVON" height="10" colspan="2" style="border:solid 0px black"><font size="2px">会员号：</font></td>
			</tr>
			<tr>
				<td id="billCodePrint_AVON" height="10" colspan="2" style="border:solid 0px black"><font size="2px">流水号：</font></td>
			</tr>
		</table>
		<table id="detailTitle_AVON" border="0" width="100%" height="16" style="border:solid 0px black;border-collapse:collapse;">
			<tr>
				<!-- <td width="40%" height="10" class="left" style="border:solid 0px black"><font size="2px">商品编号</font></td> -->
				<td width="15%" height="10" class="center" style="border:solid 0px black"><font size="2px">数量</font></td>
				<td width="15%" height="10" class="center" style="border:solid 0px black"><font size="2px">单价</font></td>
				<td width="15%" height="10" class="center" style="border:solid 0px black"><font size="2px">金额</font></td>
			</tr> 
		</table>
		<table id="detailPrint_AVON" border="0" width="100%" height="16" style="border:solid 0px black;border-collapse:collapse;">
		</table>
		<!-- 虚线 -->
		<hr style="BORDER-BOTTOM-STYLE: dotted; BORDER-LEFT-STYLE: dotted; BORDER-RIGHT-STYLE: dotted; BORDER-TOP-STYLE: dotted" color=#111111 size=1>
		<table id="printTable3_AVON" border="0" width="100%" height="16" style="border:solid 0px black;border-collapse:collapse;">
			<tr id="payChangePrintTr_AVON">
				<td width="40%" height="10" class="left" style="border:solid 0px black"><font size="2px">总计（元）：</font></td>
				<td id="originalAmountPrint_AVON" width="48" height="10" class="center" style="border:solid 0px black"></td>
			</tr>
			<tr>
				<td width="40%" height="10" class="left" style="border:solid 0px black"><font size="2px">总数量（支）：</font></td>
				<td id="sumQuantityPrint_AVON" width="48" height="10" class="center" style="border:solid 0px black"></td>
			</tr>
		</table>
		<hr style="BORDER-BOTTOM-STYLE: dotted; BORDER-LEFT-STYLE: dotted; BORDER-RIGHT-STYLE: dotted; BORDER-TOP-STYLE: dotted" color=#111111 size=1>
		<table border="0" width="100%" height="16" style="border:solid 0px black;border-collapse:collapse;">
			<tr>
				<td width="40%" height="10" class="left" style="border:solid 0px black"><font size="2px" id="counterAddressPrint_AVON"></font></td>
			</tr> 
			<tr>
				<td width="30%" height="10" class="left" style="border:solid 0px black"><font size="2px" id="counterPhonePrint_AVON"></font></td>
			</tr>
		</table>
		<p style="margin:0;font-size:13">谢谢惠顾，欢迎下次光临！请保留此单，作为退换货依据</p>
	</div>
	
	<!-- 智能促销添加产品 -->
	<div class="hide" id="dialogChkPrtInitDIV"></div>
	<div class="section hide" id="addPrtTable">
	  <form id="productForm">
	  <input class="hide" id="maincode2" value=""/>
	  <input class="hide" id="productBrand" value=""/>
	  <div class="section">
	    <div class="section-header"><strong><span class="ui-icon icon-ttl-section-list"></span><s:text name="wpsal02.preprtArea"/></strong></div>
	    <div class="section-content">
	    <div class="toolbar clearfix">
	        <span class="left">
	            <s:text name="wpsal02.unitCode"/>：<input id="addProductId" class="text"/><font color="red"><span id="productMemo"></span></font>
	        </span>
	    </div>
	    <div id="canceldetail">
	      <div style="width:100%;overflow-x:scroll;">
	        <table id="productMainTable" cellpadding="0" cellspacing="0" border="0" width="100%" class="wp_table">
	            <thead>
	               <tr>
	                    <th class="tableheader" width="15%"><s:text name="wpsal02.unitCode"/></th>
	                    <th class="tableheader" width="15%" id= "thBarCodeId"><s:text name="wpsal02.barCode"/></th>
	                    <th class="tableheader" width="15%"><s:text name="wpsal02.originalBrand"/></th>
	                    <th class="tableheader" width="15%"><s:text name="wpsal02.nameTotal"/></th>
	                    <th class="tableheader" width="10%"><s:text name="wpsal02.price"/></th>
	                    <th class="tableheader" width="10%"><s:text name="wpsal02.totalQuantity"/></th>
	                    <th class="tableheader" width="10%"><s:text name="wpsal02.act"/></th>
	                </tr>
	            </thead>
	            <tbody id="productdatabody">
	            </tbody>
	        </table>
	      </div>
	    </div>
	    <div id="mydetail" class="section">
	    </div>
	    <hr class="space" />
	    <hr class="space" />
	    <!--  
	    <div class="center hide">
	        <button class="save" type="button" onclick="BINOLSTSFH01.save();return false;"><span class="ui-icon icon-save"></span><span class="button-text"><s:text name="SFH01_Save"/></span></button>
	        <cherry:show domId="BINOLSTSFH0101">
	            <button class="confirm" type="button" onclick="BINOLSTSFH01.btnSendClick()"><span class="ui-icon icon-confirm"></span><span class="button-text"><s:text name="SFH01_OK"/></span></button>
	        </cherry:show>
	    </div>
	    -->
	  </div>
	  </div>
	  </form>
	</div>
	<div class="hide" id="activityProductDiv"></div>
</s:i18n>

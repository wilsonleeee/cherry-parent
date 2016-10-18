<%-- 产品发货单一览 DataTable--%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho" /></div>
<div id="iTotalRecords"><s:property value="iTotalRecords" /></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords" /></div>
<%-- ======================此段代码固定 结束======================= --%>
<!-- 收货延迟 -->
<s:i18n name="i18n.pt.BINOLPTRPS39">
	<div id="aaData">
		<s:iterator value="deliverList" id="productDeliver">
			<ul>
				<s:url id="detailsUrl" value="/st/BINOLSTSFH05_init">
					<%-- 发货单单号   --%>
					<s:param name="productDeliverId">${productDeliver.productDeliverId}</s:param>
				</s:url>
				<li>
					<%-- 序号  --%>
					<a href="${detailsUrl}" class="left" onclick="javascript:openWin(this);return false;">
						<s:property value="DeliverNo" />
					</a>
				</li>
				<li>
					<!-- 接口序号 --> 
					<s:property value="DeliverNoIF" />
				</li>
				<li>
					<!-- 关联序号  -->
					<s:property value="RelevanceNo" />
				</li>
				<li>
					<%-- 发货部门  --%>
					<s:property value="DeliverDepName" />
				</li>
				<li>
					<%-- 收货部门  --%>
					<s:property value="ReceiveDepName" />
				</li>
				<li>
					<%-- 总数量 --%>
					<s:if test="TotalQuantity !=null">
						<s:if test="TotalQuantity >= 0">
							<s:text name="format.number">
								<s:param value="TotalQuantity"></s:param>
							</s:text>
						</s:if>
						<s:else>
							<span class="highlight"><s:text name="format.number">
									<s:param value="TotalQuantity"></s:param>
								</s:text></span>
						</s:else>
					</s:if>
					<s:else>
		                &nbsp;
		            </s:else>
				</li>
				<li>
					<%-- 总金额 --%>
					<s:if test="TotalAmount !=null">
						<s:if test="TotalAmount >= 0">
							<s:text name="format.price">
								<s:param value="TotalAmount"></s:param>
							</s:text>
						</s:if>
						<s:else>
							<span class="highlight"><s:text name="format.price">
									<s:param value="TotalAmount"></s:param>
								</s:text></span>
						</s:else>
					</s:if>
					<s:else>
		                &nbsp;
		            </s:else>
				</li>
				<li>
				<%-- 发货日期  --%>
				<s:if test='DeliverDate != null && !"".equals(DeliverDate)'>
					<s:if test='DateRange > 3'>
						<span style="color:red">
							<s:property value="DeliverDate"/>
						</span>
					</s:if>
					<s:else>
						<s:property value="DeliverDate"/>
					</s:else>
				</s:if>
				<s:else>
					&nbsp;
				</s:else>
				</li>
				<li>
					<%-- 延迟天数  --%>
					<s:if test='DelayDate != null && !"".equals(DelayDate)'>
						<s:property value="DelayDate" /><s:text name="global.page.birthDayUnit2"/>
					</s:if>
					<s:else>
		                &nbsp;
					</s:else>
				</li>
				<li>
					<%-- 审核区分  --%>
					<s:if test="VerifiedFlag == @com.cherry.cm.core.CherryConstants@AUDIT_FLAG_UNSUBMIT">
						<span class="verified_unsubmit"> 
							<span><s:property value='#application.CodeTable.getVal("1180", VerifiedFlag)' /></span>
						</span>
					</s:if> 
					<s:elseif test="VerifiedFlag == @com.cherry.cm.core.CherryConstants@AUDIT_FLAG_SUBMIT">
						<span class="verifying"> 
							<span><s:property value='#application.CodeTable.getVal("1180", VerifiedFlag)' /></span>
						</span>
					</s:elseif> <s:elseif test="VerifiedFlag == @com.cherry.cm.core.CherryConstants@SDAUDIT_FLAG_SUBMIT2">
						<span class="verifying">
							<span><s:property value='#application.CodeTable.getVal("1180", VerifiedFlag)' /></span>
						</span>
					</s:elseif> 
						<s:elseif test="VerifiedFlag == @com.cherry.cm.core.CherryConstants@AUDIT_FLAG_AGREE">
						<span class="verified">
							<span><s:property value='#application.CodeTable.getVal("1180", VerifiedFlag)' /></span>
						</span>
					</s:elseif>
						<s:elseif test="VerifiedFlag == @com.cherry.cm.core.CherryConstants@AUDIT_FLAG_DISAGREE">
						<span class="verified_rejected">
							<span><s:property value='#application.CodeTable.getVal("1180", VerifiedFlag)' /></span>
						</span>
					</s:elseif>
						<s:elseif test="VerifiedFlag == @com.cherry.cm.core.CherryConstants@SDAUDIT_FLAG_AGREE2">
						<span class="verified">
							<span><s:property value='#application.CodeTable.getVal("1180", VerifiedFlag)' /></span>
						</span>
					</s:elseif>
						<s:elseif test="VerifiedFlag == @com.cherry.cm.core.CherryConstants@SDAUDIT_FLAG_DISAGREE2">
						<span class="verified_rejected">
							<span><s:property value='#application.CodeTable.getVal("1180", VerifiedFlag)' /></span>
						</span>
					</s:elseif>
						<s:elseif test="VerifiedFlag == @com.cherry.cm.core.CherryConstants@AUDIT_FLAG_DISCARD">
						<span class="verified_rejected">
							<span><s:property value='#application.CodeTable.getVal("1180", VerifiedFlag)' /></span>
						</span>
					</s:elseif> <s:else>
						<span></span>
					</s:else>
				</li>
				<li>
					<%-- 处理状态  --%>
					<s:if test='null != TradeStatus && !"".equals(TradeStatus)'>
						<s:property value='#application.CodeTable.getVal("1141",TradeStatus)' />
					</s:if>
					<s:else>
						&nbsp;
					</s:else>
				</li>
				<li>
					<!-- 制单员工 --> 
					<s:property value="EmployeeName" />
				</li>
				<li>
					<!-- 审核者 --> 
					<s:property value="AuditName" />
				</li>
			</ul>
		</s:iterator>
	</div>
</s:i18n>

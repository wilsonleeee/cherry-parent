<%-- 促销品发货单一览 DataTable--%>
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
			<s:url id="detailsUrl" value="/ss/BINOLSSPRM52_init">
				<%-- 促销产品收发货ID --%>
				<s:param name="deliverId">${productDeliver.deliverId}</s:param>
			</s:url>	
			<ul>
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
					<s:property value="DeliverDate"/>
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
					<s:if test='@com.cherry.cm.core.CherryConstants@AUDIT_FLAG_UNSUBMIT.equals(VerifiedFlag)'>
						<span class="verified_unsubmit">
							<span><s:property value='#application.CodeTable.getVal("1007", @com.cherry.cm.core.CherryConstants@AUDIT_FLAG_UNSUBMIT)'/></span>
						</span>
					</s:if>
					<s:elseif test='@com.cherry.cm.core.CherryConstants@AUDIT_FLAG_SUBMIT.equals(VerifiedFlag)'>
						<span class="verifying">
							<span><s:property value='#application.CodeTable.getVal("1007", @com.cherry.cm.core.CherryConstants@AUDIT_FLAG_SUBMIT)'/></span>
						</span>
					</s:elseif>
					<s:elseif test='@com.cherry.cm.core.CherryConstants@AUDIT_FLAG_AGREE.equals(VerifiedFlag)'>
						<span class="verified">
							<span><s:property value='#application.CodeTable.getVal("1007", @com.cherry.cm.core.CherryConstants@AUDIT_FLAG_AGREE)'/></span>
						</span>
					</s:elseif>
					<s:elseif test='@com.cherry.cm.core.CherryConstants@AUDIT_FLAG_DISAGREE.equals(VerifiedFlag)'>
						<span class="verified_rejected"><span>
						<s:property value='#application.CodeTable.getVal("1007", @com.cherry.cm.core.CherryConstants@AUDIT_FLAG_DISAGREE)'/>
						</span></span>
					</s:elseif>
		            <s:elseif test='@com.cherry.cm.core.CherryConstants@AUDIT_FLAG_DISCARD.equals(VerifiedFlag)'>
		                <span class="verified_rejected">
		                    <span><s:property value='#application.CodeTable.getVal("1007", VerifiedFlag)'/></span>
		                </span>
		            </s:elseif>
					<s:else><span></span></s:else>		
				</li>
				<li>
					<%-- 处理状态  --%>
					<s:if test='null != StockInFlag && !"".equals(StockInFlag)'>
						<span><s:property value='#application.CodeTable.getVal("1017", StockInFlag)'/></span>
					</s:if>
					<s:else>
						&nbsp;
					</s:else>
				</li>
				<li>	
					<!-- 制单员工 --> 
					<s:if test='null != EmployeeName && !"".equals(EmployeeName)'>
						<s:property value="EmployeeName" />
					</s:if>
					<s:else>
						-
					</s:else>
				</li>
				<li>
					<!-- 审核者 --> 
					<s:if test='null != EmployeeName && !"".equals(EmployeeName)'>
						<s:property value="AuditName" />
					</s:if>
					<s:else>
						-
					</s:else>
				</li>
			</ul>
		</s:iterator>
	</div>
</s:i18n>

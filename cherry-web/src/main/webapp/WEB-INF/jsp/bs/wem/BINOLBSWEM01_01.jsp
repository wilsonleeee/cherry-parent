<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ page import="com.cherry.cm.core.CherryConstants" %>
<s:set id="DATE_PATTERN_24_HOURS"><%=CherryConstants.DATE_PATTERN_24_HOURS%></s:set>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho" /></div>
<div id="iTotalRecords"><s:property value="iTotalRecords" /></div>
<div id="iTotalDisplayRecords"><s:property
	value="iTotalDisplayRecords" /></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.bs.BINOLBSWEM01">
	<div id="aaData">
		<s:iterator value="resultList" id="resultMap" status="status">
			<ul>
				<li><s:property value="#status.index+iDisplayStart+1" /></li>
				<li><s:property value="billCode"/></li>
				<li><s:property value='#application.CodeTable.getVal("1332", #resultMap.applyType)' /></li>
				<li>
					<s:if test='applyTime != null && !"".equals(applyTime)'>
		                <s:date name="applyTime" format="%{DATE_PATTERN_24_HOURS}"/>
		            </s:if>
		            <s:else>-</s:else>
				</li>
				<li><s:property value="applyLevel"/></li>
				<li><s:property value="applyName"/></li>
				<li><s:property value="applyMobile"/></li>
				<li><s:property value="applyOpenID"/></li>
				<li><s:property value="applyProvince"/></li>
				<li><s:property value="applyCity"/></li>
				<li><s:property value="applyDesc"/></li>
				<li><s:property value="superMobile"/></li>
				<li><s:property value="oldSuperMobile"/></li>
				<li><s:property value="assigner"/></li>
				<li>
					<s:if test='assignTime != null && !"".equals(assignTime)'>
		                <s:date name="assignTime" format="%{DATE_PATTERN_24_HOURS}"/>
		            </s:if>
		            <s:else>-</s:else>
		        </li>
				<li><s:property value="auditor"/></li>
				<li><s:property value='#application.CodeTable.getVal("1000", #resultMap.auditLevel)' /></li>
				<li><s:property value="reason"/></li>
				<li>
					<s:if test='auditTime != null && !"".equals(auditTime)'>
		                <s:date name="auditTime" format="%{DATE_PATTERN_24_HOURS}"/>
		            </s:if>
		            <s:else>-</s:else>
				</li>
				<li>
					<s:if test="status == @com.cherry.cm.core.CherryConstants@WEM_STATUS_1">
						<span class="verified_unsubmit">
							<span><s:property value='#application.CodeTable.getVal("1333", #resultMap.status)' /></span>
						</span>
					</s:if>
					<s:elseif test="status == @com.cherry.cm.core.CherryConstants@WEM_STATUS_2">
						<span class="verifying">
							<!-- 超过24小时未审核，申请分配上级单据可重新分配 -->
							<s:if test="applyType.compareTo(3) < 0 && reAssignFlag.compareTo(0) > 0">
								<span><s:text name="WEM01_reAssignFlag"/></span>
							</s:if>		
							<s:else>
								<span><s:property value='#application.CodeTable.getVal("1333", #resultMap.status)' /></span>
							</s:else>
						</span>			
					</s:elseif>
					<s:elseif test="status == @com.cherry.cm.core.CherryConstants@WEM_STATUS_3">
						<span class="verified">
							<span><s:property value='#application.CodeTable.getVal("1333", #resultMap.status)' /></span>
						</span>
					</s:elseif>
					<s:elseif test="status == @com.cherry.cm.core.CherryConstants@WEM_STATUS_4">
						<span class="verified_rejected">
							<span><s:property value='#application.CodeTable.getVal("1333", #resultMap.status)' /></span>
						</span>
					</s:elseif>
				</li>
				<li>
					<s:if test='"1".equals(validFlag)'>
						<span class='ui-icon icon-valid'></span>
					</s:if>
					<s:else>
						<span class='ui-icon icon-invalid'></span>
					</s:else>
				</li>
				<li>		
					<!-- 待分配 -->
					<s:if test='status == @com.cherry.cm.core.CherryConstants@WEM_STATUS_1 && "1".equals(validFlag) && applyType.compareTo(3) < 0'>
						<s:url id="assignInit_url" value="BINOLBSWEM01_assignInit">
							<s:param name="billCode"><s:property value="billCode"/></s:param>
						</s:url>
						<cherry:show domId="BINOLBSWEM01ASS">
							<a class="add" href="${assignInit_url }" onclick="BINOLBSWEM01.assign(this, '${resultMap.agentApplyId}', '${resultMap.billCode}');return false;">
								<span class="ui-icon icon-add"></span>
								<span class="button-text"><s:text name="WEM01_assign"/></span>
							</a>
						</cherry:show>
					</s:if>
					<!-- 超过24小时未审核，申请分配上级单据可重新分配 -->
					<s:elseif test='status == @com.cherry.cm.core.CherryConstants@WEM_STATUS_2 && "1".equals(validFlag) && applyType.compareTo(3) < 0 && reAssignFlag.compareTo(0) > 0'>
						<s:url id="assignInit_url" value="BINOLBSWEM01_assignInit">
							<s:param name="billCode"><s:property value="billCode"/></s:param>
						</s:url>
						<cherry:show domId="BINOLBSWEM01ASS">
							<a class="add" href="${assignInit_url }" onclick="BINOLBSWEM01.assign(this, '${resultMap.agentApplyId}', '${resultMap.billCode}');return false;">
								<span class="ui-icon icon-add"></span>
								<span class="button-text"><s:text name="WEM01_assign"/></span>
							</a>
						</cherry:show>
					</s:elseif>
					<!-- 待审核 -->
					<s:elseif test='status == @com.cherry.cm.core.CherryConstants@WEM_STATUS_2 && "1".equals(validFlag) && applyType.compareTo(2) > 0'>
						<s:url id="auditInit_url" value="BINOLBSWEM01_auditInit">
							<s:param name="applyMobile"><s:property value="applyMobile"/></s:param>
							<s:param name="billCode"><s:property value="billCode"/></s:param>
							<s:param name="agentApplyId"><s:property value="agentApplyId"/></s:param>
							<s:param name="oldSuperMobile"><s:property value="oldSuperMobile"/></s:param>
							<s:param name="reason"><s:property value="reason"/></s:param>
						</s:url>
						<cherry:show domId="BINOLBSWEM01AUD">
							<a class="add" href="${auditInit_url }" onclick="javascript:openWin(this);return false;">
								<span class="ui-icon icon-add"></span>
								<span class="button-text"><s:text name="WEM01_audit"/></span>
							</a>
						</cherry:show>
					</s:elseif>
					<s:else>
						-
					</s:else>												
				</li>
			</ul>
		</s:iterator>
	</div>
</s:i18n>
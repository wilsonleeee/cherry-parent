<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<s:i18n name="i18n.mb.BINOLMBMBM16">
<s:if test="null != memLevelList">
		<s:iterator value="memLevelList" status="status">
			<tr class="<s:if test='#status.index%2==0'>odd</s:if><s:else>even</s:else>">
				<td class="center"><span><s:property value="ticketDate" /></span></td><%--操作时间 --%>
				<s:if test='"".equals(oldLevelName)|| null == oldLevelName'>
					<td class="center"><span></span></td>
				</s:if>
				<s:else>
					<td class="center"><span><s:property value="oldLevelName" /></span></td>  <%--修改前的等级 --%>
				</s:else>
				<s:if test='"".equals(newLevelName)|| null == newLevelName'>
					<td class="center"><span></span></td>
				</s:if>
				<s:else>
					<td class="center"><span><s:property value="newLevelName" /></span></td> <%--修改后的等级 --%>
				</s:else>
				<td class="center"><span><s:property value='#application.CodeTable.getVal("1249",changeType)' /></span></td><%--变化类型 --%>
				<td><span><s:property value="billNo" /></span></td> <%--关联单号 --%>
				<td class="center"><span><s:property value='#application.CodeTable.getVal("1248",tradeType)' /></span></td> <%--业务类型 --%>
				<td class="center">
				<s:iterator value="campRuleList" status="status">
					<span><s:property value="campaignName"/><br></span>
				</s:iterator>
				</td> <%--匹配规则 --%>
				 <s:iterator value="reasonList" status="status">
					<s:if test='null!=reason'>
					<s:if test='reason.equals(messageReason_temp)'>
						<td><a><s:property value="reason" /></a></td>
					</s:if>
					<s:else>
						<td class="<s:if test='!"".equals(messageReason_temp)'>TIP</s:if>"
							title="<s:text name="binolmbmbm16_tip1"><s:param><s:property value="reason"/></s:param></s:text>"><a><s:property value="messageReason_temp" /></a>
					   </td>
					</s:else>
					</s:if>
					<s:else>
					 <s:if test='!"Cherry".equals(channel) && !"".equals(channel) && null!=channel'><td><span class="red"><s:text name="binolmbmbm16_Channel"/></span></td></s:if>
					</s:else>
				</s:iterator>
			</tr>
		</s:iterator>
	</s:if>
	<script type="text/javascript">
	$('#dataTable').find("td.TIP").cluetip({width: '350',splitTitle:'|'});
	</script>
</s:i18n>
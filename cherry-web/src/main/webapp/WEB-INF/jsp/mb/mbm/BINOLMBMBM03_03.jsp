<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<s:i18n name="i18n.mb.BINOLMBMBM03">
<s:if test="null != memUsedInfoList">
<s:iterator value="memUsedInfoList" status="status">
           	<tr class="<s:if test='#status.index%2==0'>odd</s:if><s:else>even</s:else>">
<td class="center"><span><s:property value="businessTime"/></span></td><%--操作时间 --%>
 <s:iterator value="memberConfiglist" status="status">
 <s:if test='"1040".equals(ConfigCode)&&"1".equals(ConfigValue)'>
 <s:if test='"".equals(oldMemberLevel)|| null == oldMemberLevel'><td class="center"><span></span></td></s:if>
              <s:else>
                      <td class="center"><span><s:property value="oldMemberLevel"/></span></td><%--修改前的等级 --%>
                 </s:else>
              <s:if test='"".equals(memberLevel)|| null == memberLevel'><td class="center"><span></span></td></s:if>
              <s:else>
                      <td class="center red"><span><s:property value="memberLevel"/></span></td><%--修改后的等级 --%>
                 </s:else>
                 </s:if>
                 <s:if test='"1042".equals(ConfigCode)&&"1".equals(ConfigValue)'>
                 <s:if test='"".equals(oldCurBtimes)|| null == oldCurBtimes'><td class="center"><span></span></td></s:if>
              <s:else>
                      <td class="center"><span><s:property value="oldCurBtimes"/></span></td><%--修改前的化妆次数 --%>
                 </s:else>
              <s:if test='"".equals(curBtimes)|| null == curBtimes'><td class="center"><span></span></td></s:if>
              <s:else>
              <td class="center red"><span><s:property value="curBtimes"/></span></td><%--修改后的化妆次数--%>
              </s:else>
              </s:if>
              <s:if test='"1044".equals(ConfigCode)&&"1".equals(ConfigValue)'>
              <s:if test='"".equals(oldTotalAmounts)|| null == oldTotalAmounts'><td class="center"><span></span></td></s:if>
              <s:else>
              <td class="center"><span><s:property value="oldTotalAmounts"/></span></td><%--修改前的累计金额--%>
              </s:else>
              <s:if test='"".equals(totalAmounts)|| null == totalAmounts'><td class="center"><span></span></td></s:if>
              <s:else>
              <td class="center red"><span><s:property value="totalAmounts"/></span></td><%--修改后的累积金额--%>
              </s:else>
              </s:if>
              <%-- <s:if test='"1046".equals(ConfigCode)&&"1".equals(ConfigValue)'>
              <s:if test='"".equals(oldJoinDate)|| null == oldJoinDate'><td class="center"><span></span></td></s:if>
              <s:else>
              <td class="center"><s:property value="oldJoinDate"/></td> <%--修改前的入会时间
              </s:else>
              <s:if test='"".equals(joinDate)|| null == joinDate'><td class="center"><span></span></td></s:if>
               <s:else>
              <td class="center red"><s:property value="joinDate"/></td><%--修改后的入会时间 
              </s:else>
              </s:if>
              --%>
              </s:iterator>
  <td class="center"><s:property value="employeeName"/></td><%-- 操作员 --%>
  <td class="center"><span><s:property value="updateTime"/></span></td><%--操作时间 --%>
  <s:if test='"Cherry".equals(Channel)'>
  <td >
  <s:url id="queryReason" action="BINOLMBMBM03_getReason">
		<s:param name="memUsedDetailId">${memUsedDetailId}</s:param>
  </s:url>
  <s:if test='reason.equals(messageReason_temp)'>
   <a><s:property value="reason" /></a>
  </s:if>
  <s:else>
	 <a class="reason" style="cursor:pointer;" href="${queryReason}" rel="${queryReason}" title='<s:text name="binolmbmbm03_commondetial"/>'>
		<span class="commons" ><s:property value="messageReason_temp"/></span>
	 </a>
 </s:else>
  </td><%-- 备注--%></s:if>
  <s:else><td > <span style="color:red"><s:text name="binolmbmbm03_Channel"/></span> </td><%-- 由终端修改--%></s:else>
            </tr>
           </s:iterator>
           </s:if>
</s:i18n>
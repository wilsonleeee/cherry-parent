<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<s:i18n name="i18n.mb.BINOLMBMBM24">
<div id="aaData">
 <div id="headInfo">
       <s:text name="binolmbmbm24_sCount"/>
       <span class="green" style="margin: 0px 5px;" >
           <span><strong style="font-size:15px;"><s:text name="format.number"><s:param value="sumInfo.sCount"></s:param></s:text></strong></span>
       </span>
       <s:text name="binolmbmbm24_fCount"/>
       <span class="highlight" style="margin: 0px 5px;" >
          <span> <strong style="font-size:15px;"><s:text name="format.number"><s:param value="sumInfo.fCount"></s:param></s:text></strong></span>
       </span>
       <s:text name="binolmbmbm24_pCount"/>
       <span style="margin: 0px 5px;" >
           <span><strong style="font-size:15px;"><s:text name="format.number"><s:param value="sumInfo.pCount"></s:param></s:text></strong></span>
       </span>
 </div>
   <s:iterator value="detailList" id="detail">
    <ul >
    	<li>${RowNumber}</li><%-- 编号 --%> 
    	<%--会员卡号 --%>
        <li><s:property value="MemberCode"/></li>
        <%--会员名称--%>
        <li><s:property value="MemberName"/></li>
        <%--会员等级--%>
        <li><s:property value="MemberLevel"/></li>
        <%--入会时间--%>
        <li><s:property value="JoinDate"/></li>
         <%--化妆次数--%>
        <li><s:property value="CurBtimes"/></li>
        <%--导入结果 --%>
         <li>
         <s:if test='ResultFlag==0'>
        <span class="task-verified_rejected">
        <span><s:property value="#application.CodeTable.getVal('1250',ResultFlag)"/></span>
        </span>
        </s:if>
         <s:if test='ResultFlag==1'>
        <span  class="task-verified" >
        <span><s:property value="#application.CodeTable.getVal('1250',ResultFlag)"/></span>
        </span>
        </s:if>
        <s:if test='ResultFlag==2'>
        <span  class="verifying" >
        <span><s:property value="#application.CodeTable.getVal('1250',ResultFlag)"/></span>
        </span>
        </s:if>
        </li>
        <%--备注 --%>
        <li><a class="description" title="<s:text name="binolmbmbm24_tip"><s:param><s:property value="ImportResults"/></s:param></s:text>">
        <s:if test="%{null!=ImportResults&&ImportResults.length()>20}">
          <s:property value="%{ImportResults.substring(0, 16)}" />...
 		</s:if>
 		<s:else>
          <s:property value="ImportResults" />
   		</s:else>
        </a></li>
    </ul>  
    </s:iterator>
</div>
</s:i18n>
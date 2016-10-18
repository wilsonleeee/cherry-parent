<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<s:i18n name="i18n.mb.BINOLMBPTM04">
  <div id="headInfo">
       <s:text name="binolmbptm04_sCount"/>
       <span class="green" style="margin: 0px 5px;" >
           <strong style="font-size:15px;"><s:text name="format.number"><s:param value="sumInfo.sCount"></s:param></s:text></strong>
       </span>
       <s:text name="binolmbptm04_fCount"/>
       <span class="highlight" style="margin: 0px 5px;" >
          <strong style="font-size:15px;"><s:text name="format.number"><s:param value="sumInfo.fCount"></s:param></s:text></strong>
       </span>
       <s:text name="binolmbptm04_pCount"/>
       <span  style="margin: 0px 5px;" >
           <strong style="font-size:15px;"><s:text name="format.number"><s:param value="sumInfo.pCount"></s:param></s:text></strong>
       </span>
 </div>
<div id="aaData">
   <s:iterator value="pointDetailList" id="point">
    <ul >
    	<li>${RowNumber}</li><%-- 编号 --%> 
    	<%--会员卡号 --%>
        <li><s:property value="memCode"/></li>
        <%--会员名称--%>
        <li><s:property value="memName"/></li>
         <%--手机号 --%>
        <li><s:property value="mobilePhone"/></li>
        <%--积分值--%>
        <li><s:property value="point"/></li>
        <%--积分时间 --%>
        <li><s:property value="pointTime"/></li>
         <%--导入结果 --%>
        <li>
         <s:if test='resultFlag==0'>
        <span class="task-verified_rejected">
        <span><s:property value="#application.CodeTable.getVal('1250',resultFlag)"/></span>
        </span>
        </s:if>
         <s:if test='resultFlag==1'>
        <span  class="task-verified" >
        <span><s:property value="#application.CodeTable.getVal('1250',resultFlag)"/></span>
        </span>
        </s:if>
        <s:if test='resultFlag==2'>
        <span  class="verifying" >
        <span><s:property value="#application.CodeTable.getVal('1250',resultFlag)"/></span>
        </span>
        </s:if>
        </li>
        <%--备注 --%>
        <li><a class="description" title="<s:text name="binolmbptm04_tip"><s:param><s:property value="importResults"/></s:param></s:text>">
        <s:if test="%{null!=importResults&&importResults.length()>20}">
          <s:property value="%{importResults.substring(0, 16)}" />...
 		</s:if>
 		<s:else>
          <s:property value="importResults" />
   		</s:else>
        </a></li>
    </ul>  
    </s:iterator>
</div>
</s:i18n>
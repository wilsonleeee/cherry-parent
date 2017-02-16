<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<s:i18n name="i18n.mb.BINOLMBMBM18">
    <div id="aaData">
        <div id="headInfo">
            <s:text name="binolmbmbm18_sCount"/>
       <span class="green" style="margin: 0px 5px;" >
           <span><strong style="font-size:15px;"><s:text name="format.number"><s:param value="sumInfo.sCount"></s:param></s:text></strong></span>
       </span>
            <s:text name="binolmbmbm18_fCount"/>
       <span class="highlight" style="margin: 0px 5px;" >
          <span> <strong style="font-size:15px;"><s:text name="format.number"><s:param value="sumInfo.fCount"></s:param></s:text></strong></span>
       </span>
            <s:text name="binolmbmbm18_pCount"/>
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
                <li><s:property value="MemName"/></li>
                    <%--电话号码 --%>
                <li><s:property value="MemPhone"/></li>
                    <%--手机号 --%>
                <li><s:property value="MobilePhone"/></li>
                    <%--性别--%>
                <li>
                    <s:property value="MemSex"/>
                </li>
                    <%--省份--%>
                <li><s:property value="MemProvince"/></li>
                    <%--城市--%>
                <li><s:property value="MemCity"/></li>
                    <%--地址--%>
                <li>
                    <s:if test="%{null!=MemAddress&&MemAddress.length()>12}">
                        <a class="MemAddress" style="cursor: pointer;"  title="<s:text name="binolmbmbm18_address" /> | <s:property value="MemAddress" />">
                            <s:property value="%{MemAddress.substring(0,12)}" />...
                        </a>
                    </s:if>
                    <s:else><s:property value="MemAddress" /></s:else>
                </li>
                    <%--邮编--%>
                <li><s:property value="MemPostcode"/></li>
                    <%--生日--%>
                <li><s:property value="MemBirthday"/></li>
                    <%--年龄获取方式--%>
                <li>
                    <s:property value="MemAgeGetMethod"/>
                </li>
                    <%--电子邮件--%>
                <li><s:property value="MemMail"/></li>
                    <%--开卡时间--%>
                <li><s:property value="MemGranddate"/></li>
                    <%-- bacode --%>
                <li><s:property value="Bacode"/></li>
                    <%--发卡柜台--%>
                <li><s:property value="CardCounter"/></li>
                    <%--会员等级--%>
                <li><s:property value="MemLevel"/></li>
                    <%--初始累计金额--%>
                <li><s:property value="InitTotalAmount"/></li>
                    <%--推荐会员--%>
                <li><s:property value="Referrer"/></li>
                    <%--是否是接受短信 --%>
                <li><s:property value="IsReceiveMsg"/></li>
                    <%--是否测试会员 --%>
                <li><s:property value="TestMemFlag"/></li>
                    <%--入会途径 --%>
                <li><s:property value="#application.CodeTable.getVal('1301',ChannelCode)"/></li>
                <li><s:property value="ReturnVisit"/></li>
                <li><s:property value="SkinType"/></li>
                <li><s:property value="Profession"/></li>
                <li><s:property value="Income"/></li>
                    <%--备注 --%>
                <li>
                    <s:if test="%{null!=Memo1&&Memo1.length()>12}">
                        <a class="Memo" style="cursor: pointer;"  title="<s:text name="binolmbmbm18_memo1" /> | <s:property value="Memo1" />">
                            <s:property value="%{Memo1.substring(0,12)}" />...
                        </a>
                    </s:if>
                    <s:else><s:property value="Memo1" /></s:else>
                </li>
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
                    <%--结果信息 --%>
                <li><a class="description" title="<s:text name="binolmbmbm18_tip"><s:param><s:property value="ImportResults"/></s:param></s:text>">
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
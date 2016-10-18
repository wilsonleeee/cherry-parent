<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ page import="com.cherry.cm.core.CherryConstants" %>
<s:set id="DATE_PATTERN_24_HOURS"><%=CherryConstants.DATE_PATTERN_24_HOURS%></s:set>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.mo.BINOLMOMAN01">
    <div id="aaData">
    <s:iterator value="machineInfoList" id="machineinfo">
    <s:url id="detailsUrl" action="BINOLMOMAN05_init">
        <%-- 新机器编号 --%>
        <s:param name="machineCode">${machineinfo.machineCode}</s:param>
        <%-- 老机器编号 --%>
        <s:param name="machineCodeOld">${machineinfo.machineCodeOld}</s:param>
    </s:url>
    <s:url id="bindUrl" action="BINOLMOMAN03_init" >
        <%-- 品牌ID --%>
        <s:param name="brandInfoId">${machineinfo.brandInfoId}</s:param>
        <%-- 新机器编号 --%>
        <s:param name="machineCode">${machineinfo.machineCode}</s:param>
        <%-- 老机器编号 --%>
        <s:param name="machineCodeOld">${machineinfo.machineCodeOld}</s:param>
    </s:url>
    <s:url id="deleteUrl" action="BINOLMOMAN01_delete" />
    <ul>
        <%-- 选择 --%>
        <li>
           <s:checkbox id="validFlag" name="machineList" value="false" fieldValue="" onclick="BINOLMOMAN01.checkSelect(this);"/>
           <input type="hidden" name="machineinfo_ms" value="<s:property value='machineStatus' />"></input>
           <input type="hidden" name="machineinfo_bs" value="<s:property value='bindStatus' />"></input>
           <input type="hidden" id="machineCodeArr" name="machineCodeArr" value="<s:property value='machineCode' />"></input>
           <input type="hidden" id="machineCodeOldArr" name="machineCodeOldArr" value="<s:property value='machineCodeOld' />"></input>
           <input type="hidden" id="machineTypeArr" name="machineTypeArr" value="<s:property value='machineType' />"></input>
           <input type="hidden" id="phoneCodeArr" name="phoneCodeArr" value="<s:property value='phoneCode' />"></input>
        </li>
        <%-- No. --%>
        <li><s:property value="RowNumber"/></li>
        <li>
        <%-- 15位机器编号 --%>
            <s:if test='machineCode != null && !"".equals(machineCode)'>
              <s:property value="machineCode"/>
            </s:if>
            <s:else>
              &nbsp;
            </s:else>
        </li>
        <li>
        <%-- 机器编号 --%>
            <s:property value="machineCodeOld"/>
            <%-- <a href="${detailsUrl}" class="popup" onclick="javascript:openWin(this);return false;"><s:property value="machineCodeOld"/></a> --%>
        </li>
        <li>
        <%-- 类型 --%>
	        <s:if test='machineType != null && !"".equals(machineType)'>
	          <s:property value='#application.CodeTable.getVal("1101", machineType)'/>
	        </s:if>
	        <s:else>
	          &nbsp;
	        </s:else>
        </li>
        <li>
        <%-- 当前使用柜台 --%>
            <s:if test='counterNameIF != null && !"".equals(counterNameIF)'>
                <s:property value="counterNameIF"/>
            </s:if>
        </li>
        <li>
            <%-- 软件版本 --%>
            <s:if test='softWareVersion != null && !"".equals(softWareVersion)'>
                <s:property value="softWareVersion"/>
            </s:if>
            <s:else>
                &nbsp;
            </s:else>
        </li>
        <li>
            <%-- 软件版本 --%>
            <s:if test='mobileMacAddress != null && !"".equals(mobileMacAddress)'>
                <s:property value="mobileMacAddress"/>
            </s:if>
            <s:else>
                &nbsp;
            </s:else>
        </li>
        <li>
        <%-- 通讯卡号 --%>
            <s:if test='phoneCode != null && !"".equals(phoneCode)'>
              <s:property value="phoneCode"/>
            </s:if>
            <s:else>
              &nbsp;
            </s:else>
        </li>
        <li>
        <%-- 责任人 --%>
            <s:if test='employeeName != null && !"".equals(employeeName)'>
              <s:property value="employeeName"/>
            </s:if>
            <s:else>
              &nbsp;
            </s:else>
        </li>
        <li>
	    <%-- 机器状态 --%>
	        <s:if test='machineStatus != null && !"".equals(machineStatus)'>
                <s:if test='@com.cherry.mo.common.MonitorConstants@MachineStatus_Start.equals(machineStatus)'>
                    <span class="verified">
                        <span><s:property value='#application.CodeTable.getVal("1102", machineStatus)'/></span>
                    </span>
                </s:if>
                <s:elseif test='@com.cherry.mo.common.MonitorConstants@MachineStatus_Stop.equals(machineStatus)'>
                    <span class="verified_rejected">
                        <span><s:property value='#application.CodeTable.getVal("1102", machineStatus)'/></span>
                    </span>
                </s:elseif>
                <s:elseif test='@com.cherry.mo.common.MonitorConstants@MachineStatus_UNSynchro.equals(machineStatus)'>
                    <span class="verified_unsubmit">
                        <span><s:property value='#application.CodeTable.getVal("1102", machineStatus)'/></span>
                    </span>
                </s:elseif>
                <s:elseif test='@com.cherry.mo.common.MonitorConstants@MachineStatus_Scrap.equals(machineStatus)'>
                    <span class="verified_rejected">
                        <span><s:property value='#application.CodeTable.getVal("1102", machineStatus)'/></span>
                    </span>
                </s:elseif>
	        </s:if>
	        <s:else>
	          &nbsp;
	        </s:else>
        </li>
        <li>
        <%-- 时间 --%>
	        <s:if test='startTime != null && !"".equals(startTime)'>
	          <s:date name="startTime" format="%{DATE_PATTERN_24_HOURS}"/>
	        </s:if>
	        <s:else>
	          &nbsp;
	        </s:else>
        </li>
        <li>
        <%-- 创建时间 --%>
	        <s:if test='createTime != null && !"".equals(createTime)'>
	          <s:date name="createTime" format="%{DATE_PATTERN_24_HOURS}"/>
	        </s:if>
	        <s:else>
	          &nbsp;
	        </s:else>
        </li>
        <li>
        <%-- 绑定设置柜台 --%>
            <span>
                <s:if test='"0".equals(bindStatus) && "1".equals(machineStatus)'>
                    <span class="<cherry:show domId='MOMAN01BIND'>hide</cherry:show>">
                        <s:property value='#application.CodeTable.getVal("1103", bindStatus)'/>
                    </span>
	                <cherry:show domId="MOMAN01BIND">
	                <a href="${bindUrl}" class="add" id="selectCounter" onclick="javascript:openWin(this,{height:500, width:650});return false;">
	                    <span class="ui-icon icon-add"></span>
	                    <span class="button-text"><s:text name="MAN01_selectCounter_button"></s:text></span>
	                </a>
	                </cherry:show>
                </s:if>
                <s:else>
                    <s:if test='null != bindStatus && "1".equals(bindStatus)'>
                   		<s:property value="bindCounterName"/>
                   	</s:if><s:else>
                       	<s:property value='#application.CodeTable.getVal("1103", bindStatus)'/>
                    </s:else>
                </s:else>
            </span>
        </li>
<!--        <li>-->
<!--        <%-- 操作 --%>-->
<!--            <cherry:show domId="MOMAN01DELETE">-->
<!--            <a href="${deleteUrl}" class="delete" id="deleteBtn" onclick="BINOLMOMAN01.deleteMachineInit(this);return false;">-->
<!--                <span class="ui-icon icon-delete"></span>-->
<!--                <span class="button-text"><s:text name="MAN01_delete"></s:text></span>-->
<!--            </a>-->
<!--            </cherry:show>-->
<!--        </li>-->
    </ul>
    </s:iterator>
    </div>
</s:i18n>

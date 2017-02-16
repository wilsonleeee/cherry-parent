<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.ss.BINOLSSPRM84">
<div id="aaData">
	<s:iterator value="activeInfoList" id="active">
		<%--发布URL --%>
    	<s:url id="publishUrl" value="/ss/BINOLSSPRM84_publicActive" >
	    	<s:param name="activeID">${active.activeID}</s:param>
	    </s:url>
	    <s:url id="checkUrl" value="/ss/BINOLSSPRM84_publicActive" >
	    	<s:param name="activityCode">${active.activityCode}</s:param>
	    	<s:param name="check">1</s:param>
	    </s:url>
		<%--智能促销URL --%>
    	<s:url id="prmRuleUrl" value="/ss/BINOLSSPRM88_init" >
	    	<s:param name="activeID">${active.activeID}</s:param>
	    	<s:param name="sSearch">${actState}</s:param>
	    </s:url>
    	<%--促销URL --%>
    	<s:url id="prmActiveUrl" value="/ss/BINOLSSPRM37_init" >
    		<s:param name="activeID">${active.activeID}</s:param>
    		<s:param name="sSearch">${actState}</s:param>
    	</s:url>
    	<%--促销URL --%>
    	<s:url id="publishHis" value="/mo/wat/BINOLMOWAT08_init" >
    		<s:param name="brandInfoId">${active.brandInfoId}</s:param>
    		<s:param name="subType">ACT</s:param>
    	</s:url>
		<ul>
			<li>
				<a href ="<s:if test='null == RuleCode'>${prmActiveUrl}&showType=detail</s:if><s:else>${prmRuleUrl}&pageNo=6</s:else>" onclick="openWin(this);return false;"
				<s:if test='sendFlag.equals("0")&& userValidFlag.equals("0")'>class="gray" title="|<s:text name="global.page.actMessageTip"/>"</s:if>
				><s:property value="#active.activityName"/></a>
			</li>
			<li><span class="left"><s:property value="#active.activityCode"/></span></li>
			<li><span class="left"><s:property value="#active.groupName"/></span></li>
			<li><s:property value="#active.startTime"/></li>
			<li><s:property value="#active.endTime"/></li>
			<li><s:property value="#active.createUserName"/></li>
			<li>				
				<s:if test="#active.validFlag ==1"><span class='ui-icon icon-valid'></span></s:if>
				<s:elseif test="#active.validFlag ==0"><span class='ui-icon icon-invalid'></span></s:elseif>
				<s:else><span></span></s:else>
			</li>
			<%-- 操作 --%>
			<li>
				<cherry:show domId="SSPRM0216COPY">
					<a href ="<s:if test='null == RuleCode'>${prmActiveUrl}&showType=copy</s:if><s:else>${prmRuleUrl}&opt=3</s:else>" 
					class="search" onclick="openWin(this);return false;">
						<span class="ui-icon icon-copy"></span>
						<span class="button-text">
						<s:if test='actState.equals("template")'><s:text name="prmUse"/></s:if><s:else><s:text name="prmCopy"/></s:else>
						</span>
					</a>
				</cherry:show>
				<s:if test="#active.validFlag ==1">
					<cherry:show domId="SSPRM0216STOP">
						<s:if test="%{actState == 'not_start'}">
							<a class="delete" onclick = "stopActive(${active.activeID},0);"><span class="ui-icon icon-delete"></span><span class="button-text"><s:text name="prmDelete"/></span></a>
						</s:if>
						<s:if test="%{actState == 'in_progress' && #active.SendFlag <= 2}">
							<a class="delete" onclick = "stopActive(${active.activeID},1);"><span class="ui-icon icon-delete"></span><span class="button-text"><s:text name="prmStop"/></span></a>
						</s:if>
					</cherry:show>
				</s:if>
				<s:if test="%{(userId == ActivitySetBy && #active.validFlag ==1) || ActivitySetBy == 0}">
					<cherry:show domId="SSPRM0216EDIT">
						<a href ="<s:if test='null == RuleCode'>${prmActiveUrl}&showType=edit</s:if><s:else>${prmRuleUrl}&opt=2</s:else><s:if test="%{actState == 'in_progress' || actState == 'past_due' || #active.SendFlag >= 2}">&sendFlag=2</s:if>"
						class="delete" onclick="openWin(this);return false;"><span class="ui-icon icon-edit"></span><span class="button-text"><s:text name="prmEdit"/></span></a>
					</cherry:show>
				</s:if>
				<s:if test="%{ #active.status == 0 && #active.validFlag ==1}">
				<cherry:show domId="SSPRM16CHECK">
					<a class="search" href ="#" onclick="checkActive('${checkUrl}');return false;">
						<span class="ui-icon icon-export"></span><span class="button-text"><s:text name="check"/></span>
					</a>
				</cherry:show>
				</s:if>
				<s:else>
					<s:if test="%{(actState == 'in_progress' || (actState == 'past_due' && #active.SendFlag == 2)) && #active.validFlag ==1}">
						<cherry:show domId="SSPRM16PUSH">
							<a href ="#" class="search" onclick="publishActive('${publishUrl}');return false;">
								<span class="ui-icon icon-export"></span><span class="button-text"><s:text name="publish"/></span>
							</a>
							<a href ="${publishHis}&code=<s:property value="#active.activityCode"/>" class="search" onclick="openWin(this);return false;">
							<span class="ui-icon icon-copy"></span>
							<span class="button-text"><s:text name="publishHistory"/></span>
							</a>
						</cherry:show>
					</s:if>
				</s:else>
				
			</li>
		</ul>
	</s:iterator>
</div>
</s:i18n>

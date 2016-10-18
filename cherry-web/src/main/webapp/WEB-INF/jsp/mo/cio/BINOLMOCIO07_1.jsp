<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho" /></div>
<div id="iTotalRecords"><s:property value="iTotalRecords" /></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords" /></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.mo.BINOLMOCIO07">
	<div id="aaData">
	<s:iterator value="saleTargetList" id="saletarget">
		<ul>
			<%-- No. --%>
			<li><s:property value="RowNumber" /></li>
			<li><%-- 单位代号  --%>
				<s:property	value="different" /></li>
			<li><%-- 名称  --%>
				<s:property	value="parameter" /></li>
			<li><%-- 单位类型 --%>
			<s:if test='type != null && !"".equals(type)'>
				<s:property value='#application.CodeTable.getVal("1124",#saletarget.type)' />
			</s:if> <s:else>
            	&nbsp;
            </s:else></li>
            <li><%-- 目标日期 --%>
				<s:property value="targetDate" /></li>
			<li>
			<s:if test='targetType != null && !"".equals(targetType)'>
				<s:property value='#application.CodeTable.getVal("1300",#saletarget.targetType)' />
			</s:if> <s:else>
            	&nbsp;
            </s:else></li>
            <li>
            	<s:property value="activity" />
            </li>
            <li><%-- 销售数量指标  --%>
				<s:property	value="targetQuantity" />
			</li>
			<li><%-- 销售金额指标  --%>
				<s:property	value="targetMoney" />
			</li>
			<li><%-- 下發區分  --%>
				<s:if test='"0".equals(#saletarget.synchroFlag)'>
					<span class="verified_rejected">
                        <span><s:property value='#application.CodeTable.getVal("1177",#saletarget.synchroFlag)'/></span>
                    </span>
				</s:if>
				<s:elseif test='"1".equals(#saletarget.synchroFlag)'>
					<span class="verified">
                        <span><s:property value='#application.CodeTable.getVal("1177",#saletarget.synchroFlag)'/></span>
                    </span>
				</s:elseif>
			</li>
			<li>
                <span><s:property value='#application.CodeTable.getVal("1304",#saletarget.source)'/></span>
			</li>
            <li>
                <s:property  value="targetSetTime" />
            </li>
			<li><%-- 操作  --%>
				<cherry:show domId="MOCIO07EDIT">
	        		<a class="add" id="editButton" onclick="BINOLMOCIO07.showEditDialog(this);return false;">
	    		 		<span class="button-text"><s:text name="CIO07_edit"/></span>
	    		 		<span class="ui-icon icon-edit"></span>
	    		 	</a>
	    		 	<div>
	    		 		<input type="hidden" id="parameterArr" name="parameterArr" value="<s:property value='parameterID' />"></input>
						<input type="hidden" id="saleTargetDateArr" name="saleTargetDateArr" value="<s:property value='targetDate' />"></input>
						<input type="hidden" id="nameArr" name="nameArr" value="<s:property value='parameter' />"></input>
						<input type="hidden" id="differentArr" name="differentArr" value="<s:property value='different' />"></input>
						<input type="hidden" id="targetType" name="targetType" value="<s:property value='targetType' />"></input>
	    		 		<input type="hidden" id="activityCode" name="activityCode" value="<s:property value='activityCode' />"></input>
	    		 		<input type="hidden" id="activityName" name="activityName" value="<s:property value='activityName' />"></input>
	    		 		<input type="hidden" id="targetDateTypeArr" name="targetDateTypeArr" value="<s:property value='targetDateType' />"></input>
	    		 	</div>
	    		</cherry:show>
			</li>
			
			<%--<li> 完成数量
				<s:property	value="completeQuantity" /></li>
			<li>完成数量% 
			<s:if test='targetQuantity ==0'>
				<s:property value=""/>
			</s:if>
			<s:else>
				<s:if test='completeQuantity/targetQuantity != null && !"".equals(completeQuantity/targetQuantity)'>
					<s:text name="format.percent"><s:param value="(completeQuantity*100.00)/targetQuantity" /></s:text>
				</s:if><s:else>
            		&nbsp;
            	</s:else>
            </s:else>
			</li>
			 --%>
		</ul>
	</s:iterator></div>
</s:i18n>
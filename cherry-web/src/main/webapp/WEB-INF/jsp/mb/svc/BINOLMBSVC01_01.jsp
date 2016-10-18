<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<s:i18n name="i18n.mb.BINOLMBSVC01">
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<div id="aaData">
	<s:iterator value="ruleList" id="rule" status="status">
		<s:url id="editUrl" value="/mb/BINOLMBSVC01_initEdit">
			<s:param name="subDiscountId">${rule.subDiscountId}</s:param>
		</s:url>
		<ul>
			<li><s:property value="RowNumber"/></li>
			<li><s:property value="subDiscountName"/></li>
			<li><s:property value="discountBeginDate"/></li>
			<li><s:property value="discountEndDate" /></li>
			<li><s:property value='#application.CodeTable.getVal("1400", rechargeType)'/></li>
			<li><s:property value="applyCntCount"/></li>
			<li><s:property value="usedCntCount" /></li>
			<li><s:property value="involveNumber"/></li>
			<li><s:property value="rechargeValueActual"/></li>
			<li><s:property value="giftAmount"/></li>
			<li>
				<a href="${editUrl}" class="edit" onclick="javascript:openWin(this);return false;">
			       		<span class="ui-icon icon-edit"></span>
			       		<span class="button-text">
						<s:text name="SVC01_edit" />
			        	</span>
			    </a>
			    <s:if test='"1".equals(validFlag)'>
			    	<a class="delete" onclick="BINOLMBSVC01.stopRuleDialog(${rule.discountId},${rule.subDiscountId});return false;">
						<span class="ui-icon icon-delete"></span>
						<span class="button-text">
						<s:text name="SVC01_stop" />
				    	</span>
				   	</a>
			    </s:if>
			    <s:elseif test='"0".equals(validFlag)'>
			    	<a class="add" onclick="BINOLMBSVC01.restartRuleDialog(${rule.discountId},${rule.subDiscountId});return false;">
						<span class="ui-icon icon-enable"></span>
						<span class="button-text">
						<s:text name="SVC01_restart" />
				    	</span>
				   	</a>
			    </s:elseif>
			   	
			</li>
		</ul>
	</s:iterator>
</div>
</s:i18n>
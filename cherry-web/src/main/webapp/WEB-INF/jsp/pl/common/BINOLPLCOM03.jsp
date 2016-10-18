<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<script type="text/javascript" src="/Cherry/js/pl/common/BINOLPLCOM03.js"></script>
<%--业务流程规则查看 --%>
<div class="main container clearfix">
    <div class="panel ui-corner-all">
        <div class="panel-header">
            <span class="ui-icon icon-breadcrumb"></span>
            <span class="breadcrumb"><s:property value='#application.CodeTable.getVal("1154",flowType)' /></span>
        </div>
        <div class="panel-content" id="div_main">
	        <s:if test='null!=ruleList && ruleList.size()>0'>
	        <s:iterator id="ruleMap" value="ruleList" status="R">
	            <div class="section">
	                <div class="section-header clearfix">
                        <strong class="left">
                            <span class="ui-icon icon-ttl-section-info"/></span>
                            <s:property value="stepName"/>
                        </strong>
                        <s:if test="ThirdPartyFlag != 1 && !'1'.equals(ThirdPartyFlag)">
	                        <s:if test="null != CanEditFlag && !''.equals(CanEditFlag)">
	                            <s:if test='"true".equals(CanEditFlag)'>
	                                &nbsp;&nbsp;<s:text name="os.navigation.CanEdit"/>
	                            </s:if>
	                            <s:elseif test='"false".equals(CanEditFlag)'>
	                                &nbsp;&nbsp;<s:text name="os.navigation.CannotEdit"/>
	                            </s:elseif>
	                        </s:if>
	                        <s:if test="null != PreferredFlag && !''.equals(PreferredFlag)">
	                            <s:if test='"true".equals(PreferredFlag)'>
	                                &nbsp;&nbsp;<s:text name="os.navigation.PreferredFlagTrue"/>
	                            </s:if>
	                            <s:elseif test='"false".equals(PreferredFlag)'>
	                                &nbsp;&nbsp;<s:text name="os.navigation.PreferredFlagFalse"/>
	                            </s:elseif>
	                        </s:if>
                            <s:if test='@com.cherry.cm.core.CherryConstants@OS_RULETYPE_HARD.equals(RuleType)'>
                                <s:if test='"false".equals(AutoAuditFlag)'>
                                    &nbsp;&nbsp;<s:text name="os.navigation.AutoAuditFlagFalse"/>
                                </s:if>
                                <s:else>
                                    &nbsp;&nbsp;<s:text name="os.navigation.AutoAuditFlagTrue"/>
                                </s:else>
                            </s:if>
                        </s:if>
	                </div>
	                <div class="section-content">
                        <s:if test='"1".equals(ThirdPartyFlag)'>
                            <s:text name="os.navigation.ThirdParty"/>
                        </s:if>
                        <s:else>
                            <table id="tableAudit" cellspacing="0" cellpadding="0">
                                <thead>
                                    <tr>
                                        <th style="width:1%"><s:text name="os.navigation.num"/></th>
		                                <s:if test='@com.cherry.cm.core.CherryConstants@OS_RULETYPE_EASY.equals(RuleType) || @com.cherry.cm.core.CherryConstants@OS_RULETYPE_CHERRYSHOW.equals(RuleType)'>
		                                    <th style="width:20%"><s:text name="os.navigation.actorType"/></th>
		                                    <th style="width:20%"><s:text name="os.navigation.actorID"/></th>
		                                </s:if>
		                                <s:elseif test='@com.cherry.cm.core.CherryConstants@OS_RULETYPE_HARD.equals(RuleType)'>
		                                    <th style="width:20%"><s:text name="os.navigation.initiatorType"/></th>
		                                    <th style="width:20%"><s:text name="os.navigation.initiatorID"/></th>
		                                    <th style="width:20%"><s:text name="os.navigation.auditorType"/></th>
		                                    <th style="width:20%"><s:text name="os.navigation.auditorID"/></th>
		                                    <th style="width:20%"><s:text name="os.navigation.PrivilegeRelationship"/></th>
		                                </s:elseif>
		                                <s:elseif test='@com.cherry.cm.core.CherryConstants@OS_RULETYPE_INSTEAD.equals(RuleType)'>
		                                    <th style="width:20%"><s:text name="os.navigation.receiveatorType"/></th>
		                                    <th style="width:20%"><s:text name="os.navigation.receiveatorID"/></th>
		                                    <th style="width:20%"><s:text name="os.navigation.confirmationType"/></th>
		                                    <th style="width:20%"><s:text name="os.navigation.confirmationID"/></th>
		                                    <th style="width:20%"><s:text name="os.navigation.PrivilegeRecRelationship"/></th>
		                                </s:elseif>
                                    </tr>
                                </thead>
                                <tbody>
                                    <s:if test="null != RuleContext && RuleContext.size()>0">
                                        <s:iterator id="ruleContextList" value="RuleContext" status="RR">
                                        <tr>
                                            <td><s:property value="#RR.index+1"/></td>
	                                        <s:if test='@com.cherry.cm.core.CherryConstants@OS_RULETYPE_EASY.equals(RuleType) || @com.cherry.cm.core.CherryConstants@OS_RULETYPE_CHERRYSHOW.equals(RuleType)'>
	                                            <td id="ActorTypeText"><s:property value="ActorTypeText"/></td>
	                                            <td id="ActorValueText"><s:property value="ActorValueText"/></td>
	                                        </s:if>
	                                        <s:elseif test='@com.cherry.cm.core.CherryConstants@OS_RULETYPE_HARD.equals(RuleType)'>
	                                            <td id="RoleTypeCreaterText"><s:property value="RoleTypeCreaterText"/></td>
	                                            <td id="RoleValueCreaterText"><s:property value="RoleValueCreaterText"/></td>
	                                            <td id="RoleTypeAuditorText"><s:property value="RoleTypeAuditorText"/></td>
	                                            <td id="RoleValueAuditorText"><s:property value="RoleValueAuditorText"/></td>
	                                            <td id="RolePrivilegeFlagText"><s:property value="RolePrivilegeFlagText"/></td>
	                                        </s:elseif>
	                                        <s:elseif test='@com.cherry.cm.core.CherryConstants@OS_RULETYPE_INSTEAD.equals(RuleType)'>
	                                            <td id="RoleTypeReceiverText"><s:property value="RoleTypeReceiverText"/></td>
	                                            <td id="RoleValueReceiverText"><s:property value="RoleValueReceiverText"/></td>
	                                            <td id="RoleTypeConfirmationText"><s:property value="RoleTypeConfirmationText"/></td>
	                                            <td id="RoleValueConfirmationText"><s:property value="RoleValueConfirmationText"/></td>
	                                            <td id="RolePrivilegeRecFlagText"><s:property value="RolePrivilegeRecFlagText"/></td>
	                                        </s:elseif>
                                        </tr>
                                        </s:iterator>
                                    </s:if>
                                </tbody>
                            </table>
                        </s:else>
	                </div>
	            </div>
	        </s:iterator>
	        </s:if>
	        <div class="center clearfix" id="pageButton">
	            <button class="close" type="button" onclick="window.close();return false;">
	               <span class="ui-icon icon-close"></span>
	               <span class="button-text"><s:text name="global.page.close"/></span>
	            </button>
            </div>
            <div id="configFlow" class="hide">
                <s:property value="wfFileContentMap.configWF"/>
            </div>
            <div id="actualFlow" class="hide">
                <s:property value="wfFileContentMap.actualWF"/>
            </div>
        </div>
    </div>
</div>
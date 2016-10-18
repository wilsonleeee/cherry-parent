<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<link rel="stylesheet" href="/Cherry/css/cherry/template.css" type="text/css">
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<script type="text/javascript" src="/Cherry/js/pl/common/BINOLPLCOM02.js"></script>
<script type="text/javascript">
window.onbeforeunload = function(){
    if (binOLPLCOM02_global.needUnlock) {
        if (window.opener) {
        	window.opener.binOLPLSCF15.changeBrand();
            window.opener.unlockParentWindow();
        }
    }
};
</script>
<%--工作流向导 --%>
<div class="main container clearfix">
    <s:url id="searchCodeByTypeUrl" value="/common/BINOLCM26_searchCodeByType" />
    <s:url id="saveRuleUrl" value="/pl/BINOLPLCOM02_saveRule" />
    <s:url id="saveAllRuleUrl" value="/pl/BINOLPLCOM02_saveAllRule" />
    <div class="hide">
        <a id="searchCodeByTypeUrl" href="${searchCodeByTypeUrl}"></a>
        <a id="saveRuleUrl" href="${saveRuleUrl}"></a>
        <a id="saveAllRuleUrl" href="${saveAllRuleUrl}"></a>
    </div>
    <div class="panel ui-corner-all">
        <div class="panel-header">
            <span class="ui-icon icon-breadcrumb"></span>
            <span class="breadcrumb"><s:property value='#application.CodeTable.getVal("1154",flowType)' /></span>
        </div>
        <div class="panel-content" id="div_main">
            <ol class="steps clearfix" id="ol_steps">
                <s:text name="100.00/topStepsList.size()" id="stepWidth"/>
                <s:iterator value="topStepsList" id="topStepsInfo">
                    <li style="width:<s:property value="stepWidth"/>%;" class=""><span><s:property value="stepName"/></span></li>
                </s:iterator>
            </ol>
            <div id="actionDisplay">
                <div id="errorDiv" class="actionError" style="display:none;">
                    <ul>
                        <li><span id="errorSpan"></span></li>
                    </ul>
                </div>
                <div id="successDiv" class="actionSuccess" style="display:none;">
                    <ul class="actionMessage">
                        <li><span id="successSpan"></span></li>
                    </ul>
                </div>
            </div>
            <div id="actionResultDisplay"></div>
            <form id="toNextForm" action="BINOLPLCOM02_preNextStep" method="post">
                <input type="hidden" name="csrftoken" id="parentCsrftoken"/>
                <input type="hidden" name="brandInfoId" id="brandInfoId" value="<s:property value="brandInfoId"/>"/>
                <input type="hidden" name="actionId" id="actionId" value=""/>
                <input type="hidden" name="ruleParams" id="ruleParams" value=""/>
                <input type="hidden" name="hideThirdParty" id="hideThirdParty" value=""/>
                <input type="hidden" name="hideCanEditFlag" id="hideCanEditFlag" value=""/>
                <input type="hidden" name="hidePreferredFlag" id="hidePreferredFlag" value=""/>
                <input type="hidden" name="hideAutoAuditFlag" id="hideAutoAuditFlag" value=""/>
                <input type="hidden" name="onStep" id="onStep" value="<s:property value="onStep"/>"/>
                <input type="hidden" name="flowType" id="flowType" value="<s:property value="flowType"/>"/>
                <input type="hidden" name="workFlowId" id="workFlowId" value="<s:property value="workFlowId"/>"/>
                <input type="hidden" name="ruleType" id="ruleType" value="<s:property value="ruleType"/>"/>
                <input type="hidden" name="ruleOnFlowCode" id="ruleOnFlowCode" value="<s:property value="ruleOnFlowCode"/>"/>
                <input type="hidden" name="stepId" id="stepId" value="<s:property value="stepId"/>"/>
                <s:iterator value="ruleOnAction" status="stuts">
                    <input type="hidden" name="ruleOnAction" id="ruleOnAction" value="<s:property value="ruleOnAction[#stuts.index]"/>"/>
                </s:iterator>
                <s:iterator value="ruleOnStep" status="stuts">
                    <input type="hidden" name="ruleOnStep" id="ruleOnStep" value="<s:property value="ruleOnStep[#stuts.index]"/>"/>
                </s:iterator>
            </form>
            <form id="mainForm" method="post" class="inline">
                <s:if test='null!=ruleMap'>
                    <s:if test='!@com.cherry.cm.core.CherryConstants@OS_RULETYPE_NO.equals(ruleType)'>
                        <div class="<s:if test='"3".equals(ruleMap.get("ThirdPartyFlag"))'>hide</s:if>">
                            <input type="radio" id="thirdParty_2" name="thirdParty" value="2" <s:if test='"2".equals(ruleMap.get("ThirdPartyFlag"))'>checked</s:if> onclick="binOLPLCOM02.changeRadio(this);"/><s:text name="os.navigation.NotThirdParty"/>
                            <input type="radio" id="thirdParty_1" name="thirdParty" value="1" <s:if test='"1".equals(ruleMap.get("ThirdPartyFlag"))'>checked</s:if> onclick="binOLPLCOM02.changeRadio(this);"/><s:text name="os.navigation.ThirdParty"/>
                            <input type="radio" id="thirdParty_3" name="thirdParty" value="3" class="hide" <s:if test='"3".equals(ruleMap.get("ThirdPartyFlag"))'>checked</s:if> onclick="binOLPLCOM02.changeRadio(this);"/>
                        </div>
                        <table id="ruleSet" class="detail <s:if test='"1".equals(ruleMap.get("ThirdPartyFlag"))'>hide</s:if>" cellspacing="0" cellpadding="0">
	                        <tbody>
	                            <s:if test='@com.cherry.cm.core.CherryConstants@OS_RULETYPE_EASY.equals(ruleType) || @com.cherry.cm.core.CherryConstants@OS_RULETYPE_CHERRYSHOW.equals(ruleType)'>
	                                <tr>
	                                    <th><s:text name="os.navigation.actorType"/><span class="highlight">*</span></th>
	                                    <td>
	                                        <span>
	                                        <select id="actorType" name="actorType" onchange="binOLPLCOM02.searchCodeByType(this);return false;">
	                                            <option value=""><s:text name="os.navigation.select"/></option>
	                                            <option value="1"><s:text name="os.navigation.user"/></option>
	                                            <option value="2"><s:text name="os.navigation.pos"/></option>
	                                            <option value="3"><s:text name="os.navigation.depart"/></option>
	                                        </select>
	                                        </span>
	                                    </td>
	                                    <th><s:text name="os.navigation.actorID"/><span class="highlight">*</span></th>
	                                    <td>
	                                        <span>
	                                        <select id="actorID" name="actorID" style="width:200px;">
	                                            <option value=""><s:text name="os.navigation.select"/></option>
	                                        </select>
	                                        </span>
	                                    </td>
	                                </tr>
	                            </s:if>
	                            <s:elseif test='@com.cherry.cm.core.CherryConstants@OS_RULETYPE_HARD.equals(ruleType)'>
	                                <tr>
	                                    <th><s:text name="os.navigation.initiatorType"/><span class="highlight">*</span></th>
	                                    <td>
	                                        <span>
	                                        <select id="initiatorType" name="initiatorType" onchange="binOLPLCOM02.searchCodeByType(this);return false;">
	                                            <option value=""><s:text name="os.navigation.select"/></option>
	                                            <option value="1"><s:text name="os.navigation.user"/></option>
	                                            <option value="2"><s:text name="os.navigation.pos"/></option>
	                                            <option value="3"><s:text name="os.navigation.depart"/></option>
	                                            <option value="4"><s:text name="os.navigation.departType"/></option>
	                                        </select>
	                                        </span>
	                                    </td>
	                                    <th><s:text name="os.navigation.initiatorID"/><span class="highlight">*</span></th>
	                                    <td>
	                                        <span>
	                                        <select id="initiatorID" name="initiatorID" style="width:200px;" onchange="binOLPLCOM02.hideError();return false;">
	                                            <option value=""><s:text name="os.navigation.select"/></option>
	                                        </select>
	                                        </span>
	                                    </td>
	                                </tr>
	                                <tr>
	                                    <th><s:text name="os.navigation.auditorType"/><span class="highlight">*</span></th>
	                                    <td>
	                                        <span>
	                                        <select id="auditorType" name="auditorType" onchange="binOLPLCOM02.searchCodeByType(this);return false;">
	                                            <option value=""><s:text name="os.navigation.select"/></option>
	                                            <option value="1"><s:text name="os.navigation.user"/></option>
	                                            <option value="2"><s:text name="os.navigation.pos"/></option>
	                                            <option value="3"><s:text name="os.navigation.depart"/></option>
	                                        </select>
	                                        <label id="privilegeRelationship" class="hide"><s:text name="os.navigation.PrivilegeRelationship"/></label>
                                            <select id="privilegeFlag" name="privilegeFlag" class="hide">
                                                <option value="<s:property value="@com.cherry.cm.core.CherryConstants@OS_PRIVILEGEFLAG_FOLLOW"/>"><s:text name="os.navigation.PrivilegeFollow"/></option>
                                                <option value="<s:property value="@com.cherry.cm.core.CherryConstants@OS_PRIVILEGEFLAG_LIKE"/>"><s:text name="os.navigation.PrivilegeLike"/></option>
                                                <option value="<s:property value="@com.cherry.cm.core.CherryConstants@OS_PRIVILEGEFLAG_ALL"/>"><s:text name="os.navigation.PrivilegeALL"/></option>
                                            </select>
	                                        </span>
	                                    </td>
	                                    <th><s:text name="os.navigation.auditorID"/><span class="highlight">*</span></th>
	                                    <td>
	                                        <span>
	                                        <select id="auditorID" name="auditorID" style="width:200px;" onchange="binOLPLCOM02.hideError();return false;">
	                                            <option value=""><s:text name="os.navigation.select"/></option>
	                                        </select>
	                                        </span>
	                                    </td>
	                                </tr>
	                            </s:elseif>
	                            <!-- 代收模式 -->
	                            <s:elseif test='@com.cherry.cm.core.CherryConstants@OS_RULETYPE_INSTEAD.equals(ruleType)'>
	                                <tr>
	                                    <th><s:text name="os.navigation.receiveatorType"/><span class="highlight">*</span></th>
	                                    <td>
	                                        <span>
	                                        <select id="receiveatorType" name="receiveatorType" onchange="binOLPLCOM02.searchCodeByType(this);return false;">
	                                            <option value=""><s:text name="os.navigation.select"/></option>
	                                            <option value="3"><s:text name="os.navigation.depart"/></option>
	                                            <option value="4"><s:text name="os.navigation.departType"/></option>
	                                        </select>
	                                        </span>
	                                    </td>
	                                    <th><s:text name="os.navigation.receiveatorID"/><span class="highlight">*</span></th>
	                                    <td>
	                                        <span>
	                                        <select id="receiveatorID" name="receiveatorID" style="width:200px;" onchange="binOLPLCOM02.hideError();return false;">
	                                            <option value=""><s:text name="os.navigation.select"/></option>
	                                        </select>
	                                        </span>
	                                    </td>
	                                </tr>
	                                <tr>
	                                    <th><s:text name="os.navigation.confirmationType"/><span class="highlight">*</span></th>
	                                    <td>
	                                        <span>
	                                        <select id="confirmationType" name="confirmationType" onchange="binOLPLCOM02.searchCodeByType(this);return false;">
	                                            <option value=""><s:text name="os.navigation.select"/></option>
	                                            <option value="1"><s:text name="os.navigation.user"/></option>
	                                            <option value="2"><s:text name="os.navigation.pos"/></option>
	                                            <option value="3"><s:text name="os.navigation.depart"/></option>
	                                        </select>
	                                        <label id="privilegeRecRelationship" class="hide"><s:text name="os.navigation.PrivilegeRecRelationship"/></label>
                                            <select id="privilegeRecFlag" name="privilegeRecFlag" class="hide">
                                                <option value="<s:property value="@com.cherry.cm.core.CherryConstants@OS_PRIVILEGEFLAG_FOLLOW"/>"><s:text name="os.navigation.PrivilegeFollow"/></option>
                                                <option value="<s:property value="@com.cherry.cm.core.CherryConstants@OS_PRIVILEGEFLAG_LIKE"/>"><s:text name="os.navigation.PrivilegeLike"/></option>
                                                <option value="<s:property value="@com.cherry.cm.core.CherryConstants@OS_PRIVILEGEFLAG_ALL"/>"><s:text name="os.navigation.PrivilegeALL"/></option>
                                            </select>
	                                        </span>
	                                    </td>
	                                    <th><s:text name="os.navigation.confirmationID"/><span class="highlight">*</span></th>
	                                    <td>
	                                        <span>
	                                        <select id="confirmationID" name="confirmationID" style="width:200px;" onchange="binOLPLCOM02.hideError();return false;">
	                                            <option value=""><s:text name="os.navigation.select"/></option>
	                                        </select>
	                                        </span>
	                                    </td>
	                                </tr>
	                            </s:elseif>
	                        </tbody>
	                    </table>
	                    <div class="center clearfix" style="border-bottom:1px #ccc dashed;padding-bottom:10px;">
	                        <div id="divBtnAdd" class="<s:if test='"1".equals(ruleMap.get("ThirdPartyFlag"))'>hide</s:if>">
		                    <a id="add" class="add" onclick="binOLPLCOM02.addRule(this);return false;">
		                        <span class="ui-icon icon-enable"></span>
		                        <span class="button-text"><s:text name="os.navigation.confirmAdd"/></span>
	                        </a>
	                        </div>
                        </div>
                        <hr class="space">
	                    <table id="tableAudit" class="<s:if test='"1".equals(ruleMap.get("ThirdPartyFlag"))'>hide</s:if>" cellspacing="0" cellpadding="0">
                        <thead>
                            <tr>
                                <th style="width:1%"><s:text name="os.navigation.num"/></th>
                                <s:if test='@com.cherry.cm.core.CherryConstants@OS_RULETYPE_EASY.equals(ruleType) || @com.cherry.cm.core.CherryConstants@OS_RULETYPE_CHERRYSHOW.equals(ruleType)'>
                                    <th style="width:20%"><s:text name="os.navigation.actorType"/></th>
                                    <th style="width:20%"><s:text name="os.navigation.actorID"/></th>
                                    <th style="width:20%"><s:text name="os.navigation.operator"/></th>
                                </s:if>
                                <s:elseif test='@com.cherry.cm.core.CherryConstants@OS_RULETYPE_HARD.equals(ruleType)'>
                                    <th style="width:20%"><s:text name="os.navigation.initiatorType"/></th>
                                    <th style="width:20%"><s:text name="os.navigation.initiatorID"/></th>
                                    <th style="width:20%"><s:text name="os.navigation.auditorType"/></th>
                                    <th style="width:20%"><s:text name="os.navigation.auditorID"/></th>
                                    <th style="width:10%"><s:text name="os.navigation.PrivilegeRelationship"/></th>
                                    <th id="thViewSeq" class="<s:if test='"false".equals(ruleMap.get("PreferredFlag"))'>hide</s:if>" style="width:20%"><s:text name="os.navigation.viewSeq"/></th>
                                    <th style="width:5%"><s:text name="os.navigation.operator"/></th>
                                </s:elseif>
                                <!-- 代收模式（确认） -->
                                <s:elseif test='@com.cherry.cm.core.CherryConstants@OS_RULETYPE_INSTEAD.equals(ruleType)'>
                                	<th style="width:20%"><s:text name="os.navigation.receiveatorType"/></th>
                                    <th style="width:20%"><s:text name="os.navigation.receiveatorID"/></th>
                                    <th style="width:20%"><s:text name="os.navigation.confirmationType"/></th>
                                    <th style="width:20%"><s:text name="os.navigation.confirmationID"/></th>
                                    <!-- 与接收者关系 -->
                                    <th style="width:10%"><s:text name="os.navigation.PrivilegeRecRelationship"/></th>
                                    <!-- 是否判断优先级 -->
                                    <th id="thViewSeq" class="<s:if test='"false".equals(ruleMap.get("PreferredFlag"))'>hide</s:if>" style="width:20%"><s:text name="os.navigation.viewSeq"/></th>
                                    <th style="width:5%"><s:text name="os.navigation.operator"/></th>
                                </s:elseif>
                            </tr>
                        </thead>
                        <tbody>
                            <s:if test="null != ruleMap.get('RuleContext') && ruleMap.get('RuleContext').size()>0">
                                <s:iterator id="ruleContextList" value="ruleMap.get('RuleContext')" status="R">
                                    <tr>
                                        <td><s:property value="#R.index+1"/></td>
                                        <s:if test='@com.cherry.cm.core.CherryConstants@OS_RULETYPE_EASY.equals(ruleType) || @com.cherry.cm.core.CherryConstants@OS_RULETYPE_CHERRYSHOW.equals(ruleType)'>
                                            <td id="ActorTypeText"><s:property value="ActorTypeText"/></td>
                                            <td id="ActorValueText"><s:property value="ActorValueText"/></td>
                                        </s:if>
                                        <s:elseif test='@com.cherry.cm.core.CherryConstants@OS_RULETYPE_HARD.equals(ruleType)'>
                                            <td id="RoleTypeCreaterText"><s:property value="RoleTypeCreaterText"/></td>
                                            <td id="RoleValueCreaterText"><s:property value="RoleValueCreaterText"/></td>
                                            <td id="RoleTypeAuditorText"><s:property value="RoleTypeAuditorText"/></td>
                                            <td id="RoleValueAuditorText"><s:property value="RoleValueAuditorText"/></td>
                                            <td id="RolePrivilegeFlagText"><s:property value="RolePrivilegeFlagText"/></td>
                                            <td id="viewSeq" class="<s:if test='"false".equals(ruleMap.get("PreferredFlag"))'>hide</s:if>">
                                                <s:if test='#R.index!=0'>
                                                    <a href="#" class="left" title="<s:text name='os.navigation.arrow-first'/>" onclick="binOLPLCOM02.moveFirst(this);return false;"><span class="arrow-first"></span></a>
                                                    <a href="#" class="left" title="<s:text name='os.navigation.arrow-up'/>" onclick="binOLPLCOM02.moveUp(this);return false;"><span class="arrow-up"></span></a>
                                                </s:if>
                                                <s:else>
                                                    <span class="left" style="height:16px; width:16px; display:block;"></span>
                                                    <span class="left" style="height:16px; width:16px; display:block;"></span>
                                                </s:else>
                                                <s:if test="#R.index!=ruleMap.get('RuleContext').size()-1">
                                                    <a href="#" class="left" title="<s:text name='os.navigation.arrow-down'/>" onclick="binOLPLCOM02.moveDown(this);return false;"><span class="arrow-down"></span></a>
                                                    <a href="#" class="left" title="<s:text name='os.navigation.arrow-last'/>" onclick="binOLPLCOM02.moveLast(this);return false;"><span class="arrow-last"></span></a>
                                                </s:if>
                                                <s:else>
                                                    <span class="left" style="height:16px; width:16px; display:block;"></span>
                                                    <span class="left" style="height:16px; width:16px; display:block;"></span>
                                                </s:else>
                                            </td>
                                        </s:elseif>
                                        <!-- 代收模式（确认） -->
                                        <s:elseif test='@com.cherry.cm.core.CherryConstants@OS_RULETYPE_INSTEAD.equals(ruleType)'>
                                        	<td id="RoleTypeReceiverText"><s:property value="RoleTypeReceiverText"/></td>
                                            <td id="RoleValueReceiverText"><s:property value="RoleValueReceiverText"/></td>
                                            <td id="RoleTypeConfirmationText"><s:property value="RoleTypeConfirmationText"/></td>
                                            <td id="RoleValueConfirmationText"><s:property value="RoleValueConfirmationText"/></td>
                                            <!-- 与接收者关系 -->
                                            <td id="RolePrivilegeRecFlagText"><s:property value="RolePrivilegeRecFlagText"/></td>
                                            <td id="viewSeq" class="<s:if test='"false".equals(ruleMap.get("PreferredFlag"))'>hide</s:if>">
                                                <s:if test='#R.index!=0'>
                                                    <a href="#" class="left" title="<s:text name='os.navigation.arrow-first'/>" onclick="binOLPLCOM02.moveFirst(this);return false;"><span class="arrow-first"></span></a>
                                                    <a href="#" class="left" title="<s:text name='os.navigation.arrow-up'/>" onclick="binOLPLCOM02.moveUp(this);return false;"><span class="arrow-up"></span></a>
                                                </s:if>
                                                <s:else>
                                                    <span class="left" style="height:16px; width:16px; display:block;"></span>
                                                    <span class="left" style="height:16px; width:16px; display:block;"></span>
                                                </s:else>
                                                <s:if test="#R.index!=ruleMap.get('RuleContext').size()-1">
                                                    <a href="#" class="left" title="<s:text name='os.navigation.arrow-down'/>" onclick="binOLPLCOM02.moveDown(this);return false;"><span class="arrow-down"></span></a>
                                                    <a href="#" class="left" title="<s:text name='os.navigation.arrow-last'/>" onclick="binOLPLCOM02.moveLast(this);return false;"><span class="arrow-last"></span></a>
                                                </s:if>
                                                <s:else>
                                                    <span class="left" style="height:16px; width:16px; display:block;"></span>
                                                    <span class="left" style="height:16px; width:16px; display:block;"></span>
                                                </s:else>
                                            </td>
                                        </s:elseif>
                                        <td>
                                           <a class="delete" onclick="binOLPLCOM02.deleteRule(this);return false;">
                                               <span class="ui-icon icon-delete"></span>
                                               <span class="button-text"><s:text name="global.page.delete"/></span>
                                           </a>
                                        </td>
                                        <td id="hiddentd" class="hide">
                                            <s:if test='@com.cherry.cm.core.CherryConstants@OS_RULETYPE_EASY.equals(ruleType) || @com.cherry.cm.core.CherryConstants@OS_RULETYPE_CHERRYSHOW.equals(ruleType)'>
                                                <input type="hidden" name="roleTypeActor" value="<s:property value="ActorType"/>">
                                                <input type="hidden" name="roleValueActor" value="<s:property value="ActorValue"/>">
                                            </s:if>
                                            <s:elseif test='@com.cherry.cm.core.CherryConstants@OS_RULETYPE_HARD.equals(ruleType)'>
                                                <input type="hidden" name="roleTypeCreater" value="<s:property value="RoleTypeCreater"/>">
                                                <input type="hidden" name="roleValueCreater" value="<s:property value="RoleValueCreater"/>">
                                                <input type="hidden" name="roleTypeAuditor" value="<s:property value="RoleTypeAuditor"/>">
                                                <input type="hidden" name="roleValueAuditor" value="<s:property value="RoleValueAuditor"/>">
                                                <input type="hidden" name="rolePrivilegeFlag" value="<s:property value="RolePrivilegeFlag"/>">
                                            </s:elseif>
                                            <s:elseif test='@com.cherry.cm.core.CherryConstants@OS_RULETYPE_INSTEAD.equals(ruleType)'>
                                             	<input type="hidden" name="roleTypeReceiver" value="<s:property value="RoleTypeReceiver"/>">
                                                <input type="hidden" name="roleValueReceiver" value="<s:property value="RoleValueReceiver"/>">
                                                <input type="hidden" name="roleTypeConfirmation" value="<s:property value="RoleTypeConfirmation"/>">
                                                <input type="hidden" name="roleValueConfirmation" value="<s:property value="RoleValueConfirmation"/>">
                                                <!-- 与接收者关系 -->
                                                <input type="hidden" name="rolePrivilegeRecFlag" value="<s:property value="RolePrivilegeRecFlag"/>">
                                            </s:elseif>
                                        </td>
                                    </tr>
                                </s:iterator>
                            </s:if>
                        </tbody>
                    </table>
                    <br/>
                    <div id="otherRule" class="<s:if test='"1".equals(ruleMap.get("ThirdPartyFlag"))'>hide</s:if>">
                        <s:if test="null != ruleMap.get('CanEditFlag') && !''.equals(ruleMap.get('CanEditFlag'))">
                            <input type="checkbox" id="canEdit" name="canEdit" <s:if test='"true".equals(ruleMap.get("CanEditFlag"))'>checked</s:if>/><s:text name="os.navigation.CanEdit"/>
                            <br/>
                        </s:if>
                        <s:if test="null != ruleMap.get('PreferredFlag') && !''.equals(ruleMap.get('PreferredFlag'))">
                            <input type="checkbox" id="preferred" name="preferred" <s:if test='"true".equals(ruleMap.get("PreferredFlag"))'>checked</s:if> onclick="binOLPLCOM02.changePreferredFlag();"/><s:text name="os.navigation.PreferredFlagTrue"/>
                            <span class="gray"><s:text name="os.navigation.PreferredComment"/></span>
                            <br/>
                        </s:if>
                        <s:if test='@com.cherry.cm.core.CherryConstants@OS_RULETYPE_HARD.equals(ruleType)'>
                            <%-- 复杂模式下显示。如果当前步骤工作流元数据OS_Rule没有key AutoAuditFlag，则视AutoAuditFlag=true，即执行自动审核通过。 --%>
                            <input type="checkbox" id="autoAudit" name="autoAudit" <s:if test='!"false".equals(ruleMap.get("AutoAuditFlag"))'>checked</s:if>/><s:text name="os.navigation.AutoAuditFlagTrue"/>
                            <br/>
                        </s:if>
                    </div>
                    </s:if>
                </s:if>
            </form>
            <div id="deleteRuleDiv" class="hide">
                <a class="delete" onclick="binOLPLCOM02.deleteRule(this);return false;">
                    <span class="ui-icon icon-delete"></span>
                    <span class="button-text"><s:text name="global.page.delete"/></span>
                </a>
            </div>
            <div id="arrowFirstDiv" class="hide">
                <a href="#" class="left" title="<s:text name='os.navigation.arrow-first'/>" onclick="binOLPLCOM02.moveFirst(this);return false;"><span class="arrow-first"></span></a>
            </div>
            <div id="arrowUpDiv" class="hide">
                <a href="#" class="left" title="<s:text name='os.navigation.arrow-up'/>" onclick="binOLPLCOM02.moveUp(this);return false;"><span class="arrow-up"></span></a>
            </div>
            <div id="arrowDownDiv" class="hide">
                <a href="#" class="left" title="<s:text name='os.navigation.arrow-down'/>" onclick="binOLPLCOM02.moveDown(this);return false;"><span class="arrow-down"></span></a>
            </div>
            <div id="arrowLastDiv" class="hide">
                <a href="#" class="left" title="<s:text name='os.navigation.arrow-last'/>" onclick="binOLPLCOM02.moveLast(this);return false;"><span class="arrow-last"></span></a>
            </div>
            <hr class="space" />
            <div class="center clearfix" id="pageButton">
                <s:iterator id="buttonList" value="buttonList">
                    <button type="button" class="close" onclick="<s:property value='buttonOnclick'/>">
                        <span class='ui-icon <s:property value='buttonClass'/>'></span>
                        <span class="button-text"><s:text name="%{buttonName}"/></span>
                    </button>
                </s:iterator>
            </div>
        </div>
    </div>
</div>
<div id="errmessage" style="display:none">
    <input type="hidden" id="errMsg1" value='<s:text name="os.navigation.errorMsg1"/>'/>
</div>
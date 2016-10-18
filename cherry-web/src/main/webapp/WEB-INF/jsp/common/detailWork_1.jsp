<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<s:if test='audMap.OS_TaskName !=null' >
    <tr id="audtiorTR">
       <td colspan="5" class="bg_yew">
       <div class="left">
            <s:if test='null != audMap.OS_TaskName && !"".equals(audMap.OS_TaskName)' >
	            <span class="osNextName"><s:text name="global.page.nextOperate" /></span>
	            <span class="" style="margin:0px 15px;">
	                <s:property value="audMap.OS_TaskName"/>
	            </span>
            </s:if>
        </div>
        <div class="left">
        	<span class="osNextName"><s:text name="global.page.nextAuditor" /></span>
        </div>
        <div class="left">
            <s:iterator value="audMap.participantList" id="participantList" status="status">
                <s:if test='#status.index>0 && (null != userName || null != categoryName || null != departName)'>
                    <s:if test='#status.index %1 == 0'>
                        <br/>
                    </s:if>
                </s:if>
                <s:if test = 'null != userName'>
	                <span class="" style="margin:0px 15px;">
	                <s:property value="userName"/>
	                </span>
                </s:if>
                <s:if test='null != categoryName'>
	                <span class="" style="margin:0px 15px;">
	                <s:set var="categoryTipCreator" ><s:text name="global.page.categoryTip1" /></s:set>
	                <s:set var="categoryTipReceiver" ><s:text name="global.page.categoryTip4" /></s:set>
                    <s:if test='null != RoleTypeCreater && RoleTypeCreater.equals("DT")'>
                        <s:set var="categoryTipCreator" ><s:text name="global.page.categoryTip3" /></s:set>
                    </s:if>
                    <s:if test='null != RoleTypeReceiver && RoleTypeReceiver.equals("DT")'>
                        <s:set var="categoryTipReceiver" ><s:text name="global.page.categoryTip4" /></s:set>
                    </s:if>
	                <s:if test='@com.cherry.cm.core.CherryConstants@OS_PRIVILEGEFLAG_FOLLOW.equals(privilegeFlag)' >
	                	<s:if test='@com.cherry.cm.core.CherryConstants@OS_PRIVILEGEFLAG_FOLLOW.equals(privilegeRecFlag)'>
	                		<s:text name="os.navigation.PrivilegeFollow"/><s:property value="#categoryTipReceiver"/>
	                	</s:if><s:else>
	                		<s:text name="os.navigation.PrivilegeFollow"/><s:property value="#categoryTipCreator"/>
	                	</s:else>
	                </s:if>
	                <s:elseif test='@com.cherry.cm.core.CherryConstants@OS_PRIVILEGEFLAG_LIKE.equals(privilegeFlag)' >
	                	<s:if test='@com.cherry.cm.core.CherryConstants@OS_PRIVILEGEFLAG_LIKE.equals(privilegeRecFlag)'>
	                		<s:text name="os.navigation.PrivilegeLike"/><s:property value="#categoryTipReceiver"/>
	                	</s:if><s:else>
	                    	<s:text name="os.navigation.PrivilegeLike"/><s:property value="#categoryTipCreator"/>
	                    </s:else>
	                </s:elseif>
	                <s:elseif test='@com.cherry.cm.core.CherryConstants@OS_PRIVILEGEFLAG_ALL.equals(privilegeFlag)' >
	                	<s:if test='@com.cherry.cm.core.CherryConstants@OS_PRIVILEGEFLAG_ALL.equals(privilegeRecFlag)'>
	                		<s:text name="os.navigation.PrivilegeALL"/><s:property value="#categoryTipReceiver"/>
	                	</s:if><s:else>
	                    	<s:text name="os.navigation.PrivilegeALL"/><s:property value="#categoryTipCreator"/>
	                    </s:else>
	                </s:elseif>
	                <s:text name="global.page.categoryTip2" />
	                <span class="green"><s:property value="categoryName"/></span>
	                <span id="spanOSEmployee" title=""><s:text name="global.page.departTip2" /></span> 
	                </span>
                </s:if>
                <s:if test="null != departName">
	                <span class="" style="margin:0px 15px;">
	                <s:text name="global.page.departTip1" />
	                <span class="green"><s:property value="departName"/></span>
	                <span id="spanOSEmployee" title=""><s:text name="global.page.departTip2" /></span>
	                </span>
                </s:if>
                <span id="spanEmpList" class="hide">
                    <s:iterator value="audMap.personList" id="employeeCodeName" status="status">
                        <s:property value="employeeCodeName"/>&amp;lt;br/&amp;gt;
                    </s:iterator>
                </span>
                <s:if test = 'null != ThirdParty'>
                    <span class="" style="margin:0px 15px;">
                    <s:text name="global.page.ThirdParty" />
                    </span>
                </s:if>
            </s:iterator>
        </div>
       	<s:if test='null != audMap.operationURL && !"".equals(audMap.operationURL)' >
            <a style="margin-top:3px;" class="add" onclick="detailWork_billJump('<s:property value="audMap.operationURL"/>');return false;">
                <span class="ui-icon icon-setting"></span>
                <span class="button-text"><s:text name="global.page.option"/></span>
            </a>
        </s:if>
       </td>
    </tr>
</s:if>
<%@ page language="java" pageEncoding="utf-8"%>
<%@ page import="java.util.*" %>
<%@ page import="com.cherry.cm.core.CherryConstants" %>
<%@ page import="com.cherry.cm.cmbeans.UserInfo" %>
<%@ page import="com.cherry.cm.cmbeans.ControlOrganization" %>
<%@ page import="com.cherry.cm.cmbeans.RoleInfo" %>
<%@ page import="com.cherry.cm.cmbeans.CherryTaskInstance" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%> 
<script type=text/javascript>

</script>
<cherry:form id="mainForm" onsubmit="javascript:return false;" csrftoken="true">
 <input type="hidden" id="currentUnitID" name="currentUnitID" value="TOP"/>
<s:i18n name="i18n.lg.BINOLLGTOP02">
<div class="panel ui-corner-all">
      <div class="panel-content">  
      <div class="welcome">
        	<div class="welcome-l">
            <div class="welcome-r">
            <p class="welcome-t"></p>
            <div class="section">
             <div class="section-header"><strong><span class="ui-icon icon-ttl-section-info"></span><s:text name="lblLoginInfo"/></strong></div>
                <div class="section-content">
                  <table border="0" style="width:80%;" class="detail">
                    <tbody><tr>
                      <th><s:text name="lblLoginName"/></th>
                      <td><s:property value="#session.userinfo.loginName"/></td>
                    </tr>
                    <tr>
                      <th><s:text name="lblUserID"/></th>
                      <td><s:property value="#session.userinfo.BIN_UserID"/></td>
                    </tr>
                    <tr>
                      <th><s:text name="lblIPAddr"/></th>
                      <td><s:property value="#session.userinfo.loginIP"/></td>
                    </tr>
                    <tr>
                      <th><s:text name="lblLoginTime"/></th>
                      <s:set id="DATE_PATTERN_24_HOURS"><%=CherryConstants.DATE_PATTERN_24_HOURS%></s:set>
                      <td><s:date name="#session.userinfo.loginTime" format="%{DATE_PATTERN_24_HOURS}"/></td>
                    </tr>
                    <tr>
                      <th><s:text name="lblEmployeeCode"/></th>
                      <td>
                          <s:if test='#session.userinfo.employeeCode == null || "null".equals(#session.userinfo.employeeCode)'>&nbsp;</s:if>
                          <s:else><s:property value="#session.userinfo.employeeCode"/></s:else>
                      </td>
                    </tr>
                    <tr>
                      <th><s:text name="lblEmployeeName"/></th>
                      <td>
                          <s:if test='#session.userinfo.employeeName == null || "null".equals(#session.userinfo.employeeName)'>&nbsp;</s:if>
                          <s:else><s:property value="#session.userinfo.employeeName"/></s:else>
                      </td>
                    </tr>
                     <tr>
                      <th>组织信息ID</th>
                      <td><s:property value="#session.userinfo.BIN_OrganizationInfoID"/></td>
                    </tr>
                    <tr>
                      <th><s:text name="lblOrgName"/></th>
                      <td>
                          <s:if test='#session.userinfo.orgName == null || "null".equals(#session.userinfo.orgName)'>&nbsp;</s:if>
                          <s:else><s:property value="#session.userinfo.orgName"/></s:else>
                      </td>
                    </tr>
                     <tr>
                      <th>品牌ID</th>
                      <td><s:property value="#session.userinfo.BIN_BrandInfoID"/></td>
                    </tr>
                    <tr>
                      <th><s:text name="lblBrandName"/></th>
                      <td>
                          <s:if test='#session.userinfo.brandName == null || "null".equals(#session.userinfo.brandName)'>&nbsp;</s:if>
                          <s:else><s:property value="#session.userinfo.brandName"/></s:else>
                      </td>
                    </tr>
                  </tbody>
                 </table>
                </div>
              </div>        	         
        	<div class="section">
                <div class="section-header"><strong><span class="ui-icon icon-ttl-section-info"></span><s:text name="lblPositionInfo"/></strong></div>
                <div class="section-content">
                  <table border="0">
                    <tbody><tr>
                      <th><s:text name="lblOrganizationID"/></th>
                      <th><s:text name="lblDepartName"/></th>
                      <th>部门类型</th>
                      <th>管理类型</th>
                    </tr>
                    <s:iterator id="orgBrandPositionList" value="#session.userinfo.controlOrganizationList">
				    	<tr>
				    	    <td><s:property value="#orgBrandPositionList.BIN_OrganizationID"/></td>
                            <td><s:property value="#orgBrandPositionList.DepartName"/></td>
                            <td><s:property value="#orgBrandPositionList.DepartType"/></td>
                            <td><s:property value="#orgBrandPositionList.ManageType"/></td>
				    	</tr>
                    </s:iterator>
                  </tbody></table>
                </div>
              </div>
        	<div class="section">
                <div class="section-header"><strong><span class="ui-icon icon-ttl-section-info"></span><s:text name="lblRoleInfo"/></strong></div>
                <div class="section-content">
                  <table border="0">
                    <tbody><tr>
                      <th><s:text name="lblRoleID"/></th>
                      <th><s:text name="lblRoleName"/></th>
                      <th><s:text name="lblRoleKind"/></th>
                    </tr>
                    <s:iterator id="roleList" value="#session.userinfo.rolelist">
				    	<tr>
                            <td><s:property value="#roleList.BIN_RoleID"/></td>
                            <td><s:property value="#roleList.RoleName"/></td>
                            <td><s:property value="#roleList.RoleKind"/></td>
				    	</tr>
                    </s:iterator>
                  </tbody></table>
                </div>
              </div>
        	
        	</div>
            </div>
        </div>
   </div>
  </div>
    
</s:i18n>
</cherry:form>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/pl/rla/BINOLPLRLA99.js"></script>

<script type="text/javascript">

$(function () {
	
	var holidays = '${holidays }';
	$('#roleInfoList :input[name=startDate]').cherryDate({holidayObj: holidays});
	$('#roleInfoList :input[name=expireDate]').cherryDate({
		holidayObj: holidays,
		beforeShow: function(input){
			var value = $(input).parent().parent().prev().find(":input[name=startDate]").val();
			return [value,'minDate'];
		}
	});

	
});

</script>




<s:i18n name="i18n.pl.BINOLPLRLA01">
<s:if test="%{roleList != null && !roleList.isEmpty()}">
	<div id="hiddenParam">
	<s:if test="%{organizationId != null}">
		<s:hidden name="organizationId"></s:hidden>
	</s:if>
	<s:elseif test="%{positionCategoryId != null}">
		<s:hidden name="positionCategoryId"></s:hidden>
	</s:elseif>
	<s:elseif test="%{positionId != null}">
		<s:hidden name="positionId"></s:hidden>
	</s:elseif>
	<s:elseif test="%{userId != null}">
		<s:hidden name="userId"></s:hidden>
	</s:elseif>
	<s:hidden name="brandInfoId"></s:hidden>
	</div>
	<div class="hide">
		<s:url action="BINOLPLRLA99_add" id="saveRoleAssignUrl"></s:url>
		<a href="${saveRoleAssignUrl }" id="saveRoleAssignUrl"></a>
	</div>
	 
	
	
	<s:if test="!hasFieldErrors()">
	<div style="display:none" id="saveSuccessId">
	<div class="actionSuccess">
       <ul>
         <li><span><s:text name="save_success_message" /></span></li>
       </ul>         
    </div><br/>
    </div>
    </s:if>
	
	<table cellpadding="0" cellspacing="0" border="0" class="editable" width="100%" id="roleInfoList">
      <tr>
        <th style="width: 5%"><input type="checkbox" id="roleIdAll"/></th>
        <th style="width: 20%"><s:text name="role_name" /></th>
        <th style="width: 25%"><s:text name="role_decription" /></th>
        <%-- 
        <th style="width: 15%"><s:text name="role_kind" /></th>
        --%>
        <th style="width: 25%"><s:text name="startDate" /></th>
        <th style="width: 25%"><s:text name="expireDate" /></th>
        <s:if test="%{userId != null}">
        <th><s:text name="privilegeFlag" /></th>
        </s:if>
      </tr>
      <s:iterator id="role" value="roleList" status="status">
      <tr>
        <td><s:checkbox name="roleId" id="roleId" value="%{#role.checkbox}" fieldValue="%{#role.roleId}"></s:checkbox></td>
        <td><p><s:property value="#role.roleName" ></s:property></p></td>
        <td><p><s:property value="#role.decription" ></s:property>&nbsp;</p></td>
        <%-- 
        <td><p>
        	<s:property value='#application.CodeTable.getVal("1009", #role.roleKind)' />
        </p></td>
        --%>
        <td>
        	<p>
        	<s:textfield name="startDate" value="%{#role.startDate}" cssClass="date" id="startDate%{#status.index}" cssStyle="width:80px;"></s:textfield>
        	<s:fielderror cssClass="ui-icon icon-error">
        		<s:param>startDate${role.roleId }</s:param>
        	</s:fielderror>
        	</p>
        </td>
		<td>
			<p>
			<s:textfield name="expireDate" value="%{#role.expireDate}" cssClass="date" id="expireDate%{#status.index}" cssStyle="width:80px;"></s:textfield>
			<s:fielderror cssClass="ui-icon icon-error">
        		<s:param>expireDate${role.roleId }</s:param>
        	</s:fielderror>
        	</p>
		</td>
		<s:if test="%{userId != null}">
        <td>
        	<s:select list='#application.CodeTable.getCodes("1010")' listKey="CodeKey" listValue="Value" name="privilegeFlag"></s:select>
        </td>
        </s:if>
      </tr>
      </s:iterator>
    </table>
    <s:if test="%{userId == null}">
    <hr class="space" />
    <div class="center" id="saveRoleAssignButton">
      <button class="save" onclick="saveRoleAssign(false);return false;">
        <span class="ui-icon icon-save"></span><span class="button-text"><s:text name="add_button" /></span>
      </button>
    </div>
    </s:if>
    <div id="ajaxResultMsg" class="hide">
		<s:text name="save_success_message" />
	</div>
</s:if>
</s:i18n>

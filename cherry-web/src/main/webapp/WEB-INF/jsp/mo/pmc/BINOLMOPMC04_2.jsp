<!-- 编辑 -->
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>

<script type="text/javascript">
	
	cherryValidate({
		formId: 'editMenuForm',	
		rules: {
			brandMenuNameCN:{required: true,maxlength: 50},// nvarchar(50)
			brandMenuNameEN:{maxlength: 50},
			menuType: {byteLengthValid: [50]},
			container: {byteLengthValid: [200]}, //字节数最大为200（一个中文两个字节）
			menuValue: {byteLengthValid: [50]}, //varchar(50)
			menuLink: {maxlength: 255},
			comment: {maxlength: 50}
		}	
	});
</script>
<s:i18n name="i18n.mo.BINOLMOPMC04">
<!-- 保存对菜单的编辑 -->
<s:url id="saveEdit_url" action="BINOLMOPMC04_saveEdit">
</s:url>
<div class="hide">
	<a id="saveEditUrl" href="${saveEdit_url}"></a>
</div>
<div class="clearfix">
   	 <form id="editMenuForm">
   		<div>
   		<s:hidden name="posMenuBrandID" value="%{posMenuDetail.posMenuBrandID}"/>
   		<s:hidden name="posMenuID" value="%{posMenuDetail.posMenuID}"/>
   		<table style="margin:auto; width:100%;" class="detail">
   		<tr>
        	<th><s:text name="PMC04_menuCode"/></th>
        	<td>
        		<span>
        			<s:textfield name="menuCode" id="menuCode" cssClass="text" maxlength="50" value="%{posMenuDetail.menuCode}" disabled="true"/>
        		</span>
        	</td>
        </tr>
        <tr>
        	<th><s:text name="PMC04_menuNameCN"/></th>
        	<td>
        		<span>
        			<s:textfield name="brandMenuNameCN" id="brandMenuNameCN" cssClass="text" maxlength="50" value="%{posMenuDetail.brandMenuNameCN}"/>
        		</span>
        	</td>
        </tr>
        <tr>
        	<th><s:text name="PMC04_menuNameEN"/></th>
        	<td>
        		<span>
        			<s:textfield name="brandMenuNameEN" id="brandMenuNameEN" cssClass="text" maxlength="50" value="%{posMenuDetail.brandMenuNameEN}"/>
        		</span>
        	</td>
        </tr>
   		<tr>
	   		<th><s:text name="PMC04_menuContainer"/></th>
	   		<td><span><s:textfield name="container" cssClass="text" maxlength="200" value="%{posMenuDetail.container}"/></span></td>
   		</tr>
   		<tr>
	   		<th><s:text name="PMC04_menuType"/></th>
	   		<td><span><s:textfield name="menuType" cssClass="text" maxlength="50" value="%{posMenuDetail.menuType}"/></span></td>
   		</tr>
   		<tr>
	   		<th><s:text name="PMC04_menuScreen"/></th>
	   		<td><span><s:textfield name="menuValue" cssClass="text" maxlength="50" value="%{posMenuDetail.menuValue}"/></span></td>
   		</tr>
   		<tr>
	   		<th><s:text name="PMC04_menuLink"/></th>
	   		<td><span><s:textfield name="menuLink" cssClass="text" maxlength="255" value="%{posMenuDetail.menuLink}"/></span></td>
   		</tr>
   		<tr>
	   		<th><s:text name="PMC04_comment"/></th>
	   		<td><span><s:textfield name="comment" cssClass="text" maxlength="50" value="%{posMenuDetail.comment}"/></span></td>
   		</tr>
   		<tr>
   			<th><s:text name="PMC04_machineType" /></th>
   			<td><span><s:select name="machineType" id="machineType" list='#application.CodeTable.getCodes("1284")' 
   			value="%{posMenuDetail.machineType}" listKey="CodeKey" listValue="Value" disabled="true"/></span></td>
   		</tr>
   		<%-- <s:if test='"1".equals(posMenuDetail.editConfigFlag)'>
   			<tr>
		   		<th><s:text name="PMC04_addModifyDate"/></th>
		   		<td>
		   			<span><s:textfield name="configValue" cssClass="text" maxlength="50" value="%{posMenuDetail.configValue}"/></span>
		   			<span><s:text name="PMC04_day"/></span>
		   		</td>
   			</tr>
   		</s:if> --%>
   		</table>
   		</div>
   	</form>
</div>
</s:i18n>
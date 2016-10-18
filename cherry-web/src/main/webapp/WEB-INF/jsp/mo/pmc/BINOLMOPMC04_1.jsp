<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>

<s:i18n name="i18n.mo.BINOLMOPMC04">
<div class="clearfix">
    	<div>
    		<s:hidden name="posMenuBrandID" value="%{posMenuDetail.posMenuBrandID}"/>
    		<s:hidden name="posMenuID" value="%{posMenuDetail.posMenuID}"/>
    		<table style="margin:auto; width:100%;" class="detail">
	    		<tr>
	    			<th><s:text name="PMC04_menuCode"/></th>
		    		<td><s:property value="posMenuDetail.menuCode"/></td>
		    		<th><s:text name="PMC04_menuNameCN"/></th>
		    		<td><s:property value="posMenuDetail.brandMenuNameCN"/></td>
	            </tr>
	    		<tr>
	    			<th><s:text name="PMC04_menuType"/></th>
		    		<td><s:property value="posMenuDetail.menuType"/></td>
		    		<th><s:text name="PMC04_menuNameEN"/></th>
		    		<td><s:property value="posMenuDetail.brandMenuNameEN"/></td>
	    		</tr>
	    		<tr>
	    			<th><s:text name="PMC04_menuContainer"/></th>
		    		<td><s:property value="posMenuDetail.container"/></td>
		    		<th><s:text name="PMC04_menuScreen"/></th>
		    		<td><s:property value="posMenuDetail.menuValue"/></td>
	    		</tr>
	    		<tr>
		    		<th><s:text name="PMC04_menuLink"/></th>
		    		<td><span>
					  <s:set value="%{posMenuDetail.menuLink}" var="menuLink"></s:set>
					  <a title="" rel='<s:property value="menuLink"/>' onclick="BINOLMOPMC04.showMenuLinkInfo(this);return false;" style="cursor:pointer;">
						<cherry:cut length="25" value="${menuLink }"></cherry:cut>
					  </a>
					</span></td>
	    			<th><s:text name="PMC04_comment"/></th>
		    		<td><s:property value="posMenuDetail.comment"/></td>
	    		</tr>
	    		<tr>
	    			<!-- 机器类型 -->
	    			<th><s:text name="PMC04_machineType"/></th>
	    			<td><s:property value='#application.CodeTable.getVal("1284", posMenuDetail.machineType)'/></td>
	    			<th><s:text name="PMC04_isLeaf"/></th>
	    			<td>
	    				<s:if test='"1".equals(posMenuDetail.isLeaf)'>
	    				<input type="radio" id="isLeafTrue" disabled="disabled" checked/><label for="isLeafTrue" style="width:10px"><s:text name="PMC04_yes"/></label>
						<input type="radio" id="isLeafFalse" disabled="disabled"/><label for="isLeafFalse" style="width:10px"><s:text name="PMC04_no"/></label>
	    				</s:if><s:else>
	    				<input type="radio" id="isLeafTrue" disabled="disabled"/><label for="isLeafTrue" style="width:10px"><s:text name="PMC04_yes"/></label>
						<input type="radio" id="isLeafFalse" disabled="disabled" checked/><label for="isLeafFalse" style="width:10px"><s:text name="PMC04_no"/></label>
	    				</s:else>
	    			</td>
	    		</tr>
    		</table>
    	</div>
    	<hr class="space">
    	<div id="editButton" class="center clearfix">
    		<!-- 【补登销售记录】菜单可编辑补登天数 -->
    		<s:if test='"1".equals(posMenuDetail.editConfigFlag)'>
    			<cherry:show domId="MOPMC04SETCONF">
	    			<s:url action="BINOLMOPMC04_updateConfigInit" id="updateConfigInit_url">
	    			<s:param name="machineType" value="%{posMenuDetail.machineType}"></s:param>
					</s:url>
					<a id="updateConfigInitUrl" href="${updateConfigInit_url}"></a>
		    		<button class="edit" onclick="BINOLMOPMC04.popEditConfigDialog(this);return false">
		          		<span class="ui-icon icon-edit-big"></span>
		          		<span class="button-text"><s:text name="PMC04_modifyDate"/></span>
		          	</button>
	          	</cherry:show>
          	</s:if>
    		<!-- 编辑菜单 -->
    		<s:url action="BINOLMOPMC04_editInit" id="editMenuUrl">
			<s:param name="posMenuBrandID" value="%{posMenuDetail.posMenuBrandID}"></s:param>
			</s:url>
			<a id="editInit_url" href="${editMenuUrl}"></a>
        	<button class="edit" onclick="BINOLMOPMC04.popEditMenu(this);return false">
          		<span class="ui-icon icon-edit-big"></span>
          		<span class="button-text"><s:text name="global.page.edit"/></span>
          	</button>
          	<!-- 启用、停用菜单[具体的菜单] -->
          	<%-- <s:url action="BINOLMOPMC04_disOrEnableMenu" id="disOrEnableMenu">
       			<s:param name="posMenuID" value="%{posMenuDetail.posMenuID}"></s:param>
       		</s:url> --%>
       		<!-- 停用、启用功能暂时废弃 -->
          	<%-- <s:if test="%{posMenuDetail.validFlag == 1}">
          		<button class="edit" onclick="BINOLMOPMC04.editValidFlag('disable','${disOrEnableMenu }');return false;">
              		<span class="ui-icon icon-delete-big"></span>
              		<span class="button-text"><s:text name="global.page.disable"/></span>
              	</button>
         	</s:if>
         	<s:else>
          		<button class="edit" onclick="BINOLMOPMC04.editValidFlag('enable','${disOrEnableMenu }');return false;">
              		<span class="ui-icon icon-success"></span>
              		<span class="button-text"><s:text name="global.page.enable"/></span>
              	</button>
            </s:else> --%>
    	</div>
     
</div>
</s:i18n>
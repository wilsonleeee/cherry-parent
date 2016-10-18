<%-- 编辑--%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<s:i18n name="i18n.mo.BINOLMOMAN09">
<div id="actionResultDisplay"></div>
<form id="main_form">
 <table width="80%" class="detail" border="1" cellspacing="0" cellpadding="0">
  <tr>
    <th><s:text name="MAN09_menuCodee"/></th>
    <td><input style="width:100%;" maxlength="50"  name="menuCode" value="<s:property value="posMemu.MenuCode" />"/></td>
  </tr>
  <tr>
    <th><s:text name="MAN09_menuType"/></th>
    <td><input style="width:100%;" maxlength="50"  name="menuType" value="<s:property value="posMemu.MenuType"/>"/></td>
  </tr>
  <tr>
    <th><s:text name="MAN09_menuLink"/></th>
    <td><input style="width:100%;" maxlength="255" name="menuLink" value="<s:property value="posMemu.MenuLink"/>"/></td>
  </tr>
  <tr>
    <th><s:text name="MAN09_Comment"/></th>
    <td><input style="width:100%;" maxlength="50" name="comment" value="<s:property value="posMemu.Comment"/>"/></td>
  </tr>
  <tr>
    <th><s:text name="MAN09_IsLeaf"/></th>
    <td>
    <input type="radio" id="IsLeaf" name="isLeaf" value="1" /><label  for="BatchStockTakingTrue"  style="width:10px"><s:text name="MAN09_yesr"/></label>
	<input type="radio" id="IsLeaf" name="isLeaf" value="0" checked/><label for="BatchStockTakingFalse" style="width:10px"><s:text name="MAN09_no"/></label>
  </tr>
</table>
	<input type="hidden" name="posMenuID" value="<s:property value="posMemu.BIN_PosMenuID"/>"/>
	<input type="hidden" name="opt" value="<s:property value="posMemu.add"/>"/>

</form>
<script type="text/javascript">
</script>
</s:i18n>
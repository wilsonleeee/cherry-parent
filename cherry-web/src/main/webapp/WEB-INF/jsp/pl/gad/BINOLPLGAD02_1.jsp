<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>

<s:i18n name="i18n.pl.BINOLPLGAD02">
<div class="section-content">
  <form id="gadgetInfoForm">
  <table class="detail" cellpadding="0" cellspacing="0">
    <tr>
      <th><s:text name="binolplscf02.gadgetName"></s:text></th>
      <th><s:text name="binolplscf02.rowNumber"></s:text></th>
      <th><s:text name="binolplscf02.columnNumber"></s:text></th>
    </tr>
    <s:iterator value="gadgetInfoList" id="gadgetInfoMap" status="status">
      <tr>
        <td><s:property value="gadgetName"/><s:hidden name="gadgetInfoIds" value="%{#gadgetInfoMap.gadgetInfoId}"></s:hidden></td>
        <td><span><s:textfield name="rowNumbers" value="%{#gadgetInfoMap.rowNumber}" cssClass="text"></s:textfield></span></td>
        <td><span><s:textfield name="columnNumbers" value="%{#gadgetInfoMap.columnNumber}" cssClass="text"></s:textfield></span></td>
      </tr>
    </s:iterator>
  </table>     
  </form>  
</div>

<div class="center">
  <s:url action="BINOLPLGAD02_save" id="saveGadgetInfoUrl"></s:url>
  <button class="save" type="button" onclick="binolplgad02.saveGadgetInfo('${saveGadgetInfoUrl}');return false;">
	<span class="ui-icon icon-save"></span>
	<span class="button-text"><s:text name="global.page.save"></s:text></span>
  </button>
</div>
</s:i18n>
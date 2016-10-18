<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>

<s:i18n name="i18n.mb.BINOLMBRPT03">
<table cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
  <thead>
    <tr>
      <th><s:text name="mbrpt_levelName"></s:text></th>
      <th><s:text name="mbrpt_memCount"></s:text></th>
      <th><s:text name="mbrpt_amount"></s:text></th>
      <th><s:text name="mbrpt_memCountRate"></s:text></th>
      <th><s:text name="mbrpt_amountRate"></s:text></th>
    </tr>
  </thead>
  <tbody>
    <s:iterator value="memLevelCountList" id="memLevelCountMap">
    <tr>
      <td><s:property value="levelName"/></td>
      <td><s:property value="memCount"/></td>
      <td><s:property value="amount"/></td>
      <td><s:property value="memCountRate"/></td>
      <td><s:property value="amountRate"/></td>
    </tr>
    </s:iterator>
  </tbody>
</table>
</s:i18n>
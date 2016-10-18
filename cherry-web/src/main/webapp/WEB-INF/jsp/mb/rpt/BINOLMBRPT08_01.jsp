<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>

<s:i18n name="i18n.mb.BINOLMBRPT08">
<table cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
  <thead>
    <tr>
      <th><s:text name="mbrpt08_orderCount"></s:text></th>
      <th><s:text name="mbrpt08_bookCount"></s:text></th>
      <th><s:text name="mbrpt08_getCount"></s:text></th>
      <th><s:text name="mbrpt08_getPercent"></s:text></th>
      <th><s:text name="mbrpt08_memCount"></s:text></th>
      <th><s:text name="mbrpt08_saleAmount"></s:text></th>
      <th><s:text name="mbrpt08_salePercent"></s:text></th>
      <th><s:text name="mbrpt08_newMemCount"></s:text></th>
      <th><s:text name="mbrpt08_oldMemCount"></s:text></th>
      <th><s:text name="mbrpt08_newMemSaleAmount"></s:text></th>
      <th><s:text name="mbrpt08_oldMemSaleAmount"></s:text></th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td><s:property value="camCountInfo.orderCount"/></td>
      <td><s:property value="camCountInfo.bookCount"/></td>
      <td><s:property value="camCountInfo.getCount"/></td>
      <td><s:property value="camCountInfo.getPercent"/></td>
      <td><s:property value="camCountInfo.memCount"/></td>
      <td><s:property value="camCountInfo.saleAmount"/></td>
      <td><s:property value="camCountInfo.salePercent"/></td>
      <td><s:property value="camCountInfo.newMemCount"/></td>
      <td><s:property value="camCountInfo.oldMemCount"/></td>
      <td><s:property value="camCountInfo.newMemSaleAmount"/></td>
      <td><s:property value="camCountInfo.oldMemSaleAmount"/></td>
    </tr>
  </tbody>
</table>
</s:i18n>        
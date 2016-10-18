<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>

<s:i18n name="i18n.mb.BINOLMBRPT02">
<table cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
  <thead>
    <tr>
      <th><s:text name="mbrpt_regionName"></s:text></th>
      <th><s:text name="mbrpt_cityName"></s:text></th>
      <th><s:text name="mbrpt_totalBind"></s:text></th>
      <th><s:text name="mbrpt_newBind"></s:text></th>
      <th><s:text name="mbrpt_nb_tb_p"></s:text></th>
    </tr>
  </thead>
  <tbody>
    <s:iterator value="bindByCityList" id="bindByCityMap">
    <tr>
      <td><s:property value="regionName"/></td>
      <td><s:property value="cityName"/></td>
      <td><s:property value="totalBindCount"/></td>
      <td><s:property value="newBindCount"/></td>
      <td><s:property value="tb_nb_count"/></td>
    </tr>
    </s:iterator>
  </tbody>
</table>
</s:i18n>
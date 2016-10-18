<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>

<s:i18n name="i18n.mb.BINOLMBRPT02">
<table cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
  <thead>
    <tr>
      <th><s:text name="mbrpt_item"></s:text></th>
      <s:iterator value="dateList" id="dateMap">
      <th>
        <s:property value="name"/>
        <s:if test="%{#dateMap.type == 1}">
          <br/><s:property value="startDate.substring(5,10)"/>~<s:property value="endDate.substring(5,10)"/>
        </s:if>
      </th>
      </s:iterator>
    </tr>
  </thead>
  <tbody>
    <s:iterator value="totalGetList" id="totalGetMap">
    <tr>
      <td><s:property value="name"/></td>
      <s:iterator value="dateList" id="dateMap">
      	<td>${totalGetMap[dateMap.key]}</td>
      </s:iterator>
    </tr>
    </s:iterator>
  </tbody>
</table>
</s:i18n>
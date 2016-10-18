<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>

<s:i18n name="i18n.mb.BINOLMBRPT05">
<table cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
  <thead>
    <tr>
      <th>
      <s:if test='statisticsType != null && "1".equals(statisticsType)'>
      	<s:text name="RPT05_consumeTimes"></s:text>
      </s:if><s:elseif test='statisticsType != null && "2".equals(statisticsType)'>
      	<s:text name="RPT05_consumeAmount"></s:text>
      </s:elseif><s:elseif test='statisticsType != null && "3".equals(statisticsType)'>
      	<s:text name="RPT05_memberAge"></s:text>
      </s:elseif>
      </th>
      <th><s:text name="RPT05_regionMemNum"></s:text></th>
      <th><s:text name="RPT05_memberNumRate"></s:text></th>
      <th><s:text name="RPT05_regionMemConsumeAmount"></s:text></th>
      <th><s:text name="RPT05_consumeAmountRate"></s:text></th>
      <th><s:text name="RPT05_memberAverageConsume"></s:text></th>
    </tr>
  </thead>
  <tbody>
    <s:iterator value="memberConsumeRptList" id="memberConsumeRptMap">
    <tr>
	    <td class="<s:if test='"summary".equals(region)'>highlight</s:if><s:else>green</s:else>">
	    <s:if test='"summary".equals(region)'>
	    	<s:text name="RPT05_summary"></s:text>
	    </s:if><s:else>
	    	<s:property value="region"/>
	    </s:else>
	    </td>
	    <td class="<s:if test='"summary".equals(region)'>highlight</s:if><s:else>green</s:else>"><s:property value="regionMemNum"/></td>
	    <td class="<s:if test='"summary".equals(region)'>highlight</s:if><s:else>green</s:else>"><s:property value="memberNumRate"/></td>
	    <td class="<s:if test='"summary".equals(region)'>highlight</s:if><s:else>green</s:else>"><s:property value="regionMemConsumeAmount"/></td>
	    <td class="<s:if test='"summary".equals(region)'>highlight</s:if><s:else>green</s:else>"><s:property value="consumeAmountRate"/></td>
	    <td class="<s:if test='"summary".equals(region)'>highlight</s:if><s:else>green</s:else>"><s:property value="memberAverageConsume"/></td>
    </tr>
    </s:iterator>
  </tbody>
</table>
</s:i18n>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>


<s:i18n name="i18n.mb.BINOLMBMBM22">

<div class="detail_box">

<div class="section">
	<div class="section-header">
	    <strong>
	      <span class="ui-icon icon-ttl-section-info"></span>
	      <s:text name="问卷明细"></s:text>
	    </strong>
	</div>
   <div class="section-content">
     	<table class="detail" cellpadding="0" cellspacing="0">
       	<s:iterator value="memAnswerDetailList" id="memAnswerDetailMap" status="status">
        	<s:if test="%{#status.index%2 == 0}"><tr></s:if>
          		<th><s:property value="#memAnswerDetailMap.questionItem"/></th>
 				<td class="td_point"><span><s:property value="#memAnswerDetailMap.answer"/></span></td>
			<s:if test="%{#status.index%2 != 0}"></tr></s:if>  
      	</s:iterator>
    	</table>
  	</div>
</div>

</div>  
	  	
</s:i18n>
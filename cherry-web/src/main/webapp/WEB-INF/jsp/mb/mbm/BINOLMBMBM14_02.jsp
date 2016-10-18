<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  

<s:if test="%{memInfoRecordInfo != null && !memInfoRecordInfo.isEmpty()}">
<s:i18n name="i18n.mb.BINOLMBMBM14">
<div class="detail_box">
<div class="section">
  <div class="section-header">
    <strong><span class="ui-icon icon-ttl-section-info"></span><s:text name="binolmbmbm14_memInfoRecordInfo"/></strong>
  </div>
  <div class="section-content">
    <table class="detail" cellpadding="0" cellspacing="0">
      <tr>
        <th><s:text name="binolmbmbm14_memCode"/></th>
        <td><span><s:property value="memInfoRecordInfo.memCode"/></span></td>
        <th><s:text name="binolmbmbm14_modifyTime"/></th>
        <td><span><s:property value="memInfoRecordInfo.modifyTime"/></span></td>
   	  </tr>
   	  <tr>
	   	<th><s:text name="binolmbmbm14_modifyCounter"/></th>
        <td><span><s:property value="memInfoRecordInfo.modifyCounter"/></span></td>         
	   	<th><s:text name="binolmbmbm14_modifyType"/></th>
	    <td><span><s:property value='#application.CodeTable.getVal("1241", memInfoRecordInfo.modifyType)' /></span></td>
   	  </tr>
   	  <tr>
        <th><s:text name="binolmbmbm14_modifyEmployee"/></th>
        <td><span><s:property value="memInfoRecordInfo.modifyEmployee"/></span></td>	
        <th><s:text name="binolmbmbm14_batchNo"/></th>
        <td><span><s:property value="memInfoRecordInfo.batchNo"/></span></td>	
   	  </tr>
   	  <tr>
   	    <th><s:text name="binolmbmbm14_sourse"/></th>
        <td><span><s:property value="memInfoRecordInfo.sourse"/></span></td>
        <th><s:text name="binolmbmbm14_remark"/></th>
        <td><span><s:property value="memInfoRecordInfo.remark"/></span></td>
   	  </tr>
    </table>
  </div>
</div>  
  
<s:if test="%{memInfoRecordInfo.memInfoRecordDetail != null}">  
<div class="section">
  <div class="section-header">
    <strong><span class="ui-icon icon-ttl-section-search-result"></span><s:text name="binolmbmbm14_memInfoRecordDetail"/></strong>
  </div>  
  <div class="section-content">
    <table border="0" cellpadding="0" cellspacing="0" width="100%">
      <thead>
		  <tr>
			<th width="20%"><s:text name="binolmbmbm14_modifyField" /></th>
            <th width="40%"><s:text name="binolmbmbm14_oldValue" /></th>
            <th width="40%"><s:text name="binolmbmbm14_newValue" /></th>  
		  </tr>
      </thead>
      <tbody>
  	  <s:iterator value="%{memInfoRecordInfo.memInfoRecordDetail}" id="memInfoRecordDetailMap">
	      <tr>
	 	    <td><s:property value='#application.CodeTable.getVal("1242", #memInfoRecordDetailMap.modifyField)' /></td>
            <td><s:property value="#memInfoRecordDetailMap.oldValue" /></td>
            <td><s:property value="#memInfoRecordDetailMap.newValue" /></td>
		  </tr>
      </s:iterator>
	  </tbody>
    </table>
  </div>
</div>  
</s:if>
  
</div>
</s:i18n>
</s:if>



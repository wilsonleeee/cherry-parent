<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<div >
<center>
<br></br>
<strong><s:text name="global.page.workException"/></strong>
<br></br></center>
</div>

<div class="section-header">
   		<strong><span class="ui-icon icon-ttl-section-info"></span><s:text name="global.page.workdetail"/></strong>
</div>
<div >
<table width="100%" cellspacing="0" cellpadding="0">
<tbody>
  <tr>
	   <th width="15%">
	    <strong><s:text name="global.page.workTime"/></strong>
	   </th>
	   <th width="15%">
	    <strong><s:text name="global.page.workInfo"/></strong>
	   </th>
	   <th width="35%">
	    <strong><s:text name="global.page.workResult"/></strong>
	   </th>
	   <th width="15%">
	    <strong><s:text name="global.page.workPerson"/></strong>
	   </th>
	   <th width="20%">
	    <strong><s:text name="global.page.workDepart"/></strong>
	   </th>
  </tr>
  <s:iterator value="opLoglist" id="opLoglist">
    <tr>
       <td><s:property value="CreateTime"/></td><td><s:property value="OpCodeValue"/></td><td><s:property value="OpResultValue"/></td><td><s:property value="EmployeeName"/></td><td><s:property value="DepartName"/></td>
    </tr>
  </s:iterator>
</tbody>
</table>
</div>
  <hr class="space" />
<div class="center">
	<button class="close" onclick="window.close();">
		<span class="ui-icon icon-close"></span>
		<span class="button-text"><s:text name="global.page.close"/></span>
	</button>
</div>



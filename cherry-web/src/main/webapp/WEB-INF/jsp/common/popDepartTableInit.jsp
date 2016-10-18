<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<script type="text/javascript" src="/Cherry/js/common/popDataTable.js"></script>
<div id="departDialogInit" class="hide">
    <div id="departDialog_errorDisplay"></div>
    <span>
    <input type="text" class="text" onKeyup ="datatableFilter(this,9);" maxlength="50" id="departKw"/>
    <a class="search" onclick="datatableFilter('#departKw',9);return false;"><span class="ui-icon icon-search"></span><span class="button-text"><s:text name="global.page.searchfor"/></span></a>
  	</span>
  	<span style="margin-top:5px;">
  		<span><input type="radio" value="" name="validFlag9" id="validFlag_1" onclick ="changeValid(this,9);"/><label for="validFlag_1"><s:text name="global.page.all"/></label></span>
  		<span><input type="radio" value="1" name="validFlag9" id="validFlag_2" onclick ="changeValid(this,9);" checked="checked"/><label for="validFlag_2"><s:text name="global.page.valid"/></label></span>
  		<span><input type="radio" value="0" name="validFlag9" id="validFlag_3" onclick ="changeValid(this,9);"/><label for="validFlag_3"><s:text name="global.page.invalid"/></label></span>
  	</span>
  	<hr class="space" />
  		<table id="departDataTableInit" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
	       <thead>
	            <tr>
	               <th>
	               	   <s:if test='%{checkType == null || checkType == "checkbox"}'>
		               		<input type="checkbox" id="dep_checkAll"/>
		               </s:if>
		               <label for="dep_checkAll"><s:text name="global.page.Popselect"/></label>
	               </th>
	               <th><s:text name="global.page.departcode"/></th><%--代码 --%>
	               <th><s:text name="global.page.departname"/></th><%--名称 --%>
	               <th><s:text name="global.page.parttype"/></th><%--类型 --%>
	               <th>grade</th><%--grade --%>
	            </tr>
	        </thead>
	        <tbody>
	        </tbody>
	   </table>
	   <div class="hide" id="departDialogInit_temp"></div>
	   <span id ="global_page_ok" style="display:none"><s:text name="global.page.ok"/></span>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />

<s:url action="BINOLCM02_popDepart" namespace="/common" id="popDepart" />
<span id ="popDepartUrlInit" style="display:none">${popDepart}</span>
<span id ="PopdepartTitleInit" style="display:none"><s:text name="global.page.PopdepartTitle"/></span><%--部门信息 --%>
<div id ="departDialogInit_error" style="display:none"><div class="actionError"><ul><li><s:text name="global.page.selectdepart"/></li></ul></div></div>
</div>

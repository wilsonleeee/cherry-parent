<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<s:url id="s_MemSearchUrl" value="/common/BINOLCM02_popMemberDialog" />
<script type="text/javascript" src="/Cherry/js/common/popDataTable.js"></script>
<s:i18n name="">
<div id ="memberDialog" class="dialog hide">
	<input type="text" class="text" value="" id="memberDialogSearch" 
		onKeyup ="datatableFilter(this,33);" maxlength="50"/>
    <a class="search" onclick="datatableFilter('#memberDialogSearch',33)">
    	<span class="ui-icon icon-search"></span>
    	<span class="button-text"><s:text name="global.page.searchfor"/></span>
    </a>
  	<hr class="space" />
  	<table id="mem_dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table2" width="100%">
       <thead>
            <tr>
               <th>
               <s:if test='%{checkType == null || checkType == "checkbox"}'>
               		<input type="checkbox" id="mem_checkAll"/>
               </s:if>
               	<label for="mem_checkAll"><s:text name="global.page.Popselect"/></label>
               </th>         <%-- 选择 --%>
               <th><s:text name="global.page.memCode"/></th>     <%--会员卡号--%>
               <th><s:text name="global.page.memName"/></th>     <%--会员名称--%>
               <th><s:text name="global.page.memPhone"/></th>    <%-- 会员手机号--%>
               <th><s:text name="global.page.memDepart"/></th>   <%-- 所属部门--%>
            </tr>
        </thead>
        <tbody id="dataTableBody"></tbody>
   	</table>
   	<table class="hide"><tbody id="memberDialog_temp"></tbody></table>
   	<span id ="memSearchUrl" style="display:none">${s_MemSearchUrl}</span>
   	<span id ="global_page_ok" style="display:none"><s:text name="global.page.ok"/></span>
   	<span id ="PopMemTitle" style="display:none"><s:text name="global.page.PopMemTitle"/></span><%--会员信息 --%>
</div>
<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>
</s:i18n>
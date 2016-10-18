<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<script type="text/javascript" src="/Cherry/js/common/popDataTable.js"></script>
<s:url id="prmCateSearchUrl" value="/common/BINOLCM02_popPrmCateDialog" />
<s:i18n name="">
<div id ="prmCategoryDialog" class="dialog hide">
	<input class="text" id="prmCategoryDialogKey" onKeyup ="datatableFilter(this,21);" maxlength="50"/>
    <a class="search" onclick="datatableFilter('#prmCategoryDialogKey',21)">
    	<span class="ui-icon icon-search"></span><span class="button-text"><s:text name="global.page.searchfor"/></span>
    </a>
  	<hr class="space" />
  	<s:if test='%{param == null || "".equals(param)}'>
  	<div class="ui-tabs">
	  	<ul class ="ui-tabs-nav clearfix" id="prmCate_Title">
		    <li id='1' class='ui-tabs-selected' onclick="changeTab(this,21);return false;"><a><s:text name="global.page.PrimaryCategory"/></a></li>
		    <li id='2' class='ui-tabs-selected' onclick="changeTab(this,21);return false;"><a><s:text name="global.page.SecondryCategory"/></a></li>
		    <li id='3' class='ui-tabs-selected' onclick="changeTab(this,21);return false;"><a><s:text name="global.page.SmallCategory"/></a></li>
	 	</ul>
		<div class="ui-tabs-panel">
	</s:if>	
			<table id="prmCate_Table" cellpadding="0" cellspacing="0" border="0" class="jquery_table2" width="100%">
		       <thead>
		           <tr>
		              <th>
						<s:if test='%{checkType == null || checkType == "checkbox"}'>
		               		<input type="checkbox" id="prmCate_checkAll"/>
		              	</s:if>
               		  	<label for="prmCate_checkAll"><s:text name="global.page.Popselect"/></label>
		              </th>         <%-- 选择 --%>
		              <th><s:text name="global.page.cateVal"/></th>     <%-- 分类码 --%>
		              <th><s:text name="global.page.cateValName"/></th>           <%-- 分类名 --%>
		           </tr>
		        </thead>
		        <tbody id="prmCate_Body"></tbody>
		   </table>
	<s:if test='%{param == null || "".equals(param)}'>   
		</div>
   </div>
   </s:if>
   <div class="hide" id="prmCategoryDialog_temp"></div>
   <span id ="prmCateSearchUrl" style="display:none">${prmCateSearchUrl}</span>
   <span id ="global_page_ok" style="display:none"><s:text name="global.page.ok"/></span>
   <span id ="PopPrmCateTitle" style="display:none"><s:text name="global.page.PopPrmCateTitle"/></span><%--促销品分类信息 --%>
</div>
<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>
</s:i18n>
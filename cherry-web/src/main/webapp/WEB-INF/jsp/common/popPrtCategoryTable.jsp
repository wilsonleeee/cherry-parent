<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<script type="text/javascript" src="/Cherry/js/common/popDataTable.js"></script>
<s:url id="prtCateSearchUrl" value="/common/BINOLCM02_popPrtCateDialog" />
<s:i18n name="">
<div id ="prtCategoryDialog" class="dialog hide">
	<input class="text" id="prtCategoryDialogKey" name="key" onKeyup ="datatableFilter(this,20);" maxlength="50"/>
    <a class="search" onclick="datatableFilter('#prtCategoryDialogKey',20)">
    	<span class="ui-icon icon-search"></span><span class="button-text"><s:text name="global.page.searchfor"/></span>
    </a>
    <s:if test='%{param == null || "".equals(param)}'>
    <s:if test="isPosCloud==0">
    <span style="margin-top:5px;">
  		<span><input type="radio" value="0" name="radio20" id="radio_0" onclick ="changeValid(this,20);"/><label for="radio_0"><s:text name="global.page.all"/></label></span>
  		<span><input type="radio" value="1" name="radio20" id="radio_1" onclick ="changeValid(this,20);" checked="checked"/><label for="radio_1"><s:text name="global.page.isBand"/></label></span>
  		<span><input type="radio" value="2" name="radio20" id="radio_2" onclick ="changeValid(this,20);"/><label for="radio_2"><s:text name="global.page.isNotBand"/></label></span>
  	</span>
  	</s:if>
  	</s:if>
  	<s:else><input type="radio" name="radio20" value="<s:property value='param'/>" checked="checked" class="hide"/></s:else>
  	<hr class="space" />
  	<s:if test='%{prtCategoryList != null && !prtCategoryList.isEmpty()}'>
    <div class="ui-tabs"> 
	    <ul class ="ui-tabs-nav clearfix" id="promotionCateTitle">
		    <s:iterator value="prtCategoryList" status="status">
		    	<li id='<s:property value="cateId"/>' class='<s:if test="%{#status.index == 0}">ui-tabs-selected</s:if>' onclick="changeTab(this,20,'param1');return false;">
		    		<a><s:property value="cateName"/></a>
		    	</li>
		    </s:iterator>
	 	</ul>
		<div class="ui-tabs-panel">
	</s:if>
	<table id="prtCate_Table" cellpadding="0" cellspacing="0" border="0" class="jquery_table2" width="100%">
       <thead>
           <tr>
              <th>
              <s:if test='%{checkType == null || checkType == "checkbox"}'>
               	<input type="checkbox" id="prtCate_checkAll"/>
              </s:if>
             		  <label for="prtCate_checkAll"><s:text name="global.page.Popselect"/></label>
              </th>         <%-- 选择 --%>
              <th><s:text name="global.page.cateVal"/></th>     <%-- 分类码 --%>
              <th><s:text name="global.page.cateValName"/></th>           <%-- 分类名 --%>
           </tr>
        </thead>
        <tbody id="prtCate_Body"></tbody>
   </table>
   <s:if test='%{prtCategoryList != null && !prtCategoryList.isEmpty()}'>
	   </div>
   </div>
   </s:if>
   <div class="hide" id="prtCategoryDialog_temp"></div>
   <span id ="prtCateSearchUrl" style="display:none">${prtCateSearchUrl}</span>
   <span id ="global_page_ok" style="display:none"><s:text name="global.page.ok"/></span>
   <span id ="PopPrtCateTitle" style="display:none"><s:text name="global.page.PopPrtCateTitle"/></span><%--产品分类信息 --%>
</div>
<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>
</s:i18n>
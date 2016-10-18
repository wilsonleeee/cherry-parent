<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<s:url id="s_actGropSearchUrl" value="/ss/BINOLSSPRM13_actGrpDialog" />
<s:url id="s_actGropDeleteUrl" value="/ss/BINOLSSPRM13_actGropDelete" />
<s:url id="s_actGropEditUrl" value="/ss/BINOLSSPRM13_actGropEdit" />
<script type="text/javascript" src="/Cherry/js/common/popDataTable.js"></script>
<s:i18n name="">
<div id ="actGrpDialog" class="dialog hide">
	<%-- ================== 错误信息提示 START ======================= --%>
    <div id="errorDiv" class="actionError" style="display:none">
        <ul><li><span id="errorSpan"></span></li></ul>         
    </div>
    <%-- ================== 错误信息提示   END  ======================= --%>
	<input type="text" class="text" value="" id="productDialogSearch" 
		onKeyup ="datatableFilter(this,24);" maxlength="50"/>
    <a class="search" onclick="datatableFilter('#productDialogSearch',24)">
    	<span class="ui-icon icon-search"></span>
    	<span class="button-text"><s:text name="global.page.searchfor"/></span>
    </a>
  	<hr class="space" />
  	<table id="actGrpTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table2" width="100%">
       <thead>
            <tr>
               <th style="width:3%">
               <s:if test='%{checkType == null || checkType == "checkbox"}'>
               		<input type="checkbox" id="prt_checkAll"/>
               </s:if>
               	<%--<label for="prt_checkAll"><s:text name="global.page.Popselect"/></label>--%>
               </th>         <%-- 选择 --%>
               <th><s:text name="global.page.groupcode"/></th><%-- 主活动code--%>
               <th><s:text name="global.page.groupname"/></th>          <%-- 主活动名称--%>
               <th><s:text name="global.page.activitytype"/></th>       <%-- 主活动类型--%>
               <th><s:text name="global.page.activityBeginDate"/></th>       <%-- 主活动领用开始时间--%>
               <th><s:text name="global.page.activityEndDate"/></th>       <%-- 主活动领用结束时间--%>
               <th><s:text name="os.navigation.operator"/></th>						<%--操作--%>
            </tr>
        </thead>
        <tbody id="dataTableBody"></tbody>
   	</table>
   	<table class="hide"><tbody id="actGrpDialog_temp"></tbody></table>
   	<span id ="actGropEditUrl" style="display:none">${s_actGropEditUrl}</span>
   	<span id ="actGropSearchUrl" style="display:none">${s_actGropSearchUrl}</span>
   	<span id ="actGropDeleteUrl" style="display:none">${s_actGropDeleteUrl}</span>
   	<span id ="global_page_cancle" style="display:none"><s:text name="global.page.cancle"/></span>
   	<span id ="global_page_delete" style="display:none"><s:text name="global.page.delete"/></span>
   	<span id ="global_page_actTitle" style="display:none"><s:text name="global.page.actTitle"/></span><%--产品信息 --%>

    <input type="hidden" id="errmsg" value='<s:text name="EMO00069"/>'/>
    <input type="hidden" id="editerrmessage" value='<s:text name="EMO00070"/>'/>
    <input type="hidden" id="editerrmessagename" value='<s:text name="EMO00070"/>'/>

</div>
<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>
</s:i18n>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<script type="text/javascript" src="/Cherry/js/wp/ws/mng/BINOLWSMNG05_05.js"></script>
<div id="allocationDialog" class="hide">
    <input type="text" class="text" onKeyup ="datatableFilter(this,31);" maxlength="50" id="deliverKw"/>
    <a class="search" onclick="datatableFilter('#deliverKw',31);return false;"><span class="ui-icon icon-search"></span><span class="button-text"><s:text name="global.page.searchfor"/></span></a>
  	<span style="margin-top:5px;" class="hide">
  		<input id="checkboxType" type="checkbox" name="checkboxType" class="checkbox" />
  	</span>
  	<hr class="space" />
    <table id="allocationDataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
        <thead>
            <tr>
                <th><s:text name="global.page.number"/></th><%--序号 --%>
                <th><s:text name="global.page.billCode"/></th><%--单号 --%>
                <th><s:text name="global.page.productAllocationInDepart"/></th><%--调入部门--%>
                <th><s:text name="global.page.sumQuantity"/></th><%--总数量--%>
                <th><s:text name="global.page.sumAmount"/></th><%--总金额 --%>
            </tr>
        </thead>
        <tbody>
        </tbody>
    </table>
</div>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />

<s:url action="BINOLWSMNG05_getProductAllocation" namespace="/ws" id="popProductAllocation" />
<span id ="popProductAllocationUrl" style="display:none">${popProductAllocation}</span>
<span id ="global_page_ok" style="display:none"><s:text name="global.page.ok"/></span>
<span id ="global_page_close" style="display:none"><s:text name="global.page.close"/></span>
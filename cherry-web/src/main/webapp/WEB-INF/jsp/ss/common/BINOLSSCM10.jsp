<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<script type="text/javascript" src="/Cherry/js/ss/common/BINOLSSCM10.js"></script>
<div id="inDepotDialog" class="hide">
    <input type="text" class="text" onKeyup ="datatableFilter(this,31);" maxlength="50" id="deliverKw"/>
    <a class="search" onclick="datatableFilter('#deliverKw',31);return false;"><span class="ui-icon icon-search"></span><span class="button-text"><s:text name="global.page.searchfor"/></span></a>
  	<span style="margin-top:5px;">
  		<input id="checkboxType" type="checkbox" name="checkboxType" class="checkbox" />
    	<span><s:text name="global.page.messageTip5"/><span class="red"><s:text name="global.page.messageTip3"/></span><s:text name="global.page.messageTip4"/></span>
  	</span>
  	<hr class="space" />
    <table id="inDepotDataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
        <thead>
            <tr>
                <th><s:text name="global.page.Popselect"/></th><%--选择 --%>
                <th><s:text name="global.page.inDepotNo"/></th><%--入库单号 --%>
                <th><s:text name="global.page.inDepotDepart"/></th><%--入库部门 --%>
                <th><s:text name="global.page.sumQuantity"/></th><%--总数量--%>
                <th><s:text name="global.page.sumAmount"/></th><%--总金额 --%>
            </tr>
        </thead>
        <tbody>
        </tbody>
    </table>
</div>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />

<s:url action="BINOLSSCM10_getInDepot" namespace="/ss" id="popInDepot" />
<span id ="popInDepotUrl" style="display:none">${popInDepot}</span>
<span id ="global_page_ok" style="display:none"><s:text name="global.page.ok"/></span>
<span id ="InDepotBillTitle" style="display:none"><s:text name="global.page.InDepotBillTitle"/></span><%--入库单信息 --%>
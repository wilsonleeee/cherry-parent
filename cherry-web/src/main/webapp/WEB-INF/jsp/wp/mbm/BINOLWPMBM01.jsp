<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<style type="text/css">
.memReport table {
	border: none;
}
.memReport td {
	height: auto;
	line-height: 18px;
	border: none;
	font-size:8pt;
	cursor: auto;
}
</style>
<s:i18n name="i18n.wp.BINOLWPMBM01">
<div id="memInitDiv">
<div class="crm_top clearfix">
	<div><s:text name="WPMBM01_title" /></div>
</div>
<div class="wp_navbuttonbox">
	<button class="btn_top" id="doSale"><s:text name="WPMBM01_F1" /></button>
	<button class="btn_top" id="addMem"><s:text name="WPMBM01_F2" /></button>
	<button class="btn_top_disable" disabled="disabled" id="updMem"><s:text name="WPMBM01_F3" /></button>
	<button class="btn_top_disable" disabled="disabled" id="saleList"><s:text name="WPMBM01_F4" /></button>
	<button class="btn_top_disable" disabled="disabled" id="pointList"><s:text name="WPMBM01_F5" /></button>
	<button class="btn_top_disable" disabled="disabled" id="detailMem"><s:text name="WPMBM01_F6" /></button>
</div>
<div class="wp_main">
    <div class="wp_left">
        <div class="wp_content" style="margin-right: 0;padding-right: 0.5em">
        	<div class="wp_leftbox">
            	<form id="queryParam">
            	<div class="wpleft_header">
					<div class="header_box">
               	   	  <s:text name="WPMBM01_mobilePhoneQ" />
               	   	  <s:textfield name="mobilePhoneQ" cssClass="text" cssStyle="width:120px;" id="mobilePhoneQ"/>
               	   	</div>
               	   	<div class="header_box">
               	   	  <s:text name="WPMBM01_memCodeQ" />
               	   	  <s:textfield name="memCodeQ" cssClass="text" cssStyle="width:120px;" id="memCodeQ"/>
               	   	</div>
                   	<div class="header_box">
                   	  <s:text name="WPMBM01_memNameQ" />
                   	  <s:textfield name="memNameQ" cssClass="text" cssStyle="width:120px;" id="memNameQ"/>
                   	</div>
                   	<div class="header_box">
                      <s:text name="WPMBM01_birthDayQ" />
                      <select name="birthDayMonthQ" style="width:50px;" id="birthDayMonthQ">
                        <option value=""><s:text name="WPMBM01_birthDayMonthQ" /></option>
                      </select>
                      <select name="birthDayDateQ" style="width:50px;" id="birthDayDateQ">
                        <option value=""><s:text name="WPMBM01_birthDayDateQ" /></option>
                      </select>
                   	</div>
               	   	<div class="header_box"><button class="wp_search" id="search"></button></div>
               	   	<div class="header_box" id="validateError" style="display: none;color: red"></div>
                </div>
                </form>
            	<div class="wp_tablebox">
                    <table width="100%" cellspacing="0" cellpadding="0" border="0" class="wp_table memEventDataTable" id="result" style="display: none;">
                    <thead>
                      <tr>
                        <th><s:text name="WPMBM01_radio" /></th>
                        <th><s:text name="WPMBM01_mobilePhone" /></th>
                        <th><s:text name="WPMBM01_memCode" /></th>
                        <th><s:text name="WPMBM01_memName" /></th>
                        <th><s:text name="WPMBM01_birthDay" /></th>
                        <th><s:text name="WPMBM01_levelName" /></th>
                        <th><s:text name="WPMBM01_joinDate" /></th>
                        <th><s:text name="WPMBM01_changablePoint" /></th>
                        <th><s:text name="WPMBM01_changeDate" /></th>
                        <th><s:text name="WPMBM01_totalAmount" /></th>
                        <th><s:text name="WPMBM01_amount" /></th>
                        <th><s:text name="WPMBM01_couponCount" /></th>
                      </tr>
                    </thead>
                    <tbody></tbody>
                    </table>
                </div>
            </div>
        </div>
    
    </div>
</div>

<s:a action="BINOLWPMBM01_search" id="searchMemUrl" cssStyle="display: none;"></s:a>
<s:a action="BINOLWPSAL02_search" id="doSaleUrl" cssStyle="display: none;"></s:a>
<s:a action="BINOLWPMBM01_addInit" id="addInitUrl" cssStyle="display: none;"></s:a>
<s:a action="BINOLWPMBM01_updateInit" id="updateInitUrl" cssStyle="display: none;"></s:a>
<s:a action="BINOLWPMBM01_saleInit" id="saleInitUrl" cssStyle="display: none;"></s:a>
<s:a action="BINOLWPMBM01_pointInit" id="pointInitUrl" cssStyle="display: none;"></s:a>
<s:a action="BINOLWPMBM01_detail" id="detailUrl" cssStyle="display: none;"></s:a>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />

<script type="text/javascript">
$(function(){
	binolwpmbm01.memInit();
});
</script>

</div>
</s:i18n>
<div id="memOtherDiv" style="display: none;"></div>
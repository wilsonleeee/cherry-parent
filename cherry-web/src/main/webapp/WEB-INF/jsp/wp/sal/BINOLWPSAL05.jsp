<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/wp/sal/BINOLWPSAL05.js"></script>
<s:i18n name="i18n.wp.BINOLWPSAL05">
<div class="hide">
	<s:url id="s_dgSaleUrl" value="/wp/BINOLWPSAL02_init" />
	<a id="dgSaleUrl" href="${s_dgSaleUrl}"></a>
	<s:url id="s_dgHangBillUrl" value="/wp/BINOLWPSAL05_hangBill" />
	<a id="dgHangBillUrl" href="${s_dgHangBillUrl}"></a>
</div>
<form id="hangBillsForm" method="post" class="inline">
<div id="hangBillsPageDiv" class="hide ui-dialog-content ui-widget-content" style="display: block; width: auto; min-height: 300px;">
	<input type="hidden" id="dgOpenedState" name="dgOpenedState" value="Y"/>
	<input type="hidden" id="dgCounterCode" name="dgCounterCode" value="<s:property value='counterCode'/>"/>
    <div class="wpleft_header">
    <div class="header_box"><s:text name="wpsal05.baName" /><span class="top_detail2"><s:property value="baName"/></span> </div>
    <div class="header_box"><s:text name="wpsal05.memberName" /><span class="top_detail2"><s:property value="memberName"/></span> </div>
    </div>
    <div class="wp_tablebox">
    	<div class="td_detailbox"><s:text name="wpsal05.hangBillsMessage" /></div>
    </div>
    <div class="bottom_butbox clearfix">
    	<button id="btnConfirm" class="close" type="button" onclick="BINOLWPSAL05.confirm();return false;">
    		<span class="ui-icon icon-confirm"></span>
			<span class="button-text"><s:text name="wpsal05.confirm"/></span>
		</button>
		<button id="btnCancel" class="close" type="button" onclick="BINOLWPSAL05.cancel();return false;">
			<span class="ui-icon icon-close"></span>
			<span class="button-text"><s:text name="wpsal05.cancel"/></span>
		</button>
    </div>
</div>
</form>
</s:i18n>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/wp/sal/BINOLWPSAL12.js?V=20161014"></script>
<s:i18n name="i18n.wp.BINOLWPSAL12">
<div class="hide">
	<s:url id="s_getPayResultUrl" value="/wp/BINOLWPSAL12_getPayResult" />
	<a id="getPayResultUrl" href="${s_getPayResultUrl}"></a>
</div>
<div id="getPayResultPageDiv" class="ui-dialog-content ui-widget-content" style="width: auto; min-height: 300px;">
	<div id="getPayResultDiv" class="wp_tablebox">
	  <form id="payResultForm" method="post" class="inline">
		  <input type="hidden" id="payType" name="payType" value="<s:property value='payType'/>"/>
		  <input type="hidden" id="payBillCode" name="payBillCode" value="<s:property value='payBillCode'/>"/>
		  <input type="hidden" id="payBillTime" name="payBillTime" value="<s:property value='payBillTime'/>"/>
		  <input type="hidden" id="payMemberCode" name="payMemberCode" value="<s:property value='payMemberCode'/>"/>
		  <input type="hidden" id="payMemberName" name="payMemberName" value="<s:property value='payMemberName'/>"/>
		  <input type="hidden" id="payAmount" name="payAmount" value="<s:property value='payAmount'/>"/>
		  <input type="hidden" id="payCounterCode" name="payCounterCode" value="<s:property value='payCounterCode'/>"/>
	  </form>
	  <table id="getPayResultTable" width="100%" cellspacing="0" cellpadding="0" border="0" class="wp_table">
	    <thead>
	      <tr>
	        <th width="25%"><s:text name="wpsal12.billCode"/></th>
	        <th width="12%"><s:text name="wpsal12.businessDate"/></th>
	        <th width="10%"><s:text name="wpsal12.memberCode"/></th>
	        <th width="8%"><s:text name="wpsal12.memberName"/></th>
	        <th width="8%"><s:text name="wpsal12.payAmount"/></th>
	        <th width="8%"><s:text name="wpsal12.payState"/></th>
	        <th><s:text name="wpsal12.payMessage"/></th>
	      </tr>
	    </thead>
	    <tbody id="getPayResultbody">
	    </tbody>
	  </table>
	</div>
	<div class="bottom_butbox clearfix">
		<button id="btnRefresh" class="hide" type="button" onclick="BINOLWPSAL12.getPayResult();return false;">
			<span class="ui-icon icon-refresh-s"></span>
			<span class="button-text"><s:text name="wpsal12.refresh" /></span>
		</button>
		<button id="btnPaySuccess" class="hide" type="button" onclick="BINOLWPSAL12.paySuccess();return false;">
    		<span class="ui-icon icon-confirm"></span>
			<span class="button-text"><s:text name="wpsal12.paySuccess"/></span>
		</button>
    	<button id="btnPayFail" class="hide" type="button" onclick="BINOLWPSAL12.payFail();return false;">
    		<span class="ui-icon icon-back"></span>
			<span class="button-text"><s:text name="wpsal12.payFail"/></span>
		</button>
    </div>
</div>
</s:i18n>

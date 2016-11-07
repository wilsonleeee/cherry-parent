<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/wp/sal/BINOLWPSAL06.js?V=20161107"></script>
<s:i18n name="i18n.wp.BINOLWPSAL06">
<div class="hide">
	<s:url id="s_dgSearchUrl" value="/wp/BINOLWPSAL06_search" />
	<a id="dgSearchUrl" href="${s_dgSearchUrl}"></a>
	<s:url id="s_dgGetBillUrl" value="/wp/BINOLWPSAL06_getBill" />
	<a id="dgGetBillUrl" href="${s_dgGetBillUrl}"></a>
	<s:url id="s_sendMQCollect" value="/wp/BINOLWPSAL03_sendMQCollect" />
	<a id="dgSendMQCollect" href="${s_sendMQCollect}"></a>
    <s:url id="s_getPayResultBySendMQ" value="/wp/BINOLWPSAL12_getPayResultBySendMQ" />
    <a id="getPayResultBySendMQ" href="${s_getPayResultBySendMQ}"></a>
</div>
<div id="getBillsPageDiv" class="hide ui-dialog-content ui-widget-content" style="display: block; width: auto; min-height: 300px;">
    <div class="wpleft_header">
    <input type="hidden" id="dgOpenedState" name="dgOpenedState" value="Y"/>
    <form id="getBillsForm" method="post" class="inline">
    	<input type="hidden" id="dgCounterCode" name="dgCounterCode" value="<s:property value='counterCode'/>"/>
	    <div class="header_box"><s:text name="wpsal06.baName" />：<span class="top_detail2"><s:property value="baName"/></span> </div>
	    <div class="header_box"><s:text name="wpsal06.memberName" /><span class="top_detail2"><s:property value="memberName"/></span></div>
	    <div><span style="display:-moz-inline-box; display:inline-block; width:100px;"><s:text name="wpsal06.hangBillCode"/></span><span class="top_detail2">
	               	<input name="hangBillCode" class="text" style="width:195px">
	              </span></div>
	    <div><span style="display:-moz-inline-box; display:inline-block; width:100px;"><s:text name="wpsal06.showReturnDataTitle"/></span><span class="top_detail2">
	               	<select id="retryDataFlag" name="retryDataFlag">
	               		<option value="0" selected="selected"><s:text name="wpsal06.noTitle"/></option>
	               		<option value="1"><s:text name="wpsal06.yesTitle"/></option>
	               	</select>
	              </span></div>
	    <div><span style=" display:-moz-inline-box; display:inline-block; width:100px;"><s:text name="wpsal06.businessDate"/></span><span class="top_detail2">
	               	<s:textfield name="billHangDateStart" cssClass="date" cssStyle="width:80px"/>-<s:textfield name="billHangDateEnd" cssClass="date" cssStyle="width:80px"/>
	              </span></div>
        <p class="clearfix">
         	<button class="right search" id="searchButton" onclick="BINOLWPSAL06.search();return false;">
         		<span class="ui-icon icon-search-big"></span><span class="button-text"><s:text name="global.page.search"/></span>
         	</button>	
          </p>
    </form>
    </div>
    <div class="wp_tablebox">
      <table id="unfinishedBillsTable" width="100%" cellspacing="0" cellpadding="0" border="0" class="wp_table">
        <thead>
          <tr>
            <th width="5%"><s:text name="wpsal06.rowNumber"/></th>
            <th width="15%"><s:text name="wpsal06.billCode"/></th>
            <th width="20%"><s:text name="wpsal06.businessDate"/></th>
            <th width="20%"><s:text name="wpsal06.hangTime"/></th>
            <th width="20%"><s:text name="wpsal06.memberCode"/></th>
            <th width="10%"><s:text name="wpsal06.baName"/></th>
            <th width="8%"><s:text name="wpsal06.quantity"/></th>
            <th width="12%"><s:text name="wpsal06.amount"/></th>
            <th width="10%"><s:text name="wpsal06.act"/></th>
          </tr>
        </thead>
        <tbody id="unfinishedBillsbody">
        </tbody>
      </table>
    </div>
    <div class="bottom_butbox clearfix">
    	<button id="btnCancel" class="close" type="button" onclick="BINOLWPSAL06.cancel();return false;">
    		<span class="ui-icon icon-close"></span>
            <span class="button-text"><s:text name="wpsal06.cancel"/></span>
		</button>
    </div>
</div>
</s:i18n>
<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>
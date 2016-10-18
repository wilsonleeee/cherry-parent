<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/mb/svc/BINOLMBSVC03.js?v=20160721"></script>
<s:i18n name="i18n.mb.BINOLMBSVC03">
<div class="hide">
		<!-- 获取储值卡信息 -->
		<s:url id="s_cardDetailRechargeInit" value="/mb/BINOLMBSVC03_cardRechargeDetailInit" />
		<a id="cardDetailRechargeInitUrl" href="${s_cardDetailRechargeInit}" ></a>
		<!-- 检验卡号的有效性 -->
		<s:url id="s_checkCard" value="/mb/BINOLMBSVC03_checkCard" />
		<a id="checkCardUrl" href="${s_checkCard}" ></a>
		<div id="rechargeDialogTitle"><s:text name="SVC03_inputRecharge"></s:text> </div>
</div>
<form  class="inline" id="cardDetailRechargeForm">
    <div class="wp_tablebox">
        <table width="100%" cellspacing="0" cellpadding="0" border="0" class="wp_detail_s">
            <tbody>
            	<tr>
                    <th><s:text name="柜台号" /></th>
                    <td><span><input class="text" id="counterName" name="counterName" /></span></td>
                </tr>
                <tr>
                    <th><s:text name="SVC03_inputCardCode" /></th>
                    <td><span><input class="text" id="dgCardCode" name="cardCode" maxlength="11"/></span></td>
                </tr>
            </tbody>
        </table>
    </div>
    <div class="center clearfix">
	    	<button id="btnConfirm" class="close" type="button" onclick="BINOLMBSVC03.confirm();return false;">
	    		<span class="ui-icon icon-confirm"></span>
	            <span class="button-text"><s:text name="SVC03_confirm"/></span>
			</button>
			<button id="btnCancel" class="close" type="button" onclick="BINOLMBSVC03.cancel();return false;">
				<span class="ui-icon icon-close"></span>
	            <span class="button-text"><s:text name="SVC03_cancel"/></span>
			</button>
    </div>
</form>
</s:i18n>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/wp/sal/BINOLWPSAL13.js"></script>
<s:i18n name="i18n.wp.BINOLWPSAL13">
<div class="hide">
	<%--充值 --%>
	<s:url id="s_createCard" value="/wp/BINOLWPSAL13_createCard" />
	<a id="createCard" href="${s_createCard}"></a>
</div>
<form id="openCardForm" method="post" class="inline">
    <div class="wp_tablebox">
        <table width="100%" cellspacing="0" cellpadding="0" border="0" class="wp_detail_s">
            <tbody>
                <tr>
                    <th><s:text name="wpsal13.printCardCode" /></th>
                    <td><span><input class="text" id="cardCode" name="cardCode" readonly="readonly" value="<s:property value='memCode'/>"/><span class="heightlight">*</span></span></td>
                </tr>
                 <%-- <tr>
                    <th><s:text name="wpsal13.choiceCardType" /></th>
                    <td><span><select id="cardType" name="cardType" onchange="BINOLWPSAL13.choice(this.value)">
                    	<option value="1">储值卡</option>
                    	<option value="2">体验卡</option>
                    </select></span></td>
                </tr> --%>
                <tr>
                    <th><s:text name="wpsal13.reserveMobilePhone" /></th>
                    <td><span><input type="text" class="text" id="MPhone" name="mobilePhone" readonly="readonly" value="<s:property value='mobilePhone'/>"/><span class="heightlight">*</span></span></td>
                </tr>
                <tr>
                    <th><s:text name="wpsal13.memberCardCode" /></th>
                    <td><span><input class="text" id="memCode" name="memCode" readonly="readonly" value="<s:property value='memCode'/>"/></span></td>
                </tr>
                <tr>
                    <th><s:text name="wpsal13.cardPassWord" /></th>
                    <td><span><input onblur="BINOLWPSAL13.password();return false;" type="password" class="text" id="ps1" name="password"/><span class="heightlight">*</span></span></td>
                </tr>
                <tr>
                    <th><s:text name="wpsal13.confirmCardPassWord" /></th>
                    <td><span><input onblur="BINOLWPSAL13.password();return false;" type="password" class="text" id="ps2" name="ps2"/><span class="heightlight">*</span></span></td>
                </tr>
                <tr>
                    <th><s:text name="wpsal13.rechargeamount" /></th>
                    <td><span><input class="text" id="camount" name="camount"/></span></td>
                </tr>
                <tr>
                    <th><s:text name="wpsal13.giftAmount" /></th>
                    <td><span><input class="text" id="giveAmount" name="giveAmount"/></span></td>
                </tr>
                <tr>
                    <th><s:text name="wpsal13.comments" /></th>
                    <td><span><textarea id="memo" name="memo" style="width: 95%; height:30px;" maxlength="95"></textarea></span></td>
                </tr>
        </table>
    </div>
    <div class="bottom_butbox clearfix">
    	<button id="btnConfirm" class="close" type="button" onclick="BINOLWPSAL13.createCard();return false;">
    		<span class="ui-icon icon-confirm"></span>
            <span class="button-text"><s:text name="wpsal13.confirm"/></span>
		</button>
		<button id="btnCancel" class="close" type="button" onclick="BINOLWPSAL13.cancel();return false;">
			<span class="ui-icon icon-close"></span>
            <span class="button-text"><s:text name="wpsal13.cancel"/></span>
		</button>
    </div>
</form>
</s:i18n>
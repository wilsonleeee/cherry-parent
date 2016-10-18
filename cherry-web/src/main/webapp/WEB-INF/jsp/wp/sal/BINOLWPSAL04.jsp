<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/wp/sal/BINOLWPSAL04.js"></script>
<s:i18n name="i18n.wp.BINOLWPSAL04">
<form id="discountForm" method="post" class="inline">
<input type="hidden" id="dgOpenedState" name="dgOpenedState" value="Y"/>
<input type="hidden" id="roundType" name="roundType" value="1"/>
<input type="hidden" id="roundDigit" name="roundDigit" value="0"/>
<input type="hidden" id="discountName" name="discountName" value='<s:text name="wpsal04.discountName"/>'/>
<input type="hidden" id="roundingName" name="roundingName" value='<s:text name="wpsal04.roundingName"/>'/>
<div id="discountPageDiv" class="hide ui-dialog-content ui-widget-content" style="display: block; width: auto; min-height: 200px;">
    <div class="wpleft_header">
        <div class="header_box"><s:text name="wpsal04.baName" /><span class="top_detail2"><s:property value="baName"/></span></div>
        <div class="header_box"><s:text name="wpsal04.memberName" /><span class="top_detail2"><s:property value="memberName"/></span></div>
    </div>
    <div class="wp_tablebox">
        <table width="100%" cellspacing="0" cellpadding="0" border="0" class="wp_detail_s">
            <tbody>
                <tr>
                    <th><s:text name="wpsal04.originalAmount" /></th>
                    <td><s:property value="originalAmount"/><input id="dgOriginalAmount" name="dgOriginalAmount" type="hidden" value="<s:property value='originalAmount'/>" /></td>
                </tr>
                <tr>
                    <th><s:text name="wpsal04.totalDiscountRate" /></th>
                    <td><input id="dgTotalDiscountRate" name="dgTotalDiscountRate" type="text" class="wp_input2" maxlength="10" value="<s:property value='totalDiscountRate'/>" onchange="BINOLWPSAL04.changeBillDiscountRate(this)" onkeyup="BINOLWPSAL04.keyUpChangeBillDiscountRate(this)" />%</td>
                </tr>
                <tr>
                    <th><s:text name="wpsal04.afterDiscountAmount" /></th>
                    <td><span id="dgSpanAfterDiscountAmount"></span><input id="dgAfterDiscountAmount" name="dgAfterDiscountAmount" type="hidden" maxlength="10" value="" /></td>
                </tr>
                <tr>
                    <th><s:text name="wpsal04.receivableAmount" /></th>
                    <td><input id="dgReceivableAmount" name="dgReceivableAmount" type="text" class="wp_input1" maxlength="10" value="" onchange="BINOLWPSAL04.changeReceivableAmount(this)" onkeyup="BINOLWPSAL04.keyUpChangeReceivableAmount(this)" /></td>
                </tr>
                <tr>
                    <th><s:text name="wpsal04.totalDiscountPrice" /></th>
                    <td>
                        <div class="numberbox"><span id="dgSpanTotalDiscountPrice"></span><input id="dgTotalDiscountPrice" name="dgTotalDiscountPrice" type="hidden" maxlength="10" value="" /></div>
                    </td>
                </tr>
                <tr>
                    <th><s:text name="wpsal04.roundingAmount" /></th>
                    <td>
                        <div class="numberbox"><span id="dgSpanRoundingAmount"></span><input id="dgRoundingAmount" name="dgRoundingAmount" type="hidden" maxlength="10" value="" /></div>
                    </td>
                </tr>
            </tbody>
        </table>
    </div>
    <div class="bottom_butbox clearfix">
    	<button id="btnConfirm" class="close" type="button" onclick="BINOLWPSAL04.confirm();return false;">
    		<span class="ui-icon icon-confirm"></span>
            <span class="button-text"><s:text name="wpsal04.confirm"/></span>
		</button>
		<button id="btnCancel" class="close" type="button" onclick="BINOLWPSAL04.cancel();return false;">
			<span class="ui-icon icon-close"></span>
            <span class="button-text"><s:text name="wpsal04.cancel"/></span>
		</button>
    </div>
</div>
</form>
</s:i18n>
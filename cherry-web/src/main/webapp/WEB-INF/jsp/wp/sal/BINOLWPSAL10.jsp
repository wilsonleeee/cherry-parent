<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/wp/sal/BINOLWPSAL10.js?V=20160621"></script>
<s:i18n name="i18n.wp.BINOLWPSAL10">
<div class="hide">
	<s:url id="s_dgCheckCouponUrl" value="/wp/BINOLWPSAL10_checkCoupon" />
	<a id="dgCheckCouponUrl" href="${s_dgCheckCouponUrl}"></a>
</div>
<form id="checkCouponForm" method="post" class="inline">
<div id="checkCouponPageDiv" class="hide ui-dialog-content ui-widget-content" style="display: block; width: auto; min-height: 200px;">
	<input type="hidden" id="dgOpenedState" name="dgOpenedState" value="Y"/>
	<input type="hidden" id="dgCampaignValid" name="dgCampaignValid" value="<s:property value='campaignValid'/>"/>
	<input type="hidden" id="dgMemberInfoId" name="dgMemberInfoId" value="<s:property value='memberInfoId'/>"/>
	<input type="hidden" id="dgMobilePhone" name="dgMobilePhone" value="<s:property value='mobilePhone'/>"/>
	<input type="hidden" id="dgActivityType" name="dgActivityType" value="<s:property value='activityType'/>"/>
	<input type="hidden" id="dgSubjectCode" name="dgSubjectCode" value="<s:property value='subjectCode'/>"/>
	<input type="hidden" id="dgActivityCode" name="dgActivityCode" value="<s:property value='activityCode'/>"/>
    <div class="wpleft_header">
    <div class="header_box"><s:text name="wpsal10.memberName" /><span class="top_detail2"><s:property value="memberName"/></span> </div>
    </div>
    <div class="wp_tablebox">
	    <table width="100%" cellspacing="0" cellpadding="0" border="0" class="wp_detail_s">
	    <tbody>
	    	<!-- 
	    	<tr>
                <th><s:text name="wpsal10.mobilePhone" /></th>
                <td><input type="text" id="dgMobilePhone" name="dgMobilePhone" class="text titleTools" maxlength="20" value="<s:property value='mobilePhone'/>" /></td>
            </tr>
             -->
	    	<tr>
                <th><s:text name="wpsal10.couponCode" /></th>
                <td><span><input type="text" id="dgCouponCode" name="dgCouponCode" class="text titleTools" maxlength="20"/></span></td>
            </tr>
	    </tbody>
	    </table>
    </div>
    <div class="bottom_butbox clearfix">
    	<button id="btnConfirm" class="close" type="button" onclick="BINOLWPSAL10.confirm();return false;">
    		<span class="ui-icon icon-confirm"></span>
			<span class="button-text"><s:text name="wpsal10.confirm"/></span>
		</button>
		<button id="btnCancel" class="close" type="button" onclick="BINOLWPSAL10.cancel();return false;">
			<span class="ui-icon icon-close"></span>
			<span class="button-text"><s:text name="wpsal10.cancel"/></span>
		</button>
    </div>
</div>
</form>
</s:i18n>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/wp/sal/BINOLWPSAL08_4.js"></script>
<s:i18n name="i18n.wp.BINOLWPSAL08">
<div class="hide">
		<s:url id="s_getLYHDActivityInfo" value="/wp/BINOLWPSAL08_getLYHDActivityInfo" />
		<a id="getLYHDActivityInfoUrl" href="${s_getLYHDActivityInfo}"></a>
		<s:url id="s_checkMobilephoneMemcode" value="/wp/BINOLWPSAL08_checkMobilephoneMemcode" />
		<a id="checkMobilephoneMemcodeUrl" href="${s_checkMobilephoneMemcode}"></a>
		<input type="hidden" id="maincode_LYHD_4" value="<s:property value='maincode'/>">
		<input type="hidden" id="memberCode_LYHD_4" value="<s:property value='memberCode'/>">
		<input type="hidden" id="subjectCode_LYHD_4" value="<s:property value='subjectCode'/>">
</div>
	<div class="wpleft_header">
    	<div class="header_box"><s:text name="wpsal08.memberName" /><span class="top_detail2"><s:property value="memberName"/></span> </div>
	</div>
	 <div class="wp_tablebox">
	    <table width="100%" cellspacing="0" cellpadding="0" border="0" class="wp_detail_s">
	    <tbody>
	    	<tr>
                <th><s:text name="wpsal08.mobileValidRule" /></th>
                <td><span><input type="text" id="customerText" name="customerText" class="text titleTools" maxlength="20"/></span></td>
            </tr>
	    </tbody>
	    </table>
    </div>
    <div class="bottom_butbox clearfix">
    	<button id="btnConfirm" class="close" type="button" onclick="BINOLWPSAL08_4.confirm();return false;">
    		<span class="ui-icon icon-confirm"></span>
			<span class="button-text"><s:text name="wpsal08.confirm"/></span>
		</button>
		<button id="btnCancel" class="close" type="button" onclick="BINOLWPSAL08_4.cancel();return false;">
			<span class="ui-icon icon-close"></span>
			<span class="button-text"><s:text name="wpsal08.cancel"/></span>
		</button>
    </div>
    
</s:i18n>
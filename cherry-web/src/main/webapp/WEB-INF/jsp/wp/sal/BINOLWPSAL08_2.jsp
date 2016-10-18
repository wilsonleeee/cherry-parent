<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/wp/sal/BINOLWPSAL08_2.js"></script>
<s:i18n name="i18n.wp.BINOLWPSAL08">
<div class="hide">
		<s:url id="s_getLYHDActivityInfo" value="/wp/BINOLWPSAL08_getLYHDActivityInfo" />
		<a id="getLYHDActivityInfoUrl" href="${s_getLYHDActivityInfo}"></a>
		<input type="hidden" id="maincode_LYHD_0" value="<s:property value='maincode'/>">
		<input type="hidden" id="memberCode_LYHD_0" value="<s:property value='memberCode'/>">
		<input type="hidden" id="subjectCode_LYHD_0" value="<s:property value='subjectCode'/>">
</div>
	
	<p  class="message hide" style="margin: 40px auto 30px; display: block;">
		<span id="messageContentSpan1"><s:text name="wpsal08.LYHDMakeSure"/></span>
	</p>
	<div class="bottom_butbox clearfix">
    	<button id="btnConfirm" class="close" type="button" onclick="BINOLWPSAL08_2.confirm();return false;">
    		<span class="ui-icon icon-confirm"></span>
			<span class="button-text"><s:text name="wpsal08.confirm"/></span>
		</button>
		<button id="btnCancel" class="close" type="button" onclick="BINOLWPSAL08_2.cancel();return false;">
			<span class="ui-icon icon-close"></span>
			<span class="button-text"><s:text name="wpsal08.cancel"/></span>
		</button>
    </div>
</s:i18n>
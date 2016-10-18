<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/wp/sal/BINOLWPSAL08_1.js"></script>
<div id="smartProDiv" class="hide ui-dialog-content ui-widget-content" style="width: auto; min-height: 0px; display: block; height: 154px;" scrolltop="0" scrollleft="0">
<p id="messageContent" class="message hide" style="margin: 40px auto 30px; display: block;">
<span id="messageContentSpan2">加载智能促销活动异常</span>
</p>
<p id="successContent" class="success hide" style="margin: 40px auto 30px; display: none;">
<span id="successContentSpan"></span>
</p>
	<div class="bottom_butbox clearfix">
    	<%-- <button id="btnConfirm" class="close" type="button" onclick="BINOLWPSAL08_1.confirm();return false;">
    		<span class="ui-icon icon-confirm"></span>
			<span class="button-text"><s:text name="确定"/></span>
		</button> --%>
		<button id="btnCancel" class="close" type="button" onclick="BINOLWPSAL08_1.cancel();return false;">
			<span class="ui-icon icon-close"></span>
			<span class="button-text"><s:text name="取消"/></span>
		</button>
    </div>
</div>
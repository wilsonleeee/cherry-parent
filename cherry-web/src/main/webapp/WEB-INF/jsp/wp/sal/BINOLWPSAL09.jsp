<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script  type="text/javascript" src="/Cherry/js/lib/jquery.hotkeys.js"></script>
<script type="text/javascript" src="/Cherry/js/wp/sal/BINOLWPSAL09.js"></script>
<s:i18n name="i18n.wp.BINOLWPSAL09">
<div class="webpos_installbox">
	<div style="padding:1em" class="center">
        <div class="wp_tablebox">
			<p>
				<span>
					<s:text name="wpsal09.errorInfo" />
				</span>
			</p>
		</div>
		<div class="bottom_butbox clearfix">
			<p>
				<button id="btnClose" class="close" type="button" onclick="window.close();return false;">
					<span class="ui-icon icon-close"></span>
					<span class="button-text"><s:text name="wpsal09.close"/></span>
				</button>
			</p>
		</div>
	</div>
</div>
</s:i18n>

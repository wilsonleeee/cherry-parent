<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/wp/sal/BINOLWPSAL13.js?V=20160922"></script>
<s:i18n name="i18n.wp.BINOLWPSAL13">
<%-- <div class="hide">
	开卡界面
	<s:url id="s_openCard" value="/wp/BINOLWPSAL13_openCard" />
	<a id="openCard" href="${s_openCard}"></a>
	<!-- 获取储值卡信息 -->
	<s:url id="s_consumption" value="/wp/BINOLWPSAL13_getConsumption" />
	<a id="consumption" href="${s_consumption}" ></a>
	<!-- 检查储值卡是否存在 -->
	<s:url id="s_checkCardCode" value="/wp/BINOLWPSAL13_checkCardCode" />
	<a id="checkCardCode" href="${s_checkCardCode}" ></a>
</div> --%>
<form id="cardForm" method="post" class="inline">
    <div class="wp_tablebox">
        <table width="100%" cellspacing="0" cellpadding="0" border="0" class="wp_detail_s">
            <tbody>
                <tr>
                    <th><s:text name="wpsal13.printCardCode" /></th>
                    <td><span><input class="text" id="dgCardCode" name="cardCode" value="<s:property value="memCode"/>"/></span></td>
                </tr>
            </tbody>
        </table>
    </div>
    <div class="bottom_butbox clearfix" id="div1">
	    	<button id="btnConfirm" class="close" type="button" onclick="BINOLWPSAL13.confirm();return false;">
	    		<span class="ui-icon icon-confirm"></span>
	            <span class="button-text"><s:text name="wpsal13.confirm"/></span>
			</button>
			<button id="btnCancel" class="close" type="button" onclick="BINOLWPSAL13.cancel();return false;">
				<span class="ui-icon icon-close"></span>
	            <span class="button-text"><s:text name="wpsal13.cancel"/></span>
			</button>
    </div>
    <div class="bottom_butbox clearfix" id="div2" style="display: none">
	    	<button id="btnConfirm" class="close" type="button" onclick="BINOLWPSAL13.confirm3();return false;">
	    		<span class="ui-icon icon-confirm"></span>
	            <span class="button-text"><s:text name="wpsal13.confirm"/></span>
			</button>
			<button id="btnCancel" class="close" type="button" onclick="BINOLWPSAL13.cancel3();return false;">
				<span class="ui-icon icon-close"></span>
	            <span class="button-text"><s:text name="wpsal13.cancel"/></span>
			</button>
    </div>
</form>
</s:i18n>
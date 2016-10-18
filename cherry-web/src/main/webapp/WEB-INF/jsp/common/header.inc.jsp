<%@ page pageEncoding="utf-8" %>
<%@ page import="com.cherry.cm.core.CherryConstants" %>
<%@ page import="com.cherry.cm.cmbeans.UserInfo" %>
<%@ page import="com.cherry.cm.cmbeans.CounterInfo" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<link href="/Cherry/css/webtalk/webtalk.main.css" rel="stylesheet" type="text/css" />

<script type="text/javascript" src="/Cherry/js/lib/sjcl.js"></script>
<BR />
<h1 class="logo left"><a href="#">WITPOS 店务通后台管理系统</a></h1>
<s:url id="url_logout" value="/logout" />
<s:url action="BINOLLGTOP03_init" id="BINOLLGTOP03_initUrl" namespace="/lg"></s:url>
<s:url action="TopAction_getOnlineUser" id="TopAction_getOnlineUserUrl" namespace="/lg"></s:url>
<div class="topmenu right">
    <cherry:show domId="BATCHWARN">
        <%-- ================== Batch异常结束提示 START ======================= --%>
        <jsp:include page="/WEB-INF/jsp/common/batchWarn.jsp" flush="true" />
        <%-- ================== Batch异常结束提示START ======================= --%>
    </cherry:show>
    
    <div class="left">
    <s:if test='#session.userinfo.LoginName.equals("admin")'>
		<a id="TopAction_getOnlineUserUrl" href="${TopAction_getOnlineUserUrl }" class="ui-icon icon-userlist" 
		  onclick="javascript:popOnlineUser();return false;" title="<s:text name="header.onlineUser" />"
		  style="margin-left:10px; display:inline-block;">
		</a>
    </s:if>
    </div>
<%
CounterInfo counterInfo = (CounterInfo)session.getAttribute("counterInfo");
if(counterInfo != null) {
	out.print("<div class=\"left\"><span style=\"font-weight:bold;\">"+counterInfo.getCounterName()+"</span></div>");
	out.print("<div class=\"left\"><span>&nbsp;|&nbsp;</span></div>");
}
%>
    <div class="left">
    <span>
	<a id="usernameId" href="${BINOLLGTOP03_initUrl }" class="popup" onclick="javascript:openWin(this);return false;"><strong><s:property value="#session.userinfo.LoginName"/></strong></a>
	</span>
	</div>
	<div class="left"><span class="">&nbsp;|&nbsp;</span></div>
	<%-- 消息提示 --%>
    <jsp:include page="/WEB-INF/jsp/common/headerMessage.jsp" flush="true" />
    <div class="left"><span class="right">&nbsp;|&nbsp;</span></div>
    <div class="left">
    <span>
	<a id="logoutURL" href="${url_logout }"  onclick="popMsgList.beforeLogout();return false;">
		<s:text name="header.logout"/>
	</a>
	</span>
	</div>
</div>
<div class="top_logo"></div>
<s:if test='#session.userinfo.LoginName.equals("admin")'>
<%-- ================== 弹出datatable -- 在线用户共通START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/popOnlineUser.jsp" flush="true" />
<%-- ================== 弹出datatable -- 在线用户共通START ======================= --%>
</s:if>

<div class="hide">
    <input id="header_systemMsg" type="hidden" value='<s:text name="header.systemMsg"></s:text>'/>
    <input id="header_pwdExpire1" type="hidden" value='<s:text name="header.pwdExpire1"></s:text>'/>
    <input id="header_pwdExpire2" type="hidden" value='<s:text name="header.pwdExpire2"></s:text>'/>
    <input id="header_updatePW" value="<s:text name='header.updatePW'/>">
    <input id="header_global_page_ok" value="<s:text name='global.page.ok'/>">
    <input id="header_global_page_close" value="<s:text name='global.page.close'/>">
    <input id="header_lastLogin" value="<s:text name='header.lastLogin'/>">
    <input id="header_lastLoginIP" value="<s:text name='header.lastLoginIP'/>">
    <input id="header_beforeLogoutUnRead" value="<s:text name='header.beforeLogoutUnRead'/>">
</div>

<%-- 右下角消息框 --%>
<div id="talkboxmenu_window" class="talkboxmenu_window hide" style="z-index:9;">
    <div class="talkboxmenu_sub_openbox clearfix">
        <div id="openboxlist" class="openboxlist"></div>
        <%--<div class="line clearfix"></div> 
        <div class="openboxmenu clearfix"></div>--%>
    </div>
    <div class="talkboxmenu">
        <%--<div class="talkboxmenu_sh"><img src="/Cherry/css/webtalk/images/menuarrow_right.gif" /></div>--%>
        <div class="talkboxmenu_sub">
            <a href="#" class="selected"><s:text name="header.tipMsg"></s:text><span class="comimgbg w-boxmenu-arrowup"></span></a>
        </div>
    </div>
</div>

<%--修改密码Dialog --%>
<div id="dialogUpdatePwdInit" class="hide"></div>
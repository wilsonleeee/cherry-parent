<%@ page pageEncoding="utf-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<script type="text/javascript">
//圆角设置
//$("#unReadCount").corner("6px");
</script>
<style>
.unReadCount{
line-height: 16px;
/* left:-4px; */
background-color: red;
border-radius: 6px;
color: #FFFFFF;
position: relative;
text-align: center;
width: 16px;
top:-1px;
padding-left: 3px;
padding-right: 3px;
/* margin-right: -8px; */
}
</style>
<s:url action="TopAction_setMsgRead" id="TopAction_setMsgReadUrl" namespace="/lg"></s:url>
<span class="hide" id ="TopAction_setMsgReadUrl" >${TopAction_setMsgReadUrl}</span>
<s:url action="TopAction_getMsgList" id="TopAction_getMsgListUrl" namespace="/lg"></s:url>
<div class="left">
    <span title="<s:text name="header.messageUnReadInfo" />"  id="newMessageIcon" class="ui-icon icon-mail <s:if test="null == #session.UnReadCount || #session.UnReadCount==0">hide</s:if>"></span>
    <span>
        <a id="TopAction_getMsgListUrl"  href="${TopAction_getMsgListUrl }" onclick="javascript:popMsgList.popMsgList();return false;"><s:text name="header.message" /></a>
        <span id="unReadCount" class="unReadCount hide"><s:property value="#session.UnReadCount"/></span><%--未读数 --%>
    </span>
</div>
<%--点击退出提示数 --%>
    
<%-- ================== 弹出datatable -- 消息List共通START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/popMsgList.jsp" flush="true" />
<%-- ================== 弹出datatable -- 消息List共通START ======================= --%>
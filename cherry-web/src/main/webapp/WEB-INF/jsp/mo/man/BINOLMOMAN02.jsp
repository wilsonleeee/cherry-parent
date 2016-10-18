<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/mo/man/BINOLMOMAN02.js"></script>
<script type="text/javascript" src="/Cherry/js/common/ajaxfileupload.js"></script>
<s:i18n name="i18n.mo.BINOLMOMAN02">
    <div class="hide">
        <s:url action="BINOLMOMAN02_addMachineInfo" id="addMachineInfoUrl"></s:url>
        <a id="addMachineInfoUrl" href="${addMachineInfoUrl}"></a>
        <s:url id="url_parsefile" value="/mo/BINOLMOMAN02_PARSEFILE" />
        <span id ="s_parsefile" style="display:none">${url_parsefile}</span>
        <s:text name="MAN02_filename" id="filename"/>
        <s:url id="downloadUrl" value="%{filename}"/>
        <div id="errMsgText1"><s:text name="EMO00015"/></div>
    </div>
    <div class="panel-header">
        <div class="clearfix"> 
	    <span class="breadcrumb left">	    
			<jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
		</span> 
        </div>
    </div>
    <div id="actionResultDisplay"></div>
    <%-- ================== 错误信息提示 START ======================= --%>
    <div id="errorDiv2" class="actionError" style="display:none">
        <ul>
            <li><span id="errorSpan2"></span></li>
        </ul>         
    </div>
    <%-- ================== 错误信息提示   END  ======================= --%>
    <input type="hidden" id="mobileRule" name="mobileRule" value="<s:property value='mobileRule'/>"/>
    <div class="panel-content">
        <div id="section" class="section">
            <div class="section-header">
                <span class="ui-icon icon-ttl-section-search-result"></span>
                <span>
                    <%-- 所属品牌--%>
                    <s:text name="MAN02_brandInfo"/>
                </span>
                <s:if test="brandInfoList.size()>1">
                    <s:select name="brandInfoId" list="%{brandInfoList}" listKey="brandInfoId" listValue="brandName" onclick=""></s:select>
                </s:if>
                <s:else>
                    <s:iterator value="brandInfoList">
                                                                ：<s:property value='brandName'/>
                        <input type="hidden" id="brandInfoId" name="brandInfoId" value="<s:property value='brandInfoId'/>"/>
                    </s:iterator>
                </s:else>
            </div>
            <div class="section-content">
                <cherry:form id="mainForm" class="inline">
                    <div id="detailInfo">
                        <jsp:include page="/WEB-INF/jsp/mo/man/BINOLMOMAN02_1.jsp" flush="true" />
                    </div>
                </cherry:form>
            </div>
        </div>
    </div>
    <div id="savePage"></div>
</s:i18n>
<div id="errmessage" style="display:none">
    <input type="hidden" id="errmsg1" value='<s:text name="EMO00005"/>'/>
    <input type="hidden" id="errmsg2" value='<s:text name="EMO00008"/>'/>
    <input type="hidden" id="errmsg3" value='<s:text name="EMO00010"/>'/>
    <input type="hidden" id="errmsg4" value='<s:text name="EMO00011"/>'/>
    <input type="hidden" id="errmsg5" value='<s:text name="EMO00013"/>'/>
</div>
<div id="machineTypeRule" style="display:none">
<s:iterator value="codeRuleList" id="codeRuleMap">
    <ul id='<s:property value="#codeRuleMap.brandInfoId"/>'>
        <li><span id="W1"><s:property value="#codeRuleMap.W1"/></span></li>
        <li><span id="W2"><s:property value="#codeRuleMap.W2"/></span></li>
        <li><span id="W3"><s:property value="#codeRuleMap.W3"/></span></li>
        <li><span id="W4"><s:property value="#codeRuleMap.W4"/></span></li>
        <li><span id="WM"><s:property value="#codeRuleMap.WM"/></span></li>
        <li><span id="WB"><s:property value="#codeRuleMap.WB"/></span></li>
        <li><span id="WS"><s:property value="#codeRuleMap.WS"/></span></li>
        <li><span id="MP"><s:property value="#codeRuleMap.MP"/></span></li>
    </ul>
</s:iterator>
</div>
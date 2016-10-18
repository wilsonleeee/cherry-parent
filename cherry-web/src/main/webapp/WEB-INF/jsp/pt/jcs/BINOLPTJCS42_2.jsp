<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<s:i18n name="i18n.pt.BINOLPTJCS42">
<%--全部生成弹出框--%>
<div id="hideDivGenerateQRCode" class="hide">
    <div id="dialogGenerateQRCodeInit" class="hide"></div>
</div>
<div id="generateQRCodeDialog" class="hide">
    <form id="formGenerateQRCodeTemplet" method="post">
        <div id="errorDivGenQR" class="actionError" style="display:none;">
            <ul>
                <li>
                    <span id="errorSpanGenQR"><s:text name="JCS42_RequireURL"/></span>
                </li>
            </ul>
        </div>
        <br/>
        <p>
            <%-- 所属品牌 --%>
            <s:if test='%{brandInfoList != null && !brandInfoList.isEmpty()}'>
                <label style="width: 50px;"><s:text name="JCS42_brandInfo"></s:text></label>
                <s:text name="JCS42_select" id="JCS42_select"/>
                <s:select id="brandInfoIdGenQR" name="brandInfoIdGenQR" list="brandInfoList" listKey="brandInfoId" listValue="brandName" headerKey="" cssStyle="width:100px;" onchange="BINOLPTJCS42.popBrandChange();"></s:select>
            </s:if>
            <s:else>
                <input type="hidden" id="brandInfoIdGenQR" name="brandInfoIdGenQR" value='<s:property value="#session.userinfo.BIN_BrandInfoID"/>'>
            </s:else>
        </p>
        <%--经销商 --%>
        <p>
            <label style="width: 50px;"><s:text name="JCS42_reseller"></s:text></label>
            <s:select id="resellerIdGenQR" name="resellerIdGenQR" list="resellerList" listKey="BIN_ResellerInfoID" listValue="ResellerCodeName" headerKey="" headerValue="%{JCS42_selectAll}" cssStyle="width:250px;"></s:select>
        </p>
        <p class="clearfix">
            <label style="width: 50px;"><s:text name="JCS42_prefixURL"/></label>
            <span><s:textfield name="prefixURL" size="50" maxlength="200" value=""></s:textfield></span>
        </p>
        <p class="clearfix">
            <span class="highlight"><s:text name="global.page.snow"/></span>
            <span class="gray"><s:text name="JCS42_prefixURLComment"></s:text></span>
        </p>
    </form>
</div>
<div id="errmessage" style="display:none">
    <input type="hidden" id="JCS42_popTitle" value='<s:text name="JCS42_popTitle"/>'/>
    <input type="hidden" id="JCS42_genAll" value='<s:text name="JCS42_resetALLQRCode"/>'/>
    <input type="hidden" id="JCS42_resetQRCode" value='<s:text name="JCS42_resetQRCode"/>'/>
    <input type="hidden" id="JCS42_cancel" value='<s:text name="JCS42_cancel"/>'/>
    <input type="hidden" id="JCS42_genWaiting" value='<s:text name="JCS42_genWaiting"/>'/>
</div>
</s:i18n>
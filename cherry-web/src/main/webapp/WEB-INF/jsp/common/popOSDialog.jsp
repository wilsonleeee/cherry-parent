<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%--工作流弹出框--%>
<div id="hideDivOS" class="hide">
    <div id="dialogOSInit" class="hide"></div>
</div>
<div id="osDialog" class="hide">
    <form id="osDialogFormTemplet" method="post">
        <div id="errorDivOS" class="actionError" style="display:none;">
            <ul>
                <li>
                    <span id="errorSpanOS"><s:text name="os.commentsRequire"/></span>
                </li>
            </ul>
        </div>
        <br/>
        <p class="clearfix">
           <label><s:text name="os.commentsInfo"/><span class="highlight">*</span></label>
        </p>
        <p class="clearfix">
           <!--<label><s:text name="global.page.comments"/><span class="highlight">*</span></label>-->
           <textarea style="height:105px;" id="opComments" name="opComments" onpropertychange="return isMaxLen(this);" maxlength="200" rows="" cols=""></textarea>
        </p>
    </form>
</div>
<div id="osSDNoIFDialog" class="hide">
    <form id="osDialogFormSDNoIF" method="post">
        <p class="clearfix">
            <label><s:text name="os.deliverNoIF"/></label>
            <span><s:textfield name="deliverNoIFTemplet" size="30" maxlength="40" value=""></s:textfield></span>
        </p>
        <p class="clearfix">
            <span class="highlight"><s:text name="global.page.snow"/></span>
            <span class="gray"><s:text name="os.popDialog.commentSDNoIF"></s:text></span>
        </p>
    </form>
</div>
<div id="errmessage" style="display:none">
    <input type="hidden" id="os_confirm" value='<s:text name="os.confirm"/>'/>
    <input type="hidden" id="os_inhibit" value='<s:text name="os.inhibit"/>'/>
    <input type="hidden" id="os_trashOD" value='<s:text name="os.trashOD"/>'/>
    <input type="hidden" id="os_trashSD" value='<s:text name="os.trashSD"/>'/>
    <input type="hidden" id="os_inhibitSD" value='<s:text name="os.inhibitSD"/>'/>
    <input type="hidden" id="os_commentsInfo" value='<s:text name="os.commentsInfo"/>'/>
    <input type="hidden" id="os_commentsRequire" value='<s:text name="os.commentsRequire"/>'/>
    <input type="hidden" id="os_global_page_ok" value='<s:text name="global.page.ok"/>'/>
    <input type="hidden" id="os_global_page_cancel" value='<s:text name="global.page.cancle"/>'/>
    <input type="hidden" id="os_pd_pleaseInputSDNoIF" value='<s:text name="os.popDialog.pleaseInputSDNoIF"/>'/>
</div>
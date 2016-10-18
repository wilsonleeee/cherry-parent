<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript">
$(function(){
	// 表单验证配置
	cherryValidate({
	    formId: 'updatePwdForm',
	    rules: {
	        oldPassWord: {required: true, maxlength: 30},// 原始密码
	        newPassWord: {required: true, maxlength: 30},// 新密码
	        confirmPW: {required: true, maxlength: 30, equalTo: "#newPassWord"}// 确认密码
	    }
	});
});

function popUpdatePwd_doSave(url) {
    if (!$('#updatePwdForm').valid()) {
        return false;
    };
    var p = {adata:'CHERRYAUTH',
            iter:1000,
            mode:'ccm',
            ts:64,
            ks:128};
    var rp = {};
    var oldPassWord = $("#oldPassWord").val();
    var newPassWord = $("#newPassWord").val();
    var ct = sjcl.encrypt(oldPassWord, newPassWord, p, rp).replace(/,/g,",\n");
    $("#newPassWordTemp").val(ct);
    // 参数序列化
    var params = $("#submitUpdatePwdForm").serialize();
    params += "&popType=dialog";
    cherryAjaxRequest({
        url: url,
        param: params,
        callback: function(msg) {
        	if(msg.indexOf("successDiv")>0) {
        		$("#dialogUpdatePwdInit").html(msg);
        		$("#dialogUpdatePwdInit").dialog("destroy");
                var dialogSetting = {
                        dialogInit: "#dialogUpdatePwdInit",
                        text: msg,
                        width: 500,
                        height: 300,
                        title: $("#header_updatePW").val(),
                        confirm: $("#header_global_page_close").val(),
                        confirmEvent: function(){
                            $("#dialogUpdatePwdInit").dialog('destroy');
                            $("#dialogUpdatePwdInit").remove();
                        },
                        closeEvent: function(){
                            $("#dialogUpdatePwdInit").dialog('destroy');
                            $("#dialogUpdatePwdInit").remove();
                        }
                }
                openDialog(dialogSetting);
        	}
        },
        coverId: ".ui-dialog-buttonset"
    });
}

function popUpdatePwd_showPassword(obj) {
	if(obj.checked) {
		popUpdatePwd_changeType("#oldPassWord", "text");
		popUpdatePwd_changeType("#newPassWord", "text");
		popUpdatePwd_changeType("#confirmPW", "text");
	} else {
		popUpdatePwd_changeType("#oldPassWord", "password");
		popUpdatePwd_changeType("#newPassWord", "password");
		popUpdatePwd_changeType("#confirmPW", "password");
	}
}

function popUpdatePwd_changeType(id, type) {
	var html = '<input type="'+ type +'" name="'+ $(id).attr("name") +'" id="'+ $(id).attr("id") +'" class="'+ $(id).attr("class") +'" value="'+ $(id).val() +'" maxlength="'+ $(id).attr("maxlength") +'" />';
	var $parent = $(id).parent();
	$(id).remove();
	$parent.prepend(html);
}
</script>
<s:url id="BINOLLGTOP03_updatePwd_url" action="BINOLLGTOP03_update"/>
<div class="hide">
    <a id="BINOLLGTOP03_updatePwd_url" href="${BINOLLGTOP03_updatePwd_url}"></a>
</div>
<%--修改密码--%>
<div id="updatePwd" class="">
    <div id="actionResultDisplay"></div>
    <cherry:form id="updatePwdForm" csrftoken="false" >
        <table style="margin:auto; width:100%;" class="detail">
            <tr>
                <th><s:text name="header.brandName"/></th><%--所属品牌--%>
                <td><s:property value="#session.userinfo.BrandName"/></td>
            </tr>
            <tr>
                <th><s:text name="header.loginName"/></th><%--当前帐号--%>
                <td><s:property value="#session.userinfo.LoginName"/></td>
            </tr>
            <tr>
                <th><s:text name="header.oldPassWord"/><span class="highlight">*</span></th><%--旧密码--%>
                <td>
                    <span><input id="oldPassWord" class="text" type="password" maxlength="30" name="oldPassWord"></span>
                    <span><input id="showpw" type="checkbox" onclick="popUpdatePwd_showPassword(this);"><s:text name="header.showPW"/></span>
                </td>
            </tr>
            <tr>
                <th><s:text name="header.newPassWord"/><span class="highlight">*</span></th><%--新密码--%>
                <td>
                    <span><input id="newPassWord" class="text" type="password" maxlength="30" name="newPassWord"></span>
                </td>
            </tr>
            <tr>
                <th><s:text name="header.confirmPW"/><span class="highlight">*</span></th><%--确认新密码--%>
                <td>
                    <span><input id="confirmPW" class="text" type="password" maxlength="30" name="confirmPW"></span>
                </td>
            </tr>
        </table>
    </cherry:form>
    <form id="submitUpdatePwdForm">
        <%-- 更新日时 --%>
        <input type="hidden" name="modifyTime" value="${user.modifyTime}"/>
        <%-- 更新次数 --%>
        <input type="hidden" name="modifyCount" value="${user.modifyCount}"/>
        <%-- 新密码 --%>
        <input type="hidden" name="newPassWord" value="" id="newPassWordTemp"/>
    </form>
</div>
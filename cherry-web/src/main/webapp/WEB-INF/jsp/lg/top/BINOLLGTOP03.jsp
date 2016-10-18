<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/lib/sjcl.js"></script>
<script type="text/javascript">

$(document).ready(function() {
	if (window.opener) {
	    window.opener.lockParentWindow();
	}
});
window.onbeforeunload = function(){
	if(window.opener){
		window.opener.unlockParentWindow();
	}
};

$(document).ready(function() {
	cherryValidate({			
		formId: "updateUserForm",		
		rules: {
		        oldPassWord: {required: true, maxlength: 30},  // 原始密码
		        newPassWord: {required: true, maxlength: 30},  // 新密码
		        confirmPW: {required: true, maxlength: 30, equalTo: "#newPassWord"}     // 确认密码
		}		
	});
	
} );

/* 
 * 保存处理
 */
 function doSave(url) {
		if (!$('#updateUserForm').valid()) {
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
		var params = $("#submitForm").serialize();
		cherryAjaxRequest({
			url: url,
			param: params,
			callback: function(msg) {
			//$('#oldPassWord').val('');
			//$('#newPassWord').val('');
			//$('#confirmPW').val('');
			},
			coverId: "#pageButton"
		});
	}

function showPassword(obj) {
	if(obj.checked) {
		changeType("#oldPassWord", "text");
		changeType("#newPassWord", "text");
		changeType("#confirmPW", "text");
	} else {
		changeType("#oldPassWord", "password");
		changeType("#newPassWord", "password");
		changeType("#confirmPW", "password");
	}
}

function changeType(id, type) {
	var html = '<input type="'+ type +'" name="'+ $(id).attr("name") +'" id="'+ $(id).attr("id") +'" class="'+ $(id).attr("class") +'" value="'+ $(id).val() +'" maxlength="'+ $(id).attr("maxlength") +'" />';
	var $parent = $(id).parent();
	$(id).remove();
	$parent.prepend(html);
}

</script>
<%-- 保存按钮 --%>
<s:url id="update_url" action="BINOLLGTOP03_update"/>

 <s:i18n name="i18n.lg.BINOLLGTOP03">
 <div class="main container clearfix">
  <cherry:form id="updateUserForm" method="post" class="inline" csrftoken="false">
   <input type="password" style="display: none;"/>
   <div class="panel ui-corner-all">
     <div class="panel-header">
	    <div class="clearfix">
	       <span class="breadcrumb left"> 
	          <span class="ui-icon icon-breadcrumb"></span>
				<s:text name="top03_personal"/> &gt; <s:text name="top03_editPassword"/>
	       </span>
	    </div>
	 </div>
     <div class="panel-content clearfix">  
      <div class="welcome">
        <div class="welcome-l">
          <div class="welcome-r">
           <p class="welcome-t"></p>
            <div id="actionResultDisplay"></div>
            <div class="section">
             <div class="section-header"><strong><span class="ui-icon icon-ttl-section-info"></span><s:text name="top03_editPassword"/></strong></div>
                <div class="section-content">
                  <table border="0" style="width:80%;" class="detail">
                    <tbody>
                    <tr>
                      <%-- 所属品牌--%>
                      <th style="width: 25%"><s:text name="top03.brandName"/></th>
                      <td style="width: 75%"><span><s:property value="user.brandName"/></span> </td>
                    </tr>
                    <tr>
                      <%-- 登入账号--%>
                      <th style="width: 25%"><s:text name="top03.loginName"/></th>
                      <td style="width: 75%"><span><s:property value="user.loginName"/></span></td>
                    </tr>
                    <tr>
                      <%-- 原始密码--%>
                      <th style="width: 25%"><s:text name="top03.oldPassWord"/><span class="highlight"><s:text name="global.page.required"/></span></th>
                      <td style="width: 75%"><span><s:password name="oldPassWord" cssClass="text" maxlength="30"/></span><span><input type="checkbox" id="showpw" onclick="showPassword(this);"/>显示密码</span></td>
                    </tr>
                    <tr>
                      <%-- 新密码--%>
                      <th style="width: 25%"><s:text name="top03.newPassWord"/><span class="highlight"><s:text name="global.page.required"/></span></th>
                      <td style="width: 75%"><span><s:password name="newPassWord" cssClass="text" maxlength="30"/></span></td>
                    </tr>
                    <tr>
                       <%-- 确认密码--%>
                      <th style="width: 25%"><s:text name="top03.confirmPW"/><span class="highlight"><s:text name="global.page.required"/></span></th>
                      <td style="width: 75%"><span><s:password name="confirmPW" cssClass="text" maxlength="30"/></span></td>
                    </tr>
                 </tbody>
                </table>
               </div>
              </div>
              <hr/>
              <div class="center clearfix" id="pageButton">
                <button id="save" class="save" type="button" onclick="doSave('${update_url}');return false;" >
                  <span class="ui-icon icon-save"></span>
                  <span class="button-text"><s:text name="global.page.save"/></span>
                </button>
                <button id="close" class="close" type="button"  onclick="window.close();return false;">
	           		<span class="ui-icon icon-close"></span>
	           		<span class="button-text"><s:text name="global.page.close"/></span>
	         	</button>
              </div>
             </div>
            </div>
           </div>
         </div>
       </div>
    </cherry:form>
    <form id="submitForm">
    	<%-- 更新日时 --%>
        <input type="hidden" name="modifyTime" value="${user.modifyTime}"/>
        <%-- 更新次数 --%>
        <input type="hidden" name="modifyCount" value="${user.modifyCount}"/>
        <%-- 新密码 --%>
        <input type="hidden" name="newPassWord" value="" id="newPassWordTemp"/>
    </form>
   </div> 
   </s:i18n>
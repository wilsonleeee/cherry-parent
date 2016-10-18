<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>

<%-- 保存按钮 --%>
<s:url id="editSave_url" action="BINOLPLUPM03_update"/>

<s:i18n name="i18n.pl.BINOLPLUPM03">
  <div id="saveDialog" class="hide">
	<div id="saveActionResult"></div>
      <cherry:form id="updateUserForm" class="inline" csrftoken="false">
          <table class="detail" cellpadding="0" cellspacing="0">
             <tr>
                <%--品牌 --%>
                <th><s:text name="upm03.brandName"/></th>
                <td><span><s:property value="user.brandName"/></span>
                	<%--品牌 ID--%>
                	<input type="hidden" name="brandInfoId" value="${user.brandInfoId}"/>
                    <%-- 用户ID --%>
                    <input type="hidden" name="userId" value="${user.userId}"/>
                    <%-- 更新日时 --%>
                    <input type="hidden" name="modifyTime" value="${user.modifyTime}"/>
                    <%-- 更新次数 --%>
                    <input type="hidden" name="modifyCount" value="${user.modifyCount}"/>
                </td>
             </tr>
             <tr>
                <%-- 雇员代码 --%>
                <th><s:text name="upm03.employeeCode"/></th>
                <td><span><s:property value="user.employeeCode"/></span></td>
             </tr>
             <tr>
                <%-- 雇员姓名 --%>
                <th><s:text name="upm03.employeeName"/></th>
                <td><span><s:property value="user.employeeName"/></span></td>
            </tr>
            <tr>
                <%-- 用户账号 --%>
                <th><s:text name="upm03.loginName"/></th>
                <td><span><s:property value="user.loginName"/></span></td>
            </tr>
            <tr>
                <%-- 密码--%>
                <th><s:text name="upm03.passWord"/><span class="highlight"><s:text name="global.page.required"/></span></th>
                <td><span style="float:none;"><s:password name="passWord" cssClass="text" maxlength="30" id="passWord"/></span><br/>
                        <span style="font-size: 12px;color:#808080;" id="pwText">
                        	<s:property value="pwConfInfo.minLength"/> <s:text name="upm03_char1"/> <s:property value="pwConfInfo.maxLength"/><s:text name="upm03_include"/>
                        	<%-- 英文--%>
                        	<s:if test='pwConfInfo.hasAlpha == "1"'>
                        	<s:text name="upm03_hasAlpha"/>
                        	</s:if>
                        	<s:if test='pwConfInfo.hasAlpha == "1" && (pwConfInfo.hasNumeric == "1" || pwConfInfo.hasOtherChar == "1")'>
                        	<s:text name="upm03_char2"/>
                        	</s:if>
                        	<%-- 数字--%>
                        	<s:if test='pwConfInfo.hasNumeric == "1"'><s:text name="upm03_hasNumeric"/></s:if>
                        	<s:if test='pwConfInfo.hasNumeric == "1" && pwConfInfo.hasOtherChar == "1"'>
                        	<%-- 特殊符号--%>
                        	<s:text name="upm03_hasOtherChar"/><s:property value="pwConfInfo.otherCharSpace"/><s:text name="upm03_hasOtherCharClose"/>
                        	</s:if>
                        </span>
                </td>
            </tr>  
            <tr>
            <%-- 确认密码--%>
                <th><s:text name="upm03.confirmPW"/><span class="highlight"><s:text name="global.page.required"/></span></th>
                <td><span><s:password name="confirmPW" cssClass="text" maxlength="30"/></span> </td>  
           </tr> 
        </table>
        <hr class="space"/>
        <hr/>
        <div class="center clearfix" id="pageButton">
            <button class="confirm" onclick="doSave('${editSave_url}'); return false;" id="confirmBtn">
       	       <span class="ui-icon icon-confirm"></span>
       	       <%-- 确定按钮 --%>
       	       <span class="button-text"><s:text name="upm03_confirm"/></span>
           </button>
	       <button class="close" onclick="doClose('#saveDialog'); return false;"><span class="ui-icon icon-close"></span>
               <%-- 关闭按钮--%>
               <span class="button-text"><s:text name="global.page.close"/></span>
          </button>
     </div>
     </cherry:form>
  </div>
</s:i18n>

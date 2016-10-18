<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<script type="text/javascript" src="/Cherry/js/pl/upm/BINOLPLUPM02.js"></script>

<%-- 保存按钮 --%>
<s:url id="addUser_url" action="BINOLPLUPM02_save"/>

<s:i18n name="i18n.pl.BINOLPLUPM02">
<s:url id="change_url" action="BINOLPLUPM02_change"/>
<div class="hide">
	<a id="changeUrl" href="${change_url}"></a>
</div>
<div class="main container clearfix">
  <div class="panel-header">
    <div class="clearfix">
       <span class="breadcrumb left"> 
          <span class="ui-icon icon-breadcrumb"></span>
        	<s:text name="upm02_plManage"/> &gt; <s:text name="upm02_add"/>
       </span>
     </div>
    </div>
    <div id="actionResultDisplay"></div>
      <div class="panel-content">
      <cherry:form id="addUserForm" method="post" class="inline" csrftoken="false">
        <div class="section">
          <div class="section-header"><strong><span class="ui-icon icon-ttl-section-info"></span>
          		<%-- 基本信息 --%>
          		<s:text name="global.page.title"/>
          	</strong>
          </div>
          <div class="section-content" id="userMap">
            <table class="detail" cellpadding="0" cellspacing="0">
                  <tr>
                  <%-- 品牌 --%>
                    <th>
                         <label><s:text name="upm02.brand"/></label>
                    </th>
                    <td>
                       <p>
                         <s:select id="brandSel" name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" onchange="changePwText();"/>
                       </p>
                    </td>
                    <%-- 密码--%>
                    <th>
                        <label><s:text name="upm02.passWord"/><span class="highlight"><s:text name="global.page.required"/></span></label>
                    </th>
                    <td>
                        <span><s:password name="passWord" cssClass="text" maxlength="30" id="passWord"/></span><br/>
                        <span style="font-size: 12px;color:#808080;" id="pwText">
                        	<jsp:include page="/WEB-INF/jsp/pl/upm/BINOLPLUPM02_1.jsp" flush="true" />
                        </span>
                    </td>
                  </tr>
                  <tr>
                    <%-- 雇员姓名 --%>
                    <th>
                        <label><s:text name="upm02.Employee"/><span class="highlight"><s:text name="global.page.required"/></span></label>
                    </th>
                    <td id=empSel>
                       <input id="employeeId" type="hidden" name="employeeId"/>
                       <span>
                         <span id="defEmployee" class="left" style="word-wrap:break-word;overflow:hidden;width:230px;"></span>
	                     <a onclick="javascript:showEmployeeDialog(this);return false;" class="popup right" href="javascript:void(0);">
	                     <span class="ui-icon icon-search"></span>
	                     <s:text name="upm02.selectEmployee"/></a>   
                       </span>
                    </td>
                    <%-- 确认密码--%>
                    <th>
                        <label><s:text name="upm02.confirmPW"/><span class="highlight"><s:text name="global.page.required"/></span></label>
                    </th>
                    <td>
                        <span><s:password name="confirmPW" cssClass="text" maxlength="30"/></span>
                    </td>
                  </tr>
                  <tr>
                    <%-- 登入账号 --%>
                    <th>
                        <label><s:text name="upm02.loginName"/><span class="highlight"><s:text name="global.page.required"/></span></label>
                    </th>
                    <td>
                        <span><s:textfield name="loginName" cssClass="text" maxlength="30"/></span>
                    </td>
                  </tr>
              </table> 
            </div>
           </div>
           <hr/>
           <div class="center clearfix" id="pageButton">
              <button id="save" class="save" type="button" onclick="doSave('${addUser_url}');return false;" >
            	<span class="ui-icon icon-save"></span>
            	<span class="button-text"><s:text name="global.page.save"/></span>
              </button>
              <button id="close" class="close" type="button"  onclick="doClose();return false;">
            	<span class="ui-icon icon-close"></span>
            	<span class="button-text"><s:text name="global.page.close"/></span>
              </button>
         </div>
       </cherry:form>
      </div>
    </div>
</s:i18n>
<jsp:include page="/WEB-INF/jsp/common/popEmployeeTable.jsp" flush="true" />

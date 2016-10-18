<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/pl/upm/BINOLPLUPM01.js"></script>
    <%-- 用户信息查询URL --%>
    <s:url id="search_url" action="BINOLPLUPM01_search"/>
    <div class="hide">
     	<a id="searchUrl" href="${search_url}"></a>
    </div>

     <%-- 用户信息添加URL --%>
    <s:url id="add_url" action="BINOLPLUPM02_init"/>
    
   <s:i18n name="i18n.pl.BINOLPLUPM01">
     <cherry:form id="mainForm" class="inline" onsubmit="searchUser(); return false;" >
     <s:text name="select_default" id="select_default"></s:text>
      <div class="panel-header">
        <div class="clearfix">
        	<span class="breadcrumb left"> 
        		<span class="ui-icon icon-breadcrumb"></span>
        		<s:text name="upm01_plManage"/> &gt; <s:text name="upm01_query"/>
        	</span>
       		<span class="right"> 
       		<%-- 用户信息添加按钮 --%>
       		<cherry:show domId="PLUPM0101ADD">
       		 	<a href="${add_url}" class="add" onclick="javascript:openWin(this);return false;">
       		 		<span class="button-text"><s:text name="upm01_add"/></span>
       		 		<span class="ui-icon icon-add"></span>
       		 	</a>
       		 </cherry:show>
       		</span>
       	</div>
      </div>
      <%-- ================== 错误信息提示 START ======================= --%>
      <div id="errorMessage"></div>
      <%-- ================== 错误信息提示   END  ======================= --%>
      <div class="panel-content">
        <div class="box">

            <div class="box-header">
            	<strong><span class="icon icon-ttl-search"></span><s:text name="global.page.condition"/></strong>
              	<input type="checkbox" name="validFlag" value="1"/><s:text name="upm01.invalidUser"/>
            </div>
            <div class="box-content clearfix">
               <div class="column" style="width:50%; height:50px;">
               <s:if test="null != brandInfoList">
               	<p>
               		<label style="width: 70px;"><s:text name="upm01.brandInfo"/></label>
               	    <s:select name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" headerKey="" headerValue="%{#select_default}"></s:select>
               	</p>
               </s:if>
               <p>
				<%-- 用户账号--%>
                  <label style="width: 70px;"><s:text name="upm01.loginName"/></label>
                  <s:textfield name="loginName" cssClass = "text" maxlength="30"/>
                 </p>
              </div>
              <div class="column last" style="width:49%; height:50px;">
                 <p>
                 <%-- 员工代号--%>
                  <label style="width: 70px;"><s:text name="upm01.employeeCode"/></label>
                  <s:textfield name="employeeCode" cssClass="text" maxlength="10"/> 
                 </p>
                 <p>
				<%-- 员工姓名--%>
                  <label style="width: 70px;"><s:text name="upm01.employeeName"/></label>
                  <s:textfield name="employeeName" cssClass = "text" maxlength="30"/>
                 </p>
              </div>
            </div>
            <p class="clearfix">
              	<%--<cherry:show domId="PLUPM0101QU">--%>
              		<%-- 用户信息查询按钮 --%>
              		<button class="right search" type="button" onclick="searchUser();return false;">
              			<span class="ui-icon icon-search-big"></span><span class="button-text"><s:text name="global.page.search"/></span>
              		</button>
              	<%--</cherry:show>--%>
            </p>
          
        </div>
        <div id="section" class="section hide">
          <div class="section-header">
          	<strong>
          		<span class="ui-icon icon-ttl-section-search-result"></span>
          		<s:text name="global.page.list"/>
          	</strong>
          </div>
          
          <div class="section-content">
            <table id="dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
              <thead>
                <tr>
                  <%-- 编号 --%>
                  <th><s:text name="upm01.number"/></th>
                  <%-- 用户账号--%>
                  <th><s:text name="upm01.loginName"/></th>
                  <%-- 员工代号 --%>
                  <th><s:text name="upm01.employeeCode"/></th>
                  <%-- 员工姓名 --%>
                  <th><s:text name="upm01.employeeName"/></th>
                  <%-- 有效区分 --%>
                  <th><s:text name="upm01.validFlag"/></th>
                  <%-- 操作 --%>
                  <th class="center"><s:text name="upm01_operate"/></th>
                </tr>
              </thead>           
             </table>
            </div>
           </div>
          </div>
          <div class="hide" id="dialogInit"></div>
           </cherry:form>
           <div class="hide">
     	     <div id="editDialogTitle"><s:text name="editDialogTitle"/></div>
             <div id="deleteUserTitle"><s:text name="delete_user" /></div>
             <div id="enableUserTitle"><s:text name="enable_user" /></div>
             <div id="dialogConfirm"><s:text name="dialog_confirm" /></div>
             <div id="dialogCancel"><s:text name="dialog_cancel" /></div>
             <div id="dialogClose"><s:text name="dialog_close" /></div>
             <div id="dialogInitMessage"><s:text name="dialog_init_message" /></div>
             <div id="deleteUserText"><p class="message"><span><s:text name="delete_user_message" /></span></p></div>
             <div id="enableUserText"><p class="message"><span><s:text name="enable_user_message" /></span></p></div>
           </div>
           <div id="savePage"></div>
       </s:i18n>
       <%-- ================== dataTable共通导入 START ======================= --%>
       <jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
       <%-- ================== dataTable共通导入    END  ======================= --%>
  
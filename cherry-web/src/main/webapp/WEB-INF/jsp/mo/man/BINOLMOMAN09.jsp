<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<link rel="stylesheet" href="/Cherry/css/cherry/combobox.css" type="text/css">
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/common/popDataTable.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/mo/man/BINOLMOMAN09.js"></script>
<style>
/* Firefox hack */
@-moz-document url-prefix(){td{height:26px;}}
</style>
<s:i18n name="i18n.mo.BINOLMOMAN09">
    <div class="hide">
        <s:url id="search_url" value="/mo/BINOLMOMAN09_search"/>
        <a id="searchUrl" href="${search_url}"></a>
    </div>
    <div class="hide">
        <s:url id="add_url" value="/mo/BINOLMOMAN09_add"/>
        <a id="addUrl" href="${add_url}"></a>
    </div>
     <div class="hide">
        <s:url id="editPaper_url" value="/mo/BINOLMOMAN09_edit"/>
        <a id="editPaper" href="${editPaper_url}"></a>
    </div>
    <div class="hide">
        <s:url id="save_url" value="/mo/BINOLMOMAN09_save"/>
        <a id="saveUrl" href="${save_url}"></a>
    </div>
      <%-- ================== 错误信息提示 START ======================= --%>
    <div id="errorMessage"></div>    
    <div style="display: none" id="errorMessageTemp">
    <div class="actionError">
       <ul><li><span><s:text name="MAN09_deleteError" /></span></li></ul>         
    </div>
    </div>
    <%-- ================== 错误信息提示   END  ======================= --%>
      <div class="panel-header">
        <%-- --%>
        <div class="clearfix">
	    <span class="breadcrumb left">	    
			<jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
		</span>
		  <cherry:show domId="BINOLMOMAN0901">
       	<a class="add right" id="addPaper" target="_blank">
	        <span class="ui-icon icon-add"></span><span class="button-text"><s:text name="MAN09_add"/></span>
	    </a>
	    </cherry:show>
        </div>
      </div>
      <div class="panel-content">
        <div class="box">
          <cherry:form id="mainForm" class="inline" onsubmit="BINOLMOMAN09.search(); return false;">
          <%-- IE只有一个text框 ，按 Enter键跳转其他页面hack --%>
          <input class="hide"/>
            <div class="box-header">
            <strong>
                <span class="ui-icon icon-ttl-search"></span>
                <%-- 查询条件 --%>
                <s:text name="MAN09_condition"/>
            </strong>
            </div>
            <div class="box-content clearfix">
              <div class="column" style="width:50%;">
              <p>
                <%-- 菜单编号 --%>
                    <label style="width:80px;"><s:text name="MAN09_menuCodee"/></label>
                    <s:textfield name="menuCode" cssClass="text" maxlength="30"/>
              </p>
              <p>
                <%-- 菜单类型 --%>
                    <label style="width:80px;"><s:text name="MAN09_menuType"/></label>
                    <s:textfield name="menuType" cssClass="text" maxlength="30"/>
              </p>
              </div>
              <div class="column last" style="width:49%;">
                 <p>
                 <%-- 菜单连接地址 --%>
                 <label style="width:80px;"><s:text name="MAN09_menuLink" /></label>
                    <s:textfield name="menuLink" cssClass="text" maxlength="30"/>
                 </p>
              </div>
            </div>
            <p class="clearfix">
                    <button class="right search" type="submit" onclick="BINOLMOMAN09.search();return false;">
                        <span class="ui-icon icon-search-big"></span>
                        <%-- 查询 --%>
                        <span class="button-text"><s:text name="MAN09_search"/></span>
                    </button>
            </p>
          </cherry:form>
        </div>
        <div id="section" class="section hide">
          <div class="section-header">
          <strong>
            <span class="ui-icon icon-ttl-section-search-result"></span>
            <%-- 查询结果一览 --%>
            <s:text name="MAN09_results_list"/>
         </strong>
        </div>
        
        <div class="section-content">
            <div class="toolbar clearfix">
              <span class="left">    
                <s:url action="BINOLMOMAN09_delete" id="delUrl"></s:url>
                <cherry:show domId="BINOLMOMAN0902">
                <a class="delete" onclick="BINOLMOMAN09.delete('${delUrl}');return false;">
                   <span class="ui-icon icon-disable"></span>
                   <span class="button-text"><s:text name="global.page.delete"/></span>
                </a>
                </cherry:show>        
              </span>
                <span class="right"><a class="setting"><span class="ui-icon icon-setting"></span>
                <span class="button-text">
                <%-- 设置列 --%>
                <s:text name="MAN09_colSetting"/>
                </span></a></span>
            </div>
            <table id="dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
              <thead>
                <tr>
               	  <th><input type="checkbox" id="checkAll" onclick="BINOLMOMAN09.checkRecord(this,'#dataTable');"/></th>
               	  <th><s:text name="MAN09_num"/></th><%-- No. --%>
                  <th><s:text name="MAN09_menuCodee"/></th><%-- 菜单编号  --%>
                  <th><s:text name="MAN09_menuType"/></th><%-- 菜单类型 --%>
                  <th><s:text name="MAN09_menuLink"/></th><%-- 菜单连接地址 --%>
                  <th><s:text name="MAN09_Comment"/></th><%-- 说明 --%>
                  <th><s:text name="MAN09_IsLeaf"/></th><%-- 是否终结点 --%>
				  <th><s:text name="MAN09_operate"/></th><%-- 操作 --%> 
                </tr>
              </thead>
            </table>
          </div>
       </div>
    </div>
    <div class="hide" id="dialogInit"></div>
 	<div class="hide">
    <div id="updateTitle"><s:text name="MAN09_updateTitle" /></div>
    <div id="addTitle"><s:text name="MAN09_addTitle" /></div>
    <div id="confirm"><s:text name="MAN09_yes" /></div>
    <div id="cancel"><s:text name="MAN09_cancel" /></div>
    <div id="deleteTitle"><s:text name="MAN09_deletetitle" /></div>
	<div id="deleteMessage"><p class="message"><span><s:text name="MAN09_deletemessage" /></span></p></div>
    <div id="dialogConfirm"><s:text name="global.page.ok" /></div>
    <div id="dialogCancel"><s:text name="global.page.cancle" /></div>
    <div id="dialogClose"><s:text name="global.page.close" /></div>
    <div id="dialogDetail" class="hide">
	<div class="box2-active">
		<div class="box2 box2-content ui-widget" >
			<div style="word-break:break-all;word-wrap:break-word;">
				<label style="display: block;margin-right: 0px;" id="dialogContent"></label>
			</div>
		</div>
	</div>  
	</div> 
 	</div>
 </s:i18n>
<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<link rel="stylesheet" href="/Cherry/css/cherry/combobox.css" type="text/css">
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<link rel="stylesheet" href="/Cherry/plugins/zTree/zTreeStyle/zTreeStyle.css" type="text/css" media="screen, projection">
<script type="text/javascript" src="/Cherry/plugins/zTree/jquery.ztree-2.6.min.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/mo/man/BINOLMOMAN10.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery.layout.min.js"></script>
<script type="text/javascript" src="/Cherry/js/bs/common/BINOLBSCOM01.js"></script>
<style>
/* Firefox hack */
@-moz-document url-prefix(){td{height:26px;}}
</style>
<s:i18n name="i18n.mo.BINOLMOMAN10">
    <div class="hide">
        <s:url id="search_url" value="/mo/man/BINOLMOMAN10_loadTree"/>
        <a id="searchUrl" href="${search_url}"></a>
        <s:url id="save_url" value="/mo/man/BINOLMOMAN10_save"/>
        <a id="save_url" href="${save_url}"></a>
         <s:url id="edit_url" value="/mo/man/BINOLMOMAN10_edit"/>
        <a id="edit_url" href="${edit_url}"></a>
        <s:url id="add_url" value="/mo/man/BINOLMOMAN10_addMenu"/>
        <a id="add_url" href="${add_url}"></a>
    </div>
      <div class="panel-header">
        <%-- --%>
        <div class="clearfix">
	    <span class="breadcrumb left">	    
			<jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
		</span>
            <span class="right">
            	<div  style="display:none;" id="addPosMenuBrand">
            		<a id="addBtn" class="delete right" onclick="BINOLMOMAN10.posAddMenu();return false;">
               		<span class="ui-icon icon-add"></span>
               		<span class="button-text"><s:text name="MAN10_createMenu"/></span>
              	 	</a>
              	 </div>
            </span>
        </div>
          <div id="actionResultDisplay"></div>
      <div id="errorMessage"></div>
      <%-- ================== 错误信息提示 START ======================= --%>
      <div id="errorDiv2" style="display:none">
        <div class="actionError">
          <ul>
             <li><span id="errorSpan2">111111</span></li>
          </ul>
        </div>
      </div>
      <%-- ================== 错误信息提示   END  ======================= --%>
      </div>
      <div id="actionResultDisplay"></div>
      <cherry:form id="mainForm" class="inline" onsubmit="BINOLMOMAN10.search(); return false;">
		<div id="treeLayoutDiv" class="div-layout-all">
		<div class="ui-layout-west">
		<div style="min-width: 190px;">
		  <dl><div class="jquery_tree treebox_left tree" id = "treeDemo1" style="overflow: auto;background:#FCFCFC;"></dl>
		</div>
		</div>
		<div class="ui-layout-center">
		<div style="min-width: 438px;" id="employeeInfo">
	<div  style="display:none;" id="posMenuBrand">
		<table width="80%" class="detail" border="1" cellspacing="0" cellpadding="0" >
		  <tr>
		    <th><s:text name="MAN10_menuName"/></th>
		    <td>
		    <input style="width:30%;"  class="left" disabled= "disabled " name="BrandMenuName" id="BrandMenuName" value=""/>
		    <input maxlength="50"  class="left" style="display:none;width:30%;" name="BrandMenuNames" id="BrandMenuNames" value=""/>
		    <span id="editButton">
		          <a style="cursor:pointer;width:30%;" onclick="BINOLMOMAN10.openEdit();return false">
			             <span class="button-text"><s:text name="global.page.edit"/></span>
			      </a>
			 </span>
			 <span id="saveButton">
			   	  <a class="left" style="cursor:pointer;margin-top:1px" onclick="BINOLMOMAN10.save();return false;">
	             	 <span style="font-size:12px"><s:text name="global.page.save"/></span>
	              </a>
	              <span> | </span>
				  <a class="left" style="cursor:pointer;margin-top:1px" class="close" type="button"  onclick="BINOLMOMAN10.doClose();return false;">
		           <span  style="font-size:12px"><s:text name="global.page.cancle"/></span>
				 </a>
			 </span>
		    </td>
		  </tr>
		  <tr>
		    <th><s:text name="MAN10_menuContainer"/></th>
		    <td><input style="width:30%;" disabled= "disabled " name="Container" id="Container"/></td>
		  </tr>
		  <tr>
		    <th><s:text name="MAN10_menuScreen"/></th>
		    <td><input style="width:30%;"  disabled= "disabled "  Type= "test "  id="MenuValue" name="MenuValue"/></td>
		  </tr>
		</table>
		<input type="hidden" name="posMenuBrandID" id="posMenuBrandID" value=""/>
		<input type="hidden" name="treeId" id="treeId" value=""/>
	<div id="saveButton" class="center clearfix" style="display:none;" >
	    <div class="hide" id="dialogInit"></div>
	     	<div class="hide">
 </div>
	</div>
	</div>
		</div>
		</div>
		</div>
	 </cherry:form>
 </s:i18n>
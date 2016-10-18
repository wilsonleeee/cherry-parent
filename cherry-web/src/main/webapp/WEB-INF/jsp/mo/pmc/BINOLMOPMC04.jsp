<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<link rel="stylesheet" href="/Cherry/css/cherry/combobox.css" type="text/css">
<link rel="stylesheet" href="/Cherry/plugins/zTree/zTreeStyle/zTreeStyle.css" type="text/css" media="screen, projection">
<script type="text/javascript" src="/Cherry/plugins/zTree/jquery.ztree-2.6.min.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/mo/pmc/BINOLMOPMC04.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery.layout.min.js"></script>
<script type="text/javascript" src="/Cherry/js/bs/common/BINOLBSCOM01.js"></script>
<style>
/* Firefox hack */
@-moz-document url-prefix(){td{height:26px;}}
</style>
<s:i18n name="i18n.mo.BINOLMOPMC04">
	<cherry:form id="mainForm" class="inline">
	    <div class="hide">
	    	<!-- 加载树 -->
	        <s:url id="search_url" value="/mo/pmc/BINOLMOPMC04_loadTree"/>
	        <a id="searchUrl" href="${search_url}"></a>
	        <!-- 勾选状态 -->
	        <s:url id="editMenuShow_url" value="/mo/pmc/BINOLMOPMC04_editMenuShow"/>
	        <a id="editMenuShow_url" href="${editMenuShow_url}"></a>
			<!-- 创建菜单管理-->
	        <s:url id="create_url" value="/mo/pmc/BINOLMOPMC04_createMenu"/>
	        <a id="create_url" href="${create_url}"></a>
	        <!-- 菜单详细信息初始化-->
	        <s:url id="menuDetailInit_url" value="/mo/pmc/BINOLMOPMC04_menuDetailInit"/>
	        <a id="menuDetailInitUrl" href="${menuDetailInit_url}"></a>
	    </div>
        <div class="panel-header">
	        <%-- --%>
	        <div class="clearfix">
			    <span class="breadcrumb left">	    
					<jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
				</span>
	        </div>
        </div>
        <div class="panel-header">
	      	<div class="clearfix">
		      	<span><s:text name="PMC04_machineType" /></span>
		      	<span><s:text name="PMC04_select" id="PMC04_select"/></span>
		        	<s:select name="machineType" list='#application.CodeTable.getCodes("1284")' 
		           listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{#PMC04_select}" 
		           onchange="BINOLMOPMC04.posMenu();return false;"/>
		        <cherry:show domId="BINOLMOPMC04CRT">
		           <span class="right" style="display:none;" id="createMenuBrand">
		          		<a id="addBtn" class="delete right" onclick="BINOLMOPMC04.createMenu();return false;">
		             		<span class="ui-icon icon-add"></span>
		             		<span class="button-text"><s:text name="PMC04_createMenu"/></span>
		            	</a>
		           </span>
		           </cherry:show>
		           <cherry:show domId="BINOLMOPMC04ADD">
		           	<s:url id="addMenu_init" action="BINOLMOPMC04_addMenuInit"/>
			       	<a class="add right" id="addPosMenu" onclick="BINOLMOPMC04.popAddMenu('${addMenu_init}');return false;">
				        <span class="ui-icon icon-add"></span><span class="button-text"><s:text name="PMC04_addPosMenu"/></span>
				    </a>
			    </cherry:show>
		    </div>
        </div>
      	<div id="errorMessage"></div>
      	<%-- ================== 错误信息提示 START ======================= --%>
        <div id="errorDiv2" style="display:none">
	        <div class="actionError">
	          <ul>
	             <li><span id="errorSpan2"></span></li>
	          </ul>
	        </div>
        </div>
        <%-- ================== 错误信息提示   END  ======================= --%>
      	<div id="actionResultDisplay"></div>
		<div id="treeLayoutDiv" class="div-layout-all">
		<div class="ui-layout-west">
		<div style="min-width: 190px;">
		  <!-- 定位 -->
		  <div class="treebox2-header">
			<input id="locationPosition" class="text" type="text" name="locationPosition" style="width: 100px;margin-right: 0;vertical-align: middle;"/>
			<a class="search" onclick="BINOLMOPMC04.locateMenu(this);return false;">
				<span class="ui-icon icon-position"></span>
				<span class="button-text"><s:text name="global.page.locationPosition"/></span>
			</a>
		  </div>
		  <dl><dt class="jquery_tree treebox_left tree" id="menuBrandTree" style="overflow: auto;background:#FCFCFC;"></dt></dl>
		</div>
		</div>
		<div class="ui-layout-center">
		<div style="min-width: 570px;" id="posMenuDetail"></div>
		</div>
		</div>
	 </cherry:form>
<div class="hide">
	<div id="disableTitle"><s:text name="PMC04_disableTitle" /></div>
	<div id="enableTitle"><s:text name="PMC04_enableTitle" /></div>
	<div id="editShowTitle"><s:text name="PMC04_editShowTitle" /></div>
	<div id="disableMessage"><p class="message"><span><s:text name="PMC04_disableMessage" /></span></p></div>
	<div id="enableMessage"><p class="message"><span><s:text name="PMC04_enableMessage" /></span></p></div>
	<div id="editShowMessage"><p class="message"><span><s:text name="PMC04_editShowMessage" /></span></p></div>
	<div id="editDialogTitle"><s:text name="PMC04_edit"/></div>
	<div id="modifyConfigTitle"><s:text name="PMC04_modifyDate" /></div>
	<div id="dialogConfirm"><s:text name="global.page.ok"/></div>
	<div id="dialogCancel"><s:text name="global.page.cancle"/></div>
	<div id="dialogClose"><s:text name="global.page.close" /></div>
	<div id="showMenuLinkTitle"><s:text name="PMC04_menuLink" /></div>
	<!-- 菜单链接详细 -->
    <div id="menuLinkDetail" class="hide">
		<div class="box2-active">
			<div class="box2 box2-content ui-widget" >
				<div style="word-break:break-all;word-wrap:break-word;">
					<label style="display: block;margin-right: 0px;" id="dialogContent"></label>
				</div>
			</div>
		</div>
	</div>  
</div>
<div class="hide" id="dialogInit"></div>
 </s:i18n>
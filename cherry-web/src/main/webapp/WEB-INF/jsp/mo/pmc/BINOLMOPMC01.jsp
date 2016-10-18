<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<link rel="stylesheet" href="/Cherry/css/cherry/combobox.css" type="text/css">
<link rel="stylesheet" href="/Cherry/plugins/zTree v3.1/css/zTreeStyle/zTreeStyle.css" type="text/css" media="screen, projection">
<script type="text/javascript" src="/Cherry/plugins/zTree v3.1/js/jquery.ztree.all-3.1.js"></script>
<script type="text/javascript" src="/Cherry/js/mo/pmc/BINOLMOPMC01.js"></script>
<script type="text/javascript" src="/Cherry/js/common/popDataTable.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>

<style>
	.treebox_left{
		background:none repeat scroll 0 0 #FFFFFF;
		border:1px solid #FAD163;
		height:420px;
		border-color:#D6D6D6 #D6D6D6 
	}
	.tree_out{
		padding: 2em;
	}
</style>
<script type="text/javascript">
    // 节日
    var holidays = '${holidays }';
    $('#startPublishDate').cherryDate({
        holidayObj: holidays,
        beforeShow: function(input){
            var value = $('#endPublishDate').val();
            return [value,'maxDate'];
        }
    });
    $('#endPublishDate').cherryDate({
        holidayObj: holidays,
        beforeShow: function(input){
            var value = $('#startPublishDate').val();
            return [value,'minDate'];
        }
    });
</script>

<s:i18n name="i18n.mo.BINOLMOPMC01">
<div class="hide">
    <s:url id="search_url" value="/mo/BINOLMOPMC01_search"/>
    <a id="searchUrl" href="${search_url}"></a>
    <s:url id="addMenuGrpInitUrl" action="BINOLMOPMC01_addMenuGrpInit" />
    <s:url id="addMenuGrp_url" value="/mo/BINOLMOPMC01_addMenuGrp"/>
    <a id="addMenuGrpUrl" href="${addMenuGrp_url}"></a>
    <s:url id="updateMenuGrp_url" action="BINOLMOPMC01_updateMenuGrp"></s:url>
    <a id="updateMenuGrpUrl" href="${updateMenuGrp_url}"></a>
    <!-- 复制菜单组及其菜单配置 -->
    <%-- <s:url id="copyMenuGrpAndConfig_url" action="BINOLMOPMC01_copyMenuGrpAndConfig"></s:url>
    <a id="copyMenuGrpAndConfigUrl" href="${copyMenuGrpAndConfig_url}"></a> --%>
    <!-- 按渠道查看发布 -->
    <s:url id="getPublishChannelUrl" action="BINOLMOPMC01_getPublishChannel"></s:url>
    <a id="getPublishChannel_url" href="${getPublishChannelUrl}"></a>
</div>
<div class="panel-header">
	<div class="clearfix">
		<span class="breadcrumb left">	    
			<jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
		</span>
	    <%-- 添加按钮 --%>
		<span class="right"> 
		<a href="${addMenuGrpInitUrl}" class="add" id="addMenuGrpButton" onclick="BINOLMOPMC01.popAddMenuGrp(this);return false;">
       	<span class="button-text"><s:text name="PMC01_addMenuGrp"/></span>
       	<span class="ui-icon icon-add"></span>
       	</a>
		</span>  
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
	<div class="panel-content">
        <div class="box">
		<cherry:form id="mainForm" class="inline">
            <div class="box-header"></div>
            <div class="box-content clearfix">
              <div class="column" style="width:49%">
                <p>
                  <label style="width:100px;"><s:text name="PMC01_menuGrpName" /></label>
                  <input name="menuGrpNameSearch" type="text" class="text" />
                </p>
                <p>
                	<label style="width:100px;"><s:text name="PMC01_machineType" /></label>
                	<s:text name="PMC01_select" id="PMC01_select"/>
                   	<s:select name="machineType" 
                      list='#application.CodeTable.getCodes("1284")' 
                      listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{PMC01_select}" cssStyle="width:150px;"/>
                </p>
              </div>
              <div class="column last" style="width:50%">
                 <p class="date">
                 <%-- 发布日期--%>
                     <label><s:text name="PMC01_publishDate"></s:text></label>
                     <span><s:textfield id="startPublishDate" name="startPublishDate" cssClass="date"/></span>
                     - 
                     <span><s:textfield id="endPublishDate" name="endPublishDate" cssClass="date"/></span>
                 </p> 
             </div>
            </div>
            <p class="clearfix">
              <button class="right search" type="button" onclick="BINOLMOPMC01.search();return false;">
         		<span class="ui-icon icon-search-big"></span><span class="button-text"><s:text name="PMC01_search" /></span>
              </button>
            </p>
		</cherry:form>
        </div>
        <div id="section" class="section hide">
     		<div class="section-header">
     		<strong>
     			<span class="ui-icon icon-ttl-section-search-result"></span>
     			<s:text name="global.page.list"/>
    		</strong>
   			</div>
          <div class="section-content">
          <div id ="ui-tabs" class="ui-tabs">
          	<ul class ="ui-tabs-nav clearfix">
	          <%--进行中--%>
	          <li id="tab1_li" class="ui-tabs-selected" onclick = "BINOLMOPMC01.menuGrpDateFilter('in_progress',this);"><a><s:text name="PMC01_in_progress"/></a></li>
	          <%--已过期--%>
	          <li id="tab2_li" onclick = "BINOLMOPMC01.menuGrpDateFilter('past_due',this);"><a><s:text name="PMC01_past_due"/></a></li>
	          <%--未开始--%>
	          <li id="tab3_li" onclick = "BINOLMOPMC01.menuGrpDateFilter('not_start',this);"><a><s:text name="PMC01_not_start"/></a></li>
	          <%--其他--%>
	          <li id="tab4_li"  onclick = "BINOLMOPMC01.menuGrpDateFilter('not_release',this);"><a><s:text name="PMC01_not_release"/></a></li>
	       	</ul>
	       	<div id="tabs-1" class="ui-tabs-panel" style="overflow-x:auto;overflow-y:hidden;">
	            <div class="toolbar clearfix">
                    <span class="right">
                        <a class="setting">
                            <span class="ui-icon icon-setting"></span>
                            <span class="button-text">
                                <%-- 设置列 --%>
                                <s:text name="PMC01_colSetting"/>
                            </span>
                        </a>
                    </span>
                </div>
	            <table id="dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%" >
	              <thead>
	                <tr>
	                  <th><s:text name="PMC01_number" /></th>
	                  <th class="center"><s:text name="PMC01_menuGrpName" /></th>
	                  <th class="center"><s:text name="PMC01_startDate" /></th>
	                  <th class="center"><s:text name="PMC01_endDate" /></th>
	                  <th class="center"><s:text name="PMC01_publishDate" /></th>
	                  <th class="center"><s:text name="PMC01_machineType" /></th>
	                  <th class="center"><s:text name="PMC01_operation" /></th>
	                </tr>
	              </thead>
	          	</table>
          	</div>
          </div>
         </div>
      </div>
	</div>
<div class="hide" id="dialogInit"></div>

<div class="hide">
<div id="addMenuGrpTitle"><s:text name="PMC01_addMenuGrp" /></div>
<div id="updateMenuGrpTitle"><s:text name="PMC01_eidt" /></div>
<div id="copyMenuGrpTitle"><s:text name="PMC01_copy" /></div>
<div id="deleteMenuGrpTitle"><s:text name="PMC01_delete" /></div>
<div id="menuGrpConfigTitle"><s:text name="PMC01_config" /></div>
<div id="saveSuccessTitle"><s:text name="PMC01_save_success" /></div>
<div id="selMode1"><s:text name="global.page.selMode1"/></div>
<div id="selMode2"><s:text name="global.page.selMode2"/></div>
<div id="selMode3"><s:text name="global.page.selMode3"/></div>
<div id="select_default"><s:text name="global.page.select" /></div>
<div id="channelRegion"><s:text name="global.page.channelRegion"/></div>
<div id="dialogConfirm"><s:text name="global.page.ok" /></div>
<div id="dialogCancel"><s:text name="global.page.cancle" /></div>
<div id="dialogClose"><s:text name="global.page.close" /></div>
<div id="doRegion"><s:text name="PMC01_doRegion" /></div>
	      <div id="publisCounter"><s:text name="PMC01_publisCounter" /></div>
<div id="dialogContent">
<p class="message"><span><s:text name="PMC01_confirmDelete"></s:text></span>
</div>
</div>
<div id="pop">
	<div id="region">
	</div>
</div>
</s:i18n>
<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>
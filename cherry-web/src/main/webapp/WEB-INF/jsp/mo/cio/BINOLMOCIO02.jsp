<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<link rel="stylesheet" href="/Cherry/css/cherry/combobox.css" type="text/css">
<link rel="stylesheet" href="/Cherry/plugins/zTree v3.1/css/zTreeStyle/zTreeStyle.css" type="text/css" media="screen, projection">
<script type="text/javascript" src="/Cherry/plugins/zTree v3.1/js/jquery.ztree.all-3.1.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/mo/cio/BINOLMOCIO02.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript">
	//节日
	var holidays = '${holidays }';
	$('#startDate').cherryDate({
		holidayObj: holidays,
		beforeShow: function(input){
			var value = $('#endDate').val();
			return [value,'maxDate'];
		}
	});
	$('#endDate').cherryDate({
		holidayObj: holidays,
		beforeShow: function(input){
			var value = $('#startDate').val();
			return [value,'minDate'];
		}
	});
</script>
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
<%--问卷查询url --%>
<s:url id="search_url" value="/mo/BINOLMOCIO02_search"/>
<%--问卷新增url --%>
<s:url id="addInit_url" value="/mo/BINOLMOCIO03_init"/>
<s:url id="editOrCopyInit_url" value="/mo/BINOLMOCIO05_init"/>
<s:url id="issuedInit_url" value="/mo/BINOLMOCIO06_init"/>
<s:url id="paperDisableOrEnable_url" value="/mo/BINOLMOCIO02_paperDisableOrEnable"/>
<s:url id="paperDelete_url" value="/mo/BINOLMOCIO02_paperDelete"/>
<s:url id="isExistSomePaper_url" value="/mo/BINOLMOCIO02_isExistSomePaper"/>
<s:url id="getPaperIssum_url" value="/mo/BINOLMOCIO02_getPaperIssum"/>

<s:i18n name="i18n.mo.BINOLMOCIO02">
<%-- ================== js国际化  START ======================= --%>
<jsp:include page="/WEB-INF/jsp/mo/cio/BINOLMOCIO02_2.jsp" flush="true" />
<%-- ================== js国际化导入    END  ======================= --%>
<div id="CIO02_select" class="hide"><s:text
		name="global.page.all" /></div> 
<div class="panel-header">
     	<%-- ###问卷查询 --%>
       <div class="clearfix"> 
		<span class="breadcrumb left">	    
			<jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
		</span>
       	<cherry:show domId="BINOLMOCIO0201">
       	<a class="add right" id="addPaper" target="_blank">
	        <span class="ui-icon icon-add"></span><span class="button-text"><s:text name="CIO02_addPaper"/></span>
	    </a>
	    </cherry:show>
       </div>
</div>
<div id="actionResultDisplay"></div>
<div id="errorMessage"></div>    
<%-- ================== 错误信息提示 START ======================= --%>
 <div style="display: none" id="errorMessageTemp1">
    <div class="actionError">
       <ul><li><span><s:text name="CIO02_noCheckedWarm"/></span></li></ul>         
    </div>
</div>
<div style="display: none" id="errorMessageTemp2">
    <div class="actionError">
       <ul><li><span><s:text name="CIO02_errCheckedWarm"/></span></li></ul>         
    </div>
</div>
<div style="display: none" id="errorMessageTemp3">
    <div class="actionError">
       <ul><li><span><s:text name="CIO02_onlyOneChecked"/></span></li></ul>         
    </div>
</div>
<div style="display: none" id="existMember">
    <div class="actionError">
       <ul><li><span><s:text name="CIO02_existMember"/></span></li></ul>         
    </div>
</div>
<%-- ================== 错误信息提示   END  ======================= --%>
<%-- ================== 弹出datatable -- 促销产品共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/popProductTable.jsp" flush="true" />
<%-- ================== 弹出datatable -- 促销产品共通导入 START ======================= --%>
<div class="panel-content">
	<div class="box">
		<cherry:form id="mainForm" class="inline" >
			<div class="box-header">
           		<strong>
           			<span class="ui-icon icon-ttl-search"></span><s:text name="global.page.condition"/>
           		</strong>
            </div>
            <div class="box-content clearfix">
               	<div class="column" style="width:49%;">
               		<p>
                	<%-- 所属品牌 --%>
                	<s:if test="%{brandInfoList.size()> 1}">
                    	<label><s:text name="CIO02_brand"></s:text></label>
                    	<s:text name="CIO02_select" id="CIO02_select"/>
                    	<s:select name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" headerKey="" headerValue="%{#CIO02_select}" cssStyle="width:100px;"></s:select>
                	</s:if><s:else>
                		<label><s:text name="CIO02_brand"></s:text></label>
                    	<s:text name="CIO02_select" id="CIO02_select"/>
                    	<s:select name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" cssStyle="width:100px;"></s:select>      	
                	</s:else>
                	</p>

	               	<p>
	               		<%--问卷类型--%>
	               		<label><s:text name="CIO02_paperType"/></label>
	               		<s:text name="global.page.all" id="CIO02_selectAll"/>
	               		<s:select name="paperType" list="#application.CodeTable.getCodes('1107')"
               				listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{CIO02_selectAll}"/>
	               	</p>
		        </div>      
        		<div class="column last" style="width:50%;">
        			<p>
	               		<%-- 问卷名称 --%>
	                  	<label><s:text name="CIO02_paperName"/></label>
	                  	<s:textfield name="paperName" cssClass="text" maxlength="50"/>
	                </p>
        			<p class="date">
	                	<%-- 发布日期--%>
	                  	<label><s:text name="CIO02_publishTime"/></label>
	                  	<span><s:textfield id="startDate" name="startDate" cssClass="date"/></span>
	                  	- 
	                  	<span><s:textfield id="endDate" name="endDate" cssClass="date"/></span>
	                </p>        
	            </div>
              </div>
              <p class="clearfix">
           			<%-- 查询 --%>
           			<button class="right search" type="submit" onclick="binOLMOCIO02_search('<s:property value="#search_url"/>'); return false;" id="searchBut">
           				<span class="ui-icon icon-search-big"></span>
           				<span class="button-text"><s:text name="global.page.search"/></span>
           			</button>
          		</p> 
          		</cherry:form> 
          </div>
	<%-- ====================== 结果一览开始 ====================== --%>
	<div id="section" class="section">
		<div class="section-header">
			<strong> 
			<span class="ui-icon icon-ttl-section-search-result"></span> 
			<s:text name="global.page.list" />
		 	</strong>
		</div>
		<div class="section-content">
		<%-- tab start --%>
		<div id ="ui-tabs" class="ui-tabs">
		  	<ul class ="ui-tabs-nav clearfix">
	            <%--进行中--%>
	            <li id="tab2_li" class="ui-tabs-selected" onclick = "prmActDateFilter('in_progress',this);"><a><s:text name="CIO02_in_progress"/></a></li>
	           	<%--已过期--%>
	            <li id="tab4_li" onclick = "prmActDateFilter('past_due',this);"><a><s:text name="CIO02_past_due"/></a></li>
	            <%--未开始--%>
	            <li id="tab3_li" onclick = "prmActDateFilter('not_start',this);"><a><s:text name="CIO02_not_start"/></a></li>
	            <%--其他--%>
	            <%-- <li id="tab1_li"  onclick = "prmActDateFilter('not_release',this);"><a><s:text name="CIO02_not_release"/></a></li> --%>
          	</ul>
        <div id="tabs-1" class="ui-tabs-panel" style="overflow-x:auto;overflow-y:hidden;">
        <div class="toolbar clearfix">
	        <span class="right"> <%-- 设置列 --%>
				<a class="setting"> 
					<span class="ui-icon icon-setting"></span> 
					<span class="button-text"><s:text name="global.page.colSetting" /></span> 
				</a>
			</span>
		</div>
		<table id="dataTable" cellpadding="0" cellspacing="0" border="0"
		class="jquery_table" width="100%">
		<thead>
			<tr>
				<%--选择 --%>
				<%-- <th><s:checkbox name="allSelect" id="checkAll" onclick="checkSelectAll(this)"/></th>--%>
				<!-- <th><input type="checkbox" name="allSelect" id="checkAll" onclick="checkSelectAll(this)"/></th> -->
				<%-- No. --%>
				<th class="center"><s:text name="No." /></th>
				<%-- 问卷名称 --%>
				<th class="center"><s:text name="CIO02_paperName" /></th>
				<%-- 问卷类型   --%>
				<th class="center"><s:text name="CIO02_paperType" /></th>
				<%-- 开始日期   --%>
				<th class="center"><s:text name="CIO02_startDate" /></th>
				<%-- 结束日期   --%>
				<th class="center"><s:text name="CIO02_endDate" /></th>
				<%-- 发布日期   --%>
				<th class="center"><s:text name="CIO02_publishTime" /></th>
				<%-- 发布人  --%>
				<th class="center"><s:text name="CIO02_publisher" /></th>
				<%-- 问卷状态   --%>
				<th class="center"><s:text name="CIO02_paperStatus" /></th>
				<%-- 操作 --%>
				<th class="center"><s:text name="CIO02_opration" /></th>
			</tr>
			
		</thead>
	</table>
	</div>
	</div>
	</div>
	</div>
	<%-- ====================== 结果一览结束 ====================== --%>
</div>
	  <div class="hide" id="dialogInit"></div>
      <div style="display: none;">
	      <div id="disableText"><p class="message"><span><s:text name="CIO02_disableText"/></span></p></div>
	      <div id="disableTitle"><s:text name="CIO02_disable"/></div>
	      <div id="enableTitle"><s:text name="CIO02_enable"/></div>
	      <div id="deleteTitle"><s:text name="CIO02_delete"/></div>
	      <div id="dialogConfirm"><s:text name="global.page.ok" /></div>
	      <div id="dialogCancel"><s:text name="global.page.cancle" /></div>
	      <div id="dialogClose"><s:text name="global.page.close" /></div>
	      <div id="doRegion"><s:text name="CIO02_doRegion" /></div>
	      <div id="publisCounter"><s:text name="CIO02_publisCounter" /></div>
      </div>
      <div id="ajaxResultMsg" class="hide">
        <s:text name="save_success_message" />
      </div>
      <div id="pop">
      	 <div id="region">
      	 </div>
      </div>
</s:i18n>
<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>
<span id="paperDisableOrEnable" style="display:none">${paperDisableOrEnable_url}</span>
<span id="search" style="display:none">${search_url}</span>
<span id="editOrCopyInit" style="display:none">${editOrCopyInit_url}</span>
<span id="addInit" style="display:none">${addInit_url}</span>
<span id="searchUrl" style="display:none">${search_url}</span>
<span id="issuedInit" style="display:none">${issuedInit_url}</span>
<span id="paperDelete" style="display:none">${paperDelete_url}</span>
<span id="isExistSomePaper" style="display:none">${isExistSomePaper_url}</span>
<span id="getPaperIssum" style="display:none">${getPaperIssum_url}</span>

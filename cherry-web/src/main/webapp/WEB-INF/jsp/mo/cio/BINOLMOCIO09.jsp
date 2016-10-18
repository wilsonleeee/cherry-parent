<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<link rel="stylesheet" href="/Cherry/css/cherry/combobox.css" type="text/css">
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/mo/cio/BINOLMOCIO09.js"></script>
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
	
	$("#mainForm").find("input").each(function(){
		$(this).keydown(function(e){
			var curKey = e.which; 
			if(curKey == 13){
					binOLMOCIO02_search();
					return false;
				}
		});
	});
</script>
<%--问卷查询url --%>
<s:url id="search_url" value="/mo/BINOLMOCIO09_search"/>
<s:url id="enableCheckPaper_url" value="/mo/BINOLMOCIO09_enableCheckPaper"/>
<s:url id="disableCheckPaper_url" value="/mo/BINOLMOCIO09_disableCheckPaper"/>
<s:url id="deleteCheckPaper_url" value="/mo/BINOLMOCIO09_deleteCheckPaper"/>
<s:url id="addPaper_url" value="/mo/BINOLMOCIO10_init"/>
<s:url id="editInit_url" value="/mo/BINOLMOCIO12_init"/>
<s:i18n name="i18n.mo.BINOLMOCIO09">
<div id="CIO09_select" class="hide"><s:text
		name="global.page.all" /></div> 
<div class="panel-header">
     	<%-- ###问卷查询 --%>
       <div class="clearfix"> 
		<span class="breadcrumb left">	    
			<jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
		</span>
       	<cherry:show domId="BINOLMOCIO0901">
       	<a class="add right" id="addPaper">
	        <span class="ui-icon icon-add"></span><span class="button-text"><s:text name="CIO09_addPaper"/></span>
	    </a>
	    </cherry:show>
       </div>
</div>
<div id="actionResultDisplay"></div>
<div id="errorMessage"></div>    
<%-- ================== 错误信息提示 START ======================= --%>
 <div style="display: none" id="errorMessageTemp1">
    <div class="actionError">
       <ul><li><span><s:text name="CIO09_noCheckedWarm"/></span></li></ul>         
    </div>
</div>
<div style="display: none" id="errorMessageTemp2">
    <div class="actionError">
       <ul><li><span><s:text name="CIO09_errCheckedWarm"/></span></li></ul>         
    </div>
</div>
<div style="display: none" id="errorMessageTemp3">
    <div class="actionError">
       <ul><li><span><s:text name="CIO09_tooMamey"/></span></li></ul>         
    </div>
</div>
<%-- ================== 错误信息提示   END  ======================= --%>
<%-- ================== 弹出datatable -- 促销产品共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/popProductTable.jsp" flush="true" />
<%-- ================== 弹出datatable -- 促销产品共通导入 START ======================= --%>
<div class="panel-content">
	<div class="box">
		<cherry:form id="mainForm" class="inline">
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
                    	<label><s:text name="CIO09_brand"></s:text></label>
                    	<s:text name="CIO09_select" id="CIO09_select"/>
                    	<s:select name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" headerKey="" headerValue="%{#CIO09_select}" cssStyle="width:100px;"></s:select>
                	</s:if><s:else>
                		<label><s:text name="CIO09_brand"></s:text></label>
                    	<s:text name="CIO09_select" id="CIO09_select"/>
                    	<s:select name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" cssStyle="width:100px;"></s:select>      	
                	</s:else>
                	</p>
	                <p>
        				<%--问卷状态 --%>
        				<label><s:text name="CIO09_paperStatus"/></label>
	               		<s:text name="global.page.all" id="CIO09_selectAll"/>
	               		<s:select name="paperStatus" list="#application.CodeTable.getCodes('1108')"
               				listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{CIO09_selectAll}"/>
        			</p>
	               	<p>
	               		<%--问卷权限--%>
	               		<label><s:text name="CIO09_paperRight"/></label>
	               		<s:text name="global.page.all" id="CIO09_selectAll"/>
	               		<s:select name="paperRight" list="#application.CodeTable.getCodes('1126')"
               				listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{CIO09_selectAll}"/>
	               	</p>
		        </div>      
        		<div class="column last" style="width:50%;">
        			<p>
	               		<%-- 问卷名称 --%>
	                  	<label><s:text name="CIO09_paperName"/></label>
	                  	<s:textfield name="paperName" cssClass="text" maxlength="50"/>
	                </p>
        			<p class="date">
	                	<%-- 日期范围 --%>
	                  	<label><s:text name="CIO09_Date"/></label>
	                  	<span><s:textfield id="startDate" name="startDate" cssClass="date"/></span>
	                  	- 
	                  	<span><s:textfield id="endDate" name="endDate" cssClass="date"/></span>
	                </p>        
	            </div>
              </div>
              <p class="clearfix">
           			<%-- 查询 --%>
           			<button class="right search" type="button" onclick="binOLMOCIO09_search('<s:property value="#search_url"/>')">
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
		<div class="toolbar clearfix">
			<span class="left" id="paperOption">
				<cherry:show domId="BINOLMOCIO0902">
                <a id="disableBtn" class="delete">
                <span class="ui-icon icon-disable"></span>
                <span class="button-text"><s:text name="CIO09_disable"/></span>
                </a>
                </cherry:show>
                <cherry:show domId="BINOLMOCIO0903">
                <a id="enableBtn" class="delete">
                <span class="ui-icon icon-enable"></span>
                <span class="button-text"><s:text name="CIO09_enable"/></span>
                </a>
                </cherry:show>
               	<cherry:show domId="BINOLMOCIO0905">
                <a id="deleteBtn" class="delete">
                <span class="ui-icon icon-delete"></span>
                <span class="button-text"><s:text name="CIO09_delete"/></span>
                </a>
                </cherry:show>
              </span>
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
				<th><s:checkbox name="allSelect" id="checkAll" onclick="checkSelectAll(this)"/></th>
				<%-- No. --%>
				<th><s:text name="No." /></th>
				<%-- 问卷名称 --%>
				<th class="center"><s:text name="CIO09_paperName" /></th>
				<%-- 问卷类型   --%>
				<th class="center"><s:text name="CIO09_paperRight" /></th>
				<%-- 问卷状态   --%>
				<th class="center"><s:text name="CIO09_paperStatus" /></th>
				<%-- 开始日期   --%>
				<th class="center"><s:text name="CIO09_startDate" /></th>
				<%-- 结束日期 --%>
				<th class="center"><s:text name="CIO09_endDate" /></th>
				<%-- 发布人  --%>
				<th class="center"><s:text name="CIO09_publisher" /></th>
				<%-- 发布时间  --%>
				<th class="center"><s:text name="CIO09_publishTime" /></th>
				<%-- 操作  --%>
				<th class="center"><s:text name="CIO09_opration" /></th>
			</tr>
		</thead>
	</table>
	</div>
	</div>
	<%-- ====================== 结果一览结束 ====================== --%>
</div>
	  <div class="hide" id="dialogInit"></div>
      <div style="display: none;">
      <div id="disableText"><p class="message"><span><s:text name="CIO09_disableText"/></span></p></div>
      <div id="disableTitle"><s:text name="CIO09_disable"/></div>
      <div id="enableTitle"><s:text name="CIO09_enable"/></div>
      <div id="deleteTitle"><s:text name="CIO09_delete"/></div>
      <div id="dialogConfirm"><s:text name="global.page.ok" /></div>
      <div id="dialogCancel"><s:text name="global.page.cancle" /></div>
      <div id="dialogClose"><s:text name="global.page.close" /></div>
      <div id="disableWarn"><p class="message"><span><s:text name="CIO09_confirIsDisable" /></span></p></div>
      <div id="enableWarn"><p class="message"><span><s:text name="CIO09_confirIsEnable" /></span></p></div>
      <div id="deleteWarn"><p class="message"><span><s:text name="CIO09_confirIsDelete" /></span></p></div>
      </div>
      <div id="ajaxResultMsg" class="hide">
        <s:text name="save_success_message" />
      </div>
</s:i18n>
<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>
<span id="search" style="display:none">${search_url}</span>
<span id="addInit" style="display:none">${addPaper_url}</span>
<span id="editInit" style="display:none">${editInit_url}</span>
<span id="enableCheckPaper" style="display:none">${enableCheckPaper_url}</span>
<span id="disableCheckPaper" style="display:none">${disableCheckPaper_url}</span>
<span id="deleteCheckPaper" style="display:none">${deleteCheckPaper_url}</span>
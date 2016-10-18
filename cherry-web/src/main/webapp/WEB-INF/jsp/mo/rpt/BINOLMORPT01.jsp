<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>

<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/mo/rpt/BINOLMORPT01.js"></script>
<script type="text/javascript">
$(document).ready(function() {
	// 节日
	var holidays = '${holidays }';
	$('#checkDateStart').cherryDate({holidayObj: holidays});
	$('#checkDateEnd').cherryDate({
		holidayObj: holidays,
		beforeShow: function(input){
			var value = $('#checkDateStart').val();
			return [value,'minDate'];
		}
	});
});
</script>

<s:i18n name="i18n.mo.BINOLMORPT01">
	<div class="hide">
	   <s:url id="export" action="BINOLMORPT01_export" ></s:url>
	   <a id="downUrl" href="${export}"></a>
	</div>
	<s:text name="global.page.select" id="select_default"/>
    <div class="panel-header">
	    <div class="clearfix"> 
	    <span class="breadcrumb left">	    
			<jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
		</span>
	    </div>
    </div>
<%-- ================== 错误信息提示 START ======================= --%>
    <div id="actionResultDisplay"></div>
	<div id="errorMessage"></div> 
    <div class="panel-content">
      <div class="box">
        <cherry:form id="mainForm" action="/mo/BINOLMORPT01_search" method="post" class="inline" onsubmit="binolmorpt01.search();return false;">
          <div class="box-header">
          	<strong><span class="ui-icon icon-ttl-search"></span><s:text name="global.page.condition"/></strong>
          </div>
          <div class="box-content clearfix">
            <div class="column" style="width:50%; height: auto;">
              <p>
              	<%-- 问卷名称 --%>
              	<label><s:text name="binolmorpt01.paperName"/></label>
                <s:textfield name="paperName" cssClass="text" onclick="binolmorpt01.popCheckPaper(this);"/>
                <s:hidden name="checkPaperId"></s:hidden>
              </p>
              <p class="date">
              	<%-- 考核时间  --%>
                <label><s:text name="binolmorpt01.checkDate"/></label>
                <span><s:textfield id="checkDateStart" name="checkDateStart" cssClass="date"/></span>- 
                <span><s:textfield id="checkDateEnd" name="checkDateEnd" cssClass="date"/></span>
              </p>
            </div>
            <div class="column last" style="width:49%; height: auto;">
              <p>
               	<%-- 柜台 --%>
               	<label><s:text name="binolmorpt01.departName"/></label>
               	<s:textfield name="departName" cssClass="text"/>
              </p>
              <p>
               	<%-- 回答人员 --%>
               	<label><s:text name="binolmorpt01.employeeName"/></label>
               	<s:textfield name="employeeName" cssClass="text"/>
              </p>
            </div>
          </div>
          <p class="clearfix">
         	<%-- 查询按钮 --%>
         	<button class="right search" onclick="binolmorpt01.search();return false;">
         		<span class="ui-icon icon-search-big"></span>
         		<span class="button-text"><s:text name="global.page.search"/></span>
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
          <div class="toolbar clearfix">
          <cherry:show domId="BINOLMORPT01EXP">
            <span class="left">
                <a id="export" class="export">
                    <span class="ui-icon icon-export"></span>
                    <span class="button-text"><s:text name="global.page.export"/></span>
                </a>
            </span>
         </cherry:show>
           	<span class="right">
           		<%-- 列设置按钮  --%>
           		<a href="#" class="setting">
           			<span class="button-text"><s:text name="global.page.colSetting"/></span>
    		 		<span class="ui-icon icon-setting"></span>
    		 	</a>
           	</span>
          </div>
          <table id="dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
            <thead>
              <tr>              
                <th><s:text name="binolmorpt01.number" /></th>
                <th><s:text name="binolmorpt01.paperName" /></th>
                <th><s:text name="binolmorpt01.departName" /></th>
                <th><s:text name="binolmorpt01.employeeName" /></th>
                <th><s:text name="binolmorpt01.ownerName" /></th>
                <th><s:text name="binolmorpt01.checkDate" /></th>
                <th><s:text name="binolmorpt01.totalPoint" /></th>
                <th class="center"><s:text name="binolmorpt01.detail" /></th>
              </tr>
            </thead>           
          </table>
        </div>
      </div>
    </div>
</s:i18n>

<%-- 考核答卷查询URL --%>
<s:url id="search_url" value="BINOLMORPT01_search"/>
<s:hidden name="search_url" value="%{search_url}"/> 
<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>
<%-- ================== 弹出datatable共通START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/popCheckPaperTable.jsp" flush="true" />
<%-- ================== 弹出datatable共通END ======================= --%>

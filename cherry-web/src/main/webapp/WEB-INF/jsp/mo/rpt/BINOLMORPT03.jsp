<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<link rel="stylesheet" href="/${CHERRY_CONTEXT_PATH}/css/cherry/combobox.css" type="text/css">
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/common/departBar.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/common/popDataTable.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/mo/rpt/BINOLMORPT03.js"></script>
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

<s:i18n name="i18n.mo.BINOLMORPT03">
    <div class="hide">
       <s:url id="export" action="BINOLMORPT03_export" ></s:url>
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
        <cherry:form id="mainForm" action="/mo/BINOLMORPT03_search" method="post" class="inline" onsubmit="binolmorpt03.search();return false;">
          <div class="box-header">
          	<strong><span class="ui-icon icon-ttl-search"></span><s:text name="global.page.condition"/></strong>
          </div>
          <div class="box-content clearfix">
            <div class="column" style="width:50%; height: auto;">
              <p>
              	<%-- 问卷名称 --%>
              	<label><s:text name="binolmorpt03.paperName"/></label>
                <s:textfield name="paperName" cssClass="text" onclick="binolmorpt03.popCheckPaper(this);"/>
                <s:hidden name="paperId"></s:hidden>
                <s:hidden name="checkPaperType"></s:hidden>
              </p>
              <p>
              	<%-- 问卷类型 --%>
              	<label><s:text name="binolmorpt03.paperType"/></label>
                <s:select id="paperType" name="paperType" list="#application.CodeTable.getCodes('1107')" listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{#select_default}"/>
              </p>
            </div>
            <div class="column last" style="width:49%; height: auto;">
              <%-- 柜台 --%>
              <%-- <p>
               	<label><s:text name="binolmorpt03.departName"/></label>
               	<s:textfield name="departName" cssClass="text"/>
              </p> --%>
              <p class="date">
              	<%-- 答卷时间  --%>
                <label><s:text name="binolmorpt03.answerDate"/></label>
                <span><s:textfield id="checkDateStart" name="checkDateStart" cssClass="date"/></span>- 
                <span><s:textfield id="checkDateEnd" name="checkDateEnd" cssClass="date"/></span>
              </p>
            </div>
            <%-- ======================= 组织联动共通导入开始  ============================= --%>
				<s:action name="BINOLCM13_query" namespace="/common" executeResult="true">
					<s:param name="businessType">A</s:param>
					<s:param name="operationType">1</s:param>
					<s:param name="mode">dpat,area,chan</s:param>
				</s:action>
			<%-- ======================= 组织联动共通导入结束  ============================= --%>
          </div>
          <p class="clearfix">
          	<span id="filterSpan" style="display:none;">
          	<!-- 过滤重复答卷规则 -->
          	<label class="red"><strong><s:text name="binolmorpt03.filterEchoRule"/></strong></label>
          	<!-- 不过滤 -->
          	<label>
			<input type="radio" value="0" name="showMode" id="showMode0" onclick="binolmorpt03.changeShowMode('0')" checked="checked" style="margin: 0 0.1em 0 0;"/>
			<s:text name="binolmorpt03.noFilter"></s:text>
			</label>
			<!-- 会员(会员问卷不会出现重复答卷的情况) -->
			<%-- <label>
			<input type="radio" value="1" name="showMode" id="showMode1" onclick="binolmorpt03.changeShowMode('1')" style="margin: 0 0.1em 0 0;"/>
			<s:text name="binolmorpt03.memberFilter"></s:text>
			</label> --%>
			<!-- BA -->
			<label>
			<input type="radio" value="1" name="showMode" id="showMode1" onclick="binolmorpt03.changeShowMode('1')" style="margin: 0 0.1em 0 0;"/>
			<s:text name="binolmorpt03.BAFilter"></s:text>
			</label>
			<!-- BA+柜台 -->
			<label>
			<input type="radio" value="2" name="showMode" id="showMode2" onclick="binolmorpt03.changeShowMode('2')" style="margin: 0 0.1em 0 0;"/>
			<s:text name="binolmorpt03.BAAndCntFilter"></s:text>
         	</label>
         	</span>
         	<%-- 查询按钮 --%>
         	<button class="right search" onclick="binolmorpt03.search();return false;">
         		<span class="ui-icon icon-search-big"></span><span class="button-text"><s:text name="global.page.search"/></span>
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
	      <div id="errorDiv2" class="actionError" style="display:none">
			      <ul>
			         <li><span id="errorSpan2"></span></li>
			      </ul>			
	      </div>
          <div class="toolbar clearfix">
          <cherry:show domId="BINOLMORPT03EXP">
            <span class="left">
                <a id="export" class="export">
                    <span class="ui-icon icon-export"></span>
                    <span class="button-text"><s:text name="global.page.export"/></span>
                </a>
                <!-- 查询结果所属问卷是否为同一张 -->
                <input id="paperCount" name="paperCount" class="hide"></input>
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
                <th><s:text name="binolmorpt03.number" /></th>
                <th><s:text name="binolmorpt03.paperName" /></th>
                <th><s:text name="binolmorpt03.paperType" /></th>
                <th><s:text name="binolmorpt03.departName" /></th>
                <th><s:text name="binolmorpt03.employeeName" /></th>
                <th><s:text name="binolmorpt03.answerDate" /></th>
                <th><s:text name="binolmorpt03.realTotalPoint" /></th>
                <th class="center"><s:text name="binolmorpt03.detail" /></th>
              </tr>
            </thead>           
          </table>
        </div>
      </div>
    </div>
</s:i18n>
<div id="errorMessage" style="display:none">
	<input type="hidden" id="errmsgESS00063" value='<s:text name="ESS00063"/>'/>
</div>
<%-- 考核答卷查询URL --%>
<s:url id="search_url" value="BINOLMORPT03_search"/>
<s:hidden name="search_url" value="%{search_url}"/> 
<%-- 答卷类型数量查询URL --%>
<s:url action="BINOLMORPT03_getPaperCount" id="paperCountUrl"></s:url>
<a href="${paperCountUrl }" id="paperCountUrl"></a>
<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>
<%-- ================== 弹出datatable共通START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/popPaperAnswerTable.jsp" flush="true" />
<%-- ================== 弹出datatable共通END ======================= --%>

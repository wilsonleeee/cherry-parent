<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<link rel="stylesheet" href="/Cherry/css/cherry/combobox.css" type="text/css">
<script type="text/javascript" src="/Cherry/js/bs/cha/BINOLBSCHA01.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/common/popDataTable.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<s:i18n name="i18n.bs.BINOLBSCHA01">
    <s:url action="BINOLBSCHA01_disable" id="disableChannel"></s:url>
    <s:url action="BINOLBSCHA01_enable" id="enableChannel"></s:url>
	<s:url id="search_url" value="/basis/BINOLBSCHA01_search"/>
	<s:url id="add_url" action="BINOLBSCHA04_init"/>
	<s:text name="CHA01_selectAll" id="CHA01_selectAll"/>
	<div class="hide">
		<a id="searchUrl" href="${search_url}"></a>
		<s:text name="CHA01_select" id="defVal"/>
		<div id="CHA01_select">${defVal}</div>
	</div>
	<div id="CIO02_select" class="hide"><s:text name="global.page.all" /></div> 
	      <div class="panel-header">
	      	<%-- 渠道一览 --%>
	        <div class="clearfix"> 
			<span class="breadcrumb left">	    
				<jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
			</span>
	            <%-- 添加按钮 --%>
       		    <span class="right"> 
       		    <cherry:show domId="BSCHA01ADD">
       	     	 	<a href="${add_url}" class="add" onclick="javascript:openWin(this);return false;">
       		 		<span class="button-text"><s:text name="CHA01_add"/></span>
       		 		<span class="ui-icon icon-add"></span>
       		 	</a>
       		    </cherry:show> 
       		    </span>  
	        </div> 
	      </div>
	     <%-- ================== 错误信息提示 START ======================= --%>
   			 <div id="errorMessage"></div>    
   			 <div style="display: none" id="errorMessageTemp">
   			 <div class="actionError">
   			    <ul><li><span><s:text name="EBS00017"/></span></li></ul>         
    		</div>
    	</div>
   		 <%-- ================== 错误信息提示   END  ======================= --%>
	      <div id="actionResultDisplay"></div>
	      <div class="panel-content">
	        <div class="box">
	          <cherry:form id="mainForm" class="inline">
	           <input type="hidden" id="currentUnitID" name="currentUnitID" value="BINOLBSCHA01"/>
	            <div class="box-header">
	            <strong>
	            	<span class="ui-icon icon-ttl-search"></span>
	            	<%-- 查询条件 --%>
	            	<s:text name="CHA01_condition"/>
	            </strong>
	            <input type="checkbox" name="validFlag" value="1"/><s:text name="CHA01_invalid"/>
	               </div>
	            <div class="box-content clearfix">
	            <%-- 总部或者办事处用户登录 --%>
	              <div class="column" style="width:50%;height:85px;">
	              <p>
                	<%-- 所属品牌 --%>
                	<s:if test="%{brandInfoList.size()> 1}">
                    	<label><s:text name="CHA01_brand"></s:text></label>
                    	<s:text name="CHA01_select" id="CHA01_select"/>
                    	<s:select name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" headerKey="" headerValue="%{#CHA01_select}" cssStyle="width:100px;"></s:select>
                	</s:if><s:else>
                		<label><s:text name="CHA01_brand"></s:text></label>
                    	<s:text name="CHA01_select" id="CHA01_select"/>
                    	<s:select name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" cssStyle="width:100px;"></s:select>      	
                	</s:else>
                	</p>
                	<p>
	               	<%-- 渠道代码 --%>
	                  <label><s:text name="CHA01_channelCode"/></label>
	                  <s:textfield name="channelCode" cssClass="text" maxlength="16" />
	                </p>
	                <p>
	               	<%-- 渠道名称 --%>
	                  <label><s:text name="CHA01_channelName"/></label>
	                  <s:textfield name="channelName" cssClass="text" maxlength="16" />
	                </p>
	              </div>
	              <div class="column last" style="width:49%;height:85px;">            
	               <p>
	               	<%-- 渠道类型 --%>
	                  <label><s:text name="CHA01_status"/></label>
	                 <s:select name="status" list='#application.CodeTable.getCodes("1121")' 
	                 	listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{CHA01_selectAll}"/>
	                 </p>
	                <p class="date">
	                <%-- 加入日期 --%>
	                  <label><s:text name="CHA01_joinDate"/></label>
	                  <span><s:textfield name="startDate" cssClass="date"/></span> - <span><s:textfield name="endDate" cssClass="date"/></span>
	                </p>
	              </div>
	            </div>
	            <p class="clearfix">
	                <%-- 查询 --%>
	              	<button class="right search"  onclick="search();return false">
	              			<span class="ui-icon icon-search-big"></span>
	              			<span class="button-text"><s:text name="CHA01_search"/></span>
	              	</button>
	            </p>
	          </cherry:form>
	        </div>
	        <div id="section" class="section hide">
	          <div class="section-header">
	          <strong>
	          	<span class="ui-icon icon-ttl-section-search-result"></span>
	          	<%-- 查询结果一览 --%>
	          	<s:text name="CHA01_results_list"/>
	         </strong>
	        </div>
	        
	          <div class="section-content" id="result_list">
	            <div class="toolbar clearfix">
	            <span class="left">
	            <%--渠道一览启用 --%>
	            <cherry:show domId="BSCHA01ENABLE">
                   <a href="${enableChannel}" id="enableBtn" class="add" onclick="confirmInit(this,'enable');return false;">
                   <span class="ui-icon icon-enable"></span>
                   <span class="button-text"><s:text name="CHA01_enable"/></span>
                </a>
                </cherry:show>
                <%--渠道一览停用 --%>
                <cherry:show domId="BSCHA01DISABLE">
                   <a href="${disableChannel}" id="disableBtn" class="delete" onclick="confirmInit(this,'disable');return false;">
                   <span class="ui-icon icon-disable"></span>
                   <span class="button-text"><s:text name="CHA01_disable"/></span>
                </a>
                </cherry:show>
                </span>
	            <span class="right"><a class="setting"><span class="ui-icon icon-setting"></span>
	            <span class="button-text">
	             <%-- 设置列 --%>
	             <s:text name="CHA01_colSetting"/>
	             </span></a></span></div>
	          </div>
	        </div>
	      </div>
	      <div id="result_table" class="hide">
	      	<table id="dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
	              <thead>
	                <tr>	             
	                  <th><s:checkbox name="allSelect" id="allSelect" onclick="checkSelectAll(this)"/></th><%-- 选择 --%>  
	                  <th><s:text name="CHA01_num"/></th><%-- No. --%>
	                  <th><s:text name="CHA01_channelCode"/></th><%-- 渠道代码 --%>
	                  <th><s:text name="CHA01_channelName"/></th><%-- 渠道名称 --%>
	                  <th><s:text name="CHA01_status"/></th><%-- 渠道类型 --%>
	                  <th><s:text name="CHA01_joinDate"/></th><%-- 加入日期 --%>
	                  <th><s:text name="CHA01_validFlag"/></th><%-- 有效区分--%>
	                </tr>
	              </thead>
	            </table>
	      </div>
	        <div class="hide" id="dialogInit"></div>
      <div style="display: none;">
      <div id="disableText"><p class="message"><span><s:text name="CHA01_disableText"/></span></p></div>
      <div id="disableTitle"><s:text name="CHA01_disableTitle"/></div>
      <div id="enableText"><p class="message"><span><s:text name="CHA01_enableText"/></span></p></div>
      <div id="enableTitle"><s:text name="CHA01_enableTitle"/></div>
      <div id="dialogConfirm"><s:text name="global.page.ok" /></div>
      <div id="dialogCancel"><s:text name="global.page.cancle" /></div>
      <div id="dialogClose"><s:text name="global.page.close" /></div>
      </div>
      <div id="ajaxResultMsg" class="hide">
        <s:text name="save_success_message" />
      </div>
	 </s:i18n>
	    <%-- ================== dataTable共通导入 START ======================= --%>
		<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
		<%-- ================== dataTable共通导入    END  ======================= --%>
		<script type="text/javascript">
			// 节日
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
	#DEPARTLINE{
		margin-left: 90px;
		margin-right: 5px;
		margin-bottom: 5px;
	}
	#RANGELABLE{
		width: 65px; 
		float:left;
		margin-top: 4px;
		text-align: right;
		padding-left:15px;
	}
	#DEPARTLINE span{
		margin-right: 2px;
	}
        </style>
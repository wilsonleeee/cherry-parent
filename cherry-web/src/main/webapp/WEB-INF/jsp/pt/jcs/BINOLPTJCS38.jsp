<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<link rel="stylesheet" href="/Cherry/css/cherry/combobox.css" type="text/css">
<script type="text/javascript" src="/Cherry/js/pt/jcs/BINOLPTJCS38.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/common/popDataTable.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript">
	<%--节日 --%>
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
	
	cherryValidate({			
		formId: "mainForm",		
		rules: {
			startDate: {dateValid:true},	// 开始日期
			endDate: {dateValid:true}	// 结束日期
		}		
	});
</script>
<s:i18n name="i18n.pt.BINOLPTJCS38">
    <s:url action="BINOLPTJCS38_disOrEnableFun" id="disOrEnableFun"></s:url>
	<s:url id="search_url" value="/pt/BINOLPTJCS38_search"/>
	<s:url id="add_url" action="BINOLPTJCS41_init"/>
	<s:text id="selectAll" name="global.page.all"/>
	<div class="hide">
		<a id="searchUrl" href="${search_url}"></a>
		<s:text name="global.page.select" id="defVal"/>
		<div id="prtfun_select">${defVal}</div>
	</div>
	<div id="CIO02_select" class="hide"><s:text name="global.page.all" /></div> 
	      <div class="panel-header">
	      	<%-- 产品方案一览 --%>
	        <div class="clearfix"> 
			<span class="breadcrumb left">	    
				<jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
			</span>
	            <%-- 添加按钮 --%>
       		    <span class="right"> 
       		    <cherry:show domId="BINOLPTJCS38ADD">
       	     	 	<a href="${add_url}" class="add" onclick="javascript:openWin(this);return false;">
       		 		<span class="button-text"><s:text name="JCS41_detail"/></span>
       		 		<span class="ui-icon icon-add"></span>
       		 	    </a>
       		    </cherry:show> 
       		    </span>  
	        </div> 
	      </div>
	     <%-- ================== 错误信息提示 START ======================= --%>
	     	 <div id="actionResultDisplay"></div>
   			 <div id="errorMessage"></div>    
   			 <div style="display: none" id="errorMessageTemp">
   			 <div class="actionError">
   			    <ul><li><span><s:text name="EBS00127"/></span></li></ul>         
    		</div>
    	</div>
   		 <%-- ================== 错误信息提示   END  ======================= --%>
	      <div id="actionResultDisplay"></div>
	      <div class="panel-content">
	        <div class="box">
	          <cherry:form id="mainForm" class="inline">
	           <input type="hidden" id="currentUnitID" name="currentUnitID" value="BINOLPTJCS38"/>
	            <div class="box-header">
	            <strong>
	            	<span class="ui-icon icon-ttl-search"></span>
	            	<%-- 查询条件 --%>
	            	<s:text name="prtfun_condition"/>
	            </strong>
	               </div>
	            <div class="box-content clearfix">
	              <div class="column" style="width:50%;height:55px;">
	              <p>
                	<%-- 所属品牌 --%>
                   	<s:text name="global.page.select" id="prtfun_select"/>
                	<s:if test="%{brandInfoList.size()> 1}">
                    	<label><s:text name="brandName"></s:text></label>
                    	<s:select id="brandInfoId" name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" headerKey="" headerValue="%{#JCS18_select}" cssStyle="width:100px;"></s:select>
                	</s:if><s:else>
                		<label><s:text name="prtfun_brand"></s:text></label>
                    	<s:select id="brandInfoId" name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" cssStyle="width:100px;"></s:select>      	
                	</s:else>
                	</p>
                	<p>
                		<label><s:text name="prtFunType"></s:text></label>
	                  <s:select name="prtFunType" list='#application.CodeTable.getCodes("1327")' listKey="CodeKey" listValue="Value"
	                  	headerKey="" headerValue="%{selectAll}" value="1"/>	                 
                	</p>
	              </div>
	              <div class="column last" style="width:49%;height:55px;">            
	               <p>
	                  <label style="width: 65px;"><s:text name="prtfun_validFlag"/></label><%-- 有效状态--%>
	                  <s:select name="validFlag" list='#application.CodeTable.getCodes("1137")' listKey="CodeKey" listValue="Value"
	                  	headerKey="" headerValue="%{selectAll}" value="1"/>	                 
                  	</p>
                  	<p>
	                  	<label style="width: 65px;"><s:text name="prtfun_date"/></label><%-- 开启时间--%>
	                  	<span><s:textfield id="startDate" name="startDate" cssClass="date"/>-<s:textfield id="endDate" name="endDate" cssClass="date"/></span>
                  	</p>
	              </div>
	            </div>
	            <p class="clearfix">
	                <%-- 查询 --%>
	              	<button class="right search"  onclick="search();return false">
	              			<span class="ui-icon icon-search-big"></span>
	              			<span class="button-text"><s:text name="prtfun_search"/></span>
	              	</button>
	            </p>
	          </cherry:form>
	        </div>
	        <div id="section" class="section hide">
	          <div class="section-header">
	          <strong>
	          	<span class="ui-icon icon-ttl-section-search-result"></span>
	          	<%-- 查询结果一览 --%>
	          	<s:text name="global.page.list"/>
	         </strong>
	        </div>
	        
	          <div class="section-content" id="result_list">
	            <div class="toolbar clearfix">
	            <span class="left">
	            <%--方案启用 >
	            <cherry:show domId="BINOLPTJCS38DIS">--%>
                   <a href="${disOrEnableFun}" id="enableBtn" class="add" onclick="confirmInit(this,'enable');return false;">
                   <span class="ui-icon icon-enable"></span>
                   <span class="button-text"><s:text name="prtfun_enable"/></span>
                </a>
                <%--方案停用 --%>
                   <a href="${disOrEnableFun}" id="disableBtn" class="delete" onclick="confirmInit(this,'disable');return false;">
                   <span class="ui-icon icon-disable"></span>
                   <span class="button-text"><s:text name="prtfun_disable"/></span>
                </a>
               <%-- </cherry:show>--%>
                </span>
	            <span class="right"><a class="setting"><span class="ui-icon icon-setting"></span>
	            <span class="button-text">
	             <%-- 设置列 --%>
	             <s:text name="prtfun_colSetting"/>
	             </span></a></span></div>
	          </div>
	        </div>
	      </div>
	      <div id="result_table" class="hide">
	      	<table id="dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
	              <thead>
	                <tr>	             
	                  <th><s:checkbox name="allSelect" id="allSelect" onclick="checkSelectAll(this)"/></th><%-- 选择 --%>  
	                  <th><s:text name="prtfun_num"/></th><%-- No. --%>
	                  <th><s:text name="prtFunDateName"/></th><%-- 产品功能开启时间名称 --%>
	                  <th><s:text name="prtFunType"/></th><%-- 产品功能类别 --%>
	                  <th><s:text name="prtfun_startDate"/></th><%-- 生效时间 --%>
	                  <th><s:text name="prtfun_endDate"/></th><%-- 失效时间 --%>
	                  <th><s:text name="prtfun_validFlag"/></th><%-- 有效区分 --%>
	                </tr>
	              </thead>
	            </table>
	      </div>
      <div class="hide" id="dialogInit"></div>
	  <%-- 柜台产品实时下发弹窗 --%>
	  <div class="hide" id="dialogIssuedInitDIV"></div>
      <div style="display: none;">
		<div id="disableText"><p class="message"><span><s:text name="prtfun_disableText"/></span></p></div>
		<div id="disableTitle"><s:text name="prtfun_disableTitle"/></div>
		<div id="enableText"><p class="message"><span><s:text name="prtfun_enableText"/></span></p></div>
		<div id="enableTitle"><s:text name="prtfun_enableTitle"/></div>
		<div id="dialogConfirm"><s:text name="global.page.ok" /></div>
		<div id="dialogCancel"><s:text name="global.page.cancle" /></div>
		<div id="dialogClose"><s:text name="global.page.close" /></div>
		
		<div id="dialogConfirmIss"><s:text name="global.page.goOn" /></div>
		<div id="dialogCancelIss"><s:text name="global.page.cancle" /></div>
		<p id="operateSuccessId" class="success"><span><s:text name="global.page.operateSuccess"/></span>
		<p id="operateFaildId" class="message"><span><s:text name="global.page.operateFaild"/></span>
		<div id="issCntPrtMsg1"><s:text name="issCntPrtMsg1" /></div>
		<div id="issLaunchingMsg"><s:text name="issLaunchingMsg" /></div>
		<div id="issCntPrt"><s:text name="issCntPrt" /></div>
		  <!-- 柜台产品下发 -->
		<s:url id="issuedPrtUrl" action="BINOLPTJCS17_issuePrt"/>
		<span class="hide" id="issuedPrtId">${issuedPrtUrl}</span>
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
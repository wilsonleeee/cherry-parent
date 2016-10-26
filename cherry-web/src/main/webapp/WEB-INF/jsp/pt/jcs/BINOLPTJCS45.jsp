<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<link rel="stylesheet" href="/Cherry/css/cherry/combobox.css" type="text/css">
<script type="text/javascript" src="/Cherry/js/pt/jcs/BINOLPTJCS45.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/common/popDataTable.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<s:i18n name="i18n.pt.BINOLPTJCS45">
    <s:url action="BINOLPTJCS45_disOrEnableSolu" id="disOrEnableSolu"></s:url>
	<s:url id="search_url" value="/pt/BINOLPTJCS45_search"/>
	<s:url id="add_url" action="BINOLPTJCS46_init"/>
	<s:text id="selectAll" name="global.page.all"/>
	<div class="hide">
		<a id="searchUrl" href="${search_url}"></a>
		<s:text name="solu_select" id="defVal"/>
		<div id="solu_select">${defVal}</div>
	</div>
	<div id="CIO02_select" class="hide"><s:text name="global.page.all" /></div> 
	      <div class="panel-header">
	      	<%-- 产品方案一览 --%>
	        <div class="clearfix"> 
				<span class="breadcrumb left">	    
					<jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
				</span>
	            
       		    <span class="right"> 
	       		<s:if test='"0".equals(isPosCloud)'>
	       			<%-- 柜台产品下发按钮 --%>
		       		<cherry:show domId="BINOLPTJCS4501">
		       		 	<a href="#" class="add" onclick="issuedInit();return false;">
		       		 		<span class="button-text"><s:text name="issCntPrt"/></span>
		       		 		<span class="ui-icon icon-down"></span>
		       		 	</a>
		       		 </cherry:show>
	       			<%-- 添加按钮 --%>
	       		    <cherry:show domId="BINOLPTJCS4502">
	       	     	 	<a href="${add_url}" class="add" onclick="javascript:openWin(this);return false;">
	       		 		<span class="button-text">
			       		 		<s:text name="JCS46_detail"/>
	       		 		</span>
	       		 		<span class="ui-icon icon-add"></span>
	       		 	    </a>
	       		    </cherry:show> 
       		    </s:if>
       		    <s:elseif test='"1".equals(isPosCloud)'>
	       			<%-- 柜台产品下发按钮 --%>
		       		<cherry:show domId="BINOLPTJCS2302">
		       		 	<a href="#" class="add" onclick="issuedInit();return false;">
		       		 		<span class="button-text"><s:text name="issCntPrt"/></span>
		       		 		<span class="ui-icon icon-down"></span>
		       		 	</a>
		       		 </cherry:show>
       		    	<%-- 添加按钮 --%>
	       		    <cherry:show domId="BINOLPTJCS23ADD">
	       	     	 	<a href="${add_url}" class="add" onclick="javascript:openWin(this);return false;">
	       		 		<span class="button-text">
			       		 		<s:text name="JCS46_addPrtList"/> 
	       		 		</span>
	       		 		<span class="ui-icon icon-add"></span>
	       		 	    </a>
	       		    </cherry:show> 
       		    </s:elseif>
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
	           <input type="hidden" id="currentUnitID" name="currentUnitID" value="BINOLPTJCS45"/>
	            <div class="box-header">
	              <strong>
	            	<span class="ui-icon icon-ttl-search"></span>
	            	<%-- 查询条件 --%>
	            	<s:text name="solu_condition"/>
	              </strong>
	            </div>
	            
	            <s:if test='"0".equals(isPosCloud)'>
		            <div class="box-content clearfix">
		              <div class="column" style="width:50%;height:55px;">
		              <p>
	                	<%-- 所属品牌 --%>
	                	<s:if test="%{brandInfoList.size()> 1}">
	                    	<label><s:text name="solu_brand"></s:text></label>
	                    	<s:text name="solu_select" id="solu_select"/>
	                    	<s:select id="brandInfoId" name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" headerKey="" headerValue="%{#JCS18_select}" cssStyle="width:100px;"></s:select>
	                	</s:if><s:else>
	                		<label><s:text name="solu_brand"></s:text></label>
	                    	<s:text name="solu_select" id="solu_select"/>
	                    	<s:select id="brandInfoId" name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" cssStyle="width:100px;"></s:select>      	
	                	</s:else>
	                	</p>
		                <p>
		               	<%-- 方案名称 --%>
		                  <label><s:text name="solu_solutionName"/></label>
		                  <s:textfield name="solutionName" cssClass="text" maxlength="20" />
		                </p>
		              </div>
		              <div class="column last" style="width:49%;height:55px;">            
		               <p>
		                  <label style="width: 65px;"><s:text name="solu_validFlag"/></label><%-- 有效状态--%>
		                  <s:select name="validFlag" list='#application.CodeTable.getCodes("1137")' listKey="CodeKey" listValue="Value"
		                  	headerKey="" headerValue="%{selectAll}" value="1"/>	                 </p>
		                <p >
		               	<%-- 方案Code--%>
		                  <label><s:text name="solu_solutionCode"/></label>
		                  <s:textfield name="solutionCode" cssClass="text" maxlength="20" />
		                </p>
		              </div>
		            </div>
	            </s:if>
	            <s:elseif test='"1".equals(isPosCloud)'>
		            <div class="hide" >
	                	<%-- 所属品牌 --%>
	                	<s:if test="%{brandInfoList.size()> 1}">
	                    	<label><s:text name="solu_brand"></s:text></label>
	                    	<s:text name="solu_select" id="solu_select"/>
	                    	<s:select id="brandInfoId" name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" headerKey="" headerValue="%{#JCS18_select}" cssStyle="width:100px;"></s:select>
	                	</s:if><s:else>
	                		<label><s:text name="solu_brand"></s:text></label>
	                    	<s:text name="solu_select" id="solu_select"/>
	                    	<s:select id="brandInfoId" name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" cssStyle="width:100px;"></s:select>      	
	                	</s:else>
		            </div>
		            <div class="box-content clearfix">
		              <div class="column" style="width:50%;height:55px;">
		                <p>
		               	  <%-- 方案名称 --%>
		                  <label><s:text name="solu_prtlistName"/></label>
		                  <s:textfield name="solutionName" cssClass="text" maxlength="20" />
		                </p>
		              </div>
		              <div class="column last" style="width:49%;height:55px;">            
		                <p >
		               	  <%-- 方案Code--%>
		                  <label><s:text name="solu_prtlistCode"/></label>
		                  <s:textfield name="solutionCode" cssClass="text" maxlength="20" />
		                </p>
		              </div>
		            </div>
	            </s:elseif>
	            
	            <p class="clearfix">
	                <%-- 查询 --%>
	              	<button class="right search"  onclick="search();return false">
	              			<span class="ui-icon icon-search-big"></span>
	              			<span class="button-text"><s:text name="solu_search"/></span>
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
	            <span class="left ">
	            <cherry:show domId="BINOLPTJCS45UPD"> 
		            <%--方案启用 --%>
                   <a href="${disOrEnableSolu}" id="enableBtn" class="add" onclick="confirmInit(this,'enable');return false;">
                   	<span class="ui-icon icon-enable"></span>
                   	<span class="button-text"><s:text name="solu_enable"/></span>
                   </a>
                <%--方案停用 --%>
                   <a href="${disOrEnableSolu}" id="disableBtn" class="delete" onclick="confirmInit(this,'disable');return false;">
                   	<span class="ui-icon icon-disable"></span>
                   	<span class="button-text"><s:text name="solu_disable"/></span>
                   </a>
                </cherry:show> 
                </span>
	            <span class="right"><a class="setting"><span class="ui-icon icon-setting"></span>
	            <span class="button-text">
	             <%-- 设置列 --%>
	             <s:text name="solu_colSetting"/>
	             </span></a></span></div>
	          </div>
	        </div>
	      </div>
	      <div id="result_table" class="hide">
	      	<table id="dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
	              <thead>
	                <tr>	             
	                  <th><s:checkbox name="allSelect" id="allSelect" onclick="checkSelectAll(this)"/></th><%-- 选择 --%>  
	                  <th><s:text name="solu_num"/></th><%-- No. --%>
	                  <s:if test='"0".equals(isPosCloud)'>
		                  <th><s:text name="solu_solutionName"/></th><%-- 方案名称 --%>
		                  <th><s:text name="solu_solutionCode"/></th><%-- 方案编码 --%>
		                  <th><s:text name="solu_comments"/></th><%-- 方案描述 --%>
		                  <th><s:text name="solu_startDate"/></th><%-- 方案价格生效日 --%>
		                  <th><s:text name="solu_endDate"/></th><%-- 方案价格失效日期 --%>
	                  </s:if>
	                  <s:elseif test='"1".equals(isPosCloud)'>
		                  <th><s:text name="solu_prtlistName"/></th><%-- 方案名称 --%>
		                  <th><s:text name="solu_prtlistCode"/></th><%-- 方案编码 --%>
		                  <th><s:text name="solu_prtlistcomments"/></th><%-- 方案描述 --%>
		                  <th><s:text name="solu_prtliststartDate"/></th><%-- 方案价格生效日 --%>
		                  <th><s:text name="solu_prtlistendDate"/></th><%-- 方案价格失效日期 --%>
	                  </s:elseif>
	                  <th><s:text name="solu_validFlag"/></th><%-- 有效区分 --%>
	                </tr>
	              </thead>
	            </table>
	      </div>
      <div class="hide" id="dialogInit"></div>
	  <%-- 柜台产品实时下发弹窗 --%>
	  <div class="hide" id="dialogIssuedInitDIV"></div>
      <div style="display: none;">
		<div id="disableText"><p class="message"><span><s:text name="solu_disableText"/></span></p></div>
		<div id="disableTitle"><s:text name="solu_disableTitle"/></div>
		<div id="enableText"><p class="message"><span><s:text name="solu_enableText"/></span></p></div>
		<div id="enableTitle"><s:text name="solu_enableTitle"/></div>
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
		<s:url id="issuedPrtUrl" action="BINOLPTJCS17_issuePrtYT"/>
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
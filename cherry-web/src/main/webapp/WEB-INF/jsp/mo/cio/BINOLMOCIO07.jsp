<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<link rel="stylesheet" href="/${CHERRY_CONTEXT_PATH}/css/cherry/combobox.css" type="text/css">
<link rel="stylesheet" href="/${CHERRY_CONTEXT_PATH}/plugins/zTree/zTreeStyle/zTreeStyle.css" type="text/css" media="screen, projection">
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/common/departBar.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/common/popDataTable.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/plugins/zTree/jquery.ztree-2.6.min.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/common/ajaxfileupload.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/mo/cio/BINOLMOCIO07.js"></script>
<style>
	.treebox_left{
		background:none repeat scroll 0 0 #FFFFFF;
		border:1px solid #FAD163;
		height:550px;
		border-color:#D6D6D6 #D6D6D6 
	}
}
</style>
<s:i18n name="i18n.mo.BINOLMOCIO07">
<s:url id="setTarget" value="/mo/BINOLMOCIO07_setTarget"/>
<s:url id="search_url" value="/mo/BINOLMOCIO07_search"/>
<s:url id="exporCheck_url" action="BINOLMOCIO07_exportCheck" ></s:url>
<s:url id="exprtExcelUrl" value="/mo/BINOLMOCIO07_export"></s:url>
<s:url id="down" value="/mo/BINOLMOCIO07_down"/>
<s:url id="getTreeNodes_url" value="/mo/BINOLMOCIO07_getTreeNodes"/>
<%-- 销售目标模板下载URL --%>
<s:url id="downLoad_url" value="/download/销售目标导入模板.xls"/>
<%-- 销售目标批量导入URL --%>
<s:url id="import_url" value="/mo/BINOLMOCIO07_import"/>
<s:url action="BINOLCM02_initCampaignDialog" namespace="/common" id="searchCampaignInitUrl"></s:url>
<div class="hide">
	<a id="searchUrl" href="${search_url}"></a>
	<a id="setTarget" href="${setTarget}"></a>
	<a id="down" href="${down}"></a>
	<a id="getTreeNodes" style="display:none">${getTreeNodes_url}</a>
	<a id="exporCheckUrl" href="${exporCheck_url}"></a>
	<div id="setTitle"><s:text name="CIO07_edit"/></div>
	<div id="dialogConfirm"><s:text name="CIO07_dialogConfirm"/></div>
	<div id="dialogCancel"><s:text name="CIO07_dialogCancel"/></div>
	<div id="dialogClose"><s:text name="CIO07_dialogClose"/></div>
	<div id="addSaleTargetTitle"><s:text name="CIO07_addSaleTarget"/></div>
	<div id="ImportSaleTargetTitle"><s:text name="CIO07_ImportSaleTarget"/></div>
</div>
<div class="panel-header">
	<div class="clearfix">
	    <span class="breadcrumb left">	    
			<jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
		</span>
           	<cherry:show domId="MOCIO07ADD">
        		<a class="add right" id="addButton" onclick="BINOLMOCIO07.popSetSaleTargetDialog();return false;">
    		 		<span class="button-text"><s:text name="CIO07_addSaleTarget"/></span>
    		 		<span class="ui-icon icon-add"></span>
    		 	</a>
   		 	</cherry:show>
   		 	<cherry:show domId="MOCIO07IMPORT">
	  		 	<a class="add right" id="addButton" onclick="BINOLMOCIO07.popImportSaleTargetDialog();return false;">
	   		 		<span class="button-text"><s:text name="CIO07_ImportSaleTarget"/></span>
	   		 		<span class="ui-icon icon-add"></span>
	   		 	</a>
   		 	</cherry:show>
	</div> 
</div>
	<div id="errorMessage"></div>           
			<%-- ================== 错误信息提示 START ======================= --%>
			<div id="errorDiv2" class="actionError" style="display:none">
				<ul>
					<li><span id="errorSpan2"></span></li>
				</ul>         
			</div>
			<%-- ================== 错误信息提示   END  ======================= --%>        
			<div id="actionResultDisplay"></div>
			<div class="panel-content">
			<div class="box">
				<cherry:form id="mainForm" class="inline">
					<input type="hidden" id="currentUnitID" name="currentUnitID" value="BINOLMOCIO07"/>
				<div class="box-header">
	            <strong>
	            	<span class="ui-icon icon-ttl-search"></span>
	            	<%-- 查询条件 --%>
	            	<s:text name="CIO07_condition"/>
	            </strong>
	            </div>
	            <div class="box-content clearfix">
	              <div class="column" style="width:49%;">
	              	<p>
	                	<%-- 所属品牌 --%>
	                	<label style="width:80px"><s:text name="CIO07_brand"></s:text></label>
	                	<s:text name="CIO07_select" id="CIO07_select"/>
	                	<s:if test="%{brandInfoList.size()> 1}">
	                    	<s:select onchange="BINOLMOCIO07.search();return false;" name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" headerKey="" headerValue="%{#CIO07_select}" cssStyle="width:100px;"></s:select>
	                	</s:if><s:else>
	                    	<s:select onchange="BINOLMOCIO07.search();return false;" name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" cssStyle="width:100px;"></s:select>      	
	                	</s:else>
                	</p>
                	<p>
                        <label style="width:80px"><input id="targetDateType1" name="targetDateType" class="radio" type="radio" value="month" checked><s:text name="CIO07_targetMonth"></s:text></label>
                        <span><s:textfield id="targetMonth" name="targetMonth" cssClass="date" maxlength="6" value="%{targetMonth}"/></span>
						<span class="highlight">※</span>
						<span class="gray"><s:text name="CIO07_example"/></span>
                    </p>
                    <p>
                        <label style="width:80px"><input id="targetDateType2" name="targetDateType" class="radio" type="radio" value="day"><s:text name="CIO07_targetDay"></s:text></label>
                        <span><s:textfield id="targetDay" name="targetDay" cssClass="date" maxlength="8" value="%{targetDay}" disabled="true"/></span>
						<span class="highlight">※</span>
						<span class="gray"><s:text name="CIO07_exampleDay"/></span>
                    </p>
	              </div>
	              <div class="column last" style="width:50%;">
	              	<p><%-- 名称 --%>
						<label style="width:80px"><s:text name="CIO07_parameter" /></label>
						<s:textfield name="parameter" cssClass="text" maxlength="20"/>
					</p>
						<!-- 初始化已经将此参数设置为code值为1120的第一个key -->
		              	<s:hidden id="type" name="type"></s:hidden>
						<%-- 目标类型 --%>
					<p>
	                 <label style="width:80px"><s:text name="CIO07_targetType"/></label>
	                 <s:select id="targetType" name="targetType" list='#application.CodeTable.getCodes("1300")' listKey="CodeKey" listValue="Value" onchange="BINOLMOCIO07.search();return false;"/>
					</p>
					<p>
                		<label style="width:80px"><s:text name="CIO07_activityName" /></label>
                		<span>
		                <s:hidden name="campaignCode"></s:hidden>
		                <s:hidden name="campaignName"></s:hidden>
		                <span id="campaignDiv" style="line-height: 18px;"></span>
		                <a class="add" onclick="BINOLMOCIO07.popCampaignList('${searchCampaignInitUrl}',this);return false;">
						  <span class="ui-icon icon-search"></span>
						  <span class="button-text"><s:text name="global.page.Popselect" /></span>
					    </a>
					    </span>
		            </p>
	              </div>
	              <%-- ======================= 组织联动共通导入开始  ============================= --%>
                  <s:action name="BINOLCM13_query" namespace="/common" executeResult="true">
                      <s:param name="businessType">3</s:param>
                      <s:param name="operationType">1</s:param>
                      <s:param name="mode">dpat,area,chan</s:param>
                  </s:action>
                  <%-- ======================= 组织联动共通导入结束  ============================= --%>
	            </div>       
				<p class="clearfix">
	                <%-- 查询 --%>
	              	<button class="right search" onclick="BINOLMOCIO07.search();return false;">
	              			<span class="ui-icon icon-search-big"></span>
	              			<span class="button-text"><s:text name="CIO07_search"/></span>
	              	</button>
	            </p>
	          </cherry:form>
	        </div>
	        <%-- ====================== 结果一览开始 ====================== --%>
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
	         		<s:iterator value='#application.CodeTable.getCodes("1124")' id="typeMode" status="status">
			          <li id='tab<s:property value='CodeKey' />_li' class='<s:if test='0 == #status.index'>ui-tabs-selected</s:if>' onclick = "BINOLMOCIO07.typeDateFilter('<s:property value='CodeKey' />',this);"><a><s:property value='Value' /></a></li>
			       	</s:iterator>
			       	</ul>
			       	<div id="tabs-1" class="ui-tabs-panel" style="overflow-x:auto;overflow-y:hidden;">
			           	<div class="toolbar clearfix">
				           	<span class="left">
				           		<cherry:show domId="MOCIO07EXP">
						        	<span style="margin-right:10px;">
					                   <a id="export" class="export" onclick="BINOLMOCIO07.exportExcel('${exprtExcelUrl}');return false;">
					                      <span class="ui-icon icon-export"></span>
					                      <span class="button-text"><s:text name="global.page.exportExcel"/></span>
					                   </a>
					                </span>
					    		</cherry:show>
					    		<cherry:show domId="MOCIO07DOWN">
					        		<a class="add" id="setDownButton" onclick="BINOLMOCIO07.popDownDiglog();return false;">
					    		 		<span class="button-text"><s:text name="CIO07_down"/></span>
					    		 		<span class="ui-icon icon-down"></span>
					    		 	</a>
					    		</cherry:show>
				       		</span>
				      		<span class="right">
								<%-- 设置列 --%>
				      			<a class="setting">
				      				<span class="ui-icon icon-setting"></span>
				      				<span class="button-text"><s:text name="global.page.colSetting"/></span>
				      			</a>
				      		</span>
			            </div>
		            	<table id="dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%" >
			              <thead>
			                <tr>
							  <th><s:text name="CIO07_num"/></th><%-- No. --%>
			                  <th class="center"><s:text name="CIO07_different"/></th>
			                  <th class="center"><s:text name="CIO07_parameter"/></th>
			                  <th class="center"><s:text name="CIO07_type"/></th> 
			                  <th class="center"><s:text name="CIO07_targetDate"/></th>
			                  <!-- 目标类型 -->
			                  <th class="center"><s:text name="CIO07_targetType"/></th>
			                  <!-- 活动 -->
			                  <th class="center"><s:text name="CIO07_activity"/></th>
			                  <th class="center"><s:text name="CIO07_targetQuantity"/></th>
			                  <th class="center"><s:text name="CIO07_targetMoney"/></th>
			                  <th class="center"><s:text name="CIO07_downFlag"/></th>
			                  <th class="center"><s:text name="CIO07_source"/></th>
			                  <th class="center"><s:text name="CIO07_targetSetTime"/></th>
			                  <th class="center"><s:text name="CIO07_opration"/></th>
			                </tr>
			              </thead>
			            </table>
			        </div>
		        </div>
       		</div>
		</div>
	</div>
       <%-- ====================== 结果一览结束 ====================== --%>
	<%-- ====================== 编辑弹出框开始 ====================== --%>
	 <div id="editSaleTarget" class="hide">
    	<div class="clearfix">
    	 <form id="cherryEdit">
    		<div>
    		<table style="margin:auto; width:100%;" class="detail">
    		<tr>
    		<th><s:text name="CIO07_brand"/><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
    		<td><span><s:if test="%{brandInfoList.size()> 1}">                 	
                    	<s:text name="CIO07_select" id="CIO07_select"/>
                    	<s:select name="editBrandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" headerKey="" headerValue="%{#CIO07_select}" cssStyle="width:100px;"></s:select>
                	</s:if><s:else>
                    	<s:text name="CIO07_select" id="CIO07_select"/>
                    	<s:select name="editBrandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" cssStyle="width:100px;"></s:select>      	
                	</s:else>
                </span>
            </td>
            </tr>
            <tr>
            	<th><s:text name="CIO07_parameter"/></th>
            	<td>
            		<span>
            			<s:textfield name="parameterName" id="parameterName" cssClass="text" value=""/>
            		</span>
            	</td>
            </tr>
    		<tr>
    		<th><s:text name="CIO07_targetDate"/></th>
    		<td>
	    		<span>
	    			<!-- 目标日期不可编辑，此列取查询条件中有效的目标日期（或月或日） -->
		    		<span><s:textfield name="editTargetDate" cssClass="date" maxlength="8" value=""/></span>
		    		<%-- <span class="highlight">※</span>
		    		<span class="gray"><s:text name="CIO07_example"/></span> --%>
	    		</span>
    		</td>
    		</tr>
    		<tr>
    		<th><s:text name="CIO07_targetQuantity"/></th>
    		<td><span><s:textfield name="editTargetQuantity" cssClass="text" maxlength="9"/></span></td>
    		</tr>
    		<tr>
    		<th><s:text name="CIO07_targetMoney"/></th>
    		<td><span><s:textfield name="editTargetMoney" cssClass="text" maxlength="15"/></span></td>
    		</tr>
    		<tr>
	     		<th><s:text name="CIO07_targetType"/><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
	     		<td>
	                <span>
			     		<s:select id="targetType" name="targetType" list='#application.CodeTable.getCodes("1300")' listKey="CodeKey" listValue="Value"/>
	                </span>
	     		</td>
     		</tr>
     		<tr>
	     		<th><s:text name="CIO07_activityName" /></th>
	            <td>
	            	<span>
	                <s:hidden name="campaignCode"></s:hidden>
	                <s:hidden name="campaignName"></s:hidden>
	                <span id="campaignDiv" style="line-height: 18px;"></span>
	                <a class="add" onclick="BINOLMOCIO07.popCampaignList('${searchCampaignInitUrl}',this);return false;">
					  <span class="ui-icon icon-search"></span>
					  <span class="button-text"><s:text name="global.page.Popselect" /></span>
				    </a>
				    </span>
	            </td>
     		</tr>
    		</table>
    		</div>
    	</form>
    	</div>
    </div>  
    <%-- ====================== 编辑弹出框结束 ====================== --%>
    
    <%--=======================新增销售目标弹出框开始====================================== --%>
     <div id="pop_target_main" style="display:none">
     	<div id="pop_target_body">
     	<form id="cherrytargetadd">
     		<div id="errorDiv_product" class="actionError" style="display:none">
        		<ul>
            		<li><span id="errorSpan_product"></span></li>
        		</ul>         
   		 	</div>
   		 	<table class="detail">
	   		 	<tbody>
		   		 	<tr>
			   		 	<th><s:text name="CIO07_brand"/><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
			   		 	<td>
							<s:if test="%{brandInfoList.size()> 1}"><s:text name="CIO07_select" id="CIO07_select"/>
							<s:select name="addBrandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" headerKey="" headerValue="%{#CIO07_select}" cssStyle="width:100px;" onchange="BINOLMOCIO07.getTreeNodes();return false;"/>
			                </s:if>
			                <s:else>
			                <s:text name="CIO07_select" id="CIO07_select"/><s:select name="addBrandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" cssStyle="width:100px;" onchange="BINOLMOCIO07.getTreeNodes();return false;"/>
			                </s:else>
			            </td>
		     		</tr>
		     		<tr>
		     			<!-- 目标月 -->
		     			<th><input id="addTargetDateType1" name="addTargetDateType" class="radio" type="radio" value="month" checked><s:text name="CIO07_targetMonth"></s:text></th>
                        <td>
                        	<span><s:textfield id="addTargetMonth" name="addTargetMonth" cssClass="date" maxlength="6" value="%{targetMonth}"/></span>
							<span class="highlight">※</span>
							<span class="gray"><s:text name="CIO07_example"/></span>
						</td>
		     		</tr>
		     		<tr>
		     			<!-- 目标日 -->
		     			<th><input id="addTargetDateType2" name="addTargetDateType" class="radio" type="radio" value="day"><s:text name="CIO07_targetDay"></s:text></th>
                        <td>
                        	<span><s:textfield id="addTargetDay" name="addTargetDay" cssClass="date" maxlength="8" value="%{targetDay}" disabled="true"/></span>
							<span class="highlight">※</span>
							<span class="gray"><s:text name="CIO07_exampleDay"/></span>
						</td>
		     		</tr>
		     		<tr>
			     		<th><s:text name="CIO07_targetQuantity"/></th>
						<td><span><s:textfield name="addTargetQuantity" cssClass="text" maxlength="9"/></span></td>
		     		</tr>
		     		<tr>
		     		<th><s:text name="CIO07_addTargetMoney"/></th>
		     		<td>
                        <span>
			     		<s:textfield name="addTargetMoney" cssClass="text" maxlength="15"/>
			     		<s:text name="CIO07_yuan"/>
                        </span>
		     		</td>
		     		</tr>
		     		<tr>
			     		<th><s:text name="CIO07_targetType"/></th>
			     		<td>
	                        <span>
				     		<s:select id="targetType" name="targetType" list='#application.CodeTable.getCodes("1300")' listKey="CodeKey" listValue="Value"/>
	                        </span>
			     		</td>
		     		</tr>
		     		<tr>
			     		<th><s:text name="CIO07_activityName" /></th>
			            <td>
			            	<span>
			                <s:hidden name="campaignCode"></s:hidden>
			                <s:hidden name="campaignName"></s:hidden>
			                <span id="campaignDiv" style="line-height: 18px;"></span>
			                <a class="add" onclick="BINOLMOCIO07.popCampaignList('${searchCampaignInitUrl}',this);return false;">
							  <span class="ui-icon icon-search"></span>
							  <span class="button-text"><s:text name="global.page.Popselect" /></span>
						    </a>
						    </span>
			            </td>
		     		</tr>
	     		</tbody>
     		</table>
     		<p>
     			<span>
					<a class="add" id="refreshCodes" onclick="BINOLMOCIO07.getTreeNodes();return false;">
			        <span class="ui-icon icon-refresh-s"></span><span class="button-text"><s:text name="CIO07_refreshTree" /></span>
			        </a>
		        </span>
		        <!-- 注：刷新后可看单位的目标设定情况[数量，金额] -->
		        <span class="highlight"><s:text name="CIO07_refreshComment1"></s:text></span>
		        <span style="color:green"><s:text name="CIO07_refreshComment2"></s:text></span>
		    </p>
     		<div id="tree">
     		<div class="box2-header clearfix"> 
	     		<span class="left">
			   		<strong id="left_tree" class="left active" style="background:#E8E8C8">
	       				<span class="ui-icon icon-flag"></span>
	       				<s:text name="CIO07_choiceTarget"/>
	       	   		</strong>
		              &nbsp;&nbsp;&nbsp;&nbsp;<s:select name="addtype" list='#application.CodeTable.getCodes("1124")' listKey="CodeKey" listValue="Value" onchange="BINOLMOCIO07.getTreeNodes();return false;"/>
				</span>
       	   		<span class="right">
					<input id="position_left_0" class="text locationPosition ac_input" type="text" style="width: 140px;" name="locationPosition" autocomplete="off">
					<a class="search" onclick="BINOLMOCIO07.locationPosition(this);return false;">
						<span class="ui-icon icon-position"></span>
						<span class="button-text"><s:text name="CIO07_locationPosition"></s:text></span>
					</a>
				</span>
       		</div>
			<div class="jquery_tree">
    			<div class="jquery_tree treebox_left tree" id ="treeDemo" style="overflow:auto;background:#FCFCFC;height:350px"></div>
    		</div>
    		</div>
    		</form>
     	</div>
     </div>
     <%--=======================新增销售目标弹出框结束====================================== --%>
     <%--=======================导入弹出框开始================================= --%>
     <div id="popImport_target_main" style="display:none">
     	<div id="popImport_target_body">
     		<%-- ================== 信息提示区 START ======================= --%>
			<div id="errorMessageImport"></div>
			<%-- ================== 信息提示区   END  ======================= --%>
			<div class="panel-content">
				<div class="section-content">
		        	<form id="targetImportForm" class="inline">
		        		<div>
							<span class="highlight"><s:text name="CIO07_snow"/></span><s:text name="CIO07_notice"/>
							<a href="${downLoad_url }"><span class="highlight"><s:text name="CIO07_download"/></span></a>
							<s:if test="brandInfoList.size() > 1">
							<s:select id="importBrandInfoId" name="importBrandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName"></s:select>
							</s:if>
							<s:else><s:hidden name="brandInfoId" id="brandInfoId"></s:hidden></s:else>
						</div>
						<table class="detail" cellpadding="0" cellspacing="0">
				            <tr>
				                <td>
				        			<span style="margin-left:10px; display: inline;" class="left hide"> 
									    <input class="input_text" type="text" id="pathExcel" name="pathExcel"/>
									    <input type="button" value="<s:text name="global.page.browse"/>"/>
									    <input class="input_file" type="file" name="upExcel" id="upExcel" size="33" onchange="pathExcel.value=this.value;BINOLMOCIO07.deleteActionMsg();return false;" /> 
									    <input type="button" value="<s:text name="CIO07_importExcel_btn"/>" onclick="BINOLMOCIO07.ajaxFileUpload('${import_url}');return false;" id="upload"/>
									    <img id="loading" src="/Cherry/css/cherry/img/loading.gif" height="15px" style="display: none;">
									</span>
				                </td>
				            </tr>
				        </table>
		        	</form>
	        	</div>
				<div id="errorTargetsShow" style="display: none;">
					<div id="section" class="section">
						<div class="section-header">
							<strong> <span
								class="ui-icon icon-ttl-section-search-result"></span> <s:text
									name="CIO07_warn1" /><span class="highlight" id="showBasError"><s:text
										name="CIO07_warn2" /></span>
							<s:text name="CIO07_warn3" />
							</strong>
						</div>
						<div class="section-content" style="overflow-x: auto; width: 100%;">
							<table id="dataTable" cellpadding="0" cellspacing="0" border="0"
								style="width: 100%;">
								<thead>
									<tr>
										<%-- No. --%>
										<th width="1%"><s:text name="CIO07_num"/></th>
										<%-- 类型 --%>
										<th><s:text name="CIO07_type" /></th>
										<%-- 目标年月 --%>
										<th><s:text name="CIO07_targetDay" /></th>
										<%-- 代号--%>
										<th><s:text name="CIO07_different" /></th>
										<%-- 名称--%>
										<th><s:text name="CIO07_parameter" /></th>
										<%-- 产品类型--%>
										<th><s:text name="CIO07_targetType" /></th>
										<%-- 活动代号--%>
										<th><s:text name="CIO07_activityCode" /></th>
										<%-- 活动名称--%>
										<th><s:text name="CIO07_activityName" /></th>
										<%-- 金额指标--%>
										<th><s:text name="CIO07_targetMoneyMonth" /></th>
										<%-- 数量指标--%>
										<th><s:text name="CIO07_targetQuantityMonth" /></th>
										<%-- 错误原因 --%>
										<th><s:text name="CIO07_error" /></th>
									</tr>
								</thead>
								<tbody id="errorTargets">
								</tbody>
							</table>
						</div>
					</div>
				</div>
			</div>
			<%-- 错误信息 --%>
		    <div id="errMsg">
		    	<%-- 请选择上传文件。 --%>
		    	<input type="hidden" id="errmsg2" value='<s:text name="EMO00010"/>'/>
		    </div>
		    <div id="hiddenTable" class=""></div>
	    </div>
    </div>
     <%--=======================导入弹出框结束================================= --%>
     <%-- ====================== 下发弹出框开始 ====================== --%>
    <div class="hide" id="dialogInit"></div>
    <div id="errmessage" style="display:none">
    	<input type="hidden" id="errmsg1" value='<s:text name="EMO00031"/>'/>
    	<input type="hidden" id="makeSureContext" value='<s:text name="CIO07_makeSureContext"/>'/>
    	<input type="hidden" id="makeSureTitle" value='<s:text name="CIO07_makeSureTitle"/>'/>
    </div>
    <%-- ====================== 下发弹出框结束 ====================== --%>
</s:i18n>
<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>


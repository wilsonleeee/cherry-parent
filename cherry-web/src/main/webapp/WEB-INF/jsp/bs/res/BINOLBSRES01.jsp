<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/bs/res/BINOLBSRES01.js"></script>
<script type="text/javascript" src="/Cherry/js/bs/common/BINOLBSCOM03.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<s:i18n name="i18n.bs.BINOLBSRES01">
	<%-- 查询URL --%>
	<s:url id="search_url" action="BINOLBSRES01_search"/>
	<s:url id="export_url" action="BINOLBSRES01_export"/>	
	<s:url id="add_url" action="BINOLBSRES04_init"/>
	<s:hidden name="search_url" value="%{search_url}"/>
	<s:text name="global.page.select" id="select_default"/>
	<div class="panel-header">
		<div class="clearfix"> 
			<span class="breadcrumb left">	    
				<jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
			</span>		
       		<%-- 添加按钮 --%>
       		<span class="right"> 
       		    <cherry:show domId="BSRES01ADD">
       	     	 	<a href="${add_url}" class="add" onclick="javascript:openWin(this);return false;">
       		 		<span class="button-text"><s:text name="RES01_add"/></span>
       		 		<span class="ui-icon icon-add"></span>
       		 		</a>
       		    </cherry:show> 
       		</span>
       		<s:url id="export" action="BINOLBSRES01_export" ></s:url>
        	<a id="downUrl" href="${export}"></a>  
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
		<cherry:form id="mainForm">			
		<s:hidden name="provinceId" id="provinceId"/>
          <s:hidden name="cityId" id="cityId"/>	
			<div class="box">
				<div class="box-header">
					<strong>
		            	<span class="ui-icon icon-ttl-search"></span>
		            	<%-- 查询条件 --%>
		            	<s:text name="RES01_condition"/>
		            </strong>
		            <input id="validFlag" type="checkbox" value="1" name="validFlag">
		            <s:text name="RES01_hasDis"/>
				</div>
				<div class="box-content clearfix">
					<div class="column" style="width:50%;">
						<p>
		                	<%-- 所属品牌 --%>
		                	<s:if test="%{brandInfoList.size()> 1}">
		                    	<label><s:text name="RES01_brand"></s:text></label>
		                    	<s:text name="RES01_select" id="RES01_select"/>
		                    	<s:select name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" headerKey="" headerValue="%{#RES01_select}" cssStyle="width:100px;"></s:select>
		                           	</s:if>
		                    <s:else>
		                		<label><s:text name="RES01_brand"></s:text></label>
		                    	<s:select name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" cssStyle="width:100px;"></s:select>      	
		                	</s:else>
	                	</p>
	                	<p>
							<label>
								<s:text name="RES01_resellerName" />
							</label>
							<input name="resellerName" class="text">
						</p>	
						<p>
							<label>
								<s:text name="RES01_resellerCode" />
							</label>
							<input name="resellerCode" class="text">
						</p> 
						<p>
							<label><s:text name="RES01_type"/></label>
							<s:select list='#application.CodeTable.getCodes("1314")' listKey="CodeKey" listValue="Value" name="type" headerKey="" headerValue="%{#select_default}" ></s:select>						
						</p>			
					</div>
					<div class="column last" style="width:49%;">			
						<p>
							<label><s:text name="RES01_provinceName"/></label>
							<a id="province" class="ui-select" style="margin-left: 0px; font-size: 12px;">
								<span id="provinceText" class="button-text choice"><s:text name="global.page.select"/></span>
								<span class="ui-icon ui-icon-triangle-1-s"></span>
							</a>						
						</p>
						<p>
							<label><s:text name="RES01_cityName"/></label>
							<a id="city" class="ui-select" style="margin-left: 0px; font-size: 12px;">
							    <span id="cityText" class="button-text choice"><s:text name="global.page.select"/></span>
							    <span class="ui-icon ui-icon-triangle-1-s"></span>
							</a>						
						</p>
						<p>
							<label><s:text name="RES01_levelCode"/></label>
							<s:select name="levelCode" list='#application.CodeTable.getCodes("1315")' listKey="CodeKey" listValue="Value"  headerKey="" headerValue="%{#select_default}"  ></s:select>
						</p>
					</div>
				</div>
				<p class="clearfix">
	                <%-- 查询 --%>
	              	<button class="right search"  onclick="BINOLBSRES01.search();return false;">
	            		<span class="ui-icon icon-search-big"></span>
	            		<span class="button-text"><s:text name="RES01_search"/></span>
	              	</button>
	            </p>
            </div>
		</cherry:form>
		
		<%-- 查询结果一览 --%>
		<div class="section hide" id="section">
			<div class="section-header">
				<strong>
		          	<span class="ui-icon icon-ttl-section-search-result"></span>		          	
		          	<s:text name="RES01_results_list"/>
		         </strong>
			</div>			
			<div class="section-content" id="result_list">
	            <div class="toolbar clearfix">
	            	<span class="left">
	            		<s:url action="BINOLBSRES01_disable" id="disableRES"></s:url>
    					<s:url action="BINOLBSRES01_enable" id="enableRES"></s:url>
		           		<cherry:show domId="BSRES01EXP">
							<a  class="export left" onclick="BINOLBSRES01.exportExcel(this,'0');return false;"  href="${export_url }">
								<span class="ui-icon icon-export"></span>
								<span class="button-text"><s:text name="RES01_exportEXCEL" /></span>
							</a>
							<a  class="export left" onclick="BINOLBSRES01.exportExcel(this,'1');return false;"  href="${export_url }">
								<span class="ui-icon icon-export"></span>
								<span class="button-text"><s:text name="RES01_exprotCSV" /></span>
							</a>
						</cherry:show>           		
		           		<%--渠道一览启用 --%>
		            	<cherry:show domId="BSRES01ENABLE">
	                   		<a href="${enableRES}" id="enableBtn" class="add" onclick="confirmInit(this,'enable');return false;">
								<span class="ui-icon icon-enable"></span>
	                   			<span class="button-text"><s:text name="RES01_enable"/></span>
	                		</a>
	                	</cherry:show>
	                	<%--经销商一览停用 --%>
	                	<cherry:show domId="BSRES01DISABLE">
	                   		<a href="${disableRES}" id="disableBtn" class="delete" onclick="confirmInit(this,'disable');return false;">
	                   			<span class="ui-icon icon-disable"></span>
	                   			<span class="button-text"><s:text name="RES01_disable"/></span>
	                		</a>
	                	</cherry:show>
                	</span>
	             	<span class="right">
		            	<a class="setting">
		            		<span class="ui-icon icon-setting"></span>
	            			<span class="button-text">
		            		<%-- RES01_colSetting--%>
		             			<s:text name="RES01_colSetting"/>
	             			</span>
		             	</a>
					</span>
	          	</div>
				<div id="resultList">
					<table id="dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
						<thead>
							<tr>
								<th><s:checkbox name="allSelect" id="allSelect" onclick="BINOLBSRES01.checkSelectAll(this)"/></th><%-- 选择 --%>  
								<th><s:text name="RES01_resellerCode"/></th>
								<th><s:text name="RES01_resellerName"/></th>
								<th><s:text name="RES01_type"/></th>																				
								<th><s:text name="RES01_levelCode"/></th>            
								<th><s:text name="RES01_parentResellerCode"/></th>
								<th><s:text name="RES01_parentResellerName"/></th>
								<th><s:text name="RES01_regionName"/></th>            
								<th><s:text name="RES01_provinceName"/></th>            
								<th><s:text name="RES01_cityName"/></th>            
								<th><s:text name="RES01_legalPerson"/></th>
								<th><s:text name="RES01_telephone"/></th>
								<th><s:text name="RES01_mobile"/></th>	             
								<th><s:text name="RES01_validFlag"/></th>								            
							</tr>
						</thead>
					</table>
				</div>
			</div>
		</div>
	</div>
	
	<div class="hide" id="dialogInit"></div>
	<div style="display: none;">
		<div id="disableText"><p class="message"><span><s:text name="RES01_disableText"/></span></p></div>
		<div id="disableTitle"><s:text name="RES01_disableTitle"/></div>
		<div id="enableText"><p class="message"><span><s:text name="RES01_enableText"/></span></p></div>
		<div id="enableTitle"><s:text name="RES01_enableTitle"/></div>
		<div id="dialogConfirm"><s:text name="global.page.ok" /></div>
		<div id="dialogCancel"><s:text name="global.page.cancle" /></div>
		<div id="dialogClose"><s:text name="global.page.close" /></div>
	</div>
	<div id="ajaxResultMsg" class="hide">
		<s:text name="save_success_message" />
	</div>
<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<jsp:include page="/WEB-INF/jsp/common/popExportDialog.jsp" flush="true"/>
<%-- ================== dataTable共通导入    END  ======================= --%>	


<%-- ================== 省市选择DIV START ======================= --%>
<div id="provinceTemp" class="ui-option hide" style="width:325px;">
	<div class="clearfix">
		<span class="label"><s:text name="global.page.range"></s:text></span>
		<ul class="u2"><li onclick="bscom03_getNextRegin(this, '${select_default }', 1);return false;"><s:text name="global.page.all"></s:text></li></ul>
	</div>
	<s:iterator value="reginList" id="reginMap">
    	<div class="clearfix"><span class="label"><s:property value="reginName"/></span>
    	<ul class="u2">
     		<s:iterator value="%{#reginMap.provinceList}">
         		<li id="<s:property value="provinceId" />" onclick="bscom03_getNextRegin(this, '${select_default }', 1);">
         			<s:property value="provinceName"/>
         		</li>
      		</s:iterator>
      	</ul>
    	</div>
   	</s:iterator>
</div>
<div id="cityTemp" class="ui-option hide"></div>
<%-- 下级区域查询URL --%>
<s:url id="querySubRegionUrl" value="/common/BINOLCM00_querySubRegion"></s:url>
<s:hidden name="querySubRegionUrl" value="%{querySubRegionUrl}"/>
<span id="defaultTitle" class="hide"><s:text name="global.page.range"></s:text></span>
<span id="defaultText" class="hide"><s:text name="global.page.all"></s:text></span>
	<%-- ================== 省市选择DIV  END  ======================= --%>
</s:i18n>


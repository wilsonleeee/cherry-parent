<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<link rel="stylesheet" href="/Cherry/css/cherry/combobox.css" type="text/css">
<script type="text/javascript" src="/Cherry/js/bs/common/BINOLBSCOM03.js"></script>
<script type="text/javascript" src="/Cherry/js/bs/pat/BINOLBSPAT01.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<style>
/* Firefox hack */
@-moz-document url-prefix(){td{height:26px;}}
</style>
<s:i18n name="i18n.bs.BINOLBSPAT01">
<s:text name="global.page.select" id="select_default"/>
<%-- 品牌ID --%>
  <s:set id="brandInfoId" value="brandInfoId" />
    <s:url action="BINOLBSPAT01_disable" id="disablePartner"></s:url>
    <s:url action="BINOLBSPAT01_enable" id="enablePartner"></s:url>
    <s:url id="add_url" action="BINOLBSPATADD_init"/>
	<s:url id="search_url" value="/basis/BINOLBSPAT01_search"/>
	<div class="hide">
		<a id="searchUrl" href="${search_url}"></a>
		</div>
	      <div class="panel-header">
	      	<%-- 往来单位一览 --%>
	        <div class="clearfix"> 
			<span class="breadcrumb left">	    
				<jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
			</span>
	            <%-- 添加按钮 --%>
       		    <span class="right"> 
       		    <cherry:show domId="BSPAT02ADD">
       	     	 	<a href="${add_url}" class="add" onclick="javascript:openWin(this);return false;">
       		 		<span class="button-text"><s:text name="PAT02_add"/></span>
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
   			    <ul><li><span><s:text name="EBS00047"/></span></li></ul>         
    		</div>
    	</div>
   		 <%-- ================== 错误信息提示   END  ======================= --%>
	      <div id="actionResultDisplay"></div>
	      <div class="panel-content">
	        <div class="box">
	          <cherry:form id="mainForm" class="inline">
	           <input type="hidden" id="currentUnitID" name="currentUnitID" value="BINOLBSPAT01"/>
	           <s:hidden name="provinceId" id="provinceId"/>
          	   <s:hidden name="cityId" id="cityId"/>
	            <div class="box-header">
	            <strong>
	            	<span class="ui-icon icon-ttl-search"></span>
	            	<%-- 查询条件 --%>
	            	<s:text name="PAT01_condition"/>
	            </strong>
	            <input type="checkbox" name="validFlag" value="1"/><s:text name="PAT01_invalid"/>
	               </div>
	            <div class="box-content clearfix">
				<div class="column" style="width:50%;">
					<p>
					    <%-- 所属品牌 --%>	
						<label><s:text name="PAT01_brandName"/></label>
						<s:if test="%{brandInfoList.size()> 1}">
							<s:select list="brandInfoList" listKey="brandInfoId" listValue="brandName" name="brandInfoId" headerKey="" headerValue="%{#select_default}" onchange="BINOLBSPAT01.changeBrandInfo(this,'%{#select_default}');"></s:select>
						</s:if><s:else>
							<s:select name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" cssStyle="width:100px;"></s:select>
						</s:else>
					</p>
					<p>
					 	<%--单位编码 --%>
						<label><s:text name="PAT01_Code"/></label>
						<s:textfield name="code" cssClass="text" maxlength="16" />
					</p>
					<p>
						<%-- 单位名称 --%>
						<label><s:text name="PAT01_Name"/></label>
						<s:textfield name="name" cssClass="text" maxlength="16" />
					</p>
				</div>
				<div class="column last" style="width:49%;">
	              <p style="margin: 0.2em 0 1em;">
	                <%-- 所属省份 --%>
	                <label><s:text name="PAT01_provinceName"/></label>
	                <a id="province" class="ui-select" style="margin-left: 0px; font-size: 12px;">
	                	<span id="provinceText" class="button-text choice"><s:text name="global.page.select"/></span>
	     		 		<span class="ui-icon ui-icon-triangle-1-s"></span>
	     		 	</a>
	              </p>
	              <p style="margin: 0.4em 0 0.8em;">
	                <%-- 所属城市 --%>
	                <label><s:text name="PAT01_cityName"/></label>
	                <a id="city" class="ui-select" style="margin-left: 0px; font-size: 12px;">
	                	<span id="cityText" class="button-text choice"><s:text name="global.page.select"/></span>
	     		 		<span class="ui-icon ui-icon-triangle-1-s"></span>
	     		 	</a>
	              </p>           
				</div>
	            </div>
	            <p class="clearfix">
	                <%-- 查询 --%>
	              	<button class="right search"  onclick="BINOLBSPAT01.search();return false">
	              			<span class="ui-icon icon-search-big"></span>
	              			<span class="button-text"><s:text name="PAT01_search"/></span>
	              	</button>
	            </p>
	          </cherry:form>
	        </div>
	        <div id="section" class="section hide">
	          <div class="section-header">
	          <strong>
	          	<span class="ui-icon icon-ttl-section-search-result"></span>
	          	<%-- 查询结果一览 --%>
	          	<s:text name="PAT01_results_list"/>
	         </strong>
	        </div>
	        
	          <div class="section-content" id="result_list">
	            <div class="toolbar clearfix">
	            <span class="left">
	            <%--往来单位一览启用 --%>
	            <cherry:show domId="BINOLBSPAT0101">
                   <a href="${enablePartner}" id="enableBtn" class="add" onclick="BINOLBSPAT01.confirmInit(this,'enable');return false;">
                   <span class="ui-icon icon-enable"></span>
                   <span class="button-text"><s:text name="PAT01_enable"/></span>
                </a>
                </cherry:show>
                <%--往来单位一览停用 --%>
                <cherry:show domId="BINOLBSPAT0102">
                   <a href="${disablePartner}" id="disableBtn" class="delete" onclick="BINOLBSPAT01.confirmInit(this,'disable');return false;">
                   <span class="ui-icon icon-disable"></span>
                   <span class="button-text"><s:text name="PAT01_disable"/></span>
                </a>
                </cherry:show>
                </span>
	            <span class="right"><a class="setting"><span class="ui-icon icon-setting"></span>
	            <span class="button-text">
	             <%-- 设置列 --%>
	             <s:text name="PAT01_colSetting"/>
	             </span></a></span></div>
	          </div>
	        </div>
	      </div>
	      <div id="result_table" class="hide">
	      	<table id="dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
	              <thead>
	                <tr>	             
	                  <th><s:checkbox name="allSelect" id="allSelect" onclick="BINOLBSPAT01.checkSelectAll(this)"/></th><%-- 选择 --%>  
	                  <th><s:text name="PAT01_num"/></th><%-- No. --%>
	                  <th><s:text name="PAT01_Code"/></th><%--编码 --%>
	                  <th><s:text name="PAT01_Name_CN"/></th><%-- 往来单位名称 --%>
	                  <th><s:text name="PAT01_Name_EN"/></th><%-- 往来单位类型 --%>
	                  <th><s:text name="PAT01_region"/></th><%--大区（地域）--%>
	                  <th><s:text name="PAT01_province"/></th><%-- 省份 --%>
	                  <th><s:text name="PAT01_city"/></th><%-- 城市 --%>
	                  <th><s:text name="PAT01_phoneNumber"/></th><%-- 电话号码--%>
	                  <th><s:text name="PAT01_postalCode"/></th><%-- 邮编--%>
	                  <th><s:text name="PAT01_contactPerson"/></th><%-- 联系人 --%>
	                  <th><s:text name="PAT01_contactAddress"/></th><%-- 联系地址 --%>
	                  <th><s:text name="PAT01_Adress"/></th><%-- 办公地址 --%>
	                  <th><s:text name="PAT01_deliverAddress"/></th><%-- 送货地址 --%>
	                  <th><s:text name="PAT01_validFlag"/></th><%-- 有效区分--%>
	                  <th><s:text name="PAT01_Operate"/></th><%-- 操作--%>
	                </tr>
	              </thead>
	            </table>
	      </div>
	        <div class="hide" id="dialogInit"></div>
      <div style="display: none;">
      <div id="disableText"><p class="message"><span><s:text name="PAT01_disableText"/></span></p></div>
      <div id="disableTitle"><s:text name="PAT01_disableTitle"/></div>
      <div id="enableText"><p class="message"><span><s:text name="PAT01_enableText"/></span></p></div>
      <div id="enableTitle"><s:text name="PAT01_enableTitle"/></div>
      <div id="dialogConfirm"><s:text name="global.page.ok" /></div>
      <div id="dialogCancel"><s:text name="global.page.cancle" /></div>
      <div id="dialogClose"><s:text name="global.page.close" /></div>
      </div>
      
    <%-- ================== 省市选择DIV START ======================= --%>
	<div id="provinceTemp" class="ui-option hide" style="width:325px;">
	<s:if test="%{reginList != null && !reginList.isEmpty()}">
	<div class="clearfix">
		<span class="label"><s:text name="global.page.range"></s:text></span>
		<ul class="u2"><li onclick="bscom03_getNextRegin(this, '${select_default }', 1);return false;"><s:text name="global.page.all"></s:text></li></ul>
	</div>
	<s:iterator value="reginList">
 		<s:set name="provinceList" value="provinceList" />
    	<div class="clearfix"><span class="label"><s:property value="reginName"/></span>
    	<ul class="u2">
     		<s:iterator value="#provinceList">
         		<li id="<s:property value="provinceId" />" onclick="bscom03_getNextRegin(this, '${select_default }', 1);">
         			<s:property value="provinceName"/>
         		</li>
      		</s:iterator>
      	</ul>
    	</div>
   	</s:iterator>
   	</s:if>
	</div>
	<div id="cityTemp" class="ui-option hide"></div>
	<%-- 下级区域查询URL --%>
	<s:url id="querySubRegionUrl" value="/common/BINOLCM00_querySubRegion"></s:url>
	<s:hidden name="querySubRegionUrl" value="%{querySubRegionUrl}"/>
	<span id="defaultTitle" class="hide"><s:text name="global.page.range"></s:text></span>
	<span id="defaultText" class="hide"><s:text name="global.page.all"></s:text></span>
	<%-- ================== 省市选择DIV  END  ======================= --%>
	
	<div id="ajaxResultMsg" class="hide">
	  <s:text name="save_success_message" />
	</div>
</s:i18n>
<%-- 根据品牌ID筛选下拉列表URL --%>
<s:url id="filter_url" value="BINOLBSPAT01_filter"/>
<s:hidden name="filter_url" value="%{filter_url}"/>
<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>
		
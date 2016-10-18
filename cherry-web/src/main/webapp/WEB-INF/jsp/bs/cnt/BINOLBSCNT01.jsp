<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/bs/common/BINOLBSCOM03.js"></script>
<script type="text/javascript" src="/Cherry/js/bs/cnt/BINOLBSCNT01.js"></script>


<s:i18n name="i18n.bs.BINOLBSCNT01">
	<s:text name="global.page.select" id="select_default"/>
    <div class="panel-header">
	    <div class="clearfix"> 
	 	 <span class="breadcrumb left">	    
			<jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
		 </span>
	    
	    <span class="right">
	    	<cherry:show domId="BINOLBSCNT0105">
	    	<%-- 实时刷新数据权限URL --%>
			<s:url id="refreshPrivilegeUrl" value="/common/BINOLCMPL04_init" />
	    	<a class="add" href="" onclick="javascript:privilegeRefreshConfirm('${refreshPrivilegeUrl }');return false;"><span class="ui-icon icon-refresh-s"></span><span class="button-text"><s:text name="global.page.privilegeTitle"></s:text></span></a>
	   		</cherry:show>
	    	<cherry:show domId="BINOLBSCNT0101">
	    	<s:url action="BINOLBSCNT04_init" id="addCounterUrl"></s:url>
	    	<a class="add" href="${addCounterUrl }" onclick="javascript:openWin(this);return false;"><span class="ui-icon icon-add"></span><span class="button-text"><s:text name="counter.addCounter"></s:text></span></a>
	   		</cherry:show>
	    </span>
	    <s:url id="export" action="BINOLBSCNT01_export" ></s:url>
        <a id="downUrl" href="${export}"></a>
	    </div>
    </div>
    <%-- ================== 错误信息提示 START ======================= --%>
    <div id="actionResultDisplay"></div>
    <div id="errorMessage"></div>    
    <div style="display: none" id="errorMessageTemp">
    <div class="actionError">
       <ul><li><span><s:text name="counter.errorMessage"/></span></li></ul>         
    </div>
    </div>
    <%-- ================== 错误信息提示   END  ======================= --%>
    <div class="panel-content">
      <div class="box">
        <cherry:form id="mainForm" action="/basis/search" method="post" class="inline" onsubmit="binolbscnt01.search();return false;">
          <s:hidden name="provinceId" id="provinceId"/>
          <s:hidden name="cityId" id="cityId"/>
          <div class="box-header">
          	<strong><span class="ui-icon icon-ttl-search"></span><s:text name="global.page.condition"/></strong>
            <input type="checkbox" name="validFlag" id="validFlag" value="1"/><s:text name="counter.invalid"/>
          </div>
          <div class="box-content clearfix">
            <div class="column" style="width:50%; height: auto;">
              <s:if test="%{brandInfoList != null && !brandInfoList.isEmpty()}">
	              <p>
	               	<%-- 所属品牌 --%>	
	               	<label><s:text name="counter.brandNameChinese"/></label>
	               	<s:select list="brandInfoList" listKey="brandInfoId" listValue="brandName" name="brandInfoId" headerKey="" headerValue="%{#select_default}" onchange="binolbscnt01.changeBrandInfo(this,'%{#select_default}');"></s:select>
	              </p>
              </s:if>
              <p>
              	<%-- 柜台名称 --%>
              	<label><s:text name="counter.counterNameIF"/></label>
                <s:textfield name="counterNameIF" cssClass="text"/>
              </p>
              <p>
               	<%-- 柜台号 --%>	
               	<label><s:text name="counter.counterCode"/></label>
               	<s:textfield name="counterCode" cssClass="text"/>
              </p>
               <p>
               	<%-- 柜台地址 --%>	
               	<label><s:text name="counter.counterAddress"/></label>
               	<s:textfield name="counterAddress" cssClass="text"/>
              </p>
              <p>
               	<%-- 柜台主管名 --%>	
               	<label><s:text name="counter.counterHeader"/></label>
               	<s:textfield name="counterBAS" cssClass="text"/>
              </p>
              <p>
               	<%-- 业务负责人 --%>	
               	<label><s:text name="counter.busniessPrincipal"/></label>
               	<s:textfield name="busniessPrincipal" cssClass="text"/>
              </p>
            </div>
            <div class="column last" style="width:49%; height: auto;">
              <p style="margin: 0.2em 0 1em;">
                <%-- 所属省份 --%>
                <label><s:text name="counter.province"/></label>
                <a id="province" class="ui-select" style="margin-left: 0px; font-size: 12px;">
                	<span id="provinceText" class="button-text choice"><s:text name="global.page.select"/></span>
     		 		<span class="ui-icon ui-icon-triangle-1-s"></span>
     		 	</a>
              </p>
              <p style="margin: 0.4em 0 0.8em;">
                <%-- 所属城市 --%>
                <label><s:text name="counter.city"/></label>
                <a id="city" class="ui-select" style="margin-left: 0px; font-size: 12px;">
                	<span id="cityText" class="button-text choice"><s:text name="global.page.select"/></span>
     		 		<span class="ui-icon ui-icon-triangle-1-s"></span>
     		 	</a>
              </p>
              <p>
               	<%-- 渠道 --%>	
               	<label><s:text name="counter.channelName"/></label>
               	<s:if test="%{channelList != null && !channelList.isEmpty()}">
               		<s:select list="channelList" listKey="channelId" listValue="channelName" name="channelId" headerKey="" headerValue="%{#select_default}"></s:select>
               	</s:if>
               	<s:else>
               		<select name="channelId" id="channelId"><option value="">${select_default }</option></select>
               	</s:else>
              </p>
              <p>
               	<%-- 柜台状态 --%>	
               	<label><s:text name="counter.status"/></label>
               	<s:select list='#application.CodeTable.getCodes("1030")' listKey="CodeKey" listValue="Value" name="status" headerKey="" headerValue="%{#select_default}"></s:select>
				<input type="hidden" value='<s:text name="maintainCoutSynergy"/>' id="maintainCoutSynergy">
              </p>
            </div>
          </div>
          <p class="clearfix">
         	<cherry:show domId="BINOLBSCNT0106">
         	<%-- 柜台查询按钮 --%>
         	<button class="right search" onclick="binolbscnt01.search();return false;">
         		<span class="ui-icon icon-search-big"></span><span class="button-text"><s:text name="global.page.search"/></span>
         	</button>	
         	</cherry:show>
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
           	<span class="left">
           	   <cherry:show domId="BINOLBSCNT01EXP">
           	    <a id="export" class="export" onclick="binolbscnt01.exportExcel();return false;">
                   <span class="ui-icon icon-export"></span>
                   <span class="button-text"><s:text name="global.page.export"/></span>
                </a>
                 </cherry:show>
           		<s:url action="BINOLBSCNT05_delete" id="delCounter"></s:url>
                <cherry:show domId="BINOLBSCNT0102">
                <a href="" class="add" onclick="binolbscnt01.editValidFlag('enable','${delCounter}');return false;">
                   <span class="ui-icon icon-enable"></span>
                   <span class="button-text"><s:text name="global.page.enable"/></span>
                </a>
                </cherry:show>
                <cherry:show domId="BINOLBSCNT0103">
                <a href="" class="delete" onclick="binolbscnt01.editValidFlag('disable','${delCounter}');return false;">
                   <span class="ui-icon icon-disable"></span>
                   <span class="button-text"><s:text name="global.page.disable"/></span>
                </a>
                </cherry:show>
                
            </span>
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
                <th><input type="checkbox" id="checkAll" onclick="bscom03_checkRecord(this,'#dataTable_Cloned');"/></th>
                <%-- 柜台号 --%>
                <th><s:text name="counter.counterCode"/><span class="ui-icon ui-icon-document"></span></th>
                <%-- 柜台名称 --%>
                <th><s:text name="counter.counterNameIF"/></th>
                <%-- 柜台主管 --%>
                <th><s:text name="counter.counterHeader"/></th>
                <%-- 所属品牌 --%>
                <th><s:text name="counter.brandNameChinese"/></th>
                <%-- 所属大区 --%>
                <th><s:text name="counter.region"/></th>
                <%-- 所属省份 --%>
                <th><s:text name="counter.province"/></th>
                <%-- 所属城市 --%>
                <th><s:text name="counter.city"/></th>
                <%-- 运营模式--%>
                <th><s:text name="counter.operateMode"/></th>
                <%-- 所属系统--%>
                <th><s:text name="counter.belongFaction"/></th>
                <%-- 开票单位--%>
                <th><s:text name="counter.invoiceCompany"/></th>
                <%-- 渠道 --%>
                <th><s:text name="counter.channelName"/></th>
                <%-- 经销商 --%>
                <th><s:text name="counter.resellerName"/></th>
                <%-- 经销商部门 --%>
                <th><s:text name="counter.resellerDepartName"/></th>
                <%-- 柜台状态 --%>
                <th><s:text name="counter.status"/></th>
                <%-- 是否有POS机 --%>
                <th><s:text name="counter.posFlag"/></th>
                <%-- 柜台员工数--%>
                <th><s:text name="counter.employeeNum"/></th>
                <%-- 柜台地址 --%>
                <th><s:text name="counter.counterAddress"/></th>
                <%-- 柜台协同区分 --%>
                <s:if test="maintainCoutSynergy" ><th><s:text name="counter.counterSynergyFlag"/></th></s:if>
                <%-- 业务负责人 --%>
				<th><s:text name="counter.busniessPrincipal"/></th>
				<%-- 银联设备号 --%>
				<th><s:text name="counter.equipmentCode"/></th>
				<%-- 柜台类型--%>
				<th><s:text name="counter.managingType2"/></th>
                <%-- 有效区分 --%>
                <th><s:text name="counter.validFlag"/></th>
              </tr>
            </thead>           
          </table>
        </div>
      </div>
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
	<div style="display: none;">
	<div id="privilegeTitle"><s:text name="global.page.privilegeTitle" /></div>
	<div id="privileMessage"><p class="message"><span><s:text name="global.page.privileMessage" /></span></p></div>
	<div id="disableTitle"><s:text name="counter.disableTitle" /></div>
	<div id="enableTitle"><s:text name="counter.enableTitle" /></div>
	<div id="disableMessage"><p class="message"><span><s:text name="counter.disableMessage" /></span></p></div>
	<div id="enableMessage"><p class="message"><span><s:text name="counter.enableMessage" /></span></p></div>
	<div id="dialogConfirm"><s:text name="global.page.ok" /></div>
    <div id="dialogCancel"><s:text name="global.page.cancle" /></div>
    <div id="dialogClose"><s:text name="global.page.close" /></div>
</div>  
</s:i18n>

<%-- 员工查询URL --%>
<s:url id="search_url" value="BINOLBSCNT01_search"/>
<s:hidden name="search_url" value="%{search_url}"/>
<%-- 根据品牌ID筛选下拉列表URL --%>
<s:url id="filter_url" value="BINOLBSCNT01_filter"/>
<s:hidden name="filter_url" value="%{filter_url}"/>
<div class="hide" id="dialogInit"></div>    
<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>

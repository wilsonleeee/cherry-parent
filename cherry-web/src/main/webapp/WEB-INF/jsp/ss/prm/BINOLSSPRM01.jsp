<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/ss/prm/BINOLSSPRM01.js"></script>
     <%-- 促销品查询URL --%>
     <s:url id="search_url" value="/ss/BINOLSSPRM01_search"/>
     <div class="hide">
     	<a id="searchUrl" href="${search_url}"></a>
     </div>
     
     <%-- 促销品启用、停用URL--%>
     <s:url id="operate_url" value="/ss/BINOLSSPRM01_operate"/>

     <%-- 促销品添加URL --%>
     <s:url id="add_url" action="BINOLSSPRM02_init"/>

     <s:i18n name="i18n.ss.BINOLSSPRM01">
		<s:text id="selectAll" name="global.page.all"/>
      <div class="panel-header">
        <div class="clearfix">
		<span class="breadcrumb left">	    
			<jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
		</span>
       		<span class="right"> 
       		<%-- 促销产品下发按钮 --%>
       		<cherry:show domId="BINOLSSPRM01ISS">
       		 	<a href="#" class="add" onclick="issuedInit();return false;">
       		 		<span class="button-text"><s:text name="issPrmPrt"/></span>
       		 		<span class="ui-icon icon-down"></span>
       		 	</a>
       		 </cherry:show>
       		<%-- 促销品添加按钮 --%>
       		<cherry:show domId="SSPRM0101ADPRM">
       		 	<a href="${add_url}" class="add" onclick="javascript:openWin(this);return false;">
       		 		<span class="button-text"><s:text name="prm_add"/></span>
       		 		<span class="ui-icon icon-add"></span>
       		 	</a>
       		 </cherry:show>
       		 <%-- 促销品批量导入按钮 --%>
       		<%--<cherry:show domId="SSPRM0101IM">
       		 	<a href="#" class="import">
       		 		<span class="button-text"><s:text name="prm_import"/></span>
       		 		<span class="ui-icon icon-import"></span>
       		 	</a>
       		</cherry:show>--%>
       		</span>
       		   <s:url id="export" action="BINOLSSPRM01_export" ></s:url>
        <a id="downUrl" href="${export}"></a>
       	</div>
      </div>
      <%-- ================== 错误信息提示 START ======================= --%>
      <div id="errorMessage"></div>
      <%-- ================== 错误信息提示   END  ======================= --%>
      <div class="panel-content">
        <div class="box">
          <cherry:form id="mainForm" class="inline" onsubmit="search(); return false;">
            <div class="box-header">
            	<strong><span class="ui-icon icon-ttl-search"></span><s:text name="global.page.condition"/></strong>
            </div>
            <div class="box-content clearfix">
               <div class="column" style="width:49%;">
               <p>
               		<label style="width: 95px;"><s:text name="prm.brandInfo"/></label>
               		<s:select name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName"/>
               	</p>
               	 <p>
                  <label style="width: 95px;"><s:text name="prm.promCate"/></label><%-- 促销品类型 --%>
                   <s:select name="promCate" id="promCate" list='#application.CodeTable.getCodes("1139")' listKey="CodeKey" listValue="Value"
                  	headerKey="" headerValue="%{selectAll}" value="'CXLP'"  onchange="validateType(this.value)"/>
                </p>
               <p>
                  <label style="width: 95px;"><s:text name="prm.validFlag"/></label><%-- 有效状态 --%>
                   <s:select name="validFlag" list='#application.CodeTable.getCodes("1137")' listKey="CodeKey" listValue="Value"
                  	headerKey="" headerValue="%{selectAll}" value="1" />
                </p>
                  <p>
                  <label style="width: 95px;"><s:text name="prm.isPosIss"/></label><%-- 是否下发到POS --%>
                   <s:select name="isPosIss" list='#application.CodeTable.getCodes("1341")' listKey="CodeKey" listValue="Value"
                  	headerKey="" headerValue="%{selectAll}" />
                </p>
              </div>
              <div class="column last" style="width:50%;"> 
	   			<p>
                 <%-- 促销品厂商编码--%>
                  <label style="width: 80px;"><s:text name="prm.unitCode"/></label>
                  <s:textfield name="unitCode" cssClass = "text"/>
                 </p>
                 <p>
				<%-- 促销品条码--%>
                  <label style="width: 80px;"><s:text name="prm.barCode"/></label>
                  <s:textfield name="barCode" cssClass = "text"/>
                 </p>
                <p>  
                <%-- 促销品全名--%>
                  <label style="width: 80px;"><s:text name="prm.nameTotal"/></label>
                  <s:textfield name="nameTotal" cssClass="text" maxlength="50" />
                </p>
                <p id="validate_mode"> 
                <%-- 促销品类型--%>
                  <label style="width: 80px;"><s:text name="prm.mode"/></label>
                  <s:select name="mode" id="mode" list='#application.CodeTable.getCodes("1345")' listKey="CodeKey" listValue="Value"
                  	headerKey="" headerValue="%{selectAll}" />
                </p>
              </div>
              
            </div>
            <p class="clearfix">
              	<%--<cherry:show domId="SSPRM0101QU">--%>
              		<%-- 促销品查询按钮 --%>
              		<button class="right search" type="submit" onclick="search();return false;">
              			<span class="ui-icon icon-search-big"></span><span class="button-text"><s:text name="global.page.search"/></span>
              		</button>
              	<%--</cherry:show>--%>
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
	           <cherry:show domId="BINOLSSPRM01EXP">
	           	    <a id="export" class="export" onclick="exportExcel();return false;">
	                   <span class="ui-icon icon-export"></span>
	                   <span class="button-text"><s:text name="global.page.export"/></span>
	                </a>
	          </cherry:show>
            </span>
            	<!--<span class="left">
            	<%-- 全选 --%>
              		<input id="selectAll" type="checkbox" class="checkbox" onclick="select_All();"/><s:text name="selectAll"/>
              		
              		<cherry:show domId="SSPRM0101DB">
              		<%-- 促销品停用按钮 --%>
              		<a href="#" id="disable_prm" class="delete" onclick="operate_prm(this,'<s:property value="#operate_url"/>',0)">
              			<span class="ui-icon icon-disable"></span>
              			<span class="button-text"><s:text name="disable_prm"/></span>
       		 		</a>
       		 		</cherry:show>
       		 		
       		 		<cherry:show domId="SSPEM0101EB">
       		 		<%-- 促销品启用按钮 --%>
       		 		<a href="#" id="enable_prm" class="add" onclick="operate_prm(this,'<s:property value="#operate_url"/>',1)">
       		 			<span class="ui-icon icon-enable"></span>
              			<span class="button-text"><s:text name="enable_prm"/></span>
       		 		</a>
                    </cherry:show>   		 	
    
                    <cherry:show domId="SSPRM0101EX">
              		<%-- 促销品记录导出 --%>
              		<a href="#" class="export" id="export">
	              			<span class="button-text"><s:text name="export_results"/></span>
	       		 			<span class="ui-icon icon-import"></span>
	              	</a>
	              	 </cherry:show> 
              	</span> 
              	--><span class="right">
              		<%-- 列设置按钮  --%>
              		<a class="setting">
       		 			<span class="ui-icon icon-setting"></span>
       		 			<span class="button-text"><s:text name="global.page.colSetting"/></span>
       		 		</a>
              	</span>
            </div>
            
            <table id="dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
              <thead>
                <tr>
                <%-- 选择 --%>
                  <%--   <th><s:text name="select1"/></th>--%>
                  <%-- 编号 --%>
                  <th><s:text name="number"/></th>
                  <%-- 促销品全名--%>
                  <th><s:text name="prm.nameTotal"/><span class="ui-icon ui-icon-document"></span></th>
                  <%-- 促销品厂商编码 --%>
                  <th><s:text name="prm.unitCode"/></th>
                  <%-- 促销品条码 --%>
                  <th><s:text name="prm.barCode"/></th>
                  <%-- 促销品类型 --%>
                  <th><s:text name="prm.promCate"/></th>
                  <%-- 大分类 --%>
                  <th><s:text name="prm_primaryCategoryName"/></th>
                  <%-- 中分类 --%>
                  <th><s:text name="prm_secondryCategoryName"/></th>
                  <%-- 小分类 --%>
                  <th><s:text name="prm_smallCategoryName"/></th>
                  <%--是否下发到POS --%>
                  <th><s:text name="prm.isPosIss"/></th>
                  <%-- 促销品标准成本 --%>
                  <th><s:text name="prm.standardCost"/></th>
                  <%-- 促销品类型 --%>
                  <th><s:text name="prm.mode"/></th>
                  <%-- 促销品销售价格 --%>
                  <%--<th><s:text name="prm.salePrice"/></th>--%>
                  <%-- 促销品状态 --%>
                  <%--<th><s:text name="prm.status"/></th>--%>
                  <%-- 有效区分 --%>
                  <th><s:text name="prm.validFlag"/></th>
                </tr>
               </thead>           
              </table>
             </div>
            </div>
           </div>
	  <%-- 促销产品实时下发弹窗 --%>
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
		<p id="sysConfigNonSupportId" class="message"><span><s:text name="global.page.sysConfigNonSupport"/></span>
		<div id="issPrmMsg1"><s:text name="issPrmMsg1" /></div>
		<div id="issLaunchingMsg"><s:text name="issLaunchingMsg" /></div>
		<div id="issPrmPrt"><s:text name="issPrmPrt" /></div>
		  <!-- 促销产品下发 -->
		<s:url id="issuedPrmUrl" action="BINOLSSPRM01_issuePrm"/>
		<span class="hide" id="issuedPrmId">${issuedPrmUrl}</span>
      </div>
         </s:i18n>
         <%-- ================== dataTable共通导入 START ======================= --%>
         <jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
         <%-- ================== dataTable共通导入    END  ======================= --%>
         

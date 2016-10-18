<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/pt/unq/BINOLPTUNQ02.js"></script>
<script type="text/javascript" src="/Cherry/plugins/jquery-validate/jquery.validate.min.js"></script>
<s:i18n name="i18n.pt.BINOLPTUNQ02">

<s:url id="search_url" value="/pt/BINOLPTUNQ02_search"/> <%-- 查询唯一码 --%>
<s:hidden name="searchUrlID" value="%{search_url}"/>
<s:url id="searchCheck_url" value="/pt/BINOLPTUNQ02_searchCheck"/> <%-- 单码查询查询条件检测检查 --%>
<s:hidden name="searchCheckUrlID" value="%{searchCheck_url}"/>
<s:url id="initSearch_url" value="/pt/BINOLPTUNQ02_initSearch"/> <%-- 单码查询初始化查询 --%>
<s:hidden name="initSearchUrlID" value="%{initSearch_url}"/>
	<%-- ================== 错误信息提示 START ======================= --%>
	<div id="actionResultDisplay"></div>
	<div id="errorMessage"></div>
	<div style="display: none" id="errorMessageTemp">
		<div class="actionError">
		</div>
	</div>
	<%-- ================== 错误信息提示   END  ======================= --%>
    <div class="panel-header">
		<div class="clearfix">
			<span class="breadcrumb left">	    
				<jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
			</span>
		</div>
    </div>
 <div class="panel-content"> 
  <div class="box">
        <cherry:form id="mainForm"  class="inline">
          <s:hidden name="provinceId" id="provinceId"/>
          <s:hidden name="cityId" id="cityId"/>
          <div class="box-header">
          	<strong><span class="ui-icon icon-ttl-search"></span><s:text name="global.page.condition"/></strong>
          </div>
          <div class="box-content clearfix">
            <div class="column" style="width:100%; height: auto;">
              <p>
              	<%-- 积分唯一码 --%>
              	<label><s:text name="PTUNQ.pointUnqCode"/></label>
                <span id="pointUniqueCodeSrh" ><s:textfield name="pointUniqueCodeSrh" cssClass="text"/></span>
              	<%--关联唯一码--%>
              	<label><s:text name="PTUNQ.relUnqCode"/></label>
                <s:textfield name="relUniqueCodeSrh" cssClass="text"/>
              	<%-- 箱码  --%>
              	<span style="display: none">
              	<label><s:text name="PTUNQ.boxCode"/></label>
                <s:textfield name="boxCodeSrh" cssClass="text"/>
                </span>
              </p>
            </div>
          </div>
          <p class="clearfix">
         	<cherry:show domId="BINOLPTUNQ0201">
         	<%-- 柜台查询按钮 --%>
         	<button class="right search" onclick="BINOLPTUNQ02.searchCheck();return false;">
         		<span class="ui-icon icon-search-big"></span><span class="button-text"><s:text name="global.page.search"/></span>
         	</button>	
         	</cherry:show>
          </p>
        </cherry:form>
    </div>
 	      
   <div id="section" class="section" > <%-- 生成记录具体内容--%>
       <div class="section-header">
        	<strong>
        		<span class="ui-icon icon-ttl-section-search-result"></span><%-- 查询结果一览 --%>
        		<s:text name="global.page.list"/>
        	</strong>
       </div>        
       <div class="section-content">
       			<div class="toolbar clearfix">
					<span class="right"> 
						<a class="setting">
							<span class="ui-icon icon-setting"></span> <%-- 列设置按钮  --%>
							<span class="button-text"><s:text name="global.page.colSetting" /></span>
						</a>
					</span>
				</div>
          <table id="dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%"><%-- 表头  --%>
            <thead>
               <tr>          
                <th><s:text name="PTUNQ.number"/></th>
                <th><s:text name="PTUNQ.manufacturerCode"/></th>
                <th><s:text name="PTUNQ.productBarCode"/></th>
                <th><s:text name="PTUNQ.productName"/></th>
                <th><s:text name="PTUNQ.pointUnqCode"/></th>
                <th><s:text name="PTUNQ.relUnqCode"/></th>
                <th><s:text name="PTUNQ.boxCode"/></th>
				<th><s:text name="PTUNQ.generateTime"/></th>	
				<th><s:text name="PTUNQ.productBigType"/></th>
				<th><s:text name="PTUNQ.productSmallType"/></th>
				<th><s:text name="PTUNQ.activationStatus"/></th>	
				<th><s:text name="PTUNQ.useStatus"/></th>
              </tr>
            </thead>           
           </table>
         </div>
     </div>
  </div>
</s:i18n>

<div class="hide">
	<%-- ================== dataTable共通导入 START ======================= --%>
	<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
</div>

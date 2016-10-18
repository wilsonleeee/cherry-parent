<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/ss/prm/BINOLSSPRM05.js"></script>
    <%-- 促销品分类查询URL --%>
    <s:url id="search_url" value="/ss/BINOLSSPRM05_search"/>
     <div id="searchUrl" class="hide">${search_url}</div>

    <%-- 促销品分类启用、停用URL--%>
    <s:url id="operate_url" value="/ss/BINOLSSPRM05_operate"/>

    <%-- 促销品分类添加URL --%>
    <s:url id="add_url" action="BINOLSSPRM06_init"/>

    <s:i18n name="i18n.ss.BINOLSSPRM05">

      <div class="panel-header">
        <div class="clearfix">
		<span class="breadcrumb left">	    
			<jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
		</span>
       		<span class="right"> 
       		<%-- 分类添加按钮 --%>
       		<cherry:show domId="SSPRM0105ADD">
       		 	<a href="${add_url}" class="add" onclick="javascript:openWin(this);return false;">
       		 		<span class="button-text"><s:text name="prm05_add"/></span>
       		 		<span class="ui-icon icon-add"></span>
       		 	</a>
       		 </cherry:show>
       		 <%-- 分类批量导入按钮 --%>
       		 <%--<cherry:show domId="SSPRM0105IMP">
       		 	<a href="#" class="import">
       		 		<span class="button-text"><s:text name="prm05_import"/></span>
       		 		<span class="ui-icon icon-import"></span>
       		 	</a>
       		</cherry:show>--%>
       		</span>
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
              	<input type="checkbox" name="validFlag" value="1"/><s:text name="invalid_type"/>
            </div>
            <div class="box-content clearfix">
               <div class="column" style="width:50%;">
                <s:if test="null != brandInfoList">
                <p>
                <%-- 所属品牌--%>
                  <label style="width: 80px;"><s:text name="prm05.brandName"/></label>
                  <s:select name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName"></s:select>
                </p>
                </s:if>
                <div>
                 <%-- 分类名称--%>
                  <label style="width: 80px;"><s:text name="prm05.categoryName"/></label>
                  <s:textfield name="categoryName" cssClass="text" maxlength="20"/> 
                 </div>
              </div>
              <div class="column last" style="width:49%;"> 
	   			<div>
				<%-- 分类代码--%>
                  <label style="width: 80px;"><s:text name="prm05.categoryCode"/></label>
                  <s:textfield name="categoryCode" cssClass = "text" maxlength="4"/>
                 </div>
              </div>
            </div>
            <p class="clearfix">
              	<%--<cherry:show domId="SSPRM0105QU">--%>
              		<%-- 促销品分类查询按钮 --%>
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
            	<!--<span class="left">
            	    <%-- 全选 --%>
              		<input id="selectAll" type="checkbox" class="checkbox" onclick="select_All();"/><s:text name="prm05_selectAll"/>
              		
              		<cherry:show domId="SSPRM0105DB">
              		<%-- 分类停用按钮 --%>
              		<a href="#" id="disable_type" class="delete" onclick="operate_type(this,'<s:property value="#operate_url"/>',0)">
              			<span class="ui-icon icon-disable"></span>
              			<span class="button-text"><s:text name="prm05_disable"/></span>
       		 		</a>
       		 		</cherry:show>
       		 		
       		 		<cherry:show domId="SSPEM0105EB">
       		 		<%-- 分类启用按钮 --%>
       		 		<a href="#" id="enable_type" class="add" onclick="operate_type(this,'<s:property value="#operate_url"/>',1)">
       		 			<span class="ui-icon icon-enable"></span>
              			<span class="button-text"><s:text name="prm05_enable"/></span>
       		 		</a>
                    </cherry:show>   		 	
    
                    <cherry:show domId="SSPRM0105EXP">
              		<%-- 类别导出 --%>
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
                  <!--<th><s:text name="prm05.select"/></th>
                  --><%-- 编号 --%>
                  <th><s:text name="prm05.number"/></th>
                  <%-- 大分类代码 --%>
                  <th><s:text name="prm05.primaryCategoryCode"/><span class="ui-icon ui-icon-document"></span></th>
                  <%-- 大分类名称 --%>
                  <th><s:text name="prm05.primaryCategoryName"/></th>
                  <%-- 大分类代码 --%>
                  <th><s:text name="prm05.secondryCategoryCode"/></th>
                  <%-- 大分类名称 --%>
                  <th><s:text name="prm05.secondryCategoryName"/></th>
                  <%-- 大分类代码 --%>
                  <th><s:text name="prm05.smallCategoryCode"/></th>
                  <%-- 大分类名称 --%>
                  <th><s:text name="prm05.smallCategoryName"/></th>
                  <%-- 有效区分 --%>
                  <th><s:text name="prm05.validFlag"/></th>
                </tr>
              </thead>           
             </table>
            </div>
           </div>
          </div>
       </s:i18n>
       <%-- ================== dataTable共通导入 START ======================= --%>
       <jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
       <%-- ================== dataTable共通导入    END  ======================= --%>
  
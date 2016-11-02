<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/common/departBar.js"></script>
<script type="text/javascript" src="/Cherry/js/pt/rps/BINOLPTRPS11.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
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
	//search($("#search_url").val());
</script>
<%-- 取得部门URL --%>
<s:url id="getDepartUrl" value="/common/BINOLCM00_queryDepart"/>
<%-- 取得仓库URL --%>
<s:url id="getInventUrl" value="/common/BINOLCM00_queryInventory"/>
<%-- 库存记录查询URL --%>
<s:url id="search_url" value="/pt/BINOLPTRPS11_search"/>
<%-- 一览明细导出URL --%>
<s:url id="xls_url" value="/pt/BINOLPTRPS11_export"/>
<%-- 一览概要导出URL --%>
<s:url id="summaryXls_url" value="/pt/BINOLPTRPS11_exportSummary"/>

<input type="hidden" value="${search_url}" id="search_url"/>
<s:i18n name="i18n.pt.BINOLPTRPS11">
<s:text id="selectAll" name="global.page.all"/>
<div class="panel-header">
       <div class="clearfix"> 
	        <span class="breadcrumb left">	    
				<jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
			</span>  
       </div>
</div>
<%-- ================== 错误信息提示 START ======================= --%>
<div id="errorMessage"></div>
<%-- ================== 错误信息提示   END  ======================= --%>
<div class="panel-content">
	<div class="box">
		<cherry:form id="mainForm" class="inline">
			<div class="box-header">
				<%-- 查询条件  --%>
				<strong>
					<span class="ui-icon icon-ttl-search"></span>
					<s:text name="global.page.condition"/>
		        </strong>
			</div>
            <div class="box-content clearfix">
               	<div style="padding: 15px 0px 5px;">
               		<table class="detail" cellpadding="0" cellspacing="0" id="rps11searchId">
               		<tbody>
               			<tr>
		                	<%-- 产品名称 --%>
		                  	<th><s:text name="RPS11_nameTotal"/></th>
		                  	<td>
			                  	<s:textfield name="nameTotal" cssClass="text"/>
			                  	<s:if test='"2".equals(type)'><input type="hidden" id="productId" name="productId" value=""/></s:if>
			                  	<s:else><input type="hidden" id="prtVendorId" name="prtVendorId" value=""/></s:else>
			                  	<input type="hidden" id="groupType" name="type" value="<s:property value='type'/>"/>
			                </td>
			                <th><s:text name="RPS11_date"/></th>
		                	<td>
				                <p class="date">
				                	<%-- 日期范围 --%>
				                  	<span><s:textfield id="startDate" name="startDate" cssClass="date"/>- 
				                  	<s:textfield id="endDate" name="endDate" cssClass="date"/></span>
				                </p>
			                </td>
		                </tr>
		                 <tr>
		                	<%-- 产品有效状态 --%>
		                  	<th><s:text name="RPS11_validFlag"/></th>
		                  	<td>
<%-- 			                  	<s:iterator value='#application.CodeTable.getCodes("1137")'>
				                	<input type="radio" name="validFlag" value='<s:property value="CodeKey" />' <s:if test="1 == CodeKey">checked</s:if>/><s:property value="Value" />
				                </s:iterator> --%>
				                <s:select name="validFlag" list='#application.CodeTable.getCodes("1137")' listKey="CodeKey" listValue="Value"
	                  			headerKey="" headerValue="%{selectAll}"  value="1" onchange="getProductBindingFunction()" />
			                </td>
			                <%-- 品牌 --%>
		                  	<th><s:text name="RPS11_originalBrand"/></th>
		                  	<td>
	                  		<s:select name="originalBrand" list='#application.CodeTable.getCodes("1299")' listKey="CodeKey" listValue="Value"
	                  			headerKey="" headerValue="%{selectAll}" />
	                  		</td>
		                </tr>
		                <tr>
		                <th><label><s:text name="RPS11_channel"/></label></th>
		                	<td>
			            	<s:if test="%{channelList != null && !channelList.isEmpty()}">
			               		<s:select list="channelList" listKey="channelId" listValue="channelName" name="channelIdClude" headerKey="" headerValue="%{#selectAll}"></s:select>
			               	</s:if>
			               	<s:else>
			               		<select name="channelIdClude" id="channelIdClude"><option value="">${selectAll }</option></select>
			               	</s:else>
			            	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<input type="checkbox" id="excludeFlag" name="excludeFlag" value="1"><s:text name="RPS11_exclude" />
			            </td>
		                </tr>
			            <tr>
							<td colspan="4" id="dynamicShowHideCate">
								<%-- ======================= 产品分类开始  ========================================================== --%>
								<div class="detail_box2">
									<div class="section-header clearfix">
										<strong class="left"> 
											<span class="ui-icon icon-ttl-search"></span>
											<s:text name="RPS11_cateInfo" /> <%-- 产品分类 --%>
										</strong>
									</div> 
									<%-- ================== 错误信息提示 START ======================= --%>
									<div id="cateInfoMessDiv">
										<div id="errorMessageCate"></div>
										<div style="display: none" id="errorMessageTemp1">
											<div class="actionError">
												<ul>
													<li><span><s:text name="RPS11_cateInfoEmptyError" /></span></li>
												</ul>
											</div>
										</div>
									</div>
									<%-- ================== 错误信息提示   END  ======================= --%>
									<!-- 产品信息分类 -->
									<div id="cateInfo" class="section-content">
										<jsp:include page="/WEB-INF/jsp/pt/jcs/BINOLPTJCS00_1.jsp" flush="true" />
									</div>
								</div>
							 <%-- ======================= 产品分类结束  ========================================================== --%>
							</td>
						</tr>
					</tbody>
				</table>
				</div>
	            <%-- ======================= 组织联动共通导入开始  ============================= --%>
           	  	<s:action name="BINOLCM13_query" namespace="/common" executeResult="true">
           	  		<s:param name="showLgcDepot">1</s:param>
           	  		<s:param name="businessType">1</s:param>
           	  		<s:param name="operationType">1</s:param>
           	 	</s:action>
           	  	<%-- ======================= 组织联动共通导入结束  ============================= --%>
               </div>
               <p class="clearfix">
        			<%-- 查询 --%>
        			<button class="right search" type="button" onclick="search('<s:property value="#search_url"/>')">
        				<span class="ui-icon icon-search-big"></span>
        				<span class="button-text"><s:text name="global.page.search"/></span>
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
           	<div class="toolbar clearfix">
           		<span style="margin-right:10px;">
           			<cherry:show domId="BINOLPTRPS11EXP">
	           			<!-- 一览明细导出 -->
	                    <a id="export" class="export" onclick="exportExcel('${xls_url}');return false;">
	                        <span class="ui-icon icon-export"></span>
	                        <span class="button-text"><s:text name="global.page.exportExcel02"/></span>
	                    </a>
	                    <a id="export" class="export" onclick="ptrps11_exportCsv();return false;">
				          <span class="ui-icon icon-export"></span>
				          <span class="button-text"><s:text name="global.page.exportCsv"/></span>
				       </a>
                	</cherry:show>
	                <!-- 一览概要导出 -->
	                <cherry:show domId="PTRPS11SUMYEXP">
	                    <a id="export" class="export" onclick="exportSummaryExcel('${summaryXls_url}');return false;">
	                        <span class="ui-icon icon-export"></span>
	                        <span class="button-text"><s:text name="global.page.exportExcel01"/></span>
	                    </a>
                	</cherry:show>
                </span>
           		<span id="headInfo"></span>
           		<span class="right">
           			<%-- 设置列 --%>
           			<a class="setting">
           				<span class="ui-icon icon-setting"></span>
           				<span class="button-text"><s:text name="global.page.colSetting"/></span>
           			</a>
           		</span>
           	</div>
            <table id="dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
              	<thead>
	                <tr>
	                	<%-- No. --%>
	                  	<th><s:text name="RPS11_num"/></th>
	                  	<%-- 产品名称 --%>
	                  	<th><s:text name="RPS11_nameTotal"/><span class="ui-icon ui-icon-document"></span></th>
	                  	<%-- 厂商编码 --%>
	                  	<th><s:text name="RPS11_unitCode"/></th>
	                  	<%-- 产品条码 --%>
	                  	<s:if test='!"2".equals(type)'><th><s:text name="RPS11_barCode"/></th></s:if>
	                  	<%-- 品牌	 --%>
	                  	<th><s:text name="RPS11_originalBrand"/></th>
	                  	<%-- 计量单位 --%>
	                  	<th><s:text name="RPS11_moduleCode"/></th>
	                  	<%-- 销售单位 --%>
	                  	<th><s:text name="RPS11_saleUnit"/></th>
	                  	<%-- 产品规格 --%>
	                  	<th><s:text name="RPS11_spec"/></th>
                        <%-- 销售价格 --%>
                        <th><s:text name="RPS11_price"/></th>
	                  	<%-- 期初结存 --%>
	                  	<th><s:text name="RPS11_startQuantity"/></th>
                        <%-- 期初结存金额 --%>
                        <th><s:text name="RPS11_startAmount"/></th>
	                  	<%-- 本期收入  --%>
	                  	<th><s:text name="RPS11_inQuantity"/></th>
	                  	<%-- 本期发出  --%>
	                  	<th><s:text name="RPS11_outQuantity"/></th>
	                  	<%-- 期末结存 --%>
	                  	<th><s:text name="RPS11_endQuantity"/></th>
                        <%-- 期末结存金额 --%>
                        <th><s:text name="RPS11_endAmount"/></th>
	                </tr>
              	</thead>
            </table>
         	</div>
       </div>
       <%-- ====================== 结果一览结束 ====================== --%>
</div>
</s:i18n>
<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/popExportDialog.jsp" flush="true" />
<div class="hide">
<s:url id="exportCsvUrl" action="BINOLPTRPS11_exportCsv" ></s:url>
<a id="exportCsvUrl" href="${exportCsvUrl}"></a>
<s:url id="exporChecktUrl" action="BINOLPTRPS11_exportCheck" ></s:url>
<a id="exporChecktUrl" href="${exporChecktUrl}"></a>
</div>
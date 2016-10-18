<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<link rel="stylesheet" href="/Cherry/css/cherry/combobox.css" type="text/css">
<script type="text/javascript" src="/Cherry/js/common/departBar.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/pt/rps/BINOLPTRPS44.js"></script>
<script type="text/javascript">
	//节日
	var holidays = '${holidays }';
	$('#saleTimeStart').cherryDate({
		holidayObj: holidays,
		beforeShow: function(input){
			var value = $('#saleTimeEnd').val();
			return [value,'maxDate'];
		}
	});
	$('#saleTimeEnd').cherryDate({
		holidayObj: holidays,
		beforeShow: function(input){
			var value = $('#saleTimeStart').val();
			return [value,'minDate'];
		}
	});
</script>
<s:i18n name="i18n.pt.BINOLPTRPS44">
<s:text name="global.page.select" id="select_default"/>
<div class="panel-header">
  <div class="clearfix"> 
	<span class="breadcrumb left">	    
	  <jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
	</span>  
  </div>
</div>
<div class="panel-content">
	<div class="box">
		<cherry:form id="mainForm" class="inline" onsubmit="binolptrps44.searchSaleCount();return false;">
		<input name="brandInfoId" id="brandInfoId" type="hidden" value='<s:property value="#session.userinfo.BIN_BrandInfoID"/>'></input>
			<div class="box-header">
           		<strong>
           			<span class="ui-icon icon-ttl-search"></span><s:text name="global.page.condition"/>
           		</strong>
            </div>
            <div class="box-content clearfix">
            <div style="padding: 15px 0px 15px;">
            	<table class="detail" cellpadding="0" cellspacing="0" style="margin: 0">
	            	<tr>
		            	<th><label><s:text name="binolptrps44_saleTime"/></label></th>
		            	<td class="columns">
		            		<s:textfield id="saleTimeStart" name="saleTimeStart" cssClass="date"/>-<s:textfield id="saleTimeEnd" name="saleTimeEnd" cssClass="date"/>
			                <input type="hidden" id="fiscalMonthFlag" name="fiscalMonthFlag" value=""/>
			            </td>
			            <th><label><s:text name="binolptrps44_countModel"/></label></th>
		            	<td class="columns">
		            		<input type="radio" name="countModel" value="1" checked="checked"/><s:text name="binolptrps44_countModel1"/>
			                <input type="radio" name="countModel" value="2"/><s:text name="binolptrps44_countModel2"/>
			                <input type="radio" name="countModel" value="0"/><s:text name="binolptrps44_countModel0"/>
			            </td>
			            
	            	</tr>
	            	<tr>
		            	<th><label><s:text name="binolptrps44_deliveryModel"/></label></th>
			            <td class="columns">
			            	<s:text name="global.page.all" id="binolptrps44_deliveryModelNull"/>
			            	<s:select name="deliveryModel" list='#application.CodeTable.getCodes("1373")' 
	                 			listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{binolptrps44_deliveryModelNull}"/>
			            </td>
			            <th><label><s:text name="binolptrps44_channel"/></label></th>
			            <td class="columns">
			            	<s:if test="%{channelList != null && !channelList.isEmpty()}">
			               		<s:select list="channelList" listKey="channelId" listValue="channelName" name="channelIdClude" headerKey="" headerValue="%{#select_default}"></s:select>
			               	</s:if>
			               	<s:else>
			               		<select name="channelIdClude" id="channelIdClude"><option value="">${select_default }</option></select>
			               	</s:else>
			            	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<input type="checkbox" id="excludeFlag" name="excludeFlag" value="1"><s:text name="binolptrps44_exclude" />
			            </td>
	            	</tr>
	            	<tr>
	            		<th><label><s:text name="binolptrps44_prtVendorId"/></label></th>
			            <td class="columns">
			            <s:text name="global.page.all" id="binolptrps44_deliveryModelNull"/>
			            	<s:select name="selectModel" list='#application.CodeTable.getCodes("1404")' 
	                 			listKey="CodeKey" listValue="Value" headerKey="0" onchange="binolptrps44.changeSelModel(this.value);return false;"/>
			            	<a class="add" onClick="binolptrps44.openProPopup();return false;">
								<span class="ui-icon icon-add"></span><span class="button-text"><s:text name="global.page.selPro" /></span>
							</a>
			            </td>
			            <th></th>
			            <td></td>
	            	</tr>
	            	<tr id="productionTr" class="hide" >
						<td style="padding:3px;" colspan="4" class="detail box2-active">
							<div class="detail_box2" >
								<div class="section-content">
									<table cellspacing="0" cellpadding="0" style="width:100%;" id="proTable">
										<thead>
											<tr>
												<%-- 厂商编码 --%>
												<th style="width:25%;"><s:text name="global.page.prtvendorcode" /></th>									  
											    <th style="width:25%;"><s:text name="global.page.barcode" /></th>
											    <th style="width:35%;"><s:text name="global.page.productname" /></th>
											    <th class="center" style="width:15%;"><s:text name="global.page.option" /></th>
											</tr>
										</thead>
										<tbody id="production_ID"></tbody>
									</table>
								</div>
							</div>
						</td>
					</tr>
	            	<tr id="productionTr1" class="hide" >
						<td style="padding:3px;" colspan="4" class="detail box2-active">
							<div class="detail_box2" >
								<div class="section-content">
									<table cellspacing="0" cellpadding="0" style="width:100%;" id="proTable1">
										<thead>
											<tr>									  
											  <th style="width:25%;"><s:text name="global.page.barcode" /></th>
											  <th style="width:35%;"><s:text name="global.page.productname" /></th>
											  <th class="center" style="width:15%;"><s:text name="global.page.option" /></th>
											</tr>
										</thead>
										<tbody id="production_ID1"></tbody>
									</table>
								</div>
							</div>
						</td>
					</tr>
		            <s:iterator value="cateList" status="st1">
						<s:if test="teminalFlag == 1">
				            <tr>
					            <td colspan="4">
								<%-- ======================= 产品分类开始  ========================================================== --%>
									<div class="clear detail_box2" style="margin: 0 0 0.5em;">
										<div class="clearfix">
											<strong class="left"> 
												<span class="ui-icon icon-ttl-search"></span>
												<s:text name="binolptrps44_cateInfo" /> <%-- 产品分类 --%>
											</strong>
										</div>
										<hr style="margin: 0 0 0.5em"/> 
									<%-- ================== 错误信息提示 START ======================= --%>
										<div id="cateInfoMessDiv">
											<div id="errorMessageCate"></div>
											<div style="display: none" id="errorMessageTemp1">
												<div class="actionError">
													<ul>
														<li><span><s:text name="JCS04.cateInfoEmptyError" /></span></li>
													</ul>
												</div>
											</div>
										</div>
									<%-- ================== 错误信息提示   END  ======================= --%>
										<!-- 产品信息分类 -->
										<div id="cateInfo" class="section-content">
											<jsp:include page="/WEB-INF/jsp/pt/rps/BINOLPTRPS44_2.jsp" flush="true" />
										</div>
									</div>
								<%-- ======================= 产品分类结束  ========================================================== --%>
								</td>
				            </tr>
			            </s:if>
		            </s:iterator>
				</table>
				</div>
	            <%-- ======================= 组织联动共通导入开始  ============================= --%>
           	  	<s:action name="BINOLCM13_query" namespace="/common" executeResult="true">
	           	  	<s:param name="showType">0</s:param>
	           	  	<s:param name="businessType">3</s:param>
	        		<s:param name="operationType">1</s:param>
	        		<s:param name="mode">dpat,area,chan</s:param>
	        		<!-- 显示选择包含全部柜台的复选框 -->
	        		<s:param name="orgValidType">1</s:param>
           	  	</s:action>
           	  	<%-- ======================= 组织联动共通导入结束  ============================= --%>
            </div> 
            <p class="clearfix">
         		<button class="right search" type="button" onfocus="binolptrps44.getFiscalFlag();return false;" onclick="binolptrps44.searchSaleCount();return false;">
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
			<s:text name="global.page.list" />
		 	</strong>
		</div>
		<div class="section-content">
		<div class="toolbar clearfix">
			<span style="margin-right:10px;">
		      <cherry:show domId="BINOLPTRPS44_01">
		     	<a id="export" class="export" onclick="binolptrps44.exportExcel();return false;">
		          <span class="ui-icon icon-export"></span>
		          <span class="button-text"><s:text name="global.page.export"/></span>
		        </a>
			  </cherry:show> 	
		    </span>
			<span id="saleCountInfoDiv"></span>
			<span class="right highlight" id="fiscalMonthInfoDiv"></span>
		</div>
		<div id="countResultDiv"></div>
		</div>
	</div>
	<%-- ====================== 结果一览结束 ====================== --%>
	<div class="hide">
		<div id="no"><s:text name="No." /></div>
		<div id="binolptrps44_departName"><s:text name="binolptrps44_departName" /></div>
		<div id="binolptrps44_busniessPrincipal"><s:text name="binolptrps44_busniessPrincipal" /></div>
		<div id="binolptrps44_unitCode"><s:text name="binolptrps44_unitCode" /></div>
		<div id="binolptrps44_barCode"><s:text name="binolptrps44_barCode" /></div>
		<div id="binolptrps44_nameTotal"><s:text name="binolptrps44_nameTotal" /></div>
		<div id="binolptrps44_saleType"><s:text name="binolptrps44_saleType" /></div>
		<div id="binolptrps44_quantity"><s:text name="binolptrps44_quantity" /></div>
		<div id="binolptrps44_amount"><s:text name="binolptrps44_amount" /></div>
		<div id="binolptrps44_monthQuantity"><s:text name="binolptrps44_monthQuantity" /></div>
		<div id="binolptrps44_monthAmount"><s:text name="binolptrps44_monthAmount" /></div>
		<div id="binolptrps44_yearQuantity"><s:text name="binolptrps44_yearQuantity" /></div>
		<div id="binolptrps44_yearAmount"><s:text name="binolptrps44_yearAmount" /></div>
		<div id="deleteButton"><s:text name="global.page.delete" /></div><!-- 商品列表删除按钮 -->
	</div>
</div>
</s:i18n>
<div class="hide">
<s:url action="BINOLPTRPS44_search" id="saleCountUrl"></s:url>
<a href="${saleCountUrl }" id="saleCountUrl"></a>
<s:url id="exportUrl" action="BINOLPTRPS44_export" ></s:url>
<a id="exportUrl" href="${exportUrl}"></a>
<s:url action="BINOLPTRPS44_getFiscalFlag" id="fiscalFlagUrl"></s:url>
<a href="${fiscalFlagUrl }" id="fiscalFlagUrl"></a>
</div>
<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<script type="text/javascript" src="/Cherry/js/common/ajaxfileupload.js"></script>
<script type="text/javascript" src="/Cherry/js/pt/jcs/BINOLPTJCS16.js"></script>
<s:url id="downLoad_url" value="/download/产品方案明细导入模板(门店用).xls"/>
<s:url id="downLoad_urlOT" value="/download/OT/产品方案明细导入模板.xls"/>
<%-- 入库单导入导入URL --%>
<s:url id="import_url" value="/pt/BINOLSTIOS16_importCnt"/>
<s:url id="getDownloadPathByAjaxURL" value="/st/BINOLSTIOS09_getDownloadPathByAjax"/>
<div class="hide">
    <a id="getDownloadPathByAjaxURL" href="${getDownloadPathByAjaxURL}"></a>
</div>
<s:i18n name="i18n.st.BINOLSTIOS09">
<s:text name="global.page.select" id="select_default"/>
	    <div class="panel-header">
	        <div class="clearfix"> 
			<span class="breadcrumb left">	    
				<jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
			</span>
	        </div>
	    </div>
<div class="panel-content">
	<div id="div_main">
	    <%-- ================== 信息提示区 START ======================= --%>
		<div id="actionDisplay">
			<div id="errorDiv" class="actionError" style="display:none;">
				<ul>
				    <li>
				    	<span id="errorSpan"></span>
				    </li>
				</ul>			
		  	</div>
		  	<div id="successDiv" class="actionSuccess" style="display:none;">
				<ul class="actionMessage">
			  		<li>
			  			<span id="successSpan">	</span>
			  		</li>
			 	</ul>
			</div>
		</div>
		<div id="errorMessage"></div>
		<%-- ================== 信息提示区   END  ======================= --%>
	    <cherry:form id="mainForm" class="inline">
	    <div class="panel-content">
	    	<div class="box-yew">
				<div class="box-yew-header clearfix">
					<strong class="left">
						<span class="ui-icon icon-help-star-yellow"></span>
						<s:text name="binolstios09_importExplanation" />
					</strong> 
					<a id="expandCondition" style="margin-left: 25px; font-size: 12px; width: 80px" class="ui-select right"> 
						<span class="button-text choice" style="min-width: 50px;">
							<s:text name="binolstios09_moreExplanation" />
						</span> 
						<span class="ui-icon ui-icon-triangle-1-n"></span>
					</a>
				</div>
				<div class="box-yew-content">
					<div class="step-content">
						<label style="margin: 1px 0 0 0px;">1</label>
						<div class="step">
						<%--<s:text name="binolstios09_explanation1" /> --%>
						<span>产品列表是必选项，选择系统中已存在的产品列表，方可添加excel中的产品明细。</span>
						</div>
						<div class="line"></div>
					</div>
					<div id="moreCondition" style="display: none;">
						<div class="step-content">
							<label style="margin: 1px 0 0 0px;">2</label>
							<div class="step">
							<%-- 
								<span>如果产品方案中已经存在产品，则导入excel时会覆盖产品方案中原来的产品。
								若产品方案已分配过，则老产品方案分配的柜台将被无效掉，并将新产品方案分配到柜台。
								</span>
								--%>
								<span>产品列表中已经存在的产品，导入excel时执行更新操作。不存在则新增。
								</span>
								
							</div>
							<div class="line"></div>
						</div>
						<div class="step-content">
							<label style="margin: 1px 0 0 0px;">3</label>
							<div class="step">
								<span>产品条码(barcode)作为识别产品的标记，如果产品条码在系统中不存在，则无法将该条数据导入到产品方案中。</span>
							</div>
							<div class="line"></div>
						</div>
						<div class="step-content">
							<label style="margin: 1px 0 0 0px;">4</label>
							<div class="step">
								<span>产品名称为选填项，只是作为辅助识别产品使用。</span>
							</div>
							<div class="line"></div>
						</div>
					</div>
				</div>
			</div>	    	    
	    	<div class="box2 box2-active">
				<div class="box2-header clearfix">
					<strong class="active left">
						<span class="ui-icon icon-ttl-section-info"></span>
						<s:text name="binolstios09_inDepotConditions" />
					</strong>
				</div>
				<div class="box2-content clearfix">
					 <table class="detail" cellpadding="0" cellspacing="0">
						<tr class="hide">
							<th><s:text name="binolstios09_brandName" /></th>
							<td colspan="3">
								<s:if test="brandInfoList.size() > 1">
									<s:select id="brandInfoId" name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" Cssstyle="width:120px;" onchange="ptjcs48.changeBrand();"></s:select>
								</s:if> 
								<s:else>
									<s:iterator value="brandInfoList"><s:property value="brandName" />
										<input  type="hidden" id="brandInfoId" name="brandInfoId" value='<s:property value="brandInfoId"/>'/>
									</s:iterator>
								</s:else>
							</td>
						</tr>
						 <tr>
						 	<th><s:text name="产品列表" /><span class="highlight">*</span></th>
							<td colspan = "3">
						     	<span>
						     		<s:select name="productPriceSolutionID" list="prtPriceSolutionList" listKey="solutionID" listValue="solutionNameDesc" value="solutionID"
						     		headerKey="" headerValue="%{#select_default}" onchange="ptjcs48.changeSolu(this);"/>
						    	</span>
						        <s:hidden id="prtPriceSolutionListId" value="%{configMap.prtPriceSolutionListJson}"/>
						        <s:hidden id="businessDateId" value="%{configMap.businessDate}"/>
						        <s:hidden id="solutionName" name="solutionName"  />
						        <s:hidden id="soluStartDate" name="soluStartDate"  />
						        <s:hidden id="soluEndDate" name="soluEndDate"  />
						        <s:hidden id="isSynchProductPrice" name="isSynchProductPrice"  />
						        <span id="soluMsg" class="hide">
									<span class="highlight">※</span>
									<span class="red">当前产品列表已有明细，继续导入则会将原有明细覆盖。</span>
						        </span>
							</td >
							
						</tr>
					</table>    
				</div>
			</div>
			<div class="box2 box2-active">
				<div class="box2-header clearfix">
					<strong class="active left">
						<img id="excel" src="/Cherry/css/common/blueprint/images/excel.png">
						<s:text name="binolstios09_title" />
					</strong>
				</div> 
		        <div class="box2-content clearfix">
	        		<div class="box4-content">
						<span style="margin-left:10px; display: inline;" class="left hide"> 
							<span class="highlight"><s:text name="binolstios09_snow" /></span>
							<s:text name="binolstios09_notice"/>
							<a id="downloadURL" href="${downLoad_url }"><s:text name="binolstios09_down"/></a>
							<%-- <s:if test='configMap.soluAddMode == "1"'>
                            <a id="downloadURL" href="${downLoad_url }"><s:text name="binolstios09_down"/></a>
							</s:if>
							<s:elseif test='configMap.soluAddMode == "2"'>
                            <a id="downloadURL" href="${downLoad_urlOT}"><s:text name="binolstios09_down"/></a>
							</s:elseif>
							<s:elseif test='configMap.soluAddMode == "3"'>
                            <a id="downloadURL" href="${downLoad_urlOT}"><s:text name="binolstios09_down"/></a>
							</s:elseif> --%>
						    <input class="input_text" type="text" id="pathExcel" name="pathExcel"/>
						    <input type="button" value="<s:text name="global.page.browse"/>"/>
						    <input class="input_file" type="file" name="upExcel" id="upExcel" size="33" onchange="ptjcs48.deleteActionMsg();pathExcel.value=this.value;return false;" /> 
						    <input type="button" value="<s:text name="binolstios09_importExcel_btn"/>" onclick="ptjcs48.ajaxFileUpload('${import_url}');return false;" id="upload"/>
	        				<img id="loading" src="/Cherry/css/cherry/img/loading.gif" height="15px" style="display:none;">
						</span>
					</div>
		        </div>
		    </div>
	    </div>
	    </cherry:form>
	   	<%-- 错误信息 --%>
	    <div id="errMsg">
	    	<%-- 请选择上传文件。 --%>
	    	<input type="hidden" id="errmsg1" value='<s:text name="EMO00010"/>'/>
	    	<input type="hidden" id="errmsg2" value='<s:text name="binolstios09_error1"/>'/>
	    	<input type="hidden" id="errmsg3" value='<s:text name="binolstios09_error2"/>'/>
	    	<input type="hidden" id="errmsg4" value='<s:text name="binolstios09_error3"/>'/>
	    </div>
	</div>
</div>
</s:i18n>

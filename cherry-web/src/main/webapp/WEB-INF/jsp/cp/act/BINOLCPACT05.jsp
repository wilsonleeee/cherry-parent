<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/common/popDataTable.js"></script>
<script type="text/javascript" src="/Cherry/js/common/ajaxfileupload.js"></script>
<script type="text/javascript" src="/Cherry/js/cp/act/BINOLCPACT05.js"></script>
<%-- 提交URL --%>
<s:url id="confirm_url" value="/cp/BINOLCPACT05_confirm"/>
<s:url id="importExcel_url" value="/cp/BINOLCPACT05_importExcel"/>
<s:i18n name="i18n.cp.BINOLCPACT05">
	<div class="crm_content_header">
		<span class="icon_crmnav"></span>
		<span><s:text name="ACT05_actExecution" /> &gt; <s:text name="ACT05_actReservation" /> </span>
	</div>
	
	<div class="panel-content">
		<%-- ================== 信息提示区 START ======================= --%>
		<div id="actionDisplay">
			<div id="errorDiv" class="actionError" style="display:none;">
				<ul>
				    <li><span id="errorSpan"></span></li>
				</ul>			
		  	</div>
		  	<div id="successDiv" class="actionSuccess" style="display:none;">
				<ul class="actionMessage">
			  		<li><span id="successSpan"></span></li>
			 	</ul>
			</div>
		</div>
		<div id="actionResultDisplay"></div>
		<%-- ================== 信息提示区 END ======================= --%>
		<div id="div_main" class="section">
		    <cherry:form id="mainForm" method="post" class="inline" csrftoken="false">
				<%-- ==========================================批量预约=========================================== --%>
				<div>
			      	<div class="box4-content clearfix">
						<div class="box2 box2-active">
				       		<div class="box2-header clearfix"><strong class="active left"><s:text name="ACT05_step1"/></strong></div>
					        <div style="overflow-x:auto;" class="box2-content">
								<table class="detail">
						            <tbody>
						              <tr>
						                 <th style="width:15%;"><s:text name="ACT05_resertAway" /></th><%--预约方式 --%>
						                 <td style="width:85%;">
						                    <span>
									        	<input type="radio" checked="checked" onclick="BINOLCPACT05.changeMode(this);" value="1" name="orderMode" id="orderMode_1">
									        	<label for="orderMode_1"><s:text name="ACT05_selectMember" /></label>
									        </span>
											<span>
									        	<input type="radio" onclick="BINOLCPACT05.changeMode(this);" value="2" name="orderMode" id="orderMode_2">
									        	<label for="orderMode_2"><s:text name="ACT05_excelImport" /></label>
									        </span>
										</td>
						             </tr>
						           </tbody>
						        </table>
						  	</div>
				        </div>
						<div id="excelImport" class="hide">
							<div class="box2 box2-active">
					       		<div class="box2-header clearfix"><strong class="active left"><s:text name="ACT05_step5"/></strong></div>
						        <div style="overflow-x:auto;" class="box2-content">
									<!-- 选择EXCEL文件-->
									<span id="linkExcel">
										<a href="/Cherry/download/活动预约数据模板.xls"><s:text name="ACT05_download" /></a>
										<input class="input_text" type="text" id="pathExcel" name="pathExcel"/>
									    <input type="button" value="<s:text name="global.page.browse"/>"/>
									    <input class="input_file" type="file" name="upExcel" id="upExcel" size="33" onchange="pathExcel.value=this.value;BINOLCPACT05.deleteActionMsg();return false;" /> 
									    <input type="button" value="<s:text name="ACT05_batchImport"/>" onclick="BINOLCPACT05.ajaxFileUpload('${importExcel_url}');" id="upload" /> 
									    <img id="loadingImg" src="/Cherry/css/cherry/img/loading.gif" height="15px" style="display:none;">
							   		</span>
							   	</div>
							</div> 
				       	</div>
				       	<div id="selectImport">
				       		<div class="box2 box2-active">
					       		<div class="box2-header clearfix"><strong class="active left"><s:text name="ACT05_step2"/></strong></div>
						        <div style="overflow-x:auto;" class="box2-content">
					        		<table width="100%">
								      <thead>
								        <tr>
								          <th><s:text name="ACT05_select"/></th>
								          <th><s:text name="ACT05_subCampCode"/></th>
								          <th><s:text name="ACT05_sunCampName"/></th>
								          <th><s:text name="ACT05_restTimes"/></th>
								        </tr>
								      </thead>
								      <tbody id="sumCamp_table">
								    	<s:iterator value="subCampaignList" status="s">
										    <tr>
										    	<td>
											    	<input type="radio" name="subCampCode" onchange="BINOLCPACT05.changeSubCamp();" 
											    		<s:if test="#s.index== 0">checked</s:if> value="<s:property value='subCampCode'/>"/>
											    	<input type="hidden" name="conInfo" value="<s:property value='conInfo'/>"/>
													<input type="hidden" name="locationType" value="<s:property value='locationType'/>"/>
										    	</td>
												<td class="showDetail"><s:property value="subCampCode"/></td>
												<td class="showDetail"><s:property value="subCampName"/></td>
												<td><input class="number" name="times" value="1" onchange="BINOLCPACT05.changeTimes();"/></td>
											</tr>
											<tr class="detail hide">
												<td colspan="5" class="detail box2-active">
													<div class="detail_box" style="margin:5px;">
														<div class="section">
															<div class="section-header">
														    	<strong class="left"><span class="ui-icon icon-ttl-section-info"></span><s:text name="ACT05_gift"/></strong>
														    	<a class="right" href="#" onclick="BINOLCPACT05.closeDetail(this);return false;"><s:text name="global.page.close"/></a>
														    </div>
														    <div class="section-content">
														      <table width="100%">
														      	<thead>
														      		<tr>
															          <th style="width:20%;"><s:text name="global.page.unitCode"/></th>
															          <th style="width:20%;"><s:text name="global.page.barCode"/></th>
															          <th style="width:30%;"><s:text name="global.page.nameTotal"/></th>
															          <th style="width:10%;"><s:text name="global.page.price"/></th>
															          <th style="width:10%;"><s:text name="global.page.exPoint"/></th>
															          <th style="width:10%;"><s:text name="global.page.quantity"/></th>
															        </tr>
														      	</thead>
														      	<tbody>
														      		<s:iterator value="list">
														      		<s:set name="flag" value="0" />
															  		<s:if test='"TZZK".equals(prmCate) || "DHCP".equals(prmCate) || "DHMY".equals(prmCate)'>
															  			<s:set name="flag" value="1" />
															  		</s:if>
																  	<tr <s:if test='#flag == "1"'>class="gray"</s:if>>
														      			<td><s:property value='unitCode'/></td>
														      			<td><s:property value='barCode'/></td>
														      			<td><s:property value='nameTotal'/></td>
														      			<td><s:property value='price'/></td>
														      			<td><s:property value='exPoint'/></td>
														      			<td><s:property value='quantity'/></td>
														       		</tr>
														      		</s:iterator>
														      		
														 	  	</tbody>
														 	  </table>
															</div>
														</div>	
													</div>
												</td>
											</tr>
										</s:iterator>	
								      </tbody>
								  </table>
							 	</div>
							 </div> 
							 <div class="box2 box2-active">
							 	<div class="box2-header clearfix"><strong class="active left"><s:text name="ACT05_step3"/></strong></div>
						        <div class="box2-content">	 
								 	<table class="detail">
							            <thead>
							              	<tr>
							                 	<th style="width:15%;"><s:text name="ACT05_resertObject" /></th><%--选择会员 --%>
								                 <td style="width:85%;">
													<!-- 选择搜索条件-->
													<span id="linkMebSearch" class="left">
														<a id="importLink" onclick="BINOLCPACT05.searchConDialog(this);return false;" class="search left" style="margin: 3px 0 0 20px;">
												      		<span class="ui-icon icon-search"></span><span class="button-text"><s:text name="ACT05_select" /></span>
												    	</a>
													</span>
									        		<input type="hidden" id="campMebJson" name="campMebJson" value=""/>
												</td>
							             	</tr>
							           	</thead>
						         	</table>
						         	<div class="hide">
							         	<table width="100%" id="mem_table">
								         	<thead>
									            <tr>
										          <th><s:text name="ACT05_actCamMebType" /></th><%--对象类型--%>
										          <th><s:text name="ACT05_actMebCode" /></th><%--卡号/编号--%>
										          <th><s:text name="ACT05_actMebName" /></th><%--姓名--%>
										          <th><s:text name="ACT05_actMebPhhone" /></th><%--手机号--%>
										          <th><s:text name="ACT05_actMebBirthDay" /></th><%--生日--%>
										          <th><s:text name="global.page.joinDate" /></th><%--入会日期--%>
							          			  <th><s:text name="global.page.changablePoint" /></th><%--可兑换积分--%>
										          <th class="center"><s:text name="ACT05_actMebMessage" /></th><%--接收短信--%>
										        </tr>
									        </thead>
									        <tbody></tbody>
							         	</table>
						         	</div>
					       		</div>
				       		</div>
				       		<div class="box2 box2-active">
					       		<div class="box2-header clearfix"><strong class="active left"><s:text name="ACT05_step4"/></strong></div>
						        <div style="overflow-x:auto;" class="box2-content">
									<table class="detail">
							            <tbody>
							            <tr>
							                 <th style="width:15%;"><s:text name="预约代行柜台" /></th><%--预约代行柜台--%>
							                 <td style="width:85%;">
							                 	<span id="orderCntCode_text"></span>
												<a onclick="BINOLCPACT05.openCntBox(this);return false;" class="search left" style="margin: 3px 0 0 20px;">
										      		<span class="ui-icon icon-search"></span><span class="button-text"><s:text name="ACT05_select" /></span>
										    	</a>
												<input type="hidden" id="orderCntCode" name="orderCntCode"  value=""/>
												<input type="hidden" id="organizationId" name="organizationId"  value=""/>
											</td>
							             </tr>	
							             <tr>
							                 <th style="width:15%;"><s:text name="礼品领取柜台" /></th><%--礼品领取柜台--%>
							                 <td style="width:85%;">
								                 <span style="width:15%;white-space: nowrap;">
						                   			<span class="ui-icon icon-arrow-crm"></span>
						  							<s:property value="#application.CodeTable.getVal('1254',campaignInfo.gotCounter)"/>
						                   		</span>
						                   		<input type="hidden" id="gotCounter" name="gotCounter" value="<s:property value='campaignInfo.gotCounter'/>"/>
											</td>
							             </tr>
							           </tbody>
							        </table>
							  	</div>
					        </div>
			       		</div>
		       		</div>
				</div>
			</cherry:form>
			<div class="center clearfix">
			 	<button onclick="BINOLCPACT05.confirmInit('${confirm_url}');return false;" type="button" class="confirm"  id="confirmbtn">
			 		<span class="ui-icon icon-confirm"></span><span class="button-text"><s:text name="ACT05_resert"/></span>
			 	</button>
			 </div>
			<%-- 错误信息 --%>
			<div class="hide">
			   <span id="errMsg_1"><s:text name="ACT05_uploadFiles" /></span>
			   <span id="errMsg_2"><s:text name="ACT05_errMsg_2" /></span>
			   <span id="errMsg_3"><s:text name="ACT05_errMsg_3" /></span>
			</div>
		   <div class="hide" id="objDialogTitle"><s:text name="global.page.searchMemTitle" /></div>
		   <div class="hide" id="dialogConfirm"><s:text name="global.page.ok" /></div>
		   <div class="hide" id="dialogCancel"><s:text name="global.page.cancle" /></div>
		   <!-- 会员信息搜索弹出页面 -->
		   <div class="hide" id="searchCondialogInit"></div>
		</div>
<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/popCounterTable.jsp" flush="true" />
</s:i18n>
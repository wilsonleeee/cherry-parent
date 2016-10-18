<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<script type="text/javascript" src="/Cherry/plugins/zTree v3.1/js/jquery.ztree.all-3.1.js"></script>
<link rel="stylesheet" href="/Cherry/plugins/zTree v3.1/css/zTreeStyle/zTreeStyle.css" type="text/css" media="screen, projection">
<script type="text/javascript" src="/Cherry/js/pt/jcs/BINOLPTJCS17.js"></script>
<script type="text/javascript" src="/Cherry/js/common/ajaxfileupload.js"></script>
<s:i18n name="i18n.pt.BINOLPTJCS17">
<s:text name="global.page.select" id="select_default"/>
	    <div class="panel-header">
	        <div class="clearfix"> 
			<span class="breadcrumb left">	    
				<jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
			</span>
       		<span class="right"> 
       		<%-- 柜台产品下发按钮 --%>
       		<cherry:show domId="BINOLPTJCS1702">
       		 	<a href="#" class="add" onclick="ptjcs17.issuedInit();return false;">
       		 		<span class="button-text"><s:text name="issCntPrt"/></span>
       		 		<span class="ui-icon icon-down"></span>
       		 	</a>
       		 </cherry:show>
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
		<div id="msgDIV"></div>
		<%-- ================== 信息提示区   END  ======================= --%>
	    <div class="panel-content">
		 	 <p class="clearfix">
		      	  	<span class="left">
		      	  	<cherry:form id="mainForm" class="inline">
					<p>
		                <%-- 所属品牌 --%>
		                <span>
			                <label><s:text name="brandName"></s:text></label>
			                <s:select id="brandInfoId" name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" cssStyle="width:100px;" onchange="changeMachineType('%{#s_getTreeRootNodesByAjax}');"></s:select>      	
		            	</span>
		            	<span>
			            	<label>
			            		<s:if test="configMap.isPosCloud==0">
				            		<s:text name="prtSoluName" />
			            		</s:if>
			            		<s:elseif test='configMap.isPosCloud==1'>
			            			<s:text name="prtlistname" />
			            		</s:elseif>
			            		<span class="highlight">*</span>
		            		</label>
			         		<s:text name="global.page.all" id="select_default"/>
				     		<s:select name="productPriceSolutionID" list="prtPriceSolutionList" listKey="solutionID" listValue="solutionNameDesc" value="solutionID"
						     		 onchange="changeSolu(this);"  cssStyle="width:150px;"/>
				     		 <s:hidden id="placeTypeOld" name="placeTypeOld"> </s:hidden>
		            	</span>
		            </p>
		            </cherry:form>
		           </span>
		     </p>
		     <hr class="space" />
		     <div style="margin-bottom:10px;" class="detail_box2">
			 <div class="dialog2 clearfix"  id="location_dialog_0"> 
				<div id="actionDisplay_0">
					<div style="display:none;" class="actionError" id="errorDiv_0">
						<ul>
							<li>
							<span id="impErrorSpan"></span>
							</li>
						</ul>
					</div>
					<div style="display:none;" class="actionSuccess" id="successDiv_0">
						<ul>
							<li>
							<span id="successSpan"></span>
							</li>
						</ul>
					</div>
				</div>
				<div id="actionResultDisplay_0"></div> 
				<div>
					<p style="margin: 5px;">
					<label>分配地点类型</label>
					<s:hidden id="cntOwnOpt" name="cntOwnOpt"> </s:hidden><!-- 是否门店自设,有值即自设 -->
					<select id="sel_location_type_0" style="width:150px;" onchange="changeLocationType(this);return false;" >
						<s:if test="configMap.isPosCloud==1">
							<option value="2">按区域并且指定柜台</option>
						</s:if>
						<s:else>
							<option value="0">请选择分配地点类型......</option>
							<s:if test="configMap.placeConfig == 1">
								<option value="1">按区域</option>
								<option value="2">按区域并且指定柜台</option>
							</s:if>
							<s:elseif test="configMap.placeConfig == 2">
								<option value="3">按渠道</option>
								<option value="4">按渠道并且指定柜台</option> 
							</s:elseif>
							<s:elseif test="configMap.placeConfig == 3">
								<option value="1">按区域</option>
								<option value="2">按区域并且指定柜台</option>
								<option value="3">按渠道</option>
								<option value="4">按渠道并且指定柜台</option> 
							</s:elseif>
							<%--<option value="5">全部柜台</option> --%>
							<option value="6">导入指定柜台</option>
						</s:else>
						
					</select>
		   			<%-- 
			   			<s:select id="sel_location_type_0" name="location_type" list='#application.CodeTable.getCodes("1156")'
			   			listKey="CodeKey" listValue="Value" cssStyle="width:150px;"  headerKey="" headerValue="请选择活动地点类型......"/>
		   			--%>
					<a style="display:none;"></a> 
					<span style="display:none; margin-left:10px;">
					  <span class="highlight">※</span>请使用标准模板 <a style="color:#3366FF;" href="/Cherry/download/柜台信息模板.xls">模板下载</a> 
					  <input class="input_text" type="text" id="pathExcel_0" name="pathExcel" style="height:20px;"/>
					  <input type="button" value="浏览"/> 
					  <input class="input_file" style="height:auto;margin-left:-314px;" type="file" name="upExcel" id="cntFile_0" size="40" onchange="pathExcel_0.value=this.value;return false;" />
					   <input style="margin-left:10px;" type="button" id="upload" onclick="ptjcs17.ajaxCntFileUpload();return false;" value="上传">
					  <img height="15px" style="display:none;" src="/Cherry/css/cherry/img/loading.gif" id="loading">
					</span>
					</p>
				</div> 
				<div class="ztree">
					<div style="width: 45%;" class="left">
						<div class="jquery_tree">
							<div class="box2-header clearfix"> 
								<strong class="left active"><span class="ui-icon icon-flag"></span><s:text name="newIss" /></strong> 
								<s:if test="configMap.isPosCloud==0">
								<span class="right">
									<input name ="locationPosition" type="text" style="width: 140px;" class="text locationPosition" id="position_left_0">
									<a onclick="locationPosition('leftTree');return false;" class="search">
										<span class="ui-icon icon-position"></span><span class="button-text"><s:text name="positioning" /></span>
									</a>
								</span>
								</s:if>
							</div>
							<div id="treeLeft_0" class="jquery_tree treebox tree" style="overflow:auto;"></div>
						</div>
					</div> 
					<div style="width: 9%; text-align: center; padding-top: 200px;" class="left"> 
						<a class="mover" onclick ="left2Right();"><span class="ui-icon icon-mover"></span></a>
						<br><br><br>
						<a class="movel" onclick="right2Left();"><span class="ui-icon icon-movel"></span></a> 
					</div> 
					<div style="width: 45%;" class="right">
						<div class="jquery_tree">
							<div class="box2-header clearfix"> 
								<strong class="left active"><span class="ui-icon icon-flag2"></span><s:text name="oldIss" /></strong>
								<s:if test="configMap.isPosCloud==0">
								<span class="right">
									<input name ="locationPosition" type="text" style="width: 140px;" class="text locationPosition" id="position_right_0">
									<a onclick="locationPosition('rightTree');return false;" class="search">
										<span class="ui-icon icon-position"></span><span class="button-text"><s:text name="positioning" /> </span>
									</a>
								</span>
								</s:if>
							</div>
							<div id="treeRight_0" class="jquery_tree treebox tree" style="overflow:auto;"></div>
						</div>
					</div> 
				</div>
			 </div>
			 </div>
	    </div>
	    <div class="center " id="pageButton">	
	        <cherry:show domId="BINOLPTJCS1701">
		       <button id="save" class="save" type="button" onclick="addSave()">
		        	<span class="ui-icon icon-save"></span>
		        	<span class="button-text"><s:text name="iss"/></span>
		       </button>
	       </cherry:show>
	    </div>
	</div>
</div>
<s:hidden id="isPosCloudId" value="%{configMap.isPosCloud}"/>
<%-- 产品实时下发弹窗 --%>
<div class="hide" id="dialogIssuedInitDIV"></div>
<%-- 选择地点校验弹窗 --%>
<div class="hide" id="dialogLeftToRightDIV"></div>
<%-- 请选择要移动的柜台提示框 --%>
<div class="hide" id="dialogPlaceToPlaceTipDIV"></div>
<div style="display: none;">
	<div id="dialogConfirmIss"><s:text name="global.page.goOn" /></div>
	<div id="dialogCancelIss"><s:text name="global.page.cancle" /></div>
    <p id="operateSuccessId" class="success"><span><s:text name="global.page.operateSuccess"/></span>
    <p id="operateFaildId" class="message"><span><s:text name="global.page.operateFaild"/></span>
    
	<div id="issCntPrtMsg1"><s:text name="issCntPrtMsg1" /></div>
	<div id="issLaunchingMsg"><s:text name="issLaunchingMsg" /></div>
	<div id="issCntPrt"><s:text name="issCntPrt" /></div>
    
    
    <!-- 柜台产品下发 -->
	<s:url id="issuedPrtUrl" action="BINOLPTJCS17_issuePrt"/>
	<span class="hide" id="issuedPrtId">${issuedPrtUrl}</span>
</div>
<div style="display: none;">
    <div id="actionSuccessId" style="" class="actionSuccess">
    	<ul class="actionMessage">
			<li><span id="msgId"><s:text name="global.page.operateSuccess"/></span></li>
		</ul>
	 </div>
	 <div id="actionFaildId" class="actionError">
	 	<ul class="errorMessage">
	 	<li><span><s:text name="global.page.operateFaild"/></span></li>	</ul>
	 </div>
	 
      <div id="tipTitle"><s:text name="tipMessage"/></div>
      <div id="pleaseSelect"><s:text name="pleaseSelect"/></div>
      <div id="dialogConfirm"><s:text name="global.page.ok" /></div>
      <div id="dialogConfirmgoOn"><s:text name="global.page.goOn" /></div>
      <div id="dialogCancel"><s:text name="global.page.cancle" /></div>
      <div id="dialogClose"><s:text name="global.page.close" /></div> 
	 
</div>
<%--=============================== URL定义开始 ===============================--%>
<s:url id="s_locationSearchUrl" value="/pt/BINOLPTJCS17_getAllotLocationByAjax" />
<span id ="locationSearchUrl" style="display:none">${s_locationSearchUrl}</span>
<s:url id="s_getDPConfigDetailBySoluUrl" value="/pt/BINOLPTJCS17_getDPConfigDetailBySolu" />
<span id ="getDPConfigDetailBySoluUrl" style="display:none">${s_getDPConfigDetailBySoluUrl}</span>
<s:url id="s_addDepartSoluUrl" value="/pt/BINOLPTJCS17_add" />
<span id ="addDepartSoluUrl" style="display:none">${s_addDepartSoluUrl}</span>
<s:url id="s_chkExistCntUrl" value="/pt/BINOLPTJCS17_chkExistCnt" />
<span id ="chkExistCntUrl" style="display:none">${s_chkExistCntUrl}</span>
<%-- 取得柜台父节点信息 --%>
<s:url id="s_getCounterParentUrl" value="/ss/BINOLSSPRM37_getCounterParent" />
<span id ="getCounterParentUrl" style="display:none">${s_getCounterParentUrl}</span>
<%--柜台导入URL --%>
<span id="importCounterUrl" style="display:none">/Cherry/cp/BINOLCPPOI01_importCounter</span>
<%--促销地点查询URL --%>
<s:url id="s_indSearchPrmLocationUrl" value="/ss/BINOLSSPRM13_indSearchPrmLocation" />
<span id ="indSearchPrmLocationUrl" style="display:none">${s_indSearchPrmLocationUrl}</span>
</s:i18n>

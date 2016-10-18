<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<link rel="stylesheet" href="/Cherry/plugins/zTree/zTreeStyle/zTreeStyle.css" type="text/css" media="screen, projection">
<script type="text/javascript" src="/Cherry/plugins/zTree/jquery.ztree-2.6.min.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/common/popDataTable.js"></script>
<script type="text/javascript" src="/Cherry/js/mb/svc/BINOLMBSVC01_1.js?v=20160630"></script>
<style>
.box4{margin : 0.5em 0;}
td.RANGE_TD{white-space: normal;}
.section-header1 {
    border-bottom: 1px solid #E5E5E5;
    height: 24px;
    line-height: 24px;
    padding: 0 10px 5px 5px;
}
</style>
<s:i18n name="i18n.mb.BINOLMBSVC01">
<div class="main container clearfix" id="div_main">
<div class="panel ui-corner-all">
	<div class="hide">
		<s:url id="getTree_url" action="BINOLMBSVC01_getTree"/>
	    <a id="getTreeUrl" href="${getTree_url}"></a>
	    <s:url id="getChannelCntTree_url" action="BINOLMBSVC01_getChannelCntTree"/>
	    <a id="getChannelCntTreeUrl" href="${getChannelCntTree_url}"></a>
		
		<s:url id="s_addRule" value="/mb/BINOLMBSVC01_addRule"/>
		<a id="addRuleUrl" href="${s_addRule}"></a>
	</div>
	<!-- 标题start -->
	<div class="panel-header">
	  <div class="clearfix">
	    <span class="breadcrumb left"> 
			<span class="ui-icon icon-breadcrumb"></span>
			<s:text name="SVC01_ruleManager"/> &gt; <s:text name="SVC01_ruleAdd"/>
	    </span>
	  </div>
	</div>
	<!-- 标题end -->  
	<div class="panel-content" style="padding-top:5px;">
	<!-- 表格开始 -->
	<form id="ruleForm" class="inline">
		<div class="clearfix">
			<div class="box4 left" style="width:49%; margin-right:5px;" id="baseInfo">
				<div class="box4-header">
			 		<strong>
			 			<!-- 基本信息 -->
						<s:text name="global.page.title"/>
			 		</strong>
				</div>
				<div class="box4-content">
				   	<table class="detail" style="margin-bottom:0px">
				   	  <tr>
				   	    <th><s:text name="SVC01_subDiscountName"></s:text></th>
				   		<td><span><s:textfield id="ruleName" name="rechargeRule.ruleName" cssClass="text" maxlength="20" cssStyle="width:195px"></s:textfield></span></td>
				   		
				   	  </tr>
				   	  <tr>
				   	  	<th><s:text name="SVC01_discountBeginDate"></s:text></th>
				   		<td>
							<span><input class="date" id="discountBeginDate" name="rechargeRule.discountBeginDate" style="width: 80px;"/></span>
						</td>
				   	  </tr>
				   	  <tr>
				   	  	<th><s:text name="SVC01_discountEndDate"></s:text></th>
				   		<td>
							<span><input class="date" id="discountEndDate" name="rechargeRule.discountEndDate" style="width: 80px;"/></span>
						</td>
				   	  </tr>
				   	  <tr>
				   	  	<th><s:text name="SVC01_rechargeType"></s:text></th>
				   	  	<td><span><s:select name="rechargeRule.rechargeType" Cssstyle="width:186px;"
								list='#application.CodeTable.getCodes("1400")'
								listKey="CodeKey" listValue="Value"/>
						</span></td>
				   	  </tr>
				   	  <tr>
				   	    <th><s:text name="SVC01_comments"></s:text></th>
				   		<td><span><s:textfield name="rechargeRule.comments" cssClass="text" maxlength="50" cssStyle="width:195px"></s:textfield></span></td>
				   	  </tr>
				   	</table>
				</div>
			</div>
			<div class="box4 right" style="width:50%;height: 385px;" id="rangeInfo">
				<div class="box4-header">
					<strong>
						<!-- 活动范围 -->
						<s:text name="活动范围"/>
					</strong>
				</div>
				<div class="box4-content">
					<!-- 共通树 -->
					<div class="clearfix section-header1"> 
		                <strong>
		                    <span class="ui-icon icon-flag"></span>
		                    <s:text name="SVC01_choiceCounter"/>
		                </strong>
		                <span>
							<select name="selMode" id="selMode" onchange="BINOLMBSVC01_1.ajaxGetNodes();return false;">
								<option value="1"><s:text name="global.page.selMode1"/></option>
								<option value="2"><s:text name="global.page.selMode2"/></option>
								<option value="3"><s:text name="global.page.selMode3"/></option>
							</select>
						</span>
		                <span class="right">
							<input id="position_left_0" class="text locationPosition ac_input" type="text" style="width: 100px;" name="locationPosition" autocomplete="off">
							<a class="search" onclick="BINOLMBSVC01_1.locationCutPosition(this);return false;">
								<span class="ui-icon icon-position"></span>
								<span class="button-text"><s:text name="SVC01_locationPosition"></s:text></span>
							</a>
						</span>
		            </div>
		            <hr class="space" />
		            <div class="treebox" style="overflow:auto;">
		            	<div id="channelRegionDiv" class="hide" style="padding: 5px 5px 5px 8px;border-bottom: 1px dashed #D6D6D6;">
							<strong><s:text name="global.page.channelRegion"/></strong>
							<span></span>
						</div>
		                <div class="jquery_tree treebox tree" id="treeDemo" style="overflow: auto;background:#FCFCFC;">
		                </div>
		            </div>
				</div>
			</div>
		</div>
		<div class="box4">
			<div class="box4-header">
			   	<strong>
					<s:text name="SVC01_ruleDetail"/>
			  	</strong>
			</div>
		  	<div class="box4-content" id="detailInfo">
			   	<table class="detail" cellpadding="0" cellspacing="0">
				   	  <tr>
				   	    <th><s:text name="SVC01_rechargeValueActual"></s:text></th>
				   		<td><span><s:textfield id="rechargeValueActual" name="rechargeRule.rechargeValueActual" cssClass="text" maxlength="10"></s:textfield></span></td>
				   		
				   	  </tr>
				   	  <tr>
				   	  	<th><s:text name="SVC01_giftAmount"></s:text></th>
				   		<td><span><s:textfield id="giftAmount" name="rechargeRule.giftAmount" cssClass="text" maxlength="10"></s:textfield></span></td>
				   	  </tr>
				</table>
			</div>
		</div>
	</form>
	<!-- 表格结束 -->
	</div>
	<div class="center clearfix">
    	<button id="btnConfirm" class="close" type="button" onclick="BINOLMBSVC01_1.confirm();return false;">
    		<span class="ui-icon icon-confirm"></span>
			<span class="button-text"><s:text name="SVC01_confirm"/></span>
		</button>
		<button id="btnCancel" class="close" type="button" onclick="window.close();return false;">
			<span class="ui-icon icon-close"></span>
            <span class="button-text"><s:text name="SVC01_cancel"/></span>
		</button>
    </div>
    <!-- 等待进度圈 -->
    <div id="dataTable_processing" class="dataTables_processing"  style="text-algin:left;"></div>
</div>
</div>
</s:i18n>



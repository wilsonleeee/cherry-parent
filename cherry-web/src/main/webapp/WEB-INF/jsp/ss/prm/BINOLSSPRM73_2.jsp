<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/common/popDataTable.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/common/ajaxfileupload.js"></script>
<link rel="stylesheet" href="/Cherry/plugins/zTree v3.1/css/zTreeStyle/zTreeStyle.css" type="text/css" media="screen, projection">
<script type="text/javascript" src="/Cherry/plugins/zTree v3.1/js/jquery.ztree.all-3.1.js"></script>

<script type="text/javascript" src="/Cherry/js/ss/prm/BINOLSSPRM73_2.js"></script>
<style type="text/css">

.ui-accordion .ui-accordion-header {
    cursor: pointer;
    display: block;
    margin-top: 2px;
    min-height: 0;
    padding: 0.5em 0.5em 0.5em 2em;
    position: relative;
}
.ui-accordion .ui-accordion-icons {
    padding-left: 2.2em;
}
.ui-accordion .ui-accordion-noicons {
    padding-left: 0.7em;
}
.ui-accordion .ui-accordion-icons .ui-accordion-icons {
    padding-left: 2.2em;
}
.ui-accordion .ui-accordion-header .ui-accordion-header-icon {
    left: 0.5em;
    margin-top: -8px;
    position: absolute;
    top: 50%;
}
.ui-accordion .ui-accordion-content {
    border-top: 0 none;
    overflow: auto;
    padding: 1em 2.2em;
}

.ui-accordion-content {
    border: 1px solid #aaaaaa;
    color: #222222;
}
.ui-accordion-content a {
    color: #3366ff;
}
.ui-accordion-content a span {
    color: #000000;
}
.ui-state-active, .ui-state-active, .ui-widget-header .ui-state-active {
    border: 1px solid #aaaaaa;
    color: #212121;
    font-weight: normal;
}
input.date {
	font-size:11px;
	font-family:not specified;
}
.rule_content {
	margin-top:15px;margin-bottom:15px;
}
.icon_del {
	top:20%
}
</style>
<s:url id="readCouponPrtDetailUrl" action="BINOLSSPRM73_readCouponPrtDetail"></s:url>
<s:url id="delImpCouponPrtDetailForNodeUrl" action="BINOLSSPRM73_delImpCouponPrtDetailForNode"></s:url>
<s:url id="delCouponPrtDetailForConditionTypeFilterTypeContentNoUrl" action="BINOLSSPRM73_delCouponPrtDetailForConditionTypeFilterTypeContentNo"></s:url>
<s:url id="delCouponPrtDetailForNodeUrl" action="BINOLSSPRM73_delCouponPrtDetailForNode"/>
<s:url id="save_url" action="BINOLSSPRM73_save"></s:url>
<s:url id="importCounter_Url" action="BINOLSSPRM73_importCounter"></s:url>
<s:url id="counterDialogInit_Url" action="BINOLSSPRM73_counterInit"></s:url>
<s:url id="importMember_Url" action="BINOLSSPRM73_importMember"></s:url>
<s:url id="memberDialogInit_Url" action="BINOLSSPRM73_memberInit"></s:url>
<s:url action="BINOLSSPRM73_channel" namespace="/ss" id="searchChannelUrl"></s:url>
<s:url action="BINOLCM02_initCampaignDialog_3" namespace="/common" id="searchCampaignInit_Url"></s:url>
<s:url action="BINOLSSPRM73_execlLoadInit" namespace="/ss" id="popCounterUploadUrl"></s:url>
<s:url action="BINOLSSPRM73_popMemberUpload" namespace="/ss" id="popMemberUploadUrl"></s:url>
<s:url action="BINOLSSPRM73_productExeclLoad" namespace="/ss" id="popProductUploadUrl"></s:url>
<s:url action="BINOLSSPRM73_getCNTList" namespace="/ss" id="getCNTList"></s:url>
<s:url action="BINOLSSPRM73_delCNT" namespace="/ss" id="delCNT"></s:url>
<s:url action="BINOLSSPRM73_channelDialog" namespace="/ss" id="popChannelDialogUrl"></s:url>

<s:url action="BINOLSSPRM73_confirmUseCond" namespace="/ss" id="confirmUseCondUrl"></s:url>
<s:url action="BINOLSSPRM73_cancelUseCond" namespace="/ss" id="cancelUseCondUrl"></s:url>

<a href="${popChannelDialogUrl }" id="popChannelDialogUrl"></a>

<!-- 导入页面Url -->
<s:url action="BINOLSSPRM73_execlLoadInit" namespace="/ss" id="popUpload"></s:url>
<a href="${popUpload}" id="popUploadUrl"></a>
<!-- 加载导入会员Url -->
<s:url action="BINOLSSPRM73_getExeclUploadMemberList" namespace="/ss" id="getExeclUploadMemberList"></s:url>
<a href="${getExeclUploadMemberList}" id="getExeclUploadMemberListUrl"></a>
<!-- 会员等级List Url -->
<s:url action="BINOLSSPRM73_getMemLevelList" namespace="/ss" id="getMemLevelList"></s:url>
<a href="${getMemLevelList}" id="getMemLevelListUrl"></a>
<!-- 活动弹出框体 -->
<s:url action="BINOLCM02_initCampaignDialog_2" namespace="/common" id="searchCampaignInit"></s:url>
<a id="searchCampaignInitUrl2" href="${searchCampaignInit}"></a>
<!-- 使用门槛弹出框体 -->
<s:url action="BINOLSSPRM73_initUseContent" namespace="/ss" id="initUseContent"></s:url>
<a id="initUseContentUrl" href="${initUseContent}"></a>
<!-- execl导出URL -->
<s:url action="BINOLSSPRM73_export" namespace="/ss" id="exportExeclUrl" />
<a id ="exportExecl" style="display:none" href="${exportExeclUrl}"/>
<!-- 删除导入会员节点 -->
<s:url action="BINOLSSPRM73_delImpCouponMemberDetail" namespace="/ss" id="delImpCouponMemberDetailUrl" />
<a id ="delImpCouponMemberDetail" style="display:none" href="${delImpCouponMemberDetailUrl}"/>
<!-- 查询柜台渠道选择Url -->
<s:url action="BINOLSSPRM73_seachChannelWay" namespace="/ss" id="seachChannelWayUrl" />
<a id ="seachChannelWay" style="display:none" href="${seachChannelWayUrl}"/>
<div class="hide">
	<a id="saveUrl" href="${save_url}"></a>
	<a id="importCounterUrl" href="${importCounter_Url}"></a>
	<a id="counterDialogInitUrl" href="${counterDialogInit_Url}"></a>
	<a id="searchCampaignInitUrl" href="${searchCampaignInit_Url}"></a>
	<a id="importMemberUrl" href="${importMember_Url}"></a>
	<a id="memberDialogInitUrl" href="${memberDialogInit_Url}"></a>
	<a id="popCounterUpload" href="${popCounterUploadUrl}"/>
	<a id="popProductUpload" href="${popProductUploadUrl}"/>
	<span id="popMemberUpload" style="display:none">${popMemberUploadUrl}</span>
	<!-- <span id="popProductUpload" style="display:none">${popProductUploadUrl}</span> -->
	<a id="getCNTListUrl" href="${getCNTList}"></a>
	<a id="delCNTUrl" href="${delCNT}"></a>
	<span id="popUpload" style="display: none">${popUploadUrl}</span>
	<a id="readCouponPrtDetailUrl" href="${readCouponPrtDetailUrl}"></a>

	<a id="confirmUseCondUrlID" href="${confirmUseCondUrl}"></a>
	<a id="cancelUseCondUrlID" href="${cancelUseCondUrl}"></a>

	<a id="delImpCouponPrtDetailForNodeUrl" href="${delImpCouponPrtDetailForNodeUrl}"></a>
	<span id="delCouponPrtDetailForConditionTypeFilterTypeContentNoUrlUrl" style="display:none">${delCouponPrtDetailForConditionTypeFilterTypeContentNoUrllUrl}</span>
	<span id="delCouponPrtDetailForNodeUrl" style="display:none">${delCouponPrtDetailForNodeUrl}</span>
	<div id="messageDialogDiv" class="hide ui-dialog-content ui-widget-content" style="display: none; width: auto; min-height: 200px;">
		<p id="messageContent" class="message hide" style="margin:40px auto 30px;"><span id="messageContentSpan"></span></p>
		<p id="successContent" class="success hide" style="margin:40px auto 30px;"><span id="successContentSpan"></span></p>
		<p class="center">
			<button id="btnMessageConfirm" class="close" type="button">
				<span class="ui-icon icon-confirm"></span>
				<span class="button-text">确定</span>
			</button>
		</p>
	</div>

	<div id="coupon-content1">
		<div class="box2-content  clearfix rule_content">
			<div style="width:12%;float:left;" onclick="binolssprm7302.contentDivShow(this);return false;">
				<div class="ui-icon icon-arrow-crm" ></div>
				<label class="bg_title">代金券</label>
			</div>
			<div style="width:70%;float:left;">
				<input type="hidden" name ="couponType" value="1">抵扣金额&nbsp;
				<input type="text" name ="faceValue" class="number" maxlength="10" onkeyup="this.value=this.value.replace(/\D/g,'')" onblur="this.value=this.value.replace(/\D/g,'')"/> 元
			</div>
			<div class="useCondButton" style="width:18%;float:right;">
				<a class="add"  style="margin-left:30px;" onclick="binolssprm7302.popUseContionDialog(this,1);return false;">
					<span class="ui-icon icon-add"></span><span class="button-text">使用门槛设置</span>
				</a>
				<input type="hidden" name="contentNo" value="" />
				<input type="hidden" name="useCondJson" class="useCondJson hide" /> <%-- 存储使用门槛系列JSON字符串 --%>
			</div>
			<span onclick="binolssprm7302.delCoupon(this)" class="icon_del right" style="display:none"></span>
		</div>
	</div>

	<div id="coupon-content5">
		<div class="box2-content  clearfix rule_content">
			<div style="width:12%;float:left;" onclick="binolssprm7302.contentDivShow(this);return false;">
				<div class="ui-icon icon-arrow-crm" ></div>
				<label class="bg_title">折扣券</label>
			</div>
			<div style="width:70%;float:left;">
				<input type="hidden" name ="couponType" value="5"/>
				<select name="zkType" class="left" style="width:100px;" onchange="binolssprm7302.changeZk('zkType', this)">
					<option value='0'>整单折扣</option>
					<option value='1'>单品折扣</option>
				</select>
				<span id="zkType0">
					折扣率&nbsp;<input type="text" name ="zkValue" class="number zkCoupon" maxlength="3" /> %
					折扣金额上限&nbsp;<input type="text" name ="zkAmountLimt" class="number" maxlength="8" />元
				</span>
				<span id="zkType1" class="hide">
					折扣率&nbsp;<input type="text" name ="zkValue2" class="number zkCoupon" maxlength="3" /> %
					折扣数量上限&nbsp;<input type="text" name ="zkNumLimt" class="number" maxlength="3" />个</br>
					<div class="box2 box2-active" style="display:inline;height:100px;">
						<div class="box2-header clearfix" style="display:inline" >
							<strong class="active" style="width:70px;"><span class="ui-icon icon-flag"></span>产品范围
							<span>
								<a class="add" onClick="binolssprm7302.openOneProDialog2(this);return false;">
									<span class="ui-icon icon-add"></span><span class="button-text">选择产品</span>
								</a>
							</span>
							</strong>
						</div>
						<div class="box2-content clearfix" style="height:100px; width: 65%">
							<!-- 产品在此处展示 -->
							<div id="proContent" class="proContent z-tbody">
							</div>
						</div>
					</div>
					<%--<span class="left" style="margin-top:10px">
						<a class="add" onClick="binolssprm7302.openOneProDialog(this);return false;">
							<span class="ui-icon icon-add"></span><span class="button-text">选择某个产品</span>
						</a>
					</span>
					<table style="margin-left: 15px;" class="left">
						<tbody id="proContent" class="z-tbody">
						</tbody>
					</table>--%>
				</span>
			</div>
			<div class="useCondButton" style="width:18%;float:right;">
				<a class="add"  style="margin-left:30px;" onclick="binolssprm7302.popUseContionDialog(this,1);return false;">
					<span class="ui-icon icon-add"></span><span class="button-text">使用门槛设置</span>
				</a>
				<input type="hidden" name="contentNo" value="" />
				<input type="hidden" name="useCondJson" class="useCondJson hide" /> <%-- 存储使用门槛系列JSON字符串 --%>
			</div>
			<span onclick="binolssprm7302.delCoupon(this)" class="icon_del right" style="display:none"></span>
		</div>
	</div>
    <div id="coupon-content2">
	    <div class="box2-content clearfix rule_content">
		    <div style="width:12%;float:left;" onclick="binolssprm7302.contentDivShow(this);return false;">
    		    <div class="ui-icon icon-arrow-crm"></div>
				<label class="bg_title">代物券</label>
			</div>
			<div style="width:70%;float:left;">
				<div class="left" style="width:70%;">
					<input type="hidden" name ="couponType" value="2">
					<div class="box2 box2-active" style="display:inline;height:100px;">
						<div class="box2-header clearfix" style="display:inline" >
							<strong class="left active"><span class="ui-icon icon-flag"></span>产品范围</strong>
							<span>
								<a class="add" onClick="binolssprm7302.openProDialog2('3',this);return false;">
									<span class="ui-icon icon-add"></span><span class="button-text">产品选择</span>
								</a>
							</span>
							&nbsp;产品之间的关系:
							<select id="fullFlag" name="fullFlag" style="width:50px;" onclick="binolssprm7302.changeFullFlag(this)">
								<option value="0">或者</option>
								<option value="1">且</option>
							</select>
							<span id="chooseSpan">
								以下任选
								<input type="text" class="number" name="maxCount" maxlength="2"/>
								个
							</span>
						</div>
						<div class="box2-content clearfix" style="height:100px;overflow:auto">
							<!-- 产品在此处展示 -->
							<div id="contentProduct" class="contentProduct z-tbody">

							</div>
						</div>
					</div>
				</div>
			</div>
		    <div class="useCondButton" style="width:18%;float:right;">
				<a class="add"  style="margin-left:30px;" onclick="binolssprm7302.popUseContionDialog(this,1);return false;">
					<span class="ui-icon icon-add"></span><span class="button-text">使用门槛设置</span>
				</a>
				<input type="hidden" name="contentNo" value=""/>
				<input type="hidden" name="useCondJson" class="useCondJson hide" /> <%-- 存储使用门槛系列JSON字符串 --%>
			</div>
			<span onclick="binolssprm7302.delCoupon(this)" class="icon_del right" style="display:none"></span>
		</div>
    </div>
    <div id="coupon-content3">
		<div class="box2-content  clearfix rule_content">
			<div style="width:12%;float:left;" onclick="binolssprm7302.contentDivShow(this);return false;">
				<div class="ui-icon icon-arrow-crm" ></div>
				<label class="bg_title">资格券</label>
			</div>
			<div style="width:70%;float:left;">
				<input type="hidden" name ="couponType" value="3">
				<div style="width:100%;float:left;" >
					<div class="left campDiv" style="width: 70%;" id="sendRuleWhite">
						<div class="box2 box2-active" style="display:inline;height:100px;">
							<div class="box2-header clearfix" style="display:inline" >
								<strong class="left active"><span class="ui-icon icon-flag"></span>活动选择</strong>
								<div class="right">
									<a class="add" onClick="binolssprm7302.popCampDialog('1','1',this);return false;">
										<span class="ui-icon icon-add"></span><span class="button-text">选择活动</span>
									</a>
								</div>
							</div>
							<div class="box2-content clearfix" style="height:100px;"><%-- 全部对应区域 --%>
								<div class="hide campList z-tbody" id="contentCampaign" style="display: block;height:100px; overflow:auto">

								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="useCondButton" style="width:18%;float:right;">
				<a class="add"  style="margin-left:30px;" onclick="binolssprm7302.popUseContionDialog(this,1);return false;">
					<span class="ui-icon icon-add"></span><span class="button-text">使用门槛设置</span>
				</a>
				<input type="hidden" name="contentNo" value="" />
				<input type="hidden" name="useCondJson" class="useCondJson hide" /> <%-- 存储使用门槛系列JSON字符串 --%>
			</div>
			<span onclick="binolssprm7302.delCoupon(this)" class="icon_del right" style="display:none"></span>
		</div>
	</div>
</div>
<s:i18n name="i18n.ss.BINOLSSPRM73">
<div class="main container clearfix">
	<div class="panel ui-corner-all">
		<div id="div_main">
		<cherry:form id="mainForm" method="post" class="inline" csrftoken="false">
			<div class="panel-header">
				<div class="clearfix">
					<span class="breadcrumb left">
						<span class="ui-icon icon-breadcrumb"></span>
						优惠券规则管理 &gt;编辑规则
					</span>
				</div>
			</div>
			<div id="actionResultDisplay"></div>
			<div id="errorDiv" class="actionError" style="display:none;">
				<ul>
					<li><span id="errorSpan"></span></li>
				</ul>
			</div>
			<div id="accordion">
				<h3>基本信息</h3>
				<div class="panel-content">
					<div id="baseInfo">
						<div class="box4-content clearfix">
							<table class="detail">
								<tbody>
									<tr>
										<th>规则名称<span class="red">*</span></th>
										<td><span><input class="text" name ="couponRule.ruleName" maxlength="40" value="<s:property value='couponRule.ruleName'/>"/></span></td>
										<th>规则代码<span class="red">*</span></th>
										<td>
											<span><input class="text" name ="couponRule.ruleCode" maxlength="25" value="<s:property value='couponRule.ruleCode'/>" disabled="disabled"/></span>
											<input type="hidden" name ="couponRule.ruleCode" value="<s:property value='couponRule.ruleCode'/>" id="ruleCode">
											<input type="hidden" name ="couponRule.brandInfoId" value="<s:property value='couponRule.brandInfoId'/>" id="brandInfoId">
											<input type="hidden" name ="couponRule.maxContentNo" value="<s:property value='couponRule.maxContentNo'/>" id="maxContentNo">
										</td>
									</tr>
									<tr>
										<th>券发放日期<span class="red">*</span></th>
										<td>
										<span>
										<input class="date" id="sendStartTime" name="couponRule.sendStartTime" value="<s:property value='couponRule.sendStartTime'/>"/>
										-
										<input class="date" id="sendEndTime" name="couponRule.sendEndTime" value="<s:property value='couponRule.sendEndTime'/>"/>
										</span>
										</td>
										<th>校验方式</th>
										<td><s:select list='#application.CodeTable.getCodes("1381")' listKey="CodeKey" listValue="Value" name="couponRule.validMode" cssStyle="width:auto;" value="couponRule.validMode"></s:select></td>
									</tr>
									<tr>
										<th>总发券数量上限</th>
										<td><span><input type="text" name ="couponRule.sumQuantity" class="number" style="width: 60px;" maxlength="7" value="<s:property value='couponRule.sumQuantity'/>"/></span></td>
										<th>可否转赠</th>
										<td>
											<span>
											<s:if test='couponRule.isGive == "1"'>
											<s:radio name="couponRule.isGive" list="#application.CodeTable.getCodes('1382')"
											listKey="CodeKey" listValue="Value" value="couponRule.isGive"></s:radio>
											</s:if>
											<s:else>
											<s:radio name="couponRule.isGive" list="#application.CodeTable.getCodes('1382')"
											listKey="CodeKey" listValue="Value" value="0"></s:radio>
											</s:else>
											</span>
										</td>
									</tr>
									<tr>
										<th>一单发券上限<span class="red">*</span></th>
										<td><span><input type="text" name ="couponRule.quantity" class="number" style="width: 60px;" maxlength="2" value="<s:property value='couponRule.quantity'/>"/></span></td>
										<th>单个会员发券数量上限</th>
										<td><span><input type="text" name ="couponRule.limitQuantity" class="number" maxlength="3" value="<s:property value='couponRule.limitQuantity'/>"/></span></td>
									</tr>
										<tr>
										<th>券规则说明</th>
										<td colspan="3">
										<textarea cols="" rows="" name="couponRule.description" style="width: 40%;"><s:property value='couponRule.description'/></textarea>
										</td>
									</tr>
								</tbody>
							</table>
						</div>
					</div>
				</div>
				<h3>发送门槛</h3>
				<div style="min-height: 500px;" id="sendCondition">
					<div class="box4-content  clearfix">
						<div style="width:12%;float:left;">
							<div class="ui-icon icon-arrow-crm" onclick="binolssprm7302.tabDiv(this);return false;"></div>
							<label class="bg_title" >购买门店</label>
						</div>
						<div style="width:88%;float:left;display:none;" id="SendCounterDiv">
							<div class="left counterWhiteDiv" style="width: 50%;" id="sendCounterWhite">
								<div class="box2 box2-active" style="display:inline;height:150px;">
									<div class="box2-header clearfix" style="display:inline" >
										<strong class="left active"><span class="ui-icon icon-flag"></span>购买柜台</strong>
										<div class="right">
											<input type="hidden" name="oldKbn" class="oldKbn" value='<s:property value="sendCondInfo.counterKbn_w"/>' />
											<span>柜台选择</span>
											<select id="cnt_s_w_select" name="counterKbn_w" style="width:100px;display:inline;" onchange="binolssprm7302.changeCounterSpan(1,1, this,0,false);" class="z-param">
												<option <s:if test='%{sendCondInfo.counterKbn_w == "0"}'>selected</s:if> value='0'>全部</option>
												<option <s:if test='%{sendCondInfo.counterKbn_w == "1"}'>selected</s:if> value='1'>导入门店</option>
												<option <s:if test='%{sendCondInfo.counterKbn_w == "2"}'>selected</s:if> value='2'>按渠道指定柜台</option>
												<option <s:if test='%{sendCondInfo.counterKbn_w == "3"}'>selected</s:if> value='3'>渠道选择</option>
											</select>
											<a class="add" <s:if test='%{sendCondInfo.counterKbn_w != "1"}'>style="display: none;"</s:if><s:else>style="display:inline-block;"</s:else> id="cnt_s_w_inputButton" onClick="binolssprm7302.popCounterLoadDialog('1','1','0');return false;">
												<span class="ui-icon icon-add"></span><span class="button-text">导入柜台</span>
											</a>
											<input type="hidden" id="cntwhiteChannel_send" name="cntwhiteChannel_send"  value="<s:property value='sendCondInfo.cntwhiteChannel'/>"/>
										</div>
									</div>
									<div class="box2-content clearfix" style="height:150px;"><%-- 全部对应区域 --%>
										<div class="hide counterInput" id="cnt_s_w_commonDiv" style="display: block;height:150px; overflow:auto;">
											<s:if test='%{sendCondInfo.counterKbn_w == "1"}'>
												<s:iterator value="sendCondInfo.cntWhiteList" id="cntWhiteInfo">
													<li>
														<%--<input type="checkbox" value='<s:property value="#cntWhiteInfo.organizationID"/>'  onclick="binolssprm7302.cnt_s_removeLi(this,1,1);"  name="cntWhiteChoose" checked="checked" value='<s:property value="#cntWhiteInfo.organizationID"/>'/>--%><s:property value="#cntWhiteInfo.counterCode"/>  <s:property value="#cntWhiteInfo.counterName"/>
														<input name="organizationID"  class="hide" value='<s:property value="#cntWhiteInfo.organizationID"/>'/>
														<input name="counterCode"  class="hide" value='<s:property value="#cntWhiteInfo.counterCode"/>'/>
														<input name="counterName"  class="hide" value='<s:property value="#cntWhiteInfo.counterName"/>'/>
													</li>
												</s:iterator>
											</s:if>
											<s:elseif test='%{sendCondInfo.counterKbn_w == "3"}'>
												<s:iterator value="sendCondInfo.channel_list" id="channel_list">
													<li>
														<input type="checkbox"value='<s:property value="#cntChannelInfo.id"/>'  <s:if test="#channel_list.checkFlag == 1">checked="checked"</s:if> /> <s:property value="#channel_list.name"/>
														<input name="id"  class="hide" value='<s:property value="#channel_list.id"/>'/>
														<input name="name"  class="hide" value='<s:property value="#channel_list.name"/>'/>
													</li>
												</s:iterator>
											</s:elseif>
										</div>
										<div class="jquery_tree" id="cnt_s_w_treeDiv" style="height:150px;display:none">
											<div class="treebox" style="overflow:auto;height: 150px">
												<div  class="ztree" id = cnt_s_w_div_tree></div>
											</div>
										</div>
									</div>
								</div>
							</div>
							<div class="left counterBlackDiv" style="width: 50%;" id="sendCounterBlack">
								<div class="box2 box2-active" style="display:inline;height:150px;">
									<div class="box2-header clearfix" >
										<strong class="left active"><span class="ui-icon icon-flag"></span>排除柜台</strong>
										<div class="right">
											<a class="add" onClick="binolssprm7302.popCounterLoadDialog('1','2','0');return false;">
												<span class="ui-icon icon-add"></span><span class="button-text">导入排除柜台</span>
											</a>
										</div>
									</div>
									<div class="box2-content clearfix counterInput" style="height:150px;overflow:auto;"id="cnt_s_b_div">
										<s:iterator value="sendCondInfo.cntBlackList" id="cntBlackInfo">
											<li>
												<%--<input type="checkbox" value='<s:property value="#cntBlackInfo.organizationID"/>' onclick="binolssprm7302.cnt_s_removeLi(this,2,1);" name="cntBlackChoose" checked="checked" value='<s:property value="#cntBlackInfo.organizationID"/>'/>--%><s:property value="#cntBlackInfo.counterCode"/>  <s:property value="#cntBlackInfo.counterName"/>
												<input name="organizationID"  class="hide" value='<s:property value="#cntBlackInfo.organizationID"/>'/>
												<input name="counterCode"  class="hide" value='<s:property value="#cntBlackInfo.counterCode"/>'/>
												<input name="counterName" class="hide" value='<s:property value="#cntBlackInfo.counterName"/>'/>
											</li>
										</s:iterator>
									</div>
								</div>
							</div>
						</div>
					</div>
					<div class="box4-content  clearfix">
						<div style="width:12%;float:left;">
							<div class="ui-icon icon-arrow-crm" onclick="binolssprm7302.tabDiv(this);return false;"></div>
							<label class="bg_title">购买金额</label>
						</div>
						<div style="width:88%;float:left;display:none;">
							金额条件:<select name="amountCondition" class="z-param" style="width:80px;">
							<option <s:if test='%{sendCondInfo.amountCondition == "0"}'>selected</s:if> value="0">实付金额</option>
							<option <s:if test='%{sendCondInfo.amountCondition == "1"}'>selected</s:if> value="1">正价金额</option>
						</select>
							满 <input type="text" name ="sendMinAmount" class="number z-param" style="width:60px;" maxlength="9" value="<s:property value='sendCondInfo.sendMinAmount'/>"/> 元
						</div>
					</div>
					<div class="hide" id="productDivDialog"></div> <!-- 用于弹出导入产品DIV -->
					<div class="hide" id="switchBtnDialog"></div> <!-- 用于切换按钮DIV -->
					<div class="box4-content  clearfix">
						<div style="width:12%;float:left;">
							<div class="ui-icon icon-arrow-crm" onclick="binolssprm7302.tabDiv(this);return false;"></div>
							<label class="bg_title" >产品范围</label>
						</div>
						<div style="width:88%;float:left;display:none;">
							<div class="left" style="width:50%;" id="sendProductWhite">
								<div class="box2 box2-active" style="display:inline;height:150px;">
									<div class="box2-header clearfix" style="display:inline" >
										<strong class="left active"><span class="ui-icon icon-flag"></span>产品范围</strong>
											<span class="right">
												<input type="hidden" name="oldKbn" class="oldKbn" value='<s:property value="sendCondInfo.productKbn_w"/>' />
												<span>产品选择 </span>
													<select name="productKbn_w" id="prt_s_w_productKbn" style="width:100px;" onchange="binolssprm7302.changePrtSpan('1','1', 'prt_s_w_productKbn');" class="z-param">
														<option <s:if test='%{sendCondInfo.productKbn_w == "0"}'>selected</s:if> value='0'>全部</option>
														<option <s:if test='%{sendCondInfo.productKbn_w == "1"}'>selected</s:if> value='1'>分类选择</option>
														<option <s:if test='%{sendCondInfo.productKbn_w == "2"}'>selected</s:if> value='2'>产品选择</option>
														<option <s:if test='%{sendCondInfo.productKbn_w == "3"}'>selected</s:if> value='3'>产品导入</option>
													</select>
												<span id="prt_s_w_BtnSpan">
													<span id="prt_s_w_selTypeBtn" class="hide">
														<a class="add" onClick="binolssprm7302.openProTypeDialog2('1');return false;">
															<span class="ui-icon icon-add"></span><span class="button-text">选择分类</span>
														</a>
														<!-- <input type="button" value="选择分类" onclick="binolssprm7302.openProTypeDialog2('1');"/> -->
													</span>
												<span id="prt_s_w_selPrtBtn" class="hide">
													<!-- <input type="button" value="选择产品" onclick="binolssprm7302.openProDialog2('1');"/> -->
													<a class="add" onClick="binolssprm7302.openProDialog2('1');return false;">
														<span class="ui-icon icon-add"></span><span class="button-text">选择产品</span>
													</a>
												</span>
												<span id="prt_s_w_impPrtBtn" class="hide">
													<!-- <input type="button" value="导入产品" onclick="binolssprm7302.popProductLoadDialog('1','1');"/>  -->
													<a class="add" onClick="binolssprm7302.popProductLoadDialog('1','1');return false;">
														<span class="ui-icon icon-add"></span><span class="button-text">导入产品</span>
													</a>
												</span>
											</span>
											&nbsp;&nbsp;
											<span>产品关系</span>
											<select name="relation" style="width:50px;" class="z-param">
												<option <s:if test='%{sendCondInfo.relation == "2"}'>selected</s:if> value='2'>或者</option>
												<option <s:if test='%{sendCondInfo.relation == "1"}'>selected</s:if> value='1'>并且</option>
											</select>
										</span>
									</div>
									<!-- 白名单内容框 -->
									<div id="prt_s_w_div" class="box2-content clearfix" style="height:150px;overflow:auto">
										<!-- 全部页 空白页 -->
										<div id="prt_s_w_allDIV" class="hide">
										</div>
										<!-- 产品分类内容框 -->
										<div id="prt_s_w_typeDIV" class="hide">
											<s:if test='%{sendCondInfo.productKbn_w == "1"}'>
												<s:iterator value="prt_s_w_PrtCouponPrtDetail" id="prtCouponPrtDetail">
													<li>
														<input type="hidden" name="cateValId" value='<s:property value="#prtCouponPrtDetail.prtObjId"/>' />
														<input type="hidden" name="cateId" value='<s:property value="#prtCouponPrtDetail.prtObjId"/>' />
														<input type="checkbox" name="prtTypeBlackChoose" checked="checked" onclick="binolssprm7302.removeLi(this);" />
														<s:property value="#prtCouponPrtDetail.cateVal"/>&nbsp; <s:property value="#prtCouponPrtDetail.cateValName"/>
													</li>
												</s:iterator>
											</s:if>
										</div>
										<!-- 选择产品内容框 -->
										<div id="prt_s_w_selPrtDIV" class="hide">
											<s:if test='%{sendCondInfo.productKbn_w == "2"}'>
												<s:iterator value="prt_s_w_PrtCouponPrtDetail" id="prtCouponPrtDetail">
													<li>
														<input type="hidden" name="prtVendorId" value='<s:property value="#prtCouponPrtDetail.prtObjId"/>' />
														<input type="hidden" name="prtVendId" value='<s:property value="#prtCouponPrtDetail.prtObjId"/>' />
														<input type="checkbox" name="selPrtBlackChoose" checked="checked" onclick="binolssprm7302.removeLi(this);" />
														<s:property value="#prtCouponPrtDetail.unitCode"/>
														&nbsp; <s:property value="#prtCouponPrtDetail.barCode"/>
														&nbsp; <s:property value="#prtCouponPrtDetail.nameTotal"/>
														&nbsp;&nbsp;<input type="text" style="width: 20px;" name="proNum" value='<s:property value="#prtCouponPrtDetail.prtObjNum"/>' onkeyup="this.value=this.value.replace(/\D/g,'')" onblur="this.value=this.value.replace(/\D/g,'')" />
													</li>
												</s:iterator>
											</s:if>
										</div>
										<!-- 导入产品内容框 -->
										<div id="prt_s_w_impPrtDIV" class="hide">
											<s:if test='%{sendCondInfo.productKbn_w == "3"}'>
												<s:iterator value="prt_s_w_PrtCouponPrtDetail" id="prtCouponPrtDetail">
													<li>
														<input type="hidden" name="prtVendorId" value='<s:property value="#prtCouponPrtDetail.prtObjId"/>' />
														<input type="hidden" name="prtVendId" value='<s:property value="#prtCouponPrtDetail.prtObjId"/>' />
														<input type="hidden" name="proNum" value='<s:property value="#prtCouponPrtDetail.proNum"/>' />
														<%--<input type="checkbox" name="selPrtBlackChoose" checked="checked" onclick="binolssprm7302.removeLi(this);binolssprm7302.delPrtLi('1','1',this);" />--%>
														<s:property value="#prtCouponPrtDetail.unitCode"/>
														&nbsp; <s:property value="#prtCouponPrtDetail.barCode"/>
														&nbsp; <s:property value="#prtCouponPrtDetail.nameTotal"/>
														&nbsp;&nbsp;<input type="text" style="width: 20px;" name="prtNumLab" value='<s:property value="#prtCouponPrtDetail.prtObjNum"/>' disabled />
													</li>
												</s:iterator>
											</s:if>
										</div>
									</div>
								</div>
							</div>
							<div class="left" style="width: 50%;" id="sendProductBlack">
								<div class="box2 box2-active" style="display:inline;height:150px;">
									<div class="box2-header clearfix" >
										<strong class="left active"><span class="ui-icon icon-flag"></span>排除产品</strong>
										<div class="right">
											<a class="add" onClick="binolssprm7302.popProductLoadDialog('1','2');return false;">
												<span class="ui-icon icon-add"></span><span class="button-text">导入排除产品</span>
											</a>
										</div>
									</div>
									<!-- 黑名单内容框 -->
									<div id="prt_s_b_div" class="box2-content clearfix" style="height:150px;overflow:auto" >
										<input type="hidden" name ="productKbn_b" value="<s:property value='sendCondInfo.productKbn_b'/>" id="prt_s_b_productKbn" class="z-param">
										<!-- 导入产品内容框 -->
										<div id="prt_s_b_impPrtDIV" class="show">
											<s:if test='%{sendCondInfo.productKbn_b == "3"}'>
												<s:iterator value="prt_s_b_PrtCouponPrtDetail" id="prtCouponPrtDetail">
													<li>
														<input type="hidden" name="prtVendorId" value='<s:property value="#prtCouponPrtDetail.prtObjId"/>' />
														<input type="hidden" name="prtVendId" value='<s:property value="#prtCouponPrtDetail.prtObjId"/>' />
														<%--<input type="checkbox" name="selPrtBlackChoose" checked="checked" onclick="binolssprm7302.removeLi(this);binolssprm7302.delPrtLi('1','2',this);" />--%>
														<s:property value="#prtCouponPrtDetail.unitCode"/>
														&nbsp; <s:property value="#prtCouponPrtDetail.barCode"/>
														&nbsp; <s:property value="#prtCouponPrtDetail.nameTotal"/>
													</li>
												</s:iterator>
											</s:if>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
					<div class="box4-content  clearfix">
						<div style="width:12%;float:left;">
							<div class="ui-icon icon-arrow-crm" onclick="binolssprm7302.tabDiv(this);return false;"></div>
							<label class="bg_title" >需参活动</label>
						</div>
						<div style="width:88%;float:left;display:none;" id="SendRuleDiv">
							<div class="left campDiv" style="width: 50%;" id="sendRuleWhite">
								<div class="box2 box2-active" style="display:inline;height:150px;">
									<div class="box2-header clearfix" style="display:inline" >
										<strong class="left active"><span class="ui-icon icon-flag"></span>活动选择</strong>
										<div class="right">
											<a class="add" onClick="binolssprm7302.popCampDialog('1','1',this);return false;">
												<span class="ui-icon icon-add"></span><span class="button-text">活动选择</span>
											</a>
										</div>
									</div>
									<div class="box2-content clearfix" style="height:150px;"><%-- 全部对应区域 --%>
										<div class="hide campList" id="rul_s_w_commonDiv" style="display: block;height:150px; overflow:auto">
											<s:iterator value="sendCondInfo.campList_w" id="campList_w">
												<li>
													<input type="checkbox" onclick="binolssprm7302.removeLi(this);return false;" name="campListWhiteChoose" checked="checked"/><s:property value="#campList_w.campaignCode"/> &nbsp; <s:property value="#campList_w.campName"/>&nbsp;
													<s:if test='#campList_w.campaignMode == "0"'>
														会员活动
													</s:if>
													<s:elseif test='#campList_w.campaignMode == "1"'>
														促销活动
													</s:elseif>
													<s:elseif test='#campList_w.campaignMode == "2"'>
														电子券活动
													</s:elseif>
													<input name="campaignCode" value='<s:property value="#campList_w.campaignCode"/>' type="hidden">
													<input name="campaignMode" value='<s:property value="#campList_w.campaignMode"/>'type="hidden">
													<input name="campaignName" value='<s:property value="#campList_w.campaignName"/>' type="hidden">
												</li>
											</s:iterator>
										</div>
									</div>
								</div>
							</div>
							<div class="left campDiv" style="width: 50%;" id="sendRuleBlack">
								<div class="box2 box2-active" style="display:inline;height:150px;">
									<div class="box2-header clearfix" >
										<strong class="left active"><span class="ui-icon icon-flag"></span>活动排除</strong>
										<div class="right">
											<a class="add" onClick="binolssprm7302.popCampDialog('1','2',this);return false;">
												<span class="ui-icon icon-add"></span><span class="button-text">排除活动选择</span>
											</a>
										</div>
									</div>
									<div class="box2-content clearfix campList" style="height:150px; overflow:auto;" id="rul_s_b_div">
										<s:iterator value="sendCondInfo.campList_b" id="campList_b">
											<li>
												<input type="checkbox" onclick="binolssprm7302.removeLi(this);return false;" name="campListBlackChoose" checked="checked"/><s:property value="#campList_b.campaignCode"/>&nbsp;  <s:property value="#campList_b.campName"/>&nbsp;
												<s:if test='#campList_b.campaignMode == "0"'>
													会员活动
												</s:if>
												<s:elseif test='#campList_b.campaignMode == "1"'>
													促销活动
												</s:elseif>
												<s:elseif test='#campList_b.campaignMode == "2"'>
													电子券活动
												</s:elseif>
												<input name="campaignCode" value='<s:property value="#campList_b.campaignCode"/>' type="hidden">
												<input name="campaignMode" value='<s:property value="#campList_b.campaignMode"/>'type="hidden">
												<input name="campaignName" value='<s:property value="#campList_b.campaignName"/>' type="hidden">
											</li>
										</s:iterator>
									</div>
								</div>
							</div>
						</div>
					</div>
					<div class="box4-content  clearfix">
						<div style="width:12%;float:left;">
							<div class="ui-icon icon-arrow-crm" onclick="binolssprm7302.tabDiv(this);return false;"></div>
							<label class="bg_title" >发券对象</label>
						</div>
						<div style="width:88%;float:left;display:none" id="sendMemberDiv" class="memberDiv hide">
							<div class="left memberWriteDiv" style="width:50%;" id="sendMemberWhite">
								<div class="box2 box2-active" style="display:inline;height:150px;">
									<div class="box2-header clearfix" style="display:inline" >
										<strong class="left active"><span class="ui-icon icon-flag"></span>发券对象</strong>
											<span class="right">
											&nbsp;&nbsp;
											<span>对象类型</span>
											&nbsp;&nbsp;
											<input type="hidden" class="oldKbn" value='<s:property value="sendCondInfo.memberKbn_w"/>'/>
											<select id="mem_s_w_select" name="memberKbn_w" style="width:100px;" onchange="binolssprm7302.changeMemberSpan(1,0,1,this);return false;" class="z-param">
												<option <s:if test='%{sendCondInfo.memberKbn_w == "0"}'>selected</s:if> value='0'>全部</option>
												<option <s:if test='%{sendCondInfo.memberKbn_w == "1"}'>selected</s:if> value='1'>导入会员</option>
												<option <s:if test='%{sendCondInfo.memberKbn_w == "2"}'>selected</s:if> value='2'>会员等级</option>
												<option <s:if test='%{sendCondInfo.memberKbn_w == "3"}'>selected</s:if> value='3'>仅限非会员</option>
											</select>
											<span class="changeSpan <s:if test='%{sendCondInfo.memberKbn_w != "1"}'>hide</s:if>">
												<a class="add" onclick="binolssprm7302.popMemberLoadDialog(1,1,0,this);return false;">
													<span class="ui-icon icon-add"></span>
													<span class="button-text">导入会员</span>
												</a>
											</span>
										</span>
									</div>
									<!-- 白名单内容框 -->
									<div class="box2-content clearfix" style="height:150px;">
										<div class="memberInput <s:if test='%{sendCondInfo.memberKbn_w =="1"}'></s:if><s:else>hide</s:else>">
											<s:iterator  value="sendCondInfo.memberList_w" id="memberList_w">
												<li>
													<%--<input type="checkbox" checked="checked" onclick="binolssprm7302.delImpCouponMemberDetail('1','1',0,this);return false;"/>--%>
													<input type="hidden" class="sendCondMember_w" value="<s:property value='#memberList_w.mobile'/>"/>
													<span><s:property value="#memberList_w.mobile"/>&nbsp;<s:property value="#memberList_w.memberCode"/></span>
												</li>
											</s:iterator>
										</div>
										<div class="memberLevel <s:if test='%{sendCondInfo.memberKbn_w =="2"}'></s:if><s:else>hide</s:else>">
											<s:iterator  value="sendCondInfo.memLevel_w" id="memLevel">
												<li>
													<input type="checkbox" name="memLevel" <s:if test='%{#memLevel.flag=="1"}'>checked="checked"</s:if>/>
													<input type="hidden" name="memLevel" class="sendCondMember_w" value="<s:property value='#memLevel.levelId'/>"/>
													<span><s:property value="#memLevel.levelName"/></span></br>
												</li>
											</s:iterator>
										</div>
									</div>
								</div>
							</div>
							<div class="left memberBlackDiv" style="width: 50%;" id="sendMemberBlackDiv">
								<div class="box2 box2-active" style="display:inline;height:150px;">
									<div class="box2-header clearfix" >
										<strong class="left active"><span class="ui-icon icon-flag"></span>排除对象</strong>
										<div class="right">
											<a class="add" onclick="binolssprm7302.popMemberLoadDialog('1','2',0,this);return false">
												<span class="ui-icon icon-add"></span><span class="button-text">导入排除对象</span>
											</a>
										</div>
									</div>
									<!-- 黑名单内容框 -->
									<div class="box2-content clearfix memberInput" style="height:150px;" >
										<s:iterator value="sendCondInfo.memberList_b" id="memberList_b">
											<li>
												<%--<input type="checkbox" checked="checked" onclick="binolssprm7302.delImpCouponMemberDetail('1','2',0,this);return false;"/>--%>
												<input type="hidden" class="sendCondMember_b" value="<s:property value='#memberList_b.mobile'/>"/>
												<span><s:property value="#memberList_b.mobile"/>&nbsp;<s:property value="#memberList_b.memberCode"/></span>
											</li>
										</s:iterator>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				<h3>券内容</h3>
				<div id="contentDiv" style="min-height: 500px;">
					券类型&nbsp;&nbsp;
					<select name="couponRule.couponFlag" style="width:100px;" onchange="binolssprm7302.changeCouponType(this)" id="couponFlag">
							<%-- s:if test='%{couponRule.couponFlag != null && couponRule.couponFlag != ""}'> disabled="disabled" </s:if --%>
						<option <s:if test='%{couponRule.couponFlag == "1"}'>selected</s:if> value='1'>代金券</option>
						<option <s:if test='%{couponRule.couponFlag == "5"}'>selected</s:if> value='5'>折扣券</option>
						<option <s:if test='%{couponRule.couponFlag == "2"}'>selected</s:if> value='2'>代物券</option>
						<option <s:if test='%{couponRule.couponFlag == "3"}'>selected</s:if> value='3'>资格券</option>
						<option <s:if test='%{couponRule.couponFlag == "9"}'>selected</s:if> value='9'>券包</option>
					</select>
					<span class="sendCondType" <s:if test='%{couponRule.couponFlag != "9"}'>class="hide"</s:if>>
							<span>
								<a class="add" onClick="binolssprm7302.popPackageDialog(this,2);return false;" style="margin-left:30px;">
									<span class="ui-icon icon-add"></span><span class="button-text">添加券</span>
								</a>
							</span>
							<span  class="useCondType hide">
								<input type="checkbox" id ="useCondTypeCheckbox" <s:if test='%{bigContentMap.isSameFlag == "1"}'>checked="checked"</s:if> >以下所有使用门槛相同
							</span>
							<span class="useCondButtonAll <s:if test='%{bigContentMap.isSameFlag != "1"}'>hide</s:if> > ">
								<a class="add"  style="margin-left:30px;" onclick="binolssprm7302.popUseContionDialog(this,0);return false;">
									<span class="ui-icon icon-add"></span><span class="button-text">使用门槛设置</span>
								</a>
							    <input name="contentNo" type="hidden" value="-1"/>
								<input type="hidden" name="useCondJson" class="useCondJson hide" value="<s:property value='bigContentMap.useCondJson'/>"/> <%-- 存储使用门槛系列JSON字符串 --%>
							</span>
					</span>
					<div id="coupon-content">
					<!-- 当无设置券时默认显示代金券 -->
						<s:if test="bigContentMap.contentList == null || bigContentMap.contentList.size() == 0">
						<div class="box2-content  clearfix rule_content">
							<div style="width:12%;float:left;" onclick="binolssprm7302.contentDivShow(this);return false;">
								<div class="ui-icon icon-arrow-crm" ></div>
								<label class="bg_title">代金券</label>
							</div>
							<div style="width:70%;float:left;">
								<input type="hidden" name ="couponType" value="1">抵扣金额&nbsp;
								<input type="text" name ="faceValue" class="number" maxlength="10" onkeyup="this.value=this.value.replace(/\D/g,'')" onblur="this.value=this.value.replace(/\D/g,'')"/> 元
							</div>
							<div class="useCondButton" style="width:18%;float:right;<s:if test='%{couponRule.couponFlag == "9"}'>display:none;</s:if>">
								<a class="add"  style="margin-left:30px;" onclick="binolssprm7302.popUseContionDialog(this,1);return false;">
									<span class="ui-icon icon-add"></span><span class="button-text">使用门槛设置</span>
								</a>
								<input type="hidden" name ="contentNo" value="1" >
								<input type="hidden" name="useCondJson" class="useCondJson hide" value=""/> <%-- 存储使用门槛系列JSON字符串 --%>
							</div>
							<span onclick="binolssprm7302.delCoupon(this)" class="icon_del right" <s:if test='%{couponRule.couponFlag != "9"}'>style="display:none;"</s:if>> </span>
						</div>
						</s:if>
						<s:else>
							<s:iterator value="bigContentMap.contentList" id="contentInfo" status="status">
								<s:if test='#contentInfo.couponType == "1"'>
									<!-- 1为代金券 -->
									<div class="box2-content  clearfix rule_content">
										<div style="width:12%;float:left;" onclick="binolssprm7302.contentDivShow(this);return false;">
											<div class="ui-icon icon-arrow-crm" ></div>
											<label class="bg_title">代金券</label>
										</div>
										<div style="width:70%;float:left;">
											抵扣金额&nbsp;
											<input type="text" name ="faceValue" class="number" maxlength="10" value="<s:property value='#contentInfo.faceValue'/>" onkeyup="this.value=this.value.replace(/\D/g,'')" onblur="this.value=this.value.replace(/\D/g,'')"/> 元
											<input type="hidden" name ="couponType" value="<s:property value='#contentInfo.couponType'/>">
											<input type="hidden" name ="prmVendorId" value="<s:property value='#contentInfo.prmVendorId'/>">
											<input type="hidden" name ="unitCode" value="<s:property value='#contentInfo.unitCode'/>">
											<input type="hidden" name ="barCode" value="<s:property value='#contentInfo.barCode'/>">
										</div>
										<div class="useCondButton" style="width:18%;float:right;<s:if test='%{couponRule.couponFlag == "9" && bigContentMap.isSameFlag == "1"}'>display:none;</s:if>">
											<a class="add"  style="margin-left:30px;" onclick="binolssprm7302.popUseContionDialog(this,1);return false;">
												<span class="ui-icon icon-add"></span><span class="button-text">使用门槛设置</span>
											</a>
											<input type="hidden" name ="contentNo" value="<s:property value='#contentInfo.contentNo'/>" />
											<input type="hidden" name="useCondJson" class="useCondJson hide" value="<s:property value='#contentInfo.useCondJson'/>"/> <%-- 存储使用门槛系列JSON字符串 --%>
										</div>
										<span onclick="binolssprm7302.delCoupon(this)" class="icon_del right" <s:if test='%{couponRule.couponFlag != "9"}'>style="display:none;"</s:if>></span>
									</div>
								</s:if>
								<s:elseif test='#contentInfo.couponType == "5"'>
									<div class="box2-content  clearfix rule_content">
										<div style="width:12%;float:left;" onclick="binolssprm7302.contentDivShow(this);return false;">
											<div class="ui-icon icon-arrow-crm" ></div>
											<label class="bg_title">折扣券</label>
										</div>
										<div style="width:70%;float:left;">
											<input type="hidden" name ="couponType" value="5"/>
											<select name="zkType" class="left" style="width:100px;" onchange="binolssprm7302.changeZk('zkType', this)">
												<option <s:if test='%{#contentInfo.zkType == "0"}'>selected</s:if> value='0'>整单折扣</option>
												<option <s:if test='%{#contentInfo.zkType == "1"}'>selected</s:if> value='1'>单品折扣</option>
											</select>
								<span id="zkType0" <s:if test='#contentInfo.zkType != null && #contentInfo.zkType != "0"'> class="hide" </s:if>>
									折扣率&nbsp;<input type="text" id="zkValue" name ="zkValue" class="number zkCoupon" maxlength="3" value="<s:property value='#contentInfo.zkValue'/>" /> %
									折扣金额上限&nbsp;<input type="text" name ="zkAmountLimt" class="number" maxlength="8" value="<s:property value='#contentInfo.zkAmountLimt'/>"/>元
								</span>
								<span id="zkType1" <s:if test='#contentInfo.zkType == null || #contentInfo.zkType != "1"'> class="hide" </s:if>>
									折扣率&nbsp;
									<input type="text" name ="zkValue2" id="zkValue2" class="number zkCoupon" maxlength="3" value="<s:property value='#contentInfo.zkValue2'/>" /> %
									折扣数量上限&nbsp;
									<input type="text" name ="zkNumLimt" class="number" maxlength="3" value="<s:property value='#contentInfo.zkNumLimt'/>"/>个  </br>
									<div class="box2 box2-active" style="display:inline;height:100px;">
										<div class="box2-header clearfix" style="display:inline" >
											<strong class="active" style="width:70px;"><span class="ui-icon icon-flag"></span>产品范围
											<span>
												<a class="add" onClick="binolssprm7302.openOneProDialog2(this);return false;">
													<span class="ui-icon icon-add"></span><span class="button-text">选择产品</span>
												</a>
											</span>
											</strong>
										</div>
										<div class="box2-content clearfix" style="height:100px; width: 65%">
											<!-- 产品在此处展示 -->
											<div id='proContent<s:property value="#contentInfo.contentNo"/>' class="proContent z-tbody">
												<s:iterator value="#contentInfo.zList" id="proInfo">
													<li>
														<input type="checkbox" checked="checked" name="discountTicket" onclick="binolssprm7302.removeLi(this);" value='<s:property value="#proInfo.prtVendorId"/>'/>
														<input type="hidden" name="prtVendorId" value='<s:property value="#proInfo.prtVendorId"/>'/>
														<input type="hidden" name="prtVendId" value='<s:property value="#proInfo.prtVendId"/>'/>
														<s:property value="#proInfo.unitCode"/>&nbsp;<s:property value="#proInfo.barCode"/>&nbsp;<s:property value="#proInfo.nameTotal"/>&nbsp;&nbsp;
													</li>
												</s:iterator>
											</div>
										</div>
									</div>
								</span>
											<input type="hidden" name ="prmVendorId" value="<s:property value='#contentInfo.prmVendorId'/>">
											<input type="hidden" name ="unitCode" value="<s:property value='#contentInfo.unitCode'/>">
											<input type="hidden" name ="barCode" value="<s:property value='#contentInfo.barCode'/>">

										</div>
										<div class="useCondButton" style="width:18%;float:right;<s:if test='%{couponRule.couponFlag == "9" && bigContentMap.isSameFlag == "1"}'>display:none;</s:if>">
											<a class="add"  style="margin-left:30px;" onclick="binolssprm7302.popUseContionDialog(this,1);return false;">
												<span class="ui-icon icon-add"></span><span class="button-text">使用门槛设置</span>
											</a>
											<input type="hidden" name ="contentNo" value="<s:property value='#contentInfo.contentNo'/>" />
											<input type="hidden" name="useCondJson" class="useCondJson hide" value="<s:property value='#contentInfo.useCondJson'/>" /> <%-- 存储使用门槛系列JSON字符串 --%>
										</div>
										<span onclick="binolssprm7302.delCoupon(this)" class="icon_del right" <s:if test='%{couponRule.couponFlag != "9"}'>style="display:none;"</s:if>></span>
									</div>
								</s:elseif>
								<s:elseif test='#contentInfo.couponType == "2"'>
									<div class="box2-content clearfix rule_content">
										<div style="width:12%;float:left;" onclick="binolssprm7302.contentDivShow(this);return false;">
											<div class="ui-icon icon-arrow-crm" ></div>
											<label class="bg_title">代物券</label>
										</div>
										<div style="width:70%;float:left;">
											<div class="left" style="width:70%;">
												<input type="hidden" name ="couponType" value="2"/>
												<div class="box2 box2-active" style="display:inline;height:100px;">
													<div class="box2-header clearfix" style="display:inline" >
														<strong class="left active"><span class="ui-icon icon-flag"></span>产品范围</strong>
											<span>
												<a class="add" onClick="binolssprm7302.openProDialog2('3',this);return false;">
													<span class="ui-icon icon-add"></span><span class="button-text">产品选择</span>
												</a>
											</span>
														&nbsp;产品之间的关系:
														<select id="fullFlag" name="fullFlag" style="width:50px;" onclick="binolssprm7302.changeFullFlag(this)">
															<option <s:if test='%{#contentInfo.fullFlag == "0"}'>selected</s:if> value="0">或者</option>
															<option <s:if test='%{#contentInfo.fullFlag == "1"}'>selected</s:if> value="1">且</option>
														</select>
											<span id="chooseSpan" class="<s:if test='%{#contentInfo.fullFlag == "1"}'>hide</s:if>">
												以下任选<input type="text" class="number" name="maxCount" maxlength="2" value='<s:property value="#contentInfo.maxCount"/>'/>个
											</span>
													</div>
													<div class="box2-content clearfix" style="height:100px;overflow:auto">
														<!-- 产品在此处展示 -->
														<div id='contentProduct<s:property value="#contentInfo.contentNo"/>' class="contentProduct z-tbody">
															<s:iterator value="#contentInfo.zList" id="proInfo">
																<li>
																	<input type="checkbox" checked="checked" name="selPrtBlackChoose" onclick="binolssprm7302.removeLi(this);" value='<s:property value="#proInfo.prtVendorId"/>'/>
																	<input type="hidden" name="prtVendorId" value='<s:property value="#proInfo.prtVendorId"/>'/>
																	<input type="hidden" name="prtVendId" value='<s:property value="#proInfo.prtVendId"/>'/>
																	<s:property value="#proInfo.unitCode"/>&nbsp;<s:property value="#proInfo.barCode"/>&nbsp;<s:property value="#proInfo.nameTotal"/>&nbsp;&nbsp;
																	<input type="text" style="width: 20px;" name="proNum" value='<s:property value="#proInfo.proNum"/>' maxlength="2"/>
																</li>
															</s:iterator>
														</div>
													</div>
												</div>
											</div>
											<input type="hidden" name ="prmVendorId" value="<s:property value='#contentInfo.prmVendorId'/>">
											<input type="hidden" name ="unitCode" value="<s:property value='#contentInfo.unitCode'/>">
											<input type="hidden" name ="barCode" value="<s:property value='#contentInfo.barCode'/>">
										</div>
										<div class="useCondButton" style="width:18%;float:right;<s:if test='%{couponRule.couponFlag == "9" && bigContentMap.isSameFlag == "1"}'>display:none;</s:if>">
											<a class="add"  style="margin-left:30px;" onclick="binolssprm7302.popUseContionDialog(this,1);return false;">
												<span class="ui-icon icon-add"></span><span class="button-text">使用门槛设置</span>
											</a>
											<input type="hidden" name ="contentNo" value="<s:property value='#contentInfo.contentNo'/>" />
											<input type="hidden" name="useCondJson" class="useCondJson hide" value="<s:property value='#contentInfo.useCondJson'/>" /> <%-- 存储使用门槛系列JSON字符串 --%>
										</div>
										<span onclick="binolssprm7302.delCoupon(this)" class="icon_del right" <s:if test='%{couponRule.couponFlag != "9"}'>style="display:none;"</s:if>></span>
									</div>
								</s:elseif>
								<s:elseif test='#contentInfo.couponType == "3"'>
									<div class="box2-content clearfix rule_content">
										<div style="width:12%;float:left;" onclick="binolssprm7302.contentDivShow(this);return false;">
											<div class="ui-icon icon-arrow-crm" ></div>
											<label class="bg_title">资格券</label>
										</div>
										<div style="width:70%;float:left;">
											<input type="hidden" name ="couponType" value="3"/>
											<div style="width:100%;float:left;">
												<div class="left campDiv" style="width: 70%;">
													<div class="box2 box2-active" style="display:inline;height:100px;">
														<div class="box2-header clearfix" style="display:inline" >
															<strong class="left active"><span class="ui-icon icon-flag"></span>活动选择</strong>
															<div class="right">
																<a class="add" onClick="binolssprm7302.popCampDialog('1','1',this);return false;">
																	<span class="ui-icon icon-add"></span><span class="button-text">选择活动</span>
																</a>
															</div>
														</div>
														<div class="box2-content clearfix" style="height:100px;">
															<div class="hide campList z-tbody" id='contentCampaign<s:property value='#contentInfo.contentNo'/>' style="display: block;height:100px; overflow:auto">
																<s:iterator value="#contentInfo.zList" id="campInfo">
																	<li>
																		<input type="checkbox" name="campListWhiteChoose" checked="checked" onclick="binolssprm7302.removeLi(this);"/><s:property value="#campInfo.campaignCode"/>&nbsp;<s:property value="#campInfo.campName"/> &nbsp;
																		<s:if test='#campInfo.campaignMode == "0"'>
																			会员活动
																		</s:if>
																		<s:elseif test='#campInfo.campaignMode == "1"'>
																			促销活动
																		</s:elseif>
																		<s:elseif test='#campInfo.campaignMode == "2"'>
																			电子券活动
																		</s:elseif>
																		<input type="hidden" name="campaignCode" value='<s:property value="#campInfo.campaignCode"/>'/>
																		<input type="hidden" name="campaignMode" value="<s:property value='#campInfo.campaignMode'/>" />
																	</li>
																</s:iterator>
															</div>
														</div>
													</div>
												</div>
											</div>
											<input type="hidden" name ="prmVendorId" value="<s:property value='#contentInfo.prmVendorId'/>">
											<input type="hidden" name ="unitCode" value="<s:property value='#contentInfo.unitCode'/>">
											<input type="hidden" name ="barCode" value="<s:property value='#contentInfo.barCode'/>">
										</div>
										<div class="useCondButton" style="width:18%;float:right;<s:if test='%{couponRule.couponFlag == "9" && bigContentMap.isSameFlag == "1"}'>display:none;</s:if>">
											<a class="add"  style="margin-left:30px;" onclick="binolssprm7302.popUseContionDialog(this,1);return false;">
												<span class="ui-icon icon-add"></span><span class="button-text">使用门槛设置</span>
											</a>
											<input type="hidden" name ="contentNo" value="<s:property value='#contentInfo.contentNo'/>" />
											<input type="hidden"  name="useCondJson" class="useCondJson hide" value="<s:property value='#contentInfo.useCondJson'/>" /> <%-- 存储使用门槛系列JSON字符串 --%>
										</div>
										<span onclick="binolssprm7302.delCoupon(this)" class="icon_del right" <s:if test='%{couponRule.couponFlag != "9"}'>style="display:none;"</s:if>></span>
									</div>
								</s:elseif>
							</s:iterator>
						</s:else>
					</div>
				</div>





</div>
</cherry:form>
			<div class="center clearfix" id="pageBrandButton" style="margin-top:50px;">
				<button class="save" type="submit" onclick="binolssprm7302.saveRule()">
					<span class="ui-icon icon-save"></span>
					<span class="button-text"><s:text name="global.page.save" /></span>
				</button>
				<button class="close" onclick="window.close();">
					<span class="ui-icon icon-close"></span>
					<span class="button-text"><s:text name="global.page.close" /></span>
				</button>
			</div>
		</div>
	</div>
</div>
<div id ="packageDialog" class="dialog hide">
	<table width="100%">
		<thead>
			<tr>
				<th>选择</th>
				<th>券类型</th>
				<th>数量</th>
			</tr>
		</thead>
		<tbody>
		<s:iterator value='#application.CodeTable.getCodes("1383")' id="item">
		<s:if test='#item.CodeKey != "4"'>
			<tr>
				<td><input type="checkbox" value='<s:property value="#item.CodeKey"/>'/></td>
				<td><s:property value="#item.Value"/></td>
				<td><input type="text" name="coupNum" class="number" value="1"></td>
			</tr>
		</s:if>
		</s:iterator>
		</tbody>
	</table>
</div>
</s:i18n>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/common/popDataTable.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/common/ajaxfileupload.js"></script>
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
<s:url id="save_url" action="BINOLSSPRM73_save"/>
<s:url id="importCounter_Url" action="BINOLSSPRM73_importCounter"/>
<s:url id="counterDialogInit_Url" action="BINOLSSPRM73_counterInit"/>
<s:url id="importMember_Url" action="BINOLSSPRM73_importMember"/>
<s:url id="memberDialogInit_Url" action="BINOLSSPRM73_memberInit"/>
<s:url action="BINOLSSPRM73_channel" namespace="/ss" id="searchChannelUrl"></s:url>
<s:url action="BINOLCM02_initCampaignDialog" namespace="/common" id="searchCampaignInit_Url"></s:url>
<div class="hide">
	<a id="saveUrl" href="${save_url}"></a>
	<a id="importCounterUrl" href="${importCounter_Url}"></a>
	<a id="counterDialogInitUrl" href="${counterDialogInit_Url}"></a>
	<a id="searchCampaignInitUrl" href="${searchCampaignInit_Url}"></a>
	<a id="importMemberUrl" href="${importMember_Url}"></a>
	<a id="memberDialogInitUrl" href="${memberDialogInit_Url}"></a>
	<div id="coupon-content1">
	  <div class="box2-content  clearfix rule_content">
		  <div style="width:12%;float:left;">
    	<div class="ui-icon icon-arrow-crm"></div><label class="bg_title">代金券
		</label></div>
		<div style="width:88%;float:left;">
			<input type="hidden" name ="couponType" value="1">
			抵扣金额&nbsp;<input type="text" name ="faceValue" class="number" maxlength="3" /> 元
		</div>
		 <span onclick="binolssprm7302.delCoupon(this)" class="icon_del right" style="display:none"></span>
		  </div>
    </div>
    <div id="coupon-content5">
	  <div class="box2-content  clearfix rule_content">
		  <div style="width:12%;float:left;">
    	<div class="ui-icon icon-arrow-crm"></div><label class="bg_title">折扣券
		</label></div>
		<div style="width:88%;float:left;">
			<input type="hidden" name ="couponType" value="5">
			<select name="zkType" class="left" style="width:100px;" onchange="binolssprm7302.changeZk('zkType', this)">
				<option value='0'>整单折扣</option>
				<option value='1'>单品折扣</option>
			</select>
			<span id="zkType0">
			折扣率&nbsp;<input type="text" name ="zkValue" class="number" maxlength="3" /> % 
			折扣金额上限&nbsp;<input type="text" name ="zkAmountLimt" class="number" maxlength="3" />元
			</span>
			<span id="zkType1" class="hide">
			折扣率&nbsp;<input type="text" name ="zkValue2" class="number" maxlength="3" /> % 
			折扣数量上限&nbsp;<input type="text" name ="zkNumLimt" class="number" maxlength="3" />个  </br>
				<span class="left" style="margin-top:10px"> 
				<a class="add" onClick="binolssprm7302.openOneProDialog(this);return false;">
						<span class="ui-icon icon-add"></span><span class="button-text">选择某个产品</span>
				</a>
				</span>
				<table style="margin-left: 15px;" class="left">
					<tbody id="proContent" class="z-tbody"></tbody>
				</table>
			</span>
		</div>
		<span onclick="binolssprm7302.delCoupon(this)" class="icon_del right" style="display:none"></span>
		  </div>
    </div>
    <div id="coupon-content2">
	  <div class="box2-content  clearfix rule_content">
		  <div style="width:12%;float:left;">
    	<div class="ui-icon icon-arrow-crm"></div><label class="bg_title">代物券
		</label></div>
		<div style="width:88%;float:left;">
			<input type="hidden" name ="couponType" value="2"/>
			<a class="add" onClick="binolssprm7302.openProDialog('3', this);return false;">
				<span class="ui-icon icon-add"></span><span class="button-text">选择某个产品</span>
			</a>
			产品之间的关系：
			<select id="fullFlag" name="fullFlag" style="width:40px;" onchange="binolssprm7302.changeFullFlag(this)">
				<option value='0'>或</option>
				<option value='1'>且</option>
			</select>
			<span id="chooseSpan">
			  以下产品任选
			 <input type="text" class="number" name="maxCount" value='1' maxlength="2" />
			 个
			 </span>
			<table cellspacing="0" cellpadding="0" style="width:100%;margin-top:15px;" id="proTable" class="hide">
				  <thead>
					<tr>
					  <th style="width:25%;"><s:text name="global.page.prtvendorcode" /></th>
					  <th style="width:25%;"><s:text name="global.page.barcode" /></th>
					  <th style="width:25%;"><s:text name="global.page.productname" /></th>
					  <th style="width:10%;">个数</th>
					  <th class="center" style="width:15%;"><s:text name="global.page.option" /></th>
					</tr>
				  </thead>
				  <tbody id="conProShowDiv" class='z-tbody'>
				  </tbody>
			</table>
		</div>
		<span onclick="binolssprm7302.delCoupon(this)" class="icon_del right" style="display:none"></span>
		</div>
    </div>
    <div id="coupon-content3">
	  <div class="box2-content  clearfix rule_content">
		  <div style="width:12%;float:left;">
    	<div class="ui-icon icon-arrow-crm"></div><label class="bg_title">资格券
		</label></div>
		<div style="width:88%;float:left;">
		<input type="hidden" name ="couponType" value="3">
			<span class="left" >
				<a class="add" onclick="binolssprm7302.popCampaignList('3', this);return false;">
				  <span class="ui-icon icon-search"></span>
				  <span class="button-text"><s:text name="global.page.Popselect" /></span>
			    </a>
			</span>
			<table class="all_clean" style="margin-left: 15px;">
					<tbody id="camp_tbody" class="z-tbody">
					</tbody>
			</table>
		</div>
		<span onclick="binolssprm7302.delCoupon(this)" class="icon_del right" style="display:none"></span>
		  </div>
    </div>
    <div id="coupon-content9">
	  <a class="add" onClick="binolssprm7302.popPackageDialog(this);return false;" style="margin-left:30px;margin-top:10px;">
		<span class="ui-icon icon-add"></span><span class="button-text">添加券</span>
	 </a>
    </div>
</div>
<s:i18n name="i18n.ss.BINOLSSPRM73">
<div class="main container clearfix">
	<div class="panel ui-corner-all">
		<div id="div_main">
		<cherry:form id="mainForm" method="post" class="inline"
							csrftoken="false">
			 <div class="panel-header">   
		        	<div class="clearfix">
        				<span class="breadcrumb left"><span class="ui-icon icon-breadcrumb"></span>优惠券规则管理 &gt;
		        		编辑规则
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
				                   <td><span>
				                   <input class="text" name ="couponRule.ruleCode" maxlength="25" value="<s:property value='couponRule.ruleCode'/>" disabled="disabled"/></span>
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
				                   <td><s:select list='#application.CodeTable.getCodes("1381")' listKey="CodeKey" listValue="Value" name="couponRule.validMode" cssStyle="width:auto;" 
				                   value="couponRule.validMode"></s:select></td>
				               	</tr>
				               	<tr>
				                   <th>券使用期限<span class="red">*</span></th>
				                   <td>
				                   <span>
				                   	<select name="useTimeType" class="left" style="width:100px;" onchange="binolssprm7302.changeSpan('useTimeSpan', this, 1)" id="useTimeType">
										<option <s:if test='%{useTimeType == "0"}'>selected</s:if> value='0'>指定日期</option>
										<option <s:if test='%{useTimeType == "1"}'>selected</s:if> value='1'>参考发券日期</option>
									</select>
				                   </span>
				                   <span id="useTimeSpan0"  <s:if test='%{useTimeType != null && useTimeType != "0"}'> class = "hide" </s:if> style="margin-left:10px;">
				                   	<input class="date" id="useStartTime" name="useStartTime" value="<s:property value='useTimeInfo.useStartTime'/>"/>
				                   	-
				                   	<input class="date" id="useEndTime" name="useEndTime" value="<s:property value='useTimeInfo.useEndTime'/>"/>
				                   </span>
				                   <span id="useTimeSpan1"  <s:if test='%{useTimeType == null || useTimeType != "1"}'> class = "hide" </s:if> style="margin-left:10px;">
				                  	后<input type="text" class="number" maxlength="2" name="afterDays" value="<s:property value='useTimeInfo.afterDays'/>" />天
				                  	&nbsp;有效期<input type="text" class="number" maxlength="2" name="validity" value="<s:property value='useTimeInfo.validity'/>" />
				                  	<select name="validityUnit" style="width:40px;" >
										<option <s:if test='%{useTimeInfo.validityUnit == "0"}'>selected</s:if> value='0'>天</option>
										<option <s:if test='%{useTimeInfo.validityUnit == "1"}'>selected</s:if> value='1'>月</option>
									</select>
				                   </span>
				                   </td>
				                   <th>可否转赠</th>
				                   <td><span>
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
				                   <th>总发券数量上限</th>
				                   <td><span><input type="text" name ="couponRule.sumQuantity" class="number" style="width: 60px;" maxlength="7" value="<s:property value='couponRule.sumQuantity'/>"/></span></td>
				                   <th>单个会员发券数量上限</th>
				                   <td><span><input type="text" name ="couponRule.limitQuantity" class="number" maxlength="3" value="<s:property value='couponRule.limitQuantity'/>"/></span></td>
				               	</tr>
				               	<tr>
				                   <th>一单发券上限<span class="red">*</span></th>
				                   <td><span><input type="text" name ="couponRule.quantity" class="number" style="width: 60px;" maxlength="2" value="<s:property value='couponRule.quantity'/>"/></span></td>
				                   <th></th>
				                   <td></td>
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
<div id="coupon-content">
    <s:if test="contentInfoList == null || contentInfoList.size() == 0">
	  <div class="box2-content  clearfix rule_content">
		  <div style="width:12%;float:left;">
    	<div class="ui-icon icon-arrow-crm"></div><label class="bg_title">代金券
		</label></div>
		<div style="width:88%;float:left;">
			<input type="hidden" name ="couponType" value="1">
			抵扣金额&nbsp;<input type="text" name ="faceValue" class="number" maxlength="3" /> 元
		</div>
		  </div>
	 </s:if>
	 <s:else>
	 <s:iterator value="contentInfoList" id="contentInfo" status="status">
	 	<s:if test='#contentInfo.couponType == "1"'>
	 		<div class="box2-content  clearfix rule_content">
		  <div style="width:12%;float:left;">
    	<div class="ui-icon icon-arrow-crm"></div><label class="bg_title">代金券
		</label></div>
		<div style="width:88%;float:left;">
				 <input type="hidden" name ="couponType" value="<s:property value='#contentInfo.couponType'/>">
				 <input type="hidden" name ="prmVendorId" value="<s:property value='#contentInfo.prmVendorId'/>">
				 <input type="hidden" name ="unitCode" value="<s:property value='#contentInfo.unitCode'/>">
				 <input type="hidden" name ="barCode" value="<s:property value='#contentInfo.barCode'/>">
				 <input type="hidden" name ="contentNo" value="<s:property value='#contentInfo.contentNo'/>">
					抵扣金额&nbsp;<input type="text" name ="faceValue" class="number" maxlength="3" value="<s:property value='#contentInfo.faceValue'/>"/> 元
		 </div>
		 <s:if test='%{couponRule.couponFlag == "9"}'><span onclick="binolssprm7302.delCoupon(this)" class="icon_del right"></span></s:if>
		 </div>
	 	</s:if>
	 	<s:elseif test='#contentInfo.couponType == "5"'>
	 	<div class="box2-content  clearfix rule_content">
		  <div style="width:12%;float:left;">
    	<div class="ui-icon icon-arrow-crm"></div><label class="bg_title">折扣券
		</label></div>
		<div style="width:88%;float:left;">
			<input type="hidden" name ="couponType" value="5">
			<input type="hidden" name ="prmVendorId" value="<s:property value='#contentInfo.prmVendorId'/>">
			<input type="hidden" name ="unitCode" value="<s:property value='#contentInfo.unitCode'/>">
			<input type="hidden" name ="barCode" value="<s:property value='#contentInfo.barCode'/>">
			<input type="hidden" name ="contentNo" value="<s:property value='#contentInfo.contentNo'/>">
			<select name="zkType" class="left" style="width:100px;" onchange="binolssprm7302.changeZk('zkType', this)">
				<option <s:if test='%{#contentInfo.zkType == "0"}'>selected</s:if> value='0'>整单折扣</option>
				<option <s:if test='%{#contentInfo.zkType == "1"}'>selected</s:if> value='1'>单品折扣</option>
			</select>
			<span id="zkType0" <s:if test='#contentInfo.zkType != null && #contentInfo.zkType != "0"'> class="hide" </s:if>>
			折扣率&nbsp;<input type="text" name ="zkValue" class="number" maxlength="3" value="<s:property value='#contentInfo.zkValue'/>" /> % 
			折扣金额上限&nbsp;<input type="text" name ="zkAmountLimt" class="number" maxlength="3" value="<s:property value='#contentInfo.zkAmountLimt'/>"/>元
			</span>
			<span id="zkType1" <s:if test='#contentInfo.zkType == null || #contentInfo.zkType != "1"'> class="hide" </s:if>>
			折扣率&nbsp;<input type="text" name ="zkValue2" class="number" maxlength="3" value="<s:property value='#contentInfo.zkValue2'/>" /> % 
			折扣数量上限&nbsp;<input type="text" name ="zkNumLimt" class="number" maxlength="3" value="<s:property value='#contentInfo.zkNumLimt'/>"/>个  </br>
				<span class="left" style="margin-top:10px"> 
				<a class="add" onClick="binolssprm7302.openOneProDialog(this);return false;">
						<span class="ui-icon icon-add"></span><span class="button-text">选择某个产品</span>
				</a>
				</span>
				<table style="margin-left: 15px;" class="left">
					<tbody id="proContent_<s:property value="#status.index"/>" 
					class="z-tbody">
					<s:iterator value="#contentInfo.zList" id="proInfo" status="status">
						<tr><td><span class="list_normal">
						<span class="text" style="line-height:19px;">
						<s:property value='#proInfo.nameTotal'/>(<s:property value='#proInfo.barCode'/>)
						</span>
						<span class="close" style="margin: 0 1px 2px 5px;" onclick="binolssprm7302.deleteHtml2(this);return false;"><span class="ui-icon ui-icon-close"></span></span>
						<input type="hidden" name="prtVendorId" value="<s:property value='#proInfo.prtVendorId'/>"/>
						<input type="hidden" name="prtVendId" value="<s:property value='#proInfo.prtVendId'/>"/>
						</span></td></tr>
					</s:iterator>
					</tbody>
				</table>
			</span>
		</div>
		<s:if test='%{couponRule.couponFlag == "9"}'><span onclick="binolssprm7302.delCoupon(this)" class="icon_del right"></span></s:if>
		  </div>
	 	</s:elseif>
	 	<s:elseif test='#contentInfo.couponType == "2"'>
	 	<div class="box2-content  clearfix rule_content">
		  <div style="width:12%;float:left;">
    	<div class="ui-icon icon-arrow-crm"></div><label class="bg_title">代物券
		</label></div>
		<div style="width:88%;float:left;">
			<input type="hidden" name ="couponType" value="2">
			<input type="hidden" name ="prmVendorId" value="<s:property value='#contentInfo.prmVendorId'/>">
			<input type="hidden" name ="unitCode" value="<s:property value='#contentInfo.unitCode'/>">
			<input type="hidden" name ="barCode" value="<s:property value='#contentInfo.barCode'/>">
			<input type="hidden" name ="contentNo" value="<s:property value='#contentInfo.contentNo'/>">
			<a class="add" onClick="binolssprm7302.openProDialog('3', this);return false;">
				<span class="ui-icon icon-add"></span><span class="button-text">选择某个产品</span>
			</a>
			产品之间的关系：
			<select id="fullFlag" name="fullFlag" style="width:40px;" onchange="binolssprm7302.changeFullFlag(this)">
				<option <s:if test='%{#contentInfo.fullFlag == "0"}'>selected</s:if> value='0'>或</option>
				<option <s:if test='%{#contentInfo.fullFlag == "1"}'>selected</s:if> value='1'>且</option>
			</select>
			<span id="chooseSpan" <s:if test='%{#contentInfo.fullFlag == "1"}'> class="hide" </s:if>>
			 &nbsp;&nbsp;以下产品任选
			 <s:if test='%{#contentInfo.maxCount != null && #contentInfo.maxCount != ""}'>
			 <input type="text" class="number" name="maxCount" value='<s:property value="#contentInfo.maxCount"/>' maxlength="2" />
			 </s:if>
			 <s:else>
			 <input type="text" class="number" name="maxCount" value='1' maxlength="2" />
			 </s:else>
			 个
			 </span>
			<table cellspacing="0" cellpadding="0" style="width:100%;margin-top:15px;" id="proTable"
			<s:if test='%{#contentInfo.zList == null || #contentInfo.zList.size() == 0}'> class="hide"</s:if>>
				  <thead>
					<tr>
					  <th style="width:25%;"><s:text name="global.page.prtvendorcode" /></th>
					  <th style="width:25%;"><s:text name="global.page.barcode" /></th>
					  <th style="width:25%;"><s:text name="global.page.productname" /></th>
					  <th style="width:10%;">个数</th>
					  <th class="center" style="width:15%;"><s:text name="global.page.option" /></th>
					</tr>
				  </thead>
				  <tbody id="conProShowDiv_<s:property value="#status.index"/>" class='z-tbody'>
				  <s:iterator value="#contentInfo.zList" id="proInfo">
				  	 	<tr>
				  	 	<td class="hide">
				  	 		<input type="hidden" name="prtVendorId" value='<s:property value="#proInfo.prtVendorId"/>'/>
				  	 		<input type="hidden" name='prtVendId' value='<s:property value="#proInfo.prtVendId"/>' />
				  	 	</td>
				  	 	<td style="width:25%;"><s:property value="#proInfo.unitCode"/></td>
				  	 	<td style="width:25%;"><s:property value="#proInfo.barCode"/></td>
				  	 	<td style="width:25%;"><s:property value="#proInfo.nameTotal"/></td>
				  	 	<td style="width:10%;">
				  	 		<input type="text" class="number" name="proNum" value='<s:property value="#proInfo.proNum"/>' maxlength="2" />
				  	 	</td>
				  	 	<td class="center" style="width:15%;"><a href="javascript:void(0);" onClick="binolssprm7302.deleteHtml(this);return false;">删除</a></td>
				  	 	</tr>
 					</s:iterator>
				  </tbody>
			</table>
		</div>
		<s:if test='%{couponRule.couponFlag == "9"}'><span onclick="binolssprm7302.delCoupon(this)" class="icon_del right"></span></s:if>
		</div>
	 	</s:elseif>
	 	<s:elseif test='#contentInfo.couponType == "3"'>
	 		<div class="box2-content  clearfix rule_content">
		  <div style="width:12%;float:left;">
    	<div class="ui-icon icon-arrow-crm"></div><label class="bg_title">资格券
		</label></div>
		<div style="width:88%;float:left;">
		<input type="hidden" name ="couponType" value="3">
		<input type="hidden" name ="prmVendorId" value="<s:property value='#contentInfo.prmVendorId'/>">
			<input type="hidden" name ="unitCode" value="<s:property value='#contentInfo.unitCode'/>">
			<input type="hidden" name ="barCode" value="<s:property value='#contentInfo.barCode'/>">
			<input type="hidden" name ="contentNo" value="<s:property value='#contentInfo.contentNo'/>">
			<span class="left" >
				<a class="add" onclick="binolssprm7302.popCampaignList('3', this);return false;">
				  <span class="ui-icon icon-search"></span>
				  <span class="button-text"><s:text name="global.page.Popselect" /></span>
			    </a>
			</span>
			<table class="all_clean" style="margin-left: 15px;">
					<tbody id="camp_tbody" class="z-tbody">
						<s:iterator value="#contentInfo.zList" id="campInfo">
							<tr style="float:left;margin-left:10px;">
							<td>
								<span class="list_normal">
								<span class="text" style="line-height:19px;"><s:property value="#campInfo.campName"/></span>
								<span class="close" style="margin: 0 1px 2px 5px;" onclick="binolssprm7302.delCampaignHtml(this);return false;"><span class="ui-icon ui-icon-close"></span></span>
								<input type="hidden" name="campaignMode" value="<s:property value='#campInfo.campaignMode'/>" />
								<input type="hidden" name="campaignCode" value="<s:property value='#campInfo.campaignCode'/>" />
								</span>
							</td>
							</tr>
						</s:iterator>
					</tbody>
			</table>
		</div>
		<s:if test='%{couponRule.couponFlag == "9"}'><span onclick="binolssprm7302.delCoupon(this)" class="icon_del right"></span></s:if>
		  </div>
	 	</s:elseif>
	 </s:iterator>
	 </s:else>
    </div>
  </div>
  <h3>发送门槛</h3>
  <div style="min-height: 500px;" id="sendCondition">
    	 <div class="box4-content  clearfix">
		  <div style="width:12%;float:left;">
    	<div class="ui-icon icon-arrow-crm"></div><label class="bg_title">购买门店
		</label></div>
		<div style="width:88%;float:left;">
			<select name="counterKbn" style="width:100px;" onchange="binolssprm7302.changeSpan('counterKbn', this);" class="z-param">
				<option <s:if test='%{sendCondInfo.counterKbn == "0"}'>selected</s:if> value='0'>请选择</option>
				<option <s:if test='%{sendCondInfo.counterKbn == "1"}'>selected</s:if> value='1'>导入门店</option>
				<option <s:if test='%{useCondInfo.counterKbn == "2"}'>selected</s:if> value='2'>渠道选择</option>
			</select>
		</span>
		<div id="counterKbn1" <s:if test='%{sendCondInfo.counterKbn == null || sendCondInfo.counterKbn != "1"}'> class="hide" </s:if> >
		    <input class="input_text" type="text" id="counterPathExcel" name="counterPathExcel"/>
		    <input type="button" value="<s:text name="global.page.browse"/>"/>
		    <input class="input_file" type="file" name="upExcel" id="counterUpExcel" size="33" onchange="counterPathExcel.value=this.value;return false;" /> 
		    <input type="button" value="导入" onclick="binolssprm7302.counterUpload('1')"/>
  				<img id="counterloading" src="/Cherry/css/cherry/img/loading.gif" height="15px" style="display:none;">
  				<s:radio name="upMode" list="#application.CodeTable.getCodes('1398')" 
	                    		listKey="CodeKey" listValue="Value" value="1" id="counterUpMode"></s:radio>
  				<br/>
  				<a href="/Cherry/download/门店导入模板(券活动).xls">下载模板</a>
  				<a href="javascript:void(0)" style="margin-left:50px;" onclick="binolssprm7302.counterDialog('1')">导入结果一览</a>
		</div>
		<div id="counterKbn2" <s:if test='%{sendCondInfo.counterKbn == null || sendCondInfo.counterKbn != "2"}'> class="hide" </s:if> >
			<input type="hidden" id="sendChannelId" name="sendChannelId" class="z-param" value="<s:property value='sendCondInfo.sendChannelId'/>"/>
			<input type="hidden" id="sendMemCounterId" name="sendMemCounterId" class="z-param" value="<s:property value='sendCondInfo.sendMemCounterId'/>"/>
		    <span id="sendThresholdDiv" style="line-height: 18px;"></span>
			<a class="add" style="float: none;" onclick="binolssprm7302.searchChannel('${searchChannelUrl}',0);return false;"><span class="ui-icon icon-search"></span><span class="button-text"><s:text name="global.page.Popselect"></s:text></span></a>
			<div id="sendThresholdShow" <s:if test='%{(sendCondInfo.sendChannelId == null || sendCondInfo.sendChannelId == "") && (sendCondInfo.sendMemCounterId == null || sendCondInfo.sendMemCounterId == "")}'> class="hide" </s:if>>
				<br/><label class="gray"> 已选择的门店请点击上面按钮查看 </label>
			</div>
		</div>
		  </div>
		  </div>
		  
		 <div class="box4-content  clearfix">
		  <div style="width:12%;float:left;">
    	<div class="ui-icon icon-arrow-crm"></div><label class="bg_title">购买金额
		</label></div>
		<div style="width:88%;float:left;">
			金额条件:<select name="amountCondition" class="z-param" style="width:80px;">
				<option <s:if test='%{sendCondInfo.amountCondition == "0"}'>selected</s:if> value="0">实付金额</option>
				<option <s:if test='%{sendCondInfo.amountCondition == "1"}'>selected</s:if> value="1">正价金额</option>
			</select>
			满 <input type="text" name ="sendMinAmount" class="number z-param" style="width:60px;" maxlength="9" value="<s:property value='sendCondInfo.sendMinAmount'/>"/> 元
		</div>
		  </div>
		  <div class="box4-content  clearfix">
		  <div style="width:12%;float:left;">
    	<div class="ui-icon icon-arrow-crm"></div><label class="bg_title">产品范围
		</label></div>
		<div style="width:88%;float:left;">
				<a class="add" onClick="binolssprm7302.openProDialog('1');return false;">
					<span class="ui-icon icon-add"></span><span class="button-text">选择某个产品</span>
				</a>
				<a class="add" onClick="binolssprm7302.openProTypeDialog('1');return false;">
					<span class="ui-icon icon-add"></span><span class="button-text"><s:text name="global.page.selProCat" /></span>
				</a>
				&nbsp;&nbsp;
				<s:text name="global.page.relation" />
			   		<input id="relation2" type="radio" value="2" name="relation" class="z-param" <s:if test='%{sendCondInfo.relation == null || sendCondInfo.relation == "2"}'>checked="checked"</s:if>>
					<label for="relation2"><s:text name="global.page.relation2" /></label>
			   		<input id="relation1" type="radio" value="1" name="relation" class="z-param" <s:if test='%{sendCondInfo.relation == "1"}'>checked="checked"</s:if>>
					<label for="relation1"><s:text name="global.page.relation1" /></label>
				<table cellspacing="0" cellpadding="0" style="width:100%;margin-top:15px;" id="proTable" 
				<s:if test='%{sendCondInfo.proList == null || sendCondInfo.proList.size() == 0}'> class="hide"</s:if> >
				  <thead>
					<tr>
					  <th style="width:25%;"><s:text name="global.page.prtvendorcode" /></th>
					  <th style="width:25%;"><s:text name="global.page.barcode" /></th>
					  <th style="width:25%;"><s:text name="global.page.productname" /></th>
					  <th style="width:10%;">个数</th>
					  <th class="center" style="width:15%;"><s:text name="global.page.option" /></th>
					</tr>
				  </thead>
				  <tbody id="proShowDiv">
				  	 <s:iterator value="sendCondInfo.proList" id="proInfo">
				  	 	<tr>
				  	 	<td class="hide">
				  	 		<input type="hidden" name="prtVendorId" value='<s:property value="#proInfo.prtVendorId"/>'/>
				  	 		<input type="hidden" name='prtVendId' value='<s:property value="#proInfo.prtVendId"/>' />
				  	 	</td>
				  	 	<td style="width:25%;"><s:property value="#proInfo.unitCode"/></td>
				  	 	<td style="width:25%;"><s:property value="#proInfo.barCode"/></td>
				  	 	<td style="width:25%;"><s:property value="#proInfo.nameTotal"/></td>
				  	 	<td style="width:10%;">
				  	 		<input type="text" class="number" name="proNum" value='<s:property value="#proInfo.proNum"/>' maxlength="2" />
				  	 	</td>
				  	 	<td class="center" style="width:15%;"><a href="javascript:void(0);" onClick="binolssprm7302.deleteHtml(this);return false;">删除</a></td>
				  	 	</tr>
 					</s:iterator>
				  </tbody>
				</table>
				<table cellspacing="0" cellpadding="0" style="width:100%;margin-top:15px;" id="proTypeTable" 
				<s:if test='%{sendCondInfo.proTypeList == null || sendCondInfo.proTypeList.size() == 0}'> class="hide" </s:if>>
				  <thead>
					<tr>
					  <th style="width:35%;"><s:text name="global.page.cateVal" /></th>
					  <th style="width:40%;"><s:text name="global.page.cateValName" /></th>
					  <th style="width:10%;">个数</th>
					  <th class="center" style="width:15%;"><s:text name="global.page.option" /></th>
					</tr>
				  </thead>
				  <tbody id="proTypeShowDiv">
				  	<s:iterator value="sendCondInfo.proTypeList" id="proTypeInfo">
				  		<tr>
				  	 	<td class="hide">
				  	 		<input type="hidden" name="cateValId" value='<s:property value="#proTypeInfo.cateValId"/>'/>
				  	 		<input type="hidden" name='cateId' value='<s:property value="#proTypeInfo.cateId"/>' />
				  	 	</td>
				  	 	<td style="width:25%;"><s:property value="#proTypeInfo.cateVal"/></td>
				  	 	<td style="width:40%;"><s:property value="#proTypeInfo.cateValName"/></td>
				  	 	<td style="width:10%;">
				  	 		<input type="text" class="number" name="cateNum" value='<s:property value="#proTypeInfo.cateNum"/>' maxlength="2" />
				  	 	</td>
				  	 	<td class="center" style="width:15%;"><a href="javascript:void(0);" onClick="binolssprm7302.deleteHtml(this);return false;">删除</a></td>
				  	 	</tr>
				  	</s:iterator>
				  </tbody>
				</table>
		</div>
		  </div>
		 <div class="box4-content  clearfix">
		  <div style="width:12%;float:left;">
    	<div class="ui-icon icon-arrow-crm"></div><label class="bg_title">需参活动
		</label></div>
		<div style="width:88%;float:left;">
				<span class="left" >
                <a class="add" onclick="binolssprm7302.popCampaignList('1');return false;">
				  <span class="ui-icon icon-search"></span>
				  <span class="button-text"><s:text name="global.page.Popselect" /></span>
			    </a>
			    </span>
			    <table class="all_clean" style="margin-left: 15px;">
					<tbody id="camp_tbody">
						<s:iterator value="sendCondInfo.campList" id="campInfo">
							<tr style="float:left;margin-left:10px;">
							<td>
								<span class="list_normal">
								<span class="text" style="line-height:19px;"><s:property value="#campInfo.campName"/></span>
								<span class="close" style="margin: 0 1px 2px 5px;" onclick="binolssprm7302.delCampaignHtml(this);return false;"><span class="ui-icon ui-icon-close"></span></span>
								<input type="hidden" name="campaignMode" value="<s:property value='#campInfo.campaignMode'/>" />
								<input type="hidden" name="campaignCode" value="<s:property value='#campInfo.campaignCode'/>" />
								</span>
							</td>
							</tr>
						</s:iterator>
					</tbody>
				</table>
		</div>
		  </div>
		  <div class="box4-content  clearfix">
		  <div style="width:12%;float:left;">
    	<div class="ui-icon icon-arrow-crm"></div><label class="bg_title">发券对象
		</label></div>
		<div style="width:88%;float:left;">
			<select name="memberKbn" style="width:100px;" onchange="binolssprm7302.changeSpan('memberKbn', this);" class="z-param">
				<option <s:if test='%{sendCondInfo.memberKbn == "0"}'>selected</s:if> value='0'>请选择</option>
				<option <s:if test='%{sendCondInfo.memberKbn == "2"}'>selected</s:if> value='2'>会员等级</option>
				<option <s:if test='%{sendCondInfo.memberKbn == "1"}'>selected</s:if> value='1'>导入会员</option>
				<option <s:if test='%{sendCondInfo.memberKbn == "3"}'>selected</s:if> value='3'>仅限非会员</option>
			</select>
		</span>
		<div id="memberKbn2" style="margin-top:10px;" <s:if test='%{sendCondInfo.memberKbn == null || sendCondInfo.memberKbn != "2"}'> class="hide" </s:if> >
		    <s:iterator value="sendCondInfo.levelList" id="levelInfo" status="status">
		    	<input type="checkbox" name="memLevel" id='memLevel_<s:property value="#status.index"/>' 
		    	<s:if test='%{#levelInfo.checkFlag == "1"}'>checked="checked"</s:if>
		    	value='<s:property value="#levelInfo.levelId"/>'/>
		    	<label for='memLevel_<s:property value="#status.index"/>' style="cursor:pointer;margin-right:20px;"><s:property value="#levelInfo.levelName"/> </label>
		    </s:iterator>
		</div>
		<div id="memberKbn1" <s:if test='%{sendCondInfo.memberKbn == null || sendCondInfo.memberKbn != "1"}'> class="hide" </s:if> >
		    <input class="input_text" type="text" id="memberPathExcel" name="memberPathExcel"/>
		    <input type="button" value="<s:text name="global.page.browse"/>"/>
		    <input class="input_file" type="file" name="upExcel" id="memberUpExcel" size="33" onchange="memberPathExcel.value=this.value;return false;" /> 
		    <input type="button" value="导入" onclick="binolssprm7302.memberUpload('1')"/>
  				<img id="memberloading" src="/Cherry/css/cherry/img/loading.gif" height="15px" style="display:none;">
  				<s:radio name="memberUpMode" list="#application.CodeTable.getCodes('1398')" 
	                    		listKey="CodeKey" listValue="Value" value="1" id="memberUpMode"></s:radio>
  				<br/>
  				<a href="/Cherry/download/会员导入模板(券活动).xls">下载模板</a>
  				<a href="javascript:void(0)" style="margin-left:50px;" onclick="binolssprm7302.memberDialog('1')">导入结果一览</a>
		</div>
		  </div>
		  </div>
		  
		  <div class="box4-content  clearfix">
		  <div style="width:12%;float:left;">
    	<div class="ui-icon icon-arrow-crm"></div><label class="bg_title">其它限制
		</label></div>
		<div style="width:88%;float:left;">
			订单中已使用券的情况下，是否发券？
			<select name="otherCond" style="width:50px;"  class="z-param">
				<option <s:if test='%{sendCondInfo.otherCond == "0"}'>selected</s:if> value='0'>否</option>
				<option <s:if test='%{sendCondInfo.otherCond == "1"}'>selected</s:if> value='1'>是</option>
			</select>
		  </div>
		  </div>
  </div>
  <h3>使用门槛</h3>
 <div style="min-height: 500px;" id="useCondition">
    	 <div class="box4-content  clearfix">
		  <div style="width:12%;float:left;">
    	<div class="ui-icon icon-arrow-crm"></div><label class="bg_title">购买门店
		</label></div>
		<div style="width:88%;float:left;">
			<select name="counterKbn" style="width:100px;" onchange="binolssprm7302.changeSpan('useCounterKbn', this);" class="z-param">
				<option <s:if test='%{useCondInfo.counterKbn == "0"}'>selected</s:if> value='0'>请选择</option>
				<option <s:if test='%{useCondInfo.counterKbn == "1"}'>selected</s:if> value='1'>导入门店</option>
				<option <s:if test='%{useCondInfo.counterKbn == "2"}'>selected</s:if> value='2'>渠道选择</option>
			</select>
		</span>
		<div id="useCounterKbn1" <s:if test='%{useCondInfo.counterKbn == null || useCondInfo.counterKbn != "1"}'> class="hide" </s:if> >
		    <input class="input_text" type="text" id="useCounterPathExcel" name="useCounterPathExcel"/>
		    <input type="button" value="<s:text name="global.page.browse"/>"/>
		    <input class="input_file" type="file" name="upExcel" id="counterUpExcelUse" size="33" onchange="useCounterPathExcel.value=this.value;return false;" /> 
		    <input type="button" value="导入" onclick="binolssprm7302.counterUpload('2')"/>
  				<img id="counterloading" src="/Cherry/css/cherry/img/loading.gif" height="15px" style="display:none;">
  				<s:radio name="upModeUse" list="#application.CodeTable.getCodes('1398')" 
	                    		listKey="CodeKey" listValue="Value" value="1" id="upModeUse"></s:radio>
  				<br/>
  				<a href="/Cherry/download/门店导入模板(券活动).xls">下载模板</a>
  				<a href="javascript:void(0)" style="margin-left:50px;" onclick="binolssprm7302.counterDialog('2')">导入结果一览</a>
		</div>
		<div id="useCounterKbn2" <s:if test='%{useCondInfo.counterKbn == null || useCondInfo.counterKbn != "2"}'>  class="hide" </s:if> >
		    	<input type="hidden" id="useChannelId" name="useChannelId" class="z-param" value="<s:property value='useCondInfo.useChannelId'/>"/>
				<input type="hidden" id="useMemCounterId" name="useMemCounterId" class="z-param" value="<s:property value='useCondInfo.useMemCounterId'/>"/>
			    <span id="useThresholdDiv" style="line-height: 18px;"></span>
			    <a class="add" style="float: none;" onclick="binolssprm7302.searchChannel('${searchChannelUrl}',1);return false;"><span class="ui-icon icon-search"></span><span class="button-text"><s:text name="global.page.Popselect"></s:text></span></a>
			    <div id="useThresholdShow" <s:if test='%{(useCondInfo.useChannelId == null || useCondInfo.counterKbn == "") && (useCondInfo.useMemCounterId == null || useCondInfo.useMemCounterId == "")}'>  class="hide" </s:if>>
			    	<br/><label class="gray"> 已选择的门店请点击上面按钮查看 </label>
			    </div>
		</div>
		  </div>
		  </div>
		  
		 <div class="box4-content  clearfix">
		  <div style="width:12%;float:left;">
    	<div class="ui-icon icon-arrow-crm"></div><label class="bg_title">购买金额
		</label></div>
		<div style="width:88%;float:left;">
			满 <input type="text" name ="useMinAmount" class="number z-param" style="width:60px;" maxlength="9" value="<s:property value='useCondInfo.useMinAmount'/>"/> 元
		</div>
		  </div>
		  <div class="box4-content  clearfix">
		  <div style="width:12%;float:left;">
    	<div class="ui-icon icon-arrow-crm"></div><label class="bg_title">产品范围
		</label></div>
		<div style="width:88%;float:left;">
				<a class="add" onClick="binolssprm7302.openProDialog('2');return false;">
					<span class="ui-icon icon-add"></span><span class="button-text">选择某个产品</span>
				</a>
				<a class="add" onClick="binolssprm7302.openProTypeDialog('2');return false;">
					<span class="ui-icon icon-add"></span><span class="button-text"><s:text name="global.page.selProCat" /></span>
				</a>
				&nbsp;&nbsp;
				<s:text name="global.page.relation" />
			   		<input id="relationUse2" type="radio" value="2" name="relationUse" class="z-param" <s:if test='%{useCondInfo.relationUse == null || useCondInfo.relationUse == "2"}'>checked="checked"</s:if>>
					<label for="relationUse2"><s:text name="global.page.relation2" /></label>
			   		<input id="relationUse1" type="radio" value="1" name="relationUse" class="z-param" <s:if test='%{useCondInfo.relationUse == "1"}'>checked="checked"</s:if>>
					<label for="relationUse1"><s:text name="global.page.relation1" /></label>
				<table cellspacing="0" cellpadding="0" style="width:100%;margin-top:15px;" id="proTable" 
				<s:if test='%{useCondInfo.proList == null || useCondInfo.proList.size() == 0}'>class="hide"</s:if>>
				  <thead>
					<tr>
					  <th style="width:25%;"><s:text name="global.page.prtvendorcode" /></th>
					  <th style="width:25%;"><s:text name="global.page.barcode" /></th>
					  <th style="width:25%;"><s:text name="global.page.productname" /></th>
					  <th style="width:10%;">个数</th>
					  <th class="center" style="width:15%;"><s:text name="global.page.option" /></th>
					</tr>
				  </thead>
				  <tbody id="useProShowDiv">
				  	 <s:iterator value="useCondInfo.proList" id="proInfo">
				  	 	<tr>
				  	 	<td class="hide">
				  	 		<input type="hidden" name="prtVendorId" value='<s:property value="#proInfo.prtVendorId"/>'/>
				  	 		<input type="hidden" name='prtVendId' value='<s:property value="#proInfo.prtVendId"/>' />
				  	 	</td>
				  	 	<td style="width:25%;"><s:property value="#proInfo.unitCode"/></td>
				  	 	<td style="width:25%;"><s:property value="#proInfo.barCode"/></td>
				  	 	<td style="width:25%;"><s:property value="#proInfo.nameTotal"/></td>
				  	 	<td style="width:10%;">
				  	 		<input type="text" class="number" name="proNum" value='<s:property value="#proInfo.proNum"/>' maxlength="2" />
				  	 	</td>
				  	 	<td class="center" style="width:15%;"><a href="javascript:void(0);" onClick="binolssprm7302.deleteHtml(this);return false;">删除</a></td>
				  	 	</tr>
 					</s:iterator>
				  </tbody>
				</table>
				<table cellspacing="0" cellpadding="0" style="width:100%;margin-top:15px;" id="proTypeTable" 
				<s:if test='%{useCondInfo.proTypeList == null || useCondInfo.proTypeList.size() == 0}'>class="hide"</s:if>>
				  <thead>
					<tr>
					  <th style="width:35%;"><s:text name="global.page.cateVal" /></th>
					  <th style="width:40%;"><s:text name="global.page.cateValName" /></th>
					  <th style="width:10%;">个数</th>
					  <th class="center" style="width:15%;"><s:text name="global.page.option" /></th>
					</tr>
				  </thead>
				  <tbody id="useProTypeShowDiv">
				  	<s:iterator value="useCondInfo.proTypeList" id="proTypeInfo">
				  		<tr>
				  	 	<td class="hide">
				  	 		<input type="hidden" name="cateValId" value='<s:property value="#proTypeInfo.cateValId"/>'/>
				  	 		<input type="hidden" name='cateId' value='<s:property value="#proTypeInfo.cateId"/>' />
				  	 	</td>
				  	 	<td style="width:25%;"><s:property value="#proTypeInfo.cateVal"/></td>
				  	 	<td style="width:40%;"><s:property value="#proTypeInfo.cateValName"/></td>
				  	 	<td style="width:10%;">
				  	 		<input type="text" class="number" name="cateNum" value='<s:property value="#proTypeInfo.cateNum"/>' maxlength="2" />
				  	 	</td>
				  	 	<td class="center" style="width:15%;"><a href="javascript:void(0);" onClick="binolssprm7302.deleteHtml(this);return false;">删除</a></td>
				  	 	</tr>
				  	</s:iterator>
				  </tbody>
				</table>
		</div>
		  </div>
		 <div class="box4-content  clearfix">
		  <div style="width:12%;float:left;">
    	<div class="ui-icon icon-arrow-crm"></div><label class="bg_title">需参活动
		</label></div>
		<div style="width:88%;float:left;">
				<span class="left" >
                <a class="add" onclick="binolssprm7302.popCampaignList('2');return false;">
				  <span class="ui-icon icon-search"></span>
				  <span class="button-text"><s:text name="global.page.Popselect" /></span>
			    </a>
			    </span>
			    <table class="all_clean" style="margin-left: 15px;">
					<tbody id="camp_tbody">
						<s:iterator value="useCondInfo.campList" id="campInfo">
							<tr style="float:left;margin-left:10px;">
							<td>
								<span class="list_normal">
								<span class="text" style="line-height:19px;"><s:property value="#campInfo.campName"/></span>
								<span class="close" style="margin: 0 1px 2px 5px;" onclick="binolssprm7302.delCampaignHtml(this);return false;"><span class="ui-icon ui-icon-close"></span></span>
								<input type="hidden" name="campaignMode" value="<s:property value='#campInfo.campaignMode'/>" />
								<input type="hidden" name="campaignCode" value="<s:property value='#campInfo.campaignCode'/>" />
								</span>
							</td>
							</tr>
						</s:iterator>
					</tbody>
				</table>
		</div>
		  </div>
		  <div class="box4-content  clearfix">
		  <div style="width:12%;float:left;">
    	<div class="ui-icon icon-arrow-crm"></div><label class="bg_title">使用对象
		</label></div>
		<div style="width:88%;float:left;">
			<select name="memberKbn" style="width:100px;" onchange="binolssprm7302.changeSpan('useMemberKbn', this);" class="z-param">
				<option <s:if test='%{useCondInfo.memberKbn == "0"}'>selected</s:if> value='0'>请选择</option>
				<option <s:if test='%{useCondInfo.memberKbn == "2"}'>selected</s:if> value='2'>会员等级</option>
			</select>
		</span>
		<div id="useMemberKbn2" style="margin-top:10px;" <s:if test='%{useCondInfo.memberKbn == null || useCondInfo.memberKbn != "2"}'> class="hide" </s:if> >
		    <s:iterator value="useCondInfo.levelList" id="levelInfo" status="status">
		    	<input type="checkbox" name="memLevel" id='useMemLevel_<s:property value="#status.index"/>' 
		    	<s:if test='%{#levelInfo.checkFlag == "1"}'>checked="checked"</s:if>
		    	value='<s:property value="#levelInfo.levelId"/>'/>
		    	<label for='useMemLevel_<s:property value="#status.index"/>' style="cursor:pointer;margin-right:20px;"><s:property value="#levelInfo.levelName"/> </label>
		    </s:iterator>
		</div>
		  </div>
		  </div>
		  
		  <div class="box4-content  clearfix">
		  <div style="width:12%;float:left;">
    	<div class="ui-icon icon-arrow-crm"></div><label class="bg_title">其它限制
		</label></div>
		<div style="width:88%;float:left;">
			是否允许和其它类型的券一起使用？
			<select name="otherCond" style="width:50px;"  class="z-param">
				<option <s:if test='%{useCondInfo.otherCond == "1"}'>selected</s:if> value='1'>是</option>
				<option <s:if test='%{useCondInfo.otherCond == "0"}'>selected</s:if> value='0'>否</option>
			</select>
		  </div>
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
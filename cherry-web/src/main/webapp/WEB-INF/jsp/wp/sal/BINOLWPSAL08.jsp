<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<s:i18n name="i18n.wp.BINOLWPSAL08">
<div class="panel ui-corner-all">
   	<div class="panel-header">
        <span class="ui-icon icon-ttl-control"></span>
        <span class="wp_title"><s:text name="wpsal08.promotionTitle"/></span>
        <span class="hide">
			<input type="checkbox" id="ckAllPromotion" class="wp_checkbox" onclick="BINOLWPSAL02.promotion(this);">
			<span id="ckAllPromotionText" onclick="BINOLWPSAL02.promotionText(this);"><s:text name="wpsal08.allPromotion"/></span>
        </span>
    </div>
	<div id="promotionData" class="panel-content promotion">
		<s:if test='null != promotionList && promotionList.size() > 0'>
			<s:iterator value="promotionList" id="promotionList">
				<ul>
					<li>
						<input type="hidden" id="subjectCode" value='<s:property value="subjectCode" />' />
						<input type="hidden" id="activityCode" value='<s:property value="activityCode" />' />
						<input type="hidden" id="activityType" value='<s:property value="activityType" />' />
						<input type="hidden" id="activityClassify" value='<s:property value="activityClassify" />' />
						<input type="hidden" id="campaignValid" value='<s:property value="campaignValid" />' />
						<input type="hidden" id="needBuyFlag" value='<s:property value="needBuyFlag" />' />
						<input type="hidden" id="exNeedPoint" value='<s:property value="exNeedPoint" />' />
						<input type="hidden" id="detailType" value='<s:property value="detailType" />' />
						<p>
							<input type="checkbox" id="ckPromotion" class="wp_checkbox" value='<s:property value="activityCode" />' onclick="BINOLWPSAL02.checkPromotion(this);">
							<span id="ckPromotionText" onclick="BINOLWPSAL02.checkPromotionText(this);"><s:property value="activityName" /></span>
						</p>
	                	<p><span class="date"><s:text name="wpsal08.proEndDate"/><s:property value="activityToDate" /></span></p>
					</li>
				</ul>
			</s:iterator>
		</s:if>
		<s:else>
			<ul>
				<li>
					<p><span><s:text name="wpsal08.promotionNull"/></span></p>
				</li>
			</ul>	
		</s:else>
    </div>
</div>
</s:i18n>

<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<s:i18n name="i18n.ss.BINOLSSPRM13">
<style>
.box2-header .tip {
    float: left;
    margin-left: 10px;
    margin-top: 5px;
}
</style>
<div class="box4">
	<div class="box4-header clearfix"><strong class="left"><s:text name='ruleInfo'/></strong></div><!-- 活动规则信息 -->
	<div class="box4-content clearfix" id = "condition_body">
		<%-- ========================时间地点设定开始======================== --%>
		<s:iterator value="actGrpTimeList">
			<div class="box2 box2-active time_location_box">
				<div class="box2-header clearfix"> 
					<%-- 活动时间TAB --%>
					<strong class="left active"><span class="ui-icon icon-clock"></span><s:text name="activeTime"/></strong>
					<strong class="left location" onclick="initLocationBox(this,'<s:if test="%{map.showType == 'edit_add'}">editable</s:if><s:else>detail</s:else>');"><span class="ui-icon icon-flag"></span><s:text name="activeLocation"/></strong>
					<s:if test="%{map.showType == 'edit_add'}">
					<div class="tip highlight">
						<s:text name="tip6">
							<s:param><s:text name="activeLocation"/></s:param>
							<s:param><s:text name="activeLocation"/></s:param>
						</s:text>
					</div>
					</s:if>
					<%-- 添加活动时间 
					<s:if test="%{map.showType == 'edit_add'}">
					<a class="add right" onclick ="addActiveTime('#activeTimeInfo');" style="margin-top: 5px;">
						<span class="ui-icon icon-add"></span><span class="button-text"><s:text name="activeTime"/></span>
     				</a>
					</s:if>
					--%>
				</div>
				<div class="box2-content start clearfix" style="padding:.5em 1.5em">
		      		<div id="activeTimeInfo" class="end">
		      			<s:iterator value="actSglTimeList">
							<div class="clearfix time_box">
							<%-- 开始时间 --%>
							<p class="column">
								<span>
									<label><s:text name="startTime"/></label>
									<span class="highlight2"><s:property value="basePropValue1"/>
									
									<input type="hidden" name="startTime" value="<s:property value='basePropValue1'/>"/>								
								</span>
							</p>
							<%-- 终止时间 --%>
							<p class="column">
								<span>
									<label><s:text name="endTime"/></label>
									<span class="highlight2"><s:property value="basePropValue2"/>
									
									<input type="hidden" name="endTime" value="<s:property value='basePropValue2'/>"/>
								</span>
							</p>
						</div>
						</s:iterator>
		       		</div>
		     	</div>
		   	</div>
		</s:iterator>
	   	<%-- ========================时间地点设定结束======================== --%>
	   	<%-- ========================规则条件设定开始======================== --%>
		<div class="box2 box2-active hide">
			<div class="box2-header clearfix">
				<strong class="left active"><span class="ui-icon icon-book"></span><s:text name="otherCondition"/></strong>
			</div>
			<div id= "rule-detail-content" class="box2-content"></div>
		</div>
		<%-- ========================规则条件设定结束======================== --%>
	</div>
</div>
</s:i18n>
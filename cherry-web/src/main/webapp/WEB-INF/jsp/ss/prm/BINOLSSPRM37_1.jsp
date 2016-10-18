<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<s:i18n name="i18n.ss.BINOLSSPRM13">
<div class="box4">
	<div class="box4-header clearfix"><strong class="left"><s:text name="ruleSet"/></strong></div>
	<div class="box4-content clearfix" id = "condition_body">
		<%-- ========================时间地点设定开始======================== --%>
		<s:iterator value="actGrpTimeList">
			<div class="box2 box2-active time_location_box">
				<div class="box2-header clearfix"> 
					<%-- 活动时间TAB --%>
					<strong class="left active">
						<span class="ui-icon icon-clock"></span><s:text name="activeTime"/><span class="highlight">*</span>
					</strong>
					<strong class="left location" onclick="initLocationBox(this,'edit');">
						<span class="ui-icon icon-flag"></span><s:text name="activeLocation"/><span class="highlight">*</span>
					</strong>
					<%-- 时间地点TAB 
					<a style="margin-left: 8em; margin-top: 0.2em;" class="add left" onclick ="addTimeLocation();">
						<span class="ui-icon icon-add"></span><span class="button-text"><s:text name="timeLocatin"/></span>
					</a>
					<a style="margin-top: 0.2em;<s:if test='%{actGrpTimeList.size==1}'>display:none;</s:if>" class="add left remove_time_location" 
						onclick="removeTimeLocation(this);">
						<span class="ui-icon icon-delete"></span><span class="button-text"><s:text name="removeLocatin"/></span>
					</a>
					--%>
					<%-- 添加活动时间 --%>
					<a class="add right" onclick ="addActiveTime('#activeTimeInfo');" style="margin-top: 5px;">
						<span class="ui-icon icon-add"></span><span class="button-text"><s:text name="activeTime"/></span>
     				</a>
     				
				</div>
				<div class="box2-content start clearfix" style="padding:.5em 1.5em">
		      		<div id="activeTimeInfo" class="end">
		      		<s:iterator value="actSglTimeList" id="time">
						<div class="clearfix time_box">
							<%-- 开始时间 --%>
							<p class="column">
								<span>
									<s:if test='sSearch == "not_start" || showType == "copy"'>
										<label><s:text name="startTime"/></label><input type="text" class="date startTime" name="startTime" value="<s:property value='startTime'/>"/>
										<label><input class="date time" name="startTime2" type="text" value="<s:property value='startTime2'/>"/></label>
									</s:if>
									<s:else>
										<input type="hidden" name="startTime" value="<s:property value='startTime'/>"/>
										<input type="hidden" name="startTime2" value="<s:property value='startTime2'/>"/>
										<label><s:text name="startTime"/></label><input type="text" class="date startTime" disabled="disabled" name="startTime" value="<s:property value='startTime'/>"/>
										<label><input class="date time" name="startTime2" type="text" disabled="disabled" value="<s:property value='startTime2'/>"/></label>
									</s:else>
								</span>
							</p>
							<%-- 终止时间 --%>
							<p class="column">
								<span>
									<label><s:text name="endTime"/></label><input type="text" class="date endTime" name="endTime" value="<s:property value='endTime'/>"/>
									<label><input class="date time" name="endTime2" type="text" value="<s:property value='endTime2'/>"/></label>
								</span>
							</p>
							<span <s:if test='%{actSglTimeList.size==1}'>style="display:none;"</s:if> class="close left mt1">
								<span class="ui-icon ui-icon-close" onclick="removeTime(this);"></span>
							</span>
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
<%-- ==========================================HIDE设定开始=========================================== --%>
<div id="amountBox" class="ui-option ui-option-1 hide">
  	<ul class="u1">	    
    	<li id ="options-1" class="options-1"><s:text name="valueSet"/></li><%-- 设定值 --%>
    	<li id ="options-2" class="options-2"><s:text name="areaSet"/></li><%-- 设定范围 --%>
  	</ul>
</div>
<div id ="amountScope" class="ui-option ui-option-3 hide" style="padding:10px; width:335px;">
    <label><s:text name="startAmount"/></label><%-- 起始金额 --%>
    <input id="scopeStart" type="text" class="number" maxlength="5"/>
    <label><s:text name="endAmount"/></label><%-- 结束金额 --%>
    <input id="scopeEnd" type="text" class="number" maxlength="5"/>
    <button id ="amountScopeOK" class="plain"><s:text name="global.page.ok"/></button><%-- 确定 --%>	    
    <button id ="amountScopeCancle" class="plain"><s:text name="global.page.cancle"/></button><%-- 取消 --%>
</div>
<div id="logicOperator" class="ui-option ui-option-4 hide">
  	<ul class="u1">
	    <li id="op_1"><s:text name="lessThan"/></li><%-- 少于 --%>
	    <li id="op_2"><s:text name="lessThanOrEquel"/></li><%-- 小于或等于 --%>
	    <li id="op_3"><s:text name="equel"/></li><%-- 等于--%>
	    <li id="op_4"><s:text name="notEquel"/></li><%-- 不等于 --%>
	    <li id="op_5"><s:text name="moreThenOrEquel"/></li><%-- 大于或等于 --%>
	    <li id="op_6"><s:text name="moreThen"/></li><%-- 大于 --%>
  	</ul>
</div>
<div id ="conditionBox" class="ui-option ui-option-add-1 hide" style="width:250px;">
	  <%-- 选择规则条件 --%>
	  <div id="ruleConditionBox" class="clearfix">
	  	<span class="label"><s:text name="selRuleCondition"/></span>
	    <ul class="u1 left">
	      <%-- 消费金额 --%>
	      <li id ="ruleCon_amount"><s:text name="expenseAmount"/></li>
	      <%-- 购买产品 --%>
	      <li id="ruleCon_product"><s:text name="buyProducts"/></li>
	    </ul>
	</div>
  	<%-- 选择逻辑条件 --%>
  	<div id="logicConditionBox" class="clearfix">
  		<span class="label"><s:text name="selLogicCondition"/></span>
    	<ul class="u1 left">
	      <%-- 满足任意条件 --%>
	      <li id="logicCon_or"><s:text name="conditionOr"/></li>
	      <%-- 满足全部条件 --%>
	      <li id="logicCon_and"><s:text name="conditionAnd"/></li>
    	</ul>
  	</div>
	<%-- 选择位置条件 --%>
	<div id="positionBox" class="clearfix"><span class="label"><s:text name="selConditionPosition"/></span>
		<ul class="u1 left">
	      	<%-- 当前条件 --%>
	      	<li class="on clearfix" id="con_this"><input type="radio" class="left radio" name="positionRadio"/><span class="ui-icon icon-add-big"></span><s:text name="thisCondition"/></li>
	      	<%-- 上一层条件 --%>
	      	<li class="clearfix" id="con_up"><input  type="radio" class="left radio" name="positionRadio"/><span class="ui-icon icon-add-sorting-asc"></span><s:text name="upCondition"/></li>
	      	<%-- 下一层条件 --%>
	      	<li class="clearfix" id="con_down"><input type="radio" class="left radio" name="positionRadio"/><span class="ui-icon icon-add-sorting-desc"></span><s:text name="downCondition"/></li>
	    </ul>
  	</div>
  	<p class="center">
  		<%-- 确定 --%>
    	<button class="plain" id ="boxOK" onclick="closeConditionBox();return false;"><s:text name="global.page.ok"/></button>
    	<%-- 取消 --%>
     	<button class="plain" id ="boxCancle" ><s:text name="global.page.cancle"/></button>
  	</p>
</div>
<%-- 逻辑条件编辑BOX START --%>
<div  id="editLogicBox" class="ui-option ui-option-add-2 hide" style="width:200px;">
	<%-- 选择逻辑条件 --%>
	<div id ="editLogicInBox" class="clearfix">
		<span class="label"><s:text name="selLogicCondition"/></span>
	    <ul class="u1">
	    	<%-- 满足任意条件 --%>
	      	<li id="editLogicBox_or"><s:text name="conditionOr"/></li>
	      	<%-- 满足全部条件 --%>
	      	<li id="editLogicBox_and"><s:text name="conditionAnd"/></li>
	    </ul>
	</div>
	<p class="center">
	  	<%-- 确定 --%>
	    <button class="plain" id="logicBoxOK"><s:text name="global.page.ok"/></button>
	    <%-- 取消 --%>
	     <button class="plain" id="logicBoxCancle"><s:text name="global.page.cancle"/></button>
	</p>
</div>
<%-- ==========================================HIDE设定结束=========================================== --%>
</s:i18n>

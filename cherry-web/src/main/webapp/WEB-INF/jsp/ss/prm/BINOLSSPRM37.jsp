<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%> 
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<%@ page import="com.cherry.cm.core.CherryConstants" %>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<link rel="stylesheet" href="/Cherry/plugins/zTree v3.1/css/zTreeStyle/zTreeStyle.css" type="text/css" media="screen, projection">
<link type="text/css" href="/Cherry/css/cherry/cherry_timepicker.css" rel="stylesheet">
<script type="text/javascript" src="/Cherry/js/common/ajaxfileupload.js"></script>
<script type="text/javascript" src="/Cherry/js/ss/prm/BINOLSSPRM13/BINOLSSPRM13.js"></script>
<script type="text/javascript" src="/Cherry/js/ss/prm/BINOLSSPRM37.js"></script>
<script type="text/javascript" src="/Cherry/js/ss/prm/BINOLSSPRM13/BINOLSSPRM13_01.js"></script>
<script type="text/javascript" src="/Cherry/js/ss/prm/BINOLSSPRM13/BINOLSSPRM13_06.js"></script>
<script type="text/javascript" src="/Cherry/plugins/zTree v3.1/js/jquery.ztree.all-3.1.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-timepicker-addon.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>

<script type="text/javascript">
	// 日历初期化
	$(function () {
		var timeLocationJson = '${map.grpLocationPageList}';
		timeLocationJson = timeLocationJson.replace(/ALL_CONTER/,BINOLSSPRM13_js_i18n.allCounter);
		grpLocationPageList = eval('('+timeLocationJson+')');
		// 假日
		binOLSSPRM13_global.holidays = '${holidays}';
		// 日历起始时间
		binOLSSPRM13_global.calStartDate = '';
		// 日历事件绑定
		calEventBind();
		PRM13.initTime();
		var length = $('#prmActiveform').find('.time_location_box').length;
		for (var i=1;i<length;i++){
			binOLSSPRM13_global.timeLocationDataArr.push({});
		}
		// 读取规则体
		getRuleHtml(eval('('+'${map.ruleHTML}'+')'));	
		
	});
</script>
<s:i18n name="i18n.ss.BINOLSSPRM13">
<div class="main container clearfix">
	<div class="panel ui-corner-all">
		<div id="div_main">
			<cherry:form id = "prmActiveform" class ="inline" csrftoken="false">
				<input type ="hidden" name ="departType" value="<s:property value='departType'/>" id="departType"/>
				<%-- 活动地点 JSON --%>
				<input type ="hidden" name ="timeLocationJSON" value="" id="timeLocationJSON"/>
				<%-- 活动CODE --%>
				<s:hidden name ="activityCode" value="%{map.activityCode}" id="activityCode"></s:hidden>
				<%-- 活动id --%>
				<s:hidden name ="activeID" value="%{activeID}" id="activeID"></s:hidden>
				<%-- 活动规则明细id --%>
				<s:hidden name ="ruleID" value="%{map.ruleID}" id="ruleID"></s:hidden>
				<%-- 显示类型 --%>
				<s:hidden name ="showType" value="%{showType}" id="showType"></s:hidden>
				<%-- 更新次数 --%>
				<s:hidden name ="updTime" value="%{map.updTime}" id="updateTime"></s:hidden>
				<%-- 更新时间 --%>
				<s:hidden name ="modCount" value="%{map.modCount}" id="modifyCount"></s:hidden>
				<%-- 活动设定者 --%>
				<s:hidden name ="activitySetBy" value="%{map.activitySetBy}" id="activitySetBy"></s:hidden>
				<%-- 规则明细HTML --%>
				<input type ="hidden" name ="ruleHTML" value="" id="ruleHTML"/>
				<s:hidden name ="templateFlag" value="%{map.templateFlag}"></s:hidden>
				<%-- 规则Drl类型 --%>
				<input type ="hidden" name ="ruleDrl" value="" id="ruleDrl"/>
				<input type ="hidden" name ="resultInfo" value="" id="resultInfo"/>
				<s:if test='%{showType == "edit" && sendFlag== 2}'>
			  		<input type="hidden" name="sendFlag" value="2"/>
			  	</s:if>
				<%-- 标题 --%>
			    <div class="panel-header">   
		        	<div class="clearfix">
        				<span class="breadcrumb left"><span class="ui-icon icon-breadcrumb"></span><s:text name="title"/> &gt;
		        		<s:if test='%{showType == "edit"}'><s:text name="editPrmActive"/></s:if>
		        		<s:if test='%{showType == "copy"}'><s:text name="copyPrmActive"/></s:if>
		        		</span> 
		        	</div>
		      	</div>
		      	<div id="actionResultDisplay"></div>
		      	<div class="panel-content">
		      		<p class="clearfix"><span class="highlight"><s:text name="userTip"/></span></p>
		      		<%-- ==========================================活动设定开始=========================================== --%>
		      		<jsp:include page="/WEB-INF/jsp/ss/prm/BINOLSSPRM13_4.jsp" flush="true"/>
		          	<%-- ==========================================活动设定结束=========================================== --%>
		          	<hr class="space" />
					<%-- ==========================================规则设定开始=========================================== --%>
		        	<jsp:include page="/WEB-INF/jsp/ss/prm/BINOLSSPRM37_1.jsp" flush="true"/>
		        	<%-- ==========================================规则设定结束=========================================== --%>
					<hr class="space" />
					<%-- ==========================================奖励设定开始=========================================== --%>
		        	<s:if test="3 == map.virtualPrmFlag">
		        		<jsp:include page="/WEB-INF/jsp/ss/prm/BINOLSSPRM37_3.jsp" flush="true"/>
		        	</s:if>
		        	<s:else>
		        		<jsp:include page="/WEB-INF/jsp/ss/prm/BINOLSSPRM37_2.jsp" flush="true"/>
		        	</s:else>
	           		<%-- ==========================================奖励设定结束=========================================== --%>
		        	<hr class="space" />
			        <div class="center clearfix">
			          <s:if test="%{showType == 'edit'}">
			          		<%-- 编辑 --%>
			          	   <button type="button" class="save" id ="editPrmActive" onclick="savePrmActiveRule($('#editPrmActiveUrl').html());"><span class="ui-icon icon-save"></span><span class="button-text"><s:text name="global.page.save"/></span></button>
			          </s:if>
			           <s:if test="%{showType == 'copy'}">
			           		<button type="button" class="save" id ="copyPrmActive" onclick="savePrmActiveRule($('#copyPrmActiveUrl').html());"><span class="ui-icon icon-save"></span><span class="button-text"><s:text name="global.page.save"/></span></button>
			           </s:if>
			          <%-- 关闭 --%>
			          <button type="button" onclick="window.close();" class="close"><span class="ui-icon icon-close"></span><span class="button-text"><s:text name="global.page.close"/></span></button>
			        </div>
		      	</div>
			</cherry:form>
		</div>
	</div>
</div>		
<div id="amountBox" class="ui-option ui-option-1 hide">
  <ul class="u1">
    <%-- 设定值 --%>
    <li id ="options-1" class="options-1"><s:text name="valueSet"/></li>
    <%-- 设定范围 --%>
    <li id ="options-2" class="options-2"><s:text name="areaSet"/></li>
  </ul>
</div>

<div id ="amountScope" class="ui-option ui-option-3 hide" style="padding:10px; width:335px;">
	<%-- 起始金额 --%>
    <label><s:text name="startAmount"/></label>
    <input id="scopeStart" type="text" class="number" />
    <%-- 结束金额 --%>
    <label><s:text name="endAmount"/></label>
    <input id="scopeEnd" type="text" class="number" />
    <%-- 确定 --%>
    <button id ="amountScopeOK" class="plain"><s:text name="global.page.ok"/></button>
    <%-- 取消 --%>
    <button id ="amountScopeCancle" class="plain"><s:text name="global.page.cancle"/></button>
</div>

<div id="logicOperator" class="ui-option ui-option-4 hide">
  <ul class="u1">
  	<%-- 少于 --%>
    <li id="op_1"><s:text name="lessThan"/></li>
    <%-- 小于或等于 --%>
    <li id="op_2"><s:text name="lessThanOrEquel"/></li>
    <%-- 等于--%>
    <li id="op_3"><s:text name="equel"/></li>
    <%-- 不等于 --%>
    <li id="op_4"><s:text name="notEquel"/></li>
    <%-- 大于或等于 --%>
    <li id="op_5"><s:text name="moreThenOrEquel"/></li>
    <%-- 大于 --%>
    <li id="op_6"><s:text name="moreThen"/></li>
  </ul>
</div>
<div id ="conditionBox" class="ui-option ui-option-add-1 hide" style="width:250px;">
  <%-- 选择规则条件 --%>
  <div id="ruleConditionBox" class="clearfix"><span class="label"><s:text name="selRuleCondition"/></span>
    <ul class="u1 left">
      <%-- 消费金额 --%>
      <li id ="ruleCon_amount"><s:text name="expenseAmount"/></li>
      <%-- 购买产品 --%>
      <li id="ruleCon_product"><s:text name="buyProducts"/></li>
    </ul>
  </div>
  
  <%-- 选择逻辑条件 --%>
  <div id="logicConditionBox" class="clearfix"><span class="label"><s:text name="selLogicCondition"/></span>
    <ul class="u1 left">
      <%-- 满足任意条件 --%>
      <li id="logicCon_or"><s:text name="conditionOr"/></li>
      <%-- 满足全部条件 --%>
      <li id="logicCon_and"><s:text name="conditionAnd"/></li>
    </ul>
  </div>
  <%-- 选择逻辑条件 --%>
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
    <button class="plain" id ="boxOK" onclick="closeConditionBox();"><s:text name="global.page.ok"/></button>
    <%-- 取消 --%>
     <button class="plain" id ="boxCancle" ><s:text name="global.page.cancle"/></button>
  </p>
</div>
<%-- 逻辑条件编辑BOX START --%>
<div  id="editLogicBox" class="ui-option ui-option-add-2 hide" style="width:200px;">
	<%-- 选择逻辑条件 --%>
  <div id ="editLogicInBox" class="clearfix"><span class="label"><s:text name="selLogicCondition"/></span>
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
<%-- 逻辑条件编辑BOX END --%>

<%--=============================== URL定义开始 ===============================--%>
<s:hidden name="grpLocationPageList" value="%{map.grpLocationPageList}"></s:hidden>
<s:url id="s_locationSearchUrl" value="/ss/BINOLSSPRM13_getLocationByAjax" />
<s:url id="s_copyPrmActiveUrl" value="/ss/BINOLSSPRM37_copyPrmActive" />
<s:url id="s_counterSearchUrl" value="/ss/BINOLSSPRM13_getCounterByAjax" />
<s:url id="s_addPrmActiveGrpUrl" value="/ss/BINOLSSPRM13_addPrmActiveGrp" />
<s:url id="s_initActiveLocationDetailUrl" value="/ss/BINOLSSPRM37_initActiveLocationDetail" />
<s:url id="s_getCounterDetailByAjaxUrl" value="/ss/BINOLSSPRM37_getCounterDetailByAjax" />
<s:url id="s_checkCounterNodesUrl" value="/ss/BINOLSSPRM37_checkCounterNodes" />
<s:url id="s_editPrmActiveUrl" value="/ss/BINOLSSPRM37_editPrmActive" />
<s:url id="s_BINOLSSPRM13Url" value="/ss/BINOLSSPRM13" />
<%--促销地点查询URL --%>
<s:url id="s_indSearchPrmLocationUrl" value="/ss/BINOLSSPRM14_indSearchPrmLocation" />
<%-- 取得柜台父节点信息 --%>
<s:url id="s_getCounterParentUrl" value="/ss/BINOLSSPRM37_getCounterParent" />
<%-- 取得当前活动组信息 --%>
<s:url id="s_changeGroupUrl" value="/ss/BINOLSSPRM13_getGroupInfo" />
<span id ="locationSearchUrl" style="display:none">${s_locationSearchUrl}</span>
<span id ="copyPrmActiveUrl" style="display:none">${s_copyPrmActiveUrl}</span>
<span id ="counterSearchUrl" style="display:none">${s_counterSearchUrl}</span>
<span id ="addPrmActiveGrpUrl" style="display:none">${s_addPrmActiveGrpUrl}</span>
<span id ="initActiveLocationDetailUrl" style="display:none">${s_initActiveLocationDetailUrl}</span>
<span id ="getCounterDetailByAjaxUrl" style="display:none">${s_getCounterDetailByAjaxUrl}</span>
<span id ="editPrmActiveUrl" style="display:none">${s_editPrmActiveUrl}</span>
<span id ="BINOLSSPRM13Url" style="display:none">${s_BINOLSSPRM13Url}</span>
<%--促销地点查询URL --%>
<span id ="indSearchPrmLocationUrl" style="display:none">${s_indSearchPrmLocationUrl}</span>
<%--取得柜台父节点信息URL --%>
<span id ="getCounterParentUrl" style="display:none">${s_getCounterParentUrl}</span>
<%--活动组选择框change事件URL --%>
<span id ="changeGroupUrl" style="display:none">${s_changeGroupUrl}</span>
<%--柜台导入URL --%>
<span id="importCounterUrl" style="display:none">/Cherry/cp/BINOLCPPOI01_importCounter</span>
<%--预读柜台节点 --%>
<span id ="checkCounterNodesUrl" style="display:none">${s_checkCounterNodesUrl}</span>
<%--=============================== URL定义结束 ===============================--%>
<%-- ================== js国际化  START ======================= --%>
<jsp:include page="/WEB-INF/jsp/ss/prm/BINOLSSPRM13_2.jsp" flush="true" />
<%-- ================== js国际化     END  ======================= --%>
</s:i18n>

<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%> 
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<%@ page import="com.cherry.ss.common.PromotionConstants" %>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<link rel="stylesheet" href="/Cherry/plugins/zTree v3.1/css/zTreeStyle/zTreeStyle.css" type="text/css" media="screen, projection">
<link type="text/css" href="/Cherry/css/cherry/cherry_timepicker.css" rel="stylesheet">
<script type="text/javascript" src="/Cherry/js/common/ajaxfileupload.js"></script>
<script type="text/javascript" src="/Cherry/js/ss/prm/BINOLSSPRM13/BINOLSSPRM13.js"></script>
<script type="text/javascript" src="/Cherry/js/ss/prm/BINOLSSPRM13/BINOLSSPRM13_01.js"></script>
<script type="text/javascript" src="/Cherry/js/ss/prm/BINOLSSPRM13/BINOLSSPRM13_06.js"></script>
<script type="text/javascript" src="/Cherry/plugins/zTree v3.1/js/jquery.ztree.all-3.1.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-timepicker-addon.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript">
	// 日历初期化
	$(function () {
		// 假日
		binOLSSPRM13_global.holidays = '${holidays}';
		// 日历起始时间
		binOLSSPRM13_global.calStartDate = '${calStartDate}';
		// 日历事件绑定
		calEventBind();
		PRM13.initTime();
	});
</script>
<s:i18n name="i18n.ss.BINOLSSPRM13">
<div class="main container clearfix">
	<div class="panel ui-corner-all">
		<div id="div_main">
			<cherry:form id = "prmActiveform" class ="inline" csrftoken="false" action="/Cherry/ss/BINOLSSPRM13">
				<%-- 活动地点 JSON --%>
				<input type ="hidden" name ="timeLocationJSON" value="" id="timeLocationJSON"/>
				<input type ="hidden" name ="ruleHTML" value="" id="ruleHTML"/>
				<input type ="hidden" name ="departType" value="<s:property value='departType'/>" id="departType"/>
				<input type ="hidden" name ="resultInfo" value="" id="resultInfo"/>
				<input type ="hidden" name ="templateFlag" value="" id="templateFlag"/>
				<%-- 规则Drl类型 --%>
				<input type ="hidden" name ="ruleDrl" value="" id="ruleDrl"/>
			  	<%-- 标题 --%>
		      	<div class="panel-header">
        			<div class="clearfix">
        				<span class="breadcrumb left"><span class="ui-icon icon-breadcrumb"></span><s:text name="title"/> &gt; <s:text name="addPrmActive"/></span> 
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
		        	<jsp:include page="/WEB-INF/jsp/ss/prm/BINOLSSPRM13_5.jsp" flush="true"/>
		        	<%-- ==========================================规则设定结束=========================================== --%>
		        	<hr class="space" />
		        	<%-- ==========================================奖励设定开始=========================================== --%>
		        	<s:if test="3 == map.virtualPrmFlag">
		        		<jsp:include page="/WEB-INF/jsp/ss/prm/BINOLSSPRM13_7.jsp" flush="true"/>
		        	</s:if>
		        	<s:else>
		        		<jsp:include page="/WEB-INF/jsp/ss/prm/BINOLSSPRM13_6.jsp" flush="true"/>
		        	</s:else>
	           		<%-- ==========================================奖励设定结束=========================================== --%>
		        	<hr class="space" />
		        	<div class="center clearfix">
		        		<%-- 保存 --%>
	          			<button type="button" class="save" id ="savePrmActive" onclick="savePrmActiveRule($('#savaActiveUrl').html(),'1');">
	          				<span class="ui-icon icon-save"></span><span class="button-text"><s:text name="saveTemplate"/></span>
	          			</button>
	          			<%-- 保存 --%>
	          			<button type="button" class="save" id ="savePrmActive" onclick="savePrmActiveRule($('#savaActiveUrl').html(),'0');">
	          				<span class="ui-icon icon-save"></span><span class="button-text"><s:text name="global.page.save"/></span>
	          			</button>
	          			<%-- 关闭 --%>
	          			<button type="button" onclick="window.close();" class="close">
	          				<span class="ui-icon icon-close"></span><span class="button-text"><s:text name="global.page.close"/></span>
	          			</button>
	        		</div>
	      		</div>
			</cherry:form>
			<%-- ==========================================HIDE设定开始=========================================== --%>
		</div>
	</div>
</div>
<%--=============================== URL定义开始 ===============================--%>
<s:url id="s_locationSearchUrl" value="/ss/BINOLSSPRM13_getLocationByAjax" />
<s:url id="s_savaActiveUrl" value="/ss/BINOLSSPRM13_savePromotionActive" />
<s:url id="s_counterSearchUrl" value="/ss/BINOLSSPRM13_getCounterByAjax" />
<s:url id="s_addPrmActiveGrpUrl" value="/ss/BINOLSSPRM13_addPrmActiveGrp" />
<s:url id="s_getCounterDetailByAjaxUrl" value="/ss/BINOLSSPRM37_getCounterDetailByAjax" />
<s:url id="s_BINOLSSPRM13Url" value="/ss/BINOLSSPRM13" />
<s:url id="s_delCurrentBrandInfoIDUrl" value="/ss/BINOLSSPRM13_delCurrentBrandInfoID" />
<s:url id="s_checkCounterNodesUrl" value="/ss/BINOLSSPRM37_checkCounterNodes" />
<%--促销地点查询URL --%>
<s:url id="s_indSearchPrmLocationUrl" value="/ss/BINOLSSPRM13_indSearchPrmLocation" />
<%-- 取得柜台父节点信息 --%>
<s:url id="s_getCounterParentUrl" value="/ss/BINOLSSPRM37_getCounterParent" />
<%-- 取得当前活动组信息 --%>
<s:url id="s_changeGroupUrl" value="/ss/BINOLSSPRM13_getGroupInfo" />

<span id ="locationSearchUrl" style="display:none">${s_locationSearchUrl}</span>
<span id ="savaActiveUrl" style="display:none">${s_savaActiveUrl}</span>
<span id ="counterSearchUrl" style="display:none">${s_counterSearchUrl}</span>
<span id ="addPrmActiveGrpUrl" style="display:none">${s_addPrmActiveGrpUrl}</span>
<span id ="getCounterDetailByAjaxUrl" style="display:none">${s_getCounterDetailByAjaxUrl}</span>
<span id ="BINOLSSPRM13Url" style="display:none">${s_BINOLSSPRM13Url}</span>
<%--促销地点查询URL --%>
<span id ="indSearchPrmLocationUrl" style="display:none">${s_indSearchPrmLocationUrl}</span>
<%--取得柜台父节点信息URL --%>
<span id ="getCounterParentUrl" style="display:none">${s_getCounterParentUrl}</span>
<span id ="delCurrentBrandInfoIDUrl" style="display:none">${s_delCurrentBrandInfoIDUrl}</span>
<%--活动组选择框change事件URL --%>
<span id ="changeGroupUrl" style="display:none">${s_changeGroupUrl}</span>
<%--预读柜台节点 --%>
<span id ="checkCounterNodesUrl" style="display:none">${s_checkCounterNodesUrl}</span>
<%--柜台导入URL --%>
<span id="importCounterUrl" style="display:none">/Cherry/cp/BINOLCPPOI01_importCounter</span>
<%--默认日期 --%>
<span id="defaultDate" style="display:none"><s:property value="@com.cherry.ss.common.PromotionConstants@DEFAULT_END_DATE"/></span>
<%--=============================== URL定义结束 ===============================--%>

<%-- ================== js国际化  START ======================= --%>
<jsp:include page="/WEB-INF/jsp/ss/prm/BINOLSSPRM13_2.jsp" flush="true" />
<%-- ================== js国际化     END  ======================= --%>
</s:i18n>


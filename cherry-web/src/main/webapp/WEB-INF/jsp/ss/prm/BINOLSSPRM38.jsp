<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%> 
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<link rel="stylesheet" href="/Cherry/plugins/zTree v3.1/css/zTreeStyle/zTreeStyle.css" type="text/css" media="screen, projection">
<script type="text/javascript" src="/Cherry/js/common/ajaxfileupload.js"></script>
<script type="text/javascript" src="/Cherry/js/ss/prm/BINOLSSPRM13/BINOLSSPRM13.js"></script>
<script type="text/javascript" src="/Cherry/js/ss/prm/BINOLSSPRM37.js"></script>
<script type="text/javascript" src="/Cherry/js/ss/prm/BINOLSSPRM13/BINOLSSPRM13_01.js"></script>
<script type="text/javascript" src="/Cherry/js/ss/prm/BINOLSSPRM13/BINOLSSPRM13_06.js"></script>
<script type="text/javascript" src="/Cherry/plugins/zTree v3.1/js/jquery.ztree.all-3.1.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript">
	// 日历初期化
	$(function () {
		var timeLocationJson = '${map.grpLocationPageList}';
		timeLocationJson = timeLocationJson.replace(/ALL_CONTER/,BINOLSSPRM13_js_i18n.allCounter);
		grpLocationPageList = eval('('+timeLocationJson+')');
		var length = $('#prmActiveform').find('.time_location_box').length;
		for (var i=1;i<length;i++){
			binOLSSPRM13_global.timeLocationDataArr.push({});
		}
		// z-Tree 设置
		binOLSSPRM13_global.zTreeSetting = {
				check: {
					enable: true
				},
				data:{
					key:{
						name:"name",
						children:"nodes"
					}
				},
				callback:{beforeExpand: expendTreeNodesByAjax}
		};
		binOLSSPRM13_global.showType = "detail";
		// 读取规则体
		getRuleHtml(eval('('+'${map.ruleHTML}'+')'));
		$('#actProList').find("tr.gray,tr.red").cluetip({width: '500',splitTitle:'|'});
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
					<%-- 规则明细HTML --%>
					<input type ="hidden" name ="ruleHTML" value="" id="ruleHTML"/>
				  	<%-- 标题 --%>
				    <div class="panel-header">
	        			<div class="clearfix">
	        				<span class="breadcrumb left"><span class="ui-icon icon-breadcrumb"></span><s:text name="title"/> &gt; <s:text name="detailPrmActive"/></span> 
	        			</div>
	      			</div>
		      	<div class="panel-content">
		      		<div id="actionResultDisplay"></div>
			      	<%-- ==========================================活动基础详细开始=========================================== --%>
		      		<jsp:include page="/WEB-INF/jsp/ss/prm/BINOLSSPRM38_1.jsp" flush="true"/>
		          	<%-- ==========================================活动基础详细结束=========================================== --%>
					<hr class="space" />
					<%-- ==========================================活动规则详细开始=========================================== --%>
		      		<jsp:include page="/WEB-INF/jsp/ss/prm/BINOLSSPRM38_2.jsp" flush="true"/>
		          	<%-- ==========================================活动规则详细结束=========================================== --%>
		          	<hr class="space" />
		          	<%-- ==========================================活动奖励详细开始=========================================== --%>
		      		<s:if test="3 == map.virtualPrmFlag">
		        		<jsp:include page="/WEB-INF/jsp/ss/prm/BINOLSSPRM38_4.jsp" flush="true"/>
		        	</s:if>
		        	<s:else>
		        		<jsp:include page="/WEB-INF/jsp/ss/prm/BINOLSSPRM38_3.jsp" flush="true"/>
		        	</s:else>
		          	<%-- ==========================================活动奖励详细结束=========================================== --%>
		       		<hr class="space" /> 
		       		<p class="center clearfix">
					  	<%-- 保存(详细画面不显示) --%>
					  	<s:if test="!'detail'.equals(map.showType)">
						    <button onclick="updActLocation(0);return false;" class="save" type="button">
						    	<span class="ui-icon icon-save"></span>
						    	<span class="button-text"><s:text name="global.page.save"/></span>
						    </button>
					  	</s:if>
					    <%-- 取消 --%>
					     <button type="button" onclick="window.close();" class="close">
					     	<span class="ui-icon icon-close"></span>
					     	<span class="button-text"><s:text name="global.page.close"/></span>
					     </button>
					  </p>
		       	</div>
			</cherry:form>
		</div>
	</div>
</div>
<%--柜台导入URL --%>
<s:url id="importCounterUrl" value="/cp/BINOLCPPOI01_importCounter" />
<span id="importCounterUrl" style="display:none">${importCounterUrl}</span>
<s:url id="s_indSearchPrmLocationUrl" value="/ss/BINOLSSPRM14_indSearchPrmLocation" />
<%--促销地点查询URL --%>
<span id ="indSearchPrmLocationUrl" style="display:none">${s_indSearchPrmLocationUrl}</span>
<s:url id="addActLocationURL" value="/ss/BINOLSSPRM13_ajaxAddActLocation">
	<s:param name="ruleId"><s:property value="map.ruleID"/></s:param>
</s:url>
<span id="addActLocationURL" style="display:none">${addActLocationURL}</span>
<%-- ================== js国际化  START ======================= --%>
<jsp:include page="/WEB-INF/jsp/ss/prm/BINOLSSPRM13_2.jsp" flush="true" />
<%-- ================== js国际化导入    END  ======================= --%>
</s:i18n>

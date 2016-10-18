<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>

<script type="text/javascript" src="/Cherry/js/lib/jquery.layout.min.js"></script>
<script type="text/javascript" src="/Cherry/js/bs/common/BINOLBSCOM01.js"></script>

<script type="text/javascript" src="/Cherry/plugins/zTree v3.1/js/jquery.ztree.all-3.1.js"></script>
<script type="text/javascript" src="/Cherry/js/pt/jcs/BINOLPTJCS14_tree.js"></script>
<script type="text/javascript" src="/Cherry/js/pt/jcs/BINOLPTJCS14.js"></script>

<link rel="stylesheet" href="/Cherry/plugins/zTree v3.1/css/zTreeStyle/zTreeStyle.css" type="text/css" media="screen, projection">
<link type="text/css" href="/Cherry/css/cherry/cherry_timepicker.css" rel="stylesheet">
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-timepicker-addon.js"></script>
<script>
$(document).ready(function() {
	// 活动范围树初始化
	ptjcs14Tree.searchNodes($('#locationType_0'),0);
	//ptjcs14Tree.loadTree('${prtDeportPriceRule.placeJson}',0);
	
});
</script>

<input type="hidden" id="locationType_0" value='2'/>
<%-- 产品价格方案维护URL --%>
<s:url id="prtSoluInit_url" action="BINOLPTJCS14_prtSoluInit"/>

<s:i18n name="i18n.pt.BINOLPTJCS14">
<s:text name="global.page.select" id="select_default"/>
<cherry:form id="mainForm">
<div class="panel-header">
    <div class="clearfix">
		<span class="breadcrumb left">	    
			<jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
		</span>
   	</div>
</div>
<div id="treeLayoutDiv" class="div-layout-all">
     <div class="ui-layout-west">
       <div style="min-width: 190px;padding:0px;margin-top:15px;" class="ztree jquery_tree box2 box2-active">
         <div class="treebox2-header">
           <strong><span class="ui-icon icon-tree"></span><s:text name="JCS14.cntList"></s:text></strong>
         </div>
         
         <%-- 可输入下拉框 --%>
       	<div id="locationId_0" class="treebox2-header">
			<input id="locationPositiontTxt_0" class="text" type="text" name="locationPosition" style="width: 100px;margin-right: 0;vertical-align: middle;"/>
           	<a onclick="ptjcs14Tree.getPosition(0);return false;" class="search" style="margin:3px 0 0 0;">
				<span class="ui-icon icon-position"></span>
				<span class="button-text"><s:text name="global.page.locationPosition" /></span>
			</a>
		</div>
		<%-- 树形结构 --%>
        <dl><dt class="jquery_tree tree" id="tree_0" ></dt></dl>
        <s:hidden name="placeJson" id="placeJson_0" value=""/>
        <s:hidden name="saveJson" id="saveJson_0" value=""/>
        <s:hidden id="prtPriceSolutionListId" value="%{configMap.prtPriceSolutionListJson}"/>
        <s:hidden id="businessDateId" value="%{configMap.businessDate}"/>
        <div id="actionResultDisplay"></div>
       </div>
     </div>
     <div class="ui-layout-center"  style="overflow-y:hidden;">
	     <div style="min-width: 450px;" id="categoryInfo">
		   <div id="msgDIV"></div>	
		   <div class="section">
		   	  <div class="clearfix">
			   	  <span class="right">
				 	<a href="#" class="add" onclick="JCS14.addPrtSolu('${prtSoluInit_url}');return false;">
				 		<span class="button-text"><s:text name="JCS14.soluManage"/></span>
				 		<span class="ui-icon icon-add"></span>
				 	</a>
				 	</span>
		   	  </div>
            <div class="section-content hide" id="shwoMainInfo">
	            <div class="hide" id="hiddenDIV">
	            	<s:hidden id="departProductConfigID" name="departProductConfigID" value="%{configMap.departProductConfigID}"/>
	            	<s:hidden id="dbSaveJson" name="dbSaveJson" value="%{configMap.saveJson}"/>
	            </div>
                <table class="detail" cellpadding="0" cellspacing="0">
	             	<tr>
		                <th><s:text name="JCS14.objType"/></th><%-- 设置对象类型 --%>
		                <td colspan="3">
		                	<span id="objTypeDesc"></span><%-- 设置对象类型 --%>
		                </td>
	             	</tr>
	             	<tr>
		                <th><s:text name="JCS14.objName"/></th><%-- 设置对象名称 --%>
		                <td colspan="3">
		                	<span id=""></span>
		                	<span id="objName"></span><%-- 对象名称 --%>
		                	<input type="hidden" name="objType" id="objType"  /><%-- 对象类型 --%>
		                	<input type="hidden" name="objId" id="objId"  /><%-- 对象Id --%>
		                	<input type="hidden" name="cntArr" id="cntArr"  /><%-- 柜台Code集合 --%>
		                </td>
	             	</tr>
	             	<tr>
		                <th><s:text name="JCS14.solu"/><div id="soluLoadId"></div></th><%-- 应用方案 --%>
		                <td colspan="3">
		                	<span>
		                		<s:select id="productPriceSolutionID" name="productPriceSolutionID" list="prtPriceSolutionList" listKey="solutionID" listValue="solutionNameDesc" value="solutionID"
		                		headerKey="" headerValue="%{#select_default}" onchange="JCS14.changeSolu(this);" />
	                		</span>
					   	  	 <span id="soluDateId" class="hide">
						   	  	  <span style="margin-left:0px;margin-left:10px;"><strong><s:text name="方案生效日"/>:</strong><%-- 方案生效日 --%> </span>
							      <span class="green" style='margin-left:0px;'>
									  <strong><span id="soluStartDateId"> </span></strong>
							      </span>
						   	  	  <span style="margin-left:0px;margin-left:10px;"><strong><s:text name="方案失效日"/>:</strong><%-- 方案失效日 --%> </span>
							      <span class="green" style='margin-left:0px;'>
									  <strong><span id="soluEndDateId"> </span></strong>
							      </span>
					   	      </span>
							  <span id="clearSaveId">
								<span class="highlight">※</span>
								<span class="gray">选项值为"请选择"时，点击"应用"按钮，如果柜台有应用方案则会被清空。</span>
							  </span>
		                </td>
	             	</tr>
         		</table>
			     <div class="center " id="pageButton">
			       <button id="save" class="back" type="button" onclick="JCS14.addSave()">
			        	<span class="ui-icon icon-save"></span>
			        	<span class="button-text"><s:text name="JCS14.configUse"/></span>
			       </button>
			     </div>
               </div>
		   </div>
	     </div>
     </div>
</div>
<div class="hide">
  	<s:url action="BINOLPTJCS14_add" id="addSaveUrl"></s:url>
  	<input type="hidden" id="addSaveUrlId" value="${addSaveUrl}"/>
  	
  	<s:url action="BINOLPTJCS14_getPrtPriceSolu" id="getPrtPriceSoluUrl"></s:url>
  	<input type="hidden" id="getPrtPriceSoluUrlId" value="${getPrtPriceSoluUrl}"/>
  	
  	<s:url id="getPrtPriceSoluList" action="BINOLPTJCS14_getPrtPriceSoluList" />
  	<input type="hidden" id="getPrtPriceSoluListId" value="${getPrtPriceSoluList}"/>
  	
    <div id="actionSuccessId" style="" class="actionSuccess">
    	<ul class="actionMessage">
			<li><span id="msgId"><s:text name="global.page.operateSuccess"/></span></li>
		</ul>
	 </div>
	 <div id="actionFaildId" class="actionError">
	 	<ul class="errorMessage">
	 	<li><span><s:text name="global.page.operateFaild"/></span></li>	</ul>
	 </div>
	 <div id="select_defaultId"><s:text name="global.page.select" /></div>
  	
</div>
</cherry:form>
</s:i18n>

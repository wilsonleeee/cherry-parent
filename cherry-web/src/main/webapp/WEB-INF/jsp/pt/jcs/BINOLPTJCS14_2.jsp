<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/bs/common/BINOLBSCOM01.js"></script>
<script type="text/javascript" src="/Cherry/plugins/zTree v3.1/js/jquery.ztree.all-3.1.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery.layout.min.js"></script>
<script type="text/javascript" src="/Cherry/js/pt/jcs/BINOLPTJCS14_1.js"></script>

<link rel="stylesheet" href="/Cherry/plugins/zTree v3.1/css/zTreeStyle/zTreeStyle.css" type="text/css" media="screen, projection">
<link type="text/css" href="/Cherry/css/cherry/cherry_timepicker.css" rel="stylesheet">
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-timepicker-addon.js"></script>
<script>
$(document).ready(function() {
	// 活动范围树初始化
    //ptjcs14_1.searchNodes($('#locationType_0'),0);
	//ptjcs14Tree.loadTree('${prtDeportPriceRule.placeJson}',0);
	productBinding({elementId:"locationPositiontTxt_0",showNum:20,targetId:"unitCode",targetShow:"nameTotal"});//产品名称
	
});
</script>

<s:i18n name="i18n.pt.BINOLPTJCS14">
<s:text name="global.page.select" id="select_default"/>
<cherry:form id="mainForm" csrftoken="false">
<div id="div_main" class="main container clearfix">
<div class="panel ui-corner-all">
<div class="panel-header">
    <div class="clearfix">
		<span class="breadcrumb left">	    
			<s:text name="JCS14.cntPrtPriceManage"/> &gt; <s:text name="JCS14.soluManage"/>
		</span>
   	</div>
</div>
<div id="treeLayoutDiv" class="div-layout-all">
     <div class="ui-layout-west">
     	<s:text name="JCS14.solu"/>:
     	<span>
     		<s:select name="productPriceSolutionID" list="prtPriceSolutionList" listKey="solutionID" listValue="solutionNameDesc" value="solutionID"
     		headerKey="" headerValue="%{#select_default}" onchange="ptjcs14_1.changeSolu(this,0);"/>
     		<s:hidden id="prtPriceSolutionListId" value="%{configMap.prtPriceSolutionListJson}"/>
    	</span>
					 	
       <div style="min-width: 190px;padding:0px;margin-top:15px;" class="ztree jquery_tree box2 box2-active">
         <div class="treebox2-header">
           <strong><span class="ui-icon icon-tree"></span><s:text name="JCS14.prtList"></s:text></strong>
         </div>
         
         <%-- 可输入下拉框 --%>
       	<div id="locationId_0" class="treebox2-header">
			<input id="locationPositiontTxt_0" class="text" type="text" name="locationPosition" style="width: 100px;margin-right: 0;vertical-align: middle;"/>
           	<a onclick="ptjcs14_1.getPosition(0,null,1);return false;" class="search" style="margin:3px 0 0 0;">
				<span class="ui-icon icon-position"></span>
				<span class="button-text"><s:text name="global.page.locationPosition" /></span>
			</a>
			<s:hidden id="unitCode"></s:hidden>
		</div>
		<%-- 树形结构 --%>
        <dl><dt class="jquery_tree tree" id="tree_0" ></dt></dl>
        <s:hidden name="prtJson" id="prtJson_0" value=""/>
        <s:hidden name="prtSaveJson" id="prtSaveJson_0" value=""/>
        <s:hidden id="businessDateId" value="%{configMap.businessDate}"/>
        
       </div>
     </div>
     <div class="ui-layout-center"  style="overflow-y:hidden;">
	     <div style="min-width: 450px;" id="categoryInfo">
		  <div id="msgDIV"></div>			   
		   <div class="section">
		   	  <div class="section-header clearfix">
		   	  	 <span id="soluDateId" class="hide">
			   	  	  <span><strong><s:text name="方案生效日"/>:</strong><%-- 方案生效日 --%> </span>
				      <span class="green" style='margin-left:0px;'>
						  <strong><span id="soluStartDateId"> </span></strong>
				      </span>
			   	  	  <span><strong><s:text name="方案失效日"/>:</strong><%-- 方案失效日 --%> </span>
				      <span class="green" style='margin-left:0px;'>
						  <strong><span id="soluEndDateId"> </span></strong>
				      </span>
		   	      </span>
		   	  
		   	  
		   	  
			   	  <span class="right hide" id="editPrtPrcieSoluId">
					 	<a class="add " id="editPrtPrcieSolu">
					 		<span class="button-text"><s:text name="JCS14.editSolu"/></span>
					 		<span class="ui-icon icon-edit"></span>
					 	</a>
				 	</span>
			   	  <span class="right">
					 	<a class="add right" id="addPrtPrcieSolu">
					 		<span class="button-text"><s:text name="JCS14.addSolu"/></span>
					 		<span class="ui-icon icon-add"></span>
					 	</a>
				 	</span>
		   	  </div>
		   	  
		   	  
		      <div class="panel-content hide " id="mainPanel">
		      <div id="actionResultDisplay"></div>
			    <div class="hide" id="hiddenDIV">
				      <s:hidden name="productID" value="%{productId}"/>
				      <s:hidden name="productPriceSolutionDetailID" />
			    </div>
				<div class="box4">
					<div class="box4-header"><strong><s:text name="JCS14.basisInfo" /></strong></div>
					<div class="box4-content">
				        <s:hidden name="ruleId" value="%{prtDeportPriceRule.ruleId}"/>
				        <table class="detail" cellpadding="0" cellspacing="0">
			             	<tr>
				                <th><s:text name="JCS14.prtName"/></th><%-- 产品名称--%>
				                <td colspan="3">
				                	<span id="objName"></span>
				                </td>
			             	</tr>
		         		</table>
					</div>
				</div>
				<div class="box4">
					<div class="box4-header"><strong><s:text name="JCS14.soluPrtPrcie" /></strong></div>
					<div class="box4-content">
						<div class="box2 box2-active">
						  <div class="box2-header clearfix"> 
								<strong class="left active"><span class="ui-icon icon-money"></span><s:text name="JCS14.priceOpt"/></strong>
						  </div>
						  <div class="box2-content start clearfix" style="padding:0.5em 1.5em;">
				            <div class="section">
				            <%-- ======================= 产品价格开始  ============================= --%>
				            <div class="section-content" >
				                 <input type="hidden" name="priceJson" id="priceJson" value=""/>
				                  <div id="minMaxPriceInfo" class="clearfix" style="padding:3px 10px;border: 1px solid #E5E5E5; background-color: #EEEEEE;">
					            	  <span >
					            		<s:text name="JCS14.minSalePrice"/><%-- 销售最低价 --%>
					            		<span class="green highlight"><strong><span id="minSalePriceDesc"></span></strong></span>
					            		<input class="hide price" name="minSalePrice" id="minSalePrice" value="<s:if test="proMap.minSalePrice != 0"><s:property value="proMap.minSalePrice"/></s:if>"/><s:text name="JCS14.moneryUnit"/>
					            	  </span>
					            	  <span style="margin-left:30px;">
					            		<s:text name="JCS14.maxSalePrice"/><%-- 销售最高价 --%> 
					            		<span class="green highlight"><strong><span id="maxSalePriceDesc"></span></strong> </span>
					            		<input class="hide price" name="maxSalePrice" id="maxSalePrice" value="<s:if test="proMap.maxSalePrice != 0"><s:property value="proMap.maxSalePrice"/></s:if>"/><s:text name="JCS14.moneryUnit"/>
					            	  </span>
					            	  <span id="addPriceId" class="<s:if test='%{prtDeportPriceRule.priceMode == 2}'>hide</s:if>">
					            	  </span>
				                  </div>
						          <div class="section-content <s:if test='%{prtDeportPriceRule.priceMode == 2}'>hide</s:if>" id="priceInfo">
						              	<s:iterator value="sellPriceList" status="st">
						              		<table class="detail" cellpadding="0" cellspacing="0">
								            </table>
						              	</s:iterator>
						          </div>
				            </div>
				            <%-- ======================= 产品价格结束  ============================= --%>
				            </div>
					   	  </div>
				   	      </div>
					</div>
				</div>
				<%--保存 --%>
			     <div class="center clearfix" id="pageButton">
			       <button id="save" class="save" type="button" onclick="addPrtSoluDetail()" title="添加到当前方案">
			        	<span class="ui-icon icon-add-big"></span>
			        	<span class="button-text"><s:text name="添加"/></span>
			       </button>
			       <button id="delBtnId" class="save " type="button" onclick="delPrtSoluDetail()" style="display:none" title="从当前方案中去除">
			        	<span class="ui-icon icon-delete-big"></span>
			        	<span class="button-text"><s:text name="去除"/></span>
			       </button>
			       <%--
		       		<button id="close" class="close" type="button"  onclick="doClose();return false;">
		            	<span class="ui-icon icon-close"></span>
		            	<span class="button-text"><s:text name="global.page.close"/></span>
		          </button>
		          --%>
			     </div>
		      </div>
		   	  
		   </div>
	     </div>
     </div>
</div>
</div>
</div>
</cherry:form>

<div id="prt_price_solu_dialog_Main" >
  <form id="addForm" class="inline" method="post">
  <div  style="display:none" id="prt_price_solu_dialog">
	<table style="width:100%" class="detail" border="1" cellspacing="0" cellpadding="0">
	<tr>
		<th><s:text name="JCS14.soluName"/><span class="highlight">*</span></th>
		<td><span><input type ="text" class ="text" name ="solutionName" id="solutionName" maxlength="20"/></span></td>
	</tr>
	<tr>
		<th><s:text name="JCS14.comments"/></th>
		<td><span><s:textfield id="comments" name="comments" cssClass="text" maxlength="200"/></span></td>
	</tr>
	<tr>
		<th><s:text name="生效日期"/><span class="highlight">*</span></th>
		<td><span><input id="soluStartTime" type="text" style="width:90px;" class="date startTime" name="startTime"/></span></td>
	</tr>
	<tr>
		<th><s:text name="失效日期"/><span class="highlight">*</span></th>
		<td><span><input id="soluEndTime" type="text" style="width:90px;" class="date endTime" name="endTime"/></span></td>
	</tr>
	</table>
  </div>
  </form>
</div>
<input type="hidden" id="dateHolidays" name="dateHolidays" value="${holidays}"/>
<%-- 折扣率 --%>
<div class="hide" id="dialogInit"></div>   
<div class="rateDialog hide" style="z-index:100">
	<s:text name="JCS14.discountRate"/><%-- 折扣率 --%>
	<input class="number" id="memRate" value="100" onblur="ptjcs14_1.closeDialog(this);return false;"  
		onkeyup="ptjcs14_1.setMemPrice();return false;"/><s:text name="global.page.percent"/>
</div>

<%-- 价格--%>
 <div id="priceRow" class="hide" >
   <table class="detail" cellpadding="0" cellspacing="0">
    <caption style="height:20px;">
		<span class="delBtn">
		   <a class="delete right" href="javascript:void(0)" onclick="ptjcs14_1.delDivRow(this);return false;">
		   	<span class="ui-icon icon-delete"></span><span class="button-text"><s:text name="global.page.delete"/></span><%-- 删除按钮 --%>
		   </a>
	    </span>
	</caption>
	<tbody>
      	<tr>
          <th><label><s:text name="JCS14.salePrice"/><span class="highlight"><s:text name="global.page.required"/></span></label></th><%-- 销售价格 --%>
          <td><span><s:textfield name="salePrice" cssClass="price" maxlength="17" onkeyup="ptjcs14_1.setMemPrice(this);return false;"/><s:text name="JCS14.moneryUnit"/></span></td>
          <th>
          	<span style="background-color:#FFDDBB;border:1px solid red; padding:2px;"><s:text name="JCS14.memPrice"/></span>
          	<span class="calculator" title="<s:text name='JCS03.calculatorPrice'/>" onclick="ptjcs14_1.initRateDiv(this);"></span>
          </th><%-- 会员价格 --%>
          <td><span><s:textfield name="memPrice" cssClass="price" maxlength="17" /><s:text name="JCS14.moneryUnit"/></span></td>
        </tr>
     	<tr>
          <th><label><s:text name="JCS14.StartDate"/><span class="highlight"><s:text name="global.page.required"/></span></label></th><%-- 生效日期 --%>
          <td><span><s:textfield id="" name="priceStartDate" cssClass="date" cssStyle="width:80px;"/></span></td>
          <th><label><s:text name="JCS14.endDate"/></label></th><%-- 失效日期 --%>
          <td><span><s:textfield id="" name="priceEndDate" cssClass="date" cssStyle="width:80px;"/></span></td>
        </tr>
    </tbody>
   </table>
 </div>

<div class="hide">
  	<s:url action="BINOLPTJCS14_add" id="addSaveUrl"></s:url>
  	<input type="hidden" id="addSaveUrlId" value="${addSaveUrl}"/>
  	
  	<s:url action="BINOLPTJCS14_getPrtPriceSolu" id="getPrtPriceSoluUrl"></s:url>
  	<input type="hidden" id="getPrtPriceSoluUrlId" value="${getPrtPriceSoluUrl}"/>
  	
  	<s:url id="addPrtPriceSoluUrl" action="BINOLPTJCS14_addPrtPriceSolu" />
  	<input type="hidden" id="addPrtPriceSoluUrlId" value="${addPrtPriceSoluUrl}"/>
  	
  	<s:url id="updPrtPriceSoluUrl" action="BINOLPTJCS14_updPrtPriceSolu" />
  	<input type="hidden" id="updPrtPriceSoluUrlId" value="${updPrtPriceSoluUrl}"/>
  	
  	<s:url id="addPrtPriceSoluDetailUrl" action="BINOLPTJCS14_addPrtPriceSoluDetail" />
  	<input type="hidden" id="addPrtPriceSoluDetailUrlId" value="${addPrtPriceSoluDetailUrl}"/>
  	
  	<s:url id="delPrtPriceSoluDetailUrl" action="BINOLPTJCS14_delPrtPriceSoluDetail" />
  	<input type="hidden" id="delPrtPriceSoluDetailUrlId" value="${delPrtPriceSoluDetailUrl}"/>
  	
  	<s:url id="getPrtDetailInfo" action="BINOLPTJCS14_getPrtDetailInfo" />
  	<input type="hidden" id="getPrtDetailInfoId" value="${getPrtDetailInfo}"/>
  	
  	<s:url id="getPrtCateVal" action="BINOLPTJCS14_getPrtCateVal" />
  	<input type="hidden" id="getPrtCateValId" value="${getPrtCateVal}"/>
  	
    <div id="actionSuccessId" style="" class="actionSuccess">
    	<ul class="actionMessage">
			<li><span id="msgId"><s:text name="global.page.operateSuccess"/></span></li>
		</ul>
	 </div>
	 <div id="actionFaildId" class="actionError">
	 	<ul class="errorMessage">
	 	<li><span><s:text name="global.page.operateFaild"/></span></li>	</ul>
	 </div>
</div>
<%-- 产品价格方案维护URL --%>
<s:url id="prtSoluInit_url" action="BINOLPTJCS14_prtSoluInit"/>

<div class="hide">
    <div id="addMsgTitle"><s:text name="JCS14.addSoluTitle" /></div>
    <div id="editMsgTitle"><s:text name="JCS14.editSoluTitle" /></div>
    <div id="globalSave"><s:text name="global.page.save" /></div>
    <div id="globalCancle"><s:text name="global.page.cancle" /></div>
    <div id="globalDelete"><s:text name="global.page.delete" /></div>
    
    <div id="salePriceTxt"><s:text name="JCS14.salePrice" /></div>
    <div id="memPriceTxt"><s:text name="JCS14.memPrice" /></div>
    <div id="calculatorPriceTxt"><s:text name="JCS14.calculatorPrice" /></div>
    <div id="startDateTxt"><s:text name="JCS14.StartDate" /></div>
    <div id="endDateTxt"><s:text name="JCS14.endDate" /></div>
    <div id="moneryUnitTxt"><s:text name="JCS14.moneryUnit" /></div>
    
    
    
    <div id="updateMsgTitle"><s:text name="JCS09_updateTitle" /></div>
    <div id="deleteMsgTitle"><s:text name="JCS09_deleteTitle" /></div>
    <div id="deleteMsgText"><p class="message"><span><s:text name="JCS09_deleteText" /></span></p></div>
    <div id="saveSuccessTitle"><s:text name="save_success" /></div>
    <div id="dialogConfirm"><s:text name="JCS09_yes" /></div>
    <div id="dialogCancel"><s:text name="JCS09_cancel" /></div>
    <div id="dialogClose"><s:text name="JCS09_close" /></div>
    <div id="dialogInitMessage"><s:text name="dialog_init_message" /></div>
    <div id="deleteMsgText"><p class="message"><span><s:text name="delete_success" /></span></p></div>
</div>

</s:i18n>

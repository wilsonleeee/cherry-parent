<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<link rel="stylesheet" href="/Cherry/css/cherry/combobox.css" type="text/css">
<link rel="stylesheet" href="/Cherry/plugins/zTree/zTreeStyle/zTreeStyle.css" type="text/css" media="screen, projection">
<script type="text/javascript" src="/Cherry/js/st/sfh/BINOLSTSFH10.js"></script>
<script type="text/javascript" src="/Cherry/plugins/zTree/jquery.ztree-2.6.min.js"></script>

<script type="text/javascript">
$(document).ready(function() {
	$('.tabs').tabs();
	cherryValidate({
		formId: 'mainForm',	
		rules: {
			date: {dateYYYYmm:true}
		}	
	});
	binOLSTSFH10.globalSearch();
	productBinding({elementId:"productName1",showNum:20});
	counterBinding({elementId:"departCode1",showNum:20,selected:"code"});
	productBinding({elementId:"productName2",showNum:20});
	counterBinding({elementId:"departCode2",showNum:20,selected:"code"});
} );

</script>
<style>
	.treebox_left{
		 background: none repeat scroll 0 0 #FFFFFF;
    	border: 1px solid #D6D6D6;
    	height: 300px;
	}
</style>
<s:i18n name="i18n.st.BINOLSTSFH10">
<s:url id="globalSearch_url" value="/st/BINOLSTSFH10_globalSearch"></s:url>
<s:url id="proSearch_url" value="/st/BINOLSTSFH10_proSearch"></s:url>
<s:url id="couSearch_url" value="/st/BINOLSTSFH10_couSearch"></s:url>
<s:url id="couPrtSearch_url" value="/st/BINOLSTSFH10_couPrtSearch"></s:url>
<s:url id="setProductParameter_url" value="/st/BINOLSTSFH10_setProductParameter"></s:url>
<s:url id="editPrtParam_url" value="/st/BINOLSTSFH10_editPrtParam"></s:url>
<s:url id="setCounterParameter_url" value="/st/BINOLSTSFH10_setCounterParameter"></s:url>
<s:url id="editCouParam_url" value="/st/BINOLSTSFH10_editCouParam"></s:url>
<s:url id="setCouPrtParameter_url" value="/st/BINOLSTSFH10_setCouPrtParameter"></s:url>
<s:url id="editCouPtrParam_url" value="/st/BINOLSTSFH10_editCouPtrParam"></s:url>
<s:url id="setGlobalParameter_url" value="/st/BINOLSTSFH10_setGlobalParameter"></s:url>
<s:url id="editGlobalParam_url" value="/st/BINOLSTSFH10_editGlobalParam"></s:url>
<s:url id="issOrderParam_url" value="/st/BINOLSTSFH10_issOrderParam"></s:url>
<s:url id="getTreeNodes_url" value="/st/BINOLSTSFH10_getTreeNodes"></s:url>
<s:url id="dateTip_url" value="/st/BINOLSTSFH10_dateTip"></s:url>

<div class="panel-header">
       <div class="clearfix"> 
	        <span class="breadcrumb left">	    
				<jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
			</span> 
       	<cherry:show domId="BINOLSTSFH1001">
       	<a class="add right" id="down" target="_blank" onclick="binOLSTSFH10.confirmInit();return false;">
	        <span class="ui-icon icon-down"></span><span class="button-text"><s:text name="SFH10_down"/></span>
	    </a>
	    </cherry:show>
       </div>
</div>
<div id="actionResultDisplay"></div>
<div class="tabs">
<cherry:form id="mainForm" class="inline">
	 <ul>
	 	<li><a href="#tabs-4" onclick="binOLSTSFH10.globalSearch();return false;"><s:text name="SFH10_globalTitle"/></a></li><%--全局参数设定 --%>
        <li><a href="#tabs-1" onclick="binOLSTSFH10.proSearch();return false;"><s:text name="SFH10_productTitle"/></a></li><%--产品参数设定 --%>
        <li><a href="#tabs-2" onclick="binOLSTSFH10.couSearch();return false;"><s:text name="SFH10_counterTitle"/></a></li><%--柜台参数设定 --%>
        <li><a href="#tabs-3" onclick="binOLSTSFH10.couPrtSearch();return false;"><s:text name="SFH10_couPrtTitle"/></a></li><%-- 柜台产品参数设定 --%>
     </ul>
          <%--==================================全局订货参数设定 START=================================== --%>	
     <div id="tabs-4">
     <div class="box">
     	<div class="box-header">
           	<strong style="font-size:13px">
           		<span class="ui-icon icon-ttl-search"></span><s:text name="global.page.condition"/>
           	</strong>
           	<cherry:show domId="BINOLSTSFH1005">
	     		<a class="add right" id="setMulGlobal" target="_blank" onclick="binOLSTSFH10.popSetGlobalParamDialog();return false;">
		        	<span class="ui-icon icon-setting"></span><span class="button-text" style="font-size:13px"><s:text name="SFH10_mulGlobal"/></span>
		    	</a>
     		</cherry:show>
        </div>
        <div class="box-content clearfix">
        	<div class="column" style="width:49%;">
        		<p>
        			<s:if test="%{brandInfoList.size()> 1}">
                    	<label style="font-size:13px"><s:text name="SFH10_brandName"></s:text></label>
                    	<s:text name="SFH10_select" id="SFH10_select"/>
                    	<s:select id="brandInfoId4" name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" headerKey="" headerValue="%{#SFH10_select}" cssStyle="width:100px;"></s:select>
                	</s:if><s:else>
                		<label style="font-size:13px"><s:text name="SFH10_brandName"></s:text></label>
                    	<s:select id="brandInfoId4" name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" cssStyle="width:100px;"></s:select>      	
                	</s:else>
        		</p>
        	</div>
        	<div class="column last" style="width:50%;">
        		<%-- <p>
        			<label style="font-size:13px"><s:text name="SFH10_counterCode"/></label>
	            	<s:textfield name="departCode" cssClass="text" maxlength="15" id="departCode1"/>
	            </p> --%>
        	</div> 
        </div>
        <p class="clearfix">
   			<%-- 查询 --%>
   			<button class="right search" type="submit" onclick="binOLSTSFH10.globalSearch();return false;" id="searchBut1">
   				<span class="ui-icon icon-search-big"></span>
   				<span class="button-text" style="font-size:15px"><s:text name="global.page.search"/></span>
   			</button>
  		</p> 
      </div>
      <div id="section" class="section">
		<div class="section-header">
			<strong style="font-size:13px"> 
			<span class="ui-icon icon-ttl-section-search-result"></span> 
			<s:text name="global.page.list" />
		 	</strong>
		</div>
		<div class="section-content">
			<table id="dataTable4" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
				<thead>
					<tr>
						<%-- No. --%>
						<th class="center" style="font-size:13px"><s:text name="No." /></th>
						<!-- 品牌名称 -->
						<th class="center" style="font-size:13px"><s:text name="SFH10_brandName" /></th>
						<%-- 月销售天数  --%>
						<th class="center" style="font-size:13px"><s:text name="SFH10_saleDaysOfMonth" /><s:text name="SFH10_day" /></th>
						<!-- 对X天内有销售的产品生成建议发货数量 -->
						<th class="center" style="font-size:13px"><s:text name="SFH10_daysOfProduct" /><s:text name="SFH10_day" /></th>
						<%-- 操作   --%>
						<th class="center" style="font-size:13px"><s:text name="SFH10_option" /></th>
					</tr>
				</thead>
			</table>
		</div>
	  </div>
    </div>
    <%--==================================全局订货参数设定 END===================================== --%>	
     
     <%--==================================产品订货参数设定 START===================================== --%>	
     <div id="tabs-1">
     <div class="box">
     	<div class="box-header">
           	<strong style="font-size:13px">
           		<span class="ui-icon icon-ttl-search"></span><s:text name="global.page.condition"/>
           	</strong>
           	<cherry:show domId="BINOLSTSFH1002">
     		<a class="add right" id="setMulProduct" target="_blank" onclick="binOLSTSFH10.popSetProtParamDialog('#yes');return false;">
	        <span class="ui-icon icon-setting"></span><span class="button-text" style="font-size:13px"><s:text name="SFH10_mulProduct"/></span>
	    </a>
     </cherry:show>
        </div>
        <div class="box-content clearfix">
        	<div class="column" style="width:49%;">
        		<p>
        			<s:if test="%{brandInfoList.size()> 1}">
                    	<label style="font-size:13px"><s:text name="SFH10_brandName"></s:text></label>
                    	<s:text name="SFH10_select" id="SFH10_select"/>
                    	<s:select id="brandInfoId1" name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" headerKey="" headerValue="%{#SFH10_select}" cssStyle="width:100px;"></s:select>
                	</s:if><s:else>
                		<label style="font-size:13px"><s:text name="SFH10_brandName"></s:text></label>
                    	<s:select id="brandInfoId1" name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" cssStyle="width:100px;"></s:select>      	
                	</s:else>
        		</p>
        		<p>
        			<label style="font-size:13px"><s:text name="SFH10_productName"/></label>
	            	<s:textfield name="productName" cssClass="text" maxlength="50" id="productName1"/>
	            	<input type="text" value="" name="prtVendorId" id="prtVendorId" style="display:none"/>
	            </p>
        	</div>
        	<div class="column last" style="width:50%;">
        		<p>
        			<label style="font-size:13px"><s:text name="SFH10_date"/></label>
	            	<s:textfield name="date" cssClass="text" maxlength="6" id="date1" onFocus="binOLSTSFH10.dateTip(1);return false;" onBlur="binOLSTSFH10.dateTip(0);return false;"/>
	            	<%-- <label style="display:inline" class="highlight"><s:text name="SFH10_dateTip"/></label> --%>
        		</p>
        		<div id="dateTip" style="position:absolute;top:82px;z-index:999;margin-left:85px;display:none;">
            		<span class="highlight">※</span>
					<span class="gray"><s:text name="SFH10_dateTip"/></span>
            	</div>
        	</div>
        </div>
        <p class="clearfix">
           			<%-- 查询 --%>
           			<button class="right search" type="submit" onclick="binOLSTSFH10.proSearch(); return false;" id="searchBut">
           				<span class="ui-icon icon-search-big"></span>
           				<span class="button-text" style="font-size:15px"><s:text name="global.page.search"/></span>
           			</button>
          		</p> 
        </div>
        <div id="section" class="section">
		<div class="section-header">
			<strong style="font-size:13px"> 
			<span class="ui-icon icon-ttl-section-search-result"></span> 
			<s:text name="global.page.list" />
		 	</strong>
		</div>
		
		<table id="dataTable1" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
		<thead>
			<tr>
				<%-- No. --%>
				<th class="center" style="font-size:13px"><s:text name="No." /></th>
				<%-- 产品名称 --%>
				<th class="center" style="font-size:13px">（<s:text name="SFH10_unitCode" />）<s:text name="SFH10_productName" /></th>
				<%-- 年月   --%>
				<th class="center" style="font-size:13px"><s:text name="SFH10_date" /></th>
				<%-- 调整系数  --%>
				<th class="center" style="font-size:13px"><s:text name="SFH10_adtCoefficient" /></th>
				<%-- 操作   --%>
				<th class="center" style="font-size:13px"><s:text name="SFH10_option" /></th>
			</tr>
			
		</thead>
	</table>
	</div>
	</div>
    
     <%--==================================产品订货参数设定 END======================================= --%>	
     
     <%--==================================柜台参数设定 START=================================== --%>	
     <div id="tabs-2">
     <div class="box">
     	<div class="box-header">
           	<strong style="font-size:13px">
           		<span class="ui-icon icon-ttl-search"></span><s:text name="global.page.condition"/>
           	</strong>
           		<cherry:show domId="BINOLSTSFH1003">
     		<a class="add right" id="setMulCounter" target="_blank" onclick="binOLSTSFH10.popSetCoutParamDialog('#yes');return false;">
	        <span class="ui-icon icon-setting"></span><span class="button-text" style="font-size:13px"><s:text name="SFH10_mulCounter"/></span>
	    </a>
     </cherry:show>
        </div>
        <div class="box-content clearfix">
        	<div class="column" style="width:49%;">
        		<p>
        			<s:if test="%{brandInfoList.size()> 1}">
                    	<label style="font-size:13px"><s:text name="SFH10_brandName"></s:text></label>
                    	<s:text name="SFH10_select" id="SFH10_select"/>
                    	<s:select id="brandInfoId2" name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" headerKey="" headerValue="%{#SFH10_select}" cssStyle="width:100px;"></s:select>
                	</s:if><s:else>
                		<label style="font-size:13px"><s:text name="SFH10_brandName"></s:text></label>
                    	<s:select id="brandInfoId2" name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" cssStyle="width:100px;"></s:select>      	
                	</s:else>
        		</p>
        		
        	</div>
        	<div class="column last" style="width:50%;">
        		<p>
        			<label style="font-size:13px"><s:text name="SFH10_counterCode"/></label>
	            	<s:textfield name="departCode" cssClass="text" maxlength="15" id="departCode1"/>
	            </p>
        	</div> 
        </div>
        <p class="clearfix">
           			<%-- 查询 --%>
           			<button class="right search" type="submit" onclick="binOLSTSFH10.couSearch();return false;" id="searchBut1">
           				<span class="ui-icon icon-search-big"></span>
           				<span class="button-text" style="font-size:15px"><s:text name="global.page.search"/></span>
           			</button>
          		</p> 
        </div>
        <div id="section" class="section">
		<div class="section-header">
			<strong style="font-size:13px"> 
			<span class="ui-icon icon-ttl-section-search-result"></span> 
			<s:text name="global.page.list" />
		 	</strong>
		</div>
		<div class="section-content">
		
		<table id="dataTable2" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
		<thead>
			<tr>
				<%-- No. --%>
				<th class="center" style="font-size:13px"><s:text name="No." /></th>
				<%-- 柜台名称 --%>
				<th class="center" style="font-size:13px">（<s:text name="SFH10_counterCode" />）<s:text name="SFH10_counterName" /></th>
				<%-- 订货间隔  --%>
				<th class="center" style="font-size:13px"><s:text name="SFH10_orderDays" /><s:text name="SFH10_day" /></th>
				<%-- 在途时间  --%>
				<th class="center" style="font-size:13px"><s:text name="SFH10_intransitDays" /><s:text name="SFH10_day" /></th>
				<%-- 操作   --%>
				<th class="center" style="font-size:13px"><s:text name="SFH10_option" /></th>
			</tr>
			
		</thead>
	</table>
	</div>
	</div>
     </div>
     <%--==================================柜台参数设定 END===================================== --%>	
     
     <%--==================================最低库存天数设定 START=============================== --%>	
     <div id="tabs-3">
     <div class="box">
     	<div class="box-header">
           	<strong style="font-size:13px">
           		<span class="ui-icon icon-ttl-search"></span><s:text name="global.page.condition"/>
           	</strong>
           	<cherry:show domId="BINOLSTSFH1004">
     		<a class="add right" id="setMulCouPrt" target="_blank" onclick="binOLSTSFH10.popSetCouPrtParamDialog();return false;">
	        <span class="ui-icon icon-setting"></span><span class="button-text" style="font-size:13px"><s:text name="SFH10_mulCouPrt"/></span>
	    </a>
     </cherry:show>
        </div>
        <div class="box-content clearfix">
        	<div class="column" style="width:49%;">
        		<p>
        			<s:if test="%{brandInfoList.size()> 1}">
                    	<label style="font-size:13px"><s:text name="SFH10_brandName"></s:text></label>
                    	<s:text name="SFH10_select" id="SFH10_select"/>
                    	<s:select id="brandInfoId3" name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" headerKey="" headerValue="%{#SFH10_select}" cssStyle="width:100px;"></s:select>
                	</s:if><s:else>
                		<label style="font-size:13px"><s:text name="SFH10_brandName"></s:text></label>
                    	<s:select id="brandInfoId3" name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" cssStyle="width:100px;"></s:select>      	
                	</s:else>
        		</p>
        		<p>
        			<label style="font-size:13px"><s:text name="SFH10_counterCode"/></label>
	            	<s:textfield name="departCode" cssClass="text" maxlength="15" id="departCode2"/>
	            </p>
        	</div>
        	<div class="column last" style="width:50%;">
        		<p>
        			<label style="font-size:13px"><s:text name="SFH10_productName"/></label>
	            	<s:textfield name="productName" cssClass="text" maxlength="50" id="productName2"/>
	            </p>
        	</div> 
        </div>
        <p class="clearfix">
           			<%-- 查询 --%>
           			<button class="right search" type="submit" onclick="binOLSTSFH10.couPrtSearch();return false;" id="searchBut1">
           				<span class="ui-icon icon-search-big"></span>
           				<span class="button-text" style="font-size:15px"><s:text name="global.page.search"/></span>
           			</button>
          		</p> 
        </div>
        <div id="section" class="section">
		<div class="section-header">
			<strong style="font-size:13px"> 
			<span class="ui-icon icon-ttl-section-search-result"></span> 
			<s:text name="global.page.list" />
		 	</strong>
		</div>
		<div class="section-content">
		
		<table id="dataTable3" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
		<thead>
			<tr>
				<%-- No. --%>
				<th class="center" style="font-size:13px"><s:text name="No." /></th>
				<%-- 柜台名称 --%>
				<th class="center" style="font-size:13px">（<s:text name="SFH10_counterCode" />）<s:text name="SFH10_counterName" /></th>
				<%-- 产品名称  --%>
				<th class="center" style="font-size:13px">（<s:text name="SFH10_unitCode" />）<s:text name="SFH10_productName" /></th>
				<%-- 最低库存天数  --%>
				<th class="center" style="font-size:13px"><s:text name="SFH10_lowestStockDays" /><s:text name="SFH10_day" /></th>
				<%-- 旬环比增长系数  --%>
				<th class="center" style="font-size:13px"><s:text name="SFH10_growthFactor" /></th>
				<%-- 主推计划(调整系数) --%>
				<th class="center" style="font-size:13px"><s:text name="SFH10_regulateFactor" /></th>
                <%-- 陈列数 --%>
                <th class="center" style="font-size:13px"><s:text name="SFH10_exhibitQuantity" /></th>
				<%-- 操作   --%>
				<th class="center" style="font-size:13px"><s:text name="SFH10_option" /></th>
			</tr>
			
		</thead>
	</table>
	</div>
	</div>
     </div>
     <%--==================================最低库存天数设定 END================================= --%>	
</cherry:form>
 </div>
     <%--==================================弹出设定产品参数框Start============================== --%>
     <div id="pop_product_main" style="display:none">
     	<div id="pop_product_body">
     		<div id="errorDiv_product" class="actionError" style="display:none">
        		<ul>
            		<li><span id="errorSpan_product"></span></li>
        		</ul>         
   		 	</div>
   		 	<table class="detail">
	   		 	<tbody>
		     		<tr>
		     		<%--年月 --%>
		     		<th><s:text name="SFH10_date"/></th>
		     		<td>
			     		<span><s:textfield name="date" maxlength="6" cssClass="text" id="date_pop"></s:textfield></span>
			     		<%-- <label class="highlight"><s:text name="SFH10_dateTip"/></label> --%>
			     		<span class="highlight">※</span>
						<span class="gray"><s:text name="SFH10_dateTip"/></span>
		     		</td>
		     		</tr>
		     		<tr>
		     		<%--调整系数 --%>
		     		<th><s:text name="SFH10_adtCoefficient"/></th>
		     		<td>
			     		<span><s:textfield name="adtCoefficient" maxlength="16" cssClass="text" id="adtCoefficient_pop"></s:textfield></span>
			     		<%-- <label class="highlight"><s:text name="SFH10_adtCoefficientTip"/></label> --%>
			     		<span class="highlight">※</span>
						<span class="gray"><s:text name="SFH10_adtCoefficientTip"/></span>
		     		</td>
		     		</tr>
	     		</tbody>
     		</table>
     		<div id="tree">
     		<div class="box2-header clearfix"> 
		   		<strong id="left_tree" class="left active" style="background:#E8E8C8">
       				<span class="ui-icon icon-flag"></span>
       				<s:text name="SFH10_choiceProduct"/>
       	   		</strong>
       	   		<span class="right">
					<input id="position_left_0" class="text locationPosition ac_input" type="text" style="width: 140px;" name="locationPosition" autocomplete="off">
					<a class="search" onclick="binOLSTSFH10.locationPrtPosition(this);return false;">
						<span class="ui-icon icon-position"></span>
						<span class="button-text"><s:text name="SFH10_locationPosition"></s:text></span>
					</a>
				</span>
       		</div>
			<div class="jquery_tree">
    			<div class="jquery_tree treebox_left tree" id = "treeDemo" style="overflow: auto;background:#FCFCFC;min-height:350px;height:auto"></div>
    		</div>
    		</div>
     	</div>
     </div>
     <%--==================================弹出设定产品参数框     End============================== --%>
     
     <%--==================================弹出设定柜台参数框Start============================== --%>
     <div id="pop_counter_main" style="display:none">
     	<div id="pop_counter_body">
     		<div id="errorDiv_counter" class="actionError" style="display:none">
        		<ul>
            		<li><span id="errorSpan_counter"></span></li>
        		</ul>         
   		 	</div>
   		 	<table class="detail">
   		 	<tbody>
	     		<tr>
	     		<%--订货间隔 --%>
	     		<th><s:text name="SFH10_orderDays"/></th>
	     		<td>
		     		<s:textfield name="orderDays" maxlength="4" cssClass="text" id="orderDays_pop"></s:textfield>
		     		<s:text name="SFH10_day"/>
	     		</td>
	     		</tr>
	     		<tr>
	     		<%--在途天数 --%>
	     		<th><s:text name="SFH10_intransitDays"/></th>
	     		<td>
		     		<s:textfield name="intransitDays" maxlength="4" cssClass="text" id="intransitDays_pop"></s:textfield>
		     		<s:text name="SFH10_day"/>
	     		</td>
	     		</tr>
     		</tbody>
     		</table>
     		<div id="tree">
     		<div class="box2-header clearfix"> 
		   		<strong id="left_tree" class="left active" style="background:#E8E8C8">
       				<span class="ui-icon icon-flag"></span>
       				<s:text name="SFH10_choiceCounter"/>
       	   		</strong>
       	   		<span class="right">
					<input id="position_left_1" class="text locationPosition ac_input" type="text" style="width: 140px;" name="locationPosition" autocomplete="off">
					<a class="search" onclick="binOLSTSFH10.locationCutPosition(this);return false;">
						<span class="ui-icon icon-position"></span>
						<span class="button-text"><s:text name="SFH10_locationPosition"></s:text></span>
					</a>
				</span>
       		</div>
			<div class="jquery_tree">
    			<div class="jquery_tree treebox_left tree" id = "treeDemo" style="overflow: auto;background:#FCFCFC;min-height:350px;height:auto"></div>
    		</div>
    		</div>
     	</div>
     </div>
     <%--==================================弹出设定柜台参数框     End============================== --%> 
     
     <%--==================================弹出设定柜台产品订货参数框Start============================== --%>
     <div id="pop_setCounter_main" style="display:none">
     	<div id="pop_setCounter_body" >
     		<div id="errorDiv_couPrt" class="actionError" style="display:none">
        		<ul>
            		<li><span id="errorSpan_couPrt"></span></li>
        		</ul>         
   		 	</div>
   		 	<table class="detail">
	   		 	<tbody>
		     		<tr>
		     		<%--最低库存天数 --%>
		     		<th><s:text name="SFH10_lowestStockDays"/></th>
		     		<td>
			     		<s:textfield name="lowestStockDays" maxlength="4" cssClass="text" id="setLowestStockDays_pop"></s:textfield>
			     		<s:text name="SFH10_day"/>
		     		</td>
		     		</tr>
		     		<tr>
		     		<%--旬环比增长系数 --%>
		     		<th><s:text name="SFH10_growthFactor"/></th>
		     		<td>
			     		<s:textfield name="growthFactor" maxlength="10" cssClass="text" id="setGrowthFactor_pop"></s:textfield>
			     		<s:text name="SFH10_percent"/>
		     		</td>
		     		</tr>
		     		<tr>
		     		<%--主推计划(调整系数) --%>
		     		<th><s:text name="SFH10_regulateFactor"/></th>
		     		<td>
			     		<s:textfield name="regulateFactor" maxlength="10" cssClass="text" id="setRegulateFactor_pop"></s:textfield>
			     		<s:text name="SFH10_percent"/>
		     		</td>
		     		</tr>
	     		</tbody>
     		</table>
     		<div id="tree">
	     			<div class="box2-header clearfix"> 
			   			<strong id="left_tree" class="left active" style="background:#E8E8C8;cursor:pointer" onclick="javascript:$(this).css('background','#E8E8C8').next().css('background','none');$('#productTree').show();$('#counterTree').hide();$('#span_product').show();$('#span_counter').hide();">
	       					<span class="ui-icon icon-flag"></span>
	       					<s:text name="SFH10_choiceProduct"/>
	       	   			</strong>
	       	   			<strong id="left_tree" class="left active" style="background:none;cursor:pointer" onclick="javascript:$(this).css('background','#E8E8C8').prev().css('background','none');$('#productTree').hide();$('#counterTree').show();$('#span_product').hide();$('#span_counter').show();">
	       					<span class="ui-icon icon-flag"></span>
	       					<s:text name="SFH10_choiceCounter"/>
	       	   			</strong>
	       	   			<span class="right" id="span_product">
							<input id="position_left_2" class="text locationPosition ac_input" type="text" style="width: 140px;" name="locationPosition" autocomplete="off">
							<a class="search" onclick="binOLSTSFH10.locationPrtPosition(this);return false;">
								<span class="ui-icon icon-position"></span>
								<span class="button-text"><s:text name="SFH10_locationPosition"></s:text></span>
							</a>
						</span>
	       	   			<span class="right" id="span_counter" style="display:none">
							<input id="position_left_3" class="text locationPosition ac_input" type="text" style="width: 140px;" name="locationPosition" autocomplete="off">
							<a class="search" onclick="binOLSTSFH10.locationCutPosition(this);return false;">
								<span class="ui-icon icon-position"></span>
								<span class="button-text"><s:text name="SFH10_locationPosition"></s:text></span>
							</a>
						</span>
	       			</div>
					<div class="jquery_tree" id="productTree">
	    				<div class="jquery_tree treebox_left tree" id = "treeDemo_product" style="overflow: auto;background:#FCFCFC;min-height:350px;height:auto"></div>
	    			</div>
					<div class="jquery_tree hide" id="counterTree">
	    				<div class="jquery_tree treebox_left tree" id = "treeDemo_counter" style="overflow: auto;background:#FCFCFC;min-height:350px;height:auto"></div>
	    			</div>
     	</div>
     </div>
     </div>
     <%--==================================弹出设定柜台产品订货参数框     End============================== --%>	  	 				 

	<%--==================================弹出设定全局订货参数框Start============================== --%>
     <div id="pop_setGlobal_main" style="display:none">
     	<div id="pop_setGlobal_body" >
     		<div id="errorDiv_global" class="actionError" style="display:none">
        		<ul>
            		<li><span id="errorSpan_global"></span></li>
        		</ul>         
   		 	</div>
   		 	<table class="detail">
	   		 	<tbody>
	   		 		<tr>
			     		<%--品牌名称--%>
			     		<th><s:text name="SFH10_brandName"/></th>
			     		<td>
		                    <s:select id="brandInfoId_Global" name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" cssStyle="width:100px;"></s:select>      	
			     		</td>
		     		</tr>
		     		<tr>
		     		<%--月销售天数 --%>
		     		<th><s:text name="SFH10_saleDaysOfMonth"/></th>
		     		<td>
			     		<s:textfield name="saleDaysOfMonth" maxlength="7" cssClass="text" id="setSaleDaysOfMonth_pop"></s:textfield>
			     		<s:text name="SFH10_day"/>
		     		</td>
		     		</tr>
		     		<tr>
		     		<%--建议发货数量 --%>
		     		<th><s:text name="SFH10_daysOfProduct"/></th>
		     		<td>
			     		<s:textfield name="daysOfProduct" maxlength="4" cssClass="text" id="setDaysOfProduct_pop"></s:textfield>
		     			<s:text name="SFH10_day"/>
		     		</td>
		     		</tr>
	     		</tbody>
     		</table>
     	</div>
     </div>
     <%--==================================弹出设定全局订货参数框     End============================== --%>
     
<div class="hide" id="dialogInit"></div>
<span id="globalSearch" style="display:none">${globalSearch_url}</span>
<span id="proSearch" style="display:none">${proSearch_url}</span>
<span id="couSearch" style="display:none">${couSearch_url}</span>
<span id="couPrtSearch" style="display:none">${couPrtSearch_url}</span>
<span id="setProductParameter" style="display:none">${setProductParameter_url}</span>
<span id="editPrtParam" style="display:none">${editPrtParam_url}</span>
<span id="setCounterParameter" style="display:none">${setCounterParameter_url}</span>
<span id="editCouParam" style="display:none">${editCouParam_url}</span>
<span id="setCouPrtParam" style="display:none">${setCouPrtParameter_url}</span>
<span id="editCouPtrParam" style="display:none">${editCouPtrParam_url}</span>
<span id="setGlobalParam" style="display:none">${setGlobalParameter_url}</span>
<span id="editGlobalParam" style="display:none">${editGlobalParam_url}</span>
<span id="issOrderParam" style="display:none">${issOrderParam_url}</span>
<span id="getTreeNodes" style="display:none">${getTreeNodes_url}</span>

<div style="display:none">
<span id="toAllProduct"><s:text name="SFH10_toAllProduct"></s:text></span>
<span id="yes"><s:text name="SFH10_yes"></s:text></span>
<span id="toAllCounter"><s:text name="SFH10_toAllCounter"></s:text></span>
<span id="toCounterPrt"><s:text name="SFH10_toCounterPrt"></s:text></span>
<span id="toAllCouPrt"><s:text name="SFH10_toAllCouPrt"></s:text></span>
<span id="noChioceCounter"><s:text name="SFH10_noChioceCounter"></s:text></span>
<span id="daysWarning"><s:text name="SFH10_daysWarning"></s:text></span>
<span id="daysDecimalWarning"><s:text name="SFH10_daysDecimalWarning"></s:text></span>
<span id="growthFactorWarning"><s:text name="SFH10_growthFactorWarning"></s:text></span>
<span id="regulateFactorWarning"><s:text name="SFH10_regulateFactorWarning"></s:text></span>
<span id="adtCoefficientWarning"><s:text name="SFH10_adtCoefficientWarning"></s:text></span>
<span id="daysOfProductWarning"><s:text name="SFH10_daysOfProductWarning"></s:text></span>
<span id="noAdtCoefficient"><s:text name="SFH10_noAdtCoefficient"></s:text></span>
<span id="noDays"><s:text name="SFH10_noDays"></s:text></span>
<span id="noDate"><s:text name="SFH10_noDate"></s:text></span>
<span id="noInputWarn"><s:text name="SFH10_noInputWarn"></s:text></span>
<span id="dateWarning"><s:text name="SFH10_dateWarning"></s:text></span>
<span id="downWarning"><s:text name="SFH10_downWarning"></s:text></span>
<span id="downTitle"><s:text name="SFH10_down"></s:text></span>
<span id="downing"><s:text name="SFH10_downing"></s:text></span>
<span id="doing"><s:text name="SFH10_doing"></s:text></span>
<span id="noChioceProduct"><s:text name="SFH10_noChioceProduct"></s:text></span>
<span id="cancel"><s:text name="SFH10_cancel"></s:text></span>
<span id="mulCounter"><s:text name="SFH10_mulCounter"></s:text></span>
<span id="mulProduct"><s:text name="SFH10_mulProduct"></s:text></span>
<span id="mulCouPrt"><s:text name="SFH10_mulCouPrt"></s:text></span>
<span id="mulGlobal"><s:text name="SFH10_mulGlobal"></s:text></span>
<span id="SFH10_product"><s:text name="SFH10_product"/></span>
<span id="SFH10_date"><s:text name="SFH10_date"/></span>
<span id="SFH10_adtCoefficient"><s:text name="SFH10_adtCoefficient"/></span>
<span id="SFH10_adtCoefficientTip"><s:text name="SFH10_adtCoefficientTip"/></span>
<span id="SFH10_counter"><s:text name="SFH10_counter"/></span>
<span id="SFH10_orderDays"><s:text name="SFH10_orderDays"/></span>
<span id="SFH10_intransitDays"><s:text name="SFH10_intransitDays"/></span>
<span id="SFH10_day"><s:text name="SFH10_day"/></span>
<span id="SFH10_lowestStockDays"><s:text name="SFH10_lowestStockDays"/></span>
<span id="SFH10_growthFactor"><s:text name="SFH10_growthFactor"/></span>
<span id="SFH10_regulateFactor"><s:text name="SFH10_regulateFactor"/></span>
<span id="SFH10_saleDaysOfMonth"><s:text name="SFH10_saleDaysOfMonth"/></span>
<span id="SFH10_daysOfProduct"><s:text name="SFH10_daysOfProduct"/></span>
<span id="SFH10_percent"><s:text name="SFH10_percent"/></span>
</div>
<div id="editDialog">
</div>
</s:i18n>
<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>

<%-- ================== 弹出datatable -- 产品共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/popProductTable.jsp" flush="true" />
<%-- ================== 弹出datatable -- 产品共通导入 START ======================= --%>

<%-- ================== 弹出datatable -- 柜台共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/pt/common/BINOLPTCOM01.jsp" flush="true" />
<%-- ================== 弹出datatable -- 柜台共通导入 START ======================= --%>

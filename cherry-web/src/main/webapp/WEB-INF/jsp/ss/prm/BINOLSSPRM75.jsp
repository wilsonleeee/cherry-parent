<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/ss/prm/BINOLSSPRM75.js?V=20161212"></script>
<s:i18n name="i18n.ss.BINOLSSPRM75">
<s:text id="selectAll" name="global.page.all"/>
	<div class="panel-header">
        <div class="clearfix">
		<span class="breadcrumb left">	    
			<jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
		</span>
		<s:url id="export" action="BINOLSSPRM75_export" ></s:url>
        <a id="downUrl" href="${export}"></a>
        <s:url id="checkExport" action="BINOLSSPRM75_checkExport" ></s:url>
        <a id="checkExportUrl" href="${checkExport}"></a>
        </div>
	</div>
	
	<%-- ================== 错误信息提示 START ======================= --%>
		<div id="errorMessage"></div>
	<%-- ================== 错误信息提示   END  ======================= --%>
	
	<div class="panel-content">
		<div class="box">
			<%-- form start --%>
        	<cherry:form id = "mainForm" class ="inline" >
				<%--查询条件 --%>
	            <div class="box-header"> <strong><span class="ui-icon icon-ttl-search"></span><s:text name="global.page.condition"/></strong><%-- <input id ="ruleValidFlag" type="checkbox" value="1" name="ruleValidFlag">包含停用规则--%></div>
	            <div class="box-content clearfix">
	            	<div class="column" style="width:49%; height:120px;">
	            		<p>
	            			<label style="width:80px;"><s:text name="CouponNo"/></label>
	            			<s:textfield name="couponNo" cssClass="text" id="couponNo"/>
	            		</p>
	                	<p>
	                  		<%-- 券状态 --%>
							<label style="width:80px;"><s:text name="CouponStatus"/></label>
							<span class="text">
								<s:select name="status"  list='#application.CodeTable.getCodes("1384")' listKey="CodeKey" listValue="Value"
								headerKey="" headerValue="%{selectAll}" />
							</span>
	                	</p>
	                	<p>
	                  		<%-- 券类型 --%>
							<label style="width:80px;"><s:text name="CouponType"/></label>
							<span class="text">
								<s:select name="couponType"  list='#application.CodeTable.getCodes("1383")' listKey="CodeKey" listValue="Value"
								headerKey="" headerValue="%{selectAll}" />
							</span>
	                	</p> 
	                	<p>
	                  		<%-- 券规则代码 --%>
							<label style="width:80px;"><s:text name="CouponRule"/></label>
	            			<s:textfield name="couponRule" cssClass="text" id="couponRule"/>
	                	</p>           		
	            	</div>
	            	<div class="column last" style="width:50%; height:50px;">
	            		<p class="date">
	                  		<%--发券时间 --%>
	                  		<label style="width:80px;"><s:text name="CouponStartTime"/></label>
	                  		<span>
	                  		<s:textfield name="startTime" cssClass="date" ></s:textfield>
	                  		</span>
	                  		-
	                  		<s:text name="CouponEndTime"/>
	                  		<span>
	                  		<%--结束日期 --%>
	                  		<s:textfield name="endTime" cssClass="date" ></s:textfield>
	                  		</span>
	                	</p>
	                	<p>
	            			<label style="width:80px;"><s:text name="RelationBill"/></label>
	            			<s:textfield name="relationBill" cssClass="text" id="relationBill"/>
	            		</p>
	            		<p>
	            			<label style="width:80px;"><s:text name="MemberBPCode"/></label>
	            			<s:textfield name="BPCode" cssClass="text" id="BPCode"/>
	            		</p>
						<p>
							<label style="width:80px;"><s:text name="CouponRuleName"/></label>
							<s:textfield name="couponRuleName" cssClass="text" id="couponRuleName"/>
						</p>
	              	</div>
	            </div>
        		<div class="clearfix">
		              <button class="right search" onclick="binolssprm75.search();return false;">
		              	<span class="ui-icon icon-search-big"></span>
		              	<span class="button-text"><s:text name="global.page.search"/></span>
		              </button>
	            </div>
        	</cherry:form>
        	<%-- form end --%>	
		</div>
		<div class="section hide" id="coupon">
		<div class="section-header"><strong><span class="icon icon-ttl-section"></span><s:text name="global.page.list"></s:text></strong></div>
		<div class="toolbar clearfix">
			<span class="left">
	           <cherry:show domId="BINOLSSPRM75EXP">
	           	    <a id="export" class="export" onclick="exportExcel();return false;">
	                   <span class="ui-icon icon-export"></span>
	                   <span class="button-text"><s:text name="global.page.export"/></span>
	                </a>
	          </cherry:show>
            </span>
			<span class="right">
		   		<%-- 列设置按钮  --%>
		   		<a class="setting">
					<span class="ui-icon icon-setting"></span>
					<span class="button-text"><s:text name="global.page.colSetting"/></span>
				</a>
		   	</span>
		</div>
		<div class="section-content">
			<table cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%" id="couponDataTable">
				 <thead>
				 	 <tr>
				 	  	<th><s:text name="CouponNo"/></th>
         				<th><s:text name="CouponType"/></th>
          				<th><s:text name="CouponRule"/></th>
          				<th><s:text name="CouponCode"/></th>
          				<th><s:text name="MemberBPCode"/></th>
          				<th><s:text name="MemberCode"/></th>
          				<th><s:text name="MemberMobile"/></th>
         			 	<th><s:text name="CouponStartTime"/></th>
         				<th><s:text name="CouponEndTime"/></th>
         				<th><s:text name="CouponCreateTime"/></th>
         			 	<th><s:text name="RelationBill"/></th>
         			 	<th><s:text name="DepartCode"/></th>
         			 	<th><s:text name="DepartName"/></th>
         				<th><s:text name="CouponStatus"/></th>			
         			 	<th><s:text name="CouponValidFlag"/></th>
          				<th><s:text name="Operator"/></th>
          			</tr>
         		</thead>
         		<tbody>
         		</tbody> 
			</table>
		</div>
		</div>
		<%-- <div class="section hide" id="couponUsed">
		<div class="section-header"><strong><span class="icon icon-ttl-section"></span><s:text name="global.page.list"></s:text></strong></div>
		<div class="section-content">
			<table cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%" id="couponUsedDataTable">
				 <thead>
				 	 <tr>
				 	  	<th><s:text name="CouponNo"/></th>
				 	  	<th><s:text name="CouponStatus"/></th> 
         				<th><s:text name="CouponType"/></th>
          				<th><s:text name="CouponRule"/></th>
          				<th><s:text name="CouponCode"/></th>
          				<th><s:text name="MemberCode"/></th>
          				<th><s:text name="MemberName"/></th>
         			 	<th><s:text name="MemberBPCode"/></th>
         			 	<th><s:text name="MemberUseBPCode"/></th>
         			 	<th><s:text name="UseTime"/></th>
						<th><s:text name="RelationBill"/></th>
         			 	<th><s:text name="BillCode"/></th>				
						<th><s:text name="CountName"/></th>
          				<th><s:text name="Operator"/></th>
          			</tr>
         		</thead>
         		<tbody>
         		</tbody> 
			</table>
		</div>
		</div> --%>
		</div>  
</s:i18n>
	<div class="dialog2 clearfix" style="display:none" id="stop_coupon_dialog">
		<p class="clearfix message">
			<span></span>
			<img height="15px" class="hide" src="/Cherry/css/cherry/img/loading.gif"/>
		</p>
	</div>
<s:url action="BINOLSSPRM75_search" id="coupon_Url"></s:url>
<a href="${coupon_Url}" id="couponUrl"></a>
<s:url action="BINOLSSPRM75_sendMsg" id="send_msg" namespace="/ss"></s:url>
<a href="${send_msg}" id="sendUrl"></a>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />

<div class="dialog2 clearfix" style="display:none" id="send_msg_dialog">
	<p class="clearfix message">
		<span></span>
		<img height="15px" class="hide" src="/Cherry/css/cherry/img/loading.gif"/>
	</p>
</div>
<div class="dialog2 clearfix" style="display:none" id="send_result_dialog">
	<p class="clearfix message">
		<span></span>
	</p>
</div>

<div class="dialog2 clearfix" style="display:none" id="export_dialog">
	<p class="clearfix message">
		<span></span>
	</p>
</div>
	  
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/cp/act/BINOLCPACT06_6.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript">
//节日
var holidays = '${holidays }';
$('#startDate').cherryDate({
	holidayObj: holidays,
	beforeShow: function(input){
		var value = $('#endDate').val();
		return [value,'maxDate'];
	}
});
$('#endDate').cherryDate({
	holidayObj: holidays,
	beforeShow: function(input){
		var value = $('#startDate').val();
		return [value,'minDate'];
	}
});
$('#sendStartDate').cherryDate({
	holidayObj: holidays,
	beforeShow: function(input){
		var value = $('#sendEndDate').val();
		return [value,'maxDate'];
	}
});
$('#sendEndDate').cherryDate({
	holidayObj: holidays,
	beforeShow: function(input){
		var value = $('#sendStartDate').val();
		return [value,'minDate'];
	}
});
</script>
<s:i18n name="i18n.cp.BINOLCPACT06">
<s:text id="selectAll" name="global.page.all"/>
<div class="crm_content_header">
	<span class="icon_crmnav"></span>
	<span>
		<s:text name="ACT06_campRun" />
		&gt;
		<s:text name="ACT06_actDispatchTitle" />
	</span>
</div>
<div id="actionMsgDiv"></div>
<div id="messageDialogDiv" class="hide ui-dialog-content ui-widget-content" style="display: none; width: auto; min-height: 200px;">
	<p id="messageContent" class="message hide" style="margin:40px auto 30px;"><span id="messageContentSpan"></span></p>
	<p id="successContent" class="success hide" style="margin:40px auto 30px;"><span id="successContentSpan"></span></p>
	<p class="center">
		<button id="btnMessageConfirm" class="close" type="button">
			<span class="ui-icon icon-confirm"></span>
			<span class="button-text">确定</span>
		</button>
	</p>
</div>
 <div id="div_main" class="panel-content">
     <div class="section">
        <form id="mainForm" method="post" class="inline" >
        <input type="hidden" name="campaignCode" id="campaignCode" value='<s:property value="campaignInfo.campaignCode"/>'/>
        <%--<input type="hidden" name="state" value="<s:property value='state'/>"/>--%>
        <%--<input type="hidden" name="newState" value="<s:property value='newState'/>"/>--%>
    	<div class="box">
         <div class="box-header"><span class="ui-icon icon-ttl-search"></span><strong><s:text name="ACT06_searcCondition"/></strong></div>
	         <div class="box-content clearfix">
		         <div class="column" style="width:50%;height:120px;">
		             <p class="clearfix CONDISOIN">
		              <label style="width:80px;"><s:text name="ACT06_tradeNoIF"/></label>
		           	  <span><input id="tradeNoIF"  class="text" name="tradeNoIF"></span>
		             </p>
					 <p class="clearfix CONDISOIN">
		              <label style="width:80px;"><s:text name="ACT06_memCode"/></label>
		           	  <span><input id="memCode"  class="text" name="memCode"></span>
		             </p>
		            <p class="clearfix CONDISOIN">
		               <label style="width:80px;"><s:text name="ACT06_mobile"/></label>
		           	  <span><input id="mobile"  class="text" name="mobile"></span>
				    </p>
					 <p class="clearfix CONDISOIN">
						 <label style="width:80px;"><s:text name="ACT06_state"/></label>
		      		 	<span><s:select name="state" list='#application.CodeTable.getCodes("1422")' listKey="CodeKey" listValue="Value"
										headerKey="" headerValue="%{selectAll}" value="'1'" cssStyle="width:185px;"/></span>
					 </p>
		          </div>
		          <div class="column last" style="width:49%;height:120px;">  
		           	<p class="clearfix CONDISOIN">
               			 <label style="width:80px;"><s:text name="ACT06_orderTimeDate"/></label>
                 		<span><s:textfield name="startDate" cssClass="date"/></span> ~ <span><s:textfield name="endDate" cssClass="date"/></span>
	           		 </p>
		            <p class="clearfix CONDISOIN">
		                <label style="width:80px;"><s:text name="ACT06_ordercountCode"/></label>
						<span><input id="counterOrder"  class="text" name="counterOrder"></span>
		            </p>
					  <p class="clearfix CONDISOIN">
						  <label style="width:80px;"><s:text name="ACT06_testType"/></label>
		      		 	<span><s:select name="testType" list='#application.CodeTable.getCodes("1256")' listKey="CodeKey" listValue="Value"
										headerKey="" headerValue="%{selectAll}" value="'1'" cssStyle="width:185px;"/></span>
					  </p>
					  <p class="clearfix CONDISOIN">
						  <label style="width:80px;"><s:text name="ACT06_dispatchTime"/></label>
						  <span><s:textfield name="sendStartDate" cssClass="date"/></span> ~ <span><s:textfield name="sendEndDate" cssClass="date"/></span>
					  </p>
		          </div>
	          </div>
            <p class="clearfix">
            	<s:url id="option_url" action="BINOLCPACT06_optionRun2">
            	</s:url>
            	<s:url id="conditon_url" action="BINOLCPACT06_6_getConditon">
            		<s:param name="campOrderId"><s:property value=''/></s:param>
            	</s:url>
           		<button onclick="BINOLCPACT06_6.search();return false;" class="right search">
           			<span class="ui-icon icon-search-big"></span>
           			<span class="button-text"><s:text name="ACT06_search"/></span>
              	</button>
            </p>
        </div>
        </form>
        </div>
         <div id="section" class="section hide" >
       	<div class="section-header" id="section-header">
        	<%--查询结果一览 --%>
			<strong>
          		<span class="ui-icon icon-ttl-section-search-result"></span>
          		<s:text name="ACT06_searchResult"/>
          	</strong>
        </div>
		<div class="ui-tabs-panel">
	        	<div class="toolbar clearfix">
				 	<span class="right"> <%-- 设置列 --%>
						<a class="setting"> 
							<span class="ui-icon icon-setting"></span> 
							<span class="button-text"><s:text name="global.page.colSetting" /></span> 
						</a>
					</span>
			 	</div>
        <div class="section" id="result_list">
	            <table id="dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table2" width="100%">
	              	<thead>
		                <tr>
		                	<th>NO.</th>
		            		<%--单据号--%>
			                <th><s:text name="ACT06_tradeNoIF"/><span class="ui-icon ui-icon-document"></span></th>
							<%--姓名--%>
							<th><s:text name="ACT06_dispatchName"/></th>
							<%--会员手机--%>
							<th><s:text name="ACT06_mobile"/></th>
							<%--会员微信--%>
							<th><s:text name="ACT06_messageId"/></th>
							<%--会员类型--%>
							<th><s:text name="ACT06_testType"/></th>
							<%--预约柜台--%>
							<th><s:text name="ACT06_ordercountCode"/></th>
							<%--发货时间--%>
							<th><s:text name="ACT06_dispatchTime"/></th>
							<%--预约时间--%>
							<th><s:text name="ACT06_campOrderTime"/></th>
							<%--总数量--%>
							<th><s:text name="ACT06_quantity"/></th>
							<%--总金额--%>
							<th><s:text name="ACT06_amout"/></th>
							<%--总金额--%>
							<th><s:text name="ACT06_state"/></th>
			                <%--数据来源--%>
			                <th><s:text name="ACT06_dataChannel"/></th>
			                <%--操作--%>
			                <th><s:text name="global.page.option"/></th>
		                </tr>
	              	</thead>
	            </table>
          </div>
       </div>
      </div>
      </div>
      <div id="popPrtDispatchTable"></div>
      <div class="hide" id="confirmInitDIV"></div>
<div class="hide">
    <div id="dialogClose"><s:text name="global.page.close" /></div>
	<div id="dialogTitle"><s:text name="ACT06_prtDetail" /></div>
	<div id="dialogOk"><s:text name="global.page.ok" /></div>
	<div id="confirmTitle"><s:text name="ACT06_confirmTitle" /></div>
	<div id="campBillNo"><s:text name="ACT06_tradeNoIF" /></div>
	<%--<div id="confirmMsg"><p class="message"><span><s:text name="ACT06_confirmMsg"/></span></p></div>--%>
	<div id="dialogConfirm"><s:text name="global.page.ok" /></div>
    <div id="dialogCancel"><s:text name="global.page.cancle" /></div>
    <div id="conditionTip"><s:text name="ACT06_searcCondition"/></div>
    <div id="optionBillNo"><s:text name="ACT06_optionBillNo"/></div>
	<s:url id="searchDispatch_url" action="BINOLCPACT06_dispatchSearch"/>
	<input type="hidden" id="campOrderDispathUrl" value="${searchDispatch_url}" />
	<input type="hidden" id="option_url" value="${option_url}" />
</div>
<div class="hide" id="confirmInitBody">
 <div class="actionError box4" id="errorDiv" style="color:#000;">
	<ul>
	  <li><span id="errorSpan">
	  <s:text name="ACT06_messageTip"/>
	  <s:if test='"AR".equals(newState)'>
	  <span style="font-size:15px;color:#ff6d06;" > <s:text name="ACT06_shipTip"/></span>
	  </s:if><s:elseif test='"CA".equals(newState)'>
	  <span style="font-size:15px;color:#ff6d06;"><s:text name="ACT06_cancelResTip"/></span>
	  </s:elseif><s:text name="ACT06_optionTip"/></span>
	  </li>
	</ul>			
 </div>
 <div style="margin-top:0px;" class="box4">
   <div class="box4-header"> <strong><span style="color:#CC6600;font-size:13px;" id="messageCondition"><s:text name="ACT06_searcCondition"/></span></strong></div>
     <div class="box4-content clearfix" id="conditDiv"></div>
 </div>
 <div class="clearfix" style="margin-top:20px;">
	<span style="margin:4px 0 0 5px;" class="left"> <span class="highlight">※</span>
		<s:text name="ACT06_billNoCount"/>
	</span>
	<span class="left hide" style="margin-left:10px; display: inline;"> 
	 <s:text name="ACT06_totalCount"/>&nbsp;<strong><span id="count" style="font-size:16px;" class="green">1</span></strong>&nbsp;<s:text name="ACT06_totalRecord"/>
     <input type="hidden" name="countId" id="countId" value="1"/>
	</span>  
</div>
</div>
<div class="hide" id="conditMsgDIV">
<div id="confirmMsg"><p class="message"><span><s:text name="ACT06_confirmMsg"/></span></p></div>
</div>
<div class="dialog2 clearfix" style="display:none" id="update_bill_dialog">
	<span id="update_bill_dialog_title" style="display: none"><s:text name="ACT06_confirmTitle"/></span>
	<span id ="global_page_ok" style="display:none"><s:text name="global.page.ok"/></span>
	<%--<p class="clearfix"><span>预约单号:</span><span id="billNo"></span></p>--%>
	<p class="hide"><span>预约单号:</span><span id="billNo"></span></p>
	<p class="clearfix"><span><s:text name="ACT06_receiverName"/>：</span><span id="billName"></span></p>
	<p class="clearfix"><span><s:text name="ACT06_receiverMobile"/>：</span><span id="billMobile"></span></p>
	<p class="clearfix"><span><s:text name="ACT06_deliveryProvince"/>：</span><span id="billProvince"></span></p>
	<p class="clearfix"><span><s:text name="ACT06_deliveryCity"/>：</span><span id="billCity"></span></p>
	<p class="clearfix"><span><s:text name="ACT06_deliveryCounty"/>：</span><span id="billCounty"></span></p>
	<p class="clearfix"><span><s:text name="ACT06_deliveryAddress"/>：</span><span id="billAddress"></span></p>
	<p class="clearfix"><span><s:text name="ACT06_expressCode"/>：</span>
		<span>
			<s:select name="expressCode" list='#application.CodeTable.getCodes("1421")' listKey="CodeKey" listValue="Value"
						headerKey="" headerValue="" value="" cssStyle="width:185px;"/>
		</span>
	</p>
	<p class="clearfix"><span>快递单号:</span><span><input type="text" name="expressNo" id="expressNo" style="width:185px;margin: 0 0.5em 0.5em 0.8em;"/></span></p>
	<img height="15px" class="hide" src="/Cherry/css/cherry/img/loading.gif"/>
</div>

</s:i18n>
<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>

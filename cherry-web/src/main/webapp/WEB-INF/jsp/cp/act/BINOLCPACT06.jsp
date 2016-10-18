<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/cp/act/BINOLCPACT06.js"></script>
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
</script>
<s:i18n name="i18n.cp.BINOLCPACT06">
<s:url id="Excel_url" value="BINOLCPACT06_export"/>
<s:url id="CouPon_url" value="BINOLCPACT06_couPonExport"/>
<s:text id="selectAll" name="global.page.all"/>
<div class="crm_content_header">
	<span class="icon_crmnav"></span>
	<span><s:text name="ACT06_campResult" /> &gt; <s:text name="ACT06_detail" /> </span>
</div>
 <div id="errorMessage"></div>
 <div id="actionResultDisplay"></div>
 <div id="div_main" class="panel-content">
       <div class="section">
        <form id="mainForm" method="post" class="inline" >
        <input type="hidden" name="campaignCode" id="campaignCode" value='<s:property value="campaignInfo.campaignCode"/>'/>
    	<div class="box">
         <div class="box-header"><span class="ui-icon icon-ttl-search"></span><strong><s:text name="ACT06_searcCondition"/></strong></div>
        
         <div class="box-content clearfix">
         <div class="column" style="width:50%;">
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
            	<label style="width:80px;"><s:text name="ACT06_testType"/></label>
      		 	<s:select name="testType" list='#application.CodeTable.getCodes("1256")' listKey="CodeKey" listValue="Value"
               	headerKey="" headerValue="%{selectAll}" value="'1'"  cssStyle="width:185px;"/>
            </p>
            <p class="clearfix CONDISOIN">
               	<label style="width:80px;"><s:text name="ACT06_state"/></label>
               	<s:select name="state" list='#application.CodeTable.getCodes("1116")' listKey="CodeKey" listValue="Value"
               	headerKey="" headerValue="%{selectAll}" value="''"  cssStyle="width:185px;"/>
            </p>
             <p class="clearfix CONDISOIN">
           	  <label style="width:80px;"><s:text name="ACT06_sendFlag"/></label>
      		 	<select id="sendFlag" name="sendFlag" style="width:185px;">
				    <option value=""><s:text name="ACT06_selectAll"/></option>
				    <option value="0"><s:text name="ACT06_sendFlag0"/></option>
				    <option value="1"><s:text name="ACT06_sendFlag1"/></option>
				    <option value="2"><s:text name="ACT06_sendFlag2"/></option>
				</select>
            </p>
          </div>
          <div class="column last" style="width:49%;">  
       	  	<p class="clearfix CONDISOIN">
                <label style="width:80px;"><s:text name="ACT06_orderTimeDate"/></label>
                 <span><s:textfield name="startDate" cssClass="date"/></span> - <span><s:textfield name="endDate" cssClass="date"/></span>
            </p>
           <p class="clearfix CONDISOIN">
                <label style="width:80px;"><s:text name="ACT06_ordercountCode"/></label>
				<input id="counterOrder"  class="text" name="counterOrder">
            </p>
            <p class="clearfix CONDISOIN">
                <label style="width:80px;"><s:text name="ACT06_counterGot"/></label>
				<input id="counterGot"  class="text" name="counterGot">
            </p>
            <p class="clearfix CONDISOIN">
                <label style="width:80px;"><s:text name="ACT06_counterBelong"/></label>
				<input id="counterBelong"  class="text" name="counterBelong">
            </p>
             <p class="clearfix CONDISOIN">
                <label style="width:80px;"><s:text name="ACT06_couponCode"/></label>
				<input id="couponCode"  class="text" name="couponCode">
            </p>
            </div>
            </div>
            <p class="clearfix">
           		 <s:url id="batchInit_url" action="BINOLCPACT06_batchUpdateInit">
           		 </s:url>
           		 <s:url id="batchUpdate_url" action="BINOLCPACT06_batchUpdate">
           		 </s:url>
             	<button onclick="BINOLCPACT06.editInit('${batchUpdate_url}','${batchInit_url}');return false;" class="right search">
           				<span class="ui-icon icon-edit-big"></span>
          				<span class="button-text"><s:text name="ACT06_batchButton"/></span>
              	</button>
              	<button onclick="BINOLCPACT06.search('${campOrder_url}');return false;" class="right search">
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
					<cherry:show domId="BINOLCPACT06EXP">
					<a id="exportPointInfo" class="export left" onclick="BINOLCPACT06.exportExcel('${Excel_url}');return false;">
				     <span class="ui-icon icon-export"></span>
				     <span class="button-text"><s:text name="ACT06_excelExport"/></span>
			 		</a>
			 		</cherry:show>
			 		<cherry:show domId="BINOLCPACT06COU">
						<s:if test='"CXHD".equals(campaignInfo.campaignType)'>
							<a id="exportCouponInfo" class="export left" onclick="BINOLCPACT06.exportExcel('${CouPon_url}');return false;">
						     <span class="ui-icon icon-export"></span>
						     <span class="button-text"><s:text name="ACT06_coupon"/></span>
					 		</a>
				 		</s:if>		
			 		</cherry:show>
			 	</div>	
        <div class="section" id="result_list">
	            <table id="dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table2" width="100%">
	              	<thead>
		                <tr>
		                	<th>NO.</th>
		            		<%--单据号--%>
			                <th><s:text name="ACT06_tradeNoIF"/><span class="ui-icon ui-icon-document"></span></th>
			            	<%--会员卡号--%>
			                <th><s:text name="ACT06_memNameCode"/></th> 
			                <%--会员手机--%>
			                <th><s:text name="ACT06_mobile"/></th>
			                 <%--会员微信号--%>
			                <th><s:text name="ACT06_messageId"/></th>
			                <%--预约柜台--%>
			                <th><s:text name="ACT06_ordercountCode"/></th>
			                <%--领取柜台--%>
			                <th><s:text name="ACT06_counterGot"/></th>
			                <%--CouponCode--%>
			                <th><s:text name="ACT06_couponCode"/></th>
			                <%--领取开始日期 --%>
			                <th><s:text name="ACT06_getFromTime"/></th>
			                <%--领取结束日期 --%>
			                <th><s:text name="ACT06_getToTime"/></th>
			                <%--预约时间--%>
			                <th><s:text name="ACT06_campOrderTime"/></th>
			                <%--服务日期--%>
			                <th><s:text name="ACT06_bookDate"/></th> 
			                <%--服务时间段--%>
			                <th><s:text name="ACT06_bookTimeRange"/></th>
			                 <%--服务日期--%>
			                <th><s:text name="ACT06_cancelTime"/></th> 
			                <%--服务时间段--%>
			                <th><s:text name="ACT06_finishTime"/></th>
			                <%--总数量--%>
			                <th><s:text name="ACT06_quantity"/></th>
			                <%--总金额--%>
			                <th><s:text name="ACT06_amout"/></th>
			                <%--总金额--%>
			                <th><s:text name="ACT06_pointRequired"/></th>
			                <%--活动状态--%>
			                <th><s:text name="ACT06_state"/></th>
			                <%--测试区分--%>
			                <th><s:text name="ACT06_testType"/></th>
			                <%--数据来源--%>
			                <th><s:text name="ACT06_dataChannel"/></th>
			                <%--下发区分--%>
			                <th><s:text name="ACT06_sendFlag"/></th>
		                </tr>
	              	</thead>
	            </table>
          </div>
       </div>
      </div>
      </div>
<div id="popPrtTable"></div>
<div  id="confirmInitDIV"></div>
<div class="hide">
    <div id="dialogClose"><s:text name="global.page.close" /></div>
    <div id="dialogOk"><s:text name="global.page.save" /></div>
   	<div id="editConfirmTitle"><s:text name="ACT06_editConfirmTitle" /></div>
	<div id="dialogTitle"><s:text name="ACT06_results_list" /></div>
	<div id="dialogInitMessage">
	<img id="loading" src="/Cherry/css/cherry/img/loading.gif" height="15px">
	<s:text name="ACT06_messageInit" />
	</div>
	<s:url id="campOrder_url" action="BINOLCPACT06_search"/>
	<input type="hidden" id="campOrderUrl" value="${campOrder_url}" />
</div>
</s:i18n>
<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>

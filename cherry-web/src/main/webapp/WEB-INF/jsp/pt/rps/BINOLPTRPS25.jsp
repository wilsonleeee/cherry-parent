<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>

<script type="text/javascript" src="/Cherry/js/common/departBar.js"></script>
<script type="text/javascript" src="/Cherry/js/pt/rps/BINOLPTRPS25.js"></script>
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
<%--在途库存记录查询URL --%>
<s:url id="search_url" value="/pt/BINOLPTRPS25_search"/>
<input type="hidden" value="${search_url}" id="search_url"/>
<s:url id="export_url" action="BINOLPTRPS25_export"/>
<s:i18n name="i18n.pt.BINOLPTRPS25">
<div class="panel-header">
       <div class="clearfix"> 
	        <span class="breadcrumb left">	    
				<jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
			</span>  
       </div>
</div>
<%-- ================== 错误信息提示 START ======================= --%>
<div id="errorMessage"></div>
<%-- ================== 错误信息提示   END  ======================= --%>
<div class="panel-content">
	<div class="box">
		<cherry:form id="mainForm" class="inline">
			<div class="box-header">
           		<strong>
           			<span class="ui-icon icon-ttl-search"></span><s:text name="global.page.condition"/>
           		</strong>	
            </div>
            <div class="box-content clearfix">
               	<div class="column" style="width:50%;">
	                <p>
	               		<%-- 单据号 --%>
	                  	<label><s:text name="RPS25_deliverNoIF"/></label>
	                  	<s:textfield name="deliverNoIF" cssClass="text" onblur="ignoreCondition(this);return false;"/>
	                </p>
	                 <p>
	               		<%-- 产品名称 --%>
	                  	<label><s:text name="RPS25_prtName"/></label>
	                  	<s:textfield name="nameTotal" cssClass="text"/>
	                  	<input type="hidden" id="prtVendorId" name="prtVendorId" value=""/>
	                </p>
	                <P><%-- 收货部门 --%>
                        <label class="left"><s:text name="RPS25_receiveDepart"/></label>
                        <table class="all_clean left"><tbody id="inOrganization_ID"></tbody></table>
                        <%--<input type="hidden" name="inOrganizationId" id="inOrganizationId" value=''>--%>
                        <%--<span id="departNameReceive" ></span>--%>
                        <a class="add" onclick="openDepartBox();">
                            <span class="ui-icon icon-search"></span>
                            <span class="button-text"><s:text name="global.page.Popselect"/></span>
                        </a>
                    </P>
        		</div>
        		<div class="column last" style="width:49%;">
        		 	<p id="dateCover" class="date">
	                	<%-- 日期范围 --%>
	                  	<label><s:text name="RPS25_date"/></label>
	                  	<span><s:textfield id="startDate" name="startDate" cssClass="date"/></span>
	                  	- 
	                  	<span><s:textfield id="endDate" name="endDate" cssClass="date"/></span>
	                </p>     
	            </div>
	          <%-- ======================= 组织联动共通导入开始  ============================= --%>
           	  <s:action name="BINOLCM13_query" namespace="/common" executeResult="true">
           	  		<s:param name="businessType">1</s:param>
           	  		<s:param name="operationType">1</s:param>
           	  </s:action>
           	  <%-- ======================= 组织联动共通导入结束  ============================= --%>
              </div>
              <p class="clearfix">
       			<%-- 查询 --%>
       			<button class="right search" type="button" onclick="search('<s:property value="#search_url"/>')">
       				<span class="ui-icon icon-search-big"></span>
       				<span class="button-text"><s:text name="global.page.search"/></span>
       			</button>
          		</p>
		</cherry:form>
	</div>
	<%-- ====================== 结果一览开始 ====================== --%>
	<div id="section" class="section hide">
       	<div class="section-header">
       		<strong>
       			<span class="ui-icon icon-ttl-section-search-result"></span>
       			<s:text name="global.page.list"/>
      		</strong>
       	</div>
        <div class="section-content">
           	<div class="toolbar clearfix">
                <cherry:show domId="BINOLPTRPS25EXP">
                    <a  class="export left" onclick="javascript:BINOLPTRPS25.exportExcel(this);return false;"  href="${export_url}">
                        <span class="ui-icon icon-export"></span>
                        <span class="button-text"><s:text name="global.page.export"></s:text></span>
                    </a>
                </cherry:show>
           		<span id="headInfo" style=""></span>
           		<span class="right">
           			<%-- 设置列 --%>
           			<a class="setting">
           				<span class="ui-icon icon-setting"></span>
           				<span class="button-text"><s:text name="global.page.colSetting"/></span>
           			</a>
           		</span>
           	</div>
            <table id="dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
              	<thead>
	                <tr>
	                	<%-- No. --%>	
	                  	<th><s:text name="RPS25_num"/></th>
	                  	<%-- 单据号 --%>
	                  	<th><s:text name="RPS25_deliverNoIF"/><span class="ui-icon ui-icon-document"></span></th>
	                  	<%-- 发货部门 --%>
	                  	<th><s:text name="RPS25_sendDepart"/></th>
	                  	<%-- 发货仓库 --%>
	                  	<th><s:text name="RPS25_depotName"/></th>
	                  	<%-- 接收部门 --%>
	                  	<th><s:text name="RPS25_receiveDepart"/></th>
	                  	<%-- 数量 --%>
	                  	<th><s:text name="RPS25_quantity"/></th>
	                  	<%-- 金额  --%>
	                  	<th><s:text name="RPS25_amount"/></th>
	                  	<%-- 日期 --%>
	                  	<th><s:text name="RPS25_date"/></th>
	                  	<%-- 操作员 --%>
	                  	<th><s:text name="RPS25_employeeName"/></th>
	                </tr>
              	</thead>
            </table>
         	</div>
       </div>
       <%-- ====================== 结果一览结束 ====================== --%>
</div>
</s:i18n>
<input type="hidden"  id="defStartDate" value=''/>
<input type="hidden"  id="defEndDate"	value=''/>   
<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>
<%-- ================== 弹出datatable -- 部门共通START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/popDepartTable.jsp" flush="true" />
<%-- ================== 弹出datatable -- 部门共通START ======================= --%>
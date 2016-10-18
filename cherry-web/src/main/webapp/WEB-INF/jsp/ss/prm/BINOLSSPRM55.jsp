<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<link rel="stylesheet" href="/Cherry/css/cherry/combobox.css" type="text/css">
<script type="text/javascript" src="/Cherry/js/common/departBar.js"></script>
<script type="text/javascript" src="/Cherry/js/ss/prm/BINOLSSPRM55.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript">
	// 节日
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

	function research(){
		var url = $("#s_search").html();
		search(url);
	}
</script>
<%-- 调拨记录查询URL --%>
<s:url id="search_url" value="BINOLSSPRM55_search"/>
<div class="hide">
	<a id="searchUrl" href="${search_url}"></a>
	<s:text name="PRM18_select" id="defVal"/>
	<div id="PRM18_select">${defVal}</div>
</div>
<s:i18n name="i18n.ss.BINOLSSPRM55">
<div id="selectAll" class="hide"><s:text name="global.page.all"/></div>
<s:text name="global.page.all" id="PRM55_selectAll"/>
<div class="panel-header">
     	<%-- ###调拨记录查询 --%>
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
		 <input type="hidden" id="currentUnitID" name="currentUnitID" value="BINOLSSPRM55"/>
			<div class="box-header">
           		<strong>
           			<span class="ui-icon icon-ttl-search"></span>
           			<s:text name="global.page.condition"/>
           		</strong>
               </div>
               <div class="box-content clearfix">
               	<div class="column" style="width:49%;height:94px;">
	                <div class="clearfix" style="line-height:25px;margin-bottom:6px;">
	               		<%-- 调拨单号 --%>
	                  	<label><s:text name="PRM55_allocationNo"/></label>
	                  	<s:textfield name="allocationNo" cssClass="text" onblur="ignoreCondition(this);return false;"/>
	                </div>	             
                    <div class="clearfix" style="line-height:25px;">
                        <%-- 促销品名称 --%>
                        <label class="left"><s:text name="PRM55_promotionProductName"/></label>
                        <%--<s:hidden name="prmVendorId" id="prmVendorId"/>--%>
                        <%--<s:textfield name="promotionProductName" cssClass="text" maxlength="30"/>--%>
                        <input id="BIN_BrandInfoID" type="hidden" value='<s:property value="#session.userinfo.BIN_BrandInfoID"/>'></input>
                        <table class="all_clean left"><tbody id="promotion_ID"></tbody></table>
                        <a class="add" onclick="BINOLSSPRM55.openPrmPopup();">
                            <span class="ui-icon icon-search"></span>
                            <span class="button-text"><s:text name="global.page.Popselect"/></span>
                        </a>
                    </div>
        		</div>
        		<div class="column last" style="width:50%;">
        			<p id="dateCover" class="date">
	                	<%-- 日期范围 --%>
	                  	<label><s:text name="PRM55_date"/></label>
	                  	<span><s:textfield id="startDate" name="startDate" cssClass="date"/></span>
	                  	- 
	                  	<span><s:textfield id="endDate" name="endDate" cssClass="date"/></span>
	                </p>
                    <p>
                        <%-- 审核状态 --%>
                        <label><s:text name="PRM55_verifiedFlag"/></label>
                        <s:select name="verifiedFlag" list="#application.CodeTable.getCodes('1007')"
                            listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{PRM55_selectAll}"/>
                    </p>     
                    <p>
                        <%-- 处理状态 --%>
                        <label><s:text name="PRM55_tradeStatus"/></label>
                        <s:select name="tradeStatus" list="#application.CodeTable.getCodes('1186')"
                            listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{PRM55_selectAll}"/>
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
             	<button class="right search" type="button" onclick="search()">
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
           	    <span id="headInfo" style=""></span>
           		<span class="left">           		
            		</span> 
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
	                  	<th><s:text name="PRM55_num"/></th>
	                  	<%-- 调拨单号 --%>
	                  	<th><s:text name="PRM55_allocationNo"/><span class="ui-icon ui-icon-document"></span></th>
						<%-- 调拨申请部门   --%>
	                  	<th><s:text name="PRM55_sendOrg"/></th>
	                  	<%-- 调拨接受部门 --%>
	                  	<th><s:text name="PRM55_receiveOrg"/></th>
	                  	<%-- 总数量 --%>
	                  	<th><s:text name="PRM55_totalQuantity"/></th>
	                  	<%-- 总金额  --%>
	                  	<th><s:text name="PRM55_totalAmount"/></th>
	                  	<%-- 调拨日期 --%>
	                  	<th><s:text name="PRM55_date"/></th>
	                  	<%-- 审核状态 --%>
	                  	<th><s:text name="PRM55_verifiedFlag"/></th>
	                  	<%-- 审核状态 --%>
	                  	<th><s:text name="PRM55_tradeStatus"/></th>
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
<%-- ====================== 结果一览结束 ====================== --%>
<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>
<%-- ============ 弹出dataTable 促销产品共通导入 START ================= --%>
<%--<jsp:include page="/WEB-INF/jsp/common/prmProductTable.jsp" flush="true" />--%>
<%-- ============ 弹出dataTable 促销产品共通导入 END =================== --%>
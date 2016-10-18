<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/common/departBar.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/ss/prm/BINOLSSPRM41.js"></script>
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
</script>
<%-- 取得仓库URL --%>
<s:url id="getInventUrl" value="/common/BINOLCM00_queryInventory"/>
<%-- 退库单查询URL --%>
<s:url id="search_url" value="/ss/BINOLSSPRM41_search"/>

<s:i18n name="i18n.ss.BINOLSSPRM41">
<div id="PRM41_select" class="hide"><s:text name="global.page.all"/></div>
<div class="panel-header">
     	<%-- ###退库单查询 --%>
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
           			<span class="ui-icon icon-ttl-search"></span>
           			<s:text name="global.page.condition"/>
           		</strong>
               </div>
               <div class="box-content clearfix">
               	<div class="column" style="width:50%; height: 85px;">
	                <p>
	               		<%-- 退库单号 --%>
	                  	<label><s:text name="PRM41_returnNo"/></label>
	                  	<s:textfield name="returnNo" cssClass="text" onblur="ignoreCondition(this);return false;"/>
	                </p>
                    <p>
                        <%-- 促销品名称 --%>
                        <label class="left"><s:text name="PRM41_promotionProductName"/></label>
                        <%--<s:hidden name="prmVendorId"/>--%>
                        <%--<s:textfield name="nameTotal" cssClass="text" maxlength="30"/>--%>
                        <input id="BIN_BrandInfoID" type="hidden" value='<s:property value="#session.userinfo.BIN_BrandInfoID"/>'></input>
                        <table class="all_clean left"><tbody id="promotion_ID"></tbody></table>
                        <a class="add" onclick="BINOLSSPRM41.openPrmPopup();">
                            <span class="ui-icon icon-search"></span>
                            <span class="button-text"><s:text name="global.page.Popselect"/></span>
                        </a>
                    </p>
        		</div>
        		<div class="column last" style="width:49%; height: 55px;">
        		 	<p id="dateCover"  class="date">
	                	<%-- 退库日期  --%>
	                  	<label><s:text name="PRM41_date"/></label>
	                  	<span><s:textfield id="startDate" name="startDate" cssClass="date"/></span>
	                  	- 
	                  	<span><s:textfield id="endDate" name="endDate" cssClass="date"/></span>
	                </p>
	               	<p>
	                	<%-- 业务类型  --%>
	                  	<label><s:text name="PRM41_tradeType"/></label>
	                  	<s:text name="global.page.all" id="PRM41_selectAll"/>
	                  	<s:select name="tradeType" list="#application.CodeTable.getCodes('1037')"
               				listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{PRM41_selectAll}"/>
	                </p> 
	                 <p>
	                	<%-- 审核状态 --%>
	                  	<label><s:text name="PRM41_verifiedFlag"/></label>
	                  	<s:select name="verifiedFlag" list="#application.CodeTable.getCodes('1007')"
               				listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{PRM41_selectAll}"/>
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
             		<%--	<cherry:show domId="SSPRM0741QUERY">--%>
             			<%-- 查询 --%>
             			<button class="right search" type="button" onclick="search('<s:property value="#search_url"/>')">
             				<span class="ui-icon icon-search-big"></span>
             				<span class="button-text"><s:text name="global.page.search"/></span>
             			</button>
             			<%--</cherry:show>--%>
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
        <div class="section-content" id="result_list">
           	<div class="toolbar clearfix">
           		<cherry:show domId="BINOLSSPRM41PNT">
           		<div id="print_param_hide" class="hide">
       				<input type="hidden" name="pageId" value="BINOLSSPRM42"/>
          		</div>
			 	<a style="margin-right:10px;" onclick="openPrintApp('Print','#result_list');return false;" class="prints left">
					<span class="ui-icon icon-prints"></span>
					<span class="button-text"><s:text name="global.page.prints"/></span>
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
	                	<%-- 选择 --%>
	                	<th><input type="checkbox" id="allSelect" onclick="checkBillAll(this);"/><s:text name="global.page.selectAll"/></th>
	                  	<%-- 退库单号 --%>
	                  	<th><s:text name="PRM41_returnNo"/><span class="ui-icon ui-icon-document"></span></th>
	                  	<%-- 业务类型   --%>
	                  	<th><s:text name="PRM41_tradeType"/></th>
	                  	<%-- 退库部门 --%>
	                  	<th><s:text name="PRM41_sendOrg"/></th>
	                  	<%-- 退货接收部门 --%>
	                  	<th><s:text name="PRM41_receiveOrg"/></th>
	                  	<%-- 总数量 --%>
	                  	<th><s:text name="PRM41_totalQuantity"/></th>
	                  	<%-- 总金额  --%>
	                  	<th><s:text name="PRM41_totalAmount"/></th>
	                  	<%-- 退库日期 --%>
	                  	<th><s:text name="PRM41_date"/></th>
	                  	<%-- 审核区分 --%>
	                  	<th><s:text name="PRM41_verifiedFlag"/></th>
	                  	<%-- 操作员 --%>
	                  	<th><s:text name="PRM41_employeeName"/></th>
	                  	<%-- 打印状态 --%>
	                  	<th><s:text name="global.page.printStatus"/></th>
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
<%-- ============ 弹出dataTable 促销产品共通导入 START ================= --%>
<%--<jsp:include page="/WEB-INF/jsp/common/prmProductTable.jsp" flush="true" />--%>
<%-- ============ 弹出dataTable 促销产品共通导入 END =================== --%>
<%-- ================== 打印预览共通导入 START ======================= --%>
<jsp:include page="/applet/printer.jsp" flush="true" />
<%-- ================== 打印预览共通导入 END ========================= --%>
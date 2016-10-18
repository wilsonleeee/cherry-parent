<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/common/departBar.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/ss/prm/BINOLSSPRM31.js"></script>
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
	//search("BINOLSSPRM31_search");
</script>
<%-- 取得部门URL --%>
<s:url id="getDepartUrl" value="/common/BINOLCM00_queryDepart"/>
<%-- 取得仓库URL --%>
<s:url id="getInventUrl" value="/common/BINOLCM00_queryInventory"/>
<%-- 库存记录查询URL --%>
<s:url id="search_url" value="BINOLSSPRM31_search"/>
<%-- EXCEL导出URL --%>
<s:url id="xls_url" value="BINOLSSPRM31_export"/>

<s:i18n name="i18n.ss.BINOLSSPRM31">
<div id="PRM31_select" class="hide"><s:text name="global.page.all"/></div>
<div class="panel-header">
     	<%-- ###库存记录查询 --%>
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
				<%-- 查询条件  --%>
           		<strong>
           			<span class="ui-icon icon-ttl-search"></span>
           			<s:text name="global.page.condition"/>
           		</strong>
               </div>
               <div class="box-content clearfix">
               	<div class="column" style="width:50%;height: 65px;">
	               <div class="clearfix" style="line-height:25px;margin-top:2px;margin-bottom:6px;">
	                	<%-- 产品名称 --%>
	                  	<label class="left"><s:text name="PRM31_nameTotal"/></label>
	                  	<%--<s:textfield name="nameTotal" cssClass="text"/>--%>
	                  	<%--<input type="hidden" id="prmVendorId" name="prmVendorId" value=""/>--%>
	                  	<input id="BIN_BrandInfoID" type="hidden" value='<s:property value="#session.userinfo.BIN_BrandInfoID"/>'></input>
	                  	<table class="all_clean left"><tbody id="promotion_ID"></tbody></table>
	                  	<a class="add" onclick="BINOLSSPRM31.openPrmPopup();">
	                  	    <span class="ui-icon icon-search"></span>
	                  	    <span class="button-text"><s:text name="global.page.Popselect"/></span>
	                  	</a>
	                </div>
	                <div class="clearfix" style="line-height:25px;margin-bottom:6px;">
	                	<%-- 促销品有效状态 --%>
	                  	<label><s:text name="PRM31_validFlag"/></label>
	                  	<s:iterator value='#application.CodeTable.getCodes("1137")'>
		                	<input type="radio" name="validFlag" value='<s:property value="CodeKey" />' <s:if test="1 == CodeKey">checked</s:if>/><s:property value="Value" />
		                </s:iterator>
	                </div>
        		</div>
        		<div class="column last" style="width:49%;height: 65px;">
        			<p class="date">
	                	<%-- 日期范围 --%>
	                  	<label><s:text name="PRM31_date"/></label>
	                  	<span><s:textfield id="startDate" name="startDate" cssClass="date"/></span>
	                  	- 
	                  	<span><s:textfield id="endDate" name="endDate" cssClass="date"/></span>
	                </p>
	            </div>
	            <%-- ======================= 组织联动共通导入开始  ============================= --%>
           	  	<s:action name="BINOLCM13_query" namespace="/common" executeResult="true">
           	  		<s:param name="flag">1</s:param>
           	  		<s:param name="showLgcDepot">1</s:param>
           	  		<s:param name="businessType">1</s:param>
           	  		<s:param name="operationType">1</s:param>
           	  	</s:action>
           	  	<%-- ======================= 组织联动共通导入结束  ============================= --%>
               </div>
               <p class="clearfix">
             		<%--<cherry:show domId="SSPRM0731QUERY">--%>
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
         	<div class="section-content">
           	<div class="toolbar clearfix">
           		<cherry:show domId="BINOLSSPRM31EXP">
           		<span style="margin-right:10px;">
                    <a id="export" class="export" onclick="exportExcel('${xls_url}');return false;">
                        <span class="ui-icon icon-export"></span>
                        <span class="button-text"><s:text name="global.page.exportExcel"/></span>
                    </a>
                </span>
                </cherry:show>
           		<span id="headInfo"></span>
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
	                  	<th><s:text name="PRM31_num"/></th>
	                  	<%-- 产品名称 --%>
	                  	<th><s:text name="PRM31_nameTotal"/><span class="ui-icon ui-icon-document"></span></th>
	                  	<%-- 厂商编码 --%>
	                  	<th><s:text name="PRM31_unitCode"/></th>
	                  	<%-- 产品条码 --%>
	                  	<th><s:text name="PRM31_barCode"/></th>
	                  	<%-- 期初结存 --%>
	                  	<th><s:text name="PRM31_startQuantity"/></th>
	                  	<%-- 本期收入  --%>
	                  	<th><s:text name="PRM31_inQuantity"/></th>
	                  	<%-- 本期发出  --%>
	                  	<th><s:text name="PRM31_outQuantity"/></th>
	                  	<%-- 期末结存 --%>
	                  	<th><s:text name="PRM31_endQuantity"/></th>
	                </tr>
              	</thead>
            </table>
         	</div>
       </div>
       <%-- ====================== 结果一览结束 ====================== --%>
</div>
</s:i18n>
<%-- ============ 弹出dataTable 促销产品共通导入 START ================= --%>
<%--<jsp:include page="/WEB-INF/jsp/common/prmProductTable.jsp" flush="true" />--%>
<%-- ============ 弹出dataTable 促销产品共通导入 END =================== --%>
<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/common/departBar.js"></script>
<script type="text/javascript" src="/Cherry/js/ss/prm/BINOLSSPRM33.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<s:i18n name="i18n.ss.BINOLSSPRM33">
	<s:url id="getDepartUrl" value="/common/BINOLCM00_queryDepart"/>
	<s:url id="search_url" value="/ss/BINOLSSPRM33_search"/>
	<div class="hide">
		<a id="searchUrl" href="${search_url}"></a>
		<div id="PRM33_selectAll"><s:text name="PRM33_selectAll"/></div>
	</div>
      <div class="panel-header">
        <div class="clearfix"> 
		<span class="breadcrumb left">	    
			<jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
		</span>
        </div>
      </div>
      <div id="errorMessage"></div>
      <div class="panel-content">
        <div class="box">
          <cherry:form id="mainForm" class="inline" onsubmit="search(); return false;">
            <div class="box-header">
            <strong>
            	<span class="ui-icon icon-ttl-search"></span>
            	<%-- 查询条件 --%>
            	<s:text name="PRM33_condition"/>
            </strong>
               </div>
            <div class="box-content clearfix">
            <%-- 总部或者办事处用户登录 --%>
              <div class="column" style="width:49%;">
                <div class="clearfix" style="line-height:25px;margin-bottom:6px;">
               	<%-- 发货单号 --%>
                  <label><s:text name="PRM33_deliverRecNo"/></label>
                  <s:textfield name="deliverRecNo" cssClass="text" maxlength="40" onblur="ignoreCondition(this);return false;"/>
                </div>
                <div class="clearfix" style="line-height:25px;margin-bottom:6px;">
                <%-- 促销品名称 --%>
                    <label class="left"><s:text name="PRM33_promotionProductName"/></label>
                    <%--<s:hidden name="prmVendorId"/>--%>
                    <%--<s:textfield name="promotionProductName" cssClass="text" maxlength="30" autocomplete="off"/>--%>
                    <input id="BIN_BrandInfoID" type="hidden" value='<s:property value="#session.userinfo.BIN_BrandInfoID"/>'></input>
                    <table class="all_clean left"><tbody id="promotion_ID"></tbody></table>
                    <a class="add" onclick="BINOLSSPRM33.openPrmPopup();">
                        <span class="ui-icon icon-search"></span>
                        <span class="button-text"><s:text name="global.page.Popselect"/></span>
                    </a>
                </div>      
                <div class="clearfix" style="line-height:25px;margin-bottom:6px;">
		            	<%-- 收货部门 --%>
		           		<label class="left"><s:text name="PRM33_departNameReceive"/></label>
                       	<table class="all_clean left"><tbody id="inOrganization_ID"></tbody></table>
                       	<a class="add" onclick="BINOLSSPRM33.openDepartBox();">
                           <span class="ui-icon icon-search"></span>
                           <span class="button-text"><s:text name="global.page.Popselect"/></span>
                       </a>
		         </div>
              </div>
              <div class="column last" style="width:50%;">
                <p id="dateCover" class="date">
                <%-- 发货日期 --%>
                  <label><s:text name="PRM33_deliverDate"/></label>
                  <span><s:textfield name="startDate" cssClass="date"/></span> - <span><s:textfield name="endDate" cssClass="date"/></span>
                </p>
              </div>
              <%-- ======================= 组织联动共通导入开始  ============================= --%>
           	  	<s:action name="BINOLCM13_query" namespace="/common" executeResult="true">
           	  		<s:param name="flag">1</s:param>
           	  		<s:param name="businessType">1</s:param>
        			<s:param name="operationType">1</s:param>
           	  	</s:action>
           	  	<%-- ======================= 组织联动共通导入结束  ============================= --%>
            </div>
            <p class="clearfix">
             <%-- <cherry:show domId="SSPRM0733QUERY">--%>
              		<button class="right search" type="submit" onclick="search();return false">
              			<span class="ui-icon icon-search-big"></span>
              			<%-- 查询 --%>
              			<span class="button-text"><s:text name="PRM33_search"/></span>
              		</button>
              <%--	</cherry:show>--%>
            </p>
          </cherry:form>
        </div>
        <div id="section" class="section hide">
          <div class="section-header">
          <strong>
          	<span class="ui-icon icon-ttl-section-search-result"></span>
          	<%-- 查询结果一览 --%>
          	<s:text name="PRM33_results_list"/>
         </strong>
        </div>
          <div class="section-content">
            <div class="toolbar clearfix" id="result_list">
            <cherry:show domId="BINOLSSPRM33PNT">
           		<div id="print_param_hide" class="hide">
       				<input type="hidden" name="pageId" value="BINOLSSPRM28"/>
          		</div>
			 	<a style="margin-right:10px;" onclick="openPrintApp('Print','#result_list');return false;" class="prints left">
					<span class="ui-icon icon-prints"></span>
					<span class="button-text"><s:text name="global.page.prints"/></span>
				</a>
      		</cherry:show>
            <%--<span class="left">
              <a class="export"><span class="ui-icon icon-export"></span>
              	<span class="button-text">
              	
              	<s:text name="PRM33_export"/>
              	</span></a> <a class="import"><span class="ui-icon icon-export"></span>
              	<span class="button-text">
              	
              	<s:text name="PRM33_detailExport"/>
              	</span>
              	</a>
              	</span> --%>
              	<span id="headInfo" style=""></span>
              	<span class="right"><a class="setting"><span class="ui-icon icon-setting"></span>
              	<span class="button-text">
              	<%-- 设置列 --%>
              	<s:text name="PRM33_colSetting"/>
              	</span></a></span></div>
            <table id="dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
              <thead>
                <tr>
                  <th><input type="checkbox" id="allSelect" onclick="checkBillAll(this);"/><s:text name="global.page.selectAll"/></th><%-- 选择 --%>
                  <th><s:text name="PRM33_deliverRecNo"/><span class="ui-icon ui-icon-document"></span></th><%-- 发货单号 --%>
                  <th><s:text name="PRM33_departName"/></th><%-- 发货部门  --%>   
                  <th><s:text name="PRM33_departNameReceive"/></th><%-- 收货部门 --%>
                  <th><s:text name="PRM33_inventName"/></th><%-- 发货仓库 --%>
                  <th><s:text name="PRM33_totalQuantity"/></th><%-- 总数量 --%>
                  <th><s:text name="PRM33_totalAmount"/></th><%-- 总金额 --%>
                  <th><s:text name="PRM33_deliverDate"/></th><%-- 发货日期 --%>
                  <th><s:text name="PRM33_employeeName"/></th><%-- 制单员 --%>
                  <th><s:text name="global.page.printStatus"/></th><%-- 打印状态 --%>
                </tr>
              </thead>
            </table>
          </div>
        </div>
      </div>
 </s:i18n>
    <%-- ================== dataTable共通导入 START ======================= --%>
	<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
	<%-- ================== dataTable共通导入    END  ======================= --%>
	<%-- ============ 弹出dataTable 促销产品共通导入 START ================= --%>
	<%--<jsp:include page="/WEB-INF/jsp/common/prmProductTable.jsp" flush="true" />--%>
	<%-- ============ 弹出dataTable 促销产品共通导入 END =================== --%>
	<%-- ================== 打印预览共通导入 START ======================= --%>
	<jsp:include page="/applet/printer.jsp" flush="true" />
	<%-- ================== 打印预览共通导入 END ========================= --%>
	<%-- ================== 弹出datatable -- 部门共通START ======================= --%>
	<jsp:include page="/WEB-INF/jsp/common/popDepartTable.jsp" flush="true" />
	<%-- ================== 弹出datatable -- 部门共通START ======================= --%>
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
	<input type="hidden"  id="defStartDate" value=''/>
	<input type="hidden"  id="defEndDate"	value=''/>
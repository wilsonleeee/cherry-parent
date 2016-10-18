<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/common/departBar.js"></script>
<script type="text/javascript" src="/Cherry/js/ss/prm/BINOLSSPRM25.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>	 
<s:i18n name="i18n.ss.BINOLSSPRM25">
	
	<%--大中小分类 --%>
	<s:url id="url_getMCategoryAjax" value="/ss/BINOLSSPRM21_GETSECONDCATEGORY" />
	<span id ="s_getMCategoryAjax" style="display:none">${url_getMCategoryAjax}</span>
	
	<s:url id="url_getSCategoryAjax" value="/ss/BINOLSSPRM21_GETSMALLCATEGORY" />
	<span id ="s_getSCategoryAjax" style="display:none">${url_getSCategoryAjax}</span>

	<s:url id="getDepartUrl" value="/common/BINOLCM00_queryDepart"/>
	<s:url id="getInventUrl" value="/common/BINOLCM00_queryInventory"/>
	<%-- 一览明细导出URL --%>
		<s:url id="xls_url" value="BINOLSSPRM25_export"/>
			<%-- 一览导出URL --%>
		<s:url id="Excel_url" value="BINOLSSPRM25_exportTakingInfo"/>
	<div class="hide">
		<s:url id="search_url" value="/ss/BINOLSSPRM25_search"/>
		<a id="searchUrl" href="${search_url}"></a>
		<s:text name="PRM25_select" id="defVal"/>
		<div id="PRM25_select">${defVal}</div>
		<s:url id="exporChecktUrl" action="BINOLSSPRM25_exportCheck" ></s:url>
		<a id="exporChecktUrl" href="${exporChecktUrl}"></a>
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
            	<s:text name="PRM25_condition"/>
            </strong>
               </div>
            <div class="box-content clearfix">
              <div class="column" style="width:50%;height: 90px;">
                <div class="clearfix" style="line-height:25px;margin-bottom:6px;">
               	<%-- 盘点单号 --%>
                  <label><s:text name="PRM25_stockTakingNo"/></label>
                  <s:textfield name="stockTakingNo" cssClass="text" maxlength="40" onblur="ignoreCondition(this);return false;"/>
                </div>
                <div class="clearfix" style="line-height:25px;margin-bottom:6px;">
               		<%-- 促销品名称 --%>
                    <label class="left"><s:text name="PRM25_promotionProductName"/></label>
                    <%--<s:hidden name="prmVendorId"/>--%>
                    <%--<s:textfield name="promotionProductName" cssClass="text" maxlength="30" autocomplete="off"/>--%>
                    <input id="BIN_BrandInfoID" type="hidden" value='<s:property value="#session.userinfo.BIN_BrandInfoID"/>'></input>
                    <table class="all_clean left"><tbody id="promotion_ID"></tbody></table>
                    <a class="add" onclick="BINOLSSPRM25.openPrmPopup();">
	                    <span class="ui-icon icon-search"></span>
	                    <span class="button-text"><s:text name="global.page.Popselect"/></span>
                    </a>
                	<%-- 盈亏 --%>
                    <label><s:text name="PRM25_profitKbn"/></label>
	                 <s:text name="PRM25_selectAll" id="PRM25_selectAll"/>
	                 <s:select name="profitKbn" Cssstyle="height:20px;"
	                 	list='#application.CodeTable.getCodes("1018")' 
	                 	listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{PRM25_selectAll}"/>
                </div>
                <%-- <p style="height: 25px;">
                	 审核状态
                 <label><s:text name="PRM25_verifiedFlag"/></label>
                 <s:text name="PRM25_selectAll" id="PRM25_selectAll"/>
                 <s:select name="verifiedFlag" 
                 	list='#application.CodeTable.getCodes("1007")' 
                 	listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{PRM25_selectAll}"/>
                </p> --%>
                <p  id="dateCover" class="date">
                <%-- 盘点日期 --%>
                  <label><s:text name="PRM25_stockTakingDate"/></label>
                  <span><s:textfield id="startDate" name="startDate" cssClass="date"/></span> - <span><s:textfield id="endDate" name="endDate" cssClass="date"/></span>
                </p>
              </div>
              <div class="column last" style="width:49%;height: 90px;">
              	<%--大中小分类 --%>
				<input name="organizationId" id="organizationId" class="hide" value="${organizationId}"/>
				<p>
					<label><s:text name="PRM25_lblLCategory" /></label>
					<select style="width: 100px;" name="largeCategory" id="largeCategory" onchange="BINOLSSPRM25.choosePrimaryCategory();return false;">
						<option value=""><s:text name="PRM25_textAll" /></option>
						<s:iterator value="largeCategoryList">
							<option value="<s:property value="PrimaryCategoryCode" />">
								<s:property value="PrimaryCategoryName" />
							</option>
						</s:iterator>
					</select>
				</p>
				<p>
					<label><s:text name="PRM25_lblMCategory" /></label>
					<select	style="width: 100px;" name="middleCategory" id="middleCategory" onchange="BINOLSSPRM25.chooseSecondCategory();return false;">
						<option value=""><s:text name="PRM25_textAll" /></option>
					</select>
				</p>
				<p>
					<label><s:text name="PRM25_lblSCategory" /></label>
					<select style="width: 100px;" name="smallCategory" id="smallCategory">
						<option value=""><s:text name="PRM25_textAll" /></option>
					</select>
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
             <%-- <cherry:show domId="SSPRM0725QUERY">--%>
              		<button class="right search" type="submit" onclick="search();return false;">
              			<span class="ui-icon icon-search-big"></span>
              			<%-- 查询 --%>
              			<span class="button-text"><s:text name="PRM25_search"/></span>
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
          	<s:text name="PRM25_results_list"/>
         </strong>
        </div>
          <div class="section-content" id="result_list">
            <div class="toolbar clearfix">
            <%-- 批量打印--%>
            <cherry:show domId="BINOLSSPRM25PNT">
           		<div id="print_param_hide" class="hide">
       				<input type="hidden" name="pageId" value="BINOLSSPRM26"/>
          		</div>
			 	<a onclick="openPrintApp('Print','#result_list');return false;" class="prints left">
					<span class="ui-icon icon-prints"></span>
					<span class="button-text"><s:text name="global.page.prints"/></span>
				</a>
           		</cherry:show>
           		<%-- 一览导出 --%>
           		<cherry:show domId="SSPRM2501EXP">
                    <a id="exportTakingInfo" class="export left" onclick="exportExcel('${Excel_url}','0');return false;">
                        <span class="ui-icon icon-export"></span>
                        <span class="button-text"><s:text name="global.page.exportExcel01"/></span>
                    </a>
                </cherry:show>
                <%-- 一览明细导出 --%>
        		<cherry:show domId="BINOLSSPRM25EXP">
                    <a id="export" class="export left" onclick="exportExcel('${xls_url}','1');return false;">
                        <span class="ui-icon icon-export"></span>
                        <span class="button-text"><s:text name="global.page.exportExcel02"/></span>
                    </a>
                </cherry:show>
                <span id="headInfo" style="margin-left:5px;"></span>
            <%--
            <span class="left">
              <a class="export"><span class="ui-icon icon-export"></span>
              	<span class="button-text">
             
              	<s:text name="PRM25_export"/>
              	</span></a> <a class="import"><span class="ui-icon icon-export"></span>
              	<span class="button-text">
    
              	<s:text name="PRM25_detailExport"/>
              	</span>
              	</a>
              	<a class="import"><span class="ui-icon icon-export"></span>
      
              	<span class="button-text"><s:text name="PRM25_gainExport"/></span></a></span> --%>
              	<span class="right"><a class="setting"><span class="ui-icon icon-setting"></span>
              	<span class="button-text">
              	<%-- 设置列 --%>
              	<s:text name="PRM25_colSetting"/>
              	</span></a></span></div>
            <table id="dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
              <thead>
                <tr>
                 <%-- 选择 --%>
	              <th><input type="checkbox" id="allSelect" onclick="checkBillAll(this);"/><s:text name="global.page.selectAll"/></th>
                  <th><s:text name="PRM25_stockTakingNo"/><span class="ui-icon ui-icon-document"></span></th><%-- 盘点单号 --%>
                  <th><s:text name="PRM25_departName"/></th><%-- 部门名称  --%>
                  <th><s:text name="PRM25_inventName"/></th><%-- 仓库名称 --%>
                  <th><s:text name="PRM25_realQuantity"/></th><%--实盘数量 --%>
                  <th><s:text name="PRM25_gainQuantity"/></th><%-- 盘差 --%>
                  <th><s:text name="PRM25_summAmount"/></th><%-- 盘差金额 --%>
                  <th><s:text name="PRM25_type"/></th><%-- 盘点类型 --%>
                  <th><s:text name="PRM25_tradeDateTime"/></th><%-- 盘点时间 --%>
                   <%--<th><s:text name="PRM25_verifiedFlag"/></th> 审核状态 --%>
                  <th><s:text name="PRM25_employeeName"/></th><%-- 盘点员 --%>
                  <%-- 打印状态 --%>
	              <th><s:text name="global.page.printStatus"/></th>
                </tr>
              </thead>
            </table>
          </div>
        </div>
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
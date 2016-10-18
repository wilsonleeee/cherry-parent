<%-- 产品发货单一览 --%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<link rel="stylesheet" href="/${CHERRY_CONTEXT_PATH}/css/cherry/combobox.css" type="text/css">
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/common/departBar.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/common/popDataTable.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/pt/jcs/BINOLPTJCS44.js"></script>
<style>
div#DEPART_DIV hr {display:none;}
</style>
<s:i18n name="i18n.pt.BINOLPTJCS44">
	<div class="hide">
		<s:url id="s_search" value="/pt/BINOLPTJCS44_search" />
		<a id="searchUrl" href="${s_search}"></a>
		<s:url id="s_searchPrintPageUrl" value="/pt/BINOLPTJCS44_searchPrint" />
		<a id="searchPrintPageUrl" href="${s_searchPrintPageUrl}"></a>
	</div>
	     <div class="panel-header">
			<div class="clearfix">
				<span class="breadcrumb left"> <span
					class="ui-icon icon-breadcrumb"></span> <s:text
						name="JCS44_proTitle" /> &gt; <s:text name="JCS44_dropInfo" /></span>
			</div>
		</div>
	     <%-- ================== 错误信息提示 START ======================= --%>
   			 <div id="errorMessage"></div>    
   			 <div style="display: none" id="errorMessageTemp">
   			 <div class="actionError">
    		</div>
    	</div>
   		 <%-- ================== 错误信息提示   END  ======================= --%>
	      <div id="actionResultDisplay"></div>
	      <div class="panel-content">
	        <div class="box">
	          <cherry:form id="mainForm" class="inline">
	           <input type="hidden" id="currentUnitID" name="currentUnitID" value="BINOLPTJCS44"/>
	            <div class="box-header">
	            <strong>
	            	<span class="ui-icon icon-ttl-search"></span>
	            	<%-- 查询条件 --%>
	            	<s:text name="JCS44_condition"/>
	            </strong>
	            <span class="right">
	           		<a style="margin-right:10px;" onclick="BINOLPTJCS44.searchPrint(this);return false;" class="prints left">
						<span class="ui-icon icon-prints"></span>
						<span class="button-text"><s:text name="扫描打印"/></span>
					</a>
				</span>
	            </div>
	            <div class="box-content clearfix">
	               <div>
		              <div class="column" style="width:50%;height:60px;">
			            <p class="clearfix">
								<%--产品全称--%>
								<label style="width: 80px;"><s:text name="JCS44_productName" /></label>
								<span><input id="nameTotal" class="text" name="nameTotal"></span>
						</p>
		                <p class="clearfix">
								<%--产品条码--%>
								<label style="width: 80px;"><s:text name="JCS44_barCode" /></label>
								<span><input id="barCode" class="text" name="barCode"></span>
						</p>
		              </div>
		              <div class="column last" style="width:49%;height:60px;">  
		              	<p>
							 <label style="width: 80px;"><s:text name="JCS44_originalBrand" /></label><%-- 子品牌 --%>
							 <s:select name="originalBrand" list='#application.CodeTable.getCodes("1299")' listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{selectAll}" />
		              	</p>    
	              		<p>
							<%-- 有效状态--%>
							<label style="width: 80px;"><s:text
									name="JCS44_validFlag" /></label>
							<s:select name="validFlag" Cssstyle="width:186px;"
								list='#application.CodeTable.getCodes("1137")' listKey="CodeKey"
								listValue="Value" headerKey="" headerValue="%{selectAll}"
								value="1" />
						</p>     
		              </div>
	              </div>
	            </div>
	            <p class="clearfix">
	                <%-- 查询 --%>
	              	<button class="right search"  onclick="BINOLPTJCS44.searchList();return false;">
	              			<span class="ui-icon icon-search-big"></span>
	              			<span class="button-text"><s:text name="JCS44_search"/></span>
	              	</button>
	            </p>
	          </cherry:form>
	        </div>
	        <div id="section" class="section hide">
                <div class="section-header">
                    <strong>
                        <span class="ui-icon icon-ttl-section-search-result"></span>
                        <%-- 查询结果一览 --%>
                        <s:text name="JCS44_results_list"/>
                    </strong>
                </div>
                <div class="section-content" id="result_list">
                    <div class="toolbar clearfix">
		           		<div id="print_param_hide" class="hide">
		      				<input type="hidden" name="pageId" value="BINOLPTJCS44"/>
		         		</div>
				 		<a style="margin-right:10px;" onclick="BINOLPTJCS44.printAmount('1');return false;" class="prints left">
						<span class="ui-icon icon-prints"></span>
						<span class="button-text"><s:text name="global.page.prints"/></span>
						</a>
						<a style="margin-right:10px;" onclick="BINOLPTJCS44.printAmount('2');return false;" class="prints left">
						<span class="ui-icon icon-prints"></span>
						<span class="button-text"><s:text name="JCS44_printAll"/></span>
						</a>
                        <span id="headInfo" style=""></span>
        	            <span class="right">
                        <a class="setting">
                            <span class="ui-icon icon-setting"></span>
            	            <span class="button-text">
                            <%-- 设置列 --%>
                            <s:text name="JCS44_colSetting"/>
                            </span>
                        </a>
                        </span>
                    </div>
                    <table id="dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
                        <thead>
                            <tr>                 
                             <th><input type="checkbox" id="allSelect" onclick="checkBillAll(this);"/><s:text name="global.page.selectAll"/></th><%-- 选择 --%>
                              	<th><s:text name="JCS44_number" /></th>
								<th><s:text name="JCS44_productName" /></th>
								<th><s:text name="JCS44_barCode" /></th>
								<th><s:text name="JCS44_productSize" /></th>
								<th><s:text name="JCS44_memberPrice" /></th>
								<th><s:text name="JCS44_price" /></th>
                            </tr>
                        </thead>
                    </table>
	          </div>
	        </div>
	        <div class="hide" id="printDialogInit"></div>
			<div class="hide" id="printDialogTitle"><s:text name="打印"/></div>
	    </div>
	 </s:i18n>
	    <%-- ================== dataTable共通导入 START ======================= --%>
		<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
		<%-- ================== dataTable共通导入    END  ======================= --%>
		<%-- ================== 打印预览共通导入 START ======================= --%>
		<jsp:include page="/applet/printer.jsp" flush="true" />
		<%-- ================== 打印预览共通导入 END ========================= --%>
		<input type="hidden"  id="defStartDate" value=''/>
		<input type="hidden"  id="defEndDate"	value=''/>   
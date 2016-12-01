<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<s:url id="s_prtSearchUrl" value="/common/BINOLCM02_popPrtDialog" />
<link rel="stylesheet" href="/Cherry/css/common/combotree.css">
<script type="text/javascript" src="/Cherry/js/common/popDataTable.js"></script>
<script type="text/javascript" src="/Cherry/js/common/comboTreePlugin.js"></script>
<script type="text/javascript" src="/Cherry/js/common/icontains.js"></script>
<script type="text/javascript">

</script>
<s:i18n name="">

<div id ="productDialog" class="dialog hide">
    <div style="width: 50%; float: left;">
        <div style="width: 12%; float: left;">
            <s:text name="global.page.categorytree"/>
        </div>
        <div style="width: 50%; float: left;">
                <input id="justAnInputBox" style="border: 1px solid rgb(204, 204, 204); width: 200px; height: 18px;">
        </div>
    </div>
    <div style="width: 50%; float: left;">
	<input type="text" class="text" value="" id="productDialogSearch"  onKeyup ="datatableFilter(this,22);" maxlength="50"/>
	<s:if test="param!=null && param!=''">
		<input class="hide" name="originalBrand" id="param" value="<s:property value='param'/>"/>
	</s:if>
    <s:if test="isPosCloud==1">
	    <label><s:text name="品牌"/></label><%-- 品牌--%>
	    <s:text id="selectAll" name="global.page.select"/>
	    <s:select name="originalBrand" 
	    	list='#application.CodeTable.getCodes("1299")' 
	    	listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{selectAll}"
	    	onchange ="changeValid(this,22);"
	    />
    </s:if>
    <s:if test="param2!=null && param2!=''">
    <span style="margin-top:5px;">
  		<span><input type="radio" value="2" name="radio22" id="radio_2" onclick ="changeValidParam(this,22);"/><label for="radio_2"><s:text name="global.page.all"/></label></span>
  		<span><input type="radio" value="1" name="radio22" id="radio_1" onclick ="changeValidParam(this,22);" /><label for="radio_1"><s:text name="global.page.valid"/></label></span>
  		<span><input type="radio" value="0" name="radio22" id="radio_0" onclick ="changeValidParam(this,22);"/><label for="radio_0"><s:text name="global.page.invalid"/></label></span>
  	</span>
  	</s:if>

    <a class="search" onclick="datatableFilter('#productDialogSearch',22)">
    	<span class="ui-icon icon-search"></span>
    	<span class="button-text"><s:text name="global.page.searchfor"/></span>
    </a>
    </div>
  	<hr class="space" />
  	<table id="prt_dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table2" width="100%">
       <thead>
            <tr>
               <th>
               <s:if test='%{checkType == null || checkType == "checkbox"}'>
               		<input type="checkbox" id="prt_checkAll"/>
               </s:if>
               	<label for="prt_checkAll"><s:text name="global.page.Popselect"/></label>
               </th>         <%-- 选择 --%>
               <th><s:text name="global.page.prtvendorcode"/></th>     <%--厂商编码--%>
               <th><s:text name="global.page.barcode"/></th>           <%--产品条码--%>
               <th><s:text name="global.page.originalBrand"/></th>       <%-- 产品名称--%>
               <th><s:text name="global.page.productname"/></th>       <%-- 产品名称--%>
               <th><s:text name="global.page.classification"/></th>    <%-- 大分类--%>
               <th><s:text name="global.page.inclassification"/></th><%-- 中分类--%>
               <th><s:text name="global.page.smallclassification"/></th><%-- 小分类--%>
               <th><s:text name="global.page.salePrice"/></th>          <%-- 销售价格--%>
               <th><s:text name="global.page.memPrice"/></th>          <%-- 会员价格--%>
               <th><s:text name="global.page.standardCost"/></th>       <%-- 结算价格--%>
               <th><s:text name="global.page.orderPrice"/></th>       	<%-- 采购价格--%>
               <th><s:text name="global.page.platinumPrice"/></th>       <%-- 白金价格--%>
               <th><s:text name="global.page.tagPrice"/></th>       	<%-- 吊牌价格--%>
               <th><s:text name="global.page.validFlag"/></th>       	<%-- 有效区分--%>
            </tr>
        </thead>
        <tbody id="dataTableBody"></tbody>
   	</table>
   	<div class="hide" id="productDialog_temp"></div>
   	<span id ="prtSearchUrl" style="display:none">${s_prtSearchUrl}</span>
   	<span id ="global_page_ok" style="display:none"><s:text name="global.page.ok"/></span>
   	<span id ="PopProTitle" style="display:none"><s:text name="global.page.PopProTitle"/></span><%--产品信息 --%>
    <input type="hidden" id="maxCount" value="<s:property value="form.maxCount"></s:property>">
</div>
<div class="dialog2 clearfix" style="display:none" id="send_checkinfo_dialog">
    <p class="clearfix message">
        <span></span>
        <img height="15px" class="hide" src="/Cherry/css/cherry/img/loading.gif"/>
    </p>
</div>
<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>
</s:i18n>
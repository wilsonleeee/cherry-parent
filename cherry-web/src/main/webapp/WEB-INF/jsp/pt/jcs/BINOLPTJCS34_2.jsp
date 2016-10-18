<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/lib/jquery.layout.min.js"></script>
<script type="text/javascript" src="/Cherry/js/bs/common/BINOLBSCOM01.js"></script>
<script type="text/javascript">
$(function() {
	// 初始化树
	BINOLPTJCS34.initTree();
	
	productCategoryBinding({
		elementId: "locationPosition",
		showNum: 20
	});

	$("#locationPosition").result(function(event, data, formatted){
		BINOLPTJCS34.locateCategory(data[0]);
	});
	
} );
</script>
<s:i18n name="i18n.pt.BINOLPTJCS04">
  <cherry:form id="mainForm"></cherry:form>
  <div id="treeLayoutDiv" class="div-layout-all">
     <div class="ui-layout-west">
       <div style="min-width: 190px;">
         <div class="treebox2-header">
           <strong><span class="ui-icon icon-tree"></span><s:text name="pro.category"></s:text></strong>
         </div>
         <%-- 可输入下拉框 --%>
		 <div class="treebox2-header">
			<input id="locationPosition" class="text" type="text" name="locationPosition" style="width: 100px;margin-right: 0;vertical-align: middle;"/>
			<a class="search" onclick="BINOLPTJCS34.locateCategory();return false;">
				<span class="ui-icon icon-position"></span>
				<span class="button-text">定位</span>
			</a>
		 </div>
		 <%-- 树形结构 --%>
         <dl><dt class="jquery_tree" id="categoryTree"></dt></dl>
       </div>
     </div>
     <div class="ui-layout-center"  style="overflow-y:hidden;">
        <div style="min-width: 450px;" id="categoryInfo">
		   <div id = "resultByTree" class="panel-content hide" style="border: 0px;">
		        <div id="section" class="section">
		          <div class="section-header">
		          	<strong>
		          		<span class="ui-icon icon-ttl-section-search-result"></span>
		          		<s:text name="global.page.list"/>
		          	</strong>
		          </div>
		          <div class="section-content">
		            <div class="toolbar clearfix">
		            </div>
		            <table id="dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
		              <thead>
		                <tr>
		                <th><input type="checkbox" id="checkAll" onclick="bscom03_checkRecord(this,'#dataTable_Cloned');"/></th>
		                  <th width="6%"><s:text name="pro.number"/></th><%-- 编号 --%>
		                  <th><s:text name="pro.nameTotalCN"/><span class="ui-icon ui-icon-document"></span></th><%-- 中文名称--%>
		                  <th><s:text name="产品描述"/><span class="ui-icon ui-icon-document"></span></th><%-- 产品描述--%>
		                  <th><s:text name="pro.nameTotalEN"/></th><%-- 英文名称--%>
		                  <th><s:text name="pro.unitCode"/></th><%-- 产品厂商编码 --%>
		                  <th><s:text name="pro.barCode"/></th><%-- 产品条码 --%>
		                  <th><s:text name="pro.originalBrand"/></th><%-- 品牌 --%>
		                  <th><s:text name="pro.itemType"/></th><%-- 品类  --%>
		                  <th><s:text name="JCS04.primaryCategoryBig"/></th><%-- 大分类--%>
		                  <th><s:text name="JCS04.primaryCategoryMedium"/></th><%-- 中分类 --%>
		                  <th><s:text name="JCS04.primaryCategorySmall"/></th><%-- 小分类 --%>
		                  <th><s:text name="pro.mode"/></th><%-- 产品类型 --%>
		                  <th><s:text name="pro.guige"/></th><%-- 计量单位  --%>
		                  <th><s:text name="pro.standardCost"/></th><%-- 成本价格 --%>
		                  <th><s:text name="pro.orderPrice"/></th><%-- 采购价格 --%>
		                  <th><s:text name="pro.salePrice"/></th><%-- 销售价格 --%>
		                  <th><s:text name="pro.memPrice"/></th><%-- 会员价格 --%>
		                  
		                  <th ><s:text name="pro.validFlag"/></th><%-- 有效区分 --%>
		                  <th ><s:text name="global.page.option"/></th><%-- 操作 --%>
		                </tr>
		               </thead>           
		           	</table>
		         </div>
				</div>
			</div>
     	</div>
     </div>
  </div>
</s:i18n>
<div class="hide">
  <s:url action="BINOLPTJCS34_next" id="categoryInitUrl"></s:url>
  <input type="hidden" id="categoryInitUrl" value="${categoryInitUrl }"/>
  <s:url action="BINOLPTJCS34_search" id="search_url"/>
  <input type="hidden" id="search_url" value="${search_url }"/>
  <s:url action="BINOLPTJCS34_locateCat" id="locateCatUrl"></s:url>
  <a href="${locateCatUrl}" id="locateCatUrl"></a>
</div>
<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>

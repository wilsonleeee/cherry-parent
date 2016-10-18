<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<s:i18n name="i18n.pt.BINOLPTJCS03">
<%-- ======================= BOM信息开始  ============================= --%>
<div id="bomDiv" class="section <s:if test="proMap == null || proMap.mode.indexOf('BOM') == -1">hide</s:if>">
    <div class="section-header clearfix">
    	<strong class="left">
    		<%-- BOM列表 --%>
    		<span class="ui-icon icon-ttl-section-info-edit"></span>
    		<s:text name="JCS03_BOMList"/>
    		<span class="highlight">（<s:text name="JCS03_BOMListRelationship"></s:text><s:property value="%{bomListRelationship}"/>）</span>
    	</strong>
    	<span class = "right">
	    	<a class="add" onclick="BINOLPTJCS00.addBOMs();return false;">																
	      		<span class="ui-icon icon-add"></span>
				<span class="button-text"><s:text name="JCS03_addPrtBOM"/></span><%-- 添加产品BOM组件--%>
	      	</a>
	    	<a class="add" onclick="BINOLPTJCS00.addPrmBOMs();return false;">																
	      		<span class="ui-icon icon-add"></span>
				<span class="button-text"><s:text name="JCS03_addPrmBOM"/></span><%-- 添加促销品BOM组件--%>
	      	</a>
    	</span>
    </div>
    <div class="section-content" id="bomInfo">
		<table cellpadding="0" cellspacing="0" style="width: 100%">
			<thead>
				<tr>
					<th><s:text name="JCS03_BOMPrtType"/></th>
					<th><s:text name="JCS03_unitCode"/></th>
		        	<th><s:text name="JCS03_barCode"/></th>
		        	<th><s:text name="JCS03_nameTotal"/></th>
		        	<th><s:text name="JCS03_BOMprice"/>(<s:text name="JCS03_moneryUnit"/>)</th>
		        	<th><s:text name="JCS03_BOMCount"/></th>
		        	<th class="center"><s:text name="JCS03_option"/></th>
		        </tr>
			</thead>
		    <tbody id="bomBody">
		    <s:if test="bomList != null && bomList.size() > 0">
		    	<s:iterator value="bomList">
		    		<tr>
		    			<td> <%-- BOM产品类型 --%>
		    				<s:property value="#application.CodeTable.getVal('1343',subProdouctType)"/>  
		    				<input type="hidden" name="subProdouctType" value="<s:property value="subProdouctType"/>"/>
		    			</td> 
		    			<td><s:property value="unitCode"/><input type="hidden" name="subPrtVendorId" value="<s:property value="subPrtVendorId"/>"/></td>
		    			<td><s:property value="barCode"/></td>
		    			<td><s:if test="#language.equals('en_US')"><s:property value="nameEN"/></s:if><s:else><s:property value="nameCN"/></s:else></td>
		    			<td><span><input name="bomPrice" class="price" value="<s:property value="bomPrice"/>"/></span></td>
		    			<td><span><input name="bomQuantity" class="number" value="<s:property value="bomQuantity"/>"/></span></td>
		    			<td class="center">
		    				<a class="delete" href="javascript:void(0)" onclick="BINOLPTJCS00.delBOMRow(this);return false;">
						   		<span class="ui-icon icon-delete"></span><span class="button-text"><s:text name="global.page.delete"/></span><%-- 删除按钮 --%>
						   </a>
		    			</td>
		    		</tr>
		    	</s:iterator>
		    </s:if>
		    </tbody>
		</table>
	</div>
</div>
<%-- ======================= BOM信息结束  ============================= --%>
</s:i18n>
<div class="hide" id="delButton">
	<a class="delete" href="javascript:void(0)" onclick="BINOLPTJCS00.delBOMRow(this);return false;">
   	<span class="ui-icon icon-delete"></span><span class="button-text"><s:text name="global.page.delete"/></span><%-- 删除按钮 --%>
   </a>
</div>
<%-- ============ 弹出dataTable 产品共通导入 START ================= --%>
<jsp:include page="/WEB-INF/jsp/common/popProductTable.jsp" flush="true" />
<%-- ============ 弹出dataTable 产品共通导入 END =================== --%>
<%-- ============ 弹出dataTable 促销产品共通导入 START ================= --%>
<jsp:include page="/WEB-INF/jsp/common/prmProductTable.jsp" flush="true" />
<%-- ============ 弹出dataTable 促销产品共通导入 END =================== --%>
<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>
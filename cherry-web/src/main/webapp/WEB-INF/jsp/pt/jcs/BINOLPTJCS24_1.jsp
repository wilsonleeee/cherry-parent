<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<div id="aaData">
	<s:iterator value="proList">
		<s:url id="detailsUrl" value="/pt/BINOLPTJCS26_init.action">
			<s:param name="productId"><s:property value="productId"/></s:param>
			<s:param name="brandInfoId"><s:property value="brandInfoId"/></s:param>
			<s:param name="validFlag"><s:property value="validFlag"/></s:param>
		</s:url>
		<ul>
			<li>
				<s:checkbox name="validFlag" fieldValue="%{validFlag}" onclick="bscom03_checkRecord(this,'#dataTable_Cloned');"></s:checkbox>
				<s:hidden name="productInfoIds" value="%{productId}_%{BIN_ProductVendorID}_%{UpdateTime}_%{ModifyCount}"></s:hidden>
			</li>
			<li>${RowNumber}</li><%-- 编号 --%>
			<li>
				<a href="${detailsUrl}" class="popup description" style="cursor: pointer;" 
				   onclick="javascript:openWin(this);return false;" style="cursor: pointer;" 
				   <s:if test="%{null!=nameTotalCN&&nameTotalCN.length()>15}">
				   		title="<s:property value="nameTotalCN" />"
				   </s:if>
				>
			        <s:if test="%{null!=nameTotalCN&&nameTotalCN.length()>17}">
			          <s:property value="%{nameTotalCN.substring(0, 15)}" />...
			 		</s:if>
			 		<s:else>
			          <s:property value="nameTotalCN" />
			   		</s:else>
				     
				</a><%-- 产品中文名称 --%>
			</li>
			<li><span><s:property value="NameAlias"/></span></li><%-- 产品描述 --%>
			<li><span><s:property value="nameTotalEN"/></span></li><%-- 英文名称 --%>
			<li><span><s:property value="unitCode"/></span></li><%-- 厂商编码 --%>
			<li><span><s:property value="barCode"/></span></li><%-- 条码 --%>
			<li><span><s:property value="#application.CodeTable.getVal('1299',originalBrand)"/></span></li><%-- 品牌 --%>
			<li><span><s:property value="#application.CodeTable.getVal('1245',itemType)"/></span></li><%-- 品类  --%>
			<li><span><s:property value="primaryCategoryBig"/></span></li><%-- 大分类 --%>
			<li><span><s:property value="primaryCategoryMedium"/></span></li><%-- 中分类 --%>
			<li><span><s:property value="primaryCategorySmall"/></span></li><%-- 小分类 --%>
			<li><span><s:property value="#application.CodeTable.getVal('1136',mode)"/></span></li><%-- 产品类型 --%>
			<li>
				<span>
					<s:if test="spec != null && spec !=''">
						<s:property value="spec"/>&nbsp;
					</s:if>
					<s:if test="moduleCode != null && moduleCode !=''">
						<s:property value="#application.CodeTable.getVal('1190',moduleCode)"/>
					</s:if>
				</span>
			</li><%--产品规格（规格 计量单位） --%>
			<li><span><s:text name="format.price"><s:param value="standardCost"/></s:text></span></li><%-- 成本价格--%>
			<li><span><s:text name="format.price"><s:param value="orderPrice"/></s:text></span></li><%-- 采购价格--%>
			<li><span><s:text name="format.price"><s:param value="salePrice"/></s:text></span></li><%-- 销售价格 --%>
			<li><span><s:text name="format.price"><s:param value="memPrice"/></s:text></span></li><%-- 会员价格 --%>
			
			
			<li>
				<s:if test='"1".equals(validFlag)'><span class='ui-icon icon-valid'></span></s:if><%-- 有效区分 --%>
				<s:else><span class='ui-icon icon-invalid'></span></s:else>
			</li>
			<li>
				<cherry:show domId="BINOLPTJCS0402">
				<s:if test='"1".equals(validFlag)'>
					<%-- 停用 --%>
	                <a href="" class="delete" onclick="BINOLPTJCS24.confirmInit('0','<s:property value="productId"/>','<s:property value="unitCode"/>','<s:property value="barCode"/>','<s:property value="UpdateTime"/>','<s:property value="ModifyCount"/>',null,'<s:property value="BIN_ProductVendorID"/>');return false;">
	                   <span class="ui-icon icon-disable"></span>
	                   <span class="button-text"><s:text name="global.page.disable"/></span>
	                </a>
				</s:if>
				<s:else>
					<%-- 启用--%>
	                <a href="" class="add" onclick="BINOLPTJCS24.popDisOrEnableDialog('1','<s:property value="productId"/>','<s:property value="unitCode"/>','<s:property value="barCode"/>','<s:property value="UpdateTime"/>','<s:property value="ModifyCount"/>','<s:property value="BIN_ProductVendorID"/>');return false;">
	                   <span class="ui-icon icon-enable"></span>
	                   <span class="button-text"><s:text name="global.page.enable"/></span>
	                </a>
				</s:else>
				</cherry:show>
			</li>
		</ul>
	</s:iterator>
</div>

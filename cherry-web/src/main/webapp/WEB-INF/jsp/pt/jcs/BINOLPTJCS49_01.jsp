<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.pt.BINOLPTJCS45">
<div id="aaData">
	<s:iterator value="solutionDetailList" id="solutionDetail">
		<ul>
			<li>
				<s:checkbox name="validFlag" fieldValue="%{validFlag}" onclick="bscom03_checkRecord(this,'#dataTable_Cloned');"></s:checkbox>
				<!-- <s:hidden name="productInfoIds" value="%{BIN_ProductID}_%{updateTime}_%{modifyCount}"></s:hidden> -->
				<s:hidden name="productInfoIds" value="%{BIN_ProductID}" ></s:hidden>
				<span class="hide"><input type="hidden" name="updateTime" value="<s:property value='updateTime'/>"/></span><%-- 更新时间  --%>
				<span class="hide"><input type="hidden" name="modifyCount" value="<s:property value='modifyCount'/>"/></span><%-- 更新次数 --%>
			</li>
			<li>
			<span>
				${RowNumber}
				<input type="hidden" name="productPriceSolutionDetailID" value="<s:property value='BIN_ProductPriceSolutionDetailID'/>"></input>
				<input type="hidden" name="productID" value="<s:property value='BIN_ProductID'/>"></input>
				<input type="hidden" name="standNameTotal" value="<s:property value='nameTotal'/>"></input>
			</span>
			</li>
			<li>
				<span><s:property value="unitCode"/></span>
			</li>	
			<li>
				<span><s:property value="barCode"/></span>
			</li>	
			<li>
				  <span><s:property value="#application.CodeTable.getVal('1299',originalBrand)"/> </span> 
				<%-- <span><s:property value="originalBrand"/></span> --%>
			</li>
			<li>
				<s:if test="productPriceSolutionInfo.isPosCloud == 0">
				  <span>
					<%--标准产品名称 --%>
					<s:property value="nameTotal"/>
				  </span>
				</s:if>
				<s:elseif test="productPriceSolutionInfo.isPosCloud == 1">
				  <span>
					<%--方案产品名称 --%>
					<s:property value="soluProductName"/>
				  </span>
       		      <span class="hide">
       		   		  <input name="soluProductName" type="text" class="text" value="<s:property value="soluProductName"/>" onchange="" maxlength="80" />
       		      </span>
				</s:elseif>
				
				<s:if test="prtValidFlag == 0">
				<span class="red">(<s:text name="solu_validFlagDisable"/>) </span>
				</s:if>
			</li>
			<li>
				<span class="editSaleClass"><s:text name="format.price"><s:param value="salePrice"/></s:text>  </span>
       		   <span class="hide">
       		   		<input name="salePrice" type="text" class="price" value="<s:property value="salePrice"/>"  onchange="BINOLPTJCS49.setPrice(this);" maxlength="17" />
       		   </span>

			</li>
			<li>
				<span class="editMemClass"><s:text name="format.price"><s:param value="memPrice"/></s:text>  </span>
       		   <span class="hide">
       		   		<input name="memPrice" type="text" class="price" value="<s:property value="memPrice"/>" onchange="BINOLPTJCS49.setPrice(this);" maxlength="17" />
       		   </span>
			</li>
			<li>
				<s:if test="productPriceSolutionInfo.isPosCloud == 0">
					<cherry:show domId="BINOLPTJCS4503">
	   				<span class="btnEdit">
	     				<a class="edit" onclick="BINOLPTJCS49.editRowInit(this)">
	           				<span class="ui-icon icon-edit"></span>
	           				<span class="button-text"><s:text name="global.page.edit"/></span>
	          	 		</a>
	    			</span>
	    			<span class="btnSave hide">
	    				<a class="edit" onclick="BINOLPTJCS49.editRow(this);return false;">
	          				<span class="ui-icon icon-save-s"></span>
	          				<span class="button-text"><s:text name="global.page.save"/></span>
	         	 		</a>
	    			</span>
	    			</cherry:show>
    			</s:if>
    			<s:elseif test="productPriceSolutionInfo.isPosCloud == 1">
					<cherry:show domId="BINOLPTJCS2303">
	   				<span class="btnEdit">
	     				<a class="edit" onclick="BINOLPTJCS49.editRowInit(this)">
	           				<span class="ui-icon icon-edit"></span>
	           				<span class="button-text"><s:text name="global.page.edit"/></span>
	          	 		</a>
	    			</span>
	    			<span class="btnSave hide">
	    				<a class="edit" onclick="BINOLPTJCS49.editRow(this);return false;">
	          				<span class="ui-icon icon-save-s"></span>
	          				<span class="button-text"><s:text name="global.page.save"/></span>
	         	 		</a>
	    			</span>
	    			</cherry:show>
    			</s:elseif>
			</li>
		</ul>
	</s:iterator>
</div>
</s:i18n>

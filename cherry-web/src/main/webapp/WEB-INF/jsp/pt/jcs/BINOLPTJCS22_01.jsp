<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.pt.BINOLPTJCS18">
<div id="aaData">
	<s:iterator value="solutionDetailList" id="solutionDetail">
		<ul>
			<li>
				<s:if test='isCate != "1" '>
				<s:checkbox name="validFlag" fieldValue="%{validFlag}" onclick="bscom03_checkRecord(this,'#dataTable_Cloned');"></s:checkbox>
				</s:if>
				<!-- <s:hidden name="productInfoIds" value="%{BIN_ProductID}_%{updateTime}_%{modifyCount}"></s:hidden> -->
				<s:hidden name="productInfoIds" value="%{BIN_ProductID}" ></s:hidden>
				<span class="hide"><input type="hidden" name="updateTime" value="<s:property value='updateTime'/>"/></span><%-- 更新时间  --%>
				<span class="hide"><input type="hidden" name="modifyCount" value="<s:property value='modifyCount'/>"/></span><%-- 更新次数 --%>
				<span class="hide"><input type="hidden" name="isCate" value="<s:property value='isCate'/>"/></span><%-- 产品方案添加模式 --%>
			</li>
			<li>
			<span>
				${RowNumber}
				<input type="hidden" name="productPriceSolutionDetailID" value="<s:property value='BIN_ProductPriceSolutionDetailID'/>"></input>
				<input type="hidden" name="productID" value="<s:property value='BIN_ProductID'/>"></input>
			</span>
			</li>
			<li>
				<span><s:property value="unitCode"/></span>
			</li>	
			<li>
				<span>
				<s:property value="nameTotal"/>
				<s:if test="prtValidFlag == 0">
				<span class="red">(<s:text name="solu_validFlagDisable"/>) </span>
				</s:if>
				</span>
			</li>
			<li>
				<span class="editSaleClass"><s:text name="format.price"><s:param value="salePrice"/></s:text>  </span>
       		   <span class="hide">
       		   		<input name="salePrice" type="text" class="price" value="<s:text name="format.price"><s:param value="salePrice"/></s:text>" onchange="BINOLPTJCS19.setPrice(this);" maxlength="17" />
       		   </span>

			</li>
			<li>
				<span class="editMemClass"><s:text name="format.price"><s:param value="memPrice"/></s:text>  </span>
       		   <span class="hide">
       		   		<input name="memPrice" type="text" class="price" value="<s:text name="format.price"><s:param value="memPrice"/></s:text>" onchange="BINOLPTJCS19.setPrice(this);" maxlength="17" />
       		   </span>
			</li>
			<!-- 
			<li>
   				<span class="btnEdit" >
     				<a class="edit" onclick="BINOLPTJCS19.editRowInit(this)">
           				<span class="ui-icon icon-edit"></span>
           				<span class="button-text"><s:text name="global.page.edit"/></span>
          	 		</a>
    			</span>
    			<span class="btnSave hide">
    				<a class="edit" onclick="BINOLPTJCS19.editRow(this);return false;">
          				<span class="ui-icon icon-save-s"></span>
          				<span class="button-text"><s:text name="global.page.save"/></span>
         	 		</a>
    			</span>
			</li>
			 -->
		</ul>
	</s:iterator>
</div>
</s:i18n>

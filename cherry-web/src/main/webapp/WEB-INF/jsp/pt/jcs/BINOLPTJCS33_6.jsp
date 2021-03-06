<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%-- 语言环境 --%>
<s:i18n name="i18n.pt.BINOLPTJCS03">

<div class="section-content">
	 <s:hidden name="existProductListIsEmpty" value="%{chkExistBarCodeMap.existProductListIsEmpty}"/>
	 
     <s:if test='(chkExistBarCodeMap != null && chkExistBarCodeMap.size() != 0)'>
     <div class="box-yew" >
     	 <s:if test='(chkExistBarCodeMap.existProductList != null && chkExistBarCodeMap.existProductList.size() != 0)'>
     	 
   	 		<span>
   	 		<s:text name="JCS03_barCode"/><strong> <span class="green"><s:property value="chkExistBarCodeMap.barCode"/></span></strong> <span>在系统中已存在 。</span>
		     <hr>
   	 		<span class="highlight">※</span> 
   	 		<span>点击操作列的<span class="red"><strong>【添加】/【编辑】</strong></span>按钮，选择需要维护到门店的产品。若在以下列表不存在，则点击<span class="red"><strong>【继续】</strong></span>新增；<span class="hide">反之，点击<span class="red"><strong>【关闭】</strong></span>关闭当前操作窗口。</span></span> 
   	 		</span>
			<table cellspacing="0" cellpadding="0" style="width:100%;" id="proTable" class="">
				<thead>
					<tr>
					  <th style="width:5%;"><s:text name="No." /></th>
					  <th style="width:20%;"><s:text name="JCS03_originalBrand" /></th>
					  <th style="width:20%;"><s:text name="global.page.unitCode" /></th>
					  <th style="width:25%;"><s:text name="global.page.productname" /></th>
					  <th style="width:15%;"><s:text name="JCS03_prtDescription" /></th>
					  <th style="width:18%;"><s:text name="JCS03_salePrice" /></th>
					  <th style="width:17%;"><s:text name="JCS03_memPrice" /></th>
					  <th style="width:5%;"><s:text name="产品位置" /></th>
					  <th style="width:5%;"><s:text name="操作" /></th>
					</tr>
				</thead>
				<tbody id="proShowDiv">
			     	<s:iterator value="chkExistBarCodeMap.existProductList" id="existInfo" status='st'>
			     	<tr>
			     	<td>
			     		 <span><s:property value='#st.index + 1'/> </span>
				     	 <input type="hidden" name="BIN_ProductID" value="<s:property value='BIN_ProductID'/>"/>
				     	 <input type="hidden" name="BIN_ProductVendorID" value="<s:property value='BIN_ProductVendorID'/>"/>
				     	 <input type="hidden" name="UnitCode" value="<s:property value='UnitCode'/>"/>
			     	</td>
			     	<td>
			     		<span><s:property value="#application.CodeTable.getVal('1299',OriginalBrand)"/></span>
			     	</td>
			     	<td>
			     		<span><s:property value="UnitCode"/></span>
			     	</td>
			     	<td>
			     		<span><s:property value="NameTotal"/></span>
			     	</td>
			     	<td>
			     		<span><s:property value="NameAlias"/></span>
			     	</td>
			     	<td >
			     		<span class="right"><s:text name="format.price"><s:param value="SalePrice"/></s:text></span>
			     	</td>
			     	<td  >
			     		<span class="right"><s:text name="format.price"><s:param value="MemPrice"/></s:text></span>
			     	</td>
			     	<td class="center">
			     		<span>
			     			<s:if test='(frFlag == 0)'>
			     				产品云
			     			</s:if>
			     			<s:elseif test='(frFlag == 1)'>
			     				门店
			     			</s:elseif>
			     			<%-- <s:property value="frFlag"/> --%>
			     		
			     		</span>
			     	</td>
			     	<td>
		                <a href="" class="add" onclick="BINOLPTJCS33.toAddPrt('<s:property value="BIN_ProductID"/>');return false;">
		                   <span class="ui-icon icon-add"></span>
		                   <span class="button-text">
		                   <s:if test='(frFlag == 0)'>
		                  		 <s:text name="global.page.add"/>
			     			</s:if>
			     			<s:elseif test='(frFlag == 1)'>
			     				<s:text name="global.page.edit"/>
			     			</s:elseif>
		                   </span>
		                </a>
			     	</td>
			     	 </tr>
			     </s:iterator>
				</tbody>
			</table>
		     
	     <br/>
	     </s:if>
	     <%-- 
	     <span ><s:property value="chkExistCntMap"/></span>
	     --%>
     </div>
     </s:if>
</div>

</s:i18n>
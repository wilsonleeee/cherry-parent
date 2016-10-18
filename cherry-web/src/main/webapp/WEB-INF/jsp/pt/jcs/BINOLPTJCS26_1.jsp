<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!-- 语言 -->
<s:set id="language" value="#session.language" />
<%-- ==================== 基本信息 ====================== --%>
<s:i18n name="i18n.pt.BINOLPTJCS06">
<div id="tabs-1">
   <div class="section">
     <div class="section-content">
      <div class="detail_box2" style="margin-bottom:10px;">
       <table class="detail" cellpadding="0" cellspacing="0">
        <%-- 一品一码显示 --%>
        <s:if test="%{!isU2M}">
         <tr>
         	<th><label><s:text name="JCS06.barCode"/></label></th><%--  产品条码--%>
			<td >
				<span class="hide"> <s:property value="proMap.unitCode"/> </span>
				<p style="margin-bottom: 0">
				<s:iterator value="barCodeList" status="st">
				<span class="<s:if test='"0".equals(validFlag)'>gray</s:if>">
				<s:property value="barCode"/>
				<s:if test="#st.last == false">,</s:if>
				</span>
					
				</s:iterator>
				</p>
			</td>
			<th><label><s:text name="JCS06.unitCode"/></label></th><%--  厂商编码--%>
			<td >
				<span> <s:property value="proMap.unitCode"/> </span>
			</td>
		</tr>    
		</s:if> 
		<%-- 一品多码显示 --%>
		<s:else>
			<s:iterator value="barCodeList" status="st">
			<tr>
			<s:if test="#st.first">
	 			<th rowspan="<s:property value="barCodeList.size()"/>"><label><s:text name="JCS06.unitCode"/></label></th><%-- 厂商编码 --%>
	  			<td class="bg_yew" rowspan="<s:property value="barCodeList.size()"/>"><span><s:property value="proMap.unitCode"/></span></td>
	         	<th rowspan="<s:property value="barCodeList.size()"/>"><label><s:text name="JCS06.barCode"/></label></th><%--  产品条码--%>
			</s:if>
				<td class="bg_yew" ><span class="<s:if test='"0".equals(validFlag)'>gray</s:if>" <s:if test='"0".equals(validFlag)'>title="条码已停用"</s:if>><s:property value="barCode"/></span></td>
			</tr>
			</s:iterator>
		
		</s:else>
		
   		<tr>
			<th><label><s:text name="JCS06.nameCN"/></label></th><%-- 中文名 --%>
 			<td><span><s:property value="proMap.nameCN"/></span></td>
			<th><label><s:text name="产品描述"/></label></th><%-- 产品描述 --%>
 			<td><span><s:property value="proMap.nameAlias"/></span></td>
		</tr>
      	<tr>
       		<th><s:text name="JCS06.measureUnit"/></th><%-- 计量单位 --%>
       		<td><span><s:property value='proMap.spec'/><s:property value='#application.CodeTable.getVal("1190",proMap.moduleCode)'/></span></td>
     		<th><label><s:text name="JCS06.styleCode"/></label></th><%-- 样式 --%>
       		<td><span><s:property value='#application.CodeTable.getVal("1002",proMap.styleCode)'/></span></td>
   		</tr>
   		<tr>
    		<th><s:text name="JCS06.originalBrand"/></th><%-- 品牌 --%>
       		<td><span><s:property value='#application.CodeTable.getVal("1299",proMap.originalBrand)'/></span></td>
    		<th><s:text name="JCS06.itemType"/></th><%-- 品类 --%>
       		<td><span><s:property value='#application.CodeTable.getVal("1245",proMap.itemType)'/></span></td>
     	</tr>
		</table>

		<table class="detail hide" cellpadding="0" cellspacing="0" style="margin:0">
		
		<tr>
  			<th><label><s:text name="JCS06.nameEN"/></label></th><%-- 英文名 --%>
  			<td><span><s:property value="proMap.nameEN"/></span></td>
      		<th><label><s:text name="JCS06.mode"/></label></th><%-- 产品类型 --%>
       		<td><span><s:property value='#application.CodeTable.getVal("1136",proMap.mode)'/></span></td>
		</tr>
		<tr>
		 	<th><label><s:text name="JCS06.nameShort"/></label></th><%-- 中文简称 --%>
		  	<td><span><s:property value="proMap.stNameCN"/></span></td>
 			<th><label><s:text name="JCS06.stNameEN"/></label></th><%-- 英文简称 --%>
 			<td><span><s:property value="proMap.stNameEN"/></span></td>
		</tr>
      	<tr>
    		<th><s:text name="JCS06.status"/></th><%-- 产品状态 --%>
       		<td><span><s:property value='#application.CodeTable.getVal("1016",proMap.status)'/></span></td>
       		<th><s:text name="JCS06.saleStyle"/></th><%-- 产品销售方式--%>
       		<td>
       			<span><s:property value='#application.CodeTable.getVal("1253",proMap.saleStyle)'/></span>
       			<s:if test='proMap.saleStyle == "2"'>
				<span>(<s:property value="proMap.exchangeRelation"/><s:text name="g/件"/>)</span>
				</s:if>
       		</td>
   		</tr>
		<tr>
      		<th><s:text name="JCS06.shelfLife"/></th><%-- 保质期 --%>
      		<td><span><s:property value="proMap.shelfLife"/><s:text name="JCS06.month"/></span></td><%-- 月 --%>
      		<th><s:text name="JCS06.recNumDay"/></th><%-- 建议使用天数 --%>
          	<td><span><s:property value="proMap.recNumDay"/><s:text name="JCS06.day"/></span></td><%-- 天 --%>
		</tr>
		
		
		
		
		<tr>
    		<th><s:text name="JCS06.starProduct"/></th><%-- 明星产品 --%>
      		 <td><span><s:property value='#application.CodeTable.getVal("1148",proMap.starProduct)'/></span></td>
		  	<th><label><s:text name="JCS06.validFlag"/></label></th><%-- 有效状态--%>
		  	<td><span><s:property value='#application.CodeTable.getVal("1052",proMap.validFlag)'/></span></td>
		</tr>
    	<tr>
    		<th><s:text name="JCS06.isBOM"/></th><%-- 可否作为BOM的组成 --%>
            <td><span><s:property value='#application.CodeTable.getVal("1021",proMap.isBOM)'/></span></td>
       		<th><s:text name="JCS06.isExchanged"/></th><%-- 可否用于积分兑换 --%>
       		<td><span><s:property value='#application.CodeTable.getVal("1220",proMap.isExchanged)'/></span></td>
    	</tr>
      	<tr>
      		<th><s:text name="JCS06.isReplenish"/></th><%-- 可否补货 --%>
          	<td><span><s:property value='#application.CodeTable.getVal("1004",proMap.isReplenish)'/></span></td>
    		<th><s:text name="JCS06.lackFlag"/></th><%-- 可否断货区分 --%>
          	<td><span><s:property value='#application.CodeTable.getVal("1056",proMap.lackFlag)'/></span></td>
    	</tr>
      	<tr>
      		<th><s:text name="JCS06.disReseller"/></th><%-- 经销商供货状态 --%>
        	<td><span><s:property value='#application.CodeTable.getVal("1005",proMap.disReseller)'/></span></td>
       		<th><s:text name="JCS06.disCounter"/></th><%-- 柜台供货状态 --%>
       		<td><span><s:property value='#application.CodeTable.getVal("1023",proMap.disCounter)'/></span></td>
    	</tr>
      	<tr>
       		<th><label><s:text name="JCS06.standardCost"/></label></th><%-- 成本价格 --%>
       		<td><span><s:text name="format.price"><s:param value="proMap.standardCost"/></s:text><s:text name="JCS06.moneryUnit"/></span></td><%-- 元 --%>	
       		<th><label><s:text name="JCS06.orderPrice"/></label></th><%-- 采购价格 --%>
       		<td><span><s:text name="format.price"><s:param value="proMap.orderPrice"/></s:text><s:text name="JCS06.moneryUnit"/></span></td><%-- 元 --%>	
      	</tr>
        <tr>
			<th><label><s:text name="JCS06.sellStartDate"/></label></th><%-- 开始销售日期 --%>
     		<td><span><s:property value="proMap.sellStartDate"/></span></td>
       		<th><label><s:text name="JCS06.sellEndDate"/></label></th><%-- 停止销售日期 --%>
     		<td><span><s:property value="proMap.sellEndDate"/></span></td>
     	</tr>
     	<tr>
    		<th><s:text name="JCS06.saleUnit"/></th><%-- 销售单位 --%>
      		<td><span><s:property value="proMap.saleUnit"/></span></td>
    		<th><s:text name="JCS06.isStock"/></th><%-- 管理库存 --%>
       		<td><span><s:property value='#application.CodeTable.getVal("1140",proMap.isStock)'/></span></td>
     	</tr>

       </table>
       </div>
		<%-- 用户自定义扩展属性 --%>
		<s:if test="productExtCount > 0">
			<div class="box2-active hide">
				<div class="detail_box3">
					<table cellspacing="0" cellpadding="0" class="detail" style="margin-bottom:0px;">
						<tbody>
						<tr>
				      		<%-- ======================= 产品扩展属性导入开始  ============================= --%>
						  	<s:action name="BINOLCM12_showPrtExt" namespace="/common" executeResult="true">
						  		<s:param name="id"><s:property value="productId"/></s:param>
						  		<s:param name="brandInfoId"><s:property value="proMap.brandInfoId"/></s:param>
						  		<s:param name="parity">0</s:param>
						  	</s:action>
						  	<%-- ======================= 产品扩展属性导入结束  ============================= --%>
						</tr>
						</tbody>
					</table>
				</div>
			</div>
		</s:if>
       
     </div>
   </div>
</div>
</s:i18n>

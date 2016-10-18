<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!-- 语言 -->
<s:set id="language" value="#session.language" />
<%-- ==================== 价格信息 ====================== --%>
<s:i18n name="i18n.pt.BINOLPTJCS06">
<div id="tabs-3">
   <div class="section">
      <div class="section-content">
        <s:iterator value="sellPriceList">
			<table class="detail" cellpadding="0" cellspacing="0">
			    <caption style="height:27px;">
				    <span>
	            		<s:text name="JCS06.minSalePrice"/><%-- 销售最低价 --%>
	            		<s:text name="format.price"><s:param value="proMap.minSalePrice"/></s:text><s:text name="JCS06.moneryUnit"/>
	            	</span>
	            	<span style="margin-left:30px;">
	            		<s:text name="JCS06.maxSalePrice"/><%-- 销售最高价 --%>
	            		<s:text name="format.price"><s:param value="proMap.maxSalePrice"/></s:text><s:text name="JCS06.moneryUnit"/>
	            	</span>
			    </caption>
				<tbody>
			      	<tr>
			          <th><label><s:text name="JCS06.salePrice"/></label></th><%-- 销售价格 --%>
			          <td><span><s:text name="format.price"><s:param value="salePrice"/></s:text><s:text name="JCS06.moneryUnit"/></span></td>
			          <th><label><s:text name="JCS06.memPrice"/></label></th><%-- 会员价格 --%>
			          <td><span><s:text name="format.price"><s:param value="memPrice"/></s:text><s:text name="JCS06.moneryUnit"/></span></td>
			        </tr>
			     	<tr>
			          <th><label><s:text name="JCS06.startDate"/></label></th><%-- 生效日期 --%>
			          <td><span><s:property value="startDate"/></span></td>
			          <th><label><s:text name="JCS06.endDate"/></label></th><%-- 失效日期 --%>
			          <td><span><s:property value="endDate"/></span></td>
			        </tr>
			    </tbody>
		   </table>
        </s:iterator>
      </div>
    </div>
</div>
</s:i18n>

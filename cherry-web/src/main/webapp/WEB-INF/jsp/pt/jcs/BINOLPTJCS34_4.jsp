<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%-- 语言环境 --%>
<s:i18n name="i18n.pt.BINOLPTJCS04">

<div class="section-content">
	 <%--当前产品已存在有效的编码条码[oldbarcode]继续启用当前无效编码条码[newbarcode],则会将原有效编码条码[oldbarcode]停用，是否继续？ --%>
     <s:if test='(ubMap != null && ubMap.size() != 0)'>
	 	<div class="box-yew" id="showUBMapDIV">
	       	<span>
	       		<s:text name="JCS04.wranInfo_1"/><strong>[<span class="green"><s:property value="ubMap.unitCode"/>、<s:property value="ubMap.barCode"/></span>]</strong>，
	       		<s:text name="JCS04.wranInfo_2"/><strong>[<span class="red"><s:property value="ubMap.invalidUnitCode"/>、<s:property value="ubMap.invalidBarCode"/></span>]</strong>
	       		<s:text name="JCS04.wranInfo_3"/><strong>[<span class="green"><s:property value="ubMap.unitCode"/>、<s:property value="ubMap.barCode"/></span>]</strong>
	       		<s:text name="JCS04.wranInfo_4"/>
	    	</span>
	 	</div>
     </s:if>
     <%-- 当前启用的条码[barcode]已经是有效条码，无需再次启用！ --%>
     <s:if test='(enableMap != null && enableMap.size() != 0)'>
	 	<div class="box-yew" id="showUBMapDIV">
	       	<span class="center" id="showUBMapDIV_enbale" >
	       		<s:text name="JCS04.wranInfo_21"/><strong>[<span class="green"><s:property value="enableMap.barCode"/></span>]</strong><s:text name="JCS04.wranInfo_22"/>
	    	</span>
	 	</div>
     </s:if>
     <%-- 当前启用的条码[barcode]在促销品中已存在，无法启用！ --%>
     <s:if test='(extistBarcodeMap != null && extistBarcodeMap.size() != 0)'>
	 	<div class="box-yew" id="showUBMapDIV">
	       	<span class="center" id="showUBMapDIV_extistBarcode" >
	       		<s:text name="JCS04.wranInfo_21"/>
	       		<strong>[<span class="red"> <s:property value="extistBarcodeMap.barCode"/></span>]</strong>
	       		<s:text name="JCS04.wranInfo_23"/>
	    	</span>
	 	</div>
     </s:if>
</div>

</s:i18n>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%-- 产品分类AJAX查询URL --%>
<s:url id="queryCate_url" action="BINOLPTJCS03_queryCate"/>
<%-- 产品扩展属性AJAX查询URL --%>
<s:url id="queryExt_url" action="BINOLPTJCS03_queryExt"/>
<s:i18n name="i18n.pt.BINOLPTJCS03">
<s:text name="JCS03_selectFirst" id="JCS03_selectFirst"/>
<style type="text/css">
	input.textOth {
		width:280px;
	}
</style>
<%-- ======================= 基本属性开始  ============================= --%>
<div class="section">
  <div class="section-header"><strong><span class="ui-icon icon-ttl-section-info-edit"></span>
  <%-- 基本信息 --%>
  <s:text name="JCS03_basicTab"/></strong></div>
  <div id="baseInfo" class="section-content">
  	<div class="detail_box2" style="margin-bottom:10px;">
    <table class="detail" cellpadding="0" cellspacing="0" >
      <tr class="hide">
      	<th><label><s:text name="JCS03_brand"/><span class="highlight"><s:text name="global.page.required"/></span></label></th><%-- 品牌 --%>
          <td>
             <span>
               <s:select id="brandSel" name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" onchange="BINOLPTJCS03.getSelOpts('%{queryCate_url}','%{queryExt_url}');" />
             </span>
         </td>
      </tr>
      <%-- 一品一码显示 --%>
      <s:if test="%{!isU2M}">
	      <tr>
	       	<th>
	         	<span class="left" title="<s:text name="JCS03_barCodeDesc" />"><s:text name="JCS03_barCode"/><span class="highlight"><s:text name="global.page.required"/></span></span>
	         	<%--<a id="addBarCode" href="javascript:void(0)" class="right" onClick="BINOLPTJCS00.addBarCode('#barCode','#barCodeHide');return false;"><s:text name="JCS03_addBarCode"/></a>--%>
	        </th><%--  产品条码--%>
	      	<td id="barCode">
	      		<span class="vendorSpan" style="display:block; float:none;">		    
				    <span>
				    	<input class="text textOth" maxlength="13" type="text" value="" name="barCode" tabindex="2" id="barCodeValId"
				    		onkeyup="this.value=this.value.toUpperCase();BINOLPTJCS23.getNewUnitCode()" 
				    		onchange="" 
				    		onblur="BINOLPTJCS23.chkExistBarCode()"/>
		    		</span>
					<span class="hide"><s:textfield name="unitCode" id="unitCode" cssClass="text textOth" maxlength="20" tabindex="1" onkeyup="this.value=this.value.toUpperCase()" /></span>
		 		</span>
	      	</td>
	      	<th><label title="<s:text name="JCS03_unitCodeDesc" />"><s:text name="JCS03_unitCode"/><span class="highlight"><s:text name="global.page.required"/></span></label></th><%-- 厂商编码 --%>
	      	<td><span id="unitCodeSpanId" ></span></td>
	 	  </tr>     
      </s:if>
      <tr>
      	<th><label><s:text name="JCS03_nameTotal"/><span class="highlight"><s:text name="global.page.required"/></span></label></th><%-- 中文名 --%>
      	<td><span><s:textfield name="nameTotal" cssClass="text textOth" onkeyup="return isMaxByteLen(this)" tabindex="3"/></span></td>
      	<th><label><s:text name="JCS03_prtDescription"/></label></th><%-- 别名 --%>
       	<td><span><s:textfield name="nameAlias" cssClass="text textOth" maxlength="50"/></span></td>
      
      </tr>
      <tr>
      	<th><label><s:text name="JCS03.measureUnit"/></label></th><%-- 产品规格 --%>
   		<td>
   			<span><s:textfield name="spec" cssClass="text textOth" maxlength="8" cssStyle="width:80px;"/></span><%-- 规格 --%>
   			<s:select name="moduleCode" list='#application.CodeTable.getCodes("1190")'
   			listKey="CodeKey" listValue="Value" cssStyle="width:90px;"/><%-- 计量单位 --%>
   		</td>
        <th><label><s:text name="JCS03_styleCode"/></label></th><%-- 样式 --%>
       	<td>
       		<span>
       			<s:select name="styleCode" list='#application.CodeTable.getCodes("1002")' 
       				headerKey="" headerValue="%{JCS03_selectFirst}" listKey="CodeKey" listValue="Value"/>
       		</span>
       	</td>
      </tr> 
      <tr>
		<th><label><s:text name="JCS03_originalBrand"/><span class="highlight"><s:text name="global.page.required"/></span></label></th><%-- 品牌 --%>
		<td>
			<span>
       			<s:select name="originalBrand" list='#application.CodeTable.getCodes("1299")'
       			 listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{JCS03_selectFirst}"/>
			</span>
		</td>
		<th><label><s:text name="JCS03_itemType"/></label></th><%-- 品类 --%>
		<td>
       		<span>
       			<s:select name="itemType" list='#application.CodeTable.getCodes("1245")'
       			 listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{JCS03_selectFirst}"/>
       		</span>
		</td>
      </tr>
    </table>
    
    <%-- 以下table暂时隐藏，等需要时再重新排版显示 --%>
    <table class="detail hide" cellpadding="0" cellspacing="0" style="margin:0">
    
      <tr>
      	<th><label><s:text name="JCS03_nameShort"/></label></th><%-- 中文简称 --%>
        <td><span><s:textfield name="nameShort" cssClass="text textOth" maxlength="20" /></span></td>
      	<th><label><s:text name="JCS03_nameShortForeign"/></label></th><%-- 英文简称 --%>
        <td><span><s:textfield name="nameShortForeign" cssClass="text textOth" maxlength="50"/></span></td>
      </tr>
      <tr>
       	<th><s:text name="JCS03_mode"/></th><%-- 产品类型 --%>
       	<td><span><s:select name="mode" list='#application.CodeTable.getCodes("1136")' 
         			listKey="CodeKey" listValue="Value" onchange="BINOLPTJCS00.changeMode(this);return false;"/></span>
       	</td>
      	<th><label><s:text name="JCS03_nameForeign"/></label></th><%-- 英文名 --%>
        <td><span><s:textfield name="nameForeign" cssClass="text textOth" maxlength="100" onkeyup="return isMaxByteLen(this)" tabindex="4"/></span></td>
      </tr>
      <tr>

     	<th><s:text name="JCS03_saleUnit"/></th><%-- 销售单位 --%>
    	<td><span><s:textfield name="saleUnit" cssClass="number" maxlength="3"/></span></td>
      </tr>
      <tr>  
        <th><s:text name="JCS03_status"/></th><%-- 产品状态 --%>
       	<td>
       		<span>
       			<s:select name="status" list='#application.CodeTable.getCodes("1016")' 
       				headerKey="" headerValue="%{JCS03_selectFirst}" listKey="CodeKey" listValue="Value"/>
       		</span>
       	</td>
	    <th><s:text name="JCS03_saleStyle"/></th><%-- 产品销售方式--%>
	    <td>
	    	<span>
	    		<s:select name="saleStyle" list='#application.CodeTable.getCodes("1253")' 
	    			 listKey="CodeKey" listValue="Value" onchange="BINOLPTJCS00.showExchangeRel(this);return false;"/>
	    	</span>
	    	<span class="hide" id="exchangeRelationID">
	    		<s:textfield name="exchangeRelation" cssClass="text textOth" cssStyle="width:70px;height:14px;"/><s:text name="g/件"/>
	    	</span>
	    	<span id="exRelLab" style="display:none">
				<span class="highlight">※</span>
				<span class="gray"><s:text name="JCS03_exchangeRelation"/></span>
	        </span>
	    </td>
      </tr>
      
      <tr>
      	<th><s:text name="JCS03_shelfLife"/></th><%-- 保质期 --%>
      	<td>
      		<span>
      			<s:textfield name="shelfLife" cssClass="number" maxlength="3"/><s:text name="JCS03_month"/><%-- 月 --%>
      		</span>
      	</td>
      	<th><s:text name="JCS03_recNumDay"/></th><%-- 建议使用天数 --%>
      	<td>
       		<span>
       			<s:textfield name="recNumDay" cssClass="number" maxlength="4"/><s:text name="JCS03_day"/><%-- 天 --%>
       		</span>
       	</td>
      </tr>
    
    
    
      <tr>
	 	<th><label><s:text name="JCS03_starProduct"/></label></th><%-- 明星产品 --%>
	 	<td><span><s:select name="starProduct" list='#application.CodeTable.getCodes("1148")' listKey="CodeKey" listValue="Value"/></span> </td>
	 	<th><label><s:text name="JCS03_image"/></label></th><%-- 产品图片--%>
	 	<td><span><a href="javascript:void(0);" onclick="popUploadFile(this);return false;"><s:text name="JCS03_UpImage"/></a></span></td><%-- 上传图片--%>
      </tr>
      <tr>
        <th><s:text name="JCS03_isBOM"/></th><%-- 可否作为BOM的组成 --%>
   		<td><span><s:select name="isBOM" list='#application.CodeTable.getCodes("1021")' 
   			listKey="CodeKey" listValue="Value"/></span>
   		</td>
      	<th><label><s:text name="JCS03_isExchanged"/></label></th><%-- 可否用于积分兑换 --%>
   		<td><span><s:select name="isExchanged" list='#application.CodeTable.getCodes("1220")' listKey="CodeKey" listValue="Value"/></span> </td>
      </tr>
      <tr>
      	<th><s:text name="JCS03_isReplenish"/></th><%-- 可否补货 --%>
       	<td>
       		<span>
       			<s:select name="isReplenish" list='#application.CodeTable.getCodes("1004")' 
        			headerKey="" headerValue="%{JCS03_selectFirst}" listKey="CodeKey" listValue="Value"/>
        	</span>
        </td>
    	<th><s:text name="JCS03_lackFlag"/></th><%-- 可否断货区分 --%>
        <td>
        	<span>
        		<s:select name="lackFlag" list='#application.CodeTable.getCodes("1056")'
        			headerKey="" headerValue="%{JCS03_selectFirst}" listKey="CodeKey" listValue="Value"/>
       		</span>
       	</td>
      </tr>
      <tr>
      	<th><s:text name="JCS03_discontinueReseller"/></th><%-- 经销商供货状态 --%>
       	<td>
       		<span>
       			<s:select name="discontReseller" list='#application.CodeTable.getCodes("1005")'
       			headerKey="" headerValue="%{JCS03_selectFirst}" listKey="CodeKey" listValue="Value"/>
       		</span>
       	</td>
      	<th><s:text name="JCS03_discontinueCounter"/></th><%-- 柜台供货状态 --%>
      	<td>
      		<span>
      			<s:select name="discontCounter" list='#application.CodeTable.getCodes("1023")'
      			headerKey="" headerValue="%{JCS03_selectFirst}" listKey="CodeKey" listValue="Value"/>
      		</span>
      	</td>
      </tr>
      <tr>
     	<th><label><s:text name="JCS03_standardCost"/></label></th><%-- 结算(成本)价格 --%>
       	<td><span><s:textfield name="standardCost" cssClass="price" maxlength="17"/><s:text name="JCS03_moneryUnit"/></span></td><%-- 元 --%>
      	<th><label><s:text name="JCS03_orderPrice"/></label></th><%-- 采购价格 --%>
       	<td><span><s:textfield name="orderPrice" cssClass="price" maxlength="17"/><s:text name="JCS03_moneryUnit"/></span></td><%-- 元 --%>
      </tr>
      <tr>
		<th><label><s:text name="JCS03_sellStartDate"/></label></th><%-- 开始销售日期 --%>
   		<td><span><s:textfield name="sellStartDate" cssClass="date"/></span></td>
    	<th><label><s:text name="JCS03_sellEndDate"/></label></th><%-- 停止销售日期 --%>
    	<td><span><s:textfield name="sellEndDate" cssClass="date" /></span></td>
      </tr>
      <tr>

		<th><label><s:text name="JCS03_isStock"/></label></th><%-- 管理库存 --%>
		<td>
       		<span>
       			<s:select name="isStock" list='#application.CodeTable.getCodes("1140")'
       			 listKey="CodeKey" listValue="Value" />
       		</span>
		</td>
      </tr>

      <%--
      <tr>
      	<th><label><s:text name="销售价格范围"/></label></th>
   		<td>
   			<span><input class="price" name="minSalePrice"/></span>
   			<span>-</span>
            <span><input class="price" name="maxSalePrice"/></span>
   		</td>
      </tr>
      --%>
   	 <tr class="hide">  
       	<th><label><s:text name="JCS03_operationStyle"/></label></th><%-- 使用方式 --%>
       	<td>
       		<span>
       			<s:select name="operationStyle" list='#application.CodeTable.getCodes("1003")' 
       				headerKey="" headerValue="%{JCS03_selectFirst}" listKey="CodeKey" listValue="Value"/>
       		</span>
       	</td>
     	<th> </th>
     	<td> </td>
     </tr>
    </table>
  </div>
  </div>
</div>
<%-- 一品多码显示 --%>
<s:if test="%{isU2M}">
	<div class="section">
	  <div class="section-header">
	  	<strong>
	  		<span class="ui-icon icon-ttl-section-info-edit"></span>
	  		<%-- 产品编码条码 --%>
	  		<s:text name="JCS03_UBCode"/>
	  	</strong>
    	<a id="addBarCode" class="add right" onclick="BINOLPTJCS00.addUBLines('#ubBody','#ubLineHide');return false;">
    																	
      		<span class="ui-icon icon-add"></span>
			<span class="button-text"><s:text name="JCS03_addUBCode"/></span><%-- 添加编码条码--%>
      	</a>
  	  </div>
	  <div class="section-content">
		<table id="baseInfo2" cellpadding="0" cellspacing="0" style="width: 100%">
			<thead>
				<tr>
					<th class="center"><s:text name="JCS03_unitCode"/><span class="highlight"><s:text name="global.page.required"/></span></th>
		        	<th class="center"><s:text name="JCS03_barCode"/><span class="highlight"><s:text name="global.page.required"/></span></th>
		        	<th class="center"><s:text name="JCS03_option"/></th>
		        </tr>
			</thead>
		    <tbody id="barCode">
		    		<tr id="ubBody">
						<s:text name="JCS03_inputUnitCode" id="JCS03_inputUnitCode"/>
						<td style="width:30%" class="center">
							<span><s:textfield name="unitCode" id="search" cssClass="border_b" cssStyle="width:70%" value="%{#JCS03_inputUnitCode}" maxlength="20" onkeyup="this.value=this.value.toUpperCase()" /></span>
						</td>
						<s:text name="JCS03_inputBarCode" id="JCS03_inputBarCode"/>
						<td style="width:40%" class="center">
							<span class="vendorSpan"><input type="text" style="width:70%" class="border_b" value="${JCS03_inputBarCode}" maxlength="13" name="barCode" onkeyup="this.value=this.value.toUpperCase()" /></span>
						</td>
		    			<td class="center" style="width:40%">
		    			</td>
		    		</tr>
		    </tbody>
		</table>
	  </div>
	</div>
</s:if>
<%-- ======================= 基本属性结束  ============================= --%>
</s:i18n>
<div id="barCodeHide" class="hide">
	<span class="vendorSpan" style="display:block; float:none;">		    
	    <span>
  			<input class="text textOth" type="text" value="" name="barCode"/>
  			<span class="close" title="停用" onClick="BINOLPTJCS00.delBarCodeHandle(this);return false;"><span class="ui-icon ui-icon-close"></span></span>
  		</span>
	 </span>
</div>
<%--=============================================添加产品编码条码部分=================================== --%>   	
<table class="hide">
  <tbody id="ubLineHide">
	<tr>          		
		<s:text name="JCS03_inputBarCode" id="JCS03_inputBarCode"/>
		<td style="width:40%" class="center">
			<span class="vendorSpan">
				<input type="text" style="width:70%" class="border_b" value="${JCS03_inputBarCode}" maxlength="13" name="barCode" onkeyup="this.value=this.value.toUpperCase()" />
			</span>
		</td>
   		<td class="center" style="width:40%">
   			<a class="delete" href="javascript:void(0)" onclick="BINOLPTJCS00.delBOMRow(this);return false;">
			   	<span class="ui-icon icon-delete"></span><span class="button-text"><s:text name="global.page.delete"/></span><%-- 删除按钮 --%>
			 </a>
   		</td>
	</tr>
  </tbody>
</table>

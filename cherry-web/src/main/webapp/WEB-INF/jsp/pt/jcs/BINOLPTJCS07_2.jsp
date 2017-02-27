<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!-- 语言 -->
<s:set id="language" value="#session.language" />
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
  <%-- 促销活动unitCode、barCode不可编辑 警告信息 --%>
  <s:if test='"1".equals(proMap.editFlag)'>
	  <div id="errorMessage">
	      <div class="actionError">
	  	      <ul>
	  	      	<li>
	  	      		<span>
	 		      		<s:if test='"1".equals(proMap.editFlag)'>
	 		      			<s:text name="JCS03.warning"/>
	 		      		</s:if>
		      		</span>
		      	</li>
		      </ul>         
	  	 </div>
	   </div>
   </s:if>
  <div id="baseInfo" class="section-content">
   <div class="detail_box2" style="margin-bottom:10px;"> 
    <table class="detail" cellpadding="0" cellspacing="0">
   	  <%-- 可编辑标志  --%>
  	  <input type="hidden" name="editFlag" value="<s:property value='proMap.editFlag'/>"/>
      <tr><s:hidden name="productId" value="%{productId}"/><s:hidden name="prtUpdateTime" value="%{proMap.prtUpdateTime}"/><s:hidden name="prtModifyCount" value="%{proMap.prtModifyCount}"/></tr>
      <tr class="hide">
      	<th><label><s:text name="JCS03_brand"/><span class="highlight"><s:text name="global.page.required"/></span></label></th><%-- 品牌 --%>
        <td>
        	<span>
        		<s:hidden name="brandInfoId" value="%{proMap.brandInfoId}"/>
		         <%-- 
        		<select disabled="disabled">
        			<option>
        				<s:if test="#language.equals('en_US')"><s:property value="proMap.brandNameEN"/></s:if>
        				<s:else><s:property value="proMap.brandNameCN"/></s:else>
        			</option>
        		</select>
      		  	--%>
        	</span>
        </td>
      	<th><label><s:text name="JCS03_nameAlias"/></label></th><%-- 别名 --%>
       	<td><span><s:textfield name="nameAlias" cssClass="text textOth" maxlength="50" value="%{proMap.nameAlias}"/></span></td>
 	  </tr> 
      <%-- 一品一码显示 --%>
      <s:if test="%{!isU2M}">
 	  <tr>
        <th><label><s:text name="JCS03_unitCode"/><span class="highlight"><s:text name="global.page.required"/></span></label></th><%-- 厂商编码 --%>
       	<td>
       		<span>
       			<s:if test='"1".equals(proMap.editFlag)'>
       				<s:property value='%{proMap.unitCode}'/>
       			</s:if>
       			<s:else>
	       			<s:textfield name="unitCode" cssClass="text textOth" maxlength="20" value="%{proMap.unitCode}" onkeyup="this.value=this.value.toUpperCase()" />
       			</s:else>
  			</span>
  			<input type="hidden" name="oldUnitCode" value="<s:property value="proMap.unitCode"/>"/>
 		</td>
      	<th>
        	<span class="left"><s:text name="JCS03_barCode"/><span class="highlight"><s:text name="global.page.required"/></span></span>
			<%--<a id="addBarCode" href="javascript:void(0)" class="right <s:if test="proMap.mode.indexOf('BOM') != -1">hide</s:if>" 
				onClick="BINOLPTJCS00.addBarCode('#barCode','#barCodeHide');return false;"><s:text name="JCS03_addBarCode"/></a>--%>
        </th><%--  产品条码--%>
      	<td id="barCode">
		  	<s:iterator value="barCodeList">
		  		<s:if test="1 == validFlag">
		  			<span class="vendorSpan" style="display:block; float:none;">
					    <span>
					    	<s:if test='"1".equals(proMap.editFlag)'>
					    		<s:property value='barCode'/>
					    	</s:if>
					    	<s:else>
	                   			<input class="text textOth" type="text" value="<s:property value='barCode'/>" name="barCode" maxlength="13" onkeyup="this.value=this.value.toUpperCase()"/>
					    	</s:else>
                   			<input type="hidden" name="option" value="1"/>
                   			<input type="hidden" name="validFlag" value="<s:property value='validFlag'/>"/>
                   			<input type="hidden" name="oldBarCode" value="<s:property value='barCode'/>"/>
                   			<input type="hidden" name="prtVendorId" value="<s:property value='prtVendorId'/>"/>
                   		<%--	<span class="close" <s:if test="proMap.mode.indexOf('BOM') != -1">style="display:none;"</s:if>
                   				onClick="BINOLPTJCS00.delBarCode(this);return false;"  title="停用">
                   				<span class="ui-icon ui-icon-close"></span>
                   			</span>
                   		--%>
                   		</span>
					 </span>
		  		</s:if>  	
		    </s:iterator>
      	</td>
 	  </tr>
 	  </s:if>
 	  <tr>
     	<th><label><s:text name="JCS03_nameTotal"/><span class="highlight"><s:text name="global.page.required"/></span></label></th><%-- 中文名 --%>
        <td><span><s:textfield id="nameTotal" name="nameTotal" cssClass="text textOth" onkeyup="return isMaxByteLen(this)" value="%{proMap.nameCN}"/></span><span id="nameTotalTip" class="highlight"></span></td>
       	<th><label><s:text name="JCS03_nameForeign"/></label></th><%-- 英文名 --%>
        <td><span><s:textfield name="nameForeign" cssClass="text textOth" maxlength="100" onkeyup="return isMaxByteLen(this)" value="%{proMap.nameEN}"/></span></td>
      </tr>
      <tr>
    	<th><label><s:text name="JCS03_nameShort"/></label></th><%-- 中文简称 --%>
        <td><span><s:textfield name="nameShort" cssClass="text textOth" maxlength="20" value="%{proMap.stNameCN}"/></span></td>
       	<th><label><s:text name="JCS03_nameShortForeign"/></label></th><%-- 英文简称 --%>
       	<td><span><s:textfield name="nameShortForeign" cssClass="text textOth" maxlength="20" value="%{proMap.stNameEN}"/></span></td>
      </tr>
      <tr>
       	<th><s:text name="JCS03_mode"/></th><%-- 产品类型 --%>
       	<td><span><s:select name="mode" id="mode" list='#application.CodeTable.getCodes("1136")' value="%{proMap.mode}"
         			listKey="CodeKey" listValue="Value" onchange="BINOLPTJCS00.changeMode(this);return false;"/>
         	 <input type="hidden" id="modeBef" value="<s:property value="proMap.mode"/>"/> 
         	</span>
       	</td>
      	<th><label><s:text name="JCS03_styleCode"/></label></th><%-- 样式 --%>
      	<td><span><s:select name="styleCode" list='#application.CodeTable.getCodes("1002")' 
       			headerKey="" headerValue="%{JCS03_selectFirst}" listKey="CodeKey" listValue="Value" value="%{proMap.styleCode}"/></span></td>
      </tr> 
      <tr>
        <th><label><s:text name="JCS03_standardCost"/></label></th><%-- 结算(终端成本)价格 --%>
       	<td><span><s:textfield name="standardCost" cssClass="price" maxlength="17" value="%{proMap.standardCost}"/><s:text name="JCS03_moneryUnit"/></span></td><%-- 元 --%>
        <th><label><s:text name="JCS03_orderPrice"/></label></th><%-- 成本（终端采购）价格 --%>
       	<td><span><s:textfield name="orderPrice" cssClass="price" maxlength="17" value="%{proMap.orderPrice}"/><s:text name="JCS03_moneryUnit"/></span></td><%-- 元 --%>
      </tr>
      <tr>
   		<th><label><s:text name="JCS03.measureUnit"/></label></th><%-- 计量单位 --%>
   		<td>
   			<span><s:textfield name="spec" cssClass="text textOth" maxlength="8" cssStyle="width:80px;" value="%{proMap.spec}"/></span>
   			<s:select name="moduleCode" list='#application.CodeTable.getCodes("1190")'
   			listKey="CodeKey" listValue="Value" cssStyle="width:90px;" value="%{proMap.moduleCode}"/>
   		</td>
    	<th><s:text name="JCS03_saleUnit"/></th><%-- 销售单位 --%>
      	<td><span><s:textfield name="saleUnit" cssClass="number" maxlength="3" value="%{proMap.saleUnit}"/></span></td>
      </tr> 
      <tr>
      	<th><s:text name="JCS03_status"/></th><%-- 产品状态 --%>
      	<td><span><s:select name="status" list='#application.CodeTable.getCodes("1016")' 
      		headerKey="" headerValue="%{JCS03_selectFirst}" listKey="CodeKey" listValue="Value" value="%{proMap.status}"/></span></td>
	    <th><s:text name="JCS03_saleStyle"/></th><%-- 产品销售方式--%>
	    <td>
	    	<span>
	    		<s:select name="saleStyle" list='#application.CodeTable.getCodes("1253")' 
	    			listKey="CodeKey" listValue="Value" value="%{proMap.saleStyle}" onchange="BINOLPTJCS00.showExchangeRel(this);return false;"/>
	    	</span>
	   		<s:if test='proMap.saleStyle == "2"'>
	   			<span id="exchangeRelationID">
	   				<s:textfield name="exchangeRelation" cssClass="text" cssStyle="width:70px;height:14px;" value="%{proMap.exchangeRelation}"/><s:text name="g/件"/>
	   	    	</span>
	       	</s:if>
	       	<s:else>
	       		<span class="hide" id="exchangeRelationID"> 
		    		<s:textfield name="exchangeRelation" cssClass="text" cssStyle="width:70px;height:14px;"/><s:text name="g/件"/>
	    		</span>
	       	</s:else>
	       	<span id="exRelLab" <s:if test='proMap.saleStyle == "1"'>style="display:none" </s:if>>
				<span class="highlight">※</span>
				<span class="gray"><s:text name="JCS03_exchangeRelation"></s:text></span>
	       	</span>
	    </td>
      </tr>
      <tr>
      	<th><s:text name="JCS03_shelfLife"/></th><%-- 保质期 --%>
      	<td>
      		<span>
      			<s:textfield name="shelfLife" cssClass="number" maxlength="3" value="%{proMap.shelfLife}"/><s:text name="JCS03_month"/><%-- 月 --%>
      		</span>
      	</td>
      	<th><s:text name="JCS03_recNumDay"/></th><%-- 建议使用天数 --%>
      	<td>
        	<span>
          		<s:textfield name="recNumDay" cssClass="number" maxlength="4" value="%{proMap.recNumDay}"/><s:text name="JCS03_day"/><%-- 天 --%>
       	    </span>
      	</td>
	  </tr>
	  </table>

	  <table class="detail" cellpadding="0" cellspacing="0" style="margin:0">
	  <tr>
	  	<th><label><s:text name="JCS03_starProduct"/></label></th><%-- 明星产品 --%>
	  	<td><span><s:select name="starProduct" list='#application.CodeTable.getCodes("1148")' 
	   			listKey="CodeKey" listValue="Value" value="%{proMap.starProduct}"/></span>
	 	</td>
   		<th><label><s:text name="JCS03_image"/></label></th><%-- 产品图片--%>
       	<td><span><a href="javascript:void(0);" onclick="popUploadFile(this);return false;"><s:text name="JCS03_UpImage"/></a></span></td><%-- 上传图片--%>
	  </tr>
      <tr>
       	<th><s:text name="JCS03_isBOM"/></th><%-- 可否作为BOM的组成 --%>
   		<td>
   			<span>
   				<s:if test="proMap.mode.indexOf('BOM') != -1">
	   				<s:select name="isBOM" list='#application.CodeTable.getCodes("1021")' 
			   			listKey="CodeKey" listValue="Value" value="%{proMap.isBOM}" disabled="true"/>
		   			<%-- <input type="hidden" name="isBOM" value="<s:property value="proMap.isBOM"/>" /> --%>
   				</s:if>
   				<s:else>
	   				<s:select name="isBOM" list='#application.CodeTable.getCodes("1021")' 
			   			listKey="CodeKey" listValue="Value" value="%{proMap.isBOM}"/>
   				</s:else>
   			</span>
   		</td>
	    <th><label><s:text name="JCS03_isExchanged"/></label></th><%-- 可否用于积分兑换 --%>
	   	<td><span><s:select name="isExchanged" list='#application.CodeTable.getCodes("1220")' 
	   		listKey="CodeKey" listValue="Value" value="%{proMap.isExchanged}"/></span>
	   	</td>
      </tr>
      <tr>
      	<th><s:text name="JCS03_isReplenish"/></th><%-- 可否补货 --%>
       	<td><span><s:select name="isReplenish" list='#application.CodeTable.getCodes("1004")' 
          	headerKey="" headerValue="%{JCS03_selectFirst}" listKey="CodeKey" listValue="Value" value="%{proMap.isReplenish}"/></span></td>
    	<th><s:text name="JCS03_lackFlag"/></th><%-- 可否断货区分 --%>
        <td><span><s:select name="lackFlag" list='#application.CodeTable.getCodes("1056")' 
          	headerKey="" headerValue="%{JCS03_selectFirst}" listKey="CodeKey" listValue="Value" value="%{proMap.lackFlag}"/></span></td>
     </tr>
   	 <tr>
     	<th><s:text name="JCS03_discontinueReseller"/></th><%-- 经销商供货状态 --%>
     	<td><span><s:select name="discontReseller" list='#application.CodeTable.getCodes("1005")' 
        	headerKey="" headerValue="%{JCS03_selectFirst}" listKey="CodeKey" listValue="Value" value="%{proMap.disReseller}"/></span></td>
     	<th><s:text name="JCS03_discontinueCounter"/></th><%-- 柜台供货状态 --%>
     	<td><span><s:select name="discontCounter" list='#application.CodeTable.getCodes("1023")' 
       		 headerKey="" headerValue="%{JCS03_selectFirst}" listKey="CodeKey" listValue="Value" value="%{proMap.disCounter}"/></span></td>
	</tr>
    <tr>
		<th><label><s:text name="JCS03_sellStartDate"/></label></th><%-- 开始销售日期 --%>
   		<td><span><s:textfield name="sellStartDate" cssClass="date" value="%{proMap.sellStartDate}"/></span></td>
       	<th><label><s:text name="JCS03_sellEndDate"/></label></th><%-- 停止销售日期 --%>
     	<td><span><s:textfield name="sellEndDate" cssClass="date" value="%{proMap.sellEndDate}"/></span></td>
      </tr>
      <tr>
      	<th><label><s:text name="JCS03_originalBrand"/></label></th><%-- 品牌 --%>
      	<td>
			<span>
       			<s:select name="originalBrand" list='#application.CodeTable.getCodes("1299")'
       			 listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{JCS03_selectFirst}" value="%{proMap.originalBrand}"/>
			</span>
      	</td>
      	<th><s:text name="JCS03_isStock"/></th><%-- 管理库存 --%>
      	<td><span><s:select name="isStock" list='#application.CodeTable.getCodes("1140")' 
      		 listKey="CodeKey" listValue="Value" value="%{proMap.isStock}"/></span></td>
      </tr>
      <tr>
		<th><label><s:text name="JCS03_itemType"/></label></th><%-- 品类 --%>
		<td>
       		<span>
       			<s:select name="itemType" list='#application.CodeTable.getCodes("1245")'
       			 listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{JCS03_selectFirst}" value="%{proMap.itemType}"/>
       		</span>
		</td>
      	<th><label><s:text name="JCS03_color"/></label></th><%-- 色系 --%>
      	<td><span><s:textfield name="color" cssClass="text textOth" value="%{proMap.color}"/></span></td>
      </tr>
      <tr>
      	<th><label><s:text name="JCS03_profit"/></label></th>
      	<td><span><s:textfield name="profit" cssClass="price" value="%{proMap.profit}" maxlength="15"/><s:text name="JCS03_moneryUnit"/></span></td>
      	<th><label><s:text name="JCS03_grade"/></label></th><%-- 产品等级 --%>
	      <td>
	      	 <span>
       			<s:select name="grade" list='#application.CodeTable.getCodes("1348")'
       			 listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{JCS03_selectFirst}" value="%{proMap.grade}"/>
       		</span>
	      </td>
      </tr>
      <tr>
	      <th><label><s:text name="JCS03_secQty"/></label></th><%-- 安全库存数量 --%>
	      <td>
	      	 <span>
	      	 	<s:textfield name="secQty" cssClass="price"  maxlength="15" value="%{proMap.secQty}"/><s:text name="JCS03_Unit"/>
	      	 </span>
	      </td>
	      <th><label><s:text name="JCS03_displayQty"/></label></th><%-- 陈列数 --%>
	      <td>
	      	 <span>
	      	 	<s:textfield name="displayQty"   cssClass="price" maxlength="15" value="%{proMap.displayQty}"/><s:text name="JCS03_Unit"/>
	      	 </span>
	      </td>
      </tr>
      <tr class="hide">
		<th><label><s:text name="JCS03.validFlag"/></label></th><%-- 有效区分 --%>
        <td>
          	<span> 
          		<s:select id="validFlagSel" name="validFlag" list='#application.CodeTable.getCodes("1137")' listKey="CodeKey" 
                      listValue="Value" value="%{proMap.validFlag}" tabindex="25" />
             </span>
        </td>
       	<th><label><s:text name="JCS03_operationStyle"/></label></th><%-- 使用方式 --%>
       	<td><span><s:select name="operationStyle" list='#application.CodeTable.getCodes("1003")' 
       	headerKey="" headerValue="%{JCS03_selectFirst}" listKey="CodeKey" listValue="Value" value="%{proMap.optStyle}"/></span></td>
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
	    	<a id="addBarCode" class="add right " style="<s:if test='"1".equals(proMap.addBarCodeDis)'>visibility:hidden</s:if><s:else></s:else>" onclick="BINOLPTJCS00.addUBLines('#ubBody','#ubLineHide');return false;" >
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
			  	<s:iterator value="barCodeList" status="st1">
			  		<s:if test="1 == validFlag">
			  			<s:if test="#st1.first">
		  				<tr id="ubBody">
							<td style="width:30%" class="center" rowspan="<s:property value="barCodeList.size()"/>"><%-- 厂商编码 --%>
					       		<span>
					       			<s:if test='"1".equals(proMap.editFlag)'> <s:property value='%{proMap.unitCode}'/> </s:if>
					       			<s:else> <s:textfield name="unitCode" cssClass="border_b_a" CssStyle="width:70%" maxlength="20" value="%{proMap.unitCode}" onkeyup="this.value=this.value.toUpperCase()" /> </s:else>
					  			</span> 
								<input type="hidden" name="oldUnitCode" value="<s:property value="proMap.unitCode"/>"/>
							</td>
			  			</s:if>
			  			<s:else>
		  				<tr>
			  			</s:else>
				  			<td style="width:40%" class="center"> <%-- 产品条码 --%>
					  			<span class="vendorSpan" style="display:block; float:none;">
								    <span>
								    	<s:if test='"1".equals(proMap.editFlag)'> <s:property value='barCode'/> </s:if>
								    	<s:else> <input class="border_b_a" type="text" style="width:70%" maxlength="13" value="<s:property value='barCode'/>" name="barCode" onkeyup="this.value=this.value.toUpperCase()"/> </s:else>
			                   			<input type="hidden" name="option" value="1"/>
			                   			<input type="hidden" name="validFlag" value="<s:property value='validFlag'/>"/>
			                   			<input type="hidden" name="oldBarCode" value="<s:property value='barCode'/>"/>
			                   			<input type="hidden" name="prtVendorId" value="<s:property value='prtVendorId'/>"/>
			                   		</span>
								 </span>
				  			</td>
			    			<td class="center" style="width:40%"> <%-- 操作 --%>
			    			</td>
			  			</tr>
			  		</s:if>  	
			    </s:iterator>
		    </tbody>
		</table>
	  </div>
	</div>
</s:if>
<%-- ======================= 基本属性结束  ============================= --%>
</s:i18n>
<div id="barCodeInfo" class="hide">
	<s:iterator value="barCodeList">
		<span>
			<input type="hidden" name="validFlag" value="<s:property value='validFlag'/>"/>
			<input type="hidden" name="barCode" value="<s:property value='barCode'/>"/>
			<input type="hidden" name="prtVendorId" value="<s:property value='prtVendorId'/>"/>
		</span>
	</s:iterator>
</div>
<div id="barCodeHide" class="hide">
	<span class="vendorSpan" style="display:block; float:none;">		    
	    <span>
  			<input class="text" type="text" value="" name="barCode"/>
  			<span class="close" onClick="BINOLPTJCS00.delBarCode(this);return false;"><span class="ui-icon ui-icon-close"></span></span>
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
				<input type="text" style="width:70%" class="border_b" value="${JCS03_inputBarCode}" maxlength="13" name="barCode" onkeyup="this.value=this.value.toUpperCase()">
			</span>
		</td>
   		<td class="center" style="width:40%" >
   			<a class="delete" href="javascript:void(0)" onclick="BINOLPTJCS00.delBOMRow(this);return false;">
			   	<span class="ui-icon icon-delete"></span><span class="button-text"><s:text name="global.page.delete"/></span><%-- 删除按钮 --%>
			 </a>
   		</td>
	</tr>
  </tbody>
</table>

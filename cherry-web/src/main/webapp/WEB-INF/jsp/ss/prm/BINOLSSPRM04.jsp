<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<script type="text/javascript" src="/Cherry/js/ss/prm/BINOLSSPRM04.js"></script>
<s:i18n name="i18n.ss.BINOLSSPRM04">
<div class="main container clearfix">
 <div class="panel ui-corner-all">
  <div class="panel-header">
    <div class="clearfix">
       <span class="breadcrumb left"> 
          <span class="ui-icon icon-breadcrumb"></span>
          <s:text name="prm04_manage"/> &gt; <s:text name="prm04_details"/>
       </span>
    </div>
  </div>
      <div class="panel-content">
       	<form id="toEditForm" action="BINOLSSPRM03_init" method="post">
        	<%-- 促销品ID --%>
        	<input type="hidden" name="promotionProId" value="${prmInfo.promotionProId}"/>
        	<input type="hidden" name="csrftoken" id="parentCsrftoken"/>
        </form>
        <div class="tabs hide">
        <cherry:form id="mainForm" method="post" class="inline" csrftoken="false">
          <ul>
            <li><a href="#tabs-1"><s:text name="prm04_basicTab"/></a></li>
            <%--<li><a href="#tabs-2"><s:text name="prm04_priceTab"/></a></li>
            <li><a href="#tabs-3"><s:text name="prm04_priceDepTab"/></a></li>
            <li><a href="#tabs-4"><s:text name="prm04_facTab"/></a></li>--%>
            <li><a href="#tabs-5"><s:text name="prm04_imageTab"/></a></li>
          </ul>
          <div id="tabs-1">
            <div class="section">
              <div class="section-header"><strong><span class="ui-icon icon-ttl-section-info"></span>
              <%-- 基本信息 --%>
              <s:text name="global.page.title"/>
              </strong></div>
              <div class="section-content">
                <table class="detail" cellpadding="0" cellspacing="0">
                  <tr>
                  <th>
                      <%--  品牌--%>
                      <s:text name="prm04.brandName"/>
                    </th>
                    <td>
                       <span><s:property value="prmInfo.brandName"/></span>
                    </td>
                  <th>
					  <%--  大分类名称--%>
                      <s:text name="prm04.primaryCategoryName"/></th>
                    <td>
                     	<span><s:property value="prmInfo.primaryCateName"/></span>
					</td>
                  </tr>
                  <tr>
                  <th>
                     <label><s:text name="PRM04.promCate"/></label><%-- 促销品类型 --%>
                    </th>
                    <td>
                      <span><s:property value='#application.CodeTable.getVal("1139", prmInfo.promCate)'/></span>
                    </td>
                  
                    <th>
					  <%--  中分类名称--%>
                      <s:text name="prm04.secondryCategoryName"/></th>
                    <td>
                     	<span><s:property value="prmInfo.secondCateName"/></span>
					</td>
                  </tr>
                  <tr>
                  <th>
                      <%--  厂商编码 --%>                            
                      <s:text name="prm04.unitCode"/>
                    </th>
                    <td>
                      <span><s:property value="prmInfo.unitCode"/></span>
                    </td>
                  
                    <th>
					  <%--  小分类名称--%>
                      <s:text name="prm04.smallCategoryName"/></th>
                    <td>
                     	<span><s:property value="prmInfo.smallCateName"/></span>
					</td>
                  </tr>
                  <tr>
                  <th>
					  <%--  促销品条码--%>
                      <s:text name="prm04.barCode"/></th>
                    <td>
                     	<span>
                     	<s:iterator value="prmFacList" id="prmFac" status="R">
                     		<s:if test='#R.index != 0'>,</s:if>
                     		<s:property value="barCode"/>
                     	</s:iterator>
                     	</span>
					</td>
                  
                    <th>
                    <%--  标准成本 --%>
	                <s:if test='"TZZK".equals(prmInfo.promCate) || "DHCP".equals(prmInfo.promCate)'>
	                    <label style="color:red;"><s:text name="prm04.sellCost"/></label>
                    </s:if>
                    <s:else>
                    	<label><s:text name="prm04.standardCost"/></label>
                    </s:else>
                    </th>
                    <td>
                     <s:if test='prmInfo.standardCost != null'>
                       <span>
		               		<s:text name="format.price"><s:param value="prmInfo.standardCost"></s:param></s:text>
		               		<%--  元 --%>
		               		<s:text name="prm04_moneryUnit"/>
		               	</span>
		              </s:if>
                    </td>
                  </tr>
                  <tr>
                  <th>
                      <%--  促销品全称 --%>  
                      <s:text name="prm04.nameTotal"/>
                    </th>
                    <td>
                      <span><s:property value="prmInfo.nameTotal"/></span>
                    </td>
                  
                    <th>
                      <%--  促销品容量 --%>
                      <s:text name="prm04.volume"/>
                    </th>
                    <td>
                    <span>
                    <s:if test="null != prmInfo.volume">
                      <s:text name="format.price"><s:param value="prmInfo.volume"></s:param></s:text>
				      <s:property value='#application.CodeTable.getVal("1028", prmInfo.volumeUnitMeasureCode)'/>
				     </s:if>
				     </span>
                    </td>
                  </tr>
                  <tr>
                  <th>
                      <%--  促销品简称--%>  
                      <s:text name="prm04.nameShort"/>
                    </th>
                    <td>
                      <span><s:property value="prmInfo.nameShort"/></span>
                    </td>
                  
                    <th>
                      <%--  促销品重量 --%>
                      <s:text name="prm04.weight"/> 
                    </th>
                    <td>
                    <span>
                    <s:if test="null != prmInfo.weight">
                      <s:text name="format.price"><s:param value="prmInfo.weight"></s:param></s:text>
				      <s:property value='#application.CodeTable.getVal("1029", prmInfo.weightUnitMeasureCode)'/>
				    </s:if>
				    </span>
                    </td>
                  </tr>
                  <tr>
                  <th>
                      <%--  促销品别名--%>  
                      <s:text name="prm04.nameAlias" />
                    </th>
                    <td>
                      <span><s:property value="prmInfo.nameAlias"/></span>
                    </td>
                  
                    <th>
					  <%--  开始销售日期--%>
                      <s:text name="prm04.sellStartDate"/>
                    </th>
                    <td>
                      <span><s:property value="prmInfo.sellStartDate"/></span>
                    </td>
                  </tr>
                  <tr>
                    <th>
					  <%--  促销品英文简称 --%>
                      <s:text name="prm04.nameShortForeign"/>
                    </th>
                    <td>
                      <span><s:property value="prmInfo.nameShortForeign"/></span>
                    </td>
                    <th>
                      <%--  停止销售日期--%>
                      <s:text name="prm04.sellEndDate"/></th>
                    <td>
                      <span><s:property value="prmInfo.sellEndDate" /></span>
                    </td>
                  </tr>
                  <tr>
                  	<th>
				      <%--  促销品样式 --%>
                      <s:text name="prm04.styleCode"/>
                    </th>
                    <td>
                      <span><s:property value='#application.CodeTable.getVal("1012", prmInfo.styleCode)'/></span>
                    </td>
					<th>
                      <%--  保质期--%>
                      <s:text name="prm04.shelfLife"/>
                    </th>
                    <td>
                    <span>
                     <s:if test="null != prmInfo.shelfLife">
                        <s:property value="prmInfo.shelfLife" />
                        <%--  月--%>
                        <s:text name="prm04_month"/>
                     </s:if>
                     </span>
                    </td>
                  </tr>
                  <tr>
                  <th>
                      <%--  促销品英文名 --%>
                      <s:text name="prm04.nameForeign"/>
                    </th>
                    <td>
                      <span><s:property value="prmInfo.nameForeign"/></span>
                    </td>
                    
					<th>
				      <%-- 有效区分 --%>
                      <s:text name="prm04.validFlag"/>
                    </th>
                    <td>
                      <span><s:property value='#application.CodeTable.getVal("1137", prmInfo.validFlag)'/></span>
                    </td>
                  </tr>
                  <tr>
                    <th>
                      <%--  促销品使用方式 --%>
                      <s:text name="prm04.operationStyle"/>
                    </th>
                    <td>
                      <span><s:property value='#application.CodeTable.getVal("1013", prmInfo.operationStyle)'/></span>
                    </td>
					<th>
				      <label><s:text name="PRM04.isStock"/></label><%-- 库存管理--%>
                    </th>
                    <td>
                      <span><s:property value='#application.CodeTable.getVal("1140", prmInfo.isStock)'/></span>
                    </td>
                  </tr>
                  <tr>
                    <th><label><s:text name="PRM04.exPoint"/></label></th><%-- 可兑换积分值 --%>
                    <td><span><s:property value='prmInfo.exPoint'/></span></td>
					<th>
				      <label><s:text name="PRM04.isExchanged"/></label><%-- 可否用于积分兑换 --%>
                    </th>
                    <td>
                      <span><s:property value='#application.CodeTable.getVal("1220", prmInfo.isExchanged)'/></span>
                    </td>
                  </tr>
                  <tr>
                    <th>
                    	<label><s:text name="PRM04_isPosIss"/></label><%-- 是否下发到POS--%>
                    </th>
                    <td>
                    	<span><s:property value='#application.CodeTable.getVal("1341", prmInfo.isPosIss)'/></span>
                    </td>
                     <%-- 促销品类型 --%>                     
					<th>
						<s:if test="prmInfo.promCate=='CXLP'">
						<s:text name="prm04.mode"/>
						</s:if>
                    </th>
                    <td>
                    	<s:if test="prmInfo.promCate=='CXLP'">
						<span><s:property value='#application.CodeTable.getVal("1345", prmInfo.mode)'/></span>	
						</s:if>
                    </td>
                  </tr>
                </table>
              </div>
            </div>
            <div class="center clearfix">
            <%-- 促销品编辑按钮 --%>
       		<cherry:show domId="SSPRM0104EDIT">
       		  <button class="edit" onclick="editPrm();return false;" type="button">
       		 			<span class="ui-icon icon-edit-big"></span>
              			<span class="button-text"><s:text name="global.page.edit"/></span>
       		  </button>
       		</cherry:show>
       		<button id="close" class="close" type="button"  onclick="doClose();return false;">
            	<span class="ui-icon icon-close"></span>
            	<span class="button-text"><s:text name="global.page.close"/></span>
          </button>
            </div>
          </div>
          <%--<div id="tabs-2" >
            <div class="section">
              <div class="section-header"><strong><span class="ui-icon icon-ttl-section-list"></span>
              <%-- 促销品标准销售价格
              	<s:text name="prm04_standardsellPrice"/></strong>
              	&nbsp;&nbsp;<span class="highlight"><strong><s:text name="prm04_snow"/></strong></span><s:text name="prm04_explain"/>
              </div>
              <div class="section-content">
              <div id="table_scroll" style="overflow-x:auto;overflow-y:hidden">
                <table cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="60%">
                  <thead>
                    <tr>
                       <%-- 编号	
                      <th><s:text name="prm04.number"/></th>
                      <%-- 销售价格	
                      <th><s:text name="prm04.salePrice"/></th>
                      <%-- 价格生效日 
                      <th><s:text name="prm04.startDate"/></th>
                      <%-- 价格失效日 
                      <th><s:text name="prm04.endDate"/></th>
                    </tr>
                  </thead>
                  <tbody>
                  <s:iterator value="prmSalePriceList" id="prmSalePrice" status="R">
                   <tr class="<s:if test='#R.odd'>odd</s:if><s:else>even</s:else>">
                    <td>
                    	<s:property value="#R.index+1"/>
                    	<s:if test='"1".equals(#prmSalePrice.doingKbn)'>
                    		<span class="highlight"><strong><s:text name="prm04_snow"/></strong></span>
                    	</s:if>
                    </td>
                    <td>
                    	<span>
                    	<s:if test='#prmSalePrice.salePrice != null'>
		               		<s:text name="format.price"><s:param value="#prmSalePrice.salePrice"></s:param></s:text>
		               		<%--  元 
		               		<s:text name="prm04_moneryUnit"/>
		               	</s:if>
		            	</span>
                    </td>
                    <td><span><s:property value="#prmSalePrice.startDate"/></span></td>
                    <td>
                    	<span><s:property value="#prmSalePrice.endDate"/></span>
                    </td>
                 </tr>
                 </s:iterator>
                </tbody>
               </table>
               </div>
              </div>
            </div>
           </div>--%>
          <%--<div id="tabs-3" >
            <div class="section">
              <div class="section-header"><strong><span class="ui-icon icon-ttl-section-list"></span>
              <%-- 部门机构促销品价格--%
              <s:text name="prm04_priceDepInfo"/></strong>
              &nbsp;&nbsp;<span class="highlight"><strong><s:text name="prm04_snow"/></strong></span><s:text name="prm04_explain"/>
              </div>
              <div class="section-content">
              <div id="table_scroll" style="overflow-x:auto;overflow-y:hidden">
                <table cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
                  <thead>
                    <tr>
                       <%-- 编号	--%
                      <th><s:text name="prm04.number"/></th>
                      <%-- 部门名称	--%
                      <th><s:text name="prm04.departName"/></th>
                      <%-- 折扣率	--%
                      <th><div style="line-height: 13px;"><s:text name="prm04.discountRate"/></div></th>
                      <%-- 小数处理	--%
                      <th><s:text name="prm04.decimalMode"/></th>
                       <%-- 调货价格	--%
                      <th><div style="line-height: 13px;"><s:text name="prm04.allocationPrice"/></div></th>
                      <%-- 销售指导价--%
                      <th><div style="line-height: 13px;"><s:text name="prm04.depSalePrice"/></div></th>
                      <%-- 销售最低价 --%
                      <th><div style="line-height: 13px;"><s:text name="prm04.minPrice"/></div></th>
                      <%-- 销售最高价--%
                      <th><div style="line-height: 13px;"><s:text name="prm04.maxPrice"/></div></th>
                      <%-- 价格生效日--%
                      <th><s:text name="prm04.depStartDate"/></th>
                      <%-- 价格失效日--%
                      <th><s:text name="prm04.depEndDate"/></th>
                    </tr>
                  </thead>
                  <tbody>
                   <s:iterator value="prmPriceDepartList" id="prmDep" status="R">
                   <tr class="<s:if test='#R.odd'>odd</s:if><s:else>even</s:else>">
                    <td>
                    	<s:property value="#R.index+1"/>
                    	<s:if test='"1".equals(#prmDep.doingKbn)'>
                    		<span class="highlight"><strong><s:text name="prm04_snow"/></strong></span>
                    	</s:if>
                    </td>
                    <td>
                    	<s:if test='#prmDep.departName != null && !"".equals(#prmDep.departName)'>
		               		<s:property value="#prmDep.departName"/>
		               	</s:if>
		                <s:else>
		                	&nbsp;
		                </s:else>
		            </td>
                    <td>
                    	<s:if test='#prmDep.discountRate != null'>
		               		<s:property value="#prmDep.discountRate"/>
		               	</s:if>
		                <s:else>
		                	&nbsp;
		                </s:else>
                    </td>
                    <td><span><s:property value='#application.CodeTable.getVal("1036", #prmDep.decimalMode)'/></span></td>
                    <td>
                    	<s:if test='#prmDep.allocationPrice != null'>
		               		<s:text name="format.price"><s:param value="#prmDep.allocationPrice"></s:param></s:text>
		               	</s:if>
		                <s:else>
		                	&nbsp;
		                </s:else>
                    </td>
                     <td>
                    	<s:if test='#prmDep.depSalePrice != null'>
		               		<s:text name="format.price"><s:param value="#prmDep.depSalePrice"></s:param></s:text>
		               	</s:if>
		                <s:else>
		                	&nbsp;
		                </s:else>
                    </td>
                    <td>
                    	<s:if test='#prmDep.minPrice != null'>
		               		<s:text name="format.price"><s:param value="#prmDep.minPrice"></s:param></s:text>
		               	</s:if>
		                <s:else>
		                	&nbsp;
		                </s:else>
                    </td>
					<td>
                    	<s:if test='#prmDep.maxPrice != null'>
		               		<s:text name="format.price"><s:param value="#prmDep.maxPrice"></s:param></s:text>
		               	</s:if>
		                <s:else>
		                	&nbsp;
		                </s:else>
                    </td>
                    <td>
                    	<s:if test='#prmDep.depStartDate != null'>
		               		<s:property value="#prmDep.depStartDate"/>
		               	</s:if>
		                <s:else>
		                	&nbsp;
		                </s:else>
		            </td>
                    <td>
                    	<s:if test='#prmDep.depEndDate != null'>
		               		<s:property value="#prmDep.depEndDate"/>
		               	</s:if>
		                <s:else>
		                	&nbsp;
		                </s:else>
		            </td>
                 </tr>
                 </s:iterator>
                </tbody>
               </table>
               </div>
              </div>
            </div>
           </div>--%>
          <%--<div id="tabs-4" >
            <div class="section">
              <div class="section-header"><strong><span class="ui-icon icon-ttl-section-list"></span>
              <%-- 促销品厂商信息
              <s:text name="prm04_facInfo"/></strong>
              </div>
              <div class="section-content">
              <div id="table_scroll" style="overflow-x:auto;overflow-y:hidden">
                <table cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
                  <thead>
                    <tr>
                       <%-- 编号	--%
                      <th><s:text name="prm04.number"/></th>
                      <%-- 厂商名称	--%
                      <th><s:text name="prm04.factoryName"/></th>
                      <%-- 生产厂商代码	--%
                      <th><s:text name="prm04.manufacturerCode"/></th>
                      <%-- 促销品条码	--%
                      <th><s:text name="prm04.barCode"/></th>
                       <%-- 最小订货量	--%
                      <th><s:text name="prm04.minOrderQuantity"/></th>
                      <%-- 订货所需天数	--%
                      <th><s:text name="prm04.daysToOrder"/></th>
                    </tr>
                  </thead>
                  <tbody>
                  <s:iterator value="prmFacList" id="prmFac" status="R">
                   <tr class="<s:if test='#R.odd'>odd</s:if><s:else>even</s:else>">
                    <td><s:property value="#R.index+1"/></td>
                    <td>
                    	<s:if test='#prmFac.factoryName != null && !"".equals(#prmFac.factoryName)'>
		               		<s:property value="#prmFac.factoryName"/>
		               	</s:if>
		                <s:else>
		                	&nbsp;
		                </s:else>
                    </td>
                    <td><s:property value="#prmFac.manufacturerCode"/></td>
                    <td>
                    	<s:if test='#prmFac.barCode != null && !"".equals(#prmFac.barCode)'>
		               		<s:property value="#prmFac.barCode"/>
		               	</s:if>
		                <s:else>
		                	&nbsp;
		                </s:else>
                    </td>
                    <td>
                    	<s:if test='#prmFac.minOrderQuantity != null'>
		               		<s:property value="#prmFac.minOrderQuantity"/>
		               	</s:if>
		                <s:else>
		                	&nbsp;
		                </s:else>
                    </td>
                    <td>
                    	<s:if test='#prmFac.daysToOrder != null'>
		               		<s:property value="#prmFac.daysToOrder"/>
		               	</s:if>
		                <s:else>
		                	&nbsp;
		                </s:else>
                    </td>
                 </tr>
                 </s:iterator>
                </tbody>
               </table>
               </div>
              </div>
            </div>
           </div>--%>
           <div id="tabs-5" class="ui-tabs-panel">
            <div class="section">
              <div class="section-header"><strong><span class="ui-icon icon-ttl-section-list"></span>
              <%-- 促销品图片 --%>
              <s:text name="prm04_imageTab"/></strong></div>
              <div class="section-content">
              	<ul class="u3 clearfix">
              	<s:if test="null != prmExtList">
              		<s:iterator value="prmExtList">
                	<li><a href="#"><img src="<s:property value='imagePath'/>/<s:property value='promotionImagePath'/>" width="120" height="120" /></a></li>
                	</s:iterator>
                </s:if>
                </ul>
              </div>
            </div>
          </div>
        </cherry:form>
       </div>
      </div>
     </div>
   </div>
</s:i18n>

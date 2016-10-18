<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/bs/fac/BINOLBSFAC04.js"></script>
<%--请选择 --%>
<s:text id="global.select" name="global.page.select"/>
<%-- 保存厂商URL --%>
<s:url id="save_url" value="/basis/BINOLBSFAC04_save"></s:url>
<%-- 城市查询URL --%>
<s:url id="getCity_url" value="/common/BINOLCM08_querySubRegion" />

<s:i18n name="i18n.bs.BINOLBSFAC04">
<div class="main container clearfix">
	<div class="panel ui-corner-all">
		<div class="panel-header">
		    <div class="clearfix">
		       <span class="breadcrumb left"> 
		          <span class="ui-icon icon-breadcrumb"></span>
					<s:text name="BSFAC04.manage"/> &gt; <s:text name="BSFAC04.title"/>
		       </span>
		    </div>
	  	</div>
	  	<%-- ================== 错误信息提示 START ======================= --%>
      	<div id="errorMessage"></div>
      	<%-- ================== 错误信息提示   END  ======================= --%>
      	<input type="hidden" id="mobileRule" name="mobileRule" value="<s:property value='mobileRule'/>"/>
	  	<div class="panel-content">
	  		<cherry:form id="mainForm" method="post" class="inline" csrftoken="false">
	  			<div id="actionResultDisplay"></div>
		  		<div class="section">
		            <div class="section-header">
		            	<strong>
		            		<span class="ui-icon icon-ttl-section-info-edit"></span>
		            		<s:text name="global.page.title"/><%-- 基本信息 --%>
		            	</strong>
		            </div>
		            <div id="base_info" class="section-content">
		                <table class="detail" cellpadding="0" cellspacing="0">
			              	<tr>
				                <th><s:text name="BSFAC04.manufacturerCode"/><span class="highlight">*</span></th><%-- 厂商编码 --%>
				                <td><span><s:textfield name="manufacturerCode" cssClass="text" maxlength="20"/></span></td>
				                <th><s:text name="BSFAC04.factoryNameCN"/><span class="highlight">*</span></th><%-- 厂商名称 --%>
				                <td><span><s:textfield name="factoryNameCN" cssClass="text" maxlength="30"/></span></td>
			             	</tr>
			             	<tr>
				                <th><s:text name="BSFAC04.factoryNameCNShort"/></th><%-- 厂商简称 --%>
				                <td><span><s:textfield name="factoryNameCNShort" cssClass="text" maxlength="20"/></span></td>
				                <th><s:text name="BSFAC04.factoryNameEN"/></th><%-- 厂商外文名 --%>
				                <td><span><s:textfield name="factoryNameEN" cssClass="text" maxlength="30"/></span></td>
			             	</tr>
			             	<tr>
				                <th><s:text name="BSFAC04.factoryNameENShort"/></th><%-- 厂商外文简称 --%>
				                <td><span><s:textfield name="factoryNameENShort" cssClass="text" maxlength="20"/></span></td>
				                <th><s:text name="BSFAC04.legalPerson"/></th><%-- 法人代表 --%>
				                <td><span><s:textfield name="legalPerson" cssClass="text" maxlength="30"/></span></td>
			             	</tr>
			             	<tr>
				                <th><s:text name="BSFAC04.telePhone"/></th><%-- 联系电话 --%>
				                <td><span><s:textfield name="telePhone" cssClass="text" maxlength="20"/></span></td>
				                <th><s:text name="BSFAC04.mobile"/></th><%-- 手机号码 --%>
				                <td><span><s:textfield name="mobile" cssClass="text" maxlength="20"/></span></td>
			             	</tr>
			             	<tr>
				                <th><s:text name="BSFAC04.levelCode"/></th><%-- 厂商等级 --%>
				                <td>
			                    	<s:select name="levelCode" list="#application.CodeTable.getCodes('1042')"
			              		 		listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{global.select}"/>
			                    </td>
				                <th><s:text name="BSFAC04.status"/></th><%-- 厂商状态 --%>
				                <td>
			                    	<s:select name="status" list="#application.CodeTable.getCodes('1042')"
			              		 		listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{global.select}"/>
			                    </td>
			             	</tr>
			             	<tr>
				                <th><s:text name="BSFAC04.brandName"/></th><%-- 所属品牌 --%>
				                <td>
			                    	<span>
			                    		<s:set id="def_brand" value="@com.cherry.cm.core.CherryConstants@BRAND_INFO_ID_VALUE"/>
				                    	<s:if test="brandInfoList.size() > 1">
				                    		<s:select name="brandInfoId" headerKey="%{def_brand}" headerValue="%{global.select}"
				                    			list="brandInfoList" listKey="brandInfoId" listValue="brandName"/>
				                    	</s:if>
				                    	<s:else>
				                    		<s:iterator value="brandInfoList">
									         	<s:property value='brandName'/>
									         	<s:hidden name="brandInfoId" value="%{brandInfoId}"/>
									      	</s:iterator>
				                    	</s:else>
			                    	</span>
			                    </td>
				                <th><s:text name="BSFAC04.defaultFlag"/></th><%-- 默认显示 --%>
				                <td>
			                    	<s:select name="defaultFlag1" list="#application.CodeTable.getCodes('1053')"
			              		 		listKey="CodeKey" listValue="Value"/>
			                    </td>
			             	</tr>
		         		</table>
	                </div>
	            </div>
	           	<div class="section">
		            <div class="section-header clearfix">
		            	<strong  class="left">
		            		<span class="ui-icon icon-ttl-section-info-edit"></span>
		            		<s:text name="BSFAC04.addressInfo"/><%-- 厂商地址 --%>
		            	</strong>
		            	<a class="add right" href="#" onclick="addDiv('#b_address','#h_address'); return false;">
			       			<span class="ui-icon icon-add"></span><span class="button-text"><s:text name="global.page.add"/><s:text name="BSFAC04.addressInfo"/></span>
			       		</a>
		            </div>
		            <div id="b_address" class="section-content">
	            		<table class="detail" cellpadding="0" cellspacing="0">
	            			<caption>
		                    	<span class="highlight"><s:text name="BSFAC04.def_address"/></span><%-- 默认地址 --%>
		                    	<input type="hidden" name="defaultFlag2" value="1"/>
		                  	</caption>
			              	<tr>
				                <th><s:text name="BSFAC04.provinceName"/></th><%-- 省份 --%>
				               	<td>
			                    	<s:select name="provinceId" headerKey="" headerValue="%{global.select}"
			                    		list="provinceList" listKey="provinceId" listValue="provinceName" 
			                    		onchange="BSFAC04_getCity(this,'%{getCity_url}','%{global.select}');"/>
			              		</td>
				                <th><s:text name="BSFAC04.cityName"/></th><%-- 城市 --%>
				                <td>
			                    	<select name="cityId">
			                    		<option value=""><s:text name="global.page.select"/></option>
			                    	</select>
			                    </td>
			             	</tr>
			             	<tr>
				                <th><s:text name="BSFAC04.addressType"/></th><%-- 地址类型 --%>
				                <td>
			                    	<s:select name="addressTypeId" list="#application.CodeTable.getCodes('1027')"
			              		 		listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{global.select}"/>
			                    </td>
				                <th><s:text name="BSFAC04.zipCode"/></th><%-- 邮编 --%>
				                <td><span><s:textfield name="zipCode" cssClass="text"/></span></td>
			             	</tr>
			             	<tr>
				                <th><s:text name="BSFAC04.address"/></th><%-- 地址 --%>
				                <td colspan="3"><span><s:textfield name="address" cssClass="text" cssStyle="width:750px" maxlength="100"/></span></td>
			             	</tr>
		             	</table>
	             	</div>
	           	</div>
	           	<%-- 操作按钮 --%>
	            <div id="saveButton" class="center clearfix">
	          		<cherry:show domId="BSFAC04SAVE">
	          			<button class="save" onclick="BSFAC04_saveFac('${save_url}');return false;">
		              		<span class="ui-icon icon-save"></span>
		              		<span class="button-text"><s:text name="global.page.save"/></span>
		              	</button>
	          		</cherry:show>
		            <button id="close" class="close" type="button"  onclick="doClose();return false;">
		           		<span class="ui-icon icon-close"></span>
		           		<span class="button-text"><s:text name="global.page.close"/></span>
		         	</button>
	            </div>
            </cherry:form>
	  	</div>
	</div>
</div>
<%--=================== 隐藏内容 ===================== --%>
<div class="hide">
	<div id="h_address" class="section-content">
		<table class="detail" cellpadding="0" cellspacing="0">
			<caption>
	        	<span class="left"><s:text name="BSFAC04.bkp_address"/></span><%-- 备用地址 --%>
		   		<a class="delete right" href="#" onclick="delDiv(this); return false;">
		   			<span class="ui-icon icon-delete"></span><span class="button-text"><s:text name="global.page.delete"/></span>
		   		</a>
	      	</caption>
		   	<tr>
			    <th><s:text name="BSFAC04.provinceName"/></th><%-- 省份 --%>
			   	<td>
			       	<s:select name="provinceId" headerKey="" headerValue="%{global.select}"
			       		list="provinceList" listKey="provinceId" listValue="provinceName" 
			       		onchange="BSFAC04_getCity(this,'%{getCity_url}','%{global.select}');"/>
			 		</td>
			    <th><s:text name="BSFAC04.cityName"/></th><%-- 城市 --%>
			    <td>
			       	<select name="cityId">
			       		<option value=""><s:text name="global.page.select"/></option>
			       	</select>
		       </td>
			</tr>
		  	<tr>
			    <th><s:text name="BSFAC04.addressType"/></th><%-- 地址类型 --%>
			    <td>
			       	<s:select name="addressTypeId" list="#application.CodeTable.getCodes('1027')"
			 		 		listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{global.select}"/>
		        </td>
			    <th><s:text name="BSFAC04.zipCode"/></th><%-- 邮编 --%>
			    <td><span><s:textfield name="zipCode" cssClass="text"/></span></td>
		  	</tr>
		  	<tr>
		      	<th><s:text name="BSFAC04.address"/></th><%-- 地址 --%>
		      	<td colspan="3">
		      		<span>
			      		<s:textfield name="address" cssClass="text" cssStyle="width:750px" maxlength="100"/>
		      		</span>
		      		<input type="hidden" name="defaultFlag2" value="0"/>
		      	</td>
		  	</tr>
	 	</table>
	</div>
</div>
</s:i18n>
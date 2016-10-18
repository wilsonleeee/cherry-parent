<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<s:i18n name="i18n.ss.BINOLSSPRM71">
<div class="detail_box" style="border: 2px solid #32b516;backgroud:#F3F9EB;margin:15px;width:94%;">
<div class="section">
    <div class="section-header">
      <strong>
      	<span class="ui-icon icon-ttl-section-search-result"></span>
       	<s:text name="PRM71_detail"/>
       	<span class="right">
       		<cherry:show domId="BINOLSSPRM71CON">
				<s:url id="insertIntoGroup_url" action="BINOLSSPRM71_insertIntoGroup">
					<s:param name="groupId" value="groupId"/>
				</s:url>
				<input id="insertIntoGroup_url" value="${insertIntoGroup_url }" type="hidden"/>
				<a href="javascript:void(0);" class="add" onclick="BINOLSSPRM71.insertIntoGroup(this);return false;">
	  		 		<span class="button-text"><s:text name="PRM71_conjunction"/></span>
	  		 		<span class="ui-icon icon-add"></span>
	  		 	</a>
       		</cherry:show>
		</span>
      </strong>
    </div>  	
  	<div class="section-content">
      <table cellpadding="0" cellspacing="0" border="0" class="jquery_table" id="detailPrmTable" style="width: 100%;">
        <tbody>
          <tr>
          	<th>No</th>
            <th><s:text name="PRM71_nameTotal" /></th>
            <th><s:text name="PRM71_unitCode" /></th>
            <th><s:text name="PRM71_barCode" /></th>
            <th><s:text name="PRM71_promCate" /></th>
            <th style="width: 46px;"><s:text name="PRM71_validFlag" /></th>
            <cherry:show domId="BINOLSSPRM71DEL">
            	<th style="width: 46px;"><s:text name="global.page.option" /></th>
            </cherry:show>
          </tr>
          <s:iterator value="detailPrmList" id="detailPrmMap" status="status">
	          <tr>          
	            <td>
	            	<s:property value="#status.index+iDisplayStart+1" />
	            	<input id="prmVendorId" name="addOnePrmPrmVendorId" class="hide" value="<s:property value="#detailPrmMap.BIN_ProductVendorID" />"/>
	            </td>
	            <td><s:property value="#detailPrmMap.NameTotal" /></td>
	            <td><s:property value="#detailPrmMap.UnitCode" /></td>
	            <td><s:property value="#detailPrmMap.BarCode" /></td>
	            <td><s:property value="#application.CodeTable.getVal('1139', #detailPrmMap.PromotionCateCD)"/></td>
	            <td class="center">
	            	<s:if test='"1".equals(#detailPrmMap.ValidFlag)'>
	            		<span class='ui-icon icon-valid'></span>
	            	</s:if>
					<s:else>
						<span class='ui-icon icon-invalid'></span>
					</s:else>
				</td>
				<cherry:show domId="BINOLSSPRM71DEL">
					<td class="center">
						<s:url id="delOnePrmUrl" value="BINOLSSPRM71_delOnePrm">
							<s:param name="prmVendorId" value="%{#detailPrmMap.BIN_ProductVendorID}"></s:param>
							<s:param name="groupId" value="%{#detailPrmMap.BIN_GroupID}"></s:param>
						</s:url>
						<a href="javascript:void(0);" onClick="BINOLSSPRM71.delOnePrm('${delOnePrmUrl }', this);return false;"><s:text name="global.page.delete"/></a>
					</td>
				</cherry:show>
	          </tr>
          </s:iterator>
        </tbody>
      </table>
    </div>
</div>
</div>  
	  	
</s:i18n>
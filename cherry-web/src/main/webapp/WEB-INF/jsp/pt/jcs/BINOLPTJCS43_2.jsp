<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<s:i18n name="i18n.pt.BINOLPTJCS43">
<div class="detail_box" style="border: 2px solid #32b516;backgroud:#F3F9EB;margin:15px;width:94%;">
<div class="section">
    <div class="section-header">
      <strong>
      	<span class="ui-icon icon-ttl-section-search-result"></span>
       	<s:text name="JCS43_detail"/>
       	<span class="right">
			<cherry:show domId="BINOLPTJCS43CON">
				<s:url id="insertIntoGroup_url" action="BINOLPTJCS43_insertIntoGroup">
					<s:param name="groupId" value="groupId"/>
				</s:url>
				<input id="insertIntoGroup_url" value="${insertIntoGroup_url }" type="hidden"/>
				<a href="javascript:void(0);" class="add" onclick="BINOLPTJCS43.insertIntoGroup(this);return false;">
	  		 		<span class="button-text"><s:text name="JCS43_conjunction"/></span>
	  		 		<span class="ui-icon icon-add"></span>
	  		 	</a>
			</cherry:show>
		</span>
      </strong>
    </div>  	
  	<div class="section-content">
      <table cellpadding="0" cellspacing="0" border="0" class="jquery_table" id="detailPrtTable" style="width: 100%;">
        <tbody>
          <tr>
          	<th>No</th>
            <th><s:text name="JCS43_nameTotal" /></th>
            <th><s:text name="JCS43_unitCode" /></th>
            <th><s:text name="JCS43_barCode" /></th>
            <th><s:text name="JCS43_mode" /></th>
            <th style="width: 46px;"><s:text name="JCS43_validFlag" /></th>
            <cherry:show domId="BINOLPTJCS43DEL">
            	<th style="width: 46px;"><s:text name="global.page.option" /></th>
            </cherry:show>
          </tr>
          <s:iterator value="detailPrtList" id="detailPrtMap" status="status">
	          <tr>          
	            <td>
	            	<s:property value="#status.index+iDisplayStart+1" />
	            	<input id="prtVendorId" name="addOnePrtPrtVendorId" class="hide" value="<s:property value="#detailPrtMap.BIN_ProductVendorID" />"/>
	            </td>
	            <td><s:property value="#detailPrtMap.NameTotal" /></td>
	            <td><s:property value="#detailPrtMap.UnitCode" /></td>
	            <td><s:property value="#detailPrtMap.BarCode" /></td>
	            <td><s:property value="#application.CodeTable.getVal('1136', #detailPrtMap.Mode)"/></td>
	            <td class="center">
	            	<s:if test='"1".equals(#detailPrtMap.ValidFlag)'>
	            		<span class='ui-icon icon-valid'></span>
	            	</s:if>
					<s:else>
						<span class='ui-icon icon-invalid'></span>
					</s:else>
				</td>
				<cherry:show domId="BINOLPTJCS43DEL">
					<td class="center">
						<s:url id="delOnePrtUrl" action="BINOLPTJCS43_delOnePrt">
							<s:param name="prtVendorId" value="%{#detailPrtMap.BIN_ProductVendorID}"></s:param>
							<s:param name="groupId" value="%{#detailPrtMap.BIN_GroupID}"></s:param>
						</s:url>
						<a href="javascript:void(0);" onClick="BINOLPTJCS43.delOnePrt('${delOnePrtUrl }',this);return false;"><s:text name="global.page.delete"/></a>
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
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<script type="text/javascript" src="/Cherry/js/common/popDataTable.js"></script>
<div id ="memImportDialog" class="hide">
   <%-- ================== 信息提示区 START ======================= --%>
	<div id="actionMessage_import"><s:actionmessage/></div>
	<s:if test='null != searchCode && !"".equals(searchCode)'>
	<div id="showMessageInfo">
		<div class="showSuccess">
			<ul>
				<li>
				<span >
					<s:text name="global.page.memImportBatName"/>:&nbsp;&nbsp;<strong><s:property value="recordName"/></strong>，<s:text name="global.page.memImportReadly"/>:&nbsp;&nbsp;<s:property value="recordCount"/>，
  						<s:if test='"1".equals(customerType)'><s:text name="global.page.memImportMember"/></s:if>
  						<s:if test='"2".equals(customerType)'><s:text name="global.page.memImportNonMember"/></s:if>
  						<s:if test='"3".equals(customerType)'><s:text name="global.page.memImportEmployee"/></s:if>
				</span>
				</li>
			</ul>
		</div>
	</div>
	</s:if>
	<hr class="space" />
	<%-- ================== 信息提示区   END  ======================= --%>
  	<div class="box2-active clearfix">
  		<input type="hidden" name="searchCode" value="<s:property value="searchCode"/>" />
  		<input type="hidden" name="customerType" value="<s:property value="customerType"/>" />
  		<input type="hidden" name="customerCount" value="<s:property value="recordCount"/>" />
  		<input type="hidden" name="comments" value="<s:text name='global.page.importComments'/>" />
  		<table class="detail">
  			<tbody>
  				<tr>
  					<th><s:text name="global.page.memImportBatName"/><span class="red">*</span></th>
  					<td><span><input class="text" maxlength="30"  name="recordName" value="<s:property value="recordName"/>"></span></td>
  				</tr>
  				<tr>
  					<th><s:text name="global.page.memImportType"/><span class="highlight">*</span></th>
  					<td>
  						<select id="importType_init"  <s:if test='null != searchCode && !"".equals(searchCode)'>class="hide"</s:if> style="width:186px;" disabled="disabled">
  							<option value=""><s:text name="global.page.memImportInit"/></option> 
  						</select>
  						<select id="importType_two" <s:if test='null == searchCode || "".equals(searchCode)'>class="hide"</s:if> style="width:186px;" name="importType" >
  							<option value="1"><s:text name="global.page.memImportAdd"/></option>
  							<option value="2"><s:text name="global.page.memImportUpdate"/></option>  						
  						</select>
  					</td>
  				</tr>
  				<tr>
  					<th><s:text name="global.page.memImport"/><span class="highlight">*</span></th>
  					<td>
  						<input type="text" name="pathExcel" id="pathExcel_import" class="input_text"><input type="button" value="<s:text name="global.page.browse"/>">
  						<input type="file" onchange="$('#pathExcel_import').val($(this).val());return false;" size="40" id="upExcel_import" name="upExcel" style="height:auto;" class="input_file">
  						<input type="button" id="upload" value="<s:text name="global.page.memImportExcel"/>" onclick="customerUpload();return false;">
  						<img height="15px" src="/Cherry/css/cherry/img/loading.gif" style="display: none;" id="loadingImg">
  					</td>
  				</tr>
  				<tr>
  					<th><s:text name="global.page.memFileDownLaod"/></th>
  					<td><a href="/Cherry/download/会员信息模板.xls"><span style="color:#3366FF"><s:text name="global.page.memInfoXls"/></span></a></td>
  				</tr>
  			</tbody>
  		</table>
  	</div>
  	<span id ="PopMemImportTitle" style="display:none"><s:text name="global.page.memImportTitle"/></span>
	<span id ="global_page_ok" style="display:none"><s:text name="global.page.ok"/></span>
	<span id ="select_file" style="display:none"><s:text name="ESS00031"/></span>
	<span id ="nameIsNull" style="display:none"><s:text name="MMP00004"/></span>
	<span id ="nameLength" style="display:none"><s:text name="MMP00005"/></span>
</div>

<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/pl/scf/BINOLPLSCF14.js"></script>

<s:i18n name="i18n.pl.BINOLPLSCF14">
<table>
	<tbody>
	<tr>
	<td><input type="checkbox" id="choiceFlag" name="choiceFlag" checked="checked" onclick="bscom03_checkRecord(this,'#dataTableBody');"/>
	<input type="hidden" id="indexFlag" value="0" name="indexFlag"/>
	<%-- 文件名 --%>
	<td><input type="text" id="fileName" name="fileName" readonly="readonly"></td>
	<%-- 组织代码 --%>
	<td><s:select cssStyle="width:100px;" list="orgInfoList" listKey="orgCode" listValue="orgName" name="orgCode" id="orgCode" onchange="BINOLPLSCF14.searchBrand(this);return false;"></s:select></td>
	<%-- 品牌代码 --%>
	<td><s:select cssStyle="width:100px;" list="brandInfoList" listKey="brandCode" listValue="brandName" name="brandCode" id="brandInfoId"></s:select></td>
 	<%-- 文件类别 --%>
	<td><s:select list='#application.CodeTable.getCodes("1149")' listKey="CodeKey" listValue="Value" name="fileCategory"></s:select></td>
	<%-- 文件代码 --%>
	<td><input type="text" id="fileCode" name="fileCode"></td>
	<%-- 操作 --%>
	<td><a class="delete" id="deleteBtn" onclick="BINOLPLSCF14.deleteFile(this);return false;"><span class="ui-icon icon-delete"></span><span class="button-text"><s:text name="delete"/></span>
	</a>
	</td>
	</tr>
	</tbody>
	</table>
</s:i18n>
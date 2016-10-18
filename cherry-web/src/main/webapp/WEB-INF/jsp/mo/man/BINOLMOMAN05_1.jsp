<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<tr>
     <td style="width:30%"><span id="_udiskSn"><s:property value="resultMap.udiskSn"/></span><input type="hidden" name="udiskSnArr" value="<s:property value='resultMap.udiskSn'/>"></td>
     <td style="width:15%"><s:property value="resultMap.employeeCode"/><input type="hidden" name="employeeIdArr" value="<s:property value='resultMap.employeeId'/>"></td>
     <td style="width:20%"><s:property value="resultMap.employeeName"/></td>
     <td style="width:20%"><s:property value="resultMap.categoryName"/><input name="gradeArr" type = "hidden" value="<s:property value="resultMap.grade"/>"></td>
     <td style="width:15%">
     	 <a class="delete" href="#" onclick="binOLMOMAN05.deleteUdiskDetail(this); return false;">
             <span class="ui-icon icon-delete"></span>
             <span class="button-text"><s:text name="global.page.delete"/></span>
         </a>
     </td>
</tr>
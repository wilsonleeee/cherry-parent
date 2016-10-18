<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap"%>
<s:i18n name="i18n.mo.BINOLMOMAN02">
    <table id="machineInfoArr" class="editable"> 
        <tbody>
            <tr><th><s:text name="MAN02_no"/></th>
                <th><s:text name="MAN02_machineCode"/></th>
                <th style="width:100px"><s:text name="MAN02_phoneCode"/></th>
                <th><s:text name="MAN02_machineType"/></th>
                <th style="min-width:100px"><s:text name="MAN02_comment"/></th>
                <th><s:text name="MAN02_operation"/></th>
            </tr>
            <s:iterator value="%{#request.parseddata}" id="machineinfo" status="rn">
                <tr>
                    <td id="row"><s:property value="#rn.index+1"/></td>
                    <td id="machineCodeCol"><s:property value="machineCode"/>
                    	<input type="hidden" name="machineCodeArr" value="<s:property value="machineCode"/>"/>
                    	<input type="hidden" name="importFlagArr" value="<s:property value="importFlag"/>"></input>
                    </td>
                    <td id="phoneCodeCol"><s:property value="phoneCode"/><input type="hidden" name="phoneCodeArr" value="<s:property value="phoneCode"/>"></td>
                    <td id="machineTypeCol">
                        <s:if test='machineType != null && !"".equals(machineType)'>
                            <s:property value='#application.CodeTable.getVal("1101", machineType)'/>
                        </s:if>
                        <input type="hidden" name="machineTypeArr" value="<s:property value="machineType"/>">
                    </td>
                    <td id="commentCol"><p><s:property value="comment"/></p><input type="hidden" name="commentArr" value="<s:property value="comment"/>"></td>
                    <td id="">
                        <a class="delete" href="#" onclick="BINOLMOMAN02.delDiv(this); return false;">
                            <span class="ui-icon icon-delete"></span>
                            <span class="button-text"><s:text name="global.page.delete"/></span>
                        </a>
                    </td>
                </tr>
           </s:iterator>
        </tbody>
    </table>
 </s:i18n>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  

<div id="exportWarnText" class="hide">
<p class="message" style="margin: 0;text-align: left;padding: 10px 0;background: none repeat scroll 0 0 #F3F9EB;">
<label><s:text name="global.page.invoiceFlag"></s:text></label>
<s:select list='#application.CodeTable.getCodes("1330")' listKey="CodeKey" listValue="Value" name="charset"></s:select>
</p>
<p class="message" style="margin: 0;text-align: left;padding: 5px 0 0;border-top: 1px dotted #CCCCCC;">
<s:text name="export.exportWarnMsg"></s:text>
</p>
</div>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<script type="text/javascript" src="/Cherry/js/common/popDataTable.js"></script>
<div id="factoryDialog" class="hide">
    <input type="text" class="text" name="sSearch" id="filterText" maxlength="30" onkeyup="factoryFilter(this);"/>
    <a class="search"><span class="ui-icon icon-search"></span><span class="button-text"><s:text name="global.page.searchfor"/></span></a>
  	<hr class="space" />
    <table id="factory_dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
       <thead>
            <tr>
               <th width="10%"><s:text name="global.page.Popselect"/></th><%--选择--%>
               <th width="45%"><s:text name="global.page.vendorcode"/></th><%--厂商代码--%>
               <th width="45%"><s:text name="global.page.vendorname"/></th><%--厂商名称--%>
            </tr>
        </thead>
        <tbody>
        </tbody>
   </table>
   <hr class="space" />
   <div class="center clearfix">
        <button class="confirm" id="factoryConfirm" onclick="selectFactory();"><span class="ui-icon icon-confirm"></span><span class="button-text"><s:text name="global.page.ok"/></span></button>
   </div>
</div>
<s:url action="BINOLCM02_popFactory" namespace="/common" id="popFactory" />
<span id ="popFactoryUrl" style="display:none">${popFactory}</span>
<span id ="PopvendorTitle" style="display:none"><s:text name="global.page.PopvendorTitle"/></span><%--厂商信息 --%>

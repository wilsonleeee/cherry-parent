<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<script type="text/javascript" src="/Cherry/js/pt/common/BINOLPTCOM01.js"></script>

<div id="lowerCounterDialog" class="hide">
	<div id="errorDiv_pop" class="actionError" style="display:none">
        <ul>
            <li><span id="errorSpan_pop"></span></li>
        </ul>         
    </div>
    <input id="lowerCounterText" type="text" class="text" onKeyup ="datatableFilter(this,111);"/>
    <a class="search" onclick="datatableFilter('#lowerCounterText',111);"><span class="ui-icon icon-search"></span><span class="button-text"><s:text name="global.page.searchfor"/></span></a>
  	<hr class="space" />
    <table id="lowerCounter_dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
       <thead>
            <tr>
               <th><s:text name="global.page.Popselect"/></th><%--选择 --%>
               <th><s:text name="global.page.departcode"/></th><%--代码--%>
               <th><s:text name="global.page.departname"/></th><%--名称 --%>
               <th><s:text name="global.page.parttype"/></th><%--类型 --%>
            </tr>
        </thead>
        <tbody>
        </tbody>
   </table>
   <div class="center clearfix">
        <button class="confirm" id="counterConfirm"><span class="ui-icon icon-confirm"></span><span class="button-text"><s:text name="global.page.ok"/></span></button>
   </div>
   <s:url action="BINOLPTCOM_popCounter" id="popLowerCounter" namespace="/pt"/>
<span id ="popLowerCounterUrl" style="display:none">${popLowerCounter}</span>
<span id ="PopdepartTitle" style="display:none"><s:text name="global.page.PopdepartTitle"/></span><%--部门信息 --%>
</div>

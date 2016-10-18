<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<script type="text/javascript" src="/Cherry/js/bs/common/BINOLBSCOM06.js"></script>

<div id="regionDialog1" class="hide">
    <input id="regionText" type="text" class="text" onKeyup ="datatableFilter(this,2);"/>
    <a class="search" onclick="datatableFilter('#regionText',2);"><span class="ui-icon icon-search"></span><span class="button-text"><s:text name="global.page.searchfor"/></span></a>
  	<hr class="space" />
    <table id="region_dataTable1" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
       <thead>
            <tr>
               <th><s:text name="global.page.Popselect"/></th><%--选择 --%>
               <th><s:text name="global.page.departcode"/></th><%--代码 --%>
               <th><s:text name="global.page.departname"/></th><%--名称 --%>
               <th><s:text name="global.page.parttype"/></th><%--类型 --%>
            </tr>
        </thead>
        <tbody>
        </tbody>
   </table>
   <div class="center clearfix">
        <button class="confirm" id="regionConfirm1"><span class="ui-icon icon-confirm"></span><span class="button-text"><s:text name="global.page.ok"/></span></button>
   </div>
   <span id ="ProvinceTitle" style="display:none"><s:text name="global.page.ProvinceTitle"/></span><%--省份信息 --%>
</div>


<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />

<s:url action="BINOLBSCOM01_popProvince" id="popProvince" />
<span id ="popProvinceUrl" style="display:none">${popProvince}</span>
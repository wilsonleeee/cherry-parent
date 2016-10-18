<#assign s=JspTaglibs["/WEB-INF/cherrytld/struts-tags.tld"]/>
<script type="text/javascript" src="/Cherry/js/common/popDataTable.js"></script>

<@s.i18n name="i18n.cp.BINOLCPCOM01">
<div id ="counterDialog" class="hide">
    <input type="text" class="text" onKeyup ="datatableFilter(this,9);" id="counterKw"/>
    <a class="search" onclick="datatableFilter('#counterKw',9);return false;"><span class="ui-icon icon-search"></span><span class="button-text"><@s.text name="cp.find" /></span></a>
  	<hr class="space" />
    <table id="counter_dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
       <thead>
            <tr>
               <th><@s.text name="cp.choice" /></th>
               <th><@s.text name="cp.counterNum" /></th>
               <th><@s.text name="cp.counterName" /></th>
            </tr>
        </thead>
        <tbody>
        </tbody>
   </table>
   <div class="center clearfix">
        <button class="confirm" id="counterConfirm"><span class="ui-icon icon-confirm"></span><span class="button-text"><@s.text name="cp.sure" /></span></button>
   </div>
</div>
<#include "/WEB-INF/ftl/common/dataTable_i18n.ftl" />

<@s.url action="BINOLCM02_getCounterList" namespace="/common" id="getCounterListUrl" />
<span id ="getCounterListUrl" style="display:none">${getCounterListUrl}</span>
</@s.i18n>
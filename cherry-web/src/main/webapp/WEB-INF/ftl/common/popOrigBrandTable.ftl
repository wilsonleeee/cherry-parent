<#assign s=JspTaglibs["/WEB-INF/cherrytld/struts-tags.tld"]/>
<@s.url id="origBrand_SearchUrl" value="/common/BINOLCM02_popOrigBrand" />


<div id ="origBrandDialog" class="dialog hide">
<@s.i18n name="i18n.cp.BINOLCPCOM01">
<button class="confirm" id="origBrandConfirm"><span class="ui-icon icon-confirm"></span><span class="button-text">全部子品牌</span></button>
<#--
	input type="text" class="text" value="" id="origBrandDialogSearch" onKeyup ="datatableFilter(this);" maxlength="50"/>
	<a class="search" onClick="datatableFilter(document.getElementById('origBrandDialogSearch'))">
		<span class="ui-icon icon-search"></span>
		<span class="button-text"><@s.text name="global.page.searchfor" /></span>
	</a>
-->
	<hr class="space" />
	<span class="bg_yew"><span class="highlight" style="line-height:25px;">* 需要选取部分子品牌时，请勾选下列项</span></span>
		<table id="origBrandDataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
		<thead>
			<tr>
				<th><@s.text name="global.page.Popselect"/></th>
				<th>子品牌代码</th>
				<th>子品牌名称</th>
			</tr>
		</thead>
		<tbody id="dataTableBody">
		</tbody>
		</table>
		<div class="center clearfix">
        	<button class="close" id="origBrandClose"><span class="ui-icon icon-close"></span><span class="button-text"><@s.text name="global.page.close"/></span></button>
   		</div>
	<hr>
	<span id ="origBrandSearchUrl" style="display:none">${origBrand_SearchUrl}</span>
	<span id ="PopOrigBrandTitle" style="display:none">
		子品牌选择框
	</span>
	
</div>
<#include "/WEB-INF/ftl/common/dataTable_i18n.ftl">
</@s.i18n>

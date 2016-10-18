<#assign s=JspTaglibs["/WEB-INF/cherrytld/struts-tags.tld"]/> 
<#assign cherry=JspTaglibs["/WEB-INF/cherrytld/cherrytags.tld"]/>
<#include "/WEB-INF/ftl/common/head.inc.ftl">
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/ct/rpt/BINOLCTRPT06_1.js"></script>
<style>
	.statistics_size{
		font-size:120%; 
	}
</style>
<script type="text/javascript">
	$(document).ready(
		function(){
			$("#startDateDetail").cherryDate({
				beforeShow: function(input){
					var value = $("#endDateDetail").val();
					return [value, "maxDate"];
				}
			});
			$("#endDateDetail").cherryDate({
				beforeShow: function(input){
					var value = $("#startDateDetail").val();
					return [value, "minDate"];
				}
			});
			cherryValidate({
				formId: "detailForm",		
				rules: {
					price : {required : true,pointValid:[6,3]},
					startDateDetail:{dateValid: true},
					endDateDetail:{dateValid: true}
				}		
			});
		}
	);
</script>
<@s.i18n name="i18n.ct.BINOLCTRPT06">
<@s.url id="detailSearch_url" action="BINOLCTRPT06_detailSearch"/>
<div class="hide">
	<a id="detailSearchUrl" href="${detailSearch_url}"></a>
</div>
<div class="panel-header">
    <div class="clearfix"> 
        <span class="highlight left"> 
        	<a href="#"><@s.text name="ctrpt06.communicationName" />:</a>
        	<span>
        		<#if communicationName?exists && communicationName != ''>
					${(communicationName)!?html}
				<#else>
					${(communicationCode)!?html}
				</#if>
			</span>
        </span>
    </div>
</div>
<div id="errorMessage"></div>
<div id="errorDiv2" class="actionError" style="display:none">
    <ul>
        <li><span id="errorSpan2"></span></li>
    </ul>         
</div>
<div class="panel-content clearfix">
	<div class="box">
		<form id="detailForm">
        	<div class="box-header">
    			<strong><span class="ui-icon icon-ttl-search"></span><@s.text name="ctrpt06.statisticsCondition" /></strong>
    		</div>
    		<div class="box-content clearfix">
    			<input type="hidden" name="brandInfoId" value="${(brandInfoId)!?html}"/>
    			<input type="hidden" name="communicationCode" value="${(communicationCode)!?html}"/>
				<div class="column" style="width:49%;">
	        		<p>
	        			<label style="width:90px"><@s.text name="ctrpt06.price"/></label>
	        			<span>
	        				<input name="price" class="text" type="text" />
	        			</span>
	        		</p>
                </div>
				<div class="column last" style="width:50%;">
					<p>
						<label style="width:90px"><@s.text name="ctrpt06.date"/></label>
						<span><@s.textfield  id="startDateDetail" name="startDateDetail"  cssClass="date"/></span>
						- 
						<span><@s.textfield  id="endDateDetail" name="endDateDetail" cssClass="date" /></span>
					</p>
                </div>
			</div>
			<p class="clearfix">
				<button class="right search"  onclick="BINOLCTRPT06_1.detailSearch();return false;">
	              	<span class="ui-icon icon-search-big"></span>
	              	<span class="button-text"><@s.text name="ctrpt06.search" /></span>
				</button>
			</p>
		</form>
	</div>
	<div id="memberDetailSection" class="section hide">
		<div class="section-header">
			<strong>
	        	<span class="ui-icon icon-ttl-section-search-result"></span>
	        	<@s.text name="ctrpt06.showFindResult" />
	        </strong>
			<span class="right">
				<a href="#" class="setting">
					<span class="button-text"><@s.text name="global.page.colSetting"/></span>
		 			<span class="ui-icon icon-setting"></span>
				</a>
			</span>
		</div>
		<div class="section-content">
			<div class="toolbar clearfix">
				 <span id="memCommunResultInfo" style="margin-left:10px;" class="statistics_size"></span>
			</div>
			<table id="dataTableDetail" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
			<thead>
				<tr>
					<th><@s.text name="ctrpt06.rowNumber" /></th>
					<th><@s.text name="ctrpt06.memberCode" /></th>
					<th><@s.text name="ctrpt06.memberName" /></th>
					<th><@s.text name="ctrpt06.orderQuantity" /></th>
					<th><@s.text name="ctrpt06.orderAmount" /></th>
					<th><@s.text name="ctrpt06.dealQuantity" /></th>
					<th><@s.text name="ctrpt06.dealAmount" /></th>
				</tr>
			</thead>
			<tbody>
			</tbody>
			</table>
		</div>
	</div>
</div>
<#include "/WEB-INF/ftl/common/dataTable_i18n.ftl">
</@s.i18n>
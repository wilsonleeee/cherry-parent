<#include "/WEB-INF/ftl/common/head.inc.ftl">
<#assign s=JspTaglibs["/WEB-INF/cherrytld/struts-tags.tld"]/> 
<#assign cherry=JspTaglibs["/WEB-INF/cherrytld/cherrytags.tld"]/>
<script type="text/javascript" src="/Cherry/js/common/ajaxfileupload.js"></script>
<script type="text/javascript" src="/Cherry/plugins/zTree v3.1/js/jquery.ztree.all-3.1.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryTree.js"></script>
<script type="text/javascript" src="/Cherry/js/mb/clb/BINOLMBCLB02.js"></script>
<link rel="stylesheet" href="/Cherry/plugins/zTree v3.1/css/zTreeStyle/zTreeStyle.css" type="text/css" media="screen, projection">
<script>
$(document).ready(function() {
	var clubMod = '${clubMod!}';
	if (clubMod == "1") {
		CHERRYTREE.loadTree('${placeJson!}',0);
	}
});
</script>
<@s.url id="save_url" action="BINOLMBCLB02_save"/>

<@s.i18n name="i18n.cp.BINOLCPCOM01">

	<div class="main container clearfix">
		<div class="panel ui-corner-all">
			<div class="panel-header">
				<div class="clearfix">
					<span class="breadcrumb left"> <span
						class="ui-icon icon-breadcrumb"></span> <@s.text name="cp.memclub" /> &gt; <@s.text name="cp.addmemclub" />
					</span>
				</div>
			</div>
			<div class="panel-content">
				<div id="actionResultDisplay"></div>
				<div class="section">
					<div class="section-header">
						<strong><span class="ui-icon icon-ttl-section-info-edit"></span>
							<@s.text name="cp.actBaseInfo" /></strong>
					</div>
					<div class="section-content">
					<@cherry.form id="mainForm" method="post" class="inline"
							csrftoken="false">
							<input type="hidden" name="clubMod" id="clubMod" value="${(clubMod)!}"/>
							<input type="hidden" name="clubSetBy" id="clubSetBy" value="${(clubSetBy)!}"/>
							<table id="baseInfo" class="detail" cellpadding="0"
								cellspacing="0">
								<tr>
									<th>
										 <label><@s.text name="cp.brandcode" />
									</label>
									</th>
									<td><span> 
									<#if (brandInfoList?? && brandInfoList?size > 0) >
					         			<@s.select id="brandSel" name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName"/>
					         		</#if>
									</span></td>
									<th>
										<label><@s.text name="cp.clubName" /></label>
									</th>
									<td><span> <@s.textfield name="clubName" cssClass = "text" maxlength="50"></@s.textfield>
									</span></td>
								</tr>
								<tr>
									<th>
										<label><@s.text name="cp.clubCd" /></label>
									</th>
									<td><span> <@s.textfield name="clubCode" cssClass = "text" maxlength="50"></@s.textfield>
									</span></td>
									<th><label><@s.text name="cp.clubNameEn" /></label></th>
									<td><span> 
											<@s.textfield name="clubNameForeign" cssClass = "text" maxlength="50"></@s.textfield>
									</span></td>
								</tr>
								<tr>
									<th><label><@s.text name="cp.clubDesp" /></label></th>
									<td><span> 
											<textarea name="descriptionDtl" style="height:50px;"></textarea>
									</span></td>
								</tr>
								
							</table>
							</@cherry.form>
					</div>
				</div>
				<#if (clubMod)! == "2">
				<div class="section">
					<div class="section-header">
						<strong><span class="ui-icon icon-ttl-section-info-edit"></span>
							子品牌</strong>
							<a onclick="BINOLMBCLB02.selOriginalBrand(this,$('#brandSel').serialize());return false;" style="float: none;" class="add"><span class="ui-icon icon-search"></span><span class="button-text">选择</span></a>
					</div>
					<div class="section-content">
						<span class="bg_yew"><span class="highlight" style="line-height:25px;">* 以下所选择的子品牌都属于该俱乐部</span></span>
			<table cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%;">
            <thead>
              <tr> 
                <th width="35%">子品牌代号</th>
                <th width="35%">子品牌名称</th>
                <th width="25%">操作</th>
              </tr>
            </thead>
            <tbody id="origBrand_tbody">
            	
				    </tbody>
					</table>
					</div>
				</div>
				
				<#else>
				<div class="section">
					<div class="section-header">
						<strong><span class="ui-icon icon-ttl-section-info-edit"></span><@s.text name="cp.clubCouset" /></strong>
					</div>
					<div class="section-content">
					<div class="box4">
						<div class="box4-header"><strong><@s.text name="cp.clubDizhi" /></strong></div>
						<div class="box4-content">
						<div style="padding:0px;margin-top:15px;" class="ztree jquery_tree box2 box2-active">
			            <div class="box2-header clearfix">
			            	<strong class="left active" style="font-size:14px;padding-top:2px;"><span class="ui-icon icon-flag" style="margin-top:5px;"></span><@s.text name="cp.clubSelCounter" /></strong>
			            	<span style="margin:3px 15px 0px 15px;" class="left">                      
			                   	<@s.text name="cp.clubLocationType" />
				            	<select id="locationType_0" name="locationType" onchange="CHERRYTREE.changeType(this,0);" style="width:120px;">
									<option value=""><@s.text name="global.page.select"/></option>
									<@getOptionList list=application.CodeTable.getCodes("1156") val= locationType! />
								</select>
			            	</span>
			            	<span id="importDiv_0" class="left <#if locationType! != "5">hide</#if>" style="margin-left:10px;">
								<a href="/Cherry/download/柜台信息模板.xls"><@s.text name="cp.clubModDown"/></a>
								<input class="input_text" type="text" id="pathExcel_0" name="pathExcel" style="height:20px;"/>
							    <input type="button" value="<@s.text name="global.page.browse"/>"/>
							    <input class="input_file" style="height:auto;margin-left:-314px;" type="file" name="upExcel" id="upExcel_0" size="40" onchange="pathExcel_0.value=this.value;return false;" /> 
				        		<input type="button" value="导入" onclick="CHERRYTREE.ajaxFileUpload('0');return false;"/>
				        		<img id="loadingImg_0" src="/Cherry/css/cherry/img/loading.gif" height="15px" style="display:none;">
							</span>
							
			            	<div id="locationId_0">
				            	<a onclick="CHERRYTREE.getPosition(0);return false;" class="search right" style="margin:3px 0 0 0;">
									<span class="ui-icon icon-position"></span>
									<span class="button-text"><@s.text name="cp.clubDingwei"/></span>
								</a>
								<input type="text" class="text right locationPosition ac_input" id="locationPositiontTxt_0" autocomplete="off">
							</div>
			          	</div>
			            <div id="tree_0" class="jquery_tree treebox tree" style="overflow:auto;height:300px;"></div>
			            <input type="hidden" name="placeJson" id="placeJson_0" value=""/>
			          	<input type="hidden" name="saveJson" id="saveJson_0" value=""/>
				   		</div>
				   		</div>
				   		</div>
					</div>
				</div>
				</#if>
				<div class="center clearfix" id="pageBrandButton">
					<button class="save" type="submit" onclick="BINOLMBCLB02.saveClub()">
						<span class="ui-icon icon-save"></span>
						<span class="button-text"><@s.text name="global.page.save" /></span>
					</button>
					<button class="close" onclick="window.close();">
						<span class="ui-icon icon-close"></span>
						<span class="button-text"><@s.text name="global.page.close" /></span>
					</button>
				</div>
			</div>
		</div>
	</div>
	<div class="hide">
		<a id="saveUrl" href="${save_url}"></a>
	</div>
<#macro getOptionList list val>
<#list list as code>
	<option <#if val == code.CodeKey>selected="selected"</#if> value="${code.CodeKey!}"><@s.text name="${code.Value!}" /></option>
</#list>
</#macro>
</@s.i18n>

<#include "/WEB-INF/ftl/common/popOrigBrandTable.ftl">

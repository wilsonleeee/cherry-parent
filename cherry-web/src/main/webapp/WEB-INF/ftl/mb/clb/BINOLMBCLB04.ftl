<#include "/WEB-INF/ftl/common/head.inc.ftl">
<#assign s=JspTaglibs["/WEB-INF/cherrytld/struts-tags.tld"]/> 
<#assign cherry=JspTaglibs["/WEB-INF/cherrytld/cherrytags.tld"]/>

<@s.i18n name="i18n.cp.BINOLCPCOM01">
	<div class="main container clearfix">
		<div class="panel ui-corner-all">
			<div class="panel-header">
				<div class="clearfix">
					<span class="breadcrumb left"> <span
						class="ui-icon icon-breadcrumb"></span> <@s.text name="cp.memclub" /> &gt; <@s.text name="cp.dtlmemclub" />
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
							<table id="baseInfo" class="detail" cellpadding="0"
								cellspacing="0">
								<tr>
									<th>
										 <label><@s.text name="cp.brandcode" /> 
									</label>
									</th>
									<td><span> 
									${(brandName)!}
									</span></td>
									<th>
										<label><@s.text name="cp.clubName" /></label>
									</th>
									<td><span> ${(clubInfo.clubName)!}
									</span></td>
								</tr>
								<tr>
									<th>
										<label><@s.text name="cp.clubCd" /></label>
									</th>
									<td><span> ${(clubInfo.clubCode)!}
									</span></td>
									<th><label><@s.text name="cp.clubNameEn" /></label></th>
									<td><span> 
											${(clubInfo.clubNameForeign)!}
									</span></td>
								</tr>
								<#if (clubInfo.validFlag)! == "0">
								<tr>
									<th>
										<label><@s.text name="cp.clubInvaildDate" /></label>
									</th>
									<td><span> ${(clubInfo.invalidDate)!}
									</span></td>
									<th><label><@s.text name="cp.clubInvaildRe" /></label></th>
									<td><span> 
											${(clubInfo.invalidReason)!}
									</span></td>
								</tr>
								</#if>
							
								<tr>
									<th><label><@s.text name="cp.clubDesp" /></label></th>
									<td><span> 
											${(clubInfo.descriptionDtl)!}
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
					</div>
					<div class="section-content">
						<span class="bg_yew"><span class="highlight" style="line-height:25px;">* 以下所选择的子品牌都属于该俱乐部</span></span>
			<table cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%;">
            <thead>
              <tr> 
              	<th width="10%">行号</th>
                <th width="45%">子品牌代号</th>
                <th width="45%">子品牌名称</th>
              </tr>
            </thead>
            <tbody id="origBrand_tbody">
            	<#if (origBrandList?? && origBrandList?size>0)>
	 				<#list origBrandList as origBrandInfo >
	 				<tr>
	 				<td>
	 					${origBrandInfo_index + 1}
	 				</td>
	 				<td>
	 					${(origBrandInfo.originalBrand)!}
	 				</td>
	 				<td>
	 					${(origBrandInfo.origBrandName)!}
	 				</td>
	 				</tr>
	 				</#list>
				</#if>
				    </tbody>
					</table>
					</div>
				</div>
				<#else>
				<div class="section">
					<div class="section-header">
						<strong><span class="ui-icon icon-ttl-section-info-edit"></span><@s.text name="cp.clubSelCounter" /></strong>
					</div>
					<div class="section-content">
						<div class="box2 box2-active">
						<div class="box2-header clearfix"><strong class="left active"><span class="ui-icon icon-flag"></span><@s.text name="cp.clubDizhi" /></strong></div>
						<div class="box2-content clearfix" style="padding:1em;">
							<div style="width:15%;float:left;">
								<span class="ui-icon icon-arrow-crm"></span>
								<@s.property value="#application.CodeTable.getVal('1156','${(clubInfo.locationType)!}')"/>
							</div>
							<#if placeList?exists && clubInfo.locationType !='0'>
								<div style="width:85%;float:left;">
									<#list placeList as placeMap>
										<#if placeMap.half=false>
											<span style="position: relative; margin-right:20px;">
											<#if placeMap.level=0>
											 <span style="color:#FF3030;white-space: nowrap;">${(placeMap.name)!}</span>
											</#if>
											<#if placeMap.level=1>
											 <span style="color:#FF7F24;white-space: nowrap;">${(placeMap.name)!}</span>
											</#if>
											<#if placeMap.level=2>
											 <span style="color:#4876FF;white-space: nowrap;">${(placeMap.name)!}</span>
											</#if>
											<#if placeMap.level=3>
											 <span style="color:#32CD32;white-space: nowrap;">${(placeMap.name)!}</span>
											</#if>
											</span>
										</#if>
									</#list>
								</div>
							</#if>
				     	</div>
					</div>
					</div>
				</div>
				</#if>
				<div class="center clearfix" id="pageBrandButton">
					<button class="close" onclick="window.close();">
						<span class="ui-icon icon-close"></span>
						<span class="button-text"><@s.text name="global.page.close" /></span>
					</button>
				</div>
			</div>
		</div>
	</div>
</@s.i18n>

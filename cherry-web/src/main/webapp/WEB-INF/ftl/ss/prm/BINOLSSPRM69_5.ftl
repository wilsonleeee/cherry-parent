<#include "/WEB-INF/ftl/common/head.inc.ftl" />
<script type="text/javascript" src="/Cherry/js/ss/prm/BINOLSSPRM69_2.js"></script>
<@s.i18n name="i18n.ss.BINOLSSPRM69">
<div class="main container clearfix" id="div_main">
	<div class="panel ui-corner-all">
		<div class="panel-header">
			<div class="clearfix">
				<span class="breadcrumb left"> 
					<span class="ui-icon icon-breadcrumb"></span>
	    			<a href="#"><@s.text name="SSPRM69_title2" /></a> > 
	    			<a href="#"><@s.text name="SSPRM69_relationSetTitle" /></a>
	    		</span>
			</div>
		</div>
		 <div id="errorDiv" class="actionError" style="display:none;">
			<ul>
				<li>
					<span id="errorSpan"></span>
				</li>
			</ul>
		</div>
		<div class="panel-content">
			<div class="section">
				<div class="section-header">
					<strong class="left"><span class="ui-icon icon-ttl-section-list"></span><@s.text name="SSPRM69_basicInfo"/></strong>
				</div>
				<div class="section-content">
					<table class="detail">
						<tr>
							<th><@s.text name="SSPRM69_brandName"/></th>
							<td>
								${brandName!}
								<input type="hidden" id="brandId" value="${brandId! }" />
							</td>
							<th><@s.text name="SSPRM69_relationGroupName"/></th>
							<td>${ruleRelationGroupMap.groupName!}
								<input type="hidden" id="groupNo" value="${ruleRelationGroupMap.groupNo!}" />
							</td>
						</tr>
						<tr>
							<th><@s.text name="SSPRM69_relationGroupComments"/></th>
							<td colspan="3">
								${ruleRelationGroupMap.comments! }
							</td>
						</tr>
					</table>
				</div>
			</div>
	    	<div class="section-header clearfix">
			  	<strong class="left"><span class="ui-icon icon-ttl-section-list"></span><@s.text name="SSPRM69_relationTitle"/></strong>
			</div>
			<div class="section-content">
				<table id="ruleRelationTable" border="0" cellpadding="0" cellspacing="0" width="100%">
			  		<thead>
					 	<tr>
			        		 <th scope="col" width="10%" ><@s.text name="No"/></th><#-- No. -->
					         <th scope="col"><@s.text name="SSPRM69_mutexContent"/></th><#-- 关联值A类型 -->
					    </tr>
					</thead>
					<tbody id="ruleRelationBody">
						<#if ruleRelationList?exists>
							<#list ruleRelationList as ruleRelation>
								<tr class="<#if ruleRelation_index % 2==0>odd<#else>even</#if> ADD" >
							 		<td>${ruleRelation_index + 1}</td>
							      	<td>
										<span class="classification_${ruleRelation.relationTypeA!}">
											<span>
												（${ruleRelation.relationValueA!}）
									      		<#if ruleRelation.relationTypeA == '1' >
									      			${application.CodeTable.getVal("1280","${(ruleRelation.relationValueA)!?html}")}
									      		<#else>
									      			${ruleRelation.nameA! }
									      		</#if>
								      		</span>
							 			</span>
							 			<span><@s.text name="SSPRM69_and"/></span>
							   			<span class="classification_${ruleRelation.relationTypeB!}">
							   				<span>
								   				（${ruleRelation.relationValueB!}）
									      		<#if ruleRelation.relationTypeB == '1' >
									      			${application.CodeTable.getVal("1280","${(ruleRelation.relationValueB)!?html}")}
									      		<#else>
									      			${ruleRelation.nameB! }
									      		</#if>
									      	</span>
							      		</span>
							      		<span><@s.text name="SSPRM69_mutex"/></span>
							      	</td>
							 	</tr>
							</#list>
						</#if>
					</tbody>
			    </table>
			</div>
	    	<div class="center">
		    	<button class="edit" onclick="PRM69_2.editRelation();return false;">
		    		<span class="ui-icon icon-edit"></span>
		    		<span class="button-text"><@s.text name="global.page.edit"/></span>
		    	</button>
		    	<button class="close" onclick="window.close();return false;">
		    		<span class="ui-icon icon-close"></span>
		    		<span class="button-text"><@s.text name="global.page.close"/></span>
		    	</button>
		    </div>
	    </div>
	</div>
</div>
</@s.i18n>
<@s.i18n name="i18n.ss.BINOLSSPRM69">
<div class="section-header clearfix">
  	<strong class="left"><span class="ui-icon icon-ttl-section-list"></span><@s.text name="SSPRM69_relationTitle"/></strong>
  	<a class="add right" onclick="PRM69_2.addRelation();return false;">
  		<span class="ui-icon icon-add"></span>
  		<span class="button-text"><@s.text name="SSPRM69_relationAdd"/></span>
  	</a>
</div>
<div class="section-content">
	<table id="ruleRelationTable" border="0" cellpadding="0" cellspacing="0" width="100%">
  		<thead>
		 	<tr>
        		 <th scope="col" width="10%"><@s.text name="No"/></th><#-- No. -->
		         <th scope="col" ><@s.text name="SSPRM69_mutexContent"/></th>
		         <th scope="col" width="15%" class="center"><@s.text name="SSPRM69_act"/></th><#-- 操作 -->
		    </tr>
		</thead>
		<tbody id="ruleRelationBody">
			<@ruleRelationBody ruleRelationList=ruleRelationList />
		</tbody>
    </table>
</div>
<#macro ruleRelationBody ruleRelationList = []>
	<#if ruleRelationList?exists>
		<#list ruleRelationList as ruleRelation>
			<tr>
		 		<td>
		 			<span class="No">${ruleRelation_index + 1}</span>
		 		</td>
		      	<td>
			      	<span onclick="PRM69_2.selectRelationVal(this,'A',false);return false;"  class="classification_${ruleRelation.relationTypeA!}" style="cursor: pointer;">
			      		<input class="TYPEA" name="relationTypeA" type="hidden" value="${ruleRelation.relationTypeA! }" />
			      		<input class="ICODEA" name="relationValueA" type="hidden" value="${ruleRelation.relationValueA! }" />
			      		<span class="DISPLAYA">
			      			(${ruleRelation.relationValueA! })
				      		<#if ruleRelation.relationTypeA == '1' >
				      			${application.CodeTable.getVal("1280","${(ruleRelation.relationValueA)!?html}")}
				      		<#else>
				      			${ruleRelation.nameA! }
				      		</#if>
			      		</span>
			      	</span>
			      	<span><@s.text name="SSPRM69_and"/></span>
			      	<span onclick="PRM69_2.selectRelationVal(this,'B',false);return false;"  class="classification_${ruleRelation.relationTypeB!}" style="cursor: pointer;">
			      		<input class="TYPEB" name="relationTypeB" type="hidden" value="${ruleRelation.relationTypeB! }" />
			      		<input class="ICODEB" name="relationValueB" type="hidden" value="${ruleRelation.relationValueB! }" />
			      		<span class="DISPLAYB">
			      			(${ruleRelation.relationValueB! })
				      		<#if ruleRelation.relationTypeB == '1' >
				      			${application.CodeTable.getVal("1280","${(ruleRelation.relationValueB)!?html}")}
				      		<#else>
				      			${ruleRelation.nameB! }
				      		</#if>
			      		</span>
			      	</span>
			      	<span><@s.text name="SSPRM69_mutex"/></span>
		      	</td>
		      	<td class="center">
					<a class="delete" onclick="PRM69_2.cancelAddRelaton(this,'B');return false;">
						<span class="ui-icon icon-delete"></span>
						<span class="button-text"><@s.text name="global.page.delete"/></span>
					</a>
		      	</td>
		 	</tr>
		</#list>
	</#if>
</#macro>
</@s.i18n>
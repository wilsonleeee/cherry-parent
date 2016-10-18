<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/pt/jcs/BINOLPTJCS01.js"></script>
<%-- 产品类别一览URL --%>
<s:url id="search_Url" action="BINOLPTJCS01_search"/>
<%-- 产品类别保存URL --%>
<s:url id="save_Url" action="BINOLPTJCS01_save"/>
<%-- 产品类别编辑URL --%>
<s:url id="edit_url" action="BINOLPTJCS01_edit"/>
<%-- 产品类别顺序调整URL --%>
<s:url id="move_Url" action="BINOLPTJCS01_move"/>
<%-- 品牌ID --%>
<s:set id="brandId" value="brandInfoId" />
<s:i18n name="i18n.pt.BINOLPTJCS01">
	<div class="panel-header">
		<div class="clearfix">
			<span class="breadcrumb left">	    
				<jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
			</span>
	   	</div>
	</div>
	<%-- ================== 错误信息提示 START ======================= --%>
    <div id="actionResultDisplay"></div>
    <%-- ================== 错误信息提示   END  ======================= --%>
    <div class="panel-content">
	    <div class="box3">
	          <label><strong><s:text name="JCS01.brandName"/></strong></label>
	          <s:select name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName"
	          	onchange="BINOLPTJCS01.search('%{search_Url}');return false;"/>
        </div>
	 	<div id="section" class="section">
	        <div class="section-header clearfix">
	        	<strong class="left"> 
	        		<span class="ui-icon icon-ttl-section-search-result"></span>
	        		<s:text name="JCS01.results_list"/>
	        	</strong>
	        	<%-- 添加分类 --%>
	        	<cherry:show domId="BINOLPTJCS0101">
			       	<a href="#" class="add right" id="add" onclick="BINOLPTJCS01.addLine('#dataTable','#newLine');return false;">
				 		<span class="ui-icon icon-add"></span>
				 		<span class="button-text"><s:text name="JCS01_add"/></span>
				 	</a>
			 	</cherry:show>
	        </div>
        	<div class="section-content " style="overflow-x: auto;">
	        	<cherry:form id="mainForm">
	        		<table cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%;">
			            <thead>
			              <tr>              
			                <%-- NO. --%>
			                <th style="width: 8%;"><s:text name="JCS01.number"/></th>
			                <%-- 分类中文名称 --%>
			                <th style="width: 25%;"><s:text name="JCS01.propValNameCN"/></th>
			                <%-- 分类英文名称 --%>
			                <th style="width: 25%;"><s:text name="JCS01.propValNameEN"/></th>
			                <%-- 终端下发  --%>
			                <th style="width: 15%;"><s:text name="JCS01.teminalFlag"/></th>
			                <%-- 显示顺序 --%>
				            <cherry:show domId="BINOLPTJCS0102"><th style="min-width: 65px;"><s:text name="JCS01.viewSeq"/></th></cherry:show>
			                <%-- 操作 --%>
			                <th style="width: 12%;" class="center"><s:text name="JCS01.option"/></th>
			              </tr>
			            </thead>
			            <tbody id="dataTable">
			            	<s:set id="size" value="categoryList.size()"/>
				            <s:iterator value="categoryList" status="status">
				            	<tr id="${status.index + 1}" class="<s:if test='#status.odd == true'>odd</s:if><s:else>even</s:else>" >
				            		<td>${status.index + 1}</td>
				            		<td>
				            			<s:if test="teminalFlag == 1">
				            				<span class="teminalTrans1" title='<s:text name="JCS01.teminalTrans1" />'><span><s:property value="propNameCN"/></span></span>
				            			</s:if>
				            			<s:elseif test="teminalFlag == 3">
				            				<span class="teminalTrans3" title='<s:text name="JCS01.teminalTrans3" />'><span><s:property value="propNameCN"/></span></span>
				            			</s:elseif>
				            			<s:elseif test="teminalFlag == 2">
				            				<span class="teminalTrans2" title='<s:text name="JCS01.teminalTrans2" />'><span><s:property value="propNameCN"/></span></span>
				            			</s:elseif>
				            			<s:else>
				            				<span><s:property value="propNameCN"/></span>
				            			</s:else>
				            		</td>
				            		<td><span><s:property value="propNameEN"/></span></td>
				            		<td>
				            			<s:if test="teminalFlag == 1">
				            				<span class="teminalTrans1" title='<s:text name="JCS01.teminalTrans1" />'><span><s:property value='#application.CodeTable.getVal("1119", 1)'/></span></span>
				            			</s:if>
				            			<s:elseif test="teminalFlag == 3">
				            				<span class="teminalTrans3" title='<s:text name="JCS01.teminalTrans3" />'><span><s:property value='#application.CodeTable.getVal("1119", 3)'/></span></span>
				            			</s:elseif>
				            			<s:elseif test="teminalFlag == 2">
				            				<span class="teminalTrans2" title='<s:text name="JCS01.teminalTrans2" />'><span><s:property value='#application.CodeTable.getVal("1119", 2)'/></span></span>
				            			</s:elseif>
				            		</td>
				            		<%-- 分类顺序调整 --%>
				            		<cherry:show domId="BINOLPTJCS0102">
				            		<td id="<s:property value='propId'/>_<s:property value='viewSeq'/>">
				            			<s:if test='#status.index != 0'>
					            			<a href="#" onclick="BINOLPTJCS01.move(this,'${move_Url}',${size},0);return false;" class="left" title="<s:text name='JCS01.first'/>"><span class="arrow-first"></span></a>
					            			<a href="#" onclick="BINOLPTJCS01.move(this,'${move_Url}',${size},1);return false;" class="left" title="<s:text name='JCS01.up'/>"><span class="arrow-up"></span></a>
				            			</s:if>
				            			<s:else>
					            			<span class="left" style="height:16px; width:16px; display:block;"></span>
					            			<span class="left" style="height:16px; width:16px; display:block;"></span>
				            			</s:else>
				            			<s:if test='#status.index != categoryList.size()-1'>
					            			<a href="#" onclick="BINOLPTJCS01.move(this,'${move_Url}',${size},2);return false;" class="left" title="<s:text name='JCS01.down'/>"><span class="arrow-down"></span></a>
					            			<a href="#" onclick="BINOLPTJCS01.move(this,'${move_Url}',${size},3);return false;" class="left" title="<s:text name='JCS01.last'/>"><span class="arrow-last"></span></a>
				            			</s:if>
				            		</td>
				            		</cherry:show>
				            		<td class="center">
				            			<%-- 产品类别配置选项URL --%>
										<s:url id="set_Url" action="BINOLPTJCS01_set"><s:param name="propId">${propId}</s:param></s:url>
										<span>
				            			<a href="#" onclick="BINOLPTJCS01.setting('${set_Url}');return false;"><s:text name="JCS01.setting"/></a><cherry:show domId="BINOLPTJCS0103">|<a 
				            				href="#" onclick="BINOLPTJCS01.edit('${edit_url}','<s:property value="propId"/>','${status.index + 1}');return false;"><s:text name="global.page.edit"/></a>
				            			</cherry:show>
				            			</span>
				            		</td>
				            	</tr>
				            </s:iterator>
			            </tbody>
          			</table>
	        	</cherry:form>
        	</div>
        </div>
    </div>
	<div class="hide">
		<table>
			<tbody id="newLine">
				<tr>
			      	<td></td><%-- NO. --%>
			  		<td>
			  			<span><input name="propNameCN" type="text" class="text"/><span 
			  			class="highlight"><s:text name="global.page.required"/></span></span><%-- 分类中文名称 --%>
			  		</td>
			  		<td><span><input name="propNameEN" type="text" class="text"/></span></td><%-- 分类英文名称 --%>
			  		<td>
			  			<span>
			  				<%-- 终端显示是否可操作flag --%>
							<input type="hidden" id="optFlag" value="1" disabled="disabled"/>
				  			<s:select name="teminalFlag" list="#application.CodeTable.getCodes('1119')"
				  				listKey="CodeKey" listValue="Value"/>
				  		</span><%-- 终端下发  --%>
			  		</td>
			  		<cherry:show domId="BINOLPTJCS0102"><td><span></span></td></cherry:show><%-- 显示顺序 --%>
			  		<td class="center"><%-- 操作 --%>
			  			<a href="#" onclick="BINOLPTJCS01.save(this,'${save_Url}');return false;"><s:text name="global.page.save"/></a>|<a 
			  				href="#" onclick="BINOLPTJCS01.delLine(this);return false;"><s:text name="global.page.cancle"/></a>
			  		</td>
			    </tr>
		    </tbody>
		</table>
	</div>
</s:i18n>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<s:i18n name="i18n.pt.BINOLPTJCS03">
<%-- ======================= 产品分类开始  ============================= --%>
<table class="detail" cellpadding="0" cellspacing="0" style="margin:0">
    <tbody>
    	<s:iterator value="cateList" status="st1">
			<tr>
				<s:if test="#st1.getIndex() < 3">
			    <th style="white-space: nowrap; width: 20%; overflow: hidden; min-width: 200px;">
			    </s:if>
			    <s:else>
			    <th class="hide teminalTransOth" style="white-space: nowrap; width: 20%; overflow: hidden; min-width: 200px;">
			    </s:else>
			    	<label><%-- 分类名 --%>
			    		<s:if test="teminalFlag == 1">
            				<span class="teminalTrans1" title='<s:text name="JCS03.teminalTrans1" />' style="margin:5px 0;"><span><s:property value="propName"/></span></span>
            				<span class="highlight"><s:text name="global.page.required"/></span>
            			</s:if>
            			<s:elseif test="teminalFlag == 2">
            				<span class="teminalTrans2" title='<s:text name="JCS03.teminalTrans2" />' style="margin:5px 0;"><span><s:property value="propName"/></span></span>
            				<span class="highlight"><s:text name="global.page.required"/></span>
            			</s:elseif>
            			<s:elseif test="teminalFlag == 3">
            				<span class="teminalTrans3" title='<s:text name="JCS03.teminalTrans3" />' style="margin:5px 0;"><span><s:property value="propName"/></span></span>
            			    <span class="highlight"><s:text name="global.page.required"/></span>
            			</s:elseif>
            			<s:else>
            				<span><s:property value="propName"/></span>
            			</s:else>
			    	</label>
			    	<span class="right" style="display: inline-block; float: none; vertical-align: middle;">
			    		<a onclick="BINOLPTJCS00.openCateDialog(<s:property value='propId'/>,this);return false;" class="add right">																
	            			<span class="ui-icon icon-search"></span>
							<span class="button-text"><s:text name="global.page.select" /></span>
						</a>
					</span>
					<input type="hidden" name="propId" value="<s:property value='propId'/>"/>
			    </th>
				<s:if test="#st1.getIndex() < 3">
			    <td style="width: 85%;white-space:normal;" id="cate_<s:if test='teminalFlag > 0'><s:property value="teminalFlag"/></s:if>">
			    </s:if>
			    <s:else>
			    <td style="width: 85%;white-space:normal;" id="cate_<s:if test='teminalFlag > 0'><s:property value="teminalFlag"/></s:if>" class="hide teminalTransOth">
			    </s:else>
			    	<table class="all_clean">
			    		<tbody id="prtCate_<s:property value='propId'/>">
			    			<s:iterator value="list">
			    			<tr>
			    				<td>
			    					<span class="list_normal">
			    						<span style="line-height: 19px;" class="text"><s:property value='propValName'/></span>
			    						<span onclick="BINOLPTJCS00.removeCate(this);return false;" style="margin: 4px 0px 0px 6px;" class="close">
			    							<span class="ui-icon ui-icon-close"></span>
			    						</span>
			    						<input type="hidden" value="<s:property value='propValId'/>" name="cateValId">
			    						<input type="hidden" value="<s:property value='propValId'/>" name="propValId">
			    					</span>
			    				</td>
			    			</tr>
			    			</s:iterator>
			    		</tbody>
			    	</table>
			    </td>
		  	</tr>
		</s:iterator>	
  	</tbody>
</table>
<%-- 将分类类别大于3个（终端分类为3个）时，显示“更多按钮” --%>
<s:if test="cateList.size() > 3">
	<%-- 除终端分类的其他分类默认隐藏0,显示1 --%>
	<input type="hidden" id="cateVisible" value="0"/>
	<div style="text-align: center; background: none repeat scroll 0% 0% rgb(238, 238, 238); padding: 3px 0px;">
		<a style="margin-left: 0px; font-size: 12px;" class="ui-select" onclick="BINOLPTJCS00.cateVisible('<s:text name="JCS03_showCate"/>','<s:text name="JCS03_hideCate"/>');return false;">
	        <span id="cateVisibleBtn" style="min-width:50px;" class="button-text choice"><s:text name="JCS03_showCate"/></span>
	 		<span id="cateNavi" class="ui-icon ui-icon-triangle-1-n"></span>
		</a>
	</div>
</s:if>
<%-- ======================= 产品分类结束  ============================= --%>
</s:i18n>
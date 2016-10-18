<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript">
//查询
function search(url){
	if (!$('#categoryCherryForm').valid()) {
		return false;
	};
	 // 查询参数序列化
	 var params= $("#categoryCherryForm").serialize();
	 url = url + "?" + params;
	 // 显示结果一览
	 $("#section").show();
	 // 表格设置
	 var tableSetting = {
			 // 表格ID
			 tableId : '#dataTable',
			 // 数据URL
			 url : url,
			 // 表格默认排序
			 aaSorting : [[ 2, "asc" ]],
			 // 表格列属性设置
			 aoColumns : [	
			              	{ "sName": "no","sWidth": "5%","bSortable": false},              //0
			              	{ "sName": "itemClassCode","sWidth": "30%"},					 //2
			              	{ "sName": "itemClassName","sWidth": "40%"},                     //1
							{ "sName": "curClassCode","sWidth": "15%"},	                     //3
							{ "sName": "validFlag","sWidth": "10%","sClass":"center"}],		 //4 
			// 不可设置显示或隐藏的列	
			aiExclude :[0, 1],
			// 横向滚动条出现的临界宽度
			sScrollX : "100%",
			// 固定列数
			fixedColumns : 2
	 };
	 // 调用获取表格函数
	 getTable(tableSetting);
}

//根据品牌改变类别
function cate_changeBrand(object,select_default) {
	
	var callback = function(msg){
		var jsons = eval('('+msg+')');
		$('#path').empty();
		$('#path').append('<option value="">'+select_default+'</option>');
		if(jsons.higherCategoryList.length > 0) {
			for(var i in jsons.higherCategoryList) {
				$('#path').append('<option value="'+jsons.higherCategoryList[i].path+'">'+jsons.higherCategoryList[i].itemClassName+'</option>');
			}
		}
	};
	cherryAjaxRequest({
		url: $('#filterByBrandInfoUrl').attr("href"),
		param: $(object).serialize(),
		callback: callback,
		formId: '#categoryCherryForm'
	});
}
</script>
				
   <%-- 促销品类别查询URL --%>
   <s:url id="search_url" value="/ss/BINOLSSPRM09_search"/>
   <div id="searchUrl" class="hide">${search_url}</div>
   
   <s:i18n name="i18n.ss.BINOLSSPRM09">
      <s:text name="select_default" id="select_default"></s:text>
      <div class="panel-content clearfix">
        <div class="box">
          <cherry:form id="categoryCherryForm" action="/Cherry/ss/search" method="post" class="inline" >
            <div class="box-header">
            	<strong><span class="icon icon-ttl-search"></span><s:text name="global.page.condition"/></strong>
              	<input type="checkbox" name="validFlag" value="1"/><s:text name="invalid_category"/>
            </div>
            <div class="box-content clearfix">
               <div class="column" style="width:50%; height:75px;">
                <s:if test="null != brandInfoList">
                  <p>
                    <%-- 所属品牌--%>
                    <label style="width: 80px;"><s:text name="prm09.brandName"/></label>
                    <s:a action="BINOLSSPRM09_filter" cssStyle="display:none;" id="filterByBrandInfoUrl"></s:a>
                    <s:select name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" headerKey="" headerValue="%{#select_default}" onchange="cate_changeBrand(this,'%{#select_default}');"></s:select>
                  </p>
                </s:if>
                <p>
                 <%-- 上级类别名称--%>
                  <label style="width: 80px;"><s:text name="prm09.higherCtegoryName"/></label>
                  <s:select list="higherCategoryList" name="path" listKey="path" listValue="itemClassName" headerKey="" headerValue="%{#select_default}"></s:select>
                 </p>
                <p>
                 <%-- 类别名称--%>
                  <label style="width: 80px;"><s:text name="prm09.itemClassName"/></label>
                  <s:textfield name="itemClassName" cssClass="text" maxlength="50"/> 
                 </p>
              </div>
              <div class="column last" style="width:49%; height:50px;"> 
	   			<p>
				<%-- 类别码--%>
                  <label style="width: 80px;"><s:text name="prm09.itemClassCode"/></label>
                  <s:textfield name="itemClassCode" cssClass = "text" maxlength="20"/>
                 </p>
                 <p>
                 <%-- 类别特征码--%>
                  <label style="width: 80px;"><s:text name="prm09.curClassCode"/></label>
                  <s:textfield name="curClassCode" cssClass = "text" maxlength="4"/>
                 </p>
              </div>
            </div>
            <p class="clearfix">
              	<%--<cherry:show domId="SSPRM0109QU">--%>
              		<%-- 促销品查询按钮 --%>
              		<button class="right search" type="button" onclick="search('<s:property value="#search_url"/>');return false;">
              			<span class="ui-icon icon-search-big"></span><span class="button-text"><s:text name="global.page.search"/></span>
              		</button>
              <%--	</cherry:show>--%>
            </p>
          </cherry:form>
        </div>
        <div id="section" class="section hide">
          <div class="section-header">
          	<strong>
          		<span class="ui-icon icon-ttl-section-search-result"></span>
          		<s:text name="global.page.list"/>
          	</strong>
          </div>
          
          <div class="section-content">
            <div class="toolbar clearfix">
            	<!--<span class="left">
            	    <%-- 全选 --%>
              		<input id="selectAll" type="checkbox" class="checkbox" onclick="select_All();"/><s:text name="prm09_selectAll"/>
              		
              		<cherry:show domId="SSPRM0109DB">
              		<%-- 类别停用按钮 --%>
              		<a href="#" id="disable_cate" class="delete" onclick="operate_prm(this,'<s:property value="#operate_url"/>',0)">
              			<span class="ui-icon icon-disable"></span>
              			<span class="button-text"><s:text name="prm09_disable"/></span>
       		 		</a>
       		 		</cherry:show>
       		 		
       		 		<cherry:show domId="SSPEM0109EB">
       		 		<%-- 类别启用按钮 --%>
       		 		<a href="#" id="enable_cate" class="add" onclick="operate_prm(this,'<s:property value="#operate_url"/>',1)">
       		 			<span class="ui-icon icon-enable"></span>
              			<span class="button-text"><s:text name="prm09_enable"/></span>
       		 		</a>
                    </cherry:show>   		 	
    
                    <cherry:show domId="SSPRM0109EXP">
              		<%-- 类别导出 --%>
              		<a href="#" class="export" id="export">
	              		<span class="button-text"><s:text name="export_results"/></span>
	       		 		<span class="ui-icon icon-import"></span>
	              	</a>
	              	 </cherry:show> 
              	</span> 
              	--><span class="right">
              		<%-- 列设置按钮  --%>
              		<a class="setting">
       		 			<span class="ui-icon icon-setting"></span>
       		 			<span class="button-text"><s:text name="global.page.colSetting"/></span>
       		 		</a>
              	</span>
            </div>
            
            <table id="dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
              <thead>
                <tr>
                  <%-- 选择 --%>
                  <!--<th><s:text name="prm09.select"/></th>-->
                  <%-- 编号 --%>
                  <th><s:text name="prm09.number"/></th>
                   <%-- 类别码 --%>
                  <th><s:text name="prm09.itemClassCode"/><span class="ui-icon ui-icon-document"></span></th>
                  <%-- 类别名称--%>
                  <th><s:text name="prm09.itemClassName"/></th>
                  <%-- 类别特征码 --%>
                  <th><s:text name="prm09.curClassCode"/></th>
                  <%-- 有效区分 --%>
                  <th><s:text name="prm09.validFlag"/></th>
                </tr>
               </thead>           
              </table>
             </div>
            </div>
           </div>
          </s:i18n>
          <%-- ================== dataTable共通导入 START ======================= --%>
          <jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
          <%-- ================== dataTable共通导入    END  ======================= --%>
 
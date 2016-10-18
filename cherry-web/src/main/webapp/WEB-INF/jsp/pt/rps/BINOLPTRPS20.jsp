<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/pt/rps/BINOLPTRPS20.js"></script>
<style>
    .box-header {
        padding: 0.2em 0;
    }
    table.detail {
         margin-bottom: 3px;
    }
</style>
<s:i18n name="i18n.pt.BINOLPTRPS20">
<div class="main container clearfix">
<div class="panel ui-corner-all">
    <div class="panel-header">
        <div class="clearfix">
            <span class="breadcrumb left"><span class="ui-icon icon-breadcrumb"></span><s:text name="RPS20_title"/>&nbsp;(<s:text name="RPS20_deliverNo"/>:<s:property value="returnInfo.deliverNo"/>)</span>
        </div>
    </div>
      <div class="panel-content">
        <div class="section">
          <div class="section-header"><strong><span class="ui-icon icon-ttl-section-info"></span>
          		<%-- 基本信息 --%>
          		<s:text name="global.page.title"/>
          	</strong>
          </div>
          <div class="section-content">
            <form id="mainForm" method="post" class="inline">
                <div class="box-header"></div>
                <table class="detail" cellpadding="0" cellspacing="0">
                    <tr>
                        <%-- 发货单号 --%>
                        <th><s:text name="RPS20_deliverNoIF"/></th>
                        <td><s:property value="returnInfo.deliverNoIF"/></td>
                        <%-- 日期 --%>
                        <th><s:text name="RPS20_date"/></th>
                        <td><s:property value="returnInfo.date"/></td>
                    </tr>
                    <tr>
                        <%-- 关联单号 --%>
                        <th><s:text name="RPS20_relevanceNo"/></th>
                        <td><s:property value="returnInfo.relevanceNo"/></td>
                        <%-- 状态 --%>
                        <th><s:text name="RPS20_verifiedFlag"/></th>
                        <td><s:property value='#application.CodeTable.getVal("1007", returnInfo.verifiedFlag)'/></td>
                    </tr>
                    <tr>
                        <%-- 理由 --%>
                        <th><s:text name="RPS20_reason"/></th>
                        <td colspan=3><s:property value="returnInfo.comments"/></td>
                    </tr>
                </table>
                <table class="detail" cellpadding="0" cellspacing="0">
                    <tr>
                        <%-- 发货部门 --%>
                        <th><s:text name="RPS20_binOrganizationID"/></th>
                        <td>(<s:property value="returnInfo.departCode"/>)<s:property value="returnInfo.sendDepart"/></td>
                        <%-- 接受部门 --%>
                        <th><s:text name="RPS20_receiveOrg"/></th>
                        <td>(<s:property value="returnInfo.receiveDepartCode"/>)<s:property value="returnInfo.receiveDepart"/></td>
                    </tr>
                </table>
                <div class="clearfix"></div>
            </form>
          </div>
        </div>
        <div class="section">
          <div class="section-header"><strong><span class="ui-icon icon-ttl-section-search-result"></span>
          	<%-- 调拨明细一览 --%>
            <s:text name="RPS20_results_list"/>
            </strong>
          </div>
          <div class="section-content">
            <div id="table_scroll" style="overflow-x:auto;overflow-y:hidden">
            <table cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
              <thead>
                <tr>
                  <th class="center"><s:text name="RPS20_num"/></th>
                  <th class="center"><s:text name="RPS20_unitCode"/></th>
                  <th class="center"><s:text name="RPS20_nameTotal"/></th>
                  <th class="center"><s:text name="RPS20_barCode"/></th>
                  <th class="center"><s:text name="RPS20_price"/></th>
                  <th class="center"><s:text name="RPS20_quantity"/></th>
                  <th class="center"><s:text name="RPS20_amount"/></th>
                  <th class="center"><s:text name="RPS20_comments"/></th>
                </tr>
              </thead>
              <tbody>
                <s:iterator value="returnList" status="status">
                	<tr class="<s:if test='#status.odd'>odd</s:if><s:else>even</s:else>">
                	  <td><s:property value="#status.index+1"/></td>
	                  <td><span><s:property value="unitCode"/></span></td>
	                  <td><span><s:property value="nameTotal"/></span></td>	
	                  <td><span><s:property value="barCode"/></span></td>
	                  <td style=text-align:right;><s:text name="format.price"><s:param value="price"></s:param></s:text></td>
	                  <td style=text-align:right;><s:text name="format.number"><s:param value="quantity"></s:param></s:text></td>
	                  <td style=text-align:right;><s:text name="format.price"><s:param value="price * quantity"></s:param></s:text></td>
	                  <td><p><s:property value="comments"/></p></td>
	                </tr>
                </s:iterator>
              </tbody>
            </table>
           	</div>
            <hr class="space" />
            <table cellpadding="0" cellspacing="0" width="30%" border="0" class="right editable">
              <tr>
                <th rowspan="2" scope="row" class="center"><s:text name="global.page.total"/></th><%-- 合计 --%>
                <td class="center"><s:text name="RPS20_totalQuantity"/></td><%-- 总数量 --%>
                <td class="center"><s:text name="RPS20_totalAmount"/></td><%-- 总金额--%>
              </tr>
              <tr>
                <td class="center"><s:text name="format.number"><s:param value="returnInfo.totalQuantity"></s:param></s:text></td>
             	<td class="center"><s:text name="format.price"><s:param value="returnInfo.totalAmount"></s:param></s:text></td>
              </tr>
            </table>
            <hr class="space" />
            <div class="center">
              <button class="close" onclick="window.close();"><span class="ui-icon icon-close"></span>
              <%-- 关闭 --%>
              <span class="button-text"><s:text name="global.page.close"/></span>
             </button>
            </div>
          </div>
        </div>
      </div>
    </div>
    </div>
</s:i18n>

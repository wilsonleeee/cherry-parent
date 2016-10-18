<%-- 产品信息二维码  datatable--%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.mo.BINOLPTJCS42">
    <div id="aaData">
    <s:iterator value="productQRCodeList" id="productQRCodeInfo">
    <ul>
        <li>
            <%-- No. --%>
            <s:property value="RowNumber"/>
        </li>
        <li>
            <%-- 产品名称 --%>
            <s:if test='productName != null && !"".equals(productName)'>
                <s:property value="productName"/>
            </s:if>
            <s:else>&nbsp;</s:else>
        </li>
        <li>
            <%-- 厂商编码 --%>
            <s:if test='unitCode != null && !"".equals(unitCode)'>
                <s:property value="unitCode"/>
            </s:if>
            <s:else>&nbsp;</s:else>
        </li>
        <li>
            <%-- 产品条码 --%>
            <s:if test='barCode != null && !"".equals(barCode)'>
                <s:property value="barCode"/>
            </s:if>
            <s:else>&nbsp;</s:else>
        </li>
        <li>
            <%-- 经销商编号名称 --%>
            <s:if test='resellerCodeName != null && !"".equals(resellerCodeName)'>
                <s:property value="resellerCodeName"/>
            </s:if>
            <s:else>&nbsp;</s:else>
        </li>
        <li>
            <%-- 二维码 --%>
            <s:if test='qrCodeCiphertext != null && !"".equals(qrCodeCiphertext)'>
                <s:property value="qrCodeCiphertext"/>
            </s:if>
            <s:else>&nbsp;</s:else>
        </li>
        <li>
            <%-- 完整URL --%>
            <s:if test='wholeURL != null && !"".equals(wholeURL)'>
                <s:property value="wholeURL"/>
            </s:if>
            <s:else>&nbsp;</s:else>
        </li>
        <li>
            <%-- 有效区分 --%>
            <s:if test="validFlag ==1">
                <span class='ui-icon icon-valid'></span>
            </s:if>
            <s:elseif test="validFlag == 0">
                <span class='ui-icon icon-invalid'></span>
            </s:elseif>
            <s:else>
                <span></span>
            </s:else>
        </li>
    </ul>
    </s:iterator>
    </div>
</s:i18n>
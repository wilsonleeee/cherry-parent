<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<title>WITPOS店务通</title>
<link rel="stylesheet" href="../css/screen.css" type="text/css" media="screen, projection">
<link rel="stylesheet" href="../css/jquery-ui-1.8.5.custom.css" type="text/css" media="screen, projection">
<link rel="stylesheet" href="../css/cherry.css" type="text/css" media="screen, projection">
<script type="text/javascript" src="../js/jquery-1.6.min.js"></script>
<script type="text/javascript" src="../js/common/commonAjax.js"></script>
<script type="text/javascript" src="../js/common/cherry.js"></script>
<script type="text/javascript" src="../js/ss/pro/BINBESSPRO03.js"></script>
<style>
.dataTables_processing {
    position: absolute;
    top: 50%;
    left: 50%;
    width: 250px;
    height: 30px;
    margin-left: -125px;
    margin-top: -15px;
    padding: 14px 0 2px 0;
    border: 1px solid #FAD163;
    text-align: center;
    color: black;
    font-size: 14px;
    background-color: #FFEC9D;
    z-index:100;
}
</style>
<s:a action="BINBESSPRO03_getCounter" id="getCounter" cssStyle="display: none;"></s:a>
<s:a action="BINBESSPRO03_getDiff" id="getDiff" cssStyle="display: none;"></s:a>
<div id="resultDiv" class="">
<div id="actionResultDisplay"></div>
<div class="main container clearfix">
<div class="section">
    <div class="section-content">
        <div id="errorMessage" class="hide">
            <div id="maxShow" class="actionError"></div>
        </div>
        <hr/>
        <form action="BINBESSPRO03_export" id="exportForm">
            <input type="checkbox" name="filterStockNotExist" id="filterStockNotExist" />过滤新后台库存不存在
            <input type="checkbox" name="filterPrtNotExist" id="filterPrtNotExist" />过滤新后台产品不存在
            <br />
            <input type="hidden" name="brandInfoId"  value="${brandInfoId}"/>
                                 柜台编号 <input type="text" id="counterCode" name="counterCode" value=""/>
            <button onclick="BINBESSPRO03.search();return false;">查询</button>
            <button type="submit">导出</button><br/>
                                入出库日期<input type="text" id="stockInOutDate" name="stockInOutDate" value=""/>
            <span><span class="highlight">※</span>&nbsp;<span class="red">执行同步时必填</span></span>
        </form>
        <table border="0" cellpadding="0" cellspacing="0" width="100%" style="margin-bottom: 10px;">
            <thead>
                <tr>
                    <th style="width:1%">No.</th>
                    <th style="width:15%">柜台</th>
                    <th style="width:20%">产品名称</th>
                    <th style="width:20%">厂商编码</th>
                    <th style="width:20%">产品条码</th>
                    <th style="width:10%">实体仓库</th>
                    <th style="width:10%">逻辑仓库</th>
                    <th style="width:10%">新后台数量</th>
                    <th style="width:10%">老后台数量</th>
                </tr>
            </thead>
            <tbody id="aaData">

            </tbody>
        </table>
        <div class="center">
            <s:a action="BINBESSPRO03_Exec" id="exec" cssStyle="display: none;"></s:a>
            <input type="hidden" id="brandInfoId" value="${brandInfoId}"></input>
            <button class="save" onclick="BINBESSPRO03.save();return false;"><span class="ui-icon icon-confirm"></span><span class="button-text">执行同步</span></button>
            <button class="close" type="button" onclick="window.close();return false;"><span class="ui-icon icon-close"></span><span class="button-text">关闭</span></button>
        </div>
    </div>
    <div id="dataTable_processing" class="dataTables_processing hide"  style="text-algin:left;"><label>加载中</label></div>
</div>
</div>
</div>
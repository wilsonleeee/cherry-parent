<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>
<title>WITPOS店务通</title>
<link rel="stylesheet" href="../css/screen.css" type="text/css" media="screen, projection">
<link rel="stylesheet" href="../css/jquery-ui-1.8.5.custom.css" type="text/css" media="screen, projection">
<link rel="stylesheet" href="../css/cherry.css" type="text/css" media="screen, projection">
<script type="text/javascript" src="../js/jquery-1.6.min.js"></script>
<script type="text/javascript" src="../js/common/commonAjax.js"></script>
<script type="text/javascript" src="../js/common/cherry.js"></script>
<script type="text/javascript" src="../js/ss/prm/BINBESSPRM06.js"></script>
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
<%
SimpleDateFormat dateFm = new SimpleDateFormat("yyyy-MM-dd"); //格式化当前系统日�
String dateTime = dateFm.format(new java.util.Date());
%>
<s:a action="BINBESSPRM06_getDepart" id="getDepart" cssStyle="display: none;"></s:a>
<s:a action="BINBESSPRM06_getDiff" id="getDiff" cssStyle="display: none;"></s:a>
<s:a action="BINBESSPRM06_export" id="downUrl" cssStyle="display: none;"></s:a>
<div id="resultDiv" class="">
<div id="actionResultDisplay"></div>
<div class="main container clearfix">
<div class="section">
    <div class="section-content">
        <div id="errorMessage" class="hide">
            <div id="maxShow" class="actionError"></div>
        </div>
        <hr/>
        <div>注意：执行平促销品库存前必须停止MQ</div>
        <form action="BINBESSPRM06_export" id="exportForm">
	        <select id="testType" name="testType">
	            <option value="1">测试仓库</option>
	            <option value="0">正式仓库</option>
	        </select>
	        <input type="hidden" name="brandInfoId"  value="${brandInfoId}"/>
	        <button onclick="BINBESSPRM06.search();return false;">查询</button>
	        <button type="submit">导出</button>
	        <br/>
                                入出库日期 <input type="text" id="stockInOutDate" name="stockInOutDate" value="<%= dateTime %>"/>
        </form>
        <table border="0" cellpadding="0" cellspacing="0" width="100%" style="margin-bottom: 10px;">
            <thead>
                <tr>
                    <th style="width:1%">No.</th>
                    <th style="width:15%">部门</th>
                    <th style="width:10%">实体仓库</th>
                    <th style="width:10%">逻辑仓库</th>
                    <th style="width:20%">促销品名称</th>
                    <th style="width:20%">厂商编码</th>
                    <th style="width:20%">促销品条码</th>
                    <th style="width:10%">实际库存</th>
                    <th style="width:10%">入出库统计库存</th>
                </tr>
            </thead>
            <tbody id="aaData">

            </tbody>
        </table>
        <div class="center">
            <s:a action="BINBESSPRM06_Exec" id="exec" cssStyle="display: none;"></s:a>
            <input type="hidden" id="brandInfoId" value="${brandInfoId}"></input>
            <button class="save" onclick="BINBESSPRM06.save();return false;"><span class="ui-icon icon-confirm"></span><span class="button-text">执行平促销品库存</span></button>
            <button class="close" type="button" onclick="window.close();return false;"><span class="ui-icon icon-close"></span><span class="button-text">关闭</span></button>
        </div>
    </div>
    <div id="dataTable_processing" class="dataTables_processing hide"  style="text-algin:left;"><label>加载中</label></div>
</div>
</div>
</div>
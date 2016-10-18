/*
 * @(#)BINOLPTRPS33_BL.java     1.0 2014/9/24
 * 
 * Copyright (c) 2010 SHANGHAI BINGKUN DIGITAL TECHNOLOGY CO.,LTD
 * All rights reserved
 * 
 * This software is the confidential and proprietary information of 
 * SHANGHAI BINGKUN.("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with SHANGHAI BINGKUN.
 */
package com.cherry.pt.rps.bl;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM37_BL;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM37_IF;
import com.cherry.cm.cmbussiness.service.BINOLCM00_Service;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.core.PropertiesUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mo.common.interfaces.BINOLMOCOM01_IF;
import com.cherry.pt.rps.interfaces.BINOLPTRPS33_IF;
import com.cherry.pt.rps.service.BINOLPTRPS33_Service;

/**
 * 
 * 电商订单一览BL
 * 
 * @author niushunjie
 * @version 1.0 2014.9.24
 */
@SuppressWarnings("unchecked")
public class BINOLPTRPS33_BL implements BINOLPTRPS33_IF,BINOLCM37_IF{

    @Resource(name="binOLCM00_Service")
    private BINOLCM00_Service binOLCM00_Service;
    
    @Resource(name="binOLMOCOM01_BL")
    private BINOLMOCOM01_IF binOLMOCOM01_BL;
    
    @Resource(name="binOLPTRPS33_Service")
    private BINOLPTRPS33_Service binOLPTRPS33_Service;
    
    @Resource(name="binOLCM14_BL")
    private BINOLCM14_BL binOLCM14_BL;
    
    @Resource(name="binOLCM37_BL")
    private BINOLCM37_BL binOLCM37_BL;
    
    @Resource(name="CodeTable")
    private CodeTable codeTable;
    
    // Excel导出数据查询条件数组
    private final static String[] proCondition = { "billCode", "memCode",
            "employeeCode", "saleType", "consumerType", "campaignName",
            "regionId", "provinceId", "cityId", "countyId", "channelId",
            "departId", "saleDate" };
    
    // Excel导出列数组
    private final static String[][] proArray = {
            { "billCode", "RPS33_billCode", "40", "", "" },// 单据号
            { "departCode", "RPS33_departCode", "15", "", "" },// 柜台号
            { "departName", "RPS33_counterName", "", "", "" },// 柜台
            { "employeeName", "RPS33_employeeName", "", "", "" },// BA姓名
            { "employeeCode", "RPS33_baCode", "", "", "" },// BACode
            { "memberCode", "RPS33_memCode", "15", "", "" },// 会员卡号
            { "memberName", "RPS33_memName", "15", "", "" },// 会员姓名
            { "unitCode", "RPS33_unitCode", "20", "", "" },// 厂商编码
            { "barCode", "RPS33_barCode", "20", "", "" },// 产品条码
            { "productName", "RPS33_productName", "20", "", "" },// 产品名称
            { "buyQuantity", "RPS33_buyQuantity", "", "", "" },// 购买数量
            { "buyAmount", "RPS33_buyAmount", "", "", "" },// 购买金额
            { "saleType", "RPS33_saleType", "", "", "1055" },// 销售类型
            { "productType", "RPS33_productType", "", "", "1136" },// 产品类型
            { "inActivity", "RPS33_inActivity", "30", "", "" },// 参与活动
            { "billCreateTime", "RPS33_billCreateTime", "20", "", "" } // 时间
    };
    
    @Override
    public int getESOrderMainCount(Map<String, Object> map) {
        return binOLPTRPS33_Service.getESOrderMainCount(map);
    }

    @Override
    public List<Map<String, Object>> getESOrderMainList(Map<String, Object> map) {
        return binOLPTRPS33_Service.getESOrderMainList(map);
    }

    @Override
    public Map<String, Object> getSumInfo(Map<String, Object> map) {
        List<Map<String, Object>> sumInfoList = binOLPTRPS33_Service.getSumInfo(map);
        Map<String, Object> sumInfoMap = sumInfoList.get(0);

        // 如果查询条件中单据状态是退货，那么将总金额和总数量由负转换成正
        if ("SR".equals(map.get("saleType"))) {
            sumInfoMap.put("sumQuantity", 0 - Double.parseDouble(String.valueOf(sumInfoMap.get("sumQuantity"))));
            sumInfoMap.put("sumAmount", 0 - Double.parseDouble(String.valueOf(sumInfoMap.get("sumAmount"))));
        }
        return sumInfoMap;
    }

    @Override
    public byte[] exportExcel(Map<String, Object> map) throws Exception {
        //需导出数据总量
        int dataLen = binOLPTRPS33_Service.getExportDetailCount(map);
        map.put("dataLen", dataLen);
        BINOLMOCOM01_IF.ExcelParam ep = new BINOLMOCOM01_IF.ExcelParam();
        //必须设置sqlID（必须）,用于批查询
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS33.getExportDetailList");
        ep.setMap(map);
        
        String organizationInfoID = ConvertUtil.getString(map.get("organizationInfoId"));
        String brandInfoID = ConvertUtil.getString(map.get("brandInfoId"));
        String configValue = binOLCM14_BL.getConfigValue("1140", organizationInfoID, brandInfoID);
        if(configValue.equals(CherryConstants.SYSTEM_CONFIG_ENABLE)){
            //导出数据列数组（有唯一码）
            Vector<String[]> vector = new Vector<String[]>();
            for (int i = 0; i < proArray.length; i++) {
                vector.add(proArray[i]);
                if(i == proArray.length-1){
                    String[] uniCodeArr = { "uniqueCode", "RPS33_uniqueCode", "25", "", "" };// 唯一码
                    vector.add(uniCodeArr);
                }
            }
            String[][] newArrays = new String[vector.size()][vector.get(0).length];
            vector.toArray(newArrays);
            ep.setArray(newArrays);
        }else{
            // 导出数据列数组
            ep.setArray(proArray);
        }
        ep.setShowTitleStyle(true);
        // 导出数据列头国际化资源文件
        ep.setBaseName("BINOLPTRPS33");
        ep.setSearchCondition(getConditionStr(map));
        return binOLMOCOM01_BL.getBatchExportExcel(ep);
    }

    @Override
    public String exportCSV(Map<String, Object> map) throws Exception {
        // 获取导出参数
        Map<String, Object> exportMap = this.getExportParam(map);
        
        String language = (String)map.get(CherryConstants.SESSION_CHERRY_LANGUAGE);
        
        String tempFilePath = PropertiesUtil.pps.getProperty("tempFilePath");
        String sessionId = (String)map.get("sessionId");
        // 下载文件临时目录
        tempFilePath = tempFilePath + File.separator + sessionId;
        exportMap.put("tempFilePath", tempFilePath);
        
        // 下载文件名
        String downloadFileName = binOLCM37_BL.getResourceValue("BINOLPTRPS33", language, "RPS33_exportName");
        exportMap.put("tempFileName", downloadFileName);
        
        // 导出CSV处理
        boolean result = binOLCM37_BL.exportCSV(exportMap, this);
        if(result) {
            // 压缩包名
            String zipName = downloadFileName+".zip";
            // 压缩文件处理
            result = binOLCM37_BL.fileCompression(new File(tempFilePath+File.separator+downloadFileName+".csv"), zipName);
            if(result) {
                return tempFilePath+File.separator+zipName;
            }
        }
        return null;
    }

    @Override
    public String getExportName(Map<String, Object> map) throws Exception {
        String language = ConvertUtil.getString(map.get(CherryConstants.SESSION_LANGUAGE));
        String exportName = binOLMOCOM01_BL.getResourceValue("BINOLPTRPS33", language, "RPS33_exportName");
        exportName = new String(
                exportName.getBytes(CherryConstants.CHAR_ENCODING_GBK),
                CherryConstants.CHAR_ENCODING_ISO)
                + CherryConstants.POINT + CherryConstants.EXPORTTYPE_XLS;
        return exportName;
    }

    @Override
    public int getExportDetailCount(Map<String, Object> map) {
        return binOLPTRPS33_Service.getExportDetailCount(map);
    }
    
    /**
     * 取得条件字符串
     * 
     * @param map
     * @return
     */
    private String getConditionStr(Map<String, Object> map) {
        // 防止查询相关区域、城市、部门名称时篡改ibatis_sql_id
        Map<String, Object> conditionMap = new HashMap<String, Object>();
        conditionMap.putAll(map);
        String language = ConvertUtil.getString(conditionMap
                .get(CherryConstants.SESSION_LANGUAGE));
        StringBuffer condition = new StringBuffer();
        int lineNum = 0;
        for (String con : proCondition) {
            // 条件值
            String paramValue = ConvertUtil.getString(conditionMap.get(con));
            if (con.equals("saleDate")) {// 销售日期始终显示
                paramValue = "showSaleDate";
            }
            if (!"".equals(paramValue)) {
                // 条件名
                String paramName = "";
                if ("billCode".equals(con)) {
                    // 销售单据
                    paramName = binOLMOCOM01_BL.getResourceValue(
                            "BINOLPTRPS33", language, "RPS33_billCode");
                } else if ("memCode".equals(con)) {
                    // 会员卡号
                    paramName = binOLMOCOM01_BL.getResourceValue(
                            "BINOLPTRPS33", language, "RPS33_memCode");
                } else if ("saleDate".equals(con)) {
                    // 下单日期
                    paramName = binOLMOCOM01_BL.getResourceValue(
                            "BINOLPTRPS33", language, "RPS33_date");
                    // 日期
                    String startDate = (map.get("startDate") != null) ? ConvertUtil
                            .getString(map.get("startDate")).replace('-', '/')
                            : "----/--/--";
                    String endDate = (map.get("endDate") != null) ? ConvertUtil
                            .getString(map.get("endDate")).replace('-', '/')
                            : "----/--/--";
                    paramValue = startDate + "\0~\0" + endDate;
                } else if ("employeeCode".equals(con)) {
                    // 销售人员号
                    paramName = binOLMOCOM01_BL.getResourceValue(
                            "BINOLPTRPS33", language, "RPS33_saleEmployeeName");
                } else if ("campaignName".equals(con)) {
                    // 活动名称
                    paramName = binOLMOCOM01_BL.getResourceValue(
                            "BINOLPTRPS33", language, "RPS33_campaignCode");
                } else if ("saleType".equals(con)) {
                    // 交易类型
                    paramName = binOLMOCOM01_BL.getResourceValue(
                            "BINOLPTRPS33", language, "RPS33_saleType");
                    paramValue = codeTable.getVal("1055", paramValue);
                } else if ("consumerType".equals(con)) {
                    // 消费者类型
                    paramName = binOLMOCOM01_BL.getResourceValue(
                            "BINOLPTRPS33", language, "RPS33_consumerType");
                    paramValue = codeTable.getVal("1105", paramValue);
                } else if ("channelId".equals(con)) {
                    // 渠道
                    paramName = binOLMOCOM01_BL.getResourceValue(null,
                            language, "global.page.channel");
                    paramValue = binOLCM00_Service.getChannelName(conditionMap);
                } else if ("departId".equals(con)) {
                    // 部门
                    paramName = binOLMOCOM01_BL.getResourceValue(null,
                            language, "global.page.depart");
                    paramValue = binOLCM00_Service.getDepartName(conditionMap);
                } else {
                    // 区域
                    String text = con.substring(0, con.indexOf("Id"));
                    paramName = binOLMOCOM01_BL.getResourceValue(null,
                            language, "global.page." + text);
                    Map<String, Object> temp = new HashMap<String, Object>();
                    temp.put(CherryConstants.SESSION_LANGUAGE, language);
                    temp.put("regionId", map.get(con));
                    paramValue = binOLCM00_Service.getRegionName(temp);
                }
                lineNum++;
                if (lineNum % 6 == 0) {
                    condition.append("\n");
                }
                condition.append(paramName + "：" + paramValue + "\0\0\0\0\0");
            }
        }

        return condition.toString();
    }
    
    /**
     * 获取导出参数
     */
    public Map<String, Object> getExportParam(Map<String, Object> map) {
        
        Map<String, Object> exportMap = new HashMap<String, Object>();
        exportMap.put(CherryConstants.ORGANIZATIONINFOID, map.get(CherryConstants.ORGANIZATIONINFOID));
        exportMap.put(CherryConstants.BRANDINFOID, map.get(CherryConstants.BRANDINFOID));
        String language = (String)map.get(CherryConstants.SESSION_CHERRY_LANGUAGE);
        exportMap.put("charset", map.get("charset"));
        
        
        // 把会员搜索条件转换为文字说明
        String conditionContent = this.getConditionStr(map);
        if(conditionContent != null && !"".equals(conditionContent)) {
            exportMap.put("header", conditionContent);
        }
        
        exportMap.put("conditionMap", map);
        
        exportMap.put("sheetName", binOLCM37_BL.getResourceValue("BINOLPTRPS33", language, "RPS33_exportName"));
        String[][] titleRows = {
                { "billCode", binOLCM37_BL.getResourceValue("BINOLPTRPS33", language, "RPS33_billCode"), "40", "", "" },// 单据号
                { "departCode", binOLCM37_BL.getResourceValue("BINOLPTRPS33", language, "RPS33_departCode"), "15", "", "" },// 柜台号
                { "departName", binOLCM37_BL.getResourceValue("BINOLPTRPS33", language, "RPS33_counterName"), "", "", "" },// 柜台
                { "employeeName", binOLCM37_BL.getResourceValue("BINOLPTRPS33", language, "RPS33_employeeName"), "", "", "" },// BA姓名
                { "employeeCode", binOLCM37_BL.getResourceValue("BINOLPTRPS33", language, "RPS33_baCode"), "", "", "" },// BACode
                { "memberCode", binOLCM37_BL.getResourceValue("BINOLPTRPS33", language, "RPS33_memCode"), "15", "", "" },// 会员卡号
                { "memberName", binOLCM37_BL.getResourceValue("BINOLPTRPS33", language, "RPS33_memName"), "15", "", "" },// 会员姓名
                { "unitCode", binOLCM37_BL.getResourceValue("BINOLPTRPS33", language, "RPS33_unitCode"), "20", "", "" },// 厂商编码
                { "barCode", binOLCM37_BL.getResourceValue("BINOLPTRPS33", language, "RPS33_barCode"), "20", "", "" },// 产品条码
                { "productName", binOLCM37_BL.getResourceValue("BINOLPTRPS33", language, "RPS33_productName"), "20", "", "" },// 产品名称
                { "buyQuantity", binOLCM37_BL.getResourceValue("BINOLPTRPS33", language, "RPS33_buyQuantity"), "", "", "" },// 购买数量
                { "buyAmount", binOLCM37_BL.getResourceValue("BINOLPTRPS33", language, "RPS33_buyAmount"), "", "", "" },// 购买金额
                { "saleType", binOLCM37_BL.getResourceValue("BINOLPTRPS33", language, "RPS33_saleType"), "", "", "1055" },// 销售类型
                { "productType", binOLCM37_BL.getResourceValue("BINOLPTRPS33", language, "RPS33_productType"), "", "", "1136" },// 产品类型
                { "inActivity", binOLCM37_BL.getResourceValue("BINOLPTRPS33", language, "RPS33_inActivity"), "30", "", "" },// 参与活动
                { "billCreateTime", binOLCM37_BL.getResourceValue("BINOLPTRPS33", language, "RPS33_billCreateTime"), "20", "", "" } // 时间
        };
        String organizationInfoID = ConvertUtil.getString(map.get("organizationInfoId"));
        String brandInfoID = ConvertUtil.getString(map.get("brandInfoId"));
        String configValue = binOLCM14_BL.getConfigValue("1140", organizationInfoID, brandInfoID);
        if(configValue.equals(CherryConstants.SYSTEM_CONFIG_ENABLE)){
            //导出数据列数组（有唯一码）
            Vector<String[]> vector = new Vector<String[]>();
            for (int i = 0; i < titleRows.length; i++) {
                vector.add(titleRows[i]);
                if(i == titleRows.length-1){
                    String[] uniCodeArr = { "uniqueCode", binOLCM37_BL.getResourceValue("BINOLPTRPS33", language, "RPS33_uniqueCode"), "25", "", "" }; // 唯一码
                    vector.add(uniCodeArr);
                }
            }
            String[][] newArrays = new String[vector.size()][vector.get(0).length];
            vector.toArray(newArrays);
            exportMap.put("titleRows",newArrays);
        }else{
            exportMap.put("titleRows", titleRows);
        }
        
        return exportMap;
    }

    /**
     * 取得需要转成Excel文件的数据List
     * @param map 查询条件
     * @return 需要转成Excel文件的数据List
     */
    @Override
    public List<Map<String, Object>> getDataList(Map<String, Object> map)
            throws Exception {
        return binOLPTRPS33_Service.getExportDetailList(map);
    }
}
/*
 * @(#)BINBESSPRM04_BL.java     1.0 2012/04/20
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
package com.cherry.ss.pro.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryBatchException;
import com.cherry.cm.core.ICherryInterface;

/**
 * 
 * 平产品库存IF
 * 
 * 
 * @author niushunjie
 * @version 1.0 2012.04.20
 */
public interface BINBESSPRO04_IF extends ICherryInterface{
    /**
     * 取得仓库
     * @param map
     * @return
     */
    public List<Map<String,Object>> getDepotInfoList(Map<String,Object> map);
       
    /**
     * 取得不一致库存
     * @param map
     * @return
     */
    public List<Map<String,Object>> getDiffStockList(Map<String,Object> map);
    
    /**
     * 平库存
     * @param map
     * @return
     * @throws CherryBatchException
     */
    public int tran_insert(Map<String,Object> map) throws CherryBatchException;
    
    /**
     * 导出为Excel
     * @param map
     * @return
     */
    public byte[] exportExcel(Map<String, Object> map) throws Exception;
    
    /**
     * Excel导出参数类
     */
    class ExcelParam{
        /** 参数 需传入organizationInfoId、brandInfoId*/
        private Map<String, Object> map;
        
        /** 生成Excel数组 格式{数据库字段,资源文件key,列宽,对齐/数字格式(right/number),CodeTable的值}... */
        private String[][] array;
        
        /** 资源的基本名称 */
        private String baseName;
        
        /** 数据List */
        private List<Map<String, Object>> dataList;
        
        /** 合计数组 格式{列数,合计内容,显示在第几列} */
        private String[][] totalArr;
        
        /** sheet标签 */
        private String sheetLabel;
        
        /** 显示查询条件 Excel第一行显示的内容，为空时不显示这一行。可使用\n换行 */
        private String searchCondition;
        
        public Map<String, Object> getMap() {
            return map;
        }
        public void setMap(Map<String, Object> map) {
            this.map = map;
        }
        public String[][] getArray() {
            return array;
        }
        public void setArray(String[][] array) {
            this.array = array;
        }
        public String getBaseName() {
            return baseName;
        }
        public void setBaseName(String baseName) {
            this.baseName = baseName;
        }
        public List<Map<String, Object>> getDataList() {
            return dataList;
        }
        public void setDataList(List<Map<String, Object>> dataList) {
            this.dataList = dataList;
        }
        public String[][] getTotalArr() {
            return totalArr;
        }
        public void setTotalArr(String[][] totalArr) {
            this.totalArr = totalArr;
        }
        public void setSheetLabel(String sheetLabel) {
            this.sheetLabel = sheetLabel;
        }
        public String getSheetLabel() {
            return sheetLabel;
        }
        public String getSearchCondition() {
            return searchCondition;
        }
        public void setSearchCondition(String searchCondition) {
            this.searchCondition = searchCondition;
        }
    }
    
    /**
     * 取得资源的值
     * @param baseName 资源的文件名称（无语言无后缀）。取共通资源传值null或""
     * @param language 语言
     * @param key 资源的键
     */
    public String getResourceValue(String baseName,String language,String key);
    
    /**
     * 共通Excel导出
     * @param ep Excel参数
     * @throws Exception 
     */
    public byte[] getExportExcel(ExcelParam ep) throws Exception;
    
}

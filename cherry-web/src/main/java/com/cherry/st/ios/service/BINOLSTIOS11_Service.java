/*
 * @(#)BINOLSTIOS11_Service.java     1.0 2015/02/04
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
package com.cherry.st.ios.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 
 * 退库申请（Excel导入）Service
 * 
 * @author niushunjie
 * @version 1.0 2015.02.04
 */
public class BINOLSTIOS11_Service extends BaseService {

    /**
     * 获取退库申请单（Excel导入）批次总数
     * @param map
     * @return
     */
    public int getImportBatchCount(Map<String, Object> map) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryConstants.IBATIS_SQL_ID,"BINOLSTIOS11.getImportBatchCount");
        return baseServiceImpl.getSum(paramMap);
    }
    
    /**
     * 获取退库申请单（Excel导入）批次信息
     * @param map
     * @return
     */
    public List<Map<String, Object>> getImportBatchList(Map<String, Object> map) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryConstants.IBATIS_SQL_ID,"BINOLSTIOS11.getImportBatchList");
        return baseServiceImpl.getList(paramMap);
    }
    
    /**
     * 判断数据是否重复
     * @param map
     * @return
     */
    public List<Map<String, Object>> getRepeatData(Map<String, Object> map) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTIOS11.getRepeatData");
        return baseServiceImpl.getList(paramMap);
    }
    
    /**
     * 插入导入批次表
     * @param map
     * @return
     */
    public int insertImportBatch(Map<String, Object> map) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTIOS11.insertImportBatch");
        return baseServiceImpl.saveBackId(paramMap);
    }
    
    /**
     * 通过产品ID取得产品的价格信息
     * @param BIN_ProductID 产品ID
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getPrtPrice(Map<String, Object> map) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTIOS11.getPrtPrice");
        return baseServiceImpl.getList(paramMap);
    }
    
    /**
     * 通过部门编码取得部门信息
     * @param DepartCode 部门编码
     * @return
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getOrgByCode(Map<String, Object> map) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTIOS11.getOrgByCode");
        return (Map<String, Object>)baseServiceImpl.get(paramMap);
    }
    
    /**
     * 通过逻辑仓库名称获取逻辑仓库信息
     * @param Type 类型
     * @param InventoryName 仓库中文名称或英文名称
     * @return
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getLogicInventoryByName(Map<String, Object> map) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTIOS11.getLogicInventoryByName");
        return (Map<String, Object>)baseServiceImpl.get(paramMap);
    }
    
    /**
     * 通过产品厂商编码取得产品信息
     * @param UnitCode 产品厂商编码
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getPrtInfo(Map<String, Object> map) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTIOS11.getPrtInfoByUnitCode");
        return baseServiceImpl.getList(paramMap);
    }
    
    /**
     * 插入退库申请Excel导入主表
     * @param map
     * @return
     */
    public int insertProReturnRequestExcel(Map<String, Object> map) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTIOS11.insertProReturnRequestExcel");
        return baseServiceImpl.saveBackId(paramMap);
    }
    
    /**
     * 插入退库申请Excel导入明细表
     * @param map
     */
    public void insertProReturnReqExcelDetail(Map<String, Object> map) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTIOS11.insertProReturnReqExcelDetail");
        baseServiceImpl.save(paramMap);
    }
    
    /**
     * 根据仓库ID判断指定的接收退库部门是否是对应的部门,带权限
     * @param param
     * @return
     */
    public int getDepartInfoCount(Map<String,Object> param){
        param.put("userId", param.get("BIN_UserID"));
        param.put("counterKind", param.get("TestType"));
        param.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTIOS11.getDepartInfoCount");
        return baseServiceImpl.getSum(param);
    }
    
    /**
     * 根据仓库ID判断与其关联的所有部门的上级部门是否存在指定的接受订货部门(带有权限或不带权限)总数,带有测试部门和正式部门控制
     * @param param
     * @return
     */
    public int getSupDepartCount(Map<String,Object> param){
        param.put("userId", param.get("BIN_UserID"));
        param.put("counterKind", param.get("TestType"));
        param.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTIOS11.getSupDepartCount");
        return baseServiceImpl.getSum(param);
    }
    
    /**
     * 获取退库申请单（Excel导入）总数
     * @param map
     * @return
     */
    public int getBillExcelCount(Map<String, Object> map) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryConstants.IBATIS_SQL_ID,"BINOLSTIOS11.getBillExcelCount");
        return baseServiceImpl.getSum(paramMap);
    }
    
    /**
     * 获取退库申请单（Excel导入）信息
     * @param map
     * @return
     */
    public List<Map<String, Object>> getBillExcelList(Map<String, Object> map) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryConstants.IBATIS_SQL_ID,"BINOLSTIOS11.getBillExcelList");
        return baseServiceImpl.getList(paramMap);
    }
    
    /**
     * 通过ID获取单条退库申请单（Excel导入）明细信息
     * @param map
     * @return 
     */
    public List<Map<String, Object>> getBillExcelDetailList(Map<String, Object> map) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryConstants.IBATIS_SQL_ID,"BINOLSTIOS11.getBillExcelDetailList");
        return baseServiceImpl.getList(paramMap);
    }
    
    /**
     * 获取退库申请单（Excel导入）主数据
     * @param map
     * @return
     */
    public Map<String, Object> getBillExcelInfo(Map<String, Object> map) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryConstants.IBATIS_SQL_ID,"BINOLSTIOS11.getBillExcelInfo");
        return (Map<String, Object>)baseServiceImpl.get(paramMap);
    }
    
    /**
     * 查询导出数据总数
     * @param map
     * @return
     */
    public int getExportDataCount(Map<String, Object> map) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryConstants.IBATIS_SQL_ID,"BINOLSTIOS11.getExportDataCount");
        return baseServiceImpl.getSum(paramMap);
    }
    
    /**
     * 查询导出数据
     * @param map
     * @return
     */
    public List<Map<String, Object>> getExportDataList(Map<String, Object> map) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryConstants.IBATIS_SQL_ID,"BINOLSTIOS11.getExportDataList");
        return baseServiceImpl.getList(paramMap);
    }
}
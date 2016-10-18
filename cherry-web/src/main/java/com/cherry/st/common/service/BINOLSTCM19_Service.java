package com.cherry.st.common.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

public class BINOLSTCM19_Service extends BaseService{
	/**
     * 插入销售单主表，返回主表ID
     * @param map
     * @return
     */
    public int insertSaleBill(Map<String, Object> map){
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM19.insertSaleBill");
        return baseServiceImpl.saveBackId(paramMap);
    }
    
    /**
     * 插入销售单明细表
     * @param map
     * @return
     */
    public void insertSaleDetail(Map<String, Object> map){
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM19.insertSaleDetail");
        baseServiceImpl.save(paramMap);
    }
    
    /**
     * 修改销售单主表数据
     * @param map
     * @return
     */
    public int updateSaleBill(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM19.updateSaleBill");
        return baseServiceImpl.update(parameterMap);
    }
    
    /**
     * 删除指定销售单（逻辑删除）
     * @param map
     * @return
     */
    public int deleteSaleOrdersLogic(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM19.deleteSaleOrdersLogic");
        return baseServiceImpl.update(parameterMap);
    }
    
    /**
     * 删除指定销售单明细（逻辑删除）
     * @param map
     * @return
     */
    public int deleteSaleDetailLogic(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM19.deleteSaleDetailLogic");
        return baseServiceImpl.update(parameterMap);
    }
    
    /**
     * 删除指定销售单明细
     * @param map
     * @return
     */
    public int deleteSaleDetail(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM19.deleteSaleDetail");
        return baseServiceImpl.remove(parameterMap);
    }
    
    /**
     * 给定后台销售单主ID，取得概要信息。
     * @param map
     * @return
     */
    @SuppressWarnings("unchecked")
	public Map<String,Object> getBackstageSaleMainData(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM19.getBackstageSaleMainData");
        return (Map<String, Object>) baseServiceImpl.get(parameterMap);
    }
    
    /**
     * 给定后台销售单主ID，取得明细信息。
     * @param map
     * @return
     */
    @SuppressWarnings("unchecked")
	public List<Map<String,Object>> getBackstageSaleDetailData(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM19.getBackstageSaleDetailData");
        return baseServiceImpl.getList(parameterMap);
    }
    
    /**
     * 修改后台销售单主表数据。
     * @param map
     * @return
     */
    public int updateBackstageSale(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM19.updateBackstageSale");
        return baseServiceImpl.update(parameterMap);
    }
    
    /**
     * 插入后台销售业务数据主表（履历），返回主表ID
     * @param map
     * @return
     */
    public int insertBackstageSaleHistory(Map<String, Object> map){
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM19.insertBackstageSaleHistory");
        return baseServiceImpl.saveBackId(paramMap);
    }
    
    /**
     * 插入后台销售业务数据明细表（履历）
     * @param map
     * @return
     */
    public void insertBackstageSaleDetailHistory(Map<String, Object> map){
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.putAll(map);
        paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM19.insertBackstageSaleDetailHistory");
        baseServiceImpl.save(paramMap);
    }
    
    /**
     * 给定后台销售业务数据主表（履历）主ID，取得概要信息。
     * @param map
     * @return
     */
    @SuppressWarnings("unchecked")
	public Map<String,Object> getBackstageSaleHistoryMainData(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM19.getBackstageSaleHistoryMainData");
        return (Map<String, Object>) baseServiceImpl.get(parameterMap);
    }
    
    /**
     * 给定后台销售业务数据主表（履历）主ID，取得明细信息。
     * @param map
     * @return
     */
    @SuppressWarnings("unchecked")
	public List<Map<String,Object>> getBackstageSaleDetailHistoryData(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM19.getBackstageSaleDetailHistoryData");
        return baseServiceImpl.getList(parameterMap);
    }
    
    /**
     * 查询最新的履历编号
     * @param map
     * @return
     */
    @SuppressWarnings("unchecked")
	public Map<String,Object> getLatestHistoryNo(Map<String,Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM19.getLatestHistoryNo");
        return (Map<String, Object>) baseServiceImpl.get(parameterMap);
    }
    
    /**
     * 根据后台销售单据ID取得该后台销售单据的所有履历
     * @param map
     * @return
     */
    @SuppressWarnings("unchecked")
	public List<Map<String,Object>> getAllBackstageSaleHistory(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM19.getAllBackstageSaleHistory");
        return baseServiceImpl.getList(parameterMap);
    }

    /**
     * 修改后台销售单主表（履历）数据。
     * @param map
     * @return
     */
    public int updateBackstageSaleHistory(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM19.updateBackstageSaleHistory");
        return baseServiceImpl.update(parameterMap);
    }
    
    /**
     * 修改组织结构表联系人和联系地址
     * @param map
     * @return
     */
    public int updateOrganizationInfo(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM19.updateOrganizationInfo");
        return baseServiceImpl.update(parameterMap);
    }
    
    /**
     * 修改客户部门联系地址
     * @param map
     * @return
     */
    public int updateDepartAddress(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM19.updateDepartAddress");
        return baseServiceImpl.update(parameterMap);
    }
}

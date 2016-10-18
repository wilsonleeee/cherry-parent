package com.cherry.mo.cio.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

public class BINOLMOCIO16_Service extends BaseService {
	/**
	 * 获取柜台消息（Excel导入）批次总数
	 * @param map
	 * @return
	 */
	public int getImportBatchCount(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID,"BINOLMOCIO16.getImportBatchCount");
		return baseServiceImpl.getSum(paramMap);
	}
	
	/**
	 * 获取柜台消息（Excel导入）批次信息
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getImportBatchList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID,"BINOLMOCIO16.getImportBatchList");
		return baseServiceImpl.getList(paramMap);
	}
	
    /**
     * 新增柜台消息
     * 
     * @param map
     * @return 
     */
    public int addCounterMessage(Map<String, Object> map) {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOCIO16.addCounterMessage");
        return baseServiceImpl.saveBackId(parameterMap);
    }
    
    /**
     * 插入导入批次表
     * @param map
     * @return
     */
    public int insertImportBatch(Map<String, Object> map) {
    	Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOCIO16.insertImportBatch");
        return baseServiceImpl.saveBackId(parameterMap);
    }
    
    /**
     * 插入柜台消息导入主表
     * @param map
     * @return
     */
    public int insertCounterMessageImport(Map<String, Object> map) {
    	Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOCIO16.insertCounterMessageImport");
        return baseServiceImpl.saveBackId(parameterMap);
    }
    
    /**
     * 只导入柜台消息时插入明细表
     * @param map
     */
    public void insertCounterMessageImportDetail(Map<String, Object> map) {
    	Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOCIO16.insertCounterMessageImportDetail");
        baseServiceImpl.save(parameterMap);
    }
    
    /**
     * 导入并下发时插入导入明细表
     * @param list
     */
    public void insertCounterMessageImportDetail(List<Map<String, Object>> list) {
        baseServiceImpl.saveAll(list, "BINOLMOCIO16.insertCounterMessageImportDetail");
    }
    
}

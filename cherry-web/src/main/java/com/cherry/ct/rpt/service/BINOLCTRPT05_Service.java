package com.cherry.ct.rpt.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;

public class BINOLCTRPT05_Service extends BaseService{
	/**
     * 沟通绩效分析List
     * @param map
     * @return 沟通绩效分析List
     * 		
     */
	@SuppressWarnings("unchecked")
    public List<Map<String, Object>> getAnalysisList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCTRPT05.getAnalysisList");
        return baseServiceImpl.getList(paramMap);
    }
	
	/**
     * 沟通绩效分析count
     * @param map
     * @return 沟通绩效分析count
     * 		
     */
	public int getAnalysisCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCTRPT05.getAnalysisCount");
		return CherryUtil.obj2int(baseServiceImpl.get(map));
	}
	
	/**
	 * 查询沟通信息发送时间
	 * @param map
	 * @return
	 */
	public String getSendTime(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCTRPT05.getSendTime");
		return ConvertUtil.getString(baseServiceImpl.get(map));
	}
	
	/**
	 * 查询沟通统计信息
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getAnalysisTotal(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCTRPT05.getAnalysisTotal");
		return (Map<String, Object>) baseServiceImpl.get(map);
	}
	
	/**
     * 查询回柜会员明细List
     * @param map
     * @return 回柜会员明细List
     * 		
     */
	@SuppressWarnings("unchecked")
    public List<Map<String, Object>> getJoinDetailList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCTRPT05.getJoinDetailList");
        return baseServiceImpl.getList(paramMap);
    }
	
	/**
     * 查询回柜会员明细count
     * @param map
     * @return 回柜会员明细count
     * 		
     */
	public int getJoinDetailCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCTRPT05.getJoinDetailCount");
		return CherryUtil.obj2int(baseServiceImpl.get(map));
	}
	/**
     * 查询回柜会员销售明细List
     * @param map
     * @return 回柜会员销售明细List
     * 		
     */
	@SuppressWarnings("unchecked")
    public List<Map<String, Object>> getSaleDetailList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCTRPT05.getSaleDetailList");
        return baseServiceImpl.getList(paramMap);
    }
	
	/**
     * 查询回柜会员销售明细count
     * @param map
     * @return 回柜会员销售明细count
     * 		
     */
	public int getSaleDetailCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCTRPT05.getSaleDetailCount");
		return CherryUtil.obj2int(baseServiceImpl.get(map));
	}
}
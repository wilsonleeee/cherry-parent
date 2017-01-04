package com.cherry.mb.vis.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.service.BaseService;

/**
 * 生成会员回访任务batch处理Service
 * 
 * @author WangCT
 * @version 1.0 2014/12/18
 */
public class BINBEMBVIS04_Service extends BaseService {
	
	/**
	 * 取得会员回访计划List
	 * 
	 * @param map 查询条件
	 * @return 会员回访计划List
	 */
	public List<Map<String, Object>> getVisitPlanList(Map<String,Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEMBVIS04.getVisitPlanList");
		return baseServiceImpl.getList(paramMap);
	}

	/**
	 * 取得会员回访对象List
	 * 
	 * @param map 查询条件
	 * @return 会员回访对象List
	 */
	public List<Map<String, Object>> getVisitObjList(Map<String,Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEMBVIS04.getVisitObjList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 添加会员回访任务
	 * 
	 * @param map 添加内容
	 */
	public void insertVisitTask(List<Map<String, Object>> list) {
		baseServiceImpl.saveAll(list, "BINBEMBVIS04.insertVisitTask");
	}

	/**
	 * 通过单据号取得单据信息
	 *
	 * @param billCode 单据号
	 * @return map
	 * 单据信息
	 */
	public Map<String, Object> getBillInfoByCode(String billCode) {
		if (CherryChecker.isNullOrEmpty(billCode)) {
			return null;
		}
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("billCode", billCode);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEMBVIS04.getBillInfoByCode");
		return (Map<String, Object>) baseServiceImpl.get(paramMap);
	}

}

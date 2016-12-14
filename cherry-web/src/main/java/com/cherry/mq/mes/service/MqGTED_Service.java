package com.cherry.mq.mes.service;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @ClassName: MqGTED_Service
 * @Description: TODO(经销商额度变更Service)
 * @author 陈宽
 * @version v1.0.0 2016-11-30
 *
 */
public class MqGTED_Service extends BaseService  {

	/**
	 * 会员持卡信息表
	 * @param map
	 * @return
	 */
	public Map<String, Object> getMemCardInfo (Map<String, Object> map){
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.putAll(map);
        paramMap.put(CherryConstants.IBATIS_SQL_ID, "MqGTED.getMemCardInfo");
        return (Map<String, Object>) baseServiceImpl.get(paramMap);
	}

	/**
	 *  根据DepartCode得到对应的柜台信息
	 * @param map
	 * @return
	 */
	public Map<String, Object> getCounterByCode (Map<String, Object> map){
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "MqGTED.getCounterByCode");
		return (Map<String, Object>) baseServiceImpl.get(paramMap);
	}


	
	/**
	 * 插入柜台积分额度明细表
	 * @param map
	 */
	public void insertCounterLimitInfo(Map<String, Object> map){
		 baseServiceImpl.save(map,"MqGTED.insertCounterLimitInfo");
	}


	/**
	 * 修改柜台积分计划设置表
	 * @param map
	 */
	public void updateCounterPointPlan(Map<String, Object> map){
		baseServiceImpl.update(map,"MqGTED.updateCounterPointPlan");
	}

		
}

package com.cherry.pt.rps.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 
 * @ClassName: BINOLPTRPS36_Service 
 * @Description: TODO(柜台月度人效明细Service) 
 * @author menghao
 * @version v1.0.0 2015-1-13 
 *
 */
public class BINOLPTRPS36_Service extends BaseService {

	/**
	 * 取得柜台月度人效明细数
	 * @param map
	 * @return
	 */
	public int getMonthPeopleEffectCount(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS36.getMonthPeopleEffectCount");
		return baseServiceImpl.getSum(paramMap);
	}

	/**
	 * 取得柜台月度人效明细LIST
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getMonthPeopleEffectList(
			Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS36.getMonthPeopleEffectList");
		return baseServiceImpl.getList(paramMap);
	}
}

package com.cherry.pt.rps.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 
 * @ClassName: BINOLPTRPS35_Service 
 * @Description: TODO(柜台月度坪效明细Service) 
 * @author menghao
 * @version v1.0.0 2015-1-12 
 *
 */
public class BINOLPTRPS35_Service extends BaseService {

	/**
	 * 取得柜台月度坪效明细数
	 * @param map
	 * @return
	 */
	public int getMonthPingEffectCount(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS35.getMonthPingEffectCount");
		return baseServiceImpl.getSum(paramMap);
	}

	/**
	 * 取得柜台月度坪效明细LIST
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getMonthPingEffectList(
			Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS35.getMonthPingEffectList");
		return baseServiceImpl.getList(paramMap);
	}
}

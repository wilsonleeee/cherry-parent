package com.cherry.mb.rpt.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 
 * @ClassName: BINOLMBRPT05_Service 
 * @Description: TODO(会员销售统计Service) 
 * @author menghao
 * @version v1.0.0 2015-1-6 
 *
 */
public class BINOLMBRPT05_Service extends BaseService {
	
	/**
	 * 按区间统计会员销售情况
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getRegionStatisticsList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBRPT05.getRegionStatisticsList");
		return baseServiceImpl.getList(paramMap);
	}
	
}

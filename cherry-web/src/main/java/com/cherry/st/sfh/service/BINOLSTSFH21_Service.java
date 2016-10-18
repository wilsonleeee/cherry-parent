package com.cherry.st.sfh.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

public class BINOLSTSFH21_Service extends BaseService {
	/**
	 * 获取销售单（Excel导入）总数
	 * @param map
	 * @return
	 */
	public int getBackSaleReportCount(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID,"BINOLSTSFH21.getBackSaleReportCount");
		return baseServiceImpl.getSum(paramMap);
	}
	
	/**
	 * 获取销售单（Excel导入）信息
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getBackSaleReportList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID,"BINOLSTSFH21.getBackSaleReportList");
		return baseServiceImpl.getList(paramMap);
	}
}

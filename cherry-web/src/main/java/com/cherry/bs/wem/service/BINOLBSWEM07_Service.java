package com.cherry.bs.wem.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 
 * @ClassName: BINOLBSWEM07_Service 
 * @Description: TODO(银行汇款报表Service) 
 * @author menghao
 * @version v1.0.0 2015-12-7 
 *
 */
public class BINOLBSWEM07_Service extends BaseService{
	
	/**
	 * 取得银行转账记录总数
	 * @param map
	 * @return
	 */
	public int getBankTransferRecordCount(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSWEM07.getBankTransferRecordCount");
		return baseServiceImpl.getSum(paramMap);
	}
	
	public List<Map<String, Object>> getBankTransferRecordList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSWEM07.getBankTransferRecordList");
		return baseServiceImpl.getList(paramMap);
	}
	
}

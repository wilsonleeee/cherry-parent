package com.cherry.mb.rpt.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 
 * @ClassName: BINOLMBRPT04_Service 
 * @Description: TODO(会员发展统计Service) 
 * @author menghao
 * @version v1.0.0 2015-1-5 
 *
 */
public class BINOLMBRPT04_Service extends BaseService {
	
	/**
	 * 取得汇总信息
	 * 
	 * */
	public Map<String,Object> getSumInfo(Map<String,Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBRPT04.getSumInfo");
		return (Map<String,Object>)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 按终端统计总客户数、总销费金额、当期有销售会员人数、
	 * 	当期有销售的会员销售金额、新会员人数、新会员销售额
	 * @param map
	 * @return
	 */
	public int getMemberDevelopCount(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBRPT04.getMemberDevelopCount");
		return baseServiceImpl.getSum(paramMap);
	}
	
	/**
	 * 按终端统计回购会员人数，回购金额
	 * 回购：当期之前有过付款，当期之内再次付款
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getMemberDevelopList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBRPT04.getMemberDevelopList");
		return baseServiceImpl.getList(paramMap);
	}
	
	
}

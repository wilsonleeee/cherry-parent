package com.cherry.webserviceout.kingdee.counterInfo.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
*
* Kingdee接口：柜台下发Service
*
* @author ZhaoCF
*
* @version  2015-4-29
*/
public class BINBEKDCOU01_Service extends BaseService {
	
	/**
    * 查询柜台信息
    * 
    * @param map
    * @return 柜台信息
    */
	public List<Map<String, Object>> getCounterInfo(Map<String, Object> map) {
		Map<String,Object> paraMap = new HashMap<String,Object>();
		paraMap.putAll(map);
		paraMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEKDCOU01.getCounterInfo");
		return baseServiceImpl.getList(paraMap);
	}
	
	/**
	 * 更新同步时间
	 * @param synchTine
	 * @return
	 */
	public int updateSynchTime(Map<String,Object> map){
        Map<String,Object> paraMap = new HashMap<String,Object>();
        paraMap.putAll(map);
        paraMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEKDCOU01.updateSynchTime");
        return baseServiceImpl.update(paraMap);
    }
	
}

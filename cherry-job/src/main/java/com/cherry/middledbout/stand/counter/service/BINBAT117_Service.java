package com.cherry.middledbout.stand.counter.service;

import java.util.List;
import java.util.Map;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
*
* 标准接口：柜台信息导出Service
*
* @author ZhaoCF
* 
* @version  2015-7-28
*/
public class BINBAT117_Service extends BaseService{
	
	/**
	 * 从数据库中查询柜台信息数据
	 * @param map 查询条件
	 * @return 查询的柜台List
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getCounterInfo(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBAT117.getCounterInfo");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 从接口表中查询柜台信息数据
	 * @param map 查询条件
	 * @return 查询的柜台map
	 * 
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object>getCounterCode(Map<String, Object> paraMap) {
		paraMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBAT117.getCounterCode");
		return (Map<String, Object>) tpifServiceImpl.get(paraMap);
	}
	
	/**
	 * 更新已有的柜台信息
	 * @param synchTine
	 * @return
	 */
	public int updateCounterInfo(Map<String,Object> resultMap){
		resultMap.put(CherryConstants.IBATIS_SQL_ID, "BINBAT117.updateCounterInfo");
        return tpifServiceImpl.update(resultMap);
    }
	
	/**
	 * 
	 * 插入柜台信息数据
	 * @param 
	 */
	public void insertCounterInfo(Map<String,Object> resultMap){
		resultMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBAT117.insertCounterInfo");
		tpifServiceImpl.save(resultMap);
	}
	
	/**
	 * 更新同步时间
	 * @param synchTine
	 * @return
	 */
	public int updateSynchTime(Map<String,Object> paraMap){
		paraMap.put(CherryConstants.IBATIS_SQL_ID, "BINBAT117.updateSynchTime");
        return baseServiceImpl.update(paraMap);
    }
}

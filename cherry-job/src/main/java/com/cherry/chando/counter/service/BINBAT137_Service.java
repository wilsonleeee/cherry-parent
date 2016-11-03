package com.cherry.chando.counter.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
*
* 自然堂：调整BAS生效时间
*
* @author lzs
* 
* @version  2016-02-16
*/
public class BINBAT137_Service extends BaseService{
	
	/**
	 * 查询小于当前系统时间的柜台调整信息List
	 * @param map 查询条件
	 * @return 查询的柜台调整List
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getCounterAdjustInfo(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBAT137.getCounterAdjustInfo");
		return baseServiceImpl.getList(map);
	}
	/**
	 * 当生效时间已到期，审核状态为未审核时更新数据有效性为无效
	 * @param map
	 * @return
	 */
	public int updateCounterAdjustByFlag(Map<String,Object> map){
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBAT137.updateCounterAdjustByFlag");
		return baseServiceImpl.update(map);
	}
	/**
	 * 更新柜台调整数据调整状态
	 * @param map
	 * @return
	 */
	public int updateCounterAdjustByStatus(Map<String,Object> map){
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBAT137.updateCounterAdjustByStatus");
		return baseServiceImpl.update(map);
	}
	/**
	 * 更新柜台信息表经销商Id
	 * @param map
	 * @return
	 */
	public int updateCounterByResellerId(Map<String,Object> map){
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBAT137.updateCounterByResellerId");
		return baseServiceImpl.update(map);
	}
	/**
	 * 新增员工管辖部门
	 * @param map
	 */
	public void insertEmployeeDepart(Map<String,Object> map){
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBAT137.insertEmployeeDepart");
		baseServiceImpl.save(map);
	}
	/**
	 * 判断员工管辖部门是否存在
	 * @param map
	 * @return
	 */
	public int getCountByEmpolyeeDepart(Map<String,Object> map){
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBAT137.getCountByEmpolyeeDepart");
		return baseServiceImpl.getSum(map);
	}
	/**
	 * 取得柜台信息(新老后台交互时使用)
	 * 
	 * @param map 查询条件
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getCounterInfo(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBAT137.getCounterInfo");
		return (Map)baseServiceImpl.get(parameterMap);
	}
	/**
	 * 添加和更新老后台配置数据中柜台信息（不包括删除）
	 * 
	 * */
	public void synchroCounterN(Map<String,Object> param) {
		param.put(CherryConstants.IBATIS_SQL_ID, "BINBAT137.synchroCounterN");
		ifServiceImpl.updateProcs(param);
	}
	/**
	 * 取得比较多的员工信息
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> getEmployeeMoreInfo(Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBAT137.getEmployeeMoreInfo");
		return (Map<String, Object>)baseServiceImpl.get(map);
	}
	/**
	 * 取得员工信息
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> getEmployeeInfo(Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBAT137.getEmployeeInfo");
		return (Map<String, Object>)baseServiceImpl.get(map);
	}
	
	/**
	 * 根据营业员对应的员工ID取得关注和归属的柜台信息
	 * 
	 * 
	 * */
	public List<Map<String,Object>> getCounterInfoByEmplyeeId(Map<String,Object> map){
		return baseServiceImpl.getList(map, "BINBAT137.getCounterInfoByEmplyeeId");
	}
	/**
	 * 取得前进的系统时间
	 * 
	 * @param
	 * @return String
	 */
	public String getForwardSYSDate() throws Exception {
		return baseServiceImpl.getForwardSYSDate();
	}
	/**
	 * 获取柜台信息的部门ID
	 * @param map
	 * @return
	 */
	public Map<String,Object> getOrganizationId(Map<String,Object> map){
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBAT137.getOrganizationId");
		return (Map<String,Object>)baseServiceImpl.get(map);
	}
	/**
	 * 获取营业员信息
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> getBaInfo(Map<String,Object> map){
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBAT137.getBaInfo");
		return baseServiceImpl.getList(map);
	}
	/**
	 * 当柜台更改柜长时,清除原有柜台的柜长 
	 * @param map
	 * @return
	 */
	public int updateIsCounterManagerByBaId(Map<String,Object> map){
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBAT137.updateIsCounterManagerByBaId");
		return baseServiceImpl.update(map);
	}
	/**
	 * 查询待更改的BA信息
	 * @param map
	 * @return
	 */
	public Map<String,Object> getBaInfoIdByCounterId(Map<String,Object> map){
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBAT137.getBaInfoIdByCounterId");
		return (Map<String,Object>)baseServiceImpl.get(map);
	}
	/**
	 * 柜台BA调整：更改BA所属柜台
	 * @param map
	 * @return
	 */
	public int updateOrganizationByBaInfoId(Map<String,Object> map){
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBAT137.updateOrganizationByBaInfoId");
		return baseServiceImpl.update(map);
	}
}

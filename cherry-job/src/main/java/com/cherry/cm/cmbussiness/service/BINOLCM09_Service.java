package com.cherry.cm.cmbussiness.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.service.BaseService;

@SuppressWarnings("unchecked")
public class BINOLCM09_Service extends BaseService{
	
	/**
	 * 取得无效用户List
	 * @param map
	 * @return
	 */
	public List<Integer> getInvalidUserList (){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINOLCM09.getInvalidUserList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 查询促销活动地点
	 * @param map
	 * @return
	 */
	public List<Map<String, String>> getPlaceList (Map<String, Object> map){
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINOLCM09.getPlaceList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 查询促销活动礼品
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getGiftList (Map<String, Object> map){
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINOLCM09.getGiftList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 查询促销活动表
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getActiveInfoList (Map<String, Object> map){
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINOLCM09.getActiveInfoList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 根据区域市查询柜台
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getCounterByIdCity (Map<String, Object> map){
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINOLCM09.getCounterByIdCity");
		map.put("userId", map.get("userID"));
		map.put("operationType", "1");
		map.put("businessType", "1");
		map.put("DEPARTTYPE", "4");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 根据渠道查询柜台
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getCounterByIdChannel (Map<String, Object> map){
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINOLCM09.getCounterByIdChannel");
		map.put("userId", map.get("userID"));
		map.put("operationType", "1");
		map.put("businessType", "1");
		map.put("DEPARTTYPE", "4");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 根据渠道查询柜台
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getCounterByIdFaction (Map<String, Object> map){
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINOLCM09.getCounterByIdFaction");
		map.put("userId", map.get("userID"));
		map.put("operationType", "1");
		map.put("businessType", "1");
		map.put("DEPARTTYPE", "4");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 根据组织查询柜台
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getCounterByIdOrganization(Map<String, Object> map){
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINOLCM09.getCounterByIdOrganization");
		map.put("userId", map.get("userID"));
//		map.put("operationType", "1");
//		map.put("businessType", "1");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 清空活动关联表数据
	 * @param map
	 */
	public void clearActivityAssociateTable(List<Map<String, Object>> list){
		ifServiceImpl.deleteAll(list, "BINOLCM09.clearActivityAssociateTable");
	}
	
	/**
	 * 无效活动关联表数据
	 * @param map
	 */
	public void stopActivityAssociateTable(List<Map<String, Object>> list){
		ifServiceImpl.deleteAll(list, "BINOLCM09.stopActivityAssociateTable");
	}
	
	/** 停止活动表数据 
	 * @param map
	 */
	public void stopActivityTable(List<Map<String, Object>> list){
		ifServiceImpl.deleteAll(list,"BINOLCM09.stopActivityTable");
	}
	
	/**
	 * 清空活动表数据 
	 * @param map
	 */
	public void clearActivityTable(List<Map<String, Object>> list){
		ifServiceImpl.deleteAll(list,"BINOLCM09.clearActivityTable");
	}
	
	/**
	 * 删除活动信息主表(接口表)数据
	 * 
	 * @param map
	 *            查询条件
	 * 
	 */
	public void deleteActivityAssociateSubject(List<Map<String, Object>> list) {
		ifServiceImpl.deleteAll(list, "BINOLCM09.deleteActivityAssociateSubject");
	}
	
	/**
	 * 插入brand数据库活动表
	 * @param map
	 */
	public void addActivityTable_CHY (List<Map<String, Object>> list){
		// 批量插入
		ifServiceImpl.saveAll(list, "BINOLCM09.addActivityTable_CHY");
	}

	/**
	 * 插入brand数据库活动关联表 
	 * @param map
	 */
	public void addActivityAssociateTable_CHY (List<Map<String, Object>> list){
		ifServiceImpl.saveAll(list, "BINOLCM09.addActivityAssociateTable_CHY");
	}

	/**
	 * 插入活动信息主表(接口表)
	 * @param map
	 */
	public void addActAssSubject_SCS (List<Map<String, Object>> list){
		ifServiceImpl.saveAll(list, "BINOLCM09.addActivityAssociateSubject_SCS");
	}
	
	/**
	 * 插入活动下发历史表
	 * @param map
	 */
	public void addActivityTransHis (List<Map<String, Object>> list){
		baseServiceImpl.saveAll(list, "BINOLCM09.addActivityTransHis");
	}
	
	/**
	 * 取得已下发过活动的柜台
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getActHisList(Map<String, Object> map){
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINOLCM09.getActHisList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得需要停止的促销活动
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getStopActList(Map<String, Object> map){
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINOLCM09.getStopActList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 更新停止的促销活动
	 * @param map
	 * @return
	 */
	public void updatePrmActivity(String brandId){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("brandInfoId", brandId);
		baseServiceImpl.update(map, "BINOLCM09.updatePrmActivity");
	}
	
	/**
	 * 更新停止的促销活动
	 * @param map
	 * @return
	 */
	public void updatePrmActivity2(String brandId,String mainCode){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("brandInfoId", brandId);
		if(!"".equals(mainCode)){
			map.put("mainCode", mainCode);
		}
		baseServiceImpl.update(map, "BINOLCM09.updatePrmActivity2");
	}
	
	/**
	 * 取得用户权限柜台
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getAllCntList(Map<String, Object> map){
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINOLCM09.getAllCntList");
		return baseServiceImpl.getList(map);
	}
	/**
	 * 
	 * 删除活动下发历史
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 * 
	 */	
	public void clearActivityTransHis(List<Map<String, Object>> list) {
		baseServiceImpl.updateAll(list, "BINOLCM09.clearActivityTransHis");
	}
	
	/**
	 * 
	 * 停止活动下发历史
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 * 
	 */	
	public void stopActivityTransHis(List<Map<String, Object>> list) {
		baseServiceImpl.updateAll(list, "BINOLCM09.stopActivityTransHis");
	}
}

package com.cherry.cp.act.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.service.BaseService;

/**
 * 会员活动下发Service
 * @author lipc
 *
 */
@SuppressWarnings("unchecked")
public class BINCPMEACT01_Service extends BaseService{
	
	/**
	 * 取得已下发过活动
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getActHisList(Map<String, Object> map){
		Map<String, Object> param = new HashMap<String, Object>(map);
		param.put(CherryBatchConstants.IBATIS_SQL_ID, "BINCPMEACT01.getActHisList");
		return baseServiceImpl.getList(param);
	}
	/**
	 * 查询需要下发的会员活动
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getDisSendList(Map<String, Object> map){
		Map<String, Object> param = new HashMap<String, Object>(map);
		param.put(CherryBatchConstants.IBATIS_SQL_ID, "BINCPMEACT01.getDisSendList");
		return baseServiceImpl.getList(param);
	}
	
	/**
	 * 查询不需要下发的会员活动
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getActiveInfoList(Map<String, Object> map){
		Map<String, Object> param = new HashMap<String, Object>(map);
		param.put(CherryBatchConstants.IBATIS_SQL_ID, "BINCPMEACT01.getActiveInfoList");
		return baseServiceImpl.getList(param);
	}
	
	/**
	 * 取得活动条件结果
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getActConResultList (Map<String, Object> map){
		Map<String, Object> param = new HashMap<String, Object>(map);
		param.put(CherryBatchConstants.IBATIS_SQL_ID, "BINCPMEACT01.getActConResultList");
		return baseServiceImpl.getList(param);
	}
	
	
	/**
	 * 
	 * 逻辑删除活动下发历史
	 * 
	 * @param list 更新条件
	 * 
	 */	
	public void setActivityHisDisabled(List<Map<String, Object>> list) {
		baseServiceImpl.updateAll(list, "BINCPMEACT01.setActivityHisDisabled");
	}
	
	/**
	 * 
	 * 更新会员活动状态
	 * 
	 * @param list 更新条件
	 * 
	 */	
	public void updSubCampState(List<Map<String, Object>> list) {
		baseServiceImpl.updateAll(list, "BINCPMEACT01.updSubCampState");
	}
	
	/**
	 * 
	 * 更新会员主题活动状态
	 * 
	 * @param list 更新条件
	 * 
	 */	
	public void updCampState(Map<String, Object> map) {
		Map<String, Object> param = new HashMap<String, Object>(map);
		param.put(CherryBatchConstants.IBATIS_SQL_ID, "BINCPMEACT01.updCampState");
		baseServiceImpl.update(param);
	}
	
	/**
	 * 删除活动信息主表(接口表)数据
	 * 
	 * @param list
	 * 
	 */
	public void clearActivitySubject(List<Map<String, Object>> list) {
		ifServiceImpl.deleteAll(list, "BINCPMEACT01.clearActivitySubject");
	}
	
	/**
	 * 清空活动关联表(接口表)数据
	 * @param list
	 */
	public void clearActivityAssociate(List<Map<String, Object>> list){
		ifServiceImpl.updateAll(list, "BINCPMEACT01.clearActivityAssociate");
	}
	
	/**
	 * 删除活动表(接口表)数据 
	 * @param list
	 */
	public void clearActivityCouter(List<Map<String, Object>> list){
		ifServiceImpl.updateAll(list, "BINCPMEACT01.clearActivityCouter");
	}
	
	/**
	 * 
	 * 更新ActivityTable_SCS
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 * 
	 */	
	public void updCntActivity(List<Map<String, Object>> list){
		ifServiceImpl.updateAll(list,"BINCPMEACT01.updCntActivity");
	}
	
	/**
	 * 
	 * 更新ActivityAssociateTable_SCS
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 * 
	 */	
	public void updActivity(List<Map<String, Object>> list){
		ifServiceImpl.updateAll(list,"BINCPMEACT01.updActivity");
	}
	
	/**
	 * 
	 * 更新ActivityAssociateSubject_SCS
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 * 
	 */	
	public void updActivitySubject(List<Map<String, Object>> list){
		ifServiceImpl.updateAll(list,"BINCPMEACT01.updActivitySubject");
	}
}

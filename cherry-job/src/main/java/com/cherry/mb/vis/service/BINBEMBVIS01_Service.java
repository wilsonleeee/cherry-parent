package com.cherry.mb.vis.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

public class BINBEMBVIS01_Service extends BaseService{

	
	/**
	 *取得需更新的会员回访信息（分页）    从POS品牌
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> getWitMemVitInfo(Map<String,Object> map){
		Map<String,Object> param = new HashMap<String,Object>();
		param.putAll(map);
		param.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEMBVIS01.getWitMemVitInfo");
		return witBaseServiceImpl.getList(param);
	}
	
	
	/**
	 *更新会员回访信息      新后台
	 * 
	 * @return
	 */
	public int updateMemVitInfo(Map<String,Object> map){
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEMBVIS01.updateMemVitInfo");
		return baseServiceImpl.update(map);
	}
	
	
//	/**
//	 *新增会员回访信息      新后台
//	 * 
//	 * @return
//	 */
//	public void addMemVitInfo(Map<String,Object> map){
//		map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEMBVIS01.addMemVitInfo");
//		baseServiceImpl.save(map);
//	}
	
	
	/**
	 *更新会员回访信息将同步表示设为0  POS品牌
	 * 
	 * @return
	 */
	public int updateWitMemVitFlag(Map<String,Object> map){
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEMBVIS01.updateWitMemVitFlag");
		return witBaseServiceImpl.update(map);
	}
	
	/**
	 * 查询会员信息
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public HashMap selMemberInfo (Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMBVIS01.selMemberInfo");
		return (HashMap)baseServiceImpl.get(map);
	}
	
	/**
	 * 查询柜台部门信息
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public HashMap selCounterDepartmentInfo (Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMBVIS01.selCounterDepartmentInfo");
		return (HashMap)baseServiceImpl.get(map);
	}
	
	/**
	 * 查询员工信息
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public HashMap selEmployeeInfo (Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMBVIS01.selEmployeeInfo");
		return (HashMap)baseServiceImpl.get(map);
	}
	
	/**
	 * 插入会员回访信息表 
	 * @param map
	 */
	@SuppressWarnings("unchecked")
	public void InsertMemVisitInfo(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMBVIS01.InsertMemVisitInfo");
		baseServiceImpl.save(map);
	}
}

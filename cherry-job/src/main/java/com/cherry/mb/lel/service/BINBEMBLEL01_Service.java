package com.cherry.mb.lel.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

public class BINBEMBLEL01_Service extends BaseService{
	
	/**
	 * 取得新后台会员等级信息
	 * 
	 * @return
	 */
	public List<Map<String,Object>> getlevelList(Map<String,Object> map){
		Map<String,Object> param = new HashMap<String,Object>();
		param.putAll(map);
		param.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEMBLEL01.getlevelList");
		return baseServiceImpl.getList(param);
	}
	
	/**
	 * 取得接口表会员等级信息
	 * 
	 * @return
	 */
	public List<Map<String,Object>> getIFlevelList(Map<String,Object> map){
		Map<String,Object> param = new HashMap<String,Object>();
		param.putAll(map);
		param.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEMBLEL01.getIFlevelList");
		return ifServiceImpl.getList(param);
	}
	
	/**
	 * 取得接口表等级件数
	 * 
	 * @return
	 */
	public int getlevelCount(Map<String,Object> map){
		Map<String,Object> param = new HashMap<String,Object>();
		param.putAll(map);
		param.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEMBLEL01.getlevelCount");
		return ifServiceImpl.getSum(param);
	}
	
	/**
	 * 取得新后台等级件数
	 * 
	 * @return
	 */
	public int getlevelValidCount(Map<String,Object> map){
		Map<String,Object> param = new HashMap<String,Object>();
		param.putAll(map);
		param.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEMBLEL01.getlevelValidCount");
		return baseServiceImpl.getSum(param);
	}
	
	/**
	 *更新接口表会员等级信息 
	 *
	 * @return
	 */
	public int updateIFLevel(Map<String,Object> map){
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEMBLEL01.updateIFLevel");
		return ifServiceImpl.update(map);
	}
	
	/**
	 *插入接口表会员等级信息 
	 *
	 * @return
	 * 		 void
	 */
	public void InsertMemLevelInfo(Map<String,Object> map){
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEMBLEL01.InsertMemLevelInfo");
		ifServiceImpl.save(map);
	}
	
	/**
	 * 更新会员等级下发标志
	 *
	 * @return
	 */
	public int updateLevelSendFlag(Map<String,Object> map){
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEMBLEL01.updateLevelSendFlag");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 取得品牌中文名称
	 *
	 * @return
	 */
	public String getBrandName(Map<String,Object> map){
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEMBLEL01.getBrandName");
		return (String) baseServiceImpl.get(map);
	}
}
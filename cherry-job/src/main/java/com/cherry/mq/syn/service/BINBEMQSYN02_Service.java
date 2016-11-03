package com.cherry.mq.syn.service;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.service.BaseService;

/**
 * MQ同步batch处理Service
 * 
 * @author WangCT
 * @version 1.0 2015.04.10
 */
public class BINBEMQSYN02_Service extends BaseService {
	
	/**
	 * 把Cherry的MQ日志更新成待处理状态
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updateMQLogStatusCherry(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEMQSYN02.updateMQLogStatusCherry");
		return baseServiceImpl.update(map);		
	}
	
	/**
	 * 把POS品牌的MQ日志更新成待处理状态
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updateMQLogStatusPOS(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEMQSYN02.updateMQLogStatusPOS");
		return witBaseServiceImpl.update(map);		
	}
	
	/**
	 * 查询POS品牌数据库MQ日志List
	 * 
	 * @param map 查询条件
	 * @return 日志数据List
	 */
	public List<Map<String, Object>> getWitposMQLogList(Map<String, Object> map){
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEMQSYN02.getWitposMQLogList");
		return witBaseServiceImpl.getList(map);
	}
	
	/**
	 * 查询Cherry的MQ日志List
	 * 
	 * @param map 查询条件
	 * @return 日志数据List
	 */
	public List<Map<String, Object>> getCherryMQLogList(Map<String, Object> map){
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEMQSYN02.getCherryMQLogList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 删除POS品牌数据库中老后台已接收成功的MQ日志
	 * 
	 * @param list 删除条件
	 */
	public void deleteWitposMQLogR(List<Map<String, Object>> list) {
		witBaseServiceImpl.deleteAll(list, "BINBEMQSYN02.deleteWitposMQLogR");
	}
	
	/**
	 * 删除POS品牌数据库中新后台已接收成功的MQ日志
	 * 
	 * @param list 删除条件
	 */
	public void deleteWitposMQLogS(List<Map<String, Object>> list) {
		witBaseServiceImpl.deleteAll(list, "BINBEMQSYN02.deleteWitposMQLogS");
	}
	
	/**
	 * 删除cherry数据库中新后台已接收成功的MQ收发日志
	 * 
	 * @param list 删除条件
	 */
	public void deleteCherryMQLogR(List<Map<String, Object>> list) {
		baseServiceImpl.deleteAll(list, "BINBEMQSYN02.deleteCherryMQLogR");
	}
	
	/**
	 * 删除cherry数据库中老后台已接收成功的MQ收发日志
	 * 
	 * @param list 删除条件
	 */
	public void deleteCherryMQLogS(List<Map<String, Object>> list) {
		baseServiceImpl.deleteAll(list, "BINBEMQSYN02.deleteCherryMQLogS");
	}
	
	/**
	 * 判断JMSXGroupID字段是否存在
	 * 
	 * @param map 查询条件
	 * @return true：存在，false：不存在
	 */
	public boolean getJMSXGroupIDCol(Map<String, Object> map){
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEMQSYN02.getJMSXGroupIDCol");
		Map<String, Object> result = (Map<String, Object>)witBaseServiceImpl.get(map);
		if(result != null) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 查询POS品牌需要重新发送的MQ日志List（带有JMSXGroupID的查询）
	 * 
	 * @param map 查询条件
	 * @return 日志数据List
	 */
	public List<Map<String, Object>> getWitposMQLogSendGroupIDList(Map<String, Object> map){
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEMQSYN02.getWitposMQLogSendGroupIDList");
		return witBaseServiceImpl.getList(map);
	}
	
	/**
	 * 查询POS品牌需要重新发送的MQ日志List
	 * 
	 * @param map 查询条件
	 * @return 日志数据List
	 */
	public List<Map<String, Object>> getWitposMQLogSendList(Map<String, Object> map){
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEMQSYN02.getWitposMQLogSendList");
		return witBaseServiceImpl.getList(map);
	}
	
	/**
	 * 查询Cherry需要重新发送的MQ日志List
	 * 
	 * @param map 查询条件
	 * @return 日志数据List
	 */
	public List<Map<String, Object>> getCherryMQLogSendList(Map<String, Object> map){
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEMQSYN02.getCherryMQLogSendList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 把POS品牌接收失败的MQ日志更新成未比对状态
	 * 
	 * @param list 更新条件
	 */
	public void updateWitReceiveFlag(List<Map<String, Object>> list) {
		witBaseServiceImpl.updateAll(list, "BINBEMQSYN02.updateWitReceiveFlag");
	}
	
	/**
	 * 把Cherry接收失败的MQ日志更新成未比对状态
	 * 
	 * @param list 更新条件
	 */
	public void updateCherryReceiveFlag(List<Map<String, Object>> list) {
		baseServiceImpl.updateAll(list, "BINBEMQSYN02.updateCherryReceiveFlag");
	}
}

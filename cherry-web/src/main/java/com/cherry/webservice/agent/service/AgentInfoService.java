package com.cherry.webservice.agent.service;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 微商接口Service
 * 
 * @author WangCT
 * @version 2015-08-04 1.0.0
 */
public class AgentInfoService extends BaseService {
	
	/**
	 * 添加微商申请单据
	 * 
	 */
	public void addAgentApply(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "AgentInfo.addAgentApply");
		baseServiceImpl.save(map);
	}
	
	/**
	 * 添加微商操作履历
	 * 
	 */
	public void addAgentApplyLog(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "AgentInfo.addAgentApplyLog");
		baseServiceImpl.save(map);
	}
	
	/**
	 * 审核微商申请单据
	 * 
	 */
	public void auditAgentApply(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "AgentInfo.auditAgentApply");
		baseServiceImpl.update(map);
	}
	
	/**
	 * 分配微商申请单据
	 * 
	 */
	public void assignAgentApply(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "AgentInfo.assignAgentApply");
		baseServiceImpl.update(map);
	}
	
	/**
	 * 查询微商申请单据信息
	 * 
	 */
	public Map<String,Object> getAgentApplyInfo(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "AgentInfo.getAgentApplyInfo");
		return (Map<String,Object>)baseServiceImpl.get(map);
	}
	
	/**
	 * 查询微商申请单据是否存在
	 * 
	 */
	public List<Map<String,Object>> getAgentApplyExist(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "AgentInfo.getAgentApplyExist");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 微商微信绑定
	 * 
	 */
	public void agentBind(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "AgentInfo.agentBind");
		baseServiceImpl.update(map);
	}

	/**
	 * 判断是否是保留号
	 * @param map
	 * @return
	 */
	public Map<String, Object> getReservedCode(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "AgentInfo.getReservedCode");
		return (Map<String,Object>)baseServiceImpl.get(map);
	}

	/**
	 * 更新取号表
	 * @param map
	 */
	public int updateSequenceId(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "AgentInfo.updateSequenceId");
		return baseServiceImpl.update(map);
	}

	/**
	 * 判断员工是否存在
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getEmpExistsMap(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "AgentInfo.getEmpExistsMap");
		return (Map<String,Object>)baseServiceImpl.get(map);
	}
	
	/**
	 * 更新手机号
	 * @param map
	 */
	public void updateAgentMobile(Map<String, Object> map) {
		baseServiceImpl.update(map, "AgentInfo.updateAgentMobile");
	}

	/**
	 * 更新微商手机号，银行帐户
	 * @param map
	 */
	public void updateAgentMobOrAccInfo(Map<String, Object> map) {
		baseServiceImpl.save(map, "AgentInfo.updateAgentMobOrAccInfo");
		
	}

	/**
	 * 删除原有账户信息
	 * @param paramMap
	 */
	public void deleteAccInfo(Map<String, Object> map) {
		baseServiceImpl.remove(map, "AgentInfo.deleteAccInfo");
	}
}

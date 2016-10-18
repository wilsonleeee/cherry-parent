package com.cherry.webservice.agent.interfaces;

import java.util.Map;

/**
 * 微商接口
 * 
 * @author WangCT
 * @version 2015-08-04 1.0.0
 */
public interface AgentInfo_IF {
	
	Map<String, Object> tran_agentApply(Map<String, Object> map);
	Map<String, Object> tran_agentAudit(Map<String, Object> map);
	Map<String, Object> getAgentInfo(Map<String, Object> map);
	Map<String, Object> tran_agentBind(Map<String, Object> map);

}

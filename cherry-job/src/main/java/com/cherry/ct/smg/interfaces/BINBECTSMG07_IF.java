package com.cherry.ct.smg.interfaces;

import java.util.Map;

public interface BINBECTSMG07_IF {
	/**
	 * 短信发送
	 * 
	 * @param Map 
	 * @throws Exception 
	 */
	public String SmsSend (Map<String, Object> map, Map<String, Object> ifConfigMap) throws Exception;
	
	/**
	 * 短信接收
	 * 
	 * @param Map 
	 * @throws Exception 
	 */
	public boolean SmsReceive (Map<String, Object> map, Map<String, Object> ifConfigMap) throws Exception;
	
	/**
	 * 获取短信发送状态报告
	 * 
	 * @param Map 
	 * @throws Exception 
	 */
	public boolean SmsGetSendRpt (Map<String, Object> map, Map<String, Object> ifConfigMap) throws Exception;
	
	/** 
	 * 电话呼出
	 * 
	 * @param Map 呼出信息Map
	 * @param ifConfigMap 配置项Map
	 * @throws Exception 
	 */
	public String phoneCall (Map<String, Object> map, Map<String, Object> ifConfigMap) throws Exception;
}

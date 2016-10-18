package com.cherry.ct.common.interfaces;

import java.util.Map;

public interface BINOLCTCOM07_IF {
	/**
	 * 发送信息
	 * 
	 * @param Map 
	 * @throws Exception 
	 */
	public void tran_sendMsg (Map<String, Object> map) throws Exception;
	
	/**
	 * 根据会员号获取会员ID
	 * 
	 * @param Map 
	 * @throws Exception 
	 */
	public String getMemberIdByCode (String memberCode) throws Exception;
	
	/**
	 * 根据会员ID获取会员信息
	 * 
	 * @param Map 
	 * @throws Exception 
	 */
	public Map<String, Object> getMemberInfoById (Map<String, Object> map) throws Exception;
}

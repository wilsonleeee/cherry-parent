package com.cherry.st.jcs.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.ICherryInterface;

public interface BINOLSTJCS06_IF extends ICherryInterface{

	/**
	 * 根据品牌得到逻辑仓库list
	 * 
	 * @param map
	 * @return
	 * */
	public List<Map<String,Object>> getLogInvByBrand(Map<String, Object> map);
	
	/**
	 * 根据逻辑仓库Id得到逻辑仓库 
	 * 
	 * @param map
	 * @return
	 * */
	public Map<String,Object> getLogInvByLogInvId(Map<String, Object> map);
	
	/**
	 * 更新逻辑仓库
	 * 
	 * @param map
	 * @return
	 * */
	public int tran_updateLogInv(Map<String, Object> map) throws Exception;
	
	/**
	 * 保存逻辑仓库
	 * 
	 * @param map
	 * @return
	 * */
	public int tran_insertLogInv(Map<String, Object> map) throws Exception;
	
}

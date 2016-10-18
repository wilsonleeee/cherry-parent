/*
 * @(#)BINOLSTJCS01_BL.java     1.0 2011/08/25
 * 
 * Copyright (c) 2010 SHANGHAI BINGKUN DIGITAL TECHNOLOGY CO.,LTD
 * All rights reserved
 * 
 * This software is the confidential and proprietary information of 
 * SHANGHAI BINGKUN.("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with SHANGHAI BINGKUN.
 */
/**
 * 
 * 逻辑仓库与业务关联
 * @author LuoHong
 * @version 1.0 2011.08.25
 * 
 **/
package com.cherry.st.jcs.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.core.ICherryInterface;

@SuppressWarnings("unchecked")
public interface BINOLSTJCS09_IF extends ICherryInterface {
	/**
	 * 取得仓库总数
	 * 
	 * @param map
	 * 
	 * @return
	 */
	public int searchLogicDepotCount(Map<String, Object> map);

	/**
	 * 取得逻辑仓库List
	 * 
	 * @param map
	 * 
	 * @return
	 */
	public List<Map<String, Object>> searchLogicDepotList(
			Map<String, Object> map);

	/**
	 * 新增逻辑仓库配置关系
	 * 
	 * @param map
	 * 
	 * @return
	 */
	public void tran_addLogicDepot(Map<String, Object> map) throws Exception;

	/**
	 * 取得逻辑仓库业务配置INFO
	 * 
	 * @param map
	 * 
	 * @return
	 */
	public Map getLogicDepots(Map<String, Object> map);

	/**
	 * 编辑保存逻辑仓库业务配置INFO
	 * 
	 * @param map
	 * 
	 * @return
	 */
	public void tran_EditInfosave(Map<String, Object> map) throws Exception;

	/**
	 * 删除逻辑仓库配置关系
	 * 
	 * @param map
	 * @return
	 * */
	public void tran_deleteLogicDepot(Map<String, Object> map)
			throws Exception;
	
	/**
	 * 根据参数取得逻辑仓库信息
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> getLogicDepotByPraMap(Map<String,Object> map);
	
}

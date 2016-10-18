/*  
 * @(#)BINOLMOMAN04_IF.java     1.0 2011/05/31      
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
package com.cherry.mo.man.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryException;
import com.cherry.cm.core.ICherryInterface;

public interface BINOLMOMAN04_IF extends ICherryInterface {

	// 获取树形节点信息
	public List<Map<String, Object>> getTreeNodesList(Map<String, Object> map);

	// 获取下级区域的信息
	public List<Map<String, Object>> getSubRegionList(Map<String, Object> map);

	// 获取含有柜台的大区信息
	public List<Map<String, Object>> getRegionList(Map<String, Object> map);

	// 获取没有升级的柜台的大区信息
	public String getLeftRootNodes(Map<String, Object> map);

	// 获取没有升级到正式版本的柜台的大区信息
	public String getRightRootNodes(Map<String, Object> map);

	// 更新柜台机器升级状态
	public void tran_updateCounterUpdateStatus(List<Map<String, Object>> list,
			Map<String, Object> map) throws Exception;

	public String getRegionNoUpdateStatus(Map<String, Object> map);

	public List<Map<String, Object>> getSubRegionNoUpdateStatus(
			Map<String, Object> map);

	public void tran_fromUpdateStatusToNone(List<Map<String, Object>> list)
			throws CherryException;

	public void tran_fromNoneToUpdateStatus(List<Map<String, Object>> list,
			Map<String, Object> map) throws CherryException;
	
	/**
	 * 取得过滤后机器类型
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getMachineTypeListFilter(Map<String, Object> map);
}

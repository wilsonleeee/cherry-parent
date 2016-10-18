/*
 * @(#)BINOLSSPRM67_IF.java    1.0 2015/09/21
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
package com.cherry.ss.prm.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.ICherryInterface;

/**
 * 
 * 促销品关联Interface
 * 
 * @author 胡金辉
 * @version 1.0 2015.09.21
 */
public interface BINOLSSPRM71_IF extends ICherryInterface {

	/**
	 * 查询关联的促销品数量
	 * @param searchMap
	 * @return
	 */
	public int getPrmCount(Map<String, Object> searchMap);

	/**
	 * 查询相关联的促销品
	 * @param searchMap
	 * @return
	 */
	public List<Map<String, Object>> getPrmList(Map<String, Object> searchMap);

	/**
	 * 促销品关联
	 * @param map
	 */
	public void tran_conjunction(Map<String, Object> map);

	
	/**
	 * 促销品关联明细
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getDetailPrmList(Map<String, Object> map);

	/**
	 * 删除分组
	 * @param map
	 */
	public void tran_delGroups(Map<String, Object> map) throws Exception;

	/**
	 * 删除单条
	 * @param map
	 * @return
	 */
	public int tran_delOnePrm(Map<String, Object> map);

	/**
	 * 插入到指定组
	 * @param map
	 */
	public void tran_insertIntoGroup(Map<String, Object> map);

}
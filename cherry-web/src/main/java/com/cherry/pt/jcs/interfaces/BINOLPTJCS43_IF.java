/*
 * @(#)BINOLPTJCS43_IF.java     1.0 2015/10/13
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
package com.cherry.pt.jcs.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.ICherryInterface;

/**
 * 
 * 产品关联维护Interface
 * 
 * @author Hujh
 * @version 1.0 2015.10.13
 */
public interface BINOLPTJCS43_IF extends ICherryInterface{
    
	/**
	 * 查询关联产品的数量
	 * @param map
	 * @return
	 */
	public int getPrtCount(Map<String, Object> map);

	/**
	 * 查询关联的产品
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getPrtList(Map<String, Object> map);

	/**
	 * 产品关联
	 * @param map
	 * @throws Exception
	 */
	public void tran_conjunction(Map<String, Object> map) throws Exception;
	
	/**
	 * 根据BIN_GroupID获取促销品关联明细
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getDetailPrtList(Map<String, Object> map);

	/**
	 * 删除分组
	 * @param map
	 */
	public void tran_delGroups(Map<String, Object> map);

	/**
	 * 在指定组内添加产品
	 * @param map
	 */
	public void tran_insertIntoGroup(Map<String, Object> map);

	/**
	 * 删除指定组内的产品
	 * @param map
	 * @return
	 */
	public int tran_delOnePrt(Map<String, Object> map);

}
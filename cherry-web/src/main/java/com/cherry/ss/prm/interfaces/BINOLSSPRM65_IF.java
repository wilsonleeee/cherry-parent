/*
 * @(#)BINOLSSPRM65_IF.java     1.0 2013/01/25
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
 * 入库明细Interface
 * 
 * @author niushunjie
 * @version 1.0 2012.01.25
 */
public interface BINOLSSPRM65_IF extends ICherryInterface {

    /**
     * 保存入库单
     * @param sessionMap
     * @param mainData
     * @param detailList
     * @throws Exception
     */
	public void tran_save(Map<String,Object> sessionMap,Map<String,Object>mainData,List<String[]>detailList) throws Exception;
	
//	/**
//	 * 提交入库单
//	 * @param sessionMap
//	 * @param mainData
//	 * @param detailList
//	 * @throws Exception
//	 */
//	public void tran_submit(Map<String,Object> sessionMap,Map<String,Object>mainData,List<String[]>detailList) throws Exception;
	
	/**
	 * 工作流中的各种动作
	 * @param sessionMap
	 * @param billInformation
	 * @throws Exception
	 */
	public void tran_doaction(Map<String,Object> sessionMap,Map<String,Object> billInformation) throws Exception;
}
/*  
 * @(#)BINOLMOCIO14_IF.java     1.0 2011/06/14      
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
package com.cherry.mo.cio.interfaces;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryException;
import com.cherry.cm.core.ICherryInterface;

import com.cherry.cm.cmbeans.UserInfo;

/**
 * 
 * 柜台消息发布Interface
 * 
 * @author nanjunbo
 * @version 1.0 2016.09.20
 */
public interface BINOLMOCIO22_IF extends ICherryInterface {
    /**
     * 取得所有区域柜台树
     * 
     * @param map
     * @return 所有区域柜台树
     */
    public List<Map<String, Object>> getAllTree(Map<String, Object> map);
    
    /**
     * 发布
     * 
     * @param listLeft,listRight,map
     * @return 
     */
    public void tran_publish(List<Map<String, Object>> allowList,List<Map<String, Object>> forbiddenList,Map<String, Object> map)throws CherryException;

    /**
     * 取得柜台消息
     * 
     * @param map
     * @return 
     */
    public Map<String, Object> getCounterMessage(Map<String, Object> map);
    
    /**
     * 取得柜台下发控制标志
     * 
     * @param map
     * @return 
     */
    public String getControlFlag(Map<String, Object> map);

    /**
     * 取得大区信息List
     * @param map
     * @return
     */
	public List<Map<String, Object>> getRegionList(Map<String, Object> map);

	/**
	 * 取得渠道柜台树
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getChannelCntList(Map<String, Object> map);
    
	/**
	 * 解析文件
	 * @param file
	 * @param map
	 * @return
	 * @throws CherryException
	 * @throws Exception
	 */
	public List<Map<String, Object>> parseFile(File file, Map<String, Object> map) throws CherryException, Exception;
	
	/**
	 * 批量发布导入的柜台
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> tran_publishImpCnt(Map<String, Object> map) throws Exception;

	/**
	 * 取得与导入柜台下发类型相对立的柜台的组织ID
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getContraryOrgID(Map<String, Object> map) throws Exception;

}

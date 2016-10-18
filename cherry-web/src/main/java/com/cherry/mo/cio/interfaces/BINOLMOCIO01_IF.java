/*  
 * @(#)BINOLMOCIO01_IF.java     1.0 2011/06/09      
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

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.ICherryInterface;

/**
 * 
 * 柜台消息管理Interface
 * 
 * @author niushunjie
 * @version 1.0 2011.06.09
 */
public interface BINOLMOCIO01_IF extends ICherryInterface {
    /**
     * 取得柜台消息总数
     * 
     * @param map
     * @return 柜台消息总数
     */
    public int getCounterMessageCount(Map<String, Object> map);
    
    /**
     * 取得柜台消息List
     * 
     * @param map
     * @return 柜台消息List
     */
    public List<Map<String, Object>> getCounterMessageList(Map<String, Object> map);
    
    /**
     * 新增柜台消息
     * 
     * @param map
     */
    public void tran_addCounterMessage(Map<String, Object> map) throws Exception;
    
    /**
     * 编辑柜台消息（排他）
     * 
     * @param map
     * @return 
     */
    public int tran_modifyCounterMessage(Map<String, Object> map) throws Exception;
    
    /**
     * 删除柜台消息
     * 
     * @param map
     * @return 
     */
    public int tran_deleteCounterMessage(Map<String, Object> map) throws Exception;
    
    /**
     * 取得柜台消息
     * 
     * @param Map
     * @return  
     */
    public Map<String, Object> getCounterMessage(Map<String, Object> map);
    
    /**
	 * 取得已下发的消息的下发柜台及其所属区域
	 * 
	 * */
    @SuppressWarnings("unchecked")
	public List<Map<String,Object>> getMessageRegion(Map<String,Object> map);
    
    /**
     * 取得已下发的消息的下发柜台及其渠道
     * @param map
     * @return
     */
    public List<Map<String, Object>> getMessageChannel(Map<String, Object> map);
    
    /**
     * 取得已下发的消息的下发柜台及其所属组织结构
     * @param map
     * @return
     */
    public List<Map<String, Object>> getMessageOrganize(Map<String, Object> map);
    
    /**
     * 更新柜台消息状态
     * @param map
     * @return
     */
    public int tran_disableOrEnable(Map<String, Object> map) throws Exception;

    /**
     * 取得大区信息List
     * @param map
     * @return
     */
	public List<Map<String, Object>> getRegionList(Map<String, Object> map);
}

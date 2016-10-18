/*
 * @(#)BINOLMOMAN01_IF.java     1.0 2011/3/15
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

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.core.ICherryInterface;
import com.cherry.mo.man.form.BINOLMOMAN01_Form;

/**
 * 
 * 机器查询Interface
 * 
 * @author niushunjie
 * @version 1.0 2011.3.15
 */
public interface BINOLMOMAN01_IF extends ICherryInterface{
    /**
     * 取得机器总数
     * 
     * @param map
     * @return 机器总数
     */
    public int searchMachineInfoCount(Map<String, Object> map);
    
    /**
     * 取得机器List
     * 
     * @param map
     * @return 机器List
     */
    public List<Map<String, Object>> searchMachineInfoList(Map<String, Object> map);
    
    /**
     * 停用
     * 
     * @param map
     * @return
     */
    public int tran_disableMachine(Map<String, Object> map) throws CherryException;
    
    /**
     * 启用
     * 
     * @param map
     * @return
     */
    public int tran_enableMachine(Map<String, Object> map) throws CherryException;
    
    /**
     * 解除绑定
     * 
     * @param map
     * @return
     */
    public int tran_unbindCounter(Map<String, Object> map) throws CherryException;
    
    /**
     * 伦理删除机器
     * 
     * @param map
     * @return
     */
    public int tran_deleteMachine(Map<String, Object> map) throws Exception;
    
    /**
     * 报废机器
     * @param map
     * @return
     * @throws Exception
     */
    public int tran_scrapMachine(Map<String, Object> map) throws Exception;
    
    /**
     * 机器下发
     * 
     * 
     * */
    public void issueMachine(BINOLMOMAN01_Form form,UserInfo userInfo) throws Exception;
}

/*
 * @(#)BINOLMOMAN02_IF.java     1.0 2011/4/2
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

import java.io.File;
import java.util.List;
import java.util.Map;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.ICherryInterface;
import com.cherry.mo.man.form.BINOLMOMAN02_Form;

/**
 * 
 * 添加机器Interface
 * 
 * @author niushunjie
 * @version 1.0 2011.4.2
 */
public interface BINOLMOMAN02_IF extends ICherryInterface{
    /**
     * 添加机器
     * 
     * @param map
     * @return 
     */
    public void tran_addMachineInfo(BINOLMOMAN02_Form form,UserInfo userInfo)throws Exception;
    
    /**
     * 判断机器编号是否已经存在
     * 
     * @param map
     * @return 旧机器编号
     */
    public List<Map<String ,Object>> getMachineInfoId(Map<String, Object> map);

    /**
     * 解析文件，以便于画面显示
     * @param fileNameFull
     * @param language
     * @param form
     * @return
     * @throws Exception 
     */
    public List<Map<String ,Object>> parsefile(File upExcel,BINOLMOMAN02_Form form,UserInfo userInfo,String language) throws Exception;

    /**
     * 取得品牌末位码
     * 
     * @param map
     * @return 品牌末位码
     */
    public String getBrandLastCode(Map<String, Object> map);
    
    /**
     * 取得品牌简称
     * 
     * @param map
     * @return 品牌简称
     */
    public String getBrandNameShort(Map<String, Object> map);
    
    /**
     * 取得品牌编号
     * 
     * @param map
     * @return 品牌编号
     */
    public String getBrandCode(Map<String, Object> map);
    
    /**
     * 从新机器号截取旧机器号
     * @param machineCode
     * @param machineType
     * @param lastCode
     * @return machineCodeOld
     */
    public String getMachineCodeOld(String machineCode,String machineType,String lastCode);
}
